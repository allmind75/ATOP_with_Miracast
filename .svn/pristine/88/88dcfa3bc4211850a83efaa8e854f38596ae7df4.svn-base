package com.atop.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.atop.adapter.FileListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ssm_test.R;

public class FileSelectActivity extends Activity implements OnItemClickListener {


	private final String TAG = "[Chord][File]";
	private final String TAGClass = "FileSelectActivity : ";
	private Button mSelect_btn;
	private TextView mPwd_textView;
	private ListView mFileListView;
	private FileListAdapter mFilelistAdapter = null;
	private List<String> filePathList = null;
	private String homeDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/ATOP";
	private Intent returnIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fileselect);

		mSelect_btn = (Button) findViewById(R.id.select_btn);
		mPwd_textView = (TextView) findViewById(R.id.pwd_textView);

		mFileListView = (ListView) findViewById(R.id.filelist);
		mFileListView.setOnItemClickListener(this);

		returnIntent = new Intent();

		mSelect_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnFilePath();
			}
		});

		getDir(homeDir);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		File file = new File(filePathList.get(position));

		if (file.isDirectory()) {
			if (file.canRead()) {
				getDir(filePathList.get(position));
			} else {
				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_lock_lock)
						.setTitle(
								"[" + file.getName()
										+ "] folder Can't be Read!!")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		} else {
			Log.v(TAG,
					TAGClass + "fileSelected @@ file : "
							+ file.getAbsolutePath());
		}
	}

	private void getDir(String dir) {
		mPwd_textView.setText(dir);

		filePathList = new ArrayList<String>();
		mFilelistAdapter = new FileListAdapter(this);

		File file = new File(dir);
		File[] files = file.listFiles();

		if (!dir.equals(homeDir)) {
			filePathList.add(homeDir);
			filePathList.add(file.getParent());

			mFilelistAdapter.addFile(homeDir);
			mFilelistAdapter.addFile("../");
		}else{
			mFilelistAdapter.addFile("../");
			filePathList.add(file.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File mfile = files[i];
			filePathList.add(mfile.getPath());

			if (mfile.isDirectory()) {
				mFilelistAdapter.addFile(mfile.getPath() + "/");
			} else {
				mFilelistAdapter.addFile(mfile.getName());
			}
		}
		mFileListView.setAdapter(mFilelistAdapter);
	}

	private void returnFilePath() {
		ArrayList<String> selectfiles = new ArrayList<String>();
		ArrayList<String> checkedFileList = mFilelistAdapter
				.getCheckedFileList();

		for (int i = 0; i < checkedFileList.size(); i++) {
			String mFilePath = mPwd_textView.getText() + "/"
					+ checkedFileList.get(i);
			selectfiles.add(mFilePath);
		}
		returnIntent.putExtra("SELECTED_FILE", selectfiles);
		setResult(RESULT_OK, returnIntent);

		finish();
	}

}
