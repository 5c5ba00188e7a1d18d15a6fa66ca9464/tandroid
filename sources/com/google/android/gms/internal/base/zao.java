package com.google.android.gms.internal.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.content.ContextCompat;

/* loaded from: classes.dex */
public abstract class zao extends ContextCompat {
    public static Intent zaa(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Intent registerReceiver;
        if (!zan.zaa()) {
            return context.registerReceiver(broadcastReceiver, intentFilter);
        }
        registerReceiver = context.registerReceiver(broadcastReceiver, intentFilter, true != zan.zaa() ? 0 : 2);
        return registerReceiver;
    }
}
