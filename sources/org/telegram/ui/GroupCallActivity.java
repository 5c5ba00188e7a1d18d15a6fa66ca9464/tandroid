package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.messenger.voip.NativeInstance;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.messenger.voip.VoipAudioManager;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.GroupCallInvitedCell;
import org.telegram.ui.Cells.GroupCallTextCell;
import org.telegram.ui.Cells.GroupCallUserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BlobDrawable;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FillLastGridLayoutManager;
import org.telegram.ui.Components.GroupCallFullscreenAdapter;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.GroupCallRecordAlert;
import org.telegram.ui.Components.GroupVoipInviteAlert;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.JoinCallAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.Components.ProfileGalleryView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecordStatusDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.Components.voip.GroupCallGridCell;
import org.telegram.ui.Components.voip.GroupCallMiniTextureView;
import org.telegram.ui.Components.voip.GroupCallRenderersContainer;
import org.telegram.ui.Components.voip.PrivateVideoPreviewDialog;
import org.telegram.ui.Components.voip.PrivateVideoPreviewDialog$$ExternalSyntheticApiModelOutline0;
import org.telegram.ui.Components.voip.RTMPStreamPipOverlay;
import org.telegram.ui.Components.voip.VoIPTextureView;
import org.telegram.ui.Components.voip.VoIPToggleButton;
import org.telegram.ui.GroupCallActivity;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.Stories.recorder.DominantColors;
import org.webrtc.MediaStreamTrack;
import org.webrtc.voiceengine.WebRtcAudioTrack;

/* loaded from: classes4.dex */
public class GroupCallActivity extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, VoIPService.StateListener {
    public static GroupCallActivity groupCallInstance;
    public static boolean groupCallUiVisible;
    public static boolean isLandscapeMode;
    public static boolean isTabletMode;
    public static boolean paused;
    private View accountGap;
    private AccountInstance accountInstance;
    private AccountSelectCell accountSelectCell;
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarBackground;
    private View actionBarShadow;
    ObjectAnimator additionalSubtitleYAnimator;
    private ActionBarMenuSubItem adminItem;
    private float amplitude;
    private float animateAmplitudeDiff;
    boolean animateButtonsOnNextLayout;
    private float animateToAmplitude;
    private boolean animatingToFullscreenExpand;
    private boolean anyEnterEventSent;
    private final ArrayList attachedRenderers;
    private final ArrayList attachedRenderersTmp;
    private final AvatarPreviewPagerIndicator avatarPagerIndicator;
    private final FrameLayout avatarPreviewContainer;
    private boolean avatarPriviewTransitionInProgress;
    AvatarUpdaterDelegate avatarUpdaterDelegate;
    private boolean avatarsPreviewShowed;
    private final ProfileGalleryView avatarsViewPager;
    private int backgroundColor;
    private RLottieDrawable bigMicDrawable;
    private final BlobDrawable bigWaveDrawable;
    private View blurredView;
    private HashMap buttonsAnimationParamsX;
    private HashMap buttonsAnimationParamsY;
    private GradientDrawable buttonsBackgroundGradient;
    private final View buttonsBackgroundGradientView;
    private final View buttonsBackgroundGradientView2;
    private FrameLayout buttonsContainer;
    private int buttonsVisibility;
    public ChatObject.Call call;
    private boolean callInitied;
    private VoIPToggleButton cameraButton;
    private float cameraButtonScale;
    public CellFlickerDrawable cellFlickerDrawable;
    private boolean changingPermissions;
    private float colorProgress;
    private final int[] colorsTmp;
    private boolean contentFullyOverlayed;
    private long creatingServiceTime;
    ImageUpdater currentAvatarUpdater;
    private int currentCallState;
    public TLRPC.Chat currentChat;
    private ViewGroup currentOptionsLayout;
    private WeavingState currentState;
    private boolean delayedGroupCallUpdated;
    private DiffUtil.Callback diffUtilsCallback;
    private boolean drawSpeakingSubtitle;
    public boolean drawingForBlur;
    private ActionBarMenuSubItem editTitleItem;
    private boolean enterEventSent;
    private ActionBarMenuSubItem everyoneItem;
    private ValueAnimator expandAnimator;
    private ImageView expandButton;
    private ValueAnimator expandSizeAnimator;
    private VoIPToggleButton flipButton;
    private final RLottieDrawable flipIcon;
    private int flipIconCurrentEndFrame;
    GroupCallFullscreenAdapter fullscreenAdapter;
    private final DefaultItemAnimator fullscreenListItemAnimator;
    RecyclerListView fullscreenUsersListView;
    private int[] gradientColors;
    private GroupVoipInviteAlert groupVoipInviteAlert;
    private RLottieDrawable handDrawables;
    private boolean hasScrimAnchorView;
    private boolean hasVideo;
    private boolean invalidateColors;
    private ActionBarMenuSubItem inviteItem;
    private String[] invites;
    private GroupCallItemAnimator itemAnimator;
    private long lastUpdateTime;
    private FillLastGridLayoutManager layoutManager;
    private Paint leaveBackgroundPaint;
    private VoIPToggleButton leaveButton;
    private ActionBarMenuSubItem leaveItem;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private Paint listViewBackgroundPaint;
    private boolean listViewVideoVisibility;
    private ValueAnimator liveLabelBgColorAnimator;
    private Paint liveLabelPaint;
    private TextView liveLabelTextView;
    private final LinearLayout menuItemsContainer;
    private ImageView minimizeButton;
    private RLottieImageView muteButton;
    private ValueAnimator muteButtonAnimator;
    private int muteButtonState;
    private TextView[] muteLabel;
    private boolean needTextureLightning;
    private ActionBarMenuSubItem noiseItem;
    private int oldAddMemberRow;
    private int oldCount;
    private ArrayList oldInvited;
    private int oldInvitedEndRow;
    private int oldInvitedStartRow;
    private ArrayList oldParticipants;
    private int oldUsersEndRow;
    private int oldUsersStartRow;
    private int oldUsersVideoEndRow;
    private int oldUsersVideoStartRow;
    private int oldVideoDividerRow;
    private int oldVideoNotAvailableRow;
    private ArrayList oldVideoParticipants;
    private Runnable onUserLeaveHintListener;
    private ActionBarMenuItem otherItem;
    private Paint paint;
    private Paint paintTmp;
    private LaunchActivity parentActivity;
    private ActionBarMenuSubItem permissionItem;
    PinchToZoomHelper pinchToZoomHelper;
    private ActionBarMenuItem pipItem;
    private boolean playingHandAnimation;
    private int popupAnimationIndex;
    private Runnable pressRunnable;
    private boolean pressed;
    private WeavingState prevState;
    PrivateVideoPreviewDialog previewDialog;
    private boolean previewTextureTransitionEnabled;
    private float progressToAvatarPreview;
    float progressToHideUi;
    private RadialGradient radialGradient;
    private final Matrix radialMatrix;
    private final Paint radialPaint;
    private RadialProgressView radialProgressView;
    private RecordCallDrawable recordCallDrawable;
    private HintView recordHintView;
    private ActionBarMenuSubItem recordItem;
    private RectF rect;
    private HintView reminderHintView;
    private GroupCallRenderersContainer renderersContainer;
    ViewTreeObserver.OnPreDrawListener requestFullscreenListener;
    private ValueAnimator scheduleAnimator;
    private TextView scheduleButtonTextView;
    private float scheduleButtonsScale;
    private boolean scheduleHasFewPeers;
    private TextView scheduleInfoTextView;
    private TLRPC.InputPeer schedulePeer;
    private int scheduleStartAt;
    private SimpleTextView scheduleStartAtTextView;
    private SimpleTextView scheduleStartInTextView;
    private SimpleTextView scheduleTimeTextView;
    private LinearLayout scheduleTimerContainer;
    private boolean scheduled;
    private String scheduledHash;
    private ActionBarMenuSubItem screenItem;
    private ActionBarMenuItem screenShareItem;
    private AnimatorSet scrimAnimatorSet;
    private GroupCallFullscreenAdapter.GroupCallUserCell scrimFullscreenView;
    private GroupCallGridCell scrimGridView;
    private Paint scrimPaint;
    private View scrimPopupLayout;
    private ActionBarPopupWindow scrimPopupWindow;
    private GroupCallMiniTextureView scrimRenderer;
    private GroupCallUserCell scrimView;
    private boolean scrimViewAttached;
    private float scrollOffsetY;
    private TLRPC.Peer selfPeer;
    private int shaderBitmapSize;
    private Drawable shadowDrawable;
    private ShareAlert shareAlert;
    private float showLightingProgress;
    private float showWavesProgress;
    private VoIPToggleButton soundButton;
    private float soundButtonScale;
    private ActionBarMenuSubItem soundItem;
    private View soundItemDivider;
    private final GridLayoutManager.SpanSizeLookup spanSizeLookup;
    private boolean startingGroupCall;
    private WeavingState[] states;
    public final ArrayList statusIconPool;
    ObjectAnimator subtitleYAnimator;
    private float switchProgress;
    private float switchToButtonInt2;
    private float switchToButtonProgress;
    GroupCallTabletGridAdapter tabletGridAdapter;
    RecyclerListView tabletVideoGridView;
    private LightningView textureLightningView;
    private float textureLightningViewAnimatingAlpha;
    private final BlobDrawable tinyWaveDrawable;
    private LinearLayout titleLayout;
    private AudioPlayerAlert.ClippingTextViewSwitcher titleTextView;
    private UndoView[] undoView;
    private Runnable unmuteRunnable;
    private Runnable updateCallRecordRunnable;
    private Runnable updateSchedeulRunnable;
    private final Runnable updateTextureLightningRunnable;
    private boolean useBlur;
    private TLObject userSwitchObject;
    LongSparseIntArray visiblePeerIds;
    public final ArrayList visibleVideoParticipants;
    private Boolean wasExpandBigSize;
    private Boolean wasNotInLayoutFullscreen;
    private WatchersView watchersView;
    public static volatile DispatchQueue updateTextureLightningQueue = new DispatchQueue("updateTextureLightningQueue");
    public static final Property COLOR_PROGRESS = new AnimationProperties.FloatProperty("colorProgress") { // from class: org.telegram.ui.GroupCallActivity.3
        @Override // android.util.Property
        public Float get(GroupCallActivity groupCallActivity) {
            return Float.valueOf(groupCallActivity.getColorProgress());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(GroupCallActivity groupCallActivity, float f) {
            groupCallActivity.setColorProgress(f);
        }
    };

    /* loaded from: classes4.dex */
    class 18 extends FrameLayout {
        AnimatorSet currentButtonsAnimation;
        int currentLightColor;
        final OvershootInterpolator overshootInterpolator;

        18(Context context) {
            super(context);
            this.overshootInterpolator = new OvershootInterpolator(1.5f);
        }

        /* JADX WARN: Code restructure failed: missing block: B:103:0x0370, code lost:
        
            if (r25.this$0.showWavesProgress < 0.0f) goto L104;
         */
        /* JADX WARN: Code restructure failed: missing block: B:152:0x0694, code lost:
        
            if (r25.this$0.prevState.currentState == r5) goto L167;
         */
        /* JADX WARN: Code restructure failed: missing block: B:154:0x06dc, code lost:
        
            if (r25.this$0.paint.getShader() != null) goto L173;
         */
        /* JADX WARN: Code restructure failed: missing block: B:156:0x06e4, code lost:
        
            if (r25.this$0.isRtmpStream() != false) goto L173;
         */
        /* JADX WARN: Code restructure failed: missing block: B:157:0x06e6, code lost:
        
            r25.this$0.paint.setColor(org.telegram.messenger.AndroidUtilities.getOffsetColor(org.telegram.ui.ActionBar.Theme.getColor(org.telegram.ui.ActionBar.Theme.key_voipgroup_listViewBackgroundUnscrolled), org.telegram.ui.ActionBar.Theme.getColor(org.telegram.ui.ActionBar.Theme.key_voipgroup_disabledButton), r25.this$0.colorProgress, 1.0f));
         */
        /* JADX WARN: Code restructure failed: missing block: B:159:0x070d, code lost:
        
            if (r25.this$0.isRtmpStream() == false) goto L176;
         */
        /* JADX WARN: Code restructure failed: missing block: B:160:0x070f, code lost:
        
            r2 = org.telegram.ui.ActionBar.Theme.getColor(org.telegram.ui.ActionBar.Theme.key_voipgroup_rtmpButton);
            r5 = org.telegram.ui.ActionBar.Theme.key_voipgroup_disabledButton;
            r2 = org.telegram.messenger.AndroidUtilities.getOffsetColor(r2, org.telegram.ui.ActionBar.Theme.getColor(r5), r25.this$0.colorProgress, 1.0f);
            r25.this$0.paint.setColor(r2);
            r25.this$0.paint.setShader(null);
            r25.this$0.soundButton.setBackgroundColor(org.telegram.messenger.AndroidUtilities.getOffsetColor(org.telegram.ui.ActionBar.Theme.getColor(org.telegram.ui.ActionBar.Theme.key_voipgroup_listViewBackgroundUnscrolled), org.telegram.ui.ActionBar.Theme.getColor(r5), r25.this$0.colorProgress, 1.0f), r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:161:0x0759, code lost:
        
            r2 = (int) (r25.this$0.muteButton.getX() + (r25.this$0.muteButton.getMeasuredWidth() / 2));
            r5 = (int) (r25.this$0.muteButton.getY() + (r25.this$0.muteButton.getMeasuredHeight() / 2));
            r25.this$0.radialMatrix.setTranslate(r2, r5);
            r25.this$0.radialGradient.setLocalMatrix(r25.this$0.radialMatrix);
            r25.this$0.paint.setAlpha((int) ((76.0f * r12) * r25.this$0.switchToButtonProgress));
         */
        /* JADX WARN: Code restructure failed: missing block: B:162:0x07c3, code lost:
        
            if (r25.this$0.switchToButtonProgress <= 0.0f) goto L180;
         */
        /* JADX WARN: Code restructure failed: missing block: B:163:0x07c5, code lost:
        
            if (r7 != 1) goto L180;
         */
        /* JADX WARN: Code restructure failed: missing block: B:164:0x07c7, code lost:
        
            r6 = r25.this$0.leaveBackgroundPaint.getAlpha();
            r25.this$0.leaveBackgroundPaint.setAlpha((int) (r6 * r25.this$0.switchToButtonProgress));
            r9 = org.telegram.messenger.AndroidUtilities.dp(r11) / 2.0f;
            r26.drawCircle(r25.this$0.leaveButton.getX() + (r25.this$0.leaveButton.getMeasuredWidth() / 2), r25.this$0.leaveButton.getY() + r9, r9, r25.this$0.leaveBackgroundPaint);
            r25.this$0.leaveBackgroundPaint.setAlpha(r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:165:0x0821, code lost:
        
            r26.save();
            r26.scale(org.telegram.ui.Components.BlobDrawable.GLOBAL_SCALE * r25.this$0.muteButton.getScaleX(), org.telegram.ui.Components.BlobDrawable.GLOBAL_SCALE * r25.this$0.muteButton.getScaleX(), r2, r5);
            r26.save();
         */
        /* JADX WARN: Code restructure failed: missing block: B:166:0x0848, code lost:
        
            if (org.telegram.ui.GroupCallActivity.isLandscapeMode == false) goto L183;
         */
        /* JADX WARN: Code restructure failed: missing block: B:167:0x084a, code lost:
        
            r9 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:168:0x0861, code lost:
        
            r11 = org.telegram.ui.Components.BlobDrawable.SCALE_BIG_MIN + ((org.telegram.ui.Components.BlobDrawable.SCALE_BIG * r25.this$0.amplitude) * r10);
            r26.scale(r25.this$0.showLightingProgress * r11, r11 * r25.this$0.showLightingProgress, r2, r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:169:0x0883, code lost:
        
            if (r7 != 1) goto L191;
         */
        /* JADX WARN: Code restructure failed: missing block: B:171:0x088b, code lost:
        
            if (r25.this$0.isRtmpStream() != false) goto L191;
         */
        /* JADX WARN: Code restructure failed: missing block: B:173:0x0893, code lost:
        
            if (org.telegram.messenger.LiteMode.isEnabled(512) == false) goto L191;
         */
        /* JADX WARN: Code restructure failed: missing block: B:174:0x0895, code lost:
        
            r11 = (org.telegram.ui.Components.BlobDrawable.LIGHT_GRADIENT_SIZE * r25.this$0.scheduleButtonsScale) + 0.7f;
            r26.save();
            r26.scale(r11, r11, r2, r5);
            r11 = r25.this$0.radialPaint.getAlpha();
            r25.this$0.radialPaint.setAlpha((int) ((r11 * r25.this$0.switchToButtonProgress) * (1.0f - r25.this$0.progressToHideUi)));
            r26.drawCircle(r2, r5, org.telegram.messenger.AndroidUtilities.dp(160.0f), r25.this$0.radialPaint);
            r25.this$0.radialPaint.setAlpha(r11);
            r26.restore();
         */
        /* JADX WARN: Code restructure failed: missing block: B:175:0x08f4, code lost:
        
            r26.restore();
         */
        /* JADX WARN: Code restructure failed: missing block: B:176:0x08fd, code lost:
        
            if (r25.this$0.isRtmpStream() != false) goto L197;
         */
        /* JADX WARN: Code restructure failed: missing block: B:178:0x0908, code lost:
        
            if (r25.this$0.switchToButtonProgress <= 0.0f) goto L197;
         */
        /* JADX WARN: Code restructure failed: missing block: B:179:0x090a, code lost:
        
            r26.save();
            r10 = org.telegram.ui.Components.BlobDrawable.SCALE_BIG_MIN + (((org.telegram.ui.Components.BlobDrawable.SCALE_BIG * r25.this$0.amplitude) * r3) * r25.this$0.scheduleButtonsScale);
            r26.scale(r10, r10, r2, r5);
            r25.this$0.bigWaveDrawable.draw(r2, r5, r26, r25.this$0.paint);
            r26.restore();
            r26.save();
            r10 = org.telegram.ui.Components.BlobDrawable.SCALE_SMALL_MIN + (((org.telegram.ui.Components.BlobDrawable.SCALE_SMALL * r25.this$0.amplitude) * r3) * r25.this$0.scheduleButtonsScale);
            r26.scale(r10, r10, r2, r5);
            r25.this$0.tinyWaveDrawable.draw(r2, r5, r26, r25.this$0.paint);
            r26.restore();
         */
        /* JADX WARN: Code restructure failed: missing block: B:181:0x096a, code lost:
        
            if (org.telegram.ui.GroupCallActivity.isLandscapeMode == false) goto L203;
         */
        /* JADX WARN: Code restructure failed: missing block: B:182:0x096c, code lost:
        
            if (r7 != 0) goto L202;
         */
        /* JADX WARN: Code restructure failed: missing block: B:183:0x096e, code lost:
        
            r10 = r25.this$0.paint;
            r11 = (int) (r25.this$0.switchToButtonInt2 * 255.0f);
         */
        /* JADX WARN: Code restructure failed: missing block: B:184:0x097f, code lost:
        
            r10.setAlpha(r11);
         */
        /* JADX WARN: Code restructure failed: missing block: B:186:0x09ae, code lost:
        
            if (r25.currentButtonsAnimation != null) goto L210;
         */
        /* JADX WARN: Code restructure failed: missing block: B:187:0x09b0, code lost:
        
            r25.this$0.muteButton.setTranslationY(r9);
         */
        /* JADX WARN: Code restructure failed: missing block: B:189:0x09bb, code lost:
        
            if (org.telegram.ui.GroupCallActivity.isLandscapeMode == false) goto L213;
         */
        /* JADX WARN: Code restructure failed: missing block: B:190:0x09bd, code lost:
        
            r10 = 1.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:191:0x09c6, code lost:
        
            r9 = (getMeasuredWidth() / 2) - org.telegram.messenger.AndroidUtilities.dp(21.0f);
            r14 = org.telegram.messenger.AndroidUtilities.dp(24.0f);
            r9 = (r9 + ((r4 - r9) * r10)) * r25.this$0.scheduleButtonsScale;
            r14 = (r14 + ((r4 - r14) * r10)) * r25.this$0.scheduleButtonsScale;
            r25.this$0.rect.set(r2 - r9, r5 - r14, r9 + r2, r14 + r5);
            r9 = org.telegram.messenger.AndroidUtilities.dp(4.0f) + ((r4 - org.telegram.messenger.AndroidUtilities.dp(4.0f)) * r10);
            r26.drawRoundRect(r25.this$0.rect, r9, r9, r25.this$0.paint);
         */
        /* JADX WARN: Code restructure failed: missing block: B:192:0x0a24, code lost:
        
            if (r7 != 1) goto L222;
         */
        /* JADX WARN: Code restructure failed: missing block: B:194:0x0a2f, code lost:
        
            if (r25.this$0.currentState.currentState != 3) goto L223;
         */
        /* JADX WARN: Code restructure failed: missing block: B:196:0x0a37, code lost:
        
            if (r25.this$0.isRtmpStream() == false) goto L221;
         */
        /* JADX WARN: Code restructure failed: missing block: B:197:0x0a39, code lost:
        
            r25.this$0.radialProgressView.setSize((int) ((r9 * 2.0f) - org.telegram.messenger.AndroidUtilities.dp(4.0f)));
         */
        /* JADX WARN: Code restructure failed: missing block: B:198:0x0a4b, code lost:
        
            r25.this$0.radialProgressView.draw(r26, r2, r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:199:0x0a56, code lost:
        
            r26.restore();
         */
        /* JADX WARN: Code restructure failed: missing block: B:200:0x0a5b, code lost:
        
            if (org.telegram.ui.GroupCallActivity.isLandscapeMode == false) goto L229;
         */
        /* JADX WARN: Code restructure failed: missing block: B:202:0x0a66, code lost:
        
            if (r25.this$0.switchToButtonInt2 != 0.0f) goto L228;
         */
        /* JADX WARN: Code restructure failed: missing block: B:203:0x0a68, code lost:
        
            r25.this$0.paint.setAlpha(org.telegram.messenger.NotificationCenter.closeSearchByActiveAction);
            r2 = r25.this$0.scheduleButtonTextView.getX() - getX();
            r9 = r25.this$0.scheduleButtonTextView.getY() - getY();
            r25.this$0.rect.set(r2, r9, r25.this$0.scheduleButtonTextView.getMeasuredWidth() + r2, r25.this$0.scheduleButtonTextView.getMeasuredHeight() + r9);
            r26.drawRoundRect(r25.this$0.rect, org.telegram.messenger.AndroidUtilities.dp(4.0f), org.telegram.messenger.AndroidUtilities.dp(4.0f), r25.this$0.paint);
         */
        /* JADX WARN: Code restructure failed: missing block: B:210:0x09c0, code lost:
        
            r10 = r25.this$0.switchToButtonInt2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:211:0x0985, code lost:
        
            r10 = r25.this$0.paint;
            r11 = (int) ((r12 * 255.0f) * r25.this$0.switchToButtonInt2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:212:0x0999, code lost:
        
            r10 = r25.this$0.paint;
         */
        /* JADX WARN: Code restructure failed: missing block: B:213:0x099f, code lost:
        
            if (r7 != 0) goto L206;
         */
        /* JADX WARN: Code restructure failed: missing block: B:214:0x09a1, code lost:
        
            r11 = org.telegram.messenger.NotificationCenter.closeSearchByActiveAction;
         */
        /* JADX WARN: Code restructure failed: missing block: B:215:0x09a4, code lost:
        
            r10.setAlpha((int) (r12 * 255.0f));
         */
        /* JADX WARN: Code restructure failed: missing block: B:217:0x084e, code lost:
        
            r9 = org.telegram.messenger.AndroidUtilities.dp(65.0f) * (1.0f - r25.this$0.switchToButtonInt2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:218:0x06ca, code lost:
        
            r4 = r4 - (org.telegram.messenger.AndroidUtilities.dp(2.0f) * r12);
         */
        /* JADX WARN: Code restructure failed: missing block: B:226:0x06c8, code lost:
        
            if (r25.this$0.currentState.currentState == r5) goto L167;
         */
        /* JADX WARN: Removed duplicated region for block: B:100:0x0356  */
        /* JADX WARN: Removed duplicated region for block: B:138:0x0afa  */
        /* JADX WARN: Removed duplicated region for block: B:140:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:144:0x0658  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x014c  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x0379  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x039e  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x03ba  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            boolean z;
            int i;
            float f;
            float f2;
            Paint paint;
            WeavingState weavingState;
            boolean z2;
            boolean z3;
            float f3;
            int i2;
            int i3;
            int i4;
            if (GroupCallActivity.this.contentFullyOverlayed && GroupCallActivity.this.useBlur) {
                return;
            }
            int measuredWidth = (getMeasuredWidth() - getMeasuredHeight()) / 2;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - GroupCallActivity.this.lastUpdateTime;
            GroupCallActivity.this.lastUpdateTime = elapsedRealtime;
            if (j > 20) {
                j = 17;
            }
            long j2 = j;
            if (GroupCallActivity.this.currentState != null) {
                GroupCallActivity.this.currentState.update(0, measuredWidth, getMeasuredHeight(), j2, GroupCallActivity.this.amplitude);
            }
            GroupCallActivity.this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(62.0f);
            GroupCallActivity.this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(62.0f) + (AndroidUtilities.dp(20.0f) * BlobDrawable.FORM_SMALL_MAX);
            GroupCallActivity.this.bigWaveDrawable.minRadius = AndroidUtilities.dp(65.0f);
            GroupCallActivity.this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(65.0f) + (AndroidUtilities.dp(20.0f) * BlobDrawable.FORM_BIG_MAX);
            if (GroupCallActivity.this.animateToAmplitude != GroupCallActivity.this.amplitude) {
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                GroupCallActivity.access$11216(groupCallActivity, groupCallActivity.animateAmplitudeDiff * ((float) j2));
                if (GroupCallActivity.this.animateAmplitudeDiff <= 0.0f ? GroupCallActivity.this.amplitude < GroupCallActivity.this.animateToAmplitude : GroupCallActivity.this.amplitude > GroupCallActivity.this.animateToAmplitude) {
                    GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                    groupCallActivity2.amplitude = groupCallActivity2.animateToAmplitude;
                }
            }
            int i5 = 3;
            int i6 = 0;
            if (GroupCallActivity.this.prevState != null && GroupCallActivity.this.prevState.currentState == 3) {
                GroupCallActivity.this.radialProgressView.toCircle(true, true);
                if (!GroupCallActivity.this.radialProgressView.isCircle()) {
                    z = false;
                    if (z) {
                        if (GroupCallActivity.this.switchProgress != 1.0f) {
                            if (GroupCallActivity.this.prevState == null || GroupCallActivity.this.prevState.currentState != 3) {
                                GroupCallActivity.access$13716(GroupCallActivity.this, ((float) j2) / 180.0f);
                            } else {
                                GroupCallActivity.access$13716(GroupCallActivity.this, ((float) j2) / 100.0f);
                            }
                            if (GroupCallActivity.this.switchProgress >= 1.0f) {
                                GroupCallActivity.this.switchProgress = 1.0f;
                                GroupCallActivity.this.prevState = null;
                                if (GroupCallActivity.this.currentState != null && GroupCallActivity.this.currentState.currentState == 3) {
                                    GroupCallActivity.this.radialProgressView.toCircle(false, true);
                                }
                            }
                            GroupCallActivity.this.invalidateColors = true;
                        }
                        if (GroupCallActivity.this.invalidateColors && GroupCallActivity.this.currentState != null) {
                            GroupCallActivity.this.invalidateColors = false;
                            if (GroupCallActivity.this.prevState != null) {
                                GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                                groupCallActivity3.fillColors(groupCallActivity3.prevState.currentState, GroupCallActivity.this.colorsTmp);
                                int i7 = GroupCallActivity.this.colorsTmp[0];
                                int i8 = GroupCallActivity.this.colorsTmp[1];
                                int i9 = GroupCallActivity.this.colorsTmp[2];
                                GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                                groupCallActivity4.fillColors(groupCallActivity4.currentState.currentState, GroupCallActivity.this.colorsTmp);
                                i2 = ColorUtils.blendARGB(i7, GroupCallActivity.this.colorsTmp[0], GroupCallActivity.this.switchProgress);
                                i3 = ColorUtils.blendARGB(i8, GroupCallActivity.this.colorsTmp[1], GroupCallActivity.this.switchProgress);
                                i4 = ColorUtils.blendARGB(i9, GroupCallActivity.this.colorsTmp[2], GroupCallActivity.this.switchProgress);
                            } else {
                                GroupCallActivity groupCallActivity5 = GroupCallActivity.this;
                                groupCallActivity5.fillColors(groupCallActivity5.currentState.currentState, GroupCallActivity.this.colorsTmp);
                                i2 = GroupCallActivity.this.colorsTmp[0];
                                i3 = GroupCallActivity.this.colorsTmp[1];
                                i4 = GroupCallActivity.this.colorsTmp[2];
                            }
                            if (this.currentLightColor != i2) {
                                GroupCallActivity.this.radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(100.0f), new int[]{ColorUtils.setAlphaComponent(i2, 60), ColorUtils.setAlphaComponent(i2, 0)}, (float[]) null, Shader.TileMode.CLAMP);
                                GroupCallActivity.this.radialPaint.setShader(GroupCallActivity.this.radialGradient);
                                this.currentLightColor = i2;
                            }
                            GroupCallActivity.this.soundButton.setBackgroundColor(i4, i3);
                            GroupCallActivity.this.cameraButton.setBackgroundColor(i4, i3);
                            GroupCallActivity.this.flipButton.setBackgroundColor(i4, i3);
                        }
                        if (GroupCallActivity.this.currentState != null) {
                            z2 = GroupCallActivity.this.currentState.currentState == 1 || GroupCallActivity.this.currentState.currentState == 0 || GroupCallActivity.isGradientState(GroupCallActivity.this.currentState.currentState);
                            if (GroupCallActivity.this.currentState.currentState != 3) {
                                z3 = true;
                                if (GroupCallActivity.this.prevState == null && GroupCallActivity.this.currentState != null && GroupCallActivity.this.currentState.currentState == 3) {
                                    GroupCallActivity.access$14324(GroupCallActivity.this, ((float) j2) / 180.0f);
                                    if (GroupCallActivity.this.showWavesProgress < 0.0f) {
                                        f3 = 0.0f;
                                        GroupCallActivity.this.showWavesProgress = f3;
                                    }
                                    if (!z3) {
                                    }
                                    if (!z3) {
                                        GroupCallActivity.access$14424(GroupCallActivity.this, ((float) j2) / 350.0f);
                                        if (GroupCallActivity.this.showLightingProgress < 0.0f) {
                                        }
                                    }
                                } else {
                                    if (!z2 && GroupCallActivity.this.showWavesProgress != 1.0f) {
                                        GroupCallActivity.access$14316(GroupCallActivity.this, ((float) j2) / 350.0f);
                                        if (GroupCallActivity.this.showWavesProgress > 1.0f) {
                                            GroupCallActivity.this.showWavesProgress = 1.0f;
                                        }
                                    } else if (!z2) {
                                        f3 = 0.0f;
                                        if (GroupCallActivity.this.showWavesProgress != 0.0f) {
                                            GroupCallActivity.access$14324(GroupCallActivity.this, ((float) j2) / 350.0f);
                                        }
                                    }
                                    if (!z3 && GroupCallActivity.this.showLightingProgress != 1.0f) {
                                        GroupCallActivity.access$14416(GroupCallActivity.this, ((float) j2) / 350.0f);
                                        if (GroupCallActivity.this.showLightingProgress > 1.0f) {
                                            GroupCallActivity.this.showLightingProgress = 1.0f;
                                        }
                                    } else if (!z3 && GroupCallActivity.this.showLightingProgress != 0.0f) {
                                        GroupCallActivity.access$14424(GroupCallActivity.this, ((float) j2) / 350.0f);
                                        if (GroupCallActivity.this.showLightingProgress < 0.0f) {
                                            GroupCallActivity.this.showLightingProgress = 0.0f;
                                        }
                                    }
                                }
                            }
                        } else {
                            z2 = false;
                        }
                        z3 = false;
                        if (GroupCallActivity.this.prevState == null) {
                        }
                        if (!z2) {
                        }
                        if (!z2) {
                        }
                        if (!z3) {
                        }
                        if (!z3) {
                        }
                    }
                    float interpolation = (this.overshootInterpolator.getInterpolation(GroupCallActivity.this.showWavesProgress) * 0.6f) + 0.4f;
                    GroupCallActivity.this.bigWaveDrawable.update(GroupCallActivity.this.amplitude, 1.0f);
                    GroupCallActivity.this.tinyWaveDrawable.update(GroupCallActivity.this.amplitude, 1.0f);
                    float f4 = 0.5f;
                    float f5 = 52.0f;
                    if (GroupCallActivity.this.prevState != null || GroupCallActivity.this.currentState == null || GroupCallActivity.this.isRtmpStream() || !(GroupCallActivity.this.currentState.currentState == 3 || GroupCallActivity.this.prevState.currentState == 3)) {
                        i = 2;
                        while (i6 < i) {
                            float dp = AndroidUtilities.dp(57.0f);
                            if (i6 == 0 && GroupCallActivity.this.prevState != null) {
                                if (!GroupCallActivity.this.isRtmpStream()) {
                                    GroupCallActivity.this.paint.setShader(GroupCallActivity.this.prevState.shader);
                                }
                                f = 1.0f - GroupCallActivity.this.switchProgress;
                            } else if (i6 != 1 || GroupCallActivity.this.currentState == null) {
                                i6++;
                                i = 2;
                                i5 = 3;
                                f4 = 0.5f;
                                f5 = 52.0f;
                            } else {
                                if (!GroupCallActivity.this.isRtmpStream()) {
                                    GroupCallActivity.this.paint.setShader(GroupCallActivity.this.currentState.shader);
                                }
                                f = GroupCallActivity.this.switchProgress;
                            }
                        }
                    } else {
                        if (GroupCallActivity.this.currentState.currentState == 3) {
                            f2 = GroupCallActivity.this.switchProgress;
                            paint = GroupCallActivity.this.paint;
                            weavingState = GroupCallActivity.this.prevState;
                        } else {
                            f2 = 1.0f - GroupCallActivity.this.switchProgress;
                            paint = GroupCallActivity.this.paint;
                            weavingState = GroupCallActivity.this.currentState;
                        }
                        paint.setShader(weavingState.shader);
                        GroupCallActivity.this.paintTmp.setColor(AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_listViewBackgroundUnscrolled), Theme.getColor(Theme.key_voipgroup_disabledButton), GroupCallActivity.this.colorProgress, 1.0f));
                        float x = (int) (GroupCallActivity.this.muteButton.getX() + (GroupCallActivity.this.muteButton.getMeasuredWidth() / 2));
                        float y = (int) (GroupCallActivity.this.muteButton.getY() + (GroupCallActivity.this.muteButton.getMeasuredHeight() / 2));
                        GroupCallActivity.this.radialMatrix.setTranslate(x, y);
                        GroupCallActivity.this.radialGradient.setLocalMatrix(GroupCallActivity.this.radialMatrix);
                        GroupCallActivity.this.paint.setAlpha(76);
                        if (GroupCallActivity.this.call != null) {
                            float dp2 = AndroidUtilities.dp(52.0f) / 2.0f;
                            canvas.drawCircle(GroupCallActivity.this.leaveButton.getX() + (GroupCallActivity.this.leaveButton.getMeasuredWidth() / 2.0f), GroupCallActivity.this.leaveButton.getY() + dp2, dp2, GroupCallActivity.this.leaveBackgroundPaint);
                        }
                        canvas.save();
                        canvas.scale(BlobDrawable.GLOBAL_SCALE * GroupCallActivity.this.muteButton.getScaleX(), BlobDrawable.GLOBAL_SCALE * GroupCallActivity.this.muteButton.getScaleY(), x, y);
                        canvas.save();
                        float f6 = BlobDrawable.SCALE_BIG_MIN + (BlobDrawable.SCALE_BIG * GroupCallActivity.this.amplitude * 0.5f);
                        canvas.scale(GroupCallActivity.this.showLightingProgress * f6, f6 * GroupCallActivity.this.showLightingProgress, x, y);
                        float f7 = BlobDrawable.LIGHT_GRADIENT_SIZE + 0.7f;
                        canvas.save();
                        canvas.scale(f7, f7, x, y);
                        canvas.drawCircle(x, y, AndroidUtilities.dp(160.0f), GroupCallActivity.this.radialPaint);
                        canvas.restore();
                        canvas.restore();
                        if (GroupCallActivity.this.call != null) {
                            canvas.save();
                            float f8 = (BlobDrawable.SCALE_BIG_MIN + (BlobDrawable.SCALE_BIG * GroupCallActivity.this.amplitude * GroupCallActivity.this.scheduleButtonsScale)) * interpolation;
                            canvas.scale(f8, f8, x, y);
                            GroupCallActivity.this.bigWaveDrawable.draw(x, y, canvas, GroupCallActivity.this.paint);
                            canvas.restore();
                            canvas.save();
                            float f9 = (BlobDrawable.SCALE_SMALL_MIN + (BlobDrawable.SCALE_SMALL * GroupCallActivity.this.amplitude * GroupCallActivity.this.scheduleButtonsScale)) * interpolation;
                            canvas.scale(f9, f9, x, y);
                            GroupCallActivity.this.tinyWaveDrawable.draw(x, y, canvas, GroupCallActivity.this.paint);
                            canvas.restore();
                        }
                        GroupCallActivity.this.paint.setAlpha(NotificationCenter.closeSearchByActiveAction);
                        if (z) {
                            canvas.drawCircle(x, y, AndroidUtilities.dp(57.0f), GroupCallActivity.this.paint);
                            GroupCallActivity.this.paint.setColor(Theme.getColor(Theme.key_voipgroup_connectingProgress));
                            if (f2 != 0.0f) {
                                GroupCallActivity.this.paint.setAlpha((int) (f2 * 255.0f));
                                GroupCallActivity.this.paint.setShader(null);
                                canvas.drawCircle(x, y, AndroidUtilities.dp(57.0f), GroupCallActivity.this.paint);
                            }
                        }
                        canvas.drawCircle(x, y, AndroidUtilities.dp(55.0f) * f2, GroupCallActivity.this.paintTmp);
                        if (!z) {
                            GroupCallActivity.this.radialProgressView.draw(canvas, x, y);
                        }
                        canvas.restore();
                    }
                    super.dispatchDraw(canvas);
                    if (GroupCallActivity.this.renderersContainer.isAnimating()) {
                        invalidate();
                        return;
                    }
                    return;
                }
            } else if (GroupCallActivity.this.prevState != null && GroupCallActivity.this.currentState != null && GroupCallActivity.this.currentState.currentState == 3) {
                GroupCallActivity.this.radialProgressView.toCircle(true, false);
            }
            z = true;
            if (z) {
            }
            float interpolation2 = (this.overshootInterpolator.getInterpolation(GroupCallActivity.this.showWavesProgress) * 0.6f) + 0.4f;
            GroupCallActivity.this.bigWaveDrawable.update(GroupCallActivity.this.amplitude, 1.0f);
            GroupCallActivity.this.tinyWaveDrawable.update(GroupCallActivity.this.amplitude, 1.0f);
            float f42 = 0.5f;
            float f52 = 52.0f;
            if (GroupCallActivity.this.prevState != null) {
            }
            i = 2;
            while (i6 < i) {
            }
            super.dispatchDraw(canvas);
            if (GroupCallActivity.this.renderersContainer.isAnimating()) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view != GroupCallActivity.this.muteButton || view.getScaleX() == 1.0f) {
                return super.drawChild(canvas, view, j);
            }
            canvas.save();
            float scaleX = (((1.0f / GroupCallActivity.this.muteButton.getScaleX()) - 1.0f) * 0.2f) + 1.0f;
            canvas.scale(scaleX, scaleX, view.getX() + (view.getMeasuredWidth() / 2.0f), view.getY() + (view.getMeasuredHeight() / 2.0f));
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int measuredWidth;
            VoIPToggleButton voIPToggleButton;
            int i5;
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(122.0f)) / 2;
            int measuredHeight = getMeasuredHeight();
            int i6 = GroupCallActivity.this.cameraButton.getVisibility() != 0 ? 4 : 5;
            if (GroupCallActivity.this.soundButton.getVisibility() != 0) {
                i6--;
            }
            if (GroupCallActivity.this.flipButton.getVisibility() != 0) {
                i6--;
            }
            if (GroupCallActivity.isLandscapeMode && !GroupCallActivity.isTabletMode) {
                int measuredHeight2 = getMeasuredHeight() / i6;
                if (GroupCallActivity.this.soundButton.getVisibility() == 0) {
                    int i7 = measuredHeight2 / 2;
                    int measuredHeight3 = i7 - (GroupCallActivity.this.cameraButton.getMeasuredHeight() / 2);
                    int measuredWidth3 = (getMeasuredWidth() - GroupCallActivity.this.cameraButton.getMeasuredWidth()) >> 1;
                    GroupCallActivity.this.cameraButton.layout(measuredWidth3, measuredHeight3, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth3, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight3);
                    int measuredHeight4 = (i7 + (i6 == 4 ? measuredHeight2 : 0)) - (GroupCallActivity.this.soundButton.getMeasuredHeight() / 2);
                    int measuredWidth4 = (getMeasuredWidth() - GroupCallActivity.this.soundButton.getMeasuredWidth()) >> 1;
                    GroupCallActivity.this.soundButton.layout(measuredWidth4, measuredHeight4, GroupCallActivity.this.soundButton.getMeasuredWidth() + measuredWidth4, GroupCallActivity.this.soundButton.getMeasuredHeight() + measuredHeight4);
                } else {
                    int i8 = measuredHeight2 / 2;
                    int measuredHeight5 = i8 - (GroupCallActivity.this.flipButton.getMeasuredHeight() / 2);
                    int measuredWidth5 = (getMeasuredWidth() - GroupCallActivity.this.flipButton.getMeasuredWidth()) >> 1;
                    GroupCallActivity.this.flipButton.layout(measuredWidth5, measuredHeight5, GroupCallActivity.this.flipButton.getMeasuredWidth() + measuredWidth5, GroupCallActivity.this.flipButton.getMeasuredHeight() + measuredHeight5);
                    int measuredHeight6 = (i8 + (i6 == 4 ? measuredHeight2 : 0)) - (GroupCallActivity.this.cameraButton.getMeasuredHeight() / 2);
                    int measuredWidth6 = (getMeasuredWidth() - GroupCallActivity.this.cameraButton.getMeasuredWidth()) >> 1;
                    GroupCallActivity.this.cameraButton.layout(measuredWidth6, measuredHeight6, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth6, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight6);
                }
                int i9 = measuredHeight2 / 2;
                int measuredHeight7 = ((i6 == 4 ? measuredHeight2 * 3 : measuredHeight2 * 2) + i9) - (GroupCallActivity.this.leaveButton.getMeasuredHeight() / 2);
                int measuredWidth7 = (getMeasuredWidth() - GroupCallActivity.this.leaveButton.getMeasuredWidth()) >> 1;
                GroupCallActivity.this.leaveButton.layout(measuredWidth7, measuredHeight7, GroupCallActivity.this.leaveButton.getMeasuredWidth() + measuredWidth7, GroupCallActivity.this.leaveButton.getMeasuredHeight() + measuredHeight7);
                int measuredWidth8 = (((i6 == 4 ? measuredHeight2 * 2 : measuredHeight2) + i9) - (GroupCallActivity.this.muteButton.getMeasuredWidth() / 2)) - AndroidUtilities.dp(4.0f);
                int measuredWidth9 = (getMeasuredWidth() - GroupCallActivity.this.muteButton.getMeasuredWidth()) >> 1;
                if (i6 == 3) {
                    measuredWidth8 -= AndroidUtilities.dp(6.0f);
                }
                GroupCallActivity.this.muteButton.layout(measuredWidth9, measuredWidth8, GroupCallActivity.this.muteButton.getMeasuredWidth() + measuredWidth9, GroupCallActivity.this.muteButton.getMeasuredHeight() + measuredWidth8);
                GroupCallActivity.this.minimizeButton.layout(measuredWidth9, measuredWidth8, GroupCallActivity.this.minimizeButton.getMeasuredWidth() + measuredWidth9, GroupCallActivity.this.minimizeButton.getMeasuredHeight() + measuredWidth8);
                GroupCallActivity.this.expandButton.layout(measuredWidth9, measuredWidth8, GroupCallActivity.this.expandButton.getMeasuredWidth() + measuredWidth9, GroupCallActivity.this.expandButton.getMeasuredHeight() + measuredWidth8);
                float dp = AndroidUtilities.dp(52.0f) / (GroupCallActivity.this.muteButton.getMeasuredWidth() - AndroidUtilities.dp(8.0f));
                GroupCallActivity.this.muteButton.animate().cancel();
                GroupCallActivity.this.muteButton.setScaleX(dp);
                GroupCallActivity.this.muteButton.setScaleY(dp);
                for (int i10 = 0; i10 < 2; i10++) {
                    int measuredWidth10 = (getMeasuredWidth() - GroupCallActivity.this.muteLabel[i10].getMeasuredWidth()) >> 1;
                    int i11 = i6 == 4 ? measuredHeight2 * 2 : measuredHeight2;
                    int measuredWidth11 = ((i9 + i11) - (GroupCallActivity.this.muteButton.getMeasuredWidth() / 2)) - AndroidUtilities.dp(4.0f);
                    if (i6 == 3) {
                        measuredWidth11 -= AndroidUtilities.dp(6.0f);
                    }
                    int measuredWidth12 = (int) (measuredWidth11 + (GroupCallActivity.this.muteButton.getMeasuredWidth() * 0.687f) + AndroidUtilities.dp(4.0f));
                    if (GroupCallActivity.this.muteLabel[i10].getMeasuredHeight() + measuredWidth12 > i11 + measuredHeight2) {
                        measuredWidth12 -= AndroidUtilities.dp(4.0f);
                    }
                    GroupCallActivity.this.muteLabel[i10].layout(measuredWidth10, measuredWidth12, GroupCallActivity.this.muteLabel[i10].getMeasuredWidth() + measuredWidth10, GroupCallActivity.this.muteLabel[i10].getMeasuredHeight() + measuredWidth12);
                    GroupCallActivity.this.muteLabel[i10].setScaleX(0.687f);
                    GroupCallActivity.this.muteLabel[i10].setScaleY(0.687f);
                }
            } else if ((!GroupCallActivity.this.renderersContainer.inFullscreenMode || GroupCallActivity.isTabletMode) && !GroupCallActivity.this.isRtmpStream()) {
                int dp2 = AndroidUtilities.dp(0.0f);
                if (GroupCallActivity.this.soundButton.getVisibility() == 0) {
                    if (GroupCallActivity.this.cameraButton.getVisibility() == 0) {
                        int measuredWidth13 = (measuredWidth2 - GroupCallActivity.this.cameraButton.getMeasuredWidth()) / 2;
                        int measuredHeight8 = (measuredHeight - GroupCallActivity.this.cameraButton.getMeasuredHeight()) / 2;
                        GroupCallActivity.this.cameraButton.layout(measuredWidth13, measuredHeight8, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth13, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight8);
                        measuredWidth = (measuredWidth2 - GroupCallActivity.this.soundButton.getMeasuredWidth()) / 2;
                        voIPToggleButton = GroupCallActivity.this.leaveButton;
                    } else {
                        measuredWidth = (measuredWidth2 - GroupCallActivity.this.soundButton.getMeasuredWidth()) / 2;
                        voIPToggleButton = GroupCallActivity.this.soundButton;
                    }
                    int measuredHeight9 = (measuredHeight - voIPToggleButton.getMeasuredHeight()) / 2;
                    GroupCallActivity.this.soundButton.layout(measuredWidth, measuredHeight9, GroupCallActivity.this.soundButton.getMeasuredWidth() + measuredWidth, GroupCallActivity.this.soundButton.getMeasuredHeight() + measuredHeight9);
                } else {
                    int dp3 = GroupCallActivity.this.flipButton.getVisibility() == 0 ? AndroidUtilities.dp(28.0f) : 0;
                    int measuredWidth14 = (measuredWidth2 - GroupCallActivity.this.flipButton.getMeasuredWidth()) / 2;
                    int measuredHeight10 = (((measuredHeight - GroupCallActivity.this.flipButton.getMeasuredHeight()) / 2) + dp2) - dp3;
                    GroupCallActivity.this.flipButton.layout(measuredWidth14, measuredHeight10, GroupCallActivity.this.flipButton.getMeasuredWidth() + measuredWidth14, GroupCallActivity.this.flipButton.getMeasuredHeight() + measuredHeight10);
                    int measuredWidth15 = (measuredWidth2 - GroupCallActivity.this.cameraButton.getMeasuredWidth()) / 2;
                    int measuredHeight11 = ((measuredHeight - GroupCallActivity.this.cameraButton.getMeasuredHeight()) / 2) + dp2 + dp3;
                    GroupCallActivity.this.cameraButton.layout(measuredWidth15, measuredHeight11, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth15, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight11);
                }
                int measuredHeight12 = ((measuredHeight - GroupCallActivity.this.leaveButton.getMeasuredHeight()) / 2) + dp2;
                int measuredWidth16 = (getMeasuredWidth() - measuredWidth2) + ((measuredWidth2 - GroupCallActivity.this.leaveButton.getMeasuredWidth()) / 2);
                GroupCallActivity.this.leaveButton.layout(measuredWidth16, measuredHeight12, GroupCallActivity.this.leaveButton.getMeasuredWidth() + measuredWidth16, GroupCallActivity.this.leaveButton.getMeasuredHeight() + measuredHeight12);
                int measuredWidth17 = (getMeasuredWidth() - GroupCallActivity.this.muteButton.getMeasuredWidth()) / 2;
                int measuredHeight13 = ((measuredHeight - GroupCallActivity.this.muteButton.getMeasuredHeight()) / 2) - AndroidUtilities.dp(9.0f);
                GroupCallActivity.this.muteButton.layout(measuredWidth17, measuredHeight13, GroupCallActivity.this.muteButton.getMeasuredWidth() + measuredWidth17, GroupCallActivity.this.muteButton.getMeasuredHeight() + measuredHeight13);
                GroupCallActivity.this.minimizeButton.layout(measuredWidth17, measuredHeight13, GroupCallActivity.this.minimizeButton.getMeasuredWidth() + measuredWidth17, GroupCallActivity.this.minimizeButton.getMeasuredHeight() + measuredHeight13);
                GroupCallActivity.this.expandButton.layout(measuredWidth17, measuredHeight13, GroupCallActivity.this.expandButton.getMeasuredWidth() + measuredWidth17, GroupCallActivity.this.expandButton.getMeasuredHeight() + measuredHeight13);
                GroupCallActivity.this.muteButton.animate().setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).scaleX(1.0f).scaleY(1.0f).start();
                for (int i12 = 0; i12 < 2; i12++) {
                    int measuredWidth18 = (getMeasuredWidth() - GroupCallActivity.this.muteLabel[i12].getMeasuredWidth()) / 2;
                    int dp4 = (measuredHeight - AndroidUtilities.dp(12.0f)) - GroupCallActivity.this.muteLabel[i12].getMeasuredHeight();
                    GroupCallActivity.this.muteLabel[i12].layout(measuredWidth18, dp4, GroupCallActivity.this.muteLabel[i12].getMeasuredWidth() + measuredWidth18, GroupCallActivity.this.muteLabel[i12].getMeasuredHeight() + dp4);
                    GroupCallActivity.this.muteLabel[i12].animate().scaleX(1.0f).scaleY(1.0f).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                }
            } else {
                int measuredWidth19 = getMeasuredWidth() / i6;
                boolean z2 = !GroupCallActivity.this.renderersContainer.inFullscreenMode && GroupCallActivity.this.isRtmpStream();
                int dp5 = z2 ? AndroidUtilities.dp(27.0f) : 0;
                if (GroupCallActivity.this.soundButton.getVisibility() == 0) {
                    int i13 = measuredWidth19 / 2;
                    int measuredWidth20 = i13 - (GroupCallActivity.this.cameraButton.getMeasuredWidth() / 2);
                    int measuredHeight14 = getMeasuredHeight() - GroupCallActivity.this.cameraButton.getMeasuredHeight();
                    GroupCallActivity.this.cameraButton.layout(measuredWidth20, measuredHeight14, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth20, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight14);
                    int measuredWidth21 = (i13 + (i6 == 4 ? measuredWidth19 : 0)) - (GroupCallActivity.this.leaveButton.getMeasuredWidth() / 2);
                    int measuredHeight15 = (getMeasuredHeight() - GroupCallActivity.this.soundButton.getMeasuredHeight()) - dp5;
                    GroupCallActivity.this.soundButton.layout(measuredWidth21, measuredHeight15, GroupCallActivity.this.soundButton.getMeasuredWidth() + measuredWidth21, GroupCallActivity.this.soundButton.getMeasuredHeight() + measuredHeight15);
                } else {
                    int i14 = measuredWidth19 / 2;
                    int measuredWidth22 = ((i6 == 4 ? measuredWidth19 : 0) + i14) - (GroupCallActivity.this.cameraButton.getMeasuredWidth() / 2);
                    int measuredHeight16 = getMeasuredHeight() - GroupCallActivity.this.cameraButton.getMeasuredHeight();
                    GroupCallActivity.this.cameraButton.layout(measuredWidth22, measuredHeight16, GroupCallActivity.this.cameraButton.getMeasuredWidth() + measuredWidth22, GroupCallActivity.this.cameraButton.getMeasuredHeight() + measuredHeight16);
                    int measuredWidth23 = i14 - (GroupCallActivity.this.flipButton.getMeasuredWidth() / 2);
                    int measuredHeight17 = getMeasuredHeight() - GroupCallActivity.this.flipButton.getMeasuredHeight();
                    GroupCallActivity.this.flipButton.layout(measuredWidth23, measuredHeight17, GroupCallActivity.this.flipButton.getMeasuredWidth() + measuredWidth23, GroupCallActivity.this.flipButton.getMeasuredHeight() + measuredHeight17);
                }
                int i15 = measuredWidth19 / 2;
                int measuredWidth24 = ((i6 == 4 ? measuredWidth19 * 3 : measuredWidth19 * 2) + i15) - (GroupCallActivity.this.leaveButton.getMeasuredWidth() / 2);
                int measuredHeight18 = (getMeasuredHeight() - GroupCallActivity.this.leaveButton.getMeasuredHeight()) - dp5;
                GroupCallActivity.this.leaveButton.layout(measuredWidth24, measuredHeight18, GroupCallActivity.this.leaveButton.getMeasuredWidth() + measuredWidth24, GroupCallActivity.this.leaveButton.getMeasuredHeight() + measuredHeight18);
                int measuredWidth25 = (i15 + (i6 == 4 ? measuredWidth19 * 2 : measuredWidth19)) - (GroupCallActivity.this.muteButton.getMeasuredWidth() / 2);
                int measuredHeight19 = ((getMeasuredHeight() - GroupCallActivity.this.leaveButton.getMeasuredHeight()) - ((GroupCallActivity.this.muteButton.getMeasuredWidth() - AndroidUtilities.dp(52.0f)) / 2)) - dp5;
                GroupCallActivity.this.muteButton.layout(measuredWidth25, measuredHeight19, GroupCallActivity.this.muteButton.getMeasuredWidth() + measuredWidth25, GroupCallActivity.this.muteButton.getMeasuredHeight() + measuredHeight19);
                GroupCallActivity.this.minimizeButton.layout(measuredWidth25, measuredHeight19, GroupCallActivity.this.minimizeButton.getMeasuredWidth() + measuredWidth25, GroupCallActivity.this.minimizeButton.getMeasuredHeight() + measuredHeight19);
                GroupCallActivity.this.expandButton.layout(measuredWidth25, measuredHeight19, GroupCallActivity.this.expandButton.getMeasuredWidth() + measuredWidth25, GroupCallActivity.this.expandButton.getMeasuredHeight() + measuredHeight19);
                float dp6 = AndroidUtilities.dp(52.0f) / (GroupCallActivity.this.muteButton.getMeasuredWidth() - AndroidUtilities.dp(8.0f));
                GroupCallActivity.this.muteButton.animate().scaleX(dp6).scaleY(dp6).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                for (int i16 = 0; i16 < 2; i16++) {
                    int measuredWidth26 = (i6 == 4 ? measuredWidth19 * 2 : measuredWidth19) + ((measuredWidth19 - GroupCallActivity.this.muteLabel[i16].getMeasuredWidth()) / 2);
                    int dp7 = measuredHeight - AndroidUtilities.dp(27.0f);
                    if (z2) {
                        dp7 -= AndroidUtilities.dp(GroupCallActivity.this.renderersContainer.inFullscreenMode ? 2.0f : 25.0f);
                    }
                    GroupCallActivity.this.muteLabel[i16].layout(measuredWidth26, dp7, GroupCallActivity.this.muteLabel[i16].getMeasuredWidth() + measuredWidth26, GroupCallActivity.this.muteLabel[i16].getMeasuredHeight() + dp7);
                    GroupCallActivity.this.muteLabel[i16].animate().scaleX(0.687f).scaleY(0.687f).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                }
            }
            if (GroupCallActivity.this.animateButtonsOnNextLayout) {
                AnimatorSet animatorSet = new AnimatorSet();
                int i17 = 0;
                boolean z3 = false;
                while (i17 < getChildCount()) {
                    View childAt = getChildAt(i17);
                    Float f = (Float) GroupCallActivity.this.buttonsAnimationParamsX.get(childAt);
                    Float f2 = (Float) GroupCallActivity.this.buttonsAnimationParamsY.get(childAt);
                    if (f == null || f2 == null) {
                        i5 = 1;
                    } else {
                        Property property = FrameLayout.TRANSLATION_X;
                        float floatValue = f.floatValue() - childAt.getLeft();
                        i5 = 1;
                        animatorSet.playTogether(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) property, floatValue, 0.0f));
                        animatorSet.playTogether(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) FrameLayout.TRANSLATION_Y, f2.floatValue() - childAt.getTop(), 0.0f));
                        z3 = true;
                    }
                    i17 += i5;
                }
                if (z3) {
                    AnimatorSet animatorSet2 = this.currentButtonsAnimation;
                    if (animatorSet2 != null) {
                        animatorSet2.removeAllListeners();
                        this.currentButtonsAnimation.cancel();
                    }
                    this.currentButtonsAnimation = animatorSet;
                    animatorSet.setDuration(350L);
                    animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.18.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            18.this.currentButtonsAnimation = null;
                            for (int i18 = 0; i18 < 18.this.getChildCount(); i18++) {
                                View childAt2 = 18.this.getChildAt(i18);
                                childAt2.setTranslationX(0.0f);
                                childAt2.setTranslationY(0.0f);
                            }
                        }
                    });
                    animatorSet.start();
                }
                GroupCallActivity.this.buttonsAnimationParamsX.clear();
                GroupCallActivity.this.buttonsAnimationParamsY.clear();
            }
            GroupCallActivity.this.animateButtonsOnNextLayout = false;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ViewGroup.LayoutParams layoutParams;
            int i3;
            if (!GroupCallActivity.isLandscapeMode) {
                i = View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(460.0f), View.MeasureSpec.getSize(i)), 1073741824);
            }
            for (int i4 = 0; i4 < 2; i4++) {
                if (!GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) {
                    layoutParams = GroupCallActivity.this.muteLabel[i4].getLayoutParams();
                    i3 = -2;
                } else {
                    layoutParams = GroupCallActivity.this.muteLabel[i4].getLayoutParams();
                    i3 = (int) (View.MeasureSpec.getSize(i) / 0.68f);
                }
                layoutParams.width = i3;
            }
            super.onMeasure(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 implements Runnable {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int[] iArr) {
            GroupCallActivity.this.textureLightningView.setNewColors(iArr);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(VoIPTextureView voIPTextureView) {
            try {
                Bitmap bitmap = voIPTextureView.blurRenderer.getBitmap(100, 100);
                if (bitmap == null) {
                    return;
                }
                final int[] colorsSync = DominantColors.getColorsSync(false, bitmap, true);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.2.this.lambda$run$0(colorsSync);
                    }
                });
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            GroupCallGridCell findGroupCallGridCell;
            GroupCallMiniTextureView renderer;
            final VoIPTextureView voIPTextureView;
            try {
                if (GroupCallActivity.this.renderersContainer == null || GroupCallActivity.this.renderersContainer.inFullscreenMode || (findGroupCallGridCell = GroupCallActivity.this.findGroupCallGridCell()) == null || (renderer = findGroupCallGridCell.getRenderer()) == null || (voIPTextureView = renderer.textureView) == null) {
                    return;
                }
                GroupCallActivity.updateTextureLightningQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.2.this.lambda$run$1(voIPTextureView);
                    }
                });
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 20 implements View.OnClickListener {
        Runnable finishRunnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity.20.1
            @Override // java.lang.Runnable
            public void run() {
                GroupCallActivity.this.muteButton.setAnimation(GroupCallActivity.this.bigMicDrawable);
                GroupCallActivity.this.playingHandAnimation = false;
            }
        };

        20() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$0() {
            GroupCallActivity.this.wasNotInLayoutFullscreen = null;
            GroupCallActivity.this.wasExpandBigSize = null;
            GroupCallActivity groupCallActivity = GroupCallActivity.this;
            groupCallActivity.updateMuteButton(groupCallActivity.muteButtonState, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$1(TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject != null) {
                GroupCallActivity.this.accountInstance.getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$2(TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject != null) {
                GroupCallActivity.this.accountInstance.getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GroupCallActivity groupCallActivity;
            int checkSelfPermission;
            GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
            if (groupCallActivity2.call == null || groupCallActivity2.muteButtonState == 3) {
                return;
            }
            int i = 6;
            int i2 = 0;
            if (GroupCallActivity.this.isRtmpStream() && !GroupCallActivity.this.call.isScheduled()) {
                if (GroupCallActivity.this.renderersContainer != null && GroupCallActivity.this.renderersContainer.inFullscreenMode && (AndroidUtilities.isTablet() || GroupCallActivity.isLandscapeMode == GroupCallActivity.this.isRtmpLandscapeMode())) {
                    GroupCallActivity.this.fullscreenFor(null);
                    if (GroupCallActivity.isLandscapeMode) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$20$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                GroupCallActivity.20.this.lambda$onClick$0();
                            }
                        }, 200L);
                    }
                    GroupCallActivity.this.parentActivity.setRequestedOrientation(-1);
                    return;
                }
                if (GroupCallActivity.this.visibleVideoParticipants.isEmpty()) {
                    return;
                }
                ChatObject.VideoParticipant videoParticipant = (ChatObject.VideoParticipant) GroupCallActivity.this.visibleVideoParticipants.get(0);
                if (AndroidUtilities.isTablet()) {
                    GroupCallActivity.this.fullscreenFor(videoParticipant);
                    return;
                }
                if (GroupCallActivity.isLandscapeMode == GroupCallActivity.this.isRtmpLandscapeMode()) {
                    GroupCallActivity.this.fullscreenFor(videoParticipant);
                }
                if (GroupCallActivity.this.isRtmpLandscapeMode()) {
                    GroupCallActivity.this.parentActivity.setRequestedOrientation(6);
                    return;
                } else {
                    GroupCallActivity.this.parentActivity.setRequestedOrientation(1);
                    return;
                }
            }
            if (GroupCallActivity.this.muteButtonState == 5) {
                if (GroupCallActivity.this.startingGroupCall) {
                    return;
                }
                view.performHapticFeedback(3, 2);
                GroupCallActivity.this.startingGroupCall = true;
                TLRPC.TL_phone_startScheduledGroupCall tL_phone_startScheduledGroupCall = new TLRPC.TL_phone_startScheduledGroupCall();
                tL_phone_startScheduledGroupCall.call = GroupCallActivity.this.call.getInputGroupCall();
                GroupCallActivity.this.accountInstance.getConnectionsManager().sendRequest(tL_phone_startScheduledGroupCall, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$20$$ExternalSyntheticLambda1
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        GroupCallActivity.20.this.lambda$onClick$1(tLObject, tL_error);
                    }
                });
                return;
            }
            if (GroupCallActivity.this.muteButtonState == 7 || GroupCallActivity.this.muteButtonState == 6) {
                if (GroupCallActivity.this.muteButtonState == 6 && GroupCallActivity.this.reminderHintView != null) {
                    GroupCallActivity.this.reminderHintView.hide();
                }
                TLRPC.TL_phone_toggleGroupCallStartSubscription tL_phone_toggleGroupCallStartSubscription = new TLRPC.TL_phone_toggleGroupCallStartSubscription();
                tL_phone_toggleGroupCallStartSubscription.call = GroupCallActivity.this.call.getInputGroupCall();
                GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                TLRPC.GroupCall groupCall = groupCallActivity3.call.call;
                boolean z = !groupCall.schedule_start_subscribed;
                groupCall.schedule_start_subscribed = z;
                tL_phone_toggleGroupCallStartSubscription.subscribed = z;
                groupCallActivity3.accountInstance.getConnectionsManager().sendRequest(tL_phone_toggleGroupCallStartSubscription, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$20$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        GroupCallActivity.20.this.lambda$onClick$2(tLObject, tL_error);
                    }
                });
                groupCallActivity = GroupCallActivity.this;
                if (groupCallActivity.call.call.schedule_start_subscribed) {
                    i = 7;
                }
            } else {
                if (VoIPService.getSharedInstance() == null || GroupCallActivity.this.isStillConnecting()) {
                    return;
                }
                i = 4;
                if (GroupCallActivity.this.muteButtonState != 2 && GroupCallActivity.this.muteButtonState != 4) {
                    if (GroupCallActivity.this.muteButtonState == 0) {
                        if (Build.VERSION.SDK_INT >= 23 && GroupCallActivity.this.getParentActivity() != null) {
                            checkSelfPermission = GroupCallActivity.this.getParentActivity().checkSelfPermission("android.permission.RECORD_AUDIO");
                            if (checkSelfPermission != 0) {
                                PermissionRequest.ensurePermission(R.raw.permission_request_microphone, R.string.VoipNeedMicPermissionWithHint, "android.permission.RECORD_AUDIO", new Utilities.Callback() { // from class: org.telegram.ui.GroupCallActivity$20$$ExternalSyntheticLambda2
                                    @Override // org.telegram.messenger.Utilities.Callback
                                    public final void run(Object obj) {
                                        ((Boolean) obj).booleanValue();
                                    }
                                });
                                return;
                            }
                        }
                        GroupCallActivity.this.updateMuteButton(1, true);
                        VoIPService.getSharedInstance().setMicMute(false, false, true);
                    } else {
                        GroupCallActivity.this.updateMuteButton(0, true);
                        VoIPService.getSharedInstance().setMicMute(true, false, true);
                    }
                    GroupCallActivity.this.muteButton.performHapticFeedback(3, 2);
                    return;
                }
                if (GroupCallActivity.this.playingHandAnimation) {
                    return;
                }
                GroupCallActivity.this.playingHandAnimation = true;
                AndroidUtilities.shakeView(GroupCallActivity.this.muteLabel[0]);
                view.performHapticFeedback(3, 2);
                int nextInt = Utilities.random.nextInt(100);
                int i3 = 120;
                if (nextInt >= 32) {
                    i2 = NotificationCenter.needSetDayNightTheme;
                    if (nextInt < 64) {
                        i3 = NotificationCenter.needSetDayNightTheme;
                        i2 = 120;
                    } else {
                        i3 = 420;
                        if (nextInt >= 97) {
                            i2 = 540;
                            if (nextInt == 98) {
                                i3 = 540;
                                i2 = 420;
                            } else {
                                i3 = 720;
                            }
                        }
                    }
                }
                GroupCallActivity.this.handDrawables.setCustomEndFrame(i3);
                GroupCallActivity.this.handDrawables.setOnFinishCallback(this.finishRunnable, i3 - 1);
                GroupCallActivity.this.muteButton.setAnimation(GroupCallActivity.this.handDrawables);
                GroupCallActivity.this.handDrawables.setCurrentFrame(i2);
                GroupCallActivity.this.muteButton.playAnimation();
                if (GroupCallActivity.this.muteButtonState != 2) {
                    return;
                }
                GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                long peerId = MessageObject.getPeerId(((TLRPC.TL_groupCallParticipant) groupCallActivity4.call.participants.get(MessageObject.getPeerId(groupCallActivity4.selfPeer))).peer);
                VoIPService.getSharedInstance().editCallMember(DialogObject.isUserDialog(peerId) ? GroupCallActivity.this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId)) : GroupCallActivity.this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId)), null, null, null, Boolean.TRUE, null);
                groupCallActivity = GroupCallActivity.this;
            }
            groupCallActivity.updateMuteButton(i, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 21 extends AudioPlayerAlert.ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        21(Context context, Context context2) {
            super(context);
            this.val$context = context2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createTextView$0(TextView textView, View view) {
            GroupCallActivity groupCallActivity = GroupCallActivity.this;
            ChatObject.Call call = groupCallActivity.call;
            if (call == null || !call.recording) {
                return;
            }
            groupCallActivity.showRecordHint(textView);
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            final TextView textView = new TextView(this.val$context);
            textView.setTextColor(Theme.getColor(Theme.key_voipgroup_actionBarItems));
            textView.setTextSize(1, 20.0f);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setGravity(51);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$21$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.21.this.lambda$createTextView$0(textView, view);
                }
            });
            return textView;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 29 extends GroupCallRenderersContainer {
        ValueAnimator uiVisibilityAnimator;

        29(Context context, RecyclerView recyclerView, RecyclerView recyclerView2, ArrayList arrayList, ChatObject.Call call, GroupCallActivity groupCallActivity) {
            super(context, recyclerView, recyclerView2, arrayList, call, groupCallActivity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUiVisibilityChanged$0(ValueAnimator valueAnimator) {
            GroupCallActivity.this.progressToHideUi = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            GroupCallActivity.this.renderersContainer.setProgressToHideUi(GroupCallActivity.this.progressToHideUi);
            GroupCallActivity.this.fullscreenUsersListView.invalidate();
            ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            GroupCallActivity.this.buttonsContainer.invalidate();
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer
        protected boolean canHideUI() {
            return super.canHideUI() && GroupCallActivity.this.previewDialog == null;
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer, android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == GroupCallActivity.this.scrimRenderer) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer
        protected void onBackPressed() {
            GroupCallActivity.this.onBackPressed();
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer
        protected void onFullScreenModeChanged(boolean z) {
            GroupCallActivity.this.delayedGroupCallUpdated = z;
            if (GroupCallActivity.isTabletMode) {
                if (z || !GroupCallActivity.this.renderersContainer.inFullscreenMode) {
                    return;
                }
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                groupCallActivity.tabletGridAdapter.setVisibility(groupCallActivity.tabletVideoGridView, false, true);
                return;
            }
            GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
            if (z) {
                groupCallActivity2.undoView[0].hide(false, 1);
                GroupCallActivity.this.renderersContainer.undoView[0].hide(false, 2);
                if (!GroupCallActivity.this.renderersContainer.inFullscreenMode) {
                    GroupCallActivity.this.listView.setVisibility(0);
                    GroupCallActivity.this.actionBar.setVisibility(0);
                    if (GroupCallActivity.this.watchersView != null) {
                        GroupCallActivity.this.watchersView.setVisibility(0);
                    }
                }
                GroupCallActivity.this.updateState(true, false);
                GroupCallActivity.this.buttonsContainer.requestLayout();
                if (GroupCallActivity.this.fullscreenUsersListView.getVisibility() != 0) {
                    GroupCallActivity.this.fullscreenUsersListView.setVisibility(0);
                    GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                    groupCallActivity3.fullscreenAdapter.setVisibility(groupCallActivity3.fullscreenUsersListView, true);
                    GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                    groupCallActivity4.fullscreenAdapter.update(false, groupCallActivity4.fullscreenUsersListView);
                } else {
                    GroupCallActivity groupCallActivity5 = GroupCallActivity.this;
                    groupCallActivity5.fullscreenAdapter.setVisibility(groupCallActivity5.fullscreenUsersListView, true);
                    GroupCallActivity.this.applyCallParticipantUpdates(true);
                }
            } else {
                if (groupCallActivity2.renderersContainer.inFullscreenMode) {
                    GroupCallActivity.this.actionBar.setVisibility(8);
                    GroupCallActivity.this.listView.setVisibility(8);
                    if (GroupCallActivity.this.watchersView != null) {
                        GroupCallActivity.this.watchersView.setVisibility(8);
                    }
                } else {
                    GroupCallActivity.this.fullscreenUsersListView.setVisibility(8);
                    GroupCallActivity groupCallActivity6 = GroupCallActivity.this;
                    groupCallActivity6.fullscreenAdapter.setVisibility(groupCallActivity6.fullscreenUsersListView, false);
                }
                if (GroupCallActivity.this.fullscreenUsersListView.getVisibility() == 0) {
                    for (int i = 0; i < GroupCallActivity.this.fullscreenUsersListView.getChildCount(); i++) {
                        View childAt = GroupCallActivity.this.fullscreenUsersListView.getChildAt(i);
                        childAt.setAlpha(1.0f);
                        childAt.setScaleX(1.0f);
                        childAt.setScaleY(1.0f);
                        childAt.setTranslationX(0.0f);
                        childAt.setTranslationY(0.0f);
                        ((GroupCallFullscreenAdapter.GroupCallUserCell) childAt).setProgressToFullscreen(GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                    }
                }
            }
            GroupCallActivity.this.buttonsBackgroundGradientView2.setVisibility(z ? 0 : 8);
            if (GroupCallActivity.this.delayedGroupCallUpdated) {
                return;
            }
            GroupCallActivity.this.applyCallParticipantUpdates(true);
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer
        public void onUiVisibilityChanged() {
            if (GroupCallActivity.this.renderersContainer == null) {
                return;
            }
            final boolean isUiVisible = GroupCallActivity.this.renderersContainer.isUiVisible();
            ValueAnimator valueAnimator = this.uiVisibilityAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.uiVisibilityAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(GroupCallActivity.this.progressToHideUi, isUiVisible ? 0.0f : 1.0f);
            this.uiVisibilityAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$29$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    GroupCallActivity.29.this.lambda$onUiVisibilityChanged$0(valueAnimator2);
                }
            });
            this.uiVisibilityAnimator.setDuration(350L);
            this.uiVisibilityAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.uiVisibilityAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.29.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    29 r2 = 29.this;
                    r2.uiVisibilityAnimator = null;
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    groupCallActivity.progressToHideUi = isUiVisible ? 0.0f : 1.0f;
                    groupCallActivity.renderersContainer.setProgressToHideUi(GroupCallActivity.this.progressToHideUi);
                    GroupCallActivity.this.fullscreenUsersListView.invalidate();
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                    GroupCallActivity.this.buttonsContainer.invalidate();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    GroupCallActivity.this.invalidateLayoutFullscreen();
                }
            });
            this.uiVisibilityAnimator.start();
        }

        @Override // org.telegram.ui.Components.voip.GroupCallRenderersContainer
        protected void update() {
            super.update();
            ((BottomSheet) GroupCallActivity.this).navBarColor = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_actionBarUnscrolled), Theme.getColor(Theme.key_voipgroup_actionBar), Math.max(GroupCallActivity.this.colorProgress, GroupCallActivity.this.renderersContainer == null ? 0.0f : GroupCallActivity.this.renderersContainer.progressToFullscreenMode), 1.0f);
            ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            GroupCallActivity groupCallActivity = GroupCallActivity.this;
            groupCallActivity.setColorProgress(groupCallActivity.colorProgress);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 7 extends ActionBar.ActionBarMenuOnItemClick {
        final /* synthetic */ Context val$context;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 1 extends GroupCallRecordAlert {
            1(Context context, TLRPC.Chat chat, boolean z) {
                super(context, chat, z);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ boolean lambda$onStartRecord$0(AlertDialog.Builder builder, TextView textView, int i, KeyEvent keyEvent) {
                AndroidUtilities.hideKeyboard(textView);
                builder.create().getButton(-1).callOnClick();
                return false;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onStartRecord$1(AlertDialog alertDialog, EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
                GroupCallActivity.this.makeFocusable(null, alertDialog, editTextBoldCursor, true);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onStartRecord$3(EditTextBoldCursor editTextBoldCursor, int i, DialogInterface dialogInterface, int i2) {
                GroupCallActivity.this.call.toggleRecord(editTextBoldCursor.getText().toString(), i);
                AndroidUtilities.hideKeyboard(editTextBoldCursor);
                GroupCallActivity.this.getUndoView().showWithAction(0L, i == 0 ? 39 : 100, (Runnable) null);
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().playStartRecordSound();
                }
            }

            @Override // org.telegram.ui.Components.GroupCallRecordAlert
            public void onStartRecord(final int i) {
                int i2;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
                GroupCallActivity.this.enterEventSent = false;
                builder.setTitle(LocaleController.getString(R.string.VoipGroupStartRecordingTitle));
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                if (i == 0) {
                    i2 = groupCallActivity.call.call.rtmp_stream ? R.string.VoipGroupStartRecordingRtmpText : R.string.VoipGroupStartRecordingText;
                } else if (ChatObject.isChannelOrGiga(groupCallActivity.currentChat)) {
                    if (!GroupCallActivity.this.call.call.rtmp_stream) {
                        i2 = R.string.VoipChannelStartRecordingVideoText;
                    }
                    i2 = R.string.VoipGroupStartRecordingRtmpVideoText;
                } else {
                    if (!GroupCallActivity.this.call.call.rtmp_stream) {
                        i2 = R.string.VoipGroupStartRecordingVideoText;
                    }
                    i2 = R.string.VoipGroupStartRecordingRtmpVideoText;
                }
                builder.setMessage(LocaleController.getString(i2));
                builder.setCheckFocusable(false);
                final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(getContext());
                editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable(getContext(), Theme.getColor(Theme.key_voipgroup_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_voipgroup_windowBackgroundWhiteInputFieldActivated)));
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(1);
                builder.setView(linearLayout);
                editTextBoldCursor.setTextSize(1, 16.0f);
                int i3 = Theme.key_voipgroup_nameText;
                editTextBoldCursor.setTextColor(Theme.getColor(i3));
                editTextBoldCursor.setMaxLines(1);
                editTextBoldCursor.setLines(1);
                editTextBoldCursor.setInputType(16385);
                editTextBoldCursor.setGravity(51);
                editTextBoldCursor.setSingleLine(true);
                editTextBoldCursor.setHint(LocaleController.getString(R.string.VoipGroupSaveFileHint));
                editTextBoldCursor.setImeOptions(6);
                editTextBoldCursor.setHintTextColor(Theme.getColor(Theme.key_voipgroup_lastSeenText));
                editTextBoldCursor.setCursorColor(Theme.getColor(i3));
                editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
                editTextBoldCursor.setCursorWidth(1.5f);
                editTextBoldCursor.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, 36, 51, 24, 0, 24, 12));
                editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.GroupCallActivity$7$1$$ExternalSyntheticLambda0
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView, int i4, KeyEvent keyEvent) {
                        boolean lambda$onStartRecord$0;
                        lambda$onStartRecord$0 = GroupCallActivity.7.1.lambda$onStartRecord$0(AlertDialog.Builder.this, textView, i4, keyEvent);
                        return lambda$onStartRecord$0;
                    }
                });
                final AlertDialog create = builder.create();
                create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_inviteMembersBackground));
                create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.ui.GroupCallActivity$7$1$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnShowListener
                    public final void onShow(DialogInterface dialogInterface) {
                        GroupCallActivity.7.1.this.lambda$onStartRecord$1(create, editTextBoldCursor, dialogInterface);
                    }
                });
                create.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$7$1$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
                    }
                });
                builder.setPositiveButton(LocaleController.getString(R.string.Start), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$1$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        GroupCallActivity.7.1.this.lambda$onStartRecord$3(editTextBoldCursor, i, dialogInterface, i4);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$1$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
                    }
                });
                AlertDialog create2 = builder.create();
                create2.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
                create2.show();
                create2.setTextColor(Theme.getColor(i3));
                editTextBoldCursor.requestFocus();
            }
        }

        7(Context context) {
            this.val$context = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.TL_updates) {
                GroupCallActivity.this.accountInstance.getMessagesController().processUpdates((TLRPC.TL_updates) tLObject, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1(DialogInterface dialogInterface, int i) {
            if (GroupCallActivity.this.call.isScheduled()) {
                TLRPC.ChatFull chatFull = GroupCallActivity.this.accountInstance.getMessagesController().getChatFull(GroupCallActivity.this.currentChat.id);
                if (chatFull != null) {
                    chatFull.flags &= -2097153;
                    chatFull.call = null;
                    GroupCallActivity.this.accountInstance.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallUpdated, Long.valueOf(GroupCallActivity.this.currentChat.id), Long.valueOf(GroupCallActivity.this.call.call.id), Boolean.FALSE);
                }
                TLRPC.TL_phone_discardGroupCall tL_phone_discardGroupCall = new TLRPC.TL_phone_discardGroupCall();
                tL_phone_discardGroupCall.call = GroupCallActivity.this.call.getInputGroupCall();
                GroupCallActivity.this.accountInstance.getConnectionsManager().sendRequest(tL_phone_discardGroupCall, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda10
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        GroupCallActivity.7.this.lambda$onItemClick$0(tLObject, tL_error);
                    }
                });
            } else if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().hangUp(1);
            }
            GroupCallActivity.this.dismiss();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didStartedCall, new Object[0]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onItemClick$10(VoIPService voIPService, ArrayList arrayList, DialogInterface dialogInterface, int i) {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            voIPService.setAudioOutput(((Integer) arrayList.get(i)).intValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(boolean z, DialogInterface dialogInterface, int i) {
            GroupCallActivity.this.call.toggleRecord(null, 0);
            GroupCallActivity.this.getUndoView().showWithAction(0L, z ? 101 : 40, (Runnable) null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onItemClick$4(AlertDialog.Builder builder, TextView textView, int i, KeyEvent keyEvent) {
            AndroidUtilities.hideKeyboard(textView);
            builder.create().getButton(-1).callOnClick();
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$5(EditTextBoldCursor editTextBoldCursor, AlertDialog.Builder builder, DialogInterface dialogInterface, int i) {
            AndroidUtilities.hideKeyboard(editTextBoldCursor);
            GroupCallActivity.this.call.setTitle(editTextBoldCursor.getText().toString());
            builder.getDismissRunnable().run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$6(AlertDialog alertDialog, EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
            GroupCallActivity.this.makeFocusable(null, alertDialog, editTextBoldCursor, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onItemClick$8(TLObject tLObject, TLRPC.TL_error tL_error) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$9(TLRPC.InputPeer inputPeer, boolean z, boolean z2, boolean z3) {
            TLObject chat;
            int i;
            int i2;
            GroupCallActivity groupCallActivity = GroupCallActivity.this;
            if (groupCallActivity.call == null) {
                return;
            }
            boolean z4 = inputPeer instanceof TLRPC.TL_inputPeerUser;
            if (z4) {
                chat = groupCallActivity.accountInstance.getMessagesController().getUser(Long.valueOf(inputPeer.user_id));
            } else {
                chat = groupCallActivity.accountInstance.getMessagesController().getChat(Long.valueOf(inputPeer instanceof TLRPC.TL_inputPeerChat ? inputPeer.chat_id : inputPeer.channel_id));
            }
            TLObject tLObject = chat;
            if (!GroupCallActivity.this.call.isScheduled()) {
                if (VoIPService.getSharedInstance() == null || !z) {
                    return;
                }
                GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                VoIPService.getSharedInstance().setGroupCallPeer(inputPeer);
                GroupCallActivity.this.userSwitchObject = tLObject;
                return;
            }
            GroupCallActivity.this.getUndoView().showWithAction(0L, 37, tLObject, GroupCallActivity.this.currentChat, (Runnable) null, (Runnable) null);
            if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
                GroupCallActivity.this.selfPeer = new TLRPC.TL_peerChannel();
                GroupCallActivity.this.selfPeer.channel_id = inputPeer.channel_id;
            } else if (z4) {
                GroupCallActivity.this.selfPeer = new TLRPC.TL_peerUser();
                GroupCallActivity.this.selfPeer.user_id = inputPeer.user_id;
            } else if (inputPeer instanceof TLRPC.TL_inputPeerChat) {
                GroupCallActivity.this.selfPeer = new TLRPC.TL_peerChat();
                GroupCallActivity.this.selfPeer.chat_id = inputPeer.chat_id;
            }
            GroupCallActivity.this.schedulePeer = inputPeer;
            TLRPC.ChatFull chatFull = GroupCallActivity.this.accountInstance.getMessagesController().getChatFull(GroupCallActivity.this.currentChat.id);
            if (chatFull != null) {
                chatFull.groupcall_default_join_as = GroupCallActivity.this.selfPeer;
                if (chatFull instanceof TLRPC.TL_chatFull) {
                    i = chatFull.flags;
                    i2 = 32768;
                } else {
                    i = chatFull.flags;
                    i2 = ConnectionsManager.FileTypeFile;
                }
                chatFull.flags = i | i2;
            }
            TLRPC.TL_phone_saveDefaultGroupCallJoinAs tL_phone_saveDefaultGroupCallJoinAs = new TLRPC.TL_phone_saveDefaultGroupCallJoinAs();
            tL_phone_saveDefaultGroupCallJoinAs.peer = MessagesController.getInputPeer(GroupCallActivity.this.currentChat);
            tL_phone_saveDefaultGroupCallJoinAs.join_as = inputPeer;
            GroupCallActivity.this.accountInstance.getConnectionsManager().sendRequest(tL_phone_saveDefaultGroupCallJoinAs, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    GroupCallActivity.7.lambda$onItemClick$8(tLObject2, tL_error);
                }
            });
            GroupCallActivity.this.updateItems();
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            final VoIPService sharedInstance;
            int color;
            AlertDialog create;
            int i2;
            int i3;
            GroupCallActivity groupCallActivity;
            if (i == -1) {
                GroupCallActivity.this.onBackPressed();
                return;
            }
            if (i == 1) {
                groupCallActivity = GroupCallActivity.this;
                groupCallActivity.call.call.join_muted = false;
            } else {
                if (i != 2) {
                    if (i == 3) {
                        GroupCallActivity.this.getLink(false);
                        return;
                    }
                    if (i == 4) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupCallActivity.this.getContext());
                        if (ChatObject.isChannelOrGiga(GroupCallActivity.this.currentChat)) {
                            builder.setTitle(LocaleController.getString(R.string.VoipChannelEndAlertTitle));
                            i3 = R.string.VoipChannelEndAlertText;
                        } else {
                            builder.setTitle(LocaleController.getString(R.string.VoipGroupEndAlertTitle));
                            i3 = R.string.VoipGroupEndAlertText;
                        }
                        builder.setMessage(LocaleController.getString(i3));
                        builder.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
                        builder.setPositiveButton(LocaleController.getString(R.string.VoipGroupEnd), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i4) {
                                GroupCallActivity.7.this.lambda$onItemClick$1(dialogInterface, i4);
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                        create = builder.create();
                        create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
                        create.show();
                        TextView textView = (TextView) create.getButton(-1);
                        if (textView != null) {
                            textView.setTextColor(Theme.getColor(Theme.key_voipgroup_leaveCallMenu));
                        }
                        i2 = Theme.key_voipgroup_actionBarItems;
                    } else {
                        if (i == 9) {
                            GroupCallActivity.this.screenShareItem.callOnClick();
                            return;
                        }
                        if (i != 5) {
                            if (i == 7) {
                                GroupCallActivity.this.changingPermissions = true;
                                GroupCallActivity.this.everyoneItem.setVisibility(0);
                                GroupCallActivity.this.adminItem.setVisibility(0);
                                GroupCallActivity.this.accountGap.setVisibility(8);
                                GroupCallActivity.this.inviteItem.setVisibility(8);
                                GroupCallActivity.this.leaveItem.setVisibility(8);
                                GroupCallActivity.this.permissionItem.setVisibility(8);
                                GroupCallActivity.this.editTitleItem.setVisibility(8);
                                GroupCallActivity.this.recordItem.setVisibility(8);
                                GroupCallActivity.this.screenItem.setVisibility(8);
                                GroupCallActivity.this.accountSelectCell.setVisibility(8);
                                GroupCallActivity.this.soundItem.setVisibility(8);
                                GroupCallActivity.this.noiseItem.setVisibility(8);
                                GroupCallActivity.this.otherItem.forceUpdatePopupPosition();
                                return;
                            }
                            if (i == 6) {
                                GroupCallActivity.this.enterEventSent = false;
                                final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(GroupCallActivity.this.getContext());
                                editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable(GroupCallActivity.this.getContext(), true));
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(GroupCallActivity.this.getContext());
                                builder2.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
                                builder2.setTitle(LocaleController.getString(ChatObject.isChannelOrGiga(GroupCallActivity.this.currentChat) ? R.string.VoipChannelTitle : R.string.VoipGroupTitle));
                                builder2.setCheckFocusable(false);
                                builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda2
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i4) {
                                        AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
                                    }
                                });
                                LinearLayout linearLayout = new LinearLayout(GroupCallActivity.this.getContext());
                                linearLayout.setOrientation(1);
                                builder2.setView(linearLayout);
                                editTextBoldCursor.setTextSize(1, 16.0f);
                                int i4 = Theme.key_voipgroup_nameText;
                                editTextBoldCursor.setTextColor(Theme.getColor(i4));
                                editTextBoldCursor.setMaxLines(1);
                                editTextBoldCursor.setLines(1);
                                editTextBoldCursor.setInputType(16385);
                                editTextBoldCursor.setGravity(51);
                                editTextBoldCursor.setSingleLine(true);
                                editTextBoldCursor.setImeOptions(6);
                                editTextBoldCursor.setHint(GroupCallActivity.this.currentChat.title);
                                editTextBoldCursor.setHintTextColor(Theme.getColor(Theme.key_voipgroup_lastSeenText));
                                editTextBoldCursor.setCursorColor(Theme.getColor(i4));
                                editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
                                editTextBoldCursor.setCursorWidth(1.5f);
                                editTextBoldCursor.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                                linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
                                editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda3
                                    @Override // android.widget.TextView.OnEditorActionListener
                                    public final boolean onEditorAction(TextView textView2, int i5, KeyEvent keyEvent) {
                                        boolean lambda$onItemClick$4;
                                        lambda$onItemClick$4 = GroupCallActivity.7.lambda$onItemClick$4(AlertDialog.Builder.this, textView2, i5, keyEvent);
                                        return lambda$onItemClick$4;
                                    }
                                });
                                editTextBoldCursor.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.GroupCallActivity.7.2
                                    boolean ignoreTextChange;

                                    @Override // android.text.TextWatcher
                                    public void afterTextChanged(Editable editable) {
                                        if (!this.ignoreTextChange && editable.length() > 40) {
                                            this.ignoreTextChange = true;
                                            editable.delete(40, editable.length());
                                            AndroidUtilities.shakeView(editTextBoldCursor);
                                            editTextBoldCursor.performHapticFeedback(3, 2);
                                            this.ignoreTextChange = false;
                                        }
                                    }

                                    @Override // android.text.TextWatcher
                                    public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                                    }

                                    @Override // android.text.TextWatcher
                                    public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                                    }
                                });
                                if (!TextUtils.isEmpty(GroupCallActivity.this.call.call.title)) {
                                    editTextBoldCursor.setText(GroupCallActivity.this.call.call.title);
                                    editTextBoldCursor.setSelection(editTextBoldCursor.length());
                                }
                                builder2.setPositiveButton(LocaleController.getString(R.string.Save), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda4
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i5) {
                                        GroupCallActivity.7.this.lambda$onItemClick$5(editTextBoldCursor, builder2, dialogInterface, i5);
                                    }
                                });
                                final AlertDialog create2 = builder2.create();
                                create2.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_inviteMembersBackground));
                                create2.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda5
                                    @Override // android.content.DialogInterface.OnShowListener
                                    public final void onShow(DialogInterface dialogInterface) {
                                        GroupCallActivity.7.this.lambda$onItemClick$6(create2, editTextBoldCursor, dialogInterface);
                                    }
                                });
                                create2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda6
                                    @Override // android.content.DialogInterface.OnDismissListener
                                    public final void onDismiss(DialogInterface dialogInterface) {
                                        AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
                                    }
                                });
                                create2.show();
                                create2.setTextColor(Theme.getColor(i4));
                                editTextBoldCursor.requestFocus();
                                return;
                            }
                            if (i == 8) {
                                Context context = GroupCallActivity.this.getContext();
                                GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                                JoinCallAlert.open(context, -groupCallActivity2.currentChat.id, groupCallActivity2.accountInstance, null, 2, GroupCallActivity.this.selfPeer, new JoinCallAlert.JoinCallAlertDelegate() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda7
                                    @Override // org.telegram.ui.Components.JoinCallAlert.JoinCallAlertDelegate
                                    public final void didSelectChat(TLRPC.InputPeer inputPeer, boolean z, boolean z2, boolean z3) {
                                        GroupCallActivity.7.this.lambda$onItemClick$9(inputPeer, z, z2, z3);
                                    }
                                });
                                return;
                            }
                            if (i == 11) {
                                SharedConfig.toggleNoiseSupression();
                                VoIPService sharedInstance2 = VoIPService.getSharedInstance();
                                if (sharedInstance2 == null) {
                                    return;
                                }
                                sharedInstance2.setNoiseSupressionEnabled(SharedConfig.noiseSupression);
                                return;
                            }
                            if (i != 10 || (sharedInstance = VoIPService.getSharedInstance()) == null) {
                                return;
                            }
                            ArrayList arrayList = new ArrayList();
                            ArrayList arrayList2 = new ArrayList();
                            final ArrayList arrayList3 = new ArrayList();
                            arrayList.add(LocaleController.getString(R.string.VoipAudioRoutingSpeaker));
                            arrayList2.add(Integer.valueOf(R.drawable.msg_voice_speaker));
                            arrayList3.add(0);
                            if (sharedInstance.hasEarpiece()) {
                                arrayList.add(LocaleController.getString(sharedInstance.isHeadsetPlugged() ? R.string.VoipAudioRoutingHeadset : R.string.VoipAudioRoutingPhone));
                                arrayList2.add(Integer.valueOf(sharedInstance.isHeadsetPlugged() ? R.drawable.msg_voice_headphones : R.drawable.msg_voice_phone));
                                arrayList3.add(1);
                            }
                            if (sharedInstance.isBluetoothHeadsetConnected()) {
                                String str = sharedInstance.currentBluetoothDeviceName;
                                if (str == null) {
                                    str = LocaleController.getString(R.string.VoipAudioRoutingBluetooth);
                                }
                                arrayList.add(str);
                                arrayList2.add(Integer.valueOf(R.drawable.msg_voice_bluetooth));
                                arrayList3.add(2);
                            }
                            int size = arrayList.size();
                            CharSequence[] charSequenceArr = new CharSequence[size];
                            int[] iArr = new int[size];
                            for (int i5 = 0; i5 < size; i5++) {
                                charSequenceArr[i5] = (CharSequence) arrayList.get(i5);
                                iArr[i5] = ((Integer) arrayList2.get(i5)).intValue();
                            }
                            BottomSheet.Builder items = new BottomSheet.Builder(this.val$context).setTitle(LocaleController.getString(R.string.VoipSelectAudioOutput), true).setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda8
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i6) {
                                    GroupCallActivity.7.lambda$onItemClick$10(VoIPService.this, arrayList3, dialogInterface, i6);
                                }
                            });
                            BottomSheet create3 = items.create();
                            int i6 = Theme.key_voipgroup_listViewBackgroundUnscrolled;
                            create3.setBackgroundColor(Theme.getColor(i6));
                            create3.fixNavigationBar(Theme.getColor(i6));
                            int i7 = sharedInstance.getCurrentAudioRoute() == 1 ? 0 : sharedInstance.getCurrentAudioRoute() == 0 ? 1 : 2;
                            items.show();
                            create3.setTitleColor(Theme.getColor(Theme.key_voipgroup_nameText));
                            for (int i8 = 0; i8 < create3.getItemViews().size(); i8++) {
                                BottomSheet.BottomSheetCell bottomSheetCell = create3.getItemViews().get(i8);
                                if (i8 == i7) {
                                    color = Theme.getColor(Theme.key_voipgroup_listeningText);
                                    bottomSheetCell.isSelected = true;
                                } else {
                                    color = Theme.getColor(Theme.key_voipgroup_nameText);
                                }
                                bottomSheetCell.setTextColor(color);
                                bottomSheetCell.setIconColor(color);
                                bottomSheetCell.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_actionBarItems), 12), 2));
                            }
                            return;
                        }
                        GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                        ChatObject.Call call = groupCallActivity3.call;
                        if (!call.recording) {
                            Context context2 = GroupCallActivity.this.getContext();
                            GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                            1 r14 = new 1(context2, groupCallActivity4.currentChat, groupCallActivity4.hasVideo);
                            if (GroupCallActivity.this.isRtmpStream()) {
                                r14.onStartRecord(2);
                                return;
                            } else {
                                r14.show();
                                return;
                            }
                        }
                        final boolean z = call.call.record_video_active;
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(groupCallActivity3.getContext());
                        builder3.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
                        builder3.setTitle(LocaleController.getString(R.string.VoipGroupStopRecordingTitle));
                        builder3.setMessage(LocaleController.getString(ChatObject.isChannelOrGiga(GroupCallActivity.this.currentChat) ? R.string.VoipChannelStopRecordingText : R.string.VoipGroupStopRecordingText));
                        builder3.setPositiveButton(LocaleController.getString(R.string.Stop), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$7$$ExternalSyntheticLambda1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i9) {
                                GroupCallActivity.7.this.lambda$onItemClick$2(z, dialogInterface, i9);
                            }
                        });
                        builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                        create = builder3.create();
                        create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
                        create.show();
                        i2 = Theme.key_voipgroup_nameText;
                    }
                    create.setTextColor(Theme.getColor(i2));
                    return;
                }
                groupCallActivity = GroupCallActivity.this;
                groupCallActivity.call.call.join_muted = true;
            }
            groupCallActivity.toggleAdminSpeak();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class AvatarUpdaterDelegate implements ImageUpdater.ImageUpdaterDelegate {
        private TLRPC.FileLocation avatar;
        private TLRPC.FileLocation avatarBig;
        private final long peerId;
        private ImageLocation uploadingImageLocation;
        public float uploadingProgress;

        private AvatarUpdaterDelegate(long j) {
            this.peerId = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didUploadPhoto$0(TLRPC.TL_error tL_error, TLObject tLObject, String str) {
            if (this.uploadingImageLocation != null) {
                GroupCallActivity.this.avatarsViewPager.removeUploadingImage(this.uploadingImageLocation);
                this.uploadingImageLocation = null;
            }
            if (tL_error == null) {
                TLRPC.User user = GroupCallActivity.this.accountInstance.getMessagesController().getUser(Long.valueOf(GroupCallActivity.this.accountInstance.getUserConfig().getClientUserId()));
                if (user == null) {
                    user = GroupCallActivity.this.accountInstance.getUserConfig().getCurrentUser();
                    if (user == null) {
                        return;
                    } else {
                        GroupCallActivity.this.accountInstance.getMessagesController().putUser(user, false);
                    }
                } else {
                    GroupCallActivity.this.accountInstance.getUserConfig().setCurrentUser(user);
                }
                TLRPC.TL_photos_photo tL_photos_photo = (TLRPC.TL_photos_photo) tLObject;
                ArrayList<TLRPC.PhotoSize> arrayList = tL_photos_photo.photo.sizes;
                TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 150);
                TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, 800);
                TLRPC.VideoSize videoSize = tL_photos_photo.photo.video_sizes.isEmpty() ? null : tL_photos_photo.photo.video_sizes.get(0);
                TLRPC.TL_userProfilePhoto tL_userProfilePhoto = new TLRPC.TL_userProfilePhoto();
                user.photo = tL_userProfilePhoto;
                tL_userProfilePhoto.photo_id = tL_photos_photo.photo.id;
                if (closestPhotoSizeWithSize != null) {
                    tL_userProfilePhoto.photo_small = closestPhotoSizeWithSize.location;
                }
                if (closestPhotoSizeWithSize2 != null) {
                    tL_userProfilePhoto.photo_big = closestPhotoSizeWithSize2.location;
                }
                if (closestPhotoSizeWithSize != null && this.avatar != null) {
                    FileLoader.getInstance(((BottomSheet) GroupCallActivity.this).currentAccount).getPathToAttach(this.avatar, true).renameTo(FileLoader.getInstance(((BottomSheet) GroupCallActivity.this).currentAccount).getPathToAttach(closestPhotoSizeWithSize, true));
                    ImageLoader.getInstance().replaceImageInCache(this.avatar.volume_id + "_" + this.avatar.local_id + "@50_50", closestPhotoSizeWithSize.location.volume_id + "_" + closestPhotoSizeWithSize.location.local_id + "@50_50", ImageLocation.getForUser(user, 1), false);
                }
                if (closestPhotoSizeWithSize2 != null && this.avatarBig != null) {
                    FileLoader.getInstance(((BottomSheet) GroupCallActivity.this).currentAccount).getPathToAttach(this.avatarBig, true).renameTo(FileLoader.getInstance(((BottomSheet) GroupCallActivity.this).currentAccount).getPathToAttach(closestPhotoSizeWithSize2, true));
                }
                if (videoSize != null && str != null) {
                    new File(str).renameTo(FileLoader.getInstance(((BottomSheet) GroupCallActivity.this).currentAccount).getPathToAttach(videoSize, "mp4", true));
                }
                GroupCallActivity.this.accountInstance.getMessagesController().getDialogPhotos(user.id).reset();
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(user);
                GroupCallActivity.this.accountInstance.getMessagesStorage().putUsersAndChats(arrayList2, null, false, true);
                TLRPC.User user2 = GroupCallActivity.this.accountInstance.getMessagesController().getUser(Long.valueOf(this.peerId));
                ImageLocation forUser = ImageLocation.getForUser(user2, 0);
                ImageLocation forUser2 = ImageLocation.getForUser(user2, 1);
                if (ImageLocation.getForLocal(this.avatarBig) == null) {
                    forUser2 = ImageLocation.getForLocal(this.avatar);
                }
                GroupCallActivity.this.avatarsViewPager.setCreateThumbFromParent(false);
                GroupCallActivity.this.avatarsViewPager.initIfEmpty(null, forUser, forUser2, true);
                this.avatar = null;
                this.avatarBig = null;
                AndroidUtilities.updateVisibleRows(GroupCallActivity.this.listView);
                updateAvatarUploadingProgress(1.0f);
            }
            GroupCallActivity.this.accountInstance.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_ALL));
            GroupCallActivity.this.accountInstance.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
            GroupCallActivity.this.accountInstance.getUserConfig().saveConfig(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didUploadPhoto$1(final String str, final TLObject tLObject, final TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.AvatarUpdaterDelegate.this.lambda$didUploadPhoto$0(tL_error, tLObject, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didUploadPhoto$2() {
            if (this.uploadingImageLocation != null) {
                GroupCallActivity.this.avatarsViewPager.removeUploadingImage(this.uploadingImageLocation);
                this.uploadingImageLocation = null;
            }
            TLRPC.Chat chat = GroupCallActivity.this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.peerId));
            ImageLocation forChat = ImageLocation.getForChat(chat, 0);
            ImageLocation forChat2 = ImageLocation.getForChat(chat, 1);
            if (ImageLocation.getForLocal(this.avatarBig) == null) {
                forChat2 = ImageLocation.getForLocal(this.avatar);
            }
            GroupCallActivity.this.avatarsViewPager.setCreateThumbFromParent(false);
            GroupCallActivity.this.avatarsViewPager.initIfEmpty(null, forChat, forChat2, true);
            this.avatar = null;
            this.avatarBig = null;
            AndroidUtilities.updateVisibleRows(GroupCallActivity.this.listView);
            updateAvatarUploadingProgress(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didUploadPhoto$3(TLRPC.InputFile inputFile, TLRPC.InputFile inputFile2, TLRPC.VideoSize videoSize, double d, final String str, TLRPC.PhotoSize photoSize, TLRPC.PhotoSize photoSize2) {
            if (inputFile == null && inputFile2 == null && videoSize == null) {
                this.avatar = photoSize.location;
                TLRPC.FileLocation fileLocation = photoSize2.location;
                this.avatarBig = fileLocation;
                this.uploadingImageLocation = ImageLocation.getForLocal(fileLocation);
                GroupCallActivity.this.avatarsViewPager.addUploadingImage(this.uploadingImageLocation, ImageLocation.getForLocal(this.avatar));
                AndroidUtilities.updateVisibleRows(GroupCallActivity.this.listView);
                return;
            }
            if (this.peerId <= 0) {
                GroupCallActivity.this.accountInstance.getMessagesController().changeChatAvatar(-this.peerId, null, inputFile, inputFile2, videoSize, d, str, photoSize.location, photoSize2.location, new Runnable() { // from class: org.telegram.ui.GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.AvatarUpdaterDelegate.this.lambda$didUploadPhoto$2();
                    }
                });
                return;
            }
            TLRPC.TL_photos_uploadProfilePhoto tL_photos_uploadProfilePhoto = new TLRPC.TL_photos_uploadProfilePhoto();
            if (inputFile != null) {
                tL_photos_uploadProfilePhoto.file = inputFile;
                tL_photos_uploadProfilePhoto.flags |= 1;
            }
            if (inputFile2 != null) {
                tL_photos_uploadProfilePhoto.video = inputFile2;
                int i = tL_photos_uploadProfilePhoto.flags;
                tL_photos_uploadProfilePhoto.video_start_ts = d;
                tL_photos_uploadProfilePhoto.flags = i | 6;
            }
            if (videoSize != null) {
                tL_photos_uploadProfilePhoto.video_emoji_markup = videoSize;
                tL_photos_uploadProfilePhoto.flags |= 16;
            }
            GroupCallActivity.this.accountInstance.getConnectionsManager().sendRequest(tL_photos_uploadProfilePhoto, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    GroupCallActivity.AvatarUpdaterDelegate.this.lambda$didUploadPhoto$1(str, tLObject, tL_error);
                }
            });
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ boolean canFinishFragment() {
            return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public void didStartUpload(boolean z) {
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void didUploadFailed() {
            ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.InputFile inputFile2, final double d, final String str, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2, boolean z, final TLRPC.VideoSize videoSize) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$AvatarUpdaterDelegate$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.AvatarUpdaterDelegate.this.lambda$didUploadPhoto$3(inputFile, inputFile2, videoSize, d, str, photoSize2, photoSize);
                }
            });
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ String getInitialSearchString() {
            return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getInitialSearchString(this);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public void onUploadProgressChanged(float f) {
            GroupCallActivity.this.avatarsViewPager.setUploadProgress(this.uploadingImageLocation, f);
            updateAvatarUploadingProgress(f);
        }

        public void updateAvatarUploadingProgress(float f) {
            this.uploadingProgress = f;
            if (GroupCallActivity.this.listView == null) {
                return;
            }
            for (int i = 0; i < GroupCallActivity.this.listView.getChildCount(); i++) {
                View childAt = GroupCallActivity.this.listView.getChildAt(i);
                if (childAt instanceof GroupCallUserCell) {
                    GroupCallUserCell groupCallUserCell = (GroupCallUserCell) childAt;
                    if (groupCallUserCell.isSelfUser()) {
                        groupCallUserCell.setUploadProgress(f, true);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class GroupCallItemAnimator extends DefaultItemAnimator {
        HashSet addingHolders;
        public float animationProgress;
        public ValueAnimator animator;
        float outMaxBottom;
        float outMinTop;
        HashSet removingHolders;

        private GroupCallItemAnimator() {
            this.addingHolders = new HashSet();
            this.removingHolders = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
            this.animationProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            GroupCallActivity.this.listView.invalidate();
            GroupCallActivity.this.renderersContainer.invalidate();
            ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            GroupCallActivity.this.updateLayout(true);
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void endAnimations() {
            super.endAnimations();
            this.removingHolders.clear();
            this.addingHolders.clear();
            this.outMinTop = Float.MAX_VALUE;
            GroupCallActivity.this.listView.invalidate();
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void runPendingAnimations() {
            boolean z = !this.mPendingRemovals.isEmpty();
            boolean z2 = !this.mPendingMoves.isEmpty();
            boolean z3 = !this.mPendingAdditions.isEmpty();
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.animator = null;
            }
            if (z || z2 || z3) {
                this.animationProgress = 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$GroupCallItemAnimator$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        GroupCallActivity.GroupCallItemAnimator.this.lambda$runPendingAnimations$0(valueAnimator2);
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.GroupCallItemAnimator.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        GroupCallItemAnimator groupCallItemAnimator = GroupCallItemAnimator.this;
                        groupCallItemAnimator.animator = null;
                        GroupCallActivity.this.listView.invalidate();
                        GroupCallActivity.this.renderersContainer.invalidate();
                        ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                        GroupCallActivity.this.updateLayout(true);
                        GroupCallItemAnimator.this.addingHolders.clear();
                        GroupCallItemAnimator.this.removingHolders.clear();
                    }
                });
                this.animator.setDuration(350L);
                this.animator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.animator.start();
                GroupCallActivity.this.listView.invalidate();
                GroupCallActivity.this.renderersContainer.invalidate();
            }
            super.runPendingAnimations();
        }

        public void updateBackgroundBeforeAnimation() {
            if (this.animator != null) {
                return;
            }
            this.addingHolders.clear();
            this.addingHolders.addAll(this.mPendingAdditions);
            this.removingHolders.clear();
            this.removingHolders.addAll(this.mPendingRemovals);
            this.outMaxBottom = 0.0f;
            this.outMinTop = Float.MAX_VALUE;
            if (this.addingHolders.isEmpty() && this.removingHolders.isEmpty()) {
                return;
            }
            int childCount = GroupCallActivity.this.listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = GroupCallActivity.this.listView.getChildAt(i);
                RecyclerView.ViewHolder findContainingViewHolder = GroupCallActivity.this.listView.findContainingViewHolder(childAt);
                if (findContainingViewHolder != null && findContainingViewHolder.getItemViewType() != 3 && findContainingViewHolder.getItemViewType() != 4 && findContainingViewHolder.getItemViewType() != 5 && !this.addingHolders.contains(findContainingViewHolder)) {
                    this.outMaxBottom = Math.max(this.outMaxBottom, childAt.getY() + childAt.getMeasuredHeight());
                    this.outMinTop = Math.min(this.outMinTop, Math.max(0.0f, childAt.getY()));
                }
            }
            this.animationProgress = 0.0f;
            GroupCallActivity.this.listView.invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public class LightningView extends View {
        private int[] currentColors;
        private final Paint paint;
        private int parentBackgroundColor;
        private float shadowOffset;

        public LightningView(Context context) {
            super(context);
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(0);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!GroupCallActivity.this.needTextureLightning || GroupCallActivity.this.renderersContainer.progressToFullscreenMode >= 0.1d) {
                return;
            }
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
        }

        public void setNewColors(int[] iArr) {
            int[] iArr2 = this.currentColors;
            boolean z = true;
            boolean z2 = false;
            if (iArr2 == null || iArr[0] != iArr2[0] || iArr[1] != iArr2[1]) {
                if (iArr2 == null) {
                    this.paint.setAlpha(NotificationCenter.closeSearchByActiveAction);
                }
                this.currentColors = iArr;
                if (GroupCallActivity.this.liveLabelBgColorAnimator != null && GroupCallActivity.this.textureLightningViewAnimatingAlpha != 1.0f) {
                    int[] iArr3 = this.currentColors;
                    iArr3[0] = ColorUtils.setAlphaComponent(iArr3[0], (int) (GroupCallActivity.this.textureLightningViewAnimatingAlpha * 255.0f));
                    int[] iArr4 = this.currentColors;
                    iArr4[1] = ColorUtils.setAlphaComponent(iArr4[1], (int) (GroupCallActivity.this.textureLightningViewAnimatingAlpha * 255.0f));
                }
                this.paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, GroupCallActivity.this.textureLightningView.getMeasuredHeight(), this.currentColors, (float[]) null, Shader.TileMode.CLAMP));
                z2 = true;
            }
            if (this.parentBackgroundColor != GroupCallActivity.this.backgroundColor) {
                this.paint.setShadowLayer(AndroidUtilities.dp(36.0f), 0.0f, this.shadowOffset, GroupCallActivity.this.backgroundColor);
                this.parentBackgroundColor = GroupCallActivity.this.backgroundColor;
            } else {
                z = z2;
            }
            if (z) {
                invalidate();
            }
            GroupCallActivity.this.runUpdateTextureLightningRunnable();
        }

        public void setShadowOffset(int i) {
            float f = i;
            if (this.shadowOffset != f) {
                this.paint.setShadowLayer(AndroidUtilities.dp(36.0f), 0.0f, this.shadowOffset, GroupCallActivity.this.backgroundColor);
                this.shadowOffset = f;
                invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private int addMemberRow;
        private boolean hasSelfUser;
        private int invitedEndRow;
        private int invitedStartRow;
        private int lastRow;
        private Context mContext;
        private int rowsCount;
        private int usersEndRow;
        private int usersStartRow;
        private int usersVideoGridEndRow;
        private int usersVideoGridStartRow;
        private int videoGridDividerRow;
        private int videoNotAvailableRow;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x0110, code lost:
        
            if (org.telegram.messenger.ChatObject.isPublic(r0) != false) goto L48;
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00de  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateRows() {
            ChatObject.Call call = GroupCallActivity.this.call;
            if (call == null || call.isScheduled() || GroupCallActivity.this.delayedGroupCallUpdated) {
                return;
            }
            this.rowsCount = 0;
            GroupCallActivity groupCallActivity = GroupCallActivity.this;
            this.hasSelfUser = groupCallActivity.call.participants.indexOfKey(MessageObject.getPeerId(groupCallActivity.selfPeer)) >= 0;
            int i = this.rowsCount;
            this.usersVideoGridStartRow = i;
            int size = i + GroupCallActivity.this.visibleVideoParticipants.size();
            this.rowsCount = size;
            this.usersVideoGridEndRow = size;
            if (GroupCallActivity.this.visibleVideoParticipants.size() > 0) {
                int i2 = this.rowsCount;
                this.rowsCount = i2 + 1;
                this.videoGridDividerRow = i2;
            } else {
                this.videoGridDividerRow = -1;
            }
            if (!GroupCallActivity.this.visibleVideoParticipants.isEmpty() && ChatObject.canManageCalls(GroupCallActivity.this.currentChat)) {
                GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                if (groupCallActivity2.call.call.participants_count > groupCallActivity2.accountInstance.getMessagesController().groupCallVideoMaxParticipants) {
                    int i3 = this.rowsCount;
                    this.rowsCount = i3 + 1;
                    this.videoNotAvailableRow = i3;
                    this.usersStartRow = this.rowsCount;
                    if (!GroupCallActivity.this.isRtmpStream()) {
                        this.rowsCount += GroupCallActivity.this.call.visibleParticipants.size();
                    }
                    this.usersEndRow = this.rowsCount;
                    if (!GroupCallActivity.this.call.invitedUsers.isEmpty() || GroupCallActivity.this.isRtmpStream()) {
                        this.invitedStartRow = -1;
                        this.invitedEndRow = -1;
                    } else {
                        int i4 = this.rowsCount;
                        this.invitedStartRow = i4;
                        int size2 = i4 + GroupCallActivity.this.call.invitedUsers.size();
                        this.rowsCount = size2;
                        this.invitedEndRow = size2;
                    }
                    if (!GroupCallActivity.this.isRtmpStream()) {
                        if ((ChatObject.isChannel(GroupCallActivity.this.currentChat) && !GroupCallActivity.this.currentChat.megagroup) || !ChatObject.canWriteToChat(GroupCallActivity.this.currentChat)) {
                            if (ChatObject.isChannel(GroupCallActivity.this.currentChat)) {
                                TLRPC.Chat chat = GroupCallActivity.this.currentChat;
                                if (!chat.megagroup) {
                                }
                            }
                        }
                        int i5 = this.rowsCount;
                        this.rowsCount = i5 + 1;
                        this.addMemberRow = i5;
                        int i6 = this.rowsCount;
                        this.rowsCount = i6 + 1;
                        this.lastRow = i6;
                    }
                    this.addMemberRow = -1;
                    int i62 = this.rowsCount;
                    this.rowsCount = i62 + 1;
                    this.lastRow = i62;
                }
            }
            this.videoNotAvailableRow = -1;
            this.usersStartRow = this.rowsCount;
            if (!GroupCallActivity.this.isRtmpStream()) {
            }
            this.usersEndRow = this.rowsCount;
            if (GroupCallActivity.this.call.invitedUsers.isEmpty()) {
            }
            this.invitedStartRow = -1;
            this.invitedEndRow = -1;
            if (!GroupCallActivity.this.isRtmpStream()) {
            }
            this.addMemberRow = -1;
            int i622 = this.rowsCount;
            this.rowsCount = i622 + 1;
            this.lastRow = i622;
        }

        public boolean addSelfToCounter() {
            if (GroupCallActivity.this.isRtmpStream() || this.hasSelfUser || VoIPService.getSharedInstance() == null) {
                return false;
            }
            return !VoIPService.getSharedInstance().isJoined();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.rowsCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == this.lastRow) {
                return 3;
            }
            if (i == this.addMemberRow) {
                return 0;
            }
            if (i == this.videoGridDividerRow) {
                return 5;
            }
            if (i >= this.usersStartRow && i < this.usersEndRow) {
                return 1;
            }
            if (i < this.usersVideoGridStartRow || i >= this.usersVideoGridEndRow) {
                return i == this.videoNotAvailableRow ? 6 : 2;
            }
            return 4;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 3 || itemViewType == 4 || itemViewType == 5 || itemViewType == 6) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            updateRows();
            super.notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemChanged(int i) {
            updateRows();
            super.notifyItemChanged(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemMoved(int i, int i2) {
            updateRows();
            super.notifyItemMoved(i, i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeChanged(int i, int i2) {
            updateRows();
            super.notifyItemRangeChanged(i, i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeChanged(int i, int i2, Object obj) {
            updateRows();
            super.notifyItemRangeChanged(i, i2, obj);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeInserted(int i, int i2) {
            updateRows();
            super.notifyItemRangeInserted(i, i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyItemRangeRemoved(int i, int i2) {
            updateRows();
            super.notifyItemRangeRemoved(i, i2);
        }

        /* JADX WARN: Removed duplicated region for block: B:56:0x010e  */
        /* JADX WARN: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            int i2;
            ArrayList<TLRPC.TL_groupCallParticipant> arrayList;
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant;
            AvatarUpdaterDelegate avatarUpdaterDelegate;
            ArrayList<Long> arrayList2;
            ArrayList arrayList3;
            ChatObject.VideoParticipant videoParticipant;
            AvatarUpdaterDelegate avatarUpdaterDelegate2;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType == 0) {
                GroupCallTextCell groupCallTextCell = (GroupCallTextCell) viewHolder.itemView;
                int offsetColor = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_lastSeenTextUnscrolled), Theme.getColor(Theme.key_voipgroup_lastSeenText), GroupCallActivity.this.actionBar.getTag() != null ? 1.0f : 0.0f, 1.0f);
                groupCallTextCell.setColors(offsetColor, offsetColor);
                if (ChatObject.isChannel(GroupCallActivity.this.currentChat)) {
                    TLRPC.Chat chat = GroupCallActivity.this.currentChat;
                    if (!chat.megagroup && ChatObject.isPublic(chat)) {
                        string = LocaleController.getString(R.string.VoipGroupShareLink);
                        i2 = R.drawable.msg_link;
                        groupCallTextCell.setTextAndIcon(string, i2, false);
                        return;
                    }
                }
                string = LocaleController.getString(R.string.VoipGroupInviteMember);
                i2 = R.drawable.msg_contact_add;
                groupCallTextCell.setTextAndIcon(string, i2, false);
                return;
            }
            TLRPC.FileLocation fileLocation = null;
            r7 = null;
            TLRPC.FileLocation fileLocation2 = null;
            r7 = null;
            r7 = null;
            r7 = null;
            Long l = null;
            fileLocation = null;
            if (itemViewType == 1) {
                GroupCallUserCell groupCallUserCell = (GroupCallUserCell) viewHolder.itemView;
                int i3 = i - this.usersStartRow;
                if (GroupCallActivity.this.delayedGroupCallUpdated) {
                    if (i3 >= 0 && i3 < GroupCallActivity.this.oldParticipants.size()) {
                        arrayList = GroupCallActivity.this.oldParticipants;
                        tL_groupCallParticipant = arrayList.get(i3);
                    }
                    tL_groupCallParticipant = null;
                } else {
                    if (i3 >= 0 && i3 < GroupCallActivity.this.call.visibleParticipants.size()) {
                        arrayList = GroupCallActivity.this.call.visibleParticipants;
                        tL_groupCallParticipant = arrayList.get(i3);
                    }
                    tL_groupCallParticipant = null;
                }
                if (tL_groupCallParticipant != null) {
                    long peerId = MessageObject.getPeerId(tL_groupCallParticipant.peer);
                    long peerId2 = MessageObject.getPeerId(GroupCallActivity.this.selfPeer);
                    if (peerId == peerId2 && (avatarUpdaterDelegate = GroupCallActivity.this.avatarUpdaterDelegate) != null) {
                        fileLocation = avatarUpdaterDelegate.avatar;
                    }
                    TLRPC.FileLocation fileLocation3 = fileLocation;
                    float f = fileLocation3 != null ? GroupCallActivity.this.avatarUpdaterDelegate.uploadingProgress : 1.0f;
                    if (groupCallUserCell.getParticipant() != null && MessageObject.getPeerId(groupCallUserCell.getParticipant().peer) == peerId) {
                        z = true;
                    }
                    groupCallUserCell.setData(GroupCallActivity.this.accountInstance, tL_groupCallParticipant, GroupCallActivity.this.call, peerId2, fileLocation3, z);
                    groupCallUserCell.setUploadProgress(f, z);
                    return;
                }
                return;
            }
            if (itemViewType != 2) {
                if (itemViewType != 4) {
                    return;
                }
                GroupCallGridCell groupCallGridCell = (GroupCallGridCell) viewHolder.itemView;
                ChatObject.VideoParticipant participant = groupCallGridCell.getParticipant();
                int i4 = i - this.usersVideoGridStartRow;
                groupCallGridCell.spanCount = GroupCallActivity.this.spanSizeLookup.getSpanSize(i);
                if (GroupCallActivity.this.delayedGroupCallUpdated) {
                    if (i4 >= 0 && i4 < GroupCallActivity.this.oldVideoParticipants.size()) {
                        arrayList3 = GroupCallActivity.this.oldVideoParticipants;
                        videoParticipant = (ChatObject.VideoParticipant) arrayList3.get(i4);
                    }
                    videoParticipant = null;
                } else {
                    if (i4 >= 0 && i4 < GroupCallActivity.this.visibleVideoParticipants.size()) {
                        arrayList3 = GroupCallActivity.this.visibleVideoParticipants;
                        videoParticipant = (ChatObject.VideoParticipant) arrayList3.get(i4);
                    }
                    videoParticipant = null;
                }
                if (videoParticipant != null) {
                    long peerId3 = MessageObject.getPeerId(videoParticipant.participant.peer);
                    long peerId4 = MessageObject.getPeerId(GroupCallActivity.this.selfPeer);
                    if (peerId3 == peerId4 && (avatarUpdaterDelegate2 = GroupCallActivity.this.avatarUpdaterDelegate) != null) {
                        fileLocation2 = avatarUpdaterDelegate2.avatar;
                    }
                    if (fileLocation2 != null) {
                        float f2 = GroupCallActivity.this.avatarUpdaterDelegate.uploadingProgress;
                    }
                    if (groupCallGridCell.getParticipant() != null) {
                        groupCallGridCell.getParticipant().equals(videoParticipant);
                    }
                    groupCallGridCell.setData(GroupCallActivity.this.accountInstance, videoParticipant, GroupCallActivity.this.call, peerId4);
                }
                if (participant == null || participant.equals(videoParticipant) || !groupCallGridCell.attached || groupCallGridCell.getRenderer() == null) {
                    return;
                }
                GroupCallActivity.this.attachRenderer(groupCallGridCell, false);
                GroupCallActivity.this.attachRenderer(groupCallGridCell, true);
                return;
            }
            GroupCallInvitedCell groupCallInvitedCell = (GroupCallInvitedCell) viewHolder.itemView;
            int i5 = i - this.invitedStartRow;
            if (GroupCallActivity.this.delayedGroupCallUpdated) {
                if (i5 >= 0 && i5 < GroupCallActivity.this.oldInvited.size()) {
                    arrayList2 = GroupCallActivity.this.oldInvited;
                    l = arrayList2.get(i5);
                }
                if (l == null) {
                    groupCallInvitedCell.setData(((BottomSheet) GroupCallActivity.this).currentAccount, l);
                    return;
                }
                return;
            }
            if (i5 >= 0 && i5 < GroupCallActivity.this.call.invitedUsers.size()) {
                arrayList2 = GroupCallActivity.this.call.invitedUsers;
                l = arrayList2.get(i5);
            }
            if (l == null) {
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            GroupCallGridCell groupCallGridCell;
            boolean z = false;
            if (i == 0) {
                view = new GroupCallTextCell(this.mContext) { // from class: org.telegram.ui.GroupCallActivity.ListAdapter.1
                    @Override // org.telegram.ui.Cells.GroupCallTextCell, android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        if (AndroidUtilities.isTablet()) {
                            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(420.0f), View.MeasureSpec.getSize(i2)), 1073741824);
                        }
                        super.onMeasure(i2, i3);
                    }
                };
            } else if (i == 1) {
                view = new GroupCallUserCell(this.mContext) { // from class: org.telegram.ui.GroupCallActivity.ListAdapter.2
                    @Override // org.telegram.ui.Cells.GroupCallUserCell, android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        if (AndroidUtilities.isTablet()) {
                            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(420.0f), View.MeasureSpec.getSize(i2)), 1073741824);
                        }
                        super.onMeasure(i2, i3);
                    }

                    @Override // org.telegram.ui.Cells.GroupCallUserCell
                    /* renamed from: onMuteClick */
                    protected void lambda$new$5(GroupCallUserCell groupCallUserCell) {
                        GroupCallActivity.this.showMenuForCell(groupCallUserCell);
                    }
                };
            } else if (i != 2) {
                if (i == 4) {
                    groupCallGridCell = new GroupCallGridCell(this.mContext, z) { // from class: org.telegram.ui.GroupCallActivity.ListAdapter.4
                        @Override // org.telegram.ui.Components.voip.GroupCallGridCell, android.view.ViewGroup, android.view.View
                        protected void onAttachedToWindow() {
                            super.onAttachedToWindow();
                            if (GroupCallActivity.this.listView.getVisibility() == 0 && GroupCallActivity.this.listViewVideoVisibility) {
                                GroupCallActivity.this.attachRenderer(this, true);
                            }
                        }

                        @Override // org.telegram.ui.Components.voip.GroupCallGridCell, android.view.ViewGroup, android.view.View
                        protected void onDetachedFromWindow() {
                            super.onDetachedFromWindow();
                            GroupCallActivity.this.attachRenderer(this, false);
                        }
                    };
                } else if (i == 5) {
                    view = new View(this.mContext) { // from class: org.telegram.ui.GroupCallActivity.ListAdapter.5
                        @Override // android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(GroupCallActivity.isLandscapeMode ? 0.0f : 8.0f), 1073741824));
                        }
                    };
                } else if (i != 6) {
                    view = new View(this.mContext);
                } else {
                    TextView textView = new TextView(this.mContext);
                    textView.setTextColor(-8682615);
                    textView.setTextSize(1, 13.0f);
                    textView.setGravity(1);
                    textView.setPadding(0, 0, 0, AndroidUtilities.dp(10.0f));
                    textView.setText(ChatObject.isChannelOrGiga(GroupCallActivity.this.currentChat) ? LocaleController.formatString("VoipChannelVideoNotAvailableAdmin", R.string.VoipChannelVideoNotAvailableAdmin, LocaleController.formatPluralString("Participants", GroupCallActivity.this.accountInstance.getMessagesController().groupCallVideoMaxParticipants, new Object[0])) : LocaleController.formatString("VoipVideoNotAvailableAdmin", R.string.VoipVideoNotAvailableAdmin, LocaleController.formatPluralString("Members", GroupCallActivity.this.accountInstance.getMessagesController().groupCallVideoMaxParticipants, new Object[0])));
                    groupCallGridCell = textView;
                }
                view = groupCallGridCell;
            } else {
                view = new GroupCallInvitedCell(this.mContext) { // from class: org.telegram.ui.GroupCallActivity.ListAdapter.3
                    @Override // org.telegram.ui.Cells.GroupCallInvitedCell, android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        if (AndroidUtilities.isTablet()) {
                            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(420.0f), View.MeasureSpec.getSize(i2)), 1073741824);
                        }
                        super.onMeasure(i2, i3);
                    }
                };
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 1) {
                GroupCallUserCell groupCallUserCell = (GroupCallUserCell) viewHolder.itemView;
                int i = GroupCallActivity.this.actionBar.getTag() != null ? Theme.key_voipgroup_mutedIcon : Theme.key_voipgroup_mutedIconUnscrolled;
                groupCallUserCell.setGrayIconColor(i, Theme.getColor(i));
                groupCallUserCell.setDrawDivider(viewHolder.getAdapterPosition() != getItemCount() - 2);
                return;
            }
            if (itemViewType == 2) {
                GroupCallInvitedCell groupCallInvitedCell = (GroupCallInvitedCell) viewHolder.itemView;
                int i2 = GroupCallActivity.this.actionBar.getTag() != null ? Theme.key_voipgroup_mutedIcon : Theme.key_voipgroup_mutedIconUnscrolled;
                groupCallInvitedCell.setGrayIconColor(i2, Theme.getColor(i2));
                groupCallInvitedCell.setDrawDivider(viewHolder.getAdapterPosition() != getItemCount() - 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class RecordCallDrawable extends Drawable {
        private long lastUpdateTime;
        private View parentView;
        private boolean recording;
        private int state;
        private Paint paint = new Paint(1);
        private Paint paint2 = new Paint(1);
        private float alpha = 1.0f;

        public RecordCallDrawable() {
            this.paint.setColor(-1);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(1.5f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            float centerX = getBounds().centerX();
            float centerY = getBounds().centerY();
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(10.0f), this.paint);
            this.paint2.setColor(this.recording ? -1147527 : -1);
            this.paint2.setAlpha((int) (this.alpha * 255.0f));
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(5.0f), this.paint2);
            if (this.recording) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = elapsedRealtime - this.lastUpdateTime;
                if (j > 17) {
                    j = 17;
                }
                this.lastUpdateTime = elapsedRealtime;
                int i = this.state;
                if (i == 0) {
                    float f = this.alpha + (((float) j) / 2000.0f);
                    this.alpha = f;
                    if (f >= 1.0f) {
                        this.alpha = 1.0f;
                        this.state = 1;
                    }
                } else if (i == 1) {
                    float f2 = this.alpha - (((float) j) / 2000.0f);
                    this.alpha = f2;
                    if (f2 < 0.5f) {
                        this.alpha = 0.5f;
                        this.state = 0;
                    }
                }
                this.parentView.invalidate();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setParentView(View view) {
            this.parentView = view;
        }

        public void setRecording(boolean z) {
            this.recording = z;
            this.alpha = 1.0f;
            invalidateSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SmallRecordCallDrawable extends Drawable {
        private long lastUpdateTime;
        private View parentView;
        private int state;
        private Paint paint2 = new Paint(1);
        private float alpha = 1.0f;

        public SmallRecordCallDrawable(View view) {
            this.parentView = view;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            int dp;
            int centerX = getBounds().centerX();
            int centerY = getBounds().centerY();
            if (this.parentView instanceof SimpleTextView) {
                dp = centerY + AndroidUtilities.dp(1.0f);
                centerX -= AndroidUtilities.dp(3.0f);
            } else {
                dp = centerY + AndroidUtilities.dp(2.0f);
            }
            this.paint2.setColor(-1147527);
            this.paint2.setAlpha((int) (this.alpha * 255.0f));
            canvas.drawCircle(centerX, dp, AndroidUtilities.dp(4.0f), this.paint2);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            if (j > 17) {
                j = 17;
            }
            this.lastUpdateTime = elapsedRealtime;
            int i = this.state;
            if (i == 0) {
                float f = this.alpha + (((float) j) / 2000.0f);
                this.alpha = f;
                if (f >= 1.0f) {
                    this.alpha = 1.0f;
                    this.state = 1;
                }
            } else if (i == 1) {
                float f2 = this.alpha - (((float) j) / 2000.0f);
                this.alpha = f2;
                if (f2 < 0.5f) {
                    this.alpha = 0.5f;
                    this.state = 0;
                }
            }
            this.parentView.invalidate();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class UpdateCallback implements ListUpdateCallback {
        final RecyclerView.Adapter adapter;

        private UpdateCallback(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onChanged(int i, int i2, Object obj) {
            this.adapter.notifyItemRangeChanged(i, i2, obj);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onInserted(int i, int i2) {
            this.adapter.notifyItemRangeInserted(i, i2);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onMoved(int i, int i2) {
            this.adapter.notifyItemMoved(i, i2);
        }

        @Override // androidx.recyclerview.widget.ListUpdateCallback
        public void onRemoved(int i, int i2) {
            this.adapter.notifyItemRangeRemoved(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class VolumeSlider extends FrameLayout {
        private boolean captured;
        private float colorChangeProgress;
        private int currentColor;
        private TLRPC.TL_groupCallParticipant currentParticipant;
        private double currentProgress;
        private boolean dragging;
        private RLottieImageView imageView;
        private long lastUpdateTime;
        private int oldColor;
        private Paint paint;
        private Paint paint2;
        private Path path;
        private float[] radii;
        private RectF rect;
        private RLottieDrawable speakerDrawable;
        private float sx;
        private float sy;
        private TextView textView;
        private int thumbX;
        private float[] volumeAlphas;

        public VolumeSlider(Context context, TLRPC.TL_groupCallParticipant tL_groupCallParticipant) {
            super(context);
            this.paint = new Paint(1);
            this.paint2 = new Paint(1);
            this.path = new Path();
            this.radii = new float[8];
            this.rect = new RectF();
            this.volumeAlphas = new float[3];
            setWillNotDraw(false);
            this.currentParticipant = tL_groupCallParticipant;
            this.currentProgress = ChatObject.getParticipantVolume(tL_groupCallParticipant) / 20000.0f;
            this.colorChangeProgress = 1.0f;
            setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
            int i = R.raw.speaker;
            this.speakerDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), true, null);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setAnimation(this.speakerDrawable);
            this.imageView.setTag(this.currentProgress == 0.0d ? 1 : null);
            addView(this.imageView, LayoutHelper.createFrame(-2, 40.0f, (LocaleController.isRTL ? 5 : 3) | 16, 0.0f, 0.0f, 0.0f, 0.0f));
            this.speakerDrawable.setCustomEndFrame(this.currentProgress == 0.0d ? 17 : 34);
            RLottieDrawable rLottieDrawable = this.speakerDrawable;
            rLottieDrawable.setCurrentFrame(rLottieDrawable.getCustomEndFrame() - 1, false, true);
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(3);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setTextColor(Theme.getColor(Theme.key_voipgroup_actionBarItems));
            this.textView.setTextSize(1, 16.0f);
            double participantVolume = ChatObject.getParticipantVolume(this.currentParticipant);
            Double.isNaN(participantVolume);
            double d = participantVolume / 100.0d;
            this.textView.setText(String.format(Locale.US, "%d%%", Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d))));
            this.textView.setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(43.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(43.0f) : 0, 0);
            addView(this.textView, LayoutHelper.createFrame(-2, -2, (LocaleController.isRTL ? 5 : 3) | 16));
            this.paint2.setStyle(Paint.Style.STROKE);
            this.paint2.setStrokeWidth(AndroidUtilities.dp(1.5f));
            this.paint2.setStrokeCap(Paint.Cap.ROUND);
            this.paint2.setColor(-1);
            double participantVolume2 = ChatObject.getParticipantVolume(this.currentParticipant);
            Double.isNaN(participantVolume2);
            int i2 = (int) (participantVolume2 / 100.0d);
            int i3 = 0;
            while (true) {
                float[] fArr = this.volumeAlphas;
                if (i3 >= fArr.length) {
                    return;
                }
                if (i2 > (i3 == 0 ? 0 : i3 == 1 ? 50 : 150)) {
                    fArr[i3] = 1.0f;
                } else {
                    fArr[i3] = 0.0f;
                }
                i3++;
            }
        }

        private void onSeekBarDrag(double d, boolean z) {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            this.currentProgress = d;
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.currentParticipant;
            tL_groupCallParticipant.volume = (int) (d * 20000.0d);
            tL_groupCallParticipant.volume_by_admin = false;
            tL_groupCallParticipant.flags |= 128;
            double participantVolume = ChatObject.getParticipantVolume(tL_groupCallParticipant);
            Double.isNaN(participantVolume);
            double d2 = participantVolume / 100.0d;
            this.textView.setText(String.format(Locale.US, "%d%%", Integer.valueOf((int) (d2 > 0.0d ? Math.max(d2, 1.0d) : 0.0d))));
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = this.currentParticipant;
            sharedInstance.setParticipantVolume(tL_groupCallParticipant2, tL_groupCallParticipant2.volume);
            if (z) {
                long peerId = MessageObject.getPeerId(this.currentParticipant.peer);
                TLObject user = peerId > 0 ? GroupCallActivity.this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId)) : GroupCallActivity.this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId));
                if (this.currentParticipant.volume == 0) {
                    if (GroupCallActivity.this.scrimPopupWindow != null) {
                        GroupCallActivity.this.scrimPopupWindow.dismiss();
                        GroupCallActivity.this.scrimPopupWindow = null;
                    }
                    GroupCallActivity.this.dismissAvatarPreview(true);
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    groupCallActivity.processSelectedOption(this.currentParticipant, peerId, ChatObject.canManageCalls(groupCallActivity.currentChat) ? 0 : 5);
                } else {
                    VoIPService.getSharedInstance().editCallMember(user, null, null, Integer.valueOf(this.currentParticipant.volume), null, null);
                }
            }
            Integer num = this.currentProgress == 0.0d ? 1 : null;
            if ((this.imageView.getTag() != null || num == null) && (this.imageView.getTag() == null || num != null)) {
                return;
            }
            this.speakerDrawable.setCustomEndFrame(this.currentProgress == 0.0d ? 17 : 34);
            this.speakerDrawable.setCurrentFrame(this.currentProgress != 0.0d ? 17 : 0);
            this.speakerDrawable.start();
            this.imageView.setTag(num);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i;
            float dp;
            int i2;
            int i3 = this.currentColor;
            double d = this.currentProgress;
            this.currentColor = d < 0.25d ? -3385513 : (d <= 0.25d || d >= 0.5d) ? (d < 0.5d || d > 0.75d) ? -11688225 : -11027349 : -3562181;
            float f = 1.0f;
            if (i3 == 0) {
                i = this.currentColor;
                this.colorChangeProgress = 1.0f;
            } else {
                int offsetColor = AndroidUtilities.getOffsetColor(this.oldColor, i3, this.colorChangeProgress, 1.0f);
                if (i3 != this.currentColor) {
                    this.colorChangeProgress = 0.0f;
                    this.oldColor = offsetColor;
                }
                i = offsetColor;
            }
            this.paint.setColor(i);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            if (j > 17) {
                j = 17;
            }
            this.lastUpdateTime = elapsedRealtime;
            float f2 = this.colorChangeProgress;
            if (f2 < 1.0f) {
                float f3 = f2 + (((float) j) / 200.0f);
                this.colorChangeProgress = f3;
                if (f3 > 1.0f) {
                    this.colorChangeProgress = 1.0f;
                } else {
                    invalidate();
                }
            }
            this.path.reset();
            float[] fArr = this.radii;
            float f4 = 6.0f;
            float dp2 = AndroidUtilities.dp(6.0f);
            fArr[7] = dp2;
            fArr[6] = dp2;
            int i4 = 1;
            fArr[1] = dp2;
            fArr[0] = dp2;
            float max = this.thumbX < AndroidUtilities.dp(12.0f) ? Math.max(0.0f, (this.thumbX - AndroidUtilities.dp(6.0f)) / AndroidUtilities.dp(6.0f)) : 1.0f;
            float[] fArr2 = this.radii;
            float dp3 = AndroidUtilities.dp(6.0f) * max;
            fArr2[5] = dp3;
            fArr2[4] = dp3;
            fArr2[3] = dp3;
            fArr2[2] = dp3;
            this.rect.set(0.0f, 0.0f, this.thumbX, getMeasuredHeight());
            this.path.addRoundRect(this.rect, this.radii, Path.Direction.CW);
            this.path.close();
            canvas.drawPath(this.path, this.paint);
            double participantVolume = ChatObject.getParticipantVolume(this.currentParticipant);
            Double.isNaN(participantVolume);
            int i5 = (int) (participantVolume / 100.0d);
            int left = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() / 2) + AndroidUtilities.dp(5.0f);
            int top = this.imageView.getTop() + (this.imageView.getMeasuredHeight() / 2);
            int i6 = 0;
            while (i6 < this.volumeAlphas.length) {
                if (i6 == 0) {
                    dp = AndroidUtilities.dp(f4);
                    i2 = 0;
                } else if (i6 == i4) {
                    dp = AndroidUtilities.dp(10.0f);
                    i2 = 50;
                } else {
                    dp = AndroidUtilities.dp(14.0f);
                    i2 = 150;
                }
                float dp4 = AndroidUtilities.dp(2.0f);
                float f5 = this.volumeAlphas[i6];
                float f6 = dp4 * (f - f5);
                this.paint2.setAlpha((int) (f5 * 255.0f));
                float f7 = left;
                float f8 = top;
                this.rect.set((f7 - dp) + f6, (f8 - dp) + f6, (f7 + dp) - f6, (f8 + dp) - f6);
                int i7 = i2;
                int i8 = i6;
                canvas.drawArc(this.rect, -50.0f, 100.0f, false, this.paint2);
                if (i5 > i7) {
                    float[] fArr3 = this.volumeAlphas;
                    float f9 = fArr3[i8];
                    if (f9 < 1.0f) {
                        float f10 = f9 + (((float) j) / 180.0f);
                        fArr3[i8] = f10;
                        if (f10 > 1.0f) {
                            fArr3[i8] = 1.0f;
                        }
                        invalidate();
                        i6 = i8 + 1;
                        f = 1.0f;
                        f4 = 6.0f;
                        i4 = 1;
                    } else {
                        i6 = i8 + 1;
                        f = 1.0f;
                        f4 = 6.0f;
                        i4 = 1;
                    }
                } else {
                    float[] fArr4 = this.volumeAlphas;
                    float f11 = fArr4[i8];
                    if (f11 > 0.0f) {
                        float f12 = f11 - (((float) j) / 180.0f);
                        fArr4[i8] = f12;
                        if (f12 < 0.0f) {
                            fArr4[i8] = 0.0f;
                        }
                        invalidate();
                        i6 = i8 + 1;
                        f = 1.0f;
                        f4 = 6.0f;
                        i4 = 1;
                    } else {
                        i6 = i8 + 1;
                        f = 1.0f;
                        f4 = 6.0f;
                        i4 = 1;
                    }
                }
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return onTouch(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
            double size = View.MeasureSpec.getSize(i);
            double d = this.currentProgress;
            Double.isNaN(size);
            this.thumbX = (int) (size * d);
        }

        boolean onTouch(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.sx = motionEvent.getX();
                this.sy = motionEvent.getY();
                return true;
            }
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                this.captured = false;
                if (motionEvent.getAction() == 1) {
                    if (Math.abs(motionEvent.getY() - this.sy) < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        int x = (int) motionEvent.getX();
                        this.thumbX = x;
                        if (x < 0) {
                            this.thumbX = 0;
                        } else if (x > getMeasuredWidth()) {
                            this.thumbX = getMeasuredWidth();
                        }
                        this.dragging = true;
                    }
                }
                if (this.dragging) {
                    if (motionEvent.getAction() == 1) {
                        double d = this.thumbX;
                        double measuredWidth = getMeasuredWidth();
                        Double.isNaN(d);
                        Double.isNaN(measuredWidth);
                        onSeekBarDrag(d / measuredWidth, true);
                    }
                    this.dragging = false;
                    invalidate();
                    return true;
                }
            } else if (motionEvent.getAction() == 2) {
                if (!this.captured) {
                    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
                    if (Math.abs(motionEvent.getY() - this.sy) <= viewConfiguration.getScaledTouchSlop() && Math.abs(motionEvent.getX() - this.sx) > viewConfiguration.getScaledTouchSlop()) {
                        this.captured = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        if (motionEvent.getY() >= 0.0f && motionEvent.getY() <= getMeasuredHeight()) {
                            int x2 = (int) motionEvent.getX();
                            this.thumbX = x2;
                            if (x2 < 0) {
                                this.thumbX = 0;
                            } else if (x2 > getMeasuredWidth()) {
                                this.thumbX = getMeasuredWidth();
                            }
                            this.dragging = true;
                            invalidate();
                            return true;
                        }
                    }
                } else if (this.dragging) {
                    int x3 = (int) motionEvent.getX();
                    this.thumbX = x3;
                    if (x3 < 0) {
                        this.thumbX = 0;
                    } else if (x3 > getMeasuredWidth()) {
                        this.thumbX = getMeasuredWidth();
                    }
                    double d2 = this.thumbX;
                    double measuredWidth2 = getMeasuredWidth();
                    Double.isNaN(d2);
                    Double.isNaN(measuredWidth2);
                    onSeekBarDrag(d2 / measuredWidth2, false);
                    invalidate();
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return onTouch(motionEvent);
        }
    }

    /* loaded from: classes4.dex */
    public class WatchersView extends LinearLayout {
        private float lastWidth;
        private final AnimatedTextView watchersCountTextView;

        public WatchersView(Context context) {
            super(context);
            this.lastWidth = 0.0f;
            setOrientation(1);
            setGravity(17);
            AnimatedTextView animatedTextView = new AnimatedTextView(context, true, false, false);
            this.watchersCountTextView = animatedTextView;
            animatedTextView.setTextColor(-1);
            animatedTextView.setTextSize(AndroidUtilities.dp(46.0f));
            animatedTextView.setTypeface(AndroidUtilities.bold());
            animatedTextView.setGravity(1);
            TextView textView = new TextView(context);
            textView.setTextColor(-1);
            textView.setTextSize(1, 14.0f);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setText(LocaleController.getString(R.string.VoipChannelWatching));
            addView(animatedTextView, LayoutHelper.createLinear(-1, 46));
            addView(textView, LayoutHelper.createLinear(-2, -2));
        }

        void setWatchersCount(int i) {
            String formatNumber = LocaleController.formatNumber(i, ',');
            float measureText = this.watchersCountTextView.getPaint().measureText((CharSequence) formatNumber, 0, formatNumber.length());
            if (this.lastWidth != measureText) {
                this.watchersCountTextView.getPaint().setShader(new LinearGradient(0.0f, 0.0f, measureText, 0.0f, new int[]{GroupCallActivity.this.getThemedColor(Theme.key_premiumGradient1), GroupCallActivity.this.getThemedColor(Theme.key_premiumGradient3)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                this.lastWidth = measureText;
            }
            this.watchersCountTextView.setText(formatNumber);
        }
    }

    /* loaded from: classes4.dex */
    public static class WeavingState {
        public int currentState;
        private float duration;
        public Shader shader;
        private float startX;
        private float startY;
        private float time;
        private float targetX = -1.0f;
        private float targetY = -1.0f;
        private Matrix matrix = new Matrix();

        public WeavingState(int i) {
            this.currentState = i;
        }

        private void setTarget() {
            float nextInt;
            if (GroupCallActivity.isGradientState(this.currentState)) {
                this.targetX = ((Utilities.random.nextInt(100) * 0.2f) / 100.0f) + 0.85f;
                nextInt = 1.0f;
            } else if (this.currentState == 1) {
                this.targetX = ((Utilities.random.nextInt(100) * 0.3f) / 100.0f) + 0.2f;
                nextInt = ((Utilities.random.nextInt(100) * 0.3f) / 100.0f) + 0.7f;
            } else {
                this.targetX = ((Utilities.random.nextInt(100) / 100.0f) * 0.2f) + 0.8f;
                nextInt = Utilities.random.nextInt(100) / 100.0f;
            }
            this.targetY = nextInt;
        }

        public void update(int i, int i2, int i3, long j, float f) {
            if (this.shader == null) {
                return;
            }
            float f2 = this.duration;
            if (f2 == 0.0f || this.time >= f2) {
                this.duration = Utilities.random.nextInt(NotificationCenter.storyQualityUpdate) + 1500;
                this.time = 0.0f;
                if (this.targetX == -1.0f) {
                    setTarget();
                }
                this.startX = this.targetX;
                this.startY = this.targetY;
                setTarget();
            }
            float f3 = (float) j;
            float f4 = this.time + ((BlobDrawable.GRADIENT_SPEED_MIN + 0.5f) * f3) + (f3 * BlobDrawable.GRADIENT_SPEED_MAX * 2.0f * f);
            this.time = f4;
            float f5 = this.duration;
            if (f4 > f5) {
                this.time = f5;
            }
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.time / f5);
            float f6 = i3;
            float f7 = this.startX;
            float f8 = (i2 + ((f7 + ((this.targetX - f7) * interpolation)) * f6)) - 200.0f;
            float f9 = this.startY;
            float f10 = (i + (f6 * (f9 + ((this.targetY - f9) * interpolation)))) - 200.0f;
            float dp = (AndroidUtilities.dp(122.0f) / 400.0f) * (GroupCallActivity.isGradientState(this.currentState) ? 1.0f : this.currentState == 1 ? 4.0f : 2.5f);
            this.matrix.reset();
            this.matrix.postTranslate(f8, f10);
            this.matrix.postScale(dp, dp, f8 + 200.0f, f10 + 200.0f);
            this.shader.setLocalMatrix(this.matrix);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x033e  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x07f2  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0844 A[LOOP:0: B:39:0x0842->B:40:0x0844, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0a2f  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0afa  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0c5d  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0cec  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0d7d  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0e4b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0f48  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0f59  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x116c  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x1177  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0cf3  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0c66  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0456  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private GroupCallActivity(final Context context, final AccountInstance accountInstance, ChatObject.Call call, final TLRPC.Chat chat, TLRPC.InputPeer inputPeer, boolean z, String str) {
        super(context, false);
        TLRPC.TL_peerUser tL_peerUser;
        long j;
        int i;
        int i2;
        int i3;
        int i4;
        this.muteLabel = new TextView[2];
        this.undoView = new UndoView[2];
        this.visibleVideoParticipants = new ArrayList();
        this.rect = new RectF();
        this.listViewBackgroundPaint = new Paint(1);
        this.oldParticipants = new ArrayList();
        this.oldVideoParticipants = new ArrayList();
        this.oldInvited = new ArrayList();
        this.muteButtonState = 0;
        this.animatingToFullscreenExpand = false;
        this.paint = new Paint(7);
        this.paintTmp = new Paint(7);
        this.leaveBackgroundPaint = new Paint(1);
        this.states = new WeavingState[8];
        this.switchProgress = 1.0f;
        this.shaderBitmapSize = NotificationCenter.storyQualityUpdate;
        this.invalidateColors = true;
        this.colorsTmp = new int[3];
        this.attachedRenderers = new ArrayList();
        this.attachedRenderersTmp = new ArrayList();
        this.wasExpandBigSize = Boolean.TRUE;
        this.cellFlickerDrawable = new CellFlickerDrawable();
        this.statusIconPool = new ArrayList();
        this.buttonsAnimationParamsX = new HashMap();
        this.buttonsAnimationParamsY = new HashMap();
        this.onUserLeaveHintListener = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.onUserLeaveHint();
            }
        };
        this.updateSchedeulRunnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity.1
            @Override // java.lang.Runnable
            public void run() {
                if (GroupCallActivity.this.scheduleTimeTextView == null || GroupCallActivity.this.isDismissed()) {
                    return;
                }
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                ChatObject.Call call2 = groupCallActivity.call;
                int i5 = call2 != null ? call2.call.schedule_date : groupCallActivity.scheduleStartAt;
                if (i5 == 0) {
                    return;
                }
                int currentTime = i5 - GroupCallActivity.this.accountInstance.getConnectionsManager().getCurrentTime();
                if (currentTime >= 86400) {
                    GroupCallActivity.this.scheduleTimeTextView.setText(LocaleController.formatPluralString("Days", Math.round(currentTime / 86400.0f), new Object[0]));
                } else {
                    GroupCallActivity.this.scheduleTimeTextView.setText(AndroidUtilities.formatFullDuration(Math.abs(currentTime)));
                    if (currentTime < 0 && GroupCallActivity.this.scheduleStartInTextView.getTag() == null) {
                        GroupCallActivity.this.scheduleStartInTextView.setTag(1);
                        GroupCallActivity.this.scheduleStartInTextView.setText(LocaleController.getString(R.string.VoipChatLateBy));
                    }
                }
                GroupCallActivity.this.scheduleStartAtTextView.setText(LocaleController.formatStartsTime(i5, 3));
                AndroidUtilities.runOnUIThread(GroupCallActivity.this.updateSchedeulRunnable, 1000L);
            }
        };
        this.unmuteRunnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.lambda$new$0();
            }
        };
        this.pressRunnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.lambda$new$1();
            }
        };
        this.needTextureLightning = false;
        this.updateTextureLightningRunnable = new 2();
        this.visiblePeerIds = new LongSparseIntArray();
        this.gradientColors = new int[2];
        this.listViewVideoVisibility = true;
        this.invites = new String[2];
        this.popupAnimationIndex = -1;
        this.diffUtilsCallback = new DiffUtil.Callback() { // from class: org.telegram.ui.GroupCallActivity.61
            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public boolean areContentsTheSame(int i5, int i6) {
                return true;
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public boolean areItemsTheSame(int i5, int i6) {
                if (GroupCallActivity.this.listAdapter.addMemberRow >= 0) {
                    if (i5 == GroupCallActivity.this.oldAddMemberRow && i6 == GroupCallActivity.this.listAdapter.addMemberRow) {
                        return true;
                    }
                    if ((i5 == GroupCallActivity.this.oldAddMemberRow && i6 != GroupCallActivity.this.listAdapter.addMemberRow) || (i5 != GroupCallActivity.this.oldAddMemberRow && i6 == GroupCallActivity.this.listAdapter.addMemberRow)) {
                        return false;
                    }
                }
                if (GroupCallActivity.this.listAdapter.videoNotAvailableRow >= 0) {
                    if (i5 == GroupCallActivity.this.oldVideoNotAvailableRow && i6 == GroupCallActivity.this.listAdapter.videoNotAvailableRow) {
                        return true;
                    }
                    if ((i5 == GroupCallActivity.this.oldVideoNotAvailableRow && i6 != GroupCallActivity.this.listAdapter.videoNotAvailableRow) || (i5 != GroupCallActivity.this.oldVideoNotAvailableRow && i6 == GroupCallActivity.this.listAdapter.videoNotAvailableRow)) {
                        return false;
                    }
                }
                if (GroupCallActivity.this.listAdapter.videoGridDividerRow >= 0 && GroupCallActivity.this.listAdapter.videoGridDividerRow == i6 && i5 == GroupCallActivity.this.oldVideoDividerRow) {
                    return true;
                }
                if (i5 == GroupCallActivity.this.oldCount - 1 && i6 == GroupCallActivity.this.listAdapter.rowsCount - 1) {
                    return true;
                }
                if (i5 != GroupCallActivity.this.oldCount - 1 && i6 != GroupCallActivity.this.listAdapter.rowsCount - 1) {
                    if (i6 >= GroupCallActivity.this.listAdapter.usersVideoGridStartRow && i6 < GroupCallActivity.this.listAdapter.usersVideoGridEndRow && i5 >= GroupCallActivity.this.oldUsersVideoStartRow && i5 < GroupCallActivity.this.oldUsersVideoEndRow) {
                        ChatObject.VideoParticipant videoParticipant = (ChatObject.VideoParticipant) GroupCallActivity.this.oldVideoParticipants.get(i5 - GroupCallActivity.this.oldUsersVideoStartRow);
                        GroupCallActivity groupCallActivity = GroupCallActivity.this;
                        return videoParticipant.equals((ChatObject.VideoParticipant) groupCallActivity.visibleVideoParticipants.get(i6 - groupCallActivity.listAdapter.usersVideoGridStartRow));
                    }
                    if (i6 >= GroupCallActivity.this.listAdapter.usersStartRow && i6 < GroupCallActivity.this.listAdapter.usersEndRow && i5 >= GroupCallActivity.this.oldUsersStartRow && i5 < GroupCallActivity.this.oldUsersEndRow) {
                        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) GroupCallActivity.this.oldParticipants.get(i5 - GroupCallActivity.this.oldUsersStartRow);
                        GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                        if (MessageObject.getPeerId(tL_groupCallParticipant.peer) == MessageObject.getPeerId(groupCallActivity2.call.visibleParticipants.get(i6 - groupCallActivity2.listAdapter.usersStartRow).peer)) {
                            return i5 == i6 || tL_groupCallParticipant.lastActiveDate == ((long) tL_groupCallParticipant.active_date);
                        }
                        return false;
                    }
                    if (i6 >= GroupCallActivity.this.listAdapter.invitedStartRow && i6 < GroupCallActivity.this.listAdapter.invitedEndRow && i5 >= GroupCallActivity.this.oldInvitedStartRow && i5 < GroupCallActivity.this.oldInvitedEndRow) {
                        Long l = (Long) GroupCallActivity.this.oldInvited.get(i5 - GroupCallActivity.this.oldInvitedStartRow);
                        GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                        return l.equals(groupCallActivity3.call.invitedUsers.get(i6 - groupCallActivity3.listAdapter.invitedStartRow));
                    }
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public int getNewListSize() {
                return GroupCallActivity.this.listAdapter.rowsCount;
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public int getOldListSize() {
                return GroupCallActivity.this.oldCount;
            }
        };
        setOpenNoDelay(true);
        this.accountInstance = accountInstance;
        this.call = call;
        this.schedulePeer = inputPeer;
        this.currentChat = chat;
        this.scheduledHash = str;
        this.currentAccount = accountInstance.getCurrentAccount();
        this.scheduleHasFewPeers = z;
        this.fullWidth = true;
        isTabletMode = false;
        isLandscapeMode = false;
        paused = false;
        setDelegate(new BottomSheet.BottomSheetDelegateInterface() { // from class: org.telegram.ui.GroupCallActivity.4
            @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
            public boolean canDismiss() {
                return true;
            }

            @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
            public void onOpenAnimationEnd() {
                CountDownLatch groupCallBottomSheetLatch;
                VoIPService sharedInstance = VoIPService.getSharedInstance();
                if (sharedInstance != null && (groupCallBottomSheetLatch = sharedInstance.getGroupCallBottomSheetLatch()) != null) {
                    groupCallBottomSheetLatch.countDown();
                }
                if (GroupCallActivity.this.muteButtonState == 6) {
                    GroupCallActivity.this.showReminderHint();
                }
            }
        });
        this.drawDoubleNavigationBar = true;
        this.drawNavigationBar = true;
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().setNavigationBarColor(-16777216);
        }
        this.scrollNavBar = true;
        this.navBarColorKey = -1;
        this.scrimPaint = new Paint() { // from class: org.telegram.ui.GroupCallActivity.5
            @Override // android.graphics.Paint
            public void setAlpha(int i5) {
                super.setAlpha(i5);
                if (((BottomSheet) GroupCallActivity.this).containerView != null) {
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            }
        };
        setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda25
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                GroupCallActivity.this.lambda$new$10(dialogInterface);
            }
        });
        setDimBehindAlpha(75);
        this.listAdapter = new ListAdapter(context);
        final RecordStatusDrawable recordStatusDrawable = new RecordStatusDrawable(true);
        int i5 = Theme.key_voipgroup_speakingText;
        recordStatusDrawable.setColor(Theme.getColor(i5));
        recordStatusDrawable.start();
        ActionBar actionBar = new ActionBar(context) { // from class: org.telegram.ui.GroupCallActivity.6
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                if (getAdditionalSubtitleTextView().getVisibility() == 0) {
                    canvas.save();
                    canvas.translate(getSubtitleTextView().getLeft(), getSubtitleTextView().getY() - AndroidUtilities.dp(1.0f));
                    recordStatusDrawable.setAlpha((int) (getAdditionalSubtitleTextView().getAlpha() * 255.0f));
                    recordStatusDrawable.draw(canvas);
                    canvas.restore();
                    invalidate();
                }
            }

            @Override // android.view.View
            public void setAlpha(float f) {
                if (getAlpha() != f) {
                    super.setAlpha(f);
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            }
        };
        this.actionBar = actionBar;
        actionBar.setSubtitle("");
        this.actionBar.getSubtitleTextView().setVisibility(0);
        this.actionBar.createAdditionalSubtitleTextView();
        this.actionBar.getAdditionalSubtitleTextView().setPadding(AndroidUtilities.dp(24.0f), 0, 0, 0);
        AndroidUtilities.updateViewVisibilityAnimated(this.actionBar.getAdditionalSubtitleTextView(), this.drawSpeakingSubtitle, 1.0f, false);
        this.actionBar.getAdditionalSubtitleTextView().setTextColor(Theme.getColor(i5));
        ActionBar actionBar2 = this.actionBar;
        int i6 = Theme.key_voipgroup_lastSeenTextUnscrolled;
        actionBar2.setSubtitleColor(Theme.getColor(i6));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAllowOverlayTitle(false);
        ActionBar actionBar3 = this.actionBar;
        int i7 = Theme.key_voipgroup_actionBarItems;
        actionBar3.setItemsColor(Theme.getColor(i7), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefaultSelector), false);
        this.actionBar.setTitleColor(Theme.getColor(i7));
        this.actionBar.setSubtitleColor(Theme.getColor(i6));
        this.actionBar.setActionBarMenuOnItemClick(new 7(context));
        TLRPC.InputPeer groupCallPeer = inputPeer != null ? inputPeer : VoIPService.getSharedInstance().getGroupCallPeer();
        if (groupCallPeer != null) {
            if (groupCallPeer instanceof TLRPC.TL_inputPeerChannel) {
                TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
                this.selfPeer = tL_peerChannel;
                tL_peerChannel.channel_id = groupCallPeer.channel_id;
            } else if (groupCallPeer instanceof TLRPC.TL_inputPeerUser) {
                tL_peerUser = new TLRPC.TL_peerUser();
                this.selfPeer = tL_peerUser;
                j = groupCallPeer.user_id;
            } else if (groupCallPeer instanceof TLRPC.TL_inputPeerChat) {
                TLRPC.TL_peerChat tL_peerChat = new TLRPC.TL_peerChat();
                this.selfPeer = tL_peerChat;
                tL_peerChat.chat_id = groupCallPeer.chat_id;
            }
            VoIPService.audioLevelsCallback = new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda26
                @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                    GroupCallActivity.this.lambda$new$11(iArr, fArr, zArr);
                }
            };
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallUpdated);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.needShowAlert);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.didLoadChatAdmins);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.applyGroupCallVisibleParticipants);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.userInfoDidLoad);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.mainUserInfoChanged);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallScreencastStateChanged);
            this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallSpeakingUsersUpdated);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndCall);
            this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            int i8 = R.raw.voip_filled;
            this.bigMicDrawable = new RLottieDrawable(i8, "" + i8, AndroidUtilities.dp(72.0f), AndroidUtilities.dp(72.0f), true, null);
            int i9 = R.raw.hand_2;
            this.handDrawables = new RLottieDrawable(i9, "" + i9, AndroidUtilities.dp(72.0f), AndroidUtilities.dp(72.0f), true, null);
            FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.GroupCallActivity.8
                private int lastSize;
                boolean localHasVideo;
                private boolean updateRenderers;
                boolean wasLayout;
                private boolean ignoreLayout = false;
                private RectF rect = new RectF();
                HashMap listCells = new HashMap();

                /* JADX WARN: Removed duplicated region for block: B:111:0x0462  */
                /* JADX WARN: Removed duplicated region for block: B:114:0x0494  */
                /* JADX WARN: Removed duplicated region for block: B:118:0x0529  */
                /* JADX WARN: Removed duplicated region for block: B:121:0x0599  */
                /* JADX WARN: Removed duplicated region for block: B:125:0x05ad A[LOOP:5: B:123:0x05aa->B:125:0x05ad, LOOP_END] */
                /* JADX WARN: Removed duplicated region for block: B:129:0x05f3  */
                /* JADX WARN: Removed duplicated region for block: B:134:0x05a2  */
                /* JADX WARN: Removed duplicated region for block: B:135:0x055b  */
                /* JADX WARN: Removed duplicated region for block: B:136:0x04b7  */
                /* JADX WARN: Removed duplicated region for block: B:139:0x06ab  */
                @Override // android.view.ViewGroup, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                protected void dispatchDraw(Canvas canvas) {
                    float f;
                    float f2;
                    float f3;
                    View view;
                    GroupCallUserCell groupCallUserCell;
                    GroupCallUserCell groupCallUserCell2;
                    float y;
                    float left;
                    GroupCallUserCell groupCallUserCell3;
                    Path path;
                    float[] fArr;
                    GroupCallUserCell groupCallUserCell4;
                    float f4;
                    int i10;
                    float left2;
                    float f5;
                    float f6;
                    float f7;
                    if (GroupCallActivity.isTabletMode) {
                        GroupCallActivity.this.buttonsContainer.setTranslationY(0.0f);
                        GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                        GroupCallActivity.this.buttonsContainer.setTranslationX(0.0f);
                        GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                    } else if (GroupCallActivity.isLandscapeMode) {
                        GroupCallActivity.this.buttonsContainer.setTranslationY(0.0f);
                        GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                        GroupCallActivity.this.buttonsContainer.setTranslationX(GroupCallActivity.this.progressToHideUi * AndroidUtilities.dp(94.0f));
                        GroupCallActivity groupCallActivity = GroupCallActivity.this;
                        groupCallActivity.fullscreenUsersListView.setTranslationX(groupCallActivity.progressToHideUi * AndroidUtilities.dp(94.0f));
                    } else {
                        GroupCallActivity.this.buttonsContainer.setTranslationX(0.0f);
                        GroupCallActivity.this.fullscreenUsersListView.setTranslationX(0.0f);
                        GroupCallActivity.this.buttonsContainer.setTranslationY(GroupCallActivity.this.progressToHideUi * AndroidUtilities.dp(94.0f));
                        GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                        groupCallActivity2.fullscreenUsersListView.setTranslationY(groupCallActivity2.progressToHideUi * AndroidUtilities.dp(94.0f));
                    }
                    for (int i11 = 0; i11 < GroupCallActivity.this.listView.getChildCount(); i11++) {
                        View childAt = GroupCallActivity.this.listView.getChildAt(i11);
                        if (childAt instanceof GroupCallUserCell) {
                            ((GroupCallUserCell) childAt).setDrawAvatar(true);
                        }
                        if (!(childAt instanceof GroupCallGridCell)) {
                            if (childAt.getMeasuredWidth() != GroupCallActivity.this.listView.getMeasuredWidth()) {
                                childAt.setTranslationX((GroupCallActivity.this.listView.getMeasuredWidth() - childAt.getMeasuredWidth()) >> 1);
                            } else {
                                childAt.setTranslationX(0.0f);
                            }
                        }
                    }
                    if (!GroupCallActivity.this.renderersContainer.isAnimating()) {
                        for (int i12 = 0; i12 < GroupCallActivity.this.fullscreenUsersListView.getChildCount(); i12++) {
                            ((GroupCallFullscreenAdapter.GroupCallUserCell) GroupCallActivity.this.fullscreenUsersListView.getChildAt(i12)).setProgressToFullscreen(1.0f);
                        }
                    } else if (GroupCallActivity.this.fullscreenUsersListView.getVisibility() == 0) {
                        this.listCells.clear();
                        for (int i13 = 0; i13 < GroupCallActivity.this.listView.getChildCount(); i13++) {
                            View childAt2 = GroupCallActivity.this.listView.getChildAt(i13);
                            if ((childAt2 instanceof GroupCallGridCell) && GroupCallActivity.this.listView.getChildAdapterPosition(childAt2) >= 0) {
                                GroupCallGridCell groupCallGridCell = (GroupCallGridCell) childAt2;
                                if (groupCallGridCell.getRenderer() != GroupCallActivity.this.renderersContainer.fullscreenTextureView) {
                                    this.listCells.put(groupCallGridCell.getParticipant(), childAt2);
                                }
                            } else if ((childAt2 instanceof GroupCallUserCell) && GroupCallActivity.this.listView.getChildAdapterPosition(childAt2) >= 0) {
                                GroupCallUserCell groupCallUserCell5 = (GroupCallUserCell) childAt2;
                                this.listCells.put(groupCallUserCell5.getParticipant(), groupCallUserCell5);
                            }
                        }
                        for (int i14 = 0; i14 < GroupCallActivity.this.fullscreenUsersListView.getChildCount(); i14++) {
                            GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell6 = (GroupCallFullscreenAdapter.GroupCallUserCell) GroupCallActivity.this.fullscreenUsersListView.getChildAt(i14);
                            View view2 = (View) this.listCells.get(groupCallUserCell6.getVideoParticipant());
                            if (view2 == null) {
                                view2 = (View) this.listCells.get(groupCallUserCell6.getParticipant());
                            }
                            float f8 = GroupCallActivity.this.renderersContainer.progressToFullscreenMode;
                            if (!GroupCallActivity.this.fullscreenListItemAnimator.isRunning()) {
                                groupCallUserCell6.setAlpha(1.0f);
                            }
                            if (view2 != null) {
                                if (view2 instanceof GroupCallGridCell) {
                                    GroupCallGridCell groupCallGridCell2 = (GroupCallGridCell) view2;
                                    left2 = (groupCallGridCell2.getLeft() + GroupCallActivity.this.listView.getX()) - GroupCallActivity.this.renderersContainer.getLeft();
                                    f5 = (groupCallGridCell2.getTop() + GroupCallActivity.this.listView.getY()) - GroupCallActivity.this.renderersContainer.getTop();
                                    f6 = groupCallUserCell6.getLeft() + GroupCallActivity.this.fullscreenUsersListView.getX();
                                    f7 = groupCallUserCell6.getTop() + GroupCallActivity.this.fullscreenUsersListView.getY();
                                } else {
                                    left2 = ((r3.getLeft() + GroupCallActivity.this.listView.getX()) - GroupCallActivity.this.renderersContainer.getLeft()) + r3.getAvatarImageView().getLeft() + (r3.getAvatarImageView().getMeasuredWidth() >> 1);
                                    float top = ((r3.getTop() + GroupCallActivity.this.listView.getY()) - GroupCallActivity.this.renderersContainer.getTop()) + r3.getAvatarImageView().getTop() + (r3.getAvatarImageView().getMeasuredHeight() >> 1);
                                    float left3 = groupCallUserCell6.getLeft() + GroupCallActivity.this.fullscreenUsersListView.getX() + (groupCallUserCell6.getMeasuredWidth() >> 1);
                                    float top2 = groupCallUserCell6.getTop() + GroupCallActivity.this.fullscreenUsersListView.getY() + (groupCallUserCell6.getMeasuredHeight() >> 1);
                                    ((GroupCallUserCell) view2).setDrawAvatar(false);
                                    f5 = top;
                                    f6 = left3;
                                    f7 = top2;
                                }
                                float f9 = left2 - f6;
                                float f10 = 1.0f - f8;
                                groupCallUserCell6.setTranslationX(f9 * f10);
                                groupCallUserCell6.setTranslationY((f5 - f7) * f10);
                                groupCallUserCell6.setScaleX(1.0f);
                                groupCallUserCell6.setScaleY(1.0f);
                                groupCallUserCell6.setProgressToFullscreen(f8);
                            } else {
                                groupCallUserCell6.setScaleX(1.0f);
                                groupCallUserCell6.setScaleY(1.0f);
                                groupCallUserCell6.setTranslationX(0.0f);
                                groupCallUserCell6.setTranslationY(0.0f);
                                groupCallUserCell6.setProgressToFullscreen(1.0f);
                                if (groupCallUserCell6.getRenderer() == null) {
                                    groupCallUserCell6.setAlpha(f8);
                                }
                            }
                        }
                    }
                    for (int i15 = 0; i15 < GroupCallActivity.this.attachedRenderers.size(); i15++) {
                        GroupCallMiniTextureView groupCallMiniTextureView = (GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderers.get(i15);
                        RecyclerListView recyclerListView = GroupCallActivity.this.listView;
                        GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                        groupCallMiniTextureView.updatePosition(recyclerListView, groupCallActivity3.tabletVideoGridView, groupCallActivity3.fullscreenUsersListView, groupCallActivity3.renderersContainer);
                    }
                    if (GroupCallActivity.isTabletMode) {
                        GroupCallActivity.this.buttonsBackgroundGradientView.setAlpha(1.0f);
                        GroupCallActivity.this.buttonsBackgroundGradientView2.setAlpha(1.0f);
                    } else {
                        GroupCallActivity.this.buttonsBackgroundGradientView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                        GroupCallActivity.this.buttonsBackgroundGradientView2.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                    }
                    if (GroupCallActivity.this.renderersContainer.swipedBack) {
                        GroupCallActivity.this.listView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                    } else {
                        GroupCallActivity.this.listView.setAlpha(1.0f);
                    }
                    if (GroupCallActivity.this.watchersView != null) {
                        GroupCallActivity.this.watchersView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                        GroupCallActivity.this.watchersView.setTranslationY(GroupCallActivity.this.renderersContainer.progressToFullscreenMode * AndroidUtilities.dp(64.0f));
                    }
                    super.dispatchDraw(canvas);
                    GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                    if (groupCallActivity4.drawingForBlur) {
                        return;
                    }
                    float f11 = 255.0f;
                    if (groupCallActivity4.avatarsPreviewShowed) {
                        if (GroupCallActivity.this.scrimView != null) {
                            if (!GroupCallActivity.this.useBlur) {
                                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), GroupCallActivity.this.scrimPaint);
                            }
                            float y2 = GroupCallActivity.this.listView.getY();
                            float[] fArr2 = new float[8];
                            Path path2 = new Path();
                            int childCount = GroupCallActivity.this.listView.getChildCount();
                            float y3 = GroupCallActivity.this.listView.getY() + GroupCallActivity.this.listView.getMeasuredHeight();
                            if (GroupCallActivity.this.hasScrimAnchorView) {
                                for (int i16 = 0; i16 < childCount; i16++) {
                                    if (GroupCallActivity.this.listView.getChildAt(i16) != GroupCallActivity.this.scrimView) {
                                    }
                                }
                                groupCallUserCell = null;
                                groupCallUserCell2 = groupCallUserCell;
                                if (groupCallUserCell2 != null && y2 < y3) {
                                    canvas.save();
                                    if (GroupCallActivity.this.scrimFullscreenView == null) {
                                        canvas.clipRect(0.0f, (1.0f - GroupCallActivity.this.progressToAvatarPreview) * y2, getMeasuredWidth(), ((1.0f - GroupCallActivity.this.progressToAvatarPreview) * y3) + (getMeasuredHeight() * GroupCallActivity.this.progressToAvatarPreview));
                                    }
                                    if (GroupCallActivity.this.hasScrimAnchorView) {
                                        y = GroupCallActivity.this.avatarPreviewContainer.getTop() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth();
                                        left = GroupCallActivity.this.avatarPreviewContainer.getLeft();
                                    } else {
                                        y = ((GroupCallActivity.this.listView.getY() + groupCallUserCell2.getY()) * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + ((GroupCallActivity.this.avatarPreviewContainer.getTop() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth()) * GroupCallActivity.this.progressToAvatarPreview);
                                        left = ((GroupCallActivity.this.listView.getLeft() + groupCallUserCell2.getX()) * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + (GroupCallActivity.this.avatarPreviewContainer.getLeft() * GroupCallActivity.this.progressToAvatarPreview);
                                    }
                                    float f12 = y;
                                    canvas.translate(left, f12);
                                    if (GroupCallActivity.this.hasScrimAnchorView) {
                                        groupCallUserCell3 = groupCallUserCell2;
                                        path = path2;
                                        fArr = fArr2;
                                        canvas.saveLayerAlpha(0.0f, 0.0f, groupCallUserCell2.getMeasuredWidth(), groupCallUserCell2.getClipHeight(), (int) (GroupCallActivity.this.progressToAvatarPreview * 255.0f), 31);
                                    } else {
                                        groupCallUserCell3 = groupCallUserCell2;
                                        path = path2;
                                        fArr = fArr2;
                                        canvas.save();
                                    }
                                    float measuredHeight = (int) (groupCallUserCell3.getMeasuredHeight() + ((groupCallUserCell3.getClipHeight() - groupCallUserCell3.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - GroupCallActivity.this.progressToAvatarPreview))));
                                    this.rect.set(0.0f, 0.0f, groupCallUserCell3.getMeasuredWidth(), measuredHeight);
                                    if (GroupCallActivity.this.hasScrimAnchorView) {
                                        groupCallUserCell4 = groupCallUserCell3;
                                        f4 = 1.0f;
                                    } else {
                                        f4 = GroupCallActivity.this.progressToAvatarPreview;
                                        groupCallUserCell4 = groupCallUserCell3;
                                    }
                                    groupCallUserCell4.setProgressToAvatarPreview(f4);
                                    for (i10 = 0; i10 < 4; i10++) {
                                        fArr[i10] = AndroidUtilities.dp(13.0f) * (1.0f - GroupCallActivity.this.progressToAvatarPreview);
                                        fArr[i10 + 4] = AndroidUtilities.dp(13.0f);
                                    }
                                    path.reset();
                                    Path path3 = path;
                                    path3.addRoundRect(this.rect, fArr, Path.Direction.CW);
                                    path3.close();
                                    canvas.drawPath(path3, GroupCallActivity.this.listViewBackgroundPaint);
                                    groupCallUserCell4.draw(canvas);
                                    canvas.restore();
                                    canvas.restore();
                                    if (GroupCallActivity.this.scrimPopupLayout != null) {
                                        float f13 = f12 + measuredHeight;
                                        float measuredWidth = (getMeasuredWidth() - GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth()) - AndroidUtilities.dp(14.0f);
                                        if (GroupCallActivity.this.progressToAvatarPreview != 1.0f) {
                                            canvas.saveLayerAlpha(measuredWidth, f13, measuredWidth + GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth(), f13 + GroupCallActivity.this.scrimPopupLayout.getMeasuredHeight(), (int) (GroupCallActivity.this.progressToAvatarPreview * 255.0f), 31);
                                        } else {
                                            canvas.save();
                                        }
                                        GroupCallActivity.this.scrimPopupLayout.setTranslationX(measuredWidth - GroupCallActivity.this.scrimPopupLayout.getLeft());
                                        GroupCallActivity.this.scrimPopupLayout.setTranslationY(f13 - GroupCallActivity.this.scrimPopupLayout.getTop());
                                        float f14 = (GroupCallActivity.this.progressToAvatarPreview * 0.2f) + 0.8f;
                                        canvas.scale(f14, f14, (GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth() / 2.0f) + measuredWidth, f13);
                                        canvas.translate(measuredWidth, f13);
                                        GroupCallActivity.this.scrimPopupLayout.draw(canvas);
                                        canvas.restore();
                                    }
                                }
                                if (!GroupCallActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                                    canvas.save();
                                    if (GroupCallActivity.this.hasScrimAnchorView && GroupCallActivity.this.scrimFullscreenView == null) {
                                        canvas.clipRect(0.0f, y2 * (1.0f - GroupCallActivity.this.progressToAvatarPreview), getMeasuredWidth(), (y3 * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + (getMeasuredHeight() * GroupCallActivity.this.progressToAvatarPreview));
                                    }
                                    canvas.scale(GroupCallActivity.this.avatarPreviewContainer.getScaleX(), GroupCallActivity.this.avatarPreviewContainer.getScaleY(), GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY());
                                    canvas.translate(GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY());
                                    GroupCallActivity.this.avatarPreviewContainer.draw(canvas);
                                    canvas.restore();
                                }
                            }
                            groupCallUserCell = GroupCallActivity.this.scrimView;
                            groupCallUserCell2 = groupCallUserCell;
                            if (groupCallUserCell2 != null) {
                                canvas.save();
                                if (GroupCallActivity.this.scrimFullscreenView == null) {
                                }
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                }
                                float f122 = y;
                                canvas.translate(left, f122);
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                }
                                float measuredHeight2 = (int) (groupCallUserCell3.getMeasuredHeight() + ((groupCallUserCell3.getClipHeight() - groupCallUserCell3.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - GroupCallActivity.this.progressToAvatarPreview))));
                                this.rect.set(0.0f, 0.0f, groupCallUserCell3.getMeasuredWidth(), measuredHeight2);
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                }
                                groupCallUserCell4.setProgressToAvatarPreview(f4);
                                while (i10 < 4) {
                                }
                                path.reset();
                                Path path32 = path;
                                path32.addRoundRect(this.rect, fArr, Path.Direction.CW);
                                path32.close();
                                canvas.drawPath(path32, GroupCallActivity.this.listViewBackgroundPaint);
                                groupCallUserCell4.draw(canvas);
                                canvas.restore();
                                canvas.restore();
                                if (GroupCallActivity.this.scrimPopupLayout != null) {
                                }
                            }
                            if (!GroupCallActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                            }
                        }
                        if (GroupCallActivity.this.progressToAvatarPreview == 1.0f || GroupCallActivity.this.scrimFullscreenView != null) {
                            return;
                        }
                        canvas.saveLayerAlpha((int) GroupCallActivity.this.buttonsBackgroundGradientView2.getX(), (int) GroupCallActivity.this.buttonsBackgroundGradientView.getY(), (int) (GroupCallActivity.this.buttonsBackgroundGradientView2.getX() + GroupCallActivity.this.buttonsBackgroundGradientView2.getMeasuredWidth()), getMeasuredHeight(), (int) ((1.0f - GroupCallActivity.this.progressToAvatarPreview) * 255.0f), 31);
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.buttonsBackgroundGradientView2.getX(), GroupCallActivity.this.buttonsBackgroundGradientView2.getY());
                        GroupCallActivity.this.buttonsBackgroundGradientView2.draw(canvas);
                        canvas.restore();
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.buttonsBackgroundGradientView.getX(), GroupCallActivity.this.buttonsBackgroundGradientView.getY());
                        GroupCallActivity.this.buttonsBackgroundGradientView.draw(canvas);
                        canvas.restore();
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.buttonsContainer.getX(), GroupCallActivity.this.buttonsContainer.getY());
                        GroupCallActivity.this.buttonsContainer.draw(canvas);
                        canvas.restore();
                        for (int i17 = 0; i17 < 2; i17++) {
                            if (GroupCallActivity.this.undoView[i17].getVisibility() == 0) {
                                canvas.save();
                                canvas.translate(GroupCallActivity.this.undoView[1].getX(), GroupCallActivity.this.undoView[1].getY());
                                GroupCallActivity.this.undoView[1].draw(canvas);
                                canvas.restore();
                            }
                        }
                    } else {
                        if (GroupCallActivity.this.scrimView == null) {
                            return;
                        }
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), GroupCallActivity.this.scrimPaint);
                        float y4 = GroupCallActivity.this.listView.getY();
                        GroupCallActivity.this.listView.getY();
                        GroupCallActivity.this.listView.getMeasuredHeight();
                        if (GroupCallActivity.this.hasScrimAnchorView) {
                            int childCount2 = GroupCallActivity.this.listView.getChildCount();
                            int i18 = 0;
                            while (i18 < childCount2) {
                                View childAt3 = GroupCallActivity.this.listView.getChildAt(i18);
                                if (childAt3 == GroupCallActivity.this.scrimView) {
                                    float max = Math.max(GroupCallActivity.this.listView.getLeft(), GroupCallActivity.this.listView.getLeft() + childAt3.getX());
                                    float max2 = Math.max(y4, GroupCallActivity.this.listView.getY() + childAt3.getY());
                                    float min = Math.min(GroupCallActivity.this.listView.getRight(), GroupCallActivity.this.listView.getLeft() + childAt3.getX() + childAt3.getMeasuredWidth());
                                    float min2 = Math.min(GroupCallActivity.this.listView.getY() + GroupCallActivity.this.listView.getMeasuredHeight(), GroupCallActivity.this.listView.getY() + childAt3.getY() + GroupCallActivity.this.scrimView.getClipHeight());
                                    if (max2 < min2) {
                                        if (childAt3.getAlpha() != 1.0f) {
                                            int alpha = (int) (childAt3.getAlpha() * f11);
                                            f = min;
                                            f2 = max2;
                                            f3 = max;
                                            view = childAt3;
                                            canvas.saveLayerAlpha(max, max2, min, min2, alpha, 31);
                                        } else {
                                            f = min;
                                            f2 = max2;
                                            f3 = max;
                                            view = childAt3;
                                            canvas.save();
                                        }
                                        canvas.clipRect(f3, f2, f, getMeasuredHeight());
                                        canvas.translate(GroupCallActivity.this.listView.getLeft() + view.getX(), GroupCallActivity.this.listView.getY() + view.getY());
                                        this.rect.set(0.0f, 0.0f, view.getMeasuredWidth(), (int) (GroupCallActivity.this.scrimView.getMeasuredHeight() + ((GroupCallActivity.this.scrimView.getClipHeight() - GroupCallActivity.this.scrimView.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - r1)))));
                                        GroupCallActivity.this.scrimView.setAboutVisibleProgress(GroupCallActivity.this.listViewBackgroundPaint.getColor(), GroupCallActivity.this.scrimPaint.getAlpha() / 100.0f);
                                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), GroupCallActivity.this.listViewBackgroundPaint);
                                        view.draw(canvas);
                                        canvas.restore();
                                        i18++;
                                        f11 = 255.0f;
                                    }
                                }
                                i18++;
                                f11 = 255.0f;
                            }
                            return;
                        }
                        if (GroupCallActivity.this.scrimFullscreenView != null) {
                            canvas.save();
                            canvas.translate(GroupCallActivity.this.scrimFullscreenView.getX() + GroupCallActivity.this.fullscreenUsersListView.getX() + GroupCallActivity.this.renderersContainer.getX(), GroupCallActivity.this.scrimFullscreenView.getY() + GroupCallActivity.this.fullscreenUsersListView.getY() + GroupCallActivity.this.renderersContainer.getY());
                            ((GroupCallActivity.this.scrimFullscreenView.getRenderer() == null || !GroupCallActivity.this.scrimFullscreenView.getRenderer().isAttached() || GroupCallActivity.this.scrimFullscreenView.getRenderer().showingInFullscreen) ? GroupCallActivity.this.scrimFullscreenView : GroupCallActivity.this.scrimFullscreenView.getRenderer()).draw(canvas);
                            GroupCallActivity.this.scrimFullscreenView.drawOverlays(canvas);
                        } else {
                            if (GroupCallActivity.this.scrimRenderer == null || !GroupCallActivity.this.scrimRenderer.isAttached()) {
                                return;
                            }
                            canvas.save();
                            canvas.translate(GroupCallActivity.this.scrimRenderer.getX() + GroupCallActivity.this.renderersContainer.getX(), GroupCallActivity.this.scrimRenderer.getY() + GroupCallActivity.this.renderersContainer.getY());
                            GroupCallActivity.this.scrimRenderer.draw(canvas);
                        }
                    }
                    canvas.restore();
                }

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j2) {
                    if (!GroupCallActivity.isTabletMode && GroupCallActivity.this.renderersContainer.progressToFullscreenMode == 1.0f && (view == GroupCallActivity.this.actionBar || view == GroupCallActivity.this.actionBarShadow || view == GroupCallActivity.this.actionBarBackground || view == GroupCallActivity.this.titleTextView || view == GroupCallActivity.this.menuItemsContainer || view == GroupCallActivity.this.textureLightningView)) {
                        return true;
                    }
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    if (groupCallActivity.drawingForBlur && view == groupCallActivity.renderersContainer) {
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.renderersContainer.getX() + GroupCallActivity.this.fullscreenUsersListView.getX(), GroupCallActivity.this.renderersContainer.getY() + GroupCallActivity.this.fullscreenUsersListView.getY());
                        GroupCallActivity.this.fullscreenUsersListView.draw(canvas);
                        canvas.restore();
                        return true;
                    }
                    if (view == GroupCallActivity.this.avatarPreviewContainer || view == GroupCallActivity.this.scrimPopupLayout || view == GroupCallActivity.this.scrimView) {
                        return true;
                    }
                    if (GroupCallActivity.this.contentFullyOverlayed && GroupCallActivity.this.useBlur && (view == GroupCallActivity.this.listView || view == GroupCallActivity.this.buttonsContainer)) {
                        return true;
                    }
                    if (GroupCallActivity.this.scrimFullscreenView == null) {
                        GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                        if (!groupCallActivity2.drawingForBlur && groupCallActivity2.avatarsPreviewShowed && (view == GroupCallActivity.this.buttonsBackgroundGradientView2 || view == GroupCallActivity.this.buttonsBackgroundGradientView || view == GroupCallActivity.this.buttonsContainer || view == GroupCallActivity.this.undoView[0] || view == GroupCallActivity.this.undoView[1])) {
                            return true;
                        }
                    }
                    return super.drawChild(canvas, view, j2);
                }

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    float f;
                    int dp = AndroidUtilities.dp(74.0f);
                    float f2 = GroupCallActivity.this.scrollOffsetY - dp;
                    int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop;
                    if (((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + f2 < ActionBar.getCurrentActionBarHeight()) {
                        float min = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - f2) - ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop) / ((dp - ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop) - AndroidUtilities.dp(14.0f)));
                        int currentActionBarHeight = (int) ((ActionBar.getCurrentActionBarHeight() - r0) * min);
                        f2 -= currentActionBarHeight;
                        measuredHeight += currentActionBarHeight;
                        f = 1.0f - min;
                    } else {
                        f = 1.0f;
                    }
                    float paddingTop = f2 + getPaddingTop();
                    if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode != 1.0f) {
                        GroupCallActivity.this.shadowDrawable.setBounds(0, (int) paddingTop, getMeasuredWidth(), measuredHeight);
                        GroupCallActivity.this.shadowDrawable.draw(canvas);
                        if (f != 1.0f) {
                            Theme.dialogs_onlineCirclePaint.setColor(GroupCallActivity.this.backgroundColor);
                            this.rect.set(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + paddingTop, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + paddingTop + AndroidUtilities.dp(24.0f));
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int) (GroupCallActivity.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(GroupCallActivity.this.backgroundColor) * 0.8f), (int) (Color.green(GroupCallActivity.this.backgroundColor) * 0.8f), (int) (Color.blue(GroupCallActivity.this.backgroundColor) * 0.8f)));
                        canvas.drawRect(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), Theme.dialogs_onlineCirclePaint);
                        PrivateVideoPreviewDialog privateVideoPreviewDialog = GroupCallActivity.this.previewDialog;
                        if (privateVideoPreviewDialog != null) {
                            Theme.dialogs_onlineCirclePaint.setColor(privateVideoPreviewDialog.getBackgroundColor());
                            canvas.drawRect(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), Theme.dialogs_onlineCirclePaint);
                        }
                    }
                    if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode != 0.0f) {
                        Theme.dialogs_onlineCirclePaint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_actionBar), (int) (GroupCallActivity.this.renderersContainer.progressToFullscreenMode * 255.0f)));
                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_onlineCirclePaint);
                    }
                    if (GroupCallActivity.this.isRtmpStream() && LiteMode.isEnabled(512)) {
                        if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode < 0.15d) {
                            if (GroupCallActivity.this.needTextureLightning) {
                                return;
                            }
                            GroupCallActivity.this.needTextureLightning = true;
                            GroupCallActivity.this.runUpdateTextureLightningRunnable();
                            return;
                        }
                        if (GroupCallActivity.this.needTextureLightning) {
                            GroupCallActivity.this.needTextureLightning = false;
                            AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.updateTextureLightningRunnable);
                        }
                    }
                }

                @Override // android.view.ViewGroup
                public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                    if (GroupCallActivity.this.scrimView != null && motionEvent.getAction() == 0) {
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        this.rect.set(GroupCallActivity.this.scrimPopupLayout.getX(), GroupCallActivity.this.scrimPopupLayout.getY(), GroupCallActivity.this.scrimPopupLayout.getX() + GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth(), GroupCallActivity.this.scrimPopupLayout.getY() + GroupCallActivity.this.scrimPopupLayout.getMeasuredHeight());
                        boolean z2 = !this.rect.contains(x, y);
                        this.rect.set(GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY(), GroupCallActivity.this.avatarPreviewContainer.getX() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth(), GroupCallActivity.this.avatarPreviewContainer.getY() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth() + GroupCallActivity.this.scrimView.getMeasuredHeight());
                        if (this.rect.contains(x, y)) {
                            z2 = false;
                        }
                        if (z2) {
                            GroupCallActivity.this.dismissAvatarPreview(true);
                            return true;
                        }
                    }
                    if (motionEvent.getAction() == 0 && GroupCallActivity.this.scrollOffsetY != 0.0f && motionEvent.getY() < GroupCallActivity.this.scrollOffsetY - AndroidUtilities.dp(37.0f) && GroupCallActivity.this.actionBar.getAlpha() == 0.0f && !GroupCallActivity.this.avatarsPreviewShowed) {
                        GroupCallActivity groupCallActivity = GroupCallActivity.this;
                        if (groupCallActivity.previewDialog == null && !groupCallActivity.renderersContainer.inFullscreenMode) {
                            GroupCallActivity.this.dismiss();
                            return true;
                        }
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }

                @Override // android.view.View, android.view.KeyEvent.Callback
                public boolean onKeyDown(int i10, KeyEvent keyEvent) {
                    if (GroupCallActivity.this.scrimView == null || i10 != 4) {
                        return super.onKeyDown(i10, keyEvent);
                    }
                    GroupCallActivity.this.dismissAvatarPreview(true);
                    return true;
                }

                @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z2, int i10, int i11, int i12, int i13) {
                    float f;
                    boolean z3;
                    if (GroupCallActivity.isTabletMode && this.localHasVideo != GroupCallActivity.this.hasVideo && this.wasLayout) {
                        f = GroupCallActivity.this.listView.getX();
                        z3 = true;
                    } else {
                        f = 0.0f;
                        z3 = false;
                    }
                    this.localHasVideo = GroupCallActivity.this.hasVideo;
                    GroupCallActivity.this.renderersContainer.inLayout = true;
                    super.onLayout(z2, i10, i11, i12, i13);
                    GroupCallActivity.this.renderersContainer.inLayout = false;
                    GroupCallActivity.this.updateLayout(false);
                    this.wasLayout = true;
                    if (!z3 || GroupCallActivity.this.listView.getLeft() == f) {
                        return;
                    }
                    float left = f - GroupCallActivity.this.listView.getLeft();
                    GroupCallActivity.this.listView.setTranslationX(left);
                    GroupCallActivity.this.buttonsContainer.setTranslationX(left);
                    GroupCallActivity.this.buttonsBackgroundGradientView.setTranslationX(left);
                    GroupCallActivity.this.buttonsBackgroundGradientView2.setTranslationX(left);
                    ViewPropertyAnimator duration = GroupCallActivity.this.listView.animate().translationX(0.0f).setDuration(350L);
                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                    duration.setInterpolator(cubicBezierInterpolator).start();
                    GroupCallActivity.this.buttonsBackgroundGradientView.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
                    GroupCallActivity.this.buttonsBackgroundGradientView2.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
                    GroupCallActivity.this.buttonsContainer.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
                }

                /* JADX WARN: Removed duplicated region for block: B:173:0x0587  */
                /* JADX WARN: Removed duplicated region for block: B:179:0x060f  */
                /* JADX WARN: Removed duplicated region for block: B:186:0x0647  */
                /* JADX WARN: Removed duplicated region for block: B:191:0x06a4  */
                /* JADX WARN: Removed duplicated region for block: B:196:0x06e2  */
                /* JADX WARN: Removed duplicated region for block: B:206:0x0779 A[LOOP:2: B:204:0x076d->B:206:0x0779, LOOP_END] */
                /* JADX WARN: Removed duplicated region for block: B:210:0x07af  */
                /* JADX WARN: Removed duplicated region for block: B:215:0x0611  */
                /* JADX WARN: Removed duplicated region for block: B:216:0x05b9  */
                @Override // android.widget.FrameLayout, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                protected void onMeasure(int i10, int i11) {
                    int i12;
                    int max;
                    int i13;
                    int measuredHeight;
                    int size = View.MeasureSpec.getSize(i11);
                    this.ignoreLayout = true;
                    boolean z2 = View.MeasureSpec.getSize(i10) > size && !AndroidUtilities.isTablet();
                    GroupCallActivity.this.renderersContainer.listWidth = View.MeasureSpec.getSize(i10);
                    boolean z3 = AndroidUtilities.isTablet() && View.MeasureSpec.getSize(i10) > size && !GroupCallActivity.this.isRtmpStream();
                    if (GroupCallActivity.isLandscapeMode != z2) {
                        GroupCallActivity.isLandscapeMode = z2;
                        int measuredWidth = GroupCallActivity.this.muteButton.getMeasuredWidth();
                        if (measuredWidth == 0) {
                            measuredWidth = GroupCallActivity.this.muteButton.getLayoutParams().width;
                        }
                        float dp = AndroidUtilities.dp(52.0f) / (measuredWidth - AndroidUtilities.dp(8.0f));
                        if (!GroupCallActivity.isLandscapeMode && !GroupCallActivity.this.isRtmpStream() && !GroupCallActivity.this.renderersContainer.inFullscreenMode) {
                            dp = 1.0f;
                        }
                        boolean z4 = GroupCallActivity.this.renderersContainer.inFullscreenMode && (AndroidUtilities.isTablet() || GroupCallActivity.isLandscapeMode == GroupCallActivity.this.isRtmpLandscapeMode());
                        GroupCallActivity groupCallActivity = GroupCallActivity.this;
                        ImageView imageView = z4 ? groupCallActivity.minimizeButton : groupCallActivity.expandButton;
                        ImageView imageView2 = z4 ? GroupCallActivity.this.expandButton : GroupCallActivity.this.minimizeButton;
                        imageView.setAlpha(1.0f);
                        imageView.setScaleX(dp);
                        imageView.setScaleY(dp);
                        imageView2.setAlpha(0.0f);
                        GroupCallActivity.this.muteLabel[0].setAlpha(1.0f);
                        GroupCallActivity.this.muteLabel[1].setAlpha(1.0f);
                        if (GroupCallActivity.this.renderersContainer.inFullscreenMode || (GroupCallActivity.isLandscapeMode && !AndroidUtilities.isTablet())) {
                            GroupCallActivity.this.muteLabel[0].setScaleX(0.687f);
                            GroupCallActivity.this.muteLabel[1].setScaleY(0.687f);
                        } else {
                            GroupCallActivity.this.muteLabel[0].setScaleX(1.0f);
                            GroupCallActivity.this.muteLabel[1].setScaleY(1.0f);
                        }
                        GroupCallActivity.this.invalidateLayoutFullscreen();
                        GroupCallActivity.this.layoutManager.setSpanCount(GroupCallActivity.isLandscapeMode ? 6 : 2);
                        GroupCallActivity.this.listView.invalidateItemDecorations();
                        GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                        this.updateRenderers = true;
                        if (GroupCallActivity.this.scheduleInfoTextView != null) {
                            GroupCallActivity.this.scheduleInfoTextView.setVisibility(!GroupCallActivity.isLandscapeMode ? 0 : 8);
                        }
                        if (GroupCallActivity.this.isRtmpLandscapeMode() == z2 && GroupCallActivity.this.isRtmpStream() && !GroupCallActivity.this.renderersContainer.inFullscreenMode && !GroupCallActivity.this.call.visibleVideoParticipants.isEmpty()) {
                            GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                            groupCallActivity2.fullscreenFor(groupCallActivity2.call.visibleVideoParticipants.get(0));
                            GroupCallActivity.this.renderersContainer.delayHideUi();
                        }
                    }
                    if (GroupCallActivity.isTabletMode != z3) {
                        GroupCallActivity.isTabletMode = z3;
                        GroupCallActivity.this.tabletVideoGridView.setVisibility(z3 ? 0 : 8);
                        GroupCallActivity.this.listView.invalidateItemDecorations();
                        GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                        this.updateRenderers = true;
                    }
                    if (this.updateRenderers) {
                        GroupCallActivity.this.applyCallParticipantUpdates(true);
                        GroupCallActivity.this.listAdapter.notifyDataSetChanged();
                        GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                        groupCallActivity3.fullscreenAdapter.update(false, groupCallActivity3.tabletVideoGridView);
                        if (GroupCallActivity.isTabletMode) {
                            GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                            groupCallActivity4.tabletGridAdapter.update(false, groupCallActivity4.tabletVideoGridView);
                        }
                        GroupCallActivity.this.tabletVideoGridView.setVisibility(GroupCallActivity.isTabletMode ? 0 : 8);
                        GroupCallActivity groupCallActivity5 = GroupCallActivity.this;
                        groupCallActivity5.tabletGridAdapter.setVisibility(groupCallActivity5.tabletVideoGridView, GroupCallActivity.isTabletMode && !groupCallActivity5.renderersContainer.inFullscreenMode, true);
                        GroupCallActivity groupCallActivity6 = GroupCallActivity.this;
                        groupCallActivity6.listViewVideoVisibility = !GroupCallActivity.isTabletMode || groupCallActivity6.renderersContainer.inFullscreenMode;
                        boolean z5 = !GroupCallActivity.isTabletMode && GroupCallActivity.this.renderersContainer.inFullscreenMode;
                        GroupCallActivity groupCallActivity7 = GroupCallActivity.this;
                        groupCallActivity7.fullscreenAdapter.setVisibility(groupCallActivity7.fullscreenUsersListView, z5);
                        GroupCallActivity.this.fullscreenUsersListView.setVisibility(z5 ? 0 : 8);
                        GroupCallActivity.this.listView.setVisibility((GroupCallActivity.isTabletMode || !GroupCallActivity.this.renderersContainer.inFullscreenMode) ? 0 : 8);
                        GroupCallActivity.this.layoutManager.setSpanCount(GroupCallActivity.isLandscapeMode ? 6 : 2);
                        GroupCallActivity.this.updateState(false, false);
                        GroupCallActivity.this.listView.invalidateItemDecorations();
                        GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                        AndroidUtilities.updateVisibleRows(GroupCallActivity.this.listView);
                        this.updateRenderers = false;
                        GroupCallActivity.this.attachedRenderersTmp.clear();
                        GroupCallActivity.this.attachedRenderersTmp.addAll(GroupCallActivity.this.attachedRenderers);
                        GroupCallActivity.this.renderersContainer.setIsTablet(GroupCallActivity.isTabletMode);
                        for (int i14 = 0; i14 < GroupCallActivity.this.attachedRenderersTmp.size(); i14++) {
                            ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderersTmp.get(i14)).updateAttachState(true);
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 21) {
                        setPadding(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0);
                    }
                    int paddingTop = (size - getPaddingTop()) - AndroidUtilities.dp(GroupCallActivity.this.isRtmpStream() ? 72.0f : 245.0f);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) GroupCallActivity.this.renderersContainer.getLayoutParams();
                    if (GroupCallActivity.isTabletMode) {
                        layoutParams.topMargin = ActionBar.getCurrentActionBarHeight();
                    } else {
                        layoutParams.topMargin = 0;
                    }
                    for (int i15 = 0; i15 < 2; i15++) {
                        ((FrameLayout.LayoutParams) GroupCallActivity.this.undoView[i15].getLayoutParams()).rightMargin = GroupCallActivity.isTabletMode ? AndroidUtilities.dp(328.0f) : AndroidUtilities.dp(8.0f);
                    }
                    RecyclerListView recyclerListView = GroupCallActivity.this.tabletVideoGridView;
                    if (recyclerListView != null) {
                        ((FrameLayout.LayoutParams) recyclerListView.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                    }
                    int dp2 = AndroidUtilities.dp(GroupCallActivity.this.isRtmpStream() ? 100.0f : 150.0f);
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) GroupCallActivity.this.listView.getLayoutParams();
                    if (GroupCallActivity.isTabletMode) {
                        layoutParams2.gravity = GroupCallActivity.this.hasVideo ? 5 : 1;
                        layoutParams2.width = AndroidUtilities.dp(320.0f);
                        int dp3 = AndroidUtilities.dp(4.0f);
                        layoutParams2.leftMargin = dp3;
                        layoutParams2.rightMargin = dp3;
                        layoutParams2.bottomMargin = dp2;
                        layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight();
                        i12 = AndroidUtilities.dp(60.0f);
                    } else {
                        boolean z6 = GroupCallActivity.isLandscapeMode;
                        layoutParams2.gravity = 51;
                        layoutParams2.width = -1;
                        if (z6) {
                            layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight();
                            layoutParams2.bottomMargin = AndroidUtilities.dp(14.0f);
                            layoutParams2.rightMargin = AndroidUtilities.dp(90.0f);
                            layoutParams2.leftMargin = AndroidUtilities.dp(14.0f);
                            i12 = 0;
                        } else {
                            int dp4 = AndroidUtilities.dp(60.0f);
                            layoutParams2.bottomMargin = dp2;
                            layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight() + AndroidUtilities.dp(14.0f);
                            int dp5 = AndroidUtilities.dp(14.0f);
                            layoutParams2.leftMargin = dp5;
                            layoutParams2.rightMargin = dp5;
                            i12 = dp4;
                        }
                    }
                    int i16 = 81;
                    if (!GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) {
                        GroupCallActivity.this.buttonsBackgroundGradientView.setVisibility(0);
                        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsBackgroundGradientView.getLayoutParams();
                        layoutParams3.bottomMargin = dp2;
                        if (GroupCallActivity.isTabletMode) {
                            layoutParams3.gravity = GroupCallActivity.this.hasVideo ? 85 : 81;
                            layoutParams3.width = AndroidUtilities.dp(328.0f);
                        } else {
                            layoutParams3.width = -1;
                        }
                        GroupCallActivity.this.buttonsBackgroundGradientView2.setVisibility(0);
                        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsBackgroundGradientView2.getLayoutParams();
                        layoutParams4.height = dp2;
                        if (GroupCallActivity.isTabletMode) {
                            layoutParams4.gravity = GroupCallActivity.this.hasVideo ? 85 : 81;
                            layoutParams4.width = AndroidUtilities.dp(328.0f);
                        } else {
                            layoutParams4.width = -1;
                        }
                    } else {
                        GroupCallActivity.this.buttonsBackgroundGradientView.setVisibility(8);
                        GroupCallActivity.this.buttonsBackgroundGradientView2.setVisibility(8);
                    }
                    if (GroupCallActivity.isLandscapeMode) {
                        GroupCallActivity.this.fullscreenUsersListView.setPadding(0, AndroidUtilities.dp(9.0f), 0, AndroidUtilities.dp(9.0f));
                    } else {
                        GroupCallActivity.this.fullscreenUsersListView.setPadding(AndroidUtilities.dp(9.0f), 0, AndroidUtilities.dp(9.0f), 0);
                    }
                    FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsContainer.getLayoutParams();
                    if (GroupCallActivity.isTabletMode) {
                        layoutParams5.width = AndroidUtilities.dp(320.0f);
                        layoutParams5.height = AndroidUtilities.dp(200.0f);
                        if (GroupCallActivity.this.hasVideo) {
                            i16 = 85;
                        }
                    } else {
                        if (GroupCallActivity.isLandscapeMode) {
                            layoutParams5.width = AndroidUtilities.dp(90.0f);
                            layoutParams5.height = -1;
                            layoutParams5.gravity = 53;
                            if (GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) {
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = 0;
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = 0;
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = 0;
                            } else {
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                            }
                            FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) GroupCallActivity.this.fullscreenUsersListView.getLayoutParams();
                            if (GroupCallActivity.isLandscapeMode) {
                                if (((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).getOrientation() != 0) {
                                    ((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).setOrientation(0);
                                }
                                layoutParams6.height = AndroidUtilities.dp(80.0f);
                                layoutParams6.width = -1;
                                layoutParams6.gravity = 80;
                                layoutParams6.rightMargin = 0;
                                layoutParams6.bottomMargin = AndroidUtilities.dp(100.0f);
                            } else {
                                if (((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).getOrientation() != 1) {
                                    ((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).setOrientation(1);
                                }
                                layoutParams6.height = -1;
                                layoutParams6.width = AndroidUtilities.dp(80.0f);
                                layoutParams6.gravity = 53;
                                layoutParams6.rightMargin = AndroidUtilities.dp(100.0f);
                                layoutParams6.bottomMargin = 0;
                            }
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                            max = !GroupCallActivity.isTabletMode ? 0 : Math.max(0, (paddingTop - Math.max(AndroidUtilities.dp(259.0f), (paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f));
                            if (GroupCallActivity.this.listView.getPaddingTop() == max || GroupCallActivity.this.listView.getPaddingBottom() != i12) {
                                GroupCallActivity.this.listView.setPadding(0, max, 0, i12);
                            }
                            if (GroupCallActivity.this.watchersView != null) {
                                FrameLayout.LayoutParams layoutParams7 = (FrameLayout.LayoutParams) GroupCallActivity.this.watchersView.getLayoutParams();
                                GroupCallGridCell findGroupCallGridCell = GroupCallActivity.this.findGroupCallGridCell();
                                if (findGroupCallGridCell != null) {
                                    int top = (GroupCallActivity.this.buttonsContainer.getTop() + (GroupCallActivity.this.buttonsContainer.getMeasuredHeight() / 2)) - (GroupCallActivity.this.leaveButton.getMeasuredHeight() / 2);
                                    int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + max + findGroupCallGridCell.getMeasuredHeight();
                                    layoutParams7.topMargin = (currentActionBarHeight + ((top - currentActionBarHeight) / 2)) - AndroidUtilities.dp(32.0f);
                                    layoutParams7.height = AndroidUtilities.dp(70.0f);
                                }
                            }
                            if (GroupCallActivity.this.textureLightningView != null) {
                                FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) GroupCallActivity.this.textureLightningView.getLayoutParams();
                                GroupCallGridCell findGroupCallGridCell2 = GroupCallActivity.this.findGroupCallGridCell();
                                if (findGroupCallGridCell2 != null) {
                                    layoutParams8.height = findGroupCallGridCell2.getMeasuredHeight() - AndroidUtilities.dp(14.0f);
                                    layoutParams8.width = findGroupCallGridCell2.getMeasuredWidth() - AndroidUtilities.dp(7.0f);
                                    int dp6 = AndroidUtilities.dp(16.0f);
                                    layoutParams8.leftMargin = dp6;
                                    layoutParams8.rightMargin = dp6;
                                }
                            }
                            if (GroupCallActivity.this.scheduleStartAtTextView != null) {
                                int dp7 = max + (((paddingTop - max) + AndroidUtilities.dp(60.0f)) / 2);
                                FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleStartInTextView.getLayoutParams();
                                layoutParams9.topMargin = dp7 - AndroidUtilities.dp(30.0f);
                                FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleStartAtTextView.getLayoutParams();
                                layoutParams10.topMargin = AndroidUtilities.dp(80.0f) + dp7;
                                FrameLayout.LayoutParams layoutParams11 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleTimeTextView.getLayoutParams();
                                if (layoutParams9.topMargin < ActionBar.getCurrentActionBarHeight() || layoutParams10.topMargin + AndroidUtilities.dp(20.0f) > size - AndroidUtilities.dp(231.0f)) {
                                    GroupCallActivity.this.scheduleStartInTextView.setVisibility(4);
                                    GroupCallActivity.this.scheduleStartAtTextView.setVisibility(4);
                                    dp7 -= AndroidUtilities.dp(20.0f);
                                } else {
                                    GroupCallActivity.this.scheduleStartInTextView.setVisibility(0);
                                    GroupCallActivity.this.scheduleStartAtTextView.setVisibility(0);
                                }
                                layoutParams11.topMargin = dp7;
                            }
                            for (i13 = 0; i13 < GroupCallActivity.this.attachedRenderers.size(); i13++) {
                                ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderers.get(i13)).setFullscreenMode(GroupCallActivity.this.renderersContainer.inFullscreenMode, true);
                            }
                            this.ignoreLayout = false;
                            super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                            measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
                            if (measuredHeight != this.lastSize) {
                                this.lastSize = measuredHeight;
                                GroupCallActivity.this.dismissAvatarPreview(false);
                            }
                            GroupCallActivity.this.cellFlickerDrawable.setParentWidth(getMeasuredWidth());
                        }
                        layoutParams5.width = -1;
                        layoutParams5.height = AndroidUtilities.dp(200.0f);
                    }
                    layoutParams5.gravity = i16;
                    layoutParams5.rightMargin = 0;
                    if (GroupCallActivity.isLandscapeMode) {
                    }
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = 0;
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = 0;
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = 0;
                    FrameLayout.LayoutParams layoutParams62 = (FrameLayout.LayoutParams) GroupCallActivity.this.fullscreenUsersListView.getLayoutParams();
                    if (GroupCallActivity.isLandscapeMode) {
                    }
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                    if (!GroupCallActivity.isTabletMode) {
                    }
                    if (GroupCallActivity.this.listView.getPaddingTop() == max) {
                    }
                    GroupCallActivity.this.listView.setPadding(0, max, 0, i12);
                    if (GroupCallActivity.this.watchersView != null) {
                    }
                    if (GroupCallActivity.this.textureLightningView != null) {
                    }
                    if (GroupCallActivity.this.scheduleStartAtTextView != null) {
                    }
                    while (i13 < GroupCallActivity.this.attachedRenderers.size()) {
                    }
                    this.ignoreLayout = false;
                    super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                    measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
                    if (measuredHeight != this.lastSize) {
                    }
                    GroupCallActivity.this.cellFlickerDrawable.setParentWidth(getMeasuredWidth());
                }

                @Override // android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    return !GroupCallActivity.this.isDismissed() && super.onTouchEvent(motionEvent);
                }

                @Override // android.view.View, android.view.ViewParent
                public void requestLayout() {
                    if (this.ignoreLayout) {
                        return;
                    }
                    super.requestLayout();
                }
            };
            this.containerView = frameLayout;
            frameLayout.setFocusable(true);
            this.containerView.setFocusableInTouchMode(true);
            this.containerView.setWillNotDraw(false);
            ViewGroup viewGroup = this.containerView;
            int i10 = this.backgroundPaddingLeft;
            viewGroup.setPadding(i10, 0, i10, 0);
            this.containerView.setKeepScreenOn(true);
            this.containerView.setClipChildren(false);
            if (inputPeer != null) {
                SimpleTextView simpleTextView = new SimpleTextView(context);
                this.scheduleStartInTextView = simpleTextView;
                simpleTextView.setGravity(17);
                this.scheduleStartInTextView.setTextColor(-1);
                this.scheduleStartInTextView.setTypeface(AndroidUtilities.bold());
                this.scheduleStartInTextView.setTextSize(18);
                this.scheduleStartInTextView.setText(LocaleController.getString(R.string.VoipChatStartsIn));
                this.containerView.addView(this.scheduleStartInTextView, LayoutHelper.createFrame(-2, -2.0f, 49, 21.0f, 0.0f, 21.0f, 311.0f));
                SimpleTextView simpleTextView2 = new SimpleTextView(context) { // from class: org.telegram.ui.GroupCallActivity.9
                    private float duration;
                    private float gradientWidth;
                    private int lastTextWidth;
                    private long lastUpdateTime;
                    private LinearGradient linearGradient;
                    private float startX;
                    private float time;
                    private Matrix matrix = new Matrix();
                    private float targetX = -1.0f;

                    private void setTarget() {
                        this.targetX = ((Utilities.random.nextInt(100) - 50) * 0.2f) / 50.0f;
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.ActionBar.SimpleTextView
                    public boolean createLayout(int i11) {
                        boolean createLayout = super.createLayout(i11);
                        int textWidth = getTextWidth();
                        if (textWidth != this.lastTextWidth) {
                            float f = textWidth;
                            this.gradientWidth = 1.3f * f;
                            float textHeight = getTextHeight();
                            float f2 = f * 2.0f;
                            int color = Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient);
                            int color2 = Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient3);
                            int i12 = Theme.key_voipgroup_mutedByAdminGradient2;
                            this.linearGradient = new LinearGradient(0.0f, textHeight, f2, 0.0f, new int[]{color, color2, Theme.getColor(i12), Theme.getColor(i12)}, new float[]{0.0f, 0.38f, 0.76f, 1.0f}, Shader.TileMode.CLAMP);
                            getPaint().setShader(this.linearGradient);
                            this.lastTextWidth = textWidth;
                        }
                        return createLayout;
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* JADX WARN: Removed duplicated region for block: B:14:0x0065  */
                    /* JADX WARN: Removed duplicated region for block: B:21:0x00ba  */
                    /* JADX WARN: Removed duplicated region for block: B:25:0x008c  */
                    @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public void onDraw(Canvas canvas) {
                        float f;
                        long j2;
                        float f2;
                        float f3;
                        float f4;
                        if (this.linearGradient != null) {
                            ChatObject.Call call2 = GroupCallActivity.this.call;
                            if (call2 != null && call2.isScheduled()) {
                                long currentTimeMillis = (r0.call.call.schedule_date * 1000) - GroupCallActivity.this.accountInstance.getConnectionsManager().getCurrentTimeMillis();
                                f = 1.0f;
                                if (currentTimeMillis >= 0) {
                                    if (currentTimeMillis < 5000) {
                                        f = 1.0f - (((float) currentTimeMillis) / 5000.0f);
                                    }
                                }
                                this.matrix.reset();
                                this.matrix.postTranslate((-this.lastTextWidth) * 0.7f * f, 0.0f);
                                long elapsedRealtime = SystemClock.elapsedRealtime();
                                j2 = elapsedRealtime - this.lastUpdateTime;
                                if (j2 > 20) {
                                    j2 = 17;
                                }
                                this.lastUpdateTime = elapsedRealtime;
                                f2 = this.duration;
                                if (f2 != 0.0f || this.time >= f2) {
                                    this.duration = Utilities.random.nextInt(NotificationCenter.storyQualityUpdate) + 1500;
                                    this.time = 0.0f;
                                    if (this.targetX == -1.0f) {
                                        setTarget();
                                    }
                                    this.startX = this.targetX;
                                    setTarget();
                                }
                                float f5 = (float) j2;
                                f3 = this.time + ((BlobDrawable.GRADIENT_SPEED_MIN + 0.5f) * f5) + (f5 * BlobDrawable.GRADIENT_SPEED_MAX * 2.0f * GroupCallActivity.this.amplitude);
                                this.time = f3;
                                f4 = this.duration;
                                if (f3 > f4) {
                                    this.time = f4;
                                }
                                float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.time / f4);
                                float f6 = this.gradientWidth;
                                float f7 = this.startX;
                                this.matrix.postTranslate(((f7 + ((this.targetX - f7) * interpolation)) * f6) - (f6 / 2.0f), 0.0f);
                                this.linearGradient.setLocalMatrix(this.matrix);
                                invalidate();
                            }
                            f = 0.0f;
                            this.matrix.reset();
                            this.matrix.postTranslate((-this.lastTextWidth) * 0.7f * f, 0.0f);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            j2 = elapsedRealtime2 - this.lastUpdateTime;
                            if (j2 > 20) {
                            }
                            this.lastUpdateTime = elapsedRealtime2;
                            f2 = this.duration;
                            if (f2 != 0.0f) {
                            }
                            this.duration = Utilities.random.nextInt(NotificationCenter.storyQualityUpdate) + 1500;
                            this.time = 0.0f;
                            if (this.targetX == -1.0f) {
                            }
                            this.startX = this.targetX;
                            setTarget();
                            float f52 = (float) j2;
                            f3 = this.time + ((BlobDrawable.GRADIENT_SPEED_MIN + 0.5f) * f52) + (f52 * BlobDrawable.GRADIENT_SPEED_MAX * 2.0f * GroupCallActivity.this.amplitude);
                            this.time = f3;
                            f4 = this.duration;
                            if (f3 > f4) {
                            }
                            float interpolation2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.time / f4);
                            float f62 = this.gradientWidth;
                            float f72 = this.startX;
                            this.matrix.postTranslate(((f72 + ((this.targetX - f72) * interpolation2)) * f62) - (f62 / 2.0f), 0.0f);
                            this.linearGradient.setLocalMatrix(this.matrix);
                            invalidate();
                        }
                        super.onDraw(canvas);
                    }
                };
                this.scheduleTimeTextView = simpleTextView2;
                simpleTextView2.setGravity(17);
                this.scheduleTimeTextView.setTextColor(-1);
                this.scheduleTimeTextView.setTypeface(AndroidUtilities.bold());
                this.scheduleTimeTextView.setTextSize(60);
                this.containerView.addView(this.scheduleTimeTextView, LayoutHelper.createFrame(-2, -2.0f, 49, 21.0f, 0.0f, 21.0f, 231.0f));
                SimpleTextView simpleTextView3 = new SimpleTextView(context);
                this.scheduleStartAtTextView = simpleTextView3;
                simpleTextView3.setGravity(17);
                this.scheduleStartAtTextView.setTextColor(-1);
                this.scheduleStartAtTextView.setTypeface(AndroidUtilities.bold());
                this.scheduleStartAtTextView.setTextSize(18);
                this.containerView.addView(this.scheduleStartAtTextView, LayoutHelper.createFrame(-2, -2.0f, 49, 21.0f, 0.0f, 21.0f, 201.0f));
            }
            if (isRtmpStream()) {
                LightningView lightningView = new LightningView(context);
                this.textureLightningView = lightningView;
                this.containerView.addView(lightningView, LayoutHelper.createFrame(-1, 80.0f, 51, 0.0f, 44.0f, 0.0f, 0.0f));
            }
            RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.GroupCallActivity.10
                private final LongSparseIntArray visiblePeerTmp = new LongSparseIntArray();

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Removed duplicated region for block: B:29:0x00ae  */
                /* JADX WARN: Removed duplicated region for block: B:35:0x00d4  */
                @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void dispatchDraw(Canvas canvas) {
                    float f;
                    int i11;
                    int i12;
                    boolean z2 = GroupCallActivity.this.itemAnimator.outMinTop != Float.MAX_VALUE;
                    this.visiblePeerTmp.clear();
                    for (int i13 = 0; i13 < GroupCallActivity.this.visiblePeerIds.size(); i13++) {
                        this.visiblePeerTmp.put(GroupCallActivity.this.visiblePeerIds.keyAt(i13), 1);
                    }
                    GroupCallActivity.this.visiblePeerIds.clear();
                    int i14 = 0;
                    boolean z3 = false;
                    float f2 = Float.MAX_VALUE;
                    float f3 = 0.0f;
                    for (int childCount = getChildCount(); i14 < childCount; childCount = i11) {
                        View childAt = getChildAt(i14);
                        RecyclerView.ViewHolder findContainingViewHolder = findContainingViewHolder(childAt);
                        if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() == 3 || findContainingViewHolder.getItemViewType() == 4 || findContainingViewHolder.getItemViewType() == 5 || findContainingViewHolder.getItemViewType() == 6) {
                            i11 = childCount;
                            i12 = i14;
                        } else {
                            if (findContainingViewHolder.getItemViewType() == 1) {
                                View view = findContainingViewHolder.itemView;
                                if (view instanceof GroupCallUserCell) {
                                    GroupCallUserCell groupCallUserCell = (GroupCallUserCell) view;
                                    i11 = childCount;
                                    GroupCallActivity.this.visiblePeerIds.append(groupCallUserCell.getPeerId(), 1);
                                    i12 = i14;
                                    if (this.visiblePeerTmp.get(groupCallUserCell.getPeerId(), 0) == 0) {
                                        z3 = true;
                                    } else {
                                        this.visiblePeerTmp.delete(groupCallUserCell.getPeerId());
                                    }
                                    if (z2) {
                                        f3 = Math.max(f3, childAt.getY() + childAt.getMeasuredHeight());
                                        f2 = Math.min(f2, Math.max(0.0f, childAt.getY()));
                                        i14 = i12 + 1;
                                    } else if (!GroupCallActivity.this.itemAnimator.removingHolders.contains(findContainingViewHolder)) {
                                        f2 = Math.min(f2, Math.max(0, childAt.getTop()));
                                        f3 = Math.max(f3, childAt.getBottom());
                                    }
                                }
                            }
                            i11 = childCount;
                            i12 = i14;
                            if (z2) {
                            }
                        }
                        i14 = i12 + 1;
                    }
                    if (this.visiblePeerTmp.size() > 0) {
                        z3 = true;
                    }
                    if (z3) {
                        GroupCallActivity.this.updateSubtitle();
                    }
                    if (z2) {
                        f = (GroupCallActivity.this.itemAnimator.outMinTop * (1.0f - GroupCallActivity.this.itemAnimator.animationProgress)) + (GroupCallActivity.this.itemAnimator.animationProgress * f2);
                        f3 = (f3 * GroupCallActivity.this.itemAnimator.animationProgress) + (GroupCallActivity.this.itemAnimator.outMaxBottom * (1.0f - GroupCallActivity.this.itemAnimator.animationProgress));
                    } else {
                        f = f2;
                    }
                    if (f2 != Float.MAX_VALUE) {
                        GroupCallActivity.this.rect.set((getMeasuredWidth() - (AndroidUtilities.isTablet() ? Math.min(AndroidUtilities.dp(420.0f), getMeasuredWidth()) : getMeasuredWidth())) >> 1, f, getMeasuredWidth() - r4, Math.min(getMeasuredHeight() - getTranslationY(), f3));
                        canvas.drawRoundRect(GroupCallActivity.this.rect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), GroupCallActivity.this.listViewBackgroundPaint);
                    }
                    canvas.save();
                    canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    super.dispatchDraw(canvas);
                    canvas.restore();
                }

                @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view, long j2) {
                    if (view == GroupCallActivity.this.scrimView) {
                        return false;
                    }
                    return super.drawChild(canvas, view, j2);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                public void onLayout(boolean z2, int i11, int i12, int i13, int i14) {
                    super.onLayout(z2, i11, i12, i13, i14);
                    GroupCallActivity.this.itemAnimator.updateBackgroundBeforeAnimation();
                }

                @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                public void setVisibility(int i11) {
                    if (getVisibility() != i11) {
                        for (int i12 = 0; i12 < getChildCount(); i12++) {
                            View childAt = getChildAt(i12);
                            if (childAt instanceof GroupCallGridCell) {
                                GroupCallActivity.this.attachRenderer((GroupCallGridCell) childAt, i11 == 0);
                            }
                        }
                    }
                    super.setVisibility(i11);
                }
            };
            this.listView = recyclerListView;
            recyclerListView.setClipToPadding(false);
            this.listView.setClipChildren(false);
            GroupCallItemAnimator groupCallItemAnimator = new GroupCallItemAnimator();
            this.itemAnimator = groupCallItemAnimator;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            groupCallItemAnimator.setTranslationInterpolator(cubicBezierInterpolator);
            this.itemAnimator.setRemoveDuration(350L);
            this.itemAnimator.setAddDuration(350L);
            this.itemAnimator.setMoveDuration(350L);
            this.itemAnimator.setDelayAnimations(false);
            this.listView.setItemAnimator(this.itemAnimator);
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.11
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i11) {
                    if (i11 != 0) {
                        if (GroupCallActivity.this.recordHintView != null) {
                            GroupCallActivity.this.recordHintView.hide();
                        }
                        if (GroupCallActivity.this.reminderHintView != null) {
                            GroupCallActivity.this.reminderHintView.hide();
                            return;
                        }
                        return;
                    }
                    if ((GroupCallActivity.this.scrollOffsetY - AndroidUtilities.dp(74.0f)) + ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || !GroupCallActivity.this.listView.canScrollVertically(1)) {
                        return;
                    }
                    GroupCallActivity.this.listView.getChildAt(0);
                    RecyclerListView.Holder holder = (RecyclerListView.Holder) GroupCallActivity.this.listView.findViewHolderForAdapterPosition(0);
                    if (holder == null || holder.itemView.getTop() <= 0) {
                        return;
                    }
                    GroupCallActivity.this.listView.smoothScrollBy(0, holder.itemView.getTop());
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i11, int i12) {
                    GroupCallActivity groupCallActivity;
                    ChatObject.Call call2;
                    if (GroupCallActivity.this.listView.getChildCount() <= 0 || (call2 = (groupCallActivity = GroupCallActivity.this).call) == null) {
                        return;
                    }
                    if (!call2.loadingMembers && !call2.membersLoadEndReached && groupCallActivity.layoutManager.findLastVisibleItemPosition() > GroupCallActivity.this.listAdapter.getItemCount() - 5) {
                        GroupCallActivity.this.call.loadMembers(false);
                    }
                    GroupCallActivity.this.updateLayout(true);
                    if (GroupCallActivity.this.textureLightningView != null) {
                        GroupCallActivity.this.textureLightningView.invalidate();
                    }
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            });
            this.listView.setVerticalScrollBarEnabled(false);
            RecyclerListView recyclerListView2 = this.listView;
            FillLastGridLayoutManager fillLastGridLayoutManager = new FillLastGridLayoutManager(getContext(), !isLandscapeMode ? 6 : 2, 1, false, 0, this.listView);
            this.layoutManager = fillLastGridLayoutManager;
            recyclerListView2.setLayoutManager(fillLastGridLayoutManager);
            FillLastGridLayoutManager fillLastGridLayoutManager2 = this.layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.GroupCallActivity.12
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i11) {
                    int i12 = GroupCallActivity.isLandscapeMode ? 6 : 2;
                    if (GroupCallActivity.isTabletMode || i11 < GroupCallActivity.this.listAdapter.usersVideoGridStartRow || i11 >= GroupCallActivity.this.listAdapter.usersVideoGridEndRow) {
                        return i12;
                    }
                    int i13 = GroupCallActivity.this.listAdapter.usersVideoGridEndRow - GroupCallActivity.this.listAdapter.usersVideoGridStartRow;
                    int i14 = (i11 != GroupCallActivity.this.listAdapter.usersVideoGridEndRow - 1 || (!GroupCallActivity.isLandscapeMode && i13 % 2 == 0)) ? 1 : 2;
                    if (!GroupCallActivity.isLandscapeMode) {
                        return i14;
                    }
                    if (i13 == 1) {
                        return 6;
                    }
                    return i13 == 2 ? 3 : 2;
                }
            };
            this.spanSizeLookup = spanSizeLookup;
            fillLastGridLayoutManager2.setSpanSizeLookup(spanSizeLookup);
            this.listView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.GroupCallActivity.13
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                    if (childAdapterPosition >= 0) {
                        rect.setEmpty();
                        if (childAdapterPosition < GroupCallActivity.this.listAdapter.usersVideoGridStartRow || childAdapterPosition >= GroupCallActivity.this.listAdapter.usersVideoGridEndRow) {
                            return;
                        }
                        int i11 = childAdapterPosition - GroupCallActivity.this.listAdapter.usersVideoGridStartRow;
                        int i12 = GroupCallActivity.isLandscapeMode ? 6 : 2;
                        int i13 = i11 % i12;
                        if (i13 == 0) {
                            rect.right = AndroidUtilities.dp(2.0f);
                        } else {
                            rect.left = i13 == i12 + (-1) ? AndroidUtilities.dp(2.0f) : AndroidUtilities.dp(1.0f);
                        }
                    }
                }
            });
            this.layoutManager.setBind(false);
            this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 14.0f, 14.0f, 231.0f));
            this.listView.setAdapter(this.listAdapter);
            this.listView.setTopBottomSelectorRadius(13);
            this.listView.setSelectorDrawableColor(Theme.getColor(Theme.key_voipgroup_listSelector));
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda27
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i11) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i11);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i11, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i11, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i11, float f, float f2) {
                    GroupCallActivity.this.lambda$new$13(view, i11, f, f2);
                }
            });
            this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda28
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                public final boolean onItemClick(View view, int i11) {
                    boolean lambda$new$14;
                    lambda$new$14 = GroupCallActivity.this.lambda$new$14(view, i11);
                    return lambda$new$14;
                }
            });
            if (isRtmpStream()) {
                WatchersView watchersView = new WatchersView(getContext());
                this.watchersView = watchersView;
                this.containerView.addView(watchersView, LayoutHelper.createFrame(-1, 0.0f, 49, 0.0f, 0.0f, 0.0f, 0.0f));
            }
            RecyclerListView recyclerListView3 = new RecyclerListView(context);
            this.tabletVideoGridView = recyclerListView3;
            this.containerView.addView(recyclerListView3, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 14.0f, 324.0f, 14.0f));
            RecyclerListView recyclerListView4 = this.tabletVideoGridView;
            GroupCallTabletGridAdapter groupCallTabletGridAdapter = new GroupCallTabletGridAdapter(call, this.currentAccount, this);
            this.tabletGridAdapter = groupCallTabletGridAdapter;
            recyclerListView4.setAdapter(groupCallTabletGridAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 6, 1, false);
            this.tabletVideoGridView.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.GroupCallActivity.15
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i11) {
                    return GroupCallActivity.this.tabletGridAdapter.getSpanCount(i11);
                }
            });
            this.tabletVideoGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda29
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i11) {
                    GroupCallActivity.this.lambda$new$15(view, i11);
                }
            });
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setTranslationInterpolator(cubicBezierInterpolator);
            defaultItemAnimator.setRemoveDuration(350L);
            defaultItemAnimator.setAddDuration(350L);
            defaultItemAnimator.setMoveDuration(350L);
            this.tabletVideoGridView.setItemAnimator(new DefaultItemAnimator() { // from class: org.telegram.ui.GroupCallActivity.16
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    GroupCallActivity.this.listView.invalidate();
                    GroupCallActivity.this.renderersContainer.invalidate();
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                    GroupCallActivity.this.updateLayout(true);
                }
            });
            this.tabletVideoGridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.17
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i11, int i12) {
                    super.onScrolled(recyclerView, i11, i12);
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            });
            this.tabletGridAdapter.setVisibility(this.tabletVideoGridView, false, false);
            this.tabletVideoGridView.setVisibility(8);
            this.buttonsContainer = new 18(context);
            int color = Theme.getColor(Theme.key_voipgroup_unmuteButton2);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            this.radialMatrix = new Matrix();
            this.radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(160.0f), new int[]{Color.argb(50, red, green, blue), Color.argb(0, red, green, blue)}, (float[]) null, Shader.TileMode.CLAMP);
            Paint paint = new Paint(1);
            this.radialPaint = paint;
            paint.setShader(this.radialGradient);
            BlobDrawable blobDrawable = new BlobDrawable(9);
            this.tinyWaveDrawable = blobDrawable;
            BlobDrawable blobDrawable2 = new BlobDrawable(12);
            this.bigWaveDrawable = blobDrawable2;
            blobDrawable.minRadius = AndroidUtilities.dp(62.0f);
            blobDrawable.maxRadius = AndroidUtilities.dp(72.0f);
            blobDrawable.generateBlob();
            blobDrawable2.minRadius = AndroidUtilities.dp(65.0f);
            blobDrawable2.maxRadius = AndroidUtilities.dp(75.0f);
            blobDrawable2.generateBlob();
            Paint paint2 = blobDrawable.paint;
            int i11 = Theme.key_voipgroup_unmuteButton;
            paint2.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i11), 38));
            blobDrawable2.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i11), 76));
            VoIPToggleButton voIPToggleButton = new VoIPToggleButton(context);
            this.soundButton = voIPToggleButton;
            voIPToggleButton.setCheckable(true);
            this.soundButton.setTextSize(12);
            this.buttonsContainer.addView(this.soundButton, LayoutHelper.createFrame(68, 80.0f));
            this.soundButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda30
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$16(view);
                }
            });
            VoIPToggleButton voIPToggleButton2 = new VoIPToggleButton(context);
            this.cameraButton = voIPToggleButton2;
            voIPToggleButton2.setCheckable(true);
            this.cameraButton.setTextSize(12);
            this.cameraButton.showText(false, false);
            this.cameraButton.setCrossOffset(-AndroidUtilities.dpf2(3.5f));
            this.cameraButton.setVisibility(8);
            this.buttonsContainer.addView(this.cameraButton, LayoutHelper.createFrame(68, 80.0f));
            VoIPToggleButton voIPToggleButton3 = new VoIPToggleButton(context);
            this.flipButton = voIPToggleButton3;
            voIPToggleButton3.setCheckable(true);
            this.flipButton.setTextSize(12);
            this.flipButton.showText(false, false);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.flipButton.addView(rLottieImageView, LayoutHelper.createFrame(32, 32.0f, 0, 18.0f, 10.0f, 18.0f, 0.0f));
            int i12 = R.raw.camera_flip;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i12, "" + i12, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), true, null);
            this.flipIcon = rLottieDrawable;
            rLottieImageView.setAnimation(rLottieDrawable);
            this.flipButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$17(view);
                }
            });
            this.flipButton.setVisibility(8);
            this.buttonsContainer.addView(this.flipButton, LayoutHelper.createFrame(68, 80.0f));
            VoIPToggleButton voIPToggleButton4 = new VoIPToggleButton(context);
            this.leaveButton = voIPToggleButton4;
            voIPToggleButton4.setDrawBackground(false);
            this.leaveButton.setTextSize(12);
            this.leaveButton.setData((this.call == null && isRtmpStream()) ? R.drawable.msg_voiceclose : R.drawable.calls_decline, -1, Theme.getColor(Theme.key_voipgroup_leaveButton), 0.3f, false, LocaleController.getString(R.string.VoipGroupLeave), false, false);
            this.buttonsContainer.addView(this.leaveButton, LayoutHelper.createFrame(68, 80.0f));
            this.leaveButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$18(context, view);
                }
            });
            RLottieImageView rLottieImageView2 = new RLottieImageView(context) { // from class: org.telegram.ui.GroupCallActivity.19
                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName(Button.class.getName());
                    accessibilityNodeInfo.setEnabled(GroupCallActivity.this.muteButtonState == 0 || GroupCallActivity.this.muteButtonState == 1);
                    if (GroupCallActivity.this.muteButtonState != 1 || Build.VERSION.SDK_INT < 21) {
                        return;
                    }
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.VoipMute)));
                }

                @Override // android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (GroupCallActivity.this.isRtmpStream()) {
                        return super.onTouchEvent(motionEvent);
                    }
                    if (motionEvent.getAction() == 0 && GroupCallActivity.this.muteButtonState == 0) {
                        GroupCallActivity groupCallActivity = GroupCallActivity.this;
                        if (groupCallActivity.call != null) {
                            AndroidUtilities.runOnUIThread(groupCallActivity.pressRunnable, 300L);
                            GroupCallActivity.this.scheduled = true;
                            return super.onTouchEvent(motionEvent);
                        }
                    }
                    if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                        if (GroupCallActivity.this.scheduled) {
                            AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.pressRunnable);
                            GroupCallActivity.this.scheduled = false;
                        } else if (GroupCallActivity.this.pressed) {
                            AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.unmuteRunnable);
                            GroupCallActivity.this.updateMuteButton(0, true);
                            if (VoIPService.getSharedInstance() != null) {
                                VoIPService.getSharedInstance().setMicMute(true, true, false);
                                GroupCallActivity.this.muteButton.performHapticFeedback(3, 2);
                            }
                            GroupCallActivity.this.attachedRenderersTmp.clear();
                            GroupCallActivity.this.attachedRenderersTmp.addAll(GroupCallActivity.this.attachedRenderers);
                            for (int i13 = 0; i13 < GroupCallActivity.this.attachedRenderersTmp.size(); i13++) {
                                ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderersTmp.get(i13)).updateAttachState(true);
                            }
                            GroupCallActivity.this.pressed = false;
                            MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                            super.onTouchEvent(obtain);
                            obtain.recycle();
                            return true;
                        }
                    }
                    return super.onTouchEvent(motionEvent);
                }
            };
            this.muteButton = rLottieImageView2;
            rLottieImageView2.setAnimation(this.bigMicDrawable);
            this.muteButton.setScaleType(ImageView.ScaleType.CENTER);
            this.buttonsContainer.addView(this.muteButton, LayoutHelper.createFrame(122, 122, 49));
            this.muteButton.setOnClickListener(new 20());
            int dp = AndroidUtilities.dp(38.0f);
            ImageView imageView = new ImageView(context);
            this.expandButton = imageView;
            imageView.setScaleX(0.1f);
            this.expandButton.setScaleY(0.1f);
            this.expandButton.setAlpha(0.0f);
            this.expandButton.setImageResource(R.drawable.voice_expand);
            this.expandButton.setPadding(dp, dp, dp, dp);
            this.buttonsContainer.addView(this.expandButton, LayoutHelper.createFrame(122, 122, 49));
            ImageView imageView2 = new ImageView(context);
            this.minimizeButton = imageView2;
            imageView2.setScaleX(0.1f);
            this.minimizeButton.setScaleY(0.1f);
            this.minimizeButton.setAlpha(0.0f);
            this.minimizeButton.setImageResource(R.drawable.voice_minimize);
            this.minimizeButton.setPadding(dp, dp, dp, dp);
            this.buttonsContainer.addView(this.minimizeButton, LayoutHelper.createFrame(122, 122, 49));
            if (this.call != null && isRtmpStream() && !this.call.isScheduled()) {
                float dp2 = AndroidUtilities.dp(52.0f) / ((this.expandButton.getMeasuredWidth() == 0 ? this.expandButton.getLayoutParams().width : r1) - AndroidUtilities.dp(8.0f));
                this.expandButton.setAlpha(1.0f);
                this.expandButton.setScaleX(dp2);
                this.expandButton.setScaleY(dp2);
                this.muteButton.setAlpha(0.0f);
            }
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.radialProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(110.0f));
            this.radialProgressView.setStrokeWidth(4.0f);
            this.radialProgressView.setProgressColor(Theme.getColor(Theme.key_voipgroup_connectingProgress));
            i2 = 0;
            for (i = 2; i2 < i; i = 2) {
                this.muteLabel[i2] = new TextView(context);
                this.muteLabel[i2].setTextColor(Theme.getColor(Theme.key_voipgroup_actionBarItems));
                this.muteLabel[i2].setTextSize(1, 18.0f);
                this.muteLabel[i2].setGravity(1);
                this.buttonsContainer.addView(this.muteLabel[i2], LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 26.0f));
                i2++;
            }
            this.actionBar.setAlpha(0.0f);
            this.actionBar.getBackButton().setScaleX(0.9f);
            this.actionBar.getBackButton().setScaleY(0.9f);
            this.actionBar.getBackButton().setTranslationX(-AndroidUtilities.dp(14.0f));
            this.actionBar.getTitleTextView().setTranslationY(AndroidUtilities.dp(23.0f));
            this.actionBar.getSubtitleTextView().setTranslationY(AndroidUtilities.dp(20.0f));
            this.actionBar.getAdditionalSubtitleTextView().setTranslationY(AndroidUtilities.dp(20.0f));
            int i13 = Theme.key_voipgroup_actionBarItems;
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, Theme.getColor(i13));
            this.otherItem = actionBarMenuItem;
            actionBarMenuItem.setLongClickEnabled(false);
            this.otherItem.setIcon(R.drawable.ic_ab_other);
            this.otherItem.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
            this.otherItem.setSubMenuOpenSide(2);
            this.otherItem.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda10
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
                public final void onItemClick(int i14) {
                    GroupCallActivity.this.lambda$new$19(i14);
                }
            });
            ActionBarMenuItem actionBarMenuItem2 = this.otherItem;
            int i14 = Theme.key_voipgroup_actionBarItemsSelector;
            actionBarMenuItem2.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i14), 6));
            this.otherItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda11
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$20(view);
                }
            });
            this.otherItem.setPopupItemsColor(Theme.getColor(i13), false);
            this.otherItem.setPopupItemsColor(Theme.getColor(i13), true);
            ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, null, 0, Theme.getColor(i13));
            this.pipItem = actionBarMenuItem3;
            actionBarMenuItem3.setLongClickEnabled(false);
            this.pipItem.setIcon(R.drawable.msg_voice_pip);
            ActionBarMenuItem actionBarMenuItem4 = this.pipItem;
            int i15 = R.string.AccDescrPipMode;
            actionBarMenuItem4.setContentDescription(LocaleController.getString(i15));
            this.pipItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i14), 6));
            this.pipItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda12
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$21(view);
                }
            });
            ActionBarMenuItem actionBarMenuItem5 = new ActionBarMenuItem(context, null, 0, Theme.getColor(i13));
            this.screenShareItem = actionBarMenuItem5;
            actionBarMenuItem5.setLongClickEnabled(false);
            this.screenShareItem.setIcon(R.drawable.msg_screencast);
            this.screenShareItem.setContentDescription(LocaleController.getString(i15));
            this.screenShareItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i14), 6));
            this.screenShareItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallActivity.this.lambda$new$22(view);
                }
            });
            this.titleTextView = new 21(context, context);
            View view = new View(context) { // from class: org.telegram.ui.GroupCallActivity.22
                @Override // android.view.View
                protected void onMeasure(int i16, int i17) {
                    setMeasuredDimension(View.MeasureSpec.getSize(i16), ActionBar.getCurrentActionBarHeight());
                }
            };
            this.actionBarBackground = view;
            view.setAlpha(0.0f);
            Paint paint3 = new Paint(1);
            this.liveLabelPaint = paint3;
            paint3.setColor(-12761513);
            TextView textView = new TextView(getContext()) { // from class: org.telegram.ui.GroupCallActivity.23
                private RectF rect = new RectF();

                @Override // android.widget.TextView, android.view.View
                protected void onDraw(Canvas canvas) {
                    this.rect.set(0.0f, 0.0f, getWidth(), getHeight());
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), GroupCallActivity.this.liveLabelPaint);
                    super.onDraw(canvas);
                }
            };
            this.liveLabelTextView = textView;
            textView.setTextColor(getThemedColor(i13));
            this.liveLabelTextView.setTextSize(1, 11.0f);
            this.liveLabelTextView.setText(LocaleController.getString(R.string.VoipChannelLabelLive));
            this.liveLabelTextView.setMaxLines(1);
            this.liveLabelTextView.setGravity(17);
            this.liveLabelTextView.setTypeface(AndroidUtilities.bold());
            this.liveLabelTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(0.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(0.0f));
            this.liveLabelTextView.setTag(-1);
            if (!isRtmpStream()) {
                this.liveLabelTextView.setVisibility(8);
            }
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.titleLayout = linearLayout;
            linearLayout.setOrientation(0);
            this.titleLayout.addView(this.titleTextView, LayoutHelper.createLinear(0, -2, 1.0f));
            this.titleLayout.addView(this.liveLabelTextView, LayoutHelper.createLinear(-2, 18, 6.0f, 4.0f, 0.0f, 0.0f));
            this.containerView.addView(this.actionBarBackground, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
            this.containerView.addView(this.titleLayout, LayoutHelper.createFrame(-2, -2.0f, 51, 23.0f, 0.0f, 48.0f, 0.0f));
            this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.menuItemsContainer = linearLayout2;
            linearLayout2.setOrientation(0);
            linearLayout2.addView(this.screenShareItem, LayoutHelper.createLinear(48, 48));
            linearLayout2.addView(this.pipItem, LayoutHelper.createLinear(48, 48));
            linearLayout2.addView(this.otherItem, LayoutHelper.createLinear(48, 48));
            this.containerView.addView(linearLayout2, LayoutHelper.createFrame(-2, 48, 53));
            View view2 = new View(context);
            this.actionBarShadow = view2;
            view2.setAlpha(0.0f);
            this.actionBarShadow.setBackgroundColor(Theme.getColor(Theme.key_dialogShadowLine));
            this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
            i4 = 0;
            for (i3 = 2; i4 < i3; i3 = 2) {
                this.undoView[i4] = new UndoView(context) { // from class: org.telegram.ui.GroupCallActivity.24
                    @Override // org.telegram.ui.Components.UndoView
                    public void showWithAction(long j2, int i16, Object obj, Object obj2, Runnable runnable, Runnable runnable2) {
                        if (GroupCallActivity.this.previewDialog != null) {
                            return;
                        }
                        super.showWithAction(j2, i16, obj, obj2, runnable, runnable2);
                    }
                };
                this.undoView[i4].setAdditionalTranslationY(AndroidUtilities.dp(10.0f));
                if (Build.VERSION.SDK_INT >= 21) {
                    this.undoView[i4].setTranslationZ(AndroidUtilities.dp(5.0f));
                }
                this.containerView.addView(this.undoView[i4], LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                i4++;
            }
            AccountSelectCell accountSelectCell = new AccountSelectCell(context, true);
            this.accountSelectCell = accountSelectCell;
            accountSelectCell.setTag(R.id.width_tag, Integer.valueOf(NotificationCenter.needSetDayNightTheme));
            this.otherItem.addSubItem(8, this.accountSelectCell, -2, AndroidUtilities.dp(48.0f));
            this.otherItem.setShowSubmenuByMove(false);
            AccountSelectCell accountSelectCell2 = this.accountSelectCell;
            int i16 = Theme.key_voipgroup_listSelector;
            accountSelectCell2.setBackground(Theme.createRadSelectorDrawable(Theme.getColor(i16), 6, 6));
            this.accountGap = this.otherItem.addGap(0);
            ActionBarMenuSubItem addSubItem = this.otherItem.addSubItem(1, 0, (CharSequence) LocaleController.getString(R.string.VoipGroupAllCanSpeak), true);
            this.everyoneItem = addSubItem;
            addSubItem.updateSelectorBackground(true, false);
            ActionBarMenuSubItem addSubItem2 = this.otherItem.addSubItem(2, 0, (CharSequence) LocaleController.getString(R.string.VoipGroupOnlyAdminsCanSpeak), true);
            this.adminItem = addSubItem2;
            addSubItem2.updateSelectorBackground(false, true);
            ActionBarMenuSubItem actionBarMenuSubItem = this.everyoneItem;
            int i17 = Theme.key_voipgroup_checkMenu;
            actionBarMenuSubItem.setCheckColor(i17);
            this.everyoneItem.setColors(Theme.getColor(i17), Theme.getColor(i17));
            this.adminItem.setCheckColor(i17);
            this.adminItem.setColors(Theme.getColor(i17), Theme.getColor(i17));
            Paint paint4 = new Paint(1);
            int i18 = Theme.key_voipgroup_actionBarItems;
            paint4.setColor(Theme.getColor(i18));
            paint4.setStyle(Paint.Style.STROKE);
            paint4.setStrokeWidth(AndroidUtilities.dp(1.5f));
            paint4.setStrokeCap(Paint.Cap.ROUND);
            ActionBarMenuSubItem addSubItem3 = this.otherItem.addSubItem(10, R.drawable.msg_voice_speaker, null, LocaleController.getString(R.string.VoipGroupAudio), true, false);
            this.soundItem = addSubItem3;
            addSubItem3.setItemHeight(56);
            ActionBarMenuSubItem addSubItem4 = this.otherItem.addSubItem(11, R.drawable.msg_noise_on, null, LocaleController.getString(R.string.VoipNoiseCancellation), true, false);
            this.noiseItem = addSubItem4;
            addSubItem4.setItemHeight(56);
            View addDivider = this.otherItem.addDivider(ColorUtils.blendARGB(Theme.getColor(Theme.key_voipgroup_actionBar), -16777216, 0.3f));
            this.soundItemDivider = addDivider;
            ((ViewGroup.MarginLayoutParams) addDivider.getLayoutParams()).topMargin = 0;
            ((ViewGroup.MarginLayoutParams) this.soundItemDivider.getLayoutParams()).bottomMargin = 0;
            this.editTitleItem = this.otherItem.addSubItem(6, R.drawable.msg_edit, this.recordCallDrawable, LocaleController.getString(!ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelEditTitle : R.string.VoipGroupEditTitle), true, false);
            this.permissionItem = this.otherItem.addSubItem(7, R.drawable.msg_permissions, this.recordCallDrawable, LocaleController.getString(R.string.VoipGroupEditPermissions), false, false);
            this.inviteItem = this.otherItem.addSubItem(3, R.drawable.msg_link, LocaleController.getString(R.string.VoipGroupShareInviteLink));
            this.recordCallDrawable = new RecordCallDrawable();
            this.screenItem = this.otherItem.addSubItem(9, R.drawable.msg_screencast, LocaleController.getString(R.string.VoipChatStartScreenCapture));
            ActionBarMenuSubItem addSubItem5 = this.otherItem.addSubItem(5, 0, this.recordCallDrawable, LocaleController.getString(R.string.VoipGroupRecordCall), true, false);
            this.recordItem = addSubItem5;
            this.recordCallDrawable.setParentView(addSubItem5.getImageView());
            this.leaveItem = this.otherItem.addSubItem(4, R.drawable.msg_endcall, LocaleController.getString(!ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelEndChat : R.string.VoipGroupEndChat));
            this.otherItem.setPopupItemsSelectorColor(Theme.getColor(i16));
            this.otherItem.getPopupLayout().setFitItems(true);
            this.soundItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            this.noiseItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            ActionBarMenuSubItem actionBarMenuSubItem2 = this.leaveItem;
            int i19 = Theme.key_voipgroup_leaveCallMenu;
            actionBarMenuSubItem2.setColors(Theme.getColor(i19), Theme.getColor(i19));
            this.inviteItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            this.editTitleItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            this.permissionItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            this.recordItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            this.screenItem.setColors(Theme.getColor(i18), Theme.getColor(i18));
            if (this.call != null) {
                initCreatedGroupCall();
            }
            this.leaveBackgroundPaint.setColor(Theme.getColor(Theme.key_voipgroup_leaveButton));
            updateTitle(false);
            this.actionBar.getTitleTextView().setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    GroupCallActivity.this.lambda$new$23(view3);
                }
            });
            this.fullscreenUsersListView = new RecyclerListView(context) { // from class: org.telegram.ui.GroupCallActivity.25
                @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view3, long j2) {
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) view3;
                    if (!GroupCallActivity.this.renderersContainer.isAnimating() && !GroupCallActivity.this.fullscreenListItemAnimator.isRunning()) {
                        groupCallUserCell.setAlpha(1.0f);
                        groupCallUserCell.setTranslationX(0.0f);
                        groupCallUserCell.setTranslationY(0.0f);
                    }
                    if (groupCallUserCell.isRemoving(GroupCallActivity.this.fullscreenUsersListView) && groupCallUserCell.getRenderer() != null) {
                        return true;
                    }
                    if (groupCallUserCell.getTranslationY() == 0.0f || groupCallUserCell.getRenderer() == null || groupCallUserCell.getRenderer().primaryView == null) {
                        return super.drawChild(canvas, view3, j2);
                    }
                    float top = GroupCallActivity.this.listView.getTop() - getTop();
                    float f = GroupCallActivity.this.renderersContainer.progressToFullscreenMode;
                    canvas.save();
                    float f2 = 1.0f - f;
                    canvas.clipRect(0.0f, top * f2, getMeasuredWidth(), ((GroupCallActivity.this.listView.getMeasuredHeight() + top) * f2) + (getMeasuredHeight() * f));
                    boolean drawChild = super.drawChild(canvas, view3, j2);
                    canvas.restore();
                    return drawChild;
                }
            };
            DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.GroupCallActivity.26
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    GroupCallActivity.this.listView.invalidate();
                    GroupCallActivity.this.renderersContainer.invalidate();
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                    GroupCallActivity.this.updateLayout(true);
                }
            };
            this.fullscreenListItemAnimator = defaultItemAnimator2;
            this.fullscreenUsersListView.setClipToPadding(false);
            defaultItemAnimator2.setDelayAnimations(false);
            defaultItemAnimator2.setTranslationInterpolator(CubicBezierInterpolator.DEFAULT);
            defaultItemAnimator2.setRemoveDuration(350L);
            defaultItemAnimator2.setAddDuration(350L);
            defaultItemAnimator2.setMoveDuration(350L);
            this.fullscreenUsersListView.setItemAnimator(defaultItemAnimator2);
            this.fullscreenUsersListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.27
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i20, int i21) {
                    super.onScrolled(recyclerView, i20, i21);
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                    GroupCallActivity.this.renderersContainer.invalidate();
                }
            });
            this.fullscreenUsersListView.setClipChildren(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(0);
            this.fullscreenUsersListView.setLayoutManager(linearLayoutManager);
            RecyclerListView recyclerListView5 = this.fullscreenUsersListView;
            GroupCallFullscreenAdapter groupCallFullscreenAdapter = new GroupCallFullscreenAdapter(call, this.currentAccount, this);
            this.fullscreenAdapter = groupCallFullscreenAdapter;
            recyclerListView5.setAdapter(groupCallFullscreenAdapter);
            this.fullscreenAdapter.setVisibility(this.fullscreenUsersListView, false);
            this.fullscreenUsersListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view3, int i20) {
                    GroupCallActivity.this.lambda$new$24(view3, i20);
                }
            });
            this.fullscreenUsersListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda16
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                public final boolean onItemClick(View view3, int i20) {
                    boolean lambda$new$25;
                    lambda$new$25 = GroupCallActivity.this.lambda$new$25(view3, i20);
                    return lambda$new$25;
                }
            });
            this.fullscreenUsersListView.setVisibility(8);
            this.fullscreenUsersListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.GroupCallActivity.28
                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public void getItemOffsets(Rect rect, View view3, RecyclerView recyclerView, RecyclerView.State state) {
                    recyclerView.getChildAdapterPosition(view3);
                    if (GroupCallActivity.isLandscapeMode) {
                        rect.set(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                    } else {
                        rect.set(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    }
                }
            });
            29 r6 = new 29(context, this.listView, this.fullscreenUsersListView, this.attachedRenderers, this.call, this);
            this.renderersContainer = r6;
            r6.setClipChildren(false);
            this.fullscreenAdapter.setRenderersPool(this.attachedRenderers, this.renderersContainer);
            if (this.tabletVideoGridView != null) {
                this.tabletGridAdapter.setRenderersPool(this.attachedRenderers, this.renderersContainer);
            }
            AvatarPreviewPagerIndicator avatarPreviewPagerIndicator = new AvatarPreviewPagerIndicator(context);
            this.avatarPagerIndicator = avatarPreviewPagerIndicator;
            ProfileGalleryView profileGalleryView = new ProfileGalleryView(context, this.actionBar, this.listView, avatarPreviewPagerIndicator) { // from class: org.telegram.ui.GroupCallActivity.30
                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            };
            this.avatarsViewPager = profileGalleryView;
            profileGalleryView.setImagesLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
            profileGalleryView.setInvalidateWithParent(true);
            avatarPreviewPagerIndicator.setProfileGalleryView(profileGalleryView);
            FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.GroupCallActivity.31
                Rect rect = new Rect();
                RectF rectF = new RectF();
                Path path = new Path();

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    BackupImageView avatarImageView;
                    BackupImageView avatarImageView2;
                    if (GroupCallActivity.this.progressToAvatarPreview != 1.0f) {
                        if (GroupCallActivity.this.scrimView != null && GroupCallActivity.this.hasScrimAnchorView) {
                            canvas.save();
                            float measuredHeight = (GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2.0f) * (getMeasuredHeight() / GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight());
                            int dp3 = (int) (((1.0f - GroupCallActivity.this.progressToAvatarPreview) * measuredHeight) + (AndroidUtilities.dp(13.0f) * GroupCallActivity.this.progressToAvatarPreview));
                            int i20 = (int) (measuredHeight * (1.0f - GroupCallActivity.this.progressToAvatarPreview));
                            GroupCallActivity.this.scrimView.getAvatarWavesDrawable().draw(canvas, GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2, GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2, this);
                            GroupCallActivity.this.scrimView.getAvatarImageView().getImageReceiver().setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                            GroupCallActivity.this.scrimView.getAvatarImageView().setRoundRadius(dp3, dp3, i20, i20);
                            GroupCallActivity.this.scrimView.getAvatarImageView().getImageReceiver().draw(canvas);
                            avatarImageView = GroupCallActivity.this.scrimView.getAvatarImageView();
                            avatarImageView2 = GroupCallActivity.this.scrimView.getAvatarImageView();
                        } else if (GroupCallActivity.this.scrimFullscreenView != null && GroupCallActivity.this.scrimRenderer == null && GroupCallActivity.this.previewTextureTransitionEnabled) {
                            canvas.save();
                            float measuredHeight2 = (GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight() / 2.0f) * (getMeasuredHeight() / GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight());
                            int dp4 = (int) (((1.0f - GroupCallActivity.this.progressToAvatarPreview) * measuredHeight2) + (AndroidUtilities.dp(13.0f) * GroupCallActivity.this.progressToAvatarPreview));
                            int i21 = (int) (measuredHeight2 * (1.0f - GroupCallActivity.this.progressToAvatarPreview));
                            GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getImageReceiver().setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                            GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().setRoundRadius(dp4, dp4, i21, i21);
                            GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getImageReceiver().draw(canvas);
                            avatarImageView = GroupCallActivity.this.scrimFullscreenView.getAvatarImageView();
                            avatarImageView2 = GroupCallActivity.this.scrimFullscreenView.getAvatarImageView();
                        }
                        avatarImageView.setRoundRadius(avatarImageView2.getMeasuredHeight() / 2);
                        canvas.restore();
                    }
                    GroupCallActivity.this.avatarsViewPager.setAlpha(GroupCallActivity.this.progressToAvatarPreview);
                    this.path.reset();
                    this.rectF.set(0.0f, 0.0f, getMeasuredHeight(), getMeasuredWidth());
                    this.path.addRoundRect(this.rectF, new float[]{AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0.0f, 0.0f, 0.0f, 0.0f}, Path.Direction.CCW);
                    canvas.save();
                    canvas.clipPath(this.path);
                    View findVideoActiveView = GroupCallActivity.this.avatarsViewPager.findVideoActiveView();
                    if (findVideoActiveView != null && GroupCallActivity.this.scrimRenderer != null && GroupCallActivity.this.scrimRenderer.isAttached() && !GroupCallActivity.this.drawingForBlur) {
                        canvas.save();
                        this.rect.setEmpty();
                        GroupCallActivity.this.avatarsViewPager.getChildVisibleRect(findVideoActiveView, this.rect, null);
                        int i22 = this.rect.left;
                        if (i22 < (-GroupCallActivity.this.avatarsViewPager.getMeasuredWidth())) {
                            i22 += GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() * 2;
                        } else if (i22 > GroupCallActivity.this.avatarsViewPager.getMeasuredWidth()) {
                            i22 -= GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() * 2;
                        }
                        canvas.translate(i22, 0.0f);
                        GroupCallActivity.this.scrimRenderer.draw(canvas);
                        canvas.restore();
                    }
                    super.dispatchDraw(canvas);
                    canvas.restore();
                }

                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i20, int i21) {
                    int min = Math.min(View.MeasureSpec.getSize(i20), View.MeasureSpec.getSize(i21));
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), View.MeasureSpec.makeMeasureSpec(min + getPaddingBottom(), 1073741824));
                }
            };
            this.avatarPreviewContainer = frameLayout2;
            frameLayout2.setVisibility(8);
            profileGalleryView.setVisibility(0);
            profileGalleryView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.GroupCallActivity.32
                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrollStateChanged(int i20) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrolled(int i20, float f, int i21) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageSelected(int i20) {
                    GroupCallActivity.this.avatarsViewPager.getRealPosition(i20);
                    GroupCallActivity.this.avatarPagerIndicator.saveCurrentPageProgress();
                    GroupCallActivity.this.avatarPagerIndicator.invalidate();
                }
            });
            this.blurredView = new View(context) { // from class: org.telegram.ui.GroupCallActivity.33
                @Override // android.view.View
                public void setAlpha(float f) {
                    if (getAlpha() != f) {
                        super.setAlpha(f);
                        GroupCallActivity.this.checkContentOverlayed();
                    }
                }
            };
            this.containerView.addView(this.renderersContainer);
            this.renderersContainer.addView(this.fullscreenUsersListView, LayoutHelper.createFrame(-1, 80.0f, 80, 0.0f, 0.0f, 0.0f, 100.0f));
            this.buttonsContainer.setWillNotDraw(false);
            View view3 = new View(context);
            this.buttonsBackgroundGradientView = view3;
            int[] iArr = this.gradientColors;
            iArr[0] = this.backgroundColor;
            iArr[1] = 0;
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, this.gradientColors);
            this.buttonsBackgroundGradient = gradientDrawable;
            view3.setBackground(gradientDrawable);
            this.containerView.addView(view3, LayoutHelper.createFrame(-1, 60, 83));
            View view4 = new View(context);
            this.buttonsBackgroundGradientView2 = view4;
            view4.setBackgroundColor(this.gradientColors[0]);
            this.containerView.addView(view4, LayoutHelper.createFrame(-1, 0, 83));
            this.containerView.addView(this.buttonsContainer, LayoutHelper.createFrame(-1, NotificationCenter.storyQualityUpdate, 81));
            this.containerView.addView(this.blurredView);
            frameLayout2.addView(profileGalleryView, LayoutHelper.createFrame(-1, -1.0f));
            frameLayout2.addView(avatarPreviewPagerIndicator, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 0.0f, 0.0f, 0.0f));
            this.containerView.addView(frameLayout2, LayoutHelper.createFrame(-1, -1.0f, 0, 14.0f, 14.0f, 14.0f, 14.0f));
            applyCallParticipantUpdates(false);
            this.listAdapter.notifyDataSetChanged();
            if (isTabletMode) {
                this.tabletGridAdapter.update(false, this.tabletVideoGridView);
            }
            this.oldCount = this.listAdapter.getItemCount();
            if (inputPeer != null) {
                TextView textView2 = new TextView(context);
                this.scheduleInfoTextView = textView2;
                textView2.setGravity(17);
                this.scheduleInfoTextView.setTextColor(-8682615);
                this.scheduleInfoTextView.setTextSize(1, 14.0f);
                if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
                    this.scheduleInfoTextView.setTag(1);
                }
                this.containerView.addView(this.scheduleInfoTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 21.0f, 0.0f, 21.0f, 100.0f));
                final NumberPicker numberPicker = new NumberPicker(context);
                numberPicker.setTextColor(-1);
                numberPicker.setSelectorColor(-9598483);
                numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
                numberPicker.setItemCount(5);
                final NumberPicker numberPicker2 = new NumberPicker(context) { // from class: org.telegram.ui.GroupCallActivity.34
                    @Override // org.telegram.ui.Components.NumberPicker
                    protected CharSequence getContentDescription(int i20) {
                        return LocaleController.formatPluralString("Hours", i20, new Object[0]);
                    }
                };
                numberPicker2.setItemCount(5);
                numberPicker2.setTextColor(-1);
                numberPicker2.setSelectorColor(-9598483);
                numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
                final NumberPicker numberPicker3 = new NumberPicker(context) { // from class: org.telegram.ui.GroupCallActivity.35
                    @Override // org.telegram.ui.Components.NumberPicker
                    protected CharSequence getContentDescription(int i20) {
                        return LocaleController.formatPluralString("Minutes", i20, new Object[0]);
                    }
                };
                numberPicker3.setItemCount(5);
                numberPicker3.setTextColor(-1);
                numberPicker3.setSelectorColor(-9598483);
                numberPicker3.setTextOffset(-AndroidUtilities.dp(34.0f));
                TextView textView3 = new TextView(context);
                this.scheduleButtonTextView = textView3;
                textView3.setLines(1);
                this.scheduleButtonTextView.setSingleLine(true);
                this.scheduleButtonTextView.setEllipsize(TextUtils.TruncateAt.END);
                this.scheduleButtonTextView.setGravity(17);
                this.scheduleButtonTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), 0, 1056964608));
                this.scheduleButtonTextView.setTextColor(-1);
                this.scheduleButtonTextView.setTypeface(AndroidUtilities.bold());
                this.scheduleButtonTextView.setTextSize(1, 14.0f);
                this.containerView.addView(this.scheduleButtonTextView, LayoutHelper.createFrame(-1, 48.0f, 81, 21.0f, 0.0f, 21.0f, 20.5f));
                final TLRPC.InputPeer inputPeer2 = groupCallPeer;
                this.scheduleButtonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda18
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view5) {
                        GroupCallActivity.this.lambda$new$30(numberPicker, numberPicker2, numberPicker3, chat, accountInstance, inputPeer2, view5);
                    }
                });
                LinearLayout linearLayout3 = new LinearLayout(context) { // from class: org.telegram.ui.GroupCallActivity.37
                    boolean ignoreLayout = false;

                    @Override // android.widget.LinearLayout, android.view.View
                    protected void onMeasure(int i20, int i21) {
                        this.ignoreLayout = true;
                        numberPicker.setItemCount(5);
                        numberPicker2.setItemCount(5);
                        numberPicker3.setItemCount(5);
                        numberPicker.getLayoutParams().height = AndroidUtilities.dp(54.0f) * 5;
                        numberPicker2.getLayoutParams().height = AndroidUtilities.dp(54.0f) * 5;
                        numberPicker3.getLayoutParams().height = AndroidUtilities.dp(54.0f) * 5;
                        this.ignoreLayout = false;
                        super.onMeasure(i20, i21);
                    }

                    @Override // android.view.View, android.view.ViewParent
                    public void requestLayout() {
                        if (this.ignoreLayout) {
                            return;
                        }
                        super.requestLayout();
                    }
                };
                this.scheduleTimerContainer = linearLayout3;
                linearLayout3.setWeightSum(1.0f);
                this.scheduleTimerContainer.setOrientation(0);
                this.containerView.addView(this.scheduleTimerContainer, LayoutHelper.createFrame(-1, 270.0f, 51, 0.0f, 50.0f, 0.0f, 0.0f));
                final long currentTimeMillis = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTimeMillis);
                final int i20 = calendar.get(1);
                int i21 = calendar.get(6);
                this.scheduleTimerContainer.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.dialogsUnreadReactionsCounterChanged, 0.5f));
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(365);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda19
                    @Override // org.telegram.ui.Components.NumberPicker.Formatter
                    public final String format(int i22) {
                        String lambda$new$31;
                        lambda$new$31 = GroupCallActivity.lambda$new$31(currentTimeMillis, calendar, i20, i22);
                        return lambda$new$31;
                    }
                });
                NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda20
                    @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
                    public final void onValueChange(NumberPicker numberPicker4, int i22, int i23) {
                        GroupCallActivity.this.lambda$new$32(numberPicker, numberPicker2, numberPicker3, numberPicker4, i22, i23);
                    }
                };
                numberPicker.setOnValueChangedListener(onValueChangeListener);
                numberPicker2.setMinValue(0);
                numberPicker2.setMaxValue(23);
                this.scheduleTimerContainer.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.dialogsUnreadReactionsCounterChanged, 0.2f));
                numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda21
                    @Override // org.telegram.ui.Components.NumberPicker.Formatter
                    public final String format(int i22) {
                        String lambda$new$33;
                        lambda$new$33 = GroupCallActivity.lambda$new$33(i22);
                        return lambda$new$33;
                    }
                });
                numberPicker2.setOnValueChangedListener(onValueChangeListener);
                numberPicker3.setMinValue(0);
                numberPicker3.setMaxValue(59);
                numberPicker3.setValue(0);
                numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda22
                    @Override // org.telegram.ui.Components.NumberPicker.Formatter
                    public final String format(int i22) {
                        String lambda$new$34;
                        lambda$new$34 = GroupCallActivity.lambda$new$34(i22);
                        return lambda$new$34;
                    }
                });
                this.scheduleTimerContainer.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.dialogsUnreadReactionsCounterChanged, 0.3f));
                numberPicker3.setOnValueChangedListener(onValueChangeListener);
                calendar.setTimeInMillis(currentTimeMillis + 10800000);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                int i22 = calendar.get(6);
                int i23 = calendar.get(12);
                int i24 = calendar.get(11);
                numberPicker.setValue(i21 != i22 ? 1 : 0);
                numberPicker3.setValue(i23);
                numberPicker2.setValue(i24);
                AlertsCreator.checkScheduleDate(this.scheduleButtonTextView, this.scheduleInfoTextView, 604800L, 2, numberPicker, numberPicker2, numberPicker3);
            }
            PinchToZoomHelper pinchToZoomHelper = new PinchToZoomHelper(Build.VERSION.SDK_INT < 21 ? (ViewGroup) getWindow().getDecorView() : this.containerView, this.containerView) { // from class: org.telegram.ui.GroupCallActivity.38
                @Override // org.telegram.ui.PinchToZoomHelper
                protected void drawOverlays(Canvas canvas, float f, float f2, float f3, float f4, float f5) {
                    if (f > 0.0f) {
                        float x = GroupCallActivity.this.avatarPreviewContainer.getX() + ((BottomSheet) GroupCallActivity.this).containerView.getX();
                        float y = GroupCallActivity.this.avatarPreviewContainer.getY() + ((BottomSheet) GroupCallActivity.this).containerView.getY();
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(x, y, GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() + x, GroupCallActivity.this.avatarsViewPager.getMeasuredHeight() + y);
                        canvas.saveLayerAlpha(rectF, (int) (f * 255.0f), 31);
                        canvas.translate(x, y);
                        GroupCallActivity.this.avatarPreviewContainer.draw(canvas);
                        canvas.restore();
                    }
                }

                @Override // org.telegram.ui.PinchToZoomHelper
                protected void invalidateViews() {
                    super.invalidateViews();
                    for (int i25 = 0; i25 < GroupCallActivity.this.avatarsViewPager.getChildCount(); i25++) {
                        GroupCallActivity.this.avatarsViewPager.getChildAt(i25).invalidate();
                    }
                }
            };
            this.pinchToZoomHelper = pinchToZoomHelper;
            pinchToZoomHelper.setCallback(new PinchToZoomHelper.Callback() { // from class: org.telegram.ui.GroupCallActivity.39
                @Override // org.telegram.ui.PinchToZoomHelper.Callback
                public /* synthetic */ TextureView getCurrentTextureView() {
                    return PinchToZoomHelper.Callback.-CC.$default$getCurrentTextureView(this);
                }

                @Override // org.telegram.ui.PinchToZoomHelper.Callback
                public void onZoomFinished(MessageObject messageObject) {
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }

                @Override // org.telegram.ui.PinchToZoomHelper.Callback
                public void onZoomStarted(MessageObject messageObject) {
                    GroupCallActivity.this.listView.cancelClickRunnables(true);
                    GroupCallActivity.this.pinchToZoomHelper.getPhotoImage().setRoundRadius(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0, 0);
                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                }
            });
            profileGalleryView.setPinchToZoomHelper(this.pinchToZoomHelper);
            this.cameraButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda23
                @Override // android.view.View.OnClickListener
                public final void onClick(View view5) {
                    GroupCallActivity.this.lambda$new$35(context, view5);
                }
            });
            updateScheduleUI(false);
            updateItems();
            updateSpeakerPhoneIcon(false);
            updateState(false, false);
            setColorProgress(0.0f);
            updateSubtitle();
        }
        tL_peerUser = new TLRPC.TL_peerUser();
        this.selfPeer = tL_peerUser;
        j = this.accountInstance.getUserConfig().getClientUserId();
        tL_peerUser.user_id = j;
        VoIPService.audioLevelsCallback = new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda26
            @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
            public final void run(int[] iArr2, float[] fArr, boolean[] zArr) {
                GroupCallActivity.this.lambda$new$11(iArr2, fArr, zArr);
            }
        };
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallUpdated);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.needShowAlert);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.didLoadChatAdmins);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.applyGroupCallVisibleParticipants);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.userInfoDidLoad);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.mainUserInfoChanged);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallScreencastStateChanged);
        this.accountInstance.getNotificationCenter().addObserver(this, NotificationCenter.groupCallSpeakingUsersUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndCall);
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        int i82 = R.raw.voip_filled;
        this.bigMicDrawable = new RLottieDrawable(i82, "" + i82, AndroidUtilities.dp(72.0f), AndroidUtilities.dp(72.0f), true, null);
        int i92 = R.raw.hand_2;
        this.handDrawables = new RLottieDrawable(i92, "" + i92, AndroidUtilities.dp(72.0f), AndroidUtilities.dp(72.0f), true, null);
        FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.GroupCallActivity.8
            private int lastSize;
            boolean localHasVideo;
            private boolean updateRenderers;
            boolean wasLayout;
            private boolean ignoreLayout = false;
            private RectF rect = new RectF();
            HashMap listCells = new HashMap();

            /* JADX WARN: Removed duplicated region for block: B:111:0x0462  */
            /* JADX WARN: Removed duplicated region for block: B:114:0x0494  */
            /* JADX WARN: Removed duplicated region for block: B:118:0x0529  */
            /* JADX WARN: Removed duplicated region for block: B:121:0x0599  */
            /* JADX WARN: Removed duplicated region for block: B:125:0x05ad A[LOOP:5: B:123:0x05aa->B:125:0x05ad, LOOP_END] */
            /* JADX WARN: Removed duplicated region for block: B:129:0x05f3  */
            /* JADX WARN: Removed duplicated region for block: B:134:0x05a2  */
            /* JADX WARN: Removed duplicated region for block: B:135:0x055b  */
            /* JADX WARN: Removed duplicated region for block: B:136:0x04b7  */
            /* JADX WARN: Removed duplicated region for block: B:139:0x06ab  */
            @Override // android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void dispatchDraw(Canvas canvas) {
                float f;
                float f2;
                float f3;
                View view5;
                GroupCallUserCell groupCallUserCell;
                GroupCallUserCell groupCallUserCell2;
                float y;
                float left;
                GroupCallUserCell groupCallUserCell3;
                Path path;
                float[] fArr;
                GroupCallUserCell groupCallUserCell4;
                float f4;
                int i102;
                float left2;
                float f5;
                float f6;
                float f7;
                if (GroupCallActivity.isTabletMode) {
                    GroupCallActivity.this.buttonsContainer.setTranslationY(0.0f);
                    GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                    GroupCallActivity.this.buttonsContainer.setTranslationX(0.0f);
                    GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                } else if (GroupCallActivity.isLandscapeMode) {
                    GroupCallActivity.this.buttonsContainer.setTranslationY(0.0f);
                    GroupCallActivity.this.fullscreenUsersListView.setTranslationY(0.0f);
                    GroupCallActivity.this.buttonsContainer.setTranslationX(GroupCallActivity.this.progressToHideUi * AndroidUtilities.dp(94.0f));
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    groupCallActivity.fullscreenUsersListView.setTranslationX(groupCallActivity.progressToHideUi * AndroidUtilities.dp(94.0f));
                } else {
                    GroupCallActivity.this.buttonsContainer.setTranslationX(0.0f);
                    GroupCallActivity.this.fullscreenUsersListView.setTranslationX(0.0f);
                    GroupCallActivity.this.buttonsContainer.setTranslationY(GroupCallActivity.this.progressToHideUi * AndroidUtilities.dp(94.0f));
                    GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                    groupCallActivity2.fullscreenUsersListView.setTranslationY(groupCallActivity2.progressToHideUi * AndroidUtilities.dp(94.0f));
                }
                for (int i112 = 0; i112 < GroupCallActivity.this.listView.getChildCount(); i112++) {
                    View childAt = GroupCallActivity.this.listView.getChildAt(i112);
                    if (childAt instanceof GroupCallUserCell) {
                        ((GroupCallUserCell) childAt).setDrawAvatar(true);
                    }
                    if (!(childAt instanceof GroupCallGridCell)) {
                        if (childAt.getMeasuredWidth() != GroupCallActivity.this.listView.getMeasuredWidth()) {
                            childAt.setTranslationX((GroupCallActivity.this.listView.getMeasuredWidth() - childAt.getMeasuredWidth()) >> 1);
                        } else {
                            childAt.setTranslationX(0.0f);
                        }
                    }
                }
                if (!GroupCallActivity.this.renderersContainer.isAnimating()) {
                    for (int i122 = 0; i122 < GroupCallActivity.this.fullscreenUsersListView.getChildCount(); i122++) {
                        ((GroupCallFullscreenAdapter.GroupCallUserCell) GroupCallActivity.this.fullscreenUsersListView.getChildAt(i122)).setProgressToFullscreen(1.0f);
                    }
                } else if (GroupCallActivity.this.fullscreenUsersListView.getVisibility() == 0) {
                    this.listCells.clear();
                    for (int i132 = 0; i132 < GroupCallActivity.this.listView.getChildCount(); i132++) {
                        View childAt2 = GroupCallActivity.this.listView.getChildAt(i132);
                        if ((childAt2 instanceof GroupCallGridCell) && GroupCallActivity.this.listView.getChildAdapterPosition(childAt2) >= 0) {
                            GroupCallGridCell groupCallGridCell = (GroupCallGridCell) childAt2;
                            if (groupCallGridCell.getRenderer() != GroupCallActivity.this.renderersContainer.fullscreenTextureView) {
                                this.listCells.put(groupCallGridCell.getParticipant(), childAt2);
                            }
                        } else if ((childAt2 instanceof GroupCallUserCell) && GroupCallActivity.this.listView.getChildAdapterPosition(childAt2) >= 0) {
                            GroupCallUserCell groupCallUserCell5 = (GroupCallUserCell) childAt2;
                            this.listCells.put(groupCallUserCell5.getParticipant(), groupCallUserCell5);
                        }
                    }
                    for (int i142 = 0; i142 < GroupCallActivity.this.fullscreenUsersListView.getChildCount(); i142++) {
                        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell6 = (GroupCallFullscreenAdapter.GroupCallUserCell) GroupCallActivity.this.fullscreenUsersListView.getChildAt(i142);
                        View view22 = (View) this.listCells.get(groupCallUserCell6.getVideoParticipant());
                        if (view22 == null) {
                            view22 = (View) this.listCells.get(groupCallUserCell6.getParticipant());
                        }
                        float f8 = GroupCallActivity.this.renderersContainer.progressToFullscreenMode;
                        if (!GroupCallActivity.this.fullscreenListItemAnimator.isRunning()) {
                            groupCallUserCell6.setAlpha(1.0f);
                        }
                        if (view22 != null) {
                            if (view22 instanceof GroupCallGridCell) {
                                GroupCallGridCell groupCallGridCell2 = (GroupCallGridCell) view22;
                                left2 = (groupCallGridCell2.getLeft() + GroupCallActivity.this.listView.getX()) - GroupCallActivity.this.renderersContainer.getLeft();
                                f5 = (groupCallGridCell2.getTop() + GroupCallActivity.this.listView.getY()) - GroupCallActivity.this.renderersContainer.getTop();
                                f6 = groupCallUserCell6.getLeft() + GroupCallActivity.this.fullscreenUsersListView.getX();
                                f7 = groupCallUserCell6.getTop() + GroupCallActivity.this.fullscreenUsersListView.getY();
                            } else {
                                left2 = ((r3.getLeft() + GroupCallActivity.this.listView.getX()) - GroupCallActivity.this.renderersContainer.getLeft()) + r3.getAvatarImageView().getLeft() + (r3.getAvatarImageView().getMeasuredWidth() >> 1);
                                float top = ((r3.getTop() + GroupCallActivity.this.listView.getY()) - GroupCallActivity.this.renderersContainer.getTop()) + r3.getAvatarImageView().getTop() + (r3.getAvatarImageView().getMeasuredHeight() >> 1);
                                float left3 = groupCallUserCell6.getLeft() + GroupCallActivity.this.fullscreenUsersListView.getX() + (groupCallUserCell6.getMeasuredWidth() >> 1);
                                float top2 = groupCallUserCell6.getTop() + GroupCallActivity.this.fullscreenUsersListView.getY() + (groupCallUserCell6.getMeasuredHeight() >> 1);
                                ((GroupCallUserCell) view22).setDrawAvatar(false);
                                f5 = top;
                                f6 = left3;
                                f7 = top2;
                            }
                            float f9 = left2 - f6;
                            float f10 = 1.0f - f8;
                            groupCallUserCell6.setTranslationX(f9 * f10);
                            groupCallUserCell6.setTranslationY((f5 - f7) * f10);
                            groupCallUserCell6.setScaleX(1.0f);
                            groupCallUserCell6.setScaleY(1.0f);
                            groupCallUserCell6.setProgressToFullscreen(f8);
                        } else {
                            groupCallUserCell6.setScaleX(1.0f);
                            groupCallUserCell6.setScaleY(1.0f);
                            groupCallUserCell6.setTranslationX(0.0f);
                            groupCallUserCell6.setTranslationY(0.0f);
                            groupCallUserCell6.setProgressToFullscreen(1.0f);
                            if (groupCallUserCell6.getRenderer() == null) {
                                groupCallUserCell6.setAlpha(f8);
                            }
                        }
                    }
                }
                for (int i152 = 0; i152 < GroupCallActivity.this.attachedRenderers.size(); i152++) {
                    GroupCallMiniTextureView groupCallMiniTextureView = (GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderers.get(i152);
                    RecyclerListView recyclerListView6 = GroupCallActivity.this.listView;
                    GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                    groupCallMiniTextureView.updatePosition(recyclerListView6, groupCallActivity3.tabletVideoGridView, groupCallActivity3.fullscreenUsersListView, groupCallActivity3.renderersContainer);
                }
                if (GroupCallActivity.isTabletMode) {
                    GroupCallActivity.this.buttonsBackgroundGradientView.setAlpha(1.0f);
                    GroupCallActivity.this.buttonsBackgroundGradientView2.setAlpha(1.0f);
                } else {
                    GroupCallActivity.this.buttonsBackgroundGradientView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                    GroupCallActivity.this.buttonsBackgroundGradientView2.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                }
                if (GroupCallActivity.this.renderersContainer.swipedBack) {
                    GroupCallActivity.this.listView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                } else {
                    GroupCallActivity.this.listView.setAlpha(1.0f);
                }
                if (GroupCallActivity.this.watchersView != null) {
                    GroupCallActivity.this.watchersView.setAlpha(1.0f - GroupCallActivity.this.renderersContainer.progressToFullscreenMode);
                    GroupCallActivity.this.watchersView.setTranslationY(GroupCallActivity.this.renderersContainer.progressToFullscreenMode * AndroidUtilities.dp(64.0f));
                }
                super.dispatchDraw(canvas);
                GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                if (groupCallActivity4.drawingForBlur) {
                    return;
                }
                float f11 = 255.0f;
                if (groupCallActivity4.avatarsPreviewShowed) {
                    if (GroupCallActivity.this.scrimView != null) {
                        if (!GroupCallActivity.this.useBlur) {
                            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), GroupCallActivity.this.scrimPaint);
                        }
                        float y2 = GroupCallActivity.this.listView.getY();
                        float[] fArr2 = new float[8];
                        Path path2 = new Path();
                        int childCount = GroupCallActivity.this.listView.getChildCount();
                        float y3 = GroupCallActivity.this.listView.getY() + GroupCallActivity.this.listView.getMeasuredHeight();
                        if (GroupCallActivity.this.hasScrimAnchorView) {
                            for (int i162 = 0; i162 < childCount; i162++) {
                                if (GroupCallActivity.this.listView.getChildAt(i162) != GroupCallActivity.this.scrimView) {
                                }
                            }
                            groupCallUserCell = null;
                            groupCallUserCell2 = groupCallUserCell;
                            if (groupCallUserCell2 != null && y2 < y3) {
                                canvas.save();
                                if (GroupCallActivity.this.scrimFullscreenView == null) {
                                    canvas.clipRect(0.0f, (1.0f - GroupCallActivity.this.progressToAvatarPreview) * y2, getMeasuredWidth(), ((1.0f - GroupCallActivity.this.progressToAvatarPreview) * y3) + (getMeasuredHeight() * GroupCallActivity.this.progressToAvatarPreview));
                                }
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                    y = GroupCallActivity.this.avatarPreviewContainer.getTop() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth();
                                    left = GroupCallActivity.this.avatarPreviewContainer.getLeft();
                                } else {
                                    y = ((GroupCallActivity.this.listView.getY() + groupCallUserCell2.getY()) * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + ((GroupCallActivity.this.avatarPreviewContainer.getTop() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth()) * GroupCallActivity.this.progressToAvatarPreview);
                                    left = ((GroupCallActivity.this.listView.getLeft() + groupCallUserCell2.getX()) * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + (GroupCallActivity.this.avatarPreviewContainer.getLeft() * GroupCallActivity.this.progressToAvatarPreview);
                                }
                                float f122 = y;
                                canvas.translate(left, f122);
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                    groupCallUserCell3 = groupCallUserCell2;
                                    path = path2;
                                    fArr = fArr2;
                                    canvas.saveLayerAlpha(0.0f, 0.0f, groupCallUserCell2.getMeasuredWidth(), groupCallUserCell2.getClipHeight(), (int) (GroupCallActivity.this.progressToAvatarPreview * 255.0f), 31);
                                } else {
                                    groupCallUserCell3 = groupCallUserCell2;
                                    path = path2;
                                    fArr = fArr2;
                                    canvas.save();
                                }
                                float measuredHeight2 = (int) (groupCallUserCell3.getMeasuredHeight() + ((groupCallUserCell3.getClipHeight() - groupCallUserCell3.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - GroupCallActivity.this.progressToAvatarPreview))));
                                this.rect.set(0.0f, 0.0f, groupCallUserCell3.getMeasuredWidth(), measuredHeight2);
                                if (GroupCallActivity.this.hasScrimAnchorView) {
                                    groupCallUserCell4 = groupCallUserCell3;
                                    f4 = 1.0f;
                                } else {
                                    f4 = GroupCallActivity.this.progressToAvatarPreview;
                                    groupCallUserCell4 = groupCallUserCell3;
                                }
                                groupCallUserCell4.setProgressToAvatarPreview(f4);
                                for (i102 = 0; i102 < 4; i102++) {
                                    fArr[i102] = AndroidUtilities.dp(13.0f) * (1.0f - GroupCallActivity.this.progressToAvatarPreview);
                                    fArr[i102 + 4] = AndroidUtilities.dp(13.0f);
                                }
                                path.reset();
                                Path path32 = path;
                                path32.addRoundRect(this.rect, fArr, Path.Direction.CW);
                                path32.close();
                                canvas.drawPath(path32, GroupCallActivity.this.listViewBackgroundPaint);
                                groupCallUserCell4.draw(canvas);
                                canvas.restore();
                                canvas.restore();
                                if (GroupCallActivity.this.scrimPopupLayout != null) {
                                    float f13 = f122 + measuredHeight2;
                                    float measuredWidth = (getMeasuredWidth() - GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth()) - AndroidUtilities.dp(14.0f);
                                    if (GroupCallActivity.this.progressToAvatarPreview != 1.0f) {
                                        canvas.saveLayerAlpha(measuredWidth, f13, measuredWidth + GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth(), f13 + GroupCallActivity.this.scrimPopupLayout.getMeasuredHeight(), (int) (GroupCallActivity.this.progressToAvatarPreview * 255.0f), 31);
                                    } else {
                                        canvas.save();
                                    }
                                    GroupCallActivity.this.scrimPopupLayout.setTranslationX(measuredWidth - GroupCallActivity.this.scrimPopupLayout.getLeft());
                                    GroupCallActivity.this.scrimPopupLayout.setTranslationY(f13 - GroupCallActivity.this.scrimPopupLayout.getTop());
                                    float f14 = (GroupCallActivity.this.progressToAvatarPreview * 0.2f) + 0.8f;
                                    canvas.scale(f14, f14, (GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth() / 2.0f) + measuredWidth, f13);
                                    canvas.translate(measuredWidth, f13);
                                    GroupCallActivity.this.scrimPopupLayout.draw(canvas);
                                    canvas.restore();
                                }
                            }
                            if (!GroupCallActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                                canvas.save();
                                if (GroupCallActivity.this.hasScrimAnchorView && GroupCallActivity.this.scrimFullscreenView == null) {
                                    canvas.clipRect(0.0f, y2 * (1.0f - GroupCallActivity.this.progressToAvatarPreview), getMeasuredWidth(), (y3 * (1.0f - GroupCallActivity.this.progressToAvatarPreview)) + (getMeasuredHeight() * GroupCallActivity.this.progressToAvatarPreview));
                                }
                                canvas.scale(GroupCallActivity.this.avatarPreviewContainer.getScaleX(), GroupCallActivity.this.avatarPreviewContainer.getScaleY(), GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY());
                                canvas.translate(GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY());
                                GroupCallActivity.this.avatarPreviewContainer.draw(canvas);
                                canvas.restore();
                            }
                        }
                        groupCallUserCell = GroupCallActivity.this.scrimView;
                        groupCallUserCell2 = groupCallUserCell;
                        if (groupCallUserCell2 != null) {
                            canvas.save();
                            if (GroupCallActivity.this.scrimFullscreenView == null) {
                            }
                            if (GroupCallActivity.this.hasScrimAnchorView) {
                            }
                            float f1222 = y;
                            canvas.translate(left, f1222);
                            if (GroupCallActivity.this.hasScrimAnchorView) {
                            }
                            float measuredHeight22 = (int) (groupCallUserCell3.getMeasuredHeight() + ((groupCallUserCell3.getClipHeight() - groupCallUserCell3.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - GroupCallActivity.this.progressToAvatarPreview))));
                            this.rect.set(0.0f, 0.0f, groupCallUserCell3.getMeasuredWidth(), measuredHeight22);
                            if (GroupCallActivity.this.hasScrimAnchorView) {
                            }
                            groupCallUserCell4.setProgressToAvatarPreview(f4);
                            while (i102 < 4) {
                            }
                            path.reset();
                            Path path322 = path;
                            path322.addRoundRect(this.rect, fArr, Path.Direction.CW);
                            path322.close();
                            canvas.drawPath(path322, GroupCallActivity.this.listViewBackgroundPaint);
                            groupCallUserCell4.draw(canvas);
                            canvas.restore();
                            canvas.restore();
                            if (GroupCallActivity.this.scrimPopupLayout != null) {
                            }
                        }
                        if (!GroupCallActivity.this.pinchToZoomHelper.isInOverlayMode()) {
                        }
                    }
                    if (GroupCallActivity.this.progressToAvatarPreview == 1.0f || GroupCallActivity.this.scrimFullscreenView != null) {
                        return;
                    }
                    canvas.saveLayerAlpha((int) GroupCallActivity.this.buttonsBackgroundGradientView2.getX(), (int) GroupCallActivity.this.buttonsBackgroundGradientView.getY(), (int) (GroupCallActivity.this.buttonsBackgroundGradientView2.getX() + GroupCallActivity.this.buttonsBackgroundGradientView2.getMeasuredWidth()), getMeasuredHeight(), (int) ((1.0f - GroupCallActivity.this.progressToAvatarPreview) * 255.0f), 31);
                    canvas.save();
                    canvas.translate(GroupCallActivity.this.buttonsBackgroundGradientView2.getX(), GroupCallActivity.this.buttonsBackgroundGradientView2.getY());
                    GroupCallActivity.this.buttonsBackgroundGradientView2.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(GroupCallActivity.this.buttonsBackgroundGradientView.getX(), GroupCallActivity.this.buttonsBackgroundGradientView.getY());
                    GroupCallActivity.this.buttonsBackgroundGradientView.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(GroupCallActivity.this.buttonsContainer.getX(), GroupCallActivity.this.buttonsContainer.getY());
                    GroupCallActivity.this.buttonsContainer.draw(canvas);
                    canvas.restore();
                    for (int i172 = 0; i172 < 2; i172++) {
                        if (GroupCallActivity.this.undoView[i172].getVisibility() == 0) {
                            canvas.save();
                            canvas.translate(GroupCallActivity.this.undoView[1].getX(), GroupCallActivity.this.undoView[1].getY());
                            GroupCallActivity.this.undoView[1].draw(canvas);
                            canvas.restore();
                        }
                    }
                } else {
                    if (GroupCallActivity.this.scrimView == null) {
                        return;
                    }
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), GroupCallActivity.this.scrimPaint);
                    float y4 = GroupCallActivity.this.listView.getY();
                    GroupCallActivity.this.listView.getY();
                    GroupCallActivity.this.listView.getMeasuredHeight();
                    if (GroupCallActivity.this.hasScrimAnchorView) {
                        int childCount2 = GroupCallActivity.this.listView.getChildCount();
                        int i182 = 0;
                        while (i182 < childCount2) {
                            View childAt3 = GroupCallActivity.this.listView.getChildAt(i182);
                            if (childAt3 == GroupCallActivity.this.scrimView) {
                                float max = Math.max(GroupCallActivity.this.listView.getLeft(), GroupCallActivity.this.listView.getLeft() + childAt3.getX());
                                float max2 = Math.max(y4, GroupCallActivity.this.listView.getY() + childAt3.getY());
                                float min = Math.min(GroupCallActivity.this.listView.getRight(), GroupCallActivity.this.listView.getLeft() + childAt3.getX() + childAt3.getMeasuredWidth());
                                float min2 = Math.min(GroupCallActivity.this.listView.getY() + GroupCallActivity.this.listView.getMeasuredHeight(), GroupCallActivity.this.listView.getY() + childAt3.getY() + GroupCallActivity.this.scrimView.getClipHeight());
                                if (max2 < min2) {
                                    if (childAt3.getAlpha() != 1.0f) {
                                        int alpha = (int) (childAt3.getAlpha() * f11);
                                        f = min;
                                        f2 = max2;
                                        f3 = max;
                                        view5 = childAt3;
                                        canvas.saveLayerAlpha(max, max2, min, min2, alpha, 31);
                                    } else {
                                        f = min;
                                        f2 = max2;
                                        f3 = max;
                                        view5 = childAt3;
                                        canvas.save();
                                    }
                                    canvas.clipRect(f3, f2, f, getMeasuredHeight());
                                    canvas.translate(GroupCallActivity.this.listView.getLeft() + view5.getX(), GroupCallActivity.this.listView.getY() + view5.getY());
                                    this.rect.set(0.0f, 0.0f, view5.getMeasuredWidth(), (int) (GroupCallActivity.this.scrimView.getMeasuredHeight() + ((GroupCallActivity.this.scrimView.getClipHeight() - GroupCallActivity.this.scrimView.getMeasuredHeight()) * (1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - r1)))));
                                    GroupCallActivity.this.scrimView.setAboutVisibleProgress(GroupCallActivity.this.listViewBackgroundPaint.getColor(), GroupCallActivity.this.scrimPaint.getAlpha() / 100.0f);
                                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), GroupCallActivity.this.listViewBackgroundPaint);
                                    view5.draw(canvas);
                                    canvas.restore();
                                    i182++;
                                    f11 = 255.0f;
                                }
                            }
                            i182++;
                            f11 = 255.0f;
                        }
                        return;
                    }
                    if (GroupCallActivity.this.scrimFullscreenView != null) {
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.scrimFullscreenView.getX() + GroupCallActivity.this.fullscreenUsersListView.getX() + GroupCallActivity.this.renderersContainer.getX(), GroupCallActivity.this.scrimFullscreenView.getY() + GroupCallActivity.this.fullscreenUsersListView.getY() + GroupCallActivity.this.renderersContainer.getY());
                        ((GroupCallActivity.this.scrimFullscreenView.getRenderer() == null || !GroupCallActivity.this.scrimFullscreenView.getRenderer().isAttached() || GroupCallActivity.this.scrimFullscreenView.getRenderer().showingInFullscreen) ? GroupCallActivity.this.scrimFullscreenView : GroupCallActivity.this.scrimFullscreenView.getRenderer()).draw(canvas);
                        GroupCallActivity.this.scrimFullscreenView.drawOverlays(canvas);
                    } else {
                        if (GroupCallActivity.this.scrimRenderer == null || !GroupCallActivity.this.scrimRenderer.isAttached()) {
                            return;
                        }
                        canvas.save();
                        canvas.translate(GroupCallActivity.this.scrimRenderer.getX() + GroupCallActivity.this.renderersContainer.getX(), GroupCallActivity.this.scrimRenderer.getY() + GroupCallActivity.this.renderersContainer.getY());
                        GroupCallActivity.this.scrimRenderer.draw(canvas);
                    }
                }
                canvas.restore();
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view5, long j2) {
                if (!GroupCallActivity.isTabletMode && GroupCallActivity.this.renderersContainer.progressToFullscreenMode == 1.0f && (view5 == GroupCallActivity.this.actionBar || view5 == GroupCallActivity.this.actionBarShadow || view5 == GroupCallActivity.this.actionBarBackground || view5 == GroupCallActivity.this.titleTextView || view5 == GroupCallActivity.this.menuItemsContainer || view5 == GroupCallActivity.this.textureLightningView)) {
                    return true;
                }
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                if (groupCallActivity.drawingForBlur && view5 == groupCallActivity.renderersContainer) {
                    canvas.save();
                    canvas.translate(GroupCallActivity.this.renderersContainer.getX() + GroupCallActivity.this.fullscreenUsersListView.getX(), GroupCallActivity.this.renderersContainer.getY() + GroupCallActivity.this.fullscreenUsersListView.getY());
                    GroupCallActivity.this.fullscreenUsersListView.draw(canvas);
                    canvas.restore();
                    return true;
                }
                if (view5 == GroupCallActivity.this.avatarPreviewContainer || view5 == GroupCallActivity.this.scrimPopupLayout || view5 == GroupCallActivity.this.scrimView) {
                    return true;
                }
                if (GroupCallActivity.this.contentFullyOverlayed && GroupCallActivity.this.useBlur && (view5 == GroupCallActivity.this.listView || view5 == GroupCallActivity.this.buttonsContainer)) {
                    return true;
                }
                if (GroupCallActivity.this.scrimFullscreenView == null) {
                    GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                    if (!groupCallActivity2.drawingForBlur && groupCallActivity2.avatarsPreviewShowed && (view5 == GroupCallActivity.this.buttonsBackgroundGradientView2 || view5 == GroupCallActivity.this.buttonsBackgroundGradientView || view5 == GroupCallActivity.this.buttonsContainer || view5 == GroupCallActivity.this.undoView[0] || view5 == GroupCallActivity.this.undoView[1])) {
                        return true;
                    }
                }
                return super.drawChild(canvas, view5, j2);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float f;
                int dp3 = AndroidUtilities.dp(74.0f);
                float f2 = GroupCallActivity.this.scrollOffsetY - dp3;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop;
                if (((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + f2 < ActionBar.getCurrentActionBarHeight()) {
                    float min = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - f2) - ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop) / ((dp3 - ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop) - AndroidUtilities.dp(14.0f)));
                    int currentActionBarHeight = (int) ((ActionBar.getCurrentActionBarHeight() - r0) * min);
                    f2 -= currentActionBarHeight;
                    measuredHeight += currentActionBarHeight;
                    f = 1.0f - min;
                } else {
                    f = 1.0f;
                }
                float paddingTop = f2 + getPaddingTop();
                if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode != 1.0f) {
                    GroupCallActivity.this.shadowDrawable.setBounds(0, (int) paddingTop, getMeasuredWidth(), measuredHeight);
                    GroupCallActivity.this.shadowDrawable.draw(canvas);
                    if (f != 1.0f) {
                        Theme.dialogs_onlineCirclePaint.setColor(GroupCallActivity.this.backgroundColor);
                        this.rect.set(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + paddingTop, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop + paddingTop + AndroidUtilities.dp(24.0f));
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int) (GroupCallActivity.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(GroupCallActivity.this.backgroundColor) * 0.8f), (int) (Color.green(GroupCallActivity.this.backgroundColor) * 0.8f), (int) (Color.blue(GroupCallActivity.this.backgroundColor) * 0.8f)));
                    canvas.drawRect(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), Theme.dialogs_onlineCirclePaint);
                    PrivateVideoPreviewDialog privateVideoPreviewDialog = GroupCallActivity.this.previewDialog;
                    if (privateVideoPreviewDialog != null) {
                        Theme.dialogs_onlineCirclePaint.setColor(privateVideoPreviewDialog.getBackgroundColor());
                        canvas.drawRect(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), Theme.dialogs_onlineCirclePaint);
                    }
                }
                if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode != 0.0f) {
                    Theme.dialogs_onlineCirclePaint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_actionBar), (int) (GroupCallActivity.this.renderersContainer.progressToFullscreenMode * 255.0f)));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_onlineCirclePaint);
                }
                if (GroupCallActivity.this.isRtmpStream() && LiteMode.isEnabled(512)) {
                    if (GroupCallActivity.this.renderersContainer.progressToFullscreenMode < 0.15d) {
                        if (GroupCallActivity.this.needTextureLightning) {
                            return;
                        }
                        GroupCallActivity.this.needTextureLightning = true;
                        GroupCallActivity.this.runUpdateTextureLightningRunnable();
                        return;
                    }
                    if (GroupCallActivity.this.needTextureLightning) {
                        GroupCallActivity.this.needTextureLightning = false;
                        AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.updateTextureLightningRunnable);
                    }
                }
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (GroupCallActivity.this.scrimView != null && motionEvent.getAction() == 0) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    this.rect.set(GroupCallActivity.this.scrimPopupLayout.getX(), GroupCallActivity.this.scrimPopupLayout.getY(), GroupCallActivity.this.scrimPopupLayout.getX() + GroupCallActivity.this.scrimPopupLayout.getMeasuredWidth(), GroupCallActivity.this.scrimPopupLayout.getY() + GroupCallActivity.this.scrimPopupLayout.getMeasuredHeight());
                    boolean z2 = !this.rect.contains(x, y);
                    this.rect.set(GroupCallActivity.this.avatarPreviewContainer.getX(), GroupCallActivity.this.avatarPreviewContainer.getY(), GroupCallActivity.this.avatarPreviewContainer.getX() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth(), GroupCallActivity.this.avatarPreviewContainer.getY() + GroupCallActivity.this.avatarPreviewContainer.getMeasuredWidth() + GroupCallActivity.this.scrimView.getMeasuredHeight());
                    if (this.rect.contains(x, y)) {
                        z2 = false;
                    }
                    if (z2) {
                        GroupCallActivity.this.dismissAvatarPreview(true);
                        return true;
                    }
                }
                if (motionEvent.getAction() == 0 && GroupCallActivity.this.scrollOffsetY != 0.0f && motionEvent.getY() < GroupCallActivity.this.scrollOffsetY - AndroidUtilities.dp(37.0f) && GroupCallActivity.this.actionBar.getAlpha() == 0.0f && !GroupCallActivity.this.avatarsPreviewShowed) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    if (groupCallActivity.previewDialog == null && !groupCallActivity.renderersContainer.inFullscreenMode) {
                        GroupCallActivity.this.dismiss();
                        return true;
                    }
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.KeyEvent.Callback
            public boolean onKeyDown(int i102, KeyEvent keyEvent) {
                if (GroupCallActivity.this.scrimView == null || i102 != 4) {
                    return super.onKeyDown(i102, keyEvent);
                }
                GroupCallActivity.this.dismissAvatarPreview(true);
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i102, int i112, int i122, int i132) {
                float f;
                boolean z3;
                if (GroupCallActivity.isTabletMode && this.localHasVideo != GroupCallActivity.this.hasVideo && this.wasLayout) {
                    f = GroupCallActivity.this.listView.getX();
                    z3 = true;
                } else {
                    f = 0.0f;
                    z3 = false;
                }
                this.localHasVideo = GroupCallActivity.this.hasVideo;
                GroupCallActivity.this.renderersContainer.inLayout = true;
                super.onLayout(z2, i102, i112, i122, i132);
                GroupCallActivity.this.renderersContainer.inLayout = false;
                GroupCallActivity.this.updateLayout(false);
                this.wasLayout = true;
                if (!z3 || GroupCallActivity.this.listView.getLeft() == f) {
                    return;
                }
                float left = f - GroupCallActivity.this.listView.getLeft();
                GroupCallActivity.this.listView.setTranslationX(left);
                GroupCallActivity.this.buttonsContainer.setTranslationX(left);
                GroupCallActivity.this.buttonsBackgroundGradientView.setTranslationX(left);
                GroupCallActivity.this.buttonsBackgroundGradientView2.setTranslationX(left);
                ViewPropertyAnimator duration = GroupCallActivity.this.listView.animate().translationX(0.0f).setDuration(350L);
                CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
                duration.setInterpolator(cubicBezierInterpolator2).start();
                GroupCallActivity.this.buttonsBackgroundGradientView.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator2).start();
                GroupCallActivity.this.buttonsBackgroundGradientView2.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator2).start();
                GroupCallActivity.this.buttonsContainer.animate().translationX(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator2).start();
            }

            /* JADX WARN: Removed duplicated region for block: B:173:0x0587  */
            /* JADX WARN: Removed duplicated region for block: B:179:0x060f  */
            /* JADX WARN: Removed duplicated region for block: B:186:0x0647  */
            /* JADX WARN: Removed duplicated region for block: B:191:0x06a4  */
            /* JADX WARN: Removed duplicated region for block: B:196:0x06e2  */
            /* JADX WARN: Removed duplicated region for block: B:206:0x0779 A[LOOP:2: B:204:0x076d->B:206:0x0779, LOOP_END] */
            /* JADX WARN: Removed duplicated region for block: B:210:0x07af  */
            /* JADX WARN: Removed duplicated region for block: B:215:0x0611  */
            /* JADX WARN: Removed duplicated region for block: B:216:0x05b9  */
            @Override // android.widget.FrameLayout, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onMeasure(int i102, int i112) {
                int i122;
                int max;
                int i132;
                int measuredHeight;
                int size = View.MeasureSpec.getSize(i112);
                this.ignoreLayout = true;
                boolean z2 = View.MeasureSpec.getSize(i102) > size && !AndroidUtilities.isTablet();
                GroupCallActivity.this.renderersContainer.listWidth = View.MeasureSpec.getSize(i102);
                boolean z3 = AndroidUtilities.isTablet() && View.MeasureSpec.getSize(i102) > size && !GroupCallActivity.this.isRtmpStream();
                if (GroupCallActivity.isLandscapeMode != z2) {
                    GroupCallActivity.isLandscapeMode = z2;
                    int measuredWidth = GroupCallActivity.this.muteButton.getMeasuredWidth();
                    if (measuredWidth == 0) {
                        measuredWidth = GroupCallActivity.this.muteButton.getLayoutParams().width;
                    }
                    float dp3 = AndroidUtilities.dp(52.0f) / (measuredWidth - AndroidUtilities.dp(8.0f));
                    if (!GroupCallActivity.isLandscapeMode && !GroupCallActivity.this.isRtmpStream() && !GroupCallActivity.this.renderersContainer.inFullscreenMode) {
                        dp3 = 1.0f;
                    }
                    boolean z4 = GroupCallActivity.this.renderersContainer.inFullscreenMode && (AndroidUtilities.isTablet() || GroupCallActivity.isLandscapeMode == GroupCallActivity.this.isRtmpLandscapeMode());
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    ImageView imageView3 = z4 ? groupCallActivity.minimizeButton : groupCallActivity.expandButton;
                    ImageView imageView22 = z4 ? GroupCallActivity.this.expandButton : GroupCallActivity.this.minimizeButton;
                    imageView3.setAlpha(1.0f);
                    imageView3.setScaleX(dp3);
                    imageView3.setScaleY(dp3);
                    imageView22.setAlpha(0.0f);
                    GroupCallActivity.this.muteLabel[0].setAlpha(1.0f);
                    GroupCallActivity.this.muteLabel[1].setAlpha(1.0f);
                    if (GroupCallActivity.this.renderersContainer.inFullscreenMode || (GroupCallActivity.isLandscapeMode && !AndroidUtilities.isTablet())) {
                        GroupCallActivity.this.muteLabel[0].setScaleX(0.687f);
                        GroupCallActivity.this.muteLabel[1].setScaleY(0.687f);
                    } else {
                        GroupCallActivity.this.muteLabel[0].setScaleX(1.0f);
                        GroupCallActivity.this.muteLabel[1].setScaleY(1.0f);
                    }
                    GroupCallActivity.this.invalidateLayoutFullscreen();
                    GroupCallActivity.this.layoutManager.setSpanCount(GroupCallActivity.isLandscapeMode ? 6 : 2);
                    GroupCallActivity.this.listView.invalidateItemDecorations();
                    GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                    this.updateRenderers = true;
                    if (GroupCallActivity.this.scheduleInfoTextView != null) {
                        GroupCallActivity.this.scheduleInfoTextView.setVisibility(!GroupCallActivity.isLandscapeMode ? 0 : 8);
                    }
                    if (GroupCallActivity.this.isRtmpLandscapeMode() == z2 && GroupCallActivity.this.isRtmpStream() && !GroupCallActivity.this.renderersContainer.inFullscreenMode && !GroupCallActivity.this.call.visibleVideoParticipants.isEmpty()) {
                        GroupCallActivity groupCallActivity2 = GroupCallActivity.this;
                        groupCallActivity2.fullscreenFor(groupCallActivity2.call.visibleVideoParticipants.get(0));
                        GroupCallActivity.this.renderersContainer.delayHideUi();
                    }
                }
                if (GroupCallActivity.isTabletMode != z3) {
                    GroupCallActivity.isTabletMode = z3;
                    GroupCallActivity.this.tabletVideoGridView.setVisibility(z3 ? 0 : 8);
                    GroupCallActivity.this.listView.invalidateItemDecorations();
                    GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                    this.updateRenderers = true;
                }
                if (this.updateRenderers) {
                    GroupCallActivity.this.applyCallParticipantUpdates(true);
                    GroupCallActivity.this.listAdapter.notifyDataSetChanged();
                    GroupCallActivity groupCallActivity3 = GroupCallActivity.this;
                    groupCallActivity3.fullscreenAdapter.update(false, groupCallActivity3.tabletVideoGridView);
                    if (GroupCallActivity.isTabletMode) {
                        GroupCallActivity groupCallActivity4 = GroupCallActivity.this;
                        groupCallActivity4.tabletGridAdapter.update(false, groupCallActivity4.tabletVideoGridView);
                    }
                    GroupCallActivity.this.tabletVideoGridView.setVisibility(GroupCallActivity.isTabletMode ? 0 : 8);
                    GroupCallActivity groupCallActivity5 = GroupCallActivity.this;
                    groupCallActivity5.tabletGridAdapter.setVisibility(groupCallActivity5.tabletVideoGridView, GroupCallActivity.isTabletMode && !groupCallActivity5.renderersContainer.inFullscreenMode, true);
                    GroupCallActivity groupCallActivity6 = GroupCallActivity.this;
                    groupCallActivity6.listViewVideoVisibility = !GroupCallActivity.isTabletMode || groupCallActivity6.renderersContainer.inFullscreenMode;
                    boolean z5 = !GroupCallActivity.isTabletMode && GroupCallActivity.this.renderersContainer.inFullscreenMode;
                    GroupCallActivity groupCallActivity7 = GroupCallActivity.this;
                    groupCallActivity7.fullscreenAdapter.setVisibility(groupCallActivity7.fullscreenUsersListView, z5);
                    GroupCallActivity.this.fullscreenUsersListView.setVisibility(z5 ? 0 : 8);
                    GroupCallActivity.this.listView.setVisibility((GroupCallActivity.isTabletMode || !GroupCallActivity.this.renderersContainer.inFullscreenMode) ? 0 : 8);
                    GroupCallActivity.this.layoutManager.setSpanCount(GroupCallActivity.isLandscapeMode ? 6 : 2);
                    GroupCallActivity.this.updateState(false, false);
                    GroupCallActivity.this.listView.invalidateItemDecorations();
                    GroupCallActivity.this.fullscreenUsersListView.invalidateItemDecorations();
                    AndroidUtilities.updateVisibleRows(GroupCallActivity.this.listView);
                    this.updateRenderers = false;
                    GroupCallActivity.this.attachedRenderersTmp.clear();
                    GroupCallActivity.this.attachedRenderersTmp.addAll(GroupCallActivity.this.attachedRenderers);
                    GroupCallActivity.this.renderersContainer.setIsTablet(GroupCallActivity.isTabletMode);
                    for (int i142 = 0; i142 < GroupCallActivity.this.attachedRenderersTmp.size(); i142++) {
                        ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderersTmp.get(i142)).updateAttachState(true);
                    }
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    setPadding(((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, GroupCallActivity.this.getStatusBarHeight(), ((BottomSheet) GroupCallActivity.this).backgroundPaddingLeft, 0);
                }
                int paddingTop = (size - getPaddingTop()) - AndroidUtilities.dp(GroupCallActivity.this.isRtmpStream() ? 72.0f : 245.0f);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) GroupCallActivity.this.renderersContainer.getLayoutParams();
                if (GroupCallActivity.isTabletMode) {
                    layoutParams.topMargin = ActionBar.getCurrentActionBarHeight();
                } else {
                    layoutParams.topMargin = 0;
                }
                for (int i152 = 0; i152 < 2; i152++) {
                    ((FrameLayout.LayoutParams) GroupCallActivity.this.undoView[i152].getLayoutParams()).rightMargin = GroupCallActivity.isTabletMode ? AndroidUtilities.dp(328.0f) : AndroidUtilities.dp(8.0f);
                }
                RecyclerListView recyclerListView6 = GroupCallActivity.this.tabletVideoGridView;
                if (recyclerListView6 != null) {
                    ((FrameLayout.LayoutParams) recyclerListView6.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                }
                int dp22 = AndroidUtilities.dp(GroupCallActivity.this.isRtmpStream() ? 100.0f : 150.0f);
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) GroupCallActivity.this.listView.getLayoutParams();
                if (GroupCallActivity.isTabletMode) {
                    layoutParams2.gravity = GroupCallActivity.this.hasVideo ? 5 : 1;
                    layoutParams2.width = AndroidUtilities.dp(320.0f);
                    int dp32 = AndroidUtilities.dp(4.0f);
                    layoutParams2.leftMargin = dp32;
                    layoutParams2.rightMargin = dp32;
                    layoutParams2.bottomMargin = dp22;
                    layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight();
                    i122 = AndroidUtilities.dp(60.0f);
                } else {
                    boolean z6 = GroupCallActivity.isLandscapeMode;
                    layoutParams2.gravity = 51;
                    layoutParams2.width = -1;
                    if (z6) {
                        layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight();
                        layoutParams2.bottomMargin = AndroidUtilities.dp(14.0f);
                        layoutParams2.rightMargin = AndroidUtilities.dp(90.0f);
                        layoutParams2.leftMargin = AndroidUtilities.dp(14.0f);
                        i122 = 0;
                    } else {
                        int dp4 = AndroidUtilities.dp(60.0f);
                        layoutParams2.bottomMargin = dp22;
                        layoutParams2.topMargin = ActionBar.getCurrentActionBarHeight() + AndroidUtilities.dp(14.0f);
                        int dp5 = AndroidUtilities.dp(14.0f);
                        layoutParams2.leftMargin = dp5;
                        layoutParams2.rightMargin = dp5;
                        i122 = dp4;
                    }
                }
                int i162 = 81;
                if (!GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) {
                    GroupCallActivity.this.buttonsBackgroundGradientView.setVisibility(0);
                    FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsBackgroundGradientView.getLayoutParams();
                    layoutParams3.bottomMargin = dp22;
                    if (GroupCallActivity.isTabletMode) {
                        layoutParams3.gravity = GroupCallActivity.this.hasVideo ? 85 : 81;
                        layoutParams3.width = AndroidUtilities.dp(328.0f);
                    } else {
                        layoutParams3.width = -1;
                    }
                    GroupCallActivity.this.buttonsBackgroundGradientView2.setVisibility(0);
                    FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsBackgroundGradientView2.getLayoutParams();
                    layoutParams4.height = dp22;
                    if (GroupCallActivity.isTabletMode) {
                        layoutParams4.gravity = GroupCallActivity.this.hasVideo ? 85 : 81;
                        layoutParams4.width = AndroidUtilities.dp(328.0f);
                    } else {
                        layoutParams4.width = -1;
                    }
                } else {
                    GroupCallActivity.this.buttonsBackgroundGradientView.setVisibility(8);
                    GroupCallActivity.this.buttonsBackgroundGradientView2.setVisibility(8);
                }
                if (GroupCallActivity.isLandscapeMode) {
                    GroupCallActivity.this.fullscreenUsersListView.setPadding(0, AndroidUtilities.dp(9.0f), 0, AndroidUtilities.dp(9.0f));
                } else {
                    GroupCallActivity.this.fullscreenUsersListView.setPadding(AndroidUtilities.dp(9.0f), 0, AndroidUtilities.dp(9.0f), 0);
                }
                FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) GroupCallActivity.this.buttonsContainer.getLayoutParams();
                if (GroupCallActivity.isTabletMode) {
                    layoutParams5.width = AndroidUtilities.dp(320.0f);
                    layoutParams5.height = AndroidUtilities.dp(200.0f);
                    if (GroupCallActivity.this.hasVideo) {
                        i162 = 85;
                    }
                } else {
                    if (GroupCallActivity.isLandscapeMode) {
                        layoutParams5.width = AndroidUtilities.dp(90.0f);
                        layoutParams5.height = -1;
                        layoutParams5.gravity = 53;
                        if (GroupCallActivity.isLandscapeMode || GroupCallActivity.isTabletMode) {
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = 0;
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = 0;
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = 0;
                        } else {
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                            ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = AndroidUtilities.dp(90.0f);
                        }
                        FrameLayout.LayoutParams layoutParams62 = (FrameLayout.LayoutParams) GroupCallActivity.this.fullscreenUsersListView.getLayoutParams();
                        if (GroupCallActivity.isLandscapeMode) {
                            if (((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).getOrientation() != 0) {
                                ((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).setOrientation(0);
                            }
                            layoutParams62.height = AndroidUtilities.dp(80.0f);
                            layoutParams62.width = -1;
                            layoutParams62.gravity = 80;
                            layoutParams62.rightMargin = 0;
                            layoutParams62.bottomMargin = AndroidUtilities.dp(100.0f);
                        } else {
                            if (((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).getOrientation() != 1) {
                                ((LinearLayoutManager) GroupCallActivity.this.fullscreenUsersListView.getLayoutManager()).setOrientation(1);
                            }
                            layoutParams62.height = -1;
                            layoutParams62.width = AndroidUtilities.dp(80.0f);
                            layoutParams62.gravity = 53;
                            layoutParams62.rightMargin = AndroidUtilities.dp(100.0f);
                            layoutParams62.bottomMargin = 0;
                        }
                        ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                        max = !GroupCallActivity.isTabletMode ? 0 : Math.max(0, (paddingTop - Math.max(AndroidUtilities.dp(259.0f), (paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f));
                        if (GroupCallActivity.this.listView.getPaddingTop() == max || GroupCallActivity.this.listView.getPaddingBottom() != i122) {
                            GroupCallActivity.this.listView.setPadding(0, max, 0, i122);
                        }
                        if (GroupCallActivity.this.watchersView != null) {
                            FrameLayout.LayoutParams layoutParams7 = (FrameLayout.LayoutParams) GroupCallActivity.this.watchersView.getLayoutParams();
                            GroupCallGridCell findGroupCallGridCell = GroupCallActivity.this.findGroupCallGridCell();
                            if (findGroupCallGridCell != null) {
                                int top = (GroupCallActivity.this.buttonsContainer.getTop() + (GroupCallActivity.this.buttonsContainer.getMeasuredHeight() / 2)) - (GroupCallActivity.this.leaveButton.getMeasuredHeight() / 2);
                                int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + max + findGroupCallGridCell.getMeasuredHeight();
                                layoutParams7.topMargin = (currentActionBarHeight + ((top - currentActionBarHeight) / 2)) - AndroidUtilities.dp(32.0f);
                                layoutParams7.height = AndroidUtilities.dp(70.0f);
                            }
                        }
                        if (GroupCallActivity.this.textureLightningView != null) {
                            FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) GroupCallActivity.this.textureLightningView.getLayoutParams();
                            GroupCallGridCell findGroupCallGridCell2 = GroupCallActivity.this.findGroupCallGridCell();
                            if (findGroupCallGridCell2 != null) {
                                layoutParams8.height = findGroupCallGridCell2.getMeasuredHeight() - AndroidUtilities.dp(14.0f);
                                layoutParams8.width = findGroupCallGridCell2.getMeasuredWidth() - AndroidUtilities.dp(7.0f);
                                int dp6 = AndroidUtilities.dp(16.0f);
                                layoutParams8.leftMargin = dp6;
                                layoutParams8.rightMargin = dp6;
                            }
                        }
                        if (GroupCallActivity.this.scheduleStartAtTextView != null) {
                            int dp7 = max + (((paddingTop - max) + AndroidUtilities.dp(60.0f)) / 2);
                            FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleStartInTextView.getLayoutParams();
                            layoutParams9.topMargin = dp7 - AndroidUtilities.dp(30.0f);
                            FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleStartAtTextView.getLayoutParams();
                            layoutParams10.topMargin = AndroidUtilities.dp(80.0f) + dp7;
                            FrameLayout.LayoutParams layoutParams11 = (FrameLayout.LayoutParams) GroupCallActivity.this.scheduleTimeTextView.getLayoutParams();
                            if (layoutParams9.topMargin < ActionBar.getCurrentActionBarHeight() || layoutParams10.topMargin + AndroidUtilities.dp(20.0f) > size - AndroidUtilities.dp(231.0f)) {
                                GroupCallActivity.this.scheduleStartInTextView.setVisibility(4);
                                GroupCallActivity.this.scheduleStartAtTextView.setVisibility(4);
                                dp7 -= AndroidUtilities.dp(20.0f);
                            } else {
                                GroupCallActivity.this.scheduleStartInTextView.setVisibility(0);
                                GroupCallActivity.this.scheduleStartAtTextView.setVisibility(0);
                            }
                            layoutParams11.topMargin = dp7;
                        }
                        for (i132 = 0; i132 < GroupCallActivity.this.attachedRenderers.size(); i132++) {
                            ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderers.get(i132)).setFullscreenMode(GroupCallActivity.this.renderersContainer.inFullscreenMode, true);
                        }
                        this.ignoreLayout = false;
                        super.onMeasure(i102, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                        measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
                        if (measuredHeight != this.lastSize) {
                            this.lastSize = measuredHeight;
                            GroupCallActivity.this.dismissAvatarPreview(false);
                        }
                        GroupCallActivity.this.cellFlickerDrawable.setParentWidth(getMeasuredWidth());
                    }
                    layoutParams5.width = -1;
                    layoutParams5.height = AndroidUtilities.dp(200.0f);
                }
                layoutParams5.gravity = i162;
                layoutParams5.rightMargin = 0;
                if (GroupCallActivity.isLandscapeMode) {
                }
                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBar.getLayoutParams()).rightMargin = 0;
                ((FrameLayout.LayoutParams) GroupCallActivity.this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarBackground.getLayoutParams()).rightMargin = 0;
                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).rightMargin = 0;
                FrameLayout.LayoutParams layoutParams622 = (FrameLayout.LayoutParams) GroupCallActivity.this.fullscreenUsersListView.getLayoutParams();
                if (GroupCallActivity.isLandscapeMode) {
                }
                ((FrameLayout.LayoutParams) GroupCallActivity.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                if (!GroupCallActivity.isTabletMode) {
                }
                if (GroupCallActivity.this.listView.getPaddingTop() == max) {
                }
                GroupCallActivity.this.listView.setPadding(0, max, 0, i122);
                if (GroupCallActivity.this.watchersView != null) {
                }
                if (GroupCallActivity.this.textureLightningView != null) {
                }
                if (GroupCallActivity.this.scheduleStartAtTextView != null) {
                }
                while (i132 < GroupCallActivity.this.attachedRenderers.size()) {
                }
                this.ignoreLayout = false;
                super.onMeasure(i102, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
                if (measuredHeight != this.lastSize) {
                }
                GroupCallActivity.this.cellFlickerDrawable.setParentWidth(getMeasuredWidth());
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !GroupCallActivity.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.containerView = frameLayout3;
        frameLayout3.setFocusable(true);
        this.containerView.setFocusableInTouchMode(true);
        this.containerView.setWillNotDraw(false);
        ViewGroup viewGroup2 = this.containerView;
        int i102 = this.backgroundPaddingLeft;
        viewGroup2.setPadding(i102, 0, i102, 0);
        this.containerView.setKeepScreenOn(true);
        this.containerView.setClipChildren(false);
        if (inputPeer != null) {
        }
        if (isRtmpStream()) {
        }
        RecyclerListView recyclerListView6 = new RecyclerListView(context) { // from class: org.telegram.ui.GroupCallActivity.10
            private final LongSparseIntArray visiblePeerTmp = new LongSparseIntArray();

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:29:0x00ae  */
            /* JADX WARN: Removed duplicated region for block: B:35:0x00d4  */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void dispatchDraw(Canvas canvas) {
                float f;
                int i112;
                int i122;
                boolean z2 = GroupCallActivity.this.itemAnimator.outMinTop != Float.MAX_VALUE;
                this.visiblePeerTmp.clear();
                for (int i132 = 0; i132 < GroupCallActivity.this.visiblePeerIds.size(); i132++) {
                    this.visiblePeerTmp.put(GroupCallActivity.this.visiblePeerIds.keyAt(i132), 1);
                }
                GroupCallActivity.this.visiblePeerIds.clear();
                int i142 = 0;
                boolean z3 = false;
                float f2 = Float.MAX_VALUE;
                float f3 = 0.0f;
                for (int childCount = getChildCount(); i142 < childCount; childCount = i112) {
                    View childAt = getChildAt(i142);
                    RecyclerView.ViewHolder findContainingViewHolder = findContainingViewHolder(childAt);
                    if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() == 3 || findContainingViewHolder.getItemViewType() == 4 || findContainingViewHolder.getItemViewType() == 5 || findContainingViewHolder.getItemViewType() == 6) {
                        i112 = childCount;
                        i122 = i142;
                    } else {
                        if (findContainingViewHolder.getItemViewType() == 1) {
                            View view5 = findContainingViewHolder.itemView;
                            if (view5 instanceof GroupCallUserCell) {
                                GroupCallUserCell groupCallUserCell = (GroupCallUserCell) view5;
                                i112 = childCount;
                                GroupCallActivity.this.visiblePeerIds.append(groupCallUserCell.getPeerId(), 1);
                                i122 = i142;
                                if (this.visiblePeerTmp.get(groupCallUserCell.getPeerId(), 0) == 0) {
                                    z3 = true;
                                } else {
                                    this.visiblePeerTmp.delete(groupCallUserCell.getPeerId());
                                }
                                if (z2) {
                                    f3 = Math.max(f3, childAt.getY() + childAt.getMeasuredHeight());
                                    f2 = Math.min(f2, Math.max(0.0f, childAt.getY()));
                                    i142 = i122 + 1;
                                } else if (!GroupCallActivity.this.itemAnimator.removingHolders.contains(findContainingViewHolder)) {
                                    f2 = Math.min(f2, Math.max(0, childAt.getTop()));
                                    f3 = Math.max(f3, childAt.getBottom());
                                }
                            }
                        }
                        i112 = childCount;
                        i122 = i142;
                        if (z2) {
                        }
                    }
                    i142 = i122 + 1;
                }
                if (this.visiblePeerTmp.size() > 0) {
                    z3 = true;
                }
                if (z3) {
                    GroupCallActivity.this.updateSubtitle();
                }
                if (z2) {
                    f = (GroupCallActivity.this.itemAnimator.outMinTop * (1.0f - GroupCallActivity.this.itemAnimator.animationProgress)) + (GroupCallActivity.this.itemAnimator.animationProgress * f2);
                    f3 = (f3 * GroupCallActivity.this.itemAnimator.animationProgress) + (GroupCallActivity.this.itemAnimator.outMaxBottom * (1.0f - GroupCallActivity.this.itemAnimator.animationProgress));
                } else {
                    f = f2;
                }
                if (f2 != Float.MAX_VALUE) {
                    GroupCallActivity.this.rect.set((getMeasuredWidth() - (AndroidUtilities.isTablet() ? Math.min(AndroidUtilities.dp(420.0f), getMeasuredWidth()) : getMeasuredWidth())) >> 1, f, getMeasuredWidth() - r4, Math.min(getMeasuredHeight() - getTranslationY(), f3));
                    canvas.drawRoundRect(GroupCallActivity.this.rect, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), GroupCallActivity.this.listViewBackgroundPaint);
                }
                canvas.save();
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                super.dispatchDraw(canvas);
                canvas.restore();
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view5, long j2) {
                if (view5 == GroupCallActivity.this.scrimView) {
                    return false;
                }
                return super.drawChild(canvas, view5, j2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z2, int i112, int i122, int i132, int i142) {
                super.onLayout(z2, i112, i122, i132, i142);
                GroupCallActivity.this.itemAnimator.updateBackgroundBeforeAnimation();
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public void setVisibility(int i112) {
                if (getVisibility() != i112) {
                    for (int i122 = 0; i122 < getChildCount(); i122++) {
                        View childAt = getChildAt(i122);
                        if (childAt instanceof GroupCallGridCell) {
                            GroupCallActivity.this.attachRenderer((GroupCallGridCell) childAt, i112 == 0);
                        }
                    }
                }
                super.setVisibility(i112);
            }
        };
        this.listView = recyclerListView6;
        recyclerListView6.setClipToPadding(false);
        this.listView.setClipChildren(false);
        GroupCallItemAnimator groupCallItemAnimator2 = new GroupCallItemAnimator();
        this.itemAnimator = groupCallItemAnimator2;
        CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
        groupCallItemAnimator2.setTranslationInterpolator(cubicBezierInterpolator2);
        this.itemAnimator.setRemoveDuration(350L);
        this.itemAnimator.setAddDuration(350L);
        this.itemAnimator.setMoveDuration(350L);
        this.itemAnimator.setDelayAnimations(false);
        this.listView.setItemAnimator(this.itemAnimator);
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.11
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i112) {
                if (i112 != 0) {
                    if (GroupCallActivity.this.recordHintView != null) {
                        GroupCallActivity.this.recordHintView.hide();
                    }
                    if (GroupCallActivity.this.reminderHintView != null) {
                        GroupCallActivity.this.reminderHintView.hide();
                        return;
                    }
                    return;
                }
                if ((GroupCallActivity.this.scrollOffsetY - AndroidUtilities.dp(74.0f)) + ((BottomSheet) GroupCallActivity.this).backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || !GroupCallActivity.this.listView.canScrollVertically(1)) {
                    return;
                }
                GroupCallActivity.this.listView.getChildAt(0);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) GroupCallActivity.this.listView.findViewHolderForAdapterPosition(0);
                if (holder == null || holder.itemView.getTop() <= 0) {
                    return;
                }
                GroupCallActivity.this.listView.smoothScrollBy(0, holder.itemView.getTop());
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i112, int i122) {
                GroupCallActivity groupCallActivity;
                ChatObject.Call call2;
                if (GroupCallActivity.this.listView.getChildCount() <= 0 || (call2 = (groupCallActivity = GroupCallActivity.this).call) == null) {
                    return;
                }
                if (!call2.loadingMembers && !call2.membersLoadEndReached && groupCallActivity.layoutManager.findLastVisibleItemPosition() > GroupCallActivity.this.listAdapter.getItemCount() - 5) {
                    GroupCallActivity.this.call.loadMembers(false);
                }
                GroupCallActivity.this.updateLayout(true);
                if (GroupCallActivity.this.textureLightningView != null) {
                    GroupCallActivity.this.textureLightningView.invalidate();
                }
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }
        });
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView22 = this.listView;
        FillLastGridLayoutManager fillLastGridLayoutManager3 = new FillLastGridLayoutManager(getContext(), !isLandscapeMode ? 6 : 2, 1, false, 0, this.listView);
        this.layoutManager = fillLastGridLayoutManager3;
        recyclerListView22.setLayoutManager(fillLastGridLayoutManager3);
        FillLastGridLayoutManager fillLastGridLayoutManager22 = this.layoutManager;
        GridLayoutManager.SpanSizeLookup spanSizeLookup2 = new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.GroupCallActivity.12
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i112) {
                int i122 = GroupCallActivity.isLandscapeMode ? 6 : 2;
                if (GroupCallActivity.isTabletMode || i112 < GroupCallActivity.this.listAdapter.usersVideoGridStartRow || i112 >= GroupCallActivity.this.listAdapter.usersVideoGridEndRow) {
                    return i122;
                }
                int i132 = GroupCallActivity.this.listAdapter.usersVideoGridEndRow - GroupCallActivity.this.listAdapter.usersVideoGridStartRow;
                int i142 = (i112 != GroupCallActivity.this.listAdapter.usersVideoGridEndRow - 1 || (!GroupCallActivity.isLandscapeMode && i132 % 2 == 0)) ? 1 : 2;
                if (!GroupCallActivity.isLandscapeMode) {
                    return i142;
                }
                if (i132 == 1) {
                    return 6;
                }
                return i132 == 2 ? 3 : 2;
            }
        };
        this.spanSizeLookup = spanSizeLookup2;
        fillLastGridLayoutManager22.setSpanSizeLookup(spanSizeLookup2);
        this.listView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.GroupCallActivity.13
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view5, RecyclerView recyclerView, RecyclerView.State state) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(view5);
                if (childAdapterPosition >= 0) {
                    rect.setEmpty();
                    if (childAdapterPosition < GroupCallActivity.this.listAdapter.usersVideoGridStartRow || childAdapterPosition >= GroupCallActivity.this.listAdapter.usersVideoGridEndRow) {
                        return;
                    }
                    int i112 = childAdapterPosition - GroupCallActivity.this.listAdapter.usersVideoGridStartRow;
                    int i122 = GroupCallActivity.isLandscapeMode ? 6 : 2;
                    int i132 = i112 % i122;
                    if (i132 == 0) {
                        rect.right = AndroidUtilities.dp(2.0f);
                    } else {
                        rect.left = i132 == i122 + (-1) ? AndroidUtilities.dp(2.0f) : AndroidUtilities.dp(1.0f);
                    }
                }
            }
        });
        this.layoutManager.setBind(false);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 14.0f, 14.0f, 231.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setTopBottomSelectorRadius(13);
        this.listView.setSelectorDrawableColor(Theme.getColor(Theme.key_voipgroup_listSelector));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda27
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view5, int i112) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view5, i112);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view5, int i112, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view5, i112, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view5, int i112, float f, float f2) {
                GroupCallActivity.this.lambda$new$13(view5, i112, f, f2);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda28
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view5, int i112) {
                boolean lambda$new$14;
                lambda$new$14 = GroupCallActivity.this.lambda$new$14(view5, i112);
                return lambda$new$14;
            }
        });
        if (isRtmpStream()) {
        }
        RecyclerListView recyclerListView32 = new RecyclerListView(context);
        this.tabletVideoGridView = recyclerListView32;
        this.containerView.addView(recyclerListView32, LayoutHelper.createFrame(-1, -1.0f, 51, 14.0f, 14.0f, 324.0f, 14.0f));
        RecyclerListView recyclerListView42 = this.tabletVideoGridView;
        GroupCallTabletGridAdapter groupCallTabletGridAdapter2 = new GroupCallTabletGridAdapter(call, this.currentAccount, this);
        this.tabletGridAdapter = groupCallTabletGridAdapter2;
        recyclerListView42.setAdapter(groupCallTabletGridAdapter2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context, 6, 1, false);
        this.tabletVideoGridView.setLayoutManager(gridLayoutManager2);
        gridLayoutManager2.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.GroupCallActivity.15
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i112) {
                return GroupCallActivity.this.tabletGridAdapter.getSpanCount(i112);
            }
        });
        this.tabletVideoGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda29
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view5, int i112) {
                GroupCallActivity.this.lambda$new$15(view5, i112);
            }
        });
        DefaultItemAnimator defaultItemAnimator3 = new DefaultItemAnimator();
        defaultItemAnimator3.setDelayAnimations(false);
        defaultItemAnimator3.setTranslationInterpolator(cubicBezierInterpolator2);
        defaultItemAnimator3.setRemoveDuration(350L);
        defaultItemAnimator3.setAddDuration(350L);
        defaultItemAnimator3.setMoveDuration(350L);
        this.tabletVideoGridView.setItemAnimator(new DefaultItemAnimator() { // from class: org.telegram.ui.GroupCallActivity.16
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                GroupCallActivity.this.listView.invalidate();
                GroupCallActivity.this.renderersContainer.invalidate();
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                GroupCallActivity.this.updateLayout(true);
            }
        });
        this.tabletVideoGridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.17
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i112, int i122) {
                super.onScrolled(recyclerView, i112, i122);
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }
        });
        this.tabletGridAdapter.setVisibility(this.tabletVideoGridView, false, false);
        this.tabletVideoGridView.setVisibility(8);
        this.buttonsContainer = new 18(context);
        int color2 = Theme.getColor(Theme.key_voipgroup_unmuteButton2);
        int red2 = Color.red(color2);
        int green2 = Color.green(color2);
        int blue2 = Color.blue(color2);
        this.radialMatrix = new Matrix();
        this.radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(160.0f), new int[]{Color.argb(50, red2, green2, blue2), Color.argb(0, red2, green2, blue2)}, (float[]) null, Shader.TileMode.CLAMP);
        Paint paint5 = new Paint(1);
        this.radialPaint = paint5;
        paint5.setShader(this.radialGradient);
        BlobDrawable blobDrawable3 = new BlobDrawable(9);
        this.tinyWaveDrawable = blobDrawable3;
        BlobDrawable blobDrawable22 = new BlobDrawable(12);
        this.bigWaveDrawable = blobDrawable22;
        blobDrawable3.minRadius = AndroidUtilities.dp(62.0f);
        blobDrawable3.maxRadius = AndroidUtilities.dp(72.0f);
        blobDrawable3.generateBlob();
        blobDrawable22.minRadius = AndroidUtilities.dp(65.0f);
        blobDrawable22.maxRadius = AndroidUtilities.dp(75.0f);
        blobDrawable22.generateBlob();
        Paint paint22 = blobDrawable3.paint;
        int i112 = Theme.key_voipgroup_unmuteButton;
        paint22.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i112), 38));
        blobDrawable22.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i112), 76));
        VoIPToggleButton voIPToggleButton5 = new VoIPToggleButton(context);
        this.soundButton = voIPToggleButton5;
        voIPToggleButton5.setCheckable(true);
        this.soundButton.setTextSize(12);
        this.buttonsContainer.addView(this.soundButton, LayoutHelper.createFrame(68, 80.0f));
        this.soundButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$16(view5);
            }
        });
        VoIPToggleButton voIPToggleButton22 = new VoIPToggleButton(context);
        this.cameraButton = voIPToggleButton22;
        voIPToggleButton22.setCheckable(true);
        this.cameraButton.setTextSize(12);
        this.cameraButton.showText(false, false);
        this.cameraButton.setCrossOffset(-AndroidUtilities.dpf2(3.5f));
        this.cameraButton.setVisibility(8);
        this.buttonsContainer.addView(this.cameraButton, LayoutHelper.createFrame(68, 80.0f));
        VoIPToggleButton voIPToggleButton32 = new VoIPToggleButton(context);
        this.flipButton = voIPToggleButton32;
        voIPToggleButton32.setCheckable(true);
        this.flipButton.setTextSize(12);
        this.flipButton.showText(false, false);
        RLottieImageView rLottieImageView3 = new RLottieImageView(context);
        this.flipButton.addView(rLottieImageView3, LayoutHelper.createFrame(32, 32.0f, 0, 18.0f, 10.0f, 18.0f, 0.0f));
        int i122 = R.raw.camera_flip;
        RLottieDrawable rLottieDrawable2 = new RLottieDrawable(i122, "" + i122, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), true, null);
        this.flipIcon = rLottieDrawable2;
        rLottieImageView3.setAnimation(rLottieDrawable2);
        this.flipButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$17(view5);
            }
        });
        this.flipButton.setVisibility(8);
        this.buttonsContainer.addView(this.flipButton, LayoutHelper.createFrame(68, 80.0f));
        VoIPToggleButton voIPToggleButton42 = new VoIPToggleButton(context);
        this.leaveButton = voIPToggleButton42;
        voIPToggleButton42.setDrawBackground(false);
        this.leaveButton.setTextSize(12);
        this.leaveButton.setData((this.call == null && isRtmpStream()) ? R.drawable.msg_voiceclose : R.drawable.calls_decline, -1, Theme.getColor(Theme.key_voipgroup_leaveButton), 0.3f, false, LocaleController.getString(R.string.VoipGroupLeave), false, false);
        this.buttonsContainer.addView(this.leaveButton, LayoutHelper.createFrame(68, 80.0f));
        this.leaveButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$18(context, view5);
            }
        });
        RLottieImageView rLottieImageView22 = new RLottieImageView(context) { // from class: org.telegram.ui.GroupCallActivity.19
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName(Button.class.getName());
                accessibilityNodeInfo.setEnabled(GroupCallActivity.this.muteButtonState == 0 || GroupCallActivity.this.muteButtonState == 1);
                if (GroupCallActivity.this.muteButtonState != 1 || Build.VERSION.SDK_INT < 21) {
                    return;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(R.string.VoipMute)));
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (GroupCallActivity.this.isRtmpStream()) {
                    return super.onTouchEvent(motionEvent);
                }
                if (motionEvent.getAction() == 0 && GroupCallActivity.this.muteButtonState == 0) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    if (groupCallActivity.call != null) {
                        AndroidUtilities.runOnUIThread(groupCallActivity.pressRunnable, 300L);
                        GroupCallActivity.this.scheduled = true;
                        return super.onTouchEvent(motionEvent);
                    }
                }
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    if (GroupCallActivity.this.scheduled) {
                        AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.pressRunnable);
                        GroupCallActivity.this.scheduled = false;
                    } else if (GroupCallActivity.this.pressed) {
                        AndroidUtilities.cancelRunOnUIThread(GroupCallActivity.this.unmuteRunnable);
                        GroupCallActivity.this.updateMuteButton(0, true);
                        if (VoIPService.getSharedInstance() != null) {
                            VoIPService.getSharedInstance().setMicMute(true, true, false);
                            GroupCallActivity.this.muteButton.performHapticFeedback(3, 2);
                        }
                        GroupCallActivity.this.attachedRenderersTmp.clear();
                        GroupCallActivity.this.attachedRenderersTmp.addAll(GroupCallActivity.this.attachedRenderers);
                        for (int i132 = 0; i132 < GroupCallActivity.this.attachedRenderersTmp.size(); i132++) {
                            ((GroupCallMiniTextureView) GroupCallActivity.this.attachedRenderersTmp.get(i132)).updateAttachState(true);
                        }
                        GroupCallActivity.this.pressed = false;
                        MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                        super.onTouchEvent(obtain);
                        obtain.recycle();
                        return true;
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.muteButton = rLottieImageView22;
        rLottieImageView22.setAnimation(this.bigMicDrawable);
        this.muteButton.setScaleType(ImageView.ScaleType.CENTER);
        this.buttonsContainer.addView(this.muteButton, LayoutHelper.createFrame(122, 122, 49));
        this.muteButton.setOnClickListener(new 20());
        int dp3 = AndroidUtilities.dp(38.0f);
        ImageView imageView3 = new ImageView(context);
        this.expandButton = imageView3;
        imageView3.setScaleX(0.1f);
        this.expandButton.setScaleY(0.1f);
        this.expandButton.setAlpha(0.0f);
        this.expandButton.setImageResource(R.drawable.voice_expand);
        this.expandButton.setPadding(dp3, dp3, dp3, dp3);
        this.buttonsContainer.addView(this.expandButton, LayoutHelper.createFrame(122, 122, 49));
        ImageView imageView22 = new ImageView(context);
        this.minimizeButton = imageView22;
        imageView22.setScaleX(0.1f);
        this.minimizeButton.setScaleY(0.1f);
        this.minimizeButton.setAlpha(0.0f);
        this.minimizeButton.setImageResource(R.drawable.voice_minimize);
        this.minimizeButton.setPadding(dp3, dp3, dp3, dp3);
        this.buttonsContainer.addView(this.minimizeButton, LayoutHelper.createFrame(122, 122, 49));
        if (this.call != null) {
            float dp22 = AndroidUtilities.dp(52.0f) / ((this.expandButton.getMeasuredWidth() == 0 ? this.expandButton.getLayoutParams().width : r1) - AndroidUtilities.dp(8.0f));
            this.expandButton.setAlpha(1.0f);
            this.expandButton.setScaleX(dp22);
            this.expandButton.setScaleY(dp22);
            this.muteButton.setAlpha(0.0f);
        }
        RadialProgressView radialProgressView2 = new RadialProgressView(context);
        this.radialProgressView = radialProgressView2;
        radialProgressView2.setSize(AndroidUtilities.dp(110.0f));
        this.radialProgressView.setStrokeWidth(4.0f);
        this.radialProgressView.setProgressColor(Theme.getColor(Theme.key_voipgroup_connectingProgress));
        i2 = 0;
        while (i2 < i) {
        }
        this.actionBar.setAlpha(0.0f);
        this.actionBar.getBackButton().setScaleX(0.9f);
        this.actionBar.getBackButton().setScaleY(0.9f);
        this.actionBar.getBackButton().setTranslationX(-AndroidUtilities.dp(14.0f));
        this.actionBar.getTitleTextView().setTranslationY(AndroidUtilities.dp(23.0f));
        this.actionBar.getSubtitleTextView().setTranslationY(AndroidUtilities.dp(20.0f));
        this.actionBar.getAdditionalSubtitleTextView().setTranslationY(AndroidUtilities.dp(20.0f));
        int i132 = Theme.key_voipgroup_actionBarItems;
        ActionBarMenuItem actionBarMenuItem6 = new ActionBarMenuItem(context, null, 0, Theme.getColor(i132));
        this.otherItem = actionBarMenuItem6;
        actionBarMenuItem6.setLongClickEnabled(false);
        this.otherItem.setIcon(R.drawable.ic_ab_other);
        this.otherItem.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
        this.otherItem.setSubMenuOpenSide(2);
        this.otherItem.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i142) {
                GroupCallActivity.this.lambda$new$19(i142);
            }
        });
        ActionBarMenuItem actionBarMenuItem22 = this.otherItem;
        int i142 = Theme.key_voipgroup_actionBarItemsSelector;
        actionBarMenuItem22.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i142), 6));
        this.otherItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$20(view5);
            }
        });
        this.otherItem.setPopupItemsColor(Theme.getColor(i132), false);
        this.otherItem.setPopupItemsColor(Theme.getColor(i132), true);
        ActionBarMenuItem actionBarMenuItem32 = new ActionBarMenuItem(context, null, 0, Theme.getColor(i132));
        this.pipItem = actionBarMenuItem32;
        actionBarMenuItem32.setLongClickEnabled(false);
        this.pipItem.setIcon(R.drawable.msg_voice_pip);
        ActionBarMenuItem actionBarMenuItem42 = this.pipItem;
        int i152 = R.string.AccDescrPipMode;
        actionBarMenuItem42.setContentDescription(LocaleController.getString(i152));
        this.pipItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i142), 6));
        this.pipItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$21(view5);
            }
        });
        ActionBarMenuItem actionBarMenuItem52 = new ActionBarMenuItem(context, null, 0, Theme.getColor(i132));
        this.screenShareItem = actionBarMenuItem52;
        actionBarMenuItem52.setLongClickEnabled(false);
        this.screenShareItem.setIcon(R.drawable.msg_screencast);
        this.screenShareItem.setContentDescription(LocaleController.getString(i152));
        this.screenShareItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(i142), 6));
        this.screenShareItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view5) {
                GroupCallActivity.this.lambda$new$22(view5);
            }
        });
        this.titleTextView = new 21(context, context);
        View view5 = new View(context) { // from class: org.telegram.ui.GroupCallActivity.22
            @Override // android.view.View
            protected void onMeasure(int i162, int i172) {
                setMeasuredDimension(View.MeasureSpec.getSize(i162), ActionBar.getCurrentActionBarHeight());
            }
        };
        this.actionBarBackground = view5;
        view5.setAlpha(0.0f);
        Paint paint32 = new Paint(1);
        this.liveLabelPaint = paint32;
        paint32.setColor(-12761513);
        TextView textView4 = new TextView(getContext()) { // from class: org.telegram.ui.GroupCallActivity.23
            private RectF rect = new RectF();

            @Override // android.widget.TextView, android.view.View
            protected void onDraw(Canvas canvas) {
                this.rect.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), GroupCallActivity.this.liveLabelPaint);
                super.onDraw(canvas);
            }
        };
        this.liveLabelTextView = textView4;
        textView4.setTextColor(getThemedColor(i132));
        this.liveLabelTextView.setTextSize(1, 11.0f);
        this.liveLabelTextView.setText(LocaleController.getString(R.string.VoipChannelLabelLive));
        this.liveLabelTextView.setMaxLines(1);
        this.liveLabelTextView.setGravity(17);
        this.liveLabelTextView.setTypeface(AndroidUtilities.bold());
        this.liveLabelTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(0.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(0.0f));
        this.liveLabelTextView.setTag(-1);
        if (!isRtmpStream()) {
        }
        LinearLayout linearLayout4 = new LinearLayout(getContext());
        this.titleLayout = linearLayout4;
        linearLayout4.setOrientation(0);
        this.titleLayout.addView(this.titleTextView, LayoutHelper.createLinear(0, -2, 1.0f));
        this.titleLayout.addView(this.liveLabelTextView, LayoutHelper.createLinear(-2, 18, 6.0f, 4.0f, 0.0f, 0.0f));
        this.containerView.addView(this.actionBarBackground, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.containerView.addView(this.titleLayout, LayoutHelper.createFrame(-2, -2.0f, 51, 23.0f, 0.0f, 48.0f, 0.0f));
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout22 = new LinearLayout(context);
        this.menuItemsContainer = linearLayout22;
        linearLayout22.setOrientation(0);
        linearLayout22.addView(this.screenShareItem, LayoutHelper.createLinear(48, 48));
        linearLayout22.addView(this.pipItem, LayoutHelper.createLinear(48, 48));
        linearLayout22.addView(this.otherItem, LayoutHelper.createLinear(48, 48));
        this.containerView.addView(linearLayout22, LayoutHelper.createFrame(-2, 48, 53));
        View view22 = new View(context);
        this.actionBarShadow = view22;
        view22.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundColor(Theme.getColor(Theme.key_dialogShadowLine));
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
        i4 = 0;
        while (i4 < i3) {
        }
        AccountSelectCell accountSelectCell3 = new AccountSelectCell(context, true);
        this.accountSelectCell = accountSelectCell3;
        accountSelectCell3.setTag(R.id.width_tag, Integer.valueOf(NotificationCenter.needSetDayNightTheme));
        this.otherItem.addSubItem(8, this.accountSelectCell, -2, AndroidUtilities.dp(48.0f));
        this.otherItem.setShowSubmenuByMove(false);
        AccountSelectCell accountSelectCell22 = this.accountSelectCell;
        int i162 = Theme.key_voipgroup_listSelector;
        accountSelectCell22.setBackground(Theme.createRadSelectorDrawable(Theme.getColor(i162), 6, 6));
        this.accountGap = this.otherItem.addGap(0);
        ActionBarMenuSubItem addSubItem6 = this.otherItem.addSubItem(1, 0, (CharSequence) LocaleController.getString(R.string.VoipGroupAllCanSpeak), true);
        this.everyoneItem = addSubItem6;
        addSubItem6.updateSelectorBackground(true, false);
        ActionBarMenuSubItem addSubItem22 = this.otherItem.addSubItem(2, 0, (CharSequence) LocaleController.getString(R.string.VoipGroupOnlyAdminsCanSpeak), true);
        this.adminItem = addSubItem22;
        addSubItem22.updateSelectorBackground(false, true);
        ActionBarMenuSubItem actionBarMenuSubItem3 = this.everyoneItem;
        int i172 = Theme.key_voipgroup_checkMenu;
        actionBarMenuSubItem3.setCheckColor(i172);
        this.everyoneItem.setColors(Theme.getColor(i172), Theme.getColor(i172));
        this.adminItem.setCheckColor(i172);
        this.adminItem.setColors(Theme.getColor(i172), Theme.getColor(i172));
        Paint paint42 = new Paint(1);
        int i182 = Theme.key_voipgroup_actionBarItems;
        paint42.setColor(Theme.getColor(i182));
        paint42.setStyle(Paint.Style.STROKE);
        paint42.setStrokeWidth(AndroidUtilities.dp(1.5f));
        paint42.setStrokeCap(Paint.Cap.ROUND);
        ActionBarMenuSubItem addSubItem32 = this.otherItem.addSubItem(10, R.drawable.msg_voice_speaker, null, LocaleController.getString(R.string.VoipGroupAudio), true, false);
        this.soundItem = addSubItem32;
        addSubItem32.setItemHeight(56);
        ActionBarMenuSubItem addSubItem42 = this.otherItem.addSubItem(11, R.drawable.msg_noise_on, null, LocaleController.getString(R.string.VoipNoiseCancellation), true, false);
        this.noiseItem = addSubItem42;
        addSubItem42.setItemHeight(56);
        View addDivider2 = this.otherItem.addDivider(ColorUtils.blendARGB(Theme.getColor(Theme.key_voipgroup_actionBar), -16777216, 0.3f));
        this.soundItemDivider = addDivider2;
        ((ViewGroup.MarginLayoutParams) addDivider2.getLayoutParams()).topMargin = 0;
        ((ViewGroup.MarginLayoutParams) this.soundItemDivider.getLayoutParams()).bottomMargin = 0;
        this.editTitleItem = this.otherItem.addSubItem(6, R.drawable.msg_edit, this.recordCallDrawable, LocaleController.getString(!ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelEditTitle : R.string.VoipGroupEditTitle), true, false);
        this.permissionItem = this.otherItem.addSubItem(7, R.drawable.msg_permissions, this.recordCallDrawable, LocaleController.getString(R.string.VoipGroupEditPermissions), false, false);
        this.inviteItem = this.otherItem.addSubItem(3, R.drawable.msg_link, LocaleController.getString(R.string.VoipGroupShareInviteLink));
        this.recordCallDrawable = new RecordCallDrawable();
        this.screenItem = this.otherItem.addSubItem(9, R.drawable.msg_screencast, LocaleController.getString(R.string.VoipChatStartScreenCapture));
        ActionBarMenuSubItem addSubItem52 = this.otherItem.addSubItem(5, 0, this.recordCallDrawable, LocaleController.getString(R.string.VoipGroupRecordCall), true, false);
        this.recordItem = addSubItem52;
        this.recordCallDrawable.setParentView(addSubItem52.getImageView());
        this.leaveItem = this.otherItem.addSubItem(4, R.drawable.msg_endcall, LocaleController.getString(!ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelEndChat : R.string.VoipGroupEndChat));
        this.otherItem.setPopupItemsSelectorColor(Theme.getColor(i162));
        this.otherItem.getPopupLayout().setFitItems(true);
        this.soundItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        this.noiseItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        ActionBarMenuSubItem actionBarMenuSubItem22 = this.leaveItem;
        int i192 = Theme.key_voipgroup_leaveCallMenu;
        actionBarMenuSubItem22.setColors(Theme.getColor(i192), Theme.getColor(i192));
        this.inviteItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        this.editTitleItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        this.permissionItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        this.recordItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        this.screenItem.setColors(Theme.getColor(i182), Theme.getColor(i182));
        if (this.call != null) {
        }
        this.leaveBackgroundPaint.setColor(Theme.getColor(Theme.key_voipgroup_leaveButton));
        updateTitle(false);
        this.actionBar.getTitleTextView().setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view32) {
                GroupCallActivity.this.lambda$new$23(view32);
            }
        });
        this.fullscreenUsersListView = new RecyclerListView(context) { // from class: org.telegram.ui.GroupCallActivity.25
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view32, long j2) {
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) view32;
                if (!GroupCallActivity.this.renderersContainer.isAnimating() && !GroupCallActivity.this.fullscreenListItemAnimator.isRunning()) {
                    groupCallUserCell.setAlpha(1.0f);
                    groupCallUserCell.setTranslationX(0.0f);
                    groupCallUserCell.setTranslationY(0.0f);
                }
                if (groupCallUserCell.isRemoving(GroupCallActivity.this.fullscreenUsersListView) && groupCallUserCell.getRenderer() != null) {
                    return true;
                }
                if (groupCallUserCell.getTranslationY() == 0.0f || groupCallUserCell.getRenderer() == null || groupCallUserCell.getRenderer().primaryView == null) {
                    return super.drawChild(canvas, view32, j2);
                }
                float top = GroupCallActivity.this.listView.getTop() - getTop();
                float f = GroupCallActivity.this.renderersContainer.progressToFullscreenMode;
                canvas.save();
                float f2 = 1.0f - f;
                canvas.clipRect(0.0f, top * f2, getMeasuredWidth(), ((GroupCallActivity.this.listView.getMeasuredHeight() + top) * f2) + (getMeasuredHeight() * f));
                boolean drawChild = super.drawChild(canvas, view32, j2);
                canvas.restore();
                return drawChild;
            }
        };
        DefaultItemAnimator defaultItemAnimator22 = new DefaultItemAnimator() { // from class: org.telegram.ui.GroupCallActivity.26
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                GroupCallActivity.this.listView.invalidate();
                GroupCallActivity.this.renderersContainer.invalidate();
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                GroupCallActivity.this.updateLayout(true);
            }
        };
        this.fullscreenListItemAnimator = defaultItemAnimator22;
        this.fullscreenUsersListView.setClipToPadding(false);
        defaultItemAnimator22.setDelayAnimations(false);
        defaultItemAnimator22.setTranslationInterpolator(CubicBezierInterpolator.DEFAULT);
        defaultItemAnimator22.setRemoveDuration(350L);
        defaultItemAnimator22.setAddDuration(350L);
        defaultItemAnimator22.setMoveDuration(350L);
        this.fullscreenUsersListView.setItemAnimator(defaultItemAnimator22);
        this.fullscreenUsersListView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCallActivity.27
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i202, int i212) {
                super.onScrolled(recyclerView, i202, i212);
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                GroupCallActivity.this.renderersContainer.invalidate();
            }
        });
        this.fullscreenUsersListView.setClipChildren(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(0);
        this.fullscreenUsersListView.setLayoutManager(linearLayoutManager2);
        RecyclerListView recyclerListView52 = this.fullscreenUsersListView;
        GroupCallFullscreenAdapter groupCallFullscreenAdapter2 = new GroupCallFullscreenAdapter(call, this.currentAccount, this);
        this.fullscreenAdapter = groupCallFullscreenAdapter2;
        recyclerListView52.setAdapter(groupCallFullscreenAdapter2);
        this.fullscreenAdapter.setVisibility(this.fullscreenUsersListView, false);
        this.fullscreenUsersListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view32, int i202) {
                GroupCallActivity.this.lambda$new$24(view32, i202);
            }
        });
        this.fullscreenUsersListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda16
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view32, int i202) {
                boolean lambda$new$25;
                lambda$new$25 = GroupCallActivity.this.lambda$new$25(view32, i202);
                return lambda$new$25;
            }
        });
        this.fullscreenUsersListView.setVisibility(8);
        this.fullscreenUsersListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.GroupCallActivity.28
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view32, RecyclerView recyclerView, RecyclerView.State state) {
                recyclerView.getChildAdapterPosition(view32);
                if (GroupCallActivity.isLandscapeMode) {
                    rect.set(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                } else {
                    rect.set(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                }
            }
        });
        29 r62 = new 29(context, this.listView, this.fullscreenUsersListView, this.attachedRenderers, this.call, this);
        this.renderersContainer = r62;
        r62.setClipChildren(false);
        this.fullscreenAdapter.setRenderersPool(this.attachedRenderers, this.renderersContainer);
        if (this.tabletVideoGridView != null) {
        }
        ProfileGalleryView.Callback avatarPreviewPagerIndicator2 = new AvatarPreviewPagerIndicator(context);
        this.avatarPagerIndicator = avatarPreviewPagerIndicator2;
        ProfileGalleryView profileGalleryView2 = new ProfileGalleryView(context, this.actionBar, this.listView, avatarPreviewPagerIndicator2) { // from class: org.telegram.ui.GroupCallActivity.30
            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }
        };
        this.avatarsViewPager = profileGalleryView2;
        profileGalleryView2.setImagesLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
        profileGalleryView2.setInvalidateWithParent(true);
        avatarPreviewPagerIndicator2.setProfileGalleryView(profileGalleryView2);
        FrameLayout frameLayout22 = new FrameLayout(context) { // from class: org.telegram.ui.GroupCallActivity.31
            Rect rect = new Rect();
            RectF rectF = new RectF();
            Path path = new Path();

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                BackupImageView avatarImageView;
                BackupImageView avatarImageView2;
                if (GroupCallActivity.this.progressToAvatarPreview != 1.0f) {
                    if (GroupCallActivity.this.scrimView != null && GroupCallActivity.this.hasScrimAnchorView) {
                        canvas.save();
                        float measuredHeight = (GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2.0f) * (getMeasuredHeight() / GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight());
                        int dp32 = (int) (((1.0f - GroupCallActivity.this.progressToAvatarPreview) * measuredHeight) + (AndroidUtilities.dp(13.0f) * GroupCallActivity.this.progressToAvatarPreview));
                        int i202 = (int) (measuredHeight * (1.0f - GroupCallActivity.this.progressToAvatarPreview));
                        GroupCallActivity.this.scrimView.getAvatarWavesDrawable().draw(canvas, GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2, GroupCallActivity.this.scrimView.getAvatarImageView().getMeasuredHeight() / 2, this);
                        GroupCallActivity.this.scrimView.getAvatarImageView().getImageReceiver().setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        GroupCallActivity.this.scrimView.getAvatarImageView().setRoundRadius(dp32, dp32, i202, i202);
                        GroupCallActivity.this.scrimView.getAvatarImageView().getImageReceiver().draw(canvas);
                        avatarImageView = GroupCallActivity.this.scrimView.getAvatarImageView();
                        avatarImageView2 = GroupCallActivity.this.scrimView.getAvatarImageView();
                    } else if (GroupCallActivity.this.scrimFullscreenView != null && GroupCallActivity.this.scrimRenderer == null && GroupCallActivity.this.previewTextureTransitionEnabled) {
                        canvas.save();
                        float measuredHeight2 = (GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight() / 2.0f) * (getMeasuredHeight() / GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight());
                        int dp4 = (int) (((1.0f - GroupCallActivity.this.progressToAvatarPreview) * measuredHeight2) + (AndroidUtilities.dp(13.0f) * GroupCallActivity.this.progressToAvatarPreview));
                        int i212 = (int) (measuredHeight2 * (1.0f - GroupCallActivity.this.progressToAvatarPreview));
                        GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getImageReceiver().setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().setRoundRadius(dp4, dp4, i212, i212);
                        GroupCallActivity.this.scrimFullscreenView.getAvatarImageView().getImageReceiver().draw(canvas);
                        avatarImageView = GroupCallActivity.this.scrimFullscreenView.getAvatarImageView();
                        avatarImageView2 = GroupCallActivity.this.scrimFullscreenView.getAvatarImageView();
                    }
                    avatarImageView.setRoundRadius(avatarImageView2.getMeasuredHeight() / 2);
                    canvas.restore();
                }
                GroupCallActivity.this.avatarsViewPager.setAlpha(GroupCallActivity.this.progressToAvatarPreview);
                this.path.reset();
                this.rectF.set(0.0f, 0.0f, getMeasuredHeight(), getMeasuredWidth());
                this.path.addRoundRect(this.rectF, new float[]{AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0.0f, 0.0f, 0.0f, 0.0f}, Path.Direction.CCW);
                canvas.save();
                canvas.clipPath(this.path);
                View findVideoActiveView = GroupCallActivity.this.avatarsViewPager.findVideoActiveView();
                if (findVideoActiveView != null && GroupCallActivity.this.scrimRenderer != null && GroupCallActivity.this.scrimRenderer.isAttached() && !GroupCallActivity.this.drawingForBlur) {
                    canvas.save();
                    this.rect.setEmpty();
                    GroupCallActivity.this.avatarsViewPager.getChildVisibleRect(findVideoActiveView, this.rect, null);
                    int i222 = this.rect.left;
                    if (i222 < (-GroupCallActivity.this.avatarsViewPager.getMeasuredWidth())) {
                        i222 += GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() * 2;
                    } else if (i222 > GroupCallActivity.this.avatarsViewPager.getMeasuredWidth()) {
                        i222 -= GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() * 2;
                    }
                    canvas.translate(i222, 0.0f);
                    GroupCallActivity.this.scrimRenderer.draw(canvas);
                    canvas.restore();
                }
                super.dispatchDraw(canvas);
                canvas.restore();
            }

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i202, int i212) {
                int min = Math.min(View.MeasureSpec.getSize(i202), View.MeasureSpec.getSize(i212));
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), View.MeasureSpec.makeMeasureSpec(min + getPaddingBottom(), 1073741824));
            }
        };
        this.avatarPreviewContainer = frameLayout22;
        frameLayout22.setVisibility(8);
        profileGalleryView2.setVisibility(0);
        profileGalleryView2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.GroupCallActivity.32
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i202) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i202, float f, int i212) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i202) {
                GroupCallActivity.this.avatarsViewPager.getRealPosition(i202);
                GroupCallActivity.this.avatarPagerIndicator.saveCurrentPageProgress();
                GroupCallActivity.this.avatarPagerIndicator.invalidate();
            }
        });
        this.blurredView = new View(context) { // from class: org.telegram.ui.GroupCallActivity.33
            @Override // android.view.View
            public void setAlpha(float f) {
                if (getAlpha() != f) {
                    super.setAlpha(f);
                    GroupCallActivity.this.checkContentOverlayed();
                }
            }
        };
        this.containerView.addView(this.renderersContainer);
        this.renderersContainer.addView(this.fullscreenUsersListView, LayoutHelper.createFrame(-1, 80.0f, 80, 0.0f, 0.0f, 0.0f, 100.0f));
        this.buttonsContainer.setWillNotDraw(false);
        View view32 = new View(context);
        this.buttonsBackgroundGradientView = view32;
        int[] iArr2 = this.gradientColors;
        iArr2[0] = this.backgroundColor;
        iArr2[1] = 0;
        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, this.gradientColors);
        this.buttonsBackgroundGradient = gradientDrawable2;
        view32.setBackground(gradientDrawable2);
        this.containerView.addView(view32, LayoutHelper.createFrame(-1, 60, 83));
        View view42 = new View(context);
        this.buttonsBackgroundGradientView2 = view42;
        view42.setBackgroundColor(this.gradientColors[0]);
        this.containerView.addView(view42, LayoutHelper.createFrame(-1, 0, 83));
        this.containerView.addView(this.buttonsContainer, LayoutHelper.createFrame(-1, NotificationCenter.storyQualityUpdate, 81));
        this.containerView.addView(this.blurredView);
        frameLayout22.addView(profileGalleryView2, LayoutHelper.createFrame(-1, -1.0f));
        frameLayout22.addView(avatarPreviewPagerIndicator2, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 0.0f, 0.0f, 0.0f));
        this.containerView.addView(frameLayout22, LayoutHelper.createFrame(-1, -1.0f, 0, 14.0f, 14.0f, 14.0f, 14.0f));
        applyCallParticipantUpdates(false);
        this.listAdapter.notifyDataSetChanged();
        if (isTabletMode) {
        }
        this.oldCount = this.listAdapter.getItemCount();
        if (inputPeer != null) {
        }
        PinchToZoomHelper pinchToZoomHelper2 = new PinchToZoomHelper(Build.VERSION.SDK_INT < 21 ? (ViewGroup) getWindow().getDecorView() : this.containerView, this.containerView) { // from class: org.telegram.ui.GroupCallActivity.38
            @Override // org.telegram.ui.PinchToZoomHelper
            protected void drawOverlays(Canvas canvas, float f, float f2, float f3, float f4, float f5) {
                if (f > 0.0f) {
                    float x = GroupCallActivity.this.avatarPreviewContainer.getX() + ((BottomSheet) GroupCallActivity.this).containerView.getX();
                    float y = GroupCallActivity.this.avatarPreviewContainer.getY() + ((BottomSheet) GroupCallActivity.this).containerView.getY();
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(x, y, GroupCallActivity.this.avatarsViewPager.getMeasuredWidth() + x, GroupCallActivity.this.avatarsViewPager.getMeasuredHeight() + y);
                    canvas.saveLayerAlpha(rectF, (int) (f * 255.0f), 31);
                    canvas.translate(x, y);
                    GroupCallActivity.this.avatarPreviewContainer.draw(canvas);
                    canvas.restore();
                }
            }

            @Override // org.telegram.ui.PinchToZoomHelper
            protected void invalidateViews() {
                super.invalidateViews();
                for (int i25 = 0; i25 < GroupCallActivity.this.avatarsViewPager.getChildCount(); i25++) {
                    GroupCallActivity.this.avatarsViewPager.getChildAt(i25).invalidate();
                }
            }
        };
        this.pinchToZoomHelper = pinchToZoomHelper2;
        pinchToZoomHelper2.setCallback(new PinchToZoomHelper.Callback() { // from class: org.telegram.ui.GroupCallActivity.39
            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public /* synthetic */ TextureView getCurrentTextureView() {
                return PinchToZoomHelper.Callback.-CC.$default$getCurrentTextureView(this);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomFinished(MessageObject messageObject) {
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomStarted(MessageObject messageObject) {
                GroupCallActivity.this.listView.cancelClickRunnables(true);
                GroupCallActivity.this.pinchToZoomHelper.getPhotoImage().setRoundRadius(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0, 0);
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
            }
        });
        profileGalleryView2.setPinchToZoomHelper(this.pinchToZoomHelper);
        this.cameraButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view52) {
                GroupCallActivity.this.lambda$new$35(context, view52);
            }
        });
        updateScheduleUI(false);
        updateItems();
        updateSpeakerPhoneIcon(false);
        updateState(false, false);
        setColorProgress(0.0f);
        updateSubtitle();
    }

    static /* synthetic */ float access$11216(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.amplitude + f;
        groupCallActivity.amplitude = f2;
        return f2;
    }

    static /* synthetic */ float access$13716(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.switchProgress + f;
        groupCallActivity.switchProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$14316(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.showWavesProgress + f;
        groupCallActivity.showWavesProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$14324(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.showWavesProgress - f;
        groupCallActivity.showWavesProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$14416(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.showLightingProgress + f;
        groupCallActivity.showLightingProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$14424(GroupCallActivity groupCallActivity, float f) {
        float f2 = groupCallActivity.showLightingProgress - f;
        groupCallActivity.showLightingProgress = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:113:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x020c A[LOOP:2: B:94:0x0204->B:96:0x020c, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void applyCallParticipantUpdates(boolean z) {
        int i;
        int i2;
        GroupCallRenderersContainer groupCallRenderersContainer;
        ChatObject.Call call;
        boolean z2;
        ChatObject.VideoParticipant videoParticipant;
        ChatObject.VideoParticipant videoParticipant2;
        RecyclerView.ViewHolder findContainingViewHolder;
        UpdateCallback updateCallback;
        GroupCallRenderersContainer groupCallRenderersContainer2 = this.renderersContainer;
        if (groupCallRenderersContainer2 == null || this.listView == null || this.call == null || this.delayedGroupCallUpdated) {
            return;
        }
        if (groupCallRenderersContainer2.inFullscreenMode) {
            groupCallRenderersContainer2.setVisibleParticipant(true);
        }
        long peerId = MessageObject.getPeerId(this.call.selfPeer);
        if (peerId != MessageObject.getPeerId(this.selfPeer) && this.call.participants.get(peerId) != null) {
            this.selfPeer = this.call.selfPeer;
        }
        int childCount = this.listView.getChildCount();
        int i3 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        View view = null;
        int i4 = 0;
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = this.listView.getChildAt(i5);
            RecyclerView.ViewHolder findContainingViewHolder2 = this.listView.findContainingViewHolder(childAt);
            if (findContainingViewHolder2 != null && findContainingViewHolder2.getAdapterPosition() != -1 && findContainingViewHolder2.getLayoutPosition() != -1 && (view == null || childAt.getTop() < i3)) {
                i4 = findContainingViewHolder2.getLayoutPosition();
                i3 = childAt.getTop();
                view = childAt;
            }
        }
        updateVideoParticipantList();
        if (this.listView.getItemAnimator() != null && !z) {
            this.listView.setItemAnimator(null);
        } else if (this.listView.getItemAnimator() == null && z) {
            this.listView.setItemAnimator(this.itemAnimator);
        }
        try {
            updateCallback = new UpdateCallback(this.listAdapter);
            i = i4;
        } catch (Exception e) {
            e = e;
            i = i4;
        }
        try {
            setOldRows(this.listAdapter.addMemberRow, this.listAdapter.usersStartRow, this.listAdapter.usersEndRow, this.listAdapter.invitedStartRow, this.listAdapter.invitedEndRow, this.listAdapter.usersVideoGridStartRow, this.listAdapter.usersVideoGridEndRow, this.listAdapter.videoGridDividerRow, this.listAdapter.videoNotAvailableRow);
            this.listAdapter.updateRows();
            DiffUtil.calculateDiff(this.diffUtilsCallback).dispatchUpdatesTo(updateCallback);
        } catch (Exception e2) {
            e = e2;
            FileLog.e(e);
            this.listAdapter.notifyDataSetChanged();
            this.call.saveActiveDates();
            if (view != null) {
            }
            this.oldParticipants.clear();
            this.oldParticipants.addAll(this.call.visibleParticipants);
            this.oldVideoParticipants.clear();
            this.oldVideoParticipants.addAll(this.visibleVideoParticipants);
            this.oldInvited.clear();
            this.oldInvited.addAll(this.call.invitedUsers);
            this.oldCount = this.listAdapter.getItemCount();
            while (i2 < childCount) {
            }
            boolean autoPinEnabled = this.renderersContainer.autoPinEnabled();
            groupCallRenderersContainer = this.renderersContainer;
            if (groupCallRenderersContainer.inFullscreenMode) {
                if (!this.visibleVideoParticipants.isEmpty()) {
                }
            }
            this.fullscreenAdapter.update(true, this.fullscreenUsersListView);
            if (this.fullscreenUsersListView.getVisibility() == 0) {
            }
            if (isTabletMode) {
            }
            if (this.listView.getVisibility() == 0) {
            }
            this.attachedRenderersTmp.clear();
            this.attachedRenderersTmp.addAll(this.attachedRenderers);
            while (r15 < this.attachedRenderersTmp.size()) {
            }
            call = this.call;
            if (call != null) {
            }
            z2 = !this.call.visibleVideoParticipants.isEmpty();
            if (z2 == this.hasVideo) {
            }
        }
        this.call.saveActiveDates();
        if (view != null) {
            this.layoutManager.scrollToPositionWithOffset(i, view.getTop() - this.listView.getPaddingTop());
        }
        this.oldParticipants.clear();
        this.oldParticipants.addAll(this.call.visibleParticipants);
        this.oldVideoParticipants.clear();
        this.oldVideoParticipants.addAll(this.visibleVideoParticipants);
        this.oldInvited.clear();
        this.oldInvited.addAll(this.call.invitedUsers);
        this.oldCount = this.listAdapter.getItemCount();
        for (i2 = 0; i2 < childCount; i2++) {
            View childAt2 = this.listView.getChildAt(i2);
            boolean z3 = childAt2 instanceof GroupCallUserCell;
            if ((z3 || (childAt2 instanceof GroupCallInvitedCell)) && (findContainingViewHolder = this.listView.findContainingViewHolder(childAt2)) != null) {
                if (z3) {
                    ((GroupCallUserCell) childAt2).setDrawDivider(findContainingViewHolder.getAdapterPosition() != this.listAdapter.getItemCount() + (-2));
                } else {
                    ((GroupCallInvitedCell) childAt2).setDrawDivider(findContainingViewHolder.getAdapterPosition() != this.listAdapter.getItemCount() + (-2));
                }
            }
        }
        boolean autoPinEnabled2 = this.renderersContainer.autoPinEnabled();
        groupCallRenderersContainer = this.renderersContainer;
        if (groupCallRenderersContainer.inFullscreenMode && (videoParticipant2 = groupCallRenderersContainer.fullscreenParticipant) != null && !ChatObject.Call.videoIsActive(videoParticipant2.participant, videoParticipant2.presentation, this.call)) {
            if (!this.visibleVideoParticipants.isEmpty()) {
                fullscreenFor(null);
            } else if (autoPinEnabled2) {
                fullscreenFor((ChatObject.VideoParticipant) this.visibleVideoParticipants.get(0));
            }
        }
        this.fullscreenAdapter.update(true, this.fullscreenUsersListView);
        if (this.fullscreenUsersListView.getVisibility() == 0) {
            AndroidUtilities.updateVisibleRows(this.fullscreenUsersListView);
        }
        if (isTabletMode) {
            this.tabletGridAdapter.update(true, this.tabletVideoGridView);
        }
        if (this.listView.getVisibility() == 0) {
            AndroidUtilities.updateVisibleRows(this.listView);
        }
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        for (int i6 = 0; i6 < this.attachedRenderersTmp.size(); i6++) {
            ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i6)).updateAttachState(true);
        }
        call = this.call;
        if (call != null) {
            GroupCallRenderersContainer groupCallRenderersContainer3 = this.renderersContainer;
            if (groupCallRenderersContainer3.inFullscreenMode && (videoParticipant = groupCallRenderersContainer3.fullscreenParticipant) != null) {
                call.participants.get(MessageObject.getPeerId(videoParticipant.participant.peer));
            }
        }
        z2 = !this.call.visibleVideoParticipants.isEmpty();
        if (z2 == this.hasVideo) {
            this.hasVideo = z2;
            if (isTabletMode) {
                this.containerView.requestLayout();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachRenderer(GroupCallGridCell groupCallGridCell, boolean z) {
        if (isDismissed()) {
            return;
        }
        if (z && groupCallGridCell.getRenderer() == null) {
            groupCallGridCell.setRenderer(GroupCallMiniTextureView.getOrCreate(this.attachedRenderers, this.renderersContainer, groupCallGridCell, null, null, groupCallGridCell.getParticipant(), this.call, this));
        } else {
            if (z || groupCallGridCell.getRenderer() == null) {
                return;
            }
            groupCallGridCell.getRenderer().setPrimaryView(null);
            groupCallGridCell.setRenderer(null);
        }
    }

    private void cancelMutePress() {
        if (this.scheduled) {
            this.scheduled = false;
            AndroidUtilities.cancelRunOnUIThread(this.pressRunnable);
        }
        if (this.pressed) {
            this.pressed = false;
            MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
            this.muteButton.onTouchEvent(obtain);
            obtain.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkContentOverlayed() {
        boolean z = !this.avatarPriviewTransitionInProgress && this.blurredView.getVisibility() == 0 && this.blurredView.getAlpha() == 1.0f;
        if (this.contentFullyOverlayed != z) {
            this.contentFullyOverlayed = z;
            this.buttonsContainer.invalidate();
            this.containerView.invalidate();
            this.listView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearScrimView() {
        GroupCallMiniTextureView groupCallMiniTextureView = this.scrimRenderer;
        if (groupCallMiniTextureView != null) {
            groupCallMiniTextureView.textureView.setRoundCorners(AndroidUtilities.dp(8.0f));
            this.scrimRenderer.setShowingAsScrimView(false, false);
            this.scrimRenderer.invalidate();
            this.renderersContainer.invalidate();
        }
        GroupCallUserCell groupCallUserCell = this.scrimView;
        if (groupCallUserCell != null && !this.hasScrimAnchorView && groupCallUserCell.getParent() != null) {
            this.containerView.removeView(this.scrimView);
        }
        GroupCallUserCell groupCallUserCell2 = this.scrimView;
        if (groupCallUserCell2 != null) {
            groupCallUserCell2.setProgressToAvatarPreview(0.0f);
            this.scrimView.setAboutVisible(false);
            this.scrimView.getAvatarImageView().setAlpha(1.0f);
        }
        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell3 = this.scrimFullscreenView;
        if (groupCallUserCell3 != null) {
            groupCallUserCell3.getAvatarImageView().setAlpha(1.0f);
        }
        this.scrimView = null;
        this.scrimGridView = null;
        this.scrimFullscreenView = null;
        this.scrimRenderer = null;
    }

    public static void create(LaunchActivity launchActivity, AccountInstance accountInstance, TLRPC.Chat chat, TLRPC.InputPeer inputPeer, boolean z, String str) {
        TLRPC.Chat chat2;
        if (groupCallInstance == null) {
            if (inputPeer == null && VoIPService.getSharedInstance() == null) {
                return;
            }
            if (inputPeer != null) {
                groupCallInstance = new GroupCallActivity(launchActivity, accountInstance, accountInstance.getMessagesController().getGroupCall(chat.id, false), chat, inputPeer, z, str);
            } else {
                ChatObject.Call call = VoIPService.getSharedInstance().groupCall;
                if (call == null || (chat2 = accountInstance.getMessagesController().getChat(Long.valueOf(call.chatId))) == null) {
                    return;
                }
                call.addSelfDummyParticipant(true);
                groupCallInstance = new GroupCallActivity(launchActivity, accountInstance, call, chat2, null, z, str);
            }
            groupCallInstance.parentActivity = launchActivity;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.lambda$create$9();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissAvatarPreview(boolean z) {
        if (this.avatarPriviewTransitionInProgress || !this.avatarsPreviewShowed) {
            return;
        }
        if (z) {
            this.avatarPriviewTransitionInProgress = true;
            runAvatarPreviewTransition(false, this.scrimView);
            return;
        }
        clearScrimView();
        this.containerView.removeView(this.scrimPopupLayout);
        this.scrimPopupLayout = null;
        this.avatarPreviewContainer.setVisibility(8);
        this.containerView.invalidate();
        this.avatarsPreviewShowed = false;
        this.layoutManager.setCanScrollVertically(true);
        this.listView.invalidate();
        this.blurredView.setVisibility(8);
        if (this.delayedGroupCallUpdated) {
            this.delayedGroupCallUpdated = false;
            applyCallParticipantUpdates(true);
        }
        checkContentOverlayed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillColors(int i, int[] iArr) {
        if (isRtmpStream()) {
            int i2 = Theme.key_voipgroup_disabledButton;
            iArr[0] = Theme.getColor(i2);
            iArr[1] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_rtmpButton), Theme.getColor(Theme.key_voipgroup_soundButtonActiveScrolled), this.colorProgress, 1.0f);
            iArr[2] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_listViewBackgroundUnscrolled), Theme.getColor(i2), this.colorProgress, 1.0f);
            return;
        }
        if (i == 0) {
            iArr[0] = Theme.getColor(Theme.key_voipgroup_unmuteButton2);
            iArr[1] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_soundButtonActive), Theme.getColor(Theme.key_voipgroup_soundButtonActiveScrolled), this.colorProgress, 1.0f);
            iArr[2] = Theme.getColor(Theme.key_voipgroup_soundButton);
        } else if (i == 1) {
            iArr[0] = Theme.getColor(Theme.key_voipgroup_muteButton2);
            iArr[1] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_soundButtonActive2), Theme.getColor(Theme.key_voipgroup_soundButtonActive2Scrolled), this.colorProgress, 1.0f);
            iArr[2] = Theme.getColor(Theme.key_voipgroup_soundButton2);
        } else if (isGradientState(i)) {
            iArr[0] = Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient3);
            iArr[1] = Theme.getColor(Theme.key_voipgroup_mutedByAdminMuteButton);
            iArr[2] = Theme.getColor(Theme.key_voipgroup_mutedByAdminMuteButtonDisabled);
        } else {
            int i3 = Theme.key_voipgroup_disabledButton;
            iArr[0] = Theme.getColor(i3);
            iArr[1] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_disabledButtonActive), Theme.getColor(Theme.key_voipgroup_disabledButtonActiveScrolled), this.colorProgress, 1.0f);
            iArr[2] = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_listViewBackgroundUnscrolled), Theme.getColor(i3), this.colorProgress, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GroupCallGridCell findGroupCallGridCell() {
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if ((childAt instanceof GroupCallGridCell) && this.listView.getChildAdapterPosition(childAt) >= 0) {
                return (GroupCallGridCell) childAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getColorProgress() {
        return this.colorProgress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getLink(final boolean z) {
        String str;
        TLRPC.TL_chatInviteExported tL_chatInviteExported;
        TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(this.currentChat.id));
        if (chat == null || ChatObject.isPublic(chat)) {
            if (this.call == null) {
                return;
            }
            final int i = 0;
            while (i < 2) {
                TLRPC.TL_phone_exportGroupCallInvite tL_phone_exportGroupCallInvite = new TLRPC.TL_phone_exportGroupCallInvite();
                tL_phone_exportGroupCallInvite.call = this.call.getInputGroupCall();
                tL_phone_exportGroupCallInvite.can_self_unmute = i == 1;
                this.accountInstance.getConnectionsManager().sendRequest(tL_phone_exportGroupCallInvite, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda36
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        GroupCallActivity.this.lambda$getLink$42(i, z, tLObject, tL_error);
                    }
                });
                i++;
            }
            return;
        }
        final TLRPC.ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(this.currentChat.id);
        String publicUsername = ChatObject.getPublicUsername(this.currentChat);
        if (TextUtils.isEmpty(publicUsername)) {
            str = (chatFull == null || (tL_chatInviteExported = chatFull.exported_invite) == null) ? null : tL_chatInviteExported.link;
        } else {
            str = this.accountInstance.getMessagesController().linkPrefix + "/" + publicUsername;
        }
        if (!TextUtils.isEmpty(str)) {
            openShareAlert(true, null, str, z);
            return;
        }
        TLRPC.TL_messages_exportChatInvite tL_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
        tL_messages_exportChatInvite.peer = MessagesController.getInputPeer(this.currentChat);
        this.accountInstance.getConnectionsManager().sendRequest(tL_messages_exportChatInvite, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda35
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                GroupCallActivity.this.lambda$getLink$40(chatFull, z, tLObject, tL_error);
            }
        });
    }

    private void initCreatedGroupCall() {
        VoIPService sharedInstance;
        if (this.callInitied || (sharedInstance = VoIPService.getSharedInstance()) == null) {
            return;
        }
        this.callInitied = true;
        this.oldParticipants.addAll(this.call.visibleParticipants);
        this.oldVideoParticipants.addAll(this.visibleVideoParticipants);
        this.oldInvited.addAll(this.call.invitedUsers);
        this.currentCallState = sharedInstance.getCallState();
        if (this.call == null) {
            ChatObject.Call call = sharedInstance.groupCall;
            this.call = call;
            this.fullscreenAdapter.setGroupCall(call);
            this.renderersContainer.setGroupCall(this.call);
            this.tabletGridAdapter.setGroupCall(this.call);
        }
        this.actionBar.setTitleRightMargin(AndroidUtilities.dp(48.0f) * 2);
        this.call.saveActiveDates();
        VoIPService.getSharedInstance().registerStateListener(this);
        SimpleTextView simpleTextView = this.scheduleTimeTextView;
        if (simpleTextView == null || simpleTextView.getVisibility() != 0) {
            return;
        }
        this.leaveButton.setData(isRtmpStream() ? R.drawable.msg_voiceclose : R.drawable.calls_decline, -1, Theme.getColor(Theme.key_voipgroup_leaveButton), 0.3f, false, LocaleController.getString(R.string.VoipGroupLeave), false, true);
        updateSpeakerPhoneIcon(true);
        this.leaveItem.setText(LocaleController.getString(ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelEndChat : R.string.VoipGroupEndChat));
        this.listView.setVisibility(0);
        this.pipItem.setVisibility(0);
        AnimatorSet animatorSet = new AnimatorSet();
        RecyclerListView recyclerListView = this.listView;
        Property property = View.ALPHA;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(recyclerListView, (Property<RecyclerListView, Float>) property, 0.0f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.listView, (Property<RecyclerListView, Float>) View.TRANSLATION_Y, AndroidUtilities.dp(200.0f), 0.0f);
        SimpleTextView simpleTextView2 = this.scheduleTimeTextView;
        Property property2 = View.SCALE_X;
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(simpleTextView2, (Property<SimpleTextView, Float>) property2, 0.0f);
        SimpleTextView simpleTextView3 = this.scheduleTimeTextView;
        Property property3 = View.SCALE_Y;
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ObjectAnimator.ofFloat(simpleTextView3, (Property<SimpleTextView, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.scheduleTimeTextView, (Property<SimpleTextView, Float>) property, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartInTextView, (Property<SimpleTextView, Float>) property2, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartInTextView, (Property<SimpleTextView, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartInTextView, (Property<SimpleTextView, Float>) property, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartAtTextView, (Property<SimpleTextView, Float>) property2, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartAtTextView, (Property<SimpleTextView, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.scheduleStartAtTextView, (Property<SimpleTextView, Float>) property, 0.0f), ObjectAnimator.ofFloat(this.pipItem, (Property<ActionBarMenuItem, Float>) property2, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.pipItem, (Property<ActionBarMenuItem, Float>) property3, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.pipItem, (Property<ActionBarMenuItem, Float>) property, 0.0f, 1.0f));
        animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.47
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                GroupCallActivity.this.scheduleTimeTextView.setVisibility(4);
                GroupCallActivity.this.scheduleStartAtTextView.setVisibility(4);
                GroupCallActivity.this.scheduleStartInTextView.setVisibility(4);
            }
        });
        animatorSet.setDuration(300L);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateLayoutFullscreen() {
        int i;
        if (isRtmpStream()) {
            boolean z = (!this.renderersContainer.isUiVisible() && this.renderersContainer.inFullscreenMode && (isLandscapeMode == isRtmpLandscapeMode() || AndroidUtilities.isTablet())) ? false : true;
            Boolean bool = this.wasNotInLayoutFullscreen;
            if (bool == null || z != bool.booleanValue()) {
                int systemUiVisibility = this.containerView.getSystemUiVisibility();
                if (z) {
                    i = systemUiVisibility & (-7);
                    getWindow().clearFlags(1024);
                    setHideSystemVerticalInsets(false);
                } else {
                    setHideSystemVerticalInsets(true);
                    i = systemUiVisibility | 6;
                    getWindow().addFlags(1024);
                }
                this.containerView.setSystemUiVisibility(i);
                this.wasNotInLayoutFullscreen = Boolean.valueOf(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void inviteUserToCall(final long j, final boolean z) {
        final TLRPC.User user;
        if (this.call == null || (user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j))) == null) {
            return;
        }
        final AlertDialog[] alertDialogArr = {new AlertDialog(getContext(), 3)};
        final TLRPC.TL_phone_inviteToGroupCall tL_phone_inviteToGroupCall = new TLRPC.TL_phone_inviteToGroupCall();
        tL_phone_inviteToGroupCall.call = this.call.getInputGroupCall();
        TLRPC.TL_inputUser tL_inputUser = new TLRPC.TL_inputUser();
        tL_inputUser.user_id = user.id;
        tL_inputUser.access_hash = user.access_hash;
        tL_phone_inviteToGroupCall.users.add(tL_inputUser);
        final int sendRequest = this.accountInstance.getConnectionsManager().sendRequest(tL_phone_inviteToGroupCall, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda62
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                GroupCallActivity.this.lambda$inviteUserToCall$47(j, alertDialogArr, user, z, tL_phone_inviteToGroupCall, tLObject, tL_error);
            }
        });
        if (sendRequest != 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda63
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.this.lambda$inviteUserToCall$49(alertDialogArr, sendRequest);
                }
            }, 500L);
        }
    }

    public static boolean isGradientState(int i) {
        return !(VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().groupCall == null || !VoIPService.getSharedInstance().groupCall.call.rtmp_stream) || i == 2 || i == 4 || i == 5 || i == 6 || i == 7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStillConnecting() {
        int i = this.currentCallState;
        return i == 1 || i == 2 || i == 6 || i == 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$create$9() {
        GroupCallActivity groupCallActivity = groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$2() {
        if (isStillConnecting()) {
            updateState(true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$3(DialogInterface dialogInterface) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fullscreenFor$36(ArrayList arrayList) {
        for (int i = 0; i < this.attachedRenderers.size(); i++) {
            if (((GroupCallMiniTextureView) this.attachedRenderers.get(i)).participant != null) {
                arrayList.remove(((GroupCallMiniTextureView) this.attachedRenderers.get(i)).participant);
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ChatObject.VideoParticipant videoParticipant = (ChatObject.VideoParticipant) arrayList.get(i2);
            if (videoParticipant.participant.self) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().setLocalSink(null, videoParticipant.presentation);
                }
            } else if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().removeRemoteSink(videoParticipant.participant, videoParticipant.presentation);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLink$39(TLObject tLObject, TLRPC.ChatFull chatFull, boolean z) {
        if (tLObject instanceof TLRPC.TL_chatInviteExported) {
            TLRPC.TL_chatInviteExported tL_chatInviteExported = (TLRPC.TL_chatInviteExported) tLObject;
            if (chatFull != null) {
                chatFull.exported_invite = tL_chatInviteExported;
            } else {
                openShareAlert(true, null, tL_chatInviteExported.link, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLink$40(final TLRPC.ChatFull chatFull, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.lambda$getLink$39(tLObject, chatFull, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLink$41(TLObject tLObject, int i, boolean z) {
        if (tLObject instanceof TLRPC.TL_phone_exportedGroupCallInvite) {
            this.invites[i] = ((TLRPC.TL_phone_exportedGroupCallInvite) tLObject).link;
        } else {
            this.invites[i] = "";
        }
        for (int i2 = 0; i2 < 2; i2++) {
            String str = this.invites[i2];
            if (str == null) {
                return;
            }
            if (str.length() == 0) {
                this.invites[i2] = null;
            }
        }
        if (!z && ChatObject.canManageCalls(this.currentChat) && !this.call.call.join_muted) {
            this.invites[0] = null;
        }
        String[] strArr = this.invites;
        if (strArr[0] != null || strArr[1] != null || !ChatObject.isPublic(this.currentChat)) {
            String[] strArr2 = this.invites;
            openShareAlert(false, strArr2[0], strArr2[1], z);
            return;
        }
        openShareAlert(true, null, this.accountInstance.getMessagesController().linkPrefix + "/" + ChatObject.getPublicUsername(this.currentChat), z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getLink$42(final int i, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.lambda$getLink$41(tLObject, i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inviteUserToCall$45(long j, AlertDialog[] alertDialogArr, TLRPC.User user) {
        ChatObject.Call call = this.call;
        if (call == null || this.delayedGroupCallUpdated) {
            return;
        }
        call.addInvitedUser(j);
        applyCallParticipantUpdates(true);
        GroupVoipInviteAlert groupVoipInviteAlert = this.groupVoipInviteAlert;
        if (groupVoipInviteAlert != null) {
            groupVoipInviteAlert.dismiss();
        }
        try {
            alertDialogArr[0].dismiss();
        } catch (Throwable unused) {
        }
        alertDialogArr[0] = null;
        getUndoView().showWithAction(0L, 34, user, this.currentChat, (Runnable) null, (Runnable) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inviteUserToCall$46(AlertDialog[] alertDialogArr, boolean z, TLRPC.TL_error tL_error, long j, TLRPC.TL_phone_inviteToGroupCall tL_phone_inviteToGroupCall) {
        try {
            alertDialogArr[0].dismiss();
        } catch (Throwable unused) {
        }
        alertDialogArr[0] = null;
        if (z && "USER_NOT_PARTICIPANT".equals(tL_error.text)) {
            processSelectedOption(null, j, 3);
        } else {
            AlertsCreator.processError(this.currentAccount, tL_error, (BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1), tL_phone_inviteToGroupCall, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inviteUserToCall$47(final long j, final AlertDialog[] alertDialogArr, final TLRPC.User user, final boolean z, final TLRPC.TL_phone_inviteToGroupCall tL_phone_inviteToGroupCall, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda67
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.this.lambda$inviteUserToCall$46(alertDialogArr, z, tL_error, j, tL_phone_inviteToGroupCall);
                }
            });
        } else {
            this.accountInstance.getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda66
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.this.lambda$inviteUserToCall$45(j, alertDialogArr, user);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inviteUserToCall$48(int i, DialogInterface dialogInterface) {
        this.accountInstance.getConnectionsManager().cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inviteUserToCall$49(AlertDialog[] alertDialogArr, final int i) {
        AlertDialog alertDialog = alertDialogArr[0];
        if (alertDialog == null) {
            return;
        }
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda65
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                GroupCallActivity.this.lambda$inviteUserToCall$48(i, dialogInterface);
            }
        });
        alertDialogArr[0].show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$makeFocusable$7(BottomSheet bottomSheet, final EditTextBoldCursor editTextBoldCursor, boolean z, AlertDialog alertDialog) {
        Runnable runnable;
        if (bottomSheet != null && !bottomSheet.isDismissed()) {
            bottomSheet.setFocusable(true);
            editTextBoldCursor.requestFocus();
            if (!z) {
                return;
            } else {
                runnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda60
                    @Override // java.lang.Runnable
                    public final void run() {
                        AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
                    }
                };
            }
        } else {
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            alertDialog.setFocusable(true);
            editTextBoldCursor.requestFocus();
            if (!z) {
                return;
            } else {
                runnable = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
                    }
                };
            }
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$makeFocusable$8(EditTextBoldCursor editTextBoldCursor) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0() {
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService.getSharedInstance().setMicMute(false, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (this.call == null || !this.scheduled || VoIPService.getSharedInstance() == null) {
            return;
        }
        this.muteButton.performHapticFeedback(3, 2);
        updateMuteButton(1, true);
        AndroidUtilities.runOnUIThread(this.unmuteRunnable, 80L);
        this.scheduled = false;
        this.pressed = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(DialogInterface dialogInterface) {
        BaseFragment baseFragment = (BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1);
        if (this.anyEnterEventSent && (baseFragment instanceof ChatActivity)) {
            ((ChatActivity) baseFragment).onEditTextDialogClose(true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(int[] iArr, float[] fArr, boolean[] zArr) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        for (int i = 0; i < iArr.length; i++) {
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.call.participantsBySources.get(iArr[i]);
            if (tL_groupCallParticipant != null) {
                if (this.renderersContainer.inFullscreenMode) {
                    for (int i2 = 0; i2 < this.fullscreenUsersListView.getChildCount(); i2++) {
                        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) this.fullscreenUsersListView.getChildAt(i2);
                        if (MessageObject.getPeerId(groupCallUserCell.getParticipant().peer) == MessageObject.getPeerId(tL_groupCallParticipant.peer)) {
                            groupCallUserCell.setAmplitude(fArr[i] * 15.0f);
                        }
                    }
                } else {
                    int indexOf = (this.delayedGroupCallUpdated ? this.oldParticipants : this.call.visibleParticipants).indexOf(tL_groupCallParticipant);
                    if (indexOf >= 0 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(indexOf + this.listAdapter.usersStartRow)) != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (view instanceof GroupCallUserCell) {
                            ((GroupCallUserCell) view).setAmplitude(fArr[i] * 15.0f);
                            if (findViewHolderForAdapterPosition.itemView == this.scrimView && !this.contentFullyOverlayed) {
                                this.containerView.invalidate();
                            }
                        }
                    }
                }
                this.renderersContainer.setAmplitude(tL_groupCallParticipant, fArr[i] * 15.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12(DialogInterface dialogInterface) {
        this.groupVoipInviteAlert = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$13(View view, int i, float f, float f2) {
        if (view instanceof GroupCallGridCell) {
            fullscreenFor(((GroupCallGridCell) view).getParticipant());
            return;
        }
        if (view instanceof GroupCallUserCell) {
            showMenuForCell((GroupCallUserCell) view);
            return;
        }
        if (view instanceof GroupCallInvitedCell) {
            GroupCallInvitedCell groupCallInvitedCell = (GroupCallInvitedCell) view;
            if (groupCallInvitedCell.getUser() == null) {
                return;
            }
            this.parentActivity.switchToAccount(this.currentAccount, true);
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", groupCallInvitedCell.getUser().id);
            if (groupCallInvitedCell.hasAvatarSet()) {
                bundle.putBoolean("expandPhoto", true);
            }
            this.parentActivity.lambda$runLinkRequest$93(new ProfileActivity(bundle));
            dismiss();
            return;
        }
        if (i == this.listAdapter.addMemberRow) {
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC.Chat chat = this.currentChat;
                if (!chat.megagroup && ChatObject.isPublic(chat)) {
                    getLink(false);
                    return;
                }
            }
            TLRPC.ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(this.currentChat.id);
            if (chatFull == null) {
                return;
            }
            this.enterEventSent = false;
            Context context = getContext();
            int currentAccount = this.accountInstance.getCurrentAccount();
            TLRPC.Chat chat2 = this.currentChat;
            ChatObject.Call call = this.call;
            GroupVoipInviteAlert groupVoipInviteAlert = new GroupVoipInviteAlert(context, currentAccount, chat2, chatFull, call.participants, call.invitedUsersMap);
            this.groupVoipInviteAlert = groupVoipInviteAlert;
            groupVoipInviteAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda42
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    GroupCallActivity.this.lambda$new$12(dialogInterface);
                }
            });
            this.groupVoipInviteAlert.setDelegate(new GroupVoipInviteAlert.GroupVoipInviteAlertDelegate() { // from class: org.telegram.ui.GroupCallActivity.14
                @Override // org.telegram.ui.Components.GroupVoipInviteAlert.GroupVoipInviteAlertDelegate
                public void copyInviteLink() {
                    GroupCallActivity.this.getLink(true);
                }

                @Override // org.telegram.ui.Components.GroupVoipInviteAlert.GroupVoipInviteAlertDelegate
                public void inviteUser(long j) {
                    GroupCallActivity.this.inviteUserToCall(j, true);
                }

                @Override // org.telegram.ui.Components.GroupVoipInviteAlert.GroupVoipInviteAlertDelegate
                public void needOpenSearch(MotionEvent motionEvent, EditTextBoldCursor editTextBoldCursor) {
                    GroupCallActivity groupCallActivity;
                    GroupVoipInviteAlert groupVoipInviteAlert2;
                    boolean z;
                    if (GroupCallActivity.this.enterEventSent) {
                        return;
                    }
                    if (motionEvent.getX() <= editTextBoldCursor.getLeft() || motionEvent.getX() >= editTextBoldCursor.getRight() || motionEvent.getY() <= editTextBoldCursor.getTop() || motionEvent.getY() >= editTextBoldCursor.getBottom()) {
                        groupCallActivity = GroupCallActivity.this;
                        groupVoipInviteAlert2 = groupCallActivity.groupVoipInviteAlert;
                        z = false;
                    } else {
                        groupCallActivity = GroupCallActivity.this;
                        groupVoipInviteAlert2 = groupCallActivity.groupVoipInviteAlert;
                        z = true;
                    }
                    groupCallActivity.makeFocusable(groupVoipInviteAlert2, null, editTextBoldCursor, z);
                }
            });
            this.groupVoipInviteAlert.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$14(View view, int i) {
        if (isRtmpStream()) {
            return false;
        }
        if (view instanceof GroupCallGridCell) {
            return showMenuForCell(view);
        }
        if (!(view instanceof GroupCallUserCell)) {
            return false;
        }
        updateItems();
        return ((GroupCallUserCell) view).clickMuteButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$15(View view, int i) {
        GroupCallGridCell groupCallGridCell = (GroupCallGridCell) view;
        if (groupCallGridCell.getParticipant() != null) {
            fullscreenFor(groupCallGridCell.getParticipant());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$16(View view) {
        ChatObject.Call call = this.call;
        if (call == null || call.isScheduled() || isRtmpStream()) {
            getLink(false);
        } else {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            VoIPService.getSharedInstance().toggleSpeakerphoneOrShowRouteSheet(getContext(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$17(View view) {
        RLottieDrawable rLottieDrawable;
        this.renderersContainer.delayHideUi();
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            if (sharedInstance.getVideoState(false) == 2) {
                sharedInstance.switchCamera();
                int i = 18;
                if (this.flipIconCurrentEndFrame == 18) {
                    rLottieDrawable = this.flipIcon;
                    i = 39;
                } else {
                    this.flipIcon.setCurrentFrame(0, false);
                    rLottieDrawable = this.flipIcon;
                }
                this.flipIconCurrentEndFrame = i;
                rLottieDrawable.setCustomEndFrame(i);
                this.flipIcon.start();
                for (int i2 = 0; i2 < this.attachedRenderers.size(); i2++) {
                    GroupCallMiniTextureView groupCallMiniTextureView = (GroupCallMiniTextureView) this.attachedRenderers.get(i2);
                    ChatObject.VideoParticipant videoParticipant = groupCallMiniTextureView.participant;
                    if (videoParticipant.participant.self && !videoParticipant.presentation) {
                        groupCallMiniTextureView.startFlipAnimation();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$18(Context context, View view) {
        this.renderersContainer.delayHideUi();
        ChatObject.Call call = this.call;
        if (call == null || call.isScheduled()) {
            dismiss();
        } else {
            updateItems();
            onLeaveClick(context, new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.this.dismiss();
                }
            }, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$19(int i) {
        this.actionBar.getActionBarMenuOnItemClick().onItemClick(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$20(View view) {
        ActionBarMenuSubItem actionBarMenuSubItem;
        int i;
        String string;
        ChatObject.Call call = this.call;
        if (call == null || this.renderersContainer.inFullscreenMode) {
            return;
        }
        if (call.call.join_muted) {
            ActionBarMenuSubItem actionBarMenuSubItem2 = this.everyoneItem;
            int i2 = Theme.key_voipgroup_actionBarItems;
            actionBarMenuSubItem2.setColors(Theme.getColor(i2), Theme.getColor(i2));
            this.everyoneItem.setChecked(false);
            ActionBarMenuSubItem actionBarMenuSubItem3 = this.adminItem;
            int i3 = Theme.key_voipgroup_checkMenu;
            actionBarMenuSubItem3.setColors(Theme.getColor(i3), Theme.getColor(i3));
            this.adminItem.setChecked(true);
        } else {
            ActionBarMenuSubItem actionBarMenuSubItem4 = this.everyoneItem;
            int i4 = Theme.key_voipgroup_checkMenu;
            actionBarMenuSubItem4.setColors(Theme.getColor(i4), Theme.getColor(i4));
            this.everyoneItem.setChecked(true);
            ActionBarMenuSubItem actionBarMenuSubItem5 = this.adminItem;
            int i5 = Theme.key_voipgroup_actionBarItems;
            actionBarMenuSubItem5.setColors(Theme.getColor(i5), Theme.getColor(i5));
            this.adminItem.setChecked(false);
        }
        this.changingPermissions = false;
        this.otherItem.hideSubItem(1);
        this.otherItem.hideSubItem(2);
        if (VoIPService.getSharedInstance() != null && (VoIPService.getSharedInstance().hasEarpiece() || VoIPService.getSharedInstance().isBluetoothHeadsetConnected())) {
            int currentAudioRoute = VoIPService.getSharedInstance().getCurrentAudioRoute();
            if (currentAudioRoute == 2) {
                this.soundItem.setIcon(R.drawable.msg_voice_bluetooth);
                actionBarMenuSubItem = this.soundItem;
                if (VoIPService.getSharedInstance().currentBluetoothDeviceName != null) {
                    string = VoIPService.getSharedInstance().currentBluetoothDeviceName;
                    actionBarMenuSubItem.setSubtext(string);
                } else {
                    i = R.string.VoipAudioRoutingBluetooth;
                }
            } else if (currentAudioRoute == 0) {
                this.soundItem.setIcon(VoIPService.getSharedInstance().isHeadsetPlugged() ? R.drawable.msg_voice_headphones : R.drawable.msg_voice_phone);
                actionBarMenuSubItem = this.soundItem;
                i = VoIPService.getSharedInstance().isHeadsetPlugged() ? R.string.VoipAudioRoutingHeadset : R.string.VoipAudioRoutingPhone;
            } else if (currentAudioRoute == 1) {
                if (VoipAudioManager.get().isSpeakerphoneOn()) {
                    this.soundItem.setIcon(R.drawable.msg_voice_speaker);
                    actionBarMenuSubItem = this.soundItem;
                    i = R.string.VoipAudioRoutingSpeaker;
                } else {
                    this.soundItem.setIcon(R.drawable.msg_voice_phone);
                    actionBarMenuSubItem = this.soundItem;
                    i = R.string.VoipAudioRoutingPhone;
                }
            }
            string = LocaleController.getString(i);
            actionBarMenuSubItem.setSubtext(string);
        }
        updateItems();
        this.otherItem.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$21(View view) {
        if (isRtmpStream()) {
            if (!AndroidUtilities.checkInlinePermissions(this.parentActivity)) {
                AlertsCreator.createDrawOverlayPermissionDialog(this.parentActivity, null).show();
                return;
            } else {
                RTMPStreamPipOverlay.show();
                dismiss();
                return;
            }
        }
        if (!AndroidUtilities.checkInlinePermissions(this.parentActivity)) {
            AlertsCreator.createDrawOverlayGroupCallPermissionDialog(getContext()).show();
        } else {
            GroupCallPip.clearForce();
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$22(View view) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        if (sharedInstance.getVideoState(true) == 2) {
            sharedInstance.stopScreenCapture();
        } else {
            startScreenCapture();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$23(View view) {
        ChatObject.Call call = this.call;
        if (call == null || !call.recording) {
            return;
        }
        showRecordHint(this.actionBar.getTitleTextView());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$24(View view, int i) {
        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) view;
        if (groupCallUserCell.getVideoParticipant() == null) {
            fullscreenFor(new ChatObject.VideoParticipant(groupCallUserCell.getParticipant(), false, false));
        } else {
            fullscreenFor(groupCallUserCell.getVideoParticipant());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$25(View view, int i) {
        if (showMenuForCell(view)) {
            this.listView.performHapticFeedback(0);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$26(ValueAnimator valueAnimator) {
        this.switchToButtonProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateScheduleUI(true);
        this.buttonsContainer.invalidate();
        this.listView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$27(TLRPC.Chat chat, TLRPC.InputPeer inputPeer, TLRPC.TL_updateGroupCall tL_updateGroupCall) {
        ChatObject.Call call = new ChatObject.Call();
        this.call = call;
        call.call = new TLRPC.TL_groupCall();
        ChatObject.Call call2 = this.call;
        TLRPC.GroupCall groupCall = call2.call;
        groupCall.participants_count = 0;
        groupCall.version = 1;
        groupCall.can_start_video = true;
        groupCall.can_change_join_muted = true;
        call2.chatId = chat.id;
        groupCall.schedule_date = this.scheduleStartAt;
        groupCall.flags |= 128;
        call2.currentAccount = this.accountInstance;
        call2.setSelfPeer(inputPeer);
        ChatObject.Call call3 = this.call;
        TLRPC.GroupCall groupCall2 = call3.call;
        TLRPC.GroupCall groupCall3 = tL_updateGroupCall.call;
        groupCall2.access_hash = groupCall3.access_hash;
        groupCall2.id = groupCall3.id;
        call3.createNoVideoParticipant();
        this.fullscreenAdapter.setGroupCall(this.call);
        this.renderersContainer.setGroupCall(this.call);
        this.tabletGridAdapter.setGroupCall(this.call);
        MessagesController messagesController = this.accountInstance.getMessagesController();
        ChatObject.Call call4 = this.call;
        messagesController.putGroupCall(call4.chatId, call4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$28(TLRPC.TL_error tL_error) {
        this.accountInstance.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 6, tL_error.text);
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$29(final TLRPC.Chat chat, final TLRPC.InputPeer inputPeer, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.this.lambda$new$28(tL_error);
                }
            });
            return;
        }
        TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        int i = 0;
        while (true) {
            if (i >= updates.updates.size()) {
                break;
            }
            TLRPC.Update update = updates.updates.get(i);
            if (update instanceof TLRPC.TL_updateGroupCall) {
                final TLRPC.TL_updateGroupCall tL_updateGroupCall = (TLRPC.TL_updateGroupCall) update;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda50
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.this.lambda$new$27(chat, inputPeer, tL_updateGroupCall);
                    }
                });
                break;
            }
            i++;
        }
        this.accountInstance.getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$30(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, final TLRPC.Chat chat, AccountInstance accountInstance, final TLRPC.InputPeer inputPeer, View view) {
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher;
        int i;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.scheduleAnimator = ofFloat;
        ofFloat.setDuration(600L);
        this.scheduleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda33
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                GroupCallActivity.this.lambda$new$26(valueAnimator);
            }
        });
        this.scheduleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.36
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                GroupCallActivity.this.scheduleAnimator = null;
            }
        });
        this.scheduleAnimator.start();
        if (ChatObject.isChannelOrGiga(this.currentChat)) {
            clippingTextViewSwitcher = this.titleTextView;
            i = R.string.VoipChannelVoiceChat;
        } else {
            clippingTextViewSwitcher = this.titleTextView;
            i = R.string.VoipGroupVoiceChat;
        }
        clippingTextViewSwitcher.setText(LocaleController.getString(i), true);
        Calendar calendar = Calendar.getInstance();
        boolean checkScheduleDate = AlertsCreator.checkScheduleDate(null, null, 604800L, 3, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 86400000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        this.scheduleStartAt = (int) (calendar.getTimeInMillis() / 1000);
        updateScheduleUI(false);
        TLRPC.TL_phone_createGroupCall tL_phone_createGroupCall = new TLRPC.TL_phone_createGroupCall();
        tL_phone_createGroupCall.peer = MessagesController.getInputPeer(chat);
        tL_phone_createGroupCall.random_id = Utilities.random.nextInt();
        tL_phone_createGroupCall.schedule_date = this.scheduleStartAt;
        tL_phone_createGroupCall.flags |= 2;
        accountInstance.getConnectionsManager().sendRequest(tL_phone_createGroupCall, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda34
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                GroupCallActivity.this.lambda$new$29(chat, inputPeer, tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$new$31(long j, Calendar calendar, int i, int i2) {
        if (i2 == 0) {
            return LocaleController.getString(R.string.MessageScheduleToday);
        }
        long j2 = j + (i2 * 86400000);
        calendar.setTimeInMillis(j2);
        if (calendar.get(1) != i) {
            return LocaleController.getInstance().getFormatterScheduleYear().format(j2);
        }
        return LocaleController.getInstance().getFormatterWeek().format(j2) + " " + LocaleController.getInstance().getFormatterScheduleDay().format(j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$32(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i, int i2) {
        try {
            this.container.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        AlertsCreator.checkScheduleDate(this.scheduleButtonTextView, this.scheduleInfoTextView, 604800L, 2, numberPicker, numberPicker2, numberPicker3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$new$33(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$new$34(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$35(Context context, View view) {
        LaunchActivity launchActivity;
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT >= 23 && (launchActivity = this.parentActivity) != null) {
            checkSelfPermission = launchActivity.checkSelfPermission("android.permission.CAMERA");
            if (checkSelfPermission != 0) {
                this.parentActivity.requestPermissions(new String[]{"android.permission.CAMERA"}, 104);
                return;
            }
        }
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        boolean z = true;
        if (VoIPService.getSharedInstance().getVideoState(false) == 2) {
            VoIPService.getSharedInstance().setVideoState(false, 0);
            updateState(true, false);
            updateSpeakerPhoneIcon(false);
            this.call.sortParticipants();
            applyCallParticipantUpdates(true);
            this.buttonsContainer.requestLayout();
            return;
        }
        this.undoView[0].hide(false, 1);
        if (this.previewDialog == null) {
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            if (sharedInstance != null) {
                sharedInstance.createCaptureDevice(false);
            }
            PrivateVideoPreviewDialog privateVideoPreviewDialog = new PrivateVideoPreviewDialog(context, z, VoIPService.getSharedInstance().getVideoState(true) != 2) { // from class: org.telegram.ui.GroupCallActivity.40
                @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialog
                public void onDismiss(boolean z2, boolean z3) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    boolean z4 = groupCallActivity.previewDialog.micEnabled;
                    groupCallActivity.previewDialog = null;
                    VoIPService sharedInstance2 = VoIPService.getSharedInstance();
                    if (!z3) {
                        if (sharedInstance2 != null) {
                            sharedInstance2.setVideoState(false, 0);
                            return;
                        }
                        return;
                    }
                    if (sharedInstance2 != null) {
                        sharedInstance2.setupCaptureDevice(z2, z4);
                    }
                    if (z2 && sharedInstance2 != null) {
                        sharedInstance2.setVideoState(false, 0);
                    }
                    GroupCallActivity.this.updateState(true, false);
                    GroupCallActivity.this.call.sortParticipants();
                    GroupCallActivity.this.applyCallParticipantUpdates(true);
                    GroupCallActivity.this.buttonsContainer.requestLayout();
                }
            };
            this.previewDialog = privateVideoPreviewDialog;
            this.container.addView(privateVideoPreviewDialog);
            if (sharedInstance == null || sharedInstance.isFrontFaceCamera()) {
                return;
            }
            sharedInstance.switchCamera();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onLeaveClick$54(CheckBoxCell[] checkBoxCellArr, View view) {
        checkBoxCellArr[((Integer) view.getTag()).intValue()].setChecked(!checkBoxCellArr[r2.intValue()].isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onLeaveClick$55(ChatObject.Call call, CheckBoxCell[] checkBoxCellArr, long j, Runnable runnable, DialogInterface dialogInterface, int i) {
        processOnLeave(call, checkBoxCellArr[0].isChecked(), j, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openShareAlert$43(DialogInterface dialogInterface) {
        this.shareAlert = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openShareAlert$44() {
        ShareAlert shareAlert = this.shareAlert;
        if (shareAlert != null) {
            shareAlert.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$56(TLObject tLObject, DialogInterface dialogInterface, int i) {
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            this.accountInstance.getMessagesController().deleteParticipantFromChat(this.currentChat.id, user);
            getUndoView().showWithAction(0L, 32, user, (Object) null, (Runnable) null, (Runnable) null);
        } else {
            TLRPC.Chat chat = (TLRPC.Chat) tLObject;
            this.accountInstance.getMessagesController().deleteParticipantFromChat(this.currentChat.id, (TLRPC.User) null, chat, false, false);
            getUndoView().showWithAction(0L, 32, chat, (Object) null, (Runnable) null, (Runnable) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$57(long j) {
        inviteUserToCall(j, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$58(TLRPC.User user, final long j, DialogInterface dialogInterface, int i) {
        this.accountInstance.getMessagesController().addUserToChat(this.currentChat.id, user, 0, null, (BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1), new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.lambda$processSelectedOption$57(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedOption$59() {
        this.accountInstance.getMessagesController().deleteUserPhoto(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processSelectedOption$60(DialogInterface dialogInterface) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runAvatarPreviewTransition$63(float f, float f2, float f3, int i, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.progressToAvatarPreview = floatValue;
        this.renderersContainer.progressToScrimView = floatValue;
        float f4 = (f * (1.0f - floatValue)) + (floatValue * 1.0f);
        this.avatarPreviewContainer.setScaleX(f4);
        this.avatarPreviewContainer.setScaleY(f4);
        this.avatarPreviewContainer.setTranslationX(f2 * (1.0f - this.progressToAvatarPreview));
        this.avatarPreviewContainer.setTranslationY(f3 * (1.0f - this.progressToAvatarPreview));
        if (!this.useBlur) {
            this.scrimPaint.setAlpha((int) (this.progressToAvatarPreview * 100.0f));
        }
        GroupCallMiniTextureView groupCallMiniTextureView = this.scrimRenderer;
        if (groupCallMiniTextureView != null) {
            groupCallMiniTextureView.textureView.setRoundCorners(AndroidUtilities.dp(8.0f) * (1.0f - this.progressToAvatarPreview));
        }
        this.avatarPreviewContainer.invalidate();
        this.containerView.invalidate();
        ProfileGalleryView profileGalleryView = this.avatarsViewPager;
        int i2 = (int) (i * (1.0f - this.progressToAvatarPreview));
        profileGalleryView.setRoundRadius(i2, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showMenuForCell$61(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.scrimPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.scrimPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showMenuForCell$62(int i, ArrayList arrayList, TLRPC.TL_groupCallParticipant tL_groupCallParticipant, View view) {
        if (i >= arrayList.size()) {
            return;
        }
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(tL_groupCallParticipant.peer));
        if (tL_groupCallParticipant2 != null) {
            tL_groupCallParticipant = tL_groupCallParticipant2;
        }
        processSelectedOption(tL_groupCallParticipant, MessageObject.getPeerId(tL_groupCallParticipant.peer), ((Integer) arrayList.get(i)).intValue());
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        } else {
            if (((Integer) arrayList.get(i)).intValue() == 9 || ((Integer) arrayList.get(i)).intValue() == 10 || ((Integer) arrayList.get(i)).intValue() == 11) {
                return;
            }
            dismissAvatarPreview(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleAdminSpeak$64(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            this.accountInstance.getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateItems$4() {
        updateRecordCallText();
        AndroidUtilities.runOnUIThread(this.updateCallRecordRunnable, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLiveLabel$50(int i, int i2, boolean z, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.liveLabelPaint.setColor(AndroidUtilities.getOffsetColor(i, i2, floatValue, 1.0f));
        this.liveLabelTextView.invalidate();
        if (!z) {
            floatValue = 1.0f - floatValue;
        }
        this.textureLightningViewAnimatingAlpha = floatValue;
        LightningView lightningView = this.textureLightningView;
        if (lightningView == null || !this.needTextureLightning) {
            return;
        }
        lightningView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMuteButton$51(float f, View view, ValueAnimator valueAnimator) {
        if (!isLandscapeMode && !isRtmpStream()) {
            f = AndroidUtilities.lerp(1.0f, f, this.renderersContainer.progressToFullscreenMode);
        }
        view.setScaleY(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMuteButton$52(float f, View view, View view2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!isLandscapeMode && !isRtmpStream()) {
            f = AndroidUtilities.lerp(1.0f, f, this.renderersContainer.progressToFullscreenMode);
        }
        float f2 = 1.0f - floatValue;
        view.setAlpha(f2);
        float f3 = ((f2 * 0.9f) + 0.1f) * f;
        view.setScaleX(f3);
        view.setScaleY(f3);
        view2.setAlpha(floatValue);
        float f4 = ((floatValue * 0.9f) + 0.1f) * f;
        view2.setScaleX(f4);
        view2.setScaleY(f4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMuteButton$53(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.muteLabel[0].setAlpha(1.0f - floatValue);
        this.muteLabel[0].setTranslationY(AndroidUtilities.dp(5.0f) * floatValue);
        this.muteLabel[1].setAlpha(floatValue);
        this.muteLabel[1].setTranslationY(AndroidUtilities.dp((floatValue * 5.0f) - 5.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTitle$37(View view) {
        ChatObject.Call call = this.call;
        if (call == null || !call.recording) {
            return;
        }
        showRecordHint(this.actionBar.getTitleTextView());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTitle$38(View view) {
        ChatObject.Call call = this.call;
        if (call == null || !call.recording) {
            return;
        }
        showRecordHint(this.actionBar.getTitleTextView());
    }

    public static void onLeaveClick(Context context, final Runnable runnable, boolean z) {
        int i;
        CheckBoxCell checkBoxCell;
        int i2;
        Window window;
        int i3;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        TLRPC.Chat chat = sharedInstance.getChat();
        final ChatObject.Call call = sharedInstance.groupCall;
        final long selfId = sharedInstance.getSelfId();
        if (!ChatObject.canManageCalls(chat)) {
            processOnLeave(call, false, selfId, runnable);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (ChatObject.isChannelOrGiga(chat)) {
            builder.setTitle(LocaleController.getString(R.string.VoipChannelLeaveAlertTitle));
            i = R.string.VoipChannelLeaveAlertText;
        } else {
            builder.setTitle(LocaleController.getString(R.string.VoipGroupLeaveAlertTitle));
            i = R.string.VoipGroupLeaveAlertText;
        }
        builder.setMessage(LocaleController.getString(i));
        sharedInstance.getAccount();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        final CheckBoxCell[] checkBoxCellArr = {new CheckBoxCell(context, 1)};
        checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (z) {
            checkBoxCellArr[0].setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        } else {
            checkBoxCellArr[0].setTextColor(Theme.getColor(Theme.key_voipgroup_actionBarItems));
            ((CheckBoxSquare) checkBoxCellArr[0].getCheckBoxView()).setColors(Theme.key_voipgroup_mutedIcon, Theme.key_voipgroup_listeningText, Theme.key_voipgroup_nameText);
        }
        checkBoxCellArr[0].setTag(0);
        if (ChatObject.isChannelOrGiga(chat)) {
            checkBoxCell = checkBoxCellArr[0];
            i2 = R.string.VoipChannelLeaveAlertEndChat;
        } else {
            checkBoxCell = checkBoxCellArr[0];
            i2 = R.string.VoipGroupLeaveAlertEndChat;
        }
        checkBoxCell.setText(LocaleController.getString(i2), "", false, false);
        checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        linearLayout.addView(checkBoxCellArr[0], LayoutHelper.createLinear(-1, -2));
        checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda39
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCallActivity.lambda$onLeaveClick$54(checkBoxCellArr, view);
            }
        });
        builder.setView(linearLayout);
        builder.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
        builder.setPositiveButton(LocaleController.getString(R.string.VoipGroupLeave), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda40
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                GroupCallActivity.lambda$onLeaveClick$55(ChatObject.Call.this, checkBoxCellArr, selfId, runnable, dialogInterface, i4);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        if (z) {
            builder.setDimEnabled(false);
        }
        AlertDialog create = builder.create();
        if (z) {
            if (Build.VERSION.SDK_INT >= 26) {
                window = create.getWindow();
                i3 = 2038;
            } else {
                window = create.getWindow();
                i3 = 2003;
            }
            window.setType(i3);
            create.getWindow().clearFlags(2);
        }
        if (!z) {
            create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
        }
        create.show();
        if (z) {
            return;
        }
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_voipgroup_leaveCallMenu));
        }
        create.setTextColor(Theme.getColor(Theme.key_voipgroup_actionBarItems));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserLeaveHint() {
        if (isRtmpStream() && AndroidUtilities.checkInlinePermissions(this.parentActivity) && !RTMPStreamPipOverlay.isVisible()) {
            dismiss();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    RTMPStreamPipOverlay.show();
                }
            }, 100L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openShareAlert(boolean z, String str, String str2, boolean z2) {
        boolean z3;
        String str3;
        String str4;
        String str5;
        String str6 = (!isRtmpStream() || str == null) ? str2 : null;
        if (z2) {
            if (str != null) {
                str6 = str;
            }
            AndroidUtilities.addToClipboard(str6);
            if (AndroidUtilities.shouldShowClipboardToast()) {
                getUndoView().showWithAction(0L, 33, (Object) null, (Object) null, (Runnable) null, (Runnable) null);
                return;
            }
            return;
        }
        LaunchActivity launchActivity = this.parentActivity;
        if (launchActivity != null) {
            BaseFragment baseFragment = (BaseFragment) launchActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1);
            if (baseFragment instanceof ChatActivity) {
                boolean needEnterText = ((ChatActivity) baseFragment).needEnterText();
                this.anyEnterEventSent = true;
                this.enterEventSent = true;
                z3 = needEnterText;
                if (str == null && str6 == null) {
                    str4 = str;
                    str3 = null;
                } else {
                    str3 = str;
                    str4 = str6;
                }
                if (str3 == null || !z) {
                    str5 = str4;
                } else {
                    str5 = ChatObject.isChannelOrGiga(this.currentChat) ? LocaleController.formatString("VoipChannelInviteText", R.string.VoipChannelInviteText, str4) : LocaleController.formatString("VoipGroupInviteText", R.string.VoipGroupInviteText, str4);
                }
                ShareAlert shareAlert = new ShareAlert(getContext(), null, null, str5, str3, false, str4, str3, false, true) { // from class: org.telegram.ui.GroupCallActivity.48
                    @Override // org.telegram.ui.Components.ShareAlert
                    protected void onSend(LongSparseArray longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic) {
                        if (longSparseArray.size() == 1) {
                            GroupCallActivity.this.getUndoView().showWithAction(((TLRPC.Dialog) longSparseArray.valueAt(0)).id, 41, Integer.valueOf(i));
                        } else {
                            GroupCallActivity.this.getUndoView().showWithAction(0L, 41, Integer.valueOf(i), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                        }
                    }
                };
                this.shareAlert = shareAlert;
                shareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.GroupCallActivity.49
                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public boolean didCopy() {
                        if (!AndroidUtilities.shouldShowClipboardToast()) {
                            return true;
                        }
                        GroupCallActivity.this.getUndoView().showWithAction(0L, 33, (Object) null, (Object) null, (Runnable) null, (Runnable) null);
                        return true;
                    }

                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public /* synthetic */ void didShare() {
                        ShareAlert.ShareAlertDelegate.-CC.$default$didShare(this);
                    }
                });
                this.shareAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda52
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        GroupCallActivity.this.lambda$openShareAlert$43(dialogInterface);
                    }
                });
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda53
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.this.lambda$openShareAlert$44();
                    }
                }, !z3 ? 200L : 0L);
            }
        }
        z3 = false;
        if (str == null) {
        }
        str3 = str;
        str4 = str6;
        if (str3 == null) {
        }
        str5 = str4;
        ShareAlert shareAlert2 = new ShareAlert(getContext(), null, null, str5, str3, false, str4, str3, false, true) { // from class: org.telegram.ui.GroupCallActivity.48
            @Override // org.telegram.ui.Components.ShareAlert
            protected void onSend(LongSparseArray longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic) {
                if (longSparseArray.size() == 1) {
                    GroupCallActivity.this.getUndoView().showWithAction(((TLRPC.Dialog) longSparseArray.valueAt(0)).id, 41, Integer.valueOf(i));
                } else {
                    GroupCallActivity.this.getUndoView().showWithAction(0L, 41, Integer.valueOf(i), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                }
            }
        };
        this.shareAlert = shareAlert2;
        shareAlert2.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.GroupCallActivity.49
            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public boolean didCopy() {
                if (!AndroidUtilities.shouldShowClipboardToast()) {
                    return true;
                }
                GroupCallActivity.this.getUndoView().showWithAction(0L, 33, (Object) null, (Object) null, (Runnable) null, (Runnable) null);
                return true;
            }

            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public /* synthetic */ void didShare() {
                ShareAlert.ShareAlertDelegate.-CC.$default$didShare(this);
            }
        });
        this.shareAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda52
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                GroupCallActivity.this.lambda$openShareAlert$43(dialogInterface);
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallActivity.this.lambda$openShareAlert$44();
            }
        }, !z3 ? 200L : 0L);
    }

    private void prepareBlurBitmap() {
        if (this.blurredView == null) {
            return;
        }
        int measuredWidth = (int) ((this.containerView.getMeasuredWidth() - (this.backgroundPaddingLeft * 2)) / 6.0f);
        int measuredHeight = (int) ((this.containerView.getMeasuredHeight() - AndroidUtilities.statusBarHeight) / 6.0f);
        Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.16666667f, 0.16666667f);
        canvas.save();
        canvas.translate(0.0f, -AndroidUtilities.statusBarHeight);
        this.parentActivity.getActionBarLayout().getView().draw(canvas);
        canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, 76));
        canvas.restore();
        canvas.save();
        canvas.translate(this.containerView.getX(), -AndroidUtilities.statusBarHeight);
        this.drawingForBlur = true;
        this.containerView.draw(canvas);
        this.drawingForBlur = false;
        Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / NotificationCenter.updateBotMenuButton));
        this.blurredView.setBackground(new BitmapDrawable(createBitmap));
        this.blurredView.setAlpha(0.0f);
        this.blurredView.setVisibility(0);
    }

    private static void processOnLeave(ChatObject.Call call, boolean z, long j, Runnable runnable) {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().hangUp(z ? 1 : 0);
        }
        if (call != null) {
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) call.participants.get(j);
            if (tL_groupCallParticipant != null) {
                call.participants.delete(j);
                call.sortedParticipants.remove(tL_groupCallParticipant);
                call.visibleParticipants.remove(tL_groupCallParticipant);
                int i = 0;
                while (i < call.visibleVideoParticipants.size()) {
                    if (MessageObject.getPeerId(call.visibleVideoParticipants.get(i).participant.peer) == MessageObject.getPeerId(tL_groupCallParticipant.peer)) {
                        call.visibleVideoParticipants.remove(i);
                        i--;
                    }
                    i++;
                }
                TLRPC.GroupCall groupCall = call.call;
                groupCall.participants_count--;
            }
            for (int i2 = 0; i2 < call.sortedParticipants.size(); i2++) {
                TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = call.sortedParticipants.get(i2);
                tL_groupCallParticipant2.lastActiveDate = tL_groupCallParticipant2.lastSpeakTime;
            }
        }
        if (runnable != null) {
            runnable.run();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didStartedCall, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSelectedOption(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, final long j, int i) {
        String str;
        String formatString;
        TextView textView;
        Boolean bool;
        Boolean bool2;
        Runnable runnable;
        Boolean bool3;
        Integer num;
        LaunchActivity launchActivity;
        ChatActivity chatActivity;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        final TLObject user = j > 0 ? this.accountInstance.getMessagesController().getUser(Long.valueOf(j)) : this.accountInstance.getMessagesController().getChat(Long.valueOf(-j));
        if (i == 0 || i == 2 || i == 3) {
            if (i == 0) {
                if (VoIPService.getSharedInstance() == null) {
                    return;
                }
                VoIPService.getSharedInstance().editCallMember(user, Boolean.TRUE, null, null, null, null);
                getUndoView().showWithAction(0L, 30, user, (Object) null, (Runnable) null, (Runnable) null);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setDialogButtonColorKey(Theme.key_voipgroup_listeningText);
            TextView textView2 = new TextView(getContext());
            int i2 = Theme.key_voipgroup_actionBarItems;
            textView2.setTextColor(Theme.getColor(i2));
            textView2.setTextSize(1, 16.0f);
            textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            FrameLayout frameLayout = new FrameLayout(getContext());
            builder.setView(frameLayout);
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            BackupImageView backupImageView = new BackupImageView(getContext());
            backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
            avatarDrawable.setInfo(this.currentAccount, user);
            boolean z = user instanceof TLRPC.User;
            if (z) {
                TLRPC.User user2 = (TLRPC.User) user;
                backupImageView.setForUserOrChat(user2, avatarDrawable);
                str = UserObject.getFirstName(user2);
            } else {
                TLRPC.Chat chat = (TLRPC.Chat) user;
                backupImageView.setForUserOrChat(chat, avatarDrawable);
                str = chat.title;
            }
            TextView textView3 = new TextView(getContext());
            textView3.setTextColor(Theme.getColor(i2));
            textView3.setTextSize(1, 20.0f);
            textView3.setTypeface(AndroidUtilities.bold());
            textView3.setLines(1);
            textView3.setMaxLines(1);
            textView3.setSingleLine(true);
            textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            textView3.setEllipsize(TextUtils.TruncateAt.END);
            if (i == 2) {
                textView3.setText(LocaleController.getString(R.string.VoipGroupRemoveMemberAlertTitle2));
                formatString = ChatObject.isChannelOrGiga(this.currentChat) ? LocaleController.formatString("VoipChannelRemoveMemberAlertText2", R.string.VoipChannelRemoveMemberAlertText2, str, this.currentChat.title) : LocaleController.formatString("VoipGroupRemoveMemberAlertText2", R.string.VoipGroupRemoveMemberAlertText2, str, this.currentChat.title);
            } else {
                textView3.setText(LocaleController.getString(R.string.VoipGroupAddMemberTitle));
                formatString = LocaleController.formatString("VoipGroupAddMemberText", R.string.VoipGroupAddMemberText, str, this.currentChat.title);
            }
            textView2.setText(AndroidUtilities.replaceTags(formatString));
            boolean z2 = LocaleController.isRTL;
            frameLayout.addView(textView3, LayoutHelper.createFrame(-1, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 21 : 76, 11.0f, z2 ? 76 : 21, 0.0f));
            frameLayout.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
            if (i == 2) {
                builder.setPositiveButton(LocaleController.getString(R.string.VoipGroupUserRemove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda58
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        GroupCallActivity.this.lambda$processSelectedOption$56(user, dialogInterface, i3);
                    }
                });
            } else if (z) {
                final TLRPC.User user3 = (TLRPC.User) user;
                builder.setPositiveButton(LocaleController.getString(R.string.VoipGroupAdd), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda59
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        GroupCallActivity.this.lambda$processSelectedOption$58(user3, j, dialogInterface, i3);
                    }
                });
            }
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog create = builder.create();
            create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
            create.show();
            if (i != 2 || (textView = (TextView) create.getButton(-1)) == null) {
                return;
            }
            textView.setTextColor(Theme.getColor(Theme.key_voipgroup_leaveCallMenu));
            return;
        }
        if (i == 6) {
            this.parentActivity.switchToAccount(this.currentAccount, true);
            Bundle bundle = new Bundle();
            if (j > 0) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            launchActivity = this.parentActivity;
            chatActivity = new ChatActivity(bundle);
        } else {
            if (i != 8) {
                if (i == 7) {
                    sharedInstance.editCallMember(user, Boolean.TRUE, null, null, Boolean.FALSE, null);
                    updateMuteButton(2, true);
                    return;
                }
                if (i == 9) {
                    ImageUpdater imageUpdater = this.currentAvatarUpdater;
                    if (imageUpdater == null || !imageUpdater.isUploadingImage()) {
                        TLRPC.User currentUser = this.accountInstance.getUserConfig().getCurrentUser();
                        ImageUpdater imageUpdater2 = new ImageUpdater(true, 0, true);
                        this.currentAvatarUpdater = imageUpdater2;
                        imageUpdater2.setOpenWithFrontfaceCamera(true);
                        this.currentAvatarUpdater.setForceDarkTheme(true);
                        this.currentAvatarUpdater.setSearchAvailable(true, true);
                        this.currentAvatarUpdater.setShowingFromDialog(true);
                        this.currentAvatarUpdater.parentFragment = this.parentActivity.getActionBarLayout().getLastFragment();
                        ImageUpdater imageUpdater3 = this.currentAvatarUpdater;
                        AvatarUpdaterDelegate avatarUpdaterDelegate = new AvatarUpdaterDelegate(j);
                        this.avatarUpdaterDelegate = avatarUpdaterDelegate;
                        imageUpdater3.setDelegate(avatarUpdaterDelegate);
                        ImageUpdater imageUpdater4 = this.currentAvatarUpdater;
                        TLRPC.UserProfilePhoto userProfilePhoto = currentUser.photo;
                        imageUpdater4.openMenu((userProfilePhoto == null || userProfilePhoto.photo_big == null || (userProfilePhoto instanceof TLRPC.TL_userProfilePhotoEmpty)) ? false : true, new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda56
                            @Override // java.lang.Runnable
                            public final void run() {
                                GroupCallActivity.this.lambda$processSelectedOption$59();
                            }
                        }, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda57
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                GroupCallActivity.lambda$processSelectedOption$60(dialogInterface);
                            }
                        }, 0);
                        return;
                    }
                    return;
                }
                if (i == 10) {
                    AlertsCreator.createChangeBioAlert(tL_groupCallParticipant.about, j, getContext(), this.currentAccount);
                    return;
                }
                if (i == 11) {
                    AlertsCreator.createChangeNameAlert(j, getContext(), this.currentAccount);
                    return;
                }
                if (i == 5) {
                    sharedInstance.editCallMember(user, Boolean.TRUE, null, null, null, null);
                    getUndoView().showWithAction(0L, 35, user);
                    sharedInstance.setParticipantVolume(tL_groupCallParticipant, 0);
                    return;
                }
                if ((tL_groupCallParticipant.flags & 128) == 0 || tL_groupCallParticipant.volume != 0) {
                    bool = Boolean.FALSE;
                    bool2 = null;
                    runnable = null;
                    bool3 = null;
                    num = null;
                } else {
                    tL_groupCallParticipant.volume = 10000;
                    tL_groupCallParticipant.volume_by_admin = false;
                    bool = Boolean.FALSE;
                    num = 10000;
                    bool2 = null;
                    runnable = null;
                    bool3 = null;
                }
                sharedInstance.editCallMember(user, bool, bool3, num, bool2, runnable);
                sharedInstance.setParticipantVolume(tL_groupCallParticipant, ChatObject.getParticipantVolume(tL_groupCallParticipant));
                getUndoView().showWithAction(0L, i == 1 ? 31 : 36, user, (Object) null, (Runnable) null, (Runnable) null);
                return;
            }
            this.parentActivity.switchToAccount(this.currentAccount, true);
            BaseFragment baseFragment = (BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1);
            if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == j) {
                dismiss();
                return;
            }
            Bundle bundle2 = new Bundle();
            if (j > 0) {
                bundle2.putLong("user_id", j);
            } else {
                bundle2.putLong("chat_id", -j);
            }
            launchActivity = this.parentActivity;
            chatActivity = new ChatActivity(bundle2);
        }
        launchActivity.lambda$runLinkRequest$93(chatActivity);
        dismiss();
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x015e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runAvatarPreviewTransition(final boolean z, GroupCallUserCell groupCallUserCell) {
        boolean z2;
        float f;
        float f2;
        float f3;
        float y;
        float y2;
        int measuredHeight;
        GroupCallMiniTextureView groupCallMiniTextureView;
        GroupCallMiniTextureView groupCallMiniTextureView2;
        float dp = AndroidUtilities.dp(14.0f) + this.containerView.getPaddingLeft();
        float dp2 = AndroidUtilities.dp(14.0f) + this.containerView.getPaddingTop();
        if (this.hasScrimAnchorView) {
            f3 = ((groupCallUserCell.getAvatarImageView().getX() + groupCallUserCell.getX()) + this.listView.getX()) - dp;
            f2 = ((groupCallUserCell.getAvatarImageView().getY() + groupCallUserCell.getY()) + this.listView.getY()) - dp2;
            f = groupCallUserCell.getAvatarImageView().getMeasuredHeight() / this.listView.getMeasuredWidth();
            measuredHeight = (int) ((groupCallUserCell.getAvatarImageView().getMeasuredHeight() >> 1) / f);
        } else {
            if (this.scrimRenderer == null) {
                this.previewTextureTransitionEnabled = true;
            } else {
                if (!z) {
                    ProfileGalleryView profileGalleryView = this.avatarsViewPager;
                    if (profileGalleryView.getRealPosition(profileGalleryView.getCurrentItem()) != 0) {
                        z2 = false;
                        this.previewTextureTransitionEnabled = z2;
                    }
                }
                z2 = true;
                this.previewTextureTransitionEnabled = z2;
            }
            GroupCallGridCell groupCallGridCell = this.scrimGridView;
            if (groupCallGridCell == null || !this.previewTextureTransitionEnabled) {
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = this.scrimFullscreenView;
                if (groupCallUserCell2 != null) {
                    if (this.scrimRenderer == null) {
                        f3 = (((groupCallUserCell2.getAvatarImageView().getX() + this.scrimFullscreenView.getX()) + this.fullscreenUsersListView.getX()) + this.renderersContainer.getX()) - dp;
                        f2 = (((this.scrimFullscreenView.getAvatarImageView().getY() + this.scrimFullscreenView.getY()) + this.fullscreenUsersListView.getY()) + this.renderersContainer.getY()) - dp2;
                        f = this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight() / this.listView.getMeasuredWidth();
                        measuredHeight = (int) ((this.scrimFullscreenView.getAvatarImageView().getMeasuredHeight() >> 1) / f);
                        if (!this.previewTextureTransitionEnabled && (groupCallMiniTextureView = this.scrimRenderer) != null) {
                            groupCallMiniTextureView.invalidate();
                            this.renderersContainer.invalidate();
                            this.scrimRenderer.setShowingAsScrimView(false, false);
                            this.scrimRenderer = null;
                        }
                    } else if (this.previewTextureTransitionEnabled) {
                        f3 = ((groupCallUserCell2.getX() + this.fullscreenUsersListView.getX()) + this.renderersContainer.getX()) - dp;
                        y = this.scrimFullscreenView.getY() + this.fullscreenUsersListView.getY();
                        y2 = this.renderersContainer.getY();
                    }
                }
                f = 0.96f;
                f2 = 0.0f;
                f3 = 0.0f;
                measuredHeight = 0;
                if (!this.previewTextureTransitionEnabled) {
                    groupCallMiniTextureView.invalidate();
                    this.renderersContainer.invalidate();
                    this.scrimRenderer.setShowingAsScrimView(false, false);
                    this.scrimRenderer = null;
                }
            } else {
                f3 = (groupCallGridCell.getX() + this.listView.getX()) - dp;
                y = this.scrimGridView.getY() + this.listView.getY();
                y2 = AndroidUtilities.dp(2.0f);
            }
            f2 = (y + y2) - dp2;
            f = 1.0f;
            measuredHeight = 0;
            if (!this.previewTextureTransitionEnabled) {
            }
        }
        final float f4 = f2;
        final int i = measuredHeight;
        final float f5 = f3;
        final float f6 = f;
        if (z) {
            this.avatarPreviewContainer.setScaleX(f6);
            this.avatarPreviewContainer.setScaleY(f6);
            this.avatarPreviewContainer.setTranslationX(f5);
            this.avatarPreviewContainer.setTranslationY(f4);
            this.avatarPagerIndicator.setAlpha(0.0f);
        }
        this.avatarsViewPager.setRoundRadius(i, i);
        if (this.useBlur) {
            if (z) {
                this.blurredView.setAlpha(0.0f);
            }
            this.blurredView.animate().alpha(z ? 1.0f : 0.0f).setDuration(220L).start();
        }
        this.avatarPagerIndicator.animate().alpha(z ? 1.0f : 0.0f).setDuration(220L).start();
        if (!z && (groupCallMiniTextureView2 = this.scrimRenderer) != null) {
            groupCallMiniTextureView2.setShowingAsScrimView(false, true);
            ProfileGalleryView profileGalleryView2 = this.avatarsViewPager;
            if (profileGalleryView2.getRealPosition(profileGalleryView2.getCurrentItem()) != 0) {
                this.scrimRenderer.textureView.cancelAnimation();
                this.scrimGridView = null;
            }
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(z ? 0.0f : 1.0f, z ? 1.0f : 0.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                GroupCallActivity.this.lambda$runAvatarPreviewTransition$63(f6, f5, f4, i, valueAnimator);
            }
        });
        this.popupAnimationIndex = this.accountInstance.getNotificationCenter().setAnimationInProgress(this.popupAnimationIndex, new int[]{NotificationCenter.dialogPhotosLoaded, NotificationCenter.fileLoaded, NotificationCenter.messagesDidLoad});
        final GroupCallMiniTextureView groupCallMiniTextureView3 = this.scrimGridView != null ? this.scrimRenderer : null;
        if (groupCallMiniTextureView3 != null) {
            groupCallMiniTextureView3.animateToScrimView = true;
        }
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.60
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                GroupCallMiniTextureView groupCallMiniTextureView4 = groupCallMiniTextureView3;
                if (groupCallMiniTextureView4 != null) {
                    groupCallMiniTextureView4.animateToScrimView = false;
                }
                GroupCallActivity.this.accountInstance.getNotificationCenter().onAnimationFinish(GroupCallActivity.this.popupAnimationIndex);
                GroupCallActivity.this.avatarPriviewTransitionInProgress = false;
                GroupCallActivity.this.progressToAvatarPreview = z ? 1.0f : 0.0f;
                GroupCallActivity.this.renderersContainer.progressToScrimView = GroupCallActivity.this.progressToAvatarPreview;
                if (z) {
                    GroupCallActivity.this.avatarPreviewContainer.setAlpha(1.0f);
                    GroupCallActivity.this.avatarPreviewContainer.setScaleX(1.0f);
                    GroupCallActivity.this.avatarPreviewContainer.setScaleY(1.0f);
                    GroupCallActivity.this.avatarPreviewContainer.setTranslationX(0.0f);
                    GroupCallActivity.this.avatarPreviewContainer.setTranslationY(0.0f);
                } else {
                    GroupCallActivity.this.scrimPaint.setAlpha(0);
                    GroupCallActivity.this.clearScrimView();
                    if (GroupCallActivity.this.scrimPopupLayout.getParent() != null) {
                        ((BottomSheet) GroupCallActivity.this).containerView.removeView(GroupCallActivity.this.scrimPopupLayout);
                    }
                    GroupCallActivity.this.scrimPopupLayout = null;
                    GroupCallActivity.this.avatarPreviewContainer.setVisibility(8);
                    GroupCallActivity.this.avatarsPreviewShowed = false;
                    GroupCallActivity.this.layoutManager.setCanScrollVertically(true);
                    GroupCallActivity.this.blurredView.setVisibility(8);
                    if (GroupCallActivity.this.delayedGroupCallUpdated) {
                        GroupCallActivity.this.delayedGroupCallUpdated = false;
                        GroupCallActivity.this.applyCallParticipantUpdates(true);
                    }
                    if (GroupCallActivity.this.scrimRenderer != null) {
                        GroupCallActivity.this.scrimRenderer.textureView.setRoundCorners(0.0f);
                    }
                }
                GroupCallActivity.this.checkContentOverlayed();
                ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                GroupCallActivity.this.avatarsViewPager.invalidate();
                GroupCallActivity.this.listView.invalidate();
            }
        });
        if (this.hasScrimAnchorView || this.scrimRenderer == null) {
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.setDuration(220L);
            ofFloat.start();
        } else {
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.setDuration(220L);
            this.scrimRenderer.textureView.setAnimateNextDuration(220L);
            this.scrimRenderer.textureView.synchOrRunAnimation(ofFloat);
        }
        checkContentOverlayed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runUpdateTextureLightningRunnable() {
        AndroidUtilities.cancelRunOnUIThread(this.updateTextureLightningRunnable);
        if (!this.needTextureLightning || this.textureLightningView == null || VoIPService.getSharedInstance() == null || !isRtmpStream() || this.listView == null || !LiteMode.isEnabled(512)) {
            return;
        }
        AndroidUtilities.runOnUIThread(this.updateTextureLightningRunnable, 30L);
    }

    private void setAmplitude(double d) {
        float min = (float) (Math.min(8500.0d, d) / 8500.0d);
        this.animateToAmplitude = min;
        this.animateAmplitudeDiff = (min - this.amplitude) / ((BlobDrawable.AMPLITUDE_SPEED * 500.0f) + 100.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setColorProgress(float f) {
        this.colorProgress = f;
        GroupCallRenderersContainer groupCallRenderersContainer = this.renderersContainer;
        float max = Math.max(f, groupCallRenderersContainer == null ? 0.0f : groupCallRenderersContainer.progressToFullscreenMode);
        int i = Theme.key_voipgroup_actionBarUnscrolled;
        int color = Theme.getColor(i);
        int i2 = Theme.key_voipgroup_actionBar;
        int offsetColor = AndroidUtilities.getOffsetColor(color, Theme.getColor(i2), f, 1.0f);
        this.backgroundColor = offsetColor;
        this.actionBarBackground.setBackgroundColor(offsetColor);
        this.otherItem.redrawPopup(-14472653);
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(this.backgroundColor, PorterDuff.Mode.MULTIPLY));
        this.navBarColor = AndroidUtilities.getOffsetColor(Theme.getColor(i), Theme.getColor(i2), max, 1.0f);
        int offsetColor2 = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_listViewBackgroundUnscrolled), Theme.getColor(Theme.key_voipgroup_listViewBackground), f, 1.0f);
        this.listViewBackgroundPaint.setColor(offsetColor2);
        this.listView.setGlowColor(offsetColor2);
        int i3 = this.muteButtonState;
        if (i3 == 3 || isGradientState(i3)) {
            this.muteButton.invalidate();
        }
        View view = this.buttonsBackgroundGradientView;
        if (view != null) {
            int[] iArr = this.gradientColors;
            iArr[0] = this.backgroundColor;
            iArr[1] = 0;
            if (Build.VERSION.SDK_INT > 29) {
                this.buttonsBackgroundGradient.setColors(iArr);
            } else {
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, this.gradientColors);
                this.buttonsBackgroundGradient = gradientDrawable;
                view.setBackground(gradientDrawable);
            }
            this.buttonsBackgroundGradientView2.setBackgroundColor(this.gradientColors[0]);
        }
        int offsetColor3 = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_leaveButton), Theme.getColor(Theme.key_voipgroup_leaveButtonScrolled), f, 1.0f);
        this.leaveButton.setBackgroundColor(offsetColor3, offsetColor3);
        int offsetColor4 = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_lastSeenTextUnscrolled), Theme.getColor(Theme.key_voipgroup_lastSeenText), f, 1.0f);
        int offsetColor5 = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_mutedIconUnscrolled), Theme.getColor(Theme.key_voipgroup_mutedIcon), f, 1.0f);
        int childCount = this.listView.getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = this.listView.getChildAt(i4);
            if (childAt instanceof GroupCallTextCell) {
                ((GroupCallTextCell) childAt).setColors(offsetColor5, offsetColor4);
            } else if (childAt instanceof GroupCallUserCell) {
                ((GroupCallUserCell) childAt).setGrayIconColor(this.actionBar.getTag() != null ? Theme.key_voipgroup_mutedIcon : Theme.key_voipgroup_mutedIconUnscrolled, offsetColor5);
            } else if (childAt instanceof GroupCallInvitedCell) {
                ((GroupCallInvitedCell) childAt).setGrayIconColor(this.actionBar.getTag() != null ? Theme.key_voipgroup_mutedIcon : Theme.key_voipgroup_mutedIconUnscrolled, offsetColor5);
            }
        }
        this.containerView.invalidate();
        this.listView.invalidate();
        this.container.invalidate();
    }

    private void setMicAmplitude(float f) {
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute()) {
            f = 0.0f;
        }
        setAmplitude(4000.0f * f);
        ChatObject.Call call = this.call;
        if (call == null || this.listView == null || (tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) call.participants.get(MessageObject.getPeerId(this.selfPeer))) == null) {
            return;
        }
        if (this.renderersContainer.inFullscreenMode) {
            for (int i = 0; i < this.fullscreenUsersListView.getChildCount(); i++) {
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = (GroupCallFullscreenAdapter.GroupCallUserCell) this.fullscreenUsersListView.getChildAt(i);
                if (MessageObject.getPeerId(groupCallUserCell.getParticipant().peer) == MessageObject.getPeerId(tL_groupCallParticipant.peer)) {
                    groupCallUserCell.setAmplitude(f * 15.0f);
                }
            }
        } else {
            int indexOf = (this.delayedGroupCallUpdated ? this.oldParticipants : this.call.visibleParticipants).indexOf(tL_groupCallParticipant);
            if (indexOf >= 0 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(indexOf + this.listAdapter.usersStartRow)) != null) {
                View view = findViewHolderForAdapterPosition.itemView;
                if (view instanceof GroupCallUserCell) {
                    ((GroupCallUserCell) view).setAmplitude(f * 15.0f);
                    if (findViewHolderForAdapterPosition.itemView == this.scrimView && !this.contentFullyOverlayed) {
                        this.containerView.invalidate();
                    }
                }
            }
        }
        this.renderersContainer.setAmplitude(tL_groupCallParticipant, f * 15.0f);
    }

    private void setScrollOffsetY(float f) {
        int i;
        this.scrollOffsetY = f;
        this.listView.setTopGlowOffset((int) (f - ((FrameLayout.LayoutParams) this.listView.getLayoutParams()).topMargin));
        float dp = f - AndroidUtilities.dp(74.0f);
        if (this.backgroundPaddingTop + dp < ActionBar.getCurrentActionBarHeight() * 2) {
            float min = Math.min(1.0f, (((ActionBar.getCurrentActionBarHeight() * 2) - dp) - this.backgroundPaddingTop) / (((r0 - this.backgroundPaddingTop) - AndroidUtilities.dp(14.0f)) + ActionBar.getCurrentActionBarHeight()));
            i = (int) (AndroidUtilities.dp(AndroidUtilities.isTablet() ? 17.0f : 13.0f) * min);
            LightningView lightningView = this.textureLightningView;
            if (lightningView != null) {
                lightningView.setShadowOffset((int) (AndroidUtilities.dp(8.0f) * min));
            }
            if (Math.abs(Math.min(1.0f, min) - this.colorProgress) > 1.0E-4f) {
                setColorProgress(Math.min(1.0f, min));
            }
            float f2 = 1.0f - ((0.1f * min) * 1.2f);
            this.titleTextView.setScaleX(Math.max(0.9f, f2));
            this.titleTextView.setScaleY(Math.max(0.9f, f2));
            float f3 = 1.0f - (min * 1.2f);
            this.titleTextView.setAlpha(Math.max(0.0f, f3) * (1.0f - this.renderersContainer.progressToFullscreenMode));
            this.liveLabelTextView.setScaleX(Math.max(0.9f, f2));
            this.liveLabelTextView.setScaleY(Math.max(0.9f, f2));
            this.liveLabelTextView.setAlpha(Math.max(0.0f, f3) * (1.0f - this.renderersContainer.progressToFullscreenMode));
        } else {
            this.titleTextView.setScaleX(1.0f);
            this.titleTextView.setScaleY(1.0f);
            this.titleTextView.setAlpha(1.0f - this.renderersContainer.progressToFullscreenMode);
            this.liveLabelTextView.setScaleX(1.0f);
            this.liveLabelTextView.setScaleY(1.0f);
            this.liveLabelTextView.setAlpha(1.0f - this.renderersContainer.progressToFullscreenMode);
            if (this.colorProgress > 1.0E-4f) {
                setColorProgress(0.0f);
            }
            i = 0;
        }
        float f4 = i;
        this.menuItemsContainer.setTranslationY(Math.max(AndroidUtilities.dp(4.0f), (f - AndroidUtilities.dp(53.0f)) - f4));
        this.titleLayout.setTranslationY(Math.max(AndroidUtilities.dp(4.0f), (f - AndroidUtilities.dp(44.0f)) - f4));
        LightningView lightningView2 = this.textureLightningView;
        if (lightningView2 != null) {
            lightningView2.setTranslationY(Math.max(AndroidUtilities.dp(4.0f), f - AndroidUtilities.dp(37.0f)));
        }
        LinearLayout linearLayout = this.scheduleTimerContainer;
        if (linearLayout != null) {
            linearLayout.setTranslationY(Math.max(AndroidUtilities.dp(4.0f), (f - AndroidUtilities.dp(44.0f)) - f4));
        }
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x02d8, code lost:
    
        if ((r9 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipantCreator) == false) goto L110;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x02f3, code lost:
    
        if (r3 == (-r28.currentChat.id)) goto L113;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0584  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x05da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x063c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0683  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x05b2  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x04e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean showMenuForCell(View view) {
        GroupCallUserCell groupCallUserCell;
        VolumeSlider volumeSlider;
        LinearLayout linearLayout;
        ScrollView scrollView;
        ScrollView scrollView2;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout;
        LinearLayout linearLayout2;
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int size;
        final int i6;
        AnimatorSet animatorSet;
        ImageLocation forUserOrChat;
        ImageLocation forUserOrChat2;
        AvatarUpdaterDelegate avatarUpdaterDelegate;
        int x;
        float y;
        float y2;
        int measuredHeight;
        int i7;
        TLRPC.ChatParticipants chatParticipants;
        if (this.itemAnimator.isRunning() || getContext() == null) {
            return false;
        }
        if (this.avatarPriviewTransitionInProgress || this.avatarsPreviewShowed) {
            dismissAvatarPreview(true);
            return false;
        }
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            this.scrimPopupWindow = null;
            return false;
        }
        clearScrimView();
        if (view instanceof GroupCallGridCell) {
            GroupCallGridCell groupCallGridCell = (GroupCallGridCell) view;
            if (groupCallGridCell.getParticipant() == this.call.videoNotAvailableParticipant) {
                return false;
            }
            groupCallUserCell = new GroupCallUserCell(groupCallGridCell.getContext());
            groupCallUserCell.setData(this.accountInstance, groupCallGridCell.getParticipant().participant, this.call, MessageObject.getPeerId(this.selfPeer), null, false);
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = groupCallUserCell.rightDrawable;
            if (swapAnimatedEmojiDrawable != null) {
                swapAnimatedEmojiDrawable.play();
            }
            this.hasScrimAnchorView = false;
            this.scrimGridView = groupCallGridCell;
            this.scrimRenderer = groupCallGridCell.getRenderer();
            if (isTabletMode || isLandscapeMode) {
                this.scrimViewAttached = false;
            } else {
                this.scrimViewAttached = true;
                this.containerView.addView(groupCallUserCell, LayoutHelper.createFrame(-1, -2.0f, 0, 14.0f, 0.0f, 14.0f, 0.0f));
            }
        } else {
            if (view instanceof GroupCallFullscreenAdapter.GroupCallUserCell) {
                GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = (GroupCallFullscreenAdapter.GroupCallUserCell) view;
                if (groupCallUserCell2.getParticipant() == this.call.videoNotAvailableParticipant.participant) {
                    return false;
                }
                groupCallUserCell = new GroupCallUserCell(groupCallUserCell2.getContext());
                groupCallUserCell.setData(this.accountInstance, groupCallUserCell2.getParticipant(), this.call, MessageObject.getPeerId(this.selfPeer), null, false);
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = groupCallUserCell.rightDrawable;
                if (swapAnimatedEmojiDrawable2 != null) {
                    swapAnimatedEmojiDrawable2.play();
                }
                this.hasScrimAnchorView = false;
                this.scrimFullscreenView = groupCallUserCell2;
                GroupCallMiniTextureView renderer = groupCallUserCell2.getRenderer();
                this.scrimRenderer = renderer;
                if (renderer != null && renderer.showingInFullscreen) {
                    this.scrimRenderer = null;
                }
                this.containerView.addView(groupCallUserCell, LayoutHelper.createFrame(-1, -2.0f, 0, 14.0f, 0.0f, 14.0f, 0.0f));
            } else {
                groupCallUserCell = (GroupCallUserCell) view;
                this.hasScrimAnchorView = true;
            }
            this.scrimViewAttached = true;
        }
        GroupCallUserCell groupCallUserCell3 = groupCallUserCell;
        if (groupCallUserCell3 == null) {
            return false;
        }
        boolean z2 = (isLandscapeMode || isTabletMode || AndroidUtilities.isInMultiwindow) ? false : true;
        final TLRPC.TL_groupCallParticipant participant = groupCallUserCell3.getParticipant();
        if (participant == null) {
            return false;
        }
        final Rect rect = new Rect();
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getContext());
        actionBarPopupWindowLayout2.setBackgroundDrawable(null);
        actionBarPopupWindowLayout2.setPadding(0, 0, 0, 0);
        actionBarPopupWindowLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.GroupCallActivity.56
            private int[] pos = new int[2];

            /* JADX WARN: Code restructure failed: missing block: B:17:0x0070, code lost:
            
                if (r7.this$0.scrimPopupWindow.isShowing() != false) goto L17;
             */
            /* JADX WARN: Code restructure failed: missing block: B:8:0x0054, code lost:
            
                if (r2.contains((int) r9.getX(), (int) r9.getY()) == false) goto L17;
             */
            /* JADX WARN: Code restructure failed: missing block: B:9:0x0072, code lost:
            
                r7.this$0.scrimPopupWindow.dismiss();
             */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    if (GroupCallActivity.this.scrimPopupWindow != null && GroupCallActivity.this.scrimPopupWindow.isShowing()) {
                        View contentView = GroupCallActivity.this.scrimPopupWindow.getContentView();
                        contentView.getLocationInWindow(this.pos);
                        Rect rect2 = rect;
                        int[] iArr = this.pos;
                        int i8 = iArr[0];
                        rect2.set(i8, iArr[1], contentView.getMeasuredWidth() + i8, this.pos[1] + contentView.getMeasuredHeight());
                    }
                } else if (motionEvent.getActionMasked() == 4) {
                    if (GroupCallActivity.this.scrimPopupWindow != null) {
                    }
                }
                return false;
            }
        });
        actionBarPopupWindowLayout2.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda37
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
            public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                GroupCallActivity.this.lambda$showMenuForCell$61(keyEvent);
            }
        });
        final LinearLayout linearLayout3 = new LinearLayout(getContext());
        final LinearLayout linearLayout4 = !participant.muted_by_you ? new LinearLayout(getContext()) : null;
        this.currentOptionsLayout = linearLayout3;
        final LinearLayout linearLayout5 = new LinearLayout(getContext()) { // from class: org.telegram.ui.GroupCallActivity.57
            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i8, int i9) {
                int measuredWidth;
                int measuredHeight2;
                linearLayout3.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i8), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
                LinearLayout linearLayout6 = linearLayout4;
                if (linearLayout6 != null) {
                    linearLayout6.measure(View.MeasureSpec.makeMeasureSpec(linearLayout3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
                    measuredWidth = linearLayout3.getMeasuredWidth();
                    measuredHeight2 = linearLayout3.getMeasuredHeight() + linearLayout4.getMeasuredHeight();
                } else {
                    measuredWidth = linearLayout3.getMeasuredWidth();
                    measuredHeight2 = linearLayout3.getMeasuredHeight();
                }
                setMeasuredDimension(measuredWidth, measuredHeight2);
            }
        };
        linearLayout5.setMinimumWidth(AndroidUtilities.dp(240.0f));
        linearLayout5.setOrientation(1);
        int offsetColor = AndroidUtilities.getOffsetColor(Theme.getColor(Theme.key_voipgroup_listViewBackgroundUnscrolled), Theme.getColor(Theme.key_voipgroup_listViewBackground), this.colorProgress, 1.0f);
        if (linearLayout4 == null || groupCallUserCell3.isSelfUser() || participant.muted_by_you || (participant.muted && !participant.can_self_unmute)) {
            volumeSlider = null;
        } else {
            Drawable mutate = getContext().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY));
            linearLayout4.setBackgroundDrawable(mutate);
            linearLayout5.addView(linearLayout4, LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            VolumeSlider volumeSlider2 = new VolumeSlider(getContext(), participant);
            linearLayout4.addView(volumeSlider2, -1, 48);
            volumeSlider = volumeSlider2;
        }
        linearLayout3.setMinimumWidth(AndroidUtilities.dp(240.0f));
        linearLayout3.setOrientation(1);
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(offsetColor, PorterDuff.Mode.MULTIPLY));
        linearLayout3.setBackgroundDrawable(mutate2);
        linearLayout5.addView(linearLayout3, LayoutHelper.createLinear(-2, -2, 0.0f, volumeSlider != null ? -8.0f : 0.0f, 0.0f, 0.0f));
        if (Build.VERSION.SDK_INT >= 21) {
            linearLayout = linearLayout5;
            scrollView = new ScrollView(getContext(), null, 0, R.style.scrollbarShapeStyle) { // from class: org.telegram.ui.GroupCallActivity.58
                @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i8, int i9) {
                    super.onMeasure(i8, i9);
                    setMeasuredDimension(linearLayout5.getMeasuredWidth(), getMeasuredHeight());
                }
            };
        } else {
            linearLayout = linearLayout5;
            scrollView = new ScrollView(getContext());
        }
        scrollView.setClipToPadding(false);
        actionBarPopupWindowLayout2.addView(scrollView, LayoutHelper.createFrame(-2, -2.0f));
        long peerId = MessageObject.getPeerId(participant.peer);
        ArrayList arrayList = new ArrayList(2);
        ArrayList arrayList2 = new ArrayList(2);
        final ArrayList arrayList3 = new ArrayList(2);
        if (!(participant.peer instanceof TLRPC.TL_peerUser)) {
            scrollView2 = scrollView;
            actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            linearLayout2 = linearLayout3;
        } else if (ChatObject.isChannel(this.currentChat)) {
            actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            linearLayout2 = linearLayout3;
            TLRPC.ChannelParticipant adminInChannel = this.accountInstance.getMessagesController().getAdminInChannel(participant.peer.user_id, this.currentChat.id);
            if (adminInChannel != null && ((adminInChannel instanceof TLRPC.TL_channelParticipantCreator) || adminInChannel.admin_rights.manage_call)) {
                scrollView2 = scrollView;
                z = true;
            }
            scrollView2 = scrollView;
            z = false;
        } else {
            actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            linearLayout2 = linearLayout3;
            TLRPC.ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(this.currentChat.id);
            if (chatFull != null && (chatParticipants = chatFull.participants) != null) {
                int size2 = chatParticipants.participants.size();
                int i8 = 0;
                while (i8 < size2) {
                    TLRPC.ChatParticipant chatParticipant = chatFull.participants.participants.get(i8);
                    TLRPC.ChatFull chatFull2 = chatFull;
                    scrollView2 = scrollView;
                    if (chatParticipant.user_id == participant.peer.user_id) {
                        if (!(chatParticipant instanceof TLRPC.TL_chatParticipantAdmin)) {
                        }
                        z = true;
                    } else {
                        i8++;
                        chatFull = chatFull2;
                        scrollView = scrollView2;
                    }
                }
            }
            scrollView2 = scrollView;
            z = false;
        }
        if (groupCallUserCell3.isSelfUser()) {
            if (groupCallUserCell3.isHandRaised()) {
                arrayList.add(LocaleController.getString(R.string.VoipGroupCancelRaiseHand));
                arrayList2.add(Integer.valueOf(R.drawable.msg_handdown));
                arrayList3.add(7);
            }
            arrayList.add(LocaleController.getString(groupCallUserCell3.hasAvatarSet() ? R.string.VoipAddPhoto : R.string.VoipSetNewPhoto));
            arrayList2.add(Integer.valueOf(R.drawable.msg_addphoto));
            arrayList3.add(9);
            arrayList.add(LocaleController.getString(peerId > 0 ? TextUtils.isEmpty(participant.about) ? R.string.VoipAddBio : R.string.VoipEditBio : TextUtils.isEmpty(participant.about) ? R.string.VoipAddDescription : R.string.VoipEditDescription));
            arrayList2.add(Integer.valueOf(TextUtils.isEmpty(participant.about) ? R.drawable.msg_addbio : R.drawable.msg_info));
            arrayList3.add(10);
            arrayList.add(LocaleController.getString(peerId > 0 ? R.string.VoipEditName : R.string.VoipEditTitle));
            arrayList2.add(Integer.valueOf(R.drawable.msg_edit));
            i3 = 11;
        } else if (ChatObject.canManageCalls(this.currentChat)) {
            if (!z || !participant.muted) {
                if (!participant.muted || participant.can_self_unmute) {
                    arrayList.add(LocaleController.getString(R.string.VoipGroupMute));
                    arrayList2.add(Integer.valueOf(R.drawable.msg_voice_muted));
                    i4 = 0;
                } else {
                    arrayList.add(LocaleController.getString(R.string.VoipGroupAllowToSpeak));
                    arrayList2.add(Integer.valueOf(participant.raise_hand_rating != 0 ? R.drawable.msg_allowspeak : R.drawable.msg_voice_unmuted));
                    i4 = 1;
                }
                arrayList3.add(Integer.valueOf(i4));
            }
            TLRPC.Peer peer = participant.peer;
            if (peer != null) {
                long j = peer.channel_id;
                if (j != 0 && !ChatObject.isMegagroup(this.currentAccount, j)) {
                    arrayList.add(LocaleController.getString(R.string.VoipGroupOpenChannel));
                    arrayList2.add(Integer.valueOf(R.drawable.msg_channel));
                    i5 = 8;
                    arrayList3.add(i5);
                    if (!z && ChatObject.canBlockUsers(this.currentChat)) {
                        arrayList.add(LocaleController.getString(R.string.VoipGroupUserRemove));
                        arrayList2.add(Integer.valueOf(R.drawable.msg_block2));
                        arrayList3.add(2);
                    }
                    size = arrayList.size();
                    i6 = 0;
                    while (i6 < size) {
                        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), i6 == 0, i6 == size + (-1));
                        int i9 = ((Integer) arrayList3.get(i6)).intValue() != 2 ? Theme.key_voipgroup_actionBarItems : Theme.key_voipgroup_leaveCallMenu;
                        actionBarMenuSubItem.setColors(Theme.getColor(i9), Theme.getColor(i9));
                        actionBarMenuSubItem.setSelectorColor(Theme.getColor(Theme.key_voipgroup_listSelector));
                        actionBarMenuSubItem.setTextAndIcon((CharSequence) arrayList.get(i6), ((Integer) arrayList2.get(i6)).intValue());
                        linearLayout2.addView(actionBarMenuSubItem);
                        actionBarMenuSubItem.setTag(arrayList3.get(i6));
                        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda38
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                GroupCallActivity.this.lambda$showMenuForCell$62(i6, arrayList3, participant, view2);
                            }
                        });
                        i6++;
                    }
                    scrollView2.addView(linearLayout, LayoutHelper.createScroll(-2, -2, 51));
                    this.listView.stopScroll();
                    this.layoutManager.setCanScrollVertically(false);
                    this.scrimView = groupCallUserCell3;
                    groupCallUserCell3.setAboutVisible(true);
                    this.containerView.invalidate();
                    this.listView.invalidate();
                    animatorSet = this.scrimAnimatorSet;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                    }
                    ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout3 = actionBarPopupWindowLayout;
                    this.scrimPopupLayout = actionBarPopupWindowLayout3;
                    if (peerId <= 0) {
                        TLRPC.User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId));
                        forUserOrChat = ImageLocation.getForUserOrChat(user, 0);
                        forUserOrChat2 = ImageLocation.getForUserOrChat(user, 1);
                        if (MessagesController.getInstance(this.currentAccount).getUserFull(peerId) == null) {
                            MessagesController.getInstance(this.currentAccount).loadUserInfo(user, false, 0);
                        }
                    } else {
                        TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId));
                        forUserOrChat = ImageLocation.getForUserOrChat(chat, 0);
                        forUserOrChat2 = ImageLocation.getForUserOrChat(chat, 1);
                    }
                    GroupCallMiniTextureView groupCallMiniTextureView = this.scrimRenderer;
                    boolean z3 = groupCallMiniTextureView == null && groupCallMiniTextureView.isAttached();
                    if (forUserOrChat != null && !z3) {
                        z2 = false;
                    } else if (z2) {
                        this.avatarsViewPager.setParentAvatarImage(this.scrimView.getAvatarImageView());
                        this.avatarsViewPager.setHasActiveVideo(z3);
                        this.avatarsViewPager.setData(peerId, true);
                        this.avatarsViewPager.setCreateThumbFromParent(true);
                        this.avatarsViewPager.initIfEmpty(null, forUserOrChat, forUserOrChat2, true);
                        GroupCallMiniTextureView groupCallMiniTextureView2 = this.scrimRenderer;
                        if (groupCallMiniTextureView2 != null) {
                            groupCallMiniTextureView2.setShowingAsScrimView(true, true);
                        }
                        if (MessageObject.getPeerId(this.selfPeer) == peerId && this.currentAvatarUpdater != null && (avatarUpdaterDelegate = this.avatarUpdaterDelegate) != null && avatarUpdaterDelegate.avatar != null) {
                            this.avatarsViewPager.addUploadingImage(this.avatarUpdaterDelegate.uploadingImageLocation, ImageLocation.getForLocal(this.avatarUpdaterDelegate.avatar));
                        }
                    }
                    if (!z2) {
                        this.avatarsPreviewShowed = true;
                        actionBarPopupWindowLayout3.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                        this.containerView.addView(this.scrimPopupLayout, LayoutHelper.createFrame(-2, -2.0f));
                        this.useBlur = true;
                        prepareBlurBitmap();
                        this.avatarPriviewTransitionInProgress = true;
                        this.avatarPreviewContainer.setVisibility(0);
                        if (volumeSlider != null) {
                            volumeSlider.invalidate();
                        }
                        runAvatarPreviewTransition(true, groupCallUserCell3);
                        GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell4 = this.scrimFullscreenView;
                        if (groupCallUserCell4 == null) {
                            return true;
                        }
                        groupCallUserCell4.getAvatarImageView().setAlpha(0.0f);
                        return true;
                    }
                    this.avatarsPreviewShowed = false;
                    int i10 = -2;
                    ActionBarPopupWindow actionBarPopupWindow2 = new ActionBarPopupWindow(actionBarPopupWindowLayout3, i10, i10) { // from class: org.telegram.ui.GroupCallActivity.59
                        @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                        public void dismiss() {
                            super.dismiss();
                            if (GroupCallActivity.this.scrimPopupWindow != this) {
                                return;
                            }
                            GroupCallActivity.this.scrimPopupWindow = null;
                            if (GroupCallActivity.this.scrimAnimatorSet != null) {
                                GroupCallActivity.this.scrimAnimatorSet.cancel();
                                GroupCallActivity.this.scrimAnimatorSet = null;
                            }
                            GroupCallActivity.this.layoutManager.setCanScrollVertically(true);
                            GroupCallActivity.this.scrimAnimatorSet = new AnimatorSet();
                            ArrayList arrayList4 = new ArrayList();
                            arrayList4.add(ObjectAnimator.ofInt(GroupCallActivity.this.scrimPaint, (Property<Paint, Integer>) AnimationProperties.PAINT_ALPHA, 0));
                            GroupCallActivity.this.scrimAnimatorSet.playTogether(arrayList4);
                            GroupCallActivity.this.scrimAnimatorSet.setDuration(220L);
                            GroupCallActivity.this.scrimAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.59.1
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    GroupCallActivity.this.clearScrimView();
                                    ((BottomSheet) GroupCallActivity.this).containerView.invalidate();
                                    GroupCallActivity.this.listView.invalidate();
                                    if (GroupCallActivity.this.delayedGroupCallUpdated) {
                                        GroupCallActivity.this.delayedGroupCallUpdated = false;
                                        GroupCallActivity.this.applyCallParticipantUpdates(true);
                                    }
                                }
                            });
                            GroupCallActivity.this.scrimAnimatorSet.start();
                        }
                    };
                    this.scrimPopupWindow = actionBarPopupWindow2;
                    actionBarPopupWindow2.setPauseNotifications(true);
                    this.scrimPopupWindow.setDismissAnimationDuration(NotificationCenter.updateAllMessages);
                    this.scrimPopupWindow.setOutsideTouchable(true);
                    this.scrimPopupWindow.setClippingEnabled(true);
                    this.scrimPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
                    this.scrimPopupWindow.setFocusable(true);
                    actionBarPopupWindowLayout3.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                    this.scrimPopupWindow.setInputMethodMode(2);
                    this.scrimPopupWindow.setSoftInputMode(0);
                    this.scrimPopupWindow.getContentView().setFocusableInTouchMode(true);
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell5 = this.scrimFullscreenView;
                    if (groupCallUserCell5 != null) {
                        boolean z4 = isLandscapeMode;
                        float x2 = groupCallUserCell5.getX();
                        if (z4) {
                            x = (((int) ((x2 + this.fullscreenUsersListView.getX()) + this.renderersContainer.getX())) - actionBarPopupWindowLayout3.getMeasuredWidth()) + AndroidUtilities.dp(32.0f);
                            i7 = ((int) ((this.scrimFullscreenView.getY() + this.fullscreenUsersListView.getY()) + this.renderersContainer.getY())) - AndroidUtilities.dp(6.0f);
                        } else {
                            x = ((int) ((x2 + this.fullscreenUsersListView.getX()) + this.renderersContainer.getX())) - AndroidUtilities.dp(14.0f);
                            i7 = (int) (((this.scrimFullscreenView.getY() + this.fullscreenUsersListView.getY()) + this.renderersContainer.getY()) - actionBarPopupWindowLayout3.getMeasuredHeight());
                        }
                    } else {
                        x = (int) (((this.listView.getX() + this.listView.getMeasuredWidth()) + AndroidUtilities.dp(8.0f)) - actionBarPopupWindowLayout3.getMeasuredWidth());
                        if (this.hasScrimAnchorView) {
                            y2 = this.listView.getY() + groupCallUserCell3.getY();
                            measuredHeight = groupCallUserCell3.getClipHeight();
                        } else if (this.scrimGridView != null) {
                            y2 = this.listView.getY() + this.scrimGridView.getY();
                            measuredHeight = this.scrimGridView.getMeasuredHeight();
                        } else {
                            y = this.listView.getY();
                            i7 = (int) y;
                        }
                        y = y2 + measuredHeight;
                        i7 = (int) y;
                    }
                    this.scrimPopupWindow.showAtLocation(this.listView, 51, x, i7);
                    this.scrimAnimatorSet = new AnimatorSet();
                    ArrayList arrayList4 = new ArrayList();
                    arrayList4.add(ObjectAnimator.ofInt(this.scrimPaint, (Property<Paint, Integer>) AnimationProperties.PAINT_ALPHA, 0, 100));
                    this.scrimAnimatorSet.playTogether(arrayList4);
                    this.scrimAnimatorSet.setDuration(150L);
                    this.scrimAnimatorSet.start();
                    return true;
                }
            }
            arrayList.add(LocaleController.getString(R.string.VoipGroupOpenProfile));
            arrayList2.add(Integer.valueOf(R.drawable.msg_openprofile));
            i5 = 6;
            arrayList3.add(i5);
            if (!z) {
                arrayList.add(LocaleController.getString(R.string.VoipGroupUserRemove));
                arrayList2.add(Integer.valueOf(R.drawable.msg_block2));
                arrayList3.add(2);
            }
            size = arrayList.size();
            i6 = 0;
            while (i6 < size) {
            }
            scrollView2.addView(linearLayout, LayoutHelper.createScroll(-2, -2, 51));
            this.listView.stopScroll();
            this.layoutManager.setCanScrollVertically(false);
            this.scrimView = groupCallUserCell3;
            groupCallUserCell3.setAboutVisible(true);
            this.containerView.invalidate();
            this.listView.invalidate();
            animatorSet = this.scrimAnimatorSet;
            if (animatorSet != null) {
            }
            View actionBarPopupWindowLayout32 = actionBarPopupWindowLayout;
            this.scrimPopupLayout = actionBarPopupWindowLayout32;
            if (peerId <= 0) {
            }
            GroupCallMiniTextureView groupCallMiniTextureView3 = this.scrimRenderer;
            if (groupCallMiniTextureView3 == null) {
            }
            if (forUserOrChat != null) {
            }
            if (z2) {
            }
            if (!z2) {
            }
        } else {
            if (participant.muted_by_you) {
                arrayList.add(LocaleController.getString(R.string.VoipGroupUnmuteForMe));
                arrayList2.add(Integer.valueOf(R.drawable.msg_voice_unmuted));
                i = 4;
            } else {
                arrayList.add(LocaleController.getString(R.string.VoipGroupMuteForMe));
                arrayList2.add(Integer.valueOf(R.drawable.msg_voice_muted));
                i = 5;
            }
            arrayList3.add(Integer.valueOf(i));
            TLRPC.Peer peer2 = participant.peer;
            if (peer2 != null) {
                long j2 = peer2.channel_id;
                if (j2 != 0 && !ChatObject.isMegagroup(this.currentAccount, j2)) {
                    arrayList.add(LocaleController.getString(R.string.VoipGroupOpenChannel));
                    arrayList2.add(Integer.valueOf(R.drawable.msg_msgbubble3));
                    i3 = 8;
                }
            }
            arrayList.add(LocaleController.getString(R.string.VoipGroupOpenChat));
            arrayList2.add(Integer.valueOf(R.drawable.msg_msgbubble3));
            i2 = 6;
            arrayList3.add(i2);
            size = arrayList.size();
            i6 = 0;
            while (i6 < size) {
            }
            scrollView2.addView(linearLayout, LayoutHelper.createScroll(-2, -2, 51));
            this.listView.stopScroll();
            this.layoutManager.setCanScrollVertically(false);
            this.scrimView = groupCallUserCell3;
            groupCallUserCell3.setAboutVisible(true);
            this.containerView.invalidate();
            this.listView.invalidate();
            animatorSet = this.scrimAnimatorSet;
            if (animatorSet != null) {
            }
            View actionBarPopupWindowLayout322 = actionBarPopupWindowLayout;
            this.scrimPopupLayout = actionBarPopupWindowLayout322;
            if (peerId <= 0) {
            }
            GroupCallMiniTextureView groupCallMiniTextureView32 = this.scrimRenderer;
            if (groupCallMiniTextureView32 == null) {
            }
            if (forUserOrChat != null) {
            }
            if (z2) {
            }
            if (!z2) {
            }
        }
        i2 = Integer.valueOf(i3);
        arrayList3.add(i2);
        size = arrayList.size();
        i6 = 0;
        while (i6 < size) {
        }
        scrollView2.addView(linearLayout, LayoutHelper.createScroll(-2, -2, 51));
        this.listView.stopScroll();
        this.layoutManager.setCanScrollVertically(false);
        this.scrimView = groupCallUserCell3;
        groupCallUserCell3.setAboutVisible(true);
        this.containerView.invalidate();
        this.listView.invalidate();
        animatorSet = this.scrimAnimatorSet;
        if (animatorSet != null) {
        }
        View actionBarPopupWindowLayout3222 = actionBarPopupWindowLayout;
        this.scrimPopupLayout = actionBarPopupWindowLayout3222;
        if (peerId <= 0) {
        }
        GroupCallMiniTextureView groupCallMiniTextureView322 = this.scrimRenderer;
        if (groupCallMiniTextureView322 == null) {
        }
        if (forUserOrChat != null) {
        }
        if (z2) {
        }
        if (!z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRecordHint(View view) {
        HintView hintView;
        int i;
        if (this.recordHintView == null) {
            HintView hintView2 = new HintView(getContext(), 8, true);
            this.recordHintView = hintView2;
            hintView2.setAlpha(0.0f);
            this.recordHintView.setVisibility(4);
            this.recordHintView.setShowingDuration(3000L);
            this.containerView.addView(this.recordHintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
            if (ChatObject.isChannelOrGiga(this.currentChat)) {
                hintView = this.recordHintView;
                i = R.string.VoipChannelRecording;
            } else {
                hintView = this.recordHintView;
                i = R.string.VoipGroupRecording;
            }
            hintView.setText(LocaleController.getString(i));
            this.recordHintView.setBackgroundColor(-366530760, -1);
        }
        this.recordHintView.setExtraTranslationY(-AndroidUtilities.statusBarHeight);
        this.recordHintView.showForView(view, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showReminderHint() {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("reminderhint", false)) {
            return;
        }
        globalMainSettings.edit().putBoolean("reminderhint", true).commit();
        if (this.reminderHintView == null) {
            HintView hintView = new HintView(getContext(), 8);
            this.reminderHintView = hintView;
            hintView.setAlpha(0.0f);
            this.reminderHintView.setVisibility(4);
            this.reminderHintView.setShowingDuration(4000L);
            this.containerView.addView(this.reminderHintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
            this.reminderHintView.setText(LocaleController.getString(R.string.VoipChatReminderHint));
            this.reminderHintView.setBackgroundColor(-366530760, -1);
        }
        this.reminderHintView.setExtraTranslationY(-AndroidUtilities.statusBarHeight);
        this.reminderHintView.showForView(this.muteButton, true);
    }

    private void startScreenCapture() {
        Intent createScreenCaptureIntent;
        LaunchActivity launchActivity = this.parentActivity;
        if (launchActivity == null || Build.VERSION.SDK_INT < 21) {
            return;
        }
        MediaProjectionManager m = PrivateVideoPreviewDialog$$ExternalSyntheticApiModelOutline0.m(launchActivity.getSystemService("media_projection"));
        LaunchActivity launchActivity2 = this.parentActivity;
        createScreenCaptureIntent = m.createScreenCaptureIntent();
        launchActivity2.startActivityForResult(createScreenCaptureIntent, 520);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleAdminSpeak() {
        TLRPC.TL_phone_toggleGroupCallSettings tL_phone_toggleGroupCallSettings = new TLRPC.TL_phone_toggleGroupCallSettings();
        tL_phone_toggleGroupCallSettings.call = this.call.getInputGroupCall();
        tL_phone_toggleGroupCallSettings.join_muted = this.call.call.join_muted;
        tL_phone_toggleGroupCallSettings.flags |= 1;
        this.accountInstance.getConnectionsManager().sendRequest(tL_phone_toggleGroupCallSettings, new RequestDelegate() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda54
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                GroupCallActivity.this.lambda$toggleAdminSpeak$64(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0076, code lost:
    
        if (org.telegram.messenger.ChatObject.isPublic(r0) != false) goto L34;
     */
    /* JADX WARN: Removed duplicated region for block: B:113:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0267  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateItems() {
        ChatObject.Call call;
        TLRPC.Chat chat;
        FrameLayout.LayoutParams layoutParams;
        float f;
        ActionBarMenuSubItem actionBarMenuSubItem;
        int i;
        ActionBarMenuSubItem actionBarMenuSubItem2;
        String string;
        int i2;
        ChatObject.Call call2 = this.call;
        if (call2 == null || call2.isScheduled()) {
            this.pipItem.setVisibility(4);
            this.screenShareItem.setVisibility(8);
            if (this.call == null) {
                this.otherItem.setVisibility(8);
                return;
            }
        }
        if (this.changingPermissions) {
            return;
        }
        TLRPC.Chat chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(this.currentChat.id));
        if (chat2 != null) {
            this.currentChat = chat2;
        }
        if (!ChatObject.canUserDoAdminAction(this.currentChat, 3) && ((ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) || (!ChatObject.isPublic(this.currentChat) && !ChatObject.canUserDoAdminAction(this.currentChat, 3)))) {
            if (ChatObject.isChannel(this.currentChat)) {
                TLRPC.Chat chat3 = this.currentChat;
                if (!chat3.megagroup) {
                }
            }
            this.inviteItem.setVisibility(8);
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(this.selfPeer));
            call = this.call;
            if (call == null && !call.isScheduled() && (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted)) {
                this.noiseItem.setVisibility(0);
            } else {
                this.noiseItem.setVisibility(8);
            }
            this.noiseItem.setIcon(!SharedConfig.noiseSupression ? R.drawable.msg_noise_on : R.drawable.msg_noise_off);
            this.noiseItem.setSubtext(LocaleController.getString(!SharedConfig.noiseSupression ? R.string.VoipNoiseCancellationEnabled : R.string.VoipNoiseCancellationDisabled));
            if (ChatObject.canManageCalls(this.currentChat)) {
                boolean z = (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted || ChatObject.canManageCalls(this.currentChat)) ? false : true;
                boolean z2 = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getVideoState(true) == 2;
                if (Build.VERSION.SDK_INT < 21 || z || (!(this.call.canRecordVideo() || z2) || this.call.isScheduled() || isRtmpStream())) {
                    this.screenShareItem.setVisibility(8);
                    this.screenItem.setVisibility(8);
                } else if (z2) {
                    this.screenShareItem.setVisibility(8);
                    this.screenItem.setVisibility(0);
                    ActionBarMenuSubItem actionBarMenuSubItem3 = this.screenItem;
                    int i3 = R.string.VoipChatStopScreenCapture;
                    actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString(i3), R.drawable.msg_screencast_off);
                    this.screenItem.setContentDescription(LocaleController.getString(i3));
                } else {
                    ActionBarMenuSubItem actionBarMenuSubItem4 = this.screenItem;
                    int i4 = R.string.VoipChatStartScreenCapture;
                    actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString(i4), R.drawable.msg_screencast);
                    this.screenItem.setContentDescription(LocaleController.getString(i4));
                    this.screenShareItem.setVisibility(8);
                    this.screenItem.setVisibility(0);
                }
                this.leaveItem.setVisibility(8);
                this.editTitleItem.setVisibility(8);
                this.recordItem.setVisibility(8);
            } else {
                this.leaveItem.setVisibility(0);
                this.editTitleItem.setVisibility(0);
                if (isRtmpStream()) {
                    this.recordItem.setVisibility(0);
                } else if (this.call.isScheduled()) {
                    this.recordItem.setVisibility(8);
                } else {
                    this.recordItem.setVisibility(0);
                    if (this.call.canRecordVideo() || this.call.isScheduled() || Build.VERSION.SDK_INT < 21 || isRtmpStream()) {
                        this.screenItem.setVisibility(8);
                    } else {
                        this.screenItem.setVisibility(0);
                    }
                    this.screenShareItem.setVisibility(8);
                    this.recordCallDrawable.setRecording(this.call.recording);
                    if (this.call.recording) {
                        Runnable runnable = this.updateCallRecordRunnable;
                        if (runnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                            this.updateCallRecordRunnable = null;
                        }
                        actionBarMenuSubItem = this.recordItem;
                        i = R.string.VoipGroupRecordCall;
                    } else {
                        if (this.updateCallRecordRunnable == null) {
                            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda44
                                @Override // java.lang.Runnable
                                public final void run() {
                                    GroupCallActivity.this.lambda$updateItems$4();
                                }
                            };
                            this.updateCallRecordRunnable = runnable2;
                            AndroidUtilities.runOnUIThread(runnable2, 1000L);
                        }
                        actionBarMenuSubItem = this.recordItem;
                        i = R.string.VoipGroupStopRecordCall;
                    }
                    actionBarMenuSubItem.setText(LocaleController.getString(i));
                    if (VoIPService.getSharedInstance() == null && VoIPService.getSharedInstance().getVideoState(true) == 2) {
                        actionBarMenuSubItem2 = this.screenItem;
                        string = LocaleController.getString(R.string.VoipChatStopScreenCapture);
                        i2 = R.drawable.msg_screencast_off;
                    } else {
                        actionBarMenuSubItem2 = this.screenItem;
                        string = LocaleController.getString(R.string.VoipChatStartScreenCapture);
                        i2 = R.drawable.msg_screencast;
                    }
                    actionBarMenuSubItem2.setTextAndIcon(string, i2);
                    updateRecordCallText();
                }
                this.screenItem.setVisibility(8);
                if (this.call.canRecordVideo()) {
                }
                this.screenItem.setVisibility(8);
                this.screenShareItem.setVisibility(8);
                this.recordCallDrawable.setRecording(this.call.recording);
                if (this.call.recording) {
                }
                actionBarMenuSubItem.setText(LocaleController.getString(i));
                if (VoIPService.getSharedInstance() == null) {
                }
                actionBarMenuSubItem2 = this.screenItem;
                string = LocaleController.getString(R.string.VoipChatStartScreenCapture);
                i2 = R.drawable.msg_screencast;
                actionBarMenuSubItem2.setTextAndIcon(string, i2);
                updateRecordCallText();
            }
            if (ChatObject.canManageCalls(this.currentChat) || !this.call.call.can_change_join_muted) {
                this.permissionItem.setVisibility(8);
            } else {
                this.permissionItem.setVisibility(0);
            }
            this.soundItem.setVisibility(!isRtmpStream() ? 8 : 0);
            if (this.editTitleItem.getVisibility() != 0 || this.permissionItem.getVisibility() == 0 || this.inviteItem.getVisibility() == 0 || this.screenItem.getVisibility() == 0 || this.recordItem.getVisibility() == 0 || this.leaveItem.getVisibility() == 0) {
                this.soundItemDivider.setVisibility(0);
            } else {
                this.soundItemDivider.setVisibility(8);
            }
            if (!((VoIPService.getSharedInstance() == null && VoIPService.getSharedInstance().hasFewPeers) || this.scheduleHasFewPeers) || isRtmpStream()) {
                this.accountSelectCell.setVisibility(8);
                this.accountGap.setVisibility(8);
            } else {
                this.accountSelectCell.setVisibility(0);
                this.accountGap.setVisibility(0);
                long peerId = MessageObject.getPeerId(this.selfPeer);
                this.accountSelectCell.setObject(DialogObject.isUserDialog(peerId) ? this.accountInstance.getMessagesController().getUser(Long.valueOf(peerId)) : this.accountInstance.getMessagesController().getChat(Long.valueOf(-peerId)));
            }
            chat = this.currentChat;
            if (chat == null && !ChatObject.isChannelOrGiga(chat) && isRtmpStream() && this.inviteItem.getVisibility() == 8) {
                this.otherItem.setVisibility(8);
            } else {
                this.otherItem.setVisibility(0);
            }
            layoutParams = (FrameLayout.LayoutParams) this.titleLayout.getLayoutParams();
            f = 96;
            if (layoutParams.rightMargin != AndroidUtilities.dp(f)) {
                layoutParams.rightMargin = AndroidUtilities.dp(f);
                this.titleLayout.requestLayout();
            }
            ((FrameLayout.LayoutParams) this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
            this.actionBar.setTitleRightMargin(AndroidUtilities.dp(48.0f) * 2);
        }
        this.inviteItem.setVisibility(0);
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(this.selfPeer));
        call = this.call;
        if (call == null) {
        }
        this.noiseItem.setVisibility(8);
        this.noiseItem.setIcon(!SharedConfig.noiseSupression ? R.drawable.msg_noise_on : R.drawable.msg_noise_off);
        this.noiseItem.setSubtext(LocaleController.getString(!SharedConfig.noiseSupression ? R.string.VoipNoiseCancellationEnabled : R.string.VoipNoiseCancellationDisabled));
        if (ChatObject.canManageCalls(this.currentChat)) {
        }
        if (ChatObject.canManageCalls(this.currentChat)) {
        }
        this.permissionItem.setVisibility(8);
        this.soundItem.setVisibility(!isRtmpStream() ? 8 : 0);
        if (this.editTitleItem.getVisibility() != 0) {
        }
        this.soundItemDivider.setVisibility(0);
        if (VoIPService.getSharedInstance() == null) {
        }
        this.accountSelectCell.setVisibility(8);
        this.accountGap.setVisibility(8);
        chat = this.currentChat;
        if (chat == null) {
        }
        this.otherItem.setVisibility(0);
        layoutParams = (FrameLayout.LayoutParams) this.titleLayout.getLayoutParams();
        f = 96;
        if (layoutParams.rightMargin != AndroidUtilities.dp(f)) {
        }
        ((FrameLayout.LayoutParams) this.menuItemsContainer.getLayoutParams()).rightMargin = 0;
        this.actionBar.setTitleRightMargin(AndroidUtilities.dp(48.0f) * 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout(boolean z) {
        boolean z2 = true;
        int childCount = this.listView.getChildCount();
        float f = 2.14748365E9f;
        for (int i = 0; i < childCount; i++) {
            if (this.listView.getChildAdapterPosition(this.listView.getChildAt(i)) >= 0) {
                f = Math.min(f, r8.getTop());
            }
        }
        if (f < 0.0f || f == 2.14748365E9f) {
            f = childCount != 0 ? 0.0f : this.listView.getPaddingTop();
        }
        final boolean z3 = f <= ((float) (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(14.0f)));
        float currentActionBarHeight = f + ActionBar.getCurrentActionBarHeight() + AndroidUtilities.dp(14.0f);
        if ((z3 && this.actionBar.getTag() == null) || (!z3 && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z3 ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            setUseLightStatusBar(this.actionBar.getTag() == null);
            ViewPropertyAnimator duration = this.actionBar.getBackButton().animate().scaleX(z3 ? 1.0f : 0.9f).scaleY(z3 ? 1.0f : 0.9f).translationX(z3 ? 0.0f : -AndroidUtilities.dp(14.0f)).setDuration(300L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            duration.setInterpolator(cubicBezierInterpolator).start();
            this.actionBar.getTitleTextView().animate().translationY(z3 ? 0.0f : AndroidUtilities.dp(23.0f)).setDuration(300L).setInterpolator(cubicBezierInterpolator).start();
            ObjectAnimator objectAnimator = this.subtitleYAnimator;
            if (objectAnimator != null) {
                objectAnimator.removeAllListeners();
                this.subtitleYAnimator.cancel();
            }
            SimpleTextView subtitleTextView = this.actionBar.getSubtitleTextView();
            Property property = View.TRANSLATION_Y;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(subtitleTextView, (Property<SimpleTextView, Float>) property, this.actionBar.getSubtitleTextView().getTranslationY(), z3 ? 0.0f : AndroidUtilities.dp(20.0f));
            this.subtitleYAnimator = ofFloat;
            ofFloat.setDuration(300L);
            this.subtitleYAnimator.setInterpolator(cubicBezierInterpolator);
            this.subtitleYAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.50
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    groupCallActivity.subtitleYAnimator = null;
                    groupCallActivity.actionBar.getSubtitleTextView().setTranslationY(z3 ? 0.0f : AndroidUtilities.dp(20.0f));
                }
            });
            this.subtitleYAnimator.start();
            ObjectAnimator objectAnimator2 = this.additionalSubtitleYAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.cancel();
            }
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.actionBar.getAdditionalSubtitleTextView(), (Property<SimpleTextView, Float>) property, z3 ? 0.0f : AndroidUtilities.dp(20.0f));
            this.additionalSubtitleYAnimator = ofFloat2;
            ofFloat2.setDuration(300L);
            this.additionalSubtitleYAnimator.setInterpolator(cubicBezierInterpolator);
            this.additionalSubtitleYAnimator.start();
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.actionBarAnimation = animatorSet2;
            animatorSet2.setDuration(140L);
            AnimatorSet animatorSet3 = this.actionBarAnimation;
            ActionBar actionBar = this.actionBar;
            Property property2 = View.ALPHA;
            animatorSet3.playTogether(ObjectAnimator.ofFloat(actionBar, (Property<ActionBar, Float>) property2, z3 ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.actionBarBackground, (Property<View, Float>) property2, z3 ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.actionBarShadow, (Property<View, Float>) property2, z3 ? 1.0f : 0.0f));
            this.actionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.51
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    GroupCallActivity.this.actionBarAnimation = null;
                }
            });
            this.actionBarAnimation.start();
            ImageView imageView = this.renderersContainer.pipView;
            if (z3 && !isLandscapeMode) {
                z2 = false;
            }
            imageView.setClickable(z2);
        }
        if (this.scrollOffsetY != currentActionBarHeight) {
            setScrollOffsetY(currentActionBarHeight);
        }
    }

    private void updateLiveLabel(boolean z) {
        if (!isRtmpStream()) {
            this.liveLabelTextView.setVisibility(8);
            return;
        }
        this.liveLabelTextView.setVisibility(0);
        boolean z2 = ((Integer) this.liveLabelTextView.getTag()).intValue() == 3;
        int i = this.currentCallState;
        final boolean z3 = i == 3;
        this.liveLabelTextView.setTag(Integer.valueOf(i));
        if (z2 != z3) {
            ValueAnimator valueAnimator = this.liveLabelBgColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (!z) {
                this.liveLabelPaint.setColor(this.currentCallState == 3 ? -1163700 : -12761513);
                this.liveLabelTextView.invalidate();
                return;
            }
            final int color = this.liveLabelPaint.getColor();
            final int i2 = z3 ? -1163700 : -12761513;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.liveLabelBgColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    GroupCallActivity.this.lambda$updateLiveLabel$50(color, i2, z3, valueAnimator2);
                }
            });
            this.liveLabelBgColorAnimator.setDuration(300L);
            this.liveLabelBgColorAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.liveLabelBgColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.52
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    GroupCallActivity.this.liveLabelBgColorAnimator = null;
                    GroupCallActivity.this.liveLabelPaint.setColor(GroupCallActivity.this.currentCallState == 3 ? -1163700 : -12761513);
                    GroupCallActivity.this.liveLabelTextView.invalidate();
                }
            });
            this.liveLabelBgColorAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0259, code lost:
    
        if (r7 != 4) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x020b, code lost:
    
        if (r7 == 2) goto L177;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x025c, code lost:
    
        r5 = r19.bigMicDrawable;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x019f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateMuteButton(int i, boolean z) {
        RLottieDrawable rLottieDrawable;
        int i2;
        String string;
        String string2;
        boolean z2;
        String str;
        boolean z3;
        String string3;
        RLottieDrawable rLottieDrawable2;
        int i3;
        boolean customEndFrame;
        boolean customEndFrame2;
        ImageView imageView;
        int i4;
        RLottieDrawable rLottieDrawable3;
        GroupCallRenderersContainer groupCallRenderersContainer = this.renderersContainer;
        boolean z4 = groupCallRenderersContainer != null && groupCallRenderersContainer.inFullscreenMode && (AndroidUtilities.isTablet() || isLandscapeMode == isRtmpLandscapeMode());
        if (!isRtmpStream() && this.muteButtonState == i && z) {
            return;
        }
        ValueAnimator valueAnimator = this.muteButtonAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.muteButtonAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.expandAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.expandAnimator = null;
        }
        int i5 = NotificationCenter.needDeleteDialog;
        if (i == 7) {
            str = LocaleController.getString(R.string.VoipGroupCancelReminder);
            customEndFrame2 = this.bigMicDrawable.setCustomEndFrame(NotificationCenter.groupRestrictionsUnlockedByBoosts);
        } else {
            if (i != 6) {
                if (i != 5) {
                    if (i == 0) {
                        String string4 = LocaleController.getString(R.string.VoipGroupUnmute);
                        string3 = LocaleController.getString(R.string.VoipHoldAndTalk);
                        int i6 = this.muteButtonState;
                        if (i6 == 3) {
                            int customEndFrame3 = this.bigMicDrawable.getCustomEndFrame();
                            if (customEndFrame3 != 136 && customEndFrame3 != 173 && customEndFrame3 != 274 && customEndFrame3 != 311) {
                                str = string4;
                                string2 = string3;
                                z2 = false;
                                z3 = false;
                                if (isRtmpStream() && i != 3 && !this.call.isScheduled()) {
                                    str = LocaleController.getString(!z4 ? R.string.VoipGroupMinimizeStream : R.string.VoipGroupExpandStream);
                                    z2 = this.animatingToFullscreenExpand == z4;
                                    this.animatingToFullscreenExpand = z4;
                                    string2 = "";
                                }
                                this.muteButton.setContentDescription(TextUtils.isEmpty(string2) ? str : str + " " + string2);
                                if (z) {
                                    if (z2) {
                                        if (i != 5) {
                                            if (i == 7) {
                                                rLottieDrawable3 = this.bigMicDrawable;
                                            } else if (i == 6) {
                                                this.bigMicDrawable.setCurrentFrame(311);
                                            } else {
                                                if (i == 0) {
                                                    int i7 = this.muteButtonState;
                                                    if (i7 != 5) {
                                                        if (i7 != 7) {
                                                            if (i7 != 6) {
                                                            }
                                                            rLottieDrawable3 = this.bigMicDrawable;
                                                            i5 = NotificationCenter.groupRestrictionsUnlockedByBoosts;
                                                        }
                                                        rLottieDrawable3 = this.bigMicDrawable;
                                                        i5 = 344;
                                                    }
                                                } else if (i == 1) {
                                                    rLottieDrawable3 = this.bigMicDrawable;
                                                    if (this.muteButtonState != 4) {
                                                        i5 = 36;
                                                    }
                                                    i5 = 69;
                                                } else {
                                                    if (i != 4) {
                                                        int i8 = this.muteButtonState;
                                                        if (z3) {
                                                            if (i8 == 7) {
                                                                this.bigMicDrawable.setCurrentFrame(NotificationCenter.onRequestPermissionResultReceived);
                                                            } else if (i8 == 6) {
                                                                rLottieDrawable3 = this.bigMicDrawable;
                                                                i5 = NotificationCenter.themeAccentListUpdated;
                                                            } else if (i8 == 1) {
                                                                rLottieDrawable3 = this.bigMicDrawable;
                                                                i5 = NotificationCenter.fileUploadProgressChanged;
                                                            }
                                                        } else if (i8 != 5) {
                                                            if (i8 != 7) {
                                                                if (i8 != 6) {
                                                                    if (i8 != 2) {
                                                                    }
                                                                    i4 = 0;
                                                                    this.bigMicDrawable.setCurrentFrame(0);
                                                                }
                                                                rLottieDrawable3 = this.bigMicDrawable;
                                                                i5 = NotificationCenter.groupRestrictionsUnlockedByBoosts;
                                                            }
                                                            rLottieDrawable3 = this.bigMicDrawable;
                                                            i5 = 344;
                                                        }
                                                    }
                                                    rLottieDrawable3 = this.bigMicDrawable;
                                                    i5 = 99;
                                                }
                                                this.muteButton.playAnimation();
                                                this.muteLabel[1].setVisibility(i4);
                                                this.muteLabel[1].setAlpha(0.0f);
                                                this.muteLabel[1].setTranslationY(-AndroidUtilities.dp(5.0f));
                                                this.muteLabel[1].setText(str);
                                                if (isRtmpStream() || this.call.isScheduled()) {
                                                    this.muteButton.setAlpha(1.0f);
                                                    this.expandButton.setAlpha(0.0f);
                                                    this.minimizeButton.setAlpha(0.0f);
                                                } else {
                                                    this.muteButton.setAlpha(0.0f);
                                                    boolean z5 = this.renderersContainer.inFullscreenMode && (AndroidUtilities.isTablet() || isLandscapeMode == isRtmpLandscapeMode());
                                                    final ImageView imageView2 = z5 ? this.expandButton : this.minimizeButton;
                                                    final ImageView imageView3 = z5 ? this.minimizeButton : this.expandButton;
                                                    final float dp = AndroidUtilities.dp(52.0f) / (this.muteButton.getMeasuredWidth() - AndroidUtilities.dp(8.0f));
                                                    boolean z6 = !AndroidUtilities.isTablet() ? this.renderersContainer.inFullscreenMode || isLandscapeMode : this.renderersContainer.inFullscreenMode;
                                                    Boolean bool = this.wasExpandBigSize;
                                                    boolean z7 = bool == null || z6 != bool.booleanValue();
                                                    this.wasExpandBigSize = Boolean.valueOf(z6);
                                                    ValueAnimator valueAnimator3 = this.expandSizeAnimator;
                                                    if (valueAnimator3 != null) {
                                                        valueAnimator3.cancel();
                                                        this.expandSizeAnimator = null;
                                                    }
                                                    if (z7) {
                                                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                                                        this.expandSizeAnimator = ofFloat;
                                                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda4
                                                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                                            public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                                                GroupCallActivity.this.lambda$updateMuteButton$51(dp, imageView3, valueAnimator4);
                                                            }
                                                        });
                                                        this.expandSizeAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.53
                                                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                                            public void onAnimationEnd(Animator animator) {
                                                                GroupCallActivity.this.expandSizeAnimator = null;
                                                            }
                                                        });
                                                        this.expandSizeAnimator.start();
                                                    } else {
                                                        float lerp = (isLandscapeMode || isRtmpStream()) ? dp : AndroidUtilities.lerp(1.0f, dp, this.renderersContainer.progressToFullscreenMode);
                                                        imageView3.setAlpha(1.0f);
                                                        imageView3.setScaleX(lerp);
                                                        imageView3.setScaleY(lerp);
                                                        imageView2.setAlpha(0.0f);
                                                    }
                                                    if (z2) {
                                                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                                        this.expandAnimator = ofFloat2;
                                                        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda5
                                                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                                            public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                                                GroupCallActivity.this.lambda$updateMuteButton$52(dp, imageView2, imageView3, valueAnimator4);
                                                            }
                                                        });
                                                        this.expandAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.54
                                                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                                            public void onAnimationEnd(Animator animator) {
                                                                GroupCallActivity.this.expandAnimator = null;
                                                            }
                                                        });
                                                        this.expandAnimator.start();
                                                    } else {
                                                        if (!isLandscapeMode && !isRtmpStream()) {
                                                            dp = AndroidUtilities.lerp(1.0f, dp, this.renderersContainer.progressToFullscreenMode);
                                                        }
                                                        imageView3.setAlpha(1.0f);
                                                        imageView3.setScaleX(dp);
                                                        imageView3.setScaleY(dp);
                                                        imageView2.setAlpha(0.0f);
                                                    }
                                                }
                                                if (z2) {
                                                    this.muteLabel[0].setAlpha(0.0f);
                                                    this.muteLabel[1].setAlpha(1.0f);
                                                    TextView[] textViewArr = this.muteLabel;
                                                    TextView textView = textViewArr[0];
                                                    textViewArr[0] = textViewArr[1];
                                                    textViewArr[1] = textView;
                                                    textView.setVisibility(4);
                                                    for (int i9 = 0; i9 < 2; i9++) {
                                                        this.muteLabel[i9].setTranslationY(0.0f);
                                                    }
                                                } else {
                                                    ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                                    this.muteButtonAnimator = ofFloat3;
                                                    ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda6
                                                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                                        public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                                            GroupCallActivity.this.lambda$updateMuteButton$53(valueAnimator4);
                                                        }
                                                    });
                                                    this.muteButtonAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.55
                                                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                                        public void onAnimationEnd(Animator animator) {
                                                            if (GroupCallActivity.this.muteButtonAnimator != null) {
                                                                GroupCallActivity.this.muteButtonAnimator = null;
                                                                TextView textView2 = GroupCallActivity.this.muteLabel[0];
                                                                GroupCallActivity.this.muteLabel[0] = GroupCallActivity.this.muteLabel[1];
                                                                GroupCallActivity.this.muteLabel[1] = textView2;
                                                                textView2.setVisibility(4);
                                                                for (int i10 = 0; i10 < 2; i10++) {
                                                                    GroupCallActivity.this.muteLabel[i10].setTranslationY(0.0f);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    this.muteButtonAnimator.setDuration(180L);
                                                    this.muteButtonAnimator.start();
                                                }
                                                this.muteButtonState = i;
                                            }
                                            rLottieDrawable3.setCurrentFrame(i5);
                                        }
                                        rLottieDrawable3 = this.bigMicDrawable;
                                        i5 = 376;
                                        rLottieDrawable3.setCurrentFrame(i5);
                                    }
                                    i4 = 0;
                                    this.muteButton.playAnimation();
                                    this.muteLabel[1].setVisibility(i4);
                                    this.muteLabel[1].setAlpha(0.0f);
                                    this.muteLabel[1].setTranslationY(-AndroidUtilities.dp(5.0f));
                                    this.muteLabel[1].setText(str);
                                    if (isRtmpStream()) {
                                    }
                                    this.muteButton.setAlpha(1.0f);
                                    this.expandButton.setAlpha(0.0f);
                                    this.minimizeButton.setAlpha(0.0f);
                                    if (z2) {
                                    }
                                    this.muteButtonState = i;
                                } else {
                                    this.muteButtonState = i;
                                    RLottieDrawable rLottieDrawable4 = this.bigMicDrawable;
                                    boolean z8 = false;
                                    rLottieDrawable4.setCurrentFrame(rLottieDrawable4.getCustomEndFrame() - 1, false, true);
                                    this.muteLabel[0].setText(str);
                                    if (!isRtmpStream() || this.call.isScheduled()) {
                                        this.muteButton.setAlpha(1.0f);
                                        this.expandButton.setAlpha(0.0f);
                                        imageView = this.minimizeButton;
                                    } else {
                                        this.muteButton.setAlpha(0.0f);
                                        GroupCallRenderersContainer groupCallRenderersContainer2 = this.renderersContainer;
                                        if (groupCallRenderersContainer2 != null && groupCallRenderersContainer2.inFullscreenMode && (AndroidUtilities.isTablet() || isLandscapeMode == isRtmpLandscapeMode())) {
                                            z8 = true;
                                        }
                                        imageView = z8 ? this.expandButton : this.minimizeButton;
                                        (z8 ? this.minimizeButton : this.expandButton).setAlpha(1.0f);
                                    }
                                    imageView.setAlpha(0.0f);
                                }
                                updateMuteButtonState(z);
                            }
                        } else if (i6 == 5) {
                            customEndFrame = this.bigMicDrawable.setCustomEndFrame(404);
                            z2 = customEndFrame;
                            str = string4;
                        } else {
                            if (i6 == 7) {
                                rLottieDrawable2 = this.bigMicDrawable;
                                i3 = 376;
                            } else if (i6 == 6) {
                                rLottieDrawable2 = this.bigMicDrawable;
                                i3 = NotificationCenter.themeAccentListUpdated;
                            } else if (i6 == 2) {
                                rLottieDrawable2 = this.bigMicDrawable;
                                i3 = 36;
                            }
                            customEndFrame = rLottieDrawable2.setCustomEndFrame(i3);
                            z2 = customEndFrame;
                            str = string4;
                        }
                        rLottieDrawable2 = this.bigMicDrawable;
                        i3 = 99;
                        customEndFrame = rLottieDrawable2.setCustomEndFrame(i3);
                        z2 = customEndFrame;
                        str = string4;
                    } else if (i == 1) {
                        str = LocaleController.getString(R.string.VoipTapToMute);
                        z2 = this.bigMicDrawable.setCustomEndFrame(this.muteButtonState == 4 ? 99 : 69);
                    } else {
                        if (i != 4) {
                            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(this.selfPeer));
                            boolean z9 = (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted || ChatObject.canManageCalls(this.currentChat)) ? false : true;
                            if (z9) {
                                int i10 = this.muteButtonState;
                                if (i10 == 7) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = 311;
                                } else if (i10 == 6) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = NotificationCenter.onRequestPermissionResultReceived;
                                } else if (i10 == 1) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = NotificationCenter.needDeleteDialog;
                                } else {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = NotificationCenter.fileUploadProgressChanged;
                                }
                            } else {
                                int i11 = this.muteButtonState;
                                if (i11 == 5) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = 404;
                                } else if (i11 == 7) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = 376;
                                } else if (i11 == 6) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = NotificationCenter.themeAccentListUpdated;
                                } else if (i11 == 2 || i11 == 4) {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = 36;
                                } else {
                                    rLottieDrawable = this.bigMicDrawable;
                                    i2 = 99;
                                }
                            }
                            boolean customEndFrame4 = rLottieDrawable.setCustomEndFrame(i2);
                            if (i == 3) {
                                string = LocaleController.getString(R.string.Connecting);
                                string2 = "";
                            } else {
                                string = LocaleController.getString(R.string.VoipMutedByAdmin);
                                string2 = LocaleController.getString(R.string.VoipMutedTapForSpeak);
                            }
                            boolean z10 = z9;
                            z2 = customEndFrame4;
                            str = string;
                            z3 = z10;
                            if (isRtmpStream()) {
                                str = LocaleController.getString(!z4 ? R.string.VoipGroupMinimizeStream : R.string.VoipGroupExpandStream);
                                if (this.animatingToFullscreenExpand == z4) {
                                }
                                this.animatingToFullscreenExpand = z4;
                                string2 = "";
                            }
                            if (TextUtils.isEmpty(string2)) {
                            }
                            this.muteButton.setContentDescription(TextUtils.isEmpty(string2) ? str : str + " " + string2);
                            if (z) {
                            }
                            updateMuteButtonState(z);
                        }
                        str = LocaleController.getString(R.string.VoipMutedTapedForSpeak);
                        string3 = LocaleController.getString(R.string.VoipMutedTapedForSpeakInfo);
                        z2 = this.bigMicDrawable.setCustomEndFrame(NotificationCenter.fileUploadProgressChanged);
                    }
                    string2 = string3;
                    z3 = false;
                    if (isRtmpStream()) {
                    }
                    if (TextUtils.isEmpty(string2)) {
                    }
                    this.muteButton.setContentDescription(TextUtils.isEmpty(string2) ? str : str + " " + string2);
                    if (z) {
                    }
                    updateMuteButtonState(z);
                }
                str = LocaleController.getString(R.string.VoipGroupStartNow);
                z2 = this.bigMicDrawable.setCustomEndFrame(377);
                string2 = "";
                z3 = false;
                if (isRtmpStream()) {
                }
                if (TextUtils.isEmpty(string2)) {
                }
                this.muteButton.setContentDescription(TextUtils.isEmpty(string2) ? str : str + " " + string2);
                if (z) {
                }
                updateMuteButtonState(z);
            }
            str = LocaleController.getString(R.string.VoipGroupSetReminder);
            customEndFrame2 = this.bigMicDrawable.setCustomEndFrame(344);
        }
        z2 = customEndFrame2;
        string2 = "";
        z3 = false;
        if (isRtmpStream()) {
        }
        if (TextUtils.isEmpty(string2)) {
        }
        this.muteButton.setContentDescription(TextUtils.isEmpty(string2) ? str : str + " " + string2);
        if (z) {
        }
        updateMuteButtonState(z);
    }

    private void updateMuteButtonState(boolean z) {
        boolean z2;
        WeavingState weavingState;
        Shader radialGradient;
        this.muteButton.invalidate();
        WeavingState[] weavingStateArr = this.states;
        int i = this.muteButtonState;
        if (weavingStateArr[i] == null) {
            weavingStateArr[i] = new WeavingState(i);
            int i2 = this.muteButtonState;
            if (i2 == 3) {
                this.states[i2].shader = null;
            } else {
                if (isGradientState(i2)) {
                    weavingState = this.states[this.muteButtonState];
                    radialGradient = new LinearGradient(0.0f, 400.0f, 400.0f, 0.0f, new int[]{Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient), Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient3), Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient2)}, (float[]) null, Shader.TileMode.CLAMP);
                } else {
                    int i3 = this.muteButtonState;
                    WeavingState[] weavingStateArr2 = this.states;
                    if (i3 == 1) {
                        weavingState = weavingStateArr2[i3];
                        radialGradient = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{Theme.getColor(Theme.key_voipgroup_muteButton), Theme.getColor(Theme.key_voipgroup_muteButton3)}, (float[]) null, Shader.TileMode.CLAMP);
                    } else {
                        weavingState = weavingStateArr2[i3];
                        radialGradient = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{Theme.getColor(Theme.key_voipgroup_unmuteButton2), Theme.getColor(Theme.key_voipgroup_unmuteButton)}, (float[]) null, Shader.TileMode.CLAMP);
                    }
                }
                weavingState.shader = radialGradient;
            }
        }
        WeavingState weavingState2 = this.states[this.muteButtonState];
        WeavingState weavingState3 = this.currentState;
        if (weavingState2 != weavingState3) {
            this.prevState = weavingState3;
            this.currentState = weavingState2;
            if (weavingState3 == null || !z) {
                this.switchProgress = 1.0f;
                this.prevState = null;
            } else {
                this.switchProgress = 0.0f;
            }
        }
        if (!z) {
            WeavingState weavingState4 = this.currentState;
            boolean z3 = false;
            if (weavingState4 != null) {
                int i4 = weavingState4.currentState;
                boolean z4 = i4 == 1 || i4 == 0;
                z2 = i4 != 3;
                z3 = z4;
            } else {
                z2 = false;
            }
            this.showWavesProgress = z3 ? 1.0f : 0.0f;
            this.showLightingProgress = z2 ? 1.0f : 0.0f;
        }
        this.buttonsContainer.invalidate();
    }

    private void updateRecordCallText() {
        if (this.call == null) {
            return;
        }
        int currentTime = this.accountInstance.getConnectionsManager().getCurrentTime();
        ChatObject.Call call = this.call;
        int i = currentTime - call.call.record_start_date;
        if (call.recording) {
            this.recordItem.setSubtext(AndroidUtilities.formatDuration(i, false));
        } else {
            this.recordItem.setSubtext(null);
        }
    }

    private void updateScheduleUI(boolean z) {
        float interpolation;
        float f;
        ActionBarMenuSubItem actionBarMenuSubItem;
        int i;
        LinearLayout linearLayout = this.scheduleTimerContainer;
        if ((linearLayout == null || this.call != null) && this.scheduleAnimator == null) {
            this.scheduleButtonsScale = 1.0f;
            this.switchToButtonInt2 = 1.0f;
            this.switchToButtonProgress = 1.0f;
            if (linearLayout == null) {
                return;
            }
        }
        if (!z) {
            AndroidUtilities.cancelRunOnUIThread(this.updateSchedeulRunnable);
            this.updateSchedeulRunnable.run();
            ChatObject.Call call = this.call;
            if (call == null || call.isScheduled()) {
                this.listView.setVisibility(4);
            } else {
                this.listView.setVisibility(0);
            }
            if (ChatObject.isChannelOrGiga(this.currentChat)) {
                actionBarMenuSubItem = this.leaveItem;
                i = R.string.VoipChannelCancelChat;
            } else {
                actionBarMenuSubItem = this.leaveItem;
                i = R.string.VoipGroupCancelChat;
            }
            actionBarMenuSubItem.setText(LocaleController.getString(i));
        }
        float f2 = this.switchToButtonProgress;
        if (f2 > 0.6f) {
            interpolation = 1.05f - (CubicBezierInterpolator.DEFAULT.getInterpolation((f2 - 0.6f) / 0.4f) * 0.05f);
            this.scheduleButtonsScale = interpolation;
            this.switchToButtonInt2 = 1.0f;
            f = 1.0f;
        } else {
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            this.scheduleButtonsScale = (cubicBezierInterpolator.getInterpolation(f2 / 0.6f) * 0.05f) + 1.0f;
            this.switchToButtonInt2 = cubicBezierInterpolator.getInterpolation(this.switchToButtonProgress / 0.6f);
            interpolation = 1.05f * cubicBezierInterpolator.getInterpolation(this.switchToButtonProgress / 0.6f);
            f = this.switchToButtonProgress / 0.6f;
        }
        float dp = isLandscapeMode ? (AndroidUtilities.dp(52.0f) * interpolation) / (this.muteButton.getMeasuredWidth() - AndroidUtilities.dp(8.0f)) : interpolation;
        float f3 = 1.0f - f;
        this.leaveButton.setAlpha(f);
        VoIPToggleButton voIPToggleButton = this.soundButton;
        voIPToggleButton.setAlpha((voIPToggleButton.isEnabled() ? 1.0f : 0.5f) * f);
        this.muteButton.setAlpha(f);
        this.scheduleTimerContainer.setAlpha(f3);
        this.scheduleStartInTextView.setAlpha(f);
        this.scheduleStartAtTextView.setAlpha(f);
        this.scheduleTimeTextView.setAlpha(f);
        this.muteLabel[0].setAlpha(f);
        this.scheduleTimeTextView.setScaleX(interpolation);
        this.scheduleTimeTextView.setScaleY(interpolation);
        this.leaveButton.setScaleX(interpolation);
        this.leaveButton.setScaleY(interpolation);
        this.soundButton.setScaleX(interpolation);
        this.soundButton.setScaleY(interpolation);
        this.muteButton.setScaleX(dp);
        this.muteButton.setScaleY(dp);
        this.scheduleButtonTextView.setScaleX(f3);
        this.scheduleButtonTextView.setScaleY(f3);
        this.scheduleButtonTextView.setAlpha(f3);
        this.scheduleInfoTextView.setAlpha(f3);
        this.cameraButton.setAlpha(f);
        this.cameraButton.setScaleY(interpolation);
        this.cameraButton.setScaleX(interpolation);
        this.flipButton.setAlpha(f);
        this.flipButton.setScaleY(interpolation);
        this.flipButton.setScaleX(interpolation);
        this.otherItem.setAlpha(f);
        int i2 = f3 != 0.0f ? 0 : 4;
        if (i2 != this.scheduleTimerContainer.getVisibility()) {
            this.scheduleTimerContainer.setVisibility(i2);
            this.scheduleButtonTextView.setVisibility(i2);
        }
    }

    private void updateSpeakerPhoneIcon(boolean z) {
        VoIPToggleButton voIPToggleButton;
        int i;
        int i2;
        String string;
        boolean z2;
        boolean z3;
        int i3;
        int i4;
        float f;
        VoIPToggleButton voIPToggleButton2 = this.soundButton;
        if (voIPToggleButton2 == null || voIPToggleButton2.getVisibility() != 0) {
            return;
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z4 = false;
        if (sharedInstance == null || isRtmpStream()) {
            this.soundButton.setData(R.drawable.msg_voiceshare, -1, 0, 0.3f, true, LocaleController.getString(R.string.VoipChatShare), false, z);
            this.soundButton.setEnabled(ChatObject.isPublic(this.currentChat) || (ChatObject.hasAdminRights(this.currentChat) && ChatObject.canAddUsers(this.currentChat)), false);
            this.soundButton.setChecked(true, false);
            return;
        }
        this.soundButton.setEnabled(true, z);
        boolean z5 = sharedInstance.isBluetoothOn() || sharedInstance.isBluetoothWillOn();
        if (!z5 && sharedInstance.isSpeakerphoneOn()) {
            z4 = true;
        }
        if (z5) {
            voIPToggleButton = this.soundButton;
            i = R.drawable.calls_bluetooth;
            i2 = R.string.VoipAudioRoutingBluetooth;
        } else {
            if (z4) {
                voIPToggleButton = this.soundButton;
                i = R.drawable.calls_speaker;
                string = LocaleController.getString(R.string.VoipSpeaker);
                z2 = true;
                z3 = false;
                i3 = -1;
                i4 = 0;
                f = 0.3f;
                voIPToggleButton.setData(i, i3, i4, f, z2, string, z3, z);
                this.soundButton.setChecked(z4, z);
            }
            if (sharedInstance.isHeadsetPlugged()) {
                voIPToggleButton = this.soundButton;
                i = R.drawable.calls_headphones;
                i2 = R.string.VoipAudioRoutingHeadset;
            } else {
                voIPToggleButton = this.soundButton;
                i = R.drawable.calls_speaker;
                i2 = R.string.VoipSpeaker;
            }
        }
        string = LocaleController.getString(i2);
        z2 = true;
        z3 = false;
        i3 = -1;
        i4 = 0;
        f = 0.1f;
        voIPToggleButton.setData(i, i3, i4, f, z2, string, z3, z);
        this.soundButton.setChecked(z4, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x027d  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0300  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:148:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0274  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateState(boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        int i;
        int i2;
        float f;
        float f2;
        GroupCallRenderersContainer groupCallRenderersContainer;
        int i3;
        ChatObject.Call call = this.call;
        if (call == null || call.isScheduled()) {
            updateMuteButton(ChatObject.canManageCalls(this.currentChat) ? 5 : this.call.call.schedule_start_subscribed ? 7 : 6, z);
            this.leaveButton.setData(isRtmpStream() ? R.drawable.msg_voiceclose : R.drawable.calls_decline, -1, Theme.getColor(Theme.key_voipgroup_leaveButton), 0.3f, false, LocaleController.getString(R.string.Close), false, false);
            updateScheduleUI(false);
            return;
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        if (sharedInstance.isSwitchingStream() || ((this.creatingServiceTime != 0 && Math.abs(SystemClock.elapsedRealtime() - this.creatingServiceTime) <= 3000) || !((i3 = this.currentCallState) == 1 || i3 == 2 || i3 == 6 || i3 == 5))) {
            if (this.userSwitchObject != null) {
                getUndoView().showWithAction(0L, 37, this.userSwitchObject, this.currentChat, (Runnable) null, (Runnable) null);
                this.userSwitchObject = null;
            }
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(this.selfPeer));
            if (sharedInstance.micSwitching || tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted || ChatObject.canManageCalls(this.currentChat)) {
                boolean isMicMute = sharedInstance.isMicMute();
                if (!sharedInstance.micSwitching && z2 && tL_groupCallParticipant != null && tL_groupCallParticipant.muted && !isMicMute) {
                    cancelMutePress();
                    sharedInstance.setMicMute(true, false, false);
                    isMicMute = true;
                }
                if (isMicMute) {
                    updateMuteButton(0, z);
                } else {
                    updateMuteButton(1, z);
                }
            } else {
                cancelMutePress();
                if (tL_groupCallParticipant.raise_hand_rating != 0) {
                    updateMuteButton(4, z);
                } else {
                    updateMuteButton(2, z);
                }
                sharedInstance.setMicMute(true, false, false);
            }
        } else {
            cancelMutePress();
            updateMuteButton(3, z);
        }
        boolean z5 = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getVideoState(false) == 2;
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = (TLRPC.TL_groupCallParticipant) this.call.participants.get(MessageObject.getPeerId(this.selfPeer));
        if (((((tL_groupCallParticipant2 == null || tL_groupCallParticipant2.can_self_unmute || !tL_groupCallParticipant2.muted || ChatObject.canManageCalls(this.currentChat)) ? false : true) || !this.call.canRecordVideo()) && !z5) || isRtmpStream()) {
            z3 = false;
            z4 = true;
        } else {
            z3 = true;
            z4 = false;
        }
        if (z5) {
            if (z && this.flipButton.getVisibility() != 0) {
                this.flipButton.setScaleX(0.3f);
                this.flipButton.setScaleY(0.3f);
            }
            i = 1;
        } else {
            i = 0;
        }
        int i4 = i + (z4 ? 2 : 0) + (z3 ? 4 : 0);
        GroupCallRenderersContainer groupCallRenderersContainer2 = this.renderersContainer;
        int i5 = i4 + ((groupCallRenderersContainer2 == null || !groupCallRenderersContainer2.inFullscreenMode) ? 0 : 8);
        int i6 = this.buttonsVisibility;
        if (i6 != 0 && i6 != i5 && z) {
            for (int i7 = 0; i7 < this.buttonsContainer.getChildCount(); i7++) {
                View childAt = this.buttonsContainer.getChildAt(i7);
                if (childAt.getVisibility() == 0) {
                    this.buttonsAnimationParamsX.put(childAt, Float.valueOf(childAt.getX()));
                    this.buttonsAnimationParamsY.put(childAt, Float.valueOf(childAt.getY()));
                }
            }
            this.animateButtonsOnNextLayout = true;
        }
        boolean z6 = (this.buttonsVisibility | 2) != (i5 | 2);
        this.buttonsVisibility = i5;
        if (z3) {
            i2 = 8;
            this.cameraButton.setData(R.drawable.calls_video, -1, 0, 1.0f, true, LocaleController.getString(R.string.VoipCamera), !z5, z);
            this.cameraButton.setChecked(true, false);
        } else {
            i2 = 8;
            this.cameraButton.setVisibility(8);
        }
        VoIPToggleButton voIPToggleButton = this.flipButton;
        if (i != 0) {
            voIPToggleButton.setData(0, -1, 0, 1.0f, true, LocaleController.getString(R.string.VoipFlip), false, false);
            this.flipButton.setChecked(true, false);
        } else {
            voIPToggleButton.setVisibility(i2);
        }
        boolean z7 = this.soundButton.getVisibility() == 0;
        this.soundButton.setVisibility(z4 ? 0 : 8);
        if (z6 && z4) {
            updateSpeakerPhoneIcon(false);
        }
        if (z6) {
            float f3 = z4 ? 1.0f : 0.3f;
            if (z) {
                if (z4 && !z7) {
                    this.soundButton.setScaleX(0.3f);
                    this.soundButton.setScaleY(0.3f);
                }
                this.soundButton.animate().scaleX(f3).scaleY(f3).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                if (this.cameraButton.getVisibility() != 0) {
                    this.cameraButton.showText(true, z);
                    f = 1.0f;
                } else {
                    f = 0.3f;
                }
                if (this.cameraButtonScale != f) {
                    this.cameraButtonScale = f;
                    ViewPropertyAnimator animate = this.cameraButton.animate();
                    if (z) {
                        animate.scaleX(f).scaleY(f).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                    } else {
                        animate.cancel();
                        this.cameraButton.setScaleX(f);
                        this.cameraButton.setScaleY(f);
                    }
                }
                float f4 = (!isTabletMode && (isLandscapeMode || ((groupCallRenderersContainer = this.renderersContainer) != null && groupCallRenderersContainer.inFullscreenMode))) ? 1.0f : 0.8f;
                if (!z5) {
                    f4 = 0.3f;
                }
                ViewPropertyAnimator animate2 = this.flipButton.animate();
                if (z) {
                    animate2.scaleX(f4).scaleY(f4).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                } else {
                    animate2.cancel();
                    this.flipButton.setScaleX(f4);
                    this.flipButton.setScaleY(f4);
                }
                this.flipButton.showText(f4 == 1.0f, z);
                f2 = z5 ? 0.3f : 1.0f;
                if (this.soundButtonScale != f2) {
                    this.soundButtonScale = f2;
                    ViewPropertyAnimator animate3 = this.soundButton.animate();
                    if (z) {
                        animate3.scaleX(f2).scaleY(f2).setDuration(350L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                    } else {
                        animate3.cancel();
                        this.soundButton.setScaleX(f2);
                        this.soundButton.setScaleY(f2);
                    }
                }
                updateLiveLabel(z);
                if (isRtmpStream() && LiteMode.isEnabled(512)) {
                    if (this.currentCallState == 3) {
                        this.needTextureLightning = true;
                        runUpdateTextureLightningRunnable();
                        return;
                    } else {
                        this.needTextureLightning = false;
                        AndroidUtilities.cancelRunOnUIThread(this.updateTextureLightningRunnable);
                        return;
                    }
                }
                return;
            }
            this.soundButton.animate().cancel();
            this.soundButton.setScaleX(f3);
            this.soundButton.setScaleY(f3);
        }
        if (this.cameraButton.getVisibility() != 0) {
        }
        if (this.cameraButtonScale != f) {
        }
        if (isTabletMode) {
            if (!z5) {
            }
            ViewPropertyAnimator animate22 = this.flipButton.animate();
            if (z) {
            }
            this.flipButton.showText(f4 == 1.0f, z);
            if (z5) {
            }
            if (this.soundButtonScale != f2) {
            }
            updateLiveLabel(z);
            if (isRtmpStream()) {
                return;
            } else {
                return;
            }
        }
        if (!z5) {
        }
        ViewPropertyAnimator animate222 = this.flipButton.animate();
        if (z) {
        }
        this.flipButton.showText(f4 == 1.0f, z);
        if (z5) {
        }
        if (this.soundButtonScale != f2) {
        }
        updateLiveLabel(z);
        if (isRtmpStream()) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubtitle() {
        boolean z;
        WatchersView watchersView;
        String str;
        TypefaceSpan typefaceSpan;
        if (this.actionBar == null || this.call == null) {
            return;
        }
        SpannableStringBuilder spannableStringBuilder = null;
        int i = 0;
        for (int i2 = 0; i2 < this.call.currentSpeakingPeers.size(); i2++) {
            long keyAt = this.call.currentSpeakingPeers.keyAt(i2);
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.call.currentSpeakingPeers.get(keyAt);
            if (!tL_groupCallParticipant.self && !this.renderersContainer.isVisible(tL_groupCallParticipant) && this.visiblePeerIds.get(keyAt, 0) != 1) {
                long peerId = MessageObject.getPeerId(tL_groupCallParticipant.peer);
                if (spannableStringBuilder == null) {
                    spannableStringBuilder = new SpannableStringBuilder();
                }
                if (i < 2) {
                    TLRPC.User user = peerId > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId)) : null;
                    TLRPC.Chat chat = peerId <= 0 ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(peerId)) : null;
                    if (user != null || chat != null) {
                        if (i != 0) {
                            spannableStringBuilder.append((CharSequence) ", ");
                        }
                        if (user == null) {
                            int i3 = Build.VERSION.SDK_INT;
                            str = chat.title;
                            if (i3 >= 21) {
                                typefaceSpan = new TypefaceSpan(AndroidUtilities.bold());
                                spannableStringBuilder.append(str, typefaceSpan, 0);
                            }
                            spannableStringBuilder.append((CharSequence) str);
                        } else if (Build.VERSION.SDK_INT >= 21) {
                            str = UserObject.getFirstName(user);
                            typefaceSpan = new TypefaceSpan(AndroidUtilities.bold());
                            spannableStringBuilder.append(str, typefaceSpan, 0);
                        } else {
                            str = UserObject.getFirstName(user);
                            spannableStringBuilder.append((CharSequence) str);
                        }
                    }
                }
                i++;
                if (i == 2) {
                    break;
                }
            }
        }
        if (i > 0) {
            String pluralString = LocaleController.getPluralString("MembersAreSpeakingToast", i);
            int indexOf = pluralString.indexOf("un1");
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(pluralString);
            spannableStringBuilder2.replace(indexOf, indexOf + 3, (CharSequence) spannableStringBuilder);
            this.actionBar.getAdditionalSubtitleTextView().setText(spannableStringBuilder2);
            z = true;
        } else {
            z = false;
        }
        this.actionBar.getSubtitleTextView().setText(LocaleController.formatPluralString(isRtmpStream() ? "ViewersWatching" : "Participants", this.call.call.participants_count + (this.listAdapter.addSelfToCounter() ? 1 : 0), new Object[0]));
        if (isRtmpStream() && (watchersView = this.watchersView) != null) {
            watchersView.setWatchersCount(this.call.call.participants_count);
        }
        if (z != this.drawSpeakingSubtitle) {
            this.drawSpeakingSubtitle = z;
            this.actionBar.invalidate();
            this.actionBar.getSubtitleTextView().setPivotX(0.0f);
            this.actionBar.getSubtitleTextView().setPivotY(this.actionBar.getMeasuredHeight() >> 1);
            this.actionBar.getSubtitleTextView().animate().scaleX(this.drawSpeakingSubtitle ? 0.98f : 1.0f).scaleY(this.drawSpeakingSubtitle ? 0.9f : 1.0f).alpha(this.drawSpeakingSubtitle ? 0.0f : 1.0f).setDuration(150L);
            AndroidUtilities.updateViewVisibilityAnimated(this.actionBar.getAdditionalSubtitleTextView(), this.drawSpeakingSubtitle);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00fa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateTitle(boolean z) {
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher;
        int i;
        String string;
        AudioPlayerAlert.ClippingTextViewSwitcher clippingTextViewSwitcher2;
        int i2;
        ChatObject.Call call = this.call;
        if (call == null) {
            if (ChatObject.isChannelOrGiga(this.currentChat)) {
                clippingTextViewSwitcher2 = this.titleTextView;
                i2 = R.string.VoipChannelScheduleVoiceChat;
            } else {
                clippingTextViewSwitcher2 = this.titleTextView;
                i2 = R.string.VoipGroupScheduleVoiceChat;
            }
            clippingTextViewSwitcher2.setText(LocaleController.getString(i2), z);
            return;
        }
        if (!TextUtils.isEmpty(call.call.title)) {
            if (!this.call.call.title.equals(this.actionBar.getTitle())) {
                ActionBar actionBar = this.actionBar;
                if (z) {
                    actionBar.setTitleAnimated(this.call.call.title, true, 180L);
                    this.actionBar.getTitleTextView().setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda45
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            GroupCallActivity.this.lambda$updateTitle$37(view);
                        }
                    });
                } else {
                    actionBar.setTitle(this.call.call.title);
                }
                clippingTextViewSwitcher = this.titleTextView;
                string = this.call.call.title;
                clippingTextViewSwitcher.setText(string, z);
            }
            SimpleTextView titleTextView = this.actionBar.getTitleTextView();
            if (this.call.recording) {
                if (titleTextView.getRightDrawable() != null) {
                    titleTextView.setRightDrawable((Drawable) null);
                    this.titleTextView.getTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                    this.titleTextView.getNextTextView().setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                    return;
                }
                return;
            }
            if (titleTextView.getRightDrawable() == null) {
                titleTextView.setRightDrawable(new SmallRecordCallDrawable(titleTextView));
                TextView textView = this.titleTextView.getTextView();
                textView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, new SmallRecordCallDrawable(textView), (Drawable) null);
                TextView nextTextView = this.titleTextView.getNextTextView();
                nextTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, new SmallRecordCallDrawable(nextTextView), (Drawable) null);
                return;
            }
            return;
        }
        if (!this.currentChat.title.equals(this.actionBar.getTitle())) {
            ActionBar actionBar2 = this.actionBar;
            if (z) {
                actionBar2.setTitleAnimated(this.currentChat.title, true, 180L);
                this.actionBar.getTitleTextView().setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda46
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        GroupCallActivity.this.lambda$updateTitle$38(view);
                    }
                });
            } else {
                actionBar2.setTitle(this.currentChat.title);
            }
            if (!ChatObject.isChannelOrGiga(this.currentChat)) {
                clippingTextViewSwitcher = this.titleTextView;
                i = R.string.VoipGroupVoiceChat;
            } else if (isRtmpStream()) {
                clippingTextViewSwitcher = this.titleTextView;
                string = this.currentChat.title;
                clippingTextViewSwitcher.setText(string, z);
            } else {
                clippingTextViewSwitcher = this.titleTextView;
                i = R.string.VoipChannelVoiceChat;
            }
            string = LocaleController.getString(i);
            clippingTextViewSwitcher.setText(string, z);
        }
        SimpleTextView titleTextView2 = this.actionBar.getTitleTextView();
        if (this.call.recording) {
        }
    }

    private void updateVideoParticipantList() {
        this.visibleVideoParticipants.clear();
        if (!isTabletMode) {
            this.visibleVideoParticipants.addAll(this.call.visibleVideoParticipants);
            return;
        }
        if (this.renderersContainer.inFullscreenMode) {
            this.visibleVideoParticipants.addAll(this.call.visibleVideoParticipants);
            ChatObject.VideoParticipant videoParticipant = this.renderersContainer.fullscreenParticipant;
            if (videoParticipant != null) {
                this.visibleVideoParticipants.remove(videoParticipant);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithTouchOutside() {
        return !this.renderersContainer.inFullscreenMode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant;
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2;
        int i3;
        String str;
        ChatObject.VideoParticipant videoParticipant;
        TLRPC.Chat chat;
        int i4;
        int i5;
        int i6 = 0;
        if (i == NotificationCenter.groupCallUpdated) {
            Long l = (Long) objArr[1];
            ChatObject.Call call = this.call;
            if (call == null || call.call.id != l.longValue()) {
                return;
            }
            ChatObject.Call call2 = this.call;
            if (!(call2.call instanceof TLRPC.TL_groupCallDiscarded)) {
                if (this.creatingServiceTime == 0 && (((i5 = this.muteButtonState) == 7 || i5 == 5 || i5 == 6) && !call2.isScheduled())) {
                    try {
                        Intent intent = new Intent(this.parentActivity, (Class<?>) VoIPService.class);
                        intent.putExtra("chat_id", this.currentChat.id);
                        intent.putExtra("createGroupCall", false);
                        intent.putExtra("hasFewPeers", this.scheduleHasFewPeers);
                        intent.putExtra("peerChannelId", this.schedulePeer.channel_id);
                        intent.putExtra("peerChatId", this.schedulePeer.chat_id);
                        intent.putExtra("peerUserId", this.schedulePeer.user_id);
                        intent.putExtra("hash", this.scheduledHash);
                        intent.putExtra("peerAccessHash", this.schedulePeer.access_hash);
                        intent.putExtra("is_outgoing", true);
                        intent.putExtra("start_incall_activity", false);
                        intent.putExtra("account", this.accountInstance.getCurrentAccount());
                        intent.putExtra("scheduleDate", this.scheduleStartAt);
                        this.parentActivity.startService(intent);
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                    this.creatingServiceTime = SystemClock.elapsedRealtime();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda31
                        @Override // java.lang.Runnable
                        public final void run() {
                            GroupCallActivity.this.lambda$didReceivedNotification$2();
                        }
                    }, 3000L);
                }
                if (!this.callInitied && VoIPService.getSharedInstance() != null) {
                    this.call.addSelfDummyParticipant(false);
                    initCreatedGroupCall();
                    VoIPService.getSharedInstance().playConnectedSound();
                }
                updateItems();
                int childCount = this.listView.getChildCount();
                for (int i7 = 0; i7 < childCount; i7++) {
                    View childAt = this.listView.getChildAt(i7);
                    if (childAt instanceof GroupCallUserCell) {
                        ((GroupCallUserCell) childAt).applyParticipantChanges(true);
                    }
                }
                if (this.scrimView != null) {
                    this.delayedGroupCallUpdated = true;
                } else {
                    applyCallParticipantUpdates(true);
                }
                updateSubtitle();
                boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                boolean z = this.muteButtonState == 4;
                updateState(true, booleanValue);
                updateTitle(true);
                if (z && ((i4 = this.muteButtonState) == 1 || i4 == 0)) {
                    getUndoView().showWithAction(0L, 38, (Runnable) null);
                    if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().playAllowTalkSound();
                    }
                }
                if (objArr.length >= 4) {
                    Long l2 = (Long) objArr[3];
                    long longValue = l2.longValue();
                    if (longValue == 0 || isRtmpStream()) {
                        return;
                    }
                    try {
                        ArrayList<TLRPC.Dialog> allDialogs = this.accountInstance.getMessagesController().getAllDialogs();
                        if (allDialogs != null) {
                            Iterator<TLRPC.Dialog> it = allDialogs.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    if (it.next().id == longValue) {
                                        i6 = 1;
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    } catch (Exception unused) {
                    }
                    if (DialogObject.isUserDialog(longValue)) {
                        TLRPC.User user = this.accountInstance.getMessagesController().getUser(l2);
                        if (user == 0) {
                            return;
                        }
                        chat = user;
                        if (this.call.call.participants_count >= 250) {
                            boolean isContact = UserObject.isContact(user);
                            chat = user;
                            if (!isContact) {
                                boolean z2 = user.verified;
                                chat = user;
                                chat = user;
                                if (!z2 && i6 == 0) {
                                    return;
                                }
                            }
                        }
                    } else {
                        TLRPC.Chat chat2 = this.accountInstance.getMessagesController().getChat(Long.valueOf(-longValue));
                        if (chat2 == null) {
                            return;
                        }
                        chat = chat2;
                        if (this.call.call.participants_count >= 250) {
                            boolean isNotInChat = ChatObject.isNotInChat(chat2);
                            chat = chat2;
                            if (isNotInChat) {
                                boolean z3 = chat2.verified;
                                chat = chat2;
                                chat = chat2;
                                if (!z3 && i6 == 0) {
                                    return;
                                }
                            }
                        }
                    }
                    getUndoView().showWithAction(0L, 44, chat, this.currentChat, (Runnable) null, (Runnable) null);
                    return;
                }
                return;
            }
        } else {
            if (i == NotificationCenter.groupCallSpeakingUsersUpdated) {
                GroupCallRenderersContainer groupCallRenderersContainer = this.renderersContainer;
                if (groupCallRenderersContainer.inFullscreenMode && this.call != null) {
                    boolean autoPinEnabled = groupCallRenderersContainer.autoPinEnabled();
                    ChatObject.Call call3 = this.call;
                    if (call3 != null) {
                        GroupCallRenderersContainer groupCallRenderersContainer2 = this.renderersContainer;
                        if (groupCallRenderersContainer2.inFullscreenMode && (videoParticipant = groupCallRenderersContainer2.fullscreenParticipant) != null && call3.participants.get(MessageObject.getPeerId(videoParticipant.participant.peer)) == null) {
                            autoPinEnabled = true;
                        }
                    }
                    if (autoPinEnabled) {
                        ChatObject.VideoParticipant videoParticipant2 = null;
                        for (int i8 = 0; i8 < this.visibleVideoParticipants.size(); i8++) {
                            ChatObject.VideoParticipant videoParticipant3 = (ChatObject.VideoParticipant) this.visibleVideoParticipants.get(i8);
                            if (this.call.currentSpeakingPeers.get(MessageObject.getPeerId(videoParticipant3.participant.peer), null) != null) {
                                TLRPC.TL_groupCallParticipant tL_groupCallParticipant3 = videoParticipant3.participant;
                                if (!tL_groupCallParticipant3.muted_by_you && this.renderersContainer.fullscreenPeerId != MessageObject.getPeerId(tL_groupCallParticipant3.peer)) {
                                    videoParticipant2 = videoParticipant3;
                                }
                            }
                        }
                        if (videoParticipant2 != null) {
                            fullscreenFor(videoParticipant2);
                        }
                    }
                }
                this.renderersContainer.setVisibleParticipant(true);
                updateSubtitle();
                return;
            }
            if (i == NotificationCenter.webRtcMicAmplitudeEvent) {
                setMicAmplitude(((Float) objArr[0]).floatValue());
                return;
            }
            if (i == NotificationCenter.needShowAlert) {
                if (((Integer) objArr[0]).intValue() != 6) {
                    return;
                }
                String str2 = (String) objArr[1];
                try {
                    if ("GROUPCALL_PARTICIPANTS_TOO_MUCH".equals(str2)) {
                        i3 = ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelTooMuch : R.string.VoipGroupTooMuch;
                    } else {
                        if (!"ANONYMOUS_CALLS_DISABLED".equals(str2) && !"GROUPCALL_ANONYMOUS_FORBIDDEN".equals(str2)) {
                            str = LocaleController.getString(R.string.ErrorOccurred) + "\n" + str2;
                            AlertDialog.Builder createSimpleAlert = AlertsCreator.createSimpleAlert(getContext(), LocaleController.getString(R.string.VoipGroupVoiceChat), str);
                            createSimpleAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda32
                                @Override // android.content.DialogInterface.OnDismissListener
                                public final void onDismiss(DialogInterface dialogInterface) {
                                    GroupCallActivity.this.lambda$didReceivedNotification$3(dialogInterface);
                                }
                            });
                            createSimpleAlert.show();
                            return;
                        }
                        i3 = ChatObject.isChannelOrGiga(this.currentChat) ? R.string.VoipChannelJoinAnonymousAdmin : R.string.VoipGroupJoinAnonymousAdmin;
                    }
                    createSimpleAlert.show();
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
                str = LocaleController.getString(i3);
                AlertDialog.Builder createSimpleAlert2 = AlertsCreator.createSimpleAlert(getContext(), LocaleController.getString(R.string.VoipGroupVoiceChat), str);
                createSimpleAlert2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda32
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        GroupCallActivity.this.lambda$didReceivedNotification$3(dialogInterface);
                    }
                });
            } else {
                if (i != NotificationCenter.didEndCall) {
                    if (i == NotificationCenter.chatInfoDidLoad) {
                        TLRPC.ChatFull chatFull = (TLRPC.ChatFull) objArr[0];
                        if (chatFull.id == this.currentChat.id) {
                            updateItems();
                            updateState(isShowing(), false);
                        }
                        long peerId = MessageObject.getPeerId(this.selfPeer);
                        ChatObject.Call call4 = this.call;
                        if (call4 == null || chatFull.id != (-peerId) || (tL_groupCallParticipant2 = (TLRPC.TL_groupCallParticipant) call4.participants.get(peerId)) == null) {
                            return;
                        }
                        tL_groupCallParticipant2.about = chatFull.about;
                        applyCallParticipantUpdates(true);
                        AndroidUtilities.updateVisibleRows(this.listView);
                        if (this.currentOptionsLayout != null) {
                            while (i6 < this.currentOptionsLayout.getChildCount()) {
                                View childAt2 = this.currentOptionsLayout.getChildAt(i6);
                                if ((childAt2 instanceof ActionBarMenuSubItem) && childAt2.getTag() != null && ((Integer) childAt2.getTag()).intValue() == 10) {
                                    ((ActionBarMenuSubItem) childAt2).setTextAndIcon(LocaleController.getString(TextUtils.isEmpty(tL_groupCallParticipant2.about) ? R.string.VoipAddDescription : R.string.VoipEditDescription), TextUtils.isEmpty(tL_groupCallParticipant2.about) ? R.drawable.msg_addbio : R.drawable.msg_info);
                                }
                                i6++;
                            }
                            return;
                        }
                        return;
                    }
                    if (i == NotificationCenter.didLoadChatAdmins) {
                        if (((Long) objArr[0]).longValue() == this.currentChat.id) {
                            updateItems();
                            updateState(isShowing(), false);
                            return;
                        }
                        return;
                    }
                    if (i == NotificationCenter.applyGroupCallVisibleParticipants) {
                        int childCount2 = this.listView.getChildCount();
                        long longValue2 = ((Long) objArr[0]).longValue();
                        while (i6 < childCount2) {
                            RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(this.listView.getChildAt(i6));
                            if (findContainingViewHolder != null) {
                                View view = findContainingViewHolder.itemView;
                                if (view instanceof GroupCallUserCell) {
                                    GroupCallUserCell groupCallUserCell = (GroupCallUserCell) view;
                                    if (groupCallUserCell.getParticipant() != null) {
                                        groupCallUserCell.getParticipant().lastVisibleDate = longValue2;
                                    }
                                }
                            }
                            i6++;
                        }
                        return;
                    }
                    if (i != NotificationCenter.userInfoDidLoad) {
                        if (i == NotificationCenter.mainUserInfoChanged) {
                            applyCallParticipantUpdates(true);
                        } else {
                            if (i != NotificationCenter.updateInterfaces) {
                                if (i == NotificationCenter.groupCallScreencastStateChanged) {
                                    PrivateVideoPreviewDialog privateVideoPreviewDialog = this.previewDialog;
                                    if (privateVideoPreviewDialog != null) {
                                        privateVideoPreviewDialog.dismiss(true, true);
                                    }
                                    updateItems();
                                    return;
                                }
                                return;
                            }
                            int intValue = ((Integer) objArr[0]).intValue();
                            if ((MessagesController.UPDATE_MASK_CHAT_NAME & intValue) != 0) {
                                applyCallParticipantUpdates(true);
                            }
                            if ((MessagesController.UPDATE_MASK_CHAT_NAME & intValue) == 0 && (intValue & MessagesController.UPDATE_MASK_EMOJI_STATUS) == 0) {
                                return;
                            }
                        }
                        AndroidUtilities.updateVisibleRows(this.listView);
                        return;
                    }
                    Long l3 = (Long) objArr[0];
                    long peerId2 = MessageObject.getPeerId(this.selfPeer);
                    if (this.call == null || peerId2 != l3.longValue() || (tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.call.participants.get(peerId2)) == null) {
                        return;
                    }
                    tL_groupCallParticipant.about = ((TLRPC.UserFull) objArr[1]).about;
                    applyCallParticipantUpdates(true);
                    AndroidUtilities.updateVisibleRows(this.listView);
                    if (this.currentOptionsLayout != null) {
                        while (i6 < this.currentOptionsLayout.getChildCount()) {
                            View childAt3 = this.currentOptionsLayout.getChildAt(i6);
                            if ((childAt3 instanceof ActionBarMenuSubItem) && childAt3.getTag() != null && ((Integer) childAt3.getTag()).intValue() == 10) {
                                ((ActionBarMenuSubItem) childAt3).setTextAndIcon(LocaleController.getString(TextUtils.isEmpty(tL_groupCallParticipant.about) ? R.string.VoipAddBio : R.string.VoipEditBio), TextUtils.isEmpty(tL_groupCallParticipant.about) ? R.drawable.msg_addbio : R.drawable.msg_info);
                            }
                            i6++;
                        }
                        return;
                    }
                    return;
                }
                if (VoIPService.getSharedInstance() != null) {
                    return;
                }
            }
        }
        dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        this.parentActivity.removeOnUserLeaveHintListener(this.onUserLeaveHintListener);
        this.parentActivity.setRequestedOrientation(-1);
        groupCallUiVisible = false;
        GroupVoipInviteAlert groupVoipInviteAlert = this.groupVoipInviteAlert;
        if (groupVoipInviteAlert != null) {
            groupVoipInviteAlert.dismiss();
        }
        this.delayedGroupCallUpdated = true;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallVisibilityChanged, new Object[0]);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.needShowAlert);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.groupCallUpdated);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.didLoadChatAdmins);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.applyGroupCallVisibleParticipants);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.userInfoDidLoad);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.mainUserInfoChanged);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.groupCallScreencastStateChanged);
        this.accountInstance.getNotificationCenter().removeObserver(this, NotificationCenter.groupCallSpeakingUsersUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndCall);
        super.dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        if (this.renderersContainer != null) {
            if (this.requestFullscreenListener != null) {
                this.listView.getViewTreeObserver().removeOnPreDrawListener(this.requestFullscreenListener);
                this.requestFullscreenListener = null;
            }
            this.attachedRenderersTmp.clear();
            this.attachedRenderersTmp.addAll(this.attachedRenderers);
            for (int i = 0; i < this.attachedRenderersTmp.size(); i++) {
                ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).saveThumb();
                this.renderersContainer.removeView((View) this.attachedRenderersTmp.get(i));
                ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).release();
                ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).forceDetach(true);
            }
            this.attachedRenderers.clear();
            if (this.renderersContainer.getParent() != null) {
                this.attachedRenderers.clear();
                this.containerView.removeView(this.renderersContainer);
            }
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 2048);
        super.dismissInternal();
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener(this);
            VoIPService.getSharedInstance().setSinks(null, null);
        }
        if (groupCallInstance == this) {
            groupCallInstance = null;
        }
        groupCallUiVisible = false;
        VoIPService.audioLevelsCallback = null;
        GroupCallPip.updateVisibility(getContext());
        ChatObject.Call call = this.call;
        if (call != null) {
            call.clearVideFramesInfo();
        }
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().clearRemoteSinks();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int streamMinVolume;
        if (this.parentActivity == null) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (keyEvent.getAction() == 0 && ((keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) && VoIPService.getSharedInstance() != null && Build.VERSION.SDK_INT >= 32)) {
            boolean isSpeakerMuted = WebRtcAudioTrack.isSpeakerMuted();
            AudioManager audioManager = (AudioManager) this.parentActivity.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            boolean z = false;
            streamMinVolume = audioManager.getStreamMinVolume(0);
            if (audioManager.getStreamVolume(0) == streamMinVolume && keyEvent.getKeyCode() == 25) {
                z = true;
            }
            WebRtcAudioTrack.setSpeakerMute(z);
            if (isSpeakerMuted != WebRtcAudioTrack.isSpeakerMuted()) {
                getUndoView().showWithAction(0L, z ? 42 : 43, (Runnable) null);
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public void enableCamera() {
        this.cameraButton.callOnClick();
    }

    public void fullscreenFor(final ChatObject.VideoParticipant videoParticipant) {
        ViewTreeObserver viewTreeObserver;
        ViewTreeObserver.OnPreDrawListener onPreDrawListener;
        ChatObject.VideoParticipant videoParticipant2;
        if (videoParticipant == null) {
            this.parentActivity.setRequestedOrientation(-1);
        }
        if (VoIPService.getSharedInstance() == null || this.renderersContainer.isAnimating()) {
            return;
        }
        if (!isTabletMode) {
            if (this.requestFullscreenListener != null) {
                this.listView.getViewTreeObserver().removeOnPreDrawListener(this.requestFullscreenListener);
                this.requestFullscreenListener = null;
            }
            if (videoParticipant == null) {
                if (this.listView.getVisibility() != 0) {
                    this.listView.setVisibility(0);
                    applyCallParticipantUpdates(false);
                    this.delayedGroupCallUpdated = true;
                    viewTreeObserver = this.listView.getViewTreeObserver();
                    onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.GroupCallActivity.45
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public boolean onPreDraw() {
                            GroupCallActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                            GroupCallActivity.this.renderersContainer.requestFullscreen(null);
                            AndroidUtilities.updateVisibleRows(GroupCallActivity.this.fullscreenUsersListView);
                            return false;
                        }
                    };
                } else {
                    viewTreeObserver = this.listView.getViewTreeObserver();
                    onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.GroupCallActivity.46
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public boolean onPreDraw() {
                            GroupCallActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                            GroupCallActivity.this.renderersContainer.requestFullscreen(null);
                            AndroidUtilities.updateVisibleRows(GroupCallActivity.this.fullscreenUsersListView);
                            return false;
                        }
                    };
                }
                this.requestFullscreenListener = onPreDrawListener;
                viewTreeObserver.addOnPreDrawListener(onPreDrawListener);
                return;
            }
            if (this.fullscreenUsersListView.getVisibility() == 0) {
                this.renderersContainer.requestFullscreen(videoParticipant);
                AndroidUtilities.updateVisibleRows(this.fullscreenUsersListView);
                return;
            }
            this.fullscreenUsersListView.setVisibility(0);
            this.fullscreenAdapter.update(false, this.fullscreenUsersListView);
            this.delayedGroupCallUpdated = true;
            if (!this.renderersContainer.inFullscreenMode) {
                this.fullscreenAdapter.scrollTo(videoParticipant, this.fullscreenUsersListView);
            }
            ViewTreeObserver viewTreeObserver2 = this.listView.getViewTreeObserver();
            ViewTreeObserver.OnPreDrawListener onPreDrawListener2 = new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.GroupCallActivity.44
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    GroupCallActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                    GroupCallActivity groupCallActivity = GroupCallActivity.this;
                    groupCallActivity.requestFullscreenListener = null;
                    groupCallActivity.renderersContainer.requestFullscreen(videoParticipant);
                    AndroidUtilities.updateVisibleRows(GroupCallActivity.this.fullscreenUsersListView);
                    return false;
                }
            };
            this.requestFullscreenListener = onPreDrawListener2;
            viewTreeObserver2.addOnPreDrawListener(onPreDrawListener2);
            return;
        }
        if (this.requestFullscreenListener != null) {
            this.listView.getViewTreeObserver().removeOnPreDrawListener(this.requestFullscreenListener);
            this.requestFullscreenListener = null;
        }
        final ArrayList arrayList = new ArrayList();
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        int i = 0;
        if (videoParticipant == null) {
            while (i < this.attachedRenderersTmp.size()) {
                final GroupCallMiniTextureView groupCallMiniTextureView = (GroupCallMiniTextureView) this.attachedRenderersTmp.get(i);
                GroupCallGridCell groupCallGridCell = groupCallMiniTextureView.primaryView;
                if (groupCallGridCell != null) {
                    groupCallGridCell.setRenderer(null);
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell = groupCallMiniTextureView.secondaryView;
                    if (groupCallUserCell != null) {
                        groupCallUserCell.setRenderer(null);
                    }
                    GroupCallGridCell groupCallGridCell2 = groupCallMiniTextureView.tabletGridView;
                    if (groupCallGridCell2 != null) {
                        groupCallGridCell2.setRenderer(null);
                    }
                    arrayList.add(groupCallMiniTextureView.participant);
                    groupCallMiniTextureView.forceDetach(false);
                    groupCallMiniTextureView.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.41
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (groupCallMiniTextureView.getParent() != null) {
                                ((BottomSheet) GroupCallActivity.this).containerView.removeView(groupCallMiniTextureView);
                            }
                        }
                    });
                }
                i++;
            }
            this.listViewVideoVisibility = false;
            this.tabletGridAdapter.setVisibility(this.tabletVideoGridView, true, true);
        } else {
            while (i < this.attachedRenderersTmp.size()) {
                final GroupCallMiniTextureView groupCallMiniTextureView2 = (GroupCallMiniTextureView) this.attachedRenderersTmp.get(i);
                if (groupCallMiniTextureView2.tabletGridView != null && ((videoParticipant2 = groupCallMiniTextureView2.participant) == null || !videoParticipant2.equals(videoParticipant))) {
                    arrayList.add(groupCallMiniTextureView2.participant);
                    groupCallMiniTextureView2.forceDetach(false);
                    GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell2 = groupCallMiniTextureView2.secondaryView;
                    if (groupCallUserCell2 != null) {
                        groupCallUserCell2.setRenderer(null);
                    }
                    GroupCallGridCell groupCallGridCell3 = groupCallMiniTextureView2.primaryView;
                    if (groupCallGridCell3 != null) {
                        groupCallGridCell3.setRenderer(null);
                    }
                    groupCallMiniTextureView2.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCallActivity.42
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (groupCallMiniTextureView2.getParent() != null) {
                                ((BottomSheet) GroupCallActivity.this).containerView.removeView(groupCallMiniTextureView2);
                            }
                        }
                    });
                }
                i++;
            }
            this.listViewVideoVisibility = true;
            this.tabletGridAdapter.setVisibility(this.tabletVideoGridView, false, false);
            if (!arrayList.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        GroupCallActivity.this.lambda$fullscreenFor$36(arrayList);
                    }
                });
            }
        }
        final boolean z = !this.renderersContainer.inFullscreenMode;
        ViewTreeObserver viewTreeObserver3 = this.listView.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener onPreDrawListener3 = new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.GroupCallActivity.43
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                GroupCallActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                GroupCallActivity groupCallActivity = GroupCallActivity.this;
                groupCallActivity.requestFullscreenListener = null;
                groupCallActivity.renderersContainer.requestFullscreen(videoParticipant);
                if (GroupCallActivity.this.delayedGroupCallUpdated) {
                    GroupCallActivity.this.delayedGroupCallUpdated = false;
                    GroupCallActivity.this.applyCallParticipantUpdates(true);
                    if (z && videoParticipant != null) {
                        GroupCallActivity.this.listView.scrollToPosition(0);
                    }
                    GroupCallActivity.this.delayedGroupCallUpdated = true;
                } else {
                    GroupCallActivity.this.applyCallParticipantUpdates(true);
                }
                return false;
            }
        };
        this.requestFullscreenListener = onPreDrawListener3;
        viewTreeObserver3.addOnPreDrawListener(onPreDrawListener3);
    }

    public LinearLayout getMenuItemsContainer() {
        return this.menuItemsContainer;
    }

    public LaunchActivity getParentActivity() {
        return this.parentActivity;
    }

    public View getScrimView() {
        return this.scrimView;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList getThemeDescriptions() {
        return new ArrayList();
    }

    public UndoView getUndoView() {
        if (!isTabletMode) {
            GroupCallRenderersContainer groupCallRenderersContainer = this.renderersContainer;
            if (groupCallRenderersContainer.inFullscreenMode) {
                return groupCallRenderersContainer.getUndoView();
            }
        }
        if (this.undoView[0].getVisibility() == 0) {
            UndoView[] undoViewArr = this.undoView;
            UndoView undoView = undoViewArr[0];
            undoViewArr[0] = undoViewArr[1];
            undoViewArr[1] = undoView;
            undoView.hide(true, 2);
            this.containerView.removeView(this.undoView[0]);
            this.containerView.addView(this.undoView[0]);
        }
        return this.undoView[0];
    }

    public void invalidateActionBarAlpha() {
        ActionBar actionBar = this.actionBar;
        actionBar.setAlpha((actionBar.getTag() != null ? 1.0f : 0.0f) * (1.0f - this.renderersContainer.progressToFullscreenMode));
    }

    public void invalidateScrollOffsetY() {
        setScrollOffsetY(this.scrollOffsetY);
    }

    public boolean isRtmpLandscapeMode() {
        if (!isRtmpStream() || this.call.visibleVideoParticipants.isEmpty()) {
            return false;
        }
        return this.call.visibleVideoParticipants.get(0).aspectRatio == 0.0f || this.call.visibleVideoParticipants.get(0).aspectRatio >= 1.0f;
    }

    public boolean isRtmpStream() {
        ChatObject.Call call = this.call;
        return call != null && call.call.rtmp_stream;
    }

    protected void makeFocusable(final BottomSheet bottomSheet, final AlertDialog alertDialog, final EditTextBoldCursor editTextBoldCursor, final boolean z) {
        if (this.enterEventSent) {
            return;
        }
        BaseFragment baseFragment = (BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1);
        if (baseFragment instanceof ChatActivity) {
            boolean needEnterText = ((ChatActivity) baseFragment).needEnterText();
            this.enterEventSent = true;
            this.anyEnterEventSent = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.lambda$makeFocusable$7(BottomSheet.this, editTextBoldCursor, z, alertDialog);
                }
            }, needEnterText ? 200L : 0L);
            return;
        }
        this.enterEventSent = true;
        this.anyEnterEventSent = true;
        if (bottomSheet != null) {
            bottomSheet.setFocusable(true);
        } else if (alertDialog != null) {
            alertDialog.setFocusable(true);
        }
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCallActivity$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallActivity.lambda$makeFocusable$8(EditTextBoldCursor.this);
                }
            }, 100L);
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
        updateSpeakerPhoneIcon(true);
        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().isMicMute()) {
            setMicAmplitude(0.0f);
        }
        if (this.listView.getVisibility() == 0) {
            AndroidUtilities.updateVisibleRows(this.listView);
        }
        if (this.fullscreenUsersListView.getVisibility() == 0) {
            AndroidUtilities.updateVisibleRows(this.fullscreenUsersListView);
        }
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        for (int i = 0; i < this.attachedRenderersTmp.size(); i++) {
            ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).updateAttachState(true);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        PrivateVideoPreviewDialog privateVideoPreviewDialog = this.previewDialog;
        if (privateVideoPreviewDialog != null) {
            privateVideoPreviewDialog.dismiss(false, false);
            return;
        }
        if (this.avatarsPreviewShowed) {
            dismissAvatarPreview(true);
        } else if (this.renderersContainer.inFullscreenMode) {
            fullscreenFor(null);
        } else {
            super.onBackPressed();
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.-CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onCameraSwitch(boolean z) {
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        for (int i = 0; i < this.attachedRenderersTmp.size(); i++) {
            ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).updateAttachState(true);
        }
        PrivateVideoPreviewDialog privateVideoPreviewDialog = this.previewDialog;
        if (privateVideoPreviewDialog != null) {
            privateVideoPreviewDialog.update();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.parentActivity.addOnUserLeaveHintListener(this.onUserLeaveHintListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomOpenAnimation() {
        groupCallUiVisible = true;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallVisibilityChanged, new Object[0]);
        GroupCallPip.updateVisibility(getContext());
        return super.onCustomOpenAnimation();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onMediaStateUpdated(int i, int i2) {
        VoIPService.StateListener.-CC.$default$onMediaStateUpdated(this, i, i2);
    }

    public void onPause() {
        paused = true;
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        for (int i = 0; i < this.attachedRenderersTmp.size(); i++) {
            ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).updateAttachState(false);
        }
    }

    public void onResume() {
        paused = false;
        this.listAdapter.notifyDataSetChanged();
        if (this.fullscreenUsersListView.getVisibility() == 0) {
            this.fullscreenAdapter.update(false, this.fullscreenUsersListView);
        }
        if (isTabletMode) {
            this.tabletGridAdapter.update(false, this.tabletVideoGridView);
        }
        this.attachedRenderersTmp.clear();
        this.attachedRenderersTmp.addAll(this.attachedRenderers);
        for (int i = 0; i < this.attachedRenderersTmp.size(); i++) {
            ((GroupCallMiniTextureView) this.attachedRenderersTmp.get(i)).updateAttachState(true);
        }
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
    public void onStateChanged(int i) {
        this.currentCallState = i;
        updateState(isShowing(), false);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onVideoAvailableChange(boolean z) {
        VoIPService.StateListener.-CC.$default$onVideoAvailableChange(this, z);
    }

    public void setOldRows(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.oldAddMemberRow = i;
        this.oldUsersStartRow = i2;
        this.oldUsersEndRow = i3;
        this.oldInvitedStartRow = i4;
        this.oldInvitedEndRow = i5;
        this.oldUsersVideoStartRow = i6;
        this.oldUsersVideoEndRow = i7;
        this.oldVideoDividerRow = i8;
        this.oldVideoNotAvailableRow = i9;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 2048);
        super.show();
        if (RTMPStreamPipOverlay.isVisible()) {
            RTMPStreamPipOverlay.dismiss();
        }
    }
}
