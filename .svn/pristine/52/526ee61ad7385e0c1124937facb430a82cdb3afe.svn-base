package com.atop.network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

public class FileTransfer extends Thread {
	private final String TAG = "Class::FileTranse";

	private Socket fileSocket;

	private String server_ip;
	private static final int server_port = 6000;
	private DataInputStream fileIm;

	private String dirPath = "/sdcard/ATOP/"; // 파일이 저장 될 경로

	private FileOutputStream fos;
	private BufferedOutputStream bos;

	private byte[] fileNameByte; // 파일 이름 받아오는 바이트
	private int fileNameSize; // 파일 이름의 바이트크기
	private String fileName;
	private byte[] fileSizeByte; // 파일 크기 바이트
	private int fileSize; // 바이트 크기
	private String strSize; // 바이트의 크기를 String으로 확인
	private long fileAllSize; // 파일 전체 크기

	public FileTransfer(String ip) {
		this.server_ip = ip;
	}

	public void run() {

		try {

			fileSocket = new Socket();
			InetSocketAddress socketAddr = new InetSocketAddress(server_ip,
					server_port);
			fileSocket.connect(socketAddr, 5000);

			Log.e(TAG, "file new socket ");
			sendMessage("Open"); // 소켓이 열리는걸 서버에게 알림
			while (true) {

				fileIm = new DataInputStream(fileSocket.getInputStream());

				fileNameByte = new byte[1024];

				fileNameSize = fileIm.read(fileNameByte); // 파일 이름을 받음.

				fileName = new String(fileNameByte, 0, fileNameSize, "UTF-8"); // Byte를
																				// String
																				// 형으로
																				// 변환

				File receiveFile = new File(dirPath + fileName);
				fos = new FileOutputStream(receiveFile);
				bos = new BufferedOutputStream(fos); // 파일 생성

				sendMessage("Size"); // Size 받기를 서버에게 알림

				fileSizeByte = new byte[1024];
				fileSize = fileIm.read(fileSizeByte);

				strSize = new String(fileSizeByte, 0, fileSize, "UTF-8");
				Log.e(TAG, "file size : " + strSize);
				fileAllSize = Long.parseLong(strSize);

				sendMessage("Ready"); // 파일 받을 준비가 됬음을 알림.

				long len = 0;
				int size = 4096;
				byte[] data = new byte[size];

				long cut = (fileAllSize / 4);

				while (len != fileAllSize) {
					int readSize = fileIm.read(data);
					len = len + readSize;

					if (len > cut) { // 파일의 크기가 클 경우를 생각해서 전체 파일의 1/4 이 되었을 때
										// 한번씩 Stream을 비워줌
						cut = cut + len;
						bos.write(data, 0, readSize);
						bos.flush();
						continue;
					}

					bos.write(data, 0, readSize);
				}

				bos.flush();
				bos.close();
				fos.close(); // 파일닫기

			}

		} catch (IOException e) {

			Log.e(TAG, "Socket Connect Exception2 : " + e); // 소켓 IoException

		} catch (Exception e) { // 소켓 Exception

			Log.e(TAG, "Exception : " + e);
		} finally {
			try {
				fileIm.close();
				Log.e(TAG, "End");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	} // end of run

	public void Close_Socket() {
		try {
			sendMessage("End" + "\r\n");
			fileSocket.close();
		} catch (IOException e) {
			Log.d(TAG, "Socket Close Failed");
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {

		try {
			fileSocket.getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			Log.e(TAG, "패킷 전송 실패." + e);

		}
	}
}
