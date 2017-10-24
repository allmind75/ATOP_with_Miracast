package com.android.internal.telephony;

interface ITelephony {
	boolean endCall(); //통화 종료
	
	void answerRingingCall(); //수신중인 전화 받기
	
	void silenceRinger(); //벨소리를 묵음으로 하는듯
}