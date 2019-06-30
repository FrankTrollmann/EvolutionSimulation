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
	long foodValue;
	
	/**
	 * increase the food value of this particle
	 */
	public void increaseFoodValue() {
		foodValue++;
	}
	
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	public FoodParticle(WorldModel worldModel, long x, long y) {
		super(x, y);
		this.worldModel = worldModel;
		foodValue = 1;
	}

	
	@Override
	public long getRadius() {
		return foodValue * 10;
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
		if(foodValue <= 0) return false;
		foodValue --;
		return true;
	}
	
	/**
	 * test whether any food is left
	 * @return
	 */
	public boolean isEmpty() {
		return foodValue <= 0;
	}

}
