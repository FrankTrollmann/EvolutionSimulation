package de.trollmann.evolutionSimulation.model.net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * a node having memory that is sent in the next round
 * @author frank
 *
 */
public class Memory  implements HasInputConnections {
	
	/**
	 * the memorized state
	 */
	double state;
	
	/**
	 * input node
	 */
	Input input;
	public Input getInput() {
		return input;
	}
	
	/**
	 * if fixInput is active the current state will not be overwritten
	 */
	Input fixInput;
	public Input getFixInput() {
		return fixInput;
	}
	
	/**
	 * reference to the output 
	 */
	MemoryOutput output;
	public MemoryOutput getOutputWrapper() {
		return output;
	}
	public Output getOutput() {
		return output.output;
	}

	
	public Memory() {
		input = new Input(this);
		fixInput = new Input(this);
		output = new MemoryOutput(this);
		state = 0;
	}
	
	@Override
	public void getFreeInputConnections(List<Input> toFill) {
		if(input.getConnection() == null) toFill.add(input);
		if(fixInput.getConnection() == null) toFill.add(fixInput);
	}
	
	
	@Override
	public List<HasInputConnections> getPredecessors() {
		List<HasInputConnections> predecessors = new LinkedList<HasInputConnections>();
		input.addPredecessor(predecessors);
		return predecessors;
	}
	
	@Override
	public boolean isReady() {
		// only input signal matters. if fix input is not set that is no problem
		return input.hasSignal();
	}
	
	@Override
	public void process() {
		if(fixInput.getSignal() < 0.5) {
			this.state = input.getSignal();
		}
		
	}
	
	/**
	 * updates the connections after copying an object. Connections are used from originalcomponent based on a map of old and copied connections
	 * @param originalComponent
	 * @param copyMap
	 */
	public void updateConnectionsAfterCopy(Memory originalComponent, Map<Connection,Connection> copyMap) {
		// copy input
		Connection inputConnection = originalComponent.getInput().getConnection();
		if(inputConnection != null) {
			Connection copy = copyMap.get(inputConnection);
			if(copy != null) {
				copy.setReceiver(input);
			} else {
				throw new RuntimeException("copy did not exist");
			}
		}
		
		// copy fixInput
		Connection fixInputConnection = originalComponent.getFixInput().getConnection();
		if(fixInputConnection != null) {
			Connection copy = copyMap.get(fixInputConnection);
			if(copy != null) {
				copy.setReceiver(fixInput);
			} else {
				throw new RuntimeException("copy did not exist");
			}
		}
		
		// copy output
		Connection outputConnection = originalComponent.getOutput().getConnection();
		if(outputConnection != null) {
			Connection copy = copyMap.get(outputConnection);
			if(copy != null) {
				copy.setSender(getOutput());
			} else {
				throw new RuntimeException("copy did not exist");
			}
		}
	}
	
	
	
	/**
	 * output is handled as its own class so it does not implent hasInputSignal. 
	 * This causes it to break up the dependency calculation
	 * @author frank
	 *
	 */
	public class MemoryOutput implements CanProvideOutputSignal, HasOutputConnections{
		
		/**
		 * reference to the according memory
		 */
		Memory memory;
		
		/**
		 * output node
		 */
		Output output;
		
		public MemoryOutput(Memory memory) {
			this.memory = memory;
			output = new Output(this);
		}
		
		@Override
		public void getFreeOutputConnections(List<Output> toFill) {
			if(output.getConnection() == null) toFill.add(output);
		}
		
		@Override
		public void sendOutputSignal() {
			output.applySignal(memory.state);
		}
		
		
		
	}

}
