package org.telegram.ui;

import org.telegram.ui.ArticleViewer;
import org.telegram.ui.Components.SeekBar;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$BlockAudioCell$$ExternalSyntheticLambda0 implements SeekBar.SeekBarDelegate {
    public final /* synthetic */ ArticleViewer.BlockAudioCell f$0;

    public /* synthetic */ ArticleViewer$BlockAudioCell$$ExternalSyntheticLambda0(ArticleViewer.BlockAudioCell blockAudioCell) {
        this.f$0 = blockAudioCell;
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
