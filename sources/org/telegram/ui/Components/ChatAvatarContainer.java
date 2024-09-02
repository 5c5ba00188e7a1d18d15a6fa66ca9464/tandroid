package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$ChatParticipants;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$TL_channelFull;
import org.telegram.tgnet.TLRPC$TL_chatFull;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.BusinessLinksController;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AutoDeletePopupWrapper;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.TopicsFragment;
/* loaded from: classes3.dex */
public class ChatAvatarContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    public boolean allowDrawStories;
    public boolean allowShorterStatus;
    private AnimatedTextView animatedSubtitleTextView;
    private AvatarDrawable avatarDrawable;
    public BackupImageView avatarImageView;
    private ButtonBounce bounce;
    private int currentAccount;
    private int currentConnectionState;
    StatusDrawable currentTypingDrawable;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emojiStatusDrawable;
    public boolean ignoreTouches;
    private boolean[] isOnline;
    private int largerWidth;
    private CharSequence lastSubtitle;
    private int lastSubtitleColorKey;
    private int lastWidth;
    private int leftPadding;
    private boolean occupyStatusBar;
    private Runnable onLongClick;
    private int onlineCount;
    private Integer overrideSubtitleColor;
    private ChatActivity parentFragment;
    public boolean premiumIconHiddable;
    private boolean pressed;
    private Theme.ResourcesProvider resourcesProvider;
    private int rightAvatarPadding;
    private String rightDrawable2ContentDescription;
    private String rightDrawableContentDescription;
    private boolean rightDrawableIsScamOrVerified;
    private boolean secretChatTimer;
    private SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
    private ImageView starBgItem;
    private ImageView starFgItem;
    public boolean stars;
    private StatusDrawable[] statusDrawables;
    public boolean[] statusMadeShorter;
    private Integer storiesForceState;
    private AtomicReference subtitleTextLargerCopyView;
    private SimpleTextView subtitleTextView;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private AnimatorSet titleAnimation;
    private AtomicReference titleTextLargerCopyView;
    private SimpleTextView titleTextView;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends BackupImageView {
        StoriesUtilities.AvatarStoryParams params;
        final /* synthetic */ boolean val$avatarClickable;
        final /* synthetic */ BaseFragment val$baseFragment;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 1 extends StoriesUtilities.AvatarStoryParams {
            1(boolean z) {
                super(z);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ boolean lambda$openStory$0(long j, int i, int i2, int i3, StoryViewer.TransitionViewHolder transitionViewHolder) {
                1 r1 = 1.this;
                ImageReceiver imageReceiver = r1.imageReceiver;
                transitionViewHolder.storyImage = imageReceiver;
                transitionViewHolder.crossfadeToAvatarImage = imageReceiver;
                transitionViewHolder.params = r1.params;
                BackupImageView backupImageView = ChatAvatarContainer.this.avatarImageView;
                transitionViewHolder.view = backupImageView;
                transitionViewHolder.alpha = backupImageView.getAlpha();
                transitionViewHolder.clipTop = 0.0f;
                transitionViewHolder.clipBottom = AndroidUtilities.displaySize.y;
                transitionViewHolder.clipParent = (View) 1.this.getParent();
                return true;
            }

            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void openStory(long j, Runnable runnable) {
                1.this.val$baseFragment.getOrCreateStoryViewer().open(1.this.getContext(), j, new StoryViewer.PlaceProvider() { // from class: org.telegram.ui.Components.ChatAvatarContainer$1$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
                    public final boolean findView(long j2, int i, int i2, int i3, StoryViewer.TransitionViewHolder transitionViewHolder) {
                        boolean lambda$openStory$0;
                        lambda$openStory$0 = ChatAvatarContainer.1.1.this.lambda$openStory$0(j2, i, i2, i3, transitionViewHolder);
                        return lambda$openStory$0;
                    }

                    @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
                    public /* synthetic */ void loadNext(boolean z) {
                        StoryViewer.PlaceProvider.-CC.$default$loadNext(this, z);
                    }

                    @Override // org.telegram.ui.Stories.StoryViewer.PlaceProvider
                    public /* synthetic */ void preLayout(long j2, int i, Runnable runnable2) {
                        runnable2.run();
                    }
                });
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        1(Context context, BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.val$baseFragment = baseFragment;
            this.val$avatarClickable = z;
            this.val$resourcesProvider = resourcesProvider;
            this.params = new 1(true);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            long dialogId;
            if (!ChatAvatarContainer.this.allowDrawStories || this.animatedEmojiDrawable != null) {
                super.onDraw(canvas);
                return;
            }
            this.params.originalAvatarRect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            StoriesUtilities.AvatarStoryParams avatarStoryParams = this.params;
            avatarStoryParams.drawSegments = true;
            avatarStoryParams.drawInside = true;
            avatarStoryParams.resourcesProvider = this.val$resourcesProvider;
            if (ChatAvatarContainer.this.storiesForceState != null) {
                this.params.forceState = ChatAvatarContainer.this.storiesForceState.intValue();
            }
            if (ChatAvatarContainer.this.parentFragment != null) {
                dialogId = ChatAvatarContainer.this.parentFragment.getDialogId();
            } else {
                BaseFragment baseFragment = this.val$baseFragment;
                dialogId = baseFragment instanceof TopicsFragment ? ((TopicsFragment) baseFragment).getDialogId() : 0L;
            }
            StoriesUtilities.drawAvatarWithStory(dialogId, canvas, this.imageReceiver, this.params);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (!this.val$avatarClickable || !getImageReceiver().hasNotThumb()) {
                accessibilityNodeInfo.setVisibleToUser(false);
                return;
            }
            accessibilityNodeInfo.setText(LocaleController.getString(R.string.AccDescrProfilePicture));
            if (Build.VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.Open)));
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (ChatAvatarContainer.this.allowDrawStories && this.params.checkOnTouchEvent(motionEvent, this)) {
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* loaded from: classes3.dex */
    private class SimpleTextConnectedView extends SimpleTextView {
        private AtomicReference reference;

        public SimpleTextConnectedView(Context context, AtomicReference atomicReference) {
            super(context);
            this.reference = atomicReference;
        }

        @Override // org.telegram.ui.ActionBar.SimpleTextView
        public boolean setText(CharSequence charSequence) {
            SimpleTextView simpleTextView;
            AtomicReference atomicReference = this.reference;
            if (atomicReference != null && (simpleTextView = (SimpleTextView) atomicReference.get()) != null) {
                simpleTextView.setText(charSequence);
            }
            return super.setText(charSequence);
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            SimpleTextView simpleTextView;
            AtomicReference atomicReference = this.reference;
            if (atomicReference != null && (simpleTextView = (SimpleTextView) atomicReference.get()) != null) {
                simpleTextView.setTranslationY(f);
            }
            super.setTranslationY(f);
        }
    }

    public ChatAvatarContainer(Context context, BaseFragment baseFragment, boolean z) {
        this(context, baseFragment, z, null);
    }

    public ChatAvatarContainer(Context context, BaseFragment baseFragment, boolean z, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        View view;
        ImageView imageView;
        int i;
        this.titleTextLargerCopyView = new AtomicReference();
        this.subtitleTextLargerCopyView = new AtomicReference();
        this.statusDrawables = new StatusDrawable[6];
        this.avatarDrawable = new AvatarDrawable();
        this.currentAccount = UserConfig.selectedAccount;
        this.occupyStatusBar = true;
        this.leftPadding = AndroidUtilities.dp(8.0f);
        this.rightAvatarPadding = 0;
        this.lastWidth = -1;
        this.largerWidth = -1;
        this.isOnline = new boolean[1];
        this.statusMadeShorter = new boolean[1];
        this.onlineCount = -1;
        this.lastSubtitleColorKey = -1;
        this.allowShorterStatus = false;
        this.premiumIconHiddable = false;
        this.bounce = new ButtonBounce(this);
        this.onLongClick = new Runnable() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ChatAvatarContainer.this.lambda$new$3();
            }
        };
        this.rightDrawableIsScamOrVerified = false;
        this.rightDrawableContentDescription = null;
        this.rightDrawable2ContentDescription = null;
        this.resourcesProvider = resourcesProvider;
        boolean z2 = baseFragment instanceof ChatActivity;
        if (z2) {
            this.parentFragment = (ChatActivity) baseFragment;
        }
        ChatActivity chatActivity = this.parentFragment;
        boolean z3 = (chatActivity == null || chatActivity.getChatMode() != 0 || UserObject.isReplyUser(this.parentFragment.getCurrentUser())) ? false : true;
        this.avatarImageView = new 1(context, baseFragment, z3, resourcesProvider);
        if (z2 || (baseFragment instanceof TopicsFragment)) {
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 == null || (chatActivity2.getChatMode() != 5 && this.parentFragment.getChatMode() != 6)) {
                this.sharedMediaPreloader = new SharedMediaLayout.SharedMediaPreloader(baseFragment);
            }
            ChatActivity chatActivity3 = this.parentFragment;
            if (chatActivity3 != null && (chatActivity3.isThreadChat() || this.parentFragment.getChatMode() == 2 || this.parentFragment.getChatMode() == 5 || this.parentFragment.getChatMode() == 6)) {
                this.avatarImageView.setVisibility(8);
            }
        }
        this.avatarImageView.setContentDescription(LocaleController.getString(R.string.AccDescrProfilePicture));
        this.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0f));
        addView(this.avatarImageView);
        if (z3) {
            this.avatarImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatAvatarContainer.this.lambda$new$0(view2);
                }
            });
        }
        SimpleTextConnectedView simpleTextConnectedView = new SimpleTextConnectedView(context, this.titleTextLargerCopyView);
        this.titleTextView = simpleTextConnectedView;
        simpleTextConnectedView.setEllipsizeByGradient(true);
        this.titleTextView.setTextColor(getThemedColor(Theme.key_actionBarDefaultTitle));
        this.titleTextView.setTextSize(18);
        this.titleTextView.setGravity(3);
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        this.titleTextView.setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
        this.titleTextView.setCanHideRightDrawable(false);
        this.titleTextView.setRightDrawableOutside(true);
        this.titleTextView.setPadding(0, AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(12.0f));
        addView(this.titleTextView);
        if (useAnimatedSubtitle()) {
            AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, true);
            this.animatedSubtitleTextView = animatedTextView;
            animatedTextView.setAnimationProperties(0.3f, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.animatedSubtitleTextView.setEllipsizeByGradient(true);
            AnimatedTextView animatedTextView2 = this.animatedSubtitleTextView;
            int i2 = Theme.key_actionBarDefaultSubtitle;
            animatedTextView2.setTextColor(getThemedColor(i2));
            this.animatedSubtitleTextView.setTag(Integer.valueOf(i2));
            this.animatedSubtitleTextView.setTextSize(AndroidUtilities.dp(14.0f));
            this.animatedSubtitleTextView.setGravity(3);
            this.animatedSubtitleTextView.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
            this.animatedSubtitleTextView.setTranslationY(-AndroidUtilities.dp(1.0f));
            view = this.animatedSubtitleTextView;
        } else {
            SimpleTextConnectedView simpleTextConnectedView2 = new SimpleTextConnectedView(context, this.subtitleTextLargerCopyView);
            this.subtitleTextView = simpleTextConnectedView2;
            simpleTextConnectedView2.setEllipsizeByGradient(true);
            SimpleTextView simpleTextView = this.subtitleTextView;
            int i3 = Theme.key_actionBarDefaultSubtitle;
            simpleTextView.setTextColor(getThemedColor(i3));
            this.subtitleTextView.setTag(Integer.valueOf(i3));
            this.subtitleTextView.setTextSize(14);
            this.subtitleTextView.setGravity(3);
            this.subtitleTextView.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
            view = this.subtitleTextView;
        }
        addView(view);
        if (this.parentFragment != null) {
            ImageView imageView2 = new ImageView(context);
            this.timeItem = imageView2;
            imageView2.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
            this.timeItem.setScaleType(ImageView.ScaleType.CENTER);
            this.timeItem.setAlpha(0.0f);
            this.timeItem.setScaleY(0.0f);
            this.timeItem.setScaleX(0.0f);
            this.timeItem.setVisibility(8);
            ImageView imageView3 = this.timeItem;
            TimerDrawable timerDrawable = new TimerDrawable(context, resourcesProvider);
            this.timerDrawable = timerDrawable;
            imageView3.setImageDrawable(timerDrawable);
            addView(this.timeItem);
            this.secretChatTimer = z;
            this.timeItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatAvatarContainer.this.lambda$new$1(resourcesProvider, view2);
                }
            });
            if (this.secretChatTimer) {
                imageView = this.timeItem;
                i = R.string.SetTimer;
            } else {
                imageView = this.timeItem;
                i = R.string.AccAutoDeleteTimer;
            }
            imageView.setContentDescription(LocaleController.getString(i));
            ImageView imageView4 = new ImageView(context);
            this.starBgItem = imageView4;
            imageView4.setImageResource(R.drawable.star_small_outline);
            this.starBgItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_actionBarDefault), PorterDuff.Mode.SRC_IN));
            this.starBgItem.setAlpha(0.0f);
            this.starBgItem.setScaleY(0.0f);
            this.starBgItem.setScaleX(0.0f);
            addView(this.starBgItem);
            ImageView imageView5 = new ImageView(context);
            this.starFgItem = imageView5;
            imageView5.setImageResource(R.drawable.star_small_inner);
            this.starFgItem.setAlpha(0.0f);
            this.starFgItem.setScaleY(0.0f);
            this.starFgItem.setScaleX(0.0f);
            addView(this.starFgItem);
        }
        ChatActivity chatActivity4 = this.parentFragment;
        if (chatActivity4 != null && (chatActivity4.getChatMode() == 0 || this.parentFragment.getChatMode() == 3)) {
            if ((!this.parentFragment.isThreadChat() || this.parentFragment.isTopic) && !UserObject.isReplyUser(this.parentFragment.getCurrentUser())) {
                setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatAvatarContainer.this.lambda$new$2(view2);
                    }
                });
            }
            TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
            this.statusDrawables[0] = new TypingDotsDrawable(true);
            this.statusDrawables[1] = new RecordStatusDrawable(true);
            this.statusDrawables[2] = new SendingFileDrawable(true);
            this.statusDrawables[3] = new PlayingGameDrawable(false, resourcesProvider);
            this.statusDrawables[4] = new RoundStatusDrawable(true);
            this.statusDrawables[5] = new ChoosingStickerStatusDrawable(true);
            int i4 = 0;
            while (true) {
                StatusDrawable[] statusDrawableArr = this.statusDrawables;
                if (i4 >= statusDrawableArr.length) {
                    break;
                }
                statusDrawableArr[i4].setIsChat(currentChat != null);
                i4++;
            }
        }
        this.emojiStatusDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.titleTextView, AndroidUtilities.dp(24.0f));
    }

    private void fadeOutToLessWidth(int i) {
        CharSequence text;
        this.largerWidth = i;
        View view = (SimpleTextView) this.titleTextLargerCopyView.get();
        if (view != null) {
            removeView(view);
        }
        SimpleTextView simpleTextView = new SimpleTextView(getContext());
        this.titleTextLargerCopyView.set(simpleTextView);
        simpleTextView.setTextColor(getThemedColor(Theme.key_actionBarDefaultTitle));
        simpleTextView.setTextSize(18);
        simpleTextView.setGravity(3);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
        simpleTextView.setRightDrawable(this.titleTextView.getRightDrawable());
        simpleTextView.setRightDrawable2(this.titleTextView.getRightDrawable2());
        simpleTextView.setRightDrawableOutside(this.titleTextView.getRightDrawableOutside());
        simpleTextView.setLeftDrawable(this.titleTextView.getLeftDrawable());
        simpleTextView.setText(this.titleTextView.getText());
        ViewPropertyAnimator duration = simpleTextView.animate().alpha(0.0f).setDuration(350L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        duration.setInterpolator(cubicBezierInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ChatAvatarContainer.this.lambda$fadeOutToLessWidth$4();
            }
        }).start();
        addView(simpleTextView);
        View view2 = (SimpleTextView) this.subtitleTextLargerCopyView.get();
        if (view2 != null) {
            removeView(view2);
        }
        SimpleTextView simpleTextView2 = new SimpleTextView(getContext());
        this.subtitleTextLargerCopyView.set(simpleTextView2);
        int i2 = Theme.key_actionBarDefaultSubtitle;
        simpleTextView2.setTextColor(getThemedColor(i2));
        simpleTextView2.setTag(Integer.valueOf(i2));
        simpleTextView2.setTextSize(14);
        simpleTextView2.setGravity(3);
        SimpleTextView simpleTextView3 = this.subtitleTextView;
        if (simpleTextView3 == null) {
            AnimatedTextView animatedTextView = this.animatedSubtitleTextView;
            if (animatedTextView != null) {
                text = animatedTextView.getText();
            }
            simpleTextView2.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAvatarContainer.this.lambda$fadeOutToLessWidth$5();
                }
            }).start();
            addView(simpleTextView2);
            setClipChildren(false);
        }
        text = simpleTextView3.getText();
        simpleTextView2.setText(text);
        simpleTextView2.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.ChatAvatarContainer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ChatAvatarContainer.this.lambda$fadeOutToLessWidth$5();
            }
        }).start();
        addView(simpleTextView2);
        setClipChildren(false);
    }

    public static CharSequence getChatSubtitle(TLRPC$Chat tLRPC$Chat, TLRPC$ChatFull tLRPC$ChatFull, int i) {
        TLRPC$ChatParticipants tLRPC$ChatParticipants;
        int i2;
        int i3;
        String formatShortNumber;
        String formatPluralString;
        String format;
        if (!ChatObject.isChannel(tLRPC$Chat)) {
            if (ChatObject.isKickedFromChat(tLRPC$Chat)) {
                i2 = R.string.YouWereKicked;
            } else if (!ChatObject.isLeftFromChat(tLRPC$Chat)) {
                int i4 = tLRPC$Chat.participants_count;
                if (tLRPC$ChatFull != null && (tLRPC$ChatParticipants = tLRPC$ChatFull.participants) != null) {
                    i4 = tLRPC$ChatParticipants.participants.size();
                }
                return (i <= 1 || i4 == 0) ? LocaleController.formatPluralString("Members", i4, new Object[0]) : String.format("%s, %s", LocaleController.formatPluralString("Members", i4, new Object[0]), LocaleController.formatPluralString("OnlineCount", i, new Object[0]));
            } else {
                i2 = R.string.YouLeft;
            }
            return LocaleController.getString(i2);
        } else if (tLRPC$ChatFull == null || (i3 = tLRPC$ChatFull.participants_count) == 0) {
            return LocaleController.getString(tLRPC$Chat.megagroup ? tLRPC$ChatFull == null ? R.string.Loading : tLRPC$Chat.has_geo ? R.string.MegaLocation : ChatObject.isPublic(tLRPC$Chat) ? R.string.MegaPublic : R.string.MegaPrivate : ChatObject.isPublic(tLRPC$Chat) ? R.string.ChannelPublic : R.string.ChannelPrivate).toLowerCase();
        } else if (tLRPC$Chat.megagroup) {
            Object[] objArr = new Object[0];
            return i > 1 ? String.format("%s, %s", LocaleController.formatPluralString("Members", i3, objArr), LocaleController.formatPluralString("OnlineCount", Math.min(i, tLRPC$ChatFull.participants_count), new Object[0])) : LocaleController.formatPluralString("Members", i3, objArr);
        } else {
            int[] iArr = new int[1];
            boolean isAccessibilityScreenReaderEnabled = AndroidUtilities.isAccessibilityScreenReaderEnabled();
            int i5 = tLRPC$ChatFull.participants_count;
            if (isAccessibilityScreenReaderEnabled) {
                iArr[0] = i5;
                formatShortNumber = String.valueOf(i5);
            } else {
                formatShortNumber = LocaleController.formatShortNumber(i5, iArr);
            }
            if (tLRPC$Chat.megagroup) {
                formatPluralString = LocaleController.formatPluralString("Members", iArr[0], new Object[0]);
                format = String.format("%d", Integer.valueOf(iArr[0]));
            } else {
                formatPluralString = LocaleController.formatPluralString("Subscribers", iArr[0], new Object[0]);
                format = String.format("%d", Integer.valueOf(iArr[0]));
            }
            return formatPluralString.replace(format, formatShortNumber);
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeOutToLessWidth$4() {
        SimpleTextView simpleTextView = (SimpleTextView) this.titleTextLargerCopyView.get();
        if (simpleTextView != null) {
            removeView(simpleTextView);
            this.titleTextLargerCopyView.set(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeOutToLessWidth$5() {
        SimpleTextView simpleTextView = (SimpleTextView) this.subtitleTextLargerCopyView.get();
        if (simpleTextView != null) {
            removeView(simpleTextView);
            this.subtitleTextLargerCopyView.set(null);
            if (this.allowDrawStories) {
                return;
            }
            setClipChildren(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (onAvatarClick()) {
            return;
        }
        openProfile(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Theme.ResourcesProvider resourcesProvider, View view) {
        if (this.secretChatTimer) {
            this.parentFragment.showDialog(AlertsCreator.createTTLAlert(getContext(), this.parentFragment.getCurrentEncryptedChat(), resourcesProvider).create());
        } else {
            openSetTimer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        openProfile(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        this.pressed = false;
        this.bounce.setPressed(false);
        if (canSearch()) {
            openSearch();
        }
    }

    private void setTypingAnimation(boolean z) {
        SimpleTextView simpleTextView;
        SimpleTextView simpleTextView2 = this.subtitleTextView;
        if (simpleTextView2 == null) {
            return;
        }
        int i = 0;
        StatusDrawable statusDrawable = null;
        if (z) {
            try {
                int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.parentFragment.getDialogId(), this.parentFragment.getThreadId()).intValue();
                if (intValue == 5) {
                    this.subtitleTextView.replaceTextWithDrawable(this.statusDrawables[intValue], "**oo**");
                    this.statusDrawables[intValue].setColor(getThemedColor(Theme.key_chat_status));
                    simpleTextView = this.subtitleTextView;
                } else {
                    this.subtitleTextView.replaceTextWithDrawable(null, null);
                    this.statusDrawables[intValue].setColor(getThemedColor(Theme.key_chat_status));
                    simpleTextView = this.subtitleTextView;
                    statusDrawable = this.statusDrawables[intValue];
                }
                simpleTextView.setLeftDrawable(statusDrawable);
                this.currentTypingDrawable = this.statusDrawables[intValue];
                while (true) {
                    StatusDrawable[] statusDrawableArr = this.statusDrawables;
                    if (i >= statusDrawableArr.length) {
                        return;
                    }
                    if (i == intValue) {
                        statusDrawableArr[i].start();
                    } else {
                        statusDrawableArr[i].stop();
                    }
                    i++;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            this.currentTypingDrawable = null;
            simpleTextView2.setLeftDrawable((Drawable) null);
            this.subtitleTextView.replaceTextWithDrawable(null, null);
            while (true) {
                StatusDrawable[] statusDrawableArr2 = this.statusDrawables;
                if (i >= statusDrawableArr2.length) {
                    return;
                }
                statusDrawableArr2[i].stop();
                i++;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateCurrentConnectionState() {
        String str;
        int i;
        Integer num;
        int i2;
        View view;
        Integer num2;
        int i3 = this.currentConnectionState;
        if (i3 == 2) {
            i = R.string.WaitingForNetwork;
        } else if (i3 == 1) {
            i = R.string.Connecting;
        } else if (i3 == 5) {
            i = R.string.Updating;
        } else if (i3 != 4) {
            str = null;
            if (str == null) {
                SimpleTextView simpleTextView = this.subtitleTextView;
                if (simpleTextView != null) {
                    if (this.lastSubtitle == null) {
                        this.lastSubtitle = simpleTextView.getText();
                    }
                    this.subtitleTextView.setText(str);
                    num2 = this.overrideSubtitleColor;
                    if (num2 == null) {
                        SimpleTextView simpleTextView2 = this.subtitleTextView;
                        i2 = Theme.key_actionBarDefaultSubtitle;
                        simpleTextView2.setTextColor(getThemedColor(i2));
                        view = this.subtitleTextView;
                    }
                    this.subtitleTextView.setTextColor(num2.intValue());
                    return;
                }
                AnimatedTextView animatedTextView = this.animatedSubtitleTextView;
                if (animatedTextView != null) {
                    if (this.lastSubtitle == null) {
                        this.lastSubtitle = animatedTextView.getText();
                    }
                    this.animatedSubtitleTextView.setText(str, true ^ LocaleController.isRTL);
                    num = this.overrideSubtitleColor;
                    if (num == null) {
                        AnimatedTextView animatedTextView2 = this.animatedSubtitleTextView;
                        i2 = Theme.key_actionBarDefaultSubtitle;
                        animatedTextView2.setTextColor(getThemedColor(i2));
                        view = this.animatedSubtitleTextView;
                    }
                    this.animatedSubtitleTextView.setTextColor(num.intValue());
                    return;
                }
                return;
                view.setTag(Integer.valueOf(i2));
                return;
            }
            CharSequence charSequence = this.lastSubtitle;
            if (charSequence != null) {
                SimpleTextView simpleTextView3 = this.subtitleTextView;
                if (simpleTextView3 != null) {
                    simpleTextView3.setText(charSequence);
                    this.lastSubtitle = null;
                    num2 = this.overrideSubtitleColor;
                    if (num2 == null) {
                        int i4 = this.lastSubtitleColorKey;
                        if (i4 < 0) {
                            return;
                        }
                        this.subtitleTextView.setTextColor(getThemedColor(i4));
                        view = this.subtitleTextView;
                    }
                    this.subtitleTextView.setTextColor(num2.intValue());
                    return;
                }
                AnimatedTextView animatedTextView3 = this.animatedSubtitleTextView;
                if (animatedTextView3 != null) {
                    animatedTextView3.setText(charSequence, true ^ LocaleController.isRTL);
                    this.lastSubtitle = null;
                    num = this.overrideSubtitleColor;
                    if (num == null) {
                        int i5 = this.lastSubtitleColorKey;
                        if (i5 < 0) {
                            return;
                        }
                        this.animatedSubtitleTextView.setTextColor(getThemedColor(i5));
                        view = this.animatedSubtitleTextView;
                    }
                    this.animatedSubtitleTextView.setTextColor(num.intValue());
                    return;
                }
                return;
                i2 = this.lastSubtitleColorKey;
                view.setTag(Integer.valueOf(i2));
                return;
            }
            return;
        } else {
            i = R.string.ConnectingToProxy;
        }
        str = LocaleController.getString(i);
        if (str == null) {
        }
    }

    protected boolean canSearch() {
        return false;
    }

    public void checkAndUpdateAvatar() {
        TLRPC$User tLRPC$User;
        BackupImageView backupImageView;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null) {
            return;
        }
        TLRPC$User currentUser = chatActivity.getCurrentUser();
        TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
        if (this.parentFragment.getChatMode() == 3) {
            long savedDialogId = this.parentFragment.getSavedDialogId();
            if (savedDialogId >= 0) {
                tLRPC$User = this.parentFragment.getMessagesController().getUser(Long.valueOf(savedDialogId));
                currentChat = null;
            } else {
                currentChat = this.parentFragment.getMessagesController().getChat(Long.valueOf(-savedDialogId));
                tLRPC$User = null;
            }
        } else {
            tLRPC$User = currentUser;
        }
        if (tLRPC$User == null) {
            if (currentChat != null) {
                this.avatarDrawable.setInfo(this.currentAccount, currentChat);
                BackupImageView backupImageView2 = this.avatarImageView;
                if (backupImageView2 != null) {
                    backupImageView2.setForUserOrChat(currentChat, this.avatarDrawable);
                }
                this.avatarImageView.setRoundRadius(AndroidUtilities.dp(currentChat.forum ? ChatObject.hasStories(currentChat) ? 11.0f : 16.0f : 21.0f));
                return;
            }
            return;
        }
        this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User);
        if (UserObject.isReplyUser(tLRPC$User)) {
            this.avatarDrawable.setScaleSize(0.8f);
            this.avatarDrawable.setAvatarType(12);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        } else if (UserObject.isAnonymous(tLRPC$User)) {
            this.avatarDrawable.setScaleSize(0.8f);
            this.avatarDrawable.setAvatarType(21);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        } else if (UserObject.isUserSelf(tLRPC$User) && this.parentFragment.getChatMode() == 3) {
            this.avatarDrawable.setScaleSize(0.8f);
            this.avatarDrawable.setAvatarType(22);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        } else if (!UserObject.isUserSelf(tLRPC$User)) {
            this.avatarDrawable.setScaleSize(1.0f);
            BackupImageView backupImageView3 = this.avatarImageView;
            if (backupImageView3 != null) {
                backupImageView3.imageReceiver.setForUserOrChat(tLRPC$User, this.avatarDrawable, null, true, 3, false);
                return;
            }
            return;
        } else {
            this.avatarDrawable.setScaleSize(0.8f);
            this.avatarDrawable.setAvatarType(1);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        }
        backupImageView.setImage((ImageLocation) null, (String) null, this.avatarDrawable, tLRPC$User);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            if (this.currentConnectionState != connectionState) {
                this.currentConnectionState = connectionState;
                updateCurrentConnectionState();
            }
        } else if (i != NotificationCenter.emojiLoaded) {
            if (i == NotificationCenter.savedMessagesDialogsUpdate) {
                updateSubtitle(true);
            }
        } else {
            SimpleTextView simpleTextView = this.titleTextView;
            if (simpleTextView != null) {
                simpleTextView.invalidate();
            }
            if (getSubtitleTextView() != null) {
                getSubtitleTextView().invalidate();
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        canvas.save();
        float scale = this.bounce.getScale(0.02f);
        canvas.scale(scale, scale, getWidth() / 2.0f, getHeight() / 2.0f);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.ignoreTouches) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public BackupImageView getAvatarImageView() {
        return this.avatarImageView;
    }

    public int getLastSubtitleColorKey() {
        return this.lastSubtitleColorKey;
    }

    public SharedMediaLayout.SharedMediaPreloader getSharedMediaPreloader() {
        return this.sharedMediaPreloader;
    }

    public TextPaint getSubtitlePaint() {
        SimpleTextView simpleTextView = this.subtitleTextView;
        return simpleTextView != null ? simpleTextView.getTextPaint() : this.animatedSubtitleTextView.getPaint();
    }

    public View getSubtitleTextView() {
        SimpleTextView simpleTextView = this.subtitleTextView;
        if (simpleTextView != null) {
            return simpleTextView;
        }
        AnimatedTextView animatedTextView = this.animatedSubtitleTextView;
        if (animatedTextView != null) {
            return animatedTextView;
        }
        return null;
    }

    public ImageView getTimeItem() {
        return this.timeItem;
    }

    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }

    public void hideSubtitle() {
        if (getSubtitleTextView() != null) {
            getSubtitleTextView().setVisibility(8);
        }
    }

    public void hideTimeItem(boolean z) {
        ImageView imageView = this.timeItem;
        if (imageView == null || imageView.getTag() == null) {
            return;
        }
        this.timeItem.clearAnimation();
        this.timeItem.setTag(null);
        if (z) {
            this.timeItem.animate().setDuration(180L).alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAvatarContainer.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatAvatarContainer.this.timeItem.setVisibility(8);
                    super.onAnimationEnd(animator);
                }
            }).start();
            return;
        }
        this.timeItem.setVisibility(8);
        this.timeItem.setAlpha(0.0f);
        this.timeItem.setScaleY(0.0f);
        this.timeItem.setScaleX(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
            if (this.parentFragment.getChatMode() == 3) {
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.savedMessagesDialogsUpdate);
            }
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            updateCurrentConnectionState();
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatusDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.attach();
        }
    }

    protected boolean onAvatarClick() {
        return false;
    }

    public void onDestroy() {
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = this.sharedMediaPreloader;
        if (sharedMediaPreloader != null) {
            sharedMediaPreloader.onDestroy(this.parentFragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
            if (this.parentFragment.getChatMode() == 3) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.savedMessagesDialogsUpdate);
            }
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatusDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.detach();
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        CharSequence text;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        StringBuilder sb = new StringBuilder();
        sb.append(this.titleTextView.getText());
        if (this.rightDrawableContentDescription != null) {
            sb.append(", ");
            sb.append(this.rightDrawableContentDescription);
        }
        if (this.rightDrawable2ContentDescription != null) {
            sb.append(", ");
            sb.append(this.rightDrawable2ContentDescription);
        }
        sb.append("\n");
        SimpleTextView simpleTextView = this.subtitleTextView;
        if (simpleTextView == null) {
            AnimatedTextView animatedTextView = this.animatedSubtitleTextView;
            if (animatedTextView != null) {
                text = animatedTextView.getText();
            }
            accessibilityNodeInfo.setContentDescription(sb);
            if (accessibilityNodeInfo.isClickable() || Build.VERSION.SDK_INT < 21) {
            }
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.OpenProfile)));
            return;
        }
        text = simpleTextView.getText();
        sb.append(text);
        accessibilityNodeInfo.setContentDescription(sb);
        if (accessibilityNodeInfo.isClickable()) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x008d, code lost:
        if (r8 != null) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x008f, code lost:
        r8.layout(r7, org.telegram.messenger.AndroidUtilities.dp(r10) + r6, r8.getMeasuredWidth() + r7, (r8.getTextHeight() + r6) + org.telegram.messenger.AndroidUtilities.dp(r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00db, code lost:
        if (r8 != null) goto L13;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int dp;
        int measuredWidth;
        int textHeight;
        SimpleTextView simpleTextView;
        int currentActionBarHeight = ((ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(42.0f)) / 2) + ((Build.VERSION.SDK_INT < 21 || !this.occupyStatusBar) ? 0 : AndroidUtilities.statusBarHeight);
        BackupImageView backupImageView = this.avatarImageView;
        int i5 = this.leftPadding;
        int i6 = currentActionBarHeight + 1;
        backupImageView.layout(i5, i6, AndroidUtilities.dp(42.0f) + i5, AndroidUtilities.dp(42.0f) + i6);
        int dp2 = this.leftPadding + (this.avatarImageView.getVisibility() == 0 ? AndroidUtilities.dp(54.0f) : 0) + this.rightAvatarPadding;
        SimpleTextView simpleTextView2 = (SimpleTextView) this.titleTextLargerCopyView.get();
        if (getSubtitleTextView().getVisibility() != 8) {
            f = 1.3f;
            this.titleTextView.layout(dp2, (AndroidUtilities.dp(1.3f) + currentActionBarHeight) - this.titleTextView.getPaddingTop(), this.titleTextView.getMeasuredWidth() + dp2, (((this.titleTextView.getTextHeight() + currentActionBarHeight) + AndroidUtilities.dp(1.3f)) - this.titleTextView.getPaddingTop()) + this.titleTextView.getPaddingBottom());
        } else {
            f = 11.0f;
            this.titleTextView.layout(dp2, (AndroidUtilities.dp(11.0f) + currentActionBarHeight) - this.titleTextView.getPaddingTop(), this.titleTextView.getMeasuredWidth() + dp2, (((this.titleTextView.getTextHeight() + currentActionBarHeight) + AndroidUtilities.dp(11.0f)) - this.titleTextView.getPaddingTop()) + this.titleTextView.getPaddingBottom());
        }
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.layout(this.leftPadding + AndroidUtilities.dp(16.0f), AndroidUtilities.dp(15.0f) + currentActionBarHeight, this.leftPadding + AndroidUtilities.dp(50.0f), AndroidUtilities.dp(49.0f) + currentActionBarHeight);
        }
        ImageView imageView2 = this.starBgItem;
        if (imageView2 != null) {
            imageView2.layout(this.leftPadding + AndroidUtilities.dp(28.0f), AndroidUtilities.dp(24.0f) + currentActionBarHeight, this.leftPadding + AndroidUtilities.dp(28.0f) + this.starBgItem.getMeasuredWidth(), AndroidUtilities.dp(24.0f) + currentActionBarHeight + this.starBgItem.getMeasuredHeight());
        }
        ImageView imageView3 = this.starFgItem;
        if (imageView3 != null) {
            imageView3.layout(this.leftPadding + AndroidUtilities.dp(28.0f), AndroidUtilities.dp(24.0f) + currentActionBarHeight, this.leftPadding + AndroidUtilities.dp(28.0f) + this.starFgItem.getMeasuredWidth(), AndroidUtilities.dp(24.0f) + currentActionBarHeight + this.starFgItem.getMeasuredHeight());
        }
        View view = this.subtitleTextView;
        if (view == null) {
            view = this.animatedSubtitleTextView;
            if (view != null) {
                dp = AndroidUtilities.dp(24.0f) + currentActionBarHeight;
                measuredWidth = this.animatedSubtitleTextView.getMeasuredWidth() + dp2;
                textHeight = this.animatedSubtitleTextView.getTextHeight();
            }
            simpleTextView = (SimpleTextView) this.subtitleTextLargerCopyView.get();
            if (simpleTextView == null) {
                simpleTextView.layout(dp2, AndroidUtilities.dp(24.0f) + currentActionBarHeight, simpleTextView.getMeasuredWidth() + dp2, currentActionBarHeight + simpleTextView.getTextHeight() + AndroidUtilities.dp(24.0f));
                return;
            }
            return;
        }
        dp = AndroidUtilities.dp(24.0f) + currentActionBarHeight;
        measuredWidth = this.subtitleTextView.getMeasuredWidth() + dp2;
        textHeight = this.subtitleTextView.getTextHeight();
        view.layout(dp2, dp, measuredWidth, textHeight + currentActionBarHeight + AndroidUtilities.dp(24.0f));
        simpleTextView = (SimpleTextView) this.subtitleTextLargerCopyView.get();
        if (simpleTextView == null) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00db  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec;
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        int i3;
        SimpleTextView simpleTextView;
        int size = View.MeasureSpec.getSize(i) + this.titleTextView.getPaddingRight();
        int dp = size - AndroidUtilities.dp((this.avatarImageView.getVisibility() == 0 ? 54 : 0) + 16);
        this.avatarImageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
        this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f) + this.titleTextView.getPaddingRight(), Integer.MIN_VALUE));
        View view = this.subtitleTextView;
        if (view == null) {
            view = this.animatedSubtitleTextView;
            if (view != null) {
                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(dp, 1073741824);
            }
            imageView = this.timeItem;
            if (imageView != null) {
                imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824));
            }
            imageView2 = this.starBgItem;
            if (imageView2 != null) {
                imageView2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
            }
            imageView3 = this.starFgItem;
            if (imageView3 != null) {
                imageView3.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
            }
            setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
            i3 = this.lastWidth;
            if (i3 != -1 && i3 != size && i3 > size) {
                fadeOutToLessWidth(i3);
            }
            simpleTextView = (SimpleTextView) this.titleTextLargerCopyView.get();
            if (simpleTextView != null) {
                simpleTextView.measure(View.MeasureSpec.makeMeasureSpec(this.largerWidth - AndroidUtilities.dp((this.avatarImageView.getVisibility() == 0 ? 54 : 0) + 16), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), Integer.MIN_VALUE));
            }
            this.lastWidth = size;
        }
        makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE);
        view.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), Integer.MIN_VALUE));
        imageView = this.timeItem;
        if (imageView != null) {
        }
        imageView2 = this.starBgItem;
        if (imageView2 != null) {
        }
        imageView3 = this.starFgItem;
        if (imageView3 != null) {
        }
        setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
        i3 = this.lastWidth;
        if (i3 != -1) {
            fadeOutToLessWidth(i3);
        }
        simpleTextView = (SimpleTextView) this.titleTextLargerCopyView.get();
        if (simpleTextView != null) {
        }
        this.lastWidth = size;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && canSearch()) {
            this.pressed = true;
            this.bounce.setPressed(true);
            AndroidUtilities.cancelRunOnUIThread(this.onLongClick);
            AndroidUtilities.runOnUIThread(this.onLongClick, ViewConfiguration.getLongPressTimeout());
            return true;
        }
        if ((motionEvent.getAction() == 1 || motionEvent.getAction() == 3) && this.pressed) {
            this.bounce.setPressed(false);
            this.pressed = false;
            if (isClickable()) {
                openProfile(false);
            }
            AndroidUtilities.cancelRunOnUIThread(this.onLongClick);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void openProfile(boolean z) {
        openProfile(z, true, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0107, code lost:
        if (r11 != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x010a, code lost:
        r0 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x014d, code lost:
        if (r11 != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x014f, code lost:
        r1.setPlayProfileAnimation(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:
        if (r10.avatarImageView.getImageReceiver().hasNotThumb() != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x014d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void openProfile(boolean z, boolean z2, boolean z3) {
        long id;
        ProfileActivity profileActivity;
        if (z) {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y) {
                }
            }
            z = false;
        }
        TLRPC$User currentUser = this.parentFragment.getCurrentUser();
        TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
        ImageReceiver imageReceiver = this.avatarImageView.getImageReceiver();
        String imageKey = imageReceiver.getImageKey();
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (imageKey != null && !imageLoader.isInMemCache(imageKey, false)) {
            Drawable drawable = imageReceiver.getDrawable();
            if ((drawable instanceof BitmapDrawable) && !(drawable instanceof AnimatedFileDrawable)) {
                imageLoader.putImageToCache((BitmapDrawable) drawable, imageKey, false);
            }
        }
        int i = 2;
        if (currentUser == null) {
            if (currentChat != null) {
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", currentChat.id);
                if (this.parentFragment.getChatMode() != 3) {
                    ChatActivity chatActivity = this.parentFragment;
                    if (chatActivity.isTopic) {
                        id = chatActivity.getThreadMessage().getId();
                    }
                    ProfileActivity profileActivity2 = new ProfileActivity(bundle, this.sharedMediaPreloader);
                    profileActivity2.setChatInfo(this.parentFragment.getCurrentChatInfo());
                    profileActivity = profileActivity2;
                    if (z2) {
                    }
                    this.parentFragment.presentFragment(profileActivity, z3);
                }
                id = this.parentFragment.getSavedDialogId();
                bundle.putLong("topic_id", id);
                ProfileActivity profileActivity22 = new ProfileActivity(bundle, this.sharedMediaPreloader);
                profileActivity22.setChatInfo(this.parentFragment.getCurrentChatInfo());
                profileActivity = profileActivity22;
                if (z2) {
                }
                this.parentFragment.presentFragment(profileActivity, z3);
            }
            return;
        }
        Bundle bundle2 = new Bundle();
        if (UserObject.isUserSelf(currentUser)) {
            if (this.sharedMediaPreloader.hasSharedMedia()) {
                bundle2.putLong("dialog_id", this.parentFragment.getDialogId());
                if (this.parentFragment.getChatMode() == 3) {
                    bundle2.putLong("topic_id", this.parentFragment.getSavedDialogId());
                }
                MediaActivity mediaActivity = new MediaActivity(bundle2, this.sharedMediaPreloader);
                mediaActivity.setChatInfo(this.parentFragment.getCurrentChatInfo());
                this.parentFragment.presentFragment(mediaActivity, z3);
                return;
            }
            return;
        }
        if (this.parentFragment.getChatMode() == 3) {
            long savedDialogId = this.parentFragment.getSavedDialogId();
            bundle2.putBoolean("saved", true);
            if (savedDialogId >= 0) {
                bundle2.putLong("user_id", savedDialogId);
            } else {
                bundle2.putLong("chat_id", -savedDialogId);
            }
        } else {
            bundle2.putLong("user_id", currentUser.id);
            if (this.timeItem != null) {
                bundle2.putLong("dialog_id", this.parentFragment.getDialogId());
            }
        }
        bundle2.putBoolean("reportSpam", this.parentFragment.hasReportSpam());
        bundle2.putInt("actionBarColor", getThemedColor(Theme.key_actionBarDefault));
        profileActivity = new ProfileActivity(bundle2, this.sharedMediaPreloader);
        TLRPC$UserFull currentUserInfo = this.parentFragment.getCurrentUserInfo();
        ChatActivity chatActivity2 = this.parentFragment;
        profileActivity.setUserInfo(currentUserInfo, chatActivity2.profileChannelMessageFetcher, chatActivity2.birthdayAssetsFetcher);
        if (z2) {
        }
        this.parentFragment.presentFragment(profileActivity, z3);
    }

    protected void openSearch() {
    }

    public boolean openSetTimer() {
        if (this.parentFragment.getParentActivity() == null) {
            return false;
        }
        TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
        if (currentChat != null && !ChatObject.canUserDoAdminAction(currentChat, 13)) {
            if (this.timeItem.getTag() != null) {
                this.parentFragment.showTimerHint();
            }
            return false;
        }
        TLRPC$ChatFull currentChatInfo = this.parentFragment.getCurrentChatInfo();
        TLRPC$UserFull currentUserInfo = this.parentFragment.getCurrentUserInfo();
        int i = currentUserInfo != null ? currentUserInfo.ttl_period : currentChatInfo != null ? currentChatInfo.ttl_period : 0;
        AutoDeletePopupWrapper autoDeletePopupWrapper = new AutoDeletePopupWrapper(getContext(), null, new AutoDeletePopupWrapper.Callback() { // from class: org.telegram.ui.Components.ChatAvatarContainer.2
            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public void dismiss() {
                ActionBarPopupWindow actionBarPopupWindow = r2[0];
                if (actionBarPopupWindow != null) {
                    actionBarPopupWindow.dismiss();
                }
            }

            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public void setAutoDeleteHistory(int i2, int i3) {
                UndoView undoView;
                if (ChatAvatarContainer.this.parentFragment == null) {
                    return;
                }
                ChatAvatarContainer.this.parentFragment.getMessagesController().setDialogHistoryTTL(ChatAvatarContainer.this.parentFragment.getDialogId(), i2);
                TLRPC$ChatFull currentChatInfo2 = ChatAvatarContainer.this.parentFragment.getCurrentChatInfo();
                TLRPC$UserFull currentUserInfo2 = ChatAvatarContainer.this.parentFragment.getCurrentUserInfo();
                if ((currentUserInfo2 == null && currentChatInfo2 == null) || (undoView = ChatAvatarContainer.this.parentFragment.getUndoView()) == null) {
                    return;
                }
                undoView.showWithAction(ChatAvatarContainer.this.parentFragment.getDialogId(), i3, ChatAvatarContainer.this.parentFragment.getCurrentUser(), Integer.valueOf(currentUserInfo2 != null ? currentUserInfo2.ttl_period : currentChatInfo2.ttl_period), (Runnable) null, (Runnable) null);
            }

            @Override // org.telegram.ui.Components.AutoDeletePopupWrapper.Callback
            public /* synthetic */ void showGlobalAutoDeleteScreen() {
                AutoDeletePopupWrapper.Callback.-CC.$default$showGlobalAutoDeleteScreen(this);
            }
        }, true, 0, this.resourcesProvider);
        autoDeletePopupWrapper.lambda$updateItems$7(i);
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(autoDeletePopupWrapper.windowLayout, -2, -2) { // from class: org.telegram.ui.Components.ChatAvatarContainer.3
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                if (ChatAvatarContainer.this.parentFragment != null) {
                    ChatAvatarContainer.this.parentFragment.dimBehindView(false);
                }
            }
        };
        final ActionBarPopupWindow[] actionBarPopupWindowArr = {actionBarPopupWindow};
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindowArr[0].setDismissAnimationDuration(NotificationCenter.updateAllMessages);
        actionBarPopupWindowArr[0].setOutsideTouchable(true);
        actionBarPopupWindowArr[0].setClippingEnabled(true);
        actionBarPopupWindowArr[0].setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindowArr[0].setFocusable(true);
        autoDeletePopupWrapper.windowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindowArr[0].setInputMethodMode(2);
        actionBarPopupWindowArr[0].getContentView().setFocusableInTouchMode(true);
        ActionBarPopupWindow actionBarPopupWindow2 = actionBarPopupWindowArr[0];
        BackupImageView backupImageView = this.avatarImageView;
        actionBarPopupWindow2.showAtLocation(backupImageView, 0, (int) (backupImageView.getX() + getX()), (int) this.avatarImageView.getY());
        this.parentFragment.dimBehindView(true);
        return true;
    }

    public void setChatAvatar(TLRPC$Chat tLRPC$Chat) {
        this.avatarDrawable.setInfo(this.currentAccount, tLRPC$Chat);
        BackupImageView backupImageView = this.avatarImageView;
        if (backupImageView != null) {
            backupImageView.setForUserOrChat(tLRPC$Chat, this.avatarDrawable);
            this.avatarImageView.setRoundRadius(AndroidUtilities.dp(ChatObject.isForum(tLRPC$Chat) ? ChatObject.hasStories(tLRPC$Chat) ? 11.0f : 16.0f : 21.0f));
        }
    }

    public void setLeftPadding(int i) {
        this.leftPadding = i;
    }

    public void setOccupyStatusBar(boolean z) {
        this.occupyStatusBar = z;
    }

    public void setOverrideSubtitleColor(Integer num) {
        this.overrideSubtitleColor = num;
    }

    public void setRightAvatarPadding(int i) {
        this.rightAvatarPadding = i;
    }

    public void setStars(boolean z, boolean z2) {
        ImageView imageView = this.starBgItem;
        if (imageView == null || this.starFgItem == null) {
            return;
        }
        this.stars = z;
        if (z2) {
            imageView.animate().alpha(z ? 1.0f : 0.0f).scaleX(z ? 1.1f : 0.0f).scaleY(z ? 1.1f : 0.0f).start();
            this.starFgItem.animate().alpha(z ? 1.0f : 0.0f).scaleX(z ? 1.0f : 0.0f).scaleY(z ? 1.0f : 0.0f).start();
            return;
        }
        imageView.setAlpha(z ? 1.0f : 0.0f);
        this.starBgItem.setScaleX(z ? 1.1f : 0.0f);
        this.starBgItem.setScaleY(z ? 1.1f : 0.0f);
        this.starFgItem.setAlpha(z ? 1.0f : 0.0f);
        this.starFgItem.setScaleX(z ? 1.0f : 0.0f);
        this.starFgItem.setScaleY(z ? 1.0f : 0.0f);
    }

    public void setStoriesForceState(Integer num) {
        this.storiesForceState = num;
    }

    public void setSubtitle(CharSequence charSequence) {
        if (this.lastSubtitle != null) {
            this.lastSubtitle = charSequence;
            return;
        }
        SimpleTextView simpleTextView = this.subtitleTextView;
        if (simpleTextView != null) {
            simpleTextView.setText(charSequence);
            return;
        }
        AnimatedTextView animatedTextView = this.animatedSubtitleTextView;
        if (animatedTextView != null) {
            animatedTextView.setText(charSequence);
        }
    }

    public void setTime(int i, boolean z) {
        if (this.timerDrawable == null) {
            return;
        }
        boolean z2 = !this.stars;
        if (i != 0 || this.secretChatTimer) {
            if (!z2) {
                hideTimeItem(z);
                return;
            }
            showTimeItem(z);
            this.timerDrawable.setTime(i);
        }
    }

    public void setTitle(CharSequence charSequence) {
        setTitle(charSequence, false, false, false, false, null, false);
    }

    public void setTitle(CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4, TLRPC$EmojiStatus tLRPC$EmojiStatus, boolean z5) {
        if (charSequence != null) {
            charSequence = Emoji.replaceEmoji(charSequence, this.titleTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(24.0f), false);
        }
        this.titleTextView.setText(charSequence);
        if (z || z2) {
            if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
                ScamDrawable scamDrawable = new ScamDrawable(11, !z ? 1 : 0);
                scamDrawable.setColor(getThemedColor(Theme.key_actionBarDefaultSubtitle));
                this.titleTextView.setRightDrawable2(scamDrawable);
                this.rightDrawable2ContentDescription = LocaleController.getString(R.string.ScamMessage);
                this.rightDrawableIsScamOrVerified = true;
            }
        } else if (z3) {
            Drawable mutate = getResources().getDrawable(R.drawable.verified_area).mutate();
            int themedColor = getThemedColor(Theme.key_profile_verifiedBackground);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            mutate.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
            Drawable mutate2 = getResources().getDrawable(R.drawable.verified_check).mutate();
            mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_profile_verifiedCheck), mode));
            this.titleTextView.setRightDrawable2(new CombinedDrawable(mutate, mutate2));
            this.rightDrawableIsScamOrVerified = true;
            this.rightDrawable2ContentDescription = LocaleController.getString(R.string.AccDescrVerified);
        } else if (this.titleTextView.getRightDrawable() instanceof ScamDrawable) {
            this.titleTextView.setRightDrawable2(null);
            this.rightDrawableIsScamOrVerified = false;
            this.rightDrawable2ContentDescription = null;
        }
        if (!z4 && DialogObject.getEmojiStatusDocumentId(tLRPC$EmojiStatus) == 0) {
            this.titleTextView.setRightDrawable((Drawable) null);
            this.rightDrawableContentDescription = null;
            return;
        }
        if ((this.titleTextView.getRightDrawable() instanceof AnimatedEmojiDrawable.WrapSizeDrawable) && (((AnimatedEmojiDrawable.WrapSizeDrawable) this.titleTextView.getRightDrawable()).getDrawable() instanceof AnimatedEmojiDrawable)) {
            ((AnimatedEmojiDrawable) ((AnimatedEmojiDrawable.WrapSizeDrawable) this.titleTextView.getRightDrawable()).getDrawable()).removeView(this.titleTextView);
        }
        if (DialogObject.getEmojiStatusDocumentId(tLRPC$EmojiStatus) != 0) {
            this.emojiStatusDrawable.set(DialogObject.getEmojiStatusDocumentId(tLRPC$EmojiStatus), z5);
        } else if (z4) {
            Drawable mutate3 = ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_premium_liststar).mutate();
            mutate3.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_profile_verifiedBackground), PorterDuff.Mode.MULTIPLY));
            this.emojiStatusDrawable.set(mutate3, z5);
        } else {
            this.emojiStatusDrawable.set((Drawable) null, z5);
        }
        this.emojiStatusDrawable.setColor(Integer.valueOf(getThemedColor(Theme.key_profile_verifiedBackground)));
        this.titleTextView.setRightDrawable(this.emojiStatusDrawable);
        this.rightDrawableIsScamOrVerified = false;
        this.rightDrawableContentDescription = LocaleController.getString(R.string.AccDescrPremium);
    }

    public void setTitleColors(int i, int i2) {
        this.titleTextView.setTextColor(i);
        this.subtitleTextView.setTextColor(i2);
        this.subtitleTextView.setTag(Integer.valueOf(i2));
    }

    public void setTitleExpand(boolean z) {
        int dp = z ? AndroidUtilities.dp(10.0f) : 0;
        if (this.titleTextView.getPaddingRight() != dp) {
            this.titleTextView.setPadding(0, AndroidUtilities.dp(6.0f), dp, AndroidUtilities.dp(12.0f));
            requestLayout();
            invalidate();
        }
    }

    public void setTitleIcons(Drawable drawable, Drawable drawable2) {
        this.titleTextView.setLeftDrawable(drawable);
        if (this.rightDrawableIsScamOrVerified) {
            return;
        }
        this.rightDrawable2ContentDescription = drawable2 != null ? LocaleController.getString(R.string.NotificationsMuted) : null;
        this.titleTextView.setRightDrawable2(drawable2);
    }

    public void setUserAvatar(TLRPC$User tLRPC$User) {
        setUserAvatar(tLRPC$User, false);
    }

    public void setUserAvatar(TLRPC$User tLRPC$User, boolean z) {
        BackupImageView backupImageView;
        this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User);
        if (UserObject.isReplyUser(tLRPC$User)) {
            this.avatarDrawable.setAvatarType(12);
            this.avatarDrawable.setScaleSize(0.8f);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        } else if (UserObject.isAnonymous(tLRPC$User)) {
            this.avatarDrawable.setAvatarType(21);
            this.avatarDrawable.setScaleSize(0.8f);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        } else if (!UserObject.isUserSelf(tLRPC$User) || z) {
            this.avatarDrawable.setScaleSize(1.0f);
            BackupImageView backupImageView2 = this.avatarImageView;
            if (backupImageView2 != null) {
                backupImageView2.setForUserOrChat(tLRPC$User, this.avatarDrawable);
                return;
            }
            return;
        } else {
            this.avatarDrawable.setAvatarType(1);
            this.avatarDrawable.setScaleSize(0.8f);
            backupImageView = this.avatarImageView;
            if (backupImageView == null) {
                return;
            }
        }
        backupImageView.setImage((ImageLocation) null, (String) null, this.avatarDrawable, tLRPC$User);
    }

    public void showTimeItem(boolean z) {
        ImageView imageView = this.timeItem;
        if (imageView != null && imageView.getTag() == null && this.avatarImageView.getVisibility() == 0) {
            this.timeItem.clearAnimation();
            this.timeItem.setVisibility(0);
            this.timeItem.setTag(1);
            if (z) {
                this.timeItem.animate().setDuration(180L).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setListener(null).start();
                return;
            }
            this.timeItem.setAlpha(1.0f);
            this.timeItem.setScaleY(1.0f);
            this.timeItem.setScaleX(1.0f);
        }
    }

    public void updateColors() {
        StatusDrawable statusDrawable = this.currentTypingDrawable;
        if (statusDrawable != null) {
            statusDrawable.setColor(getThemedColor(Theme.key_chat_status));
        }
    }

    public void updateOnlineCount() {
        TLRPC$UserStatus tLRPC$UserStatus;
        boolean z;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null) {
            return;
        }
        this.onlineCount = 0;
        TLRPC$ChatFull currentChatInfo = chatActivity.getCurrentChatInfo();
        if (currentChatInfo == null) {
            return;
        }
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        if (!(currentChatInfo instanceof TLRPC$TL_chatFull) && (!((z = currentChatInfo instanceof TLRPC$TL_channelFull)) || currentChatInfo.participants_count > 200 || currentChatInfo.participants == null)) {
            if (!z || currentChatInfo.participants_count <= 200) {
                return;
            }
            this.onlineCount = currentChatInfo.online_count;
            return;
        }
        for (int i = 0; i < currentChatInfo.participants.participants.size(); i++) {
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(((TLRPC$ChatParticipant) currentChatInfo.participants.participants.get(i)).user_id));
            if (user != null && (tLRPC$UserStatus = user.status) != null && ((tLRPC$UserStatus.expires > currentTime || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) && user.status.expires > 10000)) {
                this.onlineCount++;
            }
        }
    }

    public void updateSubtitle() {
        updateSubtitle(false);
    }

    public void updateSubtitle(boolean z) {
        int i;
        int i2;
        View view;
        boolean z2 = false;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null) {
            return;
        }
        if (chatActivity.getChatMode() == 6) {
            setSubtitle(BusinessLinksController.stripHttps(this.parentFragment.businessLink.link));
            return;
        }
        TLRPC$User currentUser = this.parentFragment.getCurrentUser();
        if ((UserObject.isUserSelf(currentUser) || UserObject.isReplyUser(currentUser) || this.parentFragment.getChatMode() != 0) && this.parentFragment.getChatMode() != 3) {
            if (getSubtitleTextView().getVisibility() != 8) {
                getSubtitleTextView().setVisibility(8);
                return;
            }
            return;
        }
        TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
        CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.parentFragment.getDialogId(), this.parentFragment.getThreadId(), false);
        CharSequence charSequence = "";
        if (printingString != null) {
            printingString = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
        }
        if (printingString != null && printingString.length() != 0 && (!ChatObject.isChannel(currentChat) || currentChat.megagroup)) {
            if (this.parentFragment.isThreadChat() && this.titleTextView.getTag() != null) {
                this.titleTextView.setTag(null);
                getSubtitleTextView().setVisibility(0);
                AnimatorSet animatorSet = this.titleAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.titleAnimation = null;
                }
                if (z) {
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.titleAnimation = animatorSet2;
                    animatorSet2.playTogether(ObjectAnimator.ofFloat(this.titleTextView, View.TRANSLATION_Y, 0.0f), ObjectAnimator.ofFloat(getSubtitleTextView(), View.ALPHA, 1.0f));
                    this.titleAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAvatarContainer.6
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ChatAvatarContainer.this.titleAnimation = null;
                        }
                    });
                    this.titleAnimation.setDuration(180L);
                    this.titleAnimation.start();
                } else {
                    this.titleTextView.setTranslationY(0.0f);
                    getSubtitleTextView().setAlpha(1.0f);
                }
            }
            charSequence = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.parentFragment.getDialogId(), this.parentFragment.getThreadId()).intValue() == 5 ? Emoji.replaceEmoji(printingString, getSubtitlePaint().getFontMetricsInt(), AndroidUtilities.dp(15.0f), false) : printingString;
            setTypingAnimation(true);
            z2 = true;
        } else if (this.parentFragment.isThreadChat() && !this.parentFragment.isTopic) {
            if (this.titleTextView.getTag() != null) {
                return;
            }
            this.titleTextView.setTag(1);
            AnimatorSet animatorSet3 = this.titleAnimation;
            if (animatorSet3 != null) {
                animatorSet3.cancel();
                this.titleAnimation = null;
            }
            if (!z) {
                this.titleTextView.setTranslationY(AndroidUtilities.dp(9.7f));
                getSubtitleTextView().setAlpha(0.0f);
                getSubtitleTextView().setVisibility(4);
                return;
            }
            AnimatorSet animatorSet4 = new AnimatorSet();
            this.titleAnimation = animatorSet4;
            animatorSet4.playTogether(ObjectAnimator.ofFloat(this.titleTextView, View.TRANSLATION_Y, AndroidUtilities.dp(9.7f)), ObjectAnimator.ofFloat(getSubtitleTextView(), View.ALPHA, 0.0f));
            this.titleAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAvatarContainer.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    ChatAvatarContainer.this.titleAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ChatAvatarContainer.this.titleAnimation == animator) {
                        ChatAvatarContainer.this.getSubtitleTextView().setVisibility(4);
                        ChatAvatarContainer.this.titleAnimation = null;
                    }
                }
            });
            this.titleAnimation.setDuration(180L);
            this.titleAnimation.start();
            return;
        } else {
            setTypingAnimation(false);
            if (this.parentFragment.getChatMode() == 3) {
                charSequence = LocaleController.formatPluralString("SavedMessagesCount", Math.max(1, this.parentFragment.getMessagesController().getSavedMessagesController().getMessagesCount(this.parentFragment.getSavedDialogId())), new Object[0]);
            } else {
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2.isTopic && currentChat != null) {
                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(currentChat.id, this.parentFragment.getTopicId());
                    int i3 = findTopic != null ? findTopic.totalMessagesCount - 1 : 0;
                    charSequence = i3 > 0 ? LocaleController.formatPluralString("messages", i3, Integer.valueOf(i3)) : LocaleController.formatString(R.string.TopicProfileStatus, currentChat.title);
                } else if (currentChat != null) {
                    charSequence = getChatSubtitle(currentChat, chatActivity2.getCurrentChatInfo(), this.onlineCount);
                } else if (currentUser != null) {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(currentUser.id));
                    if (user != null) {
                        currentUser = user;
                    }
                    if (!UserObject.isReplyUser(currentUser)) {
                        if (currentUser.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            i = R.string.ChatYourSelf;
                        } else {
                            long j = currentUser.id;
                            if (j == 333000 || j == 777000 || j == 42777) {
                                i = R.string.ServiceNotifications;
                            } else if (MessagesController.isSupportUser(currentUser)) {
                                i = R.string.SupportStatus;
                            } else {
                                boolean z3 = currentUser.bot;
                                if (z3 && (i2 = currentUser.bot_active_users) != 0) {
                                    charSequence = LocaleController.formatPluralStringComma("BotUsers", i2, ',');
                                } else if (z3) {
                                    i = R.string.Bot;
                                } else {
                                    boolean[] zArr = this.isOnline;
                                    zArr[0] = false;
                                    charSequence = LocaleController.formatUserStatus(this.currentAccount, currentUser, zArr, this.allowShorterStatus ? this.statusMadeShorter : null);
                                    z2 = this.isOnline[0];
                                }
                            }
                        }
                        charSequence = LocaleController.getString(i);
                    }
                }
            }
        }
        this.lastSubtitleColorKey = z2 ? Theme.key_chat_status : Theme.key_actionBarDefaultSubtitle;
        if (this.lastSubtitle != null) {
            this.lastSubtitle = charSequence;
            return;
        }
        SimpleTextView simpleTextView = this.subtitleTextView;
        if (simpleTextView != null) {
            simpleTextView.setText(charSequence);
            Integer num = this.overrideSubtitleColor;
            if (num != null) {
                this.subtitleTextView.setTextColor(num.intValue());
                return;
            } else {
                this.subtitleTextView.setTextColor(getThemedColor(this.lastSubtitleColorKey));
                view = this.subtitleTextView;
            }
        } else {
            this.animatedSubtitleTextView.setText(charSequence, z);
            Integer num2 = this.overrideSubtitleColor;
            if (num2 != null) {
                this.animatedSubtitleTextView.setTextColor(num2.intValue());
                return;
            } else {
                this.animatedSubtitleTextView.setTextColor(getThemedColor(this.lastSubtitleColorKey));
                view = this.animatedSubtitleTextView;
            }
        }
        view.setTag(Integer.valueOf(this.lastSubtitleColorKey));
    }

    protected boolean useAnimatedSubtitle() {
        return false;
    }
}
