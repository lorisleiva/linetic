package ch.linetic.analysis.analyzers;

import java.util.ArrayList;
import java.util.Collection;

import ch.linetic.analysis.Analyzer;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.PoseInterface.JointType;

/**
 * Analyze the shakiness of the hands
 * <code>MIN_VALUE</code> and <code>MAX_VALUE</code> can be adjust for different sensitivity
 * @author ketsio
 *
 */
public class HandShakinessAnalyzer extends Analyzer {

	public final static float MIN_VALUE = 0;
	public final static float MAX_VALUE = 2;

	public HandShakinessAnalyzer(int index) {
		super(index, MIN_VALUE, MAX_VALUE);
	}

	@Override
	protected float compute(MovementInterface movement) {
		if (movement.size() < 2) {
			return 0;
		}
		
		float accumulator = 0;
		Collection<Collection<Joint>> jointMovements = new ArrayList<>(2);
		jointMovements.add(movement.getJointMovement(JointType.HAND_LEFT));
		jointMovements.add(movement.getJointMovement(JointType.HAND_RIGHT));
		
		for (Collection<Joint> joints : jointMovements) {
			accumulator += operationPerFrameAVG(joints);
		}
		accumulator /= JointType.values().length;
		return accumulator;
	}
	
	@Override
	protected double adjustMinValue() {
		double newMinValue;
		// The minimum is calculated with the mean
		newMinValue = stats.getMean();
		newMinValue = Math.max(newMinValue, minBoundary);
		newMinValue -= Math.abs(newMinValue - minBoundary) / 2.0;
		return newMinValue;
	}
	
	@Override
	protected float operationPerFrame(Joint a, Joint b) {
		return a.angle(b);
	}

	@Override
	public String name() {
		return "Hand Shakiness";
	}

	@Override
	public String getSlug() {
		return "hand_shakiness";
	}

	@Override
	public boolean doTrigger(float finalValue) {
		return finalValue >= 0;
	}
}
