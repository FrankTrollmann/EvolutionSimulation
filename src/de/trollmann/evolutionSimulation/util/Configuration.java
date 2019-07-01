package de.trollmann.evolutionSimulation.util;

/**
 * static class for configuration parameters
 * @author frank
 *
 */
public class Configuration {
	/**
	 * maximum of x coordinate
	 */
	public static final long maxX = 1000;
	
	/**
	 * maximum of y-coordinate
	 */
	public static final long maxY = 1000;

	
	/**
	 * threshold that decides with which probability a food particle is added
	 * threshold can be interpreted as probability of adding food 
	 * (0.6 means food is added with 60% probability)
	 */
	public static double feedingThreshold = 0.05;
	
	/**
	 * the nutrition value of one food particle.
	 */
	public static double foodParticleNutrition = 0.2;
	
	/**
	 * the energy available to the world. Energy will be held constant by adding food particles as energy is consumed by creatures, to balance the energy level. 
	 */
	public static double worldEnergy = 60;
	
	/**
	 * threshold for mutation. to be interpreted as probability.
	 * 0.2 seems to be a good value
	 */
	public static double mutationThreshold = 0.3;
	
	/**
	 * decay that is multiplied to speed in any direction and rotation on each tick
	 */
	public static double speedDecay = 0.9;
	
	/**
	 * the average length of days it should take to get the initial creature from 100% energy to 0 % energy.
	 * This factor influences energy consumption of creature components.
	 */
	public static double averageCreatureLifeLength = 1000;
	
	
	/**
	 * decides when a creature dies. It dies, when it's current energy is smaller than deathThreshold * initialEnergy. 
	 */
	public static double deathThreshold = 0.2;
	
	/**
	 * decides when a creature reproduces. It reproduces when it's current energy bigger than initialEnergy*(1 + reproductionCost). 
	 * During the birth reproductionCost*initialEnergy energy is lost by the creature.
	 */
	public static double reproductionCost = 1;
	

	
}
