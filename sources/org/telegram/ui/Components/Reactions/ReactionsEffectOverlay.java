package org.telegram.ui.Components.Reactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Random;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.SelectAnimatedEmojiDialog;
/* loaded from: classes4.dex */
public class ReactionsEffectOverlay {
    @SuppressLint({"StaticFieldLeak"})
    public static ReactionsEffectOverlay currentOverlay;
    @SuppressLint({"StaticFieldLeak"})
    public static ReactionsEffectOverlay currentShortOverlay;
    private static long lastHapticTime;
    private static int uniqPrefix;
    float animateInProgress;
    float animateOutProgress;
    private final int animationType;
    private ChatMessageCell cell;
    private final FrameLayout container;
    private ViewGroup decorView;
    private float dismissProgress;
    private boolean dismissed;
    private final AnimationView effectImageView;
    private final AnimationView emojiImageView;
    private final AnimationView emojiStaticImageView;
    private final long groupId;
    private ReactionsContainerLayout.ReactionHolderView holderView;
    boolean isFinished;
    public boolean isStories;
    private float lastDrawnToX;
    private float lastDrawnToY;
    private final int messageId;
    private ReactionsEffectOverlay nextReactionOverlay;
    private final ReactionsLayoutInBubble.VisibleReaction reaction;
    public long startTime;
    public boolean started;
    private boolean useWindow;
    private boolean wasScrolled;
    private WindowManager windowManager;
    public FrameLayout windowView;
    int[] loc = new int[2];
    ArrayList<AvatarParticle> avatars = new ArrayList<>();

    static /* synthetic */ float access$216(ReactionsEffectOverlay reactionsEffectOverlay, float f) {
        float f2 = reactionsEffectOverlay.dismissProgress + f;
        reactionsEffectOverlay.dismissProgress = f2;
        return f2;
    }

    /* JADX WARN: Removed duplicated region for block: B:211:0x0659  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x06bc  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01f9  */
    /* JADX WARN: Type inference failed for: r2v32 */
    /* JADX WARN: Type inference failed for: r2v34, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r2v36 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v14 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, View view, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2, boolean z) {
        long j;
        ReactionsLayoutInBubble.ReactionButton reactionButton;
        ChatMessageCell chatMessageCell2;
        float f3;
        float f4;
        float f5;
        int round;
        int sizeForBigReaction;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        ?? r2;
        int i9;
        int i10;
        int i11;
        ?? r7;
        String str;
        this.holderView = null;
        this.isStories = z;
        if (chatMessageCell != null) {
            this.messageId = chatMessageCell.getMessageObject().getId();
            this.groupId = chatMessageCell.getMessageObject().getGroupId();
        } else {
            this.messageId = 0;
            this.groupId = 0L;
        }
        this.reaction = visibleReaction;
        this.animationType = i2;
        this.cell = chatMessageCell;
        ReactionsLayoutInBubble.ReactionButton reactionButton2 = chatMessageCell != null ? chatMessageCell.getReactionButton(visibleReaction) : null;
        if (z && i2 == 2) {
            j = 0;
            reactionButton = reactionButton2;
            chatMessageCell2 = chatMessageCell;
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(context, baseFragment, reactionsContainerLayout, chatMessageCell, view, f, f2, visibleReaction, i, 1, true);
            this.nextReactionOverlay = reactionsEffectOverlay;
            currentShortOverlay = reactionsEffectOverlay;
        } else {
            j = 0;
            reactionButton = reactionButton2;
            chatMessageCell2 = chatMessageCell;
        }
        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
        if (reactionsContainerLayout != null) {
            int i12 = 0;
            while (true) {
                if (i12 < reactionsContainerLayout.recyclerListView.getChildCount()) {
                    if ((reactionsContainerLayout.recyclerListView.getChildAt(i12) instanceof ReactionsContainerLayout.ReactionHolderView) && ((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i12)).currentReaction.equals(this.reaction)) {
                        this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i12);
                        break;
                    }
                    i12++;
                } else {
                    break;
                }
            }
        }
        float f6 = 0.8f;
        if (i2 == 1) {
            Random random = new Random();
            ArrayList<TLRPC$MessagePeerReaction> arrayList = (chatMessageCell2 == null || chatMessageCell.getMessageObject().messageOwner.reactions == null) ? null : chatMessageCell.getMessageObject().messageOwner.reactions.recent_reactions;
            if (arrayList != null && chatActivity != null && chatActivity.getDialogId() < j) {
                int i13 = 0;
                while (i13 < arrayList.size()) {
                    if (this.reaction.equals(arrayList.get(i13).reaction) && arrayList.get(i13).unread) {
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        ImageReceiver imageReceiver = new ImageReceiver();
                        long peerId = MessageObject.getPeerId(arrayList.get(i13).peer_id);
                        if (peerId < j) {
                            TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerId));
                            if (chat != null) {
                                avatarDrawable.setInfo(chat);
                                imageReceiver.setForUserOrChat(chat, avatarDrawable);
                                AvatarParticle avatarParticle = new AvatarParticle(this, null);
                                avatarParticle.imageReceiver = imageReceiver;
                                avatarParticle.fromX = 0.5f;
                                avatarParticle.fromY = 0.5f;
                                float f7 = 100.0f;
                                avatarParticle.jumpY = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                float f8 = 0.4f;
                                avatarParticle.randomScale = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.4f) + f6;
                                avatarParticle.randomRotation = (Math.abs(random.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle.leftTime = (int) (((Math.abs(random.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f9 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                    avatarParticle.toX = ((Math.abs(random.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                    avatarParticle.toY = (Math.abs(random.nextInt() % 100) * 0.4f) / 100.0f;
                                } else {
                                    float f10 = 0.0f;
                                    int i14 = 0;
                                    float f11 = 0.0f;
                                    float f12 = 0.0f;
                                    while (i14 < 10) {
                                        float abs = ((Math.abs(random.nextInt() % 100) * f9) / f7) + 0.2f;
                                        float abs2 = ((Math.abs(random.nextInt() % 100) * f8) / f7) + 0.2f;
                                        float f13 = 2.14748365E9f;
                                        int i15 = 0;
                                        while (i15 < this.avatars.size()) {
                                            float f14 = this.avatars.get(i15).toX - abs;
                                            float f15 = abs;
                                            float f16 = this.avatars.get(i15).toY - abs2;
                                            float f17 = (f14 * f14) + (f16 * f16);
                                            if (f17 < f13) {
                                                f13 = f17;
                                            }
                                            i15++;
                                            abs = f15;
                                        }
                                        float f18 = abs;
                                        if (f13 > f12) {
                                            f11 = abs2;
                                            f12 = f13;
                                            f10 = f18;
                                        }
                                        i14++;
                                        f8 = 0.4f;
                                        f7 = 100.0f;
                                        f9 = 0.6f;
                                    }
                                    avatarParticle.toX = f10;
                                    avatarParticle.toY = f11;
                                }
                                this.avatars.add(avatarParticle);
                            }
                        } else {
                            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerId));
                            if (user != null) {
                                avatarDrawable.setInfo(user);
                                imageReceiver.setForUserOrChat(user, avatarDrawable);
                                AvatarParticle avatarParticle2 = new AvatarParticle(this, null);
                                avatarParticle2.imageReceiver = imageReceiver;
                                avatarParticle2.fromX = 0.5f;
                                avatarParticle2.fromY = 0.5f;
                                float f72 = 100.0f;
                                avatarParticle2.jumpY = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                float f82 = 0.4f;
                                avatarParticle2.randomScale = ((Math.abs(random.nextInt() % 100) / 100.0f) * 0.4f) + f6;
                                avatarParticle2.randomRotation = (Math.abs(random.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle2.leftTime = (int) (((Math.abs(random.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f92 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                }
                                this.avatars.add(avatarParticle2);
                            }
                        }
                    }
                    i13++;
                    f6 = 0.8f;
                }
            }
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
        boolean z2 = (reactionHolderView == null && (f == 0.0f || f2 == 0.0f)) ? false : true;
        if (view != null) {
            view.getLocationOnScreen(this.loc);
            int[] iArr = this.loc;
            float f19 = iArr[0];
            float f20 = iArr[1];
            f5 = view.getWidth() * view.getScaleX();
            if (view instanceof SelectAnimatedEmojiDialog.ImageViewEmoji) {
                float f21 = ((SelectAnimatedEmojiDialog.ImageViewEmoji) view).bigReactionSelectedProgress;
                if (f21 > 0.0f) {
                    f5 = view.getWidth() * ((f21 * 2.0f) + 1.0f);
                    f19 -= (f5 - view.getWidth()) / 2.0f;
                    f20 -= f5 - view.getWidth();
                }
            }
            f4 = f20;
            f3 = f19;
        } else if (reactionHolderView != null) {
            reactionHolderView.getLocationOnScreen(this.loc);
            f3 = this.loc[0] + this.holderView.loopImageView.getX();
            f4 = this.loc[1] + this.holderView.loopImageView.getY();
            f5 = this.holderView.loopImageView.getWidth() * this.holderView.getScaleX();
        } else {
            ReactionsLayoutInBubble.ReactionButton reactionButton3 = reactionButton;
            if (reactionButton3 != null) {
                chatMessageCell2.getLocationInWindow(this.loc);
                float f22 = this.loc[0] + chatMessageCell2.reactionsLayoutInBubble.x + reactionButton3.x;
                ImageReceiver imageReceiver2 = reactionButton3.imageReceiver;
                float imageX = f22 + (imageReceiver2 == null ? 0.0f : imageReceiver2.getImageX());
                float f23 = this.loc[1] + chatMessageCell2.reactionsLayoutInBubble.y + reactionButton3.y;
                ImageReceiver imageReceiver3 = reactionButton3.imageReceiver;
                float imageY = f23 + (imageReceiver3 == null ? 0.0f : imageReceiver3.getImageY());
                ImageReceiver imageReceiver4 = reactionButton3.imageReceiver;
                f5 = imageReceiver4 == null ? 0.0f : imageReceiver4.getImageHeight();
                f3 = imageX;
                f4 = imageY;
            } else {
                if (chatMessageCell2 != null) {
                    ((View) chatMessageCell.getParent()).getLocationInWindow(this.loc);
                    int[] iArr2 = this.loc;
                    f4 = iArr2[1] + f2;
                    f3 = iArr2[0] + f;
                } else {
                    f3 = f;
                    f4 = f2;
                }
                f5 = 0.0f;
            }
        }
        if (i2 == 2) {
            int dp = AndroidUtilities.dp((z && SharedConfig.deviceIsHigh()) ? 60.0f : 34.0f);
            i3 = dp;
            i4 = (int) ((dp * 2.0f) / AndroidUtilities.density);
        } else {
            if (i2 != 1) {
                int dp2 = AndroidUtilities.dp(350.0f);
                Point point = AndroidUtilities.displaySize;
                round = Math.round(Math.min(dp2, Math.min(point.x, point.y)) * 0.8f);
                sizeForBigReaction = sizeForBigReaction();
            } else if (z) {
                int dp3 = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 240.0f : 140.0f);
                i4 = SharedConfig.deviceIsHigh() ? (int) ((AndroidUtilities.dp(80.0f) * 2.0f) / AndroidUtilities.density) : sizeForAroundReaction();
                i3 = dp3;
            } else {
                round = AndroidUtilities.dp(80.0f);
                sizeForBigReaction = sizeForAroundReaction();
            }
            i3 = round;
            i4 = sizeForBigReaction;
        }
        int i16 = i3 >> 1;
        int i17 = i4 >> 1;
        float f24 = f5 / i16;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        FrameLayout frameLayout = new FrameLayout(context);
        this.container = frameLayout;
        int i18 = i4;
        int i19 = i3;
        this.windowView = new 1(context, baseFragment, chatMessageCell, z, chatActivity, i16, i2, z2, f24, f3, f4, visibleReaction);
        AnimationView animationView = new AnimationView(context);
        this.effectImageView = animationView;
        AnimationView animationView2 = new AnimationView(context);
        this.emojiImageView = animationView2;
        AnimationView animationView3 = new AnimationView(context);
        this.emojiStaticImageView = animationView3;
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction = visibleReaction.emojicon != null ? MediaDataController.getInstance(i).getReactionsMap().get(this.reaction.emojicon) : null;
        if (tLRPC$TL_availableReaction != null || visibleReaction.documentId != j) {
            if (tLRPC$TL_availableReaction != null) {
                i5 = i2;
                i6 = 2;
                if (i5 != 2) {
                    if ((i5 == 1 && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_CHAT)) || i5 == 0) {
                        TLRPC$Document tLRPC$Document = i5 == 1 ? tLRPC$TL_availableReaction.around_animation : tLRPC$TL_availableReaction.effect_animation;
                        if (i5 == 1) {
                            str = getFilterForAroundAnimation();
                        } else {
                            str = i18 + "_" + i18;
                        }
                        String str2 = str;
                        ImageReceiver imageReceiver5 = animationView.getImageReceiver();
                        StringBuilder sb = new StringBuilder();
                        int i20 = uniqPrefix;
                        uniqPrefix = i20 + 1;
                        sb.append(i20);
                        sb.append("_");
                        sb.append(this.messageId);
                        sb.append("_");
                        imageReceiver5.setUniqKeyPrefix(sb.toString());
                        animationView.setImage(ImageLocation.getForDocument(tLRPC$Document), str2, (ImageLocation) null, (String) null, 0, (Object) null);
                        r7 = 0;
                        animationView.getImageReceiver().setAutoRepeat(0);
                        animationView.getImageReceiver().setAllowStartAnimation(false);
                    } else {
                        r7 = 0;
                    }
                    if (animationView.getImageReceiver().getLottieAnimation() != null) {
                        animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(r7, r7);
                        animationView.getImageReceiver().getLottieAnimation().start();
                    }
                }
                if (i5 == 2) {
                    TLRPC$Document tLRPC$Document2 = z ? tLRPC$TL_availableReaction.select_animation : tLRPC$TL_availableReaction.appear_animation;
                    ImageReceiver imageReceiver6 = animationView2.getImageReceiver();
                    StringBuilder sb2 = new StringBuilder();
                    int i21 = uniqPrefix;
                    uniqPrefix = i21 + 1;
                    sb2.append(i21);
                    sb2.append("_");
                    sb2.append(this.messageId);
                    sb2.append("_");
                    imageReceiver6.setUniqKeyPrefix(sb2.toString());
                    animationView2.setImage(ImageLocation.getForDocument(tLRPC$Document2), i17 + "_" + i17, (ImageLocation) null, (String) null, 0, (Object) null);
                } else if (i5 == 0) {
                    TLRPC$Document tLRPC$Document3 = tLRPC$TL_availableReaction.activate_animation;
                    ImageReceiver imageReceiver7 = animationView2.getImageReceiver();
                    StringBuilder sb3 = new StringBuilder();
                    int i22 = uniqPrefix;
                    uniqPrefix = i22 + 1;
                    sb3.append(i22);
                    sb3.append("_");
                    sb3.append(this.messageId);
                    sb3.append("_");
                    imageReceiver7.setUniqKeyPrefix(sb3.toString());
                    animationView2.setImage(ImageLocation.getForDocument(tLRPC$Document3), i17 + "_" + i17, (ImageLocation) null, (String) null, 0, (Object) null);
                }
            } else {
                i5 = i2;
                i6 = 2;
                if (i5 == 0) {
                    i7 = i;
                    animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(1, i7, visibleReaction.documentId));
                } else {
                    i7 = i;
                    if (i5 == 2) {
                        animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(2, i7, visibleReaction.documentId));
                    }
                }
                if (i5 == 0 || i5 == 1) {
                    AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(2, i7, visibleReaction.documentId);
                    if (chatMessageCell != null) {
                        if (chatMessageCell.getMessageObject().shouldDrawWithoutBackground()) {
                            i9 = chatMessageCell.getMessageObject().isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground;
                        } else {
                            i9 = chatMessageCell.getMessageObject().isOutOwner() ? Theme.key_chat_outReactionButtonTextSelected : Theme.key_chat_inReactionButtonTextSelected;
                        }
                        i8 = Theme.getColor(i9, baseFragment != null ? baseFragment.getResourceProvider() : null);
                    } else {
                        i8 = -1;
                    }
                    animatedEmojiDrawable.setColorFilter(new PorterDuffColorFilter(i8, PorterDuff.Mode.SRC_IN));
                    boolean z3 = i5 == 0;
                    animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable, z3, !z3));
                    r2 = 0;
                    this.windowView.setClipChildren(false);
                    animationView2.getImageReceiver().setAutoRepeat(r2);
                    animationView2.getImageReceiver().setAllowStartAnimation(r2);
                    if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                        if (i5 == i6) {
                            animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - 1, r2);
                        } else {
                            animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(r2, r2);
                            animationView2.getImageReceiver().getLottieAnimation().start();
                        }
                    }
                    i10 = i19 - i16;
                    i11 = i10 >> 1;
                    i10 = i5 == 1 ? i11 : i10;
                    frameLayout.addView(animationView2);
                    animationView2.getLayoutParams().width = i16;
                    animationView2.getLayoutParams().height = i16;
                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i11;
                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i10;
                    if (i5 != 1 && !z) {
                        if (tLRPC$TL_availableReaction != null) {
                            animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_lastreactframe", null, "webp", tLRPC$TL_availableReaction, 1);
                        }
                        frameLayout.addView(animationView3);
                        animationView3.getLayoutParams().width = i16;
                        animationView3.getLayoutParams().height = i16;
                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i11;
                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i10;
                    }
                    this.windowView.addView(frameLayout);
                    frameLayout.getLayoutParams().width = i19;
                    frameLayout.getLayoutParams().height = i19;
                    int i23 = -i11;
                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i23;
                    int i24 = -i10;
                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i24;
                    this.windowView.addView(animationView);
                    animationView.getLayoutParams().width = i19;
                    animationView.getLayoutParams().height = i19;
                    animationView.getLayoutParams().width = i19;
                    animationView.getLayoutParams().height = i19;
                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i23;
                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i24;
                    frameLayout.setPivotX(i10);
                    frameLayout.setPivotY(i11);
                    return;
                }
            }
            r2 = 0;
            animationView2.getImageReceiver().setAutoRepeat(r2);
            animationView2.getImageReceiver().setAllowStartAnimation(r2);
            if (animationView2.getImageReceiver().getLottieAnimation() != null) {
            }
            i10 = i19 - i16;
            i11 = i10 >> 1;
            if (i5 == 1) {
            }
            frameLayout.addView(animationView2);
            animationView2.getLayoutParams().width = i16;
            animationView2.getLayoutParams().height = i16;
            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i11;
            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i10;
            if (i5 != 1) {
                if (tLRPC$TL_availableReaction != null) {
                }
                frameLayout.addView(animationView3);
                animationView3.getLayoutParams().width = i16;
                animationView3.getLayoutParams().height = i16;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i11;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i10;
            }
            this.windowView.addView(frameLayout);
            frameLayout.getLayoutParams().width = i19;
            frameLayout.getLayoutParams().height = i19;
            int i232 = -i11;
            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i232;
            int i242 = -i10;
            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i242;
            this.windowView.addView(animationView);
            animationView.getLayoutParams().width = i19;
            animationView.getLayoutParams().height = i19;
            animationView.getLayoutParams().width = i19;
            animationView.getLayoutParams().height = i19;
            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i232;
            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i242;
            frameLayout.setPivotX(i10);
            frameLayout.setPivotY(i11);
            return;
        }
        this.dismissed = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 1 extends FrameLayout {
        final /* synthetic */ int val$animationType;
        final /* synthetic */ ChatMessageCell val$cell;
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ int val$emojiSize;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ boolean val$fromHolder;
        final /* synthetic */ float val$fromScale;
        final /* synthetic */ float val$fromX;
        final /* synthetic */ float val$fromY;
        final /* synthetic */ boolean val$isStories;
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$visibleReaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        1(Context context, BaseFragment baseFragment, ChatMessageCell chatMessageCell, boolean z, ChatActivity chatActivity, int i, int i2, boolean z2, float f, float f2, float f3, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(context);
            this.val$fragment = baseFragment;
            this.val$cell = chatMessageCell;
            this.val$isStories = z;
            this.val$chatActivity = chatActivity;
            this.val$emojiSize = i;
            this.val$animationType = i2;
            this.val$fromHolder = z2;
            this.val$fromScale = f;
            this.val$fromX = f2;
            this.val$fromY = f3;
            this.val$visibleReaction = visibleReaction;
        }

        /* JADX WARN: Removed duplicated region for block: B:107:0x02ac  */
        /* JADX WARN: Removed duplicated region for block: B:126:0x0321  */
        /* JADX WARN: Removed duplicated region for block: B:165:0x03e5  */
        /* JADX WARN: Removed duplicated region for block: B:213:0x04fd  */
        /* JADX WARN: Removed duplicated region for block: B:223:0x0559  */
        /* JADX WARN: Removed duplicated region for block: B:231:0x0588  */
        /* JADX WARN: Removed duplicated region for block: B:232:0x058b  */
        /* JADX WARN: Removed duplicated region for block: B:235:0x0651  */
        /* JADX WARN: Removed duplicated region for block: B:240:0x065e  */
        /* JADX WARN: Removed duplicated region for block: B:241:0x0672  */
        /* JADX WARN: Removed duplicated region for block: B:244:0x067c  */
        /* JADX WARN: Removed duplicated region for block: B:248:0x068f  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            ChatMessageCell chatMessageCell;
            int dp;
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            ChatMessageCell chatMessageCell2;
            int i;
            boolean z;
            float f9;
            int i2;
            if (ReactionsEffectOverlay.this.dismissed) {
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    ReactionsEffectOverlay.access$216(ReactionsEffectOverlay.this, 0.10666667f);
                    if (ReactionsEffectOverlay.this.dismissProgress > 1.0f) {
                        ReactionsEffectOverlay.this.dismissProgress = 1.0f;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ReactionsEffectOverlay.1.this.lambda$dispatchDraw$0();
                            }
                        });
                    }
                }
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    setAlpha(1.0f - ReactionsEffectOverlay.this.dismissProgress);
                    super.dispatchDraw(canvas);
                }
                invalidate();
                return;
            }
            ReactionsEffectOverlay reactionsEffectOverlay = ReactionsEffectOverlay.this;
            if (reactionsEffectOverlay.started) {
                if (reactionsEffectOverlay.holderView != null) {
                    ReactionsEffectOverlay.this.holderView.enterImageView.setAlpha(0.0f);
                    ReactionsEffectOverlay.this.holderView.pressedBackupImageView.setAlpha(0.0f);
                }
                BaseFragment baseFragment = this.val$fragment;
                if (baseFragment instanceof ChatActivity) {
                    chatMessageCell = ((ChatActivity) baseFragment).findMessageCell(ReactionsEffectOverlay.this.messageId, false);
                } else {
                    chatMessageCell = this.val$cell;
                }
                if (this.val$isStories) {
                    dp = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 120.0f : 50.0f);
                } else {
                    ChatMessageCell chatMessageCell3 = this.val$cell;
                    if (chatMessageCell3 != null && chatMessageCell3.getMessageObject().shouldDrawReactionsInLayout()) {
                        dp = AndroidUtilities.dp(20.0f);
                    } else {
                        dp = AndroidUtilities.dp(14.0f);
                    }
                }
                float f10 = dp;
                if (chatMessageCell != null) {
                    this.val$cell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                    ReactionsLayoutInBubble.ReactionButton reactionButton = this.val$cell.getReactionButton(ReactionsEffectOverlay.this.reaction);
                    int[] iArr = ReactionsEffectOverlay.this.loc;
                    int i3 = iArr[0];
                    ReactionsLayoutInBubble reactionsLayoutInBubble = this.val$cell.reactionsLayoutInBubble;
                    f = i3 + reactionsLayoutInBubble.x;
                    f2 = iArr[1] + reactionsLayoutInBubble.y;
                    if (reactionButton != null) {
                        int i4 = reactionButton.x;
                        Rect rect = reactionButton.drawingImageRect;
                        f += i4 + rect.left;
                        f2 += reactionButton.y + rect.top;
                    }
                    ChatActivity chatActivity = this.val$chatActivity;
                    if (chatActivity != null) {
                        f2 += chatActivity.drawingChatLisViewYoffset;
                    }
                    if (chatMessageCell.drawPinnedBottom && !chatMessageCell.shouldDrawTimeOnMedia()) {
                        f2 += AndroidUtilities.dp(2.0f);
                    }
                    ReactionsEffectOverlay.this.lastDrawnToX = f;
                    ReactionsEffectOverlay.this.lastDrawnToY = f2;
                } else if (!this.val$isStories) {
                    f = ReactionsEffectOverlay.this.lastDrawnToX;
                    f2 = ReactionsEffectOverlay.this.lastDrawnToY;
                } else {
                    float f11 = f10 / 2.0f;
                    f = (getMeasuredWidth() / 2.0f) - f11;
                    f2 = (getMeasuredHeight() / 2.0f) - f11;
                }
                BaseFragment baseFragment2 = this.val$fragment;
                if (baseFragment2 != null && baseFragment2.getParentActivity() != null && this.val$fragment.getFragmentView() != null && this.val$fragment.getFragmentView().getParent() != null && this.val$fragment.getFragmentView().getVisibility() == 0 && this.val$fragment.getFragmentView() != null) {
                    this.val$fragment.getFragmentView().getLocationOnScreen(ReactionsEffectOverlay.this.loc);
                    setAlpha(((View) this.val$fragment.getFragmentView().getParent()).getAlpha());
                } else if (!this.val$isStories) {
                    return;
                }
                int i5 = this.val$emojiSize;
                float f12 = f - ((i5 - f10) / 2.0f);
                float f13 = f2 - ((i5 - f10) / 2.0f);
                if (this.val$isStories && this.val$animationType == 0) {
                    f12 += AndroidUtilities.dp(40.0f);
                }
                if (this.val$animationType != 1 && !this.val$isStories) {
                    int[] iArr2 = ReactionsEffectOverlay.this.loc;
                    if (f12 < iArr2[0]) {
                        f12 = iArr2[0];
                    }
                    if (this.val$emojiSize + f12 > iArr2[0] + getMeasuredWidth()) {
                        f12 = (ReactionsEffectOverlay.this.loc[0] + getMeasuredWidth()) - this.val$emojiSize;
                    }
                }
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                float interpolation = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateOutProgress);
                if (this.val$animationType == 2) {
                    f3 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(interpolation);
                    f4 = cubicBezierInterpolator.getInterpolation(interpolation);
                } else if (this.val$fromHolder) {
                    f3 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                    f4 = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                } else {
                    f3 = ReactionsEffectOverlay.this.animateInProgress;
                    f4 = f3;
                }
                float f14 = 1.0f - f3;
                float f15 = (this.val$fromScale * f14) + f3;
                float f16 = f10 / this.val$emojiSize;
                if (this.val$animationType == 1) {
                    f15 = 1.0f;
                } else {
                    f12 = (f12 * f3) + (this.val$fromX * f14);
                    f13 = (f13 * f4) + (this.val$fromY * (1.0f - f4));
                }
                ReactionsEffectOverlay.this.effectImageView.setTranslationX(f12);
                ReactionsEffectOverlay.this.effectImageView.setTranslationY(f13);
                float f17 = 1.0f - interpolation;
                ReactionsEffectOverlay.this.effectImageView.setAlpha(f17);
                ReactionsEffectOverlay.this.effectImageView.setScaleX(f15);
                ReactionsEffectOverlay.this.effectImageView.setScaleY(f15);
                int i6 = this.val$animationType;
                if (i6 == 2) {
                    f15 = (this.val$fromScale * f14) + (f16 * f3);
                    f12 = (this.val$fromX * f14) + (f * f3);
                    f5 = this.val$fromY * (1.0f - f4);
                    f6 = f2 * f4;
                } else {
                    if (interpolation != 0.0f) {
                        f15 = (f15 * f17) + (f16 * interpolation);
                        f12 = (f12 * f17) + (f * interpolation);
                        f5 = f13 * f17;
                        f6 = f2 * interpolation;
                    }
                    if (i6 != 1) {
                        if (!this.val$isStories) {
                            ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(interpolation > 0.7f ? (interpolation - 0.7f) / 0.3f : 0.0f);
                        } else {
                            ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(1.0f);
                        }
                    }
                    if (this.val$animationType == 0 && this.val$isStories) {
                        ReactionsEffectOverlay.this.emojiImageView.setAlpha(f17);
                    }
                    ReactionsEffectOverlay.this.container.setTranslationX(f12);
                    ReactionsEffectOverlay.this.container.setTranslationY(f13);
                    ReactionsEffectOverlay.this.container.setScaleX(f15);
                    ReactionsEffectOverlay.this.container.setScaleY(f15);
                    super.dispatchDraw(canvas);
                    if (this.val$animationType != 1 || ReactionsEffectOverlay.this.emojiImageView.wasPlaying) {
                        ReactionsEffectOverlay reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                        f7 = reactionsEffectOverlay2.animateInProgress;
                        if (f7 != 1.0f) {
                            if (this.val$fromHolder) {
                                reactionsEffectOverlay2.animateInProgress = f7 + 0.045714285f;
                            } else {
                                reactionsEffectOverlay2.animateInProgress = f7 + 0.07272727f;
                            }
                            if (reactionsEffectOverlay2.animateInProgress > 1.0f) {
                                reactionsEffectOverlay2.animateInProgress = 1.0f;
                            }
                        }
                    }
                    float f18 = 16.0f;
                    if (this.val$animationType != 2 || ((ReactionsEffectOverlay.this.wasScrolled && this.val$animationType == 0) || ((this.val$animationType != 1 && ReactionsEffectOverlay.this.emojiImageView.wasPlaying && ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()) || ((this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000) || ((this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000)))))) {
                        ReactionsEffectOverlay reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                        f8 = reactionsEffectOverlay3.animateOutProgress;
                        if (f8 != 1.0f) {
                            int i7 = this.val$animationType;
                            if (i7 == 1) {
                                reactionsEffectOverlay3.animateOutProgress = 1.0f;
                            } else {
                                reactionsEffectOverlay3.animateOutProgress = f8 + (16.0f / (i7 == 2 ? 350.0f : 220.0f));
                            }
                            if (reactionsEffectOverlay3.animateOutProgress > 0.7f) {
                                if (this.val$isStories && i7 == 2) {
                                    if (!reactionsEffectOverlay3.isFinished) {
                                        reactionsEffectOverlay3.isFinished = true;
                                        performHapticFeedback(0);
                                        ((ViewGroup) getParent()).addView(ReactionsEffectOverlay.this.nextReactionOverlay.windowView);
                                        ReactionsEffectOverlay.this.nextReactionOverlay.isStories = true;
                                        ReactionsEffectOverlay.this.nextReactionOverlay.started = true;
                                        ReactionsEffectOverlay.this.nextReactionOverlay.startTime = System.currentTimeMillis();
                                        ReactionsEffectOverlay.this.nextReactionOverlay.windowView.setTag(R.id.parent_tag, 1);
                                        animate().scaleX(0.0f).scaleY(0.0f).setStartDelay(1000L).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay.1.1
                                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                            public void onAnimationEnd(Animator animator) {
                                                ReactionsEffectOverlay.this.removeCurrentView();
                                            }
                                        });
                                    }
                                } else {
                                    ReactionsEffectOverlay.startShortAnimation();
                                }
                            }
                            ReactionsEffectOverlay reactionsEffectOverlay4 = ReactionsEffectOverlay.this;
                            if (reactionsEffectOverlay4.animateOutProgress >= 1.0f) {
                                int i8 = this.val$animationType;
                                if ((i8 == 0 || i8 == 2) && (chatMessageCell2 = this.val$cell) != null) {
                                    chatMessageCell2.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay4.reaction);
                                }
                                ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                                if (this.val$animationType == 1) {
                                    ReactionsEffectOverlay.currentShortOverlay = null;
                                } else {
                                    ReactionsEffectOverlay.currentOverlay = null;
                                }
                                ChatMessageCell chatMessageCell4 = this.val$cell;
                                if (chatMessageCell4 != null) {
                                    chatMessageCell4.invalidate();
                                    if (this.val$cell.getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
                                        ((View) this.val$cell.getParent()).invalidate();
                                    }
                                }
                                if (!this.val$isStories || this.val$animationType != 2) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda0
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            ReactionsEffectOverlay.1.this.lambda$dispatchDraw$1();
                                        }
                                    });
                                }
                            }
                        }
                    }
                    if (!ReactionsEffectOverlay.this.avatars.isEmpty() && ReactionsEffectOverlay.this.effectImageView.wasPlaying) {
                        RLottieDrawable lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                        i = 0;
                        while (i < ReactionsEffectOverlay.this.avatars.size()) {
                            AvatarParticle avatarParticle = ReactionsEffectOverlay.this.avatars.get(i);
                            float f19 = avatarParticle.progress;
                            if (lottieAnimation != null && lottieAnimation.isRunning()) {
                                float duration = (float) ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getDuration();
                                if (((int) (duration - ((ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * duration))) >= avatarParticle.leftTime) {
                                    z = false;
                                    if (z) {
                                        float f20 = avatarParticle.outProgress;
                                        if (f20 != 1.0f) {
                                            float f21 = f20 + 0.10666667f;
                                            avatarParticle.outProgress = f21;
                                            if (f21 > 1.0f) {
                                                avatarParticle.outProgress = 1.0f;
                                                ReactionsEffectOverlay.this.avatars.remove(i);
                                                i--;
                                                i2 = 1;
                                                i += i2;
                                                f18 = 16.0f;
                                            }
                                            float f22 = f19 < 0.5f ? f19 / 0.5f : 1.0f - ((f19 - 0.5f) / 0.5f);
                                            float f23 = 1.0f - f19;
                                            float f24 = (avatarParticle.fromX * f23) + (avatarParticle.toX * f19);
                                            float f25 = ((avatarParticle.fromY * f23) + (avatarParticle.toY * f19)) - (avatarParticle.jumpY * f22);
                                            float f26 = avatarParticle.randomScale * f19 * (1.0f - avatarParticle.outProgress);
                                            float x = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f24);
                                            float y = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f25);
                                            int dp2 = AndroidUtilities.dp(f18);
                                            float f27 = dp2;
                                            float f28 = f27 / 2.0f;
                                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setImageCoords(x - f28, y - f28, f27, f27);
                                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setRoundRadius(dp2 >> 1);
                                            canvas.save();
                                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                            canvas.scale(f26, f26, x, y);
                                            canvas.rotate(avatarParticle.currentRotation, x, y);
                                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.draw(canvas);
                                            canvas.restore();
                                            f9 = avatarParticle.progress;
                                            if (f9 < 1.0f) {
                                                float f29 = f9 + 0.045714285f;
                                                avatarParticle.progress = f29;
                                                if (f29 > 1.0f) {
                                                    avatarParticle.progress = 1.0f;
                                                }
                                            }
                                            if (f19 >= 1.0f) {
                                                avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * 16.0f) / 500.0f;
                                            }
                                            if (avatarParticle.incrementRotation) {
                                                float f30 = avatarParticle.currentRotation;
                                                float f31 = avatarParticle.randomRotation;
                                                float f32 = f30 + (f31 / 250.0f);
                                                avatarParticle.currentRotation = f32;
                                                if (f32 > f31) {
                                                    avatarParticle.incrementRotation = false;
                                                }
                                            } else {
                                                float f33 = avatarParticle.currentRotation;
                                                float f34 = avatarParticle.randomRotation;
                                                float f35 = f33 - (f34 / 250.0f);
                                                avatarParticle.currentRotation = f35;
                                                if (f35 < (-f34)) {
                                                    i2 = 1;
                                                    avatarParticle.incrementRotation = true;
                                                    i += i2;
                                                    f18 = 16.0f;
                                                }
                                            }
                                            i2 = 1;
                                            i += i2;
                                            f18 = 16.0f;
                                        }
                                    }
                                    if (f19 < 0.5f) {
                                    }
                                    float f232 = 1.0f - f19;
                                    float f242 = (avatarParticle.fromX * f232) + (avatarParticle.toX * f19);
                                    float f252 = ((avatarParticle.fromY * f232) + (avatarParticle.toY * f19)) - (avatarParticle.jumpY * f22);
                                    float f262 = avatarParticle.randomScale * f19 * (1.0f - avatarParticle.outProgress);
                                    float x2 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f242);
                                    float y2 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f252);
                                    int dp22 = AndroidUtilities.dp(f18);
                                    float f272 = dp22;
                                    float f282 = f272 / 2.0f;
                                    ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setImageCoords(x2 - f282, y2 - f282, f272, f272);
                                    ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setRoundRadius(dp22 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f262, f262, x2, y2);
                                    canvas.rotate(avatarParticle.currentRotation, x2, y2);
                                    ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f9 = avatarParticle.progress;
                                    if (f9 < 1.0f) {
                                    }
                                    if (f19 >= 1.0f) {
                                    }
                                    if (avatarParticle.incrementRotation) {
                                    }
                                    i2 = 1;
                                    i += i2;
                                    f18 = 16.0f;
                                }
                            }
                            z = true;
                            if (z) {
                            }
                            if (f19 < 0.5f) {
                            }
                            float f2322 = 1.0f - f19;
                            float f2422 = (avatarParticle.fromX * f2322) + (avatarParticle.toX * f19);
                            float f2522 = ((avatarParticle.fromY * f2322) + (avatarParticle.toY * f19)) - (avatarParticle.jumpY * f22);
                            float f2622 = avatarParticle.randomScale * f19 * (1.0f - avatarParticle.outProgress);
                            float x22 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f2422);
                            float y22 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f2522);
                            int dp222 = AndroidUtilities.dp(f18);
                            float f2722 = dp222;
                            float f2822 = f2722 / 2.0f;
                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setImageCoords(x22 - f2822, y22 - f2822, f2722, f2722);
                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.setRoundRadius(dp222 >> 1);
                            canvas.save();
                            canvas.translate(0.0f, avatarParticle.globalTranslationY);
                            canvas.scale(f2622, f2622, x22, y22);
                            canvas.rotate(avatarParticle.currentRotation, x22, y22);
                            ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.draw(canvas);
                            canvas.restore();
                            f9 = avatarParticle.progress;
                            if (f9 < 1.0f) {
                            }
                            if (f19 >= 1.0f) {
                            }
                            if (avatarParticle.incrementRotation) {
                            }
                            i2 = 1;
                            i += i2;
                            f18 = 16.0f;
                        }
                    }
                    invalidate();
                    return;
                }
                f13 = f5 + f6;
                if (i6 != 1) {
                }
                if (this.val$animationType == 0) {
                    ReactionsEffectOverlay.this.emojiImageView.setAlpha(f17);
                }
                ReactionsEffectOverlay.this.container.setTranslationX(f12);
                ReactionsEffectOverlay.this.container.setTranslationY(f13);
                ReactionsEffectOverlay.this.container.setScaleX(f15);
                ReactionsEffectOverlay.this.container.setScaleY(f15);
                super.dispatchDraw(canvas);
                if (this.val$animationType != 1) {
                }
                ReactionsEffectOverlay reactionsEffectOverlay22 = ReactionsEffectOverlay.this;
                f7 = reactionsEffectOverlay22.animateInProgress;
                if (f7 != 1.0f) {
                }
                float f182 = 16.0f;
                if (this.val$animationType != 2) {
                }
                ReactionsEffectOverlay reactionsEffectOverlay32 = ReactionsEffectOverlay.this;
                f8 = reactionsEffectOverlay32.animateOutProgress;
                if (f8 != 1.0f) {
                }
                if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
                    RLottieDrawable lottieAnimation2 = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                    i = 0;
                    while (i < ReactionsEffectOverlay.this.avatars.size()) {
                    }
                }
                invalidate();
                return;
            }
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$0() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$1() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.onAttachedToWindow();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.onDetachedFromWindow();
            }
        }
    }

    public static String getFilterForAroundAnimation() {
        return sizeForAroundReaction() + "_" + sizeForAroundReaction() + "_nolimit_pcache";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCurrentView() {
        try {
            if (this.useWindow) {
                this.windowManager.removeView(this.windowView);
            } else {
                AndroidUtilities.removeFromParent(this.windowView);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0069, code lost:
        if (r25 != 2) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0073, code lost:
        if (r1.isShowing() == false) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void show(BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, View view, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2) {
        if (chatMessageCell == null || visibleReaction == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            if (i2 == 2 || i2 == 0) {
                show(baseFragment, null, chatMessageCell, view, 0.0f, 0.0f, visibleReaction, i, 1);
            }
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, chatMessageCell, view, f, f2, visibleReaction, i, i2, false);
            if (i2 == 1) {
                currentShortOverlay = reactionsEffectOverlay;
            } else {
                currentOverlay = reactionsEffectOverlay;
            }
            if (baseFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                if (i2 != 0) {
                }
                ActionBarPopupWindow actionBarPopupWindow = chatActivity.scrimPopupWindow;
                if (actionBarPopupWindow != null) {
                }
            }
            z = false;
            reactionsEffectOverlay.useWindow = z;
            if (z) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.height = -1;
                layoutParams.width = -1;
                layoutParams.type = 1000;
                layoutParams.flags = 65816;
                layoutParams.format = -3;
                WindowManager windowManager = baseFragment.getParentActivity().getWindowManager();
                reactionsEffectOverlay.windowManager = windowManager;
                windowManager.addView(reactionsEffectOverlay.windowView, layoutParams);
            } else {
                FrameLayout frameLayout = (FrameLayout) baseFragment.getParentActivity().getWindow().getDecorView();
                reactionsEffectOverlay.decorView = frameLayout;
                frameLayout.addView(reactionsEffectOverlay.windowView);
            }
            chatMessageCell.invalidate();
            if (chatMessageCell.getCurrentMessagesGroup() == null || chatMessageCell.getParent() == null) {
                return;
            }
            ((View) chatMessageCell.getParent()).invalidate();
        }
    }

    public static void startAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (currentOverlay.animationType != 0 || System.currentTimeMillis() - lastHapticTime <= 200) {
                return;
            }
            lastHapticTime = System.currentTimeMillis();
            currentOverlay.cell.performHapticFeedback(3);
            return;
        }
        startShortAnimation();
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            reactionsEffectOverlay2.cell.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
        }
    }

    public static void startShortAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentShortOverlay;
        if (reactionsEffectOverlay == null || reactionsEffectOverlay.started) {
            return;
        }
        reactionsEffectOverlay.started = true;
        reactionsEffectOverlay.startTime = System.currentTimeMillis();
        if (currentShortOverlay.animationType != 1 || System.currentTimeMillis() - lastHapticTime <= 200) {
            return;
        }
        lastHapticTime = System.currentTimeMillis();
        currentShortOverlay.cell.performHapticFeedback(3);
    }

    public static void removeCurrent(boolean z) {
        int i = 0;
        while (i < 2) {
            ReactionsEffectOverlay reactionsEffectOverlay = i == 0 ? currentOverlay : currentShortOverlay;
            if (reactionsEffectOverlay != null) {
                if (z) {
                    reactionsEffectOverlay.removeCurrentView();
                } else {
                    reactionsEffectOverlay.dismissed = true;
                }
            }
            i++;
        }
        currentShortOverlay = null;
        currentOverlay = null;
    }

    public static boolean isPlaying(int i, long j, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            int i2 = reactionsEffectOverlay.animationType;
            if (i2 == 2 || i2 == 0) {
                long j2 = reactionsEffectOverlay.groupId;
                return ((j2 != 0 && j == j2) || i == reactionsEffectOverlay.messageId) && reactionsEffectOverlay.reaction.equals(visibleReaction);
            }
            return false;
        }
        return false;
    }

    /* loaded from: classes4.dex */
    private class AnimationView extends BackupImageView {
        AnimatedEmojiDrawable animatedEmojiDrawable;
        boolean attached;
        AnimatedEmojiEffect emojiEffect;
        boolean wasPlaying;

        public AnimationView(Context context) {
            super(context);
            getImageReceiver().setFileLoadingPriority(3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.animatedEmojiDrawable.setAlpha(255);
                this.animatedEmojiDrawable.draw(canvas);
                this.wasPlaying = true;
                return;
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.emojiEffect.draw(canvas);
                this.wasPlaying = true;
                return;
            }
            if (getImageReceiver().getLottieAnimation() != null && getImageReceiver().getLottieAnimation().isRunning()) {
                this.wasPlaying = true;
            }
            if (!this.wasPlaying && getImageReceiver().getLottieAnimation() != null && !getImageReceiver().getLottieAnimation().isRunning()) {
                if (ReactionsEffectOverlay.this.animationType == 2 && !ReactionsEffectOverlay.this.isStories) {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(getImageReceiver().getLottieAnimation().getFramesCount() - 1, false);
                } else {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                    getImageReceiver().getLottieAnimation().start();
                }
            }
            super.onDraw(canvas);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attached = true;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.addView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.setView(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attached = false;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.removeView(this);
            }
        }

        public void setAnimatedReactionDrawable(AnimatedEmojiDrawable animatedEmojiDrawable) {
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            this.animatedEmojiDrawable = animatedEmojiDrawable;
            if (!this.attached || animatedEmojiDrawable == null) {
                return;
            }
            animatedEmojiDrawable.addView(this);
        }

        public void setAnimatedEmojiEffect(AnimatedEmojiEffect animatedEmojiEffect) {
            this.emojiEffect = animatedEmojiEffect;
        }
    }

    public static void onScrolled(int i) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.lastDrawnToY -= i;
            if (i != 0) {
                reactionsEffectOverlay.wasScrolled = true;
            }
        }
    }

    public static int sizeForBigReaction() {
        int dp = AndroidUtilities.dp(350.0f);
        Point point = AndroidUtilities.displaySize;
        return (int) (Math.round(Math.min(dp, Math.min(point.x, point.y)) * 0.7f) / AndroidUtilities.density);
    }

    public static int sizeForAroundReaction() {
        return (int) ((AndroidUtilities.dp(40.0f) * 2.0f) / AndroidUtilities.density);
    }

    public static void dismissAll() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.dismissed = true;
        }
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            reactionsEffectOverlay2.dismissed = true;
        }
    }

    /* loaded from: classes4.dex */
    private class AvatarParticle {
        float currentRotation;
        float fromX;
        float fromY;
        float globalTranslationY;
        ImageReceiver imageReceiver;
        boolean incrementRotation;
        float jumpY;
        public int leftTime;
        float outProgress;
        float progress;
        float randomRotation;
        float randomScale;
        float toX;
        float toY;

        private AvatarParticle(ReactionsEffectOverlay reactionsEffectOverlay) {
        }

        /* synthetic */ AvatarParticle(ReactionsEffectOverlay reactionsEffectOverlay, 1 r2) {
            this(reactionsEffectOverlay);
        }
    }
}
