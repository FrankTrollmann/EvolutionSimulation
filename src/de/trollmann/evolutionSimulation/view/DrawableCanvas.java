package de.trollmann.evolutionSimulation.view;

import java.awt.Color;
import java.awt.Graphics;

/**
 * interface for a class that can draw stuff in a coordinate system
 * @author frank
 *
 */
public interface DrawableCanvas {

	
	/**
	 * draws one filled circle
	 * @param x x-position
	 * @param y y-position
	 * @param radius radius of the circle
	 * @param color color
	 */
	public void drawCircle(double x, double y, long radius, Color color);
	
	
	/**
	 * @see Graphics.drawArc
	 */
	public void drawArc(double x, double y, double width, double height, int startAngle, int arcAngle, Color color);
	
	
	/**
	 * draw a line
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param color
	 */
	public void drawLine(double x1, double x2, double y1, double y2, Color color);
	
	
	/**
	 * draw a text of a specific color at a specific position
	 * @param color
	 * @param text
	 * @param x
	 * @param y
	 */ 
	public void drawText(Color color, String text, double x, double y);
		
		
	/**
	 * clears the canvas
	 */
	public void clear();
	
	
	/**
	 * pushes all drawn information to the view.
	 * This is double buffering to avoid flickering
	 */
	public void updateView();
}
