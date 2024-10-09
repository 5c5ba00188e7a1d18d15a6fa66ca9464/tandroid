package com.google.android.exoplayer2.util;

import android.os.Looper;

/* loaded from: classes.dex */
public interface HandlerWrapper {

    /* loaded from: classes.dex */
    public interface Message {
        void sendToTarget();
    }

    Looper getLooper();

    boolean hasMessages(int i);

    Message obtainMessage(int i);

    Message obtainMessage(int i, int i2, int i3);

    Message obtainMessage(int i, int i2, int i3, Object obj);

    Message obtainMessage(int i, Object obj);

    boolean post(Runnable runnable);

    void removeCallbacksAndMessages(Object obj);

    void removeMessages(int i);

    boolean sendEmptyMessage(int i);

    boolean sendEmptyMessageAtTime(int i, long j);

    boolean sendMessageAtFrontOfQueue(Message message);
}
