package com.google.common.base;

import java.io.IOException;
import java.util.Iterator;
/* loaded from: classes.dex */
public class Joiner {
    private final String separator;

    public static Joiner on(String str) {
        return new Joiner(str);
    }

    public static Joiner on(char c) {
        return new Joiner(String.valueOf(c));
    }

    private Joiner(String str) {
        this.separator = (String) Preconditions.checkNotNull(str);
    }

    public <A extends Appendable> A appendTo(A a, Iterator<? extends Object> it) throws IOException {
        Preconditions.checkNotNull(a);
        if (it.hasNext()) {
            a.append(toString(it.next()));
            while (it.hasNext()) {
                a.append(this.separator);
                a.append(toString(it.next()));
            }
        }
        return a;
    }

    public final StringBuilder appendTo(StringBuilder sb, Iterable<? extends Object> iterable) {
        return appendTo(sb, iterable.iterator());
    }

    public final StringBuilder appendTo(StringBuilder sb, Iterator<? extends Object> it) {
        try {
            appendTo((Joiner) sb, it);
            return sb;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final String join(Iterable<? extends Object> iterable) {
        return join(iterable.iterator());
    }

    public final String join(Iterator<? extends Object> it) {
        return appendTo(new StringBuilder(), it).toString();
    }

    CharSequence toString(Object obj) {
        java.util.Objects.requireNonNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
