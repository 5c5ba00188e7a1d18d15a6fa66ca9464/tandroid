package com.google.android.gms.internal.vision;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
final class zzlj implements Iterator {
    private int zza;
    private Iterator zzb;
    private final /* synthetic */ zzlh zzc;

    private zzlj(zzlh zzlhVar) {
        List list;
        this.zzc = zzlhVar;
        list = zzlhVar.zzb;
        this.zza = list.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzlj(zzlh zzlhVar, zzlg zzlgVar) {
        this(zzlhVar);
    }

    private final Iterator zza() {
        Map map;
        if (this.zzb == null) {
            map = this.zzc.zzf;
            this.zzb = map.entrySet().iterator();
        }
        return this.zzb;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        List list;
        int i = this.zza;
        if (i > 0) {
            list = this.zzc.zzb;
            if (i <= list.size()) {
                return true;
            }
        }
        return zza().hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        List list;
        Object obj;
        if (zza().hasNext()) {
            obj = zza().next();
        } else {
            list = this.zzc.zzb;
            int i = this.zza - 1;
            this.zza = i;
            obj = list.get(i);
        }
        return (Map.Entry) obj;
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
