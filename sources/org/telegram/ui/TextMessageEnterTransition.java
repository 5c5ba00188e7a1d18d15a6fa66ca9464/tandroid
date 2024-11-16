package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
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

/* loaded from: classes4.dex */
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
    float fromRadius;
    private float fromStartX;
    private float fromStartY;
    private Matrix gradientMatrix;
    private Paint gradientPaint;
    private LinearGradient gradientShader;
    boolean hasReply;
    float lastMessageX;
    float lastMessageY;
    StaticLayout layout;
    RecyclerListView listView;
    private int messageId;
    ChatMessageCell messageView;
    float progress;
    int replayFromColor;
    int replayObjectFromColor;
    float replyFromObjectStartY;
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
    private final int currentAccount = UserConfig.selectedAccount;

    /* JADX WARN: Can't wrap try/catch for region: R(40:15|(1:17)(1:175)|18|(1:20)|21|(6:23|(1:25)(1:52)|26|27|29|(1:31))|53|(1:174)(1:59)|60|(3:169|(1:171)(1:173)|172)(1:63)|64|(1:66)(1:168)|67|68|(4:71|(2:73|74)(1:76)|75|69)|77|78|(1:80)|81|(1:83)|84|(1:86)|87|(17:89|(4:91|(1:93)(1:165)|94|(1:97)(1:96))|166|99|(6:103|(5:105|(2:107|(3:109|110|111)(1:112))(1:115)|113|114|111)|116|117|(1:119)(1:121)|120)|122|123|124|(4:126|(1:128)|129|(4:131|(1:133)(1:137)|134|135))|138|(1:162)(1:142)|143|(1:145)|146|(1:152)|153|(1:160)(2:157|158))(1:167)|98|99|(6:103|(0)|116|117|(0)(0)|120)|122|123|124|(0)|138|(1:140)|162|143|(0)|146|(3:148|150|152)|153|(2:155|160)(1:161)) */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x04f1, code lost:
    
        r25.drawBitmaps = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0402  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x047d A[Catch: Exception -> 0x04be, TryCatch #0 {Exception -> 0x04be, blocks: (B:124:0x0479, B:126:0x047d, B:128:0x04a1, B:129:0x04c0, B:131:0x04c4, B:133:0x04ce, B:134:0x04d9, B:135:0x04ee, B:137:0x04de), top: B:123:0x0479 }] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0508  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextMessageEnterTransition(final ChatMessageCell chatMessageCell, final ChatActivity chatActivity, RecyclerListView recyclerListView, final MessageEnterTransitionContainer messageEnterTransitionContainer, Theme.ResourcesProvider resourcesProvider) {
        int i;
        StaticLayout staticLayout;
        boolean z;
        int i2;
        int i3;
        boolean z2;
        StaticLayout staticLayout2;
        Theme.MessageDrawable currentBackgroundDrawable;
        int measuredWidth;
        int measuredHeight;
        int i4;
        StaticLayout staticLayout3;
        StaticLayout.Builder obtain;
        StaticLayout.Builder breakStrategy;
        StaticLayout.Builder hyphenationFrequency;
        StaticLayout.Builder alignment;
        StaticLayout build;
        StaticLayout.Builder obtain2;
        StaticLayout.Builder breakStrategy2;
        StaticLayout.Builder hyphenationFrequency2;
        StaticLayout.Builder alignment2;
        int i5;
        StaticLayout.Builder obtain3;
        StaticLayout.Builder breakStrategy3;
        StaticLayout.Builder hyphenationFrequency3;
        StaticLayout.Builder alignment3;
        Object[] spans;
        TextPaint textPaint;
        this.drawBitmaps = false;
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
        ChatActivityEnterView.RecordCircle recordCircle = chatActivityEnterView.getRecordCircle();
        this.fromRadius = recordCircle == null ? 0.0f : recordCircle.drawingCircleRadius;
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
        TextPaint textPaint2 = Theme.chat_msgTextPaint;
        int dp = AndroidUtilities.dp(20.0f);
        if (chatMessageCell.getMessageObject().getEmojiOnlyCount() != 0) {
            boolean z3 = chatMessageCell.getMessageObject().emojiOnlyCount == chatMessageCell.getMessageObject().animatedEmojiCount;
            switch (Math.max(chatMessageCell.getMessageObject().emojiOnlyCount, chatMessageCell.getMessageObject().animatedEmojiCount)) {
                case 0:
                case 1:
                case 2:
                    TextPaint[] textPaintArr = Theme.chat_msgTextPaintEmoji;
                    if (z3) {
                        textPaint = textPaintArr[0];
                        break;
                    } else {
                        textPaint = textPaintArr[2];
                        break;
                    }
                case 3:
                    TextPaint[] textPaintArr2 = Theme.chat_msgTextPaintEmoji;
                    if (z3) {
                        textPaint = textPaintArr2[1];
                        break;
                    } else {
                        textPaint = textPaintArr2[3];
                        break;
                    }
                case 4:
                    TextPaint[] textPaintArr3 = Theme.chat_msgTextPaintEmoji;
                    if (z3) {
                        textPaint = textPaintArr3[2];
                        break;
                    } else {
                        textPaint = textPaintArr3[4];
                        break;
                    }
                case 5:
                    TextPaint[] textPaintArr4 = Theme.chat_msgTextPaintEmoji;
                    if (z3) {
                        textPaint = textPaintArr4[3];
                        break;
                    } else {
                        textPaint = textPaintArr4[5];
                        break;
                    }
                case 6:
                    TextPaint[] textPaintArr5 = Theme.chat_msgTextPaintEmoji;
                    if (z3) {
                        textPaint = textPaintArr5[4];
                        break;
                    } else {
                        textPaint = textPaintArr5[5];
                        break;
                    }
                default:
                    textPaint = Theme.chat_msgTextPaintEmoji[5];
                    break;
            }
            textPaint2 = textPaint;
            if (textPaint2 != null) {
                dp = (int) (textPaint2.getTextSize() + AndroidUtilities.dp(4.0f));
            }
        }
        boolean z4 = (charSequence instanceof Spannable) && (spans = ((Spannable) charSequence).getSpans(0, charSequence.length(), Object.class)) != null && spans.length > 0;
        if (editText.length() != charSequence.length() || z4) {
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
            charSequence = Emoji.replaceEmoji((CharSequence) editText, textPaint2.getFontMetricsInt(), dp, false);
        } else {
            i = 0;
        }
        this.scaleFrom = chatActivityEnterView.getEditField().getTextSize() / textPaint2.getTextSize();
        int lineCount = chatActivityEnterView.getEditField().getLayout().getLineCount();
        int width = (int) (chatActivityEnterView.getEditField().getLayout().getWidth() / this.scaleFrom);
        if (Build.VERSION.SDK_INT >= 24) {
            obtain3 = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint2, width);
            breakStrategy3 = obtain3.setBreakStrategy(1);
            hyphenationFrequency3 = breakStrategy3.setHyphenationFrequency(0);
            alignment3 = hyphenationFrequency3.setAlignment(Layout.Alignment.ALIGN_NORMAL);
            staticLayout = alignment3.build();
        } else {
            staticLayout = new StaticLayout(charSequence, textPaint2, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        this.layout = staticLayout;
        this.animatedEmojiStack = AnimatedEmojiSpan.update(2, (View) null, this.animatedEmojiStack, this.layout);
        float y = chatActivityEnterView.getY() + chatActivityEnterView.getEditField().getY() + ((View) chatActivityEnterView.getEditField().getParent()).getY() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getY();
        this.fromStartX = chatActivityEnterView.getX() + chatActivityEnterView.getEditField().getX() + ((View) chatActivityEnterView.getEditField().getParent()).getX() + ((View) chatActivityEnterView.getEditField().getParent().getParent()).getX();
        this.fromStartY = ((AndroidUtilities.dp(10.0f) + y) - chatActivityEnterView.getEditField().getScrollY()) + i;
        this.toXOffset = 0.0f;
        float f = Float.MAX_VALUE;
        for (int i6 = 0; i6 < this.layout.getLineCount(); i6++) {
            float lineLeft = this.layout.getLineLeft(i6);
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
        StaticLayout staticLayout4 = textLayoutBlock.textLayout;
        int i7 = Theme.key_chat_messageTextOut;
        double calculateLuminance = ColorUtils.calculateLuminance(getThemedColor(i7));
        int i8 = Theme.key_chat_messagePanelText;
        if (Math.abs(calculateLuminance - ColorUtils.calculateLuminance(getThemedColor(i8))) > 0.20000000298023224d) {
            this.crossfade = true;
            this.changeColor = true;
        }
        this.fromColor = getThemedColor(i8);
        this.toColor = getThemedColor(i7);
        if (staticLayout4.getLineCount() == this.layout.getLineCount()) {
            lineCount = staticLayout4.getLineCount();
            i2 = 0;
            i3 = 0;
            for (int i9 = 0; i9 < lineCount; i9 += z ? 1 : 0) {
                if (isRtlLine(this.layout, i9)) {
                    z = true;
                    i2++;
                } else {
                    z = true;
                    i3++;
                }
                if (staticLayout4.getLineEnd(i9) == this.layout.getLineEnd(i9)) {
                }
            }
            if (!this.crossfade && i2 > 0 && i3 > 0) {
                SpannableString spannableString = new SpannableString(charSequence);
                SpannableString spannableString2 = new SpannableString(charSequence);
                float f2 = Float.MAX_VALUE;
                i4 = 0;
                while (i4 < lineCount) {
                    if (isRtlLine(this.layout, i4)) {
                        spannableString.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i4), this.layout.getLineEnd(i4), 0);
                        float lineLeft2 = this.layout.getLineLeft(i4);
                        if (lineLeft2 < f2) {
                            f2 = lineLeft2;
                            i5 = 1;
                            i4 += i5;
                        }
                    } else {
                        spannableString2.setSpan(new EmptyStubSpan(), this.layout.getLineStart(i4), this.layout.getLineEnd(i4), 0);
                    }
                    i5 = 1;
                    i4 += i5;
                }
                if (Build.VERSION.SDK_INT < 24) {
                    obtain = StaticLayout.Builder.obtain(spannableString, 0, spannableString.length(), textPaint2, width);
                    breakStrategy = obtain.setBreakStrategy(1);
                    hyphenationFrequency = breakStrategy.setHyphenationFrequency(0);
                    Layout.Alignment alignment4 = Layout.Alignment.ALIGN_NORMAL;
                    alignment = hyphenationFrequency.setAlignment(alignment4);
                    build = alignment.build();
                    this.layout = build;
                    obtain2 = StaticLayout.Builder.obtain(spannableString2, 0, spannableString2.length(), textPaint2, width);
                    breakStrategy2 = obtain2.setBreakStrategy(1);
                    hyphenationFrequency2 = breakStrategy2.setHyphenationFrequency(0);
                    alignment2 = hyphenationFrequency2.setAlignment(alignment4);
                    staticLayout3 = alignment2.build();
                } else {
                    Layout.Alignment alignment5 = Layout.Alignment.ALIGN_NORMAL;
                    TextPaint textPaint3 = textPaint2;
                    this.layout = new StaticLayout(spannableString, textPaint3, width, alignment5, 1.0f, 0.0f, false);
                    staticLayout3 = new StaticLayout(spannableString2, textPaint3, width, alignment5, 1.0f, 0.0f, false);
                }
                this.rtlLayout = staticLayout3;
            }
            this.toXOffsetRtl = this.layout.getWidth() - chatMessageCell.getMessageObject().textLayoutBlocks.get(0).textLayout.getWidth();
            if (this.drawBitmaps) {
                int width2 = this.layout.getWidth();
                int height2 = this.layout.getHeight();
                Bitmap.Config config = Bitmap.Config.ARGB_8888;
                this.textLayoutBitmap = Bitmap.createBitmap(width2, height2, config);
                this.layout.draw(new Canvas(this.textLayoutBitmap));
                StaticLayout staticLayout5 = this.rtlLayout;
                if (staticLayout5 != null) {
                    this.textLayoutBitmapRtl = Bitmap.createBitmap(staticLayout5.getWidth(), this.rtlLayout.getHeight(), config);
                    this.rtlLayout.draw(new Canvas(this.textLayoutBitmapRtl));
                }
                if (this.crossfade) {
                    if (chatMessageCell.getMeasuredHeight() < recyclerListView.getMeasuredHeight()) {
                        this.crossfadeTextOffset = 0.0f;
                        measuredWidth = chatMessageCell.getMeasuredWidth();
                        measuredHeight = chatMessageCell.getMeasuredHeight();
                    } else {
                        this.crossfadeTextOffset = chatMessageCell.getTop();
                        measuredWidth = chatMessageCell.getMeasuredWidth();
                        measuredHeight = recyclerListView.getMeasuredHeight();
                    }
                    this.crossfadeTextBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, config);
                }
            }
            z2 = chatMessageCell.getMessageObject().getReplyMsgId() == 0 && chatMessageCell.replyNameLayout != null;
            this.hasReply = z2;
            if (z2) {
                SimpleTextView replyNameTextView = chatActivity.getReplyNameTextView();
                this.replyFromStartX = replyNameTextView.getX() + ((View) replyNameTextView.getParent()).getX();
                this.replyFromStartWidth = ((View) replyNameTextView.getParent()).getWidth();
                this.replyFromStartY = replyNameTextView.getY() + ((View) replyNameTextView.getParent().getParent()).getY() + ((View) replyNameTextView.getParent().getParent().getParent()).getY();
                SimpleTextView replyObjectTextView = chatActivity.getReplyObjectTextView();
                this.replyFromObjectStartY = replyObjectTextView.getY() + ((View) replyObjectTextView.getParent().getParent()).getY() + ((View) replyObjectTextView.getParent().getParent().getParent()).getY();
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
            if (SharedConfig.getDevicePerformanceClass() == 2 || (currentBackgroundDrawable = chatMessageCell.getCurrentBackgroundDrawable(true)) == null) {
            }
            this.fromMessageDrawable = currentBackgroundDrawable.getTransitionDrawable(getThemedColor(Theme.key_chat_messagePanelBackground));
            return;
        }
        z = true;
        i2 = 0;
        i3 = 0;
        this.crossfade = z;
        if (!this.crossfade) {
            SpannableString spannableString3 = new SpannableString(charSequence);
            SpannableString spannableString22 = new SpannableString(charSequence);
            float f22 = Float.MAX_VALUE;
            i4 = 0;
            while (i4 < lineCount) {
            }
            if (Build.VERSION.SDK_INT < 24) {
            }
            this.rtlLayout = staticLayout3;
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
        if (SharedConfig.getDevicePerformanceClass() == 2) {
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private boolean isRtlLine(Layout layout, int i) {
        return layout.getLineRight(i) == ((float) layout.getWidth()) && layout.getLineLeft(i) != 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ChatActivityEnterView chatActivityEnterView, MessageEnterTransitionContainer messageEnterTransitionContainer, ValueAnimator valueAnimator) {
        this.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatActivityEnterView.getEditField().setAlpha(this.progress);
        messageEnterTransitionContainer.invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:229:0x040d, code lost:
    
        if (android.text.TextUtils.isEmpty(r5.caption) != false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x0429, code lost:
    
        if ((org.telegram.messenger.MessageObject.getMedia(r46.currentMessageObject.replyMessageObject.messageOwner) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) != false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x046c, code lost:
    
        if (android.text.TextUtils.isEmpty(r5.caption) != false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0488, code lost:
    
        if ((org.telegram.messenger.MessageObject.getMedia(r46.currentMessageObject.replyMessageObject.messageOwner) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) != false) goto L111;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x069c  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x071a  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x07b1  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0882  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0700  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x049c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x05ad  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x068d  */
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
        float f16;
        int themedColor;
        int i3;
        int i4;
        int themedColor2;
        float f17;
        float f18;
        MessageObject messageObject;
        float f19;
        float f20;
        float f21;
        float f22;
        ChatMessageCell chatMessageCell;
        int color;
        ChatMessageCell chatMessageCell2;
        StaticLayout staticLayout;
        int themedColor3;
        int i5;
        float f23;
        float dp;
        float dp2;
        int dp3;
        Drawable drawable;
        if (this.drawBitmaps && !this.initBitmaps && this.crossfadeTextBitmap != null && this.messageView.getTransitionParams().wasDraw) {
            this.initBitmaps = true;
            Canvas canvas2 = new Canvas(this.crossfadeTextBitmap);
            canvas2.translate(0.0f, this.crossfadeTextOffset);
            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.messageView.animatedEmojiStack;
            if (emojiGroupedSpans != null) {
                emojiGroupedSpans.clearPositions();
            }
            ChatMessageCell chatMessageCell3 = this.messageView;
            chatMessageCell3.drawMessageText(canvas2, chatMessageCell3.getMessageObject().textLayoutBlocks, true, 1.0f, true);
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
        this.lastMessageX = x2;
        this.lastMessageY = top;
        float interpolation = ChatListItemAnimator.DEFAULT_INTERPOLATOR.getInterpolation(this.progress);
        float f24 = this.progress;
        float f25 = f24 > 0.4f ? 1.0f : f24 / 0.4f;
        float interpolation2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f24));
        float f26 = x2 + this.textX;
        float f27 = top + this.textY;
        float f28 = 1.0f - interpolation2;
        int measuredHeight = (int) ((this.container.getMeasuredHeight() * f28) + (y * interpolation2));
        boolean z2 = this.messageView.getBottom() - AndroidUtilities.dp(4.0f) > this.listView.getMeasuredHeight() && (((float) this.messageView.getMeasuredHeight()) + top) - ((float) AndroidUtilities.dp(8.0f)) > ((float) measuredHeight) && this.container.getMeasuredHeight() > 0;
        if (z2) {
            i = measuredHeight;
            f = interpolation2;
            f2 = f25;
            f3 = interpolation;
            canvas.saveLayerAlpha(0.0f, Math.max(0.0f, top), this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), NotificationCenter.notificationsCountUpdated, 31);
        } else {
            i = measuredHeight;
            f = interpolation2;
            f2 = f25;
            f3 = interpolation;
        }
        canvas.save();
        canvas.clipRect(0.0f, ((this.listView.getY() + this.chatActivity.getChatListViewPadding()) - this.container.getY()) - AndroidUtilities.dp(3.0f), this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        canvas.save();
        float backgroundDrawableLeft = this.messageView.getBackgroundDrawableLeft() + x2 + ((x - (f26 - this.toXOffset)) * f28);
        float backgroundDrawableTop = this.messageView.getBackgroundDrawableTop() + top;
        float f29 = 1.0f - f3;
        float y3 = ((this.drawableFromTop - this.container.getY()) * f29) + (backgroundDrawableTop * f3);
        float y4 = ((this.drawableFromBottom - this.container.getY()) * f29) + ((backgroundDrawableTop + (this.messageView.getBackgroundDrawableBottom() - this.messageView.getBackgroundDrawableTop())) * f3);
        int backgroundDrawableRight = (int) (this.messageView.getBackgroundDrawableRight() + x2 + (AndroidUtilities.dp(4.0f) * f28));
        Theme.MessageDrawable currentBackgroundDrawable = !this.currentMessageObject.isAnimatedEmojiStickers() ? this.messageView.getCurrentBackgroundDrawable(true) : null;
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
                shadowDrawable.setAlpha(NotificationCenter.notificationsCountUpdated);
            }
            currentBackgroundDrawable.setAlpha((int) (f7 * 255.0f));
            currentBackgroundDrawable.setBounds((int) backgroundDrawableLeft, (int) y3, backgroundDrawableRight, (int) y4);
            currentBackgroundDrawable.setDrawFullBubble(true);
            currentBackgroundDrawable.draw(canvas);
            z = false;
            currentBackgroundDrawable.setDrawFullBubble(false);
            currentBackgroundDrawable.setAlpha(NotificationCenter.notificationsCountUpdated);
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
                dp = AndroidUtilities.dp(4.0f) + backgroundDrawableLeft;
                dp2 = AndroidUtilities.dp(4.0f) + y3;
                dp3 = AndroidUtilities.dp(10.0f);
            } else {
                dp = AndroidUtilities.dp(4.0f) + backgroundDrawableLeft;
                dp2 = AndroidUtilities.dp(4.0f) + y3;
                dp3 = AndroidUtilities.dp(4.0f);
            }
            canvas.clipRect(dp, dp2, backgroundDrawableRight - dp3, y4 - AndroidUtilities.dp(4.0f));
        }
        float f30 = top + ((y2 - f27) * f29);
        canvas.translate((this.messageView.getLeft() + this.listView.getX()) - this.container.getX(), f30);
        this.messageView.drawTime(canvas, f7, z);
        this.messageView.drawNamesLayout(canvas, f7);
        this.messageView.drawCommentButton(canvas, f7);
        this.messageView.drawCaptionLayout(canvas, z, f7);
        this.messageView.drawReactionsLayout(canvas, f7, null);
        this.messageView.drawCommentLayout(canvas, f7);
        this.messageView.drawLinkPreview(canvas, f7);
        canvas.restore();
        if (this.hasReply) {
            this.chatActivity.getReplyNameTextView().setAlpha(0.0f);
            this.chatActivity.getReplyObjectTextView().setAlpha(0.0f);
            AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), this.messageView.replyHeight, f6);
            int dp4 = AndroidUtilities.dp(10.0f);
            float x3 = this.replyFromStartX - this.container.getX();
            float y5 = this.replyFromStartY - this.container.getY();
            ChatMessageCell chatMessageCell4 = this.messageView;
            float f31 = chatMessageCell4.replyStartX + x2;
            i2 = backgroundDrawableRight;
            float f32 = top + chatMessageCell4.replyStartY;
            if (chatMessageCell4.replyLine == null) {
                chatMessageCell4.replyLine = new ReplyMessageLine(chatMessageCell4);
            }
            ChatMessageCell chatMessageCell5 = this.messageView;
            chatMessageCell5.replyLine.check(chatMessageCell5.getMessageObject(), this.messageView.getCurrentUser(), this.messageView.getCurrentChat(), this.resourcesProvider, 0);
            if (this.currentMessageObject.shouldDrawWithoutBackground()) {
                themedColor = getThemedColor(Theme.key_chat_stickerReplyNameText);
                i3 = Theme.key_chat_stickerReplyLine;
            } else {
                ReplyMessageLine replyMessageLine = this.messageView.replyLine;
                if (replyMessageLine != null) {
                    themedColor = replyMessageLine.nameColorAnimated.get();
                    this.messageView.replyLine.getColor();
                    if (!this.currentMessageObject.shouldDrawWithoutBackground()) {
                        f8 = y4;
                        f9 = y3;
                        ReplyMessageLine replyMessageLine2 = this.messageView.replyLine;
                        if (replyMessageLine2 != null) {
                            themedColor2 = replyMessageLine2.nameColorAnimated.get();
                        } else {
                            if (this.currentMessageObject.hasValidReplyMessageObject()) {
                                MessageObject messageObject2 = this.currentMessageObject.replyMessageObject;
                                if (messageObject2.type == 0 || !TextUtils.isEmpty(messageObject2.caption)) {
                                    TLRPC.MessageMedia messageMedia = this.currentMessageObject.replyMessageObject.messageOwner.media;
                                    if (!(messageMedia instanceof TLRPC.TL_messageMediaGame) && !(messageMedia instanceof TLRPC.TL_messageMediaInvoice)) {
                                        i4 = Theme.key_chat_outReplyMessageText;
                                        themedColor2 = getThemedColor(i4);
                                    }
                                }
                            }
                            i4 = Theme.key_chat_outReplyMediaMessageText;
                            themedColor2 = getThemedColor(i4);
                        }
                    } else if (!this.currentMessageObject.isOutOwner()) {
                        f8 = y4;
                        f9 = y3;
                        if (!this.currentMessageObject.isReplyToStory()) {
                            themedColor3 = getThemedColor(Theme.key_chat_inReplyMessageText);
                            MessageObject messageObject3 = this.currentMessageObject;
                            if (!messageObject3.forceAvatar) {
                                if (messageObject3.hasValidReplyMessageObject()) {
                                    MessageObject messageObject4 = this.currentMessageObject.replyMessageObject;
                                    if (messageObject4.type != 0) {
                                    }
                                    if (!(MessageObject.getMedia(this.currentMessageObject.replyMessageObject.messageOwner) instanceof TLRPC.TL_messageMediaGame)) {
                                    }
                                }
                                if (!this.messageView.isReplyQuote) {
                                    i5 = Theme.key_chat_inReplyMediaMessageText;
                                    themedColor3 = getThemedColor(i5);
                                    f23 = 0.6f;
                                    themedColor2 = ColorUtils.blendARGB(themedColor3, Theme.adaptHue(themedColor3, themedColor), f23);
                                }
                            }
                            f23 = 0.0f;
                            themedColor2 = ColorUtils.blendARGB(themedColor3, Theme.adaptHue(themedColor3, themedColor), f23);
                        }
                        themedColor2 = themedColor;
                    } else if (this.currentMessageObject.isReplyToStory()) {
                        f8 = y4;
                        f9 = y3;
                        themedColor2 = themedColor;
                    } else {
                        themedColor3 = getThemedColor(Theme.key_chat_outReplyMessageText);
                        f8 = y4;
                        MessageObject messageObject5 = this.currentMessageObject;
                        f9 = y3;
                        if (!messageObject5.forceAvatar) {
                            if (messageObject5.hasValidReplyMessageObject()) {
                                MessageObject messageObject6 = this.currentMessageObject.replyMessageObject;
                                if (messageObject6.type != 0) {
                                }
                                if (!(MessageObject.getMedia(this.currentMessageObject.replyMessageObject.messageOwner) instanceof TLRPC.TL_messageMediaGame)) {
                                }
                            }
                            if (!this.messageView.isReplyQuote) {
                                i5 = Theme.key_chat_outReplyMediaMessageText;
                                themedColor3 = getThemedColor(i5);
                                f23 = 0.6f;
                                themedColor2 = ColorUtils.blendARGB(themedColor3, Theme.adaptHue(themedColor3, themedColor), f23);
                            }
                        }
                        f23 = 0.0f;
                        themedColor2 = ColorUtils.blendARGB(themedColor3, Theme.adaptHue(themedColor3, themedColor), f23);
                    }
                    f10 = backgroundDrawableLeft;
                    float f33 = f4;
                    Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor2, f33));
                    Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor, f33));
                    if (this.messageView.needReplyImage) {
                        x3 -= AndroidUtilities.dp(44.0f);
                    }
                    float f34 = x3;
                    float lerp = AndroidUtilities.lerp(f34, f31, f6);
                    float lerp2 = AndroidUtilities.lerp((AndroidUtilities.dp(12.0f) * f33) + y5, f32, f33);
                    if (this.roundRectRadii == null) {
                        this.roundRectRadii = new float[]{r5, r5, 0.0f, 0.0f, 0.0f, 0.0f, r5, r5};
                        float dp5 = AndroidUtilities.dp(4.0f);
                        float[] fArr = this.roundRectRadii;
                        fArr[5] = 0.0f;
                        fArr[4] = 0.0f;
                        fArr[3] = 0.0f;
                        fArr[2] = 0.0f;
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(f34, y5, this.replyFromStartWidth + f34, AndroidUtilities.dp(35.0f) + y5);
                    rectF.offset(0.0f, AndroidUtilities.dp(12.0f) * f33);
                    this.messageReplySelectorRect.set(this.messageView.replySelectorRect);
                    this.messageReplySelectorRect.offset(x2, f30);
                    AndroidUtilities.lerp(rectF, this.messageReplySelectorRect, f6, this.replySelectorRect);
                    ChatMessageCell chatMessageCell6 = this.messageView;
                    chatMessageCell6.replyLine.drawBackground(canvas, this.replySelectorRect, f7, chatMessageCell6.isReplyQuote, chatMessageCell6.getMessageObject().shouldDrawWithoutBackground());
                    this.messageView.replyLine.drawLine(canvas, this.replySelectorRect, f7);
                    if (this.messageView.needReplyImage) {
                        f17 = 0.0f;
                    } else {
                        canvas.save();
                        float lerp3 = AndroidUtilities.lerp(AndroidUtilities.dp(35.0f), Math.min(this.replySelectorRect.height() - AndroidUtilities.dp(10.0f), AndroidUtilities.dp(this.messageView.isReplyQuote ? 3.0f : 7.0f) + Theme.chat_replyNamePaint.getTextSize() + Theme.chat_replyTextPaint.getTextSize()), f6);
                        ImageReceiver imageReceiver = this.messageView.replyImageReceiver;
                        float lerp4 = AndroidUtilities.lerp(lerp, this.replySelectorRect.left + AndroidUtilities.dp(8.0f), f6);
                        float f35 = this.replySelectorRect.top;
                        ChatMessageCell chatMessageCell7 = this.messageView;
                        imageReceiver.setImageCoords(lerp4, AndroidUtilities.lerp(lerp2, f35 + AndroidUtilities.dp(((!chatMessageCell7.isReplyQuote || (staticLayout = chatMessageCell7.replyTextLayout) == null || staticLayout.getLineCount() > 1) ? 0 : 2) + 5), f6), lerp3, lerp3);
                        this.messageView.replyImageReceiver.draw(canvas);
                        canvas.restore();
                        f17 = lerp3;
                    }
                    canvas.save();
                    f18 = dp4 * f6;
                    canvas.translate(f18, 0.0f);
                    messageObject = this.currentMessageObject;
                    if (messageObject == null && messageObject.shouldDrawWithoutBackground()) {
                        f20 = -AndroidUtilities.dp(6.0f);
                        f19 = 1.0f;
                    } else {
                        f19 = 1.0f;
                        f20 = -AndroidUtilities.dp(1.0f);
                    }
                    MessageObject messageObject7 = this.currentMessageObject;
                    float dp6 = (messageObject7 == null && messageObject7.shouldDrawWithoutBackground()) ? AndroidUtilities.dp(f19) : AndroidUtilities.dp(3.0f);
                    float f36 = this.messageView.replyTextOffset;
                    float f37 = (f31 - f36) + f20;
                    float f38 = (f31 - this.replyNameDx) + f20;
                    AndroidUtilities.lerp(f34 - f36, f37, f6);
                    float lerp5 = AndroidUtilities.lerp(f34, f38, f6) + (!this.messageView.needReplyImage ? AndroidUtilities.dp(3.0f) + f17 : 0.0f);
                    if (this.messageView.replyNameLayout == null) {
                        canvas.save();
                        canvas.translate(lerp5, (dp6 * f6) + lerp2);
                        int alpha = Theme.chat_replyNamePaint.getAlpha();
                        Theme.chat_replyNamePaint.setAlpha((int) (alpha * f6));
                        this.messageView.replyNameLayout.draw(canvas);
                        Theme.chat_replyNamePaint.setAlpha(alpha);
                        SimpleTextView replyNameTextView = this.chatActivity.getReplyNameTextView();
                        f21 = f37;
                        f22 = f33;
                        canvas.saveLayerAlpha(0.0f, 0.0f, replyNameTextView.getWidth(), replyNameTextView.getHeight(), (int) (f28 * 255.0f), 31);
                        replyNameTextView.setAlpha(1.0f);
                        replyNameTextView.draw(canvas);
                        replyNameTextView.setAlpha(0.0f);
                        canvas.restore();
                        canvas.restore();
                    } else {
                        f21 = f37;
                        f22 = f33;
                    }
                    chatMessageCell = this.messageView;
                    if (chatMessageCell.isReplyQuote && chatMessageCell.replyQuoteDrawable != null) {
                        color = chatMessageCell.replyLine.getColor();
                        chatMessageCell2 = this.messageView;
                        if (color != chatMessageCell2.replyQuoteDrawableColor) {
                            Drawable drawable2 = chatMessageCell2.replyQuoteDrawable;
                            ChatMessageCell chatMessageCell8 = this.messageView;
                            int color2 = chatMessageCell8.replyLine.getColor();
                            chatMessageCell8.replyQuoteDrawableColor = color2;
                            drawable2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_IN));
                        }
                        ChatMessageCell chatMessageCell9 = this.messageView;
                        chatMessageCell9.replyQuoteDrawable.setBounds((int) (((this.replySelectorRect.right - f18) - AndroidUtilities.dp((!chatMessageCell9.drawPinnedTop ? 1 : 0) + 2)) - this.messageView.replyQuoteDrawable.getIntrinsicWidth()), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) ((this.replySelectorRect.right - f18) - AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) (this.replySelectorRect.top + AndroidUtilities.dp((1 ^ (this.messageView.drawPinnedTop ? 1 : 0)) + 2) + this.messageView.replyQuoteDrawable.getIntrinsicHeight()));
                        this.messageView.replyQuoteDrawable.setAlpha((int) (f6 * 255.0f));
                        this.messageView.replyQuoteDrawable.draw(canvas);
                    }
                    if (this.messageView.replyTextLayout == null) {
                        canvas.save();
                        ChatMessageCell chatMessageCell10 = this.messageView;
                        float dp7 = (chatMessageCell10.isReplyQuote && chatMessageCell10.needReplyImage) ? f21 - AndroidUtilities.dp(2.0f) : f21;
                        ChatMessageCell chatMessageCell11 = this.messageView;
                        if (chatMessageCell11.needReplyImage && (!chatMessageCell11.isReplyQuote || chatMessageCell11.replyTextRTL)) {
                            dp7 += f17 + AndroidUtilities.dp(3.0f);
                        }
                        ChatMessageCell chatMessageCell12 = this.messageView;
                        if (chatMessageCell12.replyTextRTL && chatMessageCell12.replyTextOffset > 0) {
                            dp7 = ((this.replySelectorRect.right - AndroidUtilities.dp(8.0f)) - this.messageView.replyTextLayout.getWidth()) - f18;
                        }
                        canvas.translate(AndroidUtilities.lerp(f34 - this.messageView.replyTextOffset, dp7, f6), lerp2 + AndroidUtilities.lerp(AndroidUtilities.dp(19.0f), Theme.chat_replyNamePaint.getTextSize() + AndroidUtilities.dp(4.0f) + dp6, f6));
                        canvas.save();
                        SpoilerEffect.clipOutCanvas(canvas, this.messageView.replySpoilers);
                        ChatMessageCell chatMessageCell13 = this.messageView;
                        f12 = f22;
                        f11 = f30;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, chatMessageCell13.replyTextLayout, chatMessageCell13.animatedEmojiReplyStack, 0.0f, chatMessageCell13.replySpoilers, 0.0f, 0.0f, 0.0f, 1.0f);
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
                        f12 = f22;
                        f11 = f30;
                    }
                    canvas.restore();
                } else if (this.currentMessageObject.isOutOwner()) {
                    themedColor = getThemedColor(Theme.key_chat_outReplyNameText);
                    i3 = Theme.key_chat_outReplyLine;
                } else {
                    themedColor = getThemedColor(Theme.key_chat_inReplyNameText);
                    i3 = Theme.key_chat_inReplyLine;
                }
            }
            getThemedColor(i3);
            if (!this.currentMessageObject.shouldDrawWithoutBackground()) {
            }
            f10 = backgroundDrawableLeft;
            float f332 = f4;
            Theme.chat_replyTextPaint.setColor(ColorUtils.blendARGB(this.replayObjectFromColor, themedColor2, f332));
            Theme.chat_replyNamePaint.setColor(ColorUtils.blendARGB(this.replayFromColor, themedColor, f332));
            if (this.messageView.needReplyImage) {
            }
            float f342 = x3;
            float lerp6 = AndroidUtilities.lerp(f342, f31, f6);
            float lerp22 = AndroidUtilities.lerp((AndroidUtilities.dp(12.0f) * f332) + y5, f32, f332);
            if (this.roundRectRadii == null) {
            }
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(f342, y5, this.replyFromStartWidth + f342, AndroidUtilities.dp(35.0f) + y5);
            rectF2.offset(0.0f, AndroidUtilities.dp(12.0f) * f332);
            this.messageReplySelectorRect.set(this.messageView.replySelectorRect);
            this.messageReplySelectorRect.offset(x2, f30);
            AndroidUtilities.lerp(rectF2, this.messageReplySelectorRect, f6, this.replySelectorRect);
            ChatMessageCell chatMessageCell62 = this.messageView;
            chatMessageCell62.replyLine.drawBackground(canvas, this.replySelectorRect, f7, chatMessageCell62.isReplyQuote, chatMessageCell62.getMessageObject().shouldDrawWithoutBackground());
            this.messageView.replyLine.drawLine(canvas, this.replySelectorRect, f7);
            if (this.messageView.needReplyImage) {
            }
            canvas.save();
            f18 = dp4 * f6;
            canvas.translate(f18, 0.0f);
            messageObject = this.currentMessageObject;
            if (messageObject == null) {
            }
            f19 = 1.0f;
            f20 = -AndroidUtilities.dp(1.0f);
            MessageObject messageObject72 = this.currentMessageObject;
            float dp62 = (messageObject72 == null && messageObject72.shouldDrawWithoutBackground()) ? AndroidUtilities.dp(f19) : AndroidUtilities.dp(3.0f);
            float f362 = this.messageView.replyTextOffset;
            float f372 = (f31 - f362) + f20;
            float f382 = (f31 - this.replyNameDx) + f20;
            AndroidUtilities.lerp(f342 - f362, f372, f6);
            float lerp52 = AndroidUtilities.lerp(f342, f382, f6) + (!this.messageView.needReplyImage ? AndroidUtilities.dp(3.0f) + f17 : 0.0f);
            if (this.messageView.replyNameLayout == null) {
            }
            chatMessageCell = this.messageView;
            if (chatMessageCell.isReplyQuote) {
                color = chatMessageCell.replyLine.getColor();
                chatMessageCell2 = this.messageView;
                if (color != chatMessageCell2.replyQuoteDrawableColor) {
                }
                ChatMessageCell chatMessageCell92 = this.messageView;
                chatMessageCell92.replyQuoteDrawable.setBounds((int) (((this.replySelectorRect.right - f18) - AndroidUtilities.dp((!chatMessageCell92.drawPinnedTop ? 1 : 0) + 2)) - this.messageView.replyQuoteDrawable.getIntrinsicWidth()), (int) (this.replySelectorRect.top + AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) ((this.replySelectorRect.right - f18) - AndroidUtilities.dp((!this.messageView.drawPinnedTop ? 1 : 0) + 2)), (int) (this.replySelectorRect.top + AndroidUtilities.dp((1 ^ (this.messageView.drawPinnedTop ? 1 : 0)) + 2) + this.messageView.replyQuoteDrawable.getIntrinsicHeight()));
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
            f11 = f30;
            f12 = f4;
        }
        canvas.save();
        if (this.messageView.getMessageObject() == null || this.messageView.getMessageObject().type != 19) {
            canvas.clipRect(f10 + AndroidUtilities.dp(4.0f), f9 + AndroidUtilities.dp(4.0f), i2 - AndroidUtilities.dp(4.0f), f8 - AndroidUtilities.dp(4.0f));
        }
        float f39 = f6 + (this.scaleFrom * f28);
        float f40 = this.drawBitmaps ? (this.scaleY * f28) + f6 : 1.0f;
        canvas.save();
        float f41 = f5 * f28;
        float f42 = y2 * f29;
        canvas.translate(f41 + ((f26 - this.toXOffset) * f6), ((f27 + this.textLayoutBlock.textYOffset(this.messageView.getMessageObject().textLayoutBlocks, this.messageView.transitionParams)) * f12) + f42);
        float f43 = f39 * f40;
        canvas.scale(f39, f43, 0.0f, 0.0f);
        if (this.drawBitmaps) {
            if (this.crossfade) {
                this.bitmapPaint.setAlpha((int) ((1.0f - f7) * 255.0f));
            }
            canvas.drawBitmap(this.textLayoutBitmap, 0.0f, 0.0f, this.bitmapPaint);
            f13 = f43;
            f14 = f11;
            f15 = f39;
        } else {
            boolean z3 = this.crossfade;
            if (z3 && this.changeColor) {
                int color3 = this.layout.getPaint().getColor();
                this.layout.getPaint().setColor(ColorUtils.blendARGB(this.fromColor, this.toColor, f7));
                float f44 = 1.0f - f7;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f44 * 255.0f), 31);
                this.layout.draw(canvas);
                f13 = f43;
                f14 = f11;
                f15 = f39;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f44);
                this.layout.getPaint().setColor(color3);
            } else {
                f13 = f43;
                f14 = f11;
                f15 = f39;
                if (z3) {
                    float f45 = 1.0f - f7;
                    canvas.saveLayerAlpha(0.0f, 0.0f, this.layout.getWidth(), this.layout.getHeight(), (int) (f45 * 255.0f), 31);
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, f45);
                } else {
                    this.layout.draw(canvas);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout, this.animatedEmojiStack, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        if (this.rtlLayout != null) {
            canvas.save();
            canvas.translate(f41 + ((f26 - this.toXOffsetRtl) * f6), f42 + ((f27 + this.textLayoutBlock.textYOffset(this.messageView.getMessageObject().textLayoutBlocks, this.messageView.transitionParams)) * f12));
            f16 = f13;
            canvas.scale(f15, f16, 0.0f, 0.0f);
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
        } else {
            f16 = f13;
        }
        if (this.crossfade) {
            canvas.save();
            canvas.translate(((this.messageView.getLeft() + this.listView.getX()) - this.container.getX()) + ((f5 - f26) * f28), f14);
            canvas.scale(f15, f16, this.messageView.getTextX(), this.messageView.getTextY());
            canvas.translate(0.0f, -this.crossfadeTextOffset);
            if (this.crossfadeTextBitmap != null) {
                this.bitmapPaint.setAlpha((int) (f7 * 255.0f));
                canvas.drawBitmap(this.crossfadeTextBitmap, 0.0f, 0.0f, this.bitmapPaint);
            } else {
                int color5 = Theme.chat_msgTextPaint.getColor();
                Theme.chat_msgTextPaint.setColor(this.toColor);
                ChatMessageCell chatMessageCell14 = this.messageView;
                chatMessageCell14.drawMessageText(canvas, chatMessageCell14.getMessageObject().textLayoutBlocks, true, f7, true);
                this.messageView.drawAnimatedEmojis(canvas, f7);
                if (Theme.chat_msgTextPaint.getColor() != color5) {
                    Theme.chat_msgTextPaint.setColor(color5);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        if (z2) {
            float f46 = i;
            this.gradientMatrix.setTranslate(0.0f, f46);
            this.gradientShader.setLocalMatrix(this.gradientMatrix);
            canvas.drawRect(0.0f, f46, this.container.getMeasuredWidth(), this.container.getMeasuredHeight(), this.gradientPaint);
            canvas.restore();
        }
        float f47 = this.progress;
        float f48 = f47 > 0.4f ? 1.0f : f47 / 0.4f;
        if (f48 == 1.0f) {
            this.enterView.setTextTransitionIsRunning(false);
        }
        if (this.enterView.getSendButton().getVisibility() != 0 || f48 >= 1.0f) {
            return;
        }
        canvas.save();
        canvas.translate(((((this.enterView.getX() + this.enterView.getSendButton().getX()) + ((View) this.enterView.getSendButton().getParent()).getX()) + ((View) this.enterView.getSendButton().getParent().getParent()).getX()) - this.container.getX()) + (AndroidUtilities.dp(52.0f) * f48), (((this.enterView.getY() + this.enterView.getSendButton().getY()) + ((View) this.enterView.getSendButton().getParent()).getY()) + ((View) this.enterView.getSendButton().getParent().getParent()).getY()) - this.container.getY());
        this.enterView.getSendButton().draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    public void start() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }
}
