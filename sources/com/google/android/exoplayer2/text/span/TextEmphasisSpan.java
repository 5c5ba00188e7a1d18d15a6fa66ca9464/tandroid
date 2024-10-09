package com.google.android.exoplayer2.text.span;

/* loaded from: classes.dex */
public final class TextEmphasisSpan {
    public int markFill;
    public int markShape;
    public final int position;

    public TextEmphasisSpan(int i, int i2, int i3) {
        this.markShape = i;
        this.markFill = i2;
        this.position = i3;
    }
}
