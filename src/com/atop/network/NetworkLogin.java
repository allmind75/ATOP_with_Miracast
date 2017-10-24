package com.atop.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

public class NetworkLogin extends Thread {

	private final String TAG = "Class::NetworkLogin";
	private Socket socket;

	private String server_ip;
	private static final int server_port = 5000;

	private InputStream im;
	private BufferedReader br;
	private boolean Logintrue = false;  //�α����� �Ǿ����� Ȯ��.

	private Handler mhandler;  //�α��� ���¸� ������.

	private String PasswordState;  //�н����尡 �´��� Ȯ��.
	private String Password;

	public NetworkLogin(String ip, Handler handler, String password) { // ������.
		this.server_ip = ip;
		this.mhandler = handler;
		this.Password = password;

	}

	public boolean isLoginConnet() {
		return Logintrue;
	}

	public void run() {

		try {
			socket = new Socket();
			InetSocketAddress socketAddr = new InetSocketAddress(server_ip,
					server_port);
			socket.connect(socketAddr, 5000);
			
			Logintrue = true;

			im = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(im));
			
			sendMessage(Password);
			
			while (Logintrue) {

				PasswordState = br.readLine();

				PasswordState = new String(Base64.decode(PasswordState, 0));  //
				Log.e(TAG, "PasswordState : " + PasswordState);

				if (PasswordState.equalsIgnoreCase("True")) {
					Message msg = Message.obtain();
					msg.what = 1;
					mhandler.sendMessage(msg);  //�α����� ����
					Logintrue = false;
				} else {
					Message msg = Message.obtain();
					msg.what = 2;
					mhandler.sendMessage(msg);  //�α����� ����
				}
			}
		} catch (IOException e) {
			Logintrue = false;
			Log.e(TAG, "Socket Connect Exception2 : " + e); // ���� IoException

			Message msg = Message.obtain();
			msg.what = 3;
			mhandler.sendMessage(msg);  // ������ ������ ����
			
		} catch (Exception e) { // ���� Exception
			
			Logintrue = false;
			Log.e(TAG, "Exception : " + e);
			
			Message msg = Message.obtain();
			msg.what = 3;
			mhandler.sendMessage(msg); // ������ ������ ����
		} finally { 
			try {
				Logintrue = false;
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "socket close : " + e);
			}
		}

	} // end of run

	public void sendMessage(String message) {

		try {
			socket.getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			Log.e(TAG, "��Ŷ ���� ����." + e);

		}
	}

	public void Close_Socket() {
		try {
			Logintrue = false;
			socket.close();
		} catch (IOException e) {
			Log.d(TAG, "Socket Close Failed");
			e.printStackTrace();
		}
	}

}
