package org.telegram.ui.Components;

import android.view.View;
/* loaded from: classes3.dex */
public interface IPhotoPaintView {

    /* loaded from: classes3.dex */
    public abstract /* synthetic */ class -CC {
        public static View $default$getView(IPhotoPaintView iPhotoPaintView) {
            if (iPhotoPaintView instanceof View) {
                return (View) iPhotoPaintView;
            }
            throw new IllegalArgumentException("You should override getView() if you're not inheriting from it.");
        }

        public static void $default$setOffsetTranslationX(IPhotoPaintView iPhotoPaintView, float f) {
        }
    }
}
