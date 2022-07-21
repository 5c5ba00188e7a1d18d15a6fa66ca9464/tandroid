package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getStickers;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_stickers;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Cells.StickerSetNameCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTabStrip;
import org.telegram.ui.ContentPreviewViewer;
/* loaded from: classes3.dex */
public class StickerMasksAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private FrameLayout bottomTabContainer;
    private StickerMasksAlertDelegate delegate;
    private RecyclerListView gridView;
    private String[] lastSearchKeyboardLanguage;
    private ImageView masksButton;
    private int scrollOffsetY;
    private Drawable shadowDrawable;
    private View shadowLine;
    private Drawable[] stickerIcons;
    private ImageView stickersButton;
    private StickersGridAdapter stickersGridAdapter;
    private GridLayoutManager stickersLayoutManager;
    private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
    private SearchField stickersSearchField;
    private StickersSearchGridAdapter stickersSearchGridAdapter;
    private ScrollSlidingTabStrip stickersTab;
    private int stickersTabOffset;
    private int currentAccount = UserConfig.selectedAccount;
    private ArrayList<TLRPC$TL_messages_stickerSet>[] stickerSets = {new ArrayList<>(), new ArrayList<>()};
    private ArrayList<TLRPC$Document>[] recentStickers = {new ArrayList<>(), new ArrayList<>()};
    private ArrayList<TLRPC$Document> favouriteStickers = new ArrayList<>();
    private int recentTabBum = -2;
    private int favTabBum = -2;
    private ContentPreviewViewer.ContentPreviewViewerDelegate contentPreviewViewerDelegate = new AnonymousClass1();
    private int currentType = 0;
    private int searchFieldHeight = AndroidUtilities.dp(64.0f);

    /* loaded from: classes3.dex */
    public interface StickerMasksAlertDelegate {
        void onStickerSelected(Object obj, TLRPC$Document tLRPC$Document);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements ContentPreviewViewer.ContentPreviewViewerDelegate {
        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean can() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$can(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean canSchedule() {
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public long getDialogId() {
            return 0L;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ String getQuery(boolean z) {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$getQuery(this, z);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void gifAddedOrDeleted() {
            ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$gifAddedOrDeleted(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean isInScheduleMode() {
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean needMenu() {
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needOpen() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$needOpen(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needRemove() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$needRemove(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean needSend() {
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void openSet(TLRPC$InputStickerSet tLRPC$InputStickerSet, boolean z) {
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void remove(SendMessagesHelper.ImportingSticker importingSticker) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$remove(this, importingSticker);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void sendGif(Object obj, Object obj2, boolean z, int i) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.CC.$default$sendGif(this, obj, obj2, z, i);
        }

        AnonymousClass1() {
            StickerMasksAlert.this = r1;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void sendSticker(TLRPC$Document tLRPC$Document, String str, Object obj, boolean z, int i) {
            StickerMasksAlert.this.delegate.onStickerSelected(obj, tLRPC$Document);
        }
    }

    /* loaded from: classes3.dex */
    public class SearchField extends FrameLayout {
        private ImageView clearSearchImageView;
        private CloseProgressDrawable2 progressDrawable;
        private EditTextBoldCursor searchEditText;
        private AnimatorSet shadowAnimator;
        private View shadowView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SearchField(Context context, int i) {
            super(context);
            StickerMasksAlert.this = r19;
            View view = new View(context);
            this.shadowView = view;
            view.setAlpha(0.0f);
            this.shadowView.setTag(1);
            this.shadowView.setBackgroundColor(301989888);
            addView(this.shadowView, new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83));
            View view2 = new View(context);
            view2.setBackgroundColor(-14342875);
            addView(view2, new FrameLayout.LayoutParams(-1, r19.searchFieldHeight));
            View view3 = new View(context);
            view3.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), -13224394));
            addView(view3, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 14.0f, 14.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(2131166153);
            imageView.setColorFilter(new PorterDuffColorFilter(-8947849, PorterDuff.Mode.MULTIPLY));
            addView(imageView, LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 14.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.clearSearchImageView = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            ImageView imageView3 = this.clearSearchImageView;
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, r19);
            this.progressDrawable = anonymousClass1;
            imageView3.setImageDrawable(anonymousClass1);
            this.progressDrawable.setSide(AndroidUtilities.dp(7.0f));
            this.clearSearchImageView.setScaleX(0.1f);
            this.clearSearchImageView.setScaleY(0.1f);
            this.clearSearchImageView.setAlpha(0.0f);
            addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 14.0f, 14.0f, 0.0f));
            this.clearSearchImageView.setOnClickListener(new StickerMasksAlert$SearchField$$ExternalSyntheticLambda0(this));
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r19);
            this.searchEditText = anonymousClass2;
            anonymousClass2.setTextSize(1, 16.0f);
            this.searchEditText.setHintTextColor(-8947849);
            this.searchEditText.setTextColor(-1);
            this.searchEditText.setBackgroundDrawable(null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            if (i == 0) {
                this.searchEditText.setHint(LocaleController.getString("SearchStickersHint", 2131628185));
            } else if (i == 1) {
                this.searchEditText.setHint(LocaleController.getString("SearchEmojiHint", 2131628159));
            } else if (i == 2) {
                this.searchEditText.setHint(LocaleController.getString("SearchGifsTitle", 2131628175));
            }
            this.searchEditText.setCursorColor(-1);
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 12.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener(new AnonymousClass3(r19));
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$SearchField$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends CloseProgressDrawable2 {
            @Override // org.telegram.ui.Components.CloseProgressDrawable2
            public int getCurrentColor() {
                return -8947849;
            }

            AnonymousClass1(SearchField searchField, StickerMasksAlert stickerMasksAlert) {
            }
        }

        public /* synthetic */ void lambda$new$0(View view) {
            this.searchEditText.setText("");
            AndroidUtilities.showKeyboard(this.searchEditText);
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$SearchField$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends EditTextBoldCursor {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, StickerMasksAlert stickerMasksAlert) {
                super(context);
                SearchField.this = r1;
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    SearchField.this.searchEditText.requestFocus();
                    AndroidUtilities.showKeyboard(SearchField.this.searchEditText);
                }
                return super.onTouchEvent(motionEvent);
            }
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$SearchField$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 implements TextWatcher {
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass3(StickerMasksAlert stickerMasksAlert) {
                SearchField.this = r1;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                boolean z = true;
                boolean z2 = SearchField.this.searchEditText.length() > 0;
                float f = 0.0f;
                if (SearchField.this.clearSearchImageView.getAlpha() == 0.0f) {
                    z = false;
                }
                if (z2 != z) {
                    ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                    float f2 = 1.0f;
                    if (z2) {
                        f = 1.0f;
                    }
                    ViewPropertyAnimator scaleX = animate.alpha(f).setDuration(150L).scaleX(z2 ? 1.0f : 0.1f);
                    if (!z2) {
                        f2 = 0.1f;
                    }
                    scaleX.scaleY(f2).start();
                }
                StickerMasksAlert.this.stickersSearchGridAdapter.search(SearchField.this.searchEditText.getText().toString());
            }
        }

        public void hideKeyboard() {
            AndroidUtilities.hideKeyboard(this.searchEditText);
        }

        public void showShadow(boolean z, boolean z2) {
            if (!z || this.shadowView.getTag() != null) {
                if (!z && this.shadowView.getTag() != null) {
                    return;
                }
                AnimatorSet animatorSet = this.shadowAnimator;
                Integer num = null;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.shadowAnimator = null;
                }
                View view = this.shadowView;
                if (!z) {
                    num = 1;
                }
                view.setTag(num);
                float f = 1.0f;
                if (z2) {
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.shadowAnimator = animatorSet2;
                    Animator[] animatorArr = new Animator[1];
                    View view2 = this.shadowView;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    if (!z) {
                        f = 0.0f;
                    }
                    fArr[0] = f;
                    animatorArr[0] = ObjectAnimator.ofFloat(view2, property, fArr);
                    animatorSet2.playTogether(animatorArr);
                    this.shadowAnimator.setDuration(200L);
                    this.shadowAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                    this.shadowAnimator.addListener(new AnonymousClass4());
                    this.shadowAnimator.start();
                    return;
                }
                View view3 = this.shadowView;
                if (!z) {
                    f = 0.0f;
                }
                view3.setAlpha(f);
            }
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$SearchField$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 extends AnimatorListenerAdapter {
            AnonymousClass4() {
                SearchField.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SearchField.this.shadowAnimator = null;
            }
        }
    }

    public StickerMasksAlert(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        this.behindKeyboardColorKey = null;
        this.behindKeyboardColor = -14342875;
        this.useLightStatusBar = false;
        fixNavigationBar(-14342875);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoad);
        MediaDataController.getInstance(this.currentAccount).loadRecents(0, false, true, false);
        MediaDataController.getInstance(this.currentAccount).loadRecents(1, false, true, false);
        MediaDataController.getInstance(this.currentAccount).loadRecents(2, false, true, false);
        Drawable mutate = context.getResources().getDrawable(2131166143).mutate();
        this.shadowDrawable = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(-14342875, PorterDuff.Mode.MULTIPLY));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.containerView = anonymousClass2;
        anonymousClass2.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i = this.backgroundPaddingLeft;
        viewGroup.setPadding(i, 0, i, 0);
        this.stickerIcons = new Drawable[]{Theme.createEmojiIconSelectorDrawable(context, 2131166174, -11842741, -9520403), Theme.createEmojiIconSelectorDrawable(context, 2131166172, -11842741, -9520403)};
        MediaDataController.getInstance(this.currentAccount).checkStickers(0);
        MediaDataController.getInstance(this.currentAccount).checkStickers(1);
        MediaDataController.getInstance(this.currentAccount).checkFeaturedStickers();
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.gridView = anonymousClass3;
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, 5);
        this.stickersLayoutManager = anonymousClass4;
        anonymousClass3.setLayoutManager(anonymousClass4);
        this.stickersLayoutManager.setSpanSizeLookup(new AnonymousClass5());
        this.gridView.setPadding(0, AndroidUtilities.dp(52.0f), 0, AndroidUtilities.dp(48.0f));
        this.gridView.setClipToPadding(false);
        this.gridView.setHorizontalScrollBarEnabled(false);
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.setGlowColor(-14342875);
        this.stickersSearchGridAdapter = new StickersSearchGridAdapter(context);
        RecyclerListView recyclerListView = this.gridView;
        StickersGridAdapter stickersGridAdapter = new StickersGridAdapter(context);
        this.stickersGridAdapter = stickersGridAdapter;
        recyclerListView.setAdapter(stickersGridAdapter);
        this.gridView.setOnTouchListener(new StickerMasksAlert$$ExternalSyntheticLambda2(this, resourcesProvider));
        StickerMasksAlert$$ExternalSyntheticLambda3 stickerMasksAlert$$ExternalSyntheticLambda3 = new StickerMasksAlert$$ExternalSyntheticLambda3(this);
        this.stickersOnItemClickListener = stickerMasksAlert$$ExternalSyntheticLambda3;
        this.gridView.setOnItemClickListener(stickerMasksAlert$$ExternalSyntheticLambda3);
        this.containerView.addView(this.gridView, LayoutHelper.createFrame(-1, -1.0f));
        this.stickersTab = new AnonymousClass6(this, context, resourcesProvider);
        SearchField searchField = new SearchField(context, 0);
        this.stickersSearchField = searchField;
        this.containerView.addView(searchField, new FrameLayout.LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
        this.stickersTab.setType(ScrollSlidingTabStrip.Type.TAB);
        this.stickersTab.setUnderlineHeight(AndroidUtilities.getShadowHeight());
        this.stickersTab.setIndicatorColor(-9520403);
        this.stickersTab.setUnderlineColor(-16053493);
        this.stickersTab.setBackgroundColor(-14342875);
        this.containerView.addView(this.stickersTab, LayoutHelper.createFrame(-1, 36, 51));
        this.stickersTab.setDelegate(new StickerMasksAlert$$ExternalSyntheticLambda4(this));
        this.gridView.setOnScrollListener(new AnonymousClass7());
        View view = new View(context);
        view.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, -1907225));
        this.containerView.addView(view, LayoutHelper.createFrame(-1, 6.0f));
        if (!z) {
            this.bottomTabContainer = new AnonymousClass8(this, context);
            View view2 = new View(context);
            this.shadowLine = view2;
            view2.setBackgroundColor(301989888);
            this.bottomTabContainer.addView(this.shadowLine, new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight()));
            View view3 = new View(context);
            view3.setBackgroundColor(-14342875);
            this.bottomTabContainer.addView(view3, new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(48.0f), 83));
            this.containerView.addView(this.bottomTabContainer, new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(48.0f) + AndroidUtilities.getShadowHeight(), 83));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            this.bottomTabContainer.addView(linearLayout, LayoutHelper.createFrame(-2, 48, 81));
            AnonymousClass9 anonymousClass9 = new AnonymousClass9(this, context);
            this.stickersButton = anonymousClass9;
            anonymousClass9.setScaleType(ImageView.ScaleType.CENTER);
            this.stickersButton.setImageDrawable(Theme.createEmojiIconSelectorDrawable(context, 2131166160, -1, -9520403));
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 21) {
                RippleDrawable rippleDrawable = (RippleDrawable) Theme.createSelectorDrawable(520093695);
                Theme.setRippleDrawableForceSoftware(rippleDrawable);
                this.stickersButton.setBackground(rippleDrawable);
            }
            linearLayout.addView(this.stickersButton, LayoutHelper.createLinear(70, 48));
            this.stickersButton.setOnClickListener(new StickerMasksAlert$$ExternalSyntheticLambda1(this));
            AnonymousClass10 anonymousClass10 = new AnonymousClass10(this, context);
            this.masksButton = anonymousClass10;
            anonymousClass10.setScaleType(ImageView.ScaleType.CENTER);
            this.masksButton.setImageDrawable(Theme.createEmojiIconSelectorDrawable(context, 2131165483, -1, -9520403));
            if (i2 >= 21) {
                RippleDrawable rippleDrawable2 = (RippleDrawable) Theme.createSelectorDrawable(520093695);
                Theme.setRippleDrawableForceSoftware(rippleDrawable2);
                this.masksButton.setBackground(rippleDrawable2);
            }
            linearLayout.addView(this.masksButton, LayoutHelper.createLinear(70, 48));
            this.masksButton.setOnClickListener(new StickerMasksAlert$$ExternalSyntheticLambda0(this));
        }
        checkDocuments(true);
        reloadStickersAdapter();
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends SizeNotifierFrameLayout {
        private long lastUpdateTime;
        private float statusBarProgress;
        private boolean ignoreLayout = false;
        private RectF rect = new RectF();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            StickerMasksAlert.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int size = View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21 && !((BottomSheet) StickerMasksAlert.this).isFullscreen) {
                this.ignoreLayout = true;
                setPadding(((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, 0);
                this.ignoreLayout = false;
            }
            int paddingTop = size - getPaddingTop();
            if (measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                this.statusBarProgress = 1.0f;
                i3 = 0;
            } else {
                i3 = (paddingTop - ((paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f);
            }
            if (StickerMasksAlert.this.gridView.getPaddingTop() != i3) {
                this.ignoreLayout = true;
                StickerMasksAlert.this.gridView.setPinnedSectionOffsetY(-i3);
                StickerMasksAlert.this.gridView.setPadding(0, i3, 0, AndroidUtilities.dp(48.0f));
                this.ignoreLayout = false;
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            StickerMasksAlert.this.updateLayout(false);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && StickerMasksAlert.this.scrollOffsetY != 0 && motionEvent.getY() < StickerMasksAlert.this.scrollOffsetY + AndroidUtilities.dp(12.0f)) {
                StickerMasksAlert.this.dismiss();
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !StickerMasksAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            int dp = AndroidUtilities.dp(13.0f);
            int i = (StickerMasksAlert.this.scrollOffsetY - ((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop) - dp;
            if (((BottomSheet) StickerMasksAlert.this).currentSheetAnimationType == 1) {
                i = (int) (i + StickerMasksAlert.this.gridView.getTranslationY());
            }
            int dp2 = AndroidUtilities.dp(20.0f) + i;
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop;
            int dp3 = AndroidUtilities.dp(12.0f);
            if (((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop + i < dp3) {
                float dp4 = dp + AndroidUtilities.dp(4.0f);
                float min = Math.min(1.0f, ((dp3 - i) - ((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop) / dp4);
                int i2 = (int) ((dp3 - dp4) * min);
                i -= i2;
                dp2 -= i2;
                measuredHeight += i2;
                f = 1.0f - min;
            } else {
                f = 1.0f;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                int i3 = AndroidUtilities.statusBarHeight;
                i += i3;
                dp2 += i3;
            }
            StickerMasksAlert.this.shadowDrawable.setBounds(0, i, getMeasuredWidth(), measuredHeight);
            StickerMasksAlert.this.shadowDrawable.draw(canvas);
            if (f != 1.0f) {
                Theme.dialogs_onlineCirclePaint.setColor(-14342875);
                this.rect.set(((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, ((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop + i, getMeasuredWidth() - ((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, ((BottomSheet) StickerMasksAlert.this).backgroundPaddingTop + i + AndroidUtilities.dp(24.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            if (j > 18) {
                j = 18;
            }
            this.lastUpdateTime = elapsedRealtime;
            if (f > 0.0f) {
                int dp5 = AndroidUtilities.dp(36.0f);
                this.rect.set((getMeasuredWidth() - dp5) / 2, dp2, (getMeasuredWidth() + dp5) / 2, dp2 + AndroidUtilities.dp(4.0f));
                int alpha = Color.alpha(-11842741);
                Theme.dialogs_onlineCirclePaint.setColor(-11842741);
                Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha * 1.0f * f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                float f2 = this.statusBarProgress;
                if (f2 > 0.0f) {
                    float f3 = f2 - (((float) j) / 180.0f);
                    this.statusBarProgress = f3;
                    if (f3 < 0.0f) {
                        this.statusBarProgress = 0.0f;
                    } else {
                        invalidate();
                    }
                }
            } else {
                float f4 = this.statusBarProgress;
                if (f4 < 1.0f) {
                    float f5 = f4 + (((float) j) / 180.0f);
                    this.statusBarProgress = f5;
                    if (f5 > 1.0f) {
                        this.statusBarProgress = 1.0f;
                    } else {
                        invalidate();
                    }
                }
            }
            Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int) (this.statusBarProgress * 255.0f), (int) (Color.red(-14342875) * 0.8f), (int) (Color.green(-14342875) * 0.8f), (int) (Color.blue(-14342875) * 0.8f)));
            canvas.drawRect(((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) StickerMasksAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
        }
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            StickerMasksAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return f2 >= ((float) (StickerMasksAlert.this.scrollOffsetY + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return super.onInterceptTouchEvent(motionEvent) || ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, StickerMasksAlert.this.gridView, ((BottomSheet) StickerMasksAlert.this).containerView.getMeasuredHeight(), StickerMasksAlert.this.contentPreviewViewerDelegate, this.resourcesProvider);
        }
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends GridLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, int i) {
            super(context, i);
            StickerMasksAlert.this = r1;
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$4$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends LinearSmoothScroller {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                AnonymousClass4.this = r1;
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateDyToMakeVisible(View view, int i) {
                return super.calculateDyToMakeVisible(view, i) - (StickerMasksAlert.this.gridView.getPaddingTop() - AndroidUtilities.dp(7.0f));
            }

            @Override // androidx.recyclerview.widget.LinearSmoothScroller
            public int calculateTimeForDeceleration(int i) {
                return super.calculateTimeForDeceleration(i) * 4;
            }
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(recyclerView.getContext());
            anonymousClass1.setTargetPosition(i);
            startSmoothScroll(anonymousClass1);
        }
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends GridLayoutManager.SpanSizeLookup {
        AnonymousClass5() {
            StickerMasksAlert.this = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (StickerMasksAlert.this.gridView.getAdapter() != StickerMasksAlert.this.stickersGridAdapter) {
                if (i != StickerMasksAlert.this.stickersSearchGridAdapter.totalItems && (StickerMasksAlert.this.stickersSearchGridAdapter.cache.get(i) == null || (StickerMasksAlert.this.stickersSearchGridAdapter.cache.get(i) instanceof TLRPC$Document))) {
                    return 1;
                }
                return StickerMasksAlert.this.stickersGridAdapter.stickersPerRow;
            } else if (i == 0) {
                return StickerMasksAlert.this.stickersGridAdapter.stickersPerRow;
            } else {
                if (i != StickerMasksAlert.this.stickersGridAdapter.totalItems && (StickerMasksAlert.this.stickersGridAdapter.cache.get(i) == null || (StickerMasksAlert.this.stickersGridAdapter.cache.get(i) instanceof TLRPC$Document))) {
                    return 1;
                }
                return StickerMasksAlert.this.stickersGridAdapter.stickersPerRow;
            }
        }
    }

    public /* synthetic */ boolean lambda$new$0(Theme.ResourcesProvider resourcesProvider, View view, MotionEvent motionEvent) {
        return ContentPreviewViewer.getInstance().onTouch(motionEvent, this.gridView, this.containerView.getMeasuredHeight(), this.stickersOnItemClickListener, this.contentPreviewViewerDelegate, resourcesProvider);
    }

    public /* synthetic */ void lambda$new$1(View view, int i) {
        if (!(view instanceof StickerEmojiCell)) {
            return;
        }
        ContentPreviewViewer.getInstance().reset();
        StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view;
        this.delegate.onStickerSelected(stickerEmojiCell.getParentObject(), stickerEmojiCell.getSticker());
        dismiss();
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends ScrollSlidingTabStrip {
        AnonymousClass6(StickerMasksAlert stickerMasksAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTabStrip, android.widget.HorizontalScrollView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    public /* synthetic */ void lambda$new$2(int i) {
        int i2;
        if (i == this.recentTabBum) {
            i2 = this.stickersGridAdapter.getPositionForPack("recent");
            ScrollSlidingTabStrip scrollSlidingTabStrip = this.stickersTab;
            int i3 = this.recentTabBum;
            scrollSlidingTabStrip.onPageScrolled(i3, i3 > 0 ? i3 : this.stickersTabOffset);
        } else if (i == this.favTabBum) {
            i2 = this.stickersGridAdapter.getPositionForPack("fav");
            ScrollSlidingTabStrip scrollSlidingTabStrip2 = this.stickersTab;
            int i4 = this.favTabBum;
            scrollSlidingTabStrip2.onPageScrolled(i4, i4 > 0 ? i4 : this.stickersTabOffset);
        } else {
            int i5 = i - this.stickersTabOffset;
            if (i5 >= this.stickerSets[this.currentType].size()) {
                return;
            }
            if (i5 >= this.stickerSets[this.currentType].size()) {
                i5 = this.stickerSets[this.currentType].size() - 1;
            }
            i2 = this.stickersGridAdapter.getPositionForPack(this.stickerSets[this.currentType].get(i5));
        }
        if (this.stickersLayoutManager.findFirstVisibleItemPosition() == i2) {
            return;
        }
        this.stickersLayoutManager.scrollToPositionWithOffset(i2, (-this.gridView.getPaddingTop()) + this.searchFieldHeight + AndroidUtilities.dp(48.0f));
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends RecyclerView.OnScrollListener {
        AnonymousClass7() {
            StickerMasksAlert.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                StickerMasksAlert.this.stickersSearchField.hideKeyboard();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            StickerMasksAlert.this.updateLayout(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends FrameLayout {
        AnonymousClass8(StickerMasksAlert stickerMasksAlert, Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends ImageView {
        AnonymousClass9(StickerMasksAlert stickerMasksAlert, Context context) {
            super(context);
        }

        @Override // android.widget.ImageView, android.view.View
        public void setSelected(boolean z) {
            super.setSelected(z);
            Drawable background = getBackground();
            if (Build.VERSION.SDK_INT < 21 || background == null) {
                return;
            }
            int i = z ? -9520403 : 520093695;
            Theme.setSelectorDrawableColor(background, Color.argb(30, Color.red(i), Color.green(i), Color.blue(i)), true);
        }
    }

    public /* synthetic */ void lambda$new$3(View view) {
        if (this.currentType == 0) {
            return;
        }
        this.currentType = 0;
        updateType();
    }

    /* renamed from: org.telegram.ui.Components.StickerMasksAlert$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends ImageView {
        AnonymousClass10(StickerMasksAlert stickerMasksAlert, Context context) {
            super(context);
        }

        @Override // android.widget.ImageView, android.view.View
        public void setSelected(boolean z) {
            super.setSelected(z);
            Drawable background = getBackground();
            if (Build.VERSION.SDK_INT < 21 || background == null) {
                return;
            }
            int i = z ? -9520403 : 520093695;
            Theme.setSelectorDrawableColor(background, Color.argb(30, Color.red(i), Color.green(i), Color.blue(i)), true);
        }
    }

    public /* synthetic */ void lambda$new$4(View view) {
        if (this.currentType == 1) {
            return;
        }
        this.currentType = 1;
        updateType();
    }

    private void updateType() {
        View childAt;
        RecyclerView.ViewHolder findContainingViewHolder;
        int i;
        if (this.gridView.getChildCount() > 0 && (findContainingViewHolder = this.gridView.findContainingViewHolder((childAt = this.gridView.getChildAt(0)))) != null) {
            if (findContainingViewHolder.getAdapterPosition() != 0) {
                i = -this.gridView.getPaddingTop();
            } else {
                i = childAt.getTop() + (-this.gridView.getPaddingTop());
            }
            this.stickersLayoutManager.scrollToPositionWithOffset(0, i);
        }
        checkDocuments(true);
    }

    public void setDelegate(StickerMasksAlertDelegate stickerMasksAlertDelegate) {
        this.delegate = stickerMasksAlertDelegate;
    }

    public void updateLayout(boolean z) {
        RecyclerListView.Holder holder;
        if (this.gridView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.gridView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        View childAt = this.gridView.getChildAt(0);
        RecyclerListView.Holder holder2 = (RecyclerListView.Holder) this.gridView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(7.0f);
        if (top < AndroidUtilities.dp(7.0f) || holder2 == null || holder2.getAdapterPosition() != 0) {
            top = dp;
        }
        int i = top + (-AndroidUtilities.dp(11.0f));
        if (this.scrollOffsetY != i) {
            RecyclerListView recyclerListView2 = this.gridView;
            this.scrollOffsetY = i;
            recyclerListView2.setTopGlowOffset(i);
            this.stickersTab.setTranslationY(i);
            this.stickersSearchField.setTranslationY(i + AndroidUtilities.dp(48.0f));
            this.containerView.invalidate();
        }
        RecyclerListView.Holder holder3 = (RecyclerListView.Holder) this.gridView.findViewHolderForAdapterPosition(0);
        if (holder3 == null) {
            this.stickersSearchField.showShadow(true, z);
        } else {
            this.stickersSearchField.showShadow(holder3.itemView.getTop() < this.gridView.getPaddingTop(), z);
        }
        RecyclerView.Adapter adapter = this.gridView.getAdapter();
        StickersSearchGridAdapter stickersSearchGridAdapter = this.stickersSearchGridAdapter;
        if (adapter == stickersSearchGridAdapter && (holder = (RecyclerListView.Holder) this.gridView.findViewHolderForAdapterPosition(stickersSearchGridAdapter.getItemCount() - 1)) != null && holder.getItemViewType() == 5) {
            FrameLayout frameLayout = (FrameLayout) holder.itemView;
            int childCount = frameLayout.getChildCount();
            float f = (-((frameLayout.getTop() - this.searchFieldHeight) - AndroidUtilities.dp(48.0f))) / 2;
            for (int i2 = 0; i2 < childCount; i2++) {
                frameLayout.getChildAt(i2).setTranslationY(f);
            }
        }
        checkPanels();
    }

    private void updateStickerTabs() {
        ArrayList<TLRPC$Document> arrayList;
        if (this.stickersTab == null) {
            return;
        }
        ImageView imageView = this.stickersButton;
        if (imageView != null) {
            if (this.currentType == 0) {
                imageView.setSelected(true);
                this.masksButton.setSelected(false);
            } else {
                imageView.setSelected(false);
                this.masksButton.setSelected(true);
            }
        }
        this.recentTabBum = -2;
        this.favTabBum = -2;
        this.stickersTabOffset = 0;
        int currentPosition = this.stickersTab.getCurrentPosition();
        this.stickersTab.beginUpdate(false);
        if (this.currentType == 0 && !this.favouriteStickers.isEmpty()) {
            int i = this.stickersTabOffset;
            this.favTabBum = i;
            this.stickersTabOffset = i + 1;
            this.stickersTab.addIconTab(1, this.stickerIcons[1]).setContentDescription(LocaleController.getString("FavoriteStickers", 2131625836));
        }
        if (!this.recentStickers[this.currentType].isEmpty()) {
            int i2 = this.stickersTabOffset;
            this.recentTabBum = i2;
            this.stickersTabOffset = i2 + 1;
            this.stickersTab.addIconTab(0, this.stickerIcons[0]).setContentDescription(LocaleController.getString("RecentStickers", 2131627924));
        }
        this.stickerSets[this.currentType].clear();
        ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(this.currentAccount).getStickerSets(this.currentType);
        for (int i3 = 0; i3 < stickerSets.size(); i3++) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i3);
            if (!tLRPC$TL_messages_stickerSet.set.archived && (arrayList = tLRPC$TL_messages_stickerSet.documents) != null && !arrayList.isEmpty()) {
                this.stickerSets[this.currentType].add(tLRPC$TL_messages_stickerSet);
            }
        }
        for (int i4 = 0; i4 < this.stickerSets[this.currentType].size(); i4++) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2 = this.stickerSets[this.currentType].get(i4);
            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet2.documents.get(0);
            TLObject closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messages_stickerSet2.set.thumbs, 90);
            if (closestPhotoSizeWithSize == null) {
                closestPhotoSizeWithSize = tLRPC$Document;
            }
            View addStickerTab = this.stickersTab.addStickerTab(closestPhotoSizeWithSize, tLRPC$Document, tLRPC$TL_messages_stickerSet2);
            addStickerTab.setContentDescription(tLRPC$TL_messages_stickerSet2.set.title + ", " + LocaleController.getString("AccDescrStickerSet", 2131624088));
        }
        this.stickersTab.commitUpdate();
        this.stickersTab.updateTabStyles();
        if (currentPosition != 0) {
            this.stickersTab.onPageScrolled(currentPosition, currentPosition);
        }
        checkPanels();
    }

    private void checkPanels() {
        if (this.stickersTab == null) {
            return;
        }
        int childCount = this.gridView.getChildCount();
        View view = null;
        for (int i = 0; i < childCount; i++) {
            view = this.gridView.getChildAt(i);
            if (view.getBottom() > this.searchFieldHeight + AndroidUtilities.dp(48.0f)) {
                break;
            }
        }
        if (view == null) {
            return;
        }
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.gridView.findContainingViewHolder(view);
        int adapterPosition = holder != null ? holder.getAdapterPosition() : -1;
        if (adapterPosition == -1) {
            return;
        }
        int i2 = this.favTabBum;
        if (i2 <= 0 && (i2 = this.recentTabBum) <= 0) {
            i2 = this.stickersTabOffset;
        }
        this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(adapterPosition), i2);
    }

    private void reloadStickersAdapter() {
        StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        StickersSearchGridAdapter stickersSearchGridAdapter = this.stickersSearchGridAdapter;
        if (stickersSearchGridAdapter != null) {
            stickersSearchGridAdapter.notifyDataSetChanged();
        }
        if (ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().close();
        }
        ContentPreviewViewer.getInstance().reset();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoad);
    }

    private void checkDocuments(boolean z) {
        int size = this.recentStickers[this.currentType].size();
        int size2 = this.favouriteStickers.size();
        this.recentStickers[this.currentType] = MediaDataController.getInstance(this.currentAccount).getRecentStickers(this.currentType);
        this.favouriteStickers = MediaDataController.getInstance(this.currentAccount).getRecentStickers(2);
        if (this.currentType == 0) {
            for (int i = 0; i < this.favouriteStickers.size(); i++) {
                TLRPC$Document tLRPC$Document = this.favouriteStickers.get(i);
                int i2 = 0;
                while (true) {
                    if (i2 < this.recentStickers[this.currentType].size()) {
                        TLRPC$Document tLRPC$Document2 = this.recentStickers[this.currentType].get(i2);
                        if (tLRPC$Document2.dc_id == tLRPC$Document.dc_id && tLRPC$Document2.id == tLRPC$Document.id) {
                            this.recentStickers[this.currentType].remove(i2);
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
        if (z || size != this.recentStickers[this.currentType].size() || size2 != this.favouriteStickers.size()) {
            updateStickerTabs();
        }
        StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        if (!z) {
            checkPanels();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        RecyclerListView recyclerListView;
        if (i == NotificationCenter.stickersDidLoad) {
            if (((Integer) objArr[0]).intValue() != this.currentType) {
                return;
            }
            updateStickerTabs();
            reloadStickersAdapter();
            checkPanels();
        } else if (i == NotificationCenter.recentDocumentsDidLoad) {
            boolean booleanValue = ((Boolean) objArr[0]).booleanValue();
            int intValue = ((Integer) objArr[1]).intValue();
            if (booleanValue) {
                return;
            }
            if (intValue != this.currentType && intValue != 2) {
                return;
            }
            checkDocuments(false);
        } else if (i == NotificationCenter.emojiLoaded && (recyclerListView = this.gridView) != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = this.gridView.getChildAt(i3);
                if ((childAt instanceof StickerSetNameCell) || (childAt instanceof StickerEmojiCell)) {
                    childAt.invalidate();
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class StickersGridAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private int stickersPerRow;
        private int totalItems;
        private SparseArray<Object> rowStartPack = new SparseArray<>();
        private HashMap<Object, Integer> packStartPosition = new HashMap<>();
        private SparseArray<Object> cache = new SparseArray<>();
        private SparseArray<Object> cacheParents = new SparseArray<>();
        private SparseIntArray positionToRow = new SparseIntArray();

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public StickersGridAdapter(Context context) {
            StickerMasksAlert.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = this.totalItems;
            if (i != 0) {
                return i + 1;
            }
            return 0;
        }

        public int getPositionForPack(Object obj) {
            Integer num = this.packStartPosition.get(obj);
            if (num == null) {
                return -1;
            }
            return num.intValue();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 4;
            }
            Object obj = this.cache.get(i);
            if (obj == null) {
                return 1;
            }
            return obj instanceof TLRPC$Document ? 0 : 2;
        }

        public int getTabForPosition(int i) {
            if (i == 0) {
                i = 1;
            }
            if (this.stickersPerRow == 0) {
                int measuredWidth = StickerMasksAlert.this.gridView.getMeasuredWidth();
                if (measuredWidth == 0) {
                    measuredWidth = AndroidUtilities.displaySize.x;
                }
                this.stickersPerRow = measuredWidth / AndroidUtilities.dp(72.0f);
            }
            int i2 = this.positionToRow.get(i, Integer.MIN_VALUE);
            if (i2 == Integer.MIN_VALUE) {
                return (StickerMasksAlert.this.stickerSets[StickerMasksAlert.this.currentType].size() - 1) + StickerMasksAlert.this.stickersTabOffset;
            }
            Object obj = this.rowStartPack.get(i2);
            if (obj instanceof String) {
                return "recent".equals(obj) ? StickerMasksAlert.this.recentTabBum : StickerMasksAlert.this.favTabBum;
            }
            return StickerMasksAlert.this.stickerSets[StickerMasksAlert.this.currentType].indexOf((TLRPC$TL_messages_stickerSet) obj) + StickerMasksAlert.this.stickersTabOffset;
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$StickersGridAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends StickerEmojiCell {
            AnonymousClass1(StickersGridAdapter stickersGridAdapter, Context context, boolean z) {
                super(context, z);
            }

            @Override // android.widget.FrameLayout, android.view.View
            public void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            AnonymousClass1 anonymousClass1;
            if (i == 0) {
                anonymousClass1 = new AnonymousClass1(this, this.context, false);
            } else {
                if (i == 1) {
                    view = new EmptyCell(this.context);
                } else if (i == 2) {
                    StickerSetNameCell stickerSetNameCell = new StickerSetNameCell(this.context, false, ((BottomSheet) StickerMasksAlert.this).resourcesProvider);
                    stickerSetNameCell.setTitleColor(-7829368);
                    anonymousClass1 = stickerSetNameCell;
                } else if (i != 4) {
                    view = null;
                } else {
                    view = new View(this.context);
                    view.setLayoutParams(new RecyclerView.LayoutParams(-1, StickerMasksAlert.this.searchFieldHeight + AndroidUtilities.dp(48.0f)));
                }
                return new RecyclerListView.Holder(view);
            }
            view = anonymousClass1;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ArrayList<TLRPC$Document> arrayList;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) this.cache.get(i);
                StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) viewHolder.itemView;
                stickerEmojiCell.setSticker(tLRPC$Document, this.cacheParents.get(i), false);
                stickerEmojiCell.setRecent(StickerMasksAlert.this.recentStickers[StickerMasksAlert.this.currentType].contains(tLRPC$Document));
                return;
            }
            int i2 = 1;
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return;
                }
                StickerSetNameCell stickerSetNameCell = (StickerSetNameCell) viewHolder.itemView;
                Object obj = this.cache.get(i);
                if (!(obj instanceof TLRPC$TL_messages_stickerSet)) {
                    if (obj != StickerMasksAlert.this.recentStickers[StickerMasksAlert.this.currentType]) {
                        if (obj != StickerMasksAlert.this.favouriteStickers) {
                            return;
                        }
                        stickerSetNameCell.setText(LocaleController.getString("FavoriteStickers", 2131625836), 0);
                        return;
                    }
                    stickerSetNameCell.setText(LocaleController.getString("RecentStickers", 2131627924), 0);
                    return;
                }
                TLRPC$StickerSet tLRPC$StickerSet = ((TLRPC$TL_messages_stickerSet) obj).set;
                if (tLRPC$StickerSet == null) {
                    return;
                }
                stickerSetNameCell.setText(tLRPC$StickerSet.title, 0);
                return;
            }
            EmptyCell emptyCell = (EmptyCell) viewHolder.itemView;
            if (i == this.totalItems) {
                int i3 = this.positionToRow.get(i - 1, Integer.MIN_VALUE);
                if (i3 == Integer.MIN_VALUE) {
                    emptyCell.setHeight(1);
                    return;
                }
                Object obj2 = this.rowStartPack.get(i3);
                if (obj2 instanceof TLRPC$TL_messages_stickerSet) {
                    arrayList = ((TLRPC$TL_messages_stickerSet) obj2).documents;
                } else if (obj2 instanceof String) {
                    arrayList = "recent".equals(obj2) ? StickerMasksAlert.this.recentStickers[StickerMasksAlert.this.currentType] : StickerMasksAlert.this.favouriteStickers;
                } else {
                    arrayList = null;
                }
                if (arrayList == null) {
                    emptyCell.setHeight(1);
                    return;
                } else if (!arrayList.isEmpty()) {
                    int height = StickerMasksAlert.this.gridView.getHeight() - (((int) Math.ceil(arrayList.size() / this.stickersPerRow)) * AndroidUtilities.dp(82.0f));
                    if (height > 0) {
                        i2 = height;
                    }
                    emptyCell.setHeight(i2);
                    return;
                } else {
                    emptyCell.setHeight(AndroidUtilities.dp(8.0f));
                    return;
                }
            }
            emptyCell.setHeight(AndroidUtilities.dp(82.0f));
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x00ca  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00e3  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00eb  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0100  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0133  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x015a A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:58:0x014e A[EDGE_INSN: B:58:0x014e->B:46:0x014e ?: BREAK  , SYNTHETIC] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void notifyDataSetChanged() {
            Object obj;
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet;
            int ceil;
            int i;
            int i2;
            int i3;
            ArrayList<TLRPC$Document> arrayList;
            int measuredWidth = StickerMasksAlert.this.gridView.getMeasuredWidth();
            if (measuredWidth == 0) {
                measuredWidth = AndroidUtilities.displaySize.x;
            }
            this.stickersPerRow = measuredWidth / AndroidUtilities.dp(72.0f);
            StickerMasksAlert.this.stickersLayoutManager.setSpanCount(this.stickersPerRow);
            this.rowStartPack.clear();
            this.packStartPosition.clear();
            this.positionToRow.clear();
            this.cache.clear();
            this.totalItems = 0;
            ArrayList arrayList2 = StickerMasksAlert.this.stickerSets[StickerMasksAlert.this.currentType];
            int i4 = -3;
            int i5 = -3;
            int i6 = 0;
            while (i5 < arrayList2.size()) {
                if (i5 == i4) {
                    SparseArray<Object> sparseArray = this.cache;
                    int i7 = this.totalItems;
                    this.totalItems = i7 + 1;
                    sparseArray.put(i7, "search");
                    i6++;
                } else {
                    ArrayList<TLRPC$Document> arrayList3 = null;
                    if (i5 == -2) {
                        if (StickerMasksAlert.this.currentType == 0) {
                            arrayList = StickerMasksAlert.this.favouriteStickers;
                            this.packStartPosition.put("fav", Integer.valueOf(this.totalItems));
                            obj = "fav";
                            arrayList3 = arrayList;
                            tLRPC$TL_messages_stickerSet = null;
                            if (arrayList3 != null && !arrayList3.isEmpty()) {
                                ceil = (int) Math.ceil(arrayList3.size() / this.stickersPerRow);
                                if (tLRPC$TL_messages_stickerSet == null) {
                                    this.cache.put(this.totalItems, tLRPC$TL_messages_stickerSet);
                                } else {
                                    this.cache.put(this.totalItems, arrayList3);
                                }
                                this.positionToRow.put(this.totalItems, i6);
                                i = 0;
                                while (i < arrayList3.size()) {
                                    int i8 = i + 1;
                                    int i9 = this.totalItems + i8;
                                    this.cache.put(i9, arrayList3.get(i));
                                    if (tLRPC$TL_messages_stickerSet != null) {
                                        this.cacheParents.put(i9, tLRPC$TL_messages_stickerSet);
                                    } else {
                                        this.cacheParents.put(i9, obj);
                                    }
                                    this.positionToRow.put(this.totalItems + i8, i6 + 1 + (i / this.stickersPerRow));
                                    i = i8;
                                }
                                i2 = 0;
                                while (true) {
                                    i3 = ceil + 1;
                                    if (i2 < i3) {
                                        break;
                                    }
                                    if (tLRPC$TL_messages_stickerSet != null) {
                                        this.rowStartPack.put(i6 + i2, tLRPC$TL_messages_stickerSet);
                                    } else {
                                        this.rowStartPack.put(i6 + i2, i5 == -1 ? "recent" : "fav");
                                    }
                                    i2++;
                                }
                                this.totalItems += (ceil * this.stickersPerRow) + 1;
                                i6 += i3;
                            }
                        } else {
                            tLRPC$TL_messages_stickerSet = null;
                            obj = null;
                            if (arrayList3 != null) {
                                ceil = (int) Math.ceil(arrayList3.size() / this.stickersPerRow);
                                if (tLRPC$TL_messages_stickerSet == null) {
                                }
                                this.positionToRow.put(this.totalItems, i6);
                                i = 0;
                                while (i < arrayList3.size()) {
                                }
                                i2 = 0;
                                while (true) {
                                    i3 = ceil + 1;
                                    if (i2 < i3) {
                                    }
                                    i2++;
                                }
                                this.totalItems += (ceil * this.stickersPerRow) + 1;
                                i6 += i3;
                            }
                        }
                    } else if (i5 == -1) {
                        arrayList = StickerMasksAlert.this.recentStickers[StickerMasksAlert.this.currentType];
                        this.packStartPosition.put("recent", Integer.valueOf(this.totalItems));
                        obj = "recent";
                        arrayList3 = arrayList;
                        tLRPC$TL_messages_stickerSet = null;
                        if (arrayList3 != null) {
                        }
                    } else {
                        tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) arrayList2.get(i5);
                        ArrayList<TLRPC$Document> arrayList4 = tLRPC$TL_messages_stickerSet.documents;
                        this.packStartPosition.put(tLRPC$TL_messages_stickerSet, Integer.valueOf(this.totalItems));
                        obj = null;
                        arrayList3 = arrayList4;
                        if (arrayList3 != null) {
                        }
                    }
                }
                i5++;
                i4 = -3;
            }
            super.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class StickersSearchGridAdapter extends RecyclerListView.SelectionAdapter {
        boolean cleared;
        private Context context;
        private int emojiSearchId;
        private int reqId2;
        private String searchQuery;
        private int totalItems;
        private SparseArray<Object> rowStartPack = new SparseArray<>();
        private SparseArray<Object> cache = new SparseArray<>();
        private SparseArray<Object> cacheParent = new SparseArray<>();
        private SparseIntArray positionToRow = new SparseIntArray();
        private SparseArray<String> positionToEmoji = new SparseArray<>();
        private ArrayList<TLRPC$TL_messages_stickerSet> localPacks = new ArrayList<>();
        private HashMap<TLRPC$TL_messages_stickerSet, Boolean> localPacksByShortName = new HashMap<>();
        private HashMap<TLRPC$TL_messages_stickerSet, Integer> localPacksByName = new HashMap<>();
        private HashMap<ArrayList<TLRPC$Document>, String> emojiStickers = new HashMap<>();
        private ArrayList<ArrayList<TLRPC$Document>> emojiArrays = new ArrayList<>();
        private Runnable searchRunnable = new AnonymousClass1();

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        static /* synthetic */ int access$4904(StickersSearchGridAdapter stickersSearchGridAdapter) {
            int i = stickersSearchGridAdapter.emojiSearchId + 1;
            stickersSearchGridAdapter.emojiSearchId = i;
            return i;
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$StickersSearchGridAdapter$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
                StickersSearchGridAdapter.this = r1;
            }

            private void clear() {
                StickersSearchGridAdapter stickersSearchGridAdapter = StickersSearchGridAdapter.this;
                if (stickersSearchGridAdapter.cleared) {
                    return;
                }
                stickersSearchGridAdapter.cleared = true;
                stickersSearchGridAdapter.emojiStickers.clear();
                StickersSearchGridAdapter.this.emojiArrays.clear();
                StickersSearchGridAdapter.this.localPacks.clear();
                StickersSearchGridAdapter.this.localPacksByShortName.clear();
                StickersSearchGridAdapter.this.localPacksByName.clear();
            }

            /* JADX WARN: Code restructure failed: missing block: B:16:0x006c, code lost:
                if (r5.charAt(r9) <= 57343) goto L23;
             */
            /* JADX WARN: Code restructure failed: missing block: B:22:0x0086, code lost:
                if (r5.charAt(r9) != 9794) goto L24;
             */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                int indexOfIgnoreCase;
                int indexOfIgnoreCase2;
                if (TextUtils.isEmpty(StickersSearchGridAdapter.this.searchQuery)) {
                    return;
                }
                StickersSearchGridAdapter stickersSearchGridAdapter = StickersSearchGridAdapter.this;
                stickersSearchGridAdapter.cleared = false;
                int access$4904 = StickersSearchGridAdapter.access$4904(stickersSearchGridAdapter);
                ArrayList arrayList = new ArrayList(0);
                LongSparseArray longSparseArray = new LongSparseArray(0);
                HashMap<String, ArrayList<TLRPC$Document>> allStickers = MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).getAllStickers();
                if (StickersSearchGridAdapter.this.searchQuery.length() <= 14) {
                    CharSequence charSequence = StickersSearchGridAdapter.this.searchQuery;
                    int length = charSequence.length();
                    int i = 0;
                    while (i < length) {
                        if (i < length - 1) {
                            if (charSequence.charAt(i) == 55356) {
                                int i2 = i + 1;
                                if (charSequence.charAt(i2) >= 57339) {
                                }
                            }
                            if (charSequence.charAt(i) == 8205) {
                                int i3 = i + 1;
                                if (charSequence.charAt(i3) != 9792) {
                                }
                                charSequence = TextUtils.concat(charSequence.subSequence(0, i), charSequence.subSequence(i + 2, charSequence.length()));
                                length -= 2;
                                i--;
                                i++;
                            }
                        }
                        if (charSequence.charAt(i) == 65039) {
                            charSequence = TextUtils.concat(charSequence.subSequence(0, i), charSequence.subSequence(i + 1, charSequence.length()));
                            length--;
                            i--;
                            i++;
                        } else {
                            i++;
                        }
                    }
                    ArrayList<TLRPC$Document> arrayList2 = allStickers != null ? allStickers.get(charSequence.toString()) : null;
                    if (arrayList2 != null && !arrayList2.isEmpty()) {
                        clear();
                        arrayList.addAll(arrayList2);
                        int size = arrayList2.size();
                        for (int i4 = 0; i4 < size; i4++) {
                            TLRPC$Document tLRPC$Document = arrayList2.get(i4);
                            longSparseArray.put(tLRPC$Document.id, tLRPC$Document);
                        }
                        StickersSearchGridAdapter.this.emojiStickers.put(arrayList, StickersSearchGridAdapter.this.searchQuery);
                        StickersSearchGridAdapter.this.emojiArrays.add(arrayList);
                    }
                }
                if (allStickers != null && !allStickers.isEmpty() && StickersSearchGridAdapter.this.searchQuery.length() > 1) {
                    String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                    if (!Arrays.equals(StickerMasksAlert.this.lastSearchKeyboardLanguage, currentKeyboardLanguage)) {
                        MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                    }
                    StickerMasksAlert.this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                    MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).getEmojiSuggestions(StickerMasksAlert.this.lastSearchKeyboardLanguage, StickersSearchGridAdapter.this.searchQuery, false, new StickerMasksAlert$StickersSearchGridAdapter$1$$ExternalSyntheticLambda1(this, access$4904, allStickers), false);
                }
                ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).getStickerSets(StickerMasksAlert.this.currentType);
                int size2 = stickerSets.size();
                for (int i5 = 0; i5 < size2; i5++) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i5);
                    int indexOfIgnoreCase3 = AndroidUtilities.indexOfIgnoreCase(tLRPC$TL_messages_stickerSet.set.title, StickersSearchGridAdapter.this.searchQuery);
                    if (indexOfIgnoreCase3 >= 0) {
                        if (indexOfIgnoreCase3 == 0 || tLRPC$TL_messages_stickerSet.set.title.charAt(indexOfIgnoreCase3 - 1) == ' ') {
                            clear();
                            StickersSearchGridAdapter.this.localPacks.add(tLRPC$TL_messages_stickerSet);
                            StickersSearchGridAdapter.this.localPacksByName.put(tLRPC$TL_messages_stickerSet, Integer.valueOf(indexOfIgnoreCase3));
                        }
                    } else {
                        String str = tLRPC$TL_messages_stickerSet.set.short_name;
                        if (str != null && (indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str, StickersSearchGridAdapter.this.searchQuery)) >= 0 && (indexOfIgnoreCase2 == 0 || tLRPC$TL_messages_stickerSet.set.short_name.charAt(indexOfIgnoreCase2 - 1) == ' ')) {
                            clear();
                            StickersSearchGridAdapter.this.localPacks.add(tLRPC$TL_messages_stickerSet);
                            StickersSearchGridAdapter.this.localPacksByShortName.put(tLRPC$TL_messages_stickerSet, Boolean.TRUE);
                        }
                    }
                }
                ArrayList<TLRPC$TL_messages_stickerSet> stickerSets2 = MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).getStickerSets(3);
                int size3 = stickerSets2.size();
                for (int i6 = 0; i6 < size3; i6++) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2 = stickerSets2.get(i6);
                    int indexOfIgnoreCase4 = AndroidUtilities.indexOfIgnoreCase(tLRPC$TL_messages_stickerSet2.set.title, StickersSearchGridAdapter.this.searchQuery);
                    if (indexOfIgnoreCase4 >= 0) {
                        if (indexOfIgnoreCase4 == 0 || tLRPC$TL_messages_stickerSet2.set.title.charAt(indexOfIgnoreCase4 - 1) == ' ') {
                            clear();
                            StickersSearchGridAdapter.this.localPacks.add(tLRPC$TL_messages_stickerSet2);
                            StickersSearchGridAdapter.this.localPacksByName.put(tLRPC$TL_messages_stickerSet2, Integer.valueOf(indexOfIgnoreCase4));
                        }
                    } else {
                        String str2 = tLRPC$TL_messages_stickerSet2.set.short_name;
                        if (str2 != null && (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str2, StickersSearchGridAdapter.this.searchQuery)) >= 0 && (indexOfIgnoreCase == 0 || tLRPC$TL_messages_stickerSet2.set.short_name.charAt(indexOfIgnoreCase - 1) == ' ')) {
                            clear();
                            StickersSearchGridAdapter.this.localPacks.add(tLRPC$TL_messages_stickerSet2);
                            StickersSearchGridAdapter.this.localPacksByShortName.put(tLRPC$TL_messages_stickerSet2, Boolean.TRUE);
                        }
                    }
                }
                boolean isValidEmoji = Emoji.isValidEmoji(StickersSearchGridAdapter.this.searchQuery);
                if (isValidEmoji) {
                    StickerMasksAlert.this.stickersSearchField.progressDrawable.startAnimation();
                    TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers = new TLRPC$TL_messages_getStickers();
                    tLRPC$TL_messages_getStickers.emoticon = StickersSearchGridAdapter.this.searchQuery;
                    tLRPC$TL_messages_getStickers.hash = 0L;
                    StickersSearchGridAdapter stickersSearchGridAdapter2 = StickersSearchGridAdapter.this;
                    stickersSearchGridAdapter2.reqId2 = ConnectionsManager.getInstance(StickerMasksAlert.this.currentAccount).sendRequest(tLRPC$TL_messages_getStickers, new StickerMasksAlert$StickersSearchGridAdapter$1$$ExternalSyntheticLambda2(this, tLRPC$TL_messages_getStickers, arrayList, longSparseArray));
                }
                if ((!isValidEmoji || !StickersSearchGridAdapter.this.localPacks.isEmpty() || !StickersSearchGridAdapter.this.emojiStickers.isEmpty()) && StickerMasksAlert.this.gridView.getAdapter() != StickerMasksAlert.this.stickersSearchGridAdapter) {
                    StickerMasksAlert.this.gridView.setAdapter(StickerMasksAlert.this.stickersSearchGridAdapter);
                }
                StickersSearchGridAdapter.this.notifyDataSetChanged();
            }

            public /* synthetic */ void lambda$run$0(int i, HashMap hashMap, ArrayList arrayList, String str) {
                if (i != StickersSearchGridAdapter.this.emojiSearchId) {
                    return;
                }
                int size = arrayList.size();
                boolean z = false;
                for (int i2 = 0; i2 < size; i2++) {
                    String str2 = ((MediaDataController.KeywordResult) arrayList.get(i2)).emoji;
                    ArrayList arrayList2 = hashMap != null ? (ArrayList) hashMap.get(str2) : null;
                    if (arrayList2 != null && !arrayList2.isEmpty()) {
                        clear();
                        if (!StickersSearchGridAdapter.this.emojiStickers.containsKey(arrayList2)) {
                            StickersSearchGridAdapter.this.emojiStickers.put(arrayList2, str2);
                            StickersSearchGridAdapter.this.emojiArrays.add(arrayList2);
                            z = true;
                        }
                    }
                }
                if (!z) {
                    if (StickersSearchGridAdapter.this.reqId2 != 0) {
                        return;
                    }
                    clear();
                    StickersSearchGridAdapter.this.notifyDataSetChanged();
                    return;
                }
                StickersSearchGridAdapter.this.notifyDataSetChanged();
            }

            public /* synthetic */ void lambda$run$2(TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers, ArrayList arrayList, LongSparseArray longSparseArray, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                AndroidUtilities.runOnUIThread(new StickerMasksAlert$StickersSearchGridAdapter$1$$ExternalSyntheticLambda0(this, tLRPC$TL_messages_getStickers, tLObject, arrayList, longSparseArray));
            }

            public /* synthetic */ void lambda$run$1(TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers, TLObject tLObject, ArrayList arrayList, LongSparseArray longSparseArray) {
                if (tLRPC$TL_messages_getStickers.emoticon.equals(StickersSearchGridAdapter.this.searchQuery)) {
                    StickerMasksAlert.this.stickersSearchField.progressDrawable.stopAnimation();
                    StickersSearchGridAdapter.this.reqId2 = 0;
                    if (!(tLObject instanceof TLRPC$TL_messages_stickers)) {
                        return;
                    }
                    TLRPC$TL_messages_stickers tLRPC$TL_messages_stickers = (TLRPC$TL_messages_stickers) tLObject;
                    int size = arrayList.size();
                    int size2 = tLRPC$TL_messages_stickers.stickers.size();
                    for (int i = 0; i < size2; i++) {
                        TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickers.stickers.get(i);
                        if (longSparseArray.indexOfKey(tLRPC$Document.id) < 0) {
                            arrayList.add(tLRPC$Document);
                        }
                    }
                    if (size != arrayList.size()) {
                        StickersSearchGridAdapter.this.emojiStickers.put(arrayList, StickersSearchGridAdapter.this.searchQuery);
                        if (size == 0) {
                            StickersSearchGridAdapter.this.emojiArrays.add(arrayList);
                        }
                        StickersSearchGridAdapter.this.notifyDataSetChanged();
                    }
                    if (StickerMasksAlert.this.gridView.getAdapter() == StickerMasksAlert.this.stickersSearchGridAdapter) {
                        return;
                    }
                    StickerMasksAlert.this.gridView.setAdapter(StickerMasksAlert.this.stickersSearchGridAdapter);
                }
            }
        }

        public StickersSearchGridAdapter(Context context) {
            StickerMasksAlert.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = this.totalItems;
            if (i != 1) {
                return i + 1;
            }
            return 2;
        }

        public void search(String str) {
            if (this.reqId2 != 0) {
                ConnectionsManager.getInstance(StickerMasksAlert.this.currentAccount).cancelRequest(this.reqId2, true);
                this.reqId2 = 0;
            }
            if (TextUtils.isEmpty(str)) {
                this.searchQuery = null;
                this.localPacks.clear();
                this.emojiStickers.clear();
                if (StickerMasksAlert.this.gridView.getAdapter() != StickerMasksAlert.this.stickersGridAdapter) {
                    StickerMasksAlert.this.gridView.setAdapter(StickerMasksAlert.this.stickersGridAdapter);
                }
                notifyDataSetChanged();
            } else {
                this.searchQuery = str.toLowerCase();
            }
            AndroidUtilities.cancelRunOnUIThread(this.searchRunnable);
            AndroidUtilities.runOnUIThread(this.searchRunnable, 300L);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 4;
            }
            if (i == 1 && this.totalItems == 1) {
                return 5;
            }
            Object obj = this.cache.get(i);
            if (obj == null) {
                return 1;
            }
            return obj instanceof TLRPC$Document ? 0 : 2;
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$StickersSearchGridAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends StickerEmojiCell {
            AnonymousClass2(StickersSearchGridAdapter stickersSearchGridAdapter, Context context, boolean z) {
                super(context, z);
            }

            @Override // android.widget.FrameLayout, android.view.View
            public void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
            }
        }

        /* renamed from: org.telegram.ui.Components.StickerMasksAlert$StickersSearchGridAdapter$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 extends FrameLayout {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Context context) {
                super(context);
                StickersSearchGridAdapter.this = r1;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(((StickerMasksAlert.this.gridView.getMeasuredHeight() - StickerMasksAlert.this.searchFieldHeight) - AndroidUtilities.dp(48.0f)) - AndroidUtilities.dp(48.0f), 1073741824));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            AnonymousClass3 anonymousClass3;
            View view;
            if (i == 0) {
                view = new AnonymousClass2(this, this.context, false);
            } else {
                if (i == 1) {
                    anonymousClass3 = new EmptyCell(this.context);
                } else if (i == 2) {
                    view = new StickerSetNameCell(this.context, false, ((BottomSheet) StickerMasksAlert.this).resourcesProvider);
                } else if (i == 4) {
                    View view2 = new View(this.context);
                    view2.setLayoutParams(new RecyclerView.LayoutParams(-1, StickerMasksAlert.this.searchFieldHeight + AndroidUtilities.dp(48.0f)));
                    anonymousClass3 = view2;
                } else if (i != 5) {
                    anonymousClass3 = null;
                } else {
                    AnonymousClass3 anonymousClass32 = new AnonymousClass3(this.context);
                    ImageView imageView = new ImageView(this.context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    imageView.setImageResource(2131166171);
                    imageView.setColorFilter(new PorterDuffColorFilter(-7038047, PorterDuff.Mode.MULTIPLY));
                    anonymousClass32.addView(imageView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 50.0f));
                    TextView textView = new TextView(this.context);
                    textView.setText(LocaleController.getString("NoStickersFound", 2131626926));
                    textView.setTextSize(1, 16.0f);
                    textView.setTextColor(-7038047);
                    anonymousClass32.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
                    anonymousClass32.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                    anonymousClass3 = anonymousClass32;
                }
                return new RecyclerListView.Holder(anonymousClass3);
            }
            anonymousClass3 = view;
            return new RecyclerListView.Holder(anonymousClass3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            int i2 = 1;
            if (itemViewType == 0) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) this.cache.get(i);
                StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) viewHolder.itemView;
                stickerEmojiCell.setSticker(tLRPC$Document, null, this.cacheParent.get(i), this.positionToEmoji.get(i), false);
                if (StickerMasksAlert.this.recentStickers[StickerMasksAlert.this.currentType].contains(tLRPC$Document) || StickerMasksAlert.this.favouriteStickers.contains(tLRPC$Document)) {
                    z = true;
                }
                stickerEmojiCell.setRecent(z);
                return;
            }
            Integer num = null;
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return;
                }
                StickerSetNameCell stickerSetNameCell = (StickerSetNameCell) viewHolder.itemView;
                Object obj = this.cache.get(i);
                if (!(obj instanceof TLRPC$TL_messages_stickerSet)) {
                    return;
                }
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) obj;
                if (!TextUtils.isEmpty(this.searchQuery) && this.localPacksByShortName.containsKey(tLRPC$TL_messages_stickerSet)) {
                    TLRPC$StickerSet tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set;
                    if (tLRPC$StickerSet != null) {
                        stickerSetNameCell.setText(tLRPC$StickerSet.title, 0);
                    }
                    stickerSetNameCell.setUrl(tLRPC$TL_messages_stickerSet.set.short_name, this.searchQuery.length());
                    return;
                }
                Integer num2 = this.localPacksByName.get(tLRPC$TL_messages_stickerSet);
                TLRPC$StickerSet tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set;
                if (tLRPC$StickerSet2 != null && num2 != null) {
                    stickerSetNameCell.setText(tLRPC$StickerSet2.title, 0, num2.intValue(), !TextUtils.isEmpty(this.searchQuery) ? this.searchQuery.length() : 0);
                }
                stickerSetNameCell.setUrl(null, 0);
                return;
            }
            EmptyCell emptyCell = (EmptyCell) viewHolder.itemView;
            if (i == this.totalItems) {
                int i3 = this.positionToRow.get(i - 1, Integer.MIN_VALUE);
                if (i3 == Integer.MIN_VALUE) {
                    emptyCell.setHeight(1);
                    return;
                }
                Object obj2 = this.rowStartPack.get(i3);
                if (obj2 instanceof TLRPC$TL_messages_stickerSet) {
                    num = Integer.valueOf(((TLRPC$TL_messages_stickerSet) obj2).documents.size());
                } else if (obj2 instanceof Integer) {
                    num = (Integer) obj2;
                }
                if (num == null) {
                    emptyCell.setHeight(1);
                    return;
                } else if (num.intValue() != 0) {
                    int height = StickerMasksAlert.this.gridView.getHeight() - (((int) Math.ceil(num.intValue() / StickerMasksAlert.this.stickersGridAdapter.stickersPerRow)) * AndroidUtilities.dp(82.0f));
                    if (height > 0) {
                        i2 = height;
                    }
                    emptyCell.setHeight(i2);
                    return;
                } else {
                    emptyCell.setHeight(AndroidUtilities.dp(8.0f));
                    return;
                }
            }
            emptyCell.setHeight(AndroidUtilities.dp(82.0f));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            int i;
            this.rowStartPack.clear();
            this.positionToRow.clear();
            this.cache.clear();
            this.positionToEmoji.clear();
            this.totalItems = 0;
            int size = this.localPacks.size();
            int i2 = !this.emojiArrays.isEmpty() ? 1 : 0;
            int i3 = -1;
            int i4 = -1;
            int i5 = 0;
            while (i4 < size + i2) {
                if (i4 == i3) {
                    SparseArray<Object> sparseArray = this.cache;
                    int i6 = this.totalItems;
                    this.totalItems = i6 + 1;
                    sparseArray.put(i6, "search");
                    i5++;
                } else if (i4 < size) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.localPacks.get(i4);
                    ArrayList<TLRPC$Document> arrayList = tLRPC$TL_messages_stickerSet.documents;
                    if (!arrayList.isEmpty()) {
                        int ceil = (int) Math.ceil(arrayList.size() / StickerMasksAlert.this.stickersGridAdapter.stickersPerRow);
                        this.cache.put(this.totalItems, tLRPC$TL_messages_stickerSet);
                        this.positionToRow.put(this.totalItems, i5);
                        int size2 = arrayList.size();
                        int i7 = 0;
                        while (i7 < size2) {
                            int i8 = i7 + 1;
                            int i9 = this.totalItems + i8;
                            int i10 = i5 + 1 + (i7 / StickerMasksAlert.this.stickersGridAdapter.stickersPerRow);
                            this.cache.put(i9, arrayList.get(i7));
                            this.cacheParent.put(i9, tLRPC$TL_messages_stickerSet);
                            this.positionToRow.put(i9, i10);
                            i7 = i8;
                        }
                        int i11 = ceil + 1;
                        for (int i12 = 0; i12 < i11; i12++) {
                            this.rowStartPack.put(i5 + i12, tLRPC$TL_messages_stickerSet);
                        }
                        this.totalItems += (ceil * StickerMasksAlert.this.stickersGridAdapter.stickersPerRow) + 1;
                        i5 += i11;
                    }
                } else {
                    int size3 = this.emojiArrays.size();
                    String str = "";
                    int i13 = 0;
                    for (int i14 = 0; i14 < size3; i14++) {
                        ArrayList<TLRPC$Document> arrayList2 = this.emojiArrays.get(i14);
                        String str2 = this.emojiStickers.get(arrayList2);
                        if (str2 != null && !str.equals(str2)) {
                            this.positionToEmoji.put(this.totalItems + i13, str2);
                            str = str2;
                        }
                        int size4 = arrayList2.size();
                        int i15 = 0;
                        while (i15 < size4) {
                            int i16 = this.totalItems + i13;
                            int i17 = (i13 / StickerMasksAlert.this.stickersGridAdapter.stickersPerRow) + i5;
                            TLRPC$Document tLRPC$Document = arrayList2.get(i15);
                            this.cache.put(i16, tLRPC$Document);
                            int i18 = size;
                            TLRPC$TL_messages_stickerSet stickerSetById = MediaDataController.getInstance(StickerMasksAlert.this.currentAccount).getStickerSetById(MediaDataController.getStickerSetId(tLRPC$Document));
                            if (stickerSetById != null) {
                                this.cacheParent.put(i16, stickerSetById);
                            }
                            this.positionToRow.put(i16, i17);
                            i13++;
                            i15++;
                            size = i18;
                        }
                    }
                    i = size;
                    int ceil2 = (int) Math.ceil(i13 / StickerMasksAlert.this.stickersGridAdapter.stickersPerRow);
                    for (int i19 = 0; i19 < ceil2; i19++) {
                        this.rowStartPack.put(i5 + i19, Integer.valueOf(i13));
                    }
                    this.totalItems += StickerMasksAlert.this.stickersGridAdapter.stickersPerRow * ceil2;
                    i5 += ceil2;
                    i4++;
                    size = i;
                    i3 = -1;
                }
                i = size;
                i4++;
                size = i;
                i3 = -1;
            }
            super.notifyDataSetChanged();
        }
    }
}
