package com.atop.main;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.atop.chord.SendFilesFragment;
import com.example.ssm_test.R;

public class SendFilesActivity extends Activity {

	private FragmentTransaction mFragmentTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_file);
		mFragmentTransaction = getFragmentManager().beginTransaction();
		SendFilesFragment sendFilesFragment = new SendFilesFragment();
		mFragmentTransaction.replace(R.id.send_file_fragment_container,
				sendFilesFragment);
		mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		Intent intent = new Intent(SendFilesActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
