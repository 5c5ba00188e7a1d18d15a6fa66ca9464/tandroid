package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.ClientLibraryUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public final class zabe extends GoogleApiClient implements zabz {
    zabx zab;
    final Map zac;
    Set zad;
    final ClientSettings zae;
    final Map zaf;
    final Api.AbstractClientBuilder zag;
    Set zah;
    final zadc zai;
    private final Lock zaj;
    private final com.google.android.gms.common.internal.zak zak;
    private final int zam;
    private final Context zan;
    private final Looper zao;
    private volatile boolean zap;
    private long zaq;
    private long zar;
    private final zabc zas;
    private final GoogleApiAvailability zat;
    private final ListenerHolders zau;
    private final ArrayList zav;
    private Integer zaw;
    private final com.google.android.gms.common.internal.zaj zax;
    private zaca zal = null;
    final Queue zaa = new LinkedList();

    public zabe(Context context, Lock lock, Looper looper, ClientSettings clientSettings, GoogleApiAvailability googleApiAvailability, Api.AbstractClientBuilder abstractClientBuilder, Map map, List list, List list2, Map map2, int i, int i2, ArrayList arrayList) {
        this.zaq = true != ClientLibraryUtils.isPackageSide() ? 120000L : 10000L;
        this.zar = 5000L;
        this.zad = new HashSet();
        this.zau = new ListenerHolders();
        this.zaw = null;
        this.zah = null;
        zaay zaayVar = new zaay(this);
        this.zax = zaayVar;
        this.zan = context;
        this.zaj = lock;
        this.zak = new com.google.android.gms.common.internal.zak(looper, zaayVar);
        this.zao = looper;
        this.zas = new zabc(this, looper);
        this.zat = googleApiAvailability;
        this.zam = i;
        if (i >= 0) {
            this.zaw = Integer.valueOf(i2);
        }
        this.zaf = map;
        this.zac = map2;
        this.zav = arrayList;
        this.zai = new zadc();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.zak.zaf((GoogleApiClient.ConnectionCallbacks) it.next());
        }
        Iterator it2 = list2.iterator();
        while (it2.hasNext()) {
            this.zak.zag((GoogleApiClient.OnConnectionFailedListener) it2.next());
        }
        this.zae = clientSettings;
        this.zag = abstractClientBuilder;
    }

    public static int zad(Iterable iterable, boolean z) {
        Iterator it = iterable.iterator();
        boolean z2 = false;
        boolean z3 = false;
        while (it.hasNext()) {
            Api.Client client = (Api.Client) it.next();
            z2 |= client.requiresSignIn();
            z3 |= client.providesSignIn();
        }
        if (z2) {
            return (z3 && z) ? 2 : 1;
        }
        return 3;
    }

    static String zag(int i) {
        return i != 1 ? i != 2 ? i != 3 ? "UNKNOWN" : "SIGN_IN_MODE_NONE" : "SIGN_IN_MODE_OPTIONAL" : "SIGN_IN_MODE_REQUIRED";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void zai(zabe zabeVar) {
        zabeVar.zaj.lock();
        try {
            if (zabeVar.zap) {
                zabeVar.zan();
            }
        } finally {
            zabeVar.zaj.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ void zaj(zabe zabeVar) {
        zabeVar.zaj.lock();
        try {
            if (zabeVar.zak()) {
                zabeVar.zan();
            }
        } finally {
            zabeVar.zaj.unlock();
        }
    }

    private final void zal(int i) {
        Integer num = this.zaw;
        if (num == null) {
            this.zaw = Integer.valueOf(i);
        } else if (num.intValue() != i) {
            int intValue = this.zaw.intValue();
            throw new IllegalStateException("Cannot use sign-in mode: " + zag(i) + ". Mode was already set to " + zag(intValue));
        }
        if (this.zal != null) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        for (Api.Client client : this.zac.values()) {
            z |= client.requiresSignIn();
            z2 |= client.providesSignIn();
        }
        int intValue2 = this.zaw.intValue();
        if (intValue2 != 1) {
            if (intValue2 == 2 && z) {
                this.zal = zaaa.zag(this.zan, this, this.zaj, this.zao, this.zat, this.zac, this.zae, this.zaf, this.zag, this.zav);
                return;
            }
        } else if (!z) {
            throw new IllegalStateException("SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.");
        } else {
            if (z2) {
                throw new IllegalStateException("Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
        }
        this.zal = new zabi(this.zan, this, this.zaj, this.zao, this.zat, this.zac, this.zae, this.zaf, this.zag, this.zav, this);
    }

    @GuardedBy("mLock")
    private final void zan() {
        this.zak.zab();
        ((zaca) Preconditions.checkNotNull(this.zal)).zaq();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void connect() {
        this.zaj.lock();
        try {
            int i = 2;
            boolean z = false;
            if (this.zam >= 0) {
                Preconditions.checkState(this.zaw != null, "Sign-in mode should have been set explicitly by auto-manage.");
            } else {
                Integer num = this.zaw;
                if (num != null) {
                    if (num.intValue() == 2) {
                        throw new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
                    }
                } else {
                    this.zaw = Integer.valueOf(zad(this.zac.values(), false));
                }
            }
            int intValue = ((Integer) Preconditions.checkNotNull(this.zaw)).intValue();
            this.zaj.lock();
            if (intValue == 3 || intValue == 1) {
                i = intValue;
            } else if (intValue != 2) {
                i = intValue;
                Preconditions.checkArgument(z, "Illegal sign-in mode: " + i);
                zal(i);
                zan();
                this.zaj.unlock();
            }
            z = true;
            Preconditions.checkArgument(z, "Illegal sign-in mode: " + i);
            zal(i);
            zan();
            this.zaj.unlock();
        } finally {
            this.zaj.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void disconnect() {
        Lock lock;
        this.zaj.lock();
        try {
            this.zai.zab();
            zaca zacaVar = this.zal;
            if (zacaVar != null) {
                zacaVar.zar();
            }
            this.zau.zab();
            for (BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl : this.zaa) {
                baseImplementation$ApiMethodImpl.zan(null);
                baseImplementation$ApiMethodImpl.cancel();
            }
            this.zaa.clear();
            if (this.zal == null) {
                lock = this.zaj;
            } else {
                zak();
                this.zak.zaa();
                lock = this.zaj;
            }
            lock.unlock();
        } catch (Throwable th) {
            this.zaj.unlock();
            throw th;
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append((CharSequence) str).append("mContext=").println(this.zan);
        printWriter.append((CharSequence) str).append("mResuming=").print(this.zap);
        printWriter.append(" mWorkQueue.size()=").print(this.zaa.size());
        printWriter.append(" mUnconsumedApiCalls.size()=").println(this.zai.zab.size());
        zaca zacaVar = this.zal;
        if (zacaVar != null) {
            zacaVar.zas(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final <A extends Api.AnyClient, T extends BaseImplementation$ApiMethodImpl<? extends Result, A>> T execute(T t) {
        Lock lock;
        Api<?> api = t.getApi();
        boolean containsKey = this.zac.containsKey(t.getClientKey());
        String zad = api != null ? api.zad() : "the API";
        Preconditions.checkArgument(containsKey, "GoogleApiClient is not configured to use " + zad + " required for this call.");
        this.zaj.lock();
        try {
            zaca zacaVar = this.zal;
            if (zacaVar != null) {
                if (this.zap) {
                    this.zaa.add(t);
                    while (!this.zaa.isEmpty()) {
                        BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl = (BaseImplementation$ApiMethodImpl) this.zaa.remove();
                        this.zai.zaa(baseImplementation$ApiMethodImpl);
                        baseImplementation$ApiMethodImpl.setFailedResult(Status.RESULT_INTERNAL_ERROR);
                    }
                    lock = this.zaj;
                } else {
                    t = (T) zacaVar.zaf(t);
                    lock = this.zaj;
                }
                lock.unlock();
                return t;
            }
            throw new IllegalStateException("GoogleApiClient is not connected yet.");
        } catch (Throwable th) {
            this.zaj.unlock();
            throw th;
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Looper getLooper() {
        return this.zao;
    }

    public final boolean isConnected() {
        zaca zacaVar = this.zal;
        return zacaVar != null && zacaVar.zaw();
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        zaca zacaVar = this.zal;
        return zacaVar != null && zacaVar.zay(signInConnectionListener);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void maybeSignOut() {
        zaca zacaVar = this.zal;
        if (zacaVar != null) {
            zacaVar.zau();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void registerConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zak.zag(onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final void unregisterConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zak.zai(onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.api.internal.zabz
    @GuardedBy("mLock")
    public final void zaa(ConnectionResult connectionResult) {
        if (!this.zat.isPlayServicesPossiblyUpdating(this.zan, connectionResult.getErrorCode())) {
            zak();
        }
        if (this.zap) {
            return;
        }
        this.zak.zac(connectionResult);
        this.zak.zaa();
    }

    @Override // com.google.android.gms.common.api.internal.zabz
    @GuardedBy("mLock")
    public final void zab(Bundle bundle) {
        while (!this.zaa.isEmpty()) {
            execute((BaseImplementation$ApiMethodImpl) this.zaa.remove());
        }
        this.zak.zad(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zaf() {
        StringWriter stringWriter = new StringWriter();
        dump("", null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy("mLock")
    public final boolean zak() {
        if (this.zap) {
            this.zap = false;
            this.zas.removeMessages(2);
            this.zas.removeMessages(1);
            zabx zabxVar = this.zab;
            if (zabxVar != null) {
                zabxVar.zab();
                this.zab = null;
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0041, code lost:
        if (r3 == false) goto L17;
     */
    @Override // com.google.android.gms.common.api.GoogleApiClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zap(zada zadaVar) {
        this.zaj.lock();
        try {
            Set set = this.zah;
            if (set != null) {
                if (set.remove(zadaVar)) {
                    this.zaj.lock();
                    Set set2 = this.zah;
                    if (set2 != null) {
                        boolean z = !set2.isEmpty();
                        this.zaj.unlock();
                    } else {
                        this.zaj.unlock();
                    }
                    zaca zacaVar = this.zal;
                    if (zacaVar != null) {
                        zacaVar.zat();
                    }
                } else {
                    Log.wtf("GoogleApiClientImpl", "Failed to remove pending transform - this may lead to memory leaks!", new Exception());
                }
            } else {
                Log.wtf("GoogleApiClientImpl", "Attempted to remove pending transform when no transforms are registered.", new Exception());
            }
        } finally {
            this.zaj.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabz
    @GuardedBy("mLock")
    public final void zac(int i, boolean z) {
        if (i == 1) {
            if (!z && !this.zap) {
                this.zap = true;
                if (this.zab == null && !ClientLibraryUtils.isPackageSide()) {
                    try {
                        this.zab = this.zat.zac(this.zan.getApplicationContext(), new zabd(this));
                    } catch (SecurityException unused) {
                    }
                }
                zabc zabcVar = this.zas;
                zabcVar.sendMessageDelayed(zabcVar.obtainMessage(1), this.zaq);
                zabc zabcVar2 = this.zas;
                zabcVar2.sendMessageDelayed(zabcVar2.obtainMessage(2), this.zar);
            }
            i = 1;
        }
        for (BasePendingResult basePendingResult : (BasePendingResult[]) this.zai.zab.toArray(new BasePendingResult[0])) {
            basePendingResult.forceFailureUnlessReady(zadc.zaa);
        }
        this.zak.zae(i);
        this.zak.zaa();
        if (i == 2) {
            zan();
        }
    }
}
