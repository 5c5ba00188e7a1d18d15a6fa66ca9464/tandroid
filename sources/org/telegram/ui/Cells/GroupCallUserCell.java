package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BlobDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
/* loaded from: classes3.dex */
public class GroupCallUserCell extends FrameLayout {
    private AccountInstance accountInstance;
    private AnimatorSet animatorSet;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private RadialProgressView avatarProgressView;
    private AvatarWavesDrawable avatarWavesDrawable;
    private Runnable checkRaiseRunnable;
    private ChatObject.Call currentCall;
    private TLRPC$Chat currentChat;
    private boolean currentIconGray;
    private int currentStatus;
    private TLRPC$User currentUser;
    private Paint dividerPaint;
    private SimpleTextView fullAboutTextView;
    private String grayIconColor;
    private boolean hasAvatar;
    private boolean isSpeaking;
    private int lastMuteColor;
    private boolean lastMuted;
    private boolean lastRaisedHand;
    private RLottieImageView muteButton;
    private RLottieDrawable muteDrawable;
    private SimpleTextView nameTextView;
    private boolean needDivider;
    private TLRPC$TL_groupCallParticipant participant;
    private Drawable premiumDrawable;
    private float progressToAvatarPreview;
    private Runnable raiseHandCallback;
    public AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable rightDrawable;
    private long selfId;
    private Runnable shakeHandCallback;
    private RLottieDrawable shakeHandDrawable;
    private Drawable speakingDrawable;
    private SimpleTextView[] statusTextView;
    private Runnable updateRunnable;
    private boolean updateRunnableScheduled;
    private Runnable updateVoiceRunnable;
    private boolean updateVoiceRunnableScheduled;
    private Drawable verifiedDrawable;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onMuteClick */
    public void lambda$new$5(GroupCallUserCell groupCallUserCell) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.shakeHandDrawable.setOnFinishCallback(null, 0);
        this.muteDrawable.setOnFinishCallback(null, 0);
        this.muteButton.setAnimation(this.muteDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        int nextInt = Utilities.random.nextInt(100);
        int i = 540;
        int i2 = 420;
        if (nextInt < 32) {
            i = 120;
            i2 = 0;
        } else if (nextInt < 64) {
            i = 240;
            i2 = 120;
        } else if (nextInt < 97) {
            i = 420;
            i2 = 240;
        } else if (nextInt != 98) {
            i = 720;
            i2 = 540;
        }
        this.shakeHandDrawable.setCustomEndFrame(i);
        this.shakeHandDrawable.setOnFinishCallback(this.shakeHandCallback, i - 1);
        this.muteButton.setAnimation(this.shakeHandDrawable);
        this.shakeHandDrawable.setCurrentFrame(i2);
        this.muteButton.playAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        applyParticipantChanges(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        this.isSpeaking = false;
        applyParticipantChanges(true, true);
        this.avatarWavesDrawable.setAmplitude(0.0d);
        this.updateRunnableScheduled = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        applyParticipantChanges(true, true);
        this.updateVoiceRunnableScheduled = false;
    }

    public void setProgressToAvatarPreview(float f) {
        this.progressToAvatarPreview = f;
        this.nameTextView.setTranslationX((LocaleController.isRTL ? AndroidUtilities.dp(53.0f) : -AndroidUtilities.dp(53.0f)) * f);
        boolean z = true;
        if (isSelfUser() && f > 0.0f) {
            float f2 = 1.0f - f;
            this.fullAboutTextView.setTranslationX((LocaleController.isRTL ? -AndroidUtilities.dp(53.0f) : AndroidUtilities.dp(53.0f)) * f2);
            this.fullAboutTextView.setVisibility(0);
            this.fullAboutTextView.setAlpha(f);
            this.statusTextView[4].setAlpha(f2);
            SimpleTextView simpleTextView = this.statusTextView[4];
            boolean z2 = LocaleController.isRTL;
            int dp = AndroidUtilities.dp(53.0f);
            if (!z2) {
                dp = -dp;
            }
            simpleTextView.setTranslationX(dp * f);
        } else {
            this.fullAboutTextView.setVisibility(8);
            int i = 0;
            while (true) {
                SimpleTextView[] simpleTextViewArr = this.statusTextView;
                if (i >= simpleTextViewArr.length) {
                    break;
                }
                if (!TextUtils.isEmpty(simpleTextViewArr[4].getText()) && this.statusTextView[4].getLineCount() > 1) {
                    this.statusTextView[i].setFullLayoutAdditionalWidth(AndroidUtilities.dp(92.0f), LocaleController.isRTL ? AndroidUtilities.dp(48.0f) : AndroidUtilities.dp(53.0f));
                    this.statusTextView[i].setFullAlpha(f);
                    this.statusTextView[i].setTranslationX(0.0f);
                    this.statusTextView[i].invalidate();
                } else {
                    this.statusTextView[i].setTranslationX((LocaleController.isRTL ? AndroidUtilities.dp(53.0f) : -AndroidUtilities.dp(53.0f)) * f);
                    this.statusTextView[i].setFullLayoutAdditionalWidth(0, 0);
                }
                i++;
            }
        }
        this.avatarImageView.setAlpha(f == 0.0f ? 1.0f : 0.0f);
        this.avatarWavesDrawable.setShowWaves((this.isSpeaking && f == 0.0f) ? false : false, this);
        float f3 = 1.0f - f;
        this.muteButton.setAlpha(f3);
        float f4 = (f3 * 0.4f) + 0.6f;
        this.muteButton.setScaleX(f4);
        this.muteButton.setScaleY(f4);
        invalidate();
    }

    public AvatarWavesDrawable getAvatarWavesDrawable() {
        return this.avatarWavesDrawable;
    }

    public void setUploadProgress(float f, boolean z) {
        this.avatarProgressView.setProgress(f);
        if (f < 1.0f) {
            AndroidUtilities.updateViewVisibilityAnimated(this.avatarProgressView, true, 1.0f, z);
        } else {
            AndroidUtilities.updateViewVisibilityAnimated(this.avatarProgressView, false, 1.0f, z);
        }
    }

    public void setDrawAvatar(boolean z) {
        if (this.avatarImageView.getImageReceiver().getVisible() != z) {
            this.avatarImageView.getImageReceiver().setVisible(z, true);
        }
    }

    /* loaded from: classes3.dex */
    private static class VerifiedDrawable extends Drawable {
        private Drawable[] drawables;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public VerifiedDrawable(Context context) {
            Drawable[] drawableArr = new Drawable[2];
            this.drawables = drawableArr;
            drawableArr[0] = context.getResources().getDrawable(R.drawable.verified_area).mutate();
            this.drawables[0].setColorFilter(new PorterDuffColorFilter(-9063442, PorterDuff.Mode.MULTIPLY));
            this.drawables[1] = context.getResources().getDrawable(R.drawable.verified_check).mutate();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.drawables[0].getIntrinsicWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.drawables[0].getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            int i = 0;
            while (true) {
                Drawable[] drawableArr = this.drawables;
                if (i >= drawableArr.length) {
                    return;
                }
                drawableArr[i].setBounds(getBounds());
                this.drawables[i].draw(canvas);
                i++;
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            int i2 = 0;
            while (true) {
                Drawable[] drawableArr = this.drawables;
                if (i2 >= drawableArr.length) {
                    return;
                }
                drawableArr[i2].setAlpha(i);
                i2++;
            }
        }
    }

    public GroupCallUserCell(Context context) {
        super(context);
        this.statusTextView = new SimpleTextView[5];
        this.shakeHandCallback = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$0();
            }
        };
        this.raiseHandCallback = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$1();
            }
        };
        this.grayIconColor = "voipgroup_mutedIcon";
        this.checkRaiseRunnable = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$2();
            }
        };
        this.updateRunnable = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$3();
            }
        };
        this.updateVoiceRunnable = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$4();
            }
        };
        Paint paint = new Paint();
        this.dividerPaint = paint;
        paint.setColor(Theme.getColor("voipgroup_actionBar"));
        this.avatarDrawable = new AvatarDrawable();
        setClipChildren(false);
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(24.0f));
        BackupImageView backupImageView2 = this.avatarImageView;
        boolean z = LocaleController.isRTL;
        addView(backupImageView2, LayoutHelper.createFrame(46, 46.0f, (z ? 5 : 3) | 48, z ? 0.0f : 11.0f, 6.0f, z ? 11.0f : 0.0f, 0.0f));
        RadialProgressView radialProgressView = new RadialProgressView(context) { // from class: org.telegram.ui.Cells.GroupCallUserCell.1
            private Paint paint;

            {
                Paint paint2 = new Paint(1);
                this.paint = paint2;
                paint2.setColor(1426063360);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RadialProgressView, android.view.View
            public void onDraw(Canvas canvas) {
                if (GroupCallUserCell.this.avatarImageView.getImageReceiver().hasNotThumb() && GroupCallUserCell.this.avatarImageView.getAlpha() > 0.0f) {
                    this.paint.setAlpha((int) (GroupCallUserCell.this.avatarImageView.getImageReceiver().getCurrentAlpha() * 85.0f * GroupCallUserCell.this.avatarImageView.getAlpha()));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.paint);
                }
                GroupCallUserCell.this.avatarProgressView.setProgressColor(ColorUtils.setAlphaComponent(-1, (int) (GroupCallUserCell.this.avatarImageView.getImageReceiver().getCurrentAlpha() * 255.0f * GroupCallUserCell.this.avatarImageView.getAlpha())));
                super.onDraw(canvas);
            }
        };
        this.avatarProgressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(26.0f));
        this.avatarProgressView.setProgressColor(-1);
        this.avatarProgressView.setNoProgress(false);
        RadialProgressView radialProgressView2 = this.avatarProgressView;
        boolean z2 = LocaleController.isRTL;
        addView(radialProgressView2, LayoutHelper.createFrame(46, 46.0f, (z2 ? 5 : 3) | 48, z2 ? 0.0f : 11.0f, 6.0f, z2 ? 11.0f : 0.0f, 0.0f));
        AndroidUtilities.updateViewVisibilityAnimated(this.avatarProgressView, false, 1.0f, false);
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.nameTextView = simpleTextView;
        simpleTextView.setTextColor(Theme.getColor("voipgroup_nameText"));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.nameTextView.setTextSize(16);
        this.nameTextView.setDrawablePadding(AndroidUtilities.dp(6.0f));
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        SimpleTextView simpleTextView2 = this.nameTextView;
        boolean z3 = LocaleController.isRTL;
        addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? 54.0f : 67.0f, 10.0f, z3 ? 67.0f : 54.0f, 0.0f));
        this.rightDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.nameTextView, AndroidUtilities.dp(20.0f), 9);
        Drawable drawable = context.getResources().getDrawable(R.drawable.voice_volume_mini);
        this.speakingDrawable = drawable;
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("voipgroup_speakingText"), PorterDuff.Mode.MULTIPLY));
        final int i = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr = this.statusTextView;
            if (i >= simpleTextViewArr.length) {
                break;
            }
            simpleTextViewArr[i] = new SimpleTextView(context) { // from class: org.telegram.ui.Cells.GroupCallUserCell.2
                float originalAlpha;

                @Override // android.view.View
                public void setAlpha(float f) {
                    this.originalAlpha = f;
                    if (i == 4) {
                        float fullAlpha = GroupCallUserCell.this.statusTextView[4].getFullAlpha();
                        if (GroupCallUserCell.this.isSelfUser() && GroupCallUserCell.this.progressToAvatarPreview > 0.0f) {
                            super.setAlpha(1.0f - GroupCallUserCell.this.progressToAvatarPreview);
                            return;
                        } else if (fullAlpha > 0.0f) {
                            super.setAlpha(Math.max(f, fullAlpha));
                            return;
                        } else {
                            super.setAlpha(f);
                            return;
                        }
                    }
                    super.setAlpha(f * (1.0f - GroupCallUserCell.this.statusTextView[4].getFullAlpha()));
                }

                @Override // android.view.View
                public void setTranslationY(float f) {
                    if (i == 4 && getFullAlpha() > 0.0f) {
                        f = 0.0f;
                    }
                    super.setTranslationY(f);
                }

                @Override // android.view.View
                public float getAlpha() {
                    return this.originalAlpha;
                }

                @Override // org.telegram.ui.ActionBar.SimpleTextView
                public void setFullAlpha(float f) {
                    super.setFullAlpha(f);
                    for (int i2 = 0; i2 < GroupCallUserCell.this.statusTextView.length; i2++) {
                        GroupCallUserCell.this.statusTextView[i2].setAlpha(GroupCallUserCell.this.statusTextView[i2].getAlpha());
                    }
                }
            };
            this.statusTextView[i].setTextSize(15);
            this.statusTextView[i].setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            if (i == 4) {
                this.statusTextView[i].setBuildFullLayout(true);
                this.statusTextView[i].setTextColor(Theme.getColor("voipgroup_mutedIcon"));
                SimpleTextView simpleTextView3 = this.statusTextView[i];
                boolean z4 = LocaleController.isRTL;
                addView(simpleTextView3, LayoutHelper.createFrame(-1, -2.0f, (z4 ? 5 : 3) | 48, z4 ? 54.0f : 67.0f, 32.0f, z4 ? 67.0f : 54.0f, 0.0f));
            } else {
                if (i == 0) {
                    this.statusTextView[i].setTextColor(Theme.getColor("voipgroup_listeningText"));
                    this.statusTextView[i].setText(LocaleController.getString("Listening", R.string.Listening));
                } else if (i == 1) {
                    this.statusTextView[i].setTextColor(Theme.getColor("voipgroup_speakingText"));
                    this.statusTextView[i].setText(LocaleController.getString("Speaking", R.string.Speaking));
                    this.statusTextView[i].setDrawablePadding(AndroidUtilities.dp(2.0f));
                } else if (i == 2) {
                    this.statusTextView[i].setTextColor(Theme.getColor("voipgroup_mutedByAdminIcon"));
                    this.statusTextView[i].setText(LocaleController.getString("VoipGroupMutedForMe", R.string.VoipGroupMutedForMe));
                } else if (i == 3) {
                    this.statusTextView[i].setTextColor(Theme.getColor("voipgroup_listeningText"));
                    this.statusTextView[i].setText(LocaleController.getString("WantsToSpeak", R.string.WantsToSpeak));
                }
                SimpleTextView simpleTextView4 = this.statusTextView[i];
                boolean z5 = LocaleController.isRTL;
                addView(simpleTextView4, LayoutHelper.createFrame(-1, 20.0f, (z5 ? 5 : 3) | 48, z5 ? 54.0f : 67.0f, 32.0f, z5 ? 67.0f : 54.0f, 0.0f));
            }
            i++;
        }
        SimpleTextView simpleTextView5 = new SimpleTextView(context);
        this.fullAboutTextView = simpleTextView5;
        simpleTextView5.setMaxLines(3);
        this.fullAboutTextView.setTextSize(15);
        this.fullAboutTextView.setTextColor(Theme.getColor("voipgroup_mutedIcon"));
        this.fullAboutTextView.setVisibility(8);
        addView(this.fullAboutTextView, LayoutHelper.createFrame(-1, 60.0f, (LocaleController.isRTL ? 5 : 3) | 48, 14.0f, 32.0f, 14.0f, 0.0f));
        int i2 = R.raw.voice_outlined2;
        this.muteDrawable = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(32.0f), true, null);
        int i3 = R.raw.hand_1;
        this.shakeHandDrawable = new RLottieDrawable(i3, "" + i3, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(32.0f), true, null);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteButton = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.muteButton.setAnimation(this.muteDrawable);
        if (Build.VERSION.SDK_INT >= 21) {
            RippleDrawable rippleDrawable = (RippleDrawable) Theme.createSelectorDrawable(Theme.getColor(this.grayIconColor) & 620756991);
            Theme.setRippleDrawableForceSoftware(rippleDrawable);
            this.muteButton.setBackground(rippleDrawable);
        }
        this.muteButton.setImportantForAccessibility(2);
        addView(this.muteButton, LayoutHelper.createFrame(48, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 6.0f, 0.0f, 6.0f, 0.0f));
        this.muteButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallUserCell.this.lambda$new$5(view);
            }
        });
        this.avatarWavesDrawable = new AvatarWavesDrawable(AndroidUtilities.dp(26.0f), AndroidUtilities.dp(29.0f));
        setWillNotDraw(false);
        setFocusable(true);
    }

    public int getClipHeight() {
        SimpleTextView simpleTextView;
        if (!TextUtils.isEmpty(this.fullAboutTextView.getText()) && this.hasAvatar) {
            simpleTextView = this.fullAboutTextView;
        } else {
            simpleTextView = this.statusTextView[4];
        }
        if (simpleTextView.getLineCount() > 1) {
            return simpleTextView.getTop() + simpleTextView.getTextHeight() + AndroidUtilities.dp(8.0f);
        }
        return getMeasuredHeight();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.updateRunnableScheduled) {
            AndroidUtilities.cancelRunOnUIThread(this.updateRunnable);
            this.updateRunnableScheduled = false;
        }
        if (this.updateVoiceRunnableScheduled) {
            AndroidUtilities.cancelRunOnUIThread(this.updateVoiceRunnable);
            this.updateVoiceRunnableScheduled = false;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public boolean isSelfUser() {
        long j = this.selfId;
        if (j > 0) {
            TLRPC$User tLRPC$User = this.currentUser;
            return tLRPC$User != null && tLRPC$User.id == j;
        }
        TLRPC$Chat tLRPC$Chat = this.currentChat;
        return tLRPC$Chat != null && tLRPC$Chat.id == (-j);
    }

    public boolean isHandRaised() {
        return this.lastRaisedHand;
    }

    public CharSequence getName() {
        return this.nameTextView.getText();
    }

    public boolean hasAvatarSet() {
        return this.avatarImageView.getImageReceiver().hasNotThumb();
    }

    public void setData(AccountInstance accountInstance, TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant, ChatObject.Call call, long j, TLRPC$FileLocation tLRPC$FileLocation, boolean z) {
        this.currentCall = call;
        this.accountInstance = accountInstance;
        this.selfId = j;
        this.participant = tLRPC$TL_groupCallParticipant;
        long peerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
        if (peerId > 0) {
            TLRPC$User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId));
            this.currentUser = user;
            this.currentChat = null;
            this.avatarDrawable.setInfo(user);
            this.nameTextView.setText(UserObject.getUserName(this.currentUser));
            TLRPC$User tLRPC$User = this.currentUser;
            if (tLRPC$User != null && tLRPC$User.verified) {
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.rightDrawable;
                Drawable drawable = this.verifiedDrawable;
                if (drawable == null) {
                    drawable = new VerifiedDrawable(getContext());
                }
                this.verifiedDrawable = drawable;
                swapAnimatedEmojiDrawable.set(drawable, z);
            } else {
                if (tLRPC$User != null) {
                    TLRPC$EmojiStatus tLRPC$EmojiStatus = tLRPC$User.emoji_status;
                    if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatus) {
                        this.rightDrawable.set(((TLRPC$TL_emojiStatus) tLRPC$EmojiStatus).document_id, z);
                    }
                }
                if (tLRPC$User != null) {
                    TLRPC$EmojiStatus tLRPC$EmojiStatus2 = tLRPC$User.emoji_status;
                    if ((tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus2).until > ((int) (System.currentTimeMillis() / 1000))) {
                        this.rightDrawable.set(((TLRPC$TL_emojiStatusUntil) this.currentUser.emoji_status).document_id, z);
                    }
                }
                TLRPC$User tLRPC$User2 = this.currentUser;
                if (tLRPC$User2 != null && tLRPC$User2.premium) {
                    if (this.premiumDrawable == null) {
                        this.premiumDrawable = getContext().getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                        this.premiumDrawable = new AnimatedEmojiDrawable.WrapSizeDrawable(this, this.premiumDrawable, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f)) { // from class: org.telegram.ui.Cells.GroupCallUserCell.3
                            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.WrapSizeDrawable, android.graphics.drawable.Drawable
                            public void draw(Canvas canvas) {
                                canvas.save();
                                canvas.translate(AndroidUtilities.dp(-2.0f), AndroidUtilities.dp(0.0f));
                                super.draw(canvas);
                                canvas.restore();
                            }
                        };
                    }
                    this.rightDrawable.set(this.premiumDrawable, z);
                } else {
                    this.rightDrawable.set((Drawable) null, z);
                }
            }
            this.rightDrawable.setColor(Integer.valueOf(Theme.getColor("premiumGradient1")));
            this.nameTextView.setRightDrawable(this.rightDrawable);
            this.avatarImageView.getImageReceiver().setCurrentAccount(accountInstance.getCurrentAccount());
            if (tLRPC$FileLocation != null) {
                this.hasAvatar = true;
                this.avatarImageView.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
            } else {
                ImageLocation forUser = ImageLocation.getForUser(this.currentUser, 1);
                this.hasAvatar = forUser != null;
                this.avatarImageView.setImage(forUser, "50_50", this.avatarDrawable, this.currentUser);
            }
        } else {
            TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId));
            this.currentChat = chat;
            this.currentUser = null;
            this.avatarDrawable.setInfo(chat);
            TLRPC$Chat tLRPC$Chat = this.currentChat;
            if (tLRPC$Chat != null) {
                this.nameTextView.setText(tLRPC$Chat.title);
                if (this.currentChat.verified) {
                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.rightDrawable;
                    Drawable drawable2 = this.verifiedDrawable;
                    if (drawable2 == null) {
                        drawable2 = new VerifiedDrawable(getContext());
                    }
                    this.verifiedDrawable = drawable2;
                    swapAnimatedEmojiDrawable2.set(drawable2, z);
                } else {
                    this.rightDrawable.set((Drawable) null, z);
                }
                this.avatarImageView.getImageReceiver().setCurrentAccount(accountInstance.getCurrentAccount());
                if (tLRPC$FileLocation != null) {
                    this.hasAvatar = true;
                    this.avatarImageView.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
                } else {
                    ImageLocation forChat = ImageLocation.getForChat(this.currentChat, 1);
                    this.hasAvatar = forChat != null;
                    this.avatarImageView.setImage(forChat, "50_50", this.avatarDrawable, this.currentChat);
                }
            }
        }
        applyParticipantChanges(z);
    }

    public void setDrawDivider(boolean z) {
        this.needDivider = z;
        invalidate();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        applyParticipantChanges(false);
    }

    public TLRPC$TL_groupCallParticipant getParticipant() {
        return this.participant;
    }

    public void setAmplitude(double d) {
        if (d > 1.5d) {
            if (this.updateRunnableScheduled) {
                AndroidUtilities.cancelRunOnUIThread(this.updateRunnable);
            }
            if (!this.isSpeaking) {
                this.isSpeaking = true;
                applyParticipantChanges(true);
            }
            this.avatarWavesDrawable.setAmplitude(d);
            AndroidUtilities.runOnUIThread(this.updateRunnable, 500L);
            this.updateRunnableScheduled = true;
            return;
        }
        this.avatarWavesDrawable.setAmplitude(0.0d);
    }

    public boolean clickMuteButton() {
        if (this.muteButton.isEnabled()) {
            this.muteButton.callOnClick();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f), 1073741824));
    }

    public void applyParticipantChanges(boolean z) {
        applyParticipantChanges(z, false);
    }

    public void setGrayIconColor(String str, int i) {
        if (!this.grayIconColor.equals(str)) {
            if (this.currentIconGray) {
                this.lastMuteColor = Theme.getColor(str);
            }
            this.grayIconColor = str;
        }
        if (this.currentIconGray) {
            this.muteButton.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), i & 620756991, true);
        }
    }

    public void setAboutVisibleProgress(int i, float f) {
        if (TextUtils.isEmpty(this.statusTextView[4].getText())) {
            f = 0.0f;
        }
        this.statusTextView[4].setFullAlpha(f);
        this.statusTextView[4].setFullLayoutAdditionalWidth(0, 0);
        invalidate();
    }

    public void setAboutVisible(boolean z) {
        if (z) {
            this.statusTextView[4].setTranslationY(0.0f);
        } else {
            this.statusTextView[4].setFullAlpha(0.0f);
        }
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0291  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x029f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x02aa  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x02b2  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x02ee  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0341  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0410  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0452  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0482  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0499  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyParticipantChanges(boolean z, boolean z2) {
        boolean z3;
        String str;
        final int color;
        final int i;
        boolean z4;
        AnimatorSet animatorSet;
        ArrayList arrayList;
        float dp;
        boolean customEndFrame;
        int i2;
        if (this.currentCall == null) {
            return;
        }
        boolean z5 = false;
        this.muteButton.setEnabled((isSelfUser() && this.participant.raise_hand_rating == 0) ? false : true);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = this.participant;
        if (elapsedRealtime - tLRPC$TL_groupCallParticipant.lastVoiceUpdateTime < 500) {
            z3 = tLRPC$TL_groupCallParticipant.hasVoiceDelayed;
        } else {
            z3 = tLRPC$TL_groupCallParticipant.hasVoice;
        }
        if (!z2) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.participant.lastSpeakTime;
            boolean z6 = uptimeMillis < 500;
            if (!this.isSpeaking || !z6 || z3) {
                this.isSpeaking = z6;
                if (this.updateRunnableScheduled) {
                    AndroidUtilities.cancelRunOnUIThread(this.updateRunnable);
                    this.updateRunnableScheduled = false;
                }
                if (this.isSpeaking) {
                    AndroidUtilities.runOnUIThread(this.updateRunnable, 500 - uptimeMillis);
                    this.updateRunnableScheduled = true;
                }
            }
        }
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant2 = this.currentCall.participants.get(MessageObject.getPeerId(this.participant.peer));
        if (tLRPC$TL_groupCallParticipant2 != null) {
            this.participant = tLRPC$TL_groupCallParticipant2;
        }
        boolean z7 = this.participant.muted_by_you && !isSelfUser();
        boolean z8 = !isSelfUser() ? (!this.participant.muted || (this.isSpeaking && z3)) && !z7 : VoIPService.getSharedInstance() == null || !VoIPService.getSharedInstance().isMicMute() || (this.isSpeaking && z3);
        if (z8) {
            boolean z9 = this.participant.can_self_unmute;
        }
        boolean z10 = !TextUtils.isEmpty(this.participant.about);
        this.currentIconGray = false;
        AndroidUtilities.cancelRunOnUIThread(this.checkRaiseRunnable);
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant3 = this.participant;
        if ((tLRPC$TL_groupCallParticipant3.muted && !this.isSpeaking) || z7) {
            boolean z11 = tLRPC$TL_groupCallParticipant3.can_self_unmute;
            if (!z11 || z7) {
                boolean z12 = (z11 || tLRPC$TL_groupCallParticipant3.raise_hand_rating == 0) ? false : true;
                if (z12) {
                    int color2 = Theme.getColor("voipgroup_listeningText");
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    str = "voipgroup_listeningText";
                    long j = this.participant.lastRaiseHandDate;
                    long j2 = elapsedRealtime2 - j;
                    if (j == 0 || j2 > 5000) {
                        i2 = z7 ? 2 : z10 ? 4 : 0;
                    } else {
                        AndroidUtilities.runOnUIThread(this.checkRaiseRunnable, 5000 - j2);
                        i2 = 3;
                    }
                    i = i2;
                    z4 = z12;
                    color = color2;
                } else {
                    str = "voipgroup_listeningText";
                    int color3 = Theme.getColor("voipgroup_mutedByAdminIcon");
                    int i3 = z7 ? 2 : z10 ? 4 : 0;
                    z4 = z12;
                    color = color3;
                    i = i3;
                }
                if (!isSelfUser()) {
                }
                if (!isSelfUser()) {
                }
                animatorSet = this.animatorSet;
                if (animatorSet == null) {
                }
                if (z) {
                }
                animatorSet.cancel();
                this.animatorSet = null;
                if (z) {
                }
                if (!z) {
                }
            } else {
                color = Theme.getColor(this.grayIconColor);
                this.currentIconGray = true;
                i = z10 ? 4 : 0;
                str = "voipgroup_listeningText";
                z4 = false;
                if (!isSelfUser()) {
                }
                if (!isSelfUser()) {
                }
                animatorSet = this.animatorSet;
                if (animatorSet == null) {
                }
                if (z) {
                }
                animatorSet.cancel();
                this.animatorSet = null;
                if (z) {
                }
                if (!z) {
                }
            }
        } else {
            str = "voipgroup_listeningText";
            if (this.isSpeaking && z3) {
                color = Theme.getColor("voipgroup_speakingText");
                z4 = false;
                i = 1;
                if (!isSelfUser()) {
                }
                if (!isSelfUser()) {
                }
                animatorSet = this.animatorSet;
                if (animatorSet == null) {
                }
                if (z) {
                }
                animatorSet.cancel();
                this.animatorSet = null;
                if (z) {
                }
                if (!z) {
                }
            } else {
                color = Theme.getColor(this.grayIconColor);
                int i4 = z10 ? 4 : 0;
                this.currentIconGray = true;
                i = i4;
                z4 = false;
                if (!isSelfUser()) {
                    this.statusTextView[4].setTextColor(Theme.getColor(this.grayIconColor));
                }
                if (!isSelfUser()) {
                    if (!z10 && !this.hasAvatar) {
                        if (this.currentUser != null) {
                            this.statusTextView[4].setText(LocaleController.getString("TapToAddPhotoOrBio", R.string.TapToAddPhotoOrBio));
                        } else {
                            this.statusTextView[4].setText(LocaleController.getString("TapToAddPhotoOrDescription", R.string.TapToAddPhotoOrDescription));
                        }
                        this.statusTextView[4].setTextColor(Theme.getColor(this.grayIconColor));
                    } else if (!z10) {
                        if (this.currentUser != null) {
                            this.statusTextView[4].setText(LocaleController.getString("TapToAddBio", R.string.TapToAddBio));
                        } else {
                            this.statusTextView[4].setText(LocaleController.getString("TapToAddDescription", R.string.TapToAddDescription));
                        }
                        this.statusTextView[4].setTextColor(Theme.getColor(this.grayIconColor));
                    } else if (!this.hasAvatar) {
                        this.statusTextView[4].setText(LocaleController.getString("TapToAddPhoto", R.string.TapToAddPhoto));
                        this.statusTextView[4].setTextColor(Theme.getColor(this.grayIconColor));
                    } else {
                        this.statusTextView[4].setText(LocaleController.getString("ThisIsYou", R.string.ThisIsYou));
                        this.statusTextView[4].setTextColor(Theme.getColor(str));
                    }
                    if (z10) {
                        this.fullAboutTextView.setText(AndroidUtilities.replaceNewLines(this.participant.about));
                        this.fullAboutTextView.setTextColor(Theme.getColor("voipgroup_mutedIcon"));
                    } else {
                        this.fullAboutTextView.setText(this.statusTextView[i].getText());
                        this.fullAboutTextView.setTextColor(this.statusTextView[i].getTextColor());
                    }
                } else if (z10) {
                    this.statusTextView[4].setText(AndroidUtilities.replaceNewLines(this.participant.about));
                    this.fullAboutTextView.setText("");
                } else {
                    this.statusTextView[4].setText("");
                    this.fullAboutTextView.setText("");
                }
                animatorSet = this.animatorSet;
                boolean z13 = animatorSet == null && !(i == this.currentStatus && this.lastMuteColor == color);
                if ((z || z13) && animatorSet != null) {
                    animatorSet.cancel();
                    this.animatorSet = null;
                }
                if (z || this.lastMuteColor != color || z13) {
                    if (!z) {
                        arrayList = new ArrayList();
                        final int i5 = this.lastMuteColor;
                        this.lastMuteColor = color;
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                GroupCallUserCell.this.lambda$applyParticipantChanges$6(i5, color, valueAnimator);
                            }
                        });
                        arrayList.add(ofFloat);
                        if (i == 1) {
                            int participantVolume = ChatObject.getParticipantVolume(this.participant);
                            int i6 = participantVolume / 100;
                            if (i6 != 100) {
                                this.statusTextView[1].setLeftDrawable(this.speakingDrawable);
                                SimpleTextView simpleTextView = this.statusTextView[1];
                                int i7 = R.string.SpeakingWithVolume;
                                Object[] objArr = new Object[1];
                                if (participantVolume < 100) {
                                    i6 = 1;
                                }
                                objArr[0] = Integer.valueOf(i6);
                                simpleTextView.setText(LocaleController.formatString("SpeakingWithVolume", i7, objArr));
                            } else {
                                this.statusTextView[1].setLeftDrawable((Drawable) null);
                                this.statusTextView[1].setText(LocaleController.getString("Speaking", R.string.Speaking));
                            }
                        }
                        if (!isSelfUser()) {
                            applyStatus(4);
                        } else if (!z || i != this.currentStatus || z13) {
                            if (z) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                if (i != 0) {
                                    int i8 = 0;
                                    while (true) {
                                        SimpleTextView[] simpleTextViewArr = this.statusTextView;
                                        if (i8 >= simpleTextViewArr.length) {
                                            break;
                                        }
                                        SimpleTextView simpleTextView2 = simpleTextViewArr[i8];
                                        Property property = View.TRANSLATION_Y;
                                        float[] fArr = new float[1];
                                        if (i8 == i) {
                                            dp = 0.0f;
                                        } else {
                                            dp = AndroidUtilities.dp(i8 == 0 ? 2.0f : -2.0f);
                                        }
                                        fArr[0] = dp;
                                        arrayList.add(ObjectAnimator.ofFloat(simpleTextView2, property, fArr));
                                        SimpleTextView simpleTextView3 = this.statusTextView[i8];
                                        Property property2 = View.ALPHA;
                                        float[] fArr2 = new float[1];
                                        fArr2[0] = i8 == i ? 1.0f : 0.0f;
                                        arrayList.add(ObjectAnimator.ofFloat(simpleTextView3, property2, fArr2));
                                        i8++;
                                    }
                                } else {
                                    int i9 = 0;
                                    while (true) {
                                        SimpleTextView[] simpleTextViewArr2 = this.statusTextView;
                                        if (i9 >= simpleTextViewArr2.length) {
                                            break;
                                        }
                                        SimpleTextView simpleTextView4 = simpleTextViewArr2[i9];
                                        Property property3 = View.TRANSLATION_Y;
                                        float[] fArr3 = new float[1];
                                        fArr3[0] = i9 == i ? 0.0f : AndroidUtilities.dp(-2.0f);
                                        arrayList.add(ObjectAnimator.ofFloat(simpleTextView4, property3, fArr3));
                                        SimpleTextView simpleTextView5 = this.statusTextView[i9];
                                        Property property4 = View.ALPHA;
                                        float[] fArr4 = new float[1];
                                        fArr4[0] = i9 == i ? 1.0f : 0.0f;
                                        arrayList.add(ObjectAnimator.ofFloat(simpleTextView5, property4, fArr4));
                                        i9++;
                                    }
                                }
                            } else {
                                applyStatus(i);
                            }
                            this.currentStatus = i;
                        }
                        this.avatarWavesDrawable.setMuted(i, z);
                        if (arrayList != null) {
                            AnimatorSet animatorSet2 = this.animatorSet;
                            if (animatorSet2 != null) {
                                animatorSet2.cancel();
                                this.animatorSet = null;
                            }
                            AnimatorSet animatorSet3 = new AnimatorSet();
                            this.animatorSet = animatorSet3;
                            animatorSet3.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.GroupCallUserCell.4
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    if (!GroupCallUserCell.this.isSelfUser()) {
                                        GroupCallUserCell.this.applyStatus(i);
                                    }
                                    GroupCallUserCell.this.animatorSet = null;
                                }
                            });
                            this.animatorSet.playTogether(arrayList);
                            this.animatorSet.setDuration(180L);
                            this.animatorSet.start();
                        }
                        if (z || this.lastMuted != z8 || this.lastRaisedHand != z4) {
                            if (!z4) {
                                customEndFrame = this.muteDrawable.setCustomEndFrame(84);
                                if (z) {
                                    this.muteDrawable.setOnFinishCallback(this.raiseHandCallback, 83);
                                } else {
                                    this.muteDrawable.setOnFinishCallback(null, 0);
                                }
                            } else {
                                this.muteButton.setAnimation(this.muteDrawable);
                                this.muteDrawable.setOnFinishCallback(null, 0);
                                if (z8 && this.lastRaisedHand) {
                                    customEndFrame = this.muteDrawable.setCustomEndFrame(21);
                                } else {
                                    customEndFrame = this.muteDrawable.setCustomEndFrame(z8 ? 64 : 42);
                                }
                            }
                            if (!z) {
                                if (customEndFrame) {
                                    if (i == 3) {
                                        this.muteDrawable.setCurrentFrame(63);
                                    } else if (z8 && this.lastRaisedHand && !z4) {
                                        this.muteDrawable.setCurrentFrame(0);
                                    } else if (z8) {
                                        this.muteDrawable.setCurrentFrame(43);
                                    } else {
                                        this.muteDrawable.setCurrentFrame(21);
                                    }
                                }
                                this.muteButton.playAnimation();
                            } else {
                                RLottieDrawable rLottieDrawable = this.muteDrawable;
                                rLottieDrawable.setCurrentFrame(rLottieDrawable.getCustomEndFrame() - 1, false, true);
                                this.muteButton.invalidate();
                            }
                            this.lastMuted = z8;
                            this.lastRaisedHand = z4;
                        }
                        if (!this.isSpeaking) {
                            this.avatarWavesDrawable.setAmplitude(0.0d);
                        }
                        AvatarWavesDrawable avatarWavesDrawable = this.avatarWavesDrawable;
                        if (this.isSpeaking && this.progressToAvatarPreview == 0.0f) {
                            z5 = true;
                        }
                        avatarWavesDrawable.setShowWaves(z5, this);
                    }
                    RLottieImageView rLottieImageView = this.muteButton;
                    this.lastMuteColor = color;
                    rLottieImageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                    Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), color & 620756991, true);
                }
                arrayList = null;
                if (i == 1) {
                }
                if (!isSelfUser()) {
                }
                this.avatarWavesDrawable.setMuted(i, z);
                if (arrayList != null) {
                }
                if (z) {
                }
                if (!z4) {
                }
                if (!z) {
                }
                this.lastMuted = z8;
                this.lastRaisedHand = z4;
                if (!this.isSpeaking) {
                }
                AvatarWavesDrawable avatarWavesDrawable2 = this.avatarWavesDrawable;
                if (this.isSpeaking) {
                    z5 = true;
                }
                avatarWavesDrawable2.setShowWaves(z5, this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyParticipantChanges$6(int i, int i2, ValueAnimator valueAnimator) {
        int offsetColor = AndroidUtilities.getOffsetColor(i, i2, valueAnimator.getAnimatedFraction(), 1.0f);
        this.muteButton.setColorFilter(new PorterDuffColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY));
        Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), offsetColor & 620756991, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyStatus(int i) {
        float dp;
        int i2 = 0;
        if (i == 0) {
            while (true) {
                SimpleTextView[] simpleTextViewArr = this.statusTextView;
                if (i2 >= simpleTextViewArr.length) {
                    return;
                }
                simpleTextViewArr[i2].setTranslationY(i2 == i ? 0.0f : AndroidUtilities.dp(-2.0f));
                this.statusTextView[i2].setAlpha(i2 == i ? 1.0f : 0.0f);
                i2++;
            }
        } else {
            while (true) {
                SimpleTextView[] simpleTextViewArr2 = this.statusTextView;
                if (i2 >= simpleTextViewArr2.length) {
                    return;
                }
                SimpleTextView simpleTextView = simpleTextViewArr2[i2];
                if (i2 == i) {
                    dp = 0.0f;
                } else {
                    dp = AndroidUtilities.dp(i2 == 0 ? 2.0f : -2.0f);
                }
                simpleTextView.setTranslationY(dp);
                this.statusTextView[i2].setAlpha(i2 == i ? 1.0f : 0.0f);
                i2++;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.needDivider) {
            float f = this.progressToAvatarPreview;
            if (f != 0.0f) {
                this.dividerPaint.setAlpha((int) ((1.0f - f) * 255.0f));
            } else {
                this.dividerPaint.setAlpha((int) ((1.0f - this.statusTextView[4].getFullAlpha()) * 255.0f));
            }
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(68.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(68.0f) : 0), getMeasuredHeight() - 1, this.dividerPaint);
        }
        int left = this.avatarImageView.getLeft() + (this.avatarImageView.getMeasuredWidth() / 2);
        int top = this.avatarImageView.getTop() + (this.avatarImageView.getMeasuredHeight() / 2);
        this.avatarWavesDrawable.update();
        if (this.progressToAvatarPreview == 0.0f) {
            this.avatarWavesDrawable.draw(canvas, left, top, this);
        }
        this.avatarImageView.setScaleX(this.avatarWavesDrawable.getAvatarScale());
        this.avatarImageView.setScaleY(this.avatarWavesDrawable.getAvatarScale());
        this.avatarProgressView.setScaleX(this.avatarWavesDrawable.getAvatarScale());
        this.avatarProgressView.setScaleY(this.avatarWavesDrawable.getAvatarScale());
        super.dispatchDraw(canvas);
    }

    /* loaded from: classes3.dex */
    public static class AvatarWavesDrawable {
        float amplitude;
        float animateAmplitudeDiff;
        float animateToAmplitude;
        private BlobDrawable blobDrawable2;
        private boolean hasCustomColor;
        private int isMuted;
        boolean showWaves;
        float wavesEnter = 0.0f;
        private float progressToMuted = 0.0f;
        boolean invalidateColor = true;
        private BlobDrawable blobDrawable = new BlobDrawable(6);

        public AvatarWavesDrawable(int i, int i2) {
            BlobDrawable blobDrawable = new BlobDrawable(8);
            this.blobDrawable2 = blobDrawable;
            BlobDrawable blobDrawable2 = this.blobDrawable;
            float f = i;
            blobDrawable2.minRadius = f;
            float f2 = i2;
            blobDrawable2.maxRadius = f2;
            blobDrawable.minRadius = f;
            blobDrawable.maxRadius = f2;
            blobDrawable2.generateBlob();
            this.blobDrawable2.generateBlob();
            this.blobDrawable.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_speakingText"), 38));
            this.blobDrawable2.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_speakingText"), 38));
        }

        public void update() {
            float f = this.animateToAmplitude;
            float f2 = this.amplitude;
            if (f != f2) {
                float f3 = this.animateAmplitudeDiff;
                float f4 = f2 + (16.0f * f3);
                this.amplitude = f4;
                if (f3 > 0.0f) {
                    if (f4 > f) {
                        this.amplitude = f;
                    }
                } else if (f4 < f) {
                    this.amplitude = f;
                }
            }
            boolean z = this.showWaves;
            if (z) {
                float f5 = this.wavesEnter;
                if (f5 != 1.0f) {
                    float f6 = f5 + 0.045714285f;
                    this.wavesEnter = f6;
                    if (f6 > 1.0f) {
                        this.wavesEnter = 1.0f;
                        return;
                    }
                    return;
                }
            }
            if (z) {
                return;
            }
            float f7 = this.wavesEnter;
            if (f7 != 0.0f) {
                float f8 = f7 - 0.045714285f;
                this.wavesEnter = f8;
                if (f8 < 0.0f) {
                    this.wavesEnter = 0.0f;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0068  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, float f, float f2, View view) {
            if (SharedConfig.getLiteMode().enabled()) {
                return;
            }
            float f3 = (this.amplitude * 0.4f) + 0.8f;
            if (this.showWaves || this.wavesEnter != 0.0f) {
                canvas.save();
                float interpolation = f3 * CubicBezierInterpolator.DEFAULT.getInterpolation(this.wavesEnter);
                canvas.scale(interpolation, interpolation, f, f2);
                if (!this.hasCustomColor) {
                    int i = this.isMuted;
                    if (i != 1) {
                        float f4 = this.progressToMuted;
                        if (f4 != 1.0f) {
                            float f5 = f4 + 0.10666667f;
                            this.progressToMuted = f5;
                            if (f5 > 1.0f) {
                                this.progressToMuted = 1.0f;
                            }
                            this.invalidateColor = true;
                            if (this.invalidateColor) {
                                this.blobDrawable.paint.setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(Theme.getColor("voipgroup_speakingText"), Theme.getColor(this.isMuted == 2 ? "voipgroup_mutedByAdminIcon" : "voipgroup_listeningText"), this.progressToMuted), 38));
                            }
                        }
                    }
                    if (i == 1) {
                        float f6 = this.progressToMuted;
                        if (f6 != 0.0f) {
                            float f7 = f6 - 0.10666667f;
                            this.progressToMuted = f7;
                            if (f7 < 0.0f) {
                                this.progressToMuted = 0.0f;
                            }
                            this.invalidateColor = true;
                        }
                    }
                    if (this.invalidateColor) {
                    }
                }
                this.blobDrawable.update(this.amplitude, 1.0f);
                BlobDrawable blobDrawable = this.blobDrawable;
                blobDrawable.draw(f, f2, canvas, blobDrawable.paint);
                this.blobDrawable2.update(this.amplitude, 1.0f);
                this.blobDrawable2.draw(f, f2, canvas, this.blobDrawable.paint);
                canvas.restore();
            }
            if (this.wavesEnter != 0.0f) {
                view.invalidate();
            }
        }

        public float getAvatarScale() {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnter);
            return (((this.amplitude * 0.2f) + 0.9f) * interpolation) + ((1.0f - interpolation) * 1.0f);
        }

        public void setShowWaves(boolean z, View view) {
            if (this.showWaves != z) {
                view.invalidate();
            }
            this.showWaves = z;
        }

        public void setAmplitude(double d) {
            float f = ((float) d) / 80.0f;
            float f2 = 0.0f;
            if (!this.showWaves) {
                f = 0.0f;
            }
            if (f > 1.0f) {
                f2 = 1.0f;
            } else if (f >= 0.0f) {
                f2 = f;
            }
            this.animateToAmplitude = f2;
            this.animateAmplitudeDiff = (f2 - this.amplitude) / 200.0f;
        }

        public void setColor(int i) {
            this.hasCustomColor = true;
            this.blobDrawable.paint.setColor(i);
        }

        public void setMuted(int i, boolean z) {
            this.isMuted = i;
            if (!z) {
                this.progressToMuted = i != 1 ? 1.0f : 0.0f;
            }
            this.invalidateColor = true;
        }
    }

    public BackupImageView getAvatarImageView() {
        return this.avatarImageView;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        String str;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (!accessibilityNodeInfo.isEnabled() || Build.VERSION.SDK_INT < 21) {
            return;
        }
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = this.participant;
        if (!tLRPC$TL_groupCallParticipant.muted || tLRPC$TL_groupCallParticipant.can_self_unmute) {
            i = R.string.VoipMute;
            str = "VoipMute";
        } else {
            i = R.string.VoipUnmute;
            str = "VoipUnmute";
        }
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(str, i)));
    }

    public long getPeerId() {
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = this.participant;
        if (tLRPC$TL_groupCallParticipant == null) {
            return 0L;
        }
        return MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
    }
}
