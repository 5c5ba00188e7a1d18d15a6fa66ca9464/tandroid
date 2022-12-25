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
    /* JADX WARN: Code restructure failed: missing block: B:118:0x04c6, code lost:
        r24.drawBitmaps = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x044a A[Catch: Exception -> 0x04c6, TryCatch #0 {Exception -> 0x04c6, blocks: (B:106:0x0446, B:108:0x044a, B:110:0x046e, B:111:0x048c, B:113:0x0490, B:115:0x049a, B:116:0x04ae), top: B:148:0x0446 }] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x04de  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0318  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0335  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0363  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0380  */
    @SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextMessageEnterTransition(final ChatMessageCell chatMessageCell, final ChatActivity chatActivity, RecyclerListView recyclerListView, final MessageEnterTransitionContainer messageEnterTransitionContainer, Theme.ResourcesProvider resourcesProvider) {
        boolean z;
        int[] iArr;
        int i;
        int lineCount;
        int i2;
        float f;
        StaticLayout staticLayout;
        int i3;
        int i4;
        boolean z2;
        StaticLayout staticLayout2;
        StaticLayout staticLayout3;
        Theme.MessageDrawable currentBackgroundDrawable;
        int i5;
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
                            i = chatActivityEnterView.getEditField().getLayout().getLineTop(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0]));
                            height = chatActivityEnterView.getEditField().getLayout().getLineBottom(chatActivityEnterView.getEditField().getLayout().getLineForOffset(iArr[0] + trim.length())) - i;
                        } else {
                            i = 0;
                        }
                        AnimatedEmojiSpan.cloneSpans(charSequence);
                        charSequence = Emoji.replaceEmoji(text, textPaint.getFontMetricsInt(), dp, false);
                    } else {
                        i = 0;
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
                    this.fromStartY = ((AndroidUtilities.dp(10.0f) + y) - chatActivityEnterView.getEditField().getScrollY()) + i;
                    this.toXOffset = 0.0f;
                    f = Float.MAX_VALUE;
                    for (i2 = 0; i2 < this.layout.getLineCount(); i2++) {
                        float lineLeft = this.layout.getLineLeft(i2);
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
                    staticLayout = textLayoutBlock.textLayout;
                    if (Math.abs(ColorUtils.calculateLuminance(getThemedColor("chat_messageTextOut")) - ColorUtils.calculateLuminance(getThemedColor("chat_messagePanelText"))) > 0.20000000298023224d) {
                        this.crossfade = true;
                        this.changeColor = true;
                    }
                    this.fromColor = getThemedColor("chat_messagePanelText");
                    this.toColor = getThemedColor("chat_messageTextOut");
                    if (staticLayout.getLineCount() != this.layout.getLineCount()) {
                        lineCount = staticLayout.getLineCount();
                        int i6 = 0;
                        i3 = 0;
                        i4 = 0;
                        while (true) {
                            if (i6 < lineCount) {
                                if (isRtlLine(this.layout, i6)) {
                                    i4++;
                                } else {
                                    i3++;
                                }
                                if (staticLayout.getLineEnd(i6) != this.layout.getLineEnd(i6)) {
                                    this.crossfade = true;
                                } else {
                                    i6++;
                                }
                            }
                        }
                    } else {
                        this.crossfade = true;
                        i3 = 0;
                        i4 = 0;
                    }
                    if (!this.crossfade && i4 > 0 && i3 > 0) {
                        SpannableString spannableString = new SpannableString(charSequence);
                        SpannableString spannableString2 = new SpannableString(charSequence);
                        float f3 = Float.MAX_VALUE;
                        for (i5 = 0; i5 < lineCount; i5++) {
                            if (isRtlLine(this.layout, i5)) {
                                spannableString.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i5), this.layout.getLineEnd(i5), 0);
                                float lineLeft2 = this.layout.getLineLeft(i5);
                                f3 = lineLeft2 < f3 ? lineLeft2 : f3;
                            } else {
                                spannableString2.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i5), this.layout.getLineEnd(i5), 0);
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
                            chatMessageCell.getTransitionParams().lastDrawingBackgroundRect.set(chatMessageCell.getBackgroundDrawableLeft(), chatMessageCell.getBackgroundDrawableTop(), chatMessageCell.getBackgroundDrawableRight(), chatMessageCell.getBackgroundDrawableBottom());
                            chatActivityEnterView.setTextTransitionIsRunning(false);
                            chatActivityEnterView.getEditField().setAlpha(1.0f);
                            chatActivity.getReplyNameTextView().setAlpha(1.0f);
                            chatActivity.getReplyObjectTextView().setAlpha(1.0f);
                            AnimatedEmojiSpan.release((View) null, TextMessageEnterTransition.this.animatedEmojiStack);
                        }
                    });
                    if (SharedConfig.getDevicePerformanceClass() == 2 || (currentBackgroundDrawable = chatMessageCell.getCurrentBackgroundDrawable(true)) == null) {
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
        AnimatedEmojiSpan.cloneSpans(charSequence);
        charSequence = Emoji.replaceEmoji(text, textPaint.getFontMetricsInt(), dp, false);
        this.scaleFrom = chatActivityEnterView.getEditField().getTextSize() / textPaint.getTextSize();
        lineCount = chatActivityEnterView.getEditField().getLayout().getLineCount();
        int width2 = (int) (chatActivityEnterView.getEditField().getLayout().getWidth() / this.scaleFrom);
        if (Build.VERSION.SDK_INT < 24) {
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(2, (View) null, this.animatedEmojiStack, this.layout);
        float y2 = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
        this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
        this.fromStartY = ((AndroidUtilities.dp(10.0f) + y2) - chatActivityEnterView.getEditField().getScrollY()) + i;
        this.toXOffset = 0.0f;
        f = Float.MAX_VALUE;
        while (i2 < this.layout.getLineCount()) {
        }
        if (f != Float.MAX_VALUE) {
        }
        this.scaleY = height / (this.layout.getHeight() * this.scaleFrom);
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
            while (i5 < lineCount) {
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
                chatMessageCell.getTransitionParams().lastDrawingBackgroundRect.set(chatMessageCell.getBackgroundDrawableLeft(), chatMessageCell.getBackgroundDrawableTop(), chatMessageCell.getBackgroundDrawableRight(), chatMessageCell.getBackgroundDrawableBottom());
                chatActivityEnterView.setTextTransitionIsRunning(false);
                chatActivityEnterView.getEditField().setAlpha(1.0f);
                chatActivity.getReplyNameTextView().setAlpha(1.0f);
                chatActivity.getReplyObjectTextView().setAlpha(1.0f);
                AnimatedEmojiSpan.release((View) null, TextMessageEnterTransition.this.animatedEmojiStack);
            }
        });
        if (SharedConfig.getDevicePerformanceClass() == 2) {
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

    /* JADX WARN: Removed duplicated region for block: B:73:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x04d2  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0550  */
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
            f = interpolation2;
            f2 = f17;
            f3 = interpolation;
            canvas.saveLayerAlpha(0.0f, Math.max(0.0f, top), this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), 255, 31);
        } else {
            i = measuredHeight;
            f = interpolation2;
            f2 = f17;
            f3 = interpolation;
        }
        canvas.save();
        canvas.clipRect(0.0f, ((this.listView.getY() + this.chatActivity.getChatListViewPadding()) - this.container.getY()) - AndroidUtilities.dp(3.0f), this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        canvas.save();
        float backgroundDrawableLeft = this.messageView.getBackgroundDrawableLeft() + x2 + ((x - (f18 - this.toXOffset)) * f20);
        float backgroundDrawableTop = this.messageView.getBackgroundDrawableTop() + top;
        float f21 = 1.0f - f3;
        float y3 = ((this.drawableFromTop - this.container.getY()) * f21) + (backgroundDrawableTop * f3);
        float y4 = ((this.drawableFromBottom - this.container.getY()) * f21) + ((backgroundDrawableTop + (this.messageView.getBackgroundDrawableBottom() - this.messageView.getBackgroundDrawableTop())) * f3);
        int backgroundDrawableRight = (int) (this.messageView.getBackgroundDrawableRight() + x2 + (AndroidUtilities.dp(4.0f) * f20));
        Theme.MessageDrawable currentBackgroundDrawable = this.currentMessageObject.isAnimatedEmojiStickers() ? null : this.messageView.getCurrentBackgroundDrawable(true);
        if (currentBackgroundDrawable != null) {
            this.messageView.setBackgroundTopY(this.container.getTop() - this.listView.getTop());
            Drawable shadowDrawable = currentBackgroundDrawable.getShadowDrawable();
            f6 = f2;
            if (f6 == 1.0f || (drawable = this.fromMessageDrawable) == null) {
                f4 = f3;
                f7 = x;
            } else {
                f7 = x;
                f4 = f3;
                drawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                this.fromMessageDrawable.draw(canvas);
            }
            f5 = f;
            if (shadowDrawable != null) {
                shadowDrawable.setAlpha((int) (f5 * 255.0f));
                shadowDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
                shadowDrawable.draw(canvas);
                shadowDrawable.setAlpha(255);
            }
            currentBackgroundDrawable.setAlpha((int) (f6 * 255.0f));
            currentBackgroundDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
            currentBackgroundDrawable.setDrawFullBubble(true);
            currentBackgroundDrawable.draw(canvas);
            z = false;
            currentBackgroundDrawable.setDrawFullBubble(false);
            currentBackgroundDrawable.setAlpha(255);
        } else {
            f4 = f3;
            f5 = f;
            f6 = f2;
            f7 = x;
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
        float f22 = top + ((y2 - f19) * f21);
        canvas.translate((this.messageView.getLeft() + this.listView.getX()) - this.container.getX(), f22);
        this.messageView.drawTime(canvas, f6, z);
        this.messageView.drawNamesLayout(canvas, f6);
        this.messageView.drawCommentButton(canvas, f6);
        this.messageView.drawCaptionLayout(canvas, z, f6);
        this.messageView.drawLinkPreview(canvas, f6);
        canvas.restore();
        if (this.hasReply) {
            this.chatActivity.getReplyNameTextView().setAlpha(0.0f);
            this.chatActivity.getReplyObjectTextView().setAlpha(0.0f);
            float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), AndroidUtilities.dp(7.0f) + Theme.chat_replyNamePaint.getTextSize() + Theme.chat_replyTextPaint.getTextSize(), f5);
            int min = (int) Math.min(AndroidUtilities.dp(10.0f), ((lerp - AndroidUtilities.dp(35.0f)) / 1.5f) + AndroidUtilities.dp(10.0f));
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
                        int i3 = themedColor2;
                        int i4 = themedColor3;
                        float f25 = f4;
                        Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor, f25));
                        Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, i3, f25));
                        if (this.messageView.needReplyImage) {
                            x3 -= AndroidUtilities.dp(44.0f);
                        }
                        float f26 = x3;
                        float f27 = f26 * f20;
                        float f28 = f27 + (f23 * f5);
                        float dp = (f24 * f25) + ((y5 + (AndroidUtilities.dp(12.0f) * f25)) * f21);
                        Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(i4, (int) (Color.alpha(i4) * f5)));
                        i2 = backgroundDrawableRight;
                        f8 = y4;
                        f9 = y3;
                        canvas.drawRect(f28, dp, f28 + AndroidUtilities.dp(2.0f), dp + AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), this.messageView.replyHeight, f5), Theme.chat_replyLinePaint);
                        canvas.save();
                        canvas.translate(min * f5, 0.0f);
                        if (this.messageView.needReplyImage) {
                            canvas.save();
                            this.messageView.replyImageReceiver.setImageCoords(f28, dp, lerp, lerp);
                            this.messageView.replyImageReceiver.draw(canvas);
                            canvas.translate(f28, dp);
                            canvas.restore();
                            canvas.translate((min - AndroidUtilities.dp(1.0f)) + lerp, 0.0f);
                        }
                        float f29 = this.replyMessageDx;
                        float f30 = ((f26 - f29) * f20) + ((f23 - f29) * f5);
                        float f31 = f27 + ((f23 - this.replyNameDx) * f5);
                        if (this.messageView.replyNameLayout != null) {
                            canvas.save();
                            canvas.translate(f31, dp);
                            this.messageView.replyNameLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (this.messageView.replyTextLayout == null) {
                            canvas.save();
                            canvas.translate(f30, dp + AndroidUtilities.lerp(AndroidUtilities.dp(19.0f), Theme.chat_replyNamePaint.getTextSize() + AndroidUtilities.dp(5.0f), f5));
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.messageView.replySpoilers);
                            ChatMessageCell chatMessageCell3 = this.messageView;
                            f12 = f25;
                            f10 = backgroundDrawableLeft;
                            f11 = f22;
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
                            f12 = f25;
                            f11 = f22;
                            f10 = backgroundDrawableLeft;
                        }
                        canvas.restore();
                    }
                }
            }
            themedColor = getThemedColor("chat_outReplyMediaMessageText");
            if (!this.currentMessageObject.isOutOwner()) {
            }
            int i32 = themedColor2;
            int i42 = themedColor3;
            float f252 = f4;
            Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor, f252));
            Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, i32, f252));
            if (this.messageView.needReplyImage) {
            }
            float f262 = x3;
            float f272 = f262 * f20;
            float f282 = f272 + (f23 * f5);
            float dp2 = (f24 * f252) + ((y5 + (AndroidUtilities.dp(12.0f) * f252)) * f21);
            Theme.chat_replyLinePaint.setColor(ColorUtils.setAlphaComponent(i42, (int) (Color.alpha(i42) * f5)));
            i2 = backgroundDrawableRight;
            f8 = y4;
            f9 = y3;
            canvas.drawRect(f282, dp2, f282 + AndroidUtilities.dp(2.0f), dp2 + AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), this.messageView.replyHeight, f5), Theme.chat_replyLinePaint);
            canvas.save();
            canvas.translate(min * f5, 0.0f);
            if (this.messageView.needReplyImage) {
            }
            float f292 = this.replyMessageDx;
            float f302 = ((f262 - f292) * f20) + ((f23 - f292) * f5);
            float f312 = f272 + ((f23 - this.replyNameDx) * f5);
            if (this.messageView.replyNameLayout != null) {
            }
            if (this.messageView.replyTextLayout == null) {
            }
            canvas.restore();
        } else {
            i2 = backgroundDrawableRight;
            f8 = y4;
            f9 = y3;
            f10 = backgroundDrawableLeft;
            f11 = f22;
            f12 = f4;
        }
        canvas.save();
        if (this.messageView.getMessageObject() == null || this.messageView.getMessageObject().type != 19) {
            canvas.clipRect(f10 + AndroidUtilities.dp(4.0f), f9 + AndroidUtilities.dp(4.0f), i2 - AndroidUtilities.dp(4.0f), f8 - AndroidUtilities.dp(4.0f));
        }
        float f32 = f5 + (this.scaleFrom * f20);
        float f33 = this.drawBitmaps ? (this.scaleY * f20) + f5 : 1.0f;
        canvas.save();
        float f34 = f7 * f20;
        float f35 = y2 * f21;
        canvas.translate(f34 + ((f18 - this.toXOffset) * f5), ((f19 + this.textLayoutBlock.textYOffset) * f12) + f35);
        float f36 = f32 * f33;
        canvas.scale(f32, f36, 0.0f, 0.0f);
        if (this.drawBitmaps) {
            if (this.crossfade) {
                this.bitmapPaint.setAlpha((int) ((1.0f - f6) * 255.0f));
            }
            canvas.drawBitmap(this.textLayoutBitmap, 0.0f, 0.0f, this.bitmapPaint);
            f13 = f32;
            f14 = f36;
        } else {
            boolean z3 = this.crossfade;
            if (z3 && this.changeColor) {
                int color = this.layout.getPaint().getColor();
                this.layout.getPaint().setColor(ColorUtils.blendARGB(this.fromColor, this.toColor, f6));
                float f37 = 1.0f - f6;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f37 * 255.0f), 31);
                this.layout.draw(canvas);
                f13 = f32;
                f14 = f36;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f37);
                this.layout.getPaint().setColor(color);
                canvas.restore();
            } else {
                f13 = f32;
                f14 = f36;
                if (z3) {
                    float f38 = 1.0f - f6;
                    canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f38 * 255.0f), 31);
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f38);
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
            canvas.translate(f34 + ((f18 - this.toXOffsetRtl) * f5), f35 + ((f19 + this.textLayoutBlock.textYOffset) * f12));
            f15 = f13;
            canvas.scale(f15, f14, 0.0f, 0.0f);
            if (this.drawBitmaps) {
                if (this.crossfade) {
                    this.bitmapPaint.setAlpha((int) ((1.0f - f6) * 255.0f));
                }
                canvas.drawBitmap(this.textLayoutBitmapRtl, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                boolean z4 = this.crossfade;
                if (z4 && this.changeColor) {
                    int color2 = this.rtlLayout.getPaint().getColor();
                    this.rtlLayout.getPaint().setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(this.fromColor, this.toColor, f6), (int) (Color.alpha(color2) * (1.0f - f6))));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setColor(color2);
                } else if (z4) {
                    int alpha = this.rtlLayout.getPaint().getAlpha();
                    this.rtlLayout.getPaint().setAlpha((int) (alpha * (1.0f - f6)));
                    this.rtlLayout.draw(canvas);
                    this.rtlLayout.getPaint().setAlpha(alpha);
                } else {
                    this.rtlLayout.draw(canvas);
                }
            }
            canvas.restore();
        } else {
            f15 = f13;
        }
        if (this.crossfade) {
            canvas.save();
            canvas.translate(((this.messageView.getLeft() + this.listView.getX()) - this.container.getX()) + ((f7 - f18) * f20), f11);
            canvas.scale(f15, f14, this.messageView.getTextX(), this.messageView.getTextY());
            canvas.translate(0.0f, -this.crossfadeTextOffset);
            if (this.crossfadeTextBitmap != null) {
                this.bitmapPaint.setAlpha((int) (f6 * 255.0f));
                canvas.drawBitmap(this.crossfadeTextBitmap, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                int color3 = Theme.chat_msgTextPaint.getColor();
                Theme.chat_msgTextPaint.setColor(this.toColor);
                ChatMessageCell chatMessageCell4 = this.messageView;
                chatMessageCell4.drawMessageText(canvas, chatMessageCell4.getMessageObject().textLayoutBlocks, false, f6, true);
                this.messageView.drawAnimatedEmojis(canvas, f6);
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
        float f41 = f40 > 0.4f ? 1.0f : f40 / 0.4f;
        if (f41 == 1.0f) {
            this.enterView.setTextTransitionIsRunning(false);
        }
        if (this.enterView.getSendButton().getVisibility() != 0 || f41 >= 1.0f) {
            return;
        }
        canvas.save();
        canvas.translate(((((this.enterView.getX() + this.enterView.getSendButton().getX()) + ((View) this.enterView.getSendButton().getParent()).getX()) + ((View) this.enterView.getSendButton().getParent().getParent()).getX()) - this.container.getX()) + (AndroidUtilities.dp(52.0f) * f41), (((this.enterView.getY() + this.enterView.getSendButton().getY()) + ((View) this.enterView.getSendButton().getParent()).getY()) + ((View) this.enterView.getSendButton().getParent().getParent()).getY()) - this.container.getY());
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
