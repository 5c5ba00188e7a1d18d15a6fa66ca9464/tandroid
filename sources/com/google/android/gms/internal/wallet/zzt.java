package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.PaymentCardRecognitionIntentResponse;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.zzau;
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
                Status status = (Status) zzc.zza(parcel, Status.CREATOR);
                zzi zziVar = (zzi) zzc.zza(parcel, zzi.CREATOR);
                break;
            case 8:
            case 11:
            case 13:
                Status status2 = (Status) zzc.zza(parcel, Status.CREATOR);
                break;
            case 9:
                zzc((Status) zzc.zza(parcel, Status.CREATOR), zzc.zzd(parcel), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 10:
                Status status3 = (Status) zzc.zza(parcel, Status.CREATOR);
                zzk zzkVar = (zzk) zzc.zza(parcel, zzk.CREATOR);
                break;
            case 12:
                Status status4 = (Status) zzc.zza(parcel, Status.CREATOR);
                zzau zzauVar = (zzau) zzc.zza(parcel, zzau.CREATOR);
                break;
            case 14:
                zzf((Status) zzc.zza(parcel, Status.CREATOR), (PaymentData) zzc.zza(parcel, PaymentData.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
            case 15:
                Status status5 = (Status) zzc.zza(parcel, Status.CREATOR);
                zzq zzqVar = (zzq) zzc.zza(parcel, zzq.CREATOR);
                break;
            case 16:
                Status status6 = (Status) zzc.zza(parcel, Status.CREATOR);
                zzm zzmVar = (zzm) zzc.zza(parcel, zzm.CREATOR);
                break;
            case 17:
                Status status7 = (Status) zzc.zza(parcel, Status.CREATOR);
                zzo zzoVar = (zzo) zzc.zza(parcel, zzo.CREATOR);
                break;
            case 18:
                parcel.readInt();
                break;
            case 19:
                zze((Status) zzc.zza(parcel, Status.CREATOR), (PaymentCardRecognitionIntentResponse) zzc.zza(parcel, PaymentCardRecognitionIntentResponse.CREATOR), (Bundle) zzc.zza(parcel, Bundle.CREATOR));
                return true;
        }
        Bundle bundle = (Bundle) zzc.zza(parcel, Bundle.CREATOR);
        return true;
    }
}
