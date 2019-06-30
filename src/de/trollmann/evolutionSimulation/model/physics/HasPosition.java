package de.trollmann.evolutionSimulation.model.physics;

/**
 * super class for all model elements with position inside of the world.
 * @author frank
 *
 */
public class HasPosition {

	/**
	 * x-coordinate
	 */
	protected double x;
	
	/**
	 * getter for x-coordinate
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * setter for x-coorindate
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * y-coordinate
	 */
	protected double y;
	
	/**
	 * getter for y-coordinate
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * setter for y-coordinate
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	
	/**
	 * the rotation. 
	 * between 0 and 1. 0 means looking into direction X-positive
	 */
	protected double rotation = 0;
	
	
	
	/** getter for rotation
	 * 
	 * @return
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * setter for rotation
	 * @param rotation
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * constructor
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public HasPosition(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * constructor
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param rotation rotation of the entity
	 */
	public HasPosition(double x, double y, double rotation) {
		super();
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	/**
	 * distance to another position
	 * @param other the other one
	 * @return the distance
	 */
	public double getPositionDistance(HasPosition other) {
		double xDist = Math.abs(getX() - other.getX());
		double yDist = Math.abs(getY() - other.getY());
		return Math.sqrt(xDist * xDist + yDist * yDist);
	}

	/**
	 * distance to another position
	 * @param x the x-value of the position
	 * @param y thy y-value of the position
	 * @return the distance
	 */
	public double getPositionDistance(double x, double y) {
		double xDist = Math.abs(getX() - x);
		double yDist = Math.abs(getY() - y);
		return Math.sqrt(xDist * xDist + yDist * yDist);
	}
	
}
