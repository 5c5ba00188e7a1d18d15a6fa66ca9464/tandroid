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
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme$$ExternalSyntheticApiModelOutline2;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BlobDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;

/* loaded from: classes4.dex */
public class GroupCallUserCell extends FrameLayout {
    private AccountInstance accountInstance;
    private AnimatorSet animatorSet;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private RadialProgressView avatarProgressView;
    private AvatarWavesDrawable avatarWavesDrawable;
    private Runnable checkRaiseRunnable;
    private ChatObject.Call currentCall;
    private TLRPC.Chat currentChat;
    private boolean currentIconGray;
    private int currentStatus;
    private TLRPC.User currentUser;
    private Paint dividerPaint;
    private SimpleTextView fullAboutTextView;
    private int grayIconColor;
    private boolean hasAvatar;
    private boolean isSpeaking;
    private int lastMuteColor;
    private boolean lastMuted;
    private boolean lastRaisedHand;
    private RLottieImageView muteButton;
    private RLottieDrawable muteDrawable;
    private SimpleTextView nameTextView;
    private boolean needDivider;
    private TLRPC.TL_groupCallParticipant participant;
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

    /* loaded from: classes4.dex */
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
            Paint paint = this.blobDrawable.paint;
            int i3 = Theme.key_voipgroup_speakingText;
            paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i3), 38));
            this.blobDrawable2.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i3), 38));
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x0064  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, float f, float f2, View view) {
            if (LiteMode.isEnabled(512)) {
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
                                    this.blobDrawable.paint.setColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(Theme.getColor(Theme.key_voipgroup_speakingText), Theme.getColor(this.isMuted == 2 ? Theme.key_voipgroup_mutedByAdminIcon : Theme.key_voipgroup_listeningText), this.progressToMuted), 38));
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
        }

        public float getAvatarScale() {
            float f = (this.amplitude * 0.2f) + 0.9f;
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnter);
            return (f * interpolation) + ((1.0f - interpolation) * 1.0f);
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

        public void setShowWaves(boolean z, View view) {
            if (this.showWaves != z) {
                view.invalidate();
            }
            this.showWaves = z;
        }

        public void update() {
            float f = this.animateToAmplitude;
            float f2 = this.amplitude;
            if (f != f2) {
                float f3 = this.animateAmplitudeDiff;
                float f4 = f2 + (16.0f * f3);
                this.amplitude = f4;
                if (f3 <= 0.0f ? f4 < f : f4 > f) {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class VerifiedDrawable extends Drawable {
        private Drawable[] drawables;

        public VerifiedDrawable(Context context) {
            Drawable[] drawableArr = new Drawable[2];
            this.drawables = drawableArr;
            drawableArr[0] = context.getResources().getDrawable(R.drawable.verified_area).mutate();
            this.drawables[0].setColorFilter(new PorterDuffColorFilter(-9063442, PorterDuff.Mode.MULTIPLY));
            this.drawables[1] = context.getResources().getDrawable(R.drawable.verified_check).mutate();
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
        public int getIntrinsicHeight() {
            return this.drawables[0].getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.drawables[0].getIntrinsicWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
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

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x01b7, code lost:
    
        if (r11 != false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x024f, code lost:
    
        r20 = r12;
        r21 = r13;
        r19 = r14;
        r23 = 54.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0246, code lost:
    
        r20 = r12;
        r21 = r13;
        r19 = r14;
        r23 = 67.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0244, code lost:
    
        if (r11 == false) goto L79;
     */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0238  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public GroupCallUserCell(Context context) {
        super(context);
        SimpleTextView simpleTextView;
        int i;
        SimpleTextView simpleTextView2;
        int i2;
        float f;
        float f2;
        int i3;
        float f3;
        float f4;
        float f5;
        this.statusTextView = new SimpleTextView[5];
        this.shakeHandCallback = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda1
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
        this.grayIconColor = Theme.key_voipgroup_mutedIcon;
        this.checkRaiseRunnable = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallUserCell.this.lambda$new$2();
            }
        };
        this.updateRunnable = new Runnable() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda4
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
        paint.setColor(Theme.getColor(Theme.key_voipgroup_actionBar));
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
        SimpleTextView simpleTextView3 = new SimpleTextView(context);
        this.nameTextView = simpleTextView3;
        simpleTextView3.setTextColor(Theme.getColor(Theme.key_voipgroup_nameText));
        this.nameTextView.setTypeface(AndroidUtilities.bold());
        this.nameTextView.setTextSize(16);
        this.nameTextView.setDrawablePadding(AndroidUtilities.dp(6.0f));
        this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        SimpleTextView simpleTextView4 = this.nameTextView;
        boolean z3 = LocaleController.isRTL;
        addView(simpleTextView4, LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? 54.0f : 67.0f, 10.0f, z3 ? 67.0f : 54.0f, 0.0f));
        this.rightDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this.nameTextView, AndroidUtilities.dp(20.0f), 9);
        Drawable drawable = context.getResources().getDrawable(R.drawable.voice_volume_mini);
        this.speakingDrawable = drawable;
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_voipgroup_speakingText), PorterDuff.Mode.MULTIPLY));
        final int i4 = 0;
        while (true) {
            SimpleTextView[] simpleTextViewArr = this.statusTextView;
            if (i4 >= simpleTextViewArr.length) {
                break;
            }
            simpleTextViewArr[i4] = new SimpleTextView(context) { // from class: org.telegram.ui.Cells.GroupCallUserCell.2
                float originalAlpha;

                @Override // android.view.View
                public float getAlpha() {
                    return this.originalAlpha;
                }

                @Override // android.view.View
                public void setAlpha(float f6) {
                    this.originalAlpha = f6;
                    if (i4 == 4) {
                        float fullAlpha = GroupCallUserCell.this.statusTextView[4].getFullAlpha();
                        if (GroupCallUserCell.this.isSelfUser() && GroupCallUserCell.this.progressToAvatarPreview > 0.0f) {
                            super.setAlpha(1.0f - GroupCallUserCell.this.progressToAvatarPreview);
                            return;
                        } else if (fullAlpha > 0.0f) {
                            f6 = Math.max(f6, fullAlpha);
                        }
                    } else {
                        f6 *= 1.0f - GroupCallUserCell.this.statusTextView[4].getFullAlpha();
                    }
                    super.setAlpha(f6);
                }

                @Override // org.telegram.ui.ActionBar.SimpleTextView
                public void setFullAlpha(float f6) {
                    super.setFullAlpha(f6);
                    for (int i5 = 0; i5 < GroupCallUserCell.this.statusTextView.length; i5++) {
                        GroupCallUserCell.this.statusTextView[i5].setAlpha(GroupCallUserCell.this.statusTextView[i5].getAlpha());
                    }
                }

                @Override // android.view.View
                public void setTranslationY(float f6) {
                    if (i4 == 4 && getFullAlpha() > 0.0f) {
                        f6 = 0.0f;
                    }
                    super.setTranslationY(f6);
                }
            };
            this.statusTextView[i4].setTextSize(15);
            this.statusTextView[i4].setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            if (i4 == 4) {
                this.statusTextView[i4].setBuildFullLayout(true);
                this.statusTextView[i4].setTextColor(Theme.getColor(Theme.key_voipgroup_mutedIcon));
                simpleTextView2 = this.statusTextView[i4];
                boolean z4 = LocaleController.isRTL;
                i2 = (z4 ? 5 : 3) | 48;
                f = z4 ? 54.0f : 67.0f;
                f2 = -2.0f;
            } else {
                if (i4 == 0) {
                    this.statusTextView[i4].setTextColor(Theme.getColor(Theme.key_voipgroup_listeningText));
                    simpleTextView = this.statusTextView[i4];
                    i = R.string.Listening;
                } else {
                    if (i4 == 1) {
                        this.statusTextView[i4].setTextColor(Theme.getColor(Theme.key_voipgroup_speakingText));
                        this.statusTextView[i4].setText(LocaleController.getString(R.string.Speaking));
                        this.statusTextView[i4].setDrawablePadding(AndroidUtilities.dp(2.0f));
                    } else if (i4 == 2) {
                        this.statusTextView[i4].setTextColor(Theme.getColor(Theme.key_voipgroup_mutedByAdminIcon));
                        simpleTextView = this.statusTextView[i4];
                        i = R.string.VoipGroupMutedForMe;
                    } else if (i4 == 3) {
                        this.statusTextView[i4].setTextColor(Theme.getColor(Theme.key_voipgroup_listeningText));
                        simpleTextView = this.statusTextView[i4];
                        i = R.string.WantsToSpeak;
                    }
                    simpleTextView2 = this.statusTextView[i4];
                    boolean z5 = LocaleController.isRTL;
                    i2 = (!z5 ? 5 : 3) | 48;
                    f = !z5 ? 54.0f : 67.0f;
                    f2 = 20.0f;
                }
                simpleTextView.setText(LocaleController.getString(i));
                simpleTextView2 = this.statusTextView[i4];
                boolean z52 = LocaleController.isRTL;
                i2 = (!z52 ? 5 : 3) | 48;
                if (!z52) {
                }
                f2 = 20.0f;
            }
            addView(simpleTextView2, LayoutHelper.createFrame(-1, f4, i3, f3, 32.0f, f5, 0.0f));
            i4++;
        }
        SimpleTextView simpleTextView5 = new SimpleTextView(context);
        this.fullAboutTextView = simpleTextView5;
        simpleTextView5.setMaxLines(3);
        this.fullAboutTextView.setTextSize(15);
        this.fullAboutTextView.setTextColor(Theme.getColor(Theme.key_voipgroup_mutedIcon));
        this.fullAboutTextView.setVisibility(8);
        addView(this.fullAboutTextView, LayoutHelper.createFrame(-1, 60.0f, (LocaleController.isRTL ? 5 : 3) | 48, 14.0f, 32.0f, 14.0f, 0.0f));
        int i5 = R.raw.voice_outlined2;
        this.muteDrawable = new RLottieDrawable(i5, "" + i5, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(32.0f), true, null);
        int i6 = R.raw.hand_1;
        this.shakeHandDrawable = new RLottieDrawable(i6, "" + i6, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(32.0f), true, null);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteButton = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.muteButton.setAnimation(this.muteDrawable);
        if (Build.VERSION.SDK_INT >= 21) {
            RippleDrawable m = Theme$$ExternalSyntheticApiModelOutline2.m(Theme.createSelectorDrawable(Theme.getColor(this.grayIconColor) & 620756991));
            Theme.setRippleDrawableForceSoftware(m);
            this.muteButton.setBackground(m);
        }
        this.muteButton.setImportantForAccessibility(2);
        addView(this.muteButton, LayoutHelper.createFrame(48, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 6.0f, 0.0f, 6.0f, 0.0f));
        this.muteButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallUserCell.this.lambda$new$5(view);
            }
        });
        this.avatarWavesDrawable = new AvatarWavesDrawable(AndroidUtilities.dp(26.0f), AndroidUtilities.dp(29.0f));
        setWillNotDraw(false);
        setFocusable(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:223:0x0141, code lost:
    
        r11 = r2;
        r2 = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x0130, code lost:
    
        if (r7 != false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x013f, code lost:
    
        if (r7 != false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00f1, code lost:
    
        if (r7 != false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0144, code lost:
    
        r11 = r2;
        r2 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x02f4  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0449  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0432  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x02aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyParticipantChanges(boolean z, boolean z2) {
        final int color;
        final int i;
        boolean z3;
        ArrayList arrayList;
        float dp;
        boolean customEndFrame;
        RLottieDrawable rLottieDrawable;
        int i2;
        SimpleTextView simpleTextView;
        int i3;
        SimpleTextView simpleTextView2;
        int i4;
        SimpleTextView simpleTextView3;
        int textColor;
        SimpleTextView simpleTextView4;
        int i5;
        int i6;
        boolean z4 = false;
        if (this.currentCall == null) {
            return;
        }
        this.muteButton.setEnabled((isSelfUser() && this.participant.raise_hand_rating == 0) ? false : true);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.participant;
        boolean z5 = elapsedRealtime - tL_groupCallParticipant.lastVoiceUpdateTime < 500 ? tL_groupCallParticipant.hasVoiceDelayed : tL_groupCallParticipant.hasVoice;
        if (!z2) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.participant.lastSpeakTime;
            boolean z6 = uptimeMillis < 500;
            if (!this.isSpeaking || !z6 || z5) {
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
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = (TLRPC.TL_groupCallParticipant) this.currentCall.participants.get(MessageObject.getPeerId(this.participant.peer));
        if (tL_groupCallParticipant2 != null) {
            this.participant = tL_groupCallParticipant2;
        }
        boolean z7 = this.participant.muted_by_you && !isSelfUser();
        boolean z8 = !isSelfUser() ? (!this.participant.muted || (this.isSpeaking && z5)) && !z7 : VoIPService.getSharedInstance() == null || !VoIPService.getSharedInstance().isMicMute() || (this.isSpeaking && z5);
        if (z8) {
            boolean z9 = this.participant.can_self_unmute;
        }
        boolean z10 = !TextUtils.isEmpty(this.participant.about);
        this.currentIconGray = false;
        AndroidUtilities.cancelRunOnUIThread(this.checkRaiseRunnable);
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant3 = this.participant;
        if ((!tL_groupCallParticipant3.muted || this.isSpeaking) && !z7) {
            if (this.isSpeaking && z5) {
                color = Theme.getColor(Theme.key_voipgroup_speakingText);
                i = 1;
            } else {
                color = Theme.getColor(this.grayIconColor);
                i = z10 ? 4 : 0;
                this.currentIconGray = true;
            }
            z3 = false;
        } else {
            boolean z11 = tL_groupCallParticipant3.can_self_unmute;
            if (!z11 || z7) {
                z3 = (z11 || tL_groupCallParticipant3.raise_hand_rating == 0) ? false : true;
                if (z3) {
                    int color2 = Theme.getColor(Theme.key_voipgroup_listeningText);
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    long j = this.participant.lastRaiseHandDate;
                    long j2 = elapsedRealtime2 - j;
                    if (j != 0 && j2 <= 5000) {
                        AndroidUtilities.runOnUIThread(this.checkRaiseRunnable, 5000 - j2);
                        color = color2;
                        i = 3;
                    } else if (z7) {
                        color = color2;
                        i = 2;
                    } else {
                        i6 = color2;
                    }
                } else {
                    int color3 = Theme.getColor(Theme.key_voipgroup_mutedByAdminIcon);
                    if (z7) {
                        color = color3;
                        i = 2;
                    } else {
                        i6 = color3;
                    }
                }
            } else {
                i6 = Theme.getColor(this.grayIconColor);
                this.currentIconGray = true;
                z3 = false;
            }
        }
        if (!isSelfUser()) {
            this.statusTextView[4].setTextColor(Theme.getColor(this.grayIconColor));
        }
        if (isSelfUser()) {
            if (!z10 && !this.hasAvatar) {
                if (this.currentUser != null) {
                    simpleTextView4 = this.statusTextView[4];
                    i5 = R.string.TapToAddPhotoOrBio;
                } else {
                    simpleTextView4 = this.statusTextView[4];
                    i5 = R.string.TapToAddPhotoOrDescription;
                }
                simpleTextView4.setText(LocaleController.getString(i5));
                simpleTextView = this.statusTextView[4];
            } else if (!z10) {
                if (this.currentUser != null) {
                    simpleTextView2 = this.statusTextView[4];
                    i4 = R.string.TapToAddBio;
                } else {
                    simpleTextView2 = this.statusTextView[4];
                    i4 = R.string.TapToAddDescription;
                }
                simpleTextView2.setText(LocaleController.getString(i4));
                simpleTextView = this.statusTextView[4];
            } else if (this.hasAvatar) {
                this.statusTextView[4].setText(LocaleController.getString(R.string.ThisIsYou));
                simpleTextView = this.statusTextView[4];
                i3 = Theme.key_voipgroup_listeningText;
                simpleTextView.setTextColor(Theme.getColor(i3));
                if (z10) {
                    this.fullAboutTextView.setText(this.statusTextView[i].getText());
                    simpleTextView3 = this.fullAboutTextView;
                    textColor = this.statusTextView[i].getTextColor();
                } else {
                    this.fullAboutTextView.setText(AndroidUtilities.replaceNewLines(this.participant.about));
                    simpleTextView3 = this.fullAboutTextView;
                    textColor = Theme.getColor(Theme.key_voipgroup_mutedIcon);
                }
                simpleTextView3.setTextColor(textColor);
            } else {
                this.statusTextView[4].setText(LocaleController.getString(R.string.TapToAddPhoto));
                simpleTextView = this.statusTextView[4];
            }
            i3 = this.grayIconColor;
            simpleTextView.setTextColor(Theme.getColor(i3));
            if (z10) {
            }
            simpleTextView3.setTextColor(textColor);
        } else {
            if (z10) {
                this.statusTextView[4].setText(AndroidUtilities.replaceNewLines(this.participant.about));
            } else {
                this.statusTextView[4].setText("");
            }
            this.fullAboutTextView.setText("");
        }
        AnimatorSet animatorSet = this.animatorSet;
        boolean z12 = (animatorSet == null || (i == this.currentStatus && this.lastMuteColor == color)) ? false : true;
        if ((!z || z12) && animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        if (!z || this.lastMuteColor != color || z12) {
            if (z) {
                arrayList = new ArrayList();
                final int i7 = this.lastMuteColor;
                this.lastMuteColor = color;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.GroupCallUserCell$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        GroupCallUserCell.this.lambda$applyParticipantChanges$6(i7, color, valueAnimator);
                    }
                });
                arrayList.add(ofFloat);
                if (i == 1) {
                    int participantVolume = ChatObject.getParticipantVolume(this.participant);
                    int i8 = participantVolume / 100;
                    if (i8 != 100) {
                        this.statusTextView[1].setLeftDrawable(this.speakingDrawable);
                        SimpleTextView simpleTextView5 = this.statusTextView[1];
                        int i9 = R.string.SpeakingWithVolume;
                        if (participantVolume < 100) {
                            i8 = 1;
                        }
                        simpleTextView5.setText(LocaleController.formatString("SpeakingWithVolume", i9, Integer.valueOf(i8)));
                    } else {
                        this.statusTextView[1].setLeftDrawable((Drawable) null);
                        this.statusTextView[1].setText(LocaleController.getString(R.string.Speaking));
                    }
                }
                if (!isSelfUser()) {
                    applyStatus(4);
                } else if (!z || i != this.currentStatus || z12) {
                    if (z) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        int i10 = 0;
                        if (i != 0) {
                            while (true) {
                                SimpleTextView[] simpleTextViewArr = this.statusTextView;
                                if (i10 >= simpleTextViewArr.length) {
                                    break;
                                }
                                SimpleTextView simpleTextView6 = simpleTextViewArr[i10];
                                Property property = View.TRANSLATION_Y;
                                if (i10 == i) {
                                    dp = 0.0f;
                                } else {
                                    dp = AndroidUtilities.dp(i10 == 0 ? 2.0f : -2.0f);
                                }
                                arrayList.add(ObjectAnimator.ofFloat(simpleTextView6, (Property<SimpleTextView, Float>) property, dp));
                                arrayList.add(ObjectAnimator.ofFloat(this.statusTextView[i10], (Property<SimpleTextView, Float>) View.ALPHA, i10 == i ? 1.0f : 0.0f));
                                i10++;
                            }
                        } else {
                            while (true) {
                                SimpleTextView[] simpleTextViewArr2 = this.statusTextView;
                                if (i10 >= simpleTextViewArr2.length) {
                                    break;
                                }
                                arrayList.add(ObjectAnimator.ofFloat(simpleTextViewArr2[i10], (Property<SimpleTextView, Float>) View.TRANSLATION_Y, i10 == i ? 0.0f : AndroidUtilities.dp(-2.0f)));
                                arrayList.add(ObjectAnimator.ofFloat(this.statusTextView[i10], (Property<SimpleTextView, Float>) View.ALPHA, i10 == i ? 1.0f : 0.0f));
                                i10++;
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
                if (z || this.lastMuted != z8 || this.lastRaisedHand != z3) {
                    if (z3) {
                        this.muteButton.setAnimation(this.muteDrawable);
                        this.muteDrawable.setOnFinishCallback(null, 0);
                        if (z8 && this.lastRaisedHand) {
                            customEndFrame = this.muteDrawable.setCustomEndFrame(21);
                        } else {
                            customEndFrame = this.muteDrawable.setCustomEndFrame(z8 ? 64 : 42);
                        }
                    } else {
                        customEndFrame = this.muteDrawable.setCustomEndFrame(84);
                        if (z) {
                            this.muteDrawable.setOnFinishCallback(this.raiseHandCallback, 83);
                        } else {
                            this.muteDrawable.setOnFinishCallback(null, 0);
                        }
                    }
                    if (z) {
                        RLottieDrawable rLottieDrawable2 = this.muteDrawable;
                        rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getCustomEndFrame() - 1, false, true);
                        this.muteButton.invalidate();
                    } else {
                        if (customEndFrame) {
                            if (i == 3) {
                                rLottieDrawable = this.muteDrawable;
                                i2 = 63;
                            } else if (z8 && this.lastRaisedHand && !z3) {
                                this.muteDrawable.setCurrentFrame(0);
                            } else {
                                rLottieDrawable = this.muteDrawable;
                                if (z8) {
                                    i2 = 43;
                                } else {
                                    rLottieDrawable.setCurrentFrame(21);
                                }
                            }
                            rLottieDrawable.setCurrentFrame(i2);
                        }
                        this.muteButton.playAnimation();
                    }
                    this.lastMuted = z8;
                    this.lastRaisedHand = z3;
                }
                if (!this.isSpeaking) {
                    this.avatarWavesDrawable.setAmplitude(0.0d);
                }
                AvatarWavesDrawable avatarWavesDrawable = this.avatarWavesDrawable;
                if (this.isSpeaking && this.progressToAvatarPreview == 0.0f) {
                    z4 = true;
                }
                avatarWavesDrawable.setShowWaves(z4, this);
            }
            RLottieImageView rLottieImageView = this.muteButton;
            this.lastMuteColor = color;
            rLottieImageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), 620756991 & color, true);
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
        if (z3) {
        }
        if (z) {
        }
        this.lastMuted = z8;
        this.lastRaisedHand = z3;
        if (!this.isSpeaking) {
        }
        AvatarWavesDrawable avatarWavesDrawable2 = this.avatarWavesDrawable;
        if (this.isSpeaking) {
            z4 = true;
        }
        avatarWavesDrawable2.setShowWaves(z4, this);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyParticipantChanges$6(int i, int i2, ValueAnimator valueAnimator) {
        int offsetColor = AndroidUtilities.getOffsetColor(i, i2, valueAnimator.getAnimatedFraction(), 1.0f);
        this.muteButton.setColorFilter(new PorterDuffColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY));
        Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), offsetColor & 620756991, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.shakeHandDrawable.setOnFinishCallback(null, 0);
        this.muteDrawable.setOnFinishCallback(null, 0);
        this.muteButton.setAnimation(this.muteDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        int i;
        int nextInt = Utilities.random.nextInt(100);
        int i2 = 120;
        if (nextInt < 32) {
            i = 0;
        } else {
            i = NotificationCenter.needShareTheme;
            if (nextInt < 64) {
                i2 = NotificationCenter.needShareTheme;
                i = 120;
            } else {
                i2 = 420;
                if (nextInt >= 97) {
                    i = 540;
                    if (nextInt == 98) {
                        i2 = 540;
                        i = 420;
                    } else {
                        i2 = 720;
                    }
                }
            }
        }
        this.shakeHandDrawable.setCustomEndFrame(i2);
        this.shakeHandDrawable.setOnFinishCallback(this.shakeHandCallback, i2 - 1);
        this.muteButton.setAnimation(this.shakeHandDrawable);
        this.shakeHandDrawable.setCurrentFrame(i);
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

    public void applyParticipantChanges(boolean z) {
        applyParticipantChanges(z, false);
    }

    public boolean clickMuteButton() {
        if (!this.muteButton.isEnabled()) {
            return false;
        }
        this.muteButton.callOnClick();
        return true;
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

    public BackupImageView getAvatarImageView() {
        return this.avatarImageView;
    }

    public AvatarWavesDrawable getAvatarWavesDrawable() {
        return this.avatarWavesDrawable;
    }

    public int getClipHeight() {
        SimpleTextView simpleTextView = (TextUtils.isEmpty(this.fullAboutTextView.getText()) || !this.hasAvatar) ? this.statusTextView[4] : this.fullAboutTextView;
        if (simpleTextView.getLineCount() > 1) {
            return simpleTextView.getTop() + simpleTextView.getTextHeight() + AndroidUtilities.dp(8.0f);
        }
        return getMeasuredHeight();
    }

    public CharSequence getName() {
        return this.nameTextView.getText();
    }

    public TLRPC.TL_groupCallParticipant getParticipant() {
        return this.participant;
    }

    public long getPeerId() {
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.participant;
        if (tL_groupCallParticipant == null) {
            return 0L;
        }
        return MessageObject.getPeerId(tL_groupCallParticipant.peer);
    }

    public boolean hasAvatarSet() {
        return this.avatarImageView.getImageReceiver().hasNotThumb();
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isHandRaised() {
        return this.lastRaisedHand;
    }

    public boolean isSelfUser() {
        long j = this.selfId;
        if (j > 0) {
            TLRPC.User user = this.currentUser;
            return user != null && user.id == j;
        }
        TLRPC.Chat chat = this.currentChat;
        return chat != null && chat.id == (-j);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        applyParticipantChanges(false);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.rightDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.attach();
        }
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
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.rightDrawable;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.detach();
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (!accessibilityNodeInfo.isEnabled() || Build.VERSION.SDK_INT < 21) {
            return;
        }
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.participant;
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString((!tL_groupCallParticipant.muted || tL_groupCallParticipant.can_self_unmute) ? R.string.VoipMute : R.string.VoipUnmute)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(58.0f), 1073741824));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onMuteClick, reason: merged with bridge method [inline-methods] */
    public void lambda$new$5(GroupCallUserCell groupCallUserCell) {
    }

    public void setAboutVisible(boolean z) {
        if (z) {
            this.statusTextView[4].setTranslationY(0.0f);
        } else {
            this.statusTextView[4].setFullAlpha(0.0f);
        }
        invalidate();
    }

    public void setAboutVisibleProgress(int i, float f) {
        if (TextUtils.isEmpty(this.statusTextView[4].getText())) {
            f = 0.0f;
        }
        this.statusTextView[4].setFullAlpha(f);
        this.statusTextView[4].setFullLayoutAdditionalWidth(0, 0);
        invalidate();
    }

    public void setAmplitude(double d) {
        if (d <= 1.5d) {
            this.avatarWavesDrawable.setAmplitude(0.0d);
            return;
        }
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
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setData(AccountInstance accountInstance, TLRPC.TL_groupCallParticipant tL_groupCallParticipant, ChatObject.Call call, long j, TLRPC.FileLocation fileLocation, boolean z) {
        ImageLocation forChat;
        BackupImageView backupImageView;
        AvatarDrawable avatarDrawable;
        Object obj;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        Drawable drawable;
        this.currentCall = call;
        this.accountInstance = accountInstance;
        this.selfId = j;
        this.participant = tL_groupCallParticipant;
        long peerId = MessageObject.getPeerId(tL_groupCallParticipant.peer);
        if (peerId > 0) {
            this.currentUser = this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId));
            this.currentChat = null;
            this.avatarDrawable.setInfo(this.accountInstance.getCurrentAccount(), this.currentUser);
            this.nameTextView.setText(UserObject.getUserName(this.currentUser));
            TLRPC.User user = this.currentUser;
            if (user == null || !user.verified) {
                if (user == null || DialogObject.getEmojiStatusDocumentId(user.emoji_status) == 0) {
                    TLRPC.User user2 = this.currentUser;
                    if (user2 == null || !user2.premium) {
                        this.rightDrawable.set((Drawable) null, z);
                    } else {
                        if (this.premiumDrawable == null) {
                            this.premiumDrawable = getContext().getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                            this.premiumDrawable = new AnimatedEmojiDrawable.WrapSizeDrawable(this.premiumDrawable, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f)) { // from class: org.telegram.ui.Cells.GroupCallUserCell.3
                                @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.WrapSizeDrawable, android.graphics.drawable.Drawable
                                public void draw(Canvas canvas) {
                                    canvas.save();
                                    canvas.translate(AndroidUtilities.dp(-2.0f), AndroidUtilities.dp(0.0f));
                                    super.draw(canvas);
                                    canvas.restore();
                                }
                            };
                        }
                        swapAnimatedEmojiDrawable = this.rightDrawable;
                        drawable = this.premiumDrawable;
                    }
                } else {
                    this.rightDrawable.set(DialogObject.getEmojiStatusDocumentId(this.currentUser.emoji_status), z);
                }
                this.rightDrawable.setColor(Integer.valueOf(Theme.getColor(Theme.key_premiumGradient1)));
                this.nameTextView.setRightDrawable(this.rightDrawable);
                this.avatarImageView.getImageReceiver().setCurrentAccount(accountInstance.getCurrentAccount());
                if (fileLocation == null) {
                    forChat = ImageLocation.getForUser(this.currentUser, 1);
                    this.hasAvatar = forChat != null;
                    backupImageView = this.avatarImageView;
                    avatarDrawable = this.avatarDrawable;
                    obj = this.currentUser;
                    backupImageView.setImage(forChat, "50_50", avatarDrawable, obj);
                }
                this.hasAvatar = true;
                this.avatarImageView.setImage(ImageLocation.getForLocal(fileLocation), "50_50", this.avatarDrawable, (Object) null);
            } else {
                swapAnimatedEmojiDrawable = this.rightDrawable;
                drawable = this.verifiedDrawable;
                if (drawable == null) {
                    drawable = new VerifiedDrawable(getContext());
                }
                this.verifiedDrawable = drawable;
            }
            swapAnimatedEmojiDrawable.set(drawable, z);
            this.rightDrawable.setColor(Integer.valueOf(Theme.getColor(Theme.key_premiumGradient1)));
            this.nameTextView.setRightDrawable(this.rightDrawable);
            this.avatarImageView.getImageReceiver().setCurrentAccount(accountInstance.getCurrentAccount());
            if (fileLocation == null) {
            }
            this.hasAvatar = true;
            this.avatarImageView.setImage(ImageLocation.getForLocal(fileLocation), "50_50", this.avatarDrawable, (Object) null);
        } else {
            this.currentChat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId));
            this.currentUser = null;
            this.avatarDrawable.setInfo(this.accountInstance.getCurrentAccount(), this.currentChat);
            TLRPC.Chat chat = this.currentChat;
            if (chat != null) {
                this.nameTextView.setText(chat.title);
                TLRPC.Chat chat2 = this.currentChat;
                if (chat2.verified) {
                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.rightDrawable;
                    Drawable drawable2 = this.verifiedDrawable;
                    if (drawable2 == null) {
                        drawable2 = new VerifiedDrawable(getContext());
                    }
                    this.verifiedDrawable = drawable2;
                    swapAnimatedEmojiDrawable2.set(drawable2, z);
                } else {
                    long emojiStatusDocumentId = DialogObject.getEmojiStatusDocumentId(chat2.emoji_status);
                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable3 = this.rightDrawable;
                    if (emojiStatusDocumentId != 0) {
                        swapAnimatedEmojiDrawable3.set(DialogObject.getEmojiStatusDocumentId(this.currentChat.emoji_status), z);
                    } else {
                        swapAnimatedEmojiDrawable3.set((Drawable) null, z);
                    }
                }
                this.avatarImageView.getImageReceiver().setCurrentAccount(accountInstance.getCurrentAccount());
                if (fileLocation == null) {
                    forChat = ImageLocation.getForChat(this.currentChat, 1);
                    this.hasAvatar = forChat != null;
                    backupImageView = this.avatarImageView;
                    avatarDrawable = this.avatarDrawable;
                    obj = this.currentChat;
                    backupImageView.setImage(forChat, "50_50", avatarDrawable, obj);
                }
                this.hasAvatar = true;
                this.avatarImageView.setImage(ImageLocation.getForLocal(fileLocation), "50_50", this.avatarDrawable, (Object) null);
            }
        }
        applyParticipantChanges(z);
    }

    public void setDrawAvatar(boolean z) {
        if (this.avatarImageView.getImageReceiver().getVisible() != z) {
            this.avatarImageView.getImageReceiver().setVisible(z, true);
        }
    }

    public void setDrawDivider(boolean z) {
        this.needDivider = z;
        invalidate();
    }

    public void setGrayIconColor(int i, int i2) {
        if (this.grayIconColor != i) {
            if (this.currentIconGray) {
                this.lastMuteColor = Theme.getColor(i);
            }
            this.grayIconColor = i;
        }
        if (this.currentIconGray) {
            this.muteButton.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
            Theme.setSelectorDrawableColor(this.muteButton.getDrawable(), i2 & 620756991, true);
        }
    }

    public void setProgressToAvatarPreview(float f) {
        this.progressToAvatarPreview = f;
        this.nameTextView.setTranslationX((LocaleController.isRTL ? AndroidUtilities.dp(53.0f) : -AndroidUtilities.dp(53.0f)) * f);
        if (!isSelfUser() || f <= 0.0f) {
            this.fullAboutTextView.setVisibility(8);
            int i = 0;
            while (true) {
                SimpleTextView[] simpleTextViewArr = this.statusTextView;
                if (i >= simpleTextViewArr.length) {
                    break;
                }
                if (TextUtils.isEmpty(simpleTextViewArr[4].getText()) || this.statusTextView[4].getLineCount() <= 1) {
                    this.statusTextView[i].setTranslationX((LocaleController.isRTL ? AndroidUtilities.dp(53.0f) : -AndroidUtilities.dp(53.0f)) * f);
                    this.statusTextView[i].setFullLayoutAdditionalWidth(0, 0);
                } else {
                    this.statusTextView[i].setFullLayoutAdditionalWidth(AndroidUtilities.dp(92.0f), LocaleController.isRTL ? AndroidUtilities.dp(48.0f) : AndroidUtilities.dp(53.0f));
                    this.statusTextView[i].setFullAlpha(f);
                    this.statusTextView[i].setTranslationX(0.0f);
                    this.statusTextView[i].invalidate();
                }
                i++;
            }
        } else {
            float f2 = 1.0f - f;
            this.fullAboutTextView.setTranslationX((LocaleController.isRTL ? -AndroidUtilities.dp(53.0f) : AndroidUtilities.dp(53.0f)) * f2);
            this.fullAboutTextView.setVisibility(0);
            this.fullAboutTextView.setAlpha(f);
            this.statusTextView[4].setAlpha(f2);
            SimpleTextView simpleTextView = this.statusTextView[4];
            boolean z = LocaleController.isRTL;
            int dp = AndroidUtilities.dp(53.0f);
            if (!z) {
                dp = -dp;
            }
            simpleTextView.setTranslationX(dp * f);
        }
        this.avatarImageView.setAlpha(f == 0.0f ? 1.0f : 0.0f);
        this.avatarWavesDrawable.setShowWaves(this.isSpeaking && f == 0.0f, this);
        float f3 = 1.0f - f;
        this.muteButton.setAlpha(f3);
        float f4 = (f3 * 0.4f) + 0.6f;
        this.muteButton.setScaleX(f4);
        this.muteButton.setScaleY(f4);
        invalidate();
    }

    public void setUploadProgress(float f, boolean z) {
        RadialProgressView radialProgressView;
        boolean z2;
        this.avatarProgressView.setProgress(f);
        if (f < 1.0f) {
            radialProgressView = this.avatarProgressView;
            z2 = true;
        } else {
            radialProgressView = this.avatarProgressView;
            z2 = false;
        }
        AndroidUtilities.updateViewVisibilityAnimated(radialProgressView, z2, 1.0f, z);
    }
}
