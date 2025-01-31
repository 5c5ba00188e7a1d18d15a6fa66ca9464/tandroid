package com.google.android.recaptcha.internal;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes.dex */
final class zzkg {
    private static final char[] zza;

    static {
        char[] cArr = new char[80];
        zza = cArr;
        Arrays.fill(cArr, ' ');
    }

    static String zza(zzke zzkeVar, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        zzd(zzkeVar, sb, 0);
        return sb.toString();
    }

    static void zzb(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            Iterator it = ((List) obj).iterator();
            while (it.hasNext()) {
                zzb(sb, i, str, it.next());
            }
            return;
        }
        if (obj instanceof Map) {
            Iterator it2 = ((Map) obj).entrySet().iterator();
            while (it2.hasNext()) {
                zzb(sb, i, str, (Map.Entry) it2.next());
            }
            return;
        }
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
            sb.append(zzlg.zza(new zzgt(((String) obj).getBytes(zzjc.zzb))));
            sb.append('\"');
            return;
        }
        if (obj instanceof zzgw) {
            sb.append(": \"");
            sb.append(zzlg.zza((zzgw) obj));
            sb.append('\"');
            return;
        }
        if (obj instanceof zzit) {
            sb.append(" {");
            zzd((zzit) obj, sb, i + 2);
            sb.append("\n");
            zzc(i, sb);
            sb.append("}");
            return;
        }
        if (!(obj instanceof Map.Entry)) {
            sb.append(": ");
            sb.append(obj);
            return;
        }
        int i3 = i + 2;
        sb.append(" {");
        Map.Entry entry = (Map.Entry) obj;
        zzb(sb, i3, "key", entry.getKey());
        zzb(sb, i3, "value", entry.getValue());
        sb.append("\n");
        zzc(i, sb);
        sb.append("}");
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

    private static void zzd(zzke zzkeVar, StringBuilder sb, int i) {
        int i2;
        Object obj;
        Method method;
        String substring;
        Object zzz;
        Method method2;
        HashSet hashSet = new HashSet();
        HashMap hashMap = new HashMap();
        TreeMap treeMap = new TreeMap();
        Method[] declaredMethods = zzkeVar.getClass().getDeclaredMethods();
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
                zzz = zzit.zzz(method2, zzkeVar, new Object[0]);
            } else if (!substring2.endsWith("Map") || substring2.equals("Map") || (method = (Method) entry.getValue()) == null || !method.getReturnType().equals(Map.class) || method.isAnnotationPresent(Deprecated.class) || !Modifier.isPublic(method.getModifiers())) {
                if (hashSet.contains("set".concat(substring2)) && (!substring2.endsWith("Bytes") || !treeMap.containsKey("get".concat(String.valueOf(substring2.substring(0, substring2.length() - 5)))))) {
                    Method method4 = (Method) entry.getValue();
                    Method method5 = (Method) hashMap.get("has".concat(substring2));
                    if (method4 != null) {
                        Object zzz2 = zzit.zzz(method4, zzkeVar, new Object[0]);
                        if (method5 != null) {
                            if (!((Boolean) zzit.zzz(method5, zzkeVar, new Object[0])).booleanValue()) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        } else if (zzz2 instanceof Boolean) {
                            if (!((Boolean) zzz2).booleanValue()) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        } else if (zzz2 instanceof Integer) {
                            if (((Integer) zzz2).intValue() == 0) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        } else if (zzz2 instanceof Float) {
                            if (Float.floatToRawIntBits(((Float) zzz2).floatValue()) == 0) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        } else if (zzz2 instanceof Double) {
                            if (Double.doubleToRawLongBits(((Double) zzz2).doubleValue()) == 0) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        } else {
                            if (zzz2 instanceof String) {
                                obj = "";
                            } else if (zzz2 instanceof zzgw) {
                                obj = zzgw.zzb;
                            } else if (zzz2 instanceof zzke) {
                                if (zzz2 == ((zzke) zzz2).zzY()) {
                                }
                                zzb(sb, i, substring2, zzz2);
                            } else {
                                if ((zzz2 instanceof Enum) && ((Enum) zzz2).ordinal() == 0) {
                                }
                                zzb(sb, i, substring2, zzz2);
                            }
                            if (zzz2.equals(obj)) {
                            }
                            zzb(sb, i, substring2, zzz2);
                        }
                    }
                }
                i2 = 3;
            } else {
                substring = substring2.substring(0, substring2.length() - 3);
                zzz = zzit.zzz(method, zzkeVar, new Object[0]);
            }
            zzb(sb, i, substring, zzz);
            i2 = 3;
        }
        if (zzkeVar instanceof zzip) {
            Iterator zzf = ((zzip) zzkeVar).zzb.zzf();
            while (zzf.hasNext()) {
                Map.Entry entry2 = (Map.Entry) zzf.next();
                zzb(sb, i, "[" + ((zziq) entry2.getKey()).zza + "]", entry2.getValue());
            }
        }
        zzlm zzlmVar = ((zzit) zzkeVar).zzc;
        if (zzlmVar != null) {
            zzlmVar.zzi(sb, i);
        }
    }
}
