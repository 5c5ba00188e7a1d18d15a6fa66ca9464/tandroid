package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes.dex */
public final /* synthetic */ class SharedConfig$$ExternalSyntheticLambda3 implements Comparator {
    public static final /* synthetic */ SharedConfig$$ExternalSyntheticLambda3 INSTANCE = new SharedConfig$$ExternalSyntheticLambda3();

    private /* synthetic */ SharedConfig$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$saveProxyList$1;
        lambda$saveProxyList$1 = SharedConfig.lambda$saveProxyList$1((SharedConfig.ProxyInfo) obj, (SharedConfig.ProxyInfo) obj2);
        return lambda$saveProxyList$1;
    }
}
