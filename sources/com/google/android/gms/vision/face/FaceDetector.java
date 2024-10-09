package com.google.android.gms.vision.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.vision.zzs;
import com.google.android.gms.internal.vision.zzw;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.internal.client.zzb;
import com.google.android.gms.vision.face.internal.client.zzf;
import com.google.android.gms.vision.zzc;
import java.nio.ByteBuffer;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class FaceDetector extends Detector {
    private final zzc zza;
    private final zzb zzb;
    private final Object zzc;
    private boolean zzd;

    /* loaded from: classes.dex */
    public static class Builder {
        private final Context zza;
        private int zzb = 0;
        private boolean zzc = false;
        private int zzd = 0;
        private boolean zze = true;
        private int zzf = 0;
        private float zzg = -1.0f;

        public Builder(Context context) {
            this.zza = context;
        }

        public FaceDetector build() {
            zzf zzfVar = new zzf();
            zzfVar.zza = this.zzf;
            zzfVar.zzb = this.zzb;
            zzfVar.zzc = this.zzd;
            zzfVar.zzd = this.zzc;
            zzfVar.zze = this.zze;
            zzfVar.zzf = this.zzg;
            if (FaceDetector.zzb(zzfVar)) {
                return new FaceDetector(new zzb(this.zza, zzfVar));
            }
            throw new IllegalArgumentException("Invalid build options");
        }

        public Builder setLandmarkType(int i) {
            if (i == 0 || i == 1 || i == 2) {
                this.zzb = i;
                return this;
            }
            StringBuilder sb = new StringBuilder(34);
            sb.append("Invalid landmark type: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }

        public Builder setMode(int i) {
            if (i == 0 || i == 1 || i == 2) {
                this.zzf = i;
                return this;
            }
            StringBuilder sb = new StringBuilder(25);
            sb.append("Invalid mode: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }

        public Builder setTrackingEnabled(boolean z) {
            this.zze = z;
            return this;
        }
    }

    private FaceDetector(zzb zzbVar) {
        this.zza = new zzc();
        this.zzc = new Object();
        this.zzd = true;
        this.zzb = zzbVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean zzb(zzf zzfVar) {
        boolean z;
        if (zzfVar.zza == 2 || zzfVar.zzb != 2) {
            z = true;
        } else {
            Log.e("FaceDetector", "Contour is not supported for non-SELFIE mode.");
            z = false;
        }
        if (zzfVar.zzb != 2 || zzfVar.zzc != 1) {
            return z;
        }
        Log.e("FaceDetector", "Classification is not supported with contour.");
        return false;
    }

    public final SparseArray detect(Frame frame) {
        Face[] zza;
        if (frame == null) {
            throw new IllegalArgumentException("No frame supplied.");
        }
        if (frame.getPlanes() == null || ((Image.Plane[]) Preconditions.checkNotNull(frame.getPlanes())).length != 3) {
            ByteBuffer zza2 = frame.getBitmap() != null ? zzw.zza((Bitmap) Preconditions.checkNotNull(frame.getBitmap()), true) : frame.getGrayscaleImageData();
            synchronized (this.zzc) {
                if (!this.zzd) {
                    throw new IllegalStateException("Cannot use detector after release()");
                }
                zza = this.zzb.zza((ByteBuffer) Preconditions.checkNotNull(zza2), zzs.zza(frame));
            }
        } else {
            synchronized (this.zzc) {
                try {
                    if (!this.zzd) {
                        throw new IllegalStateException("Cannot use detector after release()");
                    }
                    zza = this.zzb.zza((Image.Plane[]) Preconditions.checkNotNull(frame.getPlanes()), zzs.zza(frame));
                } finally {
                }
            }
        }
        HashSet hashSet = new HashSet();
        SparseArray sparseArray = new SparseArray(zza.length);
        int i = 0;
        for (Face face : zza) {
            int id = face.getId();
            i = Math.max(i, id);
            if (hashSet.contains(Integer.valueOf(id))) {
                id = i + 1;
                i = id;
            }
            hashSet.add(Integer.valueOf(id));
            sparseArray.append(this.zza.zza(id), face);
        }
        return sparseArray;
    }

    protected final void finalize() {
        try {
            synchronized (this.zzc) {
                try {
                    if (this.zzd) {
                        Log.w("FaceDetector", "FaceDetector was not released with FaceDetector.release()");
                        release();
                    }
                } finally {
                }
            }
        } finally {
            super.finalize();
        }
    }

    public final boolean isOperational() {
        return this.zzb.zzb();
    }

    @Override // com.google.android.gms.vision.Detector
    public final void release() {
        super.release();
        synchronized (this.zzc) {
            try {
                if (this.zzd) {
                    this.zzb.zzc();
                    this.zzd = false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
