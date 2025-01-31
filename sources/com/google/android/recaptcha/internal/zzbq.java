package com.google.android.recaptcha.internal;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;

/* loaded from: classes.dex */
public final class zzbq {
    private final zzh zza;
    private final zzbg zzb;

    public zzbq(zzh zzhVar, zzbg zzbgVar) {
        this.zza = zzhVar;
        this.zzb = zzbgVar;
    }

    public final zzoe zza(String str, byte[] bArr, zzbd zzbdVar) {
        zzbb zza = zzbdVar.zza(zzne.zzh);
        r0.zze.put(zza, new zzbf(zza, this.zzb.zza, new zzac()));
        try {
            URLConnection openConnection = new URL(str).openConnection();
            Intrinsics.checkNotNull(openConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
            HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept", "application/x-protobuffer");
            try {
                httpURLConnection.connect();
                httpURLConnection.getOutputStream().write(bArr);
                if (httpURLConnection.getResponseCode() == 200) {
                    try {
                        zzoe zzi = zzoe.zzi(httpURLConnection.getInputStream());
                        this.zzb.zza(zza);
                        return zzi;
                    } catch (Exception unused) {
                        throw new zzp(zzn.zzc, zzl.zzR, null);
                    }
                }
                if (httpURLConnection.getResponseCode() != 400) {
                    throw zzbr.zza(httpURLConnection.getResponseCode());
                }
                zzoz zzg = zzoz.zzg(httpURLConnection.getErrorStream());
                zzo zzoVar = zzp.zza;
                throw zzo.zza(zzg.zzi());
            } catch (Exception e) {
                if (e instanceof zzp) {
                    throw ((zzp) e);
                }
                throw new zzp(zzn.zze, zzl.zzQ, null);
            }
        } catch (zzp e2) {
            this.zzb.zzb(zza, e2, null);
            throw e2.zzc();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0051 A[Catch: Exception -> 0x004c, TRY_LEAVE, TryCatch #2 {Exception -> 0x004c, blocks: (B:3:0x0003, B:5:0x0015, B:11:0x0051, B:13:0x005c, B:18:0x0088, B:29:0x00b6, B:30:0x00bb, B:34:0x00d1, B:21:0x00ed, B:22:0x00fd, B:37:0x00e3, B:38:0x00ec, B:40:0x00fe, B:41:0x0107, B:43:0x0108, B:44:0x0111, B:46:0x0112, B:47:0x0117, B:49:0x0118, B:52:0x003c, B:53:0x002e, B:7:0x0020, B:9:0x0028, B:32:0x00c1), top: B:2:0x0003, inners: #1, #4, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final String zzb(zzoe zzoeVar, zzbd zzbdVar) {
        String str;
        String replace$default;
        try {
            String zzk = zzoeVar.zzk();
            String zzH = zzoeVar.zzH();
            if (this.zza.zzd(zzH)) {
                zzbb zza = zzbdVar.zza(zzne.zzk);
                r7.zze.put(zza, new zzbf(zza, this.zzb.zza, new zzac()));
                try {
                    str = this.zza.zza(zzH);
                } catch (Exception unused) {
                    this.zzb.zzb(zza, new zzp(zzn.zzn, zzl.zzad, null), null);
                }
                if (str != null) {
                    this.zzb.zza(zza);
                    if (str == null) {
                        this.zza.zzb();
                        zzbb zza2 = zzbdVar.zza(zzne.zzi);
                        try {
                            r7.zze.put(zza2, new zzbf(zza2, this.zzb.zza, new zzac()));
                            try {
                                try {
                                    URLConnection openConnection = new URL(zzk).openConnection();
                                    Intrinsics.checkNotNull(openConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
                                    HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
                                    httpURLConnection.setRequestMethod("GET");
                                    httpURLConnection.setDoInput(true);
                                    httpURLConnection.setRequestProperty("Accept", "application/x-protobuffer");
                                    httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
                                    httpURLConnection.connect();
                                    if (httpURLConnection.getResponseCode() != 200) {
                                        throw new zzp(zzn.zze, new zzl(httpURLConnection.getResponseCode()), null);
                                    }
                                    try {
                                        str = TextStreamsKt.readText(Intrinsics.areEqual("gzip", httpURLConnection.getContentEncoding()) ? new InputStreamReader(new GZIPInputStream(httpURLConnection.getInputStream())) : new InputStreamReader(httpURLConnection.getInputStream()));
                                        this.zzb.zza(zza2);
                                        zzbb zza3 = zzbdVar.zza(zzne.zzj);
                                        try {
                                            r0.zze.put(zza3, new zzbf(zza3, this.zzb.zza, new zzac()));
                                            this.zza.zzc(zzH, str);
                                            this.zzb.zza(zza3);
                                        } catch (Exception unused2) {
                                            this.zzb.zzb(zza3, new zzp(zzn.zzn, zzl.zzaf, null), null);
                                        }
                                    } catch (Exception unused3) {
                                        throw new zzp(zzn.zze, zzl.zzab, null);
                                    }
                                } catch (Exception unused4) {
                                    throw new zzp(zzn.zze, zzl.zzaa, null);
                                }
                            } catch (Exception unused5) {
                                throw new zzp(zzn.zzc, zzl.zzZ, null);
                            }
                        } catch (zzp e) {
                            this.zzb.zzb(zza2, e, null);
                            throw e;
                        }
                    }
                    replace$default = StringsKt__StringsJVMKt.replace$default(zzoeVar.zzj(), "JAVASCRIPT_TAG", str, false, 4, null);
                    return replace$default;
                }
                this.zzb.zzb(zza, new zzp(zzn.zzn, zzl.zzae, null), null);
            }
            str = null;
            if (str == null) {
            }
            replace$default = StringsKt__StringsJVMKt.replace$default(zzoeVar.zzj(), "JAVASCRIPT_TAG", str, false, 4, null);
            return replace$default;
        } catch (Exception e2) {
            if (e2 instanceof zzp) {
                throw e2;
            }
            throw new zzp(zzn.zzc, zzl.zzX, null);
        }
    }
}
