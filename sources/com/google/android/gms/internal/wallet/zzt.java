package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.PaymentCardRecognitionIntentResponse;
import com.google.android.gms.wallet.PaymentData;

/* loaded from: classes.dex */
public abstract class zzt extends zzb implements zzu {
    public zzt() {
        super("com.google.android.gms.wallet.internal.IWalletServiceCallbacks");
    }

    @Override // com.google.android.gms.internal.wallet.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        switch (i) {
            case 1:
                zzd(parcel.readInt(), (MaskedWallet) zzc.zza(parcel, MaskedWallet.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 2:
                zzb(parcel.readInt(), (FullWallet) zzc.zza(parcel, FullWallet.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 3:
                zzg(parcel.readInt(), zzc.zzd(parcel), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 4:
                zzh(parcel.readInt(), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 5:
            default:
                return false;
            case 6:
                parcel.readInt();
                zzc.zzd(parcel);
                break;
            case 7:
                break;
            case 8:
            case 11:
            case 13:
                break;
            case 9:
                zzc((Status) zzc.zza(parcel, Status.CREATOR), zzc.zzd(parcel), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 10:
                break;
            case 12:
                break;
            case 14:
                zzf((Status) zzc.zza(parcel, Status.CREATOR), (PaymentData) zzc.zza(parcel, PaymentData.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;
            case 18:
                parcel.readInt();
                break;
            case 19:
                zze((Status) zzc.zza(parcel, Status.CREATOR), (PaymentCardRecognitionIntentResponse) zzc.zza(parcel, PaymentCardRecognitionIntentResponse.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
        }
        return true;
    }
}
