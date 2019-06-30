package de.trollmann.evolutionSimulation.model.net.functions;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.net.functions.in.AverageInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.ConstantFactorFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.IdenticalInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.InvertedInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.out.IdenticalOutFunction;
import de.trollmann.evolutionSimulation.model.net.functions.out.UniformOutFunction;
import de.trollmann.evolutionSimulation.util.RandomGenerator;


/**
 * an out function that uses a calculated value and splits it among the outputs
 * @author frank
 *
 */
public abstract class OutFunction extends Function {

	/**
	 * takes a value and applies it to all output as specified by the function
	 * @param value
	 * @param outputs
	 */
	public abstract void applyToOutput(double value, List<Output> outputs);
	
	/**
	 * function to copy an in function
	 * @return
	 */
	public abstract OutFunction copy();
	
	/**
	 * creates a random out function
	 * @return
	 */
	public static OutFunction getRandomOutFunction() {
		int functionType = RandomGenerator.nextInt(2);
		switch(functionType) {
			case 0 : return new IdenticalOutFunction();
			case 1 : return new UniformOutFunction();
		}
		
		throw new RuntimeException("unable to create out function. Is sth.wrong with the switch case?");
	}
}
