//Nano

package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
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
	Talon rightMotor1, rightMotor2, leftMotor1, leftMotor2, upMotor1, upMotor2;
	BuiltInAccelerometer acc;
	double inPower;
	private double limiter;
	private int timer;
	private boolean b = false;
	private double leftM, rightM;

	// gyro
	private Gyro gyro;
	private int gyroInput = 1;
	private double adjust;
	// Auto
	RobotDrive robot;
	private int execute;// Changing the execute command to an integer
						// will check what the value is instead of using
						// a boolean to allow for different commands to be
						// executed.
	//Compressor
	DoubleSolenoid sol1, sol2;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		sol1 = new DoubleSolenoid(1,2);

		driver = new Joystick(0); // passing in port of the joystick
		leftMotor1 = new Talon(0);
		leftMotor2 = new Talon(1);
		rightMotor1 = new Talon(2); // port is based on roboRio pwm ports
		rightMotor2 = new Talon(3); // port is based on roboRio pwm ports
		upMotor1 = new Talon(4);//Motors for the liftymagiger
		upMotor2 = new Talon(5);
		limiter = 1;
		acc = new BuiltInAccelerometer();
		timer = 0;
		leftM = 1;
		rightM = 1;

		gyro = new Gyro(gyroInput);
		adjust = 0;
		// Auto
		robot = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
		execute = 0;
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
	public void teleopPeriodic() {// Set the gyro

		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from

		leftMotor1.set(driver.getRawAxis(2) * t);
		leftMotor2.set(driver.getRawAxis(2) * t);
		rightMotor1.set(driver.getRawAxis(2) * t);
		rightMotor2.set(driver.getRawAxis(2) * t);

		if (driver.getRawButton(1)) {// set gyro
			adjust = gyro.getAngle();
		}
	}

	public void command() {
		if (execute == 0) {
			if (driver.getRawButton(1)) {
				execute = 1;
				timer = 0;
			}
		} else {
			timer++;
			switch (execute) {
			case 1://
				if (timer < 60)
					robot.drive(0.2, 0);
				if (timer > 60 && timer < 120)
					robot.drive(0.2, 1);
				if (timer > 120) {
					execute = 0;
				}
				break;
			case 2:
				if (timer < 60)
					robot.drive(0, 0.2);
				if (timer > 60 && timer < 120)
					robot.drive(1, 0.2);
				if (timer > 120) {
					execute = 0;
				}
				break;
			case 3:// TODO: spin left & right 1 revolution each.
				if (timer < 30) {//
				} else if (true) {// TODO: make the timer stuff work right
				} else if (timer > 120) {//TODO: Get timer sh*t workin
				}
				break;
			default:
				vitalMessage();
				break;
			}
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void vitalMessage() {
		System.out.println("Ryan is fat.");
	}

	public void testPeriodic() {
		System.out.println("Gyro ~ " + gyro.getAngle());
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

		double forward = driver.getRawAxis(1); // Front back
		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from
											// 0-1
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
				if (driver.getRawAxis(2) < -0.1 || driver.getRawAxis(2) > 0.1) {
					leftMotor1.set(driver.getRawAxis(2) * t);
					leftMotor2.set(driver.getRawAxis(2) * t);
					rightMotor1.set(driver.getRawAxis(2) * t);
					rightMotor2.set(driver.getRawAxis(2) * t);
				}
				/*
				 * if (driver.getRawAxis(2) < -0.1) {//This can probably be
				 * removed leftMotor1.set(-Math.abs(driver.getRawAxis(2))*t);
				 * leftMotor2.set(-Math.abs(driver.getRawAxis(2))*t);
				 * rightMotor1.set(-Math.abs(driver.getRawAxis(2))*t);
				 * rightMotor2.set(-Math.abs(driver.getRawAxis(2))*t); } else if
				 * (driver.getRawAxis(2) > 0.1) {// right only
				 * leftMotor1.set(Math.abs(driver.getRawAxis(2))*t);
				 * leftMotor2.set(Math.abs(driver.getRawAxis(2))*t);
				 * rightMotor1.set(Math.abs(driver.getRawAxis(2))*t);
				 * rightMotor2.set(Math.abs(driver.getRawAxis(2))*t); }
				 */
			}
		}
		if (timer == 60) {// data output
			System.out.println("forward ~ " + forward + "\tThrottle ~ " + t
					+ "\t Limiter ~ " + limiter);
		}
	}

	// Swerve Drive - first try? #itMightNotWork
	public void compressionControl(){
		if(driver.getRawButton(3)){
			sol1.set(DoubleSolenoid.Value.kForward);
			System.out.println("Solenoid State: Forward");
		}else if(driver.getRawButton(4)){
			System.out.println("Solenoid State: Reverse");
			sol1.set(DoubleSolenoid.Value.kReverse);
		}else{
			System.out.println("Solenoid State: Off");
			sol1.set(DoubleSolenoid.Value.kOff);
		}
	}
	/*public void motorControl(){//TODO: make it so the motors go up
		if(driver.getRawButton(12)){
			double inPower;
			upMotor1.set(inPower);
			upMotor2.set(inPower);
			System.out.println("");
		}
		
	}*/
	public double getX() { // xAxis = 0 yAxis = 1
		double theta1 = Math.atan(driver.getRawAxis(0) / driver.getRawAxis(1));
		double h = driver.getRawAxis(1) / Math.sin(theta1);
		double thetaX = (adjust - gyro.getAngle()) + theta1;
		return (h * Math.sin(thetaX));
		// return ((driver.getRawAxis(1) /
		// Math.sin(Math.atan(driver.getRawAxis(0)/driver.getRawAxis(1))))*Math.sin((adjust-gyro.getAngle())+(Math.atan(driver.getRawAxis(0)/driver.getRawAxis(1)))));
	}

	public double getY() {
		double theta2 = Math.atan(driver.getRawAxis(1) / driver.getRawAxis(0));
		double h = driver.getRawAxis(1) / Math.sin(theta2);
		double thetaY = theta2 - (adjust - gyro.getAngle());
		return (h * Math.sin(thetaY));
		// return ((driver.getRawAxis(1) /
		// Math.sin(Math.atan(driver.getRawAxis(1)/driver.getRawAxis(0)))*Math.sin((Math.atan(driver.getRawAxis(1)/driver.getRawAxis(0)))
		// - (adjust-gyro.getAngle()))));
	}

}
