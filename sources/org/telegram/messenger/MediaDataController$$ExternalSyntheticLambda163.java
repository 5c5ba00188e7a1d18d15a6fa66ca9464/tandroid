package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda163 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda163 INSTANCE = new MediaDataController$$ExternalSyntheticLambda163();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda163() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increasePeerRaiting$143;
        lambda$increasePeerRaiting$143 = MediaDataController.lambda$increasePeerRaiting$143((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increasePeerRaiting$143;
    }
}
