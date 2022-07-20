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
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.MessageEnterTransitionContainer;
/* loaded from: classes3.dex */
public class TextMessageEnterTransition implements MessageEnterTransitionContainer.Transition {
    private int animationIndex;
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
    float replyFromStartX;
    float replyFromStartY;
    float replyMessageDx;
    float replyNameDx;
    private final Theme.ResourcesProvider resourcesProvider;
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
    private final int currentAccount = UserConfig.selectedAccount;

    @SuppressLint({"WrongConstant"})
    public TextMessageEnterTransition(ChatMessageCell chatMessageCell, ChatActivity chatActivity, RecyclerListView recyclerListView, MessageEnterTransitionContainer messageEnterTransitionContainer, Theme.ResourcesProvider resourcesProvider) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        Theme.MessageDrawable currentBackgroundDrawable;
        this.drawBitmaps = false;
        this.animationIndex = -1;
        this.resourcesProvider = resourcesProvider;
        if (chatMessageCell.getMessageObject().textLayoutBlocks.size() > 1 || chatMessageCell.getMessageObject().textLayoutBlocks.isEmpty() || chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getLineCount() > 10) {
            return;
        }
        this.messageView = chatMessageCell;
        this.listView = recyclerListView;
        this.container = messageEnterTransitionContainer;
        this.chatActivity = chatActivity;
        this.enterView = chatActivity.getChatActivityEnterView();
        ChatActivityEnterView chatActivityEnterView = chatActivity.getChatActivityEnterView();
        if (chatActivityEnterView == null || chatActivityEnterView.getEditField() == null || chatActivityEnterView.getEditField().getLayout() == null) {
            return;
        }
        float f = chatActivityEnterView.getRecordCicle().drawingCircleRadius;
        this.bitmapPaint.setFilterBitmap(true);
        this.currentMessageObject = chatMessageCell.getMessageObject();
        if (!chatMessageCell.getTransitionParams().wasDraw) {
            chatMessageCell.draw(new Canvas());
        }
        chatMessageCell.setEnterTransitionInProgress(true);
        CharSequence text = chatActivityEnterView.getEditField().getLayout().getText();
        CharSequence charSequence = chatMessageCell.getMessageObject().messageText;
        this.crossfade = false;
        int height = chatActivityEnterView.getEditField().getLayout().getHeight();
        TextPaint textPaint = Theme.chat_msgTextPaint;
        int dp = AndroidUtilities.dp(20.0f);
        if (chatMessageCell.getMessageObject().getEmojiOnlyCount() != 0) {
            if (chatMessageCell.getMessageObject().getEmojiOnlyCount() == 1) {
                textPaint = Theme.chat_msgTextPaintOneEmoji;
                dp = AndroidUtilities.dp(32.0f);
            } else if (chatMessageCell.getMessageObject().getEmojiOnlyCount() == 2) {
                textPaint = Theme.chat_msgTextPaintTwoEmoji;
                dp = AndroidUtilities.dp(28.0f);
            } else if (chatMessageCell.getMessageObject().getEmojiOnlyCount() == 3) {
                textPaint = Theme.chat_msgTextPaintThreeEmoji;
                dp = AndroidUtilities.dp(24.0f);
            }
        }
        if (charSequence instanceof Spannable) {
            for (Object obj : ((Spannable) charSequence).getSpans(0, charSequence.length(), Object.class)) {
                if (!(obj instanceof Emoji.EmojiSpan)) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (text.length() != charSequence.length() || z) {
            this.crossfade = true;
            String charSequence2 = text.toString();
            String trim = charSequence2.trim();
            int indexOf = charSequence2.indexOf(trim);
            if (indexOf > 0) {
                i = chatActivityEnterView.getEditField().getLayout().getLineTop(chatActivityEnterView.getEditField().getLayout().getLineForOffset(indexOf));
                i2 = chatActivityEnterView.getEditField().getLayout().getLineBottom(chatActivityEnterView.getEditField().getLayout().getLineForOffset(indexOf + trim.length())) - i;
            } else {
                i2 = height;
                i = 0;
            }
            charSequence = Emoji.replaceEmoji(trim, textPaint.getFontMetricsInt(), dp, false);
        } else {
            i2 = height;
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
        float y = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
        this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
        this.fromStartY = ((AndroidUtilities.dp(10.0f) + y) - chatActivityEnterView.getEditField().getScrollY()) + i;
        this.toXOffset = 0.0f;
        float f2 = Float.MAX_VALUE;
        for (int i5 = 0; i5 < this.layout.getLineCount(); i5++) {
            float lineLeft = this.layout.getLineLeft(i5);
            if (lineLeft < f2) {
                f2 = lineLeft;
            }
        }
        if (f2 != Float.MAX_VALUE) {
            this.toXOffset = f2;
        }
        this.scaleY = i2 / (this.layout.getHeight() * this.scaleFrom);
        this.drawableFromTop = AndroidUtilities.dp(4.0f) + y;
        if (this.enterView.isTopViewVisible()) {
            this.drawableFromTop -= AndroidUtilities.dp(12.0f);
        }
        this.drawableFromBottom = y + chatActivityEnterView.getEditField().getMeasuredHeight();
        MessageObject.TextLayoutBlock textLayoutBlock = chatMessageCell.getMessageObject().textLayoutBlocks.get(0);
        this.textLayoutBlock = textLayoutBlock;
        StaticLayout staticLayout = textLayoutBlock.textLayout;
        if (Math.abs(ColorUtils.calculateLuminance(getThemedColor("chat_messageTextOut")) - ColorUtils.calculateLuminance(getThemedColor("chat_messagePanelText"))) > 0.20000000298023224d) {
            this.crossfade = true;
            this.changeColor = true;
        }
        this.fromColor = getThemedColor("chat_messagePanelText");
        this.toColor = getThemedColor("chat_messageTextOut");
        if (staticLayout.getLineCount() == this.layout.getLineCount()) {
            lineCount = staticLayout.getLineCount();
            int i6 = 0;
            i4 = 0;
            i3 = 0;
            while (true) {
                if (i6 >= lineCount) {
                    break;
                }
                if (isRtlLine(this.layout, i6)) {
                    i3++;
                } else {
                    i4++;
                }
                if (staticLayout.getLineEnd(i6) != this.layout.getLineEnd(i6)) {
                    this.crossfade = true;
                    break;
                }
                i6++;
            }
        } else {
            this.crossfade = true;
            i4 = 0;
            i3 = 0;
        }
        if (!this.crossfade && i3 > 0 && i4 > 0) {
            SpannableString spannableString = new SpannableString(charSequence);
            SpannableString spannableString2 = new SpannableString(charSequence);
            float f3 = Float.MAX_VALUE;
            for (int i7 = 0; i7 < lineCount; i7++) {
                if (isRtlLine(this.layout, i7)) {
                    spannableString.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i7), this.layout.getLineEnd(i7), 0);
                    float lineLeft2 = this.layout.getLineLeft(i7);
                    f3 = lineLeft2 < f3 ? lineLeft2 : f3;
                } else {
                    spannableString2.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i7), this.layout.getLineEnd(i7), 0);
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
        boolean z2 = (chatMessageCell.getMessageObject().getReplyMsgId() == 0 || chatMessageCell.replyNameLayout == null) ? false : true;
        this.hasReply = z2;
        if (z2) {
            SimpleTextView replyNameTextView = chatActivity.getReplyNameTextView();
            this.replyFromStartX = replyNameTextView.getX() + ((View) replyNameTextView.getParent()).getX();
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
        StaticLayout staticLayout4 = chatMessageCell.replyTextLayout;
        if (staticLayout4 != null && staticLayout4.getText().length() >= 1 && chatMessageCell.replyTextLayout.getPrimaryHorizontal(0) != 0.0f) {
            this.replyMessageDx = chatMessageCell.replyTextLayout.getWidth() - chatMessageCell.replyTextLayout.getLineWidth(0);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new TextMessageEnterTransition$$ExternalSyntheticLambda0(this, chatActivityEnterView, messageEnterTransitionContainer));
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.setDuration(250L);
        messageEnterTransitionContainer.addTransition(this);
        this.animationIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.animationIndex, null);
        this.animator.addListener(new AnonymousClass1(messageEnterTransitionContainer, chatMessageCell, chatActivityEnterView, chatActivity));
        if (SharedConfig.getDevicePerformanceClass() != 2 || (currentBackgroundDrawable = chatMessageCell.getCurrentBackgroundDrawable(true)) == null) {
            return;
        }
        this.fromMessageDrawable = currentBackgroundDrawable.getTransitionDrawable(getThemedColor("chat_messagePanelBackground"));
    }

    public /* synthetic */ void lambda$new$0(ChatActivityEnterView chatActivityEnterView, MessageEnterTransitionContainer messageEnterTransitionContainer, ValueAnimator valueAnimator) {
        this.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatActivityEnterView.getEditField().setAlpha(this.progress);
        messageEnterTransitionContainer.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.TextMessageEnterTransition$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ ChatActivityEnterView val$chatActivityEnterView;
        final /* synthetic */ MessageEnterTransitionContainer val$container;
        final /* synthetic */ ChatMessageCell val$messageView;

        AnonymousClass1(MessageEnterTransitionContainer messageEnterTransitionContainer, ChatMessageCell chatMessageCell, ChatActivityEnterView chatActivityEnterView, ChatActivity chatActivity) {
            TextMessageEnterTransition.this = r1;
            this.val$container = messageEnterTransitionContainer;
            this.val$messageView = chatMessageCell;
            this.val$chatActivityEnterView = chatActivityEnterView;
            this.val$chatActivity = chatActivity;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(TextMessageEnterTransition.this.currentAccount).onAnimationFinish(TextMessageEnterTransition.this.animationIndex);
            this.val$container.removeTransition(TextMessageEnterTransition.this);
            this.val$messageView.setEnterTransitionInProgress(false);
            this.val$chatActivityEnterView.setTextTransitionIsRunning(false);
            this.val$chatActivityEnterView.getEditField().setAlpha(1.0f);
            this.val$chatActivity.getReplyNameTextView().setAlpha(1.0f);
            this.val$chatActivity.getReplyObjectTextView().setAlpha(1.0f);
        }
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

    /* JADX WARN: Removed duplicated region for block: B:65:0x0353  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0360  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0392  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0453  */
    @Override // org.telegram.ui.MessageEnterTransitionContainer.Transition
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        float f;
        int i;
        float f2;
        float f3;
        float f4;
        boolean z;
        float f5;
        float f6;
        float f7;
        int i2;
        float f8;
        int i3;
        int i4;
        int i5;
        Drawable drawable;
        if (this.drawBitmaps && !this.initBitmaps && this.crossfadeTextBitmap != null && this.messageView.getTransitionParams().wasDraw) {
            this.initBitmaps = true;
            Canvas canvas2 = new Canvas(this.crossfadeTextBitmap);
            canvas2.translate(0.0f, this.crossfadeTextOffset);
            ChatMessageCell chatMessageCell = this.messageView;
            chatMessageCell.drawMessageText(canvas2, chatMessageCell.getMessageObject().textLayoutBlocks, true, 1.0f, true);
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
        float f9 = this.progress;
        float f10 = f9 > 0.4f ? 1.0f : f9 / 0.4f;
        float interpolation2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f9));
        float f11 = x2 + this.textX;
        float f12 = top + this.textY;
        float f13 = 1.0f - interpolation2;
        int measuredHeight = (int) ((this.container.getMeasuredHeight() * f13) + (y * interpolation2));
        boolean z2 = (this.messageView.getBottom() - AndroidUtilities.dp(4.0f) > this.listView.getMeasuredHeight()) && (((float) this.messageView.getMeasuredHeight()) + top) - ((float) AndroidUtilities.dp(8.0f)) > ((float) measuredHeight) && this.container.getMeasuredHeight() > 0;
        if (z2) {
            i = measuredHeight;
            f = f10;
            canvas.saveLayerAlpha(0.0f, Math.max(0.0f, top), this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), 255, 31);
        } else {
            i = measuredHeight;
            f = f10;
        }
        canvas.save();
        canvas.clipRect(0.0f, ((this.listView.getY() + this.chatActivity.getChatListViewPadding()) - this.container.getY()) - AndroidUtilities.dp(3.0f), this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        canvas.save();
        float backgroundDrawableLeft = this.messageView.getBackgroundDrawableLeft() + x2 + ((x - (f11 - this.toXOffset)) * f13);
        float backgroundDrawableTop = this.messageView.getBackgroundDrawableTop() + top;
        float f14 = 1.0f - interpolation;
        float y3 = ((this.drawableFromTop - this.container.getY()) * f14) + (backgroundDrawableTop * interpolation);
        float y4 = ((this.drawableFromBottom - this.container.getY()) * f14) + ((backgroundDrawableTop + (this.messageView.getBackgroundDrawableBottom() - this.messageView.getBackgroundDrawableTop())) * interpolation);
        int backgroundDrawableRight = (int) (this.messageView.getBackgroundDrawableRight() + x2 + (AndroidUtilities.dp(4.0f) * f13));
        Theme.MessageDrawable currentBackgroundDrawable = this.messageView.getCurrentBackgroundDrawable(true);
        if (currentBackgroundDrawable != null) {
            this.messageView.setBackgroundTopY(this.container.getTop() - this.listView.getTop());
            Drawable shadowDrawable = currentBackgroundDrawable.getShadowDrawable();
            f4 = f;
            if (f4 == 1.0f || (drawable = this.fromMessageDrawable) == null) {
                f3 = x;
                f2 = interpolation;
            } else {
                f3 = x;
                f2 = interpolation;
                drawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                this.fromMessageDrawable.draw(canvas);
            }
            if (shadowDrawable != null) {
                shadowDrawable.setAlpha((int) (interpolation2 * 255.0f));
                shadowDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                shadowDrawable.draw(canvas);
                shadowDrawable.setAlpha(255);
            }
            currentBackgroundDrawable.setAlpha((int) (f4 * 255.0f));
            currentBackgroundDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
            currentBackgroundDrawable.setDrawFullBubble(true);
            currentBackgroundDrawable.draw(canvas);
            z = false;
            currentBackgroundDrawable.setDrawFullBubble(false);
            currentBackgroundDrawable.setAlpha(255);
        } else {
            f3 = x;
            f2 = interpolation;
            f4 = f;
            z = false;
        }
        canvas.restore();
        canvas.save();
        if (this.currentMessageObject.isOutOwner()) {
            canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(10.0f), y4 - AndroidUtilities.dp(4.0f));
        } else {
            canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(4.0f), y4 - AndroidUtilities.dp(4.0f));
        }
        float f15 = top + ((y2 - f12) * f14);
        canvas.translate((this.messageView.getLeft() + this.listView.getX()) - this.container.getX(), f15);
        this.messageView.drawTime(canvas, f4, z);
        this.messageView.drawNamesLayout(canvas, f4);
        this.messageView.drawCommentButton(canvas, f4);
        this.messageView.drawCaptionLayout(canvas, z, f4);
        this.messageView.drawLinkPreview(canvas, f4);
        canvas.restore();
        if (this.hasReply) {
            this.chatActivity.getReplyNameTextView().setAlpha(0.0f);
            this.chatActivity.getReplyObjectTextView().setAlpha(0.0f);
            float x3 = this.replyFromStartX - this.container.getX();
            float y5 = this.replyFromStartY - this.container.getY();
            ChatMessageCell chatMessageCell2 = this.messageView;
            float f16 = x2 + chatMessageCell2.replyStartX;
            float f17 = top + chatMessageCell2.replyStartY;
            if (this.currentMessageObject.hasValidReplyMessageObject()) {
                MessageObject messageObject = this.currentMessageObject.replyMessageObject;
                if (messageObject.type == 0 || !TextUtils.isEmpty(messageObject.caption)) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = this.currentMessageObject.replyMessageObject.messageOwner.media;
                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) && !(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice)) {
                        i3 = getThemedColor("chat_outReplyMessageText");
                        if (!this.currentMessageObject.isOutOwner()) {
                            i5 = getThemedColor("chat_outReplyNameText");
                            i4 = getThemedColor("chat_outReplyLine");
                        } else {
                            i5 = getThemedColor("chat_inReplyNameText");
                            i4 = getThemedColor("chat_inReplyLine");
                        }
                        i2 = backgroundDrawableRight;
                        f7 = y4;
                        f6 = f15;
                        f8 = f2;
                        Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, i3, f8));
                        Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, i5, f8));
                        if (this.messageView.needReplyImage) {
                            x3 -= AndroidUtilities.dp(44.0f);
                        }
                        float f18 = x3;
                        float f19 = f18 * f13;
                        float f20 = f19 + (f16 * interpolation2);
                        float dp = (f17 * f8) + ((y5 + (AndroidUtilities.dp(12.0f) * f8)) * f14);
                        Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(i4, (int) (Color.alpha(i4) * interpolation2)));
                        f5 = y3;
                        canvas.drawRect(f20, dp, f20 + AndroidUtilities.dp(2.0f), dp + AndroidUtilities.dp(35.0f), Theme.chat_replyLinePaint);
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(10.0f) * interpolation2, 0.0f);
                        if (this.messageView.needReplyImage) {
                            canvas.save();
                            this.messageView.replyImageReceiver.setImageCoords(f20, dp, AndroidUtilities.dp(35.0f), AndroidUtilities.dp(35.0f));
                            this.messageView.replyImageReceiver.draw(canvas);
                            canvas.translate(f20, dp);
                            canvas.restore();
                            canvas.translate(AndroidUtilities.dp(44.0f), 0.0f);
                        }
                        float f21 = this.replyMessageDx;
                        float f22 = ((f18 - f21) * f13) + ((f16 - f21) * interpolation2);
                        float f23 = f19 + ((f16 - this.replyNameDx) * interpolation2);
                        if (this.messageView.replyNameLayout != null) {
                            canvas.save();
                            canvas.translate(f23, dp);
                            this.messageView.replyNameLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (this.messageView.replyTextLayout != null) {
                            canvas.save();
                            canvas.translate(f22, dp + AndroidUtilities.dp(19.0f));
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.messageView.replySpoilers);
                            this.messageView.replyTextLayout.draw(canvas);
                            canvas.restore();
                            for (SpoilerEffect spoilerEffect : this.messageView.replySpoilers) {
                                if (spoilerEffect.shouldInvalidateColor()) {
                                    spoilerEffect.setColor(this.messageView.replyTextLayout.getPaint().getColor());
                                }
                                spoilerEffect.draw(canvas);
                            }
                            canvas.restore();
                        }
                        canvas.restore();
                    }
                }
            }
            i3 = getThemedColor("chat_outReplyMediaMessageText");
            if (!this.currentMessageObject.isOutOwner()) {
            }
            i2 = backgroundDrawableRight;
            f7 = y4;
            f6 = f15;
            f8 = f2;
            Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, i3, f8));
            Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, i5, f8));
            if (this.messageView.needReplyImage) {
            }
            float f182 = x3;
            float f192 = f182 * f13;
            float f202 = f192 + (f16 * interpolation2);
            float dp2 = (f17 * f8) + ((y5 + (AndroidUtilities.dp(12.0f) * f8)) * f14);
            Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(i4, (int) (Color.alpha(i4) * interpolation2)));
            f5 = y3;
            canvas.drawRect(f202, dp2, f202 + AndroidUtilities.dp(2.0f), dp2 + AndroidUtilities.dp(35.0f), Theme.chat_replyLinePaint);
            canvas.save();
            canvas.translate(AndroidUtilities.dp(10.0f) * interpolation2, 0.0f);
            if (this.messageView.needReplyImage) {
            }
            float f212 = this.replyMessageDx;
            float f222 = ((f182 - f212) * f13) + ((f16 - f212) * interpolation2);
            float f232 = f192 + ((f16 - this.replyNameDx) * interpolation2);
            if (this.messageView.replyNameLayout != null) {
            }
            if (this.messageView.replyTextLayout != null) {
            }
            canvas.restore();
        } else {
            i2 = backgroundDrawableRight;
            f7 = y4;
            f5 = y3;
            f6 = f15;
            f8 = f2;
        }
        canvas.save();
        canvas.clipRect(backgroundDrawableLeft + AndroidUtilities.dp(4.0f), f5 + AndroidUtilities.dp(4.0f), i2 - AndroidUtilities.dp(4.0f), f7 - AndroidUtilities.dp(4.0f));
        float f24 = interpolation2 + (this.scaleFrom * f13);
        float f25 = this.drawBitmaps ? interpolation2 + (this.scaleY * f13) : 1.0f;
        canvas.save();
        float f26 = f3 * f13;
        float f27 = y2 * f14;
        canvas.translate(((f11 - this.toXOffset) * interpolation2) + f26, ((f12 + this.textLayoutBlock.textYOffset) * f8) + f27);
        float f28 = f25 * f24;
        canvas.scale(f24, f28, 0.0f, 0.0f);
        if (this.drawBitmaps) {
            if (this.crossfade) {
                this.bitmapPaint.setAlpha((int) ((1.0f - f4) * 255.0f));
            }
            canvas.drawBitmap(this.textLayoutBitmap, 0.0f, 0.0f, this.bitmapPaint);
        } else {
            boolean z3 = this.crossfade;
            if (z3 && this.changeColor) {
                int color = this.layout.getPaint().getColor();
                this.layout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f4), (int) (Color.alpha(color) * (1.0f - f4))));
                this.layout.draw(canvas);
                this.layout.getPaint().setColor(color);
            } else if (z3) {
                int alpha = Theme.chat_msgTextPaint.getAlpha();
                Theme.chat_msgTextPaint.setAlpha((int) (alpha * (1.0f - f4)));
                this.layout.draw(canvas);
                Theme.chat_msgTextPaint.setAlpha(alpha);
            } else {
                this.layout.draw(canvas);
            }
        }
        canvas.restore();
        if (this.rtlLayout != null) {
            canvas.save();
            canvas.translate(f26 + ((f11 - this.toXOffsetRtl) * interpolation2), f27 + ((f12 + this.textLayoutBlock.textYOffset) * f8));
            canvas.scale(f24, f28, 0.0f, 0.0f);
            if (this.drawBitmaps) {
                if (this.crossfade) {
                    this.bitmapPaint.setAlpha((int) ((1.0f - f4) * 255.0f));
                }
                canvas.drawBitmap(this.textLayoutBitmapRtl, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                boolean z4 = this.crossfade;
                if (z4 && this.changeColor) {
                    int color2 = this.rtlLayout.getPaint().getColor();
                    this.rtlLayout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f4), (int) (Color.alpha(color2) * (1.0f - f4))));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setColor(color2);
                } else if (z4) {
                    int alpha2 = this.rtlLayout.getPaint().getAlpha();
                    this.rtlLayout.getPaint().setAlpha((int) (alpha2 * (1.0f - f4)));
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
            canvas.translate(((this.messageView.getLeft() + this.listView.getX()) - this.container.getX()) + ((f3 - f11) * f13), f6);
            canvas.scale(f24, f28, this.messageView.getTextX(), this.messageView.getTextY());
            canvas.translate(0.0f, -this.crossfadeTextOffset);
            if (this.crossfadeTextBitmap != null) {
                this.bitmapPaint.setAlpha((int) (f4 * 255.0f));
                canvas.drawBitmap(this.crossfadeTextBitmap, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                int color3 = Theme.chat_msgTextPaint.getColor();
                Theme.chat_msgTextPaint.setColor(this.toColor);
                ChatMessageCell chatMessageCell3 = this.messageView;
                chatMessageCell3.drawMessageText(canvas, chatMessageCell3.getMessageObject().textLayoutBlocks, false, f4, true);
                if (Theme.chat_msgTextPaint.getColor() != color3) {
                    Theme.chat_msgTextPaint.setColor(color3);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        if (z2) {
            float f29 = i;
            this.gradientMatrix.setTranslate(0.0f, f29);
            this.gradientShader.setLocalMatrix(this.gradientMatrix);
            canvas.drawRect(0.0f, f29, this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), this.gradientPaint);
            canvas.restore();
        }
        float f30 = this.progress;
        float f31 = f30 > 0.4f ? 1.0f : f30 / 0.4f;
        if (f31 == 1.0f) {
            this.enterView.setTextTransitionIsRunning(false);
        }
        if (this.enterView.getSendButton().getVisibility() != 0 || f31 >= 1.0f) {
            return;
        }
        canvas.save();
        canvas.translate(((((this.enterView.getX() + this.enterView.getSendButton().getX()) + ((View) this.enterView.getSendButton().getParent()).getX()) + ((View) this.enterView.getSendButton().getParent().getParent()).getX()) - this.container.getX()) + (AndroidUtilities.dp(52.0f) * f31), (((this.enterView.getY() + this.enterView.getSendButton().getY()) + ((View) this.enterView.getSendButton().getParent()).getY()) + ((View) this.enterView.getSendButton().getParent().getParent()).getY()) - this.container.getY());
        this.enterView.getSendButton().draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
