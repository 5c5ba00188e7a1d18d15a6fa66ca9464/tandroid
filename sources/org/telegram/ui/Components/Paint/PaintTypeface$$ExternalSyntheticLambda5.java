package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Paint.PaintTypeface;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaintTypeface$$ExternalSyntheticLambda5 implements PaintTypeface.LazyTypeface.LazyTypefaceLoader {
    public static final /* synthetic */ PaintTypeface$$ExternalSyntheticLambda5 INSTANCE = new PaintTypeface$$ExternalSyntheticLambda5();

    private /* synthetic */ PaintTypeface$$ExternalSyntheticLambda5() {
    }

    @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
    public final Typeface load() {
        Typeface typeface;
        typeface = AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM_ITALIC);
        return typeface;
    }
}
