package com.google.android.exoplayer2.util;

import android.util.SparseBooleanArray;

/* loaded from: classes.dex */
public final class FlagSet {
    private final SparseBooleanArray flags;

    /* loaded from: classes.dex */
    public static final class Builder {
        private boolean buildCalled;
        private final SparseBooleanArray flags = new SparseBooleanArray();

        public Builder add(int i) {
            Assertions.checkState(!this.buildCalled);
            this.flags.append(i, true);
            return this;
        }

        public Builder addAll(FlagSet flagSet) {
            for (int i = 0; i < flagSet.size(); i++) {
                add(flagSet.get(i));
            }
            return this;
        }

        public Builder addAll(int... iArr) {
            for (int i : iArr) {
                add(i);
            }
            return this;
        }

        public Builder addIf(int i, boolean z) {
            return z ? add(i) : this;
        }

        public FlagSet build() {
            Assertions.checkState(!this.buildCalled);
            this.buildCalled = true;
            return new FlagSet(this.flags);
        }
    }

    private FlagSet(SparseBooleanArray sparseBooleanArray) {
        this.flags = sparseBooleanArray;
    }

    public boolean contains(int i) {
        return this.flags.get(i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FlagSet)) {
            return false;
        }
        FlagSet flagSet = (FlagSet) obj;
        if (Util.SDK_INT >= 24) {
            return this.flags.equals(flagSet.flags);
        }
        if (size() != flagSet.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (get(i) != flagSet.get(i)) {
                return false;
            }
        }
        return true;
    }

    public int get(int i) {
        Assertions.checkIndex(i, 0, size());
        return this.flags.keyAt(i);
    }

    public int hashCode() {
        if (Util.SDK_INT >= 24) {
            return this.flags.hashCode();
        }
        int size = size();
        for (int i = 0; i < size(); i++) {
            size = (size * 31) + get(i);
        }
        return size;
    }

    public int size() {
        return this.flags.size();
    }
}
