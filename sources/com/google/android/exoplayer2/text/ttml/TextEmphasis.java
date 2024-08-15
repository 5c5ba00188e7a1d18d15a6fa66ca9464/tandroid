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

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0081, code lost:
        if (r9.equals("auto") != false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00fe, code lost:
        if (r9.equals("dot") == false) goto L59;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TextEmphasis parseWords(ImmutableSet<String> immutableSet) {
        char c;
        char c2;
        String str = (String) Iterables.getFirst(Sets.intersection(POSITION_VALUES, immutableSet), "outside");
        int hashCode = str.hashCode();
        char c3 = 0;
        char c4 = 1;
        if (hashCode == -1392885889) {
            if (str.equals("before")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode != -1106037339) {
            if (hashCode == 92734940 && str.equals("after")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (str.equals("outside")) {
                c = 1;
            }
            c = 65535;
        }
        int i = c != 0 ? c != 1 ? 1 : -2 : 2;
        Sets.SetView intersection = Sets.intersection(SINGLE_STYLE_VALUES, immutableSet);
        if (!intersection.isEmpty()) {
            String str2 = (String) intersection.iterator().next();
            int hashCode2 = str2.hashCode();
            if (hashCode2 != 3005871) {
                if (hashCode2 == 3387192 && str2.equals("none")) {
                    c4 = 0;
                }
                c4 = 65535;
            }
            return new TextEmphasis(c4 == 0 ? 0 : -1, 0, i);
        }
        Sets.SetView intersection2 = Sets.intersection(MARK_FILL_VALUES, immutableSet);
        Sets.SetView intersection3 = Sets.intersection(MARK_SHAPE_VALUES, immutableSet);
        if (intersection2.isEmpty() && intersection3.isEmpty()) {
            return new TextEmphasis(-1, 0, i);
        }
        String str3 = (String) Iterables.getFirst(intersection2, "filled");
        int hashCode3 = str3.hashCode();
        if (hashCode3 != -1274499742) {
            if (hashCode3 == 3417674 && str3.equals("open")) {
                c2 = 0;
            }
            c2 = 65535;
        } else {
            if (str3.equals("filled")) {
                c2 = 1;
            }
            c2 = 65535;
        }
        int i2 = c2 != 0 ? 1 : 2;
        String str4 = (String) Iterables.getFirst(intersection3, "circle");
        int hashCode4 = str4.hashCode();
        if (hashCode4 == -1360216880) {
            if (str4.equals("circle")) {
                c3 = 2;
            }
            c3 = 65535;
        } else if (hashCode4 != -905816648) {
            if (hashCode4 == 99657) {
            }
            c3 = 65535;
        } else {
            if (str4.equals("sesame")) {
                c3 = 1;
            }
            c3 = 65535;
        }
        return new TextEmphasis(c3 != 0 ? c3 != 1 ? 1 : 3 : 2, i2, i);
    }
}
