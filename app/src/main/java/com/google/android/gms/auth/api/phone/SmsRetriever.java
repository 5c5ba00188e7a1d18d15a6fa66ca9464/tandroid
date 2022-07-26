package com.google.android.gms.auth.api.phone;

import android.content.Context;
import com.google.android.gms.internal.auth-api-phone.zzj;
/* loaded from: classes.dex */
public final class SmsRetriever {
    public static SmsRetrieverClient getClient(Context context) {
        return new zzj(context);
    }
}
