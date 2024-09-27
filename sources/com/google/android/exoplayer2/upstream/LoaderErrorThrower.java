package com.google.android.exoplayer2.upstream;
/* loaded from: classes.dex */
public interface LoaderErrorThrower {

    /* loaded from: classes.dex */
    public static final class Placeholder implements LoaderErrorThrower {
        @Override // com.google.android.exoplayer2.upstream.LoaderErrorThrower
        public void maybeThrowError() {
        }
    }

    void maybeThrowError();
}
