package de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution;

/**
 * this class is the superclass for one option of evolution
 * 
 * @author frank
 *
 */
public abstract class EvolutionOption {

	/**
	 * the weight of this option
	 */
	int weight;
		public int getWeight() {
			return weight;
		}
	
		
	/**
	 * description of the mutation
	 */
	String description;
	protected String description_appender = null;
		public String getDescription() {
			if(description_appender != null) {
				return description + " : " + description_appender;
			}
			return description;
		}
		
	/**
	 * constructor	
	 * @param weight
	 */
	public EvolutionOption(int weight, String description) {
			super();
			this.description = description;
			this.weight = weight;
	}

	public abstract void apply();
}
