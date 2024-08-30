package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes4.dex */
public class HeaderCell extends FrameLayout {
    private boolean animated;
    private AnimatedTextView animatedTextView;
    protected int bottomMargin;
    private int height;
    public int id;
    protected int padding;
    private final Theme.ResourcesProvider resourcesProvider;
    private TextView textView;
    private SimpleTextView textView2;

    public HeaderCell(Context context) {
        this(context, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, false, null);
    }

    public HeaderCell(Context context, int i) {
        this(context, Theme.key_windowBackgroundWhiteBlueHeader, i, 15, false, null);
    }

    public HeaderCell(Context context, int i, int i2, int i3, int i4, boolean z, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, i3, i4, z, false, resourcesProvider);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x007b, code lost:
        if (r23 != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00d8, code lost:
        if (r23 != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00da, code lost:
        r14 = r3;
        r11 = r5;
        r12 = r6;
        r15 = r10;
        r16 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00e2, code lost:
        r14 = r3;
        r12 = r6;
        r15 = r10;
        r16 = r22;
        r11 = r5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public HeaderCell(Context context, int i, int i2, int i3, int i4, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        View view;
        int i5;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        int i6;
        float f6;
        float f7;
        this.height = 40;
        this.resourcesProvider = resourcesProvider;
        this.padding = i2;
        this.bottomMargin = i4;
        this.animated = z2;
        if (z2) {
            AnimatedTextView animatedTextView = new AnimatedTextView(getContext());
            this.animatedTextView = animatedTextView;
            animatedTextView.setTextSize(AndroidUtilities.dp(15.0f));
            this.animatedTextView.setTypeface(AndroidUtilities.bold());
            this.animatedTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.animatedTextView.setTextColor(getThemedColor(i));
            this.animatedTextView.setTag(Integer.valueOf(i));
            this.animatedTextView.getDrawable().setHacks(true, true, false);
            view = this.animatedTextView;
            f3 = this.height - i3;
            i5 = (LocaleController.isRTL ? 5 : 3) | 48;
            f = i2;
            f2 = i3;
        } else {
            TextView textView = new TextView(getContext());
            this.textView = textView;
            textView.setTextSize(1, 15.0f);
            this.textView.setTypeface(AndroidUtilities.bold());
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.textView.setMinHeight(AndroidUtilities.dp(this.height - i3));
            this.textView.setTextColor(getThemedColor(i));
            this.textView.setTag(Integer.valueOf(i));
            view = this.textView;
            i5 = (LocaleController.isRTL ? 5 : 3) | 48;
            f = i2;
            f2 = i3;
            f3 = -1.0f;
        }
        addView(view, LayoutHelper.createFrame(-1, f5, i6, f6, f4, f6, f7));
        if (z) {
            SimpleTextView simpleTextView = new SimpleTextView(getContext());
            this.textView2 = simpleTextView;
            simpleTextView.setTextSize(13);
            this.textView2.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
            float f8 = i2;
            addView(this.textView2, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 3 : 5) | 48, f8, 21.0f, f8, i4));
        }
        ViewCompat.setAccessibilityHeading(this, true);
    }

    public HeaderCell(Context context, int i, int i2, int i3, boolean z) {
        this(context, i, i2, i3, z, null);
    }

    public HeaderCell(Context context, int i, int i2, int i3, boolean z, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, i3, 0, z, resourcesProvider);
    }

    public HeaderCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        this(context, Theme.key_windowBackgroundWhiteBlueHeader, i, 15, false, resourcesProvider);
    }

    public HeaderCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        this(context, Theme.key_windowBackgroundWhiteBlueHeader, 21, 15, false, resourcesProvider);
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public TextView getTextView() {
        return this.textView;
    }

    public SimpleTextView getTextView2() {
        return this.textView2;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (Build.VERSION.SDK_INT >= 28) {
            accessibilityNodeInfo.setHeading(true);
        } else {
            AccessibilityNodeInfo.CollectionItemInfo collectionItemInfo = accessibilityNodeInfo.getCollectionItemInfo();
            if (collectionItemInfo != null) {
                accessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(collectionItemInfo.getRowIndex(), collectionItemInfo.getRowSpan(), collectionItemInfo.getColumnIndex(), collectionItemInfo.getColumnSpan(), true));
            }
        }
        accessibilityNodeInfo.setEnabled(true);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    public void setBottomMargin(int i) {
        float f = i;
        ((FrameLayout.LayoutParams) this.textView.getLayoutParams()).bottomMargin = AndroidUtilities.dp(f);
        SimpleTextView simpleTextView = this.textView2;
        if (simpleTextView != null) {
            ((FrameLayout.LayoutParams) simpleTextView.getLayoutParams()).bottomMargin = AndroidUtilities.dp(f);
        }
    }

    public void setEnabled(boolean z, ArrayList arrayList) {
        if (arrayList != null) {
            arrayList.add(ObjectAnimator.ofFloat(this.textView, View.ALPHA, z ? 1.0f : 0.5f));
        } else {
            this.textView.setAlpha(z ? 1.0f : 0.5f);
        }
    }

    public void setHeight(int i) {
        this.height = i;
        int dp = AndroidUtilities.dp(i) - ((FrameLayout.LayoutParams) this.textView.getLayoutParams()).topMargin;
        if (this.textView.getMinHeight() != dp) {
            this.textView.setMinHeight(dp);
            requestLayout();
        }
    }

    public void setText(CharSequence charSequence) {
        setText(charSequence, false);
    }

    public void setText(CharSequence charSequence, boolean z) {
        if (this.animated) {
            this.animatedTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.animatedTextView.setText(charSequence, z);
            return;
        }
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.textView.setText(charSequence);
    }

    public void setText2(CharSequence charSequence) {
        SimpleTextView simpleTextView = this.textView2;
        if (simpleTextView == null) {
            return;
        }
        simpleTextView.setText(charSequence);
    }

    public void setTextColor(int i) {
        this.textView.setTextColor(i);
    }

    public void setTextSize(float f) {
        if (this.animated) {
            this.animatedTextView.setTextSize(AndroidUtilities.dp(f));
        } else {
            this.textView.setTextSize(1, f);
        }
    }

    public void setTopMargin(int i) {
        ((FrameLayout.LayoutParams) this.textView.getLayoutParams()).topMargin = AndroidUtilities.dp(i);
        setHeight(this.height);
    }
}
