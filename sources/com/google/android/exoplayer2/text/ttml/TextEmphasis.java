package com.google.android.exoplayer2.text.ttml;

import android.text.TextUtils;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
final class TextEmphasis {
    public final int markFill;
    public final int markShape;
    public final int position;
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final ImmutableSet<String> SINGLE_STYLE_VALUES = ImmutableSet.of("auto", "none");
    private static final ImmutableSet<String> MARK_SHAPE_VALUES = ImmutableSet.of("dot", "sesame", "circle");
    private static final ImmutableSet<String> MARK_FILL_VALUES = ImmutableSet.of("filled", "open");
    private static final ImmutableSet<String> POSITION_VALUES = ImmutableSet.of("after", "before", "outside");

    private TextEmphasis(int i, int i2, int i3) {
        this.markShape = i;
        this.markFill = i2;
        this.position = i3;
    }

    public static TextEmphasis parse(String str) {
        if (str == null) {
            return null;
        }
        String lowerCase = Ascii.toLowerCase(str.trim());
        if (lowerCase.isEmpty()) {
            return null;
        }
        return parseWords(ImmutableSet.copyOf(TextUtils.split(lowerCase, WHITESPACE_PATTERN)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x00eb, code lost:
        if (r9.equals("dot") != false) goto L47;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0103  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TextEmphasis parseWords(ImmutableSet<String> immutableSet) {
        char c;
        int i;
        int hashCode;
        String str = (String) Iterables.getFirst(Sets.intersection(POSITION_VALUES, immutableSet), "outside");
        int hashCode2 = str.hashCode();
        char c2 = 0;
        int i2 = -1;
        if (hashCode2 == -1392885889) {
            if (str.equals("before")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode2 != -1106037339) {
            if (hashCode2 == 92734940 && str.equals("after")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (str.equals("outside")) {
                c = 1;
            }
            c = 65535;
        }
        int i3 = c != 0 ? c != 1 ? 1 : -2 : 2;
        Sets.SetView intersection = Sets.intersection(SINGLE_STYLE_VALUES, immutableSet);
        if (!intersection.isEmpty()) {
            String str2 = (String) intersection.iterator().next();
            int hashCode3 = str2.hashCode();
            if (hashCode3 == 3005871) {
                str2.equals("auto");
            } else if (hashCode3 == 3387192 && str2.equals("none")) {
                i2 = 0;
            }
            return new TextEmphasis(i2, 0, i3);
        }
        Sets.SetView intersection2 = Sets.intersection(MARK_FILL_VALUES, immutableSet);
        Sets.SetView intersection3 = Sets.intersection(MARK_SHAPE_VALUES, immutableSet);
        if (intersection2.isEmpty() && intersection3.isEmpty()) {
            return new TextEmphasis(-1, 0, i3);
        }
        String str3 = (String) Iterables.getFirst(intersection2, "filled");
        int hashCode4 = str3.hashCode();
        if (hashCode4 == -1274499742) {
            str3.equals("filled");
        } else if (hashCode4 == 3417674 && str3.equals("open")) {
            i = 2;
            String str4 = (String) Iterables.getFirst(intersection3, "circle");
            hashCode = str4.hashCode();
            if (hashCode != -1360216880) {
                if (str4.equals("circle")) {
                    c2 = 2;
                }
                c2 = 65535;
            } else if (hashCode != -905816648) {
                if (hashCode == 99657) {
                }
                c2 = 65535;
            } else {
                if (str4.equals("sesame")) {
                    c2 = 1;
                }
                c2 = 65535;
            }
            return new TextEmphasis(c2 != 0 ? c2 != 1 ? 1 : 3 : 2, i, i3);
        }
        i = 1;
        String str42 = (String) Iterables.getFirst(intersection3, "circle");
        hashCode = str42.hashCode();
        if (hashCode != -1360216880) {
        }
        return new TextEmphasis(c2 != 0 ? c2 != 1 ? 1 : 3 : 2, i, i3);
    }
}
