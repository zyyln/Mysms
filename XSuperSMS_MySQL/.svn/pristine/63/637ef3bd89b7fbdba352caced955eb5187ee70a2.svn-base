package com.android.uhflibs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceControl {

	private BufferedWriter CtrlFile;

	DeviceControl(String path) throws IOException {
		File DeviceName = new File(path);
		// open file
		CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
	}

	// poweron as3992 device
	public void PowerOnDevice() throws IOException {
		CtrlFile.write("-wdout64 1");
		CtrlFile.flush();
	}

	// poweroff as3992 device
	public void PowerOffDevice() throws IOException {
		CtrlFile.write("-wdout64 0");
		CtrlFile.flush();
	}

	// close file
	public void DeviceClose() throws IOException {
		CtrlFile.close();
	}

}