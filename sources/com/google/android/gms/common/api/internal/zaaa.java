package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zaaa implements zaca {
    private final Context zaa;
    private final zabe zab;
    private final Looper zac;
    private final zabi zad;
    private final zabi zae;
    private final Map zaf;
    private final Api.Client zah;
    private Bundle zai;
    private final Lock zam;
    private final Set zag = Collections.newSetFromMap(new WeakHashMap());
    private ConnectionResult zaj = null;
    private ConnectionResult zak = null;
    private boolean zal = false;
    private int zan = 0;

    private zaaa(Context context, zabe zabeVar, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map map, Map map2, ClientSettings clientSettings, Api.AbstractClientBuilder abstractClientBuilder, Api.Client client, ArrayList arrayList, ArrayList arrayList2, Map map3, Map map4) {
        this.zaa = context;
        this.zab = zabeVar;
        this.zam = lock;
        this.zac = looper;
        this.zah = client;
        this.zad = new zabi(context, zabeVar, lock, looper, googleApiAvailabilityLight, map2, null, map4, null, arrayList2, new zax(this, null));
        this.zae = new zabi(context, zabeVar, lock, looper, googleApiAvailabilityLight, map, clientSettings, map3, abstractClientBuilder, arrayList, new zaz(this, null));
        ArrayMap arrayMap = new ArrayMap();
        Iterator it = map2.keySet().iterator();
        while (it.hasNext()) {
            arrayMap.put((Api.AnyClientKey) it.next(), this.zad);
        }
        Iterator it2 = map.keySet().iterator();
        while (it2.hasNext()) {
            arrayMap.put((Api.AnyClientKey) it2.next(), this.zae);
        }
        this.zaf = Collections.unmodifiableMap(arrayMap);
    }

    private final void zaA(ConnectionResult connectionResult) {
        int i = this.zan;
        if (i != 1) {
            if (i != 2) {
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                this.zan = 0;
            }
            this.zab.zaa(connectionResult);
        }
        zaB();
        this.zan = 0;
    }

    private final void zaB() {
        Iterator it = this.zag.iterator();
        while (it.hasNext()) {
            ((SignInConnectionListener) it.next()).onComplete();
        }
        this.zag.clear();
    }

    private final boolean zaC() {
        ConnectionResult connectionResult = this.zak;
        return connectionResult != null && connectionResult.getErrorCode() == 4;
    }

    private final boolean zaD(BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        zabi zabiVar = (zabi) this.zaf.get(baseImplementation$ApiMethodImpl.getClientKey());
        Preconditions.checkNotNull(zabiVar, "GoogleApiClient is not configured to use the API required for this call.");
        return zabiVar.equals(this.zae);
    }

    private static boolean zaE(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    public static zaaa zag(Context context, zabe zabeVar, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map map, ClientSettings clientSettings, Map map2, Api.AbstractClientBuilder abstractClientBuilder, ArrayList arrayList) {
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        Api.Client client = null;
        for (Map.Entry entry : map.entrySet()) {
            Api.Client client2 = (Api.Client) entry.getValue();
            if (true == client2.providesSignIn()) {
                client = client2;
            }
            boolean requiresSignIn = client2.requiresSignIn();
            Api.AnyClientKey anyClientKey = (Api.AnyClientKey) entry.getKey();
            if (requiresSignIn) {
                arrayMap.put(anyClientKey, client2);
            } else {
                arrayMap2.put(anyClientKey, client2);
            }
        }
        Preconditions.checkState(!arrayMap.isEmpty(), "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        for (Api api : map2.keySet()) {
            Api.AnyClientKey zab = api.zab();
            if (arrayMap.containsKey(zab)) {
                arrayMap3.put(api, (Boolean) map2.get(api));
            } else {
                if (!arrayMap2.containsKey(zab)) {
                    throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
                }
                arrayMap4.put(api, (Boolean) map2.get(api));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            zat zatVar = (zat) arrayList.get(i);
            if (arrayMap3.containsKey(zatVar.zaa)) {
                arrayList2.add(zatVar);
            } else {
                if (!arrayMap4.containsKey(zatVar.zaa)) {
                    throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
                }
                arrayList3.add(zatVar);
            }
        }
        return new zaaa(context, zabeVar, lock, looper, googleApiAvailabilityLight, arrayMap, arrayMap2, clientSettings, abstractClientBuilder, client, arrayList2, arrayList3, arrayMap3, arrayMap4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void zan(zaaa zaaaVar, int i, boolean z) {
        zaaaVar.zab.zac(i, z);
        zaaaVar.zak = null;
        zaaaVar.zaj = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void zao(zaaa zaaaVar, Bundle bundle) {
        Bundle bundle2 = zaaaVar.zai;
        if (bundle2 == null) {
            zaaaVar.zai = bundle;
        } else if (bundle != null) {
            bundle2.putAll(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void zap(zaaa zaaaVar) {
        ConnectionResult connectionResult;
        if (!zaE(zaaaVar.zaj)) {
            if (zaaaVar.zaj != null && zaE(zaaaVar.zak)) {
                zaaaVar.zae.zar();
                zaaaVar.zaA((ConnectionResult) Preconditions.checkNotNull(zaaaVar.zaj));
                return;
            }
            ConnectionResult connectionResult2 = zaaaVar.zaj;
            if (connectionResult2 == null || (connectionResult = zaaaVar.zak) == null) {
                return;
            }
            if (zaaaVar.zae.zaf < zaaaVar.zad.zaf) {
                connectionResult2 = connectionResult;
            }
            zaaaVar.zaA(connectionResult2);
            return;
        }
        if (!zaE(zaaaVar.zak) && !zaaaVar.zaC()) {
            ConnectionResult connectionResult3 = zaaaVar.zak;
            if (connectionResult3 != null) {
                if (zaaaVar.zan == 1) {
                    zaaaVar.zaB();
                    return;
                } else {
                    zaaaVar.zaA(connectionResult3);
                    zaaaVar.zad.zar();
                    return;
                }
            }
            return;
        }
        int i = zaaaVar.zan;
        if (i != 1) {
            if (i != 2) {
                Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                zaaaVar.zan = 0;
            }
            ((zabe) Preconditions.checkNotNull(zaaaVar.zab)).zab(zaaaVar.zai);
        }
        zaaaVar.zaB();
        zaaaVar.zan = 0;
    }

    private final PendingIntent zaz() {
        if (this.zah == null) {
            return null;
        }
        return PendingIntent.getActivity(this.zaa, System.identityHashCode(this.zab), this.zah.getSignInIntent(), com.google.android.gms.internal.base.zap.zaa | 134217728);
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final BaseImplementation$ApiMethodImpl zae(BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        if (!zaD(baseImplementation$ApiMethodImpl)) {
            this.zad.zae(baseImplementation$ApiMethodImpl);
            return baseImplementation$ApiMethodImpl;
        }
        if (zaC()) {
            baseImplementation$ApiMethodImpl.setFailedResult(new Status(4, (String) null, zaz()));
            return baseImplementation$ApiMethodImpl;
        }
        this.zae.zae(baseImplementation$ApiMethodImpl);
        return baseImplementation$ApiMethodImpl;
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final BaseImplementation$ApiMethodImpl zaf(BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        if (!zaD(baseImplementation$ApiMethodImpl)) {
            return this.zad.zaf(baseImplementation$ApiMethodImpl);
        }
        if (!zaC()) {
            return this.zae.zaf(baseImplementation$ApiMethodImpl);
        }
        baseImplementation$ApiMethodImpl.setFailedResult(new Status(4, (String) null, zaz()));
        return baseImplementation$ApiMethodImpl;
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final void zaq() {
        this.zan = 2;
        this.zal = false;
        this.zak = null;
        this.zaj = null;
        this.zad.zaq();
        this.zae.zaq();
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final void zar() {
        this.zak = null;
        this.zaj = null;
        this.zan = 0;
        this.zad.zar();
        this.zae.zar();
        zaB();
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final void zas(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append((CharSequence) str).append("authClient").println(":");
        this.zae.zas(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append((CharSequence) str).append("anonClient").println(":");
        this.zad.zas(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final void zau() {
        this.zam.lock();
        try {
            boolean zax = zax();
            this.zae.zar();
            this.zak = new ConnectionResult(4);
            if (zax) {
                new com.google.android.gms.internal.base.zau(this.zac).post(new zav(this));
            } else {
                zaB();
            }
            this.zam.unlock();
        } catch (Throwable th) {
            this.zam.unlock();
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
    
        if (r3.zan == 1) goto L11;
     */
    @Override // com.google.android.gms.common.api.internal.zaca
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zaw() {
        this.zam.lock();
        try {
            boolean z = false;
            if (this.zad.zaw()) {
                if (!this.zae.zaw() && !zaC()) {
                }
                z = true;
            }
            return z;
        } finally {
            this.zam.unlock();
        }
    }

    public final boolean zax() {
        this.zam.lock();
        try {
            return this.zan == 2;
        } finally {
            this.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zaca
    public final boolean zay(SignInConnectionListener signInConnectionListener) {
        this.zam.lock();
        try {
            if (!zax()) {
                if (zaw()) {
                }
                this.zam.unlock();
                return false;
            }
            if (!this.zae.zaw()) {
                this.zag.add(signInConnectionListener);
                if (this.zan == 0) {
                    this.zan = 1;
                }
                this.zak = null;
                this.zae.zaq();
                this.zam.unlock();
                return true;
            }
            this.zam.unlock();
            return false;
        } catch (Throwable th) {
            this.zam.unlock();
            throw th;
        }
    }
}
