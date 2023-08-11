package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Paint.PaintTypeface;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaintTypeface$$ExternalSyntheticLambda4 implements PaintTypeface.LazyTypeface.LazyTypefaceLoader {
    public static final /* synthetic */ PaintTypeface$$ExternalSyntheticLambda4 INSTANCE = new PaintTypeface$$ExternalSyntheticLambda4();

    private /* synthetic */ PaintTypeface$$ExternalSyntheticLambda4() {
    }

    @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
    public final Typeface load() {
        Typeface typeface;
        typeface = AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf");
        return typeface;
    }
}
