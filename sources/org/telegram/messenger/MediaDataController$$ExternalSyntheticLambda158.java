package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$MessageEntity;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda158 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda158 INSTANCE = new MediaDataController$$ExternalSyntheticLambda158();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda158() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getTextStyleRuns$168;
        lambda$getTextStyleRuns$168 = MediaDataController.lambda$getTextStyleRuns$168((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
        return lambda$getTextStyleRuns$168;
    }
}
