package com.google.firebase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo;
import com.google.firebase.platforminfo.DefaultUserAgentPublisher;
import com.google.firebase.platforminfo.KotlinDetector;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FirebaseCommonRegistrar implements ComponentRegistrar {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getComponents$0(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo != null ? String.valueOf(applicationInfo.targetSdkVersion) : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getComponents$1(Context context) {
        int i;
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (applicationInfo == null || Build.VERSION.SDK_INT < 24) {
            return "";
        }
        i = applicationInfo.minSdkVersion;
        return String.valueOf(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getComponents$2(Context context) {
        int i = Build.VERSION.SDK_INT;
        return context.getPackageManager().hasSystemFeature("android.hardware.type.television") ? "tv" : (i < 20 || !context.getPackageManager().hasSystemFeature("android.hardware.type.watch")) ? (i < 23 || !context.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) ? (i < 26 || !context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) ? "" : "embedded" : "auto" : "watch";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getComponents$3(Context context) {
        String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installerPackageName != null ? safeValue(installerPackageName) : "";
    }

    private static String safeValue(String str) {
        return str.replace(' ', '_').replace('/', '_');
    }

    @Override // com.google.firebase.components.ComponentRegistrar
    public List getComponents() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(DefaultUserAgentPublisher.component());
        arrayList.add(DefaultHeartBeatInfo.component());
        arrayList.add(LibraryVersionComponent.create("fire-android", String.valueOf(Build.VERSION.SDK_INT)));
        arrayList.add(LibraryVersionComponent.create("fire-core", "20.0.0"));
        arrayList.add(LibraryVersionComponent.create("device-name", safeValue(Build.PRODUCT)));
        arrayList.add(LibraryVersionComponent.create("device-model", safeValue(Build.DEVICE)));
        arrayList.add(LibraryVersionComponent.create("device-brand", safeValue(Build.BRAND)));
        arrayList.add(LibraryVersionComponent.fromContext("android-target-sdk", new LibraryVersionComponent.VersionExtractor() { // from class: com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda1
            @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
            public final String extract(Object obj) {
                String lambda$getComponents$0;
                lambda$getComponents$0 = FirebaseCommonRegistrar.lambda$getComponents$0((Context) obj);
                return lambda$getComponents$0;
            }
        }));
        arrayList.add(LibraryVersionComponent.fromContext("android-min-sdk", new LibraryVersionComponent.VersionExtractor() { // from class: com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda2
            @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
            public final String extract(Object obj) {
                String lambda$getComponents$1;
                lambda$getComponents$1 = FirebaseCommonRegistrar.lambda$getComponents$1((Context) obj);
                return lambda$getComponents$1;
            }
        }));
        arrayList.add(LibraryVersionComponent.fromContext("android-platform", new LibraryVersionComponent.VersionExtractor() { // from class: com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda3
            @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
            public final String extract(Object obj) {
                String lambda$getComponents$2;
                lambda$getComponents$2 = FirebaseCommonRegistrar.lambda$getComponents$2((Context) obj);
                return lambda$getComponents$2;
            }
        }));
        arrayList.add(LibraryVersionComponent.fromContext("android-installer", new LibraryVersionComponent.VersionExtractor() { // from class: com.google.firebase.FirebaseCommonRegistrar$$ExternalSyntheticLambda4
            @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
            public final String extract(Object obj) {
                String lambda$getComponents$3;
                lambda$getComponents$3 = FirebaseCommonRegistrar.lambda$getComponents$3((Context) obj);
                return lambda$getComponents$3;
            }
        }));
        String detectVersion = KotlinDetector.detectVersion();
        if (detectVersion != null) {
            arrayList.add(LibraryVersionComponent.create("kotlin", detectVersion));
        }
        return arrayList;
    }
}
