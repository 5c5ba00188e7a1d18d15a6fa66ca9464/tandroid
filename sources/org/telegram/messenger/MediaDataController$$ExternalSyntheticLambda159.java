package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$MessageEntity;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda159 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda159 INSTANCE = new MediaDataController$$ExternalSyntheticLambda159();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda159() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getTextStyleRuns$167;
        lambda$getTextStyleRuns$167 = MediaDataController.lambda$getTextStyleRuns$167((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
        return lambda$getTextStyleRuns$167;
    }
}
