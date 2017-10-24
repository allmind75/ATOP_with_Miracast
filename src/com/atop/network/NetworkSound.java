package com.atop.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class NetworkSound extends Thread {
	private final String TAG = "Class::NetworkTCP_Sound";
	static final int frequency = 44100;
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	private Socket socket;
	boolean isPlaying;
	int playBufSize;
	Socket connfd;
	AudioTrack audioTrack;

	private String server_ip;
	private int server_port = 4000;

	private InputStream im;
	private BufferedInputStream br;
	private int speed = 0;

	public NetworkSound(String ip) {
		this.server_ip = ip;
		playBufSize = AudioTrack.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelConfiguration, audioEncoding, playBufSize * 2,
				AudioTrack.MODE_STREAM);

		audioTrack.setStereoVolume(1f, 1f);
	}

	public void run() {

		try {
			socket = new Socket();
			InetSocketAddress socketAddr = new InetSocketAddress(server_ip,
					server_port);
			socket.connect(socketAddr, 5000);

			im = socket.getInputStream();
			br = new BufferedInputStream(im);
			// android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			// byte[] buffer = new byte[115200];//19200

			byte[] buffer = new byte[playBufSize];
			speed = 47000;
			audioTrack.setPlaybackRate(speed);
			audioTrack.play();
			isPlaying = true;

			while (isPlaying) {
				int readSize = 0;
				try {

					readSize = br.read(buffer);
					audioTrack.write(buffer, 0, readSize);
					//Log.e(TAG, "readSize : " + readSize);
					// buffer = new byte[7680];
				} catch (Exception e) {
					Log.e(TAG, "오류 : " + e);
				}
			}
		} catch (IOException e) {
			isPlaying = false;
			Log.e(TAG, "Socket Connect Exception2 : " + e); // 소켓 IoException

		} catch (Exception e) { // 소켓 Exception
			isPlaying = false;
			Log.e(TAG, "Exception : " + e);
		} finally {
			try {
				sendMessage("quit" + "\r\n");
				isPlaying = false;
				audioTrack.stop();
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "socket close : " + e);
			}
		}
	} // end of run

	public void setVolume(float lvol, float rvol) {
		audioTrack.setStereoVolume(lvol, rvol);
	}

	public void sendMessage(String message) {

		try {
			socket.getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			Log.e(TAG, "패킷 전송 실패." + e);

		}
	}

	public void Close_Socket() {
		try {
			isPlaying = false;
			sendMessage("quit" + "\r\n");
			audioTrack.stop();
			socket.close();
		} catch (IOException e) {
			Log.d(TAG, "Socket Close Failed");
			e.printStackTrace();
		}
	}

	public void upSpeed() {
		speed += 500;
		audioTrack.setPlaybackRate(speed);
	}

	public void downSpeed() {
		speed -= 500;
		audioTrack.setPlaybackRate(speed);
	}

}
