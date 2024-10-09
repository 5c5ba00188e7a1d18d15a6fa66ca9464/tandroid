package com.google.android.gms.internal.clearcut;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public abstract class zzy {
    private static HashMap zzcu;
    private static Object zzcz;
    private static boolean zzda;
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static final Uri zzcq = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern zzcr = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern zzcs = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    private static final AtomicBoolean zzct = new AtomicBoolean();
    private static final HashMap zzcv = new HashMap();
    private static final HashMap zzcw = new HashMap();
    private static final HashMap zzcx = new HashMap();
    private static final HashMap zzcy = new HashMap();
    private static String[] zzdb = new String[0];

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        Object zzb = zzb(contentResolver);
        long j2 = 0;
        Long l = (Long) zza(zzcx, str, (Object) 0L);
        if (l != null) {
            return l.longValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null) {
            try {
                long parseLong = Long.parseLong(zza);
                l = Long.valueOf(parseLong);
                j2 = parseLong;
            } catch (NumberFormatException unused) {
            }
        }
        zza(zzb, zzcx, str, l);
        return j2;
    }

    private static Object zza(HashMap hashMap, String str, Object obj) {
        synchronized (zzy.class) {
            try {
                if (!hashMap.containsKey(str)) {
                    return null;
                }
                Object obj2 = hashMap.get(str);
                if (obj2 != null) {
                    obj = obj2;
                }
                return obj;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static String zza(ContentResolver contentResolver, String str, String str2) {
        synchronized (zzy.class) {
            try {
                zza(contentResolver);
                Object obj = zzcz;
                if (zzcu.containsKey(str)) {
                    String str3 = (String) zzcu.get(str);
                    return str3 != null ? str3 : null;
                }
                for (String str4 : zzdb) {
                    if (str.startsWith(str4)) {
                        if (!zzda || zzcu.isEmpty()) {
                            zzcu.putAll(zza(contentResolver, zzdb));
                            zzda = true;
                            if (zzcu.containsKey(str)) {
                                String str5 = (String) zzcu.get(str);
                                return str5 != null ? str5 : null;
                            }
                        }
                        return null;
                    }
                }
                Cursor query = contentResolver.query(CONTENT_URI, null, null, new String[]{str}, null);
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            String string = query.getString(1);
                            if (string != null && string.equals(null)) {
                                string = null;
                            }
                            zza(obj, str, string);
                            String str6 = string != null ? string : null;
                            query.close();
                            return str6;
                        }
                    } finally {
                        if (query != null) {
                            query.close();
                        }
                    }
                }
                zza(obj, str, (String) null);
                if (query != null) {
                    query.close();
                }
                return null;
            } finally {
            }
        }
    }

    private static Map zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzcq, null, null, strArr, null);
        TreeMap treeMap = new TreeMap();
        if (query == null) {
            return treeMap;
        }
        while (query.moveToNext()) {
            try {
                treeMap.put(query.getString(0), query.getString(1));
            } finally {
                query.close();
            }
        }
        return treeMap;
    }

    private static void zza(ContentResolver contentResolver) {
        if (zzcu == null) {
            zzct.set(false);
            zzcu = new HashMap();
            zzcz = new Object();
            zzda = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzz(null));
            return;
        }
        if (zzct.getAndSet(false)) {
            zzcu.clear();
            zzcv.clear();
            zzcw.clear();
            zzcx.clear();
            zzcy.clear();
            zzcz = new Object();
            zzda = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzy.class) {
            try {
                if (obj == zzcz) {
                    zzcu.put(str, str2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static void zza(Object obj, HashMap hashMap, String str, Object obj2) {
        synchronized (zzy.class) {
            try {
                if (obj == zzcz) {
                    hashMap.put(str, obj2);
                    zzcu.remove(str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        HashMap hashMap = zzcv;
        Boolean bool = (Boolean) zza(hashMap, str, Boolean.valueOf(z));
        if (bool != null) {
            return bool.booleanValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null && !zza.equals("")) {
            if (zzcr.matcher(zza).matches()) {
                bool = Boolean.TRUE;
                z = true;
            } else if (zzcs.matcher(zza).matches()) {
                bool = Boolean.FALSE;
                z = false;
            } else {
                Log.w("Gservices", "attempt to read gservices key " + str + " (value \"" + zza + "\") as boolean");
            }
        }
        zza(zzb, hashMap, str, bool);
        return z;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzy.class) {
            zza(contentResolver);
            obj = zzcz;
        }
        return obj;
    }
}
