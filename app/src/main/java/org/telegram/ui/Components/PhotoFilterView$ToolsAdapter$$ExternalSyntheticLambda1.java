package org.telegram.ui.Components;

import org.telegram.ui.Components.PhotoEditorSeekBar;
import org.telegram.ui.Components.PhotoFilterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoFilterView$ToolsAdapter$$ExternalSyntheticLambda1 implements PhotoEditorSeekBar.PhotoEditorSeekBarDelegate {
    public final /* synthetic */ PhotoFilterView.ToolsAdapter f$0;

    public /* synthetic */ PhotoFilterView$ToolsAdapter$$ExternalSyntheticLambda1(PhotoFilterView.ToolsAdapter toolsAdapter) {
        this.f$0 = toolsAdapter;
    }

    @Override // org.telegram.ui.Components.PhotoEditorSeekBar.PhotoEditorSeekBarDelegate
    public final void onProgressChanged(int i, int i2) {
        this.f$0.lambda$onCreateViewHolder$0(i, i2);
    }
}
