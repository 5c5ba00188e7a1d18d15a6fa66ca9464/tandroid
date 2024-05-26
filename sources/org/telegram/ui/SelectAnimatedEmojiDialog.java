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
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_availableEffect;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_emojiList;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmojiChannelDefaultStatuses;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmojiDefaultStatuses;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.tgnet.TLRPC$TL_stickerSetNoCovered;
import org.telegram.tgnet.TLRPC$messages_AvailableEffects;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.FixedHeightEmptyCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CloseProgressDrawable2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DrawingInBackgroundThreadDrawable;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.EmojiTabsStrip;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugView$$ExternalSyntheticLambda3;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumButtonView;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.PremiumLockIconView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.Reactions.HwEmojis;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SearchStateDrawable;
import org.telegram.ui.Components.StickerCategoriesListView;
import org.telegram.ui.SelectAnimatedEmojiDialog;
/* loaded from: classes4.dex */
public class SelectAnimatedEmojiDialog extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static String[] lastSearchKeyboardLanguage;
    private int accentColor;
    private Adapter adapter;
    private View animateExpandFromButton;
    private float animateExpandFromButtonTranslate;
    private int animateExpandFromPosition;
    private long animateExpandStartTime;
    private int animateExpandToPosition;
    private boolean animationsEnabled;
    private BackgroundDelegate backgroundDelegate;
    private View backgroundView;
    private BaseFragment baseFragment;
    AnimatedEmojiDrawable bigReactionAnimatedEmoji;
    ImageReceiver bigReactionImageReceiver;
    public onLongPressedListener bigReactionListener;
    private boolean bottomGradientShown;
    private View bottomGradientView;
    private View bubble1View;
    private View bubble2View;
    private EmojiTabsStrip[] cachedEmojiTabs;
    public boolean cancelPressed;
    private Runnable clearSearchRunnable;
    public FrameLayout contentView;
    private View contentViewForeground;
    private final int currentAccount;
    private ArrayList<AnimatedEmojiSpan> defaultStatuses;
    private int defaultTopicIconRow;
    private ValueAnimator dimAnimator;
    private Runnable dismiss;
    private boolean drawBackground;
    private Rect drawableToBounds;
    public EmojiListView emojiGridView;
    public FrameLayout emojiGridViewContainer;
    DefaultItemAnimator emojiItemAnimator;
    public FrameLayout emojiSearchEmptyView;
    private BackupImageView emojiSearchEmptyViewImageView;
    public EmojiListView emojiSearchGridView;
    private float emojiSelectAlpha;
    private ValueAnimator emojiSelectAnimator;
    private Rect emojiSelectRect;
    private ImageViewEmoji emojiSelectView;
    public EmojiTabsStrip emojiTabs;
    public View emojiTabsShadow;
    private Integer emojiX;
    private boolean enterAnimationInProgress;
    private ArrayList<Long> expandedEmojiSets;
    public boolean forUser;
    private Drawable forumIconDrawable;
    private ImageViewEmoji forumIconImage;
    private ArrayList<TLRPC$TL_messages_stickerSet> frozenEmojiPacks;
    private boolean gridSearch;
    private ValueAnimator gridSwitchAnimator;
    public FrameLayout gridViewContainer;
    private ValueAnimator hideAnimator;
    private Integer hintExpireDate;
    private boolean includeEmpty;
    public boolean includeHint;
    private ArrayList<Long> installedEmojiSets;
    private boolean isAttached;
    private String lastQuery;
    private GridLayoutManager layoutManager;
    private Integer listStateId;
    private int longtapHintRow;
    private AnimationNotificationsLocker notificationsLocker;
    private OvershootInterpolator overshootInterpolator;
    private ArrayList<EmojiView.EmojiPack> packs;
    Paint paint;
    public boolean paused;
    public boolean pausedExceptSelected;
    private int popularSectionRow;
    private SparseIntArray positionToButton;
    private SparseIntArray positionToExpand;
    private SparseIntArray positionToSection;
    private Drawable premiumStar;
    private ColorFilter premiumStarColorFilter;
    float pressedProgress;
    private ArrayList<AnimatedEmojiSpan> recent;
    private EmojiPackExpand recentExpandButton;
    private boolean recentExpanded;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> recentReactions;
    private int recentReactionsEndRow;
    private int recentReactionsSectionRow;
    private int recentReactionsStartRow;
    private List<ReactionsLayoutInBubble.VisibleReaction> recentReactionsToSet;
    private ArrayList<TLRPC$Document> recentStickers;
    private Theme.ResourcesProvider resourcesProvider;
    private ArrayList<Long> rowHashCodes;
    private float scaleX;
    private float scaleY;
    private float scrimAlpha;
    private int scrimColor;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable scrimDrawable;
    private View scrimDrawableParent;
    private RecyclerAnimationScrollHelper scrollHelper;
    private SearchAdapter searchAdapter;
    public SearchBox searchBox;
    private ValueAnimator searchEmptyViewAnimator;
    private boolean searchEmptyViewVisible;
    private GridLayoutManager searchLayoutManager;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> searchResult;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> searchResultStickers;
    private int searchRow;
    private Runnable searchRunnable;
    private ArrayList<TLRPC$Document> searchSets;
    public boolean searched;
    public boolean searching;
    private SparseIntArray sectionToPosition;
    private SelectStatusDurationDialog selectStatusDateDialog;
    HashSet<Long> selectedDocumentIds;
    ImageViewEmoji selectedReactionView;
    HashSet<ReactionsLayoutInBubble.VisibleReaction> selectedReactions;
    public Paint selectorAccentPaint;
    public Paint selectorPaint;
    private ValueAnimator showAnimator;
    private boolean showStickers;
    private boolean smoothScrolling;
    private ArrayList<String> standardEmojis;
    private ArrayList<TLRPC$TL_messages_stickerSet> stickerSets;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> stickers;
    private int stickersEndRow;
    private ArrayList<TLRPC$Document> stickersSearchResult;
    private int stickersSectionRow;
    private int stickersStartRow;
    private View topGradientView;
    private int topMarginDp;
    private ArrayList<ReactionsLayoutInBubble.VisibleReaction> topReactions;
    private int topReactionsEndRow;
    private int topReactionsStartRow;
    private int topicEmojiHeaderRow;
    private int totalCount;
    private int type;
    private final Runnable updateRows;
    private final Runnable updateRowsDelayed;
    public boolean useAccentForPlus;
    private static final List<String> emptyViewEmojis = Arrays.asList("😖", "😫", "\u1fae0", "😨", "❓");
    private static boolean[] preloaded = new boolean[4];
    private static boolean isFirstOpen = true;
    private static HashMap<Integer, Parcelable> listStates = new HashMap<>();

    /* loaded from: classes4.dex */
    public interface BackgroundDelegate {
        void drawRect(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2);
    }

    /* loaded from: classes4.dex */
    public interface onLongPressedListener {
        void onLongPressed(ImageViewEmoji imageViewEmoji);
    }

    /* loaded from: classes4.dex */
    public interface onRecentClearedListener {
    }

    protected float getScrimDrawableTranslationY() {
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: invalidateParent */
    public void lambda$new$3() {
    }

    protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
    }

    protected void onInputFocus() {
    }

    protected void onReactionClick(ImageViewEmoji imageViewEmoji, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
    }

    protected void onSettings() {
    }

    public boolean prevWindowKeyboardVisible() {
        return false;
    }

    public void setOnRecentClearedListener(onRecentClearedListener onrecentclearedlistener) {
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
    }

    public boolean isBottom() {
        int i = this.type;
        return i == 5 || i == 10 || i == 12;
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

    public void setSelectedReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        this.selectedReactions.clear();
        this.selectedReactions.add(visibleReaction);
        if (this.emojiGridView != null) {
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                if (this.emojiGridView.getChildAt(i) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) this.emojiGridView.getChildAt(i);
                    imageViewEmoji.setViewSelected(this.selectedReactions.contains(imageViewEmoji.reaction), true);
                }
            }
            this.emojiGridView.invalidate();
        }
        if (this.emojiSearchGridView != null) {
            for (int i2 = 0; i2 < this.emojiSearchGridView.getChildCount(); i2++) {
                if (this.emojiSearchGridView.getChildAt(i2) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) this.emojiSearchGridView.getChildAt(i2);
                    imageViewEmoji2.setViewSelected(this.selectedReactions.contains(imageViewEmoji2.reaction), true);
                }
            }
            this.emojiSearchGridView.invalidate();
        }
    }

    public void setSelectedReactions(ArrayList<String> arrayList) {
        this.selectedReactions.clear();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            this.selectedReactions.add(ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(it.next()));
        }
        if (this.emojiGridView != null) {
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                if (this.emojiGridView.getChildAt(i) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) this.emojiGridView.getChildAt(i);
                    imageViewEmoji.setViewSelected(this.selectedReactions.contains(imageViewEmoji.reaction), true);
                }
            }
            this.emojiGridView.invalidate();
        }
    }

    public void setForUser(boolean z) {
        this.forUser = z;
        updateRows(false, false);
    }

    public void invalidateSearchBox() {
        this.searchBox.invalidate();
    }

    /* loaded from: classes4.dex */
    public static class SelectAnimatedEmojiDialogWindow extends PopupWindow {
        private static final ViewTreeObserver.OnScrollChangedListener NOP = new ViewTreeObserver.OnScrollChangedListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SelectAnimatedEmojiDialogWindow$$ExternalSyntheticLambda0
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow.lambda$static$0();
            }
        };
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
            setInputMethodMode(0);
            setSoftInputMode(4);
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
                if (viewTreeObserver != viewTreeObserver2) {
                    if (viewTreeObserver2 != null && viewTreeObserver2.isAlive()) {
                        this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                    }
                    this.mViewTreeObserver = viewTreeObserver;
                    if (viewTreeObserver != null) {
                        viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
                    }
                }
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
                if ((i & 2) != 0) {
                    layoutParams.flags = i & (-3);
                    layoutParams.dimAmount = 0.0f;
                    windowManager.updateViewLayout(rootView, layoutParams);
                }
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
        this(baseFragment, context, z, num, i, true, resourcesProvider, 16);
    }

    public SelectAnimatedEmojiDialog(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, boolean z2, Theme.ResourcesProvider resourcesProvider, int i2) {
        this(baseFragment, context, z, num, i, z2, resourcesProvider, i2, Theme.getColor(Theme.key_windowBackgroundWhiteBlueIcon, resourcesProvider));
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x02ab, code lost:
        if (r38 == 5) goto L203;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0390 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x048f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x04f9  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x04fc  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0571  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x058d  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0599  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x06a3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x07b8  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0802  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x030b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SelectAnimatedEmojiDialog(BaseFragment baseFragment, Context context, boolean z, Integer num, final int i, boolean z2, final Theme.ResourcesProvider resourcesProvider, int i2, int i3) {
        super(context);
        boolean z3;
        boolean z4;
        final Integer num2;
        int i4;
        int i5;
        float f;
        int i6;
        boolean z5;
        int i7;
        float f2;
        EmojiListView emojiListView;
        int i8;
        Integer num3;
        final BaseFragment baseFragment2 = baseFragment;
        boolean z6 = z2;
        this.selectedReactions = new HashSet<>();
        this.selectedDocumentIds = new HashSet<>();
        this.selectorPaint = new Paint(1);
        this.selectorAccentPaint = new Paint(1);
        this.stickerSets = new ArrayList<>();
        this.currentAccount = UserConfig.selectedAccount;
        this.cachedEmojiTabs = new EmojiTabsStrip[2];
        this.rowHashCodes = new ArrayList<>();
        this.positionToSection = new SparseIntArray();
        this.sectionToPosition = new SparseIntArray();
        this.positionToExpand = new SparseIntArray();
        this.positionToButton = new SparseIntArray();
        this.expandedEmojiSets = new ArrayList<>();
        this.installedEmojiSets = new ArrayList<>();
        this.recentExpanded = false;
        this.recent = new ArrayList<>();
        this.recentStickers = new ArrayList<>();
        this.standardEmojis = new ArrayList<>();
        this.topReactions = new ArrayList<>();
        this.recentReactions = new ArrayList<>();
        this.stickers = new ArrayList<>();
        this.defaultStatuses = new ArrayList<>();
        this.frozenEmojiPacks = new ArrayList<>();
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
        this.searching = false;
        this.searched = false;
        this.gridSearch = false;
        this.searchEmptyViewVisible = false;
        this.paused = false;
        this.pausedExceptSelected = false;
        this.animateExpandFromPosition = -1;
        this.animateExpandToPosition = -1;
        this.animateExpandStartTime = -1L;
        this.updateRows = new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$new$32();
            }
        };
        this.updateRowsDelayed = new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$new$33();
            }
        };
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.paint = new Paint();
        this.resourcesProvider = resourcesProvider;
        this.type = i;
        this.includeEmpty = z;
        this.baseFragment = baseFragment2;
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        StringBuilder sb = new StringBuilder();
        sb.append("emoji");
        sb.append((i == 0 || i == 12 || i == 9 || i == 10) ? "status" : "reaction");
        sb.append("usehint");
        this.includeHint = globalMainSettings.getInt(sb.toString(), 0) < 3;
        this.accentColor = i3;
        this.selectorPaint.setColor(Theme.getColor(Theme.key_listSelector, resourcesProvider));
        this.selectorAccentPaint.setColor(ColorUtils.setAlphaComponent(i3, 30));
        this.premiumStarColorFilter = new PorterDuffColorFilter(i3, PorterDuff.Mode.SRC_IN);
        this.emojiX = num;
        Integer valueOf = num == null ? null : Integer.valueOf(MathUtils.clamp(num.intValue(), AndroidUtilities.dp(26.0f), AndroidUtilities.dp(292.0f)));
        if (valueOf == null || valueOf.intValue() <= AndroidUtilities.dp(170.0f)) {
            z3 = true;
            z4 = false;
        } else {
            z3 = true;
            z4 = true;
        }
        setFocusableInTouchMode(z3);
        if (i == 0 || i == 12 || i == 9 || i == 10 || i == 2 || i == 5 || i == 7) {
            this.topMarginDp = i2;
            setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda10
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
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider), PorterDuff.Mode.MULTIPLY));
            this.bubble1View.setBackground(mutate);
            addView(this.bubble1View, LayoutHelper.createFrame(10, 10.0f, (isBottom() ? 80 : 48) | 3, (valueOf.intValue() / AndroidUtilities.density) + (z4 ? -12 : 4), isBottom() ? 0 : this.topMarginDp, 0.0f, isBottom() ? this.topMarginDp : 0));
        }
        this.backgroundView = new View(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.1
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                if (!SelectAnimatedEmojiDialog.this.drawBackground) {
                    super.dispatchDraw(canvas);
                } else {
                    canvas.drawColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider));
                }
            }
        };
        boolean z7 = i == 3 || i == 4;
        final boolean z8 = z7;
        final Integer num4 = valueOf;
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.2
            private final boolean beforeLollipop;
            private final Path pathApi20 = new Path();
            private final Paint paintApi20 = new Paint(1);

            {
                this.beforeLollipop = Build.VERSION.SDK_INT < 21;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                Integer num5;
                if (!SelectAnimatedEmojiDialog.this.drawBackground) {
                    super.dispatchDraw(canvas);
                } else if (this.beforeLollipop) {
                    canvas.save();
                    if (z8) {
                        Theme.applyDefaultShadow(this.paintApi20);
                    }
                    this.paintApi20.setColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider));
                    this.paintApi20.setAlpha((int) (getAlpha() * 255.0f));
                    float width = (num4 == null ? getWidth() / 2.0f : num5.intValue()) + AndroidUtilities.dp(20.0f);
                    float width2 = (getWidth() - getPaddingLeft()) - getPaddingRight();
                    float height = (getHeight() - getPaddingBottom()) - getPaddingTop();
                    if (SelectAnimatedEmojiDialog.this.isBottom()) {
                        AndroidUtilities.rectTmp.set(getPaddingLeft() + (width - (SelectAnimatedEmojiDialog.this.scaleX * width)), getPaddingTop() + ((1.0f - SelectAnimatedEmojiDialog.this.scaleY) * height), getPaddingLeft() + width + ((width2 - width) * SelectAnimatedEmojiDialog.this.scaleX), getPaddingTop() + height);
                    } else {
                        AndroidUtilities.rectTmp.set(getPaddingLeft() + (width - (SelectAnimatedEmojiDialog.this.scaleX * width)), getPaddingTop(), getPaddingLeft() + width + ((width2 - width) * SelectAnimatedEmojiDialog.this.scaleX), getPaddingTop() + (height * SelectAnimatedEmojiDialog.this.scaleY));
                    }
                    this.pathApi20.rewind();
                    this.pathApi20.addRoundRect(AndroidUtilities.rectTmp, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), Path.Direction.CW);
                    canvas.drawPath(this.pathApi20, this.paintApi20);
                    canvas.clipPath(this.pathApi20);
                    super.dispatchDraw(canvas);
                    canvas.restore();
                } else {
                    super.dispatchDraw(canvas);
                }
            }
        };
        this.contentView = frameLayout;
        if (Build.VERSION.SDK_INT < 21 || i == 4) {
            num2 = num4;
        } else {
            num2 = num4;
            frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.3
                private final Rect rect = new Rect();

                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    Integer num5;
                    float width = (num2 == null ? view.getWidth() / 2.0f : num5.intValue()) + AndroidUtilities.dp(20.0f);
                    float width2 = (view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight();
                    float height = (view.getHeight() - view.getPaddingBottom()) - view.getPaddingTop();
                    if (SelectAnimatedEmojiDialog.this.isBottom()) {
                        this.rect.set((int) (view.getPaddingLeft() + (width - (SelectAnimatedEmojiDialog.this.scaleX * width))), (int) (view.getPaddingTop() + ((1.0f - SelectAnimatedEmojiDialog.this.scaleY) * height) + (AndroidUtilities.dp(SelectAnimatedEmojiDialog.this.topMarginDp) * (1.0f - SelectAnimatedEmojiDialog.this.scaleY))), (int) (view.getPaddingLeft() + width + ((width2 - width) * SelectAnimatedEmojiDialog.this.scaleX)), (int) (view.getPaddingTop() + height + (AndroidUtilities.dp(SelectAnimatedEmojiDialog.this.topMarginDp) * (1.0f - SelectAnimatedEmojiDialog.this.scaleY))));
                    } else {
                        this.rect.set((int) (view.getPaddingLeft() + (width - (SelectAnimatedEmojiDialog.this.scaleX * width))), view.getPaddingTop(), (int) (view.getPaddingLeft() + width + ((width2 - width) * SelectAnimatedEmojiDialog.this.scaleX)), (int) (view.getPaddingTop() + (height * SelectAnimatedEmojiDialog.this.scaleY)));
                    }
                    outline.setRoundRect(this.rect, AndroidUtilities.dp(12.0f));
                }
            });
            this.contentView.setClipToOutline(true);
            if (z7) {
                this.contentView.setElevation(2.0f);
                if (i != 0 || i == 12 || i == 9) {
                    i4 = 5;
                } else if (i == 10 || i == 2) {
                    i4 = 5;
                } else {
                    i4 = 5;
                }
                this.contentView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                this.contentView.addView(this.backgroundView, LayoutHelper.createFrame(-1, -1.0f));
                FrameLayout frameLayout2 = this.contentView;
                if (i != 0 || i == 12 || i == 9 || i == 2) {
                    i5 = 7;
                } else {
                    i5 = 7;
                    if (i != 7) {
                        f = 0.0f;
                        addView(frameLayout2, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, f, 0.0f, isBottom() ? this.topMarginDp + 6 : 0.0f));
                        if (num2 != null) {
                            this.bubble2View = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.4
                                @Override // android.view.View
                                protected void onMeasure(int i9, int i10) {
                                    super.onMeasure(i9, i10);
                                    setPivotX(getMeasuredWidth() / 2);
                                    setPivotY(getMeasuredHeight());
                                }
                            };
                            Drawable drawable = getResources().getDrawable(R.drawable.shadowed_bubble2_half);
                            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider), PorterDuff.Mode.MULTIPLY));
                            this.bubble2View.setBackground(drawable);
                            i6 = 3;
                            addView(this.bubble2View, LayoutHelper.createFrame(17, 9.0f, (isBottom() ? 80 : 48) | 3, (num2.intValue() / AndroidUtilities.density) + (z4 ? -25 : 10), isBottom() ? 0.0f : this.topMarginDp + i4, 0.0f, isBottom() ? this.topMarginDp + i4 + 9 : 0.0f));
                        } else {
                            i6 = 3;
                        }
                        if (baseFragment2 == null && i != i6 && i != 6 && i != i4 && i != i5 && i != 4) {
                            if (i != 9 && i != 10 && z6) {
                                z5 = true;
                                i7 = 0;
                                while (i7 < 2) {
                                    Runnable runnable = z5 ? new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda23
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SelectAnimatedEmojiDialog.this.lambda$new$1(baseFragment2);
                                        }
                                    } : null;
                                    int i9 = i7;
                                    Integer num5 = num2;
                                    boolean z9 = z6;
                                    EmojiTabsStrip emojiTabsStrip = new EmojiTabsStrip(context, resourcesProvider, true, false, true, i, runnable, i3) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.5
                                        @Override // org.telegram.ui.Components.EmojiTabsStrip
                                        protected ColorFilter getEmojiColorFilter() {
                                            return SelectAnimatedEmojiDialog.this.premiumStarColorFilter;
                                        }

                                        @Override // org.telegram.ui.Components.EmojiTabsStrip
                                        protected boolean onTabClick(int i10) {
                                            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog;
                                            int i11 = 0;
                                            if (SelectAnimatedEmojiDialog.this.smoothScrolling) {
                                                return false;
                                            }
                                            if (i == 4) {
                                                if (i10 == 0) {
                                                    SelectAnimatedEmojiDialog.this.showStickers = !selectAnimatedEmojiDialog.showStickers;
                                                    SelectAnimatedEmojiDialog.this.emojiTabs.setVisibility(8);
                                                    SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                                                    selectAnimatedEmojiDialog2.emojiTabs = selectAnimatedEmojiDialog2.cachedEmojiTabs[SelectAnimatedEmojiDialog.this.showStickers ? 1 : 0];
                                                    SelectAnimatedEmojiDialog.this.emojiTabs.setVisibility(0);
                                                    SelectAnimatedEmojiDialog.this.emojiTabs.toggleEmojiStickersTab.setDrawable(ContextCompat.getDrawable(getContext(), SelectAnimatedEmojiDialog.this.showStickers ? R.drawable.msg_emoji_stickers : R.drawable.msg_emoji_smiles));
                                                    SelectAnimatedEmojiDialog.this.updateRows(true, false, false);
                                                    SelectAnimatedEmojiDialog.this.layoutManager.scrollToPositionWithOffset(0, 0);
                                                    return true;
                                                }
                                                i10--;
                                            }
                                            if (i10 > 0) {
                                                int i12 = i10 - 1;
                                                if (SelectAnimatedEmojiDialog.this.sectionToPosition.indexOfKey(i12) >= 0) {
                                                    i11 = SelectAnimatedEmojiDialog.this.sectionToPosition.get(i12);
                                                }
                                            }
                                            SelectAnimatedEmojiDialog.this.scrollToPosition(i11, AndroidUtilities.dp(-2.0f));
                                            SelectAnimatedEmojiDialog.this.emojiTabs.select(i10);
                                            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog3 = SelectAnimatedEmojiDialog.this;
                                            selectAnimatedEmojiDialog3.emojiGridView.scrolledByUserOnce = true;
                                            selectAnimatedEmojiDialog3.search(null);
                                            SearchBox searchBox = SelectAnimatedEmojiDialog.this.searchBox;
                                            if (searchBox != null && searchBox.categoriesListView != null) {
                                                SelectAnimatedEmojiDialog.this.searchBox.categoriesListView.selectCategory((StickerCategoriesListView.EmojiCategory) null);
                                            }
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
                                    emojiTabsStrip.recentTab.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda9
                                        @Override // android.view.View.OnLongClickListener
                                        public final boolean onLongClick(View view) {
                                            boolean lambda$new$2;
                                            lambda$new$2 = SelectAnimatedEmojiDialog.this.lambda$new$2(view);
                                            return lambda$new$2;
                                        }
                                    });
                                    emojiTabsStrip.updateButtonDrawables = false;
                                    if (i == 4) {
                                        i8 = 13;
                                        emojiTabsStrip.setAnimatedEmojiCacheType(13);
                                        num3 = num5;
                                    } else {
                                        i8 = 13;
                                        emojiTabsStrip.setAnimatedEmojiCacheType((i == 0 || i == 12 || i == 2) ? 6 : 5);
                                        num3 = num5;
                                    }
                                    emojiTabsStrip.animateAppear = num3 == null;
                                    emojiTabsStrip.setPaddingLeft(i == 6 ? 10.0f : 5.0f);
                                    if (i != 14 && i != 8 && i != i8) {
                                        this.contentView.addView(emojiTabsStrip, LayoutHelper.createFrame(-1, 36.0f));
                                    }
                                    this.cachedEmojiTabs[i9] = emojiTabsStrip;
                                    i7 = i9 + 1;
                                    num2 = num3;
                                    z6 = z9;
                                    baseFragment2 = baseFragment;
                                }
                                final Integer num6 = num2;
                                boolean z10 = z6;
                                EmojiTabsStrip[] emojiTabsStripArr = this.cachedEmojiTabs;
                                this.emojiTabs = emojiTabsStripArr[0];
                                emojiTabsStripArr[1].setVisibility(8);
                                View view = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.6
                                    @Override // android.view.View
                                    protected void onMeasure(int i10, int i11) {
                                        super.onMeasure(i10, i11);
                                        Integer num7 = num6;
                                        if (num7 != null) {
                                            setPivotX(num7.intValue());
                                        }
                                    }
                                };
                                this.emojiTabsShadow = view;
                                view.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
                                if (i != 14 || i == 8 || i == 13) {
                                    f2 = 1.0f;
                                } else {
                                    f2 = 1.0f;
                                    this.contentView.addView(this.emojiTabsShadow, LayoutHelper.createFrame(-1, 1.0f / AndroidUtilities.density, 48, 0.0f, 36.0f, 0.0f, 0.0f));
                                }
                                AndroidUtilities.updateViewVisibilityAnimated(this.emojiTabsShadow, true, f2, false);
                                this.emojiGridView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7
                                    @Override // androidx.recyclerview.widget.RecyclerView
                                    public void onScrolled(int i10, int i11) {
                                        int i12;
                                        super.onScrolled(i10, i11);
                                        SelectAnimatedEmojiDialog.this.checkScroll();
                                        if (!SelectAnimatedEmojiDialog.this.smoothScrolling) {
                                            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                            selectAnimatedEmojiDialog.updateTabsPosition(selectAnimatedEmojiDialog.layoutManager.findFirstCompletelyVisibleItemPosition());
                                        }
                                        SelectAnimatedEmojiDialog.this.updateSearchBox();
                                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                                        AndroidUtilities.updateViewVisibilityAnimated(selectAnimatedEmojiDialog2.emojiTabsShadow, selectAnimatedEmojiDialog2.emojiGridView.computeVerticalScrollOffset() != 0 || (i12 = i) == 0 || i12 == 12 || i12 == 10 || i12 == 1 || i12 == 11 || i12 == 6, 1.0f, true);
                                        SelectAnimatedEmojiDialog.this.lambda$new$3();
                                    }

                                    @Override // androidx.recyclerview.widget.RecyclerView
                                    public void onScrollStateChanged(int i10) {
                                        if (i10 == 0) {
                                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                            if (SelectAnimatedEmojiDialog.this.searchRow != -1 && SelectAnimatedEmojiDialog.this.searchBox.getVisibility() == 0 && SelectAnimatedEmojiDialog.this.searchBox.getTranslationY() > (-AndroidUtilities.dp(51.0f))) {
                                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                                selectAnimatedEmojiDialog.scrollToPosition(selectAnimatedEmojiDialog.searchBox.getTranslationY() > ((float) (-AndroidUtilities.dp(16.0f))) ? 0 : 1, 0);
                                            }
                                        }
                                        super.onScrollStateChanged(i10);
                                    }
                                };
                                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.8
                                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                                    protected float animateByScale(View view2) {
                                        return view2 instanceof EmojiPackExpand ? 0.6f : 0.0f;
                                    }
                                };
                                this.emojiItemAnimator = defaultItemAnimator;
                                defaultItemAnimator.setAddDuration(220L);
                                this.emojiItemAnimator.setMoveDuration(260L);
                                this.emojiItemAnimator.setChangeDuration(160L);
                                this.emojiItemAnimator.setSupportsChangeAnimations(false);
                                DefaultItemAnimator defaultItemAnimator2 = this.emojiItemAnimator;
                                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                                defaultItemAnimator2.setMoveInterpolator(cubicBezierInterpolator);
                                this.emojiItemAnimator.setDelayAnimations(false);
                                this.emojiGridView.setItemAnimator(this.emojiItemAnimator);
                                this.emojiGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(i != 6 ? 8.0f : 2.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                                Adapter adapter = new Adapter();
                                this.adapter = adapter;
                                this.emojiGridView.setAdapter(adapter);
                                EmojiListView emojiListView2 = this.emojiGridView;
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9
                                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                                    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i10) {
                                        try {
                                            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9.1
                                                @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                                public void onEnd() {
                                                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                                }
                                            };
                                            linearSmoothScrollerCustom.setTargetPosition(i10);
                                            startSmoothScroll(linearSmoothScrollerCustom);
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                };
                                this.layoutManager = gridLayoutManager;
                                emojiListView2.setLayoutManager(gridLayoutManager);
                                this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10
                                    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                                    public int getSpanSize(int i10) {
                                        if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i10) >= 0 || SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i10) >= 0 || i10 == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i10 == SelectAnimatedEmojiDialog.this.stickersSectionRow || i10 == SelectAnimatedEmojiDialog.this.popularSectionRow || i10 == SelectAnimatedEmojiDialog.this.longtapHintRow || i10 == SelectAnimatedEmojiDialog.this.searchRow || i10 == SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                                            return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                                        }
                                        return ((i10 < SelectAnimatedEmojiDialog.this.stickersStartRow || i10 >= SelectAnimatedEmojiDialog.this.stickersEndRow) && !SelectAnimatedEmojiDialog.this.showStickers) ? 5 : 8;
                                    }
                                });
                                this.gridViewContainer = new FrameLayout(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.11
                                    @Override // android.widget.FrameLayout, android.view.View
                                    protected void onMeasure(int i10, int i11) {
                                        super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i11) + AndroidUtilities.dp(36.0f), 1073741824));
                                    }
                                };
                                FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.12
                                    private final Rect rect = new Rect();

                                    @Override // android.view.ViewGroup
                                    protected boolean drawChild(Canvas canvas, View view2, long j) {
                                        if (view2 == SelectAnimatedEmojiDialog.this.emojiGridView && HwEmojis.isHwEnabled() && HwEmojis.isCascade()) {
                                            for (int i10 = 0; i10 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i10++) {
                                                View childAt = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i10);
                                                if (childAt instanceof ImageViewEmoji) {
                                                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                                                    if (imageViewEmoji.getAnimatedScale() == 1.0f) {
                                                        this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                                        canvas.save();
                                                        canvas.clipRect(this.rect);
                                                        super.drawChild(canvas, view2, j);
                                                        canvas.restore();
                                                    } else if (imageViewEmoji.getAnimatedScale() > 0.0f) {
                                                        this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                                        Rect rect = this.rect;
                                                        rect.set((int) (rect.centerX() - ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() - ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerX() + ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() + ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())));
                                                        canvas.save();
                                                        canvas.clipRect(this.rect);
                                                        canvas.scale(imageViewEmoji.getAnimatedScale(), imageViewEmoji.getAnimatedScale(), this.rect.centerX(), this.rect.centerY());
                                                        super.drawChild(canvas, view2, j);
                                                        canvas.restore();
                                                    }
                                                } else if ((childAt instanceof TextView) || (childAt instanceof EmojiPackExpand) || (childAt instanceof EmojiPackButton) || (childAt instanceof HeaderView)) {
                                                    this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                                    canvas.save();
                                                    canvas.clipRect(this.rect);
                                                    super.drawChild(canvas, view2, j);
                                                    canvas.restore();
                                                }
                                            }
                                            return false;
                                        }
                                        return super.drawChild(canvas, view2, j);
                                    }
                                };
                                this.emojiGridViewContainer = frameLayout3;
                                frameLayout3.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                                this.gridViewContainer.addView(this.emojiGridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                                emojiListView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.13
                                    @Override // androidx.recyclerview.widget.RecyclerView
                                    public void onScrolled(int i10, int i11) {
                                        super.onScrolled(i10, i11);
                                        SelectAnimatedEmojiDialog.this.checkScroll();
                                    }
                                };
                                this.emojiSearchGridView = emojiListView;
                                if (emojiListView.getItemAnimator() != null) {
                                    this.emojiSearchGridView.getItemAnimator().setDurations(180L);
                                    this.emojiSearchGridView.getItemAnimator().setMoveInterpolator(cubicBezierInterpolator);
                                }
                                TextView textView = new TextView(context);
                                if (i != 4) {
                                    textView.setText(LocaleController.getString("NoEmojiOrStickersFound", R.string.NoEmojiOrStickersFound));
                                } else if (i == 0 || i == 13 || i == 12 || i == 11 || i == 9 || i == 10) {
                                    textView.setText(LocaleController.getString("NoEmojiFound", R.string.NoEmojiFound));
                                } else if (i == 1 || i == 2) {
                                    textView.setText(LocaleController.getString("NoReactionsFound", R.string.NoReactionsFound));
                                } else if (i == 14) {
                                    textView.setText(LocaleController.getString("NoEffectsFound", R.string.NoEffectsFound));
                                } else {
                                    textView.setText(LocaleController.getString("NoIconsFound", R.string.NoIconsFound));
                                }
                                textView.setTextSize(1, 14.0f);
                                textView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText, resourcesProvider));
                                this.emojiSearchEmptyViewImageView = new BackupImageView(context);
                                FrameLayout frameLayout4 = new FrameLayout(context);
                                this.emojiSearchEmptyView = frameLayout4;
                                frameLayout4.addView(this.emojiSearchEmptyViewImageView, LayoutHelper.createFrame(36, 36.0f, 49, 0.0f, 16.0f, 0.0f, 0.0f));
                                this.emojiSearchEmptyView.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 60.0f, 0.0f, 0.0f));
                                this.emojiSearchEmptyView.setVisibility(8);
                                this.emojiSearchEmptyView.setAlpha(0.0f);
                                this.gridViewContainer.addView(this.emojiSearchEmptyView, LayoutHelper.createFrame(-1, -2.0f, 16, 0.0f, 0.0f, 0.0f, 0.0f));
                                this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                                EmojiListView emojiListView3 = this.emojiSearchGridView;
                                SearchAdapter searchAdapter = new SearchAdapter();
                                this.searchAdapter = searchAdapter;
                                emojiListView3.setAdapter(searchAdapter);
                                EmojiListView emojiListView4 = this.emojiSearchGridView;
                                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14
                                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                                    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i10) {
                                        try {
                                            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14.1
                                                @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                                public void onEnd() {
                                                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                                }
                                            };
                                            linearSmoothScrollerCustom.setTargetPosition(i10);
                                            startSmoothScroll(linearSmoothScrollerCustom);
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                };
                                this.searchLayoutManager = gridLayoutManager2;
                                emojiListView4.setLayoutManager(gridLayoutManager2);
                                this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.15
                                    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                                    public int getSpanSize(int i10) {
                                        int itemViewType = SelectAnimatedEmojiDialog.this.searchAdapter.getItemViewType(i10);
                                        if (itemViewType == 6) {
                                            return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                                        }
                                        return (itemViewType == 5 || SelectAnimatedEmojiDialog.this.searchAdapter.isSticker(i10)) ? 8 : 5;
                                    }
                                });
                                this.emojiSearchGridView.setVisibility(8);
                                this.gridViewContainer.addView(this.emojiSearchGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                                this.contentView.addView(this.gridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, (i != 8 || i == 13 || i == 14) ? 0.0f : 36.0f + (f2 / AndroidUtilities.density), 0.0f, 0.0f));
                                RecyclerAnimationScrollHelper recyclerAnimationScrollHelper = new RecyclerAnimationScrollHelper(this.emojiGridView, this.layoutManager);
                                this.scrollHelper = recyclerAnimationScrollHelper;
                                recyclerAnimationScrollHelper.setAnimationCallback(new RecyclerAnimationScrollHelper.AnimationCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.16
                                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                                    public void onPreAnimation() {
                                        SelectAnimatedEmojiDialog.this.smoothScrolling = true;
                                    }

                                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                                    public void onEndAnimation() {
                                        SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                    }
                                });
                                this.scrollHelper.setScrollListener(new RecyclerAnimationScrollHelper.ScrollListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda39
                                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
                                    public final void onScroll() {
                                        SelectAnimatedEmojiDialog.this.lambda$new$3();
                                    }
                                });
                                17 r8 = new 17(i, context, resourcesProvider, num);
                                this.emojiGridView.setOnItemLongClickListener(r8, ViewConfiguration.getLongPressTimeout() * 0.25f);
                                this.emojiSearchGridView.setOnItemLongClickListener(r8, ViewConfiguration.getLongPressTimeout() * 0.25f);
                                RecyclerListView.OnItemClickListener onItemClickListener = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda40
                                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                                    public final void onItemClick(View view2, int i10) {
                                        SelectAnimatedEmojiDialog.this.lambda$new$4(i, view2, i10);
                                    }
                                };
                                this.emojiGridView.setOnItemClickListener(onItemClickListener);
                                this.emojiSearchGridView.setOnItemClickListener(onItemClickListener);
                                SearchBox searchBox = new SearchBox(context, z10) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.18
                                    @Override // android.view.ViewGroup, android.view.View
                                    protected void dispatchDraw(Canvas canvas) {
                                        if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                                            SelectAnimatedEmojiDialog.this.backgroundDelegate.drawRect(canvas, 0, 0, getMeasuredWidth(), getMeasuredHeight(), SelectAnimatedEmojiDialog.this.searchBox.getX() + SelectAnimatedEmojiDialog.this.gridViewContainer.getX(), SelectAnimatedEmojiDialog.this.searchBox.getY() + SelectAnimatedEmojiDialog.this.gridViewContainer.getY());
                                        }
                                        super.dispatchDraw(canvas);
                                    }

                                    @Override // android.view.View
                                    public void setTranslationY(float f3) {
                                        if (f3 != getTranslationY()) {
                                            super.setTranslationY(f3);
                                            if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                                                invalidate();
                                            }
                                        }
                                    }
                                };
                                this.searchBox = searchBox;
                                searchBox.setTranslationY(-AndroidUtilities.dp(52.0f));
                                this.searchBox.setVisibility(4);
                                this.gridViewContainer.addView(this.searchBox, LayoutHelper.createFrame(-1, 52.0f, 48, 0.0f, -4.0f, 0.0f, 0.0f));
                                this.topGradientView = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.19
                                    @Override // android.view.View
                                    protected void onMeasure(int i10, int i11) {
                                        super.onMeasure(i10, i11);
                                        Integer num7 = num6;
                                        if (num7 != null) {
                                            setPivotX(num7.intValue());
                                        }
                                    }
                                };
                                Drawable drawable2 = getResources().getDrawable(R.drawable.gradient_top);
                                int i10 = Theme.key_actionBarDefaultSubmenuBackground;
                                drawable2.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor(i10, resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
                                this.topGradientView.setBackground(drawable2);
                                this.topGradientView.setAlpha(0.0f);
                                this.contentView.addView(this.topGradientView, LayoutHelper.createFrame(-1, 20.0f, 55, 0.0f, (f2 / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
                                this.bottomGradientView = new View(context);
                                Drawable drawable3 = getResources().getDrawable(R.drawable.gradient_bottom);
                                drawable3.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i10, resourcesProvider), PorterDuff.Mode.SRC_IN));
                                if (i == 14) {
                                    this.bottomGradientView.setBackground(drawable3);
                                }
                                this.bottomGradientView.setAlpha(0.0f);
                                this.contentView.addView(this.bottomGradientView, LayoutHelper.createFrame(-1, 20, 87));
                                View view2 = new View(context);
                                this.contentViewForeground = view2;
                                view2.setAlpha(0.0f);
                                this.contentViewForeground.setBackgroundColor(-16777216);
                                this.contentView.addView(this.contentViewForeground, LayoutHelper.createFrame(-1, -1.0f));
                                preload(i, this.currentAccount);
                                this.bigReactionImageReceiver.setLayerNum(7);
                                if (isAnimatedShow()) {
                                    HwEmojis.beforePreparing();
                                }
                                updateRows(true, false);
                            }
                        }
                        z5 = false;
                        i7 = 0;
                        while (i7 < 2) {
                        }
                        final Integer num62 = num2;
                        boolean z102 = z6;
                        EmojiTabsStrip[] emojiTabsStripArr2 = this.cachedEmojiTabs;
                        this.emojiTabs = emojiTabsStripArr2[0];
                        emojiTabsStripArr2[1].setVisibility(8);
                        View view3 = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.6
                            @Override // android.view.View
                            protected void onMeasure(int i102, int i11) {
                                super.onMeasure(i102, i11);
                                Integer num7 = num62;
                                if (num7 != null) {
                                    setPivotX(num7.intValue());
                                }
                            }
                        };
                        this.emojiTabsShadow = view3;
                        view3.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
                        if (i != 14) {
                        }
                        f2 = 1.0f;
                        AndroidUtilities.updateViewVisibilityAnimated(this.emojiTabsShadow, true, f2, false);
                        this.emojiGridView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7
                            @Override // androidx.recyclerview.widget.RecyclerView
                            public void onScrolled(int i102, int i11) {
                                int i12;
                                super.onScrolled(i102, i11);
                                SelectAnimatedEmojiDialog.this.checkScroll();
                                if (!SelectAnimatedEmojiDialog.this.smoothScrolling) {
                                    SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                    selectAnimatedEmojiDialog.updateTabsPosition(selectAnimatedEmojiDialog.layoutManager.findFirstCompletelyVisibleItemPosition());
                                }
                                SelectAnimatedEmojiDialog.this.updateSearchBox();
                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                                AndroidUtilities.updateViewVisibilityAnimated(selectAnimatedEmojiDialog2.emojiTabsShadow, selectAnimatedEmojiDialog2.emojiGridView.computeVerticalScrollOffset() != 0 || (i12 = i) == 0 || i12 == 12 || i12 == 10 || i12 == 1 || i12 == 11 || i12 == 6, 1.0f, true);
                                SelectAnimatedEmojiDialog.this.lambda$new$3();
                            }

                            @Override // androidx.recyclerview.widget.RecyclerView
                            public void onScrollStateChanged(int i102) {
                                if (i102 == 0) {
                                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                    if (SelectAnimatedEmojiDialog.this.searchRow != -1 && SelectAnimatedEmojiDialog.this.searchBox.getVisibility() == 0 && SelectAnimatedEmojiDialog.this.searchBox.getTranslationY() > (-AndroidUtilities.dp(51.0f))) {
                                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                        selectAnimatedEmojiDialog.scrollToPosition(selectAnimatedEmojiDialog.searchBox.getTranslationY() > ((float) (-AndroidUtilities.dp(16.0f))) ? 0 : 1, 0);
                                    }
                                }
                                super.onScrollStateChanged(i102);
                            }
                        };
                        DefaultItemAnimator defaultItemAnimator3 = new DefaultItemAnimator(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.8
                            @Override // androidx.recyclerview.widget.DefaultItemAnimator
                            protected float animateByScale(View view22) {
                                return view22 instanceof EmojiPackExpand ? 0.6f : 0.0f;
                            }
                        };
                        this.emojiItemAnimator = defaultItemAnimator3;
                        defaultItemAnimator3.setAddDuration(220L);
                        this.emojiItemAnimator.setMoveDuration(260L);
                        this.emojiItemAnimator.setChangeDuration(160L);
                        this.emojiItemAnimator.setSupportsChangeAnimations(false);
                        DefaultItemAnimator defaultItemAnimator22 = this.emojiItemAnimator;
                        CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_OUT_QUINT;
                        defaultItemAnimator22.setMoveInterpolator(cubicBezierInterpolator2);
                        this.emojiItemAnimator.setDelayAnimations(false);
                        this.emojiGridView.setItemAnimator(this.emojiItemAnimator);
                        this.emojiGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(i != 6 ? 8.0f : 2.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                        Adapter adapter2 = new Adapter();
                        this.adapter = adapter2;
                        this.emojiGridView.setAdapter(adapter2);
                        EmojiListView emojiListView22 = this.emojiGridView;
                        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i102) {
                                try {
                                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9.1
                                        @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                        public void onEnd() {
                                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                        }
                                    };
                                    linearSmoothScrollerCustom.setTargetPosition(i102);
                                    startSmoothScroll(linearSmoothScrollerCustom);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                        };
                        this.layoutManager = gridLayoutManager3;
                        emojiListView22.setLayoutManager(gridLayoutManager3);
                        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10
                            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                            public int getSpanSize(int i102) {
                                if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i102) >= 0 || SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i102) >= 0 || i102 == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i102 == SelectAnimatedEmojiDialog.this.stickersSectionRow || i102 == SelectAnimatedEmojiDialog.this.popularSectionRow || i102 == SelectAnimatedEmojiDialog.this.longtapHintRow || i102 == SelectAnimatedEmojiDialog.this.searchRow || i102 == SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                                    return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                                }
                                return ((i102 < SelectAnimatedEmojiDialog.this.stickersStartRow || i102 >= SelectAnimatedEmojiDialog.this.stickersEndRow) && !SelectAnimatedEmojiDialog.this.showStickers) ? 5 : 8;
                            }
                        });
                        this.gridViewContainer = new FrameLayout(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.11
                            @Override // android.widget.FrameLayout, android.view.View
                            protected void onMeasure(int i102, int i11) {
                                super.onMeasure(i102, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i11) + AndroidUtilities.dp(36.0f), 1073741824));
                            }
                        };
                        FrameLayout frameLayout32 = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.12
                            private final Rect rect = new Rect();

                            @Override // android.view.ViewGroup
                            protected boolean drawChild(Canvas canvas, View view22, long j) {
                                if (view22 == SelectAnimatedEmojiDialog.this.emojiGridView && HwEmojis.isHwEnabled() && HwEmojis.isCascade()) {
                                    for (int i102 = 0; i102 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i102++) {
                                        View childAt = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i102);
                                        if (childAt instanceof ImageViewEmoji) {
                                            ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                                            if (imageViewEmoji.getAnimatedScale() == 1.0f) {
                                                this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                                canvas.save();
                                                canvas.clipRect(this.rect);
                                                super.drawChild(canvas, view22, j);
                                                canvas.restore();
                                            } else if (imageViewEmoji.getAnimatedScale() > 0.0f) {
                                                this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                                Rect rect = this.rect;
                                                rect.set((int) (rect.centerX() - ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() - ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerX() + ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() + ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())));
                                                canvas.save();
                                                canvas.clipRect(this.rect);
                                                canvas.scale(imageViewEmoji.getAnimatedScale(), imageViewEmoji.getAnimatedScale(), this.rect.centerX(), this.rect.centerY());
                                                super.drawChild(canvas, view22, j);
                                                canvas.restore();
                                            }
                                        } else if ((childAt instanceof TextView) || (childAt instanceof EmojiPackExpand) || (childAt instanceof EmojiPackButton) || (childAt instanceof HeaderView)) {
                                            this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                            canvas.save();
                                            canvas.clipRect(this.rect);
                                            super.drawChild(canvas, view22, j);
                                            canvas.restore();
                                        }
                                    }
                                    return false;
                                }
                                return super.drawChild(canvas, view22, j);
                            }
                        };
                        this.emojiGridViewContainer = frameLayout32;
                        frameLayout32.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                        this.gridViewContainer.addView(this.emojiGridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                        emojiListView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.13
                            @Override // androidx.recyclerview.widget.RecyclerView
                            public void onScrolled(int i102, int i11) {
                                super.onScrolled(i102, i11);
                                SelectAnimatedEmojiDialog.this.checkScroll();
                            }
                        };
                        this.emojiSearchGridView = emojiListView;
                        if (emojiListView.getItemAnimator() != null) {
                        }
                        TextView textView2 = new TextView(context);
                        if (i != 4) {
                        }
                        textView2.setTextSize(1, 14.0f);
                        textView2.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText, resourcesProvider));
                        this.emojiSearchEmptyViewImageView = new BackupImageView(context);
                        FrameLayout frameLayout42 = new FrameLayout(context);
                        this.emojiSearchEmptyView = frameLayout42;
                        frameLayout42.addView(this.emojiSearchEmptyViewImageView, LayoutHelper.createFrame(36, 36.0f, 49, 0.0f, 16.0f, 0.0f, 0.0f));
                        this.emojiSearchEmptyView.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 60.0f, 0.0f, 0.0f));
                        this.emojiSearchEmptyView.setVisibility(8);
                        this.emojiSearchEmptyView.setAlpha(0.0f);
                        this.gridViewContainer.addView(this.emojiSearchEmptyView, LayoutHelper.createFrame(-1, -2.0f, 16, 0.0f, 0.0f, 0.0f, 0.0f));
                        this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                        EmojiListView emojiListView32 = this.emojiSearchGridView;
                        SearchAdapter searchAdapter2 = new SearchAdapter();
                        this.searchAdapter = searchAdapter2;
                        emojiListView32.setAdapter(searchAdapter2);
                        EmojiListView emojiListView42 = this.emojiSearchGridView;
                        GridLayoutManager gridLayoutManager22 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14
                            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i102) {
                                try {
                                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14.1
                                        @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                        public void onEnd() {
                                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                        }
                                    };
                                    linearSmoothScrollerCustom.setTargetPosition(i102);
                                    startSmoothScroll(linearSmoothScrollerCustom);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                        };
                        this.searchLayoutManager = gridLayoutManager22;
                        emojiListView42.setLayoutManager(gridLayoutManager22);
                        this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.15
                            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                            public int getSpanSize(int i102) {
                                int itemViewType = SelectAnimatedEmojiDialog.this.searchAdapter.getItemViewType(i102);
                                if (itemViewType == 6) {
                                    return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                                }
                                return (itemViewType == 5 || SelectAnimatedEmojiDialog.this.searchAdapter.isSticker(i102)) ? 8 : 5;
                            }
                        });
                        this.emojiSearchGridView.setVisibility(8);
                        this.gridViewContainer.addView(this.emojiSearchGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                        this.contentView.addView(this.gridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, (i != 8 || i == 13 || i == 14) ? 0.0f : 36.0f + (f2 / AndroidUtilities.density), 0.0f, 0.0f));
                        RecyclerAnimationScrollHelper recyclerAnimationScrollHelper2 = new RecyclerAnimationScrollHelper(this.emojiGridView, this.layoutManager);
                        this.scrollHelper = recyclerAnimationScrollHelper2;
                        recyclerAnimationScrollHelper2.setAnimationCallback(new RecyclerAnimationScrollHelper.AnimationCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.16
                            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                            public void onPreAnimation() {
                                SelectAnimatedEmojiDialog.this.smoothScrolling = true;
                            }

                            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                            public void onEndAnimation() {
                                SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                            }
                        });
                        this.scrollHelper.setScrollListener(new RecyclerAnimationScrollHelper.ScrollListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda39
                            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
                            public final void onScroll() {
                                SelectAnimatedEmojiDialog.this.lambda$new$3();
                            }
                        });
                        17 r82 = new 17(i, context, resourcesProvider, num);
                        this.emojiGridView.setOnItemLongClickListener(r82, ViewConfiguration.getLongPressTimeout() * 0.25f);
                        this.emojiSearchGridView.setOnItemLongClickListener(r82, ViewConfiguration.getLongPressTimeout() * 0.25f);
                        RecyclerListView.OnItemClickListener onItemClickListener2 = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda40
                            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                            public final void onItemClick(View view22, int i102) {
                                SelectAnimatedEmojiDialog.this.lambda$new$4(i, view22, i102);
                            }
                        };
                        this.emojiGridView.setOnItemClickListener(onItemClickListener2);
                        this.emojiSearchGridView.setOnItemClickListener(onItemClickListener2);
                        SearchBox searchBox2 = new SearchBox(context, z102) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.18
                            @Override // android.view.ViewGroup, android.view.View
                            protected void dispatchDraw(Canvas canvas) {
                                if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                                    SelectAnimatedEmojiDialog.this.backgroundDelegate.drawRect(canvas, 0, 0, getMeasuredWidth(), getMeasuredHeight(), SelectAnimatedEmojiDialog.this.searchBox.getX() + SelectAnimatedEmojiDialog.this.gridViewContainer.getX(), SelectAnimatedEmojiDialog.this.searchBox.getY() + SelectAnimatedEmojiDialog.this.gridViewContainer.getY());
                                }
                                super.dispatchDraw(canvas);
                            }

                            @Override // android.view.View
                            public void setTranslationY(float f3) {
                                if (f3 != getTranslationY()) {
                                    super.setTranslationY(f3);
                                    if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                                        invalidate();
                                    }
                                }
                            }
                        };
                        this.searchBox = searchBox2;
                        searchBox2.setTranslationY(-AndroidUtilities.dp(52.0f));
                        this.searchBox.setVisibility(4);
                        this.gridViewContainer.addView(this.searchBox, LayoutHelper.createFrame(-1, 52.0f, 48, 0.0f, -4.0f, 0.0f, 0.0f));
                        this.topGradientView = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.19
                            @Override // android.view.View
                            protected void onMeasure(int i102, int i11) {
                                super.onMeasure(i102, i11);
                                Integer num7 = num62;
                                if (num7 != null) {
                                    setPivotX(num7.intValue());
                                }
                            }
                        };
                        Drawable drawable22 = getResources().getDrawable(R.drawable.gradient_top);
                        int i102 = Theme.key_actionBarDefaultSubmenuBackground;
                        drawable22.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor(i102, resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
                        this.topGradientView.setBackground(drawable22);
                        this.topGradientView.setAlpha(0.0f);
                        this.contentView.addView(this.topGradientView, LayoutHelper.createFrame(-1, 20.0f, 55, 0.0f, (f2 / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
                        this.bottomGradientView = new View(context);
                        Drawable drawable32 = getResources().getDrawable(R.drawable.gradient_bottom);
                        drawable32.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i102, resourcesProvider), PorterDuff.Mode.SRC_IN));
                        if (i == 14) {
                        }
                        this.bottomGradientView.setAlpha(0.0f);
                        this.contentView.addView(this.bottomGradientView, LayoutHelper.createFrame(-1, 20, 87));
                        View view22 = new View(context);
                        this.contentViewForeground = view22;
                        view22.setAlpha(0.0f);
                        this.contentViewForeground.setBackgroundColor(-16777216);
                        this.contentView.addView(this.contentViewForeground, LayoutHelper.createFrame(-1, -1.0f));
                        preload(i, this.currentAccount);
                        this.bigReactionImageReceiver.setLayerNum(7);
                        if (isAnimatedShow()) {
                        }
                        updateRows(true, false);
                    }
                }
                f = this.topMarginDp + 6;
                addView(frameLayout2, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, f, 0.0f, isBottom() ? this.topMarginDp + 6 : 0.0f));
                if (num2 != null) {
                }
                if (baseFragment2 == null) {
                }
                z5 = false;
                i7 = 0;
                while (i7 < 2) {
                }
                final Integer num622 = num2;
                boolean z1022 = z6;
                EmojiTabsStrip[] emojiTabsStripArr22 = this.cachedEmojiTabs;
                this.emojiTabs = emojiTabsStripArr22[0];
                emojiTabsStripArr22[1].setVisibility(8);
                View view32 = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.6
                    @Override // android.view.View
                    protected void onMeasure(int i1022, int i11) {
                        super.onMeasure(i1022, i11);
                        Integer num7 = num622;
                        if (num7 != null) {
                            setPivotX(num7.intValue());
                        }
                    }
                };
                this.emojiTabsShadow = view32;
                view32.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
                if (i != 14) {
                }
                f2 = 1.0f;
                AndroidUtilities.updateViewVisibilityAnimated(this.emojiTabsShadow, true, f2, false);
                this.emojiGridView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7
                    @Override // androidx.recyclerview.widget.RecyclerView
                    public void onScrolled(int i1022, int i11) {
                        int i12;
                        super.onScrolled(i1022, i11);
                        SelectAnimatedEmojiDialog.this.checkScroll();
                        if (!SelectAnimatedEmojiDialog.this.smoothScrolling) {
                            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                            selectAnimatedEmojiDialog.updateTabsPosition(selectAnimatedEmojiDialog.layoutManager.findFirstCompletelyVisibleItemPosition());
                        }
                        SelectAnimatedEmojiDialog.this.updateSearchBox();
                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                        AndroidUtilities.updateViewVisibilityAnimated(selectAnimatedEmojiDialog2.emojiTabsShadow, selectAnimatedEmojiDialog2.emojiGridView.computeVerticalScrollOffset() != 0 || (i12 = i) == 0 || i12 == 12 || i12 == 10 || i12 == 1 || i12 == 11 || i12 == 6, 1.0f, true);
                        SelectAnimatedEmojiDialog.this.lambda$new$3();
                    }

                    @Override // androidx.recyclerview.widget.RecyclerView
                    public void onScrollStateChanged(int i1022) {
                        if (i1022 == 0) {
                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                            if (SelectAnimatedEmojiDialog.this.searchRow != -1 && SelectAnimatedEmojiDialog.this.searchBox.getVisibility() == 0 && SelectAnimatedEmojiDialog.this.searchBox.getTranslationY() > (-AndroidUtilities.dp(51.0f))) {
                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                selectAnimatedEmojiDialog.scrollToPosition(selectAnimatedEmojiDialog.searchBox.getTranslationY() > ((float) (-AndroidUtilities.dp(16.0f))) ? 0 : 1, 0);
                            }
                        }
                        super.onScrollStateChanged(i1022);
                    }
                };
                DefaultItemAnimator defaultItemAnimator32 = new DefaultItemAnimator(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.8
                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                    protected float animateByScale(View view222) {
                        return view222 instanceof EmojiPackExpand ? 0.6f : 0.0f;
                    }
                };
                this.emojiItemAnimator = defaultItemAnimator32;
                defaultItemAnimator32.setAddDuration(220L);
                this.emojiItemAnimator.setMoveDuration(260L);
                this.emojiItemAnimator.setChangeDuration(160L);
                this.emojiItemAnimator.setSupportsChangeAnimations(false);
                DefaultItemAnimator defaultItemAnimator222 = this.emojiItemAnimator;
                CubicBezierInterpolator cubicBezierInterpolator22 = CubicBezierInterpolator.EASE_OUT_QUINT;
                defaultItemAnimator222.setMoveInterpolator(cubicBezierInterpolator22);
                this.emojiItemAnimator.setDelayAnimations(false);
                this.emojiGridView.setItemAnimator(this.emojiItemAnimator);
                this.emojiGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(i != 6 ? 8.0f : 2.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                Adapter adapter22 = new Adapter();
                this.adapter = adapter22;
                this.emojiGridView.setAdapter(adapter22);
                EmojiListView emojiListView222 = this.emojiGridView;
                GridLayoutManager gridLayoutManager32 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i1022) {
                        try {
                            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9.1
                                @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                public void onEnd() {
                                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                }
                            };
                            linearSmoothScrollerCustom.setTargetPosition(i1022);
                            startSmoothScroll(linearSmoothScrollerCustom);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                };
                this.layoutManager = gridLayoutManager32;
                emojiListView222.setLayoutManager(gridLayoutManager32);
                this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10
                    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                    public int getSpanSize(int i1022) {
                        if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i1022) >= 0 || SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i1022) >= 0 || i1022 == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i1022 == SelectAnimatedEmojiDialog.this.stickersSectionRow || i1022 == SelectAnimatedEmojiDialog.this.popularSectionRow || i1022 == SelectAnimatedEmojiDialog.this.longtapHintRow || i1022 == SelectAnimatedEmojiDialog.this.searchRow || i1022 == SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                            return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                        }
                        return ((i1022 < SelectAnimatedEmojiDialog.this.stickersStartRow || i1022 >= SelectAnimatedEmojiDialog.this.stickersEndRow) && !SelectAnimatedEmojiDialog.this.showStickers) ? 5 : 8;
                    }
                });
                this.gridViewContainer = new FrameLayout(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.11
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i1022, int i11) {
                        super.onMeasure(i1022, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i11) + AndroidUtilities.dp(36.0f), 1073741824));
                    }
                };
                FrameLayout frameLayout322 = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.12
                    private final Rect rect = new Rect();

                    @Override // android.view.ViewGroup
                    protected boolean drawChild(Canvas canvas, View view222, long j) {
                        if (view222 == SelectAnimatedEmojiDialog.this.emojiGridView && HwEmojis.isHwEnabled() && HwEmojis.isCascade()) {
                            for (int i1022 = 0; i1022 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i1022++) {
                                View childAt = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i1022);
                                if (childAt instanceof ImageViewEmoji) {
                                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                                    if (imageViewEmoji.getAnimatedScale() == 1.0f) {
                                        this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                        canvas.save();
                                        canvas.clipRect(this.rect);
                                        super.drawChild(canvas, view222, j);
                                        canvas.restore();
                                    } else if (imageViewEmoji.getAnimatedScale() > 0.0f) {
                                        this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                        Rect rect = this.rect;
                                        rect.set((int) (rect.centerX() - ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() - ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerX() + ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() + ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())));
                                        canvas.save();
                                        canvas.clipRect(this.rect);
                                        canvas.scale(imageViewEmoji.getAnimatedScale(), imageViewEmoji.getAnimatedScale(), this.rect.centerX(), this.rect.centerY());
                                        super.drawChild(canvas, view222, j);
                                        canvas.restore();
                                    }
                                } else if ((childAt instanceof TextView) || (childAt instanceof EmojiPackExpand) || (childAt instanceof EmojiPackButton) || (childAt instanceof HeaderView)) {
                                    this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                    canvas.save();
                                    canvas.clipRect(this.rect);
                                    super.drawChild(canvas, view222, j);
                                    canvas.restore();
                                }
                            }
                            return false;
                        }
                        return super.drawChild(canvas, view222, j);
                    }
                };
                this.emojiGridViewContainer = frameLayout322;
                frameLayout322.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                this.gridViewContainer.addView(this.emojiGridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                emojiListView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.13
                    @Override // androidx.recyclerview.widget.RecyclerView
                    public void onScrolled(int i1022, int i11) {
                        super.onScrolled(i1022, i11);
                        SelectAnimatedEmojiDialog.this.checkScroll();
                    }
                };
                this.emojiSearchGridView = emojiListView;
                if (emojiListView.getItemAnimator() != null) {
                }
                TextView textView22 = new TextView(context);
                if (i != 4) {
                }
                textView22.setTextSize(1, 14.0f);
                textView22.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText, resourcesProvider));
                this.emojiSearchEmptyViewImageView = new BackupImageView(context);
                FrameLayout frameLayout422 = new FrameLayout(context);
                this.emojiSearchEmptyView = frameLayout422;
                frameLayout422.addView(this.emojiSearchEmptyViewImageView, LayoutHelper.createFrame(36, 36.0f, 49, 0.0f, 16.0f, 0.0f, 0.0f));
                this.emojiSearchEmptyView.addView(textView22, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 60.0f, 0.0f, 0.0f));
                this.emojiSearchEmptyView.setVisibility(8);
                this.emojiSearchEmptyView.setAlpha(0.0f);
                this.gridViewContainer.addView(this.emojiSearchEmptyView, LayoutHelper.createFrame(-1, -2.0f, 16, 0.0f, 0.0f, 0.0f, 0.0f));
                this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
                EmojiListView emojiListView322 = this.emojiSearchGridView;
                SearchAdapter searchAdapter22 = new SearchAdapter();
                this.searchAdapter = searchAdapter22;
                emojiListView322.setAdapter(searchAdapter22);
                EmojiListView emojiListView422 = this.emojiSearchGridView;
                GridLayoutManager gridLayoutManager222 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i1022) {
                        try {
                            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14.1
                                @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                                public void onEnd() {
                                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                                }
                            };
                            linearSmoothScrollerCustom.setTargetPosition(i1022);
                            startSmoothScroll(linearSmoothScrollerCustom);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                };
                this.searchLayoutManager = gridLayoutManager222;
                emojiListView422.setLayoutManager(gridLayoutManager222);
                this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.15
                    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                    public int getSpanSize(int i1022) {
                        int itemViewType = SelectAnimatedEmojiDialog.this.searchAdapter.getItemViewType(i1022);
                        if (itemViewType == 6) {
                            return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                        }
                        return (itemViewType == 5 || SelectAnimatedEmojiDialog.this.searchAdapter.isSticker(i1022)) ? 8 : 5;
                    }
                });
                this.emojiSearchGridView.setVisibility(8);
                this.gridViewContainer.addView(this.emojiSearchGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
                this.contentView.addView(this.gridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, (i != 8 || i == 13 || i == 14) ? 0.0f : 36.0f + (f2 / AndroidUtilities.density), 0.0f, 0.0f));
                RecyclerAnimationScrollHelper recyclerAnimationScrollHelper22 = new RecyclerAnimationScrollHelper(this.emojiGridView, this.layoutManager);
                this.scrollHelper = recyclerAnimationScrollHelper22;
                recyclerAnimationScrollHelper22.setAnimationCallback(new RecyclerAnimationScrollHelper.AnimationCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.16
                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                    public void onPreAnimation() {
                        SelectAnimatedEmojiDialog.this.smoothScrolling = true;
                    }

                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
                    public void onEndAnimation() {
                        SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                    }
                });
                this.scrollHelper.setScrollListener(new RecyclerAnimationScrollHelper.ScrollListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda39
                    @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
                    public final void onScroll() {
                        SelectAnimatedEmojiDialog.this.lambda$new$3();
                    }
                });
                17 r822 = new 17(i, context, resourcesProvider, num);
                this.emojiGridView.setOnItemLongClickListener(r822, ViewConfiguration.getLongPressTimeout() * 0.25f);
                this.emojiSearchGridView.setOnItemLongClickListener(r822, ViewConfiguration.getLongPressTimeout() * 0.25f);
                RecyclerListView.OnItemClickListener onItemClickListener22 = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda40
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view222, int i1022) {
                        SelectAnimatedEmojiDialog.this.lambda$new$4(i, view222, i1022);
                    }
                };
                this.emojiGridView.setOnItemClickListener(onItemClickListener22);
                this.emojiSearchGridView.setOnItemClickListener(onItemClickListener22);
                SearchBox searchBox22 = new SearchBox(context, z1022) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.18
                    @Override // android.view.ViewGroup, android.view.View
                    protected void dispatchDraw(Canvas canvas) {
                        if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                            SelectAnimatedEmojiDialog.this.backgroundDelegate.drawRect(canvas, 0, 0, getMeasuredWidth(), getMeasuredHeight(), SelectAnimatedEmojiDialog.this.searchBox.getX() + SelectAnimatedEmojiDialog.this.gridViewContainer.getX(), SelectAnimatedEmojiDialog.this.searchBox.getY() + SelectAnimatedEmojiDialog.this.gridViewContainer.getY());
                        }
                        super.dispatchDraw(canvas);
                    }

                    @Override // android.view.View
                    public void setTranslationY(float f3) {
                        if (f3 != getTranslationY()) {
                            super.setTranslationY(f3);
                            if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                                invalidate();
                            }
                        }
                    }
                };
                this.searchBox = searchBox22;
                searchBox22.setTranslationY(-AndroidUtilities.dp(52.0f));
                this.searchBox.setVisibility(4);
                this.gridViewContainer.addView(this.searchBox, LayoutHelper.createFrame(-1, 52.0f, 48, 0.0f, -4.0f, 0.0f, 0.0f));
                this.topGradientView = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.19
                    @Override // android.view.View
                    protected void onMeasure(int i1022, int i11) {
                        super.onMeasure(i1022, i11);
                        Integer num7 = num622;
                        if (num7 != null) {
                            setPivotX(num7.intValue());
                        }
                    }
                };
                Drawable drawable222 = getResources().getDrawable(R.drawable.gradient_top);
                int i1022 = Theme.key_actionBarDefaultSubmenuBackground;
                drawable222.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor(i1022, resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
                this.topGradientView.setBackground(drawable222);
                this.topGradientView.setAlpha(0.0f);
                this.contentView.addView(this.topGradientView, LayoutHelper.createFrame(-1, 20.0f, 55, 0.0f, (f2 / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
                this.bottomGradientView = new View(context);
                Drawable drawable322 = getResources().getDrawable(R.drawable.gradient_bottom);
                drawable322.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i1022, resourcesProvider), PorterDuff.Mode.SRC_IN));
                if (i == 14) {
                }
                this.bottomGradientView.setAlpha(0.0f);
                this.contentView.addView(this.bottomGradientView, LayoutHelper.createFrame(-1, 20, 87));
                View view222 = new View(context);
                this.contentViewForeground = view222;
                view222.setAlpha(0.0f);
                this.contentViewForeground.setBackgroundColor(-16777216);
                this.contentView.addView(this.contentViewForeground, LayoutHelper.createFrame(-1, -1.0f));
                preload(i, this.currentAccount);
                this.bigReactionImageReceiver.setLayerNum(7);
                if (isAnimatedShow()) {
                }
                updateRows(true, false);
            }
        }
        if (i != 0) {
        }
        i4 = 5;
        this.contentView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        this.contentView.addView(this.backgroundView, LayoutHelper.createFrame(-1, -1.0f));
        FrameLayout frameLayout22 = this.contentView;
        if (i != 0) {
        }
        i5 = 7;
        f = this.topMarginDp + 6;
        addView(frameLayout22, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, f, 0.0f, isBottom() ? this.topMarginDp + 6 : 0.0f));
        if (num2 != null) {
        }
        if (baseFragment2 == null) {
        }
        z5 = false;
        i7 = 0;
        while (i7 < 2) {
        }
        final Integer num6222 = num2;
        boolean z10222 = z6;
        EmojiTabsStrip[] emojiTabsStripArr222 = this.cachedEmojiTabs;
        this.emojiTabs = emojiTabsStripArr222[0];
        emojiTabsStripArr222[1].setVisibility(8);
        View view322 = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.6
            @Override // android.view.View
            protected void onMeasure(int i10222, int i11) {
                super.onMeasure(i10222, i11);
                Integer num7 = num6222;
                if (num7 != null) {
                    setPivotX(num7.intValue());
                }
            }
        };
        this.emojiTabsShadow = view322;
        view322.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
        if (i != 14) {
        }
        f2 = 1.0f;
        AndroidUtilities.updateViewVisibilityAnimated(this.emojiTabsShadow, true, f2, false);
        this.emojiGridView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.7
            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i10222, int i11) {
                int i12;
                super.onScrolled(i10222, i11);
                SelectAnimatedEmojiDialog.this.checkScroll();
                if (!SelectAnimatedEmojiDialog.this.smoothScrolling) {
                    SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                    selectAnimatedEmojiDialog.updateTabsPosition(selectAnimatedEmojiDialog.layoutManager.findFirstCompletelyVisibleItemPosition());
                }
                SelectAnimatedEmojiDialog.this.updateSearchBox();
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                AndroidUtilities.updateViewVisibilityAnimated(selectAnimatedEmojiDialog2.emojiTabsShadow, selectAnimatedEmojiDialog2.emojiGridView.computeVerticalScrollOffset() != 0 || (i12 = i) == 0 || i12 == 12 || i12 == 10 || i12 == 1 || i12 == 11 || i12 == 6, 1.0f, true);
                SelectAnimatedEmojiDialog.this.lambda$new$3();
            }

            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrollStateChanged(int i10222) {
                if (i10222 == 0) {
                    SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                    if (SelectAnimatedEmojiDialog.this.searchRow != -1 && SelectAnimatedEmojiDialog.this.searchBox.getVisibility() == 0 && SelectAnimatedEmojiDialog.this.searchBox.getTranslationY() > (-AndroidUtilities.dp(51.0f))) {
                        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                        selectAnimatedEmojiDialog.scrollToPosition(selectAnimatedEmojiDialog.searchBox.getTranslationY() > ((float) (-AndroidUtilities.dp(16.0f))) ? 0 : 1, 0);
                    }
                }
                super.onScrollStateChanged(i10222);
            }
        };
        DefaultItemAnimator defaultItemAnimator322 = new DefaultItemAnimator(this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.8
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            protected float animateByScale(View view2222) {
                return view2222 instanceof EmojiPackExpand ? 0.6f : 0.0f;
            }
        };
        this.emojiItemAnimator = defaultItemAnimator322;
        defaultItemAnimator322.setAddDuration(220L);
        this.emojiItemAnimator.setMoveDuration(260L);
        this.emojiItemAnimator.setChangeDuration(160L);
        this.emojiItemAnimator.setSupportsChangeAnimations(false);
        DefaultItemAnimator defaultItemAnimator2222 = this.emojiItemAnimator;
        CubicBezierInterpolator cubicBezierInterpolator222 = CubicBezierInterpolator.EASE_OUT_QUINT;
        defaultItemAnimator2222.setMoveInterpolator(cubicBezierInterpolator222);
        this.emojiItemAnimator.setDelayAnimations(false);
        this.emojiGridView.setItemAnimator(this.emojiItemAnimator);
        this.emojiGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(i != 6 ? 8.0f : 2.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
        Adapter adapter222 = new Adapter();
        this.adapter = adapter222;
        this.emojiGridView.setAdapter(adapter222);
        EmojiListView emojiListView2222 = this.emojiGridView;
        GridLayoutManager gridLayoutManager322 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i10222) {
                try {
                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.9.1
                        @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                        public void onEnd() {
                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                        }
                    };
                    linearSmoothScrollerCustom.setTargetPosition(i10222);
                    startSmoothScroll(linearSmoothScrollerCustom);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        };
        this.layoutManager = gridLayoutManager322;
        emojiListView2222.setLayoutManager(gridLayoutManager322);
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.10
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i10222) {
                if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i10222) >= 0 || SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i10222) >= 0 || i10222 == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i10222 == SelectAnimatedEmojiDialog.this.stickersSectionRow || i10222 == SelectAnimatedEmojiDialog.this.popularSectionRow || i10222 == SelectAnimatedEmojiDialog.this.longtapHintRow || i10222 == SelectAnimatedEmojiDialog.this.searchRow || i10222 == SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                    return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                }
                return ((i10222 < SelectAnimatedEmojiDialog.this.stickersStartRow || i10222 >= SelectAnimatedEmojiDialog.this.stickersEndRow) && !SelectAnimatedEmojiDialog.this.showStickers) ? 5 : 8;
            }
        });
        this.gridViewContainer = new FrameLayout(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.11
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i10222, int i11) {
                super.onMeasure(i10222, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i11) + AndroidUtilities.dp(36.0f), 1073741824));
            }
        };
        FrameLayout frameLayout3222 = new FrameLayout(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.12
            private final Rect rect = new Rect();

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view2222, long j) {
                if (view2222 == SelectAnimatedEmojiDialog.this.emojiGridView && HwEmojis.isHwEnabled() && HwEmojis.isCascade()) {
                    for (int i10222 = 0; i10222 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i10222++) {
                        View childAt = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i10222);
                        if (childAt instanceof ImageViewEmoji) {
                            ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                            if (imageViewEmoji.getAnimatedScale() == 1.0f) {
                                this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                canvas.save();
                                canvas.clipRect(this.rect);
                                super.drawChild(canvas, view2222, j);
                                canvas.restore();
                            } else if (imageViewEmoji.getAnimatedScale() > 0.0f) {
                                this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                                Rect rect = this.rect;
                                rect.set((int) (rect.centerX() - ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() - ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerX() + ((this.rect.width() / 2.0f) * imageViewEmoji.getAnimatedScale())), (int) (this.rect.centerY() + ((this.rect.height() / 2.0f) * imageViewEmoji.getAnimatedScale())));
                                canvas.save();
                                canvas.clipRect(this.rect);
                                canvas.scale(imageViewEmoji.getAnimatedScale(), imageViewEmoji.getAnimatedScale(), this.rect.centerX(), this.rect.centerY());
                                super.drawChild(canvas, view2222, j);
                                canvas.restore();
                            }
                        } else if ((childAt instanceof TextView) || (childAt instanceof EmojiPackExpand) || (childAt instanceof EmojiPackButton) || (childAt instanceof HeaderView)) {
                            this.rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                            canvas.save();
                            canvas.clipRect(this.rect);
                            super.drawChild(canvas, view2222, j);
                            canvas.restore();
                        }
                    }
                    return false;
                }
                return super.drawChild(canvas, view2222, j);
            }
        };
        this.emojiGridViewContainer = frameLayout3222;
        frameLayout3222.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
        this.gridViewContainer.addView(this.emojiGridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
        emojiListView = new EmojiListView(context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.13
            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i10222, int i11) {
                super.onScrolled(i10222, i11);
                SelectAnimatedEmojiDialog.this.checkScroll();
            }
        };
        this.emojiSearchGridView = emojiListView;
        if (emojiListView.getItemAnimator() != null) {
        }
        TextView textView222 = new TextView(context);
        if (i != 4) {
        }
        textView222.setTextSize(1, 14.0f);
        textView222.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText, resourcesProvider));
        this.emojiSearchEmptyViewImageView = new BackupImageView(context);
        FrameLayout frameLayout4222 = new FrameLayout(context);
        this.emojiSearchEmptyView = frameLayout4222;
        frameLayout4222.addView(this.emojiSearchEmptyViewImageView, LayoutHelper.createFrame(36, 36.0f, 49, 0.0f, 16.0f, 0.0f, 0.0f));
        this.emojiSearchEmptyView.addView(textView222, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 60.0f, 0.0f, 0.0f));
        this.emojiSearchEmptyView.setVisibility(8);
        this.emojiSearchEmptyView.setAlpha(0.0f);
        this.gridViewContainer.addView(this.emojiSearchEmptyView, LayoutHelper.createFrame(-1, -2.0f, 16, 0.0f, 0.0f, 0.0f, 0.0f));
        this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
        EmojiListView emojiListView3222 = this.emojiSearchGridView;
        SearchAdapter searchAdapter222 = new SearchAdapter();
        this.searchAdapter = searchAdapter222;
        emojiListView3222.setAdapter(searchAdapter222);
        EmojiListView emojiListView4222 = this.emojiSearchGridView;
        GridLayoutManager gridLayoutManager2222 = new GridLayoutManager(context, 40) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i10222) {
                try {
                    LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.14.1
                        @Override // androidx.recyclerview.widget.LinearSmoothScrollerCustom
                        public void onEnd() {
                            SelectAnimatedEmojiDialog.this.smoothScrolling = false;
                        }
                    };
                    linearSmoothScrollerCustom.setTargetPosition(i10222);
                    startSmoothScroll(linearSmoothScrollerCustom);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        };
        this.searchLayoutManager = gridLayoutManager2222;
        emojiListView4222.setLayoutManager(gridLayoutManager2222);
        this.searchLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.15
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i10222) {
                int itemViewType = SelectAnimatedEmojiDialog.this.searchAdapter.getItemViewType(i10222);
                if (itemViewType == 6) {
                    return SelectAnimatedEmojiDialog.this.layoutManager.getSpanCount();
                }
                return (itemViewType == 5 || SelectAnimatedEmojiDialog.this.searchAdapter.isSticker(i10222)) ? 8 : 5;
            }
        });
        this.emojiSearchGridView.setVisibility(8);
        this.gridViewContainer.addView(this.emojiSearchGridView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 0.0f));
        this.contentView.addView(this.gridViewContainer, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, (i != 8 || i == 13 || i == 14) ? 0.0f : 36.0f + (f2 / AndroidUtilities.density), 0.0f, 0.0f));
        RecyclerAnimationScrollHelper recyclerAnimationScrollHelper222 = new RecyclerAnimationScrollHelper(this.emojiGridView, this.layoutManager);
        this.scrollHelper = recyclerAnimationScrollHelper222;
        recyclerAnimationScrollHelper222.setAnimationCallback(new RecyclerAnimationScrollHelper.AnimationCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.16
            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
            public void onPreAnimation() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = true;
            }

            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.AnimationCallback
            public void onEndAnimation() {
                SelectAnimatedEmojiDialog.this.smoothScrolling = false;
            }
        });
        this.scrollHelper.setScrollListener(new RecyclerAnimationScrollHelper.ScrollListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda39
            @Override // org.telegram.ui.Components.RecyclerAnimationScrollHelper.ScrollListener
            public final void onScroll() {
                SelectAnimatedEmojiDialog.this.lambda$new$3();
            }
        });
        17 r8222 = new 17(i, context, resourcesProvider, num);
        this.emojiGridView.setOnItemLongClickListener(r8222, ViewConfiguration.getLongPressTimeout() * 0.25f);
        this.emojiSearchGridView.setOnItemLongClickListener(r8222, ViewConfiguration.getLongPressTimeout() * 0.25f);
        RecyclerListView.OnItemClickListener onItemClickListener222 = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda40
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2222, int i10222) {
                SelectAnimatedEmojiDialog.this.lambda$new$4(i, view2222, i10222);
            }
        };
        this.emojiGridView.setOnItemClickListener(onItemClickListener222);
        this.emojiSearchGridView.setOnItemClickListener(onItemClickListener222);
        SearchBox searchBox222 = new SearchBox(context, z10222) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.18
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                    SelectAnimatedEmojiDialog.this.backgroundDelegate.drawRect(canvas, 0, 0, getMeasuredWidth(), getMeasuredHeight(), SelectAnimatedEmojiDialog.this.searchBox.getX() + SelectAnimatedEmojiDialog.this.gridViewContainer.getX(), SelectAnimatedEmojiDialog.this.searchBox.getY() + SelectAnimatedEmojiDialog.this.gridViewContainer.getY());
                }
                super.dispatchDraw(canvas);
            }

            @Override // android.view.View
            public void setTranslationY(float f3) {
                if (f3 != getTranslationY()) {
                    super.setTranslationY(f3);
                    if (SelectAnimatedEmojiDialog.this.backgroundDelegate != null) {
                        invalidate();
                    }
                }
            }
        };
        this.searchBox = searchBox222;
        searchBox222.setTranslationY(-AndroidUtilities.dp(52.0f));
        this.searchBox.setVisibility(4);
        this.gridViewContainer.addView(this.searchBox, LayoutHelper.createFrame(-1, 52.0f, 48, 0.0f, -4.0f, 0.0f, 0.0f));
        this.topGradientView = new View(this, context) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.19
            @Override // android.view.View
            protected void onMeasure(int i10222, int i11) {
                super.onMeasure(i10222, i11);
                Integer num7 = num6222;
                if (num7 != null) {
                    setPivotX(num7.intValue());
                }
            }
        };
        Drawable drawable2222 = getResources().getDrawable(R.drawable.gradient_top);
        int i10222 = Theme.key_actionBarDefaultSubmenuBackground;
        drawable2222.setColorFilter(new PorterDuffColorFilter(AndroidUtilities.multiplyAlphaComponent(Theme.getColor(i10222, resourcesProvider), 0.8f), PorterDuff.Mode.SRC_IN));
        this.topGradientView.setBackground(drawable2222);
        this.topGradientView.setAlpha(0.0f);
        this.contentView.addView(this.topGradientView, LayoutHelper.createFrame(-1, 20.0f, 55, 0.0f, (f2 / AndroidUtilities.density) + 36.0f, 0.0f, 0.0f));
        this.bottomGradientView = new View(context);
        Drawable drawable3222 = getResources().getDrawable(R.drawable.gradient_bottom);
        drawable3222.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i10222, resourcesProvider), PorterDuff.Mode.SRC_IN));
        if (i == 14) {
        }
        this.bottomGradientView.setAlpha(0.0f);
        this.contentView.addView(this.bottomGradientView, LayoutHelper.createFrame(-1, 20, 87));
        View view2222 = new View(context);
        this.contentViewForeground = view2222;
        view2222.setAlpha(0.0f);
        this.contentViewForeground.setBackgroundColor(-16777216);
        this.contentView.addView(this.contentViewForeground, LayoutHelper.createFrame(-1, -1.0f));
        preload(i, this.currentAccount);
        this.bigReactionImageReceiver.setLayerNum(7);
        if (isAnimatedShow()) {
        }
        updateRows(true, false);
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
    public /* synthetic */ void lambda$new$1(BaseFragment baseFragment) {
        search(null, false, false);
        onSettings();
        baseFragment.presentFragment(new StickersActivity(5, this.frozenEmojiPacks));
        Runnable runnable = this.dismiss;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$2(View view) {
        onRecentLongClick();
        try {
            performHapticFeedback(0, 1);
        } catch (Exception unused) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 17 implements RecyclerListView.OnItemLongClickListenerExtended {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Integer val$emojiX;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ int val$type;

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public /* synthetic */ void onMove(float f, float f2) {
            RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onMove(this, f, f2);
        }

        17(int i, Context context, Theme.ResourcesProvider resourcesProvider, Integer num) {
            this.val$type = i;
            this.val$context = context;
            this.val$resourcesProvider = resourcesProvider;
            this.val$emojiX = num;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(final View view, int i, float f, float f2) {
            int i2 = this.val$type;
            if (i2 != 11 && i2 != 13) {
                boolean z = view instanceof ImageViewEmoji;
                if (z && (i2 == 1 || i2 == 8)) {
                    SelectAnimatedEmojiDialog.this.incrementHintUse();
                    SelectAnimatedEmojiDialog.this.performHapticFeedback(0);
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                    if (!imageViewEmoji.isDefaultReaction && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                        TLRPC$Document tLRPC$Document = imageViewEmoji.span.document;
                        if (tLRPC$Document == null) {
                            tLRPC$Document = AnimatedEmojiDrawable.findDocument(SelectAnimatedEmojiDialog.this.currentAccount, imageViewEmoji.span.documentId);
                        }
                        SelectAnimatedEmojiDialog.this.onEmojiSelected(imageViewEmoji, Long.valueOf(imageViewEmoji.span.documentId), tLRPC$Document, null);
                        return true;
                    }
                    SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                    selectAnimatedEmojiDialog.selectedReactionView = imageViewEmoji;
                    selectAnimatedEmojiDialog.pressedProgress = 0.0f;
                    selectAnimatedEmojiDialog.cancelPressed = false;
                    if (imageViewEmoji.isDefaultReaction) {
                        selectAnimatedEmojiDialog.setBigReactionAnimatedEmoji(null);
                        TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).getReactionsMap().get(SelectAnimatedEmojiDialog.this.selectedReactionView.reaction.emojicon);
                        if (tLRPC$TL_availableReaction != null) {
                            SelectAnimatedEmojiDialog.this.bigReactionImageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_pcache", null, null, null, 0L, "tgs", SelectAnimatedEmojiDialog.this.selectedReactionView.reaction, 0);
                        }
                    } else {
                        selectAnimatedEmojiDialog.setBigReactionAnimatedEmoji(new AnimatedEmojiDrawable(4, SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.selectedReactionView.span.documentId));
                    }
                    SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
                    SelectAnimatedEmojiDialog.this.lambda$new$3();
                    return true;
                } else if (z) {
                    ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) view;
                    if (imageViewEmoji2.span != null && (i2 == 0 || i2 == 12 || i2 == 9 || i2 == 10)) {
                        SelectAnimatedEmojiDialog.this.selectStatusDateDialog = new SelectStatusDurationDialog(this.val$context, SelectAnimatedEmojiDialog.this.dismiss, SelectAnimatedEmojiDialog.this, imageViewEmoji2, this.val$resourcesProvider) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.17.1
                            {
                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                            }

                            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog
                            protected boolean getOutBounds(Rect rect) {
                                if (SelectAnimatedEmojiDialog.this.scrimDrawable != null) {
                                    17 r0 = 17.this;
                                    if (r0.val$emojiX != null) {
                                        rect.set(SelectAnimatedEmojiDialog.this.drawableToBounds);
                                        return true;
                                    }
                                    return false;
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

                            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectStatusDurationDialog, android.app.Dialog, android.content.DialogInterface
                            public void dismiss() {
                                super.dismiss();
                                SelectAnimatedEmojiDialog.this.selectStatusDateDialog = null;
                            }
                        }.show();
                        try {
                            view.performHapticFeedback(0, 1);
                        } catch (Exception unused) {
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            if (selectAnimatedEmojiDialog.selectedReactionView != null) {
                selectAnimatedEmojiDialog.cancelPressed = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(selectAnimatedEmojiDialog.pressedProgress, 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$17$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SelectAnimatedEmojiDialog.17.this.lambda$onLongClickRelease$0(valueAnimator);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.17.2
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
    public /* synthetic */ void lambda$new$4(int i, View view, int i2) {
        TLRPC$Document tLRPC$Document;
        try {
            if (view instanceof ImageViewEmoji) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                if (imageViewEmoji.isDefaultReaction || i == 13 || i == 14) {
                    incrementHintUse();
                    onReactionClick(imageViewEmoji, imageViewEmoji.reaction);
                } else if (imageViewEmoji.isStaticIcon && (tLRPC$Document = imageViewEmoji.document) != null) {
                    onStickerClick(imageViewEmoji, tLRPC$Document);
                } else {
                    onEmojiClick(imageViewEmoji, imageViewEmoji.span);
                }
                if (i == 1 || i == 11) {
                    return;
                }
                performHapticFeedback(3, 1);
            } else if (view instanceof ImageView) {
                onEmojiClick(view, null);
                if (i == 1 || i == 11) {
                    return;
                }
                performHapticFeedback(3, 1);
            } else if (!(view instanceof EmojiPackExpand)) {
                if (view != null) {
                    view.callOnClick();
                }
            } else {
                expand(i2, (EmojiPackExpand) view);
                if (i == 1 || i == 11) {
                    return;
                }
                performHapticFeedback(3, 1);
            }
        } catch (Exception unused) {
        }
    }

    private void onStickerClick(ImageViewEmoji imageViewEmoji, TLRPC$Document tLRPC$Document) {
        if (this.type == 6) {
            onEmojiSelected(imageViewEmoji, Long.valueOf(tLRPC$Document.id), tLRPC$Document, null);
        } else {
            onEmojiSelected(imageViewEmoji, null, tLRPC$Document, null);
        }
    }

    public void setExpireDateHint(int i) {
        if (i <= 0) {
            return;
        }
        this.includeHint = true;
        this.hintExpireDate = Integer.valueOf(i);
        updateRows(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBigReactionAnimatedEmoji(AnimatedEmojiDrawable animatedEmojiDrawable) {
        AnimatedEmojiDrawable animatedEmojiDrawable2;
        if (this.isAttached && (animatedEmojiDrawable2 = this.bigReactionAnimatedEmoji) != animatedEmojiDrawable) {
            if (animatedEmojiDrawable2 != null) {
                animatedEmojiDrawable2.removeView(this);
            }
            this.bigReactionAnimatedEmoji = animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.setColorFilter(this.premiumStarColorFilter);
                this.bigReactionAnimatedEmoji.addView(this);
            }
        }
    }

    private void onRecentLongClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), null);
        builder.setTitle(LocaleController.getString("ClearRecentEmojiStatusesTitle", R.string.ClearRecentEmojiStatusesTitle));
        builder.setMessage(LocaleController.getString("ClearRecentEmojiStatusesText", R.string.ClearRecentEmojiStatusesText));
        builder.setPositiveButton(LocaleController.getString("Clear", R.string.Clear), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$5(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setDimEnabled(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SelectAnimatedEmojiDialog.this.lambda$onRecentLongClick$6(dialogInterface);
            }
        });
        builder.show();
        setDim(1.0f, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecentLongClick$5(DialogInterface dialogInterface, int i) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_account_clearRecentEmojiStatuses
            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i2, boolean z) {
                return TLRPC$Bool.TLdeserialize(abstractSerializedData, i2, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(404757166);
            }
        }, null);
        MediaDataController.getInstance(this.currentAccount).clearRecentEmojiStatuses();
        updateRows(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRecentLongClick$6(DialogInterface dialogInterface) {
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
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.this.lambda$setDim$7(valueAnimator2);
                }
            });
            this.dimAnimator.setDuration(200L);
            this.dimAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.dimAnimator.start();
            return;
        }
        this.contentViewForeground.setAlpha(f * 0.25f);
        int blendOver = Theme.blendOver(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, this.resourcesProvider), ColorUtils.setAlphaComponent(-16777216, (int) (f * 255.0f * 0.25f)));
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
    public /* synthetic */ void lambda$setDim$7(ValueAnimator valueAnimator) {
        View view = this.contentViewForeground;
        if (view != null) {
            view.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
        int blendOver = Theme.blendOver(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, this.resourcesProvider), ColorUtils.setAlphaComponent(-16777216, (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f)));
        View view2 = this.bubble1View;
        if (view2 != null) {
            view2.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
        }
        View view3 = this.bubble2View;
        if (view3 != null) {
            view3.getBackground().setColorFilter(new PorterDuffColorFilter(blendOver, PorterDuff.Mode.MULTIPLY));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTabsPosition(int i) {
        if (i != -1) {
            int i2 = 40;
            if (this.recent.size() <= 40 || this.recentExpanded) {
                i2 = (this.includeEmpty ? 1 : 0) + this.recent.size();
            }
            if (i <= i2 || i <= this.recentReactions.size()) {
                this.emojiTabs.select(0);
                return;
            }
            for (int i3 = 0; i3 < this.positionToSection.size(); i3++) {
                int keyAt = this.positionToSection.keyAt(i3);
                int i4 = i3 - (!this.defaultStatuses.isEmpty() ? 1 : 0);
                EmojiView.EmojiPack emojiPack = i4 >= 0 ? this.packs.get(i4) : null;
                if (emojiPack != null) {
                    int size = emojiPack.expanded ? emojiPack.documents.size() : Math.min(24, emojiPack.documents.size());
                    if (i > keyAt && i <= keyAt + 1 + size) {
                        this.emojiTabs.select(i3 + 1);
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSearchBox() {
        SearchBox searchBox = this.searchBox;
        if (searchBox == null) {
            return;
        }
        if (this.searched) {
            searchBox.clearAnimation();
            this.searchBox.setVisibility(0);
            this.searchBox.animate().translationY(0.0f).start();
        } else if (this.emojiGridView.getChildCount() > 0) {
            View childAt = this.emojiGridView.getChildAt(0);
            if (this.emojiGridView.getChildAdapterPosition(childAt) == this.searchRow && "searchbox".equals(childAt.getTag())) {
                this.searchBox.setVisibility(0);
                this.searchBox.setTranslationY(childAt.getY());
                return;
            }
            this.searchBox.setTranslationY(-AndroidUtilities.dp(52.0f));
        } else {
            this.searchBox.setTranslationY(-AndroidUtilities.dp(52.0f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getPremiumStar() {
        if (this.premiumStar == null) {
            int i = this.type;
            if (i == 5 || i == 9 || i == 10 || i == 7) {
                this.premiumStar = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_filled_blocked).mutate();
            } else {
                this.premiumStar = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_settings_premium).mutate();
            }
            this.premiumStar.setColorFilter(this.premiumStarColorFilter);
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
            int alpha = Build.VERSION.SDK_INT >= 19 ? this.scrimDrawable.getAlpha() : 255;
            View view2 = this.scrimDrawableParent;
            if (view2 == null) {
                bounds.height();
            } else {
                view2.getHeight();
            }
            canvas.save();
            canvas.translate(0.0f, -getTranslationY());
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.scrimDrawable;
            double d = alpha;
            double pow = Math.pow(this.contentView.getAlpha(), 0.25d);
            Double.isNaN(d);
            double d2 = d * pow;
            double d3 = this.scrimAlpha;
            Double.isNaN(d3);
            swapAnimatedEmojiDrawable2.setAlpha((int) (d2 * d3));
            if (this.drawableToBounds == null) {
                this.drawableToBounds = new Rect();
            }
            float f = (scaleY <= 1.0f || scaleY >= 1.5f) ? 0 : 2;
            float intValue = this.emojiX.intValue() + f;
            float centerY = (bounds.centerY() * (scaleY - 1.0f)) + (-(scaleY > 1.5f ? (bounds.height() * 0.81f) + 1.0f : 0.0f)) + (!isBottom() ? AndroidUtilities.dp(this.topMarginDp) : getMeasuredHeight() - (AndroidUtilities.dp(this.topMarginDp) / 2.0f)) + getScrimDrawableTranslationY();
            float width = (bounds.width() * scaleY) / 2.0f;
            float height = (bounds.height() * scaleY) / 2.0f;
            this.drawableToBounds.set((int) (intValue - width), (int) (centerY - height), (int) (intValue + width), (int) (centerY + height));
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable3 = this.scrimDrawable;
            Rect rect = this.drawableToBounds;
            int i = rect.left;
            Rect rect2 = this.drawableToBounds;
            swapAnimatedEmojiDrawable3.setBounds(i, rect.top, (int) (i + (rect.width() / scaleY)), (int) (rect2.top + (rect2.height() / scaleY)));
            Rect rect3 = this.drawableToBounds;
            canvas.scale(scaleY, scaleY, rect3.left, rect3.top);
            this.scrimDrawable.draw(canvas);
            this.scrimDrawable.setAlpha(alpha);
            this.scrimDrawable.setBounds(bounds);
            canvas.restore();
        }
        super.dispatchDraw(canvas);
        ImageViewEmoji imageViewEmoji = this.emojiSelectView;
        if (imageViewEmoji == null || this.emojiSelectRect == null || imageViewEmoji.drawable == null) {
            return;
        }
        canvas.save();
        canvas.translate(0.0f, -getTranslationY());
        this.emojiSelectView.drawable.setAlpha((int) (this.emojiSelectAlpha * 255.0f));
        this.emojiSelectView.drawable.setBounds(this.emojiSelectRect);
        this.emojiSelectView.drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(this.accentColor, this.scrimColor, 1.0f - this.scrimAlpha), PorterDuff.Mode.SRC_IN));
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
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SelectAnimatedEmojiDialog.this.lambda$animateEmojiSelect$8(rect, imageViewEmoji, zArr, runnable, make, valueAnimator);
            }
        });
        this.emojiSelectAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.20
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectAnimatedEmojiDialog.this.emojiSelectView = null;
                SelectAnimatedEmojiDialog.this.invalidate();
                boolean[] zArr2 = zArr;
                if (zArr2[0]) {
                    return;
                }
                zArr2[0] = true;
                runnable.run();
            }
        });
        this.emojiSelectAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.emojiSelectAnimator.setDuration(260L);
        this.emojiSelectAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEmojiSelect$8(Rect rect, ImageViewEmoji imageViewEmoji, boolean[] zArr, Runnable runnable, AnimatedEmojiDrawable animatedEmojiDrawable, ValueAnimator valueAnimator) {
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
        boolean canScrollVertically = (this.gridSearch ? this.emojiSearchGridView : this.emojiGridView).canScrollVertically(1);
        if (canScrollVertically != this.bottomGradientShown) {
            this.bottomGradientShown = canScrollVertically;
            this.bottomGradientView.animate().alpha(canScrollVertically ? 1.0f : 0.0f).setDuration(200L).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToPosition(int i, int i2) {
        View findViewByPosition = this.layoutManager.findViewByPosition(i);
        int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
        if ((findViewByPosition == null && Math.abs(i - findFirstVisibleItemPosition) > 72.0f) || !SharedConfig.animationsEnabled()) {
            this.scrollHelper.setScrollDirection(this.layoutManager.findFirstVisibleItemPosition() < i ? 0 : 1);
            this.scrollHelper.scrollToPosition(i, i2, false, true);
            return;
        }
        LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(this.emojiGridView.getContext(), 2) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.21
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

    public void switchGrids(final boolean z, boolean z2) {
        if (this.gridSearch == z) {
            return;
        }
        this.gridSearch = z;
        this.emojiGridView.setVisibility(0);
        this.emojiSearchGridView.setVisibility(0);
        ValueAnimator valueAnimator = this.gridSwitchAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.searchEmptyViewAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.searchEmptyViewAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.gridSwitchAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                SelectAnimatedEmojiDialog.this.lambda$switchGrids$9(z, valueAnimator3);
            }
        });
        this.gridSwitchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.22
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectAnimatedEmojiDialog.this.emojiSearchGridView.setVisibility(z ? 0 : 8);
                SelectAnimatedEmojiDialog.this.emojiGridView.setVisibility(z ? 8 : 0);
                SelectAnimatedEmojiDialog.this.gridSwitchAnimator = null;
                if (!z && SelectAnimatedEmojiDialog.this.searchResult != null) {
                    SelectAnimatedEmojiDialog.this.searchResult.clear();
                    if (SelectAnimatedEmojiDialog.this.searchSets != null) {
                        SelectAnimatedEmojiDialog.this.searchSets.clear();
                    }
                    SelectAnimatedEmojiDialog.this.searchAdapter.updateRows(false);
                }
                if (z || SelectAnimatedEmojiDialog.this.searchResultStickers == null) {
                    return;
                }
                SelectAnimatedEmojiDialog.this.searchResultStickers.clear();
            }
        });
        this.gridSwitchAnimator.setDuration(320L);
        this.gridSwitchAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.gridSwitchAnimator.start();
        ((View) this.emojiGridView.getParent()).animate().translationY((this.gridSearch && z2) ? -AndroidUtilities.dp(36.0f) : 0.0f).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                SelectAnimatedEmojiDialog.this.lambda$switchGrids$10(valueAnimator3);
            }
        }).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(160L).start();
        if (this.gridSearch && z2) {
            this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
        } else {
            this.emojiSearchGridView.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(54.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(38.0f));
        }
        checkScroll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchGrids$9(boolean z, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!z) {
            floatValue = 1.0f - floatValue;
        }
        float f = 1.0f - floatValue;
        this.emojiGridView.setAlpha(f);
        this.emojiGridView.setTranslationY(AndroidUtilities.dp(8.0f) * floatValue);
        this.emojiSearchGridView.setAlpha(floatValue);
        this.emojiSearchGridView.setTranslationY(AndroidUtilities.dp(8.0f) * f);
        this.emojiSearchEmptyView.setAlpha(this.emojiSearchGridView.getAlpha() * floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchGrids$10(ValueAnimator valueAnimator) {
        lambda$new$3();
    }

    public static void updateSearchEmptyViewImage(int i, BackupImageView backupImageView) {
        ImageLocation forDocument;
        if (backupImageView == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(MediaDataController.getInstance(i).getFeaturedEmojiSets());
        Collections.shuffle(arrayList);
        int round = (int) Math.round(Math.random() * 10.0d);
        TLRPC$Document tLRPC$Document = null;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if ((arrayList.get(i2) instanceof TLRPC$TL_stickerSetFullCovered) && ((TLRPC$TL_stickerSetFullCovered) arrayList.get(i2)).documents != null) {
                ArrayList arrayList2 = new ArrayList(((TLRPC$TL_stickerSetFullCovered) arrayList.get(i2)).documents);
                Collections.shuffle(arrayList2);
                int i3 = 0;
                while (true) {
                    if (i3 >= arrayList2.size()) {
                        break;
                    }
                    TLRPC$Document tLRPC$Document2 = (TLRPC$Document) arrayList2.get(i3);
                    if (tLRPC$Document2 != null && emptyViewEmojis.contains(MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document2, null))) {
                        int i4 = round - 1;
                        if (round <= 0) {
                            round = i4;
                            tLRPC$Document = tLRPC$Document2;
                            break;
                        }
                        round = i4;
                        tLRPC$Document = tLRPC$Document2;
                    }
                    i3++;
                }
            }
            if (tLRPC$Document != null && round <= 0) {
                break;
            }
        }
        if (tLRPC$Document == null || round > 0) {
            ArrayList arrayList3 = new ArrayList(MediaDataController.getInstance(i).getStickerSets(5));
            Collections.shuffle(arrayList3);
            for (int i5 = 0; i5 < arrayList3.size(); i5++) {
                if (arrayList3.get(i5) != null && ((TLRPC$TL_messages_stickerSet) arrayList3.get(i5)).documents != null) {
                    ArrayList arrayList4 = new ArrayList(((TLRPC$TL_messages_stickerSet) arrayList3.get(i5)).documents);
                    Collections.shuffle(arrayList4);
                    int i6 = 0;
                    while (true) {
                        if (i6 >= arrayList4.size()) {
                            break;
                        }
                        TLRPC$Document tLRPC$Document3 = (TLRPC$Document) arrayList4.get(i6);
                        if (tLRPC$Document3 != null && emptyViewEmojis.contains(MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document3, null))) {
                            int i7 = round - 1;
                            if (round <= 0) {
                                round = i7;
                                tLRPC$Document = tLRPC$Document3;
                                break;
                            }
                            round = i7;
                            tLRPC$Document = tLRPC$Document3;
                        }
                        i6++;
                    }
                }
                if (tLRPC$Document != null && round <= 0) {
                    break;
                }
            }
        }
        TLRPC$Document tLRPC$Document4 = tLRPC$Document;
        if (tLRPC$Document4 != null) {
            String str = "36_36";
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document4.thumbs, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document4.thumbs, 90);
            if ("video/webm".equals(tLRPC$Document4.mime_type)) {
                forDocument = ImageLocation.getForDocument(tLRPC$Document4);
                str = "36_36_" + ImageLoader.AUTOPLAY_FILTER;
                if (svgThumb != null) {
                    svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                }
            } else {
                if (svgThumb != null && MessageObject.isAnimatedStickerDocument(tLRPC$Document4, false)) {
                    svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                }
                forDocument = ImageLocation.getForDocument(tLRPC$Document4);
            }
            String str2 = str;
            ImageLocation imageLocation = forDocument;
            backupImageView.setLayerNum(7);
            backupImageView.setRoundRadius(AndroidUtilities.dp(4.0f));
            backupImageView.setImage(imageLocation, str2, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document4), "36_36", svgThumb, tLRPC$Document4);
        }
    }

    public void switchSearchEmptyView(final boolean z) {
        if (this.searchEmptyViewVisible == z) {
            return;
        }
        this.searchEmptyViewVisible = z;
        ValueAnimator valueAnimator = this.searchEmptyViewAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.searchEmptyViewAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                SelectAnimatedEmojiDialog.this.lambda$switchSearchEmptyView$11(z, valueAnimator2);
            }
        });
        this.searchEmptyViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.23
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                selectAnimatedEmojiDialog.emojiSearchEmptyView.setVisibility((z && selectAnimatedEmojiDialog.emojiSearchGridView.getVisibility() == 0) ? 0 : 8);
                SelectAnimatedEmojiDialog.this.searchEmptyViewAnimator = null;
            }
        });
        this.searchEmptyViewAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.searchEmptyViewAnimator.setDuration(100L);
        this.searchEmptyViewAnimator.start();
        if (z) {
            updateSearchEmptyViewImage(this.currentAccount, this.emojiSearchEmptyViewImageView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchSearchEmptyView$11(boolean z, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!z) {
            floatValue = 1.0f - floatValue;
        }
        this.emojiSearchEmptyView.setAlpha(this.emojiSearchGridView.getAlpha() * floatValue);
    }

    public void search(String str) {
        search(str, true, true);
    }

    public void setPaused(boolean z, boolean z2) {
        if (this.paused == z) {
            return;
        }
        this.paused = z;
        this.pausedExceptSelected = z2;
        EmojiListView emojiListView = this.emojiGridView;
        if (emojiListView != null) {
            emojiListView.invalidate();
        }
        EmojiListView emojiListView2 = this.emojiSearchGridView;
        if (emojiListView2 != null) {
            emojiListView2.invalidate();
        }
    }

    public void search(final String str, final boolean z, boolean z2) {
        Runnable runnable = this.clearSearchRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.clearSearchRunnable = null;
        }
        Runnable runnable2 = this.searchRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.searchRunnable = null;
        }
        if (TextUtils.isEmpty(str)) {
            this.searching = false;
            this.searched = false;
            switchGrids(false, z);
            SearchBox searchBox = this.searchBox;
            if (searchBox != null) {
                searchBox.showProgress(false);
                this.searchBox.toggleClear(false);
            }
            this.searchAdapter.updateRows(true);
            this.lastQuery = null;
        } else {
            final boolean z3 = !this.searching;
            this.searching = true;
            this.searched = false;
            SearchBox searchBox2 = this.searchBox;
            if (searchBox2 != null) {
                searchBox2.showProgress(true);
            }
            if (z3) {
                ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList = this.searchResult;
                if (arrayList != null) {
                    arrayList.clear();
                }
                ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList2 = this.searchResultStickers;
                if (arrayList2 != null) {
                    arrayList2.clear();
                }
                ArrayList<TLRPC$Document> arrayList3 = this.searchSets;
                if (arrayList3 != null) {
                    arrayList3.clear();
                }
                this.searchAdapter.updateRows(false);
            } else if (!str.equals(this.lastQuery)) {
                Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.this.lambda$search$12();
                    }
                };
                this.clearSearchRunnable = runnable3;
                AndroidUtilities.runOnUIThread(runnable3, 120L);
            }
            this.lastQuery = str;
            String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
            if (!Arrays.equals(currentKeyboardLanguage, lastSearchKeyboardLanguage)) {
                MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
            }
            lastSearchKeyboardLanguage = currentKeyboardLanguage;
            Runnable runnable4 = new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    SelectAnimatedEmojiDialog.this.lambda$search$29(str, z, z3);
                }
            };
            this.searchRunnable = runnable4;
            AndroidUtilities.runOnUIThread(runnable4, z2 ? 425L : 0L);
            SearchBox searchBox3 = this.searchBox;
            if (searchBox3 != null) {
                searchBox3.showProgress(true);
                this.searchBox.toggleClear(z);
            }
        }
        updateSearchBox();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$12() {
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList = this.searchResult;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList2 = this.searchResultStickers;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        ArrayList<TLRPC$Document> arrayList3 = this.searchSets;
        if (arrayList3 != null) {
            arrayList3.clear();
        }
        this.searchAdapter.updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$29(final String str, final boolean z, final boolean z2) {
        Utilities.Callback callback;
        final LinkedHashSet linkedHashSet = new LinkedHashSet();
        final LinkedHashSet linkedHashSet2 = new LinkedHashSet();
        final HashMap<String, TLRPC$TL_availableReaction> reactionsMap = MediaDataController.getInstance(this.currentAccount).getReactionsMap();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final boolean fullyConsistsOfEmojis = Emoji.fullyConsistsOfEmojis(str);
        final ArrayList arrayList3 = new ArrayList();
        final HashMap hashMap = new HashMap();
        final ArrayList arrayList4 = new ArrayList();
        Utilities.Callback callback2 = new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda36
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                SelectAnimatedEmojiDialog.this.lambda$search$14(str, z, arrayList, reactionsMap, arrayList2, linkedHashSet, linkedHashSet2, arrayList4, arrayList3, z2, (Runnable) obj);
            }
        };
        int i = this.type;
        if (i == 13) {
            Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda35
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.this.lambda$search$16(str, linkedHashSet2, (Runnable) obj);
                }
            }, callback2);
        } else if (i == 14) {
            if (fullyConsistsOfEmojis) {
                callback = new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda31
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SelectAnimatedEmojiDialog.this.lambda$search$17(str, arrayList2, arrayList, (Runnable) obj);
                    }
                };
            } else {
                callback = new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda32
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SelectAnimatedEmojiDialog.this.lambda$search$19(str, arrayList2, arrayList, (Runnable) obj);
                    }
                };
            }
            Utilities.doCallbacks(callback, callback2);
        } else {
            Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda38
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.lambda$search$21(fullyConsistsOfEmojis, str, linkedHashSet, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda34
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.this.lambda$search$23(str, linkedHashSet, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda37
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.this.lambda$search$25(fullyConsistsOfEmojis, linkedHashSet, str, reactionsMap, arrayList, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda33
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.this.lambda$search$27(str, arrayList3, hashMap, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda30
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.this.lambda$search$28(str, arrayList4, (Runnable) obj);
                }
            }, callback2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$14(final String str, final boolean z, final ArrayList arrayList, final HashMap hashMap, final ArrayList arrayList2, final LinkedHashSet linkedHashSet, final LinkedHashSet linkedHashSet2, final ArrayList arrayList3, final ArrayList arrayList4, final boolean z2, Runnable runnable) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$search$13(str, z, arrayList, hashMap, arrayList2, linkedHashSet, linkedHashSet2, arrayList3, arrayList4, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$13(String str, boolean z, ArrayList arrayList, HashMap hashMap, ArrayList arrayList2, LinkedHashSet linkedHashSet, LinkedHashSet linkedHashSet2, ArrayList arrayList3, ArrayList arrayList4, boolean z2) {
        Runnable runnable = this.clearSearchRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.clearSearchRunnable = null;
        }
        if (str != this.lastQuery) {
            return;
        }
        this.searched = true;
        switchGrids(true, z);
        SearchBox searchBox = this.searchBox;
        if (searchBox != null) {
            searchBox.showProgress(false);
        }
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList5 = this.searchResult;
        if (arrayList5 == null) {
            this.searchResult = new ArrayList<>();
        } else {
            arrayList5.clear();
        }
        ArrayList<TLRPC$Document> arrayList6 = this.searchSets;
        if (arrayList6 == null) {
            this.searchSets = new ArrayList<>();
        } else {
            arrayList6.clear();
        }
        ArrayList<TLRPC$Document> arrayList7 = this.stickersSearchResult;
        if (arrayList7 == null) {
            this.stickersSearchResult = new ArrayList<>();
        } else {
            arrayList7.clear();
        }
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList8 = this.searchResultStickers;
        if (arrayList8 == null) {
            this.searchResultStickers = new ArrayList<>();
        } else {
            arrayList8.clear();
        }
        this.emojiSearchGridView.scrollToPosition(0);
        int i = this.type;
        if (i == 1 || i == 14 || i == 11 || i == 2) {
            if (!arrayList.isEmpty()) {
                this.searchResult.addAll(arrayList);
            } else {
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = (TLRPC$TL_availableReaction) hashMap.get(str);
                if (tLRPC$TL_availableReaction != null) {
                    this.searchResult.add(ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(tLRPC$TL_availableReaction));
                }
            }
            if (!arrayList2.isEmpty()) {
                this.searchResultStickers.addAll(arrayList2);
            }
        }
        Iterator it = linkedHashSet.iterator();
        while (it.hasNext()) {
            this.searchResult.add(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(Long.valueOf(((Long) it.next()).longValue())));
        }
        Iterator it2 = linkedHashSet2.iterator();
        while (it2.hasNext()) {
            this.searchResult.add(ReactionsLayoutInBubble.VisibleReaction.fromEmojicon((String) it2.next()));
        }
        this.searchSets.addAll(arrayList3);
        Iterator it3 = arrayList4.iterator();
        while (it3.hasNext()) {
            this.stickersSearchResult.addAll((ArrayList) it3.next());
        }
        this.searchAdapter.updateRows(!z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$16(String str, final LinkedHashSet linkedHashSet, final Runnable runnable) {
        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(lastSearchKeyboardLanguage, str, false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda25
            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
            public final void run(ArrayList arrayList, String str2) {
                SelectAnimatedEmojiDialog.lambda$search$15(linkedHashSet, runnable, arrayList, str2);
            }
        }, null, false, false, false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$search$15(LinkedHashSet linkedHashSet, Runnable runnable, ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                if (!((MediaDataController.KeywordResult) arrayList.get(i)).emoji.startsWith("animated_")) {
                    String fixEmoji = Emoji.fixEmoji(((MediaDataController.KeywordResult) arrayList.get(i)).emoji);
                    if (Emoji.getEmojiDrawable(fixEmoji) != null) {
                        linkedHashSet.add(fixEmoji);
                    }
                }
            } catch (Exception unused) {
            }
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$17(String str, ArrayList arrayList, ArrayList arrayList2, Runnable runnable) {
        TLRPC$messages_AvailableEffects availableEffects = MessagesController.getInstance(this.currentAccount).getAvailableEffects();
        if (availableEffects != null) {
            for (int i = 0; i < availableEffects.effects.size(); i++) {
                try {
                    TLRPC$TL_availableEffect tLRPC$TL_availableEffect = availableEffects.effects.get(i);
                    if (str.contains(tLRPC$TL_availableEffect.emoticon)) {
                        ((tLRPC$TL_availableEffect.effect_animation_id > 0L ? 1 : (tLRPC$TL_availableEffect.effect_animation_id == 0L ? 0 : -1)) == 0 ? arrayList : arrayList2).add(ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$TL_availableEffect));
                    }
                } catch (Exception unused) {
                }
            }
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$19(String str, final ArrayList arrayList, final ArrayList arrayList2, final Runnable runnable) {
        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(lastSearchKeyboardLanguage, str, false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda26
            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
            public final void run(ArrayList arrayList3, String str2) {
                SelectAnimatedEmojiDialog.this.lambda$search$18(arrayList, arrayList2, runnable, arrayList3, str2);
            }
        }, null, false, false, false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$18(ArrayList arrayList, ArrayList arrayList2, Runnable runnable, ArrayList arrayList3, String str) {
        TLRPC$messages_AvailableEffects availableEffects = MessagesController.getInstance(this.currentAccount).getAvailableEffects();
        HashSet hashSet = new HashSet();
        if (availableEffects != null) {
            for (int i = 0; i < arrayList3.size(); i++) {
                try {
                    if (!((MediaDataController.KeywordResult) arrayList3.get(i)).emoji.startsWith("animated_")) {
                        String fixEmoji = Emoji.fixEmoji(((MediaDataController.KeywordResult) arrayList3.get(i)).emoji);
                        for (int i2 = 0; i2 < availableEffects.effects.size(); i2++) {
                            TLRPC$TL_availableEffect tLRPC$TL_availableEffect = availableEffects.effects.get(i2);
                            if (!hashSet.contains(Long.valueOf(tLRPC$TL_availableEffect.id)) && (tLRPC$TL_availableEffect.emoticon.contains(fixEmoji) || fixEmoji.contains(tLRPC$TL_availableEffect.emoticon))) {
                                ((tLRPC$TL_availableEffect.effect_animation_id > 0L ? 1 : (tLRPC$TL_availableEffect.effect_animation_id == 0L ? 0 : -1)) == 0 ? arrayList : arrayList2).add(ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$TL_availableEffect));
                                hashSet.add(Long.valueOf(tLRPC$TL_availableEffect.id));
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$search$21(boolean z, String str, final LinkedHashSet linkedHashSet, final Runnable runnable) {
        if (z) {
            StickerCategoriesListView.search.fetch(UserConfig.selectedAccount, str, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda29
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SelectAnimatedEmojiDialog.lambda$search$20(linkedHashSet, runnable, (TLRPC$TL_emojiList) obj);
                }
            });
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$search$20(LinkedHashSet linkedHashSet, Runnable runnable, TLRPC$TL_emojiList tLRPC$TL_emojiList) {
        if (tLRPC$TL_emojiList != null) {
            linkedHashSet.addAll(tLRPC$TL_emojiList.document_id);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$23(String str, final LinkedHashSet linkedHashSet, final Runnable runnable) {
        MediaDataController.getInstance(this.currentAccount).getAnimatedEmojiByKeywords(str, new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda28
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                SelectAnimatedEmojiDialog.lambda$search$22(linkedHashSet, runnable, (ArrayList) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$search$22(LinkedHashSet linkedHashSet, Runnable runnable, ArrayList arrayList) {
        if (arrayList != null) {
            linkedHashSet.addAll(arrayList);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$25(boolean z, final LinkedHashSet linkedHashSet, String str, final HashMap hashMap, final ArrayList arrayList, final Runnable runnable) {
        ArrayList<TLRPC$Document> arrayList2;
        ArrayList<TLRPC$Document> arrayList3;
        if (z) {
            ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(this.currentAccount).getStickerSets(5);
            for (int i = 0; i < stickerSets.size(); i++) {
                if (stickerSets.get(i).documents != null && (arrayList3 = stickerSets.get(i).documents) != null) {
                    for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                        String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(arrayList3.get(i2), null);
                        long j = arrayList3.get(i2).id;
                        if (findAnimatedEmojiEmoticon != null && !linkedHashSet.contains(Long.valueOf(j)) && str.contains(findAnimatedEmojiEmoticon.toLowerCase())) {
                            linkedHashSet.add(Long.valueOf(j));
                        }
                    }
                }
            }
            ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = MediaDataController.getInstance(this.currentAccount).getFeaturedEmojiSets();
            for (int i3 = 0; i3 < featuredEmojiSets.size(); i3++) {
                if ((featuredEmojiSets.get(i3) instanceof TLRPC$TL_stickerSetFullCovered) && ((TLRPC$TL_stickerSetFullCovered) featuredEmojiSets.get(i3)).keywords != null && (arrayList2 = ((TLRPC$TL_stickerSetFullCovered) featuredEmojiSets.get(i3)).documents) != null) {
                    for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                        String findAnimatedEmojiEmoticon2 = MessageObject.findAnimatedEmojiEmoticon(arrayList2.get(i4), null);
                        long j2 = arrayList2.get(i4).id;
                        if (findAnimatedEmojiEmoticon2 != null && !linkedHashSet.contains(Long.valueOf(j2)) && str.contains(findAnimatedEmojiEmoticon2)) {
                            linkedHashSet.add(Long.valueOf(j2));
                        }
                    }
                }
            }
            runnable.run();
            return;
        }
        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(lastSearchKeyboardLanguage, str, false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda27
            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
            public final void run(ArrayList arrayList4, String str2) {
                SelectAnimatedEmojiDialog.this.lambda$search$24(linkedHashSet, hashMap, arrayList, runnable, arrayList4, str2);
            }
        }, null, true, this.type == 3, false, 30);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$24(LinkedHashSet linkedHashSet, HashMap hashMap, ArrayList arrayList, Runnable runnable, ArrayList arrayList2, String str) {
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
        for (int i = 0; i < arrayList2.size(); i++) {
            try {
                if (((MediaDataController.KeywordResult) arrayList2.get(i)).emoji.startsWith("animated_")) {
                    linkedHashSet.add(Long.valueOf(Long.parseLong(((MediaDataController.KeywordResult) arrayList2.get(i)).emoji.substring(9))));
                } else {
                    int i2 = this.type;
                    if ((i2 == 1 || i2 == 11 || i2 == 2) && (tLRPC$TL_availableReaction = (TLRPC$TL_availableReaction) hashMap.get(((MediaDataController.KeywordResult) arrayList2.get(i)).emoji)) != null) {
                        arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(tLRPC$TL_availableReaction));
                    }
                }
            } catch (Exception unused) {
            }
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0052, code lost:
        if (r9.charAt(r10) <= 57343) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x006c, code lost:
        if (r9.charAt(r10) != 9794) goto L28;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v10, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r9v6, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$search$27(String str, final ArrayList arrayList, final HashMap hashMap, final Runnable runnable) {
        if (this.type != 4) {
            runnable.run();
            return;
        }
        ArrayList arrayList2 = new ArrayList(0);
        LongSparseArray longSparseArray = new LongSparseArray(0);
        final HashMap<String, ArrayList<TLRPC$Document>> allStickers = MediaDataController.getInstance(this.currentAccount).getAllStickers();
        if (str.length() <= 14) {
            int length = str.length();
            String str2 = str;
            int i = 0;
            while (i < length) {
                if (i < length - 1) {
                    if (str2.charAt(i) == 55356) {
                        int i2 = i + 1;
                        if (str2.charAt(i2) >= 57339) {
                        }
                    }
                    if (str2.charAt(i) == 8205) {
                        int i3 = i + 1;
                        if (str2.charAt(i3) != 9792) {
                        }
                        length -= 2;
                        str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 2, str2.length()));
                        i--;
                        i++;
                    }
                }
                if (str2.charAt(i) == 65039) {
                    length--;
                    str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 1, str2.length()));
                    i--;
                    i++;
                } else {
                    i++;
                }
            }
            ArrayList<TLRPC$Document> arrayList3 = allStickers != null ? allStickers.get(str2.toString()) : null;
            if (arrayList3 != null && !arrayList3.isEmpty()) {
                arrayList2.addAll(arrayList3);
                int size = arrayList3.size();
                for (int i4 = 0; i4 < size; i4++) {
                    TLRPC$Document tLRPC$Document = arrayList3.get(i4);
                    longSparseArray.put(tLRPC$Document.id, tLRPC$Document);
                }
                arrayList.add(arrayList2);
            }
        }
        if (allStickers == null || allStickers.isEmpty() || str.length() <= 1) {
            return;
        }
        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(lastSearchKeyboardLanguage, str, false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda24
            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
            public final void run(ArrayList arrayList4, String str3) {
                SelectAnimatedEmojiDialog.lambda$search$26(allStickers, hashMap, arrayList, runnable, arrayList4, str3);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$search$26(HashMap hashMap, HashMap hashMap2, ArrayList arrayList, Runnable runnable, ArrayList arrayList2, String str) {
        int size = arrayList2.size();
        for (int i = 0; i < size; i++) {
            String str2 = ((MediaDataController.KeywordResult) arrayList2.get(i)).emoji;
            ArrayList arrayList3 = hashMap != null ? (ArrayList) hashMap.get(str2) : null;
            if (arrayList3 != null && !arrayList3.isEmpty() && !hashMap2.containsKey(arrayList3)) {
                hashMap2.put(arrayList3, str2);
                arrayList.add(arrayList3);
            }
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$search$28(String str, ArrayList arrayList, Runnable runnable) {
        TLRPC$StickerSet tLRPC$StickerSet;
        TLRPC$StickerSet tLRPC$StickerSet2;
        ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(this.currentAccount).getStickerSets(5);
        HashSet hashSet = new HashSet();
        String translitSafe = AndroidUtilities.translitSafe(str);
        String str2 = " " + translitSafe;
        if (stickerSets != null) {
            for (int i = 0; i < stickerSets.size(); i++) {
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i);
                if (tLRPC$TL_messages_stickerSet != null && (tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set) != null && tLRPC$StickerSet2.title != null && tLRPC$TL_messages_stickerSet.documents != null && !hashSet.contains(Long.valueOf(tLRPC$StickerSet2.id))) {
                    String translitSafe2 = AndroidUtilities.translitSafe(tLRPC$TL_messages_stickerSet.set.title);
                    if (translitSafe2.startsWith(translitSafe) || translitSafe2.contains(str2)) {
                        arrayList.add(new SetTitleDocument(translitSafe2));
                        arrayList.addAll(tLRPC$TL_messages_stickerSet.documents);
                        hashSet.add(Long.valueOf(tLRPC$TL_messages_stickerSet.set.id));
                    }
                }
            }
        }
        ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = MediaDataController.getInstance(this.currentAccount).getFeaturedEmojiSets();
        if (featuredEmojiSets != null) {
            for (int i2 = 0; i2 < featuredEmojiSets.size(); i2++) {
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = featuredEmojiSets.get(i2);
                if (tLRPC$StickerSetCovered != null && (tLRPC$StickerSet = tLRPC$StickerSetCovered.set) != null && tLRPC$StickerSet.title != null && !hashSet.contains(Long.valueOf(tLRPC$StickerSet.id))) {
                    String translitSafe3 = AndroidUtilities.translitSafe(tLRPC$StickerSetCovered.set.title);
                    if (translitSafe3.startsWith(translitSafe) || translitSafe3.contains(str2)) {
                        ArrayList<TLRPC$Document> arrayList2 = null;
                        if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetNoCovered) {
                            TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSet(MediaDataController.getInputStickerSet(tLRPC$StickerSetCovered.set), Integer.valueOf(tLRPC$StickerSetCovered.set.hash), true);
                            if (stickerSet != null) {
                                arrayList2 = stickerSet.documents;
                            }
                        } else if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
                            arrayList2 = ((TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered).documents;
                        } else {
                            arrayList2 = tLRPC$StickerSetCovered.covers;
                        }
                        if (arrayList2 != null && arrayList2.size() != 0) {
                            arrayList.add(new SetTitleDocument(tLRPC$StickerSetCovered.set.title));
                            arrayList.addAll(arrayList2);
                            hashSet.add(Long.valueOf(tLRPC$StickerSetCovered.set.id));
                        }
                    }
                }
            }
        }
        runnable.run();
    }

    public static TLRPC$Document findSticker(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, String str) {
        long j;
        if (tLRPC$TL_messages_stickerSet == null) {
            return null;
        }
        String fixEmoji = Emoji.fixEmoji(str);
        int i = 0;
        while (true) {
            if (i >= tLRPC$TL_messages_stickerSet.packs.size()) {
                j = 0;
                break;
            } else if (!tLRPC$TL_messages_stickerSet.packs.get(i).documents.isEmpty() && TextUtils.equals(Emoji.fixEmoji(tLRPC$TL_messages_stickerSet.packs.get(i).emoticon), fixEmoji)) {
                j = tLRPC$TL_messages_stickerSet.packs.get(i).documents.get(0).longValue();
                break;
            } else {
                i++;
            }
        }
        if (j == 0) {
            return null;
        }
        for (int i2 = 0; i2 < tLRPC$TL_messages_stickerSet.documents.size(); i2++) {
            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i2);
            if (tLRPC$Document.id == j) {
                return tLRPC$Document;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private int count;
        int emojiHeaderRow;
        int emojiStartRow;
        private ArrayList<Integer> rowHashCodes;
        int setsStartRow;
        int stickersHeaderRow;
        int stickersStartRow;

        private SearchAdapter() {
            this.emojiHeaderRow = -1;
            this.stickersHeaderRow = -1;
            this.count = 1;
            this.rowHashCodes = new ArrayList<>();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 4;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View imageViewEmoji;
            if (i == 6) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                imageViewEmoji = new HeaderView(selectAnimatedEmojiDialog.getContext(), SelectAnimatedEmojiDialog.this.type == 6);
            } else if (i == 7) {
                imageViewEmoji = new View(this, SelectAnimatedEmojiDialog.this.getContext()) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchAdapter.1
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0f), 1073741824));
                    }
                };
                imageViewEmoji.setTag("searchbox");
            } else {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                imageViewEmoji = new ImageViewEmoji(selectAnimatedEmojiDialog2.getContext());
            }
            if (SelectAnimatedEmojiDialog.this.enterAnimationInProgress()) {
                imageViewEmoji.setScaleX(0.0f);
                imageViewEmoji.setScaleY(0.0f);
            }
            return new RecyclerListView.Holder(imageViewEmoji);
        }

        public boolean isSticker(int i) {
            int i2;
            if (SelectAnimatedEmojiDialog.this.type == 14) {
                return SelectAnimatedEmojiDialog.this.searchResultStickers != null && i >= (i2 = this.stickersStartRow) && i - i2 < SelectAnimatedEmojiDialog.this.searchResultStickers.size();
            }
            int i3 = this.stickersStartRow;
            return i > i3 && (i - i3) - 1 < SelectAnimatedEmojiDialog.this.stickersSearchResult.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            int i2;
            if (i == this.emojiHeaderRow || i == this.stickersHeaderRow) {
                return 6;
            }
            if (SelectAnimatedEmojiDialog.this.type == 14) {
                if (SelectAnimatedEmojiDialog.this.searchResultStickers != null && i >= (i2 = this.stickersStartRow) && i - i2 < SelectAnimatedEmojiDialog.this.searchResultStickers.size()) {
                    return 4;
                }
            } else {
                int i3 = this.stickersStartRow;
                if (i > i3 && (i - i3) - 1 < SelectAnimatedEmojiDialog.this.stickersSearchResult.size()) {
                    return 5;
                }
            }
            if (SelectAnimatedEmojiDialog.this.searchResult == null) {
                return 3;
            }
            int i4 = this.emojiStartRow;
            if (i <= i4 || (i - i4) - 1 >= SelectAnimatedEmojiDialog.this.searchResult.size() || (SelectAnimatedEmojiDialog.this.type != 13 && ((ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.searchResult.get((i - this.emojiStartRow) - 1)).documentId == 0)) {
                int i5 = this.setsStartRow;
                if (i - i5 < 0 || i - i5 >= SelectAnimatedEmojiDialog.this.searchSets.size()) {
                    return 4;
                }
                return SelectAnimatedEmojiDialog.this.searchSets.get(i - this.setsStartRow) instanceof SetTitleDocument ? 6 : 3;
            }
            return 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Document tLRPC$Document;
            Long l;
            boolean contains;
            int i2;
            ReactionsLayoutInBubble.VisibleReaction visibleReaction;
            if (viewHolder.getItemViewType() == 6) {
                HeaderView headerView = (HeaderView) viewHolder.itemView;
                if (SelectAnimatedEmojiDialog.this.searchSets != null) {
                    int i3 = this.setsStartRow;
                    if (i - i3 >= 0 && i - i3 < SelectAnimatedEmojiDialog.this.searchSets.size()) {
                        TLRPC$Document tLRPC$Document2 = (TLRPC$Document) SelectAnimatedEmojiDialog.this.searchSets.get(i - this.setsStartRow);
                        if (tLRPC$Document2 instanceof SetTitleDocument) {
                            headerView.setText(((SetTitleDocument) tLRPC$Document2).title, SelectAnimatedEmojiDialog.this.lastQuery, false);
                        }
                        headerView.closeIcon.setVisibility(8);
                    }
                }
                if (i != this.emojiHeaderRow) {
                    if (SelectAnimatedEmojiDialog.this.type == 14) {
                        headerView.setText(LocaleController.getString(R.string.StickerEffects), false);
                    } else {
                        headerView.setText(LocaleController.getString(R.string.AccDescrStickers), false);
                    }
                } else {
                    headerView.setText(LocaleController.getString("Emoji", R.string.Emoji), false);
                }
                headerView.closeIcon.setVisibility(8);
            } else if (viewHolder.getItemViewType() == 5) {
                TLRPC$Document tLRPC$Document3 = (TLRPC$Document) SelectAnimatedEmojiDialog.this.stickersSearchResult.get((i - this.stickersStartRow) - 1);
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) viewHolder.itemView;
                imageViewEmoji.createImageReceiver(SelectAnimatedEmojiDialog.this.emojiSearchGridView);
                imageViewEmoji.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document3), "100_100_firstframe", null, null, DocumentObject.getSvgThumb(tLRPC$Document3, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f), 0L, "tgs", tLRPC$Document3, 0);
                imageViewEmoji.isStaticIcon = true;
                imageViewEmoji.document = tLRPC$Document3;
                imageViewEmoji.span = null;
            } else if (viewHolder.getItemViewType() == 4) {
                ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) viewHolder.itemView;
                imageViewEmoji2.position = i;
                if (SelectAnimatedEmojiDialog.this.searchResult == null || i < 0 || i >= SelectAnimatedEmojiDialog.this.searchResult.size()) {
                    if (SelectAnimatedEmojiDialog.this.searchResultStickers == null || i < (i2 = this.stickersStartRow) || i - i2 >= SelectAnimatedEmojiDialog.this.searchResultStickers.size()) {
                        return;
                    }
                    visibleReaction = (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.searchResultStickers.get(i - this.stickersStartRow);
                } else {
                    visibleReaction = (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.searchResult.get(i);
                }
                if (imageViewEmoji2.imageReceiver == null) {
                    ImageReceiver imageReceiver = new ImageReceiver(imageViewEmoji2);
                    imageViewEmoji2.imageReceiver = imageReceiver;
                    imageReceiver.setLayerNum(7);
                    imageViewEmoji2.imageReceiver.onAttachedToWindow();
                }
                imageViewEmoji2.imageReceiver.setParentView(SelectAnimatedEmojiDialog.this.emojiSearchGridView);
                imageViewEmoji2.reaction = visibleReaction;
                imageViewEmoji2.isFirstReactions = false;
                imageViewEmoji2.setViewSelected(SelectAnimatedEmojiDialog.this.selectedReactions.contains(visibleReaction), false);
                imageViewEmoji2.notDraw = false;
                imageViewEmoji2.invalidate();
                if (SelectAnimatedEmojiDialog.this.type == 13) {
                    imageViewEmoji2.setDrawable(Emoji.getEmojiDrawable(visibleReaction.emojicon));
                } else if (!visibleReaction.isEffect && visibleReaction.emojicon != null) {
                    imageViewEmoji2.isDefaultReaction = true;
                    TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).getReactionsMap().get(visibleReaction.emojicon);
                    if (tLRPC$TL_availableReaction != null) {
                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.activate_animation, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
                        if (!LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
                            imageViewEmoji2.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_firstframe", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                        } else {
                            imageViewEmoji2.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_pcache", ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "30_30_firstframe", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                        }
                        MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).preloadImage(imageViewEmoji2.preloadEffectImageReceiver, ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation());
                    } else {
                        imageViewEmoji2.imageReceiver.clearImage();
                        imageViewEmoji2.preloadEffectImageReceiver.clearImage();
                    }
                    imageViewEmoji2.span = null;
                    imageViewEmoji2.document = null;
                    imageViewEmoji2.setDrawable(null);
                    PremiumLockIconView premiumLockIconView = imageViewEmoji2.premiumLockIconView;
                    if (premiumLockIconView != null) {
                        premiumLockIconView.setVisibility(8);
                        imageViewEmoji2.premiumLockIconView.setImageReceiver(null);
                    }
                    if (tLRPC$TL_availableReaction == null && visibleReaction.isEffect) {
                        imageViewEmoji2.setDrawable(Emoji.getEmojiDrawable(visibleReaction.emojicon));
                    }
                } else {
                    imageViewEmoji2.isDefaultReaction = false;
                    imageViewEmoji2.span = new AnimatedEmojiSpan(visibleReaction.documentId, (Paint.FontMetricsInt) null);
                    imageViewEmoji2.document = null;
                    imageViewEmoji2.imageReceiver.clearImage();
                    imageViewEmoji2.preloadEffectImageReceiver.clearImage();
                    AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) SelectAnimatedEmojiDialog.this.emojiSearchGridView.animatedEmojiDrawables.get(imageViewEmoji2.span.getDocumentId());
                    if (animatedEmojiDrawable == null) {
                        animatedEmojiDrawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji2.span.getDocumentId());
                        SelectAnimatedEmojiDialog.this.emojiSearchGridView.animatedEmojiDrawables.put(imageViewEmoji2.span.getDocumentId(), animatedEmojiDrawable);
                    }
                    imageViewEmoji2.setDrawable(animatedEmojiDrawable);
                }
                if (!UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium() && SelectAnimatedEmojiDialog.this.type == 14 && visibleReaction.isEffect && visibleReaction.premium) {
                    imageViewEmoji2.createPremiumLockView();
                    imageViewEmoji2.premiumLockIconView.setVisibility(0);
                    imageViewEmoji2.setEmojicon(null);
                    return;
                }
                if (visibleReaction.sticker) {
                    imageViewEmoji2.setEmojicon(visibleReaction.emojicon);
                } else {
                    imageViewEmoji2.setEmojicon(null);
                }
                PremiumLockIconView premiumLockIconView2 = imageViewEmoji2.premiumLockIconView;
                if (premiumLockIconView2 != null) {
                    premiumLockIconView2.setVisibility(4);
                }
            } else if (viewHolder.getItemViewType() == 3) {
                ImageViewEmoji imageViewEmoji3 = (ImageViewEmoji) viewHolder.itemView;
                imageViewEmoji3.empty = false;
                imageViewEmoji3.position = i;
                imageViewEmoji3.setPadding(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                imageViewEmoji3.setDrawable(null);
                if (SelectAnimatedEmojiDialog.this.searchResult == null || i < 0 || i >= SelectAnimatedEmojiDialog.this.searchResult.size()) {
                    if (SelectAnimatedEmojiDialog.this.searchSets != null) {
                        int i4 = this.setsStartRow;
                        if (i - i4 >= 0 && i - i4 < SelectAnimatedEmojiDialog.this.searchSets.size()) {
                            tLRPC$Document = (TLRPC$Document) SelectAnimatedEmojiDialog.this.searchSets.get(i - this.setsStartRow);
                            if (!(tLRPC$Document instanceof SetTitleDocument)) {
                                l = null;
                            }
                        }
                    }
                    tLRPC$Document = null;
                    l = null;
                } else {
                    ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.searchResult.get(i);
                    imageViewEmoji3.reaction = visibleReaction2;
                    long j = visibleReaction2.documentId;
                    if (j == 0) {
                        boolean contains2 = SelectAnimatedEmojiDialog.this.selectedReactions.contains(visibleReaction2);
                        imageViewEmoji3.isFirstReactions = true;
                        imageViewEmoji3.setDrawable(Emoji.getEmojiDrawable(visibleReaction2.emojicon));
                        imageViewEmoji3.setViewSelected(contains2, false);
                        return;
                    }
                    l = Long.valueOf(j);
                    if (SelectAnimatedEmojiDialog.this.type == 14 && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium() && visibleReaction2.isEffect && visibleReaction2.premium) {
                        imageViewEmoji3.createPremiumLockView();
                        imageViewEmoji3.premiumLockIconView.setVisibility(0);
                    } else {
                        PremiumLockIconView premiumLockIconView3 = imageViewEmoji3.premiumLockIconView;
                        if (premiumLockIconView3 != null) {
                            premiumLockIconView3.setVisibility(4);
                        }
                    }
                    tLRPC$Document = null;
                }
                if (l == null && tLRPC$Document == null) {
                    contains = false;
                } else {
                    if (tLRPC$Document != null) {
                        imageViewEmoji3.span = new AnimatedEmojiSpan(tLRPC$Document, (Paint.FontMetricsInt) null);
                        imageViewEmoji3.document = tLRPC$Document;
                        contains = SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(Long.valueOf(tLRPC$Document.id));
                    } else {
                        AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(l.longValue(), (Paint.FontMetricsInt) null);
                        imageViewEmoji3.span = animatedEmojiSpan;
                        imageViewEmoji3.document = animatedEmojiSpan.document;
                        contains = SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(l);
                    }
                    AnimatedEmojiDrawable animatedEmojiDrawable2 = (AnimatedEmojiDrawable) SelectAnimatedEmojiDialog.this.emojiSearchGridView.animatedEmojiDrawables.get(imageViewEmoji3.span.getDocumentId());
                    if (animatedEmojiDrawable2 == null) {
                        animatedEmojiDrawable2 = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji3.span.getDocumentId());
                        SelectAnimatedEmojiDialog.this.emojiSearchGridView.animatedEmojiDrawables.put(imageViewEmoji3.span.getDocumentId(), animatedEmojiDrawable2);
                    }
                    imageViewEmoji3.setDrawable(animatedEmojiDrawable2);
                }
                imageViewEmoji3.setViewSelected(contains, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.count;
        }

        public void updateRows(boolean z) {
            if (SelectAnimatedEmojiDialog.this.isAttached) {
                int unused = SelectAnimatedEmojiDialog.this.type;
            }
            new ArrayList(this.rowHashCodes);
            this.setsStartRow = -1;
            this.stickersStartRow = -1;
            boolean z2 = false;
            this.count = 0;
            this.rowHashCodes.clear();
            if (SelectAnimatedEmojiDialog.this.searchResult != null) {
                if (SelectAnimatedEmojiDialog.this.type == 4 && !SelectAnimatedEmojiDialog.this.searchResult.isEmpty()) {
                    int i = this.count;
                    this.count = i + 1;
                    this.emojiHeaderRow = i;
                    this.rowHashCodes.add(1);
                }
                this.emojiStartRow = this.count;
                for (int i2 = 0; i2 < SelectAnimatedEmojiDialog.this.searchResult.size(); i2++) {
                    this.count++;
                    this.rowHashCodes.add(Integer.valueOf(Objects.hash(-4342, SelectAnimatedEmojiDialog.this.searchResult.get(i2))));
                }
            }
            if (SelectAnimatedEmojiDialog.this.type == 14) {
                if (SelectAnimatedEmojiDialog.this.searchResultStickers != null && !SelectAnimatedEmojiDialog.this.searchResultStickers.isEmpty()) {
                    int i3 = this.count;
                    this.count = i3 + 1;
                    this.stickersHeaderRow = i3;
                    this.rowHashCodes.add(2);
                    this.stickersStartRow = this.count;
                    for (int i4 = 0; i4 < SelectAnimatedEmojiDialog.this.searchResultStickers.size(); i4++) {
                        this.count++;
                        this.rowHashCodes.add(Integer.valueOf(Objects.hash(-7453, SelectAnimatedEmojiDialog.this.searchResultStickers.get(i4))));
                    }
                }
            } else if (SelectAnimatedEmojiDialog.this.stickersSearchResult != null) {
                if (SelectAnimatedEmojiDialog.this.type == 4 && !SelectAnimatedEmojiDialog.this.stickersSearchResult.isEmpty()) {
                    int i5 = this.count;
                    this.count = i5 + 1;
                    this.stickersHeaderRow = i5;
                    this.rowHashCodes.add(2);
                }
                this.stickersStartRow = this.count;
                for (int i6 = 0; i6 < SelectAnimatedEmojiDialog.this.stickersSearchResult.size(); i6++) {
                    this.count++;
                    this.rowHashCodes.add(Integer.valueOf(Objects.hash(-7453, SelectAnimatedEmojiDialog.this.stickersSearchResult.get(i6))));
                }
            }
            if (SelectAnimatedEmojiDialog.this.searchSets != null) {
                int i7 = this.count;
                this.setsStartRow = i7;
                this.count = i7 + SelectAnimatedEmojiDialog.this.searchSets.size();
            }
            notifyDataSetChanged();
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            if (selectAnimatedEmojiDialog.searched && this.count == 0) {
                z2 = true;
            }
            selectAnimatedEmojiDialog.switchSearchEmptyView(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        private Adapter() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 2 || itemViewType == 1 || itemViewType == 3 || itemViewType == 8;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView textView;
            if (i == 0) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                textView = new HeaderView(selectAnimatedEmojiDialog.getContext(), SelectAnimatedEmojiDialog.this.type == 6);
            } else if (i == 2) {
                textView = new ImageView(SelectAnimatedEmojiDialog.this.getContext());
            } else if (i == 3 || i == 1 || i == 8) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog2 = SelectAnimatedEmojiDialog.this;
                ImageViewEmoji imageViewEmoji = new ImageViewEmoji(selectAnimatedEmojiDialog2.getContext());
                if (i == 8) {
                    imageViewEmoji.isStaticIcon = true;
                    ImageReceiver imageReceiver = new ImageReceiver(imageViewEmoji);
                    imageViewEmoji.imageReceiver = imageReceiver;
                    imageViewEmoji.imageReceiverToDraw = imageReceiver;
                    imageReceiver.setImageBitmap(SelectAnimatedEmojiDialog.this.forumIconDrawable);
                    SelectAnimatedEmojiDialog.this.forumIconImage = imageViewEmoji;
                    imageViewEmoji.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                }
                textView = imageViewEmoji;
            } else if (i == 4) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog3 = SelectAnimatedEmojiDialog.this;
                textView = new EmojiPackExpand(selectAnimatedEmojiDialog3, selectAnimatedEmojiDialog3.getContext(), null);
            } else if (i == 5) {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog4 = SelectAnimatedEmojiDialog.this;
                textView = new EmojiPackButton(selectAnimatedEmojiDialog4, selectAnimatedEmojiDialog4.getContext());
            } else if (i == 6) {
                TextView textView2 = new TextView(this, SelectAnimatedEmojiDialog.this.getContext()) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.Adapter.1
                    @Override // android.widget.TextView, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(AndroidUtilities.dp(26.0f)), 1073741824));
                    }
                };
                textView2.setTextSize(1, 13.0f);
                if (SelectAnimatedEmojiDialog.this.type != 3) {
                    if (SelectAnimatedEmojiDialog.this.type == 0 || SelectAnimatedEmojiDialog.this.type == 12 || SelectAnimatedEmojiDialog.this.type == 9 || SelectAnimatedEmojiDialog.this.type == 10) {
                        textView2.setText(LocaleController.getString("EmojiLongtapHint", R.string.EmojiLongtapHint));
                    } else {
                        textView2.setText(LocaleController.getString("ReactionsLongtapHint", R.string.ReactionsLongtapHint));
                    }
                } else {
                    textView2.setText(LocaleController.getString("SelectTopicIconHint", R.string.SelectTopicIconHint));
                }
                textView2.setGravity(17);
                textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, SelectAnimatedEmojiDialog.this.resourcesProvider));
                textView = textView2;
            } else if (i == 7) {
                View fixedHeightEmptyCell = new FixedHeightEmptyCell(SelectAnimatedEmojiDialog.this.getContext(), 52);
                fixedHeightEmptyCell.setTag("searchbox");
                textView = fixedHeightEmptyCell;
            } else {
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog5 = SelectAnimatedEmojiDialog.this;
                textView = new ImageViewEmoji(selectAnimatedEmojiDialog5.getContext());
            }
            if (SelectAnimatedEmojiDialog.this.enterAnimationInProgress()) {
                textView.setScaleX(0.0f);
                textView.setScaleY(0.0f);
            }
            return new RecyclerListView.Holder(textView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == SelectAnimatedEmojiDialog.this.searchRow) {
                return 7;
            }
            if (i < SelectAnimatedEmojiDialog.this.recentReactionsStartRow || i >= SelectAnimatedEmojiDialog.this.recentReactionsEndRow) {
                if (i < SelectAnimatedEmojiDialog.this.topReactionsStartRow || i >= SelectAnimatedEmojiDialog.this.topReactionsEndRow) {
                    if (i < SelectAnimatedEmojiDialog.this.stickersStartRow || i >= SelectAnimatedEmojiDialog.this.stickersEndRow) {
                        if (SelectAnimatedEmojiDialog.this.positionToExpand.indexOfKey(i) >= 0) {
                            return 4;
                        }
                        if (SelectAnimatedEmojiDialog.this.positionToButton.indexOfKey(i) >= 0) {
                            return 5;
                        }
                        if (i == SelectAnimatedEmojiDialog.this.longtapHintRow) {
                            return 6;
                        }
                        if (SelectAnimatedEmojiDialog.this.positionToSection.indexOfKey(i) >= 0 || i == SelectAnimatedEmojiDialog.this.recentReactionsSectionRow || i == SelectAnimatedEmojiDialog.this.stickersSectionRow || i == SelectAnimatedEmojiDialog.this.popularSectionRow || i == SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                            return 0;
                        }
                        if (i == SelectAnimatedEmojiDialog.this.defaultTopicIconRow) {
                            return 8;
                        }
                        boolean unused = SelectAnimatedEmojiDialog.this.showStickers;
                        return 3;
                    }
                    return 1;
                }
                return 1;
            }
            return 1;
        }

        /* JADX WARN: Code restructure failed: missing block: B:227:0x05f4, code lost:
            if (r27.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.id)) != false) goto L227;
         */
        /* JADX WARN: Code restructure failed: missing block: B:315:0x076a, code lost:
            if (r27.this$0.selectedDocumentIds.contains(java.lang.Long.valueOf(r2.getDocumentId())) != false) goto L227;
         */
        /* JADX WARN: Removed duplicated region for block: B:321:0x0773  */
        /* JADX WARN: Removed duplicated region for block: B:325:0x07b4  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int size;
            TLRPC$Document tLRPC$Document;
            boolean z;
            final EmojiView.EmojiPack emojiPack;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 8) {
                if (SelectAnimatedEmojiDialog.this.showAnimator == null || !SelectAnimatedEmojiDialog.this.showAnimator.isRunning()) {
                    viewHolder.itemView.setScaleX(1.0f);
                    viewHolder.itemView.setScaleY(1.0f);
                }
                boolean z2 = true;
                boolean z3 = true;
                boolean z4 = true;
                z2 = true;
                z2 = true;
                z2 = true;
                if (itemViewType == 6) {
                    TextView textView = (TextView) viewHolder.itemView;
                    if (SelectAnimatedEmojiDialog.this.hintExpireDate != null) {
                        textView.setText(LocaleController.formatString("EmojiStatusExpireHint", R.string.EmojiStatusExpireHint, LocaleController.formatStatusExpireDateTime(SelectAnimatedEmojiDialog.this.hintExpireDate.intValue())));
                        return;
                    }
                    return;
                } else if (itemViewType == 0) {
                    HeaderView headerView = (HeaderView) viewHolder.itemView;
                    if (i != SelectAnimatedEmojiDialog.this.topicEmojiHeaderRow) {
                        if (i != SelectAnimatedEmojiDialog.this.recentReactionsSectionRow) {
                            if (i == SelectAnimatedEmojiDialog.this.stickersSectionRow) {
                                headerView.setText(LocaleController.getString(R.string.StickerEffects), false);
                                headerView.closeIcon.setVisibility(8);
                                return;
                            }
                            headerView.closeIcon.setVisibility(8);
                            if (i != SelectAnimatedEmojiDialog.this.popularSectionRow) {
                                int i2 = SelectAnimatedEmojiDialog.this.positionToSection.get(i);
                                if (i2 >= 0) {
                                    EmojiView.EmojiPack emojiPack2 = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i2);
                                    headerView.setText(emojiPack2.set.title, (emojiPack2.free || UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium() || SelectAnimatedEmojiDialog.this.type == 4 || SelectAnimatedEmojiDialog.this.type == 5 || SelectAnimatedEmojiDialog.this.type == 7 || SelectAnimatedEmojiDialog.this.type == 6) ? false : false);
                                    return;
                                }
                                headerView.setText(null, false);
                                return;
                            }
                            headerView.setText(LocaleController.getString("PopularReactions", R.string.PopularReactions), false);
                            return;
                        }
                        headerView.setText(LocaleController.getString("RecentlyUsed", R.string.RecentlyUsed), false);
                        headerView.closeIcon.setVisibility(8);
                        return;
                    }
                    headerView.setText(LocaleController.getString("SelectTopicIconHint", R.string.SelectTopicIconHint), false);
                    headerView.closeIcon.setVisibility(8);
                    return;
                } else if (itemViewType == 1) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) viewHolder.itemView;
                    imageViewEmoji.position = i;
                    ReactionsLayoutInBubble.VisibleReaction visibleReaction = (i < SelectAnimatedEmojiDialog.this.recentReactionsStartRow || i >= SelectAnimatedEmojiDialog.this.recentReactionsEndRow) ? (i < SelectAnimatedEmojiDialog.this.stickersStartRow || i >= SelectAnimatedEmojiDialog.this.stickersEndRow) ? (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.topReactions.get(i - SelectAnimatedEmojiDialog.this.topReactionsStartRow) : (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.stickers.get(i - SelectAnimatedEmojiDialog.this.stickersStartRow) : (ReactionsLayoutInBubble.VisibleReaction) SelectAnimatedEmojiDialog.this.recentReactions.get(i - SelectAnimatedEmojiDialog.this.recentReactionsStartRow);
                    if (SelectAnimatedEmojiDialog.this.type == 13) {
                        imageViewEmoji.notDraw = false;
                        imageViewEmoji.isFirstReactions = true;
                        imageViewEmoji.reaction = visibleReaction;
                        imageViewEmoji.setDrawable(Emoji.getEmojiDrawable(visibleReaction.emojicon));
                        imageViewEmoji.setViewSelected(SelectAnimatedEmojiDialog.this.selectedReactions.contains(visibleReaction), false);
                        return;
                    }
                    imageViewEmoji.createImageReceiver(SelectAnimatedEmojiDialog.this.emojiGridView);
                    imageViewEmoji.isFirstReactions = true;
                    imageViewEmoji.reaction = visibleReaction;
                    imageViewEmoji.setViewSelected(SelectAnimatedEmojiDialog.this.selectedReactions.contains(visibleReaction), false);
                    imageViewEmoji.notDraw = false;
                    if (!visibleReaction.isEffect && visibleReaction.emojicon != null) {
                        imageViewEmoji.isDefaultReaction = true;
                        TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).getReactionsMap().get(visibleReaction.emojicon);
                        if (tLRPC$TL_availableReaction != null) {
                            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.activate_animation, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
                            if (!LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
                                imageViewEmoji.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_firstframe", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                            } else {
                                imageViewEmoji.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60_pcache", ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "30_30_firstframe", null, null, svgThumb, 0L, "tgs", visibleReaction, 0);
                            }
                            MediaDataController.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).preloadImage(imageViewEmoji.preloadEffectImageReceiver, ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation());
                        } else {
                            imageViewEmoji.imageReceiver.clearImage();
                            imageViewEmoji.preloadEffectImageReceiver.clearImage();
                        }
                        imageViewEmoji.span = null;
                        imageViewEmoji.document = null;
                        imageViewEmoji.setDrawable(null);
                        PremiumLockIconView premiumLockIconView = imageViewEmoji.premiumLockIconView;
                        if (premiumLockIconView != null) {
                            premiumLockIconView.setVisibility(8);
                            imageViewEmoji.premiumLockIconView.setImageReceiver(null);
                        }
                    } else {
                        imageViewEmoji.isDefaultReaction = false;
                        imageViewEmoji.span = new AnimatedEmojiSpan(visibleReaction.documentId, (Paint.FontMetricsInt) null);
                        imageViewEmoji.document = null;
                        imageViewEmoji.imageReceiver.clearImage();
                        imageViewEmoji.preloadEffectImageReceiver.clearImage();
                        Drawable drawable = (Drawable) SelectAnimatedEmojiDialog.this.emojiGridView.animatedEmojiDrawables.get(imageViewEmoji.span.getDocumentId());
                        if (drawable == null) {
                            drawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji.span.getDocumentId());
                            SelectAnimatedEmojiDialog.this.emojiGridView.animatedEmojiDrawables.put(imageViewEmoji.span.getDocumentId(), drawable);
                        }
                        imageViewEmoji.setDrawable(drawable);
                    }
                    if (!UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium() && SelectAnimatedEmojiDialog.this.type == 14 && visibleReaction.isEffect && visibleReaction.premium) {
                        imageViewEmoji.createPremiumLockView();
                        imageViewEmoji.premiumLockIconView.setVisibility(0);
                        imageViewEmoji.setEmojicon(null);
                        return;
                    }
                    if (visibleReaction.sticker) {
                        imageViewEmoji.setEmojicon(visibleReaction.emojicon);
                    } else {
                        imageViewEmoji.setEmojicon(null);
                    }
                    PremiumLockIconView premiumLockIconView2 = imageViewEmoji.premiumLockIconView;
                    if (premiumLockIconView2 != null) {
                        premiumLockIconView2.setVisibility(4);
                        return;
                    }
                    return;
                } else {
                    int i3 = 40;
                    if (itemViewType == 4) {
                        EmojiPackExpand emojiPackExpand = (EmojiPackExpand) viewHolder.itemView;
                        int i4 = SelectAnimatedEmojiDialog.this.positionToExpand.get(i);
                        EmojiView.EmojiPack emojiPack3 = (i4 < 0 || i4 >= SelectAnimatedEmojiDialog.this.packs.size()) ? null : (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i4);
                        if (i4 == -1) {
                            SelectAnimatedEmojiDialog.this.recentExpandButton = emojiPackExpand;
                            TextView textView2 = emojiPackExpand.textView;
                            textView2.setText("+" + ((SelectAnimatedEmojiDialog.this.recent.size() - 40) + (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0) + 1));
                            return;
                        } else if (emojiPack3 != null) {
                            if (SelectAnimatedEmojiDialog.this.recentExpandButton == emojiPackExpand) {
                                SelectAnimatedEmojiDialog.this.recentExpandButton = null;
                            }
                            TextView textView3 = emojiPackExpand.textView;
                            textView3.setText("+" + ((emojiPack3.documents.size() - 24) + 1));
                            return;
                        } else if (SelectAnimatedEmojiDialog.this.recentExpandButton == emojiPackExpand) {
                            SelectAnimatedEmojiDialog.this.recentExpandButton = null;
                            return;
                        } else {
                            return;
                        }
                    } else if (itemViewType == 5) {
                        EmojiPackButton emojiPackButton = (EmojiPackButton) viewHolder.itemView;
                        final int i5 = SelectAnimatedEmojiDialog.this.positionToButton.get(i);
                        if (i5 < 0 || i5 >= SelectAnimatedEmojiDialog.this.packs.size() || (emojiPack = (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i5)) == null) {
                            return;
                        }
                        emojiPackButton.set(emojiPack.set.title, (emojiPack.free || UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) ? false : false, emojiPack.installed, new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$Adapter$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                SelectAnimatedEmojiDialog.Adapter.this.lambda$onBindViewHolder$0(emojiPack, i5, view);
                            }
                        });
                        return;
                    } else if (itemViewType == 7 || itemViewType == 9) {
                        return;
                    } else {
                        ImageViewEmoji imageViewEmoji2 = (ImageViewEmoji) viewHolder.itemView;
                        imageViewEmoji2.empty = false;
                        imageViewEmoji2.position = i;
                        imageViewEmoji2.setPadding(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                        if ((SelectAnimatedEmojiDialog.this.type != 4 || !SelectAnimatedEmojiDialog.this.showStickers) && SelectAnimatedEmojiDialog.this.type != 6) {
                            if (SelectAnimatedEmojiDialog.this.type == 4 || SelectAnimatedEmojiDialog.this.type == 3) {
                                size = SelectAnimatedEmojiDialog.this.recent.size();
                            } else {
                                if (SelectAnimatedEmojiDialog.this.recent.size() <= 40 || SelectAnimatedEmojiDialog.this.recentExpanded) {
                                    i3 = SelectAnimatedEmojiDialog.this.recent.size() + (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0);
                                }
                                size = i3;
                            }
                        } else {
                            size = SelectAnimatedEmojiDialog.this.recentStickers.size();
                        }
                        if (SelectAnimatedEmojiDialog.this.includeEmpty) {
                            if (i == (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0) + (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0)) {
                                z = SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(null);
                                imageViewEmoji2.empty = true;
                                imageViewEmoji2.setPadding(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
                                imageViewEmoji2.span = null;
                                imageViewEmoji2.document = null;
                                imageViewEmoji2.isStaticIcon = false;
                                ImageReceiver imageReceiver = imageViewEmoji2.imageReceiver;
                                if (imageReceiver != null) {
                                    imageReceiver.clearImage();
                                }
                                if (imageViewEmoji2.span != null) {
                                    imageViewEmoji2.setDrawable(null);
                                } else {
                                    AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) SelectAnimatedEmojiDialog.this.emojiGridView.animatedEmojiDrawables.get(imageViewEmoji2.span.getDocumentId());
                                    if (animatedEmojiDrawable == null) {
                                        animatedEmojiDrawable = AnimatedEmojiDrawable.make(SelectAnimatedEmojiDialog.this.currentAccount, SelectAnimatedEmojiDialog.this.getCacheType(), imageViewEmoji2.span.getDocumentId());
                                        SelectAnimatedEmojiDialog.this.emojiGridView.animatedEmojiDrawables.put(imageViewEmoji2.span.getDocumentId(), animatedEmojiDrawable);
                                    }
                                    imageViewEmoji2.setDrawable(animatedEmojiDrawable);
                                }
                                imageViewEmoji2.setViewSelected(z, false);
                                return;
                            }
                        }
                        if (SelectAnimatedEmojiDialog.this.type == 13) {
                            if ((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0) < SelectAnimatedEmojiDialog.this.standardEmojis.size()) {
                                String str = (String) SelectAnimatedEmojiDialog.this.standardEmojis.get(((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow == -1 ? 0 : 1)) - (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0));
                                imageViewEmoji2.notDraw = false;
                                imageViewEmoji2.isFirstReactions = false;
                                imageViewEmoji2.reaction = ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(str);
                                imageViewEmoji2.setDrawable(Emoji.getEmojiDrawable(str));
                                imageViewEmoji2.setViewSelected(SelectAnimatedEmojiDialog.this.selectedReactions.contains(imageViewEmoji2.reaction), false);
                                return;
                            }
                        }
                        if ((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0) < size) {
                            int i6 = ((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.includeEmpty ? 1 : 0);
                            if (SelectAnimatedEmojiDialog.this.type != 4 || !SelectAnimatedEmojiDialog.this.showStickers) {
                                if (SelectAnimatedEmojiDialog.this.type == 6) {
                                    TLRPC$Document tLRPC$Document2 = (TLRPC$Document) SelectAnimatedEmojiDialog.this.recentStickers.get(i6);
                                    imageViewEmoji2.setSticker(tLRPC$Document2, SelectAnimatedEmojiDialog.this.emojiGridView);
                                    if (tLRPC$Document2 != null) {
                                    }
                                    z2 = false;
                                } else {
                                    AnimatedEmojiSpan animatedEmojiSpan = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.recent.get(i6);
                                    imageViewEmoji2.span = animatedEmojiSpan;
                                    imageViewEmoji2.document = animatedEmojiSpan == null ? null : animatedEmojiSpan.document;
                                    z2 = (animatedEmojiSpan == null || !SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(Long.valueOf(animatedEmojiSpan.getDocumentId()))) ? false : false;
                                    imageViewEmoji2.isStaticIcon = false;
                                    ImageReceiver imageReceiver2 = imageViewEmoji2.imageReceiver;
                                    if (imageReceiver2 != null) {
                                        imageReceiver2.clearImage();
                                    }
                                }
                                z = z2;
                            } else {
                                imageViewEmoji2.setSticker((TLRPC$Document) SelectAnimatedEmojiDialog.this.recentStickers.get(i6), SelectAnimatedEmojiDialog.this.emojiGridView);
                                z = false;
                            }
                        } else {
                            if (!SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty()) {
                                if ((((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0)) - size) - 1 >= 0) {
                                    if ((((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0)) - size) - 1 < SelectAnimatedEmojiDialog.this.defaultStatuses.size()) {
                                        AnimatedEmojiSpan animatedEmojiSpan2 = (AnimatedEmojiSpan) SelectAnimatedEmojiDialog.this.defaultStatuses.get((((i - (SelectAnimatedEmojiDialog.this.searchRow != -1 ? 1 : 0)) - (SelectAnimatedEmojiDialog.this.longtapHintRow != -1 ? 1 : 0)) - size) - 1);
                                        imageViewEmoji2.span = animatedEmojiSpan2;
                                        imageViewEmoji2.document = animatedEmojiSpan2 == null ? null : animatedEmojiSpan2.document;
                                        z2 = (animatedEmojiSpan2 == null || !SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(Long.valueOf(animatedEmojiSpan2.getDocumentId()))) ? false : false;
                                        imageViewEmoji2.isStaticIcon = false;
                                        ImageReceiver imageReceiver3 = imageViewEmoji2.imageReceiver;
                                        if (imageReceiver3 != null) {
                                            imageReceiver3.clearImage();
                                        }
                                        z = z2;
                                    }
                                }
                            }
                            for (int i7 = 0; i7 < SelectAnimatedEmojiDialog.this.positionToSection.size(); i7++) {
                                int keyAt = SelectAnimatedEmojiDialog.this.positionToSection.keyAt(i7);
                                int i8 = i7 - (!SelectAnimatedEmojiDialog.this.defaultStatuses.isEmpty());
                                EmojiView.EmojiPack emojiPack4 = i8 >= 0 ? (EmojiView.EmojiPack) SelectAnimatedEmojiDialog.this.packs.get(i8) : null;
                                if (emojiPack4 != null) {
                                    int size2 = emojiPack4.expanded ? emojiPack4.documents.size() : Math.min(emojiPack4.documents.size(), 24);
                                    if (i > keyAt && i <= keyAt + 1 + size2 && (tLRPC$Document = emojiPack4.documents.get((i - keyAt) - 1)) != null) {
                                        if (SelectAnimatedEmojiDialog.this.showStickers) {
                                            imageViewEmoji2.setSticker(tLRPC$Document, SelectAnimatedEmojiDialog.this.emojiSearchGridView);
                                        } else {
                                            imageViewEmoji2.isStaticIcon = false;
                                            ImageReceiver imageReceiver4 = imageViewEmoji2.imageReceiver;
                                            if (imageReceiver4 != null) {
                                                imageReceiver4.clearImage();
                                            }
                                            imageViewEmoji2.span = new AnimatedEmojiSpan(tLRPC$Document, (Paint.FontMetricsInt) null);
                                        }
                                        imageViewEmoji2.document = tLRPC$Document;
                                    }
                                }
                            }
                            AnimatedEmojiSpan animatedEmojiSpan3 = imageViewEmoji2.span;
                            if (animatedEmojiSpan3 != null) {
                            }
                            z2 = false;
                            z = z2;
                        }
                        if (imageViewEmoji2.span != null) {
                        }
                        imageViewEmoji2.setViewSelected(z, false);
                        return;
                    }
                }
            }
            ImageViewEmoji imageViewEmoji3 = (ImageViewEmoji) viewHolder.itemView;
            imageViewEmoji3.position = i;
            imageViewEmoji3.selected = SelectAnimatedEmojiDialog.this.selectedDocumentIds.contains(0L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(EmojiView.EmojiPack emojiPack, int i, View view) {
            Integer num;
            View view2;
            int childAdapterPosition;
            if (!emojiPack.free && !UserConfig.getInstance(SelectAnimatedEmojiDialog.this.currentAccount).isPremium()) {
                BaseFragment lastFragment = LaunchActivity.getLastFragment();
                if (lastFragment != null) {
                    lastFragment.showDialog(new PremiumFeatureBottomSheet(SelectAnimatedEmojiDialog.this.baseFragment, SelectAnimatedEmojiDialog.this.getContext(), SelectAnimatedEmojiDialog.this.currentAccount, 11, false));
                    return;
                }
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
            SelectAnimatedEmojiDialog.this.updateRows(true, true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return SelectAnimatedEmojiDialog.this.totalCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return Math.abs(((Long) SelectAnimatedEmojiDialog.this.rowHashCodes.get(i)).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enterAnimationInProgress() {
        ValueAnimator valueAnimator;
        return this.enterAnimationInProgress || ((valueAnimator = this.showAnimator) != null && valueAnimator.isRunning());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class HeaderView extends FrameLayout {
        ImageView closeIcon;
        private LinearLayout layoutView;
        private ValueAnimator lockAnimator;
        private float lockT;
        private RLottieImageView lockView;
        private TextView textView;

        public HeaderView(Context context, boolean z) {
            super(context);
            LinearLayout linearLayout = new LinearLayout(context);
            this.layoutView = linearLayout;
            linearLayout.setOrientation(0);
            addView(this.layoutView, LayoutHelper.createFrame(-2, -2, z ? 3 : 17));
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockView = rLottieImageView;
            rLottieImageView.setAnimation(R.raw.unlock_icon, 20, 20);
            RLottieImageView rLottieImageView2 = this.lockView;
            int i = Theme.key_chat_emojiPanelStickerSetName;
            rLottieImageView2.setColorFilter(Theme.getColor(i, SelectAnimatedEmojiDialog.this.resourcesProvider));
            this.layoutView.addView(this.lockView, LayoutHelper.createLinear(20, 20));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor(i, SelectAnimatedEmojiDialog.this.resourcesProvider));
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
            this.closeIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelStickerSetNameIcon, SelectAnimatedEmojiDialog.this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
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

        /* JADX WARN: Multi-variable type inference failed */
        public void setText(String str, String str2, boolean z) {
            int indexOf;
            if (str != null && str2 != null && (indexOf = str.toLowerCase().indexOf(str2.toLowerCase())) >= 0) {
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_chat_emojiPanelStickerSetNameHighlight, SelectAnimatedEmojiDialog.this.resourcesProvider)), indexOf, str2.length() + indexOf, 33);
                str = spannableString;
            }
            this.textView.setText(str);
            updateLock(z, false);
        }

        public void updateLock(boolean z, boolean z2) {
            ValueAnimator valueAnimator = this.lockAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.lockAnimator = null;
            }
            if (z2) {
                float[] fArr = new float[2];
                fArr[0] = this.lockT;
                fArr[1] = z ? 1.0f : 0.0f;
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
            this.lockT = z ? 1.0f : 0.0f;
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
    /* loaded from: classes4.dex */
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
            AnimatedTextView animatedTextView = new AnimatedTextView(this, getContext(), selectAnimatedEmojiDialog) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.EmojiPackButton.1
                @Override // android.view.View
                public void invalidate() {
                    if (HwEmojis.grab(this)) {
                        return;
                    }
                    super.invalidate();
                }

                @Override // android.view.View
                public void invalidate(int i, int i2, int i3, int i4) {
                    if (HwEmojis.grab(this)) {
                        return;
                    }
                    super.invalidate(i, i2, i3, i4);
                }
            };
            this.addButtonTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.3f, 0L, 250L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.addButtonTextView.setTextSize(AndroidUtilities.dp(14.0f));
            this.addButtonTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.addButtonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, selectAnimatedEmojiDialog.resourcesProvider));
            this.addButtonTextView.setGravity(17);
            FrameLayout frameLayout = new FrameLayout(getContext());
            this.addButtonView = frameLayout;
            frameLayout.setBackground(Theme.AdaptiveRipple.filledRect(Theme.getColor(Theme.key_featuredStickers_addButton, selectAnimatedEmojiDialog.resourcesProvider), 8.0f));
            this.addButtonView.addView(this.addButtonTextView, LayoutHelper.createFrame(-1, -2, 17));
            addView(this.addButtonView, LayoutHelper.createFrame(-1, -1.0f));
            PremiumButtonView premiumButtonView = new PremiumButtonView(getContext(), false, selectAnimatedEmojiDialog.resourcesProvider);
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
            if (z2) {
                float[] fArr = new float[2];
                fArr[0] = this.addButtonView.getAlpha();
                fArr[1] = z ? 0.6f : 1.0f;
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
            this.addButtonView.setAlpha(z ? 0.6f : 1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateInstall$0(ValueAnimator valueAnimator) {
            FrameLayout frameLayout = this.addButtonView;
            if (frameLayout != null) {
                frameLayout.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
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
                if (z2) {
                    this.premiumButtonView.setVisibility(0);
                    float[] fArr = new float[2];
                    fArr[0] = this.lockT;
                    fArr[1] = z ? 1.0f : 0.0f;
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                    this.lockAnimator = ofFloat;
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$EmojiPackButton$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            SelectAnimatedEmojiDialog.EmojiPackButton.this.lambda$updateLock$1(valueAnimator2);
                        }
                    });
                    this.lockAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.EmojiPackButton.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (z) {
                                return;
                            }
                            EmojiPackButton.this.premiumButtonView.setVisibility(8);
                        }
                    });
                    this.lockAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.lockAnimator.setDuration(350L);
                    this.lockAnimator.start();
                    return;
                }
                float f = valueOf.booleanValue() ? 1.0f : 0.0f;
                this.lockT = f;
                this.addButtonView.setAlpha(1.0f - f);
                this.premiumButtonView.setAlpha(this.lockT);
                this.premiumButtonView.setScaleX(this.lockT);
                this.premiumButtonView.setScaleY(this.lockT);
                this.premiumButtonView.setVisibility(this.lockShow.booleanValue() ? 0 : 8);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateLock$1(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.lockT = floatValue;
            FrameLayout frameLayout = this.addButtonView;
            if (frameLayout != null) {
                frameLayout.setAlpha(1.0f - floatValue);
            }
            PremiumButtonView premiumButtonView = this.premiumButtonView;
            if (premiumButtonView != null) {
                premiumButtonView.setAlpha(this.lockT);
            }
        }
    }

    /* loaded from: classes4.dex */
    public class EmojiPackExpand extends FrameLayout {
        public TextView textView;

        public EmojiPackExpand(SelectAnimatedEmojiDialog selectAnimatedEmojiDialog, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextSize(1, 12.0f);
            this.textView.setTextColor(-1);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0f), selectAnimatedEmojiDialog.useAccentForPlus ? Theme.blendOver(selectAnimatedEmojiDialog.accentColor, Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundWhite), 0.4f)) : ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chat_emojiPanelStickerSetName, resourcesProvider), 99)));
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

    /* loaded from: classes4.dex */
    public class ImageViewEmoji extends View {
        private float animatedScale;
        public boolean attached;
        ValueAnimator backAnimator;
        public ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolder;
        public float bigReactionSelectedProgress;
        public TLRPC$Document document;
        public Drawable drawable;
        public Rect drawableBounds;
        Drawable emojiDrawable;
        public boolean empty;
        public ImageReceiver imageReceiver;
        public ImageReceiver imageReceiverToDraw;
        final AnimatedEmojiSpan.InvalidateHolder invalidateHolder;
        public boolean isDefaultReaction;
        public boolean isFirstReactions;
        public boolean isStaticIcon;
        public boolean notDraw;
        public int position;
        public ImageReceiver preloadEffectImageReceiver;
        PremiumLockIconView premiumLockIconView;
        private float pressedProgress;
        public ReactionsLayoutInBubble.VisibleReaction reaction;
        public boolean selected;
        private float selectedProgress;
        private float selectedProgressT;
        private boolean shouldSelected;
        public float skewAlpha;
        public int skewIndex;
        public AnimatedEmojiSpan span;

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            if (HwEmojis.isHwEnabled() || getParent() == null) {
                return;
            }
            ((View) getParent()).invalidate();
        }

        public ImageViewEmoji(Context context) {
            super(context);
            this.empty = false;
            this.notDraw = false;
            this.backgroundThreadDrawHolder = new ImageReceiver.BackgroundThreadDrawHolder[2];
            this.preloadEffectImageReceiver = new ImageReceiver();
            this.animatedScale = 1.0f;
            this.invalidateHolder = new AnimatedEmojiSpan.InvalidateHolder() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$ImageViewEmoji$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.Components.AnimatedEmojiSpan.InvalidateHolder
                public final void invalidate() {
                    SelectAnimatedEmojiDialog.ImageViewEmoji.this.lambda$new$0();
                }
            };
            this.preloadEffectImageReceiver.ignoreNotifications = true;
        }

        public void setAnimatedScale(float f) {
            this.animatedScale = f;
        }

        public float getAnimatedScale() {
            return this.animatedScale;
        }

        @Override // android.view.View
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
                if (z || this.pressedProgress == 0.0f || SelectAnimatedEmojiDialog.this.type == 14) {
                    return;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pressedProgress, 0.0f);
                this.backAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$ImageViewEmoji$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        SelectAnimatedEmojiDialog.ImageViewEmoji.this.lambda$setPressed$1(valueAnimator2);
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
        public /* synthetic */ void lambda$setPressed$1(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
        }

        public void updatePressedProgress() {
            if (!isPressed() || this.pressedProgress == 1.0f || SelectAnimatedEmojiDialog.this.type == 14) {
                return;
            }
            this.pressedProgress = Utilities.clamp(this.pressedProgress + 0.16f, 1.0f, 0.0f);
            invalidate();
        }

        public void update(long j) {
            ImageReceiver imageReceiver = this.imageReceiverToDraw;
            if (imageReceiver != null) {
                if (imageReceiver.getLottieAnimation() != null) {
                    this.imageReceiverToDraw.getLottieAnimation().updateCurrentFrame(j, true);
                }
                if (this.imageReceiverToDraw.getAnimation() != null) {
                    this.imageReceiverToDraw.getAnimation().updateCurrentFrame(j, true);
                }
            }
        }

        private void cancelBackAnimator() {
            ValueAnimator valueAnimator = this.backAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.backAnimator.cancel();
            }
        }

        public void unselectWithScale() {
            if (!this.selected || SelectAnimatedEmojiDialog.this.type == 14) {
                return;
            }
            cancelBackAnimator();
            this.pressedProgress = 1.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.backAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$ImageViewEmoji$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SelectAnimatedEmojiDialog.ImageViewEmoji.this.lambda$unselectWithScale$2(valueAnimator);
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
            setViewSelected(false, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$unselectWithScale$2(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
        }

        public void setViewSelectedWithScale(boolean z, boolean z2) {
            if (!this.selected && z && z2 && SelectAnimatedEmojiDialog.this.type != 14) {
                this.shouldSelected = true;
                this.selectedProgress = 1.0f;
                this.selectedProgressT = 1.0f;
                cancelBackAnimator();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pressedProgress, 1.6f, 0.7f);
                this.backAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$ImageViewEmoji$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SelectAnimatedEmojiDialog.ImageViewEmoji.this.lambda$setViewSelectedWithScale$3(valueAnimator);
                    }
                });
                this.backAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.ImageViewEmoji.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        ImageViewEmoji.this.pressedProgress = 0.0f;
                        ImageViewEmoji imageViewEmoji = ImageViewEmoji.this;
                        imageViewEmoji.backAnimator = null;
                        imageViewEmoji.shouldSelected = false;
                        ImageViewEmoji.this.setViewSelected(true, false);
                    }
                });
                this.backAnimator.setInterpolator(new LinearInterpolator());
                this.backAnimator.setDuration(200L);
                this.backAnimator.start();
                return;
            }
            this.shouldSelected = false;
            setViewSelected(z, z2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setViewSelectedWithScale$3(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
        }

        public void setViewSelected(boolean z, boolean z2) {
            if (this.selected != z) {
                this.selected = z;
                if (z2) {
                    return;
                }
                this.selectedProgressT = z ? 1.0f : 0.0f;
                this.selectedProgress = z ? 1.0f : 0.0f;
            }
        }

        public void drawSelected(Canvas canvas, View view) {
            Paint paint;
            boolean z = this.selected;
            if ((z || this.shouldSelected || this.selectedProgress > 0.0f) && !this.notDraw) {
                if (z || this.shouldSelected) {
                    float f = this.selectedProgressT;
                    if (f < 1.0f) {
                        this.selectedProgressT = f + ((1000.0f / AndroidUtilities.screenRefreshRate) / 240.0f);
                        view.invalidate();
                    }
                }
                if (!this.selected && !this.shouldSelected) {
                    float f2 = this.selectedProgressT;
                    if (f2 > 0.0f) {
                        this.selectedProgressT = f2 - ((1000.0f / AndroidUtilities.screenRefreshRate) / 240.0f);
                        view.invalidate();
                    }
                }
                this.selectedProgress = Utilities.clamp(this.selected ? CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.selectedProgressT) : 1.0f - CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(1.0f - this.selectedProgressT), 1.0f, 0.0f);
                int dp = AndroidUtilities.dp(SelectAnimatedEmojiDialog.this.type == 6 ? 1.5f : 1.0f);
                int dp2 = AndroidUtilities.dp(SelectAnimatedEmojiDialog.this.type == 6 ? 6.0f : 4.0f);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                float f3 = dp;
                rectF.inset(f3, f3);
                if (!this.empty) {
                    Drawable drawable = this.drawable;
                    if (!(drawable instanceof AnimatedEmojiDrawable) || !((AnimatedEmojiDrawable) drawable).canOverrideColor()) {
                        paint = SelectAnimatedEmojiDialog.this.selectorPaint;
                        int alpha = paint.getAlpha();
                        paint.setAlpha((int) (alpha * getAlpha() * this.selectedProgress));
                        float f4 = dp2;
                        canvas.drawRoundRect(rectF, f4, f4, paint);
                        paint.setAlpha(alpha);
                    }
                }
                paint = SelectAnimatedEmojiDialog.this.selectorAccentPaint;
                int alpha2 = paint.getAlpha();
                paint.setAlpha((int) (alpha2 * getAlpha() * this.selectedProgress));
                float f42 = dp2;
                canvas.drawRoundRect(rectF, f42, f42, paint);
                paint.setAlpha(alpha2);
            }
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.attached) {
                return;
            }
            this.attached = true;
            Drawable drawable = this.drawable;
            if (drawable instanceof AnimatedEmojiDrawable) {
                ((AnimatedEmojiDrawable) drawable).addView(this.invalidateHolder);
            }
            ImageReceiver imageReceiver = this.imageReceiver;
            if (imageReceiver != null) {
                imageReceiver.setParentView((View) getParent());
                this.imageReceiver.onAttachedToWindow();
            }
            this.preloadEffectImageReceiver.onAttachedToWindow();
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.attached) {
                this.attached = false;
                Drawable drawable = this.drawable;
                if (drawable instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawable).removeView(this.invalidateHolder);
                    if (((AnimatedEmojiDrawable) this.drawable).getImageReceiver() != null) {
                        ((AnimatedEmojiDrawable) this.drawable).getImageReceiver().setEmojiPaused(false);
                    }
                }
                ImageReceiver imageReceiver = this.imageReceiver;
                if (imageReceiver != null) {
                    imageReceiver.onDetachedFromWindow();
                    this.imageReceiver.setEmojiPaused(false);
                }
                this.preloadEffectImageReceiver.onDetachedFromWindow();
            }
        }

        public void setDrawable(Drawable drawable) {
            Drawable drawable2 = this.drawable;
            if (drawable2 != drawable) {
                if (this.attached && drawable2 != null && (drawable2 instanceof AnimatedEmojiDrawable)) {
                    ((AnimatedEmojiDrawable) drawable2).removeView(this.invalidateHolder);
                }
                this.drawable = drawable;
                if (this.attached && (drawable instanceof AnimatedEmojiDrawable)) {
                    ((AnimatedEmojiDrawable) drawable).addView(this.invalidateHolder);
                }
            }
        }

        public void setSticker(TLRPC$Document tLRPC$Document, View view) {
            this.document = tLRPC$Document;
            createImageReceiver(view);
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
            if (SelectAnimatedEmojiDialog.this.type == 6) {
                this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), !LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD) ? "34_34_firstframe" : "34_34", null, null, svgThumb, tLRPC$Document.size, null, tLRPC$Document, 0);
            } else {
                this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), "100_100_firstframe", null, null, svgThumb, 0L, "tgs", tLRPC$Document, 0);
            }
            this.isStaticIcon = true;
            this.span = null;
        }

        public void createImageReceiver(View view) {
            if (this.imageReceiver == null) {
                ImageReceiver imageReceiver = new ImageReceiver(view);
                this.imageReceiver = imageReceiver;
                imageReceiver.setLayerNum(7);
                if (this.attached) {
                    this.imageReceiver.onAttachedToWindow();
                }
                this.imageReceiver.setAspectFit(true);
            }
        }

        @Override // android.view.View
        public void invalidate() {
            if (HwEmojis.isHwEnabled() || getParent() == null) {
                return;
            }
            ((View) getParent()).invalidate();
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            if (HwEmojis.isHwEnabled()) {
                return;
            }
            super.invalidate(i, i2, i3, i4);
        }

        public void createPremiumLockView() {
            PremiumLockIconView premiumLockIconView = this.premiumLockIconView;
            if (premiumLockIconView == null) {
                this.premiumLockIconView = new PremiumLockIconView(getContext(), PremiumLockIconView.TYPE_REACTIONS_LOCK) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.ImageViewEmoji.4
                    @Override // android.view.View
                    public void invalidate() {
                        super.invalidate();
                        if (ImageViewEmoji.this.getParent() instanceof View) {
                            ((View) ImageViewEmoji.this.getParent()).invalidate();
                        }
                    }
                };
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.66f), 1073741824);
                this.premiumLockIconView.measure(makeMeasureSpec, makeMeasureSpec);
                PremiumLockIconView premiumLockIconView2 = this.premiumLockIconView;
                premiumLockIconView2.layout(0, 0, premiumLockIconView2.getMeasuredWidth(), this.premiumLockIconView.getMeasuredHeight());
                return;
            }
            premiumLockIconView.resetColor();
        }

        public void setEmojicon(String str) {
            if (TextUtils.isEmpty(str)) {
                this.emojiDrawable = null;
            } else {
                this.emojiDrawable = Emoji.getEmojiDrawable(str);
            }
        }
    }

    public void onEmojiClick(final View view, final AnimatedEmojiSpan animatedEmojiSpan) {
        int i;
        incrementHintUse();
        if (animatedEmojiSpan == null || (((i = this.type) == 0 || i == 12 || i == 9 || i == 10) && this.selectedDocumentIds.contains(Long.valueOf(animatedEmojiSpan.documentId)))) {
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
            int i2 = this.type;
            if (i2 == 0 || i2 == 12 || i2 == 9 || i2 == 10) {
                MediaDataController.getInstance(this.currentAccount).pushRecentEmojiStatus(tLRPC$TL_emojiStatus);
            }
            int i3 = this.type;
            if (i3 == 0 || i3 == 12 || i3 == 9 || i3 == 10 || i3 == 2) {
                animateEmojiSelect((ImageViewEmoji) view, new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.this.lambda$onEmojiClick$30(view, animatedEmojiSpan, tLRPC$Document);
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
    public /* synthetic */ void lambda$onEmojiClick$30(View view, AnimatedEmojiSpan animatedEmojiSpan, TLRPC$Document tLRPC$Document) {
        onEmojiSelected(view, Long.valueOf(animatedEmojiSpan.documentId), tLRPC$Document, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementHintUse() {
        if (this.type == 2) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("emoji");
        int i = this.type;
        sb.append((i == 0 || i == 12 || i == 9 || i == 10) ? "status" : "reaction");
        sb.append("usehint");
        String sb2 = sb.toString();
        int i2 = MessagesController.getGlobalMainSettings().getInt(sb2, 0);
        if (i2 <= 3) {
            MessagesController.getGlobalMainSettings().edit().putInt(sb2, i2 + 1).apply();
        }
    }

    public void preload(int i, int i2) {
        if (MediaDataController.getInstance(i2) == null) {
            return;
        }
        MediaDataController.getInstance(i2).checkStickers(5);
        if (i == 14) {
            MessagesController.getInstance(this.currentAccount).getAvailableEffects();
        } else if (i == 1 || i == 11 || i == 2 || i == 6 || i == 13) {
            MediaDataController.getInstance(i2).checkReactions();
        } else if (i == 9 || i == 10) {
            if (MessagesController.getInstance(i2).getMainSettings().getBoolean("resetemojipacks", true)) {
                MediaDataController.getInstance(i2).loadStickers(5, false, false);
                MessagesController.getInstance(i2).getMainSettings().edit().putBoolean("resetemojipacks", false).commit();
            }
            MediaDataController.getInstance(i2).fetchEmojiStatuses(2, false);
            MediaDataController.getInstance(i2).loadRestrictedStatusEmojis();
            MediaDataController.getInstance(i2).getStickerSet(new TLRPC$TL_inputStickerSetEmojiChannelDefaultStatuses(), false);
        } else if (i == 0 || i == 12) {
            MediaDataController.getInstance(i2).fetchEmojiStatuses(0, true);
            MediaDataController.getInstance(i2).getStickerSet(new TLRPC$TL_inputStickerSetEmojiDefaultStatuses(), false);
        } else if (i == 3) {
            MediaDataController.getInstance(i2).checkDefaultTopicIcons();
        } else if (i == 4) {
            MediaDataController.getInstance(i2).loadRecents(0, false, true, false);
            MediaDataController.getInstance(i2).checkStickers(0);
        }
    }

    public static void preload(int i) {
        if (preloaded[i] || MediaDataController.getInstance(i) == null) {
            return;
        }
        preloaded[i] = true;
        MediaDataController.getInstance(i).checkStickers(5);
        MediaDataController.getInstance(i).fetchEmojiStatuses(0, true);
        MediaDataController.getInstance(i).checkReactions();
        MediaDataController.getInstance(i).getStickerSet(new TLRPC$TL_inputStickerSetEmojiDefaultStatuses(), false);
        MediaDataController.getInstance(i).getDefaultEmojiStatuses();
        MediaDataController.getInstance(i).checkDefaultTopicIcons();
        StickerCategoriesListView.preload(i, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows(boolean z, boolean z2) {
        updateRows(z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:173:0x03fb A[LOOP:2: B:171:0x03f3->B:173:0x03fb, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0449  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0462  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x04a9  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0503 A[LOOP:5: B:210:0x04fd->B:212:0x0503, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0906  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0a13 A[LOOP:13: B:416:0x0a0b->B:418:0x0a13, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:457:0x045c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRows(boolean z, boolean z2, boolean z3) {
        int i;
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList;
        ArrayList<Long> arrayList2;
        TLRPC$TL_emojiList tLRPC$TL_emojiList;
        HashSet<Long> hashSet;
        ArrayList arrayList3;
        ArrayList<TLRPC$EmojiStatus> defaultEmojiStatuses;
        boolean z4;
        int i2;
        boolean z5;
        int i3;
        int i4;
        boolean z6;
        ArrayList<TLRPC$Document> arrayList4;
        boolean isPremiumEmojiPack;
        int i5;
        int i6;
        ArrayList arrayList5;
        EmojiView.EmojiPack emojiPack;
        TLRPC$StickerSet tLRPC$StickerSet;
        int i7;
        long j;
        int i8;
        HashSet<Long> hashSet2;
        int i9;
        int i10;
        String[][] strArr;
        String str;
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList6;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        boolean z7;
        int i16;
        int i17;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet;
        ArrayList<Long> arrayList7;
        boolean z8 = !this.animationsEnabled ? false : z2;
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        if (mediaDataController == null) {
            return;
        }
        if (z || this.frozenEmojiPacks == null) {
            this.frozenEmojiPacks = new ArrayList<>(mediaDataController.getStickerSets(this.showStickers ? 0 : 5));
        }
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList8 = this.frozenEmojiPacks;
        ArrayList arrayList9 = new ArrayList(mediaDataController.getFeaturedEmojiSets());
        ArrayList arrayList10 = new ArrayList(this.rowHashCodes);
        this.totalCount = 0;
        this.recentReactionsSectionRow = -1;
        this.recentReactionsStartRow = -1;
        this.recentReactionsEndRow = -1;
        this.popularSectionRow = -1;
        this.longtapHintRow = -1;
        this.defaultTopicIconRow = -1;
        this.topicEmojiHeaderRow = -1;
        this.stickersSectionRow = -1;
        this.stickersStartRow = -1;
        this.stickersEndRow = -1;
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
        this.stickerSets.clear();
        this.recentStickers.clear();
        this.standardEmojis.clear();
        if ((!arrayList8.isEmpty() || this.type == 4) && (i = this.type) != 5 && i != 7 && i != 6 && i != 8) {
            int i18 = this.totalCount;
            this.totalCount = i18 + 1;
            this.searchRow = i18;
            this.rowHashCodes.add(9L);
        } else {
            this.searchRow = -1;
        }
        int i19 = this.type;
        if (i19 == 5 || i19 == 7) {
            arrayList = arrayList8;
            if (this.includeEmpty) {
                this.totalCount++;
                this.rowHashCodes.add(2L);
            }
            TLRPC$TL_emojiList tLRPC$TL_emojiList2 = MediaDataController.getInstance(this.currentAccount).replyIconsDefault;
            if (tLRPC$TL_emojiList2 != null && (arrayList2 = tLRPC$TL_emojiList2.document_id) != null && !arrayList2.isEmpty()) {
                for (int i20 = 0; i20 < tLRPC$TL_emojiList2.document_id.size(); i20++) {
                    this.recent.add(new AnimatedEmojiSpan(tLRPC$TL_emojiList2.document_id.get(i20).longValue(), (Paint.FontMetricsInt) null));
                }
                for (int i21 = 0; i21 < this.recent.size(); i21++) {
                    this.rowHashCodes.add(Long.valueOf((this.recent.get(i21).getDocumentId() * 13) + 43223));
                    this.totalCount++;
                }
            }
        } else if (i19 != 4) {
            arrayList = arrayList8;
            if (i19 == 6) {
                if (this.includeEmpty) {
                    this.totalCount++;
                    this.rowHashCodes.add(2L);
                }
                List<TLRPC$TL_availableReaction> enabledReactionsList = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
                for (int i22 = 0; i22 < enabledReactionsList.size(); i22++) {
                    this.recentStickers.add(enabledReactionsList.get(i22).activate_animation);
                }
                for (int i23 = 0; i23 < this.recentStickers.size(); i23++) {
                    this.rowHashCodes.add(Long.valueOf((this.recentStickers.get(i23).id * 13) + 62425));
                    this.totalCount++;
                }
            } else if (i19 == 3) {
                int i24 = this.totalCount;
                this.totalCount = i24 + 1;
                this.topicEmojiHeaderRow = i24;
                this.rowHashCodes.add(12L);
                int i25 = this.totalCount;
                this.totalCount = i25 + 1;
                this.defaultTopicIconRow = i25;
                this.rowHashCodes.add(7L);
                String str2 = UserConfig.getInstance(this.currentAccount).defaultTopicIcons;
                if (str2 != null) {
                    TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str2);
                    tLRPC$TL_messages_stickerSet = stickerSetByName == null ? MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str2) : stickerSetByName;
                } else {
                    tLRPC$TL_messages_stickerSet = null;
                }
                if (tLRPC$TL_messages_stickerSet != null) {
                    if (this.includeEmpty) {
                        this.totalCount++;
                        this.rowHashCodes.add(2L);
                    }
                    ArrayList<TLRPC$Document> arrayList11 = tLRPC$TL_messages_stickerSet.documents;
                    if (arrayList11 != null && !arrayList11.isEmpty()) {
                        for (int i26 = 0; i26 < tLRPC$TL_messages_stickerSet.documents.size(); i26++) {
                            this.recent.add(new AnimatedEmojiSpan(tLRPC$TL_messages_stickerSet.documents.get(i26), (Paint.FontMetricsInt) null));
                        }
                    }
                    for (int i27 = 0; i27 < this.recent.size(); i27++) {
                        this.rowHashCodes.add(Long.valueOf((this.recent.get(i27).getDocumentId() * 13) + 43223));
                        this.totalCount++;
                    }
                }
            }
        } else if (this.showStickers) {
            this.recentStickers.addAll(MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(0));
            int i28 = 0;
            while (i28 < this.recentStickers.size()) {
                this.rowHashCodes.add(Long.valueOf((this.recentStickers.get(i28).id * 13) + 62425));
                this.totalCount++;
                i28++;
                arrayList8 = arrayList8;
            }
            arrayList = arrayList8;
        } else {
            arrayList = arrayList8;
            TLRPC$TL_emojiList tLRPC$TL_emojiList3 = this.forUser ? MediaDataController.getInstance(this.currentAccount).profileAvatarConstructorDefault : MediaDataController.getInstance(this.currentAccount).groupAvatarConstructorDefault;
            if (tLRPC$TL_emojiList3 != null && (arrayList7 = tLRPC$TL_emojiList3.document_id) != null && !arrayList7.isEmpty()) {
                for (int i29 = 0; i29 < tLRPC$TL_emojiList3.document_id.size(); i29++) {
                    this.recent.add(new AnimatedEmojiSpan(tLRPC$TL_emojiList3.document_id.get(i29).longValue(), (Paint.FontMetricsInt) null));
                }
                for (int i30 = 0; i30 < this.recent.size(); i30++) {
                    this.rowHashCodes.add(Long.valueOf((this.recent.get(i30).getDocumentId() * 13) + 43223));
                    this.totalCount++;
                }
            }
        }
        if (this.includeHint && (i17 = this.type) != 13 && i17 != 2 && i17 != 11 && i17 != 3 && i17 != 6 && i17 != 8 && i17 != 4 && i17 != 5 && i17 != 7) {
            int i31 = this.totalCount;
            this.totalCount = i31 + 1;
            this.longtapHintRow = i31;
            this.rowHashCodes.add(6L);
        }
        int i32 = this.type;
        if ((i32 == 9 || i32 == 10) && (tLRPC$TL_emojiList = MediaDataController.getInstance(this.currentAccount).restrictedStatusEmojis) != null) {
            hashSet = new HashSet<>();
            hashSet.addAll(tLRPC$TL_emojiList.document_id);
        } else {
            hashSet = null;
        }
        if (this.recentReactionsToSet != null) {
            this.topReactionsStartRow = this.totalCount;
            ArrayList arrayList12 = new ArrayList(this.recentReactionsToSet);
            if (this.type == 13 && arrayList12.size() > 8) {
                arrayList12.removeAll(arrayList12.subList(8, arrayList12.size()));
            } else if (this.type == 14) {
                arrayList6 = new ArrayList<>();
                int i33 = 0;
                while (i33 < arrayList12.size()) {
                    if (((ReactionsLayoutInBubble.VisibleReaction) arrayList12.get(i33)).sticker) {
                        arrayList6.add((ReactionsLayoutInBubble.VisibleReaction) arrayList12.remove(i33));
                        i33--;
                    }
                    i33++;
                }
                i11 = this.type;
                if (i11 != 8 || i11 == 11 || i11 == 13) {
                    this.topReactions.addAll(arrayList12);
                } else {
                    for (int i34 = 0; i34 < 16; i34++) {
                        if (!arrayList12.isEmpty()) {
                            this.topReactions.add((ReactionsLayoutInBubble.VisibleReaction) arrayList12.remove(0));
                        }
                    }
                }
                i12 = 0;
                while (i12 < this.topReactions.size()) {
                    this.rowHashCodes.add(Long.valueOf((this.topReactions.get(i12).hashCode() * 13) - 5632));
                    i12++;
                    arrayList9 = arrayList9;
                }
                arrayList3 = arrayList9;
                int size = this.totalCount + this.topReactions.size();
                this.totalCount = size;
                this.topReactionsEndRow = size;
                if (!arrayList12.isEmpty() && (i14 = this.type) != 8 && i14 != 11 && i14 != 13) {
                    i15 = 0;
                    while (true) {
                        if (i15 < arrayList12.size()) {
                            z7 = true;
                            break;
                        } else if (((ReactionsLayoutInBubble.VisibleReaction) arrayList12.get(i15)).documentId != 0) {
                            z7 = false;
                            break;
                        } else {
                            i15++;
                        }
                    }
                    if (this.type != 14) {
                        if (z7) {
                            if (UserConfig.getInstance(this.currentAccount).isPremium()) {
                                int i35 = this.totalCount;
                                this.totalCount = i35 + 1;
                                this.popularSectionRow = i35;
                                this.rowHashCodes.add(5L);
                            }
                        } else {
                            int i36 = this.totalCount;
                            this.totalCount = i36 + 1;
                            this.recentReactionsSectionRow = i36;
                            this.rowHashCodes.add(4L);
                        }
                    }
                    this.recentReactionsStartRow = this.totalCount;
                    this.recentReactions.addAll(arrayList12);
                    for (i16 = 0; i16 < this.recentReactions.size(); i16++) {
                        this.rowHashCodes.add(Long.valueOf((z7 ? 4235 : -3142) + (this.recentReactions.get(i16).hash * 13)));
                    }
                    int size2 = this.totalCount + this.recentReactions.size();
                    this.totalCount = size2;
                    this.recentReactionsEndRow = size2;
                }
                if (arrayList6 != null && !arrayList6.isEmpty()) {
                    int i37 = this.totalCount;
                    this.totalCount = i37 + 1;
                    this.stickersSectionRow = i37;
                    this.rowHashCodes.add(8L);
                    this.stickersStartRow = this.totalCount;
                    this.stickers = arrayList6;
                    for (i13 = 0; i13 < arrayList6.size(); i13++) {
                        this.rowHashCodes.add(Long.valueOf(arrayList6.get(i13).effectId * 19));
                    }
                    int size3 = this.totalCount + arrayList6.size();
                    this.totalCount = size3;
                    this.stickersEndRow = size3;
                }
            }
            arrayList6 = null;
            i11 = this.type;
            if (i11 != 8) {
            }
            this.topReactions.addAll(arrayList12);
            i12 = 0;
            while (i12 < this.topReactions.size()) {
            }
            arrayList3 = arrayList9;
            int size4 = this.totalCount + this.topReactions.size();
            this.totalCount = size4;
            this.topReactionsEndRow = size4;
            if (!arrayList12.isEmpty()) {
                i15 = 0;
                while (true) {
                    if (i15 < arrayList12.size()) {
                    }
                    i15++;
                }
                if (this.type != 14) {
                }
                this.recentReactionsStartRow = this.totalCount;
                this.recentReactions.addAll(arrayList12);
                while (i16 < this.recentReactions.size()) {
                }
                int size22 = this.totalCount + this.recentReactions.size();
                this.totalCount = size22;
                this.recentReactionsEndRow = size22;
            }
            if (arrayList6 != null) {
                int i372 = this.totalCount;
                this.totalCount = i372 + 1;
                this.stickersSectionRow = i372;
                this.rowHashCodes.add(8L);
                this.stickersStartRow = this.totalCount;
                this.stickers = arrayList6;
                while (i13 < arrayList6.size()) {
                }
                int size32 = this.totalCount + arrayList6.size();
                this.totalCount = size32;
                this.stickersEndRow = size32;
            }
        } else {
            arrayList3 = arrayList9;
            int i38 = this.type;
            if (i38 == 0 || i38 == 12 || i38 == 9 || i38 == 10) {
                ArrayList<TLRPC$EmojiStatus> recentEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getRecentEmojiStatuses();
                MediaDataController mediaDataController2 = MediaDataController.getInstance(this.currentAccount);
                int i39 = this.type;
                TLRPC$TL_messages_stickerSet stickerSet = mediaDataController2.getStickerSet((i39 == 0 || i39 == 12) ? new TLRPC$TL_inputStickerSetEmojiDefaultStatuses() : new TLRPC$TL_inputStickerSetEmojiChannelDefaultStatuses(), true);
                if (stickerSet != null) {
                    if (this.includeEmpty) {
                        this.totalCount++;
                        this.rowHashCodes.add(2L);
                    }
                    int i40 = this.type;
                    if (i40 == 0 || i40 == 12) {
                        defaultEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getDefaultEmojiStatuses();
                    } else {
                        defaultEmojiStatuses = MediaDataController.getInstance(this.currentAccount).getDefaultChannelEmojiStatuses();
                    }
                    ArrayList<TLRPC$Document> arrayList13 = stickerSet.documents;
                    if (arrayList13 != null && !arrayList13.isEmpty()) {
                        for (int i41 = 0; i41 < Math.min(7, stickerSet.documents.size()); i41++) {
                            this.recent.add(new AnimatedEmojiSpan(stickerSet.documents.get(i41), (Paint.FontMetricsInt) null));
                            if (this.recent.size() + (this.includeEmpty ? 1 : 0) >= 104) {
                                break;
                            }
                        }
                    }
                    int i42 = this.type;
                    if ((i42 == 0 || i42 == 12) && recentEmojiStatuses != null && !recentEmojiStatuses.isEmpty()) {
                        Iterator<TLRPC$EmojiStatus> it = recentEmojiStatuses.iterator();
                        while (it.hasNext()) {
                            Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(it.next());
                            if (emojiStatusDocumentId != null) {
                                int i43 = 0;
                                while (true) {
                                    if (i43 >= this.recent.size()) {
                                        z4 = false;
                                        break;
                                    } else if (this.recent.get(i43).getDocumentId() == emojiStatusDocumentId.longValue()) {
                                        z4 = true;
                                        break;
                                    } else {
                                        i43++;
                                    }
                                }
                                if (!z4) {
                                    this.recent.add(new AnimatedEmojiSpan(emojiStatusDocumentId.longValue(), (Paint.FontMetricsInt) null));
                                    if (this.recent.size() + (this.includeEmpty ? 1 : 0) >= 104) {
                                        break;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    if (defaultEmojiStatuses != null && !defaultEmojiStatuses.isEmpty()) {
                        Iterator<TLRPC$EmojiStatus> it2 = defaultEmojiStatuses.iterator();
                        while (it2.hasNext()) {
                            Long emojiStatusDocumentId2 = UserObject.getEmojiStatusDocumentId(it2.next());
                            if (emojiStatusDocumentId2 != null) {
                                int i44 = 0;
                                while (true) {
                                    if (i44 >= this.recent.size()) {
                                        z5 = false;
                                        break;
                                    } else if (this.recent.get(i44).getDocumentId() == emojiStatusDocumentId2.longValue()) {
                                        z5 = true;
                                        break;
                                    } else {
                                        i44++;
                                    }
                                }
                                if (!z5) {
                                    this.recent.add(new AnimatedEmojiSpan(emojiStatusDocumentId2.longValue(), (Paint.FontMetricsInt) null));
                                    if (this.recent.size() + (this.includeEmpty ? 1 : 0) >= 104) {
                                        break;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    int i45 = 40 - (this.includeEmpty ? 1 : 0);
                    if (this.recent.size() > i45 && !this.recentExpanded) {
                        for (int i46 = 0; i46 < i45 - 1; i46++) {
                            this.rowHashCodes.add(Long.valueOf((this.recent.get(i46).getDocumentId() * 13) + 43223));
                            this.totalCount++;
                        }
                        this.rowHashCodes.add(Long.valueOf(((((this.recent.size() - 40) + (this.includeEmpty ? 1 : 0)) + 1) * 13) - 5531));
                        EmojiPackExpand emojiPackExpand = this.recentExpandButton;
                        if (emojiPackExpand != null) {
                            TextView textView = emojiPackExpand.textView;
                            StringBuilder sb = new StringBuilder();
                            sb.append("+");
                            i2 = 1;
                            sb.append((this.recent.size() - 40) + (this.includeEmpty ? 1 : 0) + 1);
                            textView.setText(sb.toString());
                        } else {
                            i2 = 1;
                        }
                        this.positionToExpand.put(this.totalCount, -1);
                        this.totalCount += i2;
                    } else {
                        for (int i47 = 0; i47 < this.recent.size(); i47++) {
                            this.rowHashCodes.add(Long.valueOf((this.recent.get(i47).getDocumentId() * 13) + 43223));
                            this.totalCount++;
                        }
                    }
                }
            }
        }
        if (this.type == 13) {
            for (String[] strArr2 : EmojiData.dataColored) {
                int length = strArr2.length;
                int i48 = 0;
                while (i48 < length) {
                    this.standardEmojis.add(strArr2[i48]);
                    this.rowHashCodes.add(Long.valueOf((str.hashCode() * 322) + 13334));
                    this.totalCount++;
                    i48++;
                    hashSet = hashSet;
                }
            }
        }
        HashSet<Long> hashSet3 = hashSet;
        int i49 = this.type;
        if (i49 != 8 && i49 != 13 && i49 != 14) {
            int i50 = 0;
            while (i50 < arrayList.size()) {
                ArrayList<TLRPC$TL_messages_stickerSet> arrayList14 = arrayList;
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2 = arrayList14.get(i50);
                if (tLRPC$TL_messages_stickerSet2 != null && tLRPC$TL_messages_stickerSet2.set != null && ((((i9 = this.type) != 5 && i9 != 7) || MessageObject.isTextColorSet(tLRPC$TL_messages_stickerSet2)) && (((i10 = this.type) != 10 && i10 != 9) || tLRPC$TL_messages_stickerSet2.set.channel_emoji_status))) {
                    TLRPC$StickerSet tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet2.set;
                    if ((tLRPC$StickerSet2.emojis || this.showStickers) && !this.installedEmojiSets.contains(Long.valueOf(tLRPC$StickerSet2.id))) {
                        this.positionToSection.put(this.totalCount, this.packs.size());
                        this.sectionToPosition.put(this.packs.size(), this.totalCount);
                        this.totalCount++;
                        this.rowHashCodes.add(Long.valueOf((tLRPC$TL_messages_stickerSet2.set.id * 13) + 9211));
                        EmojiView.EmojiPack emojiPack2 = new EmojiView.EmojiPack();
                        emojiPack2.installed = true;
                        emojiPack2.featured = false;
                        emojiPack2.expanded = true;
                        emojiPack2.free = !MessageObject.isPremiumEmojiPack(tLRPC$TL_messages_stickerSet2);
                        emojiPack2.set = tLRPC$TL_messages_stickerSet2.set;
                        hashSet2 = hashSet3;
                        emojiPack2.documents = filter(tLRPC$TL_messages_stickerSet2.documents, hashSet2);
                        this.packs.size();
                        this.packs.add(emojiPack2);
                        this.totalCount += emojiPack2.documents.size();
                        for (int i51 = 0; i51 < emojiPack2.documents.size(); i51++) {
                            this.rowHashCodes.add(Long.valueOf((emojiPack2.documents.get(i51).id * 13) + 3212));
                        }
                        i50++;
                        arrayList = arrayList14;
                        hashSet3 = hashSet2;
                    }
                }
                hashSet2 = hashSet3;
                i50++;
                arrayList = arrayList14;
                hashSet3 = hashSet2;
            }
        }
        HashSet<Long> hashSet4 = hashSet3;
        if (!this.showStickers && (i4 = this.type) != 8 && i4 != 13 && i4 != 14) {
            int i52 = 0;
            while (i52 < arrayList3.size()) {
                ArrayList arrayList15 = arrayList3;
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) arrayList15.get(i52);
                TLRPC$StickerSet tLRPC$StickerSet3 = tLRPC$StickerSetCovered.set;
                int i53 = 0;
                while (true) {
                    if (i53 >= this.packs.size()) {
                        z6 = false;
                        break;
                    } else if (this.packs.get(i53).set.id == tLRPC$StickerSet3.id) {
                        z6 = true;
                        break;
                    } else {
                        i53++;
                    }
                }
                if (!z6) {
                    if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetNoCovered) {
                        TLRPC$TL_messages_stickerSet stickerSet2 = mediaDataController.getStickerSet(MediaDataController.getInputStickerSet(tLRPC$StickerSetCovered.set), Integer.valueOf(tLRPC$StickerSetCovered.set.hash), true);
                        if (stickerSet2 != null) {
                            ArrayList<TLRPC$Document> arrayList16 = stickerSet2.documents;
                            isPremiumEmojiPack = MessageObject.isPremiumEmojiPack(stickerSet2);
                            arrayList4 = arrayList16;
                            if (arrayList4 != null && ((((i5 = this.type) != 5 && i5 != 7) || (!arrayList4.isEmpty() && MessageObject.isTextColorEmoji(arrayList4.get(0)))) && (((i6 = this.type) != 10 && i6 != 9) || tLRPC$StickerSet3.channel_emoji_status))) {
                                this.positionToSection.put(this.totalCount, this.packs.size());
                                this.sectionToPosition.put(this.packs.size(), this.totalCount);
                                this.totalCount++;
                                arrayList5 = arrayList10;
                                this.rowHashCodes.add(Long.valueOf((tLRPC$StickerSet3.id * 13) + 9211));
                                emojiPack = new EmojiView.EmojiPack();
                                emojiPack.installed = this.installedEmojiSets.contains(Long.valueOf(tLRPC$StickerSet3.id));
                                emojiPack.featured = true;
                                emojiPack.free = !isPremiumEmojiPack;
                                emojiPack.set = tLRPC$StickerSet3;
                                emojiPack.documents = filter(arrayList4, hashSet4);
                                this.packs.size();
                                emojiPack.expanded = this.expandedEmojiSets.contains(Long.valueOf(emojiPack.set.id));
                                if (emojiPack.documents.size() <= 24 && !emojiPack.expanded) {
                                    this.totalCount += 24;
                                    for (int i54 = 0; i54 < 23; i54++) {
                                        this.rowHashCodes.add(Long.valueOf((emojiPack.documents.get(i54).id * 13) + 3212));
                                    }
                                    tLRPC$StickerSet = tLRPC$StickerSet3;
                                    this.rowHashCodes.add(Long.valueOf(((tLRPC$StickerSet3.id * 13) - 5531) + (((emojiPack.documents.size() - 24) + 1) * 169)));
                                    this.positionToExpand.put(this.totalCount - 1, this.packs.size());
                                } else {
                                    tLRPC$StickerSet = tLRPC$StickerSet3;
                                    this.totalCount += emojiPack.documents.size();
                                    for (i7 = 0; i7 < emojiPack.documents.size(); i7++) {
                                        this.rowHashCodes.add(Long.valueOf((emojiPack.documents.get(i7).id * 13) + 3212));
                                    }
                                }
                                j = 3212;
                                if (emojiPack.installed && (i8 = this.type) != 4 && i8 != 5 && i8 != 7) {
                                    if (i8 != 6) {
                                        this.positionToButton.put(this.totalCount, this.packs.size());
                                        this.totalCount++;
                                        this.rowHashCodes.add(Long.valueOf((tLRPC$StickerSet.id * 13) + 3321));
                                        this.packs.add(emojiPack);
                                        i52++;
                                        arrayList10 = arrayList5;
                                        arrayList3 = arrayList15;
                                    }
                                }
                                this.packs.add(emojiPack);
                                i52++;
                                arrayList10 = arrayList5;
                                arrayList3 = arrayList15;
                            }
                        }
                        isPremiumEmojiPack = false;
                        arrayList4 = null;
                        if (arrayList4 != null) {
                            this.positionToSection.put(this.totalCount, this.packs.size());
                            this.sectionToPosition.put(this.packs.size(), this.totalCount);
                            this.totalCount++;
                            arrayList5 = arrayList10;
                            this.rowHashCodes.add(Long.valueOf((tLRPC$StickerSet3.id * 13) + 9211));
                            emojiPack = new EmojiView.EmojiPack();
                            emojiPack.installed = this.installedEmojiSets.contains(Long.valueOf(tLRPC$StickerSet3.id));
                            emojiPack.featured = true;
                            emojiPack.free = !isPremiumEmojiPack;
                            emojiPack.set = tLRPC$StickerSet3;
                            emojiPack.documents = filter(arrayList4, hashSet4);
                            this.packs.size();
                            emojiPack.expanded = this.expandedEmojiSets.contains(Long.valueOf(emojiPack.set.id));
                            if (emojiPack.documents.size() <= 24) {
                            }
                            tLRPC$StickerSet = tLRPC$StickerSet3;
                            this.totalCount += emojiPack.documents.size();
                            while (i7 < emojiPack.documents.size()) {
                            }
                            j = 3212;
                            if (emojiPack.installed) {
                            }
                            this.packs.add(emojiPack);
                            i52++;
                            arrayList10 = arrayList5;
                            arrayList3 = arrayList15;
                        }
                    } else {
                        if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
                            arrayList4 = ((TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered).documents;
                            isPremiumEmojiPack = MessageObject.isPremiumEmojiPack(tLRPC$StickerSetCovered);
                            if (arrayList4 != null) {
                            }
                        }
                        isPremiumEmojiPack = false;
                        arrayList4 = null;
                        if (arrayList4 != null) {
                        }
                    }
                }
                arrayList5 = arrayList10;
                j = 3212;
                i52++;
                arrayList10 = arrayList5;
                arrayList3 = arrayList15;
            }
        }
        final ArrayList arrayList17 = arrayList10;
        int i55 = this.type;
        if (i55 != 14 && i55 != 8 && i55 != 13) {
            this.emojiTabs.updateEmojiPacks(this.packs);
        }
        if (z8) {
            this.emojiGridView.setItemAnimator(this.emojiItemAnimator);
        } else {
            this.emojiGridView.setItemAnimator(null);
        }
        if (z3) {
            i3 = 0;
            DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.24
                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areContentsTheSame(int i56, int i57) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getOldListSize() {
                    return arrayList17.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getNewListSize() {
                    return SelectAnimatedEmojiDialog.this.rowHashCodes.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areItemsTheSame(int i56, int i57) {
                    return ((Long) arrayList17.get(i56)).equals(SelectAnimatedEmojiDialog.this.rowHashCodes.get(i57));
                }
            }, false).dispatchUpdatesTo(this.adapter);
        } else {
            i3 = 0;
            this.adapter.notifyDataSetChanged();
        }
        EmojiListView emojiListView = this.emojiGridView;
        if (emojiListView.scrolledByUserOnce) {
            return;
        }
        emojiListView.scrollToPosition(i3);
    }

    public void notifyDataSetChanged() {
        Adapter adapter = this.adapter;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void expand(int i, View view) {
        boolean z;
        int min;
        int i2;
        int size;
        Integer num;
        int i3;
        int i4;
        boolean z2;
        int i5 = this.positionToExpand.get(i);
        this.animateExpandFromButtonTranslate = 0.0f;
        Integer num2 = null;
        if (i5 >= 0 && i5 < this.packs.size()) {
            i4 = 24;
            EmojiView.EmojiPack emojiPack = this.packs.get(i5);
            if (emojiPack.expanded) {
                return;
            }
            z2 = i5 + 1 == this.packs.size();
            i3 = this.sectionToPosition.get(i5);
            this.expandedEmojiSets.add(Long.valueOf(emojiPack.set.id));
            i2 = emojiPack.expanded ? emojiPack.documents.size() : Math.min(24, emojiPack.documents.size());
            num = emojiPack.documents.size() > 24 ? Integer.valueOf(i3 + 1 + i2) : null;
            emojiPack.expanded = true;
            size = emojiPack.documents.size();
        } else if (i5 != -1 || (z = this.recentExpanded)) {
            return;
        } else {
            int i6 = (this.searchRow != -1 ? 1 : 0) + (this.longtapHintRow != -1 ? 1 : 0);
            boolean z3 = this.includeEmpty;
            int i7 = i6 + (z3 ? 1 : 0);
            if (z) {
                min = this.recent.size();
            } else {
                min = Math.min((40 - (z3 ? 1 : 0)) - 2, this.recent.size());
            }
            i2 = min;
            size = this.recent.size();
            this.recentExpanded = true;
            num = null;
            i3 = i7;
            i4 = 40;
            z2 = false;
        }
        if (size > i2) {
            num = Integer.valueOf(i3 + 1 + i2);
            num2 = Integer.valueOf(size - i2);
        }
        updateRows(false, true);
        if (num == null || num2 == null) {
            return;
        }
        this.animateExpandFromButton = view;
        this.animateExpandFromPosition = num.intValue();
        this.animateExpandToPosition = num.intValue() + num2.intValue();
        this.animateExpandStartTime = SystemClock.elapsedRealtime();
        if (z2) {
            final int intValue = num.intValue();
            final float f = num2.intValue() > i4 / 2 ? 1.5f : 3.5f;
            post(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    SelectAnimatedEmojiDialog.this.lambda$expand$31(f, intValue);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$expand$31(float f, int i) {
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
        int i3;
        if (this.drawBackground && (i3 = this.type) != 3 && i3 != 4) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f), 1073741824), View.MeasureSpec.makeMeasureSpec((int) Math.min(AndroidUtilities.dp(330.0f), AndroidUtilities.displaySize.y * 0.75f), Integer.MIN_VALUE));
        } else if (this.type == 6) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((int) (AndroidUtilities.displaySize.y * 0.35f), Integer.MIN_VALUE));
        } else {
            super.onMeasure(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z && this.type == 6) {
            this.layoutManager.setSpanCount((getMeasuredWidth() / AndroidUtilities.dp(42.0f)) * 5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCacheType() {
        int i = this.type;
        if (i != 5 && i != 7) {
            if (i == 6) {
                return AnimatedEmojiDrawable.getCacheTypeForEnterView();
            }
            if (i != 3 && i != 4) {
                return (i == 0 || i == 12 || i == 9 || i == 10 || i == 2) ? 2 : 3;
            }
        }
        return 13;
    }

    /* loaded from: classes4.dex */
    public class EmojiListView extends RecyclerListView {
        private LongSparseArray<AnimatedEmojiDrawable> animatedEmojiDrawables;
        private boolean invalidated;
        ArrayList<DrawingInBackgroundLine> lineDrawables;
        ArrayList<DrawingInBackgroundLine> lineDrawablesTmp;
        ArrayList<ArrayList<ImageViewEmoji>> unusedArrays;
        ArrayList<DrawingInBackgroundLine> unusedLineDrawables;
        SparseArray<ArrayList<ImageViewEmoji>> viewsGroupedByLines;

        public EmojiListView(Context context) {
            super(context);
            this.viewsGroupedByLines = new SparseArray<>();
            this.unusedArrays = new ArrayList<>();
            this.unusedLineDrawables = new ArrayList<>();
            this.lineDrawables = new ArrayList<>();
            this.lineDrawablesTmp = new ArrayList<>();
            this.animatedEmojiDrawables = new LongSparseArray<>();
            setDrawSelectorBehind(true);
            setClipToPadding(false);
            setSelectorRadius(AndroidUtilities.dp(4.0f));
            setSelectorDrawableColor(Theme.getColor(Theme.key_listSelector, this.resourcesProvider));
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (((org.telegram.ui.Components.AnimatedEmojiDrawable) r0).canOverrideColor() != false) goto L9;
         */
        @Override // org.telegram.ui.Components.RecyclerListView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean canHighlightChildAt(View view, float f, float f2) {
            if (view instanceof ImageViewEmoji) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) view;
                if (!imageViewEmoji.empty) {
                    Drawable drawable = imageViewEmoji.drawable;
                    if (drawable instanceof AnimatedEmojiDrawable) {
                    }
                }
                setSelectorDrawableColor(ColorUtils.setAlphaComponent(SelectAnimatedEmojiDialog.this.accentColor, 30));
                return super.canHighlightChildAt(view, f, f2);
            }
            setSelectorDrawableColor(Theme.getColor(Theme.key_listSelector, this.resourcesProvider));
            return super.canHighlightChildAt(view, f, f2);
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            invalidate();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            ImageReceiver imageReceiver;
            ImageReceiver imageReceiver2;
            if (getVisibility() != 0) {
                return;
            }
            this.invalidated = false;
            int saveCount = canvas.getSaveCount();
            if (SelectAnimatedEmojiDialog.this.type != 6 && SelectAnimatedEmojiDialog.this.type != 14 && SelectAnimatedEmojiDialog.this.type != 13 && !this.selectorRect.isEmpty()) {
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
            boolean z = ((SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0L ? 1 : (SelectAnimatedEmojiDialog.this.animateExpandStartTime == 0L ? 0 : -1)) > 0 && ((SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime) > SelectAnimatedEmojiDialog.this.animateExpandDuration() ? 1 : ((SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime) == SelectAnimatedEmojiDialog.this.animateExpandDuration() ? 0 : -1)) < 0) && SelectAnimatedEmojiDialog.this.animateExpandFromButton != null && SelectAnimatedEmojiDialog.this.animateExpandFromPosition >= 0;
            if (this.animatedEmojiDrawables != null) {
                for (int i2 = 0; i2 < getChildCount(); i2++) {
                    View childAt = getChildAt(i2);
                    if (childAt instanceof ImageViewEmoji) {
                        ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt;
                        imageViewEmoji.updatePressedProgress();
                        int y = SelectAnimatedEmojiDialog.this.smoothScrolling ? (int) childAt.getY() : childAt.getTop();
                        ArrayList<ImageViewEmoji> arrayList = this.viewsGroupedByLines.get(y);
                        canvas.save();
                        canvas.translate(imageViewEmoji.getX(), imageViewEmoji.getY());
                        imageViewEmoji.drawSelected(canvas, this);
                        canvas.restore();
                        if (imageViewEmoji.getBackground() != null) {
                            imageViewEmoji.getBackground().setBounds((int) imageViewEmoji.getX(), (int) imageViewEmoji.getY(), ((int) imageViewEmoji.getX()) + imageViewEmoji.getWidth(), ((int) imageViewEmoji.getY()) + imageViewEmoji.getHeight());
                            imageViewEmoji.getBackground().setAlpha((int) (255 * imageViewEmoji.getAlpha()));
                            imageViewEmoji.getBackground().draw(canvas);
                            imageViewEmoji.getBackground().setAlpha(255);
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
                        if (premiumLockIconView != null && premiumLockIconView.getVisibility() == 0 && imageViewEmoji.premiumLockIconView.getImageReceiver() == null && (imageReceiver2 = imageViewEmoji.imageReceiverToDraw) != null) {
                            imageViewEmoji.premiumLockIconView.setImageReceiver(imageReceiver2);
                        }
                    }
                    if (z && childAt != null) {
                        if (getChildAdapterPosition(childAt) == SelectAnimatedEmojiDialog.this.animateExpandFromPosition - (SelectAnimatedEmojiDialog.this.animateExpandFromButtonTranslate > 0.0f ? 0 : 1)) {
                            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(MathUtils.clamp(((float) (SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime)) / 200.0f, 0.0f, 1.0f));
                            if (interpolation < 1.0f) {
                                float f = 1.0f - interpolation;
                                canvas.saveLayerAlpha(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom(), (int) (255.0f * f), 31);
                                canvas.translate(childAt.getLeft(), childAt.getTop() + SelectAnimatedEmojiDialog.this.animateExpandFromButtonTranslate);
                                float f2 = (f * 0.5f) + 0.5f;
                                canvas.scale(f2, f2, childAt.getWidth() / 2.0f, childAt.getHeight() / 2.0f);
                                SelectAnimatedEmojiDialog.this.animateExpandFromButton.draw(canvas);
                                canvas.restore();
                            }
                        }
                    }
                }
            }
            this.lineDrawablesTmp.clear();
            this.lineDrawablesTmp.addAll(this.lineDrawables);
            this.lineDrawables.clear();
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
                    if (i4 >= this.lineDrawablesTmp.size()) {
                        break;
                    } else if (this.lineDrawablesTmp.get(i4).position == childAdapterPosition) {
                        drawingInBackgroundLine = this.lineDrawablesTmp.get(i4);
                        this.lineDrawablesTmp.remove(i4);
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
                        drawingInBackgroundLine.setLayerNum(7);
                    }
                    drawingInBackgroundLine.position = childAdapterPosition;
                    drawingInBackgroundLine.onAttachToWindow();
                }
                this.lineDrawables.add(drawingInBackgroundLine);
                drawingInBackgroundLine.imageViewEmojis = valueAt2;
                canvas.save();
                canvas.translate(imageViewEmoji2.getLeft(), imageViewEmoji2.getY());
                drawingInBackgroundLine.startOffset = imageViewEmoji2.getLeft();
                int measuredWidth = getMeasuredWidth() - (imageViewEmoji2.getLeft() * 2);
                int measuredHeight = imageViewEmoji2.getMeasuredHeight();
                if (measuredWidth > 0 && measuredHeight > 0) {
                    drawingInBackgroundLine.draw(canvas, currentTimeMillis, measuredWidth, measuredHeight, getAlpha());
                }
                canvas.restore();
                i3++;
            }
            for (int i5 = 0; i5 < this.lineDrawablesTmp.size(); i5++) {
                if (this.unusedLineDrawables.size() < 3) {
                    this.unusedLineDrawables.add(this.lineDrawablesTmp.get(i5));
                    this.lineDrawablesTmp.get(i5).imageViewEmojis = null;
                    this.lineDrawablesTmp.get(i5).reset();
                } else {
                    this.lineDrawablesTmp.get(i5).onDetachFromWindow();
                }
            }
            this.lineDrawablesTmp.clear();
            for (int i6 = 0; i6 < getChildCount(); i6++) {
                View childAt2 = getChildAt(i6);
                if (!(childAt2 instanceof ImageViewEmoji)) {
                    if (childAt2 != null && childAt2 != SelectAnimatedEmojiDialog.this.animateExpandFromButton) {
                        canvas.save();
                        canvas.translate((int) childAt2.getX(), (int) childAt2.getY());
                        childAt2.draw(canvas);
                        canvas.restore();
                    }
                } else {
                    ImageViewEmoji imageViewEmoji3 = (ImageViewEmoji) childAt2;
                    PremiumLockIconView premiumLockIconView2 = imageViewEmoji3.premiumLockIconView;
                    if (premiumLockIconView2 != null && premiumLockIconView2.getVisibility() == 0) {
                        canvas.save();
                        canvas.translate((int) ((imageViewEmoji3.getX() + imageViewEmoji3.getMeasuredWidth()) - imageViewEmoji3.premiumLockIconView.getMeasuredWidth()), (int) ((imageViewEmoji3.getY() + imageViewEmoji3.getMeasuredHeight()) - imageViewEmoji3.premiumLockIconView.getMeasuredHeight()));
                        Drawable drawable = imageViewEmoji3.drawable;
                        if (drawable instanceof AnimatedEmojiDrawable) {
                            imageReceiver = ((AnimatedEmojiDrawable) drawable).getImageReceiver();
                        } else {
                            imageReceiver = imageViewEmoji3.imageReceiver;
                        }
                        if (!imageViewEmoji3.premiumLockIconView.done()) {
                            imageViewEmoji3.premiumLockIconView.setImageReceiver(imageReceiver);
                        }
                        imageViewEmoji3.premiumLockIconView.draw(canvas);
                        canvas.restore();
                    }
                    if (imageViewEmoji3.emojiDrawable != null) {
                        canvas.save();
                        int dp = AndroidUtilities.dp(17.0f);
                        float f3 = dp;
                        canvas.translate((int) ((imageViewEmoji3.getX() + imageViewEmoji3.getMeasuredWidth()) - f3), (int) ((imageViewEmoji3.getY() + imageViewEmoji3.getMeasuredHeight()) - f3));
                        imageViewEmoji3.emojiDrawable.setBounds(0, 0, dp, dp);
                        imageViewEmoji3.emojiDrawable.draw(canvas);
                        canvas.restore();
                    }
                }
            }
            canvas.restoreToCount(saveCount);
            HwEmojis.exec();
        }

        /* loaded from: classes4.dex */
        public class DrawingInBackgroundLine extends DrawingInBackgroundThreadDrawable {
            ArrayList<ImageViewEmoji> imageViewEmojis;
            public int position;
            public int startOffset;
            ArrayList<ImageViewEmoji> drawInBackgroundViews = new ArrayList<>();
            float skewAlpha = 1.0f;
            boolean skewBelow = false;
            boolean lite = LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS);
            private OvershootInterpolator appearScaleInterpolator = new OvershootInterpolator(3.0f);

            public DrawingInBackgroundLine() {
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void draw(Canvas canvas, long j, int i, int i2, float f) {
                ArrayList<ImageViewEmoji> arrayList = this.imageViewEmojis;
                if (arrayList == null) {
                    return;
                }
                this.skewAlpha = 1.0f;
                this.skewBelow = false;
                if (!arrayList.isEmpty()) {
                    ImageViewEmoji imageViewEmoji = this.imageViewEmojis.get(0);
                    if (imageViewEmoji.getY() > (EmojiListView.this.getHeight() - EmojiListView.this.getPaddingBottom()) - imageViewEmoji.getHeight()) {
                        float clamp = MathUtils.clamp((-((imageViewEmoji.getY() - EmojiListView.this.getHeight()) + EmojiListView.this.getPaddingBottom())) / imageViewEmoji.getHeight(), 0.0f, 1.0f);
                        this.skewAlpha = clamp;
                        this.skewAlpha = (clamp * 0.75f) + 0.25f;
                    }
                }
                boolean z = true;
                boolean z2 = SelectAnimatedEmojiDialog.this.type == 13 || this.skewAlpha < 1.0f || EmojiListView.this.isAnimating() || this.imageViewEmojis.size() <= 4 || !this.lite || SelectAnimatedEmojiDialog.this.enterAnimationInProgress() || SelectAnimatedEmojiDialog.this.type == 4 || SelectAnimatedEmojiDialog.this.type == 6;
                if (!z2) {
                    boolean z3 = SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration();
                    for (int i3 = 0; i3 < this.imageViewEmojis.size(); i3++) {
                        ImageViewEmoji imageViewEmoji2 = this.imageViewEmojis.get(i3);
                        if (imageViewEmoji2.pressedProgress != 0.0f || imageViewEmoji2.selectedProgress != 0.0f || imageViewEmoji2.backAnimator != null || imageViewEmoji2.getTranslationX() != 0.0f || imageViewEmoji2.getTranslationY() != 0.0f || imageViewEmoji2.getAlpha() != 1.0f || ((z3 && imageViewEmoji2.position > SelectAnimatedEmojiDialog.this.animateExpandFromPosition && imageViewEmoji2.position < SelectAnimatedEmojiDialog.this.animateExpandToPosition) || imageViewEmoji2.isStaticIcon)) {
                            break;
                        }
                    }
                }
                z = z2;
                float f2 = HwEmojis.isHwEnabled() ? 1.0f : f;
                if (z || HwEmojis.isPreparing()) {
                    prepareDraw(System.currentTimeMillis());
                    drawInUiThread(canvas, f2);
                    reset();
                    return;
                }
                super.draw(canvas, j, i, i2, f2);
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void prepareDraw(long j) {
                float alpha;
                ImageReceiver imageReceiver;
                this.drawInBackgroundViews.clear();
                for (int i = 0; i < this.imageViewEmojis.size(); i++) {
                    ImageViewEmoji imageViewEmoji = this.imageViewEmojis.get(i);
                    if (!imageViewEmoji.notDraw) {
                        if (imageViewEmoji.empty) {
                            Drawable premiumStar = SelectAnimatedEmojiDialog.this.getPremiumStar();
                            float f = (SelectAnimatedEmojiDialog.this.type == 5 || SelectAnimatedEmojiDialog.this.type == 10 || SelectAnimatedEmojiDialog.this.type == 9 || SelectAnimatedEmojiDialog.this.type == 7) ? 1.3f : 1.0f;
                            if (imageViewEmoji.pressedProgress != 0.0f || imageViewEmoji.selectedProgress > 0.0f) {
                                f *= ((1.0f - Math.max(imageViewEmoji.selectedProgress * 0.8f, imageViewEmoji.pressedProgress)) * 0.2f) + 0.8f;
                            }
                            if (premiumStar != null) {
                                premiumStar.setAlpha(255);
                                int width = (imageViewEmoji.getWidth() - imageViewEmoji.getPaddingLeft()) - imageViewEmoji.getPaddingRight();
                                int height = (imageViewEmoji.getHeight() - imageViewEmoji.getPaddingTop()) - imageViewEmoji.getPaddingBottom();
                                Rect rect = AndroidUtilities.rectTmp2;
                                float f2 = width / 2.0f;
                                float f3 = height / 2.0f;
                                rect.set((int) ((imageViewEmoji.getWidth() / 2.0f) - ((imageViewEmoji.getScaleX() * f2) * f)), (int) ((imageViewEmoji.getHeight() / 2.0f) - ((imageViewEmoji.getScaleY() * f3) * f)), (int) ((imageViewEmoji.getWidth() / 2.0f) + (f2 * imageViewEmoji.getScaleX() * f)), (int) ((imageViewEmoji.getHeight() / 2.0f) + (f3 * imageViewEmoji.getScaleY() * f)));
                                rect.offset(imageViewEmoji.getLeft() - this.startOffset, 0);
                                if (imageViewEmoji.drawableBounds == null) {
                                    imageViewEmoji.drawableBounds = new Rect();
                                }
                                imageViewEmoji.drawableBounds.set(rect);
                                imageViewEmoji.setDrawable(premiumStar);
                                this.drawInBackgroundViews.add(imageViewEmoji);
                            }
                        } else {
                            if (imageViewEmoji.pressedProgress != 0.0f || imageViewEmoji.selectedProgress > 0.0f) {
                                Math.max(imageViewEmoji.selectedProgress * 0.8f, imageViewEmoji.pressedProgress);
                            }
                            if ((SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration()) && SelectAnimatedEmojiDialog.this.animateExpandFromPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandToPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0) {
                                int childAdapterPosition = EmojiListView.this.getChildAdapterPosition(imageViewEmoji) - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                int i2 = SelectAnimatedEmojiDialog.this.animateExpandToPosition - SelectAnimatedEmojiDialog.this.animateExpandFromPosition;
                                if (childAdapterPosition < 0 || childAdapterPosition >= i2) {
                                    alpha = 1.0f;
                                } else {
                                    float clamp = MathUtils.clamp(((float) (SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime)) / ((float) SelectAnimatedEmojiDialog.this.animateExpandAppearDuration()), 0.0f, 1.0f);
                                    float f4 = childAdapterPosition;
                                    float f5 = i2;
                                    float f6 = f5 / 4.0f;
                                    float cascade = AndroidUtilities.cascade(clamp, f4, f5, f6);
                                    this.appearScaleInterpolator.getInterpolation(AndroidUtilities.cascade(clamp, f4, f5, f6));
                                    alpha = cascade * 1.0f;
                                }
                            } else {
                                alpha = 1.0f * imageViewEmoji.getAlpha();
                            }
                            if (!imageViewEmoji.isDefaultReaction && !imageViewEmoji.isStaticIcon) {
                                if (imageViewEmoji.span != null) {
                                    Drawable drawable = imageViewEmoji.drawable;
                                    AnimatedEmojiDrawable animatedEmojiDrawable = drawable instanceof AnimatedEmojiDrawable ? (AnimatedEmojiDrawable) drawable : null;
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
                                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
                                imageReceiver.setEmojiPaused(selectAnimatedEmojiDialog.paused && !(selectAnimatedEmojiDialog.pausedExceptSelected && imageViewEmoji.selected));
                                if (imageViewEmoji.selected) {
                                    imageReceiver.setRoundRadius(AndroidUtilities.dp(4.0f));
                                } else {
                                    imageReceiver.setRoundRadius(0);
                                }
                                ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolderArr = imageViewEmoji.backgroundThreadDrawHolder;
                                int i3 = this.threadIndex;
                                backgroundThreadDrawHolderArr[i3] = imageReceiver.setDrawInBackgroundThread(backgroundThreadDrawHolderArr[i3], i3);
                                imageViewEmoji.backgroundThreadDrawHolder[this.threadIndex].time = j;
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
                                if (imageViewEmoji.selected && SelectAnimatedEmojiDialog.this.type != 3 && SelectAnimatedEmojiDialog.this.type != 4) {
                                    rect2.set(Math.round(rect2.centerX() - ((rect2.width() / 2.0f) * 0.86f)), Math.round(rect2.centerY() - ((rect2.height() / 2.0f) * 0.86f)), Math.round(rect2.centerX() + ((rect2.width() / 2.0f) * 0.86f)), Math.round(rect2.centerY() + ((rect2.height() / 2.0f) * 0.86f)));
                                }
                                rect2.offset((imageViewEmoji.getLeft() + ((int) imageViewEmoji.getTranslationX())) - this.startOffset, 0);
                                imageViewEmoji.backgroundThreadDrawHolder[this.threadIndex].setBounds(rect2);
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
                            imageViewEmoji.drawable.draw(canvas);
                        } else {
                            ImageReceiver imageReceiver = imageViewEmoji.imageReceiverToDraw;
                            if (imageReceiver != null) {
                                imageReceiver.draw(canvas, imageViewEmoji.backgroundThreadDrawHolder[this.threadIndex]);
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
                            if (SelectAnimatedEmojiDialog.this.type == 13) {
                                scaleX *= 0.87f;
                            }
                            if (imageViewEmoji.pressedProgress != 0.0f || (imageViewEmoji.selectedProgress > 0.0f && SelectAnimatedEmojiDialog.this.type != 3 && SelectAnimatedEmojiDialog.this.type != 4)) {
                                scaleX *= 0.8f + (0.2f * (1.0f - Math.max((SelectAnimatedEmojiDialog.this.type == 3 || SelectAnimatedEmojiDialog.this.type == 4) ? 1.0f : imageViewEmoji.selectedProgress * 0.7f, imageViewEmoji.pressedProgress)));
                            }
                            boolean z = SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0 && SystemClock.elapsedRealtime() - SelectAnimatedEmojiDialog.this.animateExpandStartTime < SelectAnimatedEmojiDialog.this.animateExpandDuration();
                            if (z && SelectAnimatedEmojiDialog.this.animateExpandFromPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandToPosition >= 0 && SelectAnimatedEmojiDialog.this.animateExpandStartTime > 0) {
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
                                f2 *= imageViewEmoji.getAlpha();
                            }
                            Rect rect = AndroidUtilities.rectTmp2;
                            rect.set(((int) imageViewEmoji.getX()) + imageViewEmoji.getPaddingLeft(), imageViewEmoji.getPaddingTop(), (((int) imageViewEmoji.getX()) + imageViewEmoji.getWidth()) - imageViewEmoji.getPaddingRight(), imageViewEmoji.getHeight() - imageViewEmoji.getPaddingBottom());
                            if (!SelectAnimatedEmojiDialog.this.smoothScrolling && !z) {
                                rect.offset(0, (int) imageViewEmoji.getTranslationY());
                            }
                            Drawable drawable = null;
                            if (imageViewEmoji.empty) {
                                drawable = SelectAnimatedEmojiDialog.this.getPremiumStar();
                                if (SelectAnimatedEmojiDialog.this.type == 5 || SelectAnimatedEmojiDialog.this.type == 10 || SelectAnimatedEmojiDialog.this.type == 9 || SelectAnimatedEmojiDialog.this.type == 7) {
                                    rect.inset((int) ((-rect.width()) * 0.15f), (int) ((-rect.height()) * 0.15f));
                                }
                                drawable.setBounds(rect);
                                drawable.setAlpha(255);
                            } else if (!imageViewEmoji.isDefaultReaction && !imageViewEmoji.isStaticIcon) {
                                if ((imageViewEmoji.span != null || SelectAnimatedEmojiDialog.this.type == 13) && !imageViewEmoji.notDraw && (drawable = imageViewEmoji.drawable) != null) {
                                    drawable.setAlpha(255);
                                    drawable.setBounds(rect);
                                }
                            } else {
                                ImageReceiver imageReceiver = imageViewEmoji.imageReceiver;
                                if (imageReceiver != null) {
                                    imageReceiver.setImageCoords(rect);
                                }
                            }
                            if (SelectAnimatedEmojiDialog.this.premiumStarColorFilter != null) {
                                Drawable drawable2 = imageViewEmoji.drawable;
                                if (drawable2 instanceof AnimatedEmojiDrawable) {
                                    drawable2.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                                }
                            }
                            float f6 = this.skewAlpha;
                            imageViewEmoji.skewAlpha = f6;
                            imageViewEmoji.skewIndex = i;
                            if (scaleX != 1.0f || f6 < 1.0f) {
                                canvas.save();
                                if (imageViewEmoji.selectedProgress > 1.0f && SelectAnimatedEmojiDialog.this.type != 3 && SelectAnimatedEmojiDialog.this.type != 4 && SelectAnimatedEmojiDialog.this.type != 6) {
                                    float lerp = AndroidUtilities.lerp(1.0f, 0.85f, imageViewEmoji.selectedProgress);
                                    canvas.scale(lerp, lerp, rect.centerX(), rect.centerY());
                                }
                                if (SelectAnimatedEmojiDialog.this.type == 6 || SelectAnimatedEmojiDialog.this.type == 13 || SelectAnimatedEmojiDialog.this.type == 14) {
                                    canvas.scale(scaleX, scaleX, rect.centerX(), rect.centerY());
                                } else {
                                    skew(canvas, i, imageViewEmoji.getHeight());
                                }
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
                if (drawable != null) {
                    drawable.setAlpha((int) (f * 255.0f));
                    drawable.draw(canvas);
                    drawable.setColorFilter(SelectAnimatedEmojiDialog.this.premiumStarColorFilter);
                } else if ((imageViewEmoji.isDefaultReaction || imageViewEmoji.isStaticIcon) && imageViewEmoji.imageReceiver != null) {
                    canvas.save();
                    canvas.clipRect(imageViewEmoji.imageReceiver.getImageX(), imageViewEmoji.imageReceiver.getImageY(), imageViewEmoji.imageReceiver.getImageX2(), imageViewEmoji.imageReceiver.getImageY2());
                    imageViewEmoji.imageReceiver.setAlpha(f);
                    imageViewEmoji.imageReceiver.draw(canvas);
                    canvas.restore();
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void onFrameReady() {
                super.onFrameReady();
                for (int i = 0; i < this.drawInBackgroundViews.size(); i++) {
                    ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolderArr = this.drawInBackgroundViews.get(i).backgroundThreadDrawHolder;
                    int i2 = this.threadIndex;
                    if (backgroundThreadDrawHolderArr[i2] != null) {
                        backgroundThreadDrawHolderArr[i2].release();
                    }
                }
                SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            if (this == selectAnimatedEmojiDialog.emojiGridView) {
                selectAnimatedEmojiDialog.bigReactionImageReceiver.onAttachedToWindow();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = SelectAnimatedEmojiDialog.this;
            if (this == selectAnimatedEmojiDialog.emojiGridView) {
                selectAnimatedEmojiDialog.bigReactionImageReceiver.onDetachedFromWindow();
            }
            release(this.unusedLineDrawables);
            release(this.lineDrawables);
            release(this.lineDrawablesTmp);
        }

        private void release(ArrayList<DrawingInBackgroundLine> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.get(i).onDetachFromWindow();
            }
            arrayList.clear();
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public void invalidateViews() {
            if (HwEmojis.grab(this)) {
                return;
            }
            super.invalidateViews();
        }

        @Override // android.view.View
        public void invalidate() {
            if (HwEmojis.grab(this) || this.invalidated) {
                return;
            }
            this.invalidated = true;
            super.invalidate();
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            if (HwEmojis.grab(this)) {
                return;
            }
            super.invalidate(i, i2, i3, i4);
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
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.scrimDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.setSecondParent(this);
        }
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
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.scrimDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.setSecondParent(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$32() {
        updateRows(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$33() {
        NotificationCenter.getGlobalInstance().removeDelayed(this.updateRows);
        NotificationCenter.getGlobalInstance().doOnIdle(this.updateRows);
    }

    private void updateRowsDelayed() {
        AndroidUtilities.cancelRunOnUIThread(this.updateRowsDelayed);
        AndroidUtilities.runOnUIThread(this.updateRowsDelayed);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.stickersDidLoad) {
            if (((Integer) objArr[0]).intValue() == 5 || (((Integer) objArr[0]).intValue() == 0 && this.showStickers)) {
                updateRowsDelayed();
            }
        } else if (i == NotificationCenter.featuredEmojiDidLoad) {
            updateRowsDelayed();
        } else if (i == NotificationCenter.recentEmojiStatusesUpdate) {
            updateRowsDelayed();
        } else if (i == NotificationCenter.groupStickersDidLoad) {
            updateRowsDelayed();
        } else if (i == NotificationCenter.emojiLoaded) {
            AndroidUtilities.forEachViews((RecyclerView) this.emojiGridView, (com.google.android.exoplayer2.util.Consumer<View>) FloatingDebugView$$ExternalSyntheticLambda3.INSTANCE);
            EmojiListView emojiListView = this.emojiGridView;
            if (emojiListView != null) {
                emojiListView.invalidate();
            }
        }
    }

    private boolean isAnimatedShow() {
        int i = this.type;
        return (i == 3 || i == 4 || i == 6) ? false : true;
    }

    public void onShow(Runnable runnable) {
        int i;
        Integer num = this.listStateId;
        if (num != null) {
            listStates.get(num);
        }
        this.dismiss = runnable;
        if (!this.drawBackground) {
            checkScroll();
            for (int i2 = 0; i2 < this.emojiGridView.getChildCount(); i2++) {
                View childAt = this.emojiGridView.getChildAt(i2);
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
        if (isAnimatedShow()) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.showAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                    SelectAnimatedEmojiDialog.this.lambda$onShow$34(valueAnimator3);
                }
            });
            this.showAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.25
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    HwEmojis.disableHw();
                    SelectAnimatedEmojiDialog.this.emojiGridView.setLayerType(0, null);
                    SelectAnimatedEmojiDialog.this.searchBox.setLayerType(0, null);
                    SelectAnimatedEmojiDialog.this.emojiTabsShadow.setLayerType(0, null);
                    SelectAnimatedEmojiDialog.this.backgroundView.setLayerType(0, null);
                    if (SelectAnimatedEmojiDialog.this.bubble2View != null) {
                        SelectAnimatedEmojiDialog.this.bubble2View.setLayerType(0, null);
                    }
                    if (SelectAnimatedEmojiDialog.this.bubble1View != null) {
                        SelectAnimatedEmojiDialog.this.bubble1View.setLayerType(0, null);
                    }
                    SelectAnimatedEmojiDialog.this.searchBox.checkInitialization();
                    SelectAnimatedEmojiDialog.this.emojiTabs.showRecentTabStub(false);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_CALLS_ANIMATIONS));
                    SelectAnimatedEmojiDialog.this.notificationsLocker.unlock();
                    final NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                    Objects.requireNonNull(globalInstance);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$25$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationCenter.this.runDelayedNotifications();
                        }
                    });
                    SelectAnimatedEmojiDialog.this.checkScroll();
                    SelectAnimatedEmojiDialog.this.updateShow(1.0f);
                    for (int i3 = 0; i3 < SelectAnimatedEmojiDialog.this.emojiGridView.getChildCount(); i3++) {
                        View childAt2 = SelectAnimatedEmojiDialog.this.emojiGridView.getChildAt(i3);
                        childAt2.setScaleX(1.0f);
                        childAt2.setScaleY(1.0f);
                    }
                    for (int i4 = 0; i4 < SelectAnimatedEmojiDialog.this.emojiTabs.contentView.getChildCount(); i4++) {
                        View childAt3 = SelectAnimatedEmojiDialog.this.emojiTabs.contentView.getChildAt(i4);
                        childAt3.setScaleX(1.0f);
                        childAt3.setScaleY(1.0f);
                    }
                    SelectAnimatedEmojiDialog.this.emojiTabs.contentView.invalidate();
                    SelectAnimatedEmojiDialog.this.emojiGridViewContainer.invalidate();
                    SelectAnimatedEmojiDialog.this.emojiGridView.invalidate();
                }
            });
            if (isFirstOpen && (i = this.type) != 5 && i != 10 && i != 7) {
                isFirstOpen = false;
                AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).setUiDbCallback(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.this.lambda$onShow$36();
                    }
                });
                HwEmojis.prepare(null, true);
            } else {
                HwEmojis.prepare(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        SelectAnimatedEmojiDialog.this.lambda$onShow$38();
                    }
                }, true);
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_CALLS_ANIMATIONS));
            this.notificationsLocker.lock();
            this.showAnimator.setDuration(800L);
            this.emojiGridView.setLayerType(2, null);
            this.searchBox.setLayerType(2, null);
            this.emojiTabsShadow.setLayerType(2, null);
            this.backgroundView.setLayerType(2, null);
            View view = this.bubble2View;
            if (view != null) {
                view.setLayerType(2, null);
            }
            View view2 = this.bubble1View;
            if (view2 != null) {
                view2.setLayerType(2, null);
            }
            this.emojiTabs.showRecentTabStub(true);
            updateShow(0.0f);
            return;
        }
        checkScroll();
        updateShow(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$34(ValueAnimator valueAnimator) {
        updateShow(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$36() {
        HwEmojis.enableHw();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$onShow$35();
            }
        }, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$35() {
        this.showAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$38() {
        HwEmojis.enableHw();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                SelectAnimatedEmojiDialog.this.lambda$onShow$37();
            }
        }, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShow$37() {
        this.showAnimator.start();
    }

    /* loaded from: classes4.dex */
    public class SearchBox extends FrameLayout {
        private FrameLayout box;
        private StickerCategoriesListView categoriesListView;
        private ImageView clear;
        private Runnable delayedToggle;
        private EditTextCaption input;
        private FrameLayout inputBox;
        private View inputBoxGradient;
        private float inputBoxGradientAlpha;
        private ValueAnimator inputBoxGradientAnimator;
        private boolean inputBoxShown;
        private ImageView search;
        private SearchStateDrawable searchStateDrawable;

        public SearchBox(Context context, boolean z) {
            super(context);
            this.inputBoxShown = false;
            setClickable(true);
            this.box = new FrameLayout(context);
            if (z) {
                setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, SelectAnimatedEmojiDialog.this.resourcesProvider));
            }
            FrameLayout frameLayout = this.box;
            int dp = AndroidUtilities.dp(18.0f);
            int i = Theme.key_chat_emojiPanelBackground;
            frameLayout.setBackground(Theme.createRoundRectDrawable(dp, Theme.getColor(i, SelectAnimatedEmojiDialog.this.resourcesProvider)));
            if (Build.VERSION.SDK_INT >= 21) {
                this.box.setClipToOutline(true);
                this.box.setOutlineProvider(new ViewOutlineProvider(this, SelectAnimatedEmojiDialog.this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.1
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), AndroidUtilities.dp(18.0f));
                    }
                });
            }
            addView(this.box, LayoutHelper.createFrame(-1, 36.0f, 55, 8.0f, 12.0f, 8.0f, 8.0f));
            ImageView imageView = new ImageView(context);
            this.search = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            SearchStateDrawable searchStateDrawable = new SearchStateDrawable();
            this.searchStateDrawable = searchStateDrawable;
            searchStateDrawable.setIconState(0, false);
            SearchStateDrawable searchStateDrawable2 = this.searchStateDrawable;
            int i2 = Theme.key_chat_emojiSearchIcon;
            searchStateDrawable2.setColor(Theme.getColor(i2, SelectAnimatedEmojiDialog.this.resourcesProvider));
            this.search.setImageDrawable(this.searchStateDrawable);
            this.search.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectAnimatedEmojiDialog.SearchBox.this.lambda$new$0(view);
                }
            });
            this.box.addView(this.search, LayoutHelper.createFrame(36, 36, 51));
            FrameLayout frameLayout2 = new FrameLayout(context, SelectAnimatedEmojiDialog.this, z) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.2
                Paint fadePaint;
                final /* synthetic */ boolean val$drawBackground;

                {
                    this.val$drawBackground = z;
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    if (!this.val$drawBackground && SearchBox.this.inputBoxGradientAlpha > 0.0f) {
                        if (this.fadePaint == null) {
                            Paint paint = new Paint();
                            this.fadePaint = paint;
                            paint.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(18.0f), 0.0f, new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                            this.fadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                        }
                        canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                        super.dispatchDraw(canvas);
                        this.fadePaint.setAlpha((int) (SearchBox.this.inputBoxGradientAlpha * 255.0f));
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(18.0f), getMeasuredHeight(), this.fadePaint);
                        canvas.restore();
                        return;
                    }
                    super.dispatchDraw(canvas);
                }
            };
            this.inputBox = frameLayout2;
            this.box.addView(frameLayout2, LayoutHelper.createFrame(-1, -1.0f, 119, 36.0f, 0.0f, 0.0f, 0.0f));
            3 r6 = new 3(context, SelectAnimatedEmojiDialog.this.resourcesProvider, SelectAnimatedEmojiDialog.this);
            this.input = r6;
            r6.addTextChangedListener(new TextWatcher(SelectAnimatedEmojiDialog.this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.4
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    String obj = (SearchBox.this.input.getText() == null || AndroidUtilities.trim(SearchBox.this.input.getText(), null).length() == 0) ? null : SearchBox.this.input.getText().toString();
                    SelectAnimatedEmojiDialog.this.search(obj);
                    if (SearchBox.this.categoriesListView != null) {
                        SearchBox.this.categoriesListView.selectCategory((StickerCategoriesListView.EmojiCategory) null);
                        SearchBox.this.categoriesListView.updateCategoriesShown(TextUtils.isEmpty(obj), true);
                    }
                    if (SearchBox.this.input != null) {
                        SearchBox.this.input.clearAnimation();
                        SearchBox.this.input.animate().translationX(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                    }
                    SearchBox.this.showInputBoxGradient(false);
                }
            });
            this.input.setBackground(null);
            this.input.setPadding(0, 0, AndroidUtilities.dp(4.0f), 0);
            this.input.setTextSize(1, 16.0f);
            this.input.setHint(LocaleController.getString("Search", R.string.Search));
            this.input.setHintTextColor(Theme.getColor(i2, SelectAnimatedEmojiDialog.this.resourcesProvider));
            this.input.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, SelectAnimatedEmojiDialog.this.resourcesProvider));
            this.input.setImeOptions(268435459);
            this.input.setCursorColor(Theme.getColor(Theme.key_featuredStickers_addedIcon, SelectAnimatedEmojiDialog.this.resourcesProvider));
            this.input.setCursorSize(AndroidUtilities.dp(20.0f));
            this.input.setGravity(19);
            this.input.setCursorWidth(1.5f);
            this.input.setMaxLines(1);
            this.input.setSingleLine(true);
            this.input.setLines(1);
            this.input.setTranslationY(AndroidUtilities.dp(-1.0f));
            this.inputBox.addView(this.input, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 32.0f, 0.0f));
            if (z) {
                this.inputBoxGradient = new View(context);
                Drawable mutate = context.getResources().getDrawable(R.drawable.gradient_right).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i, SelectAnimatedEmojiDialog.this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
                this.inputBoxGradient.setBackground(mutate);
                this.inputBoxGradient.setAlpha(0.0f);
                this.inputBox.addView(this.inputBoxGradient, LayoutHelper.createFrame(18, -1, 3));
            }
            setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectAnimatedEmojiDialog.SearchBox.this.lambda$new$1(view);
                }
            });
            ImageView imageView2 = new ImageView(context);
            this.clear = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            this.clear.setImageDrawable(new CloseProgressDrawable2(1.25f, SelectAnimatedEmojiDialog.this) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.5
                {
                    setSide(AndroidUtilities.dp(7.0f));
                }

                @Override // org.telegram.ui.Components.CloseProgressDrawable2
                protected int getCurrentColor() {
                    return Theme.getColor(Theme.key_chat_emojiSearchIcon, SelectAnimatedEmojiDialog.this.resourcesProvider);
                }
            });
            this.clear.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector, SelectAnimatedEmojiDialog.this.resourcesProvider), 1, AndroidUtilities.dp(15.0f)));
            this.clear.setAlpha(0.0f);
            this.clear.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectAnimatedEmojiDialog.SearchBox.this.lambda$new$2(view);
                }
            });
            this.box.addView(this.clear, LayoutHelper.createFrame(36, 36, 53));
            if (HwEmojis.isFirstOpen()) {
                return;
            }
            createCategoriesListView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            if (this.searchStateDrawable.getIconState() == 1) {
                this.input.setText("");
                SelectAnimatedEmojiDialog.this.search(null, true, false);
                StickerCategoriesListView stickerCategoriesListView = this.categoriesListView;
                if (stickerCategoriesListView != null) {
                    stickerCategoriesListView.selectCategory((StickerCategoriesListView.EmojiCategory) null);
                    this.categoriesListView.updateCategoriesShown(true, true);
                    this.categoriesListView.scrollToStart();
                }
                this.input.clearAnimation();
                this.input.animate().translationX(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                showInputBoxGradient(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 3 extends EditTextCaption {
            3(Context context, Theme.ResourcesProvider resourcesProvider, SelectAnimatedEmojiDialog selectAnimatedEmojiDialog) {
                super(context, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 && SelectAnimatedEmojiDialog.this.prevWindowKeyboardVisible()) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$3$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            SelectAnimatedEmojiDialog.SearchBox.3.this.lambda$onTouchEvent$0();
                        }
                    }, 200L);
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onTouchEvent$0() {
                requestFocus();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public void onFocusChanged(boolean z, int i, Rect rect) {
                if (z) {
                    SelectAnimatedEmojiDialog.this.onInputFocus();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$3$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SelectAnimatedEmojiDialog.SearchBox.3.this.lambda$onFocusChanged$1();
                        }
                    }, 200L);
                }
                super.onFocusChanged(z, i, rect);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onFocusChanged$1() {
                AndroidUtilities.showKeyboard(SearchBox.this.input);
            }

            @Override // android.view.View
            public void invalidate() {
                if (HwEmojis.isHwEnabled()) {
                    return;
                }
                super.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            if (SelectAnimatedEmojiDialog.this.prevWindowKeyboardVisible()) {
                return;
            }
            SelectAnimatedEmojiDialog.this.onInputFocus();
            this.input.requestFocus();
            SelectAnimatedEmojiDialog.this.scrollToPosition(0, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(View view) {
            this.input.setText("");
            SelectAnimatedEmojiDialog.this.search(null, true, false);
            StickerCategoriesListView stickerCategoriesListView = this.categoriesListView;
            if (stickerCategoriesListView != null) {
                stickerCategoriesListView.selectCategory((StickerCategoriesListView.EmojiCategory) null);
                this.categoriesListView.updateCategoriesShown(true, true);
            }
            this.input.clearAnimation();
            this.input.animate().translationX(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
            showInputBoxGradient(false);
        }

        public void checkInitialization() {
            createCategoriesListView();
        }

        /* JADX WARN: Removed duplicated region for block: B:34:0x008a  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x008d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void createCategoriesListView() {
            if (this.categoriesListView != null || getContext() == null) {
                return;
            }
            int i = 2;
            if (SelectAnimatedEmojiDialog.this.type == 1 || SelectAnimatedEmojiDialog.this.type == 11 || SelectAnimatedEmojiDialog.this.type == 2 || SelectAnimatedEmojiDialog.this.type == 0 || SelectAnimatedEmojiDialog.this.type == 12 || SelectAnimatedEmojiDialog.this.type == 4 || SelectAnimatedEmojiDialog.this.type == 10 || SelectAnimatedEmojiDialog.this.type == 9 || SelectAnimatedEmojiDialog.this.type == 14) {
                int i2 = SelectAnimatedEmojiDialog.this.type;
                if (i2 != 0) {
                    if (i2 != 4) {
                        if (i2 != 12) {
                            i = 0;
                        }
                    }
                    StickerCategoriesListView stickerCategoriesListView = new StickerCategoriesListView(getContext(), i, SelectAnimatedEmojiDialog.this.resourcesProvider) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.6
                        @Override // org.telegram.ui.Components.StickerCategoriesListView
                        public void selectCategory(int i3) {
                            super.selectCategory(i3);
                            SearchBox.this.updateButton();
                        }

                        @Override // org.telegram.ui.Components.StickerCategoriesListView
                        protected boolean isTabIconsAnimationEnabled(boolean z) {
                            return LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD) || SelectAnimatedEmojiDialog.this.type == 4;
                        }
                    };
                    this.categoriesListView = stickerCategoriesListView;
                    stickerCategoriesListView.setShownButtonsAtStart(SelectAnimatedEmojiDialog.this.type != 4 ? 6.5f : 4.5f);
                    StickerCategoriesListView stickerCategoriesListView2 = this.categoriesListView;
                    TextPaint paint = this.input.getPaint();
                    stickerCategoriesListView2.setDontOccupyWidth((int) paint.measureText(((Object) this.input.getHint()) + ""));
                    this.categoriesListView.setOnScrollIntoOccupiedWidth(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda5
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            SelectAnimatedEmojiDialog.SearchBox.this.lambda$createCategoriesListView$3((Integer) obj);
                        }
                    });
                    this.categoriesListView.setOnCategoryClick(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda6
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            SelectAnimatedEmojiDialog.SearchBox.this.lambda$createCategoriesListView$4((StickerCategoriesListView.EmojiCategory) obj);
                        }
                    });
                    this.box.addView(this.categoriesListView, LayoutHelper.createFrame(-1, -1.0f, 119, 36.0f, 0.0f, 0.0f, 0.0f));
                }
                i = 1;
                StickerCategoriesListView stickerCategoriesListView3 = new StickerCategoriesListView(getContext(), i, SelectAnimatedEmojiDialog.this.resourcesProvider) { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.SearchBox.6
                    @Override // org.telegram.ui.Components.StickerCategoriesListView
                    public void selectCategory(int i3) {
                        super.selectCategory(i3);
                        SearchBox.this.updateButton();
                    }

                    @Override // org.telegram.ui.Components.StickerCategoriesListView
                    protected boolean isTabIconsAnimationEnabled(boolean z) {
                        return LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD) || SelectAnimatedEmojiDialog.this.type == 4;
                    }
                };
                this.categoriesListView = stickerCategoriesListView3;
                stickerCategoriesListView3.setShownButtonsAtStart(SelectAnimatedEmojiDialog.this.type != 4 ? 6.5f : 4.5f);
                StickerCategoriesListView stickerCategoriesListView22 = this.categoriesListView;
                TextPaint paint2 = this.input.getPaint();
                stickerCategoriesListView22.setDontOccupyWidth((int) paint2.measureText(((Object) this.input.getHint()) + ""));
                this.categoriesListView.setOnScrollIntoOccupiedWidth(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda5
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SelectAnimatedEmojiDialog.SearchBox.this.lambda$createCategoriesListView$3((Integer) obj);
                    }
                });
                this.categoriesListView.setOnCategoryClick(new Utilities.Callback() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda6
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SelectAnimatedEmojiDialog.SearchBox.this.lambda$createCategoriesListView$4((StickerCategoriesListView.EmojiCategory) obj);
                    }
                });
                this.box.addView(this.categoriesListView, LayoutHelper.createFrame(-1, -1.0f, 119, 36.0f, 0.0f, 0.0f, 0.0f));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createCategoriesListView$3(Integer num) {
            this.input.setTranslationX(-Math.max(0, num.intValue()));
            showInputBoxGradient(num.intValue() > 0);
            updateButton();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createCategoriesListView$4(StickerCategoriesListView.EmojiCategory emojiCategory) {
            if (this.categoriesListView.getSelectedCategory() == emojiCategory) {
                SelectAnimatedEmojiDialog.this.search(null, false, false);
                this.categoriesListView.selectCategory((StickerCategoriesListView.EmojiCategory) null);
                return;
            }
            SelectAnimatedEmojiDialog.this.search(emojiCategory.emojis, false, false);
            this.categoriesListView.selectCategory(emojiCategory);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void toggleClear(boolean z) {
            if (z) {
                if (this.delayedToggle == null) {
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            SelectAnimatedEmojiDialog.SearchBox.this.lambda$toggleClear$5();
                        }
                    };
                    this.delayedToggle = runnable;
                    AndroidUtilities.runOnUIThread(runnable, 340L);
                    return;
                }
                return;
            }
            Runnable runnable2 = this.delayedToggle;
            if (runnable2 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable2);
                this.delayedToggle = null;
            }
            AndroidUtilities.updateViewShow(this.clear, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$toggleClear$5() {
            AndroidUtilities.updateViewShow(this.clear, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showInputBoxGradient(boolean z) {
            if (z == this.inputBoxShown) {
                return;
            }
            this.inputBoxShown = z;
            ValueAnimator valueAnimator = this.inputBoxGradientAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.inputBoxGradientAlpha;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.inputBoxGradientAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$SearchBox$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SelectAnimatedEmojiDialog.SearchBox.this.lambda$showInputBoxGradient$6(valueAnimator2);
                }
            });
            this.inputBoxGradientAnimator.setDuration(120L);
            this.inputBoxGradientAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.inputBoxGradientAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showInputBoxGradient$6(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.inputBoxGradientAlpha = floatValue;
            View view = this.inputBoxGradient;
            if (view != null) {
                view.setAlpha(floatValue);
                return;
            }
            FrameLayout frameLayout = this.inputBox;
            if (frameLayout != null) {
                frameLayout.invalidate();
            }
        }

        public boolean isInProgress() {
            return this.searchStateDrawable.getIconState() == 2;
        }

        public void showProgress(boolean z) {
            if (z) {
                this.searchStateDrawable.setIconState(2);
            } else {
                updateButton(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateButton() {
            updateButton(false);
        }

        private void updateButton(boolean z) {
            StickerCategoriesListView stickerCategoriesListView;
            StickerCategoriesListView stickerCategoriesListView2;
            if (!isInProgress() || ((this.input.length() == 0 && ((stickerCategoriesListView2 = this.categoriesListView) == null || stickerCategoriesListView2.getSelectedCategory() == null)) || z)) {
                this.searchStateDrawable.setIconState((this.input.length() > 0 || ((stickerCategoriesListView = this.categoriesListView) != null && stickerCategoriesListView.isCategoriesShown() && (this.categoriesListView.isScrolledIntoOccupiedWidth() || this.categoriesListView.getSelectedCategory() != null))) ? 1 : 0);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0f), 1073741824));
        }

        @Override // android.view.View
        public void invalidate() {
            if (HwEmojis.grab(this)) {
                return;
            }
            super.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShow(float f) {
        if (this.bubble1View != null) {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(MathUtils.clamp((((f * 800.0f) - 0.0f) / 120.0f) / 1.0f, 0.0f, 1.0f));
            this.bubble1View.setAlpha(interpolation);
            this.bubble1View.setScaleX(interpolation);
            this.bubble1View.setScaleY(interpolation * (isBottom() ? -1 : 1));
        }
        if (this.bubble2View != null) {
            float clamp = MathUtils.clamp((((f * 800.0f) - 30.0f) / 120.0f) / 1.0f, 0.0f, 1.0f);
            this.bubble2View.setAlpha(clamp);
            this.bubble2View.setScaleX(clamp);
            this.bubble2View.setScaleY(clamp * (isBottom() ? -1 : 1));
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
        this.backgroundView.setAlpha(clamp5);
        this.searchBox.setAlpha(clamp5);
        for (int i = 0; i < this.emojiTabs.contentView.getChildCount(); i++) {
            this.emojiTabs.contentView.getChildAt(i).setAlpha(clamp5);
        }
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
        if (Build.VERSION.SDK_INT >= 21) {
            this.contentView.invalidateOutline();
        } else {
            this.backgroundView.setVisibility(8);
            this.contentView.setAlpha(clamp5);
            this.contentView.invalidate();
        }
        View view2 = this.bubble2View;
        if (view2 != null) {
            view2.setAlpha(clamp5);
        }
        this.emojiTabsShadow.setAlpha(clamp5);
        this.emojiTabsShadow.setScaleX(Math.min(this.scaleX, 1.0f));
        float pivotX = this.emojiTabsShadow.getPivotX();
        double d = pivotX * pivotX;
        double pow = Math.pow(this.contentView.getHeight(), 2.0d);
        Double.isNaN(d);
        float sqrt = (float) Math.sqrt(Math.max(d + pow, Math.pow(this.contentView.getWidth() - pivotX, 2.0d) + Math.pow(this.contentView.getHeight(), 2.0d)));
        for (int i2 = 0; i2 < this.emojiTabs.contentView.getChildCount(); i2++) {
            View childAt = this.emojiTabs.contentView.getChildAt(i2);
            if (f == 0.0f) {
                childAt.setLayerType(2, null);
            } else if (f == 1.0f) {
                childAt.setLayerType(0, null);
            }
            float left = (childAt.getLeft() + (childAt.getWidth() / 2.0f)) - pivotX;
            float top = childAt.getTop() + (childAt.getHeight() / 2.0f);
            if (isBottom()) {
                top = getMeasuredHeight() - top;
            }
            float cascade = AndroidUtilities.cascade(clamp4, (float) Math.sqrt((left * left) + (top * top * 0.4f)), sqrt, childAt.getHeight() * 1.75f);
            if (Float.isNaN(cascade)) {
                cascade = 0.0f;
            }
            childAt.setScaleX(cascade);
            childAt.setScaleY(cascade);
        }
        for (int i3 = 0; i3 < this.emojiGridView.getChildCount(); i3++) {
            View childAt2 = this.emojiGridView.getChildAt(i3);
            if (childAt2 instanceof ImageViewEmoji) {
                ImageViewEmoji imageViewEmoji = (ImageViewEmoji) childAt2;
                float left2 = (childAt2.getLeft() + (childAt2.getWidth() / 2.0f)) - pivotX;
                float top2 = childAt2.getTop() + (childAt2.getHeight() / 2.0f);
                if (isBottom()) {
                    top2 = getMeasuredHeight() - top2;
                }
                float cascade2 = AndroidUtilities.cascade(clamp4, (float) Math.sqrt((left2 * left2) + (top2 * top2 * 0.2f)), sqrt, childAt2.getHeight() * 1.75f);
                if (Float.isNaN(cascade2)) {
                    cascade2 = 0.0f;
                }
                imageViewEmoji.setAnimatedScale(cascade2);
            }
        }
        this.emojiGridViewContainer.invalidate();
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
                SelectAnimatedEmojiDialog.this.lambda$onDismiss$39(valueAnimator2);
            }
        });
        this.hideAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog.26
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
                if (SelectAnimatedEmojiDialog.this.selectStatusDateDialog != null) {
                    SelectAnimatedEmojiDialog.this.selectStatusDateDialog.dismiss();
                    SelectAnimatedEmojiDialog.this.selectStatusDateDialog = null;
                }
            }
        });
        this.hideAnimator.setDuration(200L);
        this.hideAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.hideAnimator.start();
        SearchBox searchBox = this.searchBox;
        if (searchBox != null) {
            AndroidUtilities.hideKeyboard(searchBox.input);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDismiss$39(ValueAnimator valueAnimator) {
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
        if (Build.VERSION.SDK_INT >= 21) {
            this.contentView.setClipToOutline(z);
        }
        if (!z) {
            this.backgroundView.setVisibility(8);
        } else {
            this.backgroundView.setVisibility(0);
        }
    }

    public void setRecentReactions(List<ReactionsLayoutInBubble.VisibleReaction> list) {
        this.recentReactionsToSet = list;
        updateRows(false, true);
    }

    public void resetBackgroundBitmaps() {
        for (int i = 0; i < this.emojiGridView.lineDrawables.size(); i++) {
            EmojiListView.DrawingInBackgroundLine drawingInBackgroundLine = this.emojiGridView.lineDrawables.get(i);
            for (int i2 = 0; i2 < drawingInBackgroundLine.imageViewEmojis.size(); i2++) {
                if (drawingInBackgroundLine.imageViewEmojis.get(i2).notDraw) {
                    drawingInBackgroundLine.imageViewEmojis.get(i2).notDraw = false;
                    drawingInBackgroundLine.imageViewEmojis.get(i2).invalidate();
                    drawingInBackgroundLine.reset();
                }
            }
        }
        this.emojiGridView.invalidate();
        for (int i3 = 0; i3 < this.emojiSearchGridView.lineDrawables.size(); i3++) {
            EmojiListView.DrawingInBackgroundLine drawingInBackgroundLine2 = this.emojiSearchGridView.lineDrawables.get(i3);
            for (int i4 = 0; i4 < drawingInBackgroundLine2.imageViewEmojis.size(); i4++) {
                if (drawingInBackgroundLine2.imageViewEmojis.get(i4).notDraw) {
                    drawingInBackgroundLine2.imageViewEmojis.get(i4).notDraw = false;
                    drawingInBackgroundLine2.imageViewEmojis.get(i4).invalidate();
                    drawingInBackgroundLine2.reset();
                }
            }
        }
        this.emojiSearchGridView.invalidate();
    }

    public void setSelected(Long l) {
        this.selectedDocumentIds.clear();
        this.selectedDocumentIds.add(l);
        if (this.emojiGridView != null) {
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                if (this.emojiGridView.getChildAt(i) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) this.emojiGridView.getChildAt(i);
                    AnimatedEmojiSpan animatedEmojiSpan = imageViewEmoji.span;
                    if (animatedEmojiSpan != null) {
                        imageViewEmoji.setViewSelected(this.selectedDocumentIds.contains(Long.valueOf(animatedEmojiSpan.getDocumentId())), true);
                    } else {
                        imageViewEmoji.setViewSelected(this.selectedDocumentIds.contains(0L), true);
                    }
                }
            }
            this.emojiGridView.invalidate();
        }
    }

    public void setMultiSelected(Long l, boolean z) {
        boolean z2;
        if (!this.selectedDocumentIds.contains(l)) {
            this.selectedDocumentIds.add(l);
            z2 = true;
        } else {
            this.selectedDocumentIds.remove(l);
            z2 = false;
        }
        if (this.emojiGridView != null) {
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                if (this.emojiGridView.getChildAt(i) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) this.emojiGridView.getChildAt(i);
                    AnimatedEmojiSpan animatedEmojiSpan = imageViewEmoji.span;
                    if (animatedEmojiSpan != null && animatedEmojiSpan.getDocumentId() == l.longValue()) {
                        imageViewEmoji.setViewSelectedWithScale(z2, z);
                    } else {
                        TLRPC$Document tLRPC$Document = imageViewEmoji.document;
                        if (tLRPC$Document != null && tLRPC$Document.id == l.longValue()) {
                            imageViewEmoji.setViewSelectedWithScale(z2, z);
                        }
                    }
                }
            }
            this.emojiGridView.invalidate();
        }
    }

    public boolean unselect(Long l) {
        this.selectedDocumentIds.remove(l);
        if (this.emojiGridView != null) {
            boolean z = false;
            for (int i = 0; i < this.emojiGridView.getChildCount(); i++) {
                if (this.emojiGridView.getChildAt(i) instanceof ImageViewEmoji) {
                    ImageViewEmoji imageViewEmoji = (ImageViewEmoji) this.emojiGridView.getChildAt(i);
                    AnimatedEmojiSpan animatedEmojiSpan = imageViewEmoji.span;
                    if (animatedEmojiSpan != null && animatedEmojiSpan.getDocumentId() == l.longValue()) {
                        imageViewEmoji.unselectWithScale();
                    } else {
                        TLRPC$Document tLRPC$Document = imageViewEmoji.document;
                        if (tLRPC$Document != null && tLRPC$Document.id == l.longValue()) {
                            imageViewEmoji.unselectWithScale();
                        }
                    }
                    z = true;
                }
            }
            this.emojiGridView.invalidate();
            if (!z) {
                for (int i2 = 0; i2 < this.rowHashCodes.size(); i2++) {
                    long longValue = this.rowHashCodes.get(i2).longValue();
                    if (longValue == (l.longValue() * 13) + 62425 || longValue == (l.longValue() * 13) + 3212) {
                        Adapter adapter = this.adapter;
                        if (adapter != null) {
                            adapter.notifyItemChanged(i2);
                        }
                        return true;
                    }
                }
            }
            return z;
        }
        return false;
    }

    public void clearSelectedDocuments() {
        this.selectedDocumentIds.clear();
    }

    public void setScrimDrawable(AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable, View view) {
        this.scrimColor = (swapAnimatedEmojiDrawable == null || swapAnimatedEmojiDrawable.getColor() == null) ? 0 : swapAnimatedEmojiDrawable.getColor().intValue();
        this.scrimDrawable = swapAnimatedEmojiDrawable;
        this.scrimDrawableParent = view;
        if (this.isAttached && swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.setSecondParent(this);
        }
        invalidate();
    }

    public void drawBigReaction(Canvas canvas, View view) {
        if (this.selectedReactionView == null) {
            return;
        }
        this.bigReactionImageReceiver.setParentView(view);
        ImageViewEmoji imageViewEmoji = this.selectedReactionView;
        if (imageViewEmoji != null) {
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
            canvas.translate(this.emojiGridView.getX() + this.selectedReactionView.getX(), this.gridViewContainer.getY() + this.emojiGridView.getY() + this.selectedReactionView.getY());
            this.paint.setColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, this.resourcesProvider));
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
    }

    public void setSaveState(int i) {
        this.listStateId = Integer.valueOf(i);
    }

    public void setOnLongPressedListener(onLongPressedListener onlongpressedlistener) {
        this.bigReactionListener = onlongpressedlistener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SelectStatusDurationDialog extends Dialog {
        private Bitmap blurBitmap;
        private Paint blurBitmapPaint;
        private boolean changeToScrimColor;
        private int clipBottom;
        private ContentView contentView;
        private Rect current;
        private BottomSheet dateBottomSheet;
        private boolean dismissed;
        private boolean done;
        private View emojiPreviewView;
        private Rect from;
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
        private int[] tempLocation;
        private Rect to;

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
        /* loaded from: classes4.dex */
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
                            drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(SelectAnimatedEmojiDialog.this.scrimColor, SelectAnimatedEmojiDialog.this.accentColor, SelectStatusDurationDialog.this.showT), PorterDuff.Mode.MULTIPLY));
                        }
                        drawable.setAlpha((int) ((1.0f - SelectStatusDurationDialog.this.showT) * 255.0f));
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(SelectStatusDurationDialog.this.current);
                        float max = (SelectStatusDurationDialog.this.imageViewEmoji.pressedProgress != 0.0f || SelectStatusDurationDialog.this.imageViewEmoji.selectedProgress > 0.0f) ? (((1.0f - Math.max(SelectStatusDurationDialog.this.imageViewEmoji.selectedProgress * 0.8f, SelectStatusDurationDialog.this.imageViewEmoji.pressedProgress)) * 0.2f) + 0.8f) * 1.0f : 1.0f;
                        Rect rect = AndroidUtilities.rectTmp2;
                        rect.set((int) (rectF.centerX() - ((rectF.width() / 2.0f) * max)), (int) (rectF.centerY() - ((rectF.height() / 2.0f) * max)), (int) (rectF.centerX() + ((rectF.width() / 2.0f) * max)), (int) (rectF.centerY() + ((rectF.height() / 2.0f) * max)));
                        float f = 1.0f - ((1.0f - SelectStatusDurationDialog.this.imageViewEmoji.skewAlpha) * (1.0f - SelectStatusDurationDialog.this.showT));
                        canvas.save();
                        if (f < 1.0f) {
                            canvas.translate(rect.left, rect.top);
                            canvas.scale(1.0f, f, 0.0f, 0.0f);
                            canvas.skew((1.0f - ((SelectStatusDurationDialog.this.imageViewEmoji.skewIndex * 2.0f) / 8.0f)) * (1.0f - f), 0.0f);
                            canvas.translate(-rect.left, -rect.top);
                        }
                        canvas.clipRect(0.0f, 0.0f, getWidth(), SelectStatusDurationDialog.this.clipBottom + (SelectStatusDurationDialog.this.showT * AndroidUtilities.dp(45.0f)));
                        drawable.setBounds(rect);
                        drawable.draw(canvas);
                        canvas.restore();
                        if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != 0) {
                            if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != 1) {
                                if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex != 6) {
                                    if (SelectStatusDurationDialog.this.imageViewEmoji.skewIndex == 7) {
                                        rect.offset(AndroidUtilities.dp(f * (-8.0f)), 0);
                                    }
                                } else {
                                    rect.offset(-AndroidUtilities.dp(f * (-4.0f)), 0);
                                }
                            } else {
                                rect.offset(AndroidUtilities.dp(f * 4.0f), 0);
                            }
                        } else {
                            rect.offset(AndroidUtilities.dp(f * 8.0f), 0);
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
            String str;
            ImageLocation forDocument;
            this.from = new Rect();
            this.to = new Rect();
            this.current = new Rect();
            this.tempLocation = new int[2];
            this.done = false;
            this.dismissed = false;
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
                int i3 = Build.VERSION.SDK_INT;
                if (i3 >= 21) {
                    attributes.flags = i2 | (-2147417856);
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
                if (i3 >= 28) {
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
                SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document.thumbs, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                if ("video/webm".equals(tLRPC$Document.mime_type)) {
                    ImageLocation forDocument2 = ImageLocation.getForDocument(tLRPC$Document);
                    String str2 = "160_160_" + ImageLoader.AUTOPLAY_FILTER;
                    if (svgThumb != null) {
                        svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                    }
                    forDocument = forDocument2;
                    str = str2;
                } else {
                    if (svgThumb != null && MessageObject.isAnimatedStickerDocument(tLRPC$Document, false)) {
                        svgThumb.overrideWidthAndHeight(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS);
                    }
                    str = "160_160";
                    forDocument = ImageLocation.getForDocument(tLRPC$Document);
                }
                this.imageReceiver.setImage(forDocument, str, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document), "160_160", null, null, svgThumb, tLRPC$Document.size, null, tLRPC$Document, 1);
                if ((imageViewEmoji.drawable instanceof AnimatedEmojiDrawable) && (MessageObject.isTextColorEmoji(tLRPC$Document) || ((AnimatedEmojiDrawable) imageViewEmoji.drawable).canOverrideColor())) {
                    this.imageReceiver.setColorFilter((MessageObject.isTextColorEmoji(tLRPC$Document) || AnimatedEmojiDrawable.isDefaultStatusEmoji((AnimatedEmojiDrawable) imageViewEmoji.drawable)) ? SelectAnimatedEmojiDialog.this.premiumStarColorFilter : Theme.getAnimatedEmojiColorFilter(resourcesProvider));
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
            int i4 = iArr[1];
            this.parentDialogY = i4;
            this.clipBottom = i4 + view.getHeight();
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
            if (this.done) {
                return;
            }
            this.done = true;
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
            try {
                super.dismiss();
            } catch (Exception unused) {
            }
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
            canvas.drawColor(Theme.getColor(Theme.key_windowBackgroundWhite));
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
                if (runnable != null) {
                    runnable.run();
                    return;
                }
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
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 4);
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
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 4);
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
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 4);
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

    public void setForumIconDrawable(Drawable drawable) {
        this.forumIconDrawable = drawable;
        ImageViewEmoji imageViewEmoji = this.forumIconImage;
        if (imageViewEmoji != null) {
            imageViewEmoji.imageReceiver.setImageBitmap(drawable);
        }
    }

    public void setAnimationsEnabled(boolean z) {
        this.animationsEnabled = z;
    }

    public void setEnterAnimationInProgress(boolean z) {
        if (this.enterAnimationInProgress != z) {
            this.enterAnimationInProgress = z;
            if (z) {
                return;
            }
            AndroidUtilities.forEachViews((RecyclerView) this.emojiGridView, (com.google.android.exoplayer2.util.Consumer<View>) new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.SelectAnimatedEmojiDialog$$ExternalSyntheticLambda11
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    SelectAnimatedEmojiDialog.lambda$setEnterAnimationInProgress$40((View) obj);
                }
            });
            for (int i = 0; i < this.emojiTabs.contentView.getChildCount(); i++) {
                View childAt = this.emojiTabs.contentView.getChildAt(i);
                childAt.setScaleX(1.0f);
                childAt.setScaleY(1.0f);
            }
            this.emojiTabs.contentView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setEnterAnimationInProgress$40(View view) {
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
    }

    public void setBackgroundDelegate(BackgroundDelegate backgroundDelegate) {
        this.backgroundDelegate = backgroundDelegate;
    }

    /* loaded from: classes4.dex */
    public static class SetTitleDocument extends TLRPC$Document {
        public final String title;

        public SetTitleDocument(String str) {
            this.title = str;
        }
    }

    private ArrayList<TLRPC$Document> filter(ArrayList<TLRPC$Document> arrayList, HashSet<Long> hashSet) {
        if (hashSet == null) {
            return arrayList;
        }
        int i = 0;
        while (i < arrayList.size()) {
            TLRPC$Document tLRPC$Document = arrayList.get(i);
            if (tLRPC$Document == null || hashSet.contains(Long.valueOf(tLRPC$Document.id))) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        return arrayList;
    }
}
