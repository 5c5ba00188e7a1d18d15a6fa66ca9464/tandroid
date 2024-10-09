package com.google.android.play.integrity.internal;

/* loaded from: classes.dex */
public final class al implements ak {
    private static final al a = new al(null);
    private final Object b;

    private al(Object obj) {
        this.b = obj;
    }

    public static ak b(Object obj) {
        if (obj != null) {
            return new al(obj);
        }
        throw new NullPointerException("instance cannot be null");
    }

    @Override // com.google.android.play.integrity.internal.an
    public final Object a() {
        return this.b;
    }
}
