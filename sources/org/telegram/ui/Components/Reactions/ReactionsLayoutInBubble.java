package org.telegram.ui.Components.Reactions;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$TL_availableEffect;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_reactionCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_reactionEmoji;
import org.telegram.tgnet.TLRPC$TL_reactionPaid;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarsDrawable;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CounterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Stars.StarsReactionsSheet;
/* loaded from: classes3.dex */
public class ReactionsLayoutInBubble {
    private static int animationUniq;
    private int animateFromTotalHeight;
    private boolean animateHeight;
    private boolean animateMove;
    private boolean animateWidth;
    boolean attached;
    int availableWidth;
    public float drawServiceShaderBackground;
    public int fromWidth;
    private float fromX;
    private float fromY;
    public boolean hasPaidReaction;
    public boolean hasUnreadReactions;
    public int height;
    public boolean isEmpty;
    public boolean isSmall;
    private int lastDrawTotalHeight;
    private int lastDrawnWidth;
    private float lastDrawnX;
    private float lastDrawnY;
    public int lastLineX;
    ReactionButton lastSelectedButton;
    float lastX;
    float lastY;
    Runnable longPressRunnable;
    MessageObject messageObject;
    ChatMessageCell parentView;
    public int positionOffsetY;
    boolean pressed;
    Theme.ResourcesProvider resourcesProvider;
    private float scrimProgress;
    private Integer scrimViewReaction;
    public boolean tags;
    public int totalHeight;
    private float touchSlop;
    private boolean wasDrawn;
    public int width;
    public int x;
    public int y;
    private static Paint paint = new Paint(1);
    private static Paint tagPaint = new Paint(1);
    private static Paint cutTagPaint = new Paint(1);
    private static TextPaint textPaint = new TextPaint(1);
    private static final ButtonsComparator comparator = new ButtonsComparator();
    private static int pointer = 1;
    private static final Comparator usersComparator = new Comparator() { // from class: org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int lambda$static$0;
            lambda$static$0 = ReactionsLayoutInBubble.lambda$static$0((TLObject) obj, (TLObject) obj2);
            return lambda$static$0;
        }
    };
    public ArrayList reactionButtons = new ArrayList();
    ArrayList outButtons = new ArrayList();
    HashMap lastDrawingReactionButtons = new HashMap();
    HashMap lastDrawingReactionButtonsTmp = new HashMap();
    HashMap animatedReactions = new HashMap();
    private final RectF scrimRect = new RectF();
    int currentAccount = UserConfig.selectedAccount;

    /* loaded from: classes3.dex */
    private static class ButtonsComparator implements Comparator {
        int currentAccount;
        long dialogId;

        private ButtonsComparator() {
        }

        @Override // java.util.Comparator
        public int compare(ReactionButton reactionButton, ReactionButton reactionButton2) {
            int i;
            int i2;
            int i3 = (this.dialogId > 0L ? 1 : (this.dialogId == 0L ? 0 : -1));
            boolean z = reactionButton.paid;
            boolean z2 = reactionButton2.paid;
            if (i3 >= 0) {
                if (z != z2) {
                    return z ? -1 : 1;
                }
                boolean z3 = reactionButton.isSelected;
                if (z3 != reactionButton2.isSelected) {
                    return z3 ? -1 : 1;
                } else if (z3 && (i = reactionButton.choosenOrder) != (i2 = reactionButton2.choosenOrder)) {
                    return i - i2;
                }
            } else if (z != z2) {
                return z ? -1 : 1;
            } else {
                int i4 = reactionButton.realCount;
                int i5 = reactionButton2.realCount;
                if (i4 != i5) {
                    return i5 - i4;
                }
            }
            return reactionButton.reactionCount.lastDrawnPosition - reactionButton2.reactionCount.lastDrawnPosition;
        }
    }

    /* loaded from: classes3.dex */
    public static class ReactionButton {
        public int animateFromWidth;
        public int animateFromX;
        public int animateFromY;
        public AnimatedEmojiDrawable animatedEmojiDrawable;
        int animatedEmojiDrawableColor;
        public int animationType;
        public boolean attached;
        AvatarsDrawable avatarsDrawable;
        int backgroundColor;
        public final ButtonBounce bounce;
        public boolean choosen;
        public int choosenOrder;
        public int count;
        public String countText;
        public CounterView.CounterDrawable counterDrawable;
        private final int currentAccount;
        public int fromBackgroundColor;
        public int fromTagDotColor;
        public int fromTextColor;
        public boolean hasName;
        public int height;
        public ImageReceiver imageReceiver;
        boolean isSelected;
        private final boolean isSmall;
        public boolean isTag;
        public String key;
        public int lastDrawnBackgroundColor;
        public int lastDrawnTagDotColor;
        public int lastDrawnTextColor;
        public boolean lastImageDrawn;
        public String name;
        public boolean paid;
        private final View parentView;
        private StarsReactionsSheet.Particles particles;
        public AnimatedEmojiDrawable previewAnimatedEmojiDrawable;
        public ImageReceiver previewImageReceiver;
        public TLRPC$Reaction reaction;
        private final TLRPC$ReactionCount reactionCount;
        public int realCount;
        private final Theme.ResourcesProvider resourcesProvider;
        int serviceBackgroundColor;
        int serviceTextColor;
        int textColor;
        public AnimatedTextView.AnimatedTextDrawable textDrawable;
        ArrayList users;
        VisibleReaction visibleReaction;
        public boolean wasDrawn;
        public int width;
        public int x;
        public int y;
        public boolean drawImage = true;
        Rect drawingImageRect = new Rect();
        private RectF bounds = new RectF();
        private RectF rect2 = new RectF();
        private final Path tagPath = new Path();

        public ReactionButton(ReactionButton reactionButton, int i, View view, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
            String l;
            StarsReactionsSheet.Particles particles;
            this.currentAccount = i;
            this.parentView = view;
            this.bounce = new ButtonBounce(view);
            this.resourcesProvider = resourcesProvider;
            this.isTag = z2;
            if (reactionButton != null) {
                this.counterDrawable = reactionButton.counterDrawable;
            }
            if (this.imageReceiver == null) {
                this.imageReceiver = new ImageReceiver();
            }
            if (this.counterDrawable == null) {
                this.counterDrawable = new CounterView.CounterDrawable(view, false, null);
            }
            if (this.textDrawable == null) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
                this.textDrawable = animatedTextDrawable;
                animatedTextDrawable.setAnimationProperties(0.4f, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
                this.textDrawable.setTextSize(AndroidUtilities.dp(13.0f));
                this.textDrawable.setCallback(view);
                this.textDrawable.setTypeface(AndroidUtilities.bold());
                this.textDrawable.setOverrideFullWidth(AndroidUtilities.displaySize.x);
            }
            this.reactionCount = tLRPC$ReactionCount;
            TLRPC$Reaction tLRPC$Reaction = tLRPC$ReactionCount.reaction;
            this.reaction = tLRPC$Reaction;
            this.visibleReaction = VisibleReaction.fromTL(tLRPC$Reaction);
            int i2 = tLRPC$ReactionCount.count;
            this.count = i2;
            this.choosen = tLRPC$ReactionCount.chosen;
            this.realCount = i2;
            this.choosenOrder = tLRPC$ReactionCount.chosen_order;
            this.isSmall = z;
            TLRPC$Reaction tLRPC$Reaction2 = this.reaction;
            if (tLRPC$Reaction2 instanceof TLRPC$TL_reactionPaid) {
                l = "stars";
            } else if (tLRPC$Reaction2 instanceof TLRPC$TL_reactionEmoji) {
                l = ((TLRPC$TL_reactionEmoji) tLRPC$Reaction2).emoticon;
            } else if (!(tLRPC$Reaction2 instanceof TLRPC$TL_reactionCustomEmoji)) {
                throw new RuntimeException("unsupported");
            } else {
                l = Long.toString(((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction2).document_id);
            }
            this.key = l;
            this.imageReceiver.setParentView(view);
            this.isSelected = tLRPC$ReactionCount.chosen;
            CounterView.CounterDrawable counterDrawable = this.counterDrawable;
            counterDrawable.updateVisibility = false;
            counterDrawable.shortFormat = true;
            if (this.reaction != null) {
                VisibleReaction visibleReaction = this.visibleReaction;
                if (visibleReaction.isStar) {
                    this.paid = true;
                    if (LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
                        this.imageReceiver.setImageBitmap(new RLottieDrawable(R.raw.star_reaction_click, "star_reaction_click", AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f)));
                    } else {
                        this.imageReceiver.setImageBitmap(ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.star_reaction).mutate());
                    }
                    if (reactionButton == null || (particles = reactionButton.particles) == null) {
                        particles = new StarsReactionsSheet.Particles(1, SharedConfig.getDevicePerformanceClass() == 2 ? 25 : 8);
                    }
                    this.particles = particles;
                } else if (visibleReaction.emojicon != null) {
                    TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(i).getReactionsMap().get(this.visibleReaction.emojicon);
                    if (tLRPC$TL_availableReaction != null) {
                        this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_lastreactframe", DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.static_icon, Theme.key_windowBackgroundGray, 1.0f), "webp", tLRPC$TL_availableReaction, 1);
                    }
                } else if (visibleReaction.documentId != 0) {
                    this.animatedEmojiDrawable = new AnimatedEmojiDrawable(getCacheType(), i, this.visibleReaction.documentId);
                }
            }
            this.counterDrawable.setSize(AndroidUtilities.dp(26.0f), AndroidUtilities.dp(100.0f));
            this.counterDrawable.textPaint = ReactionsLayoutInBubble.textPaint;
            if (z2) {
                String savedTagName = MessagesController.getInstance(i).getSavedTagName(this.reaction);
                this.name = savedTagName;
                this.hasName = !TextUtils.isEmpty(savedTagName);
            }
            if (this.hasName) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.textDrawable;
                animatedTextDrawable2.setText(Emoji.replaceEmoji(this.name, animatedTextDrawable2.getPaint().getFontMetricsInt(), false), !LocaleController.isRTL);
                if (!drawTextWithCounter()) {
                    this.countText = "";
                    this.counterDrawable.setCount(0, false);
                    this.counterDrawable.setType(2);
                    this.counterDrawable.gravity = 3;
                }
            } else {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable3 = this.textDrawable;
                if (animatedTextDrawable3 != null) {
                    animatedTextDrawable3.setText("", false);
                }
            }
            this.countText = Integer.toString(tLRPC$ReactionCount.count);
            this.counterDrawable.setCount(this.count, false);
            this.counterDrawable.setType(2);
            this.counterDrawable.gravity = 3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drawImage(Canvas canvas, Rect rect, float f) {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            ImageReceiver imageReceiver = (animatedEmojiDrawable == null || animatedEmojiDrawable.getImageReceiver() == null) ? this.imageReceiver : this.animatedEmojiDrawable.getImageReceiver();
            if (imageReceiver != null && rect != null) {
                imageReceiver.setImageCoords(rect);
            }
            AnimatedEmojiDrawable animatedEmojiDrawable2 = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable2 != null && this.animatedEmojiDrawableColor != this.lastDrawnTextColor) {
                int i = this.lastDrawnTextColor;
                this.animatedEmojiDrawableColor = i;
                animatedEmojiDrawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
            }
            boolean z = false;
            if (!this.drawImage || (!this.paid && this.realCount <= 1 && isPlaying() && this.isSelected)) {
                imageReceiver.setAlpha(0.0f);
                imageReceiver.draw(canvas);
                this.lastImageDrawn = false;
                return;
            }
            ImageReceiver imageReceiver2 = getImageReceiver();
            if (imageReceiver2 != null) {
                z = (imageReceiver2.getLottieAnimation() == null || !imageReceiver2.getLottieAnimation().hasBitmap()) ? true : true;
                if (f != 1.0f) {
                    imageReceiver2.setAlpha(f);
                    if (f <= 0.0f) {
                        imageReceiver2.onDetachedFromWindow();
                        removeImageReceiver();
                    }
                } else if (imageReceiver2.getLottieAnimation() != null && !imageReceiver2.getLottieAnimation().isRunning()) {
                    float alpha = imageReceiver2.getAlpha() - 0.08f;
                    if (alpha <= 0.0f) {
                        imageReceiver2.onDetachedFromWindow();
                        removeImageReceiver();
                    } else {
                        imageReceiver2.setAlpha(alpha);
                    }
                    this.parentView.invalidate();
                    z = true;
                }
                imageReceiver2.setImageCoords(imageReceiver.getImageX() - (imageReceiver.getImageWidth() / 2.0f), imageReceiver.getImageY() - (imageReceiver.getImageWidth() / 2.0f), imageReceiver.getImageWidth() * 2.0f, imageReceiver.getImageHeight() * 2.0f);
                imageReceiver2.draw(canvas);
            } else {
                z = true;
            }
            if (z) {
                imageReceiver.draw(canvas);
            }
            this.lastImageDrawn = true;
        }

        private void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint) {
            if (!this.isTag) {
                canvas.drawRoundRect(rectF, f, f, paint);
                return;
            }
            RectF rectF2 = this.bounds;
            if (rectF2.left != rectF.left || rectF2.top != rectF.top || rectF2.right != rectF.right || rectF2.bottom != rectF.bottom) {
                rectF2.set(rectF);
                ReactionsLayoutInBubble.fillTagPath(this.bounds, this.rect2, this.tagPath);
            }
            canvas.drawPath(this.tagPath, paint);
        }

        public void attach() {
            this.attached = true;
            ImageReceiver imageReceiver = this.imageReceiver;
            if (imageReceiver != null) {
                imageReceiver.onAttachedToWindow();
            }
            AvatarsDrawable avatarsDrawable = this.avatarsDrawable;
            if (avatarsDrawable != null) {
                avatarsDrawable.onAttachedToWindow();
            }
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.addView(this.parentView);
            }
        }

        public void attachPreview(View view) {
            if (this.previewImageReceiver == null && this.previewAnimatedEmojiDrawable == null) {
                View view2 = this.parentView;
                View view3 = (view2 == null || !(view2.getParent() instanceof View)) ? this.parentView : (View) this.parentView.getParent();
                if (this.reaction != null) {
                    VisibleReaction visibleReaction = this.visibleReaction;
                    if (visibleReaction.isStar) {
                        return;
                    }
                    if (visibleReaction.emojicon == null) {
                        if (visibleReaction.documentId != 0) {
                            AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(24, this.currentAccount, this.visibleReaction.documentId);
                            this.previewAnimatedEmojiDrawable = animatedEmojiDrawable;
                            animatedEmojiDrawable.addView(view3);
                            return;
                        }
                        return;
                    }
                    TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(this.currentAccount).getReactionsMap().get(this.visibleReaction.emojicon);
                    if (tLRPC$TL_availableReaction == null || tLRPC$TL_availableReaction.activate_animation == null) {
                        return;
                    }
                    SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.static_icon, Theme.key_windowBackgroundGray, 1.0f);
                    ImageReceiver imageReceiver = new ImageReceiver(view3);
                    this.previewImageReceiver = imageReceiver;
                    imageReceiver.setLayerNum(7);
                    this.previewImageReceiver.onAttachedToWindow();
                    this.previewImageReceiver.setRoundRadius(AndroidUtilities.dp(14.0f));
                    this.previewImageReceiver.setAllowStartLottieAnimation(true);
                    this.previewImageReceiver.setAllowStartAnimation(true);
                    this.previewImageReceiver.setAutoRepeat(1);
                    this.previewImageReceiver.setAllowDecodeSingleFrame(true);
                    this.previewImageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.activate_animation), "140_140", svgThumb, null, tLRPC$TL_availableReaction, 1);
                }
            }
        }

        public void detach() {
            this.attached = false;
            ImageReceiver imageReceiver = this.imageReceiver;
            if (imageReceiver != null) {
                imageReceiver.onDetachedFromWindow();
            }
            AvatarsDrawable avatarsDrawable = this.avatarsDrawable;
            if (avatarsDrawable != null) {
                avatarsDrawable.onDetachedFromWindow();
            }
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this.parentView);
            }
            detachPreview();
        }

        public void detachPreview() {
            ImageReceiver imageReceiver = this.previewImageReceiver;
            if (imageReceiver == null && this.previewAnimatedEmojiDrawable == null) {
                return;
            }
            if (imageReceiver != null) {
                imageReceiver.onDetachedFromWindow();
                this.previewImageReceiver = null;
            } else if (this.previewAnimatedEmojiDrawable != null) {
                View view = this.parentView;
                this.previewAnimatedEmojiDrawable.removeView((view == null || !(view.getParent() instanceof View)) ? this.parentView : (View) this.parentView.getParent());
                this.previewAnimatedEmojiDrawable = null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:149:0x0393  */
        /* JADX WARN: Removed duplicated region for block: B:153:0x03af  */
        /* JADX WARN: Removed duplicated region for block: B:154:0x03b7  */
        /* JADX WARN: Removed duplicated region for block: B:162:0x03fb  */
        /* JADX WARN: Removed duplicated region for block: B:164:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, float f, float f2, float f3, float f4, boolean z) {
            float f5;
            float f6;
            float f7;
            int i;
            int i2;
            Paint paint;
            Theme.MessageDrawable currentBackgroundDrawable;
            boolean z2 = true;
            this.wasDrawn = true;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            ImageReceiver imageReceiver = animatedEmojiDrawable != null ? animatedEmojiDrawable.getImageReceiver() : this.imageReceiver;
            if (this.isSmall && imageReceiver != null) {
                imageReceiver.setAlpha(f4);
                this.drawingImageRect.set((int) f, (int) f2, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f));
                imageReceiver.setImageCoords(this.drawingImageRect);
                imageReceiver.setRoundRadius(0);
                drawImage(canvas, this.drawingImageRect, f4);
                return;
            }
            if (!this.choosen) {
                if (this.paid) {
                    this.textColor = -1529086;
                    this.backgroundColor = 1088989954;
                    this.serviceTextColor = -1;
                } else {
                    this.textColor = Theme.getColor(isOutOwner() ? Theme.key_chat_outReactionButtonText : Theme.key_chat_inReactionButtonText, this.resourcesProvider);
                    int color = Theme.getColor(isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground, this.resourcesProvider);
                    this.backgroundColor = color;
                    this.backgroundColor = ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * 0.156f));
                    this.serviceTextColor = Theme.getColor(Theme.key_chat_serviceText, this.resourcesProvider);
                }
                this.serviceBackgroundColor = 0;
            } else if (this.paid) {
                this.backgroundColor = -1529086;
                this.textColor = -1;
                this.serviceTextColor = -1;
                this.serviceBackgroundColor = -1529086;
            } else {
                this.backgroundColor = Theme.getColor(isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground, this.resourcesProvider);
                this.textColor = Theme.getColor(isOutOwner() ? Theme.key_chat_outReactionButtonTextSelected : Theme.key_chat_inReactionButtonTextSelected, this.resourcesProvider);
                this.serviceTextColor = Theme.getColor(isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground, this.resourcesProvider);
                this.serviceBackgroundColor = Theme.getColor(isOutOwner() ? Theme.key_chat_outBubble : Theme.key_chat_inBubble);
            }
            updateColors(f3);
            ReactionsLayoutInBubble.textPaint.setColor(this.lastDrawnTextColor);
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.textDrawable;
            if (animatedTextDrawable != null) {
                animatedTextDrawable.setTextColor(this.lastDrawnTextColor);
            }
            ReactionsLayoutInBubble.paint.setColor(this.lastDrawnBackgroundColor);
            boolean z3 = this.isTag && drawTagDot() && Color.alpha(this.lastDrawnTagDotColor) == 0;
            if (f4 != 1.0f) {
                ReactionsLayoutInBubble.textPaint.setAlpha((int) (ReactionsLayoutInBubble.textPaint.getAlpha() * f4));
                ReactionsLayoutInBubble.paint.setAlpha((int) (ReactionsLayoutInBubble.paint.getAlpha() * f4));
            }
            if (imageReceiver != null) {
                imageReceiver.setAlpha(f4);
            }
            float scale = this.bounce.getScale(0.1f);
            int i3 = this.width;
            if (f3 != 1.0f && this.animationType == 3) {
                i3 = (int) ((i3 * f3) + (this.animateFromWidth * (1.0f - f3)));
            }
            RectF rectF = AndroidUtilities.rectTmp;
            float f8 = i3;
            rectF.set(f, f2, f + f8, this.height + f2);
            if (scale != 1.0f) {
                canvas.save();
                canvas.scale(scale, scale, (f8 / 2.0f) + f, (this.height / 2.0f) + f2);
            } else {
                z2 = false;
            }
            float f9 = this.height / 2.0f;
            if (getDrawServiceShaderBackground() > 0.0f) {
                Paint themePaint = Theme.getThemePaint("paintChatActionBackground", this.resourcesProvider);
                Paint themePaint2 = Theme.getThemePaint("paintChatActionBackgroundDarken", this.resourcesProvider);
                int alpha = themePaint.getAlpha();
                int alpha2 = themePaint2.getAlpha();
                themePaint.setAlpha((int) (alpha * f4 * getDrawServiceShaderBackground()));
                themePaint2.setAlpha((int) (alpha2 * f4 * getDrawServiceShaderBackground()));
                drawRoundRect(canvas, rectF, f9, themePaint);
                Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
                if (resourcesProvider == null ? Theme.hasGradientService() : resourcesProvider.hasGradientService()) {
                    drawRoundRect(canvas, rectF, f9, themePaint2);
                }
                themePaint.setAlpha(alpha);
                themePaint2.setAlpha(alpha2);
            }
            if (z && getDrawServiceShaderBackground() < 1.0f) {
                View view = this.parentView;
                if ((view instanceof ChatMessageCell) && (currentBackgroundDrawable = ((ChatMessageCell) view).getCurrentBackgroundDrawable(false)) != null && !this.isTag) {
                    canvas.drawRoundRect(rectF, f9, f9, currentBackgroundDrawable.getPaint());
                }
            }
            if (z3) {
                rectF.right += AndroidUtilities.dp(4.0f);
                canvas.saveLayerAlpha(rectF, NotificationCenter.didClearDatabase, 31);
                rectF.right -= AndroidUtilities.dp(4.0f);
            }
            if (this.particles != null) {
                LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS);
            }
            drawRoundRect(canvas, rectF, f9, ReactionsLayoutInBubble.paint);
            if (this.isTag && drawTagDot()) {
                if (z3) {
                    paint = ReactionsLayoutInBubble.cutTagPaint;
                } else {
                    ReactionsLayoutInBubble.tagPaint.setColor(this.lastDrawnTagDotColor);
                    ReactionsLayoutInBubble.tagPaint.setAlpha((int) (ReactionsLayoutInBubble.tagPaint.getAlpha() * f4));
                    paint = ReactionsLayoutInBubble.tagPaint;
                }
                canvas.drawCircle(rectF.right - AndroidUtilities.dp(8.4f), rectF.centerY(), AndroidUtilities.dp(2.66f), paint);
            }
            if (z3) {
                canvas.restore();
            }
            if (imageReceiver != null) {
                if (this.paid) {
                    i = AndroidUtilities.dp(22.0f);
                    i2 = AndroidUtilities.dp(4.0f);
                } else if (this.animatedEmojiDrawable != null) {
                    int dp = AndroidUtilities.dp(24.0f);
                    int dp2 = AndroidUtilities.dp(6.0f);
                    imageReceiver.setRoundRadius(AndroidUtilities.dp(6.0f));
                    i = dp;
                    i2 = dp2;
                } else {
                    int dp3 = AndroidUtilities.dp(20.0f);
                    int dp4 = AndroidUtilities.dp(8.0f);
                    imageReceiver.setRoundRadius(0);
                    i = dp3;
                    i2 = dp4;
                }
                int i4 = (int) ((this.height - i) / 2.0f);
                if (this.isTag) {
                    i2 -= AndroidUtilities.dp(2.0f);
                }
                int i5 = ((int) f) + i2;
                int i6 = ((int) f2) + i4;
                this.drawingImageRect.set(i5, i6, i5 + i, i + i6);
                drawImage(canvas, this.drawingImageRect, f4);
            }
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.textDrawable;
            if (animatedTextDrawable2 != null) {
                f5 = 0.0f;
                if (animatedTextDrawable2.isNotEmpty() > 0.0f) {
                    canvas.save();
                    if (!this.hasName || drawTagDot()) {
                        f7 = this.hasName ? 9 : 8;
                    } else {
                        f7 = 10.0f;
                    }
                    canvas.translate(AndroidUtilities.dp(f7) + f + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(2.0f), f2);
                    this.textDrawable.setBounds(0, 0, this.width, this.height);
                    this.textDrawable.draw(canvas);
                    this.textDrawable.setAlpha((int) (255.0f * f4));
                    canvas.restore();
                    f5 = this.textDrawable.getCurrentWidth() + (AndroidUtilities.dp(4.0f) * this.textDrawable.isNotEmpty());
                    if (this.counterDrawable != null && drawCounter()) {
                        canvas.save();
                        if (this.hasName || drawTagDot()) {
                            f6 = this.hasName ? 9 : 8;
                        } else {
                            f6 = 10.0f;
                        }
                        canvas.translate(AndroidUtilities.dp(f6) + f + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(2.0f) + f5 + (!this.paid ? -AndroidUtilities.dp(1.0f) : 0), f2);
                        this.counterDrawable.draw(canvas);
                        canvas.restore();
                    }
                    if (!this.isTag && this.avatarsDrawable != null) {
                        canvas.save();
                        canvas.translate(f + AndroidUtilities.dp(10.0f) + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(2.0f), f2);
                        this.avatarsDrawable.setAlpha(f4);
                        this.avatarsDrawable.setTransitionProgress(f3);
                        this.avatarsDrawable.onDraw(canvas);
                        canvas.restore();
                    }
                    if (z2) {
                        return;
                    }
                    canvas.restore();
                    return;
                }
            } else {
                f5 = 0.0f;
            }
            if (this.counterDrawable != null) {
                canvas.save();
                if (this.hasName) {
                }
                f6 = this.hasName ? 9 : 8;
                canvas.translate(AndroidUtilities.dp(f6) + f + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(2.0f) + f5 + (!this.paid ? -AndroidUtilities.dp(1.0f) : 0), f2);
                this.counterDrawable.draw(canvas);
                canvas.restore();
            }
            if (!this.isTag) {
                canvas.save();
                canvas.translate(f + AndroidUtilities.dp(10.0f) + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(2.0f), f2);
                this.avatarsDrawable.setAlpha(f4);
                this.avatarsDrawable.setTransitionProgress(f3);
                this.avatarsDrawable.onDraw(canvas);
                canvas.restore();
            }
            if (z2) {
            }
        }

        protected boolean drawCounter() {
            int i = this.count;
            return ((i == 0 || (this.isTag && !this.hasName && i == 1)) && this.counterDrawable.countChangeProgress == 1.0f) ? false : true;
        }

        public void drawOverlay(Canvas canvas, float f, float f2, float f3, float f4, boolean z) {
            if (this.particles != null && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(f, f2, this.width + f, this.height + f2);
                float f5 = this.height / 2.0f;
                this.tagPath.rewind();
                this.tagPath.addRoundRect(rectF, f5, f5, Path.Direction.CW);
                this.particles.bounds.set(rectF);
                this.particles.bounds.inset(-AndroidUtilities.dp(4.0f), -AndroidUtilities.dp(4.0f));
                StarsReactionsSheet.Particles particles = this.particles;
                particles.setBounds(particles.bounds);
                this.particles.process();
                View view = this.parentView;
                if (view != null) {
                    view.invalidate();
                }
                this.particles.draw(canvas, ColorUtils.blendARGB(ColorUtils.setAlphaComponent(this.backgroundColor, NotificationCenter.didClearDatabase), ColorUtils.blendARGB(this.serviceTextColor, ColorUtils.setAlphaComponent(this.backgroundColor, NotificationCenter.didClearDatabase), 0.4f), getDrawServiceShaderBackground()));
                canvas.save();
                canvas.clipPath(this.tagPath);
                this.particles.draw(canvas, this.textColor);
                canvas.restore();
            }
        }

        public void drawPreview(View view, Canvas canvas, RectF rectF, float f) {
            if (f <= 0.0f) {
                return;
            }
            ImageReceiver imageReceiver = this.previewImageReceiver;
            if (imageReceiver != null) {
                imageReceiver.setImageCoords(rectF);
                this.previewImageReceiver.setAlpha(f);
                this.previewImageReceiver.draw(canvas);
            } else {
                AnimatedEmojiDrawable animatedEmojiDrawable = this.previewAnimatedEmojiDrawable;
                if (animatedEmojiDrawable != null) {
                    animatedEmojiDrawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                    this.previewAnimatedEmojiDrawable.setAlpha((int) (f * 255.0f));
                    this.previewAnimatedEmojiDrawable.draw(canvas);
                }
            }
            if (view != null) {
                view.invalidate();
            }
        }

        protected boolean drawTagDot() {
            return true;
        }

        protected boolean drawTextWithCounter() {
            return false;
        }

        protected int getCacheType() {
            return this.isTag ? 18 : 3;
        }

        protected float getDrawServiceShaderBackground() {
            return 0.0f;
        }

        protected ImageReceiver getImageReceiver() {
            return null;
        }

        protected boolean isOutOwner() {
            return false;
        }

        protected boolean isPlaying() {
            return false;
        }

        protected void removeImageReceiver() {
        }

        public void setUsers(ArrayList arrayList) {
            this.users = arrayList;
            if (arrayList != null) {
                Collections.sort(arrayList, ReactionsLayoutInBubble.usersComparator);
                if (this.avatarsDrawable == null) {
                    AvatarsDrawable avatarsDrawable = new AvatarsDrawable(this.parentView, false);
                    this.avatarsDrawable = avatarsDrawable;
                    avatarsDrawable.transitionDuration = 250L;
                    avatarsDrawable.transitionInterpolator = ChatListItemAnimator.DEFAULT_INTERPOLATOR;
                    avatarsDrawable.setSize(AndroidUtilities.dp(20.0f));
                    this.avatarsDrawable.width = AndroidUtilities.dp(100.0f);
                    AvatarsDrawable avatarsDrawable2 = this.avatarsDrawable;
                    avatarsDrawable2.height = this.height;
                    avatarsDrawable2.setAvatarsTextSize(AndroidUtilities.dp(22.0f));
                }
                if (this.attached) {
                    this.avatarsDrawable.onAttachedToWindow();
                }
                for (int i = 0; i < arrayList.size() && i != 3; i++) {
                    this.avatarsDrawable.setObject(i, this.currentAccount, (TLObject) arrayList.get(i));
                }
                this.avatarsDrawable.commitTransition(false);
            }
        }

        public void startAnimation() {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            ImageReceiver imageReceiver = (animatedEmojiDrawable == null || animatedEmojiDrawable.getImageReceiver() == null) ? this.imageReceiver : this.animatedEmojiDrawable.getImageReceiver();
            if (imageReceiver != null) {
                RLottieDrawable lottieAnimation = imageReceiver.getLottieAnimation();
                if (lottieAnimation != null) {
                    lottieAnimation.restart(true);
                    return;
                }
                AnimatedFileDrawable animation = imageReceiver.getAnimation();
                if (animation != null) {
                    animation.start();
                }
            }
        }

        public void stopAnimation() {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            ImageReceiver imageReceiver = (animatedEmojiDrawable == null || animatedEmojiDrawable.getImageReceiver() == null) ? this.imageReceiver : this.animatedEmojiDrawable.getImageReceiver();
            if (imageReceiver != null) {
                RLottieDrawable lottieAnimation = imageReceiver.getLottieAnimation();
                if (lottieAnimation != null) {
                    lottieAnimation.stop();
                    return;
                }
                AnimatedFileDrawable animation = imageReceiver.getAnimation();
                if (animation != null) {
                    animation.stop();
                }
            }
        }

        protected void updateColors(float f) {
            this.lastDrawnTextColor = ColorUtils.blendARGB(this.fromTextColor, ColorUtils.blendARGB(this.textColor, this.serviceTextColor, getDrawServiceShaderBackground()), f);
            int blendARGB = ColorUtils.blendARGB(this.fromBackgroundColor, ColorUtils.blendARGB(this.backgroundColor, this.serviceBackgroundColor, getDrawServiceShaderBackground()), f);
            this.lastDrawnBackgroundColor = blendARGB;
            this.lastDrawnTagDotColor = ColorUtils.blendARGB(this.fromTagDotColor, AndroidUtilities.computePerceivedBrightness(blendARGB) > 0.8f ? 0 : 1526726655, f);
        }
    }

    /* loaded from: classes3.dex */
    public class ReactionLayoutButton extends ReactionButton {
        public ReactionLayoutButton(ReactionButton reactionButton, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z, boolean z2) {
            super(reactionButton, ReactionsLayoutInBubble.this.currentAccount, ReactionsLayoutInBubble.this.parentView, tLRPC$ReactionCount, z, z2, ReactionsLayoutInBubble.this.resourcesProvider);
        }

        @Override // org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble.ReactionButton
        protected float getDrawServiceShaderBackground() {
            return ReactionsLayoutInBubble.this.drawServiceShaderBackground;
        }

        @Override // org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble.ReactionButton
        protected ImageReceiver getImageReceiver() {
            return (ImageReceiver) ReactionsLayoutInBubble.this.animatedReactions.get(this.visibleReaction);
        }

        @Override // org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble.ReactionButton
        protected boolean isOutOwner() {
            return ReactionsLayoutInBubble.this.messageObject.isOutOwner();
        }

        @Override // org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble.ReactionButton
        protected boolean isPlaying() {
            return ReactionsEffectOverlay.isPlaying(ReactionsLayoutInBubble.this.messageObject.getId(), ReactionsLayoutInBubble.this.messageObject.getGroupId(), this.visibleReaction);
        }

        @Override // org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble.ReactionButton
        protected void removeImageReceiver() {
            ReactionsLayoutInBubble.this.animatedReactions.remove(this.visibleReaction);
        }
    }

    /* loaded from: classes3.dex */
    public static class VisibleReaction {
        public long documentId;
        public long effectId;
        public String emojicon;
        public long hash;
        public boolean isEffect;
        public boolean isStar;
        public boolean premium;
        public boolean sticker;

        public static VisibleReaction asStar() {
            VisibleReaction visibleReaction = new VisibleReaction();
            visibleReaction.isStar = true;
            return visibleReaction;
        }

        public static VisibleReaction fromCustomEmoji(Long l) {
            VisibleReaction visibleReaction = new VisibleReaction();
            long longValue = l.longValue();
            visibleReaction.documentId = longValue;
            visibleReaction.hash = longValue;
            return visibleReaction;
        }

        public static VisibleReaction fromEmojicon(String str) {
            if (str == null) {
                str = "";
            }
            VisibleReaction visibleReaction = new VisibleReaction();
            if (str.startsWith("animated_")) {
                try {
                    long parseLong = Long.parseLong(str.substring(9));
                    visibleReaction.documentId = parseLong;
                    visibleReaction.hash = parseLong;
                } catch (Exception unused) {
                }
                return visibleReaction;
            }
            visibleReaction.emojicon = str;
            visibleReaction.hash = str.hashCode();
            return visibleReaction;
        }

        public static VisibleReaction fromEmojicon(TLRPC$TL_availableReaction tLRPC$TL_availableReaction) {
            VisibleReaction visibleReaction = new VisibleReaction();
            String str = tLRPC$TL_availableReaction.reaction;
            visibleReaction.emojicon = str;
            visibleReaction.hash = str.hashCode();
            return visibleReaction;
        }

        public static VisibleReaction fromTL(TLRPC$Reaction tLRPC$Reaction) {
            long j;
            VisibleReaction visibleReaction = new VisibleReaction();
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionPaid) {
                visibleReaction.isStar = true;
            } else {
                if (tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji) {
                    String str = ((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon;
                    visibleReaction.emojicon = str;
                    j = str.hashCode();
                } else if (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) {
                    j = ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id;
                    visibleReaction.documentId = j;
                }
                visibleReaction.hash = j;
            }
            return visibleReaction;
        }

        public static VisibleReaction fromTL(TLRPC$TL_availableEffect tLRPC$TL_availableEffect) {
            VisibleReaction visibleReaction = new VisibleReaction();
            visibleReaction.isEffect = true;
            long j = tLRPC$TL_availableEffect.id;
            visibleReaction.effectId = j;
            visibleReaction.sticker = tLRPC$TL_availableEffect.effect_animation_id == 0;
            visibleReaction.documentId = tLRPC$TL_availableEffect.effect_sticker_id;
            visibleReaction.hash = j;
            visibleReaction.premium = tLRPC$TL_availableEffect.premium_required;
            visibleReaction.emojicon = tLRPC$TL_availableEffect.emoticon;
            return visibleReaction;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            VisibleReaction visibleReaction = (VisibleReaction) obj;
            return this.documentId == visibleReaction.documentId && Objects.equals(this.emojicon, visibleReaction.emojicon);
        }

        public VisibleReaction flatten() {
            String findAnimatedEmojiEmoticon;
            long j = this.documentId;
            return (j == 0 || (findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(AnimatedEmojiDrawable.findDocument(UserConfig.selectedAccount, j), null)) == null) ? this : fromEmojicon(findAnimatedEmojiEmoticon);
        }

        public int hashCode() {
            return Objects.hash(this.emojicon, Long.valueOf(this.documentId));
        }

        public boolean isSame(TLRPC$Reaction tLRPC$Reaction) {
            return tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji ? TextUtils.equals(((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon, this.emojicon) : (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == this.documentId;
        }

        public CharSequence toCharSequence(int i) {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(AndroidUtilities.dp(i));
            if (TextUtils.isEmpty(this.emojicon)) {
                SpannableString spannableString = new SpannableString("😀");
                spannableString.setSpan(new AnimatedEmojiSpan(this.documentId, textPaint.getFontMetricsInt()), 0, spannableString.length(), 17);
                return spannableString;
            }
            return Emoji.replaceEmoji(this.emojicon, textPaint.getFontMetricsInt(), false);
        }

        public CharSequence toCharSequence(Paint.FontMetricsInt fontMetricsInt) {
            if (TextUtils.isEmpty(this.emojicon)) {
                SpannableString spannableString = new SpannableString("😀");
                spannableString.setSpan(new AnimatedEmojiSpan(this.documentId, fontMetricsInt), 0, spannableString.length(), 17);
                return spannableString;
            }
            return this.emojicon;
        }

        public String toString() {
            TLRPC$Document findDocument;
            if (TextUtils.isEmpty(this.emojicon)) {
                long j = this.documentId;
                if (j == 0 || (findDocument = AnimatedEmojiDrawable.findDocument(UserConfig.selectedAccount, j)) == null) {
                    return "VisibleReaction{" + this.documentId + ", " + this.emojicon + "}";
                }
                return MessageObject.findAnimatedEmojiEmoticon(findDocument, null);
            }
            return this.emojicon;
        }

        public TLRPC$Reaction toTLReaction() {
            if (this.isStar) {
                return new TLRPC$TL_reactionPaid();
            }
            if (this.emojicon != null) {
                TLRPC$TL_reactionEmoji tLRPC$TL_reactionEmoji = new TLRPC$TL_reactionEmoji();
                tLRPC$TL_reactionEmoji.emoticon = this.emojicon;
                return tLRPC$TL_reactionEmoji;
            }
            TLRPC$TL_reactionCustomEmoji tLRPC$TL_reactionCustomEmoji = new TLRPC$TL_reactionCustomEmoji();
            tLRPC$TL_reactionCustomEmoji.document_id = this.documentId;
            return tLRPC$TL_reactionCustomEmoji;
        }
    }

    public ReactionsLayoutInBubble(ChatMessageCell chatMessageCell) {
        this.parentView = chatMessageCell;
        initPaints(this.resourcesProvider);
        this.touchSlop = ViewConfiguration.get(ApplicationLoader.applicationContext).getScaledTouchSlop();
    }

    public static boolean equalsTLReaction(TLRPC$Reaction tLRPC$Reaction, TLRPC$Reaction tLRPC$Reaction2) {
        return ((tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji) && (tLRPC$Reaction2 instanceof TLRPC$TL_reactionEmoji)) ? TextUtils.equals(((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon, ((TLRPC$TL_reactionEmoji) tLRPC$Reaction2).emoticon) : (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) && (tLRPC$Reaction2 instanceof TLRPC$TL_reactionCustomEmoji) && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction2).document_id;
    }

    private boolean equalsUsersList(ArrayList arrayList, ArrayList arrayList2) {
        if (arrayList == null || arrayList2 == null || arrayList.size() != arrayList2.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TLObject tLObject = (TLObject) arrayList.get(i);
            TLObject tLObject2 = (TLObject) arrayList2.get(i);
            if (tLObject == null || tLObject2 == null || getPeerId(tLObject) != getPeerId(tLObject2)) {
                return false;
            }
        }
        return true;
    }

    public static void fillTagPath(RectF rectF, Path path) {
        fillTagPath(rectF, AndroidUtilities.rectTmp, path);
    }

    public static void fillTagPath(RectF rectF, RectF rectF2, Path path) {
        path.rewind();
        float f = rectF.left;
        rectF2.set(f, rectF.top, AndroidUtilities.dp(12.0f) + f, rectF.top + AndroidUtilities.dp(12.0f));
        path.arcTo(rectF2, -90.0f, -90.0f, false);
        rectF2.set(rectF.left, rectF.bottom - AndroidUtilities.dp(12.0f), rectF.left + AndroidUtilities.dp(12.0f), rectF.bottom);
        path.arcTo(rectF2, -180.0f, -90.0f, false);
        float f2 = rectF.height() > ((float) AndroidUtilities.dp(26.0f)) ? 1.4f : 0.0f;
        float dpf2 = rectF.right - AndroidUtilities.dpf2(9.09f);
        float dpf22 = dpf2 - AndroidUtilities.dpf2(0.056f);
        float dpf23 = dpf2 + AndroidUtilities.dpf2(1.22f);
        float dpf24 = dpf2 + AndroidUtilities.dpf2(3.07f);
        float dpf25 = dpf2 + AndroidUtilities.dpf2(2.406f);
        float dpf26 = dpf2 + AndroidUtilities.dpf2(8.27f + f2);
        float dpf27 = dpf2 + AndroidUtilities.dpf2(8.923f + f2);
        float dpf28 = rectF.top + AndroidUtilities.dpf2(1.753f);
        float dpf29 = rectF.bottom - AndroidUtilities.dpf2(1.753f);
        float dpf210 = rectF.top + AndroidUtilities.dpf2(0.663f);
        float dpf211 = rectF.bottom - AndroidUtilities.dpf2(0.663f);
        float f3 = 10.263f + f2;
        float dpf212 = rectF.top + AndroidUtilities.dpf2(f3);
        float dpf213 = rectF.bottom - AndroidUtilities.dpf2(f3);
        float f4 = f2 + 11.333f;
        float dpf214 = rectF.top + AndroidUtilities.dpf2(f4);
        float dpf215 = rectF.bottom - AndroidUtilities.dpf2(f4);
        path.lineTo(dpf22, rectF.bottom);
        path.cubicTo(dpf23, rectF.bottom, dpf25, dpf211, dpf24, dpf29);
        path.lineTo(dpf26, dpf213);
        path.cubicTo(dpf27, dpf215, dpf27, dpf214, dpf26, dpf212);
        path.lineTo(dpf24, dpf28);
        float f5 = rectF.top;
        path.cubicTo(dpf25, dpf210, dpf23, f5, dpf22, f5);
        path.close();
    }

    private static long getPeerId(TLObject tLObject) {
        if (tLObject instanceof TLRPC$User) {
            return ((TLRPC$User) tLObject).id;
        }
        if (tLObject instanceof TLRPC$Chat) {
            return ((TLRPC$Chat) tLObject).id;
        }
        return 0L;
    }

    public static void initPaints(Theme.ResourcesProvider resourcesProvider) {
        paint.setColor(Theme.getColor(Theme.key_chat_inLoader, resourcesProvider));
        textPaint.setColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint.setTypeface(AndroidUtilities.bold());
        cutTagPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkTouchEvent$1(ReactionButton reactionButton) {
        this.parentView.getDelegate().didPressReaction(this.parentView, reactionButton.reactionCount, true, 0.0f, 0.0f);
        reactionButton.bounce.setPressed(false);
        this.lastSelectedButton = null;
        this.pressed = false;
        this.longPressRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$0(TLObject tLObject, TLObject tLObject2) {
        return (int) (getPeerId(tLObject) - getPeerId(tLObject2));
    }

    public static boolean reactionsEqual(TLRPC$Reaction tLRPC$Reaction, TLRPC$Reaction tLRPC$Reaction2) {
        if (!(tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji)) {
            return (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) && (tLRPC$Reaction2 instanceof TLRPC$TL_reactionCustomEmoji) && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction2).document_id;
        } else if (tLRPC$Reaction2 instanceof TLRPC$TL_reactionEmoji) {
            return TextUtils.equals(((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon, ((TLRPC$TL_reactionEmoji) tLRPC$Reaction2).emoticon);
        } else {
            return false;
        }
    }

    public boolean animateChange() {
        AvatarsDrawable avatarsDrawable;
        CounterView.CounterDrawable counterDrawable;
        if (this.messageObject == null) {
            return false;
        }
        this.lastDrawingReactionButtonsTmp.clear();
        for (int i = 0; i < this.outButtons.size(); i++) {
            ((ReactionButton) this.outButtons.get(i)).detach();
        }
        this.outButtons.clear();
        this.lastDrawingReactionButtonsTmp.putAll(this.lastDrawingReactionButtons);
        boolean z = false;
        for (int i2 = 0; i2 < this.reactionButtons.size(); i2++) {
            ReactionButton reactionButton = (ReactionButton) this.reactionButtons.get(i2);
            ReactionButton reactionButton2 = (ReactionButton) this.lastDrawingReactionButtonsTmp.get(reactionButton.key);
            if (reactionButton2 != null && reactionButton.isSmall != reactionButton2.isSmall) {
                reactionButton2 = null;
            }
            if (reactionButton2 != null) {
                this.lastDrawingReactionButtonsTmp.remove(reactionButton.key);
                int i3 = reactionButton.x;
                int i4 = reactionButton2.x;
                if (i3 == i4 && reactionButton.y == reactionButton2.y && reactionButton.width == reactionButton2.width && reactionButton.count == reactionButton2.count && reactionButton.choosen == reactionButton2.choosen && reactionButton.avatarsDrawable == null && reactionButton2.avatarsDrawable == null) {
                    reactionButton.animationType = 0;
                } else {
                    reactionButton.animateFromX = i4;
                    reactionButton.animateFromY = reactionButton2.y;
                    reactionButton.animateFromWidth = reactionButton2.width;
                    reactionButton.fromTextColor = reactionButton2.lastDrawnTextColor;
                    reactionButton.fromBackgroundColor = reactionButton2.lastDrawnBackgroundColor;
                    reactionButton.fromTagDotColor = reactionButton2.lastDrawnTagDotColor;
                    reactionButton.animationType = 3;
                    int i5 = reactionButton.count;
                    int i6 = reactionButton2.count;
                    if (i5 != i6 && (counterDrawable = reactionButton.counterDrawable) != null) {
                        counterDrawable.setCount(i6, false);
                        reactionButton.counterDrawable.setCount(reactionButton.count, true);
                    }
                    AvatarsDrawable avatarsDrawable2 = reactionButton.avatarsDrawable;
                    if (avatarsDrawable2 != null || reactionButton2.avatarsDrawable != null) {
                        if (avatarsDrawable2 == null) {
                            reactionButton.setUsers(new ArrayList());
                        }
                        if (reactionButton2.avatarsDrawable == null) {
                            reactionButton2.setUsers(new ArrayList());
                        }
                        if (!equalsUsersList(reactionButton2.users, reactionButton.users) && (avatarsDrawable = reactionButton.avatarsDrawable) != null) {
                            avatarsDrawable.animateFromState(reactionButton2.avatarsDrawable, this.currentAccount, false);
                        }
                    }
                }
            } else {
                reactionButton.animationType = 1;
            }
            z = true;
        }
        if (!this.lastDrawingReactionButtonsTmp.isEmpty()) {
            this.outButtons.addAll(this.lastDrawingReactionButtonsTmp.values());
            for (int i7 = 0; i7 < this.outButtons.size(); i7++) {
                ((ReactionButton) this.outButtons.get(i7)).drawImage = ((ReactionButton) this.outButtons.get(i7)).lastImageDrawn;
                ((ReactionButton) this.outButtons.get(i7)).attach();
            }
            z = true;
        }
        if (this.wasDrawn) {
            float f = this.lastDrawnX;
            if (f != this.x || this.lastDrawnY != this.y) {
                this.animateMove = true;
                this.fromX = f;
                this.fromY = this.lastDrawnY;
                z = true;
            }
        }
        int i8 = this.lastDrawnWidth;
        if (i8 != this.width) {
            this.animateWidth = true;
            this.fromWidth = i8;
            z = true;
        }
        int i9 = this.lastDrawTotalHeight;
        if (i9 != this.totalHeight) {
            this.animateHeight = true;
            this.animateFromTotalHeight = i9;
            return true;
        }
        return z;
    }

    public void animateReaction(VisibleReaction visibleReaction) {
        if (visibleReaction.documentId != 0 || this.animatedReactions.get(visibleReaction) != null) {
            if (!this.tags || visibleReaction.documentId == 0) {
                return;
            }
            for (int i = 0; i < this.reactionButtons.size(); i++) {
                if (visibleReaction.isSame(((ReactionButton) this.reactionButtons.get(i)).reaction)) {
                    ((ReactionButton) this.reactionButtons.get(i)).startAnimation();
                    return;
                }
            }
            return;
        }
        ImageReceiver imageReceiver = new ImageReceiver();
        imageReceiver.setParentView(this.parentView);
        int i2 = animationUniq;
        animationUniq = i2 + 1;
        imageReceiver.setUniqKeyPrefix(Integer.toString(i2));
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(this.currentAccount).getReactionsMap().get(visibleReaction.emojicon);
        if (tLRPC$TL_availableReaction != null) {
            imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_nolimit", null, "tgs", tLRPC$TL_availableReaction, 1);
        }
        imageReceiver.setAutoRepeat(0);
        imageReceiver.onAttachedToWindow();
        this.animatedReactions.put(visibleReaction, imageReceiver);
    }

    public boolean checkTouchEvent(MotionEvent motionEvent) {
        MessageObject messageObject;
        TLRPC$Message tLRPC$Message;
        int i = 0;
        if (this.isEmpty || this.isSmall || (messageObject = this.messageObject) == null || (tLRPC$Message = messageObject.messageOwner) == null || tLRPC$Message.reactions == null) {
            return false;
        }
        float x = motionEvent.getX() - this.x;
        float y = motionEvent.getY() - this.y;
        if (motionEvent.getAction() == 0) {
            int size = this.reactionButtons.size();
            while (true) {
                if (i >= size) {
                    break;
                } else if (x <= ((ReactionButton) this.reactionButtons.get(i)).x || x >= ((ReactionButton) this.reactionButtons.get(i)).x + ((ReactionButton) this.reactionButtons.get(i)).width || y <= ((ReactionButton) this.reactionButtons.get(i)).y || y >= ((ReactionButton) this.reactionButtons.get(i)).y + ((ReactionButton) this.reactionButtons.get(i)).height) {
                    i++;
                } else {
                    this.lastX = motionEvent.getX();
                    this.lastY = motionEvent.getY();
                    this.lastSelectedButton = (ReactionButton) this.reactionButtons.get(i);
                    Runnable runnable = this.longPressRunnable;
                    if (runnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.longPressRunnable = null;
                    }
                    this.lastSelectedButton.bounce.setPressed(true);
                    final ReactionButton reactionButton = this.lastSelectedButton;
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ReactionsLayoutInBubble.this.lambda$checkTouchEvent$1(reactionButton);
                        }
                    };
                    this.longPressRunnable = runnable2;
                    AndroidUtilities.runOnUIThread(runnable2, ViewConfiguration.getLongPressTimeout());
                    this.pressed = true;
                }
            }
        } else if (motionEvent.getAction() == 2) {
            if ((this.pressed && Math.abs(motionEvent.getX() - this.lastX) > this.touchSlop) || Math.abs(motionEvent.getY() - this.lastY) > this.touchSlop) {
                this.pressed = false;
                ReactionButton reactionButton2 = this.lastSelectedButton;
                if (reactionButton2 != null) {
                    reactionButton2.bounce.setPressed(false);
                }
                this.lastSelectedButton = null;
                Runnable runnable3 = this.longPressRunnable;
                if (runnable3 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable3);
                    this.longPressRunnable = null;
                }
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            Runnable runnable4 = this.longPressRunnable;
            if (runnable4 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable4);
                this.longPressRunnable = null;
            }
            if (this.pressed && this.lastSelectedButton != null && motionEvent.getAction() == 1 && this.parentView.getDelegate() != null) {
                this.parentView.getDelegate().didPressReaction(this.parentView, this.lastSelectedButton.reactionCount, false, motionEvent.getX(), motionEvent.getY());
            }
            this.pressed = false;
            ReactionButton reactionButton3 = this.lastSelectedButton;
            if (reactionButton3 != null) {
                reactionButton3.bounce.setPressed(false);
            }
            this.lastSelectedButton = null;
        }
        return this.pressed;
    }

    public void draw(Canvas canvas, float f, Integer num) {
        float f2;
        if (this.isEmpty && this.outButtons.isEmpty()) {
            return;
        }
        float f3 = this.x;
        float f4 = this.y;
        if (this.isEmpty) {
            f3 = this.lastDrawnX;
            f4 = this.lastDrawnY;
        } else if (this.animateMove) {
            float f5 = 1.0f - f;
            f3 = (f3 * f) + (this.fromX * f5);
            f4 = (f4 * f) + (this.fromY * f5);
        }
        float f6 = f3;
        float f7 = f4;
        for (int i = 0; i < this.reactionButtons.size(); i++) {
            ReactionButton reactionButton = (ReactionButton) this.reactionButtons.get(i);
            if (this.scrimViewReaction == null && num == null && this.scrimProgress < 0.5f) {
                reactionButton.detachPreview();
            }
            if (!Integer.valueOf(reactionButton.reaction.hashCode()).equals(this.scrimViewReaction) && (num == null || reactionButton.reaction.hashCode() == num.intValue())) {
                canvas.save();
                float f8 = reactionButton.x;
                float f9 = reactionButton.y;
                if (f != 1.0f && reactionButton.animationType == 3) {
                    float f10 = 1.0f - f;
                    f8 = (f8 * f) + (reactionButton.animateFromX * f10);
                    f9 = (f9 * f) + (reactionButton.animateFromY * f10);
                }
                if (f == 1.0f || reactionButton.animationType != 1) {
                    f2 = 1.0f;
                } else {
                    float f11 = (f * 0.5f) + 0.5f;
                    canvas.scale(f11, f11, f6 + f8 + (reactionButton.width / 2.0f), f7 + f9 + (reactionButton.height / 2.0f));
                    f2 = f;
                }
                reactionButton.draw(canvas, f6 + f8, f7 + f9, reactionButton.animationType == 3 ? f : 1.0f, f2, num != null);
                canvas.restore();
            }
        }
        for (int i2 = 0; i2 < this.outButtons.size(); i2++) {
            ReactionButton reactionButton2 = (ReactionButton) this.outButtons.get(i2);
            float f12 = 1.0f - f;
            float f13 = (f12 * 0.5f) + 0.5f;
            canvas.save();
            canvas.scale(f13, f13, reactionButton2.x + f6 + (reactionButton2.width / 2.0f), reactionButton2.y + f7 + (reactionButton2.height / 2.0f));
            ((ReactionButton) this.outButtons.get(i2)).draw(canvas, reactionButton2.x + f6, f7 + reactionButton2.y, 1.0f, f12, false);
            canvas.restore();
        }
    }

    public void drawOverlay(Canvas canvas, float f) {
        ReactionButton reactionButton;
        float f2;
        if (this.isEmpty && this.outButtons.isEmpty()) {
            return;
        }
        float f3 = this.x;
        float f4 = this.y;
        if (this.isEmpty) {
            f3 = this.lastDrawnX;
            f4 = this.lastDrawnY;
        } else if (this.animateMove) {
            float f5 = 1.0f - f;
            f3 = (f3 * f) + (this.fromX * f5);
            f4 = (f4 * f) + (this.fromY * f5);
        }
        float f6 = f3;
        float f7 = f4;
        for (int i = 0; i < this.reactionButtons.size(); i++) {
            ReactionButton reactionButton2 = (ReactionButton) this.reactionButtons.get(i);
            if (reactionButton2.paid) {
                canvas.save();
                float f8 = reactionButton2.x;
                float f9 = reactionButton2.y;
                if (f != 1.0f && reactionButton2.animationType == 3) {
                    float f10 = 1.0f - f;
                    f8 = (f8 * f) + (reactionButton2.animateFromX * f10);
                    f9 = (f9 * f) + (reactionButton2.animateFromY * f10);
                }
                if (f == 1.0f || reactionButton2.animationType != 1) {
                    f2 = 1.0f;
                } else {
                    float f11 = (f * 0.5f) + 0.5f;
                    canvas.scale(f11, f11, f6 + f8 + (reactionButton2.width / 2.0f), f7 + f9 + (reactionButton2.height / 2.0f));
                    f2 = f;
                }
                reactionButton2.drawOverlay(canvas, f6 + f8, f7 + f9, reactionButton2.animationType == 3 ? f : 1.0f, f2, false);
                canvas.restore();
            }
        }
        for (int i2 = 0; i2 < this.outButtons.size(); i2++) {
            if (((ReactionButton) this.outButtons.get(i2)).paid) {
                float f12 = 1.0f - f;
                float f13 = (f12 * 0.5f) + 0.5f;
                canvas.save();
                canvas.scale(f13, f13, reactionButton.x + f6 + (reactionButton.width / 2.0f), reactionButton.y + f7 + (reactionButton.height / 2.0f));
                ((ReactionButton) this.outButtons.get(i2)).drawOverlay(canvas, reactionButton.x + f6, f7 + reactionButton.y, 1.0f, f12, false);
                canvas.restore();
            }
        }
    }

    public void drawPreview(View view, Canvas canvas, int i, Integer num) {
        ChatMessageCell chatMessageCell;
        if (this.isEmpty && this.outButtons.isEmpty()) {
            return;
        }
        for (int i2 = 0; i2 < this.reactionButtons.size(); i2++) {
            ReactionButton reactionButton = (ReactionButton) this.reactionButtons.get(i2);
            if ((num == null || reactionButton.reaction.hashCode() == num.intValue()) && num != null) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(reactionButton.drawingImageRect);
                float dp = AndroidUtilities.dp(140.0f);
                float dp2 = AndroidUtilities.dp(14.0f);
                float clamp = Utilities.clamp(rectF.left - AndroidUtilities.dp(12.0f), ((this.parentView != null ? chatMessageCell.getParentWidth() : AndroidUtilities.displaySize.x) - dp) - AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
                RectF rectF2 = this.scrimRect;
                float f = rectF.top - dp2;
                float f2 = i;
                rectF2.set(clamp, (f - dp) + f2, dp + clamp, f + f2);
                float interpolation = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.scrimProgress);
                RectF rectF3 = this.scrimRect;
                AndroidUtilities.lerp(rectF, rectF3, interpolation, rectF3);
                reactionButton.attachPreview(view);
                Rect rect = AndroidUtilities.rectTmp2;
                RectF rectF4 = this.scrimRect;
                rect.set((int) rectF4.left, (int) rectF4.top, (int) rectF4.right, (int) rectF4.bottom);
                float f3 = 1.0f - interpolation;
                if (f3 > 0.0f) {
                    canvas.saveLayerAlpha(this.scrimRect, (int) (f3 * 255.0f), 31);
                    reactionButton.drawImage(canvas, rect, 1.0f);
                    canvas.restore();
                }
                reactionButton.drawPreview(view, canvas, this.scrimRect, interpolation);
            }
        }
    }

    public float getCurrentTotalHeight(float f) {
        return this.animateHeight ? (this.animateFromTotalHeight * (1.0f - f)) + (this.totalHeight * f) : this.totalHeight;
    }

    public float getCurrentWidth(float f) {
        return this.animateWidth ? (this.fromWidth * (1.0f - f)) + (this.width * f) : this.width;
    }

    public ReactionButton getReactionButton(String str) {
        if (this.isSmall) {
            HashMap hashMap = this.lastDrawingReactionButtons;
            ReactionButton reactionButton = (ReactionButton) hashMap.get(str + "_");
            if (reactionButton != null) {
                return reactionButton;
            }
        }
        return (ReactionButton) this.lastDrawingReactionButtons.get(str);
    }

    public ReactionButton getReactionButton(VisibleReaction visibleReaction) {
        String l;
        if (visibleReaction.isStar) {
            l = "stars";
        } else {
            String str = visibleReaction.emojicon;
            l = str != null ? str : Long.toString(visibleReaction.documentId);
        }
        return getReactionButton(l);
    }

    public boolean hasOverlay() {
        return this.hasPaidReaction && !(this.isEmpty && this.outButtons.isEmpty()) && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x012e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void measure(int i, int i2) {
        int animateToWidth;
        this.height = 0;
        this.width = 0;
        this.positionOffsetY = 0;
        this.totalHeight = 0;
        if (this.isEmpty) {
            return;
        }
        this.availableWidth = i;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (true) {
            float f = 26.0f;
            if (i3 >= this.reactionButtons.size()) {
                break;
            }
            ReactionButton reactionButton = (ReactionButton) this.reactionButtons.get(i3);
            if (reactionButton.isSmall) {
                f = 14.0f;
                animateToWidth = AndroidUtilities.dp(14.0f);
            } else if (reactionButton.isTag) {
                reactionButton.width = AndroidUtilities.dp(42.0f);
                reactionButton.height = AndroidUtilities.dp(26.0f);
                if (reactionButton.hasName) {
                    reactionButton.width = (int) (reactionButton.width + reactionButton.textDrawable.getAnimateToWidth() + AndroidUtilities.dp(8.0f));
                } else {
                    CounterView.CounterDrawable counterDrawable = reactionButton.counterDrawable;
                    if (counterDrawable != null && reactionButton.count > 1) {
                        reactionButton.width += counterDrawable.getCurrentWidth() + AndroidUtilities.dp(8.0f);
                    }
                }
                if (reactionButton.width + i5 > i) {
                    i6 += reactionButton.height + AndroidUtilities.dp(4.0f);
                    i5 = 0;
                }
                reactionButton.x = i5;
                reactionButton.y = i6;
                i5 += reactionButton.width + AndroidUtilities.dp(4.0f);
                if (i5 > i4) {
                    i4 = i5;
                }
                i3++;
            } else {
                reactionButton.width = AndroidUtilities.dp(8.0f) + AndroidUtilities.dp(20.0f) + AndroidUtilities.dp(4.0f);
                if (reactionButton.avatarsDrawable == null || reactionButton.users.size() <= 0) {
                    animateToWidth = reactionButton.hasName ? (int) (reactionButton.width + reactionButton.textDrawable.getAnimateToWidth() + AndroidUtilities.dp(8.0f)) : reactionButton.width + reactionButton.counterDrawable.getCurrentWidth() + AndroidUtilities.dp(8.0f);
                } else {
                    reactionButton.users.size();
                    reactionButton.width = (int) (reactionButton.width + AndroidUtilities.dp(2.0f) + AndroidUtilities.dp(20.0f) + ((reactionButton.users.size() > 1 ? reactionButton.users.size() - 1 : 0) * AndroidUtilities.dp(20.0f) * 0.8f) + AndroidUtilities.dp(1.0f));
                    reactionButton.avatarsDrawable.height = AndroidUtilities.dp(26.0f);
                    reactionButton.height = AndroidUtilities.dp(f);
                    if (reactionButton.width + i5 > i) {
                    }
                    reactionButton.x = i5;
                    reactionButton.y = i6;
                    i5 += reactionButton.width + AndroidUtilities.dp(4.0f);
                    if (i5 > i4) {
                    }
                    i3++;
                }
            }
            reactionButton.width = animateToWidth;
            reactionButton.height = AndroidUtilities.dp(f);
            if (reactionButton.width + i5 > i) {
            }
            reactionButton.x = i5;
            reactionButton.y = i6;
            i5 += reactionButton.width + AndroidUtilities.dp(4.0f);
            if (i5 > i4) {
            }
            i3++;
        }
        if (i2 == 5 && !this.reactionButtons.isEmpty()) {
            int i7 = ((ReactionButton) this.reactionButtons.get(0)).y;
            int i8 = 0;
            for (int i9 = 0; i9 < this.reactionButtons.size(); i9++) {
                if (((ReactionButton) this.reactionButtons.get(i9)).y != i7) {
                    int i10 = i9 - 1;
                    int i11 = i - (((ReactionButton) this.reactionButtons.get(i10)).x + ((ReactionButton) this.reactionButtons.get(i10)).width);
                    while (i8 < i9) {
                        ((ReactionButton) this.reactionButtons.get(i8)).x += i11;
                        i8++;
                    }
                    i8 = i9;
                }
            }
            int size = this.reactionButtons.size() - 1;
            int i12 = i - (((ReactionButton) this.reactionButtons.get(size)).x + ((ReactionButton) this.reactionButtons.get(size)).width);
            while (i8 <= size) {
                ((ReactionButton) this.reactionButtons.get(i8)).x += i12;
                i8++;
            }
        }
        this.lastLineX = i5;
        if (i2 == 5) {
            this.width = i;
        } else {
            this.width = i4;
        }
        this.height = i6 + (this.reactionButtons.size() != 0 ? AndroidUtilities.dp(26.0f) : 0);
        this.drawServiceShaderBackground = 0.0f;
    }

    public void onAttachToWindow() {
        this.attached = true;
        for (int i = 0; i < this.reactionButtons.size(); i++) {
            ((ReactionButton) this.reactionButtons.get(i)).attach();
        }
    }

    public void onDetachFromWindow() {
        this.attached = false;
        for (int i = 0; i < this.reactionButtons.size(); i++) {
            ((ReactionButton) this.reactionButtons.get(i)).detach();
        }
        if (!this.animatedReactions.isEmpty()) {
            for (ImageReceiver imageReceiver : this.animatedReactions.values()) {
                imageReceiver.onDetachedFromWindow();
            }
        }
        this.animatedReactions.clear();
    }

    public void recordDrawingState() {
        this.lastDrawingReactionButtons.clear();
        for (int i = 0; i < this.reactionButtons.size(); i++) {
            this.lastDrawingReactionButtons.put(((ReactionButton) this.reactionButtons.get(i)).key, (ReactionButton) this.reactionButtons.get(i));
        }
        this.wasDrawn = !this.isEmpty;
        this.lastDrawnX = this.x;
        this.lastDrawnY = this.y;
        this.lastDrawnWidth = this.width;
        this.lastDrawTotalHeight = this.totalHeight;
    }

    public void resetAnimation() {
        for (int i = 0; i < this.outButtons.size(); i++) {
            ((ReactionButton) this.outButtons.get(i)).detach();
        }
        this.outButtons.clear();
        this.animateMove = false;
        this.animateWidth = false;
        this.animateHeight = false;
        for (int i2 = 0; i2 < this.reactionButtons.size(); i2++) {
            ((ReactionButton) this.reactionButtons.get(i2)).animationType = 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00fa, code lost:
        if (r3 != null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0107, code lost:
        if (r3 != null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0109, code lost:
        r1.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x017f, code lost:
        if (r1.isEmpty() == false) goto L45;
     */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0205 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0202  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessage(MessageObject messageObject, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        ReactionButton reactionButton;
        int i;
        this.resourcesProvider = resourcesProvider;
        this.isSmall = z;
        this.tags = z2;
        this.messageObject = messageObject;
        ArrayList arrayList = new ArrayList(this.reactionButtons);
        this.hasUnreadReactions = false;
        this.hasPaidReaction = false;
        this.reactionButtons.clear();
        if (messageObject != null) {
            comparator.dialogId = messageObject.getDialogId();
            TLRPC$TL_messageReactions tLRPC$TL_messageReactions = messageObject.messageOwner.reactions;
            if (tLRPC$TL_messageReactions != null && tLRPC$TL_messageReactions.results != null) {
                int i2 = 0;
                for (int i3 = 0; i3 < messageObject.messageOwner.reactions.results.size(); i3++) {
                    i2 += ((TLRPC$ReactionCount) messageObject.messageOwner.reactions.results.get(i3)).count;
                }
                int i4 = 0;
                while (i4 < messageObject.messageOwner.reactions.results.size()) {
                    TLRPC$ReactionCount tLRPC$ReactionCount = (TLRPC$ReactionCount) messageObject.messageOwner.reactions.results.get(i4);
                    int i5 = 0;
                    while (true) {
                        if (i5 >= arrayList.size()) {
                            reactionButton = null;
                            break;
                        }
                        ReactionButton reactionButton2 = (ReactionButton) arrayList.get(i5);
                        if (reactionButton2.reaction.equals(tLRPC$ReactionCount.reaction)) {
                            reactionButton = reactionButton2;
                            break;
                        }
                        i5++;
                    }
                    ReactionLayoutButton reactionLayoutButton = new ReactionLayoutButton(reactionButton, tLRPC$ReactionCount, z, z2);
                    this.reactionButtons.add(reactionLayoutButton);
                    this.hasPaidReaction = this.hasPaidReaction || reactionLayoutButton.paid;
                    if (!z && !z2 && messageObject.messageOwner.reactions.recent_reactions != null) {
                        if (messageObject.getDialogId() > 0 && !UserObject.isReplyUser(messageObject.getDialogId())) {
                            ArrayList arrayList2 = new ArrayList();
                            TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                            if (tLRPC$ReactionCount.count != 2) {
                                if (tLRPC$ReactionCount.chosen) {
                                    if (currentUser != null) {
                                        arrayList2.add(currentUser);
                                    }
                                }
                                reactionLayoutButton.setUsers(arrayList2);
                                if (!arrayList2.isEmpty()) {
                                    i = i2;
                                    reactionLayoutButton.count = 0;
                                    reactionLayoutButton.counterDrawable.setCount(0, false);
                                }
                            } else if (currentUser != null) {
                                arrayList2.add(currentUser);
                            }
                        } else if (tLRPC$ReactionCount.count <= 3 && i2 <= 3) {
                            ArrayList arrayList3 = null;
                            int i6 = 0;
                            while (i6 < messageObject.messageOwner.reactions.recent_reactions.size()) {
                                TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = (TLRPC$MessagePeerReaction) messageObject.messageOwner.reactions.recent_reactions.get(i6);
                                VisibleReaction fromTL = VisibleReaction.fromTL(tLRPC$MessagePeerReaction.reaction);
                                VisibleReaction fromTL2 = VisibleReaction.fromTL(tLRPC$ReactionCount.reaction);
                                int i7 = i2;
                                TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(MessageObject.getPeerId(tLRPC$MessagePeerReaction.peer_id));
                                if (fromTL.equals(fromTL2) && userOrChat != null) {
                                    if (arrayList3 == null) {
                                        arrayList3 = new ArrayList();
                                    }
                                    arrayList3.add(userOrChat);
                                }
                                i6++;
                                i2 = i7;
                            }
                            i = i2;
                            reactionLayoutButton.setUsers(arrayList3);
                            if (arrayList3 != null) {
                            }
                        }
                        if (!z && tLRPC$ReactionCount.count > 1 && tLRPC$ReactionCount.chosen) {
                            this.reactionButtons.add(new ReactionLayoutButton(null, tLRPC$ReactionCount, z, z2));
                            ((ReactionButton) this.reactionButtons.get(0)).isSelected = false;
                            ((ReactionButton) this.reactionButtons.get(1)).isSelected = true;
                            ((ReactionButton) this.reactionButtons.get(0)).realCount = 1;
                            ((ReactionButton) this.reactionButtons.get(1)).realCount = 1;
                            ((ReactionButton) this.reactionButtons.get(1)).key += "_";
                            break;
                        } else if (!z && i4 == 2) {
                            break;
                        } else {
                            if (!this.attached) {
                                reactionLayoutButton.attach();
                            }
                            i4++;
                            i2 = i;
                        }
                    }
                    i = i2;
                    if (!z) {
                    }
                    if (!z) {
                    }
                    if (!this.attached) {
                    }
                    i4++;
                    i2 = i;
                }
            }
            if (!z && !this.reactionButtons.isEmpty()) {
                ButtonsComparator buttonsComparator = comparator;
                buttonsComparator.currentAccount = this.currentAccount;
                Collections.sort(this.reactionButtons, buttonsComparator);
                for (int i8 = 0; i8 < this.reactionButtons.size(); i8++) {
                    TLRPC$ReactionCount tLRPC$ReactionCount2 = ((ReactionButton) this.reactionButtons.get(i8)).reactionCount;
                    int i9 = pointer;
                    pointer = i9 + 1;
                    tLRPC$ReactionCount2.lastDrawnPosition = i9;
                }
            }
            this.hasUnreadReactions = MessageObject.hasUnreadReactions(messageObject.messageOwner);
        }
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            ((ReactionButton) arrayList.get(i10)).detach();
        }
        this.isEmpty = this.reactionButtons.isEmpty();
    }

    public void setScrimProgress(float f) {
        this.scrimProgress = f;
    }

    public void setScrimReaction(Integer num) {
        this.scrimViewReaction = num;
    }

    public boolean verifyDrawable(Drawable drawable) {
        return drawable instanceof AnimatedTextView.AnimatedTextDrawable;
    }
}
