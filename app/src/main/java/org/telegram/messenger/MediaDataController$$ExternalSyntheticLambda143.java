package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda143 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda143 INSTANCE = new MediaDataController$$ExternalSyntheticLambda143();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda143() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increasePeerRaiting$129;
        lambda$increasePeerRaiting$129 = MediaDataController.lambda$increasePeerRaiting$129((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increasePeerRaiting$129;
    }
}
