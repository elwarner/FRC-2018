package org.usfirst.frc.team3997.robot.auto.actions;

import org.usfirst.frc.team3997.robot.MasterController;
import org.usfirst.frc.team3997.robot.controllers.DriveController;
import org.usfirst.frc.team3997.robot.hardware.RobotModel;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeStraightAction extends Action{
	private DriveController driveTrain;
	private RobotModel robot;
	
	private double distance;
	private double timeout;
	private double maxSpeed;
	private double P, I, D;
	private double leftEncoderStartDistance, rightEncoderStartDistance;
	private double afterSetpointTime, timeAfterHit;
	private boolean reachedSetpoint;
	private int target_pass;
	
    public ArcadeStraightAction (MasterController controllers, double distance, double maxSpeed, double timeout, double timeAfterHit) {
		this.driveTrain = controllers.getDriveController();
		this.distance = distance;
		this.timeout = timeout;
		this.robot = controllers.getRobotModel();
		this.maxSpeed = maxSpeed;
		this.timeAfterHit = timeAfterHit;
		start_time = 0;
		reachedSetpoint = false;
		leftEncoderStartDistance = 0.0; 
		rightEncoderStartDistance = 0.0;
		
		P = 0.3;
		//TODO Might Need to change pID values
		I = 0.0;
		D = 0.0;
		
		SmartDashboard.putNumber("DRIVE_PID_P", P);
		SmartDashboard.putNumber("DRIVE_PID_I", I);
		SmartDashboard.putNumber("DRIVE_PID_D", D);

	}
	@Override
	public boolean isFinished() {
		
			return (Timer.getFPGATimestamp() >= start_time + timeout);
	}

	@Override
	public void update() {
		robot.updateGyro();
	}
	//Disables PID
	@Override
	public void finish() {
		driveTrain.straightPID.disable();
	}
	//
	@Override
	public void start() {
		// Start timer
		start_time = Timer.getFPGATimestamp();
		
		// Set PID SourceType for the left and right drive encoder
		
		robot.leftDriveEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		robot.rightDriveEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		
		//resets the right and left drive encoders
		
		robot.leftDriveEncoder.reset();
		robot.rightDriveEncoder.reset();
		
		// Gets distance for Left and Right Drive encoders
		
		leftEncoderStartDistance = robot.leftDriveEncoder.getDistance();
		leftEncoderStartDistance = robot.rightDriveEncoder.getDistance();
		
		// Sets OutputRange, PID, and Setpoints.
		
		driveTrain.straightPID.setOutputRange(-maxSpeed, maxSpeed);
		driveTrain.straightPID.setPID(P, I, D);
		//TODO Maybe distance + encoderStartDistance or - encoder
		driveTrain.straightPID.setSetpoint(distance);
		
		// Enables StraightPID
		
		driveTrain.straightPID.enable();
	}

}
