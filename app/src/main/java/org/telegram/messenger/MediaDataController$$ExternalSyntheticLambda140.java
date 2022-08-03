package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$MessageEntity;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda140 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda140 INSTANCE = new MediaDataController$$ExternalSyntheticLambda140();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda140() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getTextStyleRuns$153;
        lambda$getTextStyleRuns$153 = MediaDataController.lambda$getTextStyleRuns$153((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
        return lambda$getTextStyleRuns$153;
    }
}
