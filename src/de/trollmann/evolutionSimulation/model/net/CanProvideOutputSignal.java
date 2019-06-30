package de.trollmann.evolutionSimulation.model.net;

/**
 * interface for everything that can send an output signal
 * This is the class that originates a signal in a tick, not a class that forwards it.
 * @author frank
 *
 */
public interface CanProvideOutputSignal {

	public void sendOutputSignal();
}
