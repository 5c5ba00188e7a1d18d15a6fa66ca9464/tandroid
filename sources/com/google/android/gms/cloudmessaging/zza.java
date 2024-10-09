package com.google.android.gms.cloudmessaging;

import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.cloudmessaging.IMessengerCompat;

/* loaded from: classes.dex */
public class zza implements Parcelable {
    public static final Parcelable.Creator<zza> CREATOR = new zzc();
    private Messenger zza;
    private IMessengerCompat zzb;

    /* loaded from: classes.dex */
    public static final class zza extends ClassLoader {
        @Override // java.lang.ClassLoader
        protected final Class loadClass(String str, boolean z) {
            if (!"com.google.android.gms.iid.MessengerCompat".equals(str)) {
                return super.loadClass(str, z);
            }
            if (!Log.isLoggable("CloudMessengerCompat", 3) && (Build.VERSION.SDK_INT != 23 || !Log.isLoggable("CloudMessengerCompat", 3))) {
                return zza.class;
            }
            Log.d("CloudMessengerCompat", "Using renamed FirebaseIidMessengerCompat class");
            return zza.class;
        }
    }

    public zza(IBinder iBinder) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.zza = new Messenger(iBinder);
        } else {
            this.zzb = new IMessengerCompat.Proxy(iBinder);
        }
    }

    private final IBinder zza() {
        Messenger messenger = this.zza;
        return messenger != null ? messenger.getBinder() : this.zzb.asBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return zza().equals(((zza) obj).zza());
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public int hashCode() {
        return zza().hashCode();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Messenger messenger = this.zza;
        parcel.writeStrongBinder(messenger != null ? messenger.getBinder() : this.zzb.asBinder());
    }

    public final void zza(Message message) {
        Messenger messenger = this.zza;
        if (messenger != null) {
            messenger.send(message);
        } else {
            this.zzb.send(message);
        }
    }
}
