package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedConfig$$ExternalSyntheticLambda6 implements Comparator {
    public static final /* synthetic */ SharedConfig$$ExternalSyntheticLambda6 INSTANCE = new SharedConfig$$ExternalSyntheticLambda6();

    private /* synthetic */ SharedConfig$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$saveProxyList$4;
        lambda$saveProxyList$4 = SharedConfig.lambda$saveProxyList$4((SharedConfig.ProxyInfo) obj, (SharedConfig.ProxyInfo) obj2);
        return lambda$saveProxyList$4;
    }
}
