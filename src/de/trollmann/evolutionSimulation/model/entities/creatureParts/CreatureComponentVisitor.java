package de.trollmann.evolutionSimulation.model.entities.creatureParts;

/**
 * visitor interface for traversing a creature
 * @author frank
 *
 */
public interface CreatureComponentVisitor {

	/**
	 * visit one creature
	 * @param component
	 */
	public void visit(CreatureComponent component);
}
