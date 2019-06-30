package de.trollmann.evolutionSimulation.util;

import java.util.Random;

/**
 * global random number generator. (wrapper for the instance class random)
 * @author frank
 *
 */
public class RandomGenerator {
	static Random random = new Random(); 
	
	/**
	 * returns a new random long-values number between 0 (inklusive) and max (exclusive)
	 * @param max
	 * @return
	 */
	public static long nextLong(long max) {
		return Math.abs(random.nextLong() % max);
	}
	
	/**
	 * returns a new random long-values number between 0 (inklusive) and max (exclusive)
	 * @param max
	 * @return
	 */
	public static int nextInt(int max) {
		return random.nextInt(max);
	}
	
	/**
	 * returns a new random double
	 * @return
	 */
	public static double nextDouble() {
		return random.nextDouble();
	}
}
