package com.google.common.base;

import java.io.IOException;
import java.util.Iterator;

/* loaded from: classes.dex */
public class Joiner {
    private final String separator;

    private Joiner(String str) {
        this.separator = (String) Preconditions.checkNotNull(str);
    }

    public static Joiner on(char c) {
        return new Joiner(String.valueOf(c));
    }

    public Appendable appendTo(Appendable appendable, Iterator it) {
        Preconditions.checkNotNull(appendable);
        if (it.hasNext()) {
            while (true) {
                appendable.append(toString(it.next()));
                if (!it.hasNext()) {
                    break;
                }
                appendable.append(this.separator);
            }
        }
        return appendable;
    }

    public final StringBuilder appendTo(StringBuilder sb, Iterable iterable) {
        return appendTo(sb, iterable.iterator());
    }

    public final StringBuilder appendTo(StringBuilder sb, Iterator it) {
        try {
            appendTo((Appendable) sb, it);
            return sb;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    CharSequence toString(Object obj) {
        java.util.Objects.requireNonNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
