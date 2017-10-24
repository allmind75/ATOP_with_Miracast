package com.atop.wifiDirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atop.main.MyApplication;
import com.atop.wifiDirect.DeviceListFragment.DeviceActionListener;
import com.example.ssm_test.R;

public class WiFiDirectActivity extends Activity implements ChannelListener,
		DeviceActionListener {

	public static final String TAG = "wifidirectdemo";
	private WifiP2pManager manager;
	private boolean isWifiP2pEnabled = false;
	private boolean retryChannel = false;

	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private BroadcastReceiver receiver = null;
	private MyApplication myApp;

	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_direct);
		myApp = (MyApplication) getApplication();
		
		// add necessary intent values to be matched.

		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		// WIFI_P2P �ʱ�ȭ
		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
	}

	@Override
	public void onResume() {
		super.onResume();

		receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();

		unregisterReceiver(receiver);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}
	/**
	 * Remove all peers and clear all fields. This is called on
	 * BroadcastReceiver receiving a state change event.
	 */
	public void resetData() {
		DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
				.findFragmentById(R.id.frag_list);
//		DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getFragmentManager()
//				.findFragmentById(R.id.frag_detail);
		if (fragmentList != null) {
			fragmentList.clearPeers();
		}
//		if (fragmentDetails != null) {
//			//fragmentDetails.resetViews();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_items, menu);
		return true;
	}

	
	//Wi-Fi ���������� �Ѱ�, Direct ����
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.atn_direct_discover:
			
			WifiManager wManager = null;
			wManager =(WifiManager)getSystemService(Context.WIFI_SERVICE);
			if(!wManager.isWifiEnabled()) {
				Toast.makeText(WiFiDirectActivity.this, "WiFi On", Toast.LENGTH_SHORT).show();
				wManager.setWifiEnabled(true);
				DelayTime(7000);
				wManager =(WifiManager)getSystemService(Context.WIFI_SERVICE);				
			}
			

			if(wManager.isWifiEnabled()) {

				final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
						.findFragmentById(R.id.frag_list);
				fragment.onInitiateDiscovery();

				// Peer Discovery, ������ ���� �Ǵ� �׷� ������ ������ Ȱ������ ����
				manager.discoverPeers(channel,
						new WifiP2pManager.ActionListener() {

							@Override
							public void onSuccess() {
								Toast.makeText(WiFiDirectActivity.this,
										"�˻� �Ϸ�", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(int reasonCode) {
								Toast.makeText(WiFiDirectActivity.this,
										"�˻� ���� : " + reasonCode,
										Toast.LENGTH_SHORT).show();
							}
						});
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void showDetails(WifiP2pDevice device, int deviceStatus) {
		DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
				.findFragmentById(R.id.frag_detail);
		fragment.showDetails(device, device.status);

	}

	@Override
	public void connect(WifiP2pConfig config) {
		// Peer�� �����ϱ� ���� ���ο� WifiP2PConfig ��ü ����,
		manager.connect(channel, config, new ActionListener() {

			@Override
			public void onSuccess() {
				// WiFiDirectBroadcastReceiver will notify us. Ignore for now.
				myApp.setWifiP2pState(true);
			}

			@Override
			public void onFailure(int reason) {
				Toast.makeText(WiFiDirectActivity.this,
						"Connect failed. Retry.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void disconnect() {
//		final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
//				.findFragmentById(R.id.frag_detail);
		
		manager.removeGroup(channel, new ActionListener() {

			@Override
			public void onFailure(int reasonCode) {
				Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

			}

			@Override
			public void onSuccess() {
				//fragment.getView().setVisibility(View.GONE);
				myApp.setWifiP2pState(false);
			}

		});
	}

	@Override
	public void onChannelDisconnected() {
		// we will try once more
		if (manager != null && !retryChannel) {
			Toast.makeText(this, "Channel lost. Trying again",
					Toast.LENGTH_LONG).show();
			resetData();
			retryChannel = true;
			manager.initialize(this, getMainLooper(), this);
		} else {
			Toast.makeText(
					this,
					"Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void cancelDisconnect() {

		/*
		 * A cancel abort request by user. Disconnect i.e. removeGroup if
		 * already connected. Else, request WifiP2pManager to abort the ongoing
		 * request
		 */
		if (manager != null) {
			final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
					.findFragmentById(R.id.frag_list);
			if (fragment.getDevice() == null
					|| fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
				disconnect();
			} else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
					|| fragment.getDevice().status == WifiP2pDevice.INVITED) {

				manager.cancelConnect(channel, new ActionListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(WiFiDirectActivity.this,
								"Aborting connection", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onFailure(int reasonCode) {
						Toast.makeText(
								WiFiDirectActivity.this,
								"Connect abort request failed. Reason Code: "
										+ reasonCode, Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
		}

	}
	
	public void DelayTime(int delayTime) {
		
		long saveTime = System.currentTimeMillis();
		long currentTime = 0;
		while(currentTime - saveTime < delayTime) {
			currentTime = System.currentTimeMillis();
	}
	}
}