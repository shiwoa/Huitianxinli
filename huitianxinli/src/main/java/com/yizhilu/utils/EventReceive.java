package com.yizhilu.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ming on 2017/2/17 16:22.
 * Description:事件广播工具
 */

public class EventReceive extends BroadcastReceiver {

    private static volatile EventReceive instance;

    private OnEventReceiveListener onEventReceiveListener;

    private String action = null;

    private IntentFilter intentFilter = null;

    public static EventReceive getInstance() {
        if (instance == null) {
            synchronized (EventReceive.class) {
                if (instance == null) {
                    instance = new EventReceive();
                }
            }
        }
        return instance;
    }

    public IntentFilter addAction(String action) {
        this.action = action;
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
        }
        intentFilter.addAction(action);
        return intentFilter;
    }

    public void sendEventBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public void sendEventBroadcast(Context context, String message) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(action, message);
        context.sendBroadcast(intent);
    }

    public String getName() {
        return action;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onEventReceiveListener != null) {
            onEventReceiveListener.onEventReceive(context, intent);
        }
    }

    public interface OnEventReceiveListener {
        void onEventReceive(Context context, Intent intent);
    }

    public void setOnEventReceiveListener(OnEventReceiveListener onEventReceiveListener) {
        this.onEventReceiveListener = onEventReceiveListener;
    }
}
