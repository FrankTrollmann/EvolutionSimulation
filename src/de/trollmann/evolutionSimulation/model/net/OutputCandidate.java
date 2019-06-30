package de.trollmann.evolutionSimulation.model.net;

/**
 * this class is an output candidate that is not yet established
 * e.g., it represents that an output can be added to a list.
 * This implements a normal output but adds a callback to the setConnection method in which it can establish itself when connected.
 * after connection has been established it behaves exactly like a normal output
 * @author frank
 *
 */
public abstract class OutputCandidate extends Output {

	boolean connectionEstablished = false;
	
	@Override
	public void setConnection(Connection connection) {
		if(!connectionEstablished) {
			onConnectionEstablished();
			connectionEstablished = true;
		}
		
		super.setConnection(connection);
	}
	
	/**
	 * callback to be implemented when connection is established.
	 */
	public abstract void onConnectionEstablished();

	/**
	 * constructor
	 * @param parent
	 */
	public OutputCandidate(HasOutputConnections parent) {
		super(parent);
	}
	
	
}
