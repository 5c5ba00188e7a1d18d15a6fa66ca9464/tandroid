package com.google.android.datatransport;

import java.util.Objects;
/* loaded from: classes.dex */
public final class Encoding {
    private final String name;

    public static Encoding of(String str) {
        return new Encoding(str);
    }

    public String getName() {
        return this.name;
    }

    private Encoding(String str) {
        Objects.requireNonNull(str, "name is null");
        this.name = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Encoding) {
            return this.name.equals(((Encoding) obj).name);
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode() ^ 1000003;
    }

    public String toString() {
        return "Encoding{name=\"" + this.name + "\"}";
    }
}
