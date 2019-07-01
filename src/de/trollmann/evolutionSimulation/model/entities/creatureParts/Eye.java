package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.FoodParticle;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.net.CanProvideOutputSignal;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.HasOutputConnections;
import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

public class Eye extends CreatureComponentWithConnections implements HasOutputConnections, CanProvideOutputSignal{


	
	/**
	 * output for sending the distance signal
	 */
	Output distanceOutput;
	public Output getDistanceOutput() {
		return distanceOutput;
	}
	
	/**
	 * the maximal sight distance
	 */
	long sightDistance = 20;
	public void setSightDistance(long sightDistance) {
		this.sightDistance = sightDistance;
	}
	
	public Eye() {
		this.radius = 10;
		this.distanceOutput = new Output(this);
		this.registerOutput(distanceOutput);
	}
	
	@Override
	public long getWeight() {
		// always has radius 10. so always weight of 1
		return 1;
	}

	@Override
	public double calculateTreeWeight() {
		return getWeight();
	}
	
	
	@Override
	public void draw(DrawableCanvas view, double x, double y, double angle) {
		view.drawCircle(x, y, radius, Color.YELLOW);
		
	}
	
	@Override
	public void drawSchematic(DrawableCanvas view, double x, double y, double angle) {
		Color transparentYellow = new Color(255, 255, 0, 100);
		view.drawCircle(x, y, sightDistance, transparentYellow);
		draw(view, x, y, angle);
//		view.drawCircle(x, y, sightDistance, color);
	}

	@Override
	public void actInternal() {
		//nothing to do
	}
	
	@Override
	public void sendOutputSignal() {
		
		double minDistance = -1;
		for (FoodParticle foodParticle :  this.creature.getWorldModel().getFoodParticles()) {
			double distance = foodParticle.getDistance(x,y);
			if(minDistance == -1 || distance < minDistance) {
				minDistance = distance;
			} 
		}
			
		if(minDistance <= sightDistance) {
			distanceOutput.applySignal(1);
		} else {
			distanceOutput.applySignal(0);
		}
	}

	@Override
	public void postInitialize(Creature creature) {
		this.creature = creature;
		// nothing to do here
	}

	@Override
	public long getBiggestPartSize() {
		return this.radius;
	}

	@Override
	public CreatureComponent copy() {
		return new Eye();
	}
	


	@Override
	public void getEvolutionOptions(List<EvolutionOption> options) {
		// change sight distance by 10 in positive or negative direction
		options.add(new EvolutionOption(1, "Eye - Change Sight Distance") {
			@Override
			public void apply() {
				long delta = RandomGenerator.nextLong(20) - 10;
				double newSightDistance = Math.max(0,sightDistance + delta);
				setSightDistance((long) newSightDistance); 
				this.description_appender = "Delta: " + delta + " New: " + sightDistance;
			}
		});

	}

	@Override
	protected void propagateVisitorToChildren(CreatureComponentVisitor visitor, boolean childrenFirst) {	}

	@Override
	public void updateConnectionsAfterCopy(
			CreatureComponentWithConnections originalComponent,
			Map<Connection, Connection> copyMap) {
		Eye copiedEye = (Eye) originalComponent;
	
		Connection originalConnection = copiedEye.getDistanceOutput().getConnection(); 
		
		if(originalConnection != null) {
			Connection copiedConnection = copyMap.get(originalConnection);
			if(copiedConnection != null) {
				copiedConnection.setSender(this.distanceOutput);
			} else {
				throw new RuntimeException("connection exists but was not copied!");
			}
		}
		
	}
	
	@Override
	public void getFreeOutputConnections(List<Output> toFill) {
		if(distanceOutput.getConnection() == null) toFill.add(distanceOutput);
	}
	
	/**
	 * create a random eye
	 * @return
	 */
	public static Eye createRandomEye() {
		return new Eye();
	}
	
	@Override
	public void calculateEnergyRequirement() {
		this.energyRequirement = 0.01;
		
	}
}
