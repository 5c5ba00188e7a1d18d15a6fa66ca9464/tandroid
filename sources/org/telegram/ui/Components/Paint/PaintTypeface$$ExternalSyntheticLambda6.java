package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import org.telegram.ui.Components.Paint.PaintTypeface;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaintTypeface$$ExternalSyntheticLambda6 implements PaintTypeface.LazyTypeface.LazyTypefaceLoader {
    public static final /* synthetic */ PaintTypeface$$ExternalSyntheticLambda6 INSTANCE = new PaintTypeface$$ExternalSyntheticLambda6();

    private /* synthetic */ PaintTypeface$$ExternalSyntheticLambda6() {
    }

    @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
    public final Typeface load() {
        Typeface create;
        create = Typeface.create("serif", 1);
        return create;
    }
}
