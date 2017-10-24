package com.atop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.ssm_test.R;

public class SettingDialog extends Dialog implements OnCheckedChangeListener,
		android.view.View.OnClickListener {
	Context mcontext;
	private CheckBox check_call_on, check_call_off;
	private boolean saveCall;
	private Button btn_save_setting;
	private Button btnscreenNum, btnscreenNum2, btnscreenNum3, btnscreenNum4,
			btnscreenNum5;
	private int checkFlag = 0;

	public SettingDialog(Context context, boolean iscall, int numCheck) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_setting);

		saveCall = iscall;
		checkFlag = numCheck;

		mcontext = context;
		check_call_on = (CheckBox) findViewById(R.id.checkbox_call_on_dialog);
		check_call_on.setOnCheckedChangeListener(this);
		check_call_off = (CheckBox) findViewById(R.id.checkbox_call_off_dialog);
		check_call_off.setOnCheckedChangeListener(this);
		if (saveCall) { // 전화 사용이 켜져있을 때
			check_call_on.setChecked(true);
			check_call_off.setChecked(false);
		} else {
			check_call_on.setChecked(false);
			check_call_off.setChecked(true);
		}

		btn_save_setting = (Button) findViewById(R.id.btn_save_dialog);
		btn_save_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mcontext, "저장되었습니다.", Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
		
		btnscreenNum = (Button) findViewById(R.id.btn_dialog_layout1);
		btnscreenNum.setOnClickListener(this);
		btnscreenNum.setTag(2);
		btnscreenNum2 = (Button) findViewById(R.id.btn_dialog_layout2);
		btnscreenNum2.setOnClickListener(this);
		btnscreenNum2.setTag(3);
		btnscreenNum3 = (Button) findViewById(R.id.btn_dialog_layout3);
		btnscreenNum3.setOnClickListener(this);
		btnscreenNum3.setTag(4);
		btnscreenNum4 = (Button) findViewById(R.id.btn_dialog_layout4);
		btnscreenNum4.setOnClickListener(this);
		btnscreenNum4.setTag(5);
		btnscreenNum5 = (Button) findViewById(R.id.btn_dialog_layout5);
		btnscreenNum5.setOnClickListener(this);
		btnscreenNum5.setTag(6);

		setDevice(checkFlag);
	}

	public boolean getcall() {

		return saveCall;
	}

	public int getNum() {
		return checkFlag;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.checkbox_call_on_dialog) {
			if (isChecked) {
				saveCall = true;
				check_call_off.setChecked(false);
			} else {
				check_call_off.setChecked(true);
			}

		} else if (buttonView.getId() == R.id.checkbox_call_off_dialog) {
			if (isChecked) {
				saveCall = false;
				check_call_on.setChecked(false);
			} else {
				check_call_on.setChecked(true);
			}

		}
	}

	private void setDeviceNum(int num) {
		checkFlag = num;
	}

	private void setFreeDevice(int num) {
		switch (num) {
		case 2: {
			btnscreenNum.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.layout_1, 0, 0);
			break;
		}
		case 3: {
			btnscreenNum2.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.layout_2, 0, 0);
			break;
		}
		case 4: {
			btnscreenNum3.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.layout_3, 0, 0);
			break;
		}
		case 5: {
			btnscreenNum4.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.layout_4, 0, 0);
			break;
		}
		case 6: {
			btnscreenNum5.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.layout_5, 0, 0);
			break;
		}
		default:
			break;
		}
	}

	private void setDevice(int num) {
		switch (num) {

		case 2: {

			btnscreenNum.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.check_layout_1, 0, 0);
			setDeviceNum(2);

			break;
		}
		case 3: {
			btnscreenNum2.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.check_layout_2, 0, 0);
			setDeviceNum(3);
			break;
		}
		case 4: {
			btnscreenNum3.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.check_layout_3, 0, 0);
			setDeviceNum(4);
			break;
		}
		case 5: {
			btnscreenNum4.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.check_layout_4, 0, 0);
			setDeviceNum(5);
			break;
		}
		case 6: {
			btnscreenNum5.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.check_layout_5, 0, 0);
			setDeviceNum(6);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		int selectNum = Integer.parseInt(v.getTag().toString());

		if (selectNum != checkFlag && checkFlag != 0) {
			setFreeDevice(checkFlag);
		}
		setDevice(selectNum);
	}

}
