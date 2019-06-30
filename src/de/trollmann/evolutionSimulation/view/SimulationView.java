package de.trollmann.evolutionSimulation.view;

import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;

/**
 * interface to abstract from view implementation.
 * This is the overall view of the simulation, consisting of multiple subViews
 * @author frank
 *
 */
public interface SimulationView {
	
	/**
	 * getter for the canvas to draw the main simulation output on.
	 * @return
	 */
	DrawableCanvas getMainDrawableCanvas();
	
	/**
	 * display this window
	 */
	public void display(long maxX, long maxY);
	
	/**
	 * initializes the view of the evolutionHistoy
	 * @param evolutionHistory
	 */
	public void initializeHistoryView(MutationHistoryNode evolutionHistory);

	/**
	 * method that is used to propagate an evolution history change to the view
	 */
	public void onEvolutionHistoryChange();
}
