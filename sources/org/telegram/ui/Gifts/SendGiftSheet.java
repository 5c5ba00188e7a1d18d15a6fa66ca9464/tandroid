package org.telegram.ui.Gifts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.EditEmojiTextCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextSuggestionsFix;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.GiftPremiumBottomSheet$GiftTier;
import org.telegram.ui.Components.Premium.boosts.BoostDialogs;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.Premium.boosts.PremiumPreviewGiftSentBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.PreviewView;

/* loaded from: classes3.dex */
public class SendGiftSheet extends BottomSheetWithRecyclerListView {
    private final TLRPC.MessageAction action;
    private final ChatActionCell actionCell;
    private UniversalAdapter adapter;
    public final AnimationNotificationsLocker animationsLock;
    public boolean anonymous;
    private final ButtonWithCounterView button;
    private final LinearLayout buttonContainer;
    private final SizeNotifierFrameLayout chatView;
    private final Runnable closeParentSheet;
    private final int currentAccount;
    private final long dialogId;
    private final TextView leftTextView;
    private final TextView leftTextView2;
    private final FrameLayout limitContainer;
    private final View limitProgressView;
    private EditEmojiTextCell messageEdit;
    private final MessageObject messageObject;
    private final String name;
    private final GiftPremiumBottomSheet$GiftTier premiumTier;
    private final TextView soldTextView;
    private final TextView soldTextView2;
    private final TL_stars.StarGift starGift;
    private final FrameLayout valueContainerView;

    public SendGiftSheet(Context context, int i, TL_stars.StarGift starGift, long j, Runnable runnable) {
        this(context, i, starGift, null, j, runnable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x03be  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0424  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0435  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x019f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private SendGiftSheet(Context context, final int i, final TL_stars.StarGift starGift, GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier, long j, Runnable runnable) {
        super(context, null, true, false, false, false, BottomSheetWithRecyclerListView.ActionBarType.SLIDING, null);
        CharSequence formatString;
        TLRPC.TL_messageActionGiftPremium tL_messageActionGiftPremium;
        this.animationsLock = new AnimationNotificationsLocker();
        setImageReceiverNumLevel(0, 4);
        fixNavigationBar();
        this.headerPaddingTop = AndroidUtilities.dp(4.0f);
        this.headerPaddingBottom = AndroidUtilities.dp(-10.0f);
        this.currentAccount = i;
        this.dialogId = j;
        this.starGift = starGift;
        this.premiumTier = giftPremiumBottomSheet$GiftTier;
        this.closeParentSheet = runnable;
        this.topPadding = 0.2f;
        this.name = UserObject.getForcedFirstName(MessagesController.getInstance(i).getUser(Long.valueOf(j)));
        ChatActionCell chatActionCell = new ChatActionCell(context, false, this.resourcesProvider);
        this.actionCell = chatActionCell;
        chatActionCell.setDelegate(new ChatActionCell.ChatActionCellDelegate() { // from class: org.telegram.ui.Gifts.SendGiftSheet.1
            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ boolean canDrawOutboundsContent() {
                return ChatActionCell.ChatActionCellDelegate.-CC.$default$canDrawOutboundsContent(this);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didClickButton(ChatActionCell chatActionCell2) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$didClickButton(this, chatActionCell2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didClickImage(ChatActionCell chatActionCell2) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$didClickImage(this, chatActionCell2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ boolean didLongPress(ChatActionCell chatActionCell2, float f, float f2) {
                return ChatActionCell.ChatActionCellDelegate.-CC.$default$didLongPress(this, chatActionCell2, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didOpenPremiumGift(ChatActionCell chatActionCell2, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$didOpenPremiumGift(this, chatActionCell2, tL_premiumGiftOption, str, z);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didOpenPremiumGiftChannel(ChatActionCell chatActionCell2, String str, boolean z) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$didOpenPremiumGiftChannel(this, chatActionCell2, str, z);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didPressReplyMessage(ChatActionCell chatActionCell2, int i2) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$didPressReplyMessage(this, chatActionCell2, i2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void forceUpdate(ChatActionCell chatActionCell2, boolean z) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$forceUpdate(this, chatActionCell2, z);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ BaseFragment getBaseFragment() {
                return ChatActionCell.ChatActionCellDelegate.-CC.$default$getBaseFragment(this);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ long getDialogId() {
                return ChatActionCell.ChatActionCellDelegate.-CC.$default$getDialogId(this);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ long getTopicId() {
                return ChatActionCell.ChatActionCellDelegate.-CC.$default$getTopicId(this);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenInviteLink(this, tL_chatInviteExported);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needOpenUserProfile(long j2) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenUserProfile(this, j2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needShowEffectOverlay(ChatActionCell chatActionCell2, TLRPC.Document document, TLRPC.VideoSize videoSize) {
                ChatActionCell.ChatActionCellDelegate.-CC.$default$needShowEffectOverlay(this, chatActionCell2, document, videoSize);
            }
        });
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.Gifts.SendGiftSheet.2
            int maxHeight = -1;

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected boolean isActionBarVisible() {
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            public boolean isStatusBarVisible() {
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                SendGiftSheet.this.actionCell.setTranslationY((((i5 - i3) - SendGiftSheet.this.actionCell.getMeasuredHeight()) / 2.0f) - AndroidUtilities.dp(8.0f));
                SendGiftSheet.this.actionCell.setVisiblePart(SendGiftSheet.this.actionCell.getY(), getBackgroundSizeY());
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                if (this.maxHeight != -1) {
                    super.onMeasure(i2, i3);
                    int measuredHeight = getMeasuredHeight();
                    int i4 = this.maxHeight;
                    if (measuredHeight < i4) {
                        i3 = View.MeasureSpec.makeMeasureSpec(Math.max(i4, getMeasuredHeight()), Integer.MIN_VALUE);
                    }
                }
                super.onMeasure(i2, i3);
                int i5 = this.maxHeight;
                if (i5 == -1) {
                    this.maxHeight = Math.max(i5, getMeasuredHeight());
                }
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            protected boolean useRootView() {
                return false;
            }
        };
        this.chatView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setBackgroundImage(PreviewView.getBackgroundDrawable((Drawable) null, i, j, Theme.isCurrentThemeDark()), false);
        if (starGift != null) {
            TLRPC.TL_messageActionStarGift tL_messageActionStarGift = new TLRPC.TL_messageActionStarGift();
            tL_messageActionStarGift.gift = starGift;
            tL_messageActionStarGift.flags |= 2;
            tL_messageActionStarGift.message = new TLRPC.TL_textWithEntities();
            tL_messageActionStarGift.convert_stars = starGift.convert_stars;
            tL_messageActionStarGift.forceIn = true;
            tL_messageActionGiftPremium = tL_messageActionStarGift;
        } else {
            if (giftPremiumBottomSheet$GiftTier != null && giftPremiumBottomSheet$GiftTier.giftCodeOption != null) {
                TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = new TLRPC.TL_messageActionGiftCode();
                tL_messageActionGiftCode.unclaimed = true;
                tL_messageActionGiftCode.via_giveaway = false;
                tL_messageActionGiftCode.months = giftPremiumBottomSheet$GiftTier.getMonths();
                tL_messageActionGiftCode.flags = 4 | tL_messageActionGiftCode.flags;
                tL_messageActionGiftCode.currency = giftPremiumBottomSheet$GiftTier.getCurrency();
                long price = giftPremiumBottomSheet$GiftTier.getPrice();
                tL_messageActionGiftCode.amount = price;
                if (giftPremiumBottomSheet$GiftTier.googlePlayProductDetails != null) {
                    double d = price;
                    double pow = Math.pow(10.0d, BillingController.getInstance().getCurrencyExp(tL_messageActionGiftCode.currency) - 6);
                    Double.isNaN(d);
                    tL_messageActionGiftCode.amount = (long) (d * pow);
                }
                tL_messageActionGiftCode.flags |= 16;
                tL_messageActionGiftCode.message = new TLRPC.TL_textWithEntities();
                this.action = tL_messageActionGiftCode;
                TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
                tL_messageService.id = 1;
                tL_messageService.dialog_id = j;
                tL_messageService.from_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
                tL_messageService.peer_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
                tL_messageService.action = this.action;
                MessageObject messageObject = new MessageObject(i, tL_messageService, false, false);
                this.messageObject = messageObject;
                chatActionCell.setMessageObject(messageObject, true);
                sizeNotifierFrameLayout.addView(chatActionCell, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 8.0f, 0.0f, 8.0f));
                EditEmojiTextCell editEmojiTextCell = new EditEmojiTextCell(context, (SizeNotifierFrameLayout) this.containerView, LocaleController.getString(starGift == null ? R.string.Gift2Message : R.string.Gift2MessageOptional), true, MessagesController.getInstance(i).stargiftsMessageLengthMax, 4, this.resourcesProvider) { // from class: org.telegram.ui.Gifts.SendGiftSheet.3
                    @Override // org.telegram.ui.Cells.EditEmojiTextCell
                    protected void onFocusChanged(boolean z) {
                    }

                    @Override // org.telegram.ui.Cells.EditEmojiTextCell
                    protected void onTextChanged(CharSequence charSequence) {
                        TLRPC.TL_textWithEntities tL_textWithEntities;
                        if (SendGiftSheet.this.action instanceof TLRPC.TL_messageActionStarGift) {
                            TLRPC.TL_messageActionStarGift tL_messageActionStarGift2 = (TLRPC.TL_messageActionStarGift) SendGiftSheet.this.action;
                            tL_textWithEntities = new TLRPC.TL_textWithEntities();
                            tL_messageActionStarGift2.message = tL_textWithEntities;
                        } else if (SendGiftSheet.this.action instanceof TLRPC.TL_messageActionGiftCode) {
                            ((TLRPC.TL_messageActionGiftCode) SendGiftSheet.this.action).flags |= 16;
                            TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode2 = (TLRPC.TL_messageActionGiftCode) SendGiftSheet.this.action;
                            tL_textWithEntities = new TLRPC.TL_textWithEntities();
                            tL_messageActionGiftCode2.message = tL_textWithEntities;
                        } else {
                            if (!(SendGiftSheet.this.action instanceof TLRPC.TL_messageActionGiftPremium)) {
                                return;
                            }
                            ((TLRPC.TL_messageActionGiftPremium) SendGiftSheet.this.action).flags |= 16;
                            TLRPC.TL_messageActionGiftPremium tL_messageActionGiftPremium2 = (TLRPC.TL_messageActionGiftPremium) SendGiftSheet.this.action;
                            tL_textWithEntities = new TLRPC.TL_textWithEntities();
                            tL_messageActionGiftPremium2.message = tL_textWithEntities;
                        }
                        CharSequence[] charSequenceArr = {SendGiftSheet.this.messageEdit.getText()};
                        tL_textWithEntities.entities = MediaDataController.getInstance(i).getEntities(charSequenceArr, true);
                        tL_textWithEntities.text = charSequenceArr[0].toString();
                        SendGiftSheet.this.messageObject.setType();
                        SendGiftSheet.this.actionCell.setMessageObject(SendGiftSheet.this.messageObject, true);
                        SendGiftSheet.this.adapter.update(true);
                    }
                };
                this.messageEdit = editEmojiTextCell;
                editEmojiTextCell.editTextEmoji.getEditText().addTextChangedListener(new EditTextSuggestionsFix());
                this.messageEdit.editTextEmoji.allowEmojisForNonPremium(true);
                this.messageEdit.setShowLimitWhenNear(50);
                setEditTextEmoji(this.messageEdit.editTextEmoji);
                this.messageEdit.setShowLimitOnFocus(true);
                EditEmojiTextCell editEmojiTextCell2 = this.messageEdit;
                int i2 = Theme.key_dialogBackground;
                editEmojiTextCell2.setBackgroundColor(Theme.getColor(i2, this.resourcesProvider));
                this.messageEdit.setDivider(false);
                this.messageEdit.hideKeyboardOnEnter();
                EditEmojiTextCell editEmojiTextCell3 = this.messageEdit;
                int i3 = this.backgroundPaddingLeft;
                editEmojiTextCell3.setPadding(i3, 0, i3, 0);
                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.Gifts.SendGiftSheet.4
                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                    protected float animateByScale(View view) {
                        return 0.3f;
                    }
                };
                defaultItemAnimator.setDelayAnimations(false);
                defaultItemAnimator.setSupportsChangeAnimations(false);
                defaultItemAnimator.setDurations(350L);
                defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                defaultItemAnimator.setDelayIncrement(40L);
                this.recyclerListView.setItemAnimator(defaultItemAnimator);
                RecyclerListView recyclerListView = this.recyclerListView;
                int i4 = this.backgroundPaddingLeft;
                recyclerListView.setPadding(i4, 0, i4, AndroidUtilities.dp(68 + ((starGift == null && starGift.limited) ? 40 : 0)));
                this.adapter.update(false);
                this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i5) {
                        SendGiftSheet.this.lambda$new$0(view, i5);
                    }
                });
                LinearLayout linearLayout = new LinearLayout(context);
                this.buttonContainer = linearLayout;
                linearLayout.setOrientation(1);
                linearLayout.setBackgroundColor(Theme.getColor(i2, this.resourcesProvider));
                int i5 = this.backgroundPaddingLeft;
                linearLayout.setPadding(i5, 0, i5, 0);
                this.containerView.addView(linearLayout, LayoutHelper.createFrame(-1, -2, 87));
                View view = new View(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_dialogGrayLine, this.resourcesProvider));
                linearLayout.addView(view, LayoutHelper.createLinear(-1.0f, 1.0f / AndroidUtilities.density, 55));
                final float clamp = Utilities.clamp(starGift != null ? 0.0f : starGift.availability_remains / starGift.availability_total, 0.97f, 0.0f);
                FrameLayout frameLayout = new FrameLayout(context);
                this.limitContainer = frameLayout;
                frameLayout.setVisibility((starGift == null && starGift.limited) ? 0 : 8);
                frameLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider)));
                linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 30, 10.0f, 10.0f, 10.0f, 0.0f));
                TextView textView = new TextView(context);
                this.leftTextView = textView;
                textView.setTextSize(1, 13.0f);
                textView.setGravity(19);
                textView.setTypeface(AndroidUtilities.bold());
                int i6 = Theme.key_windowBackgroundWhiteBlackText;
                textView.setTextColor(Theme.getColor(i6, this.resourcesProvider));
                if (starGift != null) {
                    textView.setText(LocaleController.formatPluralStringComma("Gift2AvailabilityLeft", starGift.availability_remains));
                }
                frameLayout.addView(textView, LayoutHelper.createFrame(-1, -1.0f, 3, 11.0f, 0.0f, 11.0f, 0.0f));
                TextView textView2 = new TextView(context);
                this.soldTextView = textView2;
                textView2.setTextSize(1, 13.0f);
                textView2.setGravity(21);
                textView2.setTypeface(AndroidUtilities.bold());
                textView2.setTextColor(Theme.getColor(i6, this.resourcesProvider));
                if (starGift != null) {
                    textView2.setText(LocaleController.formatPluralStringComma("Gift2AvailabilitySold", starGift.availability_total - starGift.availability_remains));
                }
                frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -1.0f, 5, 11.0f, 0.0f, 11.0f, 0.0f));
                View view2 = new View(context) { // from class: org.telegram.ui.Gifts.SendGiftSheet.5
                    @Override // android.view.View
                    protected void onMeasure(int i7, int i8) {
                        if (starGift == null) {
                            super.onMeasure(i7, i8);
                        } else {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) (View.MeasureSpec.getSize(i7) * clamp), 1073741824), i8);
                        }
                    }
                };
                this.limitProgressView = view2;
                view2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider)));
                frameLayout.addView(view2, LayoutHelper.createFrame(-1, -1, 119));
                FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Gifts.SendGiftSheet.6
                    @Override // android.view.ViewGroup, android.view.View
                    protected void dispatchDraw(Canvas canvas) {
                        canvas.save();
                        canvas.clipRect(0.0f, 0.0f, getWidth() * clamp, getHeight());
                        super.dispatchDraw(canvas);
                        canvas.restore();
                    }
                };
                this.valueContainerView = frameLayout2;
                frameLayout2.setWillNotDraw(false);
                frameLayout.addView(frameLayout2, LayoutHelper.createFrame(-1, -1, 119));
                TextView textView3 = new TextView(context);
                this.leftTextView2 = textView3;
                textView3.setTextSize(1, 13.0f);
                textView3.setGravity(19);
                textView3.setTypeface(AndroidUtilities.bold());
                textView3.setTextColor(-1);
                if (starGift != null) {
                    textView3.setText(LocaleController.formatPluralStringComma("Gift2AvailabilityLeft", starGift.availability_remains));
                }
                frameLayout2.addView(textView3, LayoutHelper.createFrame(-1, -1.0f, 3, 11.0f, 0.0f, 11.0f, 0.0f));
                TextView textView4 = new TextView(context);
                this.soldTextView2 = textView4;
                textView4.setTextSize(1, 13.0f);
                textView4.setGravity(21);
                textView4.setTypeface(AndroidUtilities.bold());
                textView4.setTextColor(-1);
                if (starGift != null) {
                    textView4.setText(LocaleController.formatPluralStringComma("Gift2AvailabilitySold", starGift.availability_total - starGift.availability_remains));
                }
                frameLayout2.addView(textView4, LayoutHelper.createFrame(-1, -1.0f, 5, 11.0f, 0.0f, 11.0f, 0.0f));
                ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, this.resourcesProvider);
                this.button = buttonWithCounterView;
                if (starGift != null) {
                    formatString = giftPremiumBottomSheet$GiftTier != null ? LocaleController.formatString(R.string.Gift2SendPremium, giftPremiumBottomSheet$GiftTier.getFormattedPrice()) : formatString;
                    linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 119, 10, 10, 10, 10));
                    buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view3) {
                            SendGiftSheet.this.lambda$new$1(starGift, view3);
                        }
                    });
                    LinearLayoutManager linearLayoutManager = this.layoutManager;
                    this.reverseLayout = true;
                    linearLayoutManager.setReverseLayout(true);
                    this.adapter.update(false);
                    this.layoutManager.scrollToPositionWithOffset(this.adapter.getItemCount(), AndroidUtilities.dp(200.0f));
                }
                formatString = StarsIntroActivity.replaceStars(LocaleController.formatPluralStringComma("Gift2Send", (int) starGift.stars));
                buttonWithCounterView.setText(formatString, false);
                linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 119, 10, 10, 10, 10));
                buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        SendGiftSheet.this.lambda$new$1(starGift, view3);
                    }
                });
                LinearLayoutManager linearLayoutManager2 = this.layoutManager;
                this.reverseLayout = true;
                linearLayoutManager2.setReverseLayout(true);
                this.adapter.update(false);
                this.layoutManager.scrollToPositionWithOffset(this.adapter.getItemCount(), AndroidUtilities.dp(200.0f));
            }
            if (giftPremiumBottomSheet$GiftTier == null || giftPremiumBottomSheet$GiftTier.giftOption == null) {
                throw new RuntimeException("SendGiftSheet with no star gift and no premium tier");
            }
            TLRPC.TL_messageActionGiftPremium tL_messageActionGiftPremium2 = new TLRPC.TL_messageActionGiftPremium();
            tL_messageActionGiftPremium2.months = giftPremiumBottomSheet$GiftTier.getMonths();
            tL_messageActionGiftPremium2.currency = giftPremiumBottomSheet$GiftTier.getCurrency();
            long price2 = giftPremiumBottomSheet$GiftTier.getPrice();
            tL_messageActionGiftPremium2.amount = price2;
            if (giftPremiumBottomSheet$GiftTier.googlePlayProductDetails != null) {
                double d2 = price2;
                double pow2 = Math.pow(10.0d, BillingController.getInstance().getCurrencyExp(tL_messageActionGiftPremium2.currency) - 6);
                Double.isNaN(d2);
                tL_messageActionGiftPremium2.amount = (long) (d2 * pow2);
            }
            tL_messageActionGiftPremium2.flags |= 2;
            tL_messageActionGiftPremium2.message = new TLRPC.TL_textWithEntities();
            tL_messageActionGiftPremium = tL_messageActionGiftPremium2;
        }
        this.action = tL_messageActionGiftPremium;
        TLRPC.TL_messageService tL_messageService2 = new TLRPC.TL_messageService();
        tL_messageService2.id = 1;
        tL_messageService2.dialog_id = j;
        tL_messageService2.from_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
        tL_messageService2.peer_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
        tL_messageService2.action = this.action;
        MessageObject messageObject2 = new MessageObject(i, tL_messageService2, false, false);
        this.messageObject = messageObject2;
        chatActionCell.setMessageObject(messageObject2, true);
        sizeNotifierFrameLayout.addView(chatActionCell, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 8.0f, 0.0f, 8.0f));
        EditEmojiTextCell editEmojiTextCell4 = new EditEmojiTextCell(context, (SizeNotifierFrameLayout) this.containerView, LocaleController.getString(starGift == null ? R.string.Gift2Message : R.string.Gift2MessageOptional), true, MessagesController.getInstance(i).stargiftsMessageLengthMax, 4, this.resourcesProvider) { // from class: org.telegram.ui.Gifts.SendGiftSheet.3
            @Override // org.telegram.ui.Cells.EditEmojiTextCell
            protected void onFocusChanged(boolean z) {
            }

            @Override // org.telegram.ui.Cells.EditEmojiTextCell
            protected void onTextChanged(CharSequence charSequence) {
                TLRPC.TL_textWithEntities tL_textWithEntities;
                if (SendGiftSheet.this.action instanceof TLRPC.TL_messageActionStarGift) {
                    TLRPC.TL_messageActionStarGift tL_messageActionStarGift2 = (TLRPC.TL_messageActionStarGift) SendGiftSheet.this.action;
                    tL_textWithEntities = new TLRPC.TL_textWithEntities();
                    tL_messageActionStarGift2.message = tL_textWithEntities;
                } else if (SendGiftSheet.this.action instanceof TLRPC.TL_messageActionGiftCode) {
                    ((TLRPC.TL_messageActionGiftCode) SendGiftSheet.this.action).flags |= 16;
                    TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode2 = (TLRPC.TL_messageActionGiftCode) SendGiftSheet.this.action;
                    tL_textWithEntities = new TLRPC.TL_textWithEntities();
                    tL_messageActionGiftCode2.message = tL_textWithEntities;
                } else {
                    if (!(SendGiftSheet.this.action instanceof TLRPC.TL_messageActionGiftPremium)) {
                        return;
                    }
                    ((TLRPC.TL_messageActionGiftPremium) SendGiftSheet.this.action).flags |= 16;
                    TLRPC.TL_messageActionGiftPremium tL_messageActionGiftPremium22 = (TLRPC.TL_messageActionGiftPremium) SendGiftSheet.this.action;
                    tL_textWithEntities = new TLRPC.TL_textWithEntities();
                    tL_messageActionGiftPremium22.message = tL_textWithEntities;
                }
                CharSequence[] charSequenceArr = {SendGiftSheet.this.messageEdit.getText()};
                tL_textWithEntities.entities = MediaDataController.getInstance(i).getEntities(charSequenceArr, true);
                tL_textWithEntities.text = charSequenceArr[0].toString();
                SendGiftSheet.this.messageObject.setType();
                SendGiftSheet.this.actionCell.setMessageObject(SendGiftSheet.this.messageObject, true);
                SendGiftSheet.this.adapter.update(true);
            }
        };
        this.messageEdit = editEmojiTextCell4;
        editEmojiTextCell4.editTextEmoji.getEditText().addTextChangedListener(new EditTextSuggestionsFix());
        this.messageEdit.editTextEmoji.allowEmojisForNonPremium(true);
        this.messageEdit.setShowLimitWhenNear(50);
        setEditTextEmoji(this.messageEdit.editTextEmoji);
        this.messageEdit.setShowLimitOnFocus(true);
        EditEmojiTextCell editEmojiTextCell22 = this.messageEdit;
        int i22 = Theme.key_dialogBackground;
        editEmojiTextCell22.setBackgroundColor(Theme.getColor(i22, this.resourcesProvider));
        this.messageEdit.setDivider(false);
        this.messageEdit.hideKeyboardOnEnter();
        EditEmojiTextCell editEmojiTextCell32 = this.messageEdit;
        int i32 = this.backgroundPaddingLeft;
        editEmojiTextCell32.setPadding(i32, 0, i32, 0);
        DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.Gifts.SendGiftSheet.4
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            protected float animateByScale(View view3) {
                return 0.3f;
            }
        };
        defaultItemAnimator2.setDelayAnimations(false);
        defaultItemAnimator2.setSupportsChangeAnimations(false);
        defaultItemAnimator2.setDurations(350L);
        defaultItemAnimator2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator2.setDelayIncrement(40L);
        this.recyclerListView.setItemAnimator(defaultItemAnimator2);
        RecyclerListView recyclerListView2 = this.recyclerListView;
        int i42 = this.backgroundPaddingLeft;
        recyclerListView2.setPadding(i42, 0, i42, AndroidUtilities.dp(68 + ((starGift == null && starGift.limited) ? 40 : 0)));
        this.adapter.update(false);
        this.recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view3, int i52) {
                SendGiftSheet.this.lambda$new$0(view3, i52);
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.buttonContainer = linearLayout2;
        linearLayout2.setOrientation(1);
        linearLayout2.setBackgroundColor(Theme.getColor(i22, this.resourcesProvider));
        int i52 = this.backgroundPaddingLeft;
        linearLayout2.setPadding(i52, 0, i52, 0);
        this.containerView.addView(linearLayout2, LayoutHelper.createFrame(-1, -2, 87));
        View view3 = new View(context);
        view3.setBackgroundColor(Theme.getColor(Theme.key_dialogGrayLine, this.resourcesProvider));
        linearLayout2.addView(view3, LayoutHelper.createLinear(-1.0f, 1.0f / AndroidUtilities.density, 55));
        final float clamp2 = Utilities.clamp(starGift != null ? 0.0f : starGift.availability_remains / starGift.availability_total, 0.97f, 0.0f);
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.limitContainer = frameLayout3;
        frameLayout3.setVisibility((starGift == null && starGift.limited) ? 0 : 8);
        frameLayout3.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider)));
        linearLayout2.addView(frameLayout3, LayoutHelper.createLinear(-1, 30, 10.0f, 10.0f, 10.0f, 0.0f));
        TextView textView5 = new TextView(context);
        this.leftTextView = textView5;
        textView5.setTextSize(1, 13.0f);
        textView5.setGravity(19);
        textView5.setTypeface(AndroidUtilities.bold());
        int i62 = Theme.key_windowBackgroundWhiteBlackText;
        textView5.setTextColor(Theme.getColor(i62, this.resourcesProvider));
        if (starGift != null) {
        }
        frameLayout3.addView(textView5, LayoutHelper.createFrame(-1, -1.0f, 3, 11.0f, 0.0f, 11.0f, 0.0f));
        TextView textView22 = new TextView(context);
        this.soldTextView = textView22;
        textView22.setTextSize(1, 13.0f);
        textView22.setGravity(21);
        textView22.setTypeface(AndroidUtilities.bold());
        textView22.setTextColor(Theme.getColor(i62, this.resourcesProvider));
        if (starGift != null) {
        }
        frameLayout3.addView(textView22, LayoutHelper.createFrame(-1, -1.0f, 5, 11.0f, 0.0f, 11.0f, 0.0f));
        View view22 = new View(context) { // from class: org.telegram.ui.Gifts.SendGiftSheet.5
            @Override // android.view.View
            protected void onMeasure(int i7, int i8) {
                if (starGift == null) {
                    super.onMeasure(i7, i8);
                } else {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec((int) (View.MeasureSpec.getSize(i7) * clamp2), 1073741824), i8);
                }
            }
        };
        this.limitProgressView = view22;
        view22.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider)));
        frameLayout3.addView(view22, LayoutHelper.createFrame(-1, -1, 119));
        FrameLayout frameLayout22 = new FrameLayout(context) { // from class: org.telegram.ui.Gifts.SendGiftSheet.6
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0.0f, 0.0f, getWidth() * clamp2, getHeight());
                super.dispatchDraw(canvas);
                canvas.restore();
            }
        };
        this.valueContainerView = frameLayout22;
        frameLayout22.setWillNotDraw(false);
        frameLayout3.addView(frameLayout22, LayoutHelper.createFrame(-1, -1, 119));
        TextView textView32 = new TextView(context);
        this.leftTextView2 = textView32;
        textView32.setTextSize(1, 13.0f);
        textView32.setGravity(19);
        textView32.setTypeface(AndroidUtilities.bold());
        textView32.setTextColor(-1);
        if (starGift != null) {
        }
        frameLayout22.addView(textView32, LayoutHelper.createFrame(-1, -1.0f, 3, 11.0f, 0.0f, 11.0f, 0.0f));
        TextView textView42 = new TextView(context);
        this.soldTextView2 = textView42;
        textView42.setTextSize(1, 13.0f);
        textView42.setGravity(21);
        textView42.setTypeface(AndroidUtilities.bold());
        textView42.setTextColor(-1);
        if (starGift != null) {
        }
        frameLayout22.addView(textView42, LayoutHelper.createFrame(-1, -1.0f, 5, 11.0f, 0.0f, 11.0f, 0.0f));
        ButtonWithCounterView buttonWithCounterView2 = new ButtonWithCounterView(context, this.resourcesProvider);
        this.button = buttonWithCounterView2;
        if (starGift != null) {
        }
        buttonWithCounterView2.setText(formatString, false);
        linearLayout2.addView(buttonWithCounterView2, LayoutHelper.createLinear(-1, 48, 119, 10, 10, 10, 10));
        buttonWithCounterView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view32) {
                SendGiftSheet.this.lambda$new$1(starGift, view32);
            }
        });
        LinearLayoutManager linearLayoutManager22 = this.layoutManager;
        this.reverseLayout = true;
        linearLayoutManager22.setReverseLayout(true);
        this.adapter.update(false);
        this.layoutManager.scrollToPositionWithOffset(this.adapter.getItemCount(), AndroidUtilities.dp(200.0f));
    }

    public SendGiftSheet(Context context, int i, GiftPremiumBottomSheet$GiftTier giftPremiumBottomSheet$GiftTier, long j, Runnable runnable) {
        this(context, i, null, giftPremiumBottomSheet$GiftTier, j, runnable);
    }

    private void buyPremiumTier() {
        final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
        if (user == null) {
            this.button.setLoading(false);
            return;
        }
        if (this.premiumTier.giftCodeOption != null) {
            BoostRepository.payGiftCode(new ArrayList(Arrays.asList(user)), this.premiumTier.giftCodeOption, null, getMessage(), new BaseFragment() { // from class: org.telegram.ui.Gifts.SendGiftSheet.7
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public Activity getParentActivity() {
                    Activity ownerActivity = SendGiftSheet.this.getOwnerActivity();
                    if (ownerActivity == null) {
                        ownerActivity = LaunchActivity.instance;
                    }
                    return ownerActivity == null ? AndroidUtilities.findActivity(SendGiftSheet.this.getContext()) : ownerActivity;
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public Theme.ResourcesProvider getResourceProvider() {
                    return ((BottomSheet) SendGiftSheet.this).resourcesProvider;
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda3
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SendGiftSheet.this.lambda$buyPremiumTier$4(user, (Void) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    SendGiftSheet.this.lambda$buyPremiumTier$5((TLRPC.TL_error) obj);
                }
            });
            return;
        }
        if (BuildVars.useInvoiceBilling()) {
            LaunchActivity launchActivity = LaunchActivity.instance;
            if (launchActivity != null) {
                Uri parse = Uri.parse(this.premiumTier.giftOption.bot_url);
                if (parse.getHost().equals("t.me")) {
                    if (parse.getPath().startsWith("/$") || parse.getPath().startsWith("/invoice/")) {
                        launchActivity.setNavigateToPremiumGiftCallback(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendGiftSheet.this.lambda$buyPremiumTier$6();
                            }
                        });
                    } else {
                        launchActivity.setNavigateToPremiumBot(true);
                    }
                }
                Browser.openUrl(launchActivity, this.premiumTier.giftOption.bot_url);
                dismiss();
                return;
            }
            return;
        }
        if (!BillingController.getInstance().isReady() || this.premiumTier.googlePlayProductDetails == null) {
            return;
        }
        final TLRPC.TL_inputStorePaymentGiftPremium tL_inputStorePaymentGiftPremium = new TLRPC.TL_inputStorePaymentGiftPremium();
        tL_inputStorePaymentGiftPremium.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(user);
        ProductDetails.OneTimePurchaseOfferDetails oneTimePurchaseOfferDetails = this.premiumTier.googlePlayProductDetails.getOneTimePurchaseOfferDetails();
        tL_inputStorePaymentGiftPremium.currency = oneTimePurchaseOfferDetails.getPriceCurrencyCode();
        double priceAmountMicros = oneTimePurchaseOfferDetails.getPriceAmountMicros();
        double pow = Math.pow(10.0d, 6.0d);
        Double.isNaN(priceAmountMicros);
        tL_inputStorePaymentGiftPremium.amount = (long) ((priceAmountMicros / pow) * Math.pow(10.0d, BillingController.getInstance().getCurrencyExp(tL_inputStorePaymentGiftPremium.currency)));
        BillingController.getInstance().addResultListener(this.premiumTier.giftOption.store_product, new Consumer() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda6
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                SendGiftSheet.this.lambda$buyPremiumTier$8((BillingResult) obj);
            }
        });
        final TLRPC.TL_payments_canPurchasePremium tL_payments_canPurchasePremium = new TLRPC.TL_payments_canPurchasePremium();
        tL_payments_canPurchasePremium.purpose = tL_inputStorePaymentGiftPremium;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_canPurchasePremium, new RequestDelegate() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendGiftSheet.this.lambda$buyPremiumTier$10(tL_inputStorePaymentGiftPremium, tL_payments_canPurchasePremium, tLObject, tL_error);
            }
        });
    }

    private void buyStarGift() {
        StarsController.getInstance(this.currentAccount).buyStarGift(AndroidUtilities.getActivity(getContext()), this.starGift, this.anonymous, this.dialogId, getMessage(), new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda8
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                SendGiftSheet.this.lambda$buyStarGift$2((Boolean) obj, (String) obj2);
            }
        });
    }

    private TLRPC.TL_textWithEntities getMessage() {
        TLRPC.MessageAction messageAction = this.action;
        if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            return ((TLRPC.TL_messageActionStarGift) messageAction).message;
        }
        if (messageAction instanceof TLRPC.TL_messageActionGiftCode) {
            return ((TLRPC.TL_messageActionGiftCode) messageAction).message;
        }
        if (messageAction instanceof TLRPC.TL_messageActionGiftPremium) {
            return ((TLRPC.TL_messageActionGiftPremium) messageAction).message;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$10(final TLRPC.TL_inputStorePaymentGiftPremium tL_inputStorePaymentGiftPremium, final TLRPC.TL_payments_canPurchasePremium tL_payments_canPurchasePremium, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SendGiftSheet.this.lambda$buyPremiumTier$9(tLObject, tL_inputStorePaymentGiftPremium, tL_error, tL_payments_canPurchasePremium);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$buyPremiumTier$3(TLRPC.User user) {
        PremiumPreviewGiftSentBottomSheet.show(new ArrayList(Arrays.asList(user)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$4(final TLRPC.User user, Void r6) {
        Runnable runnable = this.closeParentSheet;
        if (runnable != null) {
            runnable.run();
        }
        dismiss();
        NotificationCenter.getInstance(UserConfig.selectedAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.giftsToUserSent, new Object[0]);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                SendGiftSheet.lambda$buyPremiumTier$3(TLRPC.User.this);
            }
        }, 250L);
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean("show_gift_for_" + this.dialogId, true).putBoolean(Calendar.getInstance().get(1) + "show_gift_for_" + this.dialogId, true).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$5(TLRPC.TL_error tL_error) {
        BoostDialogs.showToastError(getContext(), tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$6() {
        onGiftSuccess(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$7() {
        onGiftSuccess(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$8(BillingResult billingResult) {
        if (billingResult.getResponseCode() == 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    SendGiftSheet.this.lambda$buyPremiumTier$7();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyPremiumTier$9(TLObject tLObject, TLRPC.TL_inputStorePaymentGiftPremium tL_inputStorePaymentGiftPremium, TLRPC.TL_error tL_error, TLRPC.TL_payments_canPurchasePremium tL_payments_canPurchasePremium) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            BillingController.getInstance().launchBillingFlow(getBaseFragment().getParentActivity(), AccountInstance.getInstance(this.currentAccount), tL_inputStorePaymentGiftPremium, Collections.singletonList(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(this.premiumTier.googlePlayProductDetails).build()));
        } else if (tL_error != null) {
            AlertsCreator.processError(this.currentAccount, tL_error, getBaseFragment(), tL_payments_canPurchasePremium, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buyStarGift$2(Boolean bool, String str) {
        if (bool.booleanValue()) {
            Runnable runnable = this.closeParentSheet;
            if (runnable != null) {
                runnable.run();
            }
            AndroidUtilities.hideKeyboard(this.messageEdit);
            dismiss();
        } else if ("STARGIFT_USAGE_LIMITED".equalsIgnoreCase(str)) {
            AndroidUtilities.hideKeyboard(this.messageEdit);
            dismiss();
            StarsController.getInstance(this.currentAccount).makeStarGiftSoldOut(this.starGift);
            return;
        }
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i) {
        UniversalAdapter universalAdapter = this.adapter;
        if (!this.reverseLayout) {
            i--;
        }
        UItem item = universalAdapter.getItem(i);
        if (item != null && item.id == 1) {
            boolean z = !this.anonymous;
            this.anonymous = z;
            TLRPC.MessageAction messageAction = this.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                ((TLRPC.TL_messageActionStarGift) messageAction).name_hidden = z;
            }
            this.messageObject.updateMessageText();
            this.actionCell.setMessageObject(this.messageObject, true);
            this.adapter.update(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(TL_stars.StarGift starGift, View view) {
        if (this.button.isLoading()) {
            return;
        }
        this.button.setLoading(true);
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
        } else if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
        }
        if (starGift != null) {
            buyStarGift();
        } else {
            buyPremiumTier();
        }
    }

    private void onGiftSuccess(boolean z) {
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.dialogId);
        TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(this.dialogId);
        if (userFull != null && (userOrChat instanceof TLRPC.User)) {
            TLRPC.User user = (TLRPC.User) userOrChat;
            user.premium = true;
            MessagesController.getInstance(this.currentAccount).putUser(user, true);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(user.id), userFull);
        }
        if (getBaseFragment() != null) {
            ArrayList<BaseFragment> arrayList = new ArrayList(((LaunchActivity) getBaseFragment().getParentActivity()).getActionBarLayout().getFragmentStack());
            INavigationLayout parentLayout = getBaseFragment().getParentLayout();
            ChatActivity chatActivity = null;
            for (BaseFragment baseFragment : arrayList) {
                if (baseFragment instanceof ChatActivity) {
                    chatActivity = (ChatActivity) baseFragment;
                    if (chatActivity.getDialogId() != this.dialogId) {
                        baseFragment.removeSelfFromStack();
                    }
                } else if (baseFragment instanceof ProfileActivity) {
                    if (z && parentLayout.getLastFragment() == baseFragment) {
                        baseFragment.lambda$onBackPressed$319();
                    }
                    baseFragment.removeSelfFromStack();
                }
            }
            if (chatActivity == null || chatActivity.getDialogId() != this.dialogId) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.dialogId);
                parentLayout.presentFragment(new ChatActivity(bundle), true);
            }
        }
        dismiss();
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected RecyclerListView.SelectionAdapter createAdapter(RecyclerListView recyclerListView) {
        UniversalAdapter universalAdapter = new UniversalAdapter(this.recyclerListView, getContext(), this.currentAccount, 0, true, new Utilities.Callback2() { // from class: org.telegram.ui.Gifts.SendGiftSheet$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                SendGiftSheet.this.fillItems((ArrayList) obj, (UniversalAdapter) obj2);
            }
        }, this.resourcesProvider);
        this.adapter = universalAdapter;
        universalAdapter.setApplyBackground(false);
        return this.adapter;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
            return;
        }
        if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
            return;
        }
        EditEmojiTextCell editEmojiTextCell = this.messageEdit;
        if (editEmojiTextCell != null) {
            editEmojiTextCell.editTextEmoji.onPause();
        }
        super.dismiss();
    }

    public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        UItem asShadow;
        arrayList.add(UItem.asCustom(-1, this.chatView));
        arrayList.add(UItem.asCustom(-2, this.messageEdit));
        if (this.starGift != null) {
            arrayList.add(UItem.asShadow(-3, null));
            arrayList.add(UItem.asCheck(1, LocaleController.getString(R.string.Gift2Hide)).setChecked(this.anonymous));
            asShadow = UItem.asShadow(-4, LocaleController.formatString(R.string.Gift2HideInfo, this.name));
        } else {
            asShadow = UItem.asShadow(-3, LocaleController.formatString(R.string.Gift2MessagePremiumInfo, this.name));
        }
        arrayList.add(asShadow);
        if (this.reverseLayout) {
            Collections.reverse(arrayList);
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected CharSequence getTitle() {
        return LocaleController.getString(R.string.Gift2Title);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (this.messageEdit.editTextEmoji.getEmojiPadding() > 0) {
            this.messageEdit.editTextEmoji.hidePopup(true);
        } else if (this.messageEdit.editTextEmoji.isKeyboardVisible()) {
            this.messageEdit.editTextEmoji.closeKeyboard();
        } else {
            super.onBackPressed();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        EditEmojiTextCell editEmojiTextCell = this.messageEdit;
        if (editEmojiTextCell != null) {
            editEmojiTextCell.editTextEmoji.onResume();
        }
        super.show();
    }
}
