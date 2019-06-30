package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;

public class InvertedInFunction extends InFunction {

	
	public InvertedInFunction() {
		this.arity = Arity.SINGLE;
	}
	@Override
	public double calculate(List<Input> inputs) {
		return 1 - inputs.get(0).getSignal();
	}

	@Override
	public InFunction copy() {
		return new InvertedInFunction();
	}
	
	@Override
	public void mutate() {
		// nothing to mutate
		
	}

}
