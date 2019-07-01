package de.trollmann.evolutionSimulation.model.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.trollmann.evolutionSimulation.model.WorldModel;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.CreatureComponent;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.CreatureComponentVisitor;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.MassComponent;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.ConstantOne;
import de.trollmann.evolutionSimulation.model.physics.HasSpeed;
import de.trollmann.evolutionSimulation.model.time.CanAct;
import de.trollmann.evolutionSimulation.model.time.CanDraw;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * wrapper for one creature
 * @author frank
 *
 */
public class Creature extends HasSpeed implements CanDraw, CanAct{
	
	/**
	 * backwards reference to the world model
	 */
	WorldModel worldModel;
		public WorldModel getWorldModel() {
			return worldModel;
		}

	
	/**
	 * the head of the creature. 
	 * This is the root of the list of components
	 */
	MassComponent head;
	public void setHead(MassComponent head) {
		this.head = head;
	}
	
	/**
	 * the weight of the creature
	 * derived as sum of the weight of body parts
	 */
	double weight = -1;
	
	/**
	 * the inital energy value.
	 */
	double initialEnergy = 0;
		/**
		 * getter
		 * @return
		 */
		public double getInitialEnergy() {
			return initialEnergy;
		}
		/**
		 * adder
		 * @param energy
		 */
		protected void  addInitialEnergy(double energy) {
			this.initialEnergy += energy;
		}
	
//	/**
//	 * threshold for reproduction. as soon as life force exceeds this threshold a child is produced
//	 */
//	long reproductionThreshold = -1;
	
	/**
	 * the size of the biggest creature part
	 * used to scale its speed. 
	 * e.g. a drift of 1 wil move the creature for it's whole length
	 */
	long biggestPartSize = -1;
	
	
	/**
	 * the current energy of the creature
	 */
	double energy;
		/**
		 * getter
		 * @return
		 */
		public double getEnergy() {
			return energy;
		}
	
	
	/**
	 * the current amount of food consumed
	 */
//	long nutritionValue = 0;
	
	/**
	 * the life force of the creature
	 * the creature dies when this reaches 0.
	 * life force is incremented for each food consumed
	 */
//	double lifeForce = 1;
//		public double getLifeForce() {
//			return lifeForce;
//		}
	 
	/**
	 * reference to the respective entry in th emutation history
	 */
	MutationHistoryNode mutationHistoryNode;
		public void setMutationHistoryNode(MutationHistoryNode mutationHistoryNode) {
			this.mutationHistoryNode = mutationHistoryNode;
		}
	
	
	
	
	/**
	 * constructor
	 * @param x x-position
	 * @param y y-position
	 */
	public Creature(WorldModel worldModel, MassComponent head, double x, double y) {
		super(x, y);
		this.worldModel = worldModel;
		this.head = head;
	}

	/**
	 * applies a dirft force to the whole creature
	 * the force will be scaled based on the moved weight the force scale and applied in the angle
	 * @param forceWeight the weight of the fore
	 * @param scale scale value. between 0 and 1
	 * @param angle the angle in which to move
	 */
	public void applyDriftForce(long forceWeight, double scale, double angle) {
		double weightScale = ((double)forceWeight) / weight;
		double distance = weightScale * scale * biggestPartSize;
		
		this.applyForce((long) Math.floor(distance), angle);
		
	}
	
	/**
	 * applies rotation force to the whole creature
	 * the force will be scaled based on the moved weight
	 * @param forceWeight the weight of the force
	 * @param scale scale value between 0 and 1
	 * @param forward whether the rotation is done in clockwise direction
	 */
	public void applyRotationForce(long forceWeight, double scale, boolean forward) {
		
		double baseValue = 0.1;
		if(!forward) baseValue *= -1;
		baseValue*= ((double)(forceWeight)) / weight;
		baseValue *= scale;

		this.setRotationalSpeed(getRotationalSpeed() + baseValue);
	}
	 
	/** 
	 * method to be called after creature initialization has finished
	 * In here we assume that the creature composition is final
	 */
	public void postInitialize() {
		if(head == null) throw new RuntimeException("creature has no body after initialization");
		
		// TODO: can this be merged into postInitialize somehow?
		compileBody();
		this.energy = initialEnergy;
		
		this.weight = head.calculateTreeWeight();
		this.biggestPartSize = head.getBiggestPartSize();
		this.mutationHistoryNode.onCreatureBirth();
		
		head.postInitialize(this);

	}
	
	/**
	 * compiles the body of the creature, filling in blanks that are 
	 */
	public void compileBody() {
		
		head.visitSubtree(new CreatureComponentVisitor() {
			
			
			@Override
			public void visit(CreatureComponent component) {
				component.compile();
				Creature.this.addInitialEnergy(component.getEnergyRequirement());
			}
		}, true);
	}
	
	/**
	 * creature dies. Remove it from wherever they are registered
	 * Also do statistics and cleanups intermally
	 */
	public void deathAndCleanup() {
		// cleanup in world model
		worldModel.getDeadCreatures().add(this);
		
		// cleanup in mutation history
		mutationHistoryNode.onCreatureDeath();
		if(mutationHistoryNode.getNrLivingCreatures() == 0
				&& worldModel.getEvolutionUpdateListener() != null) {
			// if last one view needs to be updated
			// trigger redraw
			worldModel.getEvolutionUpdateListener().onEvolutionUpdate();
		}
	}
	
	/**
	 * increase nutrition balue by taking a bite
	 */
	public void takeBite(double consumedEnergy) {
		this.energy += consumedEnergy;
		
		if(energy > initialEnergy * (1 + Configuration.reproductionCost)) {
			energy -= initialEnergy * Configuration.reproductionCost;
			reproduce();
		}

	}
	
	public void reproduce() {
		Creature copy = copy();
		boolean isMutating = RandomGenerator.nextDouble() <= Configuration.mutationThreshold; 
		if(isMutating) {
			// mutate
			EvolutionOption mutation = copy.mutate();
			MutationHistoryNode newMutationHistoryNode = new MutationHistoryNode(copy.copy(), this.mutationHistoryNode, mutation);
			copy.setMutationHistoryNode(newMutationHistoryNode);
		} else {
			// if no mutation set same evolution variant
			copy.setMutationHistoryNode(this.mutationHistoryNode);
		}
		
		copy.postInitialize();
		worldModel.getNewCreatures().add(copy);
		
		// trigger redraw
		if(isMutating && worldModel.getEvolutionUpdateListener() != null) {
			worldModel.getEvolutionUpdateListener().onEvolutionUpdate();
		}
	}
	
	public Creature copy() {
		
		// create creature at same position but with random rotation
		MassComponent headCopy = (MassComponent) head.copy();
		
		Creature creature = new Creature(worldModel, headCopy, getX(), getY());
		creature.setRotation(RandomGenerator.nextDouble());
	
		
		return creature;
	}
	
	private EvolutionOption mutate() {
		List<EvolutionOption> options = new LinkedList<EvolutionOption>();
		
		head.visitSubtree(new CreatureComponentVisitor() {
			@Override
			public void visit(CreatureComponent component) {
				component.getEvolutionOptions(options);
			}
		},false);
		
		
		head.getEvolutionOptions(options);
		long max = 0;
		for (EvolutionOption option : options) {
			max += option.getWeight();
		}
		
		long selection = RandomGenerator.nextLong(max);
		EvolutionOption selectedOption = null;
		long current = 0;
		for (EvolutionOption option : options) {
			if(current + option.getWeight() > selection) {
				selectedOption = option;
				break;
			}
			current += option.getWeight();
		}
		
		if(selectedOption == null) throw new RuntimeException("did not create a valid option during mutation");
		selectedOption.apply();
		return selectedOption;
	}
	
	@Override
	public void draw(DrawableCanvas view) {
		head.draw(view, getX(), getY(),getRotation());	
	}
	
	/**
	 * draws a schematic view of the creature
	 * @param view
	 * @param x
	 * @param y
	 * @param rotation
	 * @param drawWithMovement
	 */
	public void drawSchematicView(DrawableCanvas view, int x, int y, double rotation) {
		head.drawSchematic(view, x, y, rotation);
	}
	
	@Override
	public void act() {
		energy -= initialEnergy/Configuration.averageCreatureLifeLength;
		if(energy < initialEnergy * Configuration.deathThreshold ) {
			deathAndCleanup();
		} else {
			head.act(this.getX(), this.getY(), this.getRotation());
			// TODO: remove action costs here!
		}
	}

}
