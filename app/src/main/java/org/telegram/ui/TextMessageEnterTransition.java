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
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.MessageEnterTransitionContainer;
/* loaded from: classes3.dex */
public class TextMessageEnterTransition implements MessageEnterTransitionContainer.Transition {
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
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

    /* JADX WARN: Can't wrap try/catch for region: R(42:15|(1:17)|18|(3:20|21|(1:24))|30|(3:32|(3:35|(33:38|39|40|(3:150|(1:152)(1:154)|153)(1:43)|44|(1:46)(1:149)|47|(4:50|(2:52|53)(1:55)|54|48)|56|57|(1:59)|60|(1:62)|63|(1:65)|66|(2:68|(1:(2:146|147)(4:70|(1:72)(1:145)|73|(2:76|77)(1:75))))(1:148)|78|(5:82|(3:84|(4:86|(1:88)|89|90)(2:92|93)|91)|94|95|(1:97)(1:98))|99|100|101|(4:103|(1:105)|106|(2:108|(1:110)(1:111)))|112|(1:143)(1:116)|117|(1:119)|120|(1:126)|127|(1:133)|134|(1:141)(2:138|139))(1:37)|33)|155)|156|40|(0)|150|(0)(0)|153|44|(0)(0)|47|(1:48)|56|57|(0)|60|(0)|63|(0)|66|(0)(0)|78|(5:82|(0)|94|95|(0)(0))|99|100|101|(0)|112|(1:114)|143|117|(0)|120|(3:122|124|126)|127|(3:129|131|133)|134|(2:136|141)(1:142)) */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x04cf, code lost:
        r25.drawBitmaps = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0453 A[Catch: Exception -> 0x04cf, TryCatch #0 {Exception -> 0x04cf, blocks: (B:101:0x044f, B:103:0x0453, B:105:0x0477, B:106:0x0495, B:108:0x0499, B:110:0x04a3, B:111:0x04b7), top: B:100:0x044f }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x04e7  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x02af  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x02d4  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0389  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x040e  */
    @SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextMessageEnterTransition(final ChatMessageCell chatMessageCell, final ChatActivity chatActivity, RecyclerListView recyclerListView, final MessageEnterTransitionContainer messageEnterTransitionContainer, Theme.ResourcesProvider resourcesProvider) {
        boolean z;
        int[] iArr;
        int i;
        int i2;
        int i3;
        int lineCount;
        float f;
        int i4;
        StaticLayout staticLayout;
        int i5;
        int i6;
        boolean z2;
        StaticLayout staticLayout2;
        StaticLayout staticLayout3;
        Theme.MessageDrawable currentBackgroundDrawable;
        int i7;
        this.drawBitmaps = false;
        this.animationIndex = -1;
        this.resourcesProvider = resourcesProvider;
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
        float f2 = chatActivityEnterView.getRecordCicle().drawingCircleRadius;
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
            switch (chatMessageCell.getMessageObject().getEmojiOnlyCount()) {
                case 0:
                case 1:
                case 2:
                    textPaint = Theme.chat_msgTextPaintEmoji[0];
                    break;
                case 3:
                    textPaint = Theme.chat_msgTextPaintEmoji[1];
                    break;
                case 4:
                    textPaint = Theme.chat_msgTextPaintEmoji[2];
                    break;
                case 5:
                    textPaint = Theme.chat_msgTextPaintEmoji[3];
                    break;
                case 6:
                    textPaint = Theme.chat_msgTextPaintEmoji[4];
                    break;
                default:
                    textPaint = Theme.chat_msgTextPaintEmoji[5];
                    break;
            }
            if (textPaint != null) {
                dp = (int) (textPaint.getTextSize() + AndroidUtilities.dp(4.0f));
            }
        }
        if (charSequence instanceof Spannable) {
            for (Object obj : ((Spannable) charSequence).getSpans(0, charSequence.length(), Object.class)) {
                if (!(obj instanceof Emoji.EmojiSpan)) {
                    z = true;
                    if (text.length() == charSequence.length() || z) {
                        this.crossfade = true;
                        iArr = new int[1];
                        CharSequence trim = AndroidUtilities.trim(text, iArr);
                        if (iArr[0] <= 0) {
                            i2 = chatActivityEnterView.getEditField().getLayout().getLineTop(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0]));
                            i = chatActivityEnterView.getEditField().getLayout().getLineBottom(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0] + trim.length())) - i2;
                        } else {
                            i = height;
                            i2 = 0;
                        }
                        int i8 = i;
                        charSequence = AnimatedEmojiSpan.cloneSpans(Emoji.replaceEmoji(text, textPaint.getFontMetricsInt(), dp, false));
                        i3 = i8;
                    } else {
                        i3 = height;
                        i2 = 0;
                    }
                    this.scaleFrom = chatActivityEnterView.getEditField().getTextSize() / textPaint.getTextSize();
                    lineCount = chatActivityEnterView.getEditField().getLayout().getLineCount();
                    int width = (int) (chatActivityEnterView.getEditField().getLayout().getWidth() / this.scaleFrom);
                    if (Build.VERSION.SDK_INT < 24) {
                        this.layout = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                    } else {
                        this.layout = new StaticLayout(charSequence, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(2, (View) null, this.animatedEmojiStack, this.layout);
                    float y = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
                    this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
                    this.fromStartY = ((AndroidUtilities.dp(10.0f) + y) - chatActivityEnterView.getEditField().getScrollY()) + i2;
                    this.toXOffset = 0.0f;
                    f = Float.MAX_VALUE;
                    for (i4 = 0; i4 < this.layout.getLineCount(); i4++) {
                        float lineLeft = this.layout.getLineLeft(i4);
                        if (lineLeft < f) {
                            f = lineLeft;
                        }
                    }
                    if (f != Float.MAX_VALUE) {
                        this.toXOffset = f;
                    }
                    this.scaleY = i3 / (this.layout.getHeight() * this.scaleFrom);
                    this.drawableFromTop = AndroidUtilities.dp(4.0f) + y;
                    if (this.enterView.isTopViewVisible()) {
                        this.drawableFromTop -= AndroidUtilities.dp(12.0f);
                    }
                    this.drawableFromBottom = y + chatActivityEnterView.getEditField().getMeasuredHeight();
                    MessageObject.TextLayoutBlock textLayoutBlock = chatMessageCell.getMessageObject().textLayoutBlocks.get(0);
                    this.textLayoutBlock = textLayoutBlock;
                    staticLayout = textLayoutBlock.textLayout;
                    if (Math.abs(ColorUtils.calculateLuminance(getThemedColor("chat_messageTextOut")) - ColorUtils.calculateLuminance(getThemedColor("chat_messagePanelText"))) > 0.20000000298023224d) {
                        this.crossfade = true;
                        this.changeColor = true;
                    }
                    this.fromColor = getThemedColor("chat_messagePanelText");
                    this.toColor = getThemedColor("chat_messageTextOut");
                    if (staticLayout.getLineCount() != this.layout.getLineCount()) {
                        lineCount = staticLayout.getLineCount();
                        int i9 = 0;
                        i5 = 0;
                        i6 = 0;
                        while (true) {
                            if (i9 < lineCount) {
                                if (isRtlLine(this.layout, i9)) {
                                    i6++;
                                } else {
                                    i5++;
                                }
                                if (staticLayout.getLineEnd(i9) != this.layout.getLineEnd(i9)) {
                                    this.crossfade = true;
                                } else {
                                    i9++;
                                }
                            }
                        }
                    } else {
                        this.crossfade = true;
                        i5 = 0;
                        i6 = 0;
                    }
                    if (!this.crossfade && i6 > 0 && i5 > 0) {
                        SpannableString spannableString = new SpannableString(charSequence);
                        SpannableString spannableString2 = new SpannableString(charSequence);
                        float f3 = Float.MAX_VALUE;
                        for (i7 = 0; i7 < lineCount; i7++) {
                            if (isRtlLine(this.layout, i7)) {
                                spannableString.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i7), this.layout.getLineEnd(i7), 0);
                                float lineLeft2 = this.layout.getLineLeft(i7);
                                f3 = lineLeft2 < f3 ? lineLeft2 : f3;
                            } else {
                                spannableString2.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i7), this.layout.getLineEnd(i7), 0);
                            }
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            this.layout = StaticLayout.Builder.obtain(spannableString, 0, spannableString.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                            this.rtlLayout = StaticLayout.Builder.obtain(spannableString2, 0, spannableString2.length(), textPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                        } else {
                            TextPaint textPaint2 = textPaint;
                            this.layout = new StaticLayout(spannableString, textPaint2, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.rtlLayout = new StaticLayout(spannableString2, textPaint2, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        }
                    }
                    this.toXOffsetRtl = this.layout.getWidth() - chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getWidth();
                    if (this.drawBitmaps) {
                        this.textLayoutBitmap = Bitmap.createBitmap(this.layout.getWidth(), this.layout.getHeight(), Bitmap.Config.ARGB_8888);
                        this.layout.draw(new Canvas(this.textLayoutBitmap));
                        StaticLayout staticLayout4 = this.rtlLayout;
                        if (staticLayout4 != null) {
                            this.textLayoutBitmapRtl = Bitmap.createBitmap(staticLayout4.getWidth(), this.rtlLayout.getHeight(), Bitmap.Config.ARGB_8888);
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
                    z2 = chatMessageCell.getMessageObject().getReplyMsgId() == 0 && chatMessageCell.replyNameLayout != null;
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
                    staticLayout2 = chatMessageCell.replyNameLayout;
                    if (staticLayout2 != null && staticLayout2.getText().length() > 1 && chatMessageCell.replyNameLayout.getPrimaryHorizontal(0) != 0.0f) {
                        this.replyNameDx = chatMessageCell.replyNameLayout.getWidth() - chatMessageCell.replyNameLayout.getLineWidth(0);
                    }
                    staticLayout3 = chatMessageCell.replyTextLayout;
                    if (staticLayout3 != null && staticLayout3.getText().length() >= 1 && chatMessageCell.replyTextLayout.getPrimaryHorizontal(0) != 0.0f) {
                        this.replyMessageDx = chatMessageCell.replyTextLayout.getWidth() - chatMessageCell.replyTextLayout.getLineWidth(0);
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
                    this.animationIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.animationIndex, null);
                    this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.TextMessageEnterTransition.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            NotificationCenter.getInstance(TextMessageEnterTransition.this.currentAccount).onAnimationFinish(TextMessageEnterTransition.this.animationIndex);
                            messageEnterTransitionContainer.removeTransition(TextMessageEnterTransition.this);
                            chatMessageCell.setEnterTransitionInProgress(false);
                            chatActivityEnterView.setTextTransitionIsRunning(false);
                            chatActivityEnterView.getEditField().setAlpha(1.0f);
                            chatActivity.getReplyNameTextView().setAlpha(1.0f);
                            chatActivity.getReplyObjectTextView().setAlpha(1.0f);
                            AnimatedEmojiSpan.release((View) null, TextMessageEnterTransition.this.animatedEmojiStack);
                        }
                    });
                    if (SharedConfig.getDevicePerformanceClass() != 2 || (currentBackgroundDrawable = chatMessageCell.getCurrentBackgroundDrawable(true)) == null) {
                    }
                    this.fromMessageDrawable = currentBackgroundDrawable.getTransitionDrawable(getThemedColor("chat_messagePanelBackground"));
                    return;
                }
            }
        }
        z = false;
        if (text.length() == charSequence.length()) {
        }
        this.crossfade = true;
        iArr = new int[1];
        CharSequence trim2 = AndroidUtilities.trim(text, iArr);
        if (iArr[0] <= 0) {
        }
        int i82 = i;
        charSequence = AnimatedEmojiSpan.cloneSpans(Emoji.replaceEmoji(text, textPaint.getFontMetricsInt(), dp, false));
        i3 = i82;
        this.scaleFrom = chatActivityEnterView.getEditField().getTextSize() / textPaint.getTextSize();
        lineCount = chatActivityEnterView.getEditField().getLayout().getLineCount();
        int width2 = (int) (chatActivityEnterView.getEditField().getLayout().getWidth() / this.scaleFrom);
        if (Build.VERSION.SDK_INT < 24) {
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(2, (View) null, this.animatedEmojiStack, this.layout);
        float y2 = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
        this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
        this.fromStartY = ((AndroidUtilities.dp(10.0f) + y2) - chatActivityEnterView.getEditField().getScrollY()) + i2;
        this.toXOffset = 0.0f;
        f = Float.MAX_VALUE;
        while (i4 < this.layout.getLineCount()) {
        }
        if (f != Float.MAX_VALUE) {
        }
        this.scaleY = i3 / (this.layout.getHeight() * this.scaleFrom);
        this.drawableFromTop = AndroidUtilities.dp(4.0f) + y2;
        if (this.enterView.isTopViewVisible()) {
        }
        this.drawableFromBottom = y2 + chatActivityEnterView.getEditField().getMeasuredHeight();
        MessageObject.TextLayoutBlock textLayoutBlock2 = chatMessageCell.getMessageObject().textLayoutBlocks.get(0);
        this.textLayoutBlock = textLayoutBlock2;
        staticLayout = textLayoutBlock2.textLayout;
        if (Math.abs(ColorUtils.calculateLuminance(getThemedColor("chat_messageTextOut")) - ColorUtils.calculateLuminance(getThemedColor("chat_messagePanelText"))) > 0.20000000298023224d) {
        }
        this.fromColor = getThemedColor("chat_messagePanelText");
        this.toColor = getThemedColor("chat_messageTextOut");
        if (staticLayout.getLineCount() != this.layout.getLineCount()) {
        }
        if (!this.crossfade) {
            SpannableString spannableString3 = new SpannableString(charSequence);
            SpannableString spannableString22 = new SpannableString(charSequence);
            float f32 = Float.MAX_VALUE;
            while (i7 < lineCount) {
            }
            if (Build.VERSION.SDK_INT < 24) {
            }
        }
        this.toXOffsetRtl = this.layout.getWidth() - chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getWidth();
        if (this.drawBitmaps) {
        }
        if (chatMessageCell.getMessageObject().getReplyMsgId() == 0) {
        }
        this.hasReply = z2;
        if (z2) {
        }
        this.gradientMatrix = new Matrix();
        Paint paint2 = new Paint(1);
        this.gradientPaint = paint2;
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        LinearGradient linearGradient2 = new LinearGradient(0.0f, AndroidUtilities.dp(12.0f), 0.0f, 0.0f, 0, -16777216, Shader.TileMode.CLAMP);
        this.gradientShader = linearGradient2;
        this.gradientPaint.setShader(linearGradient2);
        this.messageId = chatMessageCell.getMessageObject().stableId;
        chatActivityEnterView.getEditField().setAlpha(0.0f);
        chatActivityEnterView.setTextTransitionIsRunning(true);
        staticLayout2 = chatMessageCell.replyNameLayout;
        if (staticLayout2 != null) {
            this.replyNameDx = chatMessageCell.replyNameLayout.getWidth() - chatMessageCell.replyNameLayout.getLineWidth(0);
        }
        staticLayout3 = chatMessageCell.replyTextLayout;
        if (staticLayout3 != null) {
            this.replyMessageDx = chatMessageCell.replyTextLayout.getWidth() - chatMessageCell.replyTextLayout.getLineWidth(0);
        }
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat2;
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.TextMessageEnterTransition$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextMessageEnterTransition.this.lambda$new$0(chatActivityEnterView, messageEnterTransitionContainer, valueAnimator);
            }
        });
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.setDuration(250L);
        messageEnterTransitionContainer.addTransition(this);
        this.animationIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.animationIndex, null);
        this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.TextMessageEnterTransition.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                NotificationCenter.getInstance(TextMessageEnterTransition.this.currentAccount).onAnimationFinish(TextMessageEnterTransition.this.animationIndex);
                messageEnterTransitionContainer.removeTransition(TextMessageEnterTransition.this);
                chatMessageCell.setEnterTransitionInProgress(false);
                chatActivityEnterView.setTextTransitionIsRunning(false);
                chatActivityEnterView.getEditField().setAlpha(1.0f);
                chatActivity.getReplyNameTextView().setAlpha(1.0f);
                chatActivity.getReplyObjectTextView().setAlpha(1.0f);
                AnimatedEmojiSpan.release((View) null, TextMessageEnterTransition.this.animatedEmojiStack);
            }
        });
        if (SharedConfig.getDevicePerformanceClass() != 2) {
        }
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

    /* JADX WARN: Removed duplicated region for block: B:160:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0373  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0418  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x045d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0473  */
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
        boolean z;
        int i2;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        int themedColor;
        int themedColor2;
        int themedColor3;
        Drawable drawable;
        if (this.drawBitmaps && !this.initBitmaps && this.crossfadeTextBitmap != null && this.messageView.getTransitionParams().wasDraw) {
            this.initBitmaps = true;
            Canvas canvas2 = new Canvas(this.crossfadeTextBitmap);
            canvas2.translate(0.0f, this.crossfadeTextOffset);
            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.messageView.animatedEmojiStack;
            if (emojiGroupedSpans != null) {
                emojiGroupedSpans.clearPositions();
            }
            ChatMessageCell chatMessageCell = this.messageView;
            chatMessageCell.drawMessageText(canvas2, chatMessageCell.getMessageObject().textLayoutBlocks, true, 1.0f, true);
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
        float f16 = this.progress;
        float f17 = f16 > 0.4f ? 1.0f : f16 / 0.4f;
        float interpolation2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f16));
        float f18 = x2 + this.textX;
        float f19 = top + this.textY;
        float f20 = 1.0f - interpolation2;
        int measuredHeight = (int) ((this.container.getMeasuredHeight() * f20) + (y * interpolation2));
        boolean z2 = (this.messageView.getBottom() - AndroidUtilities.dp(4.0f) > this.listView.getMeasuredHeight()) && (((float) this.messageView.getMeasuredHeight()) + top) - ((float) AndroidUtilities.dp(8.0f)) > ((float) measuredHeight) && this.container.getMeasuredHeight() > 0;
        if (z2) {
            i = measuredHeight;
            f = f17;
            f2 = interpolation;
            canvas.saveLayerAlpha(0.0f, Math.max(0.0f, top), this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), 255, 31);
        } else {
            i = measuredHeight;
            f = f17;
            f2 = interpolation;
        }
        canvas.save();
        canvas.clipRect(0.0f, ((this.listView.getY() + this.chatActivity.getChatListViewPadding()) - this.container.getY()) - AndroidUtilities.dp(3.0f), this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        canvas.save();
        float backgroundDrawableLeft = this.messageView.getBackgroundDrawableLeft() + x2 + ((x - (f18 - this.toXOffset)) * f20);
        float backgroundDrawableTop = this.messageView.getBackgroundDrawableTop() + top;
        float f21 = 1.0f - f2;
        float y3 = ((this.drawableFromTop - this.container.getY()) * f21) + (backgroundDrawableTop * f2);
        float y4 = ((this.drawableFromBottom - this.container.getY()) * f21) + ((backgroundDrawableTop + (this.messageView.getBackgroundDrawableBottom() - this.messageView.getBackgroundDrawableTop())) * f2);
        int backgroundDrawableRight = (int) (this.messageView.getBackgroundDrawableRight() + x2 + (AndroidUtilities.dp(4.0f) * f20));
        Theme.MessageDrawable messageDrawable = null;
        if (!this.currentMessageObject.isAnimatedEmojiStickers()) {
            messageDrawable = this.messageView.getCurrentBackgroundDrawable(true);
        }
        if (messageDrawable != null) {
            this.messageView.setBackgroundTopY(this.container.getTop() - this.listView.getTop());
            Drawable shadowDrawable = messageDrawable.getShadowDrawable();
            f5 = f;
            if (f5 == 1.0f || (drawable = this.fromMessageDrawable) == null) {
                f3 = f2;
                f4 = x;
            } else {
                f4 = x;
                f3 = f2;
                drawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                this.fromMessageDrawable.draw(canvas);
            }
            if (shadowDrawable != null) {
                shadowDrawable.setAlpha((int) (interpolation2 * 255.0f));
                shadowDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                shadowDrawable.draw(canvas);
                shadowDrawable.setAlpha(255);
            }
            messageDrawable.setAlpha((int) (f5 * 255.0f));
            messageDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
            messageDrawable.setDrawFullBubble(true);
            messageDrawable.draw(canvas);
            z = false;
            messageDrawable.setDrawFullBubble(false);
            messageDrawable.setAlpha(255);
        } else {
            f3 = f2;
            f4 = x;
            f5 = f;
            z = false;
        }
        canvas.restore();
        canvas.save();
        if (messageDrawable != null) {
            if (this.currentMessageObject.isOutOwner()) {
                canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(10.0f), y4 - AndroidUtilities.dp(4.0f));
            } else {
                canvas.clipRect(AndroidUtilities.dp(4.0f) + backgroundDrawableLeft, AndroidUtilities.dp(4.0f) + y3, backgroundDrawableRight - AndroidUtilities.dp(4.0f), y4 - AndroidUtilities.dp(4.0f));
            }
        }
        float f22 = top + ((y2 - f19) * f21);
        canvas.translate((this.messageView.getLeft() + this.listView.getX()) - this.container.getX(), f22);
        this.messageView.drawTime(canvas, f5, z);
        this.messageView.drawNamesLayout(canvas, f5);
        this.messageView.drawCommentButton(canvas, f5);
        this.messageView.drawCaptionLayout(canvas, z, f5);
        this.messageView.drawLinkPreview(canvas, f5);
        canvas.restore();
        if (this.hasReply) {
            this.chatActivity.getReplyNameTextView().setAlpha(0.0f);
            this.chatActivity.getReplyObjectTextView().setAlpha(0.0f);
            float x3 = this.replyFromStartX - this.container.getX();
            float y5 = this.replyFromStartY - this.container.getY();
            ChatMessageCell chatMessageCell2 = this.messageView;
            float f23 = x2 + chatMessageCell2.replyStartX;
            float f24 = top + chatMessageCell2.replyStartY;
            if (this.currentMessageObject.hasValidReplyMessageObject()) {
                MessageObject messageObject = this.currentMessageObject.replyMessageObject;
                if (messageObject.type == 0 || !TextUtils.isEmpty(messageObject.caption)) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = this.currentMessageObject.replyMessageObject.messageOwner.media;
                    if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) && !(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice)) {
                        themedColor = getThemedColor("chat_outReplyMessageText");
                        if (!this.currentMessageObject.isOutOwner()) {
                            themedColor2 = getThemedColor("chat_outReplyNameText");
                            themedColor3 = getThemedColor("chat_outReplyLine");
                        } else {
                            themedColor2 = getThemedColor("chat_inReplyNameText");
                            themedColor3 = getThemedColor("chat_inReplyLine");
                        }
                        i2 = backgroundDrawableRight;
                        f6 = y4;
                        float f25 = f3;
                        Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor, f25));
                        Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor2, f25));
                        if (this.messageView.needReplyImage) {
                            x3 -= AndroidUtilities.dp(44.0f);
                        }
                        float f26 = x3;
                        float f27 = f26 * f20;
                        float f28 = f27 + (f23 * interpolation2);
                        float dp = (f24 * f25) + ((y5 + (AndroidUtilities.dp(12.0f) * f25)) * f21);
                        Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(themedColor3, (int) (Color.alpha(themedColor3) * interpolation2)));
                        f7 = y3;
                        canvas.drawRect(f28, dp, f28 + AndroidUtilities.dp(2.0f), dp + AndroidUtilities.dp(35.0f), Theme.chat_replyLinePaint);
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(10.0f) * interpolation2, 0.0f);
                        if (this.messageView.needReplyImage) {
                            canvas.save();
                            this.messageView.replyImageReceiver.setImageCoords(f28, dp, AndroidUtilities.dp(35.0f), AndroidUtilities.dp(35.0f));
                            this.messageView.replyImageReceiver.draw(canvas);
                            canvas.translate(f28, dp);
                            canvas.restore();
                            canvas.translate(AndroidUtilities.dp(44.0f), 0.0f);
                        }
                        float f29 = this.replyMessageDx;
                        float f30 = ((f26 - f29) * f20) + ((f23 - f29) * interpolation2);
                        float f31 = f27 + ((f23 - this.replyNameDx) * interpolation2);
                        if (this.messageView.replyNameLayout != null) {
                            canvas.save();
                            canvas.translate(f31, dp);
                            this.messageView.replyNameLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (this.messageView.replyTextLayout == null) {
                            canvas.save();
                            canvas.translate(f30, dp + AndroidUtilities.dp(19.0f));
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.messageView.replySpoilers);
                            ChatMessageCell chatMessageCell3 = this.messageView;
                            f9 = f25;
                            f8 = backgroundDrawableLeft;
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, chatMessageCell3.replyTextLayout, chatMessageCell3.animatedEmojiReplyStack, 0.0f, chatMessageCell3.replySpoilers, 0.0f, 0.0f, 0.0f, 1.0f);
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
                            f9 = f25;
                            f8 = backgroundDrawableLeft;
                        }
                        canvas.restore();
                    }
                }
            }
            themedColor = getThemedColor("chat_outReplyMediaMessageText");
            if (!this.currentMessageObject.isOutOwner()) {
            }
            i2 = backgroundDrawableRight;
            f6 = y4;
            float f252 = f3;
            Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor, f252));
            Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor2, f252));
            if (this.messageView.needReplyImage) {
            }
            float f262 = x3;
            float f272 = f262 * f20;
            float f282 = f272 + (f23 * interpolation2);
            float dp2 = (f24 * f252) + ((y5 + (AndroidUtilities.dp(12.0f) * f252)) * f21);
            Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(themedColor3, (int) (Color.alpha(themedColor3) * interpolation2)));
            f7 = y3;
            canvas.drawRect(f282, dp2, f282 + AndroidUtilities.dp(2.0f), dp2 + AndroidUtilities.dp(35.0f), Theme.chat_replyLinePaint);
            canvas.save();
            canvas.translate(AndroidUtilities.dp(10.0f) * interpolation2, 0.0f);
            if (this.messageView.needReplyImage) {
            }
            float f292 = this.replyMessageDx;
            float f302 = ((f262 - f292) * f20) + ((f23 - f292) * interpolation2);
            float f312 = f272 + ((f23 - this.replyNameDx) * interpolation2);
            if (this.messageView.replyNameLayout != null) {
            }
            if (this.messageView.replyTextLayout == null) {
            }
            canvas.restore();
        } else {
            i2 = backgroundDrawableRight;
            f6 = y4;
            f7 = y3;
            f8 = backgroundDrawableLeft;
            f9 = f3;
        }
        canvas.save();
        if (this.messageView.getMessageObject() == null || this.messageView.getMessageObject().type != 19) {
            canvas.clipRect(f8 + AndroidUtilities.dp(4.0f), f7 + AndroidUtilities.dp(4.0f), i2 - AndroidUtilities.dp(4.0f), f6 - AndroidUtilities.dp(4.0f));
        }
        float f32 = interpolation2 + (this.scaleFrom * f20);
        float f33 = this.drawBitmaps ? interpolation2 + (this.scaleY * f20) : 1.0f;
        canvas.save();
        float f34 = f4 * f20;
        float f35 = y2 * f21;
        canvas.translate(f34 + ((f18 - this.toXOffset) * interpolation2), ((f19 + this.textLayoutBlock.textYOffset) * f9) + f35);
        float f36 = f32 * f33;
        canvas.scale(f32, f36, 0.0f, 0.0f);
        if (this.drawBitmaps) {
            if (this.crossfade) {
                this.bitmapPaint.setAlpha((int) ((1.0f - f5) * 255.0f));
            }
            canvas.drawBitmap(this.textLayoutBitmap, 0.0f, 0.0f, this.bitmapPaint);
            f12 = f5;
            f10 = f22;
            f11 = f36;
        } else {
            boolean z3 = this.crossfade;
            if (!z3 || !this.changeColor) {
                f10 = f22;
                f11 = f36;
                if (z3) {
                    int alpha = Theme.chat_msgTextPaint.getAlpha();
                    float f37 = 1.0f - f5;
                    Theme.chat_msgTextPaint.setAlpha((int) (alpha * f37));
                    this.layout.draw(canvas);
                    f12 = f5;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f37);
                    Theme.chat_msgTextPaint.setAlpha(alpha);
                } else {
                    f12 = f5;
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                }
            } else {
                int color = this.layout.getPaint().getColor();
                float f38 = 1.0f - f5;
                this.layout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f5), (int) (Color.alpha(color) * f38)));
                this.layout.draw(canvas);
                f10 = f22;
                f11 = f36;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f38);
                this.layout.getPaint().setColor(color);
                f12 = f5;
            }
        }
        canvas.restore();
        if (this.rtlLayout != null) {
            canvas.save();
            canvas.translate(f34 + ((f18 - this.toXOffsetRtl) * interpolation2), f35 + ((f19 + this.textLayoutBlock.textYOffset) * f9));
            canvas.scale(f32, f11, 0.0f, 0.0f);
            if (this.drawBitmaps) {
                if (this.crossfade) {
                    this.bitmapPaint.setAlpha((int) ((1.0f - f12) * 255.0f));
                }
                canvas.drawBitmap(this.textLayoutBitmapRtl, 0.0f, 0.0f, this.bitmapPaint);
                f13 = f12;
            } else {
                boolean z4 = this.crossfade;
                if (!z4 || !this.changeColor) {
                    f13 = f12;
                    if (z4) {
                        int alpha2 = this.rtlLayout.getPaint().getAlpha();
                        this.rtlLayout.getPaint().setAlpha((int) (alpha2 * (1.0f - f13)));
                        this.rtlLayout.draw(canvas);
                        this.rtlLayout.getPaint().setAlpha(alpha2);
                    } else {
                        this.rtlLayout.draw(canvas);
                    }
                } else {
                    int color2 = this.rtlLayout.getPaint().getColor();
                    f13 = f12;
                    this.rtlLayout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f13), (int) (Color.alpha(color2) * (1.0f - f13))));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setColor(color2);
                }
            }
            canvas.restore();
        } else {
            f13 = f12;
        }
        if (this.crossfade) {
            canvas.save();
            canvas.translate(((this.messageView.getLeft() + this.listView.getX()) - this.container.getX()) + ((f4 - f18) * f20), f10);
            canvas.scale(f32, f11, this.messageView.getTextX(), this.messageView.getTextY());
            canvas.translate(0.0f, -this.crossfadeTextOffset);
            if (this.crossfadeTextBitmap != null) {
                this.bitmapPaint.setAlpha((int) (f13 * 255.0f));
                canvas.drawBitmap(this.crossfadeTextBitmap, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                int color3 = Theme.chat_msgTextPaint.getColor();
                Theme.chat_msgTextPaint.setColor(this.toColor);
                ChatMessageCell chatMessageCell4 = this.messageView;
                chatMessageCell4.drawMessageText(canvas, chatMessageCell4.getMessageObject().textLayoutBlocks, false, f13, true);
                this.messageView.drawAnimatedEmojis(canvas, f13);
                if (Theme.chat_msgTextPaint.getColor() != color3) {
                    Theme.chat_msgTextPaint.setColor(color3);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        if (z2) {
            float f39 = i;
            this.gradientMatrix.setTranslate(0.0f, f39);
            this.gradientShader.setLocalMatrix(this.gradientMatrix);
            canvas.drawRect(0.0f, f39, this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), this.gradientPaint);
            canvas.restore();
        }
        float f40 = this.progress;
        if (f40 > 0.4f) {
            f15 = 1.0f;
            f14 = 1.0f;
        } else {
            f14 = f40 / 0.4f;
            f15 = 1.0f;
        }
        if (f14 == f15) {
            this.enterView.setTextTransitionIsRunning(false);
        }
        if (this.enterView.getSendButton().getVisibility() != 0 || f14 >= f15) {
            return;
        }
        canvas.save();
        canvas.translate(((((this.enterView.getX() + this.enterView.getSendButton().getX()) + ((View) this.enterView.getSendButton().getParent()).getX()) + ((View) this.enterView.getSendButton().getParent().getParent()).getX()) - this.container.getX()) + (AndroidUtilities.dp(52.0f) * f14), (((this.enterView.getY() + this.enterView.getSendButton().getY()) + ((View) this.enterView.getSendButton().getParent()).getY()) + ((View) this.enterView.getSendButton().getParent().getParent()).getY()) - this.container.getY());
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
