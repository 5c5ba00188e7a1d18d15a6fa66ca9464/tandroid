package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.URLSpanNoUnderline;
/* loaded from: classes3.dex */
public class AboutLinkCell extends FrameLayout {
    private static final int COLLAPSED_HEIGHT = AndroidUtilities.dp(76.0f);
    private static final int MOST_SPEC = View.MeasureSpec.makeMeasureSpec(999999, Integer.MIN_VALUE);
    private FrameLayout bottomShadow;
    private ValueAnimator collapseAnimator;
    private FrameLayout container;
    private boolean expanded;
    private StaticLayout firstThreeLinesLayout;
    private LinkSpanDrawable.LinkCollector links;
    private boolean moreButtonDisabled;
    private Point[] nextLinesLayoutsPositions;
    private String oldText;
    private BaseFragment parentFragment;
    private LinkSpanDrawable pressedLink;
    private Theme.ResourcesProvider resourcesProvider;
    private Drawable rippleBackground;
    private Drawable showMoreBackgroundDrawable;
    private FrameLayout showMoreTextBackgroundView;
    private TextView showMoreTextView;
    private SpannableStringBuilder stringBuilder;
    private StaticLayout textLayout;
    private int textX;
    private int textY;
    private TextView valueTextView;
    private StaticLayout[] nextLinesLayouts = null;
    private int lastInlineLine = -1;
    private boolean needSpace = false;
    private Paint backgroundPaint = new Paint();
    final float SPACE = AndroidUtilities.dp(3.0f);
    Runnable longPressedRunnable = new AnonymousClass3();
    private float expandT = 0.0f;
    private int lastMaxWidth = 0;
    private boolean shouldExpand = false;

    protected void didExtend() {
    }

    protected void didPressUrl(String str) {
    }

    protected void didResizeEnd() {
    }

    protected void didResizeStart() {
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        return false;
    }

    public AboutLinkCell(Context context, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        new Point();
        new LinkPath(true);
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = baseFragment;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
        this.container = anonymousClass1;
        anonymousClass1.setImportantForAccessibility(2);
        this.links = new LinkSpanDrawable.LinkCollector(this.container);
        this.container.setClickable(true);
        this.rippleBackground = Theme.createRadSelectorDrawable(Theme.getColor("listSelectorSDK21", resourcesProvider), 0, 0);
        TextView textView = new TextView(context);
        this.valueTextView = textView;
        textView.setVisibility(8);
        this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2", resourcesProvider));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        int i = 5;
        this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.valueTextView.setImportantForAccessibility(2);
        this.valueTextView.setFocusable(false);
        this.container.addView(this.valueTextView, LayoutHelper.createFrame(-2, -2.0f, (!LocaleController.isRTL ? 3 : i) | 80, 23.0f, 0.0f, 23.0f, 10.0f));
        this.bottomShadow = new FrameLayout(context);
        Drawable mutate = context.getResources().getDrawable(2131165431).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhite", resourcesProvider), PorterDuff.Mode.SRC_ATOP));
        this.bottomShadow.setBackground(mutate);
        addView(this.bottomShadow, LayoutHelper.createFrame(-1, 12.0f, 87, 0.0f, 0.0f, 0.0f, 0.0f));
        addView(this.container, LayoutHelper.createFrame(-1, -1, 55));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.showMoreTextView = anonymousClass2;
        anonymousClass2.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText", resourcesProvider));
        this.showMoreTextView.setTextSize(1, 16.0f);
        this.showMoreTextView.setLines(1);
        this.showMoreTextView.setMaxLines(1);
        this.showMoreTextView.setSingleLine(true);
        this.showMoreTextView.setText(LocaleController.getString("DescriptionMore", 2131625461));
        this.showMoreTextView.setOnClickListener(new AboutLinkCell$$ExternalSyntheticLambda1(this));
        this.showMoreTextView.setPadding(AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(2.0f), 0);
        this.showMoreTextBackgroundView = new FrameLayout(context);
        Drawable mutate2 = context.getResources().getDrawable(2131165432).mutate();
        this.showMoreBackgroundDrawable = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhite", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        this.showMoreTextBackgroundView.setBackground(this.showMoreBackgroundDrawable);
        FrameLayout frameLayout = this.showMoreTextBackgroundView;
        frameLayout.setPadding(frameLayout.getPaddingLeft() + AndroidUtilities.dp(4.0f), AndroidUtilities.dp(1.0f), 0, AndroidUtilities.dp(3.0f));
        this.showMoreTextBackgroundView.addView(this.showMoreTextView, LayoutHelper.createFrame(-2, -2.0f));
        FrameLayout frameLayout2 = this.showMoreTextBackgroundView;
        addView(frameLayout2, LayoutHelper.createFrame(-2, -2.0f, 85, 22.0f - (frameLayout2.getPaddingLeft() / AndroidUtilities.density), 0.0f, 22.0f - (this.showMoreTextBackgroundView.getPaddingRight() / AndroidUtilities.density), 6.0f));
        this.backgroundPaint.setColor(Theme.getColor("windowBackgroundWhite", resourcesProvider));
        setWillNotDraw(false);
    }

    /* renamed from: org.telegram.ui.Cells.AboutLinkCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            AboutLinkCell.this = r1;
        }

        /* JADX WARN: Code restructure failed: missing block: B:50:0x011b, code lost:
            if (r1.checkTouchTextLayout(r1.textLayout, org.telegram.ui.Cells.AboutLinkCell.this.textX, org.telegram.ui.Cells.AboutLinkCell.this.textY, r0, r7) != false) goto L51;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z;
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (AboutLinkCell.this.textLayout != null || AboutLinkCell.this.nextLinesLayouts != null) {
                if (motionEvent.getAction() == 0 || (AboutLinkCell.this.pressedLink != null && motionEvent.getAction() == 1)) {
                    if (x >= AboutLinkCell.this.showMoreTextView.getLeft() && x <= AboutLinkCell.this.showMoreTextView.getRight() && y >= AboutLinkCell.this.showMoreTextView.getTop() && y <= AboutLinkCell.this.showMoreTextView.getBottom()) {
                        return super.onTouchEvent(motionEvent);
                    }
                    if (getMeasuredWidth() > 0 && x > getMeasuredWidth() - AndroidUtilities.dp(23.0f)) {
                        return super.onTouchEvent(motionEvent);
                    }
                    if (motionEvent.getAction() == 0) {
                        if (AboutLinkCell.this.firstThreeLinesLayout != null && AboutLinkCell.this.expandT < 1.0f && AboutLinkCell.this.shouldExpand) {
                            AboutLinkCell aboutLinkCell = AboutLinkCell.this;
                            if (!aboutLinkCell.checkTouchTextLayout(aboutLinkCell.firstThreeLinesLayout, AboutLinkCell.this.textX, AboutLinkCell.this.textY, x, y)) {
                                if (AboutLinkCell.this.nextLinesLayouts != null) {
                                    for (int i = 0; i < AboutLinkCell.this.nextLinesLayouts.length; i++) {
                                        AboutLinkCell aboutLinkCell2 = AboutLinkCell.this;
                                        if (!aboutLinkCell2.checkTouchTextLayout(aboutLinkCell2.nextLinesLayouts[i], AboutLinkCell.this.nextLinesLayoutsPositions[i].x, AboutLinkCell.this.nextLinesLayoutsPositions[i].y, x, y)) {
                                        }
                                    }
                                }
                                z = false;
                            }
                            z = true;
                            break;
                        }
                        AboutLinkCell aboutLinkCell3 = AboutLinkCell.this;
                        if (!z) {
                            AboutLinkCell.this.resetPressedLink();
                        }
                    } else if (AboutLinkCell.this.pressedLink != null) {
                        try {
                            AboutLinkCell aboutLinkCell4 = AboutLinkCell.this;
                            aboutLinkCell4.onLinkClick((ClickableSpan) aboutLinkCell4.pressedLink.getSpan());
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        AboutLinkCell.this.resetPressedLink();
                        z = true;
                    }
                    return !z || super.onTouchEvent(motionEvent);
                } else if (motionEvent.getAction() == 3) {
                    AboutLinkCell.this.resetPressedLink();
                }
            }
            z = false;
            if (!z) {
            }
        }
    }

    /* renamed from: org.telegram.ui.Cells.AboutLinkCell$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends TextView {
        private boolean pressed = false;

        AnonymousClass2(AboutLinkCell aboutLinkCell, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z = this.pressed;
            if (motionEvent.getAction() == 0) {
                this.pressed = true;
            } else if (motionEvent.getAction() != 2) {
                this.pressed = false;
            }
            if (z != this.pressed) {
                invalidate();
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.pressed) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_urlPaint);
            }
            super.onDraw(canvas);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        updateCollapse(true, true);
    }

    private void setShowMoreMarginBottom(int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.showMoreTextBackgroundView.getLayoutParams();
        if (layoutParams.bottomMargin != i) {
            layoutParams.bottomMargin = i;
            this.showMoreTextBackgroundView.setLayoutParams(layoutParams);
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        View view = (View) getParent();
        float pow = view == null ? 1.0f : (float) Math.pow(view.getAlpha(), 2.0d);
        drawText(canvas);
        float alpha = this.showMoreTextBackgroundView.getAlpha();
        if (alpha > 0.0f) {
            canvas.save();
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (alpha * 255.0f), 31);
            this.showMoreBackgroundDrawable.setAlpha((int) (pow * 255.0f));
            canvas.translate(this.showMoreTextBackgroundView.getLeft(), this.showMoreTextBackgroundView.getTop());
            this.showMoreTextBackgroundView.draw(canvas);
            canvas.restore();
        }
        float alpha2 = this.bottomShadow.getAlpha();
        if (alpha2 > 0.0f) {
            canvas.save();
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (alpha2 * 255.0f), 31);
            canvas.translate(this.bottomShadow.getLeft(), this.bottomShadow.getTop());
            this.bottomShadow.draw(canvas);
            canvas.restore();
        }
        this.container.draw(canvas);
        super.draw(canvas);
    }

    private void drawText(Canvas canvas) {
        StaticLayout staticLayout;
        int i;
        StaticLayout staticLayout2;
        int i2;
        canvas.save();
        canvas.clipRect(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(8.0f), getWidth() - AndroidUtilities.dp(23.0f), getHeight());
        int dp = AndroidUtilities.dp(23.0f);
        this.textX = dp;
        float f = 0.0f;
        canvas.translate(dp, 0.0f);
        LinkSpanDrawable.LinkCollector linkCollector = this.links;
        if (linkCollector != null && linkCollector.draw(canvas)) {
            invalidate();
        }
        int dp2 = AndroidUtilities.dp(8.0f);
        this.textY = dp2;
        canvas.translate(0.0f, dp2);
        try {
            staticLayout = this.firstThreeLinesLayout;
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (staticLayout != null && this.shouldExpand) {
            staticLayout.draw(canvas);
            int lineCount = this.firstThreeLinesLayout.getLineCount() - 1;
            float lineTop = this.firstThreeLinesLayout.getLineTop(lineCount) + this.firstThreeLinesLayout.getTopPadding();
            float lineRight = this.firstThreeLinesLayout.getLineRight(lineCount) + (this.needSpace ? this.SPACE : 0.0f);
            float lineBottom = (this.firstThreeLinesLayout.getLineBottom(lineCount) - this.firstThreeLinesLayout.getLineTop(lineCount)) - this.firstThreeLinesLayout.getBottomPadding();
            float easeInOutCubic = easeInOutCubic(1.0f - ((float) Math.pow(this.expandT, 0.25d)));
            if (this.nextLinesLayouts != null) {
                float f2 = lineRight;
                int i3 = 0;
                while (true) {
                    StaticLayout[] staticLayoutArr = this.nextLinesLayouts;
                    if (i3 >= staticLayoutArr.length) {
                        break;
                    }
                    StaticLayout staticLayout3 = staticLayoutArr[i3];
                    if (staticLayout3 != null) {
                        int save = canvas.save();
                        Point[] pointArr = this.nextLinesLayoutsPositions;
                        if (pointArr[i3] != null) {
                            pointArr[i3].set((int) (this.textX + (f2 * easeInOutCubic)), (int) (this.textY + lineTop + ((1.0f - easeInOutCubic) * lineBottom)));
                        }
                        int i4 = this.lastInlineLine;
                        if (i4 != -1 && i4 <= i3) {
                            canvas.translate(f, lineTop + lineBottom);
                            i2 = save;
                            staticLayout2 = staticLayout3;
                            i = i3;
                            canvas.saveLayerAlpha(0.0f, 0.0f, staticLayout3.getWidth(), staticLayout3.getHeight(), (int) (this.expandT * 255.0f), 31);
                        } else {
                            i2 = save;
                            staticLayout2 = staticLayout3;
                            i = i3;
                            canvas.translate(f2 * easeInOutCubic, ((1.0f - easeInOutCubic) * lineBottom) + lineTop);
                        }
                        StaticLayout staticLayout4 = staticLayout2;
                        staticLayout4.draw(canvas);
                        canvas.restoreToCount(i2);
                        f2 += staticLayout4.getLineRight(0) + this.SPACE;
                        lineBottom += staticLayout4.getLineBottom(0) + staticLayout4.getTopPadding();
                    } else {
                        i = i3;
                    }
                    i3 = i + 1;
                    f = 0.0f;
                }
            }
            canvas.restore();
        }
        StaticLayout staticLayout5 = this.textLayout;
        if (staticLayout5 != null) {
            staticLayout5.draw(canvas);
        }
        canvas.restore();
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.container.setOnClickListener(onClickListener);
    }

    public void resetPressedLink() {
        this.links.clear();
        this.pressedLink = null;
        AndroidUtilities.cancelRunOnUIThread(this.longPressedRunnable);
        invalidate();
    }

    public void setText(String str, boolean z) {
        setTextAndValue(str, null, z);
    }

    public void setTextAndValue(String str, String str2, boolean z) {
        if (TextUtils.isEmpty(str) || TextUtils.equals(str, this.oldText)) {
            return;
        }
        try {
            this.oldText = AndroidUtilities.getSafeString(str);
        } catch (Throwable unused) {
            this.oldText = str;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.oldText);
        this.stringBuilder = spannableStringBuilder;
        MessageObject.addLinks(false, spannableStringBuilder, false, false, !z);
        Emoji.replaceEmoji(this.stringBuilder, Theme.profile_aboutTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
        if (this.lastMaxWidth <= 0) {
            this.lastMaxWidth = AndroidUtilities.displaySize.x - AndroidUtilities.dp(46.0f);
        }
        checkTextLayout(this.lastMaxWidth, true);
        updateHeight();
        int visibility = this.valueTextView.getVisibility();
        if (TextUtils.isEmpty(str2)) {
            this.valueTextView.setVisibility(8);
        } else {
            this.valueTextView.setText(str2);
            this.valueTextView.setVisibility(0);
        }
        if (visibility != this.valueTextView.getVisibility()) {
            checkTextLayout(this.lastMaxWidth, true);
        }
        requestLayout();
    }

    /* renamed from: org.telegram.ui.Cells.AboutLinkCell$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
            AboutLinkCell.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AboutLinkCell.this.pressedLink != null) {
                String url = AboutLinkCell.this.pressedLink.getSpan() instanceof URLSpanNoUnderline ? ((URLSpanNoUnderline) AboutLinkCell.this.pressedLink.getSpan()).getURL() : AboutLinkCell.this.pressedLink.getSpan() instanceof URLSpan ? ((URLSpan) AboutLinkCell.this.pressedLink.getSpan()).getURL() : AboutLinkCell.this.pressedLink.getSpan().toString();
                try {
                    AboutLinkCell.this.performHapticFeedback(0, 2);
                } catch (Exception unused) {
                }
                BottomSheet.Builder builder = new BottomSheet.Builder(AboutLinkCell.this.parentFragment.getParentActivity());
                builder.setTitle(url);
                builder.setItems(new CharSequence[]{LocaleController.getString("Open", 2131627090), LocaleController.getString("Copy", 2131625256)}, new AboutLinkCell$3$$ExternalSyntheticLambda0(this, (ClickableSpan) AboutLinkCell.this.pressedLink.getSpan(), url));
                builder.setOnPreDismissListener(new AboutLinkCell$3$$ExternalSyntheticLambda1(this));
                builder.show();
                AboutLinkCell.this.pressedLink = null;
            }
        }

        public /* synthetic */ void lambda$run$0(ClickableSpan clickableSpan, String str, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                AboutLinkCell.this.onLinkClick(clickableSpan);
            } else if (i != 1) {
            } else {
                AndroidUtilities.addToClipboard(str);
                if (Build.VERSION.SDK_INT >= 31) {
                    return;
                }
                if (str.startsWith("@")) {
                    BulletinFactory.of(AboutLinkCell.this.parentFragment).createSimpleBulletin(2131558433, LocaleController.getString("UsernameCopied", 2131628846)).show();
                } else if (str.startsWith("#") || str.startsWith("$")) {
                    BulletinFactory.of(AboutLinkCell.this.parentFragment).createSimpleBulletin(2131558433, LocaleController.getString("HashtagCopied", 2131626128)).show();
                } else {
                    BulletinFactory.of(AboutLinkCell.this.parentFragment).createSimpleBulletin(2131558433, LocaleController.getString("LinkCopied", 2131626433)).show();
                }
            }
        }

        public /* synthetic */ void lambda$run$1(DialogInterface dialogInterface) {
            AboutLinkCell.this.resetPressedLink();
        }
    }

    public boolean checkTouchTextLayout(StaticLayout staticLayout, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        try {
            int lineForVertical = staticLayout.getLineForVertical(i6);
            float f = i5;
            int offsetForHorizontal = staticLayout.getOffsetForHorizontal(lineForVertical, f);
            float lineLeft = staticLayout.getLineLeft(lineForVertical);
            if (lineLeft <= f && lineLeft + staticLayout.getLineWidth(lineForVertical) >= f && i6 >= 0 && i6 <= staticLayout.getHeight()) {
                Spannable spannable = (Spannable) staticLayout.getText();
                ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannable.getSpans(offsetForHorizontal, offsetForHorizontal, ClickableSpan.class);
                if (clickableSpanArr.length != 0 && !AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                    resetPressedLink();
                    LinkSpanDrawable linkSpanDrawable = new LinkSpanDrawable(clickableSpanArr[0], this.parentFragment.getResourceProvider(), i3, i4);
                    this.pressedLink = linkSpanDrawable;
                    this.links.addLink(linkSpanDrawable);
                    int spanStart = spannable.getSpanStart(this.pressedLink.getSpan());
                    int spanEnd = spannable.getSpanEnd(this.pressedLink.getSpan());
                    LinkPath obtainNewPath = this.pressedLink.obtainNewPath();
                    obtainNewPath.setCurrentLayout(staticLayout, spanStart, i2);
                    staticLayout.getSelectionPath(spanStart, spanEnd, obtainNewPath);
                    AndroidUtilities.runOnUIThread(this.longPressedRunnable, ViewConfiguration.getLongPressTimeout());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public void onLinkClick(ClickableSpan clickableSpan) {
        if (clickableSpan instanceof URLSpanNoUnderline) {
            String url = ((URLSpanNoUnderline) clickableSpan).getURL();
            if (!url.startsWith("@") && !url.startsWith("#") && !url.startsWith("/")) {
                return;
            }
            didPressUrl(url);
        } else if (clickableSpan instanceof URLSpan) {
            String url2 = ((URLSpan) clickableSpan).getURL();
            if (AndroidUtilities.shouldShowUrlInAlert(url2)) {
                AlertsCreator.showOpenUrlAlert(this.parentFragment, url2, true, true);
            } else {
                Browser.openUrl(getContext(), url2);
            }
        } else {
            clickableSpan.onClick(this);
        }
    }

    /* loaded from: classes3.dex */
    public class SpringInterpolator {
        public float friction;
        public float tension;
        private float position = 0.0f;
        private float velocity = 0.0f;

        public SpringInterpolator(AboutLinkCell aboutLinkCell, float f, float f2) {
            this.tension = f;
            this.friction = f2;
        }

        public float getValue(float f) {
            float min = Math.min(f, 250.0f);
            while (min > 0.0f) {
                float min2 = Math.min(min, 18.0f);
                step(min2);
                min -= min2;
            }
            return this.position;
        }

        private void step(float f) {
            float f2 = this.position;
            float f3 = this.velocity;
            float f4 = f3 + ((((((-this.tension) * 1.0E-6f) * (f2 - 1.0f)) + (((-this.friction) * 0.001f) * f3)) / 1.0f) * f);
            this.velocity = f4;
            this.position = f2 + (f4 * f);
        }
    }

    public void updateCollapse(boolean z, boolean z2) {
        ValueAnimator valueAnimator = this.collapseAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.collapseAnimator = null;
        }
        float f = this.expandT;
        float f2 = z ? 1.0f : 0.0f;
        if (z2) {
            if (f2 > 0.0f) {
                didExtend();
            }
            float textHeight = textHeight();
            float min = Math.min(COLLAPSED_HEIGHT, textHeight);
            Math.abs(AndroidUtilities.lerp(min, textHeight, f2) - AndroidUtilities.lerp(min, textHeight, f));
            this.collapseAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.collapseAnimator.addUpdateListener(new AboutLinkCell$$ExternalSyntheticLambda0(this, new AtomicReference(Float.valueOf(f)), f, f2, new SpringInterpolator(this, 380.0f, 20.17f)));
            this.collapseAnimator.addListener(new AnonymousClass4());
            this.collapseAnimator.setDuration(Math.abs(f - f2) * 1250.0f * 2.0f);
            this.collapseAnimator.start();
            return;
        }
        this.expandT = f2;
        forceLayout();
    }

    public /* synthetic */ void lambda$updateCollapse$1(AtomicReference atomicReference, float f, float f2, SpringInterpolator springInterpolator, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        AndroidUtilities.lerp(f, f2, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        float lerp = AndroidUtilities.lerp(f, f2, springInterpolator.getValue((floatValue - ((Float) atomicReference.getAndSet(Float.valueOf(floatValue))).floatValue()) * 1000.0f * 8.0f));
        this.expandT = lerp;
        if (lerp > 0.8f && this.container.getBackground() == null) {
            this.container.setBackground(this.rippleBackground);
        }
        this.showMoreTextBackgroundView.setAlpha(1.0f - this.expandT);
        this.bottomShadow.setAlpha((float) Math.pow(1.0f - this.expandT, 2.0d));
        updateHeight();
        this.container.invalidate();
    }

    /* renamed from: org.telegram.ui.Cells.AboutLinkCell$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        AnonymousClass4() {
            AboutLinkCell.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AboutLinkCell.this.didResizeEnd();
            if (AboutLinkCell.this.container.getBackground() == null) {
                AboutLinkCell.this.container.setBackground(AboutLinkCell.this.rippleBackground);
            }
            AboutLinkCell.this.expanded = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            AboutLinkCell.this.didResizeStart();
        }
    }

    private int fromHeight() {
        return Math.min(COLLAPSED_HEIGHT + (this.valueTextView.getVisibility() == 0 ? AndroidUtilities.dp(20.0f) : 0), textHeight());
    }

    private int updateHeight() {
        int textHeight = textHeight();
        float fromHeight = fromHeight();
        if (this.shouldExpand) {
            textHeight = (int) AndroidUtilities.lerp(fromHeight, textHeight, this.expandT);
        }
        setHeight(textHeight);
        return textHeight;
    }

    private void setHeight(int i) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
        boolean z = true;
        if (layoutParams == null) {
            if (getMinimumHeight() == 0) {
                getHeight();
            } else {
                getMinimumHeight();
            }
            layoutParams = new RecyclerView.LayoutParams(-1, i);
        } else {
            if (((ViewGroup.MarginLayoutParams) layoutParams).height == i) {
                z = false;
            }
            ((ViewGroup.MarginLayoutParams) layoutParams).height = i;
        }
        if (z) {
            setLayoutParams(layoutParams);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    protected void onMeasure(int i, int i2) {
        checkTextLayout(View.MeasureSpec.getSize(i) - AndroidUtilities.dp(46.0f), false);
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(updateHeight(), 1073741824));
    }

    private StaticLayout makeTextLayout(CharSequence charSequence, int i) {
        if (Build.VERSION.SDK_INT >= 24) {
            return StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), Theme.profile_aboutTextPaint, i).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(LocaleController.isRTL ? StaticLayoutEx.ALIGN_RIGHT() : StaticLayoutEx.ALIGN_LEFT()).build();
        }
        return new StaticLayout(charSequence, Theme.profile_aboutTextPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    private void checkTextLayout(int i, boolean z) {
        int i2 = 0;
        if (this.moreButtonDisabled) {
            this.shouldExpand = false;
        }
        SpannableStringBuilder spannableStringBuilder = this.stringBuilder;
        if (spannableStringBuilder != null && (i != this.lastMaxWidth || z)) {
            StaticLayout makeTextLayout = makeTextLayout(spannableStringBuilder, i);
            this.textLayout = makeTextLayout;
            this.shouldExpand = makeTextLayout.getLineCount() >= 4;
            if (this.textLayout.getLineCount() >= 3 && this.shouldExpand) {
                int max = Math.max(this.textLayout.getLineStart(2), this.textLayout.getLineEnd(2));
                if (this.stringBuilder.charAt(max - 1) == '\n') {
                    max--;
                }
                int i3 = max - 1;
                this.needSpace = (this.stringBuilder.charAt(i3) == ' ' || this.stringBuilder.charAt(i3) == '\n') ? false : true;
                this.firstThreeLinesLayout = makeTextLayout(this.stringBuilder.subSequence(0, max), i);
                this.nextLinesLayouts = new StaticLayout[this.textLayout.getLineCount() - 3];
                this.nextLinesLayoutsPositions = new Point[this.textLayout.getLineCount() - 3];
                float lineRight = this.firstThreeLinesLayout.getLineRight(this.firstThreeLinesLayout.getLineCount() - 1) + (this.needSpace ? this.SPACE : 0.0f);
                this.lastInlineLine = -1;
                if (this.showMoreTextBackgroundView.getMeasuredWidth() <= 0) {
                    FrameLayout frameLayout = this.showMoreTextBackgroundView;
                    int i4 = MOST_SPEC;
                    frameLayout.measure(i4, i4);
                }
                for (int i5 = 3; i5 < this.textLayout.getLineCount(); i5++) {
                    int lineStart = this.textLayout.getLineStart(i5);
                    int lineEnd = this.textLayout.getLineEnd(i5);
                    StaticLayout makeTextLayout2 = makeTextLayout(this.stringBuilder.subSequence(Math.min(lineStart, lineEnd), Math.max(lineStart, lineEnd)), i);
                    int i6 = i5 - 3;
                    this.nextLinesLayouts[i6] = makeTextLayout2;
                    this.nextLinesLayoutsPositions[i6] = new Point();
                    if (this.lastInlineLine == -1 && lineRight > (i - this.showMoreTextBackgroundView.getMeasuredWidth()) + this.showMoreTextBackgroundView.getPaddingLeft()) {
                        this.lastInlineLine = i6;
                    }
                    lineRight += makeTextLayout2.getLineRight(0) + this.SPACE;
                }
                if (lineRight < (i - this.showMoreTextBackgroundView.getMeasuredWidth()) + this.showMoreTextBackgroundView.getPaddingLeft()) {
                    this.shouldExpand = false;
                }
            }
            if (!this.shouldExpand) {
                this.firstThreeLinesLayout = null;
                this.nextLinesLayouts = null;
            }
            this.lastMaxWidth = i;
            this.container.setMinimumHeight(textHeight());
            if (this.shouldExpand && this.firstThreeLinesLayout != null) {
                int fromHeight = fromHeight() - AndroidUtilities.dp(8.0f);
                StaticLayout staticLayout = this.firstThreeLinesLayout;
                setShowMoreMarginBottom((((fromHeight - staticLayout.getLineBottom(staticLayout.getLineCount() - 1)) - this.showMoreTextBackgroundView.getPaddingBottom()) - this.showMoreTextView.getPaddingBottom()) - (this.showMoreTextView.getLayout() == null ? 0 : this.showMoreTextView.getLayout().getHeight() - this.showMoreTextView.getLayout().getLineBottom(this.showMoreTextView.getLineCount() - 1)));
            }
        }
        TextView textView = this.showMoreTextView;
        if (!this.shouldExpand) {
            i2 = 8;
        }
        textView.setVisibility(i2);
        if (!this.shouldExpand && this.container.getBackground() == null) {
            this.container.setBackground(this.rippleBackground);
        }
        if (!this.shouldExpand || this.expandT >= 1.0f || this.container.getBackground() == null) {
            return;
        }
        this.container.setBackground(null);
    }

    private int textHeight() {
        StaticLayout staticLayout = this.textLayout;
        int height = (staticLayout != null ? staticLayout.getHeight() : AndroidUtilities.dp(20.0f)) + AndroidUtilities.dp(16.0f);
        return this.valueTextView.getVisibility() == 0 ? height + AndroidUtilities.dp(23.0f) : height;
    }

    public boolean onClick() {
        if (!this.shouldExpand || this.expandT > 0.0f) {
            return false;
        }
        updateCollapse(true, true);
        return true;
    }

    private float easeInOutCubic(float f) {
        return ((double) f) < 0.5d ? 4.0f * f * f * f : 1.0f - (((float) Math.pow((f * (-2.0f)) + 2.0f, 3.0d)) / 2.0f);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.textLayout != null) {
            SpannableStringBuilder spannableStringBuilder = this.stringBuilder;
            CharSequence text = this.valueTextView.getText();
            accessibilityNodeInfo.setClassName("android.widget.TextView");
            if (TextUtils.isEmpty(text)) {
                accessibilityNodeInfo.setText(spannableStringBuilder);
                return;
            }
            accessibilityNodeInfo.setText(((Object) text) + ": " + ((Object) spannableStringBuilder));
        }
    }

    public void setMoreButtonDisabled(boolean z) {
        this.moreButtonDisabled = z;
    }
}
