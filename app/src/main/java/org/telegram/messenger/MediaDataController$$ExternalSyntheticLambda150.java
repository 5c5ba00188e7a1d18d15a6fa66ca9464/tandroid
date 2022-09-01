package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda150 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda150 INSTANCE = new MediaDataController$$ExternalSyntheticLambda150();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda150() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increaseInlineRaiting$130;
        lambda$increaseInlineRaiting$130 = MediaDataController.lambda$increaseInlineRaiting$130((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increaseInlineRaiting$130;
    }
}
