package com.google.android.gms.internal.clearcut;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
final class zzea {
    private static final zzea zznc = new zzea();
    private final zzeg zznd;
    private final ConcurrentMap zzne = new ConcurrentHashMap();

    private zzea() {
        String[] strArr = {"com.google.protobuf.AndroidProto3SchemaFactory"};
        zzeg zzegVar = null;
        for (int i = 0; i <= 0; i++) {
            zzegVar = zzk(strArr[0]);
            if (zzegVar != null) {
                break;
            }
        }
        this.zznd = zzegVar == null ? new zzdd() : zzegVar;
    }

    public static zzea zzcm() {
        return zznc;
    }

    private static zzeg zzk(String str) {
        try {
            return (zzeg) Class.forName(str).getConstructor(null).newInstance(null);
        } catch (Throwable unused) {
            return null;
        }
    }

    public final zzef zze(Class cls) {
        zzci.zza((Object) cls, "messageType");
        zzef zzefVar = (zzef) this.zzne.get(cls);
        if (zzefVar != null) {
            return zzefVar;
        }
        zzef zzd = this.zznd.zzd(cls);
        zzci.zza((Object) cls, "messageType");
        zzci.zza((Object) zzd, "schema");
        zzef zzefVar2 = (zzef) this.zzne.putIfAbsent(cls, zzd);
        return zzefVar2 != null ? zzefVar2 : zzd;
    }

    public final zzef zzp(Object obj) {
        return zze(obj.getClass());
    }
}
