package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda149 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda149 INSTANCE = new MediaDataController$$ExternalSyntheticLambda149();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda149() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increasePeerRaiting$133;
        lambda$increasePeerRaiting$133 = MediaDataController.lambda$increasePeerRaiting$133((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increasePeerRaiting$133;
    }
}
