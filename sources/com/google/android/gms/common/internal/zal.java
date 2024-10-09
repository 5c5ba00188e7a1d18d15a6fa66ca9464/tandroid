package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.SparseIntArray;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
public final class zal {
    private final SparseIntArray zaa = new SparseIntArray();
    private GoogleApiAvailabilityLight zab;

    public zal(GoogleApiAvailabilityLight googleApiAvailabilityLight) {
        Preconditions.checkNotNull(googleApiAvailabilityLight);
        this.zab = googleApiAvailabilityLight;
    }

    public final int zaa(Context context, int i) {
        return this.zaa.get(i, -1);
    }

    public final int zab(Context context, Api.Client client) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(client);
        int i = 0;
        if (!client.requiresGooglePlayServices()) {
            return 0;
        }
        int minApkVersion = client.getMinApkVersion();
        int zaa = zaa(context, minApkVersion);
        if (zaa == -1) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.zaa.size()) {
                    i = -1;
                    break;
                }
                int keyAt = this.zaa.keyAt(i2);
                if (keyAt > minApkVersion && this.zaa.get(keyAt) == 0) {
                    break;
                }
                i2++;
            }
            zaa = i == -1 ? this.zab.isGooglePlayServicesAvailable(context, minApkVersion) : i;
            this.zaa.put(minApkVersion, zaa);
        }
        return zaa;
    }

    public final void zac() {
        this.zaa.clear();
    }
}
