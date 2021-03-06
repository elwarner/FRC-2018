package org.usfirst.frc.team3997.robot.hardware;


public abstract class RemoteControl {
	public enum Joysticks {
		kDriverJoy, kOperatorJoy
	};
	public enum Axes {
		kLX, kLY, kRX, kRY
	};
	public abstract void readControls();

	public abstract void readAllButtons();

	public abstract double getJoystickValue(Joysticks j, Axes a);

	public abstract boolean getTankDriveDesired();

	// Returns true if arcade drive is desired
	public abstract boolean getArcadeDriveDesired();

	public abstract boolean getSlowDriveTier1Desired();
	public abstract boolean getSlowDriveTier2Desired();

	public abstract boolean getDriveBackDesired();

	public abstract boolean getDriveBackOtherDesired();
	
	public abstract boolean getGearTilterDownDesired();
	public abstract boolean getGearTilterUpDesired();
	public abstract boolean getGearTilterRampDesired();
	public abstract boolean getGearWheelOuttakeDesired();
	public abstract boolean getGearWheelIntakeDesired();
	public abstract boolean getGearIntakeDesired();
	public abstract boolean getManualGearDesired();
}
