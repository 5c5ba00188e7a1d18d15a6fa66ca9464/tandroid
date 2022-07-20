package com.google.android.datatransport.cct;

import com.google.android.datatransport.cct.CctTransportBackend;
import com.google.android.datatransport.runtime.retries.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class CctTransportBackend$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ CctTransportBackend f$0;

    public /* synthetic */ CctTransportBackend$$ExternalSyntheticLambda0(CctTransportBackend cctTransportBackend) {
        this.f$0 = cctTransportBackend;
    }

    @Override // com.google.android.datatransport.runtime.retries.Function
    public final Object apply(Object obj) {
        CctTransportBackend.HttpResponse doSend;
        doSend = this.f$0.doSend((CctTransportBackend.HttpRequest) obj);
        return doSend;
    }
}
