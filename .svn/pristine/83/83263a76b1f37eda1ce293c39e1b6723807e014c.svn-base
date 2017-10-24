package com.atop.main;

import android.app.Application;

import com.atop.chord.ChordFragment;
import com.atop.network.NetworkTCP;
import com.atop.network.NetworkTCP_Image;

public class MyApplication extends Application {

	private static final String key = "qkrgustkdwlswnduswkdghksgh123456";
	private NetworkTCP tcpclient;
	private NetworkTCP_Image tcp_image;
	private ChordFragment mChord;
	private String serverIP;
	private int DeviceNum = 1;
	private boolean isCall;
	private boolean WifiP2pState = false;
		
	public String getKey() {
		return key;
	}

	public boolean getCall() {
		return isCall;
	}

	public void setCall(boolean call) {
		this.isCall = call;
	}

	public void setTCP(NetworkTCP tcp) {
		this.tcpclient = tcp;
	}

	public NetworkTCP getTCP() {
		return tcpclient;
	}

	public void setImageTCP(NetworkTCP_Image image) {
		this.tcp_image = image;
	}

	public NetworkTCP_Image getImageTCP() {
		return tcp_image;
	}

	public void setIP(String ip) {
		this.serverIP = ip;
	}

	public String getIP() {
		return serverIP;
	}

	public void setChord(ChordFragment mChord) {
		this.mChord = mChord;
	}

	public ChordFragment getChord() {
		return mChord;
	}

	public void setDeviceNum(int DeviceNum) {
		this.DeviceNum = DeviceNum;
	}

	public int getDeviceNum() {
		return DeviceNum;
	}
	
	public void setWifiP2pState(boolean WifiP2pState) {
		this.WifiP2pState = WifiP2pState;
	}
	
	public boolean getWifiP2pState() {
		return WifiP2pState;
	}

}
