package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_savedReactionTag;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda273 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda273 INSTANCE = new MessagesController$$ExternalSyntheticLambda273();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda273() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateSavedReactionTags$410;
        lambda$updateSavedReactionTags$410 = MessagesController.lambda$updateSavedReactionTags$410((TLRPC$TL_savedReactionTag) obj, (TLRPC$TL_savedReactionTag) obj2);
        return lambda$updateSavedReactionTags$410;
    }
}
