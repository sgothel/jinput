package net.java.games.input;

import net.java.games.input.Component.Identifier;

public class LinuxDeviceRumbler implements Rumbler {
	
	private int deviceID;
	
	public LinuxDeviceRumbler(int deviceID) {
		this.deviceID = deviceID;
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				cleanup();
			}
		});			
	}

	public void rumble(float intensity) {
		// TODO Auto-generated method stub
		LinuxEnvironmentPlugin.rumble(deviceID, intensity);
	}

	public String getAxisName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Identifier getAxisIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	private void cleanup() {
        System.out.println("LinuxDeviceRumbler.cleanup");
        rumble(0f);
	}
}
