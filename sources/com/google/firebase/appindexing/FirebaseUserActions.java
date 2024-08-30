package com.google.firebase.appindexing;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.internal.zzt;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public abstract class FirebaseUserActions {
    private static WeakReference zza;

    public static synchronized FirebaseUserActions getInstance(Context context) {
        synchronized (FirebaseUserActions.class) {
            Preconditions.checkNotNull(context);
            WeakReference weakReference = zza;
            FirebaseUserActions firebaseUserActions = weakReference == null ? null : (FirebaseUserActions) weakReference.get();
            if (firebaseUserActions == null) {
                zzt zztVar = new zzt(context.getApplicationContext());
                zza = new WeakReference(zztVar);
                return zztVar;
            }
            return firebaseUserActions;
        }
    }

    public abstract Task end(Action action);
}
