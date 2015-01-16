package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Joystick driver;
	Talon rightMotor1, rightMotor2, leftMotor1, leftMotor2;
	BuiltInAccelerometer acc;

	private double limiter;
	private int timer;
	private boolean b = false;
	private double leftM, rightM;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		driver = new Joystick(0); // passing in port of the joystick
		leftMotor1 = new Talon(0);
		leftMotor2 = new Talon(1);
		rightMotor1 = new Talon(2); // port is based on roboRio pwm ports
		rightMotor2 = new Talon(3); // port is based on roboRio pwm ports
		limiter = 1;
		acc = new BuiltInAccelerometer();
		timer = 0;
		leftM = 1;
		rightM = 1;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called periodically during operator control Simple tank
	 * drive
	 */
	public void teleopPeriodic() {
		// access brake button state
		// button values can be inferred from driver station
		boolean button = driver.getRawButton(1);
		// check if we should stop all motors
		if (button) {
			// set speeds to zero
			leftMotor1.set(0);
			leftMotor2.set(0);
			rightMotor1.set(0);
			rightMotor2.set(0);
			System.out.println("Brake");
		} else {
			// get value of left and right joysticks
			double leftYAxis = driver.getRawAxis(1); // access corresponds to
														// joystick (can find
														// this on drivers
														// station program)
			double rightYAxis = driver.getRawAxis(5); // right stick on xbox
														// controller
			// invert the values
			leftYAxis = leftYAxis * -1;
			rightYAxis = rightYAxis;
			// set motors to the values obtained from the joystick
			leftMotor1.set(leftYAxis);
			leftMotor2.set(leftYAxis);
			rightMotor1.set(rightYAxis);
			rightMotor2.set(rightYAxis);
			System.out.println(leftYAxis + "," + rightYAxis);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		timer++;
		if (timer > 60) {
			timer = 0;
		}
		
		double xAxis = driver.getRawAxis(0);
		if (xAxis < -0.1) {// left
			leftM = 1 - Math.abs(xAxis);// slow down the left tracks
			rightM = 1;
		} else if (xAxis > 0.1) {// right
			rightM = 1 - Math.abs(xAxis);// slow down the right side
			leftM = 1;
		} else {
			leftM = 1;
			rightM = 1;
		}
		
		double forward = driver.getRawAxis(1); //Front back
		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from 0-1
		if (driver.getRawButton(1)) {// brake (trigger button)
			// set speeds to zero
			leftMotor1.set(0);
			leftMotor2.set(0);
			rightMotor1.set(0);
			rightMotor2.set(0);
			System.out.println("Brake");
		} else {
			leftMotor1.set(-forward * t * leftM); // move the left wheels
			leftMotor2.set(-forward * t * leftM);
			rightMotor1.set((forward) * t * rightM); // move the right wheels
			rightMotor2.set((forward) * t * rightM);
			if (forward > -0.1 && forward < 0.1) {
				if(driver.getRawAxis(2) < -0.1 || driver.getRawAxis(2) > 0.1){
					leftMotor1.set(driver.getRawAxis(2)*t);
					leftMotor2.set(driver.getRawAxis(2)*t);
					rightMotor1.set(driver.getRawAxis(2)*t);
					rightMotor2.set(driver.getRawAxis(2)*t);
				}
				if (driver.getRawAxis(2) < -0.1) {//This can probably be removed
					leftMotor1.set(-Math.abs(driver.getRawAxis(2))*t);
					leftMotor2.set(-Math.abs(driver.getRawAxis(2))*t);
					rightMotor1.set(-Math.abs(driver.getRawAxis(2))*t);
					rightMotor2.set(-Math.abs(driver.getRawAxis(2))*t);
				} else if (driver.getRawAxis(2) > 0.1) {// right only
					leftMotor1.set(Math.abs(driver.getRawAxis(2))*t);
					leftMotor2.set(Math.abs(driver.getRawAxis(2))*t);
					rightMotor1.set(Math.abs(driver.getRawAxis(2))*t);
					rightMotor2.set(Math.abs(driver.getRawAxis(2))*t);
				}
			}
		}
		if (timer == 60) {// data output
			System.out.println("forward ~ " + forward + "\tThrottle ~ " + t
					+ "\t Limiter ~ " + limiter);
		}
	}

}
