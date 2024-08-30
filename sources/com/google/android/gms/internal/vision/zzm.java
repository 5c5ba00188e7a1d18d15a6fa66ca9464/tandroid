package com.google.android.gms.internal.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.vision.barcode.Barcode;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class zzm extends zzt {
    private final zzk zza;

    public zzm(Context context, zzk zzkVar) {
        super(context, "BarcodeNativeHandle", "barcode");
        this.zza = zzkVar;
        zzd();
    }

    @Override // com.google.android.gms.internal.vision.zzt
    protected final /* synthetic */ Object zza(DynamiteModule dynamiteModule, Context context) {
        zzn zzpVar;
        IBinder instantiate = dynamiteModule.instantiate("com.google.android.gms.vision.barcode.ChimeraNativeBarcodeDetectorCreator");
        if (instantiate == null) {
            zzpVar = null;
        } else {
            IInterface queryLocalInterface = instantiate.queryLocalInterface("com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetectorCreator");
            zzpVar = queryLocalInterface instanceof zzn ? (zzn) queryLocalInterface : new zzp(instantiate);
        }
        if (zzpVar == null) {
            return null;
        }
        return zzpVar.zza(ObjectWrapper.wrap(context), (zzk) Preconditions.checkNotNull(this.zza));
    }

    @Override // com.google.android.gms.internal.vision.zzt
    protected final void zza() {
        if (zzb()) {
            ((zzl) Preconditions.checkNotNull((zzl) zzd())).zza();
        }
    }

    public final Barcode[] zza(Bitmap bitmap, zzs zzsVar) {
        if (zzb()) {
            try {
                return ((zzl) Preconditions.checkNotNull((zzl) zzd())).zzb(ObjectWrapper.wrap(bitmap), zzsVar);
            } catch (RemoteException e) {
                Log.e("BarcodeNativeHandle", "Error calling native barcode detector", e);
                return new Barcode[0];
            }
        }
        return new Barcode[0];
    }

    public final Barcode[] zza(ByteBuffer byteBuffer, zzs zzsVar) {
        if (zzb()) {
            try {
                return ((zzl) Preconditions.checkNotNull((zzl) zzd())).zza(ObjectWrapper.wrap(byteBuffer), zzsVar);
            } catch (RemoteException e) {
                Log.e("BarcodeNativeHandle", "Error calling native barcode detector", e);
                return new Barcode[0];
            }
        }
        return new Barcode[0];
    }
}
