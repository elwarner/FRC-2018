package org.usfirst.frc.team3997.robot.feed;

import edu.wpi.first.wpilibj.Preferences;

public class DashboardInput {

	Preferences preferences;

	public DashboardInput() {
		try {

			DashboardVariables.firstAutoDistance = preferences.getDouble("First Auto Distance", 0);

			DashboardVariables.nextAutoAngle = preferences.getDouble("Next Auto Angle", 0);

			DashboardVariables.lastAutoDistance = preferences.getDouble("Second Auto Distance", 0);

			DashboardVariables.DRIVE_P = preferences.getDouble("Drive P Value", 0);
			DashboardVariables.DRIVE_I = preferences.getDouble("Drive I Value", 0);
			DashboardVariables.DRIVE_D = preferences.getDouble("Drive D Value", 0);

			DashboardVariables.max_speed = preferences.getDouble("Max Speed", 1);
		} catch (Exception e) {

		}

	}

	public void updateInput() {

		try {

			DashboardVariables.firstAutoDistance = preferences.getDouble("First Auto Distance", 0);

			DashboardVariables.nextAutoAngle = preferences.getDouble("Next Auto Angle", 0);

			DashboardVariables.lastAutoDistance = preferences.getDouble("Second Auto Distance", 0);

			DashboardVariables.DRIVE_P = preferences.getDouble("Drive P Value", 0);
			DashboardVariables.DRIVE_I = preferences.getDouble("Drive I Value", 0);
			DashboardVariables.DRIVE_D = preferences.getDouble("Drive D Value", 0);

			DashboardVariables.max_speed = preferences.getDouble("Max Speed", 1);
		} catch (Exception e) {

		}

	}

}
