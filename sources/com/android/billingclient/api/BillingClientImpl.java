package com.android.billingclient.api;

import android.annotation.SuppressLint;
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
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zze;
import com.google.android.gms.internal.play_billing.zzfl;
import com.google.android.gms.internal.play_billing.zzfm;
import com.google.android.gms.internal.play_billing.zzz;
import java.util.ArrayList;
import java.util.Arrays;
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
/* compiled from: com.android.billingclient:billing@@6.0.1 */
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

    private void initialize(Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzbe zzbeVar, AlternativeBillingListener alternativeBillingListener, String str, zzar zzarVar) {
        this.zze = context.getApplicationContext();
        zzfl zzv = zzfm.zzv();
        zzv.zzj(str);
        zzv.zzi(this.zze.getPackageName());
        if (zzarVar != null) {
            this.zzf = zzarVar;
        } else {
            this.zzf = new zzaw(this.zze, (zzfm) zzv.zzc());
        }
        if (purchasesUpdatedListener == null) {
            zzb.zzj("BillingClient", "Billing client should have a valid listener but the provided is null.");
        }
        this.zzd = new zzh(this.zze, purchasesUpdatedListener, alternativeBillingListener, this.zzf);
        this.zzx = zzbeVar;
        this.zzy = alternativeBillingListener != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ zzbj zzN(BillingClientImpl billingClientImpl, String str, int i) {
        Bundle zzi;
        zzb.zzi("BillingClient", "Querying owned items, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        Bundle zzc = zzb.zzc(billingClientImpl.zzn, billingClientImpl.zzv, true, false, billingClientImpl.zzb);
        List list = null;
        String str2 = null;
        while (true) {
            try {
                if (billingClientImpl.zzn) {
                    zzi = billingClientImpl.zzg.zzj(z != billingClientImpl.zzv ? 9 : 19, billingClientImpl.zze.getPackageName(), str, str2, zzc);
                } else {
                    zzi = billingClientImpl.zzg.zzi(3, billingClientImpl.zze.getPackageName(), str, str2);
                }
                zzbk zza = zzbl.zza(zzi, "BillingClient", "getPurchase()");
                BillingResult zza2 = zza.zza();
                if (zza2 == zzat.zzl) {
                    ArrayList<String> stringArrayList = zzi.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                    ArrayList<String> stringArrayList2 = zzi.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                    ArrayList<String> stringArrayList3 = zzi.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
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
                    str2 = zzi.getString("INAPP_CONTINUATION_TOKEN");
                    zzb.zzi("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
                    if (TextUtils.isEmpty(str2)) {
                        return new zzbj(zzat.zzl, arrayList);
                    }
                    list = null;
                    z = true;
                } else {
                    billingClientImpl.zzf.zza(zzaq.zza(zza.zzb(), 9, zza2));
                    return new zzbj(zza2, list);
                }
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
        if (this.zza == 0 || this.zza == 3) {
            return zzat.zzm;
        }
        return zzat.zzj;
    }

    @SuppressLint({"PrivateApi"})
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

    /* JADX WARN: Removed duplicated region for block: B:148:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0411  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0420 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:172:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0477 A[Catch: Exception -> 0x04c2, CancellationException -> 0x04d9, TimeoutException -> 0x04db, TryCatch #4 {CancellationException -> 0x04d9, TimeoutException -> 0x04db, Exception -> 0x04c2, blocks: (B:176:0x0463, B:178:0x0477, B:180:0x04a8), top: B:196:0x0463 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04a8 A[Catch: Exception -> 0x04c2, CancellationException -> 0x04d9, TimeoutException -> 0x04db, TRY_LEAVE, TryCatch #4 {CancellationException -> 0x04d9, TimeoutException -> 0x04db, Exception -> 0x04c2, blocks: (B:176:0x0463, B:178:0x0477, B:180:0x04a8), top: B:196:0x0463 }] */
    @Override // com.android.billingclient.api.BillingClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final BillingResult launchBillingFlow(Activity activity, final BillingFlowParams billingFlowParams) {
        final String productId;
        final String productType;
        String str;
        String str2;
        Future zzS;
        int zzb;
        boolean z;
        String str3;
        SkuDetails skuDetails;
        BillingFlowParams.ProductDetailsParams productDetailsParams;
        String str4;
        String str5;
        String str6;
        boolean z2;
        Intent intent;
        String str7;
        final int i;
        BillingFlowParams.ProductDetailsParams productDetailsParams2;
        final BillingClientImpl billingClientImpl = this;
        if (!isReady()) {
            zzar zzarVar = billingClientImpl.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(2, 2, billingResult));
            billingClientImpl.zzP(billingResult);
            return billingResult;
        }
        ArrayList<SkuDetails> zzg = billingFlowParams.zzg();
        List zzh = billingFlowParams.zzh();
        SkuDetails skuDetails2 = (SkuDetails) zzz.zza(zzg, null);
        BillingFlowParams.ProductDetailsParams productDetailsParams3 = (BillingFlowParams.ProductDetailsParams) zzz.zza(zzh, null);
        if (skuDetails2 != null) {
            productId = skuDetails2.getSku();
            productType = skuDetails2.getType();
        } else {
            productId = productDetailsParams3.zza().getProductId();
            productType = productDetailsParams3.zza().getProductType();
        }
        if (!productType.equals("subs") || billingClientImpl.zzi) {
            if (!billingFlowParams.zzq() || billingClientImpl.zzl) {
                if (zzg.size() <= 1 || billingClientImpl.zzs) {
                    if (zzh.isEmpty() || billingClientImpl.zzt) {
                        if (billingClientImpl.zzl) {
                            boolean z3 = billingClientImpl.zzn;
                            boolean z4 = billingClientImpl.zzy;
                            String str8 = billingClientImpl.zzb;
                            final Bundle bundle = new Bundle();
                            bundle.putString("playBillingLibraryVersion", str8);
                            if (billingFlowParams.zzb() != 0) {
                                bundle.putInt("prorationMode", billingFlowParams.zzb());
                            } else if (billingFlowParams.zza() != 0) {
                                bundle.putInt("prorationMode", billingFlowParams.zza());
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
                            String str9 = null;
                            if (!TextUtils.isEmpty(null)) {
                                bundle.putString("oldSkuPurchaseId", null);
                            }
                            if (!TextUtils.isEmpty(billingFlowParams.zzf())) {
                                bundle.putString("originalExternalTransactionId", billingFlowParams.zzf());
                                str9 = null;
                            }
                            if (!TextUtils.isEmpty(str9)) {
                                bundle.putString("paymentsPurchaseParams", str9);
                            }
                            if (z3) {
                                z = true;
                                bundle.putBoolean("enablePendingPurchases", true);
                            } else {
                                z = true;
                            }
                            if (z4) {
                                bundle.putBoolean("enableAlternativeBilling", z);
                            }
                            str = "BUY_INTENT";
                            final String str10 = productType;
                            if (!zzg.isEmpty()) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayList2 = new ArrayList<>();
                                str4 = productId;
                                ArrayList<String> arrayList3 = new ArrayList<>();
                                str3 = "proxyPackageVersion";
                                ArrayList<Integer> arrayList4 = new ArrayList<>();
                                str5 = "BillingClient";
                                ArrayList<String> arrayList5 = new ArrayList<>();
                                boolean z5 = false;
                                boolean z6 = false;
                                boolean z7 = false;
                                boolean z8 = false;
                                for (SkuDetails skuDetails3 : zzg) {
                                    if (skuDetails3.zzf().isEmpty()) {
                                        productDetailsParams2 = productDetailsParams3;
                                    } else {
                                        productDetailsParams2 = productDetailsParams3;
                                        arrayList.add(skuDetails3.zzf());
                                    }
                                    String zzc = skuDetails3.zzc();
                                    SkuDetails skuDetails4 = skuDetails2;
                                    String zzb2 = skuDetails3.zzb();
                                    int zza = skuDetails3.zza();
                                    String zze = skuDetails3.zze();
                                    arrayList2.add(zzc);
                                    z5 |= !TextUtils.isEmpty(zzc);
                                    arrayList3.add(zzb2);
                                    z6 |= !TextUtils.isEmpty(zzb2);
                                    arrayList4.add(Integer.valueOf(zza));
                                    z7 |= zza != 0;
                                    z8 |= !TextUtils.isEmpty(zze);
                                    arrayList5.add(zze);
                                    productDetailsParams3 = productDetailsParams2;
                                    skuDetails2 = skuDetails4;
                                }
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams3;
                                if (!arrayList.isEmpty()) {
                                    bundle.putStringArrayList("skuDetailsTokens", arrayList);
                                }
                                if (z5) {
                                    bundle.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList2);
                                }
                                if (z6) {
                                    bundle.putStringArrayList("SKU_OFFER_ID_LIST", arrayList3);
                                }
                                if (z7) {
                                    bundle.putIntegerArrayList("SKU_OFFER_TYPE_LIST", arrayList4);
                                }
                                if (z8) {
                                    bundle.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList5);
                                }
                                if (zzg.size() > 1) {
                                    ArrayList<String> arrayList6 = new ArrayList<>(zzg.size() - 1);
                                    ArrayList<String> arrayList7 = new ArrayList<>(zzg.size() - 1);
                                    for (int i2 = 1; i2 < zzg.size(); i2++) {
                                        arrayList6.add(((SkuDetails) zzg.get(i2)).getSku());
                                        arrayList7.add(((SkuDetails) zzg.get(i2)).getType());
                                    }
                                    bundle.putStringArrayList("additionalSkus", arrayList6);
                                    bundle.putStringArrayList("additionalSkuTypes", arrayList7);
                                }
                            } else {
                                str3 = "proxyPackageVersion";
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams3;
                                str4 = productId;
                                str5 = "BillingClient";
                                ArrayList<String> arrayList8 = new ArrayList<>(zzh.size() - 1);
                                ArrayList<String> arrayList9 = new ArrayList<>(zzh.size() - 1);
                                ArrayList<String> arrayList10 = new ArrayList<>();
                                ArrayList<String> arrayList11 = new ArrayList<>();
                                ArrayList<String> arrayList12 = new ArrayList<>();
                                for (int i3 = 0; i3 < zzh.size(); i3++) {
                                    BillingFlowParams.ProductDetailsParams productDetailsParams4 = (BillingFlowParams.ProductDetailsParams) zzh.get(i3);
                                    ProductDetails zza2 = productDetailsParams4.zza();
                                    if (!zza2.zzb().isEmpty()) {
                                        arrayList10.add(zza2.zzb());
                                    }
                                    arrayList11.add(productDetailsParams4.zzb());
                                    if (!TextUtils.isEmpty(zza2.zzc())) {
                                        arrayList12.add(zza2.zzc());
                                    }
                                    if (i3 > 0) {
                                        arrayList8.add(((BillingFlowParams.ProductDetailsParams) zzh.get(i3)).zza().getProductId());
                                        arrayList9.add(((BillingFlowParams.ProductDetailsParams) zzh.get(i3)).zza().getProductType());
                                    }
                                }
                                bundle.putStringArrayList("SKU_OFFER_ID_TOKEN_LIST", arrayList11);
                                if (!arrayList10.isEmpty()) {
                                    bundle.putStringArrayList("skuDetailsTokens", arrayList10);
                                }
                                if (!arrayList12.isEmpty()) {
                                    bundle.putStringArrayList("SKU_SERIALIZED_DOCID_LIST", arrayList12);
                                }
                                if (!arrayList8.isEmpty()) {
                                    bundle.putStringArrayList("additionalSkus", arrayList8);
                                    bundle.putStringArrayList("additionalSkuTypes", arrayList9);
                                }
                            }
                            billingClientImpl = this;
                            if (!bundle.containsKey("SKU_OFFER_ID_TOKEN_LIST") || billingClientImpl.zzq) {
                                if (skuDetails == null || TextUtils.isEmpty(skuDetails.zzd())) {
                                    if (productDetailsParams != null && !TextUtils.isEmpty(productDetailsParams.zza().zza())) {
                                        bundle.putString("skuPackageName", productDetailsParams.zza().zza());
                                    } else {
                                        str6 = null;
                                        z2 = false;
                                        if (!TextUtils.isEmpty(str6)) {
                                            bundle.putString("accountName", str6);
                                        }
                                        intent = activity.getIntent();
                                        if (intent != null) {
                                            str2 = str5;
                                            zzb.zzj(str2, "Activity's intent is null.");
                                        } else {
                                            str2 = str5;
                                            if (!TextUtils.isEmpty(intent.getStringExtra("PROXY_PACKAGE"))) {
                                                String stringExtra = intent.getStringExtra("PROXY_PACKAGE");
                                                bundle.putString("proxyPackage", stringExtra);
                                                try {
                                                    str7 = str3;
                                                    try {
                                                        bundle.putString(str7, billingClientImpl.zze.getPackageManager().getPackageInfo(stringExtra, 0).versionName);
                                                    } catch (PackageManager.NameNotFoundException unused) {
                                                        bundle.putString(str7, "package not found");
                                                        if (billingClientImpl.zzt) {
                                                        }
                                                        if (billingClientImpl.zzr) {
                                                        }
                                                        final String str11 = str4;
                                                        zzS = zzS(new Callable() { // from class: com.android.billingclient.api.zzs
                                                            @Override // java.util.concurrent.Callable
                                                            public final Object call() {
                                                                return BillingClientImpl.this.zzc(i, str11, str10, billingFlowParams, bundle);
                                                            }
                                                        }, 5000L, null, billingClientImpl.zzc);
                                                        Bundle bundle2 = (Bundle) zzS.get(5000L, TimeUnit.MILLISECONDS);
                                                        zzb = zzb.zzb(bundle2, str2);
                                                        String zzf = zzb.zzf(bundle2, str2);
                                                        if (zzb == 0) {
                                                        }
                                                    }
                                                } catch (PackageManager.NameNotFoundException unused2) {
                                                    str7 = str3;
                                                }
                                            }
                                        }
                                        if (billingClientImpl.zzt || zzh.isEmpty()) {
                                            i = (billingClientImpl.zzr || !z2) ? billingClientImpl.zzn ? 9 : 6 : 15;
                                        } else {
                                            i = 17;
                                        }
                                        final String str112 = str4;
                                        zzS = zzS(new Callable() { // from class: com.android.billingclient.api.zzs
                                            @Override // java.util.concurrent.Callable
                                            public final Object call() {
                                                return BillingClientImpl.this.zzc(i, str112, str10, billingFlowParams, bundle);
                                            }
                                        }, 5000L, null, billingClientImpl.zzc);
                                    }
                                } else {
                                    bundle.putString("skuPackageName", skuDetails.zzd());
                                }
                                str6 = null;
                                z2 = true;
                                if (!TextUtils.isEmpty(str6)) {
                                }
                                intent = activity.getIntent();
                                if (intent != null) {
                                }
                                if (billingClientImpl.zzt) {
                                }
                                if (billingClientImpl.zzr) {
                                }
                                final String str1122 = str4;
                                zzS = zzS(new Callable() { // from class: com.android.billingclient.api.zzs
                                    @Override // java.util.concurrent.Callable
                                    public final Object call() {
                                        return BillingClientImpl.this.zzc(i, str1122, str10, billingFlowParams, bundle);
                                    }
                                }, 5000L, null, billingClientImpl.zzc);
                            } else {
                                zzar zzarVar2 = billingClientImpl.zzf;
                                BillingResult billingResult2 = zzat.zzu;
                                zzarVar2.zza(zzaq.zza(21, 2, billingResult2));
                                billingClientImpl.zzP(billingResult2);
                                return billingResult2;
                            }
                        } else {
                            str = "BUY_INTENT";
                            str2 = "BillingClient";
                            zzS = zzS(new Callable() { // from class: com.android.billingclient.api.zzt
                                @Override // java.util.concurrent.Callable
                                public final Object call() {
                                    return BillingClientImpl.this.zzd(productId, productType);
                                }
                            }, 5000L, null, billingClientImpl.zzc);
                        }
                        try {
                            Bundle bundle22 = (Bundle) zzS.get(5000L, TimeUnit.MILLISECONDS);
                            zzb = zzb.zzb(bundle22, str2);
                            String zzf2 = zzb.zzf(bundle22, str2);
                            if (zzb == 0) {
                                zzb.zzj(str2, "Unable to buy item, Error response code: " + zzb);
                                BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                newBuilder.setResponseCode(zzb);
                                newBuilder.setDebugMessage(zzf2);
                                BillingResult build = newBuilder.build();
                                billingClientImpl.zzf.zza(zzaq.zza(3, 2, build));
                                billingClientImpl.zzP(build);
                                return build;
                            }
                            Intent intent2 = new Intent(activity, ProxyBillingActivity.class);
                            String str12 = str;
                            intent2.putExtra(str12, (PendingIntent) bundle22.getParcelable(str12));
                            activity.startActivity(intent2);
                            return zzat.zzl;
                        } catch (CancellationException e) {
                            e = e;
                            zzb.zzk(str2, "Time out while launching billing flow. Try to reconnect", e);
                            zzar zzarVar3 = billingClientImpl.zzf;
                            BillingResult billingResult3 = zzat.zzn;
                            zzarVar3.zza(zzaq.zza(4, 2, billingResult3));
                            billingClientImpl.zzP(billingResult3);
                            return billingResult3;
                        } catch (TimeoutException e2) {
                            e = e2;
                            zzb.zzk(str2, "Time out while launching billing flow. Try to reconnect", e);
                            zzar zzarVar32 = billingClientImpl.zzf;
                            BillingResult billingResult32 = zzat.zzn;
                            zzarVar32.zza(zzaq.zza(4, 2, billingResult32));
                            billingClientImpl.zzP(billingResult32);
                            return billingResult32;
                        } catch (Exception e3) {
                            zzb.zzk(str2, "Exception while launching billing flow. Try to reconnect", e3);
                            zzar zzarVar4 = billingClientImpl.zzf;
                            BillingResult billingResult4 = zzat.zzm;
                            zzarVar4.zza(zzaq.zza(5, 2, billingResult4));
                            billingClientImpl.zzP(billingResult4);
                            return billingResult4;
                        }
                    }
                    zzb.zzj("BillingClient", "Current client doesn't support purchases with ProductDetails.");
                    zzar zzarVar5 = billingClientImpl.zzf;
                    BillingResult billingResult5 = zzat.zzv;
                    zzarVar5.zza(zzaq.zza(20, 2, billingResult5));
                    billingClientImpl.zzP(billingResult5);
                    return billingResult5;
                }
                zzb.zzj("BillingClient", "Current client doesn't support multi-item purchases.");
                zzar zzarVar6 = billingClientImpl.zzf;
                BillingResult billingResult6 = zzat.zzt;
                zzarVar6.zza(zzaq.zza(19, 2, billingResult6));
                billingClientImpl.zzP(billingResult6);
                return billingResult6;
            }
            zzb.zzj("BillingClient", "Current client doesn't support extra params for buy intent.");
            zzar zzarVar7 = billingClientImpl.zzf;
            BillingResult billingResult7 = zzat.zzh;
            zzarVar7.zza(zzaq.zza(18, 2, billingResult7));
            billingClientImpl.zzP(billingResult7);
            return billingResult7;
        }
        zzb.zzj("BillingClient", "Current client doesn't support subscriptions.");
        zzar zzarVar8 = billingClientImpl.zzf;
        BillingResult billingResult8 = zzat.zzo;
        zzarVar8.zza(zzaq.zza(9, 2, billingResult8));
        billingClientImpl.zzP(billingResult8);
        return billingResult8;
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void queryProductDetailsAsync(final QueryProductDetailsParams queryProductDetailsParams, final ProductDetailsResponseListener productDetailsResponseListener) {
        if (!isReady()) {
            zzar zzarVar = this.zzf;
            BillingResult billingResult = zzat.zzm;
            zzarVar.zza(zzaq.zza(2, 7, billingResult));
            productDetailsResponseListener.onProductDetailsResponse(billingResult, new ArrayList());
        } else if (!this.zzt) {
            zzb.zzj("BillingClient", "Querying product details is not supported.");
            zzar zzarVar2 = this.zzf;
            BillingResult billingResult2 = zzat.zzv;
            zzarVar2.zza(zzaq.zza(20, 7, billingResult2));
            productDetailsResponseListener.onProductDetailsResponse(billingResult2, new ArrayList());
        } else if (zzS(new Callable() { // from class: com.android.billingclient.api.zzk
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
    }

    @Override // com.android.billingclient.api.BillingClient
    public final void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener purchasesResponseListener) {
        zzU(queryPurchasesParams.zza(), purchasesResponseListener);
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
    public final /* synthetic */ Bundle zzc(int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) throws Exception {
        return this.zzg.zzg(i, this.zze.getPackageName(), str, str2, null, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Bundle zzd(String str, String str2) throws Exception {
        return this.zzg.zzf(3, this.zze.getPackageName(), str, str2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ Object zzk(ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener) throws Exception {
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
                consumeResponseListener.onConsumeResponse(build, purchaseToken);
                return null;
            }
            zzb.zzj("BillingClient", "Error consuming purchase with token. Response code: " + zza);
            this.zzf.zza(zzaq.zza(23, 4, build));
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
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0121, code lost:
        r12 = 4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final /* synthetic */ Object zzl(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) throws Exception {
        String str;
        int i;
        int i2;
        zze zzeVar;
        int i3;
        String packageName;
        Bundle bundle;
        com.google.android.gms.internal.play_billing.zzu zzuVar;
        ArrayList arrayList = new ArrayList();
        String zzb = queryProductDetailsParams.zzb();
        com.google.android.gms.internal.play_billing.zzu zza = queryProductDetailsParams.zza();
        int size = zza.size();
        int i4 = 0;
        while (true) {
            if (i4 >= size) {
                str = "";
                i = 0;
                break;
            }
            int i5 = i4 + 20;
            ArrayList arrayList2 = new ArrayList(zza.subList(i4, i5 > size ? size : i5));
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
                zzuVar = zza;
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
                if (zzl != null) {
                    if (zzl.containsKey("DETAILS_LIST")) {
                        ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                        if (stringArrayList != null) {
                            for (int i9 = 0; i9 < stringArrayList.size(); i9++) {
                                try {
                                    ProductDetails productDetails = new ProductDetails(stringArrayList.get(i9));
                                    zzb.zzi("BillingClient", "Got product details: ".concat(productDetails.toString()));
                                    arrayList.add(productDetails);
                                } catch (JSONException e2) {
                                    zzb.zzk("BillingClient", "Got a JSON exception trying to decode ProductDetails. \n Exception: ", e2);
                                    zzar zzarVar = this.zzf;
                                    BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                    newBuilder.setResponseCode(6);
                                    str = "Error trying to decode SkuDetails.";
                                    newBuilder.setDebugMessage("Error trying to decode SkuDetails.");
                                    zzarVar.zza(zzaq.zza(47, 7, newBuilder.build()));
                                    i = 6;
                                    BillingResult.Builder newBuilder2 = BillingResult.newBuilder();
                                    newBuilder2.setResponseCode(i);
                                    newBuilder2.setDebugMessage(str);
                                    productDetailsResponseListener.onProductDetailsResponse(newBuilder2.build(), arrayList);
                                    return null;
                                }
                            }
                            i4 = i5;
                            zza = zzuVar;
                        } else {
                            zzb.zzj("BillingClient", "queryProductDetailsAsync got null response list");
                            this.zzf.zza(zzaq.zza(46, 7, zzat.zzB));
                            break;
                        }
                    } else {
                        i = zzb.zzb(zzl, "BillingClient");
                        str = zzb.zzf(zzl, "BillingClient");
                        if (i != 0) {
                            zzb.zzj("BillingClient", "getSkuDetails() failed for queryProductDetailsAsync. Response code: " + i);
                            this.zzf.zza(zzaq.zza(23, 7, zzat.zza(i, str)));
                        } else {
                            zzb.zzj("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a product detail list for queryProductDetailsAsync.");
                            zzar zzarVar2 = this.zzf;
                            BillingResult.Builder newBuilder3 = BillingResult.newBuilder();
                            newBuilder3.setResponseCode(6);
                            newBuilder3.setDebugMessage(str);
                            zzarVar2.zza(zzaq.zza(45, 7, newBuilder3.build()));
                        }
                    }
                } else {
                    zzb.zzj("BillingClient", "queryProductDetailsAsync got empty product details response.");
                    zzar zzarVar3 = this.zzf;
                    BillingResult.Builder newBuilder4 = BillingResult.newBuilder();
                    newBuilder4.setResponseCode(4);
                    newBuilder4.setDebugMessage("Item is unavailable for purchase.");
                    zzarVar3.zza(zzaq.zza(44, 7, newBuilder4.build()));
                    break;
                }
            } catch (Exception e3) {
                e = e3;
                zzb.zzk("BillingClient", "queryProductDetailsAsync got a remote exception (try to reconnect).", e);
                this.zzf.zza(zzaq.zza(43, i2, zzat.zzj));
                str = "An internal error occurred.";
                i = 6;
                BillingResult.Builder newBuilder22 = BillingResult.newBuilder();
                newBuilder22.setResponseCode(i);
                newBuilder22.setDebugMessage(str);
                productDetailsResponseListener.onProductDetailsResponse(newBuilder22.build(), arrayList);
                return null;
            }
        }
        BillingResult.Builder newBuilder222 = BillingResult.newBuilder();
        newBuilder222.setResponseCode(i);
        newBuilder222.setDebugMessage(str);
        productDetailsResponseListener.onProductDetailsResponse(newBuilder222.build(), arrayList);
        return null;
    }

    private BillingClientImpl(Context context, zzbe zzbeVar, PurchasesUpdatedListener purchasesUpdatedListener, String str, String str2, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzk = 0;
        this.zzb = str;
        initialize(context, purchasesUpdatedListener, zzbeVar, alternativeBillingListener, str, null);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public BillingClientImpl(String str, zzbe zzbeVar, Context context, PurchasesUpdatedListener purchasesUpdatedListener, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this(context, zzbeVar, purchasesUpdatedListener, zzR(), null, alternativeBillingListener, null);
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
}
