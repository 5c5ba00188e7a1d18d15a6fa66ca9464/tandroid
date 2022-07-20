package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AnimatedEmojiDrawable.EmojiDocumentFetcher f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ AnimatedEmojiDrawable$EmojiDocumentFetcher$$ExternalSyntheticLambda2(AnimatedEmojiDrawable.EmojiDocumentFetcher emojiDocumentFetcher, ArrayList arrayList) {
        this.f$0 = emojiDocumentFetcher;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putToStorage$5(this.f$1);
    }
}
