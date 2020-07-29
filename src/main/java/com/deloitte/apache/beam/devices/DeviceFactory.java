package com.deloitte.apache.beam.devices;

public class DeviceFactory {

	public Device getDevice(String deviceType){  

		if(deviceType == null){  
			return null;  
		} else if(deviceType == "tcp") // TODO Remove hardcoding. Make it part of the configuration
			return new TCPDevice(); 
		else
			return null;
	}
}
