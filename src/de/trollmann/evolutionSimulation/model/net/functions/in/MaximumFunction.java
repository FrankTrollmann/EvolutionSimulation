package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;

public class MaximumFunction extends InFunction {

	public MaximumFunction() {
		this.arity = Arity.MULTIPLE;
				
	}
	
	@Override
	public double calculate(List<Input> inputs) {
		double max = 0;
		for (Input input : inputs) {
			double signal = input.getSignal();
			if(max < signal) max = signal;
		}
		return max;
	}

	@Override
	public InFunction copy() {
		return new MaximumFunction();
	}

	@Override
	public void mutate() {
		// no mutations here

	}

}
