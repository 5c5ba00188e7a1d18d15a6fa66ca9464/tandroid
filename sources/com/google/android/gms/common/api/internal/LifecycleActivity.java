package com.google.android.gms.common.api.internal;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public class LifecycleActivity {
    private final Object zza;

    public LifecycleActivity(Activity activity) {
        Preconditions.checkNotNull(activity, "Activity must not be null");
        this.zza = activity;
    }

    public final Activity zza() {
        return (Activity) this.zza;
    }

    public final FragmentActivity zzb() {
        return (FragmentActivity) this.zza;
    }

    public final boolean zzc() {
        return this.zza instanceof Activity;
    }

    public final boolean zzd() {
        return this.zza instanceof FragmentActivity;
    }
}
