package com.google.android.datatransport.runtime;

import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.runtime.AutoValue_SendRequest;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class SendRequest {

    /* loaded from: classes.dex */
    public static abstract class Builder {
        public abstract SendRequest build();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Builder setEncoding(Encoding encoding);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Builder setEvent(Event event);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Builder setTransformer(Transformer transformer);

        public abstract Builder setTransportContext(TransportContext transportContext);

        public abstract Builder setTransportName(String str);
    }

    public static Builder builder() {
        return new AutoValue_SendRequest.Builder();
    }

    public abstract Encoding getEncoding();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Event getEvent();

    public byte[] getPayload() {
        return (byte[]) getTransformer().apply(getEvent().getPayload());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Transformer getTransformer();

    public abstract TransportContext getTransportContext();

    public abstract String getTransportName();
}
