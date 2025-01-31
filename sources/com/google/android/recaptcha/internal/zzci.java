package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzci {
    public static final zzci zza = new zzci();

    private zzci() {
    }

    public static final Class zza(Object obj) {
        if (obj instanceof Class) {
            return (Class) obj;
        }
        if (obj instanceof Integer) {
            int intValue = ((Number) obj).intValue();
            Class cls = intValue == 1 ? Integer.TYPE : intValue == 2 ? Short.TYPE : intValue == 3 ? Byte.TYPE : intValue == 4 ? Long.TYPE : intValue == 5 ? Character.TYPE : intValue == 6 ? Float.TYPE : intValue == 7 ? Double.TYPE : intValue == 8 ? Boolean.TYPE : null;
            if (cls != null) {
                return cls;
            }
            throw new zzae(4, 6, null);
        }
        if (!(obj instanceof String)) {
            throw new zzae(4, 5, null);
        }
        try {
            String str = (String) obj;
            Class<?> cls2 = Class.forName(str);
            if (zzcb.zzb(str)) {
                return cls2;
            }
            throw new zzae(6, 47, null);
        } catch (Exception e) {
            throw new zzae(6, 8, e);
        }
    }
}
