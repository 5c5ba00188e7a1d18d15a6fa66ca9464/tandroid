package com.google.android.play.integrity.internal;

import android.os.Bundle;
import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class o extends b implements p {
    public o() {
        super("com.google.android.play.core.integrity.protocol.IIntegrityServiceCallback");
    }

    @Override // com.google.android.play.integrity.internal.b
    protected final boolean a(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i != 2) {
            return false;
        }
        Bundle bundle = (Bundle) c.a(parcel, Bundle.CREATOR);
        c.b(parcel);
        b(bundle);
        return true;
    }
}
