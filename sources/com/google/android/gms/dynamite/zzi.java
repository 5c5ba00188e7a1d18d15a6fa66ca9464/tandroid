package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

/* loaded from: classes.dex */
final class zzi implements DynamiteModule.VersionPolicy {
    zzi() {
    }

    @Override // com.google.android.gms.dynamite.DynamiteModule.VersionPolicy
    public final DynamiteModule.VersionPolicy.SelectionResult selectModule(Context context, String str, DynamiteModule.VersionPolicy.IVersions iVersions) {
        DynamiteModule.VersionPolicy.SelectionResult selectionResult = new DynamiteModule.VersionPolicy.SelectionResult();
        selectionResult.localVersion = iVersions.zza(context, str);
        int zzb = iVersions.zzb(context, str, true);
        selectionResult.remoteVersion = zzb;
        int i = selectionResult.localVersion;
        if (i == 0) {
            i = 0;
            if (zzb == 0) {
                selectionResult.selection = 0;
                return selectionResult;
            }
        }
        if (i >= zzb) {
            selectionResult.selection = -1;
        } else {
            selectionResult.selection = 1;
        }
        return selectionResult;
    }
}
