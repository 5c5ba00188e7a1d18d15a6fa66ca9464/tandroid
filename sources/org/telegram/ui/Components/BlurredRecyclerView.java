package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;

/* loaded from: classes3.dex */
public class BlurredRecyclerView extends RecyclerListView {
    public int additionalClipBottom;
    public int blurTopPadding;
    public int bottomPadding;
    boolean globalIgnoreLayout;
    public int topPadding;

    public BlurredRecyclerView(Context context) {
        super(context);
    }

    private void updateTopPadding() {
        if (getLayoutParams() == null) {
            return;
        }
        if (!SharedConfig.chatBlurEnabled()) {
            this.blurTopPadding = 0;
            ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = 0;
        } else {
            this.blurTopPadding = AndroidUtilities.dp(203.0f);
            ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = -this.blurTopPadding;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        int i = this.blurTopPadding;
        if (i != 0) {
            canvas.clipRect(0, i, getMeasuredWidth(), getMeasuredHeight() + this.additionalClipBottom);
        }
        super.dispatchDraw(canvas);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j) {
        if (view.getY() + view.getMeasuredHeight() < this.blurTopPadding) {
            return true;
        }
        return super.drawChild(canvas, view, j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateTopPadding();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i, int i2) {
        this.globalIgnoreLayout = true;
        updateTopPadding();
        super.setPadding(getPaddingLeft(), this.topPadding + this.blurTopPadding, getPaddingRight(), getPaddingBottom());
        this.globalIgnoreLayout = false;
        super.onMeasure(i, i2);
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.globalIgnoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.topPadding = i2;
        this.bottomPadding = i4;
        super.setPadding(i, i2 + this.blurTopPadding, i3, i4);
    }
}
