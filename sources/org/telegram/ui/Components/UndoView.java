package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.PaymentFormActivity;

/* loaded from: classes3.dex */
public class UndoView extends FrameLayout {
    public static int ACTION_RINGTONE_ADDED = 83;
    private float additionalTranslationY;
    private BackupImageView avatarImageView;
    Drawable backgroundDrawable;
    private int currentAccount;
    private int currentAction;
    private Runnable currentActionRunnable;
    private Runnable currentCancelRunnable;
    private ArrayList currentDialogIds;
    private Object currentInfoObject;
    private Object currentInfoObject2;
    float enterOffset;
    private int enterOffsetMargin;
    private boolean fromTop;
    private int hideAnimationType;
    private CharSequence infoText;
    private LinkSpanDrawable.LinksTextView infoTextView;
    private boolean isShown;
    private long lastUpdateTime;
    private RLottieImageView leftImageView;
    private BaseFragment parentFragment;
    private int prevSeconds;
    private Paint progressPaint;
    private RectF rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private TextView subinfoTextView;
    private TextPaint textPaint;
    private int textWidth;
    int textWidthOut;
    StaticLayout timeLayout;
    StaticLayout timeLayoutOut;
    private long timeLeft;
    private String timeLeftString;
    float timeReplaceProgress;
    private LinearLayout undoButton;
    private ImageView undoImageView;
    private TextView undoTextView;
    private int undoViewHeight;

    /* loaded from: classes3.dex */
    public class LinkMovementMethodMy extends LinkMovementMethod {
        public LinkMovementMethodMy() {
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            CharacterStyle[] characterStyleArr;
            try {
                if (motionEvent.getAction() != 0 || ((characterStyleArr = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class)) != null && characterStyleArr.length != 0)) {
                    if (motionEvent.getAction() != 1) {
                        return super.onTouchEvent(textView, spannable, motionEvent);
                    }
                    CharacterStyle[] characterStyleArr2 = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class);
                    if (characterStyleArr2 != null && characterStyleArr2.length > 0) {
                        UndoView.this.didPressUrl(characterStyleArr2[0]);
                    }
                    Selection.removeSelection(spannable);
                    return true;
                }
                return false;
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    public UndoView(Context context) {
        this(context, null, false, null);
    }

    public UndoView(Context context, BaseFragment baseFragment) {
        this(context, baseFragment, false, null);
    }

    public UndoView(Context context, BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.currentAction = -1;
        this.hideAnimationType = 1;
        this.enterOffsetMargin = AndroidUtilities.dp(8.0f);
        this.timeReplaceProgress = 1.0f;
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = baseFragment;
        this.fromTop = z;
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        this.infoTextView = linksTextView;
        linksTextView.setTextSize(1, 15.0f);
        LinkSpanDrawable.LinksTextView linksTextView2 = this.infoTextView;
        int i = Theme.key_undo_infoColor;
        linksTextView2.setTextColor(getThemedColor(i));
        LinkSpanDrawable.LinksTextView linksTextView3 = this.infoTextView;
        int i2 = Theme.key_undo_cancelColor;
        linksTextView3.setLinkTextColor(getThemedColor(i2));
        this.infoTextView.setMovementMethod(new LinkMovementMethodMy());
        addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 45.0f, 13.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.subinfoTextView = textView;
        textView.setTextSize(1, 13.0f);
        this.subinfoTextView.setTextColor(getThemedColor(i));
        this.subinfoTextView.setLinkTextColor(getThemedColor(i2));
        this.subinfoTextView.setHighlightColor(0);
        this.subinfoTextView.setSingleLine(true);
        this.subinfoTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.subinfoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        addView(this.subinfoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 58.0f, 27.0f, 8.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.leftImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        RLottieImageView rLottieImageView2 = this.leftImageView;
        int i3 = Theme.key_undo_background;
        rLottieImageView2.setLayerColor("info1.**", getThemedColor(i3) | (-16777216));
        this.leftImageView.setLayerColor("info2.**", getThemedColor(i3) | (-16777216));
        this.leftImageView.setLayerColor("luc12.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc11.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc10.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc9.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc8.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc7.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc6.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc5.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc4.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc3.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc2.**", getThemedColor(i));
        this.leftImageView.setLayerColor("luc1.**", getThemedColor(i));
        this.leftImageView.setLayerColor("Oval.**", getThemedColor(i));
        addView(this.leftImageView, LayoutHelper.createFrame(54, -2.0f, 19, 3.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(15.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(30, 30.0f, 19, 15.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.undoButton = linearLayout;
        linearLayout.setOrientation(0);
        this.undoButton.setBackground(Theme.createRadSelectorDrawable(getThemedColor(i2) & 587202559, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f)));
        addView(this.undoButton, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 11.0f, 0.0f));
        this.undoButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UndoView.this.lambda$new$0(view);
            }
        });
        ImageView imageView = new ImageView(context);
        this.undoImageView = imageView;
        imageView.setImageResource(R.drawable.chats_undo);
        this.undoImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
        this.undoButton.addView(this.undoImageView, LayoutHelper.createLinear(-2, -2, 19, 4, 4, 0, 4));
        TextView textView2 = new TextView(context);
        this.undoTextView = textView2;
        textView2.setTextSize(1, 14.0f);
        this.undoTextView.setTypeface(AndroidUtilities.bold());
        this.undoTextView.setTextColor(getThemedColor(i2));
        this.undoTextView.setText(LocaleController.getString(R.string.Undo));
        this.undoButton.addView(this.undoTextView, LayoutHelper.createLinear(-2, -2, 19, 6, 4, 8, 4));
        this.rect = new RectF(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), AndroidUtilities.dp(33.0f), AndroidUtilities.dp(33.0f));
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setColor(getThemedColor(i));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.bold());
        this.textPaint.setColor(getThemedColor(i));
        setWillNotDraw(false);
        this.backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), getThemedColor(i3));
        setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$new$1;
                lambda$new$1 = UndoView.lambda$new$1(view, motionEvent);
                return lambda$new$1;
            }
        });
        setVisibility(4);
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private boolean hasSubInfo() {
        int i;
        Object obj;
        int i2 = this.currentAction;
        return i2 == 11 || i2 == 24 || i2 == 6 || i2 == 3 || i2 == 5 || i2 == 13 || i2 == 14 || i2 == 74 || (i2 == 7 && MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) || (i = this.currentAction) == ACTION_RINGTONE_ADDED || i == 85 || (i == 88 && (obj = this.currentInfoObject2) != null && ((Integer) obj).intValue() > 0);
    }

    private boolean isTooltipAction() {
        int i = this.currentAction;
        return i == 6 || i == 3 || i == 5 || i == 7 || i == 8 || i == 87 || i == 9 || i == 10 || i == 13 || i == 14 || i == 19 || i == 20 || i == 21 || i == 22 || i == 23 || i == 30 || i == 31 || i == 32 || i == 33 || i == 34 || i == 35 || i == 36 || i == 74 || i == 37 || i == 38 || i == 39 || i == 40 || i == 42 || i == 43 || i == 77 || i == 44 || i == 78 || i == 79 || i == 100 || i == 101 || i == ACTION_RINGTONE_ADDED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (canUndo()) {
            hide(false, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$2(View view) {
        hide(false, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showWithAction$3(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$4(TLObject tLObject) {
        if (tLObject instanceof TLRPC.PaymentReceipt) {
            this.parentFragment.presentFragment(new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$5(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                UndoView.this.lambda$showWithAction$4(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$6(TLRPC.Message message, View view) {
        hide(true, 1);
        TLRPC.TL_payments_getPaymentReceipt tL_payments_getPaymentReceipt = new TLRPC.TL_payments_getPaymentReceipt();
        tL_payments_getPaymentReceipt.msg_id = message.id;
        tL_payments_getPaymentReceipt.peer = this.parentFragment.getMessagesController().getInputPeer(message.peer_id);
        this.parentFragment.getConnectionsManager().sendRequest(tL_payments_getPaymentReceipt, new RequestDelegate() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda6
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                UndoView.this.lambda$showWithAction$5(tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showWithAction$7() {
        this.leftImageView.performHapticFeedback(3, 2);
    }

    private void updatePosition() {
        setTranslationY(((this.enterOffset - this.enterOffsetMargin) + AndroidUtilities.dp(8.0f)) - this.additionalTranslationY);
        invalidate();
    }

    protected boolean canUndo() {
        return true;
    }

    public void didPressUrl(CharacterStyle characterStyle) {
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.additionalTranslationY == 0.0f) {
            super.dispatchDraw(canvas);
            return;
        }
        canvas.save();
        float measuredHeight = (getMeasuredHeight() - this.enterOffset) + AndroidUtilities.dp(9.0f);
        if (measuredHeight > 0.0f) {
            canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight);
            super.dispatchDraw(canvas);
        }
        canvas.restore();
    }

    @Override // android.view.View
    public Drawable getBackground() {
        return this.backgroundDrawable;
    }

    public Object getCurrentInfoObject() {
        return this.currentInfoObject;
    }

    public float getEnterOffset() {
        return this.enterOffset;
    }

    public void hide(boolean z, int i) {
        long j;
        if (getVisibility() == 0 && this.isShown) {
            this.currentInfoObject = null;
            this.currentInfoObject2 = null;
            this.isShown = false;
            Runnable runnable = this.currentActionRunnable;
            if (runnable != null) {
                if (z) {
                    runnable.run();
                }
                this.currentActionRunnable = null;
            }
            Runnable runnable2 = this.currentCancelRunnable;
            if (runnable2 != null) {
                if (!z) {
                    runnable2.run();
                }
                this.currentCancelRunnable = null;
            }
            int i2 = this.currentAction;
            if (i2 == 0 || i2 == 1 || i2 == 26 || i2 == 27) {
                for (int i3 = 0; i3 < this.currentDialogIds.size(); i3++) {
                    long longValue = ((Long) this.currentDialogIds.get(i3)).longValue();
                    MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                    int i4 = this.currentAction;
                    messagesController.removeDialogAction(longValue, i4 == 0 || i4 == 26, z);
                    onRemoveDialogAction(longValue, this.currentAction);
                }
            }
            if (i == 0) {
                setEnterOffset((this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight));
                setVisibility(4);
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            if (i == 1) {
                animatorSet.playTogether(ObjectAnimator.ofFloat(this, "enterOffset", (this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight)));
                j = 250;
            } else {
                animatorSet.playTogether(ObjectAnimator.ofFloat(this, (Property<UndoView, Float>) View.SCALE_X, 0.8f), ObjectAnimator.ofFloat(this, (Property<UndoView, Float>) View.SCALE_Y, 0.8f), ObjectAnimator.ofFloat(this, (Property<UndoView, Float>) View.ALPHA, 0.0f));
                j = 180;
            }
            animatorSet.setDuration(j);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.UndoView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    UndoView.this.setVisibility(4);
                    UndoView.this.setScaleX(1.0f);
                    UndoView.this.setScaleY(1.0f);
                    UndoView.this.setAlpha(1.0f);
                }
            });
            animatorSet.start();
        }
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        this.infoTextView.invalidate();
        this.leftImageView.invalidate();
    }

    public boolean isMultilineSubInfo() {
        int i = this.currentAction;
        return i == 12 || i == 15 || i == 24 || i == 74 || i == ACTION_RINGTONE_ADDED;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.additionalTranslationY != 0.0f) {
            canvas.save();
            float measuredHeight = (getMeasuredHeight() - this.enterOffset) + this.enterOffsetMargin + AndroidUtilities.dp(1.0f);
            if (measuredHeight > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight);
                super.dispatchDraw(canvas);
            }
            this.backgroundDrawable.draw(canvas);
            canvas.restore();
        } else {
            this.backgroundDrawable.draw(canvas);
        }
        int i = this.currentAction;
        if (i == 1 || i == 0 || i == 27 || i == 26 || i == 81 || i == 88) {
            int ceil = this.timeLeft > 0 ? (int) Math.ceil(((float) r10) / 1000.0f) : 0;
            if (this.prevSeconds != ceil) {
                this.prevSeconds = ceil;
                this.timeLeftString = String.format("%d", Integer.valueOf(Math.max(1, ceil)));
                StaticLayout staticLayout = this.timeLayout;
                if (staticLayout != null) {
                    this.timeLayoutOut = staticLayout;
                    this.timeReplaceProgress = 0.0f;
                    this.textWidthOut = this.textWidth;
                }
                this.textWidth = (int) Math.ceil(this.textPaint.measureText(r2));
                this.timeLayout = new StaticLayout(this.timeLeftString, this.textPaint, ConnectionsManager.DEFAULT_DATACENTER_ID, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            float f = this.timeReplaceProgress;
            if (f < 1.0f) {
                float f2 = f + 0.10666667f;
                this.timeReplaceProgress = f2;
                if (f2 > 1.0f) {
                    this.timeReplaceProgress = 1.0f;
                } else {
                    invalidate();
                }
            }
            int alpha = this.textPaint.getAlpha();
            if (this.timeLayoutOut != null) {
                float f3 = this.timeReplaceProgress;
                if (f3 < 1.0f) {
                    this.textPaint.setAlpha((int) (alpha * (1.0f - f3)));
                    canvas.save();
                    canvas.translate(this.rect.centerX() - (this.textWidth / 2), AndroidUtilities.dp(17.2f) + (AndroidUtilities.dp(10.0f) * this.timeReplaceProgress));
                    this.timeLayoutOut.draw(canvas);
                    this.textPaint.setAlpha(alpha);
                    canvas.restore();
                }
            }
            if (this.timeLayout != null) {
                float f4 = this.timeReplaceProgress;
                if (f4 != 1.0f) {
                    this.textPaint.setAlpha((int) (alpha * f4));
                }
                canvas.save();
                canvas.translate(this.rect.centerX() - (this.textWidth / 2), AndroidUtilities.dp(17.2f) - (AndroidUtilities.dp(10.0f) * (1.0f - this.timeReplaceProgress)));
                this.timeLayout.draw(canvas);
                if (this.timeReplaceProgress != 1.0f) {
                    this.textPaint.setAlpha(alpha);
                }
                canvas.restore();
            }
            canvas.drawArc(this.rect, -90.0f, (-360.0f) * (((float) this.timeLeft) / 5000.0f), false, this.progressPaint);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.timeLeft - (elapsedRealtime - this.lastUpdateTime);
        this.timeLeft = j;
        this.lastUpdateTime = elapsedRealtime;
        if (j <= 0) {
            hide(true, this.hideAnimationType);
        }
        if (this.currentAction != 82) {
            invalidate();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(this.undoViewHeight, 1073741824));
        this.backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    protected void onRemoveDialogAction(long j, int i) {
    }

    public void setAdditionalTranslationY(float f) {
        if (this.additionalTranslationY != f) {
            this.additionalTranslationY = f;
            updatePosition();
        }
    }

    public void setColors(int i, int i2) {
        Theme.setDrawableColor(this.backgroundDrawable, i);
        this.infoTextView.setTextColor(i2);
        this.subinfoTextView.setTextColor(i2);
        int i3 = i | (-16777216);
        this.leftImageView.setLayerColor("info1.**", i3);
        this.leftImageView.setLayerColor("info2.**", i3);
    }

    public void setEnterOffset(float f) {
        if (this.enterOffset != f) {
            this.enterOffset = f;
            updatePosition();
        }
    }

    public void setEnterOffsetMargin(int i) {
        this.enterOffsetMargin = i;
    }

    public void setHideAnimationType(int i) {
        this.hideAnimationType = i;
    }

    public void setInfoText(CharSequence charSequence) {
        this.infoText = charSequence;
    }

    public void showWithAction(long j, int i, Object obj) {
        showWithAction(j, i, obj, (Object) null, (Runnable) null, (Runnable) null);
    }

    public void showWithAction(long j, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(j));
        showWithAction(arrayList, i, obj, obj2, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Object obj, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, obj, (Object) null, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Runnable runnable) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, (Runnable) null);
    }

    public void showWithAction(long j, int i, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, runnable2);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:531:0x0e79. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:322:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0637  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x07a0  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x07cc  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x1631  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x1658  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0e05  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x1743  */
    /* JADX WARN: Removed duplicated region for block: B:579:0x1611  */
    /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x169e  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x080d  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x07c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showWithAction(ArrayList arrayList, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        LinkSpanDrawable.LinksTextView linksTextView;
        int i2;
        LinkSpanDrawable.LinksTextView linksTextView2;
        String formatString;
        RLottieImageView rLottieImageView;
        int i3;
        LinkSpanDrawable.LinksTextView linksTextView3;
        SpannableStringBuilder replaceTags;
        long j;
        LinkSpanDrawable.LinksTextView linksTextView4;
        String formatString2;
        String string;
        BulletinFactory$$ExternalSyntheticLambda0 bulletinFactory$$ExternalSyntheticLambda0;
        LinkSpanDrawable.LinksTextView linksTextView5;
        String formatString3;
        RLottieImageView rLottieImageView2;
        int i4;
        LinkSpanDrawable.LinksTextView linksTextView6;
        int i5;
        RLottieImageView rLottieImageView3;
        int i6;
        boolean z;
        TextView textView;
        String formatString4;
        float f;
        int dp;
        LinkSpanDrawable.LinksTextView linksTextView7;
        CharSequence formatPluralString;
        int i7;
        RLottieImageView rLottieImageView4;
        int i8;
        TextView textView2;
        int i9;
        String string2;
        BackupImageView backupImageView;
        StringBuilder sb;
        String str;
        int i10;
        int i11;
        float f2;
        int max;
        int i12;
        float f3;
        CharSequence replaceTags2;
        int i13;
        String formatString5;
        CharSequence charSequence;
        int i14;
        int i15;
        int i16;
        long intValue;
        int i17;
        String string3;
        String string4;
        int i18;
        String formatString6;
        String formatString7;
        String str2;
        int i19;
        String formatString8;
        String formatString9;
        int i20;
        if (!AndroidUtilities.shouldShowClipboardToast() && ((i20 = this.currentAction) == 52 || i20 == 56 || i20 == 57 || i20 == 58 || i20 == 59 || i20 == 60 || i20 == 80 || i20 == 33)) {
            return;
        }
        Runnable runnable3 = this.currentActionRunnable;
        if (runnable3 != null) {
            runnable3.run();
        }
        this.isShown = true;
        this.currentActionRunnable = runnable;
        this.currentCancelRunnable = runnable2;
        this.currentDialogIds = arrayList;
        Long l = (Long) arrayList.get(0);
        long longValue = l.longValue();
        this.currentAction = i;
        this.timeLeft = 5000L;
        this.currentInfoObject = obj;
        this.currentInfoObject2 = obj2;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
        this.undoTextView.setText(LocaleController.getString(R.string.Undo));
        this.undoImageView.setVisibility(0);
        this.leftImageView.setPadding(0, 0, 0, 0);
        this.leftImageView.setScaleX(1.0f);
        this.leftImageView.setScaleY(1.0f);
        this.infoTextView.setTextSize(1, 15.0f);
        this.avatarImageView.setVisibility(8);
        this.infoTextView.setGravity(51);
        ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).leftMargin = AndroidUtilities.dp(58.0f);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.infoTextView.getLayoutParams();
        layoutParams.height = -2;
        layoutParams.topMargin = AndroidUtilities.dp(13.0f);
        layoutParams.bottomMargin = 0;
        this.leftImageView.setScaleType(ImageView.ScaleType.CENTER);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.leftImageView.getLayoutParams();
        layoutParams2.gravity = 19;
        layoutParams2.bottomMargin = 0;
        layoutParams2.topMargin = 0;
        layoutParams2.leftMargin = AndroidUtilities.dp(3.0f);
        layoutParams2.width = AndroidUtilities.dp(54.0f);
        layoutParams2.height = -2;
        this.infoTextView.setMinHeight(0);
        String str3 = null;
        if ((runnable == null && runnable2 == null) || i == ACTION_RINGTONE_ADDED) {
            setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    UndoView.this.lambda$showWithAction$2(view);
                }
            });
            setOnTouchListener(null);
        } else {
            setOnClickListener(null);
            setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda3
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$showWithAction$3;
                    lambda$showWithAction$3 = UndoView.lambda$showWithAction$3(view, motionEvent);
                    return lambda$showWithAction$3;
                }
            });
        }
        this.infoTextView.setMovementMethod(null);
        if (isTooltipAction()) {
            if (i == ACTION_RINGTONE_ADDED) {
                this.subinfoTextView.setSingleLine(false);
                replaceTags2 = LocaleController.getString(R.string.SoundAdded);
                charSequence = AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.SoundAddedSubtitle), runnable);
                this.currentActionRunnable = null;
                i15 = R.raw.sound_download;
                this.timeLeft = 4000L;
            } else {
                if (i == 74) {
                    this.subinfoTextView.setSingleLine(false);
                    replaceTags2 = LocaleController.getString(R.string.ReportChatSent);
                    str3 = LocaleController.formatString("ReportSentInfo", R.string.ReportSentInfo, new Object[0]);
                    i13 = R.raw.ic_admin;
                    intValue = 4000;
                } else {
                    if (i == 34) {
                        TLRPC.User user = (TLRPC.User) obj;
                        SpannableStringBuilder replaceTags3 = AndroidUtilities.replaceTags(ChatObject.isChannelOrGiga((TLRPC.Chat) obj2) ? LocaleController.formatString("VoipChannelInvitedUser", R.string.VoipChannelInvitedUser, UserObject.getFirstName(user)) : LocaleController.formatString("VoipGroupInvitedUser", R.string.VoipGroupInvitedUser, UserObject.getFirstName(user)));
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
                        avatarDrawable.setInfo(this.currentAccount, user);
                        this.avatarImageView.setForUserOrChat(user, avatarDrawable);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                        replaceTags2 = replaceTags3;
                    } else if (i == 44) {
                        TLRPC.Chat chat = (TLRPC.Chat) obj2;
                        if (obj instanceof TLRPC.User) {
                            TLRPC.User user2 = (TLRPC.User) obj;
                            formatString9 = ChatObject.isChannelOrGiga(chat) ? LocaleController.formatString("VoipChannelUserJoined", R.string.VoipChannelUserJoined, UserObject.getFirstName(user2)) : LocaleController.formatString("VoipChatUserJoined", R.string.VoipChatUserJoined, UserObject.getFirstName(user2));
                        } else if (obj instanceof TLRPC.Chat) {
                            TLRPC.Chat chat2 = (TLRPC.Chat) obj;
                            formatString9 = ChatObject.isChannelOrGiga(chat) ? LocaleController.formatString("VoipChannelChatJoined", R.string.VoipChannelChatJoined, chat2.title) : LocaleController.formatString("VoipChatChatJoined", R.string.VoipChatChatJoined, chat2.title);
                        } else {
                            replaceTags2 = "";
                            AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                            avatarDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
                            TLObject tLObject = (TLObject) obj;
                            avatarDrawable2.setInfo(this.currentAccount, tLObject);
                            this.avatarImageView.setForUserOrChat(tLObject, avatarDrawable2);
                            this.avatarImageView.setVisibility(0);
                            this.timeLeft = 3000L;
                        }
                        replaceTags2 = AndroidUtilities.replaceTags(formatString9);
                        AvatarDrawable avatarDrawable22 = new AvatarDrawable();
                        avatarDrawable22.setTextSize(AndroidUtilities.dp(12.0f));
                        TLObject tLObject2 = (TLObject) obj;
                        avatarDrawable22.setInfo(this.currentAccount, tLObject2);
                        this.avatarImageView.setForUserOrChat(tLObject2, avatarDrawable22);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                    } else if (i == 37) {
                        AvatarDrawable avatarDrawable3 = new AvatarDrawable();
                        avatarDrawable3.setTextSize(AndroidUtilities.dp(12.0f));
                        if (obj instanceof TLRPC.User) {
                            TLRPC.User user3 = (TLRPC.User) obj;
                            avatarDrawable3.setInfo(this.currentAccount, user3);
                            this.avatarImageView.setForUserOrChat(user3, avatarDrawable3);
                            str2 = ContactsController.formatName(user3.first_name, user3.last_name);
                        } else {
                            TLRPC.Chat chat3 = (TLRPC.Chat) obj;
                            avatarDrawable3.setInfo(this.currentAccount, chat3);
                            this.avatarImageView.setForUserOrChat(chat3, avatarDrawable3);
                            str2 = chat3.title;
                        }
                        if (ChatObject.isChannelOrGiga((TLRPC.Chat) obj2)) {
                            i19 = 0;
                            formatString8 = LocaleController.formatString("VoipChannelUserChanged", R.string.VoipChannelUserChanged, str2);
                        } else {
                            i19 = 0;
                            formatString8 = LocaleController.formatString("VoipGroupUserChanged", R.string.VoipGroupUserChanged, str2);
                        }
                        replaceTags2 = AndroidUtilities.replaceTags(formatString8);
                        this.avatarImageView.setVisibility(i19);
                        this.timeLeft = 3000L;
                    } else {
                        if (i == 33) {
                            replaceTags2 = LocaleController.getString(R.string.VoipGroupCopyInviteLinkCopied);
                            i13 = R.raw.voip_invite;
                            this.timeLeft = 3000L;
                        } else if (i == 77) {
                            replaceTags2 = (CharSequence) obj;
                            i13 = R.raw.payment_success;
                            this.timeLeft = 5000L;
                            if (this.parentFragment != null && (obj2 instanceof TLRPC.Message)) {
                                final TLRPC.Message message = (TLRPC.Message) obj2;
                                setOnTouchListener(null);
                                this.infoTextView.setMovementMethod(null);
                                setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda4
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        UndoView.this.lambda$showWithAction$6(message, view);
                                    }
                                });
                            }
                        } else {
                            if (i == 30) {
                                formatString7 = LocaleController.formatString("VoipGroupUserCantNowSpeak", R.string.VoipGroupUserCantNowSpeak, obj instanceof TLRPC.User ? UserObject.getFirstName((TLRPC.User) obj) : ((TLRPC.Chat) obj).title);
                            } else if (i == 35) {
                                formatString7 = LocaleController.formatString("VoipGroupUserCantNowSpeakForYou", R.string.VoipGroupUserCantNowSpeakForYou, obj instanceof TLRPC.User ? UserObject.getFirstName((TLRPC.User) obj) : obj instanceof TLRPC.Chat ? ((TLRPC.Chat) obj).title : "");
                            } else {
                                if (i == 31) {
                                    formatString6 = LocaleController.formatString("VoipGroupUserCanNowSpeak", R.string.VoipGroupUserCanNowSpeak, obj instanceof TLRPC.User ? UserObject.getFirstName((TLRPC.User) obj) : ((TLRPC.Chat) obj).title);
                                } else {
                                    if (i == 38) {
                                        replaceTags2 = AndroidUtilities.replaceTags(obj instanceof TLRPC.Chat ? LocaleController.formatString("VoipGroupYouCanNowSpeakIn", R.string.VoipGroupYouCanNowSpeakIn, ((TLRPC.Chat) obj).title) : LocaleController.getString(R.string.VoipGroupYouCanNowSpeak));
                                        i13 = R.raw.voip_allow_talk;
                                    } else if (i == 42) {
                                        replaceTags2 = AndroidUtilities.replaceTags(LocaleController.getString(ChatObject.isChannelOrGiga((TLRPC.Chat) obj) ? R.string.VoipChannelSoundMuted : R.string.VoipGroupSoundMuted));
                                        i13 = R.raw.ic_mute;
                                    } else if (i == 43) {
                                        replaceTags2 = AndroidUtilities.replaceTags(LocaleController.getString(ChatObject.isChannelOrGiga((TLRPC.Chat) obj) ? R.string.VoipChannelSoundUnmuted : R.string.VoipGroupSoundUnmuted));
                                        i13 = R.raw.ic_unmute;
                                    } else {
                                        int i21 = this.currentAction;
                                        if (i21 == 39 || i21 == 100) {
                                            replaceTags2 = AndroidUtilities.replaceTags(LocaleController.getString(i21 == 39 ? R.string.VoipGroupAudioRecordStarted : R.string.VoipGroupVideoRecordStarted));
                                            i13 = R.raw.voip_record_start;
                                        } else if (i21 == 40 || i21 == 101) {
                                            String string5 = LocaleController.getString(i21 == 40 ? R.string.VoipGroupAudioRecordSaved : R.string.VoipGroupVideoRecordSaved);
                                            i13 = R.raw.voip_record_saved;
                                            this.timeLeft = 4000L;
                                            this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string5);
                                            int indexOf = string5.indexOf("**");
                                            int lastIndexOf = string5.lastIndexOf("**");
                                            if (indexOf >= 0 && lastIndexOf >= 0 && indexOf != lastIndexOf) {
                                                spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 2, (CharSequence) "");
                                                spannableStringBuilder.replace(indexOf, indexOf + 2, (CharSequence) "");
                                                try {
                                                    spannableStringBuilder.setSpan(new URLSpanNoUnderline("tg://openmessage?user_id=" + UserConfig.getInstance(this.currentAccount).getClientUserId()), indexOf, lastIndexOf - 2, 33);
                                                } catch (Exception e) {
                                                    FileLog.e(e);
                                                }
                                            }
                                            replaceTags2 = spannableStringBuilder;
                                        } else if (i == 36) {
                                            formatString6 = LocaleController.formatString("VoipGroupUserCanNowSpeakForYou", R.string.VoipGroupUserCanNowSpeakForYou, obj instanceof TLRPC.User ? UserObject.getFirstName((TLRPC.User) obj) : ((TLRPC.Chat) obj).title);
                                        } else if (i == 32) {
                                            replaceTags2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupRemovedFromGroup", R.string.VoipGroupRemovedFromGroup, obj instanceof TLRPC.User ? UserObject.getFirstName((TLRPC.User) obj) : ((TLRPC.Chat) obj).title));
                                            i13 = R.raw.voip_group_removed;
                                        } else {
                                            if (i == 9 || i == 10) {
                                                TLRPC.User user4 = (TLRPC.User) obj;
                                                replaceTags2 = AndroidUtilities.replaceTags(i == 9 ? LocaleController.formatString("EditAdminTransferChannelToast", R.string.EditAdminTransferChannelToast, UserObject.getFirstName(user4)) : LocaleController.formatString("EditAdminTransferGroupToast", R.string.EditAdminTransferGroupToast, UserObject.getFirstName(user4)));
                                            } else if (i == 8) {
                                                replaceTags2 = LocaleController.formatString("NowInContacts", R.string.NowInContacts, UserObject.getFirstName((TLRPC.User) obj));
                                                charSequence = null;
                                                i15 = R.raw.contact_check;
                                            } else if (i == 87) {
                                                replaceTags2 = LocaleController.formatString(R.string.ProxyAddedSuccess, new Object[0]);
                                            } else {
                                                if (i == 22) {
                                                    if (DialogObject.isUserDialog(longValue)) {
                                                        i18 = obj == null ? R.string.MainProfilePhotoSetHint : R.string.MainProfileVideoSetHint;
                                                    } else {
                                                        TLRPC.Chat chat4 = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-longValue));
                                                        i18 = (!ChatObject.isChannel(chat4) || chat4.megagroup) ? obj == null ? R.string.MainGroupProfilePhotoSetHint : R.string.MainGroupProfileVideoSetHint : obj == null ? R.string.MainChannelProfilePhotoSetHint : R.string.MainChannelProfileVideoSetHint;
                                                    }
                                                } else if (i == 23) {
                                                    i18 = R.string.ChatWasMovedToMainList;
                                                } else {
                                                    if (i == 6) {
                                                        replaceTags2 = LocaleController.getString(R.string.ArchiveHidden);
                                                        String string6 = LocaleController.getString(R.string.ArchiveHiddenInfo);
                                                        i15 = R.raw.chats_swipearchive;
                                                        charSequence = string6;
                                                        i14 = 48;
                                                    } else {
                                                        if (i21 == 13) {
                                                            string3 = LocaleController.getString(R.string.QuizWellDone);
                                                            string4 = LocaleController.getString(R.string.QuizWellDoneInfo);
                                                            i15 = R.raw.wallet_congrats;
                                                        } else if (i21 == 14) {
                                                            string3 = LocaleController.getString(R.string.QuizWrongAnswer);
                                                            string4 = LocaleController.getString(R.string.QuizWrongAnswerInfo);
                                                            i15 = R.raw.wallet_science;
                                                        } else if (i == 7) {
                                                            replaceTags2 = LocaleController.getString(R.string.ArchivePinned);
                                                            if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                                                i17 = R.string.ArchivePinnedInfo;
                                                                str3 = LocaleController.getString(i17);
                                                            }
                                                            i13 = R.raw.chats_infotip;
                                                        } else if (i == 20 || i == 21) {
                                                            MessagesController.DialogFilter dialogFilter = (MessagesController.DialogFilter) obj2;
                                                            if (longValue != 0) {
                                                                if (DialogObject.isEncryptedDialog(longValue)) {
                                                                    longValue = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue))).user_id;
                                                                }
                                                                if (DialogObject.isUserDialog(longValue)) {
                                                                    TLRPC.User user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                                                                    String firstName = UserObject.getFirstName(user5);
                                                                    if (UserObject.isUserSelf(user5)) {
                                                                        i16 = R.string.SavedMessages;
                                                                    } else {
                                                                        if (UserObject.isReplyUser(user5)) {
                                                                            i16 = R.string.RepliesTitle;
                                                                        }
                                                                        formatString5 = i != 20 ? LocaleController.formatString("FilterUserAddedToExisting", R.string.FilterUserAddedToExisting, firstName, dialogFilter.name) : LocaleController.formatString("FilterUserRemovedFrom", R.string.FilterUserRemovedFrom, firstName, dialogFilter.name);
                                                                    }
                                                                    firstName = LocaleController.getString(i16);
                                                                    if (i != 20) {
                                                                    }
                                                                } else {
                                                                    TLRPC.Chat chat5 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                                                    formatString5 = i == 20 ? LocaleController.formatString("FilterChatAddedToExisting", R.string.FilterChatAddedToExisting, chat5.title, dialogFilter.name) : LocaleController.formatString("FilterChatRemovedFrom", R.string.FilterChatRemovedFrom, chat5.title, dialogFilter.name);
                                                                }
                                                            } else {
                                                                formatString5 = i == 20 ? LocaleController.formatString("FilterChatsAddedToExisting", R.string.FilterChatsAddedToExisting, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name) : LocaleController.formatString("FilterChatsRemovedFrom", R.string.FilterChatsRemovedFrom, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name);
                                                            }
                                                            SpannableStringBuilder replaceTags4 = AndroidUtilities.replaceTags(formatString5);
                                                            charSequence = null;
                                                            i14 = 36;
                                                            i15 = i == 20 ? R.raw.folder_in : R.raw.folder_out;
                                                            replaceTags2 = replaceTags4;
                                                        } else if (i == 19) {
                                                            replaceTags2 = this.infoText;
                                                            i13 = R.raw.ic_delete;
                                                        } else if (i == 82) {
                                                            replaceTags2 = LocaleController.getString(((MediaController.PhotoEntry) obj).isVideo ? R.string.AttachMediaVideoDeselected : R.string.AttachMediaPhotoDeselected);
                                                        } else if (i == 78 || i == 79) {
                                                            int intValue2 = ((Integer) obj).intValue();
                                                            replaceTags2 = i == 78 ? LocaleController.formatPluralString("PinnedDialogsCount", intValue2, new Object[0]) : LocaleController.formatPluralString("UnpinnedDialogsCount", intValue2, new Object[0]);
                                                            i13 = this.currentAction == 78 ? R.raw.ic_pin : R.raw.ic_unpin;
                                                            if (obj2 instanceof Integer) {
                                                                intValue = ((Integer) obj2).intValue();
                                                            }
                                                        } else {
                                                            replaceTags2 = LocaleController.getString(i == 3 ? R.string.ChatArchived : R.string.ChatsArchived);
                                                            if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                                                i17 = R.string.ChatArchivedInfo;
                                                                str3 = LocaleController.getString(i17);
                                                            }
                                                            i13 = R.raw.chats_infotip;
                                                        }
                                                        replaceTags2 = string3;
                                                        charSequence = string4;
                                                        i14 = 44;
                                                    }
                                                    this.infoTextView.setText(replaceTags2);
                                                    RLottieImageView rLottieImageView5 = this.leftImageView;
                                                    if (i15 == 0) {
                                                        rLottieImageView5.setAnimation(i15, i14, i14);
                                                        RLottieDrawable animatedDrawable = this.leftImageView.getAnimatedDrawable();
                                                        animatedDrawable.setPlayInDirectionOfCustomEndFrame(false);
                                                        animatedDrawable.setCustomEndFrame(animatedDrawable.getFramesCount());
                                                        this.leftImageView.setVisibility(0);
                                                        this.leftImageView.setProgress(0.0f);
                                                        this.leftImageView.playAnimation();
                                                    } else {
                                                        rLottieImageView5.setVisibility(8);
                                                    }
                                                    if (charSequence == null) {
                                                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                                                        layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                                                        ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(8.0f);
                                                        this.subinfoTextView.setText(charSequence);
                                                        this.subinfoTextView.setVisibility(0);
                                                        this.infoTextView.setTextSize(1, 14.0f);
                                                        this.infoTextView.setTypeface(AndroidUtilities.bold());
                                                        i10 = 8;
                                                    } else {
                                                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                                        layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                                                        layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                                                        i10 = 8;
                                                        this.subinfoTextView.setVisibility(8);
                                                        this.infoTextView.setTextSize(1, 15.0f);
                                                        this.infoTextView.setTypeface(Typeface.DEFAULT);
                                                    }
                                                }
                                                replaceTags2 = LocaleController.getString(i18);
                                            }
                                            i13 = R.raw.contact_check;
                                        }
                                    }
                                    intValue = 3000;
                                }
                                replaceTags2 = AndroidUtilities.replaceTags(formatString6);
                                i13 = R.raw.voip_unmuted;
                                intValue = 3000;
                            }
                            replaceTags2 = AndroidUtilities.replaceTags(formatString7);
                            i13 = R.raw.voip_muted;
                            intValue = 3000;
                        }
                        i15 = i13;
                        charSequence = str3;
                    }
                    charSequence = null;
                    i15 = 0;
                }
                this.timeLeft = intValue;
                i15 = i13;
                charSequence = str3;
            }
            i14 = 36;
            this.infoTextView.setText(replaceTags2);
            RLottieImageView rLottieImageView52 = this.leftImageView;
            if (i15 == 0) {
            }
            if (charSequence == null) {
            }
        } else {
            int i22 = this.currentAction;
            if (i22 == 45 || i22 == 46 || i22 == 47 || i22 == 52 || i22 == 53 || i22 == 54 || i22 == 55 || i22 == 56 || i22 == 57 || i22 == 58 || i22 == 59 || i22 == 60 || i22 == 71 || i22 == 70 || i22 == 75 || i22 == 76 || i22 == 41 || i22 == 78 || i22 == 79 || i22 == 61 || i22 == 80) {
                this.undoImageView.setVisibility(8);
                this.leftImageView.setVisibility(0);
                this.infoTextView.setTypeface(Typeface.DEFAULT);
                int i23 = this.currentAction;
                long j2 = -1;
                if (i23 == 76) {
                    this.infoTextView.setText(LocaleController.getString(R.string.BroadcastGroupConvertSuccess));
                    rLottieImageView3 = this.leftImageView;
                    i6 = R.raw.gigagroup_convert;
                } else if (i23 == 75) {
                    this.infoTextView.setText(LocaleController.getString(R.string.GigagroupConvertCancelHint));
                    rLottieImageView3 = this.leftImageView;
                    i6 = R.raw.chats_infotip;
                } else {
                    if (i == 70) {
                        int intValue3 = ((Integer) obj2).intValue();
                        this.subinfoTextView.setSingleLine(false);
                        this.infoTextView.setText(LocaleController.formatString("AutoDeleteHintOnText", R.string.AutoDeleteHintOnText, LocaleController.formatTTLString(intValue3)));
                        this.leftImageView.setAnimation(R.raw.fire_on, 36, 36);
                        layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                        this.timeLeft = 4000L;
                        this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
                        z = true;
                    } else {
                        if (i23 == 71) {
                            this.infoTextView.setText(LocaleController.getString(R.string.AutoDeleteHintOffText));
                            this.leftImageView.setAnimation(R.raw.fire_off, 36, 36);
                            this.infoTextView.setTextSize(1, 14.0f);
                            this.timeLeft = 3000L;
                            this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(4.0f));
                        } else {
                            if (i23 == 45) {
                                linksTextView6 = this.infoTextView;
                                i5 = R.string.ImportMutualError;
                            } else if (i23 == 46) {
                                linksTextView6 = this.infoTextView;
                                i5 = R.string.ImportNotAdmin;
                            } else if (i23 == 47) {
                                this.infoTextView.setText(LocaleController.getString(R.string.ImportedInfo));
                                this.leftImageView.setAnimation(R.raw.imported, 36, 36);
                                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0f));
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                                z = true;
                            } else {
                                if (i23 == 52 || i23 == 56 || i23 == 57 || i23 == 58 || i23 == 59 || i23 == 60 || i23 == 80) {
                                    if (!AndroidUtilities.shouldShowClipboardToast()) {
                                        return;
                                    }
                                    int i24 = R.raw.copy;
                                    int i25 = this.currentAction;
                                    if (i25 == 80) {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.EmailCopied;
                                    } else if (i25 == 60) {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.PhoneCopied;
                                    } else if (i25 == 56) {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.UsernameCopied;
                                    } else if (i25 == 57) {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.HashtagCopied;
                                    } else if (i25 == 52) {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.MessageCopied;
                                    } else if (i25 == 59) {
                                        i24 = R.raw.voip_invite;
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.LinkCopied;
                                    } else {
                                        linksTextView = this.infoTextView;
                                        i2 = R.string.TextCopied;
                                    }
                                    linksTextView.setText(LocaleController.getString(i2));
                                    this.leftImageView.setAnimation(i24, 30, 30);
                                } else {
                                    if (i23 == 54) {
                                        this.infoTextView.setText(LocaleController.getString(R.string.ChannelNotifyMembersInfoOn));
                                        rLottieImageView2 = this.leftImageView;
                                        i4 = R.raw.silent_unmute;
                                    } else if (i23 == 55) {
                                        this.infoTextView.setText(LocaleController.getString(R.string.ChannelNotifyMembersInfoOff));
                                        rLottieImageView2 = this.leftImageView;
                                        i4 = R.raw.silent_mute;
                                    } else {
                                        if (i23 == 41) {
                                            if (obj2 != null) {
                                                int intValue4 = ((Integer) obj2).intValue();
                                                linksTextView5 = this.infoTextView;
                                                formatString3 = LocaleController.formatString("InvLinkToChats", R.string.InvLinkToChats, LocaleController.formatPluralString("Chats", intValue4, new Object[0]));
                                            } else if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.InvLinkToSavedMessages)));
                                                this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                                                j = 3000;
                                            } else if (DialogObject.isChatDialog(longValue)) {
                                                TLRPC.Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                                linksTextView5 = this.infoTextView;
                                                formatString3 = LocaleController.formatString("InvLinkToGroup", R.string.InvLinkToGroup, chat6.title);
                                            } else {
                                                TLRPC.User user6 = MessagesController.getInstance(this.currentAccount).getUser(l);
                                                linksTextView5 = this.infoTextView;
                                                formatString3 = LocaleController.formatString("InvLinkToUser", R.string.InvLinkToUser, UserObject.getFirstName(user6));
                                            }
                                            linksTextView5.setText(AndroidUtilities.replaceTags(formatString3));
                                            this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                                            j = 3000;
                                        } else {
                                            if (i23 == 53) {
                                                Integer num = (Integer) obj;
                                                if (obj2 != null && !(obj2 instanceof TLRPC.TL_forumTopic)) {
                                                    int intValue5 = ((Integer) obj2).intValue();
                                                    if (num.intValue() == 1) {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatPluralString("FwdMessageToManyChats", intValue5, new Object[0]);
                                                    } else {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatPluralString("FwdMessagesToManyChats", intValue5, new Object[0]);
                                                    }
                                                } else if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                    if (num.intValue() == 1) {
                                                        linksTextView3 = this.infoTextView;
                                                        string = LocaleController.getString(R.string.FwdMessageToSavedMessages);
                                                        bulletinFactory$$ExternalSyntheticLambda0 = new BulletinFactory$$ExternalSyntheticLambda0();
                                                    } else {
                                                        linksTextView3 = this.infoTextView;
                                                        string = LocaleController.getString(R.string.FwdMessagesToSavedMessages);
                                                        bulletinFactory$$ExternalSyntheticLambda0 = new BulletinFactory$$ExternalSyntheticLambda0();
                                                    }
                                                    replaceTags = AndroidUtilities.replaceSingleTag(string, bulletinFactory$$ExternalSyntheticLambda0);
                                                } else if (DialogObject.isChatDialog(longValue)) {
                                                    TLRPC.Chat chat7 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                                    TLRPC.TL_forumTopic tL_forumTopic = (TLRPC.TL_forumTopic) obj2;
                                                    if (num.intValue() == 1) {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatString("FwdMessageToGroup", R.string.FwdMessageToGroup, tL_forumTopic != null ? tL_forumTopic.title : chat7.title);
                                                    } else {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatString("FwdMessagesToGroup", R.string.FwdMessagesToGroup, tL_forumTopic != null ? tL_forumTopic.title : chat7.title);
                                                    }
                                                } else {
                                                    TLRPC.User user7 = MessagesController.getInstance(this.currentAccount).getUser(l);
                                                    if (num.intValue() == 1) {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatString("FwdMessageToUser", R.string.FwdMessageToUser, UserObject.getFirstName(user7));
                                                    } else {
                                                        linksTextView4 = this.infoTextView;
                                                        formatString2 = LocaleController.formatString("FwdMessagesToUser", R.string.FwdMessagesToUser, UserObject.getFirstName(user7));
                                                    }
                                                }
                                                linksTextView4.setText(AndroidUtilities.replaceTags(formatString2));
                                                this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                                                j2 = 300;
                                                j = 3000;
                                            } else if (i23 == 61) {
                                                if (obj2 != null) {
                                                    int intValue6 = ((Integer) obj2).intValue();
                                                    linksTextView2 = this.infoTextView;
                                                    formatString = LocaleController.formatString("BackgroundToChats", R.string.BackgroundToChats, LocaleController.formatPluralString("Chats", intValue6, new Object[0]));
                                                } else if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                    linksTextView3 = this.infoTextView;
                                                    replaceTags = AndroidUtilities.replaceTags(LocaleController.getString(R.string.BackgroundToSavedMessages));
                                                } else if (DialogObject.isChatDialog(longValue)) {
                                                    TLRPC.Chat chat8 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                                    linksTextView2 = this.infoTextView;
                                                    formatString = LocaleController.formatString("BackgroundToGroup", R.string.BackgroundToGroup, chat8.title);
                                                } else {
                                                    TLRPC.User user8 = MessagesController.getInstance(this.currentAccount).getUser(l);
                                                    linksTextView2 = this.infoTextView;
                                                    formatString = LocaleController.formatString("BackgroundToUser", R.string.BackgroundToUser, UserObject.getFirstName(user8));
                                                }
                                                linksTextView2.setText(AndroidUtilities.replaceTags(formatString));
                                                rLottieImageView = this.leftImageView;
                                                i3 = R.raw.forward;
                                                rLottieImageView.setAnimation(i3, 30, 30);
                                                j = 3000;
                                            }
                                            linksTextView3.setText(replaceTags);
                                            rLottieImageView = this.leftImageView;
                                            i3 = R.raw.saved_messages;
                                            rLottieImageView.setAnimation(i3, 30, 30);
                                            j = 3000;
                                        }
                                        this.timeLeft = j;
                                    }
                                    rLottieImageView2.setAnimation(i4, 30, 30);
                                }
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            }
                            linksTextView6.setText(LocaleController.getString(i5));
                            rLottieImageView3 = this.leftImageView;
                            i6 = R.raw.error;
                        }
                        z = false;
                    }
                    this.subinfoTextView.setVisibility(8);
                    this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                    this.undoButton.setVisibility(8);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                    if (j2 > 0) {
                        this.leftImageView.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                UndoView.this.lambda$showWithAction$7();
                            }
                        }, j2);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append((Object) this.infoTextView.getText());
                    sb2.append(this.subinfoTextView.getVisibility() == 0 ? ". " + ((Object) this.subinfoTextView.getText()) : "");
                    AndroidUtilities.makeAccessibilityAnnouncement(sb2.toString());
                    if (isMultilineSubInfo()) {
                        int measuredWidth = ((ViewGroup) getParent()).getMeasuredWidth();
                        if (measuredWidth == 0) {
                            measuredWidth = AndroidUtilities.displaySize.x;
                        }
                        measureChildWithMargins(this.subinfoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                        max = this.subinfoTextView.getMeasuredHeight() + AndroidUtilities.dp(37.0f);
                    } else {
                        if (!hasSubInfo()) {
                            if (getParent() instanceof ViewGroup) {
                                ViewGroup viewGroup = (ViewGroup) getParent();
                                int measuredWidth2 = (viewGroup.getMeasuredWidth() - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight();
                                if (measuredWidth2 <= 0) {
                                    measuredWidth2 = AndroidUtilities.displaySize.x;
                                }
                                measureChildWithMargins(this.infoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth2 - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                                int measuredHeight = this.infoTextView.getMeasuredHeight();
                                int i26 = this.currentAction;
                                int dp2 = measuredHeight + AndroidUtilities.dp((i26 == 16 || i26 == 17 || i26 == 18 || i26 == 84 || i26 == 86) ? 14.0f : 28.0f);
                                this.undoViewHeight = dp2;
                                int i27 = this.currentAction;
                                if (i27 == 18) {
                                    f2 = 52.0f;
                                } else if (i27 == 25) {
                                    f2 = 50.0f;
                                } else if (z) {
                                    this.undoViewHeight = dp2 - AndroidUtilities.dp(8.0f);
                                }
                                max = Math.max(dp2, AndroidUtilities.dp(f2));
                            }
                            if (getVisibility() == 0) {
                                setVisibility(0);
                                setEnterOffset((this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight));
                                AnimatorSet animatorSet = new AnimatorSet();
                                boolean z2 = this.fromTop;
                                float f4 = (z2 ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight);
                                if (z2) {
                                    i12 = 2;
                                    f3 = 1.0f;
                                } else {
                                    i12 = 2;
                                    f3 = -1.0f;
                                }
                                float[] fArr = new float[i12];
                                fArr[0] = f4;
                                fArr[1] = f3;
                                animatorSet.playTogether(ObjectAnimator.ofFloat(this, "enterOffset", fArr));
                                animatorSet.setInterpolator(new DecelerateInterpolator());
                                animatorSet.setDuration(180L);
                                animatorSet.start();
                                return;
                            }
                            return;
                        }
                        max = AndroidUtilities.dp(52.0f);
                    }
                    this.undoViewHeight = max;
                    if (getVisibility() == 0) {
                    }
                }
                rLottieImageView3.setAnimation(i6, 36, 36);
                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                this.infoTextView.setTextSize(1, 14.0f);
                z = true;
                this.subinfoTextView.setVisibility(8);
                this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                this.undoButton.setVisibility(8);
                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                this.leftImageView.setProgress(0.0f);
                this.leftImageView.playAnimation();
                if (j2 > 0) {
                }
                StringBuilder sb22 = new StringBuilder();
                sb22.append((Object) this.infoTextView.getText());
                if (this.subinfoTextView.getVisibility() == 0) {
                }
                sb22.append(this.subinfoTextView.getVisibility() == 0 ? ". " + ((Object) this.subinfoTextView.getText()) : "");
                AndroidUtilities.makeAccessibilityAnnouncement(sb22.toString());
                if (isMultilineSubInfo()) {
                }
                this.undoViewHeight = max;
                if (getVisibility() == 0) {
                }
            } else {
                if (i22 == 24 || i22 == 25) {
                    int intValue7 = ((Integer) obj).intValue();
                    TLRPC.User user9 = (TLRPC.User) obj2;
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    if (intValue7 != 0) {
                        this.infoTextView.setTypeface(AndroidUtilities.bold());
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.leftImageView.clearLayerColors();
                        RLottieImageView rLottieImageView6 = this.leftImageView;
                        int i28 = Theme.key_undo_infoColor;
                        rLottieImageView6.setLayerColor("BODY.**", getThemedColor(i28));
                        this.leftImageView.setLayerColor("Wibe Big.**", getThemedColor(i28));
                        this.leftImageView.setLayerColor("Wibe Big 3.**", getThemedColor(i28));
                        this.leftImageView.setLayerColor("Wibe Small.**", getThemedColor(i28));
                        this.infoTextView.setText(LocaleController.getString(R.string.ProximityAlertSet));
                        this.leftImageView.setAnimation(R.raw.ic_unmute, 28, 28);
                        this.subinfoTextView.setVisibility(0);
                        this.subinfoTextView.setSingleLine(false);
                        this.subinfoTextView.setMaxLines(3);
                        if (user9 != null) {
                            textView = this.subinfoTextView;
                            formatString4 = LocaleController.formatString("ProximityAlertSetInfoUser", R.string.ProximityAlertSetInfoUser, UserObject.getFirstName(user9), LocaleController.formatDistance(intValue7, 2));
                        } else {
                            textView = this.subinfoTextView;
                            formatString4 = LocaleController.formatString("ProximityAlertSetInfoGroup2", R.string.ProximityAlertSetInfoGroup2, LocaleController.formatDistance(intValue7, 2));
                        }
                        textView.setText(formatString4);
                        this.undoButton.setVisibility(8);
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    } else {
                        this.infoTextView.setTypeface(Typeface.DEFAULT);
                        this.infoTextView.setTextSize(1, 15.0f);
                        this.leftImageView.clearLayerColors();
                        RLottieImageView rLottieImageView7 = this.leftImageView;
                        int i29 = Theme.key_undo_infoColor;
                        rLottieImageView7.setLayerColor("Body Main.**", getThemedColor(i29));
                        this.leftImageView.setLayerColor("Body Top.**", getThemedColor(i29));
                        this.leftImageView.setLayerColor("Line.**", getThemedColor(i29));
                        this.leftImageView.setLayerColor("Curve Big.**", getThemedColor(i29));
                        this.leftImageView.setLayerColor("Curve Small.**", getThemedColor(i29));
                        layoutParams.topMargin = AndroidUtilities.dp(14.0f);
                        this.infoTextView.setText(LocaleController.getString(R.string.ProximityAlertCancelled));
                        this.leftImageView.setAnimation(R.raw.ic_mute, 28, 28);
                        this.subinfoTextView.setVisibility(8);
                        this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                        this.undoButton.setVisibility(0);
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                } else {
                    if (i22 == 11) {
                        this.infoTextView.setText(LocaleController.getString(R.string.AuthAnotherClientOk));
                        this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                        this.subinfoTextView.setText(((TLRPC.TL_authorization) obj).app_name);
                        this.subinfoTextView.setVisibility(0);
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.infoTextView.setTypeface(AndroidUtilities.bold());
                        this.undoTextView.setTextColor(getThemedColor(Theme.key_text_RedRegular));
                        this.undoImageView.setVisibility(8);
                        i11 = 0;
                        this.undoButton.setVisibility(0);
                    } else if (i22 == 15) {
                        this.timeLeft = 10000L;
                        this.undoTextView.setText(LocaleController.getString(R.string.Open));
                        this.infoTextView.setText(LocaleController.getString(R.string.FilterAvailableTitle));
                        this.leftImageView.setAnimation(R.raw.filter_new, 36, 36);
                        int ceil = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.rightMargin = ceil;
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                        ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = ceil;
                        String string7 = LocaleController.getString(R.string.FilterAvailableText);
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(string7);
                        int indexOf2 = string7.indexOf(42);
                        int lastIndexOf2 = string7.lastIndexOf(42);
                        if (indexOf2 >= 0 && lastIndexOf2 >= 0 && indexOf2 != lastIndexOf2) {
                            spannableStringBuilder2.replace(lastIndexOf2, lastIndexOf2 + 1, (CharSequence) "");
                            spannableStringBuilder2.replace(indexOf2, indexOf2 + 1, (CharSequence) "");
                            spannableStringBuilder2.setSpan(new URLSpanNoUnderline("tg://settings/folders"), indexOf2, lastIndexOf2 - 1, 33);
                        }
                        this.subinfoTextView.setText(spannableStringBuilder2);
                        i11 = 0;
                        this.subinfoTextView.setVisibility(0);
                        this.subinfoTextView.setSingleLine(false);
                        this.subinfoTextView.setMaxLines(2);
                        this.undoButton.setVisibility(0);
                        this.undoImageView.setVisibility(8);
                    } else {
                        if (i22 == 16 || i22 == 17) {
                            this.timeLeft = 4000L;
                            this.infoTextView.setTextSize(1, 14.0f);
                            this.infoTextView.setGravity(16);
                            this.infoTextView.setMinHeight(AndroidUtilities.dp(30.0f));
                            String str4 = (String) obj;
                            if ("🎲".equals(str4)) {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.DiceInfo2)));
                                this.leftImageView.setImageResource(R.drawable.dice);
                            } else {
                                if ("🎯".equals(str4)) {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.DartInfo)));
                                } else {
                                    String serverString = LocaleController.getServerString("DiceEmojiInfo_" + str4);
                                    if (TextUtils.isEmpty(serverString)) {
                                        f = 14.0f;
                                        this.infoTextView.setText(Emoji.replaceEmoji((CharSequence) LocaleController.formatString("DiceEmojiInfo", R.string.DiceEmojiInfo, str4), this.infoTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                        this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str4));
                                        this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                        layoutParams.topMargin = AndroidUtilities.dp(f);
                                        layoutParams.bottomMargin = AndroidUtilities.dp(f);
                                        layoutParams2.leftMargin = AndroidUtilities.dp(f);
                                        layoutParams2.width = AndroidUtilities.dp(26.0f);
                                        layoutParams2.height = AndroidUtilities.dp(26.0f);
                                    } else {
                                        LinkSpanDrawable.LinksTextView linksTextView8 = this.infoTextView;
                                        linksTextView8.setText(Emoji.replaceEmoji((CharSequence) serverString, linksTextView8.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                    }
                                }
                                f = 14.0f;
                                this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str4));
                                this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                layoutParams.topMargin = AndroidUtilities.dp(f);
                                layoutParams.bottomMargin = AndroidUtilities.dp(f);
                                layoutParams2.leftMargin = AndroidUtilities.dp(f);
                                layoutParams2.width = AndroidUtilities.dp(26.0f);
                                layoutParams2.height = AndroidUtilities.dp(26.0f);
                            }
                            this.undoTextView.setText(LocaleController.getString(R.string.SendDice));
                            if (this.currentAction == 16) {
                                dp = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                                this.undoTextView.setVisibility(0);
                                this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                                this.undoImageView.setVisibility(8);
                                this.undoButton.setVisibility(0);
                            } else {
                                dp = AndroidUtilities.dp(8.0f);
                                this.undoTextView.setVisibility(8);
                                this.undoButton.setVisibility(8);
                            }
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = dp;
                            layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                            layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                            layoutParams.height = -1;
                            this.subinfoTextView.setVisibility(8);
                            this.leftImageView.setVisibility(0);
                        } else if (i22 == 18) {
                            this.timeLeft = Math.max(4000, Math.min((r0.length() / 50) * 1600, 10000));
                            this.infoTextView.setTextSize(1, 14.0f);
                            this.infoTextView.setGravity(16);
                            this.infoTextView.setText((CharSequence) obj);
                            this.undoTextView.setVisibility(8);
                            this.undoButton.setVisibility(8);
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                            layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                            layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                            layoutParams.height = -1;
                            layoutParams2.gravity = 51;
                            int dp3 = AndroidUtilities.dp(8.0f);
                            layoutParams2.bottomMargin = dp3;
                            layoutParams2.topMargin = dp3;
                            this.leftImageView.setVisibility(0);
                            this.leftImageView.setAnimation(R.raw.chats_infotip, 36, 36);
                            this.leftImageView.setProgress(0.0f);
                            this.leftImageView.playAnimation();
                            this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                        } else if (i22 == 12) {
                            this.infoTextView.setText(LocaleController.getString(R.string.ColorThemeChanged));
                            this.leftImageView.setImageResource(R.drawable.toast_pallete);
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = AndroidUtilities.dp(48.0f);
                            layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                            ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(48.0f);
                            String string8 = LocaleController.getString(R.string.ColorThemeChangedInfo);
                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(string8);
                            int indexOf3 = string8.indexOf(42);
                            int lastIndexOf3 = string8.lastIndexOf(42);
                            if (indexOf3 >= 0 && lastIndexOf3 >= 0 && indexOf3 != lastIndexOf3) {
                                spannableStringBuilder3.replace(lastIndexOf3, lastIndexOf3 + 1, (CharSequence) "");
                                spannableStringBuilder3.replace(indexOf3, indexOf3 + 1, (CharSequence) "");
                                spannableStringBuilder3.setSpan(new URLSpanNoUnderline("tg://settings/themes"), indexOf3, lastIndexOf3 - 1, 33);
                            }
                            this.subinfoTextView.setText(spannableStringBuilder3);
                            this.subinfoTextView.setVisibility(0);
                            this.subinfoTextView.setSingleLine(false);
                            this.subinfoTextView.setMaxLines(2);
                            this.undoTextView.setVisibility(8);
                            this.undoButton.setVisibility(0);
                            this.leftImageView.setVisibility(0);
                        } else if (i22 == 84) {
                            this.infoTextView.setVisibility(0);
                            this.infoTextView.setTextSize(1, 15.0f);
                            this.infoTextView.setTypeface(Typeface.DEFAULT);
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.UnlockPremiumTranscriptionHint)));
                            this.leftImageView.setVisibility(0);
                            this.leftImageView.setAnimation(R.raw.voice_to_text, 36, 36);
                            this.leftImageView.setProgress(0.0f);
                            this.leftImageView.playAnimation();
                            this.undoTextView.setText(LocaleController.getString(R.string.PremiumMore));
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                            int dp4 = AndroidUtilities.dp(6.0f);
                            layoutParams.bottomMargin = dp4;
                            layoutParams.topMargin = dp4;
                            layoutParams.height = -2;
                            this.avatarImageView.setVisibility(8);
                            this.subinfoTextView.setVisibility(8);
                            this.undoTextView.setVisibility(0);
                            this.undoButton.setVisibility(0);
                            this.undoImageView.setVisibility(8);
                        } else if (i22 == 85) {
                            this.infoTextView.setVisibility(0);
                            this.infoTextView.setTextSize(1, 15.0f);
                            this.infoTextView.setTypeface(AndroidUtilities.bold());
                            this.infoTextView.setText(LocaleController.getString(R.string.SwipeToReplyHint));
                            this.leftImageView.setVisibility(0);
                            this.leftImageView.setAnimation(R.raw.hint_swipe_reply, 64, 64);
                            this.leftImageView.setProgress(0.0f);
                            this.leftImageView.playAnimation();
                            this.subinfoTextView.setVisibility(0);
                            this.subinfoTextView.setText(LocaleController.getString(R.string.SwipeToReplyHintMessage));
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                            layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                            layoutParams.height = -2;
                            i10 = 8;
                            this.avatarImageView.setVisibility(8);
                        } else {
                            if (i22 == 90 || i22 == 91 || i22 == 92 || i22 == 93 || i22 == 94) {
                                switch (i22) {
                                    case 90:
                                        linksTextView7 = this.infoTextView;
                                        formatPluralString = LocaleController.formatPluralString("BoostingSelectUpToWarningChannelsGroupsPlural", (int) BoostRepository.giveawayAddPeersMax(), new Object[0]);
                                        linksTextView7.setText(formatPluralString);
                                        break;
                                    case 91:
                                        linksTextView7 = this.infoTextView;
                                        i7 = R.string.BoostingSelectUpToWarningUsers;
                                        formatPluralString = LocaleController.getString(i7);
                                        linksTextView7.setText(formatPluralString);
                                        break;
                                    case 92:
                                        linksTextView7 = this.infoTextView;
                                        formatPluralString = LocaleController.formatPluralString("BoostingSelectUpToWarningCountriesPlural", (int) BoostRepository.giveawayCountriesMax(), new Object[0]);
                                        linksTextView7.setText(formatPluralString);
                                        break;
                                    case 93:
                                        linksTextView7 = this.infoTextView;
                                        formatPluralString = AndroidUtilities.replaceTags(LocaleController.formatPluralString("BoostingWaitWarningPlural", BoostRepository.boostsPerSentGift(), new Object[0]));
                                        linksTextView7.setText(formatPluralString);
                                        break;
                                    case 94:
                                        linksTextView7 = this.infoTextView;
                                        i7 = R.string.BoostingOnlyRecipientCode;
                                        formatPluralString = LocaleController.getString(i7);
                                        linksTextView7.setText(formatPluralString);
                                        break;
                                }
                                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                                this.infoTextView.setTextSize(1, 15.0f);
                                this.undoButton.setVisibility(8);
                                this.infoTextView.setTypeface(Typeface.DEFAULT);
                                this.subinfoTextView.setVisibility(8);
                                this.leftImageView.setVisibility(0);
                                rLottieImageView4 = this.leftImageView;
                                i8 = R.raw.chats_infotip;
                            } else if (i22 == 2 || i22 == 4) {
                                this.infoTextView.setText(LocaleController.getString(i == 2 ? R.string.ChatArchived : R.string.ChatsArchived));
                                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                                layoutParams.rightMargin = 0;
                                this.infoTextView.setTextSize(1, 15.0f);
                                this.undoButton.setVisibility(0);
                                this.infoTextView.setTypeface(Typeface.DEFAULT);
                                this.subinfoTextView.setVisibility(8);
                                this.leftImageView.setVisibility(0);
                                rLottieImageView4 = this.leftImageView;
                                i8 = R.raw.chats_archived;
                            } else if (i == 82) {
                                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) obj;
                                this.infoTextView.setText(LocaleController.getString(photoEntry.isVideo ? R.string.AttachMediaVideoDeselected : R.string.AttachMediaPhotoDeselected));
                                this.undoButton.setVisibility(0);
                                this.infoTextView.setTextSize(1, 15.0f);
                                this.infoTextView.setTypeface(Typeface.DEFAULT);
                                this.subinfoTextView.setVisibility(8);
                                this.avatarImageView.setVisibility(0);
                                this.avatarImageView.setRoundRadius(AndroidUtilities.dp(2.0f));
                                String str5 = photoEntry.thumbPath;
                                if (str5 != null) {
                                    this.avatarImageView.setImage(str5, null, Theme.chat_attachEmptyDrawable);
                                } else if (photoEntry.path != null) {
                                    this.avatarImageView.setOrientation(photoEntry.orientation, photoEntry.invert, true);
                                    if (photoEntry.isVideo) {
                                        backupImageView = this.avatarImageView;
                                        sb = new StringBuilder();
                                        str = "vthumb://";
                                    } else {
                                        backupImageView = this.avatarImageView;
                                        sb = new StringBuilder();
                                        str = "thumb://";
                                    }
                                    sb.append(str);
                                    sb.append(photoEntry.imageId);
                                    sb.append(":");
                                    sb.append(photoEntry.path);
                                    backupImageView.setImage(sb.toString(), null, Theme.chat_attachEmptyDrawable);
                                } else {
                                    this.avatarImageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                                }
                            } else {
                                layoutParams.leftMargin = AndroidUtilities.dp(45.0f);
                                layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                                layoutParams.rightMargin = 0;
                                this.infoTextView.setTextSize(1, 15.0f);
                                this.undoButton.setVisibility(0);
                                LinkSpanDrawable.LinksTextView linksTextView9 = this.infoTextView;
                                Typeface typeface = Typeface.DEFAULT;
                                linksTextView9.setTypeface(typeface);
                                this.subinfoTextView.setVisibility(8);
                                this.leftImageView.setVisibility(8);
                                int i30 = this.currentAction;
                                if (i30 == 88) {
                                    String str6 = (String) obj;
                                    int intValue8 = ((Integer) obj2).intValue();
                                    if (intValue8 > 0) {
                                        int ceil2 = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                                        layoutParams.leftMargin = AndroidUtilities.dp(48.0f);
                                        layoutParams.rightMargin = ceil2;
                                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                                        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams();
                                        layoutParams3.leftMargin = AndroidUtilities.dp(48.0f);
                                        layoutParams3.rightMargin = ceil2;
                                        this.infoTextView.setText(LocaleController.formatString("FolderLinkDeletedTitle", R.string.FolderLinkDeletedTitle, str6));
                                        this.infoTextView.setTypeface(AndroidUtilities.bold());
                                        this.subinfoTextView.setVisibility(0);
                                        textView2 = this.subinfoTextView;
                                        string2 = LocaleController.formatPluralString("FolderLinkDeletedSubtitle", intValue8, new Object[0]);
                                    } else {
                                        this.infoTextView.setTypeface(typeface);
                                        LinkSpanDrawable.LinksTextView linksTextView10 = this.infoTextView;
                                        int i31 = R.string.FolderLinkDeleted;
                                        if (str6 == null) {
                                            str6 = "";
                                        }
                                        linksTextView10.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FolderLinkDeleted", i31, str6.replace('*', (char) 10033))));
                                        if (this.currentAction != 81) {
                                            for (int i32 = 0; i32 < arrayList.size(); i32++) {
                                                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                long longValue2 = ((Long) arrayList.get(i32)).longValue();
                                                int i33 = this.currentAction;
                                                messagesController.addDialogAction(longValue2, i33 == 0 || i33 == 26);
                                            }
                                        }
                                    }
                                } else {
                                    if (i30 == 81 || i30 == 0 || i30 == 26) {
                                        textView2 = this.infoTextView;
                                        i9 = R.string.HistoryClearedUndo;
                                    } else if (i30 == 27) {
                                        textView2 = this.infoTextView;
                                        i9 = R.string.ChatsDeletedUndo;
                                    } else if (DialogObject.isChatDialog(longValue)) {
                                        TLRPC.Chat chat9 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                        if (!ChatObject.isChannel(chat9) || chat9.megagroup) {
                                            textView2 = this.infoTextView;
                                            i9 = R.string.GroupDeletedUndo;
                                        } else {
                                            textView2 = this.infoTextView;
                                            i9 = R.string.ChannelDeletedUndo;
                                        }
                                    } else {
                                        textView2 = this.infoTextView;
                                        i9 = R.string.ChatDeletedUndo;
                                    }
                                    string2 = LocaleController.getString(i9);
                                }
                                textView2.setText(string2);
                                if (this.currentAction != 81) {
                                }
                            }
                            rLottieImageView4.setAnimation(i8, 36, 36);
                        }
                        z = false;
                        StringBuilder sb222 = new StringBuilder();
                        sb222.append((Object) this.infoTextView.getText());
                        if (this.subinfoTextView.getVisibility() == 0) {
                        }
                        sb222.append(this.subinfoTextView.getVisibility() == 0 ? ". " + ((Object) this.subinfoTextView.getText()) : "");
                        AndroidUtilities.makeAccessibilityAnnouncement(sb222.toString());
                        if (isMultilineSubInfo()) {
                        }
                        this.undoViewHeight = max;
                        if (getVisibility() == 0) {
                        }
                    }
                    this.leftImageView.setVisibility(i11);
                }
                this.leftImageView.setProgress(0.0f);
                this.leftImageView.playAnimation();
                z = false;
                StringBuilder sb2222 = new StringBuilder();
                sb2222.append((Object) this.infoTextView.getText());
                if (this.subinfoTextView.getVisibility() == 0) {
                }
                sb2222.append(this.subinfoTextView.getVisibility() == 0 ? ". " + ((Object) this.subinfoTextView.getText()) : "");
                AndroidUtilities.makeAccessibilityAnnouncement(sb2222.toString());
                if (isMultilineSubInfo()) {
                }
                this.undoViewHeight = max;
                if (getVisibility() == 0) {
                }
            }
        }
        this.undoButton.setVisibility(i10);
        z = false;
        StringBuilder sb22222 = new StringBuilder();
        sb22222.append((Object) this.infoTextView.getText());
        if (this.subinfoTextView.getVisibility() == 0) {
        }
        sb22222.append(this.subinfoTextView.getVisibility() == 0 ? ". " + ((Object) this.subinfoTextView.getText()) : "");
        AndroidUtilities.makeAccessibilityAnnouncement(sb22222.toString());
        if (isMultilineSubInfo()) {
        }
        this.undoViewHeight = max;
        if (getVisibility() == 0) {
        }
    }
}
