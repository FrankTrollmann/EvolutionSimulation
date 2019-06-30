package de.trollmann.evolutionSimulation.model.net.functions;

import java.util.List;

import javax.swing.text.html.MinimalHTMLWriter;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.in.AverageInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.ConstantFactorFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.IdenticalInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.InvertedInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.MaximumFunction;
import de.trollmann.evolutionSimulation.model.net.functions.in.MinimumFunction;
import de.trollmann.evolutionSimulation.util.RandomGenerator;


/** 
 * an in function that calculates a single value from a list of inputs
 * @author frank
 *
 */
public abstract class InFunction extends Function{

	/**
	 * calculate the value
	 * @param inputs
	 * @return
	 */
	public abstract double calculate(List<Input> inputs);
	
	public abstract InFunction copy();
	
	public static InFunction getRandomInFunction() {
		int functionType = RandomGenerator.nextInt(6);
		switch(functionType) {
			case 0 : return new IdenticalInFunction();
			case 1 : return new InvertedInFunction();
			case 2 : return new ConstantFactorFunction(Math.random());
			case 3 : return new AverageInFunction();
			case 4 : return new MaximumFunction();
			case 5 : return new MinimumFunction();
		}
		
		throw new RuntimeException("unable to create in function. Is sth.wrong with the switch case?");
	}
	
}
