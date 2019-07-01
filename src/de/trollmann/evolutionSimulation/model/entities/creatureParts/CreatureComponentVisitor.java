package de.trollmann.evolutionSimulation.model.entities.creatureParts;

/**
 * visitor interface for traversing a creature
 * @author frank
 *
 */
public abstract class CreatureComponentVisitor {
	
	/**
	 * visit one creature
	 * @param component
	 */
	public abstract void visit(CreatureComponent component);
}
