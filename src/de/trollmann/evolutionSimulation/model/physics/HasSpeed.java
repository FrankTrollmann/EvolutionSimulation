package de.trollmann.evolutionSimulation.model.physics;

import de.trollmann.evolutionSimulation.model.time.CanDoMoveAction;
import de.trollmann.evolutionSimulation.util.Configuration;


public class HasSpeed extends HasPosition implements CanDoMoveAction {

	/**
	 * speed in x-direction
	 */
	private double xSpeed = 0;
	
	
	/**
	 * getter for x-speed
	 * @return
	 */
	public double getXSpeed() {
		return xSpeed;
	}

	/**
	 * setter for x-speed
	 */
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	/**
	 * speed in y-direction
	 */
	private double ySpeed = 0;
	
	
	/**
	 * getter for y-Speed
	 * @return
	 */
	public double getYSpeed() {
		return ySpeed;
	}

	/**
	 * setter for y-speed
	 * @param ySpeed
	 */
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * speed in rotational-direction (interpreted clockwise)
	 */
	private double rotationalSpeed = 0;


	/**
	 * getter for rotational speed;
	 * @return
	 */
	public double getRotationalSpeed() {
		return rotationalSpeed;
	}

	/**
	 * setter for rotational speed
	 * @param rotationalSpeed
	 */
	public void setRotationalSpeed(double rotationalSpeed) {
		this.rotationalSpeed = rotationalSpeed;
	}

	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public HasSpeed(double x, double y) {
		super(x, y);
	}
	
	@Override
	public void move() {
		// update coordinates and rotation
		x += xSpeed;
		if(x <=0) x = 0;
		if(x > Configuration.maxX) x = Configuration.maxX;
		y += ySpeed;
		if(y <=0) y = 0;
		if(y > Configuration.maxY) y = Configuration.maxY;
		rotation += rotationalSpeed;
		rotation = rotation % 1;
		
		// apply decay to speed
		xSpeed *= Configuration.speedDecay;
		ySpeed *= Configuration.speedDecay;
		rotationalSpeed *= Configuration.speedDecay;
	}
	
	public void applyForce(double amount, double direction) {

		double directionRadiant = Math.toRadians(direction * 360);
		
		double xOffset = Math.cos(directionRadiant) * amount;
		xSpeed += xOffset;
		
		double yOffset = Math.sin(directionRadiant) * amount;
		ySpeed += yOffset;
	}

	
}
