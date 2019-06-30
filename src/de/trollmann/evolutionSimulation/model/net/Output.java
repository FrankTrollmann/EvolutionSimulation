package de.trollmann.evolutionSimulation.model.net;



/**
 * this class represents an output
 * @author frank
 *
 */
public class Output {
	
	HasOutputConnections parent;
	public HasOutputConnections getParent() {
		return parent;
	}
	
	/**
	 * the next input to connect to
	 * null is a valid option and means the signal ends
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
	 * @param connection
	 */
	public Output(HasOutputConnections parent) {
		super();
		this.parent = parent;
	}



	/**
	 * applies the signal to the output and sends it a signal to the input.
	 * @param signal
	 */
	public void applySignal(double signal) {
		if(connection != null) connection.putSignal(signal);
	}
	
	
	
}
