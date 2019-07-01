package de.trollmann.evolutionSimulation.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.FoodParticle;
import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;
import de.trollmann.evolutionSimulation.model.time.CanAct;
import de.trollmann.evolutionSimulation.model.time.CanDoMoveAction;
import de.trollmann.evolutionSimulation.model.time.CanDraw;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * represents the overall world model
 * @author frank
 *
 */
public class WorldModel implements CanDraw, CanAct, CanDoMoveAction{
	
	
	
	
	/**
	 * the food particles currently in the world
	 */
	private List<FoodParticle> foodParticles;
	
	/**
	 * getter for food particle list
	 * @return
	 */
	public List<FoodParticle> getFoodParticles() {
		return foodParticles;
	}
	
	
	/**
	 * the creatures in the world
	 */
	List<Creature> creatures;
	public List<Creature> getCreatures() {
		return creatures;
	}
	
	
	public double getBoundEnergy() {
		double energy = 0;
		for (Creature creature : creatures) {
			energy += creature.getEnergy();
		}
		for (FoodParticle particle : foodParticles) {
			energy += particle.getNrBites() * Configuration.foodParticleNutrition;
		}
		return energy;
	}
	
	/**
	 * temp storage for new creatures to add to the world.
	 */
	List<Creature> newCreatures = new LinkedList<Creature>();
	public List<Creature> getNewCreatures() {
		return newCreatures;
	}
	
	/**
	 * temp storage for creatures that are dead
	 */
	List<Creature> deadCreatures = new LinkedList<Creature>();
	public List<Creature> getDeadCreatures() {
		return deadCreatures;
	}
	
	/**
	 * History of evolution variants.
	 * List of Creatures, each representing an evolution variant
	 */
	MutationHistoryNode evolutionHistory;
//	List<Creature> evolutionHistory = new LinkedList<Creature>();
		public MutationHistoryNode getEvolutionHistory() {
			return evolutionHistory;
		}
		public void setEvolutionHistory(MutationHistoryNode evolutionHistory) {
			this.evolutionHistory = evolutionHistory;
		}
		
	/**
	 * listener for evolution updates
	 */
	EvolutionUpdateListener evolutionUpdateListener;
		public void setEvolutionUpdateListener(
				EvolutionUpdateListener evolutionUpdateListener) {
			this.evolutionUpdateListener = evolutionUpdateListener;
		}
		public EvolutionUpdateListener getEvolutionUpdateListener() {
			return evolutionUpdateListener;
		}
	
		
		
	/**
	 * constructors
	 */
	public WorldModel() {
		super();
		foodParticles = new LinkedList<FoodParticle>();
		 creatures = new LinkedList<Creature>();
	}



	/**
	 * adds a new food particle at a specific position 

	 */
	public void addRandomFoodParticle() {
		long x = RandomGenerator.nextLong(Configuration.maxX);
		long y = RandomGenerator.nextLong(Configuration.maxY);
		FoodParticle newParticle = new FoodParticle(this, x, y);
		for (FoodParticle foodParticle : this.getFoodParticles()) {
			if(foodParticle.getPositionDistance(newParticle) < foodParticle.getRadius() + newParticle.getRadius()) {
				foodParticle.addBite();
				return;
			}
				
		}
		foodParticles.add(newParticle);
		
	}
	
	@Override
	public void draw(DrawableCanvas view) {
		for(FoodParticle food : getFoodParticles()) {
			food.draw(view);
		}
		
		for(Creature creature: creatures) {
			creature.draw(view);
		}
	}
	
	@Override
	public void act() {
		for (Creature creature : creatures) {
			creature.act();
		}
		
		creatures.removeAll(deadCreatures);
		deadCreatures.clear();
		
		creatures.addAll(newCreatures);
		newCreatures.clear();
		

		
		
		foodParticles.removeIf(new Predicate<FoodParticle>() {
			@Override
			public boolean test(FoodParticle t) {
				return t.isEmpty();
			}
		});
		
	}
	
	@Override
	public void move() {
		for (Creature creature : creatures) {
			creature.move();
		}
	}
	
	/**
	 * listener interface for updates in evolution history
	 * @author frank
	 *
	 */
	public interface EvolutionUpdateListener {
		public void onEvolutionUpdate();
	}
	
}
