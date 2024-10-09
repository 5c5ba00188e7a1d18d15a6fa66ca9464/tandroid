package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public abstract class UnregisterListenerMethod {
    private final ListenerHolder.ListenerKey zaa;

    /* JADX INFO: Access modifiers changed from: protected */
    public UnregisterListenerMethod(ListenerHolder.ListenerKey listenerKey) {
        this.zaa = listenerKey;
    }

    public ListenerHolder.ListenerKey getListenerKey() {
        return this.zaa;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void unregisterListener(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource);
}
