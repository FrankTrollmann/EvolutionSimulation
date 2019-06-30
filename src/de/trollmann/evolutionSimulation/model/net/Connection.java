package de.trollmann.evolutionSimulation.model.net;

import java.awt.im.InputContext;

import de.trollmann.evolutionSimulation.model.entities.creatureParts.MassComponent;

/**
 *  one 1-1 connection in the network. can receive a signal. 
 * @author frank
 *
 */
public class Connection extends InputContext{

	/**
	 * current value of the connection. -1 means the value is not set 
	 * It is also set to -1 after the value has been sent
	 */
	double value;
	
	/**
	 * receiver of the signal
	 */
	Input receiver;
	public Input getReceiver() {
		return receiver;
	}
	public void setReceiver(Input receiver) {
		this.receiver = receiver;
		receiver.setConnection(this);
	}
	
	/**
	 * sender of the signal
	 */
	Output sender;
	public Output getSender() {
		return sender;
	}
	public void setSender(Output sender) {
		this.sender = sender;
		sender.setConnection(this);
	}
	
	
	
	public Connection(MassComponent component) {
		component.addConnection(this);
	}
	
	/**
	 * constructor
	 * does some initial setup by creating the connected input, output and registering itself in the creature
	 * @param receiver
	 * @param sender
	 */
	public Connection(MassComponent component, Output sender, Input receiver) {
		super();
		this.receiver = receiver;
		receiver.setConnection(this);
		this.sender = sender;
		sender.setConnection(this);
		component.addConnection(this);
	}
	
	/**
	 * put a signal on the connection.
	 * @param signal
	 */
	public void putSignal(double signal) {
		this.value = signal;
	}

	
	/**
	 * take the signal from the connection.
	 * signal is reset to -1 afterwards
	 * @return
	 */
	public double takeSignal() {
		double signal = value;
		value = -1;
		return signal;
	}
	
	/**
	 * peek at the signal without taking it away.
	 * can be used to check whether there is a signal without influencing it.
	 * @return
	 */
	public double peekSignal() {
		return value;
	}
	
	
	
}
