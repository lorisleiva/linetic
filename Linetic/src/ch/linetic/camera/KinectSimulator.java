package ch.linetic.camera;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import ch.linetic.gesture.Joint;
import ch.linetic.gesture.Pose;
import ch.linetic.gesture.PoseInterface;
import ch.linetic.gesture.PoseInterface.JointType;
import ch.linetic.user.UserInterface;

/**
 * Implementation of the CameraInterface by simulating a kinect
 * @author ketsio
 *
 */
public class KinectSimulator implements CameraInterface {

	private static final Random rand = new Random();
	private PGraphics pg;
	
	private PoseInterface simulatedPose;
	private final static int range = 10;
	
	public KinectSimulator(PApplet parrent) {
		this.pg = parrent.createGraphics(640, 480);
		pg.beginDraw();
		pg.background(0);
		pg.textAlign(PApplet.CENTER, PApplet.CENTER);
		pg.color(255);
		pg.text("Kinect Simulator", pg.width / 2, pg.height / 2);
		pg.endDraw();
		
		this.simulatedPose = randomPose();
	}

	@Override
	public void update() {
		updatePose();
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public PoseInterface capture(UserInterface user) {
		return simulatedPose;
	}

	@Override
	public PImage getImage() {
		return pg.get();
	}

	
	// RANDOM
	
	
	private PoseInterface updatePose() {
		PoseInterface pose = new Pose();
		for (JointType jointType : JointType.values()) {
			updateJoint(jointType);
		}
		return pose;
	}
	
	private void updateJoint(JointType jointType) {
		Joint nextJoint = simulatedPose.getJoint(jointType).add(new Joint(
			rand.nextInt(range+1)-(range/2),
			rand.nextInt(range+1)-(range/2),
			rand.nextInt(range+1)-(range/2)));
		simulatedPose.setJoint(jointType, nextJoint);
	}
	
	private PoseInterface randomPose() {
		PoseInterface pose = new Pose();
		for (JointType jointType : JointType.values()) {
			pose.setJoint(jointType, new Joint(
				rand.nextInt(range),
				rand.nextInt(range),
				rand.nextInt(range)));
		}
		return pose;
	}

}
