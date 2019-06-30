package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;

public class AverageInFunction extends InFunction {

	public AverageInFunction() {
		this.arity = Arity.MULTIPLE;
	}
	
	@Override
	public double calculate(List<Input> inputs) {
		double sum = 0;
		for (Input input : inputs) {
			sum += input.getSignal();
		}
		return sum / inputs.size();
	}

	@Override
	public InFunction copy() {
		return new AverageInFunction();
	}

	@Override
	public void mutate() {
		// nothing to mutate
		
	}
}
