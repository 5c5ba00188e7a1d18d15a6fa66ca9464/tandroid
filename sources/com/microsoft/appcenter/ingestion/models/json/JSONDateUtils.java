package com.microsoft.appcenter.ingestion.models.json;

import j$.util.DesugarTimeZone;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONException;
/* loaded from: classes.dex */
public abstract class JSONDateUtils {
    private static final ThreadLocal DATE_FORMAT = new ThreadLocal() { // from class: com.microsoft.appcenter.ingestion.models.json.JSONDateUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public DateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            simpleDateFormat.setTimeZone(DesugarTimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    private static void checkNull(Object obj) {
        if (obj == null) {
            throw new JSONException("date cannot be null");
        }
    }

    public static Date toDate(String str) {
        checkNull(str);
        try {
            return ((DateFormat) DATE_FORMAT.get()).parse(str);
        } catch (ParseException e) {
            throw new JSONException(e.getMessage());
        }
    }

    public static String toString(Date date) {
        checkNull(date);
        return ((DateFormat) DATE_FORMAT.get()).format(date);
    }
}
