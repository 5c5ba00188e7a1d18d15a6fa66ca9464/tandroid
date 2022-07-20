package org.telegram.ui;

import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.RequestTimeDelegate;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProxyListActivity$$ExternalSyntheticLambda2 implements RequestTimeDelegate {
    public final /* synthetic */ SharedConfig.ProxyInfo f$0;

    public /* synthetic */ ProxyListActivity$$ExternalSyntheticLambda2(SharedConfig.ProxyInfo proxyInfo) {
        this.f$0 = proxyInfo;
    }

    @Override // org.telegram.tgnet.RequestTimeDelegate
    public final void run(long j) {
        ProxyListActivity.lambda$checkProxyList$4(this.f$0, j);
    }
}
