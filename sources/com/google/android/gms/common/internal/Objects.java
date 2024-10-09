package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Objects {

    /* loaded from: classes.dex */
    public static final class ToStringHelper {
        private final List zza;
        private final Object zzb;

        /* synthetic */ ToStringHelper(Object obj, zzah zzahVar) {
            Preconditions.checkNotNull(obj);
            this.zzb = obj;
            this.zza = new ArrayList();
        }

        public ToStringHelper add(String str, Object obj) {
            List list = this.zza;
            Preconditions.checkNotNull(str);
            list.add(str + "=" + String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(100);
            sb.append(this.zzb.getClass().getSimpleName());
            sb.append('{');
            int size = this.zza.size();
            for (int i = 0; i < size; i++) {
                sb.append((String) this.zza.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj, null);
    }
}
