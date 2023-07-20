package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Paint.PaintTypeface;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaintTypeface$$ExternalSyntheticLambda8 implements PaintTypeface.LazyTypeface.LazyTypefaceLoader {
    public static final /* synthetic */ PaintTypeface$$ExternalSyntheticLambda8 INSTANCE = new PaintTypeface$$ExternalSyntheticLambda8();

    private /* synthetic */ PaintTypeface$$ExternalSyntheticLambda8() {
    }

    @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
    public final Typeface load() {
        Typeface typeface;
        typeface = AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM);
        return typeface;
    }
}
