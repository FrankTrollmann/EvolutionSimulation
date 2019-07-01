package de.trollmann.evolutionSimulation.model.entities;

import java.awt.Color;

import de.trollmann.evolutionSimulation.model.WorldModel;
import de.trollmann.evolutionSimulation.model.physics.HasRadius;
import de.trollmann.evolutionSimulation.model.time.CanDraw;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;


/**
 * one particle of food.
 * @author frank
 *
 */
public class FoodParticle extends HasRadius implements CanDraw {

	/**
	 * up reference the world model
	 */
	WorldModel worldModel;
	
	/**
	 * nutritionalValue of the food.
	 */
	long nrBites;
	
	/**
	 * getter for the nr of bites
	 * @return
	 */
	public long getNrBites() {
		return nrBites;
	}
	
	/**
	 * increase the food value of this particle by adding another bite
	 */
	public void addBite() {
		nrBites++;
	}
	
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public FoodParticle(WorldModel worldModel, long x, long y) {
		super(x, y);
		this.worldModel = worldModel;
		nrBites = 1;
	}

	
	@Override
	public long getRadius() {
		return nrBites * 10;
	}
	
	@Override
	public void draw(DrawableCanvas view) {
		view.drawCircle(getX(), getY(), getRadius(), Color.green);	
	}
	
	/**
	 * take one bite.
	 * returns true if there is enough food left to take a bite
	 */
	public boolean takeBite() {
		if(nrBites <= 0) return false;
		nrBites --;
		return true;
	}
	
	/**
	 * test whether any food is left
	 * @return
	 */
	public boolean isEmpty() {
		return nrBites <= 0;
	}

}
