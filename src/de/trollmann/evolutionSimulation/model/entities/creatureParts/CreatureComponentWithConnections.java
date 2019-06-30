package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.Output;

/**
 * creature component that has a set of registered connections
 * @author frank
 *
 */
public abstract class CreatureComponentWithConnections extends CreatureComponent {


	/**
	 * list of registered signal outputting components
	 */
	List<Output> registeredOutputs = new LinkedList<Output>();
		public List<Output> getRegisteredOutputs() {
			return registeredOutputs;
		}
		protected void registerOutput(Output output) {
			registeredOutputs.add(output);
		}
	/**
	 * list of registered signal input components
	 */
	List<Input> registeredInputs = new LinkedList<Input>();
		public List<Input> getRegisteredInputs() {
			return registeredInputs;
		}
		protected void registerInputs(Input input) {
			registeredInputs.add(input);
		}
	
	/**
	 * updates the connections after copying an object. Connections are used from originalcomponent based on a map of old and copied connections
	 * @param originalComponent
	 * @param copyMap
	 */
	public abstract void updateConnectionsAfterCopy(CreatureComponentWithConnections originalComponent, Map<Connection,Connection> copyMap);

}
