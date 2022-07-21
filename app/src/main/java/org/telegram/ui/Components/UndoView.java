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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
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
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentReceipt;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
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
    private ArrayList<Long> currentDialogIds;
    private Object currentInfoObject;
    float enterOffset;
    private int enterOffsetMargin;
    private boolean fromTop;
    private int hideAnimationType;
    private CharSequence infoText;
    private TextView infoTextView;
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
    StaticLayout timeLayout;
    StaticLayout timeLayoutOut;
    private long timeLeft;
    private String timeLeftString;
    float timeReplaceProgress;
    private LinearLayout undoButton;
    private ImageView undoImageView;
    private TextView undoTextView;
    private int undoViewHeight;

    public static /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ boolean lambda$showWithAction$3(View view, MotionEvent motionEvent) {
        return true;
    }

    protected boolean canUndo() {
        return true;
    }

    public void didPressUrl(CharacterStyle characterStyle) {
    }

    protected void onRemoveDialogAction(long j, int i) {
    }

    /* loaded from: classes3.dex */
    public class LinkMovementMethodMy extends LinkMovementMethod {
        public LinkMovementMethodMy() {
            UndoView.this = r1;
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            CharacterStyle[] characterStyleArr;
            try {
                if (motionEvent.getAction() == 0 && ((characterStyleArr = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class)) == null || characterStyleArr.length == 0)) {
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    CharacterStyle[] characterStyleArr2 = (CharacterStyle[]) spannable.getSpans(textView.getSelectionStart(), textView.getSelectionEnd(), CharacterStyle.class);
                    if (characterStyleArr2 != null && characterStyleArr2.length > 0) {
                        UndoView.this.didPressUrl(characterStyleArr2[0]);
                    }
                    Selection.removeSelection(spannable);
                    return true;
                }
                return super.onTouchEvent(textView, spannable, motionEvent);
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
        TextView textView = new TextView(context);
        this.infoTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.infoTextView.setTextColor(getThemedColor("undo_infoColor"));
        this.infoTextView.setLinkTextColor(getThemedColor("undo_cancelColor"));
        this.infoTextView.setMovementMethod(new LinkMovementMethodMy());
        addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 45.0f, 13.0f, 0.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.subinfoTextView = textView2;
        textView2.setTextSize(1, 13.0f);
        this.subinfoTextView.setTextColor(getThemedColor("undo_infoColor"));
        this.subinfoTextView.setLinkTextColor(getThemedColor("undo_cancelColor"));
        this.subinfoTextView.setHighlightColor(0);
        this.subinfoTextView.setSingleLine(true);
        this.subinfoTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.subinfoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        addView(this.subinfoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 58.0f, 27.0f, 8.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.leftImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.leftImageView.setLayerColor("info1.**", getThemedColor("undo_background") | (-16777216));
        this.leftImageView.setLayerColor("info2.**", getThemedColor("undo_background") | (-16777216));
        this.leftImageView.setLayerColor("luc12.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc11.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc10.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc9.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc8.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc7.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc6.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc5.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc4.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc3.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc2.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("luc1.**", getThemedColor("undo_infoColor"));
        this.leftImageView.setLayerColor("Oval.**", getThemedColor("undo_infoColor"));
        addView(this.leftImageView, LayoutHelper.createFrame(54, -2.0f, 19, 3.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(15.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(30, 30.0f, 19, 15.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.undoButton = linearLayout;
        linearLayout.setOrientation(0);
        this.undoButton.setBackground(Theme.createRadSelectorDrawable(getThemedColor("undo_cancelColor") & 587202559, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f)));
        addView(this.undoButton, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 11.0f, 0.0f));
        this.undoButton.setOnClickListener(new UndoView$$ExternalSyntheticLambda0(this));
        ImageView imageView = new ImageView(context);
        this.undoImageView = imageView;
        imageView.setImageResource(2131165342);
        this.undoImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("undo_cancelColor"), PorterDuff.Mode.MULTIPLY));
        this.undoButton.addView(this.undoImageView, LayoutHelper.createLinear(-2, -2, 19, 4, 4, 0, 4));
        TextView textView3 = new TextView(context);
        this.undoTextView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.undoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
        this.undoTextView.setText(LocaleController.getString("Undo", 2131628797));
        this.undoButton.addView(this.undoTextView, LayoutHelper.createLinear(-2, -2, 19, 6, 4, 8, 4));
        this.rect = new RectF(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), AndroidUtilities.dp(33.0f), AndroidUtilities.dp(33.0f));
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setColor(getThemedColor("undo_infoColor"));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textPaint.setColor(getThemedColor("undo_infoColor"));
        setWillNotDraw(false);
        this.backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor("undo_background"));
        setOnTouchListener(UndoView$$ExternalSyntheticLambda3.INSTANCE);
        setVisibility(4);
    }

    public /* synthetic */ void lambda$new$0(View view) {
        if (!canUndo()) {
            return;
        }
        hide(false, 1);
    }

    public void setColors(int i, int i2) {
        Theme.setDrawableColor(this.backgroundDrawable, i);
        this.infoTextView.setTextColor(i2);
        this.subinfoTextView.setTextColor(i2);
        int i3 = i | (-16777216);
        this.leftImageView.setLayerColor("info1.**", i3);
        this.leftImageView.setLayerColor("info2.**", i3);
    }

    private boolean isTooltipAction() {
        int i = this.currentAction;
        return i == 6 || i == 3 || i == 5 || i == 7 || i == 8 || i == 9 || i == 10 || i == 13 || i == 14 || i == 19 || i == 20 || i == 21 || i == 22 || i == 23 || i == 30 || i == 31 || i == 32 || i == 33 || i == 34 || i == 35 || i == 36 || i == 74 || i == 37 || i == 38 || i == 39 || i == 40 || i == 42 || i == 43 || i == 77 || i == 44 || i == 78 || i == 79 || i == 100 || i == 101 || i == ACTION_RINGTONE_ADDED;
    }

    private boolean hasSubInfo() {
        int i = this.currentAction;
        return i == 11 || i == 24 || i == 6 || i == 3 || i == 5 || i == 13 || i == 14 || i == 74 || (i == 7 && MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) || this.currentAction == ACTION_RINGTONE_ADDED;
    }

    public boolean isMultilineSubInfo() {
        int i = this.currentAction;
        return i == 12 || i == 15 || i == 24 || i == 74 || i == ACTION_RINGTONE_ADDED;
    }

    public void setAdditionalTranslationY(float f) {
        if (this.additionalTranslationY != f) {
            this.additionalTranslationY = f;
            updatePosition();
        }
    }

    public Object getCurrentInfoObject() {
        return this.currentInfoObject;
    }

    public void hide(boolean z, int i) {
        if (getVisibility() != 0 || !this.isShown) {
            return;
        }
        this.currentInfoObject = null;
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
                long longValue = this.currentDialogIds.get(i3).longValue();
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                int i4 = this.currentAction;
                messagesController.removeDialogAction(longValue, i4 == 0 || i4 == 26, z);
                onRemoveDialogAction(longValue, this.currentAction);
            }
        }
        float f = -1.0f;
        if (i != 0) {
            AnimatorSet animatorSet = new AnimatorSet();
            if (i == 1) {
                Animator[] animatorArr = new Animator[1];
                float[] fArr = new float[1];
                if (!this.fromTop) {
                    f = 1.0f;
                }
                fArr[0] = f * (this.enterOffsetMargin + this.undoViewHeight);
                animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
                animatorSet.playTogether(animatorArr);
                animatorSet.setDuration(250L);
            } else {
                animatorSet.playTogether(ObjectAnimator.ofFloat(this, View.SCALE_X, 0.8f), ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.8f), ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f));
                animatorSet.setDuration(180L);
            }
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.addListener(new AnonymousClass1());
            animatorSet.start();
            return;
        }
        if (!this.fromTop) {
            f = 1.0f;
        }
        setEnterOffset(f * (this.enterOffsetMargin + this.undoViewHeight));
        setVisibility(4);
    }

    /* renamed from: org.telegram.ui.Components.UndoView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        AnonymousClass1() {
            UndoView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            UndoView.this.setVisibility(4);
            UndoView.this.setScaleX(1.0f);
            UndoView.this.setScaleY(1.0f);
            UndoView.this.setAlpha(1.0f);
        }
    }

    public void showWithAction(long j, int i, Runnable runnable) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, (Runnable) null);
    }

    public void showWithAction(long j, int i, Object obj) {
        showWithAction(j, i, obj, (Object) null, (Runnable) null, (Runnable) null);
    }

    public void showWithAction(long j, int i, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, (Object) null, (Object) null, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Object obj, Runnable runnable, Runnable runnable2) {
        showWithAction(j, i, obj, (Object) null, runnable, runnable2);
    }

    public void showWithAction(long j, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(Long.valueOf(j));
        showWithAction(arrayList, i, obj, obj2, runnable, runnable2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:567:0x1697  */
    /* JADX WARN: Removed duplicated region for block: B:570:0x16b7  */
    /* JADX WARN: Removed duplicated region for block: B:573:0x16de  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x1723  */
    /* JADX WARN: Removed duplicated region for block: B:603:0x17cd  */
    /* JADX WARN: Removed duplicated region for block: B:629:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v361, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showWithAction(ArrayList<Long> arrayList, int i, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
        boolean z;
        int i2;
        float f;
        String str;
        int i3;
        CharSequence charSequence;
        int i4;
        CharSequence charSequence2;
        int i5;
        String str2;
        int i6;
        String str3;
        int i7;
        String string;
        String str4;
        int i8;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        int i9;
        if (AndroidUtilities.shouldShowClipboardToast() || !((i9 = this.currentAction) == 52 || i9 == 56 || i9 == 57 || i9 == 58 || i9 == 59 || i9 == 60 || i9 == 80 || i9 == 33)) {
            Runnable runnable3 = this.currentActionRunnable;
            if (runnable3 != null) {
                runnable3.run();
            }
            this.isShown = true;
            this.currentActionRunnable = runnable;
            this.currentCancelRunnable = runnable2;
            this.currentDialogIds = arrayList;
            long longValue = arrayList.get(0).longValue();
            this.currentAction = i;
            this.timeLeft = 5000L;
            this.currentInfoObject = obj;
            this.lastUpdateTime = SystemClock.elapsedRealtime();
            this.undoTextView.setText(LocaleController.getString("Undo", 2131628797).toUpperCase());
            this.undoImageView.setVisibility(0);
            this.leftImageView.setPadding(0, 0, 0, 0);
            this.infoTextView.setTextSize(1, 15.0f);
            this.avatarImageView.setVisibility(8);
            this.infoTextView.setGravity(51);
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
            String str11 = null;
            if ((runnable == null && runnable2 == null) || i == ACTION_RINGTONE_ADDED) {
                setOnClickListener(new UndoView$$ExternalSyntheticLambda1(this));
                setOnTouchListener(null);
            } else {
                setOnClickListener(null);
                setOnTouchListener(UndoView$$ExternalSyntheticLambda4.INSTANCE);
            }
            this.infoTextView.setMovementMethod(null);
            String str12 = "";
            int i10 = 36;
            if (isTooltipAction()) {
                if (i == ACTION_RINGTONE_ADDED) {
                    this.subinfoTextView.setSingleLine(false);
                    charSequence2 = LocaleController.getString("SoundAdded", 2131628449);
                    ?? replaceSingleTag = AndroidUtilities.replaceSingleTag(LocaleController.getString("SoundAddedSubtitle", 2131628450), runnable);
                    this.currentActionRunnable = null;
                    this.timeLeft = 4000L;
                    str11 = replaceSingleTag;
                    i4 = 2131558550;
                } else if (i == 74) {
                    this.subinfoTextView.setSingleLine(false);
                    charSequence2 = LocaleController.getString("ReportChatSent", 2131628000);
                    str11 = LocaleController.formatString("ReportSentInfo", 2131628009, new Object[0]);
                    i4 = 2131558462;
                    this.timeLeft = 4000L;
                } else {
                    if (i == 34) {
                        TLRPC$User tLRPC$User = (TLRPC$User) obj;
                        SpannableStringBuilder replaceTags = ChatObject.isChannelOrGiga((TLRPC$Chat) obj2) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelInvitedUser", 2131629047, UserObject.getFirstName(tLRPC$User))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupInvitedUser", 2131629136, UserObject.getFirstName(tLRPC$User)));
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
                        avatarDrawable.setInfo(tLRPC$User);
                        this.avatarImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                        charSequence2 = replaceTags;
                    } else if (i == 44) {
                        TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj2;
                        if (obj instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User2 = (TLRPC$User) obj;
                            charSequence2 = ChatObject.isChannelOrGiga(tLRPC$Chat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserJoined", 2131629072, UserObject.getFirstName(tLRPC$User2))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatUserJoined", 2131629087, UserObject.getFirstName(tLRPC$User2)));
                        } else {
                            TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) obj;
                            charSequence2 = ChatObject.isChannelOrGiga(tLRPC$Chat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelChatJoined", 2131629041, tLRPC$Chat2.title)) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatChatJoined", 2131629077, tLRPC$Chat2.title));
                        }
                        AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                        avatarDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
                        TLObject tLObject = (TLObject) obj;
                        avatarDrawable2.setInfo(tLObject);
                        this.avatarImageView.setForUserOrChat(tLObject, avatarDrawable2);
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                    } else if (i == 37) {
                        AvatarDrawable avatarDrawable3 = new AvatarDrawable();
                        avatarDrawable3.setTextSize(AndroidUtilities.dp(12.0f));
                        if (obj instanceof TLRPC$User) {
                            TLRPC$User tLRPC$User3 = (TLRPC$User) obj;
                            avatarDrawable3.setInfo(tLRPC$User3);
                            this.avatarImageView.setForUserOrChat(tLRPC$User3, avatarDrawable3);
                            str10 = ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name);
                        } else {
                            TLRPC$Chat tLRPC$Chat3 = (TLRPC$Chat) obj;
                            avatarDrawable3.setInfo(tLRPC$Chat3);
                            this.avatarImageView.setForUserOrChat(tLRPC$Chat3, avatarDrawable3);
                            str10 = tLRPC$Chat3.title;
                        }
                        if (ChatObject.isChannelOrGiga((TLRPC$Chat) obj2)) {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserChanged", 2131629071, str10));
                        } else {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserChanged", 2131629200, str10));
                        }
                        this.avatarImageView.setVisibility(0);
                        this.timeLeft = 3000L;
                    } else if (i == 33) {
                        charSequence2 = LocaleController.getString("VoipGroupCopyInviteLinkCopied", 2131629118);
                        i4 = 2131558613;
                        this.timeLeft = 3000L;
                    } else if (i == 77) {
                        charSequence2 = (CharSequence) obj;
                        i4 = 2131558500;
                        this.timeLeft = 5000L;
                        if (this.parentFragment != null && (obj2 instanceof TLRPC$Message)) {
                            setOnTouchListener(null);
                            this.infoTextView.setMovementMethod(null);
                            setOnClickListener(new UndoView$$ExternalSyntheticLambda2(this, (TLRPC$Message) obj2));
                        }
                    } else if (i == 30) {
                        if (obj instanceof TLRPC$User) {
                            str9 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str9 = ((TLRPC$Chat) obj).title;
                        }
                        charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeak", 2131629198, str9));
                        i4 = 2131558614;
                        this.timeLeft = 3000L;
                    } else if (i == 35) {
                        if (obj instanceof TLRPC$User) {
                            str8 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str8 = obj instanceof TLRPC$Chat ? ((TLRPC$Chat) obj).title : str12;
                        }
                        charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeakForYou", 2131629199, str8));
                        i4 = 2131558614;
                        this.timeLeft = 3000L;
                    } else if (i == 31) {
                        if (obj instanceof TLRPC$User) {
                            str7 = UserObject.getFirstName((TLRPC$User) obj);
                        } else {
                            str7 = ((TLRPC$Chat) obj).title;
                        }
                        charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeak", 2131629196, str7));
                        i4 = 2131558620;
                        this.timeLeft = 3000L;
                    } else if (i == 38) {
                        charSequence2 = obj instanceof TLRPC$Chat ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupYouCanNowSpeakIn", 2131629208, ((TLRPC$Chat) obj).title)) : AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupYouCanNowSpeak", 2131629207));
                        i4 = 2131558606;
                        this.timeLeft = 3000L;
                    } else if (i == 42) {
                        if (ChatObject.isChannelOrGiga((TLRPC$Chat) obj)) {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundMuted", 2131629062));
                        } else {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundMuted", 2131629176));
                        }
                        i4 = 2131558466;
                        this.timeLeft = 3000L;
                    } else if (i == 43) {
                        if (ChatObject.isChannelOrGiga((TLRPC$Chat) obj)) {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundUnmuted", 2131629063));
                        } else {
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundUnmuted", 2131629177));
                        }
                        i4 = 2131558472;
                        this.timeLeft = 3000L;
                    } else {
                        int i11 = this.currentAction;
                        if (i11 == 39 || i11 == 100) {
                            if (i11 == 39) {
                                i6 = 2131629109;
                                str2 = "VoipGroupAudioRecordStarted";
                            } else {
                                i6 = 2131629203;
                                str2 = "VoipGroupVideoRecordStarted";
                            }
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.getString(str2, i6));
                            i4 = 2131558617;
                            this.timeLeft = 3000L;
                        } else if (i11 == 40 || i11 == 101) {
                            if (i11 == 40) {
                                i7 = 2131629108;
                                str3 = "VoipGroupAudioRecordSaved";
                            } else {
                                i7 = 2131629202;
                                str3 = "VoipGroupVideoRecordSaved";
                            }
                            String string2 = LocaleController.getString(str3, i7);
                            i4 = 2131558616;
                            this.timeLeft = 4000L;
                            this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string2);
                            int indexOf = string2.indexOf("**");
                            int lastIndexOf = string2.lastIndexOf("**");
                            if (indexOf >= 0 && lastIndexOf >= 0 && indexOf != lastIndexOf) {
                                spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 2, (CharSequence) str12);
                                spannableStringBuilder.replace(indexOf, indexOf + 2, (CharSequence) str12);
                                try {
                                    spannableStringBuilder.setSpan(new URLSpanNoUnderline("tg://openmessage?user_id=" + UserConfig.getInstance(this.currentAccount).getClientUserId()), indexOf, lastIndexOf - 2, 33);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            charSequence2 = spannableStringBuilder;
                        } else if (i == 36) {
                            if (obj instanceof TLRPC$User) {
                                str6 = UserObject.getFirstName((TLRPC$User) obj);
                            } else {
                                str6 = ((TLRPC$Chat) obj).title;
                            }
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeakForYou", 2131629197, str6));
                            i4 = 2131558620;
                            this.timeLeft = 3000L;
                        } else if (i == 32) {
                            if (obj instanceof TLRPC$User) {
                                str5 = UserObject.getFirstName((TLRPC$User) obj);
                            } else {
                                str5 = ((TLRPC$Chat) obj).title;
                            }
                            charSequence2 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupRemovedFromGroup", 2131629167, str5));
                            i4 = 2131558612;
                            this.timeLeft = 3000L;
                        } else {
                            if (i == 9 || i == 10) {
                                TLRPC$User tLRPC$User4 = (TLRPC$User) obj;
                                charSequence2 = i == 9 ? AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferChannelToast", 2131625581, UserObject.getFirstName(tLRPC$User4))) : AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferGroupToast", 2131625582, UserObject.getFirstName(tLRPC$User4)));
                            } else if (i == 8) {
                                charSequence2 = LocaleController.formatString("NowInContacts", 2131627125, UserObject.getFirstName((TLRPC$User) obj));
                            } else if (i == 22) {
                                if (!DialogObject.isUserDialog(longValue)) {
                                    TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-longValue));
                                    if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                        if (obj == null) {
                                            charSequence2 = LocaleController.getString("MainGroupProfilePhotoSetHint", 2131626563);
                                        } else {
                                            charSequence2 = LocaleController.getString("MainGroupProfileVideoSetHint", 2131626564);
                                        }
                                    } else if (obj == null) {
                                        charSequence2 = LocaleController.getString("MainChannelProfilePhotoSetHint", 2131626561);
                                    } else {
                                        charSequence2 = LocaleController.getString("MainChannelProfileVideoSetHint", 2131626562);
                                    }
                                } else if (obj == null) {
                                    charSequence2 = LocaleController.getString("MainProfilePhotoSetHint", 2131626565);
                                } else {
                                    charSequence2 = LocaleController.getString("MainProfileVideoSetHint", 2131626566);
                                }
                            } else if (i == 23) {
                                charSequence2 = LocaleController.getString("ChatWasMovedToMainList", 2131625056);
                            } else if (i == 6) {
                                charSequence2 = LocaleController.getString("ArchiveHidden", 2131624404);
                                str11 = LocaleController.getString("ArchiveHiddenInfo", 2131624405);
                                i4 = 2131558425;
                                i10 = 48;
                            } else {
                                if (i11 == 13) {
                                    charSequence2 = LocaleController.getString("QuizWellDone", 2131627886);
                                    str11 = LocaleController.getString("QuizWellDoneInfo", 2131627887);
                                    i4 = 2131558622;
                                } else if (i11 == 14) {
                                    charSequence2 = LocaleController.getString("QuizWrongAnswer", 2131627888);
                                    str11 = LocaleController.getString("QuizWrongAnswerInfo", 2131627889);
                                    i4 = 2131558624;
                                } else if (i == 7) {
                                    charSequence2 = LocaleController.getString("ArchivePinned", 2131624412);
                                    if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                        string = LocaleController.getString("ArchivePinnedInfo", 2131624413);
                                        str11 = string;
                                    }
                                    i4 = 2131558424;
                                } else if (i == 20 || i == 21) {
                                    MessagesController.DialogFilter dialogFilter = (MessagesController.DialogFilter) obj2;
                                    if (longValue != 0) {
                                        if (DialogObject.isEncryptedDialog(longValue)) {
                                            longValue = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue))).user_id;
                                        }
                                        if (DialogObject.isUserDialog(longValue)) {
                                            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                                            charSequence2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserAddedToExisting", 2131625938, UserObject.getFirstName(user), dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserRemovedFrom", 2131625939, UserObject.getFirstName(user), dialogFilter.name));
                                        } else {
                                            TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                            charSequence2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatAddedToExisting", 2131625878, chat2.title, dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatRemovedFrom", 2131625879, chat2.title, dialogFilter.name));
                                        }
                                    } else {
                                        charSequence2 = i == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsAddedToExisting", 2131625882, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsRemovedFrom", 2131625883, LocaleController.formatPluralString("ChatsSelected", ((Integer) obj).intValue(), new Object[0]), dialogFilter.name));
                                    }
                                    i4 = i == 20 ? 2131558452 : 2131558453;
                                } else {
                                    if (i == 19) {
                                        charSequence2 = this.infoText;
                                    } else if (i == 82) {
                                        if (((MediaController.PhotoEntry) obj).isVideo) {
                                            i8 = 2131624510;
                                            str4 = "AttachMediaVideoDeselected";
                                        } else {
                                            i8 = 2131624505;
                                            str4 = "AttachMediaPhotoDeselected";
                                        }
                                        charSequence2 = LocaleController.getString(str4, i8);
                                    } else if (i == 78 || i == 79) {
                                        int intValue = ((Integer) obj).intValue();
                                        if (i == 78) {
                                            charSequence2 = LocaleController.formatPluralString("PinnedDialogsCount", intValue, new Object[0]);
                                        } else {
                                            charSequence2 = LocaleController.formatPluralString("UnpinnedDialogsCount", intValue, new Object[0]);
                                        }
                                        i4 = this.currentAction == 78 ? 2131558467 : 2131558473;
                                        if (obj2 instanceof Integer) {
                                            this.timeLeft = ((Integer) obj2).intValue();
                                        }
                                    } else {
                                        if (i == 3) {
                                            charSequence2 = LocaleController.getString("ChatArchived", 2131625013);
                                        } else {
                                            charSequence2 = LocaleController.getString("ChatsArchived", 2131625069);
                                        }
                                        if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                            string = LocaleController.getString("ChatArchivedInfo", 2131625014);
                                            str11 = string;
                                        }
                                    }
                                    i4 = 2131558424;
                                }
                                i10 = 44;
                            }
                            i4 = 2131558430;
                        }
                    }
                    i4 = 0;
                }
                this.infoTextView.setText(charSequence2);
                if (i4 != 0) {
                    this.leftImageView.setAnimation(i4, i10, i10);
                    RLottieDrawable animatedDrawable = this.leftImageView.getAnimatedDrawable();
                    animatedDrawable.setPlayInDirectionOfCustomEndFrame(false);
                    animatedDrawable.setCustomEndFrame(animatedDrawable.getFramesCount());
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else {
                    this.leftImageView.setVisibility(8);
                }
                if (str11 != null) {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(8.0f);
                    this.subinfoTextView.setText(str11);
                    this.subinfoTextView.setVisibility(0);
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    i5 = 8;
                } else {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    i5 = 8;
                    this.subinfoTextView.setVisibility(8);
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                }
                this.undoButton.setVisibility(i5);
            } else {
                int i12 = this.currentAction;
                if (i12 == 45 || i12 == 46 || i12 == 47 || i12 == 51 || i12 == 50 || i12 == 52 || i12 == 53 || i12 == 54 || i12 == 55 || i12 == 56 || i12 == 57 || i12 == 58 || i12 == 59 || i12 == 60 || i12 == 71 || i12 == 70 || i12 == 75 || i12 == 76 || i12 == 41 || i12 == 78 || i12 == 79 || i12 == 61 || i12 == 80) {
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    long j = -1;
                    int i13 = this.currentAction;
                    if (i13 == 76) {
                        this.infoTextView.setText(LocaleController.getString("BroadcastGroupConvertSuccess", 2131624771));
                        this.leftImageView.setAnimation(2131558458, 36, 36);
                        layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                        this.infoTextView.setTextSize(1, 14.0f);
                    } else if (i13 == 75) {
                        this.infoTextView.setText(LocaleController.getString("GigagroupConvertCancelHint", 2131626116));
                        this.leftImageView.setAnimation(2131558424, 36, 36);
                        layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                        this.infoTextView.setTextSize(1, 14.0f);
                    } else {
                        if (i == 70) {
                            TLRPC$User tLRPC$User5 = (TLRPC$User) obj;
                            int intValue2 = ((Integer) obj2).intValue();
                            this.subinfoTextView.setSingleLine(false);
                            this.infoTextView.setText(LocaleController.formatString("AutoDeleteHintOnText", 2131624564, LocaleController.formatTTLString(intValue2)));
                            this.leftImageView.setAnimation(2131558450, 36, 36);
                            layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                            this.timeLeft = 4000L;
                            this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
                            z = true;
                        } else {
                            if (i13 == 71) {
                                this.infoTextView.setText(LocaleController.getString("AutoDeleteHintOffText", 2131624563));
                                this.leftImageView.setAnimation(2131558449, 36, 36);
                                this.infoTextView.setTextSize(1, 14.0f);
                                this.timeLeft = 3000L;
                                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(4.0f));
                            } else if (i13 == 45) {
                                this.infoTextView.setText(LocaleController.getString("ImportMutualError", 2131626243));
                                this.leftImageView.setAnimation(2131558445, 36, 36);
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i13 == 46) {
                                this.infoTextView.setText(LocaleController.getString("ImportNotAdmin", 2131626244));
                                this.leftImageView.setAnimation(2131558445, 36, 36);
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i13 == 47) {
                                this.infoTextView.setText(LocaleController.getString("ImportedInfo", 2131626266));
                                this.leftImageView.setAnimation(2131558478, 36, 36);
                                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0f));
                                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                                this.infoTextView.setTextSize(1, 14.0f);
                            } else if (i13 == 51) {
                                this.infoTextView.setText(LocaleController.getString("AudioSpeedNormal", 2131624524));
                                this.leftImageView.setAnimation(2131558409, 36, 36);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i13 == 50) {
                                this.infoTextView.setText(LocaleController.getString("AudioSpeedFast", 2131624523));
                                this.leftImageView.setAnimation(2131558408, 36, 36);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i13 == 52 || i13 == 56 || i13 == 57 || i13 == 58 || i13 == 59 || i13 == 60 || i13 == 80) {
                                if (!AndroidUtilities.shouldShowClipboardToast()) {
                                    return;
                                }
                                int i14 = 2131558433;
                                int i15 = this.currentAction;
                                if (i15 == 80) {
                                    this.infoTextView.setText(LocaleController.getString("EmailCopied", 2131625613));
                                } else if (i15 == 60) {
                                    this.infoTextView.setText(LocaleController.getString("PhoneCopied", 2131627540));
                                } else if (i15 == 56) {
                                    this.infoTextView.setText(LocaleController.getString("UsernameCopied", 2131628919));
                                } else if (i15 == 57) {
                                    this.infoTextView.setText(LocaleController.getString("HashtagCopied", 2131626175));
                                } else if (i15 == 52) {
                                    this.infoTextView.setText(LocaleController.getString("MessageCopied", 2131626675));
                                } else if (i15 == 59) {
                                    i14 = 2131558613;
                                    this.infoTextView.setText(LocaleController.getString("LinkCopied", 2131626480));
                                } else {
                                    this.infoTextView.setText(LocaleController.getString("TextCopied", 2131628663));
                                }
                                this.leftImageView.setAnimation(i14, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i13 == 54) {
                                this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOn", 2131624951));
                                this.leftImageView.setAnimation(2131558545, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i13 == 55) {
                                this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOff", 2131624950));
                                this.leftImageView.setAnimation(2131558544, 30, 30);
                                this.timeLeft = 3000L;
                                this.infoTextView.setTextSize(1, 15.0f);
                            } else if (i13 == 41) {
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("InvLinkToSavedMessages", 2131626291)));
                                    } else if (DialogObject.isChatDialog(longValue)) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToGroup", 2131626290, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue)).title)));
                                    } else {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToUser", 2131626292, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue))))));
                                    }
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToChats", 2131626289, LocaleController.formatPluralString("Chats", ((Integer) obj2).intValue(), new Object[0]))));
                                }
                                this.leftImageView.setAnimation(2131558430, 36, 36);
                                this.timeLeft = 3000L;
                            } else if (i13 == 53) {
                                Integer num = (Integer) obj;
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        if (num.intValue() == 1) {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessageToSavedMessages", 2131626080)));
                                        } else {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessagesToSavedMessages", 2131626084)));
                                        }
                                        this.leftImageView.setAnimation(2131558542, 30, 30);
                                        this.timeLeft = 3000L;
                                    } else {
                                        if (DialogObject.isChatDialog(longValue)) {
                                            TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                                            if (num.intValue() == 1) {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToGroup", 2131626079, chat3.title)));
                                            } else {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToGroup", 2131626083, chat3.title)));
                                            }
                                        } else {
                                            TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                                            if (num.intValue() == 1) {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToUser", 2131626081, UserObject.getFirstName(user2))));
                                            } else {
                                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToUser", 2131626085, UserObject.getFirstName(user2))));
                                            }
                                        }
                                        this.leftImageView.setAnimation(2131558454, 30, 30);
                                    }
                                } else {
                                    int intValue3 = ((Integer) obj2).intValue();
                                    if (num.intValue() == 1) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToChats", 2131626078, LocaleController.formatPluralString("Chats", intValue3, new Object[0]))));
                                    } else {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToChats", 2131626082, LocaleController.formatPluralString("Chats", intValue3, new Object[0]))));
                                    }
                                    this.leftImageView.setAnimation(2131558454, 30, 30);
                                }
                                j = 300;
                                this.timeLeft = 3000L;
                            } else if (i13 == 61) {
                                Integer num2 = (Integer) obj;
                                if (obj2 == null) {
                                    if (longValue == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("BackgroundToSavedMessages", 2131624669)));
                                        this.leftImageView.setAnimation(2131558542, 30, 30);
                                    } else {
                                        if (DialogObject.isChatDialog(longValue)) {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToGroup", 2131624668, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue)).title)));
                                        } else {
                                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToUser", 2131624670, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue))))));
                                        }
                                        this.leftImageView.setAnimation(2131558454, 30, 30);
                                    }
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToChats", 2131624667, LocaleController.formatPluralString("Chats", ((Integer) obj2).intValue(), new Object[0]))));
                                    this.leftImageView.setAnimation(2131558454, 30, 30);
                                }
                                this.timeLeft = 3000L;
                            }
                            z = false;
                        }
                        this.subinfoTextView.setVisibility(8);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoButton.setVisibility(8);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                        this.leftImageView.setProgress(0.0f);
                        this.leftImageView.playAnimation();
                        if (j > 0) {
                            this.leftImageView.postDelayed(new UndoView$$ExternalSyntheticLambda5(this), j);
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append((Object) this.infoTextView.getText());
                        if (this.subinfoTextView.getVisibility() == 0) {
                            str12 = ". " + ((Object) this.subinfoTextView.getText());
                        }
                        sb.append(str12);
                        AndroidUtilities.makeAccessibilityAnnouncement(sb.toString());
                        if (isMultilineSubInfo()) {
                            int measuredWidth = ((ViewGroup) getParent()).getMeasuredWidth();
                            if (measuredWidth == 0) {
                                measuredWidth = AndroidUtilities.displaySize.x;
                            }
                            measureChildWithMargins(this.subinfoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                            this.undoViewHeight = this.subinfoTextView.getMeasuredHeight() + AndroidUtilities.dp(37.0f);
                        } else if (hasSubInfo()) {
                            this.undoViewHeight = AndroidUtilities.dp(52.0f);
                        } else if (getParent() instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) getParent();
                            int measuredWidth2 = (viewGroup.getMeasuredWidth() - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight();
                            if (measuredWidth2 <= 0) {
                                measuredWidth2 = AndroidUtilities.displaySize.x;
                            }
                            measureChildWithMargins(this.infoTextView, View.MeasureSpec.makeMeasureSpec(measuredWidth2 - AndroidUtilities.dp(16.0f), 1073741824), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
                            int measuredHeight = this.infoTextView.getMeasuredHeight();
                            int i16 = this.currentAction;
                            int dp = measuredHeight + AndroidUtilities.dp((i16 == 16 || i16 == 17 || i16 == 18) ? 14.0f : 28.0f);
                            this.undoViewHeight = dp;
                            int i17 = this.currentAction;
                            if (i17 == 18) {
                                this.undoViewHeight = Math.max(dp, AndroidUtilities.dp(52.0f));
                            } else if (i17 == 25) {
                                this.undoViewHeight = Math.max(dp, AndroidUtilities.dp(50.0f));
                            } else if (z) {
                                this.undoViewHeight = dp - AndroidUtilities.dp(8.0f);
                            }
                        }
                        if (getVisibility() == 0) {
                            return;
                        }
                        setVisibility(0);
                        setEnterOffset((this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight));
                        AnimatorSet animatorSet = new AnimatorSet();
                        Animator[] animatorArr = new Animator[1];
                        float[] fArr = new float[2];
                        boolean z2 = this.fromTop;
                        fArr[0] = (z2 ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight);
                        fArr[1] = z2 ? 1.0f : -1.0f;
                        animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
                        animatorSet.playTogether(animatorArr);
                        animatorSet.setInterpolator(new DecelerateInterpolator());
                        animatorSet.setDuration(180L);
                        animatorSet.start();
                        return;
                    }
                    z = true;
                    this.subinfoTextView.setVisibility(8);
                    this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                    this.undoButton.setVisibility(8);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                    if (j > 0) {
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append((Object) this.infoTextView.getText());
                    if (this.subinfoTextView.getVisibility() == 0) {
                    }
                    sb2.append(str12);
                    AndroidUtilities.makeAccessibilityAnnouncement(sb2.toString());
                    if (isMultilineSubInfo()) {
                    }
                    if (getVisibility() == 0) {
                    }
                } else if (i12 == 24 || i12 == 25) {
                    int intValue4 = ((Integer) obj).intValue();
                    TLRPC$User tLRPC$User6 = (TLRPC$User) obj2;
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    if (intValue4 != 0) {
                        this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.leftImageView.clearLayerColors();
                        this.leftImageView.setLayerColor("BODY.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Big.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Big 3.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Wibe Small.**", getThemedColor("undo_infoColor"));
                        this.infoTextView.setText(LocaleController.getString("ProximityAlertSet", 2131627806));
                        this.leftImageView.setAnimation(2131558472, 28, 28);
                        this.subinfoTextView.setVisibility(0);
                        this.subinfoTextView.setSingleLine(false);
                        this.subinfoTextView.setMaxLines(3);
                        if (tLRPC$User6 != null) {
                            this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoUser", 2131627808, UserObject.getFirstName(tLRPC$User6), LocaleController.formatDistance(intValue4, 2)));
                        } else {
                            this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoGroup2", 2131627807, LocaleController.formatDistance(intValue4, 2)));
                        }
                        this.undoButton.setVisibility(8);
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    } else {
                        this.infoTextView.setTypeface(Typeface.DEFAULT);
                        this.infoTextView.setTextSize(1, 15.0f);
                        this.leftImageView.clearLayerColors();
                        this.leftImageView.setLayerColor("Body Main.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Body Top.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Line.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Curve Big.**", getThemedColor("undo_infoColor"));
                        this.leftImageView.setLayerColor("Curve Small.**", getThemedColor("undo_infoColor"));
                        layoutParams.topMargin = AndroidUtilities.dp(14.0f);
                        this.infoTextView.setText(LocaleController.getString("ProximityAlertCancelled", 2131627805));
                        this.leftImageView.setAnimation(2131558466, 28, 28);
                        this.subinfoTextView.setVisibility(8);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoButton.setVisibility(0);
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i12 == 11) {
                    this.infoTextView.setText(LocaleController.getString("AuthAnotherClientOk", 2131624543));
                    this.leftImageView.setAnimation(2131558430, 36, 36);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    this.subinfoTextView.setText(((TLRPC$TL_authorization) obj).app_name);
                    this.subinfoTextView.setVisibility(0);
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.undoTextView.setTextColor(getThemedColor("windowBackgroundWhiteRedText2"));
                    this.undoImageView.setVisibility(8);
                    this.undoButton.setVisibility(0);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i12 == 15) {
                    this.timeLeft = 10000L;
                    this.undoTextView.setText(LocaleController.getString("Open", 2131627142).toUpperCase());
                    this.infoTextView.setText(LocaleController.getString("FilterAvailableTitle", 2131625875));
                    this.leftImageView.setAnimation(2131558446, 36, 36);
                    int ceil = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = ceil;
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = ceil;
                    String string3 = LocaleController.getString("FilterAvailableText", 2131625874);
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(string3);
                    int indexOf2 = string3.indexOf(42);
                    int lastIndexOf2 = string3.lastIndexOf(42);
                    if (indexOf2 >= 0 && lastIndexOf2 >= 0 && indexOf2 != lastIndexOf2) {
                        spannableStringBuilder2.replace(lastIndexOf2, lastIndexOf2 + 1, (CharSequence) str12);
                        spannableStringBuilder2.replace(indexOf2, indexOf2 + 1, (CharSequence) str12);
                        spannableStringBuilder2.setSpan(new URLSpanNoUnderline("tg://settings/folders"), indexOf2, lastIndexOf2 - 1, 33);
                    }
                    this.subinfoTextView.setText(spannableStringBuilder2);
                    this.subinfoTextView.setVisibility(0);
                    this.subinfoTextView.setSingleLine(false);
                    this.subinfoTextView.setMaxLines(2);
                    this.undoButton.setVisibility(0);
                    this.undoImageView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i12 == 16 || i12 == 17) {
                    this.timeLeft = 4000L;
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.infoTextView.setGravity(16);
                    this.infoTextView.setMinHeight(AndroidUtilities.dp(30.0f));
                    String str13 = (String) obj;
                    if ("🎲".equals(str13)) {
                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DiceInfo2", 2131625485)));
                        this.leftImageView.setImageResource(2131165386);
                    } else {
                        if ("🎯".equals(str13)) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DartInfo", 2131625323)));
                        } else {
                            String serverString = LocaleController.getServerString("DiceEmojiInfo_" + str13);
                            if (!TextUtils.isEmpty(serverString)) {
                                TextView textView = this.infoTextView;
                                textView.setText(Emoji.replaceEmoji(serverString, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                            } else {
                                f = 14.0f;
                                this.infoTextView.setText(Emoji.replaceEmoji(LocaleController.formatString("DiceEmojiInfo", 2131625484, str13), this.infoTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str13));
                                this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                layoutParams.topMargin = AndroidUtilities.dp(f);
                                layoutParams.bottomMargin = AndroidUtilities.dp(f);
                                layoutParams2.leftMargin = AndroidUtilities.dp(f);
                                layoutParams2.width = AndroidUtilities.dp(26.0f);
                                layoutParams2.height = AndroidUtilities.dp(26.0f);
                            }
                        }
                        f = 14.0f;
                        this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(str13));
                        this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        layoutParams.topMargin = AndroidUtilities.dp(f);
                        layoutParams.bottomMargin = AndroidUtilities.dp(f);
                        layoutParams2.leftMargin = AndroidUtilities.dp(f);
                        layoutParams2.width = AndroidUtilities.dp(26.0f);
                        layoutParams2.height = AndroidUtilities.dp(26.0f);
                    }
                    this.undoTextView.setText(LocaleController.getString("SendDice", 2131628248));
                    if (this.currentAction == 16) {
                        i2 = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                        this.undoTextView.setVisibility(0);
                        this.undoTextView.setTextColor(getThemedColor("undo_cancelColor"));
                        this.undoImageView.setVisibility(8);
                        this.undoButton.setVisibility(0);
                    } else {
                        i2 = AndroidUtilities.dp(8.0f);
                        this.undoTextView.setVisibility(8);
                        this.undoButton.setVisibility(8);
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = i2;
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                    layoutParams.height = -1;
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                } else if (i12 == 18) {
                    this.timeLeft = Math.max(4000, Math.min((charSequence.length() / 50) * 1600, 10000));
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
                    int dp2 = AndroidUtilities.dp(8.0f);
                    layoutParams2.bottomMargin = dp2;
                    layoutParams2.topMargin = dp2;
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setAnimation(2131558424, 36, 36);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                    this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                } else if (i12 == 12) {
                    this.infoTextView.setText(LocaleController.getString("ColorThemeChanged", 2131625202));
                    this.leftImageView.setImageResource(2131166188);
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.rightMargin = AndroidUtilities.dp(48.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                    ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(48.0f);
                    String string4 = LocaleController.getString("ColorThemeChangedInfo", 2131625203);
                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(string4);
                    int indexOf3 = string4.indexOf(42);
                    int lastIndexOf3 = string4.lastIndexOf(42);
                    if (indexOf3 >= 0 && lastIndexOf3 >= 0 && indexOf3 != lastIndexOf3) {
                        spannableStringBuilder3.replace(lastIndexOf3, lastIndexOf3 + 1, (CharSequence) str12);
                        spannableStringBuilder3.replace(indexOf3, indexOf3 + 1, (CharSequence) str12);
                        spannableStringBuilder3.setSpan(new URLSpanNoUnderline("tg://settings/themes"), indexOf3, lastIndexOf3 - 1, 33);
                    }
                    this.subinfoTextView.setText(spannableStringBuilder3);
                    this.subinfoTextView.setVisibility(0);
                    this.subinfoTextView.setSingleLine(false);
                    this.subinfoTextView.setMaxLines(2);
                    this.undoTextView.setVisibility(8);
                    this.undoButton.setVisibility(0);
                    this.leftImageView.setVisibility(0);
                } else if (i12 == 2 || i12 == 4) {
                    if (i == 2) {
                        this.infoTextView.setText(LocaleController.getString("ChatArchived", 2131625013));
                    } else {
                        this.infoTextView.setText(LocaleController.getString("ChatsArchived", 2131625069));
                    }
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = 0;
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(0);
                    this.leftImageView.setAnimation(2131558422, 36, 36);
                    this.leftImageView.setProgress(0.0f);
                    this.leftImageView.playAnimation();
                } else if (i == 82) {
                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                    MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) obj;
                    TextView textView2 = this.infoTextView;
                    if (photoEntry.isVideo) {
                        i3 = 2131624510;
                        str = "AttachMediaVideoDeselected";
                    } else {
                        i3 = 2131624505;
                        str = "AttachMediaPhotoDeselected";
                    }
                    textView2.setText(LocaleController.getString(str, i3));
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.avatarImageView.setVisibility(0);
                    this.avatarImageView.setRoundRadius(AndroidUtilities.dp(2.0f));
                    String str14 = photoEntry.thumbPath;
                    if (str14 != null) {
                        this.avatarImageView.setImage(str14, null, Theme.chat_attachEmptyDrawable);
                    } else if (photoEntry.path != null) {
                        this.avatarImageView.setOrientation(photoEntry.orientation, true);
                        if (photoEntry.isVideo) {
                            this.avatarImageView.setImage("vthumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                        } else {
                            this.avatarImageView.setImage("thumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                        }
                    } else {
                        this.avatarImageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                    }
                } else {
                    layoutParams.leftMargin = AndroidUtilities.dp(45.0f);
                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                    layoutParams.rightMargin = 0;
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.undoButton.setVisibility(0);
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.subinfoTextView.setVisibility(8);
                    this.leftImageView.setVisibility(8);
                    int i18 = this.currentAction;
                    if (i18 == 81 || i18 == 0 || i18 == 26) {
                        this.infoTextView.setText(LocaleController.getString("HistoryClearedUndo", 2131626193));
                    } else if (i18 == 27) {
                        this.infoTextView.setText(LocaleController.getString("ChatsDeletedUndo", 2131625070));
                    } else if (DialogObject.isChatDialog(longValue)) {
                        TLRPC$Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
                        if (ChatObject.isChannel(chat4) && !chat4.megagroup) {
                            this.infoTextView.setText(LocaleController.getString("ChannelDeletedUndo", 2131624913));
                        } else {
                            this.infoTextView.setText(LocaleController.getString("GroupDeletedUndo", 2131626137));
                        }
                    } else {
                        this.infoTextView.setText(LocaleController.getString("ChatDeletedUndo", 2131625017));
                    }
                    if (this.currentAction != 81) {
                        for (int i19 = 0; i19 < arrayList.size(); i19++) {
                            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                            long longValue2 = arrayList.get(i19).longValue();
                            int i20 = this.currentAction;
                            messagesController.addDialogAction(longValue2, i20 == 0 || i20 == 26);
                        }
                    }
                }
            }
            z = false;
            StringBuilder sb22 = new StringBuilder();
            sb22.append((Object) this.infoTextView.getText());
            if (this.subinfoTextView.getVisibility() == 0) {
            }
            sb22.append(str12);
            AndroidUtilities.makeAccessibilityAnnouncement(sb22.toString());
            if (isMultilineSubInfo()) {
            }
            if (getVisibility() == 0) {
            }
        }
    }

    public /* synthetic */ void lambda$showWithAction$2(View view) {
        hide(false, 1);
    }

    public /* synthetic */ void lambda$showWithAction$6(TLRPC$Message tLRPC$Message, View view) {
        hide(true, 1);
        TLRPC$TL_payments_getPaymentReceipt tLRPC$TL_payments_getPaymentReceipt = new TLRPC$TL_payments_getPaymentReceipt();
        tLRPC$TL_payments_getPaymentReceipt.msg_id = tLRPC$Message.id;
        tLRPC$TL_payments_getPaymentReceipt.peer = this.parentFragment.getMessagesController().getInputPeer(tLRPC$Message.peer_id);
        this.parentFragment.getConnectionsManager().sendRequest(tLRPC$TL_payments_getPaymentReceipt, new UndoView$$ExternalSyntheticLambda7(this), 2);
    }

    public /* synthetic */ void lambda$showWithAction$5(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new UndoView$$ExternalSyntheticLambda6(this, tLObject));
    }

    public /* synthetic */ void lambda$showWithAction$4(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_payments_paymentReceipt) {
            this.parentFragment.presentFragment(new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject));
        }
    }

    public /* synthetic */ void lambda$showWithAction$7() {
        this.leftImageView.performHapticFeedback(3, 2);
    }

    public void setEnterOffsetMargin(int i) {
        this.enterOffsetMargin = i;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(this.undoViewHeight, 1073741824));
        this.backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.additionalTranslationY != 0.0f) {
            canvas.save();
            float measuredHeight = (getMeasuredHeight() - this.enterOffset) + AndroidUtilities.dp(9.0f);
            if (measuredHeight > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight);
                super.dispatchDraw(canvas);
            }
            canvas.restore();
            return;
        }
        super.dispatchDraw(canvas);
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
        if (i == 1 || i == 0 || i == 27 || i == 26 || i == 81) {
            long j = this.timeLeft;
            int ceil = j > 0 ? (int) Math.ceil(((float) j) / 1000.0f) : 0;
            if (this.prevSeconds != ceil) {
                this.prevSeconds = ceil;
                String format = String.format("%d", Integer.valueOf(Math.max(1, ceil)));
                this.timeLeftString = format;
                StaticLayout staticLayout = this.timeLayout;
                if (staticLayout != null) {
                    this.timeLayoutOut = staticLayout;
                    this.timeReplaceProgress = 0.0f;
                }
                this.textWidth = (int) Math.ceil(this.textPaint.measureText(format));
                this.timeLayout = new StaticLayout(this.timeLeftString, this.textPaint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
            canvas.drawArc(this.rect, -90.0f, (((float) this.timeLeft) / 5000.0f) * (-360.0f), false, this.progressPaint);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j2 = this.timeLeft - (elapsedRealtime - this.lastUpdateTime);
        this.timeLeft = j2;
        this.lastUpdateTime = elapsedRealtime;
        if (j2 <= 0) {
            hide(true, this.hideAnimationType);
        }
        if (this.currentAction != 82) {
            invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        this.infoTextView.invalidate();
        this.leftImageView.invalidate();
    }

    public void setInfoText(CharSequence charSequence) {
        this.infoText = charSequence;
    }

    public void setHideAnimationType(int i) {
        this.hideAnimationType = i;
    }

    @Keep
    public float getEnterOffset() {
        return this.enterOffset;
    }

    @Keep
    public void setEnterOffset(float f) {
        if (this.enterOffset != f) {
            this.enterOffset = f;
            updatePosition();
        }
    }

    private void updatePosition() {
        setTranslationY(((this.enterOffset - this.enterOffsetMargin) + AndroidUtilities.dp(8.0f)) - this.additionalTranslationY);
        invalidate();
    }

    @Override // android.view.View
    public Drawable getBackground() {
        return this.backgroundDrawable;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
