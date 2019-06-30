package de.trollmann.evolutionSimulation.model.net;

import java.util.LinkedList;
import java.util.List;

/**
 * a  connection that sends constant ones to all connected outputs
 * @author frank
 *
 */
public class ConstantOne implements CanProvideOutputSignal, HasOutputConnections{
	
	/**
	 * the connected outputs
	 */
	List<Output> outputs = new LinkedList<Output>();
	public List<Output> getOutputs() {
		return outputs;
	}
	public void addOutput(Output output) {
		outputs.add(output);
	}
	public Output getNewOutput() {
		Output ret = new Output(this);
		outputs.add(ret);
		return ret;
	}
	
	@Override
	public void sendOutputSignal() {
		for (Output output : outputs) {
			output.applySignal(1);
		}
	}
	
	@Override
	public void getFreeOutputConnections(List<Output> toFill) {
		toFill.add(new OutputCandidate(this) {
			
			@Override
			public void onConnectionEstablished() {
				outputs.add(this);
			}
		});
	}
}
