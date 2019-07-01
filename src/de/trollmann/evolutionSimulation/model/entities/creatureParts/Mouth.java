package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.FoodParticle;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 *  a mouth component
 * @author frank
 */
public class Mouth extends CreatureComponent {

	/**
	 * the radius this mouth is drawn with
	 */
	final long radius = 10;
	
	@Override
	public void draw(DrawableCanvas view, double x, double y, double angle) {
		view.drawCircle(x, y, radius, Color.PINK);
		
	}
	
	@Override
	public void drawSchematic(DrawableCanvas view, double x, double y, double angle) {
		draw(view, x, y, angle);
	}
	
	@Override
	public void actInternal() {
		for(FoodParticle food : creature.getWorldModel().getFoodParticles()) {
			if(food.getPositionDistance(x,y) <= radius + food.getRadius()) {
				boolean successful = food.takeBite();
				if(successful) creature.takeBite();
			}
		}
	}
	
	@Override
	public long getWeight() {
		// always has radius 10. so always weight of 1
		return 1;
	}
	
	@Override
	public long getBiggestPartSize() {
		return radius;
	}
	@Override
	public void postInitialize(Creature creature) {
		this.creature = creature;
		// nothing to do here
	}
	
	@Override
	public CreatureComponent copy() {
		Mouth mouth = new Mouth();
		return mouth;
	}
	
	@Override
	public void getEvolutionOptions(List<EvolutionOption> options) {
		// nothing to evolve
	}
	
	@Override
	protected void propagateVisitorToChildren(CreatureComponentVisitor visitor, boolean childrenFirst) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * create a random mouth object
	 * @return
	 */
	public static Mouth createRandomMouth(MassComponent parent) {
		Mouth mouth = new Mouth();
		return mouth;
	}
	
	@Override
	public void calculateEnergyRequirement() {
		this.energyRequirement = 0.01 / Configuration.averageCreatureLifeLength;
	}
}
