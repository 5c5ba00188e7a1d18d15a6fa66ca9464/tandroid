package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.SeekBar;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$SeekBarWaveformView$$ExternalSyntheticLambda0 implements SeekBar.SeekBarDelegate {
    public final /* synthetic */ ChatActivityEnterView.SeekBarWaveformView f$0;

    public /* synthetic */ ChatActivityEnterView$SeekBarWaveformView$$ExternalSyntheticLambda0(ChatActivityEnterView.SeekBarWaveformView seekBarWaveformView) {
        this.f$0 = seekBarWaveformView;
    }

    @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
    public /* synthetic */ void onSeekBarContinuousDrag(float f) {
        SeekBar.SeekBarDelegate.CC.$default$onSeekBarContinuousDrag(this, f);
    }

    @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
    public final void onSeekBarDrag(float f) {
        this.f$0.lambda$new$0(f);
    }
}
