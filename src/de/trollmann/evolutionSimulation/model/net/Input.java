package de.trollmann.evolutionSimulation.model.net;

import java.util.List;

/**
 * an element that can receive an input signal
 * @author frank
 *
 */
public class Input {
	
	HasInputConnections parent;
	public HasInputConnections getParent() {
		return parent;
	}
	
	/**
	 * the connection that gives a signal to this input.
	 */
	Connection connection = null;
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	/**
	 * constructor
	 * @param conn
	 */
	public Input(HasInputConnections parent) {
		super();
		this.parent = parent;
	}
	
	/**
	 * get the signal
	 * @return
	 */
	public double getSignal() {
		if(connection == null) return 0;
		return Math.max(0, connection.takeSignal());
	}
	
	/**
	 * check whether there is a signal on the connection.
	 * @return
	 */
	public boolean hasSignal() {
		return connection != null && connection.peekSignal() >= 0;
	}
	
	/**
	 * adds all predecessor to list if exists and is of type HasInputConnections
	 * @param predecessors
	 */
	public void addPredecessor(List<HasInputConnections> predecessors) {
		// add movement input
		if(getConnection() != null) {
			Output sender =  getConnection().getSender();
			if(sender != null && sender instanceof HasInputConnections) {
				predecessors.add((HasInputConnections) sender);
			}
		}
	}
	

}
