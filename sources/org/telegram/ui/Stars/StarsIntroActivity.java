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
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.BirthdayController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda7;
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
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.LoadingSpan;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.Premium.GLIcon.GLIconRenderer;
import org.telegram.ui.Components.Premium.GLIcon.GLIconTextureView;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.Premium.boosts.UserSelectorBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.StarAppsSheet;
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
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.HintView2;
/* loaded from: classes3.dex */
public class StarsIntroActivity extends GradientHeaderActivity implements NotificationCenter.NotificationCenterDelegate {
    private FrameLayout aboveTitleView;
    private UniversalAdapter adapter;
    private LinearLayout balanceLayout;
    private ButtonWithCounterView buyButton;
    private View emptyLayout;
    private FireworksOverlay fireworksOverlay;
    private ButtonWithCounterView giftButton;
    private boolean hadTransactions;
    private GLIconTextureView iconTextureView;
    private SpannableStringBuilder starBalanceIcon;
    private AnimatedTextView starBalanceTextView;
    private TextView starBalanceTitleView;
    private StarsTransactionsLayout transactionsLayout;
    private boolean expanded = false;
    private final int BUTTON_EXPAND = -1;
    private final int BUTTON_GIFT = -2;
    private final int BUTTON_SUBSCRIPTIONS_EXPAND = -3;

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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Paint lambda$configure$0(Integer num) {
            return this.paints[num.intValue() % this.paints.length];
        }

        @Override // org.telegram.ui.Components.Premium.StarParticlesView
        protected void configure() {
            StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(this.val$particlesCount);
            this.drawable = drawable;
            drawable.type = 105;
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
                if (i >= paintArr.length) {
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
                paintArr[i] = new Paint(1);
                this.paints[i].setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-371690, -14281, i / (this.paints.length - 1)), PorterDuff.Mode.SRC_IN));
                i++;
            }
        }

        @Override // org.telegram.ui.Components.Premium.StarParticlesView
        protected int getStarsRectWidth() {
            return getMeasuredWidth();
        }
    }

    /* loaded from: classes3.dex */
    public static class ExpandView extends FrameLayout {
        public final ImageView arrowView;
        private int lastId;
        private boolean needDivider;
        public final AnimatedTextView textView;

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory {
            static {
                UItem.UItemFactory.setup(new Factory());
            }

            public static UItem asExpand(int i, CharSequence charSequence, boolean z) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.id = i;
                ofFactory.text = charSequence;
                ofFactory.collapsed = z;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((ExpandView) view).set(uItem, z);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public ExpandView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                return new ExpandView(context, resourcesProvider);
            }
        }

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

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.lastId = ConnectionsManager.DEFAULT_DATACENTER_ID;
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

        public void set(String str, boolean z, boolean z2, boolean z3) {
            boolean z4 = this.lastId == -1;
            this.lastId = -1;
            this.textView.setText(str, z4);
            int color = Theme.getColor(Theme.key_windowBackgroundWhiteBlueText2);
            this.textView.setTextColor(color);
            this.arrowView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            if (z4) {
                this.arrowView.animate().rotation(z ? 0.0f : 180.0f).setDuration(340L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            } else {
                this.arrowView.setRotation(z ? 0.0f : 180.0f);
            }
            this.needDivider = z3;
            setWillNotDraw(!z3);
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
    }

    /* loaded from: classes3.dex */
    public static class GiftStarsSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private final HeaderView headerView;
        private final TLRPC.User user;
        private final Runnable whenPurchased;

        /* loaded from: classes3.dex */
        public static class HeaderView extends LinearLayout {
            public final BackupImageView avatarImageView;
            public final StarParticlesView particlesView;
            public final LinkSpanDrawable.LinksTextView subtitleView;
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
                BackupImageView backupImageView = new BackupImageView(context);
                this.avatarImageView = backupImageView;
                backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
                frameLayout.addView(backupImageView, LayoutHelper.createFrame(100, 100.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
                addView(frameLayout, LayoutHelper.createFrame(-1, 150.0f));
                TextView textView = new TextView(context);
                this.titleView = textView;
                textView.setTextSize(1, 20.0f);
                textView.setTypeface(AndroidUtilities.bold());
                int i2 = Theme.key_dialogTextBlack;
                textView.setTextColor(Theme.getColor(i2, resourcesProvider));
                textView.setGravity(17);
                addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 2, 0, 0));
                LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                this.subtitleView = linksTextView;
                linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                linksTextView.setTextSize(1, 14.0f);
                linksTextView.setTextColor(Theme.getColor(i2, resourcesProvider));
                linksTextView.setGravity(17);
                addView(linksTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 9, 0, 18));
            }
        }

        public GiftStarsSheet(Context context, Theme.ResourcesProvider resourcesProvider, TLRPC.User user, Runnable runnable) {
            super(context, null, false, false, false, resourcesProvider);
            this.BUTTON_EXPAND = -1;
            this.user = user;
            this.whenPurchased = runnable;
            this.topPadding = 0.2f;
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starGiftOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
            fixNavigationBar();
            RecyclerListView recyclerListView = this.recyclerListView;
            int i = this.backgroundPaddingLeft;
            recyclerListView.setPadding(i, 0, i, 0);
            this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i2) {
                    StarsIntroActivity.GiftStarsSheet.this.lambda$new$0(view, i2);
                }
            });
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setSupportsChangeAnimations(false);
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            defaultItemAnimator.setDurations(350L);
            this.recyclerListView.setItemAnimator(defaultItemAnimator);
            setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray, resourcesProvider));
            HeaderView headerView = new HeaderView(context, this.currentAccount, resourcesProvider);
            this.headerView = headerView;
            headerView.titleView.setText(LocaleController.getString(R.string.GiftStarsTitle));
            headerView.subtitleView.setText(TextUtils.concat(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.GiftStarsSubtitle, UserObject.getForcedFirstName(user))), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.GiftStarsSubtitleLinkName).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.GiftStarsSheet.this.lambda$new$1();
                }
            }), true)));
            LinkSpanDrawable.LinksTextView linksTextView = headerView.subtitleView;
            linksTextView.setMaxWidth(HintView2.cutInFancyHalf(linksTextView.getText(), headerView.subtitleView.getPaint()) + 1);
            this.actionBar.setTitle(getTitle());
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setInfo(user);
            headerView.avatarImageView.setForUserOrChat(user, avatarDrawable);
            FrameLayout frameLayout = new FrameLayout(context);
            this.footerView = frameLayout;
            LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            frameLayout.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            linksTextView2.setTextSize(1, 12.0f);
            linksTextView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
            linksTextView2.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
            linksTextView2.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.GiftStarsSheet.this.lambda$new$2();
                }
            }));
            linksTextView2.setGravity(17);
            linksTextView2.setMaxWidth(HintView2.cutInFancyHalf(linksTextView2.getText(), linksTextView2.getPaint()));
            frameLayout.addView(linksTextView2, LayoutHelper.createFrame(-2, -1, 17));
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
            BaseFragment baseFragment;
            StarAppsSheet starAppsSheet = new StarAppsSheet(getContext());
            if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(this.attachedFragment) && (baseFragment = this.attachedFragment) != null) {
                starAppsSheet.makeAttached(baseFragment);
            }
            starAppsSheet.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2() {
            Browser.openUrl(getContext(), LocaleController.getString(R.string.StarsTOSLink));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onItemClick$3(long j) {
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment != null) {
                safeLastFragment.presentFragment(ChatActivity.of(j));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$4(UItem uItem, final long j, Boolean bool, String str) {
            Runnable runnable;
            if (getContext() == null) {
                return;
            }
            if ((bool.booleanValue() || str != null) && (runnable = this.whenPurchased) != null) {
                runnable.run();
            }
            dismiss();
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            FireworksOverlay fireworksOverlay = LaunchActivity.instance.getFireworksOverlay();
            if (safeLastFragment == null) {
                return;
            }
            if (!bool.booleanValue()) {
                if (str != null) {
                    BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
                    return;
                }
                return;
            }
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_send, LocaleController.getString(R.string.StarsGiftSentPopup), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsGiftSentPopupInfo", (int) uItem.longValue, UserObject.getForcedFirstName(this.user))), LocaleController.getString(R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.GiftStarsSheet.lambda$onItemClick$3(j);
                }
            }).setDuration(5000).show(true);
            if (fireworksOverlay != null) {
                fireworksOverlay.start(true);
            }
            StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
            UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.GiftStarsSheet.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
                }
            }, this.resourcesProvider);
            this.adapter = universalAdapter;
            return universalAdapter;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            UniversalAdapter universalAdapter;
            if ((i == NotificationCenter.starGiftOptionsLoaded || i == NotificationCenter.starBalanceUpdated) && (universalAdapter = this.adapter) != null) {
                universalAdapter.update(true);
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
        public void dismiss() {
            super.dismiss();
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starGiftOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        }

        public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
            UItem asFlicker;
            arrayList.add(UItem.asCustom(this.headerView));
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
            ArrayList giftOptions = StarsController.getInstance(this.currentAccount).getGiftOptions();
            if (giftOptions != null && !giftOptions.isEmpty()) {
                int i = 0;
                int i2 = 1;
                for (int i3 = 0; i3 < giftOptions.size(); i3++) {
                    TL_stars.TL_starsGiftOption tL_starsGiftOption = (TL_stars.TL_starsGiftOption) giftOptions.get(i3);
                    if (this.expanded || !tL_starsGiftOption.extended) {
                        arrayList.add(StarTierView.Factory.asStarTier(i3, i2, tL_starsGiftOption));
                        i2++;
                    } else {
                        i++;
                    }
                }
                boolean z = this.expanded;
                if (!z && i > 0) {
                    asFlicker = ExpandView.Factory.asExpand(-1, LocaleController.getString(z ? R.string.NotifyLessOptions : R.string.NotifyMoreOptions), !this.expanded).accent();
                }
                arrayList.add(UItem.asCustom(this.footerView));
            }
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            asFlicker = UItem.asFlicker(31);
            arrayList.add(asFlicker);
            arrayList.add(UItem.asCustom(this.footerView));
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected CharSequence getTitle() {
            HeaderView headerView = this.headerView;
            if (headerView == null) {
                return null;
            }
            return headerView.titleView.getText();
        }

        public void onItemClick(final UItem uItem, UniversalAdapter universalAdapter) {
            if (uItem.id == -1) {
                this.expanded = !this.expanded;
                universalAdapter.update(true);
                this.recyclerListView.smoothScrollBy(0, AndroidUtilities.dp(200.0f), CubicBezierInterpolator.EASE_OUT);
            } else if (uItem.instanceOf(StarTierView.Factory.class) && (uItem.object instanceof TL_stars.TL_starsGiftOption)) {
                Activity findActivity = AndroidUtilities.findActivity(getContext());
                if (findActivity == null) {
                    findActivity = LaunchActivity.instance;
                }
                Activity activity = findActivity;
                if (activity == null) {
                    return;
                }
                final long j = this.user.id;
                StarsController.getInstance(this.currentAccount).buyGift(activity, (TL_stars.TL_starsGiftOption) uItem.object, j, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$GiftStarsSheet$$ExternalSyntheticLambda4
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        StarsIntroActivity.GiftStarsSheet.this.lambda$onItemClick$4(uItem, j, (Boolean) obj, (String) obj2);
                    }
                });
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class NestedFrameLayout extends GradientHeaderActivity.ContentView implements NestedScrollingParent3 {
        private NestedScrollingParentHelper nestedScrollingParentHelper;

        public NestedFrameLayout(Context context) {
            super(context);
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
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
            int i4;
            if (view == ((GradientHeaderActivity) StarsIntroActivity.this).listView && StarsIntroActivity.this.transactionsLayout.isAttachedToWindow()) {
                boolean isSearchFieldVisible = ((BaseFragment) StarsIntroActivity.this).actionBar.isSearchFieldVisible();
                int top = (((View) StarsIntroActivity.this.transactionsLayout.getParent()).getTop() - AndroidUtilities.statusBarHeight) - ActionBar.getCurrentActionBarHeight();
                int bottom = ((View) StarsIntroActivity.this.transactionsLayout.getParent()).getBottom();
                boolean z = false;
                if (i2 < 0) {
                    if (((GradientHeaderActivity) StarsIntroActivity.this).listView.getHeight() - bottom >= 0) {
                        RecyclerListView currentListView = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                        int findFirstVisibleItemPosition = ((LinearLayoutManager) currentListView.getLayoutManager()).findFirstVisibleItemPosition();
                        if (findFirstVisibleItemPosition != -1) {
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition = currentListView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                            int top2 = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : -1;
                            int paddingTop = currentListView.getPaddingTop();
                            if (top2 != paddingTop || findFirstVisibleItemPosition != 0) {
                                iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, top2 - paddingTop);
                                currentListView.scrollBy(0, i2);
                                z = true;
                            }
                        }
                    }
                    if (isSearchFieldVisible) {
                        if (z || top >= 0) {
                            iArr[1] = i2;
                        } else {
                            iArr[1] = i2 - Math.max(top, i2);
                        }
                    }
                } else if (isSearchFieldVisible) {
                    RecyclerListView currentListView2 = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                    iArr[1] = i2;
                    if (top > 0) {
                        iArr[1] = i2 - i2;
                    }
                    if (currentListView2 == null || (i4 = iArr[1]) <= 0) {
                        return;
                    }
                    currentListView2.scrollBy(0, i4);
                } else if (i2 > 0) {
                    RecyclerListView currentListView3 = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                    if (((GradientHeaderActivity) StarsIntroActivity.this).listView.getHeight() - bottom < 0 || currentListView3 == null || currentListView3.canScrollVertically(1)) {
                        return;
                    }
                    iArr[1] = i2;
                    ((GradientHeaderActivity) StarsIntroActivity.this).listView.stopScroll();
                }
            }
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
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

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
            return i == 2;
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
        }

        @Override // org.telegram.ui.Components.NestedSizeNotifierLayout, androidx.core.view.NestedScrollingParent2
        public void onStopNestedScroll(View view, int i) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
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

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory {
            static {
                UItem.UItemFactory.setup(new Factory());
            }

            public static UItem asStarTier(int i, int i2, TL_stars.TL_starsGiftOption tL_starsGiftOption) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.id = i;
                ofFactory.intValue = i2;
                long j = tL_starsGiftOption.stars;
                ofFactory.longValue = j;
                ofFactory.text = LocaleController.formatPluralStringSpaced("StarsCount", (int) j);
                ofFactory.subtext = tL_starsGiftOption.loadingStorePrice ? null : BillingController.getInstance().formatCurrency(tL_starsGiftOption.amount, tL_starsGiftOption.currency);
                ofFactory.object = tL_starsGiftOption;
                return ofFactory;
            }

            public static UItem asStarTier(int i, int i2, TL_stars.TL_starsTopupOption tL_starsTopupOption) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.id = i;
                ofFactory.intValue = i2;
                long j = tL_starsTopupOption.stars;
                ofFactory.longValue = j;
                ofFactory.text = LocaleController.formatPluralStringSpaced("StarsCount", (int) j);
                ofFactory.subtext = tL_starsTopupOption.loadingStorePrice ? null : BillingController.getInstance().formatCurrency(tL_starsTopupOption.amount, tL_starsTopupOption.currency);
                ofFactory.object = tL_starsTopupOption;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((StarTierView) view).set(uItem.intValue, uItem.text, uItem.subtext, z);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public boolean contentsEquals(UItem uItem, UItem uItem2) {
                return uItem.intValue == uItem2.intValue && uItem.id == uItem2.id && TextUtils.equals(uItem.subtext, uItem2.subtext);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public StarTierView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                return new StarTierView(context, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public boolean equals(UItem uItem, UItem uItem2) {
                return uItem.id == uItem2.id;
            }
        }

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
            AnimatedTextView animatedTextView = new AnimatedTextView(context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.StarsBalanceView.1
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bounce$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.amountTextView.setScaleX(floatValue);
            this.amountTextView.setScaleY(floatValue);
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

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.starBalanceUpdated) {
                updateBalance(true);
            }
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

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(ActionBar.getCurrentActionBarHeight(), 1073741824));
        }

        public void updateBalance(boolean z) {
            StarsController starsController = StarsController.getInstance(this.currentAccount);
            this.amountTextView.cancelAnimation();
            long balance = StarsController.getInstance(this.currentAccount).getBalance();
            long j = this.lastBalance;
            if (balance > j && j != -1) {
                bounce();
            }
            if (starsController.balanceAvailable()) {
                this.amountTextView.setText(LocaleController.formatNumber(balance, ' '));
                this.lastBalance = balance;
                return;
            }
            if (this.loadingString == null) {
                SpannableString spannableString = new SpannableString("x");
                this.loadingString = spannableString;
                spannableString.setSpan(new LoadingSpan(this.amountTextView, AndroidUtilities.dp(48.0f)), 0, this.loadingString.length(), 33);
            }
            this.amountTextView.setText(this.loadingString, z);
            this.lastBalance = -1L;
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsNeededSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private final HeaderView headerView;
        private final long starsNeeded;
        private Runnable whenPurchased;

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
                frameLayout.addView(gLIconTextureView, LayoutHelper.createFrame(NotificationCenter.groupCallVisibilityChanged, 170.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
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

        public StarsNeededSheet(Context context, Theme.ResourcesProvider resourcesProvider, long j, int i, String str, Runnable runnable) {
            super(context, null, false, false, false, resourcesProvider);
            String str2;
            String str3;
            this.BUTTON_EXPAND = -1;
            this.topPadding = 0.2f;
            this.whenPurchased = runnable;
            fixNavigationBar();
            RecyclerListView recyclerListView = this.recyclerListView;
            int i2 = this.backgroundPaddingLeft;
            recyclerListView.setPadding(i2, 0, i2, 0);
            this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i3) {
                    StarsIntroActivity.StarsNeededSheet.this.lambda$new$0(view, i3);
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
            headerView.titleView.setText(LocaleController.formatPluralString("StarsNeededTitle", (int) Math.max(0L, j - StarsController.getInstance(this.currentAccount).getBalance()), new Object[0]));
            if (i == 1) {
                str2 = "StarsNeededTextBuySubscription";
            } else if (i == 2 || i == 3) {
                str2 = "StarsNeededTextKeepSubscription";
            } else if (i == 4) {
                str2 = "StarsNeededTextLink";
                if (str == null) {
                    str3 = "StarsNeededTextLink";
                } else {
                    str3 = "StarsNeededTextLink_" + str.toLowerCase();
                }
                if (LocaleController.nullable(LocaleController.getString(str3)) != null) {
                    str2 = str3;
                }
            } else {
                str2 = i == 5 ? "StarsNeededTextReactions" : i == 6 ? "StarsNeededTextGift" : "StarsNeededText";
            }
            if (TextUtils.isEmpty(str2)) {
                headerView.subtitleView.setText("");
            } else {
                String nullable = LocaleController.nullable(LocaleController.formatString(str2, LocaleController.getStringResId(str2), str));
                headerView.subtitleView.setText(AndroidUtilities.replaceTags(nullable == null ? LocaleController.getString(str2) : nullable));
                TextView textView = headerView.subtitleView;
                textView.setMaxWidth(HintView2.cutInFancyHalf(textView.getText(), headerView.subtitleView.getPaint()));
            }
            this.actionBar.setTitle(getTitle());
            FrameLayout frameLayout = new FrameLayout(context);
            this.footerView = frameLayout;
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            frameLayout.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            linksTextView.setTextSize(1, 12.0f);
            linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
            linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
            linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda2
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(UItem uItem, Boolean bool, String str) {
            if (getContext() == null) {
                return;
            }
            if (bool.booleanValue()) {
                BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
                this.fireworksOverlay.start(true);
                StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
            } else if (str != null) {
                BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
            }
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
            UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.StarsNeededSheet.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
                }
            }, this.resourcesProvider);
            this.adapter = universalAdapter;
            return universalAdapter;
        }

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

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
        public void dismiss() {
            super.dismiss();
            HeaderView headerView = this.headerView;
            if (headerView != null) {
                headerView.iconView.setPaused(true);
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        }

        /* JADX WARN: Code restructure failed: missing block: B:36:0x00c2, code lost:
            if (r15 != false) goto L42;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00c4, code lost:
            r15 = org.telegram.messenger.R.string.NotifyLessOptions;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00c7, code lost:
            r15 = org.telegram.messenger.R.string.NotifyMoreOptions;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00db, code lost:
            if (r15 != false) goto L42;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
            UItem asFlicker;
            int i;
            arrayList.add(UItem.asCustom(this.headerView));
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
            ArrayList options = StarsController.getInstance(this.currentAccount).getOptions();
            if (options == null || options.isEmpty()) {
                arrayList.add(UItem.asFlicker(31));
                arrayList.add(UItem.asFlicker(31));
                asFlicker = UItem.asFlicker(31);
            } else {
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                boolean z = false;
                int i5 = 1;
                for (int i6 = 0; i6 < options.size(); i6++) {
                    TL_stars.TL_starsTopupOption tL_starsTopupOption = (TL_stars.TL_starsTopupOption) options.get(i6);
                    if (tL_starsTopupOption.stars >= this.starsNeeded) {
                        if (tL_starsTopupOption.extended && !this.expanded && z) {
                            i4++;
                        } else {
                            arrayList.add(StarTierView.Factory.asStarTier(i6, i5, tL_starsTopupOption));
                            i3++;
                            i5++;
                            z = true;
                        }
                    }
                }
                if (i3 >= 3) {
                    if (i3 > 0) {
                        boolean z2 = this.expanded;
                        if (!z2 && i4 > 0) {
                        }
                    } else {
                        while (i2 < options.size()) {
                            arrayList.add(StarTierView.Factory.asStarTier(i2, i5, (TL_stars.TL_starsTopupOption) options.get(i2)));
                            i2++;
                            i5++;
                        }
                    }
                    arrayList.add(UItem.asCustom(this.footerView));
                }
                arrayList.clear();
                arrayList.add(UItem.asCustom(this.headerView));
                arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
                int i7 = 0;
                for (int i8 = 0; i8 < options.size(); i8++) {
                    TL_stars.TL_starsTopupOption tL_starsTopupOption2 = (TL_stars.TL_starsTopupOption) options.get(i8);
                    if (tL_starsTopupOption2.stars >= this.starsNeeded) {
                        arrayList.add(StarTierView.Factory.asStarTier(i8, i5, tL_starsTopupOption2));
                        i7++;
                        i5++;
                    }
                }
                if (i7 == 0) {
                    while (i2 < options.size()) {
                        arrayList.add(StarTierView.Factory.asStarTier(i2, i5, (TL_stars.TL_starsTopupOption) options.get(i2)));
                        i2++;
                        i5++;
                    }
                    boolean z3 = this.expanded;
                    if (!z3) {
                    }
                } else {
                    this.expanded = true;
                }
                arrayList.add(UItem.asCustom(this.footerView));
                asFlicker = ExpandView.Factory.asExpand(-1, LocaleController.getString(i), !this.expanded).accent();
            }
            arrayList.add(asFlicker);
            arrayList.add(UItem.asCustom(this.footerView));
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected CharSequence getTitle() {
            HeaderView headerView = this.headerView;
            if (headerView == null) {
                return null;
            }
            return headerView.titleView.getText();
        }

        public void onItemClick(final UItem uItem, UniversalAdapter universalAdapter) {
            if (uItem.id == -1) {
                this.expanded = !this.expanded;
                universalAdapter.update(true);
            } else if (uItem.instanceOf(StarTierView.Factory.class) && (uItem.object instanceof TL_stars.TL_starsTopupOption)) {
                Activity findActivity = AndroidUtilities.findActivity(getContext());
                if (findActivity == null) {
                    findActivity = LaunchActivity.instance;
                }
                if (findActivity == null) {
                    return;
                }
                StarsController.getInstance(this.currentAccount).buy(findActivity, (TL_stars.TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsNeededSheet$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        StarsIntroActivity.StarsNeededSheet.this.lambda$onItemClick$2(uItem, (Boolean) obj, (String) obj2);
                    }
                });
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
        public void show() {
            if (StarsController.getInstance(this.currentAccount).getBalance() >= this.starsNeeded) {
                Runnable runnable = this.whenPurchased;
                if (runnable != null) {
                    runnable.run();
                    this.whenPurchased = null;
                    return;
                }
                return;
            }
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) lastFragment;
                if (chatActivity.isKeyboardVisible() && chatActivity.getChatActivityEnterView() != null) {
                    chatActivity.getChatActivityEnterView().closeKeyboard();
                }
            }
            super.show();
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsOptionsSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private Runnable whenPurchased;

        public StarsOptionsSheet(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, null, false, false, false, resourcesProvider);
            this.BUTTON_EXPAND = -1;
            this.whenPurchased = this.whenPurchased;
            RecyclerListView recyclerListView = this.recyclerListView;
            int i = this.backgroundPaddingLeft;
            recyclerListView.setPadding(i, 0, i, 0);
            this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsOptionsSheet$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i2) {
                    StarsIntroActivity.StarsOptionsSheet.this.lambda$new$0(view, i2);
                }
            });
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setSupportsChangeAnimations(false);
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            defaultItemAnimator.setDurations(350L);
            this.recyclerListView.setItemAnimator(defaultItemAnimator);
            int i2 = Theme.key_windowBackgroundWhite;
            setBackgroundColor(Theme.getColor(i2, resourcesProvider));
            fixNavigationBar(Theme.getColor(i2, resourcesProvider));
            this.actionBar.setTitle(getTitle());
            FrameLayout frameLayout = new FrameLayout(context);
            this.footerView = frameLayout;
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            frameLayout.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            linksTextView.setTextSize(1, 12.0f);
            linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
            linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
            linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsOptionsSheet$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.StarsOptionsSheet.this.lambda$new$1();
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(UItem uItem, Boolean bool, String str) {
            if (getContext() == null) {
                return;
            }
            dismiss();
            StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment == null) {
                return;
            }
            if (!bool.booleanValue()) {
                if (str != null) {
                    BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
                    return;
                }
                return;
            }
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
            LaunchActivity launchActivity = LaunchActivity.instance;
            if (launchActivity != null) {
                launchActivity.getFireworksOverlay().start(true);
            }
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
            UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsOptionsSheet$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.StarsOptionsSheet.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
                }
            }, this.resourcesProvider);
            this.adapter = universalAdapter;
            universalAdapter.setApplyBackground(false);
            return this.adapter;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            UniversalAdapter universalAdapter;
            if ((i == NotificationCenter.starOptionsLoaded || i == NotificationCenter.starBalanceUpdated) && (universalAdapter = this.adapter) != null) {
                universalAdapter.update(true);
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        }

        public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
            UItem asFlicker;
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.TelegramStarsChoose)));
            ArrayList options = StarsController.getInstance(this.currentAccount).getOptions();
            if (options != null && !options.isEmpty()) {
                int i = 0;
                int i2 = 1;
                for (int i3 = 0; i3 < options.size(); i3++) {
                    TL_stars.TL_starsTopupOption tL_starsTopupOption = (TL_stars.TL_starsTopupOption) options.get(i3);
                    if (!tL_starsTopupOption.extended || this.expanded) {
                        arrayList.add(StarTierView.Factory.asStarTier(i3, i2, tL_starsTopupOption));
                        i2++;
                    } else {
                        i++;
                    }
                }
                boolean z = this.expanded;
                if (!z && i > 0) {
                    asFlicker = ExpandView.Factory.asExpand(-1, LocaleController.getString(z ? R.string.NotifyLessOptions : R.string.NotifyMoreOptions), !this.expanded).accent();
                }
                arrayList.add(UItem.asCustom(this.footerView));
            }
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            arrayList.add(UItem.asFlicker(31));
            asFlicker = UItem.asFlicker(31);
            arrayList.add(asFlicker);
            arrayList.add(UItem.asCustom(this.footerView));
        }

        @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
        protected CharSequence getTitle() {
            return LocaleController.getString(R.string.StarsBuy);
        }

        public void onItemClick(final UItem uItem, UniversalAdapter universalAdapter) {
            if (uItem.id == -1) {
                this.expanded = !this.expanded;
                universalAdapter.update(true);
                this.recyclerListView.smoothScrollBy(0, AndroidUtilities.dp(300.0f));
            } else if (uItem.instanceOf(StarTierView.Factory.class) && (uItem.object instanceof TL_stars.TL_starsTopupOption)) {
                Activity findActivity = AndroidUtilities.findActivity(getContext());
                if (findActivity == null) {
                    findActivity = LaunchActivity.instance;
                }
                if (findActivity == null) {
                    return;
                }
                StarsController.getInstance(this.currentAccount).buy(findActivity, (TL_stars.TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$StarsOptionsSheet$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        StarsIntroActivity.StarsOptionsSheet.this.lambda$onItemClick$2(uItem, (Boolean) obj, (String) obj2);
                    }
                });
            }
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
        public void show() {
            StarsController.getInstance(this.currentAccount).getBalance();
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) lastFragment;
                if (chatActivity.isKeyboardVisible() && chatActivity.getChatActivityEnterView() != null) {
                    chatActivity.getChatActivityEnterView().closeKeyboard();
                }
            }
            super.show();
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starOptionsLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsSubscriptionView extends LinearLayout {
        private final int currentAccount;
        public final BackupImageView imageView;
        private boolean needDivider;
        public final LinearLayout priceLayout;
        public final TextView priceSubtitleView;
        public final TextView priceTitleView;
        private final Theme.ResourcesProvider resourcesProvider;
        public final TextView subtitleView;
        public final LinearLayout textLayout;
        public final SimpleTextView titleView;

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory {
            static {
                UItem.UItemFactory.setup(new Factory());
            }

            public static UItem asSubscription(TL_stars.StarsSubscription starsSubscription) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.object = starsSubscription;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((StarsSubscriptionView) view).set((TL_stars.StarsSubscription) uItem.object, z);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public StarsSubscriptionView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                StarsSubscriptionView starsSubscriptionView = (StarsSubscriptionView) getCached();
                return starsSubscriptionView != null ? starsSubscriptionView : new StarsSubscriptionView(context, i, resourcesProvider);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public boolean equals(UItem uItem, UItem uItem2) {
                if (uItem == null && uItem2 == null) {
                    return true;
                }
                if (uItem != null && uItem2 != null) {
                    Object obj = uItem.object;
                    if (obj instanceof TL_stars.StarsSubscription) {
                        Object obj2 = uItem2.object;
                        if (obj2 instanceof TL_stars.StarsSubscription) {
                            return TextUtils.equals(((TL_stars.StarsSubscription) obj).id, ((TL_stars.StarsSubscription) obj2).id);
                        }
                    }
                }
                return false;
            }
        }

        public StarsSubscriptionView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.currentAccount = i;
            this.resourcesProvider = resourcesProvider;
            setOrientation(0);
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(46.0f));
            addView(backupImageView, LayoutHelper.createLinear(46, 46, 0.0f, 19, 13, 0, 13, 0));
            LinearLayout linearLayout = new LinearLayout(context);
            this.textLayout = linearLayout;
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 16, 0, 0, 0, 0));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.titleView = simpleTextView;
            int i2 = Theme.key_windowBackgroundWhiteBlackText;
            simpleTextView.setTextColor(Theme.getColor(i2, resourcesProvider));
            simpleTextView.setTextSize(16);
            simpleTextView.setTypeface(AndroidUtilities.bold());
            linearLayout.addView(simpleTextView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 2.0f));
            TextView textView = new TextView(context);
            this.subtitleView = textView;
            int i3 = Theme.key_windowBackgroundWhiteGrayText2;
            textView.setTextColor(Theme.getColor(i3, resourcesProvider));
            textView.setTextSize(1, 14.0f);
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.priceLayout = linearLayout2;
            linearLayout2.setOrientation(1);
            addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 0.0f, 16, 0, 0, 18, 0));
            TextView textView2 = new TextView(context);
            this.priceTitleView = textView2;
            textView2.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView2.setTextSize(1, 16.0f);
            textView2.setTypeface(AndroidUtilities.bold());
            textView2.setGravity(5);
            linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2, 5, 0, 0, 0, 1));
            TextView textView3 = new TextView(context);
            this.priceSubtitleView = textView3;
            textView3.setTextColor(Theme.getColor(i3, resourcesProvider));
            textView3.setTextSize(1, 13.0f);
            textView3.setGravity(5);
            linearLayout2.addView(textView3, LayoutHelper.createLinear(-1, -2, 5, 0, 0, 0, 0));
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.needDivider) {
                canvas.drawRect(AndroidUtilities.dp(72.0f), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight(), Theme.dividerPaint);
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f), 1073741824));
        }

        public void set(TL_stars.StarsSubscription starsSubscription, boolean z) {
            TextView textView;
            String str;
            int i;
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(starsSubscription.peer)));
            if (chat == null) {
                return;
            }
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setInfo(chat);
            this.imageView.setForUserOrChat(chat, avatarDrawable);
            long currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            this.titleView.setText(chat.title);
            if (starsSubscription.canceled) {
                TextView textView2 = this.subtitleView;
                long j = starsSubscription.until_date;
                textView2.setText(LocaleController.formatString(j < currentTime ? R.string.StarsSubscriptionExpired : R.string.StarsSubscriptionExpires, LocaleController.formatDateChat(j)));
                this.priceTitleView.setVisibility(8);
                this.priceSubtitleView.setTextColor(Theme.getColor(Theme.key_color_red, this.resourcesProvider));
                textView = this.priceSubtitleView;
                i = R.string.StarsSubscriptionStatusCancelled;
            } else {
                long j2 = starsSubscription.until_date;
                if (j2 < currentTime) {
                    this.subtitleView.setText(LocaleController.formatString(R.string.StarsSubscriptionExpired, LocaleController.formatDateChat(j2)));
                    this.priceTitleView.setVisibility(8);
                    this.priceSubtitleView.setTextColor(Theme.getColor(Theme.key_color_red, this.resourcesProvider));
                    textView = this.priceSubtitleView;
                    i = R.string.StarsSubscriptionStatusExpired;
                } else {
                    this.subtitleView.setText(LocaleController.formatString(R.string.StarsSubscriptionRenews, LocaleController.formatDateChat(j2)));
                    this.priceTitleView.setVisibility(0);
                    this.priceTitleView.setText(StarsIntroActivity.replaceStarsWithPlain("⭐️ " + Long.toString(starsSubscription.pricing.amount), 0.8f));
                    this.priceSubtitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, this.resourcesProvider));
                    int i2 = starsSubscription.pricing.period;
                    if (i2 != 2592000) {
                        if (i2 != 60) {
                            if (i2 == 300) {
                                textView = this.priceSubtitleView;
                                str = "per 5 minutes";
                            }
                            this.needDivider = z;
                            setWillNotDraw(!z);
                        }
                        textView = this.priceSubtitleView;
                        str = "per minute";
                        textView.setText(str);
                        this.needDivider = z;
                        setWillNotDraw(!z);
                    }
                    textView = this.priceSubtitleView;
                    i = R.string.StarsParticipantSubscriptionPerMonth;
                }
            }
            str = LocaleController.getString(i);
            textView.setText(str);
            this.needDivider = z;
            setWillNotDraw(!z);
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsTransactionView extends LinearLayout {
        public static HashMap cachedPlatformDrawables;
        private final TextView amountTextView;
        private final AvatarDrawable avatarDrawable;
        private Runnable cancelCurrentGift;
        private final int currentAccount;
        private final TextView dateTextView;
        private final LinearLayout.LayoutParams dateTextViewParams;
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

        /* loaded from: classes3.dex */
        public static class Factory extends UItem.UItemFactory {
            static {
                UItem.UItemFactory.setup(new Factory());
            }

            public static UItem asTransaction(TL_stars.StarsTransaction starsTransaction, boolean z) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.object = starsTransaction;
                ofFactory.accent = z;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                ((StarsTransactionView) view).set((TL_stars.StarsTransaction) uItem.object, uItem.accent, z);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public StarsTransactionView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                StarsTransactionView starsTransactionView = (StarsTransactionView) getCached();
                return starsTransactionView != null ? starsTransactionView : new StarsTransactionView(context, i, resourcesProvider);
            }
        }

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
            addView(frameLayout, LayoutHelper.createLinear(72, -1, 0.0f, 115));
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
            addView(linearLayout, LayoutHelper.createLinear(-2, -1, 1.0f, 119));
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setTypeface(AndroidUtilities.bold());
            int i2 = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView.setTextSize(1, 16.0f);
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView.setEllipsize(truncateAt);
            textView.setSingleLine(true);
            LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 4.33f);
            this.titleTextViewParams = createLinear;
            linearLayout.addView(textView, createLinear);
            TextView textView2 = new TextView(context);
            this.subtitleTextView = textView2;
            textView2.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView2.setTextSize(1, 13.0f);
            textView2.setEllipsize(truncateAt);
            textView2.setSingleLine(true);
            linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.33f));
            TextView textView3 = new TextView(context);
            this.dateTextView = textView3;
            textView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
            textView3.setTextSize(1, 14.0f);
            textView3.setEllipsize(truncateAt);
            textView3.setSingleLine(true);
            LinearLayout.LayoutParams createLinear2 = LayoutHelper.createLinear(-1, -2);
            this.dateTextViewParams = createLinear2;
            linearLayout.addView(textView3, createLinear2);
            TextView textView4 = new TextView(context);
            this.amountTextView = textView4;
            textView4.setTypeface(AndroidUtilities.bold());
            textView4.setTextSize(1, 15.3f);
            textView4.setGravity(5);
            addView(textView4, LayoutHelper.createLinear(-2, -2, 0.0f, 21, 8, 0, 20, 0));
            SpannableString spannableString = new SpannableString("⭐️");
            this.star = spannableString;
            Drawable mutate = context.getResources().getDrawable(R.drawable.star_small_inner).mutate();
            mutate.setBounds(0, 0, AndroidUtilities.dp(21.0f), AndroidUtilities.dp(21.0f));
            spannableString.setSpan(new ImageSpan(mutate), 0, spannableString.length(), 33);
        }

        public static CombinedDrawable getPlatformDrawable(String str) {
            return getPlatformDrawable(str, 44);
        }

        public static CombinedDrawable getPlatformDrawable(String str, int i) {
            if (i != 44) {
                return SessionCell.createDrawable(i, str);
            }
            if (cachedPlatformDrawables == null) {
                cachedPlatformDrawables = new HashMap();
            }
            CombinedDrawable combinedDrawable = (CombinedDrawable) cachedPlatformDrawables.get(str);
            if (combinedDrawable == null) {
                HashMap hashMap = cachedPlatformDrawables;
                CombinedDrawable createDrawable = SessionCell.createDrawable(44, str);
                hashMap.put(str, createDrawable);
                return createDrawable;
            }
            return combinedDrawable;
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

        /* JADX WARN: Code restructure failed: missing block: B:103:0x0237, code lost:
            if (r3 != false) goto L73;
         */
        /* JADX WARN: Code restructure failed: missing block: B:152:0x035e, code lost:
            if (r3 != false) goto L73;
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x0360, code lost:
            r3 = 8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:154:0x0363, code lost:
            r3 = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x020b, code lost:
            if (r3 != false) goto L73;
         */
        /* JADX WARN: Code restructure failed: missing block: B:98:0x0222, code lost:
            if (r3 != false) goto L73;
         */
        /* JADX WARN: Removed duplicated region for block: B:186:0x042f  */
        /* JADX WARN: Removed duplicated region for block: B:188:0x045e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void set(TL_stars.StarsTransaction starsTransaction, boolean z, boolean z2) {
            TextView textView;
            CharSequence concat;
            BackupImageView backupImageView;
            String str;
            boolean z3;
            String str2;
            boolean z4;
            String str3;
            TextView textView2;
            TextView textView3;
            ImageLocation imageLocation;
            ImageLocation forDocument;
            int i;
            TextView textView4;
            int i2;
            long j;
            TextView textView5;
            CharSequence concat2;
            long peerDialogId = DialogObject.getPeerDialogId(starsTransaction.peer.peer);
            boolean z5 = peerDialogId != 0 || starsTransaction.subscription || starsTransaction.stargift != null || (starsTransaction.gift && (starsTransaction.peer instanceof TL_stars.TL_starsTransactionPeerFragment));
            this.threeLines = z5;
            this.titleTextViewParams.bottomMargin = z5 ? 0 : AndroidUtilities.dp(4.33f);
            this.subtitleTextView.setVisibility(this.threeLines ? 0 : 8);
            this.dateTextView.setTextSize(1, this.threeLines ? 13.0f : 14.0f);
            this.dateTextView.setText(LocaleController.formatShortDateTime(starsTransaction.date));
            if (starsTransaction.refund) {
                TextView textView6 = this.dateTextView;
                textView6.setText(TextUtils.concat(textView6.getText(), " — ", LocaleController.getString(R.string.StarsRefunded)));
            } else {
                if (starsTransaction.failed) {
                    textView = this.dateTextView;
                    concat = TextUtils.concat(textView.getText(), " — ", LocaleController.getString(R.string.StarsFailed));
                } else if (starsTransaction.pending) {
                    textView = this.dateTextView;
                    concat = TextUtils.concat(textView.getText(), " — ", LocaleController.getString(R.string.StarsPending));
                }
                textView.setText(concat);
            }
            Runnable runnable = this.cancelCurrentGift;
            if (runnable != null) {
                runnable.run();
                this.cancelCurrentGift = null;
            }
            this.imageView.setTranslationX(0.0f);
            this.imageView.setTranslationY(0.0f);
            this.imageView2.setVisibility(8);
            this.imageView.setRoundRadius(AndroidUtilities.dp(46.0f));
            if (starsTransaction.stargift == null) {
                if (peerDialogId != 0) {
                    if (UserObject.isService(peerDialogId)) {
                        str3 = LocaleController.getString(R.string.StarsTransactionUnknown);
                        this.imageView.setImageDrawable(getPlatformDrawable("fragment"));
                        z4 = false;
                    } else {
                        if (peerDialogId >= 0) {
                            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId));
                            z3 = user == null;
                            if (starsTransaction.photo == null) {
                                this.avatarDrawable.setInfo(user);
                                this.imageView.setForUserOrChat(user, this.avatarDrawable);
                            }
                            str2 = UserObject.getUserName(user);
                        } else {
                            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId));
                            z3 = chat == null;
                            if (starsTransaction.photo == null) {
                                this.avatarDrawable.setInfo(chat);
                                this.imageView.setForUserOrChat(chat, this.avatarDrawable);
                            }
                            if (chat == null) {
                                z4 = z3;
                                str3 = "";
                            } else {
                                str2 = chat.title;
                            }
                        }
                        boolean z6 = z3;
                        str3 = str2;
                        z4 = z6;
                    }
                    if (starsTransaction.subscription) {
                        this.titleTextView.setText(str3);
                        int i3 = starsTransaction.subscription_period;
                        if (i3 == 2592000) {
                            this.subtitleTextView.setVisibility(0);
                            textView4 = this.subtitleTextView;
                            i2 = R.string.StarsTransactionSubscriptionMonthly;
                        } else {
                            String str4 = i3 == 300 ? "5 minutes" : "Minute";
                            this.subtitleTextView.setVisibility(0);
                            this.subtitleTextView.setText(String.format(Locale.US, "%s subscription fee", str4));
                        }
                    } else {
                        if (starsTransaction.gift) {
                            this.titleTextView.setText(LocaleController.getString(R.string.StarsGiftReceived));
                            textView2 = this.subtitleTextView;
                        } else if ((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0) {
                            this.titleTextView.setText(LocaleController.getString(R.string.StarsGiveawayPrizeReceived));
                            textView2 = this.subtitleTextView;
                        } else if (starsTransaction.reaction) {
                            this.titleTextView.setText(LocaleController.getString(R.string.StarsReactionsSent));
                            textView2 = this.subtitleTextView;
                        } else if (!starsTransaction.extended_media.isEmpty()) {
                            if (z) {
                                this.titleTextView.setText(str3);
                                this.subtitleTextView.setVisibility(0);
                                textView3 = this.subtitleTextView;
                                str3 = LocaleController.getString(R.string.StarMediaPurchase);
                            } else {
                                this.titleTextView.setText(LocaleController.getString(R.string.StarMediaPurchase));
                                this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                                textView3 = this.subtitleTextView;
                            }
                            textView3.setText(str3);
                            this.imageViewCount = 0;
                            int i4 = 0;
                            while (i4 < Math.min(2, starsTransaction.extended_media.size())) {
                                TLRPC.MessageMedia messageMedia = starsTransaction.extended_media.get(i4);
                                BackupImageView backupImageView2 = i4 == 0 ? this.imageView : this.imageView2;
                                backupImageView2.setRoundRadius(AndroidUtilities.dp(12.0f));
                                if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                                    forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(messageMedia.photo.sizes, AndroidUtilities.dp(46.0f), true), messageMedia.photo);
                                } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                                    forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(messageMedia.document.thumbs, AndroidUtilities.dp(46.0f), true), messageMedia.document);
                                } else {
                                    imageLocation = null;
                                    backupImageView2.setVisibility(0);
                                    backupImageView2.setImage(imageLocation, "46_46", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                                    this.imageViewCount++;
                                    i4++;
                                }
                                imageLocation = forDocument;
                                backupImageView2.setVisibility(0);
                                backupImageView2.setImage(imageLocation, "46_46", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                                this.imageViewCount++;
                                i4++;
                            }
                            int i5 = 0;
                            while (i5 < this.imageViewCount) {
                                BackupImageView backupImageView3 = i5 == 0 ? this.imageView : this.imageView2;
                                float f = i5;
                                backupImageView3.setTranslationX(AndroidUtilities.dp(2.0f) + ((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f)));
                                backupImageView3.setTranslationY((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f));
                                i5++;
                            }
                        } else if (starsTransaction.photo != null) {
                            TextView textView7 = this.titleTextView;
                            String str5 = starsTransaction.title;
                            textView7.setText(str5 != null ? str5 : "");
                            this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                            this.subtitleTextView.setText(str3);
                            this.imageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsTransaction.photo)), "46_46", (Drawable) null, 0, (Object) null);
                        } else {
                            TextView textView8 = this.titleTextView;
                            String str6 = starsTransaction.title;
                            textView8.setText(str6 != null ? str6 : "");
                            textView2 = this.subtitleTextView;
                        }
                        textView2.setVisibility(i);
                        textView4 = this.subtitleTextView;
                        textView4.setText(str3);
                    }
                } else {
                    TL_stars.StarsTransactionPeer starsTransactionPeer = starsTransaction.peer;
                    if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerAppStore) {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionInApp));
                        backupImageView = this.imageView;
                        str = "ios";
                    } else if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerPlayMarket) {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionInApp));
                        backupImageView = this.imageView;
                        str = "android";
                    } else if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerFragment) {
                        if (starsTransaction.gift) {
                            this.titleTextView.setText(LocaleController.getString(R.string.StarsGiftReceived));
                            this.subtitleTextView.setText(LocaleController.getString(R.string.StarsTransactionUnknown));
                            this.subtitleTextView.setVisibility(0);
                        } else {
                            this.titleTextView.setText(LocaleController.getString(z ? R.string.StarsTransactionWithdrawFragment : R.string.StarsTransactionFragment));
                        }
                        this.imageView.setImageDrawable(getPlatformDrawable("fragment"));
                    } else if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerPremiumBot) {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionBot));
                        backupImageView = this.imageView;
                        str = "premiumbot";
                    } else if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerUnsupported) {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionUnsupported));
                        backupImageView = this.imageView;
                        str = "?";
                    } else if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerAds) {
                        this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionAds));
                        backupImageView = this.imageView;
                        str = "ads";
                    } else {
                        this.titleTextView.setText("");
                        this.imageView.setImageDrawable(null);
                    }
                    backupImageView.setImageDrawable(getPlatformDrawable(str));
                }
                j = starsTransaction.stars;
                if (j <= 0) {
                    this.amountTextView.setVisibility(0);
                    this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_green));
                    textView5 = this.amountTextView;
                    concat2 = TextUtils.concat("+", LocaleController.formatNumber(starsTransaction.stars, ' '), " ", this.star);
                } else if (j >= 0) {
                    this.amountTextView.setVisibility(8);
                    this.needDivider = z2;
                    setWillNotDraw(!z2);
                } else {
                    this.amountTextView.setVisibility(0);
                    this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_red));
                    textView5 = this.amountTextView;
                    concat2 = TextUtils.concat("-", LocaleController.formatNumber(-starsTransaction.stars, ' '), " ", this.star);
                }
                textView5.setText(concat2);
                this.needDivider = z2;
                setWillNotDraw(!z2);
            }
            StarsIntroActivity.setGiftImage(this.imageView.getImageReceiver(), starsTransaction.stargift, 46);
            this.titleTextView.setText(MessagesController.getInstance(this.currentAccount).getPeerName(DialogObject.getPeerDialogId(starsTransaction.peer.peer)));
            if (starsTransaction.refund) {
                textView4 = this.subtitleTextView;
                i2 = starsTransaction.stars > 0 ? R.string.Gift2TransactionRefundedSent : R.string.Gift2TransactionRefundedConverted;
            } else {
                textView4 = this.subtitleTextView;
                i2 = starsTransaction.stars > 0 ? R.string.Gift2TransactionConverted : R.string.Gift2TransactionSent;
            }
            str3 = LocaleController.getString(i2);
            textView4.setText(str3);
            j = starsTransaction.stars;
            if (j <= 0) {
            }
            textView5.setText(concat2);
            this.needDivider = z2;
            setWillNotDraw(!z2);
        }
    }

    /* loaded from: classes3.dex */
    public static class StarsTransactionsLayout extends LinearLayout implements NotificationCenter.NotificationCenterDelegate {
        private final PageAdapter adapter;
        private final long bot_id;
        private final int currentAccount;
        private final ViewPagerFixed.TabsView tabsView;
        private final ViewPagerFixed viewPager;

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
                        if (!Page.this.listView.canScrollVertically(1) || Page.this.isLoadingVisible()) {
                            Page.this.loadTransactionsRunnable.run();
                        }
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
                if (this.bot_id != 0) {
                    BotStarsController botStarsController = BotStarsController.getInstance(this.currentAccount);
                    Iterator it = botStarsController.getTransactions(this.bot_id, this.type).iterator();
                    while (it.hasNext()) {
                        arrayList.add(StarsTransactionView.Factory.asTransaction((TL_stars.StarsTransaction) it.next(), true));
                    }
                    if (botStarsController.didFullyLoadTransactions(this.bot_id, this.type)) {
                        return;
                    }
                } else {
                    StarsController starsController = StarsController.getInstance(this.currentAccount);
                    Iterator it2 = starsController.transactions[this.type].iterator();
                    while (it2.hasNext()) {
                        arrayList.add(StarsTransactionView.Factory.asTransaction((TL_stars.StarsTransaction) it2.next(), false));
                    }
                    if (starsController.didFullyLoadTransactions(this.type)) {
                        return;
                    }
                }
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
                arrayList.add(UItem.asFlicker(arrayList.size(), 7));
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
            public void onClick(UItem uItem, View view, int i, float f, float f2) {
                if (uItem.object instanceof TL_stars.StarsTransaction) {
                    StarsIntroActivity.showTransactionSheet(getContext(), false, 0L, this.currentAccount, (TL_stars.StarsTransaction) uItem.object, this.resourcesProvider);
                }
            }

            @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
            public void didReceivedNotification(int i, int i2, Object... objArr) {
                if (i != NotificationCenter.starTransactionsLoaded) {
                    if (i == NotificationCenter.botStarsTransactionsLoaded && ((Long) objArr[0]).longValue() == this.bot_id) {
                        this.listView.adapter.update(true);
                        return;
                    }
                    return;
                }
                this.listView.adapter.update(true);
                if (!this.listView.canScrollVertically(1) || isLoadingVisible()) {
                    this.loadTransactionsRunnable.run();
                }
            }

            public boolean isLoadingVisible() {
                for (int i = 0; i < this.listView.getChildCount(); i++) {
                    if (this.listView.getChildAt(i) instanceof FlickerLoadingView) {
                        return true;
                    }
                }
                return false;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, this.bot_id != 0 ? NotificationCenter.botStarsTransactionsLoaded : NotificationCenter.starTransactionsLoaded);
                this.listView.adapter.update(false);
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, this.bot_id != 0 ? NotificationCenter.botStarsTransactionsLoaded : NotificationCenter.starTransactionsLoaded);
            }
        }

        /* loaded from: classes3.dex */
        private static class PageAdapter extends ViewPagerFixed.Adapter {
            private final long bot_id;
            private final int classGuid;
            private final Context context;
            private final int currentAccount;
            private final ArrayList items = new ArrayList();
            private final Theme.ResourcesProvider resourcesProvider;

            public PageAdapter(Context context, int i, long j, int i2, Theme.ResourcesProvider resourcesProvider) {
                this.context = context;
                this.currentAccount = i;
                this.classGuid = i2;
                this.resourcesProvider = resourcesProvider;
                this.bot_id = j;
                fill();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i, int i2) {
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i) {
                return new Page(this.context, this.bot_id, i, this.currentAccount, this.classGuid, this.resourcesProvider);
            }

            public void fill() {
                this.items.clear();
                int i = (this.bot_id > 0L ? 1 : (this.bot_id == 0L ? 0 : -1));
                int i2 = this.currentAccount;
                if (i == 0) {
                    StarsController starsController = StarsController.getInstance(i2);
                    this.items.add(UItem.asSpace(0));
                    if (starsController.hasTransactions(1)) {
                        this.items.add(UItem.asSpace(1));
                    }
                    if (!starsController.hasTransactions(2)) {
                        return;
                    }
                } else {
                    BotStarsController botStarsController = BotStarsController.getInstance(i2);
                    this.items.add(UItem.asSpace(0));
                    if (botStarsController.hasTransactions(this.bot_id, 1)) {
                        this.items.add(UItem.asSpace(1));
                    }
                    if (!botStarsController.hasTransactions(this.bot_id, 2)) {
                        return;
                    }
                }
                this.items.add(UItem.asSpace(2));
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return this.items.size();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public String getItemTitle(int i) {
                int i2;
                int itemViewType = getItemViewType(i);
                if (itemViewType == 0) {
                    i2 = R.string.StarsTransactionsAll;
                } else if (itemViewType == 1) {
                    i2 = R.string.StarsTransactionsIncoming;
                } else if (itemViewType != 2) {
                    return "";
                } else {
                    i2 = R.string.StarsTransactionsOutgoing;
                }
                return LocaleController.getString(i2);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i) {
                if (i < 0 || i >= this.items.size()) {
                    return 0;
                }
                return ((UItem) this.items.get(i)).intValue;
            }
        }

        public StarsTransactionsLayout(Context context, int i, long j, int i2, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.currentAccount = i;
            this.bot_id = j;
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

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.starTransactionsLoaded) {
                this.adapter.fill();
                this.viewPager.fillTabs(true);
            }
        }

        public RecyclerListView getCurrentListView() {
            View currentView = this.viewPager.getCurrentView();
            if (currentView instanceof Page) {
                return ((Page) currentView).listView;
            }
            return null;
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
    }

    public StarsIntroActivity() {
        setWhiteBackground(true);
    }

    public static void addAvailabilityRow(TableView tableView, int i, TL_stars.StarGift starGift, Theme.ResourcesProvider resourcesProvider) {
        final TextView textView = (TextView) ((TableView.TableRowContent) tableView.addRow(LocaleController.getString(R.string.Gift2Availability), "").getChildAt(1)).getChildAt(0);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x ");
        LoadingSpan loadingSpan = new LoadingSpan(textView, AndroidUtilities.dp(90.0f), 0, resourcesProvider);
        loadingSpan.setColors(Theme.multAlpha(textView.getPaint().getColor(), 0.65f), Theme.multAlpha(textView.getPaint().getColor(), 0.08f));
        spannableStringBuilder.setSpan(loadingSpan, 0, 1, 33);
        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        StarsController.getInstance(i).getStarGift(starGift.id, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda22
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarsIntroActivity.lambda$addAvailabilityRow$95(textView, (TL_stars.StarGift) obj);
            }
        });
    }

    private static CharSequence appendStatus(SpannableStringBuilder spannableStringBuilder, TextView textView, String str) {
        spannableStringBuilder.append(" ");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ReplacementSpan(textView.getCurrentTextColor(), str) { // from class: org.telegram.ui.Stars.StarsIntroActivity.10
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
            public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
                RectF rectF = AndroidUtilities.rectTmp;
                int i6 = i3 + i5;
                rectF.set(f, (i6 - AndroidUtilities.dp(20.0f)) / 2.0f, AndroidUtilities.dp(12.0f) + f + this.layout.getCurrentWidth(), (AndroidUtilities.dp(20.0f) + i6) / 2.0f);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.backgroundPaint);
                this.layout.draw(canvas, f + AndroidUtilities.dp(6.0f), i6 / 2.0f, this.val$color, 1.0f);
            }

            @Override // android.text.style.ReplacementSpan
            public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
                return (int) (AndroidUtilities.dp(12.0f) + this.layout.getCurrentWidth());
            }
        }, 0, spannableString.length(), 33);
        spannableStringBuilder.append((CharSequence) spannableString);
        return spannableStringBuilder;
    }

    public static CharSequence getTransactionTitle(int i, boolean z, TL_stars.StarsTransaction starsTransaction) {
        if (starsTransaction.extended_media.isEmpty()) {
            if (starsTransaction.stargift != null) {
                if (starsTransaction.refund) {
                    return LocaleController.getString(starsTransaction.stars > 0 ? R.string.Gift2TransactionRefundedSent : R.string.Gift2TransactionRefundedConverted);
                }
                return LocaleController.getString(starsTransaction.stars > 0 ? R.string.Gift2TransactionConverted : R.string.Gift2TransactionSent);
            }
            if (starsTransaction.subscription) {
                int i2 = starsTransaction.subscription_period;
                if (i2 == 2592000) {
                    return LocaleController.getString(R.string.StarSubscriptionPurchase);
                }
                if (i2 == 300) {
                    return "5-minute subscription fee";
                }
                if (i2 == 60) {
                    return "Minute subscription fee";
                }
            }
            if ((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0) {
                return LocaleController.getString(R.string.StarsGiveawayPrizeReceived);
            }
            if (starsTransaction.gift) {
                if (starsTransaction.sent_by != null) {
                    return LocaleController.getString(UserObject.isUserSelf(MessagesController.getInstance(i).getUser(Long.valueOf(DialogObject.getPeerDialogId(starsTransaction.sent_by)))) ? R.string.StarsGiftSent : R.string.StarsGiftReceived);
                }
                return LocaleController.getString(R.string.StarsGiftReceived);
            }
            String str = starsTransaction.title;
            if (str != null) {
                return str;
            }
            long peerDialogId = DialogObject.getPeerDialogId(starsTransaction.peer.peer);
            if (peerDialogId != 0) {
                if (peerDialogId >= 0) {
                    return UserObject.getUserName(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(peerDialogId)));
                }
                TLRPC.Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-peerDialogId));
                return chat == null ? "" : chat.title;
            }
            TL_stars.StarsTransactionPeer starsTransactionPeer = starsTransaction.peer;
            if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerFragment) {
                return LocaleController.getString(z ? R.string.StarsTransactionWithdrawFragment : R.string.StarsTransactionFragment);
            }
            return starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeerPremiumBot ? LocaleController.getString(R.string.StarsTransactionBot) : LocaleController.getString(R.string.StarsTransactionUnsupported);
        }
        return LocaleController.getString(R.string.StarMediaPurchase);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addAvailabilityRow$95(TextView textView, TL_stars.StarGift starGift) {
        if (starGift == null) {
            return;
        }
        int i = starGift.availability_remains;
        int i2 = starGift.availability_total;
        textView.setText(i <= 0 ? LocaleController.formatPluralStringComma("Gift2Availability2ValueNone", i2) : LocaleController.formatPluralStringComma("Gift2Availability4Value", i, LocaleController.formatNumber(i2, ',')));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$0(Context context) {
        new ExplainStarsSheet(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view, int i) {
        UItem item;
        UniversalAdapter universalAdapter = this.adapter;
        if (universalAdapter == null || (item = universalAdapter.getItem(i)) == null) {
            return;
        }
        onItemClick(item, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(Context context, View view) {
        new StarsOptionsSheet(context, this.resourceProvider).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        StarsController.getInstance(this.currentAccount).getGiftOptions();
        UserSelectorBottomSheet.open(1, 0L, BirthdayController.getInstance(this.currentAccount).getState());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemClick$4(UItem uItem, Boolean bool, String str) {
        if (getContext() == null) {
            return;
        }
        if (bool.booleanValue()) {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
            this.fireworksOverlay.start(true);
            StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        } else if (str != null) {
            BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, str)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$5(StarsBalanceView starsBalanceView, View view) {
        BaseFragment lastFragment;
        if (starsBalanceView.lastBalance > 0 && (lastFragment = LaunchActivity.getLastFragment()) != null) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new StarsIntroActivity(), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$6(BottomSheet bottomSheet, ButtonWithCounterView buttonWithCounterView) {
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$7(final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, Boolean bool) {
        if (bool.booleanValue()) {
            bottomSheet.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$openConfirmPurchaseSheet$6(BottomSheet.this, buttonWithCounterView);
                }
            }, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$8(Utilities.Callback callback, final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, View view) {
        if (callback == null) {
            bottomSheet.dismiss();
            return;
        }
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(true);
        callback.run(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda42
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$7(BottomSheet.this, buttonWithCounterView, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$9(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$10(StarsBalanceView starsBalanceView, View view) {
        BaseFragment lastFragment;
        if (starsBalanceView.lastBalance > 0 && (lastFragment = LaunchActivity.getLastFragment()) != null) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new StarsIntroActivity(), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$11(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsSubscribeInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$12(BottomSheet bottomSheet, ButtonWithCounterView buttonWithCounterView) {
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$13(final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, Boolean bool) {
        if (bool.booleanValue()) {
            bottomSheet.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda90
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$openStarsChannelInviteSheet$12(BottomSheet.this, buttonWithCounterView);
                }
            }, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$14(Utilities.Callback callback, final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, View view) {
        if (callback == null) {
            bottomSheet.dismiss();
            return;
        }
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(true);
        callback.run(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda84
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$13(BottomSheet.this, buttonWithCounterView, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$15(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setGiftImage$16(int i, int i2, ImageReceiver imageReceiver, final boolean[] zArr) {
        TLRPC.Document document;
        String str = UserConfig.getInstance(i).premiumGiftsStickerPack;
        if (str == null) {
            MediaDataController.getInstance(i).checkPremiumGiftStickers();
            return;
        }
        TLRPC.TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(i).getStickerSetByName(str);
        if (stickerSetByName == null) {
            stickerSetByName = MediaDataController.getInstance(i).getStickerSetByEmojiOrName(str);
        }
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = stickerSetByName;
        if (tL_messages_stickerSet != null) {
            String str2 = i2 == 2 ? "2⃣" : i2 == 3 ? "3⃣" : "4⃣";
            int i3 = 0;
            while (true) {
                if (i3 >= tL_messages_stickerSet.packs.size()) {
                    break;
                }
                TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet.packs.get(i3);
                if (TextUtils.equals(tL_stickerPack.emoticon, str2) && !tL_stickerPack.documents.isEmpty()) {
                    long longValue = tL_stickerPack.documents.get(0).longValue();
                    for (int i4 = 0; i4 < tL_messages_stickerSet.documents.size(); i4++) {
                        document = tL_messages_stickerSet.documents.get(i4);
                        if (document != null && document.id == longValue) {
                            break;
                        }
                    }
                } else {
                    i3++;
                }
            }
            document = null;
            if (document == null && !tL_messages_stickerSet.documents.isEmpty()) {
                document = tL_messages_stickerSet.documents.get(0);
            }
        } else {
            document = null;
        }
        if (document == null) {
            MediaDataController.getInstance(i).loadStickersByEmojiOrName(str, false, tL_messages_stickerSet == null);
            return;
        }
        imageReceiver.setAllowStartLottieAnimation(true);
        imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity.5
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public void didSetImage(ImageReceiver imageReceiver2, boolean z, boolean z2, boolean z3) {
                RLottieDrawable lottieAnimation;
                if (!z || (lottieAnimation = imageReceiver2.getLottieAnimation()) == null || zArr[0]) {
                    return;
                }
                lottieAnimation.setCurrentFrame(0, false);
                AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda7(lottieAnimation));
                zArr[0] = true;
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i5, String str3, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i5, str3, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver2) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver2);
            }
        });
        Drawable svgThumb = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, NotificationCenter.audioRouteChanged, true, null, true);
        imageReceiver.setAutoRepeat(0);
        imageReceiver.setImage(ImageLocation.getForDocument(document), "160_160_nr", ImageLocation.getForDocument(closestPhotoSizeWithSize, document), "160_160", svgThumb, document.size, "tgs", tL_messages_stickerSet, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setGiftImage$19(Runnable runnable, Runnable runnable2) {
        runnable.run();
        runnable2.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$70(Context context) {
        new ExplainStarsSheet(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$71(BottomSheet[] bottomSheetArr, long j, long j2) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null || UserObject.isService(j)) {
            return;
        }
        Bundle bundle = new Bundle();
        if (j > 0) {
            bundle.putLong("user_id", j);
            if (j == j2) {
                bundle.putBoolean("my_profile", true);
                bundle.putBoolean("open_gifts", true);
            }
        } else {
            bundle.putLong("chat_id", -j);
        }
        safeLastFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$72(BottomSheet[] bottomSheetArr, long j, long j2) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null || UserObject.isService(j)) {
            return;
        }
        Bundle bundle = new Bundle();
        if (j > 0) {
            bundle.putLong("user_id", j);
            if (j == j2) {
                bundle.putBoolean("my_profile", true);
                bundle.putBoolean("open_gifts", true);
            }
        } else {
            bundle.putLong("chat_id", -j);
        }
        safeLastFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$73(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$74(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$75(TLObject tLObject, BottomSheet[] bottomSheetArr, final int i, long j, TL_stars.StarGift starGift, boolean z, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider, ButtonWithCounterView buttonWithCounterView) {
        BulletinFactory of;
        String string;
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            bottomSheetArr[0].dismiss();
            StarsController.GiftsList profileGiftsList = StarsController.getInstance(i).getProfileGiftsList(j, false);
            if (profileGiftsList != null) {
                profileGiftsList.invalidate();
            }
            BulletinFactory.of(safeLastFragment).createEmojiBulletin(starGift.sticker, LocaleController.getString(z ? R.string.Gift2MadePrivateTitle : R.string.Gift2MadePublicTitle), AndroidUtilities.replaceSingleTag(LocaleController.getString(z ? R.string.Gift2MadePrivate : R.string.Gift2MadePublic), safeLastFragment instanceof ProfileActivity ? null : new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda81
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showActionGiftSheet$74(i, safeLastFragment);
                }
            })).show(true);
        } else {
            if (tL_error != null) {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text);
            } else {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.getString(R.string.UnknownError);
            }
            of.createErrorBulletin(string).show(false);
        }
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$76(final BottomSheet[] bottomSheetArr, final int i, final long j, final TL_stars.StarGift starGift, final boolean z, final Theme.ResourcesProvider resourcesProvider, final ButtonWithCounterView buttonWithCounterView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$75(TLObject.this, bottomSheetArr, i, j, starGift, z, tL_error, resourcesProvider, buttonWithCounterView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$77(final ButtonWithCounterView buttonWithCounterView, ButtonWithCounterView buttonWithCounterView2, TLRPC.TL_messageActionStarGift tL_messageActionStarGift, int i, final int i2, final long j, final BottomSheet[] bottomSheetArr, final TL_stars.StarGift starGift, final Theme.ResourcesProvider resourcesProvider, View view) {
        if (buttonWithCounterView.isLoading() || buttonWithCounterView2.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.saveStarGift savestargift = new TL_stars.saveStarGift();
        final boolean z = tL_messageActionStarGift.saved;
        savestargift.unsave = z;
        savestargift.msg_id = i;
        savestargift.user_id = MessagesController.getInstance(i2).getInputUser(j);
        ConnectionsManager.getInstance(i2).sendRequest(savestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda56
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showActionGiftSheet$76(bottomSheetArr, i2, j, starGift, z, resourcesProvider, buttonWithCounterView, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$78(StarsIntroActivity starsIntroActivity, TLRPC.TL_messageActionStarGift tL_messageActionStarGift) {
        BulletinFactory.of(starsIntroActivity).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) tL_messageActionStarGift.convert_stars)).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$79(ButtonWithCounterView buttonWithCounterView, TLObject tLObject, BottomSheet[] bottomSheetArr, int i, long j, long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider) {
        BulletinFactory of;
        String string;
        buttonWithCounterView.setLoading(false);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            if (tL_error != null) {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text);
            } else {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.getString(R.string.UnknownError);
            }
            of.createErrorBulletin(string).show(false);
            return;
        }
        bottomSheetArr[0].dismiss();
        StarsController.GiftsList profileGiftsList = StarsController.getInstance(i).getProfileGiftsList(j, false);
        if (profileGiftsList != null) {
            profileGiftsList.invalidate();
        }
        TLRPC.UserFull userFull = MessagesController.getInstance(i).getUserFull(j2);
        if (userFull != null) {
            int max = Math.max(0, userFull.stargifts_count - 1);
            userFull.stargifts_count = max;
            if (max <= 0) {
                userFull.flags2 &= -257;
            }
        }
        StarsController.getInstance(i).invalidateBalance();
        StarsController.getInstance(i).invalidateTransactions(true);
        if (safeLastFragment instanceof StarsIntroActivity) {
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) tL_messageActionStarGift.convert_stars)).show(true);
            return;
        }
        final StarsIntroActivity starsIntroActivity = new StarsIntroActivity();
        starsIntroActivity.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda93
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$78(StarsIntroActivity.this, tL_messageActionStarGift);
            }
        });
        safeLastFragment.presentFragment(starsIntroActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$80(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final long j, final long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$79(ButtonWithCounterView.this, tLObject, bottomSheetArr, i, j, j2, tL_messageActionStarGift, tL_error, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$81(final ButtonWithCounterView buttonWithCounterView, int i, final int i2, final long j, final BottomSheet[] bottomSheetArr, final long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i3) {
        buttonWithCounterView.setLoading(true);
        TL_stars.convertStarGift convertstargift = new TL_stars.convertStarGift();
        convertstargift.msg_id = i;
        convertstargift.user_id = MessagesController.getInstance(i2).getInputUser(j);
        ConnectionsManager.getInstance(i2).sendRequest(convertstargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda72
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showActionGiftSheet$80(ButtonWithCounterView.this, bottomSheetArr, i2, j, j2, tL_messageActionStarGift, resourcesProvider, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$82(ButtonWithCounterView buttonWithCounterView, final ButtonWithCounterView buttonWithCounterView2, Context context, final Theme.ResourcesProvider resourcesProvider, TLRPC.User user, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final int i, final int i2, final long j, final BottomSheet[] bottomSheetArr, final long j2, View view) {
        if (buttonWithCounterView.isLoading() || buttonWithCounterView2.isLoading()) {
            return;
        }
        new AlertDialog.Builder(context, resourcesProvider).setTitle(LocaleController.getString(R.string.Gift2ConvertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2ConvertText, UserObject.getForcedFirstName(user), LocaleController.formatPluralStringComma("Gift2ConvertStars", (int) tL_messageActionStarGift.convert_stars)))).setPositiveButton(LocaleController.getString(R.string.Gift2ConvertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda57
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                StarsIntroActivity.lambda$showActionGiftSheet$81(ButtonWithCounterView.this, i, i2, j, bottomSheetArr, j2, tL_messageActionStarGift, resourcesProvider, dialogInterface, i3);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$54(BottomSheet[] bottomSheetArr, long j) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        safeLastFragment.presentFragment(ChatActivity.of(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$55(BottomSheet[] bottomSheetArr, long j, TL_stories.Boost boost) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        safeLastFragment.presentFragment(ChatActivity.of(j, boost.giveaway_msg_id));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$56(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$57(BottomSheet[] bottomSheetArr, View view) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$83(Context context) {
        new ExplainStarsSheet(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$84(BottomSheet[] bottomSheetArr, long j, TL_stars.UserStarGift userStarGift, long j2) {
        BaseFragment profileActivity;
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null || UserObject.isService(j)) {
            return;
        }
        if ((userStarGift.flags & 8) != 0) {
            profileActivity = ChatActivity.of(j, userStarGift.msg_id);
        } else {
            Bundle bundle = new Bundle();
            if (j > 0) {
                bundle.putLong("user_id", j);
                if (j == j2) {
                    bundle.putBoolean("my_profile", true);
                    bundle.putBoolean("open_gifts", true);
                }
            } else {
                bundle.putLong("chat_id", -j);
            }
            profileActivity = new ProfileActivity(bundle);
        }
        safeLastFragment.presentFragment(profileActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$85(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$86(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$87(TLObject tLObject, BottomSheet[] bottomSheetArr, final int i, long j, TL_stars.StarGift starGift, boolean z, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider, ButtonWithCounterView buttonWithCounterView) {
        BulletinFactory of;
        String string;
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            bottomSheetArr[0].dismiss();
            StarsController.GiftsList profileGiftsList = StarsController.getInstance(i).getProfileGiftsList(j, false);
            if (profileGiftsList != null) {
                profileGiftsList.invalidate();
            }
            BulletinFactory.of(safeLastFragment).createEmojiBulletin(starGift.sticker, LocaleController.getString(z ? R.string.Gift2MadePrivateTitle : R.string.Gift2MadePublicTitle), AndroidUtilities.replaceSingleTag(LocaleController.getString(z ? R.string.Gift2MadePrivate : R.string.Gift2MadePublic), safeLastFragment instanceof ProfileActivity ? null : new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda92
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showGiftSheet$86(i, safeLastFragment);
                }
            })).show(true);
        } else {
            if (tL_error != null) {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text);
            } else {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.getString(R.string.UnknownError);
            }
            of.createErrorBulletin(string).show(false);
        }
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$88(final BottomSheet[] bottomSheetArr, final int i, final long j, final TL_stars.StarGift starGift, final boolean z, final Theme.ResourcesProvider resourcesProvider, final ButtonWithCounterView buttonWithCounterView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$87(TLObject.this, bottomSheetArr, i, j, starGift, z, tL_error, resourcesProvider, buttonWithCounterView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$89(final ButtonWithCounterView buttonWithCounterView, ButtonWithCounterView buttonWithCounterView2, TL_stars.UserStarGift userStarGift, final int i, final BottomSheet[] bottomSheetArr, final long j, final TL_stars.StarGift starGift, final Theme.ResourcesProvider resourcesProvider, View view) {
        if (buttonWithCounterView.isLoading() || buttonWithCounterView2.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.saveStarGift savestargift = new TL_stars.saveStarGift();
        final boolean z = !userStarGift.unsaved;
        savestargift.unsave = z;
        savestargift.msg_id = userStarGift.msg_id;
        savestargift.user_id = MessagesController.getInstance(i).getInputUser(userStarGift.from_id);
        ConnectionsManager.getInstance(i).sendRequest(savestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda76
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showGiftSheet$88(bottomSheetArr, i, j, starGift, z, resourcesProvider, buttonWithCounterView, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$90(StarsIntroActivity starsIntroActivity, TL_stars.UserStarGift userStarGift) {
        BulletinFactory.of(starsIntroActivity).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) userStarGift.convert_stars)).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$91(ButtonWithCounterView buttonWithCounterView, TLObject tLObject, BottomSheet[] bottomSheetArr, int i, long j, long j2, final TL_stars.UserStarGift userStarGift, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider) {
        BulletinFactory of;
        String string;
        buttonWithCounterView.setLoading(false);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            if (tL_error != null) {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text);
            } else {
                of = BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider);
                string = LocaleController.getString(R.string.UnknownError);
            }
            of.createErrorBulletin(string).show(false);
            return;
        }
        bottomSheetArr[0].dismiss();
        StarsController.GiftsList profileGiftsList = StarsController.getInstance(i).getProfileGiftsList(j, false);
        if (profileGiftsList != null) {
            profileGiftsList.invalidate();
        }
        TLRPC.UserFull userFull = MessagesController.getInstance(i).getUserFull(j2);
        if (userFull != null) {
            int max = Math.max(0, userFull.stargifts_count - 1);
            userFull.stargifts_count = max;
            if (max <= 0) {
                userFull.flags2 &= -257;
            }
        }
        StarsController.getInstance(i).invalidateBalance();
        StarsController.getInstance(i).invalidateTransactions(true);
        if (safeLastFragment instanceof StarsIntroActivity) {
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) userStarGift.convert_stars)).show(true);
            return;
        }
        final StarsIntroActivity starsIntroActivity = new StarsIntroActivity();
        starsIntroActivity.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$90(StarsIntroActivity.this, userStarGift);
            }
        });
        safeLastFragment.presentFragment(starsIntroActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$92(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final long j, final long j2, final TL_stars.UserStarGift userStarGift, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda91
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$91(ButtonWithCounterView.this, tLObject, bottomSheetArr, i, j, j2, userStarGift, tL_error, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$93(final ButtonWithCounterView buttonWithCounterView, final TL_stars.UserStarGift userStarGift, final int i, final BottomSheet[] bottomSheetArr, final long j, final long j2, final Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i2) {
        buttonWithCounterView.setLoading(true);
        TL_stars.convertStarGift convertstargift = new TL_stars.convertStarGift();
        convertstargift.msg_id = userStarGift.msg_id;
        convertstargift.user_id = MessagesController.getInstance(i).getInputUser(userStarGift.from_id);
        ConnectionsManager.getInstance(i).sendRequest(convertstargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda83
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showGiftSheet$92(ButtonWithCounterView.this, bottomSheetArr, i, j, j2, userStarGift, resourcesProvider, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$94(ButtonWithCounterView buttonWithCounterView, final ButtonWithCounterView buttonWithCounterView2, Context context, final Theme.ResourcesProvider resourcesProvider, TLRPC.User user, long j, final TL_stars.UserStarGift userStarGift, final int i, final BottomSheet[] bottomSheetArr, final long j2, final long j3, View view) {
        if (buttonWithCounterView.isLoading() || buttonWithCounterView2.isLoading()) {
            return;
        }
        new AlertDialog.Builder(context, resourcesProvider).setTitle(LocaleController.getString(R.string.Gift2ConvertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2ConvertText, (user == null || UserObject.isService(j)) ? LocaleController.getString(R.string.StarsTransactionHidden) : UserObject.getForcedFirstName(user), LocaleController.formatPluralStringComma("Gift2ConvertStars", (int) userStarGift.convert_stars)))).setPositiveButton(LocaleController.getString(R.string.Gift2ConvertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda75
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                StarsIntroActivity.lambda$showGiftSheet$93(ButtonWithCounterView.this, userStarGift, i, bottomSheetArr, j2, j3, resourcesProvider, dialogInterface, i2);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$59(OutlineTextContainerView outlineTextContainerView, EditTextBoldCursor editTextBoldCursor, View view, boolean z) {
        outlineTextContainerView.animateSelection(z, !TextUtils.isEmpty(editTextBoldCursor.getText()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$60(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.PaidContentInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$61(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showMediaPriceSheet$62(boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            if (zArr[0]) {
                return true;
            }
            if (callback2 != null) {
                zArr[0] = true;
                buttonWithCounterView.setLoading(true);
                callback2.run(Long.valueOf(Long.parseLong(editTextBoldCursor.getText().toString())), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda58
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showMediaPriceSheet$61(EditTextBoldCursor.this, bottomSheetArr);
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
    public static /* synthetic */ void lambda$showMediaPriceSheet$63(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$64(boolean[] zArr, Utilities.Callback2 callback2, final EditTextBoldCursor editTextBoldCursor, ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 == null) {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            bottomSheetArr[0].dismiss();
            return;
        }
        String obj = editTextBoldCursor.getText().toString();
        zArr[0] = true;
        buttonWithCounterView.setLoading(true);
        callback2.run(Long.valueOf(TextUtils.isEmpty(obj) ? 0L : Long.parseLong(obj)), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$63(EditTextBoldCursor.this, bottomSheetArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$65(boolean[] zArr, EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        zArr[0] = false;
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$66(final boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 == null) {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            bottomSheetArr[0].dismiss();
            return;
        }
        zArr[0] = true;
        buttonWithCounterView.setLoading(true);
        callback2.run(0L, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$65(zArr, editTextBoldCursor, bottomSheetArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$69(BottomSheet[] bottomSheetArr, final EditTextBoldCursor editTextBoldCursor) {
        bottomSheetArr[0].setFocusable(true);
        editTextBoldCursor.requestFocus();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda67
            @Override // java.lang.Runnable
            public final void run() {
                AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$37(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$38(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, long j) {
        buttonWithCounterView.setLoading(false);
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        StarsController.getInstance(i).invalidateSubscriptions(true);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        safeLastFragment.presentFragment(ChatActivity.of(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$39(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$38(ButtonWithCounterView.this, bottomSheetArr, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$40(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final long j) {
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_fulfillStarsSubscription tL_fulfillStarsSubscription = new TL_stars.TL_fulfillStarsSubscription();
        tL_fulfillStarsSubscription.subscription_id = starsSubscription.id;
        tL_fulfillStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        ConnectionsManager.getInstance(i).sendRequest(tL_fulfillStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda87
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$39(ButtonWithCounterView.this, bottomSheetArr, i, j, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$41(final ButtonWithCounterView buttonWithCounterView, final int i, final TL_stars.StarsSubscription starsSubscription, final BottomSheet[] bottomSheetArr, final long j, Context context, Theme.ResourcesProvider resourcesProvider, TLRPC.Chat chat, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        StarsController starsController = StarsController.getInstance(i);
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$40(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, j);
            }
        };
        if (starsController.balance < starsSubscription.pricing.amount) {
            new StarsNeededSheet(context, resourcesProvider, starsSubscription.pricing.amount, 2, chat == null ? "" : chat.title, runnable).show();
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$42(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, TLRPC.Chat chat) {
        buttonWithCounterView.setLoading(false);
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        StarsController.getInstance(i).invalidateSubscriptions(true);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createUsersBulletin(Collections.singletonList(chat), LocaleController.getString(R.string.StarsSubscriptionRenewedToast), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StarsSubscriptionRenewedToastText, chat == null ? "" : chat.title))).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$43(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final TLRPC.Chat chat, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$42(ButtonWithCounterView.this, bottomSheetArr, i, chat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$44(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final TLRPC.Chat chat, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_changeStarsSubscription tL_changeStarsSubscription = new TL_stars.TL_changeStarsSubscription();
        tL_changeStarsSubscription.canceled = Boolean.FALSE;
        tL_changeStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        tL_changeStarsSubscription.subscription_id = starsSubscription.id;
        ConnectionsManager.getInstance(i).sendRequest(tL_changeStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda74
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$43(ButtonWithCounterView.this, bottomSheetArr, i, chat, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$45(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, TLRPC.Chat chat, TL_stars.StarsSubscription starsSubscription) {
        buttonWithCounterView.setLoading(false);
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        StarsController.getInstance(i).invalidateSubscriptions(true);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createUsersBulletin(Collections.singletonList(chat), LocaleController.getString(R.string.StarsSubscriptionCancelledToast), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StarsSubscriptionCancelledToastText, LocaleController.formatDateChat(starsSubscription.until_date)))).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$46(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final TLRPC.Chat chat, final TL_stars.StarsSubscription starsSubscription, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$45(ButtonWithCounterView.this, bottomSheetArr, i, chat, starsSubscription);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$47(final ButtonWithCounterView buttonWithCounterView, final TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final TLRPC.Chat chat, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_changeStarsSubscription tL_changeStarsSubscription = new TL_stars.TL_changeStarsSubscription();
        tL_changeStarsSubscription.canceled = Boolean.TRUE;
        tL_changeStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        tL_changeStarsSubscription.subscription_id = starsSubscription.id;
        ConnectionsManager.getInstance(i).sendRequest(tL_changeStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda78
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$46(ButtonWithCounterView.this, bottomSheetArr, i, chat, starsSubscription, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$48(BaseFragment baseFragment, long j, TLRPC.Chat chat) {
        BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.stars_send, LocaleController.getString(R.string.StarsSubscriptionCompleted), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsSubscriptionCompletedText", (int) j, chat.title))).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$49(Long l, int i, final long j) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        final ChatActivity of = ChatActivity.of(l.longValue());
        safeLastFragment.presentFragment(of);
        final TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-l.longValue()));
        if (chat != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showSubscriptionSheet$48(BaseFragment.this, j, chat);
                }
            }, 250L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$50(final int i, final long j, String str, final Long l) {
        if (!"paid".equals(str) || l.longValue() == 0) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$49(l, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$51(ButtonWithCounterView buttonWithCounterView, TLObject tLObject, BottomSheet[] bottomSheetArr, Theme.ResourcesProvider resourcesProvider, final int i, TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite) {
        buttonWithCounterView.setLoading(false);
        if (!(tLObject instanceof TLRPC.ChatInvite)) {
            BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.LinkHashExpired)).show(false);
            return;
        }
        TLRPC.ChatInvite chatInvite = (TLRPC.ChatInvite) tLObject;
        TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = chatInvite.subscription_pricing;
        if (tL_starsSubscriptionPricing == null) {
            BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.UnknownError)).show(false);
            return;
        }
        final long j = tL_starsSubscriptionPricing.amount;
        StarsController.getInstance(i).subscribeTo(tL_messages_checkChatInvite.hash, chatInvite, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda94
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                StarsIntroActivity.lambda$showSubscriptionSheet$50(i, j, (String) obj, (Long) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$52(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final Theme.ResourcesProvider resourcesProvider, final int i, final TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$51(ButtonWithCounterView.this, tLObject, bottomSheetArr, resourcesProvider, i, tL_messages_checkChatInvite);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$53(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final Theme.ResourcesProvider resourcesProvider, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        final TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite = new TLRPC.TL_messages_checkChatInvite();
        tL_messages_checkChatInvite.hash = starsSubscription.chat_invite_hash;
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda79
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$52(ButtonWithCounterView.this, bottomSheetArr, resourcesProvider, i, tL_messages_checkChatInvite, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$20(boolean z, long j, TL_stars.StarsTransaction starsTransaction, int i, Theme.ResourcesProvider resourcesProvider, final BackupImageView backupImageView, final LinearLayout linearLayout, View view) {
        final long peerDialogId = z ? j : DialogObject.getPeerDialogId(starsTransaction.peer.peer);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < starsTransaction.extended_media.size(); i2++) {
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.id = starsTransaction.msg_id;
            tL_message.dialog_id = peerDialogId;
            TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
            tL_message.from_id = tL_peerChannel;
            long j2 = -peerDialogId;
            tL_peerChannel.channel_id = j2;
            TLRPC.TL_peerChannel tL_peerChannel2 = new TLRPC.TL_peerChannel();
            tL_message.peer_id = tL_peerChannel2;
            tL_peerChannel2.channel_id = j2;
            tL_message.date = starsTransaction.date;
            tL_message.flags |= 512;
            tL_message.media = starsTransaction.extended_media.get(i2);
            tL_message.noforwards = true;
            arrayList.add(new MessageObject(i, tL_message, false, false));
        }
        if (arrayList.isEmpty()) {
            return;
        }
        PhotoViewer.getInstance().setParentActivity(LaunchActivity.getLastFragment(), resourcesProvider);
        PhotoViewer.getInstance().openPhoto(arrayList, 0, peerDialogId, 0L, 0L, new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Stars.StarsIntroActivity.6
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean forceAllInGroup() {
                return true;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC.FileLocation fileLocation, int i3, boolean z2) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$21(Context context, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet;
        BaseFragment baseFragment;
        StarAppsSheet starAppsSheet = new StarAppsSheet(context);
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(bottomSheetArr[0].attachedFragment) && (bottomSheet = bottomSheetArr[0]) != null && (baseFragment = bottomSheet.attachedFragment) != null) {
            starAppsSheet.makeAttached(baseFragment);
        }
        starAppsSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$22(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$23(BottomSheet[] bottomSheetArr, int i) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
            bundle.putBoolean("my_profile", true);
            bundle.putBoolean("open_gifts", true);
            safeLastFragment.presentFragment(new ProfileActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$24(BottomSheet[] bottomSheetArr, int i) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
            bundle.putBoolean("my_profile", true);
            bundle.putBoolean("open_gifts", true);
            safeLastFragment.presentFragment(new ProfileActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$25(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$26(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$27(BottomSheet[] bottomSheetArr, int i) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
            bundle.putBoolean("my_profile", true);
            safeLastFragment.presentFragment(new ProfileActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$28(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$29(BottomSheet[] bottomSheetArr, long j, Context context) {
        bottomSheetArr[0].dismiss();
        if (UserObject.isService(j)) {
            Browser.openUrl(context, LocaleController.getString(R.string.StarsTransactionUnknownLink));
            return;
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment(ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$30(BottomSheet[] bottomSheetArr, long j, Context context) {
        bottomSheetArr[0].dismiss();
        if (UserObject.isService(j)) {
            Browser.openUrl(context, LocaleController.getString(R.string.StarsTransactionUnknownLink));
            return;
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment(ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$31(BottomSheet[] bottomSheetArr, long j, TL_stars.StarsTransaction starsTransaction) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            bundle.putInt("message_id", starsTransaction.msg_id);
            safeLastFragment.presentFragment(new ChatActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$33(TL_stars.StarsTransaction starsTransaction, BottomSheet[] bottomSheetArr, Theme.ResourcesProvider resourcesProvider, View view) {
        AndroidUtilities.addToClipboard(starsTransaction.id);
        BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createSimpleBulletin(R.raw.copy, LocaleController.getString(R.string.StarsTransactionIDCopied)).show(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$34(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$35(Context context, TL_stars.StarsTransaction starsTransaction, View view) {
        Browser.openUrl(context, starsTransaction.transaction_url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$36(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    public static StarParticlesView makeParticlesView(Context context, int i, int i2) {
        return new 2(context, i, i2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x029d  */
    /* JADX WARN: Type inference failed for: r4v3, types: [android.view.View, android.view.ViewGroup] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet openConfirmPurchaseSheet(Context context, Theme.ResourcesProvider resourcesProvider, int i, MessageObject messageObject, long j, String str, long j2, TLRPC.WebDocument webDocument, final Utilities.Callback callback, final Runnable runnable) {
        FrameLayout frameLayout;
        LinearLayout linearLayout;
        int i2;
        BackupImageView backupImageView;
        String formatPluralStringComma;
        TLRPC.Message message;
        String str2;
        boolean z;
        TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia;
        int i3;
        int i4;
        int i5;
        String formatPluralString;
        String str3;
        char c;
        String formatPluralString2;
        char c2;
        String formatPluralString3;
        boolean z2;
        TLRPC.User user;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        TLRPC.Message message2;
        BackupImageView backupImageView2;
        FrameLayout frameLayout2;
        int i6;
        ImageLocation imageLocation;
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        FrameLayout frameLayout3 = new FrameLayout(context);
        frameLayout3.addView(makeParticlesView(context, 40, 0), LayoutHelper.createFrame(-1, -1.0f));
        if (messageObject == null || (message2 = messageObject.messageOwner) == null || !(message2.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
            frameLayout = frameLayout3;
            linearLayout = linearLayout2;
            i2 = 17;
            if (webDocument == null) {
                backupImageView = new BackupImageView(context);
                backupImageView.setRoundRadius(AndroidUtilities.dp(80.0f));
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setInfo(user2);
                backupImageView.setForUserOrChat(user2, avatarDrawable);
            } else {
                backupImageView = new BackupImageView(context);
                backupImageView.setRoundRadius(AndroidUtilities.dp(80.0f));
                backupImageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(webDocument)), "80_80", (Drawable) null, 0, (Object) null);
            }
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(80, 80, 17));
        } else {
            BackupImageView backupImageView3 = new BackupImageView(context, context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.4
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
            backupImageView3.setRoundRadius(AndroidUtilities.dp(24.0f));
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia2 = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            if (tL_messageMediaPaidMedia2.extended_media.isEmpty()) {
                backupImageView2 = backupImageView3;
                frameLayout2 = frameLayout3;
                linearLayout = linearLayout2;
                i6 = 80;
            } else {
                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia2.extended_media.get(0);
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                    imageLocation = ImageLocation.getForObject(((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia).thumb, messageObject.messageOwner);
                } else {
                    if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                        TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                            imageLocation = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(messageMedia.photo.sizes, AndroidUtilities.dp(80.0f), true), messageMedia.photo);
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                            imageLocation = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(messageMedia.document.thumbs, AndroidUtilities.dp(80.0f), true), messageMedia.document);
                        }
                    }
                    imageLocation = null;
                }
                backupImageView2 = backupImageView3;
                i6 = 80;
                frameLayout2 = frameLayout3;
                linearLayout = linearLayout2;
                backupImageView3.setImage(imageLocation, "80_80_b2", (ImageLocation) null, (String) null, (Drawable) null, messageObject);
            }
            i2 = 17;
            frameLayout = frameLayout2;
            frameLayout.addView(backupImageView2, LayoutHelper.createFrame(i6, i6, 17));
        }
        final StarsBalanceView starsBalanceView = new StarsBalanceView(context, i);
        ScaleStateListAnimator.apply(starsBalanceView);
        starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$5(StarsIntroActivity.StarsBalanceView.this, view);
            }
        });
        frameLayout.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, -8.0f, 0.0f));
        LinearLayout linearLayout3 = linearLayout;
        linearLayout3.addView((View) frameLayout, LayoutHelper.createLinear(-1, 117, 7));
        TextView textView = new TextView(context);
        int i7 = 1;
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        int i8 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i8, resourcesProvider));
        textView.setText(LocaleController.getString(R.string.StarsConfirmPurchaseTitle));
        textView.setGravity(i2);
        linearLayout3.addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(Theme.getColor(i8, resourcesProvider));
        if (messageObject == null || (message = messageObject.messageOwner) == null || !(message.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
            formatPluralStringComma = LocaleController.formatPluralStringComma("StarsConfirmPurchaseText", (int) j2, str, UserObject.getUserName(user2));
        } else {
            long dialogId = messageObject.getDialogId();
            TLRPC.Message message3 = messageObject.messageOwner;
            if (message3 != null && (messageFwdHeader = message3.fwd_from) != null && (peer = messageFwdHeader.from_id) != null) {
                dialogId = DialogObject.getPeerDialogId(peer);
            }
            if (dialogId < 0 && messageObject.getFromChatId() > 0 && (user = MessagesController.getInstance(i).getUser(Long.valueOf(messageObject.getFromChatId()))) != null && user.bot) {
                dialogId = user.id;
            }
            if (dialogId >= 0) {
                TLRPC.User user3 = MessagesController.getInstance(i).getUser(Long.valueOf(dialogId));
                str2 = UserObject.getUserName(user3);
                if (user3 != null && user3.bot) {
                    z = true;
                    tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
                    i4 = 0;
                    int i9 = 0;
                    for (i3 = 0; i3 < tL_messageMediaPaidMedia.extended_media.size(); i3++) {
                        TLRPC.MessageExtendedMedia messageExtendedMedia2 = tL_messageMediaPaidMedia.extended_media.get(i3);
                        if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMediaPreview) {
                            if ((((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia2).flags & 4) != 0) {
                                z2 = true;
                            }
                            z2 = false;
                        } else {
                            if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMedia) {
                                z2 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia2).media instanceof TLRPC.TL_messageMediaDocument;
                            }
                            z2 = false;
                        }
                        if (z2) {
                            i4++;
                        } else {
                            i9++;
                        }
                    }
                    if (i4 != 0) {
                        str3 = z ? "StarsConfirmPurchaseMediaBotOne2" : "StarsConfirmPurchaseMediaOne2";
                        int i10 = (int) j2;
                        if (i9 == 1) {
                            formatPluralString3 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto);
                            c2 = 0;
                        } else {
                            c2 = 0;
                            formatPluralString3 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i9, new Object[0]);
                        }
                        Object[] objArr = new Object[2];
                        objArr[c2] = formatPluralString3;
                        objArr[1] = str2;
                        formatPluralStringComma = LocaleController.formatPluralString(str3, i10, objArr);
                    } else if (i9 == 0) {
                        str3 = z ? "StarsConfirmPurchaseMediaBotOne2" : "StarsConfirmPurchaseMediaOne2";
                        int i11 = (int) j2;
                        if (i4 == 1) {
                            formatPluralString2 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo);
                            c = 0;
                        } else {
                            c = 0;
                            formatPluralString2 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i4, new Object[0]);
                        }
                        Object[] objArr2 = new Object[2];
                        objArr2[c] = formatPluralString2;
                        objArr2[1] = str2;
                        formatPluralStringComma = LocaleController.formatPluralString(str3, i11, objArr2);
                    } else {
                        String str4 = z ? "StarsConfirmPurchaseMediaBotTwo2" : "StarsConfirmPurchaseMediaTwo2";
                        int i12 = (int) j2;
                        if (i9 == 1) {
                            formatPluralString = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto);
                            i5 = 0;
                        } else {
                            i5 = 0;
                            formatPluralString = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i9, new Object[0]);
                            i7 = 1;
                        }
                        String string = i4 == i7 ? LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo) : LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i4, new Object[i5]);
                        Object[] objArr3 = new Object[3];
                        objArr3[i5] = formatPluralString;
                        objArr3[i7] = string;
                        objArr3[2] = str2;
                        formatPluralStringComma = LocaleController.formatPluralString(str4, i12, objArr3);
                    }
                }
            } else {
                TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-dialogId));
                str2 = chat == null ? "" : chat.title;
            }
            z = false;
            tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            i4 = 0;
            int i92 = 0;
            while (i3 < tL_messageMediaPaidMedia.extended_media.size()) {
            }
            if (i4 != 0) {
            }
        }
        textView2.setText(AndroidUtilities.replaceTags(formatPluralStringComma));
        textView2.setMaxWidth(HintView2.cutInFancyHalf(textView2.getText(), textView2.getPaint()));
        textView2.setGravity(17);
        linearLayout3.addView(textView2, LayoutHelper.createLinear(-2, -2, 1, 0, 6, 0, 24));
        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(replaceStars(AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("StarsConfirmPurchaseButton", (int) j2))), false);
        linearLayout3.addView(buttonWithCounterView, LayoutHelper.createFrame(-1, 48.0f));
        builder.setCustomView(linearLayout3);
        final BottomSheet create = builder.create();
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$8(Utilities.Callback.this, create, buttonWithCounterView, view);
            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda25
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$9(runnable, dialogInterface);
            }
        });
        create.fixNavigationBar();
        create.show();
        return create;
    }

    public static BottomSheet openStarsChannelInviteSheet(final Context context, Theme.ResourcesProvider resourcesProvider, int i, TLRPC.ChatInvite chatInvite, final Utilities.Callback callback, final Runnable runnable) {
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(makeParticlesView(context, 40, 0), LayoutHelper.createFrame(-1, -1.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(80.0f));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setPeerColor(chatInvite.color);
        avatarDrawable.setText(chatInvite.title);
        TLRPC.Photo photo = chatInvite.photo;
        if (photo != null) {
            backupImageView.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.dp(80.0f)), chatInvite.photo), "80_80", avatarDrawable, chatInvite);
        } else {
            backupImageView.setImageDrawable(avatarDrawable);
        }
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(80, 80, 17));
        Drawable drawable = context.getResources().getDrawable(R.drawable.star_small_outline);
        int i2 = Theme.key_dialogBackground;
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i2, resourcesProvider), PorterDuff.Mode.SRC_IN));
        Drawable drawable2 = context.getResources().getDrawable(R.drawable.star_small_inner);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawable);
        frameLayout.addView(imageView, LayoutHelper.createFrame(26, 26, 17));
        imageView.setTranslationX(AndroidUtilities.dp(26.0f));
        imageView.setTranslationY(AndroidUtilities.dp(26.0f));
        imageView.setScaleX(1.2f);
        imageView.setScaleY(1.2f);
        ImageView imageView2 = new ImageView(context);
        imageView2.setImageDrawable(drawable2);
        frameLayout.addView(imageView2, LayoutHelper.createFrame(26, 26, 17));
        imageView2.setTranslationX(AndroidUtilities.dp(26.0f));
        imageView2.setTranslationY(AndroidUtilities.dp(26.0f));
        final StarsBalanceView starsBalanceView = new StarsBalanceView(context, i);
        ScaleStateListAnimator.apply(starsBalanceView);
        starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda68
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$10(StarsIntroActivity.StarsBalanceView.this, view);
            }
        });
        frameLayout.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, -8.0f, 0.0f));
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 117, 7));
        TextView textView = new TextView(context);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        int i3 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i3, resourcesProvider));
        textView.setText(LocaleController.getString(R.string.StarsSubscribeTitle));
        textView.setGravity(17);
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(Theme.getColor(i3, resourcesProvider));
        TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = chatInvite.subscription_pricing;
        int i4 = tL_starsSubscriptionPricing.period;
        if (i4 == 2592000) {
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsSubscribeText", (int) tL_starsSubscriptionPricing.amount, chatInvite.title)));
        } else {
            textView2.setText(AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsSubscribeTextTest", (int) tL_starsSubscriptionPricing.amount, chatInvite.title, i4 == 300 ? "5 minutes" : "a minute")));
        }
        textView2.setMaxWidth(HintView2.cutInFancyHalf(textView2.getText(), textView2.getPaint()));
        textView2.setGravity(17);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 1, 0, 6, 0, 22));
        if (!TextUtils.isEmpty(chatInvite.about)) {
            TextView textView3 = new TextView(context);
            textView3.setTextSize(1, 14.0f);
            textView3.setTextColor(Theme.getColor(i3, resourcesProvider));
            textView3.setText(Emoji.replaceEmoji(chatInvite.about, textView3.getPaint().getFontMetricsInt(), false));
            textView3.setGravity(17);
            linearLayout.addView(textView3, LayoutHelper.createLinear(-2, -2, 1, 0, 6, 0, 22));
        }
        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.StarsSubscribeButton), false);
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsSubscribeInfo), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$11(context);
            }
        }));
        linksTextView.setGravity(17);
        linksTextView.setTextSize(1, 13.0f);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 49, 14, 14, 14, 6));
        builder.setCustomView(linearLayout);
        final BottomSheet create = builder.create();
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda70
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$14(Utilities.Callback.this, create, buttonWithCounterView, view);
            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda71
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$15(runnable, dialogInterface);
            }
        });
        create.fixNavigationBar(Theme.getColor(i2, resourcesProvider));
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet() && safeLastFragment != null && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
            create.makeAttached(safeLastFragment);
        }
        create.show();
        return create;
    }

    public static SpannableStringBuilder replaceStars(CharSequence charSequence) {
        return replaceStars(charSequence, 1.13f);
    }

    public static SpannableStringBuilder replaceStars(CharSequence charSequence, float f) {
        if (charSequence == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = !(charSequence instanceof SpannableStringBuilder) ? new SpannableStringBuilder(charSequence) : (SpannableStringBuilder) charSequence;
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
        ColoredImageSpan coloredImageSpan;
        if (charSequence == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = !(charSequence instanceof SpannableStringBuilder) ? new SpannableStringBuilder(charSequence) : (SpannableStringBuilder) charSequence;
        if (coloredImageSpanArr == null || (coloredImageSpan = coloredImageSpanArr[0]) == null) {
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
        if (charSequence == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = !(charSequence instanceof SpannableStringBuilder) ? new SpannableStringBuilder(charSequence) : (SpannableStringBuilder) charSequence;
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

    public static Runnable setGiftImage(View view, final ImageReceiver imageReceiver, final int i) {
        final boolean[] zArr = new boolean[1];
        final int currentAccount = imageReceiver.getCurrentAccount();
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$setGiftImage$16(currentAccount, i, imageReceiver, zArr);
            }
        };
        runnable.run();
        final Runnable listen = NotificationCenter.getInstance(currentAccount).listen(view, NotificationCenter.didUpdatePremiumGiftStickers, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda19
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                Object[] objArr = (Object[]) obj;
                runnable.run();
            }
        });
        final Runnable listen2 = NotificationCenter.getInstance(currentAccount).listen(view, NotificationCenter.diceStickersDidLoad, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda20
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                Object[] objArr = (Object[]) obj;
                runnable.run();
            }
        });
        return new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$setGiftImage$19(listen, listen2);
            }
        };
    }

    public static Runnable setGiftImage(View view, ImageReceiver imageReceiver, long j) {
        return setGiftImage(view, imageReceiver, j <= 1000 ? 2 : j < 2500 ? 3 : 4);
    }

    public static void setGiftImage(ImageReceiver imageReceiver, TL_stars.StarGift starGift, int i) {
        TLRPC.Document document;
        if (starGift == null || (document = starGift.sticker) == null) {
            imageReceiver.clearImage();
            return;
        }
        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, i);
        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(starGift.sticker.thumbs, Theme.key_windowBackgroundGray, 0.35f);
        imageReceiver.setImage(ImageLocation.getForDocument(starGift.sticker), i + "_" + i, ImageLocation.getForDocument(closestPhotoSizeWithSize, starGift.sticker), i + "_" + i, svgThumb, 0L, null, starGift, 0);
    }

    public static BottomSheet showActionGiftSheet(final Context context, final int i, final long j, boolean z, int i2, final int i3, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider) {
        final TL_stars.StarGift starGift;
        int i4;
        String formatPluralStringComma;
        if (tL_messageActionStarGift == null || context == null || (starGift = tL_messageActionStarGift.gift) == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        BackupImageView backupImageView = new BackupImageView(context);
        setGiftImage(backupImageView.getImageReceiver(), starGift, (int) NotificationCenter.audioRouteChanged);
        linearLayout.addView(backupImageView, LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, (int) NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
        TextView textView = new TextView(context);
        int i5 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i5, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(z ? R.string.Gift2TitleSent : R.string.Gift2TitleReceived));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 18.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(!z ? Theme.key_color_green : Theme.key_color_red, resourcesProvider));
        StringBuilder sb = new StringBuilder();
        sb.append(!z ? "" : "-");
        sb.append(LocaleController.formatNumber((int) Math.abs(z ? starGift.stars : tL_messageActionStarGift.convert_stars), ' '));
        sb.append(" ⭐️");
        textView2.setText(replaceStarsWithPlain(sb.toString(), 0.8f));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        final TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setTextColor(Theme.getColor(i5, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setGravity(17);
        int i6 = Theme.key_chat_messageLinkIn;
        linksTextView.setLinkTextColor(Theme.getColor(i6, resourcesProvider));
        linksTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        linksTextView.setDisablePaddingsOffsetY(true);
        if (!z) {
            i4 = i6;
            if (!tL_messageActionStarGift.saved || tL_messageActionStarGift.converted) {
                formatPluralStringComma = LocaleController.formatPluralStringComma(tL_messageActionStarGift.converted ? "Gift2InfoConverted" : "Gift2Info", (int) tL_messageActionStarGift.convert_stars);
            } else {
                formatPluralStringComma = LocaleController.getString(R.string.Gift2InfoPinned);
            }
        } else if (!tL_messageActionStarGift.saved || tL_messageActionStarGift.converted) {
            i4 = i6;
            formatPluralStringComma = LocaleController.formatPluralStringComma(tL_messageActionStarGift.converted ? "Gift2InfoOutConverted" : "Gift2InfoOut", (int) tL_messageActionStarGift.convert_stars, UserObject.getForcedFirstName(user));
        } else {
            formatPluralStringComma = LocaleController.formatString(R.string.Gift2InfoOutPinned, UserObject.getForcedFirstName(user));
            i4 = i6;
        }
        linksTextView.setText(TextUtils.concat(AndroidUtilities.replaceTags(formatPluralStringComma), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$70(context);
            }
        }), true)));
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 5, 5, 5, 4));
        if (tL_messageActionStarGift.name_hidden) {
            LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider));
            linksTextView2.setTextSize(1, 14.0f);
            linksTextView2.setGravity(17);
            linksTextView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linksTextView2.setLinkTextColor(Theme.getColor(i4, resourcesProvider));
            linksTextView2.setDisablePaddingsOffsetY(true);
            linksTextView2.setText(LocaleController.getString(R.string.Gift2SenderHidden));
            linearLayout.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 17, 5, 8, 5, 4));
        }
        TableView tableView = new TableView(context, resourcesProvider);
        final long clientUserId = UserConfig.getInstance(i).getClientUserId();
        long j2 = z ? clientUserId : j;
        long j3 = z ? j : clientUserId;
        final long j4 = j2;
        tableView.addRowUser(LocaleController.getString(R.string.Gift2From), i, j2, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$71(bottomSheetArr, j4, clientUserId);
            }
        });
        final long j5 = j3;
        tableView.addRowUser(LocaleController.getString(R.string.Gift2To), i, j3, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$72(bottomSheetArr, j5, clientUserId);
            }
        });
        long j6 = i2 * 1000;
        tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(j6)), LocaleController.getInstance().getFormatterDay().format(new Date(j6))));
        if (starGift.limited) {
            addAvailabilityRow(tableView, i, starGift, resourcesProvider);
        }
        TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageActionStarGift.message;
        if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
            TLRPC.TL_textWithEntities tL_textWithEntities2 = tL_messageActionStarGift.message;
            tableView.addFullRow(tL_textWithEntities2.text, tL_textWithEntities2.entities);
        }
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
        if (z || tL_messageActionStarGift.converted) {
            ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
            buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
            linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
            builder.setCustomView(linearLayout);
            BottomSheet create = builder.create();
            bottomSheetArr[0] = create;
            create.useBackgroundTopPadding = false;
            buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda49
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showActionGiftSheet$73(bottomSheetArr, view);
                }
            });
            bottomSheetArr[0].fixNavigationBar();
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                bottomSheetArr[0].makeAttached(safeLastFragment);
            }
            bottomSheetArr[0].show();
            return bottomSheetArr[0];
        }
        final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView2.setText(LocaleController.getString(tL_messageActionStarGift.saved ? R.string.Gift2ProfileMakeInvisible : R.string.Gift2ProfileMakeVisible), false);
        linearLayout.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48, 0.0f, 0.0f, 0.0f, 4.0f));
        final ButtonWithCounterView buttonWithCounterView3 = new ButtonWithCounterView(context, false, resourcesProvider);
        buttonWithCounterView3.setText(LocaleController.formatPluralStringComma("Gift2ToBalance", (int) tL_messageActionStarGift.convert_stars), false);
        linearLayout.addView(buttonWithCounterView3, LayoutHelper.createLinear(-1, 48));
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda47
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showActionGiftSheet$77(ButtonWithCounterView.this, buttonWithCounterView3, tL_messageActionStarGift, i3, i, j, bottomSheetArr, starGift, resourcesProvider, view);
            }
        });
        buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda48
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showActionGiftSheet$82(ButtonWithCounterView.this, buttonWithCounterView3, context, resourcesProvider, user, tL_messageActionStarGift, i3, i, j, bottomSheetArr, clientUserId, view);
            }
        });
        builder.setCustomView(linearLayout);
        BottomSheet create2 = builder.create();
        bottomSheetArr[0] = create2;
        create2.useBackgroundTopPadding = false;
        create2.fixNavigationBar();
        BaseFragment safeLastFragment2 = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment2)) {
            bottomSheetArr[0].makeAttached(safeLastFragment2);
        }
        bottomSheetArr[0].show();
        return bottomSheetArr[0];
    }

    public static BottomSheet showBoostsSheet(final Context context, int i, final long j, final TL_stories.Boost boost, Theme.ResourcesProvider resourcesProvider) {
        if (boost == null || context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(4.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setClipChildren(false);
        frameLayout.setClipToPadding(false);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 150, 7, 0, 0, 0, 10));
        StarParticlesView makeParticlesView = makeParticlesView(context, 70, 0);
        frameLayout.addView(makeParticlesView, LayoutHelper.createFrame(-1, -1.0f));
        final GLIconTextureView gLIconTextureView = new GLIconTextureView(context, 1, 2);
        GLIconRenderer gLIconRenderer = gLIconTextureView.mRenderer;
        gLIconRenderer.colorKey1 = Theme.key_starsGradient1;
        gLIconRenderer.colorKey2 = Theme.key_starsGradient2;
        gLIconRenderer.updateColors();
        gLIconTextureView.setStarParticlesView(makeParticlesView);
        frameLayout.addView(gLIconTextureView, LayoutHelper.createFrame(NotificationCenter.groupCallVisibilityChanged, 170.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
        gLIconTextureView.setPaused(false);
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.formatPluralStringSpaced("BoostStars", (int) boost.stars));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(20.0f), -6915073));
        textView2.setTextColor(-1);
        textView2.setTextSize(1, 11.33f);
        textView2.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(8.33f), 0);
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.bold());
        StringBuilder sb = new StringBuilder();
        sb.append("x");
        int i2 = boost.multiplier;
        if (i2 == 0) {
            i2 = 1;
        }
        sb.append(LocaleController.formatPluralStringSpaced("BoostingBoostsCount", i2));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(sb.toString());
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_boost_badge, 2);
        coloredImageSpan.translate(0.0f, AndroidUtilities.dp(0.66f));
        spannableStringBuilder.setSpan(coloredImageSpan, 0, 1, 33);
        textView2.setText(spannableStringBuilder);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-2, 20, 17, 20, 4, 20, 4));
        TableView tableView = new TableView(context, resourcesProvider);
        tableView.addRowUser(LocaleController.getString(R.string.BoostFrom), i, j, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showBoostsSheet$54(bottomSheetArr, j);
            }
        });
        tableView.addRow(LocaleController.getString(R.string.BoostGift), LocaleController.formatPluralString("BoostStars", (int) boost.stars, new Object[0]));
        if (boost.giveaway_msg_id != 0) {
            tableView.addRowLink(LocaleController.getString(R.string.BoostReason), LocaleController.getString(R.string.BoostReasonGiveaway), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showBoostsSheet$55(bottomSheetArr, j, boost);
                }
            });
        }
        String string = LocaleController.getString(R.string.BoostDate);
        int i3 = R.string.formatDateAtTime;
        tableView.addRow(string, LocaleController.formatString(i3, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(boost.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(boost.date * 1000))));
        tableView.addRow(LocaleController.getString(R.string.BoostUntil), LocaleController.formatString(i3, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(boost.expires * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(boost.expires * 1000))));
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 16.0f, 17.0f, 16.0f, 0.0f));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showBoostsSheet$56(context);
            }
        }));
        linksTextView.setGravity(17);
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 14.0f, 15.0f, 14.0f, 7.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda33
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showBoostsSheet$57(bottomSheetArr, view);
            }
        });
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 16.0f, 8.0f, 16.0f, 0.0f));
        builder.setCustomView(linearLayout);
        BottomSheet create = builder.create();
        bottomSheetArr[0] = create;
        create.useBackgroundTopPadding = false;
        create.fixNavigationBar();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
            bottomSheetArr[0].makeAttached(safeLastFragment);
        }
        gLIconTextureView.setPaused(false);
        bottomSheetArr[0].show();
        bottomSheetArr[0].setOnDismissListener(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                GLIconTextureView.this.setPaused(true);
            }
        });
        return bottomSheetArr[0];
    }

    public static BottomSheet showGiftSheet(final Context context, final int i, final long j, boolean z, final TL_stars.UserStarGift userStarGift, final Theme.ResourcesProvider resourcesProvider) {
        final TL_stars.StarGift starGift;
        if (userStarGift == null || context == null || (starGift = userStarGift.gift) == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        BackupImageView backupImageView = new BackupImageView(context);
        setGiftImage(backupImageView.getImageReceiver(), starGift, (int) NotificationCenter.audioRouteChanged);
        linearLayout.addView(backupImageView, LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, (int) NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
        TextView textView = new TextView(context);
        int i2 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i2, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(z ? R.string.Gift2TitleReceived : R.string.Gift2TitleProfile));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 18.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(Theme.key_color_green, resourcesProvider));
        textView2.setText(replaceStarsWithPlain(LocaleController.formatNumber((int) Math.abs(Math.max(userStarGift.gift.convert_stars, userStarGift.convert_stars)), ' ') + " ⭐️", 0.8f));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        if (z) {
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView.setTextColor(Theme.getColor(i2, resourcesProvider));
            linksTextView.setTextSize(1, 14.0f);
            linksTextView.setGravity(17);
            int i3 = Theme.key_chat_messageLinkIn;
            linksTextView.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
            linksTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linksTextView.setDisablePaddingsOffsetY(true);
            long j2 = userStarGift.convert_stars;
            linksTextView.setText(TextUtils.concat(AndroidUtilities.replaceTags(z ? LocaleController.formatPluralStringComma("Gift2Info", (int) j2) : LocaleController.formatPluralStringComma("Gift2InfoOut", (int) j2, UserObject.getForcedFirstName(user))), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showGiftSheet$83(context);
                }
            }), true)));
            linearLayout = linearLayout;
            linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 5, 5, 5, 4));
            LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider));
            linksTextView2.setTextSize(1, 14.0f);
            linksTextView2.setGravity(17);
            linksTextView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linksTextView2.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
            linksTextView2.setDisablePaddingsOffsetY(true);
            linksTextView2.setText(LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2ProfileVisible : R.string.Gift2ProfileInvisible));
            linearLayout.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 17, 5, 8, 5, 4));
        }
        TableView tableView = new TableView(context, resourcesProvider);
        final long clientUserId = UserConfig.getInstance(i).getClientUserId();
        final long j3 = ((userStarGift.flags & 2) == 0 || userStarGift.name_hidden) ? UserObject.ANONYMOUS : userStarGift.from_id;
        final TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(j3));
        LinearLayout linearLayout2 = linearLayout;
        tableView.addRowUser(LocaleController.getString(R.string.Gift2From), i, j3, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$84(bottomSheetArr, j3, userStarGift, clientUserId);
            }
        });
        tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
        if (starGift.limited) {
            addAvailabilityRow(tableView, i, starGift, resourcesProvider);
        }
        TLRPC.TL_textWithEntities tL_textWithEntities = userStarGift.message;
        if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
            TLRPC.TL_textWithEntities tL_textWithEntities2 = userStarGift.message;
            tableView.addFullRow(tL_textWithEntities2.text, tL_textWithEntities2.entities);
        }
        linearLayout2.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
        if (z) {
            int i4 = userStarGift.flags;
            if ((i4 & 8) != 0 && (i4 & 2) != 0) {
                final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
                buttonWithCounterView.setText(LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2ProfileMakeInvisible : R.string.Gift2ProfileMakeVisible), false);
                linearLayout2.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 0.0f, 0.0f, 0.0f, 4.0f));
                final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, false, resourcesProvider);
                buttonWithCounterView2.setText(LocaleController.formatPluralStringComma("Gift2ToBalance", (int) userStarGift.convert_stars), false);
                linearLayout2.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
                buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda53
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showGiftSheet$89(ButtonWithCounterView.this, buttonWithCounterView2, userStarGift, i, bottomSheetArr, j, starGift, resourcesProvider, view);
                    }
                });
                buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda54
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showGiftSheet$94(ButtonWithCounterView.this, buttonWithCounterView2, context, resourcesProvider, user2, j3, userStarGift, i, bottomSheetArr, j, clientUserId, view);
                    }
                });
                builder.setCustomView(linearLayout2);
                BottomSheet create = builder.create();
                bottomSheetArr[0] = create;
                create.useBackgroundTopPadding = false;
                create.fixNavigationBar();
                BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                    bottomSheetArr[0].makeAttached(safeLastFragment);
                }
                bottomSheetArr[0].show();
                return bottomSheetArr[0];
            }
        }
        ButtonWithCounterView buttonWithCounterView3 = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView3.setText(LocaleController.getString(R.string.OK), false);
        linearLayout2.addView(buttonWithCounterView3, LayoutHelper.createLinear(-1, 48));
        builder.setCustomView(linearLayout2);
        BottomSheet create2 = builder.create();
        bottomSheetArr[0] = create2;
        create2.useBackgroundTopPadding = false;
        buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda55
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showGiftSheet$85(bottomSheetArr, view);
            }
        });
        bottomSheetArr[0].fixNavigationBar();
        BaseFragment safeLastFragment2 = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment2)) {
            bottomSheetArr[0].makeAttached(safeLastFragment2);
        }
        bottomSheetArr[0].show();
        return bottomSheetArr[0];
    }

    public static BottomSheet showMediaPriceSheet(final Context context, final long j, final boolean z, final Utilities.Callback2 callback2, Theme.ResourcesProvider resourcesProvider) {
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
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
        editTextBoldCursor.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda35
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z2) {
                StarsIntroActivity.lambda$showMediaPriceSheet$59(OutlineTextContainerView.this, editTextBoldCursor, view, z2);
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
        final TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 16.0f);
        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        outlineTextContainerView.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 14.0f, 0.0f));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
        linksTextView.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.PaidContentInfo), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$60(context);
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
        final BottomSheet[] bottomSheetArr = {builder.create()};
        editTextBoldCursor.setText(j <= 0 ? "" : Long.toString(j));
        editTextBoldCursor.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Stars.StarsIntroActivity.11
            private boolean ignore;
            private int shakeDp = 2;

            /* JADX WARN: Removed duplicated region for block: B:23:0x007d  */
            /* JADX WARN: Removed duplicated region for block: B:29:0x00a1  */
            /* JADX WARN: Removed duplicated region for block: B:31:0x00b5  */
            @Override // android.text.TextWatcher
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void afterTextChanged(Editable editable) {
                long j2;
                TextView textView3;
                String str = "";
                if (this.ignore) {
                    return;
                }
                try {
                    j2 = TextUtils.isEmpty(editable) ? 0L : Long.parseLong(editable.toString());
                } catch (Exception unused) {
                    j2 = 0;
                }
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
                } catch (Exception unused2) {
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
                    if (j2 != 0) {
                    }
                    textView3.setText(str);
                }
                this.ignore = false;
                if (!z) {
                    buttonWithCounterView2.setEnabled(j2 > 0);
                }
                outlineTextContainerView.animateSelection(EditTextBoldCursor.this.isFocused(), true ^ TextUtils.isEmpty(EditTextBoldCursor.this.getText()));
                if (j2 != 0) {
                    textView2.animate().alpha(0.0f).start();
                    textView3 = textView2;
                } else {
                    textView2.animate().alpha(1.0f).start();
                    textView3 = textView2;
                    StringBuilder sb = new StringBuilder();
                    sb.append("≈");
                    BillingController billingController = BillingController.getInstance();
                    double d = j2;
                    Double.isNaN(d);
                    double d2 = MessagesController.getInstance(UserConfig.selectedAccount).starsUsdWithdrawRate1000;
                    Double.isNaN(d2);
                    sb.append(billingController.formatCurrency((long) ((d / 1000.0d) * d2), "USD"));
                    str = sb.toString();
                }
                textView3.setText(str);
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }
        });
        final boolean[] zArr = {false};
        editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda37
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView3, int i2, KeyEvent keyEvent) {
                boolean lambda$showMediaPriceSheet$62;
                lambda$showMediaPriceSheet$62 = StarsIntroActivity.lambda$showMediaPriceSheet$62(zArr, callback2, buttonWithCounterView2, editTextBoldCursor, bottomSheetArr, textView3, i2, keyEvent);
                return lambda$showMediaPriceSheet$62;
            }
        });
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda38
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showMediaPriceSheet$64(zArr, callback2, editTextBoldCursor, buttonWithCounterView2, bottomSheetArr, view);
            }
        });
        if (buttonWithCounterView3 != null) {
            buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda39
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showMediaPriceSheet$66(zArr, callback2, buttonWithCounterView3, editTextBoldCursor, bottomSheetArr, view);
                }
            });
        }
        bottomSheetArr[0].fixNavigationBar();
        bottomSheetArr[0].setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda40
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
            }
        });
        bottomSheetArr[0].show();
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$69(bottomSheetArr, editTextBoldCursor);
            }
        }, lastFragment instanceof ChatActivity ? ((ChatActivity) lastFragment).needEnterText() : false ? 200L : 80L);
        return bottomSheetArr[0];
    }

    public static BottomSheet showSubscriptionSheet(final Context context, final int i, final TL_stars.StarsSubscription starsSubscription, final Theme.ResourcesProvider resourcesProvider) {
        BottomSheet.Builder builder;
        String formatString;
        final ButtonWithCounterView buttonWithCounterView;
        View.OnClickListener onClickListener;
        if (starsSubscription == null || context == null) {
            return null;
        }
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(4.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 7, 0, 0, 0, 10));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
        final long peerDialogId = DialogObject.getPeerDialogId(starsSubscription.peer);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        if (peerDialogId >= 0) {
            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
            avatarDrawable.setInfo(user);
            backupImageView.setForUserOrChat(user, avatarDrawable);
            builder = builder2;
        } else {
            builder = builder2;
            TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
            avatarDrawable.setInfo(chat);
            backupImageView.setForUserOrChat(chat, avatarDrawable);
        }
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(100, 100, 17));
        Drawable drawable = context.getResources().getDrawable(R.drawable.star_small_outline);
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground, resourcesProvider), PorterDuff.Mode.SRC_IN));
        Drawable drawable2 = context.getResources().getDrawable(R.drawable.star_small_inner);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawable);
        frameLayout.addView(imageView, LayoutHelper.createFrame(28, 28, 17));
        imageView.setTranslationX(AndroidUtilities.dp(34.0f));
        imageView.setTranslationY(AndroidUtilities.dp(35.0f));
        imageView.setScaleX(1.1f);
        imageView.setScaleY(1.1f);
        ImageView imageView2 = new ImageView(context);
        imageView2.setImageDrawable(drawable2);
        frameLayout.addView(imageView2, LayoutHelper.createFrame(28, 28, 17));
        imageView2.setTranslationX(AndroidUtilities.dp(34.0f));
        imageView2.setTranslationY(AndroidUtilities.dp(35.0f));
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(R.string.StarsSubscriptionTitle));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
        TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = starsSubscription.pricing;
        int i2 = tL_starsSubscriptionPricing.period;
        if (i2 == 2592000) {
            formatString = LocaleController.formatString(R.string.StarsSubscriptionPrice, Long.valueOf(tL_starsSubscriptionPricing.amount));
        } else {
            formatString = LocaleController.formatString(R.string.StarsSubscriptionPrice, Long.valueOf(tL_starsSubscriptionPricing.amount), i2 == 300 ? "5min" : "min");
        }
        textView2.setText(replaceStarsWithPlain(formatString, 0.8f));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TableView tableView = new TableView(context, resourcesProvider);
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
        linksTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i3 = Theme.key_chat_messageLinkIn;
        linksTextView.setTextColor(Theme.getColor(i3, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setSingleLine(true);
        linksTextView.setDisablePaddingsOffsetY(true);
        AvatarSpan avatarSpan = new AvatarSpan(linksTextView, i, 24.0f);
        final TLRPC.Chat chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
        boolean z = chat2 == null;
        String str = chat2 == null ? "" : chat2.title;
        avatarSpan.setChat(chat2);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x  " + ((Object) str));
        spannableStringBuilder.setSpan(avatarSpan, 0, 1, 33);
        spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.9
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                bottomSheetArr[0].dismiss();
                BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (safeLastFragment != null) {
                    safeLastFragment.presentFragment(ChatActivity.of(peerDialogId));
                }
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        }, 3, spannableStringBuilder.length(), 33);
        linksTextView.setText(spannableStringBuilder);
        if (!z) {
            tableView.addRowUnpadded(LocaleController.getString(R.string.StarsSubscriptionChannel), linksTextView);
        }
        CharSequence string = LocaleController.getString(R.string.StarsSubscriptionSince);
        int i4 = R.string.formatDateAtTime;
        tableView.addRow(string, LocaleController.formatString(i4, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000))));
        long currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
        tableView.addRow(LocaleController.getString(starsSubscription.canceled ? R.string.StarsSubscriptionUntilExpires : currentTime > ((long) starsSubscription.until_date) ? R.string.StarsSubscriptionUntilExpired : R.string.StarsSubscriptionUntilRenews), LocaleController.formatString(i4, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsSubscription.until_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsSubscription.until_date * 1000))));
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
        LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        int i5 = Theme.key_windowBackgroundWhiteGrayText2;
        linksTextView2.setTextColor(Theme.getColor(i5, resourcesProvider));
        linksTextView2.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
        linksTextView2.setTextSize(1, 14.0f);
        linksTextView2.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$37(context);
            }
        }));
        linksTextView2.setGravity(17);
        linearLayout.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 14.0f, 15.0f, 14.0f, 7.0f));
        if (currentTime >= starsSubscription.until_date) {
            LinkSpanDrawable.LinksTextView linksTextView3 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView3.setTextColor(Theme.getColor(i5, resourcesProvider));
            linksTextView3.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
            linksTextView3.setTextSize(1, 14.0f);
            linksTextView3.setText(LocaleController.formatString(R.string.StarsSubscriptionExpiredInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
            linksTextView3.setSingleLine(false);
            linksTextView3.setMaxLines(4);
            linksTextView3.setGravity(17);
            linearLayout.addView(linksTextView3, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
            if (starsSubscription.chat_invite_hash != null) {
                buttonWithCounterView = new ButtonWithCounterView(context, true, resourcesProvider);
                buttonWithCounterView.setText(LocaleController.getString(R.string.StarsSubscriptionAgain), false);
                linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda66
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showSubscriptionSheet$53(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, resourcesProvider, view);
                    }
                };
                buttonWithCounterView.setOnClickListener(onClickListener);
            }
        } else if (starsSubscription.can_refulfill) {
            LinkSpanDrawable.LinksTextView linksTextView4 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView4.setTextColor(Theme.getColor(i5, resourcesProvider));
            linksTextView4.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
            linksTextView4.setTextSize(1, 14.0f);
            linksTextView4.setText(LocaleController.formatString(R.string.StarsSubscriptionRefulfillInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
            linksTextView4.setSingleLine(false);
            linksTextView4.setMaxLines(4);
            linksTextView4.setGravity(17);
            linearLayout.addView(linksTextView4, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
            final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, true, resourcesProvider);
            buttonWithCounterView2.setText(LocaleController.getString(R.string.StarsSubscriptionRefulfill), false);
            linearLayout.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
            buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda63
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showSubscriptionSheet$41(ButtonWithCounterView.this, i, starsSubscription, bottomSheetArr, peerDialogId, context, resourcesProvider, chat2, view);
                }
            });
        } else {
            if (starsSubscription.canceled) {
                LinkSpanDrawable.LinksTextView linksTextView5 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                linksTextView5.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
                linksTextView5.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                linksTextView5.setTextSize(1, 14.0f);
                linksTextView5.setText(LocaleController.getString(R.string.StarsSubscriptionCancelledText));
                linksTextView5.setSingleLine(false);
                linksTextView5.setMaxLines(4);
                linksTextView5.setGravity(17);
                linearLayout.addView(linksTextView5, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                if (starsSubscription.chat_invite_hash != null) {
                    buttonWithCounterView = new ButtonWithCounterView(context, true, resourcesProvider);
                    buttonWithCounterView.setText(LocaleController.getString(R.string.StarsSubscriptionRenew), false);
                    linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                    onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda64
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showSubscriptionSheet$44(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, chat2, view);
                        }
                    };
                }
            } else {
                LinkSpanDrawable.LinksTextView linksTextView6 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                linksTextView6.setTextColor(Theme.getColor(i5, resourcesProvider));
                linksTextView6.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                linksTextView6.setTextSize(1, 14.0f);
                linksTextView6.setText(LocaleController.formatString(R.string.StarsSubscriptionCancelInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
                linksTextView6.setSingleLine(false);
                linksTextView6.setMaxLines(4);
                linksTextView6.setGravity(17);
                linearLayout.addView(linksTextView6, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                buttonWithCounterView = new ButtonWithCounterView(context, false, resourcesProvider);
                buttonWithCounterView.setText(LocaleController.getString(R.string.StarsSubscriptionCancel), false);
                buttonWithCounterView.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
                linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda65
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showSubscriptionSheet$47(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, chat2, view);
                    }
                };
            }
            buttonWithCounterView.setOnClickListener(onClickListener);
        }
        BottomSheet.Builder builder3 = builder;
        builder3.setCustomView(linearLayout);
        BottomSheet create = builder3.create();
        bottomSheetArr[0] = create;
        create.useBackgroundTopPadding = false;
        create.fixNavigationBar();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
            bottomSheetArr[0].makeAttached(safeLastFragment);
        }
        bottomSheetArr[0].show();
        return bottomSheetArr[0];
    }

    public static BottomSheet showTransactionSheet(Context context, int i, int i2, TLRPC.Peer peer, TLRPC.Peer peer2, TLRPC.TL_messageActionGiftStars tL_messageActionGiftStars, Theme.ResourcesProvider resourcesProvider) {
        TL_stars.StarsTransaction starsTransaction = new TL_stars.StarsTransaction();
        starsTransaction.title = null;
        starsTransaction.description = null;
        starsTransaction.photo = null;
        TL_stars.TL_starsTransactionPeer tL_starsTransactionPeer = new TL_stars.TL_starsTransactionPeer();
        starsTransaction.peer = tL_starsTransactionPeer;
        tL_starsTransactionPeer.peer = peer;
        starsTransaction.date = i2;
        starsTransaction.stars = tL_messageActionGiftStars.stars;
        starsTransaction.id = tL_messageActionGiftStars.transaction_id;
        starsTransaction.gift = true;
        starsTransaction.sent_by = peer;
        starsTransaction.received_by = peer2;
        return showTransactionSheet(context, false, 0L, i, starsTransaction, resourcesProvider);
    }

    public static BottomSheet showTransactionSheet(Context context, int i, int i2, TLRPC.Peer peer, TLRPC.Peer peer2, TLRPC.TL_messageActionPrizeStars tL_messageActionPrizeStars, Theme.ResourcesProvider resourcesProvider) {
        TL_stars.StarsTransaction starsTransaction = new TL_stars.StarsTransaction();
        starsTransaction.title = null;
        starsTransaction.description = null;
        starsTransaction.photo = null;
        TL_stars.TL_starsTransactionPeer tL_starsTransactionPeer = new TL_stars.TL_starsTransactionPeer();
        starsTransaction.peer = tL_starsTransactionPeer;
        tL_starsTransactionPeer.peer = tL_messageActionPrizeStars.boost_peer;
        starsTransaction.date = i2;
        starsTransaction.stars = tL_messageActionPrizeStars.stars;
        starsTransaction.id = tL_messageActionPrizeStars.transaction_id;
        starsTransaction.gift = true;
        starsTransaction.flags |= LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        starsTransaction.giveaway_post_id = tL_messageActionPrizeStars.giveaway_msg_id;
        starsTransaction.sent_by = peer;
        starsTransaction.received_by = peer2;
        return showTransactionSheet(context, false, 0L, i, starsTransaction, resourcesProvider);
    }

    public static BottomSheet showTransactionSheet(Context context, int i, int i2, TLRPC.TL_messageActionPaymentRefunded tL_messageActionPaymentRefunded, Theme.ResourcesProvider resourcesProvider) {
        TL_stars.StarsTransaction starsTransaction = new TL_stars.StarsTransaction();
        starsTransaction.title = null;
        starsTransaction.description = null;
        starsTransaction.photo = null;
        TL_stars.TL_starsTransactionPeer tL_starsTransactionPeer = new TL_stars.TL_starsTransactionPeer();
        starsTransaction.peer = tL_starsTransactionPeer;
        tL_starsTransactionPeer.peer = tL_messageActionPaymentRefunded.peer;
        starsTransaction.date = i2;
        starsTransaction.stars = tL_messageActionPaymentRefunded.total_amount;
        starsTransaction.id = tL_messageActionPaymentRefunded.charge.id;
        starsTransaction.refund = true;
        return showTransactionSheet(context, false, 0L, i, starsTransaction, resourcesProvider);
    }

    public static BottomSheet showTransactionSheet(Context context, boolean z, int i, TLRPC.TL_payments_paymentReceiptStars tL_payments_paymentReceiptStars, Theme.ResourcesProvider resourcesProvider) {
        TL_stars.StarsTransaction starsTransaction = new TL_stars.StarsTransaction();
        starsTransaction.title = tL_payments_paymentReceiptStars.title;
        starsTransaction.description = tL_payments_paymentReceiptStars.description;
        starsTransaction.photo = tL_payments_paymentReceiptStars.photo;
        TL_stars.TL_starsTransactionPeer tL_starsTransactionPeer = new TL_stars.TL_starsTransactionPeer();
        starsTransaction.peer = tL_starsTransactionPeer;
        tL_starsTransactionPeer.peer = MessagesController.getInstance(i).getPeer(tL_payments_paymentReceiptStars.bot_id);
        starsTransaction.date = tL_payments_paymentReceiptStars.date;
        starsTransaction.stars = -tL_payments_paymentReceiptStars.total_amount;
        starsTransaction.id = tL_payments_paymentReceiptStars.transaction_id;
        return showTransactionSheet(context, z, 0L, i, starsTransaction, resourcesProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0363  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x037c  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0397  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x03fd  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x04dc  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x063d  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x064b  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x06f5  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0729 A[LOOP:0: B:166:0x06a6->B:181:0x0729, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x07ac A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x08a8  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x08df  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x096f  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x097a  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x09a0  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x09aa  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x09c2  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0733 A[EDGE_INSN: B:226:0x0733->B:182:0x0733 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r15v2, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r1v60, types: [android.widget.TextView, android.view.View, org.telegram.ui.Components.LinkSpanDrawable$LinksTextView] */
    /* JADX WARN: Type inference failed for: r1v86, types: [android.view.View, org.telegram.ui.Stories.recorder.ButtonWithCounterView] */
    /* JADX WARN: Type inference failed for: r3v49, types: [android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r4v31 */
    /* JADX WARN: Type inference failed for: r4v32, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v33 */
    /* JADX WARN: Type inference failed for: r4v40, types: [android.widget.TextView, android.view.View, org.telegram.ui.Components.LinkSpanDrawable$LinksTextView] */
    /* JADX WARN: Type inference failed for: r7v2, types: [android.view.ViewGroup] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showTransactionSheet(final Context context, final boolean z, final long j, final int i, final TL_stars.StarsTransaction starsTransaction, final Theme.ResourcesProvider resourcesProvider) {
        BottomSheet[] bottomSheetArr;
        LinearLayout linearLayout;
        BottomSheet.Builder builder;
        String str;
        String str2;
        BackupImageView backupImageView;
        LinearLayout.LayoutParams createLinear;
        LinearLayout linearLayout2;
        TLRPC.Chat chat;
        ImageLocation imageLocation;
        ImageLocation forDocument;
        int i2;
        boolean isUserSelf;
        TLRPC.User user;
        char c;
        String string;
        final BottomSheet[] bottomSheetArr2;
        final BottomSheet[] bottomSheetArr3;
        Object obj;
        float f;
        String string2;
        int i3;
        final long peerDialogId;
        String string3;
        Runnable runnable;
        TableView tableView;
        int i4;
        TL_stars.StarsTransactionPeer starsTransactionPeer;
        final Theme.ResourcesProvider resourcesProvider2;
        TL_stars.StarGift starGift;
        ?? r4;
        String string4;
        BaseFragment safeLastFragment;
        TLRPC.Chat chat2;
        String sb;
        ImageLocation imageLocation2;
        ImageLocation forDocument2;
        final int i5 = i;
        if (starsTransaction == null || context == null) {
            return null;
        }
        boolean z2 = (starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        BottomSheet[] bottomSheetArr4 = new BottomSheet[1];
        final LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp((z2 || starsTransaction.gift) ? 0.0f : 20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout3.setClipChildren(false);
        linearLayout3.setClipToPadding(false);
        final BackupImageView backupImageView2 = new BackupImageView(context);
        if (starsTransaction.stargift != null) {
            setGiftImage(backupImageView2.getImageReceiver(), starsTransaction.stargift, (int) NotificationCenter.audioRouteChanged);
            linearLayout3.addView(backupImageView2, LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, (int) NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
            bottomSheetArr = bottomSheetArr4;
            linearLayout2 = linearLayout3;
            builder = builder2;
            str = "fragment";
            str2 = "/";
        } else {
            if (z2 || starsTransaction.gift) {
                bottomSheetArr = bottomSheetArr4;
                linearLayout = linearLayout3;
                builder = builder2;
                str = "fragment";
                str2 = "/";
                backupImageView = backupImageView2;
                setGiftImage(backupImageView, backupImageView.getImageReceiver(), starsTransaction.stars);
                createLinear = LayoutHelper.createLinear((int) NotificationCenter.audioRouteChanged, (int) NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10);
            } else if (starsTransaction.extended_media.isEmpty()) {
                bottomSheetArr = bottomSheetArr4;
                builder = builder2;
                str = "fragment";
                str2 = "/";
                backupImageView = backupImageView2;
                TL_stars.StarsTransactionPeer starsTransactionPeer2 = starsTransaction.peer;
                if (starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeer) {
                    if (starsTransaction.photo != null) {
                        backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
                        backupImageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsTransaction.photo)), "100_100", (Drawable) null, 0, (Object) null);
                    } else {
                        backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
                        long peerDialogId2 = (starsTransaction.subscription && z) ? j : DialogObject.getPeerDialogId(starsTransaction.peer.peer);
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        if (peerDialogId2 >= 0) {
                            TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId2));
                            avatarDrawable.setInfo(user2);
                            chat = user2;
                        } else {
                            TLRPC.Chat chat3 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId2));
                            avatarDrawable.setInfo(chat3);
                            chat = chat3;
                        }
                        backupImageView.setForUserOrChat(chat, avatarDrawable);
                    }
                    createLinear = LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10);
                    linearLayout = linearLayout3;
                } else {
                    linearLayout2 = linearLayout3;
                    CombinedDrawable createDrawable = SessionCell.createDrawable(100, starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerAppStore ? "ios" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerPlayMarket ? "android" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerPremiumBot ? "premiumbot" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerFragment ? str : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerAds ? "ads" : "?");
                    createDrawable.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                    backupImageView.setImageDrawable(createDrawable);
                }
            } else {
                backupImageView2.setRoundRadius(AndroidUtilities.dp(30.0f));
                TLRPC.MessageMedia messageMedia = starsTransaction.extended_media.get(0);
                if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                    forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(messageMedia.photo.sizes, AndroidUtilities.dp(100.0f), true), messageMedia.photo);
                } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(messageMedia.document.thumbs, AndroidUtilities.dp(100.0f), true), messageMedia.document);
                } else {
                    imageLocation = null;
                    backupImageView2.setImage(imageLocation, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                    linearLayout3.addView(backupImageView2, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                    str2 = "/";
                    bottomSheetArr = bottomSheetArr4;
                    builder = builder2;
                    str = "fragment";
                    backupImageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$20(z, j, starsTransaction, i, resourcesProvider, backupImageView2, linearLayout3, view);
                        }
                    });
                    linearLayout2 = linearLayout3;
                }
                imageLocation = forDocument;
                backupImageView2.setImage(imageLocation, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                linearLayout3.addView(backupImageView2, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                str2 = "/";
                bottomSheetArr = bottomSheetArr4;
                builder = builder2;
                str = "fragment";
                backupImageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showTransactionSheet$20(z, j, starsTransaction, i, resourcesProvider, backupImageView2, linearLayout3, view);
                    }
                });
                linearLayout2 = linearLayout3;
            }
            linearLayout.addView(backupImageView, createLinear);
            linearLayout2 = linearLayout;
        }
        TextView textView = new TextView(context);
        int i6 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i6, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(getTransactionTitle(i5, z, starsTransaction));
        linearLayout2.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 18.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(starsTransaction.stars >= 0 ? Theme.key_color_green : Theme.key_color_red, resourcesProvider));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(starsTransaction.stars >= 0 ? "+" : "-");
        sb2.append(LocaleController.formatNumber((int) Math.abs(starsTransaction.stars), ' '));
        sb2.append(" ⭐️");
        textView2.setText(replaceStarsWithPlain(sb2.toString(), 0.8f));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView2.getText());
        if (starsTransaction.refund) {
            i2 = R.string.StarsRefunded;
        } else if (!starsTransaction.failed) {
            if (starsTransaction.pending) {
                textView2.setTextColor(Theme.getColor(Theme.key_color_yellow, resourcesProvider));
                i2 = R.string.StarsPending;
            }
            textView2.setText(spannableStringBuilder);
            linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
            if (!z2 || starsTransaction.gift) {
                TLRPC.User user3 = starsTransaction.sent_by != null ? null : MessagesController.getInstance(i).getUser(Long.valueOf(DialogObject.getPeerDialogId(starsTransaction.sent_by)));
                TLRPC.User user4 = starsTransaction.sent_by != null ? null : MessagesController.getInstance(i).getUser(Long.valueOf(DialogObject.getPeerDialogId(starsTransaction.received_by)));
                isUserSelf = UserObject.isUserSelf(user3);
                if (isUserSelf) {
                    user = user4;
                } else {
                    textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                    StringBuilder sb3 = new StringBuilder();
                    user = user4;
                    sb3.append(LocaleController.formatNumber((int) Math.abs(starsTransaction.stars), ' '));
                    sb3.append(" ⭐️");
                    textView2.setText(replaceStarsWithPlain(sb3.toString(), 0.8f));
                }
                LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
                linksTextView.setTextColor(Theme.getColor(i6, resourcesProvider));
                linksTextView.setTextSize(1, 16.0f);
                linksTextView.setGravity(17);
                linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                linksTextView.setDisablePaddingsOffsetY(true);
                if (isUserSelf) {
                    c = 0;
                    string = LocaleController.getString(R.string.ActionGiftStarsSubtitleYou);
                } else {
                    c = 0;
                    string = LocaleController.formatString(R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user));
                }
                SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(string);
                bottomSheetArr2 = bottomSheetArr;
                CharSequence replaceArrows = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.GiftStarsSubtitleLinkName).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$21(context, bottomSheetArr2);
                    }
                }), true);
                CharSequence[] charSequenceArr = new CharSequence[3];
                charSequenceArr[c] = replaceTags;
                charSequenceArr[1] = " ";
                charSequenceArr[2] = replaceArrows;
                linksTextView.setText(TextUtils.concat(charSequenceArr));
                linearLayout2.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
            } else {
                if (starsTransaction.description != null && starsTransaction.extended_media.isEmpty()) {
                    TextView textView3 = new TextView(context);
                    textView3.setTextColor(Theme.getColor(i6, resourcesProvider));
                    textView3.setTextSize(1, 16.0f);
                    textView3.setGravity(17);
                    textView3.setText(starsTransaction.description);
                    linearLayout2.addView(textView3, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                }
                bottomSheetArr2 = bottomSheetArr;
            }
            TableView tableView2 = new TableView(context, resourcesProvider);
            if (starsTransaction.stargift == null) {
                if (starsTransaction.refund) {
                    bottomSheetArr3 = bottomSheetArr2;
                    obj = linearLayout2;
                    f = 14.0f;
                } else {
                    peerDialogId = DialogObject.getPeerDialogId(starsTransaction.peer.peer);
                    if (starsTransaction.stars > 0) {
                        String string5 = LocaleController.getString(R.string.StarGiveawayPrizeFrom);
                        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$22(bottomSheetArr2, starsTransaction, peerDialogId);
                            }
                        };
                        obj = linearLayout2;
                        f = 14.0f;
                        final BottomSheet[] bottomSheetArr5 = bottomSheetArr2;
                        tableView2.addRowUser(string5, i, peerDialogId, runnable2);
                        tableView2.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeTo), i, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda11
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$23(bottomSheetArr5, i5);
                            }
                        });
                        bottomSheetArr3 = bottomSheetArr5;
                    } else {
                        obj = linearLayout2;
                        f = 14.0f;
                        i4 = i;
                        bottomSheetArr3 = bottomSheetArr2;
                        tableView2.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeFrom), i4, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda12
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$24(bottomSheetArr2, i5);
                            }
                        });
                        string3 = LocaleController.getString(R.string.StarGiveawayPrizeTo);
                        runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda13
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$25(bottomSheetArr3, starsTransaction, peerDialogId);
                            }
                        };
                        tableView = tableView2;
                        tableView.addRowUser(string3, i4, peerDialogId, runnable);
                    }
                }
                starsTransactionPeer = starsTransaction.peer;
                if ((starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) && (starsTransaction.flags & 256) != 0) {
                    final long peerDialogId3 = DialogObject.getPeerDialogId(starsTransactionPeer.peer);
                    if (z) {
                        peerDialogId3 = j;
                    }
                    chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId3));
                    if (chat2 != null) {
                        ?? linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                        linksTextView2.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
                        linksTextView2.setEllipsize(TextUtils.TruncateAt.END);
                        int i7 = Theme.key_chat_messageLinkIn;
                        linksTextView2.setTextColor(Theme.getColor(i7, resourcesProvider));
                        linksTextView2.setLinkTextColor(Theme.getColor(i7, resourcesProvider));
                        linksTextView2.setTextSize(1, f);
                        linksTextView2.setDisablePaddingsOffsetY(true);
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("");
                        if (!starsTransaction.extended_media.isEmpty()) {
                            Iterator<TLRPC.MessageMedia> it = starsTransaction.extended_media.iterator();
                            int i8 = 0;
                            while (it.hasNext()) {
                                TLRPC.MessageMedia next = it.next();
                                Iterator<TLRPC.MessageMedia> it2 = it;
                                ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(linksTextView2, i5, 24.0f);
                                if (next instanceof TLRPC.TL_messageMediaPhoto) {
                                    forDocument2 = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(next.photo.sizes, AndroidUtilities.dp(24.0f), true), next.photo);
                                } else if (next instanceof TLRPC.TL_messageMediaDocument) {
                                    forDocument2 = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(next.document.thumbs, AndroidUtilities.dp(24.0f), true), next.document);
                                } else {
                                    imageLocation2 = null;
                                    if (imageLocation2 != null) {
                                        imageReceiverSpan.setRoundRadius(6.0f);
                                        imageReceiverSpan.imageReceiver.setImage(imageLocation2, "24_24", null, null, null, 0);
                                        SpannableString spannableString = new SpannableString("x");
                                        spannableString.setSpan(imageReceiverSpan, 0, spannableString.length(), 33);
                                        spannableStringBuilder2.append((CharSequence) spannableString);
                                        spannableStringBuilder2.append((CharSequence) " ");
                                        i8++;
                                    }
                                    if (i8 < 3) {
                                        break;
                                    }
                                    it = it2;
                                    i5 = i;
                                }
                                imageLocation2 = forDocument2;
                                if (imageLocation2 != null) {
                                }
                                if (i8 < 3) {
                                }
                            }
                        }
                        spannableStringBuilder2.append((CharSequence) " ");
                        int length = spannableStringBuilder2.length();
                        String publicUsername = ChatObject.getPublicUsername(chat2);
                        if (TextUtils.isEmpty(publicUsername)) {
                            sb = chat2.title;
                        } else {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(MessagesController.getInstance(i).linkPrefix);
                            String str3 = str2;
                            sb4.append(str3);
                            sb4.append(publicUsername);
                            sb4.append(str3);
                            sb4.append(starsTransaction.msg_id);
                            sb = sb4.toString();
                        }
                        spannableStringBuilder2.append((CharSequence) sb);
                        final Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$31(bottomSheetArr3, peerDialogId3, starsTransaction);
                            }
                        };
                        spannableStringBuilder2.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.8
                            @Override // android.text.style.ClickableSpan
                            public void onClick(View view) {
                                runnable3.run();
                            }

                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public void updateDrawState(TextPaint textPaint) {
                                textPaint.setUnderlineText(false);
                            }
                        }, length, spannableStringBuilder2.length(), 33);
                        linksTextView2.setSingleLine(true);
                        linksTextView2.setEllipsize(TextUtils.TruncateAt.END);
                        linksTextView2.setText(spannableStringBuilder2);
                        linksTextView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                runnable3.run();
                            }
                        });
                        tableView2.addRowUnpadded(LocaleController.getString(starsTransaction.reaction ? R.string.StarsTransactionMessage : R.string.StarsTransactionMedia), linksTextView2);
                    }
                }
                if (!TextUtils.isEmpty(starsTransaction.id) || z2) {
                    resourcesProvider2 = resourcesProvider;
                } else {
                    FrameLayout frameLayout = new FrameLayout(context);
                    frameLayout.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(10.66f), AndroidUtilities.dp(9.33f));
                    TextView textView4 = new TextView(context);
                    textView4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MONO));
                    textView4.setTextSize(1, 9.0f);
                    resourcesProvider2 = resourcesProvider;
                    textView4.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider2));
                    textView4.setMaxLines(4);
                    textView4.setSingleLine(false);
                    textView4.setText(starsTransaction.id);
                    frameLayout.addView(textView4, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 34.0f, 0.0f));
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.msg_copy);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    int i9 = Theme.key_windowBackgroundWhiteBlueIcon;
                    imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i9, resourcesProvider2), PorterDuff.Mode.SRC_IN));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$33(TL_stars.StarsTransaction.this, bottomSheetArr3, resourcesProvider2, view);
                        }
                    });
                    ScaleStateListAnimator.apply(imageView);
                    imageView.setBackground(Theme.createSelectorDrawable(Theme.multAlpha(Theme.getColor(i9, resourcesProvider2), 0.1f), 7));
                    frameLayout.addView(imageView, LayoutHelper.createFrame(30, 30, 21));
                    tableView2.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionID), frameLayout);
                }
                String string6 = LocaleController.getString(R.string.StarsTransactionDate);
                int i10 = R.string.formatDateAtTime;
                tableView2.addRow(string6, LocaleController.formatString(i10, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction.date * 1000))));
                starGift = starsTransaction.stargift;
                if (starGift != null) {
                    if (starGift.limited) {
                        addAvailabilityRow(tableView2, i, starGift, resourcesProvider2);
                    }
                    if (!TextUtils.isEmpty(starsTransaction.description)) {
                        tableView2.addFullRow(new SpannableStringBuilder(starsTransaction.description));
                    }
                }
                ?? r3 = obj;
                r3.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                if ((starsTransaction.flags & 32) != 0) {
                    tableView2.addRow(LocaleController.getString(R.string.StarsTransactionTONDate), LocaleController.formatString(i10, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction.transaction_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction.transaction_date * 1000))));
                }
                LinkSpanDrawable.LinksTextView linksTextView3 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
                linksTextView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider2));
                linksTextView3.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider2));
                linksTextView3.setTextSize(1, 14.0f);
                linksTextView3.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$34(context);
                    }
                }));
                linksTextView3.setGravity(17);
                r3.addView(linksTextView3, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                ?? buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider2);
                if ((starsTransaction.flags & 32) == 0) {
                    string4 = LocaleController.getString(R.string.StarsTransactionViewInBlockchainExplorer);
                    r4 = 0;
                } else {
                    r4 = 0;
                    string4 = LocaleController.getString(R.string.OK);
                }
                buttonWithCounterView.setText(string4, r4);
                r3.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                BottomSheet.Builder builder3 = builder;
                builder3.setCustomView(r3);
                BottomSheet create = builder3.create();
                bottomSheetArr3[r4] = create;
                create.useBackgroundTopPadding = r4;
                if ((starsTransaction.flags & 32) == 0) {
                    buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda7
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$35(context, starsTransaction, view);
                        }
                    });
                } else {
                    buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showTransactionSheet$36(bottomSheetArr3, view);
                        }
                    });
                }
                bottomSheetArr3[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                    bottomSheetArr3[0].makeAttached(safeLastFragment);
                }
                bottomSheetArr3[0].show();
                return bottomSheetArr3[0];
            }
            bottomSheetArr3 = bottomSheetArr2;
            obj = linearLayout2;
            f = 14.0f;
            TL_stars.StarsTransactionPeer starsTransactionPeer3 = starsTransaction.peer;
            if (!(starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeer)) {
                if (!(starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerFragment)) {
                    if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerAppStore) {
                        string2 = LocaleController.getString(R.string.StarsTransactionSource);
                        i3 = R.string.AppStore;
                    } else if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerPlayMarket) {
                        string2 = LocaleController.getString(R.string.StarsTransactionSource);
                        i3 = R.string.PlayMarket;
                    } else if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerPremiumBot) {
                        string2 = LocaleController.getString(R.string.StarsTransactionSource);
                        i3 = R.string.StarsTransactionBot;
                    }
                    tableView2.addRow(string2, LocaleController.getString(i3));
                } else if (starsTransaction.gift) {
                    ?? linksTextView4 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                    linksTextView4.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
                    linksTextView4.setEllipsize(TextUtils.TruncateAt.END);
                    int i11 = Theme.key_chat_messageLinkIn;
                    linksTextView4.setTextColor(Theme.getColor(i11, resourcesProvider));
                    linksTextView4.setLinkTextColor(Theme.getColor(i11, resourcesProvider));
                    linksTextView4.setTextSize(1, 14.0f);
                    linksTextView4.setSingleLine(true);
                    linksTextView4.setDisablePaddingsOffsetY(true);
                    AvatarSpan avatarSpan = new AvatarSpan(linksTextView4, i5, 24.0f);
                    String string7 = LocaleController.getString(R.string.StarsTransactionUnknown);
                    CombinedDrawable platformDrawable = StarsTransactionView.getPlatformDrawable(str, 24);
                    platformDrawable.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    avatarSpan.setImageDrawable(platformDrawable);
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder("x  " + ((Object) string7));
                    spannableStringBuilder3.setSpan(avatarSpan, 0, 1, 33);
                    spannableStringBuilder3.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.7
                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view) {
                            bottomSheetArr3[0].dismiss();
                            Browser.openUrl(context, LocaleController.getString(R.string.StarsTransactionUnknownLink));
                        }

                        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                        public void updateDrawState(TextPaint textPaint) {
                            textPaint.setUnderlineText(false);
                        }
                    }, 3, spannableStringBuilder3.length(), 33);
                    linksTextView4.setText(spannableStringBuilder3);
                    tableView2.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionRecipient), linksTextView4);
                } else {
                    string2 = LocaleController.getString(R.string.StarsTransactionSource);
                    i3 = R.string.Fragment;
                    tableView2.addRow(string2, LocaleController.getString(i3));
                }
                starsTransactionPeer = starsTransaction.peer;
                if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) {
                }
                if (TextUtils.isEmpty(starsTransaction.id)) {
                }
                resourcesProvider2 = resourcesProvider;
                String string62 = LocaleController.getString(R.string.StarsTransactionDate);
                int i102 = R.string.formatDateAtTime;
                tableView2.addRow(string62, LocaleController.formatString(i102, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction.date * 1000))));
                starGift = starsTransaction.stargift;
                if (starGift != null) {
                }
                ?? r32 = obj;
                r32.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                if ((starsTransaction.flags & 32) != 0) {
                }
                LinkSpanDrawable.LinksTextView linksTextView32 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
                linksTextView32.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider2));
                linksTextView32.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider2));
                linksTextView32.setTextSize(1, 14.0f);
                linksTextView32.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$34(context);
                    }
                }));
                linksTextView32.setGravity(17);
                r32.addView(linksTextView32, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                ?? buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider2);
                if ((starsTransaction.flags & 32) == 0) {
                }
                buttonWithCounterView2.setText(string4, r4);
                r32.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
                BottomSheet.Builder builder32 = builder;
                builder32.setCustomView(r32);
                BottomSheet create2 = builder32.create();
                bottomSheetArr3[r4] = create2;
                create2.useBackgroundTopPadding = r4;
                if ((starsTransaction.flags & 32) == 0) {
                }
                bottomSheetArr3[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet()) {
                }
                bottomSheetArr3[0].show();
                return bottomSheetArr3[0];
            }
            peerDialogId = DialogObject.getPeerDialogId(starsTransactionPeer3.peer);
            if (z2) {
                tableView2.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeFrom), i, peerDialogId, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$26(bottomSheetArr3, starsTransaction, peerDialogId);
                    }
                });
                tableView2.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeTo), i, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$27(bottomSheetArr3, i5);
                    }
                });
                tableView2.addRowLink(LocaleController.getString(R.string.StarGiveawayReason), LocaleController.getString(R.string.StarGiveawayReasonLink), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$28(bottomSheetArr3, starsTransaction, peerDialogId);
                    }
                });
                tableView2.addRow(LocaleController.getString(R.string.StarGiveawayGift), LocaleController.formatPluralStringComma("Stars", (int) starsTransaction.stars));
                starsTransactionPeer = starsTransaction.peer;
                if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) {
                    final long peerDialogId32 = DialogObject.getPeerDialogId(starsTransactionPeer.peer);
                    if (z) {
                    }
                    chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId32));
                    if (chat2 != null) {
                    }
                }
                if (TextUtils.isEmpty(starsTransaction.id)) {
                }
                resourcesProvider2 = resourcesProvider;
                String string622 = LocaleController.getString(R.string.StarsTransactionDate);
                int i1022 = R.string.formatDateAtTime;
                tableView2.addRow(string622, LocaleController.formatString(i1022, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction.date * 1000))));
                starGift = starsTransaction.stargift;
                if (starGift != null) {
                }
                ?? r322 = obj;
                r322.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                if ((starsTransaction.flags & 32) != 0) {
                }
                LinkSpanDrawable.LinksTextView linksTextView322 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
                linksTextView322.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider2));
                linksTextView322.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider2));
                linksTextView322.setTextSize(1, 14.0f);
                linksTextView322.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$34(context);
                    }
                }));
                linksTextView322.setGravity(17);
                r322.addView(linksTextView322, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                ?? buttonWithCounterView22 = new ButtonWithCounterView(context, resourcesProvider2);
                if ((starsTransaction.flags & 32) == 0) {
                }
                buttonWithCounterView22.setText(string4, r4);
                r322.addView(buttonWithCounterView22, LayoutHelper.createLinear(-1, 48));
                BottomSheet.Builder builder322 = builder;
                builder322.setCustomView(r322);
                BottomSheet create22 = builder322.create();
                bottomSheetArr3[r4] = create22;
                create22.useBackgroundTopPadding = r4;
                if ((starsTransaction.flags & 32) == 0) {
                }
                bottomSheetArr3[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet()) {
                    bottomSheetArr3[0].makeAttached(safeLastFragment);
                }
                bottomSheetArr3[0].show();
                return bottomSheetArr3[0];
            }
            if (!starsTransaction.subscription || z) {
                string3 = LocaleController.getString(R.string.StarsTransactionRecipient);
                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$30(bottomSheetArr3, peerDialogId, context);
                    }
                };
            } else {
                string3 = LocaleController.getString(R.string.StarSubscriptionTo);
                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$29(bottomSheetArr3, peerDialogId, context);
                    }
                };
            }
            tableView = tableView2;
            i4 = i;
            tableView.addRowUser(string3, i4, peerDialogId, runnable);
            starsTransactionPeer = starsTransaction.peer;
            if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) {
            }
            if (TextUtils.isEmpty(starsTransaction.id)) {
            }
            resourcesProvider2 = resourcesProvider;
            String string6222 = LocaleController.getString(R.string.StarsTransactionDate);
            int i10222 = R.string.formatDateAtTime;
            tableView2.addRow(string6222, LocaleController.formatString(i10222, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction.date * 1000))));
            starGift = starsTransaction.stargift;
            if (starGift != null) {
            }
            ?? r3222 = obj;
            r3222.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
            if ((starsTransaction.flags & 32) != 0) {
            }
            LinkSpanDrawable.LinksTextView linksTextView3222 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
            linksTextView3222.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider2));
            linksTextView3222.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider2));
            linksTextView3222.setTextSize(1, 14.0f);
            linksTextView3222.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showTransactionSheet$34(context);
                }
            }));
            linksTextView3222.setGravity(17);
            r3222.addView(linksTextView3222, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
            ?? buttonWithCounterView222 = new ButtonWithCounterView(context, resourcesProvider2);
            if ((starsTransaction.flags & 32) == 0) {
            }
            buttonWithCounterView222.setText(string4, r4);
            r3222.addView(buttonWithCounterView222, LayoutHelper.createLinear(-1, 48));
            BottomSheet.Builder builder3222 = builder;
            builder3222.setCustomView(r3222);
            BottomSheet create222 = builder3222.create();
            bottomSheetArr3[r4] = create222;
            create222.useBackgroundTopPadding = r4;
            if ((starsTransaction.flags & 32) == 0) {
            }
            bottomSheetArr3[0].fixNavigationBar();
            safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (!AndroidUtilities.isTablet()) {
            }
            bottomSheetArr3[0].show();
            return bottomSheetArr3[0];
        } else {
            textView2.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
            i2 = R.string.StarsFailed;
        }
        appendStatus(spannableStringBuilder, textView2, LocaleController.getString(i2));
        textView2.setText(spannableStringBuilder);
        linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        if (z2) {
        }
        if (starsTransaction.sent_by != null) {
        }
        if (starsTransaction.sent_by != null) {
        }
        isUserSelf = UserObject.isUserSelf(user3);
        if (isUserSelf) {
        }
        LinkSpanDrawable.LinksTextView linksTextView5 = new LinkSpanDrawable.LinksTextView(context);
        linksTextView5.setTextColor(Theme.getColor(i6, resourcesProvider));
        linksTextView5.setTextSize(1, 16.0f);
        linksTextView5.setGravity(17);
        linksTextView5.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linksTextView5.setDisablePaddingsOffsetY(true);
        if (isUserSelf) {
        }
        SpannableStringBuilder replaceTags2 = AndroidUtilities.replaceTags(string);
        bottomSheetArr2 = bottomSheetArr;
        CharSequence replaceArrows2 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.GiftStarsSubtitleLinkName).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showTransactionSheet$21(context, bottomSheetArr2);
            }
        }), true);
        CharSequence[] charSequenceArr2 = new CharSequence[3];
        charSequenceArr2[c] = replaceTags2;
        charSequenceArr2[1] = " ";
        charSequenceArr2[2] = replaceArrows2;
        linksTextView5.setText(TextUtils.concat(charSequenceArr2));
        linearLayout2.addView(linksTextView5, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TableView tableView22 = new TableView(context, resourcesProvider);
        if (starsTransaction.stargift == null) {
        }
    }

    private void updateBalance() {
        StarsController starsController = StarsController.getInstance(this.currentAccount);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) this.starBalanceIcon);
        spannableStringBuilder.append((CharSequence) LocaleController.formatNumber(starsController.getBalance(), ','));
        this.starBalanceTextView.setText(spannableStringBuilder);
        this.buyButton.setText(LocaleController.getString(starsController.getBalance() > 0 ? R.string.StarsBuyMore : R.string.StarsBuy), true);
    }

    public boolean attachedTransactionsLayout() {
        StarsTransactionsLayout starsTransactionsLayout = this.transactionsLayout;
        if (starsTransactionsLayout == null || !(starsTransactionsLayout.getParent() instanceof View)) {
            return false;
        }
        return this.listView.getHeight() - ((View) this.transactionsLayout.getParent()).getBottom() >= 0;
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected RecyclerView.Adapter createAdapter() {
        UniversalAdapter universalAdapter = new UniversalAdapter(this.listView, getContext(), this.currentAccount, this.classGuid, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda43
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                StarsIntroActivity.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, getResourceProvider()) { // from class: org.telegram.ui.Stars.StarsIntroActivity.3
            @Override // org.telegram.ui.Components.UniversalAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                if (i == 42) {
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

    @Override // org.telegram.ui.GradientHeaderActivity
    protected GradientHeaderActivity.ContentView createContentView() {
        return new NestedFrameLayout(getContext());
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    public StarParticlesView createParticlesView() {
        return makeParticlesView(getContext(), 75, 1);
    }

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.useFillLastLayoutManager = false;
        this.particlesViewHeight = AndroidUtilities.dp(238.0f);
        this.transactionsLayout = new StarsTransactionsLayout(context, this.currentAccount, 0L, getClassGuid(), getResourceProvider());
        View view = new View(context) { // from class: org.telegram.ui.Stars.StarsIntroActivity.1
            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                int i3;
                StarsIntroActivity starsIntroActivity = StarsIntroActivity.this;
                if (starsIntroActivity.isLandscapeMode) {
                    i3 = (starsIntroActivity.statusBarHeight + ((BaseFragment) starsIntroActivity).actionBar.getMeasuredHeight()) - AndroidUtilities.dp(16.0f);
                } else {
                    int dp = AndroidUtilities.dp(140.0f);
                    StarsIntroActivity starsIntroActivity2 = StarsIntroActivity.this;
                    int i4 = dp + starsIntroActivity2.statusBarHeight;
                    if (starsIntroActivity2.backgroundView.getMeasuredHeight() + AndroidUtilities.dp(24.0f) > i4) {
                        i3 = AndroidUtilities.dp(24.0f) + StarsIntroActivity.this.backgroundView.getMeasuredHeight();
                    } else {
                        i3 = i4;
                    }
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((int) (i3 - (((GradientHeaderActivity) StarsIntroActivity.this).yOffset * 2.5f)), 1073741824));
            }
        };
        this.emptyLayout = view;
        view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray));
        super.createView(context);
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
        this.aboveTitleView.addView(this.iconTextureView, LayoutHelper.createFrame(NotificationCenter.storiesSendAsUpdate, 190.0f, 17, 0.0f, 32.0f, 0.0f, 24.0f));
        configureHeader(LocaleController.getString(R.string.TelegramStars), AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.TelegramStarsInfo2), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$createView$0(context);
            }
        }), true), this.aboveTitleView, null);
        this.listView.setOverScrollMode(2);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDurations(350L);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda27
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i) {
                StarsIntroActivity.this.lambda$createView$1(view2, i);
            }
        });
        FireworksOverlay fireworksOverlay = new FireworksOverlay(getContext());
        this.fireworksOverlay = fireworksOverlay;
        this.contentView.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
        StarsController.getInstance(this.currentAccount);
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.balanceLayout = linearLayout;
        linearLayout.setOrientation(1);
        this.balanceLayout.setPadding(0, 0, 0, AndroidUtilities.dp(10.0f));
        AnimatedTextView animatedTextView = new AnimatedTextView(getContext(), false, true, false);
        this.starBalanceTextView = animatedTextView;
        animatedTextView.setTypeface(AndroidUtilities.bold());
        this.starBalanceTextView.setTextSize(AndroidUtilities.dp(32.0f));
        this.starBalanceTextView.setGravity(17);
        this.starBalanceTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourceProvider));
        this.starBalanceIcon = new SpannableStringBuilder("S");
        ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(this.starBalanceTextView, this.currentAccount, 42.0f);
        ImageReceiver imageReceiver = imageReceiverSpan.imageReceiver;
        int i = R.raw.star_reaction;
        imageReceiver.setImageBitmap(new RLottieDrawable(i, "s" + i, AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f)));
        imageReceiverSpan.imageReceiver.setAutoRepeat(2);
        imageReceiverSpan.enableShadow(false);
        imageReceiverSpan.translate((float) (-AndroidUtilities.dp(3.0f)), 0.0f);
        this.starBalanceIcon.setSpan(imageReceiverSpan, 0, 1, 33);
        this.balanceLayout.addView(this.starBalanceTextView, LayoutHelper.createFrame(-1, 40.0f, 17, 24.0f, 0.0f, 24.0f, 0.0f));
        TextView textView = new TextView(getContext());
        this.starBalanceTitleView = textView;
        textView.setTextSize(1, 14.0f);
        this.starBalanceTitleView.setGravity(17);
        this.starBalanceTitleView.setText(LocaleController.getString(R.string.YourStarsBalance));
        this.starBalanceTitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, this.resourceProvider));
        this.balanceLayout.addView(this.starBalanceTitleView, LayoutHelper.createFrame(-1, -2.0f, 17, 24.0f, 0.0f, 24.0f, 0.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(getContext(), this.resourceProvider);
        this.buyButton = buttonWithCounterView;
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                StarsIntroActivity.this.lambda$createView$2(context, view2);
            }
        });
        this.balanceLayout.addView(this.buyButton, LayoutHelper.createFrame(-1, 48.0f, 17, 20.0f, 17.0f, 20.0f, 0.0f));
        this.giftButton = new ButtonWithCounterView(getContext(), false, this.resourceProvider);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "G  ");
        spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.menu_stars_gift), 0, 1, 33);
        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.TelegramStarsGift));
        this.giftButton.setText(spannableStringBuilder, false);
        this.giftButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                StarsIntroActivity.this.lambda$createView$3(view2);
            }
        });
        this.balanceLayout.addView(this.giftButton, LayoutHelper.createFrame(-1, 48.0f, 17, 20.0f, 8.0f, 20.0f, 0.0f));
        updateBalance();
        UniversalAdapter universalAdapter = this.adapter;
        if (universalAdapter != null) {
            universalAdapter.update(false);
        }
        return this.fragmentView;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0015, code lost:
        if (r1.savedScrollOffset < 0) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r1.savedScrollOffset < 0) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0042, code lost:
        r1.savedScrollOffset = 0;
     */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.starOptionsLoaded) {
            saveScrollPosition();
            UniversalAdapter universalAdapter = this.adapter;
            if (universalAdapter != null) {
                universalAdapter.update(true);
            }
            if (this.savedScrollPosition == 0) {
            }
            applyScrolledPosition();
        } else if (i != NotificationCenter.starTransactionsLoaded) {
            if (i != NotificationCenter.starSubscriptionsLoaded) {
                if (i == NotificationCenter.starBalanceUpdated) {
                    updateBalance();
                    return;
                }
                return;
            }
            UniversalAdapter universalAdapter2 = this.adapter;
            if (universalAdapter2 != null) {
                universalAdapter2.update(true);
            }
        } else {
            StarsController starsController = StarsController.getInstance(this.currentAccount);
            if (this.hadTransactions != starsController.hasTransactions()) {
                this.hadTransactions = starsController.hasTransactions();
                saveScrollPosition();
                UniversalAdapter universalAdapter3 = this.adapter;
                if (universalAdapter3 != null) {
                    universalAdapter3.update(true);
                }
                if (this.savedScrollPosition == 0) {
                }
                applyScrolledPosition();
            }
        }
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected boolean drawActionBarShadow() {
        return !attachedTransactionsLayout();
    }

    public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        UItem accent;
        if (getContext() == null) {
            return;
        }
        StarsController starsController = StarsController.getInstance(this.currentAccount);
        arrayList.add(UItem.asFullyCustom(getHeader(getContext())));
        arrayList.add(UItem.asCustom(this.balanceLayout));
        ButtonWithCounterView buttonWithCounterView = this.giftButton;
        if (buttonWithCounterView != null) {
            buttonWithCounterView.setVisibility(getMessagesController().starsGiftsEnabled ? 0 : 8);
        }
        arrayList.add(UItem.asShadow(null));
        if (starsController.hasSubscriptions()) {
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.StarMySubscriptions)));
            for (int i = 0; i < starsController.subscriptions.size(); i++) {
                arrayList.add(StarsSubscriptionView.Factory.asSubscription((TL_stars.StarsSubscription) starsController.subscriptions.get(i)));
            }
            if (starsController.isLoadingSubscriptions()) {
                accent = UItem.asFlicker(arrayList.size(), 33);
            } else {
                if (!starsController.didFullyLoadSubscriptions()) {
                    accent = UItem.asButton(-3, R.drawable.arrow_more, LocaleController.getString(R.string.StarMySubscriptionsExpand)).accent();
                }
                arrayList.add(UItem.asShadow(null));
            }
            arrayList.add(accent);
            arrayList.add(UItem.asShadow(null));
        }
        boolean hasTransactions = starsController.hasTransactions();
        this.hadTransactions = hasTransactions;
        arrayList.add(hasTransactions ? UItem.asFullscreenCustom(this.transactionsLayout, ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight) : UItem.asCustom(this.emptyLayout));
    }

    @Override // org.telegram.ui.GradientHeaderActivity
    protected View getHeader(Context context) {
        return super.getHeader(context);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return Theme.getColor(Theme.key_dialogBackgroundGray);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starOptionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starBalanceUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starTransactionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starSubscriptionsLoaded);
        StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        StarsController.getInstance(this.currentAccount).invalidateSubscriptions(true);
        StarsController.getInstance(this.currentAccount).getOptions();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starOptionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starBalanceUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starTransactionsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starSubscriptionsLoaded);
    }

    public void onItemClick(final UItem uItem, int i) {
        int i2 = uItem.id;
        if (i2 == -1) {
            this.expanded = !this.expanded;
        } else if (i2 == -2) {
            StarsController.getInstance(this.currentAccount).getGiftOptions();
            UserSelectorBottomSheet.open(1, 0L, BirthdayController.getInstance(this.currentAccount).getState());
            return;
        } else if (i2 != -3) {
            if (uItem.instanceOf(StarTierView.Factory.class)) {
                if (uItem.object instanceof TL_stars.TL_starsTopupOption) {
                    StarsController.getInstance(this.currentAccount).buy(getParentActivity(), (TL_stars.TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda50
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj, Object obj2) {
                            StarsIntroActivity.this.lambda$onItemClick$4(uItem, (Boolean) obj, (String) obj2);
                        }
                    });
                    return;
                }
                return;
            } else if (uItem.instanceOf(StarsSubscriptionView.Factory.class) && (uItem.object instanceof TL_stars.StarsSubscription)) {
                showSubscriptionSheet(getContext(), this.currentAccount, (TL_stars.StarsSubscription) uItem.object, getResourceProvider());
                return;
            } else {
                return;
            }
        } else {
            StarsController.getInstance(this.currentAccount).loadSubscriptions();
        }
        this.adapter.update(true);
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

    @Override // org.telegram.ui.GradientHeaderActivity, org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        GLIconTextureView gLIconTextureView = this.iconTextureView;
        if (gLIconTextureView != null) {
            gLIconTextureView.setPaused(false);
            this.iconTextureView.setDialogVisible(false);
        }
    }
}
