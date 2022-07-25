package com.google.android.gms.internal.location;

import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public final class zzav {
    private final zzbg<zzam> zza;
    private boolean zzc = false;
    private final Map<ListenerHolder.ListenerKey<LocationListener>, zzau> zzd = new HashMap();
    private final Map<ListenerHolder.ListenerKey, zzas> zze = new HashMap();
    private final Map<ListenerHolder.ListenerKey<LocationCallback>, zzar> zzf = new HashMap();

    public zzav(Context context, zzbg<zzam> zzbgVar) {
        this.zza = zzbgVar;
    }

    public final Location zza(String str) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        return ((zzh) this.zza).zza().zzn(str);
    }

    @Deprecated
    public final Location zzb() throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        return ((zzh) this.zza).zza().zzm();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zzd(LocationRequest locationRequest, ListenerHolder<LocationListener> listenerHolder, zzai zzaiVar) throws RemoteException {
        zzau zzauVar;
        zzau zzauVar2;
        zzi.zzp(((zzh) this.zza).zza);
        ListenerHolder.ListenerKey<LocationListener> listenerKey = listenerHolder.getListenerKey();
        if (listenerKey == null) {
            zzauVar2 = null;
        } else {
            synchronized (this.zzd) {
                zzauVar = this.zzd.get(listenerKey);
                if (zzauVar == null) {
                    zzauVar = new zzau(listenerHolder);
                }
                this.zzd.put(listenerKey, zzauVar);
            }
            zzauVar2 = zzauVar;
        }
        if (zzauVar2 == null) {
            return;
        }
        ((zzh) this.zza).zza().zzo(new zzbc(1, zzba.zza(null, locationRequest), zzauVar2, null, null, zzaiVar));
    }

    public final void zzh(ListenerHolder.ListenerKey<LocationListener> listenerKey, zzai zzaiVar) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        Preconditions.checkNotNull(listenerKey, "Invalid null listener key");
        synchronized (this.zzd) {
            zzau remove = this.zzd.remove(listenerKey);
            if (remove != null) {
                remove.zzc();
                ((zzh) this.zza).zza().zzo(zzbc.zza(remove, zzaiVar));
            }
        }
    }

    public final void zzk(boolean z) throws RemoteException {
        zzi.zzp(((zzh) this.zza).zza);
        ((zzh) this.zza).zza().zzp(z);
        this.zzc = z;
    }

    public final void zzn() throws RemoteException {
        synchronized (this.zzd) {
            for (zzau zzauVar : this.zzd.values()) {
                if (zzauVar != null) {
                    ((zzh) this.zza).zza().zzo(zzbc.zza(zzauVar, null));
                }
            }
            this.zzd.clear();
        }
        synchronized (this.zzf) {
            for (zzar zzarVar : this.zzf.values()) {
                if (zzarVar != null) {
                    ((zzh) this.zza).zza().zzo(zzbc.zzc(zzarVar, null));
                }
            }
            this.zzf.clear();
        }
        synchronized (this.zze) {
            for (zzas zzasVar : this.zze.values()) {
                if (zzasVar != null) {
                    ((zzh) this.zza).zza().zzu(new zzl(2, null, zzasVar, null));
                }
            }
            this.zze.clear();
        }
    }

    public final void zzo() throws RemoteException {
        if (this.zzc) {
            zzk(false);
        }
    }
}
