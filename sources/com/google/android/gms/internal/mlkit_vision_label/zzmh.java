package com.google.android.gms.internal.mlkit_vision_label;

import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzmh {
    private String zza;
    private String zzb;
    private String zzc;
    private String zzd;
    private zzbe zze;
    private String zzf;
    private Boolean zzg;
    private Boolean zzh;
    private Boolean zzi;
    private Integer zzj;
    private Integer zzk;

    public final zzmh zzb(String str) {
        this.zza = str;
        return this;
    }

    public final zzmh zzc(String str) {
        this.zzb = str;
        return this;
    }

    public final zzmh zzd(Integer num) {
        this.zzj = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzmh zze(Boolean bool) {
        this.zzg = bool;
        return this;
    }

    public final zzmh zzf(Boolean bool) {
        this.zzi = bool;
        return this;
    }

    public final zzmh zzg(Boolean bool) {
        this.zzh = bool;
        return this;
    }

    public final zzmh zzh(zzbe zzbeVar) {
        this.zze = zzbeVar;
        return this;
    }

    public final zzmh zzi(String str) {
        this.zzf = str;
        return this;
    }

    public final zzmh zzj(String str) {
        this.zzc = str;
        return this;
    }

    public final zzmh zzk(Integer num) {
        this.zzk = num;
        return this;
    }

    public final zzmh zzl(String str) {
        this.zzd = str;
        return this;
    }

    public final zzmj zzm() {
        return new zzmj(this, null);
    }
}
