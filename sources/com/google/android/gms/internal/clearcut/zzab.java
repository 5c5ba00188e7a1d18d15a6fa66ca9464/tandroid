package com.google.android.gms.internal.clearcut;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzab {
    private static final ConcurrentHashMap zzde = new ConcurrentHashMap();
    private static final String[] zzdl = {"key", "value"};
    private final Uri uri;
    private final ContentResolver zzdf;
    private volatile Map zzdi;
    private final Object zzdh = new Object();
    private final Object zzdj = new Object();
    private final List zzdk = new ArrayList();
    private final ContentObserver zzdg = new zzac(this, null);

    private zzab(ContentResolver contentResolver, Uri uri) {
        this.zzdf = contentResolver;
        this.uri = uri;
    }

    public static zzab zza(ContentResolver contentResolver, Uri uri) {
        ConcurrentHashMap concurrentHashMap = zzde;
        zzab zzabVar = (zzab) concurrentHashMap.get(uri);
        if (zzabVar != null) {
            return zzabVar;
        }
        zzab zzabVar2 = new zzab(contentResolver, uri);
        zzab zzabVar3 = (zzab) concurrentHashMap.putIfAbsent(uri, zzabVar2);
        if (zzabVar3 != null) {
            return zzabVar3;
        }
        zzabVar2.zzdf.registerContentObserver(zzabVar2.uri, false, zzabVar2.zzdg);
        return zzabVar2;
    }

    private final Map zzi() {
        try {
            HashMap hashMap = new HashMap();
            Cursor query = this.zzdf.query(this.uri, zzdl, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    try {
                        hashMap.put(query.getString(0), query.getString(1));
                    } catch (Throwable th) {
                        query.close();
                        throw th;
                    }
                }
                query.close();
            }
            return hashMap;
        } catch (SQLiteException | SecurityException unused) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzj() {
        synchronized (this.zzdj) {
            try {
                Iterator it = this.zzdk.iterator();
                if (it.hasNext()) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                    throw null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final Map zzg() {
        Map zzi = zzae.zza("gms:phenotype:phenotype_flag:debug_disable_caching", false) ? zzi() : this.zzdi;
        if (zzi == null) {
            synchronized (this.zzdh) {
                try {
                    zzi = this.zzdi;
                    if (zzi == null) {
                        zzi = zzi();
                        this.zzdi = zzi;
                    }
                } finally {
                }
            }
        }
        return zzi != null ? zzi : Collections.emptyMap();
    }

    public final void zzh() {
        synchronized (this.zzdh) {
            this.zzdi = null;
        }
    }
}
