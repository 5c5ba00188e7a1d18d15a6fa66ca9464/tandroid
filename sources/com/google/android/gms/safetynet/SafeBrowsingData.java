package com.google.android.gms.safetynet;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public class SafeBrowsingData extends AbstractSafeParcelable {
    public static final Parcelable.Creator<SafeBrowsingData> CREATOR = new zzj();
    private String zzb;
    private DataHolder zzc;
    private ParcelFileDescriptor zzd;
    private long zze;
    private byte[] zzf;
    private byte[] zzg;
    private File zzh;

    public SafeBrowsingData() {
        this(null, null, null, 0L, null);
    }

    private static final void zza(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException unused) {
        }
    }

    public ParcelFileDescriptor getFileDescriptor() {
        return this.zzd;
    }

    public long getLastUpdateTimeMs() {
        return this.zze;
    }

    public DataHolder getListsDataHolder() {
        return this.zzc;
    }

    public String getMetadata() {
        return this.zzb;
    }

    public byte[] getState() {
        return this.zzf;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x003f  */
    @Override // android.os.Parcelable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeToParcel(Parcel parcel, int i) {
        File file;
        FileOutputStream fileOutputStream;
        File file2 = null;
        if (this.zzd == null && this.zzg != null) {
            File file3 = this.zzh;
            if (file3 != null) {
                try {
                    file = File.createTempFile("xlb", ".tmp", file3);
                } catch (IOException unused) {
                    file = null;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    fileOutputStream = new FileOutputStream(file);
                    this.zzd = ParcelFileDescriptor.open(file, 268435456);
                    if (file != null) {
                        file.delete();
                    }
                } catch (IOException unused2) {
                    if (file != null) {
                        file.delete();
                    }
                    fileOutputStream = null;
                    if (fileOutputStream != null) {
                    }
                    zzj.zza(this, parcel, i);
                    this.zzd = null;
                } catch (Throwable th2) {
                    th = th2;
                    file2 = file;
                    if (file2 != null) {
                        file2.delete();
                    }
                    throw th;
                }
                if (fileOutputStream != null) {
                    DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));
                    try {
                        dataOutputStream.writeInt(this.zzg.length);
                        dataOutputStream.write(this.zzg);
                        zza(dataOutputStream);
                        zzj.zza(this, parcel, i | 1);
                    } catch (IOException unused3) {
                        zza(dataOutputStream);
                    } catch (Throwable th3) {
                        zza(dataOutputStream);
                        throw th3;
                    }
                    this.zzd = null;
                }
            }
            fileOutputStream = null;
            if (fileOutputStream != null) {
            }
        }
        zzj.zza(this, parcel, i);
        this.zzd = null;
    }

    public SafeBrowsingData(String str, DataHolder dataHolder, ParcelFileDescriptor parcelFileDescriptor, long j, byte[] bArr) {
        this.zzb = str;
        this.zzc = dataHolder;
        this.zzd = parcelFileDescriptor;
        this.zze = j;
        this.zzf = bArr;
    }
}
