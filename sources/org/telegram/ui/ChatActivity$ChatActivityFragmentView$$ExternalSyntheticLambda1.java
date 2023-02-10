package org.telegram.ui;

import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ChatActivityFragmentView$$ExternalSyntheticLambda1 implements Runnable {
    public static final /* synthetic */ ChatActivity$ChatActivityFragmentView$$ExternalSyntheticLambda1 INSTANCE = new ChatActivity$ChatActivityFragmentView$$ExternalSyntheticLambda1();

    private /* synthetic */ ChatActivity$ChatActivityFragmentView$$ExternalSyntheticLambda1() {
    }

    @Override // java.lang.Runnable
    public final void run() {
        ReactionsEffectOverlay.removeCurrent(true);
    }
}
