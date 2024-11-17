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
import android.net.Uri;
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
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
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
import org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda11;
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
import org.telegram.ui.Components.ButtonSpan;
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
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.GradientHeaderActivity;
import org.telegram.ui.ImageReceiverSpan;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.HintView2;

/* loaded from: classes4.dex */
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

    class 2 extends StarParticlesView {
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

    public static class ExpandView extends FrameLayout {
        public final ImageView arrowView;
        private int lastId;
        private boolean needDivider;
        public final AnimatedTextView textView;

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

    public static class GiftStarsSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private final HeaderView headerView;
        private final TLRPC.User user;
        private final Runnable whenPurchased;

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
                }
            } else {
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
    class NestedFrameLayout extends GradientHeaderActivity.ContentView implements NestedScrollingParent3 {
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
                            return;
                        } else {
                            iArr[1] = i2 - Math.max(top, i2);
                            return;
                        }
                    }
                    return;
                }
                if (isSearchFieldVisible) {
                    RecyclerListView currentListView2 = StarsIntroActivity.this.transactionsLayout.getCurrentListView();
                    iArr[1] = i2;
                    if (top > 0) {
                        iArr[1] = i2 - i2;
                    }
                    if (currentListView2 == null || (i4 = iArr[1]) <= 0) {
                        return;
                    }
                    currentListView2.scrollBy(0, i4);
                    return;
                }
                if (i2 > 0) {
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

    public static class StarTierView extends FrameLayout {
        private final AnimatedFloat animatedStarsCount;
        private SpannableString loading;
        private boolean needDivider;
        private final Drawable starDrawable;
        private final Drawable starDrawableOutline;
        private int starsCount;
        private final TextView textView;
        private final AnimatedTextView textView2;

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

    public static class StarsNeededSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;
        private final HeaderView headerView;
        private final long starsNeeded;
        private Runnable whenPurchased;

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
            } else {
                if (i != 2) {
                    if (i == 7) {
                        str2 = "StarsNeededTextKeepBotSubscription";
                    } else if (i == 8) {
                        str2 = "StarsNeededTextKeepBizSubscription";
                    } else if (i != 3) {
                        if (i == 4) {
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
                            str2 = i == 5 ? "StarsNeededTextReactions" : i == 6 ? "StarsNeededTextGift" : i == 9 ? "StarsNeededBizText" : "StarsNeededText";
                        }
                    }
                }
                str2 = "StarsNeededTextKeepSubscription";
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

        /* JADX WARN: Code restructure failed: missing block: B:41:0x00c2, code lost:
        
            if (r15 != false) goto L37;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00c4, code lost:
        
            r15 = org.telegram.messenger.R.string.NotifyLessOptions;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00c7, code lost:
        
            r15 = org.telegram.messenger.R.string.NotifyMoreOptions;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x00db, code lost:
        
            if (r15 != false) goto L37;
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

    public static class StarsOptionsSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
        private final int BUTTON_EXPAND;
        private UniversalAdapter adapter;
        private boolean expanded;
        private final FireworksOverlay fireworksOverlay;
        private final FrameLayout footerView;

        public StarsOptionsSheet(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, null, false, false, false, resourcesProvider);
            this.BUTTON_EXPAND = -1;
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
                }
            } else {
                BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.StarsAcquired), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsAcquiredInfo", (int) uItem.longValue, new Object[0]))).show();
                LaunchActivity launchActivity = LaunchActivity.instance;
                if (launchActivity != null) {
                    launchActivity.getFireworksOverlay().start(true);
                }
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

    public static class StarsSubscriptionView extends LinearLayout {
        private final int currentAccount;
        public final BackupImageView imageView;
        private boolean needDivider;
        public final LinearLayout priceLayout;
        public final TextView priceSubtitleView;
        public final TextView priceTitleView;
        public final TextView productView;
        private final Theme.ResourcesProvider resourcesProvider;
        public final TextView subtitleView;
        public final LinearLayout textLayout;
        private boolean threeLines;
        public final SimpleTextView titleView;

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
            NotificationCenter.listenEmojiLoading(simpleTextView);
            linearLayout.addView(simpleTextView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 2.0f));
            TextView textView = new TextView(context);
            this.productView = textView;
            textView.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView.setTextSize(1, 13.0f);
            textView.setVisibility(8);
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 1.0f));
            TextView textView2 = new TextView(context);
            this.subtitleView = textView2;
            int i3 = Theme.key_windowBackgroundWhiteGrayText2;
            textView2.setTextColor(Theme.getColor(i3, resourcesProvider));
            textView2.setTextSize(1, 14.0f);
            linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.priceLayout = linearLayout2;
            linearLayout2.setOrientation(1);
            addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 0.0f, 16, 0, 0, 18, 0));
            TextView textView3 = new TextView(context);
            this.priceTitleView = textView3;
            textView3.setTextColor(Theme.getColor(i2, resourcesProvider));
            textView3.setTextSize(1, 16.0f);
            textView3.setTypeface(AndroidUtilities.bold());
            textView3.setGravity(5);
            linearLayout2.addView(textView3, LayoutHelper.createLinear(-1, -2, 5, 0, 0, 0, 1));
            TextView textView4 = new TextView(context);
            this.priceSubtitleView = textView4;
            textView4.setTextColor(Theme.getColor(i3, resourcesProvider));
            textView4.setTextSize(1, 13.0f);
            textView4.setGravity(5);
            linearLayout2.addView(textView4, LayoutHelper.createLinear(-1, -2, 5, 0, 0, 0, 0));
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
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.threeLines ? 68.0f : 58.0f), 1073741824));
        }

        public void set(TL_stars.StarsSubscription starsSubscription, boolean z) {
            boolean z2;
            String str;
            TextView textView;
            String str2;
            int i;
            long peerDialogId = DialogObject.getPeerDialogId(starsSubscription.peer);
            this.threeLines = !TextUtils.isEmpty(starsSubscription.title);
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            if (peerDialogId < 0) {
                TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-peerDialogId));
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setInfo(chat);
                this.imageView.setForUserOrChat(chat, avatarDrawable);
                str = chat != null ? chat.title : null;
                z2 = false;
            } else {
                TLRPC.User user = messagesController.getUser(Long.valueOf(peerDialogId));
                AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                avatarDrawable2.setInfo(user);
                this.imageView.setForUserOrChat(user, avatarDrawable2);
                String userName = UserObject.getUserName(user);
                z2 = !UserObject.isBot(user);
                str = userName;
            }
            long currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            SimpleTextView simpleTextView = this.titleView;
            simpleTextView.setText(Emoji.replaceEmoji(str, simpleTextView.getPaint().getFontMetricsInt(), false));
            if (TextUtils.isEmpty(starsSubscription.title)) {
                this.productView.setVisibility(8);
            } else {
                this.productView.setVisibility(0);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                if (starsSubscription.photo != null) {
                    ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(this.productView, this.currentAccount, 14.0f);
                    imageReceiverSpan.setRoundRadius(4.0f);
                    imageReceiverSpan.enableShadow(false);
                    SpannableString spannableString = new SpannableString("x");
                    spannableString.setSpan(imageReceiverSpan, 0, 1, 33);
                    imageReceiverSpan.imageReceiver.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsSubscription.photo)), "14_14", null, null, 0, 0);
                    spannableStringBuilder.append((CharSequence) spannableString).append((CharSequence) " ");
                }
                spannableStringBuilder.append(Emoji.replaceEmoji(starsSubscription.title, this.titleView.getPaint().getFontMetricsInt(), false));
                this.productView.setText(spannableStringBuilder);
            }
            this.subtitleView.setTextSize(1, this.threeLines ? 13.0f : 14.0f);
            if (starsSubscription.canceled || starsSubscription.bot_canceled) {
                TextView textView2 = this.subtitleView;
                long j = starsSubscription.until_date;
                textView2.setText(LocaleController.formatString(j < currentTime ? R.string.StarsSubscriptionExpired : R.string.StarsSubscriptionExpires, LocaleController.formatDateChat(j)));
                this.priceTitleView.setVisibility(8);
                this.priceSubtitleView.setTextColor(Theme.getColor(Theme.key_color_red, this.resourcesProvider));
                this.priceSubtitleView.setText(LocaleController.getString(starsSubscription.bot_canceled ? z2 ? R.string.StarsSubscriptionStatusBizCancelled : R.string.StarsSubscriptionStatusBotCancelled : R.string.StarsSubscriptionStatusCancelled));
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
                    if (i2 == 2592000) {
                        textView = this.priceSubtitleView;
                        i = R.string.StarsParticipantSubscriptionPerMonth;
                    } else {
                        if (i2 == 60) {
                            textView = this.priceSubtitleView;
                            str2 = "per minute";
                        } else if (i2 == 300) {
                            textView = this.priceSubtitleView;
                            str2 = "per 5 minutes";
                        }
                        textView.setText(str2);
                    }
                }
                str2 = LocaleController.getString(i);
                textView.setText(str2);
            }
            this.needDivider = z;
            setWillNotDraw(!z);
        }
    }

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
            if (combinedDrawable != null) {
                return combinedDrawable;
            }
            HashMap hashMap = cachedPlatformDrawables;
            CombinedDrawable createDrawable = SessionCell.createDrawable(44, str);
            hashMap.put(str, createDrawable);
            return createDrawable;
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

        public void set(TL_stars.StarsTransaction starsTransaction, boolean z, boolean z2) {
            TextView textView;
            CharSequence concat;
            BackupImageView backupImageView;
            String str;
            TextView textView2;
            CharSequence concat2;
            boolean z3;
            String str2;
            boolean z4;
            String str3;
            TextView textView3;
            CharSequence replaceEmoji;
            TextView textView4;
            ImageLocation imageLocation;
            ImageLocation forDocument;
            TextView textView5;
            int i;
            CharSequence string;
            long peerDialogId = DialogObject.getPeerDialogId(starsTransaction.peer.peer);
            boolean z5 = peerDialogId != 0 || starsTransaction.subscription || starsTransaction.floodskip || starsTransaction.stargift != null || (starsTransaction.gift && (starsTransaction.peer instanceof TL_stars.TL_starsTransactionPeerFragment));
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
            if (peerDialogId != 0) {
                if (UserObject.isService(peerDialogId)) {
                    str3 = LocaleController.getString(R.string.StarsTransactionUnknown);
                    this.imageView.setImageDrawable(getPlatformDrawable("fragment"));
                    z4 = false;
                } else {
                    if (peerDialogId >= 0) {
                        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId));
                        z3 = user == null;
                        this.avatarDrawable.setInfo(user);
                        this.imageView.setForUserOrChat(user, this.avatarDrawable);
                        str2 = UserObject.getUserName(user);
                    } else {
                        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId));
                        z3 = chat == null;
                        this.avatarDrawable.setInfo(chat);
                        this.imageView.setForUserOrChat(chat, this.avatarDrawable);
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
                if (starsTransaction.stargift != null) {
                    ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(this.subtitleTextView, this.currentAccount, 16.0f);
                    imageReceiverSpan.setRoundRadius(4.0f);
                    imageReceiverSpan.enableShadow(false);
                    SpannableString spannableString = new SpannableString("x");
                    spannableString.setSpan(imageReceiverSpan, 0, 1, 33);
                    StarsIntroActivity.setGiftImage(imageReceiverSpan.imageReceiver, starsTransaction.stargift, 16);
                    this.titleTextView.setText(str3);
                    if (starsTransaction.refund) {
                        textView5 = this.subtitleTextView;
                        string = TextUtils.concat(spannableString, " ", LocaleController.getString(starsTransaction.stars > 0 ? R.string.Gift2TransactionRefundedSent : R.string.Gift2TransactionRefundedConverted));
                    } else {
                        textView5 = this.subtitleTextView;
                        string = TextUtils.concat(spannableString, " ", LocaleController.getString(starsTransaction.stars > 0 ? R.string.Gift2TransactionConverted : R.string.Gift2TransactionSent));
                    }
                } else if (starsTransaction.subscription) {
                    this.titleTextView.setText(str3);
                    int i2 = starsTransaction.subscription_period;
                    if (i2 == 2592000) {
                        this.subtitleTextView.setVisibility(0);
                        textView5 = this.subtitleTextView;
                        i = R.string.StarsTransactionSubscriptionMonthly;
                        string = LocaleController.getString(i);
                    } else {
                        String str4 = i2 == 300 ? "5 minutes" : "Minute";
                        this.subtitleTextView.setVisibility(0);
                        this.subtitleTextView.setText(String.format(Locale.US, "%s subscription fee", str4));
                    }
                } else {
                    if (starsTransaction.gift) {
                        this.titleTextView.setText(str3);
                        this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                        textView5 = this.subtitleTextView;
                        i = R.string.StarsGiftReceived;
                    } else if ((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0) {
                        this.titleTextView.setText(str3);
                        this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                        textView5 = this.subtitleTextView;
                        i = R.string.StarsGiveawayPrizeReceived;
                    } else if (starsTransaction.reaction) {
                        this.titleTextView.setText(str3);
                        this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                        textView5 = this.subtitleTextView;
                        i = R.string.StarsReactionsSent;
                    } else if (starsTransaction.extended_media.isEmpty()) {
                        if (starsTransaction.photo != null) {
                            ImageReceiverSpan imageReceiverSpan2 = new ImageReceiverSpan(this.subtitleTextView, this.currentAccount, 14.0f);
                            imageReceiverSpan2.setRoundRadius(4.0f);
                            imageReceiverSpan2.enableShadow(false);
                            SpannableString spannableString2 = new SpannableString("x");
                            spannableString2.setSpan(imageReceiverSpan2, 0, 1, 33);
                            imageReceiverSpan2.imageReceiver.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsTransaction.photo)), "14_14", null, null, 0, 0);
                            this.titleTextView.setText(str3);
                            this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                            textView3 = this.subtitleTextView;
                            String str5 = starsTransaction.title;
                            if (str5 == null) {
                                str5 = "";
                            }
                            replaceEmoji = Emoji.replaceEmoji(TextUtils.concat(spannableString2, " ", str5), this.subtitleTextView.getPaint().getFontMetricsInt(), false);
                        } else {
                            this.titleTextView.setText(str3);
                            this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                            textView3 = this.subtitleTextView;
                            String str6 = starsTransaction.title;
                            if (str6 == null) {
                                str6 = "";
                            }
                            replaceEmoji = Emoji.replaceEmoji(str6, textView3.getPaint().getFontMetricsInt(), false);
                        }
                        textView3.setText(replaceEmoji);
                    } else {
                        if (z) {
                            this.titleTextView.setText(str3);
                            this.subtitleTextView.setVisibility(0);
                            textView4 = this.subtitleTextView;
                            str3 = LocaleController.getString(R.string.StarMediaPurchase);
                        } else {
                            this.titleTextView.setText(LocaleController.getString(R.string.StarMediaPurchase));
                            this.subtitleTextView.setVisibility(z4 ? 8 : 0);
                            textView4 = this.subtitleTextView;
                        }
                        textView4.setText(str3);
                        this.imageViewCount = 0;
                        int i3 = 0;
                        while (i3 < Math.min(2, starsTransaction.extended_media.size())) {
                            TLRPC.MessageMedia messageMedia = starsTransaction.extended_media.get(i3);
                            BackupImageView backupImageView2 = i3 == 0 ? this.imageView : this.imageView2;
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
                                i3++;
                            }
                            imageLocation = forDocument;
                            backupImageView2.setVisibility(0);
                            backupImageView2.setImage(imageLocation, "46_46", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                            this.imageViewCount++;
                            i3++;
                        }
                        int i4 = 0;
                        while (i4 < this.imageViewCount) {
                            BackupImageView backupImageView3 = i4 == 0 ? this.imageView : this.imageView2;
                            float f = i4;
                            backupImageView3.setTranslationX(AndroidUtilities.dp(2.0f) + ((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f)));
                            backupImageView3.setTranslationY((f - (this.imageViewCount / 2.0f)) * AndroidUtilities.dp(4.33f));
                            i4++;
                        }
                    }
                    string = LocaleController.getString(i);
                }
                textView5.setText(string);
            } else {
                if (starsTransaction.floodskip) {
                    this.titleTextView.setText(LocaleController.getString(R.string.StarsTransactionFloodskip));
                    this.subtitleTextView.setText(LocaleController.formatPluralStringComma("StarsTransactionFloodskipMessages", starsTransaction.floodskip_number));
                    backupImageView = this.imageView;
                    str = "api";
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
                }
                backupImageView.setImageDrawable(getPlatformDrawable(str));
            }
            long j = starsTransaction.stars;
            if (j > 0) {
                this.amountTextView.setVisibility(0);
                this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_green));
                textView2 = this.amountTextView;
                concat2 = TextUtils.concat("+", LocaleController.formatNumber(starsTransaction.stars, ' '), " ", this.star);
            } else if (j >= 0) {
                this.amountTextView.setVisibility(8);
                this.needDivider = z2;
                setWillNotDraw(!z2);
            } else {
                this.amountTextView.setVisibility(0);
                this.amountTextView.setTextColor(Theme.getColor(Theme.key_color_red));
                textView2 = this.amountTextView;
                concat2 = TextUtils.concat("-", LocaleController.formatNumber(-starsTransaction.stars, ' '), " ", this.star);
            }
            textView2.setText(concat2);
            this.needDivider = z2;
            setWillNotDraw(!z2);
        }
    }

    public static class StarsTransactionsLayout extends LinearLayout implements NotificationCenter.NotificationCenterDelegate {
        private final PageAdapter adapter;
        private final long bot_id;
        private final int currentAccount;
        private final ViewPagerFixed.TabsView tabsView;
        private final ViewPagerFixed viewPager;

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
                long j = this.bot_id;
                int i = this.currentAccount;
                if (j == 0) {
                    StarsController starsController = StarsController.getInstance(i);
                    this.items.add(UItem.asSpace(0));
                    if (starsController.hasTransactions(1)) {
                        this.items.add(UItem.asSpace(1));
                    }
                    if (!starsController.hasTransactions(2)) {
                        return;
                    }
                } else {
                    BotStarsController botStarsController = BotStarsController.getInstance(i);
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
                } else {
                    if (itemViewType != 2) {
                        return "";
                    }
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
        loadingSpan.setColors(Theme.multAlpha(textView.getPaint().getColor(), 0.21f), Theme.multAlpha(textView.getPaint().getColor(), 0.08f));
        spannableStringBuilder.setSpan(loadingSpan, 0, 1, 33);
        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        if (!starGift.sold_out) {
            StarsController.getInstance(i).getStarGift(starGift.id, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda28
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    StarsIntroActivity.lambda$addAvailabilityRow$104(textView, (TL_stars.StarGift) obj);
                }
            });
        } else {
            int i2 = starGift.availability_remains;
            textView.setText(i2 <= 0 ? LocaleController.formatPluralStringComma("Gift2Availability2ValueNone", starGift.availability_total) : LocaleController.formatPluralStringComma("Gift2Availability4Value", i2, LocaleController.formatNumber(starGift.availability_total, ',')));
        }
    }

    private static CharSequence appendStatus(SpannableStringBuilder spannableStringBuilder, TextView textView, String str) {
        spannableStringBuilder.append(" ");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ReplacementSpan(textView.getCurrentTextColor(), str) { // from class: org.telegram.ui.Stars.StarsIntroActivity.12
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
                rectF.set(f, (r12 - AndroidUtilities.dp(20.0f)) / 2.0f, AndroidUtilities.dp(12.0f) + f + this.layout.getCurrentWidth(), (AndroidUtilities.dp(20.0f) + r12) / 2.0f);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.backgroundPaint);
                this.layout.draw(canvas, f + AndroidUtilities.dp(6.0f), (i3 + i5) / 2.0f, this.val$color, 1.0f);
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
        if (starsTransaction.floodskip) {
            return LocaleController.getString(R.string.StarsTransactionFloodskip);
        }
        if (!starsTransaction.extended_media.isEmpty()) {
            return LocaleController.getString(R.string.StarMediaPurchase);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addAvailabilityRow$104(TextView textView, TL_stars.StarGift starGift) {
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
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$10(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
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
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$6(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$7(BottomSheet bottomSheet, ButtonWithCounterView buttonWithCounterView) {
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$8(final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, Boolean bool) {
        if (bool.booleanValue()) {
            bottomSheet.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda70
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$openConfirmPurchaseSheet$7(BottomSheet.this, buttonWithCounterView);
                }
            }, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openConfirmPurchaseSheet$9(Utilities.Callback callback, final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, View view) {
        if (callback == null) {
            bottomSheet.dismiss();
            return;
        }
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(true);
        callback.run(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda46
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$8(BottomSheet.this, buttonWithCounterView, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$11(StarsBalanceView starsBalanceView, View view) {
        BaseFragment lastFragment;
        if (starsBalanceView.lastBalance > 0 && (lastFragment = LaunchActivity.getLastFragment()) != null) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new StarsIntroActivity(), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$12(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsSubscribeInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$13(BottomSheet bottomSheet, ButtonWithCounterView buttonWithCounterView) {
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$14(final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, Boolean bool) {
        if (bool.booleanValue()) {
            bottomSheet.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda98
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$openStarsChannelInviteSheet$13(BottomSheet.this, buttonWithCounterView);
                }
            }, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$15(Utilities.Callback callback, final BottomSheet bottomSheet, final ButtonWithCounterView buttonWithCounterView, View view) {
        if (callback == null) {
            bottomSheet.dismiss();
            return;
        }
        bottomSheet.setCanDismissWithSwipe(false);
        buttonWithCounterView.setLoading(true);
        callback.run(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda93
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$14(BottomSheet.this, buttonWithCounterView, (Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStarsChannelInviteSheet$16(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setGiftImage$17(int i, int i2, ImageReceiver imageReceiver, final boolean[] zArr) {
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
    public static /* synthetic */ void lambda$setGiftImage$20(Runnable runnable, Runnable runnable2) {
        runnable.run();
        runnable2.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$74(Context context) {
        new ExplainStarsSheet(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$75(BottomSheet[] bottomSheetArr, long j, long j2) {
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
    public static /* synthetic */ void lambda$showActionGiftSheet$76(Context context, int i, long j, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        Objects.requireNonNull(bottomSheet);
        new GiftSheet(context, i, j, new BottomSheet$$ExternalSyntheticLambda11(bottomSheet)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$77(BottomSheet[] bottomSheetArr, long j, long j2) {
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
    public static /* synthetic */ void lambda$showActionGiftSheet$78(Context context, int i, long j, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        Objects.requireNonNull(bottomSheet);
        new GiftSheet(context, i, j, new BottomSheet$$ExternalSyntheticLambda11(bottomSheet)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$79(StarsIntroActivity starsIntroActivity, TLRPC.TL_messageActionStarGift tL_messageActionStarGift) {
        BulletinFactory.of(starsIntroActivity).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) tL_messageActionStarGift.convert_stars)).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$80(AlertDialog alertDialog, TLObject tLObject, BottomSheet[] bottomSheetArr, int i, long j, long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider) {
        BulletinFactory of;
        String string;
        alertDialog.dismissUnless(400L);
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
        StarsController.getInstance(i).invalidateProfileGifts(j);
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
        starsIntroActivity.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda100
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$79(StarsIntroActivity.this, tL_messageActionStarGift);
            }
        });
        safeLastFragment.presentFragment(starsIntroActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$81(final AlertDialog alertDialog, final BottomSheet[] bottomSheetArr, final int i, final long j, final long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda92
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$80(AlertDialog.this, tLObject, bottomSheetArr, i, j, j2, tL_messageActionStarGift, tL_error, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$82(int i, final int i2, final long j, final BottomSheet[] bottomSheetArr, final long j2, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i3) {
        final AlertDialog alertDialog = new AlertDialog(ApplicationLoader.applicationContext, 3);
        alertDialog.showDelayed(500L);
        TL_stars.convertStarGift convertstargift = new TL_stars.convertStarGift();
        convertstargift.msg_id = i;
        convertstargift.user_id = MessagesController.getInstance(i2).getInputUser(j);
        ConnectionsManager.getInstance(i2).sendRequest(convertstargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda86
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showActionGiftSheet$81(AlertDialog.this, bottomSheetArr, i2, j, j2, tL_messageActionStarGift, resourcesProvider, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$83(Context context, final Theme.ResourcesProvider resourcesProvider, int i, TLRPC.User user, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final int i2, final int i3, final long j, final BottomSheet[] bottomSheetArr, final long j2) {
        new AlertDialog.Builder(context, resourcesProvider).setTitle(LocaleController.getString(R.string.Gift2ConvertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatPluralString("Gift2ConvertText2", i, UserObject.getForcedFirstName(user), LocaleController.formatPluralStringComma("Gift2ConvertStars", (int) tL_messageActionStarGift.convert_stars)))).setPositiveButton(LocaleController.getString(R.string.Gift2ConvertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda68
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                StarsIntroActivity.lambda$showActionGiftSheet$82(i2, i3, j, bottomSheetArr, j2, tL_messageActionStarGift, resourcesProvider, dialogInterface, i4);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$84(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$85(TLObject tLObject, BottomSheet[] bottomSheetArr, final int i, long j, TL_stars.StarGift starGift, boolean z, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider) {
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            bottomSheetArr[0].dismiss();
            StarsController.getInstance(i).invalidateProfileGifts(j);
            BulletinFactory.of(safeLastFragment).createEmojiBulletin(starGift.sticker, LocaleController.getString(z ? R.string.Gift2MadePrivateTitle : R.string.Gift2MadePublicTitle), AndroidUtilities.replaceSingleTag(LocaleController.getString(z ? R.string.Gift2MadePrivate : R.string.Gift2MadePublic), safeLastFragment instanceof ProfileActivity ? null : new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showActionGiftSheet$84(i, safeLastFragment);
                }
            })).show(true);
        } else if (tL_error != null) {
            BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createErrorBulletin(LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text)).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$86(final BottomSheet[] bottomSheetArr, final int i, final long j, final TL_stars.StarGift starGift, final boolean z, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda87
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$85(TLObject.this, bottomSheetArr, i, j, starGift, z, tL_error, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$87(ButtonWithCounterView buttonWithCounterView, TLRPC.TL_messageActionStarGift tL_messageActionStarGift, int i, final int i2, long j, final BottomSheet[] bottomSheetArr, final long j2, final TL_stars.StarGift starGift, final Theme.ResourcesProvider resourcesProvider) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.saveStarGift savestargift = new TL_stars.saveStarGift();
        final boolean z = tL_messageActionStarGift.saved;
        savestargift.unsave = z;
        savestargift.msg_id = i;
        savestargift.user_id = MessagesController.getInstance(i2).getInputUser(j);
        ConnectionsManager.getInstance(i2).sendRequest(savestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda66
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showActionGiftSheet$86(bottomSheetArr, i2, j2, starGift, z, resourcesProvider, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$88(BottomSheet[] bottomSheetArr, long j) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        bottomSheetArr[0].dismiss();
        if ((safeLastFragment instanceof ProfileActivity) && ((ProfileActivity) safeLastFragment).myProfile) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", j);
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        safeLastFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showActionGiftSheet$89(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$58(BottomSheet[] bottomSheetArr, long j) {
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
    public static /* synthetic */ void lambda$showBoostsSheet$59(BottomSheet[] bottomSheetArr, long j, TL_stories.Boost boost) {
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
    public static /* synthetic */ void lambda$showBoostsSheet$60(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBoostsSheet$61(BottomSheet[] bottomSheetArr, View view) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$100(final BottomSheet[] bottomSheetArr, final int i, final long j, final TL_stars.StarGift starGift, final boolean z, final Theme.ResourcesProvider resourcesProvider, final ButtonWithCounterView buttonWithCounterView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda91
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$99(TLObject.this, bottomSheetArr, i, j, starGift, z, tL_error, resourcesProvider, buttonWithCounterView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$101(final ButtonWithCounterView buttonWithCounterView, TL_stars.UserStarGift userStarGift, final int i, final BottomSheet[] bottomSheetArr, final long j, final TL_stars.StarGift starGift, final Theme.ResourcesProvider resourcesProvider) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.saveStarGift savestargift = new TL_stars.saveStarGift();
        final boolean z = !userStarGift.unsaved;
        savestargift.unsave = z;
        savestargift.msg_id = userStarGift.msg_id;
        savestargift.user_id = MessagesController.getInstance(i).getInputUser(userStarGift.from_id);
        ConnectionsManager.getInstance(i).sendRequest(savestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda77
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showGiftSheet$100(bottomSheetArr, i, j, starGift, z, resourcesProvider, buttonWithCounterView, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$102(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$90(Context context) {
        new ExplainStarsSheet(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$91(BottomSheet[] bottomSheetArr, long j, TL_stars.UserStarGift userStarGift, long j2) {
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
    public static /* synthetic */ void lambda$showGiftSheet$92(Context context, int i, long j, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        Objects.requireNonNull(bottomSheet);
        new GiftSheet(context, i, j, new BottomSheet$$ExternalSyntheticLambda11(bottomSheet)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$93(StarsIntroActivity starsIntroActivity, TL_stars.UserStarGift userStarGift) {
        BulletinFactory.of(starsIntroActivity).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) userStarGift.convert_stars)).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$94(AlertDialog alertDialog, TLObject tLObject, BottomSheet[] bottomSheetArr, int i, long j, long j2, final TL_stars.UserStarGift userStarGift, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider) {
        BulletinFactory of;
        String string;
        alertDialog.dismissUnless(400L);
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
        StarsController.getInstance(i).invalidateProfileGifts(j);
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
        starsIntroActivity.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda104
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$93(StarsIntroActivity.this, userStarGift);
            }
        });
        safeLastFragment.presentFragment(starsIntroActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$95(final AlertDialog alertDialog, final BottomSheet[] bottomSheetArr, final int i, final long j, final long j2, final TL_stars.UserStarGift userStarGift, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda103
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$94(AlertDialog.this, tLObject, bottomSheetArr, i, j, j2, userStarGift, tL_error, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$96(final TL_stars.UserStarGift userStarGift, final int i, final BottomSheet[] bottomSheetArr, final long j, final long j2, final Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i2) {
        final AlertDialog alertDialog = new AlertDialog(ApplicationLoader.applicationContext, 3);
        alertDialog.showDelayed(500L);
        TL_stars.convertStarGift convertstargift = new TL_stars.convertStarGift();
        convertstargift.msg_id = userStarGift.msg_id;
        convertstargift.user_id = MessagesController.getInstance(i).getInputUser(userStarGift.from_id);
        ConnectionsManager.getInstance(i).sendRequest(convertstargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda95
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showGiftSheet$95(AlertDialog.this, bottomSheetArr, i, j, j2, userStarGift, resourcesProvider, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$97(Context context, final Theme.ResourcesProvider resourcesProvider, int i, TLRPC.User user, long j, final TL_stars.UserStarGift userStarGift, final int i2, final BottomSheet[] bottomSheetArr, final long j2, final long j3) {
        new AlertDialog.Builder(context, resourcesProvider).setTitle(LocaleController.getString(R.string.Gift2ConvertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatPluralString("Gift2ConvertText2", i, (user == null || UserObject.isService(j)) ? LocaleController.getString(R.string.StarsTransactionHidden) : UserObject.getForcedFirstName(user), LocaleController.formatPluralStringComma("Gift2ConvertStars", (int) userStarGift.convert_stars)))).setPositiveButton(LocaleController.getString(R.string.Gift2ConvertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda84
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                StarsIntroActivity.lambda$showGiftSheet$96(TL_stars.UserStarGift.this, i2, bottomSheetArr, j2, j3, resourcesProvider, dialogInterface, i3);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$98(int i, BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(i).getClientUserId());
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showGiftSheet$99(TLObject tLObject, BottomSheet[] bottomSheetArr, final int i, long j, TL_stars.StarGift starGift, boolean z, TLRPC.TL_error tL_error, Theme.ResourcesProvider resourcesProvider, ButtonWithCounterView buttonWithCounterView) {
        BulletinFactory of;
        String string;
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            bottomSheetArr[0].dismiss();
            StarsController.getInstance(i).invalidateProfileGifts(j);
            BulletinFactory.of(safeLastFragment).createEmojiBulletin(starGift.sticker, LocaleController.getString(z ? R.string.Gift2MadePrivateTitle : R.string.Gift2MadePublicTitle), AndroidUtilities.replaceSingleTag(LocaleController.getString(z ? R.string.Gift2MadePrivate : R.string.Gift2MadePublic), safeLastFragment instanceof ProfileActivity ? null : new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showGiftSheet$98(i, safeLastFragment);
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
    public static /* synthetic */ void lambda$showMediaPriceSheet$63(OutlineTextContainerView outlineTextContainerView, EditTextBoldCursor editTextBoldCursor, View view, boolean z) {
        outlineTextContainerView.animateSelection(z, !TextUtils.isEmpty(editTextBoldCursor.getText()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$64(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.PaidContentInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$65(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showMediaPriceSheet$66(boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        if (zArr[0]) {
            return true;
        }
        if (callback2 != null) {
            zArr[0] = true;
            buttonWithCounterView.setLoading(true);
            callback2.run(Long.valueOf(Long.parseLong(editTextBoldCursor.getText().toString())), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda64
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showMediaPriceSheet$65(EditTextBoldCursor.this, bottomSheetArr);
                }
            });
        } else {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            bottomSheetArr[0].dismiss();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$67(EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$68(boolean[] zArr, Utilities.Callback2 callback2, final EditTextBoldCursor editTextBoldCursor, ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 == null) {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            bottomSheetArr[0].dismiss();
        } else {
            String obj = editTextBoldCursor.getText().toString();
            zArr[0] = true;
            buttonWithCounterView.setLoading(true);
            callback2.run(Long.valueOf(TextUtils.isEmpty(obj) ? 0L : Long.parseLong(obj)), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showMediaPriceSheet$67(EditTextBoldCursor.this, bottomSheetArr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$69(boolean[] zArr, EditTextBoldCursor editTextBoldCursor, BottomSheet[] bottomSheetArr) {
        zArr[0] = false;
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$70(final boolean[] zArr, Utilities.Callback2 callback2, ButtonWithCounterView buttonWithCounterView, final EditTextBoldCursor editTextBoldCursor, final BottomSheet[] bottomSheetArr, View view) {
        if (zArr[0]) {
            return;
        }
        if (callback2 == null) {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            bottomSheetArr[0].dismiss();
        } else {
            zArr[0] = true;
            buttonWithCounterView.setLoading(true);
            callback2.run(0L, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showMediaPriceSheet$69(zArr, editTextBoldCursor, bottomSheetArr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showMediaPriceSheet$73(BottomSheet[] bottomSheetArr, final EditTextBoldCursor editTextBoldCursor) {
        bottomSheetArr[0].setFocusable(true);
        editTextBoldCursor.requestFocus();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSoldOutGiftSheet$103(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$40(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$41(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, long j) {
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
    public static /* synthetic */ void lambda$showSubscriptionSheet$42(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda99
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$41(ButtonWithCounterView.this, bottomSheetArr, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$43(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final long j) {
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_fulfillStarsSubscription tL_fulfillStarsSubscription = new TL_stars.TL_fulfillStarsSubscription();
        tL_fulfillStarsSubscription.subscription_id = starsSubscription.id;
        tL_fulfillStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        ConnectionsManager.getInstance(i).sendRequest(tL_fulfillStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda90
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$42(ButtonWithCounterView.this, bottomSheetArr, i, j, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$44(final ButtonWithCounterView buttonWithCounterView, final int i, final TL_stars.StarsSubscription starsSubscription, final BottomSheet[] bottomSheetArr, final long j, Context context, Theme.ResourcesProvider resourcesProvider, boolean z, String str, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        StarsController starsController = StarsController.getInstance(i);
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$43(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, j);
            }
        };
        if (starsController.balance < starsSubscription.pricing.amount) {
            new StarsNeededSheet(context, resourcesProvider, starsSubscription.pricing.amount, z ? 8 : j < 0 ? 2 : 7, str, runnable).show();
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$45(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, TLObject tLObject, String str) {
        buttonWithCounterView.setLoading(false);
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        StarsController.getInstance(i).invalidateSubscriptions(true);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createUsersBulletin(Collections.singletonList(tLObject), LocaleController.getString(R.string.StarsSubscriptionRenewedToast), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.StarsSubscriptionRenewedToastText, str))).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$46(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final TLObject tLObject, final String str, TLObject tLObject2, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$45(ButtonWithCounterView.this, bottomSheetArr, i, tLObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$47(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final TLObject tLObject, final String str, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_changeStarsSubscription tL_changeStarsSubscription = new TL_stars.TL_changeStarsSubscription();
        tL_changeStarsSubscription.canceled = Boolean.FALSE;
        tL_changeStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        tL_changeStarsSubscription.subscription_id = starsSubscription.id;
        ConnectionsManager.getInstance(i).sendRequest(tL_changeStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda83
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$46(ButtonWithCounterView.this, bottomSheetArr, i, tLObject, str, tLObject2, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$48(ButtonWithCounterView buttonWithCounterView, BottomSheet[] bottomSheetArr, int i, boolean z, TL_stars.StarsSubscription starsSubscription, boolean z2, TLObject tLObject) {
        buttonWithCounterView.setLoading(false);
        BottomSheet bottomSheet = bottomSheetArr[0];
        if (bottomSheet != null) {
            bottomSheet.dismiss();
        }
        StarsController.getInstance(i).invalidateSubscriptions(true);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createUsersBulletin(Collections.singletonList(tLObject), LocaleController.getString(R.string.StarsSubscriptionCancelledToast), AndroidUtilities.replaceTags((!z || TextUtils.isEmpty(starsSubscription.title)) ? (!z2 || TextUtils.isEmpty(starsSubscription.title)) ? LocaleController.formatString(R.string.StarsSubscriptionCancelledToastText, LocaleController.formatDateChat(starsSubscription.until_date)) : LocaleController.formatString(R.string.StarsSubscriptionCancelledBotToastText, LocaleController.formatDateChat(starsSubscription.until_date), starsSubscription.title) : LocaleController.formatString(R.string.StarsSubscriptionCancelledBizToastText, LocaleController.formatDateChat(starsSubscription.until_date), starsSubscription.title))).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$49(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final int i, final boolean z, final TL_stars.StarsSubscription starsSubscription, final boolean z2, final TLObject tLObject, TLObject tLObject2, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$48(ButtonWithCounterView.this, bottomSheetArr, i, z, starsSubscription, z2, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$50(final ButtonWithCounterView buttonWithCounterView, final TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final boolean z, final boolean z2, final TLObject tLObject, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        TL_stars.TL_changeStarsSubscription tL_changeStarsSubscription = new TL_stars.TL_changeStarsSubscription();
        tL_changeStarsSubscription.canceled = Boolean.TRUE;
        tL_changeStarsSubscription.peer = new TLRPC.TL_inputPeerSelf();
        tL_changeStarsSubscription.subscription_id = starsSubscription.id;
        ConnectionsManager.getInstance(i).sendRequest(tL_changeStarsSubscription, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda88
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                StarsIntroActivity.lambda$showSubscriptionSheet$49(ButtonWithCounterView.this, bottomSheetArr, i, z, starsSubscription, z2, tLObject, tLObject2, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$51(BaseFragment baseFragment, long j, TLRPC.Chat chat) {
        BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.stars_send, LocaleController.getString(R.string.StarsSubscriptionCompleted), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsSubscriptionCompletedText", (int) j, chat.title))).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$52(Long l, int i, final long j) {
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
                    StarsIntroActivity.lambda$showSubscriptionSheet$51(BaseFragment.this, j, chat);
                }
            }, 250L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$53(final int i, final long j, String str, final Long l) {
        if (!"paid".equals(str) || l.longValue() == 0) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda105
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$52(l, i, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$54(ButtonWithCounterView buttonWithCounterView, TLObject tLObject, BottomSheet[] bottomSheetArr, Theme.ResourcesProvider resourcesProvider, final int i, TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite) {
        buttonWithCounterView.setLoading(false);
        if (!(tLObject instanceof TLRPC.ChatInvite)) {
            BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.LinkHashExpired)).show(false);
            return;
        }
        TLRPC.ChatInvite chatInvite = (TLRPC.ChatInvite) tLObject;
        TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = chatInvite.subscription_pricing;
        if (tL_starsSubscriptionPricing == null) {
            BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.UnknownError)).show(false);
        } else {
            final long j = tL_starsSubscriptionPricing.amount;
            StarsController.getInstance(i).subscribeTo(tL_messages_checkChatInvite.hash, chatInvite, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda102
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    StarsIntroActivity.lambda$showSubscriptionSheet$53(i, j, (String) obj, (Long) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$55(final ButtonWithCounterView buttonWithCounterView, final BottomSheet[] bottomSheetArr, final Theme.ResourcesProvider resourcesProvider, final int i, final TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda94
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$54(ButtonWithCounterView.this, tLObject, bottomSheetArr, resourcesProvider, i, tL_messages_checkChatInvite);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$56(final ButtonWithCounterView buttonWithCounterView, TL_stars.StarsSubscription starsSubscription, final int i, final BottomSheet[] bottomSheetArr, final Theme.ResourcesProvider resourcesProvider, boolean[] zArr, Context context, View view) {
        if (buttonWithCounterView.isLoading()) {
            return;
        }
        buttonWithCounterView.setLoading(true);
        if (starsSubscription.chat_invite_hash != null) {
            final TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite = new TLRPC.TL_messages_checkChatInvite();
            tL_messages_checkChatInvite.hash = starsSubscription.chat_invite_hash;
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda78
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StarsIntroActivity.lambda$showSubscriptionSheet$55(ButtonWithCounterView.this, bottomSheetArr, resourcesProvider, i, tL_messages_checkChatInvite, tLObject, tL_error);
                }
            });
        } else if (starsSubscription.invoice_slug != null) {
            zArr[0] = true;
            Browser.openUrl(context, Uri.parse("https://t.me/$" + starsSubscription.invoice_slug), true, false, false, new Browser.Progress() { // from class: org.telegram.ui.Stars.StarsIntroActivity.11
                @Override // org.telegram.messenger.browser.Browser.Progress
                public void end() {
                    ButtonWithCounterView.this.setLoading(false);
                }
            }, null, false, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSubscriptionSheet$57(int i, NotificationCenter.NotificationCenterDelegate notificationCenterDelegate, DialogInterface dialogInterface) {
        NotificationCenter.getInstance(i).removeObserver(notificationCenterDelegate, NotificationCenter.starSubscriptionsLoaded);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$21(boolean z, long j, TL_stars.StarsTransaction starsTransaction, int i, Theme.ResourcesProvider resourcesProvider, final BackupImageView backupImageView, final LinearLayout linearLayout, View view) {
        final long peerDialogId = z ? j : DialogObject.getPeerDialogId(starsTransaction.peer.peer);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < starsTransaction.extended_media.size(); i2++) {
            TLRPC.MessageMedia messageMedia = starsTransaction.extended_media.get(i2);
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
            tL_message.media = messageMedia;
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
    public static /* synthetic */ void lambda$showTransactionSheet$22(Context context, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet;
        BaseFragment baseFragment;
        StarAppsSheet starAppsSheet = new StarAppsSheet(context);
        if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(bottomSheetArr[0].attachedFragment) && (bottomSheet = bottomSheetArr[0]) != null && (baseFragment = bottomSheet.attachedFragment) != null) {
            starAppsSheet.makeAttached(baseFragment);
        }
        starAppsSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$23(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$24(Context context, int i, long j, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        Objects.requireNonNull(bottomSheet);
        new GiftSheet(context, i, j, new BottomSheet$$ExternalSyntheticLambda11(bottomSheet)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$25(BottomSheet[] bottomSheetArr, int i) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$26(BottomSheet[] bottomSheetArr, int i) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$27(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$28(Context context, int i, long j, BottomSheet[] bottomSheetArr) {
        BottomSheet bottomSheet = bottomSheetArr[0];
        Objects.requireNonNull(bottomSheet);
        new GiftSheet(context, i, j, new BottomSheet$$ExternalSyntheticLambda11(bottomSheet)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$29(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$30(BottomSheet[] bottomSheetArr, int i) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$31(BottomSheet[] bottomSheetArr, TL_stars.StarsTransaction starsTransaction, long j) {
        bottomSheetArr[0].dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment((starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0 ? ChatActivity.of(j, starsTransaction.giveaway_post_id) : ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$32(BottomSheet[] bottomSheetArr, long j, Context context) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$33(BottomSheet[] bottomSheetArr, long j, Context context) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$34(BottomSheet[] bottomSheetArr, long j, TL_stars.StarsTransaction starsTransaction) {
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
    public static /* synthetic */ void lambda$showTransactionSheet$36(TL_stars.StarsTransaction starsTransaction, BottomSheet[] bottomSheetArr, Theme.ResourcesProvider resourcesProvider, View view) {
        AndroidUtilities.addToClipboard(starsTransaction.id);
        BulletinFactory.of(bottomSheetArr[0].topBulletinContainer, resourcesProvider).createSimpleBulletin(R.raw.copy, LocaleController.getString(R.string.StarsTransactionIDCopied)).show(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$37(Context context) {
        Browser.openUrl(context, LocaleController.getString(R.string.StarsTOSLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$38(Context context, TL_stars.StarsTransaction starsTransaction, View view) {
        Browser.openUrl(context, starsTransaction.transaction_url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTransactionSheet$39(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    public static StarParticlesView makeParticlesView(Context context, int i, int i2) {
        return new 2(context, i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x03ac  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0402  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet openConfirmPurchaseSheet(final Context context, Theme.ResourcesProvider resourcesProvider, int i, MessageObject messageObject, long j, String str, long j2, TLRPC.WebDocument webDocument, int i2, final Utilities.Callback callback, final Runnable runnable) {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        int i3;
        String str2;
        SpannableStringBuilder replaceTags;
        boolean z;
        SpannableStringBuilder replaceStars;
        TLRPC.Message message;
        String str3;
        boolean z2;
        TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia;
        int i4;
        int i5;
        int i6;
        String formatPluralString;
        int i7;
        String formatPluralString2;
        String str4;
        char c;
        String formatPluralString3;
        char c2;
        String formatPluralString4;
        boolean z3;
        int i8;
        TLRPC.User user;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        TLRPC.Message message2;
        BackupImageView backupImageView;
        FrameLayout frameLayout;
        ImageLocation imageLocation;
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        frameLayout2.addView(makeParticlesView(context, 40, 0), LayoutHelper.createFrame(-1, -1.0f));
        if (messageObject == null || (message2 = messageObject.messageOwner) == null || !(message2.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
            viewGroup = frameLayout2;
            viewGroup2 = linearLayout;
            if (webDocument == null) {
                BackupImageView backupImageView2 = new BackupImageView(context);
                backupImageView2.setRoundRadius(AndroidUtilities.dp(80.0f));
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setInfo(user2);
                backupImageView2.setForUserOrChat(user2, avatarDrawable);
                viewGroup.addView(backupImageView2, LayoutHelper.createFrame(80, 80, 17));
            } else {
                FrameLayout frameLayout3 = new FrameLayout(context);
                BackupImageView backupImageView3 = new BackupImageView(context);
                backupImageView3.setRoundRadius(AndroidUtilities.dp(18.0f));
                backupImageView3.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(webDocument)), "80_80", (Drawable) null, 0, (Object) null);
                frameLayout3.addView(backupImageView3, LayoutHelper.createFrame(80, 80, 48));
                viewGroup.addView(frameLayout3, LayoutHelper.createFrame(80, 87, 17));
                TextView textView = new TextView(context);
                textView.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
                textView.setTextSize(1, 13.0f);
                textView.setTextColor(-1);
                textView.setText(replaceStars("XTR " + LocaleController.formatNumber((int) j2, ','), 0.85f));
                textView.setPadding(AndroidUtilities.dp(5.33f), 0, AndroidUtilities.dp(5.33f), 0);
                textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(16.0f), -1133566));
                FrameLayout frameLayout4 = new FrameLayout(context);
                frameLayout4.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(20.0f), Theme.getColor(Theme.key_dialogBackground, resourcesProvider)));
                frameLayout4.setPadding(AndroidUtilities.dp(1.33f), AndroidUtilities.dp(1.33f), AndroidUtilities.dp(1.33f), AndroidUtilities.dp(1.33f));
                frameLayout4.addView(textView, LayoutHelper.createLinear(-2, 16, 119));
                frameLayout3.addView(frameLayout4, LayoutHelper.createFrame(-2.0f, 18.66f, 81));
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

                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                protected void onAttachedToWindow() {
                    SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
                    if (spoilerEffect2 != null) {
                        spoilerEffect2.attach(this);
                    }
                    super.onAttachedToWindow();
                }

                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                protected void onDetachedFromWindow() {
                    SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
                    if (spoilerEffect2 != null) {
                        spoilerEffect2.detach(this);
                    }
                    super.onDetachedFromWindow();
                }
            };
            backupImageView4.setRoundRadius(AndroidUtilities.dp(24.0f));
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia2 = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            if (tL_messageMediaPaidMedia2.extended_media.isEmpty()) {
                backupImageView = backupImageView4;
                frameLayout = frameLayout2;
                viewGroup2 = linearLayout;
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
                backupImageView = backupImageView4;
                frameLayout = frameLayout2;
                viewGroup2 = linearLayout;
                backupImageView4.setImage(imageLocation, "80_80_b2", (ImageLocation) null, (String) null, (Drawable) null, messageObject);
            }
            viewGroup = frameLayout;
            viewGroup.addView(backupImageView, LayoutHelper.createFrame(80, 80, 17));
        }
        final StarsBalanceView starsBalanceView = new StarsBalanceView(context, i);
        ScaleStateListAnimator.apply(starsBalanceView);
        starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$5(StarsIntroActivity.StarsBalanceView.this, view);
            }
        });
        viewGroup.addView(starsBalanceView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, -8.0f, 0.0f));
        ViewGroup viewGroup3 = viewGroup2;
        viewGroup3.addView(viewGroup, LayoutHelper.createLinear(-1, 117, 7));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        int i9 = Theme.key_dialogTextBlack;
        textView2.setTextColor(Theme.getColor(i9, resourcesProvider));
        if (i2 > 0) {
            if (webDocument == null) {
                i3 = R.string.StarsConfirmSubscriptionTitle;
                str2 = LocaleController.getString(i3);
            }
            str2 = str;
        } else {
            if (webDocument == null) {
                i3 = R.string.StarsConfirmPurchaseTitle;
                str2 = LocaleController.getString(i3);
            }
            str2 = str;
        }
        textView2.setText(Emoji.replaceEmoji(str2, textView2.getPaint().getFontMetricsInt(), false));
        NotificationCenter.listenEmojiLoading(textView2);
        textView2.setGravity(17);
        viewGroup3.addView(textView2, LayoutHelper.createLinear(-2, -2, 1, 0, webDocument != null ? -8 : 8, 0, 0));
        if (webDocument != null) {
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(0);
            linearLayout2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(28.0f), Theme.getColor(Theme.key_windowBackgroundGray, resourcesProvider)));
            BackupImageView backupImageView5 = new BackupImageView(context);
            backupImageView5.setRoundRadius(AndroidUtilities.dp(14.0f));
            AvatarDrawable avatarDrawable2 = new AvatarDrawable();
            avatarDrawable2.setInfo(user2);
            backupImageView5.setForUserOrChat(user2, avatarDrawable2);
            linearLayout2.addView(backupImageView5, LayoutHelper.createLinear(28, 28));
            TextView textView3 = new TextView(context);
            textView3.setTextSize(1, 13.0f);
            textView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            textView3.setText(UserObject.getUserName(user2));
            linearLayout2.addView(textView3, LayoutHelper.createLinear(-2, -2, 16, 6, 0, 10, 0));
            viewGroup3.addView(linearLayout2, LayoutHelper.createLinear(-2, 28, 1, 0, 8, 0, 2));
        }
        TextView textView4 = new TextView(context);
        textView4.setTextSize(1, 14.0f);
        textView4.setTextColor(Theme.getColor(i9, resourcesProvider));
        if (messageObject == null || (message = messageObject.messageOwner) == null || !(message.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
            replaceTags = AndroidUtilities.replaceTags(i2 > 0 ? LocaleController.formatPluralStringComma("StarsConfirmSubscriptionText2", (int) j2, str, UserObject.getUserName(user2)) : LocaleController.formatPluralStringComma("StarsConfirmPurchaseText2", (int) j2, str, UserObject.getUserName(user2)));
        } else {
            long dialogId = messageObject.getDialogId();
            TLRPC.Message message3 = messageObject.messageOwner;
            if (message3 != null && (messageFwdHeader = message3.fwd_from) != null && (peer = messageFwdHeader.from_id) != null) {
                dialogId = DialogObject.getPeerDialogId(peer);
            }
            if (dialogId < 0 && messageObject.getFromChatId() > 0 && (user = MessagesController.getInstance(i).getUser(Long.valueOf(messageObject.getFromChatId()))) != null && user.bot) {
                dialogId = user.id;
            }
            MessagesController messagesController = MessagesController.getInstance(i);
            if (dialogId >= 0) {
                TLRPC.User user3 = messagesController.getUser(Long.valueOf(dialogId));
                str3 = UserObject.getUserName(user3);
                if (user3 != null && user3.bot) {
                    z2 = true;
                    tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
                    i4 = 0;
                    i5 = 0;
                    int i10 = 0;
                    while (i4 < tL_messageMediaPaidMedia.extended_media.size()) {
                        TLRPC.MessageExtendedMedia messageExtendedMedia2 = tL_messageMediaPaidMedia.extended_media.get(i4);
                        if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMediaPreview) {
                            if ((((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia2).flags & 4) != 0) {
                                z3 = true;
                            }
                            z3 = false;
                        } else {
                            if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMedia) {
                                z3 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia2).media instanceof TLRPC.TL_messageMediaDocument;
                            }
                            z3 = false;
                        }
                        if (z3) {
                            i8 = 1;
                            i5++;
                        } else {
                            i8 = 1;
                            i10++;
                        }
                        i4 += i8;
                    }
                    if (i5 != 0) {
                        str4 = z2 ? "StarsConfirmPurchaseMediaBotOne2" : "StarsConfirmPurchaseMediaOne2";
                        int i11 = (int) j2;
                        if (i10 == 1) {
                            formatPluralString4 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto);
                            c2 = 0;
                        } else {
                            c2 = 0;
                            formatPluralString4 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i10, new Object[0]);
                        }
                        Object[] objArr = new Object[2];
                        objArr[c2] = formatPluralString4;
                        objArr[1] = str3;
                        formatPluralString2 = LocaleController.formatPluralString(str4, i11, objArr);
                    } else if (i10 == 0) {
                        str4 = z2 ? "StarsConfirmPurchaseMediaBotOne2" : "StarsConfirmPurchaseMediaOne2";
                        int i12 = (int) j2;
                        if (i5 == 1) {
                            formatPluralString3 = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo);
                            c = 0;
                        } else {
                            c = 0;
                            formatPluralString3 = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i5, new Object[0]);
                        }
                        Object[] objArr2 = new Object[2];
                        objArr2[c] = formatPluralString3;
                        objArr2[1] = str3;
                        formatPluralString2 = LocaleController.formatPluralString(str4, i12, objArr2);
                    } else {
                        String str5 = z2 ? "StarsConfirmPurchaseMediaBotTwo2" : "StarsConfirmPurchaseMediaTwo2";
                        int i13 = (int) j2;
                        if (i10 == 1) {
                            formatPluralString = LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SinglePhoto);
                            i7 = 1;
                            i6 = 0;
                        } else {
                            i6 = 0;
                            formatPluralString = LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Photos", i10, new Object[0]);
                            i7 = 1;
                        }
                        String string = i5 == i7 ? LocaleController.getString(R.string.StarsConfirmPurchaseMedia_SingleVideo) : LocaleController.formatPluralString("StarsConfirmPurchaseMedia_Videos", i5, new Object[i6]);
                        Object[] objArr3 = new Object[3];
                        objArr3[i6] = formatPluralString;
                        objArr3[i7] = string;
                        objArr3[2] = str3;
                        formatPluralString2 = LocaleController.formatPluralString(str5, i13, objArr3);
                    }
                    replaceTags = AndroidUtilities.replaceTags(formatPluralString2);
                }
            } else {
                TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-dialogId));
                str3 = chat == null ? "" : chat.title;
            }
            z2 = false;
            tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageObject.messageOwner.media;
            i4 = 0;
            i5 = 0;
            int i102 = 0;
            while (i4 < tL_messageMediaPaidMedia.extended_media.size()) {
            }
            if (i5 != 0) {
            }
            replaceTags = AndroidUtilities.replaceTags(formatPluralString2);
        }
        textView4.setText(replaceTags);
        textView4.setMaxWidth(HintView2.cutInFancyHalf(textView4.getText(), textView4.getPaint()));
        textView4.setGravity(17);
        viewGroup3.addView(textView4, LayoutHelper.createLinear(-2, -2, 1, 0, 6, 0, 18));
        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        if (i2 > 0) {
            replaceStars = replaceStars(AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("StarsConfirmSubscriptionButton", (int) j2)));
            z = false;
        } else {
            z = false;
            replaceStars = replaceStars(AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("StarsConfirmPurchaseButton", (int) j2)));
        }
        buttonWithCounterView.setText(replaceStars, z);
        viewGroup3.addView(buttonWithCounterView, LayoutHelper.createFrame(-1, 48.0f));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(i2 > 0 ? R.string.StarsConfirmSubscriptionTOS : R.string.StarsConfirmPurchaseTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$6(context);
            }
        }));
        linksTextView.setGravity(17);
        viewGroup3.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 0.0f, 2.0f));
        builder.setCustomView(viewGroup3);
        final BottomSheet create = builder.create();
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda22
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$9(Utilities.Callback.this, create, buttonWithCounterView, view);
            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda23
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$openConfirmPurchaseSheet$10(runnable, dialogInterface);
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
        starsBalanceView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda79
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$11(StarsIntroActivity.StarsBalanceView.this, view);
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
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsSubscribeInfo), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$12(context);
            }
        }));
        linksTextView.setGravity(17);
        linksTextView.setTextSize(1, 13.0f);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 49, 14, 14, 14, 6));
        builder.setCustomView(linearLayout);
        final BottomSheet create = builder.create();
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda81
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$15(Utilities.Callback.this, create, buttonWithCounterView, view);
            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda82
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$openStarsChannelInviteSheet$16(runnable, dialogInterface);
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
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$setGiftImage$17(currentAccount, i, imageReceiver, zArr);
            }
        };
        runnable.run();
        final Runnable listen = NotificationCenter.getInstance(currentAccount).listen(view, NotificationCenter.didUpdatePremiumGiftStickers, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda25
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                runnable.run();
            }
        });
        final Runnable listen2 = NotificationCenter.getInstance(currentAccount).listen(view, NotificationCenter.diceStickersDidLoad, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda26
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                runnable.run();
            }
        });
        return new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$setGiftImage$20(listen, listen2);
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

    /* JADX WARN: Removed duplicated region for block: B:20:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x036a  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showActionGiftSheet(final Context context, final int i, final long j, boolean z, int i2, final int i3, final TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final Theme.ResourcesProvider resourcesProvider) {
        int i4;
        BottomSheet[] bottomSheetArr;
        int i5;
        String str;
        String formatPluralStringComma;
        final long clientUserId;
        long j2;
        long j3;
        LinearLayout linearLayout;
        int i6;
        TableView tableView;
        TableView tableView2;
        TableView tableView3;
        BottomSheet.Builder builder;
        BottomSheet[] bottomSheetArr2;
        final long j4;
        TL_stars.StarGift starGift;
        Runnable runnable;
        TableView tableView4;
        Runnable runnable2;
        Theme.ResourcesProvider resourcesProvider2;
        TableView tableView5;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        final BottomSheet[] bottomSheetArr3;
        BaseFragment safeLastFragment;
        if (tL_messageActionStarGift == null || context == null) {
            return null;
        }
        TL_stars.StarGift starGift2 = tL_messageActionStarGift.gift;
        if (starGift2 == null) {
            return null;
        }
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        BottomSheet[] bottomSheetArr4 = new BottomSheet[1];
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout2.setClipChildren(false);
        linearLayout2.setClipToPadding(false);
        BackupImageView backupImageView = new BackupImageView(context);
        setGiftImage(backupImageView.getImageReceiver(), starGift2, NotificationCenter.audioRouteChanged);
        linearLayout2.addView(backupImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
        TextView textView = new TextView(context);
        int i7 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i7, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(z ? R.string.Gift2TitleSent : R.string.Gift2TitleReceived));
        linearLayout2.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        final TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        boolean isBot = UserObject.isBot(user);
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView.setTextColor(Theme.getColor(i7, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setGravity(17);
        int i8 = Theme.key_chat_messageLinkIn;
        linksTextView.setLinkTextColor(Theme.getColor(i8, resourcesProvider));
        linksTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        linksTextView.setDisablePaddingsOffsetY(true);
        int currentTime = MessagesController.getInstance(i).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(i).getCurrentTime() - i2);
        final int max = Math.max(1, currentTime / 86400);
        if (!isBot) {
            if (!z) {
                i4 = i8;
                bottomSheetArr = bottomSheetArr4;
                if (tL_messageActionStarGift.converted) {
                    i5 = (int) tL_messageActionStarGift.convert_stars;
                    str = "Gift2InfoConverted";
                } else {
                    i5 = (int) tL_messageActionStarGift.convert_stars;
                    str = "Gift2Info";
                }
                formatPluralStringComma = LocaleController.formatPluralStringComma(str, i5);
            } else if (!tL_messageActionStarGift.saved || tL_messageActionStarGift.converted) {
                bottomSheetArr = bottomSheetArr4;
                i4 = i8;
                formatPluralStringComma = LocaleController.formatPluralStringComma(tL_messageActionStarGift.converted ? "Gift2InfoOutConverted" : "Gift2InfoOut", (int) tL_messageActionStarGift.convert_stars, UserObject.getForcedFirstName(user));
            } else {
                formatPluralStringComma = LocaleController.formatString(R.string.Gift2InfoOutPinned, UserObject.getForcedFirstName(user));
            }
            linksTextView.setText(TextUtils.concat(AndroidUtilities.replaceTags(formatPluralStringComma), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showActionGiftSheet$74(context);
                }
            }), true)));
            linearLayout2.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 5, 5, 5, 4));
            if (!tL_messageActionStarGift.name_hidden) {
                TextView textView2 = new TextView(context);
                textView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider));
                textView2.setTextSize(1, 14.0f);
                textView2.setGravity(17);
                textView2.setText(LocaleController.getString(R.string.Gift2SenderHidden));
                linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 13, 20, 2));
            }
            TableView tableView6 = new TableView(context, resourcesProvider);
            clientUserId = UserConfig.getInstance(i).getClientUserId();
            j2 = !z ? clientUserId : j;
            j3 = !z ? j : clientUserId;
            TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(j2));
            if (j2 == clientUserId) {
                int i9 = i4;
                tableView = tableView6;
                final BottomSheet[] bottomSheetArr5 = bottomSheetArr;
                i6 = i9;
                final long j5 = j2;
                linearLayout = linearLayout2;
                final long j6 = j2;
                final BottomSheet[] bottomSheetArr6 = bottomSheetArr;
                tableView.addRowUser(LocaleController.getString(R.string.Gift2From), i, j2, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda48
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$75(bottomSheetArr5, j5, clientUserId);
                    }
                }, (j2 == clientUserId || j2 == UserObject.ANONYMOUS || UserObject.isDeleted(user2) || isBot) ? null : LocaleController.getString(R.string.Gift2ButtonSendGift), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda49
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$76(context, i, j6, bottomSheetArr6);
                    }
                });
            } else {
                linearLayout = linearLayout2;
                i6 = i4;
                tableView = tableView6;
            }
            if (j3 != clientUserId) {
                final BottomSheet[] bottomSheetArr7 = bottomSheetArr;
                final long j7 = j3;
                final long j8 = j3;
                final BottomSheet[] bottomSheetArr8 = bottomSheetArr;
                tableView.addRowUser(LocaleController.getString(R.string.Gift2To), i, j3, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda50
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$77(bottomSheetArr7, j7, clientUserId);
                    }
                }, null, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda51
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$78(context, i, j8, bottomSheetArr8);
                    }
                });
            }
            tableView2 = tableView;
            tableView2.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
            if (!z || tL_messageActionStarGift.converted || tL_messageActionStarGift.convert_stars <= 0 || currentTime <= 0) {
                tableView3 = tableView2;
                builder = builder2;
                bottomSheetArr2 = bottomSheetArr;
                j4 = clientUserId;
                starGift = starGift2;
                runnable = null;
            } else {
                j4 = clientUserId;
                starGift = starGift2;
                builder = builder2;
                final BottomSheet[] bottomSheetArr9 = bottomSheetArr;
                bottomSheetArr2 = bottomSheetArr;
                tableView3 = tableView2;
                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda52
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$83(context, resourcesProvider, max, user, tL_messageActionStarGift, i3, i, j, bottomSheetArr9, j4);
                    }
                };
            }
            tableView4 = tableView3;
            tableView4.addRow(LocaleController.getString(R.string.Gift2Value), replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(starGift.stars, ','), " ", runnable != null ? "" : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) tL_messageActionStarGift.convert_stars), runnable, resourcesProvider)), 0.8f));
            final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
            final BottomSheet[] bottomSheetArr10 = bottomSheetArr2;
            final long j9 = j4;
            final TL_stars.StarGift starGift3 = starGift;
            runnable2 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showActionGiftSheet$87(ButtonWithCounterView.this, tL_messageActionStarGift, i3, i, j, bottomSheetArr10, j9, starGift3, resourcesProvider);
                }
            };
            if (!z && !tL_messageActionStarGift.converted) {
                tableView4.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!tL_messageActionStarGift.saved ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!tL_messageActionStarGift.saved ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), runnable2);
            }
            if (starGift.limited) {
                resourcesProvider2 = resourcesProvider;
                tableView5 = tableView4;
            } else {
                resourcesProvider2 = resourcesProvider;
                tableView5 = tableView4;
                addAvailabilityRow(tableView5, i, starGift, resourcesProvider2);
            }
            tL_textWithEntities = tL_messageActionStarGift.message;
            if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
                TLRPC.TL_textWithEntities tL_textWithEntities2 = tL_messageActionStarGift.message;
                tableView5.addFullRow(tL_textWithEntities2.text, tL_textWithEntities2.entities);
            }
            LinearLayout linearLayout3 = linearLayout;
            linearLayout3.addView(tableView5, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
            if (!z || tL_messageActionStarGift.converted) {
                bottomSheetArr3 = bottomSheetArr2;
            } else {
                LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
                linksTextView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider2));
                linksTextView2.setTextSize(1, 14.0f);
                linksTextView2.setGravity(17);
                linksTextView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                linksTextView2.setLinkTextColor(Theme.getColor(i6, resourcesProvider2));
                linksTextView2.setDisablePaddingsOffsetY(true);
                bottomSheetArr3 = bottomSheetArr2;
                final long j10 = j4;
                linksTextView2.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(tL_messageActionStarGift.saved ? R.string.Gift2ProfileVisible2 : R.string.Gift2ProfileInvisible), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda54
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showActionGiftSheet$88(bottomSheetArr3, j10);
                    }
                }), true, AndroidUtilities.dp(2.6666667f), AndroidUtilities.dp(0.66f)));
                linearLayout3.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 17, 5, 6, 5, 16));
            }
            buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
            linearLayout3.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
            BottomSheet.Builder builder3 = builder;
            builder3.setCustomView(linearLayout3);
            BottomSheet create = builder3.create();
            bottomSheetArr3[0] = create;
            create.useBackgroundTopPadding = false;
            buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda55
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showActionGiftSheet$89(bottomSheetArr3, view);
                }
            });
            bottomSheetArr3[0].fixNavigationBar();
            safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                bottomSheetArr3[0].makeAttached(safeLastFragment);
            }
            bottomSheetArr3[0].show();
            return bottomSheetArr3[0];
        }
        formatPluralStringComma = LocaleController.getString(tL_messageActionStarGift.saved ? R.string.Gift2Info2BotRemove : R.string.Gift2Info2BotKeep);
        i4 = i8;
        bottomSheetArr = bottomSheetArr4;
        linksTextView.setText(TextUtils.concat(AndroidUtilities.replaceTags(formatPluralStringComma), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$74(context);
            }
        }), true)));
        linearLayout2.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 5, 5, 5, 4));
        if (!tL_messageActionStarGift.name_hidden) {
        }
        TableView tableView62 = new TableView(context, resourcesProvider);
        clientUserId = UserConfig.getInstance(i).getClientUserId();
        if (!z) {
        }
        if (!z) {
        }
        TLRPC.User user22 = MessagesController.getInstance(i).getUser(Long.valueOf(j2));
        if (j2 == clientUserId) {
        }
        if (j3 != clientUserId) {
        }
        tableView2 = tableView;
        tableView2.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
        if (z) {
        }
        tableView3 = tableView2;
        builder = builder2;
        bottomSheetArr2 = bottomSheetArr;
        j4 = clientUserId;
        starGift = starGift2;
        runnable = null;
        tableView4 = tableView3;
        tableView4.addRow(LocaleController.getString(R.string.Gift2Value), replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(starGift.stars, ','), " ", runnable != null ? "" : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) tL_messageActionStarGift.convert_stars), runnable, resourcesProvider)), 0.8f));
        final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider);
        final BottomSheet[] bottomSheetArr102 = bottomSheetArr2;
        final long j92 = j4;
        final TL_stars.StarGift starGift32 = starGift;
        runnable2 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showActionGiftSheet$87(ButtonWithCounterView.this, tL_messageActionStarGift, i3, i, j, bottomSheetArr102, j92, starGift32, resourcesProvider);
            }
        };
        if (!z) {
            tableView4.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!tL_messageActionStarGift.saved ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!tL_messageActionStarGift.saved ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), runnable2);
        }
        if (starGift.limited) {
        }
        tL_textWithEntities = tL_messageActionStarGift.message;
        if (tL_textWithEntities != null) {
            TLRPC.TL_textWithEntities tL_textWithEntities22 = tL_messageActionStarGift.message;
            tableView5.addFullRow(tL_textWithEntities22.text, tL_textWithEntities22.entities);
        }
        LinearLayout linearLayout32 = linearLayout;
        linearLayout32.addView(tableView5, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
        if (z) {
        }
        bottomSheetArr3 = bottomSheetArr2;
        buttonWithCounterView2.setText(LocaleController.getString(R.string.OK), false);
        linearLayout32.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
        BottomSheet.Builder builder32 = builder;
        builder32.setCustomView(linearLayout32);
        BottomSheet create2 = builder32.create();
        bottomSheetArr3[0] = create2;
        create2.useBackgroundTopPadding = false;
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda55
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showActionGiftSheet$89(bottomSheetArr3, view);
            }
        });
        bottomSheetArr3[0].fixNavigationBar();
        safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet()) {
            bottomSheetArr3[0].makeAttached(safeLastFragment);
        }
        bottomSheetArr3[0].show();
        return bottomSheetArr3[0];
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
        tableView.addRowUser(LocaleController.getString(R.string.BoostFrom), i, j, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showBoostsSheet$58(bottomSheetArr, j);
            }
        });
        tableView.addRow(LocaleController.getString(R.string.BoostGift), LocaleController.formatPluralString("BoostStars", (int) boost.stars, new Object[0]));
        if (boost.giveaway_msg_id != 0) {
            tableView.addRowLink(LocaleController.getString(R.string.BoostReason), LocaleController.getString(R.string.BoostReasonGiveaway), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showBoostsSheet$59(bottomSheetArr, j, boost);
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
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showBoostsSheet$60(context);
            }
        }));
        linksTextView.setGravity(17);
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 14.0f, 15.0f, 14.0f, 7.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda36
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showBoostsSheet$61(bottomSheetArr, view);
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
        bottomSheetArr[0].setOnDismissListener(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                GLIconTextureView.this.setPaused(true);
            }
        });
        return bottomSheetArr[0];
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0363  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0382  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0333  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showGiftSheet(final Context context, final int i, final long j, boolean z, final TL_stars.UserStarGift userStarGift, final Theme.ResourcesProvider resourcesProvider) {
        LinearLayout linearLayout;
        int i2;
        TableView tableView;
        LinearLayout linearLayout2;
        TL_stars.StarGift starGift;
        char c;
        Runnable runnable;
        Theme.ResourcesProvider resourcesProvider2;
        CharSequence make;
        TableView tableView2;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        BaseFragment safeLastFragment;
        String formatPluralStringComma;
        int i3;
        String str;
        if (userStarGift == null || context == null) {
            return null;
        }
        TL_stars.StarGift starGift2 = userStarGift.gift;
        if (starGift2 == null) {
            return null;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        boolean isBot = UserObject.isBot(MessagesController.getInstance(i).getUser(Long.valueOf(userStarGift.from_id)));
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout3.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout3.setClipChildren(false);
        linearLayout3.setClipToPadding(false);
        BackupImageView backupImageView = new BackupImageView(context);
        setGiftImage(backupImageView.getImageReceiver(), starGift2, NotificationCenter.audioRouteChanged);
        linearLayout3.addView(backupImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
        TextView textView = new TextView(context);
        int i4 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i4, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(z ? R.string.Gift2TitleReceived : R.string.Gift2TitleProfile));
        linearLayout3.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 9));
        if (isBot) {
            linearLayout = linearLayout3;
        } else {
            TextView textView2 = new TextView(context);
            textView2.setTextSize(1, 18.0f);
            textView2.setTypeface(AndroidUtilities.bold());
            textView2.setGravity(17);
            textView2.setTextColor(Theme.getColor(Theme.key_color_green, resourcesProvider));
            textView2.setText(replaceStarsWithPlain(LocaleController.formatNumber((int) Math.abs(Math.max(userStarGift.gift.convert_stars, userStarGift.convert_stars)), ' ') + " ⭐️", 0.8f));
            linearLayout = linearLayout3;
            linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        }
        if (z) {
            LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
            linksTextView.setTextColor(Theme.getColor(i4, resourcesProvider));
            linksTextView.setTextSize(1, 14.0f);
            linksTextView.setGravity(17);
            linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
            linksTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linksTextView.setDisablePaddingsOffsetY(true);
            int currentTime = MessagesController.getInstance(i).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(i).getCurrentTime() - userStarGift.date);
            if (isBot) {
                formatPluralStringComma = LocaleController.getString(userStarGift.unsaved ? R.string.Gift2Info2BotKeep : R.string.Gift2Info2BotRemove);
            } else if (z) {
                if (currentTime <= 0) {
                    i3 = (int) userStarGift.convert_stars;
                    str = "Gift2Info2Expired";
                } else {
                    i3 = (int) userStarGift.convert_stars;
                    str = "Gift2Info";
                }
                formatPluralStringComma = LocaleController.formatPluralStringComma(str, i3);
            } else {
                formatPluralStringComma = LocaleController.formatPluralStringComma("Gift2Info2Out", (int) userStarGift.convert_stars, UserObject.getForcedFirstName(user));
            }
            linksTextView.setText(TextUtils.concat(AndroidUtilities.replaceTags(formatPluralStringComma), " ", AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    StarsIntroActivity.lambda$showGiftSheet$90(context);
                }
            }), true)));
            linearLayout.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 5, 5, 5, 4));
            i2 = currentTime;
        } else {
            i2 = 0;
        }
        final int max = Math.max(1, i2 / 86400);
        TableView tableView3 = new TableView(context, resourcesProvider);
        final long clientUserId = UserConfig.getInstance(i).getClientUserId();
        long j2 = ((userStarGift.flags & 2) == 0 || userStarGift.name_hidden) ? 2666000L : userStarGift.from_id;
        final TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(j2));
        final long j3 = j2;
        final long j4 = j2;
        tableView3.addRowUser(LocaleController.getString(R.string.Gift2From), i, j2, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$91(bottomSheetArr, j3, userStarGift, clientUserId);
            }
        }, (j2 == clientUserId || j2 == UserObject.ANONYMOUS || isBot || UserObject.isDeleted(user2)) ? null : LocaleController.getString(R.string.Gift2ButtonSendGift), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$92(context, i, j4, bottomSheetArr);
            }
        });
        tableView3.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
        if (z) {
            int i5 = userStarGift.flags;
            if ((i5 & 8) != 0 && (i5 & 16) != 0 && (i5 & 2) != 0 && i2 > 0) {
                final long j5 = j2;
                starGift = starGift2;
                linearLayout2 = linearLayout;
                tableView = tableView3;
                c = 2;
                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda60
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showGiftSheet$97(context, resourcesProvider, max, user2, j5, userStarGift, i, bottomSheetArr, j, clientUserId);
                    }
                };
                String string = LocaleController.getString(R.string.Gift2Value);
                String str2 = "⭐️ " + LocaleController.formatNumber(starGift.stars, ',');
                if (runnable != null) {
                    make = "";
                    resourcesProvider2 = resourcesProvider;
                } else {
                    resourcesProvider2 = resourcesProvider;
                    make = ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), runnable, resourcesProvider2);
                }
                CharSequence[] charSequenceArr = new CharSequence[3];
                charSequenceArr[0] = str2;
                charSequenceArr[1] = " ";
                charSequenceArr[c] = make;
                tableView2 = tableView;
                tableView2.addRow(string, replaceStarsWithPlain(TextUtils.concat(charSequenceArr), 0.8f));
                if (starGift.limited) {
                    addAvailabilityRow(tableView2, i, starGift, resourcesProvider2);
                }
                final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider2);
                final TL_stars.StarGift starGift3 = starGift;
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showGiftSheet$101(ButtonWithCounterView.this, userStarGift, i, bottomSheetArr, clientUserId, starGift3, resourcesProvider);
                    }
                };
                if (z) {
                    int i6 = userStarGift.flags;
                    if ((i6 & 8) != 0 && (i6 & 2) != 0) {
                        tableView2.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), runnable2);
                    }
                }
                tL_textWithEntities = userStarGift.message;
                if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
                    TLRPC.TL_textWithEntities tL_textWithEntities2 = userStarGift.message;
                    tableView2.addFullRow(tL_textWithEntities2.text, tL_textWithEntities2.entities);
                }
                LinearLayout linearLayout4 = linearLayout2;
                linearLayout4.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
                if (j == UserConfig.getInstance(i).getClientUserId()) {
                    LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider2);
                    linksTextView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider2));
                    linksTextView2.setTextSize(1, 14.0f);
                    linksTextView2.setGravity(17);
                    linksTextView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    linksTextView2.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider2));
                    linksTextView2.setDisablePaddingsOffsetY(true);
                    linksTextView2.setText(LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2ProfileVisible : R.string.Gift2ProfileInvisible));
                    linearLayout4.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 17, 5, 6, 5, 16));
                }
                ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider2);
                buttonWithCounterView2.setText(LocaleController.getString(R.string.OK), false);
                linearLayout4.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
                builder.setCustomView(linearLayout4);
                BottomSheet create = builder.create();
                bottomSheetArr[0] = create;
                create.useBackgroundTopPadding = false;
                buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda62
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarsIntroActivity.lambda$showGiftSheet$102(bottomSheetArr, view);
                    }
                });
                bottomSheetArr[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                    bottomSheetArr[0].makeAttached(safeLastFragment);
                }
                bottomSheetArr[0].show();
                return bottomSheetArr[0];
            }
        }
        tableView = tableView3;
        linearLayout2 = linearLayout;
        starGift = starGift2;
        c = 2;
        runnable = null;
        String string2 = LocaleController.getString(R.string.Gift2Value);
        String str22 = "⭐️ " + LocaleController.formatNumber(starGift.stars, ',');
        if (runnable != null) {
        }
        CharSequence[] charSequenceArr2 = new CharSequence[3];
        charSequenceArr2[0] = str22;
        charSequenceArr2[1] = " ";
        charSequenceArr2[c] = make;
        tableView2 = tableView;
        tableView2.addRow(string2, replaceStarsWithPlain(TextUtils.concat(charSequenceArr2), 0.8f));
        if (starGift.limited) {
        }
        final ButtonWithCounterView buttonWithCounterView3 = new ButtonWithCounterView(context, resourcesProvider2);
        final TL_stars.StarGift starGift32 = starGift;
        Runnable runnable22 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showGiftSheet$101(ButtonWithCounterView.this, userStarGift, i, bottomSheetArr, clientUserId, starGift32, resourcesProvider);
            }
        };
        if (z) {
        }
        tL_textWithEntities = userStarGift.message;
        if (tL_textWithEntities != null) {
            TLRPC.TL_textWithEntities tL_textWithEntities22 = userStarGift.message;
            tableView2.addFullRow(tL_textWithEntities22.text, tL_textWithEntities22.entities);
        }
        LinearLayout linearLayout42 = linearLayout2;
        linearLayout42.addView(tableView2, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
        if (j == UserConfig.getInstance(i).getClientUserId()) {
        }
        ButtonWithCounterView buttonWithCounterView22 = new ButtonWithCounterView(context, resourcesProvider2);
        buttonWithCounterView22.setText(LocaleController.getString(R.string.OK), false);
        linearLayout42.addView(buttonWithCounterView22, LayoutHelper.createLinear(-1, 48));
        builder.setCustomView(linearLayout42);
        BottomSheet create2 = builder.create();
        bottomSheetArr[0] = create2;
        create2.useBackgroundTopPadding = false;
        buttonWithCounterView22.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda62
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showGiftSheet$102(bottomSheetArr, view);
            }
        });
        bottomSheetArr[0].fixNavigationBar();
        safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet()) {
            bottomSheetArr[0].makeAttached(safeLastFragment);
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
        editTextBoldCursor.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda38
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z2) {
                StarsIntroActivity.lambda$showMediaPriceSheet$63(OutlineTextContainerView.this, editTextBoldCursor, view, z2);
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
        linksTextView.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.PaidContentInfo), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$64(context);
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
        editTextBoldCursor.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Stars.StarsIntroActivity.13
            private boolean ignore;
            private int shakeDp = 2;

            /* JADX WARN: Removed duplicated region for block: B:17:0x007d  */
            /* JADX WARN: Removed duplicated region for block: B:23:0x00a1  */
            /* JADX WARN: Removed duplicated region for block: B:26:0x00b5  */
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
        editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda40
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView3, int i2, KeyEvent keyEvent) {
                boolean lambda$showMediaPriceSheet$66;
                lambda$showMediaPriceSheet$66 = StarsIntroActivity.lambda$showMediaPriceSheet$66(zArr, callback2, buttonWithCounterView2, editTextBoldCursor, bottomSheetArr, textView3, i2, keyEvent);
                return lambda$showMediaPriceSheet$66;
            }
        });
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda41
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showMediaPriceSheet$68(zArr, callback2, editTextBoldCursor, buttonWithCounterView2, bottomSheetArr, view);
            }
        });
        if (buttonWithCounterView3 != null) {
            buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda42
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarsIntroActivity.lambda$showMediaPriceSheet$70(zArr, callback2, buttonWithCounterView3, editTextBoldCursor, bottomSheetArr, view);
                }
            });
        }
        bottomSheetArr[0].fixNavigationBar();
        bottomSheetArr[0].setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda43
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
            }
        });
        bottomSheetArr[0].show();
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showMediaPriceSheet$73(bottomSheetArr, editTextBoldCursor);
            }
        }, lastFragment instanceof ChatActivity ? ((ChatActivity) lastFragment).needEnterText() : false ? 200L : 80L);
        return bottomSheetArr[0];
    }

    public static BottomSheet showSoldOutGiftSheet(Context context, int i, TL_stars.StarGift starGift, Theme.ResourcesProvider resourcesProvider) {
        if (starGift == null || context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        BackupImageView backupImageView = new BackupImageView(context);
        setGiftImage(backupImageView.getImageReceiver(), starGift, NotificationCenter.audioRouteChanged);
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(LocaleController.getString(R.string.Gift2SoldOutSheetTitle));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(Theme.key_text_RedBold, resourcesProvider));
        textView2.setText(LocaleController.getString(R.string.Gift2SoldOutSheetSubtitle));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TableView tableView = new TableView(context, resourcesProvider);
        if (starGift.first_sale_date != 0) {
            tableView.addRowDateTime(LocaleController.getString(R.string.Gift2SoldOutSheetFirstSale), starGift.first_sale_date);
        }
        if (starGift.last_sale_date != 0) {
            tableView.addRowDateTime(LocaleController.getString(R.string.Gift2SoldOutSheetLastSale), starGift.last_sale_date);
        }
        tableView.addRow(LocaleController.getString(R.string.Gift2SoldOutSheetValue), replaceStarsWithPlain("⭐️ " + LocaleController.formatNumber(starGift.stars, ','), 0.8f));
        if (starGift.limited) {
            addAvailabilityRow(tableView, i, starGift, resourcesProvider);
        }
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 12.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
        builder.setCustomView(linearLayout);
        final BottomSheet[] bottomSheetArr = {builder.create()};
        bottomSheetArr[0].useBackgroundTopPadding = false;
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda69
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarsIntroActivity.lambda$showSoldOutGiftSheet$103(bottomSheetArr, view);
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01cc  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x02ac  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x063e  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02d3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showSubscriptionSheet(final Context context, final int i, final TL_stars.StarsSubscription starsSubscription, final Theme.ResourcesProvider resourcesProvider) {
        boolean[] zArr;
        TLRPC.Chat chat;
        String str;
        final boolean z;
        boolean z2;
        FrameLayout frameLayout;
        int i2;
        SpannableStringBuilder replaceStarsWithPlain;
        TableView tableView;
        NotificationCenter.NotificationCenterDelegate notificationCenterDelegate;
        String publicUsername;
        boolean z3;
        long currentTime;
        final NotificationCenter.NotificationCenterDelegate notificationCenterDelegate2;
        BaseFragment safeLastFragment;
        if (starsSubscription == null || context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        final BottomSheet[] bottomSheetArr = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(4.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        FrameLayout frameLayout2 = new FrameLayout(context);
        linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2, 7, 0, 0, 0, 10));
        final boolean[] zArr2 = new boolean[1];
        NotificationCenter.NotificationCenterDelegate notificationCenterDelegate3 = new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.Stars.StarsIntroActivity.9
            @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
            public void didReceivedNotification(int i3, int i4, Object... objArr) {
                BottomSheet bottomSheet;
                if (i3 == NotificationCenter.starSubscriptionsLoaded && zArr2[0] && (bottomSheet = bottomSheetArr[0]) != null) {
                    bottomSheet.dismiss();
                }
            }
        };
        NotificationCenter.getInstance(i).addObserver(notificationCenterDelegate3, NotificationCenter.starSubscriptionsLoaded);
        final long peerDialogId = DialogObject.getPeerDialogId(starsSubscription.peer);
        BackupImageView backupImageView = new BackupImageView(context);
        if (peerDialogId >= 0) {
            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
            String userName = UserObject.getUserName(user);
            boolean isBot = UserObject.isBot(user);
            chat = user;
            zArr = zArr2;
            str = userName;
            z = isBot;
            z2 = !isBot;
        } else {
            zArr = zArr2;
            TLRPC.Chat chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
            chat = chat2;
            str = chat2 == null ? "" : chat2.title;
            z = false;
            z2 = false;
        }
        if (starsSubscription.photo != null) {
            backupImageView.setRoundRadius(AndroidUtilities.dp(21.0f));
            backupImageView.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsSubscription.photo)), "100_100", (Drawable) null, 0, (Object) null);
        } else {
            backupImageView.setRoundRadius(AndroidUtilities.dp(50.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            if (peerDialogId < 0) {
                frameLayout = frameLayout2;
                TLRPC.Chat chat3 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
                avatarDrawable.setInfo(chat3);
                backupImageView.setForUserOrChat(chat3, avatarDrawable);
                ViewGroup viewGroup = frameLayout;
                viewGroup.addView(backupImageView, LayoutHelper.createFrame(100, 100, 17));
                Drawable drawable = context.getResources().getDrawable(R.drawable.star_small_outline);
                drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground, resourcesProvider), PorterDuff.Mode.SRC_IN));
                Drawable drawable2 = context.getResources().getDrawable(R.drawable.star_small_inner);
                if (starsSubscription.photo == null) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageDrawable(drawable);
                    viewGroup.addView(imageView, LayoutHelper.createFrame(28, 28, 17));
                    imageView.setTranslationX(AndroidUtilities.dp(34.0f));
                    imageView.setTranslationY(AndroidUtilities.dp(35.0f));
                    imageView.setScaleX(1.1f);
                    imageView.setScaleY(1.1f);
                    ImageView imageView2 = new ImageView(context);
                    imageView2.setImageDrawable(drawable2);
                    viewGroup.addView(imageView2, LayoutHelper.createFrame(28, 28, 17));
                    imageView2.setTranslationX(AndroidUtilities.dp(34.0f));
                    imageView2.setTranslationY(AndroidUtilities.dp(35.0f));
                }
                TextView textView = new TextView(context);
                textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
                textView.setTextSize(1, 20.0f);
                textView.setTypeface(AndroidUtilities.bold());
                textView.setGravity(17);
                textView.setText(TextUtils.isEmpty(starsSubscription.title) ? starsSubscription.title : LocaleController.getString(R.string.StarsSubscriptionTitle));
                linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                TextView textView2 = new TextView(context);
                textView2.setTextSize(1, 14.0f);
                textView2.setGravity(17);
                textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
                TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = starsSubscription.pricing;
                i2 = tL_starsSubscriptionPricing.period;
                if (i2 != 2592000) {
                    replaceStarsWithPlain = replaceStarsWithPlain(LocaleController.formatString(R.string.StarsSubscriptionPrice, Long.valueOf(tL_starsSubscriptionPricing.amount)), 0.8f);
                } else {
                    replaceStarsWithPlain = replaceStarsWithPlain(LocaleController.formatString(R.string.StarsSubscriptionPrice, Long.valueOf(tL_starsSubscriptionPricing.amount), i2 == 300 ? "5min" : "min"), 0.8f);
                }
                textView2.setText(replaceStarsWithPlain);
                linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                tableView = new TableView(context, resourcesProvider);
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
                if (peerDialogId < 0) {
                    TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
                    boolean z4 = user2 == null || UserObject.isDeleted(user2);
                    String publicUsername2 = UserObject.getPublicUsername(user2);
                    avatarSpan.setUser(user2);
                    z3 = z4;
                    notificationCenterDelegate = notificationCenterDelegate3;
                    publicUsername = publicUsername2;
                } else {
                    notificationCenterDelegate = notificationCenterDelegate3;
                    TLRPC.Chat chat4 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
                    boolean z5 = chat4 == null;
                    publicUsername = ChatObject.getPublicUsername(chat4);
                    avatarSpan.setChat(chat4);
                    z3 = z5;
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x  " + ((Object) publicUsername));
                spannableStringBuilder.setSpan(avatarSpan, 0, 1, 33);
                spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.10
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        bottomSheetArr[0].dismiss();
                        BaseFragment safeLastFragment2 = LaunchActivity.getSafeLastFragment();
                        if (safeLastFragment2 != null) {
                            safeLastFragment2.presentFragment(ChatActivity.of(peerDialogId));
                        }
                    }

                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        textPaint.setUnderlineText(false);
                    }
                }, 3, spannableStringBuilder.length(), 33);
                linksTextView.setText(spannableStringBuilder);
                if (!z3) {
                    tableView.addRowUnpadded(LocaleController.getString(peerDialogId < 0 ? R.string.StarsSubscriptionChannel : z2 ? R.string.StarsSubscriptionBusiness : R.string.StarsSubscriptionBot), linksTextView);
                }
                if (peerDialogId >= 0 && !TextUtils.isEmpty(starsSubscription.title)) {
                    tableView.addRow(LocaleController.getString(!z2 ? R.string.StarsSubscriptionBusinessProduct : R.string.StarsSubscriptionBotProduct), starsSubscription.title);
                }
                CharSequence string = LocaleController.getString(R.string.StarsSubscriptionSince);
                int i4 = R.string.formatDateAtTime;
                tableView.addRow(string, LocaleController.formatString(i4, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000))));
                currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
                tableView.addRow(LocaleController.getString((!starsSubscription.canceled || starsSubscription.bot_canceled) ? R.string.StarsSubscriptionUntilExpires : currentTime > ((long) starsSubscription.until_date) ? R.string.StarsSubscriptionUntilExpired : R.string.StarsSubscriptionUntilRenews), LocaleController.formatString(i4, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsSubscription.until_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsSubscription.until_date * 1000))));
                linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                int i5 = Theme.key_windowBackgroundWhiteGrayText2;
                linksTextView2.setTextColor(Theme.getColor(i5, resourcesProvider));
                linksTextView2.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                linksTextView2.setTextSize(1, 14.0f);
                linksTextView2.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda71
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showSubscriptionSheet$40(context);
                    }
                }));
                linksTextView2.setGravity(17);
                linearLayout.addView(linksTextView2, LayoutHelper.createLinear(-1, -2, 14.0f, 15.0f, 14.0f, 7.0f));
                if (currentTime < starsSubscription.until_date) {
                    notificationCenterDelegate2 = notificationCenterDelegate;
                    LinkSpanDrawable.LinksTextView linksTextView3 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                    linksTextView3.setTextColor(Theme.getColor(i5, resourcesProvider));
                    linksTextView3.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                    linksTextView3.setTextSize(1, 14.0f);
                    linksTextView3.setText(LocaleController.formatString(R.string.StarsSubscriptionExpiredInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
                    linksTextView3.setSingleLine(false);
                    linksTextView3.setMaxLines(4);
                    linksTextView3.setGravity(17);
                    linearLayout.addView(linksTextView3, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                    if (starsSubscription.chat_invite_hash != null || starsSubscription.invoice_slug != null) {
                        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, true, resourcesProvider);
                        buttonWithCounterView.setText(LocaleController.getString(R.string.StarsSubscriptionAgain), false);
                        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                        final boolean[] zArr3 = zArr;
                        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda75
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarsIntroActivity.lambda$showSubscriptionSheet$56(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, resourcesProvider, zArr3, context, view);
                            }
                        });
                    }
                } else if (starsSubscription.can_refulfill) {
                    LinkSpanDrawable.LinksTextView linksTextView4 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                    linksTextView4.setTextColor(Theme.getColor(i5, resourcesProvider));
                    linksTextView4.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                    linksTextView4.setTextSize(1, 14.0f);
                    linksTextView4.setText(LocaleController.formatString(z ? R.string.StarsSubscriptionBotRefulfillInfo : R.string.StarsSubscriptionRefulfillInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
                    linksTextView4.setSingleLine(false);
                    linksTextView4.setMaxLines(4);
                    linksTextView4.setGravity(17);
                    linearLayout.addView(linksTextView4, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                    final ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, true, resourcesProvider);
                    buttonWithCounterView2.setText(LocaleController.getString(z ? R.string.StarsSubscriptionBotRefulfill : R.string.StarsSubscriptionRefulfill), false);
                    linearLayout.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
                    final boolean z6 = z2;
                    notificationCenterDelegate2 = notificationCenterDelegate;
                    final String str2 = str;
                    buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda72
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarsIntroActivity.lambda$showSubscriptionSheet$44(ButtonWithCounterView.this, i, starsSubscription, bottomSheetArr, peerDialogId, context, resourcesProvider, z6, str2, view);
                        }
                    });
                } else {
                    notificationCenterDelegate2 = notificationCenterDelegate;
                    if (starsSubscription.bot_canceled) {
                        LinkSpanDrawable.LinksTextView linksTextView5 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                        linksTextView5.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
                        linksTextView5.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                        linksTextView5.setTextSize(1, 14.0f);
                        linksTextView5.setText(LocaleController.getString(z2 ? R.string.StarsSubscriptionBusinessCancelledText : R.string.StarsSubscriptionBotCancelledText));
                        linksTextView5.setSingleLine(false);
                        linksTextView5.setMaxLines(4);
                        linksTextView5.setGravity(17);
                        linearLayout.addView(linksTextView5, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                    } else if (starsSubscription.canceled) {
                        LinkSpanDrawable.LinksTextView linksTextView6 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                        linksTextView6.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
                        linksTextView6.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                        linksTextView6.setTextSize(1, 14.0f);
                        linksTextView6.setText(LocaleController.getString(R.string.StarsSubscriptionCancelledText));
                        linksTextView6.setSingleLine(false);
                        linksTextView6.setMaxLines(4);
                        linksTextView6.setGravity(17);
                        linearLayout.addView(linksTextView6, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                        if (starsSubscription.chat_invite_hash != null || starsSubscription.invoice_slug != null) {
                            final ButtonWithCounterView buttonWithCounterView3 = new ButtonWithCounterView(context, true, resourcesProvider);
                            buttonWithCounterView3.setText(LocaleController.getString(R.string.StarsSubscriptionRenew), false);
                            linearLayout.addView(buttonWithCounterView3, LayoutHelper.createLinear(-1, 48));
                            final TLRPC.Chat chat5 = chat;
                            final String str3 = str;
                            buttonWithCounterView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda73
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    StarsIntroActivity.lambda$showSubscriptionSheet$47(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, chat5, str3, view);
                                }
                            });
                        }
                    } else {
                        LinkSpanDrawable.LinksTextView linksTextView7 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
                        linksTextView7.setTextColor(Theme.getColor(i5, resourcesProvider));
                        linksTextView7.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
                        linksTextView7.setTextSize(1, 14.0f);
                        linksTextView7.setText(LocaleController.formatString(R.string.StarsSubscriptionCancelInfo, LocaleController.formatDateChat(starsSubscription.until_date)));
                        linksTextView7.setSingleLine(false);
                        linksTextView7.setMaxLines(4);
                        linksTextView7.setGravity(17);
                        linearLayout.addView(linksTextView7, LayoutHelper.createLinear(-1, -2, 26.0f, 7.0f, 26.0f, 15.0f));
                        final ButtonWithCounterView buttonWithCounterView4 = new ButtonWithCounterView(context, false, resourcesProvider);
                        buttonWithCounterView4.setText(LocaleController.getString(R.string.StarsSubscriptionCancel), false);
                        buttonWithCounterView4.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
                        linearLayout.addView(buttonWithCounterView4, LayoutHelper.createLinear(-1, 48));
                        final boolean z7 = z2;
                        final TLRPC.Chat chat6 = chat;
                        buttonWithCounterView4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda74
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarsIntroActivity.lambda$showSubscriptionSheet$50(ButtonWithCounterView.this, starsSubscription, i, bottomSheetArr, z7, z, chat6, view);
                            }
                        });
                    }
                }
                builder.setCustomView(linearLayout);
                BottomSheet create = builder.create();
                bottomSheetArr[0] = create;
                create.useBackgroundTopPadding = false;
                create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda76
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        StarsIntroActivity.lambda$showSubscriptionSheet$57(i, notificationCenterDelegate2, dialogInterface);
                    }
                });
                bottomSheetArr[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet() && !AndroidUtilities.hasDialogOnTop(safeLastFragment)) {
                    bottomSheetArr[0].makeAttached(safeLastFragment);
                }
                bottomSheetArr[0].show();
                return bottomSheetArr[0];
            }
            TLRPC.User user3 = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
            avatarDrawable.setInfo(user3);
            backupImageView.setForUserOrChat(user3, avatarDrawable);
        }
        frameLayout = frameLayout2;
        ViewGroup viewGroup2 = frameLayout;
        viewGroup2.addView(backupImageView, LayoutHelper.createFrame(100, 100, 17));
        Drawable drawable3 = context.getResources().getDrawable(R.drawable.star_small_outline);
        drawable3.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground, resourcesProvider), PorterDuff.Mode.SRC_IN));
        Drawable drawable22 = context.getResources().getDrawable(R.drawable.star_small_inner);
        if (starsSubscription.photo == null) {
        }
        TextView textView3 = new TextView(context);
        textView3.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView3.setTextSize(1, 20.0f);
        textView3.setTypeface(AndroidUtilities.bold());
        textView3.setGravity(17);
        textView3.setText(TextUtils.isEmpty(starsSubscription.title) ? starsSubscription.title : LocaleController.getString(R.string.StarsSubscriptionTitle));
        linearLayout.addView(textView3, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TextView textView22 = new TextView(context);
        textView22.setTextSize(1, 14.0f);
        textView22.setGravity(17);
        textView22.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4, resourcesProvider));
        TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing2 = starsSubscription.pricing;
        i2 = tL_starsSubscriptionPricing2.period;
        if (i2 != 2592000) {
        }
        textView22.setText(replaceStarsWithPlain);
        linearLayout.addView(textView22, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        tableView = new TableView(context, resourcesProvider);
        LinkSpanDrawable.LinksTextView linksTextView8 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        linksTextView8.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
        linksTextView8.setEllipsize(TextUtils.TruncateAt.END);
        int i32 = Theme.key_chat_messageLinkIn;
        linksTextView8.setTextColor(Theme.getColor(i32, resourcesProvider));
        linksTextView8.setLinkTextColor(Theme.getColor(i32, resourcesProvider));
        linksTextView8.setTextSize(1, 14.0f);
        linksTextView8.setSingleLine(true);
        linksTextView8.setDisablePaddingsOffsetY(true);
        AvatarSpan avatarSpan2 = new AvatarSpan(linksTextView8, i, 24.0f);
        if (peerDialogId < 0) {
        }
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("x  " + ((Object) publicUsername));
        spannableStringBuilder2.setSpan(avatarSpan2, 0, 1, 33);
        spannableStringBuilder2.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.10
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                bottomSheetArr[0].dismiss();
                BaseFragment safeLastFragment2 = LaunchActivity.getSafeLastFragment();
                if (safeLastFragment2 != null) {
                    safeLastFragment2.presentFragment(ChatActivity.of(peerDialogId));
                }
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setUnderlineText(false);
            }
        }, 3, spannableStringBuilder2.length(), 33);
        linksTextView8.setText(spannableStringBuilder2);
        if (!z3) {
        }
        if (peerDialogId >= 0) {
            tableView.addRow(LocaleController.getString(!z2 ? R.string.StarsSubscriptionBusinessProduct : R.string.StarsSubscriptionBotProduct), starsSubscription.title);
        }
        CharSequence string2 = LocaleController.getString(R.string.StarsSubscriptionSince);
        int i42 = R.string.formatDateAtTime;
        tableView.addRow(string2, LocaleController.formatString(i42, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date((starsSubscription.until_date - starsSubscription.pricing.period) * 1000))));
        currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
        tableView.addRow(LocaleController.getString((!starsSubscription.canceled || starsSubscription.bot_canceled) ? R.string.StarsSubscriptionUntilExpires : currentTime > ((long) starsSubscription.until_date) ? R.string.StarsSubscriptionUntilExpired : R.string.StarsSubscriptionUntilRenews), LocaleController.formatString(i42, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsSubscription.until_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsSubscription.until_date * 1000))));
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
        LinkSpanDrawable.LinksTextView linksTextView22 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        int i52 = Theme.key_windowBackgroundWhiteGrayText2;
        linksTextView22.setTextColor(Theme.getColor(i52, resourcesProvider));
        linksTextView22.setLinkTextColor(Theme.getColor(i32, resourcesProvider));
        linksTextView22.setTextSize(1, 14.0f);
        linksTextView22.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showSubscriptionSheet$40(context);
            }
        }));
        linksTextView22.setGravity(17);
        linearLayout.addView(linksTextView22, LayoutHelper.createLinear(-1, -2, 14.0f, 15.0f, 14.0f, 7.0f));
        if (currentTime < starsSubscription.until_date) {
        }
        builder.setCustomView(linearLayout);
        BottomSheet create2 = builder.create();
        bottomSheetArr[0] = create2;
        create2.useBackgroundTopPadding = false;
        create2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda76
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarsIntroActivity.lambda$showSubscriptionSheet$57(i, notificationCenterDelegate2, dialogInterface);
            }
        });
        bottomSheetArr[0].fixNavigationBar();
        safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (!AndroidUtilities.isTablet()) {
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
    /* JADX WARN: Removed duplicated region for block: B:101:0x09da  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0a70  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0aa0  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0ac2  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0aaa  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0a7b  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x03ff  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0382  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x045d  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x070a  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0718  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0729  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x07c6  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x07f9 A[LOOP:0: B:58:0x0775->B:68:0x07f9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0805 A[EDGE_INSN: B:69:0x0805->B:70:0x0805 BREAK  A[LOOP:0: B:58:0x0775->B:68:0x07f9], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0881 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0945  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x09a4  */
    /* JADX WARN: Type inference failed for: r4v23 */
    /* JADX WARN: Type inference failed for: r4v24, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v25 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet showTransactionSheet(final Context context, final boolean z, final long j, final int i, final TL_stars.StarsTransaction starsTransaction, final Theme.ResourcesProvider resourcesProvider) {
        String str;
        BottomSheet[] bottomSheetArr;
        BottomSheet.Builder builder;
        String str2;
        ViewGroup viewGroup;
        ViewGroup.LayoutParams createLinear;
        View view;
        ViewGroup viewGroup2;
        TLRPC.Chat chat;
        ImageLocation imageLocation;
        ImageLocation forDocument;
        int i2;
        boolean isUserSelf;
        ViewGroup viewGroup3;
        char c;
        String string;
        final BottomSheet[] bottomSheetArr2;
        ViewGroup viewGroup4;
        String str3;
        CharSequence charSequence;
        final BottomSheet[] bottomSheetArr3;
        final TL_stars.StarsTransaction starsTransaction2;
        final Context context2;
        Theme.ResourcesProvider resourcesProvider2;
        int i3;
        CharSequence string2;
        int i4;
        CharSequence string3;
        Runnable runnable;
        TableView tableView;
        TL_stars.StarsTransactionPeer starsTransactionPeer;
        ViewGroup viewGroup5;
        final Theme.ResourcesProvider resourcesProvider3;
        TL_stars.StarGift starGift;
        ?? r4;
        String string4;
        BaseFragment safeLastFragment;
        TLRPC.Chat chat2;
        String sb;
        ImageLocation imageLocation2;
        ImageLocation forDocument2;
        TableView tableView2;
        if (starsTransaction == null || context == null) {
            return null;
        }
        boolean z2 = (starsTransaction.flags & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
        BottomSheet.Builder builder2 = new BottomSheet.Builder(context, false, resourcesProvider);
        BottomSheet[] bottomSheetArr4 = new BottomSheet[1];
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp((z2 || starsTransaction.gift) ? 0.0f : 20.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        final BackupImageView backupImageView = new BackupImageView(context);
        if (starsTransaction.stargift != null) {
            setGiftImage(backupImageView.getImageReceiver(), starsTransaction.stargift, NotificationCenter.audioRouteChanged);
            linearLayout.addView(backupImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10));
            str = "/";
            bottomSheetArr = bottomSheetArr4;
            viewGroup2 = linearLayout;
            builder = builder2;
            str2 = "fragment";
        } else {
            if (z2 || starsTransaction.gift) {
                str = "/";
                bottomSheetArr = bottomSheetArr4;
                builder = builder2;
                str2 = "fragment";
                BackupImageView backupImageView2 = backupImageView;
                viewGroup = linearLayout;
                setGiftImage(backupImageView2, backupImageView2.getImageReceiver(), starsTransaction.stars);
                createLinear = LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17, 0, -8, 0, 10);
                view = backupImageView2;
            } else if (starsTransaction.extended_media.isEmpty()) {
                str = "/";
                bottomSheetArr = bottomSheetArr4;
                builder = builder2;
                str2 = "fragment";
                BackupImageView backupImageView3 = backupImageView;
                TL_stars.StarsTransactionPeer starsTransactionPeer2 = starsTransaction.peer;
                if (starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeer) {
                    if (starsTransaction.photo != null) {
                        backupImageView3.setRoundRadius(AndroidUtilities.dp(50.0f));
                        backupImageView3.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(starsTransaction.photo)), "100_100", (Drawable) null, 0, (Object) null);
                    } else {
                        backupImageView3.setRoundRadius(AndroidUtilities.dp(50.0f));
                        long peerDialogId = (starsTransaction.subscription && z) ? j : DialogObject.getPeerDialogId(starsTransaction.peer.peer);
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        if (peerDialogId >= 0) {
                            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId));
                            avatarDrawable.setInfo(user);
                            chat = user;
                        } else {
                            TLRPC.Chat chat3 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
                            avatarDrawable.setInfo(chat3);
                            chat = chat3;
                        }
                        backupImageView3.setForUserOrChat(chat, avatarDrawable);
                    }
                    createLinear = LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10);
                    viewGroup = linearLayout;
                    view = backupImageView3;
                } else {
                    viewGroup2 = linearLayout;
                    CombinedDrawable createDrawable = SessionCell.createDrawable(100, starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerAppStore ? "ios" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerPlayMarket ? "android" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerPremiumBot ? "premiumbot" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerFragment ? str2 : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerAds ? "ads" : starsTransactionPeer2 instanceof TL_stars.TL_starsTransactionPeerAPI ? "api" : "?");
                    createDrawable.setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                    backupImageView3.setImageDrawable(createDrawable);
                }
            } else {
                backupImageView.setRoundRadius(AndroidUtilities.dp(30.0f));
                TLRPC.MessageMedia messageMedia = starsTransaction.extended_media.get(0);
                if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                    forDocument = ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(messageMedia.photo.sizes, AndroidUtilities.dp(100.0f), true), messageMedia.photo);
                } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    forDocument = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(messageMedia.document.thumbs, AndroidUtilities.dp(100.0f), true), messageMedia.document);
                } else {
                    imageLocation = null;
                    backupImageView.setImage(imageLocation, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                    linearLayout.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                    str = "/";
                    bottomSheetArr = bottomSheetArr4;
                    builder = builder2;
                    str2 = "fragment";
                    final LinearLayout linearLayout2 = linearLayout;
                    backupImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            StarsIntroActivity.lambda$showTransactionSheet$21(z, j, starsTransaction, i, resourcesProvider, backupImageView, linearLayout2, view2);
                        }
                    });
                    viewGroup2 = linearLayout2;
                }
                imageLocation = forDocument;
                backupImageView.setImage(imageLocation, "100_100", (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                linearLayout.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0, 0, 0, 10));
                str = "/";
                bottomSheetArr = bottomSheetArr4;
                builder = builder2;
                str2 = "fragment";
                final LinearLayout linearLayout22 = linearLayout;
                backupImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        StarsIntroActivity.lambda$showTransactionSheet$21(z, j, starsTransaction, i, resourcesProvider, backupImageView, linearLayout22, view2);
                    }
                });
                viewGroup2 = linearLayout22;
            }
            viewGroup.addView(view, createLinear);
            viewGroup2 = viewGroup;
        }
        TextView textView = new TextView(context);
        int i5 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i5, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setText(getTransactionTitle(i, z, starsTransaction));
        viewGroup2.addView(textView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
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
        } else {
            if (!starsTransaction.failed) {
                if (starsTransaction.pending) {
                    textView2.setTextColor(Theme.getColor(Theme.key_color_yellow, resourcesProvider));
                    i2 = R.string.StarsPending;
                }
                textView2.setText(spannableStringBuilder);
                viewGroup2.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                if (!z2 || starsTransaction.gift) {
                    TLRPC.User user2 = starsTransaction.sent_by != null ? null : MessagesController.getInstance(i).getUser(Long.valueOf(DialogObject.getPeerDialogId(starsTransaction.sent_by)));
                    TLRPC.User user3 = starsTransaction.sent_by != null ? null : MessagesController.getInstance(i).getUser(Long.valueOf(DialogObject.getPeerDialogId(starsTransaction.received_by)));
                    isUserSelf = UserObject.isUserSelf(user2);
                    if (isUserSelf) {
                        viewGroup3 = viewGroup2;
                    } else {
                        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                        StringBuilder sb3 = new StringBuilder();
                        viewGroup3 = viewGroup2;
                        sb3.append(LocaleController.formatNumber((int) Math.abs(starsTransaction.stars), ' '));
                        sb3.append(" ⭐️");
                        textView2.setText(replaceStarsWithPlain(sb3.toString(), 0.8f));
                    }
                    LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
                    linksTextView.setTextColor(Theme.getColor(i5, resourcesProvider));
                    linksTextView.setTextSize(1, 16.0f);
                    linksTextView.setGravity(17);
                    linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                    linksTextView.setDisablePaddingsOffsetY(true);
                    if (isUserSelf) {
                        c = 0;
                        string = LocaleController.getString(R.string.ActionGiftStarsSubtitleYou);
                    } else {
                        c = 0;
                        string = LocaleController.formatString(R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user3));
                    }
                    SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(string);
                    bottomSheetArr2 = bottomSheetArr;
                    CharSequence replaceArrows = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.GiftStarsSubtitleLinkName).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarsIntroActivity.lambda$showTransactionSheet$22(context, bottomSheetArr2);
                        }
                    }), true);
                    CharSequence[] charSequenceArr = new CharSequence[3];
                    charSequenceArr[c] = replaceTags;
                    charSequenceArr[1] = " ";
                    charSequenceArr[2] = replaceArrows;
                    linksTextView.setText(TextUtils.concat(charSequenceArr));
                    viewGroup4 = viewGroup3;
                    viewGroup4.addView(linksTextView, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                } else {
                    if (starsTransaction.description != null && starsTransaction.extended_media.isEmpty()) {
                        TextView textView3 = new TextView(context);
                        textView3.setTextColor(Theme.getColor(i5, resourcesProvider));
                        textView3.setTextSize(1, 16.0f);
                        textView3.setGravity(17);
                        textView3.setText(starsTransaction.description);
                        viewGroup2.addView(textView3, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
                    }
                    viewGroup4 = viewGroup2;
                    bottomSheetArr2 = bottomSheetArr;
                }
                TableView tableView3 = new TableView(context, resourcesProvider);
                if (starsTransaction.stargift != null) {
                    ViewGroup viewGroup6 = viewGroup4;
                    final BottomSheet[] bottomSheetArr5 = bottomSheetArr2;
                    str3 = str;
                    TL_stars.StarsTransactionPeer starsTransactionPeer3 = starsTransaction.peer;
                    if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeer) {
                        final long peerDialogId2 = DialogObject.getPeerDialogId(starsTransactionPeer3.peer);
                        if (z2) {
                            charSequence = " ";
                            viewGroup4 = viewGroup6;
                            TableView tableView4 = tableView3;
                            bottomSheetArr3 = bottomSheetArr5;
                            tableView3.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeFrom), i, peerDialogId2, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda18
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarsIntroActivity.lambda$showTransactionSheet$29(bottomSheetArr5, starsTransaction, peerDialogId2);
                                }
                            });
                            tableView4.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeTo), i, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda19
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarsIntroActivity.lambda$showTransactionSheet$30(bottomSheetArr3, i);
                                }
                            });
                            tableView4.addRowLink(LocaleController.getString(R.string.StarGiveawayReason), LocaleController.getString(R.string.StarGiveawayReasonLink), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarsIntroActivity.lambda$showTransactionSheet$31(bottomSheetArr3, starsTransaction, peerDialogId2);
                                }
                            });
                            tableView4.addRow(LocaleController.getString(R.string.StarGiveawayGift), LocaleController.formatPluralStringComma("Stars", (int) starsTransaction.stars));
                            resourcesProvider2 = resourcesProvider;
                            starsTransaction2 = starsTransaction;
                            i3 = i;
                            tableView2 = tableView4;
                            context2 = context;
                            tableView = tableView2;
                        } else {
                            charSequence = " ";
                            viewGroup4 = viewGroup6;
                            TableView tableView5 = tableView3;
                            bottomSheetArr3 = bottomSheetArr5;
                            if (!starsTransaction.subscription || z) {
                                starsTransaction2 = starsTransaction;
                                context2 = context;
                                string3 = LocaleController.getString(R.string.StarsTransactionRecipient);
                                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StarsIntroActivity.lambda$showTransactionSheet$33(bottomSheetArr3, peerDialogId2, context2);
                                    }
                                };
                            } else {
                                string3 = LocaleController.getString(R.string.StarSubscriptionTo);
                                starsTransaction2 = starsTransaction;
                                context2 = context;
                                runnable = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda3
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StarsIntroActivity.lambda$showTransactionSheet$32(bottomSheetArr3, peerDialogId2, context2);
                                    }
                                };
                            }
                            tableView5.addRowUser(string3, i, peerDialogId2, runnable);
                            resourcesProvider2 = resourcesProvider;
                            i3 = i;
                            tableView = tableView5;
                        }
                    } else {
                        charSequence = " ";
                        viewGroup4 = viewGroup6;
                        TableView tableView6 = tableView3;
                        bottomSheetArr3 = bottomSheetArr5;
                        starsTransaction2 = starsTransaction;
                        context2 = context;
                        if (!(starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerFragment)) {
                            resourcesProvider2 = resourcesProvider;
                            i3 = i;
                            if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerAppStore) {
                                string2 = LocaleController.getString(R.string.StarsTransactionSource);
                                i4 = R.string.AppStore;
                            } else if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerPlayMarket) {
                                string2 = LocaleController.getString(R.string.StarsTransactionSource);
                                i4 = R.string.PlayMarket;
                            } else {
                                tableView = tableView6;
                                if (starsTransactionPeer3 instanceof TL_stars.TL_starsTransactionPeerPremiumBot) {
                                    string2 = LocaleController.getString(R.string.StarsTransactionSource);
                                    i4 = R.string.StarsTransactionBot;
                                }
                            }
                            tableView6.addRow(string2, LocaleController.getString(i4));
                            tableView = tableView6;
                        } else if (starsTransaction2.gift) {
                            resourcesProvider2 = resourcesProvider;
                            i3 = i;
                            LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context2, resourcesProvider2);
                            linksTextView2.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
                            linksTextView2.setEllipsize(TextUtils.TruncateAt.END);
                            int i6 = Theme.key_chat_messageLinkIn;
                            linksTextView2.setTextColor(Theme.getColor(i6, resourcesProvider2));
                            linksTextView2.setLinkTextColor(Theme.getColor(i6, resourcesProvider2));
                            linksTextView2.setTextSize(1, 14.0f);
                            linksTextView2.setSingleLine(true);
                            linksTextView2.setDisablePaddingsOffsetY(true);
                            AvatarSpan avatarSpan = new AvatarSpan(linksTextView2, i3, 24.0f);
                            String string5 = LocaleController.getString(R.string.StarsTransactionUnknown);
                            CombinedDrawable platformDrawable = StarsTransactionView.getPlatformDrawable(str2, 24);
                            platformDrawable.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                            avatarSpan.setImageDrawable(platformDrawable);
                            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("x  " + ((Object) string5));
                            spannableStringBuilder2.setSpan(avatarSpan, 0, 1, 33);
                            spannableStringBuilder2.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.7
                                @Override // android.text.style.ClickableSpan
                                public void onClick(View view2) {
                                    bottomSheetArr3[0].dismiss();
                                    Browser.openUrl(context2, LocaleController.getString(R.string.StarsTransactionUnknownLink));
                                }

                                @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                                public void updateDrawState(TextPaint textPaint) {
                                    textPaint.setUnderlineText(false);
                                }
                            }, 3, spannableStringBuilder2.length(), 33);
                            linksTextView2.setText(spannableStringBuilder2);
                            tableView6.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionRecipient), linksTextView2);
                            tableView = tableView6;
                        } else {
                            resourcesProvider2 = resourcesProvider;
                            i3 = i;
                            string2 = LocaleController.getString(R.string.StarsTransactionSource);
                            i4 = R.string.Fragment;
                            tableView6.addRow(string2, LocaleController.getString(i4));
                            tableView = tableView6;
                        }
                    }
                    starsTransactionPeer = starsTransaction2.peer;
                    if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) {
                    }
                    viewGroup5 = viewGroup4;
                    if (TextUtils.isEmpty(starsTransaction2.id)) {
                    }
                    resourcesProvider3 = resourcesProvider;
                    if (starsTransaction2.floodskip) {
                    }
                    CharSequence string6 = LocaleController.getString(R.string.StarsTransactionDate);
                    int i7 = R.string.formatDateAtTime;
                    tableView.addRow(string6, LocaleController.formatString(i7, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction2.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction2.date * 1000))));
                    starGift = starsTransaction2.stargift;
                    if (starGift != null) {
                    }
                    ViewGroup viewGroup7 = viewGroup5;
                    viewGroup7.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                    if ((starsTransaction2.flags & 32) != 0) {
                    }
                    LinkSpanDrawable.LinksTextView linksTextView3 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider3);
                    linksTextView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider3));
                    linksTextView3.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider3));
                    linksTextView3.setTextSize(1, 14.0f);
                    linksTextView3.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarsIntroActivity.lambda$showTransactionSheet$37(context);
                        }
                    }));
                    linksTextView3.setGravity(17);
                    viewGroup7.addView(linksTextView3, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                    ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider3);
                    if ((starsTransaction2.flags & 32) == 0) {
                    }
                    buttonWithCounterView.setText(string4, r4);
                    viewGroup7.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48));
                    BottomSheet.Builder builder3 = builder;
                    builder3.setCustomView(viewGroup7);
                    BottomSheet create = builder3.create();
                    bottomSheetArr3[r4] = create;
                    create.useBackgroundTopPadding = r4;
                    if ((starsTransaction2.flags & 32) == 0) {
                    }
                    bottomSheetArr3[0].fixNavigationBar();
                    safeLastFragment = LaunchActivity.getSafeLastFragment();
                    if (!AndroidUtilities.isTablet()) {
                    }
                    bottomSheetArr3[0].show();
                    return bottomSheetArr3[0];
                }
                if (starsTransaction.refund) {
                    str3 = str;
                    resourcesProvider2 = resourcesProvider;
                    i3 = i;
                    tableView2 = tableView3;
                    charSequence = " ";
                    bottomSheetArr3 = bottomSheetArr2;
                    starsTransaction2 = starsTransaction;
                } else {
                    final long peerDialogId3 = DialogObject.getPeerDialogId(starsTransaction.peer.peer);
                    TLRPC.User user4 = MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId3));
                    if (starsTransaction.stars <= 0) {
                        str3 = str;
                        ViewGroup viewGroup8 = viewGroup4;
                        final BottomSheet[] bottomSheetArr6 = bottomSheetArr2;
                        tableView3.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeFrom), i, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda15
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$26(bottomSheetArr6, i);
                            }
                        });
                        tableView3.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeTo), i, peerDialogId3, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda16
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$27(bottomSheetArr6, starsTransaction, peerDialogId3);
                            }
                        }, !UserObject.isDeleted(user4) ? LocaleController.getString(R.string.Gift2ButtonSendGift) : null, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$28(context, i, peerDialogId3, bottomSheetArr6);
                            }
                        });
                        charSequence = " ";
                        viewGroup4 = viewGroup8;
                        tableView = tableView3;
                        bottomSheetArr3 = bottomSheetArr6;
                        starsTransaction2 = starsTransaction;
                        i3 = i;
                        context2 = context;
                        resourcesProvider2 = resourcesProvider;
                        starsTransactionPeer = starsTransaction2.peer;
                        if ((starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) && (starsTransaction2.flags & 256) != 0) {
                            final long peerDialogId4 = DialogObject.getPeerDialogId(starsTransactionPeer.peer);
                            if (z) {
                                peerDialogId4 = j;
                            }
                            chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId4));
                            if (chat2 != null) {
                                LinkSpanDrawable.LinksTextView linksTextView4 = new LinkSpanDrawable.LinksTextView(context2, resourcesProvider2);
                                linksTextView4.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f));
                                linksTextView4.setEllipsize(TextUtils.TruncateAt.END);
                                int i8 = Theme.key_chat_messageLinkIn;
                                linksTextView4.setTextColor(Theme.getColor(i8, resourcesProvider2));
                                linksTextView4.setLinkTextColor(Theme.getColor(i8, resourcesProvider2));
                                linksTextView4.setTextSize(1, 14.0f);
                                linksTextView4.setDisablePaddingsOffsetY(true);
                                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder("");
                                if (!starsTransaction2.extended_media.isEmpty()) {
                                    Iterator<TLRPC.MessageMedia> it = starsTransaction2.extended_media.iterator();
                                    int i9 = 0;
                                    while (it.hasNext()) {
                                        TLRPC.MessageMedia next = it.next();
                                        Iterator<TLRPC.MessageMedia> it2 = it;
                                        viewGroup5 = viewGroup4;
                                        ImageReceiverSpan imageReceiverSpan = new ImageReceiverSpan(linksTextView4, i3, 24.0f);
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
                                                spannableStringBuilder3.append((CharSequence) spannableString);
                                                spannableStringBuilder3.append(charSequence);
                                                i9++;
                                            }
                                            if (i9 < 3) {
                                                break;
                                            }
                                            it = it2;
                                            i3 = i;
                                            viewGroup4 = viewGroup5;
                                        }
                                        imageLocation2 = forDocument2;
                                        if (imageLocation2 != null) {
                                        }
                                        if (i9 < 3) {
                                        }
                                    }
                                }
                                viewGroup5 = viewGroup4;
                                spannableStringBuilder3.append(charSequence);
                                int length = spannableStringBuilder3.length();
                                String publicUsername = ChatObject.getPublicUsername(chat2);
                                if (TextUtils.isEmpty(publicUsername)) {
                                    sb = chat2.title;
                                } else {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(MessagesController.getInstance(i).linkPrefix);
                                    String str4 = str3;
                                    sb4.append(str4);
                                    sb4.append(publicUsername);
                                    sb4.append(str4);
                                    sb4.append(starsTransaction2.msg_id);
                                    sb = sb4.toString();
                                }
                                spannableStringBuilder3.append((CharSequence) sb);
                                final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda5
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StarsIntroActivity.lambda$showTransactionSheet$34(bottomSheetArr3, peerDialogId4, starsTransaction2);
                                    }
                                };
                                spannableStringBuilder3.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarsIntroActivity.8
                                    @Override // android.text.style.ClickableSpan
                                    public void onClick(View view2) {
                                        runnable2.run();
                                    }

                                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                                    public void updateDrawState(TextPaint textPaint) {
                                        textPaint.setUnderlineText(false);
                                    }
                                }, length, spannableStringBuilder3.length(), 33);
                                linksTextView4.setSingleLine(true);
                                linksTextView4.setEllipsize(TextUtils.TruncateAt.END);
                                linksTextView4.setText(spannableStringBuilder3);
                                linksTextView4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda6
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view2) {
                                        runnable2.run();
                                    }
                                });
                                tableView.addRowUnpadded(LocaleController.getString(starsTransaction2.reaction ? R.string.StarsTransactionMessage : R.string.StarsTransactionMedia), linksTextView4);
                                if (!TextUtils.isEmpty(starsTransaction2.id) || z2) {
                                    resourcesProvider3 = resourcesProvider;
                                } else {
                                    FrameLayout frameLayout = new FrameLayout(context2);
                                    frameLayout.setPadding(AndroidUtilities.dp(12.66f), AndroidUtilities.dp(9.33f), AndroidUtilities.dp(10.66f), AndroidUtilities.dp(9.33f));
                                    TextView textView4 = new TextView(context2);
                                    textView4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MONO));
                                    textView4.setTextSize(1, starsTransaction2.id.length() > 25 ? 9.0f : 10.0f);
                                    resourcesProvider3 = resourcesProvider;
                                    textView4.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider3));
                                    textView4.setMaxLines(4);
                                    textView4.setSingleLine(false);
                                    textView4.setText(starsTransaction2.id);
                                    frameLayout.addView(textView4, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 34.0f, 0.0f));
                                    ImageView imageView = new ImageView(context2);
                                    imageView.setImageResource(R.drawable.msg_copy);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                                    int i10 = Theme.key_windowBackgroundWhiteBlueIcon;
                                    imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i10, resourcesProvider3), PorterDuff.Mode.SRC_IN));
                                    imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda7
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            StarsIntroActivity.lambda$showTransactionSheet$36(TL_stars.StarsTransaction.this, bottomSheetArr3, resourcesProvider3, view2);
                                        }
                                    });
                                    ScaleStateListAnimator.apply(imageView);
                                    imageView.setBackground(Theme.createSelectorDrawable(Theme.multAlpha(Theme.getColor(i10, resourcesProvider3), 0.1f), 7));
                                    frameLayout.addView(imageView, LayoutHelper.createFrame(30, 30, 21));
                                    tableView.addRowUnpadded(LocaleController.getString(R.string.StarsTransactionID), frameLayout);
                                }
                                if (starsTransaction2.floodskip && starsTransaction2.floodskip_number > 0) {
                                    tableView.addRow(LocaleController.getString(R.string.StarsTransactionFloodskipNumberName), LocaleController.formatPluralStringComma("StarsTransactionFloodskipNumber", starsTransaction2.floodskip_number));
                                }
                                CharSequence string62 = LocaleController.getString(R.string.StarsTransactionDate);
                                int i72 = R.string.formatDateAtTime;
                                tableView.addRow(string62, LocaleController.formatString(i72, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction2.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction2.date * 1000))));
                                starGift = starsTransaction2.stargift;
                                if (starGift != null) {
                                    if (starGift.limited) {
                                        addAvailabilityRow(tableView, i, starGift, resourcesProvider3);
                                    }
                                    if (!TextUtils.isEmpty(starsTransaction2.description)) {
                                        tableView.addFullRow(new SpannableStringBuilder(starsTransaction2.description));
                                    }
                                }
                                ViewGroup viewGroup72 = viewGroup5;
                                viewGroup72.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                                if ((starsTransaction2.flags & 32) != 0) {
                                    tableView.addRow(LocaleController.getString(R.string.StarsTransactionTONDate), LocaleController.formatString(i72, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction2.transaction_date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction2.transaction_date * 1000))));
                                }
                                LinkSpanDrawable.LinksTextView linksTextView32 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider3);
                                linksTextView32.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider3));
                                linksTextView32.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider3));
                                linksTextView32.setTextSize(1, 14.0f);
                                linksTextView32.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StarsIntroActivity.lambda$showTransactionSheet$37(context);
                                    }
                                }));
                                linksTextView32.setGravity(17);
                                viewGroup72.addView(linksTextView32, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                                ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, resourcesProvider3);
                                if ((starsTransaction2.flags & 32) == 0) {
                                    string4 = LocaleController.getString(R.string.StarsTransactionViewInBlockchainExplorer);
                                    r4 = 0;
                                } else {
                                    r4 = 0;
                                    string4 = LocaleController.getString(R.string.OK);
                                }
                                buttonWithCounterView2.setText(string4, r4);
                                viewGroup72.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48));
                                BottomSheet.Builder builder32 = builder;
                                builder32.setCustomView(viewGroup72);
                                BottomSheet create2 = builder32.create();
                                bottomSheetArr3[r4] = create2;
                                create2.useBackgroundTopPadding = r4;
                                if ((starsTransaction2.flags & 32) == 0) {
                                    buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda9
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            StarsIntroActivity.lambda$showTransactionSheet$38(context, starsTransaction2, view2);
                                        }
                                    });
                                } else {
                                    buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda10
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            StarsIntroActivity.lambda$showTransactionSheet$39(bottomSheetArr3, view2);
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
                        }
                        viewGroup5 = viewGroup4;
                        if (TextUtils.isEmpty(starsTransaction2.id)) {
                        }
                        resourcesProvider3 = resourcesProvider;
                        if (starsTransaction2.floodskip) {
                            tableView.addRow(LocaleController.getString(R.string.StarsTransactionFloodskipNumberName), LocaleController.formatPluralStringComma("StarsTransactionFloodskipNumber", starsTransaction2.floodskip_number));
                        }
                        CharSequence string622 = LocaleController.getString(R.string.StarsTransactionDate);
                        int i722 = R.string.formatDateAtTime;
                        tableView.addRow(string622, LocaleController.formatString(i722, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction2.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction2.date * 1000))));
                        starGift = starsTransaction2.stargift;
                        if (starGift != null) {
                        }
                        ViewGroup viewGroup722 = viewGroup5;
                        viewGroup722.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                        if ((starsTransaction2.flags & 32) != 0) {
                        }
                        LinkSpanDrawable.LinksTextView linksTextView322 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider3);
                        linksTextView322.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider3));
                        linksTextView322.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider3));
                        linksTextView322.setTextSize(1, 14.0f);
                        linksTextView322.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarsIntroActivity.lambda$showTransactionSheet$37(context);
                            }
                        }));
                        linksTextView322.setGravity(17);
                        viewGroup722.addView(linksTextView322, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                        ButtonWithCounterView buttonWithCounterView22 = new ButtonWithCounterView(context, resourcesProvider3);
                        if ((starsTransaction2.flags & 32) == 0) {
                        }
                        buttonWithCounterView22.setText(string4, r4);
                        viewGroup722.addView(buttonWithCounterView22, LayoutHelper.createLinear(-1, 48));
                        BottomSheet.Builder builder322 = builder;
                        builder322.setCustomView(viewGroup722);
                        BottomSheet create22 = builder322.create();
                        bottomSheetArr3[r4] = create22;
                        create22.useBackgroundTopPadding = r4;
                        if ((starsTransaction2.flags & 32) == 0) {
                        }
                        bottomSheetArr3[0].fixNavigationBar();
                        safeLastFragment = LaunchActivity.getSafeLastFragment();
                        if (!AndroidUtilities.isTablet()) {
                            bottomSheetArr3[0].makeAttached(safeLastFragment);
                        }
                        bottomSheetArr3[0].show();
                        return bottomSheetArr3[0];
                    }
                    str3 = str;
                    final BottomSheet[] bottomSheetArr7 = bottomSheetArr2;
                    final BottomSheet[] bottomSheetArr8 = bottomSheetArr2;
                    tableView3.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeFrom), i, peerDialogId3, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarsIntroActivity.lambda$showTransactionSheet$23(bottomSheetArr2, starsTransaction, peerDialogId3);
                        }
                    }, !UserObject.isDeleted(user4) ? LocaleController.getString(R.string.Gift2ButtonSendGift) : null, new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarsIntroActivity.lambda$showTransactionSheet$24(context, i, peerDialogId3, bottomSheetArr7);
                        }
                    });
                    tableView3.addRowUser(LocaleController.getString(R.string.StarGiveawayPrizeTo), i, UserConfig.getInstance(i).getClientUserId(), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda14
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarsIntroActivity.lambda$showTransactionSheet$25(bottomSheetArr8, i);
                        }
                    });
                    resourcesProvider2 = resourcesProvider;
                    starsTransaction2 = starsTransaction;
                    bottomSheetArr3 = bottomSheetArr8;
                    tableView2 = tableView3;
                    viewGroup4 = viewGroup4;
                    charSequence = " ";
                    i3 = i;
                }
                context2 = context;
                tableView = tableView2;
                starsTransactionPeer = starsTransaction2.peer;
                if (starsTransactionPeer instanceof TL_stars.TL_starsTransactionPeer) {
                    final long peerDialogId42 = DialogObject.getPeerDialogId(starsTransactionPeer.peer);
                    if (z) {
                    }
                    chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId42));
                    if (chat2 != null) {
                    }
                }
                viewGroup5 = viewGroup4;
                if (TextUtils.isEmpty(starsTransaction2.id)) {
                }
                resourcesProvider3 = resourcesProvider;
                if (starsTransaction2.floodskip) {
                }
                CharSequence string6222 = LocaleController.getString(R.string.StarsTransactionDate);
                int i7222 = R.string.formatDateAtTime;
                tableView.addRow(string6222, LocaleController.formatString(i7222, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(starsTransaction2.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(starsTransaction2.date * 1000))));
                starGift = starsTransaction2.stargift;
                if (starGift != null) {
                }
                ViewGroup viewGroup7222 = viewGroup5;
                viewGroup7222.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 17.0f, 0.0f, 0.0f));
                if ((starsTransaction2.flags & 32) != 0) {
                }
                LinkSpanDrawable.LinksTextView linksTextView3222 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider3);
                linksTextView3222.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider3));
                linksTextView3222.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider3));
                linksTextView3222.setTextSize(1, 14.0f);
                linksTextView3222.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.StarsTransactionTOS), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarsIntroActivity.lambda$showTransactionSheet$37(context);
                    }
                }));
                linksTextView3222.setGravity(17);
                viewGroup7222.addView(linksTextView3222, LayoutHelper.createLinear(-1, -2, 0.0f, 15.0f, 0.0f, 15.0f));
                ButtonWithCounterView buttonWithCounterView222 = new ButtonWithCounterView(context, resourcesProvider3);
                if ((starsTransaction2.flags & 32) == 0) {
                }
                buttonWithCounterView222.setText(string4, r4);
                viewGroup7222.addView(buttonWithCounterView222, LayoutHelper.createLinear(-1, 48));
                BottomSheet.Builder builder3222 = builder;
                builder3222.setCustomView(viewGroup7222);
                BottomSheet create222 = builder3222.create();
                bottomSheetArr3[r4] = create222;
                create222.useBackgroundTopPadding = r4;
                if ((starsTransaction2.flags & 32) == 0) {
                }
                bottomSheetArr3[0].fixNavigationBar();
                safeLastFragment = LaunchActivity.getSafeLastFragment();
                if (!AndroidUtilities.isTablet()) {
                }
                bottomSheetArr3[0].show();
                return bottomSheetArr3[0];
            }
            textView2.setTextColor(Theme.getColor(Theme.key_color_red, resourcesProvider));
            i2 = R.string.StarsFailed;
        }
        appendStatus(spannableStringBuilder, textView2, LocaleController.getString(i2));
        textView2.setText(spannableStringBuilder);
        viewGroup2.addView(textView2, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        if (z2) {
        }
        if (starsTransaction.sent_by != null) {
        }
        if (starsTransaction.sent_by != null) {
        }
        isUserSelf = UserObject.isUserSelf(user2);
        if (isUserSelf) {
        }
        LinkSpanDrawable.LinksTextView linksTextView5 = new LinkSpanDrawable.LinksTextView(context);
        linksTextView5.setTextColor(Theme.getColor(i5, resourcesProvider));
        linksTextView5.setTextSize(1, 16.0f);
        linksTextView5.setGravity(17);
        linksTextView5.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
        linksTextView5.setDisablePaddingsOffsetY(true);
        if (isUserSelf) {
        }
        SpannableStringBuilder replaceTags2 = AndroidUtilities.replaceTags(string);
        bottomSheetArr2 = bottomSheetArr;
        CharSequence replaceArrows2 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.GiftStarsSubtitleLinkName).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                StarsIntroActivity.lambda$showTransactionSheet$22(context, bottomSheetArr2);
            }
        }), true);
        CharSequence[] charSequenceArr2 = new CharSequence[3];
        charSequenceArr2[c] = replaceTags2;
        charSequenceArr2[1] = " ";
        charSequenceArr2[2] = replaceArrows2;
        linksTextView5.setText(TextUtils.concat(charSequenceArr2));
        viewGroup4 = viewGroup3;
        viewGroup4.addView(linksTextView5, LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 4));
        TableView tableView32 = new TableView(context, resourcesProvider);
        if (starsTransaction.stargift != null) {
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
        UniversalAdapter universalAdapter = new UniversalAdapter(this.listView, getContext(), this.currentAccount, this.classGuid, true, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda45
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                StarsIntroActivity.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, getResourceProvider()) { // from class: org.telegram.ui.Stars.StarsIntroActivity.3
            @Override // org.telegram.ui.Components.UniversalAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                if (i != 42) {
                    return super.onCreateViewHolder(viewGroup, i);
                }
                HeaderCell headerCell = new HeaderCell(StarsIntroActivity.this.getContext(), Theme.key_windowBackgroundWhiteBlueHeader, 21, 0, false, ((BaseFragment) StarsIntroActivity.this).resourceProvider);
                headerCell.setHeight(25);
                return new RecyclerListView.Holder(headerCell);
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
        configureHeader(LocaleController.getString(R.string.TelegramStars), AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.TelegramStarsInfo2), new Runnable() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda29
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
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda30
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
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda31
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
        this.giftButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda32
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

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0042, code lost:
    
        r1.savedScrollOffset = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0040, code lost:
    
        if (r1.savedScrollOffset < 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
    
        if (r1.savedScrollOffset < 0) goto L23;
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
        }
        if (i != NotificationCenter.starTransactionsLoaded) {
            if (i != NotificationCenter.starSubscriptionsLoaded) {
                if (i == NotificationCenter.starBalanceUpdated) {
                    updateBalance();
                    return;
                }
                return;
            } else {
                UniversalAdapter universalAdapter2 = this.adapter;
                if (universalAdapter2 != null) {
                    universalAdapter2.update(true);
                    return;
                }
                return;
            }
        }
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
        } else {
            if (i2 == -2) {
                StarsController.getInstance(this.currentAccount).getGiftOptions();
                UserSelectorBottomSheet.open(1, 0L, BirthdayController.getInstance(this.currentAccount).getState());
                return;
            }
            if (i2 != -3) {
                if (uItem.instanceOf(StarTierView.Factory.class)) {
                    if (uItem.object instanceof TL_stars.TL_starsTopupOption) {
                        StarsController.getInstance(this.currentAccount).buy(getParentActivity(), (TL_stars.TL_starsTopupOption) uItem.object, new Utilities.Callback2() { // from class: org.telegram.ui.Stars.StarsIntroActivity$$ExternalSyntheticLambda56
                            @Override // org.telegram.messenger.Utilities.Callback2
                            public final void run(Object obj, Object obj2) {
                                StarsIntroActivity.this.lambda$onItemClick$4(uItem, (Boolean) obj, (String) obj2);
                            }
                        });
                        return;
                    }
                    return;
                } else {
                    if (uItem.instanceOf(StarsSubscriptionView.Factory.class) && (uItem.object instanceof TL_stars.StarsSubscription)) {
                        showSubscriptionSheet(getContext(), this.currentAccount, (TL_stars.StarsSubscription) uItem.object, getResourceProvider());
                        return;
                    }
                    return;
                }
            }
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
