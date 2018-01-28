package org.usfirst.frc.team3997.robot.controllers;

import org.usfirst.frc.team3997.robot.Params;
import org.usfirst.frc.team3997.robot.hardware.RemoteControl;
import org.usfirst.frc.team3997.robot.hardware.RobotModel;
import org.usfirst.frc.team3997.robot.pid.ArcadeStraightPIDOutput;
import org.usfirst.frc.team3997.robot.pid.DriveEncodersPIDSource;
import org.usfirst.frc.team3997.robot.pid.WheelsPIDOutput;

import org.usfirst.frc.team3997.robot.hardware.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveController {

	private RobotModel robot;

	private DifferentialDrive drive;
	private RemoteControl humanControl;
	private DriveState m_stateVal;
	private DriveState nextState;

	public PIDOutput leftPIDOutput;
	public PIDController leftPID;

	public PIDOutput rightPIDOutput;
	public PIDController rightPID;

	// TODO public PIDOutput driveXPIDOutput;
	// TODO public PIDController visionPID;
	// TODO public VisionPIDSource visionPIDSource

	public PIDOutput straightPIDOutput;
	public PIDController straightPID;

	public PIDSource avgEncodersPIDSource;
	// What types of drive states there are.
	enum DriveState {
		kInitialize, kTeleopDrive
	};
	// function gets called from Robot.java peter is a dummy butt
	public DriveController(RobotModel robot, RemoteControl humanControl) {
		this.robot = robot;
		this.humanControl = humanControl;
		
		
		// Does math for Arcade Drive
		drive = new DifferentialDrive(this.robot.leftDriveMotors, this.robot.rightDriveMotors);
		drive.setSafetyEnabled(false);
		
		//Sets PIDSorceTypes and SamplesToAverage for Right and left Encoders and gets the perameters.
		
		this.robot.leftDriveEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		this.robot.leftDriveEncoder.setSamplesToAverage(Params.DRIVE_Y_PID_SAMPLES_AVERAGE);

		this.robot.rightDriveEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		this.robot.rightDriveEncoder.setSamplesToAverage(Params.DRIVE_Y_PID_SAMPLES_AVERAGE);
		// TODO I think that this is wrong.
		// Does the output for left, right, and straight PIDs
		leftPIDOutput = new WheelsPIDOutput(RobotModel.Wheels.LeftWheels, this.robot);

		leftPID = new PIDController(0, 0, 0, this.robot.leftDriveEncoder, leftPIDOutput);
		// TODO Might change this to max power variable
		leftPID.setOutputRange(-1.0, 1.0);
		leftPID.setAbsoluteTolerance(0.25);
		leftPID.disable();

		// TODO I think that this is wrong.
		rightPIDOutput = new WheelsPIDOutput(RobotModel.Wheels.RightWheels, this.robot);

		rightPID = new PIDController(0, 0, 0, robot.rightDriveEncoder, rightPIDOutput);
		// TODO Might change this to max power variable
		rightPID.setOutputRange(-1.0, 1.0);
		rightPID.setAbsoluteTolerance(0.25);
		rightPID.disable();

		avgEncodersPIDSource = new DriveEncodersPIDSource(this.robot);

		straightPIDOutput = new ArcadeStraightPIDOutput(drive, this.robot);
		straightPID = new PIDController(0, 0, 0, avgEncodersPIDSource, straightPIDOutput);
		// TODO might change this to max power variable.
		straightPID.setOutputRange(-1.0, 1.0);
		straightPID.setAbsoluteTolerance(1);
		straightPID.disable();
		// TODO ???? enum kInitialize
		m_stateVal = DriveState.kInitialize;
		nextState = DriveState.kInitialize;

	}
//PID Cases
	public void update(double currTimeSec, double deltaTimeSec) {
		switch (m_stateVal) {
		//case one disables Left and Right PID
		case kInitialize:
			leftPID.disable();
			rightPID.disable();
			nextState = DriveState.kTeleopDrive;
			break;
			//Runs Teleop
		case kTeleopDrive:
			// Declares Driver and humanControl values
			double driverLeftX = humanControl.getJoystickValue(RemoteControl.Joysticks.kDriverJoy,
					RemoteControl.Axes.kLX);
			double driverLeftY = humanControl.getJoystickValue(RemoteControl.Joysticks.kDriverJoy,
					RemoteControl.Axes.kLY);
			double driverRightX = humanControl.getJoystickValue(RemoteControl.Joysticks.kDriverJoy,
					RemoteControl.Axes.kRX);
			double driverRightY = humanControl.getJoystickValue(RemoteControl.Joysticks.kDriverJoy,
					RemoteControl.Axes.kRY);
			// If Left PID is enabled or the Right PID is enabkled, Disable them.
			if (leftPID.isEnabled() || rightPID.isEnabled()) {
				leftPID.disable();
				rightPID.disable();
			}
			// If Arcade drive, ARCADE DRIVE. If not then Tank Drive
			if (Params.USE_ARCADE_DRIVE) {
				arcadeDrive(driverLeftY, driverRightX, true);
			} else {
				tankDrive(driverLeftY, driverRightY);
			}

			nextState = DriveState.kTeleopDrive;
			break;
		}
		m_stateVal = nextState;
	}

	public void arcadeDrive(double myY, double myX, boolean teleOp) {
		if (teleOp) {
			// Brake system slows down robot.
			if ((humanControl.getSlowDriveTier1Desired() && !humanControl.getSlowDriveTier2Desired())
					|| (!humanControl.getSlowDriveTier1Desired() && humanControl.getSlowDriveTier2Desired())) {
				Params.GLOBAL_Y_DRIVE_SPEED_MULTIPLIER = 0.65;
				Params.GLOBAL_X_DRIVE_SPEED_MULTIPLIER = 0.65;
				Params.SQUARE_DRIVE_AXIS_INPUT = false;
			} else {
				// Normal speed of robot
				Params.GLOBAL_Y_DRIVE_SPEED_MULTIPLIER = 1.0;
				Params.GLOBAL_X_DRIVE_SPEED_MULTIPLIER = 1.0;
				Params.SQUARE_DRIVE_AXIS_INPUT = true;
			}
			// Does Arcadedrive
			drive.arcadeDrive(myY * Params.GLOBAL_Y_DRIVE_SPEED_MULTIPLIER * Params.HARDSET_DRIVE_SPEED_MAX,
					myX * Params.GLOBAL_X_DRIVE_SPEED_MULTIPLIER * Params.HARDSET_DRIVE_SPEED_MAX,
					Params.SQUARE_DRIVE_AXIS_INPUT);
		} else {
			drive.arcadeDrive(myY, myX, false);
		}
	}

	// Does TankDrive
	public void tankDrive(double myLeft, double myRight) {
		drive.tankDrive(myLeft * Params.GLOBAL_Y_DRIVE_SPEED_MULTIPLIER,
				myRight * Params.GLOBAL_Y_DRIVE_SPEED_MULTIPLIER, Params.SQUARE_DRIVE_AXIS_INPUT);
	}

	// Resets Switch
	public void reset() {
		m_stateVal = DriveState.kInitialize;
	}
	// STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP  
	public void stop() {
		drive.arcadeDrive(0, 0, false);
	}
}
