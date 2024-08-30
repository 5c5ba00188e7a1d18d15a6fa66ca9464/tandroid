package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatMessageSharedResources;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagePreviewParams;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_webPagePending;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.MessagePreviewView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.PinchToZoomHelper;
/* loaded from: classes3.dex */
public abstract class MessagePreviewView extends FrameLayout {
    Runnable changeBoundsRunnable;
    final ChatActivity chatActivity;
    private final int currentAccount;
    TLRPC$Chat currentChat;
    TLRPC$User currentUser;
    private final ArrayList drawingGroups;
    boolean isLandscapeMode;
    final MessagePreviewParams messagePreviewParams;
    ValueAnimator offsetsAnimator;
    private final ResourcesDelegate resourcesProvider;
    boolean returnSendersNames;
    TLRPC$Peer sendAsPeer;
    final boolean showOutdatedQuote;
    boolean showing;
    TabsView tabsView;
    ViewPagerFixed viewPager;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ActionBar extends FrameLayout {
        private Theme.ResourcesProvider resourcesProvider;
        private final AnimatedTextView.AnimatedTextDrawable subtitle;
        private final AnimatedTextView.AnimatedTextDrawable title;

        public ActionBar(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.title = animatedTextDrawable;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            animatedTextDrawable.setAnimationProperties(0.3f, 0L, 430L, cubicBezierInterpolator);
            animatedTextDrawable.setTypeface(AndroidUtilities.bold());
            animatedTextDrawable.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle, resourcesProvider));
            animatedTextDrawable.setTextSize(AndroidUtilities.dp(18.0f));
            animatedTextDrawable.setEllipsizeByGradient(!LocaleController.isRTL);
            animatedTextDrawable.setCallback(this);
            animatedTextDrawable.setOverrideFullWidth(AndroidUtilities.displaySize.x);
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.subtitle = animatedTextDrawable2;
            animatedTextDrawable2.setAnimationProperties(0.3f, 0L, 430L, cubicBezierInterpolator);
            animatedTextDrawable2.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle, resourcesProvider));
            animatedTextDrawable2.setTextSize(AndroidUtilities.dp(14.0f));
            animatedTextDrawable2.setEllipsizeByGradient(true ^ LocaleController.isRTL);
            animatedTextDrawable2.setCallback(this);
            animatedTextDrawable2.setOverrideFullWidth(AndroidUtilities.displaySize.x);
        }

        private void setBounds(Drawable drawable, float f) {
            int i = (int) f;
            drawable.setBounds(getPaddingLeft(), i - AndroidUtilities.dp(32.0f), getMeasuredWidth() - getPaddingRight(), i + AndroidUtilities.dp(32.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            setBounds(this.title, AndroidUtilities.lerp(AndroidUtilities.dp(29.0f), AndroidUtilities.dp(18.83f), this.subtitle.isNotEmpty()));
            this.title.draw(canvas);
            setBounds(this.subtitle, AndroidUtilities.dp(39.5f));
            this.subtitle.draw(canvas);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
            setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        }

        public void setSubtitle(CharSequence charSequence, boolean z) {
            this.subtitle.setText(charSequence, z && !LocaleController.isRTL);
        }

        public void setTitle(CharSequence charSequence, boolean z) {
            this.title.setText(charSequence, z && !LocaleController.isRTL);
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return this.title == drawable || this.subtitle == drawable || super.verifyDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Page extends FrameLayout {
        ActionBar actionBar;
        Adapter adapter;
        private int buttonsHeight;
        ToggleButton changePositionBtn;
        ToggleButton changeSizeBtn;
        FrameLayout changeSizeBtnContainer;
        GridLayoutManagerFixed chatLayoutManager;
        RecyclerListView chatListView;
        SizeNotifierFrameLayout chatPreviewContainer;
        int chatTopOffset;
        ActionBarMenuSubItem clearQuoteButton;
        public int currentTab;
        int currentTopOffset;
        float currentYOffset;
        ActionBarMenuSubItem deleteReplyButton;
        private boolean firstAttach;
        private boolean firstLayout;
        ChatListItemAnimator itemAnimator;
        int lastSize;
        ActionBarPopupWindow.ActionBarPopupWindowLayout menu;
        int menuBack;
        MessagePreviewParams.Messages messages;
        ActionBarMenuSubItem quoteAnotherChatButton;
        ActionBarMenuSubItem quoteButton;
        private AnimatorSet quoteSwitcher;
        android.graphics.Rect rect;
        ActionBarMenuSubItem replyAnotherChatButton;
        int scrollToOffset;
        ChatMessageSharedResources sharedResources;
        TextSelectionHelper.ChatListTextSelectionHelper textSelectionHelper;
        View textSelectionOverlay;
        boolean toQuote;
        boolean updateAfterAnimations;
        private boolean updateScroll;
        ToggleButton videoChangeSizeBtn;
        float yOffset;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 10 extends GridLayoutManagerFixed {
            final /* synthetic */ MessagePreviewView val$this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            10(Context context, int i, int i2, boolean z, MessagePreviewView messagePreviewView) {
                super(context, i, i2, z);
                this.val$this$0 = messagePreviewView;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onLayoutChildren$0() {
                Page.this.adapter.notifyDataSetChanged();
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            protected boolean hasSiblingChild(int i) {
                byte b;
                MessageObject messageObject = Page.this.messages.previewMessages.get(i);
                MessageObject.GroupedMessages validGroupedMessage = Page.this.getValidGroupedMessage(messageObject);
                if (validGroupedMessage != null) {
                    MessageObject.GroupedMessagePosition position = validGroupedMessage.getPosition(messageObject);
                    if (position.minX != position.maxX && (b = position.minY) == position.maxY && b != 0) {
                        int size = validGroupedMessage.posArray.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition = validGroupedMessage.posArray.get(i2);
                            if (groupedMessagePosition != position) {
                                byte b2 = groupedMessagePosition.minY;
                                byte b3 = position.minY;
                                if (b2 <= b3 && groupedMessagePosition.maxY >= b3) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    super.onLayoutChildren(recycler, state);
                    return;
                }
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    FileLog.e(e);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$10$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagePreviewView.Page.10.this.lambda$onLayoutChildren$0();
                        }
                    });
                }
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            public boolean shouldLayoutChildFromOpositeSide(View view) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 7 extends ChatListItemAnimator {
            Runnable finishRunnable;
            int scrollAnimationIndex;
            final /* synthetic */ MessagePreviewView val$this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            7(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider, MessagePreviewView messagePreviewView) {
                super(chatActivity, recyclerListView, resourcesProvider);
                this.val$this$0 = messagePreviewView;
                this.scrollAnimationIndex = -1;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$endAnimations$2() {
                if (this.scrollAnimationIndex != -1) {
                    NotificationCenter.getInstance(MessagePreviewView.this.currentAccount).onAnimationFinish(this.scrollAnimationIndex);
                    this.scrollAnimationIndex = -1;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onAllAnimationsDone$0() {
                if (this.scrollAnimationIndex != -1) {
                    NotificationCenter.getInstance(MessagePreviewView.this.currentAccount).onAnimationFinish(this.scrollAnimationIndex);
                    this.scrollAnimationIndex = -1;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onAllAnimationsDone$1() {
                Page.this.updateMessages();
            }

            @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
            public void endAnimations() {
                super.endAnimations();
                Runnable runnable = this.finishRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                }
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagePreviewView.Page.7.this.lambda$endAnimations$2();
                    }
                };
                this.finishRunnable = runnable2;
                AndroidUtilities.runOnUIThread(runnable2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator
            public void onAllAnimationsDone() {
                super.onAllAnimationsDone();
                Runnable runnable = this.finishRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                }
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagePreviewView.Page.7.this.lambda$onAllAnimationsDone$0();
                    }
                };
                this.finishRunnable = runnable2;
                AndroidUtilities.runOnUIThread(runnable2);
                Page page = Page.this;
                if (page.updateAfterAnimations) {
                    page.updateAfterAnimations = false;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagePreviewView.Page.7.this.lambda$onAllAnimationsDone$1();
                        }
                    });
                }
            }

            @Override // androidx.recyclerview.widget.ChatListItemAnimator
            public void onAnimationStart() {
                super.onAnimationStart();
                AndroidUtilities.cancelRunOnUIThread(MessagePreviewView.this.changeBoundsRunnable);
                MessagePreviewView.this.changeBoundsRunnable.run();
                if (this.scrollAnimationIndex == -1) {
                    this.scrollAnimationIndex = NotificationCenter.getInstance(MessagePreviewView.this.currentAccount).setAnimationInProgress(this.scrollAnimationIndex, null, false);
                }
                Runnable runnable = this.finishRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.finishRunnable = null;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class Adapter extends RecyclerView.Adapter {
            private Adapter() {
            }

            private int offset(ChatMessageCell chatMessageCell, int i) {
                MessageObject messageObject;
                int i2;
                ArrayList<MessageObject.TextLayoutBlock> arrayList;
                CharSequence charSequence;
                MessageObject.TextLayoutBlocks textLayoutBlocks;
                if (chatMessageCell == null || (messageObject = chatMessageCell.getMessageObject()) == null || messageObject.getGroupId() != 0) {
                    return 0;
                }
                if (TextUtils.isEmpty(messageObject.caption) || (textLayoutBlocks = chatMessageCell.captionLayout) == null) {
                    chatMessageCell.layoutTextXY(true);
                    i2 = chatMessageCell.textY;
                    CharSequence charSequence2 = messageObject.messageText;
                    ArrayList<MessageObject.TextLayoutBlock> arrayList2 = messageObject.textLayoutBlocks;
                    if (chatMessageCell.linkPreviewAbove) {
                        i2 += chatMessageCell.linkPreviewHeight + AndroidUtilities.dp(10.0f);
                    }
                    arrayList = arrayList2;
                    charSequence = charSequence2;
                } else {
                    i2 = (int) chatMessageCell.captionY;
                    charSequence = messageObject.caption;
                    arrayList = textLayoutBlocks.textLayoutBlocks;
                }
                if (arrayList != null && charSequence != null) {
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        MessageObject.TextLayoutBlock textLayoutBlock = arrayList.get(i3);
                        StaticLayout staticLayout = textLayoutBlock.textLayout;
                        String charSequence3 = staticLayout.getText().toString();
                        int i4 = textLayoutBlock.charactersOffset;
                        if (i > i4) {
                            return (int) (i - i4 > charSequence3.length() - 1 ? i2 + ((int) (textLayoutBlock.textYOffset(arrayList, chatMessageCell.transitionParams) + textLayoutBlock.padTop + textLayoutBlock.height)) : staticLayout.getLineTop(staticLayout.getLineForOffset(i - textLayoutBlock.charactersOffset)) + i2 + textLayoutBlock.textYOffset(arrayList, chatMessageCell.transitionParams) + textLayoutBlock.padTop);
                        }
                    }
                }
                return 0;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                MessagePreviewParams.Messages messages = Page.this.messages;
                if (messages == null) {
                    return 0;
                }
                return messages.previewMessages.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                MessagePreviewParams.Messages messages = Page.this.messages;
                if (messages == null) {
                    return;
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) viewHolder.itemView;
                chatMessageCell.setInvalidateSpoilersParent(messages.hasSpoilers);
                chatMessageCell.setParentViewSize(Page.this.chatListView.getMeasuredWidth(), Page.this.chatListView.getMeasuredHeight());
                int id = chatMessageCell.getMessageObject() != null ? chatMessageCell.getMessageObject().getId() : 0;
                Page page = Page.this;
                if (page.currentTab == 2) {
                    MessagePreviewView.this.messagePreviewParams.checkCurrentLink(page.messages.previewMessages.get(i));
                }
                MessagePreviewParams.Messages messages2 = Page.this.messages;
                chatMessageCell.setMessageObject(Page.this.messages.previewMessages.get(i), messages2.groupedMessagesMap.get(messages2.previewMessages.get(i).getGroupId()), true, true);
                if (Page.this.currentTab == 1) {
                    chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.Adapter.3
                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean canDrawOutboundsContent() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean canPerformActions() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canPerformActions(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell2, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell2, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i2, float f, float f2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i2, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressAboutRevenueSharingAds() {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAboutRevenueSharingAds(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBoostCounter(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i2, float f, float f2, boolean z) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i2, f, f2, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, boolean z) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell2, tLRPC$Chat, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressCodeCopy(ChatMessageCell chatMessageCell2, MessageObject.TextLayoutBlock textLayoutBlock) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCodeCopy(this, chatMessageCell2, textLayoutBlock);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressDialogButton(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressDialogButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressEffect(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, tLRPC$KeyboardButton);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheck(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell2, int i2, int i3) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheckWhat(this, chatMessageCell2, i2, i3);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell2, ImageReceiver imageReceiver, TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGroupImage(this, chatMessageCell2, imageReceiver, tLRPC$MessageExtendedMedia, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHiddenForward(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell2, int i2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHint(this, chatMessageCell2, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell2, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressImage(this, chatMessageCell2, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell2, int i2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressInstantButton(this, chatMessageCell2, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell2, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell2, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, tLRPC$ReactionCount, z, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressRevealSensitiveContent(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell2, float f, float f2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredInfo(this, chatMessageCell2, f, f2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTime(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressTopicButton(ChatMessageCell chatMessageCell2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTopicButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressUrl(ChatMessageCell chatMessageCell2, CharacterStyle characterStyle, boolean z) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUrl(this, chatMessageCell2, characterStyle, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2, boolean z) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, TLRPC$Document tLRPC$Document) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell2, tLRPC$User, tLRPC$Document);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell2, String str) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBot(this, chatMessageCell2, str);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell2, long j) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBotNotInline(this, chatMessageCell2, j);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell2, ArrayList arrayList, int i2, int i3, int i4) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressVoteButtons(this, chatMessageCell2, arrayList, i2, i3, i4);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell2, TLRPC$WebPage tLRPC$WebPage, String str, boolean z) {
                            Browser.openUrl(chatMessageCell2.getContext(), str);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean doNotShowLoadingReply(MessageObject messageObject) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$doNotShowLoadingReply(this, messageObject);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void forceUpdate(ChatMessageCell chatMessageCell2, boolean z) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$forceUpdate(this, chatMessageCell2, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ String getAdminRank(long j) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getAdminRank(this, j);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getPinchToZoomHelper(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingLink(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getTextSelectionHelper(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean hasSelectedMessages() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$hasSelectedMessages(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void invalidateBlur() {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$invalidateBlur(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean isLandscape() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isLandscape(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean isProgressLoading(ChatMessageCell chatMessageCell2, int i2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isProgressLoading(this, chatMessageCell2, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean isReplyOrSelf() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isReplyOrSelf(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean keyboardIsOpened() {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$keyboardIsOpened(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i2, int i3) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needOpenWebView(this, messageObject, str, str2, str3, str4, i2, i3);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean needPlayMessage(ChatMessageCell chatMessageCell2, MessageObject messageObject, boolean z) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, chatMessageCell2, messageObject, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void needReloadPolls() {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needReloadPolls(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void needShowPremiumBulletin(int i2) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needShowPremiumBulletin(this, i2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean onAccessibilityAction(int i2, Bundle bundle) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onAccessibilityAction(this, i2, bundle);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void onDiceFinished() {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onDiceFinished(this);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$setShouldNotRepeatSticker(this, messageObject);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2, boolean z) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2, z);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean shouldShowDialogButton(ChatMessageCell chatMessageCell2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowDialogButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ boolean shouldShowTopicButton(ChatMessageCell chatMessageCell2) {
                            return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this, chatMessageCell2);
                        }

                        @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                        public /* synthetic */ void videoTimerReached() {
                            ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                        }
                    });
                }
                if (Page.this.messages.previewMessages.size() > 1) {
                    chatMessageCell.setCheckBoxVisible(Page.this.currentTab == 1, false);
                    boolean z = id == Page.this.messages.previewMessages.get(i).getId();
                    MessagePreviewParams.Messages messages3 = Page.this.messages;
                    boolean z2 = messages3.selectedIds.get(messages3.previewMessages.get(i).getId(), false);
                    chatMessageCell.setChecked(z2, z2, z);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                Context context = viewGroup.getContext();
                int i2 = MessagePreviewView.this.currentAccount;
                Page page = Page.this;
                ChatMessageCell chatMessageCell = new ChatMessageCell(context, i2, false, page.sharedResources, MessagePreviewView.this.resourcesProvider) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.Adapter.1
                    @Override // org.telegram.ui.Cells.ChatMessageCell, org.telegram.ui.Cells.BaseCell, android.view.View
                    public void invalidate() {
                        super.invalidate();
                        Page.this.chatListView.invalidate();
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.View
                    public void invalidate(int i3, int i4, int i5, int i6) {
                        super.invalidate(i3, i4, i5, i6);
                        Page.this.chatListView.invalidate();
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.ViewGroup, android.view.View
                    public void onLayout(boolean z, int i3, int i4, int i5, int i6) {
                        super.onLayout(z, i3, i4, i5, i6);
                        Page.this.updateLinkHighlight(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell
                    public void setMessageObject(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, boolean z, boolean z2) {
                        super.setMessageObject(messageObject, groupedMessages, z, z2);
                        Page.this.updateLinkHighlight(this);
                    }
                };
                chatMessageCell.setClipChildren(false);
                chatMessageCell.setClipToPadding(false);
                chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.Adapter.2
                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean canPerformActions() {
                        Page page2 = Page.this;
                        if (page2.currentTab == 2) {
                            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                            if (!messagePreviewParams.singleLink && !messagePreviewParams.isSecret) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i3, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i3, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressAboutRevenueSharingAds() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAboutRevenueSharingAds(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBoostCounter(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i3, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i3, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell2, tLRPC$Chat, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCodeCopy(ChatMessageCell chatMessageCell2, MessageObject.TextLayoutBlock textLayoutBlock) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCodeCopy(this, chatMessageCell2, textLayoutBlock);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressDialogButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressDialogButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressEffect(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheck(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell2, int i3, int i4) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheckWhat(this, chatMessageCell2, i3, i4);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell2, ImageReceiver imageReceiver, TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGroupImage(this, chatMessageCell2, imageReceiver, tLRPC$MessageExtendedMedia, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHiddenForward(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHint(this, chatMessageCell2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressImage(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressInstantButton(this, chatMessageCell2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, tLRPC$ReactionCount, z, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressRevealSensitiveContent(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredInfo(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTime(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTopicButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTopicButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public void didPressUrl(ChatMessageCell chatMessageCell2, CharacterStyle characterStyle, boolean z) {
                        Page page2 = Page.this;
                        if (page2.currentTab != 2 || MessagePreviewView.this.messagePreviewParams.currentLink == characterStyle || chatMessageCell2.getMessageObject() == null || !(characterStyle instanceof URLSpan)) {
                            return;
                        }
                        String url = ((URLSpan) characterStyle).getURL();
                        MessagePreviewView messagePreviewView = MessagePreviewView.this;
                        MessagePreviewParams messagePreviewParams = messagePreviewView.messagePreviewParams;
                        messagePreviewParams.currentLink = characterStyle;
                        messagePreviewParams.webpage = null;
                        ChatActivity chatActivity = messagePreviewView.chatActivity;
                        if (chatActivity != null && url != null) {
                            chatActivity.searchLinks(url, true);
                        }
                        Page.this.updateLinkHighlight(chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, TLRPC$Document tLRPC$Document) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell2, tLRPC$User, tLRPC$Document);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell2, String str) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBot(this, chatMessageCell2, str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell2, long j) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBotNotInline(this, chatMessageCell2, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell2, ArrayList arrayList, int i3, int i4, int i5) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressVoteButtons(this, chatMessageCell2, arrayList, i3, i4, i5);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell2, TLRPC$WebPage tLRPC$WebPage, String str, boolean z) {
                        Browser.openUrl(chatMessageCell2.getContext(), str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean doNotShowLoadingReply(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$doNotShowLoadingReply(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void forceUpdate(ChatMessageCell chatMessageCell2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$forceUpdate(this, chatMessageCell2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getAdminRank(long j) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getAdminRank(this, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getPinchToZoomHelper(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell2) {
                        Page page2 = Page.this;
                        if (page2.currentTab == 2) {
                            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                            if (messagePreviewParams.singleLink) {
                                return null;
                            }
                            return messagePreviewParams.currentLink;
                        }
                        return null;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                        return Page.this.textSelectionHelper;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean hasSelectedMessages() {
                        return true;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void invalidateBlur() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$invalidateBlur(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isLandscape() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isLandscape(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean isProgressLoading(ChatMessageCell chatMessageCell2, int i3) {
                        Page page2 = Page.this;
                        if (page2.currentTab == 2 && i3 == 1) {
                            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                            if (messagePreviewParams.singleLink) {
                                return false;
                            }
                            TLRPC$WebPage tLRPC$WebPage = messagePreviewParams.webpage;
                            return tLRPC$WebPage == null || (tLRPC$WebPage instanceof TLRPC$TL_webPagePending);
                        }
                        return false;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isReplyOrSelf() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isReplyOrSelf(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean keyboardIsOpened() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$keyboardIsOpened(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i3, int i4) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needOpenWebView(this, messageObject, str, str2, str3, str4, i3, i4);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean needPlayMessage(ChatMessageCell chatMessageCell2, MessageObject messageObject, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, chatMessageCell2, messageObject, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needReloadPolls() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needReloadPolls(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needShowPremiumBulletin(int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needShowPremiumBulletin(this, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean onAccessibilityAction(int i3, Bundle bundle) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onAccessibilityAction(this, i3, bundle);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void onDiceFinished() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onDiceFinished(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$setShouldNotRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowDialogButton(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowDialogButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowTopicButton(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void videoTimerReached() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                    }
                });
                return new RecyclerListView.Holder(chatMessageCell);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
                int i;
                Page page = Page.this;
                if (page.messages == null || (i = page.currentTab) == 1) {
                    return;
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) viewHolder.itemView;
                if (i != 0) {
                    chatMessageCell.setDrawSelectionBackground(false);
                    return;
                }
                MessageObject.GroupedMessages validGroupedMessage = page.getValidGroupedMessage(chatMessageCell.getMessageObject());
                chatMessageCell.setDrawSelectionBackground(validGroupedMessage == null);
                chatMessageCell.setChecked(true, validGroupedMessage == null, false);
                Page page2 = Page.this;
                MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                if (messagePreviewParams.isSecret || messagePreviewParams.quote == null || !page2.isReplyMessageCell(chatMessageCell) || Page.this.textSelectionHelper.isInSelectionMode()) {
                    return;
                }
                Page page3 = Page.this;
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = page3.textSelectionHelper;
                MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                chatListTextSelectionHelper.select(chatMessageCell, messagePreviewParams2.quoteStart, messagePreviewParams2.quoteEnd);
                if (Page.this.firstAttach) {
                    Page page4 = Page.this;
                    page4.scrollToOffset = offset(chatMessageCell, MessagePreviewView.this.messagePreviewParams.quoteStart);
                    Page.this.firstAttach = false;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:96:0x0710  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x0724  */
        /* JADX WARN: Type inference failed for: r0v171, types: [org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout] */
        /* JADX WARN: Type inference failed for: r7v10, types: [android.view.View] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Page(Context context, int i) {
            super(context);
            final ToggleButton toggleButton;
            MessagePreviewParams.Messages messages;
            View view;
            MessagePreviewParams messagePreviewParams;
            MessagePreviewParams.Messages messages2;
            LinearLayout linearLayout;
            this.firstLayout = true;
            this.scrollToOffset = -1;
            this.rect = new android.graphics.Rect();
            this.updateScroll = false;
            this.firstAttach = true;
            this.sharedResources = new ChatMessageSharedResources(context);
            this.currentTab = i;
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda0
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$new$0;
                    lambda$new$0 = MessagePreviewView.Page.this.lambda$new$0(view2, motionEvent);
                    return lambda$new$0;
                }
            });
            SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.2
                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getY() < Page.this.currentTopOffset) {
                        return false;
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
                public Drawable getNewDrawable() {
                    Drawable wallpaperDrawable = MessagePreviewView.this.resourcesProvider.getWallpaperDrawable();
                    return wallpaperDrawable != null ? wallpaperDrawable : super.getNewDrawable();
                }
            };
            this.chatPreviewContainer = sizeNotifierFrameLayout;
            sizeNotifierFrameLayout.setBackgroundImage(MessagePreviewView.this.resourcesProvider.getWallpaperDrawable(), MessagePreviewView.this.resourcesProvider.isWallpaperMotion());
            this.chatPreviewContainer.setOccupyStatusBar(false);
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 21) {
                this.chatPreviewContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.3
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view2, Outline outline) {
                        outline.setRoundRect(0, Page.this.currentTopOffset + 1, view2.getMeasuredWidth(), view2.getMeasuredHeight(), AndroidUtilities.dp(8.0f));
                    }
                });
                this.chatPreviewContainer.setClipToOutline(true);
                this.chatPreviewContainer.setElevation(AndroidUtilities.dp(4.0f));
            }
            ActionBar actionBar = new ActionBar(context, MessagePreviewView.this.resourcesProvider);
            this.actionBar = actionBar;
            actionBar.setBackgroundColor(MessagePreviewView.this.getThemedColor(Theme.key_actionBarDefault));
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = new TextSelectionHelper.ChatListTextSelectionHelper() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.4
                {
                    this.resourcesProvider = MessagePreviewView.this.resourcesProvider;
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                protected boolean canCopy() {
                    MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                    return messagePreviewParams2 == null || !messagePreviewParams2.noforwards;
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                protected boolean canShowQuote() {
                    Page page = Page.this;
                    return page.currentTab == 0 && !MessagePreviewView.this.messagePreviewParams.isSecret;
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                protected Theme.ResourcesProvider getResourcesProvider() {
                    return this.resourcesProvider;
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper.ChatListTextSelectionHelper, org.telegram.ui.Cells.TextSelectionHelper
                public void invalidate() {
                    super.invalidate();
                    RecyclerListView recyclerListView = Page.this.chatListView;
                    if (recyclerListView != null) {
                        recyclerListView.invalidate();
                    }
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                public boolean isSelected(MessageObject messageObject) {
                    Page page = Page.this;
                    return page.currentTab == 0 && !MessagePreviewView.this.messagePreviewParams.isSecret && isInSelectionMode();
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                protected void onQuoteClick(MessageObject messageObject, int i3, int i4, CharSequence charSequence) {
                    ChatActivity.ReplyQuote replyQuote;
                    MessageObject messageObject2;
                    Page page = Page.this;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = page.textSelectionHelper;
                    if (chatListTextSelectionHelper2.selectionEnd - chatListTextSelectionHelper2.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                        Page.this.showQuoteLengthError();
                        return;
                    }
                    Page page2 = Page.this;
                    MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = page2.textSelectionHelper;
                    messagePreviewParams2.quoteStart = chatListTextSelectionHelper3.selectionStart;
                    messagePreviewParams2.quoteEnd = chatListTextSelectionHelper3.selectionEnd;
                    MessageObject replyMessage = page2.getReplyMessage(messageObject);
                    if (replyMessage != null && ((replyQuote = MessagePreviewView.this.messagePreviewParams.quote) == null || (messageObject2 = replyQuote.message) == null || messageObject2.getId() != replyMessage.getId())) {
                        MessagePreviewView.this.messagePreviewParams.quote = ChatActivity.ReplyQuote.from(replyMessage, i3, i4);
                    }
                    MessagePreviewView.this.onQuoteSelectedPart();
                    MessagePreviewView.this.dismiss(true);
                }
            };
            this.textSelectionHelper = chatListTextSelectionHelper;
            chatListTextSelectionHelper.setCallback(new TextSelectionHelper.Callback() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.5
                @Override // org.telegram.ui.Cells.TextSelectionHelper.Callback
                public void onStateChanged(boolean z) {
                    Page page = Page.this;
                    if (MessagePreviewView.this.showing) {
                        if (!z && page.menu.getSwipeBack().isForegroundOpen()) {
                            Page.this.menu.getSwipeBack().closeForeground(true);
                        } else if (z) {
                            Page page2 = Page.this;
                            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = page2.textSelectionHelper;
                            if (chatListTextSelectionHelper2.selectionEnd - chatListTextSelectionHelper2.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                                Page.this.showQuoteLengthError();
                                return;
                            }
                            MessageObject replyMessage = Page.this.getReplyMessage(Page.this.textSelectionHelper.getSelectedCell() != null ? ((ChatMessageCell) Page.this.textSelectionHelper.getSelectedCell()).getMessageObject() : null);
                            Page page3 = Page.this;
                            MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                            if (messagePreviewParams2.quote == null) {
                                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = page3.textSelectionHelper;
                                int i3 = chatListTextSelectionHelper3.selectionStart;
                                messagePreviewParams2.quoteStart = i3;
                                int i4 = chatListTextSelectionHelper3.selectionEnd;
                                messagePreviewParams2.quoteEnd = i4;
                                messagePreviewParams2.quote = ChatActivity.ReplyQuote.from(replyMessage, i3, i4);
                                Page.this.menu.getSwipeBack().openForeground(Page.this.menuBack);
                            }
                        }
                    }
                }
            });
            RecyclerListView recyclerListView = new RecyclerListView(context, MessagePreviewView.this.resourcesProvider) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.6
                /* JADX WARN: Type inference failed for: r3v0 */
                /* JADX WARN: Type inference failed for: r3v1, types: [int, boolean] */
                /* JADX WARN: Type inference failed for: r3v9 */
                private void drawChatBackgroundElements(Canvas canvas) {
                    boolean z;
                    int i3;
                    MessageObject.GroupedMessages currentMessagesGroup;
                    ChatMessageCell chatMessageCell;
                    MessageObject.GroupedMessages currentMessagesGroup2;
                    int childCount = getChildCount();
                    ?? r3 = 0;
                    MessageObject.GroupedMessages groupedMessages = null;
                    for (int i4 = 0; i4 < childCount; i4++) {
                        View childAt = getChildAt(i4);
                        if ((childAt instanceof ChatMessageCell) && ((currentMessagesGroup2 = (chatMessageCell = (ChatMessageCell) childAt).getCurrentMessagesGroup()) == null || currentMessagesGroup2 != groupedMessages)) {
                            chatMessageCell.getCurrentPosition();
                            chatMessageCell.getBackgroundDrawable();
                            groupedMessages = currentMessagesGroup2;
                        }
                    }
                    int i5 = 0;
                    while (i5 < 3) {
                        MessagePreviewView.this.drawingGroups.clear();
                        if (i5 != 2 || Page.this.chatListView.isFastScrollAnimationRunning()) {
                            int i6 = 0;
                            while (true) {
                                z = true;
                                if (i6 >= childCount) {
                                    break;
                                }
                                View childAt2 = Page.this.chatListView.getChildAt(i6);
                                if (childAt2 instanceof ChatMessageCell) {
                                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt2;
                                    if (childAt2.getY() <= Page.this.chatListView.getHeight() && childAt2.getY() + childAt2.getHeight() >= 0.0f && (currentMessagesGroup = chatMessageCell2.getCurrentMessagesGroup()) != null && ((i5 != 0 || currentMessagesGroup.messages.size() != 1) && ((i5 != 1 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i5 != 0 || !chatMessageCell2.getMessageObject().deleted) && ((i5 != 1 || chatMessageCell2.getMessageObject().deleted) && ((i5 != 2 || chatMessageCell2.willRemovedAfterAnimation()) && (i5 == 2 || !chatMessageCell2.willRemovedAfterAnimation()))))))) {
                                        if (!MessagePreviewView.this.drawingGroups.contains(currentMessagesGroup)) {
                                            MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                                            transitionParams.left = r3;
                                            transitionParams.top = r3;
                                            transitionParams.right = r3;
                                            transitionParams.bottom = r3;
                                            transitionParams.pinnedBotton = r3;
                                            transitionParams.pinnedTop = r3;
                                            transitionParams.cell = chatMessageCell2;
                                            MessagePreviewView.this.drawingGroups.add(currentMessagesGroup);
                                        }
                                        currentMessagesGroup.transitionParams.pinnedTop = chatMessageCell2.isPinnedTop();
                                        currentMessagesGroup.transitionParams.pinnedBotton = chatMessageCell2.isPinnedBottom();
                                        int left = chatMessageCell2.getLeft() + chatMessageCell2.getBackgroundDrawableLeft();
                                        int left2 = chatMessageCell2.getLeft() + chatMessageCell2.getBackgroundDrawableRight();
                                        int top = chatMessageCell2.getTop() + chatMessageCell2.getBackgroundDrawableTop();
                                        int top2 = chatMessageCell2.getTop() + chatMessageCell2.getBackgroundDrawableBottom();
                                        if ((chatMessageCell2.getCurrentPosition().flags & 4) == 0) {
                                            top -= AndroidUtilities.dp(10.0f);
                                        }
                                        if ((chatMessageCell2.getCurrentPosition().flags & 8) == 0) {
                                            top2 += AndroidUtilities.dp(10.0f);
                                        }
                                        if (chatMessageCell2.willRemovedAfterAnimation()) {
                                            currentMessagesGroup.transitionParams.cell = chatMessageCell2;
                                        }
                                        MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                        int i7 = transitionParams2.top;
                                        if (i7 == 0 || top < i7) {
                                            transitionParams2.top = top;
                                        }
                                        int i8 = transitionParams2.bottom;
                                        if (i8 == 0 || top2 > i8) {
                                            transitionParams2.bottom = top2;
                                        }
                                        int i9 = transitionParams2.left;
                                        if (i9 == 0 || left < i9) {
                                            transitionParams2.left = left;
                                        }
                                        int i10 = transitionParams2.right;
                                        if (i10 == 0 || left2 > i10) {
                                            transitionParams2.right = left2;
                                        }
                                    }
                                }
                                i6++;
                            }
                            int i11 = 0;
                            while (i11 < MessagePreviewView.this.drawingGroups.size()) {
                                MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) MessagePreviewView.this.drawingGroups.get(i11);
                                if (groupedMessages2 == null) {
                                    i3 = i5;
                                } else {
                                    float nonAnimationTranslationX = groupedMessages2.transitionParams.cell.getNonAnimationTranslationX(z);
                                    MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages2.transitionParams;
                                    float f = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                                    float f2 = transitionParams3.top + transitionParams3.offsetTop;
                                    float f3 = transitionParams3.right + nonAnimationTranslationX + transitionParams3.offsetRight;
                                    float f4 = transitionParams3.bottom + transitionParams3.offsetBottom;
                                    if (!transitionParams3.backgroundChangeBounds) {
                                        f2 += transitionParams3.cell.getTranslationY();
                                        f4 += groupedMessages2.transitionParams.cell.getTranslationY();
                                    }
                                    if (f2 < (-AndroidUtilities.dp(20.0f))) {
                                        f2 = -AndroidUtilities.dp(20.0f);
                                    }
                                    if (f4 > Page.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) {
                                        f4 = Page.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f);
                                    }
                                    boolean z2 = (groupedMessages2.transitionParams.cell.getScaleX() == 1.0f && groupedMessages2.transitionParams.cell.getScaleY() == 1.0f) ? false : true;
                                    if (z2) {
                                        canvas.save();
                                        canvas.scale(groupedMessages2.transitionParams.cell.getScaleX(), groupedMessages2.transitionParams.cell.getScaleY(), f + ((f3 - f) / 2.0f), f2 + ((f4 - f2) / 2.0f));
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages2.transitionParams;
                                    i3 = i5;
                                    transitionParams4.cell.drawBackground(canvas, (int) f, (int) f2, (int) f3, (int) f4, transitionParams4.pinnedTop, transitionParams4.pinnedBotton, false, 0);
                                    MessageObject.GroupedMessages.TransitionParams transitionParams5 = groupedMessages2.transitionParams;
                                    transitionParams5.cell = null;
                                    transitionParams5.drawCaptionLayout = groupedMessages2.hasCaption;
                                    if (z2) {
                                        canvas.restore();
                                        for (int i12 = 0; i12 < childCount; i12++) {
                                            View childAt3 = Page.this.chatListView.getChildAt(i12);
                                            if (childAt3 instanceof ChatMessageCell) {
                                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt3;
                                                if (chatMessageCell3.getCurrentMessagesGroup() == groupedMessages2) {
                                                    int left3 = chatMessageCell3.getLeft();
                                                    int top3 = chatMessageCell3.getTop();
                                                    childAt3.setPivotX((f - left3) + ((f3 - f) / 2.0f));
                                                    childAt3.setPivotY((f2 - top3) + ((f4 - f2) / 2.0f));
                                                }
                                            }
                                        }
                                    }
                                }
                                i11++;
                                i5 = i3;
                                z = true;
                            }
                        }
                        i5++;
                        r3 = 0;
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                public void dispatchDraw(Canvas canvas) {
                    for (int i3 = 0; i3 < getChildCount(); i3++) {
                        View childAt = getChildAt(i3);
                        if (childAt instanceof ChatMessageCell) {
                            ((ChatMessageCell) childAt).setParentViewSize(Page.this.chatPreviewContainer.getMeasuredWidth(), Page.this.chatPreviewContainer.getBackgroundSizeY());
                        }
                    }
                    drawChatBackgroundElements(canvas);
                    super.dispatchDraw(canvas);
                }

                @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view2, long j) {
                    if (view2 instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                        boolean drawChild = super.drawChild(canvas, view2, j);
                        chatMessageCell.drawCheckBox(canvas);
                        canvas.save();
                        canvas.translate(chatMessageCell.getX(), chatMessageCell.getY());
                        canvas.save();
                        canvas.scale(chatMessageCell.getScaleX(), chatMessageCell.getScaleY(), chatMessageCell.getPivotX(), chatMessageCell.getPivotY());
                        chatMessageCell.drawContent(canvas, true);
                        chatMessageCell.layoutTextXY(true);
                        chatMessageCell.drawMessageText(canvas);
                        if (chatMessageCell.getCurrentMessagesGroup() == null || ((chatMessageCell.getCurrentPosition() != null && (((chatMessageCell.getCurrentPosition().flags & chatMessageCell.captionFlag()) != 0 && (chatMessageCell.getCurrentPosition().flags & 1) != 0) || (chatMessageCell.getCurrentMessagesGroup() != null && chatMessageCell.getCurrentMessagesGroup().isDocuments))) || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner)) {
                            chatMessageCell.drawCaptionLayout(canvas, false, chatMessageCell.getAlpha());
                            chatMessageCell.drawReactionsLayout(canvas, chatMessageCell.getAlpha(), null);
                            chatMessageCell.drawCommentLayout(canvas, chatMessageCell.getAlpha());
                        }
                        if (chatMessageCell.getCurrentMessagesGroup() != null || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                            chatMessageCell.drawNamesLayout(canvas, chatMessageCell.getAlpha());
                        }
                        if ((chatMessageCell.getCurrentPosition() != null && chatMessageCell.getCurrentPosition().last) || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                            chatMessageCell.drawTime(canvas, chatMessageCell.getAlpha(), true);
                        }
                        chatMessageCell.drawOverlays(canvas);
                        canvas.restore();
                        chatMessageCell.getTransitionParams().recordDrawingStatePreview();
                        canvas.restore();
                        return drawChild;
                    }
                    return true;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                public void onLayout(boolean z, int i3, int i4, int i5, int i6) {
                    if (Page.this.firstLayout) {
                        if (Page.this.currentTab != 0) {
                            scrollToPosition(0);
                        }
                        Page.this.firstLayout = false;
                    }
                    super.onLayout(z, i3, i4, i5, i6);
                    Page.this.updatePositions();
                    Page.this.checkScroll();
                }

                @Override // androidx.recyclerview.widget.RecyclerView
                public void onScrollStateChanged(int i3) {
                    if (i3 == 0) {
                        Page.this.textSelectionHelper.stopScrolling();
                    }
                    super.onScrollStateChanged(i3);
                }

                @Override // androidx.recyclerview.widget.RecyclerView
                public void onScrolled(int i3, int i4) {
                    super.onScrolled(i3, i4);
                    Page.this.textSelectionHelper.onParentScrolled();
                }
            };
            this.chatListView = recyclerListView;
            7 r3 = new 7(null, this.chatListView, MessagePreviewView.this.resourcesProvider, MessagePreviewView.this);
            this.itemAnimator = r3;
            recyclerListView.setItemAnimator(r3);
            this.chatListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.8
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                    super.onScrolled(recyclerView, i3, i4);
                    for (int i5 = 0; i5 < Page.this.chatListView.getChildCount(); i5++) {
                        ((ChatMessageCell) Page.this.chatListView.getChildAt(i5)).setParentViewSize(Page.this.chatPreviewContainer.getMeasuredWidth(), Page.this.chatPreviewContainer.getBackgroundSizeY());
                    }
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = Page.this.textSelectionHelper;
                    if (chatListTextSelectionHelper2 != null) {
                        chatListTextSelectionHelper2.invalidate();
                    }
                }
            });
            this.chatListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.9
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public void onItemClick(View view2, int i3) {
                    Page page = Page.this;
                    if (page.currentTab != 1 || page.messages.previewMessages.size() <= 1) {
                        return;
                    }
                    int id = Page.this.messages.previewMessages.get(i3).getId();
                    boolean z = !Page.this.messages.selectedIds.get(id, false);
                    if (Page.this.messages.selectedIds.size() != 1 || z) {
                        SparseBooleanArray sparseBooleanArray = Page.this.messages.selectedIds;
                        if (z) {
                            sparseBooleanArray.put(id, z);
                        } else {
                            sparseBooleanArray.delete(id);
                        }
                        ((ChatMessageCell) view2).setChecked(z, z, true);
                        Page.this.updateSubtitle(true);
                    }
                }
            });
            RecyclerListView recyclerListView2 = this.chatListView;
            Adapter adapter = new Adapter();
            this.adapter = adapter;
            recyclerListView2.setAdapter(adapter);
            this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            10 r13 = new 10(context, 1000, 1, true, MessagePreviewView.this);
            this.chatLayoutManager = r13;
            r13.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.11
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i3) {
                    if (i3 < 0 || i3 >= Page.this.messages.previewMessages.size()) {
                        return 1000;
                    }
                    MessageObject messageObject = Page.this.messages.previewMessages.get(i3);
                    MessageObject.GroupedMessages validGroupedMessage = Page.this.getValidGroupedMessage(messageObject);
                    if (validGroupedMessage != null) {
                        return validGroupedMessage.getPosition(messageObject).spanSize;
                    }
                    return 1000;
                }
            });
            this.chatListView.setClipToPadding(false);
            this.chatListView.setLayoutManager(this.chatLayoutManager);
            this.chatListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.12
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(android.graphics.Rect rect, View view2, RecyclerView recyclerView, RecyclerView.State state) {
                    ChatMessageCell chatMessageCell;
                    MessageObject.GroupedMessages currentMessagesGroup;
                    MessageObject.GroupedMessagePosition currentPosition;
                    int i3 = 0;
                    rect.bottom = 0;
                    if (!(view2 instanceof ChatMessageCell) || (currentMessagesGroup = (chatMessageCell = (ChatMessageCell) view2).getCurrentMessagesGroup()) == null || (currentPosition = chatMessageCell.getCurrentPosition()) == null || currentPosition.siblingHeights == null) {
                        return;
                    }
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    float max = Math.max(point.x, point.y) * 0.5f;
                    int extraInsetHeight = chatMessageCell.getExtraInsetHeight();
                    int i4 = 0;
                    while (true) {
                        float[] fArr = currentPosition.siblingHeights;
                        if (i4 >= fArr.length) {
                            break;
                        }
                        extraInsetHeight += (int) Math.ceil(fArr[i4] * max);
                        i4++;
                    }
                    int round = extraInsetHeight + ((currentPosition.maxY - currentPosition.minY) * Math.round(AndroidUtilities.density * 7.0f));
                    int size = currentMessagesGroup.posArray.size();
                    while (true) {
                        if (i3 < size) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition = currentMessagesGroup.posArray.get(i3);
                            byte b = groupedMessagePosition.minY;
                            byte b2 = currentPosition.minY;
                            if (b == b2 && ((groupedMessagePosition.minX != currentPosition.minX || groupedMessagePosition.maxX != currentPosition.maxX || b != b2 || groupedMessagePosition.maxY != currentPosition.maxY) && b == b2)) {
                                round -= ((int) Math.ceil(max * groupedMessagePosition.ph)) - AndroidUtilities.dp(4.0f);
                                break;
                            }
                            i3++;
                        } else {
                            break;
                        }
                    }
                    rect.bottom = -round;
                }
            });
            this.chatPreviewContainer.addView(this.chatListView);
            addView(this.chatPreviewContainer, LayoutHelper.createFrame(-1, 400.0f, 0, 8.0f, 0.0f, 8.0f, 0.0f));
            this.chatPreviewContainer.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getContext(), R.drawable.popup_fixed_alert2, MessagePreviewView.this.resourcesProvider, 1);
            this.menu = actionBarPopupWindowLayout;
            actionBarPopupWindowLayout.getSwipeBack().setOnForegroundOpenFinished(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    MessagePreviewView.Page.this.lambda$new$1();
                }
            });
            addView(this.menu, LayoutHelper.createFrame(-2, -2.0f));
            if (i != 0 || (messages2 = (messagePreviewParams = MessagePreviewView.this.messagePreviewParams).replyMessage) == null) {
                if (i == 1) {
                    MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                    if (messagePreviewParams2.forwardMessages != null) {
                        final ToggleButton toggleButton2 = new ToggleButton(context, R.raw.name_hide, LocaleController.getString(messagePreviewParams2.multipleUsers ? R.string.ShowSenderNames : R.string.ShowSendersName), R.raw.name_show, LocaleController.getString(MessagePreviewView.this.messagePreviewParams.multipleUsers ? R.string.HideSenderNames : R.string.HideSendersName), MessagePreviewView.this.resourcesProvider);
                        this.menu.addView((View) toggleButton2, LayoutHelper.createLinear(-1, 48));
                        if (MessagePreviewView.this.messagePreviewParams.hasCaption) {
                            toggleButton = new ToggleButton(context, R.raw.caption_hide, LocaleController.getString(R.string.ShowCaption), R.raw.caption_show, LocaleController.getString(R.string.HideCaption), MessagePreviewView.this.resourcesProvider);
                            toggleButton.setState(MessagePreviewView.this.messagePreviewParams.hideCaption, false);
                            this.menu.addView((View) toggleButton, LayoutHelper.createLinear(-1, 48));
                        } else {
                            toggleButton = null;
                        }
                        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(context, true, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda3
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$9(view2);
                            }
                        });
                        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.ChangeRecipient), R.drawable.msg_forward_replace);
                        this.menu.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
                        ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                        gapView.setTag(R.id.fit_width_tag, 1);
                        this.menu.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
                        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                        actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$10(view2);
                            }
                        });
                        this.menu.addView((View) actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
                        ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString(R.string.DoNotForward), R.drawable.msg_delete);
                        int themedColor = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                        int i3 = Theme.key_text_RedRegular;
                        actionBarMenuSubItem3.setColors(themedColor, MessagePreviewView.this.getThemedColor(i3));
                        actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$11(view2);
                            }
                        });
                        actionBarMenuSubItem3.setSelectorColor(Theme.multAlpha(Theme.getColor(i3), 0.12f));
                        this.menu.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
                        toggleButton2.setState(MessagePreviewView.this.messagePreviewParams.hideForwardSendersName, false);
                        toggleButton2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda6
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$12(toggleButton, toggleButton2, view2);
                            }
                        });
                        if (toggleButton != null) {
                            toggleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda7
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view2) {
                                    MessagePreviewView.Page.this.lambda$new$13(toggleButton, toggleButton2, view2);
                                }
                            });
                        }
                    }
                }
                if (i == 2 && MessagePreviewView.this.messagePreviewParams.linkMessage != null) {
                    ToggleButton toggleButton3 = new ToggleButton(context, R.raw.position_below, LocaleController.getString(R.string.LinkAbove), R.raw.position_above, LocaleController.getString(R.string.LinkBelow), MessagePreviewView.this.resourcesProvider);
                    this.changePositionBtn = toggleButton3;
                    toggleButton3.setState(!MessagePreviewView.this.messagePreviewParams.webpageTop, false);
                    this.menu.addView((View) this.changePositionBtn, LayoutHelper.createLinear(-1, 48));
                    FrameLayout frameLayout = new FrameLayout(context);
                    this.changeSizeBtnContainer = frameLayout;
                    frameLayout.setBackground(Theme.createRadSelectorDrawable(MessagePreviewView.this.getThemedColor(Theme.key_dialogButtonSelector), 0, 0));
                    int i4 = R.raw.media_shrink;
                    String string = LocaleController.getString(R.string.LinkMediaLarger);
                    int i5 = R.raw.media_enlarge;
                    ToggleButton toggleButton4 = new ToggleButton(context, i4, string, i5, LocaleController.getString(R.string.LinkMediaSmaller), MessagePreviewView.this.resourcesProvider);
                    this.changeSizeBtn = toggleButton4;
                    toggleButton4.setBackground(null);
                    this.changeSizeBtn.setVisibility(MessagePreviewView.this.messagePreviewParams.isVideo ? 4 : 0);
                    this.changeSizeBtnContainer.addView(this.changeSizeBtn, LayoutHelper.createLinear(-1, 48));
                    ToggleButton toggleButton5 = new ToggleButton(context, i4, LocaleController.getString(R.string.LinkVideoLarger), i5, LocaleController.getString(R.string.LinkVideoSmaller), MessagePreviewView.this.resourcesProvider);
                    this.videoChangeSizeBtn = toggleButton5;
                    toggleButton5.setBackground(null);
                    this.videoChangeSizeBtn.setVisibility(MessagePreviewView.this.messagePreviewParams.isVideo ? 0 : 4);
                    this.changeSizeBtnContainer.setAlpha(MessagePreviewView.this.messagePreviewParams.hasMedia ? 1.0f : 0.5f);
                    this.changeSizeBtnContainer.addView(this.videoChangeSizeBtn, LayoutHelper.createLinear(-1, 48));
                    this.menu.addView((View) this.changeSizeBtnContainer, LayoutHelper.createLinear(-1, 48));
                    FrameLayout frameLayout2 = this.changeSizeBtnContainer;
                    MessagePreviewParams messagePreviewParams3 = MessagePreviewView.this.messagePreviewParams;
                    frameLayout2.setVisibility((!messagePreviewParams3.singleLink || messagePreviewParams3.hasMedia) ? 0 : 8);
                    this.changeSizeBtn.setState(MessagePreviewView.this.messagePreviewParams.webpageSmall, false);
                    this.videoChangeSizeBtn.setState(MessagePreviewView.this.messagePreviewParams.webpageSmall, false);
                    ActionBarPopupWindow.GapView gapView2 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                    gapView2.setTag(R.id.fit_width_tag, 1);
                    this.menu.addView((View) gapView2, LayoutHelper.createLinear(-1, 8));
                    ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                    actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$14(view2);
                        }
                    });
                    this.menu.addView((View) actionBarMenuSubItem4, LayoutHelper.createLinear(-1, 48));
                    ActionBarMenuSubItem actionBarMenuSubItem5 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    actionBarMenuSubItem5.setTextAndIcon(LocaleController.getString(R.string.DoNotLinkPreview), R.drawable.msg_delete);
                    int themedColor2 = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                    int i6 = Theme.key_text_RedRegular;
                    actionBarMenuSubItem5.setColors(themedColor2, MessagePreviewView.this.getThemedColor(i6));
                    actionBarMenuSubItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda9
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$15(view2);
                        }
                    });
                    actionBarMenuSubItem5.setSelectorColor(Theme.multAlpha(Theme.getColor(i6), 0.12f));
                    this.menu.addView((View) actionBarMenuSubItem5, LayoutHelper.createLinear(-1, 48));
                    this.changeSizeBtnContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda10
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$16(view2);
                        }
                    });
                    this.changePositionBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda11
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$17(view2);
                        }
                    });
                }
            } else {
                if (messages2.hasText && !messagePreviewParams.isSecret) {
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    if (MessagePreviewView.this.showOutdatedQuote) {
                        linearLayout = linearLayout2;
                    } else {
                        linearLayout = linearLayout2;
                        ActionBarMenuSubItem actionBarMenuSubItem6 = new ActionBarMenuSubItem(context, false, true, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem6.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
                        actionBarMenuSubItem6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda13
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$2(view2);
                            }
                        });
                        linearLayout.addView(actionBarMenuSubItem6, LayoutHelper.createLinear(-1, 48));
                        ActionBarPopupWindow.GapView gapView3 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                        gapView3.setTag(R.id.fit_width_tag, 1);
                        linearLayout.addView(gapView3, LayoutHelper.createLinear(-1, 8));
                        ActionBarMenuSubItem actionBarMenuSubItem7 = new ActionBarMenuSubItem(context, false, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem7.setTextAndIcon(LocaleController.getString(R.string.QuoteSelectedPart), R.drawable.menu_quote_specific);
                        actionBarMenuSubItem7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda14
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$3(view2);
                            }
                        });
                        linearLayout.addView(actionBarMenuSubItem7, LayoutHelper.createLinear(-1, 48));
                    }
                    this.menuBack = this.menu.addViewToSwipeBack(linearLayout);
                    this.menu.getSwipeBack().setStickToRight(true);
                    FrameLayout frameLayout3 = new FrameLayout(context);
                    ActionBarMenuSubItem actionBarMenuSubItem8 = new ActionBarMenuSubItem(context, true, true, false, MessagePreviewView.this.resourcesProvider) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.13
                        @Override // android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (getVisibility() != 0 || getAlpha() < 0.5f) {
                                return false;
                            }
                            return super.onTouchEvent(motionEvent);
                        }

                        @Override // org.telegram.ui.ActionBar.ActionBarMenuSubItem
                        public void updateBackground() {
                            setBackground(null);
                        }
                    };
                    this.quoteButton = actionBarMenuSubItem8;
                    actionBarMenuSubItem8.setTextAndIcon(LocaleController.getString(MessagePreviewView.this.showOutdatedQuote ? R.string.QuoteSelectedPart : R.string.SelectSpecificQuote), R.drawable.menu_select_quote);
                    ActionBarMenuSubItem actionBarMenuSubItem9 = new ActionBarMenuSubItem(context, true, true, false, MessagePreviewView.this.resourcesProvider) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.14
                        @Override // android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (getVisibility() != 0 || getAlpha() < 0.5f) {
                                return false;
                            }
                            return super.onTouchEvent(motionEvent);
                        }

                        @Override // org.telegram.ui.ActionBar.ActionBarMenuSubItem
                        public void updateBackground() {
                            setBackground(null);
                        }
                    };
                    this.clearQuoteButton = actionBarMenuSubItem9;
                    actionBarMenuSubItem9.setTextAndIcon(LocaleController.getString(R.string.ClearQuote), R.drawable.menu_quote_delete);
                    frameLayout3.setBackground(Theme.createRadSelectorDrawable(MessagePreviewView.this.getThemedColor(Theme.key_dialogButtonSelector), 6, 0));
                    frameLayout3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda15
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$4(view2);
                        }
                    });
                    frameLayout3.addView(this.quoteButton, LayoutHelper.createFrame(-1, 48.0f));
                    frameLayout3.addView(this.clearQuoteButton, LayoutHelper.createFrame(-1, 48.0f));
                    this.menu.addView((View) frameLayout3, LayoutHelper.createLinear(-1, 48));
                }
                MessagePreviewParams messagePreviewParams4 = MessagePreviewView.this.messagePreviewParams;
                if (!messagePreviewParams4.noforwards && !messagePreviewParams4.hasSecretMessages) {
                    FrameLayout frameLayout4 = new FrameLayout(context);
                    ActionBarMenuSubItem actionBarMenuSubItem10 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    this.replyAnotherChatButton = actionBarMenuSubItem10;
                    String string2 = LocaleController.getString(R.string.ReplyToAnotherChat);
                    int i7 = R.drawable.msg_forward_replace;
                    actionBarMenuSubItem10.setTextAndIcon(string2, i7);
                    this.replyAnotherChatButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda16
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$5(view2);
                        }
                    });
                    ActionBarMenuSubItem actionBarMenuSubItem11 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    this.quoteAnotherChatButton = actionBarMenuSubItem11;
                    actionBarMenuSubItem11.setTextAndIcon(LocaleController.getString(R.string.QuoteToAnotherChat), i7);
                    this.quoteAnotherChatButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda17
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$6(view2);
                        }
                    });
                    frameLayout4.addView(this.quoteAnotherChatButton, LayoutHelper.createFrame(-1, 48.0f));
                    frameLayout4.addView(this.replyAnotherChatButton, LayoutHelper.createFrame(-1, 48.0f));
                    this.menu.addView((View) frameLayout4, LayoutHelper.createLinear(-1, 48));
                }
                MessagePreviewParams messagePreviewParams5 = MessagePreviewView.this.messagePreviewParams;
                if (!messagePreviewParams5.noforwards && !messagePreviewParams5.hasSecretMessages) {
                    ActionBarPopupWindow.GapView gapView4 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                    gapView4.setTag(R.id.fit_width_tag, 1);
                    this.menu.addView((View) gapView4, LayoutHelper.createLinear(-1, 8));
                }
                switchToQuote(MessagePreviewView.this.messagePreviewParams.quote != null, false);
                ActionBarMenuSubItem actionBarMenuSubItem12 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                actionBarMenuSubItem12.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                actionBarMenuSubItem12.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MessagePreviewView.Page.this.lambda$new$7(view2);
                    }
                });
                this.menu.addView((View) actionBarMenuSubItem12, LayoutHelper.createLinear(-1, 48));
                ActionBarMenuSubItem actionBarMenuSubItem13 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                this.deleteReplyButton = actionBarMenuSubItem13;
                actionBarMenuSubItem13.setTextAndIcon(LocaleController.getString(MessagePreviewView.this.showOutdatedQuote ? R.string.DoNotQuote : R.string.DoNotReply), R.drawable.msg_delete);
                ActionBarMenuSubItem actionBarMenuSubItem14 = this.deleteReplyButton;
                int themedColor3 = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                int i8 = Theme.key_text_RedRegular;
                actionBarMenuSubItem14.setColors(themedColor3, MessagePreviewView.this.getThemedColor(i8));
                this.deleteReplyButton.setSelectorColor(Theme.multAlpha(Theme.getColor(i8), 0.12f));
                this.deleteReplyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MessagePreviewView.Page.this.lambda$new$8(view2);
                    }
                });
                this.menu.addView((View) this.deleteReplyButton, LayoutHelper.createLinear(-1, 48));
            }
            int i9 = this.currentTab;
            if (i9 == 1) {
                messages = MessagePreviewView.this.messagePreviewParams.forwardMessages;
            } else if (i9 != 0) {
                if (i9 == 2) {
                    messages = MessagePreviewView.this.messagePreviewParams.linkMessage;
                }
                TextSelectionHelper.TextSelectionOverlay overlayView = this.textSelectionHelper.getOverlayView(context);
                this.textSelectionOverlay = overlayView;
                if (i2 >= 21) {
                    overlayView.setElevation(AndroidUtilities.dp(8.0f));
                    this.textSelectionOverlay.setOutlineProvider(null);
                }
                view = this.textSelectionOverlay;
                if (view != null) {
                    if (view.getParent() instanceof ViewGroup) {
                        ((ViewGroup) this.textSelectionOverlay.getParent()).removeView(this.textSelectionOverlay);
                    }
                    addView(this.textSelectionOverlay, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, org.telegram.ui.ActionBar.ActionBar.getCurrentActionBarHeight() / AndroidUtilities.density, 0.0f, 0.0f));
                }
                this.textSelectionHelper.setParentView(this.chatListView);
            } else {
                messages = MessagePreviewView.this.messagePreviewParams.replyMessage;
            }
            this.messages = messages;
            TextSelectionHelper.TextSelectionOverlay overlayView2 = this.textSelectionHelper.getOverlayView(context);
            this.textSelectionOverlay = overlayView2;
            if (i2 >= 21) {
            }
            view = this.textSelectionOverlay;
            if (view != null) {
            }
            this.textSelectionHelper.setParentView(this.chatListView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkScroll() {
            if (this.updateScroll) {
                if (this.chatListView.computeVerticalScrollRange() > this.chatListView.computeVerticalScrollExtent()) {
                    postDelayed(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagePreviewView.Page.this.lambda$checkScroll$18();
                        }
                    }, 0L);
                }
                this.updateScroll = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MessageObject.GroupedMessages getValidGroupedMessage(MessageObject messageObject) {
            if (messageObject.getGroupId() != 0) {
                MessageObject.GroupedMessages groupedMessages = this.messages.groupedMessagesMap.get(messageObject.getGroupId());
                if (groupedMessages == null || (groupedMessages.messages.size() > 1 && groupedMessages.getPosition(messageObject) != null)) {
                    return groupedMessages;
                }
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkScroll$18() {
            RecyclerListView recyclerListView;
            int computeVerticalScrollRange;
            if (MessagePreviewView.this.messagePreviewParams.webpageTop) {
                recyclerListView = this.chatListView;
                computeVerticalScrollRange = -recyclerListView.computeVerticalScrollOffset();
            } else {
                recyclerListView = this.chatListView;
                computeVerticalScrollRange = recyclerListView.computeVerticalScrollRange() - (this.chatListView.computeVerticalScrollOffset() + this.chatListView.computeVerticalScrollExtent());
            }
            recyclerListView.smoothScrollBy(0, computeVerticalScrollRange, NotificationCenter.playerDidStartPlaying, ChatListItemAnimator.DEFAULT_INTERPOLATOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                MessagePreviewView.this.dismiss(true);
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            switchToQuote(true, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$10(View view) {
            MessagePreviewView.this.dismiss(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$11(View view) {
            MessagePreviewView.this.removeForward();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$12(ToggleButton toggleButton, ToggleButton toggleButton2, View view) {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            MessagePreviewParams messagePreviewParams = messagePreviewView.messagePreviewParams;
            boolean z = !messagePreviewParams.hideForwardSendersName;
            messagePreviewParams.hideForwardSendersName = z;
            messagePreviewView.returnSendersNames = false;
            if (!z) {
                messagePreviewParams.hideCaption = false;
                if (toggleButton != null) {
                    toggleButton.setState(false, true);
                }
            }
            toggleButton2.setState(MessagePreviewView.this.messagePreviewParams.hideForwardSendersName, true);
            updateMessages();
            updateSubtitle(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$13(ToggleButton toggleButton, ToggleButton toggleButton2, View view) {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            MessagePreviewParams messagePreviewParams = messagePreviewView.messagePreviewParams;
            boolean z = !messagePreviewParams.hideCaption;
            messagePreviewParams.hideCaption = z;
            if (!z) {
                if (messagePreviewView.returnSendersNames) {
                    messagePreviewParams.hideForwardSendersName = false;
                }
                messagePreviewView.returnSendersNames = false;
            } else if (!messagePreviewParams.hideForwardSendersName) {
                messagePreviewParams.hideForwardSendersName = true;
                messagePreviewView.returnSendersNames = true;
            }
            toggleButton.setState(z, true);
            toggleButton2.setState(MessagePreviewView.this.messagePreviewParams.hideForwardSendersName, true);
            updateMessages();
            updateSubtitle(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$14(View view) {
            MessagePreviewView.this.dismiss(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$15(View view) {
            MessagePreviewView.this.removeLink();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$16(View view) {
            TLRPC$Message tLRPC$Message;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Message tLRPC$Message2;
            TLRPC$MessageMedia tLRPC$MessageMedia2;
            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
            if (messagePreviewParams.hasMedia) {
                boolean z = !messagePreviewParams.webpageSmall;
                messagePreviewParams.webpageSmall = z;
                this.changeSizeBtn.setState(z, true);
                this.videoChangeSizeBtn.setState(MessagePreviewView.this.messagePreviewParams.webpageSmall, true);
                if (this.messages.messages.size() > 0 && (tLRPC$Message2 = this.messages.messages.get(0).messageOwner) != null && (tLRPC$MessageMedia2 = tLRPC$Message2.media) != null) {
                    boolean z2 = MessagePreviewView.this.messagePreviewParams.webpageSmall;
                    tLRPC$MessageMedia2.force_small_media = z2;
                    tLRPC$MessageMedia2.force_large_media = !z2;
                }
                if (this.messages.previewMessages.size() > 0 && (tLRPC$Message = this.messages.previewMessages.get(0).messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null) {
                    boolean z3 = MessagePreviewView.this.messagePreviewParams.webpageSmall;
                    tLRPC$MessageMedia.force_small_media = z3;
                    tLRPC$MessageMedia.force_large_media = !z3;
                }
                updateMessages();
                this.updateScroll = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$17(View view) {
            TLRPC$Message tLRPC$Message;
            TLRPC$Message tLRPC$Message2;
            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
            boolean z = messagePreviewParams.webpageTop;
            messagePreviewParams.webpageTop = !z;
            this.changePositionBtn.setState(z, true);
            if (this.messages.messages.size() > 0 && (tLRPC$Message2 = this.messages.messages.get(0).messageOwner) != null) {
                tLRPC$Message2.invert_media = MessagePreviewView.this.messagePreviewParams.webpageTop;
            }
            if (this.messages.previewMessages.size() > 0 && (tLRPC$Message = this.messages.previewMessages.get(0).messageOwner) != null) {
                tLRPC$Message.invert_media = MessagePreviewView.this.messagePreviewParams.webpageTop;
            }
            updateMessages();
            this.updateScroll = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(View view) {
            MessagePreviewView.this.messagePreviewParams.quote = null;
            this.textSelectionHelper.clear();
            switchToQuote(false, false);
            this.menu.getSwipeBack().closeForeground();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            if (getReplyMessage() != null) {
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.textSelectionHelper;
                if (chatListTextSelectionHelper.selectionEnd - chatListTextSelectionHelper.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                    showQuoteLengthError();
                    return;
                }
                MessageObject replyMessage = getReplyMessage(this.textSelectionHelper.getSelectedCell() != null ? ((ChatMessageCell) this.textSelectionHelper.getSelectedCell()).getMessageObject() : null);
                MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = this.textSelectionHelper;
                int i = chatListTextSelectionHelper2.selectionStart;
                messagePreviewParams.quoteStart = i;
                int i2 = chatListTextSelectionHelper2.selectionEnd;
                messagePreviewParams.quoteEnd = i2;
                messagePreviewParams.quote = ChatActivity.ReplyQuote.from(replyMessage, i, i2);
                MessagePreviewView.this.onQuoteSelectedPart();
                MessagePreviewView.this.dismiss(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(View view) {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            MessagePreviewParams messagePreviewParams = messagePreviewView.messagePreviewParams;
            if (messagePreviewParams.quote != null && !messagePreviewView.showOutdatedQuote) {
                messagePreviewParams.quote = null;
                this.textSelectionHelper.clear();
                switchToQuote(false, true);
                updateSubtitle(true);
                return;
            }
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.textSelectionHelper;
            if (chatListTextSelectionHelper.selectionEnd - chatListTextSelectionHelper.selectionStart > MessagesController.getInstance(messagePreviewView.currentAccount).quoteLengthMax) {
                showQuoteLengthError();
                return;
            }
            MessageObject replyMessage = getReplyMessage();
            if (replyMessage != null) {
                if (this.textSelectionHelper.isInSelectionMode()) {
                    MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = this.textSelectionHelper;
                    messagePreviewParams2.quoteStart = chatListTextSelectionHelper2.selectionStart;
                    messagePreviewParams2.quoteEnd = chatListTextSelectionHelper2.selectionEnd;
                    MessageObject replyMessage2 = getReplyMessage(chatListTextSelectionHelper2.getSelectedCell() != null ? ((ChatMessageCell) this.textSelectionHelper.getSelectedCell()).getMessageObject() : null);
                    MessagePreviewParams messagePreviewParams3 = MessagePreviewView.this.messagePreviewParams;
                    messagePreviewParams3.quote = ChatActivity.ReplyQuote.from(replyMessage2, messagePreviewParams3.quoteStart, messagePreviewParams3.quoteEnd);
                    MessagePreviewView.this.onQuoteSelectedPart();
                    MessagePreviewView.this.dismiss(true);
                    return;
                }
                MessagePreviewView messagePreviewView2 = MessagePreviewView.this;
                MessagePreviewParams messagePreviewParams4 = messagePreviewView2.messagePreviewParams;
                messagePreviewParams4.quoteStart = 0;
                messagePreviewParams4.quoteEnd = Math.min(MessagesController.getInstance(messagePreviewView2.currentAccount).quoteLengthMax, replyMessage.messageOwner.message.length());
                MessagePreviewParams messagePreviewParams5 = MessagePreviewView.this.messagePreviewParams;
                messagePreviewParams5.quote = ChatActivity.ReplyQuote.from(replyMessage, messagePreviewParams5.quoteStart, messagePreviewParams5.quoteEnd);
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = this.textSelectionHelper;
                ChatMessageCell replyMessageCell = getReplyMessageCell();
                MessagePreviewParams messagePreviewParams6 = MessagePreviewView.this.messagePreviewParams;
                chatListTextSelectionHelper3.select(replyMessageCell, messagePreviewParams6.quoteStart, messagePreviewParams6.quoteEnd);
                if (!MessagePreviewView.this.showOutdatedQuote) {
                    this.menu.getSwipeBack().openForeground(this.menuBack);
                }
                switchToQuote(true, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(View view) {
            MessagePreviewView.this.selectAnotherChat(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(View view) {
            MessagePreviewView.this.selectAnotherChat(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$7(View view) {
            MessagePreviewView.this.dismiss(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$8(View view) {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            if (messagePreviewView.showOutdatedQuote) {
                messagePreviewView.removeQuote();
            } else {
                messagePreviewView.removeReply();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$9(View view) {
            MessagePreviewView.this.selectAnotherChat(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAttachedToWindow$19(View view) {
            this.adapter.onViewAttachedToWindow(this.chatListView.getChildViewHolder(view));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updatePositions$20(int i, float f, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f2 = 1.0f - floatValue;
            int i2 = (int) ((i * f2) + (this.chatTopOffset * floatValue));
            this.currentTopOffset = i2;
            float f3 = (f * f2) + (this.yOffset * floatValue);
            this.currentYOffset = f3;
            setOffset(f3, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setOffset(float f, int i) {
            if (MessagePreviewView.this.isLandscapeMode) {
                this.actionBar.setTranslationY(0.0f);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.chatPreviewContainer.invalidateOutline();
                }
                this.chatPreviewContainer.setTranslationY(0.0f);
                this.menu.setTranslationY(0.0f);
            } else {
                this.actionBar.setTranslationY(i);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.chatPreviewContainer.invalidateOutline();
                }
                this.chatPreviewContainer.setTranslationY(f);
                this.menu.setTranslationY((f + this.chatPreviewContainer.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
            }
            this.textSelectionOverlay.setTranslationX(this.chatPreviewContainer.getX());
            this.textSelectionOverlay.setTranslationY(this.chatPreviewContainer.getY());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showQuoteLengthError() {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            BulletinFactory.of(messagePreviewView, messagePreviewView.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.QuoteMaxError), LocaleController.getString(R.string.QuoteMaxErrorMessage)).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void switchToQuote(final boolean z, boolean z2) {
            if (MessagePreviewView.this.showOutdatedQuote) {
                z = false;
            }
            if (z2 && this.toQuote == z) {
                return;
            }
            this.toQuote = z;
            AnimatorSet animatorSet = this.quoteSwitcher;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.quoteSwitcher = null;
            }
            if (!z2) {
                ActionBarMenuSubItem actionBarMenuSubItem = this.quoteButton;
                if (actionBarMenuSubItem != null) {
                    actionBarMenuSubItem.setAlpha(!z ? 1.0f : 0.0f);
                    this.quoteButton.setVisibility(!z ? 0 : 4);
                }
                ActionBarMenuSubItem actionBarMenuSubItem2 = this.clearQuoteButton;
                if (actionBarMenuSubItem2 != null) {
                    actionBarMenuSubItem2.setAlpha(z ? 1.0f : 0.0f);
                    this.clearQuoteButton.setVisibility(z ? 0 : 4);
                }
                ActionBarMenuSubItem actionBarMenuSubItem3 = this.replyAnotherChatButton;
                if (actionBarMenuSubItem3 != null) {
                    actionBarMenuSubItem3.setAlpha(!z ? 1.0f : 0.0f);
                    this.replyAnotherChatButton.setVisibility(!z ? 0 : 4);
                }
                ActionBarMenuSubItem actionBarMenuSubItem4 = this.quoteAnotherChatButton;
                if (actionBarMenuSubItem4 != null) {
                    actionBarMenuSubItem4.setAlpha(z ? 1.0f : 0.0f);
                    this.quoteAnotherChatButton.setVisibility(z ? 0 : 4);
                    return;
                }
                return;
            }
            this.quoteSwitcher = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            ActionBarMenuSubItem actionBarMenuSubItem5 = this.quoteButton;
            if (actionBarMenuSubItem5 != null) {
                actionBarMenuSubItem5.setVisibility(0);
                arrayList.add(ObjectAnimator.ofFloat(this.quoteButton, View.ALPHA, !z ? 1.0f : 0.0f));
            }
            ActionBarMenuSubItem actionBarMenuSubItem6 = this.clearQuoteButton;
            if (actionBarMenuSubItem6 != null) {
                actionBarMenuSubItem6.setVisibility(0);
                arrayList.add(ObjectAnimator.ofFloat(this.clearQuoteButton, View.ALPHA, z ? 1.0f : 0.0f));
            }
            ActionBarMenuSubItem actionBarMenuSubItem7 = this.replyAnotherChatButton;
            if (actionBarMenuSubItem7 != null) {
                actionBarMenuSubItem7.setVisibility(0);
                arrayList.add(ObjectAnimator.ofFloat(this.replyAnotherChatButton, View.ALPHA, !z ? 1.0f : 0.0f));
            }
            ActionBarMenuSubItem actionBarMenuSubItem8 = this.quoteAnotherChatButton;
            if (actionBarMenuSubItem8 != null) {
                actionBarMenuSubItem8.setVisibility(0);
                arrayList.add(ObjectAnimator.ofFloat(this.quoteAnotherChatButton, View.ALPHA, z ? 1.0f : 0.0f));
            }
            this.quoteSwitcher.playTogether(arrayList);
            this.quoteSwitcher.setDuration(360L);
            this.quoteSwitcher.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.quoteSwitcher.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Page.this.quoteSwitcher = null;
                    Page.this.switchToQuote(z, false);
                }
            });
            this.quoteSwitcher.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateLinkHighlight(ChatMessageCell chatMessageCell) {
            CharacterStyle characterStyle;
            TLRPC$WebPage tLRPC$WebPage;
            if (this.currentTab == 2) {
                MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                if (!messagePreviewParams.singleLink && (characterStyle = messagePreviewParams.currentLink) != null && (tLRPC$WebPage = messagePreviewParams.webpage) != null && !(tLRPC$WebPage instanceof TLRPC$TL_webPagePending)) {
                    chatMessageCell.setHighlightedSpan(characterStyle);
                    return;
                }
            }
            chatMessageCell.setHighlightedSpan(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMessages() {
            TLRPC$Message tLRPC$Message;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            if (this.itemAnimator.isRunning()) {
                this.updateAfterAnimations = true;
                return;
            }
            for (int i = 0; i < this.messages.previewMessages.size(); i++) {
                MessageObject messageObject = this.messages.previewMessages.get(i);
                messageObject.forceUpdate = true;
                MessagePreviewView messagePreviewView = MessagePreviewView.this;
                messageObject.sendAsPeer = messagePreviewView.sendAsPeer;
                MessagePreviewParams messagePreviewParams = messagePreviewView.messagePreviewParams;
                if (messagePreviewParams.hideForwardSendersName) {
                    messageObject.messageOwner.flags &= -5;
                    messageObject.hideSendersName = true;
                } else {
                    messageObject.messageOwner.flags |= 4;
                    messageObject.hideSendersName = false;
                }
                if (this.currentTab == 2) {
                    TLRPC$WebPage tLRPC$WebPage = messagePreviewParams.webpage;
                    if (tLRPC$WebPage != null && ((tLRPC$MessageMedia = (tLRPC$Message = messageObject.messageOwner).media) == null || tLRPC$MessageMedia.webpage != tLRPC$WebPage)) {
                        tLRPC$Message.flags |= LiteMode.FLAG_CALLS_ANIMATIONS;
                        tLRPC$Message.media = new TLRPC$TL_messageMediaWebPage();
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
                        MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                        tLRPC$MessageMedia2.webpage = messagePreviewParams2.webpage;
                        boolean z = messagePreviewParams2.webpageSmall;
                        tLRPC$MessageMedia2.force_large_media = !z;
                        tLRPC$MessageMedia2.force_small_media = z;
                        tLRPC$MessageMedia2.manual = true;
                        messageObject.linkDescription = null;
                        messageObject.generateLinkDescription();
                        messageObject.photoThumbs = null;
                        messageObject.photoThumbs2 = null;
                        messageObject.photoThumbsObject = null;
                        messageObject.photoThumbsObject2 = null;
                        messageObject.generateThumbs(true);
                        messageObject.checkMediaExistance();
                    } else if (tLRPC$WebPage == null) {
                        TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                        tLRPC$Message2.flags &= -513;
                        tLRPC$Message2.media = null;
                    }
                }
                if (MessagePreviewView.this.messagePreviewParams.hideCaption) {
                    messageObject.caption = null;
                } else {
                    messageObject.generateCaption();
                }
                if (messageObject.isPoll()) {
                    MessagePreviewParams.PreviewMediaPoll previewMediaPoll = (MessagePreviewParams.PreviewMediaPoll) messageObject.messageOwner.media;
                    previewMediaPoll.results.total_voters = MessagePreviewView.this.messagePreviewParams.hideCaption ? 0 : previewMediaPoll.totalVotersCached;
                }
            }
            for (int i2 = 0; i2 < this.messages.pollChosenAnswers.size(); i2++) {
                this.messages.pollChosenAnswers.get(i2).chosen = !MessagePreviewView.this.messagePreviewParams.hideForwardSendersName;
            }
            for (int i3 = 0; i3 < this.messages.groupedMessagesMap.size(); i3++) {
                this.itemAnimator.groupWillChanged(this.messages.groupedMessagesMap.valueAt(i3));
            }
            this.adapter.notifyItemRangeChanged(0, this.messages.previewMessages.size());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePositions() {
            final int i = this.chatTopOffset;
            final float f = this.yOffset;
            if (MessagePreviewView.this.isLandscapeMode) {
                this.yOffset = 0.0f;
                this.chatTopOffset = 0;
                this.menu.setTranslationX(this.chatListView.getMeasuredWidth() + AndroidUtilities.dp(8.0f));
            } else {
                int measuredHeight = this.chatListView.getMeasuredHeight();
                int i2 = 0;
                for (int i3 = 0; i3 < this.chatListView.getChildCount(); i3++) {
                    View childAt = this.chatListView.getChildAt(i3);
                    if (this.chatListView.getChildAdapterPosition(childAt) != -1) {
                        measuredHeight = Math.min(measuredHeight, childAt.getTop());
                        i2++;
                    }
                }
                MessagePreviewParams.Messages messages = this.messages;
                if (messages == null || i2 == 0 || i2 > messages.previewMessages.size()) {
                    this.chatTopOffset = 0;
                } else {
                    int max = Math.max(0, measuredHeight - AndroidUtilities.dp(4.0f));
                    this.chatTopOffset = max;
                    this.chatTopOffset = Math.min((max + (this.chatListView.getMeasuredHeight() - this.chatTopOffset)) - ((int) (((AndroidUtilities.displaySize.y * 0.8f) - this.buttonsHeight) - AndroidUtilities.dp(8.0f))), this.chatTopOffset);
                }
                float dp = (AndroidUtilities.dp(8.0f) + (((getMeasuredHeight() - AndroidUtilities.dp(16.0f)) - ((this.buttonsHeight - AndroidUtilities.dp(8.0f)) + (this.chatPreviewContainer.getMeasuredHeight() - this.chatTopOffset))) / 2.0f)) - this.chatTopOffset;
                this.yOffset = dp;
                if (dp > AndroidUtilities.dp(8.0f)) {
                    this.yOffset = AndroidUtilities.dp(8.0f);
                }
                this.menu.setTranslationX(getMeasuredWidth() - this.menu.getMeasuredWidth());
            }
            boolean z = this.firstLayout;
            if (z || (this.chatTopOffset == i && this.yOffset == f)) {
                if (z) {
                    float f2 = this.yOffset;
                    this.currentYOffset = f2;
                    int i4 = this.chatTopOffset;
                    this.currentTopOffset = i4;
                    setOffset(f2, i4);
                    return;
                }
                return;
            }
            ValueAnimator valueAnimator = MessagePreviewView.this.offsetsAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            MessagePreviewView.this.offsetsAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            MessagePreviewView.this.offsetsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda18
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    MessagePreviewView.Page.this.lambda$updatePositions$20(i, f, valueAnimator2);
                }
            });
            MessagePreviewView.this.offsetsAnimator.setDuration(250L);
            MessagePreviewView.this.offsetsAnimator.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
            MessagePreviewView.this.offsetsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MessagePreviewView.Page.15
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Page page = Page.this;
                    MessagePreviewView.this.offsetsAnimator = null;
                    page.setOffset(page.yOffset, page.chatTopOffset);
                }
            });
            AndroidUtilities.runOnUIThread(MessagePreviewView.this.changeBoundsRunnable, 50L);
            this.currentTopOffset = i;
            this.currentYOffset = f;
            setOffset(f, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0057, code lost:
            if (r6.this$0.currentChat.megagroup == false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x0080, code lost:
            if (r6.this$0.currentChat.megagroup == false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00ac, code lost:
            if (r6.this$0.currentChat.megagroup == false) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00ae, code lost:
            r0 = org.telegram.messenger.R.string.ForwardPreviewSendersNameVisibleChannel;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00df, code lost:
            if (r6.this$0.currentChat.megagroup == false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x00e1, code lost:
            r0 = org.telegram.messenger.R.string.ForwardPreviewSendersNameHiddenChannel;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateSubtitle(boolean z) {
            ActionBar actionBar;
            int i;
            String str;
            String formatString;
            int i2;
            int i3 = this.currentTab;
            if (i3 != 1) {
                if (i3 == 0) {
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    if (messagePreviewParams.quote == null || !messagePreviewParams.replyMessage.hasText) {
                        this.actionBar.setTitle(LocaleController.getString(R.string.MessageOptionsReplyTitle), z);
                        actionBar = this.actionBar;
                        if (!MessagePreviewView.this.messagePreviewParams.replyMessage.hasText) {
                            str = "";
                            actionBar.setSubtitle(str, z);
                            return;
                        }
                        i = R.string.MessageOptionsReplySubtitle;
                    } else {
                        this.actionBar.setTitle(LocaleController.getString(R.string.PreviewQuoteUpdate), z);
                        actionBar = this.actionBar;
                        i = R.string.PreviewQuoteUpdateSubtitle;
                    }
                } else if (i3 != 2) {
                    return;
                } else {
                    this.actionBar.setTitle(LocaleController.getString(R.string.MessageOptionsLinkTitle), z);
                    actionBar = this.actionBar;
                    i = R.string.MessageOptionsLinkSubtitle;
                }
                str = LocaleController.getString(i);
                actionBar.setSubtitle(str, z);
                return;
            }
            ActionBar actionBar2 = this.actionBar;
            MessagePreviewParams.Messages messages = MessagePreviewView.this.messagePreviewParams.forwardMessages;
            actionBar2.setTitle(LocaleController.formatPluralString("PreviewForwardMessagesCount", messages == null ? 0 : messages.selectedIds.size(), new Object[0]), z);
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            MessagePreviewParams messagePreviewParams2 = messagePreviewView.messagePreviewParams;
            if (messagePreviewParams2.hasSenders) {
                if (messagePreviewParams2.hideForwardSendersName) {
                    TLRPC$User tLRPC$User = messagePreviewView.currentUser;
                    if (tLRPC$User != null) {
                        formatString = LocaleController.formatString("ForwardPreviewSendersNameHidden", R.string.ForwardPreviewSendersNameHidden, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name));
                    } else {
                        if (ChatObject.isChannel(messagePreviewView.currentChat)) {
                        }
                        i2 = R.string.ForwardPreviewSendersNameHiddenGroup;
                    }
                } else {
                    TLRPC$User tLRPC$User2 = messagePreviewView.currentUser;
                    if (tLRPC$User2 != null) {
                        formatString = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name));
                    } else {
                        if (ChatObject.isChannel(messagePreviewView.currentChat)) {
                        }
                        i2 = R.string.ForwardPreviewSendersNameVisibleGroup;
                    }
                }
                this.actionBar.setSubtitle(formatString, z);
            }
            if (messagePreviewParams2.willSeeSenders) {
                TLRPC$User tLRPC$User3 = messagePreviewView.currentUser;
                if (tLRPC$User3 != null) {
                    formatString = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name));
                } else {
                    if (ChatObject.isChannel(messagePreviewView.currentChat)) {
                    }
                    i2 = R.string.ForwardPreviewSendersNameVisibleGroup;
                }
            } else {
                TLRPC$User tLRPC$User4 = messagePreviewView.currentUser;
                if (tLRPC$User4 != null) {
                    formatString = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User4.first_name, tLRPC$User4.last_name));
                } else {
                    if (ChatObject.isChannel(messagePreviewView.currentChat)) {
                    }
                    i2 = R.string.ForwardPreviewSendersNameHiddenGroup;
                }
            }
            this.actionBar.setSubtitle(formatString, z);
            formatString = LocaleController.getString(i2);
            this.actionBar.setSubtitle(formatString, z);
        }

        public void bind() {
            updateMessages();
            updateSubtitle(false);
        }

        public MessageObject getReplyMessage() {
            return getReplyMessage(null);
        }

        public MessageObject getReplyMessage(MessageObject messageObject) {
            MessageObject.GroupedMessages valueAt;
            MessagePreviewParams.Messages messages = MessagePreviewView.this.messagePreviewParams.replyMessage;
            if (messages != null) {
                LongSparseArray<MessageObject.GroupedMessages> longSparseArray = messages.groupedMessagesMap;
                if (longSparseArray == null || longSparseArray.size() <= 0 || (valueAt = MessagePreviewView.this.messagePreviewParams.replyMessage.groupedMessagesMap.valueAt(0)) == null) {
                    return MessagePreviewView.this.messagePreviewParams.replyMessage.messages.get(0);
                }
                if (valueAt.isDocuments) {
                    if (messageObject != null) {
                        return messageObject;
                    }
                    ChatActivity.ReplyQuote replyQuote = MessagePreviewView.this.messagePreviewParams.quote;
                    if (replyQuote != null) {
                        return replyQuote.message;
                    }
                }
                return valueAt.captionMessage;
            }
            return null;
        }

        public ChatMessageCell getReplyMessageCell() {
            MessageObject replyMessage = getReplyMessage();
            if (replyMessage == null) {
                return null;
            }
            for (int i = 0; i < this.chatListView.getChildCount(); i++) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) this.chatListView.getChildAt(i);
                if (chatMessageCell.getMessageObject() != null && (chatMessageCell.getMessageObject() == replyMessage || chatMessageCell.getMessageObject().getId() == replyMessage.getId())) {
                    return chatMessageCell;
                }
            }
            return null;
        }

        public boolean isReplyMessageCell(ChatMessageCell chatMessageCell) {
            MessageObject replyMessage;
            if (chatMessageCell == null || chatMessageCell.getMessageObject() == null || (replyMessage = getReplyMessage()) == null) {
                return false;
            }
            return chatMessageCell.getMessageObject() == replyMessage || chatMessageCell.getMessageObject().getId() == replyMessage.getId();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.currentTab == 0) {
                AndroidUtilities.forEachViews((RecyclerView) this.chatListView, new Consumer() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda19
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        MessagePreviewView.Page.this.lambda$onAttachedToWindow$19((View) obj);
                    }
                });
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            updateSelection();
            this.firstAttach = true;
            this.firstLayout = true;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            updatePositions();
            this.firstLayout = false;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ViewGroup.LayoutParams layoutParams;
            MessageObject messageObject;
            int size;
            MessagePreviewView.this.isLandscapeMode = View.MeasureSpec.getSize(i) > View.MeasureSpec.getSize(i2);
            this.buttonsHeight = 0;
            this.menu.measure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 0));
            int i3 = this.buttonsHeight;
            int measuredHeight = this.menu.getMeasuredHeight();
            android.graphics.Rect rect = this.rect;
            this.buttonsHeight = Math.max(i3, measuredHeight + rect.top + rect.bottom);
            ((ViewGroup.MarginLayoutParams) this.chatListView.getLayoutParams()).topMargin = org.telegram.ui.ActionBar.ActionBar.getCurrentActionBarHeight();
            int i4 = -1;
            if (MessagePreviewView.this.isLandscapeMode) {
                this.chatPreviewContainer.getLayoutParams().height = -1;
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).topMargin = AndroidUtilities.dp(8.0f);
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).bottomMargin = AndroidUtilities.dp(8.0f);
                this.chatPreviewContainer.getLayoutParams().width = (int) Math.min(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(340.0f), View.MeasureSpec.getSize(i) * 0.6f));
                layoutParams = this.menu.getLayoutParams();
            } else {
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).topMargin = 0;
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).bottomMargin = 0;
                this.chatPreviewContainer.getLayoutParams().height = (View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(6.0f)) - this.buttonsHeight;
                if (this.chatPreviewContainer.getLayoutParams().height < View.MeasureSpec.getSize(i2) * 0.5f) {
                    this.chatPreviewContainer.getLayoutParams().height = (int) (View.MeasureSpec.getSize(i2) * 0.5f);
                }
                this.chatPreviewContainer.getLayoutParams().width = -1;
                layoutParams = this.menu.getLayoutParams();
                i4 = View.MeasureSpec.getSize(i2) - this.chatPreviewContainer.getLayoutParams().height;
            }
            layoutParams.height = i4;
            int size2 = (View.MeasureSpec.getSize(i) + View.MeasureSpec.getSize(i2)) << 16;
            if (this.lastSize != size2) {
                for (int i5 = 0; i5 < this.messages.previewMessages.size(); i5++) {
                    if (MessagePreviewView.this.isLandscapeMode) {
                        messageObject = this.messages.previewMessages.get(i5);
                        size = this.chatPreviewContainer.getLayoutParams().width;
                    } else {
                        messageObject = this.messages.previewMessages.get(i5);
                        size = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(16.0f);
                    }
                    messageObject.parentWidth = size;
                    this.messages.previewMessages.get(i5).resetLayout();
                    this.messages.previewMessages.get(i5).forceUpdate = true;
                    Adapter adapter = this.adapter;
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                this.firstLayout = true;
            }
            this.lastSize = size2;
            super.onMeasure(i, i2);
        }

        public void updateSelection() {
            MessageObject messageObject;
            if (this.currentTab == 0) {
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.textSelectionHelper;
                if (chatListTextSelectionHelper.selectionEnd - chatListTextSelectionHelper.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                    return;
                }
                MessageObject replyMessage = getReplyMessage(this.textSelectionHelper.getSelectedCell() != null ? ((ChatMessageCell) this.textSelectionHelper.getSelectedCell()).getMessageObject() : null);
                if (MessagePreviewView.this.messagePreviewParams.quote != null && this.textSelectionHelper.isInSelectionMode()) {
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = this.textSelectionHelper;
                    messagePreviewParams.quoteStart = chatListTextSelectionHelper2.selectionStart;
                    messagePreviewParams.quoteEnd = chatListTextSelectionHelper2.selectionEnd;
                    if (replyMessage != null && ((messageObject = messagePreviewParams.quote.message) == null || messageObject.getId() != replyMessage.getId())) {
                        MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                        messagePreviewParams2.quote = ChatActivity.ReplyQuote.from(replyMessage, messagePreviewParams2.quoteStart, messagePreviewParams2.quoteEnd);
                        MessagePreviewView.this.onQuoteSelectedPart();
                    }
                }
                this.textSelectionHelper.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class RLottieToggleDrawable extends Drawable {
        private RLottieDrawable currentState;
        private boolean detached;
        private boolean isState1;
        private RLottieDrawable state1;
        private RLottieDrawable state2;

        public RLottieToggleDrawable(View view, int i, int i2) {
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            this.state1 = rLottieDrawable;
            rLottieDrawable.setMasterParent(view);
            this.state1.setAllowDecodeSingleFrame(true);
            this.state1.setPlayInDirectionOfCustomEndFrame(true);
            this.state1.setAutoRepeat(0);
            RLottieDrawable rLottieDrawable2 = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            this.state2 = rLottieDrawable2;
            rLottieDrawable2.setMasterParent(view);
            this.state2.setAllowDecodeSingleFrame(true);
            this.state2.setPlayInDirectionOfCustomEndFrame(true);
            this.state2.setAutoRepeat(0);
            this.currentState = this.state1;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.detached) {
                return;
            }
            android.graphics.Rect rect = AndroidUtilities.rectTmp2;
            rect.set(getBounds().centerX() - AndroidUtilities.dp(12.0f), getBounds().centerY() - AndroidUtilities.dp(12.0f), getBounds().centerX() + AndroidUtilities.dp(12.0f), getBounds().centerY() + AndroidUtilities.dp(12.0f));
            if (this.currentState.isLastFrame()) {
                RLottieDrawable rLottieDrawable = this.currentState;
                boolean z = this.isState1;
                if (rLottieDrawable != (z ? this.state1 : this.state2)) {
                    RLottieDrawable rLottieDrawable2 = z ? this.state1 : this.state2;
                    this.currentState = rLottieDrawable2;
                    rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getFramesCount() - 1);
                }
            }
            this.currentState.setBounds(rect);
            this.currentState.draw(canvas);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.state1.setAlpha(i);
            this.state2.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.state1.setColorFilter(colorFilter);
            this.state2.setColorFilter(colorFilter);
        }

        public void setState(boolean z, boolean z2) {
            this.isState1 = z;
            if (!z2) {
                RLottieDrawable rLottieDrawable = z ? this.state1 : this.state2;
                this.currentState = rLottieDrawable;
                rLottieDrawable.setCurrentFrame(rLottieDrawable.getFramesCount() - 1);
                return;
            }
            this.currentState = z ? this.state1 : this.state2;
            this.state1.setCurrentFrame(0);
            this.state2.setCurrentFrame(0);
            this.currentState.start();
        }
    }

    /* loaded from: classes3.dex */
    public interface ResourcesDelegate extends Theme.ResourcesProvider {
        Drawable getWallpaperDrawable();

        boolean isWallpaperMotion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TabsView extends View {
        private final Paint bgPaint;
        private int color;
        private float marginBetween;
        private Utilities.Callback onTabClick;
        private final Theme.ResourcesProvider resourcesProvider;
        private RectF selectRect;
        private int selectedColor;
        private float selectedTab;
        private float tabInnerPadding;
        public final ArrayList tabs;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Tab {
            final RectF bounds = new RectF();
            final RectF clickBounds = new RectF();
            final int id;
            final Text text;

            public Tab(int i, String str) {
                this.id = i;
                this.text = new Text(str, 14.0f, AndroidUtilities.bold());
            }
        }

        public TabsView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            int adaptHue;
            int[] colors;
            this.tabs = new ArrayList();
            Paint paint = new Paint(1);
            this.bgPaint = paint;
            this.tabInnerPadding = AndroidUtilities.dp(12.0f);
            this.marginBetween = AndroidUtilities.dp(13.0f);
            this.selectRect = new RectF();
            this.resourcesProvider = resourcesProvider;
            if (Theme.isCurrentThemeDark()) {
                this.color = -1862270977;
                this.selectedColor = -1325400065;
                adaptHue = 285212671;
            } else {
                int color = Theme.getColor(Theme.key_chat_wallpaper, resourcesProvider);
                if (resourcesProvider instanceof ChatActivity.ThemeDelegate) {
                    ChatActivity.ThemeDelegate themeDelegate = (ChatActivity.ThemeDelegate) resourcesProvider;
                    if ((themeDelegate.getWallpaperDrawable() instanceof MotionBackgroundDrawable) && (colors = ((MotionBackgroundDrawable) themeDelegate.getWallpaperDrawable()).getColors()) != null) {
                        color = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(colors[0], colors[1]), AndroidUtilities.getAverageColor(colors[2], colors[3]));
                    }
                }
                this.color = Theme.adaptHue(-1606201797, color);
                this.selectedColor = Theme.adaptHue(-448573893, color);
                adaptHue = Theme.adaptHue(814980216, color);
            }
            paint.setColor(adaptHue);
        }

        private int getHitTab(float f, float f2) {
            for (int i = 0; i < this.tabs.size(); i++) {
                if (((Tab) this.tabs.get(i)).clickBounds.contains(f, f2)) {
                    return ((Tab) this.tabs.get(i)).id;
                }
            }
            return -1;
        }

        public void addTab(int i, String str) {
            this.tabs.add(new Tab(i, str));
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            boolean z = true;
            if (this.tabs.size() <= 1) {
                return;
            }
            float f = this.selectedTab;
            double d = f;
            int floor = (int) Math.floor(d);
            boolean z2 = floor >= 0 && floor < this.tabs.size();
            int ceil = (int) Math.ceil(d);
            z = (ceil < 0 || ceil >= this.tabs.size()) ? false : false;
            if (z2 && z) {
                AndroidUtilities.lerp(((Tab) this.tabs.get(floor)).bounds, ((Tab) this.tabs.get(ceil)).bounds, f - floor, this.selectRect);
            } else if (z2) {
                this.selectRect.set(((Tab) this.tabs.get(floor)).bounds);
            } else if (z) {
                this.selectRect.set(((Tab) this.tabs.get(ceil)).bounds);
            }
            if (z2 || z) {
                canvas.drawRoundRect(this.selectRect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), this.bgPaint);
            }
            for (int i = 0; i < this.tabs.size(); i++) {
                Tab tab = (Tab) this.tabs.get(i);
                tab.text.draw(canvas, tab.bounds.left + this.tabInnerPadding, getMeasuredHeight() / 2.0f, ColorUtils.blendARGB(this.color, this.selectedColor, 1.0f - Math.abs(f - i)), 1.0f);
            }
        }

        @Override // android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            Utilities.Callback callback;
            if (this.tabs.size() <= 1) {
                return false;
            }
            int hitTab = getHitTab(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() == 0) {
                return hitTab != -1;
            }
            if (motionEvent.getAction() == 1 && hitTab != -1 && (callback = this.onTabClick) != null) {
                callback.run(Integer.valueOf(hitTab));
            }
            return false;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.tabInnerPadding = AndroidUtilities.dp(12.0f);
            this.marginBetween = AndroidUtilities.dp(13.0f);
            float f = 0.0f;
            for (int i3 = 0; i3 < this.tabs.size(); i3++) {
                if (i3 > 0) {
                    f += this.marginBetween;
                }
                f += this.tabInnerPadding + ((Tab) this.tabs.get(i3)).text.getWidth() + this.tabInnerPadding;
            }
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            float dp = (measuredHeight - AndroidUtilities.dp(26.0f)) / 2.0f;
            float dp2 = (measuredHeight + AndroidUtilities.dp(26.0f)) / 2.0f;
            float f2 = (measuredWidth - f) / 2.0f;
            for (int i4 = 0; i4 < this.tabs.size(); i4++) {
                float width = this.tabInnerPadding + ((Tab) this.tabs.get(i4)).text.getWidth() + this.tabInnerPadding;
                ((Tab) this.tabs.get(i4)).bounds.set(f2, dp, f2 + width, dp2);
                ((Tab) this.tabs.get(i4)).clickBounds.set(((Tab) this.tabs.get(i4)).bounds);
                ((Tab) this.tabs.get(i4)).clickBounds.inset((-this.marginBetween) / 2.0f, -dp);
                f2 += width + this.marginBetween;
            }
        }

        public void setOnTabClick(Utilities.Callback callback) {
            this.onTabClick = callback;
        }

        public void setSelectedTab(float f) {
            this.selectedTab = f;
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    public static class ToggleButton extends View {
        private boolean first;
        RLottieToggleDrawable iconDrawable;
        private boolean isState1;
        final int minWidth;
        final String text1;
        final String text2;
        AnimatedTextView.AnimatedTextDrawable textDrawable;

        public ToggleButton(Context context, int i, String str, int i2, String str2, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.first = true;
            this.text1 = str;
            this.text2 = str2;
            setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), 2));
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.textDrawable = animatedTextDrawable;
            animatedTextDrawable.setAnimationProperties(0.35f, 0L, 300L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.textDrawable.setTextSize(AndroidUtilities.dp(16.0f));
            this.textDrawable.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem, resourcesProvider));
            this.textDrawable.setCallback(this);
            this.textDrawable.setEllipsizeByGradient(true ^ LocaleController.isRTL);
            if (LocaleController.isRTL) {
                this.textDrawable.setGravity(5);
            }
            int dp = (int) (AndroidUtilities.dp(77.0f) + Math.max(this.textDrawable.getPaint().measureText(str), this.textDrawable.getPaint().measureText(str2)));
            this.minWidth = dp;
            this.textDrawable.setOverrideFullWidth(dp);
            RLottieToggleDrawable rLottieToggleDrawable = new RLottieToggleDrawable(this, i, i2);
            this.iconDrawable = rLottieToggleDrawable;
            rLottieToggleDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultSubmenuItemIcon, resourcesProvider), PorterDuff.Mode.SRC_IN));
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (LocaleController.isRTL) {
                this.iconDrawable.setBounds(getMeasuredWidth() - AndroidUtilities.dp(41.0f), (getMeasuredHeight() - AndroidUtilities.dp(24.0f)) / 2, getMeasuredWidth() - AndroidUtilities.dp(17.0f), (getMeasuredHeight() + AndroidUtilities.dp(24.0f)) / 2);
                this.textDrawable.setBounds(0, 0, getMeasuredWidth() - AndroidUtilities.dp(59.0f), getMeasuredHeight());
            } else {
                this.iconDrawable.setBounds(AndroidUtilities.dp(17.0f), (getMeasuredHeight() - AndroidUtilities.dp(24.0f)) / 2, AndroidUtilities.dp(41.0f), (getMeasuredHeight() + AndroidUtilities.dp(24.0f)) / 2);
                this.textDrawable.setBounds(AndroidUtilities.dp(59.0f), 0, getMeasuredWidth(), getMeasuredHeight());
            }
            this.textDrawable.draw(canvas);
            this.iconDrawable.draw(canvas);
        }

        public boolean getState() {
            return this.isState1;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int mode = View.MeasureSpec.getMode(i);
            int size = View.MeasureSpec.getSize(i);
            int i3 = this.minWidth;
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(mode == 1073741824 ? Math.max(size, i3) : Math.min(size, i3), mode), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getVisibility() != 0 || getAlpha() < 0.5f) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }

        public void setState(boolean z, boolean z2) {
            if (this.first || z != this.isState1) {
                this.isState1 = z;
                this.textDrawable.setText(z ? this.text1 : this.text2, z2 && !LocaleController.isRTL);
                this.iconDrawable.setState(z, z2);
                this.first = false;
            }
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.textDrawable || super.verifyDrawable(drawable);
        }
    }

    public MessagePreviewView(final Context context, ChatActivity chatActivity, MessagePreviewParams messagePreviewParams, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, int i, ResourcesDelegate resourcesDelegate, int i2, final boolean z) {
        super(context);
        this.changeBoundsRunnable = new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView.1
            @Override // java.lang.Runnable
            public void run() {
                ValueAnimator valueAnimator = MessagePreviewView.this.offsetsAnimator;
                if (valueAnimator == null || valueAnimator.isRunning()) {
                    return;
                }
                MessagePreviewView.this.offsetsAnimator.start();
            }
        };
        this.drawingGroups = new ArrayList(10);
        this.showOutdatedQuote = z;
        this.chatActivity = chatActivity;
        this.currentAccount = i;
        this.currentUser = tLRPC$User;
        this.currentChat = tLRPC$Chat;
        this.messagePreviewParams = messagePreviewParams;
        this.resourcesProvider = resourcesDelegate;
        this.viewPager = new ViewPagerFixed(context, resourcesDelegate) { // from class: org.telegram.ui.Components.MessagePreviewView.2
            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onScrollEnd() {
                View view = this.viewPages[0];
                if (view instanceof Page) {
                    ((Page) view).textSelectionHelper.stopScrolling();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.ViewPagerFixed
            public void onTabAnimationUpdate(boolean z2) {
                MessagePreviewView messagePreviewView = MessagePreviewView.this;
                messagePreviewView.tabsView.setSelectedTab(messagePreviewView.viewPager.getPositionAnimated());
                View view = this.viewPages[0];
                if (view instanceof Page) {
                    ((Page) view).textSelectionHelper.onParentScrolled();
                }
                View view2 = this.viewPages[1];
                if (view2 instanceof Page) {
                    ((Page) view2).textSelectionHelper.onParentScrolled();
                }
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (MessagePreviewView.this.isTouchedHandle()) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.tabsView = new TabsView(context, resourcesDelegate);
        int i3 = 0;
        for (int i4 = 0; i4 < 3; i4++) {
            if (i4 == 0 && messagePreviewParams.replyMessage != null) {
                this.tabsView.addTab(0, LocaleController.getString(R.string.MessageOptionsReply));
            } else if (i4 == 1 && messagePreviewParams.forwardMessages != null && !z) {
                this.tabsView.addTab(1, LocaleController.getString(R.string.MessageOptionsForward));
            } else if (i4 == 2 && messagePreviewParams.linkMessage != null && !z) {
                this.tabsView.addTab(2, LocaleController.getString(R.string.MessageOptionsLink));
            }
            if (i4 == i2) {
                i3 = this.tabsView.tabs.size() - 1;
            }
        }
        this.viewPager.setAdapter(new ViewPagerFixed.Adapter() { // from class: org.telegram.ui.Components.MessagePreviewView.3
            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i5, int i6) {
                ((Page) view).bind();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i5) {
                return new Page(context, i5);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return MessagePreviewView.this.tabsView.tabs.size();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i5) {
                return ((TabsView.Tab) MessagePreviewView.this.tabsView.tabs.get(i5)).id;
            }
        });
        this.viewPager.setPosition(i3);
        this.tabsView.setSelectedTab(i3);
        addView(this.tabsView, LayoutHelper.createFrame(-1, 66, 87));
        addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 66.0f));
        this.tabsView.setOnTabClick(new Utilities.Callback() { // from class: org.telegram.ui.Components.MessagePreviewView$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MessagePreviewView.this.lambda$new$0((Integer) obj);
            }
        });
        setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.MessagePreviewView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$new$1;
                lambda$new$1 = MessagePreviewView.this.lambda$new$1(z, view, motionEvent);
                return lambda$new$1;
            }
        });
        this.showing = true;
        setAlpha(0.0f);
        setScaleX(0.95f);
        setScaleY(0.95f);
        animate().alpha(1.0f).scaleX(1.0f).setDuration(250L).setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR).scaleY(1.0f);
        updateColors();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Integer num) {
        if (((TabsView.Tab) this.tabsView.tabs.get(this.viewPager.getCurrentPosition())).id == num.intValue()) {
            return;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= this.tabsView.tabs.size()) {
                break;
            } else if (((TabsView.Tab) this.tabsView.tabs.get(i2)).id == num.intValue()) {
                i = i2;
                break;
            } else {
                i2++;
            }
        }
        if (this.viewPager.getCurrentPosition() == i) {
            return;
        }
        this.viewPager.scrollToPosition(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(boolean z, View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 && !z) {
            dismiss(true);
        }
        return true;
    }

    private void updateColors() {
    }

    public void dismiss(final boolean z) {
        if (this.showing) {
            int i = 0;
            this.showing = false;
            animate().alpha(0.0f).scaleX(0.95f).scaleY(0.95f).setDuration(250L).setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MessagePreviewView.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (MessagePreviewView.this.getParent() != null) {
                        ((ViewGroup) MessagePreviewView.this.getParent()).removeView(MessagePreviewView.this);
                    }
                    MessagePreviewView.this.onFullDismiss(z);
                }
            });
            while (true) {
                View[] viewArr = this.viewPager.viewPages;
                if (i >= viewArr.length) {
                    break;
                }
                View view = viewArr[i];
                if (view instanceof Page) {
                    Page page = (Page) view;
                    if (page.currentTab == 0) {
                        page.updateSelection();
                        break;
                    }
                }
                i++;
            }
            onDismiss(z);
        }
    }

    public boolean isShowing() {
        return this.showing;
    }

    public boolean isTouchedHandle() {
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return false;
            }
            View view = viewArr[i];
            if (view != null) {
                Page page = (Page) view;
                if (page.currentTab == 0) {
                    return page.textSelectionHelper.isTouched();
                }
            }
            i++;
        }
    }

    protected abstract void onDismiss(boolean z);

    protected abstract void onFullDismiss(boolean z);

    protected abstract void onQuoteSelectedPart();

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeForward() {
    }

    protected abstract void removeLink();

    protected abstract void removeQuote();

    protected abstract void removeReply();

    protected abstract void selectAnotherChat(boolean z);

    public void setSendAsPeer(TLRPC$Peer tLRPC$Peer) {
        this.sendAsPeer = tLRPC$Peer;
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            View view = viewArr[i];
            if (view != null) {
                Page page = (Page) view;
                if (page.currentTab == 1) {
                    page.updateMessages();
                }
            }
            i++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b7 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateAll() {
        MessagePreviewParams.Messages messages;
        ToggleButton toggleButton;
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            View view = viewArr[i];
            if (view instanceof Page) {
                Page page = (Page) view;
                int i2 = page.currentTab;
                if (i2 == 1) {
                    messages = this.messagePreviewParams.forwardMessages;
                } else if (i2 == 0) {
                    messages = this.messagePreviewParams.replyMessage;
                } else {
                    if (i2 == 2) {
                        messages = this.messagePreviewParams.linkMessage;
                    }
                    page.updateMessages();
                    if (page.currentTab == 0) {
                        if (!this.showOutdatedQuote || this.messagePreviewParams.isSecret) {
                            this.messagePreviewParams.quote = null;
                            page.textSelectionHelper.clear();
                            page.switchToQuote(false, true);
                        } else {
                            MessageObject replyMessage = page.getReplyMessage(page.textSelectionHelper.getSelectedCell() != null ? ((ChatMessageCell) page.textSelectionHelper.getSelectedCell()).getMessageObject() : null);
                            if (replyMessage != null) {
                                MessagePreviewParams messagePreviewParams = this.messagePreviewParams;
                                messagePreviewParams.quoteStart = 0;
                                messagePreviewParams.quoteEnd = Math.min(MessagesController.getInstance(this.currentAccount).quoteLengthMax, replyMessage.messageOwner.message.length());
                                MessagePreviewParams messagePreviewParams2 = this.messagePreviewParams;
                                messagePreviewParams2.quote = ChatActivity.ReplyQuote.from(replyMessage, messagePreviewParams2.quoteStart, messagePreviewParams2.quoteEnd);
                                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = page.textSelectionHelper;
                                ChatMessageCell replyMessageCell = page.getReplyMessageCell();
                                MessagePreviewParams messagePreviewParams3 = this.messagePreviewParams;
                                chatListTextSelectionHelper.select(replyMessageCell, messagePreviewParams3.quoteStart, messagePreviewParams3.quoteEnd);
                            }
                        }
                        page.updateSubtitle(true);
                    }
                    toggleButton = page.changeSizeBtn;
                    if (toggleButton == null) {
                        toggleButton.animate().alpha(this.messagePreviewParams.hasMedia ? 1.0f : 0.5f).start();
                    }
                }
                page.messages = messages;
                page.updateMessages();
                if (page.currentTab == 0) {
                }
                toggleButton = page.changeSizeBtn;
                if (toggleButton == null) {
                }
            }
            i++;
        }
    }

    public void updateLink() {
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            View view = viewArr[i];
            if (view != null) {
                Page page = (Page) view;
                if (page.currentTab == 2) {
                    FrameLayout frameLayout = page.changeSizeBtnContainer;
                    MessagePreviewParams messagePreviewParams = this.messagePreviewParams;
                    frameLayout.setVisibility((!messagePreviewParams.singleLink || messagePreviewParams.hasMedia) ? 0 : 8);
                    page.changeSizeBtn.setVisibility(this.messagePreviewParams.isVideo ? 4 : 0);
                    page.videoChangeSizeBtn.setVisibility(this.messagePreviewParams.isVideo ? 0 : 4);
                    page.changeSizeBtnContainer.animate().alpha(this.messagePreviewParams.hasMedia ? 1.0f : 0.5f).start();
                    page.changeSizeBtn.setState(this.messagePreviewParams.webpageSmall, true);
                    page.videoChangeSizeBtn.setState(this.messagePreviewParams.webpageSmall, true);
                    page.changePositionBtn.setState(!this.messagePreviewParams.webpageTop, true);
                    page.updateMessages();
                }
            }
            i++;
        }
    }
}
