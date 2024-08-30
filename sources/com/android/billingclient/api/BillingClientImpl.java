package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zze;
import com.google.android.gms.internal.play_billing.zzfb;
import com.google.android.gms.internal.play_billing.zzfl;
import com.google.android.gms.internal.play_billing.zzfm;
import com.google.android.gms.internal.play_billing.zzz;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BillingClientImpl extends BillingClient {
    private volatile int zza;
    private final String zzb;
    private final Handler zzc;
    private volatile zzh zzd;
    private Context zze;
    private zzar zzf;
    private volatile zze zzg;
    private volatile zzaf zzh;
    private boolean zzi;
    private boolean zzj;
    private int zzk;
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
    private boolean zzv;
    private boolean zzw;
    private zzbe zzx;
    private boolean zzy;
    private ExecutorService zzz;

    private BillingClientImpl(Context context, zzbe zzbeVar, PurchasesUpdatedListener purchasesUpdatedListener, String str, String str2, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzk = 0;
        this.zzb = str;
        initialize(context, purchasesUpdatedListener, zzbeVar, alternativeBillingListener, str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BillingClientImpl(String str, zzbe zzbeVar, Context context, PurchasesUpdatedListener purchasesUpdatedListener, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this(context, zzbeVar, purchasesUpdatedListener, zzR(), null, alternativeBillingListener, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BillingClientImpl(String str, zzbe zzbeVar, Context context, zzaz zzazVar, zzar zzarVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzk = 0;
        this.zzb = zzR();
        this.zze = context.getApplicationContext();
        zzfl zzv = zzfm.zzv();
        zzv.zzj(zzR());
        zzv.zzi(this.zze.getPackageName());
        this.zzf = new zzaw(this.zze, (zzfm) zzv.zzc());
        zzb.zzj("BillingClient", "Billing client should have a valid listener but the provided is null.");
        this.zzd = new zzh(this.zze, null, this.zzf);
        this.zzx = zzbeVar;
    }

    private void initialize(Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzbe zzbeVar, AlternativeBillingListener alternativeBillingListener, String str, zzar zzarVar) {
        this.zze = context.getApplicationContext();
        zzfl zzv = zzfm.zzv();
        zzv.zzj(str);
        zzv.zzi(this.zze.getPackageName());
        if (zzarVar == null) {
            zzarVar = new zzaw(this.zze, (zzfm) zzv.zzc());
        }
        this.zzf = zzarVar;
        if (purchasesUpdatedListener == null) {
            zzb.zzj("BillingClient", "Billing client should have a valid listener but the provided is null.");
        }
        this.zzd = new zzh(this.zze, purchasesUpdatedListener, alternativeBillingListener, this.zzf);
        this.zzx = zzbeVar;
        this.zzy = alternativeBillingListener != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ zzbj zzN(BillingClientImpl billingClientImpl, String str, int i) {
        zzb.zzi("BillingClient", "Querying owned items, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        Bundle zzc = zzb.zzc(billingClientImpl.zzn, billingClientImpl.zzv, true, false, billingClientImpl.zzb);
        List list = null;
        String str2 = null;
        while (true) {
            try {
                Bundle zzj = billingClientImpl.zzn ? billingClientImpl.zzg.zzj(z != billingClientImpl.zzv ? 9 : 19, billingClientImpl.zze.getPackageName(), str, str2, zzc) : billingClientImpl.zzg.zzi(3, billingClientImpl.zze.getPackageName(), str, str2);
                zzbk zza = zzbl.zza(zzj, "BillingClient", "getPurchase()");
                BillingResult zza2 = zza.zza();
                if (zza2 != zzat.zzl) {
                    billingClientImpl.zzf.zza(zzaq.zza(zza.zzb(), 9, zza2));
                    return new zzbj(zza2, list);
                }
                ArrayList<String> stringArrayList = zzj.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> stringArrayList2 = zzj.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> stringArrayList3 = zzj.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                boolean z2 = false;
                for (int i2 = 0; i2 < stringArrayList2.size(); i2++) {
                    String str3 = stringArrayList2.get(i2);
                    String str4 = stringArrayList3.get(i2);
                    zzb.zzi("BillingClient", "Sku is owned: ".concat(String.valueOf(stringArrayList.get(i2))));
                    try {
                        Purchase purchase = new Purchase(str3, str4);
                        if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
                            zzb.zzj("BillingClient", "BUG: empty/null token!");
                            z2 = true;
                        }
                        arrayList.add(purchase);
                    } catch (JSONException e) {
                        zzb.zzk("BillingClient", "Got an exception trying to decode the purchase!", e);
                        zzar zzarVar = billingClientImpl.zzf;
                        BillingResult billingResult = zzat.zzj;
                        zzarVar.zza(zzaq.zza(51, 9, billingResult));
                        return new zzbj(billingResult, null);
                    }
                }
                if (z2) {
                    billingClientImpl.zzf.zza(zzaq.zza(26, 9, zzat.zzj));
                }
                str2 = zzj.getString("INAPP_CONTINUATION_TOKEN");
                zzb.zzi("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
                if (TextUtils.isEmpty(str2)) {
                    return new zzbj(zzat.zzl, arrayList);
                }
                list = null;
                z = true;
            } catch (Exception e2) {
                zzar zzarVar2 = billingClientImpl.zzf;
                BillingResult billingResult2 = zzat.zzm;
                zzarVar2.zza(zzaq.zza(52, 9, billingResult2));
                zzb.zzk("BillingClient", "Got exception trying to get purchasesm try to reconnect", e2);
                return new zzbj(billingResult2, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Handler zzO() {
        return Looper.myLooper() == null ? this.zzc : new Handler(Looper.myLooper());
    }

    private final BillingResult zzP(final BillingResult billingResult) {
        if (Thread.interrupted()) {
            return billingResult;
        }
        this.zzc.post(new Runnable() { // from class: com.android.billingclient.api.zzx
            @Override // java.lang.Runnable
            public final void run() {
                BillingClientImpl.this.zzH(billingResult);
            }
        });
        return billingResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final BillingResult zzQ() {
        return (this.zza == 0 || this.zza == 3) ? zzat.zzm : zzat.zzj;
    }

    private static String zzR() {
        try {
            return (String) Class.forName("com.android.billingclient.ktx.BuildConfig").getField("VERSION_NAME").get(null);
        } catch (Exception unused) {
            return "6.0.1";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Future zzS(Callable callable, long j, final Runnable runnable, Handler handler) {
        if (this.zzz == null) {
            this.zzz = Executors.newFixedThreadPool(zzb.zza, new zzab(this));
        }
        try {
            final Future submit = this.zzz.submit(callable);
            double d = j;
            Runnable runnable2 = new Runnable() { // from class: com.android.billingclient.api.zzw
                @Override // java.lang.Runnable
                public final void run() {
                    Future future = submit;
                    Runnable runnable3 = runnable;
                    if (future.isDone() || future.isCancelled()) {
                        return;
                    }
                    future.cancel(true);
                    zzb.zzj("BillingClient", "Async task is taking too long, cancel it!");
                    if (runnable3 != null) {
                        runnable3.run();
                    }
                }
            };
            Double.isNaN(d);
            handler.postDelayed(runnable2, (long) (d * 0.95d));
            return submit;
        } catch (Exception e) {
            zzb.zzk("BillingClient", "Async task throws exception!", e);
            return null;
        }
    }

    private final void zzU(String str, final PurchasesResponseListener purchasesResponseListener) {
        if (!isReady()) {
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(2, 9, billingResult));
            purchasesResponseListener.onQueryPurchasesResponse(billingResult, com.google.android.gms.internal.play_billing.zzu.zzk());
        } else if (TextUtils.isEmpty(str)) {
            zzb.zzj("BillingClient", "Please provide a valid product type.");
            zzar zzarVar2 = this.zzf;
            BillingResult billingResult2 = zzat.zzg;
            zzarVar2.zza(zzaq.zza(50, 9, billingResult2));
            purchasesResponseListener.onQueryPurchasesResponse(billingResult2, com.google.android.gms.internal.play_billing.zzu.zzk());
        } else if (zzS(new zzy(this, str, purchasesResponseListener), 30000L, new Runnable() { // from class: com.android.billingclient.api.zzu
            @Override // java.lang.Runnable
            public final void run() {
                BillingClientImpl.this.zzL(purchasesResponseListener);
            }
        }, zzO()) == null) {
            BillingResult zzQ = zzQ();
            this.zzf.zza(zzaq.zza(25, 9, zzQ));
            purchasesResponseListener.onQueryPurchasesResponse(zzQ, com.google.android.gms.internal.play_billing.zzu.zzk());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void consumeAsync(final ConsumeParams consumeParams, final ConsumeResponseListener consumeResponseListener) {
        if (!isReady()) {
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(2, 4, billingResult));
            consumeResponseListener.onConsumeResponse(billingResult, consumeParams.getPurchaseToken());
        } else if (zzS(new Callable() { // from class: com.android.billingclient.api.zzm
            @Override // java.util.concurrent.Callable
            public final Object call() {
                BillingClientImpl.this.zzk(consumeParams, consumeResponseListener);
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzn
            @Override // java.lang.Runnable
            public final void run() {
                BillingClientImpl.this.zzI(consumeResponseListener, consumeParams);
            }
        }, zzO()) == null) {
            BillingResult zzQ = zzQ();
            this.zzf.zza(zzaq.zza(25, 4, zzQ));
            consumeResponseListener.onConsumeResponse(zzQ, consumeParams.getPurchaseToken());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final boolean isReady() {
        return (this.zza != 2 || this.zzg == null || this.zzh == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0337  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0346 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x039e A[Catch: Exception -> 0x03cf, CancellationException -> 0x03d1, TimeoutException -> 0x03d3, TryCatch #4 {CancellationException -> 0x03d1, TimeoutException -> 0x03d3, Exception -> 0x03cf, blocks: (B:152:0x038a, B:154:0x039e, B:162:0x03d5), top: B:172:0x038a }] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x03d5 A[Catch: Exception -> 0x03cf, CancellationException -> 0x03d1, TimeoutException -> 0x03d3, TRY_LEAVE, TryCatch #4 {CancellationException -> 0x03d1, TimeoutException -> 0x03d3, Exception -> 0x03cf, blocks: (B:152:0x038a, B:154:0x039e, B:162:0x03d5), top: B:172:0x038a }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01f6  */
    @Override // com.android.billingclient.api.BillingClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final BillingResult launchBillingFlow(Activity activity, final BillingFlowParams billingFlowParams) {
        String str;
        String str2;
        Callable callable;
        Runnable runnable;
        Handler handler;
        long j;
        BillingClientImpl billingClientImpl;
        zzar zzarVar;
        BillingResult billingResult;
        int i;
        int zzb;
        zzfb zza;
        int zza2;
        boolean z;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        boolean z2;
        Intent intent;
        String str9;
        int i2;
        if (isReady()) {
            ArrayList zzg = billingFlowParams.zzg();
            List zzh = billingFlowParams.zzh();
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzz.zza(zzg, null));
            BillingFlowParams.ProductDetailsParams productDetailsParams = (BillingFlowParams.ProductDetailsParams) zzz.zza(zzh, null);
            final String productId = productDetailsParams.zza().getProductId();
            final String productType = productDetailsParams.zza().getProductType();
            String str10 = "BillingClient";
            if (!productType.equals("subs") || this.zzi) {
                if (billingFlowParams.zzq() && !this.zzl) {
                    zzb.zzj("BillingClient", "Current client doesn't support extra params for buy intent.");
                    zzarVar = this.zzf;
                    billingResult = zzat.zzh;
                    i2 = 18;
                } else if (zzg.size() > 1 && !this.zzs) {
                    zzb.zzj("BillingClient", "Current client doesn't support multi-item purchases.");
                    zzarVar = this.zzf;
                    billingResult = zzat.zzt;
                    i2 = 19;
                } else if (zzh.isEmpty() || this.zzt) {
                    if (this.zzl) {
                        boolean z3 = this.zzn;
                        boolean z4 = this.zzy;
                        String str11 = this.zzb;
                        final Bundle bundle = new Bundle();
                        bundle.putString("playBillingLibraryVersion", str11);
                        if (billingFlowParams.zzb() != 0) {
                            zza2 = billingFlowParams.zzb();
                        } else {
                            if (billingFlowParams.zza() != 0) {
                                zza2 = billingFlowParams.zza();
                            }
                            if (!TextUtils.isEmpty(billingFlowParams.zzc())) {
                                bundle.putString("accountId", billingFlowParams.zzc());
                            }
                            if (!TextUtils.isEmpty(billingFlowParams.zzd())) {
                                bundle.putString("obfuscatedProfileId", billingFlowParams.zzd());
                            }
                            if (billingFlowParams.zzp()) {
                                bundle.putBoolean("isOfferPersonalizedByDeveloper", true);
                            }
                            if (!TextUtils.isEmpty(null)) {
                                bundle.putStringArrayList("skusToReplace", new ArrayList<>(Arrays.asList(null)));
                            }
                            if (!TextUtils.isEmpty(billingFlowParams.zze())) {
                                bundle.putString("oldSkuPurchaseToken", billingFlowParams.zze());
                            }
                            if (!TextUtils.isEmpty(null)) {
                                bundle.putString("oldSkuPurchaseId", null);
                            }
                            if (!TextUtils.isEmpty(billingFlowParams.zzf())) {
                                bundle.putString("originalExternalTransactionId", billingFlowParams.zzf());
                            }
                            if (!TextUtils.isEmpty(null)) {
                                bundle.putString("paymentsPurchaseParams", null);
                            }
                            if (z3) {
                                z = true;
                            } else {
                                z = true;
                                bundle.putBoolean("enablePendingPurchases", true);
                            }
                            if (z4) {
                                bundle.putBoolean("enableAlternativeBilling", z);
                            }
                            str = "BUY_INTENT";
                            if (zzg.isEmpty()) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                new ArrayList();
                                new ArrayList();
                                new ArrayList();
                                new ArrayList();
                                Iterator it = zzg.iterator();
                                if (it.hasNext()) {
                                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                                    throw null;
                                }
                                if (!arrayList.isEmpty()) {
                                    bundle.putStringArrayList("skuDetailsTokens", arrayList);
                                }
                                if (zzg.size() > 1) {
                                    ArrayList<String> arrayList2 = new ArrayList<>(zzg.size() - 1);
                                    ArrayList<String> arrayList3 = new ArrayList<>(zzg.size() - 1);
                                    if (1 < zzg.size()) {
                                        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzg.get(1));
                                        throw null;
                                    }
                                    bundle.putStringArrayList("additionalSkus", arrayList2);
                                    bundle.putStringArrayList("additionalSkuTypes", arrayList3);
                                }
                                str4 = "proxyPackageVersion";
                                str5 = productId;
                                str3 = productType;
                                str6 = "BillingClient";
                            } else {
                                ArrayList<String> arrayList4 = new ArrayList<>(zzh.size() - 1);
                                ArrayList<String> arrayList5 = new ArrayList<>(zzh.size() - 1);
                                ArrayList<String> arrayList6 = new ArrayList<>();
                                ArrayList<String> arrayList7 = new ArrayList<>();
                                str3 = productType;
                                ArrayList<String> arrayList8 = new ArrayList<>();
                                str4 = "proxyPackageVersion";
                                str5 = productId;
                                int i3 = 0;
                                while (i3 < zzh.size()) {
                                    BillingFlowParams.ProductDetailsParams productDetailsParams2 = (BillingFlowParams.ProductDetailsParams) zzh.get(i3);
                                    ProductDetails zza3 = productDetailsParams2.zza();
                                    if (zza3.zzb().isEmpty()) {
                                        str7 = str10;
                                    } else {
                                        str7 = str10;
                                        arrayList6.add(zza3.zzb());
                                    }
                                    arrayList7.add(productDetailsParams2.zzb());
                                    if (!TextUtils.isEmpty(zza3.zzc())) {
                                        arrayList8.add(zza3.zzc());
                                    }
                                    if (i3 > 0) {
                                        arrayList4.add(((BillingFlowParams.ProductDetailsParams) zzh.get(i3)).zza().getProductId());
                                        arrayList5.add(((BillingFlowParams.ProductDetailsParams) zzh.get(i3)).zza().getProductType());
                                    }
                                    i3++;
                                    str10 = str7;
                                }
                                str6 = str10;
                                bundle.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList7);
                                if (!arrayList6.isEmpty()) {
                                    bundle.putStringArrayList("skuDetailsTokens", arrayList6);
                                }
                                if (!arrayList8.isEmpty()) {
                                    bundle.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList8);
                                }
                                if (!arrayList4.isEmpty()) {
                                    bundle.putStringArrayList("additionalSkus", arrayList4);
                                    bundle.putStringArrayList("additionalSkuTypes", arrayList5);
                                }
                            }
                            if (bundle.containsKey("SKU_OFFER_ID_TOKEN_LIST") || this.zzq) {
                                if (productDetailsParams != null || TextUtils.isEmpty(productDetailsParams.zza().zza())) {
                                    str8 = null;
                                    z2 = false;
                                } else {
                                    bundle.putString("skuPackageName", productDetailsParams.zza().zza());
                                    str8 = null;
                                    z2 = true;
                                }
                                if (!TextUtils.isEmpty(str8)) {
                                    bundle.putString("accountName", str8);
                                }
                                intent = activity.getIntent();
                                if (intent != null) {
                                    str2 = str6;
                                    zzb.zzj(str2, "Activity's intent is null.");
                                } else {
                                    str2 = str6;
                                    if (!TextUtils.isEmpty(intent.getStringExtra("PROXY_PACKAGE"))) {
                                        String stringExtra = intent.getStringExtra("PROXY_PACKAGE");
                                        bundle.putString("proxyPackage", stringExtra);
                                        try {
                                            str9 = str4;
                                            try {
                                                bundle.putString(str9, this.zze.getPackageManager().getPackageInfo(stringExtra, 0).versionName);
                                            } catch (PackageManager.NameNotFoundException unused) {
                                                bundle.putString(str9, "package not found");
                                                if (this.zzt) {
                                                }
                                                final String str12 = str5;
                                                final String str13 = str3;
                                                Callable callable2 = new Callable() { // from class: com.android.billingclient.api.zzs
                                                    @Override // java.util.concurrent.Callable
                                                    public final Object call() {
                                                        return BillingClientImpl.this.zzc(r2, str12, str13, billingFlowParams, bundle);
                                                    }
                                                };
                                                runnable = null;
                                                handler = this.zzc;
                                                j = 5000;
                                                billingClientImpl = this;
                                                callable = callable2;
                                                Bundle bundle2 = (Bundle) billingClientImpl.zzS(callable, j, runnable, handler).get(5000L, TimeUnit.MILLISECONDS);
                                                zzb = zzb.zzb(bundle2, str2);
                                                String zzf = zzb.zzf(bundle2, str2);
                                                if (zzb != 0) {
                                                }
                                            }
                                        } catch (PackageManager.NameNotFoundException unused2) {
                                            str9 = str4;
                                        }
                                    }
                                }
                                final int i4 = (this.zzt || zzh.isEmpty()) ? (this.zzr || !z2) ? this.zzn ? 9 : 6 : 15 : 17;
                                final String str122 = str5;
                                final String str132 = str3;
                                Callable callable22 = new Callable() { // from class: com.android.billingclient.api.zzs
                                    @Override // java.util.concurrent.Callable
                                    public final Object call() {
                                        return BillingClientImpl.this.zzc(i4, str122, str132, billingFlowParams, bundle);
                                    }
                                };
                                runnable = null;
                                handler = this.zzc;
                                j = 5000;
                                billingClientImpl = this;
                                callable = callable22;
                            } else {
                                zzarVar = this.zzf;
                                billingResult = zzat.zzu;
                                i = 21;
                                zza = zzaq.zza(i, 2, billingResult);
                            }
                        }
                        bundle.putInt("prorationMode", zza2);
                        if (!TextUtils.isEmpty(billingFlowParams.zzc())) {
                        }
                        if (!TextUtils.isEmpty(billingFlowParams.zzd())) {
                        }
                        if (billingFlowParams.zzp()) {
                        }
                        if (!TextUtils.isEmpty(null)) {
                        }
                        if (!TextUtils.isEmpty(billingFlowParams.zze())) {
                        }
                        if (!TextUtils.isEmpty(null)) {
                        }
                        if (!TextUtils.isEmpty(billingFlowParams.zzf())) {
                        }
                        if (!TextUtils.isEmpty(null)) {
                        }
                        if (z3) {
                        }
                        if (z4) {
                        }
                        str = "BUY_INTENT";
                        if (zzg.isEmpty()) {
                        }
                        if (bundle.containsKey("SKU_OFFER_ID_TOKEN_LIST")) {
                        }
                        if (productDetailsParams != null) {
                        }
                        str8 = null;
                        z2 = false;
                        if (!TextUtils.isEmpty(str8)) {
                        }
                        intent = activity.getIntent();
                        if (intent != null) {
                        }
                        if (this.zzt) {
                        }
                        final String str1222 = str5;
                        final String str1322 = str3;
                        Callable callable222 = new Callable() { // from class: com.android.billingclient.api.zzs
                            @Override // java.util.concurrent.Callable
                            public final Object call() {
                                return BillingClientImpl.this.zzc(i4, str1222, str1322, billingFlowParams, bundle);
                            }
                        };
                        runnable = null;
                        handler = this.zzc;
                        j = 5000;
                        billingClientImpl = this;
                        callable = callable222;
                    } else {
                        str = "BUY_INTENT";
                        str2 = "BillingClient";
                        callable = new Callable() { // from class: com.android.billingclient.api.zzt
                            @Override // java.util.concurrent.Callable
                            public final Object call() {
                                return BillingClientImpl.this.zzd(productId, productType);
                            }
                        };
                        runnable = null;
                        handler = this.zzc;
                        j = 5000;
                        billingClientImpl = this;
                    }
                    try {
                        Bundle bundle22 = (Bundle) billingClientImpl.zzS(callable, j, runnable, handler).get(5000L, TimeUnit.MILLISECONDS);
                        zzb = zzb.zzb(bundle22, str2);
                        String zzf2 = zzb.zzf(bundle22, str2);
                        if (zzb != 0) {
                            Intent intent2 = new Intent(activity, ProxyBillingActivity.class);
                            String str14 = str;
                            intent2.putExtra(str14, (PendingIntent) bundle22.getParcelable(str14));
                            activity.startActivity(intent2);
                            return zzat.zzl;
                        }
                        zzb.zzj(str2, "Unable to buy item, Error response code: " + zzb);
                        BillingResult.Builder newBuilder = BillingResult.newBuilder();
                        newBuilder.setResponseCode(zzb);
                        newBuilder.setDebugMessage(zzf2);
                        BillingResult build = newBuilder.build();
                        this.zzf.zza(zzaq.zza(3, 2, build));
                        zzP(build);
                        return build;
                    } catch (CancellationException e) {
                        e = e;
                        zzb.zzk(str2, "Time out while launching billing flow. Try to reconnect", e);
                        zzarVar = this.zzf;
                        billingResult = zzat.zzn;
                        i = 4;
                        zza = zzaq.zza(i, 2, billingResult);
                        zzarVar.zza(zza);
                        zzP(billingResult);
                        return billingResult;
                    } catch (TimeoutException e2) {
                        e = e2;
                        zzb.zzk(str2, "Time out while launching billing flow. Try to reconnect", e);
                        zzarVar = this.zzf;
                        billingResult = zzat.zzn;
                        i = 4;
                        zza = zzaq.zza(i, 2, billingResult);
                        zzarVar.zza(zza);
                        zzP(billingResult);
                        return billingResult;
                    } catch (Exception e3) {
                        zzb.zzk(str2, "Exception while launching billing flow. Try to reconnect", e3);
                        zzarVar = this.zzf;
                        billingResult = zzat.zzm;
                        i = 5;
                    }
                } else {
                    zzb.zzj("BillingClient", "Current client doesn't support purchases with ProductDetails.");
                    zzarVar = this.zzf;
                    billingResult = zzat.zzv;
                    i2 = 20;
                }
                zza = zzaq.zza(i2, 2, billingResult);
            } else {
                zzb.zzj("BillingClient", "Current client doesn't support subscriptions.");
                zzarVar = this.zzf;
                billingResult = zzat.zzo;
                zza = zzaq.zza(9, 2, billingResult);
            }
        } else {
            zzarVar = this.zzf;
            billingResult = zzat.zzm;
            zza = zzaq.zza(2, 2, billingResult);
        }
        zzarVar.zza(zza);
        zzP(billingResult);
        return billingResult;
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void queryProductDetailsAsync(final QueryProductDetailsParams queryProductDetailsParams, final ProductDetailsResponseListener productDetailsResponseListener) {
        if (!isReady()) {
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(2, 7, billingResult));
            productDetailsResponseListener.onProductDetailsResponse(billingResult, new ArrayList());
        } else if (this.zzt) {
            if (zzS(new Callable() { // from class: com.android.billingclient.api.zzk
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    BillingClientImpl.this.zzl(queryProductDetailsParams, productDetailsResponseListener);
                    return null;
                }
            }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzl
                @Override // java.lang.Runnable
                public final void run() {
                    BillingClientImpl.this.zzJ(productDetailsResponseListener);
                }
            }, zzO()) == null) {
                BillingResult zzQ = zzQ();
                this.zzf.zza(zzaq.zza(25, 7, zzQ));
                productDetailsResponseListener.onProductDetailsResponse(zzQ, new ArrayList());
            }
        } else {
            zzb.zzj("BillingClient", "Querying product details is not supported.");
            zzar zzarVar2 = this.zzf;
            BillingResult billingResult2 = zzat.zzv;
            zzarVar2.zza(zzaq.zza(20, 7, billingResult2));
            productDetailsResponseListener.onProductDetailsResponse(billingResult2, new ArrayList());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener purchasesResponseListener) {
        zzU(queryPurchasesParams.zza(), purchasesResponseListener);
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void startConnection(BillingClientStateListener billingClientStateListener) {
        if (isReady()) {
            zzb.zzi("BillingClient", "Service connection is valid. No need to re-initialize.");
            this.zzf.zzb(zzaq.zzb(6));
            billingClientStateListener.onBillingSetupFinished(zzat.zzl);
            return;
        }
        int i = 1;
        if (this.zza == 1) {
            zzb.zzj("BillingClient", "Client is already in the process of connecting to billing service.");
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzd;
            zzarVar.zza(zzaq.zza(37, 6, billingResult));
            billingClientStateListener.onBillingSetupFinished(billingResult);
        } else if (this.zza == 3) {
            zzb.zzj("BillingClient", "Client was already closed and can't be reused. Please create another instance.");
            zzar zzarVar2 = this.zzf;
            BillingResult billingResult2 = zzat.zzm;
            zzarVar2.zza(zzaq.zza(38, 6, billingResult2));
            billingClientStateListener.onBillingSetupFinished(billingResult2);
        } else {
            this.zza = 1;
            this.zzd.zze();
            zzb.zzi("BillingClient", "Starting in-app billing setup.");
            this.zzh = new zzaf(this, billingClientStateListener, null);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.zze.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices == null || queryIntentServices.isEmpty()) {
                i = 41;
            } else {
                ServiceInfo serviceInfo = queryIntentServices.get(0).serviceInfo;
                if (serviceInfo != null) {
                    String str = serviceInfo.packageName;
                    String str2 = serviceInfo.name;
                    if (!"com.android.vending".equals(str) || str2 == null) {
                        zzb.zzj("BillingClient", "The device doesn't have valid Play Store.");
                        i = 40;
                    } else {
                        ComponentName componentName = new ComponentName(str, str2);
                        Intent intent2 = new Intent(intent);
                        intent2.setComponent(componentName);
                        intent2.putExtra("playBillingLibraryVersion", this.zzb);
                        if (this.zze.bindService(intent2, this.zzh, 1)) {
                            zzb.zzi("BillingClient", "Service was bonded successfully.");
                            return;
                        } else {
                            zzb.zzj("BillingClient", "Connection to Billing service is blocked.");
                            i = 39;
                        }
                    }
                }
            }
            this.zza = 0;
            zzb.zzi("BillingClient", "Billing service unavailable on device.");
            zzar zzarVar3 = this.zzf;
            BillingResult billingResult3 = zzat.zzc;
            zzarVar3.zza(zzaq.zza(i, 6, billingResult3));
            billingClientStateListener.onBillingSetupFinished(billingResult3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzH(BillingResult billingResult) {
        if (this.zzd.zzc() != null) {
            this.zzd.zzc().onPurchasesUpdated(billingResult, null);
            return;
        }
        this.zzd.zzb();
        zzb.zzj("BillingClient", "No valid listener is set in BroadcastManager");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzI(ConsumeResponseListener consumeResponseListener, ConsumeParams consumeParams) {
        zzar zzarVar = this.zzf;
        BillingResult billingResult = zzat.zzn;
        zzarVar.zza(zzaq.zza(24, 4, billingResult));
        consumeResponseListener.onConsumeResponse(billingResult, consumeParams.getPurchaseToken());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzJ(ProductDetailsResponseListener productDetailsResponseListener) {
        zzar zzarVar = this.zzf;
        BillingResult billingResult = zzat.zzn;
        zzarVar.zza(zzaq.zza(24, 7, billingResult));
        productDetailsResponseListener.onProductDetailsResponse(billingResult, new ArrayList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzL(PurchasesResponseListener purchasesResponseListener) {
        zzar zzarVar = this.zzf;
        BillingResult billingResult = zzat.zzn;
        zzarVar.zza(zzaq.zza(24, 9, billingResult));
        purchasesResponseListener.onQueryPurchasesResponse(billingResult, com.google.android.gms.internal.play_billing.zzu.zzk());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Bundle zzc(int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) {
        return this.zzg.zzg(i, this.zze.getPackageName(), str, str2, null, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Bundle zzd(String str, String str2) {
        return this.zzg.zzf(3, this.zze.getPackageName(), str, str2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Object zzk(ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener) {
        int zza;
        String str;
        String purchaseToken = consumeParams.getPurchaseToken();
        try {
            zzb.zzi("BillingClient", "Consuming purchase with token: " + purchaseToken);
            if (this.zzn) {
                zze zzeVar = this.zzg;
                String packageName = this.zze.getPackageName();
                boolean z = this.zzn;
                String str2 = this.zzb;
                Bundle bundle = new Bundle();
                if (z) {
                    bundle.putString("playBillingLibraryVersion", str2);
                }
                Bundle zze = zzeVar.zze(9, packageName, purchaseToken, bundle);
                zza = zze.getInt("RESPONSE_CODE");
                str = zzb.zzf(zze, "BillingClient");
            } else {
                zza = this.zzg.zza(3, this.zze.getPackageName(), purchaseToken);
                str = "";
            }
            BillingResult.Builder newBuilder = BillingResult.newBuilder();
            newBuilder.setResponseCode(zza);
            newBuilder.setDebugMessage(str);
            BillingResult build = newBuilder.build();
            if (zza == 0) {
                zzb.zzi("BillingClient", "Successfully consumed purchase.");
            } else {
                zzb.zzj("BillingClient", "Error consuming purchase with token. Response code: " + zza);
                this.zzf.zza(zzaq.zza(23, 4, build));
            }
            consumeResponseListener.onConsumeResponse(build, purchaseToken);
            return null;
        } catch (Exception e) {
            zzb.zzk("BillingClient", "Error consuming purchase!", e);
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(29, 4, billingResult));
            consumeResponseListener.onConsumeResponse(billingResult, purchaseToken);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0124, code lost:
        r0.zza(r2);
        r12 = 4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final /* synthetic */ Object zzl(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) {
        String str;
        int i;
        int i2;
        zze zzeVar;
        int i3;
        String packageName;
        Bundle bundle;
        com.google.android.gms.internal.play_billing.zzu zzuVar;
        zzar zzarVar;
        zzfb zza;
        zzar zzarVar2;
        zzfb zza2;
        ArrayList arrayList = new ArrayList();
        String zzb = queryProductDetailsParams.zzb();
        com.google.android.gms.internal.play_billing.zzu zza3 = queryProductDetailsParams.zza();
        int size = zza3.size();
        int i4 = 0;
        while (true) {
            if (i4 >= size) {
                str = "";
                i = 0;
                break;
            }
            int i5 = i4 + 20;
            ArrayList arrayList2 = new ArrayList(zza3.subList(i4, i5 > size ? size : i5));
            ArrayList<String> arrayList3 = new ArrayList<>();
            int size2 = arrayList2.size();
            for (int i6 = 0; i6 < size2; i6++) {
                arrayList3.add(((QueryProductDetailsParams.Product) arrayList2.get(i6)).zza());
            }
            Bundle bundle2 = new Bundle();
            bundle2.putStringArrayList("ITEM_ID_LIST", arrayList3);
            bundle2.putString("playBillingLibraryVersion", this.zzb);
            try {
                zzeVar = this.zzg;
                i3 = true != this.zzw ? 17 : 20;
                packageName = this.zze.getPackageName();
                String str2 = this.zzb;
                if (TextUtils.isEmpty(null)) {
                    this.zze.getPackageName();
                }
                bundle = new Bundle();
                bundle.putString("playBillingLibraryVersion", str2);
                bundle.putBoolean("enablePendingPurchases", true);
                bundle.putString("SKU_DETAILS_RESPONSE_FORMAT", "PRODUCT_DETAILS");
                ArrayList<String> arrayList4 = new ArrayList<>();
                ArrayList<String> arrayList5 = new ArrayList<>();
                int size3 = arrayList2.size();
                zzuVar = zza3;
                int i7 = 0;
                boolean z = false;
                boolean z2 = false;
                while (i7 < size3) {
                    ArrayList arrayList6 = arrayList2;
                    arrayList4.add(null);
                    z |= !TextUtils.isEmpty(null);
                    int i8 = size3;
                    if (((QueryProductDetailsParams.Product) arrayList2.get(i7)).zzb().equals("first_party")) {
                        com.google.android.gms.internal.play_billing.zzm.zzc(null, "Serialized DocId is required for constructing ExtraParams to query ProductDetails for all first party products.");
                        arrayList5.add(null);
                        z2 = true;
                    }
                    i7++;
                    size3 = i8;
                    arrayList2 = arrayList6;
                }
                if (z) {
                    bundle.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList4);
                }
                if (!arrayList5.isEmpty()) {
                    bundle.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList5);
                }
                if (z2 && !TextUtils.isEmpty(null)) {
                    bundle.putString("accountName", null);
                }
                i2 = 7;
            } catch (Exception e) {
                e = e;
                i2 = 7;
            }
            try {
                Bundle zzl = zzeVar.zzl(i3, packageName, zzb, bundle2, bundle);
                str = "Item is unavailable for purchase.";
                if (zzl == null) {
                    zzb.zzj("BillingClient", "queryProductDetailsAsync got empty product details response.");
                    zzarVar = this.zzf;
                    BillingResult.Builder newBuilder = BillingResult.newBuilder();
                    newBuilder.setResponseCode(4);
                    newBuilder.setDebugMessage("Item is unavailable for purchase.");
                    zza = zzaq.zza(44, 7, newBuilder.build());
                    break;
                } else if (zzl.containsKey("DETAILS_LIST")) {
                    ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                    if (stringArrayList == null) {
                        zzb.zzj("BillingClient", "queryProductDetailsAsync got null response list");
                        zzarVar = this.zzf;
                        zza = zzaq.zza(46, 7, zzat.zzB);
                        break;
                    }
                    for (int i9 = 0; i9 < stringArrayList.size(); i9++) {
                        try {
                            ProductDetails productDetails = new ProductDetails(stringArrayList.get(i9));
                            zzb.zzi("BillingClient", "Got product details: ".concat(productDetails.toString()));
                            arrayList.add(productDetails);
                        } catch (JSONException e2) {
                            zzb.zzk("BillingClient", "Got a JSON exception trying to decode ProductDetails. \n Exception: ", e2);
                            zzarVar2 = this.zzf;
                            BillingResult.Builder newBuilder2 = BillingResult.newBuilder();
                            newBuilder2.setResponseCode(6);
                            str = "Error trying to decode SkuDetails.";
                            newBuilder2.setDebugMessage("Error trying to decode SkuDetails.");
                            zza2 = zzaq.zza(47, 7, newBuilder2.build());
                            zzarVar2.zza(zza2);
                            i = 6;
                            BillingResult.Builder newBuilder3 = BillingResult.newBuilder();
                            newBuilder3.setResponseCode(i);
                            newBuilder3.setDebugMessage(str);
                            productDetailsResponseListener.onProductDetailsResponse(newBuilder3.build(), arrayList);
                            return null;
                        }
                    }
                    i4 = i5;
                    zza3 = zzuVar;
                } else {
                    i = zzb.zzb(zzl, "BillingClient");
                    str = zzb.zzf(zzl, "BillingClient");
                    if (i != 0) {
                        zzb.zzj("BillingClient", "getSkuDetails() failed for queryProductDetailsAsync. Response code: " + i);
                        this.zzf.zza(zzaq.zza(23, 7, zzat.zza(i, str)));
                    } else {
                        zzb.zzj("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a product detail list for queryProductDetailsAsync.");
                        zzarVar2 = this.zzf;
                        BillingResult.Builder newBuilder4 = BillingResult.newBuilder();
                        newBuilder4.setResponseCode(6);
                        newBuilder4.setDebugMessage(str);
                        zza2 = zzaq.zza(45, 7, newBuilder4.build());
                    }
                }
            } catch (Exception e3) {
                e = e3;
                zzb.zzk("BillingClient", "queryProductDetailsAsync got a remote exception (try to reconnect).", e);
                this.zzf.zza(zzaq.zza(43, i2, zzat.zzj));
                str = "An internal error occurred.";
                i = 6;
                BillingResult.Builder newBuilder32 = BillingResult.newBuilder();
                newBuilder32.setResponseCode(i);
                newBuilder32.setDebugMessage(str);
                productDetailsResponseListener.onProductDetailsResponse(newBuilder32.build(), arrayList);
                return null;
            }
        }
        BillingResult.Builder newBuilder322 = BillingResult.newBuilder();
        newBuilder322.setResponseCode(i);
        newBuilder322.setDebugMessage(str);
        productDetailsResponseListener.onProductDetailsResponse(newBuilder322.build(), arrayList);
        return null;
    }
}
