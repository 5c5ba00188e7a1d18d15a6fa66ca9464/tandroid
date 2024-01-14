package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_savedReactionTag;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda271 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda271 INSTANCE = new MessagesController$$ExternalSyntheticLambda271();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda271() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateSavedReactionTags$410;
        lambda$updateSavedReactionTags$410 = MessagesController.lambda$updateSavedReactionTags$410((TLRPC$TL_savedReactionTag) obj, (TLRPC$TL_savedReactionTag) obj2);
        return lambda$updateSavedReactionTags$410;
    }
}
