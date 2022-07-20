package org.telegram.messenger;

import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.messenger.MediaDataController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda123 implements Comparator {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda123(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getEmojiSuggestions$172;
        lambda$getEmojiSuggestions$172 = MediaDataController.lambda$getEmojiSuggestions$172(this.f$0, (MediaDataController.KeywordResult) obj, (MediaDataController.KeywordResult) obj2);
        return lambda$getEmojiSuggestions$172;
    }
}
