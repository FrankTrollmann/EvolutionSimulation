package de.trollmann.evolutionSimulation.model.net.functions.out;

import java.util.List;

import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.net.functions.OutFunction;

public class UniformOutFunction extends OutFunction {

	
	public UniformOutFunction() {
		this.arity = Arity.MULTIPLE;
	}
	@Override
	public void applyToOutput(double value, List<Output> outputs) {
		double average = value / outputs.size();
		for (Output output : outputs) {
			output.applySignal(average);
		}

	}

	@Override
	public OutFunction copy() {
		return new UniformOutFunction();
	}
	
	@Override
	public void mutate() {
		// nothing to mutate
		
	}

}
