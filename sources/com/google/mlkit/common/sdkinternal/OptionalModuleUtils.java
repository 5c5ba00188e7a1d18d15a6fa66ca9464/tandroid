package com.google.mlkit.common.sdkinternal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.android.gms.internal.mlkit_common.zzaq;
import com.google.android.gms.internal.mlkit_common.zzas;
import com.google.android.gms.internal.mlkit_common.zzat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Tasks;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public class OptionalModuleUtils {
    public static final Feature[] EMPTY_FEATURES = new Feature[0];
    public static final Feature FEATURE_BARCODE;
    public static final Feature FEATURE_CUSTOM_ICA;
    public static final Feature FEATURE_FACE;
    public static final Feature FEATURE_ICA;
    public static final Feature FEATURE_LANGID;
    public static final Feature FEATURE_MLKIT_BARCODE_UI;
    public static final Feature FEATURE_NLCLASSIFIER;
    public static final Feature FEATURE_OCR;
    public static final Feature FEATURE_SMART_REPLY;
    public static final Feature FEATURE_SUBJECT_SEGMENTATION;
    public static final Feature FEATURE_TFLITE_DYNAMITE;
    private static final zzat zza;

    static {
        Feature feature = new Feature("vision.barcode", 1L);
        FEATURE_BARCODE = feature;
        Feature feature2 = new Feature("vision.custom.ica", 1L);
        FEATURE_CUSTOM_ICA = feature2;
        Feature feature3 = new Feature("vision.face", 1L);
        FEATURE_FACE = feature3;
        Feature feature4 = new Feature("vision.ica", 1L);
        FEATURE_ICA = feature4;
        Feature feature5 = new Feature("vision.ocr", 1L);
        FEATURE_OCR = feature5;
        new Feature("mlkit.ocr.chinese", 1L);
        new Feature("mlkit.ocr.common", 1L);
        new Feature("mlkit.ocr.devanagari", 1L);
        new Feature("mlkit.ocr.japanese", 1L);
        new Feature("mlkit.ocr.korean", 1L);
        Feature feature6 = new Feature("mlkit.langid", 1L);
        FEATURE_LANGID = feature6;
        Feature feature7 = new Feature("mlkit.nlclassifier", 1L);
        FEATURE_NLCLASSIFIER = feature7;
        Feature feature8 = new Feature("tflite_dynamite", 1L);
        FEATURE_TFLITE_DYNAMITE = feature8;
        Feature feature9 = new Feature("mlkit.barcode.ui", 1L);
        FEATURE_MLKIT_BARCODE_UI = feature9;
        Feature feature10 = new Feature("mlkit.smartreply", 1L);
        FEATURE_SMART_REPLY = feature10;
        new Feature("mlkit.image.caption", 1L);
        new Feature("mlkit.docscan.detect", 1L);
        new Feature("mlkit.docscan.crop", 1L);
        new Feature("mlkit.docscan.enhance", 1L);
        new Feature("mlkit.docscan.ui", 1L);
        new Feature("mlkit.docscan.stain", 1L);
        new Feature("mlkit.docscan.shadow", 1L);
        new Feature("mlkit.quality.aesthetic", 1L);
        new Feature("mlkit.quality.technical", 1L);
        FEATURE_SUBJECT_SEGMENTATION = new Feature("mlkit.segmentation.subject", 1L);
        zzas zzasVar = new zzas();
        zzasVar.zza("barcode", feature);
        zzasVar.zza("custom_ica", feature2);
        zzasVar.zza("face", feature3);
        zzasVar.zza("ica", feature4);
        zzasVar.zza("ocr", feature5);
        zzasVar.zza("langid", feature6);
        zzasVar.zza("nlclassifier", feature7);
        zzasVar.zza("tflite_dynamite", feature8);
        zzasVar.zza("barcode_ui", feature9);
        zzasVar.zza("smart_reply", feature10);
        zza = zzasVar.zzb();
        zzas zzasVar2 = new zzas();
        zzasVar2.zza("com.google.android.gms.vision.barcode", feature);
        zzasVar2.zza("com.google.android.gms.vision.custom.ica", feature2);
        zzasVar2.zza("com.google.android.gms.vision.face", feature3);
        zzasVar2.zza("com.google.android.gms.vision.ica", feature4);
        zzasVar2.zza("com.google.android.gms.vision.ocr", feature5);
        zzasVar2.zza("com.google.android.gms.mlkit.langid", feature6);
        zzasVar2.zza("com.google.android.gms.mlkit.nlclassifier", feature7);
        zzasVar2.zza("com.google.android.gms.tflite_dynamite", feature8);
        zzasVar2.zza("com.google.android.gms.mlkit_smartreply", feature10);
        zzasVar2.zzb();
    }

    @Deprecated
    public static void requestDownload(Context context, String str) {
        requestDownload(context, zzaq.zzh(str));
    }

    private static Feature[] zza(Map map, List list) {
        Feature[] featureArr = new Feature[list.size()];
        for (int i = 0; i < list.size(); i++) {
            featureArr[i] = (Feature) Preconditions.checkNotNull((Feature) map.get(list.get(i)));
        }
        return featureArr;
    }

    @Deprecated
    public static void requestDownload(Context context, List<String> list) {
        if (GoogleApiAvailabilityLight.getInstance().getApkVersion(context) >= 221500000) {
            requestDownload(context, zza(zza, list));
            return;
        }
        Intent intent = new Intent();
        intent.setClassName("com.google.android.gms", "com.google.android.gms.vision.DependencyBroadcastReceiverProxy");
        intent.setAction("com.google.android.gms.vision.DEPENDENCY");
        intent.putExtra("com.google.android.gms.vision.DEPENDENCIES", TextUtils.join(",", list));
        intent.putExtra("requester_app_package", context.getApplicationInfo().packageName);
        context.sendBroadcast(intent);
    }

    public static boolean areAllRequiredModulesAvailable(Context context, final Feature[] featureArr) {
        try {
            return ((ModuleAvailabilityResponse) Tasks.await(ModuleInstall.getClient(context).areModulesAvailable(new OptionalModuleApi() { // from class: com.google.mlkit.common.sdkinternal.zzq
                @Override // com.google.android.gms.common.api.OptionalModuleApi
                public final Feature[] getOptionalFeatures() {
                    Feature[] featureArr2 = OptionalModuleUtils.EMPTY_FEATURES;
                    return featureArr;
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.google.mlkit.common.sdkinternal.zzr
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    Log.e("OptionalModuleUtils", "Failed to check feature availability", exc);
                }
            }))).areModulesAvailable();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("OptionalModuleUtils", "Failed to complete the task of features availability check", e);
            return false;
        }
    }

    public static void requestDownload(Context context, final Feature[] featureArr) {
        ModuleInstall.getClient(context).installModules(ModuleInstallRequest.newBuilder().addApi(new OptionalModuleApi() { // from class: com.google.mlkit.common.sdkinternal.zzo
            @Override // com.google.android.gms.common.api.OptionalModuleApi
            public final Feature[] getOptionalFeatures() {
                Feature[] featureArr2 = OptionalModuleUtils.EMPTY_FEATURES;
                return featureArr;
            }
        }).build()).addOnFailureListener(new OnFailureListener() { // from class: com.google.mlkit.common.sdkinternal.zzp
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                Log.e("OptionalModuleUtils", "Failed to request modules install request", exc);
            }
        });
    }
}
