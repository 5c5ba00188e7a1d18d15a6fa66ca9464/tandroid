package com.google.android.gms.internal.mlkit_common;

import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public class zzal {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, int i2) {
        if (i2 >= 0) {
            int i3 = i + (i >> 1) + 1;
            if (i3 < i2) {
                int highestOneBit = Integer.highestOneBit(i2 - 1);
                i3 = highestOneBit + highestOneBit;
            }
            return i3 < 0 ? ConnectionsManager.DEFAULT_DATACENTER_ID : i3;
        }
        throw new AssertionError("cannot store more than MAX_VALUE elements");
    }
}
