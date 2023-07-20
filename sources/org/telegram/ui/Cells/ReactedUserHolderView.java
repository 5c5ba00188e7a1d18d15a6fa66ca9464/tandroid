package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MessageSeenCheckDrawable;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Stories.StoriesUtilities;
/* loaded from: classes3.dex */
public class ReactedUserHolderView extends FrameLayout {
    public static int STYLE_DEFAULT = 0;
    public static int STYLE_STORY = 1;
    public static final MessageSeenCheckDrawable reactDrawable;
    public static final MessageSeenCheckDrawable seenDrawable;
    AvatarDrawable avatarDrawable;
    public BackupImageView avatarView;
    int currentAccount;
    public long dialogId;
    View overlaySelectorView;
    public StoriesUtilities.AvatarStoryParams params;
    BackupImageView reactView;
    Theme.ResourcesProvider resourcesProvider;
    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable rightDrawable;
    int style;
    SimpleTextView subtitleView;
    SimpleTextView titleView;

    public void openStory(long j, Runnable runnable) {
    }

    static {
        int i = R.drawable.msg_mini_checks;
        int i2 = Theme.key_windowBackgroundWhiteGrayText;
        seenDrawable = new MessageSeenCheckDrawable(i, i2);
        reactDrawable = new MessageSeenCheckDrawable(R.drawable.msg_reactions, i2, 16, 16, 5.66f);
    }

    public ReactedUserHolderView(final int i, int i2, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.avatarDrawable = new AvatarDrawable();
        this.params = new StoriesUtilities.AvatarStoryParams(false) { // from class: org.telegram.ui.Cells.ReactedUserHolderView.1
            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void openStory(long j, Runnable runnable) {
                ReactedUserHolderView.this.openStory(j, runnable);
            }
        };
        this.style = i;
        this.currentAccount = i2;
        this.resourcesProvider = resourcesProvider;
        setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(50.0f)));
        int i3 = i == STYLE_STORY ? 48 : 34;
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Cells.ReactedUserHolderView.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            public void onDraw(Canvas canvas) {
                if (i == ReactedUserHolderView.STYLE_STORY) {
                    ReactedUserHolderView.this.params.originalAvatarRect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    StoriesUtilities.drawAvatarWithStory(ReactedUserHolderView.this.dialogId, canvas, getImageReceiver(), ReactedUserHolderView.this.params);
                    return;
                }
                super.onDraw(canvas);
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return ReactedUserHolderView.this.params.checkOnTouchEvent(motionEvent, this);
            }
        };
        this.avatarView = backupImageView;
        float f = i3;
        backupImageView.setRoundRadius(AndroidUtilities.dp(f));
        addView(this.avatarView, LayoutHelper.createFrameRelatively(f, f, 8388627, 10.0f, 0.0f, 0.0f, 0.0f));
        if (i == STYLE_STORY) {
            setClipChildren(false);
        }
        SimpleTextView simpleTextView = new SimpleTextView(this, context) { // from class: org.telegram.ui.Cells.ReactedUserHolderView.3
            @Override // org.telegram.ui.ActionBar.SimpleTextView
            public boolean setText(CharSequence charSequence) {
                return super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
            }
        };
        this.titleView = simpleTextView;
        NotificationCenter.listenEmojiLoading(simpleTextView);
        this.titleView.setTextSize(16);
        this.titleView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem, resourcesProvider));
        this.titleView.setEllipsizeByGradient(true);
        this.titleView.setImportantForAccessibility(2);
        this.titleView.setRightPadding(AndroidUtilities.dp(30.0f));
        this.titleView.setTranslationX(LocaleController.isRTL ? AndroidUtilities.dp(30.0f) : 0.0f);
        this.titleView.setRightDrawableOutside(true);
        int i4 = STYLE_STORY;
        float f2 = i == i4 ? 7.66f : 5.33f;
        float f3 = i == i4 ? 73.0f : 55.0f;
        addView(this.titleView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 55, f3, f2, 12.0f, 0.0f));
        this.rightDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(18.0f));
        this.titleView.setDrawablePadding(AndroidUtilities.dp(3.0f));
        this.titleView.setRightDrawable(this.rightDrawable);
        SimpleTextView simpleTextView2 = new SimpleTextView(context);
        this.subtitleView = simpleTextView2;
        simpleTextView2.setTextSize(13);
        this.subtitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, resourcesProvider));
        this.subtitleView.setEllipsizeByGradient(true);
        this.subtitleView.setImportantForAccessibility(2);
        this.subtitleView.setTranslationX(LocaleController.isRTL ? AndroidUtilities.dp(30.0f) : 0.0f);
        addView(this.subtitleView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 55, f3, i == STYLE_STORY ? 24.0f : 19.0f, 20.0f, 0.0f));
        BackupImageView backupImageView2 = new BackupImageView(context);
        this.reactView = backupImageView2;
        addView(backupImageView2, LayoutHelper.createFrameRelatively(24.0f, 24.0f, 8388629, 0.0f, 0.0f, 12.0f, 0.0f));
        View view = new View(context);
        this.overlaySelectorView = view;
        view.setBackground(Theme.getSelectorDrawable(false));
        addView(this.overlaySelectorView, LayoutHelper.createFrame(-1, -1.0f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00fd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setUserReaction(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLRPC$Reaction tLRPC$Reaction, long j, boolean z, boolean z2) {
        TLRPC$ChatPhoto tLRPC$ChatPhoto;
        Drawable drawable;
        String formatString;
        boolean z3;
        Object obj;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$User tLRPC$User2 = tLRPC$User == null ? tLRPC$Chat : tLRPC$User;
        if (tLRPC$User2 == null) {
            return;
        }
        Long emojiStatusDocumentId = tLRPC$User2 instanceof TLRPC$User ? UserObject.getEmojiStatusDocumentId(tLRPC$User2) : null;
        if (emojiStatusDocumentId == null) {
            if (tLRPC$User != null && tLRPC$User.premium) {
                this.rightDrawable.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
            } else {
                this.rightDrawable.set((Drawable) null, false);
            }
        } else {
            this.rightDrawable.set(emojiStatusDocumentId.longValue(), false);
        }
        this.rightDrawable.setColor(Integer.valueOf(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider)));
        this.avatarDrawable.setInfo((TLObject) tLRPC$User2);
        if (tLRPC$User != null) {
            this.dialogId = tLRPC$User.id;
            this.titleView.setText(UserObject.getUserName(tLRPC$User));
        } else {
            this.dialogId = tLRPC$Chat.id;
            this.titleView.setText(tLRPC$Chat.title);
        }
        Drawable drawable2 = this.avatarDrawable;
        if (tLRPC$User == null ? !((tLRPC$ChatPhoto = tLRPC$Chat.photo) == null || (drawable = tLRPC$ChatPhoto.strippedBitmap) == null) : !((tLRPC$UserProfilePhoto = tLRPC$User.photo) == null || (drawable = tLRPC$UserProfilePhoto.strippedBitmap) == null)) {
            drawable2 = drawable;
        }
        this.avatarView.setImage(ImageLocation.getForUserOrChat(tLRPC$User2, 1), "50_50", drawable2, tLRPC$User2);
        if (tLRPC$Reaction != null) {
            ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$Reaction);
            if (fromTLReaction.emojicon != null) {
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(this.currentAccount).getReactionsMap().get(fromTLReaction.emojicon);
                if (tLRPC$TL_availableReaction != null) {
                    this.reactView.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_lastreactframe", "webp", DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.static_icon.thumbs, Theme.key_windowBackgroundGray, 1.0f), tLRPC$TL_availableReaction);
                } else {
                    this.reactView.setImageDrawable(null);
                    z3 = false;
                    int i = R.string.AccDescrReactedWith;
                    Object[] objArr = new Object[2];
                    objArr[0] = this.titleView.getText();
                    obj = fromTLReaction.emojicon;
                    if (obj == null) {
                        obj = tLRPC$Reaction;
                    }
                    objArr[1] = obj;
                    formatString = LocaleController.formatString("AccDescrReactedWith", i, objArr);
                }
            } else {
                AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(0, this.currentAccount, fromTLReaction.documentId);
                animatedEmojiDrawable.setColorFilter(Theme.getAnimatedEmojiColorFilter(this.resourcesProvider));
                this.reactView.setAnimatedEmojiDrawable(animatedEmojiDrawable);
            }
            z3 = true;
            int i2 = R.string.AccDescrReactedWith;
            Object[] objArr2 = new Object[2];
            objArr2[0] = this.titleView.getText();
            obj = fromTLReaction.emojicon;
            if (obj == null) {
            }
            objArr2[1] = obj;
            formatString = LocaleController.formatString("AccDescrReactedWith", i2, objArr2);
        } else {
            this.reactView.setImageDrawable(null);
            formatString = LocaleController.formatString("AccDescrPersonHasSeen", R.string.AccDescrPersonHasSeen, this.titleView.getText());
            z3 = false;
        }
        if (j != 0) {
            formatString = formatString + " " + LocaleController.formatSeenDate(j);
        }
        setContentDescription(formatString);
        float f = 0.0f;
        if (j != 0) {
            this.subtitleView.setVisibility(0);
            this.subtitleView.setText(TextUtils.concat((z ? seenDrawable : reactDrawable).getSpanned(getContext(), this.resourcesProvider), LocaleController.formatSeenDate(j)));
            this.subtitleView.setTranslationY(!z ? AndroidUtilities.dp(-1.0f) : 0.0f);
            this.titleView.setTranslationY(0.0f);
            if (z2) {
                this.titleView.setTranslationY(AndroidUtilities.dp(9.0f));
                this.titleView.animate().translationY(0.0f);
                this.subtitleView.setAlpha(0.0f);
                this.subtitleView.animate().alpha(1.0f);
            }
        } else {
            this.subtitleView.setVisibility(8);
            this.titleView.setTranslationY(AndroidUtilities.dp(9.0f));
        }
        this.titleView.setRightPadding(AndroidUtilities.dp(z3 ? 30.0f : 0.0f));
        this.titleView.setTranslationX((z3 && LocaleController.isRTL) ? AndroidUtilities.dp(30.0f) : 0.0f);
        ((ViewGroup.MarginLayoutParams) this.subtitleView.getLayoutParams()).rightMargin = AndroidUtilities.dp((!z3 || LocaleController.isRTL) ? 12.0f : 36.0f);
        SimpleTextView simpleTextView = this.subtitleView;
        if (z3 && LocaleController.isRTL) {
            f = AndroidUtilities.dp(30.0f);
        }
        simpleTextView.setTranslationX(f);
    }

    public void setUserReaction(TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction) {
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        if (tLRPC$MessagePeerReaction == null) {
            return;
        }
        long peerId = MessageObject.getPeerId(tLRPC$MessagePeerReaction.peer_id);
        if (peerId > 0) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId));
            chat = null;
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
            tLRPC$User = null;
        }
        setUserReaction(tLRPC$User, chat, tLRPC$MessagePeerReaction.reaction, tLRPC$MessagePeerReaction.date, tLRPC$MessagePeerReaction.dateIsSeen, false);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.style == STYLE_DEFAULT ? 50 : 58), 1073741824));
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.rightDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.attach();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.rightDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.detach();
        }
        this.params.onDetachFromWindow();
    }
}
