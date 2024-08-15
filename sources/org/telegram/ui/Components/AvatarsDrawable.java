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
import org.telegram.ui.Stories.StoriesGradientTools;
/* loaded from: classes3.dex */
public class AvatarsDrawable {
    private boolean attached;
    boolean centered;
    public int count;
    int currentStyle;
    public boolean drawStoriesCircle;
    public int height;
    private boolean isInCall;
    private int overrideSize;
    View parent;
    private boolean showSavedMessages;
    StoriesGradientTools storiesTools;
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
    public int strokeWidth = AndroidUtilities.dp(1.67f);
    private float overrideSizeStepFactor = 0.8f;
    private float overrideAlpha = 1.0f;
    public long transitionDuration = 220;
    public Interpolator transitionInterpolator = CubicBezierInterpolator.DEFAULT;
    Random random = new Random();

    public void commitTransition(boolean z) {
        commitTransition(z, true);
    }

    public void setTransitionProgress(float f) {
        if (!this.transitionInProgress || this.transitionProgress == f) {
            return;
        }
        this.transitionProgress = f;
        if (f == 1.0f) {
            swapStates();
            this.transitionInProgress = false;
        }
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
            DrawingState drawingState = drawingStateArr[i4];
            if (drawingState != null) {
                drawingState.animationType = 1;
            }
        }
        ValueAnimator valueAnimator = this.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.transitionProgressAnimator.cancel();
            if (this.transitionInProgress) {
                swapStates();
                this.transitionInProgress = false;
            }
        }
        this.transitionProgress = 0.0f;
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.transitionProgressAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AvatarsDrawable$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    AvatarsDrawable.this.lambda$commitTransition$0(valueAnimator2);
                }
            });
            this.transitionProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AvatarsDrawable.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AvatarsDrawable avatarsDrawable = AvatarsDrawable.this;
                    if (avatarsDrawable.transitionProgressAnimator != null) {
                        avatarsDrawable.transitionProgress = 1.0f;
                        avatarsDrawable.swapStates();
                        AvatarsDrawable avatarsDrawable2 = AvatarsDrawable.this;
                        if (avatarsDrawable2.updateAfterTransition) {
                            avatarsDrawable2.updateAfterTransition = false;
                            Runnable runnable = avatarsDrawable2.updateDelegate;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                        AvatarsDrawable.this.invalidate();
                    }
                    AvatarsDrawable.this.transitionProgressAnimator = null;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commitTransition$0(ValueAnimator valueAnimator) {
        this.transitionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidate() {
        View view = this.parent;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setSize(int i) {
        this.overrideSize = i;
    }

    public void setStepFactor(float f) {
        this.overrideSizeStepFactor = f;
    }

    public void animateFromState(AvatarsDrawable avatarsDrawable, int i, boolean z) {
        if (avatarsDrawable == null) {
            return;
        }
        ValueAnimator valueAnimator = avatarsDrawable.transitionProgressAnimator;
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
            setObject(i2, i, avatarsDrawable.currentStates[i2].object);
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

    /* JADX INFO: Access modifiers changed from: private */
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

    public AvatarsDrawable(View view, boolean z) {
        this.parent = view;
        for (int i = 0; i < 3; i++) {
            this.currentStates[i] = new DrawingState();
            this.currentStates[i].imageReceiver = new ImageReceiver(view);
            this.currentStates[i].imageReceiver.setInvalidateAll(true);
            this.currentStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.currentStates[i].avatarDrawable = new AvatarDrawable();
            this.currentStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            this.animatingStates[i] = new DrawingState();
            this.animatingStates[i].imageReceiver = new ImageReceiver(view);
            this.animatingStates[i].imageReceiver.setInvalidateAll(true);
            this.animatingStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.animatingStates[i].avatarDrawable = new AvatarDrawable();
            this.animatingStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        }
        this.isInCall = z;
        this.xRefP.setColor(0);
        this.xRefP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setAvatarsTextSize(int i) {
        for (int i2 = 0; i2 < 3; i2++) {
            DrawingState drawingState = this.currentStates[i2];
            if (drawingState != null && drawingState.avatarDrawable != null) {
                this.currentStates[i2].avatarDrawable.setTextSize(i);
            }
            DrawingState drawingState2 = this.animatingStates[i2];
            if (drawingState2 != null && drawingState2.avatarDrawable != null) {
                this.animatingStates[i2].avatarDrawable.setTextSize(i);
            }
        }
    }

    public void setObject(int i, int i2, TLObject tLObject) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        this.animatingStates[i].id = 0L;
        DrawingState drawingState = this.animatingStates[i];
        TLRPC$User tLRPC$User = null;
        drawingState.participant = null;
        if (tLObject == null) {
            drawingState.imageReceiver.setImageBitmap((Drawable) null);
            invalidate();
            return;
        }
        drawingState.lastSpeakTime = -1L;
        this.animatingStates[i].object = tLObject;
        if (tLObject instanceof TLRPC$TL_groupCallParticipant) {
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = (TLRPC$TL_groupCallParticipant) tLObject;
            this.animatingStates[i].participant = tLRPC$TL_groupCallParticipant;
            long peerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
            if (DialogObject.isUserDialog(peerId)) {
                TLRPC$User user = MessagesController.getInstance(i2).getUser(Long.valueOf(peerId));
                this.animatingStates[i].avatarDrawable.setInfo(i2, user);
                tLRPC$User = user;
                chat = null;
            } else {
                chat = MessagesController.getInstance(i2).getChat(Long.valueOf(-peerId));
                this.animatingStates[i].avatarDrawable.setInfo(i2, chat);
            }
            if (this.currentStyle == 4) {
                if (peerId == AccountInstance.getInstance(i2).getUserConfig().getClientUserId()) {
                    this.animatingStates[i].lastSpeakTime = 0L;
                } else if (this.isInCall) {
                    this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.lastActiveDate;
                } else {
                    this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.active_date;
                }
            } else {
                this.animatingStates[i].lastSpeakTime = tLRPC$TL_groupCallParticipant.active_date;
            }
            this.animatingStates[i].id = peerId;
            tLRPC$Chat = chat;
        } else if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User2 = (TLRPC$User) tLObject;
            if (!tLRPC$User2.self || !this.showSavedMessages) {
                this.animatingStates[i].avatarDrawable.setAvatarType(0);
                this.animatingStates[i].avatarDrawable.setScaleSize(1.0f);
                this.animatingStates[i].avatarDrawable.setInfo(i2, tLRPC$User2);
            } else {
                this.animatingStates[i].avatarDrawable.setAvatarType(1);
                this.animatingStates[i].avatarDrawable.setScaleSize(0.6f);
            }
            this.animatingStates[i].id = tLRPC$User2.id;
            tLRPC$User = tLRPC$User2;
            tLRPC$Chat = null;
        } else {
            tLRPC$Chat = (TLRPC$Chat) tLObject;
            this.animatingStates[i].avatarDrawable.setAvatarType(0);
            this.animatingStates[i].avatarDrawable.setScaleSize(1.0f);
            this.animatingStates[i].avatarDrawable.setInfo(i2, tLRPC$Chat);
            this.animatingStates[i].id = -tLRPC$Chat.id;
        }
        if (tLRPC$User == null) {
            this.animatingStates[i].imageReceiver.setForUserOrChat(tLRPC$Chat, this.animatingStates[i].avatarDrawable);
        } else if (!tLRPC$User.self || !this.showSavedMessages) {
            this.animatingStates[i].imageReceiver.setForUserOrChat(tLRPC$User, this.animatingStates[i].avatarDrawable);
        } else {
            this.animatingStates[i].imageReceiver.setImageBitmap(this.animatingStates[i].avatarDrawable);
        }
        int size = getSize();
        this.animatingStates[i].imageReceiver.setRoundRadius(size / 2);
        float f = size;
        this.animatingStates[i].imageReceiver.setImageCoords(0.0f, 0.0f, f, f);
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:158:0x0283  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0368  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x043e  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x060e  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x062a  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x065b  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x067f  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x06a8  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x06bd  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x06c2  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x02be A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:345:0x06c5 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int dp;
        int i;
        boolean z;
        float f;
        int i2;
        int i3;
        DrawingState drawingState;
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant;
        float avatarScale;
        DrawingState drawingState2;
        DrawingState drawingState3;
        int i4;
        float f2;
        float f3;
        boolean z2;
        int i5;
        int i6 = 1;
        this.wasDraw = true;
        int i7 = this.currentStyle;
        int i8 = 10;
        int i9 = 4;
        boolean z3 = i7 == 4 || i7 == 10;
        int size = getSize();
        int i10 = 11;
        if (this.currentStyle == 11) {
            dp = AndroidUtilities.dp(12.0f);
        } else {
            int i11 = this.overrideSize;
            if (i11 != 0) {
                dp = (int) (i11 * this.overrideSizeStepFactor);
            } else {
                dp = AndroidUtilities.dp(z3 ? 24.0f : 20.0f);
            }
        }
        int i12 = dp;
        int i13 = 0;
        for (int i14 = 0; i14 < 3; i14++) {
            if (this.currentStates[i14].id != 0) {
                i13++;
            }
        }
        int i15 = this.currentStyle;
        int dp2 = (i15 == 0 || i15 == 10 || i15 == 11) ? 0 : AndroidUtilities.dp(10.0f);
        int dp3 = this.centered ? ((this.width - (i13 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / 2 : dp2;
        boolean z4 = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        int i16 = this.currentStyle;
        if (i16 == 4) {
            this.paint.setColor(Theme.getColor(Theme.key_inappPlayerBackground));
        } else if (i16 != 3) {
            this.paint.setColor(Theme.getColor(z4 ? Theme.key_returnToCallMutedBackground : Theme.key_returnToCallBackground));
        }
        int i17 = 0;
        for (int i18 = 0; i18 < 3; i18++) {
            if (this.animatingStates[i18].id != 0) {
                i17++;
            }
        }
        int i19 = this.currentStyle;
        boolean z5 = i19 == 0 || i19 == 1 || i19 == 3 || i19 == 4 || i19 == 5 || i19 == 10 || i19 == 11;
        if (z5) {
            float dp4 = i19 == 10 ? AndroidUtilities.dp(16.0f) : 0.0f;
            if (this.drawStoriesCircle) {
                dp4 += AndroidUtilities.dp(20.0f);
            }
            float f4 = -dp4;
            i = 2;
            canvas.saveLayerAlpha(f4, f4, this.width + dp4, this.height + dp4, 255, 31);
        } else {
            i = 2;
        }
        float f5 = 2.0f;
        float f6 = 1.0f;
        if (this.drawStoriesCircle) {
            int i20 = 2;
            while (i20 >= 0) {
                int i21 = 0;
                while (i21 < i) {
                    if (i21 != 0 || this.transitionProgress != 1.0f) {
                        DrawingState[] drawingStateArr = i21 == 0 ? this.animatingStates : this.currentStates;
                        if (i21 != i6 || this.transitionProgress == 1.0f || drawingStateArr[i20].animationType == i6) {
                            ImageReceiver imageReceiver = drawingStateArr[i20].imageReceiver;
                            if (imageReceiver.hasImageSet()) {
                                if (i21 == 0) {
                                    int dp5 = this.centered ? ((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2;
                                    imageReceiver.setImageX(dp5 + (i12 * i20));
                                } else {
                                    imageReceiver.setImageX(dp3 + (i12 * i20));
                                }
                                int i22 = this.currentStyle;
                                if (i22 == 0 || i22 == i8 || i22 == i10) {
                                    imageReceiver.setImageY((this.height - size) / f5);
                                } else {
                                    imageReceiver.setImageY(AndroidUtilities.dp(i22 == i9 ? 8.0f : 6.0f));
                                }
                                if (this.transitionProgress != 1.0f) {
                                    if (drawingStateArr[i20].animationType != i6) {
                                        if (drawingStateArr[i20].animationType != 0) {
                                            if (drawingStateArr[i20].animationType != i) {
                                                if (drawingStateArr[i20].animationType == -1 && this.centered) {
                                                    int dp6 = (((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i) + (i12 * i20);
                                                    float f7 = this.transitionProgress;
                                                    imageReceiver.setImageX((int) ((dp6 * f7) + ((dp3 + i5) * (1.0f - f7))));
                                                }
                                            } else {
                                                int dp7 = this.centered ? ((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2;
                                                float f8 = this.transitionProgress;
                                                imageReceiver.setImageX((int) (((dp7 + (i12 * i20)) * f8) + ((dp3 + (drawingStateArr[i20].moveFromIndex * i12)) * (1.0f - f8))));
                                            }
                                        } else {
                                            canvas.save();
                                            float f9 = this.transitionProgress;
                                            canvas.scale(f9, f9, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                            f3 = this.transitionProgress;
                                        }
                                    } else {
                                        canvas.save();
                                        float f10 = this.transitionProgress;
                                        canvas.scale(1.0f - f10, 1.0f - f10, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                        f3 = 1.0f - this.transitionProgress;
                                    }
                                    z2 = true;
                                    float f11 = f3 * this.overrideAlpha;
                                    float size2 = (getSize() / f5) + AndroidUtilities.dp(4.0f);
                                    if (this.storiesTools == null) {
                                        this.storiesTools = new StoriesGradientTools();
                                    }
                                    this.storiesTools.setBounds(0.0f, 0.0f, this.parent.getMeasuredHeight(), AndroidUtilities.dp(40.0f));
                                    this.storiesTools.paint.setAlpha((int) (f11 * 255.0f));
                                    canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size2, this.storiesTools.paint);
                                    if (!z2) {
                                        canvas.restore();
                                    }
                                }
                                f3 = 1.0f;
                                z2 = false;
                                float f112 = f3 * this.overrideAlpha;
                                float size22 = (getSize() / f5) + AndroidUtilities.dp(4.0f);
                                if (this.storiesTools == null) {
                                }
                                this.storiesTools.setBounds(0.0f, 0.0f, this.parent.getMeasuredHeight(), AndroidUtilities.dp(40.0f));
                                this.storiesTools.paint.setAlpha((int) (f112 * 255.0f));
                                canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size22, this.storiesTools.paint);
                                if (!z2) {
                                }
                            }
                        }
                    }
                    i21++;
                    f5 = 2.0f;
                    i6 = 1;
                    i8 = 10;
                    i9 = 4;
                    i10 = 11;
                }
                i20--;
                f5 = 2.0f;
                i6 = 1;
                i8 = 10;
                i9 = 4;
                i10 = 11;
            }
        }
        int i23 = 2;
        while (i23 >= 0) {
            int i24 = 0;
            while (i24 < i) {
                if (i24 != 0 || this.transitionProgress != f6) {
                    DrawingState[] drawingStateArr2 = i24 == 0 ? this.animatingStates : this.currentStates;
                    if (i24 != 1 || this.transitionProgress == f6 || drawingStateArr2[i23].animationType == 1) {
                        ImageReceiver imageReceiver2 = drawingStateArr2[i23].imageReceiver;
                        if (imageReceiver2.hasImageSet()) {
                            if (i24 == 0) {
                                int dp8 = this.centered ? ((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2;
                                imageReceiver2.setImageX(dp8 + (i12 * i23));
                            } else {
                                imageReceiver2.setImageX(dp3 + (i12 * i23));
                            }
                            int i25 = this.currentStyle;
                            if (i25 != 0 && i25 != 10) {
                                if (i25 != 11) {
                                    imageReceiver2.setImageY(AndroidUtilities.dp(i25 == 4 ? 8.0f : 6.0f));
                                    if (this.transitionProgress != f6) {
                                        if (drawingStateArr2[i23].animationType != 1) {
                                            if (drawingStateArr2[i23].animationType != 0) {
                                                if (drawingStateArr2[i23].animationType != i) {
                                                    if (drawingStateArr2[i23].animationType == -1 && this.centered) {
                                                        int dp9 = (((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i) + (i12 * i23);
                                                        float f12 = this.transitionProgress;
                                                        imageReceiver2.setImageX((int) ((dp9 * f12) + ((dp3 + i4) * (f6 - f12))));
                                                    }
                                                    z = false;
                                                    f = 1.0f;
                                                    float f13 = f * this.overrideAlpha;
                                                    if (i23 == drawingStateArr2.length - 1 || this.drawStoriesCircle) {
                                                        i2 = this.currentStyle;
                                                        if (i2 != 1 || i2 == 3) {
                                                            i3 = 5;
                                                        } else {
                                                            i3 = 5;
                                                            if (i2 != 5) {
                                                                if (i2 == 4 || i2 == 10) {
                                                                    canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(17.0f), this.xRefP);
                                                                    if (drawingStateArr2[i23].wavesDrawable == null) {
                                                                        drawingStateArr2[i23].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                                                    }
                                                                    if (this.currentStyle == 10) {
                                                                        drawingStateArr2[i23].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_speakingText), (int) (f13 * 76.5f)));
                                                                    } else {
                                                                        drawingStateArr2[i23].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_listeningText), (int) (f13 * 76.5f)));
                                                                    }
                                                                    long currentTimeMillis = System.currentTimeMillis();
                                                                    if (currentTimeMillis - drawingStateArr2[i23].lastUpdateTime > 100) {
                                                                        drawingStateArr2[i23].lastUpdateTime = currentTimeMillis;
                                                                        if (this.currentStyle != 10) {
                                                                            if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - drawingStateArr2[i23].lastSpeakTime <= 5) {
                                                                                drawingStateArr2[i23].wavesDrawable.setShowWaves(true, this.parent);
                                                                                drawingStateArr2[i23].wavesDrawable.setAmplitude(this.random.nextInt() % 100);
                                                                            } else {
                                                                                drawingStateArr2[i23].wavesDrawable.setShowWaves(false, this.parent);
                                                                                drawingStateArr2[i23].wavesDrawable.setAmplitude(0.0d);
                                                                            }
                                                                        } else {
                                                                            DrawingState drawingState4 = drawingStateArr2[i23];
                                                                            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant2 = drawingState4.participant;
                                                                            if (tLRPC$TL_groupCallParticipant2 == null || tLRPC$TL_groupCallParticipant2.amplitude <= 0.0f) {
                                                                                drawingState4.wavesDrawable.setShowWaves(false, this.parent);
                                                                            } else {
                                                                                drawingState4.wavesDrawable.setShowWaves(true, this.parent);
                                                                                drawingStateArr2[i23].wavesDrawable.setAmplitude(drawingState3.participant.amplitude * 15.0f);
                                                                            }
                                                                        }
                                                                    }
                                                                    drawingStateArr2[i23].wavesDrawable.update();
                                                                    drawingStateArr2[i23].wavesDrawable.draw(canvas, imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), this.parent);
                                                                    avatarScale = drawingStateArr2[i23].wavesDrawable.getAvatarScale();
                                                                    imageReceiver2.setAlpha(f13);
                                                                    f6 = 1.0f;
                                                                    if (avatarScale == 1.0f) {
                                                                        canvas.save();
                                                                        canvas.scale(avatarScale, avatarScale, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                                                        imageReceiver2.draw(canvas);
                                                                        canvas.restore();
                                                                    } else {
                                                                        imageReceiver2.draw(canvas);
                                                                    }
                                                                    if (!z) {
                                                                        canvas.restore();
                                                                    }
                                                                    i24++;
                                                                    i = 2;
                                                                } else {
                                                                    float size3 = (getSize() / 2.0f) + this.strokeWidth;
                                                                    if (z5) {
                                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), size3, this.xRefP);
                                                                    } else {
                                                                        int alpha = this.paint.getAlpha();
                                                                        if (f13 != f6) {
                                                                            this.paint.setAlpha((int) (alpha * f13));
                                                                        }
                                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), size3, this.paint);
                                                                        if (f13 != f6) {
                                                                            this.paint.setAlpha(alpha);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                                        if (drawingStateArr2[i23].wavesDrawable == null) {
                                                            if (this.currentStyle == i3) {
                                                                drawingStateArr2[i23].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(16.0f));
                                                            } else {
                                                                drawingStateArr2[i23].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                                            }
                                                        }
                                                        if (this.currentStyle == i3) {
                                                            drawingStateArr2[i23].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_speakingText), (int) (f13 * 76.5f)));
                                                        }
                                                        drawingState = drawingStateArr2[i23];
                                                        tLRPC$TL_groupCallParticipant = drawingState.participant;
                                                        if (tLRPC$TL_groupCallParticipant != null || tLRPC$TL_groupCallParticipant.amplitude <= 0.0f) {
                                                            drawingState.wavesDrawable.setShowWaves(false, this.parent);
                                                        } else {
                                                            drawingState.wavesDrawable.setShowWaves(true, this.parent);
                                                            drawingStateArr2[i23].wavesDrawable.setAmplitude(drawingState2.participant.amplitude * 15.0f);
                                                        }
                                                        if (this.currentStyle == i3 && SystemClock.uptimeMillis() - drawingStateArr2[i23].participant.lastSpeakTime > 500) {
                                                            this.updateDelegate.run();
                                                        }
                                                        drawingStateArr2[i23].wavesDrawable.update();
                                                        if (this.currentStyle == i3) {
                                                            drawingStateArr2[i23].wavesDrawable.draw(canvas, imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), this.parent);
                                                            invalidate();
                                                        }
                                                        avatarScale = drawingStateArr2[i23].wavesDrawable.getAvatarScale();
                                                        imageReceiver2.setAlpha(f13);
                                                        f6 = 1.0f;
                                                        if (avatarScale == 1.0f) {
                                                        }
                                                        if (!z) {
                                                        }
                                                        i24++;
                                                        i = 2;
                                                    }
                                                    avatarScale = 1.0f;
                                                    imageReceiver2.setAlpha(f13);
                                                    f6 = 1.0f;
                                                    if (avatarScale == 1.0f) {
                                                    }
                                                    if (!z) {
                                                    }
                                                    i24++;
                                                    i = 2;
                                                } else {
                                                    int dp10 = this.centered ? ((this.width - (i17 * i12)) - AndroidUtilities.dp(z3 ? 8.0f : 4.0f)) / i : dp2;
                                                    float f14 = this.transitionProgress;
                                                    imageReceiver2.setImageX((int) (((dp10 + (i12 * i23)) * f14) + ((dp3 + (drawingStateArr2[i23].moveFromIndex * i12)) * (f6 - f14))));
                                                }
                                            } else {
                                                canvas.save();
                                                float f15 = this.transitionProgress;
                                                canvas.scale(f15, f15, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                                f2 = this.transitionProgress;
                                            }
                                        } else {
                                            canvas.save();
                                            float f16 = this.transitionProgress;
                                            canvas.scale(f6 - f16, f6 - f16, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                            f2 = f6 - this.transitionProgress;
                                        }
                                        f = f2;
                                        z = true;
                                        float f132 = f * this.overrideAlpha;
                                        if (i23 == drawingStateArr2.length - 1) {
                                        }
                                        i2 = this.currentStyle;
                                        if (i2 != 1) {
                                        }
                                        i3 = 5;
                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                        if (drawingStateArr2[i23].wavesDrawable == null) {
                                        }
                                        if (this.currentStyle == i3) {
                                        }
                                        drawingState = drawingStateArr2[i23];
                                        tLRPC$TL_groupCallParticipant = drawingState.participant;
                                        if (tLRPC$TL_groupCallParticipant != null) {
                                        }
                                        drawingState.wavesDrawable.setShowWaves(false, this.parent);
                                        if (this.currentStyle == i3) {
                                            this.updateDelegate.run();
                                        }
                                        drawingStateArr2[i23].wavesDrawable.update();
                                        if (this.currentStyle == i3) {
                                        }
                                        avatarScale = drawingStateArr2[i23].wavesDrawable.getAvatarScale();
                                        imageReceiver2.setAlpha(f132);
                                        f6 = 1.0f;
                                        if (avatarScale == 1.0f) {
                                        }
                                        if (!z) {
                                        }
                                        i24++;
                                        i = 2;
                                    }
                                    z = false;
                                    f = 1.0f;
                                    float f1322 = f * this.overrideAlpha;
                                    if (i23 == drawingStateArr2.length - 1) {
                                    }
                                    i2 = this.currentStyle;
                                    if (i2 != 1) {
                                    }
                                    i3 = 5;
                                    canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                    if (drawingStateArr2[i23].wavesDrawable == null) {
                                    }
                                    if (this.currentStyle == i3) {
                                    }
                                    drawingState = drawingStateArr2[i23];
                                    tLRPC$TL_groupCallParticipant = drawingState.participant;
                                    if (tLRPC$TL_groupCallParticipant != null) {
                                    }
                                    drawingState.wavesDrawable.setShowWaves(false, this.parent);
                                    if (this.currentStyle == i3) {
                                    }
                                    drawingStateArr2[i23].wavesDrawable.update();
                                    if (this.currentStyle == i3) {
                                    }
                                    avatarScale = drawingStateArr2[i23].wavesDrawable.getAvatarScale();
                                    imageReceiver2.setAlpha(f1322);
                                    f6 = 1.0f;
                                    if (avatarScale == 1.0f) {
                                    }
                                    if (!z) {
                                    }
                                    i24++;
                                    i = 2;
                                }
                            }
                            imageReceiver2.setImageY((this.height - size) / 2.0f);
                            if (this.transitionProgress != f6) {
                            }
                            z = false;
                            f = 1.0f;
                            float f13222 = f * this.overrideAlpha;
                            if (i23 == drawingStateArr2.length - 1) {
                            }
                            i2 = this.currentStyle;
                            if (i2 != 1) {
                            }
                            i3 = 5;
                            canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                            if (drawingStateArr2[i23].wavesDrawable == null) {
                            }
                            if (this.currentStyle == i3) {
                            }
                            drawingState = drawingStateArr2[i23];
                            tLRPC$TL_groupCallParticipant = drawingState.participant;
                            if (tLRPC$TL_groupCallParticipant != null) {
                            }
                            drawingState.wavesDrawable.setShowWaves(false, this.parent);
                            if (this.currentStyle == i3) {
                            }
                            drawingStateArr2[i23].wavesDrawable.update();
                            if (this.currentStyle == i3) {
                            }
                            avatarScale = drawingStateArr2[i23].wavesDrawable.getAvatarScale();
                            imageReceiver2.setAlpha(f13222);
                            f6 = 1.0f;
                            if (avatarScale == 1.0f) {
                            }
                            if (!z) {
                            }
                            i24++;
                            i = 2;
                        }
                    }
                }
                i24++;
                i = 2;
            }
            i23--;
            i = 2;
        }
        if (z5) {
            canvas.restore();
        }
    }

    public int getSize() {
        int i = this.overrideSize;
        if (i != 0) {
            return i;
        }
        int i2 = this.currentStyle;
        return AndroidUtilities.dp(i2 == 4 || i2 == 10 ? 32.0f : 24.0f);
    }

    public void onDetachedFromWindow() {
        if (this.attached) {
            this.attached = false;
            this.wasDraw = false;
            for (int i = 0; i < 3; i++) {
                this.currentStates[i].imageReceiver.onDetachedFromWindow();
                this.animatingStates[i].imageReceiver.onDetachedFromWindow();
            }
            if (this.currentStyle == 3) {
                Theme.getFragmentContextViewWavesDrawable().setAmplitude(0.0f);
            }
        }
    }

    public void onAttachedToWindow() {
        if (this.attached) {
            return;
        }
        this.attached = true;
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

    public void setShowSavedMessages(boolean z) {
        this.showSavedMessages = z;
    }
}
