package de.trollmann.evolutionSimulation.model.net.functions.in;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;

public class IdenticalInFunction extends InFunction {

	
	
	public IdenticalInFunction() {
		this.arity = Arity.SINGLE;
	}
	
	@Override
	public double calculate(List<Input> inputs) {
		if(inputs.size() == 0) return 0;
		return inputs.get(0).getSignal();
	}

	@Override
	public InFunction copy() {
		return new IdenticalInFunction();
	}
	
	@Override
	public void mutate() {
		// nothing to mutate
		
	}

}
