package com.android.internal.telephony;

interface ITelephony {
	boolean endCall(); //��ȭ ����
	
	void answerRingingCall(); //�������� ��ȭ �ޱ�
	
	void silenceRinger(); //���Ҹ��� �������� �ϴµ�
}