package org.telegram.ui.Components;

import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Components.spoilers.SpoilersClickDetector;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditTextEffects$$ExternalSyntheticLambda5 implements SpoilersClickDetector.OnSpoilerClickedListener {
    public final /* synthetic */ EditTextEffects f$0;

    public /* synthetic */ EditTextEffects$$ExternalSyntheticLambda5(EditTextEffects editTextEffects) {
        this.f$0 = editTextEffects;
    }

    @Override // org.telegram.ui.Components.spoilers.SpoilersClickDetector.OnSpoilerClickedListener
    public final void onSpoilerClicked(SpoilerEffect spoilerEffect, float f, float f2) {
        this.f$0.onSpoilerClicked(spoilerEffect, f, f2);
    }
}
