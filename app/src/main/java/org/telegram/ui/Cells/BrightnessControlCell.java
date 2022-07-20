package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SeekBarView;
/* loaded from: classes3.dex */
public class BrightnessControlCell extends FrameLayout {
    private ImageView leftImageView;
    private ImageView rightImageView;
    private SeekBarView seekBarView;

    protected void didChangedValue(float f) {
    }

    public BrightnessControlCell(Context context) {
        super(context);
        ImageView imageView = new ImageView(context);
        this.leftImageView = imageView;
        imageView.setImageResource(2131165660);
        addView(this.leftImageView, LayoutHelper.createFrame(24, 24.0f, 51, 17.0f, 12.0f, 0.0f, 0.0f));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, context, true, null);
        this.seekBarView = anonymousClass1;
        anonymousClass1.setReportChanges(true);
        this.seekBarView.setDelegate(new AnonymousClass2());
        this.seekBarView.setImportantForAccessibility(2);
        addView(this.seekBarView, LayoutHelper.createFrame(-1, 38.0f, 51, 54.0f, 5.0f, 54.0f, 0.0f));
        ImageView imageView2 = new ImageView(context);
        this.rightImageView = imageView2;
        imageView2.setImageResource(2131165659);
        addView(this.rightImageView, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 12.0f, 17.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Cells.BrightnessControlCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends SeekBarView {
        AnonymousClass1(BrightnessControlCell brightnessControlCell, Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, z, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.SeekBarView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Cells.BrightnessControlCell$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements SeekBarView.SeekBarViewDelegate {
        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public CharSequence getContentDescription() {
            return " ";
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public /* synthetic */ int getStepsCount() {
            return SeekBarView.SeekBarViewDelegate.CC.$default$getStepsCount(this);
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarPressed(boolean z) {
        }

        AnonymousClass2() {
            BrightnessControlCell.this = r1;
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarDrag(boolean z, float f) {
            BrightnessControlCell.this.didChangedValue(f);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.leftImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.MULTIPLY));
        this.rightImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.MULTIPLY));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
    }

    public void setProgress(float f) {
        this.seekBarView.setProgress(f);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.seekBarView.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        return super.performAccessibilityAction(i, bundle) || this.seekBarView.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, i, bundle);
    }
}
