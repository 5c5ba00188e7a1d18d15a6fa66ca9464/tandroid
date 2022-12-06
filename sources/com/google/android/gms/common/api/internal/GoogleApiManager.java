package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.collection.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.TelemetryData;
import com.google.android.gms.common.internal.TelemetryLogging;
import com.google.android.gms.common.internal.TelemetryLoggingClient;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public class GoogleApiManager implements Handler.Callback {
    public static final Status zaa = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zab = new Status(4, "The user must be signed in to make this API call.");
    private static final Object zac = new Object();
    @GuardedBy("lock")
    private static GoogleApiManager zad;
    private TelemetryData zai;
    private TelemetryLoggingClient zaj;
    private final Context zak;
    private final GoogleApiAvailability zal;
    private final com.google.android.gms.common.internal.zal zam;
    @NotOnlyInitialized
    private final Handler zat;
    private volatile boolean zau;
    private long zae = 5000;
    private long zaf = 120000;
    private long zag = 10000;
    private boolean zah = false;
    private final AtomicInteger zan = new AtomicInteger(1);
    private final AtomicInteger zao = new AtomicInteger(0);
    private final Map zap = new ConcurrentHashMap(5, 0.75f, 1);
    @GuardedBy("lock")
    private zaae zaq = null;
    @GuardedBy("lock")
    private final Set zar = new ArraySet();
    private final Set zas = new ArraySet();

    private GoogleApiManager(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zau = true;
        this.zak = context;
        com.google.android.gms.internal.base.zau zauVar = new com.google.android.gms.internal.base.zau(looper, this);
        this.zat = zauVar;
        this.zal = googleApiAvailability;
        this.zam = new com.google.android.gms.common.internal.zal(googleApiAvailability);
        if (DeviceProperties.isAuto(context)) {
            this.zau = false;
        }
        zauVar.sendMessage(zauVar.obtainMessage(6));
    }

    public static void reportSignOut() {
        synchronized (zac) {
            GoogleApiManager googleApiManager = zad;
            if (googleApiManager != null) {
                googleApiManager.zao.incrementAndGet();
                Handler handler = googleApiManager.zat;
                handler.sendMessageAtFrontOfQueue(handler.obtainMessage(10));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Status zaH(ApiKey apiKey, ConnectionResult connectionResult) {
        String zaa2 = apiKey.zaa();
        String valueOf = String.valueOf(connectionResult);
        return new Status(connectionResult, "API: " + zaa2 + " is not available on this device. Connection failed with: " + valueOf);
    }

    private final zabq zaI(GoogleApi googleApi) {
        ApiKey apiKey = googleApi.getApiKey();
        zabq zabqVar = (zabq) this.zap.get(apiKey);
        if (zabqVar == null) {
            zabqVar = new zabq(this, googleApi);
            this.zap.put(apiKey, zabqVar);
        }
        if (zabqVar.zaz()) {
            this.zas.add(apiKey);
        }
        zabqVar.zao();
        return zabqVar;
    }

    private final TelemetryLoggingClient zaJ() {
        if (this.zaj == null) {
            this.zaj = TelemetryLogging.getClient(this.zak);
        }
        return this.zaj;
    }

    private final void zaK() {
        TelemetryData telemetryData = this.zai;
        if (telemetryData != null) {
            if (telemetryData.zaa() > 0 || zaF()) {
                zaJ().log(telemetryData);
            }
            this.zai = null;
        }
    }

    private final void zaL(TaskCompletionSource taskCompletionSource, int i, GoogleApi googleApi) {
        zacd zaa2;
        if (i == 0 || (zaa2 = zacd.zaa(this, i, googleApi.getApiKey())) == null) {
            return;
        }
        Task task = taskCompletionSource.getTask();
        final Handler handler = this.zat;
        handler.getClass();
        task.addOnCompleteListener(new Executor() { // from class: com.google.android.gms.common.api.internal.zabk
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, zaa2);
    }

    public static GoogleApiManager zam(Context context) {
        GoogleApiManager googleApiManager;
        synchronized (zac) {
            if (zad == null) {
                zad = new GoogleApiManager(context.getApplicationContext(), GmsClientSupervisor.getOrStartHandlerThread().getLooper(), GoogleApiAvailability.getInstance());
            }
            googleApiManager = zad;
        }
        return googleApiManager;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        ApiKey apiKey;
        ApiKey apiKey2;
        ApiKey apiKey3;
        ApiKey apiKey4;
        int i = message.what;
        long j = 300000;
        zabq zabqVar = null;
        switch (i) {
            case 1:
                if (true == ((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zag = j;
                this.zat.removeMessages(12);
                for (ApiKey apiKey5 : this.zap.keySet()) {
                    Handler handler = this.zat;
                    handler.sendMessageDelayed(handler.obtainMessage(12, apiKey5), this.zag);
                }
                break;
            case 2:
                zal zalVar = (zal) message.obj;
                Iterator it = zalVar.zab().iterator();
                while (true) {
                    if (it.hasNext()) {
                        ApiKey apiKey6 = (ApiKey) it.next();
                        zabq zabqVar2 = (zabq) this.zap.get(apiKey6);
                        if (zabqVar2 != null) {
                            if (zabqVar2.zay()) {
                                zalVar.zac(apiKey6, ConnectionResult.RESULT_SUCCESS, zabqVar2.zaf().getEndpointPackageName());
                            } else {
                                ConnectionResult zad2 = zabqVar2.zad();
                                if (zad2 != null) {
                                    zalVar.zac(apiKey6, zad2, null);
                                } else {
                                    zabqVar2.zat(zalVar);
                                    zabqVar2.zao();
                                }
                            }
                        } else {
                            zalVar.zac(apiKey6, new ConnectionResult(13), null);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            case 3:
                for (zabq zabqVar3 : this.zap.values()) {
                    zabqVar3.zan();
                    zabqVar3.zao();
                }
                break;
            case 4:
            case 8:
            case 13:
                zach zachVar = (zach) message.obj;
                zabq zabqVar4 = (zabq) this.zap.get(zachVar.zac.getApiKey());
                if (zabqVar4 == null) {
                    zabqVar4 = zaI(zachVar.zac);
                }
                if (!zabqVar4.zaz() || this.zao.get() == zachVar.zab) {
                    zabqVar4.zap(zachVar.zaa);
                    break;
                } else {
                    zachVar.zaa.zad(zaa);
                    zabqVar4.zav();
                    break;
                }
            case 5:
                int i2 = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator it2 = this.zap.values().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        zabq zabqVar5 = (zabq) it2.next();
                        if (zabqVar5.zab() == i2) {
                            zabqVar = zabqVar5;
                        }
                    }
                }
                if (zabqVar == null) {
                    Log.wtf("GoogleApiManager", "Could not find API instance " + i2 + " while trying to fail enqueued calls.", new Exception());
                    break;
                } else if (connectionResult.getErrorCode() == 13) {
                    String errorString = this.zal.getErrorString(connectionResult.getErrorCode());
                    String errorMessage = connectionResult.getErrorMessage();
                    zabq.zai(zabqVar, new Status(17, "Error resolution was canceled by the user, original error message: " + errorString + ": " + errorMessage));
                    break;
                } else {
                    zabq.zai(zabqVar, zaH(zabq.zag(zabqVar), connectionResult));
                    break;
                }
            case 6:
                if (this.zak.getApplicationContext() instanceof Application) {
                    BackgroundDetector.initialize((Application) this.zak.getApplicationContext());
                    BackgroundDetector.getInstance().addListener(new zabl(this));
                    if (!BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) {
                        this.zag = 300000L;
                        break;
                    }
                }
                break;
            case 7:
                zaI((GoogleApi) message.obj);
                break;
            case 9:
                if (this.zap.containsKey(message.obj)) {
                    ((zabq) this.zap.get(message.obj)).zau();
                    break;
                }
                break;
            case 10:
                for (ApiKey apiKey7 : this.zas) {
                    zabq zabqVar6 = (zabq) this.zap.remove(apiKey7);
                    if (zabqVar6 != null) {
                        zabqVar6.zav();
                    }
                }
                this.zas.clear();
                break;
            case 11:
                if (this.zap.containsKey(message.obj)) {
                    ((zabq) this.zap.get(message.obj)).zaw();
                    break;
                }
                break;
            case 12:
                if (this.zap.containsKey(message.obj)) {
                    ((zabq) this.zap.get(message.obj)).zaA();
                    break;
                }
                break;
            case 14:
                zaaf zaafVar = (zaaf) message.obj;
                ApiKey zaa2 = zaafVar.zaa();
                if (!this.zap.containsKey(zaa2)) {
                    zaafVar.zab().setResult(Boolean.FALSE);
                    break;
                } else {
                    zaafVar.zab().setResult(Boolean.valueOf(zabq.zax((zabq) this.zap.get(zaa2), false)));
                    break;
                }
            case 15:
                zabs zabsVar = (zabs) message.obj;
                Map map = this.zap;
                apiKey = zabsVar.zaa;
                if (map.containsKey(apiKey)) {
                    Map map2 = this.zap;
                    apiKey2 = zabsVar.zaa;
                    zabq.zal((zabq) map2.get(apiKey2), zabsVar);
                    break;
                }
                break;
            case 16:
                zabs zabsVar2 = (zabs) message.obj;
                Map map3 = this.zap;
                apiKey3 = zabsVar2.zaa;
                if (map3.containsKey(apiKey3)) {
                    Map map4 = this.zap;
                    apiKey4 = zabsVar2.zaa;
                    zabq.zam((zabq) map4.get(apiKey4), zabsVar2);
                    break;
                }
                break;
            case 17:
                zaK();
                break;
            case 18:
                zace zaceVar = (zace) message.obj;
                if (zaceVar.zac == 0) {
                    zaJ().log(new TelemetryData(zaceVar.zab, Arrays.asList(zaceVar.zaa)));
                    break;
                } else {
                    TelemetryData telemetryData = this.zai;
                    if (telemetryData != null) {
                        List zab2 = telemetryData.zab();
                        if (telemetryData.zaa() == zaceVar.zab && (zab2 == null || zab2.size() < zaceVar.zad)) {
                            this.zai.zac(zaceVar.zaa);
                        } else {
                            this.zat.removeMessages(17);
                            zaK();
                        }
                    }
                    if (this.zai == null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(zaceVar.zaa);
                        this.zai = new TelemetryData(zaceVar.zab, arrayList);
                        Handler handler2 = this.zat;
                        handler2.sendMessageDelayed(handler2.obtainMessage(17), zaceVar.zac);
                        break;
                    }
                }
                break;
            case 19:
                this.zah = false;
                break;
            default:
                Log.w("GoogleApiManager", "Unknown message id: " + i);
                return false;
        }
        return true;
    }

    public final void zaA() {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(3));
    }

    public final void zaB(GoogleApi googleApi) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(7, googleApi));
    }

    public final void zaC(zaae zaaeVar) {
        synchronized (zac) {
            if (this.zaq != zaaeVar) {
                this.zaq = zaaeVar;
                this.zar.clear();
            }
            this.zar.addAll(zaaeVar.zaa());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zaD(zaae zaaeVar) {
        synchronized (zac) {
            if (this.zaq == zaaeVar) {
                this.zaq = null;
                this.zar.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zaF() {
        if (this.zah) {
            return false;
        }
        RootTelemetryConfiguration config = RootTelemetryConfigManager.getInstance().getConfig();
        if (config != null && !config.getMethodInvocationTelemetryEnabled()) {
            return false;
        }
        int zaa2 = this.zam.zaa(this.zak, 203400000);
        return zaa2 == -1 || zaa2 == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zaG(ConnectionResult connectionResult, int i) {
        return this.zal.zah(this.zak, connectionResult, i);
    }

    public final int zaa() {
        return this.zan.getAndIncrement();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zabq zak(ApiKey apiKey) {
        return (zabq) this.zap.get(apiKey);
    }

    public final Task zaq(GoogleApi googleApi, RegisterListenerMethod registerListenerMethod, UnregisterListenerMethod unregisterListenerMethod, Runnable runnable) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zaL(taskCompletionSource, registerListenerMethod.zaa(), googleApi);
        zaf zafVar = new zaf(new zaci(registerListenerMethod, unregisterListenerMethod, runnable), taskCompletionSource);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(8, new zach(zafVar, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final Task zar(GoogleApi googleApi, ListenerHolder.ListenerKey listenerKey, int i) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zaL(taskCompletionSource, i, googleApi);
        zah zahVar = new zah(listenerKey, taskCompletionSource);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(13, new zach(zahVar, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final void zaw(GoogleApi googleApi, int i, BaseImplementation$ApiMethodImpl baseImplementation$ApiMethodImpl) {
        zae zaeVar = new zae(i, baseImplementation$ApiMethodImpl);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, new zach(zaeVar, this.zao.get(), googleApi)));
    }

    public final void zax(GoogleApi googleApi, int i, TaskApiCall taskApiCall, TaskCompletionSource taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        zaL(taskCompletionSource, taskApiCall.zaa(), googleApi);
        zag zagVar = new zag(i, taskApiCall, taskCompletionSource, statusExceptionMapper);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, new zach(zagVar, this.zao.get(), googleApi)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zay(MethodInvocation methodInvocation, int i, long j, int i2) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(18, new zace(methodInvocation, i, j, i2)));
    }

    public final void zaz(ConnectionResult connectionResult, int i) {
        if (!zaG(connectionResult, i)) {
            Handler handler = this.zat;
            handler.sendMessage(handler.obtainMessage(5, i, 0, connectionResult));
        }
    }
}
