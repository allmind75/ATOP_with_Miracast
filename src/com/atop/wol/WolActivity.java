package com.atop.wol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssm_test.R;

public class WolActivity extends Activity implements OnLongClickListener,
		OnClickListener {

	private TextView name1, name2, mac1, mac2, ip1, ip2;
	private SharedPreferences pref;
	private Wol wol;
	private LinearLayout savecom1, savecom2;
	private EditText editview_name, editview_mac, editview_wolIp;
	private String save_name1, save_mac1, save_ip1, save_name2, save_mac2,
			save_ip2;

	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wol);

		savecom1 = (LinearLayout) findViewById(R.id.save_com1);
		savecom1.setOnLongClickListener(this);
		savecom1.setOnClickListener(this);
		savecom2 = (LinearLayout) findViewById(R.id.save_com2);
		savecom2.setOnLongClickListener(this);
		savecom2.setOnClickListener(this);

		name1 = (TextView) findViewById(R.id.save_com1_name);
		name2 = (TextView) findViewById(R.id.save_com2_name);
		mac1 = (TextView) findViewById(R.id.save_com1_mac);
		mac2 = (TextView) findViewById(R.id.save_com2_mac);
		ip1 = (TextView) findViewById(R.id.save_com1_ip);
		ip2 = (TextView) findViewById(R.id.save_com2_ip);

		editview_name = (EditText) findViewById(R.id.edittext_wol_name);
		editview_mac = (EditText) findViewById(R.id.edittext_wol_mac);
		editview_wolIp = (EditText) findViewById(R.id.edittext_wol_ip);

		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

		save_name1 = pref.getString("name1", "");
		save_mac1 = pref.getString("mac1", "");
		save_ip1 = pref.getString("ip1", "");

		save_name2 = pref.getString("name2", "");
		save_mac2 = pref.getString("mac2", "");
		save_ip2 = pref.getString("ip2", "");

		if (!save_name1.equals("")) { // 1번 공간을 채움.
			setText_SaveInfo1(save_name1, save_mac1, save_ip1);
		}
		if (!save_name2.equals("")) { // 2번 공간을 채움.
			setText_SaveInfo2(save_name2, save_mac2, save_ip2);
		}

		Button btn_clean = (Button) findViewById(R.id.btn_wol_clean);
		btn_clean.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				textView_Clean();
				Toast.makeText(WolActivity.this, "삭제 되었습니다.",
						Toast.LENGTH_SHORT).show();
			}
		});

		Button btn_save = (Button) findViewById(R.id.btn_wol_save);
		btn_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				save_name1 = pref.getString("name1", "");
				save_name2 = pref.getString("name2", "");
				if (save_name1.equals("") || count == 1) {
					count = 0;
					SharedPreferences.Editor edit = pref.edit();
					edit.putString("name1", editview_name.getText().toString());
					edit.putString("mac1", editview_mac.getText().toString());
					edit.putString("ip1", editview_wolIp.getText().toString());
					edit.commit();

					Toast.makeText(WolActivity.this, "저장되었습니다.",
							Toast.LENGTH_SHORT).show();

					setText_SaveInfo1(editview_name.getText().toString(),
							editview_mac.getText().toString(), editview_wolIp
									.getText().toString());
					textView_Clean();
					return;
				} else if (save_name2.equals("") || count == 2) {

					count = 0;
					SharedPreferences.Editor edit = pref.edit();
					edit.putString("name2", editview_name.getText().toString());
					edit.putString("mac2", editview_mac.getText().toString());
					edit.putString("ip2", editview_wolIp.getText().toString());
					edit.commit();

					Toast.makeText(WolActivity.this, "저장되었습니다.",
							Toast.LENGTH_SHORT).show();

					setText_SaveInfo2(editview_name.getText().toString(),
							editview_mac.getText().toString(), editview_wolIp
									.getText().toString());
					textView_Clean();
					return;
				} else {

					Toast.makeText(WolActivity.this,
							"저장공간이 부족합니다. 저장된 정보를 지워주세요", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		Button btn_wolsend = (Button) findViewById(R.id.btn_wol_send);
		btn_wolsend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				wol = new Wol(editview_mac.getText().toString());
				wol.start();

				Toast.makeText(WolActivity.this, "패킷이 전송 되었습니다.",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void textView_Clean() {
		editview_name.setText("");
		editview_mac.setText("");
		editview_wolIp.setText("");
	}

	public void setText_SaveInfo1(String name, String mac, String ip) {
		name1.setText(name);
		mac1.setText(mac);
		ip1.setText(ip);
	}

	public void setText_SaveInfo2(String name, String mac, String ip) {
		name2.setText(name);
		mac2.setText(mac);
		ip2.setText(ip);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == savecom1.getId()) {

			save_name1 = pref.getString("name1", "");
			if (!save_name1.equals("")) {

				save_mac1 = pref.getString("mac1", "");
				wol = new Wol(save_mac1);
				wol.start();

				Toast.makeText(WolActivity.this, save_name1 + " 패킷이 전송 되었습니다.",
						Toast.LENGTH_SHORT).show();
			} else {

			}

		} else if (v.getId() == savecom2.getId()) {

			save_name2 = pref.getString("name2", "");
			if (!save_name2.equals("")) {

				save_mac2 = pref.getString("mac2", "");
				wol = new Wol(save_mac2);
				wol.start();

				Toast.makeText(WolActivity.this, save_name2 + " 패킷이 전송 되었습니다.",
						Toast.LENGTH_SHORT).show();
			} else {

			}
		}

	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == savecom1.getId()) {
			AlertDialog.Builder longDialog = new AlertDialog.Builder(this);
			longDialog.setPositiveButton("수정",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							save_name1 = pref.getString("name1", "");
							if (!save_name1.equals("")) {
								editview_name.setText(pref.getString("name1",
										""));
								editview_mac.setText(pref.getString("mac1", ""));
								editview_wolIp.setText(pref
										.getString("ip1", ""));

								count = 1; 
							}

						}
					}).setNegativeButton("삭제",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences.Editor edit = pref.edit();
							edit.remove("name1");
							edit.remove("mac1");
							edit.remove("ip1");
							edit.commit();

							setText_SaveInfo1("", "", "");
							Toast.makeText(WolActivity.this, "삭제되었습니다.",
									Toast.LENGTH_SHORT).show();
						}
					});
			AlertDialog alert = longDialog.create();
			alert.setTitle("선택하세요");
			alert.show();

		} else if (v.getId() == savecom2.getId()) {
			AlertDialog.Builder longDialog = new AlertDialog.Builder(this);
			longDialog.setPositiveButton("수정",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							save_name2 = pref.getString("name2", "");
							if (!save_name2.equals("")) {
								editview_name.setText(pref.getString("name2",
										""));
								editview_mac.setText(pref.getString("mac2", ""));
								editview_wolIp.setText(pref
										.getString("ip2", ""));

								count = 2;
							}

						}
					}).setNegativeButton("삭제",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences.Editor edit = pref.edit();
							edit.remove("name2");
							edit.remove("mac2");
							edit.remove("ip2");
							edit.commit();

							setText_SaveInfo2("", "", "");
							Toast.makeText(WolActivity.this, "삭제되었습니다.",
									Toast.LENGTH_SHORT).show();
						}
					});
			AlertDialog alert = longDialog.create();
			alert.setTitle("선택하세요");
			alert.show();
		}
		return false;
	}
}
