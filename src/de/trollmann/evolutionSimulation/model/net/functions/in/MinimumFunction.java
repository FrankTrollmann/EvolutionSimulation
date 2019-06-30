package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;

/**
 * function that calculates the minimum over all inputs
 * @author frank
 *
 */
public class MinimumFunction extends InFunction {

	
	public MinimumFunction() {
		this.arity = Arity.MULTIPLE;
	}
	
	@Override
	public double calculate(List<Input> inputs) {
		double min = 1;
		for (Input input : inputs) {
			double signal = input.getSignal();
			if(min > signal) min = signal;
		}
		return min;
	}

	@Override
	public InFunction copy() {
		return new MinimumFunction();
	}

	@Override
	public void mutate() {
		// nothing to mutate here

	}

}
