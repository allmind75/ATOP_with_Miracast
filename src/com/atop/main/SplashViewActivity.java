package com.atop.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.example.ssm_test.R;

public class SplashViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstaceState){
		super.onCreate(savedInstaceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		Loading();
	}
	
	private void Loading(){
		Handler handler = new Handler(){
			public void handleMessage(Message msg){
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
		};
		handler.sendEmptyMessageDelayed(0, 1000);
	}
}
