package org.telegram.ui.Stars;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$StarsTransaction;
import org.telegram.tgnet.TLRPC$StarsTransactionPeer;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceiptStars;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_starsTopupOption;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeer;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerAds;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerAppStore;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerFragment;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerPlayMarket;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerPremiumBot;
import org.telegram.tgnet.TLRPC$TL_starsTransactionPeerUnsupported;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.SessionCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.LoadingSpan;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.Premium.GLIcon.GLIconRenderer;
import org.telegram.ui.Components.Premium.GLIcon.GLIconTextureView;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.TableView;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.GradientHeaderActivity;
import org.telegram.ui.ImageReceiverSpan;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.HintView2;
/* loaded from: classes3.dex */
public class StarsIntroActivity extends GradientHeaderActivity implements NotificationCenter.NotificationCenterDelegate {
    private FrameLayout aboveTitleView;
    private UniversalAdapter adapter;
    private StarsBalanceView balanceView;
    private View emptyLayout;
    private boolean expanded = false;
    private FireworksOverlay fireworksOverlay;
    private GLIconTextureView iconTextureView;
    private StarsTransactionsLayout transactionsLayout;

    public StarsIntroActivity() {
        setWhiteBackground(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starOptionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starTransactionsLoaded);
        StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starOptionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starTransactionsLoaded);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.starOptionsLoaded) {
            saveScrollPosition();
            UniversalAdapter universalAdapter = this.adapter;
            if (universalAdapter != null) {
                universalAdapter.update(true);
            }
            if (this.savedScrollPosition == 0 && this.savedScrollOffset < 0) {
                this.savedScrollOffset = 0;
            }
            applyScrolledPosition();
        }
    }

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.useFillLastLayoutManager = false;
        this.particlesViewHeight = AndroidUtilities.dp(238.0f);
        this.transactionsLayout = new StarsTransactionsLayout(context, this.currentAccount, 0L, getClassGuid(), getResourceProvider());
        View view = new View(this, context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.1
            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(300.0f), 1073741824));
            }
        };
        this.emptyLayout = view;
        view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        super.createView(context);
        StarsBalanceView starsBalanceView = new StarsBalanceView(context, this.currentAccount);
        this.balanceView = starsBalanceView;
        this.actionBar.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2, 85));
        FrameLayout frameLayout = new FrameLayout(context);
        this.aboveTitleView = frameLayout;
        frameLayout.setClickable(true);
        GLIconTextureView gLIconTextureView = new GLIconTextureView(context, 1, 2);
        this.iconTextureView = gLIconTextureView;
        GLIconRenderer gLIconRenderer = gLIconTextureView.mRenderer;
        gLIconRenderer.colorKey1 = Theme.key_starsGradient1;
        gLIconRenderer.colorKey2 = Theme.key_starsGradient2;
        gLIconRenderer.updateColors();
        this.iconTextureView.setStarParticlesView(this.particlesView);
        this.aboveTitleView.addView(this.iconTextureView, LayoutHelper.createFrame(190, 190.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
        configureHeader(LocaleController.getString(R.string.TelegramStars), LocaleController.getString(R.string.TelegramStarsInfo), this.aboveTitleView, null);
        this.listView.setOverScrollMode(2);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDurations(350L);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda26
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i) {
                StarsIntroActivity.this.lambda$createView$0(view2, i);
            }
        });
        FireworksOverlay fireworksOverlay = new FireworksOverlay(getContext());
        this.fireworksOverlay = fireworksOverlay;
        this.contentView.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view, int i) {
        UItem item;
        UniversalAdapter universalAdapter = this.adapter;
        if (universalAdapter == null || (item = universalAdapter.getItem(i)) == null) {
            return;
        }
        onItemClick(item, i);
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected GradientHeaderActivity.ContentView createContentView() {
        return new NestedFrameLayout(getContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class NestedFrameLayout extends GradientHeaderActivity.ContentView {
        private NestedScrollingParentHelper nestedScrollingParentHelper;

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
            return i == 2;
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
        }

        public NestedFrameLayout(Context context) {
            super(context);
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent3
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
            try {
                if (view == ((GradientHeaderActivity) StarsIntroActivity.this).listView && StarsIntroActivity.this.transactionsLayout.isAttachedToWindow()) {
                    RecyclerListView currentListView = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                    if (((GradientHeaderActivity) StarsIntroActivity.this).listView.getHeight() - ((View) StarsIntroActivity.this.transactionsLayout.getParent()).getBottom() >= 0) {
                        iArr[1] = i4;
                        currentListView.scrollBy(0, i4);
                    }
                }
            } catch (Throwable th) {
                FileLog.e(th);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$NestedFrameLayout$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.NestedFrameLayout.this.lambda$onNestedScroll$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNestedScroll$0() {
            try {
                RecyclerListView currentListView = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                if (currentListView == null || currentListView.getAdapter() == null) {
                    return;
                }
                currentListView.getAdapter().notifyDataSetChanged();
            } catch (Throwable unused) {
            }
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedPreFling(View view, float f, float f2) {
            return super.onNestedPreFling(view, f, f2);
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
            if (view == ((GradientHeaderActivity) StarsIntroActivity.this).listView && StarsIntroActivity.this.transactionsLayout.isAttachedToWindow()) {
                boolean isSearchFieldVisible = ((BaseFragment) StarsIntroActivity.this).actionBar.isSearchFieldVisible();
                int top = (((View) StarsIntroActivity.this.transactionsLayout.getParent()).getTop() - AndroidUtilities.statusBarHeight) - ActionBar.getCurrentActionBarHeight();
                int bottom = ((View) StarsIntroActivity.this.transactionsLayout.getParent()).getBottom();
                boolean z = false;
                if (i2 >= 0) {
                    if (isSearchFieldVisible) {
                        RecyclerListView currentListView = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                        iArr[1] = i2;
                        if (top > 0) {
                            iArr[1] = iArr[1] - i2;
                        }
                        if (currentListView == null || iArr[1] <= 0) {
                            return;
                        }
                        currentListView.scrollBy(0, iArr[1]);
                        return;
                    }
                    return;
                }
                if (((GradientHeaderActivity) StarsIntroActivity.this).listView.getHeight() - bottom >= 0) {
                    RecyclerListView currentListView2 = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) currentListView2.getLayoutManager()).findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition != -1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = currentListView2.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        int top2 = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : -1;
                        int paddingTop = currentListView2.getPaddingTop();
                        if (top2 != paddingTop || findFirstVisibleItemPosition != 0) {
                            iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, top2 - paddingTop);
                            currentListView2.scrollBy(0, i2);
                            z = true;
                        }
                    }
                }
                if (isSearchFieldVisible) {
                    if (!z && top < 0) {
                        iArr[1] = i2 - Math.max(top, i2);
                    } else {
                        iArr[1] = i2;
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onStopNestedScroll(View view, int i) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
        }
    }

    public boolean attachedTransactionsLayout() {
        StarsTransactionsLayout starsTransactionsLayout = this.transactionsLayout;
        if (starsTransactionsLayout == null || !(starsTransactionsLayout.getParent() instanceof View)) {
            return false;
        }
        return this.listView.getHeight() - ((View) this.transactionsLayout.getParent()).getBottom() >= 0;
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected boolean drawActionBarShadow() {
        return !attachedTransactionsLayout();
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    public StarParticlesView createParticlesView() {
        return makeParticlesView(getContext(), 75, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 extends StarParticlesView {
        Paint[] paints;
        final /* synthetic */ int val$particlesCount;
        final /* synthetic */ int val$type;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        2(Context context, int i, int i2) {
            super(context);
            this.val$particlesCount = i;
            this.val$type = i2;
            setClipWithGradient();
        }

        @Override // org.telegram.ui.Components.Premium.StarParticlesView
        protected void configure() {
            StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(this.val$particlesCount);
            this.drawable = drawable;
            drawable.type = R.styleable.AppCompatTheme_textAppearanceListItemSmall;
            int i = 0;
            drawable.roundEffect = false;
            drawable.useRotate = false;
            drawable.useBlur = true;
            drawable.checkBounds = true;
            drawable.isCircle = false;
            drawable.useScale = true;
            drawable.startFromCenter = true;
            if (this.val$type == 1) {
                drawable.centerOffsetY = AndroidUtilities.dp(24.0f);
            }
            this.paints = new Paint[20];
            while (true) {
                Paint[] paintArr = this.paints;
                if (i < paintArr.length) {
                    paintArr[i] = new Paint(1);
                    this.paints[i].setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-371690, -14281, i / (this.paints.length - 1)), PorterDuff.Mode.SRC_IN));
                    i++;
                } else {
                    this.drawable.getPaint = new Utilities.CallbackReturn() { // from class: org.telegram.ui.Stars.StarsIntroActivity$2$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.CallbackReturn
                        public final Object run(Object obj) {
                            Paint lambda$configure$0;
                            lambda$configure$0 = StarsIntroActivity.2.this.lambda$configure$0((Integer) obj);
                            return lambda$configure$0;
                        }
                    };
                    StarParticlesView.Drawable drawable2 = this.drawable;
                    drawable2.size1 = 17;
                    drawable2.size2 = 18;
                    drawable2.size3 = 19;
                    drawable2.colorKey = Theme.key_windowBackgroundWhiteBlackText;
                    drawable2.init();
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Paint lambda$configure$0(Integer num) {
            return this.paints[num.intValue() % this.paints.length];
        }

        @Override // org.telegram.ui.Components.Premium.StarParticlesView
        protected int getStarsRectWidth() {
            return getMeasuredWidth();
        }
    }

    public static StarParticlesView makeParticlesView(Context context, int i, int i2) {
        return new 2(context, i, i2);
    }

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        GLIconTextureView gLIconTextureView = this.iconTextureView;
        if (gLIconTextureView != null) {
            gLIconTextureView.setPaused(false);
            this.iconTextureView.setDialogVisible(false);
        }
    }

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        GLIconTextureView gLIconTextureView = this.iconTextureView;
        if (gLIconTextureView != null) {
            gLIconTextureView.setPaused(true);
            this.iconTextureView.setDialogVisible(true);
        }
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected View getHeader(Context context) {
        return super.getHeader(context);
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected RecyclerView.Adapter<?> createAdapter() {
        UniversalAdapter universalAdapter = new UniversalAdapter(this.listView, getContext(), this.currentAccount, this.classGuid, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda23
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                StarsIntroActivity.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, getResourceProvider()) { // from class: org.telegram.ui.Stars.StarsIntroActivity.3
            @Override // org.telegram.ui.Components.UniversalAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                if (i == 0) {
                    HeaderCell headerCell = new HeaderCell(StarsIntroActivity.this.getContext(), Theme.key_windowBackgroundWhiteBlueHeader, 21, 0, false, ((BaseFragment) StarsIntroActivity.this).resourceProvider);
                    headerCell.setHeight(25);
                    return new RecyclerListView.Holder(headerCell);
                }
                return super.onCreateViewHolder(viewGroup, i);
            }
        };
        this.adapter = universalAdapter;
        return universalAdapter;
    }

    public void fillItems(ArrayList<UItem> arrayList, UniversalAdapter universalAdapter) {
        if (getContext() == null) {
            return;
        }
        StarsController starsController = StarsController.getInstance(this.currentAccount);
        arrayList.add(UItem.asFullyCustom(getHeader(getContext())));
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
        ArrayList<TLRPC$TL_starsTopupOption> options = starsController.getOptions();
        if (options != null && !options.isEmpty()) {
            int i = 0;
            int i2 = 1;
            for (int i3 = 0; i3 < options.size(); i3++) {
                TLRPC$TL_starsTopupOption tLRPC$TL_starsTopupOption = options.get(i3);
                if (!tLRPC$TL_starsTopupOption.collapsed || this.expanded) {
                    arrayList.add(StarTierView.Factory.asStarTier(i3, i2, tLRPC$TL_starsTopupOption));
                    i2++;
                } else {
                    i++;
                }
            }
            boolean z = this.expanded;
            if (!z && i > 0) {
                arrayList.add(ExpandView.Factory.asExpand(-1, LocaleController.getString(z ? R.string.NotifyLessOptions : R.string.NotifyMoreOptions), true ^ this.expanded).accent());
            }
        } else {
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
        }
        arrayList.add(UItem.asShadow(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.this.lambda$fillItems$1();
            }
        })));
        arrayList.add(UItem.asFullscreenCustom(this.transactionsLayout, ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillItems$1() {
        Browser.openUrl(getContext(), LocaleController.getString(R.string.StarsTOSLink));
    }

    public void onItemClick(final UItem uItem, int i) {
        if (uItem.id == -1) {
            this.expanded = !this.expanded;
            this.adapter.update(true);
        } else if (uItem.instanceOf(StarTierView.Factory.class) && (uItem.object instanceof TLRPC$TL_starsTopupOption)) {
            StarsController.getInstance(this.currentAccount).buy(getParentActivity(), (TLRPC$TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda24
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.this.lambda$onItemClick$2(uItem, (Boolean) obj, (String) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemClick$2(UItem uItem, Boolean bool, String str) {
        if (getContext() == null) {
            return;
        }
        if (bool.booleanValue()) {
            BulletinFactory.of(this).createSimpleBulletin(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate(), LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
            this.fireworksOverlay.start(true);
            StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        } else if (str != null) {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsBalanceView extends LinearLayout implements NotificationCenter.NotificationCenterDelegate {
        private final AnimatedTextView amountTextView;
        private ValueAnimator bounceAnimator;
        private final int currentAccount;
        private final TextView headerTextView;
        private long lastBalance;
        private SpannableString loadingString;

        public StarsBalanceView(Context context, int i) {
            super(context);
            this.lastBalance = -1L;
            this.currentAccount = i;
            setOrientation(1);
            setGravity(21);
            TextView textView = new TextView(context);
            this.headerTextView = textView;
            int i2 = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(i2));
            textView.setTextSize(1, 13.0f);
            textView.setText(LocaleController.getString(R.string.StarsBalance));
            textView.setGravity(5);
            textView.setTypeface(AndroidUtilities.bold());
            addView(textView, LayoutHelper.createLinear(-2, -2, 5));
            final Drawable mutate = context.getResources().getDrawable(R.drawable.star_small_inner).mutate();
            AnimatedTextView animatedTextView = new AnimatedTextView(this, context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.StarsBalanceView.1
                @Override // android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    int measuredWidth = (int) ((getMeasuredWidth() - getDrawable().getCurrentWidth()) - AndroidUtilities.dp(20.0f));
                    mutate.setBounds(measuredWidth, (getMeasuredHeight() - AndroidUtilities.dp(17.0f)) / 2, AndroidUtilities.dp(17.0f) + measuredWidth, (getMeasuredHeight() + AndroidUtilities.dp(17.0f)) / 2);
                    mutate.draw(canvas);
                    super.dispatchDraw(canvas);
                }
            };
            this.amountTextView = animatedTextView;
            animatedTextView.adaptWidth = true;
            animatedTextView.getDrawable().setHacks(false, true, true);
            animatedTextView.setTypeface(AndroidUtilities.bold());
            animatedTextView.setTextColor(Theme.getColor(i2));
            animatedTextView.setTextSize(AndroidUtilities.dp(13.0f));
            animatedTextView.setGravity(5);
            animatedTextView.setPadding(AndroidUtilities.dp(19.0f), 0, 0, 0);
            addView(animatedTextView, LayoutHelper.createLinear(-2, 20, 5, 0, -2, 0, 0));
            updateBalance(false);
            setPadding(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(15.0f), AndroidUtilities.dp(4.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateBalance(false);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.starBalanceUpdated) {
                updateBalance(true);
            }
        }

        public void updateBalance(boolean z) {
            StarsController starsController = StarsController.getInstance(this.currentAccount);
            this.amountTextView.cancelAnimation();
            long balance = StarsController.getInstance(this.currentAccount).getBalance();
            long j = this.lastBalance;
            if (balance > j && j != -1) {
                bounce();
            }
            if (!starsController.balanceAvailable()) {
                if (this.loadingString == null) {
                    SpannableString spannableString = new SpannableString("x");
                    this.loadingString = spannableString;
                    spannableString.setSpan(new LoadingSpan(this.amountTextView, AndroidUtilities.dp(48.0f)), 0, this.loadingString.length(), 33);
                }
                this.amountTextView.setText(this.loadingString, z);
                this.lastBalance = -1L;
                return;
            }
            this.amountTextView.setText(LocaleController.formatNumber(balance, ' '));
            this.lastBalance = balance;
        }

        public void bounce() {
            ValueAnimator valueAnimator = this.bounceAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.9f, 1.0f);
            this.bounceAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsBalanceView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    StarsIntroActivity.StarsBalanceView.this.lambda$bounce$0(valueAnimator2);
                }
            });
            this.bounceAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.StarsIntroActivity.StarsBalanceView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    StarsBalanceView.this.amountTextView.setScaleX(1.0f);
                    StarsBalanceView.this.amountTextView.setScaleY(1.0f);
                }
            });
            this.bounceAnimator.setDuration(320L);
            this.bounceAnimator.setInterpolator(new OvershootInterpolator());
            this.bounceAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bounce$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.amountTextView.setScaleX(floatValue);
            this.amountTextView.setScaleY(floatValue);
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(ActionBar.getCurrentActionBarHeight(), 1073741824));
        }
    }

    /* loaded from: classes3.dex */
    public static class StarTierView extends FrameLayout {
        private final AnimatedFloat animatedStarsCount;
        private SpannableString loading;
        private boolean needDivider;
        private final Drawable starDrawable;
        private final Drawable starDrawableOutline;
        private int starsCount;
        private final TextView textView;
        private final AnimatedTextView textView2;

        public StarTierView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.animatedStarsCount = new AnimatedFloat(this, 0L, 500L, CubicBezierInterpolator.EASE_OUT_QUINT);
            Drawable mutate = context.getResources().getDrawable(R.drawable.star_small_outline).mutate();
            this.starDrawableOutline = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground, resourcesProvider), PorterDuff.Mode.SRC_IN));
            this.starDrawable = context.getResources().getDrawable(R.drawable.star_small_inner).mutate();
            setWillNotDraw(false);
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTypeface(AndroidUtilities.bold());
            textView.setTextSize(1, 15.0f);
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            addView(textView, LayoutHelper.createFrameRelatively(-2.0f, -2.0f, 8388627, 48.0f, 0.0f, 0.0f, 0.0f));
            AnimatedTextView animatedTextView = new AnimatedTextView(context);
            this.textView2 = animatedTextView;
            animatedTextView.setTextSize(AndroidUtilities.dp(15.0f));
            animatedTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
            animatedTextView.setGravity(LocaleController.isRTL ? 3 : 5);
            addView(animatedTextView, LayoutHelper.createFrameRelatively(-2.0f, 21.0f, 8388629, 0.0f, 0.0f, 19.0f, 0.0f));
        }

        public void set(int i, CharSequence charSequence, CharSequence charSequence2, boolean z) {
            boolean equals = TextUtils.equals(this.textView.getText(), charSequence);
            this.starsCount = i;
            if (!equals) {
                this.animatedStarsCount.set(i, true);
            }
            this.textView.setText(charSequence);
            if (charSequence2 == null) {
                if (this.loading == null) {
                    SpannableString spannableString = new SpannableString("x");
                    this.loading = spannableString;
                    spannableString.setSpan(new LoadingSpan(this.textView2, AndroidUtilities.dp(55.0f)), 0, this.loading.length(), 33);
                }
                charSequence2 = this.loading;
            }
            this.textView2.setText(charSequence2);
            float f = LocaleController.isRTL ? -1.0f : 1.0f;
            if (equals) {
                this.textView.animate().translationX(f * (i - 1) * AndroidUtilities.dp(2.66f)).setDuration(320L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
            } else {
                this.textView.setTranslationX(f * (i - 1) * AndroidUtilities.dp(2.66f));
            }
            this.needDivider = z;
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float f = this.animatedStarsCount.set(this.starsCount);
            float f2 = LocaleController.isRTL ? -1.0f : 1.0f;
            float dp = AndroidUtilities.dp(24.0f);
            float dp2 = AndroidUtilities.dp(24.0f);
            float dp3 = AndroidUtilities.dp(2.5f);
            float width = LocaleController.isRTL ? (getWidth() - AndroidUtilities.dp(19.0f)) - dp : AndroidUtilities.dp(19.0f);
            int ceil = (int) Math.ceil(f);
            while (true) {
                ceil--;
                if (ceil < 0) {
                    break;
                }
                float clamp = Utilities.clamp(f - ceil, 1.0f, 0.0f);
                float f3 = (((ceil - 1) - (1.0f - clamp)) * dp3 * f2) + width;
                float measuredHeight = (getMeasuredHeight() - dp2) / 2.0f;
                int i = (int) f3;
                int i2 = (int) measuredHeight;
                int i3 = (int) (f3 + dp);
                int i4 = (int) (measuredHeight + dp2);
                this.starDrawableOutline.setBounds(i, i2, i3, i4);
                int i5 = (int) (clamp * 255.0f);
                this.starDrawableOutline.setAlpha(i5);
                this.starDrawableOutline.draw(canvas);
                this.starDrawable.setBounds(i, i2, i3, i4);
                this.starDrawable.setAlpha(i5);
                this.starDrawable.draw(canvas);
            }
            if (this.needDivider) {
                canvas.drawRect(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(22.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(22.0f) : 0), getMeasuredHeight(), Theme.dividerPaint);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory<StarTierView> {
            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public StarTierView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                return new StarTierView(context, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((StarTierView) view).set(uItem.intValue, uItem.text, uItem.subtext, z);
            }

            public static UItem asStarTier(int i, int i2, TLRPC$TL_starsTopupOption tLRPC$TL_starsTopupOption) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.id = i;
                ofFactory.intValue = i2;
                long j = tLRPC$TL_starsTopupOption.stars;
                ofFactory.longValue = j;
                ofFactory.text = LocaleController.formatPluralString("StarsCount", (int) j, new Object[0]);
                ofFactory.subtext = tLRPC$TL_starsTopupOption.loadingStorePrice ? null : BillingController.getInstance().formatCurrency(tLRPC$TL_starsTopupOption.amount, tLRPC$TL_starsTopupOption.currency);
                ofFactory.object = tLRPC$TL_starsTopupOption;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public boolean equals(UItem uItem, UItem uItem2) {
                return uItem.id == uItem2.id;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public boolean contentsEquals(UItem uItem, UItem uItem2) {
                return uItem.intValue == uItem2.intValue && uItem.id == uItem2.id && TextUtils.equals(uItem.subtext, uItem2.subtext);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class ExpandView extends FrameLayout {
        public final ImageView arrowView;
        private int lastId;
        private boolean needDivider;
        public final AnimatedTextView textView;

        public ExpandView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            AnimatedTextView animatedTextView = new AnimatedTextView(context);
            this.textView = animatedTextView;
            animatedTextView.getDrawable().setHacks(true, true, true);
            animatedTextView.setTextSize(AndroidUtilities.dp(15.0f));
            addView(animatedTextView, LayoutHelper.createFrameRelatively(-1.0f, -1.0f, 8388627, 22.0f, 0.0f, 58.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            this.arrowView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(R.drawable.arrow_more);
            addView(imageView, LayoutHelper.createFrameRelatively(24.0f, 24.0f, 8388629, 0.0f, 0.0f, 17.0f, 0.0f));
        }

        public void set(UItem uItem, boolean z) {
            int i = this.lastId;
            int i2 = uItem.id;
            boolean z2 = i == i2;
            this.lastId = i2;
            this.textView.setText(uItem.text, z2);
            int color = Theme.getColor(uItem.accent ? Theme.key_windowBackgroundWhiteBlueText2 : Theme.key_windowBackgroundWhiteBlackText);
            this.textView.setTextColor(color);
            this.arrowView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            if (z2) {
                this.arrowView.animate().rotation(uItem.collapsed ? 0.0f : 180.0f).setDuration(340L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            } else {
                this.arrowView.setRotation(uItem.collapsed ? 0.0f : 180.0f);
            }
            this.needDivider = z;
            setWillNotDraw(!z);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.needDivider) {
                canvas.drawRect(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(22.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(22.0f) : 0), getMeasuredHeight(), Theme.dividerPaint);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.lastId = ConnectionsManager.DEFAULT_DATACENTER_ID;
        }

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory<ExpandView> {
            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public ExpandView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                return new ExpandView(context, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((ExpandView) view).set(uItem, z);
            }

            public static UItem asExpand(int i, CharSequence charSequence, boolean z) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.id = i;
                ofFactory.text = charSequence;
                ofFactory.collapsed = z;
                return ofFactory;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsTransactionsLayout extends LinearLayout implements NotificationCenter.NotificationCenterDelegate {
        private final PageAdapter adapter;
        private final int currentAccount;
        private final ViewPagerFixed.TabsView tabsView;
        private final ViewPagerFixed viewPager;

        /* loaded from: classes3.dex */
        private static class PageAdapter extends ViewPagerFixed.Adapter {
            private final long bot_id;
            private final int classGuid;
            private final Context context;
            private final int currentAccount;
            private final ArrayList<UItem> items = new ArrayList<>();
            private final Theme.ResourcesProvider resourcesProvider;

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i, int i2) {
            }

            public PageAdapter(Context context, int i, long j, int i2, Theme.ResourcesProvider resourcesProvider) {
                this.context = context;
                this.currentAccount = i;
                this.classGuid = i2;
                this.resourcesProvider = resourcesProvider;
                this.bot_id = j;
                fill();
            }

            public void fill() {
                this.items.clear();
                if (this.bot_id == 0) {
                    StarsController starsController = StarsController.getInstance(this.currentAccount);
                    this.items.add(UItem.asSpace(0));
                    if (starsController.hasTransactions(1)) {
                        this.items.add(UItem.asSpace(1));
                    }
                    if (starsController.hasTransactions(2)) {
                        this.items.add(UItem.asSpace(2));
                        return;
                    }
                    return;
                }
                BotStarsController botStarsController = BotStarsController.getInstance(this.currentAccount);
                this.items.add(UItem.asSpace(0));
                if (botStarsController.hasTransactions(this.bot_id, 1)) {
                    this.items.add(UItem.asSpace(1));
                }
                if (botStarsController.hasTransactions(this.bot_id, 2)) {
                    this.items.add(UItem.asSpace(2));
                }
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return this.items.size();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i) {
                return new Page(this.context, this.bot_id, i, this.currentAccount, this.classGuid, this.resourcesProvider);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i) {
                if (i < 0 || i >= this.items.size()) {
                    return 0;
                }
                return this.items.get(i).intValue;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public String getItemTitle(int i) {
                int itemViewType = getItemViewType(i);
                if (itemViewType != 0) {
                    if (itemViewType != 1) {
                        return itemViewType != 2 ? "" : LocaleController.getString(R.string.StarsTransactionsOutgoing);
                    }
                    return LocaleController.getString(R.string.StarsTransactionsIncoming);
                }
                return LocaleController.getString(R.string.StarsTransactionsAll);
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.starTransactionsLoaded) {
                this.adapter.fill();
                this.viewPager.fillTabs(true);
            }
        }

        public StarsTransactionsLayout(Context context, int i, long j, int i2, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.currentAccount = i;
            setOrientation(1);
            ViewPagerFixed viewPagerFixed = new ViewPagerFixed(context);
            this.viewPager = viewPagerFixed;
            PageAdapter pageAdapter = new PageAdapter(context, i, j, i2, resourcesProvider);
            this.adapter = pageAdapter;
            viewPagerFixed.setAdapter(pageAdapter);
            ViewPagerFixed.TabsView createTabsView = viewPagerFixed.createTabsView(true, 3);
            this.tabsView = createTabsView;
            View view = new View(context);
            view.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
            addView(createTabsView, LayoutHelper.createLinear(-1, 48));
            addView(view, LayoutHelper.createLinear(-1.0f, 1.0f / AndroidUtilities.density));
            addView(viewPagerFixed, LayoutHelper.createLinear(-1, -1));
            setBackgroundColor(Theme.getColor(Theme.key_dialogBackground, resourcesProvider));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            this.adapter.fill();
            this.viewPager.fillTabs(false);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starTransactionsLoaded);
            super.onAttachedToWindow();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starTransactionsLoaded);
            super.onDetachedFromWindow();
        }

        public RecyclerListView getCurrentListView() {
            View currentView = this.viewPager.getCurrentView();
            if (currentView instanceof Page) {
                return ((Page) currentView).listView;
            }
            return null;
        }

        /* loaded from: classes3.dex */
        public static class Page extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
            private final long bot_id;
            private final int currentAccount;
            private final UniversalRecyclerView listView;
            private final Runnable loadTransactionsRunnable;
            private final Theme.ResourcesProvider resourcesProvider;
            private final int type;

            public Page(Context context, final long j, final int i, final int i2, int i3, Theme.ResourcesProvider resourcesProvider) {
                super(context);
                this.type = i;
                this.currentAccount = i2;
                this.bot_id = j;
                this.resourcesProvider = resourcesProvider;
                this.loadTransactionsRunnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsTransactionsLayout$Page$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.StarsTransactionsLayout.Page.lambda$new$0(j, i2, i);
                    }
                };
                UniversalRecyclerView universalRecyclerView = new UniversalRecyclerView(context, i2, i3, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsTransactionsLayout$Page$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        StarsIntroActivity.StarsTransactionsLayout.Page.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
                    }
                }, new Utilities.Callback5() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsTransactionsLayout$Page$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback5
                    public final void run(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                        StarsIntroActivity.StarsTransactionsLayout.Page.this.onClick((UItem) obj, (View) obj2, ((Integer) obj3).intValue(), ((Float) obj4).floatValue(), ((Float) obj5).floatValue());
                    }
                }, null, resourcesProvider);
                this.listView = universalRecyclerView;
                addView(universalRecyclerView, LayoutHelper.createFrame(-1, -1.0f));
                universalRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity.StarsTransactionsLayout.Page.1
                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                    public void onScrolled(RecyclerView recyclerView, int i4, int i5) {
                        Page.this.scheduleLoadTransactions();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ void lambda$new$0(long j, int i, int i2) {
                if (j != 0) {
                    BotStarsController.getInstance(i).loadTransactions(j, i2);
                } else {
                    StarsController.getInstance(i).loadTransactions(i2);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void scheduleLoadTransactions() {
                if (this.listView.canScrollVertically(1)) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(this.loadTransactionsRunnable);
                AndroidUtilities.runOnUIThread(this.loadTransactionsRunnable, 250L);
            }

            @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
            public void didReceivedNotification(int i, int i2, Object... objArr) {
                if (i == NotificationCenter.starTransactionsLoaded) {
                    this.listView.adapter.update(true);
                } else if (i == NotificationCenter.botStarsTransactionsLoaded && ((Long) objArr[0]).longValue() == this.bot_id) {
                    this.listView.adapter.update(true);
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                if (this.bot_id != 0) {
                    NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.botStarsTransactionsLoaded);
                } else {
                    NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starTransactionsLoaded);
                }
                this.listView.adapter.update(false);
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                if (this.bot_id != 0) {
                    NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.botStarsTransactionsLoaded);
                } else {
                    NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starTransactionsLoaded);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void fillItems(ArrayList<UItem> arrayList, UniversalAdapter universalAdapter) {
                if (this.bot_id != 0) {
                    BotStarsController botStarsController = BotStarsController.getInstance(this.currentAccount);
                    Iterator<TLRPC$StarsTransaction> it = botStarsController.getTransactions(this.bot_id, this.type).iterator();
                    while (it.hasNext()) {
                        arrayList.add(StarsTransactionView.Factory.asTransaction(it.next(), true));
                    }
                    if (botStarsController.didFullyLoadTransactions(this.bot_id, this.type)) {
                        return;
                    }
                    arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                    arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                    arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                    return;
                }
                StarsController starsController = StarsController.getInstance(this.currentAccount);
                Iterator<TLRPC$StarsTransaction> it2 = starsController.transactions[this.type].iterator();
                while (it2.hasNext()) {
                    arrayList.add(StarsTransactionView.Factory.asTransaction(it2.next(), false));
                }
                if (starsController.didFullyLoadTransactions(this.type)) {
                    return;
                }
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void onClick(UItem uItem, View view, int i, float f, float f2) {
                if (uItem.object instanceof TLRPC$StarsTransaction) {
                    StarsIntroActivity.showTransactionSheet(getContext(), false, 0L, this.currentAccount, (TLRPC$StarsTransaction) uItem.object, this.resourcesProvider);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsTransactionView extends LinearLayout {
        public static HashMap<String, Drawable> cachedPlatformDrawables;
        private final TextView amountTextView;
        private final AvatarDrawable avatarDrawable;
        private final int currentAccount;
        private final TextView dateTextView;
        private final BackupImageView imageView;
        private final BackupImageView imageView2;
        private final FrameLayout imageViewContainer;
        private int imageViewCount;
        private boolean needDivider;
        private final SpannableString star;
        private final TextView subtitleTextView;
        private final LinearLayout textLayout;
        private boolean threeLines;
        private final TextView titleTextView;
        private final LinearLayout.LayoutParams titleTextViewParams;

        public StarsTransactionView(Context context, int i, final Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.imageViewCount = 1;
            this.currentAccount = i;
            setOrientation(0);
            FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.StarsTransactionView.1
                private final Paint backgroundPaint = new Paint(1);

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j) {
                    if (StarsTransactionView.this.imageViewCount > 1) {
                        this.backgroundPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(view.getX(), view.getY(), view.getX() + view.getWidth(), view.getY() + view.getHeight());
                        rectF.inset(-AndroidUtilities.dp(1.66f), -AndroidUtilities.dp(1.66f));
                        canvas.drawRoundRect(rectF, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), this.backgroundPaint);
                    }
                    return super.drawChild(canvas, view, j);
                }
            };
            this.imageViewContainer = frameLayout;
            addView(frameLayout, LayoutHelper.createLinear(72, -1, (int) R.styleable.AppCompatTheme_tooltipFrameBackground));
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView2 = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(46.0f));
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(46, 46.0f, 16, 13.0f, 0.0f, 13.0f, 0.0f));
            this.avatarDrawable = new AvatarDrawable();
            BackupImageView backupImageView2 = new BackupImageView(context);
            this.imageView = backupImageView2;
            backupImageView2.setRoundRadius(AndroidUtilities.dp(46.0f));
            frameLayout.addView(backupImageView2, LayoutHelper.createFrame(46, 46.0f, 16, 13.0f, 0.0f, 13.0f, 0.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            this.textLayout = linearLayout;
            linearLayout.setOrientation(1);
            linearLayout.setGravity(19);
            addView(linearLayout, LayoutHelper.createLinear(-2, -1, 119));
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setTypeface(AndroidUtilities.bold());
            int i2 = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView.setTextSize(1, 16.0f);
            LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 0.0f, 4.33f);
            this.titleTextViewParams = createLinear;
            linearLayout.addView(textView, createLinear);
            TextView textView2 = new TextView(context);
            this.subtitleTextView = textView2;
            textView2.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView2.setTextSize(1, 13.0f);
            linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 0.0f, 0.33f));
            TextView textView3 = new TextView(context);
            this.dateTextView = textView3;
            textView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
            textView3.setTextSize(1, 14.0f);
            linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2));
            TextView textView4 = new TextView(context);
            this.amountTextView = textView4;
            textView4.setTypeface(AndroidUtilities.bold());
            textView4.setTextSize(1, 15.3f);
            textView4.setGravity(5);
            addView(textView4, LayoutHelper.createLinear(-1, -2, 21, 0, 0, 20, 0));
            SpannableString spannableString = new SpannableString("⭐️");
            this.star = spannableString;
            Drawable mutate = context.getResources().getDrawable(R.drawable.star_small_inner).mutate();
            mutate.setBounds(0, 0, AndroidUtilities.dp(21.0f), AndroidUtilities.dp(21.0f));
            spannableString.setSpan(new ImageSpan(mutate), 0, spannableString.length(), 33);
        }

        public static Drawable getPlatformDrawable(String str) {
            if (cachedPlatformDrawables == null) {
                cachedPlatformDrawables = new HashMap<>();
            }
            Drawable drawable = cachedPlatformDrawables.get(str);
            if (drawable == null) {
                HashMap<String, Drawable> hashMap = cachedPlatformDrawables;
                Drawable createDrawable = SessionCell.createDrawable(44, str);
                hashMap.put(str, createDrawable);
                return createDrawable;
            }
            return drawable;
        }

        public void set(TLRPC$StarsTransaction tLRPC$StarsTransaction, boolean z, boolean z2) {
            boolean z3;
            String str;
            ImageLocation imageLocation;
            ImageLocation forDocument;
            long peerDialogId = DialogObject.getPeerDialogId(tLRPC$StarsTransaction.peer.peer);
            boolean z4 = peerDialogId != 0;
            this.threeLines = z4;
            this.titleTextViewParams.bottomMargin = z4 ? 0 : AndroidUtilities.dp(4.33f);
            this.subtitleTextView.setVisibility(this.threeLines ? 0 : 8);
            this.dateTextView.setTextSize(1, this.threeLines ? 13.0f : 14.0f);
            this.dateTextView.setText(LocaleController.formatShortDateTime(tLRPC$StarsTransaction.date));
            if (tLRPC$StarsTransaction.refund) {
                TextView textView = this.dateTextView;
                textView.setText(TextUtils.concat(textView.getText(), " — ", LocaleController.getString(R.string.StarsRefunded)));
            } else if (tLRPC$StarsTransaction.failed) {
                TextView textView2 = this.dateTextView;
                textView2.setText(TextUtils.concat(textView2.getText(), " — ", LocaleController.getString(R.string.StarsFailed)));
            } else if (tLRPC$StarsTransaction.pending) {
                TextView textView3 = this.dateTextView;
                textView3.setText(TextUtils.concat(textView3.getText(), " — ", LocaleController.getString(R.string.StarsPending)));
            }
            this.imageView.setTranslationX(0.0f);
            this.imageView.setTranslationY(0.0f);
            this.imageView2.setVisibility(8);
            this.imageView.setRoundRadius(AndroidUtilities.dp(46.0f));
            if (peerDialogId != 0) {
                if (peerDialogId >= 0) {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId));
                    z3 = user == null;
                    if (tLRPC$StarsTransaction.photo == null) {
                        this.avatarDrawable.setInfo(user);
                        this.imageView.setForUserOrChat(user, this.avatarDrawable);
                    }
                    str = UserObject.getUserName(user);
                } else {
                    TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId));
                    z3 = chat == null;
                    if (tLRPC$StarsTransaction.photo == null) {
                        this.avatarDrawable.setInfo(chat);
                        this.imageView.setForUserOrChat(chat, this.avatarDrawable);
                    }
                    str = chat == null ? "" : chat.title;
                }
                if (!tLRPC$StarsTransaction.extended_media.isEmpty()) {
                    if (z) {
                        this.titleTextView.setText(str);
                        this.subtitleTextView.setVisibility(0);
                        this.subtitleTextView.setText(LocaleController.getString(R.string.StarMediaPurchase));
                    } else {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarMediaPurchase));
                        this.subtitleTextView.setVisibility(z3 ? 8 : 0);
                        this.subtitleTextView.setText(str);
                    }
                    this.imageViewCount = 0;
                    int i = 0;
                    while (i < Math.min(2, tLRPC$StarsTransaction.extended_media.size())) {
                        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StarsTransaction.extended_media.get(i);
                        BackupImageView backupImageView = i == 0 ? this.imageView : this.imageView2;
                        backupImageView.setRoundRadius(AndroidUtilities.dp(12.0f));
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                            forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.photo.sizes, AndroidUtilities.dp(46.0f), true), tLRPC$MessageMedia.photo);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.document.thumbs, AndroidUtilities.dp(46.0f), true), tLRPC$MessageMedia.document);
                        } else {
                            imageLocation = null;
                            backupImageView.setVisibility(0);
                            backupImageView.setImage(imageLocation, "46_46", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                            this.imageViewCount++;
                            i++;
                        }
                        imageLocation = forDocument;
                        backupImageView.setVisibility(0);
                        backupImageView.setImage(imageLocation, "46_46", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                        this.imageViewCount++;
                        i++;
                    }
                    int i2 = 0;
                    while (i2 < this.imageViewCount) {
                        BackupImageView backupImageView2 = i2 == 0 ? this.imageView : this.imageView2;
                        float f = i2;
                        backupImageView2.setTranslationX(AndroidUtilities.dp(2.0f) + ((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f)));
                        backupImageView2.setTranslationY((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f));
                        i2++;
                    }
                } else if (tLRPC$StarsTransaction.photo != null) {
                    TextView textView4 = this.titleTextView;
                    String str2 = tLRPC$StarsTransaction.title;
                    textView4.setText(str2 != null ? str2 : "");
                    this.subtitleTextView.setVisibility(z3 ? 8 : 0);
                    this.subtitleTextView.setText(str);
                    this.imageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$StarsTransaction.photo)), "46_46", (Drawable) null, 0, (Object) null);
                } else {
                    TextView textView5 = this.titleTextView;
                    String str3 = tLRPC$StarsTransaction.title;
                    textView5.setText(str3 != null ? str3 : "");
                    this.subtitleTextView.setVisibility(z3 ? 8 : 0);
                    this.subtitleTextView.setText(str);
                }
            } else {
                TLRPC$StarsTransactionPeer tLRPC$StarsTransactionPeer = tLRPC$StarsTransaction.peer;
                if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerAppStore) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionInApp));
                    this.imageView.setImageDrawable(getPlatformDrawable("ios"));
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerPlayMarket) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionInApp));
                    this.imageView.setImageDrawable(getPlatformDrawable("android"));
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerFragment) {
                    this.titleTextView.setText(LocaleController.getString(z ? R.string.StarsTransactionWithdrawFragment : R.string.StarsTransactionFragment));
                    this.imageView.setImageDrawable(getPlatformDrawable("fragment"));
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerPremiumBot) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionBot));
                    this.imageView.setImageDrawable(getPlatformDrawable("premiumbot"));
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerUnsupported) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionUnsupported));
                    this.imageView.setImageDrawable(getPlatformDrawable("?"));
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerAds) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionAds));
                    this.imageView.setImageDrawable(getPlatformDrawable("ads"));
                } else {
                    this.titleTextView.setText("");
                    this.imageView.setImageDrawable(null);
                }
            }
            long j = tLRPC$StarsTransaction.stars;
            if (j > 0) {
                this.amountTextView.setVisibility(0);
                this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_green));
                this.amountTextView.setText(TextUtils.concat("+", LocaleController.formatNumber(tLRPC$StarsTransaction.stars, ' '), " ", this.star));
            } else if (j < 0) {
                this.amountTextView.setVisibility(0);
                this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_red));
                this.amountTextView.setText(TextUtils.concat("-", LocaleController.formatNumber(-tLRPC$StarsTransaction.stars, ' '), " ", this.star));
            } else {
                this.amountTextView.setVisibility(8);
            }
            this.needDivider = z2;
            setWillNotDraw(!z2);
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.needDivider) {
                canvas.drawRect(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(72.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(72.0f) : 0), getMeasuredHeight(), Theme.dividerPaint);
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.threeLines ? 71.0f : 58.0f), 1073741824));
        }

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory<StarsTransactionView> {
            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public StarsTransactionView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                StarsTransactionView cached = getCached();
                return cached != null ? cached : new StarsTransactionView(context, i, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((StarsTransactionView) view).set((TLRPC$StarsTransaction) uItem.object, uItem.accent, z);
            }

            public static UItem asTransaction(TLRPC$StarsTransaction tLRPC$StarsTransaction, boolean z) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.object = tLRPC$StarsTransaction;
                ofFactory.accent = z;
                return ofFactory;
            }
        }
    }

    public static BottomSheet openConfirmPurchaseSheet(Context context, Theme.ResourcesProvider resourcesProvider, int i, MessageObject messageObject, long j, String str, long j2, TLRPC$WebDocument tLRPC$WebDocument, final Utilities.Callback<Utilities.Callback<Boolean>> callback, final Runnable runnable) {
        FrameLayout frameLayout;
        BottomSheet.Builder builder;
        int i2;
        int i3;
        TLRPC$Message tLRPC$Message;
        String str2;
        String formatPluralString;
        char c;
        String formatPluralString2;
        char c2;
        String formatPluralString3;
        boolean z;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Message tLRPC$Message2;
        BackupImageView backupImageView;
        FrameLayout frameLayout2;
        ImageLocation imageLocation;
        ImageLocation forDocument;
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        FrameLayout frameLayout3 = new FrameLayout(context);
        frameLayout3.addView(makeParticlesView(context, 40, 0), LayoutHelper.createFrame(-1, -1.0f));
        if (messageObject == null || (tLRPC$Message2 = messageObject.messageOwner) == null || !(tLRPC$Message2.media instanceof TLRPC$TL_messageMediaPaidMedia)) {
            frameLayout = frameLayout3;
            builder = builder2;
            i2 = 17;
            i3 = 1;
            if (tLRPC$WebDocument == null) {
                BackupImageView backupImageView2 = new BackupImageView(context);
                backupImageView2.setRoundRadius(AndroidUtilities.dp(80.0f));
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setInfo(user);
                backupImageView2.setForUserOrChat(user, avatarDrawable);
                frameLayout.addView(backupImageView2, LayoutHelper.createFrame(80, 80, 17));
            } else {
                BackupImageView backupImageView3 = new BackupImageView(context);
                backupImageView3.setRoundRadius(AndroidUtilities.dp(80.0f));
                backupImageView3.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$WebDocument)), "80_80", (Drawable) null, 0, (Object) null);
                frameLayout.addView(backupImageView3, LayoutHelper.createFrame(80, 80, 17));
            }
        } else {
            BackupImageView backupImageView4 = new BackupImageView(context, context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.4
                private Path clipPath = new Path();
                private RectF clipRect = new RectF();
                private Drawable lock;
                private SpoilerEffect2 spoilerEffect2;
                final /* synthetic */ Context val$context;

                {
                    this.val$context = context;
                    this.lock = context.getResources().getDrawable(R.drawable.large_locked_post).mutate();
                }

                @Override // android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    if (this.spoilerEffect2 == null) {
                        this.spoilerEffect2 = SpoilerEffect2.getInstance(this);
                    }
                    if (this.spoilerEffect2 != null) {
                        this.clipRect.set(0.0f, 0.0f, getWidth(), getHeight());
                        this.clipPath.rewind();
                        this.clipPath.addRoundRect(this.clipRect, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), Path.Direction.CW);
                        canvas.save();
                        canvas.clipPath(this.clipPath);
                        this.spoilerEffect2.draw(canvas, this, getWidth(), getHeight(), 1.0f);
                        canvas.restore();
                    }
                    this.lock.setBounds((getWidth() - this.lock.getIntrinsicWidth()) / 2, (getHeight() - this.lock.getIntrinsicHeight()) / 2, (getWidth() + this.lock.getIntrinsicWidth()) / 2, (getHeight() + this.lock.getIntrinsicHeight()) / 2);
                    this.lock.draw(canvas);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                public void onAttachedToWindow() {
                    SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
                    if (spoilerEffect2 != null) {
                        spoilerEffect2.attach(this);
                    }
                    super.onAttachedToWindow();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                public void onDetachedFromWindow() {
                    SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
                    if (spoilerEffect2 != null) {
                        spoilerEffect2.detach(this);
                    }
                    super.onDetachedFromWindow();
                }
            };
            backupImageView4.setRoundRadius(AndroidUtilities.dp(24.0f));
            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            if (tLRPC$TL_messageMediaPaidMedia.extended_media.isEmpty()) {
                backupImageView = backupImageView4;
                frameLayout2 = frameLayout3;
                builder = builder2;
                i3 = 1;
            } else {
                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(0);
                if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                    forDocument = ImageLocation.getForObject(((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).thumb, messageObject.messageOwner);
                } else {
                    if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                        TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                            forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.photo.sizes, AndroidUtilities.dp(80.0f), true), tLRPC$MessageMedia.photo);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.document.thumbs, AndroidUtilities.dp(80.0f), true), tLRPC$MessageMedia.document);
                        }
                    }
                    imageLocation = null;
                    backupImageView = backupImageView4;
                    frameLayout2 = frameLayout3;
                    builder = builder2;
                    i3 = 1;
                    backupImageView4.setImage(imageLocation, "80_80_b2", (ImageLocation) null, (String) null, (Drawable) null, messageObject);
                }
                imageLocation = forDocument;
                backupImageView = backupImageView4;
                frameLayout2 = frameLayout3;
                builder = builder2;
                i3 = 1;
                backupImageView4.setImage(imageLocation, "80_80_b2", (ImageLocation) null, (String) null, (Drawable) null, messageObject);
            }
            i2 = 17;
            frameLayout = frameLayout2;
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(80, 80, 17));
        }
        final StarsBalanceView starsBalanceView = new StarsBalanceView(context, i);
        ScaleStateListAnimator.apply(starsBalanceView);
        starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$3(StarsIntroActivity.StarsBalanceView.this, view);
            }
        });
        frameLayout.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, -8.0f, 0.0f));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 117, 7));
        TextView textView = new TextView(context);
        textView.setTextSize(i3, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        int i4 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i4, resourcesProvider));
        textView.setText(LocaleController.getString(R.string.StarsConfirmPurchaseTitle));
        textView.setGravity(i2);
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(i3, 14.0f);
        textView2.setTextColor(Theme.getColor(i4, resourcesProvider));
        if (messageObject != null && (tLRPC$Message = messageObject.messageOwner) != null && (tLRPC$Message.media instanceof TLRPC$TL_messageMediaPaidMedia)) {
            long dialogId = messageObject.getDialogId();
            TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
            if (tLRPC$Message3 != null && (tLRPC$MessageFwdHeader = tLRPC$Message3.fwd_from) != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.from_id) != null) {
                dialogId = DialogObject.getPeerDialogId(tLRPC$Peer);
            }
            if (dialogId >= 0) {
                str2 = UserObject.getUserName(MessagesController.getInstance(i).getUser(Long.valueOf(dialogId)));
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-dialogId));
                str2 = chat == null ? "" : chat.title;
            }
            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia2 = (TLRPC$TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            int i5 = 0;
            int i6 = 0;
            for (int i7 = 0; i7 < tLRPC$TL_messageMediaPaidMedia2.extended_media.size(); i7++) {
                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia2 = tLRPC$TL_messageMediaPaidMedia2.extended_media.get(i7);
                if (tLRPC$MessageExtendedMedia2 instanceof TLRPC$TL_messageExtendedMediaPreview) {
                    if ((((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia2).flags & 4) != 0) {
                        z = true;
                    }
                    z = false;
                } else {
                    if (tLRPC$MessageExtendedMedia2 instanceof TLRPC$TL_messageExtendedMedia) {
                        z = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia2).media instanceof TLRPC$TL_messageMediaDocument;
                    }
                    z = false;
                }
                if (z) {
                    i5++;
                } else {
                    i6++;
                }
            }
            if (i5 == 0) {
                int i8 = (int) j2;
                Object[] objArr = new Object[2];
                if (i6 == i3) {
                    formatPluralString3 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto);
                    c2 = 0;
                } else {
                    c2 = 0;
                    formatPluralString3 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i6, new Object[0]);
                }
                objArr[c2] = formatPluralString3;
                objArr[i3] = str2;
                formatPluralString = LocaleController.formatPluralString("StarsConfirmPurchaseMediaOne2", i8, objArr);
            } else if (i6 == 0) {
                int i9 = (int) j2;
                Object[] objArr2 = new Object[2];
                if (i5 == i3) {
                    formatPluralString2 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo);
                    c = 0;
                } else {
                    c = 0;
                    formatPluralString2 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i5, new Object[0]);
                }
                objArr2[c] = formatPluralString2;
                objArr2[i3] = str2;
                formatPluralString = LocaleController.formatPluralString("StarsConfirmPurchaseMediaOne2", i9, objArr2);
            } else {
                int i10 = (int) j2;
                Object[] objArr3 = new Object[3];
                objArr3[0] = i6 == i3 ? LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto) : LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i6, new Object[0]);
                objArr3[i3] = i5 == i3 ? LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo) : LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i5, new Object[0]);
                objArr3[2] = str2;
                formatPluralString = LocaleController.formatPluralString("StarsConfirmPurchaseMediaTwo2", i10, objArr3);
            }
            textView2.setText(AndroidUtilities.replaceTags(formatPluralString));
        } else {
            Object[] objArr4 = new Object[2];
            objArr4[0] = str;
            objArr4[i3] = UserObject.getUserName(user);
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("StarsConfirmPurchaseText", (int) j2, objArr4)));
        }
        textView2.setMaxWidth(HintView2.cutInFancyHalf(textView2.getText(), textView2.getPaint()));
        textView2.setGravity(17);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 1, 0, 6, 0, 24));
        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(replaceStars(AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("StarsConfirmPurchaseButton", (int) j2))), false);
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createFrame(-1, 48.0f));
        BottomSheet.Builder builder3 = builder;
        builder3.setCustomView(linearLayout);
        final BottomSheet create = builder3.create();
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$6(Utilities.Callback.this, create, buttonWithCounterView, view);
            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$7(runnable, dialogInterface);
            }
        });
        create.fixNavigationBar();
        create.show();
        return create;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$3(StarsBalanceView starsBalanceView, View view) {
        BaseFragment lastFragment;
        if (starsBalanceView.lastBalance > 0 && (lastFragment = LaunchActivity.getLastFragment()) != null) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new StarsIntroActivity(), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$6(Utilities.Callback callback, final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, View view) {
        if (callback != null) {
            bottomSheet.setCanDismissWithSwipe(false);
            buttonWithCounterView.setLoading(true);
            callback.run(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda25
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    StarsIntroActivity.lambda$openConfirmPurchaseSheet$5(BottomSheet.this, buttonWithCounterView, (Boolean) obj);
                }
            });
            return;
        }
        bottomSheet.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$5(final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, Boolean bool) {
        if (bool.booleanValue()) {
            bottomSheet.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$openConfirmPurchaseSheet$4(BottomSheet.this, buttonWithCounterView);
                }
            }, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$4(BottomSheet bottomSheet, ButtonWithCounterView buttonWithCounterView) {
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$7(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsNeededSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private final HeaderView headerView;
        private final long starsNeeded;
        private Runnable whenPurchased;

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            Runnable runnable;
            if (i == NotificationCenter.starOptionsLoaded || i == NotificationCenter.starBalanceUpdated) {
                UniversalAdapter universalAdapter = this.adapter;
                if (universalAdapter != null) {
                    universalAdapter.update(true);
                }
                long balance = StarsController.getInstance(this.currentAccount).getBalance();
                this.headerView.titleView.setText(LocaleController.formatPluralStringComma("StarsNeededTitle", (int) (this.starsNeeded - balance)));
                ActionBar actionBar = this.actionBar;
                if (actionBar != null) {
                    actionBar.setTitle(getTitle());
                }
                if (balance < this.starsNeeded || (runnable = this.whenPurchased) == null) {
                    return;
                }
                runnable.run();
                this.whenPurchased = null;
                dismiss();
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
        public void show() {
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) lastFragment;
                if (chatActivity.isKeyboardVisible() && chatActivity.getChatActivityEnterView() != null) {
                    chatActivity.getChatActivityEnterView().closeKeyboard();
                }
            }
            super.show();
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        }

        public StarsNeededSheet(Context context, Theme.ResourcesProvider resourcesProvider, long j, String str, Runnable runnable) {
            super(context, null, false, false, false, resourcesProvider);
            this.topPadding = 0.2f;
            this.whenPurchased = runnable;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
            fixNavigationBar();
            RecyclerListView recyclerListView = this.recyclerListView;
            int i = this.backgroundPaddingLeft;
            recyclerListView.setPadding(i, 0, i, 0);
            this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i2) {
                    StarsIntroActivity.StarsNeededSheet.this.lambda$new$0(view, i2);
                }
            });
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setSupportsChangeAnimations(false);
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            defaultItemAnimator.setDurations(350L);
            this.recyclerListView.setItemAnimator(defaultItemAnimator);
            setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray, resourcesProvider));
            this.starsNeeded = j;
            HeaderView headerView = new HeaderView(context, this.currentAccount, resourcesProvider);
            this.headerView = headerView;
            headerView.titleView.setText(LocaleController.formatPluralString("StarsNeededTitle", (int) (j - StarsController.getInstance(this.currentAccount).getBalance()), new Object[0]));
            headerView.subtitleView.setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StarsNeededText, str)));
            TextView textView = headerView.subtitleView;
            textView.setMaxWidth(HintView2.cutInFancyHalf(textView.getText(), headerView.subtitleView.getPaint()));
            this.actionBar.setTitle(getTitle());
            FrameLayout frameLayout = new FrameLayout(context);
            this.footerView = frameLayout;
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            frameLayout.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            linksTextView.setTextSize(1, 12.0f);
            linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
            linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
            linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.StarsNeededSheet.this.lambda$new$1();
                }
            }));
            linksTextView.setGravity(17);
            linksTextView.setMaxWidth(HintView2.cutInFancyHalf(linksTextView.getText(), linksTextView.getPaint()));
            frameLayout.addView(linksTextView, LayoutHelper.createFrame(-2, -1, 17));
            frameLayout.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground, resourcesProvider));
            FireworksOverlay fireworksOverlay = new FireworksOverlay(getContext());
            this.fireworksOverlay = fireworksOverlay;
            this.containerView.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
            UniversalAdapter universalAdapter = this.adapter;
            if (universalAdapter != null) {
                universalAdapter.update(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view, int i) {
            UItem item;
            UniversalAdapter universalAdapter = this.adapter;
            if (universalAdapter == null || (item = universalAdapter.getItem(i - 1)) == null) {
                return;
            }
            onItemClick(item, this.adapter);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            Browser.openUrl(getContext(), LocaleController.getString(R.string.StarsTOSLink));
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected CharSequence getTitle() {
            HeaderView headerView = this.headerView;
            if (headerView == null) {
                return null;
            }
            return headerView.titleView.getText();
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
            UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.StarsNeededSheet.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
                }
            }, this.resourcesProvider);
            this.adapter = universalAdapter;
            return universalAdapter;
        }

        public void fillItems(ArrayList<UItem> arrayList, UniversalAdapter universalAdapter) {
            arrayList.add(UItem.asCustom(this.headerView));
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
            ArrayList<TLRPC$TL_starsTopupOption> options = StarsController.getInstance(this.currentAccount).getOptions();
            if (options != null && !options.isEmpty()) {
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                int i4 = 1;
                boolean z = false;
                for (int i5 = 0; i5 < options.size(); i5++) {
                    TLRPC$TL_starsTopupOption tLRPC$TL_starsTopupOption = options.get(i5);
                    if (tLRPC$TL_starsTopupOption.stars >= this.starsNeeded) {
                        if (tLRPC$TL_starsTopupOption.collapsed && !this.expanded && z) {
                            i3++;
                        } else {
                            arrayList.add(StarTierView.Factory.asStarTier(i5, i4, tLRPC$TL_starsTopupOption));
                            i2++;
                            i4++;
                            z = true;
                        }
                    }
                }
                if (i2 > 0) {
                    boolean z2 = this.expanded;
                    if (!z2 && i3 > 0) {
                        arrayList.add(ExpandView.Factory.asExpand(-1, LocaleController.getString(z2 ? R.string.NotifyLessOptions : R.string.NotifyMoreOptions), true ^ this.expanded).accent());
                    }
                } else {
                    while (i < options.size()) {
                        arrayList.add(StarTierView.Factory.asStarTier(i, i4, options.get(i)));
                        i++;
                        i4++;
                    }
                }
            } else {
                arrayList.add(UItem.asFlicker(31));
                arrayList.add(UItem.asFlicker(31));
                arrayList.add(UItem.asFlicker(31));
            }
            arrayList.add(UItem.asCustom(this.footerView));
        }

        public void onItemClick(final UItem uItem, UniversalAdapter universalAdapter) {
            if (uItem.id == -1) {
                this.expanded = !this.expanded;
                universalAdapter.update(true);
            } else if (uItem.instanceOf(StarTierView.Factory.class) && (uItem.object instanceof TLRPC$TL_starsTopupOption)) {
                Activity findActivity = AndroidUtilities.findActivity(getContext());
                if (findActivity == null) {
                    findActivity = LaunchActivity.instance;
                }
                if (findActivity == null) {
                    return;
                }
                StarsController.getInstance(this.currentAccount).buy(findActivity, (TLRPC$TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        StarsIntroActivity.StarsNeededSheet.this.lambda$onItemClick$2(uItem, (Boolean) obj, (String) obj2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(UItem uItem, Boolean bool, String str) {
            if (getContext() == null) {
                return;
            }
            if (bool.booleanValue()) {
                BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate(), LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
                this.fireworksOverlay.start(true);
                StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
            } else if (str != null) {
                BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
        public void dismiss() {
            super.dismiss();
            HeaderView headerView = this.headerView;
            if (headerView != null) {
                headerView.iconView.setPaused(true);
            }
        }

        /* loaded from: classes3.dex */
        public static class HeaderView extends LinearLayout {
            public final StarsBalanceView balanceView;
            public final GLIconTextureView iconView;
            public final StarParticlesView particlesView;
            public final TextView subtitleView;
            public final TextView titleView;
            private final FrameLayout topView;

            public HeaderView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
                super(context);
                setOrientation(1);
                FrameLayout frameLayout = new FrameLayout(context);
                this.topView = frameLayout;
                frameLayout.setClipChildren(false);
                frameLayout.setClipToPadding(false);
                StarParticlesView makeParticlesView = StarsIntroActivity.makeParticlesView(context, 70, 0);
                this.particlesView = makeParticlesView;
                frameLayout.addView(makeParticlesView, LayoutHelper.createFrame(-1, -1.0f));
                GLIconTextureView gLIconTextureView = new GLIconTextureView(context, 1, 2);
                this.iconView = gLIconTextureView;
                GLIconRenderer gLIconRenderer = gLIconTextureView.mRenderer;
                gLIconRenderer.colorKey1 = Theme.key_starsGradient1;
                gLIconRenderer.colorKey2 = Theme.key_starsGradient2;
                gLIconRenderer.updateColors();
                gLIconTextureView.setStarParticlesView(makeParticlesView);
                frameLayout.addView(gLIconTextureView, LayoutHelper.createFrame(170, 170.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
                gLIconTextureView.setPaused(false);
                StarsBalanceView starsBalanceView = new StarsBalanceView(context, i);
                this.balanceView = starsBalanceView;
                ScaleStateListAnimator.apply(starsBalanceView);
                starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$HeaderView$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.StarsNeededSheet.HeaderView.this.lambda$new$0(view);
                    }
                });
                frameLayout.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
                addView(frameLayout, LayoutHelper.createFrame(-1, 150.0f));
                TextView textView = new TextView(context);
                this.titleView = textView;
                textView.setTextSize(1, 20.0f);
                textView.setTypeface(AndroidUtilities.bold());
                int i2 = Theme.key_dialogTextBlack;
                textView.setTextColor(Theme.getColor(i2, resourcesProvider));
                textView.setGravity(17);
                addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 2, 0, 0));
                TextView textView2 = new TextView(context);
                this.subtitleView = textView2;
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(Theme.getColor(i2, resourcesProvider));
                textView2.setGravity(17);
                addView(textView2, LayoutHelper.createLinear(-2, -2, 1, 0, 9, 0, 18));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$new$0(View view) {
                BaseFragment lastFragment;
                if (this.balanceView.lastBalance > 0 && (lastFragment = LaunchActivity.getLastFragment()) != null) {
                    BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
                    bottomSheetParams.transitionFromLeft = true;
                    bottomSheetParams.allowNestedScroll = false;
                    lastFragment.showAsSheet(new StarsIntroActivity(), bottomSheetParams);
                }
            }
        }
    }

    public static SpannableStringBuilder replaceStars(CharSequence charSequence) {
        return replaceStars(charSequence, 1.13f);
    }

    public static SpannableStringBuilder replaceStars(CharSequence charSequence, float f) {
        SpannableStringBuilder spannableStringBuilder;
        if (charSequence == null) {
            return null;
        }
        if (!(charSequence instanceof SpannableStringBuilder)) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        } else {
            spannableStringBuilder = (SpannableStringBuilder) charSequence;
        }
        SpannableString spannableString = new SpannableString("⭐ ");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_premium_liststar);
        coloredImageSpan.setScale(f, f);
        spannableString.setSpan(coloredImageSpan, 0, spannableString.length() - 1, 33);
        AndroidUtilities.replaceMultipleCharSequence("⭐️", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐ ", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐", spannableStringBuilder, spannableString);
        AndroidUtilities.replaceMultipleCharSequence("XTR ", spannableStringBuilder, "XTR");
        AndroidUtilities.replaceMultipleCharSequence("XTR", spannableStringBuilder, spannableString);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder replaceStars(CharSequence charSequence, ColoredImageSpan[] coloredImageSpanArr) {
        SpannableStringBuilder spannableStringBuilder;
        ColoredImageSpan coloredImageSpan;
        if (charSequence == null) {
            return null;
        }
        if (!(charSequence instanceof SpannableStringBuilder)) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        } else {
            spannableStringBuilder = (SpannableStringBuilder) charSequence;
        }
        if (coloredImageSpanArr != null && coloredImageSpanArr[0] != null) {
            coloredImageSpan = coloredImageSpanArr[0];
        } else {
            coloredImageSpan = new ColoredImageSpan(R.drawable.msg_premium_liststar);
            coloredImageSpan.setScale(1.13f, 1.13f);
        }
        if (coloredImageSpanArr != null) {
            coloredImageSpanArr[0] = coloredImageSpan;
        }
        SpannableString spannableString = new SpannableString("⭐ ");
        spannableString.setSpan(coloredImageSpan, 0, spannableString.length() - 1, 33);
        AndroidUtilities.replaceMultipleCharSequence("⭐️", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐ ", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐", spannableStringBuilder, spannableString);
        AndroidUtilities.replaceMultipleCharSequence("XTR ", spannableStringBuilder, "XTR");
        AndroidUtilities.replaceMultipleCharSequence("XTR", spannableStringBuilder, spannableString);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder replaceStarsWithPlain(CharSequence charSequence, float f) {
        SpannableStringBuilder spannableStringBuilder;
        if (charSequence == null) {
            return null;
        }
        if (!(charSequence instanceof SpannableStringBuilder)) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        } else {
            spannableStringBuilder = (SpannableStringBuilder) charSequence;
        }
        SpannableString spannableString = new SpannableString("⭐ ");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.star_small_inner);
        coloredImageSpan.recolorDrawable = false;
        coloredImageSpan.setScale(f, f);
        spannableString.setSpan(coloredImageSpan, 0, spannableString.length() - 1, 33);
        AndroidUtilities.replaceMultipleCharSequence("⭐️", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐ ", spannableStringBuilder, "⭐");
        AndroidUtilities.replaceMultipleCharSequence("⭐", spannableStringBuilder, spannableString);
        AndroidUtilities.replaceMultipleCharSequence("XTR ", spannableStringBuilder, "XTR");
        AndroidUtilities.replaceMultipleCharSequence("XTR", spannableStringBuilder, spannableString);
        return spannableStringBuilder;
    }

    public static CharSequence getTransactionTitle(boolean z, TLRPC$StarsTransaction tLRPC$StarsTransaction) {
        if (!tLRPC$StarsTransaction.extended_media.isEmpty()) {
            return LocaleController.getString(R.string.StarMediaPurchase);
        }
        String str = tLRPC$StarsTransaction.title;
        if (str != null) {
            return str;
        }
        long peerDialogId = DialogObject.getPeerDialogId(tLRPC$StarsTransaction.peer.peer);
        if (peerDialogId != 0) {
            if (peerDialogId >= 0) {
                return UserObject.getUserName(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(peerDialogId)));
            }
            TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-peerDialogId));
            return chat == null ? "" : chat.title;
        }
        TLRPC$StarsTransactionPeer tLRPC$StarsTransactionPeer = tLRPC$StarsTransaction.peer;
        if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerFragment) {
            return LocaleController.getString(z ? R.string.StarsTransactionWithdrawFragment : R.string.StarsTransactionFragment);
        } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerPremiumBot) {
            return LocaleController.getString(R.string.StarsTransactionBot);
        } else {
            return LocaleController.getString(R.string.StarsTransactionUnsupported);
        }
    }

    public static BottomSheet showTransactionSheet(Context context, boolean z, int i, TLRPC$TL_payments_paymentReceiptStars tLRPC$TL_payments_paymentReceiptStars, Theme.ResourcesProvider resourcesProvider) {
        TLRPC$StarsTransaction tLRPC$StarsTransaction = new TLRPC$StarsTransaction();
        tLRPC$StarsTransaction.title = tLRPC$TL_payments_paymentReceiptStars.title;
        tLRPC$StarsTransaction.description = tLRPC$TL_payments_paymentReceiptStars.description;
        tLRPC$StarsTransaction.photo = tLRPC$TL_payments_paymentReceiptStars.photo;
        TLRPC$TL_starsTransactionPeer tLRPC$TL_starsTransactionPeer = new TLRPC$TL_starsTransactionPeer();
        tLRPC$StarsTransaction.peer = tLRPC$TL_starsTransactionPeer;
        tLRPC$TL_starsTransactionPeer.peer = MessagesController.getInstance(i).getPeer(tLRPC$TL_payments_paymentReceiptStars.bot_id);
        tLRPC$StarsTransaction.date = tLRPC$TL_payments_paymentReceiptStars.date;
        tLRPC$StarsTransaction.stars = tLRPC$TL_payments_paymentReceiptStars.total_amount;
        tLRPC$StarsTransaction.id = tLRPC$TL_payments_paymentReceiptStars.transaction_id;
        return showTransactionSheet(context, z, 0L, i, tLRPC$StarsTransaction, resourcesProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x04bb  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x04f1 A[LOOP:0: B:110:0x0466->B:125:0x04f1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0683  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0715  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0720  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0746  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x074f  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04fe A[EDGE_INSN: B:152:0x04fe->B:127:0x04fe ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v24, types: [android.text.SpannableStringBuilder, java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r15v8 */
    /* JADX WARN: Type inference failed for: r1v14, types: [android.widget.TextView, android.view.View] */
    /* JADX WARN: Type inference failed for: r1v4, types: [android.widget.TextView, android.view.View] */
    /* JADX WARN: Type inference failed for: r1v5, types: [android.widget.TextView, android.view.View] */
    /* JADX WARN: Type inference failed for: r1v7, types: [android.view.View, org.telegram.ui.Components.TableView] */
    /* JADX WARN: Type inference failed for: r2v80, types: [android.widget.TextView, android.view.View, org.telegram.ui.Components.LinkSpanDrawable$LinksTextView] */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v30 */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v6, types: [android.widget.LinearLayout] */
    /* JADX WARN: Type inference failed for: r4v51, types: [android.widget.TextView, android.view.View, org.telegram.ui.Components.LinkSpanDrawable$LinksTextView] */
    /* JADX WARN: Type inference failed for: r6v24, types: [android.widget.LinearLayout, android.view.View] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showTransactionSheet(final Context context, final boolean z, final long j, final int i, final TLRPC$StarsTransaction tLRPC$StarsTransaction, final Theme.ResourcesProvider resourcesProvider) {
        BottomSheet[] bottomSheetArr;
        BottomSheet.Builder builder;
        ?? r15;
        LinearLayout linearLayout;
        String str;
        String str2;
        final BottomSheet[] bottomSheetArr2;
        Object obj;
        final BottomSheet[] bottomSheetArr3;
        char c;
        Object obj2;
        ImageLocation imageLocation;
        ImageLocation forDocument;
        int i2;
        Object obj3;
        boolean z2;
        String str3;
        ImageLocation imageLocation2;
        ImageLocation forDocument2;
        int i3 = i;
        if (tLRPC$StarsTransaction == null || context == null) {
            return null;
        }
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        BottomSheet[] bottomSheetArr4 = new BottomSheet[1];
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        final BackupImageView backupImageView = new BackupImageView(context);
        if (!tLRPC$StarsTransaction.extended_media.isEmpty()) {
            backupImageView.setRoundRadius(AndroidUtilities.dp(30.0f));
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StarsTransaction.extended_media.get(0);
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                forDocument2 = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.photo.sizes, AndroidUtilities.dp(100.0f), true), tLRPC$MessageMedia.photo);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                forDocument2 = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$MessageMedia.document.thumbs, AndroidUtilities.dp(100.0f), true), tLRPC$MessageMedia.document);
            } else {
                imageLocation2 = null;
                backupImageView.setImage(imageLocation2, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                linearLayout2.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                builder = builder2;
                bottomSheetArr = bottomSheetArr4;
                r15 = 1;
                backupImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda7
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showTransactionSheet$8(z, j, tLRPC$StarsTransaction, i, resourcesProvider, backupImageView, linearLayout2, view);
                    }
                });
                linearLayout = linearLayout2;
            }
            imageLocation2 = forDocument2;
            backupImageView.setImage(imageLocation2, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
            linearLayout2.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
            builder = builder2;
            bottomSheetArr = bottomSheetArr4;
            r15 = 1;
            backupImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showTransactionSheet$8(z, j, tLRPC$StarsTransaction, i, resourcesProvider, backupImageView, linearLayout2, view);
                }
            });
            linearLayout = linearLayout2;
        } else {
            bottomSheetArr = bottomSheetArr4;
            builder = builder2;
            r15 = 1;
            r15 = 1;
            TLRPC$StarsTransactionPeer tLRPC$StarsTransactionPeer = tLRPC$StarsTransaction.peer;
            if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeer) {
                if (tLRPC$StarsTransaction.photo != null) {
                    backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
                    backupImageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$StarsTransaction.photo)), "100_100", (Drawable) null, 0, (Object) null);
                } else {
                    backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
                    long peerDialogId = DialogObject.getPeerDialogId(tLRPC$StarsTransaction.peer.peer);
                    AvatarDrawable avatarDrawable = new AvatarDrawable();
                    if (peerDialogId >= 0) {
                        TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
                        avatarDrawable.setInfo(user);
                        backupImageView.setForUserOrChat(user, avatarDrawable);
                    } else {
                        TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(peerDialogId));
                        avatarDrawable.setInfo(chat);
                        backupImageView.setForUserOrChat(chat, avatarDrawable);
                    }
                }
                LinearLayout linearLayout3 = linearLayout2;
                linearLayout3.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                linearLayout = linearLayout3;
            } else {
                linearLayout = linearLayout2;
                if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerAppStore) {
                    str = "ios";
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerPlayMarket) {
                    str = "android";
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerPremiumBot) {
                    str = "premiumbot";
                } else if (tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerFragment) {
                    str = "fragment";
                } else {
                    str = tLRPC$StarsTransactionPeer instanceof TLRPC$TL_starsTransactionPeerAds ? "ads" : "?";
                }
                CombinedDrawable combinedDrawable = (CombinedDrawable) SessionCell.createDrawable(100, str);
                combinedDrawable.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                backupImageView.setImageDrawable(combinedDrawable);
            }
        }
        ?? textView = new TextView(context);
        int i4 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i4, resourcesProvider));
        textView.setTextSize(r15, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(getTransactionTitle(z, tLRPC$StarsTransaction));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        ?? textView2 = new TextView(context);
        textView2.setTextColor(Theme.getColor(tLRPC$StarsTransaction.stars >= 0 ? Theme.key_color_green : Theme.key_color_red, resourcesProvider));
        textView2.setTextSize(r15, 18.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setGravity(17);
        StringBuilder sb = new StringBuilder();
        sb.append(tLRPC$StarsTransaction.stars >= 0 ? "+" : "-");
        sb.append(LocaleController.formatNumber((int) Math.abs(tLRPC$StarsTransaction.stars), ' '));
        sb.append("\u2009⭐️");
        textView2.setText(replaceStarsWithPlain(sb.toString(), 0.8f));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView2.getText());
        if (tLRPC$StarsTransaction.refund) {
            appendStatus(spannableStringBuilder, textView2, LocaleController.getString(R.string.StarsRefunded));
        } else if (tLRPC$StarsTransaction.failed) {
            textView2.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
            appendStatus(spannableStringBuilder, textView2, LocaleController.getString(R.string.StarsFailed));
        } else if (tLRPC$StarsTransaction.pending) {
            textView2.setTextColor(Theme.getColor(Theme.key_color_yellow, resourcesProvider));
            appendStatus(spannableStringBuilder, textView2, LocaleController.getString(R.string.StarsPending));
        }
        textView2.setText(spannableStringBuilder);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        if (tLRPC$StarsTransaction.description != null && tLRPC$StarsTransaction.extended_media.isEmpty()) {
            ?? textView3 = new TextView(context);
            textView3.setTextColor(Theme.getColor(i4, resourcesProvider));
            textView3.setTextSize(r15, 16.0f);
            textView3.setGravity(17);
            textView3.setText(tLRPC$StarsTransaction.description);
            linearLayout.addView(textView3, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        }
        ?? tableView = new TableView(context, resourcesProvider);
        TLRPC$StarsTransactionPeer tLRPC$StarsTransactionPeer2 = tLRPC$StarsTransaction.peer;
        if (tLRPC$StarsTransactionPeer2 instanceof TLRPC$TL_starsTransactionPeer) {
            str2 = "";
            final long peerDialogId2 = DialogObject.getPeerDialogId(tLRPC$StarsTransactionPeer2.peer);
            ?? linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
            linksTextView.setEllipsize(TextUtils.TruncateAt.END);
            int i5 = Theme.key_chat_messageLinkIn;
            linksTextView.setTextColor(Theme.getColor(i5, resourcesProvider));
            linksTextView.setLinkTextColor(Theme.getColor(i5, resourcesProvider));
            linksTextView.setTextSize(r15, 14.0f);
            linksTextView.setDisablePaddingsOffsetY(r15);
            AvatarSpan avatarSpan = new AvatarSpan(linksTextView, i3, 24.0f);
            if (peerDialogId2 >= 0) {
                TLRPC$User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId2));
                boolean z3 = user2 == null;
                str3 = UserObject.getUserName(user2);
                avatarSpan.setUser(user2);
                z2 = z3;
            } else {
                TLRPC$Chat chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId2));
                z2 = chat2 == null;
                str3 = chat2 == null ? str2 : chat2.title;
                avatarSpan.setChat(chat2);
            }
            ?? spannableStringBuilder2 = new SpannableStringBuilder("x  " + ((Object) str3));
            spannableStringBuilder2.setSpan(avatarSpan, 0, r15, 33);
            bottomSheetArr2 = bottomSheetArr;
            spannableStringBuilder2.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.6
                @Override // android.text.style.ClickableSpan
                public void onClick(View view) {
                    bottomSheetArr2[0].dismiss();
                    BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                    if (safeLastFragment != null) {
                        safeLastFragment.presentFragment(ChatActivity.of(peerDialogId2));
                    }
                }

                @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setUnderlineText(false);
                }
            }, 3, spannableStringBuilder2.length(), 33);
            linksTextView.setText(spannableStringBuilder2);
            if (!z2) {
                tableView.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionRecipient), linksTextView);
            }
        } else {
            str2 = "";
            bottomSheetArr2 = bottomSheetArr;
            if (tLRPC$StarsTransactionPeer2 instanceof TLRPC$TL_starsTransactionPeerFragment) {
                tableView.addRow(LocaleController.getString(R.string.StarsTransactionSource), LocaleController.getString(R.string.Fragment));
            } else if (tLRPC$StarsTransactionPeer2 instanceof TLRPC$TL_starsTransactionPeerAppStore) {
                tableView.addRow(LocaleController.getString(R.string.StarsTransactionSource), LocaleController.getString(R.string.AppStore));
            } else if (tLRPC$StarsTransactionPeer2 instanceof TLRPC$TL_starsTransactionPeerPlayMarket) {
                tableView.addRow(LocaleController.getString(R.string.StarsTransactionSource), LocaleController.getString(R.string.PlayMarket));
            } else if (tLRPC$StarsTransactionPeer2 instanceof TLRPC$TL_starsTransactionPeerPremiumBot) {
                tableView.addRow(LocaleController.getString(R.string.StarsTransactionSource), LocaleController.getString(R.string.StarsTransactionBot));
            }
        }
        TLRPC$StarsTransactionPeer tLRPC$StarsTransactionPeer3 = tLRPC$StarsTransaction.peer;
        if ((tLRPC$StarsTransactionPeer3 instanceof TLRPC$TL_starsTransactionPeer) && (tLRPC$StarsTransaction.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
            final long peerDialogId3 = DialogObject.getPeerDialogId(tLRPC$StarsTransactionPeer3.peer);
            if (z) {
                peerDialogId3 = j;
            }
            TLRPC$Chat chat3 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId3));
            if (chat3 != null) {
                ?? linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                linksTextView2.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
                linksTextView2.setEllipsize(TextUtils.TruncateAt.END);
                int i6 = Theme.key_chat_messageLinkIn;
                linksTextView2.setTextColor(Theme.getColor(i6, resourcesProvider));
                linksTextView2.setLinkTextColor(Theme.getColor(i6, resourcesProvider));
                linksTextView2.setTextSize(r15, 14.0f);
                bottomSheetArr3 = bottomSheetArr2;
                linksTextView2.setDisablePaddingsOffsetY(r15);
                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(str2);
                if (!tLRPC$StarsTransaction.extended_media.isEmpty()) {
                    Iterator<TLRPC$MessageMedia> it = tLRPC$StarsTransaction.extended_media.iterator();
                    int i7 = 0;
                    linearLayout = linearLayout;
                    while (it.hasNext()) {
                        TLRPC$MessageMedia next = it.next();
                        Iterator<TLRPC$MessageMedia> it2 = it;
                        ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(linksTextView2, i3, 24.0f);
                        if (next instanceof TLRPC$TL_messageMediaPhoto) {
                            obj3 = linearLayout;
                            forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(next.photo.sizes, AndroidUtilities.dp(24.0f), true), next.photo);
                        } else {
                            Object obj4 = linearLayout;
                            if (next instanceof TLRPC$TL_messageMediaDocument) {
                                forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(next.document.thumbs, AndroidUtilities.dp(24.0f), true), next.document);
                                obj3 = obj4;
                            } else {
                                imageLocation = null;
                                obj2 = obj4;
                                if (imageLocation != null) {
                                    imageReceiverSpan.setRoundRadius(6.0f);
                                    imageReceiverSpan.imageReceiver.setImage(imageLocation, "24_24", null, null, null, 0);
                                    SpannableString spannableString = new SpannableString("x");
                                    spannableString.setSpan(imageReceiverSpan, 0, spannableString.length(), 33);
                                    spannableStringBuilder3.append((CharSequence) spannableString);
                                    spannableStringBuilder3.append((CharSequence) " ");
                                    i7++;
                                }
                                i2 = i7;
                                if (i2 < 3) {
                                    break;
                                }
                                it = it2;
                                i3 = i;
                                i7 = i2;
                                linearLayout = obj2;
                            }
                        }
                        imageLocation = forDocument;
                        obj2 = obj3;
                        if (imageLocation != null) {
                        }
                        i2 = i7;
                        if (i2 < 3) {
                        }
                    }
                }
                obj2 = linearLayout;
                spannableStringBuilder3.append((CharSequence) " ");
                int length = spannableStringBuilder3.length();
                String publicUsername = ChatObject.getPublicUsername(chat3);
                if (TextUtils.isEmpty(publicUsername)) {
                    spannableStringBuilder3.append((CharSequence) chat3.title);
                } else {
                    spannableStringBuilder3.append((CharSequence) (MessagesController.getInstance(i).linkPrefix + "/" + publicUsername + "/" + tLRPC$StarsTransaction.msg_id));
                }
                final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$9(bottomSheetArr3, peerDialogId3, tLRPC$StarsTransaction);
                    }
                };
                spannableStringBuilder3.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.7
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        runnable.run();
                    }

                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        textPaint.setUnderlineText(false);
                    }
                }, length, spannableStringBuilder3.length(), 33);
                linksTextView2.setSingleLine(true);
                linksTextView2.setEllipsize(TextUtils.TruncateAt.END);
                linksTextView2.setText(spannableStringBuilder3);
                linksTextView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        runnable.run();
                    }
                });
                tableView.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionMedia), linksTextView2);
                obj = obj2;
                if (!TextUtils.isEmpty(tLRPC$StarsTransaction.id)) {
                    FrameLayout frameLayout = new FrameLayout(context);
                    frameLayout.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(10.66f), AndroidUtilities.dp(9.33f));
                    TextView textView4 = new TextView(context);
                    textView4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MONO));
                    textView4.setTextSize(1, 13.0f);
                    textView4.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
                    textView4.setMaxLines(4);
                    textView4.setSingleLine(false);
                    textView4.setText(tLRPC$StarsTransaction.id);
                    frameLayout.addView(textView4, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 32.0f, 0.0f));
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.msg_copy);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    int i8 = Theme.key_windowBackgroundWhiteBlueIcon;
                    imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i8, resourcesProvider), PorterDuff.Mode.SRC_IN));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$11(TLRPC$StarsTransaction.this, bottomSheetArr3, resourcesProvider, view);
                        }
                    });
                    ScaleStateListAnimator.apply(imageView);
                    imageView.setBackground(Theme.createSelectorDrawable(Theme.multAlpha(Theme.getColor(i8, resourcesProvider), 0.1f), 7));
                    frameLayout.addView(imageView, LayoutHelper.createFrame(30, 30, 21));
                    tableView.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionID), frameLayout);
                }
                String string = LocaleController.getString(R.string.StarsTransactionDate);
                int i9 = R.string.formatDateAtTime;
                tableView.addRow(string, LocaleController.formatString(i9, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(tLRPC$StarsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(tLRPC$StarsTransaction.date * 1000))));
                ?? r6 = obj;
                r6.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                if ((tLRPC$StarsTransaction.flags & 32) != 0) {
                    tableView.addRow(LocaleController.getString(R.string.StarsTransactionTONDate), LocaleController.formatString(i9, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(tLRPC$StarsTransaction.transaction_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(tLRPC$StarsTransaction.transaction_date * 1000))));
                }
                LinkSpanDrawable.LinksTextView linksTextView3 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                linksTextView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
                linksTextView3.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                linksTextView3.setTextSize(1, 14.0f);
                linksTextView3.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$12(context);
                    }
                }));
                linksTextView3.setGravity(17);
                r6.addView(linksTextView3, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
                if ((tLRPC$StarsTransaction.flags & 32) == 0) {
                    c = 0;
                    buttonWithCounterView.setText(LocaleController.getString(R.string.StarsTransactionViewInBlockchainExplorer), false);
                } else {
                    c = 0;
                    buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
                }
                r6.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                BottomSheet.Builder builder3 = builder;
                builder3.setCustomView(r6);
                bottomSheetArr3[c] = builder3.create();
                if ((tLRPC$StarsTransaction.flags & 32) == 0) {
                    buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$13(context, tLRPC$StarsTransaction, view);
                        }
                    });
                } else {
                    buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$14(bottomSheetArr3, view);
                        }
                    });
                }
                bottomSheetArr3[0].fixNavigationBar();
                bottomSheetArr3[0].show();
                return bottomSheetArr3[0];
            }
        }
        obj = linearLayout;
        bottomSheetArr3 = bottomSheetArr2;
        if (!TextUtils.isEmpty(tLRPC$StarsTransaction.id)) {
        }
        String string2 = LocaleController.getString(R.string.StarsTransactionDate);
        int i92 = R.string.formatDateAtTime;
        tableView.addRow(string2, LocaleController.formatString(i92, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(tLRPC$StarsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(tLRPC$StarsTransaction.date * 1000))));
        ?? r62 = obj;
        r62.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
        if ((tLRPC$StarsTransaction.flags & 32) != 0) {
        }
        LinkSpanDrawable.LinksTextView linksTextView32 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView32.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
        linksTextView32.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linksTextView32.setTextSize(1, 14.0f);
        linksTextView32.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showTransactionSheet$12(context);
            }
        }));
        linksTextView32.setGravity(17);
        r62.addView(linksTextView32, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
        ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider);
        if ((tLRPC$StarsTransaction.flags & 32) == 0) {
        }
        r62.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
        BottomSheet.Builder builder32 = builder;
        builder32.setCustomView(r62);
        bottomSheetArr3[c] = builder32.create();
        if ((tLRPC$StarsTransaction.flags & 32) == 0) {
        }
        bottomSheetArr3[0].fixNavigationBar();
        bottomSheetArr3[0].show();
        return bottomSheetArr3[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$8(boolean z, long j, TLRPC$StarsTransaction tLRPC$StarsTransaction, int i, Theme.ResourcesProvider resourcesProvider, final BackupImageView backupImageView, final LinearLayout linearLayout, View view) {
        final long peerDialogId = z ? j : DialogObject.getPeerDialogId(tLRPC$StarsTransaction.peer.peer);
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < tLRPC$StarsTransaction.extended_media.size(); i2++) {
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.id = tLRPC$StarsTransaction.msg_id;
            tLRPC$TL_message.dialog_id = peerDialogId;
            TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
            tLRPC$TL_message.from_id = tLRPC$TL_peerChannel;
            long j2 = -peerDialogId;
            tLRPC$TL_peerChannel.channel_id = j2;
            TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel2;
            tLRPC$TL_peerChannel2.channel_id = j2;
            tLRPC$TL_message.date = tLRPC$StarsTransaction.date;
            tLRPC$TL_message.flags |= LiteMode.FLAG_CALLS_ANIMATIONS;
            tLRPC$TL_message.media = tLRPC$StarsTransaction.extended_media.get(i2);
            tLRPC$TL_message.noforwards = true;
            arrayList.add(new MessageObject(i, tLRPC$TL_message, false, false));
        }
        if (arrayList.isEmpty()) {
            return;
        }
        PhotoViewer.getInstance().setParentActivity(LaunchActivity.getLastFragment(), resourcesProvider);
        PhotoViewer.getInstance().openPhoto(arrayList, 0, peerDialogId, 0L, 0L, new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Stars.StarsIntroActivity.5
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean forceAllInGroup() {
                return true;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i3, boolean z2) {
                ImageReceiver imageReceiver = BackupImageView.this.getImageReceiver();
                int[] iArr = new int[2];
                BackupImageView.this.getLocationInWindow(iArr);
                PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                placeProviderObject.viewX = iArr[0];
                placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                placeProviderObject.parentView = linearLayout;
                placeProviderObject.animatingImageView = null;
                placeProviderObject.imageReceiver = imageReceiver;
                if (z2) {
                    placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                }
                placeProviderObject.radius = imageReceiver.getRoundRadius(true);
                placeProviderObject.dialogId = peerDialogId;
                placeProviderObject.clipTopAddition = 0;
                placeProviderObject.clipBottomAddition = 0;
                return placeProviderObject;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$9(BottomSheet[] bottomSheetArr, long j, TLRPC$StarsTransaction tLRPC$StarsTransaction) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            bundle.putInt("message_id", tLRPC$StarsTransaction.msg_id);
            safeLastFragment.presentFragment(new ChatActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$11(TLRPC$StarsTransaction tLRPC$StarsTransaction, BottomSheet[] bottomSheetArr, Theme.ResourcesProvider resourcesProvider, View view) {
        AndroidUtilities.addToClipboard(tLRPC$StarsTransaction.id);
        BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createSimpleBulletin(R.raw.copy, LocaleController.getString(R.string.StarsTransactionIDCopied)).show(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$12(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$13(Context context, TLRPC$StarsTransaction tLRPC$StarsTransaction, View view) {
        Browser.openUrl(context, tLRPC$StarsTransaction.transaction_url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$14(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    private static CharSequence appendStatus(SpannableStringBuilder spannableStringBuilder, TextView textView, String str) {
        spannableStringBuilder.append(" ");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ReplacementSpan(textView.getCurrentTextColor(), str) { // from class: org.telegram.ui.Stars.StarsIntroActivity.8
            private final Paint backgroundPaint;
            private final Text layout;
            final /* synthetic */ int val$color;
            final /* synthetic */ String val$string;

            {
                this.val$color = r3;
                this.val$string = str;
                Paint paint = new Paint(1);
                this.backgroundPaint = paint;
                paint.setColor(Theme.multAlpha(r3, 0.1f));
                this.layout = new Text(str, 13.0f, AndroidUtilities.bold());
            }

            @Override // android.text.style.ReplacementSpan
            public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
                return (int) (AndroidUtilities.dp(12.0f) + this.layout.getCurrentWidth());
            }

            @Override // android.text.style.ReplacementSpan
            public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
                RectF rectF = AndroidUtilities.rectTmp;
                int i6 = i3 + i5;
                rectF.set(f, (i6 - AndroidUtilities.dp(20.0f)) / 2.0f, AndroidUtilities.dp(12.0f) + f + this.layout.getCurrentWidth(), (AndroidUtilities.dp(20.0f) + i6) / 2.0f);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.backgroundPaint);
                this.layout.draw(canvas, f + AndroidUtilities.dp(6.0f), i6 / 2.0f, this.val$color, 1.0f);
            }
        }, 0, spannableString.length(), 33);
        spannableStringBuilder.append((CharSequence) spannableString);
        return spannableStringBuilder;
    }

    public static BottomSheet showMediaPriceSheet(final Context context, final long j, final boolean z, final Utilities.Callback2<Long, Runnable> callback2, Theme.ResourcesProvider resourcesProvider) {
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        TextView textView = new TextView(context);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setText(LocaleController.getString(R.string.PaidContentTitle));
        textView.setTextSize(1, 20.0f);
        int i = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(Theme.getColor(i, resourcesProvider));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 4.0f, 0.0f, 4.0f, 18.0f));
        final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        final OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context, resourcesProvider);
        outlineTextContainerView.setForceForceUseCenter(true);
        outlineTextContainerView.setText(LocaleController.getString(R.string.PaidContentPriceTitle));
        outlineTextContainerView.setLeftPadding(AndroidUtilities.dp(36.0f));
        editTextBoldCursor.setTextColor(Theme.getColor(i, resourcesProvider));
        editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
        editTextBoldCursor.setCursorWidth(1.5f);
        ButtonWithCounterView buttonWithCounterView = null;
        editTextBoldCursor.setBackground(null);
        editTextBoldCursor.setTextSize(1, 18.0f);
        editTextBoldCursor.setMaxLines(1);
        int dp = AndroidUtilities.dp(16.0f);
        editTextBoldCursor.setPadding(AndroidUtilities.dp(6.0f), dp, dp, dp);
        editTextBoldCursor.setInputType(2);
        editTextBoldCursor.setTypeface(Typeface.DEFAULT);
        editTextBoldCursor.setSelectAllOnFocus(true);
        editTextBoldCursor.setHighlightColor(Theme.getColor(Theme.key_chat_inTextSelectionHighlight, resourcesProvider));
        editTextBoldCursor.setHandlesColor(Theme.getColor(Theme.key_chat_TextSelectionCursor, resourcesProvider));
        editTextBoldCursor.setGravity(LocaleController.isRTL ? 5 : 3);
        editTextBoldCursor.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z2) {
                StarsIntroActivity.lambda$showMediaPriceSheet$15(OutlineTextContainerView.this, editTextBoldCursor, view, z2);
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.star_small_inner);
        linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 0.0f, 19, 14, 0, 0, 0));
        linearLayout2.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, -2, 1.0f, 119));
        outlineTextContainerView.attachEditText(editTextBoldCursor);
        outlineTextContainerView.addView(linearLayout2, LayoutHelper.createFrame(-1, -2, 48));
        linearLayout.addView(outlineTextContainerView, LayoutHelper.createLinear(-1, -2));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
        linksTextView.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.PaidContentInfo), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$16(context);
            }
        }), true));
        linksTextView.setTextSize(1, 12.0f);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 14.0f, 3.0f, 14.0f, 24.0f));
        final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView2.setText(LocaleController.getString(j > 0 ? R.string.PaidContentUpdateButton : R.string.PaidContentButton), false);
        linearLayout.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
        if (j > 0 && z) {
            buttonWithCounterView = new ButtonWithCounterView(context, false, resourcesProvider);
            buttonWithCounterView.setText(LocaleController.getString(R.string.PaidContentClearButton), false, false);
            linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 0.0f, 4.0f, 0.0f, 0.0f));
        }
        final ButtonWithCounterView buttonWithCounterView3 = buttonWithCounterView;
        builder.setCustomView(linearLayout);
        bottomSheetArr[0] = builder.create();
        editTextBoldCursor.setText(j <= 0 ? "" : Long.toString(j));
        editTextBoldCursor.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Stars.StarsIntroActivity.9
            private boolean ignore;
            private int shakeDp = 2;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            /* JADX WARN: Removed duplicated region for block: B:23:0x007b  */
            @Override // android.text.TextWatcher
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void afterTextChanged(Editable editable) {
                long j2;
                if (this.ignore) {
                    return;
                }
                try {
                    j2 = TextUtils.isEmpty(editable) ? 0L : Long.parseLong(editable.toString());
                    try {
                        if (j2 > MessagesController.getInstance(UserConfig.selectedAccount).starsPaidPostAmountMax) {
                            this.ignore = true;
                            EditTextBoldCursor editTextBoldCursor2 = EditTextBoldCursor.this;
                            j2 = MessagesController.getInstance(UserConfig.selectedAccount).starsPaidPostAmountMax;
                            editTextBoldCursor2.setText(Long.toString(j2));
                            EditTextBoldCursor editTextBoldCursor3 = EditTextBoldCursor.this;
                            editTextBoldCursor3.setSelection(editTextBoldCursor3.getText().length());
                            OutlineTextContainerView outlineTextContainerView2 = outlineTextContainerView;
                            int i2 = -this.shakeDp;
                            this.shakeDp = i2;
                            AndroidUtilities.shakeViewSpring(outlineTextContainerView2, i2);
                        }
                    } catch (Exception unused) {
                        this.ignore = true;
                        EditTextBoldCursor editTextBoldCursor4 = EditTextBoldCursor.this;
                        long j3 = j;
                        editTextBoldCursor4.setText(j3 <= 0 ? "" : Long.toString(j3));
                        EditTextBoldCursor editTextBoldCursor5 = EditTextBoldCursor.this;
                        editTextBoldCursor5.setSelection(editTextBoldCursor5.getText().length());
                        this.ignore = false;
                        if (!z) {
                        }
                        outlineTextContainerView.animateSelection(EditTextBoldCursor.this.isFocused(), true ^ TextUtils.isEmpty(EditTextBoldCursor.this.getText()));
                    }
                } catch (Exception unused2) {
                    j2 = 0;
                }
                this.ignore = false;
                if (!z) {
                    buttonWithCounterView2.setEnabled(j2 > 0);
                }
                outlineTextContainerView.animateSelection(EditTextBoldCursor.this.isFocused(), true ^ TextUtils.isEmpty(EditTextBoldCursor.this.getText()));
            }
        });
        final boolean[] zArr = {false};
        editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda12
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView2, int i2, KeyEvent keyEvent) {
                boolean lambda$showMediaPriceSheet$18;
                lambda$showMediaPriceSheet$18 = StarsIntroActivity.lambda$showMediaPriceSheet$18(zArr, callback2, buttonWithCounterView2, editTextBoldCursor, bottomSheetArr, textView2, i2, keyEvent);
                return lambda$showMediaPriceSheet$18;
            }
        });
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showMediaPriceSheet$20(zArr, callback2, editTextBoldCursor, buttonWithCounterView2, bottomSheetArr, view);
            }
        });
        if (buttonWithCounterView3 != null) {
            buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showMediaPriceSheet$22(zArr, callback2, buttonWithCounterView3, editTextBoldCursor, bottomSheetArr, view);
                }
            });
        }
        bottomSheetArr[0].fixNavigationBar();
        bottomSheetArr[0].setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
            }
        });
        bottomSheetArr[0].show();
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$25(bottomSheetArr, editTextBoldCursor);
            }
        }, lastFragment instanceof ChatActivity ? ((ChatActivity) lastFragment).needEnterText() : false ? 200L : 80L);
        return bottomSheetArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$15(OutlineTextContainerView outlineTextContainerView, EditTextBoldCursor editTextBoldCursor, View view, boolean z) {
        outlineTextContainerView.animateSelection(z, !TextUtils.isEmpty(editTextBoldCursor.getText()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$16(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.PaidContentInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showMediaPriceSheet$18(boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            if (zArr[0]) {
                return true;
            }
            if (callback2 != null) {
                zArr[0] = true;
                buttonWithCounterView.setLoading(true);
                callback2.run(Long.valueOf(Long.parseLong(editTextBoldCursor.getText().toString())), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showMediaPriceSheet$17(EditTextBoldCursor.this, bottomSheetArr);
                    }
                });
            } else {
                AndroidUtilities.hideKeyboard(editTextBoldCursor);
                bottomSheetArr[0].dismiss();
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$17(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$20(boolean[] zArr, Utilities.Callback2 callback2, final EditTextBoldCursor editTextBoldCursor, ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 != null) {
            String obj = editTextBoldCursor.getText().toString();
            zArr[0] = true;
            buttonWithCounterView.setLoading(true);
            callback2.run(Long.valueOf(TextUtils.isEmpty(obj) ? 0L : Long.parseLong(obj)), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showMediaPriceSheet$19(EditTextBoldCursor.this, bottomSheetArr);
                }
            });
            return;
        }
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$19(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$22(final boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 != null) {
            zArr[0] = true;
            buttonWithCounterView.setLoading(true);
            callback2.run(0L, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showMediaPriceSheet$21(zArr, editTextBoldCursor, bottomSheetArr);
                }
            });
            return;
        }
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$21(boolean[] zArr, EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        zArr[0] = false;
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$25(BottomSheet[] bottomSheetArr, final EditTextBoldCursor editTextBoldCursor) {
        bottomSheetArr[0].setFocusable(true);
        editTextBoldCursor.requestFocus();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
            }
        });
    }
}
