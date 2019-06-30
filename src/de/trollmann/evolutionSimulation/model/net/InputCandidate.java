package de.trollmann.evolutionSimulation.model.net;

/**
 * this class is an input candidate that is not yet established
 * e.g., it represents that an input can be added to a list.
 * This implements a normal input but adds a callback to the setConnection method in which it can establish itself when connected.
 * after connection has been established it behaves exactly like a normal input
 * @author frank
 *
 */
public abstract class InputCandidate extends Input {

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
	public InputCandidate(HasInputConnections parent) {
		super(parent);
	}
	
	
	
}
