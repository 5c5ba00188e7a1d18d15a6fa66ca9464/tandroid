package com.google.android.datatransport.runtime;

import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.runtime.AutoValue_SendRequest;

/* loaded from: classes.dex */
abstract class SendRequest {

    public static abstract class Builder {
        public abstract SendRequest build();

        abstract Builder setEncoding(Encoding encoding);

        abstract Builder setEvent(Event event);

        abstract Builder setTransformer(Transformer transformer);

        public abstract Builder setTransportContext(TransportContext transportContext);

        public abstract Builder setTransportName(String str);
    }

    SendRequest() {
    }

    public static Builder builder() {
        return new AutoValue_SendRequest.Builder();
    }

    public abstract Encoding getEncoding();

    abstract Event getEvent();

    public byte[] getPayload() {
        return (byte[]) getTransformer().apply(getEvent().getPayload());
    }

    abstract Transformer getTransformer();

    public abstract TransportContext getTransportContext();

    public abstract String getTransportName();
}
