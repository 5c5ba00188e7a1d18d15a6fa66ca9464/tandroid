package com.google.android.exoplayer2.util;
/* loaded from: classes.dex */
public final class Size {
    public static final Size UNKNOWN = new Size(-1, -1);
    private final int height;
    private final int width;

    static {
        new Size(0, 0);
    }

    public Size(int i, int i2) {
        Assertions.checkArgument((i == -1 || i >= 0) && (i2 == -1 || i2 >= 0));
        this.width = i;
        this.height = i2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Size) {
            Size size = (Size) obj;
            return this.width == size.width && this.height == size.height;
        }
        return false;
    }

    public String toString() {
        return this.width + "x" + this.height;
    }

    public int hashCode() {
        int i = this.height;
        int i2 = this.width;
        return i ^ ((i2 >>> 16) | (i2 << 16));
    }
}
