package com.atop.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.ssm_test.R;

public class SettingActivity extends Activity implements
		OnCheckedChangeListener {
	private SharedPreferences mainPref;
	private CheckBox check_call_on, check_call_off;
	private Button btn_save_setting;
	private String settingState;
	private boolean saveCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		check_call_on = (CheckBox) findViewById(R.id.checkbox_call_on);
		check_call_on.setOnCheckedChangeListener(this);
		check_call_off = (CheckBox) findViewById(R.id.checkbox_call_off);
		check_call_off.setOnCheckedChangeListener(this);

		mainPref = getSharedPreferences("mainpref", 0);

		settingState = mainPref.getString("setting", null);

		if (settingState != null) {
			saveCall = mainPref.getBoolean("call", true);

			check_call_on.setChecked(saveCall);
			check_call_off.setChecked(!saveCall);

		}

		btn_save_setting = (Button) findViewById(R.id.btn_save_setting);
		btn_save_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = mainPref.edit();
				editor.putString("setting", "ok");
				editor.putBoolean("call", check_call_on.isChecked());

				editor.commit();

				Toast.makeText(SettingActivity.this, "저장되었습니다.",
						Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.checkbox_call_on) {
			if (isChecked) {
				check_call_off.setChecked(false);

			} else {
				check_call_off.setChecked(true);
			}

		} else if (buttonView.getId() == R.id.checkbox_call_off) {
			if (isChecked) {
				check_call_on.setChecked(false);

			} else {
				check_call_on.setChecked(true);
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}
	
	


}
