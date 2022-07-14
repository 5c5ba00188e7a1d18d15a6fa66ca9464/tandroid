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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.gms.wearable.WearableStatusCodes;
import com.microsoft.appcenter.Constants;
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
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes5.dex */
public class UndoView extends FrameLayout {
    public static final int ACTION_ADDED_TO_FOLDER = 20;
    public static final int ACTION_ARCHIVE = 2;
    public static final int ACTION_ARCHIVE_FEW = 4;
    public static final int ACTION_ARCHIVE_FEW_HINT = 5;
    public static final int ACTION_ARCHIVE_HIDDEN = 6;
    public static final int ACTION_ARCHIVE_HINT = 3;
    public static final int ACTION_ARCHIVE_PINNED = 7;
    public static final int ACTION_AUTO_DELETE_OFF = 71;
    public static final int ACTION_AUTO_DELETE_ON = 70;
    public static final int ACTION_CACHE_WAS_CLEARED = 19;
    public static final int ACTION_CHAT_UNARCHIVED = 23;
    public static final int ACTION_CLEAR = 0;
    public static final int ACTION_CLEAR_DATES = 81;
    public static final int ACTION_CLEAR_FEW = 26;
    public static final int ACTION_CONTACT_ADDED = 8;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_DELETE_FEW = 27;
    public static final int ACTION_DICE_INFO = 16;
    public static final int ACTION_DICE_NO_SEND_INFO = 17;
    public static final int ACTION_EMAIL_COPIED = 80;
    public static final int ACTION_FILTERS_AVAILABLE = 15;
    public static final int ACTION_FWD_MESSAGES = 53;
    public static final int ACTION_GIGAGROUP_CANCEL = 75;
    public static final int ACTION_GIGAGROUP_SUCCESS = 76;
    public static final int ACTION_HASHTAG_COPIED = 57;
    public static final int ACTION_IMPORT_GROUP_NOT_ADMIN = 46;
    public static final int ACTION_IMPORT_INFO = 47;
    public static final int ACTION_IMPORT_NOT_MUTUAL = 45;
    public static final int ACTION_LINK_COPIED = 59;
    public static final int ACTION_MESSAGE_COPIED = 52;
    public static final int ACTION_NOTIFY_OFF = 55;
    public static final int ACTION_NOTIFY_ON = 54;
    public static final int ACTION_OWNER_TRANSFERED_CHANNEL = 9;
    public static final int ACTION_OWNER_TRANSFERED_GROUP = 10;
    public static final int ACTION_PAYMENT_SUCCESS = 77;
    public static final int ACTION_PHONE_COPIED = 60;
    public static final int ACTION_PIN_DIALOGS = 78;
    public static final int ACTION_PLAYBACK_SPEED_DISABLED = 51;
    public static final int ACTION_PLAYBACK_SPEED_ENABLED = 50;
    public static final int ACTION_PREVIEW_MEDIA_DESELECTED = 82;
    public static final int ACTION_PROFILE_PHOTO_CHANGED = 22;
    public static final int ACTION_PROXIMITY_REMOVED = 25;
    public static final int ACTION_PROXIMITY_SET = 24;
    public static final int ACTION_QR_SESSION_ACCEPTED = 11;
    public static final int ACTION_QUIZ_CORRECT = 13;
    public static final int ACTION_QUIZ_INCORRECT = 14;
    public static final int ACTION_REMOVED_FROM_FOLDER = 21;
    public static final int ACTION_REPORT_SENT = 74;
    public static int ACTION_RINGTONE_ADDED = 83;
    public static final int ACTION_SHARE_BACKGROUND = 61;
    public static final int ACTION_TEXT_COPIED = 58;
    public static final int ACTION_TEXT_INFO = 18;
    public static final int ACTION_THEME_CHANGED = 12;
    public static final int ACTION_UNPIN_DIALOGS = 79;
    public static final int ACTION_USERNAME_COPIED = 56;
    public static final int ACTION_VOIP_CAN_NOW_SPEAK = 38;
    public static final int ACTION_VOIP_INVITED = 34;
    public static final int ACTION_VOIP_INVITE_LINK_SENT = 41;
    public static final int ACTION_VOIP_LINK_COPIED = 33;
    public static final int ACTION_VOIP_MUTED = 30;
    public static final int ACTION_VOIP_MUTED_FOR_YOU = 35;
    public static final int ACTION_VOIP_RECORDING_FINISHED = 40;
    public static final int ACTION_VOIP_RECORDING_STARTED = 39;
    public static final int ACTION_VOIP_REMOVED = 32;
    public static final int ACTION_VOIP_SOUND_MUTED = 42;
    public static final int ACTION_VOIP_SOUND_UNMUTED = 43;
    public static final int ACTION_VOIP_UNMUTED = 31;
    public static final int ACTION_VOIP_UNMUTED_FOR_YOU = 36;
    public static final int ACTION_VOIP_USER_CHANGED = 37;
    public static final int ACTION_VOIP_USER_JOINED = 44;
    public static final int ACTION_VOIP_VIDEO_RECORDING_FINISHED = 101;
    public static final int ACTION_VOIP_VIDEO_RECORDING_STARTED = 100;
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

    /* loaded from: classes5.dex */
    public class LinkMovementMethodMy extends LinkMovementMethod {
        public LinkMovementMethodMy() {
            UndoView.this = this$0;
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            CharacterStyle[] links;
            try {
                if (event.getAction() == 0 && ((links = (CharacterStyle[]) buffer.getSpans(widget.getSelectionStart(), widget.getSelectionEnd(), CharacterStyle.class)) == null || links.length == 0)) {
                    return false;
                }
                if (event.getAction() == 1) {
                    CharacterStyle[] links2 = (CharacterStyle[]) buffer.getSpans(widget.getSelectionStart(), widget.getSelectionEnd(), CharacterStyle.class);
                    if (links2 != null && links2.length > 0) {
                        UndoView.this.didPressUrl(links2[0]);
                    }
                    Selection.removeSelection(buffer);
                    return true;
                }
                boolean result = super.onTouchEvent(widget, buffer, event);
                return result;
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    public UndoView(Context context) {
        this(context, null, false, null);
    }

    public UndoView(Context context, BaseFragment parent) {
        this(context, parent, false, null);
    }

    public UndoView(Context context, BaseFragment parent, boolean top, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.currentAction = -1;
        this.hideAnimationType = 1;
        this.enterOffsetMargin = AndroidUtilities.dp(8.0f);
        this.timeReplaceProgress = 1.0f;
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = parent;
        this.fromTop = top;
        TextView textView = new TextView(context);
        this.infoTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.infoTextView.setTextColor(getThemedColor(Theme.key_undo_infoColor));
        this.infoTextView.setLinkTextColor(getThemedColor(Theme.key_undo_cancelColor));
        this.infoTextView.setMovementMethod(new LinkMovementMethodMy());
        addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 45.0f, 13.0f, 0.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.subinfoTextView = textView2;
        textView2.setTextSize(1, 13.0f);
        this.subinfoTextView.setTextColor(getThemedColor(Theme.key_undo_infoColor));
        this.subinfoTextView.setLinkTextColor(getThemedColor(Theme.key_undo_cancelColor));
        this.subinfoTextView.setHighlightColor(0);
        this.subinfoTextView.setSingleLine(true);
        this.subinfoTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.subinfoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        addView(this.subinfoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 58.0f, 27.0f, 8.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.leftImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.leftImageView.setLayerColor("info1.**", getThemedColor(Theme.key_undo_background) | (-16777216));
        this.leftImageView.setLayerColor("info2.**", getThemedColor(Theme.key_undo_background) | (-16777216));
        this.leftImageView.setLayerColor("luc12.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc11.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc10.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc9.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc8.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc7.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc6.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc5.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc4.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc3.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc2.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("luc1.**", getThemedColor(Theme.key_undo_infoColor));
        this.leftImageView.setLayerColor("Oval.**", getThemedColor(Theme.key_undo_infoColor));
        addView(this.leftImageView, LayoutHelper.createFrame(54, -2.0f, 19, 3.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(15.0f));
        addView(this.avatarImageView, LayoutHelper.createFrame(30, 30.0f, 19, 15.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.undoButton = linearLayout;
        linearLayout.setOrientation(0);
        this.undoButton.setBackground(Theme.createRadSelectorDrawable(getThemedColor(Theme.key_undo_cancelColor) & 587202559, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f)));
        addView(this.undoButton, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 11.0f, 0.0f));
        this.undoButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UndoView.this.m3186lambda$new$0$orgtelegramuiComponentsUndoView(view);
            }
        });
        ImageView imageView = new ImageView(context);
        this.undoImageView = imageView;
        imageView.setImageResource(R.drawable.chats_undo);
        this.undoImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_undo_cancelColor), PorterDuff.Mode.MULTIPLY));
        this.undoButton.addView(this.undoImageView, LayoutHelper.createLinear(-2, -2, 19, 4, 4, 0, 4));
        TextView textView3 = new TextView(context);
        this.undoTextView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.undoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
        this.undoTextView.setText(LocaleController.getString("Undo", R.string.Undo));
        this.undoButton.addView(this.undoTextView, LayoutHelper.createLinear(-2, -2, 19, 6, 4, 8, 4));
        this.rect = new RectF(AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), AndroidUtilities.dp(33.0f), AndroidUtilities.dp(33.0f));
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setColor(getThemedColor(Theme.key_undo_infoColor));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.textPaint.setColor(getThemedColor(Theme.key_undo_infoColor));
        setWillNotDraw(false);
        this.backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor(Theme.key_undo_background));
        setOnTouchListener(UndoView$$ExternalSyntheticLambda3.INSTANCE);
        setVisibility(4);
    }

    /* renamed from: lambda$new$0$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3186lambda$new$0$orgtelegramuiComponentsUndoView(View v) {
        if (!canUndo()) {
            return;
        }
        hide(false, 1);
    }

    public static /* synthetic */ boolean lambda$new$1(View v, MotionEvent event) {
        return true;
    }

    public void setColors(int background, int text) {
        Theme.setDrawableColor(this.backgroundDrawable, background);
        this.infoTextView.setTextColor(text);
        this.subinfoTextView.setTextColor(text);
        this.leftImageView.setLayerColor("info1.**", background | (-16777216));
        this.leftImageView.setLayerColor("info2.**", (-16777216) | background);
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

    public void setAdditionalTranslationY(float value) {
        if (this.additionalTranslationY != value) {
            this.additionalTranslationY = value;
            updatePosition();
        }
    }

    public Object getCurrentInfoObject() {
        return this.currentInfoObject;
    }

    public void hide(boolean apply, int animated) {
        if (getVisibility() != 0 || !this.isShown) {
            return;
        }
        this.currentInfoObject = null;
        this.isShown = false;
        Runnable runnable = this.currentActionRunnable;
        if (runnable != null) {
            if (apply) {
                runnable.run();
            }
            this.currentActionRunnable = null;
        }
        Runnable runnable2 = this.currentCancelRunnable;
        if (runnable2 != null) {
            if (!apply) {
                runnable2.run();
            }
            this.currentCancelRunnable = null;
        }
        int i = this.currentAction;
        if (i == 0 || i == 1 || i == 26 || i == 27) {
            for (int a = 0; a < this.currentDialogIds.size(); a++) {
                long did = this.currentDialogIds.get(a).longValue();
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                int i2 = this.currentAction;
                messagesController.removeDialogAction(did, i2 == 0 || i2 == 26, apply);
                onRemoveDialogAction(did, this.currentAction);
            }
        }
        int a2 = -1082130432;
        if (animated != 0) {
            AnimatorSet animatorSet = new AnimatorSet();
            if (animated == 1) {
                Animator[] animatorArr = new Animator[1];
                float[] fArr = new float[1];
                if (!this.fromTop) {
                    a2 = 1065353216;
                }
                fArr[0] = a2 * (this.enterOffsetMargin + this.undoViewHeight);
                animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
                animatorSet.playTogether(animatorArr);
                animatorSet.setDuration(250L);
            } else {
                animatorSet.playTogether(ObjectAnimator.ofFloat(this, View.SCALE_X, 0.8f), ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.8f), ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f));
                animatorSet.setDuration(180L);
            }
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.UndoView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    UndoView.this.setVisibility(4);
                    UndoView.this.setScaleX(1.0f);
                    UndoView.this.setScaleY(1.0f);
                    UndoView.this.setAlpha(1.0f);
                }
            });
            animatorSet.start();
            return;
        }
        if (!this.fromTop) {
            a2 = 1065353216;
        }
        setEnterOffset(a2 * (this.enterOffsetMargin + this.undoViewHeight));
        setVisibility(4);
    }

    protected void onRemoveDialogAction(long currentDialogId, int action) {
    }

    public void didPressUrl(CharacterStyle span) {
    }

    public void showWithAction(long did, int action, Runnable actionRunnable) {
        showWithAction(did, action, (Object) null, (Object) null, actionRunnable, (Runnable) null);
    }

    public void showWithAction(long did, int action, Object infoObject) {
        showWithAction(did, action, infoObject, (Object) null, (Runnable) null, (Runnable) null);
    }

    public void showWithAction(long did, int action, Runnable actionRunnable, Runnable cancelRunnable) {
        showWithAction(did, action, (Object) null, (Object) null, actionRunnable, cancelRunnable);
    }

    public void showWithAction(long did, int action, Object infoObject, Runnable actionRunnable, Runnable cancelRunnable) {
        showWithAction(did, action, infoObject, (Object) null, actionRunnable, cancelRunnable);
    }

    public void showWithAction(long did, int action, Object infoObject, Object infoObject2, Runnable actionRunnable, Runnable cancelRunnable) {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(did));
        showWithAction(ids, action, infoObject, infoObject2, actionRunnable, cancelRunnable);
    }

    /* JADX WARN: Removed duplicated region for block: B:300:0x0986  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x09c5  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x09d0  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x0a14  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0f00  */
    /* JADX WARN: Removed duplicated region for block: B:620:0x18ea  */
    /* JADX WARN: Removed duplicated region for block: B:623:0x1911  */
    /* JADX WARN: Removed duplicated region for block: B:627:0x1957  */
    /* JADX WARN: Removed duplicated region for block: B:653:0x1a00  */
    /* JADX WARN: Removed duplicated region for block: B:685:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showWithAction(ArrayList<Long> dialogIds, int action, Object infoObject, Object infoObject2, Runnable actionRunnable, Runnable cancelRunnable) {
        boolean reversedPlay;
        FrameLayout.LayoutParams layoutParams2;
        long hapticDelay;
        FrameLayout.LayoutParams layoutParams22;
        int margin;
        String str;
        int i;
        CharSequence subInfoText;
        CharSequence infoText;
        int size;
        int icon;
        boolean reversedPlay2;
        int size2;
        String str2;
        int i2;
        int size3;
        String str3;
        int i3;
        CharSequence subInfoText2;
        int icon2;
        Exception e;
        StringBuilder sb;
        int size4;
        int size5;
        CharSequence infoText2;
        int icon3;
        String str4;
        int i4;
        String name;
        String name2;
        String name3;
        String name4;
        String name5;
        String name6;
        int i5;
        if (!AndroidUtilities.shouldShowClipboardToast() && ((i5 = this.currentAction) == 52 || i5 == 56 || i5 == 57 || i5 == 58 || i5 == 59 || i5 == 60 || i5 == 80 || i5 == 33)) {
            return;
        }
        Runnable runnable = this.currentActionRunnable;
        if (runnable != null) {
            runnable.run();
        }
        this.isShown = true;
        this.currentActionRunnable = actionRunnable;
        this.currentCancelRunnable = cancelRunnable;
        this.currentDialogIds = dialogIds;
        long did = dialogIds.get(0).longValue();
        this.currentAction = action;
        this.timeLeft = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
        this.currentInfoObject = infoObject;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
        this.undoTextView.setText(LocaleController.getString("Undo", R.string.Undo).toUpperCase());
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
        FrameLayout.LayoutParams layoutParams23 = (FrameLayout.LayoutParams) this.leftImageView.getLayoutParams();
        layoutParams23.gravity = 19;
        layoutParams23.bottomMargin = 0;
        layoutParams23.topMargin = 0;
        layoutParams23.leftMargin = AndroidUtilities.dp(3.0f);
        layoutParams23.width = AndroidUtilities.dp(54.0f);
        layoutParams23.height = -2;
        this.infoTextView.setMinHeight(0);
        boolean infoOnly = false;
        if ((actionRunnable == null && cancelRunnable == null) || action == ACTION_RINGTONE_ADDED) {
            setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    UndoView.this.m3187lambda$showWithAction$2$orgtelegramuiComponentsUndoView(view);
                }
            });
            setOnTouchListener(null);
        } else {
            setOnClickListener(null);
            setOnTouchListener(UndoView$$ExternalSyntheticLambda4.INSTANCE);
        }
        this.infoTextView.setMovementMethod(null);
        String str5 = "";
        FrameLayout.LayoutParams layoutParams24 = layoutParams23;
        if (!isTooltipAction()) {
            int i6 = this.currentAction;
            if (i6 == 45 || i6 == 46 || i6 == 47 || i6 == 51 || i6 == 50 || i6 == 52 || i6 == 53 || i6 == 54 || i6 == 55 || i6 == 56 || i6 == 57 || i6 == 58 || i6 == 59 || i6 == 60 || i6 == 71 || i6 == 70 || i6 == 75 || i6 == 76 || i6 == 41 || i6 == 78 || i6 == 79 || i6 == 61) {
                reversedPlay = false;
                layoutParams2 = layoutParams24;
            } else if (i6 == 80) {
                reversedPlay = false;
                layoutParams2 = layoutParams24;
            } else {
                if (i6 == 24) {
                    reversedPlay = false;
                    layoutParams22 = layoutParams24;
                } else if (i6 == 25) {
                    reversedPlay = false;
                    layoutParams22 = layoutParams24;
                } else {
                    if (i6 == 11) {
                        TLRPC.TL_authorization authorization = (TLRPC.TL_authorization) infoObject;
                        this.infoTextView.setText(LocaleController.getString("AuthAnotherClientOk", R.string.AuthAnotherClientOk));
                        this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                        this.subinfoTextView.setText(authorization.app_name);
                        this.subinfoTextView.setVisibility(0);
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        this.undoTextView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteRedText2));
                        this.undoImageView.setVisibility(8);
                        this.undoButton.setVisibility(0);
                        this.leftImageView.setVisibility(0);
                        this.leftImageView.setProgress(0.0f);
                        this.leftImageView.playAnimation();
                        reversedPlay = false;
                        layoutParams22 = layoutParams24;
                    } else if (i6 == 15) {
                        this.timeLeft = 10000L;
                        this.undoTextView.setText(LocaleController.getString("Open", R.string.Open).toUpperCase());
                        this.infoTextView.setText(LocaleController.getString("FilterAvailableTitle", R.string.FilterAvailableTitle));
                        this.leftImageView.setAnimation(R.raw.filter_new, 36, 36);
                        int margin2 = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.rightMargin = margin2;
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                        ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = margin2;
                        String text = LocaleController.getString("FilterAvailableText", R.string.FilterAvailableText);
                        SpannableStringBuilder builder = new SpannableStringBuilder(text);
                        int index1 = text.indexOf(42);
                        int index2 = text.lastIndexOf(42);
                        if (index1 >= 0 && index2 >= 0 && index1 != index2) {
                            builder.replace(index2, index2 + 1, (CharSequence) str5);
                            builder.replace(index1, index1 + 1, (CharSequence) str5);
                            builder.setSpan(new URLSpanNoUnderline("tg://settings/folders"), index1, index2 - 1, 33);
                        }
                        this.subinfoTextView.setText(builder);
                        this.subinfoTextView.setVisibility(0);
                        this.subinfoTextView.setSingleLine(false);
                        this.subinfoTextView.setMaxLines(2);
                        this.undoButton.setVisibility(0);
                        this.undoImageView.setVisibility(8);
                        this.leftImageView.setVisibility(0);
                        this.leftImageView.setProgress(0.0f);
                        this.leftImageView.playAnimation();
                        reversedPlay = false;
                    } else {
                        if (i6 == 16) {
                            reversedPlay = false;
                            layoutParams22 = layoutParams24;
                        } else if (i6 == 17) {
                            reversedPlay = false;
                            layoutParams22 = layoutParams24;
                        } else if (i6 == 18) {
                            CharSequence info = (CharSequence) infoObject;
                            this.timeLeft = Math.max((int) WearableStatusCodes.TARGET_NODE_NOT_CONNECTED, Math.min((info.length() / 50) * 1600, 10000));
                            this.infoTextView.setTextSize(1, 14.0f);
                            this.infoTextView.setGravity(16);
                            this.infoTextView.setText(info);
                            this.undoTextView.setVisibility(8);
                            this.undoButton.setVisibility(8);
                            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                            layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                            layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                            layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                            layoutParams.height = -1;
                            layoutParams22 = layoutParams24;
                            layoutParams22.gravity = 51;
                            int dp = AndroidUtilities.dp(8.0f);
                            layoutParams22.bottomMargin = dp;
                            layoutParams22.topMargin = dp;
                            this.leftImageView.setVisibility(0);
                            this.leftImageView.setAnimation(R.raw.chats_infotip, 36, 36);
                            this.leftImageView.setProgress(0.0f);
                            this.leftImageView.playAnimation();
                            this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                            reversedPlay = false;
                        } else {
                            layoutParams22 = layoutParams24;
                            if (i6 == 12) {
                                this.infoTextView.setText(LocaleController.getString("ColorThemeChanged", R.string.ColorThemeChanged));
                                this.leftImageView.setImageResource(R.drawable.toast_pallete);
                                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                layoutParams.rightMargin = AndroidUtilities.dp(48.0f);
                                layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                                ((FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams()).rightMargin = AndroidUtilities.dp(48.0f);
                                String text2 = LocaleController.getString("ColorThemeChangedInfo", R.string.ColorThemeChangedInfo);
                                SpannableStringBuilder builder2 = new SpannableStringBuilder(text2);
                                int index12 = text2.indexOf(42);
                                int index22 = text2.lastIndexOf(42);
                                if (index12 >= 0 && index22 >= 0 && index12 != index22) {
                                    builder2.replace(index22, index22 + 1, (CharSequence) str5);
                                    builder2.replace(index12, index12 + 1, (CharSequence) str5);
                                    builder2.setSpan(new URLSpanNoUnderline("tg://settings/themes"), index12, index22 - 1, 33);
                                }
                                this.subinfoTextView.setText(builder2);
                                this.subinfoTextView.setVisibility(0);
                                this.subinfoTextView.setSingleLine(false);
                                this.subinfoTextView.setMaxLines(2);
                                this.undoTextView.setVisibility(8);
                                this.undoButton.setVisibility(0);
                                this.leftImageView.setVisibility(0);
                                layoutParams24 = layoutParams22;
                                reversedPlay = false;
                            } else {
                                if (i6 == 2) {
                                    reversedPlay = false;
                                } else if (i6 == 4) {
                                    reversedPlay = false;
                                } else if (action == 82) {
                                    layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                    MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) infoObject;
                                    TextView textView = this.infoTextView;
                                    if (photoEntry.isVideo) {
                                        i = R.string.AttachMediaVideoDeselected;
                                        str = "AttachMediaVideoDeselected";
                                    } else {
                                        i = R.string.AttachMediaPhotoDeselected;
                                        str = "AttachMediaPhotoDeselected";
                                    }
                                    textView.setText(LocaleController.getString(str, i));
                                    this.undoButton.setVisibility(0);
                                    this.infoTextView.setTextSize(1, 15.0f);
                                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                                    this.subinfoTextView.setVisibility(8);
                                    this.avatarImageView.setVisibility(0);
                                    this.avatarImageView.setRoundRadius(AndroidUtilities.dp(2.0f));
                                    if (photoEntry.thumbPath != null) {
                                        this.avatarImageView.setImage(photoEntry.thumbPath, null, Theme.chat_attachEmptyDrawable);
                                    } else if (photoEntry.path != null) {
                                        this.avatarImageView.setOrientation(photoEntry.orientation, true);
                                        if (photoEntry.isVideo) {
                                            this.avatarImageView.setImage("vthumb://" + photoEntry.imageId + Constants.COMMON_SCHEMA_PREFIX_SEPARATOR + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                                        } else {
                                            this.avatarImageView.setImage("thumb://" + photoEntry.imageId + Constants.COMMON_SCHEMA_PREFIX_SEPARATOR + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                                        }
                                    } else {
                                        this.avatarImageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                                    }
                                    reversedPlay = false;
                                } else {
                                    layoutParams.leftMargin = AndroidUtilities.dp(45.0f);
                                    layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                                    layoutParams.rightMargin = 0;
                                    this.infoTextView.setTextSize(1, 15.0f);
                                    this.undoButton.setVisibility(0);
                                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                                    this.subinfoTextView.setVisibility(8);
                                    this.leftImageView.setVisibility(8);
                                    int i7 = this.currentAction;
                                    if (i7 == 81 || i7 == 0) {
                                        reversedPlay = false;
                                    } else if (i7 == 26) {
                                        reversedPlay = false;
                                    } else {
                                        if (i7 == 27) {
                                            this.infoTextView.setText(LocaleController.getString("ChatsDeletedUndo", R.string.ChatsDeletedUndo));
                                            reversedPlay = false;
                                        } else if (DialogObject.isChatDialog(did)) {
                                            reversedPlay = false;
                                            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-did));
                                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                                this.infoTextView.setText(LocaleController.getString("ChannelDeletedUndo", R.string.ChannelDeletedUndo));
                                            } else {
                                                this.infoTextView.setText(LocaleController.getString("GroupDeletedUndo", R.string.GroupDeletedUndo));
                                            }
                                        } else {
                                            reversedPlay = false;
                                            this.infoTextView.setText(LocaleController.getString("ChatDeletedUndo", R.string.ChatDeletedUndo));
                                        }
                                        if (this.currentAction != 81) {
                                            for (int a = 0; a < dialogIds.size(); a++) {
                                                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                long longValue = dialogIds.get(a).longValue();
                                                int i8 = this.currentAction;
                                                messagesController.addDialogAction(longValue, i8 == 0 || i8 == 26);
                                            }
                                        }
                                    }
                                    this.infoTextView.setText(LocaleController.getString("HistoryClearedUndo", R.string.HistoryClearedUndo));
                                    if (this.currentAction != 81) {
                                    }
                                }
                                if (action == 2) {
                                    this.infoTextView.setText(LocaleController.getString("ChatArchived", R.string.ChatArchived));
                                } else {
                                    this.infoTextView.setText(LocaleController.getString("ChatsArchived", R.string.ChatsArchived));
                                }
                                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                                layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                                layoutParams.rightMargin = 0;
                                this.infoTextView.setTextSize(1, 15.0f);
                                this.undoButton.setVisibility(0);
                                this.infoTextView.setTypeface(Typeface.DEFAULT);
                                this.subinfoTextView.setVisibility(8);
                                this.leftImageView.setVisibility(0);
                                this.leftImageView.setAnimation(R.raw.chats_archived, 36, 36);
                                this.leftImageView.setProgress(0.0f);
                                this.leftImageView.playAnimation();
                            }
                        }
                        this.timeLeft = 4000L;
                        this.infoTextView.setTextSize(1, 14.0f);
                        this.infoTextView.setGravity(16);
                        this.infoTextView.setMinHeight(AndroidUtilities.dp(30.0f));
                        String emoji = (String) infoObject;
                        if ("🎲".equals(emoji)) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DiceInfo2", R.string.DiceInfo2)));
                            this.leftImageView.setImageResource(R.drawable.dice);
                        } else {
                            if ("🎯".equals(emoji)) {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DartInfo", R.string.DartInfo)));
                            } else {
                                String info2 = LocaleController.getServerString("DiceEmojiInfo_" + emoji);
                                if (TextUtils.isEmpty(info2)) {
                                    this.infoTextView.setText(Emoji.replaceEmoji(LocaleController.formatString("DiceEmojiInfo", R.string.DiceEmojiInfo, emoji), this.infoTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                } else {
                                    TextView textView2 = this.infoTextView;
                                    textView2.setText(Emoji.replaceEmoji(info2, textView2.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                }
                            }
                            this.leftImageView.setImageDrawable(Emoji.getEmojiDrawable(emoji));
                            this.leftImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            layoutParams.topMargin = AndroidUtilities.dp(14.0f);
                            layoutParams.bottomMargin = AndroidUtilities.dp(14.0f);
                            layoutParams22.leftMargin = AndroidUtilities.dp(14.0f);
                            layoutParams22.width = AndroidUtilities.dp(26.0f);
                            layoutParams22.height = AndroidUtilities.dp(26.0f);
                        }
                        this.undoTextView.setText(LocaleController.getString("SendDice", R.string.SendDice));
                        if (this.currentAction == 16) {
                            margin = ((int) Math.ceil(this.undoTextView.getPaint().measureText(this.undoTextView.getText().toString()))) + AndroidUtilities.dp(26.0f);
                            this.undoTextView.setVisibility(0);
                            this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                            this.undoImageView.setVisibility(8);
                            this.undoButton.setVisibility(0);
                        } else {
                            margin = AndroidUtilities.dp(8.0f);
                            this.undoTextView.setVisibility(8);
                            this.undoButton.setVisibility(8);
                        }
                        layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                        layoutParams.rightMargin = margin;
                        layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                        layoutParams.bottomMargin = AndroidUtilities.dp(7.0f);
                        layoutParams.height = -1;
                        this.subinfoTextView.setVisibility(8);
                        this.leftImageView.setVisibility(0);
                    }
                    layoutParams24 = layoutParams22;
                }
                int radius = ((Integer) infoObject).intValue();
                TLRPC.User user = (TLRPC.User) infoObject2;
                this.undoImageView.setVisibility(8);
                this.leftImageView.setVisibility(0);
                if (radius != 0) {
                    this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.leftImageView.clearLayerColors();
                    this.leftImageView.setLayerColor("BODY.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Wibe Big.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Wibe Big 3.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Wibe Small.**", getThemedColor(Theme.key_undo_infoColor));
                    this.infoTextView.setText(LocaleController.getString("ProximityAlertSet", R.string.ProximityAlertSet));
                    this.leftImageView.setAnimation(R.raw.ic_unmute, 28, 28);
                    this.subinfoTextView.setVisibility(0);
                    this.subinfoTextView.setSingleLine(false);
                    this.subinfoTextView.setMaxLines(3);
                    if (user != null) {
                        this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoUser", R.string.ProximityAlertSetInfoUser, UserObject.getFirstName(user), LocaleController.formatDistance(radius, 2)));
                    } else {
                        this.subinfoTextView.setText(LocaleController.formatString("ProximityAlertSetInfoGroup2", R.string.ProximityAlertSetInfoGroup2, LocaleController.formatDistance(radius, 2)));
                    }
                    this.undoButton.setVisibility(8);
                    layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                } else {
                    this.infoTextView.setTypeface(Typeface.DEFAULT);
                    this.infoTextView.setTextSize(1, 15.0f);
                    this.leftImageView.clearLayerColors();
                    this.leftImageView.setLayerColor("Body Main.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Body Top.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Line.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Curve Big.**", getThemedColor(Theme.key_undo_infoColor));
                    this.leftImageView.setLayerColor("Curve Small.**", getThemedColor(Theme.key_undo_infoColor));
                    layoutParams.topMargin = AndroidUtilities.dp(14.0f);
                    this.infoTextView.setText(LocaleController.getString("ProximityAlertCancelled", R.string.ProximityAlertCancelled));
                    this.leftImageView.setAnimation(R.raw.ic_mute, 28, 28);
                    this.subinfoTextView.setVisibility(8);
                    this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
                    this.undoButton.setVisibility(0);
                }
                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                this.leftImageView.setProgress(0.0f);
                this.leftImageView.playAnimation();
                layoutParams24 = layoutParams22;
            }
            this.undoImageView.setVisibility(8);
            this.leftImageView.setVisibility(0);
            this.infoTextView.setTypeface(Typeface.DEFAULT);
            long hapticDelay2 = -1;
            int i9 = this.currentAction;
            if (i9 == 76) {
                this.infoTextView.setText(LocaleController.getString("BroadcastGroupConvertSuccess", R.string.BroadcastGroupConvertSuccess));
                this.leftImageView.setAnimation(R.raw.gigagroup_convert, 36, 36);
                infoOnly = true;
                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                this.infoTextView.setTextSize(1, 14.0f);
                layoutParams24 = layoutParams2;
            } else if (i9 == 75) {
                this.infoTextView.setText(LocaleController.getString("GigagroupConvertCancelHint", R.string.GigagroupConvertCancelHint));
                this.leftImageView.setAnimation(R.raw.chats_infotip, 36, 36);
                infoOnly = true;
                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                this.infoTextView.setTextSize(1, 14.0f);
                layoutParams24 = layoutParams2;
            } else if (action == 70) {
                TLRPC.User user2 = (TLRPC.User) infoObject;
                int ttl = ((Integer) infoObject2).intValue();
                this.subinfoTextView.setSingleLine(false);
                String time = LocaleController.formatTTLString(ttl);
                layoutParams24 = layoutParams2;
                this.infoTextView.setText(LocaleController.formatString("AutoDeleteHintOnText", R.string.AutoDeleteHintOnText, time));
                this.leftImageView.setAnimation(R.raw.fire_on, 36, 36);
                layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                this.timeLeft = 4000L;
                infoOnly = true;
                this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
                hapticDelay2 = -1;
            } else {
                layoutParams24 = layoutParams2;
                if (i9 == 71) {
                    this.infoTextView.setText(LocaleController.getString("AutoDeleteHintOffText", R.string.AutoDeleteHintOffText));
                    this.leftImageView.setAnimation(R.raw.fire_off, 36, 36);
                    this.infoTextView.setTextSize(1, 14.0f);
                    this.timeLeft = 3000L;
                    this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(4.0f));
                } else if (i9 == 45) {
                    this.infoTextView.setText(LocaleController.getString("ImportMutualError", R.string.ImportMutualError));
                    this.leftImageView.setAnimation(R.raw.error, 36, 36);
                    infoOnly = true;
                    layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                    this.infoTextView.setTextSize(1, 14.0f);
                    hapticDelay2 = -1;
                } else if (i9 == 46) {
                    this.infoTextView.setText(LocaleController.getString("ImportNotAdmin", R.string.ImportNotAdmin));
                    this.leftImageView.setAnimation(R.raw.error, 36, 36);
                    infoOnly = true;
                    layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                    this.infoTextView.setTextSize(1, 14.0f);
                    hapticDelay2 = -1;
                } else if (i9 == 47) {
                    this.infoTextView.setText(LocaleController.getString("ImportedInfo", R.string.ImportedInfo));
                    this.leftImageView.setAnimation(R.raw.imported, 36, 36);
                    this.leftImageView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0f));
                    infoOnly = true;
                    layoutParams.topMargin = AndroidUtilities.dp(9.0f);
                    this.infoTextView.setTextSize(1, 14.0f);
                    hapticDelay2 = -1;
                } else if (i9 == 51) {
                    this.infoTextView.setText(LocaleController.getString("AudioSpeedNormal", R.string.AudioSpeedNormal));
                    this.leftImageView.setAnimation(R.raw.audio_stop_speed, 36, 36);
                    this.timeLeft = 3000L;
                    this.infoTextView.setTextSize(1, 15.0f);
                } else if (i9 == 50) {
                    this.infoTextView.setText(LocaleController.getString("AudioSpeedFast", R.string.AudioSpeedFast));
                    this.leftImageView.setAnimation(R.raw.audio_speed, 36, 36);
                    this.timeLeft = 3000L;
                    this.infoTextView.setTextSize(1, 15.0f);
                } else if (i9 == 52 || i9 == 56 || i9 == 57 || i9 == 58 || i9 == 59 || i9 == 60 || i9 == 80) {
                    if (!AndroidUtilities.shouldShowClipboardToast()) {
                        return;
                    }
                    int iconRawId = R.raw.copy;
                    int i10 = this.currentAction;
                    if (i10 == 80) {
                        this.infoTextView.setText(LocaleController.getString("EmailCopied", R.string.EmailCopied));
                    } else if (i10 == 60) {
                        this.infoTextView.setText(LocaleController.getString("PhoneCopied", R.string.PhoneCopied));
                    } else if (i10 == 56) {
                        this.infoTextView.setText(LocaleController.getString("UsernameCopied", R.string.UsernameCopied));
                    } else if (i10 == 57) {
                        this.infoTextView.setText(LocaleController.getString("HashtagCopied", R.string.HashtagCopied));
                    } else if (i10 == 52) {
                        this.infoTextView.setText(LocaleController.getString("MessageCopied", R.string.MessageCopied));
                    } else if (i10 == 59) {
                        iconRawId = R.raw.voip_invite;
                        this.infoTextView.setText(LocaleController.getString("LinkCopied", R.string.LinkCopied));
                    } else {
                        this.infoTextView.setText(LocaleController.getString("TextCopied", R.string.TextCopied));
                    }
                    this.leftImageView.setAnimation(iconRawId, 30, 30);
                    this.timeLeft = 3000L;
                    this.infoTextView.setTextSize(1, 15.0f);
                } else if (i9 == 54) {
                    this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOn", R.string.ChannelNotifyMembersInfoOn));
                    this.leftImageView.setAnimation(R.raw.silent_unmute, 30, 30);
                    this.timeLeft = 3000L;
                    this.infoTextView.setTextSize(1, 15.0f);
                } else if (i9 == 55) {
                    this.infoTextView.setText(LocaleController.getString("ChannelNotifyMembersInfoOff", R.string.ChannelNotifyMembersInfoOff));
                    this.leftImageView.setAnimation(R.raw.silent_mute, 30, 30);
                    this.timeLeft = 3000L;
                    this.infoTextView.setTextSize(1, 15.0f);
                } else if (i9 == 41) {
                    if (infoObject2 == null) {
                        if (did == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("InvLinkToSavedMessages", R.string.InvLinkToSavedMessages)));
                        } else if (DialogObject.isChatDialog(did)) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToGroup", R.string.InvLinkToGroup, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-did)).title)));
                        } else {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToUser", R.string.InvLinkToUser, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(did))))));
                        }
                    } else {
                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("InvLinkToChats", R.string.InvLinkToChats, LocaleController.formatPluralString("Chats", ((Integer) infoObject2).intValue(), new Object[0]))));
                    }
                    this.leftImageView.setAnimation(R.raw.contact_check, 36, 36);
                    this.timeLeft = 3000L;
                } else if (i9 == 53) {
                    Integer count = (Integer) infoObject;
                    if (infoObject2 == null) {
                        if (did == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            if (count.intValue() == 1) {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessageToSavedMessages", R.string.FwdMessageToSavedMessages)));
                            } else {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("FwdMessagesToSavedMessages", R.string.FwdMessagesToSavedMessages)));
                            }
                            this.leftImageView.setAnimation(R.raw.saved_messages, 30, 30);
                            hapticDelay = -1;
                        } else {
                            if (DialogObject.isChatDialog(did)) {
                                TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-did));
                                if (count.intValue() != 1) {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToGroup", R.string.FwdMessagesToGroup, chat2.title)));
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToGroup", R.string.FwdMessageToGroup, chat2.title)));
                                }
                            } else {
                                TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(did));
                                if (count.intValue() != 1) {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToUser", R.string.FwdMessagesToUser, UserObject.getFirstName(user3))));
                                } else {
                                    this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToUser", R.string.FwdMessageToUser, UserObject.getFirstName(user3))));
                                }
                            }
                            this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                            hapticDelay = 300;
                        }
                    } else {
                        int amount = ((Integer) infoObject2).intValue();
                        if (count.intValue() != 1) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessagesToChats", R.string.FwdMessagesToChats, LocaleController.formatPluralString("Chats", amount, new Object[0]))));
                        } else {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("FwdMessageToChats", R.string.FwdMessageToChats, LocaleController.formatPluralString("Chats", amount, new Object[0]))));
                        }
                        this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                        hapticDelay = 300;
                    }
                    this.timeLeft = 3000L;
                    hapticDelay2 = hapticDelay;
                } else if (i9 == 61) {
                    Integer num = (Integer) infoObject;
                    if (infoObject2 == null) {
                        if (did == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("BackgroundToSavedMessages", R.string.BackgroundToSavedMessages)));
                            this.leftImageView.setAnimation(R.raw.saved_messages, 30, 30);
                        } else {
                            if (DialogObject.isChatDialog(did)) {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToGroup", R.string.BackgroundToGroup, MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-did)).title)));
                            } else {
                                this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToUser", R.string.BackgroundToUser, UserObject.getFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(did))))));
                            }
                            this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                        }
                    } else {
                        this.infoTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BackgroundToChats", R.string.BackgroundToChats, LocaleController.formatPluralString("Chats", ((Integer) infoObject2).intValue(), new Object[0]))));
                        this.leftImageView.setAnimation(R.raw.forward, 30, 30);
                    }
                    this.timeLeft = 3000L;
                }
                hapticDelay2 = -1;
            }
            this.subinfoTextView.setVisibility(8);
            this.undoTextView.setTextColor(getThemedColor(Theme.key_undo_cancelColor));
            this.undoButton.setVisibility(8);
            layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
            layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
            this.leftImageView.setProgress(0.0f);
            this.leftImageView.playAnimation();
            if (hapticDelay2 > 0) {
                this.leftImageView.postDelayed(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        UndoView.this.m3191lambda$showWithAction$7$orgtelegramuiComponentsUndoView();
                    }
                }, hapticDelay2);
            }
        } else {
            if (action == ACTION_RINGTONE_ADDED) {
                this.subinfoTextView.setSingleLine(false);
                infoText = LocaleController.getString("SoundAdded", R.string.SoundAdded);
                subInfoText = AndroidUtilities.replaceSingleTag(LocaleController.getString("SoundAddedSubtitle", R.string.SoundAddedSubtitle), actionRunnable);
                this.currentActionRunnable = null;
                this.timeLeft = 4000L;
                size = 36;
                icon = R.raw.sound_download;
            } else if (action == 74) {
                this.subinfoTextView.setSingleLine(false);
                infoText = LocaleController.getString("ReportChatSent", R.string.ReportChatSent);
                subInfoText = LocaleController.formatString("ReportSentInfo", R.string.ReportSentInfo, new Object[0]);
                this.timeLeft = 4000L;
                size = 36;
                icon = R.raw.ic_admin;
            } else if (action == 34) {
                TLRPC.User user4 = (TLRPC.User) infoObject;
                CharSequence infoText3 = ChatObject.isChannelOrGiga((TLRPC.Chat) infoObject2) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelInvitedUser", R.string.VoipChannelInvitedUser, UserObject.getFirstName(user4))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupInvitedUser", R.string.VoipGroupInvitedUser, UserObject.getFirstName(user4)));
                subInfoText = null;
                AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
                avatarDrawable.setInfo(user4);
                this.avatarImageView.setForUserOrChat(user4, avatarDrawable);
                this.avatarImageView.setVisibility(0);
                this.timeLeft = 3000L;
                size = 36;
                infoText = infoText3;
                icon = 0;
            } else if (action == 44) {
                TLRPC.Chat currentChat = (TLRPC.Chat) infoObject2;
                if (infoObject instanceof TLRPC.User) {
                    TLRPC.User user5 = (TLRPC.User) infoObject;
                    infoText = ChatObject.isChannelOrGiga(currentChat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserJoined", R.string.VoipChannelUserJoined, UserObject.getFirstName(user5))) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatUserJoined", R.string.VoipChatUserJoined, UserObject.getFirstName(user5)));
                } else {
                    TLRPC.Chat chat3 = (TLRPC.Chat) infoObject;
                    infoText = ChatObject.isChannelOrGiga(currentChat) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelChatJoined", R.string.VoipChannelChatJoined, chat3.title)) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipChatChatJoined", R.string.VoipChatChatJoined, chat3.title));
                }
                subInfoText = null;
                AvatarDrawable avatarDrawable2 = new AvatarDrawable();
                avatarDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
                avatarDrawable2.setInfo((TLObject) infoObject);
                this.avatarImageView.setForUserOrChat((TLObject) infoObject, avatarDrawable2);
                this.avatarImageView.setVisibility(0);
                this.timeLeft = 3000L;
                size = 36;
                icon = 0;
            } else if (action == 37) {
                AvatarDrawable avatarDrawable3 = new AvatarDrawable();
                avatarDrawable3.setTextSize(AndroidUtilities.dp(12.0f));
                if (infoObject instanceof TLRPC.User) {
                    TLRPC.User user6 = (TLRPC.User) infoObject;
                    avatarDrawable3.setInfo(user6);
                    this.avatarImageView.setForUserOrChat(user6, avatarDrawable3);
                    name6 = ContactsController.formatName(user6.first_name, user6.last_name);
                } else {
                    TLRPC.Chat chat4 = (TLRPC.Chat) infoObject;
                    avatarDrawable3.setInfo(chat4);
                    this.avatarImageView.setForUserOrChat(chat4, avatarDrawable3);
                    name6 = chat4.title;
                }
                CharSequence infoText4 = ChatObject.isChannelOrGiga((TLRPC.Chat) infoObject2) ? AndroidUtilities.replaceTags(LocaleController.formatString("VoipChannelUserChanged", R.string.VoipChannelUserChanged, name6)) : AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserChanged", R.string.VoipGroupUserChanged, name6));
                subInfoText = null;
                CharSequence infoText5 = infoText4;
                this.avatarImageView.setVisibility(0);
                this.timeLeft = 3000L;
                icon = 0;
                size = 36;
                infoText = infoText5;
            } else if (action == 33) {
                infoText = LocaleController.getString("VoipGroupCopyInviteLinkCopied", R.string.VoipGroupCopyInviteLinkCopied);
                subInfoText = null;
                this.timeLeft = 3000L;
                icon = R.raw.voip_invite;
                size = 36;
            } else if (action == 77) {
                infoText = (CharSequence) infoObject;
                subInfoText = null;
                this.timeLeft = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
                if (this.parentFragment != null && (infoObject2 instanceof TLRPC.Message)) {
                    final TLRPC.Message message = (TLRPC.Message) infoObject2;
                    setOnTouchListener(null);
                    this.infoTextView.setMovementMethod(null);
                    setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            UndoView.this.m3190lambda$showWithAction$6$orgtelegramuiComponentsUndoView(message, view);
                        }
                    });
                }
                size = 36;
                icon = R.raw.payment_success;
            } else if (action == 30) {
                if (infoObject instanceof TLRPC.User) {
                    name5 = UserObject.getFirstName((TLRPC.User) infoObject);
                } else {
                    name5 = ((TLRPC.Chat) infoObject).title;
                }
                infoText = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeak", R.string.VoipGroupUserCantNowSpeak, name5));
                subInfoText = null;
                this.timeLeft = 3000L;
                size = 36;
                icon = R.raw.voip_muted;
            } else if (action == 35) {
                if (infoObject instanceof TLRPC.User) {
                    name4 = UserObject.getFirstName((TLRPC.User) infoObject);
                } else if (infoObject instanceof TLRPC.Chat) {
                    name4 = ((TLRPC.Chat) infoObject).title;
                } else {
                    name4 = "";
                }
                infoText = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCantNowSpeakForYou", R.string.VoipGroupUserCantNowSpeakForYou, name4));
                subInfoText = null;
                this.timeLeft = 3000L;
                size = 36;
                icon = R.raw.voip_muted;
            } else if (action == 31) {
                if (infoObject instanceof TLRPC.User) {
                    name3 = UserObject.getFirstName((TLRPC.User) infoObject);
                } else {
                    name3 = ((TLRPC.Chat) infoObject).title;
                }
                infoText = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeak", R.string.VoipGroupUserCanNowSpeak, name3));
                subInfoText = null;
                this.timeLeft = 3000L;
                size = 36;
                icon = R.raw.voip_unmuted;
            } else if (action == 38) {
                if (infoObject instanceof TLRPC.Chat) {
                    CharSequence infoText6 = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupYouCanNowSpeakIn", R.string.VoipGroupYouCanNowSpeakIn, ((TLRPC.Chat) infoObject).title));
                    infoText = infoText6;
                } else {
                    infoText = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupYouCanNowSpeak", R.string.VoipGroupYouCanNowSpeak));
                }
                subInfoText = null;
                this.timeLeft = 3000L;
                icon = R.raw.voip_allow_talk;
                size = 36;
            } else if (action == 42) {
                if (ChatObject.isChannelOrGiga((TLRPC.Chat) infoObject)) {
                    infoText = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundMuted", R.string.VoipChannelSoundMuted));
                } else {
                    infoText = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundMuted", R.string.VoipGroupSoundMuted));
                }
                subInfoText = null;
                this.timeLeft = 3000L;
                size = 36;
                icon = R.raw.ic_mute;
            } else if (action != 43) {
                int i11 = this.currentAction;
                if (i11 == 39) {
                    size2 = 36;
                } else if (i11 == 100) {
                    size2 = 36;
                } else {
                    if (i11 == 40) {
                        size3 = 36;
                    } else if (i11 == 101) {
                        size3 = 36;
                    } else if (action == 36) {
                        if (infoObject instanceof TLRPC.User) {
                            name2 = UserObject.getFirstName((TLRPC.User) infoObject);
                        } else {
                            name2 = ((TLRPC.Chat) infoObject).title;
                        }
                        infoText = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupUserCanNowSpeakForYou", R.string.VoipGroupUserCanNowSpeakForYou, name2));
                        subInfoText = null;
                        this.timeLeft = 3000L;
                        size = 36;
                        icon = R.raw.voip_unmuted;
                    } else if (action == 32) {
                        if (infoObject instanceof TLRPC.User) {
                            name = UserObject.getFirstName((TLRPC.User) infoObject);
                        } else {
                            name = ((TLRPC.Chat) infoObject).title;
                        }
                        infoText = AndroidUtilities.replaceTags(LocaleController.formatString("VoipGroupRemovedFromGroup", R.string.VoipGroupRemovedFromGroup, name));
                        subInfoText = null;
                        this.timeLeft = 3000L;
                        size = 36;
                        icon = R.raw.voip_group_removed;
                    } else {
                        if (action == 9) {
                            size4 = 36;
                        } else if (action == 10) {
                            size4 = 36;
                        } else if (action == 8) {
                            infoText = LocaleController.formatString("NowInContacts", R.string.NowInContacts, UserObject.getFirstName((TLRPC.User) infoObject));
                            subInfoText = null;
                            icon = R.raw.contact_check;
                            size = 36;
                        } else if (action == 22) {
                            if (DialogObject.isUserDialog(did)) {
                                if (infoObject == null) {
                                    infoText = LocaleController.getString("MainProfilePhotoSetHint", R.string.MainProfilePhotoSetHint);
                                } else {
                                    infoText = LocaleController.getString("MainProfileVideoSetHint", R.string.MainProfileVideoSetHint);
                                }
                            } else {
                                TLRPC.Chat chat5 = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-did));
                                if (ChatObject.isChannel(chat5) && !chat5.megagroup) {
                                    if (infoObject == null) {
                                        infoText = LocaleController.getString("MainChannelProfilePhotoSetHint", R.string.MainChannelProfilePhotoSetHint);
                                    } else {
                                        infoText = LocaleController.getString("MainChannelProfileVideoSetHint", R.string.MainChannelProfileVideoSetHint);
                                    }
                                } else if (infoObject == null) {
                                    infoText = LocaleController.getString("MainGroupProfilePhotoSetHint", R.string.MainGroupProfilePhotoSetHint);
                                } else {
                                    infoText = LocaleController.getString("MainGroupProfileVideoSetHint", R.string.MainGroupProfileVideoSetHint);
                                }
                            }
                            subInfoText = null;
                            icon = R.raw.contact_check;
                            size = 36;
                        } else if (action == 23) {
                            infoText = LocaleController.getString("ChatWasMovedToMainList", R.string.ChatWasMovedToMainList);
                            subInfoText = null;
                            icon = R.raw.contact_check;
                            size = 36;
                        } else if (action == 6) {
                            infoText = LocaleController.getString("ArchiveHidden", R.string.ArchiveHidden);
                            subInfoText = LocaleController.getString("ArchiveHiddenInfo", R.string.ArchiveHiddenInfo);
                            icon = R.raw.chats_swipearchive;
                            size = 48;
                        } else if (i11 == 13) {
                            infoText = LocaleController.getString("QuizWellDone", R.string.QuizWellDone);
                            subInfoText = LocaleController.getString("QuizWellDoneInfo", R.string.QuizWellDoneInfo);
                            icon = R.raw.wallet_congrats;
                            size = 44;
                        } else if (i11 == 14) {
                            infoText = LocaleController.getString("QuizWrongAnswer", R.string.QuizWrongAnswer);
                            subInfoText = LocaleController.getString("QuizWrongAnswerInfo", R.string.QuizWrongAnswerInfo);
                            icon = R.raw.wallet_science;
                            size = 44;
                        } else if (action == 7) {
                            infoText = LocaleController.getString("ArchivePinned", R.string.ArchivePinned);
                            if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                subInfoText = LocaleController.getString("ArchivePinnedInfo", R.string.ArchivePinnedInfo);
                            } else {
                                subInfoText = null;
                            }
                            icon = R.raw.chats_infotip;
                            size = 36;
                        } else if (action == 20 || action == 21) {
                            MessagesController.DialogFilter filter = (MessagesController.DialogFilter) infoObject2;
                            if (did != 0) {
                                long dialogId = did;
                                if (DialogObject.isEncryptedDialog(did)) {
                                    TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)));
                                    dialogId = encryptedChat.user_id;
                                }
                                if (DialogObject.isUserDialog(dialogId)) {
                                    TLRPC.User user7 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialogId));
                                    if (action == 20) {
                                        size5 = 36;
                                        infoText2 = AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserAddedToExisting", R.string.FilterUserAddedToExisting, UserObject.getFirstName(user7), filter.name));
                                    } else {
                                        size5 = 36;
                                        infoText2 = AndroidUtilities.replaceTags(LocaleController.formatString("FilterUserRemovedFrom", R.string.FilterUserRemovedFrom, UserObject.getFirstName(user7), filter.name));
                                    }
                                } else {
                                    size5 = 36;
                                    TLRPC.Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                                    infoText2 = action == 20 ? AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatAddedToExisting", R.string.FilterChatAddedToExisting, chat6.title, filter.name)) : AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatRemovedFrom", R.string.FilterChatRemovedFrom, chat6.title, filter.name));
                                }
                                infoText = infoText2;
                            } else {
                                size5 = 36;
                                if (action == 20) {
                                    infoText = AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsAddedToExisting", R.string.FilterChatsAddedToExisting, LocaleController.formatPluralString("ChatsSelected", ((Integer) infoObject).intValue(), new Object[0]), filter.name));
                                } else {
                                    infoText = AndroidUtilities.replaceTags(LocaleController.formatString("FilterChatsRemovedFrom", R.string.FilterChatsRemovedFrom, LocaleController.formatPluralString("ChatsSelected", ((Integer) infoObject).intValue(), new Object[0]), filter.name));
                                }
                            }
                            subInfoText = null;
                            icon = action == 20 ? R.raw.folder_in : R.raw.folder_out;
                            size = size5;
                        } else if (action == 19) {
                            infoText = this.infoText;
                            subInfoText = null;
                            icon = R.raw.chats_infotip;
                            size = 36;
                        } else if (action == 82) {
                            MediaController.PhotoEntry photo = (MediaController.PhotoEntry) infoObject;
                            if (photo.isVideo) {
                                i4 = R.string.AttachMediaVideoDeselected;
                                str4 = "AttachMediaVideoDeselected";
                            } else {
                                i4 = R.string.AttachMediaPhotoDeselected;
                                str4 = "AttachMediaPhotoDeselected";
                            }
                            infoText = LocaleController.getString(str4, i4);
                            subInfoText = null;
                            icon = 0;
                            size = 36;
                        } else if (action == 78 || action == 79) {
                            int count2 = ((Integer) infoObject).intValue();
                            if (action == 78) {
                                infoText = LocaleController.formatPluralString("PinnedDialogsCount", count2, new Object[0]);
                            } else {
                                infoText = LocaleController.formatPluralString("UnpinnedDialogsCount", count2, new Object[0]);
                            }
                            subInfoText = null;
                            int icon4 = this.currentAction == 78 ? R.raw.ic_pin : R.raw.ic_unpin;
                            if (infoObject2 instanceof Integer) {
                                icon3 = icon4;
                                this.timeLeft = ((Integer) infoObject2).intValue();
                            } else {
                                icon3 = icon4;
                            }
                            size = 36;
                            icon = icon3;
                        } else {
                            if (action == 3) {
                                infoText = LocaleController.getString("ChatArchived", R.string.ChatArchived);
                            } else {
                                infoText = LocaleController.getString("ChatsArchived", R.string.ChatsArchived);
                            }
                            if (MessagesController.getInstance(this.currentAccount).dialogFilters.isEmpty()) {
                                subInfoText = LocaleController.getString("ChatArchivedInfo", R.string.ChatArchivedInfo);
                            } else {
                                subInfoText = null;
                            }
                            icon = R.raw.chats_infotip;
                            size = 36;
                        }
                        TLRPC.User user8 = (TLRPC.User) infoObject;
                        infoText = action == 9 ? AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferChannelToast", R.string.EditAdminTransferChannelToast, UserObject.getFirstName(user8))) : AndroidUtilities.replaceTags(LocaleController.formatString("EditAdminTransferGroupToast", R.string.EditAdminTransferGroupToast, UserObject.getFirstName(user8)));
                        subInfoText = null;
                        icon = R.raw.contact_check;
                        size = size4;
                    }
                    if (i11 == 40) {
                        i3 = R.string.VoipGroupAudioRecordSaved;
                        str3 = "VoipGroupAudioRecordSaved";
                    } else {
                        i3 = R.string.VoipGroupVideoRecordSaved;
                        str3 = "VoipGroupVideoRecordSaved";
                    }
                    String text3 = LocaleController.getString(str3, i3);
                    this.timeLeft = 4000L;
                    this.infoTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    SpannableStringBuilder builder3 = new SpannableStringBuilder(text3);
                    int index13 = text3.indexOf("**");
                    int index23 = text3.lastIndexOf("**");
                    if (index13 < 0 || index23 < 0 || index13 == index23) {
                        icon2 = R.raw.voip_record_saved;
                        subInfoText2 = null;
                    } else {
                        builder3.replace(index23, index23 + 2, (CharSequence) str5);
                        builder3.replace(index13, index13 + 2, (CharSequence) str5);
                        try {
                            try {
                                sb = new StringBuilder();
                                icon2 = R.raw.voip_record_saved;
                                try {
                                    sb.append("tg://openmessage?user_id=");
                                    subInfoText2 = null;
                                } catch (Exception e2) {
                                    e = e2;
                                    subInfoText2 = null;
                                }
                            } catch (Exception e3) {
                                e = e3;
                                icon2 = R.raw.voip_record_saved;
                                subInfoText2 = null;
                            }
                            try {
                                sb.append(UserConfig.getInstance(this.currentAccount).getClientUserId());
                                builder3.setSpan(new URLSpanNoUnderline(sb.toString()), index13, index23 - 2, 33);
                            } catch (Exception e4) {
                                e = e4;
                                FileLog.e(e);
                                infoText = builder3;
                                icon = icon2;
                                subInfoText = subInfoText2;
                                size = size3;
                                this.infoTextView.setText(infoText);
                                if (icon == 0) {
                                }
                                if (subInfoText == null) {
                                }
                                this.undoButton.setVisibility(8);
                                reversedPlay = reversedPlay2;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((Object) this.infoTextView.getText());
                                if (this.subinfoTextView.getVisibility() == 0) {
                                }
                                sb2.append(str5);
                                AndroidUtilities.makeAccessibilityAnnouncement(sb2.toString());
                                if (!isMultilineSubInfo()) {
                                }
                                if (getVisibility() == 0) {
                                }
                            }
                        } catch (Exception e5) {
                            e = e5;
                            icon2 = R.raw.voip_record_saved;
                            subInfoText2 = null;
                        }
                    }
                    infoText = builder3;
                    icon = icon2;
                    subInfoText = subInfoText2;
                    size = size3;
                }
                if (i11 == 39) {
                    i2 = R.string.VoipGroupAudioRecordStarted;
                    str2 = "VoipGroupAudioRecordStarted";
                } else {
                    i2 = R.string.VoipGroupVideoRecordStarted;
                    str2 = "VoipGroupVideoRecordStarted";
                }
                infoText = AndroidUtilities.replaceTags(LocaleController.getString(str2, i2));
                subInfoText = null;
                icon = R.raw.voip_record_start;
                this.timeLeft = 3000L;
                size = size2;
            } else {
                if (ChatObject.isChannelOrGiga((TLRPC.Chat) infoObject)) {
                    infoText = AndroidUtilities.replaceTags(LocaleController.getString("VoipChannelSoundUnmuted", R.string.VoipChannelSoundUnmuted));
                } else {
                    infoText = AndroidUtilities.replaceTags(LocaleController.getString("VoipGroupSoundUnmuted", R.string.VoipGroupSoundUnmuted));
                }
                subInfoText = null;
                this.timeLeft = 3000L;
                size = 36;
                icon = R.raw.ic_unmute;
            }
            this.infoTextView.setText(infoText);
            if (icon == 0) {
                if (0 != 0) {
                    this.leftImageView.setImageResource(icon);
                    reversedPlay2 = false;
                } else {
                    this.leftImageView.setAnimation(icon, size, size);
                    RLottieDrawable drawable = this.leftImageView.getAnimatedDrawable();
                    reversedPlay2 = false;
                    drawable.setPlayInDirectionOfCustomEndFrame(false);
                    drawable.setCustomEndFrame(0 != 0 ? 0 : drawable.getFramesCount());
                }
                this.leftImageView.setVisibility(0);
                if (0 == 0) {
                    this.leftImageView.setProgress(reversedPlay2 ? 1.0f : 0.0f);
                    this.leftImageView.playAnimation();
                }
            } else {
                reversedPlay2 = false;
                this.leftImageView.setVisibility(8);
            }
            if (subInfoText == null) {
                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                layoutParams.topMargin = AndroidUtilities.dp(6.0f);
                layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                layoutParams = (FrameLayout.LayoutParams) this.subinfoTextView.getLayoutParams();
                layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                this.subinfoTextView.setText(subInfoText);
                this.subinfoTextView.setVisibility(0);
                this.infoTextView.setTextSize(1, 14.0f);
                this.infoTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            } else {
                layoutParams.leftMargin = AndroidUtilities.dp(58.0f);
                layoutParams.topMargin = AndroidUtilities.dp(13.0f);
                layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
                this.subinfoTextView.setVisibility(8);
                this.infoTextView.setTextSize(1, 15.0f);
                this.infoTextView.setTypeface(Typeface.DEFAULT);
            }
            this.undoButton.setVisibility(8);
            reversedPlay = reversedPlay2;
        }
        StringBuilder sb22 = new StringBuilder();
        sb22.append((Object) this.infoTextView.getText());
        if (this.subinfoTextView.getVisibility() == 0) {
            str5 = ". " + ((Object) this.subinfoTextView.getText());
        }
        sb22.append(str5);
        AndroidUtilities.makeAccessibilityAnnouncement(sb22.toString());
        if (!isMultilineSubInfo()) {
            int width = ((ViewGroup) getParent()).getMeasuredWidth();
            if (width == 0) {
                width = AndroidUtilities.displaySize.x;
            }
            measureChildWithMargins(this.subinfoTextView, View.MeasureSpec.makeMeasureSpec(width - AndroidUtilities.dp(16.0f), C.BUFFER_FLAG_ENCRYPTED), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
            this.undoViewHeight = this.subinfoTextView.getMeasuredHeight() + AndroidUtilities.dp(37.0f);
        } else if (hasSubInfo()) {
            this.undoViewHeight = AndroidUtilities.dp(52.0f);
        } else if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            int width2 = (parent.getMeasuredWidth() - parent.getPaddingLeft()) - parent.getPaddingRight();
            if (width2 <= 0) {
                width2 = AndroidUtilities.displaySize.x;
            }
            measureChildWithMargins(this.infoTextView, View.MeasureSpec.makeMeasureSpec(width2 - AndroidUtilities.dp(16.0f), C.BUFFER_FLAG_ENCRYPTED), 0, View.MeasureSpec.makeMeasureSpec(0, 0), 0);
            int measuredHeight = this.infoTextView.getMeasuredHeight();
            int i12 = this.currentAction;
            int dp2 = measuredHeight + AndroidUtilities.dp((i12 == 16 || i12 == 17 || i12 == 18) ? 14.0f : 28.0f);
            this.undoViewHeight = dp2;
            int i13 = this.currentAction;
            if (i13 == 18) {
                this.undoViewHeight = Math.max(dp2, AndroidUtilities.dp(52.0f));
            } else if (i13 == 25) {
                this.undoViewHeight = Math.max(dp2, AndroidUtilities.dp(50.0f));
            } else if (infoOnly) {
                this.undoViewHeight = dp2 - AndroidUtilities.dp(8.0f);
            }
        }
        if (getVisibility() == 0) {
            setVisibility(0);
            setEnterOffset((this.fromTop ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight));
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[1];
            float[] fArr = new float[2];
            boolean z = this.fromTop;
            fArr[0] = (z ? -1.0f : 1.0f) * (this.enterOffsetMargin + this.undoViewHeight);
            fArr[1] = z ? 1.0f : -1.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(this, "enterOffset", fArr);
            animatorSet.playTogether(animatorArr);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.setDuration(180L);
            animatorSet.start();
        }
    }

    /* renamed from: lambda$showWithAction$2$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3187lambda$showWithAction$2$orgtelegramuiComponentsUndoView(View view) {
        hide(false, 1);
    }

    public static /* synthetic */ boolean lambda$showWithAction$3(View v, MotionEvent event) {
        return true;
    }

    /* renamed from: lambda$showWithAction$6$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3190lambda$showWithAction$6$orgtelegramuiComponentsUndoView(TLRPC.Message message, View v) {
        hide(true, 1);
        TLRPC.TL_payments_getPaymentReceipt req = new TLRPC.TL_payments_getPaymentReceipt();
        req.msg_id = message.id;
        req.peer = this.parentFragment.getMessagesController().getInputPeer(message.peer_id);
        this.parentFragment.getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                UndoView.this.m3189lambda$showWithAction$5$orgtelegramuiComponentsUndoView(tLObject, tL_error);
            }
        }, 2);
    }

    /* renamed from: lambda$showWithAction$5$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3189lambda$showWithAction$5$orgtelegramuiComponentsUndoView(final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.UndoView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                UndoView.this.m3188lambda$showWithAction$4$orgtelegramuiComponentsUndoView(response);
            }
        });
    }

    /* renamed from: lambda$showWithAction$4$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3188lambda$showWithAction$4$orgtelegramuiComponentsUndoView(TLObject response) {
        if (response instanceof TLRPC.TL_payments_paymentReceipt) {
            this.parentFragment.presentFragment(new PaymentFormActivity((TLRPC.TL_payments_paymentReceipt) response));
        }
    }

    /* renamed from: lambda$showWithAction$7$org-telegram-ui-Components-UndoView */
    public /* synthetic */ void m3191lambda$showWithAction$7$orgtelegramuiComponentsUndoView() {
        this.leftImageView.performHapticFeedback(3, 2);
    }

    public void setEnterOffsetMargin(int enterOffsetMargin) {
        this.enterOffsetMargin = enterOffsetMargin;
    }

    protected boolean canUndo() {
        return true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(this.undoViewHeight, C.BUFFER_FLAG_ENCRYPTED));
        this.backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.additionalTranslationY != 0.0f) {
            canvas.save();
            float bottom = (getMeasuredHeight() - this.enterOffset) + AndroidUtilities.dp(9.0f);
            if (bottom > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), bottom);
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
            float bottom = (getMeasuredHeight() - this.enterOffset) + this.enterOffsetMargin + AndroidUtilities.dp(1.0f);
            if (bottom > 0.0f) {
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), bottom);
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
            int newSeconds = j > 0 ? (int) Math.ceil(((float) j) / 1000.0f) : 0;
            if (this.prevSeconds != newSeconds) {
                this.prevSeconds = newSeconds;
                String format = String.format("%d", Integer.valueOf(Math.max(1, newSeconds)));
                this.timeLeftString = format;
                StaticLayout staticLayout = this.timeLayout;
                if (staticLayout != null) {
                    this.timeLayoutOut = staticLayout;
                    this.timeReplaceProgress = 0.0f;
                    this.textWidthOut = this.textWidth;
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
        long newTime = SystemClock.elapsedRealtime();
        long dt = newTime - this.lastUpdateTime;
        long j2 = this.timeLeft - dt;
        this.timeLeft = j2;
        this.lastUpdateTime = newTime;
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

    public void setInfoText(CharSequence text) {
        this.infoText = text;
    }

    public void setHideAnimationType(int hideAnimationType) {
        this.hideAnimationType = hideAnimationType;
    }

    public float getEnterOffset() {
        return this.enterOffset;
    }

    public void setEnterOffset(float enterOffset) {
        if (this.enterOffset != enterOffset) {
            this.enterOffset = enterOffset;
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

    private int getThemedColor(String key) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(key) : null;
        return color != null ? color.intValue() : Theme.getColor(key);
    }
}
