package de.trollmann.evolutionSimulation.model.physics;

/**
 * object that has a position and radius
 * can calculate a radius
 * @author frank
 *
 */
public abstract class HasRadius extends HasPosition{

	
	/**
	 * constructor
	 * hands over parameters to constructor of HasPosition
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public HasRadius(double x, double y, double rotation) {
		super(x, y, rotation);
	}

	/**
	 * constructor
	 * hands over parameters to constructor of HasPosition
	 * @param x
	 * @param y
	 */
	public HasRadius(double x, double y) {
		super(x, y);
	}
	
	
	/**
	 * method for calculating a radius
	 * @return
	 */
	public abstract long getRadius();
	
	
	/**
	 * distance to another radius object. subtracts the radius from the distance
	 * @param other the other one
	 * @return the distance
	 */
	public double getDistance(HasRadius other) {
		return Math.max(0, this.getPositionDistance(other) - other.getRadius() - this.getRadius());
	}

	/**
	 * distance to a point.
	 * subrtracts this object's rdius from the distance
	 * @param x the x-value of the position
	 * @param y thy y-value of the position
	 * @return the distance
	 */
	public double getDistance(double x, double y) {
		return Math.max(0, this.getPositionDistance(x,y) - this.getRadius());
	}

}
