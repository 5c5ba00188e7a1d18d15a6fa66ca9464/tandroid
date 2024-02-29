package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes3.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda172 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda172 INSTANCE = new MediaDataController$$ExternalSyntheticLambda172();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda172() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increasePeerRaiting$147;
        lambda$increasePeerRaiting$147 = MediaDataController.lambda$increasePeerRaiting$147((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increasePeerRaiting$147;
    }
}
