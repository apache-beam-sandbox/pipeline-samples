package com.deloitte.apache.beam.devices;

import com.deloitte.apache.beam.shared.DeviceTelemetry;

public abstract class Device {

	public abstract DeviceTelemetry getTelemetryData(String dataPacket);
}