package org.telegram.ui.Components;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

/* loaded from: classes3.dex */
public abstract class SlideView extends LinearLayout {
    public SlideView(Context context) {
        super(context);
    }

    public String getHeaderName() {
        return "";
    }

    public boolean hasCustomKeyboard() {
        return false;
    }

    public boolean needBackButton() {
        return false;
    }

    public boolean onBackPressed(boolean z) {
        return true;
    }

    public void onCancelPressed() {
    }

    public void onDestroyActivity() {
    }

    public void onHide() {
    }

    public void onNextPressed(String str) {
    }

    public void onResume() {
    }

    public void onShow() {
    }

    public void restoreStateParams(Bundle bundle) {
    }

    public void saveStateParams(Bundle bundle) {
    }

    public void setParams(Bundle bundle, boolean z) {
    }

    public void updateColors() {
    }
}
