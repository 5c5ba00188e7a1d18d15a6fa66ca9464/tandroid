package org.telegram.ui.Components.Forum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedColor;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.StaticLayoutEx;
/* loaded from: classes3.dex */
public class MessageTopicButton {
    private AvatarDrawable avatarDrawable;
    private int avatarSize;
    private Context context;
    private ImageReceiver imageReceiver;
    private boolean isGeneralTopic;
    private MessageObject lastMessageObject;
    private Theme.ResourcesProvider resourcesProvider;
    private int topicArrowColor;
    private Drawable topicArrowDrawable;
    private boolean topicArrowDrawableVisible;
    private int topicBackgroundColor;
    private AnimatedColor topicBackgroundColorAnimated;
    private boolean topicClosed;
    private Drawable topicClosedDrawable;
    private float[] topicHSV;
    private int topicHeight;
    private RectF topicHitRect;
    private Drawable topicIconDrawable;
    private Rect topicIconDrawableBounds;
    private boolean topicIconWaiting;
    private int topicNameColor;
    private AnimatedColor topicNameColorAnimated;
    private StaticLayout topicNameLayout;
    private float topicNameLeft;
    private Paint topicPaint;
    private Path topicPath;
    private boolean topicPressed;
    private Drawable topicSelectorDrawable;
    private int topicWidth;
    private static final float[] lightHueRanges = {0.0f, 43.0f, 56.0f, 86.0f, 169.0f, 183.0f, 249.0f, 289.0f, 360.0f};
    private static final float[] lightSatValues = {0.6f, 1.0f, 0.95f, 0.98f, 0.8f, 0.88f, 0.51f, 0.55f, 0.6f};
    private static final float[] lightValValues = {0.79f, 0.77f, 0.6f, 0.62f, 0.6f, 0.61f, 0.8f, 0.7f, 0.79f};
    private static final float[] darkHueRanges = {0.0f, 43.0f, 56.0f, 63.0f, 86.0f, 122.0f, 147.0f, 195.0f, 205.0f, 249.0f, 270.0f, 312.0f, 388.0f, 360.0f};
    private static final float[] darkSatValues = {0.64f, 0.89f, 0.84f, 0.87f, 0.74f, 0.66f, 0.81f, 0.81f, 0.71f, 0.51f, 0.61f, 0.55f, 0.62f, 0.64f};
    private static final float[] darkValValues = {0.92f, 0.9f, 0.82f, 0.82f, 0.84f, 0.84f, 0.82f, 0.88f, 0.96f, 0.1f, 0.93f, 0.88f, 0.96f, 0.92f};
    private static final int[] idleState = new int[0];
    private static final int[] pressedState = {16842910, 16842919};

    protected void onClick() {
        throw null;
    }

    public MessageTopicButton(Context context, Theme.ResourcesProvider resourcesProvider) {
        this.context = context;
        this.resourcesProvider = resourcesProvider;
    }

    public int set(ChatMessageCell chatMessageCell, MessageObject messageObject, TLObject tLObject, int i) {
        String formatName;
        int colorId;
        if (chatMessageCell == null || messageObject == null) {
            return 0;
        }
        this.isGeneralTopic = false;
        this.topicClosed = false;
        boolean z = tLObject instanceof TLRPC$User;
        if (z) {
            formatName = UserObject.getForcedFirstName((TLRPC$User) tLObject);
        } else {
            formatName = ContactsController.formatName(tLObject);
        }
        this.topicIconDrawable = null;
        int dp = AndroidUtilities.dp(11.0f) + ((int) Theme.chat_topicTextPaint.getTextSize());
        this.avatarSize = dp;
        float dp2 = dp / AndroidUtilities.dp(56.0f);
        this.avatarDrawable = new AvatarDrawable();
        ImageReceiver imageReceiver = new ImageReceiver(chatMessageCell);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(this.avatarSize / 2);
        if (z && UserObject.isReplyUser((TLRPC$User) tLObject)) {
            this.avatarDrawable.setAvatarType(12);
            this.avatarDrawable.setScaleSize(dp2);
            this.imageReceiver.setImage(null, null, this.avatarDrawable, null, tLObject, 0);
            formatName = LocaleController.getString(R.string.RepliesTitle);
        } else if (z && UserObject.isUserSelf((TLRPC$User) tLObject)) {
            this.avatarDrawable.setAvatarType(22);
            this.avatarDrawable.setScaleSize(dp2);
            this.imageReceiver.setImage(null, null, this.avatarDrawable, null, tLObject, 0);
            formatName = LocaleController.getString(R.string.MyNotes);
        } else {
            this.avatarDrawable.setInfo(messageObject.currentAccount, tLObject);
            this.imageReceiver.setForUserOrChat(tLObject, this.avatarDrawable);
        }
        if (messageObject.isOutOwner()) {
            this.topicNameColor = getThemedColor(Theme.key_chat_outReplyNameText);
        } else {
            if (z) {
                colorId = UserObject.getColorId((TLRPC$User) tLObject);
            } else {
                colorId = tLObject instanceof TLRPC$Chat ? ChatObject.getColorId((TLRPC$Chat) tLObject) : 0;
            }
            if (colorId < 7) {
                this.topicNameColor = getThemedColor(Theme.keys_avatar_nameInMessage[colorId]);
            } else {
                MessagesController.PeerColors peerColors = MessagesController.getInstance(messageObject.currentAccount).peerColors;
                MessagesController.PeerColor color = peerColors != null ? peerColors.getColor(colorId) : null;
                if (color != null) {
                    this.topicNameColor = color.getColor(0, this.resourcesProvider);
                } else {
                    this.topicNameColor = getThemedColor(Theme.key_chat_inReplyNameText);
                }
            }
        }
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        this.topicBackgroundColor = Theme.multAlpha(this.topicNameColor, resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark() ? 0.12f : 0.1f);
        return setInternal(chatMessageCell, messageObject, i, formatName, 1);
    }

    public int set(ChatMessageCell chatMessageCell, MessageObject messageObject, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, int i) {
        int i2;
        if (chatMessageCell == null || messageObject == null) {
            return 0;
        }
        boolean z = tLRPC$TL_forumTopic.id == 1;
        this.isGeneralTopic = z;
        this.topicClosed = tLRPC$TL_forumTopic.closed;
        String str = tLRPC$TL_forumTopic.title;
        if (str == null) {
            str = "";
        }
        String str2 = str;
        if (z) {
            i2 = getThemedColor(messageObject.isOutOwner() ? Theme.key_chat_outReactionButtonText : Theme.key_chat_inReactionButtonText);
            this.topicIconDrawable = ForumUtilities.createGeneralTopicDrawable(this.context, 0.65f, i2, false);
        } else {
            long j = tLRPC$TL_forumTopic.icon_emoji_id;
            if (j != 0) {
                Drawable drawable = this.topicIconDrawable;
                if (!(drawable instanceof AnimatedEmojiDrawable) || j != ((AnimatedEmojiDrawable) drawable).getDocumentId()) {
                    Drawable drawable2 = this.topicIconDrawable;
                    if (drawable2 instanceof AnimatedEmojiDrawable) {
                        ((AnimatedEmojiDrawable) drawable2).removeView(new MessageTopicButton$$ExternalSyntheticLambda0(chatMessageCell));
                        this.topicIconDrawable = null;
                    }
                    AnimatedEmojiDrawable make = AnimatedEmojiDrawable.make(messageObject.currentAccount, 0, tLRPC$TL_forumTopic.icon_emoji_id);
                    this.topicIconDrawable = make;
                    make.addView(new MessageTopicButton$$ExternalSyntheticLambda0(chatMessageCell));
                }
                this.topicIconWaiting = false;
                Drawable drawable3 = this.topicIconDrawable;
                int dominantColor = drawable3 instanceof AnimatedEmojiDrawable ? AnimatedEmojiDrawable.getDominantColor((AnimatedEmojiDrawable) drawable3) : 0;
                if (dominantColor == 0) {
                    this.topicIconWaiting = true;
                    i2 = getThemedColor(messageObject.isOutOwner() ? Theme.key_chat_outReactionButtonText : Theme.key_chat_inReactionButtonText);
                } else {
                    i2 = dominantColor;
                }
            } else {
                i2 = tLRPC$TL_forumTopic.icon_color;
                this.topicIconDrawable = ForumUtilities.createSmallTopicDrawable(str2, i2);
            }
        }
        setupColors(i2);
        return setInternal(chatMessageCell, messageObject, i, str2, 2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:76:0x048f, code lost:
        if (r1.type == 5) goto L83;
     */
    /* JADX WARN: Removed duplicated region for block: B:91:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x04cf  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x04e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int setInternal(ChatMessageCell chatMessageCell, MessageObject messageObject, int i, String str, int i2) {
        int i3;
        int i4;
        float f;
        float f2;
        float f3;
        MessageObject messageObject2;
        int i5;
        Drawable drawable;
        int dp;
        boolean z;
        this.lastMessageObject = messageObject;
        int dp2 = AndroidUtilities.dp(7.0f) + ((int) Theme.chat_topicTextPaint.getTextSize());
        float dp3 = AndroidUtilities.dp(this.isGeneralTopic ? 6.0f : 10.0f) + dp2;
        float textSize = Theme.chat_topicTextPaint.getTextSize() - AndroidUtilities.dp(8.0f);
        float dp4 = AndroidUtilities.dp(5.0f) + Theme.chat_topicTextPaint.getTextSize();
        float f4 = dp3 + dp4;
        int i6 = (int) (i - f4);
        if (this.topicClosed) {
            i6 -= AndroidUtilities.dp(18.0f);
        }
        int i7 = i6;
        this.topicNameLayout = StaticLayoutEx.createStaticLayout(str, Theme.chat_topicTextPaint, i7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i7, i2, false);
        int dp5 = AndroidUtilities.dp(8.5f);
        int dp6 = AndroidUtilities.dp(24.0f);
        StaticLayout staticLayout = this.topicNameLayout;
        this.topicHeight = dp5 + Math.min(dp6, staticLayout == null ? 0 : staticLayout.getHeight());
        StaticLayout staticLayout2 = this.topicNameLayout;
        int lineCount = staticLayout2 == null ? 0 : staticLayout2.getLineCount();
        Path path = this.topicPath;
        if (path == null) {
            this.topicPath = new Path();
        } else {
            path.rewind();
        }
        if (this.topicPaint == null) {
            this.topicPaint = new Paint(1);
        }
        if (this.topicIconWaiting) {
            if (this.topicNameColorAnimated == null) {
                this.topicNameColorAnimated = new AnimatedColor(chatMessageCell);
            }
            if (this.topicBackgroundColorAnimated == null) {
                this.topicBackgroundColorAnimated = new AnimatedColor(chatMessageCell);
            }
        }
        if (this.topicArrowDrawable == null) {
            this.topicArrowDrawable = this.context.getResources().getDrawable(R.drawable.msg_mini_topicarrow).mutate();
        }
        Drawable drawable2 = this.topicArrowDrawable;
        int alphaComponent = ColorUtils.setAlphaComponent(this.topicNameColor, 140);
        this.topicArrowColor = alphaComponent;
        drawable2.setColorFilter(new PorterDuffColorFilter(alphaComponent, PorterDuff.Mode.MULTIPLY));
        if (this.topicClosedDrawable == null) {
            this.topicClosedDrawable = this.context.getResources().getDrawable(R.drawable.msg_mini_lock2).mutate();
        }
        Drawable drawable3 = this.topicClosedDrawable;
        int alphaComponent2 = ColorUtils.setAlphaComponent(this.topicNameColor, 140);
        this.topicArrowColor = alphaComponent2;
        drawable3.setColorFilter(new PorterDuffColorFilter(alphaComponent2, PorterDuff.Mode.MULTIPLY));
        float dp7 = AndroidUtilities.dp(11.0f) + ((int) Theme.chat_topicTextPaint.getTextSize());
        int max = Math.max(1, ((int) Theme.chat_topicTextPaint.getTextSize()) + AndroidUtilities.dp(0.0f));
        int max2 = Math.max(1, ((int) Theme.chat_topicTextPaint.getTextSize()) - AndroidUtilities.dp(4.0f));
        if (lineCount == 2) {
            this.topicHeight = AndroidUtilities.dp(15.0f) + (((int) Theme.chat_topicTextPaint.getTextSize()) * 2);
            float abs = Math.abs(this.topicNameLayout.getLineRight(0) - this.topicNameLayout.getLineLeft(0));
            float abs2 = Math.abs(this.topicNameLayout.getLineRight(1) - this.topicNameLayout.getLineLeft(1));
            if (this.topicClosed) {
                abs2 += AndroidUtilities.dp(4.0f) + max2;
            }
            i3 = dp2;
            float min = Math.min(this.topicNameLayout.getLineLeft(0), this.topicNameLayout.getLineLeft(1));
            this.topicNameLeft = min;
            boolean z2 = min != 0.0f;
            float max3 = Math.max(abs, abs2);
            float dp8 = (AndroidUtilities.dp(11.0f) + ((int) Theme.chat_topicTextPaint.getTextSize())) / 1.5f;
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, dp7, dp7);
            int i8 = lineCount;
            this.topicPath.arcTo(rectF, 180.0f, 90.0f);
            float f5 = dp4 - textSize;
            if (Math.abs(abs - abs2) <= f5 || z2) {
                abs = Math.max(abs, abs2 + f5);
                abs2 = Math.max(abs2, abs - f5);
                z = true;
            } else {
                z = false;
            }
            float f6 = dp3 + textSize + abs;
            rectF.set(f6 - dp8, 0.0f, f6, dp8);
            this.topicPath.arcTo(rectF, 270.0f, 90.0f);
            float dp9 = AndroidUtilities.dp(11.0f) + Theme.chat_topicTextPaint.getTextSize();
            float min2 = Math.min(dp8, Math.abs((abs - AndroidUtilities.dp(13.0f)) - abs2));
            if (!z) {
                if (abs - f5 > abs2) {
                    rectF.set(f6 - min2, dp9 - min2, f6, dp9);
                    this.topicPath.arcTo(rectF, 0.0f, 90.0f);
                    float f7 = f4 + abs2;
                    rectF.set(f7, dp9, f7 + min2, min2 + dp9);
                    this.topicPath.arcTo(rectF, 270.0f, -90.0f);
                } else {
                    float f8 = this.topicHeight - dp9;
                    rectF.set(f6, f8 - min2, f6 + min2, f8);
                    this.topicPath.arcTo(rectF, 180.0f, -90.0f);
                    float f9 = f4 + abs2;
                    rectF.set(f9 - min2, f8, f9, min2 + f8);
                    this.topicPath.arcTo(rectF, 270.0f, 90.0f);
                }
            }
            this.topicArrowDrawableVisible = !z2;
            float f10 = max;
            float f11 = max / 2;
            this.topicArrowDrawable.setBounds((int) (((AndroidUtilities.dp(-4.0f) + f4) + abs2) - f10), (int) (((((this.topicHeight - AndroidUtilities.dp(11.0f)) - Theme.chat_topicTextPaint.getTextSize()) + this.topicHeight) / 2.0f) - f11), (int) (AndroidUtilities.dp(-4.0f) + f4 + abs2), (int) (((((this.topicHeight - AndroidUtilities.dp(11.0f)) - Theme.chat_topicTextPaint.getTextSize()) + this.topicHeight) / 2.0f) + f11));
            float f12 = max2 / 2;
            this.topicClosedDrawable.setBounds((int) ((((AndroidUtilities.dp(-4.0f) + f4) - f10) + abs2) - max2), (int) (((((this.topicHeight - AndroidUtilities.dp(11.0f)) - Theme.chat_topicTextPaint.getTextSize()) + this.topicHeight) / 2.0f) - f12), (int) (((AndroidUtilities.dp(-4.0f) + f4) - f10) + abs2), (int) (((((this.topicHeight - AndroidUtilities.dp(11.0f)) - Theme.chat_topicTextPaint.getTextSize()) + this.topicHeight) / 2.0f) + f12));
            float f13 = abs2 + f4;
            int i9 = this.topicHeight;
            rectF.set(f13 - dp8, i9 - dp8, f13, i9);
            this.topicPath.arcTo(rectF, 0.0f, 90.0f);
            int i10 = this.topicHeight;
            rectF.set(0.0f, i10 - dp7, dp7, i10);
            this.topicPath.arcTo(rectF, 90.0f, 90.0f);
            this.topicPath.close();
            f = max3;
            i4 = i8;
        } else {
            i3 = dp2;
            i4 = lineCount;
            if (i4 == 1) {
                this.topicHeight = AndroidUtilities.dp(11.0f) + ((int) Theme.chat_topicTextPaint.getTextSize());
                float abs3 = Math.abs(this.topicNameLayout.getLineRight(0) - this.topicNameLayout.getLineLeft(0));
                if (this.topicClosed) {
                    abs3 += AndroidUtilities.dp(4.0f) + max2;
                }
                this.topicNameLeft = this.topicNameLayout.getLineLeft(0);
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(0.0f, 0.0f, f4 + abs3, this.topicHeight);
                this.topicArrowDrawableVisible = true;
                float f14 = max;
                float f15 = max / 2;
                this.topicArrowDrawable.setBounds((int) (((AndroidUtilities.dp(-4.0f) + f4) + abs3) - f14), (int) ((this.topicHeight / 2.0f) - f15), (int) (AndroidUtilities.dp(-4.0f) + f4 + abs3), (int) ((this.topicHeight / 2.0f) + f15));
                float f16 = max2 / 2;
                this.topicClosedDrawable.setBounds((int) ((((AndroidUtilities.dp(-4.0f) + f4) + abs3) - f14) - max2), (int) ((this.topicHeight / 2.0f) - f16), (int) (((AndroidUtilities.dp(-4.0f) + f4) + abs3) - f14), (int) ((this.topicHeight / 2.0f) + f16));
                this.topicPath.addRoundRect(rectF2, dp7, dp7, Path.Direction.CW);
                f = abs3;
            } else if (i4 == 0) {
                this.topicHeight = AndroidUtilities.dp(11.0f) + ((int) Theme.chat_topicTextPaint.getTextSize());
                if (this.topicClosed) {
                    f2 = 0.0f;
                    f3 = AndroidUtilities.dp(4.0f) + max2 + 0.0f;
                } else {
                    f2 = 0.0f;
                    f3 = 0.0f;
                }
                this.topicNameLeft = f2;
                this.topicArrowDrawableVisible = true;
                float f17 = max2;
                float f18 = max / 2;
                this.topicArrowDrawable.setBounds((int) (((AndroidUtilities.dp(-4.0f) + f4) + f3) - f17), (int) ((this.topicHeight / 2.0f) - f18), (int) (AndroidUtilities.dp(-4.0f) + f4 + f3), (int) ((this.topicHeight / 2.0f) + f18));
                float f19 = max;
                float f20 = max2 / 2;
                this.topicClosedDrawable.setBounds((int) ((((AndroidUtilities.dp(-4.0f) + f4) + f3) - f19) - f17), (int) ((this.topicHeight / 2.0f) - f20), (int) (((AndroidUtilities.dp(-4.0f) + f4) + f3) - f19), (int) ((this.topicHeight / 2.0f) + f20));
                RectF rectF3 = AndroidUtilities.rectTmp;
                rectF3.set(0.0f, 0.0f, f4 + f3, this.topicHeight);
                this.topicPath.addRoundRect(rectF3, dp7, dp7, Path.Direction.CW);
                f = f3;
            } else {
                f = 0.0f;
            }
        }
        this.topicWidth = (int) ((f4 - AndroidUtilities.dp(1.0f)) + f);
        if (messageObject.isAnyKindOfSticker()) {
            messageObject2 = messageObject;
        } else {
            messageObject2 = messageObject;
        }
        if (messageObject2.type != 19) {
            i5 = 0;
            drawable = this.topicSelectorDrawable;
            if (drawable == null) {
                Drawable createSelectorDrawable = Theme.createSelectorDrawable(this.topicBackgroundColor, 2);
                this.topicSelectorDrawable = createSelectorDrawable;
                createSelectorDrawable.setCallback(chatMessageCell);
            } else {
                Theme.setSelectorDrawableColor(drawable, this.topicBackgroundColor, true);
            }
            this.topicPaint.setColor(this.topicBackgroundColor);
            if (this.topicIconDrawable != null) {
                if (this.topicIconDrawableBounds == null) {
                    this.topicIconDrawableBounds = new Rect();
                }
                this.topicIconDrawableBounds.set(AndroidUtilities.dp(5.0f), AndroidUtilities.dp((i4 == 2 ? 3 : 0) + 2), AndroidUtilities.dp(5.0f) + i3, AndroidUtilities.dp((i4 == 2 ? 3 : 0) + 2) + i3);
                this.topicIconDrawable.setBounds(this.topicIconDrawableBounds);
            }
            return i5;
        }
        int dp10 = AndroidUtilities.dp(6.0f) + this.topicHeight + 0;
        int i11 = messageObject2.type;
        if (i11 == 19) {
            dp = AndroidUtilities.dp(16.0f);
        } else if (i11 != 0) {
            dp = AndroidUtilities.dp(9.0f);
        } else {
            i5 = dp10;
            drawable = this.topicSelectorDrawable;
            if (drawable == null) {
            }
            this.topicPaint.setColor(this.topicBackgroundColor);
            if (this.topicIconDrawable != null) {
            }
            return i5;
        }
        i5 = dp + dp10;
        drawable = this.topicSelectorDrawable;
        if (drawable == null) {
        }
        this.topicPaint.setColor(this.topicBackgroundColor);
        if (this.topicIconDrawable != null) {
        }
        return i5;
    }

    public void onAttached(ChatMessageCell chatMessageCell) {
        Drawable drawable = this.topicIconDrawable;
        if (!(drawable instanceof AnimatedEmojiDrawable) || chatMessageCell == null) {
            return;
        }
        ((AnimatedEmojiDrawable) drawable).addView(new MessageTopicButton$$ExternalSyntheticLambda0(chatMessageCell));
    }

    public void onDetached(ChatMessageCell chatMessageCell) {
        Drawable drawable = this.topicIconDrawable;
        if (!(drawable instanceof AnimatedEmojiDrawable) || chatMessageCell == null) {
            return;
        }
        ((AnimatedEmojiDrawable) drawable).removeView(new MessageTopicButton$$ExternalSyntheticLambda0(chatMessageCell));
    }

    private void setupColors(int i) {
        MessageObject messageObject = this.lastMessageObject;
        if (messageObject != null && messageObject.shouldDrawWithoutBackground()) {
            this.topicNameColor = getThemedColor(Theme.key_chat_stickerReplyNameText);
            return;
        }
        MessageObject messageObject2 = this.lastMessageObject;
        if (messageObject2 != null && messageObject2.isOutOwner()) {
            this.topicNameColor = getThemedColor(Theme.key_chat_outReactionButtonText);
            this.topicBackgroundColor = ColorUtils.setAlphaComponent(getThemedColor(Theme.key_chat_outReactionButtonBackground), 38);
            return;
        }
        if (this.topicHSV == null) {
            this.topicHSV = new float[3];
        }
        Color.colorToHSV(i, this.topicHSV);
        float[] fArr = this.topicHSV;
        float f = fArr[0];
        if (fArr[1] <= 0.02f) {
            this.topicNameColor = getThemedColor(Theme.key_chat_inReactionButtonText);
            this.topicBackgroundColor = ColorUtils.setAlphaComponent(getThemedColor(Theme.key_chat_inReactionButtonBackground), 38);
            return;
        }
        Color.colorToHSV(getThemedColor(Theme.key_chat_inReactionButtonText), this.topicHSV);
        this.topicHSV[0] = f;
        float[] fArr2 = Theme.isCurrentThemeDark() ? darkHueRanges : lightHueRanges;
        float[] fArr3 = Theme.isCurrentThemeDark() ? darkSatValues : lightSatValues;
        float[] fArr4 = Theme.isCurrentThemeDark() ? darkValValues : lightValValues;
        int i2 = 1;
        while (true) {
            if (i2 >= fArr2.length) {
                break;
            } else if (f <= fArr2[i2]) {
                int i3 = i2 - 1;
                float f2 = (f - fArr2[i3]) / (fArr2[i2] - fArr2[i3]);
                this.topicHSV[1] = AndroidUtilities.lerp(fArr3[i3], fArr3[i2], f2);
                this.topicHSV[2] = AndroidUtilities.lerp(fArr4[i3], fArr4[i2], f2);
                break;
            } else {
                i2++;
            }
        }
        this.topicNameColor = Color.HSVToColor(Color.alpha(getThemedColor(Theme.key_chat_inReactionButtonText)), this.topicHSV);
        this.topicBackgroundColor = Color.HSVToColor(38, this.topicHSV);
    }

    public boolean checkTouchEvent(MotionEvent motionEvent) {
        Drawable drawable;
        RectF rectF = this.topicHitRect;
        if (rectF == null) {
            this.topicPressed = false;
            return false;
        }
        boolean contains = rectF.contains(motionEvent.getX(), motionEvent.getY());
        if (motionEvent.getAction() == 0) {
            if (contains) {
                Drawable drawable2 = this.topicSelectorDrawable;
                if (drawable2 != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        drawable2.setHotspot(motionEvent.getX() - this.topicHitRect.left, motionEvent.getY() - this.topicHitRect.top);
                    }
                    this.topicSelectorDrawable.setState(pressedState);
                }
                this.topicPressed = true;
            } else {
                this.topicPressed = false;
            }
            return this.topicPressed;
        } else if (motionEvent.getAction() == 2) {
            boolean z = this.topicPressed;
            if (z != contains) {
                if (z && (drawable = this.topicSelectorDrawable) != null) {
                    drawable.setState(idleState);
                }
                this.topicPressed = contains;
            }
            return this.topicPressed;
        } else {
            if ((motionEvent.getAction() == 1 || motionEvent.getAction() == 3) && this.topicPressed) {
                this.topicPressed = false;
                Drawable drawable3 = this.topicSelectorDrawable;
                if (drawable3 != null) {
                    drawable3.setState(idleState);
                }
                if (motionEvent.getAction() == 1) {
                    onClick();
                    return true;
                }
            }
            return false;
        }
    }

    public int width() {
        return this.topicWidth;
    }

    public int height() {
        return this.topicHeight;
    }

    public void draw(Canvas canvas, float f, float f2, float f3) {
        Paint paint;
        int i;
        int dominantColor;
        if (this.topicIconWaiting) {
            Drawable drawable = this.topicIconDrawable;
            if ((drawable instanceof AnimatedEmojiDrawable) && (dominantColor = AnimatedEmojiDrawable.getDominantColor((AnimatedEmojiDrawable) drawable)) != 0) {
                this.topicIconWaiting = false;
                setupColors(dominantColor);
            }
        }
        canvas.save();
        MessageObject messageObject = this.lastMessageObject;
        if (messageObject != null && messageObject.shouldDrawWithoutBackground()) {
            this.topicPath.offset(f, f2);
            int i2 = -1;
            if (f3 < 1.0f) {
                i = getThemedPaint("paintChatActionBackground").getAlpha();
                getThemedPaint("paintChatActionBackground").setAlpha((int) (i * f3));
            } else {
                i = -1;
            }
            canvas.drawPath(this.topicPath, getThemedPaint("paintChatActionBackground"));
            if (hasGradientService()) {
                if (f3 < 1.0f) {
                    i2 = Theme.chat_actionBackgroundGradientDarkenPaint.getAlpha();
                    Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha((int) (i2 * f3));
                }
                canvas.drawPath(this.topicPath, Theme.chat_actionBackgroundGradientDarkenPaint);
            }
            if (i >= 0) {
                getThemedPaint("paintChatActionBackground").setAlpha(i);
            }
            if (i2 >= 0) {
                Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha(i2);
            }
            this.topicPath.offset(-f, -f2);
            canvas.translate(f, f2);
        } else {
            canvas.translate(f, f2);
            if (this.topicPath != null && (paint = this.topicPaint) != null) {
                AnimatedColor animatedColor = this.topicBackgroundColorAnimated;
                if (animatedColor != null) {
                    paint.setColor(animatedColor.set(this.topicBackgroundColor));
                } else {
                    paint.setColor(this.topicBackgroundColor);
                }
                int alpha = this.topicPaint.getAlpha();
                this.topicPaint.setAlpha((int) (alpha * f3));
                canvas.drawPath(this.topicPath, this.topicPaint);
                this.topicPaint.setAlpha(alpha);
            }
        }
        if (this.topicHitRect == null) {
            this.topicHitRect = new RectF();
        }
        this.topicHitRect.set(f, f2, this.topicWidth + f, this.topicHeight + f2);
        if (this.topicSelectorDrawable != null) {
            canvas.save();
            canvas.clipPath(this.topicPath);
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set(0, 0, this.topicWidth, this.topicHeight);
            this.topicSelectorDrawable.setBounds(rect);
            this.topicSelectorDrawable.draw(canvas);
            canvas.restore();
        }
        int i3 = this.topicNameColor;
        if (this.topicNameLayout != null) {
            canvas.save();
            canvas.translate((AndroidUtilities.dp(this.isGeneralTopic ? 13.0f : 17.0f) + Theme.chat_topicTextPaint.getTextSize()) - this.topicNameLeft, AndroidUtilities.dp(4.5f));
            AnimatedColor animatedColor2 = this.topicNameColorAnimated;
            if (animatedColor2 != null) {
                TextPaint textPaint = Theme.chat_topicTextPaint;
                i3 = animatedColor2.set(this.topicNameColor);
                textPaint.setColor(i3);
            } else {
                TextPaint textPaint2 = Theme.chat_topicTextPaint;
                int i4 = this.topicNameColor;
                textPaint2.setColor(i4);
                i3 = i4;
            }
            TextPaint textPaint3 = Theme.chat_topicTextPaint;
            textPaint3.setAlpha((int) (textPaint3.getAlpha() * f3 * (this.topicClosed ? 0.7f : 1.0f)));
            this.topicNameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.topicClosedDrawable != null && this.topicClosed) {
            int alphaComponent = ColorUtils.setAlphaComponent(i3, 140);
            if (this.topicArrowColor != alphaComponent) {
                Drawable drawable2 = this.topicClosedDrawable;
                this.topicArrowColor = alphaComponent;
                drawable2.setColorFilter(new PorterDuffColorFilter(alphaComponent, PorterDuff.Mode.MULTIPLY));
            }
            this.topicClosedDrawable.draw(canvas);
        }
        if (this.topicArrowDrawable != null && this.topicArrowDrawableVisible) {
            int alphaComponent2 = ColorUtils.setAlphaComponent(i3, 140);
            if (this.topicArrowColor != alphaComponent2) {
                Drawable drawable3 = this.topicArrowDrawable;
                this.topicArrowColor = alphaComponent2;
                drawable3.setColorFilter(new PorterDuffColorFilter(alphaComponent2, PorterDuff.Mode.MULTIPLY));
            }
            this.topicArrowDrawable.draw(canvas);
        }
        canvas.restore();
    }

    public void drawOutbounds(Canvas canvas, float f) {
        if (this.topicHitRect != null) {
            canvas.save();
            RectF rectF = this.topicHitRect;
            canvas.translate(rectF.left, rectF.top);
            Drawable drawable = this.topicIconDrawable;
            if (drawable != null) {
                drawable.setAlpha((int) (f * 255.0f));
                this.topicIconDrawable.setBounds(this.topicIconDrawableBounds);
                this.topicIconDrawable.draw(canvas);
            } else {
                ImageReceiver imageReceiver = this.imageReceiver;
                if (imageReceiver != null) {
                    int i = this.avatarSize;
                    imageReceiver.setImageCoords(0.0f, 0.0f, i, i);
                    this.imageReceiver.draw(canvas);
                }
            }
            canvas.restore();
        }
    }

    public void resetClick() {
        Drawable drawable = this.topicSelectorDrawable;
        if (drawable != null) {
            drawable.setState(idleState);
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    private boolean hasGradientService() {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        return resourcesProvider != null ? resourcesProvider.hasGradientService() : Theme.hasGradientService();
    }
}
