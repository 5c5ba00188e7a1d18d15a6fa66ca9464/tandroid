package com.google.android.gms.internal.play_billing;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzdh {
    private static final char[] zza;

    static {
        char[] cArr = new char[80];
        zza = cArr;
        Arrays.fill(cArr, ' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String zza(zzdf zzdfVar, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        zzd(zzdfVar, sb, 0);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zzb(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object obj2 : (List) obj) {
                zzb(sb, i, str, obj2);
            }
        } else if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                zzb(sb, i, str, entry);
            }
        } else {
            sb.append('\n');
            zzc(i, sb);
            if (!str.isEmpty()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Character.toLowerCase(str.charAt(0)));
                for (int i2 = 1; i2 < str.length(); i2++) {
                    char charAt = str.charAt(i2);
                    if (Character.isUpperCase(charAt)) {
                        sb2.append("_");
                    }
                    sb2.append(Character.toLowerCase(charAt));
                }
                str = sb2.toString();
            }
            sb.append(str);
            if (obj instanceof String) {
                sb.append(": \"");
                sb.append(zzee.zza(new zzax(((String) obj).getBytes(zzcg.zzb))));
                sb.append('\"');
            } else if (obj instanceof zzba) {
                sb.append(": \"");
                sb.append(zzee.zza((zzba) obj));
                sb.append('\"');
            } else if (obj instanceof zzcb) {
                sb.append(" {");
                zzd((zzcb) obj, sb, i + 2);
                sb.append("\n");
                zzc(i, sb);
                sb.append("}");
            } else if (!(obj instanceof Map.Entry)) {
                sb.append(": ");
                sb.append(obj);
            } else {
                sb.append(" {");
                Map.Entry entry2 = (Map.Entry) obj;
                int i3 = i + 2;
                zzb(sb, i3, "key", entry2.getKey());
                zzb(sb, i3, "value", entry2.getValue());
                sb.append("\n");
                zzc(i, sb);
                sb.append("}");
            }
        }
    }

    private static void zzc(int i, StringBuilder sb) {
        while (i > 0) {
            int i2 = 80;
            if (i <= 80) {
                i2 = i;
            }
            sb.append(zza, 0, i2);
            i -= i2;
        }
    }

    private static void zzd(zzdf zzdfVar, StringBuilder sb, int i) {
        int i2;
        Object obj;
        Method method;
        String substring;
        Object zzl;
        Method method2;
        HashSet hashSet = new HashSet();
        HashMap hashMap = new HashMap();
        TreeMap treeMap = new TreeMap();
        Method[] declaredMethods = zzdfVar.getClass().getDeclaredMethods();
        int length = declaredMethods.length;
        int i3 = 0;
        while (true) {
            i2 = 3;
            if (i3 >= length) {
                break;
            }
            Method method3 = declaredMethods[i3];
            if (!Modifier.isStatic(method3.getModifiers()) && method3.getName().length() >= 3) {
                if (method3.getName().startsWith("set")) {
                    hashSet.add(method3.getName());
                } else if (Modifier.isPublic(method3.getModifiers()) && method3.getParameterTypes().length == 0) {
                    if (method3.getName().startsWith("has")) {
                        hashMap.put(method3.getName(), method3);
                    } else if (method3.getName().startsWith("get")) {
                        treeMap.put(method3.getName(), method3);
                    }
                }
            }
            i3++;
        }
        for (Map.Entry entry : treeMap.entrySet()) {
            String substring2 = ((String) entry.getKey()).substring(i2);
            if (substring2.endsWith("List") && !substring2.endsWith("OrBuilderList") && !substring2.equals("List") && (method2 = (Method) entry.getValue()) != null && method2.getReturnType().equals(List.class)) {
                substring = substring2.substring(0, substring2.length() - 4);
                zzl = zzcb.zzl(method2, zzdfVar, new Object[0]);
            } else if (!substring2.endsWith("Map") || substring2.equals("Map") || (method = (Method) entry.getValue()) == null || !method.getReturnType().equals(Map.class) || method.isAnnotationPresent(Deprecated.class) || !Modifier.isPublic(method.getModifiers())) {
                if (hashSet.contains("set".concat(substring2)) && (!substring2.endsWith("Bytes") || !treeMap.containsKey("get".concat(String.valueOf(substring2.substring(0, substring2.length() - 5)))))) {
                    Method method4 = (Method) entry.getValue();
                    Method method5 = (Method) hashMap.get("has".concat(substring2));
                    if (method4 != null) {
                        Object zzl2 = zzcb.zzl(method4, zzdfVar, new Object[0]);
                        if (method5 != null) {
                            if (!((Boolean) zzcb.zzl(method5, zzdfVar, new Object[0])).booleanValue()) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        } else if (zzl2 instanceof Boolean) {
                            if (!((Boolean) zzl2).booleanValue()) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        } else if (zzl2 instanceof Integer) {
                            if (((Integer) zzl2).intValue() == 0) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        } else if (zzl2 instanceof Float) {
                            if (Float.floatToRawIntBits(((Float) zzl2).floatValue()) == 0) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        } else if (zzl2 instanceof Double) {
                            if (Double.doubleToRawLongBits(((Double) zzl2).doubleValue()) == 0) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        } else {
                            if (zzl2 instanceof String) {
                                obj = "";
                            } else if (zzl2 instanceof zzba) {
                                obj = zzba.zzb;
                            } else if (zzl2 instanceof zzdf) {
                                if (zzl2 == ((zzdf) zzl2).zzf()) {
                                }
                                zzb(sb, i, substring2, zzl2);
                            } else {
                                if ((zzl2 instanceof Enum) && ((Enum) zzl2).ordinal() == 0) {
                                }
                                zzb(sb, i, substring2, zzl2);
                            }
                            if (zzl2.equals(obj)) {
                            }
                            zzb(sb, i, substring2, zzl2);
                        }
                    }
                }
                i2 = 3;
            } else {
                substring = substring2.substring(0, substring2.length() - 3);
                zzl = zzcb.zzl(method, zzdfVar, new Object[0]);
            }
            zzb(sb, i, substring, zzl);
            i2 = 3;
        }
        zzeh zzehVar = ((zzcb) zzdfVar).zzc;
        if (zzehVar != null) {
            zzehVar.zzi(sb, i);
        }
    }
}
