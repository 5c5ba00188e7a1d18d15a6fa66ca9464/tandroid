package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.util.Property;
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
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
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
/* loaded from: classes4.dex */
public class MessagePreviewView extends FrameLayout {
    Runnable changeBoundsRunnable;
    final ChatActivity chatActivity;
    private final int currentAccount;
    TLRPC$Chat currentChat;
    TLRPC$User currentUser;
    private final ArrayList<MessageObject.GroupedMessages> drawingGroups;
    private boolean firstLayout;
    boolean isLandscapeMode;
    final MessagePreviewParams messagePreviewParams;
    ValueAnimator offsetsAnimator;
    private final ResourcesDelegate resourcesProvider;
    boolean returnSendersNames;
    TLRPC$Peer sendAsPeer;
    ChatMessageSharedResources sharedResources;
    final boolean showOutdatedQuote;
    boolean showing;
    TabsView tabsView;
    ViewPagerFixed viewPager;

    /* loaded from: classes4.dex */
    public interface ResourcesDelegate extends Theme.ResourcesProvider {
        Drawable getWallpaperDrawable();

        boolean isWallpaperMotion();
    }

    private void updateColors() {
    }

    protected void onDismiss(boolean z) {
    }

    protected void onFullDismiss() {
    }

    protected void onQuoteSelectedPart() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeForward() {
    }

    protected void removeLink() {
    }

    protected void removeQuote() {
    }

    protected void removeReply() {
    }

    protected void selectAnotherChat(boolean z) {
    }

    public void setSendAsPeer(TLRPC$Peer tLRPC$Peer) {
        this.sendAsPeer = tLRPC$Peer;
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            if (viewArr[i] != null && ((Page) viewArr[i]).currentTab == 1) {
                ((Page) viewArr[i]).updateMessages();
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class Page extends FrameLayout {
        ActionBar actionBar;
        Adapter adapter;
        private int buttonsHeight;
        ToggleButton changePositionBtn;
        ToggleButton changeSizeBtn;
        GridLayoutManagerFixed chatLayoutManager;
        RecyclerListView chatListView;
        SizeNotifierFrameLayout chatPreviewContainer;
        int chatTopOffset;
        ActionBarMenuSubItem clearQuoteButton;
        public int currentTab;
        int currentTopOffset;
        float currentYOffset;
        ActionBarMenuSubItem deleteReplyButton;
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
        TextSelectionHelper.ChatListTextSelectionHelper textSelectionHelper;
        View textSelectionOverlay;
        boolean toQuote;
        boolean updateAfterAnimations;
        float yOffset;

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
            if (z2) {
                this.quoteSwitcher = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                ActionBarMenuSubItem actionBarMenuSubItem = this.quoteButton;
                if (actionBarMenuSubItem != null) {
                    actionBarMenuSubItem.setVisibility(0);
                    ActionBarMenuSubItem actionBarMenuSubItem2 = this.quoteButton;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    fArr[0] = !z ? 1.0f : 0.0f;
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuSubItem2, property, fArr));
                }
                ActionBarMenuSubItem actionBarMenuSubItem3 = this.clearQuoteButton;
                if (actionBarMenuSubItem3 != null) {
                    actionBarMenuSubItem3.setVisibility(0);
                    ActionBarMenuSubItem actionBarMenuSubItem4 = this.clearQuoteButton;
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    fArr2[0] = z ? 1.0f : 0.0f;
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuSubItem4, property2, fArr2));
                }
                ActionBarMenuSubItem actionBarMenuSubItem5 = this.replyAnotherChatButton;
                if (actionBarMenuSubItem5 != null) {
                    actionBarMenuSubItem5.setVisibility(0);
                    ActionBarMenuSubItem actionBarMenuSubItem6 = this.replyAnotherChatButton;
                    Property property3 = View.ALPHA;
                    float[] fArr3 = new float[1];
                    fArr3[0] = !z ? 1.0f : 0.0f;
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuSubItem6, property3, fArr3));
                }
                ActionBarMenuSubItem actionBarMenuSubItem7 = this.quoteAnotherChatButton;
                if (actionBarMenuSubItem7 != null) {
                    actionBarMenuSubItem7.setVisibility(0);
                    ActionBarMenuSubItem actionBarMenuSubItem8 = this.quoteAnotherChatButton;
                    Property property4 = View.ALPHA;
                    float[] fArr4 = new float[1];
                    fArr4[0] = z ? 1.0f : 0.0f;
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuSubItem8, property4, fArr4));
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
                return;
            }
            ActionBarMenuSubItem actionBarMenuSubItem9 = this.quoteButton;
            if (actionBarMenuSubItem9 != null) {
                actionBarMenuSubItem9.setAlpha(!z ? 1.0f : 0.0f);
                this.quoteButton.setVisibility(!z ? 0 : 4);
            }
            ActionBarMenuSubItem actionBarMenuSubItem10 = this.clearQuoteButton;
            if (actionBarMenuSubItem10 != null) {
                actionBarMenuSubItem10.setAlpha(z ? 1.0f : 0.0f);
                this.clearQuoteButton.setVisibility(z ? 0 : 4);
            }
            ActionBarMenuSubItem actionBarMenuSubItem11 = this.replyAnotherChatButton;
            if (actionBarMenuSubItem11 != null) {
                actionBarMenuSubItem11.setAlpha(!z ? 1.0f : 0.0f);
                this.replyAnotherChatButton.setVisibility(!z ? 0 : 4);
            }
            ActionBarMenuSubItem actionBarMenuSubItem12 = this.quoteAnotherChatButton;
            if (actionBarMenuSubItem12 != null) {
                actionBarMenuSubItem12.setAlpha(z ? 1.0f : 0.0f);
                this.quoteAnotherChatButton.setVisibility(z ? 0 : 4);
            }
        }

        public ChatMessageCell getReplyMessageCell() {
            MessageObject replyMessage = getReplyMessage();
            for (int i = 0; i < this.chatListView.getChildCount(); i++) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) this.chatListView.getChildAt(i);
                if (chatMessageCell.getMessageObject() != null && (chatMessageCell.getMessageObject() == replyMessage || (chatMessageCell.getMessageObject().getId() == replyMessage.getId() && chatMessageCell.getMessageObject().getDialogId() == replyMessage.getDialogId()))) {
                    return chatMessageCell;
                }
            }
            return null;
        }

        public MessageObject getReplyMessage() {
            MessageObject.GroupedMessages valueAt;
            if (MessagePreviewView.this.messagePreviewParams.replyMessage.groupedMessagesMap.size() > 0 && (valueAt = MessagePreviewView.this.messagePreviewParams.replyMessage.groupedMessagesMap.valueAt(0)) != null) {
                return valueAt.captionMessage;
            }
            return MessagePreviewView.this.messagePreviewParams.replyMessage.messages.get(0);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0644  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x064b  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0668  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x067c  */
        /* JADX WARN: Type inference failed for: r0v157, types: [org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout] */
        /* JADX WARN: Type inference failed for: r7v8, types: [android.view.View] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Page(Context context, int i) {
            super(context);
            int i2;
            boolean z;
            final ToggleButton toggleButton;
            int i3;
            View view;
            MessagePreviewParams.Messages messages;
            float f;
            int i4;
            LinearLayout linearLayout;
            this.rect = new android.graphics.Rect();
            this.currentTab = i;
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda17
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$new$0;
                    lambda$new$0 = MessagePreviewView.Page.this.lambda$new$0(view2, motionEvent);
                    return lambda$new$0;
                }
            });
            SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context, MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
                public Drawable getNewDrawable() {
                    Drawable wallpaperDrawable = MessagePreviewView.this.resourcesProvider.getWallpaperDrawable();
                    return wallpaperDrawable != null ? wallpaperDrawable : super.getNewDrawable();
                }

                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getY() < Page.this.currentTopOffset) {
                        return false;
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }
            };
            this.chatPreviewContainer = sizeNotifierFrameLayout;
            sizeNotifierFrameLayout.setBackgroundImage(MessagePreviewView.this.resourcesProvider.getWallpaperDrawable(), MessagePreviewView.this.resourcesProvider.isWallpaperMotion());
            this.chatPreviewContainer.setOccupyStatusBar(false);
            int i5 = Build.VERSION.SDK_INT;
            if (i5 >= 21) {
                this.chatPreviewContainer.setOutlineProvider(new ViewOutlineProvider(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.3
                    @Override // android.view.ViewOutlineProvider
                    @TargetApi(21)
                    public void getOutline(View view2, Outline outline) {
                        outline.setRoundRect(0, Page.this.currentTopOffset + 1, view2.getMeasuredWidth(), view2.getMeasuredHeight(), AndroidUtilities.dp(8.0f));
                    }
                });
                this.chatPreviewContainer.setClipToOutline(true);
                this.chatPreviewContainer.setElevation(AndroidUtilities.dp(4.0f));
            }
            ActionBar actionBar = new ActionBar(MessagePreviewView.this, context, MessagePreviewView.this.resourcesProvider);
            this.actionBar = actionBar;
            actionBar.setBackgroundColor(MessagePreviewView.this.getThemedColor(Theme.key_actionBarDefault));
            TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = new TextSelectionHelper.ChatListTextSelectionHelper(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.4
                {
                    this.resourcesProvider = MessagePreviewView.this.resourcesProvider;
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
                protected boolean canShowQuote() {
                    Page page = Page.this;
                    return page.currentTab == 0 && !MessagePreviewView.this.messagePreviewParams.secret;
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                protected void onQuoteClick(MessageObject messageObject, int i6, int i7, CharSequence charSequence) {
                    Page page = Page.this;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = page.textSelectionHelper;
                    if (chatListTextSelectionHelper2.selectionEnd - chatListTextSelectionHelper2.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                        Page.this.showQuoteLengthError();
                        return;
                    }
                    Page page2 = Page.this;
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = page2.textSelectionHelper;
                    messagePreviewParams.quoteStart = chatListTextSelectionHelper3.selectionStart;
                    messagePreviewParams.quoteEnd = chatListTextSelectionHelper3.selectionEnd;
                    if (messagePreviewParams.quote == null) {
                        messagePreviewParams.quote = ChatActivity.ReplyQuote.from(page2.getReplyMessage(), i6, i7);
                    }
                    MessagePreviewView.this.onQuoteSelectedPart();
                    MessagePreviewView.this.dismiss(true);
                }

                @Override // org.telegram.ui.Cells.TextSelectionHelper
                public boolean isSelected(MessageObject messageObject) {
                    Page page = Page.this;
                    return page.currentTab == 0 && !MessagePreviewView.this.messagePreviewParams.secret && isInSelectionMode();
                }
            };
            this.textSelectionHelper = chatListTextSelectionHelper;
            chatListTextSelectionHelper.setCallback(new TextSelectionHelper.Callback(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.5
                @Override // org.telegram.ui.Cells.TextSelectionHelper.Callback
                public void onStateChanged(boolean z2) {
                    Page page = Page.this;
                    if (MessagePreviewView.this.showing) {
                        if (!z2 && page.menu.getSwipeBack().isForegroundOpen()) {
                            Page.this.menu.getSwipeBack().closeForeground(true);
                        } else if (z2) {
                            Page page2 = Page.this;
                            MessagePreviewView messagePreviewView = MessagePreviewView.this;
                            if (messagePreviewView.messagePreviewParams.quote == null) {
                                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = page2.textSelectionHelper;
                                if (chatListTextSelectionHelper2.selectionEnd - chatListTextSelectionHelper2.selectionStart > MessagesController.getInstance(messagePreviewView.currentAccount).quoteLengthMax) {
                                    Page.this.showQuoteLengthError();
                                    return;
                                }
                                Page page3 = Page.this;
                                MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = page3.textSelectionHelper;
                                messagePreviewParams.quoteStart = chatListTextSelectionHelper3.selectionStart;
                                messagePreviewParams.quoteEnd = chatListTextSelectionHelper3.selectionEnd;
                                MessageObject replyMessage = page3.getReplyMessage();
                                MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                                messagePreviewParams.quote = ChatActivity.ReplyQuote.from(replyMessage, messagePreviewParams2.quoteStart, messagePreviewParams2.quoteEnd);
                                Page.this.menu.getSwipeBack().openForeground(Page.this.menuBack);
                            }
                        }
                    }
                }
            });
            RecyclerListView recyclerListView = new RecyclerListView(context, MessagePreviewView.this.resourcesProvider, MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.6
                private boolean firstLayout = true;

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
                        chatMessageCell.drawMessageText(canvas);
                        if (chatMessageCell.getCurrentMessagesGroup() == null || ((chatMessageCell.getCurrentPosition() != null && chatMessageCell.getCurrentPosition().last) || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner)) {
                            chatMessageCell.drawCaptionLayout(canvas, false, chatMessageCell.getAlpha());
                        }
                        if (chatMessageCell.getCurrentMessagesGroup() != null || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                            chatMessageCell.drawNamesLayout(canvas, chatMessageCell.getAlpha());
                        }
                        if ((chatMessageCell.getCurrentPosition() != null && chatMessageCell.getCurrentPosition().last) || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                            chatMessageCell.drawTime(canvas, chatMessageCell.getAlpha(), true);
                        }
                        chatMessageCell.drawLinkPreview(canvas, 1.0f, false);
                        if (chatMessageCell.transitionParams.animateLinkAbove) {
                            chatMessageCell.drawLinkPreview(canvas, 1.0f, true);
                        }
                        canvas.restore();
                        chatMessageCell.getTransitionParams().recordDrawingStatePreview();
                        canvas.restore();
                        return drawChild;
                    }
                    return true;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                public void dispatchDraw(Canvas canvas) {
                    for (int i6 = 0; i6 < getChildCount(); i6++) {
                        View childAt = getChildAt(i6);
                        if (childAt instanceof ChatMessageCell) {
                            ((ChatMessageCell) childAt).setParentViewSize(Page.this.chatPreviewContainer.getMeasuredWidth(), Page.this.chatPreviewContainer.getBackgroundSizeY());
                        }
                    }
                    drawChatBackgroundElements(canvas);
                    super.dispatchDraw(canvas);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                public void onLayout(boolean z2, int i6, int i7, int i8, int i9) {
                    if (this.firstLayout) {
                        scrollToPosition(0);
                        this.firstLayout = false;
                    }
                    super.onLayout(z2, i6, i7, i8, i9);
                    Page.this.updatePositions();
                }

                /* JADX WARN: Type inference failed for: r3v0 */
                /* JADX WARN: Type inference failed for: r3v1, types: [int, boolean] */
                /* JADX WARN: Type inference failed for: r3v9 */
                private void drawChatBackgroundElements(Canvas canvas) {
                    boolean z2;
                    int i6;
                    MessageObject.GroupedMessages currentMessagesGroup;
                    ChatMessageCell chatMessageCell;
                    MessageObject.GroupedMessages currentMessagesGroup2;
                    int childCount = getChildCount();
                    ?? r3 = 0;
                    MessageObject.GroupedMessages groupedMessages = null;
                    for (int i7 = 0; i7 < childCount; i7++) {
                        View childAt = getChildAt(i7);
                        if ((childAt instanceof ChatMessageCell) && ((currentMessagesGroup2 = (chatMessageCell = (ChatMessageCell) childAt).getCurrentMessagesGroup()) == null || currentMessagesGroup2 != groupedMessages)) {
                            chatMessageCell.getCurrentPosition();
                            chatMessageCell.getBackgroundDrawable();
                            groupedMessages = currentMessagesGroup2;
                        }
                    }
                    int i8 = 0;
                    while (i8 < 3) {
                        MessagePreviewView.this.drawingGroups.clear();
                        if (i8 != 2 || Page.this.chatListView.isFastScrollAnimationRunning()) {
                            int i9 = 0;
                            while (true) {
                                z2 = true;
                                if (i9 >= childCount) {
                                    break;
                                }
                                View childAt2 = Page.this.chatListView.getChildAt(i9);
                                if (childAt2 instanceof ChatMessageCell) {
                                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt2;
                                    if (childAt2.getY() <= Page.this.chatListView.getHeight() && childAt2.getY() + childAt2.getHeight() >= 0.0f && (currentMessagesGroup = chatMessageCell2.getCurrentMessagesGroup()) != null && ((i8 != 0 || currentMessagesGroup.messages.size() != 1) && ((i8 != 1 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i8 != 0 || !chatMessageCell2.getMessageObject().deleted) && ((i8 != 1 || chatMessageCell2.getMessageObject().deleted) && ((i8 != 2 || chatMessageCell2.willRemovedAfterAnimation()) && (i8 == 2 || !chatMessageCell2.willRemovedAfterAnimation()))))))) {
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
                                        int i10 = transitionParams2.top;
                                        if (i10 == 0 || top < i10) {
                                            transitionParams2.top = top;
                                        }
                                        int i11 = transitionParams2.bottom;
                                        if (i11 == 0 || top2 > i11) {
                                            transitionParams2.bottom = top2;
                                        }
                                        int i12 = transitionParams2.left;
                                        if (i12 == 0 || left < i12) {
                                            transitionParams2.left = left;
                                        }
                                        int i13 = transitionParams2.right;
                                        if (i13 == 0 || left2 > i13) {
                                            transitionParams2.right = left2;
                                        }
                                    }
                                }
                                i9++;
                            }
                            int i14 = 0;
                            while (i14 < MessagePreviewView.this.drawingGroups.size()) {
                                MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) MessagePreviewView.this.drawingGroups.get(i14);
                                if (groupedMessages2 == null) {
                                    i6 = i8;
                                } else {
                                    float nonAnimationTranslationX = groupedMessages2.transitionParams.cell.getNonAnimationTranslationX(z2);
                                    MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages2.transitionParams;
                                    float f2 = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                                    float f3 = transitionParams3.top + transitionParams3.offsetTop;
                                    float f4 = transitionParams3.right + nonAnimationTranslationX + transitionParams3.offsetRight;
                                    float f5 = transitionParams3.bottom + transitionParams3.offsetBottom;
                                    if (!transitionParams3.backgroundChangeBounds) {
                                        f3 += transitionParams3.cell.getTranslationY();
                                        f5 += groupedMessages2.transitionParams.cell.getTranslationY();
                                    }
                                    if (f3 < (-AndroidUtilities.dp(20.0f))) {
                                        f3 = -AndroidUtilities.dp(20.0f);
                                    }
                                    if (f5 > Page.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) {
                                        f5 = Page.this.chatListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f);
                                    }
                                    boolean z3 = (groupedMessages2.transitionParams.cell.getScaleX() == 1.0f && groupedMessages2.transitionParams.cell.getScaleY() == 1.0f) ? false : true;
                                    if (z3) {
                                        canvas.save();
                                        canvas.scale(groupedMessages2.transitionParams.cell.getScaleX(), groupedMessages2.transitionParams.cell.getScaleY(), f2 + ((f4 - f2) / 2.0f), f3 + ((f5 - f3) / 2.0f));
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages2.transitionParams;
                                    i6 = i8;
                                    transitionParams4.cell.drawBackground(canvas, (int) f2, (int) f3, (int) f4, (int) f5, transitionParams4.pinnedTop, transitionParams4.pinnedBotton, false, 0);
                                    MessageObject.GroupedMessages.TransitionParams transitionParams5 = groupedMessages2.transitionParams;
                                    transitionParams5.cell = null;
                                    transitionParams5.drawCaptionLayout = groupedMessages2.hasCaption;
                                    if (z3) {
                                        canvas.restore();
                                        for (int i15 = 0; i15 < childCount; i15++) {
                                            View childAt3 = Page.this.chatListView.getChildAt(i15);
                                            if (childAt3 instanceof ChatMessageCell) {
                                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt3;
                                                if (chatMessageCell3.getCurrentMessagesGroup() == groupedMessages2) {
                                                    int left3 = chatMessageCell3.getLeft();
                                                    int top3 = chatMessageCell3.getTop();
                                                    childAt3.setPivotX((f2 - left3) + ((f4 - f2) / 2.0f));
                                                    childAt3.setPivotY((f3 - top3) + ((f5 - f3) / 2.0f));
                                                }
                                            }
                                        }
                                    }
                                }
                                i14++;
                                i8 = i6;
                                z2 = true;
                            }
                        }
                        i8++;
                        r3 = 0;
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView
                public void onScrollStateChanged(int i6) {
                    if (i6 == 0) {
                        Page.this.textSelectionHelper.stopScrolling();
                    }
                    super.onScrollStateChanged(i6);
                }

                @Override // androidx.recyclerview.widget.RecyclerView
                public void onScrolled(int i6, int i7) {
                    super.onScrolled(i6, i7);
                    Page.this.textSelectionHelper.onParentScrolled();
                }
            };
            this.chatListView = recyclerListView;
            7 r4 = new 7(null, this.chatListView, MessagePreviewView.this.resourcesProvider, MessagePreviewView.this);
            this.itemAnimator = r4;
            recyclerListView.setItemAnimator(r4);
            this.chatListView.setOnScrollListener(new RecyclerView.OnScrollListener(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.8
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i6, int i7) {
                    super.onScrolled(recyclerView, i6, i7);
                    for (int i8 = 0; i8 < Page.this.chatListView.getChildCount(); i8++) {
                        ((ChatMessageCell) Page.this.chatListView.getChildAt(i8)).setParentViewSize(Page.this.chatPreviewContainer.getMeasuredWidth(), Page.this.chatPreviewContainer.getBackgroundSizeY());
                    }
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = Page.this.textSelectionHelper;
                    if (chatListTextSelectionHelper2 != null) {
                        chatListTextSelectionHelper2.invalidate();
                    }
                }
            });
            this.chatListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.9
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public void onItemClick(View view2, int i6) {
                    Page page = Page.this;
                    if (page.currentTab != 1 || page.messages.previewMessages.size() <= 1) {
                        return;
                    }
                    int id = Page.this.messages.previewMessages.get(i6).getId();
                    boolean z2 = !Page.this.messages.selectedIds.get(id, false);
                    if (Page.this.messages.selectedIds.size() != 1 || z2) {
                        if (!z2) {
                            Page.this.messages.selectedIds.delete(id);
                        } else {
                            Page.this.messages.selectedIds.put(id, z2);
                        }
                        ((ChatMessageCell) view2).setChecked(z2, z2, true);
                        Page.this.updateSubtitle(true);
                    }
                }
            });
            RecyclerListView recyclerListView2 = this.chatListView;
            Adapter adapter = new Adapter();
            this.adapter = adapter;
            recyclerListView2.setAdapter(adapter);
            this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            10 r14 = new 10(context, 1000, 1, true, MessagePreviewView.this);
            this.chatLayoutManager = r14;
            r14.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.11
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i6) {
                    if (i6 < 0 || i6 >= Page.this.messages.previewMessages.size()) {
                        return 1000;
                    }
                    MessageObject messageObject = Page.this.messages.previewMessages.get(i6);
                    MessageObject.GroupedMessages validGroupedMessage = Page.this.getValidGroupedMessage(messageObject);
                    if (validGroupedMessage != null) {
                        return validGroupedMessage.positions.get(messageObject).spanSize;
                    }
                    return 1000;
                }
            });
            this.chatListView.setClipToPadding(false);
            this.chatListView.setLayoutManager(this.chatLayoutManager);
            this.chatListView.addItemDecoration(new RecyclerView.ItemDecoration(this, MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.12
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(android.graphics.Rect rect, View view2, RecyclerView recyclerView, RecyclerView.State state) {
                    ChatMessageCell chatMessageCell;
                    MessageObject.GroupedMessages currentMessagesGroup;
                    MessageObject.GroupedMessagePosition currentPosition;
                    int i6 = 0;
                    rect.bottom = 0;
                    if (!(view2 instanceof ChatMessageCell) || (currentMessagesGroup = (chatMessageCell = (ChatMessageCell) view2).getCurrentMessagesGroup()) == null || (currentPosition = chatMessageCell.getCurrentPosition()) == null || currentPosition.siblingHeights == null) {
                        return;
                    }
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    float max = Math.max(point.x, point.y) * 0.5f;
                    int extraInsetHeight = chatMessageCell.getExtraInsetHeight();
                    int i7 = 0;
                    while (true) {
                        float[] fArr = currentPosition.siblingHeights;
                        if (i7 >= fArr.length) {
                            break;
                        }
                        extraInsetHeight += (int) Math.ceil(fArr[i7] * max);
                        i7++;
                    }
                    int round = extraInsetHeight + ((currentPosition.maxY - currentPosition.minY) * Math.round(AndroidUtilities.density * 7.0f));
                    int size = currentMessagesGroup.posArray.size();
                    while (true) {
                        if (i6 < size) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition = currentMessagesGroup.posArray.get(i6);
                            byte b = groupedMessagePosition.minY;
                            byte b2 = currentPosition.minY;
                            if (b == b2 && ((groupedMessagePosition.minX != currentPosition.minX || groupedMessagePosition.maxX != currentPosition.maxX || b != b2 || groupedMessagePosition.maxY != currentPosition.maxY) && b == b2)) {
                                round -= ((int) Math.ceil(max * groupedMessagePosition.ph)) - AndroidUtilities.dp(4.0f);
                                break;
                            }
                            i6++;
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
            actionBarPopupWindowLayout.getSwipeBack().setOnForegroundOpenFinished(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    MessagePreviewView.Page.this.lambda$new$1();
                }
            });
            addView(this.menu, LayoutHelper.createFrame(-2, -2.0f));
            if (i == 0 && (messages = MessagePreviewView.this.messagePreviewParams.replyMessage) != null) {
                if (messages.hasText) {
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    if (MessagePreviewView.this.showOutdatedQuote) {
                        linearLayout = linearLayout2;
                    } else {
                        linearLayout = linearLayout2;
                        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(context, false, true, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
                        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$2(view2);
                            }
                        });
                        linearLayout.addView(actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
                        ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                        gapView.setTag(R.id.fit_width_tag, 1);
                        linearLayout.addView(gapView, LayoutHelper.createLinear(-1, 8));
                        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(context, false, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.QuoteSelectedPart), R.drawable.menu_quote_specific);
                        actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda3
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$3(view2);
                            }
                        });
                        linearLayout.addView(actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
                    }
                    this.menuBack = this.menu.addViewToSwipeBack(linearLayout);
                    this.menu.getSwipeBack().setStickToRight(true);
                    FrameLayout frameLayout = new FrameLayout(context);
                    f = 48.0f;
                    ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(this, context, true, true, false, MessagePreviewView.this.resourcesProvider, MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.13
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
                    this.quoteButton = actionBarMenuSubItem3;
                    actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString(MessagePreviewView.this.showOutdatedQuote ? R.string.QuoteSelectedPart : R.string.SelectSpecificQuote), R.drawable.menu_select_quote);
                    ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(this, context, true, true, false, MessagePreviewView.this.resourcesProvider, MessagePreviewView.this) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.14
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
                    this.clearQuoteButton = actionBarMenuSubItem4;
                    actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString(R.string.ClearQuote), R.drawable.menu_quote_delete);
                    frameLayout.setBackground(Theme.createRadSelectorDrawable(MessagePreviewView.this.getThemedColor(Theme.key_dialogButtonSelector), 6, 0));
                    frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda13
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$4(view2);
                        }
                    });
                    frameLayout.addView(this.quoteButton, LayoutHelper.createFrame(-1, 48.0f));
                    frameLayout.addView(this.clearQuoteButton, LayoutHelper.createFrame(-1, 48.0f));
                    this.menu.addView((View) frameLayout, LayoutHelper.createLinear(-1, 48));
                } else {
                    f = 48.0f;
                }
                if (MessagePreviewView.this.messagePreviewParams.noforwards) {
                    i4 = -1;
                } else {
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    ActionBarMenuSubItem actionBarMenuSubItem5 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    this.replyAnotherChatButton = actionBarMenuSubItem5;
                    String string = LocaleController.getString(R.string.ReplyToAnotherChat);
                    int i6 = R.drawable.msg_forward_replace;
                    actionBarMenuSubItem5.setTextAndIcon(string, i6);
                    this.replyAnotherChatButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda7
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$5(view2);
                        }
                    });
                    ActionBarMenuSubItem actionBarMenuSubItem6 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    this.quoteAnotherChatButton = actionBarMenuSubItem6;
                    actionBarMenuSubItem6.setTextAndIcon(LocaleController.getString(R.string.QuoteToAnotherChat), i6);
                    this.quoteAnotherChatButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda9
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$6(view2);
                        }
                    });
                    i4 = -1;
                    frameLayout2.addView(this.quoteAnotherChatButton, LayoutHelper.createFrame(-1, f));
                    frameLayout2.addView(this.replyAnotherChatButton, LayoutHelper.createFrame(-1, f));
                    this.menu.addView((View) frameLayout2, LayoutHelper.createLinear(-1, 48));
                }
                ActionBarPopupWindow.GapView gapView2 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                gapView2.setTag(R.id.fit_width_tag, 1);
                this.menu.addView((View) gapView2, LayoutHelper.createLinear(i4, 8));
                switchToQuote(MessagePreviewView.this.messagePreviewParams.quote != null, false);
                ActionBarMenuSubItem actionBarMenuSubItem7 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                actionBarMenuSubItem7.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                actionBarMenuSubItem7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda12
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MessagePreviewView.Page.this.lambda$new$7(view2);
                    }
                });
                this.menu.addView((View) actionBarMenuSubItem7, LayoutHelper.createLinear(-1, 48));
                ActionBarMenuSubItem actionBarMenuSubItem8 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                this.deleteReplyButton = actionBarMenuSubItem8;
                actionBarMenuSubItem8.setTextAndIcon(LocaleController.getString(MessagePreviewView.this.showOutdatedQuote ? R.string.DoNotQuote : R.string.DoNotReply), R.drawable.msg_delete);
                ActionBarMenuSubItem actionBarMenuSubItem9 = this.deleteReplyButton;
                int themedColor = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                int i7 = Theme.key_text_RedRegular;
                actionBarMenuSubItem9.setColors(themedColor, MessagePreviewView.this.getThemedColor(i7));
                this.deleteReplyButton.setSelectorColor(Theme.multAlpha(Theme.getColor(i7), 0.12f));
                this.deleteReplyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MessagePreviewView.Page.this.lambda$new$8(view2);
                    }
                });
                this.menu.addView((View) this.deleteReplyButton, LayoutHelper.createLinear(-1, 48));
            } else {
                if (i == 1) {
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    if (messagePreviewParams.forwardMessages != null) {
                        final ToggleButton toggleButton2 = new ToggleButton(MessagePreviewView.this, context, R.raw.name_hide, LocaleController.getString(messagePreviewParams.multipleUsers ? R.string.ShowSenderNames : R.string.ShowSendersName), R.raw.name_show, LocaleController.getString(MessagePreviewView.this.messagePreviewParams.multipleUsers ? R.string.HideSenderNames : R.string.HideSendersName));
                        this.menu.addView((View) toggleButton2, LayoutHelper.createLinear(-1, 48));
                        if (MessagePreviewView.this.messagePreviewParams.hasCaption) {
                            toggleButton = new ToggleButton(MessagePreviewView.this, context, R.raw.caption_hide, LocaleController.getString(R.string.ShowCaption), R.raw.caption_show, LocaleController.getString(R.string.HideCaption));
                            z = false;
                            toggleButton.setState(MessagePreviewView.this.messagePreviewParams.hideCaption, false);
                            this.menu.addView((View) toggleButton, LayoutHelper.createLinear(-1, 48));
                        } else {
                            z = false;
                            toggleButton = null;
                        }
                        ActionBarMenuSubItem actionBarMenuSubItem10 = new ActionBarMenuSubItem(context, true, z, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem10.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda6
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$9(view2);
                            }
                        });
                        actionBarMenuSubItem10.setTextAndIcon(LocaleController.getString(R.string.ChangeRecipient), R.drawable.msg_forward_replace);
                        this.menu.addView((View) actionBarMenuSubItem10, LayoutHelper.createLinear(-1, 48));
                        ActionBarPopupWindow.GapView gapView3 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                        gapView3.setTag(R.id.fit_width_tag, 1);
                        this.menu.addView((View) gapView3, LayoutHelper.createLinear(-1, 8));
                        ActionBarMenuSubItem actionBarMenuSubItem11 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem11.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                        actionBarMenuSubItem11.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda1
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$10(view2);
                            }
                        });
                        this.menu.addView((View) actionBarMenuSubItem11, LayoutHelper.createLinear(-1, 48));
                        ActionBarMenuSubItem actionBarMenuSubItem12 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                        actionBarMenuSubItem12.setTextAndIcon(LocaleController.getString(R.string.DoNotForward), R.drawable.msg_delete);
                        int themedColor2 = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                        int i8 = Theme.key_text_RedRegular;
                        actionBarMenuSubItem12.setColors(themedColor2, MessagePreviewView.this.getThemedColor(i8));
                        actionBarMenuSubItem12.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda10
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$11(view2);
                            }
                        });
                        actionBarMenuSubItem12.setSelectorColor(Theme.multAlpha(Theme.getColor(i8), 0.12f));
                        this.menu.addView((View) actionBarMenuSubItem12, LayoutHelper.createLinear(-1, 48));
                        toggleButton2.setState(MessagePreviewView.this.messagePreviewParams.hideForwardSendersName, false);
                        toggleButton2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda16
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                MessagePreviewView.Page.this.lambda$new$12(toggleButton, toggleButton2, view2);
                            }
                        });
                        if (toggleButton != null) {
                            toggleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda15
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view2) {
                                    MessagePreviewView.Page.this.lambda$new$13(toggleButton, toggleButton2, view2);
                                }
                            });
                        }
                    }
                }
                i2 = 2;
                if (i == 2 && MessagePreviewView.this.messagePreviewParams.linkMessage != null) {
                    ToggleButton toggleButton3 = new ToggleButton(MessagePreviewView.this, context, R.raw.position_below, LocaleController.getString(R.string.LinkAbove), R.raw.position_above, LocaleController.getString(R.string.LinkBelow));
                    this.changePositionBtn = toggleButton3;
                    toggleButton3.setState(!MessagePreviewView.this.messagePreviewParams.webpageTop, false);
                    this.menu.addView((View) this.changePositionBtn, LayoutHelper.createLinear(-1, 48));
                    ToggleButton toggleButton4 = new ToggleButton(MessagePreviewView.this, context, R.raw.media_shrink, LocaleController.getString(R.string.LinkMediaLarger), R.raw.media_enlarge, LocaleController.getString(R.string.LinkMediaSmaller));
                    this.changeSizeBtn = toggleButton4;
                    int i9 = 0;
                    toggleButton4.setState(MessagePreviewView.this.messagePreviewParams.webpageSmall, false);
                    ToggleButton toggleButton5 = this.changeSizeBtn;
                    MessagePreviewParams messagePreviewParams2 = MessagePreviewView.this.messagePreviewParams;
                    if (messagePreviewParams2.singleLink && !messagePreviewParams2.hasMedia) {
                        i9 = 8;
                    }
                    toggleButton5.setVisibility(i9);
                    this.changeSizeBtn.setAlpha(MessagePreviewView.this.messagePreviewParams.hasMedia ? 1.0f : 0.5f);
                    this.menu.addView((View) this.changeSizeBtn, LayoutHelper.createLinear(-1, 48));
                    ActionBarPopupWindow.GapView gapView4 = new ActionBarPopupWindow.GapView(context, MessagePreviewView.this.resourcesProvider);
                    gapView4.setTag(R.id.fit_width_tag, 1);
                    this.menu.addView((View) gapView4, LayoutHelper.createLinear(-1, 8));
                    ActionBarMenuSubItem actionBarMenuSubItem13 = new ActionBarMenuSubItem(context, true, false, false, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    actionBarMenuSubItem13.setTextAndIcon(LocaleController.getString(R.string.ApplyChanges), R.drawable.msg_select);
                    actionBarMenuSubItem13.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda14
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$14(view2);
                        }
                    });
                    this.menu.addView((View) actionBarMenuSubItem13, LayoutHelper.createLinear(-1, 48));
                    ActionBarMenuSubItem actionBarMenuSubItem14 = new ActionBarMenuSubItem(context, true, false, true, (Theme.ResourcesProvider) MessagePreviewView.this.resourcesProvider);
                    actionBarMenuSubItem14.setTextAndIcon(LocaleController.getString(R.string.DoNotLinkPreview), R.drawable.msg_delete);
                    int themedColor3 = MessagePreviewView.this.getThemedColor(Theme.key_text_RedBold);
                    int i10 = Theme.key_text_RedRegular;
                    actionBarMenuSubItem14.setColors(themedColor3, MessagePreviewView.this.getThemedColor(i10));
                    actionBarMenuSubItem14.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda11
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$15(view2);
                        }
                    });
                    actionBarMenuSubItem14.setSelectorColor(Theme.multAlpha(Theme.getColor(i10), 0.12f));
                    this.menu.addView((View) actionBarMenuSubItem14, LayoutHelper.createLinear(-1, 48));
                    this.changeSizeBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$16(view2);
                        }
                    });
                    this.changePositionBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            MessagePreviewView.Page.this.lambda$new$17(view2);
                        }
                    });
                }
                i3 = this.currentTab;
                if (i3 != 1) {
                    this.messages = MessagePreviewView.this.messagePreviewParams.forwardMessages;
                } else if (i3 == 0) {
                    this.messages = MessagePreviewView.this.messagePreviewParams.replyMessage;
                } else if (i3 == i2) {
                    this.messages = MessagePreviewView.this.messagePreviewParams.linkMessage;
                }
                TextSelectionHelper<Cell>.TextSelectionOverlay overlayView = this.textSelectionHelper.getOverlayView(context);
                this.textSelectionOverlay = overlayView;
                if (i5 >= 21) {
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
            }
            i2 = 2;
            i3 = this.currentTab;
            if (i3 != 1) {
            }
            TextSelectionHelper<Cell>.TextSelectionOverlay overlayView2 = this.textSelectionHelper.getOverlayView(context);
            this.textSelectionOverlay = overlayView2;
            if (i5 >= 21) {
            }
            view = this.textSelectionOverlay;
            if (view != null) {
            }
            this.textSelectionHelper.setParentView(this.chatListView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                MessagePreviewView.this.dismiss(true);
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 7 extends ChatListItemAnimator {
            Runnable finishRunnable;
            int scrollAnimationIndex;

            7(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider, MessagePreviewView messagePreviewView) {
                super(chatActivity, recyclerListView, resourcesProvider);
                this.scrollAnimationIndex = -1;
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

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.ChatListItemAnimator, androidx.recyclerview.widget.DefaultItemAnimator
            public void onAllAnimationsDone() {
                super.onAllAnimationsDone();
                Runnable runnable = this.finishRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                }
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda2
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
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagePreviewView.Page.7.this.lambda$onAllAnimationsDone$1();
                        }
                    });
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
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$7$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagePreviewView.Page.7.this.lambda$endAnimations$2();
                    }
                };
                this.finishRunnable = runnable2;
                AndroidUtilities.runOnUIThread(runnable2);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$endAnimations$2() {
                if (this.scrollAnimationIndex != -1) {
                    NotificationCenter.getInstance(MessagePreviewView.this.currentAccount).onAnimationFinish(this.scrollAnimationIndex);
                    this.scrollAnimationIndex = -1;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 10 extends GridLayoutManagerFixed {
            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            public boolean shouldLayoutChildFromOpositeSide(View view) {
                return false;
            }

            10(Context context, int i, int i2, boolean z, MessagePreviewView messagePreviewView) {
                super(context, i, i2, z);
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            protected boolean hasSiblingChild(int i) {
                byte b;
                MessageObject messageObject = Page.this.messages.previewMessages.get(i);
                MessageObject.GroupedMessages validGroupedMessage = Page.this.getValidGroupedMessage(messageObject);
                if (validGroupedMessage != null) {
                    MessageObject.GroupedMessagePosition groupedMessagePosition = validGroupedMessage.positions.get(messageObject);
                    if (groupedMessagePosition.minX != groupedMessagePosition.maxX && (b = groupedMessagePosition.minY) == groupedMessagePosition.maxY && b != 0) {
                        int size = validGroupedMessage.posArray.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition2 = validGroupedMessage.posArray.get(i2);
                            if (groupedMessagePosition2 != groupedMessagePosition) {
                                byte b2 = groupedMessagePosition2.minY;
                                byte b3 = groupedMessagePosition.minY;
                                if (b2 <= b3 && groupedMessagePosition2.maxY >= b3) {
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

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onLayoutChildren$0() {
                Page.this.adapter.notifyDataSetChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            switchToQuote(true, false);
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
            MessageObject replyMessage = getReplyMessage();
            if (replyMessage != null) {
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.textSelectionHelper;
                if (chatListTextSelectionHelper.selectionEnd - chatListTextSelectionHelper.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                    showQuoteLengthError();
                    return;
                }
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
                    int i = chatListTextSelectionHelper2.selectionStart;
                    messagePreviewParams2.quoteStart = i;
                    int i2 = chatListTextSelectionHelper2.selectionEnd;
                    messagePreviewParams2.quoteEnd = i2;
                    messagePreviewParams2.quote = ChatActivity.ReplyQuote.from(replyMessage, i, i2);
                    MessagePreviewView.this.onQuoteSelectedPart();
                    MessagePreviewView.this.dismiss(true);
                    return;
                }
                MessagePreviewView messagePreviewView2 = MessagePreviewView.this;
                MessagePreviewParams messagePreviewParams3 = messagePreviewView2.messagePreviewParams;
                messagePreviewParams3.quoteStart = 0;
                messagePreviewParams3.quoteEnd = Math.min(MessagesController.getInstance(messagePreviewView2.currentAccount).quoteLengthMax, replyMessage.messageOwner.message.length());
                MessagePreviewParams messagePreviewParams4 = MessagePreviewView.this.messagePreviewParams;
                messagePreviewParams4.quote = ChatActivity.ReplyQuote.from(replyMessage, messagePreviewParams4.quoteStart, messagePreviewParams4.quoteEnd);
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper3 = this.textSelectionHelper;
                ChatMessageCell replyMessageCell = getReplyMessageCell();
                MessagePreviewParams messagePreviewParams5 = MessagePreviewView.this.messagePreviewParams;
                chatListTextSelectionHelper3.select(replyMessageCell, messagePreviewParams5.quoteStart, messagePreviewParams5.quoteEnd);
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
            if (z) {
                if (!messagePreviewParams.hideForwardSendersName) {
                    messagePreviewParams.hideForwardSendersName = true;
                    messagePreviewView.returnSendersNames = true;
                }
            } else {
                if (messagePreviewView.returnSendersNames) {
                    messagePreviewParams.hideForwardSendersName = false;
                }
                messagePreviewView.returnSendersNames = false;
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
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$17(View view) {
            TLRPC$Message tLRPC$Message;
            TLRPC$Message tLRPC$Message2;
            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
            boolean z = !messagePreviewParams.webpageTop;
            messagePreviewParams.webpageTop = z;
            this.changePositionBtn.setState(!z, true);
            if (this.messages.messages.size() > 0 && (tLRPC$Message2 = this.messages.messages.get(0).messageOwner) != null) {
                tLRPC$Message2.invert_media = MessagePreviewView.this.messagePreviewParams.webpageTop;
            }
            if (this.messages.previewMessages.size() > 0 && (tLRPC$Message = this.messages.previewMessages.get(0).messageOwner) != null) {
                tLRPC$Message.invert_media = MessagePreviewView.this.messagePreviewParams.webpageTop;
            }
            updateMessages();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showQuoteLengthError() {
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            BulletinFactory.of(messagePreviewView, messagePreviewView.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.QuoteMaxError), LocaleController.getString(R.string.QuoteMaxErrorMessage)).show();
        }

        public void bind() {
            updateMessages();
            updateSubtitle(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSubtitle(boolean z) {
            String string;
            int i = this.currentTab;
            if (i != 1) {
                if (i != 0) {
                    if (i == 2) {
                        this.actionBar.setTitle(LocaleController.getString(R.string.MessageOptionsLinkTitle), z);
                        this.actionBar.setSubtitle(LocaleController.getString(R.string.MessageOptionsLinkSubtitle), z);
                        return;
                    }
                    return;
                }
                MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                if (messagePreviewParams.quote != null && messagePreviewParams.replyMessage.hasText) {
                    this.actionBar.setTitle(LocaleController.getString(R.string.PreviewQuoteUpdate), z);
                    this.actionBar.setSubtitle(LocaleController.getString(R.string.PreviewQuoteUpdateSubtitle), z);
                    return;
                }
                this.actionBar.setTitle(LocaleController.getString(R.string.MessageOptionsReplyTitle), z);
                this.actionBar.setSubtitle(MessagePreviewView.this.messagePreviewParams.replyMessage.hasText ? LocaleController.getString(R.string.MessageOptionsReplySubtitle) : "", z);
                return;
            }
            ActionBar actionBar = this.actionBar;
            MessagePreviewParams.Messages messages = MessagePreviewView.this.messagePreviewParams.forwardMessages;
            actionBar.setTitle(LocaleController.formatPluralString("PreviewForwardMessagesCount", messages == null ? 0 : messages.selectedIds.size(), new Object[0]), z);
            MessagePreviewView messagePreviewView = MessagePreviewView.this;
            MessagePreviewParams messagePreviewParams2 = messagePreviewView.messagePreviewParams;
            if (!messagePreviewParams2.hasSenders) {
                if (messagePreviewParams2.willSeeSenders) {
                    TLRPC$User tLRPC$User = messagePreviewView.currentUser;
                    if (tLRPC$User != null) {
                        string = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name));
                    } else if (ChatObject.isChannel(messagePreviewView.currentChat) && !MessagePreviewView.this.currentChat.megagroup) {
                        string = LocaleController.getString("ForwardPreviewSendersNameVisibleChannel", R.string.ForwardPreviewSendersNameVisibleChannel);
                    } else {
                        string = LocaleController.getString("ForwardPreviewSendersNameVisibleGroup", R.string.ForwardPreviewSendersNameVisibleGroup);
                    }
                } else {
                    TLRPC$User tLRPC$User2 = messagePreviewView.currentUser;
                    if (tLRPC$User2 != null) {
                        string = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name));
                    } else if (ChatObject.isChannel(messagePreviewView.currentChat) && !MessagePreviewView.this.currentChat.megagroup) {
                        string = LocaleController.getString(R.string.ForwardPreviewSendersNameHiddenChannel);
                    } else {
                        string = LocaleController.getString(R.string.ForwardPreviewSendersNameHiddenGroup);
                    }
                }
            } else if (!messagePreviewParams2.hideForwardSendersName) {
                TLRPC$User tLRPC$User3 = messagePreviewView.currentUser;
                if (tLRPC$User3 != null) {
                    string = LocaleController.formatString("ForwardPreviewSendersNameVisible", R.string.ForwardPreviewSendersNameVisible, ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name));
                } else if (ChatObject.isChannel(messagePreviewView.currentChat) && !MessagePreviewView.this.currentChat.megagroup) {
                    string = LocaleController.getString(R.string.ForwardPreviewSendersNameVisibleChannel);
                } else {
                    string = LocaleController.getString(R.string.ForwardPreviewSendersNameVisibleGroup);
                }
            } else {
                TLRPC$User tLRPC$User4 = messagePreviewView.currentUser;
                if (tLRPC$User4 != null) {
                    string = LocaleController.formatString("ForwardPreviewSendersNameHidden", R.string.ForwardPreviewSendersNameHidden, ContactsController.formatName(tLRPC$User4.first_name, tLRPC$User4.last_name));
                } else if (ChatObject.isChannel(messagePreviewView.currentChat) && !MessagePreviewView.this.currentChat.megagroup) {
                    string = LocaleController.getString(R.string.ForwardPreviewSendersNameHiddenChannel);
                } else {
                    string = LocaleController.getString(R.string.ForwardPreviewSendersNameHiddenGroup);
                }
            }
            this.actionBar.setSubtitle(string, z);
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
                if (!messagePreviewParams.hideForwardSendersName) {
                    messageObject.messageOwner.flags |= 4;
                    messageObject.hideSendersName = false;
                } else {
                    messageObject.messageOwner.flags &= -5;
                    messageObject.hideSendersName = true;
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

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            MessagePreviewView.this.isLandscapeMode = View.MeasureSpec.getSize(i) > View.MeasureSpec.getSize(i2);
            this.buttonsHeight = 0;
            this.menu.measure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 0));
            int i3 = this.buttonsHeight;
            int measuredHeight = this.menu.getMeasuredHeight();
            android.graphics.Rect rect = this.rect;
            this.buttonsHeight = Math.max(i3, measuredHeight + rect.top + rect.bottom);
            ((ViewGroup.MarginLayoutParams) this.chatListView.getLayoutParams()).topMargin = org.telegram.ui.ActionBar.ActionBar.getCurrentActionBarHeight();
            if (MessagePreviewView.this.isLandscapeMode) {
                this.chatPreviewContainer.getLayoutParams().height = -1;
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).topMargin = AndroidUtilities.dp(8.0f);
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).bottomMargin = AndroidUtilities.dp(8.0f);
                this.chatPreviewContainer.getLayoutParams().width = (int) Math.min(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(340.0f), View.MeasureSpec.getSize(i) * 0.6f));
                this.menu.getLayoutParams().height = -1;
            } else {
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).topMargin = 0;
                ((ViewGroup.MarginLayoutParams) this.chatPreviewContainer.getLayoutParams()).bottomMargin = 0;
                this.chatPreviewContainer.getLayoutParams().height = (View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(6.0f)) - this.buttonsHeight;
                if (this.chatPreviewContainer.getLayoutParams().height < View.MeasureSpec.getSize(i2) * 0.5f) {
                    this.chatPreviewContainer.getLayoutParams().height = (int) (View.MeasureSpec.getSize(i2) * 0.5f);
                }
                this.chatPreviewContainer.getLayoutParams().width = -1;
                this.menu.getLayoutParams().height = View.MeasureSpec.getSize(i2) - this.chatPreviewContainer.getLayoutParams().height;
            }
            int size = (View.MeasureSpec.getSize(i) + View.MeasureSpec.getSize(i2)) << 16;
            if (this.lastSize != size) {
                for (int i4 = 0; i4 < this.messages.previewMessages.size(); i4++) {
                    if (MessagePreviewView.this.isLandscapeMode) {
                        this.messages.previewMessages.get(i4).parentWidth = this.chatPreviewContainer.getLayoutParams().width;
                    } else {
                        this.messages.previewMessages.get(i4).parentWidth = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(16.0f);
                    }
                    this.messages.previewMessages.get(i4).resetLayout();
                    this.messages.previewMessages.get(i4).forceUpdate = true;
                    Adapter adapter = this.adapter;
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                MessagePreviewView.this.firstLayout = true;
            }
            this.lastSize = size;
            super.onMeasure(i, i2);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            updatePositions();
            MessagePreviewView.this.firstLayout = false;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            updateSelection();
        }

        public void updateSelection() {
            if (this.currentTab == 0) {
                TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = this.textSelectionHelper;
                if (chatListTextSelectionHelper.selectionEnd - chatListTextSelectionHelper.selectionStart > MessagesController.getInstance(MessagePreviewView.this.currentAccount).quoteLengthMax) {
                    return;
                }
                if (MessagePreviewView.this.messagePreviewParams.quote != null && this.textSelectionHelper.isInSelectionMode()) {
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper2 = this.textSelectionHelper;
                    messagePreviewParams.quoteStart = chatListTextSelectionHelper2.selectionStart;
                    messagePreviewParams.quoteEnd = chatListTextSelectionHelper2.selectionEnd;
                }
                this.textSelectionHelper.clear();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.currentTab == 0) {
                AndroidUtilities.forEachViews((RecyclerView) this.chatListView, (Consumer<View>) new Consumer() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda18
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        MessagePreviewView.Page.this.lambda$onAttachedToWindow$18((View) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAttachedToWindow$18(View view) {
            this.adapter.onViewAttachedToWindow(this.chatListView.getChildViewHolder(view));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePositions() {
            final int i = this.chatTopOffset;
            final float f = this.yOffset;
            if (!MessagePreviewView.this.isLandscapeMode) {
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
            } else {
                this.yOffset = 0.0f;
                this.chatTopOffset = 0;
                this.menu.setTranslationX(this.chatListView.getMeasuredWidth() + AndroidUtilities.dp(8.0f));
            }
            if (MessagePreviewView.this.firstLayout || (this.chatTopOffset == i && this.yOffset == f)) {
                if (MessagePreviewView.this.firstLayout) {
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
            MessagePreviewView.this.offsetsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.MessagePreviewView$Page$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    MessagePreviewView.Page.this.lambda$updatePositions$19(i, f, valueAnimator2);
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
        public /* synthetic */ void lambda$updatePositions$19(int i, float f, ValueAnimator valueAnimator) {
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
        /* loaded from: classes4.dex */
        public class Adapter extends RecyclerView.Adapter {
            private Adapter() {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                Context context = viewGroup.getContext();
                MessagePreviewView messagePreviewView = MessagePreviewView.this;
                ChatMessageCell chatMessageCell = new ChatMessageCell(context, false, messagePreviewView.sharedResources, messagePreviewView.resourcesProvider) { // from class: org.telegram.ui.Components.MessagePreviewView.Page.Adapter.1
                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.View, org.telegram.ui.Cells.TextSelectionHelper.SelectableView
                    public void invalidate() {
                        super.invalidate();
                        Page.this.chatListView.invalidate();
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.View
                    public void invalidate(int i2, int i3, int i4, int i5) {
                        super.invalidate(i2, i3, i4, i5);
                        Page.this.chatListView.invalidate();
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell
                    public void setMessageObject(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, boolean z, boolean z2) {
                        super.setMessageObject(messageObject, groupedMessages, z, z2);
                        Page.this.updateLinkHighlight(this);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.ViewGroup, android.view.View
                    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                        super.onLayout(z, i2, i3, i4, i5);
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
                    public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
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
                    public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i2);
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
                    public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, tLRPC$ReactionCount, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredClose() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this);
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
                    public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2);
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
                    public /* synthetic */ boolean needPlayMessage(MessageObject messageObject, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, messageObject, z);
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
                    public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowTopicButton() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void videoTimerReached() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                        return Page.this.textSelectionHelper;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean canPerformActions() {
                        Page page = Page.this;
                        return page.currentTab == 2 && !MessagePreviewView.this.messagePreviewParams.singleLink;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public void didPressUrl(ChatMessageCell chatMessageCell2, CharacterStyle characterStyle, boolean z) {
                        Page page = Page.this;
                        if (page.currentTab != 2 || MessagePreviewView.this.messagePreviewParams.currentLink == characterStyle || chatMessageCell2.getMessageObject() == null || !(characterStyle instanceof URLSpan)) {
                            return;
                        }
                        String url = ((URLSpan) characterStyle).getURL();
                        MessagePreviewView messagePreviewView2 = MessagePreviewView.this;
                        MessagePreviewParams messagePreviewParams = messagePreviewView2.messagePreviewParams;
                        messagePreviewParams.currentLink = characterStyle;
                        messagePreviewParams.webpage = null;
                        ChatActivity chatActivity = messagePreviewView2.chatActivity;
                        if (chatActivity != null && url != null) {
                            chatActivity.searchLinks(url, true);
                        }
                        Page.this.updateLinkHighlight(chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell2) {
                        Page page = Page.this;
                        if (page.currentTab == 2) {
                            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                            if (messagePreviewParams.singleLink) {
                                return null;
                            }
                            return messagePreviewParams.currentLink;
                        }
                        return null;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public boolean isProgressLoading(ChatMessageCell chatMessageCell2, int i2) {
                        Page page = Page.this;
                        if (page.currentTab == 2 && i2 == 1) {
                            MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                            if (messagePreviewParams.singleLink) {
                                return false;
                            }
                            TLRPC$WebPage tLRPC$WebPage = messagePreviewParams.webpage;
                            return tLRPC$WebPage == null || (tLRPC$WebPage instanceof TLRPC$TL_webPagePending);
                        }
                        return false;
                    }
                });
                return new RecyclerListView.Holder(chatMessageCell);
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
                if (Page.this.messages.previewMessages.size() > 1) {
                    chatMessageCell.setCheckBoxVisible(Page.this.currentTab == 1, false);
                    boolean z = id == Page.this.messages.previewMessages.get(i).getId();
                    MessagePreviewParams.Messages messages3 = Page.this.messages;
                    boolean z2 = messages3.selectedIds.get(messages3.previewMessages.get(i).getId(), false);
                    chatMessageCell.setChecked(z2, z2, z);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
                Page page = Page.this;
                if (page.messages == null) {
                    return;
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) viewHolder.itemView;
                if (page.currentTab == 0) {
                    MessageObject.GroupedMessages validGroupedMessage = page.getValidGroupedMessage(chatMessageCell.getMessageObject());
                    chatMessageCell.setDrawSelectionBackground(validGroupedMessage == null);
                    chatMessageCell.setChecked(true, validGroupedMessage == null, false);
                    Page page2 = Page.this;
                    if (MessagePreviewView.this.messagePreviewParams.quote == null || page2.textSelectionHelper.isInSelectionMode()) {
                        return;
                    }
                    Page page3 = Page.this;
                    TextSelectionHelper.ChatListTextSelectionHelper chatListTextSelectionHelper = page3.textSelectionHelper;
                    MessagePreviewParams messagePreviewParams = MessagePreviewView.this.messagePreviewParams;
                    chatListTextSelectionHelper.select(chatMessageCell, messagePreviewParams.quoteStart, messagePreviewParams.quoteEnd);
                    return;
                }
                chatMessageCell.setDrawSelectionBackground(false);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                MessagePreviewParams.Messages messages = Page.this.messages;
                if (messages == null) {
                    return 0;
                }
                return messages.previewMessages.size();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MessageObject.GroupedMessages getValidGroupedMessage(MessageObject messageObject) {
            if (messageObject.getGroupId() != 0) {
                MessageObject.GroupedMessages groupedMessages = this.messages.groupedMessagesMap.get(messageObject.getGroupId());
                if (groupedMessages == null || (groupedMessages.messages.size() > 1 && groupedMessages.positions.get(messageObject) != null)) {
                    return groupedMessages;
                }
                return null;
            }
            return null;
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public MessagePreviewView(final Context context, ChatActivity chatActivity, MessagePreviewParams messagePreviewParams, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, int i, ResourcesDelegate resourcesDelegate, int i2, final boolean z) {
        super(context);
        this.firstLayout = true;
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
        this.drawingGroups = new ArrayList<>(10);
        this.showOutdatedQuote = z;
        this.chatActivity = chatActivity;
        this.sharedResources = new ChatMessageSharedResources(context);
        this.currentAccount = i;
        this.currentUser = tLRPC$User;
        this.currentChat = tLRPC$Chat;
        this.messagePreviewParams = messagePreviewParams;
        this.resourcesProvider = resourcesDelegate;
        this.viewPager = new ViewPagerFixed(context, resourcesDelegate) { // from class: org.telegram.ui.Components.MessagePreviewView.2
            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onTabAnimationUpdate(boolean z2) {
                MessagePreviewView messagePreviewView = MessagePreviewView.this;
                messagePreviewView.tabsView.setSelectedTab(messagePreviewView.viewPager.getPositionAnimated());
                View[] viewArr = this.viewPages;
                if (viewArr[0] instanceof Page) {
                    ((Page) viewArr[0]).textSelectionHelper.onParentScrolled();
                }
                View[] viewArr2 = this.viewPages;
                if (viewArr2[1] instanceof Page) {
                    ((Page) viewArr2[1]).textSelectionHelper.onParentScrolled();
                }
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onScrollEnd() {
                View[] viewArr = this.viewPages;
                if (viewArr[0] instanceof Page) {
                    ((Page) viewArr[0]).textSelectionHelper.stopScrolling();
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
            } else {
                if (i4 == 2 && messagePreviewParams.linkMessage != null && !z) {
                    this.tabsView.addTab(2, LocaleController.getString(R.string.MessageOptionsLink));
                }
            }
            if (i4 == i2) {
                i3 = this.tabsView.tabs.size() - 1;
            }
        }
        this.viewPager.setAdapter(new ViewPagerFixed.Adapter() { // from class: org.telegram.ui.Components.MessagePreviewView.3
            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemCount() {
                return MessagePreviewView.this.tabsView.tabs.size();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public int getItemViewType(int i5) {
                return MessagePreviewView.this.tabsView.tabs.get(i5).id;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public View createView(int i5) {
                return new Page(context, i5);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
            public void bindView(View view, int i5, int i6) {
                ((Page) view).bind();
            }
        });
        this.viewPager.setPosition(i3);
        this.tabsView.setSelectedTab(i3);
        addView(this.tabsView, LayoutHelper.createFrame(-1, 66, 87));
        addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 66.0f));
        this.tabsView.setOnTabClick(new Utilities.Callback() { // from class: org.telegram.ui.Components.MessagePreviewView$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MessagePreviewView.this.lambda$new$0((Integer) obj);
            }
        });
        setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.MessagePreviewView$$ExternalSyntheticLambda0
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
    public /* synthetic */ void lambda$new$0(Integer num) {
        if (this.tabsView.tabs.get(this.viewPager.getCurrentPosition()).id == num.intValue()) {
            return;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= this.tabsView.tabs.size()) {
                break;
            } else if (this.tabsView.tabs.get(i2).id == num.intValue()) {
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

    public void dismiss(boolean z) {
        if (this.showing) {
            int i = 0;
            this.showing = false;
            animate().alpha(0.0f).scaleX(0.95f).scaleY(0.95f).setDuration(250L).setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MessagePreviewView.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (MessagePreviewView.this.getParent() != null) {
                        ((ViewGroup) MessagePreviewView.this.getParent()).removeView(MessagePreviewView.this);
                    }
                    MessagePreviewView.this.onFullDismiss();
                }
            });
            while (true) {
                View[] viewArr = this.viewPager.viewPages;
                if (i >= viewArr.length) {
                    break;
                }
                if (viewArr[i] instanceof Page) {
                    Page page = (Page) viewArr[i];
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class TabsView extends View {
        private final Paint bgPaint;
        private int color;
        private float marginBetween;
        private Utilities.Callback<Integer> onTabClick;
        private final Theme.ResourcesProvider resourcesProvider;
        private RectF selectRect;
        private int selectedColor;
        private float selectedTab;
        private float tabInnerPadding;
        public final ArrayList<Tab> tabs;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Tab {
            final RectF bounds = new RectF();
            final RectF clickBounds = new RectF();
            final int id;
            final Text text;

            public Tab(int i, String str) {
                this.id = i;
                this.text = new Text(str, 14, AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            }
        }

        public TabsView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            int[] colors;
            this.tabs = new ArrayList<>();
            Paint paint = new Paint(1);
            this.bgPaint = paint;
            this.tabInnerPadding = AndroidUtilities.dp(12.0f);
            this.marginBetween = AndroidUtilities.dp(13.0f);
            this.selectRect = new RectF();
            this.resourcesProvider = resourcesProvider;
            if (Theme.isCurrentThemeDark()) {
                this.color = -1862270977;
                this.selectedColor = -1325400065;
                paint.setColor(285212671);
                return;
            }
            int color = Theme.getColor(Theme.key_chat_wallpaper, resourcesProvider);
            if (resourcesProvider instanceof ChatActivity.ThemeDelegate) {
                ChatActivity.ThemeDelegate themeDelegate = (ChatActivity.ThemeDelegate) resourcesProvider;
                if ((themeDelegate.getWallpaperDrawable() instanceof MotionBackgroundDrawable) && (colors = ((MotionBackgroundDrawable) themeDelegate.getWallpaperDrawable()).getColors()) != null) {
                    color = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(colors[0], colors[1]), AndroidUtilities.getAverageColor(colors[2], colors[3]));
                }
            }
            this.color = Theme.adaptHue(-1606201797, color);
            this.selectedColor = Theme.adaptHue(-448573893, color);
            paint.setColor(Theme.adaptHue(814980216, color));
        }

        public void addTab(int i, String str) {
            this.tabs.add(new Tab(i, str));
        }

        public void setSelectedTab(float f) {
            this.selectedTab = f;
            invalidate();
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
                f += this.tabInnerPadding + this.tabs.get(i3).text.getWidth() + this.tabInnerPadding;
            }
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            float dp = (measuredHeight - AndroidUtilities.dp(26.0f)) / 2.0f;
            float dp2 = (measuredHeight + AndroidUtilities.dp(26.0f)) / 2.0f;
            float f2 = (measuredWidth - f) / 2.0f;
            for (int i4 = 0; i4 < this.tabs.size(); i4++) {
                float width = this.tabInnerPadding + this.tabs.get(i4).text.getWidth() + this.tabInnerPadding;
                this.tabs.get(i4).bounds.set(f2, dp, f2 + width, dp2);
                this.tabs.get(i4).clickBounds.set(this.tabs.get(i4).bounds);
                this.tabs.get(i4).clickBounds.inset((-this.marginBetween) / 2.0f, -dp);
                f2 += width + this.marginBetween;
            }
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
                AndroidUtilities.lerp(this.tabs.get(floor).bounds, this.tabs.get(ceil).bounds, f - floor, this.selectRect);
            } else if (z2) {
                this.selectRect.set(this.tabs.get(floor).bounds);
            } else if (z) {
                this.selectRect.set(this.tabs.get(ceil).bounds);
            }
            if (z2 || z) {
                canvas.drawRoundRect(this.selectRect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), this.bgPaint);
            }
            for (int i = 0; i < this.tabs.size(); i++) {
                Tab tab = this.tabs.get(i);
                tab.text.draw(canvas, tab.bounds.left + this.tabInnerPadding, getMeasuredHeight() / 2.0f, ColorUtils.blendARGB(this.color, this.selectedColor, 1.0f - Math.abs(f - i)), 1.0f);
            }
        }

        public void setOnTabClick(Utilities.Callback<Integer> callback) {
            this.onTabClick = callback;
        }

        @Override // android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            Utilities.Callback<Integer> callback;
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

        private int getHitTab(float f, float f2) {
            for (int i = 0; i < this.tabs.size(); i++) {
                if (this.tabs.get(i).clickBounds.contains(f, f2)) {
                    return this.tabs.get(i).id;
                }
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ActionBar extends FrameLayout {
        private Theme.ResourcesProvider resourcesProvider;
        private final AnimatedTextView.AnimatedTextDrawable subtitle;
        private final AnimatedTextView.AnimatedTextDrawable title;

        public ActionBar(MessagePreviewView messagePreviewView, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.title = animatedTextDrawable;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            animatedTextDrawable.setAnimationProperties(0.3f, 0L, 430L, cubicBezierInterpolator);
            animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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

        public void setTitle(CharSequence charSequence, boolean z) {
            this.title.setText(charSequence, z && !LocaleController.isRTL);
        }

        public void setSubtitle(CharSequence charSequence, boolean z) {
            this.subtitle.setText(charSequence, z && !LocaleController.isRTL);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
            setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return this.title == drawable || this.subtitle == drawable || super.verifyDrawable(drawable);
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
    }

    public void updateLink() {
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            if (viewArr[i] != null && ((Page) viewArr[i]).currentTab == 2) {
                Page page = (Page) viewArr[i];
                ToggleButton toggleButton = page.changeSizeBtn;
                MessagePreviewParams messagePreviewParams = this.messagePreviewParams;
                toggleButton.setVisibility((!messagePreviewParams.singleLink || messagePreviewParams.hasMedia) ? 0 : 8);
                page.changeSizeBtn.animate().alpha(this.messagePreviewParams.hasMedia ? 1.0f : 0.5f).start();
                page.changeSizeBtn.setState(this.messagePreviewParams.webpageSmall, true);
                page.changePositionBtn.setState(!this.messagePreviewParams.webpageTop, true);
                page.updateMessages();
            }
            i++;
        }
    }

    public void updateAll() {
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            if (viewArr[i] instanceof Page) {
                Page page = (Page) viewArr[i];
                int i2 = page.currentTab;
                if (i2 == 1) {
                    page.messages = this.messagePreviewParams.forwardMessages;
                } else if (i2 == 0) {
                    page.messages = this.messagePreviewParams.replyMessage;
                } else if (i2 == 2) {
                    page.messages = this.messagePreviewParams.linkMessage;
                }
                page.updateMessages();
                if (page.currentTab == 0) {
                    if (this.showOutdatedQuote) {
                        MessageObject replyMessage = page.getReplyMessage();
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
                    } else {
                        this.messagePreviewParams.quote = null;
                        page.textSelectionHelper.clear();
                        page.switchToQuote(false, true);
                    }
                    page.updateSubtitle(true);
                }
                ToggleButton toggleButton = page.changeSizeBtn;
                if (toggleButton != null) {
                    toggleButton.animate().alpha(this.messagePreviewParams.hasMedia ? 1.0f : 0.5f).start();
                }
            }
            i++;
        }
    }

    public boolean isTouchedHandle() {
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPager.viewPages;
            if (i >= viewArr.length) {
                return false;
            }
            if (viewArr[i] != null && ((Page) viewArr[i]).currentTab == 0) {
                return ((Page) viewArr[i]).textSelectionHelper.isTouched();
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ToggleButton extends View {
        private boolean first;
        RLottieToggleDrawable iconDrawable;
        private boolean isState1;
        final int minWidth;
        final String text1;
        final String text2;
        AnimatedTextView.AnimatedTextDrawable textDrawable;

        public ToggleButton(MessagePreviewView messagePreviewView, Context context, int i, String str, int i2, String str2) {
            super(context);
            this.first = true;
            this.text1 = str;
            this.text2 = str2;
            setBackground(Theme.createSelectorDrawable(messagePreviewView.getThemedColor(Theme.key_listSelector), 2));
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.textDrawable = animatedTextDrawable;
            animatedTextDrawable.setAnimationProperties(0.35f, 0L, 300L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.textDrawable.setTextSize(AndroidUtilities.dp(16.0f));
            this.textDrawable.setTextColor(messagePreviewView.getThemedColor(Theme.key_actionBarDefaultSubmenuItem));
            this.textDrawable.setCallback(this);
            this.textDrawable.setEllipsizeByGradient(true ^ LocaleController.isRTL);
            int dp = (int) (AndroidUtilities.dp(77.0f) + Math.max(this.textDrawable.getPaint().measureText(str), this.textDrawable.getPaint().measureText(str2)));
            this.minWidth = dp;
            this.textDrawable.setOverrideFullWidth(dp);
            RLottieToggleDrawable rLottieToggleDrawable = new RLottieToggleDrawable(messagePreviewView, this, i, i2);
            this.iconDrawable = rLottieToggleDrawable;
            rLottieToggleDrawable.setColorFilter(new PorterDuffColorFilter(messagePreviewView.getThemedColor(Theme.key_actionBarDefaultSubmenuItemIcon), PorterDuff.Mode.SRC_IN));
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

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int min;
            int mode = View.MeasureSpec.getMode(i);
            if (mode == 1073741824) {
                min = Math.max(View.MeasureSpec.getSize(i), this.minWidth);
            } else {
                min = Math.min(View.MeasureSpec.getSize(i), this.minWidth);
            }
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, mode), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class RLottieToggleDrawable extends Drawable {
        private RLottieDrawable currentState;
        private boolean detached;
        private boolean isState1;
        private RLottieDrawable state1;
        private RLottieDrawable state2;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        public RLottieToggleDrawable(MessagePreviewView messagePreviewView, View view, int i, int i2) {
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

        public void setState(boolean z, boolean z2) {
            this.isState1 = z;
            if (z2) {
                this.currentState = z ? this.state1 : this.state2;
                this.state1.setCurrentFrame(0);
                this.state2.setCurrentFrame(0);
                this.currentState.start();
                return;
            }
            RLottieDrawable rLottieDrawable = z ? this.state1 : this.state2;
            this.currentState = rLottieDrawable;
            rLottieDrawable.setCurrentFrame(rLottieDrawable.getFramesCount() - 1);
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
        public void setAlpha(int i) {
            this.state1.setAlpha(i);
            this.state2.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.state1.setColorFilter(colorFilter);
            this.state2.setColorFilter(colorFilter);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(24.0f);
        }
    }
}
