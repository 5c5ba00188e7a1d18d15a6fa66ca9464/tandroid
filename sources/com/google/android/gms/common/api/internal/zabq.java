package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zal;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/* loaded from: classes.dex */
public final class zabq implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, zau {
    final /* synthetic */ GoogleApiManager zaa;
    private final Api.Client zac;
    private final ApiKey zad;
    private final zaad zae;
    private final int zah;
    private final zact zai;
    private boolean zaj;
    private final Queue zab = new LinkedList();
    private final Set zaf = new HashSet();
    private final Map zag = new HashMap();
    private final List zak = new ArrayList();
    private ConnectionResult zal = null;
    private int zam = 0;

    public zabq(GoogleApiManager googleApiManager, GoogleApi googleApi) {
        Handler handler;
        Context context;
        Handler handler2;
        this.zaa = googleApiManager;
        handler = googleApiManager.zat;
        Api.Client zab = googleApi.zab(handler.getLooper(), this);
        this.zac = zab;
        this.zad = googleApi.getApiKey();
        this.zae = new zaad();
        this.zah = googleApi.zaa();
        if (!zab.requiresSignIn()) {
            this.zai = null;
            return;
        }
        context = googleApiManager.zak;
        handler2 = googleApiManager.zat;
        this.zai = googleApi.zac(context, handler2);
    }

    private final Feature zaB(Feature[] featureArr) {
        if (featureArr != null && featureArr.length != 0) {
            Feature[] availableFeatures = this.zac.getAvailableFeatures();
            if (availableFeatures == null) {
                availableFeatures = new Feature[0];
            }
            ArrayMap arrayMap = new ArrayMap(availableFeatures.length);
            for (Feature feature : availableFeatures) {
                arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
            }
            for (Feature feature2 : featureArr) {
                Long l = (Long) arrayMap.get(feature2.getName());
                if (l == null || l.longValue() < feature2.getVersion()) {
                    return feature2;
                }
            }
        }
        return null;
    }

    private final void zaC(ConnectionResult connectionResult) {
        Iterator it = this.zaf.iterator();
        if (!it.hasNext()) {
            this.zaf.clear();
            return;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
        if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
            this.zac.getEndpointPackageName();
        }
        throw null;
    }

    public final void zaD(Status status) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zaE(status, null, false);
    }

    private final void zaE(Status status, Exception exc, boolean z) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if ((status == null) == (exc == null)) {
            throw new IllegalArgumentException("Status XOR exception should be null");
        }
        Iterator it = this.zab.iterator();
        while (it.hasNext()) {
            zai zaiVar = (zai) it.next();
            if (!z || zaiVar.zac == 2) {
                if (status != null) {
                    zaiVar.zad(status);
                } else {
                    zaiVar.zae(exc);
                }
                it.remove();
            }
        }
    }

    private final void zaF() {
        ArrayList arrayList = new ArrayList(this.zab);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            zai zaiVar = (zai) arrayList.get(i);
            if (!this.zac.isConnected()) {
                return;
            }
            if (zaL(zaiVar)) {
                this.zab.remove(zaiVar);
            }
        }
    }

    public final void zaG() {
        zan();
        zaC(ConnectionResult.RESULT_SUCCESS);
        zaK();
        Iterator it = this.zag.values().iterator();
        while (it.hasNext()) {
            zaci zaciVar = (zaci) it.next();
            if (zaB(zaciVar.zaa.getRequiredFeatures()) == null) {
                try {
                    zaciVar.zaa.registerListener(this.zac, new TaskCompletionSource());
                } catch (DeadObjectException unused) {
                    onConnectionSuspended(3);
                    this.zac.disconnect("DeadObjectException thrown while calling register listener method.");
                } catch (RemoteException unused2) {
                }
            }
            it.remove();
        }
        zaF();
        zaI();
    }

    public final void zaH(int i) {
        Handler handler;
        Handler handler2;
        long j;
        Handler handler3;
        Handler handler4;
        long j2;
        zal zalVar;
        zan();
        this.zaj = true;
        this.zae.zae(i, this.zac.getLastDisconnectMessage());
        GoogleApiManager googleApiManager = this.zaa;
        handler = googleApiManager.zat;
        handler2 = googleApiManager.zat;
        Message obtain = Message.obtain(handler2, 9, this.zad);
        j = this.zaa.zae;
        handler.sendMessageDelayed(obtain, j);
        GoogleApiManager googleApiManager2 = this.zaa;
        handler3 = googleApiManager2.zat;
        handler4 = googleApiManager2.zat;
        Message obtain2 = Message.obtain(handler4, 11, this.zad);
        j2 = this.zaa.zaf;
        handler3.sendMessageDelayed(obtain2, j2);
        zalVar = this.zaa.zam;
        zalVar.zac();
        Iterator it = this.zag.values().iterator();
        while (it.hasNext()) {
            ((zaci) it.next()).zac.run();
        }
    }

    private final void zaI() {
        Handler handler;
        Handler handler2;
        Handler handler3;
        long j;
        handler = this.zaa.zat;
        handler.removeMessages(12, this.zad);
        GoogleApiManager googleApiManager = this.zaa;
        handler2 = googleApiManager.zat;
        handler3 = googleApiManager.zat;
        Message obtainMessage = handler3.obtainMessage(12, this.zad);
        j = this.zaa.zag;
        handler2.sendMessageDelayed(obtainMessage, j);
    }

    private final void zaJ(zai zaiVar) {
        zaiVar.zag(this.zae, zaz());
        try {
            zaiVar.zaf(this);
        } catch (DeadObjectException unused) {
            onConnectionSuspended(1);
            this.zac.disconnect("DeadObjectException thrown while running ApiCallRunner.");
        }
    }

    private final void zaK() {
        Handler handler;
        Handler handler2;
        if (this.zaj) {
            handler = this.zaa.zat;
            handler.removeMessages(11, this.zad);
            handler2 = this.zaa.zat;
            handler2.removeMessages(9, this.zad);
            this.zaj = false;
        }
    }

    private final boolean zaL(zai zaiVar) {
        boolean z;
        Handler handler;
        Handler handler2;
        long j;
        Handler handler3;
        Handler handler4;
        long j2;
        Handler handler5;
        Handler handler6;
        Handler handler7;
        long j3;
        if (!(zaiVar instanceof zac)) {
            zaJ(zaiVar);
            return true;
        }
        zac zacVar = (zac) zaiVar;
        Feature zaB = zaB(zacVar.zab(this));
        if (zaB == null) {
            zaJ(zaiVar);
            return true;
        }
        Log.w("GoogleApiManager", this.zac.getClass().getName() + " could not execute call because it requires feature (" + zaB.getName() + ", " + zaB.getVersion() + ").");
        z = this.zaa.zau;
        if (!z || !zacVar.zaa(this)) {
            zacVar.zae(new UnsupportedApiCallException(zaB));
            return true;
        }
        zabs zabsVar = new zabs(this.zad, zaB, null);
        int indexOf = this.zak.indexOf(zabsVar);
        if (indexOf >= 0) {
            zabs zabsVar2 = (zabs) this.zak.get(indexOf);
            handler5 = this.zaa.zat;
            handler5.removeMessages(15, zabsVar2);
            GoogleApiManager googleApiManager = this.zaa;
            handler6 = googleApiManager.zat;
            handler7 = googleApiManager.zat;
            Message obtain = Message.obtain(handler7, 15, zabsVar2);
            j3 = this.zaa.zae;
            handler6.sendMessageDelayed(obtain, j3);
            return false;
        }
        this.zak.add(zabsVar);
        GoogleApiManager googleApiManager2 = this.zaa;
        handler = googleApiManager2.zat;
        handler2 = googleApiManager2.zat;
        Message obtain2 = Message.obtain(handler2, 15, zabsVar);
        j = this.zaa.zae;
        handler.sendMessageDelayed(obtain2, j);
        GoogleApiManager googleApiManager3 = this.zaa;
        handler3 = googleApiManager3.zat;
        handler4 = googleApiManager3.zat;
        Message obtain3 = Message.obtain(handler4, 16, zabsVar);
        j2 = this.zaa.zaf;
        handler3.sendMessageDelayed(obtain3, j2);
        ConnectionResult connectionResult = new ConnectionResult(2, null);
        if (zaM(connectionResult)) {
            return false;
        }
        this.zaa.zaG(connectionResult, this.zah);
        return false;
    }

    private final boolean zaM(ConnectionResult connectionResult) {
        Object obj;
        zaae zaaeVar;
        Set set;
        zaae zaaeVar2;
        obj = GoogleApiManager.zac;
        synchronized (obj) {
            try {
                GoogleApiManager googleApiManager = this.zaa;
                zaaeVar = googleApiManager.zaq;
                if (zaaeVar != null) {
                    set = googleApiManager.zar;
                    if (set.contains(this.zad)) {
                        zaaeVar2 = this.zaa.zaq;
                        zaaeVar2.zah(connectionResult, this.zah);
                        return true;
                    }
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final boolean zaN(boolean z) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (!this.zac.isConnected() || this.zag.size() != 0) {
            return false;
        }
        if (!this.zae.zag()) {
            this.zac.disconnect("Timing out service connection.");
            return true;
        }
        if (z) {
            zaI();
        }
        return false;
    }

    public static /* bridge */ /* synthetic */ ApiKey zag(zabq zabqVar) {
        return zabqVar.zad;
    }

    public static /* bridge */ /* synthetic */ void zai(zabq zabqVar, Status status) {
        zabqVar.zaD(status);
    }

    public static /* bridge */ /* synthetic */ void zal(zabq zabqVar, zabs zabsVar) {
        if (zabqVar.zak.contains(zabsVar) && !zabqVar.zaj) {
            if (zabqVar.zac.isConnected()) {
                zabqVar.zaF();
            } else {
                zabqVar.zao();
            }
        }
    }

    public static /* bridge */ /* synthetic */ void zam(zabq zabqVar, zabs zabsVar) {
        Handler handler;
        Handler handler2;
        Feature feature;
        Feature[] zab;
        if (zabqVar.zak.remove(zabsVar)) {
            handler = zabqVar.zaa.zat;
            handler.removeMessages(15, zabsVar);
            handler2 = zabqVar.zaa.zat;
            handler2.removeMessages(16, zabsVar);
            feature = zabsVar.zab;
            ArrayList arrayList = new ArrayList(zabqVar.zab.size());
            for (zai zaiVar : zabqVar.zab) {
                if ((zaiVar instanceof zac) && (zab = ((zac) zaiVar).zab(zabqVar)) != null && ArrayUtils.contains(zab, feature)) {
                    arrayList.add(zaiVar);
                }
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zai zaiVar2 = (zai) arrayList.get(i);
                zabqVar.zab.remove(zaiVar2);
                zaiVar2.zae(new UnsupportedApiCallException(feature));
            }
        }
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.zaa.zat;
        if (myLooper == handler.getLooper()) {
            zaG();
        } else {
            handler2 = this.zaa.zat;
            handler2.post(new zabm(this));
        }
    }

    @Override // com.google.android.gms.common.api.internal.OnConnectionFailedListener
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        zar(connectionResult, null);
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.zaa.zat;
        if (myLooper == handler.getLooper()) {
            zaH(i);
        } else {
            handler2 = this.zaa.zat;
            handler2.post(new zabn(this, i));
        }
    }

    public final boolean zaA() {
        return zaN(true);
    }

    @Override // com.google.android.gms.common.api.internal.zau
    public final void zaa(ConnectionResult connectionResult, Api api, boolean z) {
        throw null;
    }

    public final int zab() {
        return this.zah;
    }

    public final int zac() {
        return this.zam;
    }

    public final Api.Client zaf() {
        return this.zac;
    }

    public final Map zah() {
        return this.zag;
    }

    public final void zan() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        this.zal = null;
    }

    public final void zao() {
        Handler handler;
        ConnectionResult connectionResult;
        zal zalVar;
        Context context;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zac.isConnected() || this.zac.isConnecting()) {
            return;
        }
        try {
            GoogleApiManager googleApiManager = this.zaa;
            zalVar = googleApiManager.zam;
            context = googleApiManager.zak;
            int zab = zalVar.zab(context, this.zac);
            if (zab != 0) {
                ConnectionResult connectionResult2 = new ConnectionResult(zab, null);
                Log.w("GoogleApiManager", "The service for " + this.zac.getClass().getName() + " is not available: " + connectionResult2.toString());
                zar(connectionResult2, null);
                return;
            }
            GoogleApiManager googleApiManager2 = this.zaa;
            Api.Client client = this.zac;
            zabu zabuVar = new zabu(googleApiManager2, client, this.zad);
            if (client.requiresSignIn()) {
                ((zact) Preconditions.checkNotNull(this.zai)).zae(zabuVar);
            }
            try {
                this.zac.connect(zabuVar);
            } catch (SecurityException e) {
                e = e;
                connectionResult = new ConnectionResult(10);
                zar(connectionResult, e);
            }
        } catch (IllegalStateException e2) {
            e = e2;
            connectionResult = new ConnectionResult(10);
        }
    }

    public final void zap(zai zaiVar) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zac.isConnected()) {
            if (zaL(zaiVar)) {
                zaI();
                return;
            } else {
                this.zab.add(zaiVar);
                return;
            }
        }
        this.zab.add(zaiVar);
        ConnectionResult connectionResult = this.zal;
        if (connectionResult == null || !connectionResult.hasResolution()) {
            zao();
        } else {
            zar(this.zal, null);
        }
    }

    public final void zaq() {
        this.zam++;
    }

    public final void zar(ConnectionResult connectionResult, Exception exc) {
        Handler handler;
        zal zalVar;
        boolean z;
        Status zaH;
        Status zaH2;
        Status zaH3;
        Handler handler2;
        Handler handler3;
        long j;
        Handler handler4;
        Status status;
        Handler handler5;
        Handler handler6;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zact zactVar = this.zai;
        if (zactVar != null) {
            zactVar.zaf();
        }
        zan();
        zalVar = this.zaa.zam;
        zalVar.zac();
        zaC(connectionResult);
        if ((this.zac instanceof com.google.android.gms.common.internal.service.zap) && connectionResult.getErrorCode() != 24) {
            this.zaa.zah = true;
            GoogleApiManager googleApiManager = this.zaa;
            handler5 = googleApiManager.zat;
            handler6 = googleApiManager.zat;
            handler5.sendMessageDelayed(handler6.obtainMessage(19), 300000L);
        }
        if (connectionResult.getErrorCode() == 4) {
            status = GoogleApiManager.zab;
            zaD(status);
            return;
        }
        if (this.zab.isEmpty()) {
            this.zal = connectionResult;
            return;
        }
        if (exc != null) {
            handler4 = this.zaa.zat;
            Preconditions.checkHandlerThread(handler4);
            zaE(null, exc, false);
            return;
        }
        z = this.zaa.zau;
        if (!z) {
            zaH = GoogleApiManager.zaH(this.zad, connectionResult);
            zaD(zaH);
            return;
        }
        zaH2 = GoogleApiManager.zaH(this.zad, connectionResult);
        zaE(zaH2, null, true);
        if (this.zab.isEmpty() || zaM(connectionResult) || this.zaa.zaG(connectionResult, this.zah)) {
            return;
        }
        if (connectionResult.getErrorCode() == 18) {
            this.zaj = true;
        }
        if (!this.zaj) {
            zaH3 = GoogleApiManager.zaH(this.zad, connectionResult);
            zaD(zaH3);
            return;
        }
        GoogleApiManager googleApiManager2 = this.zaa;
        handler2 = googleApiManager2.zat;
        handler3 = googleApiManager2.zat;
        Message obtain = Message.obtain(handler3, 9, this.zad);
        j = this.zaa.zae;
        handler2.sendMessageDelayed(obtain, j);
    }

    public final void zas(ConnectionResult connectionResult) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        Api.Client client = this.zac;
        client.disconnect("onSignInFailed for " + client.getClass().getName() + " with " + String.valueOf(connectionResult));
        zar(connectionResult, null);
    }

    public final void zau() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zaj) {
            zao();
        }
    }

    public final void zav() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zaD(GoogleApiManager.zaa);
        this.zae.zaf();
        for (ListenerHolder.ListenerKey listenerKey : (ListenerHolder.ListenerKey[]) this.zag.keySet().toArray(new ListenerHolder.ListenerKey[0])) {
            zap(new zah(listenerKey, new TaskCompletionSource()));
        }
        zaC(new ConnectionResult(4));
        if (this.zac.isConnected()) {
            this.zac.onUserSignOut(new zabp(this));
        }
    }

    public final void zaw() {
        Handler handler;
        GoogleApiAvailability googleApiAvailability;
        Context context;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zaj) {
            zaK();
            GoogleApiManager googleApiManager = this.zaa;
            googleApiAvailability = googleApiManager.zal;
            context = googleApiManager.zak;
            zaD(googleApiAvailability.isGooglePlayServicesAvailable(context) == 18 ? new Status(21, "Connection timed out waiting for Google Play services update to complete.") : new Status(22, "API failed to connect while resuming due to an unknown error."));
            this.zac.disconnect("Timing out connection while resuming.");
        }
    }

    public final boolean zaz() {
        return this.zac.requiresSignIn();
    }
}
