package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.SparseIntArray;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.GestureDetectorFixDoubleTap;
import org.telegram.ui.Components.RecyclerListView;

/* loaded from: classes3.dex */
public class RecyclerListView extends RecyclerView {
    private static int[] attributes;
    private static boolean gotAttributes;
    private static final Method initializeScrollbars;
    private View.AccessibilityDelegate accessibilityDelegate;
    private boolean accessibilityEnabled;
    private boolean allowItemsInteractionDuringAnimation;
    private boolean allowStopHeaveOperations;
    private boolean animateEmptyView;
    private Paint backgroundPaint;
    private Runnable clickRunnable;
    private int currentChildPosition;
    private View currentChildView;
    private int currentFirst;
    int currentSelectedPosition;
    private int currentVisible;
    private boolean disableHighlightState;
    private boolean disallowInterceptTouchEvents;
    private boolean drawSelection;
    private boolean drawSelectorBehind;
    private View emptyView;
    int emptyViewAnimateToVisibility;
    private int emptyViewAnimationType;
    private FastScroll fastScroll;
    public boolean fastScrollAnimationRunning;
    private GestureDetectorFixDoubleTap gestureDetector;
    private GenericProvider getSelectorColor;
    private ArrayList headers;
    private ArrayList headersCache;
    private boolean hiddenByEmptyView;
    private boolean hideIfEmpty;
    private int highlightPosition;
    private boolean instantClick;
    private boolean interceptedByChild;
    private boolean isChildViewEnabled;
    private boolean isHidden;
    RecyclerItemsEnterAnimator itemsEnterAnimator;
    private long lastAlphaAnimationTime;
    float lastX;
    float lastY;
    int[] listPaddings;
    private boolean longPressCalled;
    boolean multiSelectionGesture;
    boolean multiSelectionGestureStarted;
    onMultiSelectionChanged multiSelectionListener;
    boolean multiselectScrollRunning;
    boolean multiselectScrollToTop;
    private RecyclerView.AdapterDataObserver observer;
    private OnInterceptTouchListener onInterceptTouchListener;
    private OnItemClickListener onItemClickListener;
    private OnItemClickListenerExtended onItemClickListenerExtended;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemLongClickListenerExtended onItemLongClickListenerExtended;
    private RecyclerView.OnScrollListener onScrollListener;
    private FrameLayout overlayContainer;
    private IntReturnCallback pendingHighlightPosition;
    private View pinnedHeader;
    private float pinnedHeaderShadowAlpha;
    private Drawable pinnedHeaderShadowDrawable;
    private float pinnedHeaderShadowTargetAlpha;
    private Runnable removeHighlighSelectionRunnable;
    private boolean resetSelectorOnChanged;
    protected final Theme.ResourcesProvider resourcesProvider;
    private boolean scrollEnabled;
    public boolean scrolledByUserOnce;
    Runnable scroller;
    public boolean scrollingByUser;
    private int sectionOffset;
    private SectionsAdapter sectionsAdapter;
    private int sectionsCount;
    private int sectionsType;
    private Runnable selectChildRunnable;
    HashSet selectedPositions;
    protected Drawable selectorDrawable;
    protected int selectorPosition;
    private int selectorRadius;
    protected android.graphics.Rect selectorRect;
    protected Consumer selectorTransformer;
    private int selectorType;
    protected View selectorView;
    private boolean selfOnLayout;
    private int startSection;
    int startSelectionFrom;
    private boolean stoppedAllHeavyOperations;
    private int topBottomSelectorRadius;
    private int touchSlop;
    private int translateSelector;
    public boolean useLayoutPositionOnClick;
    boolean useRelativePositions;

    public class FastScroll extends View {
        private int activeColor;
        private Path arrowPath;
        private float bubbleProgress;
        private String currentLetter;
        Drawable fastScrollBackgroundDrawable;
        Drawable fastScrollShadowDrawable;
        private float floatingDateProgress;
        private boolean floatingDateVisible;
        private boolean fromTop;
        private float fromWidth;
        Runnable hideFloatingDateRunnable;
        private StaticLayout inLetterLayout;
        private int inactiveColor;
        boolean isMoving;
        boolean isRtl;
        public boolean isVisible;
        private float lastLetterY;
        private long lastUpdateTime;
        private float lastY;
        private StaticLayout letterLayout;
        private TextPaint letterPaint;
        private StaticLayout oldLetterLayout;
        private StaticLayout outLetterLayout;
        private Paint paint;
        private Paint paint2;
        private Path path;
        private int[] positionWithOffset;
        private boolean pressed;
        private float progress;
        private float[] radii;
        private RectF rect;
        private float replaceLayoutProgress;
        private int scrollX;
        private StaticLayout stableLetterLayout;
        private float startDy;
        long startTime;
        float startY;
        private float textX;
        private float textY;
        public int topOffset;
        float touchSlop;
        private int type;
        public boolean usePadding;
        float viewAlpha;
        float visibilityAlpha;

        public FastScroll(Context context, int i) {
            super(context);
            this.usePadding = true;
            this.rect = new RectF();
            this.paint = new Paint(1);
            this.paint2 = new Paint(1);
            this.replaceLayoutProgress = 1.0f;
            this.letterPaint = new TextPaint(1);
            this.path = new Path();
            this.arrowPath = new Path();
            this.radii = new float[8];
            this.positionWithOffset = new int[2];
            this.hideFloatingDateRunnable = new Runnable() { // from class: org.telegram.ui.Components.RecyclerListView.FastScroll.1
                @Override // java.lang.Runnable
                public void run() {
                    if (FastScroll.this.pressed) {
                        AndroidUtilities.cancelRunOnUIThread(FastScroll.this.hideFloatingDateRunnable);
                        AndroidUtilities.runOnUIThread(FastScroll.this.hideFloatingDateRunnable, 4000L);
                    } else {
                        FastScroll.this.floatingDateVisible = false;
                        FastScroll.this.invalidate();
                    }
                }
            };
            this.viewAlpha = 1.0f;
            this.type = i;
            if (i == 0) {
                this.letterPaint.setTextSize(AndroidUtilities.dp(45.0f));
                this.isRtl = LocaleController.isRTL;
            } else {
                this.isRtl = false;
                this.letterPaint.setTextSize(AndroidUtilities.dp(13.0f));
                this.letterPaint.setTypeface(AndroidUtilities.bold());
                Paint paint = this.paint2;
                int i2 = Theme.key_windowBackgroundWhite;
                paint.setColor(Theme.getColor(i2, RecyclerListView.this.resourcesProvider));
                Drawable mutate = ContextCompat.getDrawable(context, R.drawable.calendar_date).mutate();
                this.fastScrollBackgroundDrawable = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(Theme.getColor(i2, RecyclerListView.this.resourcesProvider), -1, 0.1f), PorterDuff.Mode.MULTIPLY));
            }
            for (int i3 = 0; i3 < 8; i3++) {
                this.radii[i3] = AndroidUtilities.dp(44.0f);
            }
            this.scrollX = AndroidUtilities.dp(this.isRtl ? 10.0f : (i == 0 ? NotificationCenter.httpFileDidFailedLoad : NotificationCenter.themeListUpdated) - 15);
            updateColors();
            setFocusableInTouchMode(true);
            this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.fastScrollShadowDrawable = ContextCompat.getDrawable(context, R.drawable.fast_scroll_shadow);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getCurrentLetter(boolean z) {
            RecyclerView.LayoutManager layoutManager = RecyclerListView.this.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.getOrientation() == 1) {
                    RecyclerView.Adapter adapter = RecyclerListView.this.getAdapter();
                    if (adapter instanceof FastScrollAdapter) {
                        FastScrollAdapter fastScrollAdapter = (FastScrollAdapter) adapter;
                        fastScrollAdapter.getPositionForScrollProgress(RecyclerListView.this, this.progress, this.positionWithOffset);
                        if (z) {
                            int[] iArr = this.positionWithOffset;
                            linearLayoutManager.scrollToPositionWithOffset(iArr[0], (-iArr[1]) + RecyclerListView.this.sectionOffset);
                        }
                        String letter = fastScrollAdapter.getLetter(this.positionWithOffset[0]);
                        if (letter == null) {
                            StaticLayout staticLayout = this.letterLayout;
                            if (staticLayout != null) {
                                this.oldLetterLayout = staticLayout;
                            }
                            this.letterLayout = null;
                            return;
                        }
                        if (letter.equals(this.currentLetter)) {
                            return;
                        }
                        this.currentLetter = letter;
                        if (this.type == 0) {
                            this.letterLayout = new StaticLayout(letter, this.letterPaint, MediaDataController.MAX_STYLE_RUNS_COUNT, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        } else {
                            this.outLetterLayout = this.letterLayout;
                            int measureText = ((int) this.letterPaint.measureText(letter)) + 1;
                            TextPaint textPaint = this.letterPaint;
                            Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                            this.letterLayout = new StaticLayout(letter, textPaint, measureText, alignment, 1.0f, 0.0f, false);
                            if (this.outLetterLayout != null) {
                                String[] split = letter.split(" ");
                                String[] split2 = this.outLetterLayout.getText().toString().split(" ");
                                if (split != null && split2 != null && split.length == 2 && split2.length == 2 && split[1].equals(split2[1])) {
                                    String charSequence = this.outLetterLayout.getText().toString();
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
                                    spannableStringBuilder.setSpan(new EmptyStubSpan(), split2[0].length(), charSequence.length(), 0);
                                    this.outLetterLayout = new StaticLayout(spannableStringBuilder, this.letterPaint, ((int) this.letterPaint.measureText(charSequence)) + 1, alignment, 1.0f, 0.0f, false);
                                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(letter);
                                    spannableStringBuilder2.setSpan(new EmptyStubSpan(), split[0].length(), letter.length(), 0);
                                    this.inLetterLayout = new StaticLayout(spannableStringBuilder2, this.letterPaint, measureText, alignment, 1.0f, 0.0f, false);
                                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(letter);
                                    spannableStringBuilder3.setSpan(new EmptyStubSpan(), 0, split[0].length(), 0);
                                    this.stableLetterLayout = new StaticLayout(spannableStringBuilder3, this.letterPaint, measureText, alignment, 1.0f, 0.0f, false);
                                } else {
                                    this.inLetterLayout = this.letterLayout;
                                    this.stableLetterLayout = null;
                                }
                                this.fromWidth = this.outLetterLayout.getWidth();
                                this.replaceLayoutProgress = 0.0f;
                                this.fromTop = getProgress() > this.lastLetterY;
                            }
                            this.lastLetterY = getProgress();
                        }
                        this.oldLetterLayout = null;
                        if (this.letterLayout.getLineCount() > 0) {
                            this.letterLayout.getLineWidth(0);
                            this.letterLayout.getLineLeft(0);
                            this.textX = (this.isRtl ? AndroidUtilities.dp(10.0f) + ((AndroidUtilities.dp(88.0f) - this.letterLayout.getLineWidth(0)) / 2.0f) : (AndroidUtilities.dp(88.0f) - this.letterLayout.getLineWidth(0)) / 2.0f) - this.letterLayout.getLineLeft(0);
                            this.textY = (AndroidUtilities.dp(88.0f) - this.letterLayout.getHeight()) / 2;
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateColors() {
            TextPaint textPaint;
            int i;
            this.inactiveColor = this.type == 0 ? Theme.getColor(Theme.key_fastScrollInactive, RecyclerListView.this.resourcesProvider) : ColorUtils.setAlphaComponent(-16777216, 102);
            this.activeColor = Theme.getColor(Theme.key_fastScrollActive, RecyclerListView.this.resourcesProvider);
            this.paint.setColor(this.inactiveColor);
            if (this.type == 0) {
                textPaint = this.letterPaint;
                i = Theme.key_fastScrollText;
            } else {
                textPaint = this.letterPaint;
                i = Theme.key_windowBackgroundWhiteBlackText;
            }
            textPaint.setColor(Theme.getColor(i, RecyclerListView.this.resourcesProvider));
            invalidate();
        }

        @Override // android.view.View
        public float getAlpha() {
            return this.viewAlpha;
        }

        public float getProgress() {
            return this.progress;
        }

        public int getScrollBarY() {
            return ((int) Math.ceil((getMeasuredHeight() - AndroidUtilities.dp(54.0f)) * this.progress)) + AndroidUtilities.dp(17.0f);
        }

        @Override // android.view.View
        public boolean isPressed() {
            return this.pressed;
        }

        @Override // android.view.View
        public void layout(int i, int i2, int i3, int i4) {
            if (RecyclerListView.this.selfOnLayout) {
                super.layout(i, i2, i3, i4);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:68:0x01f2, code lost:
        
            if (r15[6] == r8) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x0202, code lost:
        
            if (r15[4] == r8) goto L47;
         */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0206  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x0229  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x0234  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x025d  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x0261  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x0237  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x022f  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x0212  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            float f;
            float dp;
            float dp2;
            StaticLayout staticLayout;
            int paddingTop = (this.usePadding ? getPaddingTop() : this.topOffset) + ((int) Math.ceil(((getMeasuredHeight() - r2) - AndroidUtilities.dp(54.0f)) * this.progress));
            this.rect.set(this.scrollX, AndroidUtilities.dp(12.0f) + paddingTop, this.scrollX + AndroidUtilities.dp(5.0f), AndroidUtilities.dp(42.0f) + paddingTop);
            if (this.type == 0) {
                this.paint.setColor(ColorUtils.blendARGB(this.inactiveColor, this.activeColor, this.bubbleProgress));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint);
            } else {
                this.paint.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite, RecyclerListView.this.resourcesProvider), -1, 0.1f));
                float dp3 = AndroidUtilities.dp(27.0f) + paddingTop;
                this.fastScrollShadowDrawable.setBounds(getMeasuredWidth() - this.fastScrollShadowDrawable.getIntrinsicWidth(), (int) (dp3 - (this.fastScrollShadowDrawable.getIntrinsicHeight() / 2)), getMeasuredWidth(), (int) (dp3 + (this.fastScrollShadowDrawable.getIntrinsicHeight() / 2)));
                this.fastScrollShadowDrawable.draw(canvas);
                canvas.drawCircle(this.scrollX + AndroidUtilities.dp(8.0f), AndroidUtilities.dp(27.0f) + paddingTop, AndroidUtilities.dp(24.0f), this.paint);
                this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, RecyclerListView.this.resourcesProvider));
                canvas.save();
                canvas.translate(this.scrollX + AndroidUtilities.dp(4.0f), AndroidUtilities.dp(34.0f) + paddingTop + (AndroidUtilities.dp(2.0f) * this.bubbleProgress));
                canvas.drawPath(this.arrowPath, this.paint);
                canvas.restore();
                canvas.save();
                canvas.translate(this.scrollX + AndroidUtilities.dp(4.0f), (AndroidUtilities.dp(24.0f) + paddingTop) - (AndroidUtilities.dp(2.0f) * this.bubbleProgress));
                canvas.rotate(180.0f, 0.0f, -AndroidUtilities.dp(2.0f));
                canvas.drawPath(this.arrowPath, this.paint);
                canvas.restore();
            }
            int i2 = this.type;
            if (i2 == 0) {
                if (this.isMoving || this.bubbleProgress != 0.0f) {
                    this.paint.setAlpha((int) (this.bubbleProgress * 255.0f));
                    int dp4 = AndroidUtilities.dp(30.0f) + paddingTop;
                    int dp5 = paddingTop - AndroidUtilities.dp(46.0f);
                    if (dp5 <= AndroidUtilities.dp(12.0f)) {
                        f = AndroidUtilities.dp(12.0f) - dp5;
                        i = AndroidUtilities.dp(12.0f);
                    } else {
                        i = dp5;
                        f = 0.0f;
                    }
                    canvas.translate(AndroidUtilities.dp(10.0f), i);
                    if (f <= AndroidUtilities.dp(29.0f)) {
                        dp2 = AndroidUtilities.dp(44.0f);
                        dp = AndroidUtilities.dp(4.0f) + ((f / AndroidUtilities.dp(29.0f)) * AndroidUtilities.dp(40.0f));
                    } else {
                        dp = AndroidUtilities.dp(44.0f);
                        dp2 = ((1.0f - ((f - AndroidUtilities.dp(29.0f)) / AndroidUtilities.dp(29.0f))) * AndroidUtilities.dp(40.0f)) + AndroidUtilities.dp(4.0f);
                    }
                    boolean z = this.isRtl;
                    if (z) {
                        float[] fArr = this.radii;
                        if (fArr[0] == dp2) {
                        }
                        if (z) {
                            float[] fArr2 = this.radii;
                            fArr2[3] = dp2;
                            fArr2[2] = dp2;
                            fArr2[5] = dp;
                            fArr2[4] = dp;
                        } else {
                            float[] fArr3 = this.radii;
                            fArr3[1] = dp2;
                            fArr3[0] = dp2;
                            fArr3[7] = dp;
                            fArr3[6] = dp;
                        }
                        this.path.reset();
                        this.rect.set(!this.isRtl ? AndroidUtilities.dp(10.0f) : 0.0f, 0.0f, AndroidUtilities.dp(!this.isRtl ? 98.0f : 88.0f), AndroidUtilities.dp(88.0f));
                        this.path.addRoundRect(this.rect, this.radii, Path.Direction.CW);
                        this.path.close();
                        staticLayout = this.letterLayout;
                        if (staticLayout == null) {
                            staticLayout = this.oldLetterLayout;
                        }
                        if (staticLayout != null) {
                            canvas.save();
                            float f2 = this.bubbleProgress;
                            canvas.scale(f2, f2, this.scrollX, dp4 - i);
                            canvas.drawPath(this.path, this.paint);
                            canvas.translate(this.textX, this.textY);
                            staticLayout.draw(canvas);
                            canvas.restore();
                        }
                    }
                    if (!z) {
                        float[] fArr4 = this.radii;
                        if (fArr4[2] == dp2) {
                        }
                        if (z) {
                        }
                        this.path.reset();
                        this.rect.set(!this.isRtl ? AndroidUtilities.dp(10.0f) : 0.0f, 0.0f, AndroidUtilities.dp(!this.isRtl ? 98.0f : 88.0f), AndroidUtilities.dp(88.0f));
                        this.path.addRoundRect(this.rect, this.radii, Path.Direction.CW);
                        this.path.close();
                    }
                    staticLayout = this.letterLayout;
                    if (staticLayout == null) {
                    }
                    if (staticLayout != null) {
                    }
                }
            } else if (i2 == 1 && this.letterLayout != null && this.floatingDateProgress != 0.0f) {
                canvas.save();
                float f3 = (this.floatingDateProgress * 0.3f) + 0.7f;
                canvas.scale(f3, f3, this.rect.right - AndroidUtilities.dp(12.0f), this.rect.centerY());
                float centerY = this.rect.centerY();
                float dp6 = (this.rect.left - (AndroidUtilities.dp(30.0f) * this.bubbleProgress)) - AndroidUtilities.dp(8.0f);
                this.letterLayout.getHeight();
                AndroidUtilities.dp(6.0f);
                this.rect.set((dp6 - ((this.replaceLayoutProgress * this.letterLayout.getWidth()) + (this.fromWidth * (1.0f - this.replaceLayoutProgress)))) - AndroidUtilities.dp(36.0f), (centerY - (this.letterLayout.getHeight() / 2.0f)) - AndroidUtilities.dp(8.0f), dp6 - AndroidUtilities.dp(12.0f), (this.letterLayout.getHeight() / 2.0f) + centerY + AndroidUtilities.dp(8.0f));
                int alpha = this.paint2.getAlpha();
                int alpha2 = this.letterPaint.getAlpha();
                this.paint2.setAlpha((int) (alpha * this.floatingDateProgress));
                Drawable drawable = this.fastScrollBackgroundDrawable;
                RectF rectF = this.rect;
                drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                this.fastScrollBackgroundDrawable.setAlpha((int) (this.floatingDateProgress * 255.0f));
                this.fastScrollBackgroundDrawable.draw(canvas);
                float f4 = this.replaceLayoutProgress;
                if (f4 != 1.0f) {
                    float f5 = f4 + 0.10666667f;
                    this.replaceLayoutProgress = f5;
                    if (f5 > 1.0f) {
                        this.replaceLayoutProgress = 1.0f;
                    } else {
                        invalidate();
                    }
                }
                if (this.replaceLayoutProgress != 1.0f) {
                    canvas.save();
                    this.rect.inset(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f));
                    canvas.clipRect(this.rect);
                    if (this.outLetterLayout != null) {
                        this.letterPaint.setAlpha((int) (alpha2 * this.floatingDateProgress * (1.0f - this.replaceLayoutProgress)));
                        canvas.save();
                        canvas.translate((dp6 - this.outLetterLayout.getWidth()) - AndroidUtilities.dp(24.0f), (centerY - (this.outLetterLayout.getHeight() / 2.0f)) + ((this.fromTop ? -1 : 1) * AndroidUtilities.dp(15.0f) * this.replaceLayoutProgress));
                        this.outLetterLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (this.inLetterLayout != null) {
                        this.letterPaint.setAlpha((int) (alpha2 * this.floatingDateProgress * this.replaceLayoutProgress));
                        canvas.save();
                        canvas.translate((dp6 - this.inLetterLayout.getWidth()) - AndroidUtilities.dp(24.0f), (centerY - (this.inLetterLayout.getHeight() / 2.0f)) + (AndroidUtilities.dp(15.0f) * (this.fromTop ? 1 : -1) * (1.0f - this.replaceLayoutProgress)));
                        this.inLetterLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (this.stableLetterLayout != null) {
                        this.letterPaint.setAlpha((int) (alpha2 * this.floatingDateProgress));
                        canvas.save();
                        canvas.translate((dp6 - this.stableLetterLayout.getWidth()) - AndroidUtilities.dp(24.0f), centerY - (this.stableLetterLayout.getHeight() / 2.0f));
                        this.stableLetterLayout.draw(canvas);
                        canvas.restore();
                    }
                } else {
                    this.letterPaint.setAlpha((int) (alpha2 * this.floatingDateProgress));
                    canvas.save();
                    canvas.translate((dp6 - this.letterLayout.getWidth()) - AndroidUtilities.dp(24.0f), (centerY - (this.letterLayout.getHeight() / 2.0f)) + (AndroidUtilities.dp(15.0f) * (1.0f - this.replaceLayoutProgress)));
                    this.letterLayout.draw(canvas);
                }
                canvas.restore();
                this.paint2.setAlpha(alpha);
                this.letterPaint.setAlpha(alpha2);
                canvas.restore();
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastUpdateTime;
            if (j < 0 || j > 17) {
                j = 17;
            }
            boolean z2 = this.isMoving;
            if ((z2 && this.letterLayout != null && this.bubbleProgress < 1.0f) || ((!z2 || this.letterLayout == null) && this.bubbleProgress > 0.0f)) {
                this.lastUpdateTime = currentTimeMillis;
                invalidate();
                if (!this.isMoving || this.letterLayout == null) {
                    float f6 = this.bubbleProgress - (j / 120.0f);
                    this.bubbleProgress = f6;
                    if (f6 < 0.0f) {
                        this.bubbleProgress = 0.0f;
                    }
                } else {
                    float f7 = this.bubbleProgress + (j / 120.0f);
                    this.bubbleProgress = f7;
                    if (f7 > 1.0f) {
                        this.bubbleProgress = 1.0f;
                    }
                }
            }
            boolean z3 = this.floatingDateVisible;
            if (z3) {
                float f8 = this.floatingDateProgress;
                if (f8 != 1.0f) {
                    float f9 = f8 + (j / 120.0f);
                    this.floatingDateProgress = f9;
                    if (f9 > 1.0f) {
                        this.floatingDateProgress = 1.0f;
                    }
                    invalidate();
                }
            }
            if (z3) {
                return;
            }
            float f10 = this.floatingDateProgress;
            if (f10 != 0.0f) {
                float f11 = f10 - (j / 120.0f);
                this.floatingDateProgress = f11;
                if (f11 < 0.0f) {
                    this.floatingDateProgress = 0.0f;
                }
                invalidate();
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(AndroidUtilities.dp(this.type == 0 ? 132.0f : 240.0f), View.MeasureSpec.getSize(i2));
            this.arrowPath.reset();
            this.arrowPath.setLastPoint(0.0f, 0.0f);
            this.arrowPath.lineTo(AndroidUtilities.dp(4.0f), -AndroidUtilities.dp(4.0f));
            this.arrowPath.lineTo(-AndroidUtilities.dp(4.0f), -AndroidUtilities.dp(4.0f));
            this.arrowPath.close();
        }

        /* JADX WARN: Code restructure failed: missing block: B:31:0x0082, code lost:
        
            if (r8 > 1.0f) goto L31;
         */
        /* JADX WARN: Code restructure failed: missing block: B:79:0x0159, code lost:
        
            if (r0 <= (org.telegram.messenger.AndroidUtilities.dp(30.0f) + r8)) goto L81;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!this.isVisible) {
                this.pressed = false;
                return false;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                this.lastY = y;
                this.startY = y;
                float ceil = ((float) Math.ceil((getMeasuredHeight() - AndroidUtilities.dp(54.0f)) * this.progress)) + AndroidUtilities.dp(12.0f);
                if ((!this.isRtl || x <= AndroidUtilities.dp(25.0f)) && (this.isRtl || x >= AndroidUtilities.dp(107.0f))) {
                    float f = this.lastY;
                    if (f >= ceil && f <= AndroidUtilities.dp(30.0f) + ceil) {
                        if (this.type == 1 && !this.floatingDateVisible) {
                            if ((!this.isRtl || x <= AndroidUtilities.dp(25.0f)) && (this.isRtl || x >= getMeasuredWidth() - AndroidUtilities.dp(25.0f))) {
                                float f2 = this.lastY;
                                if (f2 >= ceil) {
                                }
                            }
                            return false;
                        }
                        this.startDy = this.lastY - ceil;
                        this.startTime = System.currentTimeMillis();
                        this.pressed = true;
                        this.isMoving = false;
                        this.lastUpdateTime = System.currentTimeMillis();
                        invalidate();
                        RecyclerView.Adapter adapter = RecyclerListView.this.getAdapter();
                        showFloatingDate();
                        if (adapter instanceof FastScrollAdapter) {
                            ((FastScrollAdapter) adapter).onStartFastScroll();
                        }
                        return true;
                    }
                }
                return false;
            }
            if (action != 1) {
                if (action == 2) {
                    if (!this.pressed) {
                        return true;
                    }
                    if (Math.abs(motionEvent.getY() - this.startY) > this.touchSlop) {
                        this.isMoving = true;
                    }
                    if (this.isMoving) {
                        float y2 = motionEvent.getY();
                        float dp = AndroidUtilities.dp(12.0f) + this.startDy;
                        float measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(42.0f)) + this.startDy;
                        if (y2 < dp) {
                            y2 = dp;
                        } else if (y2 > measuredHeight) {
                            y2 = measuredHeight;
                        }
                        float f3 = y2 - this.lastY;
                        this.lastY = y2;
                        float measuredHeight2 = this.progress + (f3 / (getMeasuredHeight() - AndroidUtilities.dp(54.0f)));
                        this.progress = measuredHeight2;
                        float f4 = measuredHeight2 >= 0.0f ? 1.0f : 0.0f;
                        this.progress = f4;
                        getCurrentLetter(true);
                        invalidate();
                    }
                    return true;
                }
                if (action != 3) {
                    return this.pressed;
                }
            }
            RecyclerView.Adapter adapter2 = RecyclerListView.this.getAdapter();
            if (this.pressed && !this.isMoving && System.currentTimeMillis() - this.startTime < 150 && (adapter2 instanceof FastScrollAdapter)) {
                ((FastScrollAdapter) adapter2).onFastScrollSingleTap();
            }
            this.isMoving = false;
            this.pressed = false;
            this.lastUpdateTime = System.currentTimeMillis();
            invalidate();
            if (adapter2 instanceof FastScrollAdapter) {
                ((FastScrollAdapter) adapter2).onFinishFastScroll(RecyclerListView.this);
            }
            showFloatingDate();
            return true;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            if (this.viewAlpha != f) {
                this.viewAlpha = f;
                super.setAlpha(f * this.visibilityAlpha);
            }
        }

        public void setIsVisible(boolean z) {
            if (this.isVisible != z) {
                this.isVisible = z;
                float f = z ? 1.0f : 0.0f;
                this.visibilityAlpha = f;
                super.setAlpha(this.viewAlpha * f);
            }
        }

        public void setProgress(float f) {
            this.progress = f;
            invalidate();
        }

        public void setVisibilityAlpha(float f) {
            if (this.visibilityAlpha != f) {
                this.visibilityAlpha = f;
                super.setAlpha(this.viewAlpha * f);
            }
        }

        public void showFloatingDate() {
            if (this.type != 1) {
                return;
            }
            if (!this.floatingDateVisible) {
                this.floatingDateVisible = true;
                invalidate();
            }
            AndroidUtilities.cancelRunOnUIThread(this.hideFloatingDateRunnable);
            AndroidUtilities.runOnUIThread(this.hideFloatingDateRunnable, 2000L);
        }
    }

    public static abstract class FastScrollAdapter extends SelectionAdapter {
        public boolean fastScrollIsVisible(RecyclerListView recyclerListView) {
            return true;
        }

        public abstract String getLetter(int i);

        public abstract void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr);

        public float getScrollProgress(RecyclerListView recyclerListView) {
            return recyclerListView.computeVerticalScrollOffset() / ((getTotalItemsCount() * recyclerListView.getChildAt(0).getMeasuredHeight()) - recyclerListView.getMeasuredHeight());
        }

        public int getTotalItemsCount() {
            return getItemCount();
        }

        public void onFastScrollSingleTap() {
        }

        public void onFinishFastScroll(RecyclerListView recyclerListView) {
        }

        public void onStartFastScroll() {
        }
    }

    public static class FoucsableOnTouchListener implements View.OnTouchListener {
        private boolean onFocus;
        private float x;
        private float y;

        /* JADX WARN: Code restructure failed: missing block: B:13:0x0053, code lost:
        
            if (java.lang.Math.sqrt((r2 * r2) + (r3 * r3)) > r6) goto L19;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0061, code lost:
        
            if (r7.getAction() != 3) goto L20;
         */
        @Override // android.view.View.OnTouchListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ViewParent parent = view.getParent();
            if (parent == null) {
                return false;
            }
            if (motionEvent.getAction() == 0) {
                this.x = motionEvent.getX();
                this.y = motionEvent.getY();
                this.onFocus = true;
                parent.requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() != 2) {
                if (motionEvent.getAction() != 1) {
                }
                this.onFocus = false;
                parent.requestDisallowInterceptTouchEvent(false);
                return false;
            }
            float x = this.x - motionEvent.getX();
            float y = this.y - motionEvent.getY();
            float scaledTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            if (this.onFocus) {
            }
            return false;
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public Holder(View view) {
            super(view);
        }
    }

    public interface IntReturnCallback {
        int run();
    }

    public interface OnInterceptTouchListener {
        boolean onInterceptTouchEvent(MotionEvent motionEvent);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public interface OnItemClickListenerExtended {

        public abstract /* synthetic */ class -CC {
            public static boolean $default$hasDoubleTap(OnItemClickListenerExtended onItemClickListenerExtended, View view, int i) {
                return false;
            }

            public static void $default$onDoubleTap(OnItemClickListenerExtended onItemClickListenerExtended, View view, int i, float f, float f2) {
            }
        }

        boolean hasDoubleTap(View view, int i);

        void onDoubleTap(View view, int i, float f, float f2);

        void onItemClick(View view, int i, float f, float f2);
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(View view, int i);
    }

    public interface OnItemLongClickListenerExtended {

        public abstract /* synthetic */ class -CC {
            public static void $default$onLongClickRelease(OnItemLongClickListenerExtended onItemLongClickListenerExtended) {
            }

            public static void $default$onMove(OnItemLongClickListenerExtended onItemLongClickListenerExtended, float f, float f2) {
            }
        }

        boolean onItemClick(View view, int i, float f, float f2);

        void onLongClickRelease();

        void onMove(float f, float f2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class RecyclerListViewItemClickListener implements RecyclerView.OnItemTouchListener {
        public RecyclerListViewItemClickListener(Context context) {
            RecyclerListView.this.gestureDetector = new GestureDetectorFixDoubleTap(context, new GestureDetectorFixDoubleTap.OnGestureListener() { // from class: org.telegram.ui.Components.RecyclerListView.RecyclerListViewItemClickListener.1
                private View doubleTapView;

                private void onPressItem(final View view, MotionEvent motionEvent) {
                    if (view != null) {
                        if (RecyclerListView.this.onItemClickListener == null && RecyclerListView.this.onItemClickListenerExtended == null) {
                            return;
                        }
                        final float x = motionEvent.getX();
                        final float y = motionEvent.getY();
                        RecyclerListView.this.onChildPressed(view, x, y, true);
                        final int i = RecyclerListView.this.currentChildPosition;
                        if (RecyclerListView.this.instantClick && i != -1) {
                            try {
                                view.playSoundEffect(0);
                            } catch (Exception unused) {
                            }
                            view.sendAccessibilityEvent(1);
                            if (RecyclerListView.this.onItemClickListener != null) {
                                RecyclerListView.this.onItemClickListener.onItemClick(view, i);
                            } else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                                RecyclerListView.this.onItemClickListenerExtended.onItemClick(view, i, x - view.getX(), y - view.getY());
                            }
                        }
                        AndroidUtilities.runOnUIThread(RecyclerListView.this.clickRunnable = new Runnable() { // from class: org.telegram.ui.Components.RecyclerListView.RecyclerListViewItemClickListener.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (this == RecyclerListView.this.clickRunnable) {
                                    RecyclerListView.this.clickRunnable = null;
                                }
                                View view2 = view;
                                if (view2 != null) {
                                    RecyclerListView.this.onChildPressed(view2, 0.0f, 0.0f, false);
                                    if (RecyclerListView.this.instantClick) {
                                        return;
                                    }
                                    try {
                                        view.playSoundEffect(0);
                                    } catch (Exception unused2) {
                                    }
                                    view.sendAccessibilityEvent(1);
                                    if (i != -1) {
                                        if (RecyclerListView.this.onItemClickListener != null) {
                                            RecyclerListView.this.onItemClickListener.onItemClick(view, i);
                                        } else if (RecyclerListView.this.onItemClickListenerExtended != null) {
                                            OnItemClickListenerExtended onItemClickListenerExtended = RecyclerListView.this.onItemClickListenerExtended;
                                            View view3 = view;
                                            onItemClickListenerExtended.onItemClick(view3, i, x - view3.getX(), y - view.getY());
                                        }
                                    }
                                }
                            }
                        }, ViewConfiguration.getPressedStateDuration());
                        if (RecyclerListView.this.selectChildRunnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                            RecyclerListView.this.selectChildRunnable = null;
                            RecyclerListView.this.currentChildView = null;
                            RecyclerListView.this.interceptedByChild = false;
                            RecyclerListView.this.removeSelection(view, motionEvent);
                        }
                    }
                }

                @Override // org.telegram.ui.Components.GestureDetectorFixDoubleTap.OnGestureListener
                public boolean hasDoubleTap(MotionEvent motionEvent) {
                    return RecyclerListView.this.onItemLongClickListenerExtended != null;
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
                public boolean onDoubleTap(MotionEvent motionEvent) {
                    if (this.doubleTapView == null || RecyclerListView.this.onItemClickListenerExtended == null || !RecyclerListView.this.onItemClickListenerExtended.hasDoubleTap(this.doubleTapView, RecyclerListView.this.currentChildPosition)) {
                        return false;
                    }
                    RecyclerListView.this.onItemClickListenerExtended.onDoubleTap(this.doubleTapView, RecyclerListView.this.currentChildPosition, motionEvent.getX(), motionEvent.getY());
                    this.doubleTapView = null;
                    return true;
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onDown(MotionEvent motionEvent) {
                    return false;
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public void onLongPress(MotionEvent motionEvent) {
                    if (RecyclerListView.this.currentChildView == null || RecyclerListView.this.currentChildPosition == -1) {
                        return;
                    }
                    if (RecyclerListView.this.onItemLongClickListener == null && RecyclerListView.this.onItemLongClickListenerExtended == null) {
                        return;
                    }
                    View view = RecyclerListView.this.currentChildView;
                    if (RecyclerListView.this.onItemLongClickListener != null) {
                        if (RecyclerListView.this.onItemLongClickListener.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition)) {
                            try {
                                view.performHapticFeedback(0);
                            } catch (Exception unused) {
                            }
                            view.sendAccessibilityEvent(2);
                            return;
                        }
                        return;
                    }
                    if (RecyclerListView.this.onItemLongClickListenerExtended.onItemClick(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition, motionEvent.getX() - RecyclerListView.this.currentChildView.getX(), motionEvent.getY() - RecyclerListView.this.currentChildView.getY())) {
                        try {
                            view.performHapticFeedback(0);
                        } catch (Exception unused2) {
                        }
                        view.sendAccessibilityEvent(2);
                        RecyclerListView.this.longPressCalled = true;
                    }
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
                public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                    if (this.doubleTapView == null || RecyclerListView.this.onItemClickListenerExtended == null || !RecyclerListView.this.onItemClickListenerExtended.hasDoubleTap(this.doubleTapView, RecyclerListView.this.currentChildPosition)) {
                        return false;
                    }
                    onPressItem(this.doubleTapView, motionEvent);
                    this.doubleTapView = null;
                    return true;
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    if (RecyclerListView.this.currentChildView != null) {
                        if (RecyclerListView.this.onItemClickListenerExtended == null || !RecyclerListView.this.onItemClickListenerExtended.hasDoubleTap(RecyclerListView.this.currentChildView, RecyclerListView.this.currentChildPosition)) {
                            onPressItem(RecyclerListView.this.currentChildView, motionEvent);
                        } else {
                            this.doubleTapView = RecyclerListView.this.currentChildView;
                        }
                    }
                    return false;
                }
            });
            RecyclerListView.this.gestureDetector.setIsLongpressEnabled(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterceptTouchEvent$0(float f, float f2) {
            if (RecyclerListView.this.selectChildRunnable == null || RecyclerListView.this.currentChildView == null) {
                return;
            }
            RecyclerListView recyclerListView = RecyclerListView.this;
            recyclerListView.onChildPressed(recyclerListView.currentChildView, f, f2, true);
            RecyclerListView.this.selectChildRunnable = null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View findChildViewUnder;
            int actionMasked = motionEvent.getActionMasked();
            boolean z = RecyclerListView.this.getScrollState() == 0;
            if ((actionMasked == 0 || actionMasked == 5) && RecyclerListView.this.currentChildView == null && z) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                RecyclerListView.this.longPressCalled = false;
                RecyclerView.ItemAnimator itemAnimator = RecyclerListView.this.getItemAnimator();
                if ((RecyclerListView.this.allowItemsInteractionDuringAnimation || itemAnimator == null || !itemAnimator.isRunning()) && RecyclerListView.this.allowSelectChildAtPosition(x, y) && (findChildViewUnder = RecyclerListView.this.findChildViewUnder(x, y)) != null && RecyclerListView.this.allowSelectChildAtPosition(findChildViewUnder)) {
                    RecyclerListView.this.currentChildView = findChildViewUnder;
                }
                if (RecyclerListView.this.currentChildView instanceof ViewGroup) {
                    float x2 = motionEvent.getX() - RecyclerListView.this.currentChildView.getLeft();
                    float y2 = motionEvent.getY() - RecyclerListView.this.currentChildView.getTop();
                    ViewGroup viewGroup = (ViewGroup) RecyclerListView.this.currentChildView;
                    int childCount = viewGroup.getChildCount() - 1;
                    while (true) {
                        if (childCount < 0) {
                            break;
                        }
                        View childAt = viewGroup.getChildAt(childCount);
                        if (x2 >= childAt.getLeft() && x2 <= childAt.getRight() && y2 >= childAt.getTop() && y2 <= childAt.getBottom() && childAt.isClickable()) {
                            RecyclerListView.this.currentChildView = null;
                            break;
                        }
                        childCount--;
                    }
                }
                RecyclerListView.this.currentChildPosition = -1;
                if (RecyclerListView.this.currentChildView != null) {
                    RecyclerListView recyclerListView = RecyclerListView.this;
                    recyclerListView.currentChildPosition = recyclerListView.useLayoutPositionOnClick ? recyclerView.getChildLayoutPosition(recyclerListView.currentChildView) : recyclerView.getChildAdapterPosition(recyclerListView.currentChildView);
                    MotionEvent obtain = MotionEvent.obtain(0L, 0L, motionEvent.getActionMasked(), motionEvent.getX() - RecyclerListView.this.currentChildView.getLeft(), motionEvent.getY() - RecyclerListView.this.currentChildView.getTop(), 0);
                    if (RecyclerListView.this.currentChildView.onTouchEvent(obtain)) {
                        RecyclerListView.this.interceptedByChild = true;
                    }
                    obtain.recycle();
                }
            }
            if (RecyclerListView.this.currentChildView != null && !RecyclerListView.this.interceptedByChild) {
                try {
                    RecyclerListView.this.gestureDetector.onTouchEvent(motionEvent);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (actionMasked == 0 || actionMasked == 5) {
                if (!RecyclerListView.this.interceptedByChild && RecyclerListView.this.currentChildView != null) {
                    final float x3 = motionEvent.getX();
                    final float y3 = motionEvent.getY();
                    RecyclerListView.this.selectChildRunnable = new Runnable() { // from class: org.telegram.ui.Components.RecyclerListView$RecyclerListViewItemClickListener$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            RecyclerListView.RecyclerListViewItemClickListener.this.lambda$onInterceptTouchEvent$0(x3, y3);
                        }
                    };
                    AndroidUtilities.runOnUIThread(RecyclerListView.this.selectChildRunnable, ViewConfiguration.getTapTimeout());
                    if (RecyclerListView.this.currentChildView.isEnabled()) {
                        RecyclerListView recyclerListView2 = RecyclerListView.this;
                        if (recyclerListView2.canHighlightChildAt(recyclerListView2.currentChildView, x3 - RecyclerListView.this.currentChildView.getX(), y3 - RecyclerListView.this.currentChildView.getY())) {
                            RecyclerListView recyclerListView3 = RecyclerListView.this;
                            recyclerListView3.positionSelector(recyclerListView3.currentChildPosition, RecyclerListView.this.currentChildView);
                            Drawable drawable = RecyclerListView.this.selectorDrawable;
                            if (drawable != null) {
                                Drawable current = drawable.getCurrent();
                                if (current instanceof TransitionDrawable) {
                                    if (RecyclerListView.this.onItemLongClickListener == null && RecyclerListView.this.onItemClickListenerExtended == null) {
                                        ((TransitionDrawable) current).resetTransition();
                                    } else {
                                        ((TransitionDrawable) current).startTransition(ViewConfiguration.getLongPressTimeout());
                                    }
                                }
                                if (Build.VERSION.SDK_INT >= 21) {
                                    RecyclerListView.this.selectorDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                                }
                            }
                            RecyclerListView.this.updateSelectorState();
                        }
                    }
                }
                RecyclerListView.this.selectorRect.setEmpty();
            } else if ((actionMasked == 1 || actionMasked == 6 || actionMasked == 3 || !z) && RecyclerListView.this.currentChildView != null) {
                if (RecyclerListView.this.selectChildRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                    RecyclerListView.this.selectChildRunnable = null;
                }
                View view = RecyclerListView.this.currentChildView;
                RecyclerListView recyclerListView4 = RecyclerListView.this;
                recyclerListView4.onChildPressed(recyclerListView4.currentChildView, 0.0f, 0.0f, false);
                RecyclerListView.this.currentChildView = null;
                RecyclerListView.this.interceptedByChild = false;
                RecyclerListView.this.removeSelection(view, motionEvent);
                if ((actionMasked == 1 || actionMasked == 6 || actionMasked == 3) && RecyclerListView.this.onItemLongClickListenerExtended != null && RecyclerListView.this.longPressCalled) {
                    RecyclerListView.this.onItemLongClickListenerExtended.onLongClickRelease();
                    RecyclerListView.this.longPressCalled = false;
                }
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onRequestDisallowInterceptTouchEvent(boolean z) {
            RecyclerListView.this.cancelClickRunnables(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }
    }

    public static abstract class SectionsAdapter extends FastScrollAdapter {
        private int count;
        private ArrayList hashes = new ArrayList();
        private SparseIntArray sectionCache;
        private int sectionCount;
        private SparseIntArray sectionCountCache;
        private SparseIntArray sectionPositionCache;

        public SectionsAdapter() {
            cleanupCache();
        }

        private int internalGetCountForSection(int i) {
            int i2 = this.sectionCountCache.get(i, ConnectionsManager.DEFAULT_DATACENTER_ID);
            if (i2 != Integer.MAX_VALUE) {
                return i2;
            }
            int countForSection = getCountForSection(i);
            this.sectionCountCache.put(i, countForSection);
            return countForSection;
        }

        private int internalGetSectionCount() {
            int i = this.sectionCount;
            if (i >= 0) {
                return i;
            }
            int sectionCount = getSectionCount();
            this.sectionCount = sectionCount;
            return sectionCount;
        }

        public void cleanupCache() {
            SparseIntArray sparseIntArray = this.sectionCache;
            if (sparseIntArray == null) {
                this.sectionCache = new SparseIntArray();
                this.sectionPositionCache = new SparseIntArray();
                this.sectionCountCache = new SparseIntArray();
            } else {
                sparseIntArray.clear();
                this.sectionPositionCache.clear();
                this.sectionCountCache.clear();
            }
            this.count = -1;
            this.sectionCount = -1;
        }

        public abstract int getCountForSection(int i);

        public int getHash(int i, int i2) {
            return Objects.hash(Integer.valueOf((-49612) * i), getItem(i, i2));
        }

        public final Object getItem(int i) {
            return getItem(getSectionForPosition(i), getPositionInSectionForPosition(i));
        }

        public abstract Object getItem(int i, int i2);

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = this.count;
            if (i >= 0) {
                return i;
            }
            this.count = 0;
            int internalGetSectionCount = internalGetSectionCount();
            for (int i2 = 0; i2 < internalGetSectionCount; i2++) {
                this.count += internalGetCountForSection(i2);
            }
            return this.count;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemViewType(int i) {
            return getItemViewType(getSectionForPosition(i), getPositionInSectionForPosition(i));
        }

        public abstract int getItemViewType(int i, int i2);

        public int getPositionInSectionForPosition(int i) {
            int i2 = this.sectionPositionCache.get(i, ConnectionsManager.DEFAULT_DATACENTER_ID);
            if (i2 != Integer.MAX_VALUE) {
                return i2;
            }
            int internalGetSectionCount = internalGetSectionCount();
            int i3 = 0;
            int i4 = 0;
            while (i3 < internalGetSectionCount) {
                int internalGetCountForSection = internalGetCountForSection(i3) + i4;
                if (i >= i4 && i < internalGetCountForSection) {
                    int i5 = i - i4;
                    this.sectionPositionCache.put(i, i5);
                    return i5;
                }
                i3++;
                i4 = internalGetCountForSection;
            }
            return -1;
        }

        public abstract int getSectionCount();

        public final int getSectionForPosition(int i) {
            int i2 = this.sectionCache.get(i, ConnectionsManager.DEFAULT_DATACENTER_ID);
            if (i2 != Integer.MAX_VALUE) {
                return i2;
            }
            int internalGetSectionCount = internalGetSectionCount();
            int i3 = 0;
            int i4 = 0;
            while (i3 < internalGetSectionCount) {
                int internalGetCountForSection = internalGetCountForSection(i3) + i4;
                if (i >= i4 && i < internalGetCountForSection) {
                    this.sectionCache.put(i, i3);
                    return i3;
                }
                i3++;
                i4 = internalGetCountForSection;
            }
            return -1;
        }

        public abstract View getSectionHeaderView(int i, View view);

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return isEnabled(viewHolder, getSectionForPosition(adapterPosition), getPositionInSectionForPosition(adapterPosition));
        }

        public abstract boolean isEnabled(RecyclerView.ViewHolder viewHolder, int i, int i2);

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            update(false);
        }

        public void notifySectionsChanged() {
            cleanupCache();
        }

        public abstract void onBindViewHolder(int i, int i2, RecyclerView.ViewHolder viewHolder);

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            onBindViewHolder(getSectionForPosition(i), getPositionInSectionForPosition(i), viewHolder);
        }

        public void update(boolean z) {
            final ArrayList arrayList = new ArrayList(this.hashes);
            updateHashes();
            if (z) {
                DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.Components.RecyclerListView.SectionsAdapter.1
                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areContentsTheSame(int i, int i2) {
                        return areItemsTheSame(i, i2);
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areItemsTheSame(int i, int i2) {
                        return Objects.equals(arrayList.get(i), SectionsAdapter.this.hashes.get(i2));
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getNewListSize() {
                        return SectionsAdapter.this.hashes.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getOldListSize() {
                        return arrayList.size();
                    }
                }, true).dispatchUpdatesTo(this);
            } else {
                super.notifyDataSetChanged();
            }
        }

        public void updateHashes() {
            cleanupCache();
            this.hashes.clear();
            int internalGetSectionCount = internalGetSectionCount();
            for (int i = 0; i < internalGetSectionCount; i++) {
                int internalGetCountForSection = internalGetCountForSection(i);
                for (int i2 = 0; i2 < internalGetCountForSection; i2++) {
                    this.hashes.add(Integer.valueOf(getHash(i, i2)));
                }
            }
        }
    }

    public static abstract class SelectionAdapter extends RecyclerView.Adapter {
        public int getSelectionBottomPadding(View view) {
            return 0;
        }

        public abstract boolean isEnabled(RecyclerView.ViewHolder viewHolder);
    }

    public interface onMultiSelectionChanged {
        boolean canSelect(int i);

        int checkPosition(int i, boolean z);

        void getPaddings(int[] iArr);

        boolean limitReached();

        void onSelectionChanged(int i, boolean z, float f, float f2);

        void scrollBy(int i);
    }

    static {
        Method method;
        try {
            method = View.class.getDeclaredMethod("initializeScrollbars", TypedArray.class);
        } catch (Exception unused) {
            method = null;
        }
        initializeScrollbars = method;
    }

    public RecyclerListView(Context context) {
        this(context, null);
    }

    public RecyclerListView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.drawSelection = true;
        this.allowItemsInteractionDuringAnimation = true;
        this.currentFirst = -1;
        this.currentVisible = -1;
        this.hideIfEmpty = true;
        this.selectorType = 2;
        this.selectorRect = new android.graphics.Rect();
        this.translateSelector = -1;
        this.scrollEnabled = true;
        this.lastX = Float.MAX_VALUE;
        this.lastY = Float.MAX_VALUE;
        this.accessibilityEnabled = true;
        this.accessibilityDelegate = new View.AccessibilityDelegate() { // from class: org.telegram.ui.Components.RecyclerListView.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (view.isEnabled()) {
                    accessibilityNodeInfo.addAction(16);
                }
            }
        };
        this.resetSelectorOnChanged = true;
        this.observer = new RecyclerView.AdapterDataObserver() { // from class: org.telegram.ui.Components.RecyclerListView.2
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                RecyclerListView.this.checkIfEmpty(true);
                if (RecyclerListView.this.resetSelectorOnChanged) {
                    RecyclerListView.this.currentFirst = -1;
                    if (RecyclerListView.this.removeHighlighSelectionRunnable == null) {
                        RecyclerListView.this.selectorRect.setEmpty();
                    }
                }
                RecyclerListView.this.invalidate();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int i, int i2) {
                RecyclerListView.this.checkIfEmpty(true);
                if (RecyclerListView.this.pinnedHeader == null || RecyclerListView.this.pinnedHeader.getAlpha() != 0.0f) {
                    return;
                }
                RecyclerListView.this.currentFirst = -1;
                RecyclerListView.this.invalidateViews();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i, int i2) {
                RecyclerListView.this.checkIfEmpty(true);
            }
        };
        this.scroller = new Runnable() { // from class: org.telegram.ui.Components.RecyclerListView.6
            @Override // java.lang.Runnable
            public void run() {
                int dp;
                RecyclerListView recyclerListView;
                int measuredHeight;
                RecyclerListView recyclerListView2 = RecyclerListView.this;
                recyclerListView2.multiSelectionListener.getPaddings(recyclerListView2.listPaddings);
                if (RecyclerListView.this.multiselectScrollToTop) {
                    dp = -AndroidUtilities.dp(12.0f);
                    recyclerListView = RecyclerListView.this;
                    measuredHeight = recyclerListView.listPaddings[0];
                } else {
                    dp = AndroidUtilities.dp(12.0f);
                    recyclerListView = RecyclerListView.this;
                    measuredHeight = recyclerListView.getMeasuredHeight() - RecyclerListView.this.listPaddings[1];
                }
                recyclerListView.chekMultiselect(0.0f, measuredHeight);
                RecyclerListView.this.multiSelectionListener.scrollBy(dp);
                RecyclerListView recyclerListView3 = RecyclerListView.this;
                if (recyclerListView3.multiselectScrollRunning) {
                    AndroidUtilities.runOnUIThread(recyclerListView3.scroller);
                }
            }
        };
        this.resourcesProvider = resourcesProvider;
        setGlowColor(getThemedColor(Theme.key_actionBarDefault));
        Drawable selectorDrawable = Theme.getSelectorDrawable(getThemedColor(Theme.key_listSelector), false);
        this.selectorDrawable = selectorDrawable;
        selectorDrawable.setCallback(this);
        try {
            if (!gotAttributes) {
                int[] resourceDeclareStyleableIntArray = getResourceDeclareStyleableIntArray("com.android.internal", "View");
                attributes = resourceDeclareStyleableIntArray;
                if (resourceDeclareStyleableIntArray == null) {
                    attributes = new int[0];
                }
                gotAttributes = true;
            }
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributes);
            Method method = initializeScrollbars;
            if (method != null) {
                method.invoke(this, obtainStyledAttributes);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        super.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.RecyclerListView.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                RecyclerListView.this.checkStopHeavyOperations(i);
                if (i != 0 && RecyclerListView.this.currentChildView != null) {
                    if (RecyclerListView.this.selectChildRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(RecyclerListView.this.selectChildRunnable);
                        RecyclerListView.this.selectChildRunnable = null;
                    }
                    MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                    try {
                        RecyclerListView.this.gestureDetector.onTouchEvent(obtain);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    RecyclerListView.this.currentChildView.onTouchEvent(obtain);
                    obtain.recycle();
                    View view = RecyclerListView.this.currentChildView;
                    RecyclerListView recyclerListView = RecyclerListView.this;
                    recyclerListView.onChildPressed(recyclerListView.currentChildView, 0.0f, 0.0f, false);
                    RecyclerListView.this.currentChildView = null;
                    RecyclerListView.this.removeSelection(view, null);
                    RecyclerListView.this.interceptedByChild = false;
                }
                if (RecyclerListView.this.onScrollListener != null) {
                    RecyclerListView.this.onScrollListener.onScrollStateChanged(recyclerView, i);
                }
                RecyclerListView recyclerListView2 = RecyclerListView.this;
                boolean z = i == 1 || i == 2;
                recyclerListView2.scrollingByUser = z;
                if (z) {
                    recyclerListView2.scrolledByUserOnce = true;
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                if (RecyclerListView.this.onScrollListener != null) {
                    RecyclerListView.this.onScrollListener.onScrolled(recyclerView, i, i2);
                }
                RecyclerListView recyclerListView = RecyclerListView.this;
                int i3 = recyclerListView.selectorPosition;
                android.graphics.Rect rect = recyclerListView.selectorRect;
                if (i3 != -1) {
                    rect.offset(-i, -i2);
                    RecyclerListView recyclerListView2 = RecyclerListView.this;
                    Drawable drawable = recyclerListView2.selectorDrawable;
                    if (drawable != null) {
                        drawable.setBounds(recyclerListView2.selectorRect);
                    }
                    RecyclerListView.this.invalidate();
                } else {
                    rect.setEmpty();
                }
                RecyclerListView.this.checkSection(false);
                if (i2 != 0 && RecyclerListView.this.fastScroll != null) {
                    RecyclerListView.this.fastScroll.showFloatingDate();
                }
                if (RecyclerListView.this.pendingHighlightPosition != null) {
                    RecyclerListView recyclerListView3 = RecyclerListView.this;
                    recyclerListView3.highlightRowInternal(recyclerListView3.pendingHighlightPosition, 700, false);
                }
            }
        });
        addOnItemTouchListener(new RecyclerListViewItemClickListener(context));
    }

    private void cancelMultiselectScroll() {
        this.multiselectScrollRunning = false;
        AndroidUtilities.cancelRunOnUIThread(this.scroller);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIfEmpty(boolean z) {
        ViewPropertyAnimator listener;
        if (this.isHidden) {
            return;
        }
        if (getAdapter() == null || this.emptyView == null) {
            if (!this.hiddenByEmptyView || getVisibility() == 0) {
                return;
            }
            setVisibility(0);
            this.hiddenByEmptyView = false;
            return;
        }
        boolean emptyViewIsVisible = emptyViewIsVisible();
        int i = emptyViewIsVisible ? 0 : 8;
        if (!this.animateEmptyView || !SharedConfig.animationsEnabled()) {
            z = false;
        }
        emptyViewUpdated(emptyViewIsVisible, z);
        if (!z) {
            this.emptyViewAnimateToVisibility = i;
            this.emptyView.setVisibility(i);
            this.emptyView.setAlpha(1.0f);
        } else if (this.emptyViewAnimateToVisibility != i) {
            this.emptyViewAnimateToVisibility = i;
            if (i == 0) {
                this.emptyView.animate().setListener(null).cancel();
                if (this.emptyView.getVisibility() == 8) {
                    this.emptyView.setVisibility(0);
                    this.emptyView.setAlpha(0.0f);
                    if (this.emptyViewAnimationType == 1) {
                        this.emptyView.setScaleX(0.7f);
                        this.emptyView.setScaleY(0.7f);
                    }
                }
                listener = this.emptyView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L);
            } else if (this.emptyView.getVisibility() != 8) {
                ViewPropertyAnimator alpha = this.emptyView.animate().alpha(0.0f);
                if (this.emptyViewAnimationType == 1) {
                    alpha.scaleY(0.7f).scaleX(0.7f);
                }
                listener = alpha.setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.RecyclerListView.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (RecyclerListView.this.emptyView != null) {
                            RecyclerListView.this.emptyView.setVisibility(8);
                        }
                    }
                });
            }
            listener.start();
        }
        if (this.hideIfEmpty) {
            int i2 = emptyViewIsVisible ? 4 : 0;
            if (getVisibility() != i2) {
                setVisibility(i2);
            }
            this.hiddenByEmptyView = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStopHeavyOperations(int i) {
        if (i == 0) {
            if (this.stoppedAllHeavyOperations) {
                this.stoppedAllHeavyOperations = false;
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
                return;
            }
            return;
        }
        if (this.stoppedAllHeavyOperations || !this.allowStopHeaveOperations) {
            return;
        }
        this.stoppedAllHeavyOperations = true;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean chekMultiselect(float f, float f2) {
        int measuredHeight = getMeasuredHeight();
        int[] iArr = this.listPaddings;
        float min = Math.min(measuredHeight - iArr[1], Math.max(f2, iArr[0]));
        float min2 = Math.min(getMeasuredWidth(), Math.max(f, 0.0f));
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                break;
            }
            this.multiSelectionListener.getPaddings(this.listPaddings);
            if (!this.useRelativePositions) {
                View childAt = getChildAt(i);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(childAt.getLeft(), childAt.getTop(), childAt.getLeft() + childAt.getMeasuredWidth(), childAt.getTop() + childAt.getMeasuredHeight());
                if (rectF.contains(min2, min)) {
                    int childLayoutPosition = getChildLayoutPosition(childAt);
                    int i2 = this.currentSelectedPosition;
                    if (i2 != childLayoutPosition) {
                        int i3 = this.startSelectionFrom;
                        boolean z = i2 > i3 || childLayoutPosition > i3;
                        childLayoutPosition = this.multiSelectionListener.checkPosition(childLayoutPosition, z);
                        if (z) {
                            int i4 = this.currentSelectedPosition;
                            if (childLayoutPosition <= i4) {
                                while (i4 > childLayoutPosition) {
                                    if (i4 != this.startSelectionFrom && this.multiSelectionListener.canSelect(i4)) {
                                        this.multiSelectionListener.onSelectionChanged(i4, false, min2, min);
                                    }
                                    i4--;
                                }
                            } else if (!this.multiSelectionListener.limitReached()) {
                                for (int i5 = this.currentSelectedPosition + 1; i5 <= childLayoutPosition; i5++) {
                                    if (i5 != this.startSelectionFrom && this.multiSelectionListener.canSelect(i5)) {
                                        this.multiSelectionListener.onSelectionChanged(i5, true, min2, min);
                                    }
                                }
                            }
                        } else {
                            int i6 = this.currentSelectedPosition;
                            if (childLayoutPosition > i6) {
                                while (i6 < childLayoutPosition) {
                                    if (i6 != this.startSelectionFrom && this.multiSelectionListener.canSelect(i6)) {
                                        this.multiSelectionListener.onSelectionChanged(i6, false, min2, min);
                                    }
                                    i6++;
                                }
                            } else if (!this.multiSelectionListener.limitReached()) {
                                for (int i7 = this.currentSelectedPosition - 1; i7 >= childLayoutPosition; i7--) {
                                    if (i7 != this.startSelectionFrom && this.multiSelectionListener.canSelect(i7)) {
                                        this.multiSelectionListener.onSelectionChanged(i7, true, min2, min);
                                    }
                                }
                            }
                        }
                    }
                    if (!this.multiSelectionListener.limitReached()) {
                        this.currentSelectedPosition = childLayoutPosition;
                    }
                }
            }
            i++;
        }
        return true;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(6:8|(1:10)(4:18|(1:20)|13|14)|11|12|13|14) */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0027, code lost:
    
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0039, code lost:
    
        org.telegram.messenger.FileLog.e(r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void ensurePinnedHeaderLayout(View view, boolean z) {
        int makeMeasureSpec;
        int makeMeasureSpec2;
        if (view == null) {
            return;
        }
        if (view.isLayoutRequested() || z) {
            int i = this.sectionsType;
            if (i != 1) {
                if (i == 2) {
                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
                    makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
                }
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    }

    private int[] getDrawableStateForSelector() {
        int[] onCreateDrawableState = onCreateDrawableState(1);
        onCreateDrawableState[onCreateDrawableState.length - 1] = 16842919;
        return onCreateDrawableState;
    }

    private View getSectionHeaderView(int i, View view) {
        boolean z = view == null;
        View sectionHeaderView = this.sectionsAdapter.getSectionHeaderView(i, view);
        if (z) {
            ensurePinnedHeaderLayout(sectionHeaderView, false);
        }
        return sectionHeaderView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void highlightRowInternal(IntReturnCallback intReturnCallback, int i, boolean z) {
        Runnable runnable = this.removeHighlighSelectionRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.removeHighlighSelectionRunnable = null;
        }
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(intReturnCallback.run());
        if (findViewHolderForAdapterPosition == null) {
            if (z) {
                this.pendingHighlightPosition = intReturnCallback;
                return;
            }
            return;
        }
        int layoutPosition = findViewHolderForAdapterPosition.getLayoutPosition();
        this.highlightPosition = layoutPosition;
        positionSelector(layoutPosition, findViewHolderForAdapterPosition.itemView, false, -1.0f, -1.0f, true);
        Drawable drawable = this.selectorDrawable;
        if (drawable != null) {
            Drawable current = drawable.getCurrent();
            if (current instanceof TransitionDrawable) {
                if (this.onItemLongClickListener == null && this.onItemClickListenerExtended == null) {
                    ((TransitionDrawable) current).resetTransition();
                } else {
                    ((TransitionDrawable) current).startTransition(ViewConfiguration.getLongPressTimeout());
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.selectorDrawable.setHotspot(findViewHolderForAdapterPosition.itemView.getMeasuredWidth() / 2, findViewHolderForAdapterPosition.itemView.getMeasuredHeight() / 2);
            }
        }
        Drawable drawable2 = this.selectorDrawable;
        if (drawable2 != null && drawable2.isStateful() && this.selectorDrawable.setState(getDrawableStateForSelector())) {
            invalidateDrawable(this.selectorDrawable);
        }
        if (i > 0) {
            this.pendingHighlightPosition = null;
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.RecyclerListView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RecyclerListView.this.lambda$highlightRowInternal$0();
                }
            };
            this.removeHighlighSelectionRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$highlightRowInternal$0() {
        this.removeHighlighSelectionRunnable = null;
        this.pendingHighlightPosition = null;
        Drawable drawable = this.selectorDrawable;
        if (drawable != null) {
            Drawable current = drawable.getCurrent();
            if (current instanceof TransitionDrawable) {
                ((TransitionDrawable) current).resetTransition();
            }
        }
        Drawable drawable2 = this.selectorDrawable;
        if (drawable2 == null || !drawable2.isStateful()) {
            return;
        }
        this.selectorDrawable.setState(StateSet.NOTHING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void positionSelector(int i, View view) {
        positionSelector(i, view, false, -1.0f, -1.0f, false);
    }

    private void positionSelector(int i, View view, boolean z, float f, float f2, boolean z2) {
        Runnable runnable = this.removeHighlighSelectionRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.removeHighlighSelectionRunnable = null;
            this.pendingHighlightPosition = null;
        }
        if (this.selectorDrawable == null) {
            return;
        }
        boolean z3 = i != this.selectorPosition;
        int selectionBottomPadding = getAdapter() instanceof SelectionAdapter ? ((SelectionAdapter) getAdapter()).getSelectionBottomPadding(view) : 0;
        if (i != -1) {
            this.selectorPosition = i;
        }
        this.selectorView = view;
        if (this.selectorType == 8) {
            Theme.setMaskDrawableRad(this.selectorDrawable, this.selectorRadius, 0);
        } else if (this.topBottomSelectorRadius > 0 && getAdapter() != null) {
            Theme.setMaskDrawableRad(this.selectorDrawable, i == 0 ? this.topBottomSelectorRadius : 0, i == getAdapter().getItemCount() + (-2) ? this.topBottomSelectorRadius : 0);
        }
        this.selectorRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom() - selectionBottomPadding);
        boolean isEnabled = view.isEnabled();
        if (this.isChildViewEnabled != isEnabled) {
            this.isChildViewEnabled = isEnabled;
        }
        if (z3) {
            this.selectorDrawable.setVisible(false, false);
            this.selectorDrawable.setState(StateSet.NOTHING);
        }
        setListSelectorColor(getSelectorColor(i));
        this.selectorDrawable.setBounds(this.selectorRect);
        if (z3 && getVisibility() == 0) {
            this.selectorDrawable.setVisible(true, false);
        }
        if (Build.VERSION.SDK_INT < 21 || !z) {
            return;
        }
        this.selectorDrawable.setHotspot(f, f2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSelection(View view, MotionEvent motionEvent) {
        if (view == null || this.selectorRect.isEmpty()) {
            return;
        }
        if (view.isEnabled()) {
            positionSelector(this.currentChildPosition, view);
            Drawable drawable = this.selectorDrawable;
            if (drawable != null) {
                Drawable current = drawable.getCurrent();
                if (current instanceof TransitionDrawable) {
                    ((TransitionDrawable) current).resetTransition();
                }
                if (motionEvent != null && Build.VERSION.SDK_INT >= 21) {
                    this.selectorDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
            }
        } else {
            this.selectorRect.setEmpty();
        }
        updateSelectorState();
    }

    private void requestDisallowInterceptTouchEvent(View view, boolean z) {
        ViewParent parent;
        if (view == null || (parent = view.getParent()) == null) {
            return;
        }
        parent.requestDisallowInterceptTouchEvent(z);
        ViewParent touchParent = getTouchParent();
        if (touchParent == null) {
            return;
        }
        touchParent.requestDisallowInterceptTouchEvent(z);
    }

    private void startMultiselectScroll(boolean z) {
        this.multiselectScrollToTop = z;
        if (this.multiselectScrollRunning) {
            return;
        }
        this.multiselectScrollRunning = true;
        AndroidUtilities.cancelRunOnUIThread(this.scroller);
        AndroidUtilities.runOnUIThread(this.scroller);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectorState() {
        Drawable drawable = this.selectorDrawable;
        if (drawable == null || !drawable.isStateful()) {
            return;
        }
        if (this.currentChildView != null) {
            if (this.selectorDrawable.setState(getDrawableStateForSelector())) {
                invalidateDrawable(this.selectorDrawable);
            }
        } else if (this.removeHighlighSelectionRunnable == null) {
            this.selectorDrawable.setState(StateSet.NOTHING);
        }
    }

    public void addOverlayView(View view, FrameLayout.LayoutParams layoutParams) {
        if (this.overlayContainer == null) {
            this.overlayContainer = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.RecyclerListView.5
                @Override // android.view.View, android.view.ViewParent
                public void requestLayout() {
                    super.requestLayout();
                    try {
                        measure(View.MeasureSpec.makeMeasureSpec(RecyclerListView.this.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(RecyclerListView.this.getMeasuredHeight(), 1073741824));
                        layout(0, 0, RecyclerListView.this.overlayContainer.getMeasuredWidth(), RecyclerListView.this.overlayContainer.getMeasuredHeight());
                    } catch (Exception unused) {
                    }
                }
            };
        }
        this.overlayContainer.addView(view, layoutParams);
    }

    protected boolean allowSelectChildAtPosition(float f, float f2) {
        return true;
    }

    protected boolean allowSelectChildAtPosition(View view) {
        return true;
    }

    protected boolean canHighlightChildAt(View view, float f, float f2) {
        return true;
    }

    @Override // android.view.View
    public boolean canScrollVertically(int i) {
        return this.scrollEnabled && super.canScrollVertically(i);
    }

    public void cancelClickRunnables(boolean z) {
        Runnable runnable = this.selectChildRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.selectChildRunnable = null;
        }
        View view = this.currentChildView;
        if (view != null) {
            if (z) {
                onChildPressed(view, 0.0f, 0.0f, false);
            }
            this.currentChildView = null;
            removeSelection(view, null);
        }
        this.selectorRect.setEmpty();
        Runnable runnable2 = this.clickRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.clickRunnable = null;
        }
        this.interceptedByChild = false;
    }

    public void checkIfEmpty() {
        checkIfEmpty(updateEmptyViewAnimated());
    }

    public void checkSection(boolean z) {
        FastScroll fastScroll;
        RecyclerView.ViewHolder childViewHolder;
        FastScroll fastScroll2;
        View view;
        int i;
        int min;
        RecyclerView.ViewHolder childViewHolder2;
        int adapterPosition;
        int sectionForPosition;
        View view2;
        int i2;
        if (((this.scrollingByUser || z) && this.fastScroll != null) || !(this.sectionsType == 0 || this.sectionsAdapter == null)) {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.getOrientation() == 1) {
                    if (this.sectionsAdapter == null) {
                        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        Math.abs(linearLayoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition);
                        if (findFirstVisibleItemPosition == -1) {
                            return;
                        }
                        if ((!this.scrollingByUser && !z) || (fastScroll = this.fastScroll) == null || fastScroll.isPressed()) {
                            return;
                        }
                        RecyclerView.Adapter adapter = getAdapter();
                        if (adapter instanceof FastScrollAdapter) {
                            FastScrollAdapter fastScrollAdapter = (FastScrollAdapter) adapter;
                            float scrollProgress = fastScrollAdapter.getScrollProgress(this);
                            this.fastScroll.setIsVisible(fastScrollAdapter.fastScrollIsVisible(this));
                            this.fastScroll.setProgress(Math.min(1.0f, scrollProgress));
                            this.fastScroll.getCurrentLetter(false);
                            return;
                        }
                        return;
                    }
                    int paddingTop = getPaddingTop();
                    int i3 = this.sectionsType;
                    int i4 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    if (i3 != 1 && i3 != 3) {
                        if (i3 == 2) {
                            this.pinnedHeaderShadowTargetAlpha = 0.0f;
                            if (this.sectionsAdapter.getItemCount() == 0) {
                                return;
                            }
                            int childCount = getChildCount();
                            View view3 = null;
                            int i5 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            View view4 = null;
                            int i6 = 0;
                            for (int i7 = 0; i7 < childCount; i7++) {
                                View childAt = getChildAt(i7);
                                int bottom = childAt.getBottom();
                                if (bottom > this.sectionOffset + paddingTop) {
                                    if (bottom < i4) {
                                        view4 = childAt;
                                        i4 = bottom;
                                    }
                                    i6 = Math.max(i6, bottom);
                                    if (bottom >= this.sectionOffset + paddingTop + AndroidUtilities.dp(32.0f) && bottom < i5) {
                                        view3 = childAt;
                                        i5 = bottom;
                                    }
                                }
                            }
                            if (view4 == null || (childViewHolder2 = getChildViewHolder(view4)) == null || (sectionForPosition = this.sectionsAdapter.getSectionForPosition((adapterPosition = childViewHolder2.getAdapterPosition()))) < 0) {
                                return;
                            }
                            if (this.currentFirst != sectionForPosition || this.pinnedHeader == null) {
                                View sectionHeaderView = getSectionHeaderView(sectionForPosition, this.pinnedHeader);
                                this.pinnedHeader = sectionHeaderView;
                                sectionHeaderView.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0));
                                View view5 = this.pinnedHeader;
                                view5.layout(0, 0, view5.getMeasuredWidth(), this.pinnedHeader.getMeasuredHeight());
                                this.currentFirst = sectionForPosition;
                            }
                            if (this.pinnedHeader != null && view3 != null && view3.getClass() != this.pinnedHeader.getClass()) {
                                this.pinnedHeaderShadowTargetAlpha = 1.0f;
                            }
                            int countForSection = this.sectionsAdapter.getCountForSection(sectionForPosition);
                            int positionInSectionForPosition = this.sectionsAdapter.getPositionInSectionForPosition(adapterPosition);
                            int i8 = (i6 == 0 || i6 >= getMeasuredHeight() - getPaddingBottom()) ? this.sectionOffset : 0;
                            if (positionInSectionForPosition == countForSection - 1) {
                                int height = this.pinnedHeader.getHeight();
                                int top = ((view4.getTop() - paddingTop) - this.sectionOffset) + view4.getHeight();
                                int i9 = top < height ? top - height : paddingTop;
                                if (i9 < 0) {
                                    view2 = this.pinnedHeader;
                                    i2 = paddingTop + i8 + i9;
                                    view2.setTag(Integer.valueOf(i2));
                                    invalidate();
                                    return;
                                }
                            }
                            view2 = this.pinnedHeader;
                            i2 = paddingTop + i8;
                            view2.setTag(Integer.valueOf(i2));
                            invalidate();
                            return;
                        }
                        return;
                    }
                    int childCount2 = getChildCount();
                    int i10 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    View view6 = null;
                    int i11 = 0;
                    for (int i12 = 0; i12 < childCount2; i12++) {
                        View childAt2 = getChildAt(i12);
                        int bottom2 = childAt2.getBottom();
                        if (bottom2 > this.sectionOffset + paddingTop) {
                            if (bottom2 < i4) {
                                i4 = bottom2;
                                view6 = childAt2;
                            }
                            i11 = Math.max(i11, bottom2);
                            if (bottom2 >= this.sectionOffset + paddingTop + AndroidUtilities.dp(32.0f) && bottom2 < i10) {
                                i10 = bottom2;
                            }
                        }
                    }
                    if (view6 == null || (childViewHolder = getChildViewHolder(view6)) == null) {
                        return;
                    }
                    int adapterPosition2 = childViewHolder.getAdapterPosition();
                    int abs = Math.abs(linearLayoutManager.findLastVisibleItemPosition() - adapterPosition2) + 1;
                    if ((this.scrollingByUser || z) && (fastScroll2 = this.fastScroll) != null && !fastScroll2.isPressed() && (getAdapter() instanceof FastScrollAdapter)) {
                        this.fastScroll.setProgress(Math.min(1.0f, adapterPosition2 / ((this.sectionsAdapter.getTotalItemsCount() - abs) + 1)));
                    }
                    this.headersCache.addAll(this.headers);
                    this.headers.clear();
                    if (this.sectionsAdapter.getItemCount() == 0) {
                        return;
                    }
                    if (this.currentFirst != adapterPosition2 || this.currentVisible != abs) {
                        this.currentFirst = adapterPosition2;
                        this.currentVisible = abs;
                        this.sectionsCount = 1;
                        int sectionForPosition2 = this.sectionsAdapter.getSectionForPosition(adapterPosition2);
                        this.startSection = sectionForPosition2;
                        int countForSection2 = (this.sectionsAdapter.getCountForSection(sectionForPosition2) + adapterPosition2) - this.sectionsAdapter.getPositionInSectionForPosition(adapterPosition2);
                        while (countForSection2 < adapterPosition2 + abs) {
                            countForSection2 += this.sectionsAdapter.getCountForSection(this.startSection + this.sectionsCount);
                            this.sectionsCount++;
                        }
                    }
                    if (this.sectionsType != 3) {
                        int i13 = adapterPosition2;
                        for (int i14 = this.startSection; i14 < this.startSection + this.sectionsCount; i14++) {
                            if (this.headersCache.isEmpty()) {
                                view = null;
                            } else {
                                view = (View) this.headersCache.get(0);
                                this.headersCache.remove(0);
                            }
                            View sectionHeaderView2 = getSectionHeaderView(i14, view);
                            this.headers.add(sectionHeaderView2);
                            int countForSection3 = this.sectionsAdapter.getCountForSection(i14);
                            if (i14 == this.startSection) {
                                int positionInSectionForPosition2 = this.sectionsAdapter.getPositionInSectionForPosition(i13);
                                if (positionInSectionForPosition2 == countForSection3 - 1) {
                                    min = (-sectionHeaderView2.getHeight()) + paddingTop;
                                } else if (positionInSectionForPosition2 == countForSection3 - 2) {
                                    View childAt3 = getChildAt(i13 - adapterPosition2);
                                    min = Math.min(childAt3 != null ? childAt3.getTop() + paddingTop : -AndroidUtilities.dp(100.0f), 0);
                                } else {
                                    i = 0;
                                    sectionHeaderView2.setTag(i);
                                    countForSection3 -= this.sectionsAdapter.getPositionInSectionForPosition(adapterPosition2);
                                }
                                i = Integer.valueOf(min);
                                sectionHeaderView2.setTag(i);
                                countForSection3 -= this.sectionsAdapter.getPositionInSectionForPosition(adapterPosition2);
                            } else {
                                View childAt4 = getChildAt(i13 - adapterPosition2);
                                sectionHeaderView2.setTag(Integer.valueOf(childAt4 != null ? childAt4.getTop() + paddingTop : -AndroidUtilities.dp(100.0f)));
                            }
                            i13 += countForSection3;
                        }
                    }
                }
            }
        }
    }

    public void clickItem(View view, int i) {
        OnItemClickListener onItemClickListener = this.onItemClickListener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, i);
            return;
        }
        OnItemClickListenerExtended onItemClickListenerExtended = this.onItemClickListenerExtended;
        if (onItemClickListenerExtended != null) {
            onItemClickListenerExtended.onItemClick(view, i, 0.0f, 0.0f);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x0241, code lost:
    
        if (r1 > r2) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0253, code lost:
    
        invalidate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0251, code lost:
    
        r9.pinnedHeaderShadowAlpha = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x024f, code lost:
    
        if (r1 < r2) goto L114;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        View view;
        Drawable drawable;
        Consumer consumer;
        View view2;
        Drawable drawable2;
        Consumer consumer2;
        View view3;
        RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = this.itemsEnterAnimator;
        if (recyclerItemsEnterAnimator != null) {
            recyclerItemsEnterAnimator.dispatchDraw();
        }
        if (this.drawSelection && this.drawSelectorBehind && !this.selectorRect.isEmpty() && (drawable2 = this.selectorDrawable) != null) {
            int i = this.translateSelector;
            if ((i == -2 || i == this.selectorPosition) && this.selectorView != null) {
                this.selectorDrawable.setBounds(this.selectorView.getLeft(), this.selectorView.getTop(), this.selectorView.getRight(), this.selectorView.getBottom() - (getAdapter() instanceof SelectionAdapter ? ((SelectionAdapter) getAdapter()).getSelectionBottomPadding(this.selectorView) : 0));
            } else {
                drawable2.setBounds(this.selectorRect);
            }
            canvas.save();
            int i2 = this.translateSelector;
            if ((i2 == -2 || i2 == this.selectorPosition) && (consumer2 = this.selectorTransformer) != null) {
                consumer2.accept(canvas);
            }
            int i3 = this.translateSelector;
            if ((i3 == -2 || i3 == this.selectorPosition) && (view3 = this.selectorView) != null) {
                canvas.translate(view3.getX() - this.selectorRect.left, this.selectorView.getY() - this.selectorRect.top);
                this.selectorDrawable.setAlpha((int) (this.selectorView.getAlpha() * 255.0f));
            }
            this.selectorDrawable.draw(canvas);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
        if (this.drawSelection && !this.drawSelectorBehind && !this.selectorRect.isEmpty() && (drawable = this.selectorDrawable) != null) {
            int i4 = this.translateSelector;
            if ((i4 == -2 || i4 == this.selectorPosition) && this.selectorView != null) {
                this.selectorDrawable.setBounds(this.selectorView.getLeft(), this.selectorView.getTop(), this.selectorView.getRight(), this.selectorView.getBottom() - (getAdapter() instanceof SelectionAdapter ? ((SelectionAdapter) getAdapter()).getSelectionBottomPadding(this.selectorView) : 0));
            } else {
                drawable.setBounds(this.selectorRect);
            }
            canvas.save();
            int i5 = this.translateSelector;
            if ((i5 == -2 || i5 == this.selectorPosition) && (consumer = this.selectorTransformer) != null) {
                consumer.accept(canvas);
            }
            int i6 = this.translateSelector;
            if ((i6 == -2 || i6 == this.selectorPosition) && (view2 = this.selectorView) != null) {
                canvas.translate(view2.getX() - this.selectorRect.left, this.selectorView.getY() - this.selectorRect.top);
                this.selectorDrawable.setAlpha((int) (this.selectorView.getAlpha() * 255.0f));
            }
            this.selectorDrawable.draw(canvas);
            canvas.restore();
        }
        FrameLayout frameLayout = this.overlayContainer;
        if (frameLayout != null) {
            frameLayout.draw(canvas);
        }
        int i7 = this.sectionsType;
        if (i7 == 1) {
            if (this.sectionsAdapter == null || this.headers.isEmpty()) {
                return;
            }
            for (int i8 = 0; i8 < this.headers.size(); i8++) {
                View view4 = (View) this.headers.get(i8);
                int save = canvas.save();
                canvas.translate(LocaleController.isRTL ? getWidth() - view4.getWidth() : 0.0f, ((Integer) view4.getTag()).intValue());
                canvas.clipRect(0, 0, getWidth(), view4.getMeasuredHeight());
                view4.draw(canvas);
                canvas.restoreToCount(save);
            }
            return;
        }
        if (i7 != 2 || this.sectionsAdapter == null || (view = this.pinnedHeader) == null || view.getAlpha() == 0.0f) {
            return;
        }
        int save2 = canvas.save();
        canvas.translate(LocaleController.isRTL ? getWidth() - this.pinnedHeader.getWidth() : 0.0f, ((Integer) this.pinnedHeader.getTag()).intValue());
        Drawable drawable3 = this.pinnedHeaderShadowDrawable;
        if (drawable3 != null) {
            drawable3.setBounds(0, this.pinnedHeader.getMeasuredHeight(), getWidth(), this.pinnedHeader.getMeasuredHeight() + this.pinnedHeaderShadowDrawable.getIntrinsicHeight());
            this.pinnedHeaderShadowDrawable.setAlpha((int) (this.pinnedHeaderShadowAlpha * 255.0f));
            this.pinnedHeaderShadowDrawable.draw(canvas);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long min = Math.min(20L, elapsedRealtime - this.lastAlphaAnimationTime);
            this.lastAlphaAnimationTime = elapsedRealtime;
            float f = this.pinnedHeaderShadowAlpha;
            float f2 = this.pinnedHeaderShadowTargetAlpha;
            if (f < f2) {
                float f3 = f + (min / 180.0f);
                this.pinnedHeaderShadowAlpha = f3;
            } else if (f > f2) {
                float f4 = f - (min / 180.0f);
                this.pinnedHeaderShadowAlpha = f4;
            }
        }
        canvas.clipRect(0, 0, getWidth(), this.pinnedHeader.getMeasuredHeight());
        this.pinnedHeader.draw(canvas);
        canvas.restoreToCount(save2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
        if (!this.longPressCalled) {
            return super.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
        }
        OnItemLongClickListenerExtended onItemLongClickListenerExtended = this.onItemLongClickListenerExtended;
        if (onItemLongClickListenerExtended != null) {
            onItemLongClickListenerExtended.onMove(i, i2);
        }
        iArr[0] = i;
        iArr[1] = i2;
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View view;
        FastScroll fastScroll = getFastScroll();
        if (fastScroll != null && fastScroll.isVisible && fastScroll.isMoving && motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
            return true;
        }
        if (this.sectionsAdapter == null || (view = this.pinnedHeader) == null || view.getAlpha() == 0.0f || !this.pinnedHeader.dispatchTouchEvent(motionEvent)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    protected void drawItemBackground(Canvas canvas, int i, int i2, int i3) {
        int i4 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int i5 = Integer.MIN_VALUE;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt != null && getChildAdapterPosition(childAt) == i) {
                i4 = (int) childAt.getY();
                i5 = i2 <= 0 ? childAt.getHeight() + i4 : i4 + i2;
            }
        }
        if (i4 < i5) {
            if (this.backgroundPaint == null) {
                this.backgroundPaint = new Paint(1);
            }
            this.backgroundPaint.setColor(i3);
            canvas.drawRect(0.0f, i4, getWidth(), i5, this.backgroundPaint);
        }
    }

    protected void drawSectionBackground(Canvas canvas, int i, int i2, int i3) {
        drawSectionBackground(canvas, i, i2, i3, 0, 0);
    }

    protected void drawSectionBackground(Canvas canvas, int i, int i2, int i3, int i4, int i5) {
        if (i2 < i || i < 0 || i2 < 0) {
            return;
        }
        int i6 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int i7 = Integer.MIN_VALUE;
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if (childAt != null) {
                int childAdapterPosition = getChildAdapterPosition(childAt);
                int top = childAt.getTop();
                if (childAdapterPosition >= i && childAdapterPosition <= i2) {
                    i6 = Math.min(top, i6);
                    i7 = Math.max((int) (top + (childAt.getHeight() * childAt.getAlpha())), i7);
                }
            }
        }
        if (i6 < i7) {
            if (this.backgroundPaint == null) {
                this.backgroundPaint = new Paint(1);
            }
            this.backgroundPaint.setColor(i3);
            canvas.drawRect(0.0f, i6 - i4, getWidth(), i7 + i5, this.backgroundPaint);
        }
    }

    protected void drawSectionBackgroundExclusive(Canvas canvas, int i, int i2, int i3) {
        int y;
        int i4 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int i5 = Integer.MIN_VALUE;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt != null) {
                int childAdapterPosition = getChildAdapterPosition(childAt);
                if (childAdapterPosition > i && childAdapterPosition < i2) {
                    y = (int) childAt.getY();
                } else if (childAdapterPosition == i) {
                    y = ((int) childAt.getY()) + childAt.getHeight();
                } else if (childAdapterPosition == i2) {
                    i4 = Math.min((int) childAt.getY(), i4);
                    i5 = Math.max((int) childAt.getY(), i5);
                }
                i4 = Math.min(y, i4);
                i5 = Math.max(((int) childAt.getY()) + childAt.getHeight(), i5);
            }
        }
        if (i4 < i5) {
            if (this.backgroundPaint == null) {
                this.backgroundPaint = new Paint(1);
            }
            this.backgroundPaint.setColor(i3);
            canvas.drawRect(0.0f, i4, getWidth(), i5, this.backgroundPaint);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateSelectorState();
    }

    public boolean emptyViewIsVisible() {
        return (getAdapter() == null || isFastScrollAnimationRunning() || getAdapter().getItemCount() != 0) ? false : true;
    }

    protected void emptyViewUpdated(boolean z, boolean z2) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public View findChildViewUnder(float f, float f2) {
        int childCount = getChildCount();
        int i = 0;
        while (i < 2) {
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                View childAt = getChildAt(i2);
                if ((!(childAt instanceof ChatMessageCell) && !(childAt instanceof ChatActionCell)) || childAt.getVisibility() != 4) {
                    float translationX = i == 0 ? childAt.getTranslationX() : 0.0f;
                    float translationY = i == 0 ? childAt.getTranslationY() : 0.0f;
                    if (f >= childAt.getLeft() + translationX && f <= childAt.getRight() + translationX && f2 >= childAt.getTop() + translationY && f2 <= childAt.getBottom() + translationY) {
                        return childAt;
                    }
                }
            }
            i++;
        }
        return null;
    }

    public View getEmptyView() {
        return this.emptyView;
    }

    public FastScroll getFastScroll() {
        return this.fastScroll;
    }

    public ArrayList<View> getHeaders() {
        return this.headers;
    }

    public ArrayList<View> getHeadersCache() {
        return this.headersCache;
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return this.onScrollListener;
    }

    public View getPinnedHeader() {
        return this.pinnedHeader;
    }

    public View getPressedChildView() {
        return this.currentChildView;
    }

    public int[] getResourceDeclareStyleableIntArray(String str, String str2) {
        try {
            Field field = Class.forName(str + ".R$styleable").getField(str2);
            if (field != null) {
                return (int[]) field.get(null);
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    public Integer getSelectorColor(int i) {
        GenericProvider genericProvider = this.getSelectorColor;
        if (genericProvider != null) {
            return (Integer) genericProvider.provide(Integer.valueOf(i));
        }
        return null;
    }

    public Drawable getSelectorDrawable() {
        return this.selectorDrawable;
    }

    public android.graphics.Rect getSelectorRect() {
        return this.selectorRect;
    }

    protected int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    protected Drawable getThemedDrawable(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Drawable drawable = resourcesProvider != null ? resourcesProvider.getDrawable(str) : null;
        return drawable != null ? drawable : Theme.getThemeDrawable(str);
    }

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public ViewParent getTouchParent() {
        return null;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void hide() {
        if (this.isHidden) {
            return;
        }
        this.isHidden = true;
        if (getVisibility() != 8) {
            setVisibility(8);
        }
        View view = this.emptyView;
        if (view == null || view.getVisibility() == 8) {
            return;
        }
        this.emptyView.setVisibility(8);
    }

    public void hideSelector(boolean z) {
        View view = this.currentChildView;
        if (view != null) {
            onChildPressed(view, 0.0f, 0.0f, false);
            this.currentChildView = null;
            if (z) {
                removeSelection(view, null);
            }
        }
        if (z) {
            return;
        }
        this.selectorDrawable.setState(StateSet.NOTHING);
        this.selectorRect.setEmpty();
    }

    public void highlightRow(IntReturnCallback intReturnCallback) {
        highlightRowInternal(intReturnCallback, 700, true);
    }

    public void highlightRow(IntReturnCallback intReturnCallback, int i) {
        highlightRowInternal(intReturnCallback, i, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void invalidateViews() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof Theme.Colorable) {
                ((Theme.Colorable) childAt).updateColors();
            }
            childAt.invalidate();
        }
    }

    public boolean isFastScrollAnimationRunning() {
        return this.fastScrollAnimationRunning;
    }

    public boolean isMultiselect() {
        return this.multiSelectionGesture;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.selectorDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll == null || fastScroll.getParent() == getParent()) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) this.fastScroll.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this.fastScroll);
        }
        ((ViewGroup) getParent()).addView(this.fastScroll);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void onChildAttachedToWindow(View view) {
        View.AccessibilityDelegate accessibilityDelegate;
        if (getAdapter() instanceof SelectionAdapter) {
            RecyclerView.ViewHolder findContainingViewHolder = findContainingViewHolder(view);
            if (findContainingViewHolder != null) {
                view.setEnabled(((SelectionAdapter) getAdapter()).isEnabled(findContainingViewHolder));
                accessibilityDelegate = this.accessibilityEnabled ? this.accessibilityDelegate : null;
            }
            super.onChildAttachedToWindow(view);
        }
        view.setEnabled(false);
        view.setAccessibilityDelegate(accessibilityDelegate);
        super.onChildAttachedToWindow(view);
    }

    protected void onChildPressed(View view, float f, float f2, boolean z) {
        if (this.disableHighlightState || view == null) {
            return;
        }
        view.setPressed(z);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.selectorPosition = -1;
        this.selectorView = null;
        this.selectorRect.setEmpty();
        RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = this.itemsEnterAnimator;
        if (recyclerItemsEnterAnimator != null) {
            recyclerItemsEnterAnimator.onDetached();
        }
        if (this.stoppedAllHeavyOperations) {
            this.stoppedAllHeavyOperations = false;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        if (this.disallowInterceptTouchEvents) {
            requestDisallowInterceptTouchEvent(this, true);
        }
        OnInterceptTouchListener onInterceptTouchListener = this.onInterceptTouchListener;
        return (onInterceptTouchListener != null && onInterceptTouchListener.onInterceptTouchEvent(motionEvent)) || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            this.selfOnLayout = true;
            int paddingTop = i2 + (fastScroll.usePadding ? getPaddingTop() : fastScroll.topOffset);
            FastScroll fastScroll2 = this.fastScroll;
            if (fastScroll2.isRtl) {
                fastScroll2.layout(0, paddingTop, fastScroll2.getMeasuredWidth(), this.fastScroll.getMeasuredHeight() + paddingTop);
            } else {
                int measuredWidth = getMeasuredWidth() - this.fastScroll.getMeasuredWidth();
                FastScroll fastScroll3 = this.fastScroll;
                fastScroll3.layout(measuredWidth, paddingTop, fastScroll3.getMeasuredWidth() + measuredWidth, this.fastScroll.getMeasuredHeight() + paddingTop);
            }
            this.selfOnLayout = false;
        }
        checkSection(false);
        IntReturnCallback intReturnCallback = this.pendingHighlightPosition;
        if (intReturnCallback != null) {
            highlightRowInternal(intReturnCallback, 700, false);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null && fastScroll.getLayoutParams() != null) {
            FastScroll fastScroll2 = this.fastScroll;
            int measuredHeight = (getMeasuredHeight() - (fastScroll2.usePadding ? getPaddingTop() : fastScroll2.topOffset)) - getPaddingBottom();
            this.fastScroll.getLayoutParams().height = measuredHeight;
            this.fastScroll.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(132.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
        }
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        View view;
        super.onSizeChanged(i, i2, i3, i4);
        FrameLayout frameLayout = this.overlayContainer;
        if (frameLayout != null) {
            frameLayout.requestLayout();
        }
        int i5 = this.sectionsType;
        if (i5 != 1) {
            if (i5 != 2 || this.sectionsAdapter == null || (view = this.pinnedHeader) == null) {
                return;
            }
            ensurePinnedHeaderLayout(view, true);
            return;
        }
        if (this.sectionsAdapter == null || this.headers.isEmpty()) {
            return;
        }
        for (int i6 = 0; i6 < this.headers.size(); i6++) {
            ensurePinnedHeaderLayout((View) this.headers.get(i6), true);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null && fastScroll.pressed) {
            return false;
        }
        if (!this.multiSelectionGesture || motionEvent.getAction() == 0 || motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.lastX = Float.MAX_VALUE;
            this.lastY = Float.MAX_VALUE;
            this.multiSelectionGesture = false;
            this.multiSelectionGestureStarted = false;
            requestDisallowInterceptTouchEvent(this, false);
            cancelMultiselectScroll();
            return super.onTouchEvent(motionEvent);
        }
        if (this.lastX == Float.MAX_VALUE && this.lastY == Float.MAX_VALUE) {
            this.lastX = motionEvent.getX();
            this.lastY = motionEvent.getY();
        }
        if (!this.multiSelectionGestureStarted && Math.abs(motionEvent.getY() - this.lastY) > this.touchSlop) {
            this.multiSelectionGestureStarted = true;
            requestDisallowInterceptTouchEvent(this, true);
        }
        if (this.multiSelectionGestureStarted) {
            chekMultiselect(motionEvent.getX(), motionEvent.getY());
            this.multiSelectionListener.getPaddings(this.listPaddings);
            if (motionEvent.getY() > (getMeasuredHeight() - AndroidUtilities.dp(56.0f)) - this.listPaddings[1] && (this.currentSelectedPosition >= this.startSelectionFrom || !this.multiSelectionListener.limitReached())) {
                startMultiselectScroll(false);
            } else if (motionEvent.getY() >= AndroidUtilities.dp(56.0f) + this.listPaddings[0] || (this.currentSelectedPosition > this.startSelectionFrom && this.multiSelectionListener.limitReached())) {
                cancelMultiselectScroll();
            } else {
                startMultiselectScroll(true);
            }
        }
        return true;
    }

    public void relayoutPinnedHeader() {
        View view = this.pinnedHeader;
        if (view != null) {
            view.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0));
            View view2 = this.pinnedHeader;
            view2.layout(0, 0, view2.getMeasuredWidth(), this.pinnedHeader.getMeasuredHeight());
            invalidate();
        }
    }

    public void removeHighlightRow() {
        int i;
        Runnable runnable = this.removeHighlighSelectionRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.removeHighlighSelectionRunnable.run();
            this.removeHighlighSelectionRunnable = null;
            this.selectorView = null;
            return;
        }
        this.removeHighlighSelectionRunnable = null;
        this.pendingHighlightPosition = null;
        View view = this.selectorView;
        if (view != null && (i = this.highlightPosition) != -1) {
            positionSelector(i, view);
            Drawable drawable = this.selectorDrawable;
            if (drawable != null) {
                drawable.setState(new int[0]);
                invalidateDrawable(this.selectorDrawable);
            }
            this.selectorView = null;
            this.highlightPosition = -1;
            return;
        }
        Drawable drawable2 = this.selectorDrawable;
        if (drawable2 != null) {
            Drawable current = drawable2.getCurrent();
            if (current instanceof TransitionDrawable) {
                ((TransitionDrawable) current).resetTransition();
            }
        }
        Drawable drawable3 = this.selectorDrawable;
        if (drawable3 != null && drawable3.isStateful() && this.selectorDrawable.setState(StateSet.NOTHING)) {
            invalidateDrawable(this.selectorDrawable);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.fastScrollAnimationRunning) {
            return;
        }
        super.requestLayout();
    }

    public void setAccessibilityEnabled(boolean z) {
        this.accessibilityEnabled = z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter adapter2 = getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.observer);
        }
        ArrayList arrayList = this.headers;
        if (arrayList != null) {
            arrayList.clear();
            this.headersCache.clear();
        }
        this.currentFirst = -1;
        this.selectorPosition = -1;
        this.selectorView = null;
        this.selectorRect.setEmpty();
        this.pinnedHeader = null;
        this.sectionsAdapter = adapter instanceof SectionsAdapter ? (SectionsAdapter) adapter : null;
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.observer);
        }
        checkIfEmpty(false);
    }

    public void setAllowItemsInteractionDuringAnimation(boolean z) {
        this.allowItemsInteractionDuringAnimation = z;
    }

    public void setAllowStopHeaveOperations(boolean z) {
        this.allowStopHeaveOperations = z;
    }

    public void setAnimateEmptyView(boolean z, int i) {
        this.animateEmptyView = z;
        this.emptyViewAnimationType = i;
    }

    public void setDisableHighlightState(boolean z) {
        this.disableHighlightState = z;
    }

    public void setDisallowInterceptTouchEvents(boolean z) {
        this.disallowInterceptTouchEvents = z;
    }

    public void setDrawSelection(boolean z) {
        this.drawSelection = z;
    }

    public void setDrawSelectorBehind(boolean z) {
        this.drawSelectorBehind = z;
    }

    public void setEmptyView(View view) {
        View view2 = this.emptyView;
        if (view2 == view) {
            return;
        }
        if (view2 != null) {
            view2.animate().setListener(null).cancel();
        }
        this.emptyView = view;
        if (this.animateEmptyView && view != null) {
            view.setVisibility(8);
        }
        if (!this.isHidden) {
            this.emptyViewAnimateToVisibility = -1;
            checkIfEmpty(false);
            return;
        }
        View view3 = this.emptyView;
        if (view3 != null) {
            this.emptyViewAnimateToVisibility = 8;
            view3.setVisibility(8);
        }
    }

    public void setFastScrollEnabled(int i) {
        this.fastScroll = new FastScroll(getContext(), i);
        if (getParent() != null) {
            ((ViewGroup) getParent()).addView(this.fastScroll);
        }
    }

    public void setFastScrollVisible(boolean z) {
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll == null) {
            return;
        }
        fastScroll.setVisibility(z ? 0 : 8);
        this.fastScroll.isVisible = z;
    }

    public void setHideIfEmpty(boolean z) {
        this.hideIfEmpty = z;
    }

    public void setInstantClick(boolean z) {
        this.instantClick = z;
    }

    public void setItemSelectorColorProvider(GenericProvider<Integer, Integer> genericProvider) {
        this.getSelectorColor = genericProvider;
    }

    public void setItemsEnterAnimator(RecyclerItemsEnterAnimator recyclerItemsEnterAnimator) {
        this.itemsEnterAnimator = recyclerItemsEnterAnimator;
    }

    public void setListSelectorColor(Integer num) {
        Theme.setSelectorDrawableColor(this.selectorDrawable, num == null ? getThemedColor(Theme.key_listSelector) : num.intValue(), true);
    }

    public void setOnInterceptTouchListener(OnInterceptTouchListener onInterceptTouchListener) {
        this.onInterceptTouchListener = onInterceptTouchListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListenerExtended onItemClickListenerExtended) {
        this.onItemClickListenerExtended = onItemClickListenerExtended;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        setOnItemLongClickListener(onItemLongClickListener, ViewConfiguration.getLongPressTimeout());
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener, long j) {
        this.onItemLongClickListener = onItemLongClickListener;
        this.gestureDetector.setIsLongpressEnabled(onItemLongClickListener != null);
        this.gestureDetector.setLongpressDuration(j);
    }

    public void setOnItemLongClickListener(OnItemLongClickListenerExtended onItemLongClickListenerExtended) {
        setOnItemLongClickListener(onItemLongClickListenerExtended, ViewConfiguration.getLongPressTimeout());
    }

    public void setOnItemLongClickListener(OnItemLongClickListenerExtended onItemLongClickListenerExtended, long j) {
        this.onItemLongClickListenerExtended = onItemLongClickListenerExtended;
        this.gestureDetector.setIsLongpressEnabled(onItemLongClickListenerExtended != null);
        this.gestureDetector.setLongpressDuration(j);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setPinnedHeaderShadowDrawable(Drawable drawable) {
        this.pinnedHeaderShadowDrawable = drawable;
    }

    public void setPinnedSectionOffsetY(int i) {
        this.sectionOffset = i;
        invalidate();
    }

    public void setResetSelectorOnChanged(boolean z) {
        this.resetSelectorOnChanged = z;
    }

    public void setScrollEnabled(boolean z) {
        this.scrollEnabled = z;
    }

    public void setSectionsType(int i) {
        this.sectionsType = i;
        if (i == 1 || i == 3) {
            this.headers = new ArrayList();
            this.headersCache = new ArrayList();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSelectorDrawableColor(int i) {
        Drawable selectorDrawable;
        Drawable drawable;
        Drawable drawable2 = this.selectorDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        int i2 = this.selectorType;
        if (i2 == 8) {
            selectorDrawable = Theme.createRadSelectorDrawable(i, this.selectorRadius, 0);
        } else {
            if (i2 == 9) {
                this.selectorDrawable = null;
                drawable = this.selectorDrawable;
                if (drawable == null) {
                    drawable.setCallback(this);
                    return;
                }
                return;
            }
            int i3 = this.topBottomSelectorRadius;
            if (i3 > 0) {
                selectorDrawable = Theme.createRadSelectorDrawable(i, i3, i3);
            } else {
                int i4 = this.selectorRadius;
                selectorDrawable = (i4 <= 0 || i2 == 1) ? i2 == 2 ? Theme.getSelectorDrawable(i, false) : Theme.createSelectorDrawable(i, i2, i4) : Theme.createSimpleSelectorRoundRectDrawable(i4, 0, i, -16777216);
            }
        }
        this.selectorDrawable = selectorDrawable;
        drawable = this.selectorDrawable;
        if (drawable == null) {
        }
    }

    public void setSelectorRadius(int i) {
        this.selectorRadius = i;
    }

    public void setSelectorTransformer(Consumer consumer) {
        this.selectorTransformer = consumer;
    }

    public void setSelectorType(int i) {
        this.selectorType = i;
    }

    public void setTopBottomSelectorRadius(int i) {
        this.topBottomSelectorRadius = i;
    }

    public void setTranslateSelector(boolean z) {
        this.translateSelector = z ? -2 : -1;
    }

    public void setTranslateSelectorPosition(int i) {
        if (i <= 0) {
            i = -1;
        }
        this.translateSelector = i;
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            fastScroll.setTranslationY(f);
        }
    }

    @Override // android.view.View
    public void setVerticalScrollBarEnabled(boolean z) {
        if (attributes != null) {
            super.setVerticalScrollBarEnabled(z);
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            this.hiddenByEmptyView = false;
        }
    }

    public void show() {
        if (this.isHidden) {
            this.isHidden = false;
            checkIfEmpty(false);
        }
    }

    public void startMultiselect(int i, boolean z, onMultiSelectionChanged onmultiselectionchanged) {
        if (!this.multiSelectionGesture) {
            this.listPaddings = new int[2];
            this.selectedPositions = new HashSet();
            requestDisallowInterceptTouchEvent(this, true);
            this.multiSelectionListener = onmultiselectionchanged;
            this.multiSelectionGesture = true;
            this.currentSelectedPosition = i;
            this.startSelectionFrom = i;
        }
        this.useRelativePositions = z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void stopScroll() {
        try {
            super.stopScroll();
        } catch (NullPointerException unused) {
        }
    }

    protected boolean updateEmptyViewAnimated() {
        return isAttachedToWindow();
    }

    public void updateFastScrollColors() {
        FastScroll fastScroll = this.fastScroll;
        if (fastScroll != null) {
            fastScroll.updateColors();
        }
    }

    public void updateSelector() {
        View view;
        int i = this.selectorPosition;
        if (i == -1 || (view = this.selectorView) == null) {
            return;
        }
        positionSelector(i, view);
        invalidate();
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return this.selectorDrawable == drawable || super.verifyDrawable(drawable);
    }
}
