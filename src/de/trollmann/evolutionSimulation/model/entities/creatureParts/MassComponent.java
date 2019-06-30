package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.net.CanProvideOutputSignal;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.ConstantOne;
import de.trollmann.evolutionSimulation.model.net.HasInputConnections;
import de.trollmann.evolutionSimulation.model.net.HasOutputConnections;
import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.Memory;
import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.net.ProcessingNode;
import de.trollmann.evolutionSimulation.model.net.functions.InFunction;
import de.trollmann.evolutionSimulation.model.net.functions.OutFunction;
import de.trollmann.evolutionSimulation.model.physics.HasPosition;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * a component that has mass
 * This is the component that the main creatue is built out of. All other components are placed on this creature
 * @author frank
 *
 */
public class MassComponent extends CreatureComponent{

	/**
	 * radius of the mass compoment
	 */
	long radius;
	
	/**
	 * getter for radius
	 * @return
	 */
	public long getRadius() {
		return radius;
	}
	
	/**
	 * reference to all connected components
	 * links are unidirectionl (no connection to the compoment this has bee referenced from , if any)
	 */
	List<ComponentConnection> subComponents;
	
	/**
	 * getter for sub components
	 * @return
	 */
	public List<ComponentConnection> getSubComponents() {
		return subComponents;
	}
	
	public void addComponent(double angle, CreatureComponent component) {
		this.subComponents.add(new ComponentConnection(angle, component));
	}
	
	
	ConstantOne constantSignalProducer = new ConstantOne();
	public ConstantOne getConstantSignalProducer() {
		return constantSignalProducer;
	}
	
	/**
	 * reference to the connections of this List;
	 */
	List<Connection> connections = new LinkedList<Connection>();
	public void addConnection(Connection connection) {
		this.connections.add(connection);
	}
	
	/**
	 * the nodes that do single processing but do not have a 
	 */
	List<ProcessingNode> signalProcessors = new LinkedList<ProcessingNode>();
	public List<ProcessingNode> getSignalProcessors() {
		return signalProcessors;
	}
	
	/**
	 * nodes that can represent a signal for one or more turns
	 */
	List<Memory> memory = new LinkedList<Memory>();
	public List<Memory> getMemory() {
		return memory;
	}
	
	
	List<CanProvideOutputSignal> signalSenders = new LinkedList<CanProvideOutputSignal>();
	
	/**
	 * list of connections that depend on other connections in an order such that they can be processes from 0 to end.
	 */
	List<HasInputConnections> sortedConnections;
	
	
	/**
	 * constructor
	 */
	public MassComponent(long radius) {
		this.radius = radius;
		subComponents = new LinkedList<MassComponent.ComponentConnection>();
	}
	
	@Override
	protected void propagateVisitorToChildren(CreatureComponentVisitor visitor) {
		for (ComponentConnection componentConnection : subComponents) {
			componentConnection.component.visitSubtree(visitor);
		}
	}
	
	@Override
	public void draw(DrawableCanvas view, double x, double y, double angle) {
		
		double blue = creature.getLifeForce();
		if(blue > 1) blue = 1;
		
		Color color = new Color(0, 0, (int) Math.floor(blue * 255));
		
		view.drawCircle(x, y, radius, color);
		
		for (ComponentConnection componentConnection : subComponents) {
			HasPosition childPosition = getChildPosition(x, y, angle + componentConnection.angle);
			componentConnection.component.draw(view, childPosition.getX(), childPosition.getY(), childPosition.getRotation());
		}
	}
	
	@Override
	public void drawSchematic(DrawableCanvas view, double x, double y,
			double angle) {

		Color color = Color.blue;
		
		view.drawCircle(x, y, radius, color);
		
		// collect all input and output components positions in a hashmap for drawing connections
		HashMap<HasInputConnections, HasPosition> inputPositions = new HashMap<HasInputConnections, HasPosition>();
		HashMap<HasOutputConnections, HasPosition> outputPositions = new HashMap<HasOutputConnections, HasPosition>();
		
		// go through all child components
		for (ComponentConnection componentConnection : subComponents) {
			HasPosition childPosition = getChildPosition(x, y, angle + componentConnection.angle);
			componentConnection.component.drawSchematic(view, childPosition.getX(), childPosition.getY(), childPosition.getRotation());
			if(componentConnection.component instanceof HasInputConnections) {
				inputPositions.put((HasInputConnections) componentConnection.component,childPosition);
			}
			if(componentConnection.component instanceof HasOutputConnections) {
				outputPositions.put((HasOutputConnections) componentConnection.component,childPosition);
			}
		}
		
		// draw constant producer 
		HasPosition constantProducerPos = getInnerPosition(x, y, 0, 0);
		view.drawCircle(constantProducerPos.getX(), constantProducerPos.getY(), 5, Color.GRAY);
		outputPositions.put(constantSignalProducer, constantProducerPos);
		
		// draw memory components in an arc
		System.out.println("memory");
		if(this.getMemory().size() > 0) {
			double memoryDistance = this.getRadius() * 0.2;
			double memoryStep = 1.0 / this.getMemory().size();
			for(int i = 0; i < this.getMemory().size(); i++) {
				HasPosition childPosition = getInnerPosition(x, y, memoryDistance, angle + i * memoryStep);
				view.drawCircle(childPosition.getX(), childPosition.getY(), 5, Color.white);
				inputPositions.put(this.getMemory().get(i), childPosition);
				outputPositions.put(this.getMemory().get(i).getOutputWrapper(), childPosition);
			}
			
		
		}
		
		// draw signal processors
		System.out.println("signal processors " + this.getSignalProcessors().size());
		if(this.getSignalProcessors().size() > 0) {
			double processorDistance = this.getRadius() * 0.6;
			double processorOffset = 0.3;
			double processorStep = 1.0 / this.getSignalProcessors().size();
			System.out.println("processorStep" +  processorStep);
			for(int i = 0; i < this.getSignalProcessors().size(); i++) {
				HasPosition childPosition = getInnerPosition(x, y, processorDistance, angle + i * processorStep + processorOffset);
				view.drawCircle(childPosition.getX(), childPosition.getY(), 5, Color.white);
				inputPositions.put(this.getSignalProcessors().get(i), childPosition);
				outputPositions.put(this.getSignalProcessors().get(i), childPosition);
			}
		
		}
		
		// draw all connections
		for(Connection connection : connections) {
			if(connection.getSender() == null || connection.getReceiver() == null) continue;
			HasPosition senderPos = outputPositions.get(connection.getSender().getParent());
			HasPosition receiverPos = inputPositions.get(connection.getReceiver().getParent());
			if(senderPos != null && receiverPos != null) {
				view.drawLine(senderPos.getX(), receiverPos.getX(), senderPos.getY(), receiverPos.getY(), Color.white);
			} 
		}
		
	}
	
	
	@Override
	public void actInternal() {
		// send signal from signal producer
		constantSignalProducer.sendOutputSignal();
		// 1: produce all outputs
		for (CanProvideOutputSignal outputProducer : signalSenders) {
			outputProducer.sendOutputSignal();
		}

		
		// process inputs through net of connections
		// trigger all processing nodes
		for (HasInputConnections connection : sortedConnections) {
			connection.process();
		}
		
		// 3. let everything act.
		for (ComponentConnection componentConnection : subComponents) {
			HasPosition childPosition = getChildPosition(x, y, getRotation() +  componentConnection.angle);
			componentConnection.component.act(childPosition.getX(), childPosition.getY(), childPosition.getRotation());
		}
		
	}
	
	@Override
	public long getWeight() {
		// area: Pi * r²
		// since we assume 1 to be equal to Pi *10*10 we can ignore the Pi and divide radius by 10.
		return (radius/10)*(radius/10);
	}
	
	@Override
	public long getBiggestPartSize() {
		long max = this.radius;
		for (ComponentConnection componentConnection : subComponents) {
			max = Math.max(max, componentConnection.component.getBiggestPartSize());
		}
		return max;
	}
	
	
	@Override
	public void postInitialize(Creature creature) {
		this.creature = creature;
		// nothing to do here
		for (ComponentConnection componentConnection : subComponents) {
			componentConnection.component.postInitialize(creature);
		}
		
		findSignalSenders();
		sortInputComponents();
	}
	
	@Override
	public CreatureComponent copy() {
		MassComponent massComponent = new MassComponent(radius);
		
		// copy connections
		HashMap<Connection,Connection> connectionMap = new HashMap<Connection, Connection>();
		for (Connection connection : connections) {
			Connection connectionCopy = new Connection(massComponent);
			connectionMap.put(connection, connectionCopy);
		}
		
		// copy connections of constant signal producer
		for (Output output : getConstantSignalProducer().getOutputs()) {
			Output newOutput = massComponent.getConstantSignalProducer().getNewOutput();
			Connection copiedConnection = connectionMap.get(output.getConnection());
			if(copiedConnection != null) {
				copiedConnection.setSender(newOutput);
			} else {
				new RuntimeException("connection exists but was not copied!").printStackTrace();
			}
		}
		
		// copy connected components and update connections
		for (ComponentConnection connection : subComponents) {
			CreatureComponent targetCopy = connection.component.copy();
			ComponentConnection connectionCopy = new ComponentConnection(connection.angle, targetCopy);
			massComponent.getSubComponents().add(connectionCopy);
			
			if(targetCopy instanceof CreatureComponentWithConnections) {
				((CreatureComponentWithConnections) targetCopy).updateConnectionsAfterCopy((CreatureComponentWithConnections)connection.component, connectionMap);
			}
		} 

		//copy signal processors and update connections
		for (ProcessingNode signalProcessor : signalProcessors) {
			ProcessingNode signalProcessorCopy = signalProcessor.copy();
			massComponent.signalProcessors.add(signalProcessorCopy);
			signalProcessorCopy.updateConnectionsAfterCopy(signalProcessor, connectionMap);
		}
		
		// copy memory
		for(Memory memory : getMemory()) {
			Memory copy = new Memory();
			massComponent.getMemory().add(copy);
			copy.updateConnectionsAfterCopy(memory, connectionMap);
			
		}
		
		return massComponent;
	}
	
	@Override
	public void getEvolutionOptions(List<EvolutionOption> options) {
		
		// change radius by +-10
		options.add(new EvolutionOption(1,"Mass - change radius") {
			
			@Override
			public void apply() {
				radius += RandomGenerator.nextLong(20) - 10;
				if(radius < 10) radius = 10;
			}
		});
		
		// add sub component
		options.add(new EvolutionOption(1,"Mass - add sub component") {
			
			@Override
			public void apply() {
				CreatureComponent subComponent = CreatureComponent.createRandomComponent(MassComponent.this);
				double angle = RandomGenerator.nextDouble();
				ComponentConnection connection = new ComponentConnection(angle, subComponent);
				getSubComponents().add(connection);
				
			}
		});
		
		if(subComponents.size() > 0) {
			// remove sub component
			options.add(new EvolutionOption(1,"Mass - remove sub component") {
				@Override
				public void apply() {
					if(subComponents.size() == 0) {
						this.description_appender = "nothing to remove";
						return;
					}
					
					ComponentConnection toRemove = getSubComponents().get(RandomGenerator.nextInt(getSubComponents().size()));
					getSubComponents().remove(toRemove);
					description_appender = toRemove.component.getClass().getName();
					description_appender = "removed: " + toRemove.getClass().getSimpleName();
				}
			});
		}

		// add connection
		// 	get a random free input
		//  get a random free output
		//	if source > target we cannot create a direction. add a memory node instead
		options.add(new EvolutionOption(3,"Mass - add connection") {
			
			@Override
			public void apply() {
				
				// get all inputs and outputs
				List<Input> inputs = getAllInputs();
				List<Output> outputs = getAllOutputs();
				
				// nothing to do if there are no inputs or outputs
				if(inputs.size() == 0 || outputs.size() == 0) {
					this.description_appender = "no inputs or outputs";
					return;
				}
				
				Input input = inputs.get(RandomGenerator.nextInt(inputs.size()));
				Output output = outputs.get(RandomGenerator.nextInt(outputs.size()));
				
				boolean dependent = false;
				if(output.getParent() instanceof HasInputConnections) {
					if(HasInputConnections.isDependentOn(input.getParent(),(HasInputConnections) output.getParent())) {
						dependent = true;
					}
				}
				
				if(!dependent) {
					Connection connection = new Connection(MassComponent.this);
					connection.setSender(output);
					connection.setReceiver(input);
					this.description_appender = "normalConnection " + output.getParent().getClass().getSimpleName() + "->" + input.getParent().getClass().getSimpleName(); 
 				} else {
					Memory memory = new Memory();
					MassComponent.this.getMemory().add(memory);
					
					Connection output2Memory = new Connection(MassComponent.this);
					output2Memory.setSender(output);
					output2Memory.setReceiver(memory.getInput());
					
					Connection memory2Input = new Connection(MassComponent.this);
					memory2Input.setSender(memory.getOutput());
					memory2Input.setReceiver(input);
					this.description_appender = "new memoryConnection " + output.getParent().getClass().getSimpleName() + "->" + input.getParent().getClass().getSimpleName();
				}
				
				
				
			}
		});

		if(connections.size() > 0) {
			// remove connection
			options.add(new EvolutionOption(3,"Mass - remove connection") {
				@Override
				public void apply() {
					if(connections.size() == 0) {
						this.description_appender = "no connections to delete";
						return;
					}
					Connection conn = connections.get(RandomGenerator.nextInt(connections.size()));
					this.description_appender = conn.getSender().getParent().getClass().getSimpleName() + "->" + conn.getReceiver().getParent().getClass().getSimpleName(); 
					conn.getSender().setConnection(null);
					conn.getReceiver().setConnection(null);
					connections.remove(conn); 
				}
			});
		}
		
		// change connection to processing node
		if(connections.size() > 0) {
			options.add(new EvolutionOption(1,"Mass - nodify connection") {
				@Override
				public void apply() {
					if(connections.size() == 0) {
						this.description_appender = "no connections existing";
						return;
					}
					Connection connection = connections.get(RandomGenerator.nextInt(connections.size()));
					connections.remove(connection);
					
					Output out = connection.getSender();
					Input in = connection.getReceiver();
					ProcessingNode node = new ProcessingNode(InFunction.getRandomInFunction(), OutFunction.getRandomOutFunction());
					signalProcessors.add(node);
					
					Connection outNodeConnection = new Connection(MassComponent.this);
					out.setConnection(outNodeConnection);
					node.createInput().setConnection(outNodeConnection);
					
					Connection nodeInConnection = new Connection(MassComponent.this);
					node.createOutput().setConnection(nodeInConnection);
					in.setConnection(nodeInConnection);
					
				}
			});
		}
		
		// change processing node function
		if(signalProcessors.size() > 0) {
			options.add(new EvolutionOption(1,"Mass - change processing node function") {
				@Override
				public void apply() {
					if(signalProcessors.size() == 0) return;
					ProcessingNode node = signalProcessors.get(RandomGenerator.nextInt(signalProcessors.size()));
					if(Math.random() > 0.5) {
						node.setInFunction(InFunction.getRandomInFunction());
					} else {
						node.setOutFunction(OutFunction.getRandomOutFunction());
					}
				}
			});
		}
		
		// mutate processing node function
		// change processing node function
		if(signalProcessors.size() > 0) {
			options.add(new EvolutionOption(1,"mutate processing node function") {
				@Override
				public void apply() {
					if(signalProcessors.size() == 0) return;
					ProcessingNode node = signalProcessors.get(RandomGenerator.nextInt(signalProcessors.size()));
					if(Math.random() > 0.5) {
						node.getInFunction().mutate();
					} else {
						node.getInFunction().mutate();
					}
				}
			});
		}
		// add a memory node
		options.add(new EvolutionOption(3,"add memory") {
			
			@Override
			public void apply() {
				Memory memory = new Memory();
				MassComponent.this.getMemory().add(memory);
			}
		});

	}
	
	public List<Output> getAllOutputs() {
		LinkedList<Output> outputs = new LinkedList<Output>();
		constantSignalProducer.getFreeOutputConnections(outputs);
		for (ComponentConnection connection : subComponents) {
			if(connection.component instanceof HasOutputConnections) {
				((HasOutputConnections) connection.component).getFreeOutputConnections(outputs);
			}
		}
		for (ProcessingNode sender : signalProcessors) {
			if(sender instanceof HasOutputConnections) {
				((HasOutputConnections) sender).getFreeOutputConnections(outputs);
			}
		}
		for(Memory memoryNode : this.memory) {
			memoryNode.getOutputWrapper().getFreeOutputConnections(outputs);
		}
		
		return outputs;
	}
	
	public List<Input> getAllInputs() {
		LinkedList<Input> inputs = new LinkedList<Input>();
		for (ComponentConnection connection : subComponents) {
			if(connection.component instanceof HasInputConnections) {
				((HasInputConnections) connection.component).getFreeInputConnections(inputs);
			}
		}
		for (ProcessingNode sender : signalProcessors) {
			if(sender instanceof HasInputConnections) {
				((HasInputConnections) sender).getFreeInputConnections(inputs);
			}
		}
		
		for(Memory memoryNode : this.memory) {
			memoryNode.getFreeInputConnections(inputs);
		}
		return inputs;
	}
	
	/**
	 * gets the point inthis component that corsseponds to th erespective angle with radius as radius
	 * 
	 * @param x
	 * @param y
	 * @param angle
	 * @param componentAngle
	 * @return
	 */
	private HasPosition getInnerPosition(double x, double y, double radius, double angle) {
		double childAngle = angle;
		double childAngleRad = Math.toRadians(childAngle * 360);
		double xOffset = Math.cos(childAngleRad) * radius;
		double childX = x + xOffset;
		
		double yOffset =  Math.sin(childAngleRad) * radius;
		double childY = y + yOffset;
		
		return new HasPosition(childX, childY,childAngle);
	}
	
	/**
	 * gets the point on the outer rim of this component that coressponds to the respective angle 
	 * @param x
	 * @param y
	 * @param angle
	 * @param componentAngle
	 * @return
	 */ 
	private HasPosition getChildPosition(double x, double y, double angle) {
		return getInnerPosition(x, y, this.radius, angle);
	}

	
	public static MassComponent createRandomMassComponent() {
		MassComponent component = new MassComponent(20);
		return component;
	}
	
	private void sortInputComponents() {
		
		// get all input connections
		List<HasInputConnections> inputConnections = new LinkedList<HasInputConnections>();
		for (ComponentConnection connection: this.getSubComponents()) {
			if(connection.component instanceof HasInputConnections) {
				inputConnections.add((HasInputConnections) connection.component);  
			}
		}
		for(ProcessingNode processingNode : this.getSignalProcessors()) {
			if(processingNode instanceof HasInputConnections) inputConnections.add((HasInputConnections) processingNode);
		}
		for(Memory memory: getMemory()) {
			inputConnections.add(memory);
		}
		
		System.out.println("input connections: " + inputConnections.size());
		
		// sort them
		sortedConnections = new LinkedList<HasInputConnections>();
		
		HasInputConnections current = null;
		boolean finished = inputConnections.size() == 0;
		while(!finished) {
			if(current == null) current = inputConnections.remove(0);
			
			List<HasInputConnections> dependencies = current.getPredecessors();
			for (HasInputConnections hasInputConnections : dependencies) {
				// if there is an unmet dependency start with the dependency and re-queue this node.
				if(!dependencies.contains(hasInputConnections)) {
					inputConnections.remove(hasInputConnections);
					inputConnections.add(0, current);
					current = hasInputConnections;
					break;
				}
			}
			
			sortedConnections.add(sortedConnections.size(), current);
			current = null;
			finished = inputConnections.size() == 0;
		}
		System.out.println("sorted connections: " + sortedConnections.size());
		
		
	}
	
	private void findSignalSenders() {
		for (ComponentConnection connection: this.getSubComponents()) {
			if(connection.component instanceof CanProvideOutputSignal) {
				signalSenders.add((CanProvideOutputSignal) connection.component);  
			}
		}
		for(ProcessingNode processingNode : this.getSignalProcessors()) {
			if(processingNode instanceof CanProvideOutputSignal) signalSenders.add((CanProvideOutputSignal) processingNode);
		}
		
		for(Memory memory : this.getMemory()) {
			signalSenders.add(memory.getOutputWrapper());
		}
	}
	
	/**
	 * a connection to another component has an angle and reference to that component
	 * @author frank
	 *
	 */
	private class ComponentConnection {
		/**
		 * the angle in radial 
		 * between 0 and 1
		 */
		double angle;
		
		/**
		 * reference to another creature component
		 */
		CreatureComponent component;

		/**
		 * constructor
		 * @param angle
		 * @param component
		 */
		public ComponentConnection(double angle, CreatureComponent component) {
			super();
			this.angle = angle;
			this.component = component;
		}
		
	}
	
}

