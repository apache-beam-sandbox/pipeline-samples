package com.deloitte.apache.beam.process;

import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deloitte.apache.beam.devices.Device;
import com.deloitte.apache.beam.devices.DeviceFactory;
import com.deloitte.apache.beam.shared.DeviceTelemetry;

public class IoTTelemetryMsg extends DoFn<String, DeviceTelemetry> {

	private static final long serialVersionUID = 1462827258689031685L;
	private static final Logger LOG = LoggerFactory.getLogger(IoTTelemetryMsg.class);

	@ProcessElement
	public void processElement(DoFn<String, DeviceTelemetry>.ProcessContext c) throws Exception {
		String dataPacket = c.element();	

		DeviceFactory df = new DeviceFactory();
		Device dev = df.getDevice("tcp");

		try {
			DeviceTelemetry dt = dev.getTelemetryData(dataPacket);
//			LOG.info("DeviceTelemetry packet is:"+dt.toString());
			System.out.println("DeviceTelemetry packet is:"+dt.toString());
			c.output(dt);
		} catch(Exception e)
		{
			LOG.error("Exception in processing packet:"+e.getMessage(), e);
		}
	}
}