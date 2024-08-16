package com.google.common.base;
/* loaded from: classes.dex */
public abstract class CharMatcher implements Predicate<Character> {
    public abstract boolean matches(char c);

    public static CharMatcher none() {
        return None.INSTANCE;
    }

    public static CharMatcher is(char c) {
        return new Is(c);
    }

    protected CharMatcher() {
    }

    public int indexIn(CharSequence charSequence, int i) {
        int length = charSequence.length();
        Preconditions.checkPositionIndex(i, length);
        while (i < length) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Deprecated
    public boolean apply(Character ch) {
        return matches(ch.charValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String showCharacter(char c) {
        char[] cArr = new char[6];
        cArr[0] = '\\';
        cArr[1] = 'u';
        cArr[2] = 0;
        cArr[3] = 0;
        cArr[4] = 0;
        cArr[5] = 0;
        for (int i = 0; i < 4; i++) {
            cArr[5 - i] = "0123456789ABCDEF".charAt(c & 15);
            c = (char) (c >> 4);
        }
        return String.copyValueOf(cArr);
    }

    /* loaded from: classes.dex */
    static abstract class FastMatcher extends CharMatcher {
        FastMatcher() {
        }

        @Override // com.google.common.base.Predicate
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character ch) {
            return super.apply(ch);
        }
    }

    /* loaded from: classes.dex */
    static abstract class NamedFastMatcher extends FastMatcher {
        private final String description;

        NamedFastMatcher(String str) {
            this.description = (String) Preconditions.checkNotNull(str);
        }

        public final String toString() {
            return this.description;
        }
    }

    /* loaded from: classes.dex */
    private static final class None extends NamedFastMatcher {
        static final None INSTANCE = new None();

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return false;
        }

        private None() {
            super("CharMatcher.none()");
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence charSequence, int i) {
            Preconditions.checkPositionIndex(i, charSequence.length());
            return -1;
        }
    }

    /* loaded from: classes.dex */
    private static final class Is extends FastMatcher {
        private final char match;

        Is(char c) {
            this.match = c;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return c == this.match;
        }

        public String toString() {
            String showCharacter = CharMatcher.showCharacter(this.match);
            StringBuilder sb = new StringBuilder(String.valueOf(showCharacter).length() + 18);
            sb.append("CharMatcher.is('");
            sb.append(showCharacter);
            sb.append("')");
            return sb.toString();
        }
    }
}
