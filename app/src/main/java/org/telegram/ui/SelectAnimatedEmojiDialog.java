package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerCustom;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DrawingInBackgroundThreadDrawable;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.EmojiTabsStrip;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumButtonView;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.PremiumLockIconView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.SelectAnimatedEmojiDialog;
/* loaded from: classes3.dex */
public class SelectAnimatedEmojiDialog extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static HashMap<Integer, Parcelable> listStates = new HashMap<>();
    private Adapter adapter;
    private View animateExpandFromButton;
    AnimatedEmojiDrawable bigReactionAnimatedEmoji;
    public onLongPressedListener bigReactionListener;
    private View bottomGradientView;
    private View bubble1View;
    private View bubble2View;
    public boolean cancelPressed;
    private FrameLayout contentView;
    private View contentViewForeground;
    private ValueAnimator dimAnimator;
    private Runnable dismiss;
    public RecyclerListView emojiGridView;
    private ValueAnimator emojiSelectAnimator;
    private Rect emojiSelectRect;
    private ImageViewEmoji emojiSelectView;
    private EmojiTabsStrip emojiTabs;
    private View emojiTabsShadow;
    private Integer emojiX;
    private ValueAnimator hideAnimator;
    private boolean includeEmpty;
    private boolean isAttached;
    private GridLayoutManager layoutManager;
    private Integer listStateId;
    private int longtapHintRow;
    public onRecentClearedListener onRecentClearedListener;
    private int popularSectionRow;
    private Drawable premiumStar;
    private ColorFilter premiumStarColorFilter;
    float pressedProgress;
    private int recentReactionsEndRow;
    private int recentReactionsSectionRow;
    private int recentReactionsStartRow;
    private List<ReactionsLayoutInBubble.VisibleReaction> recentReactionsToSet;
    private Theme.ResourcesProvider resourcesProvider;
    private float scaleX;
    private float scaleY;
    private int scrimColor;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable scrimDrawable;
    private View scrimDrawableParent;
    private RecyclerAnimationScrollHelper scrollHelper;
    ImageViewEmoji selectedReactionView;
    private ValueAnimator showAnimator;
    private View topGradientView;
    private int topReactionsEndRow;
    private int topReactionsStartRow;
    private int totalCount;
    private int type;
    ArrayList<EmojiListView.DrawingInBackgroundLine> lineDrawables = new ArrayList<>();
    ArrayList<EmojiListView.DrawingInBackgroundLine> lineDrawablesTmp = new ArrayList<>();
    HashSet<ReactionsLayoutInBubble.VisibleReaction> selectedReactions = new HashSet<>();
    HashSet<Long> selectedDocumentIds = new HashSet<>();
    public Paint selectorPaint = new Paint(1);
    private int currentAccount = UserConfig.selectedAccount;
    private ArrayList<Integer> rowHashCodes = new ArrayList<>();
    private SparseIntArray positionToSection = new SparseIntArray();
    private SparseIntArray sectionToPosition = new SparseIntArray();
    private SparseIntArray positionToExpand = new SparseIntArray();
    private SparseIntArray positionToButton = new SparseIntArray();
    private ArrayList<Long> expandedEmojiSets = new ArrayList<>();
    private ArrayList<Long> installedEmojiSets = new ArrayList<>();
    private boolean recentExpanded = false;
    private ArrayList<AnimatedEmojiSpan> recent = new ArrayList<>();
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> topReactions = new ArrayList<>();
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> recentReactions = new ArrayList<>();
    private ArrayList<AnimatedEmojiSpan> defaultStatuses = new ArrayList<>();
    private ArrayList<EmojiView.EmojiPack> packs = new ArrayList<>();
    private boolean drawBackground = true;
    ImageReceiver bigReactionImageReceiver = new ImageReceiver();
    private float scrimAlpha = 1.0f;
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator(2.0f);
    private boolean bottomGradientShown = false;
    private boolean smoothScrolling = false;
    private int animateExpandFromPosition = -1;
    private int animateExpandToPosition = -1;
    private long animateExpandStartTime = -1;
    private LongSparseArray<AnimatedEmojiDrawable> animatedEmojiDrawables = new LongSparseArray<>();
    Paint paint = new Paint();

    /* loaded from: classes3.dex */
    public interface onLongPressedListener {
        void onLongPressed(ImageViewEmoji imageViewEmoji);
    }

    /* loaded from: classes3.dex */
    public interface onRecentClearedListener {
        void onRecentCleared();
    }

    protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document) {
    }

    protected void onReactionClick(ImageViewEmoji imageViewEmoji, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
    }

    public void putAnimatedEmojiToCache(AnimatedEmojiDrawable animatedEmojiDrawable) {
        this.animatedEmojiDrawables.put(animatedEmojiDrawable.getDocumentId(), animatedEmojiDrawable);
    }

    public void setSelectedReactions(HashSet<ReactionsLayoutInBubble.VisibleReaction> hashSet) {
        this.selectedReactions = hashSet;
        this.selectedDocumentIds.clear();
        ArrayList arrayList = new ArrayList(hashSet);
        for (int i = 0; i < arrayList.size(); i++) {
            if (((ReactionsLayoutInBubble.VisibleReaction) arrayList.get(i)).documentId != 0) {
                this.selectedDocumentIds.add(Long.valueOf(((ReactionsLayoutInBubble.VisibleReaction) arrayList.get(i)).documentId));
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class SelectAnimatedEmojiDialogWindow extends PopupWindow {
        private static final ViewTreeObserver.OnScrollChangedListener NOP = SelectAnimatedEmojiDialog$SelectAnimatedEmojiDialogWindow$$ExternalSyntheticLambda0.INSTANCE;
        private static final Field superListenerField;
        private ViewTreeObserver.OnScrollChangedListener mSuperScrollListener;
        private ViewTreeObserver mViewTreeObserver;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$static$0() {
        }

        static {
            Field field = null;
            try {
                field = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                field.setAccessible(true);
            } catch (NoSuchFieldException unused) {
            }
            superListenerField = field;
        }

        public SelectAnimatedEmojiDialogWindow(View view, int i, int i2) {
            super(view, i, i2);
            init();
        }

        private void init() {
            setFocusable(true);
            setAnimationStyle(0);
            setOutsideTouchable(true);
            setClippingEnabled(true);
            setInputMethodMode(2);
            setSoftInputMode(0);
            Field field = superListenerField;
            if (field != null) {
                try {
                    this.mSuperScrollListener = (ViewTreeObserver.OnScrollChangedListener) field.get(this);
                    field.set(this, NOP);
                } catch (Exception unused) {
                    this.mSuperScrollListener = null;
                }
            }
        }

        private void unregisterListener() {
            ViewTreeObserver viewTreeObserver;
            if (this.mSuperScrollListener == null || (viewTreeObserver = this.mViewTreeObserver) == null) {
                return;
            }
            if (viewTreeObserver.isAlive()) {
                this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
            }
            this.mViewTreeObserver = null;
        }

        private void registerListener(View view) {
            if (getContentView() instanceof SelectAnimatedEmojiDialog) {
                ((SelectAnimatedEmojiDialog) getContentView()).onShow(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectAnimatedEmojiDialogWindow$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow.this.dismiss();
                    }
                });
            }
            if (this.mSuperScrollListener != null) {
                ViewTreeObserver viewTreeObserver = view.getWindowToken() != null ? view.getViewTreeObserver() : null;
                ViewTreeObserver viewTreeObserver2 = this.mViewTreeObserver;
                if (viewTreeObserver == viewTreeObserver2) {
                    return;
                }
                if (viewTreeObserver2 != null && viewTreeObserver2.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = viewTreeObserver;
                if (viewTreeObserver == null) {
                    return;
                }
                viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
            }
        }

        public void dimBehind() {
            View rootView = getContentView().getRootView();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) rootView.getLayoutParams();
            layoutParams.flags |= 2;
            layoutParams.dimAmount = 0.2f;
            ((WindowManager) getContentView().getContext().getSystemService("window")).updateViewLayout(rootView, layoutParams);
        }

        private void dismissDim() {
            View rootView = getContentView().getRootView();
            WindowManager windowManager = (WindowManager) getContentView().getContext().getSystemService("window");
            if (rootView.getLayoutParams() == null || !(rootView.getLayoutParams() instanceof WindowManager.LayoutParams)) {
                return;
            }
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) rootView.getLayoutParams();
            try {
                int i = layoutParams.flags;
                if ((i & 2) == 0) {
                    return;
                }
                layoutParams.flags = i & (-3);
                layoutParams.dimAmount = 0.0f;
                windowManager.updateViewLayout(rootView, layoutParams);
            } catch (Exception unused) {
            }
        }

        @Override // android.widget.PopupWindow
        public void showAsDropDown(View view) {
            super.showAsDropDown(view);
            registerListener(view);
        }

        @Override // android.widget.PopupWindow
        public void showAsDropDown(View view, int i, int i2) {
            super.showAsDropDown(view, i, i2);
            registerListener(view);
        }

        @Override // android.widget.PopupWindow
        public void showAsDropDown(View view, int i, int i2, int i3) {
            super.showAsDropDown(view, i, i2, i3);
            registerListener(view);
        }

        @Override // android.widget.PopupWindow
        public void showAtLocation(View view, int i, int i2, int i3) {
            super.showAtLocation(view, i, i2, i3);
            unregisterListener();
        }

        @Override // android.widget.PopupWindow
        public void dismiss() {
            if (getContentView() instanceof SelectAnimatedEmojiDialog) {
                ((SelectAnimatedEmojiDialog) getContentView()).onDismiss(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectAnimatedEmojiDialogWindow$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow.this.lambda$dismiss$1();
                    }
                });
                dismissDim();
                return;
            }
            super.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dismiss$1() {
            super.dismiss();
        }
    }

    public SelectAnimatedEmojiDialog(Context context, boolean z, Integer num, final int i, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i2;
        this.includeEmpty = false;
        this.resourcesProvider = resourcesProvider;
        this.type = i;
        this.includeEmpty = z;
        this.selectorPaint.setColor(Theme.getColor("listSelectorSDK21", resourcesProvider));
        this.premiumStarColorFilter = new PorterDuffColorFilter(ColorUtils.setAlphaComponent(Theme.getColor("chats_menuItemIcon", resourcesProvider), 178), PorterDuff.Mode.MULTIPLY);
        this.emojiX = num;
        final Integer valueOf = num == null ? null : Integer.valueOf(MathUtils.clamp(num.intValue(), AndroidUtilities.dp(26.0f), AndroidUtilities.dp(324.0f)));
        boolean z2 = valueOf != null && valueOf.intValue() > AndroidUtilities.dp(170.0f);
        setFocusableInTouchMode(true);
        if (i == 0) {
            setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda6
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$new$0;
                    lambda$new$0 = SelectAnimatedEmojiDialog.this.lambda$new$0(view, motionEvent);
                    return lambda$new$0;
                }
            });
            i2 = 16;
        } else {
            i2 = 0;
        }
        if (valueOf != null) {
            this.bubble1View = new View(context);
            Drawable mutate = getResources().getDrawable(R.drawable.shadowed_bubble1).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider), PorterDuff.Mode.MULTIPLY));
            this.bubble1View.setBackground(mutate);
            addView(this.bubble1View, LayoutHelper.createFrame(10, 10.0f, 51, (valueOf.intValue() / AndroidUtilities.density) + (z2 ? -12 : 4), i2, 0.0f, 0.0f));
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.1
            private Path path = new Path();
            private Paint paint = new Paint(1);

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (!SelectAnimatedEmojiDialog.this.drawBackground) {
                    super.dispatchDraw(canvas);
                    return;
                }
                canvas.save();
                this.paint.setShadowLayer(AndroidUtilities.dp(2.0f), 0.0f, AndroidUtilities.dp(-0.66f), 503316480);
                this.paint.setColor(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider));
                this.paint.setAlpha((int) (getAlpha() * 255.0f));
                Integer num2 = valueOf;
                float width = (num2 == null ? getWidth() / 2.0f : num2.intValue()) + AndroidUtilities.dp(20.0f);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(getPaddingLeft() + (width - (SelectAnimatedEmojiDialog.this.scaleX * width)), getPaddingTop(), getPaddingLeft() + width + ((((getWidth() - getPaddingLeft()) - getPaddingRight()) - width) * SelectAnimatedEmojiDialog.this.scaleX), getPaddingTop() + (((getHeight() - getPaddingBottom()) - getPaddingTop()) * SelectAnimatedEmojiDialog.this.scaleY));
                this.path.rewind();
                this.path.addRoundRect(rectF, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), Path.Direction.CW);
                canvas.drawPath(this.path, this.paint);
                canvas.clipPath(this.path);
                super.dispatchDraw(canvas);
                canvas.restore();
            }
        };
        this.contentView = frameLayout;
        if (i == 0) {
            frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        }
        addView(this.contentView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, i == 0 ? i2 + 6 : 0.0f, 0.0f, 0.0f));
        if (valueOf != null) {
            this.bubble2View = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.2
                @Override // android.view.View
                protected void onMeasure(int i3, int i4) {
                    super.onMeasure(i3, i4);
                    setPivotX(getMeasuredWidth() / 2);
                    setPivotY(getMeasuredHeight());
                }
            };
            Drawable drawable = getResources().getDrawable(R.drawable.shadowed_bubble2_half);
            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider), PorterDuff.Mode.MULTIPLY));
            this.bubble2View.setBackground(drawable);
            addView(this.bubble2View, LayoutHelper.createFrame(17, 9.0f, 51, (valueOf.intValue() / AndroidUtilities.density) + (z2 ? -25 : 10), i2 + 5, 0.0f, 0.0f));
        }
        final Integer num2 = valueOf;
        EmojiTabsStrip emojiTabsStrip = new EmojiTabsStrip(context, null, false, true, null) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.3
            @Override // org.telegram.ui.Components.EmojiTabsStrip
            protected boolean onTabClick(int i3) {
                int i4 = 0;
                if (SelectAnimatedEmojiDialog.this.smoothScrolling) {
                    return false;
                }
                if (i3 > 0) {
                    int i5 = i3 - 1;
                    if (SelectAnimatedEmojiDialog.this.sectionToPosition.indexOfKey(i5) >= 0) {
                        i4 = SelectAnimatedEmojiDialog.this.sectionToPosition.get(i5);
                    }
                }
                SelectAnimatedEmojiDialog.this.scrollToPosition(i4, AndroidUtilities.dp(-2.0f));
                SelectAnimatedEmojiDialog.this.emojiTabs.select(i3);
                return true;
            }

            @Override // org.telegram.ui.Components.EmojiTabsStrip
            protected void onTabCreate(EmojiTabsStrip.EmojiTabButton emojiTabButton) {
                if (SelectAnimatedEmojiDialog.this.showAnimator == null || SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                    emojiTabButton.setScaleX(0.0f);
                    emojiTabButton.setScaleY(0.0f);
                }
            }
        };
        this.emojiTabs = emojiTabsStrip;
        emojiTabsStrip.updateButtonDrawables = false;
        emojiTabsStrip.setAnimatedEmojiCacheType(5);
        EmojiTabsStrip emojiTabsStrip2 = this.emojiTabs;
        emojiTabsStrip2.animateAppear = num2 == null;
        emojiTabsStrip2.setPaddingLeft(5.0f);
        this.contentView.addView(this.emojiTabs, LayoutHelper.createFrame(-1, 36.0f));
        View view = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.4
            @Override // android.view.View
            protected void onMeasure(int i3, int i4) {
                super.onMeasure(i3, i4);
                Integer num3 = num2;
                if (num3 != null) {
                    setPivotX(num3.intValue());
                }
            }
        };
        this.emojiTabsShadow = view;
        view.setBackgroundColor(Theme.getColor("divider", resourcesProvider));
        this.contentView.addView(this.emojiTabsShadow, LayoutHelper.createFrame(-1, 1.0f / AndroidUtilities.density, 48, 0.0f, 36.0f, 0.0f, 0.0f));
        this.emojiGridView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.5
            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i3, int i4) {
                super.onScrolled(i3, i4);
                SelectAnimatedEmojiDialog.this.checkScroll();
                if (!SelectAnimatedEmojiDialog.this.smoothScrolling) {
                    SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                    selectAnimatedEmojiDialog.updateTabsPosition(selectAnimatedEmojiDialog.layoutManager.findFirstCompletelyVisibleItemPosition());
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrollStateChanged(int i3) {
                if (i3 == 0) {
                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                }
                super.onScrollStateChanged(i3);
            }
        };
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.6
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            protected float animateByScale(View view2) {
                return view2 instanceof EmojiPackExpand ? 0.6f : 0.0f;
            }
        };
        defaultItemAnimator.setAddDuration(220L);
        defaultItemAnimator.setMoveDuration(260L);
        defaultItemAnimator.setChangeDuration(160L);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setMoveInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        this.emojiGridView.setItemAnimator(defaultItemAnimator);
        this.emojiGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(2.0f));
        this.emojiGridView.setClipToPadding(false);
        RecyclerListView recyclerListView = this.emojiGridView;
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        RecyclerListView recyclerListView2 = this.emojiGridView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 8) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i3) {
                try {
                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7.1
                        @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                        public void onEnd() {
                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                        }
                    };
                    linearSmoothScrollerCustom.setTargetPosition(i3);
                    startSmoothScroll(linearSmoothScrollerCustom);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        };
        this.layoutManager = gridLayoutManager;
        recyclerListView2.setLayoutManager(gridLayoutManager);
        this.emojiGridView.setSelectorRadius(AndroidUtilities.dp(4.0f));
        this.emojiGridView.setSelectorDrawableColor(Theme.getColor("listSelectorSDK21", resourcesProvider));
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.8
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i3) {
                if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i3) >= 0 || SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i3) >= 0 || i3 == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i3 == SelectAnimatedEmojiDialog.this.popularSectionRow || i3 == SelectAnimatedEmojiDialog.this.longtapHintRow) {
                    return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.contentView.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, (1.0f / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
        RecyclerAnimationScrollHelper recyclerAnimationScrollHelper = new RecyclerAnimationScrollHelper(this.emojiGridView, this.layoutManager);
        this.scrollHelper = recyclerAnimationScrollHelper;
        recyclerAnimationScrollHelper.setAnimationCallback(new RecyclerAnimationScrollHelper.AnimationCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9
            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
            public void onPreAnimation() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = true;
            }

            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
            public void onEndAnimation() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = false;
            }
        });
        this.emojiGridView.setOnItemLongClickListener(new 10(i, z));
        this.emojiGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i3) {
                SelectAnimatedEmojiDialog.this.lambda$new$1(i, view2, i3);
            }
        });
        this.topGradientView = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.11
            @Override // android.view.View
            protected void onMeasure(int i3, int i4) {
                super.onMeasure(i3, i4);
                Integer num3 = num2;
                if (num3 != null) {
                    setPivotX(num3.intValue());
                }
            }
        };
        Drawable drawable2 = getResources().getDrawable(R.drawable.gradient_top);
        drawable2.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
        this.topGradientView.setBackground(drawable2);
        this.topGradientView.setAlpha(0.0f);
        this.contentView.addView(this.topGradientView, LayoutHelper.createFrame(-1, 20.0f, 55, 0.0f, (1.0f / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
        this.bottomGradientView = new View(context);
        Drawable drawable3 = getResources().getDrawable(R.drawable.gradient_bottom);
        drawable3.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
        this.bottomGradientView.setBackground(drawable3);
        this.bottomGradientView.setAlpha(0.0f);
        this.contentView.addView(this.bottomGradientView, LayoutHelper.createFrame(-1, 20, 87));
        View view2 = new View(context);
        this.contentViewForeground = view2;
        view2.setAlpha(0.0f);
        this.contentViewForeground.setBackgroundColor(-16777216);
        this.contentView.addView(this.contentViewForeground, LayoutHelper.createFrame(-1, -1.0f));
        Emoji.loadRecentEmoji();
        MediaDataController.getInstance(UserConfig.selectedAccount).checkStickers(5);
        MediaDataController.getInstance(UserConfig.selectedAccount).fetchEmojiStatuses(0, true);
        MediaDataController.getInstance(UserConfig.selectedAccount).checkReactions();
        this.bigReactionImageReceiver.setLayerNum(7);
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        Runnable runnable;
        if (motionEvent.getAction() != 0 || (runnable = this.dismiss) == null) {
            return false;
        }
        runnable.run();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 10 implements RecyclerListView.OnItemLongClickListenerExtended {
        final /* synthetic */ boolean val$includeEmpty;
        final /* synthetic */ int val$type;

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public /* synthetic */ void onMove(float f, float f2) {
            RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onMove(this, f, f2);
        }

        10(int i, boolean z) {
            this.val$type = i;
            this.val$includeEmpty = z;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, int i, float f, float f2) {
            if (!(view instanceof ImageViewEmoji) || this.val$type != 1) {
                int spanCount = SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 5;
                if (SelectAnimatedEmojiDialog.this.recent.size() <= spanCount || SelectAnimatedEmojiDialog.this.recentExpanded) {
                    spanCount = SelectAnimatedEmojiDialog.this.recent.size();
                }
                boolean z = this.val$includeEmpty;
                if (i - (z ? 1 : 0) >= spanCount || (i == 0 && z)) {
                    return false;
                }
                SelectAnimatedEmojiDialog.this.onRecentLongClick();
                return true;
            }
            SelectAnimatedEmojiDialog.this.performHapticFeedback(0);
            ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
            if (!imageViewEmoji.isDefaultReaction && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                TLRPC$Document tLRPC$Document = imageViewEmoji.span.document;
                if (tLRPC$Document == null) {
                    tLRPC$Document = AnimatedEmojiDrawable.findDocument(SelectAnimatedEmojiDialog.this.currentAccount, imageViewEmoji.span.documentId);
                }
                SelectAnimatedEmojiDialog.this.onEmojiSelected(imageViewEmoji, Long.valueOf(imageViewEmoji.span.documentId), tLRPC$Document);
                return true;
            }
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            selectAnimatedEmojiDialog.selectedReactionView = imageViewEmoji;
            selectAnimatedEmojiDialog.pressedProgress = 0.0f;
            selectAnimatedEmojiDialog.cancelPressed = false;
            if (imageViewEmoji.isDefaultReaction) {
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(selectAnimatedEmojiDialog.currentAccount).getReactionsMap().get(SelectAnimatedEmojiDialog.this.selectedReactionView.reaction.emojicon);
                if (tLRPC$TL_availableReaction != null) {
                    SelectAnimatedEmojiDialog.this.bigReactionImageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_pcache", null, null, null, 0L, "tgs", SelectAnimatedEmojiDialog.this.selectedReactionView.reaction, 0);
                }
            } else {
                selectAnimatedEmojiDialog.setBigReactionAnimatedEmoji(new AnimatedEmojiDrawable(4, SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.selectedReactionView.span.documentId));
            }
            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
            return true;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            if (selectAnimatedEmojiDialog.selectedReactionView != null) {
                selectAnimatedEmojiDialog.cancelPressed = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(selectAnimatedEmojiDialog.pressedProgress, 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$10$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SelectAnimatedEmojiDialog.10.this.lambda$onLongClickRelease$0(valueAnimator);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                        selectAnimatedEmojiDialog2.selectedReactionView.bigReactionSelectedProgress = 0.0f;
                        selectAnimatedEmojiDialog2.selectedReactionView = null;
                        selectAnimatedEmojiDialog2.emojiGridView.invalidate();
                    }
                });
                ofFloat.setDuration(150L);
                ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLongClickRelease$0(ValueAnimator valueAnimator) {
            SelectAnimatedEmojiDialog.this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, View view, int i2) {
        try {
            if (view instanceof ImageViewEmoji) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                if (imageViewEmoji.isDefaultReaction) {
                    onReactionClick(imageViewEmoji, imageViewEmoji.reaction);
                } else {
                    onEmojiClick(imageViewEmoji, imageViewEmoji.span);
                }
                if (i == 1) {
                    return;
                }
                performHapticFeedback(3, 1);
            } else if (view instanceof ImageView) {
                onEmojiClick(view, null);
                if (i == 1) {
                    return;
                }
                performHapticFeedback(3, 1);
            } else if (!(view instanceof EmojiPackExpand)) {
                if (view == null) {
                    return;
                }
                view.callOnClick();
            } else {
                expand(i2, (EmojiPackExpand) view);
                if (i == 1) {
                    return;
                }
                performHapticFeedback(3, 1);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBigReactionAnimatedEmoji(AnimatedEmojiDrawable animatedEmojiDrawable) {
        AnimatedEmojiDrawable animatedEmojiDrawable2;
        if (this.isAttached && (animatedEmojiDrawable2 = this.bigReactionAnimatedEmoji) != animatedEmojiDrawable) {
            if (animatedEmojiDrawable2 != null) {
                animatedEmojiDrawable2.removeView(this);
            }
            this.bigReactionAnimatedEmoji = animatedEmojiDrawable;
            if (animatedEmojiDrawable == null) {
                return;
            }
            animatedEmojiDrawable.addView(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecentLongClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), null);
        builder.setTitle(LocaleController.getString("ClearRecentEmojiStatusesTitle", R.string.ClearRecentEmojiStatusesTitle));
        builder.setMessage(LocaleController.getString("ClearRecentEmojiStatusesText", R.string.ClearRecentEmojiStatusesText));
        builder.setPositiveButton(LocaleController.getString("Clear", R.string.Clear).toUpperCase(), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$2(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setDimEnabled(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda5
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$3(dialogInterface);
            }
        });
        builder.show();
        setDim(1.0f, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecentLongClick$2(DialogInterface dialogInterface, int i) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_account_clearRecentEmojiStatuses
            public static int constructor = 404757166;

            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i2, boolean z) {
                return TLRPC$Bool.TLdeserialize(abstractSerializedData, i2, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(constructor);
            }
        }, null);
        MediaDataController.getInstance(this.currentAccount).clearRecentEmojiStatuses();
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecentLongClick$3(DialogInterface dialogInterface) {
        setDim(0.0f, true);
    }

    private void setDim(float f, boolean z) {
        ValueAnimator valueAnimator = this.dimAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.dimAnimator = null;
        }
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.contentViewForeground.getAlpha(), f * 0.25f);
            this.dimAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.this.lambda$setDim$4(valueAnimator2);
                }
            });
            this.dimAnimator.setDuration(200L);
            this.dimAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.dimAnimator.start();
            return;
        }
        this.contentViewForeground.setAlpha(f * 0.25f);
        int blendOver = Theme.blendOver(Theme.getColor("actionBarDefaultSubmenuBackground", this.resourcesProvider), ColorUtils.setAlphaComponent(-16777216, (int) (f * 255.0f * 0.25f)));
        View view = this.bubble1View;
        if (view != null) {
            view.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
        }
        View view2 = this.bubble2View;
        if (view2 == null) {
            return;
        }
        view2.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDim$4(ValueAnimator valueAnimator) {
        this.contentViewForeground.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        int blendOver = Theme.blendOver(Theme.getColor("actionBarDefaultSubmenuBackground", this.resourcesProvider), ColorUtils.setAlphaComponent(-16777216, (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f)));
        View view = this.bubble1View;
        if (view != null) {
            view.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
        }
        View view2 = this.bubble2View;
        if (view2 != null) {
            view2.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTabsPosition(int i) {
        if (i != -1) {
            int spanCount = this.layoutManager.getSpanCount() * 5;
            if (this.recent.size() <= spanCount || this.recentExpanded) {
                spanCount = this.recent.size() + (this.includeEmpty ? 1 : 0);
            }
            if (i <= spanCount || i <= this.recentReactions.size()) {
                this.emojiTabs.select(0);
                return;
            }
            int spanCount2 = this.layoutManager.getSpanCount() * 3;
            for (int i2 = 0; i2 < this.positionToSection.size(); i2++) {
                int keyAt = this.positionToSection.keyAt(i2);
                int i3 = i2 - (!this.defaultStatuses.isEmpty() ? 1 : 0);
                EmojiView.EmojiPack emojiPack = i3 >= 0 ? this.packs.get(i3) : null;
                if (emojiPack != null) {
                    boolean z = emojiPack.expanded;
                    int size = emojiPack.documents.size();
                    if (!z) {
                        size = Math.min(spanCount2, size);
                    }
                    if (i > keyAt && i <= keyAt + 1 + size) {
                        this.emojiTabs.select(i2 + 1);
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getPremiumStar() {
        if (this.premiumStar == null) {
            Drawable mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_settings_premium).mutate();
            this.premiumStar = mutate;
            mutate.setColorFilter(this.premiumStarColorFilter);
        }
        return this.premiumStar;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.scrimDrawable != null && this.emojiX != null) {
            canvas.save();
            Rect bounds = this.scrimDrawable.getBounds();
            canvas.translate((-bounds.centerX()) + this.emojiX.intValue(), (((-bounds.top) - (bounds.height() / 2.0f)) - getTranslationY()) + AndroidUtilities.dp(16.0f));
            int alpha = Build.VERSION.SDK_INT >= 19 ? this.scrimDrawable.getAlpha() : 255;
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.scrimDrawable;
            double d = alpha;
            double pow = Math.pow(this.contentView.getAlpha(), 0.25d);
            Double.isNaN(d);
            double d2 = d * pow;
            double d3 = this.scrimAlpha;
            Double.isNaN(d3);
            swapAnimatedEmojiDrawable.setAlpha((int) (d2 * d3));
            View view = this.scrimDrawableParent;
            float scaleY = view == null ? 1.0f : view.getScaleY();
            this.scrimDrawable.setBounds((int) (bounds.centerX() - ((bounds.width() / 2.0f) * scaleY)), bounds.bottom - ((int) (bounds.height() * scaleY)), (int) (bounds.centerX() + ((bounds.width() / 2.0f) * scaleY)), bounds.bottom);
            this.scrimDrawable.draw(canvas);
            this.scrimDrawable.setAlpha(alpha);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
        if (this.emojiSelectView == null || this.emojiSelectRect == null) {
            return;
        }
        canvas.save();
        canvas.translate(0.0f, -getTranslationY());
        this.emojiSelectView.drawable.setAlpha(255);
        this.emojiSelectView.drawable.setBounds(this.emojiSelectRect);
        this.emojiSelectView.drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(Theme.getColor("premiumGradient1", this.resourcesProvider), this.scrimColor, 1.0f - this.scrimAlpha), PorterDuff.Mode.MULTIPLY));
        this.emojiSelectView.drawable.draw(canvas);
        canvas.restore();
    }

    public void animateEmojiSelect(final ImageViewEmoji imageViewEmoji, final Runnable runnable) {
        if (this.emojiSelectAnimator != null || this.scrimDrawable == null) {
            runnable.run();
            return;
        }
        imageViewEmoji.notDraw = true;
        final Rect rect = new Rect();
        rect.set(this.contentView.getLeft() + this.emojiGridView.getLeft() + imageViewEmoji.getLeft(), this.contentView.getTop() + this.emojiGridView.getTop() + imageViewEmoji.getTop(), this.contentView.getLeft() + this.emojiGridView.getLeft() + imageViewEmoji.getRight(), this.contentView.getTop() + this.emojiGridView.getTop() + imageViewEmoji.getBottom());
        final Rect rect2 = new Rect();
        rect2.set(this.scrimDrawable.getBounds());
        rect2.offset((-rect2.centerX()) + this.emojiX.intValue(), (int) (((-rect2.top) - (rect2.height() / 2.0f)) + AndroidUtilities.dp(16.0f)));
        View view = this.scrimDrawableParent;
        float scaleY = view == null ? 1.0f : view.getScaleY();
        rect2.set((int) (rect2.centerX() - ((rect2.width() / 2.0f) * scaleY)), rect2.bottom - ((int) (rect2.height() * scaleY)), (int) (rect2.centerX() + ((rect2.width() / 2.0f) * scaleY)), rect2.bottom);
        Drawable drawable = imageViewEmoji.drawable;
        final AnimatedEmojiDrawable make = drawable instanceof AnimatedEmojiDrawable ? AnimatedEmojiDrawable.make(this.currentAccount, 7, ((AnimatedEmojiDrawable) drawable).getDocumentId()) : null;
        this.emojiSelectView = imageViewEmoji;
        Rect rect3 = new Rect();
        this.emojiSelectRect = rect3;
        rect3.set(rect);
        final boolean[] zArr = new boolean[1];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.emojiSelectAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SelectAnimatedEmojiDialog.this.lambda$animateEmojiSelect$5(rect, rect2, imageViewEmoji, zArr, runnable, make, valueAnimator);
            }
        });
        this.emojiSelectAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.12
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectAnimatedEmojiDialog.this.emojiSelectView = null;
                SelectAnimatedEmojiDialog.this.invalidate();
                boolean[] zArr2 = zArr;
                if (!zArr2[0]) {
                    zArr2[0] = true;
                    runnable.run();
                }
            }
        });
        this.emojiSelectAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.emojiSelectAnimator.setDuration(260L);
        this.emojiSelectAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEmojiSelect$5(Rect rect, Rect rect2, ImageViewEmoji imageViewEmoji, boolean[] zArr, Runnable runnable, AnimatedEmojiDrawable animatedEmojiDrawable, ValueAnimator valueAnimator) {
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.scrimAlpha = 1.0f - ((floatValue * floatValue) * floatValue);
        AndroidUtilities.lerp(rect, rect2, floatValue, this.emojiSelectRect);
        float max = Math.max(1.0f, this.overshootInterpolator.getInterpolation(MathUtils.clamp((3.0f * floatValue) - 2.0f, 0.0f, 1.0f))) * imageViewEmoji.getScaleX();
        Rect rect3 = this.emojiSelectRect;
        rect3.set((int) (rect3.centerX() - ((this.emojiSelectRect.width() / 2.0f) * max)), (int) (this.emojiSelectRect.centerY() - ((this.emojiSelectRect.height() / 2.0f) * max)), (int) (this.emojiSelectRect.centerX() + ((this.emojiSelectRect.width() / 2.0f) * max)), (int) (this.emojiSelectRect.centerY() + ((this.emojiSelectRect.height() / 2.0f) * max)));
        invalidate();
        if (floatValue <= 0.85f || zArr[0]) {
            return;
        }
        zArr[0] = true;
        runnable.run();
        if (animatedEmojiDrawable == null || (swapAnimatedEmojiDrawable = this.scrimDrawable) == null) {
            return;
        }
        swapAnimatedEmojiDrawable.play();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkScroll() {
        boolean canScrollVertically = this.emojiGridView.canScrollVertically(1);
        if (canScrollVertically != this.bottomGradientShown) {
            this.bottomGradientShown = canScrollVertically;
            this.bottomGradientView.animate().alpha(canScrollVertically ? 1.0f : 0.0f).setDuration(200L).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToPosition(int i, int i2) {
        View findViewByPosition = this.layoutManager.findViewByPosition(i);
        int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
        if ((findViewByPosition == null && Math.abs(i - findFirstVisibleItemPosition) > this.layoutManager.getSpanCount() * 9.0f) || !SharedConfig.animationsEnabled()) {
            this.scrollHelper.setScrollDirection(this.layoutManager.findFirstVisibleItemPosition() < i ? 0 : 1);
            this.scrollHelper.scrollToPosition(i, i2, false, true);
            return;
        }
        LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(this.emojiGridView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.13
            @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
            public void onEnd() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = false;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom, androidx.recyclerview.widget.RecyclerView.SmoothScroller
            protected void onStart() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = true;
            }
        };
        linearSmoothScrollerCustom.setTargetPosition(i);
        linearSmoothScrollerCustom.setOffset(i2);
        this.layoutManager.startSmoothScroll(linearSmoothScrollerCustom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        private Adapter() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return (viewHolder.getItemViewType() == 0 || viewHolder.getItemViewType() == 5 || viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 6) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView textView;
            if (i == 0) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                textView = new HeaderView(selectAnimatedEmojiDialog, selectAnimatedEmojiDialog.getContext());
            } else if (i == 2) {
                textView = new ImageView(SelectAnimatedEmojiDialog.this.getContext());
            } else if (i == 3) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                textView = new ImageViewEmoji(selectAnimatedEmojiDialog2.getContext());
            } else if (i == 4) {
                textView = new EmojiPackExpand(SelectAnimatedEmojiDialog.this.getContext(), null);
            } else if (i == 5) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog3 = SelectAnimatedEmojiDialog.this;
                textView = new EmojiPackButton(selectAnimatedEmojiDialog3, selectAnimatedEmojiDialog3.getContext());
            } else if (i == 6) {
                TextView textView2 = new TextView(this, SelectAnimatedEmojiDialog.this.getContext()) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.Adapter.1
                    @Override // android.widget.TextView, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(AndroidUtilities.dp(26.0f)), 1073741824));
                    }
                };
                textView2.setTextSize(1, 13.0f);
                textView2.setText(LocaleController.getString("ReactionsLongtapHint", R.string.ReactionsLongtapHint));
                textView2.setGravity(17);
                textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText", SelectAnimatedEmojiDialog.this.resourcesProvider));
                textView = textView2;
            } else {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog4 = SelectAnimatedEmojiDialog.this;
                textView = new ImageViewEmoji(selectAnimatedEmojiDialog4.getContext());
            }
            if (SelectAnimatedEmojiDialog.this.showAnimator != null && SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                textView.setScaleX(0.0f);
                textView.setScaleY(0.0f);
            }
            return new RecyclerListView.Holder(textView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < SelectAnimatedEmojiDialog.this.recentReactionsStartRow || i >= SelectAnimatedEmojiDialog.this.recentReactionsEndRow) {
                if (i >= SelectAnimatedEmojiDialog.this.topReactionsStartRow && i < SelectAnimatedEmojiDialog.this.topReactionsEndRow) {
                    return 3;
                }
                if (SelectAnimatedEmojiDialog.this.positionToExpand.indexOfKey(i) >= 0) {
                    return 4;
                }
                if (SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i) >= 0) {
                    return 5;
                }
                if (i == SelectAnimatedEmojiDialog.this.longtapHintRow) {
                    return 6;
                }
                return (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i) >= 0 || i == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i == SelectAnimatedEmojiDialog.this.popularSectionRow) ? 0 : 1;
            }
            return 3;
        }

        /* JADX WARN: Code restructure failed: missing block: B:106:0x033f, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L107;
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x0341, code lost:
            r7 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:118:0x0382, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L107;
         */
        /* JADX WARN: Code restructure failed: missing block: B:147:0x0403, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L107;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Document tLRPC$Document;
            int i2;
            boolean z;
            final EmojiView.EmojiPack emojiPack;
            int itemViewType = viewHolder.getItemViewType();
            if (SelectAnimatedEmojiDialog.this.showAnimator == null || !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
            if (itemViewType == 6) {
                return;
            }
            EmojiView.EmojiPack emojiPack2 = null;
            boolean z2 = false;
            if (itemViewType == 0) {
                HeaderView headerView = (HeaderView) viewHolder.itemView;
                if (i == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow) {
                    headerView.setText(LocaleController.getString("RecentlyUsed", R.string.RecentlyUsed), false);
                    headerView.closeIcon.setVisibility(0);
                    headerView.closeIcon.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$Adapter$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            SelectAnimatedEmojiDialog.Adapter.this.lambda$onBindViewHolder$0(view);
                        }
                    });
                    return;
                }
                headerView.closeIcon.setVisibility(8);
                if (i != SelectAnimatedEmojiDialog.this.popularSectionRow) {
                    int i3 = SelectAnimatedEmojiDialog.this.positionToSection.get(i);
                    if (i3 >= 0) {
                        EmojiView.EmojiPack emojiPack3 = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i3);
                        String str = emojiPack3.set.title;
                        if (!emojiPack3.free && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                            z2 = true;
                        }
                        headerView.setText(str, z2);
                        return;
                    }
                    headerView.setText(null, false);
                    return;
                }
                headerView.setText(LocaleController.getString("PopularReactions", R.string.PopularReactions), false);
            } else if (itemViewType == 3) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) viewHolder.itemView;
                imageViewEmoji.position = i;
                ReactionsLayoutInBubble.VisibleReaction visibleReaction = (i < SelectAnimatedEmojiDialog.this.recentReactionsStartRow || i >= SelectAnimatedEmojiDialog.this.recentReactionsEndRow) ? (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.topReactions.get(i - SelectAnimatedEmojiDialog.this.topReactionsStartRow) : (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.recentReactions.get(i - SelectAnimatedEmojiDialog.this.recentReactionsStartRow);
                if (imageViewEmoji.imageReceiver == null) {
                    ImageReceiver imageReceiver = new ImageReceiver();
                    imageViewEmoji.imageReceiver = imageReceiver;
                    imageReceiver.setLayerNum(7);
                    imageViewEmoji.imageReceiver.onAttachedToWindow();
                }
                imageViewEmoji.reaction = visibleReaction;
                imageViewEmoji.setViewSelected(SelectAnimatedEmojiDialog.this.selectedReactions.contains(visibleReaction));
                if (visibleReaction.emojicon != null) {
                    imageViewEmoji.isDefaultReaction = true;
                    TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).getReactionsMap().get(visibleReaction.emojicon);
                    if (tLRPC$TL_availableReaction != null) {
                        imageViewEmoji.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_pcache", null, null, DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.activate_animation, "windowBackgroundWhiteGrayIcon", 0.2f), 0L, "tgs", visibleReaction, 0);
                    } else {
                        imageViewEmoji.imageReceiver.clearImage();
                    }
                    imageViewEmoji.span = null;
                    PremiumLockIconView premiumLockIconView = imageViewEmoji.premiumLockIconView;
                    if (premiumLockIconView == null) {
                        return;
                    }
                    premiumLockIconView.setVisibility(8);
                    imageViewEmoji.premiumLockIconView.setImageReceiver(null);
                    return;
                }
                imageViewEmoji.isDefaultReaction = false;
                imageViewEmoji.span = new AnimatedEmojiSpan(visibleReaction.documentId, (Paint.FontMetricsInt) null);
                imageViewEmoji.imageReceiver.clearImage();
                if (UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                    return;
                }
                if (imageViewEmoji.premiumLockIconView == null) {
                    PremiumLockIconView premiumLockIconView2 = new PremiumLockIconView(SelectAnimatedEmojiDialog.this.getContext(), PremiumLockIconView.TYPE_STICKERS_PREMIUM_LOCKED);
                    imageViewEmoji.premiumLockIconView = premiumLockIconView2;
                    imageViewEmoji.addView(premiumLockIconView2, LayoutHelper.createFrame(12, 12, 85));
                }
                imageViewEmoji.premiumLockIconView.setVisibility(0);
            } else if (itemViewType == 4) {
                EmojiPackExpand emojiPackExpand = (EmojiPackExpand) viewHolder.itemView;
                int i4 = SelectAnimatedEmojiDialog.this.positionToExpand.get(i);
                if (i4 >= 0 && i4 < SelectAnimatedEmojiDialog.this.packs.size()) {
                    emojiPack2 = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i4);
                }
                if (i4 == -1) {
                    TextView textView = emojiPackExpand.textView;
                    textView.setText("+" + ((SelectAnimatedEmojiDialog.this.recent.size() - (SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 5)) + (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0) + 1));
                } else if (emojiPack2 != null) {
                    TextView textView2 = emojiPackExpand.textView;
                    textView2.setText("+" + ((emojiPack2.documents.size() - (SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 3)) + 1));
                }
            } else if (itemViewType == 5) {
                EmojiPackButton emojiPackButton = (EmojiPackButton) viewHolder.itemView;
                final int i5 = SelectAnimatedEmojiDialog.this.positionToButton.get(i);
                if (i5 < 0 || i5 >= SelectAnimatedEmojiDialog.this.packs.size() || (emojiPack = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i5)) == null) {
                    return;
                }
                String str2 = emojiPack.set.title;
                if (!emojiPack.free && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                    z2 = true;
                }
                emojiPackButton.set(str2, z2, emojiPack.installed, new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$Adapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        SelectAnimatedEmojiDialog.Adapter.this.lambda$onBindViewHolder$1(emojiPack, i5, view);
                    }
                });
            } else {
                ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) viewHolder.itemView;
                imageViewEmoji2.empty = false;
                imageViewEmoji2.position = i;
                imageViewEmoji2.setPadding(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                int spanCount = SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 5;
                int spanCount2 = SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 3;
                if (SelectAnimatedEmojiDialog.this.recent.size() <= spanCount || SelectAnimatedEmojiDialog.this.recentExpanded) {
                    spanCount = SelectAnimatedEmojiDialog.this.recent.size() + (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0);
                }
                if (SelectAnimatedEmojiDialog.this.includeEmpty && i == 0) {
                    z = SelectAnimatedEmojiDialog.this.selectedDocumentIds.isEmpty();
                    imageViewEmoji2.empty = true;
                    imageViewEmoji2.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
                    imageViewEmoji2.span = null;
                } else if (i < spanCount) {
                    AnimatedEmojiSpan animatedEmojiSpan = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.recent.get(i - (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0));
                    imageViewEmoji2.span = animatedEmojiSpan;
                    if (animatedEmojiSpan != null) {
                    }
                    z = z2;
                } else if (SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty() || (i2 = (i - spanCount) - 1) < 0 || i2 >= SelectAnimatedEmojiDialog.this.defaultStatuses.size()) {
                    for (int i6 = 0; i6 < SelectAnimatedEmojiDialog.this.positionToSection.size(); i6++) {
                        int keyAt = SelectAnimatedEmojiDialog.this.positionToSection.keyAt(i6);
                        int i7 = i6 - (!SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty());
                        EmojiView.EmojiPack emojiPack4 = i7 >= 0 ? (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i7) : null;
                        if (emojiPack4 != null) {
                            int size = emojiPack4.expanded ? emojiPack4.documents.size() : Math.min(emojiPack4.documents.size(), spanCount2);
                            if (i > keyAt && i <= keyAt + 1 + size && (tLRPC$Document = emojiPack4.documents.get((i - keyAt) - 1)) != null) {
                                imageViewEmoji2.span = new AnimatedEmojiSpan(tLRPC$Document, (Paint.FontMetricsInt) null);
                            }
                        }
                    }
                    AnimatedEmojiSpan animatedEmojiSpan2 = imageViewEmoji2.span;
                    if (animatedEmojiSpan2 != null) {
                    }
                    z = z2;
                } else {
                    AnimatedEmojiSpan animatedEmojiSpan3 = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.defaultStatuses.get(i2);
                    imageViewEmoji2.span = animatedEmojiSpan3;
                    if (animatedEmojiSpan3 != null) {
                    }
                    z = z2;
                }
                imageViewEmoji2.setViewSelected(z);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
            SelectAnimatedEmojiDialog.this.clearRecent();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(EmojiView.EmojiPack emojiPack, int i, View view) {
            Integer num;
            View view2;
            int childAdapterPosition;
            if (!emojiPack.free && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                new PremiumFeatureBottomSheet(null, SelectAnimatedEmojiDialog.this.getContext(), SelectAnimatedEmojiDialog.this.currentAccount, 11, false).show();
                return;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount()) {
                    num = null;
                    view2 = null;
                    break;
                } else if ((SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i2) instanceof EmojiPackExpand) && (childAdapterPosition = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAdapterPosition((view2 = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i2)))) >= 0 && SelectAnimatedEmojiDialog.this.positionToExpand.get(childAdapterPosition) == i) {
                    num = Integer.valueOf(childAdapterPosition);
                    break;
                } else {
                    i2++;
                }
            }
            if (num != null) {
                SelectAnimatedEmojiDialog.this.expand(num.intValue(), view2);
            }
            EmojiPacksAlert.installSet(null, emojiPack.set, false);
            SelectAnimatedEmojiDialog.this.installedEmojiSets.add(Long.valueOf(emojiPack.set.id));
            SelectAnimatedEmojiDialog.this.updateRows(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return SelectAnimatedEmojiDialog.this.totalCount;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearRecent() {
        onRecentClearedListener onrecentclearedlistener;
        if (this.type != 1 || (onrecentclearedlistener = this.onRecentClearedListener) == null) {
            return;
        }
        onrecentclearedlistener.onRecentCleared();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class HeaderView extends FrameLayout {
        ImageView closeIcon;
        private LinearLayout layoutView;
        private ValueAnimator lockAnimator;
        private float lockT;
        private RLottieImageView lockView;
        private TextView textView;

        public HeaderView(SelectAnimatedEmojiDialog selectAnimatedEmojiDialog, Context context) {
            super(context);
            LinearLayout linearLayout = new LinearLayout(context);
            this.layoutView = linearLayout;
            linearLayout.setOrientation(0);
            addView(this.layoutView, LayoutHelper.createFrame(-2, -2, 17));
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockView = rLottieImageView;
            rLottieImageView.setAnimation(R.raw.unlock_icon, 20, 20);
            this.lockView.setColorFilter(Theme.getColor("chat_emojiPanelStickerSetName", selectAnimatedEmojiDialog.resourcesProvider));
            this.layoutView.addView(this.lockView, LayoutHelper.createLinear(20, 20));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor("chat_emojiPanelStickerSetName", selectAnimatedEmojiDialog.resourcesProvider));
            this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.textView.setTextSize(1, 14.0f);
            this.layoutView.addView(this.textView, LayoutHelper.createLinear(-2, -2, 17));
            ImageView imageView = new ImageView(context);
            this.closeIcon = imageView;
            imageView.setImageResource(R.drawable.msg_close);
            this.closeIcon.setScaleType(ImageView.ScaleType.CENTER);
            this.closeIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelStickerSetNameIcon", selectAnimatedEmojiDialog.resourcesProvider), PorterDuff.Mode.MULTIPLY));
            addView(this.closeIcon, LayoutHelper.createFrame(24, 24, 21));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
        }

        public void setText(String str, boolean z) {
            this.textView.setText(str);
            updateLock(z, false);
        }

        public void updateLock(boolean z, boolean z2) {
            ValueAnimator valueAnimator = this.lockAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.lockAnimator = null;
            }
            float f = 0.0f;
            if (z2) {
                float[] fArr = new float[2];
                fArr[0] = this.lockT;
                if (z) {
                    f = 1.0f;
                }
                fArr[1] = f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.lockAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$HeaderView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        SelectAnimatedEmojiDialog.HeaderView.this.lambda$updateLock$0(valueAnimator2);
                    }
                });
                this.lockAnimator.setDuration(200L);
                this.lockAnimator.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                this.lockAnimator.start();
                return;
            }
            if (z) {
                f = 1.0f;
            }
            this.lockT = f;
            this.lockView.setTranslationX(AndroidUtilities.dp(-8.0f) * (1.0f - this.lockT));
            this.textView.setTranslationX(AndroidUtilities.dp(-8.0f) * (1.0f - this.lockT));
            this.lockView.setAlpha(this.lockT);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateLock$0(ValueAnimator valueAnimator) {
            this.lockT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.lockView.setTranslationX(AndroidUtilities.dp(-8.0f) * (1.0f - this.lockT));
            this.textView.setTranslationX(AndroidUtilities.dp(-8.0f) * (1.0f - this.lockT));
            this.lockView.setAlpha(this.lockT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class EmojiPackButton extends FrameLayout {
        AnimatedTextView addButtonTextView;
        FrameLayout addButtonView;
        private ValueAnimator installFadeAway;
        private String lastTitle;
        private ValueAnimator lockAnimator;
        private Boolean lockShow;
        private float lockT;
        PremiumButtonView premiumButtonView;

        public EmojiPackButton(SelectAnimatedEmojiDialog selectAnimatedEmojiDialog, Context context) {
            super(context);
            AnimatedTextView animatedTextView = new AnimatedTextView(getContext());
            this.addButtonTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.3f, 0L, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.addButtonTextView.setTextSize(AndroidUtilities.dp(14.0f));
            this.addButtonTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.addButtonTextView.setTextColor(Theme.getColor("featuredStickers_buttonText", selectAnimatedEmojiDialog.resourcesProvider));
            this.addButtonTextView.setGravity(17);
            FrameLayout frameLayout = new FrameLayout(getContext());
            this.addButtonView = frameLayout;
            frameLayout.setBackground(Theme.AdaptiveRipple.filledRect(Theme.getColor("featuredStickers_addButton", selectAnimatedEmojiDialog.resourcesProvider), 8.0f));
            this.addButtonView.addView(this.addButtonTextView, LayoutHelper.createFrame(-1, -2, 17));
            addView(this.addButtonView, LayoutHelper.createFrame(-1, -1.0f));
            PremiumButtonView premiumButtonView = new PremiumButtonView(getContext(), false);
            this.premiumButtonView = premiumButtonView;
            premiumButtonView.setIcon(R.raw.unlock_icon);
            addView(this.premiumButtonView, LayoutHelper.createFrame(-1, -1.0f));
        }

        public void set(String str, boolean z, boolean z2, View.OnClickListener onClickListener) {
            this.lastTitle = str;
            if (z) {
                this.addButtonView.setVisibility(8);
                this.premiumButtonView.setVisibility(0);
                this.premiumButtonView.setButton(LocaleController.formatString("UnlockPremiumEmojiPack", R.string.UnlockPremiumEmojiPack, str), onClickListener);
            } else {
                this.premiumButtonView.setVisibility(8);
                this.addButtonView.setVisibility(0);
                this.addButtonView.setOnClickListener(onClickListener);
            }
            updateInstall(z2, false);
            updateLock(z, false);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(8.0f));
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f) + getPaddingTop() + getPaddingBottom(), 1073741824));
        }

        public void updateInstall(boolean z, boolean z2) {
            String formatString;
            if (z) {
                formatString = LocaleController.getString("Added", R.string.Added);
            } else {
                formatString = LocaleController.formatString("AddStickersCount", R.string.AddStickersCount, this.lastTitle);
            }
            this.addButtonTextView.setText(formatString, z2);
            ValueAnimator valueAnimator = this.installFadeAway;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.installFadeAway = null;
            }
            this.addButtonView.setEnabled(!z);
            float f = 0.6f;
            if (z2) {
                float[] fArr = new float[2];
                fArr[0] = this.addButtonView.getAlpha();
                if (!z) {
                    f = 1.0f;
                }
                fArr[1] = f;
                this.installFadeAway = ValueAnimator.ofFloat(fArr);
                FrameLayout frameLayout = this.addButtonView;
                frameLayout.setAlpha(frameLayout.getAlpha());
                this.installFadeAway.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$EmojiPackButton$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        SelectAnimatedEmojiDialog.EmojiPackButton.this.lambda$updateInstall$0(valueAnimator2);
                    }
                });
                this.installFadeAway.setDuration(450L);
                this.installFadeAway.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.installFadeAway.start();
                return;
            }
            FrameLayout frameLayout2 = this.addButtonView;
            if (!z) {
                f = 1.0f;
            }
            frameLayout2.setAlpha(f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateInstall$0(ValueAnimator valueAnimator) {
            this.addButtonView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        private void updateLock(final boolean z, boolean z2) {
            ValueAnimator valueAnimator = this.lockAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.lockAnimator = null;
            }
            Boolean bool = this.lockShow;
            if (bool == null || bool.booleanValue() != z) {
                Boolean valueOf = Boolean.valueOf(z);
                this.lockShow = valueOf;
                float f = 0.0f;
                int i = 0;
                if (z2) {
                    this.premiumButtonView.setVisibility(0);
                    float[] fArr = new float[2];
                    fArr[0] = this.lockT;
                    if (z) {
                        f = 1.0f;
                    }
                    fArr[1] = f;
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                    this.lockAnimator = ofFloat;
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$EmojiPackButton$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            SelectAnimatedEmojiDialog.EmojiPackButton.this.lambda$updateLock$1(valueAnimator2);
                        }
                    });
                    this.lockAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.EmojiPackButton.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (!z) {
                                EmojiPackButton.this.premiumButtonView.setVisibility(8);
                            }
                        }
                    });
                    this.lockAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.lockAnimator.setDuration(350L);
                    this.lockAnimator.start();
                    return;
                }
                if (valueOf.booleanValue()) {
                    f = 1.0f;
                }
                this.lockT = f;
                this.addButtonView.setAlpha(1.0f - f);
                this.premiumButtonView.setAlpha(this.lockT);
                this.premiumButtonView.setScaleX(this.lockT);
                this.premiumButtonView.setScaleY(this.lockT);
                PremiumButtonView premiumButtonView = this.premiumButtonView;
                if (!this.lockShow.booleanValue()) {
                    i = 8;
                }
                premiumButtonView.setVisibility(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateLock$1(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.lockT = floatValue;
            this.addButtonView.setAlpha(1.0f - floatValue);
            this.premiumButtonView.setAlpha(this.lockT);
        }
    }

    /* loaded from: classes3.dex */
    public static class EmojiPackExpand extends FrameLayout {
        public TextView textView;

        public EmojiPackExpand(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextSize(1, 12.0f);
            this.textView.setTextColor(-1);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0f), ColorUtils.setAlphaComponent(Theme.getColor("chat_emojiPanelStickerSetName", resourcesProvider), 99)));
            this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.textView.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(1.66f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f));
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }
    }

    public long animateExpandDuration() {
        return animateExpandAppearDuration() + animateExpandCrossfadeDuration() + 16;
    }

    public long animateExpandAppearDuration() {
        return Math.max(450L, Math.min(55, this.animateExpandToPosition - this.animateExpandFromPosition) * 30);
    }

    public long animateExpandCrossfadeDuration() {
        return Math.max(300L, Math.min(45, this.animateExpandToPosition - this.animateExpandFromPosition) * 25);
    }

    /* loaded from: classes3.dex */
    public class ImageViewEmoji extends FrameLayout {
        ValueAnimator backAnimator;
        public ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder;
        public float bigReactionSelectedProgress;
        public Drawable drawable;
        public Rect drawableBounds;
        public ImageReceiver imageReceiver;
        public ImageReceiver imageReceiverToDraw;
        public boolean isDefaultReaction;
        public int position;
        PremiumLockIconView premiumLockIconView;
        private float pressedProgress;
        public ReactionsLayoutInBubble.VisibleReaction reaction;
        public boolean selected;
        public AnimatedEmojiSpan span;
        public boolean empty = false;
        public boolean notDraw = false;

        public ImageViewEmoji(Context context) {
            super(context);
        }

        @Override // android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
        }

        @Override // android.view.View
        public void setPressed(boolean z) {
            ValueAnimator valueAnimator;
            if (isPressed() != z) {
                super.setPressed(z);
                invalidate();
                if (z && (valueAnimator = this.backAnimator) != null) {
                    valueAnimator.removeAllListeners();
                    this.backAnimator.cancel();
                }
                if (z) {
                    return;
                }
                float f = this.pressedProgress;
                if (f == 0.0f) {
                    return;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 0.0f);
                this.backAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$ImageViewEmoji$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        SelectAnimatedEmojiDialog.ImageViewEmoji.this.lambda$setPressed$0(valueAnimator2);
                    }
                });
                this.backAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.ImageViewEmoji.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        ImageViewEmoji.this.backAnimator = null;
                    }
                });
                this.backAnimator.setInterpolator(new OvershootInterpolator(5.0f));
                this.backAnimator.setDuration(350L);
                this.backAnimator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setPressed$0(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
        }

        public void updatePressedProgress() {
            if (isPressed()) {
                float f = this.pressedProgress;
                if (f == 1.0f) {
                    return;
                }
                this.pressedProgress = Utilities.clamp(f + 0.16f, 1.0f, 0.0f);
                invalidate();
            }
        }

        public void update(long j) {
            ImageReceiver imageReceiver = this.imageReceiverToDraw;
            if (imageReceiver != null) {
                if (imageReceiver.getLottieAnimation() != null) {
                    this.imageReceiverToDraw.getLottieAnimation().updateCurrentFrame(j, true);
                }
                if (this.imageReceiverToDraw.getAnimation() == null) {
                    return;
                }
                this.imageReceiverToDraw.getAnimation().updateCurrentFrame(j, true);
            }
        }

        public void setViewSelected(boolean z) {
            if (this.selected != z) {
                this.selected = z;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.selected && !this.notDraw) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                rectF.inset(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), SelectAnimatedEmojiDialog.this.selectorPaint);
            }
            super.dispatchDraw(canvas);
        }
    }

    public void onEmojiClick(final View view, final AnimatedEmojiSpan animatedEmojiSpan) {
        if (animatedEmojiSpan == null) {
            onEmojiSelected(view, null, null);
            return;
        }
        TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
        tLRPC$TL_emojiStatus.document_id = animatedEmojiSpan.getDocumentId();
        MediaDataController.getInstance(this.currentAccount).pushRecentEmojiStatus(tLRPC$TL_emojiStatus);
        final TLRPC$Document tLRPC$Document = animatedEmojiSpan.document;
        if (tLRPC$Document == null) {
            tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.documentId);
        }
        if (this.type == 0 && (view instanceof ImageViewEmoji)) {
            animateEmojiSelect((ImageViewEmoji) view, new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    SelectAnimatedEmojiDialog.this.lambda$onEmojiClick$6(view, animatedEmojiSpan, tLRPC$Document);
                }
            });
        } else {
            onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmojiClick$6(View view, AnimatedEmojiSpan animatedEmojiSpan, TLRPC$Document tLRPC$Document) {
        onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows(boolean z) {
        boolean z2;
        int i;
        boolean z3;
        TLRPC$StickerSet tLRPC$StickerSet;
        boolean z4;
        MediaDataController mediaDataController = MediaDataController.getInstance(UserConfig.selectedAccount);
        if (mediaDataController == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(mediaDataController.getStickerSets(5));
        ArrayList arrayList2 = new ArrayList(mediaDataController.getFeaturedEmojiSets());
        final ArrayList arrayList3 = new ArrayList(this.rowHashCodes);
        this.totalCount = 0;
        this.recentReactionsSectionRow = -1;
        this.recentReactionsStartRow = -1;
        this.recentReactionsEndRow = -1;
        this.popularSectionRow = -1;
        this.longtapHintRow = -1;
        this.recent.clear();
        this.defaultStatuses.clear();
        this.topReactions.clear();
        this.recentReactions.clear();
        this.packs.clear();
        this.positionToSection.clear();
        this.sectionToPosition.clear();
        this.positionToExpand.clear();
        this.rowHashCodes.clear();
        this.positionToButton.clear();
        int i2 = 2;
        if (this.includeEmpty) {
            this.totalCount++;
            this.rowHashCodes.add(2);
        }
        if (this.type == 1) {
            int i3 = this.totalCount;
            this.totalCount = i3 + 1;
            this.longtapHintRow = i3;
            this.rowHashCodes.add(6);
        }
        if (this.recentReactionsToSet != null) {
            this.topReactionsStartRow = this.totalCount;
            ArrayList arrayList4 = new ArrayList();
            arrayList4.addAll(this.recentReactionsToSet);
            for (int i4 = 0; i4 < 16; i4++) {
                if (!arrayList4.isEmpty()) {
                    this.topReactions.add((ReactionsLayoutInBubble.VisibleReaction) arrayList4.remove(0));
                }
            }
            for (int i5 = 0; i5 < this.topReactions.size(); i5++) {
                this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{-5632, Integer.valueOf(this.topReactions.get(i5).hashCode())})));
            }
            int size = this.totalCount + this.topReactions.size();
            this.totalCount = size;
            this.topReactionsEndRow = size;
            if (!arrayList4.isEmpty()) {
                int i6 = 0;
                while (true) {
                    if (i6 >= arrayList4.size()) {
                        z4 = true;
                        break;
                    } else if (((ReactionsLayoutInBubble.VisibleReaction) arrayList4.get(i6)).documentId != 0) {
                        z4 = false;
                        break;
                    } else {
                        i6++;
                    }
                }
                if (z4) {
                    if (UserConfig.getInstance(this.currentAccount).isPremium()) {
                        int i7 = this.totalCount;
                        this.totalCount = i7 + 1;
                        this.popularSectionRow = i7;
                        this.rowHashCodes.add(5);
                    }
                } else {
                    int i8 = this.totalCount;
                    this.totalCount = i8 + 1;
                    this.recentReactionsSectionRow = i8;
                    this.rowHashCodes.add(4);
                }
                this.recentReactionsStartRow = this.totalCount;
                this.recentReactions.addAll(arrayList4);
                for (int i9 = 0; i9 < this.recentReactions.size(); i9++) {
                    ArrayList<Integer> arrayList5 = this.rowHashCodes;
                    Object[] objArr = new Object[2];
                    objArr[0] = Integer.valueOf(z4 ? 4235 : -3142);
                    objArr[1] = Integer.valueOf(this.recentReactions.get(i9).hashCode());
                    arrayList5.add(Integer.valueOf(Arrays.hashCode(objArr)));
                }
                int size2 = this.totalCount + this.recentReactions.size();
                this.totalCount = size2;
                this.recentReactionsEndRow = size2;
            }
        } else if (this.type == 0) {
            ArrayList<TLRPC$EmojiStatus> recentEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getRecentEmojiStatuses();
            ArrayList<TLRPC$EmojiStatus> defaultEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getDefaultEmojiStatuses();
            if (recentEmojiStatuses != null && !recentEmojiStatuses.isEmpty()) {
                Iterator<TLRPC$EmojiStatus> it = recentEmojiStatuses.iterator();
                while (it.hasNext()) {
                    TLRPC$EmojiStatus next = it.next();
                    if (next instanceof TLRPC$TL_emojiStatus) {
                        this.recent.add(new AnimatedEmojiSpan(((TLRPC$TL_emojiStatus) next).document_id, (Paint.FontMetricsInt) null));
                    }
                }
            }
            if (defaultEmojiStatuses != null && !defaultEmojiStatuses.isEmpty()) {
                Iterator<TLRPC$EmojiStatus> it2 = defaultEmojiStatuses.iterator();
                while (it2.hasNext()) {
                    TLRPC$EmojiStatus next2 = it2.next();
                    if (next2 instanceof TLRPC$TL_emojiStatus) {
                        long j = ((TLRPC$TL_emojiStatus) next2).document_id;
                        int i10 = 0;
                        while (true) {
                            if (i10 >= this.recent.size()) {
                                z2 = false;
                                break;
                            } else if (this.recent.get(i10).getDocumentId() == j) {
                                z2 = true;
                                break;
                            } else {
                                i10++;
                            }
                        }
                        if (!z2) {
                            this.recent.add(new AnimatedEmojiSpan(j, (Paint.FontMetricsInt) null));
                        }
                    }
                }
            }
            int spanCount = this.layoutManager.getSpanCount() * 5;
            int i11 = spanCount - (this.includeEmpty ? 1 : 0);
            if (this.recent.size() > i11 && !this.recentExpanded) {
                for (int i12 = 0; i12 < i11 - 1; i12++) {
                    this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{43223, Long.valueOf(this.recent.get(i12).getDocumentId())})));
                    this.totalCount++;
                }
                this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{-5531, -1, Integer.valueOf((this.recent.size() - spanCount) + (this.includeEmpty ? 1 : 0) + 1)})));
                this.positionToExpand.put(this.totalCount, -1);
                this.totalCount++;
            } else {
                for (int i13 = 0; i13 < this.recent.size(); i13++) {
                    this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{43223, Long.valueOf(this.recent.get(i13).getDocumentId())})));
                    this.totalCount++;
                }
            }
        }
        int i14 = 0;
        while (true) {
            i = 9211;
            if (i14 >= arrayList.size()) {
                break;
            }
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) arrayList.get(i14);
            if (tLRPC$TL_messages_stickerSet != null && (tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set) != null && tLRPC$StickerSet.emojis && !this.installedEmojiSets.contains(Long.valueOf(tLRPC$StickerSet.id))) {
                this.positionToSection.put(this.totalCount, this.packs.size());
                this.sectionToPosition.put(this.packs.size(), this.totalCount);
                this.totalCount++;
                this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{9211, Long.valueOf(tLRPC$TL_messages_stickerSet.set.id)})));
                EmojiView.EmojiPack emojiPack = new EmojiView.EmojiPack();
                emojiPack.installed = true;
                emojiPack.featured = false;
                emojiPack.expanded = true;
                emojiPack.free = !MessageObject.isPremiumEmojiPack(tLRPC$TL_messages_stickerSet);
                emojiPack.set = tLRPC$TL_messages_stickerSet.set;
                emojiPack.documents = tLRPC$TL_messages_stickerSet.documents;
                this.packs.size();
                this.packs.add(emojiPack);
                this.totalCount += emojiPack.documents.size();
                for (int i15 = 0; i15 < emojiPack.documents.size(); i15++) {
                    this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{3212, Long.valueOf(emojiPack.documents.get(i15).id)})));
                }
            }
            i14++;
        }
        int spanCount2 = this.layoutManager.getSpanCount() * 3;
        int i16 = 0;
        while (i16 < arrayList2.size()) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) arrayList2.get(i16);
            if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
                TLRPC$TL_stickerSetFullCovered tLRPC$TL_stickerSetFullCovered = (TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered;
                int i17 = 0;
                while (true) {
                    if (i17 >= this.packs.size()) {
                        z3 = false;
                        break;
                    } else if (this.packs.get(i17).set.id == tLRPC$TL_stickerSetFullCovered.set.id) {
                        z3 = true;
                        break;
                    } else {
                        i17++;
                    }
                }
                if (!z3) {
                    this.positionToSection.put(this.totalCount, this.packs.size());
                    this.sectionToPosition.put(this.packs.size(), this.totalCount);
                    this.totalCount++;
                    ArrayList<Integer> arrayList6 = this.rowHashCodes;
                    Object[] objArr2 = new Object[i2];
                    objArr2[0] = Integer.valueOf(i);
                    objArr2[1] = Long.valueOf(tLRPC$TL_stickerSetFullCovered.set.id);
                    arrayList6.add(Integer.valueOf(Arrays.hashCode(objArr2)));
                    EmojiView.EmojiPack emojiPack2 = new EmojiView.EmojiPack();
                    emojiPack2.installed = this.installedEmojiSets.contains(Long.valueOf(tLRPC$TL_stickerSetFullCovered.set.id));
                    emojiPack2.featured = true;
                    emojiPack2.free = !MessageObject.isPremiumEmojiPack(tLRPC$TL_stickerSetFullCovered);
                    emojiPack2.set = tLRPC$TL_stickerSetFullCovered.set;
                    emojiPack2.documents = tLRPC$TL_stickerSetFullCovered.documents;
                    this.packs.size();
                    emojiPack2.expanded = this.expandedEmojiSets.contains(Long.valueOf(emojiPack2.set.id));
                    if (emojiPack2.documents.size() > spanCount2 && !emojiPack2.expanded) {
                        this.totalCount += spanCount2;
                        int i18 = 0;
                        while (i18 < spanCount2 - 1) {
                            ArrayList<Integer> arrayList7 = this.rowHashCodes;
                            Object[] objArr3 = new Object[i2];
                            objArr3[0] = 3212;
                            objArr3[1] = Long.valueOf(emojiPack2.documents.get(i18).id);
                            arrayList7.add(Integer.valueOf(Arrays.hashCode(objArr3)));
                            i18++;
                            i2 = 2;
                        }
                        this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{-5531, Long.valueOf(tLRPC$TL_stickerSetFullCovered.set.id), Integer.valueOf((emojiPack2.documents.size() - spanCount2) + 1)})));
                        this.positionToExpand.put(this.totalCount - 1, this.packs.size());
                    } else {
                        this.totalCount += emojiPack2.documents.size();
                        for (int i19 = 0; i19 < emojiPack2.documents.size(); i19++) {
                            this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{3212, Long.valueOf(emojiPack2.documents.get(i19).id)})));
                        }
                    }
                    if (!emojiPack2.installed) {
                        this.positionToButton.put(this.totalCount, this.packs.size());
                        this.totalCount++;
                        this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{3321, Long.valueOf(tLRPC$TL_stickerSetFullCovered.set.id)})));
                    }
                    this.packs.add(emojiPack2);
                    i16++;
                    i = 9211;
                    i2 = 2;
                }
            }
            i16++;
            i = 9211;
            i2 = 2;
        }
        post(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$updateRows$7();
            }
        });
        if (z) {
            DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14
                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areContentsTheSame(int i20, int i21) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getOldListSize() {
                    return arrayList3.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getNewListSize() {
                    return SelectAnimatedEmojiDialog.this.rowHashCodes.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areItemsTheSame(int i20, int i21) {
                    return ((Integer) arrayList3.get(i20)).equals(SelectAnimatedEmojiDialog.this.rowHashCodes.get(i21));
                }
            }, true).dispatchUpdatesTo(this.adapter);
        } else {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRows$7() {
        this.emojiTabs.updateEmojiPacks(this.packs);
    }

    public void expand(int i, View view) {
        int spanCount;
        int min;
        int i2;
        Integer num;
        int i3;
        int i4;
        int i5 = this.positionToExpand.get(i);
        boolean z = false;
        Integer num2 = null;
        if (i5 >= 0 && i5 < this.packs.size()) {
            spanCount = this.layoutManager.getSpanCount() * 3;
            EmojiView.EmojiPack emojiPack = this.packs.get(i5);
            if (emojiPack.expanded) {
                return;
            }
            if (i5 + 1 == this.packs.size()) {
                z = true;
            }
            i4 = this.sectionToPosition.get(i5);
            this.expandedEmojiSets.add(Long.valueOf(emojiPack.set.id));
            i2 = emojiPack.expanded ? emojiPack.documents.size() : Math.min(spanCount, emojiPack.documents.size());
            num = emojiPack.documents.size() > spanCount ? Integer.valueOf(i4 + 1 + i2) : null;
            emojiPack.expanded = true;
            i3 = emojiPack.documents.size();
        } else if (i5 != -1) {
            return;
        } else {
            spanCount = this.layoutManager.getSpanCount() * 5;
            boolean z2 = this.recentExpanded;
            if (z2) {
                return;
            }
            boolean z3 = this.includeEmpty;
            if (z2) {
                min = this.recent.size();
            } else {
                min = Math.min((spanCount - (z3 ? 1 : 0)) - 2, this.recent.size());
            }
            i2 = min;
            int size = this.recent.size();
            this.recentExpanded = true;
            num = null;
            i3 = size;
            i4 = z3 ? 1 : 0;
        }
        if (i3 > i2) {
            num = Integer.valueOf(i4 + 1 + i2);
            num2 = Integer.valueOf(i3 - i2);
        }
        updateRows(true);
        if (num == null || num2 == null) {
            return;
        }
        this.animateExpandFromButton = view;
        this.animateExpandFromPosition = num.intValue();
        this.animateExpandToPosition = num.intValue() + num2.intValue();
        this.animateExpandStartTime = SystemClock.elapsedRealtime();
        if (!z) {
            return;
        }
        final int intValue = num.intValue();
        final float f = num2.intValue() > spanCount / 2 ? 1.5f : 3.5f;
        post(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$expand$8(f, intValue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$expand$8(float f, int i) {
        try {
            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(this.emojiGridView.getContext(), 0, f);
            linearSmoothScrollerCustom.setTargetPosition(i);
            this.layoutManager.startSmoothScroll(linearSmoothScrollerCustom);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.drawBackground) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f), 1073741824), View.MeasureSpec.makeMeasureSpec((int) Math.min(AndroidUtilities.dp(330.0f), AndroidUtilities.displaySize.y * 0.75f), Integer.MIN_VALUE));
        } else {
            super.onMeasure(i, i2);
        }
    }

    private AnimatedEmojiSpan[] getAnimatedEmojiSpans() {
        AnimatedEmojiSpan[] animatedEmojiSpanArr = new AnimatedEmojiSpan[this.emojiGridView.getChildCount()];
        for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
            View childAt = this.emojiGridView.getChildAt(i);
            if (childAt instanceof ImageViewEmoji) {
                animatedEmojiSpanArr[i] = ((ImageViewEmoji) childAt).span;
            }
        }
        return animatedEmojiSpanArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCacheType() {
        return this.type == 0 ? 2 : 3;
    }

    public void updateEmojiDrawables() {
        this.animatedEmojiDrawables = AnimatedEmojiSpan.update(getCacheType(), this.emojiGridView, getAnimatedEmojiSpans(), this.animatedEmojiDrawables);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class EmojiListView extends RecyclerListView {
        SparseArray<ArrayList<ImageViewEmoji>> viewsGroupedByLines = new SparseArray<>();
        ArrayList<ArrayList<ImageViewEmoji>> unusedArrays = new ArrayList<>();
        ArrayList<DrawingInBackgroundLine> unusedLineDrawables = new ArrayList<>();
        private int lastChildCount = -1;

        public EmojiListView(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (SelectAnimatedEmojiDialog.this.showAnimator == null || !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                SelectAnimatedEmojiDialog.this.updateEmojiDrawables();
                this.lastChildCount = getChildCount();
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            ImageReceiver imageReceiver;
            super.dispatchDraw(canvas);
            if (this.lastChildCount != getChildCount() && SelectAnimatedEmojiDialog.this.showAnimator != null && !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                SelectAnimatedEmojiDialog.this.updateEmojiDrawables();
                this.lastChildCount = getChildCount();
            }
            for (int i = 0; i < this.viewsGroupedByLines.size(); i++) {
                ArrayList<ImageViewEmoji> valueAt = this.viewsGroupedByLines.valueAt(i);
                valueAt.clear();
                this.unusedArrays.add(valueAt);
            }
            this.viewsGroupedByLines.clear();
            if ((SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration()) && SelectAnimatedEmojiDialog.this.animateExpandFromButton != null) {
                int unused = SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
            }
            if (SelectAnimatedEmojiDialog.this.animatedEmojiDrawables != null && SelectAnimatedEmojiDialog.this.emojiGridView != null) {
                for (int i2 = 0; i2 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i2++) {
                    View childAt = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i2);
                    if (childAt instanceof ImageViewEmoji) {
                        ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                        imageViewEmoji.updatePressedProgress();
                        int y = SelectAnimatedEmojiDialog.this.smoothScrolling ? (int) childAt.getY() : childAt.getTop();
                        ArrayList<ImageViewEmoji> arrayList = this.viewsGroupedByLines.get(y);
                        if (arrayList == null) {
                            if (!this.unusedArrays.isEmpty()) {
                                ArrayList<ArrayList<ImageViewEmoji>> arrayList2 = this.unusedArrays;
                                arrayList = arrayList2.remove(arrayList2.size() - 1);
                            } else {
                                arrayList = new ArrayList<>();
                            }
                            this.viewsGroupedByLines.put(y, arrayList);
                        }
                        arrayList.add(imageViewEmoji);
                        PremiumLockIconView premiumLockIconView = imageViewEmoji.premiumLockIconView;
                        if (premiumLockIconView != null && premiumLockIconView.getVisibility() == 0 && imageViewEmoji.premiumLockIconView.getImageReceiver() == null && (imageReceiver = imageViewEmoji.imageReceiverToDraw) != null) {
                            imageViewEmoji.premiumLockIconView.setImageReceiver(imageReceiver);
                        }
                    }
                }
            }
            SelectAnimatedEmojiDialog.this.lineDrawablesTmp.clear();
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            selectAnimatedEmojiDialog.lineDrawablesTmp.addAll(selectAnimatedEmojiDialog.lineDrawables);
            SelectAnimatedEmojiDialog.this.lineDrawables.clear();
            long currentTimeMillis = System.currentTimeMillis();
            int i3 = 0;
            while (true) {
                DrawingInBackgroundLine drawingInBackgroundLine = null;
                if (i3 < this.viewsGroupedByLines.size()) {
                    ArrayList<ImageViewEmoji> valueAt2 = this.viewsGroupedByLines.valueAt(i3);
                    ImageViewEmoji imageViewEmoji2 = valueAt2.get(0);
                    int childAdapterPosition = getChildAdapterPosition(imageViewEmoji2);
                    int i4 = 0;
                    while (true) {
                        if (i4 >= SelectAnimatedEmojiDialog.this.lineDrawablesTmp.size()) {
                            break;
                        } else if (SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i4).position == childAdapterPosition) {
                            drawingInBackgroundLine = SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i4);
                            SelectAnimatedEmojiDialog.this.lineDrawablesTmp.remove(i4);
                            break;
                        } else {
                            i4++;
                        }
                    }
                    if (drawingInBackgroundLine == null) {
                        if (!this.unusedLineDrawables.isEmpty()) {
                            ArrayList<DrawingInBackgroundLine> arrayList3 = this.unusedLineDrawables;
                            drawingInBackgroundLine = arrayList3.remove(arrayList3.size() - 1);
                        } else {
                            drawingInBackgroundLine = new DrawingInBackgroundLine();
                        }
                        drawingInBackgroundLine.position = childAdapterPosition;
                        drawingInBackgroundLine.onAttachToWindow();
                    }
                    SelectAnimatedEmojiDialog.this.lineDrawables.add(drawingInBackgroundLine);
                    drawingInBackgroundLine.imageViewEmojis = valueAt2;
                    canvas.save();
                    canvas.translate(imageViewEmoji2.getLeft(), imageViewEmoji2.getY());
                    drawingInBackgroundLine.startOffset = imageViewEmoji2.getLeft();
                    int measuredWidth = getMeasuredWidth() - (imageViewEmoji2.getLeft() * 2);
                    int measuredHeight = imageViewEmoji2.getMeasuredHeight();
                    if (measuredWidth > 0 && measuredHeight > 0) {
                        drawingInBackgroundLine.draw(canvas, currentTimeMillis, measuredWidth, measuredHeight, 1.0f);
                    }
                    canvas.restore();
                    i3++;
                }
            }
            for (int i5 = 0; i5 < SelectAnimatedEmojiDialog.this.lineDrawablesTmp.size(); i5++) {
                if (this.unusedLineDrawables.size() < 3) {
                    this.unusedLineDrawables.add(SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i5));
                    SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i5).imageViewEmojis = null;
                    SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i5).reset();
                } else {
                    SelectAnimatedEmojiDialog.this.lineDrawablesTmp.get(i5).onDetachFromWindow();
                }
            }
            SelectAnimatedEmojiDialog.this.lineDrawablesTmp.clear();
        }

        /* loaded from: classes3.dex */
        public class DrawingInBackgroundLine extends DrawingInBackgroundThreadDrawable {
            ArrayList<ImageViewEmoji> imageViewEmojis;
            public int position;
            public int startOffset;
            ArrayList<ImageViewEmoji> drawInBackgroundViews = new ArrayList<>();
            float skewAlpha = 1.0f;
            boolean skewBelow = false;
            private OvershootInterpolator appearScaleInterpolator = new OvershootInterpolator(3.0f);

            public DrawingInBackgroundLine() {
            }

            /* JADX WARN: Code restructure failed: missing block: B:47:0x0105, code lost:
                prepareDraw(java.lang.System.currentTimeMillis());
                drawInUiThread(r12, r17);
                reset();
             */
            /* JADX WARN: Code restructure failed: missing block: B:48:?, code lost:
                return;
             */
            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void draw(Canvas canvas, long j, int i, int i2, float f) {
                ArrayList<ImageViewEmoji> arrayList = this.imageViewEmojis;
                if (arrayList == null) {
                    return;
                }
                this.skewAlpha = 1.0f;
                this.skewBelow = false;
                if (!arrayList.isEmpty()) {
                    ImageViewEmoji imageViewEmoji = this.imageViewEmojis.get(0);
                    if (imageViewEmoji.getY() > EmojiListView.this.getHeight() - imageViewEmoji.getHeight()) {
                        float clamp = MathUtils.clamp((-(imageViewEmoji.getY() - EmojiListView.this.getHeight())) / imageViewEmoji.getHeight(), 0.0f, 1.0f);
                        this.skewAlpha = clamp;
                        this.skewAlpha = (clamp * 0.75f) + 0.25f;
                    }
                }
                boolean z = true;
                boolean z2 = this.skewAlpha < 1.0f || this.imageViewEmojis.size() <= 4 || SharedConfig.getDevicePerformanceClass() == 0 || (SelectAnimatedEmojiDialog.this.showAnimator != null && SelectAnimatedEmojiDialog.this.showAnimator.isRunning());
                if (!z2) {
                    boolean z3 = SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration();
                    for (int i3 = 0; i3 < this.imageViewEmojis.size(); i3++) {
                        ImageViewEmoji imageViewEmoji2 = this.imageViewEmojis.get(i3);
                        if (imageViewEmoji2.pressedProgress != 0.0f || imageViewEmoji2.backAnimator != null || imageViewEmoji2.getTranslationX() != 0.0f || imageViewEmoji2.getTranslationY() != 0.0f || imageViewEmoji2.getAlpha() != 1.0f || (z3 && imageViewEmoji2.position > SelectAnimatedEmojiDialog.this.animateExpandFromPosition && imageViewEmoji2.position < SelectAnimatedEmojiDialog.this.animateExpandToPosition)) {
                            break;
                        }
                    }
                }
                z = z2;
                super.draw(canvas, j, i, i2, f);
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void prepareDraw(long j) {
                float f;
                ImageReceiver imageReceiver;
                this.drawInBackgroundViews.clear();
                for (int i = 0; i < this.imageViewEmojis.size(); i++) {
                    ImageViewEmoji imageViewEmoji = this.imageViewEmojis.get(i);
                    if (!imageViewEmoji.notDraw) {
                        float f2 = 0.7f;
                        float f3 = 1.0f;
                        if (imageViewEmoji.empty) {
                            Drawable premiumStar = SelectAnimatedEmojiDialog.this.getPremiumStar();
                            if (imageViewEmoji.pressedProgress != 0.0f || imageViewEmoji.selected) {
                                if (!imageViewEmoji.selected) {
                                    f2 = imageViewEmoji.pressedProgress;
                                }
                                f3 = 1.0f * (((1.0f - f2) * 0.2f) + 0.8f);
                            }
                            if (premiumStar != null) {
                                premiumStar.setAlpha(255);
                                int width = (imageViewEmoji.getWidth() - imageViewEmoji.getPaddingLeft()) - imageViewEmoji.getPaddingRight();
                                int height = (imageViewEmoji.getHeight() - imageViewEmoji.getPaddingTop()) - imageViewEmoji.getPaddingBottom();
                                Rect rect = AndroidUtilities.rectTmp2;
                                float f4 = width / 2.0f;
                                float f5 = height / 2.0f;
                                rect.set((int) ((imageViewEmoji.getWidth() / 2.0f) - ((imageViewEmoji.getScaleX() * f4) * f3)), (int) ((imageViewEmoji.getHeight() / 2.0f) - ((imageViewEmoji.getScaleY() * f5) * f3)), (int) ((imageViewEmoji.getWidth() / 2.0f) + (f4 * imageViewEmoji.getScaleX() * f3)), (int) ((imageViewEmoji.getHeight() / 2.0f) + (f5 * imageViewEmoji.getScaleY() * f3)));
                                rect.offset(imageViewEmoji.getLeft() - this.startOffset, 0);
                                if (imageViewEmoji.drawableBounds == null) {
                                    imageViewEmoji.drawableBounds = new Rect();
                                }
                                imageViewEmoji.drawableBounds.set(rect);
                                imageViewEmoji.drawable = premiumStar;
                                this.drawInBackgroundViews.add(imageViewEmoji);
                            }
                        } else {
                            if (imageViewEmoji.pressedProgress != 0.0f || imageViewEmoji.selected) {
                                if (!imageViewEmoji.selected) {
                                    f2 = imageViewEmoji.pressedProgress;
                                }
                                f = (((1.0f - f2) * 0.2f) + 0.8f) * 1.0f;
                            } else {
                                f = 1.0f;
                            }
                            if ((SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration()) && SelectAnimatedEmojiDialog.this.animateExpandFromPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandToPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0) {
                                int childAdapterPosition = EmojiListView.this.getChildAdapterPosition(imageViewEmoji) - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                int i2 = SelectAnimatedEmojiDialog.this.animateExpandToPosition - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                if (childAdapterPosition >= 0 && childAdapterPosition < i2) {
                                    float clamp = MathUtils.clamp(((float) (SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime)) / ((float) SelectAnimatedEmojiDialog.this.animateExpandAppearDuration()), 0.0f, 1.0f);
                                    float f6 = childAdapterPosition;
                                    float f7 = i2;
                                    float f8 = f7 / 4.0f;
                                    float cascade = AndroidUtilities.cascade(clamp, f6, f7, f8);
                                    f *= (this.appearScaleInterpolator.getInterpolation(AndroidUtilities.cascade(clamp, f6, f7, f8)) * 0.5f) + 0.5f;
                                    f3 = 1.0f * cascade;
                                }
                            } else {
                                f3 = imageViewEmoji.getAlpha();
                            }
                            if (!imageViewEmoji.isDefaultReaction) {
                                if (imageViewEmoji.span != null) {
                                    AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.get(imageViewEmoji.span.getDocumentId());
                                    if (animatedEmojiDrawable == null && Math.min(imageViewEmoji.getScaleX(), imageViewEmoji.getScaleY()) * f > 0.0f && imageViewEmoji.getAlpha() * f3 > 0.0f) {
                                        animatedEmojiDrawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji.span.getDocumentId());
                                        animatedEmojiDrawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                                        animatedEmojiDrawable.addView(EmojiListView.this);
                                        SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.put(imageViewEmoji.span.getDocumentId(), animatedEmojiDrawable);
                                    }
                                    if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null) {
                                        imageReceiver = animatedEmojiDrawable.getImageReceiver();
                                        animatedEmojiDrawable.setAlpha((int) (f3 * 255.0f));
                                        imageViewEmoji.drawable = animatedEmojiDrawable;
                                        animatedEmojiDrawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                                    }
                                }
                            } else {
                                imageReceiver = imageViewEmoji.imageReceiver;
                                imageReceiver.setAlpha(f3);
                            }
                            if (imageReceiver != null) {
                                ImageReceiver.BackgroundThreadDrawHolder drawInBackgroundThread = imageReceiver.setDrawInBackgroundThread(imageViewEmoji.backgroundThreadDrawHolder);
                                imageViewEmoji.backgroundThreadDrawHolder = drawInBackgroundThread;
                                drawInBackgroundThread.time = j;
                                imageViewEmoji.imageReceiverToDraw = imageReceiver;
                                imageViewEmoji.update(j);
                                int width2 = (imageViewEmoji.getWidth() - imageViewEmoji.getPaddingLeft()) - imageViewEmoji.getPaddingRight();
                                int height2 = (imageViewEmoji.getHeight() - imageViewEmoji.getPaddingTop()) - imageViewEmoji.getPaddingBottom();
                                Rect rect2 = AndroidUtilities.rectTmp2;
                                float f9 = width2 / 2.0f;
                                float f10 = height2 / 2.0f;
                                rect2.set((int) ((imageViewEmoji.getWidth() / 2.0f) - ((imageViewEmoji.getScaleX() * f9) * f)), (int) ((imageViewEmoji.getHeight() / 2.0f) - ((imageViewEmoji.getScaleY() * f10) * f)), (int) ((imageViewEmoji.getWidth() / 2.0f) + (f9 * imageViewEmoji.getScaleX() * f)), (int) ((imageViewEmoji.getHeight() / 2.0f) + (f10 * imageViewEmoji.getScaleY() * f)));
                                rect2.offset((imageViewEmoji.getLeft() + ((int) imageViewEmoji.getTranslationX())) - this.startOffset, 0);
                                imageViewEmoji.backgroundThreadDrawHolder.setBounds(rect2);
                                this.drawInBackgroundViews.add(imageViewEmoji);
                            }
                        }
                    }
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void drawInBackground(Canvas canvas) {
                for (int i = 0; i < this.drawInBackgroundViews.size(); i++) {
                    ImageViewEmoji imageViewEmoji = this.drawInBackgroundViews.get(i);
                    if (!imageViewEmoji.notDraw) {
                        if (imageViewEmoji.empty) {
                            imageViewEmoji.drawable.setBounds(imageViewEmoji.drawableBounds);
                            imageViewEmoji.drawable.draw(canvas);
                        } else {
                            ImageReceiver imageReceiver = imageViewEmoji.imageReceiverToDraw;
                            if (imageReceiver != null) {
                                imageReceiver.draw(canvas, imageViewEmoji.backgroundThreadDrawHolder);
                            }
                        }
                    }
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            protected void drawInUiThread(Canvas canvas, float f) {
                if (this.imageViewEmojis != null) {
                    canvas.save();
                    canvas.translate(-this.startOffset, 0.0f);
                    float f2 = f;
                    for (int i = 0; i < this.imageViewEmojis.size(); i++) {
                        ImageViewEmoji imageViewEmoji = this.imageViewEmojis.get(i);
                        if (!imageViewEmoji.notDraw) {
                            float scaleX = imageViewEmoji.getScaleX();
                            if (imageViewEmoji.pressedProgress != 0.0f || imageViewEmoji.selected) {
                                scaleX *= ((1.0f - (imageViewEmoji.selected ? 0.7f : imageViewEmoji.pressedProgress)) * 0.2f) + 0.8f;
                            }
                            boolean z = true;
                            boolean z2 = SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration();
                            if (!z2 || SelectAnimatedEmojiDialog.this.animateExpandFromPosition < 0 || SelectAnimatedEmojiDialog.this.animateExpandToPosition < 0 || SelectAnimatedEmojiDialog.this.animateExpandStartTime <= 0) {
                                z = false;
                            }
                            if (z) {
                                int childAdapterPosition = EmojiListView.this.getChildAdapterPosition(imageViewEmoji) - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                int i2 = SelectAnimatedEmojiDialog.this.animateExpandToPosition - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                if (childAdapterPosition >= 0 && childAdapterPosition < i2) {
                                    float clamp = MathUtils.clamp(((float) (SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime)) / ((float) SelectAnimatedEmojiDialog.this.animateExpandAppearDuration()), 0.0f, 1.0f);
                                    float f3 = childAdapterPosition;
                                    float f4 = i2;
                                    float f5 = f4 / 4.0f;
                                    float cascade = AndroidUtilities.cascade(clamp, f3, f4, f5);
                                    scaleX *= (this.appearScaleInterpolator.getInterpolation(AndroidUtilities.cascade(clamp, f3, f4, f5)) * 0.5f) + 0.5f;
                                    f2 = cascade;
                                }
                            } else {
                                f2 = imageViewEmoji.getAlpha();
                            }
                            Rect rect = AndroidUtilities.rectTmp2;
                            rect.set(((int) imageViewEmoji.getX()) + imageViewEmoji.getPaddingLeft(), imageViewEmoji.getPaddingTop(), (((int) imageViewEmoji.getX()) + imageViewEmoji.getWidth()) - imageViewEmoji.getPaddingRight(), imageViewEmoji.getHeight() - imageViewEmoji.getPaddingBottom());
                            if (!SelectAnimatedEmojiDialog.this.smoothScrolling && !z2) {
                                rect.offset(0, (int) imageViewEmoji.getTranslationY());
                            }
                            Drawable drawable = null;
                            drawable = null;
                            if (imageViewEmoji.empty) {
                                Drawable premiumStar = SelectAnimatedEmojiDialog.this.getPremiumStar();
                                premiumStar.setBounds(rect);
                                premiumStar.setAlpha(255);
                                drawable = premiumStar;
                            } else if (!imageViewEmoji.isDefaultReaction) {
                                if (imageViewEmoji.span != null && !imageViewEmoji.notDraw) {
                                    Drawable drawable2 = (Drawable) SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.get(imageViewEmoji.span.getDocumentId());
                                    AnimatedEmojiDrawable animatedEmojiDrawable = drawable2;
                                    if (drawable2 == null) {
                                        animatedEmojiDrawable = drawable2;
                                        if (Math.min(imageViewEmoji.getScaleX(), imageViewEmoji.getScaleY()) * scaleX > 0.0f) {
                                            animatedEmojiDrawable = drawable2;
                                            if (imageViewEmoji.getAlpha() * f2 > 0.0f) {
                                                AnimatedEmojiDrawable make = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji.span.getDocumentId());
                                                make.addView(EmojiListView.this);
                                                SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.put(imageViewEmoji.span.getDocumentId(), make);
                                                animatedEmojiDrawable = make;
                                            }
                                        }
                                    }
                                    if (animatedEmojiDrawable != null) {
                                        animatedEmojiDrawable.setAlpha(255);
                                        animatedEmojiDrawable.setBounds(rect);
                                        drawable = animatedEmojiDrawable;
                                    }
                                }
                            } else {
                                ImageReceiver imageReceiver = imageViewEmoji.imageReceiver;
                                if (imageReceiver != null) {
                                    imageReceiver.setImageCoords(rect);
                                }
                            }
                            Drawable drawable3 = imageViewEmoji.drawable;
                            if (drawable3 instanceof AnimatedEmojiDrawable) {
                                drawable3.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                            }
                            if (scaleX != 1.0f || this.skewAlpha < 1.0f) {
                                canvas.save();
                                canvas.scale(scaleX, scaleX, rect.centerX(), rect.centerY());
                                skew(canvas, i, imageViewEmoji.getHeight());
                                drawImage(canvas, drawable, imageViewEmoji, f2);
                                canvas.restore();
                            } else {
                                drawImage(canvas, drawable, imageViewEmoji, f2);
                            }
                        }
                    }
                    canvas.restore();
                }
            }

            private void skew(Canvas canvas, int i, int i2) {
                float f = this.skewAlpha;
                if (f < 1.0f) {
                    if (this.skewBelow) {
                        canvas.translate(0.0f, i2);
                        canvas.skew((1.0f - ((i * 2.0f) / this.imageViewEmojis.size())) * (-(1.0f - this.skewAlpha)), 0.0f);
                        canvas.translate(0.0f, -i2);
                        return;
                    }
                    canvas.scale(1.0f, f, 0.0f, 0.0f);
                    canvas.skew((1.0f - ((i * 2.0f) / this.imageViewEmojis.size())) * (1.0f - this.skewAlpha), 0.0f);
                }
            }

            private void drawImage(Canvas canvas, Drawable drawable, ImageViewEmoji imageViewEmoji, float f) {
                ImageReceiver imageReceiver;
                if (drawable != null) {
                    drawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                    drawable.setAlpha((int) (f * 255.0f));
                    drawable.draw(canvas);
                    PremiumLockIconView premiumLockIconView = imageViewEmoji.premiumLockIconView;
                } else if (!imageViewEmoji.isDefaultReaction || (imageReceiver = imageViewEmoji.imageReceiver) == null) {
                } else {
                    imageReceiver.setAlpha(f);
                    imageViewEmoji.imageReceiver.draw(canvas);
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void onFrameReady() {
                super.onFrameReady();
                for (int i = 0; i < this.drawInBackgroundViews.size(); i++) {
                    ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder = this.drawInBackgroundViews.get(i).backgroundThreadDrawHolder;
                    if (backgroundThreadDrawHolder != null) {
                        backgroundThreadDrawHolder.release();
                    }
                }
                SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            SelectAnimatedEmojiDialog.this.bigReactionImageReceiver.onAttachedToWindow();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            AnimatedEmojiSpan.release(this, SelectAnimatedEmojiDialog.this.animatedEmojiDrawables);
            SelectAnimatedEmojiDialog.this.bigReactionImageReceiver.onDetachedFromWindow();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAttached = true;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredEmojiDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentEmojiStatusesUpdate);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setBigReactionAnimatedEmoji(null);
        this.isAttached = false;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredEmojiDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentEmojiStatusesUpdate);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.scrimDrawable;
        if (swapAnimatedEmojiDrawable instanceof AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable) {
            swapAnimatedEmojiDrawable.removeParentView(this);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.stickersDidLoad) {
            if (((Integer) objArr[0]).intValue() != 5) {
                return;
            }
            updateRows(true);
        } else if (i == NotificationCenter.featuredEmojiDidLoad) {
            updateRows(true);
        } else if (i != NotificationCenter.recentEmojiStatusesUpdate) {
        } else {
            updateRows(true);
        }
    }

    public void onShow(Runnable runnable) {
        Integer num = this.listStateId;
        if (num != null) {
            listStates.get(num);
        }
        this.dismiss = runnable;
        if (!this.drawBackground) {
            checkScroll();
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                View childAt = this.emojiGridView.getChildAt(i);
                childAt.setScaleX(1.0f);
                childAt.setScaleY(1.0f);
            }
            return;
        }
        ValueAnimator valueAnimator = this.showAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.showAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.hideAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.hideAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.showAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                SelectAnimatedEmojiDialog.this.lambda$onShow$9(valueAnimator3);
            }
        });
        this.showAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.15
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectAnimatedEmojiDialog.this.checkScroll();
                SelectAnimatedEmojiDialog.this.updateShow(1.0f);
                for (int i2 = 0; i2 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i2++) {
                    View childAt2 = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i2);
                    childAt2.setScaleX(1.0f);
                    childAt2.setScaleY(1.0f);
                }
                for (int i3 = 0; i3 < SelectAnimatedEmojiDialog.this.emojiTabs.contentView.getChildCount(); i3++) {
                    View childAt3 = SelectAnimatedEmojiDialog.this.emojiTabs.contentView.getChildAt(i3);
                    childAt3.setScaleX(1.0f);
                    childAt3.setScaleY(1.0f);
                }
                SelectAnimatedEmojiDialog.this.emojiTabs.contentView.invalidate();
                SelectAnimatedEmojiDialog.this.updateEmojiDrawables();
            }
        });
        updateShow(0.0f);
        this.showAnimator.setDuration(800L);
        this.showAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$9(ValueAnimator valueAnimator) {
        updateShow(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    static {
        new CubicBezierInterpolator(0.0d, 0.0d, 0.65d, 1.04d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShow(float f) {
        if (this.bubble1View != null) {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(MathUtils.clamp((((f * 800.0f) - 0.0f) / 120.0f) / 1.0f, 0.0f, 1.0f));
            this.bubble1View.setAlpha(interpolation);
            this.bubble1View.setScaleX(interpolation);
            this.bubble1View.setScaleY(interpolation);
        }
        if (this.bubble2View != null) {
            float clamp = MathUtils.clamp((((f * 800.0f) - 30.0f) / 120.0f) / 1.0f, 0.0f, 1.0f);
            this.bubble2View.setAlpha(clamp);
            this.bubble2View.setScaleX(clamp);
            this.bubble2View.setScaleY(clamp);
        }
        float f2 = f * 800.0f;
        float f3 = f2 - 40.0f;
        float clamp2 = MathUtils.clamp(f3 / 700.0f, 0.0f, 1.0f);
        float clamp3 = MathUtils.clamp((f2 - 80.0f) / 700.0f, 0.0f, 1.0f);
        float clamp4 = MathUtils.clamp(f3 / 750.0f, 0.0f, 1.0f);
        float clamp5 = MathUtils.clamp((f2 - 30.0f) / 120.0f, 0.0f, 1.0f);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        float interpolation2 = cubicBezierInterpolator.getInterpolation(clamp2);
        float interpolation3 = cubicBezierInterpolator.getInterpolation(clamp3);
        this.contentView.setAlpha(clamp5);
        if (this.scrimDrawable != null) {
            invalidate();
        }
        float f4 = 1.0f - clamp5;
        this.contentView.setTranslationY(AndroidUtilities.dp(-5.0f) * f4);
        View view = this.bubble2View;
        if (view != null) {
            view.setTranslationY(AndroidUtilities.dp(-5.0f) * f4);
        }
        this.scaleX = (interpolation2 * 0.85f) + 0.15f;
        this.scaleY = (interpolation3 * 0.925f) + 0.075f;
        View view2 = this.bubble2View;
        if (view2 != null) {
            view2.setAlpha(clamp5);
        }
        this.contentView.invalidate();
        this.emojiTabsShadow.setAlpha(clamp5);
        this.emojiTabsShadow.setScaleX(Math.min(this.scaleX, 1.0f));
        float pivotX = this.emojiTabsShadow.getPivotX();
        double d = pivotX * pivotX;
        double pow = Math.pow(this.contentView.getHeight(), 2.0d);
        Double.isNaN(d);
        float sqrt = (float) Math.sqrt(Math.max(d + pow, Math.pow(this.contentView.getWidth() - pivotX, 2.0d) + Math.pow(this.contentView.getHeight(), 2.0d)));
        for (int i = 0; i < this.emojiTabs.contentView.getChildCount(); i++) {
            View childAt = this.emojiTabs.contentView.getChildAt(i);
            float top = childAt.getTop() + (childAt.getHeight() / 2.0f);
            float left = (childAt.getLeft() + (childAt.getWidth() / 2.0f)) - pivotX;
            float cascade = AndroidUtilities.cascade(clamp4, (float) Math.sqrt((left * left) + (top * top * 0.4f)), sqrt, childAt.getHeight() * 1.75f);
            if (Float.isNaN(cascade)) {
                cascade = 0.0f;
            }
            childAt.setScaleX(cascade);
            childAt.setScaleY(cascade);
        }
        this.emojiTabs.contentView.invalidate();
        for (int i2 = 0; i2 < this.emojiGridView.getChildCount(); i2++) {
            View childAt2 = this.emojiGridView.getChildAt(i2);
            float top2 = childAt2.getTop() + (childAt2.getHeight() / 2.0f);
            float left2 = (childAt2.getLeft() + (childAt2.getWidth() / 2.0f)) - pivotX;
            float cascade2 = AndroidUtilities.cascade(clamp4, (float) Math.sqrt((left2 * left2) + (top2 * top2 * 0.2f)), sqrt, childAt2.getHeight() * 1.75f);
            if (Float.isNaN(cascade2)) {
                cascade2 = 0.0f;
            }
            childAt2.setScaleX(cascade2);
            childAt2.setScaleY(cascade2);
        }
        this.emojiGridView.invalidate();
    }

    public void onDismiss(final Runnable runnable) {
        Integer num = this.listStateId;
        if (num != null) {
            listStates.put(num, this.layoutManager.onSaveInstanceState());
        }
        ValueAnimator valueAnimator = this.hideAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.hideAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.hideAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                SelectAnimatedEmojiDialog.this.lambda$onDismiss$10(valueAnimator2);
            }
        });
        this.hideAnimator.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.16
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }
        });
        this.hideAnimator.setDuration(200L);
        this.hideAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.hideAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDismiss$10(ValueAnimator valueAnimator) {
        float floatValue = 1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue();
        setTranslationY(AndroidUtilities.dp(8.0f) * (1.0f - floatValue));
        View view = this.bubble1View;
        if (view != null) {
            view.setAlpha(floatValue);
        }
        View view2 = this.bubble2View;
        if (view2 != null) {
            view2.setAlpha(floatValue * floatValue);
        }
        this.contentView.setAlpha(floatValue);
        this.contentView.invalidate();
        invalidate();
    }

    public void setDrawBackground(boolean z) {
        this.drawBackground = z;
    }

    public void setRecentReactions(List<ReactionsLayoutInBubble.VisibleReaction> list) {
        this.recentReactionsToSet = list;
        updateRows(true);
    }

    public void resetBackgroundBitmaps() {
        for (int i = 0; i < this.lineDrawables.size(); i++) {
            EmojiListView.DrawingInBackgroundLine drawingInBackgroundLine = this.lineDrawables.get(i);
            for (int i2 = 0; i2 < drawingInBackgroundLine.imageViewEmojis.size(); i2++) {
                if (drawingInBackgroundLine.imageViewEmojis.get(i2).notDraw) {
                    drawingInBackgroundLine.imageViewEmojis.get(i2).notDraw = false;
                    drawingInBackgroundLine.imageViewEmojis.get(i2).invalidate();
                    drawingInBackgroundLine.reset();
                }
            }
        }
        this.emojiGridView.invalidate();
    }

    public void setSelected(Long l) {
        this.selectedDocumentIds.clear();
        this.selectedDocumentIds.add(l);
    }

    public void setScrimDrawable(AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable, View view) {
        this.scrimColor = swapAnimatedEmojiDrawable == null ? 0 : swapAnimatedEmojiDrawable.getColor().intValue();
        this.scrimDrawable = swapAnimatedEmojiDrawable;
        this.scrimDrawableParent = view;
        swapAnimatedEmojiDrawable.addParentView(this);
    }

    public void drawBigReaction(Canvas canvas, View view) {
        if (this.selectedReactionView == null) {
            return;
        }
        this.bigReactionImageReceiver.setParentView(view);
        ImageViewEmoji imageViewEmoji = this.selectedReactionView;
        if (imageViewEmoji == null) {
            return;
        }
        float f = this.pressedProgress;
        if (f != 1.0f && !this.cancelPressed) {
            float f2 = f + 0.010666667f;
            this.pressedProgress = f2;
            if (f2 >= 1.0f) {
                this.pressedProgress = 1.0f;
                onLongPressedListener onlongpressedlistener = this.bigReactionListener;
                if (onlongpressedlistener != null) {
                    onlongpressedlistener.onLongPressed(imageViewEmoji);
                }
            }
            this.selectedReactionView.bigReactionSelectedProgress = this.pressedProgress;
        }
        float f3 = (this.pressedProgress * 2.0f) + 1.0f;
        canvas.save();
        canvas.translate(this.emojiGridView.getX() + this.selectedReactionView.getX(), this.emojiGridView.getY() + this.selectedReactionView.getY());
        this.paint.setColor(Theme.getColor("actionBarDefaultSubmenuBackground", this.resourcesProvider));
        canvas.drawRect(0.0f, 0.0f, this.selectedReactionView.getMeasuredWidth(), this.selectedReactionView.getMeasuredHeight(), this.paint);
        canvas.scale(f3, f3, this.selectedReactionView.getMeasuredWidth() / 2.0f, this.selectedReactionView.getMeasuredHeight());
        ImageViewEmoji imageViewEmoji2 = this.selectedReactionView;
        ImageReceiver imageReceiver = imageViewEmoji2.isDefaultReaction ? this.bigReactionImageReceiver : imageViewEmoji2.imageReceiverToDraw;
        AnimatedEmojiDrawable animatedEmojiDrawable = this.bigReactionAnimatedEmoji;
        if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null && this.bigReactionAnimatedEmoji.getImageReceiver().hasBitmapImage()) {
            imageReceiver = this.bigReactionAnimatedEmoji.getImageReceiver();
        }
        if (imageReceiver != null) {
            imageReceiver.setImageCoords(0.0f, 0.0f, this.selectedReactionView.getMeasuredWidth(), this.selectedReactionView.getMeasuredHeight());
            imageReceiver.draw(canvas);
        }
        canvas.restore();
        view.invalidate();
    }

    public void setSaveState(int i) {
        this.listStateId = Integer.valueOf(i);
    }

    public void setOnLongPressedListener(onLongPressedListener onlongpressedlistener) {
        this.bigReactionListener = onlongpressedlistener;
    }

    public void setOnRecentClearedListener(onRecentClearedListener onrecentclearedlistener) {
        this.onRecentClearedListener = onrecentclearedlistener;
    }
}
