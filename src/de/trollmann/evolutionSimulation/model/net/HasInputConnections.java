package de.trollmann.evolutionSimulation.model.net;

import java.util.List;

/**
 * a component that has InputConnections
 * @author frank
 *
 */
public interface HasInputConnections {
	

	
	
	/**
	 * calculates whether the processing node is ready to process
	 * @return
	 */
	public abstract boolean isReady();
	
	/**
	 * triggers the processing
	 * @return
	 */
	public abstract void process();
	
	/**
	 * get all predecessors of this node
	 * @return
	 */
	public abstract List<HasInputConnections> getPredecessors();
	
	/**
	 * get all usable output connections of this component
	 * @param toFill
	 */
	public void getFreeInputConnections(List<Input> toFill);
	
	/**
	 * detect whether node is dependent on the signal from candidate.
	 * This checks transitive closure of getPredecessors
	 * @param node
	 * @param candidate
	 * @return
	 */
	public static boolean isDependentOn(HasInputConnections node, HasInputConnections candidate) {
		for (HasInputConnections predecessor : node.getPredecessors()) {
			if(predecessor.equals(candidate)) return true;
			if(isDependentOn(predecessor, candidate)) return true;
		}
		return false;
	}
}
