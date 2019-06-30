package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.physics.HasRadius;
import de.trollmann.evolutionSimulation.model.physics.HasWeight;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * parent view for all creature components
 * @author frank
 *
 */
public abstract class CreatureComponent extends HasRadius implements HasWeight {

	/**
	 * backreference to the creature
	 */
	Creature creature;
	
	/**
	 * radius of the component
	 */
	long radius;
	public long getRadius() {
		return radius;
	}
	
	public CreatureComponent() {
		super(0,0,0);
	}
	
	
	/**
	 * the draw function. Draws centered around x and y
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param angle
	 * @param drawMovement should movement be drawn?
	 */
	public abstract void draw(DrawableCanvas view, double x, double y, double angle);
	
	public abstract void drawSchematic(DrawableCanvas view, double x, double y, double angle);
	
	/**
	 * the act function sets the new position and angle
	 * @param x
	 * @param y
	 * @param angle
	 */
	public final void act( double x, double y, double angle) {
		this.setX(x);
		this.setY(y);
		this.setRotation(angle);
		actInternal();
	}
	
	/**
	 * internal act function. can assume the new position and angle are set
	 */
	protected abstract void actInternal();
	
	/**
	 * method to be called after creature initialization has finished
	 * In here we assume that the creature composition is final
	 */
	public abstract void postInitialize(Creature creature);
	
	/**
	 * get the size of the biggest part.
	 * @return
	 */
	public abstract long getBiggestPartSize();
	
	/**
	 * get the size of the biggest part
	 * @param connectionMap the map of old2new for connections
	 * @return
	 */
	public abstract CreatureComponent copy();
	
	/**
	 * calculates the evolution options for this creature
	 * The options are supposed to be added to the list options, not returned
	 * @param options list of options to add the results to.
	 */
	public abstract void getEvolutionOptions(List<EvolutionOption> options);
	
	/**
	 * visits this component and all of its children.
	 * @param visitor
	 */
	public final void visitSubtree(CreatureComponentVisitor visitor) {
		visitor.visit(this);
		propagateVisitorToChildren(visitor);
	}
	
	/**
	 * traverses the creature and calls a visitor for each component
	 * @param visitor
	 */
	protected abstract void propagateVisitorToChildren(CreatureComponentVisitor visitor);
	
	
	
	public static CreatureComponent createRandomComponent(MassComponent parent) {
		int type = (int) RandomGenerator.nextLong(4);
		switch(type) {
			case 0: return Mouth.createRandomMouth(parent);
			case 1: return MassComponent.createRandomMassComponent();
			case 2: return Joint.createRandomJoint(parent);
			case 3: return Eye.createRandomEye();
		}
		throw new RuntimeException("type " + type + "was not hanlded in switch case");
	}
	
	
}
