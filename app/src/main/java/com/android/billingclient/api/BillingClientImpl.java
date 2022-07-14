package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import androidx.core.app.BundleCompat;
import com.android.billingclient.BuildConfig;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes3.dex */
public class BillingClientImpl extends BillingClient {
    private volatile int zza;
    private final String zzb;
    private final Handler zzc;
    private volatile zzo zzd;
    private Context zze;
    private volatile com.google.android.gms.internal.play_billing.zze zzf;
    private volatile zzap zzg;
    private boolean zzh;
    private boolean zzi;
    private int zzj;
    private boolean zzk;
    private boolean zzl;
    private boolean zzm;
    private boolean zzn;
    private boolean zzo;
    private boolean zzp;
    private boolean zzq;
    private boolean zzr;
    private boolean zzs;
    private boolean zzt;
    private boolean zzu;
    private ExecutorService zzv;

    private BillingClientImpl(Activity activity, boolean enablePendingPurchases, String versionOverride) {
        this(activity.getApplicationContext(), enablePendingPurchases, new zzat(), versionOverride, null, null);
    }

    private void initialize(Context context, PurchasesUpdatedListener purchasesUpdatedListener, boolean enablePendingPurchases, zzc alternativeBillingListener) {
        Context context2 = context.getApplicationContext();
        this.zze = context2;
        this.zzd = new zzo(context2, purchasesUpdatedListener, alternativeBillingListener);
        this.zzt = enablePendingPurchases;
        this.zzu = alternativeBillingListener != null;
    }

    private int launchBillingFlowCpp(Activity activity, BillingFlowParams params) {
        return launchBillingFlow(activity, params).getResponseCode();
    }

    private void launchPriceChangeConfirmationFlow(Activity activity, PriceChangeFlowParams priceChangeFlowParams, long futureHandle) {
        launchPriceChangeConfirmationFlow(activity, priceChangeFlowParams, new zzat(futureHandle));
    }

    private void startConnection(long futureHandle) {
        zzat zzatVar = new zzat(futureHandle);
        if (isReady()) {
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Service connection is valid. No need to re-initialize.");
            zzatVar.onBillingSetupFinished(zzbb.zzl);
        } else if (this.zza == 1) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Client is already in the process of connecting to billing service.");
            zzatVar.onBillingSetupFinished(zzbb.zzd);
        } else if (this.zza == 3) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Client was already closed and can't be reused. Please create another instance.");
            zzatVar.onBillingSetupFinished(zzbb.zzm);
        } else {
            this.zza = 1;
            this.zzd.zze();
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Starting in-app billing setup.");
            this.zzg = new zzap(this, zzatVar, null);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.zze.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
                ResolveInfo resolveInfo = queryIntentServices.get(0);
                if (resolveInfo.serviceInfo != null) {
                    String str = resolveInfo.serviceInfo.packageName;
                    String str2 = resolveInfo.serviceInfo.name;
                    if (!"com.android.vending".equals(str) || str2 == null) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "The device doesn't have valid Play Store.");
                    } else {
                        ComponentName componentName = new ComponentName(str, str2);
                        Intent intent2 = new Intent(intent);
                        intent2.setComponent(componentName);
                        intent2.putExtra("playBillingLibraryVersion", this.zzb);
                        if (this.zze.bindService(intent2, this.zzg, 1)) {
                            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Service was bonded successfully.");
                            return;
                        }
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Connection to Billing service is blocked.");
                    }
                }
            }
            this.zza = 0;
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Billing service unavailable on device.");
            zzatVar.onBillingSetupFinished(zzbb.zzc);
        }
    }

    public final Handler zzF() {
        return Looper.myLooper() == null ? this.zzc : new Handler(Looper.myLooper());
    }

    private final BillingResult zzG(final BillingResult billingResult) {
        if (Thread.interrupted()) {
            return billingResult;
        }
        this.zzc.post(new Runnable() { // from class: com.android.billingclient.api.zzag
            @Override // java.lang.Runnable
            public final void run() {
                BillingClientImpl.this.zzE(billingResult);
            }
        });
        return billingResult;
    }

    public final BillingResult zzH() {
        if (this.zza == 0 || this.zza == 3) {
            return zzbb.zzm;
        }
        return zzbb.zzj;
    }

    private static String zzI() {
        try {
            return (String) Class.forName("com.android.billingclient.ktx.BuildConfig").getField("VERSION_NAME").get(null);
        } catch (Exception e) {
            return BuildConfig.VERSION_NAME;
        }
    }

    public final Future zzJ(Callable callable, long j, final Runnable runnable, Handler handler) {
        double d = j;
        Double.isNaN(d);
        long j2 = (long) (d * 0.95d);
        if (this.zzv == null) {
            this.zzv = Executors.newFixedThreadPool(com.google.android.gms.internal.play_billing.zzb.zza, new zzal(this));
        }
        try {
            final Future submit = this.zzv.submit(callable);
            handler.postDelayed(new Runnable() { // from class: com.android.billingclient.api.zzaf
                @Override // java.lang.Runnable
                public final void run() {
                    Future future = submit;
                    Runnable runnable2 = runnable;
                    if (future.isDone() || future.isCancelled()) {
                        return;
                    }
                    future.cancel(true);
                    com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Async task is taking too long, cancel it!");
                    if (runnable2 == null) {
                        return;
                    }
                    runnable2.run();
                }
            }, j2);
            return submit;
        } catch (Exception e) {
            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Async task throws exception!", e);
            return null;
        }
    }

    private final void zzK(final BillingResult billingResult, final PriceChangeConfirmationListener priceChangeConfirmationListener) {
        if (Thread.interrupted()) {
            return;
        }
        this.zzc.post(new Runnable() { // from class: com.android.billingclient.api.zzx
            @Override // java.lang.Runnable
            public final void run() {
                PriceChangeConfirmationListener.this.onPriceChangeConfirmationResult(billingResult);
            }
        });
    }

    private final void zzL(String str, final PurchaseHistoryResponseListener purchaseHistoryResponseListener) {
        if (!isReady()) {
            purchaseHistoryResponseListener.onPurchaseHistoryResponse(zzbb.zzm, null);
        } else if (zzJ(new zzaj(this, str, purchaseHistoryResponseListener), 30000L, new Runnable() { // from class: com.android.billingclient.api.zzw
            @Override // java.lang.Runnable
            public final void run() {
                PurchaseHistoryResponseListener.this.onPurchaseHistoryResponse(zzbb.zzn, null);
            }
        }, zzF()) != null) {
        } else {
            purchaseHistoryResponseListener.onPurchaseHistoryResponse(zzH(), null);
        }
    }

    private final void zzM(String str, final PurchasesResponseListener purchasesResponseListener) {
        if (!isReady()) {
            purchasesResponseListener.onQueryPurchasesResponse(zzbb.zzm, com.google.android.gms.internal.play_billing.zzu.zzl());
        } else if (TextUtils.isEmpty(str)) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please provide a valid product type.");
            purchasesResponseListener.onQueryPurchasesResponse(zzbb.zzg, com.google.android.gms.internal.play_billing.zzu.zzl());
        } else if (zzJ(new zzai(this, str, purchasesResponseListener), 30000L, new Runnable() { // from class: com.android.billingclient.api.zzad
            @Override // java.lang.Runnable
            public final void run() {
                PurchasesResponseListener.this.onQueryPurchasesResponse(zzbb.zzn, com.google.android.gms.internal.play_billing.zzu.zzl());
            }
        }, zzF()) != null) {
        } else {
            purchasesResponseListener.onQueryPurchasesResponse(zzH(), com.google.android.gms.internal.play_billing.zzu.zzl());
        }
    }

    public static /* bridge */ /* synthetic */ zzas zzg(BillingClientImpl billingClientImpl, String str) {
        com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Querying purchase history, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        Bundle zzh = com.google.android.gms.internal.play_billing.zzb.zzh(billingClientImpl.zzm, billingClientImpl.zzt, billingClientImpl.zzb);
        String str2 = null;
        while (billingClientImpl.zzk) {
            try {
                Bundle zzh2 = billingClientImpl.zzf.zzh(6, billingClientImpl.zze.getPackageName(), str, str2, zzh);
                BillingResult zza = zzbi.zza(zzh2, "BillingClient", "getPurchaseHistory()");
                if (zza != zzbb.zzl) {
                    return new zzas(zza, null);
                }
                ArrayList<String> stringArrayList = zzh2.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> stringArrayList2 = zzh2.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> stringArrayList3 = zzh2.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                for (int i = 0; i < stringArrayList2.size(); i++) {
                    String str3 = stringArrayList2.get(i);
                    String str4 = stringArrayList3.get(i);
                    com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Purchase record found for sku : ".concat(String.valueOf(stringArrayList.get(i))));
                    try {
                        PurchaseHistoryRecord purchaseHistoryRecord = new PurchaseHistoryRecord(str3, str4);
                        if (TextUtils.isEmpty(purchaseHistoryRecord.getPurchaseToken())) {
                            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "BUG: empty/null token!");
                        }
                        arrayList.add(purchaseHistoryRecord);
                    } catch (JSONException e) {
                        com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got an exception trying to decode the purchase!", e);
                        return new zzas(zzbb.zzj, null);
                    }
                }
                str2 = zzh2.getString("INAPP_CONTINUATION_TOKEN");
                com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
                if (TextUtils.isEmpty(str2)) {
                    return new zzas(zzbb.zzl, arrayList);
                }
            } catch (RemoteException e2) {
                com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got exception trying to get purchase history, try to reconnect", e2);
                return new zzas(zzbb.zzm, null);
            }
        }
        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "getPurchaseHistory is not supported on current device");
        return new zzas(zzbb.zzq, null);
    }

    public static /* bridge */ /* synthetic */ zzbh zzi(BillingClientImpl billingClientImpl, String str) {
        Bundle bundle;
        com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Querying owned items, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        Bundle zzh = com.google.android.gms.internal.play_billing.zzb.zzh(billingClientImpl.zzm, billingClientImpl.zzt, billingClientImpl.zzb);
        String str2 = null;
        do {
            try {
                if (billingClientImpl.zzm) {
                    bundle = billingClientImpl.zzf.zzj(9, billingClientImpl.zze.getPackageName(), str, str2, zzh);
                } else {
                    bundle = billingClientImpl.zzf.zzi(3, billingClientImpl.zze.getPackageName(), str, str2);
                }
                BillingResult zza = zzbi.zza(bundle, "BillingClient", "getPurchase()");
                if (zza != zzbb.zzl) {
                    return new zzbh(zza, null);
                }
                ArrayList<String> stringArrayList = bundle.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> stringArrayList2 = bundle.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> stringArrayList3 = bundle.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                for (int i = 0; i < stringArrayList2.size(); i++) {
                    String str3 = stringArrayList2.get(i);
                    String str4 = stringArrayList3.get(i);
                    com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Sku is owned: ".concat(String.valueOf(stringArrayList.get(i))));
                    try {
                        Purchase purchase = new Purchase(str3, str4);
                        if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
                            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "BUG: empty/null token!");
                        }
                        arrayList.add(purchase);
                    } catch (JSONException e) {
                        com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got an exception trying to decode the purchase!", e);
                        return new zzbh(zzbb.zzj, null);
                    }
                }
                str2 = bundle.getString("INAPP_CONTINUATION_TOKEN");
                com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
            } catch (Exception e2) {
                com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got exception trying to get purchasesm try to reconnect", e2);
                return new zzbh(zzbb.zzm, null);
            }
        } while (!TextUtils.isEmpty(str2));
        return new zzbh(zzbb.zzl, arrayList);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void acknowledgePurchase(final AcknowledgePurchaseParams acknowledgePurchaseParams, final AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener) {
        if (!isReady()) {
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(zzbb.zzm);
        } else if (TextUtils.isEmpty(acknowledgePurchaseParams.getPurchaseToken())) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please provide a valid purchase token.");
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(zzbb.zzi);
        } else if (!this.zzm) {
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(zzbb.zzb);
        } else if (zzJ(new Callable() { // from class: com.android.billingclient.api.zzy
            @Override // java.util.concurrent.Callable
            public final Object call() {
                BillingClientImpl.this.zzk(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzz
            @Override // java.lang.Runnable
            public final void run() {
                AcknowledgePurchaseResponseListener.this.onAcknowledgePurchaseResponse(zzbb.zzn);
            }
        }, zzF()) != null) {
        } else {
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(zzH());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void consumeAsync(final ConsumeParams consumeParams, final ConsumeResponseListener consumeResponseListener) {
        if (!isReady()) {
            consumeResponseListener.onConsumeResponse(zzbb.zzm, consumeParams.getPurchaseToken());
        } else if (zzJ(new Callable() { // from class: com.android.billingclient.api.zzu
            @Override // java.util.concurrent.Callable
            public final Object call() {
                BillingClientImpl.this.zzl(consumeParams, consumeResponseListener);
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzv
            @Override // java.lang.Runnable
            public final void run() {
                ConsumeResponseListener.this.onConsumeResponse(zzbb.zzn, consumeParams.getPurchaseToken());
            }
        }, zzF()) != null) {
        } else {
            consumeResponseListener.onConsumeResponse(zzH(), consumeParams.getPurchaseToken());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void endConnection() {
        try {
            this.zzd.zzd();
            if (this.zzg != null) {
                this.zzg.zzc();
            }
            if (this.zzg != null && this.zzf != null) {
                com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Unbinding from service.");
                this.zze.unbindService(this.zzg);
                this.zzg = null;
            }
            this.zzf = null;
            ExecutorService executorService = this.zzv;
            if (executorService != null) {
                executorService.shutdownNow();
                this.zzv = null;
            }
        } catch (Exception e) {
            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "There was an exception while ending connection!", e);
        } finally {
            this.zza = 3;
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final int getConnectionState() {
        return this.zza;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.android.billingclient.api.BillingClient
    public final BillingResult isFeatureSupported(String str) {
        char c;
        if (!isReady()) {
            return zzbb.zzm;
        }
        switch (str.hashCode()) {
            case -422092961:
                if (str.equals(BillingClient.FeatureType.SUBSCRIPTIONS_UPDATE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 96321:
                if (str.equals("aaa")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 97314:
                if (str.equals(BillingClient.FeatureType.IN_APP_MESSAGING)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 98307:
                if (str.equals("ccc")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 99300:
                if (str.equals("ddd")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 100293:
                if (str.equals("eee")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 101286:
                if (str.equals(BillingClient.FeatureType.PRODUCT_DETAILS)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 207616302:
                if (str.equals(BillingClient.FeatureType.PRICE_CHANGE_CONFIRMATION)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1987365622:
                if (str.equals(BillingClient.FeatureType.SUBSCRIPTIONS)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                if (this.zzh) {
                    return zzbb.zzl;
                }
                return zzbb.zzo;
            case 1:
                if (this.zzi) {
                    return zzbb.zzl;
                }
                return zzbb.zzp;
            case 2:
                if (this.zzl) {
                    return zzbb.zzl;
                }
                return zzbb.zzr;
            case 3:
                return this.zzo ? zzbb.zzl : zzbb.zzw;
            case 4:
                return this.zzq ? zzbb.zzl : zzbb.zzs;
            case 5:
                return this.zzp ? zzbb.zzl : zzbb.zzu;
            case 6:
            case 7:
                return this.zzr ? zzbb.zzl : zzbb.zzt;
            case '\b':
                return this.zzs ? zzbb.zzl : zzbb.zzv;
            default:
                com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Unsupported feature: ".concat(String.valueOf(str)));
                return zzbb.zzy;
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final boolean isReady() {
        return (this.zza != 2 || this.zzf == null || this.zzg == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:127:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0323  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x036c A[Catch: Exception -> 0x03ac, CancellationException -> 0x03b8, TimeoutException -> 0x03ba, TryCatch #4 {CancellationException -> 0x03b8, TimeoutException -> 0x03ba, Exception -> 0x03ac, blocks: (B:132:0x035a, B:134:0x036c, B:136:0x0392), top: B:150:0x035a }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0392 A[Catch: Exception -> 0x03ac, CancellationException -> 0x03b8, TimeoutException -> 0x03ba, TRY_LEAVE, TryCatch #4 {CancellationException -> 0x03b8, TimeoutException -> 0x03ba, Exception -> 0x03ac, blocks: (B:132:0x035a, B:134:0x036c, B:136:0x0392), top: B:150:0x035a }] */
    @Override // com.android.billingclient.api.BillingClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final BillingResult launchBillingFlow(Activity activity, final BillingFlowParams billingFlowParams) {
        final String str;
        final String str2;
        String str3;
        String str4;
        Future future;
        Throwable e;
        int zzb;
        SkuDetails skuDetails;
        BillingFlowParams.ProductDetailsParams productDetailsParams;
        String str5;
        String str6;
        String str7;
        String str8;
        boolean z;
        final int i;
        String str9;
        String str10;
        if (!isReady()) {
            BillingResult billingResult = zzbb.zzm;
            zzG(billingResult);
            return billingResult;
        }
        ArrayList<SkuDetails> zze = billingFlowParams.zze();
        List zzf = billingFlowParams.zzf();
        SkuDetails skuDetails2 = (SkuDetails) com.google.android.gms.internal.play_billing.zzz.zza(zze, null);
        BillingFlowParams.ProductDetailsParams productDetailsParams2 = (BillingFlowParams.ProductDetailsParams) com.google.android.gms.internal.play_billing.zzz.zza(zzf, null);
        if (skuDetails2 != null) {
            str2 = skuDetails2.getSku();
            str = skuDetails2.getType();
        } else {
            str2 = productDetailsParams2.zza().getProductId();
            str = productDetailsParams2.zza().getProductType();
        }
        String str11 = "BillingClient";
        if (!str.equals("subs") || this.zzh) {
            if (!billingFlowParams.zzo() || this.zzk) {
                if (zze.size() <= 1 || this.zzr) {
                    if (zzf.isEmpty() || this.zzs) {
                        if (this.zzk) {
                            final Bundle zzf2 = com.google.android.gms.internal.play_billing.zzb.zzf(billingFlowParams, this.zzm, this.zzt, this.zzu, this.zzb);
                            str3 = "BUY_INTENT";
                            if (!zze.isEmpty()) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayList2 = new ArrayList<>();
                                str8 = str;
                                ArrayList<String> arrayList3 = new ArrayList<>();
                                str7 = str2;
                                ArrayList<Integer> arrayList4 = new ArrayList<>();
                                str6 = "proxyPackageVersion";
                                ArrayList<String> arrayList5 = new ArrayList<>();
                                boolean z2 = false;
                                boolean z3 = false;
                                boolean z4 = false;
                                boolean z5 = false;
                                for (SkuDetails skuDetails3 : zze) {
                                    if (skuDetails3.zzf().isEmpty()) {
                                        str10 = str11;
                                    } else {
                                        str10 = str11;
                                        arrayList.add(skuDetails3.zzf());
                                    }
                                    String zzc = skuDetails3.zzc();
                                    BillingFlowParams.ProductDetailsParams productDetailsParams3 = productDetailsParams2;
                                    String zzb2 = skuDetails3.zzb();
                                    int zza = skuDetails3.zza();
                                    SkuDetails skuDetails4 = skuDetails2;
                                    String zze2 = skuDetails3.zze();
                                    arrayList2.add(zzc);
                                    z2 |= !TextUtils.isEmpty(zzc);
                                    arrayList3.add(zzb2);
                                    z3 |= !TextUtils.isEmpty(zzb2);
                                    arrayList4.add(Integer.valueOf(zza));
                                    z4 |= zza != 0;
                                    z5 |= !TextUtils.isEmpty(zze2);
                                    arrayList5.add(zze2);
                                    str11 = str10;
                                    productDetailsParams2 = productDetailsParams3;
                                    skuDetails2 = skuDetails4;
                                }
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str5 = str11;
                                if (!arrayList.isEmpty()) {
                                    zzf2.putStringArrayList("skuDetailsTokens", arrayList);
                                }
                                if (z2) {
                                    zzf2.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList2);
                                }
                                if (z3) {
                                    zzf2.putStringArrayList("SKU_OFFER_ID_LIST", arrayList3);
                                }
                                if (z4) {
                                    zzf2.putIntegerArrayList("SKU_OFFER_TYPE_LIST", arrayList4);
                                }
                                if (z5) {
                                    zzf2.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList5);
                                }
                                if (zze.size() > 1) {
                                    ArrayList<String> arrayList6 = new ArrayList<>(zze.size() - 1);
                                    ArrayList<String> arrayList7 = new ArrayList<>(zze.size() - 1);
                                    for (int i2 = 1; i2 < zze.size(); i2++) {
                                        arrayList6.add(((SkuDetails) zze.get(i2)).getSku());
                                        arrayList7.add(((SkuDetails) zze.get(i2)).getType());
                                    }
                                    zzf2.putStringArrayList("additionalSkus", arrayList6);
                                    zzf2.putStringArrayList("additionalSkuTypes", arrayList7);
                                }
                            } else {
                                str6 = "proxyPackageVersion";
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str7 = str2;
                                str8 = str;
                                str5 = str11;
                                ArrayList<String> arrayList8 = new ArrayList<>(zzf.size() - 1);
                                ArrayList<String> arrayList9 = new ArrayList<>(zzf.size() - 1);
                                ArrayList<String> arrayList10 = new ArrayList<>();
                                ArrayList<String> arrayList11 = new ArrayList<>();
                                for (int i3 = 0; i3 < zzf.size(); i3++) {
                                    BillingFlowParams.ProductDetailsParams productDetailsParams4 = (BillingFlowParams.ProductDetailsParams) zzf.get(i3);
                                    ProductDetails zza2 = productDetailsParams4.zza();
                                    if (!zza2.zzb().isEmpty()) {
                                        arrayList10.add(zza2.zzb());
                                    }
                                    arrayList11.add(productDetailsParams4.zzb());
                                    if (i3 > 0) {
                                        arrayList8.add(((BillingFlowParams.ProductDetailsParams) zzf.get(i3)).zza().getProductId());
                                        arrayList9.add(((BillingFlowParams.ProductDetailsParams) zzf.get(i3)).zza().getProductType());
                                    }
                                }
                                zzf2.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList11);
                                if (!arrayList10.isEmpty()) {
                                    zzf2.putStringArrayList("skuDetailsTokens", arrayList10);
                                }
                                if (!arrayList8.isEmpty()) {
                                    zzf2.putStringArrayList("additionalSkus", arrayList8);
                                    zzf2.putStringArrayList("additionalSkuTypes", arrayList9);
                                }
                            }
                            if (!zzf2.containsKey("SKU_OFFER_ID_TOKEN_LIST") || this.zzp) {
                                if (skuDetails != null && !TextUtils.isEmpty(skuDetails.zzd())) {
                                    zzf2.putString("skuPackageName", skuDetails.zzd());
                                    z = true;
                                } else if (productDetailsParams == null || TextUtils.isEmpty(productDetailsParams.zza().zza())) {
                                    z = false;
                                } else {
                                    zzf2.putString("skuPackageName", productDetailsParams.zza().zza());
                                    z = true;
                                }
                                if (!TextUtils.isEmpty(null)) {
                                    zzf2.putString("accountName", null);
                                }
                                Intent intent = activity.getIntent();
                                if (intent == null) {
                                    str4 = str5;
                                    com.google.android.gms.internal.play_billing.zzb.zzo(str4, "Activity's intent is null.");
                                } else {
                                    str4 = str5;
                                    if (!TextUtils.isEmpty(intent.getStringExtra("PROXY_PACKAGE"))) {
                                        String stringExtra = intent.getStringExtra("PROXY_PACKAGE");
                                        zzf2.putString("proxyPackage", stringExtra);
                                        try {
                                            str9 = str6;
                                        } catch (PackageManager.NameNotFoundException e2) {
                                            str9 = str6;
                                        }
                                        try {
                                            zzf2.putString(str9, this.zze.getPackageManager().getPackageInfo(stringExtra, 0).versionName);
                                        } catch (PackageManager.NameNotFoundException e3) {
                                            zzf2.putString(str9, "package not found");
                                            if (!this.zzs) {
                                            }
                                            if (this.zzq) {
                                            }
                                            if (!this.zzm) {
                                            }
                                            final String str12 = str7;
                                            final String str13 = str8;
                                            future = zzJ(new Callable() { // from class: com.android.billingclient.api.zzab
                                                @Override // java.util.concurrent.Callable
                                                public final Object call() {
                                                    return BillingClientImpl.this.zzc(i, str12, str13, billingFlowParams, zzf2);
                                                }
                                            }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, null, this.zzc);
                                            Bundle bundle = (Bundle) future.get(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, TimeUnit.MILLISECONDS);
                                            zzb = com.google.android.gms.internal.play_billing.zzb.zzb(bundle, str4);
                                            String zzk = com.google.android.gms.internal.play_billing.zzb.zzk(bundle, str4);
                                            if (zzb == 0) {
                                            }
                                        }
                                    }
                                }
                                if (!this.zzs && !zzf.isEmpty()) {
                                    i = 17;
                                } else if (this.zzq || !z) {
                                    i = !this.zzm ? 9 : 6;
                                } else {
                                    i = 15;
                                }
                                final String str122 = str7;
                                final String str132 = str8;
                                future = zzJ(new Callable() { // from class: com.android.billingclient.api.zzab
                                    @Override // java.util.concurrent.Callable
                                    public final Object call() {
                                        return BillingClientImpl.this.zzc(i, str122, str132, billingFlowParams, zzf2);
                                    }
                                }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, null, this.zzc);
                            } else {
                                BillingResult billingResult2 = zzbb.zzu;
                                zzG(billingResult2);
                                return billingResult2;
                            }
                        } else {
                            str3 = "BUY_INTENT";
                            str4 = str11;
                            future = zzJ(new Callable() { // from class: com.android.billingclient.api.zzac
                                @Override // java.util.concurrent.Callable
                                public final Object call() {
                                    return BillingClientImpl.this.zzd(str2, str);
                                }
                            }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, null, this.zzc);
                        }
                        try {
                            Bundle bundle2 = (Bundle) future.get(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, TimeUnit.MILLISECONDS);
                            zzb = com.google.android.gms.internal.play_billing.zzb.zzb(bundle2, str4);
                            String zzk2 = com.google.android.gms.internal.play_billing.zzb.zzk(bundle2, str4);
                            if (zzb == 0) {
                                com.google.android.gms.internal.play_billing.zzb.zzo(str4, "Unable to buy item, Error response code: " + zzb);
                                BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                newBuilder.setResponseCode(zzb);
                                newBuilder.setDebugMessage(zzk2);
                                BillingResult build = newBuilder.build();
                                zzG(build);
                                return build;
                            }
                            Intent intent2 = new Intent(activity, ProxyBillingActivity.class);
                            String str14 = str3;
                            intent2.putExtra(str14, (PendingIntent) bundle2.getParcelable(str14));
                            activity.startActivity(intent2);
                            return zzbb.zzl;
                        } catch (CancellationException e4) {
                            e = e4;
                            com.google.android.gms.internal.play_billing.zzb.zzp(str4, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult3 = zzbb.zzn;
                            zzG(billingResult3);
                            return billingResult3;
                        } catch (TimeoutException e5) {
                            e = e5;
                            com.google.android.gms.internal.play_billing.zzb.zzp(str4, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult32 = zzbb.zzn;
                            zzG(billingResult32);
                            return billingResult32;
                        } catch (Exception e6) {
                            com.google.android.gms.internal.play_billing.zzb.zzp(str4, "Exception while launching billing flow. Try to reconnect", e6);
                            BillingResult billingResult4 = zzbb.zzm;
                            zzG(billingResult4);
                            return billingResult4;
                        }
                    }
                    com.google.android.gms.internal.play_billing.zzb.zzo(str11, "Current client doesn't support purchases with ProductDetails.");
                    BillingResult billingResult5 = zzbb.zzv;
                    zzG(billingResult5);
                    return billingResult5;
                }
                com.google.android.gms.internal.play_billing.zzb.zzo(str11, "Current client doesn't support multi-item purchases.");
                BillingResult billingResult6 = zzbb.zzt;
                zzG(billingResult6);
                return billingResult6;
            }
            com.google.android.gms.internal.play_billing.zzb.zzo(str11, "Current client doesn't support extra params for buy intent.");
            BillingResult billingResult7 = zzbb.zzh;
            zzG(billingResult7);
            return billingResult7;
        }
        com.google.android.gms.internal.play_billing.zzb.zzo(str11, "Current client doesn't support subscriptions.");
        BillingResult billingResult8 = zzbb.zzo;
        zzG(billingResult8);
        return billingResult8;
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryProductDetailsAsync(final QueryProductDetailsParams params, final ProductDetailsResponseListener listener) {
        if (!isReady()) {
            listener.onProductDetailsResponse(zzbb.zzm, new ArrayList());
        } else if (!this.zzs) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Querying product details is not supported.");
            listener.onProductDetailsResponse(zzbb.zzv, new ArrayList());
        } else if (zzJ(new Callable() { // from class: com.android.billingclient.api.zzs
            @Override // java.util.concurrent.Callable
            public final Object call() {
                BillingClientImpl.this.zzm(params, listener);
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzt
            @Override // java.lang.Runnable
            public final void run() {
                ProductDetailsResponseListener.this.onProductDetailsResponse(zzbb.zzn, new ArrayList());
            }
        }, zzF()) != null) {
        } else {
            listener.onProductDetailsResponse(zzH(), new ArrayList());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryPurchaseHistoryAsync(QueryPurchaseHistoryParams queryPurchaseHistoryParams, PurchaseHistoryResponseListener listener) {
        zzL(queryPurchaseHistoryParams.zza(), listener);
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener listener) {
        zzM(queryPurchasesParams.zza(), listener);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams, final SkuDetailsResponseListener skuDetailsResponseListener) {
        if (!isReady()) {
            skuDetailsResponseListener.onSkuDetailsResponse(zzbb.zzm, null);
            return;
        }
        String skuType = skuDetailsParams.getSkuType();
        List<String> skusList = skuDetailsParams.getSkusList();
        if (TextUtils.isEmpty(skuType)) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please fix the input params. SKU type can't be empty.");
            skuDetailsResponseListener.onSkuDetailsResponse(zzbb.zzf, null);
        } else if (skusList != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : skusList) {
                zzbt zzbtVar = new zzbt(null);
                zzbtVar.zza(str);
                arrayList.add(zzbtVar.zzb());
            }
            if (zzJ(new Callable(skuType, arrayList, null, skuDetailsResponseListener) { // from class: com.android.billingclient.api.zzq
                public final /* synthetic */ String zzb;
                public final /* synthetic */ List zzc;
                public final /* synthetic */ SkuDetailsResponseListener zzd;

                {
                    this.zzd = skuDetailsResponseListener;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    BillingClientImpl.this.zzn(this.zzb, this.zzc, null, this.zzd);
                    return null;
                }
            }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzaa
                @Override // java.lang.Runnable
                public final void run() {
                    SkuDetailsResponseListener.this.onSkuDetailsResponse(zzbb.zzn, null);
                }
            }, zzF()) != null) {
                return;
            }
            skuDetailsResponseListener.onSkuDetailsResponse(zzH(), null);
        } else {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please fix the input params. The list of SKUs can't be empty - set SKU list or SkuWithOffer list.");
            skuDetailsResponseListener.onSkuDetailsResponse(zzbb.zze, null);
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public BillingResult showInAppMessages(final Activity activity, InAppMessageParams inAppMessageParams, InAppMessageResponseListener inAppMessageResponseListener) {
        if (!isReady()) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Service disconnected.");
            return zzbb.zzm;
        } else if (!this.zzo) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Current client doesn't support showing in-app messages.");
            return zzbb.zzw;
        } else {
            View findViewById = activity.findViewById(16908290);
            IBinder windowToken = findViewById.getWindowToken();
            Rect rect = new Rect();
            findViewById.getGlobalVisibleRect(rect);
            final Bundle bundle = new Bundle();
            BundleCompat.putBinder(bundle, "KEY_WINDOW_TOKEN", windowToken);
            bundle.putInt("KEY_DIMEN_LEFT", rect.left);
            bundle.putInt("KEY_DIMEN_TOP", rect.top);
            bundle.putInt("KEY_DIMEN_RIGHT", rect.right);
            bundle.putInt("KEY_DIMEN_BOTTOM", rect.bottom);
            bundle.putString("playBillingLibraryVersion", this.zzb);
            bundle.putIntegerArrayList("KEY_CATEGORY_IDS", inAppMessageParams.getInAppMessageCategoriesToShow());
            final zzak zzakVar = new zzak(this, this.zzc, inAppMessageResponseListener);
            zzJ(new Callable() { // from class: com.android.billingclient.api.zzae
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    BillingClientImpl.this.zzo(bundle, activity, zzakVar);
                    return null;
                }
            }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, null, this.zzc);
            return zzbb.zzl;
        }
    }

    public final /* synthetic */ void zzE(BillingResult billingResult) {
        if (this.zzd.zzc() != null) {
            this.zzd.zzc().onPurchasesUpdated(billingResult, null);
            return;
        }
        this.zzd.zzb();
        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "No valid listener is set in BroadcastManager");
    }

    public final /* synthetic */ Bundle zzc(int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) throws Exception {
        return this.zzf.zzg(i, this.zze.getPackageName(), str, str2, null, bundle);
    }

    public final /* synthetic */ Bundle zzd(String str, String str2) throws Exception {
        return this.zzf.zzf(3, this.zze.getPackageName(), str, str2, null);
    }

    public final /* synthetic */ Bundle zze(String str, Bundle bundle) throws Exception {
        return this.zzf.zzm(8, this.zze.getPackageName(), str, "subs", bundle);
    }

    public final /* synthetic */ Object zzk(AcknowledgePurchaseParams acknowledgePurchaseParams, AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener) throws Exception {
        try {
            Bundle zzd = this.zzf.zzd(9, this.zze.getPackageName(), acknowledgePurchaseParams.getPurchaseToken(), com.google.android.gms.internal.play_billing.zzb.zzc(acknowledgePurchaseParams, this.zzb));
            int zzb = com.google.android.gms.internal.play_billing.zzb.zzb(zzd, "BillingClient");
            String zzk = com.google.android.gms.internal.play_billing.zzb.zzk(zzd, "BillingClient");
            BillingResult.Builder newBuilder = BillingResult.newBuilder();
            newBuilder.setResponseCode(zzb);
            newBuilder.setDebugMessage(zzk);
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(newBuilder.build());
            return null;
        } catch (Exception e) {
            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Error acknowledge purchase!", e);
            acknowledgePurchaseResponseListener.onAcknowledgePurchaseResponse(zzbb.zzm);
            return null;
        }
    }

    public final /* synthetic */ Object zzl(ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener) throws Exception {
        String str;
        int i;
        String purchaseToken = consumeParams.getPurchaseToken();
        try {
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Consuming purchase with token: " + purchaseToken);
            if (this.zzm) {
                Bundle zze = this.zzf.zze(9, this.zze.getPackageName(), purchaseToken, com.google.android.gms.internal.play_billing.zzb.zzd(consumeParams, this.zzm, this.zzb));
                i = zze.getInt("RESPONSE_CODE");
                str = com.google.android.gms.internal.play_billing.zzb.zzk(zze, "BillingClient");
            } else {
                i = this.zzf.zza(3, this.zze.getPackageName(), purchaseToken);
                str = "";
            }
            BillingResult.Builder newBuilder = BillingResult.newBuilder();
            newBuilder.setResponseCode(i);
            newBuilder.setDebugMessage(str);
            BillingResult build = newBuilder.build();
            if (i == 0) {
                com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Successfully consumed purchase.");
                consumeResponseListener.onConsumeResponse(build, purchaseToken);
                return null;
            }
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Error consuming purchase with token. Response code: " + i);
            consumeResponseListener.onConsumeResponse(build, purchaseToken);
            return null;
        } catch (Exception e) {
            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Error consuming purchase!", e);
            consumeResponseListener.onConsumeResponse(zzbb.zzm, purchaseToken);
            return null;
        }
    }

    public final /* synthetic */ Object zzm(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) throws Exception {
        String str;
        ArrayList arrayList = new ArrayList();
        String zzb = queryProductDetailsParams.zzb();
        com.google.android.gms.internal.play_billing.zzu zza = queryProductDetailsParams.zza();
        int size = zza.size();
        int i = 0;
        int i2 = 0;
        while (true) {
            str = "Item is unavailable for purchase.";
            if (i2 >= size) {
                str = "";
                break;
            }
            int i3 = i2 + 20;
            ArrayList arrayList2 = new ArrayList(zza.subList(i2, i3 > size ? size : i3));
            ArrayList<String> arrayList3 = new ArrayList<>();
            int size2 = arrayList2.size();
            for (int i4 = 0; i4 < size2; i4++) {
                arrayList3.add(((QueryProductDetailsParams.Product) arrayList2.get(i4)).zza());
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("ITEM_ID_LIST", arrayList3);
            bundle.putString("playBillingLibraryVersion", this.zzb);
            try {
                Bundle zzl = this.zzf.zzl(17, this.zze.getPackageName(), zzb, bundle, com.google.android.gms.internal.play_billing.zzb.zzg(this.zzb, arrayList2, null));
                if (zzl == null) {
                    com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "queryProductDetailsAsync got empty product details response.");
                    i = 4;
                    break;
                } else if (!zzl.containsKey("DETAILS_LIST")) {
                    i = com.google.android.gms.internal.play_billing.zzb.zzb(zzl, "BillingClient");
                    str = com.google.android.gms.internal.play_billing.zzb.zzk(zzl, "BillingClient");
                    if (i != 0) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "getSkuDetails() failed for queryProductDetailsAsync. Response code: " + i);
                    } else {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a product detail list for queryProductDetailsAsync.");
                        i = 6;
                    }
                } else {
                    ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                    if (stringArrayList == null) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "queryProductDetailsAsync got null response list");
                        i = 4;
                        break;
                    }
                    for (int i5 = 0; i5 < stringArrayList.size(); i5++) {
                        try {
                            ProductDetails productDetails = new ProductDetails(stringArrayList.get(i5));
                            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Got product details: ".concat(productDetails.toString()));
                            arrayList.add(productDetails);
                        } catch (JSONException e) {
                            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got a JSON exception trying to decode ProductDetails. \n Exception: ", e);
                            str = "Error trying to decode SkuDetails.";
                            i = 6;
                        }
                    }
                    i2 = i3;
                }
            } catch (Exception e2) {
                com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "queryProductDetailsAsync got a remote exception (try to reconnect).", e2);
                str = "An internal error occurred.";
                i = 6;
            }
        }
        BillingResult.Builder newBuilder = BillingResult.newBuilder();
        newBuilder.setResponseCode(i);
        newBuilder.setDebugMessage(str);
        productDetailsResponseListener.onProductDetailsResponse(newBuilder.build(), arrayList);
        return null;
    }

    public final /* synthetic */ Object zzn(String str, List list, String str2, SkuDetailsResponseListener skuDetailsResponseListener) throws Exception {
        String str3;
        int i;
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        int i2 = 0;
        while (true) {
            str3 = "Item is unavailable for purchase.";
            if (i2 >= size) {
                str3 = "";
                i = 0;
                break;
            }
            int i3 = i2 + 20;
            ArrayList arrayList2 = new ArrayList(list.subList(i2, i3 > size ? size : i3));
            ArrayList<String> arrayList3 = new ArrayList<>();
            int size2 = arrayList2.size();
            for (int i4 = 0; i4 < size2; i4++) {
                arrayList3.add(((zzbv) arrayList2.get(i4)).zza());
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("ITEM_ID_LIST", arrayList3);
            bundle.putString("playBillingLibraryVersion", this.zzb);
            try {
                Bundle zzl = this.zzn ? this.zzf.zzl(10, this.zze.getPackageName(), str, bundle, com.google.android.gms.internal.play_billing.zzb.zze(this.zzj, this.zzt, this.zzb, null, arrayList2)) : this.zzf.zzk(3, this.zze.getPackageName(), str, bundle);
                if (zzl == null) {
                    com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "querySkuDetailsAsync got null sku details list");
                    arrayList = null;
                    i = 4;
                    break;
                } else if (!zzl.containsKey("DETAILS_LIST")) {
                    int zzb = com.google.android.gms.internal.play_billing.zzb.zzb(zzl, "BillingClient");
                    str3 = com.google.android.gms.internal.play_billing.zzb.zzk(zzl, "BillingClient");
                    if (zzb != 0) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "getSkuDetails() failed. Response code: " + zzb);
                        i = zzb;
                    } else {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a detail list.");
                        i = 6;
                    }
                } else {
                    ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                    if (stringArrayList == null) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "querySkuDetailsAsync got null response list");
                        arrayList = null;
                        i = 4;
                        break;
                    }
                    for (int i5 = 0; i5 < stringArrayList.size(); i5++) {
                        try {
                            SkuDetails skuDetails = new SkuDetails(stringArrayList.get(i5));
                            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Got sku details: ".concat(skuDetails.toString()));
                            arrayList.add(skuDetails);
                        } catch (JSONException e) {
                            com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Got a JSON exception trying to decode SkuDetails.", e);
                            str3 = "Error trying to decode SkuDetails.";
                            arrayList = null;
                            i = 6;
                        }
                    }
                    i2 = i3;
                }
            } catch (Exception e2) {
                com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "querySkuDetailsAsync got a remote exception (try to reconnect).", e2);
                i = -1;
                str3 = "Service connection is disconnected.";
                arrayList = null;
            }
        }
        BillingResult.Builder newBuilder = BillingResult.newBuilder();
        newBuilder.setResponseCode(i);
        newBuilder.setDebugMessage(str3);
        skuDetailsResponseListener.onSkuDetailsResponse(newBuilder.build(), arrayList);
        return null;
    }

    public final /* synthetic */ Object zzo(Bundle bundle, Activity activity, ResultReceiver resultReceiver) throws Exception {
        this.zzf.zzn(12, this.zze.getPackageName(), bundle, new zzar(new WeakReference(activity), resultReceiver, null));
        return null;
    }

    @Override // com.android.billingclient.api.BillingClient
    public void launchPriceChangeConfirmationFlow(Activity activity, PriceChangeFlowParams priceChangeFlowParams, PriceChangeConfirmationListener listener) {
        Throwable e;
        if (!isReady()) {
            zzK(zzbb.zzm, listener);
        } else if (priceChangeFlowParams == null || priceChangeFlowParams.getSkuDetails() == null) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please fix the input params. priceChangeFlowParams must contain valid sku.");
            zzK(zzbb.zzk, listener);
        } else {
            final String sku = priceChangeFlowParams.getSkuDetails().getSku();
            if (sku == null) {
                com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Please fix the input params. priceChangeFlowParams must contain valid sku.");
                zzK(zzbb.zzk, listener);
            } else if (!this.zzl) {
                com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Current client doesn't support price change confirmation flow.");
                zzK(zzbb.zzr, listener);
            } else {
                final Bundle bundle = new Bundle();
                bundle.putString("playBillingLibraryVersion", this.zzb);
                bundle.putBoolean("subs_price_change", true);
                try {
                    Bundle bundle2 = (Bundle) zzJ(new Callable() { // from class: com.android.billingclient.api.zzr
                        @Override // java.util.concurrent.Callable
                        public final Object call() {
                            return BillingClientImpl.this.zze(sku, bundle);
                        }
                    }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, null, this.zzc).get(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS, TimeUnit.MILLISECONDS);
                    int zzb = com.google.android.gms.internal.play_billing.zzb.zzb(bundle2, "BillingClient");
                    String zzk = com.google.android.gms.internal.play_billing.zzb.zzk(bundle2, "BillingClient");
                    BillingResult.Builder newBuilder = BillingResult.newBuilder();
                    newBuilder.setResponseCode(zzb);
                    newBuilder.setDebugMessage(zzk);
                    BillingResult build = newBuilder.build();
                    if (zzb != 0) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Unable to launch price change flow, error response code: " + zzb);
                        zzK(build, listener);
                        return;
                    }
                    zzah zzahVar = new zzah(this, this.zzc, listener);
                    Intent intent = new Intent(activity, ProxyBillingActivity.class);
                    intent.putExtra("SUBS_MANAGEMENT_INTENT", (PendingIntent) bundle2.getParcelable("SUBS_MANAGEMENT_INTENT"));
                    intent.putExtra("result_receiver", zzahVar);
                    activity.startActivity(intent);
                } catch (CancellationException e2) {
                    e = e2;
                    com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Time out while launching Price Change Flow for sku: " + sku + "; try to reconnect", e);
                    zzK(zzbb.zzn, listener);
                } catch (TimeoutException e3) {
                    e = e3;
                    com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Time out while launching Price Change Flow for sku: " + sku + "; try to reconnect", e);
                    zzK(zzbb.zzn, listener);
                } catch (Exception e4) {
                    com.google.android.gms.internal.play_billing.zzb.zzp("BillingClient", "Exception caught while launching Price Change Flow for sku: " + sku + "; try to reconnect", e4);
                    zzK(zzbb.zzm, listener);
                }
            }
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void queryPurchaseHistoryAsync(String str, PurchaseHistoryResponseListener purchaseHistoryResponseListener) {
        zzL(str, purchaseHistoryResponseListener);
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryPurchasesAsync(String skuType, PurchasesResponseListener listener) {
        zzM(skuType, listener);
    }

    private BillingClientImpl(Context context, boolean z, PurchasesUpdatedListener purchasesUpdatedListener, String str, String str2, zzc zzcVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = str;
        initialize(context, purchasesUpdatedListener, z, null);
    }

    private BillingClientImpl(String versionOverride) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = versionOverride;
    }

    public BillingClientImpl(String str, boolean z, Context context, zzbe zzbeVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = zzI();
        Context applicationContext = context.getApplicationContext();
        this.zze = applicationContext;
        this.zzd = new zzo(applicationContext, null);
        this.zzt = z;
    }

    public BillingClientImpl(String str, boolean z, Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzc zzcVar) {
        this(context, z, purchasesUpdatedListener, zzI(), null, null);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void startConnection(BillingClientStateListener billingClientStateListener) {
        if (isReady()) {
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Service connection is valid. No need to re-initialize.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzl);
        } else if (this.zza == 1) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Client is already in the process of connecting to billing service.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzd);
        } else if (this.zza == 3) {
            com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Client was already closed and can't be reused. Please create another instance.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzm);
        } else {
            this.zza = 1;
            this.zzd.zze();
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Starting in-app billing setup.");
            this.zzg = new zzap(this, billingClientStateListener, null);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.zze.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
                ResolveInfo resolveInfo = queryIntentServices.get(0);
                if (resolveInfo.serviceInfo != null) {
                    String str = resolveInfo.serviceInfo.packageName;
                    String str2 = resolveInfo.serviceInfo.name;
                    if (!"com.android.vending".equals(str) || str2 == null) {
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "The device doesn't have valid Play Store.");
                    } else {
                        ComponentName componentName = new ComponentName(str, str2);
                        Intent intent2 = new Intent(intent);
                        intent2.setComponent(componentName);
                        intent2.putExtra("playBillingLibraryVersion", this.zzb);
                        if (this.zze.bindService(intent2, this.zzg, 1)) {
                            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Service was bonded successfully.");
                            return;
                        }
                        com.google.android.gms.internal.play_billing.zzb.zzo("BillingClient", "Connection to Billing service is blocked.");
                    }
                }
            }
            this.zza = 0;
            com.google.android.gms.internal.play_billing.zzb.zzn("BillingClient", "Billing service unavailable on device.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzc);
        }
    }
}
