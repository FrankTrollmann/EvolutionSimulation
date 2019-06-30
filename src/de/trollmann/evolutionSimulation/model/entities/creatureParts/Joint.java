package de.trollmann.evolutionSimulation.model.entities.creatureParts;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.trollmann.evolutionSimulation.model.entities.Creature;
import de.trollmann.evolutionSimulation.model.entities.creatureParts.evolution.EvolutionOption;
import de.trollmann.evolutionSimulation.model.net.Connection;
import de.trollmann.evolutionSimulation.model.net.HasInputConnections;
import de.trollmann.evolutionSimulation.model.net.Input;
import de.trollmann.evolutionSimulation.model.net.Output;
import de.trollmann.evolutionSimulation.model.physics.HasPosition;
import de.trollmann.evolutionSimulation.util.RandomGenerator;
import de.trollmann.evolutionSimulation.view.DrawableCanvas;

/**
 * a joint connecting to MassComponent objects
 * @author frank
 *
 */
public class Joint extends CreatureComponentWithConnections implements HasInputConnections{

	/**
	 * the target of the joint
	 */
	MassComponent target;
	
	/**
	 * weight of the target component
	 */
	long targetWeight;
	
	/**
	 * current angle of the joint. Should be between -1 and 1. 
	 */
	double currentAngle;
	
	/**
	 * the speed of the joint when moving from -1 to 1
	 * speed of 1 moves from -1 to 0.
	 * speed of 2 moves from -1 to 1
	 */
	double positiveSpeed;
	
	/**
	 * the speed of the joint when moving from 1 to -1
	 * speed of 1 moves from 1 to 0.
	 * speed of 2 moves from 1 to -1
	 */
	double negativeSpeed;
	
	/**
	 * defines whether the joint is currently moving from -1 to 1 (true) or 1 to -1 (fals2)
	 */
	boolean forwardDirection = true;
	
	/**
	 * the input that causes this joint to move
	 */
	Input movementInput;
	public Input getMovementInput() {
		return movementInput;
	}

	
	
	@Override
	public void draw(DrawableCanvas view, double x, double y, double angle) {
		view.drawCircle(x, y, this.radius, Color.RED);
		
		HasPosition targetPosition = getTargetPosition(x, y, angle, false);
		target.draw(view, targetPosition.getX(), targetPosition.getY(), targetPosition.getRotation());

	}
	
	@Override
	public void drawSchematic(DrawableCanvas view, double x, double y,
			double angle) {

		// draw this component
		view.drawCircle(x, y, this.radius, Color.RED);
		
		// draw target mass component
		HasPosition targetPosition = getTargetPosition(x, y, angle, true);
		target.drawSchematic(view, targetPosition.getX(), targetPosition.getY(), targetPosition.getRotation());
		
		// draw arcs indicating the movement forces.
		double innerRadius = radius * 0.9;
		int baseAngle = (int) Math.floor( 360 * angle);
		view.drawArc(x, y, innerRadius*2, innerRadius*2, baseAngle + 5, (int) Math.floor(180 * positiveSpeed), Color.cyan);
		view.drawArc(x, y, innerRadius*2, innerRadius*2, baseAngle - 5, (int) Math.floor(-180 * negativeSpeed), Color.cyan);
	}

	/**
	 * constructor
	 * @param target
	 * @param currentAngle
	 * @param positiveSpeed
	 * @param negativeSpeed
	 */
	public Joint(MassComponent target, double currentAngle,
			double positiveSpeed, double negativeSpeed) {
		super();
		this.radius = 10;
		this.target = target;
		this.movementInput = new Input(this);
		this.registerInputs(movementInput);
		this.currentAngle = currentAngle;
		this.positiveSpeed = positiveSpeed;
		this.negativeSpeed = negativeSpeed;
	}
	
	
	@Override
	public void actInternal() {
		// nothing to act. Will be done in process
	}
	
	@Override
	public long getWeight() {
		// always has radius 10. so always weight of 1
		return 1;
	}
	
	@Override
	public long getBiggestPartSize() {
		return Math.max(10, target.getBiggestPartSize());
	}
	
	@Override
	public void postInitialize(Creature creature) {
		this.creature = creature;
		if (target == null) throw new RuntimeException("joint with empty target in postInitialize");
		targetWeight = target.getWeight();
		target.postInitialize(creature);
	}
	
	@Override
	public CreatureComponent copy() {
		MassComponent targetCopy = (MassComponent) target.copy();
		
		Joint joint = new Joint(targetCopy, currentAngle, positiveSpeed, negativeSpeed);
		return joint;
	}
	
	@Override
	public void getEvolutionOptions(List<EvolutionOption> options) {
		// change positive joint speed
		options.add(new EvolutionOption(1, "Joint - change positive speed") {
			
			@Override
			public void apply() {
				positiveSpeed = RandomGenerator.nextDouble();
			}
		});
		
		// change negative joint speed
		options.add(new EvolutionOption(1, "Joing - change negative speed") {
			
			@Override
			public void apply() {
				negativeSpeed = RandomGenerator.nextDouble();
			}
		});
		
	}

	@Override
	protected void propagateVisitorToChildren(CreatureComponentVisitor visitor) {
		target.visitSubtree(visitor);
		
	}
	
	/**
	 * get target position of the end of the joint
	 * current angle is not ignored
	 * @param x
	 * @param y
	 * @param angle
	 * @return
	 */
	private HasPosition getTargetPosition(double x, double y, double angle) {
		return getTargetPosition(x, y, angle, false);
	}
	/**
	 * get target position of the end of the joint
	 * @param x
	 * @param y
	 * @param angle
	 * @param ignoreJointAngle if this is set to true the current angle is ignored
	 * @return
	 */
	private HasPosition getTargetPosition(double x, double y, double angle, boolean ignoreJointAngle) {
		double childAngle = 0;
		if(ignoreJointAngle) {
			childAngle = angle;
		} else {
			childAngle = angle + currentAngle/4;
		}

		double childAngleRad = Math.toRadians(childAngle * 360);
		double xOffset = Math.cos(childAngleRad) * target.getRadius();
		double childX = x + xOffset;
		
		double yOffset = Math.sin(childAngleRad) * target.getRadius();
		double childY = y + yOffset;
		
		return new HasPosition(childX, childY, childAngle);
	}
	
	
	public static Joint createRandomJoint(MassComponent parent) {
		MassComponent target = MassComponent.createRandomMassComponent();
		double angle = RandomGenerator.nextDouble() * 2 - 1;
		double positiveSpeed = RandomGenerator.nextDouble();
		double negativeSpeed = RandomGenerator.nextDouble();
		Joint joint = new Joint(target, angle, positiveSpeed, negativeSpeed);
		
		List<Output> outs = parent.getAllOutputs();
		if(outs.size() > 0) {
			Output out = outs.get(RandomGenerator.nextInt(outs.size()));
			Connection connection = new Connection(parent);
			connection.setSender(out);
			connection.setReceiver(joint.movementInput);
		}
		
		
		return joint;
	}
	
	@Override
	public void updateConnectionsAfterCopy(
			CreatureComponentWithConnections originalComponent,
			Map<Connection, Connection> copyMap) {
		Joint copiedJoint = (Joint) originalComponent;
		
		Connection originalConnection = copiedJoint.getMovementInput().getConnection(); 
		
		if(originalConnection != null) {
			Connection copiedConnection = copyMap.get(originalConnection);
			if(copiedConnection != null) {
				copiedConnection.setReceiver(this.movementInput);
			} else {
				throw new RuntimeException("connection exists but was not copied!");
			}
		}
		
	}
	
	@Override
	public boolean isReady() {
		return movementInput.hasSignal();
	}
	
	@Override
	public void process() {

		double signalAmplifier = movementInput.getSignal();
		if(signalAmplifier == 0) return;

		
//		HasPosition positionBefore = getTargetPosition(x, y, angle);
		// save current value of forwardDirection for later
		boolean movedForward = forwardDirection;
		
		// move the joint
		if(forwardDirection) {
			currentAngle += positiveSpeed * signalAmplifier;
			if(currentAngle >= 1) {
				forwardDirection = false;
				currentAngle = 1;
			}
		} else {
			currentAngle -= negativeSpeed * signalAmplifier;
			if (currentAngle <= -1) {
				forwardDirection = true;
				currentAngle = -1;
			}
		}
		
		HasPosition positionAfter = getTargetPosition(x, y, getRotation());
		// update speed (TODO: more sophisticated)
		
		// forward drift is square of angle movement 
		// between 0 and 1 and stronger for the higher values
		
		// apply drift force in the opposite direction
		double driftForward = 1;
		if (movedForward) {
			driftForward *= positiveSpeed; 
		} else {
			driftForward *= negativeSpeed ; 
		}
		
		creature.applyDriftForce(targetWeight, driftForward * driftForward, getRotation() + 0.5);
		
		// apply rotation force
		creature.applyRotationForce(targetWeight, driftForward * driftForward, movedForward);
		
		
//		creature.setXSpeed( creature.getXSpeed() - positionAfter.getX() + positionBefore.getX());
//		creature.setYSpeed( creature.getYSpeed() - positionAfter.getY() + positionBefore.getY());
		
		// propagate to child
		target.act(positionAfter.getX(), positionAfter.getY(), positionAfter.getRotation());
	}
	
	@Override
	public List<HasInputConnections> getPredecessors() {
		List<HasInputConnections> ret = new LinkedList<HasInputConnections>();
	
		movementInput.addPredecessor(ret);
				return ret;
	}
	
	@Override
	public void getFreeInputConnections(List<Input> toFill) {
		if(movementInput.getConnection() == null) toFill.add(movementInput);
		
	}

}
