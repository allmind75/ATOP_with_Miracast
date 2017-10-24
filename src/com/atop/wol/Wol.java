package com.atop.wol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class Wol extends Thread {

	private final static String TAG = "Class::Wol";
	private String MAC;
	private String IP = "255.255.255.255";
	private int PORT = 9;

	public Wol(String mac) {
		this.MAC = mac;
	}

	public void run() {
		try {
			byte[] macBytes = getMacBytes(MAC);
			byte[] bytes = new byte[6 + 16 * macBytes.length];
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) 0xff;
			}
			for (int i = 6; i < bytes.length; i += macBytes.length) {
				System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
			}

			InetAddress address = InetAddress.getByName(IP);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
					address, PORT);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();

			Log.e(TAG, "MAC-Address : " + MAC);
			Log.e(TAG, "BCast-Address : " + IP);
		} catch (Exception e) {
			Log.e(TAG, "send ½ÇÆÐ : " + e);
		}
	}

	private static byte[] getMacBytes(String macStr)
			throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(\\:|\\-)");
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}
		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Invalid hex digit in MAC address.");
		}
		return bytes;
	}
}
