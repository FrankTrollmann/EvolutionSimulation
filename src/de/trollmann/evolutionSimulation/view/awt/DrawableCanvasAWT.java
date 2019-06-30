package de.trollmann.evolutionSimulation.view.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * view for the simulation
 * AWT functions are wrapped on purpose to enable substitution by other view implementations. (would need interface creation, though)
 * @author frank
 *
 */
public class DrawableCanvasAWT extends JPanel implements  DrawableCanvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * x-size of this canvas in pixels. Actual canvas may be different size if zoomFactor is not 1.
	 */
	long xSize;
	/**
	 * y-size of this canvas in pixels. Actual canvas may be different size if zoomFactor is not 1.
	 */
	long ySize;
	public void updateSize(long xSize, long ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		onSizeCalculationsChanged();
	}
	
	/**
	 * factor by which the canvas zooms in.
	 */
	double zoomFactor = 1;
	public void increaseZoomFactor(double factor) {
		zoomFactor += factor;
		if(zoomFactor < 0.2) zoomFactor = 0.2;
		onSizeCalculationsChanged();
	}
	
	private void onSizeCalculationsChanged() {
		int actualXSize = (int) Math.floor(xSize * zoomFactor);
		int actualYSize= (int) Math.floor(ySize * zoomFactor);
		this.setPreferredSize(new Dimension(actualXSize,actualYSize));
		this.revalidate();
	}
//	Canvas shownCanvas;
//	Canvas drawCanvas;
	
	Panel canvas;
	
	/**
	 * canvas on which simulation is drawn
	 */
	BufferedImage drawCanvas;
	
	BufferedImage showCanvas;
	
	
	/**
	 * constructor
	 */
	public DrawableCanvasAWT(long xSize, long ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getScreenDevices()[0];
		GraphicsConfiguration conf = gd.getConfigurations()[0];
		drawCanvas = conf.createCompatibleImage((int)Configuration.maxX, (int)Configuration.maxY);
		drawCanvas.createGraphics();
		showCanvas = conf.createCompatibleImage((int)Configuration.maxX, (int)Configuration.maxY);
		showCanvas.createGraphics();
		
		
	}
	
	
	/**
	 * clears the canvas
	 */
	public void clear() {
		int actualXSize = (int) Math.floor(xSize * zoomFactor);
		int actualYSize= (int) Math.floor(ySize * zoomFactor);
		drawCanvas.getGraphics().clearRect(0, 0, actualXSize, actualYSize);
		
	}
	
	/**
	 * draws one filled circle
	 * @param x x-position
	 * @param y y-position
	 * @param radius radius of the circle
	 * @param color color
	 */
	public void drawCircle(double x, double y, long radius, Color color) {

		x = x * zoomFactor;
		y = y * zoomFactor;
		radius = (int) Math.floor(radius * zoomFactor);
		
		Graphics graphics = drawCanvas.getGraphics();
		graphics.setColor(color);
		graphics.fillOval((int)(Math.floor(x-radius)), (int)(Math.floor(y-radius)), (int)radius * 2, (int)radius*2);
	}
	
	/**
	 * @see Graphics.drawArc
	 */
	public void drawArc(double x, double y, double width, double height, int startAngle, int arcAngle, Color color) {
		x = x * zoomFactor;
		y = y * zoomFactor;
		width = width * zoomFactor;
		height = height  * zoomFactor;
		Graphics graphics = drawCanvas.getGraphics();
		graphics.setColor(color);
		graphics.fillArc((int) (Math.floor(x-width / 2)), (int) (Math.floor(y-height/2)),(int) (Math.floor(width)), (int) (Math.floor(height)), startAngle, arcAngle);
	}
	
	/**
	 * implementation of line draw function
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param color
	 */
	public void drawLine(double x1, double x2, double y1, double y2, Color color) {
		x1 = x1 * zoomFactor;
		x2 = x2 * zoomFactor;
		y1 = y1 * zoomFactor;
		y2 = y2 * zoomFactor;
		
		Graphics2D graphics = (Graphics2D) drawCanvas.getGraphics();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(3));
		graphics.drawLine((int) (Math.floor(x1)), (int) (Math.floor(y1)), (int) (Math.floor(x2)), (int) (Math.floor(y2)));
	}
	
	/**
	 * draw a text of a specific color at a specific position
	 * @param color
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawText(Color color, String text, double x, double y) {
		Graphics2D graphics = (Graphics2D) drawCanvas.getGraphics();
		graphics.setColor(color);
		graphics.drawString(text, (int) Math.floor(x),(int)  Math.floor(y));
	}
	
	/**
	 * pushes all drawn information to the view.
	 * This is double buffering to avoid flickering
	 */
	public void updateView() {
		BufferedImage temp = drawCanvas;
		drawCanvas = showCanvas;
		showCanvas = temp;
		this.repaint();
	}
	
	
	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(showCanvas, 0, 0, this);
	}
	
	
	
}
