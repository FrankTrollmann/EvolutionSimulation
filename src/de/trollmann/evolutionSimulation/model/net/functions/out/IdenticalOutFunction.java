package de.trollmann.evolutionSimulation.model.net.functions.out;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.net.functions.OutFunction;

public class IdenticalOutFunction extends OutFunction {

	
	public IdenticalOutFunction() {
		this.arity = Arity.MULTIPLE;
	}
	
	@Override
	public void applyToOutput(double value, List<Output> outputs) {
		for (Output output : outputs) {
			output.applySignal(value);
		}

	}

	@Override
	public OutFunction copy() {
		return new IdenticalOutFunction();
	}
	
	@Override
	public void mutate() {
		// nothing to mutate
	}

}
