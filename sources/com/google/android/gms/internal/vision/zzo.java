package com.google.android.gms.internal.vision;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.vision.barcode.Barcode;

/* loaded from: classes.dex */
public final class zzo extends zzb implements zzl {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetector");
    }

    @Override // com.google.android.gms.internal.vision.zzl
    public final void zza() {
        zzb(3, a_());
    }

    @Override // com.google.android.gms.internal.vision.zzl
    public final Barcode[] zza(IObjectWrapper iObjectWrapper, zzs zzsVar) {
        Parcel a_ = a_();
        zzd.zza(a_, iObjectWrapper);
        zzd.zza(a_, zzsVar);
        Parcel zza = zza(1, a_);
        Barcode[] barcodeArr = (Barcode[]) zza.createTypedArray(Barcode.CREATOR);
        zza.recycle();
        return barcodeArr;
    }

    @Override // com.google.android.gms.internal.vision.zzl
    public final Barcode[] zzb(IObjectWrapper iObjectWrapper, zzs zzsVar) {
        Parcel a_ = a_();
        zzd.zza(a_, iObjectWrapper);
        zzd.zza(a_, zzsVar);
        Parcel zza = zza(2, a_);
        Barcode[] barcodeArr = (Barcode[]) zza.createTypedArray(Barcode.CREATOR);
        zza.recycle();
        return barcodeArr;
    }
}
