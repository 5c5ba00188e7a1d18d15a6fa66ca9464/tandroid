package com.google.mlkit.vision.label.defaults.thin;

import android.os.SystemClock;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.mlkit_vision_label.zzbe;
import com.google.android.gms.internal.mlkit_vision_label.zzdf;
import com.google.android.gms.internal.mlkit_vision_label.zzdh;
import com.google.android.gms.internal.mlkit_vision_label.zzjq;
import com.google.android.gms.internal.mlkit_vision_label.zzjr;
import com.google.android.gms.internal.mlkit_vision_label.zzjv;
import com.google.android.gms.internal.mlkit_vision_label.zzkd;
import com.google.android.gms.internal.mlkit_vision_label.zzke;
import com.google.android.gms.internal.mlkit_vision_label.zzkf;
import com.google.android.gms.internal.mlkit_vision_label.zzkg;
import com.google.android.gms.internal.mlkit_vision_label.zzky;
import com.google.android.gms.internal.mlkit_vision_label.zzlb;
import com.google.android.gms.internal.mlkit_vision_label.zzle;
import com.google.android.gms.internal.mlkit_vision_label.zzlg;
import com.google.android.gms.internal.mlkit_vision_label.zznf;
import com.google.android.gms.internal.mlkit_vision_label.zznp;
import com.google.android.gms.internal.mlkit_vision_label.zznr;
import com.google.android.gms.internal.mlkit_vision_label.zzns;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.sdkinternal.MLTask;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.ImageUtils;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.List;
/* loaded from: classes.dex */
public final class zzh extends MLTask {
    private static final ImageUtils zzc = ImageUtils.getInstance();
    boolean zza = true;
    final zzlg zzb;
    private final zzb zzd;
    private final zznp zze;
    private final zznr zzf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(ImageLabelerOptions imageLabelerOptions, zzb zzbVar, zznp zznpVar) {
        Preconditions.checkNotNull(imageLabelerOptions, "ImageLabelerOptions can not be null");
        this.zzd = zzbVar;
        this.zze = zznpVar;
        zzle zzleVar = new zzle();
        zzleVar.zza(Float.valueOf(imageLabelerOptions.getConfidenceThreshold()));
        this.zzb = zzleVar.zzb();
        this.zzf = zznr.zza(MlKitContext.getInstance().getApplicationContext());
    }

    private final void zze(zzke zzkeVar, InputImage inputImage, long j) {
        final long elapsedRealtime = SystemClock.elapsedRealtime() - j;
        this.zze.zzh(new zzf(this, elapsedRealtime, zzkeVar, inputImage), zzkf.zzn);
        zzdf zzdfVar = new zzdf();
        zzdfVar.zza(this.zzb);
        zzdfVar.zzb(zzkeVar);
        zzdfVar.zzc(Boolean.valueOf(this.zza));
        final zzdh zzd = zzdfVar.zzd();
        final zzg zzgVar = zzg.zza;
        final zznp zznpVar = this.zze;
        final zzkf zzkfVar = zzkf.zzbg;
        MLTaskExecutor.workerThreadExecutor().execute(new Runnable(zzkfVar, zzd, elapsedRealtime, zzgVar, null) { // from class: com.google.android.gms.internal.mlkit_vision_label.zznm
            public final /* synthetic */ zzkf zzb;
            public final /* synthetic */ Object zzc;
            public final /* synthetic */ long zzd;
            public final /* synthetic */ com.google.mlkit.vision.label.defaults.thin.zzg zze;

            @Override // java.lang.Runnable
            public final void run() {
                zznp.this.zzg(this.zzb, this.zzc, this.zzd, this.zze);
            }
        });
        long currentTimeMillis = System.currentTimeMillis();
        this.zzf.zzc(24305, zzkeVar.zza(), currentTimeMillis - elapsedRealtime, currentTimeMillis);
    }

    @Override // com.google.mlkit.common.sdkinternal.ModelResource
    public final synchronized void load() {
        this.zzd.zzb();
        zznp zznpVar = this.zze;
        zzkg zzkgVar = new zzkg();
        zzkgVar.zze(zzkd.zzb);
        zzlb zzlbVar = new zzlb();
        zzlbVar.zzb(this.zzb);
        zzlbVar.zzc(zzbe.zzi(zzke.zza));
        zzkgVar.zzh(zzlbVar.zzd());
        zznpVar.zzd(zzns.zzf(zzkgVar), zzkf.zzq);
    }

    @Override // com.google.mlkit.common.sdkinternal.ModelResource
    public final synchronized void release() {
        this.zzd.zzc();
        this.zza = true;
        zznp zznpVar = this.zze;
        zzkg zzkgVar = new zzkg();
        zzkgVar.zze(zzkd.zzb);
        zznpVar.zzd(zzns.zzf(zzkgVar), zzkf.zzp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ zznf zzc(long j, zzke zzkeVar, InputImage inputImage) {
        zzkg zzkgVar = new zzkg();
        zzkgVar.zze(zzkd.zzb);
        zzky zzkyVar = new zzky();
        zzjv zzjvVar = new zzjv();
        zzjvVar.zzc(Long.valueOf(j));
        zzjvVar.zzd(zzkeVar);
        zzjvVar.zze(Boolean.valueOf(this.zza));
        Boolean bool = Boolean.TRUE;
        zzjvVar.zza(bool);
        zzjvVar.zzb(bool);
        zzkyVar.zzd(zzjvVar.zzf());
        ImageUtils imageUtils = zzc;
        int mobileVisionImageFormat = imageUtils.getMobileVisionImageFormat(inputImage);
        int mobileVisionImageSize = imageUtils.getMobileVisionImageSize(inputImage);
        zzjq zzjqVar = new zzjq();
        zzjqVar.zza(mobileVisionImageFormat != -1 ? mobileVisionImageFormat != 35 ? mobileVisionImageFormat != 842094169 ? mobileVisionImageFormat != 16 ? mobileVisionImageFormat != 17 ? zzjr.zza : zzjr.zzc : zzjr.zzb : zzjr.zzd : zzjr.zze : zzjr.zzg);
        zzjqVar.zzb(Integer.valueOf(mobileVisionImageSize));
        zzkyVar.zzc(zzjqVar.zzd());
        zzkyVar.zze(this.zzb);
        zzkgVar.zzg(zzkyVar.zzf());
        return zzns.zzf(zzkgVar);
    }

    @Override // com.google.mlkit.common.sdkinternal.MLTask
    /* renamed from: zzd */
    public final synchronized List run(InputImage inputImage) {
        List zza;
        Preconditions.checkNotNull(inputImage, "Mobile vision input can not be null");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            zza = this.zzd.zza(inputImage);
            zze(zzke.zza, inputImage, elapsedRealtime);
            this.zza = false;
        } catch (MlKitException e) {
            zze(e.getErrorCode() == 14 ? zzke.zzk : zzke.zzV, inputImage, elapsedRealtime);
            throw e;
        }
        return zza;
    }
}
