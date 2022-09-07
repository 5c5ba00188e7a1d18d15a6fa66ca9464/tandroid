package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.util.Consumer;
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
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmojiDefaultStatuses;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
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
    private int animateExpandFromPosition;
    private long animateExpandStartTime;
    private int animateExpandToPosition;
    private LongSparseArray<AnimatedEmojiDrawable> animatedEmojiDrawables;
    private BaseFragment baseFragment;
    AnimatedEmojiDrawable bigReactionAnimatedEmoji;
    ImageReceiver bigReactionImageReceiver;
    public onLongPressedListener bigReactionListener;
    private boolean bottomGradientShown;
    private View bottomGradientView;
    private View bubble1View;
    private View bubble2View;
    public boolean cancelPressed;
    private FrameLayout contentView;
    private View contentViewForeground;
    private int currentAccount;
    private boolean defaultSetLoading;
    private ArrayList<AnimatedEmojiSpan> defaultStatuses;
    private ValueAnimator dimAnimator;
    private Runnable dismiss;
    private boolean drawBackground;
    private Rect drawableToBounds;
    public RecyclerListView emojiGridView;
    private float emojiSelectAlpha;
    private ValueAnimator emojiSelectAnimator;
    private Rect emojiSelectRect;
    private ImageViewEmoji emojiSelectView;
    private EmojiTabsStrip emojiTabs;
    private View emojiTabsShadow;
    private Integer emojiX;
    private ArrayList<Long> expandedEmojiSets;
    private ValueAnimator hideAnimator;
    private Integer hintExpireDate;
    private boolean includeEmpty;
    private boolean includeHint;
    private ArrayList<Long> installedEmojiSets;
    private boolean isAttached;
    private GridLayoutManager layoutManager;
    ArrayList<EmojiListView.DrawingInBackgroundLine> lineDrawables;
    ArrayList<EmojiListView.DrawingInBackgroundLine> lineDrawablesTmp;
    private Integer listStateId;
    private int longtapHintRow;
    public onRecentClearedListener onRecentClearedListener;
    private OvershootInterpolator overshootInterpolator;
    private ArrayList<EmojiView.EmojiPack> packs;
    Paint paint;
    private int popularSectionRow;
    private SparseIntArray positionToButton;
    private SparseIntArray positionToExpand;
    private SparseIntArray positionToSection;
    private Drawable premiumStar;
    private ColorFilter premiumStarColorFilter;
    float pressedProgress;
    private ArrayList<AnimatedEmojiSpan> recent;
    private boolean recentExpanded;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> recentReactions;
    private int recentReactionsEndRow;
    private int recentReactionsSectionRow;
    private int recentReactionsStartRow;
    private List<ReactionsLayoutInBubble.VisibleReaction> recentReactionsToSet;
    private Theme.ResourcesProvider resourcesProvider;
    private ArrayList<Integer> rowHashCodes;
    private float scaleX;
    private float scaleY;
    private float scrimAlpha;
    private int scrimColor;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable scrimDrawable;
    private View scrimDrawableParent;
    private RecyclerAnimationScrollHelper scrollHelper;
    private SparseIntArray sectionToPosition;
    HashSet<Long> selectedDocumentIds;
    ImageViewEmoji selectedReactionView;
    HashSet<ReactionsLayoutInBubble.VisibleReaction> selectedReactions;
    public Paint selectorAccentPaint;
    public Paint selectorPaint;
    private ValueAnimator showAnimator;
    private boolean smoothScrolling;
    private View topGradientView;
    private int topMarginDp;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> topReactions;
    private int topReactionsEndRow;
    private int topReactionsStartRow;
    private int totalCount;
    private int type;

    /* loaded from: classes3.dex */
    public interface onLongPressedListener {
        void onLongPressed(ImageViewEmoji imageViewEmoji);
    }

    /* loaded from: classes3.dex */
    public interface onRecentClearedListener {
        void onRecentCleared();
    }

    protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
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

    public SelectAnimatedEmojiDialog(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, Theme.ResourcesProvider resourcesProvider) {
        this(baseFragment, context, z, num, i, resourcesProvider, 16);
    }

    public SelectAnimatedEmojiDialog(BaseFragment baseFragment, Context context, boolean z, Integer num, final int i, final Theme.ResourcesProvider resourcesProvider, int i2) {
        super(context);
        this.lineDrawables = new ArrayList<>();
        this.lineDrawablesTmp = new ArrayList<>();
        this.selectedReactions = new HashSet<>();
        this.selectedDocumentIds = new HashSet<>();
        this.selectorPaint = new Paint(1);
        this.selectorAccentPaint = new Paint(1);
        this.currentAccount = UserConfig.selectedAccount;
        this.rowHashCodes = new ArrayList<>();
        this.positionToSection = new SparseIntArray();
        this.sectionToPosition = new SparseIntArray();
        this.positionToExpand = new SparseIntArray();
        this.positionToButton = new SparseIntArray();
        this.expandedEmojiSets = new ArrayList<>();
        this.installedEmojiSets = new ArrayList<>();
        this.recentExpanded = false;
        this.recent = new ArrayList<>();
        this.topReactions = new ArrayList<>();
        this.recentReactions = new ArrayList<>();
        this.defaultStatuses = new ArrayList<>();
        this.packs = new ArrayList<>();
        this.includeEmpty = false;
        this.includeHint = false;
        this.drawBackground = true;
        this.bigReactionImageReceiver = new ImageReceiver();
        this.scrimAlpha = 1.0f;
        this.emojiSelectAlpha = 1.0f;
        this.overshootInterpolator = new OvershootInterpolator(2.0f);
        this.bottomGradientShown = false;
        this.smoothScrolling = false;
        this.animateExpandFromPosition = -1;
        this.animateExpandToPosition = -1;
        this.animateExpandStartTime = -1L;
        this.defaultSetLoading = false;
        this.animatedEmojiDrawables = new LongSparseArray<>();
        this.paint = new Paint();
        this.resourcesProvider = resourcesProvider;
        this.type = i;
        this.includeEmpty = z;
        this.baseFragment = baseFragment;
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        StringBuilder sb = new StringBuilder();
        sb.append("emoji");
        sb.append(i == 0 ? "status" : "reaction");
        sb.append("usehint");
        this.includeHint = globalMainSettings.getInt(sb.toString(), 0) < 3;
        this.selectorPaint.setColor(Theme.getColor("listSelectorSDK21", resourcesProvider));
        this.selectorAccentPaint.setColor(ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhiteBlueIcon", resourcesProvider), 30));
        this.premiumStarColorFilter = new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlueIcon", resourcesProvider), PorterDuff.Mode.MULTIPLY);
        this.emojiX = num;
        final Integer valueOf = num == null ? null : Integer.valueOf(MathUtils.clamp(num.intValue(), AndroidUtilities.dp(26.0f), AndroidUtilities.dp(292.0f)));
        boolean z2 = valueOf != null && valueOf.intValue() > AndroidUtilities.dp(170.0f);
        setFocusableInTouchMode(true);
        if (i == 0 || i == 2) {
            this.topMarginDp = i2;
            setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda7
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$new$0;
                    lambda$new$0 = SelectAnimatedEmojiDialog.this.lambda$new$0(view, motionEvent);
                    return lambda$new$0;
                }
            });
        }
        if (valueOf != null) {
            this.bubble1View = new View(context);
            Drawable mutate = getResources().getDrawable(R.drawable.shadowed_bubble1).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuBackground", resourcesProvider), PorterDuff.Mode.MULTIPLY));
            this.bubble1View.setBackground(mutate);
            addView(this.bubble1View, LayoutHelper.createFrame(10, 10.0f, 51, (valueOf.intValue() / AndroidUtilities.density) + (z2 ? -12 : 4), this.topMarginDp, 0.0f, 0.0f));
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
        if (i == 0 || i == 2) {
            frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        }
        addView(this.contentView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, (i == 0 || i == 2) ? this.topMarginDp + 6 : 0.0f, 0.0f, 0.0f));
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
            addView(this.bubble2View, LayoutHelper.createFrame(17, 9.0f, 51, (valueOf.intValue() / AndroidUtilities.density) + (z2 ? -25 : 10), this.topMarginDp + 5, 0.0f, 0.0f));
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
                SelectAnimatedEmojiDialog.this.emojiGridView.scrolledByUserOnce = true;
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
        emojiTabsStrip.recentTab.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda6
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$new$1;
                lambda$new$1 = SelectAnimatedEmojiDialog.this.lambda$new$1(view);
                return lambda$new$1;
            }
        });
        EmojiTabsStrip emojiTabsStrip2 = this.emojiTabs;
        emojiTabsStrip2.updateButtonDrawables = false;
        emojiTabsStrip2.setAnimatedEmojiCacheType((i == 0 || i == 2) ? 6 : 5);
        EmojiTabsStrip emojiTabsStrip3 = this.emojiTabs;
        emojiTabsStrip3.animateAppear = num2 == null;
        emojiTabsStrip3.setPaddingLeft(5.0f);
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
        EmojiListView emojiListView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.5
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

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
                if (((org.telegram.ui.Components.AnimatedEmojiDrawable) r0).canOverrideColor() != false) goto L9;
             */
            @Override // org.telegram.ui.Components.RecyclerListView
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean canHighlightChildAt(View view2, float f, float f2) {
                if (view2 instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view2;
                    if (!imageViewEmoji.empty) {
                        Drawable drawable2 = imageViewEmoji.drawable;
                        if (drawable2 instanceof AnimatedEmojiDrawable) {
                        }
                    }
                    setSelectorDrawableColor(ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhiteBlueIcon", this.resourcesProvider), 30));
                    return super.canHighlightChildAt(view2, f, f2);
                }
                setSelectorDrawableColor(Theme.getColor("listSelectorSDK21", this.resourcesProvider));
                return super.canHighlightChildAt(view2, f, f2);
            }
        };
        this.emojiGridView = emojiListView;
        emojiListView.setDrawSelectorBehind(true);
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
        this.emojiGridView.setOnItemLongClickListener(new 10(i, context, resourcesProvider, num), ViewConfiguration.getLongPressTimeout() * 0.25f);
        this.emojiGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda11
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i3) {
                SelectAnimatedEmojiDialog.this.lambda$new$2(i, view2, i3);
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
        updateRows(false);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(View view) {
        onRecentLongClick();
        try {
            performHapticFeedback(0, 1);
        } catch (Exception unused) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 10 implements RecyclerListView.OnItemLongClickListenerExtended {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Integer val$emojiX;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ int val$type;

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public /* synthetic */ void onMove(float f, float f2) {
            RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onMove(this, f, f2);
        }

        10(int i, Context context, Theme.ResourcesProvider resourcesProvider, Integer num) {
            this.val$type = i;
            this.val$context = context;
            this.val$resourcesProvider = resourcesProvider;
            this.val$emojiX = num;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(final View view, int i, float f, float f2) {
            boolean z = view instanceof ImageViewEmoji;
            if (!z || this.val$type != 1) {
                if (z) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                    if (imageViewEmoji.span != null && this.val$type == 0) {
                        new SelectStatusDurationDialog(this.val$context, SelectAnimatedEmojiDialog.this.dismiss, SelectAnimatedEmojiDialog.this, imageViewEmoji, this.val$resourcesProvider) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10.1
                            {
                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                            }

                            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog
                            protected boolean getOutBounds(Rect rect) {
                                if (SelectAnimatedEmojiDialog.this.scrimDrawable != null) {
                                    10 r0 = 10.this;
                                    if (r0.val$emojiX == null) {
                                        return false;
                                    }
                                    rect.set(SelectAnimatedEmojiDialog.this.drawableToBounds);
                                    return true;
                                }
                                return false;
                            }

                            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog
                            protected void onEndPartly(Integer num) {
                                SelectAnimatedEmojiDialog.this.incrementHintUse();
                                TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
                                View view2 = view;
                                long j = ((ImageViewEmoji) view2).span.documentId;
                                tLRPC$TL_emojiStatus.document_id = j;
                                SelectAnimatedEmojiDialog.this.onEmojiSelected(view2, Long.valueOf(j), ((ImageViewEmoji) view).span.document, num);
                                MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).pushRecentEmojiStatus(tLRPC$TL_emojiStatus);
                            }

                            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog
                            protected void onEnd(Integer num) {
                                if (num == null || SelectAnimatedEmojiDialog.this.dismiss == null) {
                                    return;
                                }
                                SelectAnimatedEmojiDialog.this.dismiss.run();
                            }
                        }.show();
                        try {
                            view.performHapticFeedback(0, 1);
                        } catch (Exception unused) {
                        }
                        return true;
                    }
                }
                return false;
            }
            SelectAnimatedEmojiDialog.this.incrementHintUse();
            SelectAnimatedEmojiDialog.this.performHapticFeedback(0);
            ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) view;
            if (!imageViewEmoji2.isDefaultReaction && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                TLRPC$Document tLRPC$Document = imageViewEmoji2.span.document;
                if (tLRPC$Document == null) {
                    tLRPC$Document = AnimatedEmojiDrawable.findDocument(SelectAnimatedEmojiDialog.this.currentAccount, imageViewEmoji2.span.documentId);
                }
                SelectAnimatedEmojiDialog.this.onEmojiSelected(imageViewEmoji2, Long.valueOf(imageViewEmoji2.span.documentId), tLRPC$Document, null);
                return true;
            }
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            selectAnimatedEmojiDialog.selectedReactionView = imageViewEmoji2;
            selectAnimatedEmojiDialog.pressedProgress = 0.0f;
            selectAnimatedEmojiDialog.cancelPressed = false;
            if (imageViewEmoji2.isDefaultReaction) {
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
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10.2
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
    public /* synthetic */ void lambda$new$2(int i, View view, int i2) {
        try {
            if (view instanceof ImageViewEmoji) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                if (imageViewEmoji.isDefaultReaction) {
                    incrementHintUse();
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

    public void setExpireDateHint(int i) {
        this.includeHint = true;
        this.hintExpireDate = Integer.valueOf(i);
        updateRows(false);
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

    private void onRecentLongClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), null);
        builder.setTitle(LocaleController.getString("ClearRecentEmojiStatusesTitle", R.string.ClearRecentEmojiStatusesTitle));
        builder.setMessage(LocaleController.getString("ClearRecentEmojiStatusesText", R.string.ClearRecentEmojiStatusesText));
        builder.setPositiveButton(LocaleController.getString("Clear", R.string.Clear).toUpperCase(), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$3(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setDimEnabled(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda5
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$4(dialogInterface);
            }
        });
        builder.show();
        setDim(1.0f, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecentLongClick$3(DialogInterface dialogInterface, int i) {
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
    public /* synthetic */ void lambda$onRecentLongClick$4(DialogInterface dialogInterface) {
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
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.this.lambda$setDim$5(valueAnimator2);
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
    public /* synthetic */ void lambda$setDim$5(ValueAnimator valueAnimator) {
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
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.scrimDrawable;
        if (swapAnimatedEmojiDrawable != null && this.emojiX != null) {
            Rect bounds = swapAnimatedEmojiDrawable.getBounds();
            View view = this.scrimDrawableParent;
            float scaleY = view == null ? 1.0f : view.getScaleY();
            int i = 255;
            if (Build.VERSION.SDK_INT >= 19) {
                i = this.scrimDrawable.getAlpha();
            }
            View view2 = this.scrimDrawableParent;
            int height = view2 == null ? bounds.height() : view2.getHeight();
            canvas.save();
            canvas.translate(0.0f, -getTranslationY());
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.scrimDrawable;
            double d = i;
            double pow = Math.pow(this.contentView.getAlpha(), 0.25d);
            Double.isNaN(d);
            double d2 = d * pow;
            double d3 = this.scrimAlpha;
            Double.isNaN(d3);
            swapAnimatedEmojiDrawable2.setAlpha((int) (d2 * d3));
            if (this.drawableToBounds == null) {
                this.drawableToBounds = new Rect();
            }
            this.drawableToBounds.set((int) (((bounds.centerX() - ((bounds.width() / 2.0f) * scaleY)) - bounds.centerX()) + this.emojiX.intValue() + ((scaleY <= 1.0f || scaleY >= 1.5f) ? 0 : 2)), (int) (((((((height - (height - bounds.bottom)) * scaleY) - (scaleY > 1.5f ? (bounds.height() * 0.81f) + 1.0f : 0.0f)) - bounds.top) - (bounds.height() / 2.0f)) + AndroidUtilities.dp(this.topMarginDp)) - (bounds.height() * scaleY)), (int) (((bounds.centerX() + ((bounds.width() / 2.0f) * scaleY)) - bounds.centerX()) + this.emojiX.intValue() + ((scaleY <= 1.0f || scaleY >= 1.5f) ? 0 : 2)), (int) ((((((height - (height - bounds.bottom)) * scaleY) - (scaleY > 1.5f ? (bounds.height() * 0.81f) + 1.0f : 0.0f)) - bounds.top) - (bounds.height() / 2.0f)) + AndroidUtilities.dp(this.topMarginDp)));
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable3 = this.scrimDrawable;
            Rect rect = this.drawableToBounds;
            int i2 = rect.left;
            Rect rect2 = this.drawableToBounds;
            swapAnimatedEmojiDrawable3.setBounds(i2, rect.top, (int) (i2 + (rect.width() / scaleY)), (int) (rect2.top + (rect2.height() / scaleY)));
            Rect rect3 = this.drawableToBounds;
            canvas.scale(scaleY, scaleY, rect3.left, rect3.top);
            this.scrimDrawable.draw(canvas);
            this.scrimDrawable.setAlpha(i);
            this.scrimDrawable.setBounds(bounds);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
        ImageViewEmoji imageViewEmoji = this.emojiSelectView;
        if (imageViewEmoji == null || this.emojiSelectRect == null || this.drawableToBounds == null || imageViewEmoji.drawable == null) {
            return;
        }
        canvas.save();
        canvas.translate(0.0f, -getTranslationY());
        this.emojiSelectView.drawable.setAlpha((int) (this.emojiSelectAlpha * 255.0f));
        this.emojiSelectView.drawable.setBounds(this.emojiSelectRect);
        this.emojiSelectView.drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhiteBlueIcon", this.resourcesProvider), this.scrimColor, 1.0f - this.scrimAlpha), PorterDuff.Mode.MULTIPLY));
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
        Drawable drawable = imageViewEmoji.drawable;
        final AnimatedEmojiDrawable make = drawable instanceof AnimatedEmojiDrawable ? AnimatedEmojiDrawable.make(this.currentAccount, 7, ((AnimatedEmojiDrawable) drawable).getDocumentId()) : null;
        this.emojiSelectView = imageViewEmoji;
        Rect rect2 = new Rect();
        this.emojiSelectRect = rect2;
        rect2.set(rect);
        final boolean[] zArr = new boolean[1];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.emojiSelectAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SelectAnimatedEmojiDialog.this.lambda$animateEmojiSelect$6(rect, imageViewEmoji, zArr, runnable, make, valueAnimator);
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
    public /* synthetic */ void lambda$animateEmojiSelect$6(Rect rect, ImageViewEmoji imageViewEmoji, boolean[] zArr, Runnable runnable, AnimatedEmojiDrawable animatedEmojiDrawable, ValueAnimator valueAnimator) {
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.scrimAlpha = 1.0f - ((floatValue * floatValue) * floatValue);
        this.emojiSelectAlpha = 1.0f - ((float) Math.pow(floatValue, 10.0d));
        AndroidUtilities.lerp(rect, this.drawableToBounds, floatValue, this.emojiSelectRect);
        float max = Math.max(1.0f, this.overshootInterpolator.getInterpolation(MathUtils.clamp((3.0f * floatValue) - 2.0f, 0.0f, 1.0f))) * imageViewEmoji.getScaleX();
        Rect rect2 = this.emojiSelectRect;
        rect2.set((int) (rect2.centerX() - ((this.emojiSelectRect.width() / 2.0f) * max)), (int) (this.emojiSelectRect.centerY() - ((this.emojiSelectRect.height() / 2.0f) * max)), (int) (this.emojiSelectRect.centerX() + ((this.emojiSelectRect.width() / 2.0f) * max)), (int) (this.emojiSelectRect.centerY() + ((this.emojiSelectRect.height() / 2.0f) * max)));
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
                if (SelectAnimatedEmojiDialog.this.type == 0) {
                    textView2.setText(LocaleController.getString("EmojiLongtapHint", R.string.EmojiLongtapHint));
                } else {
                    textView2.setText(LocaleController.getString("ReactionsLongtapHint", R.string.ReactionsLongtapHint));
                }
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

        /* JADX WARN: Code restructure failed: missing block: B:115:0x0391, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x0479, code lost:
            r6 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:130:0x03f3, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:159:0x0477, code lost:
            if (r20.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L116;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Document tLRPC$Document;
            boolean z;
            final EmojiView.EmojiPack emojiPack;
            int itemViewType = viewHolder.getItemViewType();
            if (SelectAnimatedEmojiDialog.this.showAnimator == null || !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
            boolean z2 = false;
            if (itemViewType == 6) {
                TextView textView = (TextView) viewHolder.itemView;
                if (SelectAnimatedEmojiDialog.this.hintExpireDate == null) {
                    return;
                }
                textView.setText(LocaleController.formatString("EmojiStatusExpireHint", R.string.EmojiStatusExpireHint, LocaleController.formatStatusExpireDateTime(SelectAnimatedEmojiDialog.this.hintExpireDate.intValue())));
                return;
            }
            TLRPC$Document tLRPC$Document2 = null;
            r8 = null;
            EmojiView.EmojiPack emojiPack2 = null;
            TLRPC$Document tLRPC$Document3 = null;
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
                    int i2 = SelectAnimatedEmojiDialog.this.positionToSection.get(i);
                    if (i2 >= 0) {
                        EmojiView.EmojiPack emojiPack3 = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i2);
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
                    imageViewEmoji.document = null;
                    imageViewEmoji.setDrawable(null);
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
                imageViewEmoji.document = null;
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
                int i3 = SelectAnimatedEmojiDialog.this.positionToExpand.get(i);
                if (i3 >= 0 && i3 < SelectAnimatedEmojiDialog.this.packs.size()) {
                    emojiPack2 = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i3);
                }
                if (i3 == -1) {
                    TextView textView2 = emojiPackExpand.textView;
                    textView2.setText("+" + ((SelectAnimatedEmojiDialog.this.recent.size() - (SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 5)) + (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0) + 1));
                } else if (emojiPack2 != null) {
                    TextView textView3 = emojiPackExpand.textView;
                    textView3.setText("+" + ((emojiPack2.documents.size() - (SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() * 3)) + 1));
                }
            } else if (itemViewType == 5) {
                EmojiPackButton emojiPackButton = (EmojiPackButton) viewHolder.itemView;
                final int i4 = SelectAnimatedEmojiDialog.this.positionToButton.get(i);
                if (i4 < 0 || i4 >= SelectAnimatedEmojiDialog.this.packs.size() || (emojiPack = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i4)) == null) {
                    return;
                }
                String str2 = emojiPack.set.title;
                if (!emojiPack.free && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                    z2 = true;
                }
                emojiPackButton.set(str2, z2, emojiPack.installed, new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$Adapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        SelectAnimatedEmojiDialog.Adapter.this.lambda$onBindViewHolder$1(emojiPack, i4, view);
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
                imageViewEmoji2.setDrawable(null);
                if (SelectAnimatedEmojiDialog.this.includeEmpty && i == SelectAnimatedEmojiDialog.this.includeHint) {
                    z = SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(null);
                    imageViewEmoji2.empty = true;
                    imageViewEmoji2.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
                    imageViewEmoji2.span = null;
                    imageViewEmoji2.document = null;
                } else if (i - (SelectAnimatedEmojiDialog.this.includeHint ? 1 : 0) < spanCount) {
                    AnimatedEmojiSpan animatedEmojiSpan = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.recent.get((i - (SelectAnimatedEmojiDialog.this.includeHint ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0));
                    imageViewEmoji2.span = animatedEmojiSpan;
                    if (animatedEmojiSpan != null) {
                        tLRPC$Document3 = animatedEmojiSpan.document;
                    }
                    imageViewEmoji2.document = tLRPC$Document3;
                    if (animatedEmojiSpan != null) {
                    }
                    z = z2;
                } else if (SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty() || ((i - (SelectAnimatedEmojiDialog.this.includeHint ? 1 : 0)) - spanCount) - 1 < 0 || ((i - (SelectAnimatedEmojiDialog.this.includeHint ? 1 : 0)) - spanCount) - 1 >= SelectAnimatedEmojiDialog.this.defaultStatuses.size()) {
                    for (int i5 = 0; i5 < SelectAnimatedEmojiDialog.this.positionToSection.size(); i5++) {
                        int keyAt = SelectAnimatedEmojiDialog.this.positionToSection.keyAt(i5);
                        int i6 = i5 - (!SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty());
                        EmojiView.EmojiPack emojiPack4 = i6 >= 0 ? (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i6) : null;
                        if (emojiPack4 != null) {
                            int size = emojiPack4.expanded ? emojiPack4.documents.size() : Math.min(emojiPack4.documents.size(), spanCount2);
                            if (i > keyAt && i <= keyAt + 1 + size && (tLRPC$Document = emojiPack4.documents.get((i - keyAt) - 1)) != null) {
                                imageViewEmoji2.span = new AnimatedEmojiSpan(tLRPC$Document, (Paint.FontMetricsInt) null);
                                imageViewEmoji2.document = tLRPC$Document;
                            }
                        }
                    }
                    AnimatedEmojiSpan animatedEmojiSpan2 = imageViewEmoji2.span;
                    if (animatedEmojiSpan2 != null) {
                    }
                    z = z2;
                } else {
                    AnimatedEmojiSpan animatedEmojiSpan3 = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.defaultStatuses.get(((i - (SelectAnimatedEmojiDialog.this.includeHint ? 1 : 0)) - spanCount) - 1);
                    imageViewEmoji2.span = animatedEmojiSpan3;
                    if (animatedEmojiSpan3 != null) {
                        tLRPC$Document2 = animatedEmojiSpan3.document;
                    }
                    imageViewEmoji2.document = tLRPC$Document2;
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
                new PremiumFeatureBottomSheet(SelectAnimatedEmojiDialog.this.baseFragment, SelectAnimatedEmojiDialog.this.getContext(), SelectAnimatedEmojiDialog.this.currentAccount, 11, false).show();
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
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
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
        public boolean attached;
        ValueAnimator backAnimator;
        public ImageReceiver.BackgroundThreadDrawHolder backgroundThreadDrawHolder;
        public float bigReactionSelectedProgress;
        public TLRPC$Document document;
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
        public float skewAlpha;
        public int skewIndex;
        public AnimatedEmojiSpan span;
        public boolean empty = false;
        public boolean notDraw = false;
        final AnimatedEmojiSpan.InvalidateHolder invalidateHolder = new AnimatedEmojiSpan.InvalidateHolder() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.ImageViewEmoji.1
            @Override // org.telegram.ui.Components.AnimatedEmojiSpan.InvalidateHolder
            public void invalidate() {
                RecyclerListView recyclerListView = SelectAnimatedEmojiDialog.this.emojiGridView;
                if (recyclerListView != null) {
                    recyclerListView.invalidate();
                }
            }
        };

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
                this.backAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.ImageViewEmoji.2
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

        public void drawSelected(Canvas canvas) {
            Paint paint;
            if (!this.selected || this.notDraw) {
                return;
            }
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            rectF.inset(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
            float dp = AndroidUtilities.dp(4.0f);
            float dp2 = AndroidUtilities.dp(4.0f);
            if (!this.empty) {
                Drawable drawable = this.drawable;
                if (!(drawable instanceof AnimatedEmojiDrawable) || !((AnimatedEmojiDrawable) drawable).canOverrideColor()) {
                    paint = SelectAnimatedEmojiDialog.this.selectorPaint;
                    canvas.drawRoundRect(rectF, dp, dp2, paint);
                }
            }
            paint = SelectAnimatedEmojiDialog.this.selectorAccentPaint;
            canvas.drawRoundRect(rectF, dp, dp2, paint);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attached = true;
            Drawable drawable = this.drawable;
            if (drawable instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawable).addView(this.invalidateHolder);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attached = false;
            Drawable drawable = this.drawable;
            if (drawable instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawable).removeView(this.invalidateHolder);
            }
        }

        public void setDrawable(Drawable drawable) {
            Drawable drawable2 = this.drawable;
            if (drawable2 != drawable) {
                if (drawable2 != null && (drawable2 instanceof AnimatedEmojiDrawable)) {
                    ((AnimatedEmojiDrawable) drawable2).removeView(this.invalidateHolder);
                }
                this.drawable = drawable;
                if (!this.attached || !(drawable instanceof AnimatedEmojiDrawable)) {
                    return;
                }
                ((AnimatedEmojiDrawable) drawable).addView(this.invalidateHolder);
            }
        }
    }

    public void onEmojiClick(final View view, final AnimatedEmojiSpan animatedEmojiSpan) {
        incrementHintUse();
        if (animatedEmojiSpan == null) {
            onEmojiSelected(view, null, null, null);
            return;
        }
        TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
        tLRPC$TL_emojiStatus.document_id = animatedEmojiSpan.getDocumentId();
        final TLRPC$Document tLRPC$Document = animatedEmojiSpan.document;
        if (tLRPC$Document == null) {
            tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.documentId);
        }
        if (view instanceof ImageViewEmoji) {
            if (this.type == 0) {
                MediaDataController.getInstance(this.currentAccount).pushRecentEmojiStatus(tLRPC$TL_emojiStatus);
            }
            int i = this.type;
            if (i == 0 || i == 2) {
                animateEmojiSelect((ImageViewEmoji) view, new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.this.lambda$onEmojiClick$7(view, animatedEmojiSpan, tLRPC$Document);
                    }
                });
                return;
            } else {
                onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document, null);
                return;
            }
        }
        onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmojiClick$7(View view, AnimatedEmojiSpan animatedEmojiSpan, TLRPC$Document tLRPC$Document) {
        onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementHintUse() {
        if (this.type == 2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("emoji");
        sb.append(this.type == 0 ? "status" : "reaction");
        sb.append("usehint");
        String sb2 = sb.toString();
        int i = MessagesController.getGlobalMainSettings().getInt(sb2, 0);
        if (i > 3) {
            return;
        }
        MessagesController.getGlobalMainSettings().edit().putInt(sb2, i + 1).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows(boolean z) {
        long j;
        boolean z2;
        Iterator<TLRPC$EmojiStatus> it;
        long j2;
        boolean z3;
        boolean z4;
        TLRPC$StickerSet tLRPC$StickerSet;
        boolean z5;
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
        if (this.includeHint && this.type != 2) {
            int i = this.totalCount;
            this.totalCount = i + 1;
            this.longtapHintRow = i;
            this.rowHashCodes.add(6);
        }
        if (this.recentReactionsToSet != null) {
            this.topReactionsStartRow = this.totalCount;
            ArrayList arrayList4 = new ArrayList();
            arrayList4.addAll(this.recentReactionsToSet);
            for (int i2 = 0; i2 < 16; i2++) {
                if (!arrayList4.isEmpty()) {
                    this.topReactions.add((ReactionsLayoutInBubble.VisibleReaction) arrayList4.remove(0));
                }
            }
            for (int i3 = 0; i3 < this.topReactions.size(); i3++) {
                this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{-5632, Integer.valueOf(this.topReactions.get(i3).hashCode())})));
            }
            int size = this.totalCount + this.topReactions.size();
            this.totalCount = size;
            this.topReactionsEndRow = size;
            if (!arrayList4.isEmpty()) {
                int i4 = 0;
                while (true) {
                    if (i4 >= arrayList4.size()) {
                        z5 = true;
                        break;
                    } else if (((ReactionsLayoutInBubble.VisibleReaction) arrayList4.get(i4)).documentId != 0) {
                        z5 = false;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (z5) {
                    if (UserConfig.getInstance(this.currentAccount).isPremium()) {
                        int i5 = this.totalCount;
                        this.totalCount = i5 + 1;
                        this.popularSectionRow = i5;
                        this.rowHashCodes.add(5);
                    }
                } else {
                    int i6 = this.totalCount;
                    this.totalCount = i6 + 1;
                    this.recentReactionsSectionRow = i6;
                    this.rowHashCodes.add(4);
                }
                this.recentReactionsStartRow = this.totalCount;
                this.recentReactions.addAll(arrayList4);
                for (int i7 = 0; i7 < this.recentReactions.size(); i7++) {
                    ArrayList<Integer> arrayList5 = this.rowHashCodes;
                    Object[] objArr = new Object[2];
                    objArr[0] = Integer.valueOf(z5 ? 4235 : -3142);
                    objArr[1] = Integer.valueOf(this.recentReactions.get(i7).hashCode());
                    arrayList5.add(Integer.valueOf(Arrays.hashCode(objArr)));
                }
                int size2 = this.totalCount + this.recentReactions.size();
                this.totalCount = size2;
                this.recentReactionsEndRow = size2;
            }
        } else if (this.type == 0) {
            ArrayList<TLRPC$EmojiStatus> recentEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getRecentEmojiStatuses();
            TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSet(new TLRPC$TL_inputStickerSetEmojiDefaultStatuses(), false);
            if (stickerSet == null) {
                this.defaultSetLoading = true;
            } else {
                if (this.includeEmpty) {
                    this.totalCount++;
                    this.rowHashCodes.add(2);
                }
                ArrayList<TLRPC$EmojiStatus> defaultEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getDefaultEmojiStatuses();
                ArrayList<TLRPC$Document> arrayList6 = stickerSet.documents;
                if (arrayList6 != null && !arrayList6.isEmpty()) {
                    for (int i8 = 0; i8 < Math.min(this.layoutManager.getSpanCount() - 1, stickerSet.documents.size()); i8++) {
                        this.recent.add(new AnimatedEmojiSpan(stickerSet.documents.get(i8), (Paint.FontMetricsInt) null));
                    }
                }
                if (recentEmojiStatuses != null && !recentEmojiStatuses.isEmpty()) {
                    Iterator<TLRPC$EmojiStatus> it2 = recentEmojiStatuses.iterator();
                    while (it2.hasNext()) {
                        TLRPC$EmojiStatus next = it2.next();
                        if (next instanceof TLRPC$TL_emojiStatus) {
                            it = it2;
                            j2 = ((TLRPC$TL_emojiStatus) next).document_id;
                        } else {
                            it = it2;
                            if (next instanceof TLRPC$TL_emojiStatusUntil) {
                                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil = (TLRPC$TL_emojiStatusUntil) next;
                                if (tLRPC$TL_emojiStatusUntil.until > ((int) (System.currentTimeMillis() / 1000))) {
                                    j2 = tLRPC$TL_emojiStatusUntil.document_id;
                                }
                                it2 = it;
                            } else {
                                it2 = it;
                            }
                        }
                        int i9 = 0;
                        while (true) {
                            if (i9 >= this.recent.size()) {
                                z3 = false;
                                break;
                            } else if (this.recent.get(i9).getDocumentId() == j2) {
                                z3 = true;
                                break;
                            } else {
                                i9++;
                            }
                        }
                        if (!z3) {
                            this.recent.add(new AnimatedEmojiSpan(j2, (Paint.FontMetricsInt) null));
                        }
                        it2 = it;
                    }
                }
                if (defaultEmojiStatuses != null && !defaultEmojiStatuses.isEmpty()) {
                    Iterator<TLRPC$EmojiStatus> it3 = defaultEmojiStatuses.iterator();
                    while (it3.hasNext()) {
                        TLRPC$EmojiStatus next2 = it3.next();
                        if (next2 instanceof TLRPC$TL_emojiStatus) {
                            j = ((TLRPC$TL_emojiStatus) next2).document_id;
                        } else if (next2 instanceof TLRPC$TL_emojiStatusUntil) {
                            TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil2 = (TLRPC$TL_emojiStatusUntil) next2;
                            if (tLRPC$TL_emojiStatusUntil2.until > ((int) (System.currentTimeMillis() / 1000))) {
                                j = tLRPC$TL_emojiStatusUntil2.document_id;
                            }
                        }
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
        }
        for (int i14 = 0; i14 < arrayList.size(); i14++) {
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
        }
        int spanCount2 = this.layoutManager.getSpanCount() * 3;
        for (int i16 = 0; i16 < arrayList2.size(); i16++) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) arrayList2.get(i16);
            if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
                TLRPC$TL_stickerSetFullCovered tLRPC$TL_stickerSetFullCovered = (TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered;
                int i17 = 0;
                while (true) {
                    if (i17 >= this.packs.size()) {
                        z4 = false;
                        break;
                    } else if (this.packs.get(i17).set.id == tLRPC$TL_stickerSetFullCovered.set.id) {
                        z4 = true;
                        break;
                    } else {
                        i17++;
                    }
                }
                if (!z4) {
                    this.positionToSection.put(this.totalCount, this.packs.size());
                    this.sectionToPosition.put(this.packs.size(), this.totalCount);
                    this.totalCount++;
                    this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{9211, Long.valueOf(tLRPC$TL_stickerSetFullCovered.set.id)})));
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
                        for (int i18 = 0; i18 < spanCount2 - 1; i18++) {
                            this.rowHashCodes.add(Integer.valueOf(Arrays.hashCode(new Object[]{3212, Long.valueOf(emojiPack2.documents.get(i18).id)})));
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
                }
            }
        }
        post(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$updateRows$8();
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
        RecyclerListView recyclerListView = this.emojiGridView;
        if (recyclerListView.scrolledByUserOnce) {
            return;
        }
        recyclerListView.scrollToPosition(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRows$8() {
        this.emojiTabs.updateEmojiPacks(this.packs);
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [boolean, int] */
    public void expand(int i, View view) {
        int spanCount;
        ?? r5;
        int size;
        Integer num;
        int i2;
        int i3;
        int i4 = this.positionToExpand.get(i);
        boolean z = false;
        Integer num2 = null;
        if (i4 >= 0 && i4 < this.packs.size()) {
            spanCount = this.layoutManager.getSpanCount() * 3;
            EmojiView.EmojiPack emojiPack = this.packs.get(i4);
            if (emojiPack.expanded) {
                return;
            }
            if (i4 + 1 == this.packs.size()) {
                z = true;
            }
            i3 = this.sectionToPosition.get(i4);
            this.expandedEmojiSets.add(Long.valueOf(emojiPack.set.id));
            size = emojiPack.expanded ? emojiPack.documents.size() : Math.min(spanCount, emojiPack.documents.size());
            num = emojiPack.documents.size() > spanCount ? Integer.valueOf(i3 + 1 + size) : null;
            emojiPack.expanded = true;
            i2 = emojiPack.documents.size();
        } else if (i4 != -1) {
            return;
        } else {
            spanCount = this.layoutManager.getSpanCount() * 5;
            boolean z2 = this.recentExpanded;
            if (z2) {
                return;
            }
            int i5 = (this.includeHint ? 1 : 0) + (this.includeEmpty == true ? 1 : 0);
            size = z2 ? this.recent.size() : Math.min((spanCount - r5) - 2, this.recent.size());
            int size2 = this.recent.size();
            this.recentExpanded = true;
            num = null;
            i2 = size2;
            i3 = i5;
        }
        if (i2 > size) {
            num = Integer.valueOf(i3 + 1 + size);
            num2 = Integer.valueOf(i2 - size);
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
        post(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$expand$9(f, intValue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$expand$9(float f, int i) {
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
        int i = this.type;
        return (i == 0 || i == 2) ? 2 : 3;
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
            if (this.lastChildCount != getChildCount() && SelectAnimatedEmojiDialog.this.showAnimator != null && !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                SelectAnimatedEmojiDialog.this.updateEmojiDrawables();
                this.lastChildCount = getChildCount();
            }
            if (!this.selectorRect.isEmpty()) {
                this.selectorDrawable.setBounds(this.selectorRect);
                canvas.save();
                Consumer<Canvas> consumer = this.selectorTransformer;
                if (consumer != null) {
                    consumer.accept(canvas);
                }
                this.selectorDrawable.draw(canvas);
                canvas.restore();
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
                        canvas.save();
                        canvas.translate(imageViewEmoji.getX(), imageViewEmoji.getY());
                        imageViewEmoji.drawSelected(canvas);
                        canvas.restore();
                        if (imageViewEmoji.getBackground() != null) {
                            imageViewEmoji.getBackground().setBounds((int) imageViewEmoji.getX(), (int) imageViewEmoji.getY(), ((int) imageViewEmoji.getX()) + imageViewEmoji.getWidth(), ((int) imageViewEmoji.getY()) + imageViewEmoji.getHeight());
                            imageViewEmoji.getBackground().draw(canvas);
                        }
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
                if (i3 >= this.viewsGroupedByLines.size()) {
                    break;
                }
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
            if (SelectAnimatedEmojiDialog.this.emojiGridView != null) {
                for (int i6 = 0; i6 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i6++) {
                    View childAt2 = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i6);
                    if (childAt2 instanceof ImageViewEmoji) {
                        ImageViewEmoji imageViewEmoji3 = (ImageViewEmoji) childAt2;
                        if (imageViewEmoji3.premiumLockIconView != null) {
                            canvas.save();
                            canvas.translate((int) (imageViewEmoji3.getX() + imageViewEmoji3.premiumLockIconView.getX()), (int) (imageViewEmoji3.getY() + imageViewEmoji3.premiumLockIconView.getY()));
                            imageViewEmoji3.premiumLockIconView.draw(canvas);
                            canvas.restore();
                        }
                    } else if (childAt2 != null) {
                        canvas.save();
                        canvas.translate((int) childAt2.getX(), (int) childAt2.getY());
                        childAt2.draw(canvas);
                        canvas.restore();
                    }
                }
            }
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
                float alpha;
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
                                if (childAdapterPosition < 0 || childAdapterPosition >= i2) {
                                    alpha = 1.0f;
                                } else {
                                    float clamp = MathUtils.clamp(((float) (SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime)) / ((float) SelectAnimatedEmojiDialog.this.animateExpandAppearDuration()), 0.0f, 1.0f);
                                    float f6 = childAdapterPosition;
                                    float f7 = i2;
                                    float f8 = f7 / 4.0f;
                                    float cascade = AndroidUtilities.cascade(clamp, f6, f7, f8);
                                    f *= (this.appearScaleInterpolator.getInterpolation(AndroidUtilities.cascade(clamp, f6, f7, f8)) * 0.5f) + 0.5f;
                                    alpha = cascade * 1.0f;
                                }
                            } else {
                                alpha = imageViewEmoji.getAlpha();
                            }
                            if (!imageViewEmoji.isDefaultReaction) {
                                if (imageViewEmoji.span != null) {
                                    AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.get(imageViewEmoji.span.getDocumentId());
                                    if (animatedEmojiDrawable == null && Math.min(imageViewEmoji.getScaleX(), imageViewEmoji.getScaleY()) * f > 0.0f && imageViewEmoji.getAlpha() * alpha > 0.0f) {
                                        animatedEmojiDrawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji.span.getDocumentId());
                                        animatedEmojiDrawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                                        SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.put(imageViewEmoji.span.getDocumentId(), animatedEmojiDrawable);
                                    }
                                    if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null) {
                                        imageReceiver = animatedEmojiDrawable.getImageReceiver();
                                        animatedEmojiDrawable.setAlpha((int) (alpha * 255.0f));
                                        imageViewEmoji.setDrawable(animatedEmojiDrawable);
                                        imageViewEmoji.drawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                                    }
                                }
                            } else {
                                imageReceiver = imageViewEmoji.imageReceiver;
                                imageReceiver.setAlpha(alpha);
                            }
                            if (imageReceiver != null) {
                                if (imageViewEmoji.selected) {
                                    imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
                                } else {
                                    imageReceiver.setRoundRadius(0);
                                }
                                ImageReceiver.BackgroundThreadDrawHolder drawInBackgroundThread = imageReceiver.setDrawInBackgroundThread(imageViewEmoji.backgroundThreadDrawHolder);
                                imageViewEmoji.backgroundThreadDrawHolder = drawInBackgroundThread;
                                drawInBackgroundThread.time = j;
                                imageViewEmoji.imageReceiverToDraw = imageReceiver;
                                imageViewEmoji.update(j);
                                imageViewEmoji.getWidth();
                                imageViewEmoji.getPaddingLeft();
                                imageViewEmoji.getPaddingRight();
                                imageViewEmoji.getHeight();
                                imageViewEmoji.getPaddingTop();
                                imageViewEmoji.getPaddingBottom();
                                Rect rect2 = AndroidUtilities.rectTmp2;
                                rect2.set(imageViewEmoji.getPaddingLeft(), imageViewEmoji.getPaddingTop(), imageViewEmoji.getWidth() - imageViewEmoji.getPaddingRight(), imageViewEmoji.getHeight() - imageViewEmoji.getPaddingBottom());
                                if (imageViewEmoji.selected) {
                                    rect2.set((int) (rect2.centerX() - ((rect2.width() / 2) * 0.86f)), (int) (rect2.centerY() - ((rect2.height() / 2) * 0.86f)), (int) (rect2.centerX() + ((rect2.width() / 2) * 0.86f)), (int) (rect2.centerY() + ((rect2.height() / 2) * 0.86f)));
                                }
                                rect2.offset((imageViewEmoji.getLeft() + ((int) imageViewEmoji.getTranslationX())) - this.startOffset, 0);
                                imageViewEmoji.backgroundThreadDrawHolder.setBounds(rect2);
                                imageViewEmoji.skewAlpha = 1.0f;
                                imageViewEmoji.skewIndex = i;
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
                            Drawable drawable = imageViewEmoji.drawable;
                            if (drawable instanceof AnimatedEmojiDrawable) {
                                ((AnimatedEmojiDrawable) drawable).draw(canvas, false);
                            } else {
                                drawable.draw(canvas);
                            }
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
                            if (imageViewEmoji.empty) {
                                drawable = SelectAnimatedEmojiDialog.this.getPremiumStar();
                                drawable.setBounds(rect);
                                drawable.setAlpha(255);
                            } else if (!imageViewEmoji.isDefaultReaction) {
                                if (imageViewEmoji.span != null && !imageViewEmoji.notDraw) {
                                    drawable = (Drawable) SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.get(imageViewEmoji.span.getDocumentId());
                                    if (drawable == null && Math.min(imageViewEmoji.getScaleX(), imageViewEmoji.getScaleY()) * scaleX > 0.0f && imageViewEmoji.getAlpha() * f2 > 0.0f) {
                                        drawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji.span.getDocumentId());
                                        imageViewEmoji.setDrawable(drawable);
                                        SelectAnimatedEmojiDialog.this.animatedEmojiDrawables.put(imageViewEmoji.span.getDocumentId(), drawable);
                                    }
                                    if (drawable != null) {
                                        drawable.setAlpha(255);
                                        drawable.setBounds(rect);
                                    }
                                }
                            } else {
                                ImageReceiver imageReceiver = imageViewEmoji.imageReceiver;
                                if (imageReceiver != null) {
                                    imageReceiver.setImageCoords(rect);
                                }
                            }
                            Drawable drawable2 = imageViewEmoji.drawable;
                            if (drawable2 instanceof AnimatedEmojiDrawable) {
                                drawable2.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                            }
                            float f6 = this.skewAlpha;
                            imageViewEmoji.skewAlpha = f6;
                            imageViewEmoji.skewIndex = i;
                            if (scaleX != 1.0f || f6 < 1.0f) {
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
                    if (drawable instanceof AnimatedEmojiDrawable) {
                        ((AnimatedEmojiDrawable) drawable).draw(canvas, false);
                    } else {
                        drawable.draw(canvas);
                    }
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
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setBigReactionAnimatedEmoji(null);
        this.isAttached = false;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredEmojiDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentEmojiStatusesUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
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
        } else if (i == NotificationCenter.recentEmojiStatusesUpdate) {
            updateRows(true);
        } else if (i != NotificationCenter.groupStickersDidLoad || !this.defaultSetLoading) {
        } else {
            updateRows(true);
            this.defaultSetLoading = false;
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
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                SelectAnimatedEmojiDialog.this.lambda$onShow$10(valueAnimator3);
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
    public /* synthetic */ void lambda$onShow$10(ValueAnimator valueAnimator) {
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
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                SelectAnimatedEmojiDialog.this.lambda$onDismiss$11(valueAnimator2);
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
    public /* synthetic */ void lambda$onDismiss$11(ValueAnimator valueAnimator) {
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
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.addParentView(this);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SelectStatusDurationDialog extends Dialog {
        private Bitmap blurBitmap;
        private Paint blurBitmapPaint;
        private boolean changeToScrimColor;
        private int clipBottom;
        private ContentView contentView;
        private BottomSheet dateBottomSheet;
        private View emojiPreviewView;
        private ImageReceiver imageReceiver;
        private ImageViewEmoji imageViewEmoji;
        private WindowInsets lastInsets;
        private LinearLayout linearLayoutView;
        private ActionBarPopupWindow.ActionBarPopupWindowLayout menuView;
        private Runnable parentDialogDismiss;
        private View parentDialogView;
        private int parentDialogX;
        private int parentDialogY;
        private Theme.ResourcesProvider resourcesProvider;
        private ValueAnimator showAnimator;
        private ValueAnimator showMenuAnimator;
        private float showMenuT;
        private float showT;
        private boolean showing;
        private boolean showingMenu;
        private Rect from = new Rect();
        private Rect to = new Rect();
        private Rect current = new Rect();
        private int[] tempLocation = new int[2];
        private boolean dismissed = false;

        protected boolean getOutBounds(Rect rect) {
            throw null;
        }

        protected void onEnd(Integer num) {
            throw null;
        }

        protected void onEndPartly(Integer num) {
            throw null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class ContentView extends FrameLayout {
            public ContentView(Context context) {
                super(context);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                float f;
                if (SelectStatusDurationDialog.this.blurBitmap != null && SelectStatusDurationDialog.this.blurBitmapPaint != null) {
                    canvas.save();
                    canvas.scale(12.0f, 12.0f);
                    SelectStatusDurationDialog.this.blurBitmapPaint.setAlpha((int) (SelectStatusDurationDialog.this.showT * 255.0f));
                    canvas.drawBitmap(SelectStatusDurationDialog.this.blurBitmap, 0.0f, 0.0f, SelectStatusDurationDialog.this.blurBitmapPaint);
                    canvas.restore();
                }
                super.dispatchDraw(canvas);
                if (SelectStatusDurationDialog.this.imageViewEmoji != null) {
                    Drawable drawable = SelectStatusDurationDialog.this.imageViewEmoji.drawable;
                    if (drawable != null) {
                        if (!SelectStatusDurationDialog.this.changeToScrimColor) {
                            drawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                        } else {
                            drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(SelectAnimatedEmojiDialog.this.scrimColor, Theme.getColor("windowBackgroundWhiteBlueIcon", SelectStatusDurationDialog.this.resourcesProvider), SelectStatusDurationDialog.this.showT), PorterDuff.Mode.MULTIPLY));
                        }
                        drawable.setAlpha((int) ((1.0f - SelectStatusDurationDialog.this.showT) * 255.0f));
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(SelectStatusDurationDialog.this.current);
                        if (SelectStatusDurationDialog.this.imageViewEmoji.pressedProgress != 0.0f || SelectStatusDurationDialog.this.imageViewEmoji.selected) {
                            f = (((1.0f - (SelectStatusDurationDialog.this.imageViewEmoji.selected ? 0.7f : SelectStatusDurationDialog.this.imageViewEmoji.pressedProgress)) * 0.2f) + 0.8f) * 1.0f;
                        } else {
                            f = 1.0f;
                        }
                        Rect rect = AndroidUtilities.rectTmp2;
                        rect.set((int) (rectF.centerX() - ((rectF.width() / 2.0f) * f)), (int) (rectF.centerY() - ((rectF.height() / 2.0f) * f)), (int) (rectF.centerX() + ((rectF.width() / 2.0f) * f)), (int) (rectF.centerY() + ((rectF.height() / 2.0f) * f)));
                        float f2 = 1.0f - ((1.0f - SelectStatusDurationDialog.this.imageViewEmoji.skewAlpha) * (1.0f - SelectStatusDurationDialog.this.showT));
                        canvas.save();
                        if (f2 < 1.0f) {
                            canvas.translate(rect.left, rect.top);
                            canvas.scale(1.0f, f2, 0.0f, 0.0f);
                            canvas.skew((1.0f - ((SelectStatusDurationDialog.this.imageViewEmoji.skewIndex * 2.0f) / SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount())) * (1.0f - f2), 0.0f);
                            canvas.translate(-rect.left, -rect.top);
                        }
                        canvas.clipRect(0.0f, 0.0f, getWidth(), SelectStatusDurationDialog.this.clipBottom + (SelectStatusDurationDialog.this.showT * AndroidUtilities.dp(45.0f)));
                        drawable.setBounds(rect);
                        drawable.draw(canvas);
                        canvas.restore();
                        if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != 0) {
                            if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != 1) {
                                if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() - 2) {
                                    if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex == SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount() - 1) {
                                        rect.offset(AndroidUtilities.dp(f2 * (-8.0f)), 0);
                                    }
                                } else {
                                    rect.offset(-AndroidUtilities.dp(f2 * (-4.0f)), 0);
                                }
                            } else {
                                rect.offset(AndroidUtilities.dp(f2 * 4.0f), 0);
                            }
                        } else {
                            rect.offset(AndroidUtilities.dp(f2 * 8.0f), 0);
                        }
                        canvas.saveLayerAlpha(rect.left, rect.top, rect.right, rect.bottom, (int) ((1.0f - SelectStatusDurationDialog.this.showT) * 255.0f), 31);
                        canvas.clipRect(rect);
                        canvas.translate((int) (SelectAnimatedEmojiDialog.this.bottomGradientView.getX() + SelectAnimatedEmojiDialog.this.contentView.getX() + SelectStatusDurationDialog.this.parentDialogX), ((int) SelectAnimatedEmojiDialog.this.bottomGradientView.getY()) + SelectAnimatedEmojiDialog.this.contentView.getY() + SelectStatusDurationDialog.this.parentDialogY);
                        SelectAnimatedEmojiDialog.this.bottomGradientView.draw(canvas);
                        canvas.restore();
                    } else if (SelectStatusDurationDialog.this.imageViewEmoji.isDefaultReaction && SelectStatusDurationDialog.this.imageViewEmoji.imageReceiver != null) {
                        SelectStatusDurationDialog.this.imageViewEmoji.imageReceiver.setAlpha(1.0f - SelectStatusDurationDialog.this.showT);
                        SelectStatusDurationDialog.this.imageViewEmoji.imageReceiver.setImageCoords(SelectStatusDurationDialog.this.current);
                        SelectStatusDurationDialog.this.imageViewEmoji.imageReceiver.draw(canvas);
                    }
                }
                if (SelectStatusDurationDialog.this.imageReceiver != null) {
                    SelectStatusDurationDialog.this.imageReceiver.setAlpha(SelectStatusDurationDialog.this.showT);
                    SelectStatusDurationDialog.this.imageReceiver.setImageCoords(SelectStatusDurationDialog.this.current);
                    SelectStatusDurationDialog.this.imageReceiver.draw(canvas);
                }
            }

            @Override // android.view.View
            protected void onConfigurationChanged(Configuration configuration) {
                SelectStatusDurationDialog.this.lastInsets = null;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                if (SelectStatusDurationDialog.this.imageReceiver != null) {
                    SelectStatusDurationDialog.this.imageReceiver.onAttachedToWindow();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                if (SelectStatusDurationDialog.this.imageReceiver != null) {
                    SelectStatusDurationDialog.this.imageReceiver.onDetachedFromWindow();
                }
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                Activity parentActivity = SelectStatusDurationDialog.this.getParentActivity();
                if (parentActivity == null) {
                    return;
                }
                View decorView = parentActivity.getWindow().getDecorView();
                if (SelectStatusDurationDialog.this.blurBitmap != null && SelectStatusDurationDialog.this.blurBitmap.getWidth() == decorView.getMeasuredWidth() && SelectStatusDurationDialog.this.blurBitmap.getHeight() == decorView.getMeasuredHeight()) {
                    return;
                }
                SelectStatusDurationDialog.this.prepareBlurBitmap();
            }
        }

        public SelectStatusDurationDialog(final Context context, Runnable runnable, View view, ImageViewEmoji imageViewEmoji, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            ImageLocation forDocument;
            String str;
            this.imageViewEmoji = imageViewEmoji;
            this.resourcesProvider = resourcesProvider;
            this.parentDialogDismiss = runnable;
            this.parentDialogView = view;
            ContentView contentView = new ContentView(context);
            this.contentView = contentView;
            setContentView(contentView, new ViewGroup.LayoutParams(-1, -1));
            LinearLayout linearLayout = new LinearLayout(context);
            this.linearLayoutView = linearLayout;
            linearLayout.setOrientation(1);
            View view2 = new View(context, SelectAnimatedEmojiDialog.this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog.1
                @Override // android.view.View
                protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                    super.onLayout(z, i, i2, i3, i4);
                    getLocationOnScreen(SelectStatusDurationDialog.this.tempLocation);
                    SelectStatusDurationDialog.this.to.set(SelectStatusDurationDialog.this.tempLocation[0], SelectStatusDurationDialog.this.tempLocation[1], SelectStatusDurationDialog.this.tempLocation[0] + getWidth(), SelectStatusDurationDialog.this.tempLocation[1] + getHeight());
                    AndroidUtilities.lerp(SelectStatusDurationDialog.this.from, SelectStatusDurationDialog.this.to, SelectStatusDurationDialog.this.showT, SelectStatusDurationDialog.this.current);
                }
            };
            this.emojiPreviewView = view2;
            this.linearLayoutView.addView(view2, LayoutHelper.createLinear(160, 160, 17, 0, 0, 0, 16));
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, R.drawable.popup_fixed_alert2, resourcesProvider);
            this.menuView = actionBarPopupWindowLayout;
            this.linearLayoutView.addView(actionBarPopupWindowLayout, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 0));
            ActionBarMenuItem.addItem(true, false, this.menuView, 0, LocaleController.getString("SetEmojiStatusUntil1Hour", R.string.SetEmojiStatusUntil1Hour), false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$0(view3);
                }
            });
            ActionBarMenuItem.addItem(false, false, this.menuView, 0, LocaleController.getString("SetEmojiStatusUntil2Hours", R.string.SetEmojiStatusUntil2Hours), false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$1(view3);
                }
            });
            ActionBarMenuItem.addItem(false, false, this.menuView, 0, LocaleController.getString("SetEmojiStatusUntil8Hours", R.string.SetEmojiStatusUntil8Hours), false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$2(view3);
                }
            });
            ActionBarMenuItem.addItem(false, false, this.menuView, 0, LocaleController.getString("SetEmojiStatusUntil2Days", R.string.SetEmojiStatusUntil2Days), false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$3(view3);
                }
            });
            ActionBarMenuItem.addItem(false, true, this.menuView, 0, LocaleController.getString("SetEmojiStatusUntilOther", R.string.SetEmojiStatusUntilOther), false, resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$6(context, view3);
                }
            });
            this.contentView.addView(this.linearLayoutView, LayoutHelper.createFrame(-2, -2, 17));
            Window window = getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.DialogNoAnimation);
                window.setBackgroundDrawable(null);
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = -1;
                attributes.gravity = 51;
                attributes.dimAmount = 0.0f;
                int i = attributes.flags & (-3);
                attributes.flags = i;
                int i2 = i | 131072;
                attributes.flags = i2;
                int i3 = i2 | 512;
                attributes.flags = i3;
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 21) {
                    attributes.flags = i3 | (-2147417856);
                    this.contentView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnApplyWindowInsetsListener
                        public final WindowInsets onApplyWindowInsets(View view3, WindowInsets windowInsets) {
                            WindowInsets lambda$new$7;
                            lambda$new$7 = SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$7(view3, windowInsets);
                            return lambda$new$7;
                        }
                    });
                }
                attributes.flags |= 1024;
                this.contentView.setFitsSystemWindows(true);
                this.contentView.setSystemUiVisibility(1284);
                attributes.height = -1;
                if (i4 >= 28) {
                    attributes.layoutInDisplayCutoutMode = 1;
                }
                window.setAttributes(attributes);
            }
            if (imageViewEmoji != null) {
                imageViewEmoji.notDraw = true;
            }
            prepareBlurBitmap();
            ImageReceiver imageReceiver = new ImageReceiver();
            this.imageReceiver = imageReceiver;
            imageReceiver.setParentView(this.contentView);
            this.imageReceiver.setLayerNum(7);
            TLRPC$Document tLRPC$Document = imageViewEmoji.document;
            if (tLRPC$Document == null) {
                Drawable drawable = imageViewEmoji.drawable;
                if (drawable instanceof AnimatedEmojiDrawable) {
                    tLRPC$Document = ((AnimatedEmojiDrawable) drawable).getDocument();
                }
            }
            if (tLRPC$Document != null) {
                SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document.thumbs, "windowBackgroundWhiteGrayIcon", 0.2f);
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                if ("video/webm".equals(tLRPC$Document.mime_type)) {
                    ImageLocation forDocument2 = ImageLocation.getForDocument(tLRPC$Document);
                    String str2 = "160_160_" + ImageLoader.AUTOPLAY_FILTER;
                    if (svgThumb != null) {
                        svgThumb.overrideWidthAndHeight(512, 512);
                    }
                    forDocument = forDocument2;
                    str = str2;
                } else {
                    if (svgThumb != null && MessageObject.isAnimatedStickerDocument(tLRPC$Document, false)) {
                        svgThumb.overrideWidthAndHeight(512, 512);
                    }
                    forDocument = ImageLocation.getForDocument(tLRPC$Document);
                    str = "160_160";
                }
                this.imageReceiver.setImage(forDocument, str, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document), "160_160", null, null, svgThumb, tLRPC$Document.size, null, tLRPC$Document, 1);
                Drawable drawable2 = imageViewEmoji.drawable;
                if ((drawable2 instanceof AnimatedEmojiDrawable) && ((AnimatedEmojiDrawable) drawable2).canOverrideColor()) {
                    this.imageReceiver.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                }
            }
            imageViewEmoji.getLocationOnScreen(this.tempLocation);
            this.from.left = this.tempLocation[0] + imageViewEmoji.getPaddingLeft();
            this.from.top = this.tempLocation[1] + imageViewEmoji.getPaddingTop();
            this.from.right = (this.tempLocation[0] + imageViewEmoji.getWidth()) - imageViewEmoji.getPaddingRight();
            this.from.bottom = (this.tempLocation[1] + imageViewEmoji.getHeight()) - imageViewEmoji.getPaddingBottom();
            AndroidUtilities.lerp(this.from, this.to, this.showT, this.current);
            view.getLocationOnScreen(this.tempLocation);
            int[] iArr = this.tempLocation;
            this.parentDialogX = iArr[0];
            int i5 = iArr[1];
            this.parentDialogY = i5;
            this.clipBottom = i5 + view.getHeight();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            done(Integer.valueOf((int) ((System.currentTimeMillis() / 1000) + 3600)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            done(Integer.valueOf((int) ((System.currentTimeMillis() / 1000) + 7200)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(View view) {
            done(Integer.valueOf((int) ((System.currentTimeMillis() / 1000) + 28800)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            done(Integer.valueOf((int) ((System.currentTimeMillis() / 1000) + 172800)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(Context context, View view) {
            if (this.dateBottomSheet != null) {
                return;
            }
            final boolean[] zArr = new boolean[1];
            BottomSheet.Builder createStatusUntilDatePickerDialog = AlertsCreator.createStatusUntilDatePickerDialog(context, System.currentTimeMillis() / 1000, new AlertsCreator.StatusUntilDatePickerDelegate() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda11
                @Override // org.telegram.ui.Components.AlertsCreator.StatusUntilDatePickerDelegate
                public final void didSelectDate(int i) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$4(zArr, i);
                }
            });
            createStatusUntilDatePickerDialog.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$new$5(zArr, dialogInterface);
                }
            });
            this.dateBottomSheet = createStatusUntilDatePickerDialog.show();
            animateMenuShow(false, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(boolean[] zArr, int i) {
            zArr[0] = true;
            done(Integer.valueOf(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(boolean[] zArr, DialogInterface dialogInterface) {
            if (!zArr[0]) {
                animateMenuShow(true, null);
            }
            this.dateBottomSheet = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ WindowInsets lambda$new$7(View view, WindowInsets windowInsets) {
            view.requestLayout();
            return Build.VERSION.SDK_INT >= 30 ? WindowInsets.CONSUMED : windowInsets.consumeSystemWindowInsets();
        }

        private void done(final Integer num) {
            Runnable runnable;
            boolean z = num != null && getOutBounds(this.from);
            this.changeToScrimColor = z;
            if (z) {
                this.parentDialogView.getLocationOnScreen(this.tempLocation);
                Rect rect = this.from;
                int[] iArr = this.tempLocation;
                rect.offset(iArr[0], iArr[1]);
            } else {
                this.imageViewEmoji.getLocationOnScreen(this.tempLocation);
                this.from.left = this.tempLocation[0] + this.imageViewEmoji.getPaddingLeft();
                this.from.top = this.tempLocation[1] + this.imageViewEmoji.getPaddingTop();
                this.from.right = (this.tempLocation[0] + this.imageViewEmoji.getWidth()) - this.imageViewEmoji.getPaddingRight();
                this.from.bottom = (this.tempLocation[1] + this.imageViewEmoji.getHeight()) - this.imageViewEmoji.getPaddingBottom();
            }
            if (num != null && (runnable = this.parentDialogDismiss) != null) {
                runnable.run();
            }
            animateShow(false, new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$done$8(num);
                }
            }, new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$done$9(num);
                }
            }, !z);
            animateMenuShow(false, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$done$8(Integer num) {
            onEnd(num);
            super.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$done$9(Integer num) {
            if (num != null) {
                try {
                    SelectAnimatedEmojiDialog.this.performHapticFeedback(0, 1);
                } catch (Exception unused) {
                }
                onEndPartly(num);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Activity getParentActivity() {
            for (Context context = getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void prepareBlurBitmap() {
            Activity parentActivity = getParentActivity();
            if (parentActivity == null) {
                return;
            }
            View decorView = parentActivity.getWindow().getDecorView();
            int measuredWidth = (int) (decorView.getMeasuredWidth() / 12.0f);
            int measuredHeight = (int) (decorView.getMeasuredHeight() / 12.0f);
            Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.scale(0.083333336f, 0.083333336f);
            canvas.drawColor(Theme.getColor("windowBackgroundWhite"));
            decorView.draw(canvas);
            if (parentActivity instanceof LaunchActivity) {
                LaunchActivity launchActivity = (LaunchActivity) parentActivity;
                if (launchActivity.getActionBarLayout().getLastFragment().getVisibleDialog() != null) {
                    launchActivity.getActionBarLayout().getLastFragment().getVisibleDialog().getWindow().getDecorView().draw(canvas);
                }
            }
            View view = this.parentDialogView;
            if (view != null) {
                view.getLocationOnScreen(this.tempLocation);
                canvas.save();
                int[] iArr = this.tempLocation;
                canvas.translate(iArr[0], iArr[1]);
                this.parentDialogView.draw(canvas);
                canvas.restore();
            }
            Utilities.stackBlurBitmap(createBitmap, Math.max(10, Math.max(measuredWidth, measuredHeight) / 180));
            this.blurBitmapPaint = new Paint(1);
            this.blurBitmap = createBitmap;
        }

        private void animateShow(final boolean z, final Runnable runnable, final Runnable runnable2, final boolean z2) {
            if (this.imageViewEmoji == null) {
                if (runnable == null) {
                    return;
                }
                runnable.run();
                return;
            }
            ValueAnimator valueAnimator = this.showAnimator;
            if (valueAnimator != null) {
                if (this.showing == z) {
                    return;
                }
                valueAnimator.cancel();
            }
            this.showing = z;
            if (z) {
                this.imageViewEmoji.notDraw = true;
            }
            final boolean[] zArr = new boolean[1];
            float[] fArr = new float[2];
            fArr[0] = this.showT;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.showAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$animateShow$10(z, z2, runnable2, zArr, valueAnimator2);
                }
            });
            this.showAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Runnable runnable3;
                    SelectStatusDurationDialog.this.showT = z ? 1.0f : 0.0f;
                    AndroidUtilities.lerp(SelectStatusDurationDialog.this.from, SelectStatusDurationDialog.this.to, SelectStatusDurationDialog.this.showT, SelectStatusDurationDialog.this.current);
                    SelectStatusDurationDialog.this.contentView.invalidate();
                    if (!z) {
                        SelectStatusDurationDialog.this.menuView.setAlpha(SelectStatusDurationDialog.this.showT);
                    }
                    if (SelectStatusDurationDialog.this.showT < 0.5f && !z && (runnable3 = runnable2) != null) {
                        boolean[] zArr2 = zArr;
                        if (!zArr2[0]) {
                            zArr2[0] = true;
                            runnable3.run();
                        }
                    }
                    if (!z) {
                        if (z2) {
                            SelectStatusDurationDialog.this.imageViewEmoji.notDraw = false;
                            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
                        }
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 4);
                    }
                    SelectStatusDurationDialog.this.showAnimator = null;
                    SelectStatusDurationDialog.this.contentView.invalidate();
                    Runnable runnable4 = runnable;
                    if (runnable4 != null) {
                        runnable4.run();
                    }
                }
            });
            this.showAnimator.setDuration(420L);
            this.showAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.showAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateShow$10(boolean z, boolean z2, Runnable runnable, boolean[] zArr, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.showT = floatValue;
            AndroidUtilities.lerp(this.from, this.to, floatValue, this.current);
            this.contentView.invalidate();
            if (!z) {
                this.menuView.setAlpha(this.showT);
            }
            if (this.showT < 0.025f && !z) {
                if (z2) {
                    this.imageViewEmoji.notDraw = false;
                    SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
                }
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 4);
            }
            if (this.showT >= 0.5f || z || runnable == null || zArr[0]) {
                return;
            }
            zArr[0] = true;
            runnable.run();
        }

        private void animateMenuShow(final boolean z, final Runnable runnable) {
            ValueAnimator valueAnimator = this.showMenuAnimator;
            if (valueAnimator != null) {
                if (this.showingMenu == z) {
                    return;
                }
                valueAnimator.cancel();
            }
            this.showingMenu = z;
            float[] fArr = new float[2];
            fArr[0] = this.showMenuT;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.showMenuAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectStatusDurationDialog$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.SelectStatusDurationDialog.this.lambda$animateMenuShow$11(valueAnimator2);
                }
            });
            this.showMenuAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SelectStatusDurationDialog.this.showMenuT = z ? 1.0f : 0.0f;
                    SelectStatusDurationDialog.this.menuView.setBackScaleY(SelectStatusDurationDialog.this.showMenuT);
                    SelectStatusDurationDialog.this.menuView.setAlpha(CubicBezierInterpolator.EASE_OUT.getInterpolation(SelectStatusDurationDialog.this.showMenuT));
                    int itemsCount = SelectStatusDurationDialog.this.menuView.getItemsCount();
                    for (int i = 0; i < itemsCount; i++) {
                        float cascade = AndroidUtilities.cascade(SelectStatusDurationDialog.this.showMenuT, i, itemsCount, 4.0f);
                        SelectStatusDurationDialog.this.menuView.getItemAt(i).setTranslationY((1.0f - cascade) * AndroidUtilities.dp(-12.0f));
                        SelectStatusDurationDialog.this.menuView.getItemAt(i).setAlpha(cascade);
                    }
                    SelectStatusDurationDialog.this.showMenuAnimator = null;
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
            if (z) {
                this.showMenuAnimator.setDuration(360L);
                this.showMenuAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            } else {
                this.showMenuAnimator.setDuration(240L);
                this.showMenuAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            }
            this.showMenuAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateMenuShow$11(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.showMenuT = floatValue;
            this.menuView.setBackScaleY(floatValue);
            this.menuView.setAlpha(CubicBezierInterpolator.EASE_OUT.getInterpolation(this.showMenuT));
            int itemsCount = this.menuView.getItemsCount();
            for (int i = 0; i < itemsCount; i++) {
                float cascade = AndroidUtilities.cascade(this.showMenuT, i, itemsCount, 4.0f);
                this.menuView.getItemAt(i).setTranslationY((1.0f - cascade) * AndroidUtilities.dp(-12.0f));
                this.menuView.getItemAt(i).setAlpha(cascade);
            }
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            if (dispatchTouchEvent || motionEvent.getAction() != 0) {
                return dispatchTouchEvent;
            }
            dismiss();
            return false;
        }

        @Override // android.app.Dialog
        public void show() {
            super.show();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 4);
            animateShow(true, null, null, true);
            animateMenuShow(true, null);
        }

        @Override // android.app.Dialog, android.content.DialogInterface
        public void dismiss() {
            if (this.dismissed) {
                return;
            }
            done(null);
            this.dismissed = true;
        }
    }
}
