package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$GroupCall;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.voip.VoIPButtonsLayout;
import org.telegram.ui.Components.voip.VoIPToggleButton;
import org.telegram.ui.GroupCallActivity;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class GroupCallPipAlertView extends LinearLayout implements VoIPService.StateListener, NotificationCenter.NotificationCenterDelegate {
    BackupImageView avatarImageView;
    int currentAccount;
    float cx;
    float cy;
    FrameLayout groupInfoContainer;
    private boolean invalidateGradient;
    VoIPToggleButton leaveButton;
    LinearGradient linearGradient;
    VoIPToggleButton muteButton;
    float muteProgress;
    private boolean mutedByAdmin;
    float mutedByAdminProgress;
    Paint paint;
    private int position;
    RectF rectF;
    VoIPToggleButton soundButton;
    TextView subtitleView;
    TextView titleView;

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.-CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraSwitch(boolean z) {
        VoIPService.StateListener.-CC.$default$onCameraSwitch(this, z);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onMediaStateUpdated(int i, int i2) {
        VoIPService.StateListener.-CC.$default$onMediaStateUpdated(this, i, i2);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onScreenOnChange(boolean z) {
        VoIPService.StateListener.-CC.$default$onScreenOnChange(this, z);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onSignalBarsCountChanged(int i) {
        VoIPService.StateListener.-CC.$default$onSignalBarsCountChanged(this, i);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onVideoAvailableChange(boolean z) {
        VoIPService.StateListener.-CC.$default$onVideoAvailableChange(this, z);
    }

    public GroupCallPipAlertView(final Context context, int i) {
        super(context);
        this.rectF = new RectF();
        this.paint = new Paint(1);
        this.invalidateGradient = true;
        setOrientation(1);
        this.currentAccount = i;
        this.paint.setAlpha(NotificationCenter.needCheckSystemBarColors);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.GroupCallPipAlertView.1
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                if (Build.VERSION.SDK_INT >= 21) {
                    VoIPService sharedInstance = VoIPService.getSharedInstance();
                    if (sharedInstance != null && ChatObject.isChannelOrGiga(sharedInstance.getChat())) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.VoipChannelOpenVoiceChat)));
                    } else {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.VoipGroupOpenVoiceChat)));
                    }
                }
            }
        };
        this.groupInfoContainer = frameLayout;
        frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        this.avatarImageView = backupImageView;
        backupImageView.setRoundRadius(AndroidUtilities.dp(22.0f));
        this.groupInfoContainer.addView(this.avatarImageView, LayoutHelper.createFrame(44, 44.0f));
        this.groupInfoContainer.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(-1, 76)));
        this.groupInfoContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallPipAlertView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallPipAlertView.this.lambda$new$0(view);
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        this.titleView = textView;
        textView.setTextColor(-1);
        this.titleView.setTextSize(15.0f);
        this.titleView.setMaxLines(2);
        this.titleView.setEllipsize(TextUtils.TruncateAt.END);
        this.titleView.setTypeface(AndroidUtilities.bold());
        linearLayout.addView(this.titleView, LayoutHelper.createLinear(-1, -2));
        TextView textView2 = new TextView(context);
        this.subtitleView = textView2;
        textView2.setTextSize(12.0f);
        this.subtitleView.setTextColor(ColorUtils.setAlphaComponent(-1, NotificationCenter.recordStopped));
        linearLayout.addView(this.subtitleView, LayoutHelper.createLinear(-1, -2));
        this.groupInfoContainer.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 16, 55.0f, 0.0f, 0.0f, 0.0f));
        addView(this.groupInfoContainer, LayoutHelper.createLinear(-1, -2, 0, 10, 10, 10, 10));
        VoIPToggleButton voIPToggleButton = new VoIPToggleButton(context, 44.0f);
        this.soundButton = voIPToggleButton;
        voIPToggleButton.setTextSize(12);
        this.soundButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallPipAlertView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallPipAlertView.this.lambda$new$1(context, view);
            }
        });
        this.soundButton.setCheckable(true);
        this.soundButton.setBackgroundColor(ColorUtils.setAlphaComponent(-1, 38), ColorUtils.setAlphaComponent(-1, 76));
        VoIPToggleButton voIPToggleButton2 = new VoIPToggleButton(context, 44.0f);
        this.muteButton = voIPToggleButton2;
        voIPToggleButton2.setTextSize(12);
        this.muteButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallPipAlertView$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallPipAlertView.this.lambda$new$2(context, view);
            }
        });
        VoIPToggleButton voIPToggleButton3 = new VoIPToggleButton(context, 44.0f);
        this.leaveButton = voIPToggleButton3;
        voIPToggleButton3.setTextSize(12);
        this.leaveButton.setData(R.drawable.calls_decline, -1, -3257782, 0.3f, false, LocaleController.getString(R.string.VoipGroupLeave), false, false);
        this.leaveButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallPipAlertView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallPipAlertView.this.lambda$new$4(context, view);
            }
        });
        VoIPButtonsLayout voIPButtonsLayout = new VoIPButtonsLayout(context);
        voIPButtonsLayout.setChildSize(68);
        voIPButtonsLayout.setUseStartPadding(false);
        voIPButtonsLayout.addView(this.soundButton, LayoutHelper.createFrame(68, 63.0f));
        voIPButtonsLayout.addView(this.muteButton, LayoutHelper.createFrame(68, 63.0f));
        voIPButtonsLayout.addView(this.leaveButton, LayoutHelper.createFrame(68, 63.0f));
        setWillNotDraw(false);
        addView(voIPButtonsLayout, LayoutHelper.createLinear(-1, -2, 0, 6, 0, 6, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (VoIPService.getSharedInstance() != null) {
            Intent action = new Intent(getContext(), LaunchActivity.class).setAction("voip_chat");
            action.putExtra("currentAccount", VoIPService.getSharedInstance().getAccount());
            getContext().startActivity(action);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Context context, View view) {
        boolean z;
        boolean canDrawOverlays;
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        Context context2 = getContext();
        if (Build.VERSION.SDK_INT >= 23) {
            canDrawOverlays = Settings.canDrawOverlays(context);
            if (!canDrawOverlays) {
                z = false;
                sharedInstance.toggleSpeakerphoneOrShowRouteSheet(context2, z);
            }
        }
        z = true;
        sharedInstance.toggleSpeakerphoneOrShowRouteSheet(context2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Context context, View view) {
        if (VoIPService.getSharedInstance() != null) {
            if (VoIPService.getSharedInstance().mutedByAdmin()) {
                this.muteButton.shakeView();
                try {
                    Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(200L);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
            VoIPService.getSharedInstance().setMicMute(!VoIPService.getSharedInstance().isMicMute(), false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(final Context context, View view) {
        boolean z;
        boolean canDrawOverlays;
        Context context2 = getContext();
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.GroupCallPipAlertView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallPip.updateVisibility(context);
            }
        };
        if (Build.VERSION.SDK_INT >= 23) {
            canDrawOverlays = Settings.canDrawOverlays(context);
            if (!canDrawOverlays) {
                z = false;
                GroupCallActivity.onLeaveClick(context2, runnable, z);
            }
        }
        z = true;
        GroupCallActivity.onLeaveClick(context2, runnable, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0218  */
    @Override // android.widget.LinearLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        boolean z;
        int i;
        float translationX;
        float measuredHeight;
        int i2;
        boolean z2 = VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute() || this.mutedByAdmin;
        if (z2) {
            float f = this.muteProgress;
            if (f != 1.0f) {
                float f2 = f + 0.10666667f;
                this.muteProgress = f2;
                if (f2 >= 1.0f) {
                    this.muteProgress = 1.0f;
                }
                this.invalidateGradient = true;
                invalidate();
                z = this.mutedByAdmin;
                if (z) {
                    float f3 = this.mutedByAdminProgress;
                    if (f3 != 1.0f) {
                        float f4 = f3 + 0.10666667f;
                        this.mutedByAdminProgress = f4;
                        if (f4 >= 1.0f) {
                            this.mutedByAdminProgress = 1.0f;
                        }
                        this.invalidateGradient = true;
                        invalidate();
                        if (this.invalidateGradient) {
                            int blendARGB = ColorUtils.blendARGB(Theme.getColor(Theme.key_voipgroup_overlayAlertGradientMuted), Theme.getColor(Theme.key_voipgroup_overlayAlertGradientUnmuted), 1.0f - this.muteProgress);
                            int blendARGB2 = ColorUtils.blendARGB(Theme.getColor(Theme.key_voipgroup_overlayAlertGradientMuted2), Theme.getColor(Theme.key_voipgroup_overlayAlertGradientUnmuted2), 1.0f - this.muteProgress);
                            int blendARGB3 = ColorUtils.blendARGB(blendARGB, Theme.getColor(Theme.key_voipgroup_overlayAlertMutedByAdmin), this.mutedByAdminProgress);
                            int blendARGB4 = ColorUtils.blendARGB(blendARGB2, Theme.getColor(Theme.key_voipgroup_overlayAlertMutedByAdmin2), this.mutedByAdminProgress);
                            this.invalidateGradient = false;
                            int i3 = this.position;
                            if (i3 == 0) {
                                this.linearGradient = new LinearGradient(-AndroidUtilities.dp(60.0f), this.cy - getTranslationY(), getMeasuredWidth(), getMeasuredHeight() / 2.0f, new int[]{blendARGB3, blendARGB4}, (float[]) null, Shader.TileMode.CLAMP);
                            } else if (i3 == 1) {
                                this.linearGradient = new LinearGradient(0.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() + AndroidUtilities.dp(60.0f), this.cy - getTranslationY(), new int[]{blendARGB4, blendARGB3}, (float[]) null, Shader.TileMode.CLAMP);
                            } else if (i3 == 2) {
                                this.linearGradient = new LinearGradient(this.cx - getTranslationX(), -AndroidUtilities.dp(60.0f), getMeasuredWidth() / 2.0f, getMeasuredHeight(), new int[]{blendARGB3, blendARGB4}, (float[]) null, Shader.TileMode.CLAMP);
                            } else {
                                this.linearGradient = new LinearGradient(getMeasuredWidth() / 2.0f, 0.0f, this.cx - getTranslationX(), getMeasuredHeight() + AndroidUtilities.dp(60.0f), new int[]{blendARGB4, blendARGB3}, (float[]) null, Shader.TileMode.CLAMP);
                            }
                        }
                        this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        this.paint.setShader(this.linearGradient);
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.paint);
                        i = this.position;
                        if (i == 0) {
                            measuredHeight = this.cy - getTranslationY();
                            translationX = 0.0f;
                        } else if (i == 1) {
                            measuredHeight = this.cy - getTranslationY();
                            translationX = getMeasuredWidth();
                        } else if (i == 2) {
                            translationX = this.cx - getTranslationX();
                            measuredHeight = 0.0f;
                        } else {
                            translationX = this.cx - getTranslationX();
                            measuredHeight = getMeasuredHeight();
                        }
                        setPivotX(translationX);
                        setPivotY(measuredHeight);
                        canvas.save();
                        i2 = this.position;
                        if (i2 == 0) {
                            canvas.clipRect(translationX - AndroidUtilities.dp(15.0f), measuredHeight - AndroidUtilities.dp(15.0f), translationX, AndroidUtilities.dp(15.0f) + measuredHeight);
                            canvas.translate(AndroidUtilities.dp(3.0f), 0.0f);
                            canvas.rotate(45.0f, translationX, measuredHeight);
                        } else if (i2 == 1) {
                            canvas.clipRect(translationX, measuredHeight - AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f) + translationX, AndroidUtilities.dp(15.0f) + measuredHeight);
                            canvas.translate(-AndroidUtilities.dp(3.0f), 0.0f);
                            canvas.rotate(45.0f, translationX, measuredHeight);
                        } else if (i2 == 2) {
                            canvas.clipRect(translationX - AndroidUtilities.dp(15.0f), measuredHeight - AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f) + translationX, measuredHeight);
                            canvas.rotate(45.0f, translationX, measuredHeight);
                            canvas.translate(0.0f, AndroidUtilities.dp(3.0f));
                        } else {
                            canvas.clipRect(translationX - AndroidUtilities.dp(15.0f), measuredHeight, AndroidUtilities.dp(15.0f) + translationX, AndroidUtilities.dp(15.0f) + measuredHeight);
                            canvas.rotate(45.0f, translationX, measuredHeight);
                            canvas.translate(0.0f, -AndroidUtilities.dp(3.0f));
                        }
                        this.rectF.set(translationX - AndroidUtilities.dp(10.0f), measuredHeight - AndroidUtilities.dp(10.0f), translationX + AndroidUtilities.dp(10.0f), measuredHeight + AndroidUtilities.dp(10.0f));
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.paint);
                        canvas.restore();
                        super.onDraw(canvas);
                    }
                }
                if (!z) {
                    float f5 = this.mutedByAdminProgress;
                    if (f5 != 0.0f) {
                        float f6 = f5 - 0.10666667f;
                        this.mutedByAdminProgress = f6;
                        if (f6 < 0.0f) {
                            this.mutedByAdminProgress = 0.0f;
                        }
                        this.invalidateGradient = true;
                        invalidate();
                    }
                }
                if (this.invalidateGradient) {
                }
                this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.paint.setShader(this.linearGradient);
                canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.paint);
                i = this.position;
                if (i == 0) {
                }
                setPivotX(translationX);
                setPivotY(measuredHeight);
                canvas.save();
                i2 = this.position;
                if (i2 == 0) {
                }
                this.rectF.set(translationX - AndroidUtilities.dp(10.0f), measuredHeight - AndroidUtilities.dp(10.0f), translationX + AndroidUtilities.dp(10.0f), measuredHeight + AndroidUtilities.dp(10.0f));
                canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.paint);
                canvas.restore();
                super.onDraw(canvas);
            }
        }
        if (!z2) {
            float f7 = this.muteProgress;
            if (f7 != 0.0f) {
                float f8 = f7 - 0.10666667f;
                this.muteProgress = f8;
                if (f8 < 0.0f) {
                    this.muteProgress = 0.0f;
                }
                this.invalidateGradient = true;
                invalidate();
            }
        }
        z = this.mutedByAdmin;
        if (z) {
        }
        if (!z) {
        }
        if (this.invalidateGradient) {
        }
        this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
        this.paint.setShader(this.linearGradient);
        canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.paint);
        i = this.position;
        if (i == 0) {
        }
        setPivotX(translationX);
        setPivotY(measuredHeight);
        canvas.save();
        i2 = this.position;
        if (i2 == 0) {
        }
        this.rectF.set(translationX - AndroidUtilities.dp(10.0f), measuredHeight - AndroidUtilities.dp(10.0f), translationX + AndroidUtilities.dp(10.0f), measuredHeight + AndroidUtilities.dp(10.0f));
        canvas.drawRoundRect(this.rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.paint);
        canvas.restore();
        super.onDraw(canvas);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(230.0f), 1073741824), i2);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        String str;
        super.onAttachedToWindow();
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null && sharedInstance.groupCall != null) {
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setColor(Theme.getColor(Theme.keys_avatar_background[AvatarDrawable.getColorIndex(sharedInstance.getChat().id)]), Theme.getColor(Theme.keys_avatar_background2[AvatarDrawable.getColorIndex(sharedInstance.getChat().id)]));
            avatarDrawable.setInfo(this.currentAccount, sharedInstance.getChat());
            this.avatarImageView.setImage(ImageLocation.getForLocal(sharedInstance.getChat().photo.photo_small), "50_50", avatarDrawable, (Object) null);
            if (!TextUtils.isEmpty(sharedInstance.groupCall.call.title)) {
                str = sharedInstance.groupCall.call.title;
            } else {
                str = sharedInstance.getChat().title;
            }
            if (str != null) {
                str = str.replace("\n", " ").replaceAll(" +", " ").trim();
            }
            this.titleView.setText(str);
            updateMembersCount();
            sharedInstance.registerStateListener(this);
            if (VoIPService.getSharedInstance() != null) {
                this.mutedByAdmin = VoIPService.getSharedInstance().mutedByAdmin();
            }
            float f = 0.0f;
            this.mutedByAdminProgress = this.mutedByAdmin ? 1.0f : 0.0f;
            this.muteProgress = (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute() || this.mutedByAdmin) ? 1.0f : 1.0f;
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupCallUpdated);
        updateButtons(false);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            sharedInstance.unregisterStateListener(this);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
    }

    private void updateMembersCount() {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || sharedInstance.groupCall == null) {
            return;
        }
        int callState = sharedInstance.getCallState();
        if (!sharedInstance.isSwitchingStream() && (callState == 1 || callState == 2 || callState == 6 || callState == 5)) {
            this.subtitleView.setText(LocaleController.getString("VoipGroupConnecting", R.string.VoipGroupConnecting));
            return;
        }
        TextView textView = this.subtitleView;
        TLRPC$GroupCall tLRPC$GroupCall = sharedInstance.groupCall.call;
        textView.setText(LocaleController.formatPluralString(tLRPC$GroupCall.rtmp_stream ? "ViewersWatching" : "Participants", tLRPC$GroupCall.participants_count, new Object[0]));
    }

    private void updateButtons(boolean z) {
        VoIPService sharedInstance;
        if (this.soundButton == null || this.muteButton == null || (sharedInstance = VoIPService.getSharedInstance()) == null) {
            return;
        }
        boolean isBluetoothOn = sharedInstance.isBluetoothOn();
        boolean z2 = !isBluetoothOn && sharedInstance.isSpeakerphoneOn();
        this.soundButton.setChecked(z2, z);
        if (isBluetoothOn) {
            this.soundButton.setData(R.drawable.calls_bluetooth, -1, 0, 0.1f, true, LocaleController.getString(R.string.VoipAudioRoutingBluetooth), false, z);
        } else if (z2) {
            this.soundButton.setData(R.drawable.calls_speaker, -1, 0, 0.3f, true, LocaleController.getString(R.string.VoipSpeaker), false, z);
        } else if (sharedInstance.isHeadsetPlugged()) {
            this.soundButton.setData(R.drawable.calls_headphones, -1, 0, 0.1f, true, LocaleController.getString(R.string.VoipAudioRoutingHeadset), false, z);
        } else {
            this.soundButton.setData(R.drawable.calls_speaker, -1, 0, 0.1f, true, LocaleController.getString(R.string.VoipSpeaker), false, z);
        }
        if (sharedInstance.mutedByAdmin()) {
            this.muteButton.setData(R.drawable.calls_unmute, -1, ColorUtils.setAlphaComponent(-1, 76), 0.1f, true, LocaleController.getString(R.string.VoipMutedByAdminShort), true, z);
        } else {
            this.muteButton.setData(R.drawable.calls_unmute, -1, ColorUtils.setAlphaComponent(-1, (int) ((sharedInstance.isMicMute() ? 0.3f : 0.15f) * 255.0f)), 0.1f, true, LocaleController.getString(sharedInstance.isMicMute() ? R.string.VoipUnmute : R.string.VoipMute), sharedInstance.isMicMute(), z);
        }
        invalidate();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
        updateButtons(true);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onStateChanged(int i) {
        updateMembersCount();
    }

    public void setPosition(int i, float f, float f2) {
        this.position = i;
        this.cx = f;
        this.cy = f2;
        invalidate();
        this.invalidateGradient = true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        boolean mutedByAdmin;
        if (i == NotificationCenter.groupCallUpdated) {
            updateMembersCount();
            if (VoIPService.getSharedInstance() == null || (mutedByAdmin = VoIPService.getSharedInstance().mutedByAdmin()) == this.mutedByAdmin) {
                return;
            }
            this.mutedByAdmin = mutedByAdmin;
            invalidate();
        }
    }
}
