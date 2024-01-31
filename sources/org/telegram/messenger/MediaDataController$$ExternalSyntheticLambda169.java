package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$MessageEntity;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda169 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda169 INSTANCE = new MediaDataController$$ExternalSyntheticLambda169();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda169() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getTextStyleRuns$172;
        lambda$getTextStyleRuns$172 = MediaDataController.lambda$getTextStyleRuns$172((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
        return lambda$getTextStyleRuns$172;
    }
}
