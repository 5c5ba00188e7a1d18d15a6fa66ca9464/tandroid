package com.google.mlkit.vision.label.defaults.thin;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.internal.mlkit_vision_label.zzl;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.ImageConvertUtils;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
final class zzc implements zzb {
    private final Context zza;
    private final com.google.android.gms.internal.mlkit_vision_label.zzj zzb;
    private final String zzc;
    private boolean zzd;
    private com.google.android.gms.internal.mlkit_vision_label.zzd zze;

    zzc(Context context, ImageLabelerOptions imageLabelerOptions) {
        this.zza = context;
        Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage());
        this.zzb = new com.google.android.gms.internal.mlkit_vision_label.zzj(1, -1, imageLabelerOptions.getConfidenceThreshold(), 1);
        this.zzc = GoogleApiAvailabilityLight.getInstance().getApkVersion(context) >= 200400000 ? "com.google.android.gms.vision.ica" : "com.google.android.gms.vision.dynamite";
    }

    private final void zzd() {
        if (this.zzd) {
            return;
        }
        Log.d("LegacyLabelDelegate", "Request ICA optional module download.");
        OptionalModuleUtils.requestDownload(this.zza, "ica");
        this.zzd = true;
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final List zza(InputImage inputImage) {
        if (this.zze == null) {
            zzb();
        }
        if (this.zze == null) {
            throw new MlKitException("Waiting for the image labeling module to be downloaded. Please wait.", 14);
        }
        try {
            com.google.android.gms.internal.mlkit_vision_label.zzh[] zze = ((com.google.android.gms.internal.mlkit_vision_label.zzd) Preconditions.checkNotNull(this.zze)).zze(ObjectWrapper.wrap(ImageConvertUtils.getInstance().convertToUpRightBitmap(inputImage)), new zzl(-1));
            ArrayList arrayList = new ArrayList();
            for (com.google.android.gms.internal.mlkit_vision_label.zzh zzhVar : zze) {
                arrayList.add(new ImageLabel(zzhVar.zzb, zzhVar.zzc, zzhVar.zzd, zzhVar.zza));
            }
            return arrayList;
        } catch (RemoteException e) {
            throw new MlKitException("Failed to run legacy image labeler.", 13, e);
        }
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final void zzb() {
        Log.d("LegacyLabelDelegate", "Try to load legacy label module.");
        if (this.zze != null) {
            return;
        }
        try {
            com.google.android.gms.internal.mlkit_vision_label.zzd zzd = com.google.android.gms.internal.mlkit_vision_label.zzf.zza(DynamiteModule.load(this.zza, DynamiteModule.PREFER_REMOTE, this.zzc).instantiate("com.google.android.gms.vision.label.ChimeraNativeImageLabelerCreator")).zzd(ObjectWrapper.wrap(this.zza), this.zzb);
            this.zze = zzd;
            if (zzd == null) {
                zzd();
            }
        } catch (RemoteException e) {
            throw new MlKitException("Failed to create legacy image labeler.", 13, e);
        } catch (DynamiteModule.LoadingException e2) {
            if (this.zzc.equals("com.google.android.gms.vision.dynamite")) {
                throw new MlKitException("Failed to load deprecated vision dynamite module.", 13, e2);
            }
            zzd();
        }
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final void zzc() {
        com.google.android.gms.internal.mlkit_vision_label.zzd zzdVar = this.zze;
        if (zzdVar != null) {
            try {
                zzdVar.zzd();
            } catch (RemoteException e) {
                Log.e("LegacyLabelDelegate", "Failed to release legacy image labeler.", e);
            }
            this.zze = null;
        }
    }
}
