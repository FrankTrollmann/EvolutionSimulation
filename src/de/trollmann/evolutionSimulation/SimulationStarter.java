package de.trollmann.evolutionSimulation;

import de.trollmann.evolutionSimulation.controller.SimulationController;
import de.trollmann.evolutionSimulation.view.awt.DrawableCanvasAWT;
import de.trollmann.evolutionSimulation.view.awt.SimulationViewAWT;

/**
 * starter class for the simulation
 * @author frank
 *
 */
public class SimulationStarter {

	/**
	 * the controller of the simulation
	 */
	static SimulationController simController;
	
	/**
	 * the view of the simulation
	 */
	static SimulationViewAWT simView;
	
	public static void main(String[] args) {
		
		simView = new SimulationViewAWT();
		
		/**
		 * start and update the controller
		 */
		simController = new SimulationController(simView);
		simController.start();
	}

}
