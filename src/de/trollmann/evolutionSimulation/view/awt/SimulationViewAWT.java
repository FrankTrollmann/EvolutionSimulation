package de.trollmann.evolutionSimulation.view.awt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;
import de.trollmann.evolutionSimulation.view.SimulationView;

/**
 * AWT implementation of the main simulation view
 * @author frank
 *
 */
public class SimulationViewAWT extends JFrame implements SimulationView{
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	DrawableCanvasAWT mainDrawableCanvas;
	
	
	EvolutionHistoryAWT historyView;
	
	/**
	 * constructor
	 */
	public SimulationViewAWT() {
		super("Evolution - Simulation");
		
		mainDrawableCanvas = new DrawableCanvasAWT(Configuration.maxX, Configuration.maxY);
		JButton button = new JButton("History");
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(historyView != null) {
					historyView.update();
					historyView.setVisible(true);
				}
			}
		});
		
		
		
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(mainDrawableCanvas, BorderLayout.CENTER);
		this.getContentPane().add(button,BorderLayout.NORTH);
		
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	
	@Override
	public DrawableCanvas getMainDrawableCanvas() {
		return mainDrawableCanvas;
	}
	
	/**
	 * display this window
	 */
	public void display(long maxX, long maxY) {
		mainDrawableCanvas.setSize(new Dimension((int)maxX, (int)maxY));
		mainDrawableCanvas.setVisible(true);
		this.setVisible(true);
		this.setSize(new Dimension((int)maxX, (int)maxY));
	}
	
	@Override
	public void initializeHistoryView(MutationHistoryNode evolutionHistory) {
		historyView = new EvolutionHistoryAWT(evolutionHistory);
	}
	
	
	@Override
	public void update(Graphics g) {
		mainDrawableCanvas.paint(g);
	}
	
	@Override
	public void paint(Graphics g) {
		mainDrawableCanvas.paint(g);
	}
	
	@Override
	public void onEvolutionHistoryChange() {
		if(historyView != null && historyView.isVisible()) {
			historyView.update();
		}
	}
	
}
