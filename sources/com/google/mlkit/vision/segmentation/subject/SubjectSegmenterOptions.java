package com.google.mlkit.vision.segmentation.subject;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzrx;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzrz;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class SubjectSegmenterOptions {
    private final boolean zza;
    private final boolean zzb;
    private final boolean zzc;
    private final boolean zzd;
    private final boolean zze;
    private final Executor zzf;

    public static class Builder {
        private boolean zza = false;
        private boolean zzb = false;
        private boolean zzc = false;
        private boolean zzd = false;
        private boolean zze = false;
        private Executor zzf;

        public SubjectSegmenterOptions build() {
            return new SubjectSegmenterOptions(this, null);
        }

        public Builder enableForegroundBitmap() {
            this.zzb = true;
            return this;
        }

        public Builder enableMultipleSubjects(SubjectResultOptions subjectResultOptions) {
            this.zzc = true;
            this.zzd = subjectResultOptions.zza;
            this.zze = subjectResultOptions.zzb;
            return this;
        }
    }

    public static class SubjectResultOptions {
        private boolean zza;
        private boolean zzb;

        public static class Builder {
            private boolean zza = false;
            private boolean zzb = false;

            public SubjectResultOptions build() {
                return new SubjectResultOptions(this, null);
            }

            public Builder enableSubjectBitmap() {
                this.zzb = true;
                return this;
            }
        }

        /* synthetic */ SubjectResultOptions(Builder builder, zza zzaVar) {
            this.zza = false;
            this.zzb = false;
            this.zza = builder.zza;
            this.zzb = builder.zzb;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SubjectResultOptions)) {
                return false;
            }
            SubjectResultOptions subjectResultOptions = (SubjectResultOptions) obj;
            return this.zza == subjectResultOptions.zza && this.zzb == subjectResultOptions.zzb;
        }

        public int hashCode() {
            return Objects.hashCode(Boolean.valueOf(this.zza), Boolean.valueOf(this.zzb));
        }
    }

    /* synthetic */ SubjectSegmenterOptions(Builder builder, zzb zzbVar) {
        this.zza = builder.zza;
        this.zzb = builder.zzb;
        this.zzc = builder.zzc;
        this.zzd = builder.zzd;
        this.zze = builder.zze;
        this.zzf = builder.zzf;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SubjectSegmenterOptions)) {
            return false;
        }
        SubjectSegmenterOptions subjectSegmenterOptions = (SubjectSegmenterOptions) obj;
        return this.zza == subjectSegmenterOptions.zza && this.zzb == subjectSegmenterOptions.zzb && this.zzc == subjectSegmenterOptions.zzc && this.zzd == subjectSegmenterOptions.zzd && this.zze == subjectSegmenterOptions.zze && Objects.equal(this.zzf, subjectSegmenterOptions.zzf);
    }

    public int hashCode() {
        return Objects.hashCode(Boolean.valueOf(this.zza), Boolean.valueOf(this.zzb), Boolean.valueOf(this.zzc), Boolean.valueOf(this.zzd), Boolean.valueOf(this.zze), this.zzf);
    }

    public final zzrz zza() {
        zzrx zzrxVar = new zzrx();
        zzrxVar.zzb(Boolean.valueOf(this.zza));
        zzrxVar.zza(Boolean.valueOf(this.zzb));
        zzrxVar.zzc(Boolean.valueOf(this.zzc));
        zzrxVar.zze(Boolean.valueOf(this.zzd));
        zzrxVar.zzd(Boolean.valueOf(this.zze));
        return zzrxVar.zzf();
    }

    public final Executor zzb() {
        return this.zzf;
    }

    public final boolean zzc() {
        return this.zzb;
    }

    public final boolean zzd() {
        return this.zza;
    }

    public final boolean zze() {
        return this.zzc;
    }

    public final boolean zzf() {
        return this.zze;
    }

    public final boolean zzg() {
        return this.zzd;
    }
}
