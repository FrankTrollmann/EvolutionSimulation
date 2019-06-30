package de.trollmann.evolutionSimulation.model.time;

import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * interface for everything that can be drawn
 * @author frank
 *
 */
public interface CanDraw {

	/**
	 * method for drawing.
	 * @param view
	 */
	public void draw(DrawableCanvas view);
}
