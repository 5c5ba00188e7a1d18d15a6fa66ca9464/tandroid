package com.google.android.gms.internal.play_billing;

import java.util.Iterator;
import java.util.Set;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzag {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(Set set) {
        Iterator it = set.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object next = it.next();
            i += next != null ? next.hashCode() : 0;
        }
        return i;
    }
}
