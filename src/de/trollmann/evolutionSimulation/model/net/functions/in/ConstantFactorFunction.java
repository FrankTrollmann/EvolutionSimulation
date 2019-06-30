package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;
import de.trollmann.evolutionSimulation.util.RandomGenerator;

public class ConstantFactorFunction extends InFunction {

	double factor;
	
	public ConstantFactorFunction(double factor) {
		this.arity = Arity.SINGLE;
		this.factor = factor;
	}
	
	@Override
	public double calculate(List<Input> inputs) {
		return Math.min(0, factor * inputs.get(0).getSignal());
	}

	@Override
	public InFunction copy() {
		return new ConstantFactorFunction(factor);
	}
	
	@Override
	public void mutate() {
		factor = RandomGenerator.nextDouble();
		
	}

}
