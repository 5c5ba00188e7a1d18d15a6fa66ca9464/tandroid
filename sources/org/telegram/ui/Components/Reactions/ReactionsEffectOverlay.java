package org.telegram.ui.Components.Reactions;

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
/* loaded from: classes.dex */
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
    private boolean finished;
    private final long groupId;
    private ReactionsContainerLayout.ReactionHolderView holderView;
    private float lastDrawnToX;
    private float lastDrawnToY;
    private final int messageId;
    private final ReactionsLayoutInBubble.VisibleReaction reaction;
    long startTime;
    private boolean started;
    private boolean useWindow;
    private boolean wasScrolled;
    private WindowManager windowManager;
    FrameLayout windowView;
    int[] loc = new int[2];
    ArrayList<AvatarParticle> avatars = new ArrayList<>();

    static /* synthetic */ float access$216(ReactionsEffectOverlay reactionsEffectOverlay, float f) {
        float f2 = reactionsEffectOverlay.dismissProgress + f;
        reactionsEffectOverlay.dismissProgress = f2;
        return f2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x03e9  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0411  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0645  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01b7  */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v13 */
    /* JADX WARN: Type inference failed for: r15v14 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, View view, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2) {
        float f3;
        float f4;
        float f5;
        float imageX;
        float imageY;
        int round;
        int sizeForBigReaction;
        int i3;
        int i4;
        FrameLayout frameLayout;
        AnimationView animationView;
        AnimationView animationView2;
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
        int i5;
        int i6;
        ?? r15;
        int i7;
        String str;
        boolean z;
        String str2;
        Random random;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        this.holderView = null;
        this.messageId = chatMessageCell.getMessageObject().getId();
        this.groupId = chatMessageCell.getMessageObject().getGroupId();
        this.reaction = visibleReaction;
        this.animationType = i2;
        this.cell = chatMessageCell;
        ReactionsLayoutInBubble.ReactionButton reactionButton = chatMessageCell.getReactionButton(visibleReaction);
        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
        if (reactionsContainerLayout != null) {
            int i8 = 0;
            while (true) {
                if (i8 < reactionsContainerLayout.recyclerListView.getChildCount()) {
                    if ((reactionsContainerLayout.recyclerListView.getChildAt(i8) instanceof ReactionsContainerLayout.ReactionHolderView) && ((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i8)).currentReaction.equals(this.reaction)) {
                        this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i8);
                        break;
                    }
                    i8++;
                } else {
                    break;
                }
            }
        }
        if (i2 == 1) {
            Random random2 = new Random();
            ArrayList<TLRPC$MessagePeerReaction> arrayList2 = chatMessageCell.getMessageObject().messageOwner.reactions != null ? chatMessageCell.getMessageObject().messageOwner.reactions.recent_reactions : null;
            if (arrayList2 != null && chatActivity != null && chatActivity.getDialogId() < 0) {
                int i9 = 0;
                while (i9 < arrayList2.size()) {
                    if (this.reaction.equals(arrayList2.get(i9).reaction) && arrayList2.get(i9).unread) {
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        ImageReceiver imageReceiver = new ImageReceiver();
                        arrayList = arrayList2;
                        long peerId = MessageObject.getPeerId(arrayList2.get(i9).peer_id);
                        if (peerId < 0) {
                            TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerId));
                            if (chat != null) {
                                avatarDrawable.setInfo(chat);
                                imageReceiver.setForUserOrChat(chat, avatarDrawable);
                                AvatarParticle avatarParticle = new AvatarParticle(this, null);
                                avatarParticle.imageReceiver = imageReceiver;
                                avatarParticle.fromX = 0.5f;
                                avatarParticle.fromY = 0.5f;
                                float f6 = 100.0f;
                                avatarParticle.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f7 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                    avatarParticle.toX = ((Math.abs(random2.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                    avatarParticle.toY = (Math.abs(random2.nextInt() % 100) * 0.4f) / 100.0f;
                                    random = random2;
                                } else {
                                    int i10 = 0;
                                    float f8 = 0.0f;
                                    float f9 = 0.0f;
                                    float f10 = 0.0f;
                                    while (i10 < 10) {
                                        float abs = ((Math.abs(random2.nextInt() % 100) * f7) / f6) + 0.2f;
                                        float abs2 = ((Math.abs(random2.nextInt() % 100) * 0.4f) / f6) + 0.2f;
                                        float f11 = 2.14748365E9f;
                                        Random random3 = random2;
                                        int i11 = 0;
                                        while (i11 < this.avatars.size()) {
                                            float f12 = this.avatars.get(i11).toX - abs;
                                            float f13 = abs;
                                            float f14 = this.avatars.get(i11).toY - abs2;
                                            float f15 = (f12 * f12) + (f14 * f14);
                                            if (f15 < f11) {
                                                f11 = f15;
                                            }
                                            i11++;
                                            abs = f13;
                                        }
                                        float f16 = abs;
                                        if (f11 > f10) {
                                            f9 = abs2;
                                            f10 = f11;
                                            f8 = f16;
                                        }
                                        i10++;
                                        random2 = random3;
                                        f7 = 0.6f;
                                        f6 = 100.0f;
                                    }
                                    random = random2;
                                    avatarParticle.toX = f8;
                                    avatarParticle.toY = f9;
                                }
                                this.avatars.add(avatarParticle);
                            }
                            random = random2;
                        } else {
                            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerId));
                            if (user != null) {
                                avatarDrawable.setInfo(user);
                                imageReceiver.setForUserOrChat(user, avatarDrawable);
                                AvatarParticle avatarParticle2 = new AvatarParticle(this, null);
                                avatarParticle2.imageReceiver = imageReceiver;
                                avatarParticle2.fromX = 0.5f;
                                avatarParticle2.fromY = 0.5f;
                                float f62 = 100.0f;
                                avatarParticle2.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle2.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle2.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle2.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f72 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                }
                                this.avatars.add(avatarParticle2);
                            }
                            random = random2;
                        }
                    } else {
                        random = random2;
                        arrayList = arrayList2;
                    }
                    i9++;
                    arrayList2 = arrayList;
                    random2 = random;
                }
            }
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
        boolean z2 = (reactionHolderView == null && (f == 0.0f || f2 == 0.0f)) ? false : true;
        if (view != null) {
            view.getLocationOnScreen(this.loc);
            int[] iArr = this.loc;
            float f17 = iArr[0];
            float f18 = iArr[1];
            float width = view.getWidth() * view.getScaleX();
            if (view instanceof SelectAnimatedEmojiDialog.ImageViewEmoji) {
                float f19 = ((SelectAnimatedEmojiDialog.ImageViewEmoji) view).bigReactionSelectedProgress;
                if (f19 > 0.0f) {
                    width = view.getWidth() * ((f19 * 2.0f) + 1.0f);
                    f17 -= (width - view.getWidth()) / 2.0f;
                    f18 -= width - view.getWidth();
                }
            }
            f3 = f18;
            f4 = f17;
            f5 = width;
        } else {
            if (reactionHolderView != null) {
                reactionHolderView.getLocationOnScreen(this.loc);
                imageX = this.loc[0] + this.holderView.loopImageView.getX();
                imageY = this.loc[1] + this.holderView.loopImageView.getY();
                f5 = this.holderView.loopImageView.getWidth() * this.holderView.getScaleX();
            } else if (reactionButton != null) {
                chatMessageCell.getLocationInWindow(this.loc);
                float f20 = this.loc[0] + chatMessageCell.reactionsLayoutInBubble.x + reactionButton.x;
                ImageReceiver imageReceiver2 = reactionButton.imageReceiver;
                imageX = f20 + (imageReceiver2 == null ? 0.0f : imageReceiver2.getImageX());
                float f21 = this.loc[1] + chatMessageCell.reactionsLayoutInBubble.y + reactionButton.y;
                ImageReceiver imageReceiver3 = reactionButton.imageReceiver;
                imageY = f21 + (imageReceiver3 == null ? 0.0f : imageReceiver3.getImageY());
                ImageReceiver imageReceiver4 = reactionButton.imageReceiver;
                f5 = imageReceiver4 == null ? 0.0f : imageReceiver4.getImageHeight();
            } else {
                ((View) chatMessageCell.getParent()).getLocationInWindow(this.loc);
                int[] iArr2 = this.loc;
                f3 = iArr2[1] + f2;
                f4 = iArr2[0] + f;
                f5 = 0.0f;
                if (i2 != 2) {
                    int dp = AndroidUtilities.dp(34.0f);
                    sizeForBigReaction = (int) ((dp * 2.0f) / AndroidUtilities.density);
                    i3 = dp;
                } else {
                    if (i2 == 1) {
                        round = AndroidUtilities.dp(80.0f);
                        sizeForBigReaction = sizeForAroundReaction();
                    } else {
                        int dp2 = AndroidUtilities.dp(350.0f);
                        Point point = AndroidUtilities.displaySize;
                        round = Math.round(Math.min(dp2, Math.min(point.x, point.y)) * 0.8f);
                        sizeForBigReaction = sizeForBigReaction();
                    }
                    i3 = round;
                }
                i4 = i3 >> 1;
                int i12 = sizeForBigReaction >> 1;
                this.animateInProgress = 0.0f;
                this.animateOutProgress = 0.0f;
                frameLayout = new FrameLayout(context);
                this.container = frameLayout;
                int i13 = sizeForBigReaction;
                int i14 = i3;
                this.windowView = new 1(context, baseFragment, chatMessageCell, chatActivity, i4, i2, z2, f5 / i4, f4, f3, visibleReaction);
                animationView = new AnimationView(context);
                this.effectImageView = animationView;
                animationView2 = new AnimationView(context);
                this.emojiImageView = animationView2;
                AnimationView animationView3 = new AnimationView(context);
                this.emojiStaticImageView = animationView3;
                tLRPC$TL_availableReaction = visibleReaction.emojicon == null ? MediaDataController.getInstance(i).getReactionsMap().get(this.reaction.emojicon) : null;
                if (tLRPC$TL_availableReaction == null || visibleReaction.documentId != 0) {
                    if (tLRPC$TL_availableReaction == null) {
                        i5 = i2;
                        i6 = 2;
                        if (i5 != 2) {
                            if ((i5 == 1 && LiteMode.isEnabled(16)) || i5 == 0) {
                                TLRPC$Document tLRPC$Document = i5 == 1 ? tLRPC$TL_availableReaction.around_animation : tLRPC$TL_availableReaction.effect_animation;
                                if (i5 == 1) {
                                    str2 = getFilterForAroundAnimation();
                                } else {
                                    str2 = i13 + "_" + i13;
                                }
                                String str3 = str2;
                                ImageReceiver imageReceiver5 = animationView.getImageReceiver();
                                StringBuilder sb = new StringBuilder();
                                int i15 = uniqPrefix;
                                uniqPrefix = i15 + 1;
                                sb.append(i15);
                                sb.append("_");
                                sb.append(chatMessageCell.getMessageObject().getId());
                                sb.append("_");
                                imageReceiver5.setUniqKeyPrefix(sb.toString());
                                animationView.setImage(ImageLocation.getForDocument(tLRPC$Document), str3, (ImageLocation) null, (String) null, 0, (Object) null);
                                z = false;
                                animationView.getImageReceiver().setAutoRepeat(0);
                                animationView.getImageReceiver().setAllowStartAnimation(false);
                            } else {
                                z = false;
                            }
                            r15 = z;
                            if (animationView.getImageReceiver().getLottieAnimation() != null) {
                                animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(z ? 1 : 0, z);
                                animationView.getImageReceiver().getLottieAnimation().start();
                                r15 = z;
                            }
                        } else {
                            r15 = 0;
                        }
                        if (i5 == 2) {
                            TLRPC$Document tLRPC$Document2 = tLRPC$TL_availableReaction.appear_animation;
                            ImageReceiver imageReceiver6 = animationView2.getImageReceiver();
                            StringBuilder sb2 = new StringBuilder();
                            int i16 = uniqPrefix;
                            uniqPrefix = i16 + 1;
                            sb2.append(i16);
                            sb2.append("_");
                            sb2.append(chatMessageCell.getMessageObject().getId());
                            sb2.append("_");
                            imageReceiver6.setUniqKeyPrefix(sb2.toString());
                            animationView2.setImage(ImageLocation.getForDocument(tLRPC$Document2), i12 + "_" + i12, (ImageLocation) null, (String) null, 0, (Object) null);
                        } else if (i5 == 0) {
                            TLRPC$Document tLRPC$Document3 = tLRPC$TL_availableReaction.activate_animation;
                            ImageReceiver imageReceiver7 = animationView2.getImageReceiver();
                            StringBuilder sb3 = new StringBuilder();
                            int i17 = uniqPrefix;
                            uniqPrefix = i17 + 1;
                            sb3.append(i17);
                            sb3.append("_");
                            sb3.append(chatMessageCell.getMessageObject().getId());
                            sb3.append("_");
                            imageReceiver7.setUniqKeyPrefix(sb3.toString());
                            animationView2.setImage(ImageLocation.getForDocument(tLRPC$Document3), i12 + "_" + i12, (ImageLocation) null, (String) null, 0, (Object) null);
                        }
                    } else {
                        i5 = i2;
                        i6 = 2;
                        r15 = 0;
                        r15 = 0;
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
                            if (chatMessageCell.getMessageObject().shouldDrawWithoutBackground()) {
                                str = chatMessageCell.getMessageObject().isOutOwner() ? "chat_outReactionButtonBackground" : "chat_inReactionButtonBackground";
                            } else {
                                str = chatMessageCell.getMessageObject().isOutOwner() ? "chat_outReactionButtonTextSelected" : "chat_inReactionButtonTextSelected";
                            }
                            animatedEmojiDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str, baseFragment.getResourceProvider()), PorterDuff.Mode.SRC_IN));
                            animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable, i5 == 0, true));
                            this.windowView.setClipChildren(false);
                        }
                    }
                    animationView2.getImageReceiver().setAutoRepeat(r15);
                    animationView2.getImageReceiver().setAllowStartAnimation(r15);
                    if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                        if (i5 == i6) {
                            animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - 1, r15);
                        } else {
                            animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(r15, r15);
                            animationView2.getImageReceiver().getLottieAnimation().start();
                        }
                    }
                    int i18 = i14 - i4;
                    int i19 = i18 >> 1;
                    i18 = i5 == 1 ? i19 : i18;
                    frameLayout.addView(animationView2);
                    animationView2.getLayoutParams().width = i4;
                    animationView2.getLayoutParams().height = i4;
                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i19;
                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i18;
                    if (i5 != 1) {
                        if (tLRPC$TL_availableReaction != null) {
                            animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_lastreactframe", null, "webp", tLRPC$TL_availableReaction, 1);
                        }
                        frameLayout.addView(animationView3);
                        animationView3.getLayoutParams().width = i4;
                        animationView3.getLayoutParams().height = i4;
                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i19;
                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i18;
                    }
                    this.windowView.addView(frameLayout);
                    frameLayout.getLayoutParams().width = i14;
                    frameLayout.getLayoutParams().height = i14;
                    int i20 = -i19;
                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i20;
                    int i21 = -i18;
                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i21;
                    this.windowView.addView(animationView);
                    animationView.getLayoutParams().width = i14;
                    animationView.getLayoutParams().height = i14;
                    animationView.getLayoutParams().width = i14;
                    animationView.getLayoutParams().height = i14;
                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i20;
                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i21;
                    frameLayout.setPivotX(i18);
                    frameLayout.setPivotY(i19);
                }
                this.dismissed = true;
                return;
            }
            f4 = imageX;
            f3 = imageY;
        }
        if (i2 != 2) {
        }
        i4 = i3 >> 1;
        int i122 = sizeForBigReaction >> 1;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        frameLayout = new FrameLayout(context);
        this.container = frameLayout;
        int i132 = sizeForBigReaction;
        int i142 = i3;
        this.windowView = new 1(context, baseFragment, chatMessageCell, chatActivity, i4, i2, z2, f5 / i4, f4, f3, visibleReaction);
        animationView = new AnimationView(context);
        this.effectImageView = animationView;
        animationView2 = new AnimationView(context);
        this.emojiImageView = animationView2;
        AnimationView animationView32 = new AnimationView(context);
        this.emojiStaticImageView = animationView32;
        if (visibleReaction.emojicon == null) {
        }
        if (tLRPC$TL_availableReaction == null) {
        }
        if (tLRPC$TL_availableReaction == null) {
        }
        animationView2.getImageReceiver().setAutoRepeat(r15);
        animationView2.getImageReceiver().setAllowStartAnimation(r15);
        if (animationView2.getImageReceiver().getLottieAnimation() != null) {
        }
        int i182 = i142 - i4;
        int i192 = i182 >> 1;
        if (i5 == 1) {
        }
        frameLayout.addView(animationView2);
        animationView2.getLayoutParams().width = i4;
        animationView2.getLayoutParams().height = i4;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i192;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i182;
        if (i5 != 1) {
        }
        this.windowView.addView(frameLayout);
        frameLayout.getLayoutParams().width = i142;
        frameLayout.getLayoutParams().height = i142;
        int i202 = -i192;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i202;
        int i212 = -i182;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i212;
        this.windowView.addView(animationView);
        animationView.getLayoutParams().width = i142;
        animationView.getLayoutParams().height = i142;
        animationView.getLayoutParams().width = i142;
        animationView.getLayoutParams().height = i142;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i202;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i212;
        frameLayout.setPivotX(i182);
        frameLayout.setPivotY(i192);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
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
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$visibleReaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        1(Context context, BaseFragment baseFragment, ChatMessageCell chatMessageCell, ChatActivity chatActivity, int i, int i2, boolean z, float f, float f2, float f3, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(context);
            this.val$fragment = baseFragment;
            this.val$cell = chatMessageCell;
            this.val$chatActivity = chatActivity;
            this.val$emojiSize = i;
            this.val$animationType = i2;
            this.val$fromHolder = z;
            this.val$fromScale = f;
            this.val$fromX = f2;
            this.val$fromY = f3;
            this.val$visibleReaction = visibleReaction;
        }

        /* JADX WARN: Removed duplicated region for block: B:174:0x0473  */
        /* JADX WARN: Removed duplicated region for block: B:182:0x04a2  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x04a5  */
        /* JADX WARN: Removed duplicated region for block: B:186:0x056b  */
        /* JADX WARN: Removed duplicated region for block: B:191:0x0578  */
        /* JADX WARN: Removed duplicated region for block: B:192:0x058c  */
        /* JADX WARN: Removed duplicated region for block: B:195:0x0596  */
        /* JADX WARN: Removed duplicated region for block: B:199:0x05a9  */
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
            boolean z;
            float f5;
            int i;
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
            } else if (ReactionsEffectOverlay.this.started) {
                if (ReactionsEffectOverlay.this.holderView != null) {
                    ReactionsEffectOverlay.this.holderView.enterImageView.setAlpha(0.0f);
                    ReactionsEffectOverlay.this.holderView.pressedBackupImageView.setAlpha(0.0f);
                }
                BaseFragment baseFragment = this.val$fragment;
                if (baseFragment instanceof ChatActivity) {
                    chatMessageCell = ((ChatActivity) baseFragment).findMessageCell(ReactionsEffectOverlay.this.messageId, false);
                } else {
                    chatMessageCell = this.val$cell;
                }
                if (this.val$cell.getMessageObject().shouldDrawReactionsInLayout()) {
                    dp = AndroidUtilities.dp(20.0f);
                } else {
                    dp = AndroidUtilities.dp(14.0f);
                }
                float f6 = dp;
                if (chatMessageCell == null) {
                    f = ReactionsEffectOverlay.this.lastDrawnToX;
                    f2 = ReactionsEffectOverlay.this.lastDrawnToY;
                } else {
                    this.val$cell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                    ReactionsLayoutInBubble.ReactionButton reactionButton = this.val$cell.getReactionButton(ReactionsEffectOverlay.this.reaction);
                    int[] iArr = ReactionsEffectOverlay.this.loc;
                    int i2 = iArr[0];
                    ReactionsLayoutInBubble reactionsLayoutInBubble = this.val$cell.reactionsLayoutInBubble;
                    f = i2 + reactionsLayoutInBubble.x;
                    f2 = iArr[1] + reactionsLayoutInBubble.y;
                    if (reactionButton != null) {
                        int i3 = reactionButton.x;
                        Rect rect = reactionButton.drawingImageRect;
                        f += i3 + rect.left;
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
                }
                if (this.val$fragment.getParentActivity() == null || this.val$fragment.getFragmentView().getParent() == null || this.val$fragment.getFragmentView().getVisibility() != 0 || this.val$fragment.getFragmentView() == null) {
                    return;
                }
                this.val$fragment.getFragmentView().getLocationOnScreen(ReactionsEffectOverlay.this.loc);
                setAlpha(((View) this.val$fragment.getFragmentView().getParent()).getAlpha());
                int i4 = this.val$emojiSize;
                float f7 = f - ((i4 - f6) / 2.0f);
                float f8 = f2 - ((i4 - f6) / 2.0f);
                if (this.val$animationType != 1) {
                    int[] iArr2 = ReactionsEffectOverlay.this.loc;
                    if (f7 < iArr2[0]) {
                        f7 = iArr2[0];
                    }
                    if (i4 + f7 > iArr2[0] + getMeasuredWidth()) {
                        f7 = (ReactionsEffectOverlay.this.loc[0] + getMeasuredWidth()) - this.val$emojiSize;
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
                float f9 = 1.0f - f3;
                float f10 = (this.val$fromScale * f9) + f3;
                float f11 = f6 / this.val$emojiSize;
                if (this.val$animationType == 1) {
                    f10 = 1.0f;
                } else {
                    f7 = (f7 * f3) + (this.val$fromX * f9);
                    f8 = (f8 * f4) + (this.val$fromY * (1.0f - f4));
                }
                ReactionsEffectOverlay.this.effectImageView.setTranslationX(f7);
                ReactionsEffectOverlay.this.effectImageView.setTranslationY(f8);
                float f12 = 1.0f - interpolation;
                ReactionsEffectOverlay.this.effectImageView.setAlpha(f12);
                ReactionsEffectOverlay.this.effectImageView.setScaleX(f10);
                ReactionsEffectOverlay.this.effectImageView.setScaleY(f10);
                int i5 = this.val$animationType;
                if (i5 == 2) {
                    f10 = (this.val$fromScale * f9) + (f11 * f3);
                    f7 = (this.val$fromX * f9) + (f * f3);
                    f8 = (this.val$fromY * (1.0f - f4)) + (f2 * f4);
                } else if (interpolation != 0.0f) {
                    f10 = (f10 * f12) + (f11 * interpolation);
                    f7 = (f7 * f12) + (f * interpolation);
                    f8 = (f8 * f12) + (f2 * interpolation);
                }
                if (i5 != 1) {
                    ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(interpolation > 0.7f ? (interpolation - 0.7f) / 0.3f : 0.0f);
                }
                ReactionsEffectOverlay.this.container.setTranslationX(f7);
                ReactionsEffectOverlay.this.container.setTranslationY(f8);
                ReactionsEffectOverlay.this.container.setScaleX(f10);
                ReactionsEffectOverlay.this.container.setScaleY(f10);
                super.dispatchDraw(canvas);
                if (this.val$animationType == 1 || ReactionsEffectOverlay.this.emojiImageView.wasPlaying) {
                    ReactionsEffectOverlay reactionsEffectOverlay = ReactionsEffectOverlay.this;
                    float f13 = reactionsEffectOverlay.animateInProgress;
                    if (f13 != 1.0f) {
                        if (this.val$fromHolder) {
                            reactionsEffectOverlay.animateInProgress = f13 + 0.045714285f;
                        } else {
                            reactionsEffectOverlay.animateInProgress = f13 + 0.07272727f;
                        }
                        if (reactionsEffectOverlay.animateInProgress > 1.0f) {
                            reactionsEffectOverlay.animateInProgress = 1.0f;
                        }
                    }
                }
                float f14 = 16.0f;
                if (this.val$animationType == 2 || ((ReactionsEffectOverlay.this.wasScrolled && this.val$animationType == 0) || ((this.val$animationType != 1 && ReactionsEffectOverlay.this.emojiImageView.wasPlaying && ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()) || ((this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000) || ((this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000)))))) {
                    ReactionsEffectOverlay reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                    float f15 = reactionsEffectOverlay2.animateOutProgress;
                    if (f15 != 1.0f) {
                        int i6 = this.val$animationType;
                        if (i6 == 1) {
                            reactionsEffectOverlay2.animateOutProgress = 1.0f;
                        } else {
                            reactionsEffectOverlay2.animateOutProgress = f15 + (16.0f / (i6 == 2 ? 350.0f : 220.0f));
                        }
                        if (reactionsEffectOverlay2.animateOutProgress > 0.7f && !reactionsEffectOverlay2.finished) {
                            ReactionsEffectOverlay.startShortAnimation();
                        }
                        ReactionsEffectOverlay reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                        if (reactionsEffectOverlay3.animateOutProgress >= 1.0f) {
                            int i7 = this.val$animationType;
                            if (i7 == 0 || i7 == 2) {
                                this.val$cell.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay3.reaction);
                            }
                            ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                            if (this.val$animationType == 1) {
                                ReactionsEffectOverlay.currentShortOverlay = null;
                            } else {
                                ReactionsEffectOverlay.currentOverlay = null;
                            }
                            this.val$cell.invalidate();
                            if (this.val$cell.getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
                                ((View) this.val$cell.getParent()).invalidate();
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ReactionsEffectOverlay.1.this.lambda$dispatchDraw$1();
                                }
                            });
                        }
                    }
                }
                if (!ReactionsEffectOverlay.this.avatars.isEmpty() && ReactionsEffectOverlay.this.effectImageView.wasPlaying) {
                    RLottieDrawable lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                    int i8 = 0;
                    while (i8 < ReactionsEffectOverlay.this.avatars.size()) {
                        AvatarParticle avatarParticle = ReactionsEffectOverlay.this.avatars.get(i8);
                        float f16 = avatarParticle.progress;
                        if (lottieAnimation != null && lottieAnimation.isRunning()) {
                            float duration = (float) ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getDuration();
                            if (((int) (duration - ((ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * duration))) >= avatarParticle.leftTime) {
                                z = false;
                                if (z) {
                                    float f17 = avatarParticle.outProgress;
                                    if (f17 != 1.0f) {
                                        float f18 = f17 + 0.10666667f;
                                        avatarParticle.outProgress = f18;
                                        if (f18 > 1.0f) {
                                            avatarParticle.outProgress = 1.0f;
                                            ReactionsEffectOverlay.this.avatars.remove(i8);
                                            i8--;
                                            i = 1;
                                            i8 += i;
                                            f14 = 16.0f;
                                        }
                                        float f19 = f16 < 0.5f ? f16 / 0.5f : 1.0f - ((f16 - 0.5f) / 0.5f);
                                        float f20 = 1.0f - f16;
                                        float f21 = (avatarParticle.fromX * f20) + (avatarParticle.toX * f16);
                                        float f22 = ((avatarParticle.fromY * f20) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                                        float f23 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                                        float x = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f21);
                                        float y = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f22);
                                        int dp2 = AndroidUtilities.dp(f14);
                                        float f24 = dp2;
                                        float f25 = f24 / 2.0f;
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x - f25, y - f25, f24, f24);
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp2 >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f23, f23, x, y);
                                        canvas.rotate(avatarParticle.currentRotation, x, y);
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f5 = avatarParticle.progress;
                                        if (f5 < 1.0f) {
                                            float f26 = f5 + 0.045714285f;
                                            avatarParticle.progress = f26;
                                            if (f26 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f16 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * 16.0f) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f27 = avatarParticle.currentRotation;
                                            float f28 = avatarParticle.randomRotation;
                                            float f29 = f27 + (f28 / 250.0f);
                                            avatarParticle.currentRotation = f29;
                                            if (f29 > f28) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f30 = avatarParticle.currentRotation;
                                            float f31 = avatarParticle.randomRotation;
                                            float f32 = f30 - (f31 / 250.0f);
                                            avatarParticle.currentRotation = f32;
                                            if (f32 < (-f31)) {
                                                i = 1;
                                                avatarParticle.incrementRotation = true;
                                                i8 += i;
                                                f14 = 16.0f;
                                            }
                                        }
                                        i = 1;
                                        i8 += i;
                                        f14 = 16.0f;
                                    }
                                }
                                if (f16 < 0.5f) {
                                }
                                float f202 = 1.0f - f16;
                                float f212 = (avatarParticle.fromX * f202) + (avatarParticle.toX * f16);
                                float f222 = ((avatarParticle.fromY * f202) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                                float f232 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                                float x2 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f212);
                                float y2 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f222);
                                int dp22 = AndroidUtilities.dp(f14);
                                float f242 = dp22;
                                float f252 = f242 / 2.0f;
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x2 - f252, y2 - f252, f242, f242);
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp22 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f232, f232, x2, y2);
                                canvas.rotate(avatarParticle.currentRotation, x2, y2);
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                                canvas.restore();
                                f5 = avatarParticle.progress;
                                if (f5 < 1.0f) {
                                }
                                if (f16 >= 1.0f) {
                                }
                                if (avatarParticle.incrementRotation) {
                                }
                                i = 1;
                                i8 += i;
                                f14 = 16.0f;
                            }
                        }
                        z = true;
                        if (z) {
                        }
                        if (f16 < 0.5f) {
                        }
                        float f2022 = 1.0f - f16;
                        float f2122 = (avatarParticle.fromX * f2022) + (avatarParticle.toX * f16);
                        float f2222 = ((avatarParticle.fromY * f2022) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                        float f2322 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                        float x22 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f2122);
                        float y22 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f2222);
                        int dp222 = AndroidUtilities.dp(f14);
                        float f2422 = dp222;
                        float f2522 = f2422 / 2.0f;
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x22 - f2522, y22 - f2522, f2422, f2422);
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp222 >> 1);
                        canvas.save();
                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                        canvas.scale(f2322, f2322, x22, y22);
                        canvas.rotate(avatarParticle.currentRotation, x22, y22);
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                        canvas.restore();
                        f5 = avatarParticle.progress;
                        if (f5 < 1.0f) {
                        }
                        if (f16 >= 1.0f) {
                        }
                        if (avatarParticle.incrementRotation) {
                        }
                        i = 1;
                        i8 += i;
                        f14 = 16.0f;
                    }
                }
                invalidate();
            } else {
                invalidate();
            }
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
                this.decorView.removeView(this.windowView);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0064, code lost:
        if (r24 != 2) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x006e, code lost:
        if (r1.isShowing() == false) goto L38;
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
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, chatMessageCell, view, f, f2, visibleReaction, i, i2);
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

    /* loaded from: classes.dex */
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
                if (ReactionsEffectOverlay.this.animationType == 2) {
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

    /* loaded from: classes.dex */
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
