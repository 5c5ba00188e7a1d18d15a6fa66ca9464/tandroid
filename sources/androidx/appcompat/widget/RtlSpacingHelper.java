package androidx.appcompat.widget;
/* loaded from: classes.dex */
class RtlSpacingHelper {
    private int mLeft = 0;
    private int mRight = 0;
    private int mStart = Integer.MIN_VALUE;
    private int mEnd = Integer.MIN_VALUE;
    private int mExplicitLeft = 0;
    private int mExplicitRight = 0;
    private boolean mIsRtl = false;
    private boolean mIsRelative = false;

    public int getEnd() {
        return this.mIsRtl ? this.mLeft : this.mRight;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getRight() {
        return this.mRight;
    }

    public int getStart() {
        return this.mIsRtl ? this.mRight : this.mLeft;
    }

    public void setAbsolute(int i, int i2) {
        this.mIsRelative = false;
        if (i != Integer.MIN_VALUE) {
            this.mExplicitLeft = i;
            this.mLeft = i;
        }
        if (i2 != Integer.MIN_VALUE) {
            this.mExplicitRight = i2;
            this.mRight = i2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x001a, code lost:
        if (r2 != Integer.MIN_VALUE) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x002a, code lost:
        if (r2 != Integer.MIN_VALUE) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setDirection(boolean z) {
        int i;
        if (z == this.mIsRtl) {
            return;
        }
        this.mIsRtl = z;
        if (this.mIsRelative) {
            if (z) {
                int i2 = this.mEnd;
                if (i2 == Integer.MIN_VALUE) {
                    i2 = this.mExplicitLeft;
                }
                this.mLeft = i2;
                i = this.mStart;
            } else {
                int i3 = this.mStart;
                if (i3 == Integer.MIN_VALUE) {
                    i3 = this.mExplicitLeft;
                }
                this.mLeft = i3;
                i = this.mEnd;
            }
            this.mRight = i;
        }
        this.mLeft = this.mExplicitLeft;
        i = this.mExplicitRight;
        this.mRight = i;
    }

    public void setRelative(int i, int i2) {
        this.mStart = i;
        this.mEnd = i2;
        this.mIsRelative = true;
        if (this.mIsRtl) {
            if (i2 != Integer.MIN_VALUE) {
                this.mLeft = i2;
            }
            if (i != Integer.MIN_VALUE) {
                this.mRight = i;
                return;
            }
            return;
        }
        if (i != Integer.MIN_VALUE) {
            this.mLeft = i;
        }
        if (i2 != Integer.MIN_VALUE) {
            this.mRight = i2;
        }
    }
}
