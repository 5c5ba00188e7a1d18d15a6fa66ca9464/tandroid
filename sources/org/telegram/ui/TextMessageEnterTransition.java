package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ChatListItemAnimator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ReplyMessageLine;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.MessageEnterTransitionContainer;
/* loaded from: classes3.dex */
public class TextMessageEnterTransition implements MessageEnterTransitionContainer.Transition {
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private ValueAnimator animator;
    boolean changeColor;
    private ChatActivity chatActivity;
    MessageEnterTransitionContainer container;
    boolean crossfade;
    Bitmap crossfadeTextBitmap;
    float crossfadeTextOffset;
    MessageObject currentMessageObject;
    boolean drawBitmaps;
    private float drawableFromBottom;
    float drawableFromTop;
    ChatActivityEnterView enterView;
    int fromColor;
    Drawable fromMessageDrawable;
    private float fromStartX;
    private float fromStartY;
    private Matrix gradientMatrix;
    private Paint gradientPaint;
    private LinearGradient gradientShader;
    boolean hasReply;
    StaticLayout layout;
    RecyclerListView listView;
    private int messageId;
    ChatMessageCell messageView;
    float progress;
    int replayFromColor;
    int replayObjectFromColor;
    float replyFromStartWidth;
    float replyFromStartX;
    float replyFromStartY;
    float replyNameDx;
    private final Theme.ResourcesProvider resourcesProvider;
    private float[] roundRectRadii;
    StaticLayout rtlLayout;
    private float scaleFrom;
    private float scaleY;
    Bitmap textLayoutBitmap;
    Bitmap textLayoutBitmapRtl;
    MessageObject.TextLayoutBlock textLayoutBlock;
    float textX;
    float textY;
    int toColor;
    float toXOffset;
    float toXOffsetRtl;
    Paint bitmapPaint = new Paint(1);
    boolean initBitmaps = false;
    private AnimationNotificationsLocker notificationsLocker = new AnimationNotificationsLocker();
    private final RectF replySelectorRect = new RectF();
    private final RectF messageReplySelectorRect = new RectF();

    @SuppressLint({"WrongConstant"})
    public TextMessageEnterTransition(final ChatMessageCell chatMessageCell, final ChatActivity chatActivity, RecyclerListView recyclerListView, final MessageEnterTransitionContainer messageEnterTransitionContainer, Theme.ResourcesProvider resourcesProvider) {
        int i;
        int i2;
        int i3;
        Theme.MessageDrawable currentBackgroundDrawable;
        Object[] spans;
        this.drawBitmaps = false;
        this.resourcesProvider = resourcesProvider;
        int i4 = UserConfig.selectedAccount;
        if (chatMessageCell.getMessageObject().textLayoutBlocks == null || chatMessageCell.getMessageObject().textLayoutBlocks.size() > 1 || chatMessageCell.getMessageObject().textLayoutBlocks.isEmpty() || chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getLineCount() > 10) {
            return;
        }
        this.messageView = chatMessageCell;
        this.listView = recyclerListView;
        this.container = messageEnterTransitionContainer;
        this.chatActivity = chatActivity;
        this.enterView = chatActivity.getChatActivityEnterView();
        final ChatActivityEnterView chatActivityEnterView = chatActivity.getChatActivityEnterView();
        if (chatActivityEnterView == null || chatActivityEnterView.getEditField() == null || chatActivityEnterView.getEditField().getLayout() == null) {
            return;
        }
        chatActivityEnterView.getRecordCircle();
        this.bitmapPaint.setFilterBitmap(true);
        this.currentMessageObject = chatMessageCell.getMessageObject();
        if (!chatMessageCell.getTransitionParams().wasDraw) {
            chatMessageCell.draw(new Canvas());
        }
        chatMessageCell.setEnterTransitionInProgress(true);
        Editable editText = chatActivityEnterView.getEditText();
        CharSequence charSequence = chatMessageCell.getMessageObject().messageText;
        this.crossfade = false;
        int height = chatActivityEnterView.getEditField().getLayout().getHeight();
        TextPaint textPaint = Theme.chat_msgTextPaint;
        int dp = AndroidUtilities.dp(20.0f);
        if (chatMessageCell.getMessageObject().getEmojiOnlyCount() != 0) {
            boolean z = chatMessageCell.getMessageObject().emojiOnlyCount == chatMessageCell.getMessageObject().animatedEmojiCount;
            switch (Math.max(chatMessageCell.getMessageObject().emojiOnlyCount, chatMessageCell.getMessageObject().animatedEmojiCount)) {
                case 0:
                case 1:
                case 2:
                    if (z) {
                        textPaint = Theme.chat_msgTextPaintEmoji[0];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[2];
                        break;
                    }
                case 3:
                    if (z) {
                        textPaint = Theme.chat_msgTextPaintEmoji[1];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[3];
                        break;
                    }
                case 4:
                    if (z) {
                        textPaint = Theme.chat_msgTextPaintEmoji[2];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[4];
                        break;
                    }
                case 5:
                    if (z) {
                        textPaint = Theme.chat_msgTextPaintEmoji[3];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[5];
                        break;
                    }
                case 6:
                    if (z) {
                        textPaint = Theme.chat_msgTextPaintEmoji[4];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[5];
                        break;
                    }
                default:
                    textPaint = Theme.chat_msgTextPaintEmoji[5];
                    break;
            }
            if (textPaint != null) {
                dp = (int) (textPaint.getTextSize() + AndroidUtilities.dp(4.0f));
            }
        }
        boolean z2 = (charSequence instanceof Spannable) && (spans = ((Spannable) charSequence).getSpans(0, charSequence.length(), Object.class)) != null && spans.length > 0;
        if (editText.length() != charSequence.length() || z2) {
            this.crossfade = true;
            int[] iArr = new int[1];
            CharSequence trim = AndroidUtilities.trim(editText, iArr);
            if (iArr[0] > 0) {
                i = chatActivityEnterView.getEditField().getLayout().getLineTop(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0]));
                height = chatActivityEnterView.getEditField().getLayout().getLineBottom(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0] + trim.length())) - i;
            } else {
                i = 0;
            }
            AnimatedEmojiSpan.cloneSpans(charSequence);
            charSequence = Emoji.replaceEmoji(editText, textPaint.getFontMetricsInt(), dp, false);
        } else {
            i = 0;
        }
        this.scaleFrom = chatActivityEnterView.getEditField().getTextSize() / textPaint.getTextSize();
        int lineCount = chatActivityEnterView.getEditField().getLayout().getLineCount();
        int width = (int) (chatActivityEnterView.getEditField().getLayout().getWidth() / this.scaleFrom);
        if (Build.VERSION.SDK_INT >= 24) {
            this.layout = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        } else {
            this.layout = new StaticLayout(charSequence, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(2, (View) null, this.animatedEmojiStack, this.layout);
        float y = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
        this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
        this.fromStartY = ((AndroidUtilities.dp(10.0f) + y) - chatActivityEnterView.getEditField().getScrollY()) + i;
        this.toXOffset = 0.0f;
        float f = Float.MAX_VALUE;
        for (int i5 = 0; i5 < this.layout.getLineCount(); i5++) {
            float lineLeft = this.layout.getLineLeft(i5);
            if (lineLeft < f) {
                f = lineLeft;
            }
        }
        if (f != Float.MAX_VALUE) {
            this.toXOffset = f;
        }
        this.scaleY = height / (this.layout.getHeight() * this.scaleFrom);
        this.drawableFromTop = AndroidUtilities.dp(4.0f) + y;
        if (this.enterView.isTopViewVisible()) {
            this.drawableFromTop -= AndroidUtilities.dp(12.0f);
        }
        this.drawableFromBottom = y + chatActivityEnterView.getEditField().getMeasuredHeight();
        MessageObject.TextLayoutBlock textLayoutBlock = chatMessageCell.getMessageObject().textLayoutBlocks.get(0);
        this.textLayoutBlock = textLayoutBlock;
        StaticLayout staticLayout = textLayoutBlock.textLayout;
        int i6 = Theme.key_chat_messageTextOut;
        double calculateLuminance = ColorUtils.calculateLuminance(getThemedColor(i6));
        int i7 = Theme.key_chat_messagePanelText;
        if (Math.abs(calculateLuminance - ColorUtils.calculateLuminance(getThemedColor(i7))) > 0.20000000298023224d) {
            this.crossfade = true;
            this.changeColor = true;
        }
        this.fromColor = getThemedColor(i7);
        this.toColor = getThemedColor(i6);
        if (staticLayout.getLineCount() == this.layout.getLineCount()) {
            lineCount = staticLayout.getLineCount();
            int i8 = 0;
            i2 = 0;
            i3 = 0;
            while (true) {
                if (i8 < lineCount) {
                    if (isRtlLine(this.layout, i8)) {
                        i3++;
                    } else {
                        i2++;
                    }
                    if (staticLayout.getLineEnd(i8) != this.layout.getLineEnd(i8)) {
                        this.crossfade = true;
                    } else {
                        i8++;
                    }
                }
            }
        } else {
            this.crossfade = true;
            i2 = 0;
            i3 = 0;
        }
        if (!this.crossfade && i3 > 0 && i2 > 0) {
            SpannableString spannableString = new SpannableString(charSequence);
            SpannableString spannableString2 = new SpannableString(charSequence);
            float f2 = Float.MAX_VALUE;
            for (int i9 = 0; i9 < lineCount; i9++) {
                if (isRtlLine(this.layout, i9)) {
                    spannableString.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i9), this.layout.getLineEnd(i9), 0);
                    float lineLeft2 = this.layout.getLineLeft(i9);
                    f2 = lineLeft2 < f2 ? lineLeft2 : f2;
                } else {
                    spannableString2.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i9), this.layout.getLineEnd(i9), 0);
                }
            }
            if (Build.VERSION.SDK_INT >= 24) {
                this.layout = StaticLayout.Builder.obtain(spannableString, 0, spannableString.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                this.rtlLayout = StaticLayout.Builder.obtain(spannableString2, 0, spannableString2.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
            } else {
                TextPaint textPaint2 = textPaint;
                this.layout = new StaticLayout(spannableString, textPaint2, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.rtlLayout = new StaticLayout(spannableString2, textPaint2, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }
        this.toXOffsetRtl = this.layout.getWidth() - chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getWidth();
        try {
            if (this.drawBitmaps) {
                this.textLayoutBitmap = Bitmap.createBitmap(this.layout.getWidth(), this.layout.getHeight(), Bitmap.Config.ARGB_8888);
                this.layout.draw(new Canvas(this.textLayoutBitmap));
                StaticLayout staticLayout2 = this.rtlLayout;
                if (staticLayout2 != null) {
                    this.textLayoutBitmapRtl = Bitmap.createBitmap(staticLayout2.getWidth(), this.rtlLayout.getHeight(), Bitmap.Config.ARGB_8888);
                    this.rtlLayout.draw(new Canvas(this.textLayoutBitmapRtl));
                }
                if (this.crossfade) {
                    if (chatMessageCell.getMeasuredHeight() < recyclerListView.getMeasuredHeight()) {
                        this.crossfadeTextOffset = 0.0f;
                        this.crossfadeTextBitmap = Bitmap.createBitmap(chatMessageCell.getMeasuredWidth(), chatMessageCell.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    } else {
                        this.crossfadeTextOffset = chatMessageCell.getTop();
                        this.crossfadeTextBitmap = Bitmap.createBitmap(chatMessageCell.getMeasuredWidth(), recyclerListView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    }
                }
            }
        } catch (Exception unused) {
            this.drawBitmaps = false;
        }
        boolean z3 = (chatMessageCell.getMessageObject().getReplyMsgId() == 0 || chatMessageCell.replyNameLayout == null) ? false : true;
        this.hasReply = z3;
        if (z3) {
            SimpleTextView replyNameTextView = chatActivity.getReplyNameTextView();
            this.replyFromStartX = replyNameTextView.getX() + ((View) replyNameTextView.getParent()).getX();
            this.replyFromStartWidth = ((View) replyNameTextView.getParent()).getWidth();
            this.replyFromStartY = replyNameTextView.getY() + ((View) replyNameTextView.getParent().getParent()).getY() + ((View) replyNameTextView.getParent().getParent().getParent()).getY();
            SimpleTextView replyObjectTextView = chatActivity.getReplyObjectTextView();
            replyObjectTextView.getY();
            ((View) replyObjectTextView.getParent().getParent()).getY();
            ((View) replyObjectTextView.getParent().getParent().getParent()).getY();
            this.replayFromColor = chatActivity.getReplyNameTextView().getTextColor();
            this.replayObjectFromColor = chatActivity.getReplyObjectTextView().getTextColor();
            this.drawableFromTop -= AndroidUtilities.dp(46.0f);
        }
        this.gradientMatrix = new Matrix();
        Paint paint = new Paint(1);
        this.gradientPaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        LinearGradient linearGradient = new LinearGradient(0.0f, AndroidUtilities.dp(12.0f), 0.0f, 0.0f, 0, -16777216, Shader.TileMode.CLAMP);
        this.gradientShader = linearGradient;
        this.gradientPaint.setShader(linearGradient);
        this.messageId = chatMessageCell.getMessageObject().stableId;
        chatActivityEnterView.getEditField().setAlpha(0.0f);
        chatActivityEnterView.setTextTransitionIsRunning(true);
        StaticLayout staticLayout3 = chatMessageCell.replyNameLayout;
        if (staticLayout3 != null && staticLayout3.getText().length() > 1 && chatMessageCell.replyNameLayout.getPrimaryHorizontal(0) != 0.0f) {
            this.replyNameDx = chatMessageCell.replyNameLayout.getWidth() - chatMessageCell.replyNameLayout.getLineWidth(0);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.TextMessageEnterTransition$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextMessageEnterTransition.this.lambda$new$0(chatActivityEnterView, messageEnterTransitionContainer, valueAnimator);
            }
        });
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.setDuration(250L);
        messageEnterTransitionContainer.addTransition(this);
        this.notificationsLocker.lock();
        this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.TextMessageEnterTransition.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TextMessageEnterTransition.this.notificationsLocker.unlock();
                messageEnterTransitionContainer.removeTransition(TextMessageEnterTransition.this);
                chatMessageCell.setEnterTransitionInProgress(false);
                chatMessageCell.getTransitionParams().lastDrawingBackgroundRect.set(chatMessageCell.getBackgroundDrawableLeft(), chatMessageCell.getBackgroundDrawableTop(), chatMessageCell.getBackgroundDrawableRight(), chatMessageCell.getBackgroundDrawableBottom());
                chatActivityEnterView.setTextTransitionIsRunning(false);
                chatActivityEnterView.getEditField().setAlpha(1.0f);
                chatActivity.getReplyNameTextView().setAlpha(1.0f);
                chatActivity.getReplyObjectTextView().setAlpha(1.0f);
                AnimatedEmojiSpan.release((View) null, TextMessageEnterTransition.this.animatedEmojiStack);
            }
        });
        if (SharedConfig.getDevicePerformanceClass() != 2 || (currentBackgroundDrawable = chatMessageCell.getCurrentBackgroundDrawable(true)) == null) {
            return;
        }
        this.fromMessageDrawable = currentBackgroundDrawable.getTransitionDrawable(getThemedColor(Theme.key_chat_messagePanelBackground));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ChatActivityEnterView chatActivityEnterView, MessageEnterTransitionContainer messageEnterTransitionContainer, ValueAnimator valueAnimator) {
        this.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatActivityEnterView.getEditField().setAlpha(this.progress);
        messageEnterTransitionContainer.invalidate();
    }

    public void start() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    private boolean isRtlLine(Layout layout, int i) {
        return layout.getLineRight(i) == ((float) layout.getWidth()) && layout.getLineLeft(i) != 0.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0555  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x055d  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x05b5  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x05cd  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x070c  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x03f5  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0499  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x04f1  */
    @Override // org.telegram.ui.MessageEnterTransitionContainer.Transition
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int i;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        boolean z;
        int i2;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        int i3;
        int themedColor;
        ChatMessageCell chatMessageCell;
        float f16;
        float f17;
        MessageObject messageObject;
        float f18;
        float f19;
        float f20;
        ChatMessageCell chatMessageCell2;
        int color;
        ChatMessageCell chatMessageCell3;
        Drawable drawable;
        if (this.drawBitmaps && !this.initBitmaps && this.crossfadeTextBitmap != null && this.messageView.getTransitionParams().wasDraw) {
            this.initBitmaps = true;
            Canvas canvas2 = new Canvas(this.crossfadeTextBitmap);
            canvas2.translate(0.0f, this.crossfadeTextOffset);
            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.messageView.animatedEmojiStack;
            if (emojiGroupedSpans != null) {
                emojiGroupedSpans.clearPositions();
            }
            ChatMessageCell chatMessageCell4 = this.messageView;
            chatMessageCell4.drawMessageText(canvas2, chatMessageCell4.getMessageObject().textLayoutBlocks, true, 1.0f, true);
            this.messageView.drawAnimatedEmojis(canvas2, 1.0f);
        }
        float y = (this.listView.getY() - this.container.getY()) + this.listView.getMeasuredHeight();
        float x = this.fromStartX - this.container.getX();
        float y2 = this.fromStartY - this.container.getY();
        this.textX = this.messageView.getTextX();
        this.textY = this.messageView.getTextY();
        if (this.messageView.getMessageObject().stableId != this.messageId) {
            return;
        }
        float x2 = (this.messageView.getX() + this.listView.getX()) - this.container.getX();
        float top = ((this.messageView.getTop() + this.listView.getTop()) - this.container.getY()) + this.enterView.getTopViewHeight();
        float interpolation = ChatListItemAnimator.DEFAULT_INTERPOLATOR.getInterpolation(this.progress);
        float f21 = this.progress;
        float f22 = f21 > 0.4f ? 1.0f : f21 / 0.4f;
        float interpolation2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f21));
        float f23 = x2 + this.textX;
        float f24 = top + this.textY;
        float f25 = 1.0f - interpolation2;
        int measuredHeight = (int) ((this.container.getMeasuredHeight() * f25) + (y * interpolation2));
        boolean z2 = (this.messageView.getBottom() - AndroidUtilities.dp(4.0f) > this.listView.getMeasuredHeight()) && (((float) this.messageView.getMeasuredHeight()) + top) - ((float) AndroidUtilities.dp(8.0f)) > ((float) measuredHeight) && this.container.getMeasuredHeight() > 0;
        if (z2) {
            i = measuredHeight;
            f = interpolation2;
            f2 = f22;
            f3 = interpolation;
            canvas.saveLayerAlpha(0.0f, Math.max(0.0f, top), this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), 255, 31);
        } else {
            i = measuredHeight;
            f = interpolation2;
            f2 = f22;
            f3 = interpolation;
        }
        canvas.save();
        canvas.clipRect(0.0f, ((this.listView.getY() + this.chatActivity.getChatListViewPadding()) - this.container.getY()) - AndroidUtilities.dp(3.0f), this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        canvas.save();
        float backgroundDrawableLeft = this.messageView.getBackgroundDrawableLeft() + x2 + ((x - (f23 - this.toXOffset)) * f25);
        float backgroundDrawableTop = this.messageView.getBackgroundDrawableTop() + top;
        float f26 = 1.0f - f3;
        float y3 = ((this.drawableFromTop - this.container.getY()) * f26) + (backgroundDrawableTop * f3);
        float y4 = ((this.drawableFromBottom - this.container.getY()) * f26) + ((backgroundDrawableTop + (this.messageView.getBackgroundDrawableBottom() - this.messageView.getBackgroundDrawableTop())) * f3);
        int backgroundDrawableRight = (int) (this.messageView.getBackgroundDrawableRight() + x2 + (AndroidUtilities.dp(4.0f) * f25));
        Theme.MessageDrawable currentBackgroundDrawable = this.currentMessageObject.isAnimatedEmojiStickers() ? null : this.messageView.getCurrentBackgroundDrawable(true);
        if (currentBackgroundDrawable != null) {
            this.messageView.setBackgroundTopY(this.container.getTop() - this.listView.getTop());
            Drawable shadowDrawable = currentBackgroundDrawable.getShadowDrawable();
            f7 = f2;
            if (f7 == 1.0f || (drawable = this.fromMessageDrawable) == null) {
                f4 = f3;
                f5 = x;
            } else {
                f5 = x;
                f4 = f3;
                drawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                this.fromMessageDrawable.draw(canvas);
            }
            f6 = f;
            if (shadowDrawable != null) {
                shadowDrawable.setAlpha((int) (f6 * 255.0f));
                shadowDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                shadowDrawable.draw(canvas);
                shadowDrawable.setAlpha(255);
            }
            currentBackgroundDrawable.setAlpha((int) (f7 * 255.0f));
            currentBackgroundDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
            currentBackgroundDrawable.setDrawFullBubble(true);
            currentBackgroundDrawable.draw(canvas);
            z = false;
            currentBackgroundDrawable.setDrawFullBubble(false);
            currentBackgroundDrawable.setAlpha(255);
        } else {
            f4 = f3;
            f5 = x;
            f6 = f;
            f7 = f2;
            z = false;
        }
        canvas.restore();
        canvas.save();
        if (currentBackgroundDrawable != null) {
            if (this.currentMessageObject.isOutOwner()) {
                canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(10.0f), y4 - AndroidUtilities.dp(4.0f));
            } else {
                canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(4.0f), y4 - AndroidUtilities.dp(4.0f));
            }
        }
        float f27 = top + ((y2 - f24) * f26);
        canvas.translate((this.messageView.getLeft() + this.listView.getX()) - this.container.getX(), f27);
        this.messageView.drawTime(canvas, f7, z);
        this.messageView.drawNamesLayout(canvas, f7);
        this.messageView.drawCommentButton(canvas, f7);
        this.messageView.drawCaptionLayout(canvas, z, f7);
        this.messageView.drawLinkPreview(canvas, f7);
        canvas.restore();
        if (this.hasReply) {
            this.chatActivity.getReplyNameTextView().setAlpha(0.0f);
            this.chatActivity.getReplyObjectTextView().setAlpha(0.0f);
            AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), this.messageView.replyHeight, f6);
            int dp = AndroidUtilities.dp(10.0f);
            float x3 = this.replyFromStartX - this.container.getX();
            float y5 = this.replyFromStartY - this.container.getY();
            ChatMessageCell chatMessageCell5 = this.messageView;
            i2 = backgroundDrawableRight;
            float f28 = chatMessageCell5.replyStartX + x2;
            float f29 = top + chatMessageCell5.replyStartY;
            if (this.currentMessageObject.hasValidReplyMessageObject()) {
                MessageObject messageObject2 = this.currentMessageObject.replyMessageObject;
                f8 = y4;
                if (messageObject2.type == 0 || !TextUtils.isEmpty(messageObject2.caption)) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = this.currentMessageObject.replyMessageObject.messageOwner.media;
                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) && !(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice)) {
                        i3 = getThemedColor(Theme.key_chat_outReplyMessageText);
                        if (!this.currentMessageObject.isOutOwner()) {
                            themedColor = getThemedColor(Theme.key_chat_outReplyNameText);
                            f9 = y3;
                            getThemedColor(Theme.key_chat_outReplyLine);
                        } else {
                            f9 = y3;
                            themedColor = getThemedColor(Theme.key_chat_inReplyNameText);
                            getThemedColor(Theme.key_chat_inReplyLine);
                        }
                        f10 = backgroundDrawableLeft;
                        f12 = y2;
                        float f30 = f4;
                        Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, i3, f30));
                        Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor, f30));
                        if (this.messageView.needReplyImage) {
                            x3 -= AndroidUtilities.dp(44.0f);
                        }
                        float lerp = AndroidUtilities.lerp(x3, f28, f6);
                        float lerp2 = AndroidUtilities.lerp((AndroidUtilities.dp(12.0f) * f30) + y5, f29, f30);
                        if (this.roundRectRadii == null) {
                            this.roundRectRadii = r7;
                            float dp2 = AndroidUtilities.dp(4.0f);
                            float[] fArr = {dp2, dp2, 0.0f, 0.0f, 0.0f, 0.0f, dp2, dp2};
                            float[] fArr2 = this.roundRectRadii;
                            fArr2[5] = 0.0f;
                            fArr2[4] = 0.0f;
                            fArr2[3] = 0.0f;
                            fArr2[2] = 0.0f;
                        }
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(x3, y5, this.replyFromStartWidth + x3, AndroidUtilities.dp(35.0f) + y5);
                        rectF.offset(0.0f, AndroidUtilities.dp(12.0f) * f30);
                        this.messageReplySelectorRect.set(this.messageView.replySelectorRect);
                        this.messageReplySelectorRect.offset(x2, f27);
                        AndroidUtilities.lerp(rectF, this.messageReplySelectorRect, f6, this.replySelectorRect);
                        chatMessageCell = this.messageView;
                        if (chatMessageCell.replyLine == null) {
                            chatMessageCell.replyLine = new ReplyMessageLine(chatMessageCell);
                        }
                        ChatMessageCell chatMessageCell6 = this.messageView;
                        chatMessageCell6.replyLine.check(chatMessageCell6.getMessageObject(), this.messageView.getCurrentUser(), this.messageView.getCurrentChat(), this.resourcesProvider, true);
                        ChatMessageCell chatMessageCell7 = this.messageView;
                        chatMessageCell7.replyLine.drawBackground(canvas, this.replySelectorRect, f7, chatMessageCell7.isReplyQuote);
                        this.messageView.replyLine.drawLine(canvas, this.replySelectorRect, f7);
                        if (this.messageView.needReplyImage) {
                            f16 = 0.0f;
                        } else {
                            canvas.save();
                            float lerp3 = AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), Math.min(this.replySelectorRect.height() - AndroidUtilities.dp(10.0f), AndroidUtilities.dp(33.0f)), f6);
                            this.messageView.replyImageReceiver.setImageCoords(AndroidUtilities.lerp(lerp, this.replySelectorRect.left + AndroidUtilities.dp(8.0f), f6), AndroidUtilities.lerp(lerp2, this.replySelectorRect.top + AndroidUtilities.dp(5.0f), f6), lerp3, lerp3);
                            this.messageView.replyImageReceiver.draw(canvas);
                            canvas.restore();
                            f16 = lerp3;
                        }
                        canvas.save();
                        f17 = dp * f6;
                        canvas.translate(f17, 0.0f);
                        messageObject = this.currentMessageObject;
                        if (messageObject == null && messageObject.shouldDrawWithoutBackground()) {
                            f19 = -AndroidUtilities.dp(6.0f);
                            f18 = 1.0f;
                        } else {
                            f18 = 1.0f;
                            f19 = -AndroidUtilities.dp(1.0f);
                        }
                        MessageObject messageObject3 = this.currentMessageObject;
                        float dp3 = (messageObject3 == null && messageObject3.shouldDrawWithoutBackground()) ? AndroidUtilities.dp(f18) : AndroidUtilities.dp(3.0f);
                        int i4 = this.messageView.replyTextOffset;
                        float lerp4 = AndroidUtilities.lerp(x3 - i4, (f28 - i4) + f19, f6);
                        float lerp5 = AndroidUtilities.lerp(x3, (f28 - this.replyNameDx) + f19, f6) + (!this.messageView.needReplyImage ? AndroidUtilities.dp(3.0f) + f16 : 0.0f);
                        if (this.messageView.replyNameLayout == null) {
                            canvas.save();
                            canvas.translate(lerp5, (dp3 * f6) + lerp2);
                            int alpha = Theme.chat_replyNamePaint.getAlpha();
                            Theme.chat_replyNamePaint.setAlpha((int) (alpha * f6));
                            this.messageView.replyNameLayout.draw(canvas);
                            Theme.chat_replyNamePaint.setAlpha(alpha);
                            SimpleTextView replyNameTextView = this.chatActivity.getReplyNameTextView();
                            f20 = f27;
                            canvas.saveLayerAlpha(0.0f, 0.0f, replyNameTextView.getWidth(), replyNameTextView.getHeight(), (int) (f25 * 255.0f), 31);
                            replyNameTextView.setAlpha(1.0f);
                            replyNameTextView.draw(canvas);
                            replyNameTextView.setAlpha(0.0f);
                            canvas.restore();
                            canvas.restore();
                        } else {
                            f20 = f27;
                        }
                        chatMessageCell2 = this.messageView;
                        if (chatMessageCell2.isReplyQuote && chatMessageCell2.replyQuoteDrawable != null) {
                            color = chatMessageCell2.replyLine.getColor();
                            chatMessageCell3 = this.messageView;
                            if (color != chatMessageCell3.replyQuoteDrawableColor) {
                                Drawable drawable2 = chatMessageCell3.replyQuoteDrawable;
                                ChatMessageCell chatMessageCell8 = this.messageView;
                                int color2 = chatMessageCell8.replyLine.getColor();
                                chatMessageCell8.replyQuoteDrawableColor = color2;
                                drawable2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_IN));
                            }
                            ChatMessageCell chatMessageCell9 = this.messageView;
                            chatMessageCell9.replyQuoteDrawable.setBounds((int) (((this.replySelectorRect.right - f17) - AndroidUtilities.dp((!chatMessageCell9.drawPinnedTop ? 1 : 0) + 2)) - this.messageView.replyQuoteDrawable.getIntrinsicWidth()), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) ((this.replySelectorRect.right - f17) - AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2) + this.messageView.replyQuoteDrawable.getIntrinsicHeight()));
                            this.messageView.replyQuoteDrawable.setAlpha((int) (f6 * 255.0f));
                            this.messageView.replyQuoteDrawable.draw(canvas);
                        }
                        if (this.messageView.replyTextLayout == null) {
                            canvas.save();
                            ChatMessageCell chatMessageCell10 = this.messageView;
                            float dp4 = (!chatMessageCell10.needReplyImage || (chatMessageCell10.isReplyQuote && !chatMessageCell10.replyTextRTL)) ? 0.0f : AndroidUtilities.dp(3.0f) + f16;
                            ChatMessageCell chatMessageCell11 = this.messageView;
                            canvas.translate(lerp4 + dp4 + AndroidUtilities.dp((chatMessageCell11.isReplyQuote && chatMessageCell11.needReplyImage) ? -2.0f : 0.0f), lerp2 + AndroidUtilities.lerp(AndroidUtilities.dp(19.0f), Theme.chat_replyNamePaint.getTextSize() + AndroidUtilities.dp(4.0f) + dp3, f6));
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.messageView.replySpoilers);
                            ChatMessageCell chatMessageCell12 = this.messageView;
                            f13 = f30;
                            f11 = f20;
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, chatMessageCell12.replyTextLayout, chatMessageCell12.animatedEmojiReplyStack, 0.0f, chatMessageCell12.replySpoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                            this.messageView.replyTextLayout.draw(canvas);
                            canvas.restore();
                            for (SpoilerEffect spoilerEffect : this.messageView.replySpoilers) {
                                if (spoilerEffect.shouldInvalidateColor()) {
                                    spoilerEffect.setColor(this.messageView.replyTextLayout.getPaint().getColor());
                                }
                                spoilerEffect.draw(canvas);
                            }
                            canvas.restore();
                        } else {
                            f13 = f30;
                            f11 = f20;
                        }
                        canvas.restore();
                    }
                }
            } else {
                f8 = y4;
            }
            i3 = getThemedColor(Theme.key_chat_outReplyMediaMessageText);
            if (!this.currentMessageObject.isOutOwner()) {
            }
            f10 = backgroundDrawableLeft;
            f12 = y2;
            float f302 = f4;
            Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, i3, f302));
            Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor, f302));
            if (this.messageView.needReplyImage) {
            }
            float lerp6 = AndroidUtilities.lerp(x3, f28, f6);
            float lerp22 = AndroidUtilities.lerp((AndroidUtilities.dp(12.0f) * f302) + y5, f29, f302);
            if (this.roundRectRadii == null) {
            }
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(x3, y5, this.replyFromStartWidth + x3, AndroidUtilities.dp(35.0f) + y5);
            rectF2.offset(0.0f, AndroidUtilities.dp(12.0f) * f302);
            this.messageReplySelectorRect.set(this.messageView.replySelectorRect);
            this.messageReplySelectorRect.offset(x2, f27);
            AndroidUtilities.lerp(rectF2, this.messageReplySelectorRect, f6, this.replySelectorRect);
            chatMessageCell = this.messageView;
            if (chatMessageCell.replyLine == null) {
            }
            ChatMessageCell chatMessageCell62 = this.messageView;
            chatMessageCell62.replyLine.check(chatMessageCell62.getMessageObject(), this.messageView.getCurrentUser(), this.messageView.getCurrentChat(), this.resourcesProvider, true);
            ChatMessageCell chatMessageCell72 = this.messageView;
            chatMessageCell72.replyLine.drawBackground(canvas, this.replySelectorRect, f7, chatMessageCell72.isReplyQuote);
            this.messageView.replyLine.drawLine(canvas, this.replySelectorRect, f7);
            if (this.messageView.needReplyImage) {
            }
            canvas.save();
            f17 = dp * f6;
            canvas.translate(f17, 0.0f);
            messageObject = this.currentMessageObject;
            if (messageObject == null) {
            }
            f18 = 1.0f;
            f19 = -AndroidUtilities.dp(1.0f);
            MessageObject messageObject32 = this.currentMessageObject;
            float dp32 = (messageObject32 == null && messageObject32.shouldDrawWithoutBackground()) ? AndroidUtilities.dp(f18) : AndroidUtilities.dp(3.0f);
            int i42 = this.messageView.replyTextOffset;
            float lerp42 = AndroidUtilities.lerp(x3 - i42, (f28 - i42) + f19, f6);
            float lerp52 = AndroidUtilities.lerp(x3, (f28 - this.replyNameDx) + f19, f6) + (!this.messageView.needReplyImage ? AndroidUtilities.dp(3.0f) + f16 : 0.0f);
            if (this.messageView.replyNameLayout == null) {
            }
            chatMessageCell2 = this.messageView;
            if (chatMessageCell2.isReplyQuote) {
                color = chatMessageCell2.replyLine.getColor();
                chatMessageCell3 = this.messageView;
                if (color != chatMessageCell3.replyQuoteDrawableColor) {
                }
                ChatMessageCell chatMessageCell92 = this.messageView;
                chatMessageCell92.replyQuoteDrawable.setBounds((int) (((this.replySelectorRect.right - f17) - AndroidUtilities.dp((!chatMessageCell92.drawPinnedTop ? 1 : 0) + 2)) - this.messageView.replyQuoteDrawable.getIntrinsicWidth()), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) ((this.replySelectorRect.right - f17) - AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2) + this.messageView.replyQuoteDrawable.getIntrinsicHeight()));
                this.messageView.replyQuoteDrawable.setAlpha((int) (f6 * 255.0f));
                this.messageView.replyQuoteDrawable.draw(canvas);
            }
            if (this.messageView.replyTextLayout == null) {
            }
            canvas.restore();
        } else {
            i2 = backgroundDrawableRight;
            f8 = y4;
            f9 = y3;
            f10 = backgroundDrawableLeft;
            f11 = f27;
            f12 = y2;
            f13 = f4;
        }
        canvas.save();
        if (this.messageView.getMessageObject() == null || this.messageView.getMessageObject().type != 19) {
            canvas.clipRect(f10 + AndroidUtilities.dp(4.0f), f9 + AndroidUtilities.dp(4.0f), i2 - AndroidUtilities.dp(4.0f), f8 - AndroidUtilities.dp(4.0f));
        }
        float f31 = f6 + (this.scaleFrom * f25);
        float f32 = this.drawBitmaps ? (this.scaleY * f25) + f6 : 1.0f;
        canvas.save();
        float f33 = f5 * f25;
        float f34 = f12 * f26;
        canvas.translate(f33 + ((f23 - this.toXOffset) * f6), f34 + ((f24 + this.textLayoutBlock.textYOffset) * f13));
        float f35 = f31 * f32;
        canvas.scale(f31, f35, 0.0f, 0.0f);
        if (this.drawBitmaps) {
            if (this.crossfade) {
                this.bitmapPaint.setAlpha((int) ((1.0f - f7) * 255.0f));
            }
            canvas.drawBitmap(this.textLayoutBitmap, 0.0f, 0.0f, this.bitmapPaint);
            f14 = f11;
            f15 = f35;
        } else {
            boolean z3 = this.crossfade;
            if (z3 && this.changeColor) {
                int color3 = this.layout.getPaint().getColor();
                this.layout.getPaint().setColor(ColorUtils.blendARGB(this.fromColor, this.toColor, f7));
                float f36 = 1.0f - f7;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f36 * 255.0f), 31);
                this.layout.draw(canvas);
                f14 = f11;
                f15 = f35;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f36);
                this.layout.getPaint().setColor(color3);
                canvas.restore();
            } else {
                f14 = f11;
                f15 = f35;
                if (z3) {
                    float f37 = 1.0f - f7;
                    canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f37 * 255.0f), 31);
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f37);
                    canvas.restore();
                } else {
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                }
            }
        }
        canvas.restore();
        if (this.rtlLayout != null) {
            canvas.save();
            canvas.translate(f33 + ((f23 - this.toXOffsetRtl) * f6), f34 + ((f24 + this.textLayoutBlock.textYOffset) * f13));
            canvas.scale(f31, f15, 0.0f, 0.0f);
            if (this.drawBitmaps) {
                if (this.crossfade) {
                    this.bitmapPaint.setAlpha((int) ((1.0f - f7) * 255.0f));
                }
                canvas.drawBitmap(this.textLayoutBitmapRtl, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                boolean z4 = this.crossfade;
                if (z4 && this.changeColor) {
                    int color4 = this.rtlLayout.getPaint().getColor();
                    this.rtlLayout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f7), (int) (Color.alpha(color4) * (1.0f - f7))));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setColor(color4);
                } else if (z4) {
                    int alpha2 = this.rtlLayout.getPaint().getAlpha();
                    this.rtlLayout.getPaint().setAlpha((int) (alpha2 * (1.0f - f7)));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setAlpha(alpha2);
                } else {
                    this.rtlLayout.draw(canvas);
                }
            }
            canvas.restore();
        }
        if (this.crossfade) {
            canvas.save();
            canvas.translate(((this.messageView.getLeft() + this.listView.getX()) - this.container.getX()) + ((f5 - f23) * f25), f14);
            canvas.scale(f31, f15, this.messageView.getTextX(), this.messageView.getTextY());
            canvas.translate(0.0f, -this.crossfadeTextOffset);
            if (this.crossfadeTextBitmap != null) {
                this.bitmapPaint.setAlpha((int) (f7 * 255.0f));
                canvas.drawBitmap(this.crossfadeTextBitmap, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                int color5 = Theme.chat_msgTextPaint.getColor();
                Theme.chat_msgTextPaint.setColor(this.toColor);
                ChatMessageCell chatMessageCell13 = this.messageView;
                chatMessageCell13.drawMessageText(canvas, chatMessageCell13.getMessageObject().textLayoutBlocks, true, f7, true);
                this.messageView.drawAnimatedEmojis(canvas, f7);
                if (Theme.chat_msgTextPaint.getColor() != color5) {
                    Theme.chat_msgTextPaint.setColor(color5);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        if (z2) {
            float f38 = i;
            this.gradientMatrix.setTranslate(0.0f, f38);
            this.gradientShader.setLocalMatrix(this.gradientMatrix);
            canvas.drawRect(0.0f, f38, this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), this.gradientPaint);
            canvas.restore();
        }
        float f39 = this.progress;
        float f40 = f39 > 0.4f ? 1.0f : f39 / 0.4f;
        if (f40 == 1.0f) {
            this.enterView.setTextTransitionIsRunning(false);
        }
        if (this.enterView.getSendButton().getVisibility() != 0 || f40 >= 1.0f) {
            return;
        }
        canvas.save();
        canvas.translate(((((this.enterView.getX() + this.enterView.getSendButton().getX()) + ((View) this.enterView.getSendButton().getParent()).getX()) + ((View) this.enterView.getSendButton().getParent().getParent()).getX()) - this.container.getX()) + (AndroidUtilities.dp(52.0f) * f40), (((this.enterView.getY() + this.enterView.getSendButton().getY()) + ((View) this.enterView.getSendButton().getParent()).getY()) + ((View) this.enterView.getSendButton().getParent().getParent()).getY()) - this.container.getY());
        this.enterView.getSendButton().draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }
}
