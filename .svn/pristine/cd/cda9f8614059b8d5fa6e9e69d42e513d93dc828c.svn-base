package com.atop.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.atop.chord.ChordFragment;
import com.atop.main.SurfaceViewMain;

public class NetworkTCP_Image_two extends Thread {
	private final String TAG = "Class::NetworkTCP_Image_two";

	private Socket socket;

	private String server_ip;
	private static final int server_port = 3000;

	private Bitmap bitmap; // 서버에서 받아온 bitmap
	private byte[] receiveData; // 전체 Data
	private byte[] byteSize; // size
	private int intSize;
	private InputStream im;
	private boolean socketConnect = false;

	private ChordFragment chord;
	private ByteArrayOutputStream bos;
	long end, start, end2;
	int cnt;

	private SurfaceViewMain mSurfaceViewMain;
	private int deviceNum;
	private boolean tcpFlag = true;

	public NetworkTCP_Image_two(ChordFragment chord,
			SurfaceViewMain mSurfaceViewMain, String ip, int deviceNum) {

		this.server_ip = ip;
		this.chord = chord;
		this.mSurfaceViewMain = mSurfaceViewMain;
		this.deviceNum = deviceNum;
	}

	public boolean istcpConnet() {
		return socketConnect;
	}

	public void run() {

		try {
			cnt = 0;
			socket = new Socket();
			InetSocketAddress socketAddr = new InetSocketAddress(server_ip,
					server_port);
			socket.connect(socketAddr, 7000);

			socketConnect = true;

			im = socket.getInputStream();
			start = System.currentTimeMillis();
			while (socketConnect) {
				if (tcpFlag) {
					cnt++;

					sendMessage("Size");
					byteSize = new byte[4];

					im.read(byteSize);
					intSize = byte2Int(byteSize);

					int len = 0;
					int size = 7168;
					byte[] data = new byte[size];

					bos = new ByteArrayOutputStream();

					while (len < intSize) {
						int readSize = im.read(data);
						len = len + readSize;
						bos.write(data, 0, readSize);
					}

					receiveData = bos.toByteArray();

					bos.flush();
					bos.close();
					
//					BitmapFactory.Options options = new BitmapFactory.Options();				
//					if (deviceNum == 1) {
//						options.inSampleSize = 1;
//					} else {
//						options.inSampleSize = 1;
//					}				
					//options.inPreferredConfig = Bitmap.Config.RGB_565;
					//options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					bitmap = BitmapFactory.decodeByteArray(receiveData, 0, receiveData.length);
					
					if (tcpFlag && mSurfaceViewMain != null ) {
						if (deviceNum == 1) {
							mSurfaceViewMain.receiveThreadBitmap(bitmap);
							
						} else {
							if (chord != null) {
								chord.setThreadimage(bitmap);
							}
						}
					}	
				}
			}
		} catch (IOException e) {
			socketConnect = false;
			Log.e(TAG, "Socket Connect Exception2 : " + e); // 소켓 IoException

		} catch (Exception e) { // 소켓 Exception
			socketConnect = false;
			Log.e(TAG, "Exception : " + e);
		} finally {
			try {

				sendMessage("quit" + "\r\n");
				socketConnect = false;
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
			Log.e(TAG, "패킷 전송 실패." + e);

		}
	}

	public void setdevicenum(int num) {
		deviceNum = num;
	}

	public void Close_Socket() {
		try {
			socketConnect = false;
			sendMessage("quit" + "\r\n");
			socket.close();
		} catch (IOException e) {
			Log.d(TAG, "Socket Close Failed");
			e.printStackTrace();
		}
	}

	public static int byte2Int(byte[] src) {
		int s1 = src[0] & 0xFF;
		int s2 = src[1] & 0xFF;
		int s3 = src[2] & 0xFF;
		int s4 = src[3] & 0xFF;

		return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
	}

	public void changeSurfaceView(SurfaceViewMain mSurfaceViewMain) {

		this.mSurfaceViewMain = mSurfaceViewMain;
	}

	public void setTcpFlag(boolean tcpFlag) {
		this.tcpFlag = tcpFlag;
		Log.e(TAG, "tcpFlag");
	}
}