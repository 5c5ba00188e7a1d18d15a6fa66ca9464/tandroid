package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public class Label {
    private static final String FIELD_LANGUAGE_INDEX = Util.intToStringMaxRadix(0);
    private static final String FIELD_VALUE_INDEX = Util.intToStringMaxRadix(1);
    public final String language;
    public final String value;

    public Label(String str, String str2) {
        this.language = Util.normalizeLanguageCode(str);
        this.value = str2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Label label = (Label) obj;
        return Util.areEqual(this.language, label.language) && Util.areEqual(this.value, label.value);
    }

    public int hashCode() {
        int hashCode = this.value.hashCode() * 31;
        String str = this.language;
        return hashCode + (str != null ? str.hashCode() : 0);
    }
}
