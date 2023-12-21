package com.google.mlkit.vision.segmentation.subject.internal;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzas;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzav;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzdu;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzdw;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzng;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zznh;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zznn;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zznz;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzoa;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzob;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzoc;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzra;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzrd;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzrt;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzta;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztk;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztl;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztn;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzto;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztz;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzub;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzud;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzuf;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzuh;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzuj;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.sdkinternal.MLTask;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.CommonConvertUtils;
import com.google.mlkit.vision.common.internal.ImageUtils;
import com.google.mlkit.vision.segmentation.subject.Subject;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final class zzj extends MLTask {
    private static final Feature[] zza = {OptionalModuleUtils.FEATURE_SUBJECT_SEGMENTATION};
    private static final ImageUtils zzb = ImageUtils.getInstance();
    private final Context zzc;
    private final SubjectSegmenterOptions zzd;
    private final zztl zze;
    private final zztn zzf;
    private boolean zzg = true;
    private boolean zzh;
    private zzub zzi;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(MlKitContext mlKitContext, SubjectSegmenterOptions subjectSegmenterOptions, zztl zztlVar, zztn zztnVar) {
        Preconditions.checkNotNull(mlKitContext, "MlKitContext can not be null");
        Preconditions.checkNotNull(subjectSegmenterOptions, "SubjectSegmenterOptions can not be null");
        this.zzc = mlKitContext.getApplicationContext();
        this.zzd = subjectSegmenterOptions;
        this.zze = zztlVar;
        this.zzf = zztnVar;
    }

    private final void zzg(final zzoa zzoaVar, long j, final boolean z, final InputImage inputImage, final zzuh zzuhVar) {
        final long elapsedRealtime = SystemClock.elapsedRealtime() - j;
        this.zze.zzf(new zztk() { // from class: com.google.mlkit.vision.segmentation.subject.internal.zzg
            @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztk
            public final zzta zza() {
                return zzj.this.zzc(elapsedRealtime, zzoaVar, z, inputImage, zzuhVar);
            }
        }, zzob.zzey);
        zzdu zzduVar = new zzdu();
        zzduVar.zzc(this.zzd.zza());
        zzduVar.zza(zzoaVar);
        zzduVar.zzb(Boolean.valueOf(z));
        final zzdw zzd = zzduVar.zzd();
        final zzh zzhVar = zzh.zza;
        final zzob zzobVar = zzob.zzbw;
        Executor workerThreadExecutor = MLTaskExecutor.workerThreadExecutor();
        final zztl zztlVar = this.zze;
        workerThreadExecutor.execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztj
            @Override // java.lang.Runnable
            public final void run() {
                zztl.this.zzh(zzobVar, zzd, elapsedRealtime, zzhVar);
            }
        });
        long currentTimeMillis = System.currentTimeMillis();
        this.zzf.zzc(24336, zzoaVar.zza(), currentTimeMillis - elapsedRealtime, currentTimeMillis);
    }

    private static final FloatBuffer zzh(float[] fArr) {
        if (fArr == null) {
            return null;
        }
        FloatBuffer allocate = FloatBuffer.allocate(fArr.length);
        allocate.put(fArr);
        allocate.rewind();
        return allocate;
    }

    @Override // com.google.mlkit.common.sdkinternal.ModelResource
    public final synchronized void load() throws MlKitException {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Context context = this.zzc;
        Feature[] featureArr = zza;
        if (!OptionalModuleUtils.areAllRequiredModulesAvailable(context, featureArr)) {
            if (!this.zzh) {
                OptionalModuleUtils.requestDownload(this.zzc, featureArr);
                this.zzh = true;
            }
            zzf(zzoa.zzB, elapsedRealtime);
            throw new MlKitException("Waiting for the subject segmentation optional module to be downloaded. Please wait.", 14);
        }
        try {
            if (this.zzi == null) {
                this.zzi = zzud.zza(DynamiteModule.load(this.zzc, DynamiteModule.PREFER_REMOTE, "com.google.android.gms.mlkit_subject_segmentation").instantiate("com.google.android.gms.mlkit.segmentation.subject.SubjectSegmenterCreator")).zzd(ObjectWrapper.wrap(this.zzc), new zzuj(this.zzd.zzd(), this.zzd.zzc(), this.zzd.zze(), this.zzd.zzg(), this.zzd.zzf()));
            }
            try {
                this.zzi.zze();
                zzf(zzoa.zza, elapsedRealtime);
            } catch (RemoteException e) {
                zzf(zzoa.zzC, elapsedRealtime);
                throw new MlKitException("Failed to init module subject segmenter", 13, e);
            }
        } catch (Exception e2) {
            zzf(zzoa.zzH, elapsedRealtime);
            throw new MlKitException("Failed to load subject segmentation module", 13, e2);
        }
    }

    @Override // com.google.mlkit.common.sdkinternal.ModelResource
    public final synchronized void release() {
        try {
            zzub zzubVar = this.zzi;
            if (zzubVar != null) {
                zzubVar.zzf();
            }
            this.zzi = null;
        } catch (RemoteException unused) {
            Log.e("SubjectSegmenterTask", "Failed to release subject segmenter");
            this.zzi = null;
        }
        this.zzg = true;
        this.zze.zzf(zzf.zza, zzob.zzez);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ zzta zzc(long j, zzoa zzoaVar, boolean z, InputImage inputImage, zzuh zzuhVar) {
        zznh zznhVar;
        zzra zzraVar = new zzra();
        zznn zznnVar = new zznn();
        zznnVar.zza(Long.valueOf(j));
        zznnVar.zzb(zzoaVar);
        zznnVar.zzc(Boolean.valueOf(z));
        zzraVar.zzf(zznnVar.zzd());
        int format = inputImage.getFormat();
        int mobileVisionImageSize = zzb.getMobileVisionImageSize(inputImage);
        zzng zzngVar = new zzng();
        if (format == -1) {
            zznhVar = zznh.zzg;
        } else if (format == 35) {
            zznhVar = zznh.zze;
        } else if (format == 842094169) {
            zznhVar = zznh.zzd;
        } else if (format == 16) {
            zznhVar = zznh.zzb;
        } else if (format == 17) {
            zznhVar = zznh.zzc;
        } else {
            zznhVar = zznh.zza;
        }
        zzngVar.zza(zznhVar);
        zzngVar.zzb(Integer.valueOf(mobileVisionImageSize));
        zzraVar.zze(zzngVar.zzd());
        zzraVar.zzi(this.zzd.zza());
        if (zzuhVar != null) {
            zzraVar.zzg(zzav.zzh(zzuhVar.zzb()));
            List<zzuf> zzc = zzuhVar.zzc();
            if (!zzc.isEmpty()) {
                zzas zzasVar = new zzas();
                for (zzuf zzufVar : zzc) {
                    zzrt zzrtVar = new zzrt();
                    zzrtVar.zzd(Integer.valueOf(zzufVar.zzd()));
                    zzrtVar.zza(Integer.valueOf(zzufVar.zza()));
                    zzrtVar.zzb(Integer.valueOf(zzufVar.zzb()));
                    zzrtVar.zzc(Integer.valueOf(zzufVar.zzc()));
                    zzasVar.zza(zzrtVar.zze());
                }
                zzraVar.zzh(zzasVar.zzb());
            }
        }
        zzoc zzocVar = new zzoc();
        zzocVar.zze(zznz.zzb);
        zzocVar.zzh(zzraVar.zzj());
        return zzto.zzf(zzocVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ zzta zzd(zzoa zzoaVar, long j) {
        zzoc zzocVar = new zzoc();
        zzocVar.zze(zznz.zzb);
        zzrd zzrdVar = new zzrd();
        zzrdVar.zzd(this.zzd.zza());
        zzrdVar.zzc(zzoaVar);
        zzrdVar.zzb(Long.valueOf(SystemClock.elapsedRealtime() - j));
        zzocVar.zzi(zzrdVar.zze());
        return zzto.zzf(zzocVar);
    }

    @Override // com.google.mlkit.common.sdkinternal.MLTask
    /* renamed from: zze */
    public final synchronized SubjectSegmentationResult run(InputImage inputImage) throws MlKitException {
        zzuh zzd;
        ArrayList arrayList;
        Preconditions.checkNotNull(inputImage, "Input image can not be null");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            zzd = ((zzub) Preconditions.checkNotNull(this.zzi)).zzd(ImageUtils.getInstance().getImageDataWrapper(inputImage), new zztz(inputImage.getFormat(), inputImage.getWidth(), inputImage.getHeight(), CommonConvertUtils.convertToMVRotation(inputImage.getRotationDegrees()), SystemClock.elapsedRealtime()));
            arrayList = new ArrayList();
            if (this.zzd.zze()) {
                for (zzuf zzufVar : zzd.zzc()) {
                    arrayList.add(new Subject(zzh(zzufVar.zzf()), zzufVar.zze(), zzufVar.zzd(), zzufVar.zza(), zzufVar.zzb(), zzufVar.zzc()));
                }
            }
            zzg(zzoa.zza, elapsedRealtime, this.zzg, inputImage, zzd);
            this.zzg = false;
        } catch (RemoteException e) {
            zzg(zzoa.zzD, elapsedRealtime, this.zzg, inputImage, null);
            throw new MlKitException("Failed to run thin subject segmenter.", 13, e);
        }
        return new SubjectSegmentationResult(arrayList, zzh(zzd.zzd()), zzd.zza());
    }

    final void zzf(final zzoa zzoaVar, final long j) {
        this.zze.zzf(new zztk() { // from class: com.google.mlkit.vision.segmentation.subject.internal.zzi
            @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztk
            public final zzta zza() {
                return zzj.this.zzd(zzoaVar, j);
            }
        }, zzob.zzex);
    }
}
