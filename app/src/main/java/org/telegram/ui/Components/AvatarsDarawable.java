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
import android.view.animation.Interpolator;
import androidx.core.graphics.ColorUtils;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCallUserCell;
/* loaded from: classes5.dex */
public class AvatarsDarawable {
    public static final int STYLE_GROUP_CALL_TOOLTIP = 10;
    public static final int STYLE_MESSAGE_SEEN = 11;
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
    public Interpolator transitionInterpolator = CubicBezierInterpolator.DEFAULT;
    Random random = new Random();

    public void commitTransition(boolean animated) {
        commitTransition(animated, true);
    }

    public void setTransitionProgress(float transitionProgress) {
        if (this.transitionInProgress && this.transitionProgress != transitionProgress) {
            this.transitionProgress = transitionProgress;
            if (transitionProgress == 1.0f) {
                swapStates();
                this.transitionInProgress = false;
            }
        }
    }

    public void commitTransition(boolean animated, boolean createAnimator) {
        if (!this.wasDraw || !animated) {
            this.transitionProgress = 1.0f;
            swapStates();
            return;
        }
        DrawingState[] removedStates = new DrawingState[3];
        boolean changed = false;
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr = this.currentStates;
            removedStates[i] = drawingStateArr[i];
            if (drawingStateArr[i].id != this.animatingStates[i].id) {
                changed = true;
            } else {
                this.currentStates[i].lastSpeakTime = this.animatingStates[i].lastSpeakTime;
            }
        }
        if (!changed) {
            this.transitionProgress = 1.0f;
            return;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            boolean found = false;
            int j = 0;
            while (true) {
                if (j >= 3) {
                    break;
                } else if (this.currentStates[j].id != this.animatingStates[i2].id) {
                    j++;
                } else {
                    found = true;
                    removedStates[j] = null;
                    if (i2 == j) {
                        this.animatingStates[i2].animationType = -1;
                        GroupCallUserCell.AvatarWavesDrawable wavesDrawable = this.animatingStates[i2].wavesDrawable;
                        this.animatingStates[i2].wavesDrawable = this.currentStates[i2].wavesDrawable;
                        this.currentStates[i2].wavesDrawable = wavesDrawable;
                    } else {
                        this.animatingStates[i2].animationType = 2;
                        this.animatingStates[i2].moveFromIndex = j;
                    }
                }
            }
            if (!found) {
                this.animatingStates[i2].animationType = 0;
            }
        }
        for (int i3 = 0; i3 < 3; i3++) {
            if (removedStates[i3] != null) {
                removedStates[i3].animationType = 1;
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
        if (createAnimator) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.transitionProgressAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AvatarsDarawable$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    AvatarsDarawable.this.m2213x32cd51d(valueAnimator2);
                }
            });
            this.transitionProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AvatarsDarawable.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    if (AvatarsDarawable.this.transitionProgressAnimator != null) {
                        AvatarsDarawable.this.transitionProgress = 1.0f;
                        AvatarsDarawable.this.swapStates();
                        if (AvatarsDarawable.this.updateAfterTransition) {
                            AvatarsDarawable.this.updateAfterTransition = false;
                            if (AvatarsDarawable.this.updateDelegate != null) {
                                AvatarsDarawable.this.updateDelegate.run();
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

    /* renamed from: lambda$commitTransition$0$org-telegram-ui-Components-AvatarsDarawable */
    public /* synthetic */ void m2213x32cd51d(ValueAnimator valueAnimator) {
        this.transitionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void swapStates() {
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr = this.currentStates;
            DrawingState state = drawingStateArr[i];
            DrawingState[] drawingStateArr2 = this.animatingStates;
            drawingStateArr[i] = drawingStateArr2[i];
            drawingStateArr2[i] = state;
        }
    }

    public void updateAfterTransitionEnd() {
        this.updateAfterTransition = true;
    }

    public void setDelegate(Runnable delegate) {
        this.updateDelegate = delegate;
    }

    public void setStyle(int currentStyle) {
        this.currentStyle = currentStyle;
        invalidate();
    }

    public void invalidate() {
        View view = this.parent;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setSize(int size) {
        this.overrideSize = size;
    }

    public void animateFromState(AvatarsDarawable avatarsDarawable, int currentAccount, boolean createAnimator) {
        ValueAnimator valueAnimator = avatarsDarawable.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            if (this.transitionInProgress) {
                this.transitionInProgress = false;
                swapStates();
            }
        }
        TLObject[] objects = new TLObject[3];
        for (int i = 0; i < 3; i++) {
            objects[i] = this.currentStates[i].object;
            setObject(i, currentAccount, avatarsDarawable.currentStates[i].object);
        }
        commitTransition(false);
        for (int i2 = 0; i2 < 3; i2++) {
            setObject(i2, currentAccount, objects[i2]);
        }
        this.wasDraw = true;
        commitTransition(true, createAnimator);
    }

    public void setAlpha(float alpha) {
        this.overrideAlpha = alpha;
    }

    /* loaded from: classes5.dex */
    public static class DrawingState {
        public static final int ANIMATION_TYPE_IN = 0;
        public static final int ANIMATION_TYPE_MOVE = 2;
        public static final int ANIMATION_TYPE_NONE = -1;
        public static final int ANIMATION_TYPE_OUT = 1;
        private int animationType;
        private AvatarDrawable avatarDrawable;
        private long id;
        private ImageReceiver imageReceiver;
        private long lastSpeakTime;
        private long lastUpdateTime;
        private int moveFromIndex;
        private TLObject object;
        TLRPC.TL_groupCallParticipant participant;
        private GroupCallUserCell.AvatarWavesDrawable wavesDrawable;

        private DrawingState() {
        }
    }

    public AvatarsDarawable(View parent, boolean inCall) {
        this.parent = parent;
        for (int a = 0; a < 3; a++) {
            this.currentStates[a] = new DrawingState();
            this.currentStates[a].imageReceiver = new ImageReceiver(parent);
            this.currentStates[a].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.currentStates[a].avatarDrawable = new AvatarDrawable();
            this.currentStates[a].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            this.animatingStates[a] = new DrawingState();
            this.animatingStates[a].imageReceiver = new ImageReceiver(parent);
            this.animatingStates[a].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.animatingStates[a].avatarDrawable = new AvatarDrawable();
            this.animatingStates[a].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        }
        this.isInCall = inCall;
        this.xRefP.setColor(0);
        this.xRefP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setObject(int index, int account, TLObject object) {
        this.animatingStates[index].id = 0L;
        this.animatingStates[index].participant = null;
        if (object == null) {
            this.animatingStates[index].imageReceiver.setImageBitmap((Drawable) null);
            invalidate();
            return;
        }
        TLRPC.User currentUser = null;
        TLRPC.Chat currentChat = null;
        this.animatingStates[index].lastSpeakTime = -1L;
        this.animatingStates[index].object = object;
        if (object instanceof TLRPC.TL_groupCallParticipant) {
            TLRPC.TL_groupCallParticipant participant = (TLRPC.TL_groupCallParticipant) object;
            this.animatingStates[index].participant = participant;
            long id = MessageObject.getPeerId(participant.peer);
            if (DialogObject.isUserDialog(id)) {
                currentUser = MessagesController.getInstance(account).getUser(Long.valueOf(id));
                this.animatingStates[index].avatarDrawable.setInfo(currentUser);
            } else {
                currentChat = MessagesController.getInstance(account).getChat(Long.valueOf(-id));
                this.animatingStates[index].avatarDrawable.setInfo(currentChat);
            }
            if (this.currentStyle != 4) {
                this.animatingStates[index].lastSpeakTime = participant.active_date;
            } else if (id == AccountInstance.getInstance(account).getUserConfig().getClientUserId()) {
                this.animatingStates[index].lastSpeakTime = 0L;
            } else if (this.isInCall) {
                this.animatingStates[index].lastSpeakTime = participant.lastActiveDate;
            } else {
                this.animatingStates[index].lastSpeakTime = participant.active_date;
            }
            this.animatingStates[index].id = id;
        } else if (object instanceof TLRPC.User) {
            currentUser = (TLRPC.User) object;
            this.animatingStates[index].avatarDrawable.setInfo(currentUser);
            this.animatingStates[index].id = currentUser.id;
        } else {
            currentChat = (TLRPC.Chat) object;
            this.animatingStates[index].avatarDrawable.setInfo(currentChat);
            this.animatingStates[index].id = -currentChat.id;
        }
        if (currentUser != null) {
            this.animatingStates[index].imageReceiver.setForUserOrChat(currentUser, this.animatingStates[index].avatarDrawable);
        } else {
            this.animatingStates[index].imageReceiver.setForUserOrChat(currentChat, this.animatingStates[index].avatarDrawable);
        }
        int i = this.currentStyle;
        boolean bigAvatars = i == 4 || i == 10;
        this.animatingStates[index].imageReceiver.setRoundRadius(AndroidUtilities.dp(bigAvatars ? 16.0f : 12.0f));
        int size = getSize();
        this.animatingStates[index].imageReceiver.setImageCoords(0.0f, 0.0f, size, size);
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:220:0x052c  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0541  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0546  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0549 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int toAdd;
        int i;
        float avatarScale;
        float avatarScale2;
        int i2 = 1;
        this.wasDraw = true;
        int i3 = this.currentStyle;
        int i4 = 4;
        int i5 = 10;
        boolean bigAvatars = i3 == 4 || i3 == 10;
        int size = getSize();
        int i6 = 11;
        if (this.currentStyle == 11) {
            toAdd = AndroidUtilities.dp(12.0f);
        } else {
            int toAdd2 = this.overrideSize;
            if (toAdd2 != 0) {
                toAdd = (int) (toAdd2 * 0.8f);
            } else {
                toAdd = AndroidUtilities.dp(bigAvatars ? 24.0f : 20.0f);
            }
        }
        int drawCount = 0;
        for (int i7 = 0; i7 < 3; i7++) {
            if (this.currentStates[i7].id != 0) {
                drawCount++;
            }
        }
        int i8 = this.currentStyle;
        int startPadding = (i8 == 0 || i8 == 10 || i8 == 11) ? 0 : AndroidUtilities.dp(10.0f);
        int ax = this.centered ? ((this.width - (drawCount * toAdd)) - AndroidUtilities.dp(bigAvatars ? 8.0f : 4.0f)) / 2 : startPadding;
        boolean isMuted = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        int i9 = this.currentStyle;
        if (i9 == 4) {
            this.paint.setColor(Theme.getColor(Theme.key_inappPlayerBackground));
        } else if (i9 != 3) {
            this.paint.setColor(Theme.getColor(isMuted ? Theme.key_returnToCallMutedBackground : Theme.key_returnToCallBackground));
        }
        int animateToDrawCount = 0;
        for (int i10 = 0; i10 < 3; i10++) {
            if (this.animatingStates[i10].id != 0) {
                animateToDrawCount++;
            }
        }
        int i11 = this.currentStyle;
        boolean useAlphaLayer = i11 == 0 || i11 == 1 || i11 == 3 || i11 == 4 || i11 == 5 || i11 == 10 || i11 == 11;
        if (!useAlphaLayer) {
            i = 2;
        } else {
            float padding = i11 == 10 ? AndroidUtilities.dp(16.0f) : 0.0f;
            i = 2;
            canvas.saveLayerAlpha(-padding, -padding, this.width + padding, this.height + padding, 255, 31);
        }
        int a = 2;
        while (a >= 0) {
            int k = 0;
            while (k < i) {
                if (k != 0 || this.transitionProgress != 1.0f) {
                    DrawingState[] states = k == 0 ? this.animatingStates : this.currentStates;
                    if (k != i2 || this.transitionProgress == 1.0f || states[a].animationType == i2) {
                        ImageReceiver imageReceiver = states[a].imageReceiver;
                        if (imageReceiver.hasImageSet()) {
                            if (k == 0) {
                                int toAx = this.centered ? ((this.width - (animateToDrawCount * toAdd)) - AndroidUtilities.dp(bigAvatars ? 8.0f : 4.0f)) / i : startPadding;
                                imageReceiver.setImageX((toAdd * a) + toAx);
                            } else {
                                imageReceiver.setImageX(ax + (toAdd * a));
                            }
                            int i12 = this.currentStyle;
                            if (i12 == 0 || i12 == i5 || i12 == i6) {
                                imageReceiver.setImageY((this.height - size) / 2.0f);
                            } else {
                                imageReceiver.setImageY(AndroidUtilities.dp(i12 == i4 ? 8.0f : 6.0f));
                            }
                            boolean needRestore = false;
                            float alpha = 1.0f;
                            if (this.transitionProgress != 1.0f) {
                                if (states[a].animationType != i2) {
                                    if (states[a].animationType != 0) {
                                        if (states[a].animationType != i) {
                                            if (states[a].animationType == -1 && this.centered) {
                                                int toAx2 = ((this.width - (animateToDrawCount * toAdd)) - AndroidUtilities.dp(bigAvatars ? 8.0f : 4.0f)) / 2;
                                                int toX = (toAdd * a) + toAx2;
                                                int fromX = ax + (toAdd * a);
                                                float f = this.transitionProgress;
                                                imageReceiver.setImageX((int) ((toX * f) + (fromX * (1.0f - f))));
                                            }
                                        } else {
                                            int toAx3 = this.centered ? ((this.width - (animateToDrawCount * toAdd)) - AndroidUtilities.dp(bigAvatars ? 8.0f : 4.0f)) / i : startPadding;
                                            int toX2 = (toAdd * a) + toAx3;
                                            int fromX2 = ax + (states[a].moveFromIndex * toAdd);
                                            float f2 = this.transitionProgress;
                                            imageReceiver.setImageX((int) ((toX2 * f2) + (fromX2 * (1.0f - f2))));
                                        }
                                    } else {
                                        canvas.save();
                                        float f3 = this.transitionProgress;
                                        canvas.scale(f3, f3, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                        needRestore = true;
                                        alpha = this.transitionProgress;
                                    }
                                } else {
                                    canvas.save();
                                    float f4 = this.transitionProgress;
                                    canvas.scale(1.0f - f4, 1.0f - f4, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                    needRestore = true;
                                    alpha = 1.0f - this.transitionProgress;
                                }
                            }
                            float alpha2 = this.overrideAlpha * alpha;
                            if (a == states.length - 1) {
                                avatarScale2 = 1.0f;
                            } else {
                                int i13 = this.currentStyle;
                                if (i13 != 1 && i13 != 3) {
                                    if (i13 != 5) {
                                        if (i13 == 4 || i13 == 10) {
                                            canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), AndroidUtilities.dp(17.0f), this.xRefP);
                                            if (states[a].wavesDrawable == null) {
                                                states[a].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                            }
                                            if (this.currentStyle == 10) {
                                                states[a].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_speakingText), (int) (alpha2 * 76.5f)));
                                            } else {
                                                states[a].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_listeningText), (int) (alpha2 * 76.5f)));
                                            }
                                            long currentTime = System.currentTimeMillis();
                                            if (currentTime - states[a].lastUpdateTime > 100) {
                                                states[a].lastUpdateTime = currentTime;
                                                if (this.currentStyle != 10) {
                                                    if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - states[a].lastSpeakTime <= 5) {
                                                        states[a].wavesDrawable.setShowWaves(true, this.parent);
                                                        states[a].wavesDrawable.setAmplitude(this.random.nextInt() % 100);
                                                    } else {
                                                        states[a].wavesDrawable.setShowWaves(false, this.parent);
                                                        states[a].wavesDrawable.setAmplitude(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                                                    }
                                                } else if (states[a].participant == null || states[a].participant.amplitude <= 0.0f) {
                                                    states[a].wavesDrawable.setShowWaves(false, this.parent);
                                                } else {
                                                    states[a].wavesDrawable.setShowWaves(true, this.parent);
                                                    float amplitude = states[a].participant.amplitude * 15.0f;
                                                    states[a].wavesDrawable.setAmplitude(amplitude);
                                                }
                                            }
                                            states[a].wavesDrawable.update();
                                            states[a].wavesDrawable.draw(canvas, imageReceiver.getCenterX(), imageReceiver.getCenterY(), this.parent);
                                            avatarScale = states[a].wavesDrawable.getAvatarScale();
                                            imageReceiver.setAlpha(alpha2);
                                            if (avatarScale != 1.0f) {
                                                canvas.save();
                                                canvas.scale(avatarScale, avatarScale, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                                imageReceiver.draw(canvas);
                                                canvas.restore();
                                            } else {
                                                imageReceiver.draw(canvas);
                                            }
                                            if (needRestore) {
                                                canvas.restore();
                                            }
                                        } else {
                                            float rad = (getSize() / 2.0f) + AndroidUtilities.dp(2.0f);
                                            if (useAlphaLayer) {
                                                canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), rad, this.xRefP);
                                                avatarScale2 = 1.0f;
                                            } else {
                                                int paintAlpha = this.paint.getAlpha();
                                                if (alpha2 != 1.0f) {
                                                    this.paint.setAlpha((int) (paintAlpha * alpha2));
                                                }
                                                canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), rad, this.paint);
                                                if (alpha2 == 1.0f) {
                                                    avatarScale2 = 1.0f;
                                                } else {
                                                    this.paint.setAlpha(paintAlpha);
                                                    avatarScale2 = 1.0f;
                                                }
                                            }
                                        }
                                    }
                                }
                                canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                if (states[a].wavesDrawable == null) {
                                    if (this.currentStyle == 5) {
                                        states[a].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(16.0f));
                                    } else {
                                        states[a].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                    }
                                }
                                if (this.currentStyle == 5) {
                                    states[a].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_speakingText), (int) (alpha2 * 76.5f)));
                                }
                                if (states[a].participant == null || states[a].participant.amplitude <= 0.0f) {
                                    states[a].wavesDrawable.setShowWaves(false, this.parent);
                                } else {
                                    states[a].wavesDrawable.setShowWaves(true, this.parent);
                                    float amplitude2 = states[a].participant.amplitude * 15.0f;
                                    states[a].wavesDrawable.setAmplitude(amplitude2);
                                }
                                if (this.currentStyle == 5 && SystemClock.uptimeMillis() - states[a].participant.lastSpeakTime > 500) {
                                    this.updateDelegate.run();
                                }
                                states[a].wavesDrawable.update();
                                if (this.currentStyle == 5) {
                                    states[a].wavesDrawable.draw(canvas, imageReceiver.getCenterX(), imageReceiver.getCenterY(), this.parent);
                                    invalidate();
                                }
                                avatarScale = states[a].wavesDrawable.getAvatarScale();
                                imageReceiver.setAlpha(alpha2);
                                if (avatarScale != 1.0f) {
                                }
                                if (needRestore) {
                                }
                            }
                            avatarScale = avatarScale2;
                            imageReceiver.setAlpha(alpha2);
                            if (avatarScale != 1.0f) {
                            }
                            if (needRestore) {
                            }
                        }
                    }
                }
                k++;
                i2 = 1;
                i4 = 4;
                i = 2;
                i5 = 10;
                i6 = 11;
            }
            a--;
            i2 = 1;
            i4 = 4;
            i = 2;
            i5 = 10;
            i6 = 11;
        }
        if (useAlphaLayer) {
            canvas.restore();
        }
    }

    private int getSize() {
        int i = this.overrideSize;
        if (i != 0) {
            return i;
        }
        int i2 = this.currentStyle;
        boolean bigAvatars = i2 == 4 || i2 == 10;
        return AndroidUtilities.dp(bigAvatars ? 32.0f : 24.0f);
    }

    public void onDetachedFromWindow() {
        this.wasDraw = false;
        for (int a = 0; a < 3; a++) {
            this.currentStates[a].imageReceiver.onDetachedFromWindow();
            this.animatingStates[a].imageReceiver.onDetachedFromWindow();
        }
        int a2 = this.currentStyle;
        if (a2 == 3) {
            Theme.getFragmentContextViewWavesDrawable().setAmplitude(0.0f);
        }
    }

    public void onAttachedToWindow() {
        for (int a = 0; a < 3; a++) {
            this.currentStates[a].imageReceiver.onAttachedToWindow();
            this.animatingStates[a].imageReceiver.onAttachedToWindow();
        }
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void setCount(int count) {
        this.count = count;
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
