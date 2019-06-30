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
	 * threshold for mutation. to be interpreted as probability.
	 * 0.2 seems to be a good value
	 */
	public static double mutationThreshold = 0.3;
	
	/**
	 * decay that is multiplied to speed in any direction and rotation on each tick
	 */
	public static double speedDecay = 0.9;
	
	
}
