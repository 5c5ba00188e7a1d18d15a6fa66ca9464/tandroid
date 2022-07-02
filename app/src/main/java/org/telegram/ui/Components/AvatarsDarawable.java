package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import java.util.Random;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCallUserCell;
/* loaded from: classes3.dex */
public class AvatarsDarawable {
    boolean centered;
    public int count;
    int currentStyle;
    public int height;
    private boolean isInCall;
    private int overrideSize;
    View parent;
    private boolean transitionInProgress;
    ValueAnimator transitionProgressAnimator;
    boolean updateAfterTransition;
    Runnable updateDelegate;
    boolean wasDraw;
    public int width;
    DrawingState[] currentStates = new DrawingState[3];
    DrawingState[] animatingStates = new DrawingState[3];
    float transitionProgress = 1.0f;
    private Paint paint = new Paint(1);
    private Paint xRefP = new Paint(1);
    private float overrideAlpha = 1.0f;
    public long transitionDuration = 220;
    Random random = new Random();

    public void commitTransition(boolean z) {
        commitTransition(z, true);
    }

    public void setTransitionProgress(float f) {
        if (!this.transitionInProgress || this.transitionProgress == f) {
            return;
        }
        this.transitionProgress = f;
        if (f != 1.0f) {
            return;
        }
        swapStates();
        this.transitionInProgress = false;
    }

    public void commitTransition(boolean z, boolean z2) {
        boolean z3;
        if (!this.wasDraw || !z) {
            this.transitionProgress = 1.0f;
            swapStates();
            return;
        }
        DrawingState[] drawingStateArr = new DrawingState[3];
        boolean z4 = false;
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr2 = this.currentStates;
            drawingStateArr[i] = drawingStateArr2[i];
            if (drawingStateArr2[i].id != this.animatingStates[i].id) {
                z4 = true;
            } else {
                this.currentStates[i].lastSpeakTime = this.animatingStates[i].lastSpeakTime;
            }
        }
        if (!z4) {
            this.transitionProgress = 1.0f;
            return;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= 3) {
                    z3 = false;
                    break;
                } else if (this.currentStates[i3].id == this.animatingStates[i2].id) {
                    drawingStateArr[i3] = null;
                    if (i2 == i3) {
                        this.animatingStates[i2].animationType = -1;
                        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable = this.animatingStates[i2].wavesDrawable;
                        this.animatingStates[i2].wavesDrawable = this.currentStates[i2].wavesDrawable;
                        this.currentStates[i2].wavesDrawable = avatarWavesDrawable;
                    } else {
                        this.animatingStates[i2].animationType = 2;
                        this.animatingStates[i2].moveFromIndex = i3;
                    }
                    z3 = true;
                } else {
                    i3++;
                }
            }
            if (!z3) {
                this.animatingStates[i2].animationType = 0;
            }
        }
        for (int i4 = 0; i4 < 3; i4++) {
            if (drawingStateArr[i4] != null) {
                drawingStateArr[i4].animationType = 1;
            }
        }
        ValueAnimator valueAnimator = this.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            if (this.transitionInProgress) {
                swapStates();
                this.transitionInProgress = false;
            }
        }
        this.transitionProgress = 0.0f;
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.transitionProgressAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AvatarsDarawable$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    AvatarsDarawable.this.lambda$commitTransition$0(valueAnimator2);
                }
            });
            this.transitionProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AvatarsDarawable.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AvatarsDarawable avatarsDarawable = AvatarsDarawable.this;
                    if (avatarsDarawable.transitionProgressAnimator != null) {
                        avatarsDarawable.transitionProgress = 1.0f;
                        avatarsDarawable.swapStates();
                        AvatarsDarawable avatarsDarawable2 = AvatarsDarawable.this;
                        if (avatarsDarawable2.updateAfterTransition) {
                            avatarsDarawable2.updateAfterTransition = false;
                            Runnable runnable = avatarsDarawable2.updateDelegate;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                        AvatarsDarawable.this.invalidate();
                    }
                    AvatarsDarawable.this.transitionProgressAnimator = null;
                }
            });
            this.transitionProgressAnimator.setDuration(this.transitionDuration);
            this.transitionProgressAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.transitionProgressAnimator.start();
        } else {
            this.transitionInProgress = true;
        }
        invalidate();
    }

    public /* synthetic */ void lambda$commitTransition$0(ValueAnimator valueAnimator) {
        this.transitionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void swapStates() {
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr = this.currentStates;
            DrawingState drawingState = drawingStateArr[i];
            DrawingState[] drawingStateArr2 = this.animatingStates;
            drawingStateArr[i] = drawingStateArr2[i];
            drawingStateArr2[i] = drawingState;
        }
    }

    public void updateAfterTransitionEnd() {
        this.updateAfterTransition = true;
    }

    public void setDelegate(Runnable runnable) {
        this.updateDelegate = runnable;
    }

    public void setStyle(int i) {
        this.currentStyle = i;
        invalidate();
    }

    public void invalidate() {
        View view = this.parent;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setSize(int i) {
        this.overrideSize = i;
    }

    public void animateFromState(AvatarsDarawable avatarsDarawable, int i, boolean z) {
        ValueAnimator valueAnimator = avatarsDarawable.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            if (this.transitionInProgress) {
                this.transitionInProgress = false;
                swapStates();
            }
        }
        TLObject[] tLObjectArr = new TLObject[3];
        for (int i2 = 0; i2 < 3; i2++) {
            tLObjectArr[i2] = this.currentStates[i2].object;
            setObject(i2, i, avatarsDarawable.currentStates[i2].object);
        }
        commitTransition(false);
        for (int i3 = 0; i3 < 3; i3++) {
            setObject(i3, i, tLObjectArr[i3]);
        }
        this.wasDraw = true;
        commitTransition(true, z);
    }

    public void setAlpha(float f) {
        this.overrideAlpha = f;
    }

    /* loaded from: classes3.dex */
    public static class DrawingState {
        private int animationType;
        private AvatarDrawable avatarDrawable;
        private long id;
        private ImageReceiver imageReceiver;
        private long lastSpeakTime;
        private long lastUpdateTime;
        private int moveFromIndex;
        private TLObject object;
        TLRPC$TL_groupCallParticipant participant;
        private GroupCallUserCell.AvatarWavesDrawable wavesDrawable;

        private DrawingState() {
        }
    }

    public AvatarsDarawable(View view, boolean z) {
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.parent = view;
        for (int i = 0; i < 3; i++) {
            this.currentStates[i] = new DrawingState();
            this.currentStates[i].imageReceiver = new ImageReceiver(view);
            this.currentStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.currentStates[i].avatarDrawable = new AvatarDrawable();
            this.currentStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            this.animatingStates[i] = new DrawingState();
            this.animatingStates[i].imageReceiver = new ImageReceiver(view);
            this.animatingStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.animatingStates[i].avatarDrawable = new AvatarDrawable();
            this.animatingStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        }
        this.isInCall = z;
        this.xRefP.setColor(0);
        this.xRefP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setObject(int i, int i2, TLObject tLObject) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        this.animatingStates[i].id = 0L;
        DrawingState[] drawingStateArr = this.animatingStates;
        TLRPC$User tLRPC$User = null;
        drawingStateArr[i].participant = null;
        if (tLObject == null) {
            drawingStateArr[i].imageReceiver.setImageBitmap((Drawable) null);
            invalidate();
            return;
        }
        drawingStateArr[i].lastSpeakTime = -1L;
        this.animatingStates[i].object = tLObject;
        if (tLObject instanceof TLRPC$TL_groupCallParticipant) {
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = (TLRPC$TL_groupCallParticipant) tLObject;
            this.animatingStates[i].participant = tLRPC$TL_groupCallParticipant;
            long peerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
            if (DialogObject.isUserDialog(peerId)) {
                TLRPC$User user = MessagesController.getInstance(i2).getUser(Long.valueOf(peerId));
                this.animatingStates[i].avatarDrawable.setInfo(user);
                tLRPC$User = user;
                tLRPC$Chat2 = null;
            } else {
                tLRPC$Chat2 = MessagesController.getInstance(i2).getChat(Long.valueOf(-peerId));
                this.animatingStates[i].avatarDrawable.setInfo(tLRPC$Chat2);
            }
            if (this.currentStyle != 4) {
                this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.active_date;
            } else if (peerId == AccountInstance.getInstance(i2).getUserConfig().getClientUserId()) {
                this.animatingStates[i].lastSpeakTime = 0L;
            } else if (this.isInCall) {
                this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.lastActiveDate;
            } else {
                this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.active_date;
            }
            this.animatingStates[i].id = peerId;
            tLRPC$Chat = tLRPC$Chat2;
        } else if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User2 = (TLRPC$User) tLObject;
            this.animatingStates[i].avatarDrawable.setInfo(tLRPC$User2);
            this.animatingStates[i].id = tLRPC$User2.id;
            tLRPC$User = tLRPC$User2;
            tLRPC$Chat = null;
        } else {
            tLRPC$Chat = (TLRPC$Chat) tLObject;
            this.animatingStates[i].avatarDrawable.setInfo(tLRPC$Chat);
            this.animatingStates[i].id = -tLRPC$Chat.id;
        }
        if (tLRPC$User != null) {
            this.animatingStates[i].imageReceiver.setForUserOrChat(tLRPC$User, this.animatingStates[i].avatarDrawable);
        } else {
            this.animatingStates[i].imageReceiver.setForUserOrChat(tLRPC$Chat, this.animatingStates[i].avatarDrawable);
        }
        int i3 = this.currentStyle;
        this.animatingStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(i3 == 4 || i3 == 10 ? 16.0f : 12.0f));
        float size = getSize();
        this.animatingStates[i].imageReceiver.setImageCoords(0.0f, 0.0f, size, size);
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:148:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x04e8  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0505 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int dp;
        int i;
        boolean z;
        float f;
        int length;
        float f2;
        int i2;
        int i3;
        boolean z2 = true;
        this.wasDraw = true;
        int i4 = this.currentStyle;
        int i5 = 4;
        int i6 = 10;
        boolean z3 = i4 == 4 || i4 == 10;
        int size = getSize();
        int i7 = 11;
        if (this.currentStyle == 11) {
            dp = AndroidUtilities.dp(12.0f);
        } else {
            int i8 = this.overrideSize;
            if (i8 != 0) {
                dp = (int) (i8 * 0.8f);
            } else {
                dp = AndroidUtilities.dp(z3 ? 24.0f : 20.0f);
            }
        }
        int i9 = dp;
        int i10 = 0;
        for (int i11 = 0; i11 < 3; i11++) {
            if (this.currentStates[i11].id != 0) {
                i10++;
            }
        }
        int i12 = this.currentStyle;
        int dp2 = (i12 == 0 || i12 == 10 || i12 == 11) ? 0 : AndroidUtilities.dp(10.0f);
        int dp3 = this.centered ? ((this.width - (i10 * i9)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / 2 : dp2;
        boolean z4 = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        int i13 = this.currentStyle;
        if (i13 == 4) {
            this.paint.setColor(Theme.getColor("inappPlayerBackground"));
        } else if (i13 != 3) {
            this.paint.setColor(Theme.getColor(z4 ? "returnToCallMutedBackground" : "returnToCallBackground"));
        }
        int i14 = 0;
        for (int i15 = 0; i15 < 3; i15++) {
            if (this.animatingStates[i15].id != 0) {
                i14++;
            }
        }
        int i16 = this.currentStyle;
        boolean z5 = i16 == 0 || i16 == 1 || i16 == 3 || i16 == 4 || i16 == 5 || i16 == 10 || i16 == 11;
        if (z5) {
            float dp4 = i16 == 10 ? AndroidUtilities.dp(16.0f) : 0.0f;
            float f3 = -dp4;
            i = 2;
            canvas.saveLayerAlpha(f3, f3, this.width + dp4, this.height + dp4, 255, 31);
        } else {
            i = 2;
        }
        int i17 = 2;
        while (i17 >= 0) {
            int i18 = 0;
            while (i18 < i) {
                if (i18 != 0 || this.transitionProgress != 1.0f) {
                    DrawingState[] drawingStateArr = i18 == 0 ? this.animatingStates : this.currentStates;
                    if (i18 != z2 || this.transitionProgress == 1.0f || drawingStateArr[i17].animationType == z2) {
                        ImageReceiver imageReceiver = drawingStateArr[i17].imageReceiver;
                        if (imageReceiver.hasImageSet()) {
                            if (i18 == 0) {
                                imageReceiver.setImageX((this.centered ? ((this.width - (i14 * i9)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2) + (i9 * i17));
                            } else {
                                imageReceiver.setImageX(dp3 + (i9 * i17));
                            }
                            int i19 = this.currentStyle;
                            if (i19 == 0 || i19 == i6 || i19 == i7) {
                                imageReceiver.setImageY((this.height - size) / 2.0f);
                            } else {
                                imageReceiver.setImageY(AndroidUtilities.dp(i19 == i5 ? 8.0f : 6.0f));
                            }
                            if (this.transitionProgress != 1.0f) {
                                if (drawingStateArr[i17].animationType != z2) {
                                    if (drawingStateArr[i17].animationType != 0) {
                                        if (drawingStateArr[i17].animationType != i) {
                                            if (drawingStateArr[i17].animationType == -1 && this.centered) {
                                                int dp5 = (((this.width - (i14 * i9)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i) + (i9 * i17);
                                                float f4 = this.transitionProgress;
                                                imageReceiver.setImageX((int) ((dp5 * f4) + ((dp3 + i2) * (1.0f - f4))));
                                            }
                                        } else {
                                            int dp6 = this.centered ? ((this.width - (i14 * i9)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2;
                                            float f5 = this.transitionProgress;
                                            imageReceiver.setImageX((int) (((dp6 + (i9 * i17)) * f5) + ((dp3 + (drawingStateArr[i17].moveFromIndex * i9)) * (1.0f - f5))));
                                        }
                                    } else {
                                        canvas.save();
                                        float f6 = this.transitionProgress;
                                        canvas.scale(f6, f6, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                        f = this.transitionProgress;
                                    }
                                } else {
                                    canvas.save();
                                    float f7 = this.transitionProgress;
                                    canvas.scale(1.0f - f7, 1.0f - f7, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                    f = 1.0f - this.transitionProgress;
                                }
                                z = true;
                                float f8 = f * this.overrideAlpha;
                                length = drawingStateArr.length;
                                i3 = z2 ? 1 : 0;
                                int i20 = z2 ? 1 : 0;
                                int i21 = z2 ? 1 : 0;
                                if (i17 != length - i3) {
                                    int i22 = this.currentStyle;
                                    if (i22 == z2 || i22 == 3 || i22 == 5) {
                                        canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                        if (drawingStateArr[i17].wavesDrawable == null) {
                                            if (this.currentStyle == 5) {
                                                drawingStateArr[i17].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(16.0f));
                                            } else {
                                                drawingStateArr[i17].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                            }
                                        }
                                        if (this.currentStyle == 5) {
                                            drawingStateArr[i17].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_speakingText"), (int) (f8 * 76.5f)));
                                        }
                                        if (drawingStateArr[i17].participant == null || drawingStateArr[i17].participant.amplitude <= 0.0f) {
                                            drawingStateArr[i17].wavesDrawable.setShowWaves(false, this.parent);
                                        } else {
                                            drawingStateArr[i17].wavesDrawable.setShowWaves(true, this.parent);
                                            drawingStateArr[i17].wavesDrawable.setAmplitude(drawingStateArr[i17].participant.amplitude * 15.0f);
                                        }
                                        if (this.currentStyle == 5 && SystemClock.uptimeMillis() - drawingStateArr[i17].participant.lastSpeakTime > 500) {
                                            this.updateDelegate.run();
                                        }
                                        drawingStateArr[i17].wavesDrawable.update();
                                        if (this.currentStyle == 5) {
                                            drawingStateArr[i17].wavesDrawable.draw(canvas, imageReceiver.getCenterX(), imageReceiver.getCenterY(), this.parent);
                                            invalidate();
                                        }
                                        f2 = drawingStateArr[i17].wavesDrawable.getAvatarScale();
                                    } else if (i22 == i5 || i22 == 10) {
                                        canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), AndroidUtilities.dp(17.0f), this.xRefP);
                                        if (drawingStateArr[i17].wavesDrawable == null) {
                                            drawingStateArr[i17].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                        }
                                        if (this.currentStyle == 10) {
                                            drawingStateArr[i17].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_speakingText"), (int) (f8 * 76.5f)));
                                        } else {
                                            drawingStateArr[i17].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_listeningText"), (int) (f8 * 76.5f)));
                                        }
                                        long currentTimeMillis = System.currentTimeMillis();
                                        if (currentTimeMillis - drawingStateArr[i17].lastUpdateTime > 100) {
                                            drawingStateArr[i17].lastUpdateTime = currentTimeMillis;
                                            if (this.currentStyle != 10) {
                                                if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - drawingStateArr[i17].lastSpeakTime <= 5) {
                                                    drawingStateArr[i17].wavesDrawable.setShowWaves(true, this.parent);
                                                    drawingStateArr[i17].wavesDrawable.setAmplitude(this.random.nextInt() % 100);
                                                } else {
                                                    drawingStateArr[i17].wavesDrawable.setShowWaves(false, this.parent);
                                                    drawingStateArr[i17].wavesDrawable.setAmplitude(0.0d);
                                                }
                                            } else if (drawingStateArr[i17].participant == null || drawingStateArr[i17].participant.amplitude <= 0.0f) {
                                                drawingStateArr[i17].wavesDrawable.setShowWaves(false, this.parent);
                                            } else {
                                                drawingStateArr[i17].wavesDrawable.setShowWaves(z2, this.parent);
                                                drawingStateArr[i17].wavesDrawable.setAmplitude(drawingStateArr[i17].participant.amplitude * 15.0f);
                                            }
                                        }
                                        drawingStateArr[i17].wavesDrawable.update();
                                        drawingStateArr[i17].wavesDrawable.draw(canvas, imageReceiver.getCenterX(), imageReceiver.getCenterY(), this.parent);
                                        f2 = drawingStateArr[i17].wavesDrawable.getAvatarScale();
                                    } else {
                                        float size2 = (getSize() / 2.0f) + AndroidUtilities.dp(2.0f);
                                        if (z5) {
                                            canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size2, this.xRefP);
                                        } else {
                                            int alpha = this.paint.getAlpha();
                                            if (f8 != 1.0f) {
                                                this.paint.setAlpha((int) (alpha * f8));
                                            }
                                            canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size2, this.paint);
                                            if (f8 != 1.0f) {
                                                this.paint.setAlpha(alpha);
                                            }
                                        }
                                    }
                                    imageReceiver.setAlpha(f8);
                                    if (f2 != 1.0f) {
                                        canvas.save();
                                        canvas.scale(f2, f2, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                        imageReceiver.draw(canvas);
                                        canvas.restore();
                                    } else {
                                        imageReceiver.draw(canvas);
                                    }
                                    if (z) {
                                        canvas.restore();
                                    }
                                    i18++;
                                    z2 = true;
                                    i5 = 4;
                                    i6 = 10;
                                    i = 2;
                                    i7 = 11;
                                }
                                f2 = 1.0f;
                                imageReceiver.setAlpha(f8);
                                if (f2 != 1.0f) {
                                }
                                if (z) {
                                }
                                i18++;
                                z2 = true;
                                i5 = 4;
                                i6 = 10;
                                i = 2;
                                i7 = 11;
                            }
                            f = 1.0f;
                            z = false;
                            float f82 = f * this.overrideAlpha;
                            length = drawingStateArr.length;
                            i3 = z2 ? 1 : 0;
                            int i202 = z2 ? 1 : 0;
                            int i212 = z2 ? 1 : 0;
                            if (i17 != length - i3) {
                            }
                            f2 = 1.0f;
                            imageReceiver.setAlpha(f82);
                            if (f2 != 1.0f) {
                            }
                            if (z) {
                            }
                            i18++;
                            z2 = true;
                            i5 = 4;
                            i6 = 10;
                            i = 2;
                            i7 = 11;
                        }
                    }
                }
                i18++;
                z2 = true;
                i5 = 4;
                i6 = 10;
                i = 2;
                i7 = 11;
            }
            i17--;
            z2 = true;
            i5 = 4;
            i6 = 10;
            i = 2;
            i7 = 11;
        }
        if (z5) {
            canvas.restore();
        }
    }

    private int getSize() {
        int i = this.overrideSize;
        if (i != 0) {
            return i;
        }
        int i2 = this.currentStyle;
        return AndroidUtilities.dp(i2 == 4 || i2 == 10 ? 32.0f : 24.0f);
    }

    public void onDetachedFromWindow() {
        this.wasDraw = false;
        for (int i = 0; i < 3; i++) {
            this.currentStates[i].imageReceiver.onDetachedFromWindow();
            this.animatingStates[i].imageReceiver.onDetachedFromWindow();
        }
        if (this.currentStyle == 3) {
            Theme.getFragmentContextViewWavesDrawable().setAmplitude(0.0f);
        }
    }

    public void onAttachedToWindow() {
        for (int i = 0; i < 3; i++) {
            this.currentStates[i].imageReceiver.onAttachedToWindow();
            this.animatingStates[i].imageReceiver.onAttachedToWindow();
        }
    }

    public void setCentered(boolean z) {
        this.centered = z;
    }

    public void setCount(int i) {
        this.count = i;
        View view = this.parent;
        if (view != null) {
            view.requestLayout();
        }
    }

    public void reset() {
        for (int i = 0; i < this.animatingStates.length; i++) {
            setObject(0, 0, null);
        }
    }
}
