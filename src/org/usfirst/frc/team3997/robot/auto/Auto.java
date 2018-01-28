package org.usfirst.frc.team3997.robot.auto;

import org.usfirst.frc.team3997.robot.MasterController;

public class Auto {
	public AutoRoutine autoRoutine;
	public AutoSelector selector;
	public AutoRoutineRunner runner;
	
	
	public Auto(MasterController controllers) {
		selector = new AutoSelector(controllers);
		runner = new AutoRoutineRunner();
		autoRoutine = selector.getDefaultRoutine();
		
	}
	
	// reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset reset 
	public void reset() {
		AutoRoutineRunner.getTimer().reset();
		
	}
	//List Routines in Smart Dashboard
	public void listOptions() {
		selector.listOptions();
	}
	// start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start  start 
	public void start() {
		autoRoutine = selector.pick();
		runner.setAutoRoutine(autoRoutine);
		
		autoRoutine.prestart();
		
		runner.start();
	}
	// STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP 
	public void stop() {
		AutoRoutineRunner.getTimer().reset();
		AutoRoutineRunner.getTimer().stop();
		runner.stop();
		autoRoutine.m_active = false;

	}

}
