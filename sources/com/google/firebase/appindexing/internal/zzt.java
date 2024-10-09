package com.google.firebase.appindexing.internal;

import android.content.Context;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndexingInvalidArgumentException;
import com.google.firebase.appindexing.FirebaseUserActions;

/* loaded from: classes.dex */
public final class zzt extends FirebaseUserActions {
    private final zzr zza;

    public zzt(Context context) {
        this.zza = new zzr(context);
    }

    private final Task zza(int i, Action action) {
        zzc[] zzcVarArr = new zzc[1];
        if (action != null) {
            if (!(action instanceof zzc)) {
                return Tasks.forException(new FirebaseAppIndexingInvalidArgumentException("Custom Action objects are not allowed. Please use the 'Actions' or 'ActionBuilder' class for creating Action objects."));
            }
            zzc zzcVar = (zzc) action;
            zzcVarArr[0] = zzcVar;
            zzcVar.zza().zza(i);
        }
        return this.zza.doWrite(new zzq(this, zzcVarArr));
    }

    @Override // com.google.firebase.appindexing.FirebaseUserActions
    public final Task end(Action action) {
        return zza(2, action);
    }
}
