package com.google.android.play.integrity.internal;

import java.util.Objects;
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
public final class al implements ak {
    private final Object b;

    static {
        new al(null);
    }

    private al(Object obj) {
        this.b = obj;
    }

    public static ak b(Object obj) {
        Objects.requireNonNull(obj, "instance cannot be null");
        return new al(obj);
    }

    @Override // com.google.android.play.integrity.internal.an
    public final Object a() {
        return this.b;
    }
}
