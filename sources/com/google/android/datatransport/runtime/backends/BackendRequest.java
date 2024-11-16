package com.google.android.datatransport.runtime.backends;

import com.google.android.datatransport.runtime.backends.AutoValue_BackendRequest;

/* loaded from: classes.dex */
public abstract class BackendRequest {

    public static abstract class Builder {
        public abstract BackendRequest build();

        public abstract Builder setEvents(Iterable iterable);

        public abstract Builder setExtras(byte[] bArr);
    }

    public static Builder builder() {
        return new AutoValue_BackendRequest.Builder();
    }

    public abstract Iterable getEvents();

    public abstract byte[] getExtras();
}
