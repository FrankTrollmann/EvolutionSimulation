package de.trollmann.evolutionSimulation.model.net.functions;

/**
 * superclass for in and out functions
 * @author frank
 *
 */
public abstract class Function {

	protected Arity arity; 
	public Arity getArity() {
		return arity;
	}
	
	public abstract void mutate();
	
	public enum Arity  {
		SINGLE,
		MULTIPLE
	}
}
