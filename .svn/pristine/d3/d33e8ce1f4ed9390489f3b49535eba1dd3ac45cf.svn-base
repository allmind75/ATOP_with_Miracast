package com.atop.chord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atop.adapter.NodeListAdapter;
import com.atop.adapter.NodeListAdapter.IFileCancelListener;
import com.atop.main.FileSelectActivity;
import com.example.ssm_test.R;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.chord.Schord;
import com.samsung.android.sdk.chord.SchordChannel;
import com.samsung.android.sdk.chord.SchordManager;
import com.samsung.android.sdk.chord.SchordManager.NetworkListener;

public class SendFilesFragment extends Fragment implements IFileCancelListener {

	private static final String CHORD_SEND_TEST_CHANNEL = "com.atop.chord.FILE";
	private static final String MESSAGE_TYPE_FILE_NOTIFICATION = "FILE_NOTIFICATION_V2";
	private static final String chordFilePath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ATOP";
	private static final int SHARE_FILE_TIMEOUT_MILISECONDS = 1000 * 60 * 60;
	private static final int SEND_FILE = 1;
	private static final int SEND_MULTI_FILES = 2;

	private int mSend_api = 2;

	private SchordManager mSchordManager_1 = null;
	private SchordManager mSchordManager_2 = null;
	private SchordManager mSchordManager_3 = null;

	private Drawable mDrawableConnected = null;
	private Drawable mDrawableDisconnected = null;

	private boolean mWifi_bStarted = false;
	private boolean mWifiDirect_bStarted = false;
	private boolean mWifi_disconnected = false;
	private boolean mWifiDirect_disconnected = false;
	private boolean mMobileAP_disconnected = false;

	private TextView mMyNodeName_textView = null;
	private TextView mJoinedNodeList_textView = null;

	private NodeListAdapter mNodeListAdapter = null;
	private ExpandableListView mNode_listView = null;
	private Button mSend_btn = null;
	private Spinner mSend_version_spinner = null;
	private Spinner mMultifiles_limitCnt_spinner = null;

	private HashMap<String, AlertDialog> mAlertDialogMap = null;
	private HashMap<String, Integer> mNodeNumberMap = new HashMap<String, Integer>();

	private int mNodeNumber = 0;
	private SparseIntArray mInterfaceMap = new SparseIntArray();

	private static final String TAG = "[Chord][FileFragment]";
	private static final String TAGClass = "SendFilesFragment : ";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.send_files_fragment, null);

		mMyNodeName_textView = (TextView) view
				.findViewById(R.id.myNodeName_textView);
		mMyNodeName_textView.setHint(getString(R.string.my_node_name, " "));
		mJoinedNodeList_textView = (TextView) view
				.findViewById(R.id.joinedNodeList_textView);
		mJoinedNodeList_textView.setText(getString(R.string.joined_node_list,
				"Empty"));

		// Set an adapter for the list of nodes and progressBars.
		mNodeListAdapter = new NodeListAdapter(getActivity().getBaseContext(),
				this);
		mNodeListAdapter.setSecureChannelFrag(false);
		mNode_listView = (ExpandableListView) view
				.findViewById(R.id.node_listView);
		mNode_listView.setAdapter(mNodeListAdapter);
		mNode_listView.setDivider(new ColorDrawable(Color.YELLOW));
		mNode_listView.setDividerHeight(3);

		mSend_btn = (Button) view.findViewById(R.id.send_btn);

		mSend_api = SEND_MULTI_FILES;

		mSend_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mNodeListAdapter.getCheckedNodeList().isEmpty()) {
					Toast.makeText(getActivity(),
							"Please select at least one node",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Call the activity for selecting files.
				Intent mIntent = new Intent(getActivity()
						.getApplicationContext(), FileSelectActivity.class);
				startActivityForResult(mIntent, 0);
			}
		});

		mAlertDialogMap = new HashMap<String, AlertDialog>();

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// After selecting files to send
		switch (resultCode) {
		case Activity.RESULT_OK:
			ArrayList<String> fileList = data
					.getStringArrayListExtra("SELECTED_FILE");
			String trId = null;
			String checkedNodeInfo[][] = new String[1][];
			String toNode = null;
			String interfaceName = null;
			int interfaceType = -1;
			SchordChannel channel = null;

			for (int i = 0; i < mNodeListAdapter.getCheckedNodeList().size(); i++) {

				// Get the list of checked nodes to send files.
				checkedNodeInfo = mNodeListAdapter.getCheckedNodeList().get(i);
				interfaceName = checkedNodeInfo[i][0];
				toNode = checkedNodeInfo[i][1];

				interfaceType = getInterfaceType(interfaceName);
				channel = getJoinedChannelByIfcType(interfaceType);
				if (channel == null) {

					return;
				}

				if (fileList.isEmpty()) {
					Toast.makeText(getActivity(),
							"Please select at least one file.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (mSend_api == SEND_FILE) {
					if (fileList.size() > 1) {
						Toast.makeText(getActivity(),
								"Don't select more than one file.",
								Toast.LENGTH_SHORT).show();
						return;
					}

					try {
						/**
						 * 6. Send a file to the selected node
						 */
						trId = channel
								.sendFile(toNode,
										MESSAGE_TYPE_FILE_NOTIFICATION,
										fileList.get(0),
										SHARE_FILE_TIMEOUT_MILISECONDS);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {

					}

					// Set the total count of files to send. (set to 1)
					mNodeListAdapter.setFileTotalCnt(interfaceName, toNode, 1,
							trId);

				} else if (mSend_api == SEND_MULTI_FILES) {
					/**
					 * 6. Send multiFile to the selected node
					 */

					try {
						trId = channel.sendMultiFiles(toNode,
								MESSAGE_TYPE_FILE_NOTIFICATION, fileList,
								SHARE_FILE_TIMEOUT_MILISECONDS);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {

					}

					// Set the total count of files to send.
					mNodeListAdapter.setFileTotalCnt(interfaceName, toNode,
							fileList.size(), trId);
				}

			}

			if (null == trId) { // failed to send
				Toast.makeText(getActivity(),
						getString(R.string.sending_ps_failed, fileList.size()),
						Toast.LENGTH_SHORT).show();
			} else { // succeed to send

				for (int i = 0; i < mNodeListAdapter.getCheckedNodeList()
						.size(); i++) {
					checkedNodeInfo = mNodeListAdapter.getCheckedNodeList()
							.get(i);
					interfaceType = getInterfaceType(checkedNodeInfo[i][0]);

				}

			}

			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mSchordManager_1 == null) {
			initChord();
		}

	}

	@Override
	public void onDestroy() {

		if (mSchordManager_1 != null) {

			mSchordManager_1.setNetworkListener(null);
			mSchordManager_1.close();
			mSchordManager_1 = null;
		}

		if (mSchordManager_2 != null) {
			mSchordManager_2.close();
			mSchordManager_2 = null;
		}

		if (mSchordManager_3 != null) {
			mSchordManager_3.close();
			mSchordManager_3 = null;
		}

		mAlertDialogMap.clear();
		mNodeNumberMap.clear();
		super.onDestroy();
	}

	// **********************************************************************
	// From adapter
	// **********************************************************************
	@Override
	public void onFileCanceled(String interfaceName, String node, String trId,
			int index, boolean bMulti) {

		int interfaceType = getInterfaceType(interfaceName);

		if (interfaceType != -1) {
			SchordChannel channel = getJoinedChannelByIfcType(interfaceType);
			if (channel == null) {
				Log.e(TAG, "Fail to get the joined Channel");
				return;
			}

			if (bMulti) {
				/**
				 * 7. Cancel the multiFile transfer
				 */
				channel.cancelMultiFiles(trId);
				Log.e(TAG, "Cancel MultiFiles()");
				mNodeListAdapter.removeCanceledProgress(interfaceName, node,
						trId);
			} else {
				/**
				 * 7. Cancel the file transfer
				 */
				channel.cancelFile(trId);
				Log.e(TAG, "Cancel File()");
				mNodeListAdapter.removeProgress(index, trId);
			}
		}

	}

	private void initChord() {

		Schord chord = new Schord();
		try {
			chord.initialize(getActivity());
		} catch (SsdkUnsupportedException e) {
			if (e.getType() == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {

				return;
			}
		}

		mSchordManager_1 = new SchordManager(getActivity());

		mSchordManager_1.setLooper(getActivity().getMainLooper());
		mSchordManager_1.setTempDirectory(chordFilePath);

		/**
		 * Optional. If you need listening network changed, you can set callback
		 * before starting chord.
		 */
		mSchordManager_1.setNetworkListener(new NetworkListener() {

			@Override
			public void onDisconnected(int interfaceType) {
				Toast.makeText(getActivity(),
						getInterfaceName(interfaceType) + " is disconnected",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onConnected(int interfaceType) {
				Toast.makeText(getActivity(),
						getInterfaceName(interfaceType) + " is connected",
						Toast.LENGTH_SHORT).show();

				switch (interfaceType) {

				case SchordManager.INTERFACE_TYPE_WIFI_P2P:
					if (mWifiDirect_disconnected)
						return;
					break;

				}
				startChord(interfaceType);
			}
		});

		List<Integer> ifcList = mSchordManager_1.getAvailableInterfaceTypes();
		for (Integer ifc : ifcList) {
			startChord(ifc);
		}

	}

	private void startChord(int interfaceType) {

		Log.e(TAG, "Start Chord");
		int managerIndex = 0;
		SchordManager startManager = null;

		if (mInterfaceMap.get(interfaceType) == 0) {
			managerIndex = mInterfaceMap.size() + 1;
			mInterfaceMap.put(interfaceType, managerIndex);
		} else {
			managerIndex = mInterfaceMap.get(interfaceType);
		}

		switch (managerIndex) {
		case 1:
			startManager = mSchordManager_1;
			break;
		case 2:
			mSchordManager_2 = new SchordManager(getActivity());
			startManager = mSchordManager_2;
			break;
		case 3:
			mSchordManager_3 = new SchordManager(getActivity());
			startManager = mSchordManager_3;
			break;
		}

		try {
			Log.d(TAG, TAGClass + "start(" + getInterfaceName(interfaceType)
					+ ") with the SchordManager number : " + managerIndex);

			startManager.setLooper(getActivity().getMainLooper());
			startManager.setTempDirectory(chordFilePath);

			switch (interfaceType) {

			case SchordManager.INTERFACE_TYPE_WIFI_P2P:
				startManager.start(interfaceType, mWifiDirect_ManagerListener);
				break;

			}

		} catch (Exception e) {
			mInterfaceMap.delete(interfaceType);
		}

	}

	private SchordManager.StatusListener mWifiDirect_ManagerListener = new SchordManager.StatusListener() {

		@Override
		public void onStarted(String nodeName, int reason) {
			mWifiDirect_bStarted = true;
			String model = Build.MODEL;

			if (!mWifi_bStarted) {
				mMyNodeName_textView.setText(getString(R.string.my_node_name,
						nodeName + " / " + model));
			}

			if (reason == STARTED_BY_USER) {
				joinTestChannel(SchordManager.INTERFACE_TYPE_WIFI_P2P);
			} 

		}

		@Override
		public void onStopped(int reason) {
			mWifiDirect_bStarted = false;

			if (!mWifi_bStarted) {
				mMyNodeName_textView.setText("");
				mMyNodeName_textView.setHint(getString(R.string.my_node_name,
						" "));
			}

			mNodeListAdapter
					.removeNodeGroup(getInterfaceName(SchordManager.INTERFACE_TYPE_WIFI_P2P));
			setJoinedNodeCount();

			if (NETWORK_DISCONNECTED == reason) {
				mWifiDirect_disconnected = true;
			}
		}
	};

	private void joinTestChannel(int interfaceType) {

		Log.e(TAG, "JoinChannel");

		SchordChannel channel = null;
		SchordManager currentManager = null;

		currentManager = getSchordManager(interfaceType);

		switch (interfaceType) {

		case SchordManager.INTERFACE_TYPE_WIFI_P2P:
			channel = currentManager.joinChannel(CHORD_SEND_TEST_CHANNEL,
					mWifiDirect_ChannelListener);
			break;

		}

		if (channel == null) {
			Log.e(TAG, "Fail to joinChannel");
		}
	}

	private SchordChannel.StatusListener mWifiDirect_ChannelListener = new SchordChannel.StatusListener() {

		int interfaceType = SchordManager.INTERFACE_TYPE_WIFI_P2P;

		String interfaceName = getInterfaceName(interfaceType);

		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			onNodeCallbackCommon(false, interfaceType, fromNode);
		}

		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			onNodeCallbackCommon(true, interfaceType, fromNode);
		}

		@Override
		public void onMultiFilesWillReceive(String fromNode,
				String fromChannel, String fileName, String taskId,
				int totalCount, String fileType, long fileSize) {

			onMultiFilesWillReceiveCommon(interfaceType, interfaceName,
					fromNode, fileName, taskId, totalCount, fileSize);
		}

		/**
		 * Called when the sending a file is completed.
		 */
		@Override
		public void onMultiFilesSent(String toNode, String toChannel,
				String fileName, String taskId, int index, String fileType) {

			mNodeListAdapter.removeProgress(index, taskId);
		}

		/**
		 * Called when the receiving a file is completed from the node.
		 */
		@Override
		public void onMultiFilesReceived(String fromNode, String fromChannel,
				String fileName, String taskId, int index, String fileType,
				long fileSize, String tmpFilePath) {

			mNodeListAdapter.removeProgress(index, taskId);
			mNodeListAdapter.removeProgress(index, taskId);
			saveFile(fileName, tmpFilePath);
		}

		/**
		 * Called when the file transfer is finished to the node.
		 */
		@Override
		public void onMultiFilesFinished(String node, String channel,
				String taskId, int reason) {

			onMultiFilesFinishedCommon(interfaceType, interfaceName, node,
					taskId, reason);
		}

		/**
		 * Called when the error is occurred while the file transfer is in
		 * progress.
		 **/
		@Override
		public void onMultiFilesFailed(String node, String channel,
				String fileName, String taskId, int index, int reason) {

			onMultiFilesFailedCommon(interfaceType, interfaceName, node,
					fileName, taskId, index, reason);
		}

		/**
		 * Called when an individual chunk of the file is sent.
		 */
		@Override
		public void onMultiFilesChunkSent(String toNode, String toChannel,
				String fileName, String taskId, int index, String fileType,
				long fileSize, long offset, long chunkSize) {

			// Set the progressBar - add or update
			int progress = (int) (100 * offset / fileSize);
			mNodeListAdapter.setProgressUpdate(interfaceName, toNode, taskId,
					index, progress, true, true);
			expandeGroup();
		}

		/**
		 * Called when the receiving a file is completed from the node.
		 */
		@Override
		public void onMultiFilesChunkReceived(String fromNode,
				String fromChannel, String fileName, String taskId, int index,
				String fileType, long fileSize, long offset) {

			// Set the progressBar - add or update
			int progress = (int) (100 * offset / fileSize);
			mNodeListAdapter.setProgressUpdate(interfaceName, fromNode, taskId,
					index, progress, true, false);
			expandeGroup();
		}

		/**
		 * Called when the Share file notification is received. User can decide
		 * to receive or reject the file.
		 */
		@Override
		public void onFileWillReceive(String fromNode, String fromChannel,
				String fileName, String hash, String fileType,
				String exchangeId, long fileSize) {

			onFileWillReceiveCommon(interfaceType, interfaceName, fromNode,
					fileName, exchangeId, fileSize);
		}

		/**
		 * Called when the file transfer is completed to the node.
		 */
		@Override
		public void onFileSent(String toNode, String toChannel,
				String fileName, String hash, String fileType, String exchangeId) {

			mNodeListAdapter.removeProgress(1, exchangeId);
		}

		/**
		 * Called when the file transfer is completed from the node.
		 */
		@Override
		public void onFileReceived(String fromNode, String fromChannel,
				String fileName, String hash, String fileType,
				String exchangeId, long fileSize, String tmpFilePath) {

			mNodeListAdapter.removeProgress(1, exchangeId);
			saveFile(fileName, tmpFilePath);
		}

		/**
		 * Called when the error is occurred while the file transfer is in
		 * progress.
		 **/
		@Override
		public void onFileFailed(String node, String channel, String fileName,
				String hash, String exchangeId, int reason) {

			onFileFailedCommon(interfaceType, interfaceName, node, fileName,
					exchangeId, reason);
		}

		/**
		 * Called when an individual chunk of the file is sent.
		 */
		@Override
		public void onFileChunkSent(String toNode, String toChannel,
				String fileName, String hash, String fileType,
				String exchangeId, long fileSize, long offset, long chunkSize) {

			// Set the progressBar - add or update
			int progress = (int) (100 * offset / fileSize);
			mNodeListAdapter.setProgressUpdate(interfaceName, toNode,
					exchangeId, 1, progress, false, true);
			expandeGroup();
		}

		/**
		 * Called when the file transfer is completed from the node.
		 */
		@Override
		public void onFileChunkReceived(String fromNode, String fromChannel,
				String fileName, String hash, String fileType,
				String exchangeId, long fileSize, long offset) {

			// Set the progressBar - add or update
			int progress = (int) (100 * offset / fileSize);
			mNodeListAdapter.setProgressUpdate(interfaceName, fromNode,
					exchangeId, 1, progress, false, false);
			expandeGroup();
		}

		/**
		 * The following callBack is not used in this Fragment. Please refer to
		 * the HelloChordFragment.java
		 */
		@Override
		public void onDataReceived(String fromNode, String fromChannel,
				String payloadType, byte[][] payload) {
		}

		@Override
		public void onUdpDataReceived(String fromNode, String fromChannel,
				String payloadType, byte[][] payload, String serviceType) {
		}

		@Override
		public void onUdpDataDelivered(String fromNode, String channelName,
				String reqId) {

		}

	};

	private void onNodeCallbackCommon(boolean isJoin, int interfaceType,
			String fromNode) {

		String interfaceName = getInterfaceName(interfaceType);

		if (isJoin) {
			if (mNodeNumberMap.containsKey(interfaceName + fromNode)) {
				mNodeListAdapter.addNode(interfaceName, fromNode,
						mNodeNumberMap.get(interfaceName + fromNode));

				Log.e(TAG,
						"NodeJoined : "
								+ mNodeNumberMap.get(interfaceName + fromNode)
								+ ":" + fromNode);
			} else {
				mNodeNumber++;
				mNodeNumberMap.put(interfaceName + fromNode, mNodeNumber);
				mNodeListAdapter.addNode(interfaceName, fromNode, mNodeNumber);

				Log.e(TAG, "onNodeJoined : " + mNodeNumber + " : " + fromNode);
			}
		} else {

			Log.e(TAG,
					"onNodeLeft : "
							+ mNodeNumberMap.get(interfaceName + fromNode)
							+ " : " + fromNode);

			mNodeListAdapter.removeNode(interfaceName, fromNode);
		}

		setJoinedNodeCount();
	}

	private void onMultiFilesWillReceiveCommon(int interfaceType,
			String interfaceName, String fromNode, String fileName,
			String taskId, int totalCount, long fileSize) {

		if (checkAvailableMemory(fileSize)) {
			displayFileNotify(interfaceType, fromNode,
					getString(R.string.file_ps_total_pd, fileName, totalCount),
					taskId, SEND_MULTI_FILES);

			// Set the total count of files to receive.
			mNodeListAdapter.setFileTotalCnt(interfaceName, fromNode,
					totalCount, taskId);
		} else {
			/**
			 * Because the external storage may be unavailable, you should
			 * verify that the volume is available before accessing it. But
			 * also, onMultiFilesFailed with ERROR_FILE_SEND_FAILED will be
			 * called while Chord got failed to write file.
			 **/

			SchordChannel channel = getJoinedChannelByIfcType(interfaceType);
			channel.rejectMultiFiles(taskId);

		}

	}

	private void onMultiFilesFinishedCommon(int interfaceType,
			String interfaceName, String node, String taskId, int reason) {

		String delimiter = "_";
		String sentOrReceived = null;

		if (taskId.split(delimiter)[0].equals(node)) {
			sentOrReceived = "from";
		} else {
			sentOrReceived = "to";
		}

		switch (reason) {
		case SchordChannel.StatusListener.ERROR_FILE_REJECTED: {

			Log.e(TAG, "onMultiFilesFinished() : Rejected by Node "
					+ mNodeNumberMap.get(interfaceName + node));
			break;
		}
		case SchordChannel.StatusListener.ERROR_FILE_CANCELED: {
			String myNodeName = null;

			if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI) {
				myNodeName = mSchordManager_1.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_P2P) {
				myNodeName = mSchordManager_2.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_AP) {
				// myNodeName = mMobileAP_ChordManager.getName();
			}

			break;
		}
		case SchordChannel.StatusListener.ERROR_NONE:
			break;
		default:
			Log.e(TAG, "onMultiFiles Finished Error : " + getErrorName(reason)
					+ " : " + reason);
			break;
		}

	}

	private void onMultiFilesFailedCommon(int interfaceType,
			String interfaceName, String node, String fileName, String taskId,
			int index, int reason) {

		switch (reason) {
		case SchordChannel.StatusListener.ERROR_FILE_REJECTED: {
			break;
		}
		case SchordChannel.StatusListener.ERROR_FILE_CANCELED: {
			String myNodeName = null;

			if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI) {
				myNodeName = mSchordManager_1.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_P2P) {
				myNodeName = mSchordManager_2.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_AP) {
				// myNodeName = mMobileAP_ChordManager.getName();
			}

			break;
		}
		case SchordChannel.StatusListener.ERROR_FILE_TIMEOUT: {
			break;
		}
		default:
			Log.e(TAG, "onMultiFiles Failed Error : " + getErrorName(reason)
					+ " : " + reason);

			break;
		}

		mNodeListAdapter.removeProgress(index, taskId);
		AlertDialog alertDialog = mAlertDialogMap.get(taskId);
		if (alertDialog != null) {
			alertDialog.dismiss();
			mAlertDialogMap.remove(taskId);
		}

	}

	private void onFileWillReceiveCommon(int interfaceType,
			String interfaceName, String fromNode, String fileName,
			String exchangeId, long fileSize) {

		if (checkAvailableMemory(fileSize)) {
			displayFileNotify(interfaceType, fromNode, fileName, exchangeId,
					SEND_FILE);

			// Set the total count of files to receive.
			mNodeListAdapter.setFileTotalCnt(interfaceName, fromNode, 1,
					exchangeId);
		} else {
			/**
			 * Because the external storage may be unavailable, you should
			 * verify that the volume is available before accessing it. But
			 * also, onFileFailed with ERROR_FILE_SEND_FAILED will be called
			 * while Chord got failed to write file.
			 **/
			SchordChannel channel = getJoinedChannelByIfcType(interfaceType);
			channel.rejectFile(exchangeId);
		}
	}

	private void onFileFailedCommon(int interfaceType, String interfaceName,
			String node, String fileName, String exchangeId, int reason) {

		switch (reason) {
		case SchordChannel.StatusListener.ERROR_FILE_REJECTED: {
			break;
		}
		case SchordChannel.StatusListener.ERROR_FILE_CANCELED: {
			String myNodeName = null;

			if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI) {
				myNodeName = mSchordManager_1.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_P2P) {
				myNodeName = mSchordManager_2.getName();
			} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_AP) {
				// myNodeName = mMobileAP_ChordManager.getName();
			}
			break;
		}
		case SchordChannel.StatusListener.ERROR_FILE_TIMEOUT: {
			break;
		}
		default:
			break;
		}

		mNodeListAdapter.removeProgress(1, exchangeId);

		AlertDialog alertDialog = mAlertDialogMap.get(exchangeId);
		if (alertDialog != null) {
			alertDialog.dismiss();
			mAlertDialogMap.remove(exchangeId);
		}

	}

	private String getErrorName(int errorType) {
		if (errorType == SchordChannel.StatusListener.ERROR_FILE_SEND_FAILED) {
			return "ERROR_FILE_SEND_FAILED";
		} else if (errorType == SchordChannel.StatusListener.ERROR_FILE_CREATE_FAILED) {
			return "ERROR_FILE_CREATE_FAILED";
		} else if (errorType == SchordChannel.StatusListener.ERROR_FILE_NO_RESOURCE) {
			return "ERROR_FILE_NO_RESOURCE";
		}

		return "UNKNOWN";
	}

	private String getInterfaceName(int interfaceType) {
		if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI) {
			return "Wi-Fi";
		} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_AP) {
			return "Mobile AP";
		} else if (interfaceType == SchordManager.INTERFACE_TYPE_WIFI_P2P) {
			return "Wi-Fi Direct";
		}

		return "UNKNOWN";
	}

	private int getInterfaceType(String interfaceName) {
		if (interfaceName.equals("Wi-Fi")) {
			return SchordManager.INTERFACE_TYPE_WIFI;
		} else if (interfaceName.equals("Wi-Fi Direct")) {
			return SchordManager.INTERFACE_TYPE_WIFI_P2P;
		} else if (interfaceName.equals("Mobile AP")) {
			return SchordManager.INTERFACE_TYPE_WIFI_AP;
		}

		return -1;
	}

	private SchordManager getSchordManager(int interfaceType) {
		int managerIndex = 0;
		SchordManager manager = null;

		managerIndex = mInterfaceMap.get(interfaceType);

		switch (managerIndex) {
		case 1:
			manager = mSchordManager_1;
			break;
		case 2:
			manager = mSchordManager_2;
			break;
		case 3:
			manager = mSchordManager_3;
			break;
		}
		return manager;
	}

	private SchordChannel getJoinedChannelByIfcType(int ifcType) {
		int managerIndex = 0;
		SchordChannel channel = null;

		managerIndex = mInterfaceMap.get(ifcType);

		switch (managerIndex) {
		case 1:
			channel = mSchordManager_1
					.getJoinedChannel(CHORD_SEND_TEST_CHANNEL);
			break;
		case 2:
			channel = mSchordManager_2
					.getJoinedChannel(CHORD_SEND_TEST_CHANNEL);
			break;
		case 3:
			channel = mSchordManager_3
					.getJoinedChannel(CHORD_SEND_TEST_CHANNEL);
			break;

		}

		return channel;
	}

	private void setJoinedNodeCount() {

		int nodeCnt = mNodeListAdapter.getGroupCount();

		if (nodeCnt == 0) {
			mJoinedNodeList_textView.setText(getString(
					R.string.joined_node_list, "Empty"));
		} else if (nodeCnt == 1) {
			mJoinedNodeList_textView.setText(getString(
					R.string.joined_node_list, nodeCnt + " node"));
		} else {
			mJoinedNodeList_textView.setText(getString(
					R.string.joined_node_list, nodeCnt + " nodes"));
		}

	}

	private boolean checkAvailableMemory(long fileSize) {
		Log.e(TAG, "CheckAvailableMemory");
		File targetdir = new File(chordFilePath);
		if (!targetdir.exists()) {
			targetdir.mkdirs();
		}

		StatFs stat = new StatFs(chordFilePath);
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getAvailableBlocks();
		long availableMemory = blockSize * totalBlocks;

		if (availableMemory < fileSize) {
			return false;
		}

		return true;
	}

	private void displayFileNotify(final int ifc, final String nodeName,
			final String fileName, final String trId, final int sendApi) {

		final SchordChannel channel = getJoinedChannelByIfcType(ifc);
		if (channel == null) {
			return;
		}

		// for dialog whether accept the file transfer or not.
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setTitle("파일 전송")
				.setMessage(
						getString(R.string.from_ps_file_ps, nodeName + " ["
								+ getInterfaceName(ifc) + "]", fileName))
				.setPositiveButton(R.string.accept,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/**
								 * 6. Accept the file transfer
								 */
								if (senderLeft(ifc, nodeName)
										|| !mAlertDialogMap.containsKey(trId)) {
									return;
								}

								if (sendApi == SEND_FILE) {
									channel.acceptFile(trId, 30 * 1000, 2,
											300 * 1024);
								} else if (sendApi == SEND_MULTI_FILES) {
									channel.acceptMultiFiles(trId, 30 * 1000,
											2, 300 * 1024);
								}
								mAlertDialogMap.remove(trId);

							}
						})
				.setNegativeButton(R.string.reject,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/**
								 * 6. Reject the file transfer
								 */
								if (senderLeft(ifc, nodeName)
										|| !mAlertDialogMap.containsKey(trId)) {
									return;
								}

								if (sendApi == SEND_FILE) {
									channel.rejectFile(trId);
								} else if (sendApi == SEND_MULTI_FILES) {
									channel.rejectMultiFiles(trId);
								}
								mAlertDialogMap.remove(trId);

							}
						}).create();

		alertDialog.show();
		mAlertDialogMap.put(trId, alertDialog);
	}

	private boolean senderLeft(int interfaceType, String nodeName) {
		for (String sender : mNodeListAdapter.getNodeList()) {
			if (sender.equals(nodeName)) {
				return false;
			}
		}

		return true;
	}

	private void saveFile(String fileName, String tmpFilePath) {
		String savedName = fileName;
		String name, ext;

		int i = savedName.lastIndexOf(".");
		if (i == -1) {
			name = savedName;
			ext = "";
		} else {
			name = savedName.substring(0, i);
			ext = savedName.substring(i);
		}

		File targetFile = new File(chordFilePath, savedName);
		int index = 0;
		while (targetFile.exists()) {
			savedName = name + "_" + index + ext;
			targetFile = new File(chordFilePath, savedName);
			index++;
		}

		File srcFile = new File(tmpFilePath);
		srcFile.renameTo(targetFile);
	}

	private void expandeGroup() {
		// Expand the list of the progressBar
		int nodeCnt = mNodeListAdapter.getGroupCount();
		for (int i = 0; i < nodeCnt; i++) {
			mNode_listView.expandGroup(i);
		}
	}



}
