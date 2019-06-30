package de.trollmann.evolutionSimulation.model.entities.mutationHistory;

import java.util.LinkedList;
import java.util.List;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;

/**
 * this is one node in the history of mutation variants
 * @author frank
 *
 */
public class MutationHistoryNode {
	/**
	 * blueprint creature for this node
	 */
	Creature blueprint;
		public Creature getBlueprint() {
			return blueprint;
		}
	
	/**
	 * the mutation relative to the parent
	 */
	EvolutionOption mutation;
		public EvolutionOption getMutation() {
			return mutation;
		}
	
	/**
	 * the parent
	 */
	MutationHistoryNode parent;
	public MutationHistoryNode getParent() {
		return parent;
	}
	
	/**
	 * link to all children
	 */
	List<MutationHistoryNode> children = new LinkedList<MutationHistoryNode>();
		public List<MutationHistoryNode> getChildren() {
			return children;
		}
	/**
	 * constructor without a parent
	 * @param blueprint
	 */
	public MutationHistoryNode(Creature blueprint) {
		this.blueprint = blueprint;
	}
	
	/**
	 * counter for livingCreature
	 */
	int nrLivingCreatures = 0;
		public int getNrLivingCreatures() {
			return nrLivingCreatures;
		}
		public void onCreatureBirth() {
			nrLivingCreatures ++;
		}
		public void onCreatureDeath() {
			nrLivingCreatures--;
		}
	
	/**
	 * constructor with parent.
	 * adds this node into the child list of the parent automatically
	 * @param blueprint
	 * @param parent
	 * @param mutation
	 */
	public MutationHistoryNode(Creature blueprint, MutationHistoryNode parent, EvolutionOption mutation) {
		this.blueprint = blueprint;
		this.parent = parent;
		this.mutation = mutation;
		parent.getChildren().add(this);
	}
}
