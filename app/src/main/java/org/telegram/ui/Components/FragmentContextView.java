package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$GroupCall;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_groupCallDiscarded;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupCallActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LocationActivity;
/* loaded from: classes3.dex */
public class FragmentContextView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, VoIPService.StateListener {
    private final int account;
    private FragmentContextView additionalContextView;
    private int animationIndex;
    private AnimatorSet animatorSet;
    private View applyingView;
    private AvatarsImageView avatars;
    private boolean checkCallAfterAnimation;
    private boolean checkImportAfterAnimation;
    private Runnable checkLocationRunnable;
    private boolean checkPlayerAfterAnimation;
    private ImageView closeButton;
    float collapseProgress;
    boolean collapseTransition;
    private int currentProgress;
    private int currentStyle;
    private FragmentContextViewDelegate delegate;
    boolean drawOverlay;
    float extraHeight;
    private boolean firstLocationsLoaded;
    private BaseFragment fragment;
    private FrameLayout frameLayout;
    private Paint gradientPaint;
    private TextPaint gradientTextPaint;
    private int gradientWidth;
    private RLottieImageView importingImageView;
    private boolean isLocation;
    private boolean isMusic;
    private boolean isMuted;
    private TextView joinButton;
    private CellFlickerDrawable joinButtonFlicker;
    private int lastLocationSharingCount;
    private MessageObject lastMessageObject;
    private String lastString;
    private LinearGradient linearGradient;
    private Matrix matrix;
    float micAmplitude;
    private RLottieImageView muteButton;
    private RLottieDrawable muteDrawable;
    private ImageView playButton;
    private PlayPauseDrawable playPauseDrawable;
    private ActionBarMenuItem playbackSpeedButton;
    private RectF rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean scheduleRunnableScheduled;
    private View selector;
    private View shadow;
    float speakerAmplitude;
    private ActionBarMenuSubItem[] speedItems;
    private AudioPlayerAlert.ClippingTextViewSwitcher subtitleTextView;
    private boolean supportsCalls;
    private StaticLayout timeLayout;
    private AudioPlayerAlert.ClippingTextViewSwitcher titleTextView;
    private float topPadding;
    private final Runnable updateScheduleTimeRunnable;
    private boolean visible;
    boolean wasDraw;

    /* loaded from: classes3.dex */
    public interface FragmentContextViewDelegate {
        void onAnimation(boolean z, boolean z2);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraSwitch(boolean z) {
        VoIPService.StateListener.CC.$default$onCameraSwitch(this, z);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onMediaStateUpdated(int i, int i2) {
        VoIPService.StateListener.CC.$default$onMediaStateUpdated(this, i, i2);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onScreenOnChange(boolean z) {
        VoIPService.StateListener.CC.$default$onScreenOnChange(this, z);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onSignalBarsCountChanged(int i) {
        VoIPService.StateListener.CC.$default$onSignalBarsCountChanged(this, i);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onVideoAvailableChange(boolean z) {
        VoIPService.StateListener.CC.$default$onVideoAvailableChange(this, z);
    }

    protected void playbackSpeedChanged(float f) {
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            FragmentContextView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            String str;
            if (FragmentContextView.this.gradientTextPaint == null || !(FragmentContextView.this.fragment instanceof ChatActivity)) {
                FragmentContextView.this.scheduleRunnableScheduled = false;
                return;
            }
            ChatObject.Call groupCall = ((ChatActivity) FragmentContextView.this.fragment).getGroupCall();
            if (groupCall == null || !groupCall.isScheduled()) {
                FragmentContextView.this.timeLayout = null;
                FragmentContextView.this.scheduleRunnableScheduled = false;
                return;
            }
            int currentTime = FragmentContextView.this.fragment.getConnectionsManager().getCurrentTime();
            int i = groupCall.call.schedule_date;
            int i2 = i - currentTime;
            if (i2 >= 86400) {
                str = LocaleController.formatPluralString("Days", Math.round(i2 / 86400.0f), new Object[0]);
            } else {
                str = AndroidUtilities.formatFullDuration(i - currentTime);
            }
            String str2 = str;
            int ceil = (int) Math.ceil(FragmentContextView.this.gradientTextPaint.measureText(str2));
            FragmentContextView.this.timeLayout = new StaticLayout(str2, FragmentContextView.this.gradientTextPaint, ceil, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            AndroidUtilities.runOnUIThread(FragmentContextView.this.updateScheduleTimeRunnable, 1000L);
            FragmentContextView.this.frameLayout.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
            FragmentContextView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentContextView.this.checkLocationString();
            AndroidUtilities.runOnUIThread(FragmentContextView.this.checkLocationRunnable, 1000L);
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
        boolean z = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        if (this.isMuted != z) {
            this.isMuted = z;
            this.muteDrawable.setCustomEndFrame(z ? 15 : 29);
            RLottieDrawable rLottieDrawable = this.muteDrawable;
            rLottieDrawable.setCurrentFrame(rLottieDrawable.getCustomEndFrame() - 1, false, true);
            this.muteButton.invalidate();
            Theme.getFragmentContextViewWavesDrawable().updateState(this.visible);
        }
        if (this.isMuted) {
            this.micAmplitude = 0.0f;
            Theme.getFragmentContextViewWavesDrawable().setAmplitude(0.0f);
        }
    }

    public FragmentContextView(Context context, BaseFragment baseFragment, boolean z) {
        this(context, baseFragment, null, z, null);
    }

    public FragmentContextView(Context context, BaseFragment baseFragment, boolean z, Theme.ResourcesProvider resourcesProvider) {
        this(context, baseFragment, null, z, resourcesProvider);
    }

    public FragmentContextView(Context context, BaseFragment baseFragment, View view, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        float f;
        int i;
        this.speedItems = new ActionBarMenuSubItem[4];
        this.currentProgress = -1;
        this.currentStyle = -1;
        this.supportsCalls = true;
        this.rect = new RectF();
        this.updateScheduleTimeRunnable = new AnonymousClass1();
        this.account = UserConfig.selectedAccount;
        this.lastLocationSharingCount = -1;
        this.checkLocationRunnable = new AnonymousClass2();
        this.animationIndex = -1;
        this.resourcesProvider = resourcesProvider;
        this.fragment = baseFragment;
        SizeNotifierFrameLayout sizeNotifierFrameLayout = baseFragment.getFragmentView() instanceof SizeNotifierFrameLayout ? (SizeNotifierFrameLayout) this.fragment.getFragmentView() : null;
        this.applyingView = view;
        this.visible = true;
        this.isLocation = z;
        if (view == null) {
            ((ViewGroup) this.fragment.getFragmentView()).setClipToPadding(false);
        }
        setTag(1);
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, sizeNotifierFrameLayout);
        this.frameLayout = anonymousClass3;
        addView(anonymousClass3, LayoutHelper.createFrame(-1, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        View view2 = new View(context);
        this.selector = view2;
        this.frameLayout.addView(view2, LayoutHelper.createFrame(-1, -1.0f));
        View view3 = new View(context);
        this.shadow = view3;
        view3.setBackgroundResource(2131165275);
        addView(this.shadow, LayoutHelper.createFrame(-1, 2.0f, 51, 0.0f, 36.0f, 0.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.playButton = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        this.playButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("inappPlayerPlayPause"), PorterDuff.Mode.MULTIPLY));
        ImageView imageView2 = this.playButton;
        PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable(14);
        this.playPauseDrawable = playPauseDrawable;
        imageView2.setImageDrawable(playPauseDrawable);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.playButton.setBackground(Theme.createSelectorDrawable(getThemedColor("inappPlayerPlayPause") & 436207615, 1, AndroidUtilities.dp(14.0f)));
        }
        addView(this.playButton, LayoutHelper.createFrame(36, 36, 51));
        this.playButton.setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda3(this));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.importingImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.importingImageView.setAutoRepeat(true);
        this.importingImageView.setAnimation(2131558475, 30, 30);
        this.importingImageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(22.0f), getThemedColor("inappPlayerPlayPause")));
        addView(this.importingImageView, LayoutHelper.createFrame(22, 22.0f, 51, 7.0f, 7.0f, 0.0f, 0.0f));
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, context);
        this.titleTextView = anonymousClass4;
        addView(anonymousClass4, LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, context);
        this.subtitleTextView = anonymousClass5;
        addView(anonymousClass5, LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 10.0f, 36.0f, 0.0f));
        CellFlickerDrawable cellFlickerDrawable = new CellFlickerDrawable();
        this.joinButtonFlicker = cellFlickerDrawable;
        cellFlickerDrawable.setProgress(2.0f);
        this.joinButtonFlicker.repeatEnabled = false;
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(context);
        this.joinButton = anonymousClass6;
        anonymousClass6.setText(LocaleController.getString("VoipChatJoin", 2131629004));
        this.joinButton.setTextColor(getThemedColor("featuredStickers_buttonText"));
        this.joinButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(16.0f), getThemedColor("featuredStickers_addButton"), getThemedColor("featuredStickers_addButtonPressed")));
        this.joinButton.setTextSize(1, 14.0f);
        this.joinButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.joinButton.setGravity(17);
        this.joinButton.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
        addView(this.joinButton, LayoutHelper.createFrame(-2, 28.0f, 53, 0.0f, 10.0f, 14.0f, 0.0f));
        this.joinButton.setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda2(this));
        if (!z) {
            i = 36;
            f = 14.0f;
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, getThemedColor("dialogTextBlack"), resourcesProvider);
            this.playbackSpeedButton = actionBarMenuItem;
            actionBarMenuItem.setLongClickEnabled(false);
            this.playbackSpeedButton.setShowSubmenuByMove(false);
            this.playbackSpeedButton.setContentDescription(LocaleController.getString("AccDescrPlayerSpeed", 2131624043));
            this.playbackSpeedButton.setDelegate(new FragmentContextView$$ExternalSyntheticLambda11(this));
            this.speedItems[0] = this.playbackSpeedButton.addSubItem(1, 2131165943, LocaleController.getString("SpeedSlow", 2131628399));
            this.speedItems[1] = this.playbackSpeedButton.addSubItem(2, 2131165944, LocaleController.getString("SpeedNormal", 2131628398));
            this.speedItems[2] = this.playbackSpeedButton.addSubItem(3, 2131165945, LocaleController.getString("SpeedFast", 2131628397));
            this.speedItems[3] = this.playbackSpeedButton.addSubItem(4, 2131165946, LocaleController.getString("SpeedVeryFast", 2131628400));
            if (AndroidUtilities.density >= 3.0f) {
                this.playbackSpeedButton.setPadding(0, 1, 0, 0);
            }
            this.playbackSpeedButton.setAdditionalXOffset(AndroidUtilities.dp(8.0f));
            addView(this.playbackSpeedButton, LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 0.0f, 36.0f, 0.0f));
            this.playbackSpeedButton.setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda4(this));
            this.playbackSpeedButton.setOnLongClickListener(new FragmentContextView$$ExternalSyntheticLambda8(this));
            updatePlaybackButton();
        } else {
            i = 36;
            f = 14.0f;
        }
        AvatarsImageView avatarsImageView = new AvatarsImageView(context, false);
        this.avatars = avatarsImageView;
        avatarsImageView.setDelegate(new FragmentContextView$$ExternalSyntheticLambda10(this));
        this.avatars.setVisibility(8);
        addView(this.avatars, LayoutHelper.createFrame(108, i, 51));
        this.muteDrawable = new RLottieDrawable(2131558577, "2131558577", AndroidUtilities.dp(16.0f), AndroidUtilities.dp(20.0f), true, null);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(context);
        this.muteButton = anonymousClass7;
        anonymousClass7.setColorFilter(new PorterDuffColorFilter(getThemedColor("returnToCallText"), PorterDuff.Mode.MULTIPLY));
        if (i2 >= 21) {
            this.muteButton.setBackground(Theme.createSelectorDrawable(getThemedColor("inappPlayerClose") & 436207615, 1, AndroidUtilities.dp(f)));
        }
        this.muteButton.setAnimation(this.muteDrawable);
        this.muteButton.setScaleType(ImageView.ScaleType.CENTER);
        this.muteButton.setVisibility(8);
        addView(this.muteButton, LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 0.0f, 2.0f, 0.0f));
        this.muteButton.setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda5(this));
        ImageView imageView3 = new ImageView(context);
        this.closeButton = imageView3;
        imageView3.setImageResource(2131165619);
        this.closeButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("inappPlayerClose"), PorterDuff.Mode.MULTIPLY));
        if (i2 >= 21) {
            this.closeButton.setBackground(Theme.createSelectorDrawable(getThemedColor("inappPlayerClose") & 436207615, 1, AndroidUtilities.dp(f)));
        }
        this.closeButton.setScaleType(ImageView.ScaleType.CENTER);
        addView(this.closeButton, LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 0.0f, 2.0f, 0.0f));
        this.closeButton.setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda6(this, resourcesProvider));
        setOnClickListener(new FragmentContextView$$ExternalSyntheticLambda7(this, resourcesProvider, baseFragment));
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends BlurredFrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout) {
            super(context, sizeNotifierFrameLayout);
            FragmentContextView.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            if (FragmentContextView.this.avatars == null || FragmentContextView.this.avatars.getVisibility() != 0) {
                return;
            }
            FragmentContextView.this.avatars.invalidate();
        }

        @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            float f;
            super.dispatchDraw(canvas);
            if (FragmentContextView.this.currentStyle != 4 || FragmentContextView.this.timeLayout == null) {
                return;
            }
            int ceil = ((int) Math.ceil(FragmentContextView.this.timeLayout.getLineWidth(0))) + AndroidUtilities.dp(24.0f);
            if (ceil != FragmentContextView.this.gradientWidth) {
                FragmentContextView.this.linearGradient = new LinearGradient(0.0f, 0.0f, 1.7f * ceil, 0.0f, new int[]{-10187532, -7575089, -2860679, -2860679}, new float[]{0.0f, 0.294f, 0.588f, 1.0f}, Shader.TileMode.CLAMP);
                FragmentContextView.this.gradientPaint.setShader(FragmentContextView.this.linearGradient);
                FragmentContextView.this.gradientWidth = ceil;
            }
            ChatObject.Call groupCall = ((ChatActivity) FragmentContextView.this.fragment).getGroupCall();
            if (FragmentContextView.this.fragment == null || groupCall == null || !groupCall.isScheduled()) {
                f = 0.0f;
            } else {
                long currentTimeMillis = (groupCall.call.schedule_date * 1000) - FragmentContextView.this.fragment.getConnectionsManager().getCurrentTimeMillis();
                f = 1.0f;
                if (currentTimeMillis >= 0) {
                    f = currentTimeMillis < 5000 ? 1.0f - (((float) currentTimeMillis) / 5000.0f) : 0.0f;
                }
                if (currentTimeMillis < 6000) {
                    invalidate();
                }
            }
            FragmentContextView.this.matrix.reset();
            FragmentContextView.this.matrix.postTranslate((-FragmentContextView.this.gradientWidth) * 0.7f * f, 0.0f);
            FragmentContextView.this.linearGradient.setLocalMatrix(FragmentContextView.this.matrix);
            int measuredWidth = (getMeasuredWidth() - ceil) - AndroidUtilities.dp(10.0f);
            int dp = AndroidUtilities.dp(10.0f);
            FragmentContextView.this.rect.set(0.0f, 0.0f, ceil, AndroidUtilities.dp(28.0f));
            canvas.save();
            canvas.translate(measuredWidth, dp);
            canvas.drawRoundRect(FragmentContextView.this.rect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), FragmentContextView.this.gradientPaint);
            canvas.translate(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(6.0f));
            FragmentContextView.this.timeLayout.draw(canvas);
            canvas.restore();
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        if (this.currentStyle == 0) {
            if (MediaController.getInstance().isMessagePaused()) {
                MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
            } else {
                MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends AudioPlayerAlert.ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, Context context2) {
            super(context);
            FragmentContextView.this = r1;
            this.val$context = context2;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            TextView textView = new TextView(this.val$context);
            textView.setMaxLines(1);
            textView.setLines(1);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextSize(1, 15.0f);
            textView.setGravity(19);
            if (FragmentContextView.this.currentStyle != 0 && FragmentContextView.this.currentStyle != 2) {
                if (FragmentContextView.this.currentStyle != 4) {
                    if (FragmentContextView.this.currentStyle == 1 || FragmentContextView.this.currentStyle == 3) {
                        textView.setGravity(19);
                        textView.setTextColor(FragmentContextView.this.getThemedColor("returnToCallText"));
                        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textView.setTextSize(1, 14.0f);
                    }
                } else {
                    textView.setGravity(51);
                    textView.setTextColor(FragmentContextView.this.getThemedColor("inappPlayerPerformer"));
                    textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    textView.setTextSize(1, 15.0f);
                }
            } else {
                textView.setGravity(19);
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(1, 15.0f);
            }
            return textView;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AudioPlayerAlert.ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, Context context2) {
            super(context);
            FragmentContextView.this = r1;
            this.val$context = context2;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            TextView textView = new TextView(this.val$context);
            textView.setMaxLines(1);
            textView.setLines(1);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(3);
            textView.setTextSize(1, 13.0f);
            textView.setTextColor(FragmentContextView.this.getThemedColor("inappPlayerClose"));
            return textView;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends TextView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            FragmentContextView.this = r1;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
            int dp = AndroidUtilities.dp(1.0f);
            RectF rectF = AndroidUtilities.rectTmp;
            float f = dp;
            rectF.set(f, f, getWidth() - dp, getHeight() - dp);
            FragmentContextView.this.joinButtonFlicker.draw(canvas, rectF, AndroidUtilities.dp(16.0f), this);
            if (FragmentContextView.this.joinButtonFlicker.getProgress() >= 1.0f || FragmentContextView.this.joinButtonFlicker.repeatEnabled) {
                return;
            }
            invalidate();
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            FragmentContextView.this.joinButtonFlicker.setParentWidth(getWidth());
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        callOnClick();
    }

    public /* synthetic */ void lambda$new$2(int i) {
        float playbackSpeed = MediaController.getInstance().getPlaybackSpeed(this.isMusic);
        if (i == 1) {
            MediaController.getInstance().setPlaybackSpeed(this.isMusic, 0.5f);
        } else if (i == 2) {
            MediaController.getInstance().setPlaybackSpeed(this.isMusic, 1.0f);
        } else if (i == 3) {
            MediaController.getInstance().setPlaybackSpeed(this.isMusic, 1.5f);
        } else {
            MediaController.getInstance().setPlaybackSpeed(this.isMusic, 1.8f);
        }
        float playbackSpeed2 = MediaController.getInstance().getPlaybackSpeed(this.isMusic);
        if (playbackSpeed != playbackSpeed2) {
            playbackSpeedChanged(playbackSpeed2);
        }
        updatePlaybackButton();
    }

    public /* synthetic */ void lambda$new$3(View view) {
        float f = 1.0f;
        if (Math.abs(MediaController.getInstance().getPlaybackSpeed(this.isMusic) - 1.0f) > 0.001f) {
            MediaController.getInstance().setPlaybackSpeed(this.isMusic, 1.0f);
        } else {
            MediaController mediaController = MediaController.getInstance();
            boolean z = this.isMusic;
            float fastPlaybackSpeed = MediaController.getInstance().getFastPlaybackSpeed(this.isMusic);
            mediaController.setPlaybackSpeed(z, fastPlaybackSpeed);
            f = fastPlaybackSpeed;
        }
        playbackSpeedChanged(f);
    }

    public /* synthetic */ boolean lambda$new$4(View view) {
        this.playbackSpeedButton.toggleSubMenu();
        return true;
    }

    public /* synthetic */ void lambda$new$5() {
        updateAvatars(true);
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends RLottieImageView {
        boolean pressed;
        boolean scheduled;
        private final Runnable toggleMicRunnable = new FragmentContextView$7$$ExternalSyntheticLambda0(this);
        private final Runnable pressRunnable = new FragmentContextView$7$$ExternalSyntheticLambda1(this);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context) {
            super(context);
            FragmentContextView.this = r1;
        }

        public /* synthetic */ void lambda$$0() {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            VoIPService.getSharedInstance().setMicMute(false, true, false);
            if (FragmentContextView.this.muteDrawable.setCustomEndFrame(FragmentContextView.this.isMuted ? 15 : 29)) {
                if (FragmentContextView.this.isMuted) {
                    FragmentContextView.this.muteDrawable.setCurrentFrame(0);
                } else {
                    FragmentContextView.this.muteDrawable.setCurrentFrame(14);
                }
            }
            FragmentContextView.this.muteButton.playAnimation();
            Theme.getFragmentContextViewWavesDrawable().updateState(true);
        }

        public /* synthetic */ void lambda$$1() {
            if (!this.scheduled || VoIPService.getSharedInstance() == null) {
                return;
            }
            this.scheduled = false;
            this.pressed = true;
            FragmentContextView.this.isMuted = false;
            AndroidUtilities.runOnUIThread(this.toggleMicRunnable, 90L);
            FragmentContextView.this.muteButton.performHapticFeedback(3, 2);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (FragmentContextView.this.currentStyle == 3 || FragmentContextView.this.currentStyle == 1) {
                VoIPService sharedInstance = VoIPService.getSharedInstance();
                if (sharedInstance == null) {
                    AndroidUtilities.cancelRunOnUIThread(this.pressRunnable);
                    AndroidUtilities.cancelRunOnUIThread(this.toggleMicRunnable);
                    this.scheduled = false;
                    this.pressed = false;
                    return true;
                }
                if (motionEvent.getAction() == 0 && sharedInstance.isMicMute()) {
                    AndroidUtilities.runOnUIThread(this.pressRunnable, 300L);
                    this.scheduled = true;
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    AndroidUtilities.cancelRunOnUIThread(this.toggleMicRunnable);
                    if (this.scheduled) {
                        AndroidUtilities.cancelRunOnUIThread(this.pressRunnable);
                        this.scheduled = false;
                    } else if (this.pressed) {
                        FragmentContextView.this.isMuted = true;
                        if (FragmentContextView.this.muteDrawable.setCustomEndFrame(15)) {
                            if (FragmentContextView.this.isMuted) {
                                FragmentContextView.this.muteDrawable.setCurrentFrame(0);
                            } else {
                                FragmentContextView.this.muteDrawable.setCurrentFrame(14);
                            }
                        }
                        FragmentContextView.this.muteButton.playAnimation();
                        if (VoIPService.getSharedInstance() != null) {
                            VoIPService.getSharedInstance().setMicMute(true, true, false);
                            FragmentContextView.this.muteButton.performHapticFeedback(3, 2);
                        }
                        this.pressed = false;
                        Theme.getFragmentContextViewWavesDrawable().updateState(true);
                        MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                        super.onTouchEvent(obtain);
                        obtain.recycle();
                        return true;
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            String str;
            int i;
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(Button.class.getName());
            if (FragmentContextView.this.isMuted) {
                i = 2131629200;
                str = "VoipUnmute";
            } else {
                i = 2131629142;
                str = "VoipMute";
            }
            accessibilityNodeInfo.setText(LocaleController.getString(str, i));
        }
    }

    public /* synthetic */ void lambda$new$6(View view) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        if (sharedInstance.groupCall != null) {
            AccountInstance.getInstance(sharedInstance.getAccount());
            ChatObject.Call call = sharedInstance.groupCall;
            TLRPC$Chat chat = sharedInstance.getChat();
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = call.participants.get(sharedInstance.getSelfId());
            if (tLRPC$TL_groupCallParticipant != null && !tLRPC$TL_groupCallParticipant.can_self_unmute && tLRPC$TL_groupCallParticipant.muted && !ChatObject.canManageCalls(chat)) {
                return;
            }
        }
        boolean z = !sharedInstance.isMicMute();
        this.isMuted = z;
        sharedInstance.setMicMute(z, false, true);
        if (this.muteDrawable.setCustomEndFrame(this.isMuted ? 15 : 29)) {
            if (this.isMuted) {
                this.muteDrawable.setCurrentFrame(0);
            } else {
                this.muteDrawable.setCurrentFrame(14);
            }
        }
        this.muteButton.playAnimation();
        Theme.getFragmentContextViewWavesDrawable().updateState(true);
        this.muteButton.performHapticFeedback(3, 2);
    }

    public /* synthetic */ void lambda$new$8(Theme.ResourcesProvider resourcesProvider, View view) {
        if (this.currentStyle == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.fragment.getParentActivity(), resourcesProvider);
            builder.setTitle(LocaleController.getString("StopLiveLocationAlertToTitle", 2131628469));
            BaseFragment baseFragment = this.fragment;
            if (baseFragment instanceof DialogsActivity) {
                builder.setMessage(LocaleController.getString("StopLiveLocationAlertAllText", 2131628467));
            } else {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                TLRPC$Chat currentChat = chatActivity.getCurrentChat();
                TLRPC$User currentUser = chatActivity.getCurrentUser();
                if (currentChat != null) {
                    builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("StopLiveLocationAlertToGroupText", 2131628468, currentChat.title)));
                } else if (currentUser != null) {
                    builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("StopLiveLocationAlertToUserText", 2131628470, UserObject.getFirstName(currentUser))));
                } else {
                    builder.setMessage(LocaleController.getString("AreYouSure", 2131624421));
                }
            }
            builder.setPositiveButton(LocaleController.getString("Stop", 2131628463), new FragmentContextView$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            AlertDialog create = builder.create();
            builder.show();
            TextView textView = (TextView) create.getButton(-1);
            if (textView == null) {
                return;
            }
            textView.setTextColor(getThemedColor("dialogTextRed2"));
            return;
        }
        MediaController.getInstance().cleanupPlayer(true, true);
    }

    public /* synthetic */ void lambda$new$7(DialogInterface dialogInterface, int i) {
        BaseFragment baseFragment = this.fragment;
        if (!(baseFragment instanceof DialogsActivity)) {
            LocationController.getInstance(baseFragment.getCurrentAccount()).removeSharingLocation(((ChatActivity) this.fragment).getDialogId());
            return;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            LocationController.getInstance(i2).removeAllLocationSharings();
        }
    }

    public /* synthetic */ void lambda$new$10(Theme.ResourcesProvider resourcesProvider, BaseFragment baseFragment, View view) {
        ChatActivity chatActivity;
        ChatObject.Call groupCall;
        long j;
        int i = this.currentStyle;
        long j2 = 0;
        if (i == 0) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (this.fragment == null || playingMessageObject == null) {
                return;
            }
            if (playingMessageObject.isMusic()) {
                if (!(getContext() instanceof LaunchActivity)) {
                    return;
                }
                this.fragment.showDialog(new AudioPlayerAlert(getContext(), resourcesProvider));
                return;
            }
            BaseFragment baseFragment2 = this.fragment;
            if (baseFragment2 instanceof ChatActivity) {
                j2 = ((ChatActivity) baseFragment2).getDialogId();
            }
            if (playingMessageObject.getDialogId() == j2) {
                ((ChatActivity) this.fragment).scrollToMessageId(playingMessageObject.getId(), 0, false, 0, true, 0);
                return;
            }
            long dialogId = playingMessageObject.getDialogId();
            Bundle bundle = new Bundle();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                bundle.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            } else if (DialogObject.isUserDialog(dialogId)) {
                bundle.putLong("user_id", dialogId);
            } else {
                bundle.putLong("chat_id", -dialogId);
            }
            bundle.putInt("message_id", playingMessageObject.getId());
            this.fragment.presentFragment(new ChatActivity(bundle), this.fragment instanceof ChatActivity);
            return;
        }
        boolean z = true;
        if (i == 1) {
            getContext().startActivity(new Intent(getContext(), LaunchActivity.class).setAction("voip"));
        } else if (i == 2) {
            int i2 = UserConfig.selectedAccount;
            BaseFragment baseFragment3 = this.fragment;
            if (baseFragment3 instanceof ChatActivity) {
                j = ((ChatActivity) baseFragment3).getDialogId();
                i2 = this.fragment.getCurrentAccount();
            } else {
                if (LocationController.getLocationsCount() == 1) {
                    for (int i3 = 0; i3 < 4; i3++) {
                        if (!LocationController.getInstance(i3).sharingLocationsUI.isEmpty()) {
                            LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(i3).sharingLocationsUI.get(0);
                            j = sharingLocationInfo.did;
                            i2 = sharingLocationInfo.messageObject.currentAccount;
                            break;
                        }
                    }
                }
                j = 0;
            }
            if (j != 0) {
                openSharingLocation(LocationController.getInstance(i2).getSharingLocationInfo(j));
            } else {
                this.fragment.showDialog(new SharingLocationsAlert(getContext(), new FragmentContextView$$ExternalSyntheticLambda12(this), resourcesProvider));
            }
        } else if (i == 3) {
            if (VoIPService.getSharedInstance() == null || !(getContext() instanceof LaunchActivity)) {
                return;
            }
            GroupCallActivity.create((LaunchActivity) getContext(), AccountInstance.getInstance(VoIPService.getSharedInstance().getAccount()), null, null, false, null);
        } else if (i == 4) {
            if (this.fragment.getParentActivity() == null || (groupCall = (chatActivity = (ChatActivity) this.fragment).getGroupCall()) == null) {
                return;
            }
            TLRPC$Chat chat = chatActivity.getMessagesController().getChat(Long.valueOf(groupCall.chatId));
            TLRPC$GroupCall tLRPC$GroupCall = groupCall.call;
            if (tLRPC$GroupCall == null || tLRPC$GroupCall.rtmp_stream) {
                z = false;
            }
            Boolean valueOf = Boolean.valueOf(z);
            Activity parentActivity = this.fragment.getParentActivity();
            BaseFragment baseFragment4 = this.fragment;
            VoIPHelper.startCall(chat, null, null, false, valueOf, parentActivity, baseFragment4, baseFragment4.getAccountInstance());
        } else if (i == 5 && baseFragment.getSendMessagesHelper().getImportingHistory(((ChatActivity) baseFragment).getDialogId()) != null) {
            ImportingAlert importingAlert = new ImportingAlert(getContext(), null, (ChatActivity) this.fragment, resourcesProvider);
            importingAlert.setOnHideListener(new FragmentContextView$$ExternalSyntheticLambda1(this));
            this.fragment.showDialog(importingAlert);
            checkImport(false);
        }
    }

    public /* synthetic */ void lambda$new$9(DialogInterface dialogInterface) {
        checkImport(false);
    }

    public void setSupportsCalls(boolean z) {
        this.supportsCalls = z;
    }

    public void setDelegate(FragmentContextViewDelegate fragmentContextViewDelegate) {
        this.delegate = fragmentContextViewDelegate;
    }

    private void updatePlaybackButton() {
        if (this.playbackSpeedButton == null) {
            return;
        }
        float playbackSpeed = MediaController.getInstance().getPlaybackSpeed(this.isMusic);
        float fastPlaybackSpeed = MediaController.getInstance().getFastPlaybackSpeed(this.isMusic);
        if (Math.abs(fastPlaybackSpeed - 1.8f) < 0.001f) {
            this.playbackSpeedButton.setIcon(2131166210);
        } else if (Math.abs(fastPlaybackSpeed - 1.5f) < 0.001f) {
            this.playbackSpeedButton.setIcon(2131166209);
        } else {
            this.playbackSpeedButton.setIcon(2131166208);
        }
        updateColors();
        for (int i = 0; i < this.speedItems.length; i++) {
            if ((i == 0 && Math.abs(playbackSpeed - 0.5f) < 0.001f) || ((i == 1 && Math.abs(playbackSpeed - 1.0f) < 0.001f) || ((i == 2 && Math.abs(playbackSpeed - 1.5f) < 0.001f) || (i == 3 && Math.abs(playbackSpeed - 1.8f) < 0.001f)))) {
                this.speedItems[i].setColors(getThemedColor("inappPlayerPlayPause"), getThemedColor("inappPlayerPlayPause"));
            } else {
                this.speedItems[i].setColors(getThemedColor("actionBarDefaultSubmenuItem"), getThemedColor("actionBarDefaultSubmenuItemIcon"));
            }
        }
    }

    public void updateColors() {
        if (this.playbackSpeedButton != null) {
            String str = Math.abs(MediaController.getInstance().getPlaybackSpeed(this.isMusic) - 1.0f) > 0.001f ? "inappPlayerPlayPause" : "inappPlayerClose";
            this.playbackSpeedButton.setIconColor(getThemedColor(str));
            if (Build.VERSION.SDK_INT < 21) {
                return;
            }
            this.playbackSpeedButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str) & 436207615, 1, AndroidUtilities.dp(14.0f)));
        }
    }

    public void setAdditionalContextView(FragmentContextView fragmentContextView) {
        this.additionalContextView = fragmentContextView;
    }

    public void openSharingLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
        if (sharingLocationInfo == null || !(this.fragment.getParentActivity() instanceof LaunchActivity)) {
            return;
        }
        LaunchActivity launchActivity = (LaunchActivity) this.fragment.getParentActivity();
        launchActivity.switchToAccount(sharingLocationInfo.messageObject.currentAccount, true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        locationActivity.setDelegate(new FragmentContextView$$ExternalSyntheticLambda13(sharingLocationInfo, sharingLocationInfo.messageObject.getDialogId()));
        launchActivity.lambda$runLinkRequest$59(locationActivity);
    }

    public static /* synthetic */ void lambda$openSharingLocation$11(LocationController.SharingLocationInfo sharingLocationInfo, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(sharingLocationInfo.messageObject.currentAccount).sendMessage(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
    }

    @Keep
    public float getTopPadding() {
        return this.topPadding;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a3, code lost:
        if (r0.getId() != 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0010, code lost:
        if (org.telegram.messenger.LocationController.getLocationsCount() != 0) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkVisibility() {
        int i = 0;
        boolean z = true;
        if (this.isLocation) {
            BaseFragment baseFragment = this.fragment;
            if (!(baseFragment instanceof DialogsActivity)) {
                z = LocationController.getInstance(baseFragment.getCurrentAccount()).isSharingLocation(((ChatActivity) this.fragment).getDialogId());
            }
        } else if (VoIPService.getSharedInstance() != null && !VoIPService.getSharedInstance().isHangingUp() && VoIPService.getSharedInstance().getCallState() != 15) {
            startJoinFlickerAnimation();
        } else {
            BaseFragment baseFragment2 = this.fragment;
            if (!(baseFragment2 instanceof ChatActivity) || baseFragment2.getSendMessagesHelper().getImportingHistory(((ChatActivity) this.fragment).getDialogId()) == null || isPlayingVoice()) {
                BaseFragment baseFragment3 = this.fragment;
                if ((baseFragment3 instanceof ChatActivity) && ((ChatActivity) baseFragment3).getGroupCall() != null && ((ChatActivity) this.fragment).getGroupCall().shouldShowPanel() && !GroupCallPip.isShowing() && !isPlayingVoice()) {
                    startJoinFlickerAnimation();
                } else {
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject != null) {
                    }
                    z = false;
                }
            }
        }
        if (!z) {
            i = 8;
        }
        setVisibility(i);
    }

    @Keep
    public void setTopPadding(float f) {
        this.topPadding = f;
        if (this.fragment == null || getParent() == null) {
            return;
        }
        View view = this.applyingView;
        if (view == null) {
            view = this.fragment.getFragmentView();
        }
        FragmentContextView fragmentContextView = this.additionalContextView;
        int dp = (fragmentContextView == null || fragmentContextView.getVisibility() != 0 || this.additionalContextView.getParent() == null) ? 0 : AndroidUtilities.dp(this.additionalContextView.getStyleHeight());
        if (view == null || getParent() == null) {
            return;
        }
        view.setPadding(0, ((int) (getVisibility() == 0 ? this.topPadding : 0.0f)) + dp, 0, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:62:0x01c9, code lost:
        if (r1.getGroupCall().call.rtmp_stream != false) goto L64;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateStyle(int i) {
        int i2 = this.currentStyle;
        if (i2 == i) {
            return;
        }
        boolean z = true;
        if (i2 == 3 || i2 == 1) {
            Theme.getFragmentContextViewWavesDrawable().removeParent(this);
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().unregisterStateListener(this);
            }
        }
        this.currentStyle = i;
        this.frameLayout.setWillNotDraw(i != 4);
        if (i != 4) {
            this.timeLayout = null;
        }
        AvatarsImageView avatarsImageView = this.avatars;
        if (avatarsImageView != null) {
            avatarsImageView.setStyle(this.currentStyle);
            this.avatars.setLayoutParams(LayoutHelper.createFrame(108, getStyleHeight(), 51));
        }
        this.frameLayout.setLayoutParams(LayoutHelper.createFrame(-1, getStyleHeight(), 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.shadow.setLayoutParams(LayoutHelper.createFrame(-1, 2.0f, 51, 0.0f, getStyleHeight(), 0.0f, 0.0f));
        float f = this.topPadding;
        if (f > 0.0f && f != AndroidUtilities.dp2(getStyleHeight())) {
            updatePaddings();
            setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
        }
        if (i == 5) {
            this.selector.setBackground(Theme.getSelectorDrawable(false));
            this.frameLayout.setBackgroundColor(getThemedColor("inappPlayerBackground"));
            this.frameLayout.setTag("inappPlayerBackground");
            int i3 = 0;
            while (i3 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = this.titleTextView;
                TextView textView = i3 == 0 ? clippingTextViewSwitcher.getTextView() : clippingTextViewSwitcher.getNextTextView();
                if (textView != null) {
                    textView.setGravity(19);
                    textView.setTextColor(getThemedColor("inappPlayerTitle"));
                    textView.setTypeface(Typeface.DEFAULT);
                    textView.setTextSize(1, 15.0f);
                }
                i3++;
            }
            this.titleTextView.setTag("inappPlayerTitle");
            this.subtitleTextView.setVisibility(8);
            this.joinButton.setVisibility(8);
            this.closeButton.setVisibility(8);
            this.playButton.setVisibility(8);
            this.muteButton.setVisibility(8);
            this.avatars.setVisibility(8);
            this.importingImageView.setVisibility(0);
            this.importingImageView.playAnimation();
            this.closeButton.setContentDescription(LocaleController.getString("AccDescrClosePlayer", 2131623977));
            ActionBarMenuItem actionBarMenuItem = this.playbackSpeedButton;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility(8);
            }
            this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
        } else if (i == 0 || i == 2) {
            this.selector.setBackground(Theme.getSelectorDrawable(false));
            this.frameLayout.setBackgroundColor(getThemedColor("inappPlayerBackground"));
            this.frameLayout.setTag("inappPlayerBackground");
            this.subtitleTextView.setVisibility(8);
            this.joinButton.setVisibility(8);
            this.closeButton.setVisibility(0);
            this.playButton.setVisibility(0);
            this.muteButton.setVisibility(8);
            this.importingImageView.setVisibility(8);
            this.importingImageView.stopAnimation();
            this.avatars.setVisibility(8);
            int i4 = 0;
            while (i4 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher2 = this.titleTextView;
                TextView textView2 = i4 == 0 ? clippingTextViewSwitcher2.getTextView() : clippingTextViewSwitcher2.getNextTextView();
                if (textView2 != null) {
                    textView2.setGravity(19);
                    textView2.setTextColor(getThemedColor("inappPlayerTitle"));
                    textView2.setTypeface(Typeface.DEFAULT);
                    textView2.setTextSize(1, 15.0f);
                }
                i4++;
            }
            this.titleTextView.setTag("inappPlayerTitle");
            if (i == 0) {
                this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
                ActionBarMenuItem actionBarMenuItem2 = this.playbackSpeedButton;
                if (actionBarMenuItem2 != null) {
                    actionBarMenuItem2.setVisibility(0);
                }
                this.closeButton.setContentDescription(LocaleController.getString("AccDescrClosePlayer", 2131623977));
                return;
            }
            this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0f, 51, 8.0f, 0.0f, 0.0f, 0.0f));
            this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 51.0f, 0.0f, 36.0f, 0.0f));
            this.closeButton.setContentDescription(LocaleController.getString("AccDescrStopLiveLocation", 2131624090));
        } else if (i == 4) {
            this.selector.setBackground(Theme.getSelectorDrawable(false));
            this.frameLayout.setBackgroundColor(getThemedColor("inappPlayerBackground"));
            this.frameLayout.setTag("inappPlayerBackground");
            this.muteButton.setVisibility(8);
            this.subtitleTextView.setVisibility(0);
            int i5 = 0;
            while (i5 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher3 = this.titleTextView;
                TextView textView3 = i5 == 0 ? clippingTextViewSwitcher3.getTextView() : clippingTextViewSwitcher3.getNextTextView();
                if (textView3 != null) {
                    textView3.setGravity(51);
                    textView3.setTextColor(getThemedColor("inappPlayerPerformer"));
                    textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    textView3.setTextSize(1, 15.0f);
                }
                i5++;
            }
            this.titleTextView.setTag("inappPlayerPerformer");
            this.titleTextView.setPadding(0, 0, 0, 0);
            this.importingImageView.setVisibility(8);
            this.importingImageView.stopAnimation();
            BaseFragment baseFragment = this.fragment;
            if (baseFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                if (chatActivity.getGroupCall() != null) {
                    if (chatActivity.getGroupCall().call != null) {
                    }
                }
            }
            z = false;
            this.avatars.setVisibility(!z ? 0 : 8);
            if (this.avatars.getVisibility() != 8) {
                updateAvatars(false);
            } else {
                this.titleTextView.setTranslationX(-AndroidUtilities.dp(36.0f));
                this.subtitleTextView.setTranslationX(-AndroidUtilities.dp(36.0f));
            }
            this.closeButton.setVisibility(8);
            this.playButton.setVisibility(8);
            ActionBarMenuItem actionBarMenuItem3 = this.playbackSpeedButton;
            if (actionBarMenuItem3 == null) {
                return;
            }
            actionBarMenuItem3.setVisibility(8);
        } else if (i != 1 && i != 3) {
        } else {
            this.selector.setBackground(null);
            updateCallTitle();
            boolean hasRtmpStream = VoIPService.hasRtmpStream();
            this.avatars.setVisibility(!hasRtmpStream ? 0 : 8);
            if (i == 3 && VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().registerStateListener(this);
            }
            if (this.avatars.getVisibility() != 8) {
                updateAvatars(false);
            } else {
                this.titleTextView.setTranslationX(0.0f);
                this.subtitleTextView.setTranslationX(0.0f);
            }
            this.muteButton.setVisibility(!hasRtmpStream ? 0 : 8);
            boolean z2 = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
            this.isMuted = z2;
            this.muteDrawable.setCustomEndFrame(z2 ? 15 : 29);
            RLottieDrawable rLottieDrawable = this.muteDrawable;
            rLottieDrawable.setCurrentFrame(rLottieDrawable.getCustomEndFrame() - 1, false, true);
            this.muteButton.invalidate();
            this.frameLayout.setBackground(null);
            this.frameLayout.setBackgroundColor(0);
            this.importingImageView.setVisibility(8);
            this.importingImageView.stopAnimation();
            Theme.getFragmentContextViewWavesDrawable().addParent(this);
            invalidate();
            int i6 = 0;
            while (i6 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher4 = this.titleTextView;
                TextView textView4 = i6 == 0 ? clippingTextViewSwitcher4.getTextView() : clippingTextViewSwitcher4.getNextTextView();
                if (textView4 != null) {
                    textView4.setGravity(19);
                    textView4.setTextColor(getThemedColor("returnToCallText"));
                    textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    textView4.setTextSize(1, 14.0f);
                }
                i6++;
            }
            this.titleTextView.setTag("returnToCallText");
            this.closeButton.setVisibility(8);
            this.playButton.setVisibility(8);
            this.subtitleTextView.setVisibility(8);
            this.joinButton.setVisibility(8);
            this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 2.0f));
            this.titleTextView.setPadding(AndroidUtilities.dp(112.0f), 0, AndroidUtilities.dp(112.0f), 0);
            ActionBarMenuItem actionBarMenuItem4 = this.playbackSpeedButton;
            if (actionBarMenuItem4 == null) {
                return;
            }
            actionBarMenuItem4.setVisibility(8);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        if (this.scheduleRunnableScheduled) {
            AndroidUtilities.cancelRunOnUIThread(this.updateScheduleTimeRunnable);
            this.scheduleRunnableScheduled = false;
        }
        this.visible = false;
        NotificationCenter.getInstance(this.account).onAnimationFinish(this.animationIndex);
        this.topPadding = 0.0f;
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsCacheChanged);
        } else {
            for (int i = 0; i < 4; i++) {
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidStart);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.groupCallUpdated);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.groupCallTypingsUpdated);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.historyImportProgressChanged);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.messagePlayingSpeedChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didStartedCall);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndCall);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcSpeakerAmplitudeEvent);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.groupCallVisibilityChanged);
        }
        int i2 = this.currentStyle;
        if (i2 == 3 || i2 == 1) {
            Theme.getFragmentContextViewWavesDrawable().removeParent(this);
        }
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener(this);
        }
        this.wasDraw = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int i = 15;
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsCacheChanged);
            FragmentContextView fragmentContextView = this.additionalContextView;
            if (fragmentContextView != null) {
                fragmentContextView.checkVisibility();
            }
            checkLiveLocation(true);
        } else {
            for (int i2 = 0; i2 < 4; i2++) {
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.messagePlayingDidStart);
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.groupCallUpdated);
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.groupCallTypingsUpdated);
                NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.historyImportProgressChanged);
            }
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.messagePlayingSpeedChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didStartedCall);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndCall);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcSpeakerAmplitudeEvent);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.groupCallVisibilityChanged);
            FragmentContextView fragmentContextView2 = this.additionalContextView;
            if (fragmentContextView2 != null) {
                fragmentContextView2.checkVisibility();
            }
            if (VoIPService.getSharedInstance() != null && !VoIPService.getSharedInstance().isHangingUp() && VoIPService.getSharedInstance().getCallState() != 15 && !GroupCallPip.isShowing()) {
                checkCall(true);
            } else {
                BaseFragment baseFragment = this.fragment;
                if ((baseFragment instanceof ChatActivity) && baseFragment.getSendMessagesHelper().getImportingHistory(((ChatActivity) this.fragment).getDialogId()) != null && !isPlayingVoice()) {
                    checkImport(true);
                } else {
                    BaseFragment baseFragment2 = this.fragment;
                    if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).getGroupCall() != null && ((ChatActivity) this.fragment).getGroupCall().shouldShowPanel() && !GroupCallPip.isShowing() && !isPlayingVoice()) {
                        checkCall(true);
                    } else {
                        checkCall(true);
                        checkPlayer(true);
                        updatePlaybackButton();
                    }
                }
            }
        }
        int i3 = this.currentStyle;
        if (i3 == 3 || i3 == 1) {
            Theme.getFragmentContextViewWavesDrawable().addParent(this);
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().registerStateListener(this);
            }
            boolean z = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
            if (this.isMuted != z) {
                this.isMuted = z;
                RLottieDrawable rLottieDrawable = this.muteDrawable;
                if (!z) {
                    i = 29;
                }
                rLottieDrawable.setCustomEndFrame(i);
                RLottieDrawable rLottieDrawable2 = this.muteDrawable;
                rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getCustomEndFrame() - 1, false, true);
                this.muteButton.invalidate();
            }
        } else if (i3 == 4 && !this.scheduleRunnableScheduled) {
            this.scheduleRunnableScheduled = true;
            this.updateScheduleTimeRunnable.run();
        }
        if (this.visible && this.topPadding == 0.0f) {
            updatePaddings();
            setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
        }
        this.speakerAmplitude = 0.0f;
        this.micAmplitude = 0.0f;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, AndroidUtilities.dp2(getStyleHeight() + 2));
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        VoIPService sharedInstance;
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant;
        if (i == NotificationCenter.liveLocationsChanged) {
            checkLiveLocation(false);
        } else if (i == NotificationCenter.liveLocationsCacheChanged) {
            if (!(this.fragment instanceof ChatActivity)) {
                return;
            }
            if (((ChatActivity) this.fragment).getDialogId() != ((Long) objArr[0]).longValue()) {
                return;
            }
            checkLocationString();
        } else if (i == NotificationCenter.messagePlayingDidStart || i == NotificationCenter.messagePlayingPlayStateChanged || i == NotificationCenter.messagePlayingDidReset || i == NotificationCenter.didEndCall) {
            int i3 = this.currentStyle;
            if (i3 == 1 || i3 == 3 || i3 == 4) {
                checkCall(false);
            }
            checkPlayer(false);
        } else {
            int i4 = NotificationCenter.didStartedCall;
            if (i == i4 || i == NotificationCenter.groupCallUpdated || i == NotificationCenter.groupCallVisibilityChanged) {
                checkCall(false);
                if (this.currentStyle != 3 || (sharedInstance = VoIPService.getSharedInstance()) == null || sharedInstance.groupCall == null) {
                    return;
                }
                if (i == i4) {
                    sharedInstance.registerStateListener(this);
                }
                int callState = sharedInstance.getCallState();
                if (callState == 1 || callState == 2 || callState == 6 || callState == 5 || (tLRPC$TL_groupCallParticipant = sharedInstance.groupCall.participants.get(sharedInstance.getSelfId())) == null || tLRPC$TL_groupCallParticipant.can_self_unmute || !tLRPC$TL_groupCallParticipant.muted || ChatObject.canManageCalls(sharedInstance.getChat())) {
                    return;
                }
                sharedInstance.setMicMute(true, false, false);
                long uptimeMillis = SystemClock.uptimeMillis();
                this.muteButton.dispatchTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
            } else if (i == NotificationCenter.groupCallTypingsUpdated) {
                if (!this.visible || this.currentStyle != 4) {
                    return;
                }
                ChatObject.Call groupCall = ((ChatActivity) this.fragment).getGroupCall();
                if (groupCall != null) {
                    if (groupCall.isScheduled()) {
                        this.subtitleTextView.setText(LocaleController.formatStartsTime(groupCall.call.schedule_date, 4), false);
                    } else {
                        TLRPC$GroupCall tLRPC$GroupCall = groupCall.call;
                        int i5 = tLRPC$GroupCall.participants_count;
                        if (i5 == 0) {
                            this.subtitleTextView.setText(LocaleController.getString(tLRPC$GroupCall.rtmp_stream ? 2131628928 : 2131626608), false);
                        } else {
                            this.subtitleTextView.setText(LocaleController.formatPluralString(tLRPC$GroupCall.rtmp_stream ? "ViewersWatching" : "Participants", i5, new Object[0]), false);
                        }
                    }
                }
                updateAvatars(true);
            } else if (i == NotificationCenter.historyImportProgressChanged) {
                int i6 = this.currentStyle;
                if (i6 == 1 || i6 == 3 || i6 == 4) {
                    checkCall(false);
                }
                checkImport(false);
            } else if (i == NotificationCenter.messagePlayingSpeedChanged) {
                updatePlaybackButton();
            } else if (i == NotificationCenter.webRtcMicAmplitudeEvent) {
                if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute()) {
                    this.micAmplitude = 0.0f;
                } else {
                    this.micAmplitude = Math.min(8500.0f, ((Float) objArr[0]).floatValue() * 4000.0f) / 8500.0f;
                }
                if (VoIPService.getSharedInstance() == null) {
                    return;
                }
                Theme.getFragmentContextViewWavesDrawable().setAmplitude(Math.max(this.speakerAmplitude, this.micAmplitude));
            } else if (i != NotificationCenter.webRtcSpeakerAmplitudeEvent) {
            } else {
                this.speakerAmplitude = Math.max(0.0f, Math.min((((Float) objArr[0]).floatValue() * 15.0f) / 80.0f, 1.0f));
                if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute()) {
                    this.micAmplitude = 0.0f;
                }
                if (VoIPService.getSharedInstance() != null) {
                    Theme.getFragmentContextViewWavesDrawable().setAmplitude(Math.max(this.speakerAmplitude, this.micAmplitude));
                }
                this.avatars.invalidate();
            }
        }
    }

    public int getStyleHeight() {
        return this.currentStyle == 4 ? 48 : 36;
    }

    public boolean isCallTypeVisible() {
        int i = this.currentStyle;
        return (i == 1 || i == 3) && this.visible;
    }

    private void checkLiveLocation(boolean z) {
        boolean z2;
        String str;
        String str2;
        View fragmentView = this.fragment.getFragmentView();
        if (!z && fragmentView != null && (fragmentView.getParent() == null || ((View) fragmentView.getParent()).getVisibility() != 0)) {
            z = true;
        }
        BaseFragment baseFragment = this.fragment;
        if (baseFragment instanceof DialogsActivity) {
            z2 = LocationController.getLocationsCount() != 0;
        } else {
            z2 = LocationController.getInstance(baseFragment.getCurrentAccount()).isSharingLocation(((ChatActivity) this.fragment).getDialogId());
        }
        if (!z2) {
            this.lastLocationSharingCount = -1;
            AndroidUtilities.cancelRunOnUIThread(this.checkLocationRunnable);
            if (!this.visible) {
                return;
            }
            this.visible = false;
            if (z) {
                if (getVisibility() != 8) {
                    setVisibility(8);
                }
                setTopPadding(0.0f);
                return;
            }
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animatorSet = null;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, "topPadding", 0.0f));
            this.animatorSet.setDuration(200L);
            this.animatorSet.addListener(new AnonymousClass8());
            this.animatorSet.start();
            return;
        }
        updateStyle(2);
        this.playButton.setImageDrawable(new ShareLocationDrawable(getContext(), 1));
        if (z && this.topPadding == 0.0f) {
            setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
        }
        if (!this.visible) {
            if (!z) {
                AnimatorSet animatorSet3 = this.animatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                    this.animatorSet = null;
                }
                AnimatorSet animatorSet4 = new AnimatorSet();
                this.animatorSet = animatorSet4;
                animatorSet4.playTogether(ObjectAnimator.ofFloat(this, "topPadding", AndroidUtilities.dp2(getStyleHeight())));
                this.animatorSet.setDuration(200L);
                this.animatorSet.addListener(new AnonymousClass9());
                this.animatorSet.start();
            }
            this.visible = true;
            setVisibility(0);
        }
        if (this.fragment instanceof DialogsActivity) {
            String string = LocaleController.getString("LiveLocationContext", 2131626471);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < 4; i++) {
                arrayList.addAll(LocationController.getInstance(i).sharingLocationsUI);
            }
            if (arrayList.size() == 1) {
                LocationController.SharingLocationInfo sharingLocationInfo = (LocationController.SharingLocationInfo) arrayList.get(0);
                long dialogId = sharingLocationInfo.messageObject.getDialogId();
                if (DialogObject.isUserDialog(dialogId)) {
                    str2 = UserObject.getFirstName(MessagesController.getInstance(sharingLocationInfo.messageObject.currentAccount).getUser(Long.valueOf(dialogId)));
                    str = LocaleController.getString("AttachLiveLocationIsSharing", 2131624489);
                } else {
                    TLRPC$Chat chat = MessagesController.getInstance(sharingLocationInfo.messageObject.currentAccount).getChat(Long.valueOf(-dialogId));
                    str2 = chat != null ? chat.title : "";
                    str = LocaleController.getString("AttachLiveLocationIsSharingChat", 2131624490);
                }
            } else {
                str2 = LocaleController.formatPluralString("Chats", arrayList.size(), new Object[0]);
                str = LocaleController.getString("AttachLiveLocationIsSharingChats", 2131624491);
            }
            String format = String.format(str, string, str2);
            int indexOf = format.indexOf(string);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
            int i2 = 0;
            while (i2 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = this.titleTextView;
                TextView textView = i2 == 0 ? clippingTextViewSwitcher.getTextView() : clippingTextViewSwitcher.getNextTextView();
                if (textView != null) {
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                }
                i2++;
            }
            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, getThemedColor("inappPlayerPerformer")), indexOf, string.length() + indexOf, 18);
            this.titleTextView.setText(spannableStringBuilder, false);
            return;
        }
        this.checkLocationRunnable.run();
        checkLocationString();
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        AnonymousClass8() {
            FragmentContextView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.setVisibility(8);
            FragmentContextView.this.animatorSet = null;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        AnonymousClass9() {
            FragmentContextView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.animatorSet = null;
        }
    }

    public void checkLocationString() {
        int i;
        String str;
        BaseFragment baseFragment = this.fragment;
        if (!(baseFragment instanceof ChatActivity) || this.titleTextView == null) {
            return;
        }
        ChatActivity chatActivity = (ChatActivity) baseFragment;
        long dialogId = chatActivity.getDialogId();
        int currentAccount = chatActivity.getCurrentAccount();
        ArrayList<TLRPC$Message> arrayList = LocationController.getInstance(currentAccount).locationsCache.get(dialogId);
        if (!this.firstLocationsLoaded) {
            LocationController.getInstance(currentAccount).loadLiveLocations(dialogId);
            this.firstLocationsLoaded = true;
        }
        TLRPC$User tLRPC$User = null;
        if (arrayList != null) {
            long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
            int currentTime = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
            i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLRPC$Message tLRPC$Message = arrayList.get(i2);
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                if (tLRPC$MessageMedia != null && tLRPC$Message.date + tLRPC$MessageMedia.period > currentTime) {
                    long fromChatId = MessageObject.getFromChatId(tLRPC$Message);
                    if (tLRPC$User == null && fromChatId != clientUserId) {
                        tLRPC$User = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(fromChatId));
                    }
                    i++;
                }
            }
        } else {
            i = 0;
        }
        if (this.lastLocationSharingCount == i) {
            return;
        }
        this.lastLocationSharingCount = i;
        String string = LocaleController.getString("LiveLocationContext", 2131626471);
        if (i == 0) {
            str = string;
        } else {
            int i3 = i - 1;
            str = LocationController.getInstance(currentAccount).isSharingLocation(dialogId) ? i3 != 0 ? (i3 != 1 || tLRPC$User == null) ? String.format("%1$s - %2$s %3$s", string, LocaleController.getString("ChatYourSelfName", 2131625054), LocaleController.formatPluralString("AndOther", i3, new Object[0])) : String.format("%1$s - %2$s", string, LocaleController.formatString("SharingYouAndOtherName", 2131628325, UserObject.getFirstName(tLRPC$User))) : String.format("%1$s - %2$s", string, LocaleController.getString("ChatYourSelfName", 2131625054)) : i3 != 0 ? String.format("%1$s - %2$s %3$s", string, UserObject.getFirstName(tLRPC$User), LocaleController.formatPluralString("AndOther", i3, new Object[0])) : String.format("%1$s - %2$s", string, UserObject.getFirstName(tLRPC$User));
        }
        if (str.equals(this.lastString)) {
            return;
        }
        this.lastString = str;
        int indexOf = str.indexOf(string);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        int i4 = 0;
        while (i4 < 2) {
            AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = this.titleTextView;
            TextView textView = i4 == 0 ? clippingTextViewSwitcher.getTextView() : clippingTextViewSwitcher.getNextTextView();
            if (textView != null) {
                textView.setEllipsize(TextUtils.TruncateAt.END);
            }
            i4++;
        }
        if (indexOf >= 0) {
            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, getThemedColor("inappPlayerPerformer")), indexOf, string.length() + indexOf, 18);
        }
        this.titleTextView.setText(spannableStringBuilder, false);
    }

    public void checkPlayer(boolean z) {
        SpannableStringBuilder spannableStringBuilder;
        boolean z2 = true;
        if (this.visible) {
            int i = this.currentStyle;
            if (i == 1 || i == 3) {
                return;
            }
            if ((i == 4 || i == 5) && !isPlayingVoice()) {
                return;
            }
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        View fragmentView = this.fragment.getFragmentView();
        if (!z && fragmentView != null && (fragmentView.getParent() == null || ((View) fragmentView.getParent()).getVisibility() != 0)) {
            z = true;
        }
        boolean z3 = this.visible;
        if (playingMessageObject == null || playingMessageObject.getId() == 0 || playingMessageObject.isVideo()) {
            this.lastMessageObject = null;
            boolean z4 = this.supportsCalls && VoIPService.getSharedInstance() != null && !VoIPService.getSharedInstance().isHangingUp() && VoIPService.getSharedInstance().getCallState() != 15 && !GroupCallPip.isShowing();
            if (!isPlayingVoice() && !z4 && (this.fragment instanceof ChatActivity) && !GroupCallPip.isShowing()) {
                ChatObject.Call groupCall = ((ChatActivity) this.fragment).getGroupCall();
                z4 = groupCall != null && groupCall.shouldShowPanel();
            }
            if (z4) {
                checkCall(false);
                return;
            } else if (this.visible) {
                ActionBarMenuItem actionBarMenuItem = this.playbackSpeedButton;
                if (actionBarMenuItem != null && actionBarMenuItem.isSubMenuShowing()) {
                    this.playbackSpeedButton.toggleSubMenu();
                }
                this.visible = false;
                if (z) {
                    if (getVisibility() != 8) {
                        setVisibility(8);
                    }
                    setTopPadding(0.0f);
                    return;
                }
                AnimatorSet animatorSet = this.animatorSet;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.animatorSet = null;
                }
                this.animationIndex = NotificationCenter.getInstance(this.account).setAnimationInProgress(this.animationIndex, null);
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.animatorSet = animatorSet2;
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this, "topPadding", 0.0f));
                this.animatorSet.setDuration(200L);
                FragmentContextViewDelegate fragmentContextViewDelegate = this.delegate;
                if (fragmentContextViewDelegate != null) {
                    fragmentContextViewDelegate.onAnimation(true, false);
                }
                this.animatorSet.addListener(new AnonymousClass10());
                this.animatorSet.start();
                return;
            } else {
                setVisibility(8);
                return;
            }
        }
        int i2 = this.currentStyle;
        if (i2 != 0 && this.animatorSet != null && !z) {
            this.checkPlayerAfterAnimation = true;
            return;
        }
        updateStyle(0);
        if (z && this.topPadding == 0.0f) {
            updatePaddings();
            setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
            FragmentContextViewDelegate fragmentContextViewDelegate2 = this.delegate;
            if (fragmentContextViewDelegate2 != null) {
                fragmentContextViewDelegate2.onAnimation(true, true);
                this.delegate.onAnimation(false, true);
            }
        }
        if (!this.visible) {
            if (!z) {
                AnimatorSet animatorSet3 = this.animatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                    this.animatorSet = null;
                }
                this.animationIndex = NotificationCenter.getInstance(this.account).setAnimationInProgress(this.animationIndex, null);
                this.animatorSet = new AnimatorSet();
                FragmentContextView fragmentContextView = this.additionalContextView;
                if (fragmentContextView != null && fragmentContextView.getVisibility() == 0) {
                    ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight() + this.additionalContextView.getStyleHeight());
                } else {
                    ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight());
                }
                FragmentContextViewDelegate fragmentContextViewDelegate3 = this.delegate;
                if (fragmentContextViewDelegate3 != null) {
                    fragmentContextViewDelegate3.onAnimation(true, true);
                }
                this.animatorSet.playTogether(ObjectAnimator.ofFloat(this, "topPadding", AndroidUtilities.dp2(getStyleHeight())));
                this.animatorSet.setDuration(200L);
                this.animatorSet.addListener(new AnonymousClass11());
                this.animatorSet.start();
            }
            this.visible = true;
            setVisibility(0);
        }
        if (MediaController.getInstance().isMessagePaused()) {
            this.playPauseDrawable.setPause(false, !z);
            this.playButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131623955));
        } else {
            this.playPauseDrawable.setPause(true, !z);
            this.playButton.setContentDescription(LocaleController.getString("AccActionPause", 2131623954));
        }
        if (this.lastMessageObject == playingMessageObject && i2 == 0) {
            return;
        }
        this.lastMessageObject = playingMessageObject;
        if (playingMessageObject.isVoice() || this.lastMessageObject.isRoundVideo()) {
            this.isMusic = false;
            ActionBarMenuItem actionBarMenuItem2 = this.playbackSpeedButton;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setAlpha(1.0f);
                this.playbackSpeedButton.setEnabled(true);
            }
            this.titleTextView.setPadding(0, 0, AndroidUtilities.dp(44.0f), 0);
            spannableStringBuilder = new SpannableStringBuilder(String.format("%s %s", playingMessageObject.getMusicAuthor(), playingMessageObject.getMusicTitle()));
            int i3 = 0;
            while (i3 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher = this.titleTextView;
                TextView textView = i3 == 0 ? clippingTextViewSwitcher.getTextView() : clippingTextViewSwitcher.getNextTextView();
                if (textView != null) {
                    textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                }
                i3++;
            }
            updatePlaybackButton();
        } else {
            this.isMusic = true;
            if (this.playbackSpeedButton != null) {
                if (playingMessageObject.getDuration() >= 600) {
                    this.playbackSpeedButton.setAlpha(1.0f);
                    this.playbackSpeedButton.setEnabled(true);
                    this.titleTextView.setPadding(0, 0, AndroidUtilities.dp(44.0f), 0);
                    updatePlaybackButton();
                } else {
                    this.playbackSpeedButton.setAlpha(0.0f);
                    this.playbackSpeedButton.setEnabled(false);
                    this.titleTextView.setPadding(0, 0, 0, 0);
                }
            } else {
                this.titleTextView.setPadding(0, 0, 0, 0);
            }
            spannableStringBuilder = new SpannableStringBuilder(String.format("%s - %s", playingMessageObject.getMusicAuthor(), playingMessageObject.getMusicTitle()));
            int i4 = 0;
            while (i4 < 2) {
                AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher2 = this.titleTextView;
                TextView textView2 = i4 == 0 ? clippingTextViewSwitcher2.getTextView() : clippingTextViewSwitcher2.getNextTextView();
                if (textView2 != null) {
                    textView2.setEllipsize(TextUtils.TruncateAt.END);
                }
                i4++;
            }
        }
        spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, getThemedColor("inappPlayerPerformer")), 0, playingMessageObject.getMusicAuthor().length(), 18);
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher3 = this.titleTextView;
        if (z || !z3 || !this.isMusic) {
            z2 = false;
        }
        clippingTextViewSwitcher3.setText(spannableStringBuilder, z2);
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        AnonymousClass10() {
            FragmentContextView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(FragmentContextView.this.account).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.setVisibility(8);
            if (FragmentContextView.this.delegate != null) {
                FragmentContextView.this.delegate.onAnimation(false, false);
            }
            FragmentContextView.this.animatorSet = null;
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends AnimatorListenerAdapter {
        AnonymousClass11() {
            FragmentContextView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(FragmentContextView.this.account).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            if (FragmentContextView.this.delegate != null) {
                FragmentContextView.this.delegate.onAnimation(false, true);
            }
            FragmentContextView.this.animatorSet = null;
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
        }
    }

    public void checkImport(boolean z) {
        int i;
        BaseFragment baseFragment = this.fragment;
        if (baseFragment instanceof ChatActivity) {
            if (this.visible && ((i = this.currentStyle) == 1 || i == 3)) {
                return;
            }
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            SendMessagesHelper.ImportingHistory importingHistory = chatActivity.getSendMessagesHelper().getImportingHistory(chatActivity.getDialogId());
            View fragmentView = this.fragment.getFragmentView();
            if (!z && fragmentView != null && (fragmentView.getParent() == null || ((View) fragmentView.getParent()).getVisibility() != 0)) {
                z = true;
            }
            Dialog visibleDialog = chatActivity.getVisibleDialog();
            if ((isPlayingVoice() || chatActivity.shouldShowImport() || ((visibleDialog instanceof ImportingAlert) && !((ImportingAlert) visibleDialog).isDismissed())) && importingHistory != null) {
                importingHistory = null;
            }
            if (importingHistory == null) {
                if (this.visible && ((z && this.currentStyle == -1) || this.currentStyle == 5)) {
                    this.visible = false;
                    if (z) {
                        if (getVisibility() != 8) {
                            setVisibility(8);
                        }
                        setTopPadding(0.0f);
                        return;
                    }
                    AnimatorSet animatorSet = this.animatorSet;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    int i2 = this.account;
                    this.animationIndex = NotificationCenter.getInstance(i2).setAnimationInProgress(this.animationIndex, null);
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.animatorSet = animatorSet2;
                    animatorSet2.playTogether(ObjectAnimator.ofFloat(this, "topPadding", 0.0f));
                    this.animatorSet.setDuration(220L);
                    this.animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    this.animatorSet.addListener(new AnonymousClass12(i2));
                    this.animatorSet.start();
                    return;
                }
                int i3 = this.currentStyle;
                if (i3 != -1 && i3 != 5) {
                    return;
                }
                this.visible = false;
                setVisibility(8);
            } else if (this.currentStyle != 5 && this.animatorSet != null && !z) {
                this.checkImportAfterAnimation = true;
            } else {
                updateStyle(5);
                if (z && this.topPadding == 0.0f) {
                    updatePaddings();
                    setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
                    FragmentContextViewDelegate fragmentContextViewDelegate = this.delegate;
                    if (fragmentContextViewDelegate != null) {
                        fragmentContextViewDelegate.onAnimation(true, true);
                        this.delegate.onAnimation(false, true);
                    }
                }
                if (!this.visible) {
                    if (!z) {
                        AnimatorSet animatorSet3 = this.animatorSet;
                        if (animatorSet3 != null) {
                            animatorSet3.cancel();
                            this.animatorSet = null;
                        }
                        this.animationIndex = NotificationCenter.getInstance(this.account).setAnimationInProgress(this.animationIndex, null);
                        this.animatorSet = new AnimatorSet();
                        FragmentContextView fragmentContextView = this.additionalContextView;
                        if (fragmentContextView != null && fragmentContextView.getVisibility() == 0) {
                            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight() + this.additionalContextView.getStyleHeight());
                        } else {
                            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight());
                        }
                        FragmentContextViewDelegate fragmentContextViewDelegate2 = this.delegate;
                        if (fragmentContextViewDelegate2 != null) {
                            fragmentContextViewDelegate2.onAnimation(true, true);
                        }
                        this.animatorSet.playTogether(ObjectAnimator.ofFloat(this, "topPadding", AndroidUtilities.dp2(getStyleHeight())));
                        this.animatorSet.setDuration(200L);
                        this.animatorSet.addListener(new AnonymousClass13());
                        this.animatorSet.start();
                    }
                    this.visible = true;
                    setVisibility(0);
                }
                int i4 = this.currentProgress;
                int i5 = importingHistory.uploadProgress;
                if (i4 == i5) {
                    return;
                }
                this.currentProgress = i5;
                this.titleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ImportUploading", 2131626218, Integer.valueOf(i5))), false);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass12(int i) {
            FragmentContextView.this = r1;
            this.val$currentAccount = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(this.val$currentAccount).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.setVisibility(8);
            FragmentContextView.this.animatorSet = null;
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends AnimatorListenerAdapter {
        AnonymousClass13() {
            FragmentContextView.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(FragmentContextView.this.account).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            if (FragmentContextView.this.delegate != null) {
                FragmentContextView.this.delegate.onAnimation(false, true);
            }
            FragmentContextView.this.animatorSet = null;
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
        }
    }

    private boolean isPlayingVoice() {
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        return playingMessageObject != null && playingMessageObject.isVoice();
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0144  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkCall(boolean z) {
        boolean z2;
        boolean z3;
        int i;
        int i2;
        int i3;
        ChatObject.Call groupCall;
        ChatObject.Call call;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (!this.visible || this.currentStyle != 5 || (sharedInstance != null && !sharedInstance.isHangingUp())) {
            View fragmentView = this.fragment.getFragmentView();
            boolean z4 = (z || fragmentView == null || (fragmentView.getParent() != null && ((View) fragmentView.getParent()).getVisibility() == 0)) ? z : true;
            if (GroupCallPip.isShowing()) {
                z3 = false;
            } else {
                z3 = !GroupCallActivity.groupCallUiVisible && this.supportsCalls && sharedInstance != null && !sharedInstance.isHangingUp();
                if (sharedInstance != null && (call = sharedInstance.groupCall) != null && (call.call instanceof TLRPC$TL_groupCallDiscarded)) {
                    z3 = false;
                }
                if (!isPlayingVoice() && !GroupCallActivity.groupCallUiVisible && this.supportsCalls && !z3) {
                    BaseFragment baseFragment = this.fragment;
                    if ((baseFragment instanceof ChatActivity) && (groupCall = ((ChatActivity) baseFragment).getGroupCall()) != null && groupCall.shouldShowPanel()) {
                        z3 = true;
                        z2 = true;
                        if (z3) {
                            boolean z5 = this.visible;
                            if (z5 && ((z4 && this.currentStyle == -1) || (i3 = this.currentStyle) == 4 || i3 == 3 || i3 == 1)) {
                                this.visible = false;
                                if (z4) {
                                    if (getVisibility() != 8) {
                                        setVisibility(8);
                                    }
                                    setTopPadding(0.0f);
                                } else {
                                    AnimatorSet animatorSet = this.animatorSet;
                                    if (animatorSet != null) {
                                        animatorSet.cancel();
                                        this.animatorSet = null;
                                    }
                                    int i4 = this.account;
                                    this.animationIndex = NotificationCenter.getInstance(i4).setAnimationInProgress(this.animationIndex, null);
                                    AnimatorSet animatorSet2 = new AnimatorSet();
                                    this.animatorSet = animatorSet2;
                                    animatorSet2.playTogether(ObjectAnimator.ofFloat(this, "topPadding", 0.0f));
                                    this.animatorSet.setDuration(220L);
                                    this.animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                                    this.animatorSet.addListener(new AnonymousClass14(i4));
                                    this.animatorSet.start();
                                }
                            } else if (z5 && ((i2 = this.currentStyle) == -1 || i2 == 4 || i2 == 3 || i2 == 1)) {
                                this.visible = false;
                                setVisibility(8);
                            }
                            if (!z4) {
                                return;
                            }
                            BaseFragment baseFragment2 = this.fragment;
                            if (!(baseFragment2 instanceof ChatActivity) || !((ChatActivity) baseFragment2).openedWithLivestream() || GroupCallPip.isShowing()) {
                                return;
                            }
                            BulletinFactory.of(this.fragment).createSimpleBulletin(2131558480, LocaleController.getString("InviteExpired", 2131626260)).show();
                            return;
                        }
                        if (z2) {
                            i = 4;
                        } else {
                            i = sharedInstance.groupCall != null ? 3 : 1;
                        }
                        int i5 = this.currentStyle;
                        if (i != i5 && this.animatorSet != null && !z4) {
                            this.checkCallAfterAnimation = true;
                            return;
                        } else if (i != i5 && this.visible && !z4) {
                            AnimatorSet animatorSet3 = this.animatorSet;
                            if (animatorSet3 != null) {
                                animatorSet3.cancel();
                                this.animatorSet = null;
                            }
                            int i6 = this.account;
                            this.animationIndex = NotificationCenter.getInstance(i6).setAnimationInProgress(this.animationIndex, null);
                            AnimatorSet animatorSet4 = new AnimatorSet();
                            this.animatorSet = animatorSet4;
                            animatorSet4.playTogether(ObjectAnimator.ofFloat(this, "topPadding", 0.0f));
                            this.animatorSet.setDuration(220L);
                            this.animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                            this.animatorSet.addListener(new AnonymousClass15(i6));
                            this.animatorSet.start();
                            return;
                        } else {
                            if (z2) {
                                boolean z6 = i5 == 4 && this.visible;
                                updateStyle(4);
                                ChatObject.Call groupCall2 = ((ChatActivity) this.fragment).getGroupCall();
                                TLRPC$Chat currentChat = ((ChatActivity) this.fragment).getCurrentChat();
                                if (groupCall2.isScheduled()) {
                                    if (this.gradientPaint == null) {
                                        TextPaint textPaint = new TextPaint(1);
                                        this.gradientTextPaint = textPaint;
                                        textPaint.setColor(-1);
                                        this.gradientTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
                                        this.gradientTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                        Paint paint = new Paint(1);
                                        this.gradientPaint = paint;
                                        paint.setColor(-1);
                                        this.matrix = new Matrix();
                                    }
                                    this.joinButton.setVisibility(8);
                                    if (!TextUtils.isEmpty(groupCall2.call.title)) {
                                        this.titleTextView.setText(groupCall2.call.title, false);
                                    } else if (ChatObject.isChannelOrGiga(currentChat)) {
                                        this.titleTextView.setText(LocaleController.getString("VoipChannelScheduledVoiceChat", 2131628986), false);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString("VoipGroupScheduledVoiceChat", 2131629096), false);
                                    }
                                    this.subtitleTextView.setText(LocaleController.formatStartsTime(groupCall2.call.schedule_date, 4), false);
                                    if (!this.scheduleRunnableScheduled) {
                                        this.scheduleRunnableScheduled = true;
                                        this.updateScheduleTimeRunnable.run();
                                    }
                                } else {
                                    this.timeLayout = null;
                                    this.joinButton.setVisibility(0);
                                    if (groupCall2.call.rtmp_stream) {
                                        this.titleTextView.setText(LocaleController.getString(2131629000), false);
                                    } else if (ChatObject.isChannelOrGiga(currentChat)) {
                                        this.titleTextView.setText(LocaleController.getString("VoipChannelVoiceChat", 2131629000), false);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString("VoipGroupVoiceChat", 2131629130), false);
                                    }
                                    TLRPC$GroupCall tLRPC$GroupCall = groupCall2.call;
                                    int i7 = tLRPC$GroupCall.participants_count;
                                    if (i7 == 0) {
                                        this.subtitleTextView.setText(LocaleController.getString(tLRPC$GroupCall.rtmp_stream ? 2131628928 : 2131626608), false);
                                    } else {
                                        this.subtitleTextView.setText(LocaleController.formatPluralString(tLRPC$GroupCall.rtmp_stream ? "ViewersWatching" : "Participants", i7, new Object[0]), false);
                                    }
                                    this.frameLayout.invalidate();
                                }
                                updateAvatars(this.avatars.avatarsDarawable.wasDraw && z6);
                            } else if (sharedInstance != null && sharedInstance.groupCall != null) {
                                updateAvatars(i5 == 3);
                                updateStyle(3);
                            } else {
                                updateAvatars(i5 == 1);
                                updateStyle(1);
                            }
                            if (this.visible) {
                                return;
                            }
                            if (!z4) {
                                AnimatorSet animatorSet5 = this.animatorSet;
                                if (animatorSet5 != null) {
                                    animatorSet5.cancel();
                                    this.animatorSet = null;
                                }
                                this.animatorSet = new AnimatorSet();
                                FragmentContextView fragmentContextView = this.additionalContextView;
                                if (fragmentContextView != null && fragmentContextView.getVisibility() == 0) {
                                    ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight() + this.additionalContextView.getStyleHeight());
                                } else {
                                    ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(getStyleHeight());
                                }
                                int i8 = this.account;
                                this.animationIndex = NotificationCenter.getInstance(i8).setAnimationInProgress(this.animationIndex, new int[]{NotificationCenter.messagesDidLoad});
                                this.animatorSet.playTogether(ObjectAnimator.ofFloat(this, "topPadding", AndroidUtilities.dp2(getStyleHeight())));
                                this.animatorSet.setDuration(220L);
                                this.animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                                this.animatorSet.addListener(new AnonymousClass16(i8));
                                this.animatorSet.start();
                            } else {
                                updatePaddings();
                                setTopPadding(AndroidUtilities.dp2(getStyleHeight()));
                                startJoinFlickerAnimation();
                            }
                            this.visible = true;
                            setVisibility(0);
                            return;
                        }
                    }
                }
            }
            z2 = false;
            if (z3) {
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass14(int i) {
            FragmentContextView.this = r1;
            this.val$currentAccount = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(this.val$currentAccount).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.setVisibility(8);
            FragmentContextView.this.animatorSet = null;
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass15(int i) {
            FragmentContextView.this = r1;
            this.val$currentAccount = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(this.val$currentAccount).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet == null || !FragmentContextView.this.animatorSet.equals(animator)) {
                return;
            }
            FragmentContextView.this.visible = false;
            FragmentContextView.this.animatorSet = null;
            FragmentContextView.this.checkCall(false);
        }
    }

    /* renamed from: org.telegram.ui.Components.FragmentContextView$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass16(int i) {
            FragmentContextView.this = r1;
            this.val$currentAccount = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter.getInstance(this.val$currentAccount).onAnimationFinish(FragmentContextView.this.animationIndex);
            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(animator)) {
                FragmentContextView.this.animatorSet = null;
            }
            if (!FragmentContextView.this.checkCallAfterAnimation) {
                if (FragmentContextView.this.checkPlayerAfterAnimation) {
                    FragmentContextView.this.checkPlayer(false);
                } else if (FragmentContextView.this.checkImportAfterAnimation) {
                    FragmentContextView.this.checkImport(false);
                }
            } else {
                FragmentContextView.this.checkCall(false);
            }
            FragmentContextView.this.checkCallAfterAnimation = false;
            FragmentContextView.this.checkPlayerAfterAnimation = false;
            FragmentContextView.this.checkImportAfterAnimation = false;
            FragmentContextView.this.startJoinFlickerAnimation();
        }
    }

    public void startJoinFlickerAnimation() {
        if (this.joinButtonFlicker.getProgress() > 1.0f) {
            AndroidUtilities.runOnUIThread(new FragmentContextView$$ExternalSyntheticLambda9(this), 150L);
        }
    }

    public /* synthetic */ void lambda$startJoinFlickerAnimation$12() {
        this.joinButtonFlicker.setProgress(0.0f);
        this.joinButton.invalidate();
    }

    private void updateAvatars(boolean z) {
        int i;
        ChatObject.Call call;
        TLRPC$User tLRPC$User;
        float f;
        int i2;
        ValueAnimator valueAnimator;
        if (!z && (valueAnimator = this.avatars.avatarsDarawable.transitionProgressAnimator) != null) {
            valueAnimator.cancel();
            this.avatars.avatarsDarawable.transitionProgressAnimator = null;
        }
        AvatarsImageView avatarsImageView = this.avatars;
        if (avatarsImageView.avatarsDarawable.transitionProgressAnimator == null) {
            if (this.currentStyle == 4) {
                BaseFragment baseFragment = this.fragment;
                if (baseFragment instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment;
                    call = chatActivity.getGroupCall();
                    i2 = chatActivity.getCurrentAccount();
                } else {
                    i2 = this.account;
                    call = null;
                }
                i = i2;
                tLRPC$User = null;
            } else if (VoIPService.getSharedInstance() != null) {
                call = VoIPService.getSharedInstance().groupCall;
                tLRPC$User = this.fragment instanceof ChatActivity ? null : VoIPService.getSharedInstance().getUser();
                i = VoIPService.getSharedInstance().getAccount();
            } else {
                call = null;
                i = this.account;
                tLRPC$User = null;
            }
            int i3 = 0;
            if (call != null) {
                int size = call.sortedParticipants.size();
                for (int i4 = 0; i4 < 3; i4++) {
                    if (i4 < size) {
                        this.avatars.setObject(i4, i, call.sortedParticipants.get(i4));
                    } else {
                        this.avatars.setObject(i4, i, null);
                    }
                }
            } else if (tLRPC$User != null) {
                this.avatars.setObject(0, i, tLRPC$User);
                for (int i5 = 1; i5 < 3; i5++) {
                    this.avatars.setObject(i5, i, null);
                }
            } else {
                for (int i6 = 0; i6 < 3; i6++) {
                    this.avatars.setObject(i6, i, null);
                }
            }
            this.avatars.commitTransition(z);
            if (this.currentStyle != 4 || call == null) {
                return;
            }
            if (!call.call.rtmp_stream) {
                i3 = Math.min(3, call.sortedParticipants.size());
            }
            int i7 = 10;
            if (i3 != 0) {
                i7 = 10 + ((i3 - 1) * 24) + 10 + 32;
            }
            if (z) {
                int i8 = ((FrameLayout.LayoutParams) this.titleTextView.getLayoutParams()).leftMargin;
                if (AndroidUtilities.dp(i7) != i8) {
                    float translationX = (this.titleTextView.getTranslationX() + i8) - AndroidUtilities.dp(f);
                    this.titleTextView.setTranslationX(translationX);
                    this.subtitleTextView.setTranslationX(translationX);
                    ViewPropertyAnimator duration = this.titleTextView.animate().translationX(0.0f).setDuration(220L);
                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                    duration.setInterpolator(cubicBezierInterpolator);
                    this.subtitleTextView.animate().translationX(0.0f).setDuration(220L).setInterpolator(cubicBezierInterpolator);
                }
            } else {
                this.titleTextView.animate().cancel();
                this.subtitleTextView.animate().cancel();
                this.titleTextView.setTranslationX(0.0f);
                this.subtitleTextView.setTranslationX(0.0f);
            }
            float f2 = i7;
            this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, 51, f2, 5.0f, call.isScheduled() ? 90.0f : 36.0f, 0.0f));
            this.subtitleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 20.0f, 51, f2, 25.0f, call.isScheduled() ? 90.0f : 36.0f, 0.0f));
            return;
        }
        avatarsImageView.updateAfterTransitionEnd();
    }

    public void setCollapseTransition(boolean z, float f, float f2) {
        this.collapseTransition = z;
        this.extraHeight = f;
        this.collapseProgress = f2;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (!this.drawOverlay || getVisibility() == 0) {
            boolean z = false;
            int i = this.currentStyle;
            if (i == 3 || i == 1) {
                if (GroupCallActivity.groupCallInstance == null) {
                    Theme.getFragmentContextViewWavesDrawable().getState();
                }
                Theme.getFragmentContextViewWavesDrawable().updateState(this.wasDraw);
                float dp = this.topPadding / AndroidUtilities.dp(getStyleHeight());
                if (this.collapseTransition) {
                    Theme.getFragmentContextViewWavesDrawable().draw(0.0f, (AndroidUtilities.dp(getStyleHeight()) - this.topPadding) + this.extraHeight, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(2.0f), canvas, null, Math.min(dp, 1.0f - this.collapseProgress));
                } else {
                    Theme.getFragmentContextViewWavesDrawable().draw(0.0f, AndroidUtilities.dp(getStyleHeight()) - this.topPadding, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(2.0f), canvas, this, dp);
                }
                float dp2 = AndroidUtilities.dp(getStyleHeight()) - this.topPadding;
                if (this.collapseTransition) {
                    dp2 += this.extraHeight;
                }
                if (dp2 > getMeasuredHeight()) {
                    return;
                }
                canvas.save();
                canvas.clipRect(0.0f, dp2, getMeasuredWidth(), getMeasuredHeight());
                invalidate();
                z = true;
            }
            super.dispatchDraw(canvas);
            if (z) {
                canvas.restore();
            }
            this.wasDraw = true;
        }
    }

    public void setDrawOverlay(boolean z) {
        this.drawOverlay = z;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        int i = this.currentStyle;
        if ((i == 3 || i == 1) && getParent() != null) {
            ((View) getParent()).invalidate();
        }
    }

    public boolean isCallStyle() {
        int i = this.currentStyle;
        return i == 3 || i == 1;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        updatePaddings();
        setTopPadding(this.topPadding);
        if (i == 8) {
            this.wasDraw = false;
        }
    }

    private void updatePaddings() {
        int i = 0;
        if (getVisibility() == 0) {
            i = 0 - AndroidUtilities.dp(getStyleHeight());
        }
        FragmentContextView fragmentContextView = this.additionalContextView;
        if (fragmentContextView != null && fragmentContextView.getVisibility() == 0) {
            int dp = i - AndroidUtilities.dp(this.additionalContextView.getStyleHeight());
            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = dp;
            ((FrameLayout.LayoutParams) this.additionalContextView.getLayoutParams()).topMargin = dp;
            return;
        }
        ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = i;
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onStateChanged(int i) {
        updateCallTitle();
    }

    private void updateCallTitle() {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            int i = this.currentStyle;
            if (i != 1 && i != 3) {
                return;
            }
            int callState = sharedInstance.getCallState();
            if (!sharedInstance.isSwitchingStream() && (callState == 1 || callState == 2 || callState == 6 || callState == 5)) {
                this.titleTextView.setText(LocaleController.getString("VoipGroupConnecting", 2131629040), false);
            } else if (sharedInstance.getChat() != null) {
                if (!TextUtils.isEmpty(sharedInstance.groupCall.call.title)) {
                    this.titleTextView.setText(sharedInstance.groupCall.call.title, false);
                    return;
                }
                BaseFragment baseFragment = this.fragment;
                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getCurrentChat() != null && ((ChatActivity) this.fragment).getCurrentChat().id == sharedInstance.getChat().id) {
                    TLRPC$Chat currentChat = ((ChatActivity) this.fragment).getCurrentChat();
                    if (VoIPService.hasRtmpStream()) {
                        this.titleTextView.setText(LocaleController.getString(2131628999), false);
                        return;
                    } else if (ChatObject.isChannelOrGiga(currentChat)) {
                        this.titleTextView.setText(LocaleController.getString("VoipChannelViewVoiceChat", 2131628999), false);
                        return;
                    } else {
                        this.titleTextView.setText(LocaleController.getString("VoipGroupViewVoiceChat", 2131629129), false);
                        return;
                    }
                }
                this.titleTextView.setText(sharedInstance.getChat().title, false);
            } else if (sharedInstance.getUser() == null) {
            } else {
                TLRPC$User user = sharedInstance.getUser();
                BaseFragment baseFragment2 = this.fragment;
                if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).getCurrentUser() != null && ((ChatActivity) this.fragment).getCurrentUser().id == user.id) {
                    this.titleTextView.setText(LocaleController.getString("ReturnToCall", 2131628038));
                } else {
                    this.titleTextView.setText(ContactsController.formatName(user.first_name, user.last_name));
                }
            }
        }
    }

    private int getTitleTextColor() {
        int i = this.currentStyle;
        if (i == 4) {
            return getThemedColor("inappPlayerPerformer");
        }
        if (i == 1 || i == 3) {
            return getThemedColor("returnToCallText");
        }
        return getThemedColor("inappPlayerTitle");
    }

    public int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
