package com.google.mlkit.vision.label.defaults.thin;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.internal.mlkit_vision_label.zzod;
import com.google.android.gms.internal.mlkit_vision_label.zzof;
import com.google.android.gms.internal.mlkit_vision_label.zzoh;
import com.google.android.gms.internal.mlkit_vision_label.zzoj;
import com.google.android.gms.internal.mlkit_vision_label.zzol;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.CommonConvertUtils;
import com.google.mlkit.vision.common.internal.ImageUtils;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
final class zza implements zzb {
    private final Context zza;
    private final ImageLabelerOptions zzb;
    private boolean zzc;
    private boolean zzd;
    private zzof zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(Context context, ImageLabelerOptions imageLabelerOptions) {
        this.zza = context;
        this.zzb = imageLabelerOptions;
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final List zza(InputImage inputImage) {
        if (this.zze == null) {
            zzb();
        }
        zzof zzofVar = (zzof) Preconditions.checkNotNull(this.zze);
        if (!this.zzc) {
            try {
                zzofVar.zze();
                this.zzc = true;
            } catch (RemoteException e) {
                throw new MlKitException("Failed to init thin image labeler.", 13, e);
            }
        }
        try {
            List<zzoj> zzd = zzofVar.zzd(ImageUtils.getInstance().getImageDataWrapper(inputImage), new zzod(inputImage.getFormat(), inputImage.getWidth(), inputImage.getHeight(), CommonConvertUtils.convertToMVRotation(inputImage.getRotationDegrees()), SystemClock.elapsedRealtime()));
            ArrayList arrayList = new ArrayList();
            for (zzoj zzojVar : zzd) {
                arrayList.add(new ImageLabel(zzojVar.zzd(), zzojVar.zza(), zzojVar.zzb(), zzojVar.zzc()));
            }
            return arrayList;
        } catch (RemoteException e2) {
            throw new MlKitException("Failed to run thin image labeler.", 13, e2);
        }
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final void zzb() {
        if (this.zze != null) {
            return;
        }
        try {
            this.zze = zzoh.zza(DynamiteModule.load(this.zza, DynamiteModule.PREFER_REMOTE, "com.google.android.gms.vision.ica").instantiate("com.google.android.gms.vision.label.mlkit.ImageLabelerCreator")).zzd(ObjectWrapper.wrap(this.zza), new zzol(this.zzb.getConfidenceThreshold(), -1));
        } catch (RemoteException e) {
            throw new MlKitException("Failed to create thin image labeler.", 13, e);
        } catch (DynamiteModule.LoadingException unused) {
            if (!this.zzd) {
                OptionalModuleUtils.requestDownload(this.zza, "ica");
                this.zzd = true;
            }
            throw new MlKitException("Waiting for the label optional module to be downloaded. Please wait.", 14);
        }
    }

    @Override // com.google.mlkit.vision.label.defaults.thin.zzb
    public final void zzc() {
        zzof zzofVar = this.zze;
        if (zzofVar != null) {
            try {
                zzofVar.zzf();
            } catch (RemoteException unused) {
                Log.e("DecoupledImageLabeler", "Failed to release thin image labeler.");
            }
            this.zze = null;
            this.zzc = false;
        }
    }
}
