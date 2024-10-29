package org.telegram.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilePathDatabase;
import org.telegram.messenger.FilesMigrationService;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda11;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.CachedMediaLayout;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CacheChart;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.HideViewAfterAnimation;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.NestedSizeNotifierLayout;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.StorageDiagramView;
import org.telegram.ui.Components.StorageUsageView;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.DilogCacheBottomSheet;
import org.telegram.ui.KeepMediaPopupView;
import org.telegram.ui.Storage.CacheModel;

/* loaded from: classes4.dex */
public class CacheControlActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private static int LISTDIR_DOCTYPE2_EMOJI = 3;
    private static int LISTDIR_DOCTYPE2_OTHER = 5;
    private static int LISTDIR_DOCTYPE2_TEMP = 4;
    private static int LISTDIR_DOCTYPE_MUSIC = 2;
    private static int LISTDIR_DOCTYPE_OTHER_THAN_MUSIC = 1;
    public static volatile boolean canceled = false;
    private static Long lastDeviceTotalFreeSize;
    private static Long lastDeviceTotalSize;
    private static Long lastTotalSizeCalculated;
    private static long lastTotalSizeCalculatedTime;
    private ValueAnimator actionBarAnimator;
    private boolean actionBarShown;
    private float actionBarShownT;
    private ActionBarMenu actionMode;
    private TextView actionModeClearButton;
    private AnimatedTextView actionModeSubtitle;
    private AnimatedTextView actionModeTitle;
    private View actionTextView;
    private BottomSheet bottomSheet;
    private View bottomSheetView;
    private CacheChart cacheChart;
    private CacheChartHeader cacheChartHeader;
    CacheModel cacheModel;
    private UndoView cacheRemovedTooltip;
    private CachedMediaLayout cachedMediaLayout;
    private boolean changeStatusBar;
    private ClearCacheButtonInternal clearCacheButton;
    private ActionBarMenuSubItem clearDatabaseItem;
    long fragmentCreateTime;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingDialogs;
    private NestedSizeNotifierLayout nestedSizeNotifierLayout;
    private int[] percents;
    AlertDialog progressDialog;
    private ActionBarMenuSubItem resetDatabaseItem;
    private float[] tempSizes;
    private boolean updateDatabaseSize;
    private boolean[] selected = {true, true, true, true, true, true, true, true, true, true, true};
    private long databaseSize = -1;
    private long cacheSize = -1;
    private long cacheEmojiSize = -1;
    private long cacheTempSize = -1;
    private long documentsSize = -1;
    private long audioSize = -1;
    private long storiesSize = -1;
    private long musicSize = -1;
    private long photoSize = -1;
    private long videoSize = -1;
    private long logsSize = -1;
    private long stickersCacheSize = -1;
    private long totalSize = -1;
    private long totalDeviceSize = -1;
    private long totalDeviceFreeSize = -1;
    private long migrateOldFolderRow = -1;
    private boolean calculating = true;
    private boolean collapsed = true;
    private int sectionsStartRow = -1;
    private int sectionsEndRow = -1;
    private ArrayList oldItems = new ArrayList();
    private ArrayList itemInners = new ArrayList();
    private float actionBarShadowAlpha = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class CacheChartHeader extends FrameLayout {
        View bottomImage;
        boolean firstSet;
        Paint loadingBackgroundPaint;
        LoadingDrawable loadingDrawable;
        AnimatedFloat loadingFloat;
        Float percent;
        AnimatedFloat percentAnimated;
        Paint percentPaint;
        RectF progressRect;
        private float[] radii;
        private Path roundPath;
        TextView[] subtitle;
        AnimatedTextView title;
        Float usedPercent;
        AnimatedFloat usedPercentAnimated;
        Paint usedPercentPaint;

        public CacheChartHeader(Context context) {
            super(context);
            TextView textView;
            this.subtitle = new TextView[3];
            this.progressRect = new RectF();
            this.loadingDrawable = new LoadingDrawable();
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.percentAnimated = new AnimatedFloat(this, 450L, cubicBezierInterpolator);
            this.usedPercentAnimated = new AnimatedFloat(this, 450L, cubicBezierInterpolator);
            this.loadingFloat = new AnimatedFloat(this, 450L, cubicBezierInterpolator);
            this.loadingBackgroundPaint = new Paint(1);
            this.percentPaint = new Paint(1);
            this.usedPercentPaint = new Paint(1);
            this.firstSet = true;
            AnimatedTextView animatedTextView = new AnimatedTextView(context);
            this.title = animatedTextView;
            animatedTextView.setAnimationProperties(0.35f, 0L, 350L, cubicBezierInterpolator);
            this.title.setTypeface(AndroidUtilities.bold());
            this.title.setTextSize(AndroidUtilities.dp(20.0f));
            this.title.setText(LocaleController.getString(R.string.StorageUsage));
            this.title.setGravity(17);
            this.title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            addView(this.title, LayoutHelper.createFrame(-2, 26, 49));
            int i = 0;
            while (i < 3) {
                this.subtitle[i] = new TextView(context);
                this.subtitle[i].setTextSize(1, 13.0f);
                this.subtitle[i].setGravity(17);
                this.subtitle[i].setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                if (i == 0) {
                    this.subtitle[i].setText(LocaleController.getString(R.string.StorageUsageCalculating));
                } else {
                    if (i == 1) {
                        this.subtitle[i].setAlpha(0.0f);
                        this.subtitle[i].setText(LocaleController.getString(R.string.StorageUsageTelegram));
                        textView = this.subtitle[i];
                    } else if (i == 2) {
                        this.subtitle[i].setText(LocaleController.getString(R.string.StorageCleared2));
                        this.subtitle[i].setAlpha(0.0f);
                        textView = this.subtitle[i];
                    }
                    textView.setVisibility(4);
                }
                this.subtitle[i].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4));
                addView(this.subtitle[i], LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, i == 2 ? 12.0f : -6.0f, 0.0f, 0.0f));
                i++;
            }
            this.bottomImage = new View(context) { // from class: org.telegram.ui.CacheControlActivity.CacheChartHeader.1
                @Override // android.view.View
                protected void onMeasure(int i2, int i3) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) + getPaddingLeft() + getPaddingRight(), 1073741824), i3);
                }
            };
            Drawable mutate = getContext().getResources().getDrawable(R.drawable.popup_fixed_alert2).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
            this.bottomImage.setBackground(mutate);
            FrameLayout.LayoutParams createFrame = LayoutHelper.createFrame(-1, 24, 87);
            ((ViewGroup.MarginLayoutParams) createFrame).leftMargin = -this.bottomImage.getPaddingLeft();
            ((ViewGroup.MarginLayoutParams) createFrame).bottomMargin = -AndroidUtilities.dp(11.0f);
            ((ViewGroup.MarginLayoutParams) createFrame).rightMargin = -this.bottomImage.getPaddingRight();
            addView(this.bottomImage, createFrame);
            this.loadingDrawable.setColors(Theme.getColor(Theme.key_actionBarActionModeDefaultSelector), Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4), 0.2f));
            this.loadingDrawable.setRadiiDp(4.0f);
            this.loadingDrawable.setCallback(this);
        }

        private void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, Paint paint) {
            Path path = this.roundPath;
            if (path == null) {
                this.roundPath = new Path();
            } else {
                path.rewind();
            }
            if (this.radii == null) {
                this.radii = new float[8];
            }
            float[] fArr = this.radii;
            fArr[7] = f;
            fArr[6] = f;
            fArr[1] = f;
            fArr[0] = f;
            fArr[5] = f2;
            fArr[4] = f2;
            fArr[3] = f2;
            fArr[2] = f2;
            this.roundPath.addRoundRect(rectF, fArr, Path.Direction.CW);
            canvas.drawPath(this.roundPath, paint);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateViewVisible$0(ValueAnimator valueAnimator) {
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateViewVisible$1(ValueAnimator valueAnimator) {
            invalidate();
        }

        private void switchSubtitle(int i) {
            boolean z = System.currentTimeMillis() - CacheControlActivity.this.fragmentCreateTime > 40;
            updateViewVisible(this.subtitle[0], i == 0, z);
            updateViewVisible(this.subtitle[1], i == 1, z);
            updateViewVisible(this.subtitle[2], i == 2, z);
        }

        private void updateViewVisible(View view, boolean z, boolean z2) {
            ViewPropertyAnimator duration;
            ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
            if (view == null) {
                return;
            }
            if (view.getParent() == null) {
                z2 = false;
            }
            view.animate().setListener(null).cancel();
            if (!z2) {
                view.setVisibility(z ? 0 : 4);
                view.setTag(z ? 1 : null);
                view.setAlpha(z ? 1.0f : 0.0f);
                view.setTranslationY(z ? 0.0f : AndroidUtilities.dp(8.0f));
                invalidate();
                return;
            }
            if (z) {
                if (view.getVisibility() != 0) {
                    view.setVisibility(0);
                    view.setAlpha(0.0f);
                    view.setTranslationY(AndroidUtilities.dp(8.0f));
                }
                duration = view.animate().alpha(1.0f).translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(340L);
                animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.CacheControlActivity$CacheChartHeader$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        CacheControlActivity.CacheChartHeader.this.lambda$updateViewVisible$0(valueAnimator);
                    }
                };
            } else {
                duration = view.animate().alpha(0.0f).translationY(AndroidUtilities.dp(8.0f)).setListener(new HideViewAfterAnimation(view)).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(340L);
                animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.CacheControlActivity$CacheChartHeader$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        CacheControlActivity.CacheChartHeader.this.lambda$updateViewVisible$1(valueAnimator);
                    }
                };
            }
            duration.setUpdateListener(animatorUpdateListener).start();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            float alpha = 1.0f - this.subtitle[2].getAlpha();
            float f = this.loadingFloat.set(this.percent == null ? 1.0f : 0.0f);
            AnimatedFloat animatedFloat = this.percentAnimated;
            Float f2 = this.percent;
            float f3 = animatedFloat.set(f2 == null ? 0.0f : f2.floatValue());
            AnimatedFloat animatedFloat2 = this.usedPercentAnimated;
            Float f4 = this.usedPercent;
            float f5 = animatedFloat2.set(f4 == null ? 0.0f : f4.floatValue());
            Paint paint = this.loadingBackgroundPaint;
            int i = Theme.key_actionBarActionModeDefaultSelector;
            paint.setColor(Theme.getColor(i));
            this.loadingBackgroundPaint.setAlpha((int) (r0.getAlpha() * alpha));
            RectF rectF = AndroidUtilities.rectTmp;
            float f6 = 1.0f - f;
            float max = Math.max(this.progressRect.left + (Math.max(AndroidUtilities.dp(4.0f), this.progressRect.width() * f5) * f6), this.progressRect.left + (Math.max(AndroidUtilities.dp(4.0f), this.progressRect.width() * f3) * f6)) + AndroidUtilities.dp(1.0f);
            RectF rectF2 = this.progressRect;
            rectF.set(max, rectF2.top, rectF2.right, rectF2.bottom);
            if (rectF.left < rectF.right && rectF.width() > AndroidUtilities.dp(3.0f)) {
                drawRoundRect(canvas, rectF, AndroidUtilities.dp(AndroidUtilities.lerp(1, 2, f)), AndroidUtilities.dp(2.0f), this.loadingBackgroundPaint);
            }
            this.loadingDrawable.setBounds(this.progressRect);
            this.loadingDrawable.setAlpha((int) (255.0f * alpha * f));
            this.loadingDrawable.draw(canvas);
            Paint paint2 = this.usedPercentPaint;
            int i2 = Theme.key_radioBackgroundChecked;
            paint2.setColor(ColorUtils.blendARGB(Theme.getColor(i2), Theme.getColor(i), 0.75f));
            this.usedPercentPaint.setAlpha((int) (r0.getAlpha() * alpha));
            float max2 = this.progressRect.left + (Math.max(AndroidUtilities.dp(4.0f), this.progressRect.width() * f3) * f6) + AndroidUtilities.dp(1.0f);
            RectF rectF3 = this.progressRect;
            rectF.set(max2, rectF3.top, rectF3.left + (Math.max(AndroidUtilities.dp(4.0f), this.progressRect.width() * f5) * f6), this.progressRect.bottom);
            if (rectF.width() > AndroidUtilities.dp(3.0f)) {
                drawRoundRect(canvas, rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(f5 > 0.97f ? 2.0f : 1.0f), this.usedPercentPaint);
            }
            this.percentPaint.setColor(Theme.getColor(i2));
            this.percentPaint.setAlpha((int) (r0.getAlpha() * alpha));
            RectF rectF4 = this.progressRect;
            float f7 = rectF4.left;
            rectF.set(f7, rectF4.top, (f6 * Math.max(AndroidUtilities.dp(4.0f), this.progressRect.width() * f3)) + f7, this.progressRect.bottom);
            drawRoundRect(canvas, rectF, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(f3 > 0.97f ? 2.0f : 1.0f), this.percentPaint);
            if (f > 0.0f || this.percentAnimated.isInProgress()) {
                invalidate();
            }
            super.dispatchDraw(canvas);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            double dp = AndroidUtilities.dp(174.0f);
            double d = size;
            Double.isNaN(d);
            int min = (int) Math.min(dp, d * 0.8d);
            super.measureChildren(View.MeasureSpec.makeMeasureSpec(size, 1073741824), i2);
            int dp2 = AndroidUtilities.dp(72.0f);
            int i3 = 0;
            int i4 = 0;
            while (true) {
                TextView[] textViewArr = this.subtitle;
                if (i3 >= textViewArr.length) {
                    setMeasuredDimension(size, dp2 + i4);
                    this.progressRect.set((size - min) / 2.0f, r8 - AndroidUtilities.dp(30.0f), (size + min) / 2.0f, r8 - AndroidUtilities.dp(26.0f));
                    return;
                }
                i4 = Math.max(i4, textViewArr[i3].getMeasuredHeight() - (i3 == 2 ? AndroidUtilities.dp(16.0f) : 0));
                i3++;
            }
        }

        public void setData(boolean z, float f, float f2) {
            TextView textView;
            String formatString;
            int i = 1;
            this.title.setText(LocaleController.getString(z ? R.string.StorageUsage : R.string.StorageCleared));
            if (z) {
                if (f < 0.01f) {
                    textView = this.subtitle[1];
                    formatString = LocaleController.formatString("StorageUsageTelegramLess", R.string.StorageUsageTelegramLess, CacheControlActivity.this.formatPercent(f));
                } else {
                    textView = this.subtitle[1];
                    formatString = LocaleController.formatString("StorageUsageTelegram", R.string.StorageUsageTelegram, CacheControlActivity.this.formatPercent(f));
                }
                textView.setText(formatString);
            } else {
                i = 2;
            }
            switchSubtitle(i);
            this.bottomImage.animate().cancel();
            if (this.firstSet) {
                this.bottomImage.setAlpha(z ? 1.0f : 0.0f);
            } else {
                this.bottomImage.animate().alpha(z ? 1.0f : 0.0f).setDuration(365L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
            }
            this.firstSet = false;
            this.percent = Float.valueOf(f);
            this.usedPercent = Float.valueOf(f2);
            invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public static class ClearCacheButton extends FrameLayout {
        FrameLayout button;
        TextView rtlTextView;
        AnimatedTextView.AnimatedTextDrawable textView;
        AnimatedTextView.AnimatedTextDrawable valueTextView;

        public ClearCacheButton(Context context) {
            super(context);
            FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.CacheControlActivity.ClearCacheButton.1
                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    int measuredWidth = (((getMeasuredWidth() - AndroidUtilities.dp(8.0f)) - ((int) ClearCacheButton.this.valueTextView.getCurrentWidth())) + ((int) ClearCacheButton.this.textView.getCurrentWidth())) / 2;
                    if (LocaleController.isRTL) {
                        super.dispatchDraw(canvas);
                        return;
                    }
                    ClearCacheButton.this.textView.setBounds(0, 0, measuredWidth, getHeight());
                    ClearCacheButton.this.textView.draw(canvas);
                    ClearCacheButton.this.valueTextView.setBounds(measuredWidth + AndroidUtilities.dp(8.0f), 0, getWidth(), getHeight());
                    ClearCacheButton.this.valueTextView.draw(canvas);
                }

                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName("android.widget.Button");
                }

                @Override // android.view.ViewGroup
                public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                    super.onInterceptTouchEvent(motionEvent);
                    return true;
                }

                @Override // android.view.View
                protected boolean verifyDrawable(Drawable drawable) {
                    ClearCacheButton clearCacheButton = ClearCacheButton.this;
                    return drawable == clearCacheButton.valueTextView || drawable == clearCacheButton.textView || super.verifyDrawable(drawable);
                }
            };
            this.button = frameLayout;
            int i = Theme.key_featuredStickers_addButton;
            frameLayout.setBackground(Theme.AdaptiveRipple.filledRectByKey(i, 8.0f));
            this.button.setImportantForAccessibility(1);
            if (LocaleController.isRTL) {
                TextView textView = new TextView(context);
                this.rtlTextView = textView;
                textView.setText(LocaleController.getString(R.string.ClearCache));
                this.rtlTextView.setGravity(17);
                this.rtlTextView.setTextSize(1, 14.0f);
                this.rtlTextView.setTypeface(AndroidUtilities.bold());
                this.rtlTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                this.button.addView(this.rtlTextView, LayoutHelper.createFrame(-2, -1, 17));
            }
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.textView = animatedTextDrawable;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            animatedTextDrawable.setAnimationProperties(0.25f, 0L, 300L, cubicBezierInterpolator);
            this.textView.setCallback(this.button);
            this.textView.setTextSize(AndroidUtilities.dp(14.0f));
            this.textView.setText(LocaleController.getString(R.string.ClearCache));
            this.textView.setGravity(5);
            this.textView.setTypeface(AndroidUtilities.bold());
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.textView;
            int i2 = Theme.key_featuredStickers_buttonText;
            animatedTextDrawable2.setTextColor(Theme.getColor(i2));
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable3 = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.valueTextView = animatedTextDrawable3;
            animatedTextDrawable3.setAnimationProperties(0.25f, 0L, 300L, cubicBezierInterpolator);
            this.valueTextView.setCallback(this.button);
            this.valueTextView.setTextSize(AndroidUtilities.dp(14.0f));
            this.valueTextView.setTypeface(AndroidUtilities.bold());
            this.valueTextView.setTextColor(Theme.blendOver(Theme.getColor(i), Theme.multAlpha(Theme.getColor(i2), 0.7f)));
            this.valueTextView.setText("");
            this.button.setContentDescription(TextUtils.concat(this.textView.getText(), "\t", this.valueTextView.getText()));
            setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            addView(this.button, LayoutHelper.createFrame(-1, 48.0f, 119, 16.0f, 16.0f, 16.0f, 16.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }

        public void setDisabled(boolean z) {
            this.button.animate().cancel();
            this.button.animate().alpha(z ? 0.65f : 1.0f).start();
            this.button.setClickable(!z);
        }

        public void setSize(boolean z, long j) {
            this.textView.setText(LocaleController.getString(z ? R.string.ClearCache : R.string.ClearSelectedCache));
            this.valueTextView.setText(j <= 0 ? "" : AndroidUtilities.formatFileSize(j));
            setDisabled(j <= 0);
            this.button.invalidate();
            this.button.setContentDescription(TextUtils.concat(this.textView.getText(), "\t", this.valueTextView.getText()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ClearCacheButtonInternal extends ClearCacheButton {
        public ClearCacheButtonInternal(Context context) {
            super(context);
            ((ViewGroup.MarginLayoutParams) this.button.getLayoutParams()).topMargin = AndroidUtilities.dp(5.0f);
            this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CacheControlActivity.ClearCacheButtonInternal.this.lambda$new$1(view);
                }
            });
        }

        private void doClearCache() {
            final BottomSheet bottomSheet = new BottomSheet(getContext(), false) { // from class: org.telegram.ui.CacheControlActivity.ClearCacheButtonInternal.1
                @Override // org.telegram.ui.ActionBar.BottomSheet
                protected boolean canDismissWithTouchOutside() {
                    return false;
                }
            };
            bottomSheet.fixNavigationBar();
            bottomSheet.setCanDismissWithSwipe(false);
            bottomSheet.setCancelable(false);
            final ClearingCacheView clearingCacheView = new ClearingCacheView(getContext());
            bottomSheet.setCustomView(clearingCacheView);
            final boolean[] zArr = {false};
            final float[] fArr = {0.0f};
            final boolean[] zArr2 = {false};
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.ClearCacheButtonInternal.this.lambda$doClearCache$2(clearingCacheView, fArr, zArr2);
                }
            };
            final long[] jArr = {-1};
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.ClearCacheButtonInternal.this.lambda$doClearCache$3(zArr, jArr, bottomSheet);
                }
            }, 150L);
            CacheControlActivity.this.cleanupFolders(new Utilities.Callback2() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    CacheControlActivity.ClearCacheButtonInternal.lambda$doClearCache$4(fArr, zArr2, runnable, (Float) obj, (Boolean) obj2);
                }
            }, new Runnable() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.ClearCacheButtonInternal.lambda$doClearCache$6(zArr, clearingCacheView, jArr, bottomSheet);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$doClearCache$2(ClearingCacheView clearingCacheView, float[] fArr, boolean[] zArr) {
            clearingCacheView.setProgress(fArr[0]);
            if (zArr[0]) {
                CacheControlActivity.this.updateRows();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$doClearCache$3(boolean[] zArr, long[] jArr, BottomSheet bottomSheet) {
            if (zArr[0]) {
                return;
            }
            jArr[0] = System.currentTimeMillis();
            CacheControlActivity.this.showDialog(bottomSheet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$doClearCache$4(float[] fArr, boolean[] zArr, Runnable runnable, Float f, Boolean bool) {
            fArr[0] = f.floatValue();
            zArr[0] = bool.booleanValue();
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$doClearCache$5(boolean[] zArr, ClearingCacheView clearingCacheView, long[] jArr, BottomSheet bottomSheet) {
            zArr[0] = true;
            clearingCacheView.setProgress(1.0f);
            if (jArr[0] <= 0) {
                bottomSheet.dismiss();
            } else {
                Objects.requireNonNull(bottomSheet);
                AndroidUtilities.runOnUIThread(new BottomSheet$$ExternalSyntheticLambda11(bottomSheet), Math.max(0L, 1000 - (System.currentTimeMillis() - jArr[0])));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$doClearCache$6(final boolean[] zArr, final ClearingCacheView clearingCacheView, final long[] jArr, final BottomSheet bottomSheet) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.ClearCacheButtonInternal.lambda$doClearCache$5(zArr, clearingCacheView, jArr, bottomSheet);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(DialogInterface dialogInterface, int i) {
            doClearCache();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            String str;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            StringBuilder sb = new StringBuilder();
            sb.append(LocaleController.getString(R.string.ClearCache));
            if (TextUtils.isEmpty(this.valueTextView.getText())) {
                str = "";
            } else {
                str = " (" + ((Object) this.valueTextView.getText()) + ")";
            }
            sb.append(str);
            AlertDialog create = builder.setTitle(sb.toString()).setMessage(LocaleController.getString(R.string.StorageUsageInfo)).setPositiveButton(this.textView.getText(), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$ClearCacheButtonInternal$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    CacheControlActivity.ClearCacheButtonInternal.this.lambda$new$0(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
            CacheControlActivity.this.showDialog(create);
            View button = create.getButton(-1);
            if (button instanceof TextView) {
                int i = Theme.key_text_RedRegular;
                ((TextView) button).setTextColor(Theme.getColor(i));
                button.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), Theme.multAlpha(Theme.getColor(i), 0.12f)));
            }
        }

        public void updateSize() {
            setSize(CacheControlActivity.this.isAllSectionsSelected(), (CacheControlActivity.this.selected[0] ? CacheControlActivity.this.photoSize : 0L) + (CacheControlActivity.this.selected[1] ? CacheControlActivity.this.videoSize : 0L) + (CacheControlActivity.this.selected[2] ? CacheControlActivity.this.documentsSize : 0L) + (CacheControlActivity.this.selected[3] ? CacheControlActivity.this.musicSize : 0L) + (CacheControlActivity.this.selected[4] ? CacheControlActivity.this.audioSize : 0L) + (CacheControlActivity.this.selected[5] ? CacheControlActivity.this.storiesSize : 0L) + (CacheControlActivity.this.selected[6] ? CacheControlActivity.this.stickersCacheSize : 0L) + (CacheControlActivity.this.selected[7] ? CacheControlActivity.this.cacheSize : 0L) + (CacheControlActivity.this.selected[8] ? CacheControlActivity.this.cacheTempSize : 0L) + (CacheControlActivity.this.selected[9] ? CacheControlActivity.this.logsSize : 0L));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ClearingCacheView extends FrameLayout {
        RLottieImageView imageView;
        AnimatedTextView percentsTextView;
        ProgressView progressView;
        TextView subtitle;
        TextView title;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class ProgressView extends View {
            Paint in;
            Paint out;
            float progress;
            AnimatedFloat progressT;

            public ProgressView(Context context) {
                super(context);
                this.in = new Paint(1);
                this.out = new Paint(1);
                this.progressT = new AnimatedFloat(this, 350L, CubicBezierInterpolator.EASE_OUT);
                Paint paint = this.in;
                int i = Theme.key_switchTrackChecked;
                paint.setColor(Theme.getColor(i));
                this.out.setColor(Theme.multAlpha(Theme.getColor(i), 0.2f));
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), this.out);
                rectF.set(0.0f, 0.0f, getMeasuredWidth() * this.progressT.set(this.progress), getMeasuredHeight());
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), this.in);
            }

            public void setProgress(float f) {
                this.progress = f;
                invalidate();
            }
        }

        public ClearingCacheView(Context context) {
            super(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setAutoRepeat(true);
            this.imageView.setAnimation(R.raw.utyan_cache, 150, 150);
            addView(this.imageView, LayoutHelper.createFrame(150, 150.0f, 49, 0.0f, 16.0f, 0.0f, 0.0f));
            this.imageView.playAnimation();
            AnimatedTextView animatedTextView = new AnimatedTextView(context, false, true, true);
            this.percentsTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.35f, 0L, 120L, CubicBezierInterpolator.EASE_OUT);
            this.percentsTextView.setGravity(1);
            AnimatedTextView animatedTextView2 = this.percentsTextView;
            int i = Theme.key_dialogTextBlack;
            animatedTextView2.setTextColor(Theme.getColor(i));
            this.percentsTextView.setTextSize(AndroidUtilities.dp(24.0f));
            this.percentsTextView.setTypeface(AndroidUtilities.bold());
            addView(this.percentsTextView, LayoutHelper.createFrame(-1, 32.0f, 49, 0.0f, 176.0f, 0.0f, 0.0f));
            ProgressView progressView = new ProgressView(context);
            this.progressView = progressView;
            addView(progressView, LayoutHelper.createFrame(NotificationCenter.needShareTheme, 5.0f, 49, 0.0f, 226.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.title = textView;
            textView.setGravity(1);
            this.title.setTextColor(Theme.getColor(i));
            this.title.setTextSize(1, 16.0f);
            this.title.setTypeface(AndroidUtilities.bold());
            this.title.setText(LocaleController.getString(R.string.ClearingCache));
            addView(this.title, LayoutHelper.createFrame(-1, -2.0f, 49, 0.0f, 261.0f, 0.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.subtitle = textView2;
            textView2.setGravity(1);
            this.subtitle.setTextColor(Theme.getColor(i));
            this.subtitle.setTextSize(1, 14.0f);
            this.subtitle.setText(LocaleController.getString(R.string.ClearingCacheDescription));
            addView(this.subtitle, LayoutHelper.createFrame(NotificationCenter.needShareTheme, -2.0f, 49, 0.0f, 289.0f, 0.0f, 0.0f));
            setProgress(0.0f);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(350.0f), 1073741824));
        }

        public void setProgress(float f) {
            this.percentsTextView.cancelAnimation();
            this.percentsTextView.setText(String.format("%d%%", Integer.valueOf((int) Math.ceil(MathUtils.clamp(f, 0.0f, 1.0f) * 100.0f))), true ^ LocaleController.isRTL);
            this.progressView.setProgress(f);
        }
    }

    /* loaded from: classes4.dex */
    public static class DialogFileEntities {
        public long dialogId;
        public final SparseArray entitiesByType = new SparseArray();
        int filesCount;
        long totalSize;

        public DialogFileEntities(long j) {
            this.dialogId = j;
        }

        public void addFile(CacheModel.FileInfo fileInfo, int i) {
            FileEntities fileEntities = (FileEntities) this.entitiesByType.get(i, null);
            if (fileEntities == null) {
                fileEntities = new FileEntities();
                this.entitiesByType.put(i, fileEntities);
            }
            fileEntities.count++;
            long j = fileInfo.size;
            fileEntities.totalSize += j;
            this.totalSize += j;
            this.filesCount++;
            fileEntities.files.add(fileInfo);
        }

        public CacheModel createCacheModel() {
            CacheModel cacheModel = new CacheModel(true);
            if (this.entitiesByType.get(0) != null) {
                cacheModel.media.addAll(((FileEntities) this.entitiesByType.get(0)).files);
            }
            if (this.entitiesByType.get(1) != null) {
                cacheModel.media.addAll(((FileEntities) this.entitiesByType.get(1)).files);
            }
            if (this.entitiesByType.get(2) != null) {
                cacheModel.documents.addAll(((FileEntities) this.entitiesByType.get(2)).files);
            }
            if (this.entitiesByType.get(3) != null) {
                cacheModel.music.addAll(((FileEntities) this.entitiesByType.get(3)).files);
            }
            if (this.entitiesByType.get(4) != null) {
                cacheModel.voice.addAll(((FileEntities) this.entitiesByType.get(4)).files);
            }
            cacheModel.selectAllFiles();
            cacheModel.sortBySize();
            return cacheModel;
        }

        public boolean isEmpty() {
            return this.totalSize <= 0;
        }

        public void merge(DialogFileEntities dialogFileEntities) {
            for (int i = 0; i < dialogFileEntities.entitiesByType.size(); i++) {
                int keyAt = dialogFileEntities.entitiesByType.keyAt(i);
                FileEntities fileEntities = (FileEntities) dialogFileEntities.entitiesByType.valueAt(i);
                FileEntities fileEntities2 = (FileEntities) this.entitiesByType.get(keyAt, null);
                if (fileEntities2 == null) {
                    fileEntities2 = new FileEntities();
                    this.entitiesByType.put(keyAt, fileEntities2);
                }
                fileEntities2.count += fileEntities.count;
                fileEntities2.totalSize += fileEntities.totalSize;
                this.totalSize += fileEntities.totalSize;
                fileEntities2.files.addAll(fileEntities.files);
            }
            this.filesCount += dialogFileEntities.filesCount;
        }

        public void removeFile(CacheModel.FileInfo fileInfo) {
            FileEntities fileEntities = (FileEntities) this.entitiesByType.get(fileInfo.type, null);
            if (fileEntities != null && fileEntities.files.remove(fileInfo)) {
                fileEntities.count--;
                long j = fileEntities.totalSize;
                long j2 = fileInfo.size;
                fileEntities.totalSize = j - j2;
                this.totalSize -= j2;
                this.filesCount--;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class FileEntities {
        public int count;
        public ArrayList files = new ArrayList();
        public long totalSize;
    }

    /* loaded from: classes4.dex */
    public static class ItemInner extends AdapterWithDiffUtils.Item {
        int colorKey;
        DialogFileEntities entities;
        int headerBottomMargin;
        CharSequence headerName;
        int headerTopMargin;
        public int index;
        int keepMediaType;
        boolean last;
        public boolean pad;
        public long size;
        String text;

        private ItemInner(int i) {
            super(i, true);
            this.headerTopMargin = 15;
            this.headerBottomMargin = 0;
            this.keepMediaType = -1;
        }

        public ItemInner(int i, int i2) {
            super(i, true);
            this.headerTopMargin = 15;
            this.headerBottomMargin = 0;
            this.keepMediaType = i2;
        }

        public ItemInner(int i, String str, DialogFileEntities dialogFileEntities) {
            super(i, true);
            this.headerTopMargin = 15;
            this.headerBottomMargin = 0;
            this.keepMediaType = -1;
            this.headerName = str;
            this.entities = dialogFileEntities;
        }

        public static ItemInner asCheckBox(CharSequence charSequence, int i, long j, int i2) {
            return asCheckBox(charSequence, i, j, i2, false);
        }

        public static ItemInner asCheckBox(CharSequence charSequence, int i, long j, int i2, boolean z) {
            ItemInner itemInner = new ItemInner(11);
            itemInner.index = i;
            itemInner.headerName = charSequence;
            itemInner.size = j;
            itemInner.colorKey = i2;
            itemInner.last = z;
            return itemInner;
        }

        public static ItemInner asInfo(String str) {
            ItemInner itemInner = new ItemInner(1);
            itemInner.text = str;
            return itemInner;
        }

        public boolean equals(Object obj) {
            Object obj2;
            Object obj3;
            DialogFileEntities dialogFileEntities;
            DialogFileEntities dialogFileEntities2;
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                ItemInner itemInner = (ItemInner) obj;
                int i = this.viewType;
                if (i == itemInner.viewType) {
                    if (i != 9 && i != 10) {
                        if (i == 5 && (dialogFileEntities = this.entities) != null && (dialogFileEntities2 = itemInner.entities) != null) {
                            return dialogFileEntities.dialogId == dialogFileEntities2.dialogId;
                        }
                        if (i != 8 && i != 4 && i != 2 && i != 0 && i != 13) {
                            if (i == 3) {
                                obj2 = this.headerName;
                                obj3 = itemInner.headerName;
                            } else {
                                if (i != 1) {
                                    return i == 11 ? this.index == itemInner.index && this.size == itemInner.size : i == 7 && this.keepMediaType == itemInner.keepMediaType;
                                }
                                obj2 = this.text;
                                obj3 = itemInner.text;
                            }
                            return Objects.equals(obj2, obj3);
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends AdapterWithDiffUtils {
        private Context mContext;

        /* loaded from: classes4.dex */
        class 1 extends CacheChart {
            1(Context context) {
                super(context);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ int lambda$onSectionDown$0(int i) {
                return i;
            }

            @Override // org.telegram.ui.Components.CacheChart
            protected void onSectionClick(int i) {
            }

            @Override // org.telegram.ui.Components.CacheChart
            protected void onSectionDown(int i, boolean z) {
                if (!z) {
                    CacheControlActivity.this.listView.removeHighlightRow();
                    return;
                }
                final int i2 = -1;
                if (i == 8) {
                    i = -1;
                }
                int i3 = 0;
                while (true) {
                    if (i3 < CacheControlActivity.this.itemInners.size()) {
                        ItemInner itemInner = (ItemInner) CacheControlActivity.this.itemInners.get(i3);
                        if (itemInner != null && itemInner.viewType == 11 && itemInner.index == i) {
                            i2 = i3;
                            break;
                        }
                        i3++;
                    } else {
                        break;
                    }
                }
                RecyclerListView recyclerListView = CacheControlActivity.this.listView;
                if (i2 >= 0) {
                    recyclerListView.highlightRow(new RecyclerListView.IntReturnCallback() { // from class: org.telegram.ui.CacheControlActivity$ListAdapter$1$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Components.RecyclerListView.IntReturnCallback
                        public final int run() {
                            int lambda$onSectionDown$0;
                            lambda$onSectionDown$0 = CacheControlActivity.ListAdapter.1.lambda$onSectionDown$0(i2);
                            return lambda$onSectionDown$0;
                        }
                    }, 0);
                } else {
                    recyclerListView.removeHighlightRow();
                }
            }
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$2(View view) {
            CacheControlActivity.this.collapsed = !r2.collapsed;
            CacheControlActivity.this.updateRows();
            CacheControlActivity.this.updateChart();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$3(CheckBoxCell checkBoxCell, View view) {
            CacheControlActivity.this.toggleOtherSelected(checkBoxCell);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreateViewHolder$0(int i) {
            if (i == 0) {
                SharedConfig.setKeepMedia(3);
                return;
            }
            if (i == 1) {
                SharedConfig.setKeepMedia(0);
            } else if (i == 2) {
                SharedConfig.setKeepMedia(1);
            } else if (i == 3) {
                SharedConfig.setKeepMedia(2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreateViewHolder$1(ArrayList arrayList, int i) {
            SharedConfig.getPreferences().edit().putInt("cache_limit", ((Integer) arrayList.get(i)).intValue()).apply();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return CacheControlActivity.this.itemInners.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return ((ItemInner) CacheControlActivity.this.itemInners.get(i)).viewType;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return ((long) viewHolder.getAdapterPosition()) == CacheControlActivity.this.migrateOldFolderRow || (viewHolder.getItemViewType() == 2 && CacheControlActivity.this.totalSize > 0 && !CacheControlActivity.this.calculating) || viewHolder.getItemViewType() == 5 || viewHolder.getItemViewType() == 7 || viewHolder.getItemViewType() == 11;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            int i2;
            int themedColor;
            boolean z;
            boolean z2;
            CacheControlActivity cacheControlActivity;
            int i3;
            ItemInner itemInner = (ItemInner) CacheControlActivity.this.itemInners.get(i);
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == CacheControlActivity.this.migrateOldFolderRow) {
                    textSettingsCell.setTextAndValue(LocaleController.getString(R.string.MigrateOldFolder), null, false);
                    return;
                }
                return;
            }
            if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setText(AndroidUtilities.replaceTags(itemInner.text));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            if (itemViewType == 2) {
                ((StorageUsageView) viewHolder.itemView).setStorageUsage(CacheControlActivity.this.calculating, CacheControlActivity.this.databaseSize, CacheControlActivity.this.totalSize, CacheControlActivity.this.totalDeviceFreeSize, CacheControlActivity.this.totalDeviceSize);
                return;
            }
            if (itemViewType == 3) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                headerCell.setText(((ItemInner) CacheControlActivity.this.itemInners.get(i)).headerName);
                headerCell.setTopMargin(((ItemInner) CacheControlActivity.this.itemInners.get(i)).headerTopMargin);
                headerCell.setBottomMargin(((ItemInner) CacheControlActivity.this.itemInners.get(i)).headerBottomMargin);
                return;
            }
            if (itemViewType != 7) {
                switch (itemViewType) {
                    case 9:
                        CacheControlActivity.this.updateChart();
                        return;
                    case 10:
                        if (CacheControlActivity.this.cacheChartHeader == null || CacheControlActivity.this.calculating) {
                            return;
                        }
                        CacheChartHeader cacheChartHeader = CacheControlActivity.this.cacheChartHeader;
                        r3 = CacheControlActivity.this.totalSize > 0;
                        float f = 0.0f;
                        float f2 = CacheControlActivity.this.totalDeviceSize <= 0 ? 0.0f : ((float) CacheControlActivity.this.totalSize) / ((float) CacheControlActivity.this.totalDeviceSize);
                        if (CacheControlActivity.this.totalDeviceFreeSize > 0 && CacheControlActivity.this.totalDeviceSize > 0) {
                            f = ((float) (CacheControlActivity.this.totalDeviceSize - CacheControlActivity.this.totalDeviceFreeSize)) / ((float) CacheControlActivity.this.totalDeviceSize);
                        }
                        cacheChartHeader.setData(r3, f2, f);
                        return;
                    case 11:
                        final CheckBoxCell checkBoxCell = (CheckBoxCell) viewHolder.itemView;
                        boolean isOtherSelected = itemInner.index < 0 ? CacheControlActivity.this.isOtherSelected() : CacheControlActivity.this.selected[itemInner.index];
                        CacheControlActivity cacheControlActivity2 = CacheControlActivity.this;
                        CharSequence charSequence = itemInner.headerName;
                        int[] iArr = cacheControlActivity2.percents;
                        int i4 = itemInner.index;
                        CharSequence checkBoxTitle = cacheControlActivity2.getCheckBoxTitle(charSequence, iArr[i4 < 0 ? 9 : i4], i4 < 0);
                        String formatFileSize = AndroidUtilities.formatFileSize(itemInner.size);
                        if (itemInner.index >= 0 ? itemInner.last : CacheControlActivity.this.collapsed) {
                            r3 = false;
                        }
                        checkBoxCell.setText(checkBoxTitle, formatFileSize, isOtherSelected, r3);
                        checkBoxCell.setCheckBoxColor(itemInner.colorKey, Theme.key_windowBackgroundWhiteGrayIcon, Theme.key_checkboxCheck);
                        checkBoxCell.setCollapsed(itemInner.index < 0 ? Boolean.valueOf(CacheControlActivity.this.collapsed) : null);
                        if (itemInner.index == -1) {
                            checkBoxCell.setOnSectionsClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$ListAdapter$$ExternalSyntheticLambda0
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    CacheControlActivity.ListAdapter.this.lambda$onBindViewHolder$2(view);
                                }
                            }, new View.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$ListAdapter$$ExternalSyntheticLambda1
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    CacheControlActivity.ListAdapter.this.lambda$onBindViewHolder$3(checkBoxCell, view);
                                }
                            });
                        } else {
                            checkBoxCell.setOnSectionsClickListener(null, null);
                        }
                        checkBoxCell.setPad(itemInner.pad ? 1 : 0);
                        return;
                    default:
                        return;
                }
            }
            TextCell textCell = (TextCell) viewHolder.itemView;
            CacheByChatsController cacheByChatsController = CacheControlActivity.this.getMessagesController().getCacheByChatsController();
            int i5 = itemInner.keepMediaType;
            int size = cacheByChatsController.getKeepMediaExceptions(((ItemInner) CacheControlActivity.this.itemInners.get(i)).keepMediaType).size();
            String formatPluralString = size > 0 ? LocaleController.formatPluralString("ExceptionShort", size, Integer.valueOf(size)) : null;
            String keepMediaString = CacheByChatsController.getKeepMediaString(cacheByChatsController.getKeepMedia(i5));
            if (((ItemInner) CacheControlActivity.this.itemInners.get(i)).keepMediaType == 0) {
                string = LocaleController.getString(R.string.PrivateChats);
                i2 = R.drawable.msg_filled_menu_users;
                cacheControlActivity = CacheControlActivity.this;
                i3 = Theme.key_statisticChartLine_lightblue;
            } else if (((ItemInner) CacheControlActivity.this.itemInners.get(i)).keepMediaType == 1) {
                string = LocaleController.getString(R.string.GroupChats);
                i2 = R.drawable.msg_filled_menu_groups;
                cacheControlActivity = CacheControlActivity.this;
                i3 = Theme.key_statisticChartLine_green;
            } else {
                if (((ItemInner) CacheControlActivity.this.itemInners.get(i)).keepMediaType != 2) {
                    if (((ItemInner) CacheControlActivity.this.itemInners.get(i)).keepMediaType == 3) {
                        string = LocaleController.getString(R.string.CacheStories);
                        i2 = R.drawable.msg_filled_stories;
                        themedColor = CacheControlActivity.this.getThemedColor(Theme.key_statisticChartLine_red);
                        z = false;
                        z2 = false;
                        textCell.setTextAndValueAndColorfulIcon(string, keepMediaString, z, i2, themedColor, z2);
                    }
                    textCell.setSubtitle(formatPluralString);
                }
                string = LocaleController.getString(R.string.CacheChannels);
                i2 = R.drawable.msg_filled_menu_channels;
                cacheControlActivity = CacheControlActivity.this;
                i3 = Theme.key_statisticChartLine_golden;
            }
            themedColor = cacheControlActivity.getThemedColor(i3);
            z = true;
            z2 = true;
            textCell.setTextAndValueAndColorfulIcon(string, keepMediaString, z, i2, themedColor, z2);
            textCell.setSubtitle(formatPluralString);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            SlideChooseView slideChooseView;
            int i2;
            FlickerLoadingView flickerLoadingView;
            if (i != 0) {
                switch (i) {
                    case 2:
                        view = new StorageUsageView(this.mContext);
                        break;
                    case 3:
                        view = new HeaderCell(this.mContext);
                        break;
                    case 4:
                        slideChooseView = new SlideChooseView(this.mContext);
                        slideChooseView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.CacheControlActivity$ListAdapter$$ExternalSyntheticLambda3
                            @Override // org.telegram.ui.Components.SlideChooseView.Callback
                            public final void onOptionSelected(int i3) {
                                CacheControlActivity.ListAdapter.lambda$onCreateViewHolder$0(i3);
                            }

                            @Override // org.telegram.ui.Components.SlideChooseView.Callback
                            public /* synthetic */ void onTouchEnd() {
                                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                            }
                        });
                        int i3 = SharedConfig.keepMedia;
                        slideChooseView.setOptions(i3 == 3 ? 0 : i3 + 1, LocaleController.formatPluralString("Days", 3, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.formatPluralString("Months", 1, new Object[0]), LocaleController.getString(R.string.KeepMediaForever));
                        view2 = slideChooseView;
                        break;
                    case 5:
                        view = new UserCell(CacheControlActivity.this.getContext(), CacheControlActivity.this.getResourceProvider());
                        break;
                    case 6:
                        FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(CacheControlActivity.this.getContext());
                        flickerLoadingView2.setIsSingleCell(true);
                        flickerLoadingView2.setItemsCount(3);
                        flickerLoadingView2.setIgnoreHeightCheck(true);
                        i2 = 25;
                        flickerLoadingView = flickerLoadingView2;
                        flickerLoadingView.setViewType(i2);
                        view = flickerLoadingView;
                        break;
                    case 7:
                        view = new TextCell(this.mContext);
                        break;
                    case 8:
                        View view3 = CacheControlActivity.this.cachedMediaLayout = new CachedMediaLayout(this.mContext, CacheControlActivity.this) { // from class: org.telegram.ui.CacheControlActivity.ListAdapter.2
                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.CachedMediaLayout, android.widget.FrameLayout, android.view.View
                            public void onMeasure(int i4, int i5) {
                                super.onMeasure(i4, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i5) - (ActionBar.getCurrentActionBarHeight() / 2), 1073741824));
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.CachedMediaLayout
                            public void showActionMode(boolean z) {
                                if (!z) {
                                    ((BaseFragment) CacheControlActivity.this).actionBar.hideActionMode();
                                } else {
                                    CacheControlActivity.this.updateActionBar(true);
                                    ((BaseFragment) CacheControlActivity.this).actionBar.showActionMode();
                                }
                            }
                        };
                        CacheControlActivity.this.cachedMediaLayout.setDelegate(new CachedMediaLayout.Delegate() { // from class: org.telegram.ui.CacheControlActivity.ListAdapter.3
                            @Override // org.telegram.ui.CachedMediaLayout.Delegate
                            public void clear() {
                                CacheControlActivity.this.clearSelectedFiles();
                            }

                            @Override // org.telegram.ui.CachedMediaLayout.Delegate
                            public void clearSelection() {
                                CacheModel cacheModel = CacheControlActivity.this.cacheModel;
                                if (cacheModel == null || cacheModel.getSelectedFiles() <= 0) {
                                    return;
                                }
                                CacheControlActivity.this.cacheModel.clearSelection();
                                if (CacheControlActivity.this.cachedMediaLayout != null) {
                                    CacheControlActivity.this.cachedMediaLayout.showActionMode(false);
                                    CacheControlActivity.this.cachedMediaLayout.updateVisibleRows();
                                }
                            }

                            @Override // org.telegram.ui.CachedMediaLayout.Delegate
                            public /* synthetic */ void dismiss() {
                                CachedMediaLayout.Delegate.-CC.$default$dismiss(this);
                            }

                            @Override // org.telegram.ui.CachedMediaLayout.Delegate
                            public void onItemSelected(DialogFileEntities dialogFileEntities, CacheModel.FileInfo fileInfo, boolean z) {
                                if (dialogFileEntities == null) {
                                    if (fileInfo != null) {
                                        CacheControlActivity.this.cacheModel.toggleSelect(fileInfo);
                                        CacheControlActivity.this.cachedMediaLayout.updateVisibleRows();
                                        CacheControlActivity.this.updateActionMode();
                                        return;
                                    }
                                    return;
                                }
                                if (CacheControlActivity.this.cacheModel.getSelectedFiles() <= 0 && !z) {
                                    CacheControlActivity.this.showClearCacheDialog(dialogFileEntities);
                                    return;
                                }
                                CacheControlActivity.this.cacheModel.toggleSelect(dialogFileEntities);
                                CacheControlActivity.this.cachedMediaLayout.updateVisibleRows();
                                CacheControlActivity.this.updateActionMode();
                            }
                        });
                        CacheControlActivity.this.cachedMediaLayout.setCacheModel(CacheControlActivity.this.cacheModel);
                        CacheControlActivity.this.nestedSizeNotifierLayout.setChildLayout(CacheControlActivity.this.cachedMediaLayout);
                        view3.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        view3.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                        view2 = view3;
                        break;
                    case 9:
                        view2 = CacheControlActivity.this.cacheChart = new 1(this.mContext);
                        break;
                    case 10:
                        view2 = CacheControlActivity.this.cacheChartHeader = new CacheChartHeader(this.mContext);
                        break;
                    case 11:
                        view = new CheckBoxCell(this.mContext, 4, 21, CacheControlActivity.this.getResourceProvider());
                        break;
                    case 12:
                        FlickerLoadingView flickerLoadingView3 = new FlickerLoadingView(CacheControlActivity.this.getContext());
                        flickerLoadingView3.setIsSingleCell(true);
                        flickerLoadingView3.setItemsCount(1);
                        flickerLoadingView3.setIgnoreHeightCheck(true);
                        i2 = 26;
                        flickerLoadingView = flickerLoadingView3;
                        flickerLoadingView.setViewType(i2);
                        view = flickerLoadingView;
                        break;
                    case 13:
                        view2 = CacheControlActivity.this.clearCacheButton = new ClearCacheButtonInternal(this.mContext);
                        break;
                    case 14:
                        slideChooseView = new SlideChooseView(this.mContext);
                        slideChooseView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                        float f = ((int) ((CacheControlActivity.this.totalDeviceSize / 1024) / 1024)) / 1000.0f;
                        final ArrayList arrayList = new ArrayList();
                        if (f <= 17.0f) {
                            arrayList.add(2);
                        }
                        if (f > 5.0f) {
                            arrayList.add(5);
                        }
                        if (f > 16.0f) {
                            arrayList.add(16);
                        }
                        if (f > 32.0f) {
                            arrayList.add(32);
                        }
                        arrayList.add(Integer.valueOf(ConnectionsManager.DEFAULT_DATACENTER_ID));
                        String[] strArr = new String[arrayList.size()];
                        for (int i4 = 0; i4 < arrayList.size(); i4++) {
                            if (((Integer) arrayList.get(i4)).intValue() == 1) {
                                strArr[i4] = String.format("300 MB", new Object[0]);
                            } else if (((Integer) arrayList.get(i4)).intValue() == Integer.MAX_VALUE) {
                                strArr[i4] = LocaleController.getString(R.string.NoLimit);
                            } else {
                                strArr[i4] = String.format("%d GB", arrayList.get(i4));
                            }
                        }
                        slideChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.CacheControlActivity$ListAdapter$$ExternalSyntheticLambda2
                            @Override // org.telegram.ui.Components.SlideChooseView.Callback
                            public final void onOptionSelected(int i5) {
                                CacheControlActivity.ListAdapter.lambda$onCreateViewHolder$1(arrayList, i5);
                            }

                            @Override // org.telegram.ui.Components.SlideChooseView.Callback
                            public /* synthetic */ void onTouchEnd() {
                                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                            }
                        });
                        int indexOf = arrayList.indexOf(Integer.valueOf(SharedConfig.getPreferences().getInt("cache_limit", ConnectionsManager.DEFAULT_DATACENTER_ID)));
                        if (indexOf < 0) {
                            indexOf = arrayList.size() - 1;
                        }
                        slideChooseView.setOptions(indexOf, strArr);
                        view2 = slideChooseView;
                        break;
                    default:
                        view2 = new TextInfoPrivacyCell(this.mContext);
                        break;
                }
                return new RecyclerListView.Holder(view2);
            }
            view = new TextSettingsCell(this.mContext);
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            view2 = view;
            return new RecyclerListView.Holder(view2);
        }
    }

    /* loaded from: classes4.dex */
    public static class UserCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
        private boolean canDisable;
        protected CheckBox2 checkBox;
        public DialogFileEntities dialogFileEntities;
        private BackupImageView imageView;
        private boolean needDivider;
        private Theme.ResourcesProvider resourcesProvider;
        private TextView textView;
        private AnimatedTextView valueTextView;

        public UserCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setSingleLine();
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            TextView textView2 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(textView2, LayoutHelper.createFrame(-1, -1.0f, (z ? 5 : 3) | 48, z ? 21.0f : 72.0f, 0.0f, z ? 72.0f : 21.0f, 0.0f));
            AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, !LocaleController.isRTL);
            this.valueTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.55f, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.valueTextView.setTextSize(AndroidUtilities.dp(16.0f));
            this.valueTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 16);
            this.valueTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText, resourcesProvider));
            AnimatedTextView animatedTextView2 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(animatedTextView2, LayoutHelper.createFrame(-2, -1.0f, (z2 ? 3 : 5) | 48, z2 ? 21.0f : 72.0f, 0.0f, z2 ? 72.0f : 21.0f, 0.0f));
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.getAvatarDrawable().setScaleSize(0.8f);
            addView(this.imageView, LayoutHelper.createFrame(38, 38.0f, (LocaleController.isRTL ? 5 : 3) | 16, 17.0f, 0.0f, 17.0f, 0.0f));
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            TextView textView;
            if (i != NotificationCenter.emojiLoaded || (textView = this.textView) == null) {
                return;
            }
            textView.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(72.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(72.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }

        public BackupImageView getImageView() {
            return this.imageView;
        }

        public TextView getTextView() {
            return this.textView;
        }

        public AnimatedTextView getValueTextView() {
            return this.valueTextView;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            String str;
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            StringBuilder sb = new StringBuilder();
            sb.append((Object) this.textView.getText());
            AnimatedTextView animatedTextView = this.valueTextView;
            if (animatedTextView == null || animatedTextView.getVisibility() != 0) {
                str = "";
            } else {
                str = "\n" + ((Object) this.valueTextView.getText());
            }
            sb.append(str);
            accessibilityNodeInfo.setText(sb.toString());
            accessibilityNodeInfo.setEnabled(isEnabled());
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
            int measuredWidth = ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()) - AndroidUtilities.dp(34.0f);
            int i3 = measuredWidth / 2;
            if (this.imageView.getVisibility() == 0) {
                this.imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(38.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(38.0f), 1073741824));
            }
            if (this.valueTextView.getVisibility() == 0) {
                this.valueTextView.measure(View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
                measuredWidth = (measuredWidth - this.valueTextView.getMeasuredWidth()) - AndroidUtilities.dp(8.0f);
            }
            int measuredWidth2 = this.valueTextView.getMeasuredWidth() + AndroidUtilities.dp(12.0f);
            if (LocaleController.isRTL) {
                ((ViewGroup.MarginLayoutParams) this.textView.getLayoutParams()).leftMargin = measuredWidth2;
            } else {
                ((ViewGroup.MarginLayoutParams) this.textView.getLayoutParams()).rightMargin = measuredWidth2;
            }
            this.textView.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth - measuredWidth2, 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
            CheckBox2 checkBox2 = this.checkBox;
            if (checkBox2 != null) {
                checkBox2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
            }
        }

        public void setCanDisable(boolean z) {
            this.canDisable = z;
        }

        public void setChecked(boolean z, boolean z2) {
            CheckBox2 checkBox2 = this.checkBox;
            if (checkBox2 != null || z) {
                if (checkBox2 == null) {
                    CheckBox2 checkBox22 = new CheckBox2(getContext(), 21, this.resourcesProvider);
                    this.checkBox = checkBox22;
                    checkBox22.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
                    this.checkBox.setDrawUnchecked(false);
                    this.checkBox.setDrawBackgroundAsArc(3);
                    addView(this.checkBox, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 5 : 3) | 48, 38.0f, 25.0f, 38.0f, 0.0f));
                }
                this.checkBox.setChecked(z, z2);
            }
        }

        @Override // android.view.View
        public void setEnabled(boolean z) {
            super.setEnabled(z);
            float f = 1.0f;
            this.textView.setAlpha((z || !this.canDisable) ? 1.0f : 0.5f);
            if (this.valueTextView.getVisibility() == 0) {
                AnimatedTextView animatedTextView = this.valueTextView;
                if (!z && this.canDisable) {
                    f = 0.5f;
                }
                animatedTextView.setAlpha(f);
            }
        }

        public void setTextAndValue(CharSequence charSequence, CharSequence charSequence2, boolean z) {
            setTextAndValue(charSequence, charSequence2, false, z);
        }

        public void setTextAndValue(CharSequence charSequence, CharSequence charSequence2, boolean z, boolean z2) {
            this.textView.setText(Emoji.replaceEmoji(charSequence, this.textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
            AnimatedTextView animatedTextView = this.valueTextView;
            if (charSequence2 != null) {
                animatedTextView.setText(charSequence2, z);
                this.valueTextView.setVisibility(0);
            } else {
                animatedTextView.setVisibility(4);
            }
            this.needDivider = z2;
            setWillNotDraw(!z2);
            requestLayout();
        }

        public void setTextColor(int i) {
            this.textView.setTextColor(i);
        }

        public void setTextValueColor(int i) {
            this.valueTextView.setTextColor(i);
        }
    }

    static /* synthetic */ float access$516(CacheControlActivity cacheControlActivity, float f) {
        float f2 = cacheControlActivity.actionBarShadowAlpha + f;
        cacheControlActivity.actionBarShadowAlpha = f2;
        return f2;
    }

    static /* synthetic */ float access$524(CacheControlActivity cacheControlActivity, float f) {
        float f2 = cacheControlActivity.actionBarShadowAlpha - f;
        cacheControlActivity.actionBarShadowAlpha = f2;
        return f2;
    }

    public static void calculateTotalSize(final Utilities.Callback callback) {
        if (callback == null) {
            return;
        }
        Long l = lastTotalSizeCalculated;
        if (l != null) {
            callback.run(l);
            if (System.currentTimeMillis() - lastTotalSizeCalculatedTime < 5000) {
                return;
            }
        }
        Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.lambda$calculateTotalSize$1(Utilities.Callback.this);
            }
        });
    }

    public static void cleanDirJava(String str, int i, int[] iArr, Utilities.Callback callback) {
        File[] listFiles;
        int countDirJava = countDirJava(str, i);
        if (iArr == null) {
            iArr = new int[]{0};
        }
        File file = new File(str);
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : listFiles) {
            String name = file2.getName();
            if (!".".equals(name)) {
                if (i > 0 && name.length() >= 4) {
                    String lowerCase = name.toLowerCase();
                    boolean z = lowerCase.endsWith(".mp3") || lowerCase.endsWith(".m4a");
                    boolean z2 = lowerCase.endsWith(".tgs") || lowerCase.endsWith(".webm");
                    boolean z3 = lowerCase.endsWith(".tmp") || lowerCase.endsWith(".temp") || lowerCase.endsWith(".preload");
                    if (z) {
                        if (i == LISTDIR_DOCTYPE_OTHER_THAN_MUSIC) {
                        }
                    }
                    if (!z) {
                        if (i == LISTDIR_DOCTYPE_MUSIC) {
                        }
                    }
                    if (z2) {
                        if (i == LISTDIR_DOCTYPE2_OTHER) {
                        }
                    }
                    if (!z2) {
                        if (i == LISTDIR_DOCTYPE2_EMOJI) {
                        }
                    }
                    if (z3) {
                        if (i == LISTDIR_DOCTYPE2_OTHER) {
                        }
                    }
                    if (!z3 && i == LISTDIR_DOCTYPE2_TEMP) {
                    }
                }
                if (!file2.isDirectory()) {
                    file2.delete();
                    int i2 = iArr[0] + 1;
                    iArr[0] = i2;
                    callback.run(Float.valueOf(i2 / countDirJava));
                } else if (!"drafts".equals(file2.getName())) {
                    cleanDirJava(str + "/" + name, i, iArr, callback);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupDialogFiles(DialogFileEntities dialogFileEntities, StorageDiagramView.ClearViewData[] clearViewDataArr, CacheModel cacheModel) {
        FileEntities fileEntities;
        int i;
        StorageDiagramView.ClearViewData clearViewData;
        final AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCancel(false);
        alertDialog.showDelayed(500L);
        HashSet hashSet = new HashSet();
        long j = this.totalSize;
        for (int i2 = 0; i2 < 8; i2++) {
            if ((clearViewDataArr == null || ((clearViewData = clearViewDataArr[i2]) != null && clearViewData.clear)) && (fileEntities = (FileEntities) dialogFileEntities.entitiesByType.get(i2)) != null) {
                hashSet.addAll(fileEntities.files);
                long j2 = dialogFileEntities.totalSize;
                long j3 = fileEntities.totalSize;
                dialogFileEntities.totalSize = j2 - j3;
                this.totalSize -= j3;
                this.totalDeviceFreeSize += j3;
                dialogFileEntities.entitiesByType.delete(i2);
                if (i2 == 0) {
                    this.photoSize -= fileEntities.totalSize;
                } else if (i2 == 1) {
                    this.videoSize -= fileEntities.totalSize;
                } else if (i2 == 2) {
                    this.documentsSize -= fileEntities.totalSize;
                } else if (i2 == 3) {
                    this.musicSize -= fileEntities.totalSize;
                } else if (i2 == 4) {
                    this.audioSize -= fileEntities.totalSize;
                } else if (i2 == 5) {
                    this.stickersCacheSize -= fileEntities.totalSize;
                } else {
                    int i3 = 7;
                    if (i2 == 7) {
                        int i4 = 0;
                        while (i4 < fileEntities.files.size()) {
                            CacheModel.FileInfo fileInfo = (CacheModel.FileInfo) fileEntities.files.get(i4);
                            int typeByPath = getTypeByPath(((CacheModel.FileInfo) fileEntities.files.get(i4)).file.getAbsolutePath());
                            if (typeByPath == i3) {
                                i = i4;
                                this.storiesSize -= fileInfo.size;
                            } else {
                                i = i4;
                                if (typeByPath == 0) {
                                    this.photoSize -= fileInfo.size;
                                } else if (typeByPath == 1) {
                                    this.videoSize -= fileInfo.size;
                                } else {
                                    this.cacheSize -= fileInfo.size;
                                }
                            }
                            i4 = i + 1;
                            i3 = 7;
                        }
                    } else {
                        this.cacheSize -= fileEntities.totalSize;
                    }
                }
            }
        }
        if (dialogFileEntities.entitiesByType.size() == 0) {
            this.cacheModel.remove(dialogFileEntities);
        }
        updateRows();
        if (cacheModel != null) {
            Iterator it = cacheModel.selectedFiles.iterator();
            while (it.hasNext()) {
                CacheModel.FileInfo fileInfo2 = (CacheModel.FileInfo) it.next();
                if (!hashSet.contains(fileInfo2)) {
                    long j4 = this.totalSize;
                    long j5 = fileInfo2.size;
                    this.totalSize = j4 - j5;
                    this.totalDeviceFreeSize += j5;
                    hashSet.add(fileInfo2);
                    dialogFileEntities.removeFile(fileInfo2);
                    int i5 = fileInfo2.type;
                    if (i5 == 0) {
                        this.photoSize -= fileInfo2.size;
                    } else if (i5 == 1) {
                        this.videoSize -= fileInfo2.size;
                    } else if (i5 == 2) {
                        this.documentsSize -= fileInfo2.size;
                    } else if (i5 == 3) {
                        this.musicSize -= fileInfo2.size;
                    } else if (i5 == 4) {
                        this.audioSize -= fileInfo2.size;
                    }
                }
            }
        }
        Iterator it2 = hashSet.iterator();
        while (it2.hasNext()) {
            this.cacheModel.onFileDeleted((CacheModel.FileInfo) it2.next());
        }
        this.cacheRemovedTooltip.setInfoText(LocaleController.formatString("CacheWasCleared", R.string.CacheWasCleared, AndroidUtilities.formatFileSize(j - this.totalSize)));
        this.cacheRemovedTooltip.showWithAction(0L, 19, null, null);
        final ArrayList arrayList = new ArrayList(hashSet);
        getFileLoader().getFileDatabase().removeFiles(arrayList);
        getFileLoader().cancelLoadAllFiles();
        getFileLoader().getFileLoaderQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupDialogFiles$23(arrayList, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupFolders(final Utilities.Callback2 callback2, final Runnable runnable) {
        CacheModel cacheModel = this.cacheModel;
        if (cacheModel != null) {
            cacheModel.clearSelection();
        }
        CachedMediaLayout cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout != null) {
            cachedMediaLayout.updateVisibleRows();
            this.cachedMediaLayout.showActionMode(false);
        }
        getFileLoader().cancelLoadAllFiles();
        getFileLoader().getFileLoaderQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupFolders$12(callback2, runnable);
            }
        });
        setCacheModel(null);
        this.loadingDialogs = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0258 A[SYNTHETIC] */
    /* renamed from: cleanupFoldersInternal, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$cleanupFolders$11(final Utilities.Callback2 callback2, final Runnable runnable) {
        int i;
        int[] iArr;
        int i2;
        int i3;
        char c;
        int i4;
        int i5 = 1;
        final int[] iArr2 = {0};
        boolean[] zArr = this.selected;
        int i6 = 5;
        final int i7 = (zArr[9] ? 1 : 0) + (zArr[0] ? 2 : 0) + (zArr[1] ? 2 : 0) + (zArr[2] ? 2 : 0) + (zArr[3] ? 2 : 0) + (zArr[4] ? 1 : 0) + (zArr[5] ? 2 : 0) + (zArr[6] ? 1 : 0) + (zArr[7] ? 1 : 0) + (zArr[8] ? 1 : 0);
        final long currentTimeMillis = System.currentTimeMillis();
        Utilities.Callback callback = new Utilities.Callback() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda23
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                CacheControlActivity.lambda$cleanupFoldersInternal$13(Utilities.Callback2.this, iArr2, i7, (Float) obj);
            }
        };
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.lambda$cleanupFoldersInternal$14(Utilities.Callback2.this, iArr2, i7, currentTimeMillis);
            }
        };
        long j = 0;
        boolean z = false;
        int i8 = 0;
        boolean z2 = true;
        while (i8 < 10) {
            if (this.selected[i8]) {
                if (i8 == 0) {
                    j += this.photoSize;
                    iArr = iArr2;
                    i5 = 0;
                } else if (i8 == i5) {
                    j += this.videoSize;
                    iArr = iArr2;
                    i5 = 2;
                } else {
                    if (i8 == 2) {
                        j += this.documentsSize;
                        iArr = iArr2;
                        i5 = 3;
                        i2 = -1;
                        i = 9;
                    } else {
                        if (i8 == 3) {
                            j += this.musicSize;
                            iArr = iArr2;
                            i5 = 3;
                            i2 = -1;
                            i = 9;
                            i3 = 2;
                        } else if (i8 == 4) {
                            j += this.audioSize;
                            iArr = iArr2;
                        } else if (i8 == i6) {
                            j += this.storiesSize;
                            iArr = iArr2;
                            i5 = 6;
                        } else if (i8 == 6) {
                            j += this.stickersCacheSize;
                            iArr = iArr2;
                            i5 = 100;
                        } else if (i8 == 7) {
                            j += this.cacheSize;
                            iArr = iArr2;
                            i5 = 4;
                            i2 = -1;
                            i = 9;
                            i3 = 5;
                        } else if (i8 == 8) {
                            j += this.cacheTempSize;
                            iArr = iArr2;
                            i5 = 4;
                            i2 = -1;
                            i = 9;
                            i3 = 4;
                        } else {
                            i = 9;
                            iArr = iArr2;
                            if (i8 == 9) {
                                j += this.logsSize;
                                i5 = 0;
                                i2 = -1;
                            } else {
                                i5 = -1;
                                i2 = -1;
                                i3 = 0;
                            }
                        }
                        if (i5 != i2) {
                            File logsDir = i8 == i ? AndroidUtilities.getLogsDir() : i5 == 100 ? new File(FileLoader.checkDirectory(4), "acache") : FileLoader.checkDirectory(i5);
                            if (logsDir != null) {
                                cleanDirJava(logsDir.getAbsolutePath(), i3, null, callback);
                            }
                            iArr[0] = iArr[0] + 1;
                            runnable2.run();
                            if (i5 == 100) {
                                File checkDirectory = FileLoader.checkDirectory(4);
                                if (checkDirectory != null) {
                                    cleanDirJava(checkDirectory.getAbsolutePath(), 3, null, callback);
                                }
                                iArr[0] = iArr[0] + 1;
                                runnable2.run();
                            }
                            if (i5 == 0 || i5 == 2) {
                                File checkDirectory2 = FileLoader.checkDirectory(i5 == 0 ? 100 : 101);
                                if (checkDirectory2 != null) {
                                    cleanDirJava(checkDirectory2.getAbsolutePath(), i3, null, callback);
                                }
                                c = 0;
                                iArr[0] = iArr[0] + 1;
                                runnable2.run();
                            } else {
                                c = 0;
                            }
                            if (i5 == 3) {
                                File checkDirectory3 = FileLoader.checkDirectory(5);
                                if (checkDirectory3 != null) {
                                    cleanDirJava(checkDirectory3.getAbsolutePath(), i3, null, callback);
                                }
                                i4 = 1;
                                iArr[c] = iArr[c] + 1;
                                runnable2.run();
                            } else {
                                i4 = 1;
                            }
                            if (i8 == 9) {
                                this.logsSize = getDirectorySize(AndroidUtilities.getLogsDir(), i4);
                            } else if (i5 == 4) {
                                this.cacheSize = getDirectorySize(FileLoader.checkDirectory(4), 5);
                                this.cacheTempSize = getDirectorySize(FileLoader.checkDirectory(4), 4);
                                z = true;
                            } else if (i5 == i4) {
                                this.audioSize = getDirectorySize(FileLoader.checkDirectory(i4), i3);
                            } else if (i5 == 6) {
                                this.storiesSize = getDirectorySize(FileLoader.checkDirectory(6), i3);
                            } else if (i5 != 3) {
                                i6 = 5;
                                if (i5 == 0) {
                                    long directorySize = getDirectorySize(FileLoader.checkDirectory(0), i3);
                                    this.photoSize = directorySize;
                                    this.photoSize = directorySize + getDirectorySize(FileLoader.checkDirectory(100), i3);
                                } else if (i5 == 2) {
                                    long directorySize2 = getDirectorySize(FileLoader.checkDirectory(2), i3);
                                    this.videoSize = directorySize2;
                                    this.videoSize = directorySize2 + getDirectorySize(FileLoader.checkDirectory(101), i3);
                                } else if (i5 == 100) {
                                    this.stickersCacheSize = getDirectorySize(new File(FileLoader.checkDirectory(4), "acache"), i3);
                                    long directorySize3 = getDirectorySize(FileLoader.checkDirectory(4), 3);
                                    this.cacheEmojiSize = directorySize3;
                                    this.stickersCacheSize += directorySize3;
                                }
                                z = true;
                            } else if (i3 == i4) {
                                long directorySize4 = getDirectorySize(FileLoader.checkDirectory(3), i3);
                                this.documentsSize = directorySize4;
                                i6 = 5;
                                this.documentsSize = directorySize4 + getDirectorySize(FileLoader.checkDirectory(5), i3);
                            } else {
                                i6 = 5;
                                long directorySize5 = getDirectorySize(FileLoader.checkDirectory(3), i3);
                                this.musicSize = directorySize5;
                                this.musicSize = directorySize5 + getDirectorySize(FileLoader.checkDirectory(5), i3);
                            }
                            i6 = 5;
                        }
                    }
                    i3 = 1;
                    if (i5 != i2) {
                    }
                }
                i2 = -1;
                i = 9;
                i3 = 0;
                if (i5 != i2) {
                }
            } else {
                iArr = iArr2;
                z2 = false;
            }
            i8++;
            iArr2 = iArr;
            i5 = 1;
        }
        long j2 = this.cacheSize + this.cacheTempSize + this.logsSize + this.videoSize + this.audioSize + this.photoSize + this.documentsSize + this.musicSize + this.stickersCacheSize + this.storiesSize;
        lastTotalSizeCalculated = Long.valueOf(j2);
        this.totalSize = j2;
        lastTotalSizeCalculatedTime = System.currentTimeMillis();
        Arrays.fill(this.selected, true);
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        this.totalDeviceSize = statFs.getBlockCountLong() * blockSizeLong;
        this.totalDeviceFreeSize = availableBlocksLong * blockSizeLong;
        if (z2) {
            FileLoader.getInstance(this.currentAccount).clearFilePaths();
        }
        FileLoader.getInstance(this.currentAccount).checkCurrentDownloadsFiles();
        final boolean z3 = z;
        final long j3 = j;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupFoldersInternal$16(z3, j3, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDatabase(final boolean z) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.LocalDatabaseClearTextTitle));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.LocalDatabaseClearText));
        spannableStringBuilder.append((CharSequence) "\n\n");
        spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString("LocalDatabaseClearText2", R.string.LocalDatabaseClearText2, AndroidUtilities.formatFileSize(this.databaseSize))));
        builder.setMessage(spannableStringBuilder);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.CacheClear), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                CacheControlActivity.this.lambda$clearDatabase$24(z, dialogInterface, i);
            }
        });
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSelectedFiles() {
        if (this.cacheModel.getSelectedFiles() == 0 || getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(LocaleController.getString(R.string.ClearCache));
        builder.setMessage(LocaleController.getString(R.string.ClearCacheForChats));
        builder.setPositiveButton(LocaleController.getString(R.string.Clear), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda12
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                CacheControlActivity.this.lambda$clearSelectedFiles$20(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    public static int countDirJava(String str, int i) {
        File[] listFiles;
        File file = new File(str);
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return 0;
        }
        int i2 = 0;
        for (File file2 : listFiles) {
            String name = file2.getName();
            if (!".".equals(name)) {
                if (i > 0 && name.length() >= 4) {
                    String lowerCase = name.toLowerCase();
                    boolean z = true;
                    boolean z2 = lowerCase.endsWith(".mp3") || lowerCase.endsWith(".m4a");
                    boolean z3 = lowerCase.endsWith(".tgs") || lowerCase.endsWith(".webm");
                    if (!lowerCase.endsWith(".tmp") && !lowerCase.endsWith(".temp") && !lowerCase.endsWith(".preload")) {
                        z = false;
                    }
                    if (z2) {
                        if (i == LISTDIR_DOCTYPE_OTHER_THAN_MUSIC) {
                        }
                    }
                    if (!z2) {
                        if (i == LISTDIR_DOCTYPE_MUSIC) {
                        }
                    }
                    if (z3) {
                        if (i == LISTDIR_DOCTYPE2_OTHER) {
                        }
                    }
                    if (!z3) {
                        if (i == LISTDIR_DOCTYPE2_EMOJI) {
                        }
                    }
                    if (z) {
                        if (i == LISTDIR_DOCTYPE2_OTHER) {
                        }
                    }
                    if (!z && i == LISTDIR_DOCTYPE2_TEMP) {
                    }
                }
                i2 = file2.isDirectory() ? i2 + countDirJava(str + "/" + name, i) : i2 + 1;
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatPercent(float f) {
        return formatPercent(f, true);
    }

    private String formatPercent(float f, boolean z) {
        if (z && f < 0.001f) {
            return String.format("<%.1f%%", Float.valueOf(0.1f));
        }
        float round = Math.round(f * 100.0f);
        return (!z || round > 0.0f) ? String.format("%d%%", Integer.valueOf((int) round)) : String.format("<%d%%", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CharSequence getCheckBoxTitle(CharSequence charSequence, int i, boolean z) {
        SpannableString spannableString = new SpannableString(i <= 0 ? String.format("<%.1f%%", Float.valueOf(1.0f)) : String.format("%d%%", Integer.valueOf(i)));
        spannableString.setSpan(new RelativeSizeSpan(0.834f), 0, spannableString.length(), 33);
        spannableString.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString.length(), 33);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        spannableStringBuilder.append((CharSequence) "  ");
        spannableStringBuilder.append((CharSequence) spannableString);
        return spannableStringBuilder;
    }

    public static void getDeviceTotalSize(final Utilities.Callback2 callback2) {
        Long l;
        Long l2 = lastDeviceTotalSize;
        if (l2 == null || (l = lastDeviceTotalFreeSize) == null) {
            Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.lambda$getDeviceTotalSize$3(Utilities.Callback2.this);
                }
            });
        } else if (callback2 != null) {
            callback2.run(l2, l);
        }
    }

    private static long getDirectorySize(File file, int i) {
        if (file == null || canceled) {
            return 0L;
        }
        if (file.isDirectory()) {
            return Utilities.getDirSize(file.getAbsolutePath(), i, false);
        }
        if (file.isFile()) {
            return file.length();
        }
        return 0L;
    }

    private int getTypeByPath(String str) {
        if (pathContains(str, 6)) {
            return 7;
        }
        if (pathContains(str, 0) || pathContains(str, 100)) {
            return 0;
        }
        return (pathContains(str, 2) || pathContains(str, 101)) ? 1 : 6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllSectionsSelected() {
        for (int i = 0; i < this.itemInners.size(); i++) {
            ItemInner itemInner = (ItemInner) this.itemInners.get(i);
            if (itemInner.viewType == 11) {
                int i2 = itemInner.index;
                if (i2 < 0) {
                    i2 = this.selected.length - 1;
                }
                if (!this.selected[i2]) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOtherSelected() {
        int i;
        int length = this.selected.length;
        boolean[] zArr = new boolean[length];
        for (int i2 = 0; i2 < this.itemInners.size(); i2++) {
            ItemInner itemInner = (ItemInner) this.itemInners.get(i2);
            if (itemInner.viewType == 11 && !itemInner.pad && (i = itemInner.index) >= 0) {
                zArr[i] = true;
            }
        }
        for (int i3 = 0; i3 < length; i3++) {
            if (!zArr[i3] && !this.selected[i3]) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$calculateTotalSize$0(Utilities.Callback callback, long j) {
        callback.run(Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$calculateTotalSize$1(final Utilities.Callback callback) {
        canceled = false;
        long directorySize = getDirectorySize(FileLoader.checkDirectory(4), 5);
        long directorySize2 = getDirectorySize(FileLoader.checkDirectory(4), 4);
        long directorySize3 = getDirectorySize(FileLoader.checkDirectory(0), 0) + getDirectorySize(FileLoader.checkDirectory(100), 0);
        long directorySize4 = getDirectorySize(FileLoader.checkDirectory(2), 0) + getDirectorySize(FileLoader.checkDirectory(101), 0);
        long directorySize5 = getDirectorySize(FileLoader.checkDirectory(3), 1) + getDirectorySize(FileLoader.checkDirectory(5), 1);
        long directorySize6 = getDirectorySize(FileLoader.checkDirectory(3), 2) + getDirectorySize(FileLoader.checkDirectory(5), 2);
        long directorySize7 = getDirectorySize(new File(FileLoader.checkDirectory(4), "acache"), 0) + getDirectorySize(FileLoader.checkDirectory(4), 3);
        long directorySize8 = getDirectorySize(FileLoader.checkDirectory(1), 0);
        long directorySize9 = getDirectorySize(FileLoader.checkDirectory(6), 0);
        long directorySize10 = getDirectorySize(AndroidUtilities.getLogsDir(), 1);
        if (!BuildVars.DEBUG_VERSION && directorySize10 < 268435456) {
            directorySize10 = 0;
        }
        final long j = directorySize + directorySize2 + directorySize4 + directorySize8 + directorySize3 + directorySize5 + directorySize6 + directorySize7 + directorySize9 + directorySize10;
        lastTotalSizeCalculated = Long.valueOf(j);
        lastTotalSizeCalculatedTime = System.currentTimeMillis();
        if (canceled) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.lambda$calculateTotalSize$0(Utilities.Callback.this, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupDialogFiles$22(AlertDialog alertDialog) {
        FileLoader.getInstance(this.currentAccount).checkCurrentDownloadsFiles();
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupDialogFiles$23(ArrayList arrayList, final AlertDialog alertDialog) {
        for (int i = 0; i < arrayList.size(); i++) {
            ((CacheModel.FileInfo) arrayList.get(i)).file.delete();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupDialogFiles$22(alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupFolders$12(final Utilities.Callback2 callback2, final Runnable runnable) {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupFolders$11(callback2, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cleanupFoldersInternal$13(Utilities.Callback2 callback2, int[] iArr, int i, Float f) {
        float f2 = i;
        callback2.run(Float.valueOf((iArr[0] / f2) + ((1.0f / f2) * MathUtils.clamp(f.floatValue(), 0.0f, 1.0f))), Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cleanupFoldersInternal$14(Utilities.Callback2 callback2, int[] iArr, int i, long j) {
        callback2.run(Float.valueOf(iArr[0] / i), Boolean.valueOf(System.currentTimeMillis() - j > 250));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupFoldersInternal$15(long j) {
        this.cacheRemovedTooltip.setInfoText(LocaleController.formatString("CacheWasCleared", R.string.CacheWasCleared, AndroidUtilities.formatFileSize(j)));
        this.cacheRemovedTooltip.showWithAction(0L, 19, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupFoldersInternal$16(boolean z, final long j, Runnable runnable) {
        if (z) {
            ImageLoader.getInstance().clearMemory();
        }
        try {
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.progressDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        getMediaDataController().ringtoneDataStore.checkRingtoneSoundsLoaded();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$cleanupFoldersInternal$15(j);
            }
        }, 150L);
        MediaDataController.getInstance(this.currentAccount).checkAllMedia(true);
        loadDialogEntities();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearDatabase$24(boolean z, DialogInterface dialogInterface, int i) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        this.progressDialog.showDelayed(500L);
        MessagesController.getInstance(this.currentAccount).clearQueryTime();
        if (z) {
            getMessagesStorage().fullReset();
        } else {
            getMessagesStorage().clearLocalDatabase();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearSelectedFiles$20(DialogInterface dialogInterface, int i) {
        DialogFileEntities removeSelectedFiles = this.cacheModel.removeSelectedFiles();
        if (removeSelectedFiles.totalSize > 0) {
            cleanupDialogFiles(removeSelectedFiles, null, null);
        }
        this.cacheModel.clearSelection();
        CachedMediaLayout cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout != null) {
            cachedMediaLayout.update();
            this.cachedMediaLayout.showActionMode(false);
        }
        updateRows();
        updateChart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(View view) {
        clearSelectedFiles();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(int i, int i2) {
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$19(View view, int i, float f, float f2) {
        if (getParentActivity() != null && i >= 0 && i < this.itemInners.size()) {
            ItemInner itemInner = (ItemInner) this.itemInners.get(i);
            if (itemInner.viewType == 11 && (view instanceof CheckBoxCell)) {
                if (itemInner.index >= 0) {
                    toggleSection(itemInner, view);
                    return;
                }
                this.collapsed = !this.collapsed;
                updateRows();
                updateChart();
                return;
            }
            DialogFileEntities dialogFileEntities = itemInner.entities;
            if (dialogFileEntities != null) {
                showClearCacheDialog(dialogFileEntities);
                return;
            }
            if (itemInner.keepMediaType >= 0) {
                KeepMediaPopupView keepMediaPopupView = new KeepMediaPopupView(this, view.getContext());
                ActionBarPopupWindow createSimplePopup = AlertsCreator.createSimplePopup(this, keepMediaPopupView, view, f, f2);
                keepMediaPopupView.update(((ItemInner) this.itemInners.get(i)).keepMediaType);
                keepMediaPopupView.setParentWindow(createSimplePopup);
                keepMediaPopupView.setCallback(new KeepMediaPopupView.Callback() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda8
                    @Override // org.telegram.ui.KeepMediaPopupView.Callback
                    public final void onKeepMediaChange(int i2, int i3) {
                        CacheControlActivity.this.lambda$createView$18(i2, i3);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDeviceTotalSize$2(long j, long j2, long j3, Utilities.Callback2 callback2) {
        lastDeviceTotalSize = Long.valueOf(j * j2);
        Long valueOf = Long.valueOf(j3 * j2);
        lastDeviceTotalFreeSize = valueOf;
        if (callback2 != null) {
            callback2.run(lastDeviceTotalSize, valueOf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDeviceTotalSize$3(final Utilities.Callback2 callback2) {
        ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
        File file = rootDirs.get(0);
        file.getAbsolutePath();
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
            int size = rootDirs.size();
            for (int i = 0; i < size; i++) {
                File file2 = rootDirs.get(i);
                if (file2.getAbsolutePath().startsWith(SharedConfig.storageCacheDir) && file2.canWrite()) {
                    file = file2;
                    break;
                }
            }
        }
        try {
            StatFs statFs = new StatFs(file.getPath());
            final long blockSizeLong = statFs.getBlockSizeLong();
            final long availableBlocksLong = statFs.getAvailableBlocksLong();
            final long blockCountLong = statFs.getBlockCountLong();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    CacheControlActivity.lambda$getDeviceTotalSize$2(blockCountLong, blockSizeLong, availableBlocksLong, callback2);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$25() {
        BottomSheet bottomSheet = this.bottomSheet;
        if (bottomSheet != null) {
            bottomSheet.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
        }
        View view = this.actionTextView;
        if (view != null) {
            view.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 4.0f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDialogEntities$6(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, CacheModel cacheModel) {
        boolean z;
        this.loadingDialogs = false;
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        DialogFileEntities dialogFileEntities = null;
        int i = 0;
        while (i < arrayList3.size()) {
            DialogFileEntities dialogFileEntities2 = (DialogFileEntities) arrayList3.get(i);
            if (getMessagesController().getUserOrChat(dialogFileEntities2.dialogId) == null) {
                dialogFileEntities2.dialogId = Long.MAX_VALUE;
                if (dialogFileEntities != null) {
                    dialogFileEntities.merge(dialogFileEntities2);
                    arrayList3.remove(i);
                    i--;
                    z = true;
                } else {
                    dialogFileEntities = dialogFileEntities2;
                    z = false;
                }
                if (z) {
                    sort(arrayList3);
                }
            }
            i++;
        }
        cacheModel.setEntities(arrayList3);
        if (canceled) {
            return;
        }
        setCacheModel(cacheModel);
        updateRows();
        updateChart();
        if (this.cacheChartHeader == null || this.calculating || System.currentTimeMillis() - this.fragmentCreateTime <= 120) {
            return;
        }
        CacheChartHeader cacheChartHeader = this.cacheChartHeader;
        long j = this.totalSize;
        boolean z2 = j > 0;
        long j2 = this.totalDeviceSize;
        float f = 0.0f;
        float f2 = j2 <= 0 ? 0.0f : ((float) j) / ((float) j2);
        long j3 = this.totalDeviceFreeSize;
        if (j3 > 0 && j2 > 0) {
            f = ((float) (j2 - j3)) / ((float) j2);
        }
        cacheChartHeader.setData(z2, f2, f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDialogEntities$7(ArrayList arrayList, ArrayList arrayList2, final ArrayList arrayList3, final CacheModel cacheModel) {
        final ArrayList<TLRPC.User> arrayList4 = new ArrayList<>();
        final ArrayList<TLRPC.Chat> arrayList5 = new ArrayList<>();
        if (!arrayList.isEmpty()) {
            try {
                getMessagesStorage().getUsersInternal((ArrayList<Long>) arrayList, arrayList4);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        if (!arrayList2.isEmpty()) {
            try {
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList2), arrayList5);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        int i = 0;
        while (i < arrayList3.size()) {
            if (((DialogFileEntities) arrayList3.get(i)).totalSize <= 0) {
                arrayList3.remove(i);
                i--;
            }
            i++;
        }
        sort(arrayList3);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$loadDialogEntities$6(arrayList4, arrayList5, arrayList3, cacheModel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDialogEntities$8() {
        getFileLoader().getFileDatabase().ensureDatabaseCreated();
        final CacheModel cacheModel = new CacheModel(false);
        LongSparseArray longSparseArray = new LongSparseArray();
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(4), 6, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(0), 0, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(100), 0, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(2), 1, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(101), 1, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(1), 4, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(6), 6, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(3), 2, longSparseArray, cacheModel);
        fillDialogsEntitiesRecursive(FileLoader.checkDirectory(5), 2, longSparseArray, cacheModel);
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        for (int i = 0; i < longSparseArray.size(); i++) {
            DialogFileEntities dialogFileEntities = (DialogFileEntities) longSparseArray.valueAt(i);
            arrayList.add(dialogFileEntities);
            if (getMessagesController().getUserOrChat(((DialogFileEntities) arrayList.get(i)).dialogId) == null) {
                long j = dialogFileEntities.dialogId;
                if (j > 0) {
                    arrayList2.add(Long.valueOf(j));
                } else {
                    arrayList3.add(Long.valueOf(j));
                }
            }
        }
        cacheModel.sortBySize();
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$loadDialogEntities$7(arrayList2, arrayList3, arrayList, cacheModel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFragmentCreate$4() {
        resumeDelayedFragmentAnimation();
        this.calculating = false;
        updateRows(true);
        updateChart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFragmentCreate$5() {
        this.cacheSize = getDirectorySize(FileLoader.checkDirectory(4), 5);
        if (canceled) {
            return;
        }
        this.cacheTempSize = getDirectorySize(FileLoader.checkDirectory(4), 4);
        if (canceled) {
            return;
        }
        long directorySize = getDirectorySize(FileLoader.checkDirectory(0), 0);
        this.photoSize = directorySize;
        this.photoSize = directorySize + getDirectorySize(FileLoader.checkDirectory(100), 0);
        if (canceled) {
            return;
        }
        long directorySize2 = getDirectorySize(FileLoader.checkDirectory(2), 0);
        this.videoSize = directorySize2;
        this.videoSize = directorySize2 + getDirectorySize(FileLoader.checkDirectory(101), 0);
        if (canceled) {
            return;
        }
        long directorySize3 = getDirectorySize(AndroidUtilities.getLogsDir(), 1);
        this.logsSize = directorySize3;
        if (!BuildVars.DEBUG_VERSION && directorySize3 < 268435456) {
            this.logsSize = 0L;
        }
        if (canceled) {
            return;
        }
        long directorySize4 = getDirectorySize(FileLoader.checkDirectory(3), 1);
        this.documentsSize = directorySize4;
        this.documentsSize = directorySize4 + getDirectorySize(FileLoader.checkDirectory(5), 1);
        if (canceled) {
            return;
        }
        long directorySize5 = getDirectorySize(FileLoader.checkDirectory(3), 2);
        this.musicSize = directorySize5;
        this.musicSize = directorySize5 + getDirectorySize(FileLoader.checkDirectory(5), 2);
        if (canceled) {
            return;
        }
        this.stickersCacheSize = getDirectorySize(new File(FileLoader.checkDirectory(4), "acache"), 0);
        if (canceled) {
            return;
        }
        this.cacheEmojiSize = getDirectorySize(FileLoader.checkDirectory(4), 3);
        if (canceled) {
            return;
        }
        this.stickersCacheSize += this.cacheEmojiSize;
        this.audioSize = getDirectorySize(FileLoader.checkDirectory(1), 0);
        this.storiesSize = getDirectorySize(FileLoader.checkDirectory(6), 0);
        if (canceled) {
            return;
        }
        long j = this.cacheSize + this.cacheTempSize + this.videoSize + this.logsSize + this.audioSize + this.photoSize + this.documentsSize + this.musicSize + this.storiesSize + this.stickersCacheSize;
        lastTotalSizeCalculated = Long.valueOf(j);
        this.totalSize = j;
        lastTotalSizeCalculatedTime = System.currentTimeMillis();
        ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
        File file = rootDirs.get(0);
        file.getAbsolutePath();
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
            int size = rootDirs.size();
            for (int i = 0; i < size; i++) {
                File file2 = rootDirs.get(i);
                if (file2.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                    file = file2;
                    break;
                }
            }
        }
        try {
            StatFs statFs = new StatFs(file.getPath());
            long blockSizeLong = statFs.getBlockSizeLong();
            long availableBlocksLong = statFs.getAvailableBlocksLong();
            this.totalDeviceSize = statFs.getBlockCountLong() * blockSizeLong;
            this.totalDeviceFreeSize = availableBlocksLong * blockSizeLong;
        } catch (Exception e) {
            FileLog.e(e);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$onFragmentCreate$4();
            }
        });
        loadDialogEntities();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sort$9(DialogFileEntities dialogFileEntities, DialogFileEntities dialogFileEntities2) {
        long j = dialogFileEntities2.totalSize;
        long j2 = dialogFileEntities.totalSize;
        if (j > j2) {
            return 1;
        }
        return j < j2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateActionBar$21(ValueAnimator valueAnimator) {
        this.actionBarShownT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.actionBar.setTitleColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), (int) (this.actionBarShownT * 255.0f)));
        this.actionBar.setBackgroundColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhite), (int) (this.actionBarShownT * 255.0f)));
        this.fragmentView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateRows$10(ItemInner itemInner, ItemInner itemInner2) {
        return Long.compare(itemInner2.size, itemInner.size);
    }

    private void loadDialogEntities() {
        getFileLoader().getFileDatabase().getQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$loadDialogEntities$8();
            }
        });
    }

    private boolean pathContains(String str, int i) {
        if (str == null || FileLoader.checkDirectory(i) == null) {
            return false;
        }
        return str.contains(FileLoader.checkDirectory(i).getAbsolutePath());
    }

    public static void resetCalculatedTotalSIze() {
        lastTotalSizeCalculated = null;
    }

    private int sectionsSelected() {
        int i = 0;
        for (int i2 = 0; i2 < 10; i2++) {
            if (this.selected[i2] && size(i2) > 0) {
                i++;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showClearCacheDialog(final DialogFileEntities dialogFileEntities) {
        if (this.totalSize <= 0 || getParentActivity() == null) {
            return;
        }
        DilogCacheBottomSheet dilogCacheBottomSheet = new DilogCacheBottomSheet(this, dialogFileEntities, dialogFileEntities.createCacheModel(), new DilogCacheBottomSheet.Delegate() { // from class: org.telegram.ui.CacheControlActivity.6
            @Override // org.telegram.ui.DilogCacheBottomSheet.Delegate
            public void cleanupDialogFiles(DialogFileEntities dialogFileEntities2, StorageDiagramView.ClearViewData[] clearViewDataArr, CacheModel cacheModel) {
                CacheControlActivity.this.cleanupDialogFiles(dialogFileEntities2, clearViewDataArr, cacheModel);
            }

            @Override // org.telegram.ui.DilogCacheBottomSheet.Delegate
            public void onAvatarClick() {
                String str;
                CacheControlActivity.this.bottomSheet.dismiss();
                Bundle bundle = new Bundle();
                long j = dialogFileEntities.dialogId;
                if (j > 0) {
                    str = "user_id";
                } else {
                    j = -j;
                    str = "chat_id";
                }
                bundle.putLong(str, j);
                CacheControlActivity.this.presentFragment(new ProfileActivity(bundle, null));
            }
        });
        this.bottomSheet = dilogCacheBottomSheet;
        showDialog(dilogCacheBottomSheet);
    }

    private long size(int i) {
        switch (i) {
            case 0:
                return this.photoSize;
            case 1:
                return this.videoSize;
            case 2:
                return this.documentsSize;
            case 3:
                return this.musicSize;
            case 4:
                return this.audioSize;
            case 5:
                return this.storiesSize;
            case 6:
                return this.stickersCacheSize;
            case 7:
                return this.cacheSize;
            case 8:
                return this.cacheTempSize;
            case 9:
                return this.logsSize;
            default:
                return 0L;
        }
    }

    private void sort(ArrayList arrayList) {
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda21
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sort$9;
                lambda$sort$9 = CacheControlActivity.lambda$sort$9((CacheControlActivity.DialogFileEntities) obj, (CacheControlActivity.DialogFileEntities) obj2);
                return lambda$sort$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleOtherSelected(View view) {
        int i;
        int childAdapterPosition;
        int i2;
        int i3;
        boolean isOtherSelected = isOtherSelected();
        if (isOtherSelected) {
            for (int i4 = 0; i4 < this.itemInners.size(); i4++) {
                ItemInner itemInner = (ItemInner) this.itemInners.get(i4);
                if (itemInner.viewType != 11 || itemInner.pad || (i3 = itemInner.index) < 0 || !this.selected[i3]) {
                }
            }
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
            if (view != null) {
                AndroidUtilities.shakeViewSpring(view, -3.0f);
                return;
            }
            return;
        }
        if (this.collapsed) {
            int length = this.selected.length;
            boolean[] zArr = new boolean[length];
            for (int i5 = 0; i5 < this.itemInners.size(); i5++) {
                ItemInner itemInner2 = (ItemInner) this.itemInners.get(i5);
                if (itemInner2.viewType == 11 && !itemInner2.pad && (i2 = itemInner2.index) >= 0) {
                    zArr[i2] = true;
                }
            }
            for (int i6 = 0; i6 < length; i6++) {
                if (!zArr[i6]) {
                    this.selected[i6] = !isOtherSelected;
                }
            }
        } else {
            for (int i7 = 0; i7 < this.itemInners.size(); i7++) {
                ItemInner itemInner3 = (ItemInner) this.itemInners.get(i7);
                if (itemInner3.viewType == 11 && itemInner3.pad && (i = itemInner3.index) >= 0) {
                    this.selected[i] = !isOtherSelected;
                }
            }
        }
        for (int i8 = 0; i8 < this.listView.getChildCount(); i8++) {
            View childAt = this.listView.getChildAt(i8);
            if ((childAt instanceof CheckBoxCell) && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt)) >= 0) {
                ItemInner itemInner4 = (ItemInner) this.itemInners.get(childAdapterPosition);
                if (itemInner4.viewType == 11) {
                    int i9 = itemInner4.index;
                    ((CheckBoxCell) childAt).setChecked(i9 < 0 ? !isOtherSelected : this.selected[i9], true);
                }
            }
        }
        updateChart();
    }

    private void toggleSection(ItemInner itemInner, View view) {
        int childAdapterPosition;
        int i = itemInner.index;
        if (i < 0) {
            toggleOtherSelected(view);
            return;
        }
        if (this.selected[i] && sectionsSelected() <= 1) {
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
            if (view != null) {
                AndroidUtilities.shakeViewSpring(view, -3.0f);
                return;
            }
            return;
        }
        int i2 = 0;
        if (view instanceof CheckBoxCell) {
            boolean[] zArr = this.selected;
            int i3 = itemInner.index;
            boolean z = !zArr[i3];
            zArr[i3] = z;
            ((CheckBoxCell) view).setChecked(z, true);
        } else {
            this.selected[itemInner.index] = !r8[r0];
            int indexOf = this.itemInners.indexOf(itemInner);
            if (indexOf >= 0) {
                for (int i4 = 0; i4 < this.listView.getChildCount(); i4++) {
                    View childAt = this.listView.getChildAt(i4);
                    if ((childAt instanceof CheckBoxCell) && indexOf == this.listView.getChildAdapterPosition(childAt)) {
                        ((CheckBoxCell) childAt).setChecked(this.selected[itemInner.index], true);
                    }
                }
            }
        }
        if (itemInner.pad) {
            while (true) {
                if (i2 >= this.listView.getChildCount()) {
                    break;
                }
                View childAt2 = this.listView.getChildAt(i2);
                if ((childAt2 instanceof CheckBoxCell) && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt2)) >= 0 && childAdapterPosition < this.itemInners.size() && ((ItemInner) this.itemInners.get(childAdapterPosition)).index < 0) {
                    ((CheckBoxCell) childAt2).setChecked(isOtherSelected(), true);
                    break;
                }
                i2++;
            }
        }
        updateChart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActionBar(boolean z) {
        if (z != this.actionBarShown) {
            ValueAnimator valueAnimator = this.actionBarAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float f = this.actionBarShownT;
            this.actionBarShown = z;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f, z ? 1.0f : 0.0f);
            this.actionBarAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda11
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    CacheControlActivity.this.lambda$updateActionBar$21(valueAnimator2);
                }
            });
            this.actionBarAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.actionBarAnimator.setDuration(380L);
            this.actionBarAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActionMode() {
        String formatPluralString;
        if (this.cacheModel.getSelectedFiles() <= 0) {
            this.cachedMediaLayout.showActionMode(false);
            return;
        }
        if (this.cachedMediaLayout != null) {
            if (this.cacheModel.selectedDialogs.isEmpty()) {
                formatPluralString = LocaleController.formatPluralString("Files", this.cacheModel.getSelectedFiles(), Integer.valueOf(this.cacheModel.getSelectedFiles()));
            } else {
                Iterator it = this.cacheModel.entities.iterator();
                int i = 0;
                while (it.hasNext()) {
                    DialogFileEntities dialogFileEntities = (DialogFileEntities) it.next();
                    if (this.cacheModel.selectedDialogs.contains(Long.valueOf(dialogFileEntities.dialogId))) {
                        i += dialogFileEntities.filesCount;
                    }
                }
                int selectedFiles = this.cacheModel.getSelectedFiles() - i;
                formatPluralString = selectedFiles > 0 ? String.format("%s, %s", LocaleController.formatPluralString("Chats", this.cacheModel.selectedDialogs.size(), Integer.valueOf(this.cacheModel.selectedDialogs.size())), LocaleController.formatPluralString("Files", selectedFiles, Integer.valueOf(selectedFiles))) : LocaleController.formatPluralString("Chats", this.cacheModel.selectedDialogs.size(), Integer.valueOf(this.cacheModel.selectedDialogs.size()));
            }
            this.actionModeTitle.setText(AndroidUtilities.formatFileSize(this.cacheModel.getSelectedFilesSize()), !LocaleController.isRTL);
            this.actionModeSubtitle.setText(formatPluralString, !LocaleController.isRTL);
            this.cachedMediaLayout.showActionMode(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateChart() {
        CacheChart cacheChart = this.cacheChart;
        if (cacheChart != null) {
            boolean z = this.calculating;
            if (!z && this.totalSize > 0) {
                CacheChart.SegmentSize[] segmentSizeArr = new CacheChart.SegmentSize[11];
                for (int i = 0; i < this.itemInners.size(); i++) {
                    ItemInner itemInner = (ItemInner) this.itemInners.get(i);
                    if (itemInner.viewType == 11) {
                        int i2 = itemInner.index;
                        if (i2 >= 0) {
                            segmentSizeArr[i2] = CacheChart.SegmentSize.of(itemInner.size, this.selected[i2]);
                        } else if (this.collapsed) {
                            segmentSizeArr[10] = CacheChart.SegmentSize.of(itemInner.size, this.selected[10]);
                        }
                    }
                }
                if (System.currentTimeMillis() - this.fragmentCreateTime < 80) {
                    this.cacheChart.loadingFloat.set(0.0f, true);
                }
                this.cacheChart.setSegments(this.totalSize, true, segmentSizeArr);
            } else if (z) {
                cacheChart.setSegments(-1L, true, new CacheChart.SegmentSize[0]);
            } else {
                cacheChart.setSegments(0L, true, new CacheChart.SegmentSize[0]);
            }
        }
        ClearCacheButtonInternal clearCacheButtonInternal = this.clearCacheButton;
        if (clearCacheButtonInternal == null || this.calculating) {
            return;
        }
        clearCacheButtonInternal.updateSize();
    }

    private void updateDatabaseItemSize() {
        if (this.clearDatabaseItem != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.ClearLocalDatabase));
            this.clearDatabaseItem.setText(spannableStringBuilder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows() {
        updateRows(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x024f  */
    /* JADX WARN: Type inference failed for: r3v11, types: [java.util.List] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateRows(boolean z) {
        boolean z2;
        float[] fArr;
        ArrayList arrayList;
        ArrayList arrayList2;
        ListAdapter listAdapter;
        CachedMediaLayout cachedMediaLayout;
        CacheModel cacheModel;
        boolean z3 = (!z || System.currentTimeMillis() - this.fragmentCreateTime >= 80) ? z : false;
        this.oldItems.clear();
        this.oldItems.addAll(this.itemInners);
        this.itemInners.clear();
        this.itemInners.add(new ItemInner(9, null, null));
        this.itemInners.add(new ItemInner(10, null, null));
        this.sectionsStartRow = this.itemInners.size();
        if (this.calculating) {
            this.itemInners.add(new ItemInner(12, null, null));
            this.itemInners.add(new ItemInner(12, null, null));
            this.itemInners.add(new ItemInner(12, null, null));
            this.itemInners.add(new ItemInner(12, null, null));
            this.itemInners.add(new ItemInner(12, null, null));
        } else {
            ArrayList arrayList3 = new ArrayList();
            if (this.photoSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalPhotoCache), 0, this.photoSize, Theme.key_statisticChartLine_lightblue));
            }
            if (this.videoSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalVideoCache), 1, this.videoSize, Theme.key_statisticChartLine_blue));
            }
            if (this.documentsSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalDocumentCache), 2, this.documentsSize, Theme.key_statisticChartLine_green));
            }
            if (this.musicSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalMusicCache), 3, this.musicSize, Theme.key_statisticChartLine_purple));
            }
            if (this.audioSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalAudioCache), 4, this.audioSize, Theme.key_statisticChartLine_lightgreen));
            }
            if (this.storiesSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalStoriesCache), 5, this.storiesSize, Theme.key_statisticChartLine_red));
            }
            if (this.stickersCacheSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalStickersCache), 6, this.stickersCacheSize, Theme.key_statisticChartLine_orange));
            }
            if (this.cacheSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalProfilePhotosCache), 7, this.cacheSize, Theme.key_statisticChartLine_cyan));
            }
            if (this.cacheTempSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalMiscellaneousCache), 8, this.cacheTempSize, Theme.key_statisticChartLine_purple));
            }
            if (this.logsSize > 0) {
                arrayList3.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalLogsCache), 9, this.logsSize, Theme.key_statisticChartLine_golden));
            }
            if (arrayList3.isEmpty()) {
                z2 = false;
                if (z2) {
                    this.sectionsEndRow = -1;
                } else {
                    this.sectionsEndRow = this.itemInners.size();
                    this.itemInners.add(new ItemInner(13, null, null));
                    this.itemInners.add(ItemInner.asInfo(LocaleController.getString(R.string.StorageUsageInfo)));
                }
                this.itemInners.add(new ItemInner(3, LocaleController.getString(R.string.AutoDeleteCachedMedia), null));
                this.itemInners.add(new ItemInner(7, 0));
                this.itemInners.add(new ItemInner(7, 1));
                this.itemInners.add(new ItemInner(7, 2));
                this.itemInners.add(new ItemInner(7, 3));
                this.itemInners.add(ItemInner.asInfo(LocaleController.getString(R.string.KeepMediaInfoPart)));
                if (this.totalDeviceSize > 0) {
                    this.itemInners.add(new ItemInner(3, LocaleController.getString(R.string.MaxCacheSize), null));
                    this.itemInners.add(new ItemInner(14));
                    this.itemInners.add(ItemInner.asInfo(LocaleController.getString(R.string.MaxCacheSizeInfo)));
                }
                if (z2 && (cacheModel = this.cacheModel) != null && !cacheModel.isEmpty()) {
                    this.itemInners.add(new ItemInner(8, null, null));
                }
                listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    if (z3) {
                        listAdapter.setItems(this.oldItems, this.itemInners);
                    } else {
                        listAdapter.notifyDataSetChanged();
                    }
                }
                cachedMediaLayout = this.cachedMediaLayout;
                if (cachedMediaLayout == null) {
                    cachedMediaLayout.update();
                    return;
                }
                return;
            }
            Collections.sort(arrayList3, new Comparator() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$updateRows$10;
                    lambda$updateRows$10 = CacheControlActivity.lambda$updateRows$10((CacheControlActivity.ItemInner) obj, (CacheControlActivity.ItemInner) obj2);
                    return lambda$updateRows$10;
                }
            });
            ((ItemInner) arrayList3.get(arrayList3.size() - 1)).last = true;
            if (this.tempSizes == null) {
                this.tempSizes = new float[11];
            }
            int i = 0;
            while (true) {
                fArr = this.tempSizes;
                if (i >= fArr.length) {
                    break;
                }
                fArr[i] = (float) size(i);
                i++;
            }
            if (this.percents == null) {
                this.percents = new int[11];
            }
            AndroidUtilities.roundPercents(fArr, this.percents);
            if (arrayList3.size() > 5) {
                this.itemInners.addAll(arrayList3.subList(0, 4));
                int i2 = 0;
                long j = 0;
                for (int i3 = 4; i3 < arrayList3.size(); i3++) {
                    ((ItemInner) arrayList3.get(i3)).pad = true;
                    j += ((ItemInner) arrayList3.get(i3)).size;
                    i2 += this.percents[((ItemInner) arrayList3.get(i3)).index];
                }
                this.percents[10] = i2;
                this.itemInners.add(ItemInner.asCheckBox(LocaleController.getString(R.string.LocalOther), -1, j, Theme.key_statisticChartLine_golden));
                if (!this.collapsed) {
                    arrayList = this.itemInners;
                    arrayList2 = arrayList3.subList(4, arrayList3.size());
                }
            } else {
                arrayList = this.itemInners;
                arrayList2 = arrayList3;
            }
            arrayList.addAll(arrayList2);
        }
        z2 = true;
        if (z2) {
        }
        this.itemInners.add(new ItemInner(3, LocaleController.getString(R.string.AutoDeleteCachedMedia), null));
        this.itemInners.add(new ItemInner(7, 0));
        this.itemInners.add(new ItemInner(7, 1));
        this.itemInners.add(new ItemInner(7, 2));
        this.itemInners.add(new ItemInner(7, 3));
        this.itemInners.add(ItemInner.asInfo(LocaleController.getString(R.string.KeepMediaInfoPart)));
        if (this.totalDeviceSize > 0) {
        }
        if (z2) {
            this.itemInners.add(new ItemInner(8, null, null));
        }
        listAdapter = this.listAdapter;
        if (listAdapter != null) {
        }
        cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout == null) {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        TextView textView;
        float f;
        float f2;
        int i;
        float f3;
        int i2;
        this.actionBar.setBackgroundDrawable(null);
        this.actionBar.setCastShadows(false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.setOccupyStatusBar(true);
        ActionBar actionBar = this.actionBar;
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        actionBar.setTitleColor(ColorUtils.setAlphaComponent(Theme.getColor(i3), 0));
        this.actionBar.setItemsColor(Theme.getColor(i3), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_listSelector), false);
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setTitle(LocaleController.getString(R.string.StorageUsage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.CacheControlActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                if (i4 != -1) {
                    if (i4 == 1) {
                        CacheControlActivity.this.clearSelectedFiles();
                        return;
                    } else if (i4 == 3) {
                        CacheControlActivity.this.clearDatabase(false);
                        return;
                    } else {
                        if (i4 == 4) {
                            CacheControlActivity.this.clearDatabase(true);
                            return;
                        }
                        return;
                    }
                }
                if (!((BaseFragment) CacheControlActivity.this).actionBar.isActionModeShowed()) {
                    CacheControlActivity.this.lambda$onBackPressed$319();
                    return;
                }
                CacheModel cacheModel = CacheControlActivity.this.cacheModel;
                if (cacheModel != null) {
                    cacheModel.clearSelection();
                }
                if (CacheControlActivity.this.cachedMediaLayout != null) {
                    CacheControlActivity.this.cachedMediaLayout.showActionMode(false);
                    CacheControlActivity.this.cachedMediaLayout.updateVisibleRows();
                }
            }
        });
        this.actionMode = this.actionBar.createActionMode();
        FrameLayout frameLayout = new FrameLayout(context);
        this.actionMode.addView(frameLayout, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, true);
        this.actionModeTitle = animatedTextView;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        animatedTextView.setAnimationProperties(0.35f, 0L, 350L, cubicBezierInterpolator);
        this.actionModeTitle.setTextSize(AndroidUtilities.dp(18.0f));
        this.actionModeTitle.setTypeface(AndroidUtilities.bold());
        this.actionModeTitle.setTextColor(Theme.getColor(i3));
        frameLayout.addView(this.actionModeTitle, LayoutHelper.createFrame(-1, 18.0f, 19, 0.0f, -11.0f, 18.0f, 0.0f));
        AnimatedTextView animatedTextView2 = new AnimatedTextView(context, true, true, true);
        this.actionModeSubtitle = animatedTextView2;
        animatedTextView2.setAnimationProperties(0.35f, 0L, 350L, cubicBezierInterpolator);
        this.actionModeSubtitle.setTextSize(AndroidUtilities.dp(14.0f));
        this.actionModeSubtitle.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        frameLayout.addView(this.actionModeSubtitle, LayoutHelper.createFrame(-1, 18.0f, 19, 0.0f, 10.0f, 18.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.actionModeClearButton = textView2;
        textView2.setTextSize(1, 14.0f);
        this.actionModeClearButton.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
        this.actionModeClearButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        this.actionModeClearButton.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 6.0f));
        this.actionModeClearButton.setTypeface(AndroidUtilities.bold());
        this.actionModeClearButton.setGravity(17);
        this.actionModeClearButton.setText(LocaleController.getString(R.string.CacheClear));
        this.actionModeClearButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CacheControlActivity.this.lambda$createView$17(view);
            }
        });
        if (LocaleController.isRTL) {
            textView = this.actionModeClearButton;
            f = 0.0f;
            f2 = 0.0f;
            i = -2;
            f3 = 28.0f;
            i2 = 19;
        } else {
            textView = this.actionModeClearButton;
            f = 14.0f;
            f2 = 0.0f;
            i = -2;
            f3 = 28.0f;
            i2 = 21;
        }
        frameLayout.addView(textView, LayoutHelper.createFrame(i, f3, i2, 0.0f, 0.0f, f, f2));
        ActionBarMenuItem addItem = this.actionBar.createMenu().addItem(2, R.drawable.ic_ab_other);
        int i4 = R.drawable.msg_delete;
        ActionBarMenuSubItem addSubItem = addItem.addSubItem(3, i4, LocaleController.getString(R.string.ClearLocalDatabase));
        this.clearDatabaseItem = addSubItem;
        int i5 = Theme.key_text_RedRegular;
        addSubItem.setIconColor(Theme.getColor(i5));
        ActionBarMenuSubItem actionBarMenuSubItem = this.clearDatabaseItem;
        int i6 = Theme.key_text_RedBold;
        actionBarMenuSubItem.setTextColor(Theme.getColor(i6));
        this.clearDatabaseItem.setSelectorColor(Theme.multAlpha(Theme.getColor(i5), 0.12f));
        if (BuildVars.DEBUG_PRIVATE_VERSION) {
            ActionBarMenuSubItem addSubItem2 = addItem.addSubItem(4, i4, "Full Reset Database");
            this.resetDatabaseItem = addSubItem2;
            addSubItem2.setIconColor(Theme.getColor(i5));
            this.resetDatabaseItem.setTextColor(Theme.getColor(i6));
            this.resetDatabaseItem.setSelectorColor(Theme.multAlpha(Theme.getColor(i5), 0.12f));
        }
        updateDatabaseItemSize();
        this.listAdapter = new ListAdapter(context);
        NestedSizeNotifierLayout nestedSizeNotifierLayout = new NestedSizeNotifierLayout(context) { // from class: org.telegram.ui.CacheControlActivity.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:9:0x004b  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                boolean z = !isPinnedToTop();
                if (z || CacheControlActivity.this.actionBarShadowAlpha == 0.0f) {
                    if (z && CacheControlActivity.this.actionBarShadowAlpha != 1.0f) {
                        CacheControlActivity.access$516(CacheControlActivity.this, 0.16f);
                    }
                    CacheControlActivity cacheControlActivity = CacheControlActivity.this;
                    cacheControlActivity.actionBarShadowAlpha = Utilities.clamp(cacheControlActivity.actionBarShadowAlpha, 1.0f, 0.0f);
                    if (((BaseFragment) CacheControlActivity.this).parentLayout == null) {
                        ((BaseFragment) CacheControlActivity.this).parentLayout.drawHeaderShadow(canvas, (int) (CacheControlActivity.this.actionBarShownT * 255.0f * CacheControlActivity.this.actionBarShadowAlpha), AndroidUtilities.statusBarHeight + ActionBar.getCurrentActionBarHeight());
                        return;
                    }
                    return;
                }
                CacheControlActivity.access$524(CacheControlActivity.this, 0.16f);
                invalidate();
                CacheControlActivity cacheControlActivity2 = CacheControlActivity.this;
                cacheControlActivity2.actionBarShadowAlpha = Utilities.clamp(cacheControlActivity2.actionBarShadowAlpha, 1.0f, 0.0f);
                if (((BaseFragment) CacheControlActivity.this).parentLayout == null) {
                }
            }
        };
        this.nestedSizeNotifierLayout = nestedSizeNotifierLayout;
        this.fragmentView = nestedSizeNotifierLayout;
        nestedSizeNotifierLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.CacheControlActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean allowSelectChildAtPosition(View view) {
                return view != CacheControlActivity.this.cacheChart;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (CacheControlActivity.this.sectionsStartRow >= 0 && CacheControlActivity.this.sectionsEndRow >= 0) {
                    drawSectionBackgroundExclusive(canvas, CacheControlActivity.this.sectionsStartRow - 1, CacheControlActivity.this.sectionsEndRow, Theme.getColor(Theme.key_windowBackgroundWhite));
                }
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setPadding(0, AndroidUtilities.statusBarHeight + (ActionBar.getCurrentActionBarHeight() / 2), 0, 0);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        nestedSizeNotifierLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.CacheControlActivity.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                CacheControlActivity.this.listView.invalidate();
            }
        };
        defaultItemAnimator.setDurations(350L);
        defaultItemAnimator.setInterpolator(cubicBezierInterpolator);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i7) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i7);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i7, float f4, float f5) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i7, f4, f5);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i7, float f4, float f5) {
                CacheControlActivity.this.lambda$createView$19(view, i7, f4, f5);
            }
        });
        this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.CacheControlActivity.5
            boolean pinned;

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i7, int i8) {
                super.onScrolled(recyclerView, i7, i8);
                CacheControlActivity cacheControlActivity = CacheControlActivity.this;
                cacheControlActivity.updateActionBar(cacheControlActivity.layoutManager.findFirstVisibleItemPosition() > 0 || ((BaseFragment) CacheControlActivity.this).actionBar.isActionModeShowed());
                if (this.pinned != CacheControlActivity.this.nestedSizeNotifierLayout.isPinnedToTop()) {
                    this.pinned = CacheControlActivity.this.nestedSizeNotifierLayout.isPinnedToTop();
                    CacheControlActivity.this.nestedSizeNotifierLayout.invalidate();
                }
            }
        });
        nestedSizeNotifierLayout.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        UndoView undoView = new UndoView(context);
        this.cacheRemovedTooltip = undoView;
        nestedSizeNotifierLayout.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        this.nestedSizeNotifierLayout.setTargetListView(this.listView);
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didClearDatabase) {
            try {
                AlertDialog alertDialog = this.progressDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.progressDialog = null;
            if (this.listAdapter != null) {
                this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
                this.updateDatabaseSize = true;
                updateDatabaseItemSize();
                updateRows();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillDialogsEntitiesRecursive(File file, int i, LongSparseArray longSparseArray, CacheModel cacheModel) {
        File[] listFiles;
        int i2;
        long j;
        if (file == null || (listFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : listFiles) {
            if (canceled) {
                return;
            }
            if (file2.isDirectory()) {
                fillDialogsEntitiesRecursive(file2, i, longSparseArray, cacheModel);
            } else if (!file2.getName().equals(".nomedia")) {
                FilePathDatabase.FileMeta fileDialogId = getFileLoader().getFileDatabase().getFileDialogId(file2, null);
                String lowerCase = file2.getName().toLowerCase();
                int i3 = (lowerCase.endsWith(".mp3") || lowerCase.endsWith(".m4a")) ? 3 : i;
                CacheModel.FileInfo fileInfo = new CacheModel.FileInfo(file2);
                long length = file2.length();
                fileInfo.size = length;
                int i4 = i3;
                if (fileDialogId != null) {
                    fileInfo.dialogId = fileDialogId.dialogId;
                    fileInfo.messageId = fileDialogId.messageId;
                    int i5 = fileDialogId.messageType;
                    fileInfo.messageType = i5;
                    if (i5 == 23 && length > 0) {
                        i2 = 7;
                        fileInfo.type = i2;
                        j = fileInfo.dialogId;
                        if (j != 0) {
                            DialogFileEntities dialogFileEntities = (DialogFileEntities) longSparseArray.get(j, null);
                            if (dialogFileEntities == null) {
                                dialogFileEntities = new DialogFileEntities(fileInfo.dialogId);
                                longSparseArray.put(fileInfo.dialogId, dialogFileEntities);
                            }
                            dialogFileEntities.addFile(fileInfo, i2);
                        }
                        if (cacheModel != null && i2 != 6) {
                            cacheModel.add(i2, fileInfo);
                        }
                    }
                }
                i2 = i4;
                fileInfo.type = i2;
                j = fileInfo.dialogId;
                if (j != 0) {
                }
                if (cacheModel != null) {
                    cacheModel.add(i2, fileInfo);
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                CacheControlActivity.this.lambda$getThemeDescriptions$25();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, SlideChooseView.class, StorageUsageView.class, HeaderCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_windowBackgroundWhiteValueText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StorageUsageView.class}, new String[]{"paintFill"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progressBackground));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StorageUsageView.class}, new String[]{"paintProgress"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progress));
        int i5 = Theme.key_windowBackgroundWhiteGrayText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StorageUsageView.class}, new String[]{"telegramCacheTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StorageUsageView.class}, new String[]{"freeSizeTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StorageUsageView.class}, new String[]{"calculationgTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{StorageDiagramView.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription((View) null, 0, new Class[]{TextCheckBoxCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_dialogBackground));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_blue));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_green));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_red));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_golden));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_lightblue));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_lightgreen));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_orange));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, Theme.key_statisticChartLine_indigo));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return !this.changeStatusBar ? super.isLightStatusBar() : AndroidUtilities.computePerceivedBrightness(Theme.getColor(Theme.key_windowBackgroundGray)) > 0.721f;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        CachedMediaLayout cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout == null || motionEvent == null) {
            return true;
        }
        Rect rect = AndroidUtilities.rectTmp2;
        cachedMediaLayout.getHitRect(rect);
        if (rect.contains((int) motionEvent.getX(), ((int) motionEvent.getY()) - this.actionBar.getMeasuredHeight())) {
            return this.cachedMediaLayout.viewPagerFixed.isCurrentTabFirst();
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean needDelayOpenAnimation() {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        CacheModel cacheModel = this.cacheModel;
        if (cacheModel == null || cacheModel.selectedFiles.isEmpty()) {
            return super.onBackPressed();
        }
        this.cacheModel.clearSelection();
        CachedMediaLayout cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout != null) {
            cachedMediaLayout.showActionMode(false);
            this.cachedMediaLayout.updateVisibleRows();
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        canceled = false;
        getNotificationCenter().addObserver(this, NotificationCenter.didClearDatabase);
        this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
        this.loadingDialogs = true;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.CacheControlActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CacheControlActivity.this.lambda$onFragmentCreate$5();
            }
        });
        this.fragmentCreateTime = System.currentTimeMillis();
        updateRows(false);
        updateChart();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.didClearDatabase);
        try {
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        } catch (Exception unused) {
        }
        this.progressDialog = null;
        canceled = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        FilesMigrationService.FilesMigrationBottomSheet filesMigrationBottomSheet;
        if (i == 4) {
            for (int i2 : iArr) {
                if (i2 != 0) {
                    return;
                }
            }
            if (Build.VERSION.SDK_INT < 30 || (filesMigrationBottomSheet = FilesMigrationService.filesMigrationBottomSheet) == null) {
                return;
            }
            filesMigrationBottomSheet.migrateOldFolder();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        if (f > 0.5f && !this.changeStatusBar) {
            this.changeStatusBar = true;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, new Object[0]);
        }
        super.onTransitionAnimationProgress(z, f);
    }

    public void setCacheModel(CacheModel cacheModel) {
        this.cacheModel = cacheModel;
        CachedMediaLayout cachedMediaLayout = this.cachedMediaLayout;
        if (cachedMediaLayout != null) {
            cachedMediaLayout.setCacheModel(cacheModel);
        }
    }
}
