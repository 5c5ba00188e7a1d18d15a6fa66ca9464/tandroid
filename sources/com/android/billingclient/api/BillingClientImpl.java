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
import com.google.android.gms.internal.play_billing.zzz;
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
/* loaded from: classes.dex */
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

    private void initialize(Context context, PurchasesUpdatedListener purchasesUpdatedListener, boolean z, zzc zzcVar) {
        Context applicationContext = context.getApplicationContext();
        this.zze = applicationContext;
        this.zzd = new zzo(applicationContext, purchasesUpdatedListener, zzcVar);
        this.zzt = z;
        this.zzu = zzcVar != null;
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

    @SuppressLint({"PrivateApi"})
    private static String zzI() {
        try {
            return (String) Class.forName("com.android.billingclient.ktx.BuildConfig").getField("VERSION_NAME").get(null);
        } catch (Exception unused) {
            return "5.0.0";
        }
    }

    public final Future zzJ(Callable callable, long j, final Runnable runnable, Handler handler) {
        double d = j;
        Double.isNaN(d);
        long j2 = (long) (d * 0.95d);
        if (this.zzv == null) {
            this.zzv = Executors.newFixedThreadPool(zzb.zza, new zzal(this));
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
                    zzb.zzo("BillingClient", "Async task is taking too long, cancel it!");
                    if (runnable2 == null) {
                        return;
                    }
                    runnable2.run();
                }
            }, j2);
            return submit;
        } catch (Exception e) {
            zzb.zzp("BillingClient", "Async task throws exception!", e);
            return null;
        }
    }

    private final void zzM(String str, final PurchasesResponseListener purchasesResponseListener) {
        if (!isReady()) {
            purchasesResponseListener.onQueryPurchasesResponse(zzbb.zzm, com.google.android.gms.internal.play_billing.zzu.zzl());
        } else if (TextUtils.isEmpty(str)) {
            zzb.zzo("BillingClient", "Please provide a valid product type.");
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

    public static /* bridge */ /* synthetic */ zzbh zzi(BillingClientImpl billingClientImpl, String str) {
        Bundle zzi;
        zzb.zzn("BillingClient", "Querying owned items, item type: ".concat(String.valueOf(str)));
        ArrayList arrayList = new ArrayList();
        Bundle zzh = zzb.zzh(billingClientImpl.zzm, billingClientImpl.zzt, billingClientImpl.zzb);
        String str2 = null;
        do {
            try {
                if (billingClientImpl.zzm) {
                    zzi = billingClientImpl.zzf.zzj(9, billingClientImpl.zze.getPackageName(), str, str2, zzh);
                } else {
                    zzi = billingClientImpl.zzf.zzi(3, billingClientImpl.zze.getPackageName(), str, str2);
                }
                BillingResult zza = zzbi.zza(zzi, "BillingClient", "getPurchase()");
                if (zza != zzbb.zzl) {
                    return new zzbh(zza, null);
                }
                ArrayList<String> stringArrayList = zzi.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> stringArrayList2 = zzi.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> stringArrayList3 = zzi.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                for (int i = 0; i < stringArrayList2.size(); i++) {
                    String str3 = stringArrayList2.get(i);
                    String str4 = stringArrayList3.get(i);
                    zzb.zzn("BillingClient", "Sku is owned: ".concat(String.valueOf(stringArrayList.get(i))));
                    try {
                        Purchase purchase = new Purchase(str3, str4);
                        if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
                            zzb.zzo("BillingClient", "BUG: empty/null token!");
                        }
                        arrayList.add(purchase);
                    } catch (JSONException e) {
                        zzb.zzp("BillingClient", "Got an exception trying to decode the purchase!", e);
                        return new zzbh(zzbb.zzj, null);
                    }
                }
                str2 = zzi.getString("INAPP_CONTINUATION_TOKEN");
                zzb.zzn("BillingClient", "Continuation token: ".concat(String.valueOf(str2)));
            } catch (Exception e2) {
                zzb.zzp("BillingClient", "Got exception trying to get purchasesm try to reconnect", e2);
                return new zzbh(zzbb.zzm, null);
            }
        } while (!TextUtils.isEmpty(str2));
        return new zzbh(zzbb.zzl, arrayList);
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
    public final boolean isReady() {
        return (this.zza != 2 || this.zzf == null || this.zzg == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0386 A[Catch: Exception -> 0x03a0, CancellationException -> 0x03ac, TimeoutException -> 0x03ae, TRY_LEAVE, TryCatch #4 {CancellationException -> 0x03ac, TimeoutException -> 0x03ae, Exception -> 0x03a0, blocks: (B:97:0x034e, B:99:0x0360, B:101:0x0386), top: B:96:0x034e }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0307 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0317  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0360 A[Catch: Exception -> 0x03a0, CancellationException -> 0x03ac, TimeoutException -> 0x03ae, TryCatch #4 {CancellationException -> 0x03ac, TimeoutException -> 0x03ae, Exception -> 0x03a0, blocks: (B:97:0x034e, B:99:0x0360, B:101:0x0386), top: B:96:0x034e }] */
    @Override // com.android.billingclient.api.BillingClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final BillingResult launchBillingFlow(Activity activity, final BillingFlowParams billingFlowParams) {
        final String productId;
        final String productType;
        String str;
        String str2;
        Future zzJ;
        int zzb;
        String str3;
        SkuDetails skuDetails;
        BillingFlowParams.ProductDetailsParams productDetailsParams;
        String str4;
        String str5;
        String str6;
        String str7;
        boolean z;
        Intent intent;
        String str8;
        final int i;
        String str9;
        if (!isReady()) {
            BillingResult billingResult = zzbb.zzm;
            zzG(billingResult);
            return billingResult;
        }
        ArrayList<SkuDetails> zze = billingFlowParams.zze();
        List zzf = billingFlowParams.zzf();
        SkuDetails skuDetails2 = (SkuDetails) zzz.zza(zze, null);
        BillingFlowParams.ProductDetailsParams productDetailsParams2 = (BillingFlowParams.ProductDetailsParams) zzz.zza(zzf, null);
        if (skuDetails2 != null) {
            productId = skuDetails2.getSku();
            productType = skuDetails2.getType();
        } else {
            productId = productDetailsParams2.zza().getProductId();
            productType = productDetailsParams2.zza().getProductType();
        }
        String str10 = "BillingClient";
        if (!productType.equals("subs") || this.zzh) {
            if (!billingFlowParams.zzo() || this.zzk) {
                if (zze.size() <= 1 || this.zzr) {
                    if (zzf.isEmpty() || this.zzs) {
                        if (this.zzk) {
                            final Bundle zzf2 = zzb.zzf(billingFlowParams, this.zzm, this.zzt, this.zzu, this.zzb);
                            str = "BUY_INTENT";
                            if (!zze.isEmpty()) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayList2 = new ArrayList<>();
                                str5 = productType;
                                ArrayList<String> arrayList3 = new ArrayList<>();
                                str4 = productId;
                                ArrayList<Integer> arrayList4 = new ArrayList<>();
                                str3 = "proxyPackageVersion";
                                ArrayList<String> arrayList5 = new ArrayList<>();
                                boolean z2 = false;
                                boolean z3 = false;
                                boolean z4 = false;
                                boolean z5 = false;
                                for (SkuDetails skuDetails3 : zze) {
                                    if (!skuDetails3.zzf().isEmpty()) {
                                        str9 = str10;
                                        arrayList.add(skuDetails3.zzf());
                                    } else {
                                        str9 = str10;
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
                                    str10 = str9;
                                    productDetailsParams2 = productDetailsParams3;
                                    skuDetails2 = skuDetails4;
                                }
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str6 = str10;
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
                                str3 = "proxyPackageVersion";
                                skuDetails = skuDetails2;
                                productDetailsParams = productDetailsParams2;
                                str4 = productId;
                                str5 = productType;
                                str6 = str10;
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
                                if (skuDetails == null || TextUtils.isEmpty(skuDetails.zzd())) {
                                    if (productDetailsParams != null && !TextUtils.isEmpty(productDetailsParams.zza().zza())) {
                                        zzf2.putString("skuPackageName", productDetailsParams.zza().zza());
                                    } else {
                                        str7 = null;
                                        z = false;
                                        if (!TextUtils.isEmpty(str7)) {
                                            zzf2.putString("accountName", str7);
                                        }
                                        intent = activity.getIntent();
                                        if (intent != null) {
                                            str2 = str6;
                                            zzb.zzo(str2, "Activity's intent is null.");
                                        } else {
                                            str2 = str6;
                                            if (!TextUtils.isEmpty(intent.getStringExtra("PROXY_PACKAGE"))) {
                                                String stringExtra = intent.getStringExtra("PROXY_PACKAGE");
                                                zzf2.putString("proxyPackage", stringExtra);
                                                try {
                                                    str8 = str3;
                                                    try {
                                                        zzf2.putString(str8, this.zze.getPackageManager().getPackageInfo(stringExtra, 0).versionName);
                                                    } catch (PackageManager.NameNotFoundException unused) {
                                                        zzf2.putString(str8, "package not found");
                                                        if (this.zzs) {
                                                        }
                                                        if (this.zzq) {
                                                        }
                                                        final String str11 = str4;
                                                        final String str12 = str5;
                                                        zzJ = zzJ(new Callable() { // from class: com.android.billingclient.api.zzab
                                                            @Override // java.util.concurrent.Callable
                                                            public final Object call() {
                                                                return BillingClientImpl.this.zzc(i, str11, str12, billingFlowParams, zzf2);
                                                            }
                                                        }, 5000L, null, this.zzc);
                                                        Bundle bundle = (Bundle) zzJ.get(5000L, TimeUnit.MILLISECONDS);
                                                        zzb = zzb.zzb(bundle, str2);
                                                        String zzk = zzb.zzk(bundle, str2);
                                                        if (zzb == 0) {
                                                        }
                                                    }
                                                } catch (PackageManager.NameNotFoundException unused2) {
                                                    str8 = str3;
                                                }
                                            }
                                        }
                                        if (this.zzs || zzf.isEmpty()) {
                                            i = (this.zzq || !z) ? this.zzm ? 9 : 6 : 15;
                                        } else {
                                            i = 17;
                                        }
                                        final String str112 = str4;
                                        final String str122 = str5;
                                        zzJ = zzJ(new Callable() { // from class: com.android.billingclient.api.zzab
                                            @Override // java.util.concurrent.Callable
                                            public final Object call() {
                                                return BillingClientImpl.this.zzc(i, str112, str122, billingFlowParams, zzf2);
                                            }
                                        }, 5000L, null, this.zzc);
                                    }
                                } else {
                                    zzf2.putString("skuPackageName", skuDetails.zzd());
                                }
                                str7 = null;
                                z = true;
                                if (!TextUtils.isEmpty(str7)) {
                                }
                                intent = activity.getIntent();
                                if (intent != null) {
                                }
                                if (this.zzs) {
                                }
                                if (this.zzq) {
                                }
                                final String str1122 = str4;
                                final String str1222 = str5;
                                zzJ = zzJ(new Callable() { // from class: com.android.billingclient.api.zzab
                                    @Override // java.util.concurrent.Callable
                                    public final Object call() {
                                        return BillingClientImpl.this.zzc(i, str1122, str1222, billingFlowParams, zzf2);
                                    }
                                }, 5000L, null, this.zzc);
                            } else {
                                BillingResult billingResult2 = zzbb.zzu;
                                zzG(billingResult2);
                                return billingResult2;
                            }
                        } else {
                            str = "BUY_INTENT";
                            str2 = str10;
                            zzJ = zzJ(new Callable() { // from class: com.android.billingclient.api.zzac
                                @Override // java.util.concurrent.Callable
                                public final Object call() {
                                    return BillingClientImpl.this.zzd(productId, productType);
                                }
                            }, 5000L, null, this.zzc);
                        }
                        try {
                            Bundle bundle2 = (Bundle) zzJ.get(5000L, TimeUnit.MILLISECONDS);
                            zzb = zzb.zzb(bundle2, str2);
                            String zzk2 = zzb.zzk(bundle2, str2);
                            if (zzb == 0) {
                                zzb.zzo(str2, "Unable to buy item, Error response code: " + zzb);
                                BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                newBuilder.setResponseCode(zzb);
                                newBuilder.setDebugMessage(zzk2);
                                BillingResult build = newBuilder.build();
                                zzG(build);
                                return build;
                            }
                            Intent intent2 = new Intent(activity, ProxyBillingActivity.class);
                            String str13 = str;
                            intent2.putExtra(str13, (PendingIntent) bundle2.getParcelable(str13));
                            activity.startActivity(intent2);
                            return zzbb.zzl;
                        } catch (CancellationException e) {
                            e = e;
                            zzb.zzp(str2, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult3 = zzbb.zzn;
                            zzG(billingResult3);
                            return billingResult3;
                        } catch (TimeoutException e2) {
                            e = e2;
                            zzb.zzp(str2, "Time out while launching billing flow. Try to reconnect", e);
                            BillingResult billingResult32 = zzbb.zzn;
                            zzG(billingResult32);
                            return billingResult32;
                        } catch (Exception e3) {
                            zzb.zzp(str2, "Exception while launching billing flow. Try to reconnect", e3);
                            BillingResult billingResult4 = zzbb.zzm;
                            zzG(billingResult4);
                            return billingResult4;
                        }
                    }
                    zzb.zzo(str10, "Current client doesn't support purchases with ProductDetails.");
                    BillingResult billingResult5 = zzbb.zzv;
                    zzG(billingResult5);
                    return billingResult5;
                }
                zzb.zzo(str10, "Current client doesn't support multi-item purchases.");
                BillingResult billingResult6 = zzbb.zzt;
                zzG(billingResult6);
                return billingResult6;
            }
            zzb.zzo(str10, "Current client doesn't support extra params for buy intent.");
            BillingResult billingResult7 = zzbb.zzh;
            zzG(billingResult7);
            return billingResult7;
        }
        zzb.zzo(str10, "Current client doesn't support subscriptions.");
        BillingResult billingResult8 = zzbb.zzo;
        zzG(billingResult8);
        return billingResult8;
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryProductDetailsAsync(final QueryProductDetailsParams queryProductDetailsParams, final ProductDetailsResponseListener productDetailsResponseListener) {
        if (!isReady()) {
            productDetailsResponseListener.onProductDetailsResponse(zzbb.zzm, new ArrayList());
        } else if (!this.zzs) {
            zzb.zzo("BillingClient", "Querying product details is not supported.");
            productDetailsResponseListener.onProductDetailsResponse(zzbb.zzv, new ArrayList());
        } else if (zzJ(new Callable() { // from class: com.android.billingclient.api.zzs
            @Override // java.util.concurrent.Callable
            public final Object call() {
                BillingClientImpl.this.zzm(queryProductDetailsParams, productDetailsResponseListener);
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzt
            @Override // java.lang.Runnable
            public final void run() {
                ProductDetailsResponseListener.this.onProductDetailsResponse(zzbb.zzn, new ArrayList());
            }
        }, zzF()) != null) {
        } else {
            productDetailsResponseListener.onProductDetailsResponse(zzH(), new ArrayList());
        }
    }

    @Override // com.android.billingclient.api.BillingClient
    public void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener purchasesResponseListener) {
        zzM(queryPurchasesParams.zza(), purchasesResponseListener);
    }

    public final /* synthetic */ void zzE(BillingResult billingResult) {
        if (this.zzd.zzc() != null) {
            this.zzd.zzc().onPurchasesUpdated(billingResult, null);
            return;
        }
        this.zzd.zzb();
        zzb.zzo("BillingClient", "No valid listener is set in BroadcastManager");
    }

    public final /* synthetic */ Bundle zzc(int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) throws Exception {
        return this.zzf.zzg(i, this.zze.getPackageName(), str, str2, null, bundle);
    }

    public final /* synthetic */ Bundle zzd(String str, String str2) throws Exception {
        return this.zzf.zzf(3, this.zze.getPackageName(), str, str2, null);
    }

    public final /* synthetic */ Object zzl(ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener) throws Exception {
        int zza;
        String str;
        String purchaseToken = consumeParams.getPurchaseToken();
        try {
            zzb.zzn("BillingClient", "Consuming purchase with token: " + purchaseToken);
            if (this.zzm) {
                Bundle zze = this.zzf.zze(9, this.zze.getPackageName(), purchaseToken, zzb.zzd(consumeParams, this.zzm, this.zzb));
                zza = zze.getInt("RESPONSE_CODE");
                str = zzb.zzk(zze, "BillingClient");
            } else {
                zza = this.zzf.zza(3, this.zze.getPackageName(), purchaseToken);
                str = "";
            }
            BillingResult.Builder newBuilder = BillingResult.newBuilder();
            newBuilder.setResponseCode(zza);
            newBuilder.setDebugMessage(str);
            BillingResult build = newBuilder.build();
            if (zza == 0) {
                zzb.zzn("BillingClient", "Successfully consumed purchase.");
                consumeResponseListener.onConsumeResponse(build, purchaseToken);
                return null;
            }
            zzb.zzo("BillingClient", "Error consuming purchase with token. Response code: " + zza);
            consumeResponseListener.onConsumeResponse(build, purchaseToken);
            return null;
        } catch (Exception e) {
            zzb.zzp("BillingClient", "Error consuming purchase!", e);
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
                Bundle zzl = this.zzf.zzl(17, this.zze.getPackageName(), zzb, bundle, zzb.zzg(this.zzb, arrayList2, null));
                if (zzl != null) {
                    if (zzl.containsKey("DETAILS_LIST")) {
                        ArrayList<String> stringArrayList = zzl.getStringArrayList("DETAILS_LIST");
                        if (stringArrayList != null) {
                            for (int i5 = 0; i5 < stringArrayList.size(); i5++) {
                                try {
                                    ProductDetails productDetails = new ProductDetails(stringArrayList.get(i5));
                                    zzb.zzn("BillingClient", "Got product details: ".concat(productDetails.toString()));
                                    arrayList.add(productDetails);
                                } catch (JSONException e) {
                                    zzb.zzp("BillingClient", "Got a JSON exception trying to decode ProductDetails. \n Exception: ", e);
                                    str = "Error trying to decode SkuDetails.";
                                    i = 6;
                                    BillingResult.Builder newBuilder = BillingResult.newBuilder();
                                    newBuilder.setResponseCode(i);
                                    newBuilder.setDebugMessage(str);
                                    productDetailsResponseListener.onProductDetailsResponse(newBuilder.build(), arrayList);
                                    return null;
                                }
                            }
                            i2 = i3;
                        } else {
                            zzb.zzo("BillingClient", "queryProductDetailsAsync got null response list");
                            break;
                        }
                    } else {
                        i = zzb.zzb(zzl, "BillingClient");
                        str = zzb.zzk(zzl, "BillingClient");
                        if (i != 0) {
                            zzb.zzo("BillingClient", "getSkuDetails() failed for queryProductDetailsAsync. Response code: " + i);
                        } else {
                            zzb.zzo("BillingClient", "getSkuDetails() returned a bundle with neither an error nor a product detail list for queryProductDetailsAsync.");
                        }
                    }
                } else {
                    zzb.zzo("BillingClient", "queryProductDetailsAsync got empty product details response.");
                    break;
                }
            } catch (Exception e2) {
                zzb.zzp("BillingClient", "queryProductDetailsAsync got a remote exception (try to reconnect).", e2);
                str = "An internal error occurred.";
            }
        }
        i = 4;
        BillingResult.Builder newBuilder2 = BillingResult.newBuilder();
        newBuilder2.setResponseCode(i);
        newBuilder2.setDebugMessage(str);
        productDetailsResponseListener.onProductDetailsResponse(newBuilder2.build(), arrayList);
        return null;
    }

    private BillingClientImpl(Context context, boolean z, PurchasesUpdatedListener purchasesUpdatedListener, String str, String str2, zzc zzcVar) {
        this.zza = 0;
        this.zzc = new Handler(Looper.getMainLooper());
        this.zzj = 0;
        this.zzb = str;
        initialize(context, purchasesUpdatedListener, z, null);
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
        ServiceInfo serviceInfo;
        if (isReady()) {
            zzb.zzn("BillingClient", "Service connection is valid. No need to re-initialize.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzl);
        } else if (this.zza == 1) {
            zzb.zzo("BillingClient", "Client is already in the process of connecting to billing service.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzd);
        } else if (this.zza == 3) {
            zzb.zzo("BillingClient", "Client was already closed and can't be reused. Please create another instance.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzm);
        } else {
            this.zza = 1;
            this.zzd.zze();
            zzb.zzn("BillingClient", "Starting in-app billing setup.");
            this.zzg = new zzap(this, billingClientStateListener, null);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.zze.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty() && (serviceInfo = queryIntentServices.get(0).serviceInfo) != null) {
                String str = serviceInfo.packageName;
                String str2 = serviceInfo.name;
                if (!"com.android.vending".equals(str) || str2 == null) {
                    zzb.zzo("BillingClient", "The device doesn't have valid Play Store.");
                } else {
                    ComponentName componentName = new ComponentName(str, str2);
                    Intent intent2 = new Intent(intent);
                    intent2.setComponent(componentName);
                    intent2.putExtra("playBillingLibraryVersion", this.zzb);
                    if (this.zze.bindService(intent2, this.zzg, 1)) {
                        zzb.zzn("BillingClient", "Service was bonded successfully.");
                        return;
                    }
                    zzb.zzo("BillingClient", "Connection to Billing service is blocked.");
                }
            }
            this.zza = 0;
            zzb.zzn("BillingClient", "Billing service unavailable on device.");
            billingClientStateListener.onBillingSetupFinished(zzbb.zzc);
        }
    }
}
