package de.trollmann.evolutionSimulation.controller;

import de.trollmann.evolutionSimulation.model.WorldModel;
import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.Eye;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.Joint;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.MassComponent;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.Mouth;
import de.trollmann.evolutionSimulation.model.entities.mutationHistory.MutationHistoryNode;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.Memory;
import de.trollmann.evolutionSimulation.model.net.ProcessingNode;
import de.trollmann.evolutionSimulation.model.net.functions.in.IdenticalInFunction;
import de.trollmann.evolutionSimulation.model.net.functions.out.IdenticalOutFunction;
import de.trollmann.evolutionSimulation.util.Configuration;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.SimulationView;

/**
 * controller for the overall simulation
 * Responsible for ticking and updateing the view
 * @author frank
 *
 */
public class SimulationController {

	/**
	 * the current time (# ticks)
	 */
	long time = 0;
	
	
	/**
	 * duration of one tick
	 */
	long tickDurationInMs = 50;
	
	/**
	 * time point of last tick ( in ms)
	 */
	long lastTickMoment = -1;
	
	SimulationView view;
	
	/**
	 * root model of the world entities,
	 */
	WorldModel worldModel = null;
	
	
	/**
	 * constructor
	 * @param view
	 */
	public SimulationController(SimulationView view) {
		this.view = view;
		worldModel = new WorldModel();
		

		
		view.display(Configuration.maxX,Configuration.maxY);
	}
	
	/**
	 * kicks off the simulation
	 */
	public void start() {
		// setup test creature
		
		// main component
		MassComponent head = new MassComponent();
		
		// creature
		Creature creature = new Creature(worldModel, head, 500, 500);
		creature.setRotation(0.75);

		// mouth
		Mouth mouth = new Mouth();
		head.addComponent(0.01, mouth);
		
		// eye
		Eye eye = new Eye();
		head.addComponent(0.99, eye);
		// eye <-> memory		
		Memory eyeProcessorConnectionMemory = new Memory();
		head.getMemory().add(eyeProcessorConnectionMemory);
		Connection eyeMemoryConnection = new Connection(head,eye.getDistanceOutput(),eyeProcessorConnectionMemory.getInput());
		
		
		// memory <-> processor
		ProcessingNode tailProcessor = new ProcessingNode(new IdenticalInFunction(), new IdenticalOutFunction());
		head.getSignalProcessors().add(tailProcessor);
		Connection memoryProcessorConnection = new Connection(head, eyeProcessorConnectionMemory.getOutput(),tailProcessor.createInput());
		
		// processor <-> tail
		MassComponent tail = new MassComponent();
		Joint joint = new Joint(tail, 0, 0.4,0.4);
		head.addComponent(0.5, joint);
		Connection processorTailConnection = new Connection(head,tailProcessor.createOutput(), joint.getMovementInput());
		
		
		// self-moving fin
		MassComponent fin = new MassComponent();
		Joint finJoint = new Joint(fin, 0, 0.1, 0.3);
		head.addComponent(0.25, finJoint);
		// constant output <-> finProcessor
		ProcessingNode finProcessor = new ProcessingNode(new IdenticalInFunction(), new IdenticalOutFunction());
		head.getSignalProcessors().add(finProcessor);
		Connection mass2FinConnection = new Connection(head, head.getConstantSignalProducer().getNewOutput(),finProcessor.createInput());
		// finProcessor2Fin
		Connection finConnection = new Connection(head, finProcessor.createOutput(), finJoint.getMovementInput());
		
//		// fin mout. Just for test.
//		Mouth finMouth = new Mouth();
//		fin.addComponent(0, finMouth);
		
		MutationHistoryNode mutationHistoryRoot = new MutationHistoryNode(creature.copy());
		creature.setMutationHistoryNode(mutationHistoryRoot);
		

		
		creature.postInitialize();
		System.out.println("required energy: " + head.getEnergyRequirement());
		worldModel.getCreatures().add(creature);
		worldModel.setEvolutionHistory(mutationHistoryRoot);
		worldModel.setEvolutionUpdateListener(new WorldModel.EvolutionUpdateListener() {
			@Override
			public void onEvolutionUpdate() {
				view.onEvolutionHistoryChange();
			}
		});
		
		// initi evolution history after at least one creature has been added.
		view.initializeHistoryView(mutationHistoryRoot);
		
		for(int i = 0; i < 500; i++) {
			worldModel.addRandomFoodParticle();
		}
		
		
		// main simulation loop
		while(true) {
			
			if(lastTickMoment > 0 ) {
				while(lastTickMoment + tickDurationInMs > System.currentTimeMillis()) {
					//System.out.println("wait");
					// waste time to finish the tick
				}
			}
			lastTickMoment = System.currentTimeMillis();
			tick();
			
		}
	}
	
	/**
	 * simulates one tick
	 * This includes action, movement and updating view
	 */
	public void tick() {
		
		System.out.println("tick " + worldModel.getCreatures().size());
		
		// feed
		if(RandomGenerator.nextDouble() < Configuration.feedingThreshold) {
			worldModel.addRandomFoodParticle();
		};
		
		
		
		// act
		worldModel.act();
		
		// move
		worldModel.move();
		
		// draw
		view.getMainDrawableCanvas().clear();
		worldModel.draw(view.getMainDrawableCanvas());
		view.getMainDrawableCanvas().updateView();
		
		time++;
	}
}
