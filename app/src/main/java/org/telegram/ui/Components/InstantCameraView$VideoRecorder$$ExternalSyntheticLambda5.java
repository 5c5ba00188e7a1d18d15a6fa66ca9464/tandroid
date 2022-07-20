package org.telegram.ui.Components;

import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.InstantCameraView;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$VideoRecorder$$ExternalSyntheticLambda5 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ InstantCameraView.VideoRecorder f$0;

    public /* synthetic */ InstantCameraView$VideoRecorder$$ExternalSyntheticLambda5(InstantCameraView.VideoRecorder videoRecorder) {
        this.f$0 = videoRecorder;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$handleStopRecording$2(z, i);
    }
}
