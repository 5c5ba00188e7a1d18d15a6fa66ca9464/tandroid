package org.telegram.ui.Cells;

import org.telegram.ui.Components.PhotoEditorSeekBar;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoEditToolCell$$ExternalSyntheticLambda0 implements PhotoEditorSeekBar.PhotoEditorSeekBarDelegate {
    public final /* synthetic */ PhotoEditToolCell f$0;
    public final /* synthetic */ PhotoEditorSeekBar.PhotoEditorSeekBarDelegate f$1;

    public /* synthetic */ PhotoEditToolCell$$ExternalSyntheticLambda0(PhotoEditToolCell photoEditToolCell, PhotoEditorSeekBar.PhotoEditorSeekBarDelegate photoEditorSeekBarDelegate) {
        this.f$0 = photoEditToolCell;
        this.f$1 = photoEditorSeekBarDelegate;
    }

    @Override // org.telegram.ui.Components.PhotoEditorSeekBar.PhotoEditorSeekBarDelegate
    public final void onProgressChanged(int i, int i2) {
        this.f$0.lambda$setSeekBarDelegate$0(this.f$1, i, i2);
    }
}
