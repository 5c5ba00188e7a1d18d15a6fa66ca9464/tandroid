package com.google.android.gms.phenotype;

import android.net.Uri;
import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
public abstract class Phenotype {
    private static final Api API;
    private static final Api.AbstractClientBuilder CLIENT_BUILDER;
    private static final Api.ClientKey CLIENT_KEY;
    private static final zzm zzaj;

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.gms.phenotype.zzm, com.google.android.gms.internal.phenotype.zzd] */
    static {
        Api.ClientKey clientKey = new Api.ClientKey();
        CLIENT_KEY = clientKey;
        zzl zzlVar = new zzl();
        CLIENT_BUILDER = zzlVar;
        API = new Api("Phenotype.API", zzlVar, clientKey);
        zzaj = new com.google.android.gms.internal.phenotype.zzd();
    }

    public static Uri getContentProviderUri(String str) {
        String valueOf = String.valueOf(Uri.encode(str));
        return Uri.parse(valueOf.length() != 0 ? "content://com.google.android.gms.phenotype/".concat(valueOf) : new String("content://com.google.android.gms.phenotype/"));
    }
}
