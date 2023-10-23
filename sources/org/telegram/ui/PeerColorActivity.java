package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_account_updateColor;
import org.telegram.tgnet.TLRPC$TL_channels_updateColor;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.Text;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
/* loaded from: classes3.dex */
public class PeerColorActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private boolean applying;
    private ButtonWithCounterView button;
    private FrameLayout buttonContainer;
    private CharSequence buttonLocked;
    private CharSequence buttonUnlocked;
    int colorPickerRow;
    private FrameLayout contentView;
    private final long dialogId;
    int iconRow;
    int info2Row;
    int infoRow;
    private final boolean isChannel;
    private RecyclerView.Adapter listAdapter;
    private RecyclerListView listView;
    private ThemePreviewMessagesCell messagesCellPreview;
    private PeerColorPicker peerColorPicker;
    int previewRow;
    int rowCount;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private int selectedColor;
    private long selectedEmoji;
    private SetReplyIconCell setReplyIconCell;

    public PeerColorActivity(long j) {
        this.dialogId = j;
        this.isChannel = j != 0;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        Bulletin.addDelegate(this, new Bulletin.Delegate(this) { // from class: org.telegram.ui.PeerColorActivity.1
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public boolean clipWithGradient(int i) {
                return true;
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getTopOffset(int i) {
                return Bulletin.Delegate.-CC.$default$getTopOffset(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                return AndroidUtilities.dp(62.0f);
            }
        });
        getMediaDataController().loadReplyIcons();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setTitle(LocaleController.getString(this.isChannel ? R.string.ChannelColorTitle : R.string.UserColorTitle));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PeerColorActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    PeerColorActivity.this.finishFragment();
                }
            }
        });
        TLRPC$User currentUser = getUserConfig().getCurrentUser();
        int i = currentUser.flags2;
        if ((i & 64) != 0) {
            this.selectedEmoji = currentUser.background_emoji_id;
        }
        if ((i & 128) != 0) {
            this.selectedColor = currentUser.color;
        } else {
            this.selectedColor = (int) (currentUser.id % 7);
        }
        FrameLayout frameLayout = new FrameLayout(context);
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        ((DefaultItemAnimator) recyclerListView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView2 = this.listView;
        3 r4 = new 3(context);
        this.listAdapter = r4;
        recyclerListView2.setAdapter(r4);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                PeerColorActivity.this.lambda$createView$0(view, i2);
            }
        });
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.buttonContainer = frameLayout2;
        frameLayout2.setPadding(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f));
        this.buttonContainer.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("l");
        spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_mini_lock2), 0, 1, 33);
        this.buttonUnlocked = LocaleController.getString(this.isChannel ? R.string.ChannelColorApply : R.string.UserColorApply);
        this.buttonLocked = new SpannableStringBuilder(spannableStringBuilder).append((CharSequence) " ").append(this.buttonUnlocked);
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, getResourceProvider());
        this.button = buttonWithCounterView;
        buttonWithCounterView.text.setHacks(true, true, true);
        this.button.setText((this.isChannel || getUserConfig().isPremium()) ? this.buttonUnlocked : this.buttonLocked, false);
        this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerColorActivity.this.lambda$createView$6(view);
            }
        });
        this.buttonContainer.addView(this.button, LayoutHelper.createFrame(-1, 48.0f));
        frameLayout.addView(this.buttonContainer, LayoutHelper.createFrame(-1, -2, 80));
        this.contentView = frameLayout;
        this.fragmentView = frameLayout;
        updateColors();
        updateRows();
        return this.contentView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 3 extends RecyclerListView.SelectionAdapter {
        final /* synthetic */ Context val$context;

        3(Context context) {
            this.val$context = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SetReplyIconCell setReplyIconCell;
            if (i == 0) {
                ThemePreviewMessagesCell themePreviewMessagesCell = PeerColorActivity.this.messagesCellPreview = new ThemePreviewMessagesCell(this.val$context, ((BaseFragment) PeerColorActivity.this).parentLayout, 3, PeerColorActivity.this.dialogId);
                if (Build.VERSION.SDK_INT >= 19) {
                    themePreviewMessagesCell.setImportantForAccessibility(4);
                }
                themePreviewMessagesCell.fragment = PeerColorActivity.this;
                setReplyIconCell = themePreviewMessagesCell;
            } else if (i == 1) {
                final PeerColorPicker peerColorPicker = PeerColorActivity.this.peerColorPicker = new PeerColorPicker(this.val$context, PeerColorActivity.this.getResourceProvider());
                peerColorPicker.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, PeerColorActivity.this.getResourceProvider()));
                peerColorPicker.setSelected(PeerColorActivity.this.selectedColor);
                peerColorPicker.layoutManager.scrollToPositionWithOffset(peerColorPicker.selectedPosition, AndroidUtilities.displaySize.x / 2);
                peerColorPicker.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PeerColorActivity$3$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i2) {
                        PeerColorActivity.3.this.lambda$onCreateViewHolder$0(peerColorPicker, view, i2);
                    }
                });
                setReplyIconCell = peerColorPicker;
            } else if (i == 3) {
                SetReplyIconCell setReplyIconCell2 = PeerColorActivity.this.setReplyIconCell = new SetReplyIconCell(this.val$context);
                setReplyIconCell2.update(false);
                setReplyIconCell = setReplyIconCell2;
            } else if (i != 4) {
                setReplyIconCell = new TextInfoPrivacyCell(this.val$context);
            } else {
                View view = new View(this, this.val$context) { // from class: org.telegram.ui.PeerColorActivity.3.1
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(16.0f), 1073741824));
                    }
                };
                view.setBackground(Theme.getThemedDrawableByKey(this.val$context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                setReplyIconCell = view;
            }
            return new RecyclerListView.Holder(setReplyIconCell);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(PeerColorPicker peerColorPicker, View view, int i) {
            PeerColorActivity.this.selectedColor = peerColorPicker.toColor(i);
            peerColorPicker.setSelectedPosition(i);
            if (view.getLeft() - peerColorPicker.getPaddingLeft() < AndroidUtilities.dp(24.0f)) {
                peerColorPicker.smoothScrollBy(i == 0 ? Math.max(-(view.getLeft() - peerColorPicker.getPaddingLeft()), -AndroidUtilities.dp(64.0f)) : -AndroidUtilities.dp(64.0f), 0);
            } else if (view.getRight() - peerColorPicker.getPaddingLeft() > ((AndroidUtilities.displaySize.x - peerColorPicker.getPaddingLeft()) - peerColorPicker.getPaddingRight()) - AndroidUtilities.dp(24.0f)) {
                peerColorPicker.smoothScrollBy(i == peerColorPicker.adapter.getItemCount() + (-1) ? Math.min((AndroidUtilities.displaySize.x - view.getRight()) - peerColorPicker.getPaddingRight(), AndroidUtilities.dp(64.0f)) : AndroidUtilities.dp(64.0f), 0);
            }
            PeerColorActivity.this.updateMessages();
            if (PeerColorActivity.this.setReplyIconCell != null) {
                PeerColorActivity.this.setReplyIconCell.invalidate();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (getItemViewType(i) != 2) {
                return;
            }
            TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
            PeerColorActivity peerColorActivity = PeerColorActivity.this;
            if (i == peerColorActivity.infoRow) {
                textInfoPrivacyCell.setText(LocaleController.getString(peerColorActivity.isChannel ? R.string.ChannelColorHint : R.string.UserColorHint));
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.val$context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            } else if (i == peerColorActivity.info2Row) {
                textInfoPrivacyCell.setText(LocaleController.getString(peerColorActivity.isChannel ? R.string.ChannelReplyIconHint : R.string.UserReplyIconHint));
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.val$context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PeerColorActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            PeerColorActivity peerColorActivity = PeerColorActivity.this;
            if (i == peerColorActivity.previewRow) {
                return 0;
            }
            if (i != peerColorActivity.infoRow && i != peerColorActivity.info2Row) {
                if (i == peerColorActivity.colorPickerRow) {
                    return 1;
                }
                if (i == peerColorActivity.iconRow) {
                    return 3;
                }
                if (i == getItemCount() - 1) {
                    return 4;
                }
            }
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view, int i) {
        if (view instanceof SetReplyIconCell) {
            showSelectStatusDialog((SetReplyIconCell) view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(View view) {
        if (this.button.isLoading()) {
            return;
        }
        if (this.isChannel) {
            this.button.setLoading(true);
            getMessagesController().getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    PeerColorActivity.this.lambda$createView$4((TL_stories$TL_premium_boostsStatus) obj);
                }
            });
        } else if (!getUserConfig().isPremium()) {
            Bulletin createSimpleBulletin = BulletinFactory.of(this).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.premiumText(LocaleController.getString(R.string.UserColorApplyPremium), new Runnable() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PeerColorActivity.this.lambda$createView$5();
                }
            }));
            createSimpleBulletin.getLayout().setPadding(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(8.0f));
            createSimpleBulletin.show();
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
        } else {
            apply();
            finishFragment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(final TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        if (tL_stories$TL_premium_boostsStatus.level < getMessagesController().channelColorLevelMin) {
            getMessagesController().getBoostsController().userCanBoostChannel(this.dialogId, tL_stories$TL_premium_boostsStatus, new Consumer() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda2
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    PeerColorActivity.this.lambda$createView$3(tL_stories$TL_premium_boostsStatus, (ChannelBoostsController.CanApplyBoost) obj);
                }
            });
            return;
        }
        this.button.setLoading(false);
        apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getContext(), 20, this.currentAccount, getResourceProvider());
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(tL_stories$TL_premium_boostsStatus, true);
        limitReachedBottomSheet.setDialogId(this.dialogId);
        limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PeerColorActivity.this.lambda$createView$1();
            }
        });
        showDialog(limitReachedBottomSheet);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PeerColorActivity.this.lambda$createView$2();
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -this.dialogId);
        bundle.putBoolean("is_megagroup", chat.megagroup);
        bundle.putBoolean("start_from_boosts", true);
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
        if (chatFull == null || !chatFull.can_view_stats) {
            bundle.putBoolean("only_boosts", true);
        }
        presentFragment(new StatisticActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5() {
        presentFragment(new PremiumPreviewFragment("color"));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        super.onBecomeFullyHidden();
        apply();
    }

    private void apply() {
        if (this.applying || this.peerColorPicker == null) {
            return;
        }
        if (this.isChannel || getUserConfig().isPremium()) {
            if (this.isChannel) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
                if (chat == null) {
                    return;
                }
                if (this.selectedColor == chat.color) {
                    if (this.selectedEmoji == ((chat.flags2 & 64) == 0 ? 0L : chat.background_emoji_id)) {
                        return;
                    }
                }
                TLRPC$TL_channels_updateColor tLRPC$TL_channels_updateColor = new TLRPC$TL_channels_updateColor();
                int i = chat.flags2 | 64;
                chat.flags2 = i;
                int i2 = this.selectedColor;
                chat.color = i2;
                tLRPC$TL_channels_updateColor.color = i2;
                long j = this.selectedEmoji;
                if (j != 0) {
                    chat.flags2 = i | 32;
                    chat.background_emoji_id = j;
                    tLRPC$TL_channels_updateColor.flags |= 1;
                    tLRPC$TL_channels_updateColor.background_emoji_id = j;
                } else {
                    chat.flags2 = i & (-33);
                    chat.background_emoji_id = 0L;
                }
                getMessagesController().putChat(chat, false);
                getUserConfig().saveConfig(true);
                getConnectionsManager().sendRequest(tLRPC$TL_channels_updateColor, null);
            } else {
                TLRPC$User currentUser = getUserConfig().getCurrentUser();
                if (this.selectedColor == currentUser.color) {
                    if (this.selectedEmoji == ((currentUser.flags2 & 64) == 0 ? 0L : currentUser.background_emoji_id)) {
                        return;
                    }
                }
                TLRPC$TL_account_updateColor tLRPC$TL_account_updateColor = new TLRPC$TL_account_updateColor();
                int i3 = currentUser.flags2 | 128;
                currentUser.flags2 = i3;
                int i4 = this.selectedColor;
                currentUser.color = i4;
                tLRPC$TL_account_updateColor.color = i4;
                long j2 = this.selectedEmoji;
                if (j2 != 0) {
                    currentUser.flags2 = i3 | 64;
                    currentUser.background_emoji_id = j2;
                    tLRPC$TL_account_updateColor.flags |= 1;
                    tLRPC$TL_account_updateColor.background_emoji_id = j2;
                } else {
                    currentUser.flags2 = i3 & (-65);
                    currentUser.background_emoji_id = 0L;
                }
                getMessagesController().putUser(currentUser, false);
                getUserConfig().saveConfig(true);
                getConnectionsManager().sendRequest(tLRPC$TL_account_updateColor, null);
            }
            this.applying = true;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_EMOJI_STATUS));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessages() {
        MessageObject messageObject;
        ThemePreviewMessagesCell themePreviewMessagesCell = this.messagesCellPreview;
        if (themePreviewMessagesCell != null) {
            ChatMessageCell[] cells = themePreviewMessagesCell.getCells();
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null && (messageObject = cells[i].getMessageObject()) != null) {
                    PeerColorPicker peerColorPicker = this.peerColorPicker;
                    if (peerColorPicker != null) {
                        messageObject.overrideLinkColor = peerColorPicker.getColor();
                    }
                    messageObject.overrideLinkEmoji = this.selectedEmoji;
                    cells[i].setAvatar(messageObject);
                    cells[i].invalidate();
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentClosed() {
        super.onFragmentClosed();
        Bulletin.removeDelegate(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SetReplyIconCell extends FrameLayout {
        private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable imageDrawable;
        private Text offText;
        private TextView textView;

        public SetReplyIconCell(Context context) {
            super(context);
            setBackgroundColor(PeerColorActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextSize(1, 16.0f);
            this.textView.setTextColor(PeerColorActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            this.textView.setText(LocaleController.getString(R.string.UserReplyIcon));
            addView(this.textView, LayoutHelper.createFrame(-1, -2.0f, 23, 20.0f, 0.0f, 48.0f, 0.0f));
            this.imageDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, false, AndroidUtilities.dp(24.0f), 13);
        }

        public void update(boolean z) {
            if (PeerColorActivity.this.selectedEmoji != 0) {
                this.imageDrawable.set(PeerColorActivity.this.selectedEmoji, z);
                this.offText = null;
                return;
            }
            this.imageDrawable.set((Drawable) null, z);
            if (this.offText == null) {
                this.offText = new Text(LocaleController.getString(R.string.UserReplyIconOff), 16);
            }
        }

        public void updateImageBounds() {
            this.imageDrawable.setBounds((getWidth() - this.imageDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(21.0f), (getHeight() - this.imageDrawable.getIntrinsicHeight()) / 2, getWidth() - AndroidUtilities.dp(21.0f), (getHeight() + this.imageDrawable.getIntrinsicHeight()) / 2);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            updateImageBounds();
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.imageDrawable;
            PeerColorActivity peerColorActivity = PeerColorActivity.this;
            swapAnimatedEmojiDrawable.setColor(Integer.valueOf(peerColorActivity.getThemedColor(AvatarDrawable.getNameColorKey1For(peerColorActivity.selectedColor))));
            Text text = this.offText;
            if (text != null) {
                text.draw(canvas, (getMeasuredWidth() - this.offText.getWidth()) - AndroidUtilities.dp(19.0f), getMeasuredHeight() / 2.0f, PeerColorActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueText4), 1.0f);
            } else {
                this.imageDrawable.draw(canvas);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageDrawable.detach();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageDrawable.attach();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00c0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showSelectStatusDialog(final SetReplyIconCell setReplyIconCell) {
        SetReplyIconCell setReplyIconCell2;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        int i;
        int i2;
        if (this.selectAnimatedEmojiDialog != null) {
            return;
        }
        final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        int min = (int) Math.min(AndroidUtilities.dp(330.0f), AndroidUtilities.displaySize.y * 0.75f);
        int min2 = (int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f);
        if (setReplyIconCell != null) {
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = setReplyIconCell.imageDrawable;
            if (setReplyIconCell.imageDrawable != null) {
                setReplyIconCell.imageDrawable.play();
                setReplyIconCell.updateImageBounds();
                Rect rect = AndroidUtilities.rectTmp2;
                rect.set(setReplyIconCell.imageDrawable.getBounds());
                int dp = ((-rect.centerY()) + AndroidUtilities.dp(12.0f)) - min;
                i = rect.centerX() - (AndroidUtilities.displaySize.x - min2);
                swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable2;
                i2 = dp;
                setReplyIconCell2 = setReplyIconCell;
                SetReplyIconCell setReplyIconCell3 = setReplyIconCell2;
                int i3 = i2;
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable3 = swapAnimatedEmojiDrawable;
                SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), true, Integer.valueOf(i), 5, true, getResourceProvider(), 24, getThemedColor(AvatarDrawable.getNameColorKey1For(this.selectedColor))) { // from class: org.telegram.ui.PeerColorActivity.4
                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog
                    protected float getScrimDrawableTranslationY() {
                        return 0.0f;
                    }

                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog
                    protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                        PeerColorActivity.this.selectedEmoji = l == null ? 0L : l.longValue();
                        SetReplyIconCell setReplyIconCell4 = setReplyIconCell;
                        if (setReplyIconCell4 != null) {
                            setReplyIconCell4.update(true);
                        }
                        PeerColorActivity.this.updateMessages();
                        if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                            PeerColorActivity.this.selectAnimatedEmojiDialog = null;
                            selectAnimatedEmojiDialogWindowArr[0].dismiss();
                        }
                    }
                };
                long j = this.selectedEmoji;
                selectAnimatedEmojiDialog.setSelected(j != 0 ? Long.valueOf(j) : null);
                selectAnimatedEmojiDialog.setSaveState(3);
                selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable3, setReplyIconCell3);
                SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.PeerColorActivity.5
                    @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        PeerColorActivity.this.selectAnimatedEmojiDialog = null;
                    }
                };
                this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
                selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(setReplyIconCell, 0, i3, 53);
                selectAnimatedEmojiDialogWindowArr[0].dimBehind();
            }
            swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable2;
            setReplyIconCell2 = setReplyIconCell;
        } else {
            setReplyIconCell2 = null;
            swapAnimatedEmojiDrawable = null;
        }
        i = 0;
        i2 = 0;
        SetReplyIconCell setReplyIconCell32 = setReplyIconCell2;
        int i32 = i2;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable32 = swapAnimatedEmojiDrawable;
        View selectAnimatedEmojiDialog2 = new SelectAnimatedEmojiDialog(this, getContext(), true, Integer.valueOf(i), 5, true, getResourceProvider(), 24, getThemedColor(AvatarDrawable.getNameColorKey1For(this.selectedColor))) { // from class: org.telegram.ui.PeerColorActivity.4
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected float getScrimDrawableTranslationY() {
                return 0.0f;
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                PeerColorActivity.this.selectedEmoji = l == null ? 0L : l.longValue();
                SetReplyIconCell setReplyIconCell4 = setReplyIconCell;
                if (setReplyIconCell4 != null) {
                    setReplyIconCell4.update(true);
                }
                PeerColorActivity.this.updateMessages();
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    PeerColorActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }
        };
        long j2 = this.selectedEmoji;
        selectAnimatedEmojiDialog2.setSelected(j2 != 0 ? Long.valueOf(j2) : null);
        selectAnimatedEmojiDialog2.setSaveState(3);
        selectAnimatedEmojiDialog2.setScrimDrawable(swapAnimatedEmojiDrawable32, setReplyIconCell32);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow2 = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog2, -2, -2) { // from class: org.telegram.ui.PeerColorActivity.5
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                PeerColorActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow2;
        selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(setReplyIconCell, 0, i32, 53);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.previewRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.colorPickerRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.infoRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.iconRow = i3;
        this.rowCount = i4 + 1;
        this.info2Row = i4;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.PeerColorActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                PeerColorActivity.this.updateColors();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, Theme.key_windowBackgroundWhite, Theme.key_windowBackgroundWhiteBlackText, Theme.key_windowBackgroundWhiteGrayText2, Theme.key_listSelector, Theme.key_windowBackgroundGray, Theme.key_windowBackgroundWhiteGrayText4, Theme.key_text_RedRegular, Theme.key_windowBackgroundChecked, Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NotifyDataSetChanged"})
    public void updateColors() {
        this.contentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    @SuppressLint({"NotifyDataSetChanged"})
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ButtonWithCounterView buttonWithCounterView;
        if (i2 == this.currentAccount && i == NotificationCenter.currentUserPremiumStatusChanged && (buttonWithCounterView = this.button) != null) {
            buttonWithCounterView.setText((this.isChannel || getUserConfig().isPremium()) ? this.buttonUnlocked : this.buttonLocked, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class PeerColorPicker extends RecyclerListView {
        private static final int[] order = {5, 3, 1, 0, 2, 4, 6};
        public final RecyclerView.Adapter adapter;
        public final LinearLayoutManager layoutManager;
        private final Theme.ResourcesProvider resourcesProvider;
        private int selectedPosition;

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (getParent() != null && getParent().getParent() != null) {
                getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1) || canScrollHorizontally(1));
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public Integer getSelectorColor(int i) {
            return 0;
        }

        public PeerColorPicker(final Context context, final Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            setClipToPadding(false);
            RecyclerView.Adapter adapter = new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.PeerColorActivity.PeerColorPicker.1
                @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
                public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                    return new RecyclerListView.Holder(new ColorCell(context));
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                    ColorCell colorCell = (ColorCell) viewHolder.itemView;
                    colorCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
                    colorCell.setSelected(i == PeerColorPicker.this.selectedPosition, false);
                    if (i >= 0) {
                        int[] iArr = Theme.keys_avatar_nameInMessage;
                        if (i < iArr.length) {
                            colorCell.set(Theme.getColor(iArr[PeerColorPicker.order[i]], resourcesProvider));
                            return;
                        }
                    }
                    int length = i - Theme.keys_avatar_nameInMessage.length;
                    if (length >= 0) {
                        int[] iArr2 = Theme.keys_avatar_composite_nameInMessage;
                        if (length < iArr2.length) {
                            colorCell.set(Theme.getColor(iArr2[PeerColorPicker.order[length]]), Theme.getColor(Theme.keys_avatar_composite_nameInMessage2[PeerColorPicker.order[length]]));
                        }
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public int getItemCount() {
                    return Theme.keys_avatar_nameInMessage.length + Theme.keys_avatar_composite_nameInMessage.length;
                }
            };
            this.adapter = adapter;
            setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            this.layoutManager = linearLayoutManager;
            linearLayoutManager.setOrientation(0);
            setLayoutManager(linearLayoutManager);
        }

        public void setSelected(int i) {
            setSelectedPosition(toPosition(i));
        }

        public void setSelectedPosition(int i) {
            if (i != this.selectedPosition) {
                this.selectedPosition = i;
                AndroidUtilities.forEachViews((RecyclerView) this, (Consumer<View>) new Consumer() { // from class: org.telegram.ui.PeerColorActivity$PeerColorPicker$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        PeerColorActivity.PeerColorPicker.this.lambda$setSelectedPosition$0((View) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelectedPosition$0(View view) {
            ((ColorCell) view).setSelected(getChildAdapterPosition(view) == this.selectedPosition, true);
        }

        public int getColor() {
            return toColor(this.selectedPosition);
        }

        public int toPosition(int i) {
            if (i >= 0 && i < Theme.keys_avatar_nameInMessage.length) {
                int i2 = 0;
                while (true) {
                    int[] iArr = order;
                    if (i2 >= iArr.length) {
                        break;
                    } else if (iArr[i2] == i) {
                        return i2;
                    } else {
                        i2++;
                    }
                }
            }
            int length = i - Theme.keys_avatar_nameInMessage.length;
            if (length >= 0 && length < Theme.keys_avatar_composite_nameInMessage.length) {
                int i3 = 0;
                while (true) {
                    int[] iArr2 = order;
                    if (i3 >= iArr2.length) {
                        break;
                    } else if (iArr2[i3] == length) {
                        return Theme.keys_avatar_nameInMessage.length + i3;
                    } else {
                        i3++;
                    }
                }
            }
            return 0;
        }

        public int toColor(int i) {
            if (i >= 0 && i < Theme.keys_avatar_nameInMessage.length) {
                return order[i];
            }
            int length = i - Theme.keys_avatar_nameInMessage.length;
            if (length < 0 || length >= Theme.keys_avatar_composite_nameInMessage.length) {
                return 0;
            }
            return Theme.keys_avatar_nameInMessage.length + order[length];
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class ColorCell extends View {
            private final Paint backgroundPaint;
            private final ButtonBounce bounce;
            private final Path circlePath;
            private final Path color2Path;
            private boolean hasColor2;
            private final Paint paint1;
            private final Paint paint2;
            private boolean selected;
            private final AnimatedFloat selectedT;

            public ColorCell(Context context) {
                super(context);
                Paint paint = new Paint(1);
                this.backgroundPaint = paint;
                this.paint1 = new Paint(1);
                this.paint2 = new Paint(1);
                this.circlePath = new Path();
                this.color2Path = new Path();
                this.bounce = new ButtonBounce(this);
                this.selectedT = new AnimatedFloat(this, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
                paint.setStyle(Paint.Style.STROKE);
            }

            @Override // android.view.View
            public void setBackgroundColor(int i) {
                this.backgroundPaint.setColor(i);
            }

            public void set(int i) {
                this.hasColor2 = false;
                this.paint1.setColor(i);
            }

            public void set(int i, int i2) {
                this.hasColor2 = true;
                this.paint1.setColor(i);
                this.paint2.setColor(i2);
            }

            public void setSelected(boolean z, boolean z2) {
                this.selected = z;
                if (!z2) {
                    this.selectedT.set(z, true);
                }
                invalidate();
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                setMeasuredDimension(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                this.circlePath.rewind();
                this.circlePath.addCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, AndroidUtilities.dp(20.0f), Path.Direction.CW);
                this.color2Path.rewind();
                this.color2Path.moveTo(getMeasuredWidth(), 0.0f);
                this.color2Path.lineTo(getMeasuredWidth(), getMeasuredHeight());
                this.color2Path.lineTo(0.0f, getMeasuredHeight());
                this.color2Path.close();
            }

            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.save();
                float scale = this.bounce.getScale(0.05f);
                canvas.scale(scale, scale, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
                canvas.save();
                canvas.clipPath(this.circlePath);
                canvas.drawPaint(this.paint1);
                if (this.hasColor2) {
                    canvas.drawPath(this.color2Path, this.paint2);
                }
                canvas.restore();
                float f = this.selectedT.set(this.selected);
                if (f > 0.0f) {
                    this.backgroundPaint.setStrokeWidth(AndroidUtilities.dpf2(2.0f));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, AndroidUtilities.lerp(AndroidUtilities.dp(20.0f) + (this.backgroundPaint.getStrokeWidth() * 0.5f), AndroidUtilities.dp(20.0f) - (this.backgroundPaint.getStrokeWidth() * 2.0f), f), this.backgroundPaint);
                }
                canvas.restore();
            }

            @Override // android.view.View
            public void setPressed(boolean z) {
                super.setPressed(z);
                this.bounce.setPressed(z);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class ChangeNameColorCell extends View {
        private final Text buttonText;
        private final Drawable drawable;
        private final boolean isChannel;
        private boolean needDivider;
        private final Theme.ResourcesProvider resourcesProvider;
        private Text userText;
        private final Paint userTextBackgroundPaint;
        private int userTextColorKey;

        public ChangeNameColorCell(boolean z, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.userTextBackgroundPaint = new Paint(1);
            this.isChannel = z;
            this.resourcesProvider = resourcesProvider;
            Drawable mutate = context.getResources().getDrawable(R.drawable.msg_palette).mutate();
            this.drawable = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4, resourcesProvider), PorterDuff.Mode.SRC_IN));
            this.buttonText = new Text(LocaleController.getString(R.string.ChangeNameColor), 16);
            updateColors();
        }

        public void updateColors() {
            this.drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(this.isChannel ? Theme.key_windowBackgroundWhiteGrayIcon : Theme.key_windowBackgroundWhiteBlueText4, this.resourcesProvider), PorterDuff.Mode.SRC_IN));
            this.buttonText.setColor(Theme.getColor(this.isChannel ? Theme.key_windowBackgroundWhiteBlackText : Theme.key_windowBackgroundWhiteBlueText4, this.resourcesProvider));
            if (this.userText == null || this.userTextBackgroundPaint == null) {
                return;
            }
            int color = Theme.getColor(this.userTextColorKey, this.resourcesProvider);
            this.userText.setColor(color);
            this.userTextBackgroundPaint.setColor(Theme.multAlpha(color, 0.1f));
        }

        public void set(TLRPC$Chat tLRPC$Chat, boolean z) {
            if (tLRPC$Chat == null) {
                return;
            }
            this.userText = new Text(Emoji.replaceEmoji(tLRPC$Chat.title, Theme.chat_msgTextPaint.getFontMetricsInt(), false), 13, AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            int nameColorKey2For = Theme.isCurrentThemeDark() ? AvatarDrawable.getNameColorKey2For(tLRPC$Chat) : AvatarDrawable.getNameColorKey1For(tLRPC$Chat);
            this.userTextColorKey = nameColorKey2For;
            int color = Theme.getColor(nameColorKey2For, this.resourcesProvider);
            this.userText.setColor(color);
            this.userTextBackgroundPaint.setColor(Theme.multAlpha(color, 0.1f));
            this.needDivider = z;
        }

        public void set(TLRPC$User tLRPC$User) {
            if (tLRPC$User == null) {
                return;
            }
            String str = tLRPC$User.first_name;
            String trim = str == null ? "" : str.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            this.userText = new Text(Emoji.replaceEmoji(trim, Theme.chat_msgTextPaint.getFontMetricsInt(), false), 13, AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            int nameColorKey2For = Theme.isCurrentThemeDark() ? AvatarDrawable.getNameColorKey2For(tLRPC$User) : AvatarDrawable.getNameColorKey1For(tLRPC$User);
            this.userTextColorKey = nameColorKey2For;
            int color = Theme.getColor(nameColorKey2For, this.resourcesProvider);
            this.userText.setColor(color);
            this.userTextBackgroundPaint.setColor(Theme.multAlpha(color, 0.1f));
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        private int rtl(int i) {
            return LocaleController.isRTL ? getMeasuredWidth() - i : i;
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            this.drawable.setBounds(rtl(AndroidUtilities.dp(64.0f) / 2) - (this.drawable.getIntrinsicWidth() / 2), (getMeasuredHeight() / 2) - (this.drawable.getIntrinsicHeight() / 2), rtl(AndroidUtilities.dp(64.0f) / 2) + (this.drawable.getIntrinsicWidth() / 2), (getMeasuredHeight() / 2) + (this.drawable.getIntrinsicHeight() / 2));
            this.drawable.draw(canvas);
            this.buttonText.ellipsize(getMeasuredWidth() - AndroidUtilities.dp(171.0f)).draw(canvas, LocaleController.isRTL ? (getMeasuredWidth() - this.buttonText.getWidth()) - AndroidUtilities.dp(71.0f) : AndroidUtilities.dp(71.0f), getMeasuredHeight() / 2.0f);
            if (this.userText != null) {
                int measuredWidth = (int) ((getMeasuredWidth() - AndroidUtilities.dp(116.0f)) - Math.min(this.buttonText.getWidth(), getMeasuredWidth() - AndroidUtilities.dp(164.0f)));
                int min = (int) Math.min(this.userText.getWidth(), measuredWidth);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(LocaleController.isRTL ? AndroidUtilities.dp(15.0f) : (getMeasuredWidth() - AndroidUtilities.dp(33.0f)) - min, (getMeasuredHeight() - AndroidUtilities.dp(22.0f)) / 2.0f, LocaleController.isRTL ? AndroidUtilities.dp(33.0f) + min : getMeasuredWidth() - AndroidUtilities.dp(15.0f), (getMeasuredHeight() + AndroidUtilities.dp(22.0f)) / 2.0f);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.userTextBackgroundPaint);
                this.userText.ellipsize(measuredWidth).draw(canvas, LocaleController.isRTL ? AndroidUtilities.dp(24.0f) : (getMeasuredWidth() - AndroidUtilities.dp(24.0f)) - min, getMeasuredHeight() / 2.0f);
            }
            if (this.needDivider) {
                Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
                Paint paint = resourcesProvider != null ? resourcesProvider.getPaint("paintDivider") : null;
                if (paint == null) {
                    paint = Theme.dividerPaint;
                }
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(64.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(64.0f) : 0), getMeasuredHeight() - 1, paint);
            }
        }
    }
}
