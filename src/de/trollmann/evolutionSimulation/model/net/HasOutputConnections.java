package de.trollmann.evolutionSimulation.model.net;

import java.util.List;

/**
 * interface for all elements that have output connections
 * @author frank
 *
 */
public interface HasOutputConnections {

	/**
	 * get all usable output connections of this component
	 * @param toFill
	 */
	public void getFreeOutputConnections(List<Output> toFill);
}
