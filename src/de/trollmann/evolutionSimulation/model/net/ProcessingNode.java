package de.trollmann.evolutionSimulation.model.net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.net.functions.Function.Arity;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;
import de.trollmann.evolutionSimulation.model.net.functions.OutFunction;

/**
 * a processing node that can process inputs via a function to outputs
 * @author frank
 *
 */
public class ProcessingNode implements HasInputConnections, HasOutputConnections{

	
	
	/**
	 * the inputs
	 */
	List<Input> inputs = new LinkedList<Input>();
	public Input createInput() {
		Input input = new Input(this);
		inputs.add(input);
		return input;
	}
	
	/**
	 * the outputs
	 */
	List<Output> outputs = new LinkedList<Output>();
	public Output createOutput() {
		Output output = new Output(this);
		outputs.add(output);
		return output;
	}
	
	/**
	 * the function that handles inputs and produces one double from them
	 */
	InFunction inFunction;
	public void setInFunction(InFunction inFunction) {
		this.inFunction = inFunction;
	}
	public InFunction getInFunction() {
		return inFunction;
	}
	
	/**
	 * the function that sends the outputs to the next node
	 */
	OutFunction outFunction;
	public void setOutFunction(OutFunction outFunction) {
		this.outFunction = outFunction;
	}
	public OutFunction getOutFunction() {
		return outFunction;
	}
	
	@Override
	public boolean isReady() {
		for (Input input : inputs) {
			if(!input.hasSignal()) return false;
		}
		return true;
	}

	@Override
	public void process() {
		
		double value = inFunction.calculate(inputs);
		outFunction.applyToOutput(value, outputs);

	}
	
	public ProcessingNode copy() {
		ProcessingNode ret = new ProcessingNode(inFunction.copy(), outFunction.copy());
		return ret;
	}
	
	public void updateConnectionsAfterCopy(
			ProcessingNode originalComponent,
			Map<Connection, Connection> copyMap) {

		// copy inputs
		List<Input> originalInputs = ((ProcessingNode)originalComponent).inputs;
		for (Input originalInput : originalInputs) {
			Connection inputConnection = originalInput.getConnection();
			if(inputConnection != null) {
				Connection copy = copyMap.get(inputConnection);
				if(copy != null) {
					Input input = new Input(this);
					this.inputs.add(input);
					copy.setReceiver(input);
				} else {
					throw new RuntimeException("copy did not exist");
				}
			}
		}
		
		// copy outputs
		List<Output> originalOutputs = ((ProcessingNode)originalComponent).outputs;
		for (Output originalOutput : originalOutputs) {
			Connection outputConnection = originalOutput.getConnection();
			if(outputConnection != null) {
				Connection copy = copyMap.get(outputConnection);
				if(copy != null) {
					Output output = new Output(this);
					this.outputs.add(output);
					copy.setSender(output);
				} else {
					throw new RuntimeException("copy did not exist");
				}
			}
		}
		
	}
	
	
	public ProcessingNode(InFunction inFunction, OutFunction outFunction) {
		this.inFunction = inFunction;
		this.outFunction = outFunction;
	}
	
	@Override
	public List<HasInputConnections> getPredecessors() {
		List<HasInputConnections> ret = new LinkedList<HasInputConnections>();
		for (Input input : inputs) {
			input.addPredecessor(ret);
		}
		return ret;
	}
	
	@Override
	public void getFreeInputConnections(List<Input> toFill) {
		if(inFunction.getArity() == Arity.SINGLE) return;
		
		toFill.add(new InputCandidate(this) {
			@Override
			public void onConnectionEstablished() {
				inputs.add(this);
			}
		});
	}
	
	@Override
	public void getFreeOutputConnections(List<Output> toFill) {
		if(outFunction.getArity() == Arity.SINGLE) return;
		toFill.add(new OutputCandidate(this) {
			@Override
			public void onConnectionEstablished() {
				outputs.add(this);
			}
		});
	}
}
