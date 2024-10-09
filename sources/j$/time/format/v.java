package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
import java.text.DateFormatSymbols;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
class v {
    private static final ConcurrentHashMap a = new ConcurrentHashMap(16, 0.75f, 2);
    private static final Comparator b = new t();
    public static final /* synthetic */ int c = 0;

    private static String b(String str) {
        return str.substring(0, Character.charCount(str.codePointAt(0)));
    }

    public String c(j$.time.temporal.l lVar, long j, TextStyle textStyle, Locale locale) {
        Object obj;
        AbstractMap.SimpleImmutableEntry simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry(lVar, locale);
        ConcurrentHashMap concurrentHashMap = a;
        Object obj2 = concurrentHashMap.get(simpleImmutableEntry);
        if (obj2 == null) {
            HashMap hashMap = new HashMap();
            int i = 0;
            if (lVar == j$.time.temporal.a.ERA) {
                DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
                HashMap hashMap2 = new HashMap();
                HashMap hashMap3 = new HashMap();
                String[] eras = dateFormatSymbols.getEras();
                while (i < eras.length) {
                    if (!eras[i].isEmpty()) {
                        long j2 = i;
                        hashMap2.put(Long.valueOf(j2), eras[i]);
                        hashMap3.put(Long.valueOf(j2), b(eras[i]));
                    }
                    i++;
                }
                if (!hashMap2.isEmpty()) {
                    hashMap.put(TextStyle.FULL, hashMap2);
                    hashMap.put(TextStyle.SHORT, hashMap2);
                    hashMap.put(TextStyle.NARROW, hashMap3);
                }
                obj = new u(hashMap);
            } else if (lVar == j$.time.temporal.a.MONTH_OF_YEAR) {
                DateFormatSymbols dateFormatSymbols2 = DateFormatSymbols.getInstance(locale);
                HashMap hashMap4 = new HashMap();
                HashMap hashMap5 = new HashMap();
                String[] months = dateFormatSymbols2.getMonths();
                for (int i2 = 0; i2 < months.length; i2++) {
                    if (!months[i2].isEmpty()) {
                        long j3 = i2 + 1;
                        hashMap4.put(Long.valueOf(j3), months[i2]);
                        hashMap5.put(Long.valueOf(j3), b(months[i2]));
                    }
                }
                if (!hashMap4.isEmpty()) {
                    hashMap.put(TextStyle.FULL, hashMap4);
                    hashMap.put(TextStyle.NARROW, hashMap5);
                }
                HashMap hashMap6 = new HashMap();
                String[] shortMonths = dateFormatSymbols2.getShortMonths();
                while (i < shortMonths.length) {
                    if (!shortMonths[i].isEmpty()) {
                        hashMap6.put(Long.valueOf(i + 1), shortMonths[i]);
                    }
                    i++;
                }
                if (!hashMap6.isEmpty()) {
                    hashMap.put(TextStyle.SHORT, hashMap6);
                }
                obj = new u(hashMap);
            } else if (lVar == j$.time.temporal.a.DAY_OF_WEEK) {
                DateFormatSymbols dateFormatSymbols3 = DateFormatSymbols.getInstance(locale);
                HashMap hashMap7 = new HashMap();
                String[] weekdays = dateFormatSymbols3.getWeekdays();
                hashMap7.put(1L, weekdays[2]);
                hashMap7.put(2L, weekdays[3]);
                hashMap7.put(3L, weekdays[4]);
                hashMap7.put(4L, weekdays[5]);
                hashMap7.put(5L, weekdays[6]);
                hashMap7.put(6L, weekdays[7]);
                hashMap7.put(7L, weekdays[1]);
                hashMap.put(TextStyle.FULL, hashMap7);
                HashMap hashMap8 = new HashMap();
                hashMap8.put(1L, b(weekdays[2]));
                hashMap8.put(2L, b(weekdays[3]));
                hashMap8.put(3L, b(weekdays[4]));
                hashMap8.put(4L, b(weekdays[5]));
                hashMap8.put(5L, b(weekdays[6]));
                hashMap8.put(6L, b(weekdays[7]));
                hashMap8.put(7L, b(weekdays[1]));
                hashMap.put(TextStyle.NARROW, hashMap8);
                HashMap hashMap9 = new HashMap();
                String[] shortWeekdays = dateFormatSymbols3.getShortWeekdays();
                hashMap9.put(1L, shortWeekdays[2]);
                hashMap9.put(2L, shortWeekdays[3]);
                hashMap9.put(3L, shortWeekdays[4]);
                hashMap9.put(4L, shortWeekdays[5]);
                hashMap9.put(5L, shortWeekdays[6]);
                hashMap9.put(6L, shortWeekdays[7]);
                hashMap9.put(7L, shortWeekdays[1]);
                hashMap.put(TextStyle.SHORT, hashMap9);
                obj = new u(hashMap);
            } else if (lVar == j$.time.temporal.a.AMPM_OF_DAY) {
                DateFormatSymbols dateFormatSymbols4 = DateFormatSymbols.getInstance(locale);
                HashMap hashMap10 = new HashMap();
                HashMap hashMap11 = new HashMap();
                String[] amPmStrings = dateFormatSymbols4.getAmPmStrings();
                while (i < amPmStrings.length) {
                    if (!amPmStrings[i].isEmpty()) {
                        long j4 = i;
                        hashMap10.put(Long.valueOf(j4), amPmStrings[i]);
                        hashMap11.put(Long.valueOf(j4), b(amPmStrings[i]));
                    }
                    i++;
                }
                if (!hashMap10.isEmpty()) {
                    hashMap.put(TextStyle.FULL, hashMap10);
                    hashMap.put(TextStyle.SHORT, hashMap10);
                    hashMap.put(TextStyle.NARROW, hashMap11);
                }
                obj = new u(hashMap);
            } else {
                obj = "";
            }
            concurrentHashMap.putIfAbsent(simpleImmutableEntry, obj);
            obj2 = concurrentHashMap.get(simpleImmutableEntry);
        }
        if (obj2 instanceof u) {
            return ((u) obj2).a(j, textStyle);
        }
        return null;
    }
}
