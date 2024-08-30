package com.google.android.datatransport;
/* loaded from: classes.dex */
public interface TransportFactory {
    Transport getTransport(String str, Class cls, Encoding encoding, Transformer transformer);
}
