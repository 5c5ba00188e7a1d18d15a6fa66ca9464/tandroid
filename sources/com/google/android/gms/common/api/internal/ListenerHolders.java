package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ListenerHolders {
    private final Set zaa = Collections.newSetFromMap(new WeakHashMap());

    public static ListenerHolder createListenerHolder(Object obj, Looper looper, String str) {
        Preconditions.checkNotNull(obj, "Listener must not be null");
        Preconditions.checkNotNull(looper, "Looper must not be null");
        Preconditions.checkNotNull(str, "Listener type must not be null");
        return new ListenerHolder(looper, obj, str);
    }

    public static ListenerHolder.ListenerKey createListenerKey(Object obj, String str) {
        Preconditions.checkNotNull(obj, "Listener must not be null");
        Preconditions.checkNotNull(str, "Listener type must not be null");
        Preconditions.checkNotEmpty(str, "Listener type must not be empty");
        return new ListenerHolder.ListenerKey(obj, str);
    }

    public final void zab() {
        for (ListenerHolder listenerHolder : this.zaa) {
            listenerHolder.clear();
        }
        this.zaa.clear();
    }
}
