package com.google.android.gms.auth.api.phone;

import android.content.Context;
import com.google.android.gms.internal.auth-api-phone.zzab;
/* loaded from: classes.dex */
public abstract class SmsRetriever {
    public static SmsRetrieverClient getClient(Context context) {
        return new zzab(context);
    }
}
