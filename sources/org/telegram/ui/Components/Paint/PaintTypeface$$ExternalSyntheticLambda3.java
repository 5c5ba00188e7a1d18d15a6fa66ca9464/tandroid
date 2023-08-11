package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Paint.PaintTypeface;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaintTypeface$$ExternalSyntheticLambda3 implements PaintTypeface.LazyTypeface.LazyTypefaceLoader {
    public static final /* synthetic */ PaintTypeface$$ExternalSyntheticLambda3 INSTANCE = new PaintTypeface$$ExternalSyntheticLambda3();

    private /* synthetic */ PaintTypeface$$ExternalSyntheticLambda3() {
    }

    @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
    public final Typeface load() {
        Typeface typeface;
        typeface = AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_MERRIWEATHER_BOLD);
        return typeface;
    }
}
