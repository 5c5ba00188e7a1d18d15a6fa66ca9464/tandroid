package com.google.android.datatransport.runtime;

import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.TransportFactory;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TransportFactoryImpl implements TransportFactory {
    private final Set supportedPayloadEncodings;
    private final TransportContext transportContext;
    private final TransportInternal transportInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransportFactoryImpl(Set set, TransportContext transportContext, TransportInternal transportInternal) {
        this.supportedPayloadEncodings = set;
        this.transportContext = transportContext;
        this.transportInternal = transportInternal;
    }

    @Override // com.google.android.datatransport.TransportFactory
    public Transport getTransport(String str, Class cls, Encoding encoding, Transformer transformer) {
        if (this.supportedPayloadEncodings.contains(encoding)) {
            return new TransportImpl(this.transportContext, str, encoding, transformer, this.transportInternal);
        }
        throw new IllegalArgumentException(String.format("%s is not supported byt this factory. Supported encodings are: %s.", encoding, this.supportedPayloadEncodings));
    }
}
