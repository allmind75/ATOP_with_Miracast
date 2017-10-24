package com.atop.callstate;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

public class CallStateReceiver extends BroadcastReceiver {

	private static final String TAG = "CallStateListner";

	private ITelephony telephonyService;
	private boolean notCall = false;

	public void CallState(boolean iscall) {

		this.notCall = !iscall;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (notCall) { // 사용자가 전화 사용 거부 일때 사용
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				Class c = Class.forName(telephony.getClass().getName());
				Method m = c.getDeclaredMethod("getITelephony");
				m.setAccessible(true);
				telephonyService = (ITelephony) m.invoke(telephony);
				telephonyService.endCall();

			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "err : " + e);
			}
		}
	}
}
