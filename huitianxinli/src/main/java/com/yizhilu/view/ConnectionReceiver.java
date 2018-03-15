package com.yizhilu.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectionReceiver extends BroadcastReceiver {

	private ConnectivityManager mConnectivityManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent netStatus = new Intent();
		netStatus.setAction("DOWNLOADSERVER_NETWORKCHANGE");
		netStatus.putExtra("isDownload", false);
		context.sendBroadcast(netStatus);

	}

}
