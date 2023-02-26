package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StatFs;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.AssistActionBuilder;
import j$.util.function.Consumer;
import j$.wrappers.$r8$wrapper$java$util$function$Consumer$-WRP;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AutoDeleteMediaTask;
import org.telegram.messenger.BackupAgent;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsLoadingObserver;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FingerprintController;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.PushListenerController;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPPendingCall;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$LangPackString;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getTheme;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_auth_acceptLoginToken;
import org.telegram.tgnet.TLRPC$TL_auth_sentCode;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_getChannels;
import org.telegram.tgnet.TLRPC$TL_channels_getForumTopicsByID;
import org.telegram.tgnet.TLRPC$TL_channels_getMessages;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatInvitePeek;
import org.telegram.tgnet.TLRPC$TL_codeSettings;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$TL_contacts_importContactToken;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvePhone;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$TL_help_appUpdate;
import org.telegram.tgnet.TLRPC$TL_help_deepLinkInfo;
import org.telegram.tgnet.TLRPC$TL_help_getAppUpdate;
import org.telegram.tgnet.TLRPC$TL_help_getDeepLinkInfo;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.tgnet.TLRPC$TL_inputChannel;
import org.telegram.tgnet.TLRPC$TL_inputGameShortName;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_inputMediaGame;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_inputThemeSlug;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_langPackLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_checkChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImport;
import org.telegram.tgnet.TLRPC$TL_messages_discussionMessage;
import org.telegram.tgnet.TLRPC$TL_messages_forumTopics;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_messages_getDiscussionMessage;
import org.telegram.tgnet.TLRPC$TL_messages_historyImportParsed;
import org.telegram.tgnet.TLRPC$TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_toggleBotInAttachMenu;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$ThemeSettings;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.tgnet.TLRPC$account_Password;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AppIconBulletinLayout;
import org.telegram.ui.Components.AttachBotIntroTopView;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.BlockingUpdateView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PhonebookShareAlert;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SharingLocationsAlert;
import org.telegram.ui.Components.SideMenultItemAnimator;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.UpdateAppAlertDialog;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.ContactsActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LauncherIconController;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.WallpapersListActivity;
import org.webrtc.MediaStreamTrack;
import org.webrtc.voiceengine.WebRtcAudioTrack;
/* loaded from: classes3.dex */
public class LaunchActivity extends BasePermissionsActivity implements INavigationLayout.INavigationLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    public static boolean isResumed;
    public static Runnable onResumeStaticCallback;
    private static LaunchActivity staticInstanceForAlerts;
    public static boolean systemBlurEnabled;
    private INavigationLayout actionBarLayout;
    private long alreadyShownFreeDiscSpaceAlertForced;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private boolean checkFreeDiscSpaceShown;
    private ArrayList<TLRPC$User> contactsToSend;
    private Uri contactsToSendUri;
    private int currentConnectionState;
    private String documentsMimeType;
    private ArrayList<String> documentsOriginalPathsArray;
    private ArrayList<String> documentsPathsArray;
    private ArrayList<Uri> documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    public DrawerLayoutContainer drawerLayoutContainer;
    private HashMap<String, String> englishLocaleStrings;
    private Uri exportingChatUri;
    View feedbackView;
    private boolean finished;
    private FireworksOverlay fireworksOverlay;
    private FrameLayout frameLayout;
    private ArrayList<Parcelable> importingStickers;
    private ArrayList<String> importingStickersEmoji;
    private String importingStickersSoftware;
    private SideMenultItemAnimator itemAnimator;
    private RelativeLayout launchLayout;
    private INavigationLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private TLRPC$TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private String loadingThemeFileName;
    private Theme.ThemeInfo loadingThemeInfo;
    private AlertDialog loadingThemeProgressDialog;
    private TLRPC$TL_wallPaper loadingThemeWallpaper;
    private String loadingThemeWallpaperName;
    private AlertDialog localeDialog;
    private Runnable lockRunnable;
    private boolean navigateToPremiumBot;
    private Runnable navigateToPremiumGiftCallback;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendMessagesHelper.SendingMediaInfo> photoPathsArray;
    private AlertDialog proxyErrorDialog;
    private INavigationLayout rightActionBarLayout;
    private View rippleAbove;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private FrameLayout sideMenuContainer;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private int[] tempLocation;
    private TermsOfServiceView termsOfServiceView;
    private ImageView themeSwitchImageView;
    private RLottieDrawable themeSwitchSunDrawable;
    private View themeSwitchSunView;
    private FrameLayout updateLayout;
    private RadialProgress2 updateLayoutIcon;
    private TextView updateSizeTextView;
    private SimpleTextView updateTextView;
    private String videoPath;
    private ActionMode visibleActionMode;
    private AlertDialog visibleDialog;
    private boolean wasMutedByAdminRaisedHand;
    public static final Pattern PREFIX_T_ME_PATTERN = Pattern.compile("^(?:http(?:s|)://|)([A-z0-9-]+?)\\.t\\.me");
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList<>();
    private List<PasscodeView> overlayPasscodeViews = new ArrayList();
    private boolean isNavigationBarColorFrozen = false;
    private List<Runnable> onUserLeaveHintListeners = new ArrayList();
    private SparseIntArray requestedPermissions = new SparseIntArray();
    private int requsetPermissionsPointer = 5934;
    private Consumer<Boolean> blurListener = new Consumer<Boolean>(this) { // from class: org.telegram.ui.LaunchActivity.1
        @Override // j$.util.function.Consumer
        public /* synthetic */ Consumer<Boolean> andThen(Consumer<? super Boolean> consumer) {
            return Objects.requireNonNull(consumer);
        }

        @Override // j$.util.function.Consumer
        public void accept(Boolean bool) {
            LaunchActivity.systemBlurEnabled = bool.booleanValue();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setupActionBarLayout$7(View view) {
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public /* synthetic */ boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, INavigationLayout iNavigationLayout) {
        return INavigationLayout.INavigationLayoutDelegate.-CC.$default$needPresentFragment(this, baseFragment, z, z2, iNavigationLayout);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public /* synthetic */ void onMeasureOverride(int[] iArr) {
        INavigationLayout.INavigationLayoutDelegate.-CC.$default$onMeasureOverride(this, iArr);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        boolean z;
        Intent intent;
        Uri data;
        if (BuildVars.DEBUG_VERSION) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy()).detectLeakedClosableObjects().build());
        }
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        if (!UserConfig.getInstance(i).isClientActivated() && (intent = getIntent()) != null && intent.getAction() != null) {
            if ("android.intent.action.SEND".equals(intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                super.onCreate(bundle);
                finish();
                return;
            } else if ("android.intent.action.VIEW".equals(intent.getAction()) && (data = intent.getData()) != null) {
                String lowerCase = data.toString().toLowerCase();
                if (!lowerCase.startsWith("tg:proxy") && !lowerCase.startsWith("tg://proxy") && !lowerCase.startsWith("tg:socks")) {
                    lowerCase.startsWith("tg://socks");
                }
            }
        }
        requestWindowFeature(1);
        setTheme(R.style.Theme_TMessages);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor("actionBarDefault") | (-16777216)));
            } catch (Throwable unused) {
            }
            try {
                getWindow().setNavigationBarColor(-16777216);
            } catch (Throwable unused2) {
            }
        }
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
            try {
                getWindow().setFlags(8192, 8192);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        super.onCreate(bundle);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
            AndroidUtilities.isInMultiwindow = isInMultiWindowMode();
        }
        Theme.createCommonChatResources();
        Theme.createDialogsResources(this);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
        }
        AndroidUtilities.fillStatusBarHeight(this);
        this.actionBarLayout = INavigationLayout.-CC.newLayout(this);
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.2
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LaunchActivity.this.drawRippleAbove(canvas, this);
            }
        };
        this.frameLayout = frameLayout;
        char c = 65535;
        setContentView(frameLayout, new ViewGroup.LayoutParams(-1, -1));
        if (i2 >= 21) {
            ImageView imageView = new ImageView(this);
            this.themeSwitchImageView = imageView;
            imageView.setVisibility(8);
        }
        3 r7 = new 3(this);
        this.drawerLayoutContainer = r7;
        r7.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
        this.frameLayout.addView(this.drawerLayoutContainer, LayoutHelper.createFrame(-1, -1.0f));
        if (i2 >= 21) {
            View view = new View(this) { // from class: org.telegram.ui.LaunchActivity.4
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    if (LaunchActivity.this.themeSwitchSunDrawable != null) {
                        LaunchActivity.this.themeSwitchSunDrawable.draw(canvas);
                        invalidate();
                    }
                }
            };
            this.themeSwitchSunView = view;
            this.frameLayout.addView(view, LayoutHelper.createFrame(48, 48.0f));
            this.themeSwitchSunView.setVisibility(8);
        }
        FrameLayout frameLayout2 = this.frameLayout;
        FireworksOverlay fireworksOverlay = new FireworksOverlay(this, this) { // from class: org.telegram.ui.LaunchActivity.5
            {
                setVisibility(8);
            }

            @Override // org.telegram.ui.Components.FireworksOverlay
            public void start() {
                setVisibility(0);
                super.start();
            }

            @Override // org.telegram.ui.Components.FireworksOverlay
            protected void onStop() {
                super.onStop();
                setVisibility(8);
            }
        };
        this.fireworksOverlay = fireworksOverlay;
        frameLayout2.addView(fireworksOverlay);
        setupActionBarLayout();
        this.sideMenuContainer = new FrameLayout(this);
        RecyclerListView recyclerListView = new RecyclerListView(this) { // from class: org.telegram.ui.LaunchActivity.6
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view2, long j) {
                int i3;
                if (LaunchActivity.this.itemAnimator != null && LaunchActivity.this.itemAnimator.isRunning() && LaunchActivity.this.itemAnimator.isAnimatingChild(view2)) {
                    i3 = canvas.save();
                    canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
                } else {
                    i3 = -1;
                }
                boolean drawChild = super.drawChild(canvas, view2, j);
                if (i3 >= 0) {
                    canvas.restoreToCount(i3);
                    invalidate();
                    invalidateViews();
                }
                return drawChild;
            }
        };
        this.sideMenu = recyclerListView;
        SideMenultItemAnimator sideMenultItemAnimator = new SideMenultItemAnimator(recyclerListView);
        this.itemAnimator = sideMenultItemAnimator;
        this.sideMenu.setItemAnimator(sideMenultItemAnimator);
        this.sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.sideMenu.setAllowItemsInteractionDuringAnimation(false);
        RecyclerListView recyclerListView2 = this.sideMenu;
        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, this.itemAnimator, this.drawerLayoutContainer);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView2.setAdapter(drawerLayoutAdapter);
        this.drawerLayoutAdapter.setOnPremiumDrawableClick(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LaunchActivity.this.lambda$onCreate$0(view2);
            }
        });
        this.sideMenuContainer.addView(this.sideMenu, LayoutHelper.createFrame(-1, -1.0f));
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenuContainer);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenuContainer.getLayoutParams();
        Point realScreenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(realScreenSize.x, realScreenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenuContainer.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda109
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view2, int i3) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view2, i3);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view2, int i3, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view2, i3, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view2, int i3, float f, float f2) {
                LaunchActivity.this.lambda$onCreate$2(view2, i3, f, f2);
            }
        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(3, 0) { // from class: org.telegram.ui.LaunchActivity.7
            private RecyclerView.ViewHolder selectedViewHolder;

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i3) {
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                    return false;
                }
                LaunchActivity.this.drawerLayoutAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
                return true;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i3) {
                clearSelectedViewHolder();
                if (i3 != 0) {
                    this.selectedViewHolder = viewHolder;
                    View view2 = viewHolder.itemView;
                    LaunchActivity.this.sideMenu.cancelClickRunnables(false);
                    view2.setBackgroundColor(Theme.getColor("dialogBackground"));
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator.ofFloat(view2, "elevation", AndroidUtilities.dp(1.0f)).setDuration(150L).start();
                    }
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                clearSelectedViewHolder();
            }

            private void clearSelectedViewHolder() {
                RecyclerView.ViewHolder viewHolder = this.selectedViewHolder;
                if (viewHolder != null) {
                    final View view2 = viewHolder.itemView;
                    this.selectedViewHolder = null;
                    view2.setTranslationX(0.0f);
                    view2.setTranslationY(0.0f);
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, "elevation", 0.0f);
                        ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.LaunchActivity.7.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                view2.setBackground(null);
                            }
                        });
                        ofFloat.setDuration(150L).start();
                    }
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i3, boolean z2) {
                View view2;
                View view3;
                View view4 = viewHolder.itemView;
                if (LaunchActivity.this.drawerLayoutAdapter.isAccountsShown()) {
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getFirstAccountPosition() - 1);
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getLastAccountPosition() + 1);
                    if ((findViewHolderForAdapterPosition != null && (view3 = findViewHolderForAdapterPosition.itemView) != null && view3.getBottom() == view4.getTop() && f2 < 0.0f) || (findViewHolderForAdapterPosition2 != null && (view2 = findViewHolderForAdapterPosition2.itemView) != null && view2.getTop() == view4.getBottom() && f2 > 0.0f)) {
                        f2 = 0.0f;
                    }
                }
                view4.setTranslationX(f);
                view4.setTranslationY(f2);
            }
        });
        itemTouchHelper.attachToRecyclerView(this.sideMenu);
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda110
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i3) {
                boolean lambda$onCreate$3;
                lambda$onCreate$3 = LaunchActivity.this.lambda$onCreate$3(itemTouchHelper, view2, i3);
                return lambda$onCreate$3;
            }
        });
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.setFragmentStack(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onCreate$4();
            }
        });
        this.actionBarLayout.setDelegate(this);
        Theme.loadWallpaper();
        checkCurrentAccount();
        updateCurrentConnectionState(this.currentAccount);
        NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
        int i3 = NotificationCenter.closeOtherAppActivities;
        globalInstance.postNotificationName(i3, this);
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().addObserver(this, i3);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.requestPermissions);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                this.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            } else {
                DialogsActivity dialogsActivity = new DialogsActivity(null);
                dialogsActivity.setSideMenu(this.sideMenu);
                this.actionBarLayout.addFragmentToStack(dialogsActivity);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
            if (bundle != null) {
                try {
                    String string = bundle.getString("fragment");
                    if (string != null) {
                        Bundle bundle2 = bundle.getBundle("args");
                        switch (string.hashCode()) {
                            case -1529105743:
                                if (string.equals("wallpapers")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case -1349522494:
                                if (string.equals("chat_profile")) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 3052376:
                                if (string.equals("chat")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 98629247:
                                if (string.equals("group")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 738950403:
                                if (string.equals("channel")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1434631203:
                                if (string.equals("settings")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        if (c != 0) {
                            if (c == 1) {
                                bundle2.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
                                ProfileActivity profileActivity = new ProfileActivity(bundle2);
                                this.actionBarLayout.addFragmentToStack(profileActivity);
                                profileActivity.restoreSelfArgs(bundle);
                            } else if (c != 2) {
                                if (c != 3) {
                                    if (c != 4) {
                                        if (c == 5) {
                                            WallpapersListActivity wallpapersListActivity = new WallpapersListActivity(0);
                                            this.actionBarLayout.addFragmentToStack(wallpapersListActivity);
                                            wallpapersListActivity.restoreSelfArgs(bundle);
                                        }
                                    } else if (bundle2 != null) {
                                        ProfileActivity profileActivity2 = new ProfileActivity(bundle2);
                                        if (this.actionBarLayout.addFragmentToStack(profileActivity2)) {
                                            profileActivity2.restoreSelfArgs(bundle);
                                        }
                                    }
                                } else if (bundle2 != null) {
                                    ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle2);
                                    if (this.actionBarLayout.addFragmentToStack(channelCreateActivity)) {
                                        channelCreateActivity.restoreSelfArgs(bundle);
                                    }
                                }
                            } else if (bundle2 != null) {
                                GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle2);
                                if (this.actionBarLayout.addFragmentToStack(groupCreateFinalActivity)) {
                                    groupCreateFinalActivity.restoreSelfArgs(bundle);
                                }
                            }
                        } else if (bundle2 != null) {
                            ChatActivity chatActivity = new ChatActivity(bundle2);
                            if (this.actionBarLayout.addFragmentToStack(chatActivity)) {
                                chatActivity.restoreSelfArgs(bundle);
                            }
                        }
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        } else {
            BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
            if (baseFragment instanceof DialogsActivity) {
                ((DialogsActivity) baseFragment).setSideMenu(this.sideMenu);
            }
            if (AndroidUtilities.isTablet()) {
                z = this.actionBarLayout.getFragmentStack().size() <= 1 && this.layersActionBarLayout.getFragmentStack().isEmpty();
                if (this.layersActionBarLayout.getFragmentStack().size() == 1 && ((this.layersActionBarLayout.getFragmentStack().get(0) instanceof LoginActivity) || (this.layersActionBarLayout.getFragmentStack().get(0) instanceof IntroActivity))) {
                    z = false;
                }
            } else {
                z = true;
            }
            if (this.actionBarLayout.getFragmentStack().size() == 1 && ((this.actionBarLayout.getFragmentStack().get(0) instanceof LoginActivity) || (this.actionBarLayout.getFragmentStack().get(0) instanceof IntroActivity))) {
                z = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(z, false);
        }
        checkLayout();
        checkSystemBarColors();
        handleIntent(getIntent(), false, bundle != null, false);
        try {
            String str = Build.DISPLAY;
            String str2 = Build.USER;
            String lowerCase2 = str != null ? str.toLowerCase() : "";
            String lowerCase3 = str2 != null ? lowerCase2.toLowerCase() : "";
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("OS name " + lowerCase2 + " " + lowerCase3);
            }
            if ((lowerCase2.contains("flyme") || lowerCase3.contains("flyme")) && Build.VERSION.SDK_INT <= 24) {
                AndroidUtilities.incorrectDisplaySizeFix = true;
                final View rootView = getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda23
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public final void onGlobalLayout() {
                        LaunchActivity.lambda$onCreate$5(rootView);
                    }
                };
                this.onGlobalLayoutListener = onGlobalLayoutListener;
                viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        MediaController.getInstance().setBaseActivity(this, true);
        ApplicationLoader.startAppCenter(this);
        updateAppUpdateViews(false);
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 23) {
            FingerprintController.checkKeyReady();
        }
        if (i4 >= 28 && ((ActivityManager) getSystemService("activity")).isBackgroundRestricted() && System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() >= 86400000 && SharedConfig.BackgroundActivityPrefs.getDismissedCount() < 3) {
            AlertsCreator.createBackgroundActivityDialog(this).show();
            SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
        }
        if (i4 >= 31) {
            getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: org.telegram.ui.LaunchActivity.9
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                    LaunchActivity.this.getWindowManager().addCrossWindowBlurEnabledListener($r8$wrapper$java$util$function$Consumer$-WRP.convert(LaunchActivity.this.blurListener));
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                    LaunchActivity.this.getWindowManager().removeCrossWindowBlurEnabledListener($r8$wrapper$java$util$function$Consumer$-WRP.convert(LaunchActivity.this.blurListener));
                }
            });
        }
        BackupAgent.requestBackup(this);
        RestrictedLanguagesSelectActivity.checkRestrictedLanguages(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 3 extends DrawerLayoutContainer {
        private boolean wasPortrait;

        3(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            setDrawerPosition(getDrawerPosition());
            boolean z2 = i4 - i2 > i3 - i;
            if (z2 != this.wasPortrait) {
                post(new Runnable() { // from class: org.telegram.ui.LaunchActivity$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.3.this.lambda$onLayout$0();
                    }
                });
                this.wasPortrait = z2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLayout$0() {
            if (LaunchActivity.this.selectAnimatedEmojiDialog != null) {
                LaunchActivity.this.selectAnimatedEmojiDialog.dismiss();
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
            }
        }

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer
        public void closeDrawer() {
            super.closeDrawer();
            if (LaunchActivity.this.selectAnimatedEmojiDialog != null) {
                LaunchActivity.this.selectAnimatedEmojiDialog.dismiss();
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
            }
        }

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer
        public void closeDrawer(boolean z) {
            super.closeDrawer(z);
            if (LaunchActivity.this.selectAnimatedEmojiDialog != null) {
                LaunchActivity.this.selectAnimatedEmojiDialog.dismiss();
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        showSelectStatusDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view, int i, float f, float f2) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        boolean z = true;
        if (i == 0) {
            DrawerProfileCell drawerProfileCell = (DrawerProfileCell) view;
            if (drawerProfileCell.isInAvatar(f, f2)) {
                openSettings(drawerProfileCell.hasAvatar());
                return;
            }
            this.drawerLayoutAdapter.setAccountsShown(!drawerLayoutAdapter.isAccountsShown(), true);
        } else if (view instanceof DrawerUserCell) {
            switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
            this.drawerLayoutContainer.closeDrawer(false);
        } else {
            Integer num = null;
            if (view instanceof DrawerAddCell) {
                int i2 = 0;
                for (int i3 = 3; i3 >= 0; i3--) {
                    if (!UserConfig.getInstance(i3).isClientActivated()) {
                        i2++;
                        if (num == null) {
                            num = Integer.valueOf(i3);
                        }
                    }
                }
                if (!UserConfig.hasPremiumOnAccounts()) {
                    i2--;
                }
                if (i2 > 0 && num != null) {
                    lambda$runLinkRequest$72(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.getFragmentStack().size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onCreate$1();
                        }
                    };
                    return;
                }
            }
            int id = this.drawerLayoutAdapter.getId(i);
            if (id == 2) {
                lambda$runLinkRequest$72(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$72(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$72(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$72(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                lambda$runLinkRequest$72(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$72(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$72(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$72(new ChatActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$72(new ActionIntroActivity(1));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                }
                if (i4 >= 28) {
                    z = ((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isLocationEnabled();
                } else if (i4 >= 19) {
                    try {
                        if (Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0) == 0) {
                            z = false;
                        }
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                if (z) {
                    lambda$runLinkRequest$72(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$72(new ActionIntroActivity(4));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 13) {
                Browser.openUrl(this, LocaleController.getString("TelegramFeaturesUrl", R.string.TelegramFeaturesUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 15) {
                showSelectStatusDialog();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1() {
        this.drawerLayoutContainer.closeDrawer(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$3(ItemTouchHelper itemTouchHelper, View view, int i) {
        if (view instanceof DrawerUserCell) {
            final int accountNumber = ((DrawerUserCell) view).getAccountNumber();
            if (accountNumber == this.currentAccount || AndroidUtilities.isTablet()) {
                itemTouchHelper.startDrag(this.sideMenu.getChildViewHolder(view));
                return false;
            }
            DialogsActivity dialogsActivity = new DialogsActivity(null) { // from class: org.telegram.ui.LaunchActivity.8
                @Override // org.telegram.ui.DialogsActivity, org.telegram.ui.ActionBar.BaseFragment
                public void onTransitionAnimationEnd(boolean z, boolean z2) {
                    super.onTransitionAnimationEnd(z, z2);
                    if (z || !z2) {
                        return;
                    }
                    LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                    LaunchActivity.this.actionBarLayout.getView().invalidate();
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public void onPreviewOpenAnimationEnd() {
                    super.onPreviewOpenAnimationEnd();
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                    LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                    LaunchActivity.this.switchToAccount(accountNumber, true);
                    LaunchActivity.this.actionBarLayout.getView().invalidate();
                }
            };
            dialogsActivity.setCurrentAccount(accountNumber);
            this.actionBarLayout.presentFragmentAsPreview(dialogsActivity);
            this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(true);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4() {
        checkSystemBarColors(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreate$5(View view) {
        int measuredHeight = view.getMeasuredHeight();
        FileLog.d("height = " + measuredHeight + " displayHeight = " + AndroidUtilities.displaySize.y);
        if (Build.VERSION.SDK_INT >= 21) {
            measuredHeight -= AndroidUtilities.statusBarHeight;
        }
        if (measuredHeight <= AndroidUtilities.dp(100.0f) || measuredHeight >= AndroidUtilities.displaySize.y) {
            return;
        }
        int dp = AndroidUtilities.dp(100.0f) + measuredHeight;
        Point point = AndroidUtilities.displaySize;
        if (dp > point.y) {
            point.y = measuredHeight;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("fix display size y to " + AndroidUtilities.displaySize.y);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public void onThemeProgress(float f) {
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().updateThemeColors(f);
        }
        this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().updateColors();
        }
    }

    private void setupActionBarLayout() {
        DrawerLayoutContainer drawerLayoutContainer;
        ViewGroup view;
        if (this.drawerLayoutContainer.indexOfChild(this.launchLayout) != -1) {
            drawerLayoutContainer = this.drawerLayoutContainer;
            view = this.launchLayout;
        } else {
            drawerLayoutContainer = this.drawerLayoutContainer;
            view = this.actionBarLayout.getView();
        }
        int indexOfChild = drawerLayoutContainer.indexOfChild(view);
        if (indexOfChild != -1) {
            this.drawerLayoutContainer.removeViewAt(indexOfChild);
        }
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout relativeLayout = new RelativeLayout(this) { // from class: org.telegram.ui.LaunchActivity.10
                private boolean inLayout;

                {
                    new Path();
                }

                @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
                public void requestLayout() {
                    if (this.inLayout) {
                        return;
                    }
                    super.requestLayout();
                }

                @Override // android.widget.RelativeLayout, android.view.View
                protected void onMeasure(int i, int i2) {
                    this.inLayout = true;
                    int size = View.MeasureSpec.getSize(i);
                    int size2 = View.MeasureSpec.getSize(i2);
                    setMeasuredDimension(size, size2);
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.tabletFullSize = true;
                        LaunchActivity.this.actionBarLayout.getView().measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    } else {
                        LaunchActivity.this.tabletFullSize = false;
                        int i3 = (size / 100) * 35;
                        if (i3 < AndroidUtilities.dp(320.0f)) {
                            i3 = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.actionBarLayout.getView().measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        LaunchActivity.this.shadowTabletSide.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        LaunchActivity.this.rightActionBarLayout.getView().measure(View.MeasureSpec.makeMeasureSpec(size - i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    }
                    LaunchActivity.this.backgroundTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    LaunchActivity.this.shadowTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    LaunchActivity.this.layersActionBarLayout.getView().measure(View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), size - AndroidUtilities.dp(16.0f)), 1073741824), View.MeasureSpec.makeMeasureSpec((size2 - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(16.0f), 1073741824));
                    this.inLayout = false;
                }

                @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                    int i5 = i3 - i;
                    int i6 = i4 - i2;
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.actionBarLayout.getView().layout(0, 0, LaunchActivity.this.actionBarLayout.getView().getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getView().getMeasuredHeight());
                    } else {
                        int i7 = (i5 / 100) * 35;
                        if (i7 < AndroidUtilities.dp(320.0f)) {
                            i7 = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.shadowTabletSide.layout(i7, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + i7, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                        LaunchActivity.this.actionBarLayout.getView().layout(0, 0, LaunchActivity.this.actionBarLayout.getView().getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getView().getMeasuredHeight());
                        LaunchActivity.this.rightActionBarLayout.getView().layout(i7, 0, LaunchActivity.this.rightActionBarLayout.getView().getMeasuredWidth() + i7, LaunchActivity.this.rightActionBarLayout.getView().getMeasuredHeight());
                    }
                    int measuredWidth = (i5 - LaunchActivity.this.layersActionBarLayout.getView().getMeasuredWidth()) / 2;
                    int measuredHeight = ((i6 - LaunchActivity.this.layersActionBarLayout.getView().getMeasuredHeight()) + AndroidUtilities.statusBarHeight) / 2;
                    LaunchActivity.this.layersActionBarLayout.getView().layout(measuredWidth, measuredHeight, LaunchActivity.this.layersActionBarLayout.getView().getMeasuredWidth() + measuredWidth, LaunchActivity.this.layersActionBarLayout.getView().getMeasuredHeight() + measuredHeight);
                    LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
                    LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
                }
            };
            this.launchLayout = relativeLayout;
            if (indexOfChild != -1) {
                this.drawerLayoutContainer.addView(relativeLayout, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
            } else {
                this.drawerLayoutContainer.addView(relativeLayout, LayoutHelper.createFrame(-1, -1.0f));
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(this, this) { // from class: org.telegram.ui.LaunchActivity.11
                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
                protected boolean isActionBarVisible() {
                    return false;
                }
            };
            this.backgroundTablet = sizeNotifierFrameLayout;
            sizeNotifierFrameLayout.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            this.launchLayout.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            ViewGroup viewGroup = (ViewGroup) this.actionBarLayout.getView().getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.actionBarLayout.getView());
            }
            this.launchLayout.addView(this.actionBarLayout.getView());
            INavigationLayout newLayout = INavigationLayout.-CC.newLayout(this);
            this.rightActionBarLayout = newLayout;
            newLayout.setFragmentStack(rightFragmentsStack);
            this.rightActionBarLayout.setDelegate(this);
            this.launchLayout.addView(this.rightActionBarLayout.getView());
            FrameLayout frameLayout = new FrameLayout(this);
            this.shadowTabletSide = frameLayout;
            frameLayout.setBackgroundColor(1076449908);
            this.launchLayout.addView(this.shadowTabletSide);
            FrameLayout frameLayout2 = new FrameLayout(this);
            this.shadowTablet = frameLayout2;
            frameLayout2.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(2130706432);
            this.launchLayout.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$setupActionBarLayout$6;
                    lambda$setupActionBarLayout$6 = LaunchActivity.this.lambda$setupActionBarLayout$6(view2, motionEvent);
                    return lambda$setupActionBarLayout$6;
                }
            });
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda21.INSTANCE);
            INavigationLayout newLayout2 = INavigationLayout.-CC.newLayout(this);
            this.layersActionBarLayout = newLayout2;
            newLayout2.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(this.shadowTablet);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setFragmentStack(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
            ViewGroup view2 = this.layersActionBarLayout.getView();
            view2.setBackgroundResource(R.drawable.popup_fixed_alert3);
            view2.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.launchLayout.addView(view2);
        } else {
            ViewGroup viewGroup2 = (ViewGroup) this.actionBarLayout.getView().getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView(this.actionBarLayout.getView());
            }
            this.actionBarLayout.setFragmentStack(mainFragmentsStack);
            if (indexOfChild != -1) {
                this.drawerLayoutContainer.addView(this.actionBarLayout.getView(), indexOfChild, new ViewGroup.LayoutParams(-1, -1));
            } else {
                this.drawerLayoutContainer.addView(this.actionBarLayout.getView(), new ViewGroup.LayoutParams(-1, -1));
            }
        }
        FloatingDebugController.setActive(this, SharedConfig.isFloatingDebugActive, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupActionBarLayout$6(View view, MotionEvent motionEvent) {
        if (!this.actionBarLayout.getFragmentStack().isEmpty() && motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] iArr = new int[2];
            this.layersActionBarLayout.getView().getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            if (!this.layersActionBarLayout.checkTransitionAnimation() && (x <= i || x >= i + this.layersActionBarLayout.getView().getWidth() || y <= i2 || y >= i2 + this.layersActionBarLayout.getView().getHeight())) {
                if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                    while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                        INavigationLayout iNavigationLayout = this.layersActionBarLayout;
                        iNavigationLayout.removeFragmentFromStack(iNavigationLayout.getFragmentStack().get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                }
                return true;
            }
        }
        return false;
    }

    public void addOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.add(runnable);
    }

    public void removeOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.remove(runnable);
    }

    private BaseFragment getClientNotActivatedFragment() {
        if (LoginActivity.loadCurrentState(false, this.currentAccount).getInt("currentViewNum", 0) != 0) {
            return new LoginActivity();
        }
        return new IntroActivity();
    }

    public void showSelectStatusDialog() {
        BaseFragment lastFragment;
        int i;
        View view;
        int i2;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        if (this.selectAnimatedEmojiDialog != null || SharedConfig.appLocked || (lastFragment = this.actionBarLayout.getLastFragment()) == null) {
            return;
        }
        View childAt = this.sideMenu.getChildAt(0);
        final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()));
        if (childAt instanceof DrawerProfileCell) {
            DrawerProfileCell drawerProfileCell = (DrawerProfileCell) childAt;
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emojiStatusDrawable = drawerProfileCell.getEmojiStatusDrawable();
            if (emojiStatusDrawable != null) {
                emojiStatusDrawable.play();
            }
            View emojiStatusDrawableParent = drawerProfileCell.getEmojiStatusDrawableParent();
            if (emojiStatusDrawable != null) {
                boolean z = emojiStatusDrawable.getDrawable() instanceof AnimatedEmojiDrawable;
            }
            Rect rect = AndroidUtilities.rectTmp2;
            drawerProfileCell.getEmojiStatusLocation(rect);
            int dp = (-(childAt.getHeight() - rect.centerY())) - AndroidUtilities.dp(16.0f);
            i = rect.centerX();
            if (Build.VERSION.SDK_INT >= 23 && getWindow() != null && getWindow().getDecorView() != null && getWindow().getDecorView().getRootWindowInsets() != null) {
                i -= getWindow().getDecorView().getRootWindowInsets().getStableInsetLeft();
            }
            i2 = dp;
            swapAnimatedEmojiDrawable = emojiStatusDrawable;
            view = emojiStatusDrawableParent;
        } else {
            i = 0;
            view = null;
            i2 = 0;
            swapAnimatedEmojiDrawable = null;
        }
        View view2 = view;
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(lastFragment, this, true, Integer.valueOf(i), 0, null) { // from class: org.telegram.ui.LaunchActivity.12
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            public void onSettings() {
                DrawerLayoutContainer drawerLayoutContainer = LaunchActivity.this.drawerLayoutContainer;
                if (drawerLayoutContainer != null) {
                    drawerLayoutContainer.closeDrawer();
                }
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view3, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil;
                String string;
                int i3;
                if (l == null) {
                    tLRPC$TL_emojiStatusUntil = new TLRPC$TL_emojiStatusEmpty();
                } else if (num != null) {
                    TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil2 = new TLRPC$TL_emojiStatusUntil();
                    tLRPC$TL_emojiStatusUntil2.document_id = l.longValue();
                    tLRPC$TL_emojiStatusUntil2.until = num.intValue();
                    tLRPC$TL_emojiStatusUntil = tLRPC$TL_emojiStatusUntil2;
                } else {
                    TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
                    tLRPC$TL_emojiStatus.document_id = l.longValue();
                    tLRPC$TL_emojiStatusUntil = tLRPC$TL_emojiStatus;
                }
                MessagesController.getInstance(LaunchActivity.this.currentAccount).updateEmojiStatus(tLRPC$TL_emojiStatusUntil);
                TLRPC$User currentUser = UserConfig.getInstance(LaunchActivity.this.currentAccount).getCurrentUser();
                if (currentUser != null) {
                    for (int i4 = 0; i4 < LaunchActivity.this.sideMenu.getChildCount(); i4++) {
                        View childAt2 = LaunchActivity.this.sideMenu.getChildAt(i4);
                        if (childAt2 instanceof DrawerUserCell) {
                            DrawerUserCell drawerUserCell = (DrawerUserCell) childAt2;
                            drawerUserCell.setAccount(drawerUserCell.getAccountNumber());
                        } else if (childAt2 instanceof DrawerProfileCell) {
                            if (l != null) {
                                ((DrawerProfileCell) childAt2).animateStateChange(l.longValue());
                            }
                            ((DrawerProfileCell) childAt2).setUser(currentUser, LaunchActivity.this.drawerLayoutAdapter.isAccountsShown());
                        } else if ((childAt2 instanceof DrawerActionCell) && LaunchActivity.this.drawerLayoutAdapter.getId(LaunchActivity.this.sideMenu.getChildAdapterPosition(childAt2)) == 15) {
                            TLRPC$EmojiStatus tLRPC$EmojiStatus = currentUser.emoji_status;
                            boolean z2 = (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatus) || ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000)));
                            DrawerActionCell drawerActionCell = (DrawerActionCell) childAt2;
                            if (z2) {
                                string = LocaleController.getString("ChangeEmojiStatus", R.string.ChangeEmojiStatus);
                            } else {
                                string = LocaleController.getString("SetEmojiStatus", R.string.SetEmojiStatus);
                            }
                            drawerActionCell.updateText(string);
                            if (z2) {
                                i3 = R.raw.emoji_status_change_to_set;
                            } else {
                                i3 = R.raw.emoji_status_set_to_change;
                            }
                            drawerActionCell.updateIcon(i3);
                        }
                    }
                }
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    LaunchActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }
        };
        if (user != null) {
            TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
            if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                selectAnimatedEmojiDialog.setExpireDateHint(((TLRPC$TL_emojiStatusUntil) user.emoji_status).until);
            }
        }
        selectAnimatedEmojiDialog.setSelected((swapAnimatedEmojiDrawable == null || !(swapAnimatedEmojiDrawable.getDrawable() instanceof AnimatedEmojiDrawable)) ? null : Long.valueOf(((AnimatedEmojiDrawable) swapAnimatedEmojiDrawable.getDrawable()).getDocumentId()));
        selectAnimatedEmojiDialog.setSaveState(2);
        selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, view2);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.LaunchActivity.13
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(this.sideMenu.getChildAt(0), 0, i2, 48);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    public FireworksOverlay getFireworksOverlay() {
        return this.fireworksOverlay;
    }

    private void openSettings(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
        if (z) {
            bundle.putBoolean("expandPhoto", true);
        }
        lambda$runLinkRequest$72(new ProfileActivity(bundle));
        this.drawerLayoutContainer.closeDrawer(false);
    }

    private void checkSystemBarColors() {
        checkSystemBarColors(false, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean z) {
        checkSystemBarColors(z, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean z, boolean z2) {
        checkSystemBarColors(false, z, z2);
    }

    private void checkSystemBarColors(boolean z, boolean z2, boolean z3) {
        BaseFragment baseFragment;
        boolean z4;
        ArrayList<BaseFragment> arrayList;
        if (mainFragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        }
        if (baseFragment != null && (baseFragment.isRemovingFromStack() || baseFragment.isInPreviewMode())) {
            if (mainFragmentsStack.size() > 1) {
                baseFragment = mainFragmentsStack.get(arrayList.size() - 2);
            } else {
                baseFragment = null;
            }
        }
        boolean z5 = baseFragment != null && baseFragment.hasForceLightStatusBar();
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            if (z2) {
                if (baseFragment != null) {
                    z4 = baseFragment.isLightStatusBar();
                } else {
                    z4 = ColorUtils.calculateLuminance(Theme.getColor("actionBarDefault", null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), z4, z5);
            }
            if (i >= 26 && z3 && (!z || baseFragment == null || !baseFragment.isInPreviewMode())) {
                Window window = getWindow();
                int color = (baseFragment == null || !z) ? Theme.getColor("windowBackgroundGray", null, true) : baseFragment.getNavigationBarColor();
                if (window.getNavigationBarColor() != color) {
                    window.setNavigationBarColor(color);
                    AndroidUtilities.setLightNavigationBar(getWindow(), AndroidUtilities.computePerceivedBrightness(color) >= 0.721f);
                }
            }
        }
        if ((SharedConfig.noStatusBar || z5) && i >= 21 && z2) {
            getWindow().setStatusBarColor(0);
        }
    }

    public FrameLayout getMainContainerFrameLayout() {
        return this.frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ DialogsActivity lambda$switchToAccount$8(Void r1) {
        return new DialogsActivity(null);
    }

    public void switchToAccount(int i, boolean z) {
        switchToAccount(i, z, LaunchActivity$$ExternalSyntheticLambda77.INSTANCE);
    }

    public void switchToAccount(int i, boolean z, GenericProvider<Void, DialogsActivity> genericProvider) {
        if (i == UserConfig.selectedAccount || !UserConfig.isValidAccount(i)) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        UserConfig.selectedAccount = i;
        UserConfig.getInstance(0).saveConfig(false);
        checkCurrentAccount();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.removeAllFragments();
            this.rightActionBarLayout.removeAllFragments();
            if (!this.tabletFullSize) {
                this.shadowTabletSide.setVisibility(0);
                if (this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                    this.backgroundTablet.setVisibility(0);
                }
                this.rightActionBarLayout.getView().setVisibility(8);
            }
            this.layersActionBarLayout.getView().setVisibility(8);
        }
        if (z) {
            this.actionBarLayout.removeAllFragments();
        } else {
            this.actionBarLayout.removeFragmentFromStack(0);
        }
        DialogsActivity provide = genericProvider.provide(null);
        provide.setSideMenu(this.sideMenu);
        this.actionBarLayout.addFragmentToStack(provide, 0);
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.rebuildFragments(1);
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildFragments(1);
            this.rightActionBarLayout.rebuildFragments(1);
        }
        if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        }
        if (UserConfig.getInstance(i).unacceptedTermsOfService != null) {
            showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
        }
        updateCurrentConnectionState(this.currentAccount);
    }

    private void switchToAvailableAccountOrLogout() {
        int i = 0;
        while (true) {
            if (i >= 4) {
                i = -1;
                break;
            } else if (UserConfig.getInstance(i).isClientActivated()) {
                break;
            } else {
                i++;
            }
        }
        TermsOfServiceView termsOfServiceView = this.termsOfServiceView;
        if (termsOfServiceView != null) {
            termsOfServiceView.setVisibility(8);
        }
        if (i != -1) {
            switchToAccount(i, true);
            return;
        }
        DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
        if (drawerLayoutAdapter != null) {
            drawerLayoutAdapter.notifyDataSetChanged();
        }
        RestrictedLanguagesSelectActivity.checkRestrictedLanguages(true);
        clearFragments();
        this.actionBarLayout.rebuildLogout();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildLogout();
            this.rightActionBarLayout.rebuildLogout();
        }
        lambda$runLinkRequest$72(new IntroActivity().setOnLogout());
    }

    public static void clearFragments() {
        Iterator<BaseFragment> it = mainFragmentsStack.iterator();
        while (it.hasNext()) {
            it.next().onFragmentDestroy();
        }
        mainFragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            Iterator<BaseFragment> it2 = layerFragmentsStack.iterator();
            while (it2.hasNext()) {
                it2.next().onFragmentDestroy();
            }
            layerFragmentsStack.clear();
            Iterator<BaseFragment> it3 = rightFragmentsStack.iterator();
            while (it3.hasNext()) {
                it3.next().onFragmentDestroy();
            }
            rightFragmentsStack.clear();
        }
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        int i = this.currentAccount;
        if (i != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowPlayServicesAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.historyImportProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersImportComplete);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatSwithcedToForum);
        }
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowPlayServicesAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.historyImportProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupCallUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersImportComplete);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newSuggestionsAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserShowLimitReachedDialog);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatSwithcedToForum);
    }

    private void checkLayout() {
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        if (AndroidUtilities.getWasTablet() == null || AndroidUtilities.getWasTablet().booleanValue() == AndroidUtilities.isTabletForce()) {
            if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2)) {
                this.tabletFullSize = false;
                if (this.actionBarLayout.getFragmentStack().size() >= 2) {
                    while (1 < this.actionBarLayout.getFragmentStack().size()) {
                        BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(1);
                        if (baseFragment instanceof ChatActivity) {
                            ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                        }
                        baseFragment.onPause();
                        this.actionBarLayout.removeFragmentFromStack(1);
                        this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    }
                    PasscodeView passcodeView = this.passcodeView;
                    if (passcodeView == null || passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.rebuildFragments(1);
                        this.rightActionBarLayout.rebuildFragments(1);
                    }
                }
                this.rightActionBarLayout.getView().setVisibility(this.rightActionBarLayout.getFragmentStack().isEmpty() ? 8 : 0);
                this.backgroundTablet.setVisibility(this.rightActionBarLayout.getFragmentStack().isEmpty() ? 0 : 8);
                this.shadowTabletSide.setVisibility(this.actionBarLayout.getFragmentStack().isEmpty() ? 8 : 0);
                return;
            }
            this.tabletFullSize = true;
            if (!this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                while (this.rightActionBarLayout.getFragmentStack().size() > 0) {
                    BaseFragment baseFragment2 = this.rightActionBarLayout.getFragmentStack().get(0);
                    if (baseFragment2 instanceof ChatActivity) {
                        ((ChatActivity) baseFragment2).setIgnoreAttachOnPause(true);
                    }
                    baseFragment2.onPause();
                    this.rightActionBarLayout.removeFragmentFromStack(0);
                    this.actionBarLayout.addFragmentToStack(baseFragment2);
                }
                PasscodeView passcodeView2 = this.passcodeView;
                if (passcodeView2 == null || passcodeView2.getVisibility() != 0) {
                    this.actionBarLayout.rebuildFragments(1);
                }
            }
            this.shadowTabletSide.setVisibility(8);
            this.rightActionBarLayout.getView().setVisibility(8);
            this.backgroundTablet.setVisibility(this.actionBarLayout.getFragmentStack().isEmpty() ? 0 : 8);
        }
    }

    private void showUpdateActivity(int i, TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, boolean z) {
        if (this.blockingUpdateView == null) {
            BlockingUpdateView blockingUpdateView = new BlockingUpdateView(this) { // from class: org.telegram.ui.LaunchActivity.14
                @Override // org.telegram.ui.Components.BlockingUpdateView, android.view.View
                public void setVisibility(int i2) {
                    super.setVisibility(i2);
                    if (i2 == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.blockingUpdateView = blockingUpdateView;
            this.drawerLayoutContainer.addView(blockingUpdateView, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(i, tLRPC$TL_help_appUpdate, z);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showTosActivity(int i, TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService) {
        if (this.termsOfServiceView == null) {
            TermsOfServiceView termsOfServiceView = new TermsOfServiceView(this);
            this.termsOfServiceView = termsOfServiceView;
            termsOfServiceView.setAlpha(0.0f);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new 15());
        }
        TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService2 = UserConfig.getInstance(i).unacceptedTermsOfService;
        if (tLRPC$TL_help_termsOfService2 != tLRPC$TL_help_termsOfService && (tLRPC$TL_help_termsOfService2 == null || !tLRPC$TL_help_termsOfService2.id.data.equals(tLRPC$TL_help_termsOfService.id.data))) {
            UserConfig.getInstance(i).unacceptedTermsOfService = tLRPC$TL_help_termsOfService;
            UserConfig.getInstance(i).saveConfig(false);
        }
        this.termsOfServiceView.show(i, tLRPC$TL_help_termsOfService);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.termsOfServiceView.animate().alpha(1.0f).setDuration(150L).setInterpolator(AndroidUtilities.decelerateInterpolator).setListener(null).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 15 implements TermsOfServiceView.TermsOfServiceViewDelegate {
        15() {
        }

        @Override // org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate
        public void onAcceptTerms(int i) {
            UserConfig.getInstance(i).unacceptedTermsOfService = null;
            UserConfig.getInstance(i).saveConfig(false);
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            if (LaunchActivity.mainFragmentsStack.size() > 0) {
                ((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1)).onResume();
            }
            LaunchActivity.this.termsOfServiceView.animate().alpha(0.0f).setDuration(150L).setInterpolator(AndroidUtilities.accelerateInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$15$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.15.this.lambda$onAcceptTerms$0();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcceptTerms$0() {
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }
    }

    public void showPasscodeActivity(boolean z, boolean z2, int i, int i2, final Runnable runnable, Runnable runnable2) {
        if (this.drawerLayoutContainer == null) {
            return;
        }
        if (this.passcodeView == null) {
            PasscodeView passcodeView = new PasscodeView(this);
            this.passcodeView = passcodeView;
            this.drawerLayoutContainer.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        }
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = this.selectAnimatedEmojiDialog;
        if (selectAnimatedEmojiDialogWindow != null) {
            selectAnimatedEmojiDialogWindow.dismiss();
            this.selectAnimatedEmojiDialog = null;
        }
        SharedConfig.appLocked = true;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null && playingMessageObject.isRoundVideo()) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
        this.passcodeView.onShow(this.overlayPasscodeViews.isEmpty() && z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showPasscodeActivity$9(runnable);
            }
        }, runnable2);
        int i3 = 0;
        while (i3 < this.overlayPasscodeViews.size()) {
            this.overlayPasscodeViews.get(i3).onShow(z && i3 == this.overlayPasscodeViews.size() - 1, z2, i, i2, null, null);
            i3++;
        }
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        PasscodeView.PasscodeViewDelegate passcodeViewDelegate = new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda108
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword(PasscodeView passcodeView2) {
                LaunchActivity.this.lambda$showPasscodeActivity$10(passcodeView2);
            }
        };
        this.passcodeView.setDelegate(passcodeViewDelegate);
        for (PasscodeView passcodeView2 : this.overlayPasscodeViews) {
            passcodeView2.setDelegate(passcodeViewDelegate);
        }
        try {
            NotificationsController.getInstance(UserConfig.selectedAccount).showNotifications();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPasscodeActivity$9(Runnable runnable) {
        this.actionBarLayout.getView().setVisibility(4);
        if (AndroidUtilities.isTablet()) {
            INavigationLayout iNavigationLayout = this.layersActionBarLayout;
            if (iNavigationLayout != null && iNavigationLayout.getView() != null && this.layersActionBarLayout.getView().getVisibility() == 0) {
                this.layersActionBarLayout.getView().setVisibility(4);
            }
            INavigationLayout iNavigationLayout2 = this.rightActionBarLayout;
            if (iNavigationLayout2 != null && iNavigationLayout2.getView() != null) {
                this.rightActionBarLayout.getView().setVisibility(4);
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPasscodeActivity$10(PasscodeView passcodeView) {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true);
            this.passcodeSaveIntent = null;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.getView().setVisibility(0);
        this.actionBarLayout.rebuildFragments(1);
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildFragments(1);
            this.rightActionBarLayout.rebuildFragments(1);
            if (this.layersActionBarLayout.getView().getVisibility() == 4) {
                this.layersActionBarLayout.getView().setVisibility(0);
            }
            this.rightActionBarLayout.getView().setVisibility(0);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.passcodeDismissed, passcodeView);
        try {
            NotificationsController.getInstance(UserConfig.selectedAccount).showNotifications();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean allowShowFingerprintDialog(PasscodeView passcodeView) {
        if (!this.overlayPasscodeViews.isEmpty()) {
            List<PasscodeView> list = this.overlayPasscodeViews;
            if (list.get(list.size() - 1) != passcodeView) {
                return false;
            }
        } else if (passcodeView != this.passcodeView) {
            return false;
        }
        return true;
    }

    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3) {
        return lambda$handleIntent$15(intent, z, z2, z3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:1071:0x200e, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r3)) != false) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x031b, code lost:
        if (r85.sendingText == null) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x09e5, code lost:
        if (r16 == 0) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0ade, code lost:
        if (r9.intValue() == 0) goto L769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0146, code lost:
        if (r1.equals(r0) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:0x1795, code lost:
        if (r9 == 0) goto L1119;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1002:0x1d29  */
    /* JADX WARN: Removed duplicated region for block: B:1003:0x1d39  */
    /* JADX WARN: Removed duplicated region for block: B:1049:0x1fab  */
    /* JADX WARN: Removed duplicated region for block: B:1062:0x1fda  */
    /* JADX WARN: Removed duplicated region for block: B:1082:0x205c  */
    /* JADX WARN: Removed duplicated region for block: B:1177:0x2292  */
    /* JADX WARN: Removed duplicated region for block: B:1178:0x22a1  */
    /* JADX WARN: Removed duplicated region for block: B:1181:0x22af  */
    /* JADX WARN: Removed duplicated region for block: B:1182:0x22c1  */
    /* JADX WARN: Removed duplicated region for block: B:1244:0x2506 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1247:0x250e  */
    /* JADX WARN: Removed duplicated region for block: B:1258:0x255d  */
    /* JADX WARN: Removed duplicated region for block: B:1269:0x25aa  */
    /* JADX WARN: Removed duplicated region for block: B:1271:0x25b6  */
    /* JADX WARN: Removed duplicated region for block: B:1273:0x25be  */
    /* JADX WARN: Removed duplicated region for block: B:1302:0x1334 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1336:0x1c50 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0322  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x03e2  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0514  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x073b  */
    /* JADX WARN: Removed duplicated region for block: B:392:0x082b A[Catch: Exception -> 0x0839, TRY_LEAVE, TryCatch #25 {Exception -> 0x0839, blocks: (B:390:0x081f, B:392:0x082b), top: B:1338:0x081f }] */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0838  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x0b93  */
    /* JADX WARN: Removed duplicated region for block: B:498:0x0bb8  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:793:0x161d  */
    /* JADX WARN: Removed duplicated region for block: B:830:0x16fc A[Catch: Exception -> 0x170a, TRY_LEAVE, TryCatch #19 {Exception -> 0x170a, blocks: (B:828:0x16f0, B:830:0x16fc), top: B:1326:0x16f0 }] */
    /* JADX WARN: Removed duplicated region for block: B:832:0x1709  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:890:0x1a3d  */
    /* JADX WARN: Removed duplicated region for block: B:891:0x1a5b  */
    /* JADX WARN: Removed duplicated region for block: B:907:0x1b1c  */
    /* JADX WARN: Removed duplicated region for block: B:919:0x1b60  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01e9  */
    /* JADX WARN: Type inference failed for: r0v278, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v302, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v480, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r11v8, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r12v172 */
    /* JADX WARN: Type inference failed for: r12v174 */
    /* JADX WARN: Type inference failed for: r12v187 */
    /* JADX WARN: Type inference failed for: r12v188 */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v9, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r1v193, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v216, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r1v24, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r1v27, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r1v270, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v29, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r1v30, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r1v38, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r1v400, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r2v89, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r43v36 */
    /* JADX WARN: Type inference failed for: r43v37 */
    /* JADX WARN: Type inference failed for: r43v41 */
    /* JADX WARN: Type inference failed for: r7v10, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v273, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r7v352 */
    /* JADX WARN: Type inference failed for: r7v354 */
    /* JADX WARN: Type inference failed for: r7v361 */
    /* JADX WARN: Type inference failed for: r8v62, types: [java.lang.Long] */
    @SuppressLint({"Range"})
    /* renamed from: handleIntent */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean lambda$handleIntent$15(final Intent intent, final boolean z, final boolean z2, final boolean z3, final Browser.Progress progress) {
        long j;
        String str;
        Intent intent2;
        final LaunchActivity launchActivity;
        final int[] iArr;
        final boolean z4;
        Intent intent3;
        long j2;
        long j3;
        long j4;
        int i;
        int i2;
        int i3;
        int i4;
        String str2;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        Intent intent4;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15;
        Intent intent5;
        Intent intent6;
        Intent intent7;
        String str3;
        String str4;
        String str5;
        int i5;
        char c;
        Intent intent8;
        boolean z16;
        boolean z17;
        boolean z18;
        boolean z19;
        ?? r12;
        ?? r7;
        GroupCallActivity groupCallActivity;
        ?? r3;
        char c2;
        BaseFragment editWidgetActivity;
        final boolean z20;
        final BaseFragment baseFragment;
        BaseFragment baseFragment2;
        boolean z21;
        boolean z22;
        boolean z23;
        boolean z24;
        boolean z25;
        String str6;
        boolean z26;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        ArrayList arrayList2;
        boolean z27;
        Pattern compile;
        int i6;
        int i7;
        int i8;
        char c3;
        String str7;
        int[] iArr2;
        long j5;
        int i9;
        char c4;
        boolean z28;
        boolean z29;
        long j6;
        String str8;
        String str9;
        String str10;
        String str11;
        long j7;
        long j8;
        String str12;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        boolean z30;
        String str23;
        String str24;
        String str25;
        String str26;
        int i10;
        char c5;
        String str27;
        String str28;
        String str29;
        String str30;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        String str36;
        String str37;
        int i11;
        String str38;
        String str39;
        String str40;
        String str41;
        String str42;
        final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Cursor query;
        boolean z31;
        char c6;
        long j9;
        Integer num;
        ?? parseInt;
        String str43;
        String str44;
        long j10;
        String str45;
        boolean z32;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z33;
        int i12;
        long j11;
        long j12;
        int parseInt2;
        long j13;
        String str46;
        String str47;
        String str48;
        boolean z34;
        boolean z35;
        boolean z36;
        boolean z37;
        String str49;
        boolean z38;
        String str50;
        String str51;
        String str52;
        String str53;
        String str54;
        String str55;
        String str56;
        String str57;
        String str58;
        String str59;
        String str60;
        int i13;
        String str61;
        String str62;
        String str63;
        String str64;
        String str65;
        String str66;
        String str67;
        String str68;
        String str69;
        Integer num2;
        String str70;
        Integer num3;
        ?? r43;
        int i14;
        int i15;
        String str71;
        Integer num4;
        Integer num5;
        Integer num6;
        ?? parseInt3;
        Integer num7;
        Integer num8;
        String str72;
        Integer num9;
        String str73;
        boolean z39;
        String queryParameter3;
        String queryParameter4;
        String[] split2;
        String substring;
        long j14;
        String type2;
        String stringExtra;
        Parcelable parcelableExtra;
        boolean z40;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str74;
        String str75;
        if (AndroidUtilities.handleProxyIntent(this, intent)) {
            return true;
        }
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible() && (intent == null || !"android.intent.action.MAIN".equals(intent.getAction()))) {
            PhotoViewer.getInstance().closePhoto(false, true);
        }
        int flags = intent.getFlags();
        String action = intent.getAction();
        int[] iArr3 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(iArr3[0], true);
        boolean z41 = action != null && action.equals("voip");
        if (!z3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z41) {
                this.passcodeSaveIntent = intent;
                this.passcodeSaveIntentIsNew = z;
                this.passcodeSaveIntentIsRestore = z2;
                return false;
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.sendingText = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.documentsMimeType = null;
        this.documentsUrisArray = null;
        this.exportingChatUri = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
        this.importingStickers = null;
        this.importingStickersEmoji = null;
        this.importingStickersSoftware = null;
        String str76 = "message_id";
        if ((flags & 1048576) == 0 && intent.getAction() != null && !z2) {
            str6 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j14 = intent.getExtras().getLong("dialogId", 0L);
                    if (j14 == 0) {
                        try {
                            String string = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                int i16 = 0;
                                while (i16 < size) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i16);
                                    int i17 = size;
                                    if (string.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j15 = extras.getLong("dialogId", 0L);
                                        try {
                                            str74 = extras.getString("hash", null);
                                            j14 = j15;
                                            break;
                                        } catch (Throwable th) {
                                            th = th;
                                            j14 = j15;
                                            FileLog.e(th);
                                            str74 = null;
                                            str75 = SharedConfig.directShareHash;
                                            if (str75 != null) {
                                            }
                                            j14 = 0;
                                            type2 = intent.getType();
                                            if (type2 == null) {
                                            }
                                            stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                                            if (stringExtra == null) {
                                                stringExtra = charSequenceExtra.toString();
                                            }
                                            String stringExtra2 = intent.getStringExtra("android.intent.extra.SUBJECT");
                                            if (TextUtils.isEmpty(stringExtra)) {
                                            }
                                            parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
                                            if (parcelableExtra == null) {
                                            }
                                        }
                                    } else {
                                        i16++;
                                        size = i17;
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        str74 = null;
                    } else {
                        str74 = intent.getExtras().getString("hash", null);
                    }
                    str75 = SharedConfig.directShareHash;
                    if (str75 != null) {
                    }
                }
                j14 = 0;
                type2 = intent.getType();
                if (type2 == null && type2.equals("text/x-vcard")) {
                    try {
                        Uri uri = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
                        if (uri != null) {
                            ArrayList<TLRPC$User> loadVCardFromStream = AndroidUtilities.loadVCardFromStream(uri, this.currentAccount, false, null, null);
                            this.contactsToSend = loadVCardFromStream;
                            if (loadVCardFromStream.size() > 5) {
                                this.contactsToSend = null;
                                ArrayList<Uri> arrayList3 = new ArrayList<>();
                                this.documentsUrisArray = arrayList3;
                                arrayList3.add(uri);
                                this.documentsMimeType = type2;
                            } else {
                                this.contactsToSendUri = uri;
                            }
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    z40 = true;
                    if (z40) {
                    }
                    str = " ";
                    intent8 = intent;
                    launchActivity = this;
                    j4 = j14;
                    i = -1;
                    i2 = 0;
                    j2 = 0;
                    i3 = 0;
                    i4 = -1;
                    j3 = 0;
                    str2 = null;
                    z5 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z6 = false;
                    z11 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    str3 = null;
                    str4 = null;
                    str5 = null;
                    i5 = 0;
                    c = 0;
                    iArr = iArr3;
                    z4 = false;
                } else {
                    stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                    if (stringExtra == null && (charSequenceExtra = intent.getCharSequenceExtra("android.intent.extra.TEXT")) != null) {
                        stringExtra = charSequenceExtra.toString();
                    }
                    String stringExtra22 = intent.getStringExtra("android.intent.extra.SUBJECT");
                    if (TextUtils.isEmpty(stringExtra)) {
                        if ((stringExtra.startsWith("http://") || stringExtra.startsWith("https://")) && !TextUtils.isEmpty(stringExtra22)) {
                            stringExtra = stringExtra22 + "\n" + stringExtra;
                        }
                        this.sendingText = stringExtra;
                    } else if (!TextUtils.isEmpty(stringExtra22)) {
                        this.sendingText = stringExtra22;
                    }
                    parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
                    if (parcelableExtra == null) {
                        boolean z42 = parcelableExtra instanceof Uri;
                        Uri uri2 = parcelableExtra;
                        if (!z42) {
                            uri2 = Uri.parse(parcelableExtra.toString());
                        }
                        Uri uri3 = (Uri) uri2;
                        boolean z43 = uri3 != null && AndroidUtilities.isInternalUri(uri3);
                        if (!z43 && uri3 != null) {
                            if ((type2 != null && type2.startsWith("image/")) || uri3.toString().toLowerCase().endsWith(".jpg")) {
                                if (this.photoPathsArray == null) {
                                    this.photoPathsArray = new ArrayList<>();
                                }
                                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                                sendingMediaInfo.uri = uri3;
                                this.photoPathsArray.add(sendingMediaInfo);
                            } else {
                                String uri4 = uri3.toString();
                                if (j14 == 0 && uri4 != null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("export path = " + uri4);
                                    }
                                    Set<String> set = MessagesController.getInstance(iArr3[0]).exportUri;
                                    String fixFileName = FileLoader.fixFileName(MediaController.getFileName(uri3));
                                    for (String str77 : set) {
                                        try {
                                            compile2 = Pattern.compile(str77);
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                        if (compile2.matcher(uri4).find() || compile2.matcher(fixFileName).find()) {
                                            this.exportingChatUri = uri3;
                                            break;
                                        }
                                    }
                                    if (this.exportingChatUri == null && uri4.startsWith("content://com.kakao.talk") && uri4.endsWith("KakaoTalkChats.txt")) {
                                        this.exportingChatUri = uri3;
                                    }
                                }
                                if (this.exportingChatUri == null) {
                                    String path = AndroidUtilities.getPath(uri3);
                                    if (!BuildVars.NO_SCOPED_STORAGE) {
                                        path = MediaController.copyFileToCache(uri3, "file");
                                    }
                                    if (path != null) {
                                        if (path.startsWith("file:")) {
                                            path = path.replace("file://", "");
                                        }
                                        if (type2 != null && type2.startsWith("video/")) {
                                            this.videoPath = path;
                                        } else {
                                            if (this.documentsPathsArray == null) {
                                                this.documentsPathsArray = new ArrayList<>();
                                                this.documentsOriginalPathsArray = new ArrayList<>();
                                            }
                                            this.documentsPathsArray.add(path);
                                            this.documentsOriginalPathsArray.add(uri3.toString());
                                        }
                                    } else {
                                        if (this.documentsUrisArray == null) {
                                            this.documentsUrisArray = new ArrayList<>();
                                        }
                                        this.documentsUrisArray.add(uri3);
                                        this.documentsMimeType = type2;
                                    }
                                }
                            }
                        }
                        z40 = z43;
                        if (z40) {
                            Toast.makeText(this, "Unsupported content", 0).show();
                        }
                        str = " ";
                        intent8 = intent;
                        launchActivity = this;
                        j4 = j14;
                        i = -1;
                        i2 = 0;
                        j2 = 0;
                        i3 = 0;
                        i4 = -1;
                        j3 = 0;
                        str2 = null;
                        z5 = false;
                        z10 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z6 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        i5 = 0;
                        c = 0;
                        iArr = iArr3;
                        z4 = false;
                    }
                }
                z40 = false;
                if (z40) {
                }
                str = " ";
                intent8 = intent;
                launchActivity = this;
                j4 = j14;
                i = -1;
                i2 = 0;
                j2 = 0;
                i3 = 0;
                i4 = -1;
                j3 = 0;
                str2 = null;
                z5 = false;
                z10 = false;
                z9 = false;
                z8 = false;
                z7 = false;
                z6 = false;
                z11 = false;
                z12 = false;
                z13 = false;
                z14 = false;
                z15 = false;
                str3 = null;
                str4 = null;
                str5 = null;
                i5 = 0;
                c = 0;
                iArr = iArr3;
                z4 = false;
            } else {
                if ("org.telegram.messenger.CREATE_STICKER_PACK".equals(intent.getAction())) {
                    try {
                        this.importingStickers = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                        this.importingStickersEmoji = intent.getStringArrayListExtra("STICKER_EMOJIS");
                        this.importingStickersSoftware = intent.getStringExtra("IMPORTER");
                    } catch (Throwable th3) {
                        FileLog.e(th3);
                        this.importingStickers = null;
                        this.importingStickersEmoji = null;
                        this.importingStickersSoftware = null;
                    }
                } else if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                    try {
                        parcelableArrayListExtra = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                        type = intent.getType();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                    if (parcelableArrayListExtra != null) {
                        int i18 = 0;
                        while (i18 < parcelableArrayListExtra.size()) {
                            Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i18);
                            boolean z44 = parcelable instanceof Uri;
                            Uri uri5 = parcelable;
                            if (!z44) {
                                uri5 = Uri.parse(parcelable.toString());
                            }
                            Uri uri6 = (Uri) uri5;
                            if (uri6 != null && AndroidUtilities.isInternalUri(uri6)) {
                                parcelableArrayListExtra.remove(i18);
                                i18--;
                            }
                            i18++;
                        }
                        if (parcelableArrayListExtra.isEmpty()) {
                            arrayList = null;
                            if (arrayList != null) {
                                if (type != null && type.startsWith("image/")) {
                                    for (int i19 = 0; i19 < arrayList.size(); i19++) {
                                        Parcelable parcelable2 = (Parcelable) arrayList.get(i19);
                                        boolean z45 = parcelable2 instanceof Uri;
                                        Uri uri7 = parcelable2;
                                        if (!z45) {
                                            uri7 = Uri.parse(parcelable2.toString());
                                        }
                                        Uri uri8 = (Uri) uri7;
                                        if (this.photoPathsArray == null) {
                                            this.photoPathsArray = new ArrayList<>();
                                        }
                                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo2 = new SendMessagesHelper.SendingMediaInfo();
                                        sendingMediaInfo2.uri = uri8;
                                        this.photoPathsArray.add(sendingMediaInfo2);
                                    }
                                } else {
                                    Set<String> set2 = MessagesController.getInstance(iArr3[0]).exportUri;
                                    int i20 = 0;
                                    while (i20 < arrayList.size()) {
                                        Object obj = (Parcelable) arrayList.get(i20);
                                        if (!(obj instanceof Uri)) {
                                            obj = Uri.parse(obj.toString());
                                        }
                                        Uri uri9 = (Uri) obj;
                                        String path2 = AndroidUtilities.getPath(uri9);
                                        String obj2 = obj.toString();
                                        String str78 = obj2 == null ? path2 : obj2;
                                        if (BuildVars.LOGS_ENABLED) {
                                            StringBuilder sb = new StringBuilder();
                                            arrayList2 = arrayList;
                                            sb.append("export path = ");
                                            sb.append(str78);
                                            FileLog.d(sb.toString());
                                        } else {
                                            arrayList2 = arrayList;
                                        }
                                        if (str78 != null && this.exportingChatUri == null) {
                                            String fixFileName2 = FileLoader.fixFileName(MediaController.getFileName(uri9));
                                            for (String str79 : set2) {
                                                try {
                                                    compile = Pattern.compile(str79);
                                                } catch (Exception e4) {
                                                    FileLog.e(e4);
                                                }
                                                if (compile.matcher(str78).find() || compile.matcher(fixFileName2).find()) {
                                                    this.exportingChatUri = uri9;
                                                    z27 = true;
                                                    break;
                                                }
                                            }
                                            z27 = false;
                                            if (!z27) {
                                                if (str78.startsWith("content://com.kakao.talk") && str78.endsWith("KakaoTalkChats.txt")) {
                                                    this.exportingChatUri = uri9;
                                                }
                                            }
                                            i20++;
                                            arrayList = arrayList2;
                                        }
                                        if (path2 != null) {
                                            if (path2.startsWith("file:")) {
                                                path2 = path2.replace("file://", "");
                                            }
                                            if (this.documentsPathsArray == null) {
                                                this.documentsPathsArray = new ArrayList<>();
                                                this.documentsOriginalPathsArray = new ArrayList<>();
                                            }
                                            this.documentsPathsArray.add(path2);
                                            this.documentsOriginalPathsArray.add(str78);
                                        } else {
                                            if (this.documentsUrisArray == null) {
                                                this.documentsUrisArray = new ArrayList<>();
                                            }
                                            this.documentsUrisArray.add(uri9);
                                            this.documentsMimeType = type;
                                        }
                                        i20++;
                                        arrayList = arrayList2;
                                    }
                                }
                                z26 = false;
                                if (z26) {
                                    Toast.makeText(this, "Unsupported content", 0).show();
                                }
                            }
                            z26 = true;
                            if (z26) {
                            }
                        }
                    }
                    arrayList = parcelableArrayListExtra;
                    if (arrayList != null) {
                    }
                    z26 = true;
                    if (z26) {
                    }
                } else if ("android.intent.action.VIEW".equals(intent.getAction())) {
                    Uri data = intent.getData();
                    if (data != null) {
                        String scheme = data.getScheme();
                        if (scheme != null) {
                            switch (scheme.hashCode()) {
                                case 3699:
                                    if (scheme.equals("tg")) {
                                        c6 = 0;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                case 3213448:
                                    if (scheme.equals("http")) {
                                        c6 = 1;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                case 99617003:
                                    if (scheme.equals("https")) {
                                        c6 = 2;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                default:
                                    c6 = 65535;
                                    break;
                            }
                            switch (c6) {
                                case 0:
                                    final String uri10 = data.toString();
                                    if (uri10.startsWith("tg:premium_offer") || uri10.startsWith("tg://premium_offer")) {
                                        j7 = 0;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$handleIntent$11(uri10);
                                            }
                                        });
                                        j8 = j7;
                                        j6 = j8;
                                        str12 = null;
                                        str13 = null;
                                        str14 = null;
                                        str15 = null;
                                        str16 = null;
                                        str6 = null;
                                        str17 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        str22 = null;
                                        z30 = false;
                                        str23 = null;
                                        str24 = null;
                                        str25 = null;
                                        str26 = null;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str30 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        str32 = null;
                                        str31 = null;
                                        str36 = null;
                                        str37 = null;
                                        i11 = -1;
                                        str38 = null;
                                        str39 = null;
                                        str40 = null;
                                        break;
                                    } else if (uri10.startsWith("tg:resolve") || uri10.startsWith("tg://resolve")) {
                                        str14 = null;
                                        j9 = 0;
                                        Uri parse = Uri.parse(uri10.replace("tg:resolve", "tg://telegram.org").replace("tg://resolve", "tg://telegram.org"));
                                        String queryParameter5 = parse.getQueryParameter("domain");
                                        if (queryParameter5 == null && (queryParameter5 = parse.getQueryParameter("phone")) != null && queryParameter5.startsWith("+")) {
                                            queryParameter5 = queryParameter5.substring(1);
                                        }
                                        if ("telegrampassport".equals(queryParameter5)) {
                                            ?? hashMap = new HashMap();
                                            String queryParameter6 = parse.getQueryParameter("scope");
                                            if (!TextUtils.isEmpty(queryParameter6) && queryParameter6.startsWith("{") && queryParameter6.endsWith("}")) {
                                                hashMap.put("nonce", parse.getQueryParameter("nonce"));
                                            } else {
                                                hashMap.put("payload", parse.getQueryParameter("payload"));
                                            }
                                            hashMap.put("bot_id", parse.getQueryParameter("bot_id"));
                                            hashMap.put("scope", queryParameter6);
                                            hashMap.put("public_key", parse.getQueryParameter("public_key"));
                                            hashMap.put("callback_url", parse.getQueryParameter("callback_url"));
                                            str28 = hashMap;
                                            str12 = null;
                                            str13 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str43 = null;
                                            str24 = null;
                                            str44 = null;
                                            str26 = null;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str27 = null;
                                            str30 = str27;
                                            str35 = str30;
                                            str34 = str35;
                                            str33 = str34;
                                            str32 = str33;
                                            str31 = str32;
                                            str36 = str31;
                                            str37 = str36;
                                            str38 = str37;
                                            str39 = str38;
                                            str40 = str39;
                                            j8 = j9;
                                            j6 = j8;
                                            z30 = false;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            str23 = str43;
                                            str25 = str44;
                                            i11 = -1;
                                            break;
                                        } else {
                                            String queryParameter7 = parse.getQueryParameter("start");
                                            String queryParameter8 = parse.getQueryParameter("startgroup");
                                            String queryParameter9 = parse.getQueryParameter("startchannel");
                                            String queryParameter10 = parse.getQueryParameter("admin");
                                            String queryParameter11 = parse.getQueryParameter("game");
                                            String queryParameter12 = parse.getQueryParameter("voicechat");
                                            String str80 = queryParameter5;
                                            String queryParameter13 = parse.getQueryParameter("livestream");
                                            String queryParameter14 = parse.getQueryParameter("startattach");
                                            String queryParameter15 = parse.getQueryParameter("choose");
                                            String queryParameter16 = parse.getQueryParameter("attach");
                                            Integer parseInt4 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                            Integer num10 = parseInt4.intValue() == 0 ? null : parseInt4;
                                            Integer parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("thread"));
                                            if (parseInt5.intValue() == 0) {
                                                parseInt5 = null;
                                            }
                                            if (parseInt5 == null) {
                                                parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("topic"));
                                                if (parseInt5.intValue() == 0) {
                                                    num = null;
                                                    parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                    if (parseInt.intValue() != 0) {
                                                        str29 = queryParameter11;
                                                        str36 = queryParameter12;
                                                        j8 = 0;
                                                        j6 = 0;
                                                        str14 = str80;
                                                        str37 = queryParameter13;
                                                        str38 = queryParameter14;
                                                        str40 = queryParameter15;
                                                        str39 = queryParameter16;
                                                        str25 = num;
                                                        str12 = null;
                                                        str13 = null;
                                                        str6 = null;
                                                        str22 = null;
                                                        str24 = null;
                                                        str26 = null;
                                                    } else {
                                                        str26 = parseInt;
                                                        str29 = queryParameter11;
                                                        str36 = queryParameter12;
                                                        j8 = 0;
                                                        j6 = 0;
                                                        str14 = str80;
                                                        str37 = queryParameter13;
                                                        str38 = queryParameter14;
                                                        str40 = queryParameter15;
                                                        str39 = queryParameter16;
                                                        str25 = num;
                                                        str12 = null;
                                                        str13 = null;
                                                        str6 = null;
                                                        str22 = null;
                                                        str24 = null;
                                                    }
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str27 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    i11 = -1;
                                                    str19 = queryParameter8;
                                                    str20 = queryParameter9;
                                                    str21 = queryParameter10;
                                                    str23 = num10;
                                                    str16 = null;
                                                    str17 = null;
                                                    z30 = false;
                                                    str18 = queryParameter7;
                                                    str15 = null;
                                                    break;
                                                }
                                            }
                                            num = parseInt5;
                                            parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                            if (parseInt.intValue() != 0) {
                                            }
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            i11 = -1;
                                            str19 = queryParameter8;
                                            str20 = queryParameter9;
                                            str21 = queryParameter10;
                                            str23 = num10;
                                            str16 = null;
                                            str17 = null;
                                            z30 = false;
                                            str18 = queryParameter7;
                                            str15 = null;
                                        }
                                    } else if (uri10.startsWith("tg:invoice") || uri10.startsWith("tg://invoice")) {
                                        str14 = null;
                                        j9 = 0;
                                        str32 = Uri.parse(uri10.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                        str12 = null;
                                        str13 = null;
                                        str15 = null;
                                        str16 = null;
                                        str6 = null;
                                        str17 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        str22 = null;
                                        str43 = null;
                                        str24 = null;
                                        str44 = null;
                                        str26 = null;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str30 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        str31 = null;
                                        str36 = str31;
                                        str37 = str36;
                                        str38 = str37;
                                        str39 = str38;
                                        str40 = str39;
                                        j8 = j9;
                                        j6 = j8;
                                        z30 = false;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str23 = str43;
                                        str25 = str44;
                                        i11 = -1;
                                    } else if (uri10.startsWith("tg:contact") || uri10.startsWith("tg://contact")) {
                                        str14 = null;
                                        j9 = 0;
                                        str22 = Uri.parse(uri10.replace("tg:contact", "tg://contact")).getQueryParameter("token");
                                        str12 = null;
                                        str13 = null;
                                        str15 = null;
                                        str16 = null;
                                        str6 = null;
                                        str17 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        str43 = null;
                                        str24 = null;
                                        str44 = null;
                                        str26 = null;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str30 = str27;
                                        str35 = str30;
                                        str34 = str35;
                                        str33 = str34;
                                        str32 = str33;
                                        str31 = str32;
                                        str36 = str31;
                                        str37 = str36;
                                        str38 = str37;
                                        str39 = str38;
                                        str40 = str39;
                                        j8 = j9;
                                        j6 = j8;
                                        z30 = false;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str23 = str43;
                                        str25 = str44;
                                        i11 = -1;
                                    } else if (uri10.startsWith("tg:privatepost") || uri10.startsWith("tg://privatepost")) {
                                        str14 = null;
                                        Uri parse2 = Uri.parse(uri10.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                        Integer parseInt6 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("post"));
                                        ?? parseLong = Utilities.parseLong(parse2.getQueryParameter("channel"));
                                        if (parseInt6.intValue() != 0) {
                                            j10 = 0;
                                            int i21 = (parseLong.longValue() > 0L ? 1 : (parseLong.longValue() == 0L ? 0 : -1));
                                            str45 = parseLong;
                                            break;
                                        } else {
                                            j10 = 0;
                                        }
                                        parseInt6 = null;
                                        str45 = null;
                                        Integer parseInt7 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("thread"));
                                        if (parseInt7.intValue() == 0) {
                                            parseInt7 = null;
                                        }
                                        if (parseInt7 == null) {
                                            parseInt7 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("topic"));
                                            if (parseInt7.intValue() == 0) {
                                                parseInt7 = null;
                                            }
                                        }
                                        ?? parseInt8 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("comment"));
                                        if (parseInt8.intValue() == 0) {
                                            str23 = parseInt6;
                                            str12 = null;
                                            str13 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str26 = null;
                                            str8 = null;
                                        } else {
                                            str26 = parseInt8;
                                            str23 = parseInt6;
                                            str12 = null;
                                            str13 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str8 = null;
                                        }
                                        str9 = str8;
                                        str10 = str9;
                                        str11 = str10;
                                        str29 = str11;
                                        str28 = str29;
                                        str27 = str28;
                                        str30 = str27;
                                        str35 = str30;
                                        str34 = str35;
                                        str33 = str34;
                                        str32 = str33;
                                        str31 = str32;
                                        str36 = str31;
                                        str37 = str36;
                                        str38 = str37;
                                        str39 = str38;
                                        str40 = str39;
                                        str24 = str45;
                                        str25 = parseInt7;
                                        j8 = j10;
                                        j6 = j8;
                                        z30 = false;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        i11 = -1;
                                        str15 = str40;
                                        str16 = str15;
                                        break;
                                    } else if (uri10.startsWith("tg:bg") || uri10.startsWith("tg://bg")) {
                                        Uri parse3 = Uri.parse(uri10.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                        ?? tLRPC$TL_wallPaper = new TLRPC$TL_wallPaper();
                                        tLRPC$TL_wallPaper.settings = new TLRPC$TL_wallPaperSettings();
                                        String queryParameter17 = parse3.getQueryParameter("slug");
                                        tLRPC$TL_wallPaper.slug = queryParameter17;
                                        if (queryParameter17 == null) {
                                            tLRPC$TL_wallPaper.slug = parse3.getQueryParameter("color");
                                        }
                                        String str81 = tLRPC$TL_wallPaper.slug;
                                        if (str81 != null && str81.length() == 6) {
                                            tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug, 16) | (-16777216);
                                            tLRPC$TL_wallPaper.slug = null;
                                            str14 = null;
                                        } else {
                                            String str82 = tLRPC$TL_wallPaper.slug;
                                            if (str82 != null && str82.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(6))) {
                                                tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(0, 6), 16) | (-16777216);
                                                tLRPC$TL_wallPaper.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(7, 13), 16) | (-16777216);
                                                if (tLRPC$TL_wallPaper.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(13))) {
                                                    tLRPC$TL_wallPaper.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(14, 20), 16) | (-16777216);
                                                }
                                                if (tLRPC$TL_wallPaper.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(20))) {
                                                    tLRPC$TL_wallPaper.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(21), 16) | (-16777216);
                                                }
                                                try {
                                                    String queryParameter18 = parse3.getQueryParameter("rotation");
                                                    if (!TextUtils.isEmpty(queryParameter18)) {
                                                        tLRPC$TL_wallPaper.settings.rotation = Utilities.parseInt((CharSequence) queryParameter18).intValue();
                                                    }
                                                } catch (Exception unused) {
                                                }
                                                str14 = null;
                                                try {
                                                    tLRPC$TL_wallPaper.slug = null;
                                                } catch (Exception unused2) {
                                                }
                                            }
                                            str14 = null;
                                            z32 = false;
                                            if (!z32) {
                                                String queryParameter19 = parse3.getQueryParameter("mode");
                                                if (queryParameter19 != null && (split = queryParameter19.toLowerCase().split(" ")) != null && split.length > 0) {
                                                    for (int i22 = 0; i22 < split.length; i22++) {
                                                        if ("blur".equals(split[i22])) {
                                                            tLRPC$TL_wallPaper.settings.blur = true;
                                                        } else if ("motion".equals(split[i22])) {
                                                            tLRPC$TL_wallPaper.settings.motion = true;
                                                        }
                                                    }
                                                }
                                                tLRPC$TL_wallPaper.settings.intensity = Utilities.parseInt((CharSequence) parse3.getQueryParameter("intensity")).intValue();
                                                try {
                                                    queryParameter2 = parse3.getQueryParameter("bg_color");
                                                } catch (Exception unused3) {
                                                }
                                                try {
                                                    if (!TextUtils.isEmpty(queryParameter2)) {
                                                        try {
                                                            tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                                            if (queryParameter2.length() >= 13) {
                                                                tLRPC$TL_wallPaper.settings.second_background_color = Integer.parseInt(queryParameter2.substring(8, 13), 16) | (-16777216);
                                                                if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                                    tLRPC$TL_wallPaper.settings.third_background_color = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                                                }
                                                                if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                                    tLRPC$TL_wallPaper.settings.fourth_background_color = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                                                }
                                                            }
                                                        } catch (Exception unused4) {
                                                        }
                                                        queryParameter = parse3.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter)) {
                                                            tLRPC$TL_wallPaper.settings.rotation = Utilities.parseInt((CharSequence) queryParameter).intValue();
                                                        }
                                                    }
                                                    queryParameter = parse3.getQueryParameter("rotation");
                                                    if (!TextUtils.isEmpty(queryParameter)) {
                                                    }
                                                } catch (Exception unused5) {
                                                }
                                            }
                                            str33 = tLRPC$TL_wallPaper;
                                            str12 = str14;
                                            str13 = str12;
                                            str15 = str13;
                                            str16 = str15;
                                            str6 = str16;
                                            str17 = str6;
                                            str18 = str17;
                                            str19 = str18;
                                            str20 = str19;
                                            str21 = str20;
                                            str22 = str21;
                                            String str83 = str22;
                                            str24 = str83;
                                            String str84 = str24;
                                            str26 = str84;
                                            str8 = str26;
                                            str9 = str8;
                                            str10 = str9;
                                            str11 = str10;
                                            str29 = str11;
                                            str28 = str29;
                                            str27 = str28;
                                            str30 = str27;
                                            str35 = str30;
                                            str34 = str35;
                                            str32 = str34;
                                            str31 = str32;
                                            str36 = str31;
                                            str37 = str36;
                                            str38 = str37;
                                            str39 = str38;
                                            str40 = str39;
                                            z30 = false;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str23 = str83;
                                            str25 = str84;
                                            i11 = -1;
                                        }
                                        z32 = true;
                                        if (!z32) {
                                        }
                                        str33 = tLRPC$TL_wallPaper;
                                        str12 = str14;
                                        str13 = str12;
                                        str15 = str13;
                                        str16 = str15;
                                        str6 = str16;
                                        str17 = str6;
                                        str18 = str17;
                                        str19 = str18;
                                        str20 = str19;
                                        str21 = str20;
                                        str22 = str21;
                                        String str832 = str22;
                                        str24 = str832;
                                        String str842 = str24;
                                        str26 = str842;
                                        str8 = str26;
                                        str9 = str8;
                                        str10 = str9;
                                        str11 = str10;
                                        str29 = str11;
                                        str28 = str29;
                                        str27 = str28;
                                        str30 = str27;
                                        str35 = str30;
                                        str34 = str35;
                                        str32 = str34;
                                        str31 = str32;
                                        str36 = str31;
                                        str37 = str36;
                                        str38 = str37;
                                        str39 = str38;
                                        str40 = str39;
                                        z30 = false;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        j8 = 0;
                                        j6 = 0;
                                        str23 = str832;
                                        str25 = str842;
                                        i11 = -1;
                                    } else {
                                        if (uri10.startsWith("tg:join") || uri10.startsWith("tg://join")) {
                                            str15 = Uri.parse(uri10.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                        } else if (uri10.startsWith("tg:addstickers") || uri10.startsWith("tg://addstickers")) {
                                            str16 = Uri.parse(uri10.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:addemoji") || uri10.startsWith("tg://addemoji")) {
                                            str17 = Uri.parse(uri10.replace("tg:addemoji", "tg://telegram.org").replace("tg://addemoji", "tg://telegram.org")).getQueryParameter("set");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:msg") || uri10.startsWith("tg://msg") || uri10.startsWith("tg://share") || uri10.startsWith("tg:share")) {
                                            Uri parse4 = Uri.parse(uri10.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                            String queryParameter20 = parse4.getQueryParameter("url");
                                            str6 = queryParameter20 != null ? queryParameter20 : "";
                                            if (parse4.getQueryParameter("text") != null) {
                                                if (str6.length() > 0) {
                                                    str6 = str6 + "\n";
                                                    z33 = true;
                                                } else {
                                                    z33 = false;
                                                }
                                                str6 = str6 + parse4.getQueryParameter("text");
                                            } else {
                                                z33 = false;
                                            }
                                            if (str6.length() > 16384) {
                                                i12 = 0;
                                                str6 = str6.substring(0, 16384);
                                            } else {
                                                i12 = 0;
                                            }
                                            while (str6.endsWith("\n")) {
                                                str6 = str6.substring(i12, str6.length() - 1);
                                            }
                                            z30 = z33;
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:confirmphone") || uri10.startsWith("tg://confirmphone")) {
                                            Uri parse5 = Uri.parse(uri10.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                            String queryParameter21 = parse5.getQueryParameter("phone");
                                            str13 = parse5.getQueryParameter("hash");
                                            str12 = queryParameter21;
                                            str14 = null;
                                            str15 = null;
                                        } else if (uri10.startsWith("tg:login") || uri10.startsWith("tg://login")) {
                                            Uri parse6 = Uri.parse(uri10.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                            String queryParameter22 = parse6.getQueryParameter("token");
                                            int intValue = Utilities.parseInt((CharSequence) parse6.getQueryParameter("code")).intValue();
                                            str35 = intValue != 0 ? "" + intValue : null;
                                            str34 = queryParameter22;
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:openmessage") || uri10.startsWith("tg://openmessage")) {
                                            Uri parse7 = Uri.parse(uri10.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                            String queryParameter23 = parse7.getQueryParameter("user_id");
                                            String queryParameter24 = parse7.getQueryParameter("chat_id");
                                            String queryParameter25 = parse7.getQueryParameter("message_id");
                                            if (queryParameter23 != null) {
                                                j11 = Long.parseLong(queryParameter23);
                                                j12 = 0;
                                                if (queryParameter25 != null) {
                                                    try {
                                                        parseInt2 = Integer.parseInt(queryParameter25);
                                                    } catch (NumberFormatException unused6) {
                                                    }
                                                    i10 = parseInt2;
                                                    j8 = j11;
                                                    j6 = j12;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str6 = null;
                                                    str17 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    str22 = null;
                                                    z30 = false;
                                                    str23 = null;
                                                    str24 = null;
                                                    str25 = null;
                                                    str26 = null;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str29 = null;
                                                    str28 = null;
                                                    str27 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str36 = null;
                                                    str37 = null;
                                                    i11 = -1;
                                                    str38 = null;
                                                    str39 = null;
                                                    str40 = null;
                                                }
                                                parseInt2 = 0;
                                                i10 = parseInt2;
                                                j8 = j11;
                                                j6 = j12;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str29 = null;
                                                str28 = null;
                                                str27 = null;
                                                str30 = null;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str36 = null;
                                                str37 = null;
                                                i11 = -1;
                                                str38 = null;
                                                str39 = null;
                                                str40 = null;
                                            } else {
                                                if (queryParameter24 != null) {
                                                    j12 = Long.parseLong(queryParameter24);
                                                    j11 = 0;
                                                    if (queryParameter25 != null) {
                                                    }
                                                    parseInt2 = 0;
                                                    i10 = parseInt2;
                                                    j8 = j11;
                                                    j6 = j12;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str6 = null;
                                                    str17 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    str22 = null;
                                                    z30 = false;
                                                    str23 = null;
                                                    str24 = null;
                                                    str25 = null;
                                                    str26 = null;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str29 = null;
                                                    str28 = null;
                                                    str27 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str36 = null;
                                                    str37 = null;
                                                    i11 = -1;
                                                    str38 = null;
                                                    str39 = null;
                                                    str40 = null;
                                                }
                                                j11 = 0;
                                                j12 = 0;
                                                if (queryParameter25 != null) {
                                                }
                                                parseInt2 = 0;
                                                i10 = parseInt2;
                                                j8 = j11;
                                                j6 = j12;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str29 = null;
                                                str28 = null;
                                                str27 = null;
                                                str30 = null;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str36 = null;
                                                str37 = null;
                                                i11 = -1;
                                                str38 = null;
                                                str39 = null;
                                                str40 = null;
                                            }
                                        } else if (uri10.startsWith("tg:passport") || uri10.startsWith("tg://passport") || uri10.startsWith("tg:secureid")) {
                                            Uri parse8 = Uri.parse(uri10.replace("tg:passport", "tg://telegram.org").replace("tg://passport", "tg://telegram.org").replace("tg:secureid", "tg://telegram.org"));
                                            ?? hashMap2 = new HashMap();
                                            String queryParameter26 = parse8.getQueryParameter("scope");
                                            if (!TextUtils.isEmpty(queryParameter26) && queryParameter26.startsWith("{") && queryParameter26.endsWith("}")) {
                                                hashMap2.put("nonce", parse8.getQueryParameter("nonce"));
                                            } else {
                                                hashMap2.put("payload", parse8.getQueryParameter("payload"));
                                            }
                                            hashMap2.put("bot_id", parse8.getQueryParameter("bot_id"));
                                            hashMap2.put("scope", queryParameter26);
                                            hashMap2.put("public_key", parse8.getQueryParameter("public_key"));
                                            hashMap2.put("callback_url", parse8.getQueryParameter("callback_url"));
                                            str28 = hashMap2;
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:setlanguage") || uri10.startsWith("tg://setlanguage")) {
                                            str27 = Uri.parse(uri10.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:addtheme") || uri10.startsWith("tg://addtheme")) {
                                            str31 = Uri.parse(uri10.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:settings") || uri10.startsWith("tg://settings")) {
                                            if (uri10.contains("themes")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 2;
                                            } else if (uri10.contains("devices")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 3;
                                            } else if (uri10.contains("folders")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 4;
                                            } else if (uri10.contains("change_number")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 5;
                                            } else {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 1;
                                            }
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:search") || uri10.startsWith("tg://search")) {
                                            String queryParameter27 = Uri.parse(uri10.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                            str11 = queryParameter27 != null ? queryParameter27.trim() : "";
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else if (uri10.startsWith("tg:calllog") || uri10.startsWith("tg://calllog")) {
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str6 = null;
                                            str17 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            z30 = false;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            str26 = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = true;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        } else {
                                            if (uri10.startsWith("tg:call") || uri10.startsWith("tg://call")) {
                                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                                    if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                        String queryParameter28 = data.getQueryParameter("format");
                                                        String queryParameter29 = data.getQueryParameter("name");
                                                        String queryParameter30 = data.getQueryParameter("phone");
                                                        List<TLRPC$TL_contact> findContacts = findContacts(queryParameter29, queryParameter30, false);
                                                        if (!findContacts.isEmpty() || queryParameter30 == null) {
                                                            long j16 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                            if (j16 != 0) {
                                                                str6 = null;
                                                            } else if (queryParameter29 != null) {
                                                                str6 = queryParameter29;
                                                            }
                                                            boolean equalsIgnoreCase = MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(queryParameter28);
                                                            j13 = j16;
                                                            str46 = str6;
                                                            str47 = null;
                                                            str48 = null;
                                                            z34 = equalsIgnoreCase;
                                                            z35 = !equalsIgnoreCase;
                                                            z36 = true;
                                                            z37 = false;
                                                        } else {
                                                            str48 = queryParameter30;
                                                            str47 = queryParameter29;
                                                            z36 = false;
                                                            z37 = true;
                                                            j13 = 0;
                                                            z35 = false;
                                                            z34 = false;
                                                            str46 = null;
                                                        }
                                                    } else {
                                                        final Intent intent9 = new Intent(intent);
                                                        intent9.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                        intent9.putExtra("extra_force_call", true);
                                                        ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
                                                            @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                            public final void onResult(boolean z46) {
                                                                LaunchActivity.this.lambda$handleIntent$12(intent9, z46);
                                                            }
                                                        }, 1000L);
                                                        z36 = false;
                                                        z37 = false;
                                                        j13 = 0;
                                                        z35 = false;
                                                        z34 = false;
                                                        str46 = null;
                                                        str47 = null;
                                                        str48 = null;
                                                    }
                                                    z12 = z36;
                                                    z14 = z37;
                                                    j8 = j13;
                                                    z28 = z35;
                                                    z29 = z34;
                                                    str8 = str46;
                                                    str9 = str47;
                                                    str10 = str48;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str6 = null;
                                                    str17 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    str22 = null;
                                                    z30 = false;
                                                    str23 = null;
                                                    str24 = null;
                                                    str25 = null;
                                                    str26 = null;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z13 = false;
                                                    z15 = false;
                                                    j6 = 0;
                                                }
                                            } else if (uri10.startsWith("tg:scanqr") || uri10.startsWith("tg://scanqr")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = true;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                            } else if (uri10.startsWith("tg:addcontact") || uri10.startsWith("tg://addcontact")) {
                                                Uri parse9 = Uri.parse(uri10.replace("tg:addcontact", "tg://telegram.org").replace("tg://addcontact", "tg://telegram.org"));
                                                String queryParameter31 = parse9.getQueryParameter("name");
                                                str10 = parse9.getQueryParameter("phone");
                                                str9 = queryParameter31;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z13 = true;
                                                z14 = false;
                                                z15 = false;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                            } else {
                                                String replace = uri10.replace("tg://", "").replace("tg:", "");
                                                int indexOf = replace.indexOf(63);
                                                if (indexOf >= 0) {
                                                    replace = replace.substring(0, indexOf);
                                                }
                                                str30 = replace;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str6 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                z30 = false;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                str26 = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str29 = null;
                                                str28 = null;
                                                str27 = null;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str36 = null;
                                                str37 = null;
                                                i11 = -1;
                                                str38 = null;
                                                str39 = null;
                                                str40 = null;
                                            }
                                            str11 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str30 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str36 = null;
                                            str37 = null;
                                            i11 = -1;
                                            str38 = null;
                                            str39 = null;
                                            str40 = null;
                                        }
                                        str16 = null;
                                        str6 = null;
                                        str17 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        str22 = null;
                                        z30 = false;
                                        str23 = null;
                                        str24 = null;
                                        str25 = null;
                                        str26 = null;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        j8 = 0;
                                        j6 = 0;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str30 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        str32 = null;
                                        str31 = null;
                                        str36 = null;
                                        str37 = null;
                                        i11 = -1;
                                        str38 = null;
                                        str39 = null;
                                        str40 = null;
                                    }
                                    break;
                                case 1:
                                case 2:
                                    String lowerCase = data.getHost().toLowerCase();
                                    Matcher matcher = PREFIX_T_ME_PATTERN.matcher(lowerCase);
                                    boolean find = matcher.find();
                                    if (lowerCase.equals("telegram.me") || lowerCase.equals("t.me") || lowerCase.equals("telegram.dog") || find) {
                                        if (find) {
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("https://t.me/");
                                            sb2.append(matcher.group(1));
                                            sb2.append(TextUtils.isEmpty(data.getPath()) ? "" : data.getPath());
                                            sb2.append(TextUtils.isEmpty(data.getQuery()) ? "" : "?" + data.getQuery());
                                            data = Uri.parse(sb2.toString());
                                        }
                                        String path3 = data.getPath();
                                        if (path3 != null && path3.length() > 1) {
                                            String substring2 = path3.substring(1);
                                            if (substring2.startsWith("$")) {
                                                substring = substring2.substring(1);
                                            } else if (substring2.startsWith("invoice/")) {
                                                substring = substring2.substring(substring2.indexOf(47) + 1);
                                            } else if (substring2.startsWith("bg/")) {
                                                ?? tLRPC$TL_wallPaper2 = new TLRPC$TL_wallPaper();
                                                tLRPC$TL_wallPaper2.settings = new TLRPC$TL_wallPaperSettings();
                                                String replace2 = substring2.replace("bg/", "");
                                                tLRPC$TL_wallPaper2.slug = replace2;
                                                if (replace2 != null && replace2.length() == 6) {
                                                    tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug, 16) | (-16777216);
                                                    tLRPC$TL_wallPaper2.slug = null;
                                                } else {
                                                    String str85 = tLRPC$TL_wallPaper2.slug;
                                                    if (str85 != null && str85.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(6))) {
                                                        tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(0, 6), 16) | (-16777216);
                                                        tLRPC$TL_wallPaper2.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(7, 13), 16) | (-16777216);
                                                        if (tLRPC$TL_wallPaper2.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(13))) {
                                                            tLRPC$TL_wallPaper2.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(14, 20), 16) | (-16777216);
                                                        }
                                                        if (tLRPC$TL_wallPaper2.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(20))) {
                                                            tLRPC$TL_wallPaper2.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(21), 16) | (-16777216);
                                                        }
                                                        try {
                                                            String queryParameter32 = data.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter32)) {
                                                                tLRPC$TL_wallPaper2.settings.rotation = Utilities.parseInt((CharSequence) queryParameter32).intValue();
                                                            }
                                                        } catch (Exception unused7) {
                                                        }
                                                        tLRPC$TL_wallPaper2.slug = null;
                                                    }
                                                    z39 = false;
                                                    if (!z39) {
                                                        String queryParameter33 = data.getQueryParameter("mode");
                                                        if (queryParameter33 != null && (split2 = queryParameter33.toLowerCase().split(" ")) != null && split2.length > 0) {
                                                            for (int i23 = 0; i23 < split2.length; i23++) {
                                                                if ("blur".equals(split2[i23])) {
                                                                    tLRPC$TL_wallPaper2.settings.blur = true;
                                                                } else if ("motion".equals(split2[i23])) {
                                                                    tLRPC$TL_wallPaper2.settings.motion = true;
                                                                }
                                                            }
                                                        }
                                                        String queryParameter34 = data.getQueryParameter("intensity");
                                                        if (!TextUtils.isEmpty(queryParameter34)) {
                                                            tLRPC$TL_wallPaper2.settings.intensity = Utilities.parseInt((CharSequence) queryParameter34).intValue();
                                                        } else {
                                                            tLRPC$TL_wallPaper2.settings.intensity = 50;
                                                        }
                                                        try {
                                                            queryParameter4 = data.getQueryParameter("bg_color");
                                                        } catch (Exception unused8) {
                                                        }
                                                        if (!TextUtils.isEmpty(queryParameter4)) {
                                                            tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(queryParameter4.substring(0, 6), 16) | (-16777216);
                                                            if (queryParameter4.length() >= 13) {
                                                                tLRPC$TL_wallPaper2.settings.second_background_color = Integer.parseInt(queryParameter4.substring(7, 13), 16) | (-16777216);
                                                                if (queryParameter4.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter4.charAt(13))) {
                                                                    tLRPC$TL_wallPaper2.settings.third_background_color = Integer.parseInt(queryParameter4.substring(14, 20), 16) | (-16777216);
                                                                }
                                                                if (queryParameter4.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter4.charAt(20))) {
                                                                    tLRPC$TL_wallPaper2.settings.fourth_background_color = Integer.parseInt(queryParameter4.substring(21), 16) | (-16777216);
                                                                }
                                                            }
                                                            try {
                                                                queryParameter3 = data.getQueryParameter("rotation");
                                                                if (!TextUtils.isEmpty(queryParameter3)) {
                                                                    tLRPC$TL_wallPaper2.settings.rotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                                                                }
                                                            } catch (Exception unused9) {
                                                            }
                                                        } else {
                                                            try {
                                                                tLRPC$TL_wallPaper2.settings.background_color = -1;
                                                            } catch (Exception unused10) {
                                                            }
                                                            queryParameter3 = data.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter3)) {
                                                            }
                                                        }
                                                    }
                                                    str64 = tLRPC$TL_wallPaper2;
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                    break;
                                                }
                                                z39 = true;
                                                if (!z39) {
                                                }
                                                str64 = tLRPC$TL_wallPaper2;
                                                str49 = null;
                                                z38 = false;
                                                str16 = null;
                                                i13 = -1;
                                                str17 = null;
                                                str60 = null;
                                                str59 = null;
                                                str58 = null;
                                                str57 = null;
                                                str21 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str61 = null;
                                                str62 = null;
                                                str63 = null;
                                                str65 = null;
                                                num3 = null;
                                                str70 = null;
                                                r43 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                num2 = r43;
                                                str15 = str49;
                                                i11 = i13;
                                                str18 = str60;
                                                str6 = str59;
                                                str14 = str58;
                                                str20 = str57;
                                                str12 = str56;
                                                str29 = str55;
                                                str36 = str54;
                                                str37 = str53;
                                                str27 = str51;
                                                str31 = str50;
                                                str19 = str61;
                                                str35 = str62;
                                                str22 = str63;
                                                str33 = str64;
                                                str32 = str65;
                                                str23 = num3;
                                                str25 = num2;
                                                str26 = str69;
                                                str38 = str68;
                                                str39 = str67;
                                                str40 = str66;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z14 = false;
                                                z15 = false;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str28 = null;
                                                str30 = null;
                                                str34 = null;
                                                z30 = z38;
                                                str13 = str52;
                                                str24 = str70;
                                                z13 = false;
                                            } else if (substring2.startsWith("login/")) {
                                                int intValue2 = Utilities.parseInt((CharSequence) substring2.replace("login/", "")).intValue();
                                                str62 = intValue2 != 0 ? "" + intValue2 : null;
                                                str49 = null;
                                                z38 = false;
                                                str16 = null;
                                                i13 = -1;
                                                str17 = null;
                                                str60 = null;
                                                str59 = null;
                                                str58 = null;
                                                str57 = null;
                                                str21 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str61 = null;
                                                str63 = null;
                                                str64 = null;
                                                str65 = null;
                                                num3 = null;
                                                str70 = null;
                                                r43 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                num2 = r43;
                                                str15 = str49;
                                                i11 = i13;
                                                str18 = str60;
                                                str6 = str59;
                                                str14 = str58;
                                                str20 = str57;
                                                str12 = str56;
                                                str29 = str55;
                                                str36 = str54;
                                                str37 = str53;
                                                str27 = str51;
                                                str31 = str50;
                                                str19 = str61;
                                                str35 = str62;
                                                str22 = str63;
                                                str33 = str64;
                                                str32 = str65;
                                                str23 = num3;
                                                str25 = num2;
                                                str26 = str69;
                                                str38 = str68;
                                                str39 = str67;
                                                str40 = str66;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z14 = false;
                                                z15 = false;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str28 = null;
                                                str30 = null;
                                                str34 = null;
                                                z30 = z38;
                                                str13 = str52;
                                                str24 = str70;
                                                z13 = false;
                                            } else {
                                                if (substring2.startsWith("joinchat/")) {
                                                    str49 = substring2.replace("joinchat/", "");
                                                } else if (substring2.startsWith("+")) {
                                                    str49 = substring2.replace("+", "");
                                                    if (AndroidUtilities.isNumeric(str49)) {
                                                        str58 = str49;
                                                        str49 = null;
                                                        z38 = false;
                                                        str16 = null;
                                                        i13 = -1;
                                                        str17 = null;
                                                        str60 = null;
                                                        str59 = null;
                                                        str57 = null;
                                                        str21 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        str50 = null;
                                                        str61 = null;
                                                        str62 = null;
                                                        str63 = null;
                                                        str64 = null;
                                                        str65 = null;
                                                        num3 = null;
                                                        str70 = null;
                                                        r43 = null;
                                                        str69 = null;
                                                        str68 = null;
                                                        str67 = null;
                                                        str66 = null;
                                                        num2 = r43;
                                                        str15 = str49;
                                                        i11 = i13;
                                                        str18 = str60;
                                                        str6 = str59;
                                                        str14 = str58;
                                                        str20 = str57;
                                                        str12 = str56;
                                                        str29 = str55;
                                                        str36 = str54;
                                                        str37 = str53;
                                                        str27 = str51;
                                                        str31 = str50;
                                                        str19 = str61;
                                                        str35 = str62;
                                                        str22 = str63;
                                                        str33 = str64;
                                                        str32 = str65;
                                                        str23 = num3;
                                                        str25 = num2;
                                                        str26 = str69;
                                                        str38 = str68;
                                                        str39 = str67;
                                                        str40 = str66;
                                                        i10 = 0;
                                                        c5 = 0;
                                                        z11 = false;
                                                        z28 = false;
                                                        z29 = false;
                                                        z12 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        j8 = 0;
                                                        j6 = 0;
                                                        str8 = null;
                                                        str9 = null;
                                                        str10 = null;
                                                        str11 = null;
                                                        str28 = null;
                                                        str30 = null;
                                                        str34 = null;
                                                        z30 = z38;
                                                        str13 = str52;
                                                        str24 = str70;
                                                        z13 = false;
                                                    }
                                                } else if (substring2.startsWith("addstickers/")) {
                                                    str16 = substring2.replace("addstickers/", "");
                                                    str49 = null;
                                                    z38 = false;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("addemoji/")) {
                                                    str17 = substring2.replace("addemoji/", "");
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("msg/") || substring2.startsWith("share/")) {
                                                    String queryParameter35 = data.getQueryParameter("url");
                                                    str6 = queryParameter35 != null ? queryParameter35 : "";
                                                    if (data.getQueryParameter("text") != null) {
                                                        if (str6.length() > 0) {
                                                            str6 = str6 + "\n";
                                                            z38 = true;
                                                        } else {
                                                            z38 = false;
                                                        }
                                                        str6 = str6 + data.getQueryParameter("text");
                                                    } else {
                                                        z38 = false;
                                                    }
                                                    if (str6.length() > 16384) {
                                                        i14 = 0;
                                                        str6 = str6.substring(0, 16384);
                                                    } else {
                                                        i14 = 0;
                                                    }
                                                    while (str6.endsWith("\n")) {
                                                        str6 = str6.substring(i14, str6.length() - 1);
                                                    }
                                                    str59 = str6;
                                                    str49 = null;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("confirmphone")) {
                                                    String queryParameter36 = data.getQueryParameter("phone");
                                                    str52 = data.getQueryParameter("hash");
                                                    str56 = queryParameter36;
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("setlanguage/")) {
                                                    str51 = substring2.substring(12);
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("addtheme/")) {
                                                    str50 = substring2.substring(9);
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("c/")) {
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() >= 3) {
                                                        ?? parseLong2 = Utilities.parseLong(pathSegments.get(1));
                                                        Integer parseInt9 = Utilities.parseInt((CharSequence) pathSegments.get(2));
                                                        if (parseInt9.intValue() != 0) {
                                                            int i24 = (parseLong2.longValue() > 0L ? 1 : (parseLong2.longValue() == 0L ? 0 : -1));
                                                            str73 = parseLong2;
                                                            break;
                                                        }
                                                        str73 = null;
                                                        parseInt9 = null;
                                                        num9 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (num9.intValue() == 0) {
                                                            num9 = null;
                                                        }
                                                        if (num9 == null) {
                                                            Integer parseInt10 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                            num9 = parseInt10.intValue() == 0 ? null : parseInt10;
                                                        }
                                                        if (num9 != null || parseInt9 == null || pathSegments.size() < 4) {
                                                            num8 = parseInt9;
                                                            str72 = str73;
                                                        } else {
                                                            num8 = Utilities.parseInt((CharSequence) pathSegments.get(3));
                                                            num9 = parseInt9;
                                                            str72 = str73;
                                                        }
                                                    } else {
                                                        num8 = null;
                                                        str72 = null;
                                                        num9 = null;
                                                    }
                                                    num3 = num8;
                                                    str70 = str72;
                                                    r43 = num9;
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.startsWith("contact/")) {
                                                    str63 = substring2.substring(8);
                                                    str49 = null;
                                                    z38 = false;
                                                    str16 = null;
                                                    i13 = -1;
                                                    str17 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str21 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str61 = null;
                                                    str62 = null;
                                                    str64 = null;
                                                    str65 = null;
                                                    num3 = null;
                                                    str70 = null;
                                                    r43 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    num2 = r43;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                } else if (substring2.length() >= 1) {
                                                    ArrayList arrayList4 = new ArrayList(data.getPathSegments());
                                                    if (arrayList4.size() > 0) {
                                                        i15 = 0;
                                                        if (((String) arrayList4.get(0)).equals("s")) {
                                                            arrayList4.remove(0);
                                                        }
                                                    } else {
                                                        i15 = 0;
                                                    }
                                                    if (arrayList4.size() > 0) {
                                                        str71 = (String) arrayList4.get(i15);
                                                        if (arrayList4.size() > 1) {
                                                            num4 = Utilities.parseInt((CharSequence) arrayList4.get(1));
                                                            break;
                                                        }
                                                    } else {
                                                        str71 = null;
                                                    }
                                                    num4 = null;
                                                    i13 = num4 != null ? getTimestampFromLink(data) : -1;
                                                    String queryParameter37 = data.getQueryParameter("start");
                                                    String queryParameter38 = data.getQueryParameter("startgroup");
                                                    str58 = str71;
                                                    str57 = data.getQueryParameter("startchannel");
                                                    str21 = data.getQueryParameter("admin");
                                                    String queryParameter39 = data.getQueryParameter("game");
                                                    String queryParameter40 = data.getQueryParameter("voicechat");
                                                    String queryParameter41 = data.getQueryParameter("livestream");
                                                    String queryParameter42 = data.getQueryParameter("startattach");
                                                    String queryParameter43 = data.getQueryParameter("choose");
                                                    String queryParameter44 = data.getQueryParameter("attach");
                                                    Integer parseInt11 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                    if (parseInt11.intValue() == 0) {
                                                        parseInt11 = null;
                                                    }
                                                    if (parseInt11 == null) {
                                                        parseInt11 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                        if (parseInt11.intValue() == 0) {
                                                            parseInt11 = null;
                                                        }
                                                    }
                                                    if (parseInt11 != null || num4 == null) {
                                                        num5 = parseInt11;
                                                        str61 = queryParameter38;
                                                    } else {
                                                        num5 = parseInt11;
                                                        str61 = queryParameter38;
                                                        if (arrayList4.size() >= 3) {
                                                            num6 = Utilities.parseInt((CharSequence) arrayList4.get(2));
                                                            num5 = num4;
                                                            parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                            if (parseInt3.intValue() != 0) {
                                                                num3 = num6;
                                                                str60 = queryParameter37;
                                                                str68 = queryParameter42;
                                                                str66 = queryParameter43;
                                                                str67 = queryParameter44;
                                                                num7 = num5;
                                                                str49 = null;
                                                                z38 = false;
                                                                str16 = null;
                                                                str17 = null;
                                                                str59 = null;
                                                                str52 = null;
                                                                str51 = null;
                                                                str50 = null;
                                                                str62 = null;
                                                                str63 = null;
                                                                str64 = null;
                                                                str65 = null;
                                                                str70 = null;
                                                                str69 = null;
                                                            } else {
                                                                str69 = parseInt3;
                                                                num3 = num6;
                                                                str60 = queryParameter37;
                                                                str68 = queryParameter42;
                                                                str66 = queryParameter43;
                                                                str67 = queryParameter44;
                                                                num7 = num5;
                                                                str49 = null;
                                                                z38 = false;
                                                                str16 = null;
                                                                str17 = null;
                                                                str59 = null;
                                                                str52 = null;
                                                                str51 = null;
                                                                str50 = null;
                                                                str62 = null;
                                                                str63 = null;
                                                                str64 = null;
                                                                str65 = null;
                                                                str70 = null;
                                                            }
                                                            str53 = queryParameter41;
                                                            str54 = queryParameter40;
                                                            str55 = queryParameter39;
                                                            str56 = null;
                                                            num2 = num7;
                                                            str15 = str49;
                                                            i11 = i13;
                                                            str18 = str60;
                                                            str6 = str59;
                                                            str14 = str58;
                                                            str20 = str57;
                                                            str12 = str56;
                                                            str29 = str55;
                                                            str36 = str54;
                                                            str37 = str53;
                                                            str27 = str51;
                                                            str31 = str50;
                                                            str19 = str61;
                                                            str35 = str62;
                                                            str22 = str63;
                                                            str33 = str64;
                                                            str32 = str65;
                                                            str23 = num3;
                                                            str25 = num2;
                                                            str26 = str69;
                                                            str38 = str68;
                                                            str39 = str67;
                                                            str40 = str66;
                                                            i10 = 0;
                                                            c5 = 0;
                                                            z11 = false;
                                                            z28 = false;
                                                            z29 = false;
                                                            z12 = false;
                                                            z14 = false;
                                                            z15 = false;
                                                            j8 = 0;
                                                            j6 = 0;
                                                            str8 = null;
                                                            str9 = null;
                                                            str10 = null;
                                                            str11 = null;
                                                            str28 = null;
                                                            str30 = null;
                                                            str34 = null;
                                                            z30 = z38;
                                                            str13 = str52;
                                                            str24 = str70;
                                                            z13 = false;
                                                        }
                                                    }
                                                    num6 = num4;
                                                    parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                    if (parseInt3.intValue() != 0) {
                                                    }
                                                    str53 = queryParameter41;
                                                    str54 = queryParameter40;
                                                    str55 = queryParameter39;
                                                    str56 = null;
                                                    num2 = num7;
                                                    str15 = str49;
                                                    i11 = i13;
                                                    str18 = str60;
                                                    str6 = str59;
                                                    str14 = str58;
                                                    str20 = str57;
                                                    str12 = str56;
                                                    str29 = str55;
                                                    str36 = str54;
                                                    str37 = str53;
                                                    str27 = str51;
                                                    str31 = str50;
                                                    str19 = str61;
                                                    str35 = str62;
                                                    str22 = str63;
                                                    str33 = str64;
                                                    str32 = str65;
                                                    str23 = num3;
                                                    str25 = num2;
                                                    str26 = str69;
                                                    str38 = str68;
                                                    str39 = str67;
                                                    str40 = str66;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z11 = false;
                                                    z28 = false;
                                                    z29 = false;
                                                    z12 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    j6 = 0;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                    str30 = null;
                                                    str34 = null;
                                                    z30 = z38;
                                                    str13 = str52;
                                                    str24 = str70;
                                                    z13 = false;
                                                }
                                                z38 = false;
                                                str16 = null;
                                                i13 = -1;
                                                str17 = null;
                                                str60 = null;
                                                str59 = null;
                                                str58 = null;
                                                str57 = null;
                                                str21 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str61 = null;
                                                str62 = null;
                                                str63 = null;
                                                str64 = null;
                                                str65 = null;
                                                num3 = null;
                                                str70 = null;
                                                r43 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                num2 = r43;
                                                str15 = str49;
                                                i11 = i13;
                                                str18 = str60;
                                                str6 = str59;
                                                str14 = str58;
                                                str20 = str57;
                                                str12 = str56;
                                                str29 = str55;
                                                str36 = str54;
                                                str37 = str53;
                                                str27 = str51;
                                                str31 = str50;
                                                str19 = str61;
                                                str35 = str62;
                                                str22 = str63;
                                                str33 = str64;
                                                str32 = str65;
                                                str23 = num3;
                                                str25 = num2;
                                                str26 = str69;
                                                str38 = str68;
                                                str39 = str67;
                                                str40 = str66;
                                                i10 = 0;
                                                c5 = 0;
                                                z11 = false;
                                                z28 = false;
                                                z29 = false;
                                                z12 = false;
                                                z14 = false;
                                                z15 = false;
                                                j8 = 0;
                                                j6 = 0;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str28 = null;
                                                str30 = null;
                                                str34 = null;
                                                z30 = z38;
                                                str13 = str52;
                                                str24 = str70;
                                                z13 = false;
                                            }
                                            str65 = substring;
                                            str49 = null;
                                            z38 = false;
                                            str16 = null;
                                            i13 = -1;
                                            str17 = null;
                                            str60 = null;
                                            str59 = null;
                                            str58 = null;
                                            str57 = null;
                                            str21 = null;
                                            str56 = null;
                                            str55 = null;
                                            str54 = null;
                                            str53 = null;
                                            str52 = null;
                                            str51 = null;
                                            str50 = null;
                                            str61 = null;
                                            str62 = null;
                                            str63 = null;
                                            str64 = null;
                                            num3 = null;
                                            str70 = null;
                                            r43 = null;
                                            str69 = null;
                                            str68 = null;
                                            str67 = null;
                                            str66 = null;
                                            num2 = r43;
                                            str15 = str49;
                                            i11 = i13;
                                            str18 = str60;
                                            str6 = str59;
                                            str14 = str58;
                                            str20 = str57;
                                            str12 = str56;
                                            str29 = str55;
                                            str36 = str54;
                                            str37 = str53;
                                            str27 = str51;
                                            str31 = str50;
                                            str19 = str61;
                                            str35 = str62;
                                            str22 = str63;
                                            str33 = str64;
                                            str32 = str65;
                                            str23 = num3;
                                            str25 = num2;
                                            str26 = str69;
                                            str38 = str68;
                                            str39 = str67;
                                            str40 = str66;
                                            i10 = 0;
                                            c5 = 0;
                                            z11 = false;
                                            z28 = false;
                                            z29 = false;
                                            z12 = false;
                                            z14 = false;
                                            z15 = false;
                                            j8 = 0;
                                            j6 = 0;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str28 = null;
                                            str30 = null;
                                            str34 = null;
                                            z30 = z38;
                                            str13 = str52;
                                            str24 = str70;
                                            z13 = false;
                                        }
                                        str49 = null;
                                        z38 = false;
                                        str16 = null;
                                        i13 = -1;
                                        str17 = null;
                                        str60 = null;
                                        str59 = null;
                                        str58 = null;
                                        str57 = null;
                                        str21 = null;
                                        str56 = null;
                                        str55 = null;
                                        str54 = null;
                                        str53 = null;
                                        str52 = null;
                                        str51 = null;
                                        str50 = null;
                                        str61 = null;
                                        str62 = null;
                                        str63 = null;
                                        str64 = null;
                                        str65 = null;
                                        num3 = null;
                                        str70 = null;
                                        r43 = null;
                                        str69 = null;
                                        str68 = null;
                                        str67 = null;
                                        str66 = null;
                                        num2 = r43;
                                        str15 = str49;
                                        i11 = i13;
                                        str18 = str60;
                                        str6 = str59;
                                        str14 = str58;
                                        str20 = str57;
                                        str12 = str56;
                                        str29 = str55;
                                        str36 = str54;
                                        str37 = str53;
                                        str27 = str51;
                                        str31 = str50;
                                        str19 = str61;
                                        str35 = str62;
                                        str22 = str63;
                                        str33 = str64;
                                        str32 = str65;
                                        str23 = num3;
                                        str25 = num2;
                                        str26 = str69;
                                        str38 = str68;
                                        str39 = str67;
                                        str40 = str66;
                                        i10 = 0;
                                        c5 = 0;
                                        z11 = false;
                                        z28 = false;
                                        z29 = false;
                                        z12 = false;
                                        z14 = false;
                                        z15 = false;
                                        j8 = 0;
                                        j6 = 0;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str28 = null;
                                        str30 = null;
                                        str34 = null;
                                        z30 = z38;
                                        str13 = str52;
                                        str24 = str70;
                                        z13 = false;
                                    }
                                    break;
                            }
                            if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                                str41 = "phone";
                                str42 = "message_id";
                            } else {
                                str42 = "message_id";
                                str41 = "phone";
                                FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(scheme) && str30 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                                intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                            }
                            if (str35 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                str7 = str42;
                            } else if (str12 == null || str13 != null) {
                                str = " ";
                                iArr2 = iArr3;
                                str7 = str42;
                                launchActivity = this;
                                final AlertDialog alertDialog = new AlertDialog(launchActivity, 3);
                                alertDialog.setCanCancel(false);
                                alertDialog.show();
                                tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                                tLRPC$TL_account_sendConfirmPhoneCode.hash = str13;
                                TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                                tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings;
                                tLRPC$TL_codeSettings.allow_flashcall = false;
                                boolean hasServices = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                                tLRPC$TL_codeSettings.allow_firebase = hasServices;
                                tLRPC$TL_codeSettings.allow_app_hash = hasServices;
                                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                                if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                                    sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                                } else {
                                    sharedPreferences.edit().remove("sms_hash").apply();
                                }
                                final Bundle bundle = new Bundle();
                                bundle.putString(str41, str12);
                                final String str86 = str12;
                                ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$handleIntent$14(alertDialog, str86, bundle, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                                    }
                                }, 2);
                            } else if (str14 != null || str15 != null || str16 != null || str17 != null || str22 != null || str6 != null || str29 != null || str36 != null || str28 != null || str30 != null || str27 != null || str35 != null || str33 != null || str32 != null || str24 != null || str31 != null || str34 != null) {
                                if (str6 != null && str6.startsWith("@")) {
                                    str6 = " " + str6;
                                }
                                str7 = str42;
                                iArr2 = iArr3;
                                str = " ";
                                runLinkRequest(iArr3[0], str14, str15, str16, str17, str18, str19, str20, str21, str6, str22, z30, str23, str24, str25, str26, str29, str28, str27, str30, str35, str34, str33, str32, str31, str36, str37, 0, i11, str38, str39, str40, progress);
                                launchActivity = this;
                            } else {
                                try {
                                    query = getContentResolver().query(intent.getData(), null, null, null, null);
                                } catch (Exception e5) {
                                    e = e5;
                                }
                                if (query != null) {
                                    try {
                                        if (query.moveToFirst()) {
                                            int intValue3 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                            int i25 = 0;
                                            while (true) {
                                                if (i25 < 4) {
                                                    if (UserConfig.getInstance(i25).getClientUserId() != intValue3) {
                                                        try {
                                                            i25++;
                                                        } catch (Throwable th4) {
                                                            th = th4;
                                                            Throwable th5 = th;
                                                            try {
                                                                query.close();
                                                                throw th5;
                                                            }
                                                        }
                                                    } else {
                                                        iArr3[0] = i25;
                                                        switchToAccount(iArr3[0], true);
                                                    }
                                                }
                                            }
                                            long j17 = query.getLong(query.getColumnIndex("data4"));
                                            NotificationCenter.getInstance(iArr3[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            try {
                                                String string2 = query.getString(query.getColumnIndex("mimetype"));
                                                if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                    j8 = j17;
                                                    z31 = true;
                                                } else {
                                                    j8 = j17;
                                                    z31 = z28;
                                                    if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                        z29 = true;
                                                    }
                                                }
                                                if (query != null) {
                                                    try {
                                                        query.close();
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        z28 = z31;
                                                        FileLog.e(e);
                                                        str = " ";
                                                        iArr2 = iArr3;
                                                        launchActivity = this;
                                                        i9 = i10;
                                                        c4 = c5;
                                                        j5 = j8;
                                                        str7 = str42;
                                                        i2 = i9;
                                                        c = c4;
                                                        z7 = z28;
                                                        z4 = z29;
                                                        str3 = str8;
                                                        str4 = str9;
                                                        str5 = str10;
                                                        str2 = str11;
                                                        str76 = str7;
                                                        iArr = iArr2;
                                                        i = -1;
                                                        i3 = 0;
                                                        i4 = -1;
                                                        z5 = false;
                                                        z10 = false;
                                                        z9 = false;
                                                        z8 = false;
                                                        z6 = false;
                                                        j4 = 0;
                                                        i5 = 0;
                                                        intent8 = intent;
                                                        j3 = j5;
                                                        j2 = j6;
                                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        }
                                                        z16 = false;
                                                        z17 = true;
                                                        z18 = z;
                                                        z19 = false;
                                                        r7 = z16;
                                                        r12 = z17;
                                                        if (!z19) {
                                                        }
                                                        if (z41) {
                                                        }
                                                        if (!z6) {
                                                        }
                                                        intent8.setAction(r7);
                                                        return z19;
                                                    }
                                                }
                                                z28 = z31;
                                                str = " ";
                                                iArr2 = iArr3;
                                                launchActivity = this;
                                                i9 = i10;
                                                c4 = c5;
                                                j5 = j8;
                                                str7 = str42;
                                            } catch (Throwable th6) {
                                                th = th6;
                                                Throwable th52 = th;
                                                query.close();
                                                throw th52;
                                            }
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                    }
                                }
                                z31 = z28;
                                if (query != null) {
                                }
                                z28 = z31;
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                i9 = i10;
                                c4 = c5;
                                j5 = j8;
                                str7 = str42;
                            }
                            i9 = i10;
                            c4 = c5;
                            j5 = j8;
                        }
                        j7 = 0;
                        j8 = j7;
                        j6 = j8;
                        str12 = null;
                        str13 = null;
                        str14 = null;
                        str15 = null;
                        str16 = null;
                        str6 = null;
                        str17 = null;
                        str18 = null;
                        str19 = null;
                        str20 = null;
                        str21 = null;
                        str22 = null;
                        z30 = false;
                        str23 = null;
                        str24 = null;
                        str25 = null;
                        str26 = null;
                        i10 = 0;
                        c5 = 0;
                        z11 = false;
                        z28 = false;
                        z29 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        str8 = null;
                        str9 = null;
                        str10 = null;
                        str11 = null;
                        str29 = null;
                        str28 = null;
                        str27 = null;
                        str30 = null;
                        str35 = null;
                        str34 = null;
                        str33 = null;
                        str32 = null;
                        str31 = null;
                        str36 = null;
                        str37 = null;
                        i11 = -1;
                        str38 = null;
                        str39 = null;
                        str40 = null;
                        if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                        }
                        if (str35 != null) {
                        }
                        if (str12 == null) {
                        }
                        str = " ";
                        iArr2 = iArr3;
                        str7 = str42;
                        launchActivity = this;
                        final AlertDialog alertDialog2 = new AlertDialog(launchActivity, 3);
                        alertDialog2.setCanCancel(false);
                        alertDialog2.show();
                        tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                        tLRPC$TL_account_sendConfirmPhoneCode.hash = str13;
                        TLRPC$TL_codeSettings tLRPC$TL_codeSettings2 = new TLRPC$TL_codeSettings();
                        tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings2;
                        tLRPC$TL_codeSettings2.allow_flashcall = false;
                        boolean hasServices2 = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                        tLRPC$TL_codeSettings2.allow_firebase = hasServices2;
                        tLRPC$TL_codeSettings2.allow_app_hash = hasServices2;
                        SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                        }
                        final Bundle bundle2 = new Bundle();
                        bundle2.putString(str41, str12);
                        final String str862 = str12;
                        ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$handleIntent$14(alertDialog2, str862, bundle2, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                        i9 = i10;
                        c4 = c5;
                        j5 = j8;
                    } else {
                        str = " ";
                        str7 = "message_id";
                        iArr2 = iArr3;
                        launchActivity = this;
                        j5 = 0;
                        i9 = 0;
                        c4 = 0;
                        z11 = false;
                        z28 = false;
                        z29 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        j6 = 0;
                        str8 = null;
                        str9 = null;
                        str10 = null;
                        str11 = null;
                    }
                    i2 = i9;
                    c = c4;
                    z7 = z28;
                    z4 = z29;
                    str3 = str8;
                    str4 = str9;
                    str5 = str10;
                    str2 = str11;
                    str76 = str7;
                    iArr = iArr2;
                    i = -1;
                    i3 = 0;
                    i4 = -1;
                    z5 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z6 = false;
                    j4 = 0;
                    i5 = 0;
                    intent8 = intent;
                    j3 = j5;
                    j2 = j6;
                } else {
                    str = " ";
                    launchActivity = this;
                    z4 = false;
                    int i26 = -1;
                    if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        intent8 = intent;
                        str76 = "message_id";
                        iArr = iArr3;
                        i = -1;
                        i2 = 0;
                        j2 = 0;
                        i3 = 0;
                        i4 = -1;
                        j3 = 0;
                        str2 = null;
                        z5 = false;
                        z10 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z6 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        j4 = 0;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        i5 = 0;
                        c = 1;
                    } else if (intent.getAction().equals("new_dialog")) {
                        intent7 = intent;
                        str76 = "message_id";
                        iArr = iArr3;
                        i = -1;
                        i2 = 0;
                        j2 = 0;
                        i3 = 0;
                        i4 = -1;
                        j3 = 0;
                        str2 = null;
                        z5 = false;
                        z10 = false;
                        z9 = false;
                        z8 = true;
                        z7 = false;
                        z6 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        j4 = 0;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        i5 = 0;
                        c = 0;
                        intent8 = intent7;
                    } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                        Intent intent10 = intent;
                        j2 = intent10.getLongExtra("chatId", 0L);
                        j3 = intent10.getLongExtra("userId", 0L);
                        int intExtra = intent10.getIntExtra("encId", 0);
                        int intExtra2 = intent10.getIntExtra("appWidgetId", 0);
                        int intExtra3 = intent10.getIntExtra("topicId", 0);
                        if (intExtra2 != 0) {
                            j3 = 0;
                            i26 = intent10.getIntExtra("appWidgetType", 0);
                            i7 = intExtra2;
                            str76 = "message_id";
                            iArr = iArr3;
                            intExtra3 = 0;
                            i8 = 0;
                            i6 = 0;
                            z10 = false;
                            c3 = 6;
                            j2 = 0;
                        } else {
                            str76 = "message_id";
                            int intExtra4 = intent10.getIntExtra(str76, 0);
                            if (j2 != 0) {
                                iArr = iArr3;
                                NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                j3 = 0;
                                i6 = intExtra4;
                                i7 = -1;
                            } else {
                                iArr = iArr3;
                                if (j3 != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    j2 = 0;
                                    i6 = intExtra4;
                                    i7 = -1;
                                    intExtra3 = 0;
                                } else if (intExtra != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    j2 = 0;
                                    j3 = 0;
                                    i6 = intExtra4;
                                    intExtra3 = 0;
                                    z10 = false;
                                    c3 = 0;
                                    i8 = intExtra;
                                    i7 = -1;
                                } else {
                                    j2 = 0;
                                    j3 = 0;
                                    i6 = intExtra4;
                                    i7 = -1;
                                    intExtra3 = 0;
                                    i8 = 0;
                                    z10 = true;
                                    c3 = 0;
                                }
                            }
                            i8 = 0;
                            z10 = false;
                            c3 = 0;
                        }
                        j4 = 0;
                        i5 = intExtra3;
                        i3 = i8;
                        i2 = i6;
                        c = c3;
                        str2 = null;
                        z5 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z6 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        i4 = i7;
                        i = i26;
                        intent8 = intent10;
                    } else {
                        Intent intent11 = intent;
                        str76 = "message_id";
                        iArr = iArr3;
                        j = 0;
                        if (intent.getAction().equals("com.tmessages.openplayer")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i = -1;
                            i2 = 0;
                            i3 = 0;
                            i4 = -1;
                            str2 = null;
                            z5 = true;
                            intent6 = intent11;
                            z10 = false;
                            z9 = false;
                            intent5 = intent6;
                            z8 = false;
                            z7 = false;
                            z6 = false;
                            intent4 = intent5;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent7 = intent4;
                            str3 = null;
                            str4 = null;
                            str5 = null;
                            i5 = 0;
                            c = 0;
                            intent8 = intent7;
                        } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i = -1;
                            i2 = 0;
                            i3 = 0;
                            i4 = -1;
                            str2 = null;
                            z5 = false;
                            z10 = false;
                            z9 = true;
                            intent5 = intent11;
                            z8 = false;
                            z7 = false;
                            z6 = false;
                            intent4 = intent5;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent7 = intent4;
                            str3 = null;
                            str4 = null;
                            str5 = null;
                            i5 = 0;
                            c = 0;
                            intent8 = intent7;
                        } else {
                            intent3 = intent11;
                            if (action.equals("voip_chat")) {
                                j2 = 0;
                                j3 = 0;
                                j4 = 0;
                                i = -1;
                                i2 = 0;
                                i3 = 0;
                                i4 = -1;
                                str2 = null;
                                z5 = false;
                                z10 = false;
                                z9 = false;
                                z8 = false;
                                z7 = false;
                                z6 = true;
                                intent4 = intent11;
                                z11 = false;
                                z12 = false;
                                z13 = false;
                                z14 = false;
                                z15 = false;
                                intent7 = intent4;
                                str3 = null;
                                str4 = null;
                                str5 = null;
                                i5 = 0;
                                c = 0;
                                intent8 = intent7;
                            }
                            j2 = j;
                            j3 = j2;
                            j4 = j3;
                            i = -1;
                            i2 = 0;
                            i3 = 0;
                            i4 = -1;
                            str2 = null;
                            z5 = false;
                            intent6 = intent3;
                            z10 = false;
                            z9 = false;
                            intent5 = intent6;
                            z8 = false;
                            z7 = false;
                            z6 = false;
                            intent4 = intent5;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent7 = intent4;
                            str3 = null;
                            str4 = null;
                            str5 = null;
                            i5 = 0;
                            c = 0;
                            intent8 = intent7;
                        }
                    }
                }
                str = " ";
                intent2 = intent;
                launchActivity = this;
                j = 0;
            }
            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                if (str2 != null) {
                    BaseFragment lastFragment = launchActivity.actionBarLayout.getLastFragment();
                    if (lastFragment instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) lastFragment;
                        if (dialogsActivity.isMainDialogList()) {
                            if (dialogsActivity.getFragmentView() != null) {
                                r3 = 1;
                                dialogsActivity.search(str2, true);
                            } else {
                                r3 = 1;
                                dialogsActivity.setInitialSearchString(str2);
                            }
                        }
                    } else {
                        r3 = 1;
                        z10 = true;
                    }
                    if (j3 != 0) {
                        if (j2 != 0) {
                            Bundle bundle3 = new Bundle();
                            bundle3.putLong("chat_id", j2);
                            if (i2 != 0) {
                                bundle3.putInt(str76, i2);
                            }
                            if (!mainFragmentsStack.isEmpty()) {
                                MessagesController messagesController = MessagesController.getInstance(iArr[0]);
                                ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                                if (!messagesController.checkCanOpenChat(bundle3, arrayList5.get(arrayList5.size() - r3))) {
                                    z24 = true;
                                    z19 = false;
                                    z23 = z24;
                                }
                            }
                            ChatActivity chatActivity = new ChatActivity(bundle3);
                            int i27 = i5;
                            if (i27 > 0) {
                                TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().findTopic(j2, i27);
                                FileLog.d(j2 + str + i27 + " TL_forumTopic " + findTopic);
                                if (findTopic != null) {
                                    TLRPC$Message tLRPC$Message = findTopic.topicStartMessage;
                                    ArrayList<MessageObject> arrayList6 = new ArrayList<>();
                                    TLRPC$Chat chat = MessagesController.getInstance(launchActivity.currentAccount).getChat(Long.valueOf(j2));
                                    arrayList6.add(new MessageObject(launchActivity.currentAccount, tLRPC$Message, false, false));
                                    chatActivity.setThreadMessages(arrayList6, chat, findTopic.id, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
                                } else {
                                    MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().loadTopic(j2, i27, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.lambda$handleIntent$15(intent, z, z2, z3, progress);
                                        }
                                    });
                                    return true;
                                }
                            }
                            z24 = true;
                            z21 = true;
                            if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(chatActivity).setNoAnimation(true))) {
                                launchActivity.drawerLayoutContainer.closeDrawer();
                                z19 = true;
                                z23 = z21;
                            }
                            z19 = false;
                            z23 = z24;
                        } else {
                            String str87 = str;
                            z17 = true;
                            z21 = true;
                            z24 = true;
                            z22 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            z17 = true;
                            r12 = 1;
                            z17 = true;
                            r12 = 1;
                            if (i3 != 0) {
                                Bundle bundle4 = new Bundle();
                                bundle4.putInt("enc_id", i3);
                                if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle4)).setNoAnimation(true))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z19 = true;
                                    z23 = z21;
                                }
                                z19 = false;
                                z23 = z24;
                            } else if (z10) {
                                if (!AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.removeAllFragments();
                                } else if (!launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                    while (launchActivity.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                                        INavigationLayout iNavigationLayout = launchActivity.layersActionBarLayout;
                                        iNavigationLayout.removeFragmentFromStack(iNavigationLayout.getFragmentStack().get(0));
                                    }
                                    z18 = false;
                                    launchActivity.layersActionBarLayout.closeLastFragment(false);
                                    z19 = false;
                                    r7 = 0;
                                    r12 = z22;
                                }
                                z18 = false;
                                z19 = false;
                                r7 = 0;
                                r12 = z22;
                            } else {
                                if (!z5) {
                                    z16 = false;
                                    z16 = false;
                                    z16 = false;
                                    z16 = false;
                                    z16 = false;
                                    z16 = false;
                                    z16 = false;
                                    r7 = 0;
                                    z16 = false;
                                    r7 = 0;
                                    if (z9) {
                                        if (!launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                            launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda111
                                                @Override // org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate
                                                public final void didSelectLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
                                                    LaunchActivity.this.lambda$handleIntent$17(iArr, sharingLocationInfo);
                                                }
                                            }, null));
                                        }
                                    } else {
                                        Uri uri11 = launchActivity.exportingChatUri;
                                        if (uri11 != null) {
                                            launchActivity.runImportRequest(uri11, launchActivity.documentsUrisArray);
                                        } else if (launchActivity.importingStickers != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    LaunchActivity.this.lambda$handleIntent$18();
                                                }
                                            });
                                        } else if (launchActivity.videoPath == null && launchActivity.photoPathsArray == null && launchActivity.sendingText == null && launchActivity.documentsPathsArray == null && launchActivity.contactsToSend == null && launchActivity.documentsUrisArray == null) {
                                            char c7 = c;
                                            if (c7 != 0) {
                                                if (c7 == 1) {
                                                    Bundle bundle5 = new Bundle();
                                                    bundle5.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                                    baseFragment2 = new ProfileActivity(bundle5);
                                                } else if (c7 == 2) {
                                                    baseFragment2 = new ThemeActivity(0);
                                                } else if (c7 == 3) {
                                                    baseFragment2 = new SessionsActivity(0);
                                                } else if (c7 == 4) {
                                                    baseFragment2 = new FiltersSetupActivity();
                                                } else if (c7 == 5) {
                                                    c2 = 6;
                                                    z20 = true;
                                                    baseFragment = new ActionIntroActivity(3);
                                                    if (c7 == c2) {
                                                        launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(baseFragment).setNoAnimation(true));
                                                    } else {
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                LaunchActivity.this.lambda$handleIntent$19(baseFragment, z20);
                                                            }
                                                        });
                                                    }
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(1);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                    }
                                                } else {
                                                    c2 = 6;
                                                    editWidgetActivity = c7 == 6 ? new EditWidgetActivity(i, i4) : null;
                                                    z20 = false;
                                                    baseFragment = editWidgetActivity;
                                                    if (c7 == c2) {
                                                    }
                                                    if (AndroidUtilities.isTablet()) {
                                                    }
                                                }
                                                c2 = 6;
                                                editWidgetActivity = baseFragment2;
                                                z20 = false;
                                                baseFragment = editWidgetActivity;
                                                if (c7 == c2) {
                                                }
                                                if (AndroidUtilities.isTablet()) {
                                                }
                                            } else if (z8) {
                                                Bundle bundle6 = new Bundle();
                                                bundle6.putBoolean("destroyAfterSelect", true);
                                                launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ContactsActivity(bundle6)).setNoAnimation(true));
                                                if (AndroidUtilities.isTablet()) {
                                                    launchActivity.actionBarLayout.rebuildFragments(1);
                                                    launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                } else {
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                }
                                            } else {
                                                String str88 = str3;
                                                if (str88 != null) {
                                                    Bundle bundle7 = new Bundle();
                                                    bundle7.putBoolean("destroyAfterSelect", true);
                                                    bundle7.putBoolean("returnAsResult", true);
                                                    bundle7.putBoolean("onlyUsers", true);
                                                    bundle7.putBoolean("allowSelf", false);
                                                    ContactsActivity contactsActivity = new ContactsActivity(bundle7);
                                                    contactsActivity.setInitialSearchString(str88);
                                                    contactsActivity.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda112
                                                        @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                                        public final void didSelectContact(TLRPC$User tLRPC$User, String str89, ContactsActivity contactsActivity2) {
                                                            LaunchActivity.this.lambda$handleIntent$20(z4, iArr, tLRPC$User, str89, contactsActivity2);
                                                        }
                                                    });
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(contactsActivity).setRemoveLast(launchActivity.actionBarLayout.getLastFragment() instanceof ContactsActivity));
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(1);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                    }
                                                } else if (z15) {
                                                    final ActionIntroActivity actionIntroActivity = new ActionIntroActivity(5);
                                                    actionIntroActivity.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
                                                        @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                                        public final void didFindQRCode(String str89) {
                                                            LaunchActivity.this.lambda$handleIntent$24(actionIntroActivity, str89);
                                                        }
                                                    });
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(actionIntroActivity).setNoAnimation(true));
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(1);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                    }
                                                } else if (z13) {
                                                    NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(launchActivity.actionBarLayout.getLastFragment(), launchActivity);
                                                    String str89 = str4;
                                                    if (str89 != null) {
                                                        String[] split3 = str89.split(str87, 2);
                                                        newContactBottomSheet.setInitialName(split3[0], split3.length > 1 ? split3[1] : null);
                                                    }
                                                    String str90 = str5;
                                                    if (str90 != null) {
                                                        newContactBottomSheet.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str90, true), false);
                                                    }
                                                    newContactBottomSheet.show();
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(1);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                    }
                                                } else {
                                                    final String str91 = str4;
                                                    String str92 = str5;
                                                    if (z6) {
                                                        GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                        if (GroupCallActivity.groupCallInstance != null) {
                                                            GroupCallActivity.groupCallUiVisible = true;
                                                        }
                                                    } else if (z14) {
                                                        final BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                                                        if (lastFragment2 == null || lastFragment2.getParentActivity() == null) {
                                                            z19 = false;
                                                        } else {
                                                            final String phoneNumber = NewContactBottomSheet.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str92, false);
                                                            lastFragment2.showDialog(new AlertDialog.Builder(lastFragment2.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", R.string.NewContactAlertMessage, PhoneFormat.getInstance().format(phoneNumber)))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                                                                @Override // android.content.DialogInterface.OnClickListener
                                                                public final void onClick(DialogInterface dialogInterface, int i28) {
                                                                    LaunchActivity.this.lambda$handleIntent$25(lastFragment2, phoneNumber, str91, dialogInterface, i28);
                                                                }
                                                            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
                                                            z19 = true;
                                                        }
                                                        z18 = z;
                                                    } else if (z11) {
                                                        launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new CallLogActivity()).setNoAnimation(true));
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.rebuildFragments(1);
                                                            launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                        }
                                                    }
                                                }
                                            }
                                            z18 = z;
                                            z19 = true;
                                        } else {
                                            if (!AndroidUtilities.isTablet()) {
                                                NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            }
                                            long j18 = j4;
                                            if (j18 == 0) {
                                                launchActivity.openDialogsToSend(false);
                                                z18 = z;
                                                z19 = true;
                                            } else {
                                                ArrayList<MessagesStorage.TopicKey> arrayList7 = new ArrayList<>();
                                                arrayList7.add(MessagesStorage.TopicKey.of(j18, 0));
                                                didSelectDialogs(null, arrayList7, null, false, null);
                                            }
                                        }
                                    }
                                } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                    z16 = false;
                                } else {
                                    z16 = false;
                                    launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                                }
                                z18 = z;
                                z19 = false;
                                r7 = z16;
                                r12 = z17;
                            }
                        }
                        z18 = z;
                        z22 = z23;
                        r7 = 0;
                        r12 = z22;
                    } else if (!z7 && !z4) {
                        Bundle bundle8 = new Bundle();
                        bundle8.putLong("user_id", j3);
                        if (i2 != 0) {
                            bundle8.putInt(str76, i2);
                        }
                        if (!mainFragmentsStack.isEmpty()) {
                            MessagesController messagesController2 = MessagesController.getInstance(iArr[0]);
                            ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                        }
                        if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle8)).setNoAnimation(r3))) {
                            launchActivity.drawerLayoutContainer.closeDrawer();
                            z25 = true;
                            z18 = z;
                            z19 = z25;
                            r7 = 0;
                            r12 = 1;
                        }
                        z25 = false;
                        z18 = z;
                        z19 = z25;
                        r7 = 0;
                        r12 = 1;
                    } else if (z12) {
                        BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                        if (lastFragment3 != null) {
                            AlertsCreator.createCallDialogAlert(lastFragment3, lastFragment3.getMessagesController().getUser(Long.valueOf(j3)), z4);
                        }
                    } else {
                        VoIPPendingCall.startOrSchedule(launchActivity, j3, z4, AccountInstance.getInstance(iArr[0]));
                    }
                    if (!z19 && !z18) {
                        if (!AndroidUtilities.isTablet()) {
                            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                if (launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                    launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                }
                            } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                DialogsActivity dialogsActivity2 = new DialogsActivity(r7);
                                dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                if (str2 != null) {
                                    dialogsActivity2.setInitialSearchString(str2);
                                }
                                launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2);
                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r12, false);
                            }
                        } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                            } else {
                                DialogsActivity dialogsActivity3 = new DialogsActivity(r7);
                                dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                                if (str2 != null) {
                                    dialogsActivity3.setInitialSearchString(str2);
                                }
                                launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3);
                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r12, false);
                            }
                        }
                        launchActivity.actionBarLayout.rebuildFragments(r12);
                        if (AndroidUtilities.isTablet()) {
                            launchActivity.layersActionBarLayout.rebuildFragments(r12);
                            launchActivity.rightActionBarLayout.rebuildFragments(r12);
                        }
                    }
                    if (z41) {
                        VoIPFragment.show(launchActivity, iArr[0]);
                    }
                    if (!z6 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                        groupCallActivity.dismiss();
                    }
                    intent8.setAction(r7);
                    return z19;
                }
                r3 = 1;
                if (j3 != 0) {
                }
                if (!z19) {
                    if (!AndroidUtilities.isTablet()) {
                    }
                    launchActivity.actionBarLayout.rebuildFragments(r12);
                    if (AndroidUtilities.isTablet()) {
                    }
                }
                if (z41) {
                }
                if (!z6) {
                    groupCallActivity.dismiss();
                }
                intent8.setAction(r7);
                return z19;
            }
            z16 = false;
            z17 = true;
            z18 = z;
            z19 = false;
            r7 = z16;
            r12 = z17;
            if (!z19) {
            }
            if (z41) {
            }
            if (!z6) {
            }
            intent8.setAction(r7);
            return z19;
        }
        j = 0;
        str = " ";
        intent2 = intent;
        launchActivity = this;
        iArr = iArr3;
        z4 = false;
        intent3 = intent2;
        j2 = j;
        j3 = j2;
        j4 = j3;
        i = -1;
        i2 = 0;
        i3 = 0;
        i4 = -1;
        str2 = null;
        z5 = false;
        intent6 = intent3;
        z10 = false;
        z9 = false;
        intent5 = intent6;
        z8 = false;
        z7 = false;
        z6 = false;
        intent4 = intent5;
        z11 = false;
        z12 = false;
        z13 = false;
        z14 = false;
        z15 = false;
        intent7 = intent4;
        str3 = null;
        str4 = null;
        str5 = null;
        i5 = 0;
        c = 0;
        intent8 = intent7;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z16 = false;
        z17 = true;
        z18 = z;
        z19 = false;
        r7 = z16;
        r12 = z17;
        if (!z19) {
        }
        if (z41) {
        }
        if (!z6) {
        }
        intent8.setAction(r7);
        return z19;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$11(String str) {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).presentFragment(new PremiumPreviewFragment(Uri.parse(str).getQueryParameter("ref")));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$12(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$14(final AlertDialog alertDialog, final String str, final Bundle bundle, final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$handleIntent$13(alertDialog, tLRPC$TL_error, str, bundle, tLObject, tLRPC$TL_account_sendConfirmPhoneCode);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$13(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, String str, Bundle bundle, TLObject tLObject, TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode) {
        alertDialog.dismiss();
        if (tLRPC$TL_error == null) {
            lambda$runLinkRequest$72(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, getActionBarLayout().getLastFragment(), tLRPC$TL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$17(final int[] iArr, LocationController.SharingLocationInfo sharingLocationInfo) {
        iArr[0] = sharingLocationInfo.messageObject.currentAccount;
        switchToAccount(iArr[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        final long dialogId = sharingLocationInfo.messageObject.getDialogId();
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda118
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
                LaunchActivity.lambda$handleIntent$16(iArr, dialogId, tLRPC$MessageMedia, i, z, i2);
            }
        });
        lambda$runLinkRequest$72(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$16(int[] iArr, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$18() {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$19(BaseFragment baseFragment, boolean z) {
        presentFragment(baseFragment, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$20(boolean z, int[] iArr, TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
        VoIPHelper.startCall(tLRPC$User, z, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(iArr[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$24(final ActionIntroActivity actionIntroActivity, String str) {
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        byte[] decode = Base64.decode(str.substring(17), 8);
        TLRPC$TL_auth_acceptLoginToken tLRPC$TL_auth_acceptLoginToken = new TLRPC$TL_auth_acceptLoginToken();
        tLRPC$TL_auth_acceptLoginToken.token = decode;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$handleIntent$23(AlertDialog.this, actionIntroActivity, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$23(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$22(AlertDialog.this, tLObject, actionIntroActivity, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$22(AlertDialog alertDialog, TLObject tLObject, final ActionIntroActivity actionIntroActivity, final TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (tLObject instanceof TLRPC$TL_authorization) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$21(ActionIntroActivity.this, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$21(ActionIntroActivity actionIntroActivity, TLRPC$TL_error tLRPC$TL_error) {
        String string = LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient);
        AlertsCreator.showSimpleAlert(actionIntroActivity, string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$25(BaseFragment baseFragment, String str, String str2, DialogInterface dialogInterface, int i) {
        NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(baseFragment, this);
        newContactBottomSheet.setInitialPhoneNumber(str, false);
        if (str2 != null) {
            String[] split = str2.split(" ", 2);
            newContactBottomSheet.setInitialName(split[0], split.length > 1 ? split[1] : null);
        }
        newContactBottomSheet.show();
    }

    public static int getTimestampFromLink(Uri uri) {
        String queryParameter;
        int i;
        if (uri.getPathSegments().contains(MediaStreamTrack.VIDEO_TRACK_KIND)) {
            queryParameter = uri.getQuery();
        } else {
            queryParameter = uri.getQueryParameter("t") != null ? uri.getQueryParameter("t") : null;
        }
        if (queryParameter != null) {
            try {
                i = Integer.parseInt(queryParameter);
            } catch (Throwable unused) {
                i = -1;
            }
            if (i == -1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                try {
                    return (int) ((simpleDateFormat.parse(queryParameter).getTime() - simpleDateFormat.parse("00:00").getTime()) / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return i;
        }
        return -1;
    }

    private void openDialogsToSend(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putBoolean("canSelectTopics", true);
        bundle.putInt("dialogsType", 3);
        bundle.putBoolean("allowSwitchAccount", true);
        ArrayList<TLRPC$User> arrayList = this.contactsToSend;
        if (arrayList != null) {
            if (arrayList.size() != 1) {
                bundle.putString("selectAlertString", LocaleController.getString("SendContactToText", R.string.SendMessagesToText));
                bundle.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroupText", R.string.SendContactToGroupText));
            }
        } else {
            bundle.putString("selectAlertString", LocaleController.getString("SendMessagesToText", R.string.SendMessagesToText));
            bundle.putString("selectAlertStringGroup", LocaleController.getString("SendMessagesToGroupText", R.string.SendMessagesToGroupText));
        }
        DialogsActivity dialogsActivity = new DialogsActivity(bundle) { // from class: org.telegram.ui.LaunchActivity.16
            @Override // org.telegram.ui.DialogsActivity
            public boolean shouldShowNextButton(DialogsActivity dialogsActivity2, ArrayList<Long> arrayList2, CharSequence charSequence, boolean z2) {
                if (LaunchActivity.this.exportingChatUri != null) {
                    return false;
                }
                if (LaunchActivity.this.contactsToSend == null || LaunchActivity.this.contactsToSend.size() != 1 || LaunchActivity.mainFragmentsStack.isEmpty()) {
                    if (arrayList2.size() <= 1) {
                        if (LaunchActivity.this.videoPath != null) {
                            return true;
                        }
                        if (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
        };
        dialogsActivity.setDelegate(this);
        this.actionBarLayout.presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), !z, true, false);
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.dismiss();
        }
        if (z) {
            return;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        if (AndroidUtilities.isTablet()) {
            this.actionBarLayout.rebuildFragments(1);
            this.rightActionBarLayout.rebuildFragments(1);
            return;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
    }

    private int runCommentRequest(int i, Runnable runnable, Integer num, Integer num2, Integer num3, TLRPC$Chat tLRPC$Chat) {
        return runCommentRequest(i, runnable, num, num2, num3, tLRPC$Chat, null);
    }

    private int runCommentRequest(final int i, final Runnable runnable, final Integer num, final Integer num2, final Integer num3, final TLRPC$Chat tLRPC$Chat, final Runnable runnable2) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = (num2 != null ? num : num3).intValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runCommentRequest$29(i, tLRPC$Chat, num3, num, tLRPC$TL_messages_getDiscussionMessage, num2, runnable2, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$29(final int i, final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, final Runnable runnable2, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$28(tLObject, i, tLRPC$Chat, num, num2, tLRPC$TL_messages_getDiscussionMessage, num3, runnable, runnable2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$28(TLObject tLObject, int i, final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, Runnable runnable2) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_discussionMessage) {
            TLRPC$TL_messages_discussionMessage tLRPC$TL_messages_discussionMessage = (TLRPC$TL_messages_discussionMessage) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_discussionMessage.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_messages_discussionMessage.chats, false);
            final ArrayList<MessageObject> arrayList = new ArrayList<>();
            int size = tLRPC$TL_messages_discussionMessage.messages.size();
            for (int i2 = 0; i2 < size; i2++) {
                arrayList.add(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_messages_discussionMessage.messages.get(i2), true, true));
            }
            if (!arrayList.isEmpty() || (tLRPC$Chat.forum && num != null && num.intValue() == 1)) {
                if (tLRPC$Chat.forum) {
                    TLRPC$TL_channels_getForumTopicsByID tLRPC$TL_channels_getForumTopicsByID = new TLRPC$TL_channels_getForumTopicsByID();
                    tLRPC$TL_channels_getForumTopicsByID.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(tLRPC$Chat.id);
                    tLRPC$TL_channels_getForumTopicsByID.topics.add(num);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runCommentRequest$27(tLRPC$Chat, num, num2, arrayList, tLRPC$TL_messages_getDiscussionMessage, num3, runnable, tLObject2, tLRPC$TL_error);
                        }
                    });
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -arrayList.get(0).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num2.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, tLRPC$TL_messages_discussionMessage.read_inbox_max_id, tLRPC$TL_messages_discussionMessage.read_outbox_max_id, null);
                    if (num3 != null) {
                        chatActivity.setHighlightMessageId(num3.intValue());
                    } else if (num != null) {
                        chatActivity.setHighlightMessageId(num2.intValue());
                    }
                    lambda$runLinkRequest$72(chatActivity);
                }
                z = true;
            }
        }
        if (!z) {
            try {
                if (!mainFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                    BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString("ChannelPostDeleted", R.string.ChannelPostDeleted)).show();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        if (runnable2 != null) {
            try {
                runnable2.run();
            } catch (Exception e2) {
                FileLog.e(e2);
                return;
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$27(final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final ArrayList arrayList, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$26(tLRPC$TL_error, tLObject, tLRPC$Chat, num, num2, arrayList, tLRPC$TL_messages_getDiscussionMessage, num3, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$26(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$Chat tLRPC$Chat, Integer num, Integer num2, ArrayList arrayList, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Integer num3, Runnable runnable) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics = (TLRPC$TL_messages_forumTopics) tLObject;
            SparseArray<TLRPC$Message> sparseArray = new SparseArray<>();
            for (int i = 0; i < tLRPC$TL_messages_forumTopics.messages.size(); i++) {
                sparseArray.put(tLRPC$TL_messages_forumTopics.messages.get(i).id, tLRPC$TL_messages_forumTopics.messages.get(i));
            }
            MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_messages_forumTopics.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_forumTopics.chats, false);
            MessagesController.getInstance(this.currentAccount).getTopicsController().processTopics(tLRPC$Chat.id, tLRPC$TL_messages_forumTopics.topics, sparseArray, false, 2, -1);
        }
        TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(tLRPC$Chat.id, num.intValue());
        if (findTopic != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", tLRPC$Chat.id);
            if (num2.intValue() != findTopic.id) {
                bundle.putInt("message_id", Math.max(1, num2.intValue()));
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            if (arrayList.isEmpty()) {
                TLRPC$Message tLRPC$Message = new TLRPC$Message();
                tLRPC$Message.id = 1;
                tLRPC$Message.action = new TLRPC$TL_messageActionChannelMigrateFrom();
                arrayList.add(new MessageObject(this.currentAccount, tLRPC$Message, false, false));
            }
            chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
            if (num3 != null) {
                chatActivity.setHighlightMessageId(num3.intValue());
            } else if (num2.intValue() != findTopic.id) {
                chatActivity.setHighlightMessageId(num2.intValue());
            }
            lambda$runLinkRequest$72(chatActivity);
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private void runImportRequest(final Uri uri, ArrayList<Uri> arrayList) {
        InputStream openInputStream;
        final int i = UserConfig.selectedAccount;
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        final int[] iArr = {0};
        InputStream inputStream = null;
        try {
            try {
                openInputStream = getContentResolver().openInputStream(uri);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openInputStream));
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null || i2 >= 100) {
                    break;
                }
                sb.append(readLine);
                sb.append('\n');
                i2++;
            }
            String sb2 = sb.toString();
            if (openInputStream != null) {
                try {
                    openInputStream.close();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            TLRPC$TL_messages_checkHistoryImport tLRPC$TL_messages_checkHistoryImport = new TLRPC$TL_messages_checkHistoryImport();
            tLRPC$TL_messages_checkHistoryImport.import_head = sb2;
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runImportRequest$31(uri, i, alertDialog, tLObject, tLRPC$TL_error);
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    LaunchActivity.lambda$runImportRequest$32(i, iArr, r3, dialogInterface);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } catch (Exception e3) {
            e = e3;
            inputStream = openInputStream;
            FileLog.e(e);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runImportRequest$31(final Uri uri, final int i, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runImportRequest$30(tLObject, uri, i, alertDialog);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runImportRequest$30(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
        boolean z;
        if (isFinishing()) {
            return;
        }
        boolean z2 = false;
        if (tLObject != null && this.actionBarLayout != null) {
            TLRPC$TL_messages_historyImportParsed tLRPC$TL_messages_historyImportParsed = (TLRPC$TL_messages_historyImportParsed) tLObject;
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putString("importTitle", tLRPC$TL_messages_historyImportParsed.title);
            bundle.putBoolean("allowSwitchAccount", true);
            if (tLRPC$TL_messages_historyImportParsed.pm) {
                bundle.putInt("dialogsType", 12);
            } else if (tLRPC$TL_messages_historyImportParsed.group) {
                bundle.putInt("dialogsType", 11);
            } else {
                String uri2 = uri.toString();
                Iterator<String> it = MessagesController.getInstance(i).exportPrivateUri.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    } else if (uri2.contains(it.next())) {
                        bundle.putInt("dialogsType", 12);
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    Iterator<String> it2 = MessagesController.getInstance(i).exportGroupUri.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        } else if (uri2.contains(it2.next())) {
                            bundle.putInt("dialogsType", 11);
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        bundle.putInt("dialogsType", 13);
                    }
                }
            }
            if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
            if (groupCallActivity != null) {
                groupCallActivity.dismiss();
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            if (AndroidUtilities.isTablet()) {
                this.actionBarLayout.rebuildFragments(1);
                this.rightActionBarLayout.rebuildFragments(1);
            } else {
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            dialogsActivity.setDelegate(this);
            if (!AndroidUtilities.isTablet() ? !(this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)) : !(this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity))) {
                z2 = true;
            }
            this.actionBarLayout.presentFragment(dialogsActivity, z2, false, true, false);
        } else {
            if (this.documentsUrisArray == null) {
                this.documentsUrisArray = new ArrayList<>();
            }
            this.documentsUrisArray.add(0, this.exportingChatUri);
            this.exportingChatUri = null;
            openDialogsToSend(true);
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runImportRequest$32(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:125:0x051f  */
    /* JADX WARN: Removed duplicated region for block: B:141:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final String str11, final HashMap<String, String> hashMap, final String str12, final String str13, final String str14, final String str15, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str16, final String str17, final String str18, final String str19, int i2, final int i3, final String str20, final String str21, final String str22, final Browser.Progress progress) {
        final AlertDialog alertDialog;
        char c;
        final int i4;
        Runnable runnable;
        final AlertDialog alertDialog2;
        final Browser.Progress progress2;
        BaseFragment baseFragment;
        final Runnable runnable2;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        EmojiPacksAlert emojiPacksAlert;
        StickersAlert stickersAlert;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername;
        String str23 = str3;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i5) {
                    LaunchActivity.this.lambda$runLinkRequest$35(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, z, num, l, num2, num3, str11, hashMap, str12, str13, str14, str15, tLRPC$TL_wallPaper, str16, str17, str18, str19, i3, str20, str21, str22, progress, i5);
                }
            }).show();
            return;
        }
        if (str14 != null) {
            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
            int i5 = NotificationCenter.didReceiveSmsCode;
            if (globalInstance.hasObservers(i5)) {
                NotificationCenter.getGlobalInstance().postNotificationName(i5, str14);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", R.string.OtherLoginCode, str14)));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        } else if (str15 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder2);
        } else {
            final AlertDialog alertDialog3 = new AlertDialog(this, 3);
            final Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$runLinkRequest$36(Browser.Progress.this, alertDialog3);
                }
            };
            final int[] iArr = {0};
            if (str10 != null) {
                TLRPC$TL_contacts_importContactToken tLRPC$TL_contacts_importContactToken = new TLRPC$TL_contacts_importContactToken();
                tLRPC$TL_contacts_importContactToken.token = str10;
                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_importContactToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$38(i, str10, runnable3, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str16 != null) {
                TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = str16;
                tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$41(i, str16, runnable3, tLObject, tLRPC$TL_error);
                    }
                });
            } else {
                if (str != null) {
                    if (AndroidUtilities.isNumeric(str)) {
                        TLRPC$TL_contacts_resolvePhone tLRPC$TL_contacts_resolvePhone = new TLRPC$TL_contacts_resolvePhone();
                        tLRPC$TL_contacts_resolvePhone.phone = str;
                        tLRPC$TL_contacts_resolveUsername = tLRPC$TL_contacts_resolvePhone;
                    } else {
                        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername2 = new TLRPC$TL_contacts_resolveUsername();
                        tLRPC$TL_contacts_resolveUsername2.username = str;
                        tLRPC$TL_contacts_resolveUsername = tLRPC$TL_contacts_resolveUsername2;
                    }
                    alertDialog = alertDialog3;
                    iArr = iArr;
                    c = 0;
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$57(str11, str18, str19, i, str20, str21, str22, num, num3, num2, iArr, runnable3, str6, str7, str8, str5, i3, str, tLObject, tLRPC$TL_error);
                        }
                    });
                } else {
                    alertDialog = alertDialog3;
                    c = 0;
                    if (str2 == null) {
                        i4 = i;
                        if (str23 != null) {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                            tLRPC$TL_inputStickerSetShortName.short_name = str23;
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BaseFragment baseFragment2 = arrayList.get(arrayList.size() - 1);
                            if (baseFragment2 instanceof ChatActivity) {
                                ChatActivity chatActivity = (ChatActivity) baseFragment2;
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, null, chatActivity.getChatActivityEnterViewForStickers(), chatActivity.getResourceProvider());
                                stickersAlert.setCalcMandatoryInsets(chatActivity.isKeyboardVisible());
                            } else {
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null);
                            }
                            stickersAlert.probablyEmojis = str4 != null;
                            baseFragment2.showDialog(stickersAlert);
                            return;
                        } else if (str4 != null) {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName2 = new TLRPC$TL_inputStickerSetShortName();
                            if (str23 == null) {
                                str23 = str4;
                            }
                            tLRPC$TL_inputStickerSetShortName2.short_name = str23;
                            ArrayList arrayList2 = new ArrayList(1);
                            arrayList2.add(tLRPC$TL_inputStickerSetShortName2);
                            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                            BaseFragment baseFragment3 = arrayList3.get(arrayList3.size() - 1);
                            if (baseFragment3 instanceof ChatActivity) {
                                ChatActivity chatActivity2 = (ChatActivity) baseFragment3;
                                emojiPacksAlert = new EmojiPacksAlert(baseFragment3, this, chatActivity2.getResourceProvider(), arrayList2);
                                emojiPacksAlert.setCalcMandatoryInsets(chatActivity2.isKeyboardVisible());
                            } else {
                                emojiPacksAlert = new EmojiPacksAlert(baseFragment3, this, null, arrayList2);
                            }
                            baseFragment3.showDialog(emojiPacksAlert);
                            return;
                        } else {
                            runnable = null;
                            if (str9 != null) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("onlySelect", true);
                                bundle.putInt("dialogsType", 3);
                                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda116
                                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z2, TopicsFragment topicsFragment) {
                                        boolean lambda$runLinkRequest$63;
                                        lambda$runLinkRequest$63 = LaunchActivity.this.lambda$runLinkRequest$63(z, i4, str9, dialogsActivity2, arrayList4, charSequence, z2, topicsFragment);
                                        return lambda$runLinkRequest$63;
                                    }
                                });
                                presentFragment(dialogsActivity, false, true);
                            } else if (hashMap != null) {
                                int intValue = Utilities.parseInt((CharSequence) hashMap.get("bot_id")).intValue();
                                if (intValue == 0) {
                                    return;
                                }
                                final String str24 = hashMap.get("payload");
                                final String str25 = hashMap.get("nonce");
                                final String str26 = hashMap.get("callback_url");
                                final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
                                tLRPC$TL_account_getAuthorizationForm.bot_id = intValue;
                                tLRPC$TL_account_getAuthorizationForm.scope = hashMap.get("scope");
                                tLRPC$TL_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$67(iArr, i, runnable3, tLRPC$TL_account_getAuthorizationForm, str24, str25, str26, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str13 != null) {
                                TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                                tLRPC$TL_help_getDeepLinkInfo.path = str13;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$69(runnable3, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str12 != null) {
                                TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                                tLRPC$TL_langpack_getLanguage.lang_code = str12;
                                tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$71(runnable3, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (tLRPC$TL_wallPaper != null) {
                                if (TextUtils.isEmpty(tLRPC$TL_wallPaper.slug)) {
                                    try {
                                        TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                                        int i6 = tLRPC$WallPaperSettings.third_background_color;
                                        if (i6 != 0) {
                                            colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, i6, tLRPC$WallPaperSettings.fourth_background_color);
                                        } else {
                                            colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false));
                                        }
                                        final ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(colorWallpaper, null, true, false);
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$runLinkRequest$72(themePreviewActivity);
                                            }
                                        });
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                    if (!r3) {
                                        TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                                        TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                                        tLRPC$TL_inputWallPaperSlug.slug = tLRPC$TL_wallPaper.slug;
                                        tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                LaunchActivity.this.lambda$runLinkRequest$74(runnable3, tLRPC$TL_wallPaper, tLObject, tLRPC$TL_error);
                                            }
                                        });
                                    }
                                }
                                r3 = false;
                                if (!r3) {
                                }
                            } else if (str17 != null) {
                                progress2 = progress;
                                runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.this.lambda$runLinkRequest$75(progress2);
                                    }
                                };
                                TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                                tLRPC$TL_account_getTheme.format = "android";
                                TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                                tLRPC$TL_inputThemeSlug.slug = str17;
                                tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                                alertDialog2 = alertDialog;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$77(alertDialog2, runnable3, tLObject, tLRPC$TL_error);
                                    }
                                });
                                if (iArr[c] == 0) {
                                }
                            } else {
                                alertDialog2 = alertDialog;
                                progress2 = progress;
                                if (l != null && num != null) {
                                    if (num2 != null) {
                                        TLRPC$Chat chat = MessagesController.getInstance(i).getChat(l);
                                        if (chat != null) {
                                            iArr[0] = runCommentRequest(i, runnable3, num, num3, num2, chat);
                                        } else {
                                            TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                                            TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
                                            tLRPC$TL_inputChannel.channel_id = l.longValue();
                                            tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
                                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                                                @Override // org.telegram.tgnet.RequestDelegate
                                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                    LaunchActivity.this.lambda$runLinkRequest$79(iArr, i, runnable3, num, num3, num2, tLObject, tLRPC$TL_error);
                                                }
                                            });
                                        }
                                    } else {
                                        final Bundle bundle2 = new Bundle();
                                        bundle2.putLong("chat_id", l.longValue());
                                        bundle2.putInt("message_id", num.intValue());
                                        TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(l);
                                        if (chat2 != null && chat2.forum) {
                                            openForumFromLink(-l.longValue(), 0, num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda28
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    LaunchActivity.lambda$runLinkRequest$80(runnable3);
                                                }
                                            });
                                        } else {
                                            if (mainFragmentsStack.isEmpty()) {
                                                baseFragment = null;
                                            } else {
                                                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                                baseFragment = arrayList4.get(arrayList4.size() - 1);
                                            }
                                            if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle2, baseFragment)) {
                                                final BaseFragment baseFragment4 = baseFragment;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        LaunchActivity.this.lambda$runLinkRequest$83(bundle2, l, iArr, runnable3, num2, num, baseFragment4, i);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                runnable2 = runnable;
                                if (iArr[c] == 0) {
                                }
                            }
                            alertDialog2 = alertDialog;
                            progress2 = progress;
                            runnable2 = runnable;
                            if (iArr[c] == 0) {
                            }
                        }
                    } else if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$60(i, alertDialog, runnable3, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                    } else if (i2 == 1) {
                        TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                        tLRPC$TL_messages_importChatInvite.hash = str2;
                        i4 = i;
                        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$62(i4, runnable3, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                        alertDialog2 = alertDialog;
                        progress2 = progress;
                        runnable = null;
                        runnable2 = runnable;
                        if (iArr[c] == 0) {
                            alertDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda1
                                @Override // android.content.DialogInterface.OnCancelListener
                                public final void onCancel(DialogInterface dialogInterface) {
                                    LaunchActivity.lambda$runLinkRequest$84(i4, iArr, runnable2, dialogInterface);
                                }
                            });
                            if (progress2 != null) {
                                progress2.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.lambda$runLinkRequest$85(i4, iArr, runnable2);
                                    }
                                });
                            }
                            try {
                                if (progress2 != null) {
                                    progress.init();
                                } else {
                                    alertDialog2.showDelayed(300L);
                                }
                                return;
                            } catch (Exception unused) {
                                return;
                            }
                        }
                        return;
                    }
                }
                i4 = i;
                alertDialog2 = alertDialog;
                progress2 = progress;
                runnable = null;
                runnable2 = runnable;
                if (iArr[c] == 0) {
                }
            }
            progress2 = progress;
            alertDialog2 = alertDialog3;
            runnable = null;
            i4 = i;
            c = 0;
            runnable2 = runnable;
            if (iArr[c] == 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$35(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, boolean z, Integer num, Long l, Integer num2, Integer num3, String str11, HashMap hashMap, String str12, String str13, String str14, String str15, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str16, String str17, String str18, String str19, int i2, String str20, String str21, String str22, Browser.Progress progress, int i3) {
        if (i3 != i) {
            switchToAccount(i3, true);
        }
        runLinkRequest(i3, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, z, num, l, num2, num3, str11, hashMap, str12, str13, str14, str15, tLRPC$TL_wallPaper, str16, str17, str18, str19, 1, i2, str20, str21, str22, progress);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$36(Browser.Progress progress, AlertDialog alertDialog) {
        if (progress != null) {
            progress.end();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$38(final int i, final String str, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$37(tLObject, i, str, tLRPC$TL_error, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$37(TLObject tLObject, int i, String str, TLRPC$TL_error tLRPC$TL_error, Runnable runnable) {
        if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
            MessagesController.getInstance(i).putUser(tLRPC$User, false);
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", tLRPC$User.id);
            lambda$runLinkRequest$72(new ChatActivity(bundle));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("cant import contact token. token=");
            sb.append(str);
            sb.append(" err=");
            sb.append(tLRPC$TL_error == null ? null : tLRPC$TL_error.text);
            FileLog.e(sb.toString());
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.NoUsernameFound)).show();
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$41(final int i, final String str, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$40(tLRPC$TL_error, tLObject, i, str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$40(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, String str, Runnable runnable) {
        PaymentFormActivity paymentFormActivity;
        if (tLRPC$TL_error != null) {
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.PaymentInvoiceLinkInvalid)).show();
        } else if (!isFinishing()) {
            if (tLObject instanceof TLRPC$TL_payments_paymentForm) {
                TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = (TLRPC$TL_payments_paymentForm) tLObject;
                MessagesController.getInstance(i).putUsers(tLRPC$TL_payments_paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(tLRPC$TL_payments_paymentForm, str, getActionBarLayout().getLastFragment());
            } else {
                paymentFormActivity = tLObject instanceof TLRPC$TL_payments_paymentReceipt ? new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                final Runnable runnable2 = this.navigateToPremiumGiftCallback;
                if (runnable2 != null) {
                    this.navigateToPremiumGiftCallback = null;
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda119
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$39(runnable2, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$72(paymentFormActivity);
            }
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$39(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$57(final String str, final String str2, final String str3, final int i, final String str4, final String str5, final String str6, final Integer num, final Integer num2, final Integer num3, final int[] iArr, final Runnable runnable, final String str7, final String str8, final String str9, final String str10, final int i2, final String str11, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$56(tLObject, tLRPC$TL_error, str, str2, str3, i, str4, str5, str6, num, num2, num3, iArr, runnable, str7, str8, str9, str10, i2, str11);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$56(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, final String str, String str2, String str3, final int i, final String str4, String str5, final String str6, Integer num, Integer num2, Integer num3, int[] iArr, final Runnable runnable, String str7, String str8, final String str9, String str10, int i2, String str11) {
        String str12;
        long j;
        boolean z;
        ChatActivity chatActivity;
        if (isFinishing()) {
            return;
        }
        final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
        boolean z2 = true;
        if (tLRPC$TL_error == null && this.actionBarLayout != null && ((str == null && str2 == null) || ((str != null && !tLRPC$TL_contacts_resolvedPeer.users.isEmpty()) || ((str2 != null && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()) || (str3 != null && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()))))) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_contacts_resolvedPeer.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_contacts_resolvedPeer.chats, false);
            MessagesStorage.getInstance(i).putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, tLRPC$TL_contacts_resolvedPeer.chats, false, true);
            if (str4 != null && str5 == null) {
                final TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_contacts_resolvedPeer.peer.user_id));
                if (user != null && user.bot) {
                    if (user.bot_attach_menu) {
                        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
                        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
                        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                                LaunchActivity.this.lambda$runLinkRequest$48(i, str6, user, str4, tLRPC$TL_contacts_resolvedPeer, tLObject2, tLRPC$TL_error2);
                            }
                        });
                    } else {
                        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                        BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
                    }
                } else {
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                    BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotSetAttachLinkNotBot)).show();
                }
            } else if (num != null && ((num2 != null || num3 != null) && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty())) {
                iArr[0] = runCommentRequest(i, runnable, num, num2, num3, tLRPC$TL_contacts_resolvedPeer.chats.get(0));
                if (iArr[0] != 0) {
                    z2 = false;
                }
            } else if (str != null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("cantSendToChannels", true);
                bundle.putInt("dialogsType", 1);
                bundle.putString("selectAlertString", LocaleController.getString("SendGameToText", R.string.SendGameToText));
                bundle.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", R.string.SendGameToGroupText));
                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda114
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z3, TopicsFragment topicsFragment) {
                        boolean lambda$runLinkRequest$49;
                        lambda$runLinkRequest$49 = LaunchActivity.this.lambda$runLinkRequest$49(str, i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity2, arrayList3, charSequence, z3, topicsFragment);
                        return lambda$runLinkRequest$49;
                    }
                });
                this.actionBarLayout.presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), true, true, false);
                if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                    SecretMediaViewer.getInstance().closePhoto(false, false);
                } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                    PhotoViewer.getInstance().closePhoto(false, true);
                } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                    ArticleViewer.getInstance().close(false, true);
                }
                GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                if (groupCallActivity != null) {
                    groupCallActivity.dismiss();
                }
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                if (AndroidUtilities.isTablet()) {
                    this.actionBarLayout.rebuildFragments(1);
                    this.rightActionBarLayout.rebuildFragments(1);
                } else {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                }
            } else if (str7 != null || str8 != null) {
                TLRPC$User tLRPC$User = !tLRPC$TL_contacts_resolvedPeer.users.isEmpty() ? tLRPC$TL_contacts_resolvedPeer.users.get(0) : null;
                if (tLRPC$User == null || (tLRPC$User.bot && tLRPC$User.bot_nochats)) {
                    try {
                        if (mainFragmentsStack.isEmpty()) {
                            return;
                        }
                        ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                        BulletinFactory.of(arrayList3.get(arrayList3.size() - 1)).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups)).show();
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                }
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("onlySelect", true);
                bundle2.putInt("dialogsType", 2);
                bundle2.putBoolean("resetDelegate", false);
                bundle2.putBoolean("closeFragment", false);
                bundle2.putBoolean("allowGroups", str7 != null);
                bundle2.putBoolean("allowChannels", str8 != null);
                String str13 = TextUtils.isEmpty(str7) ? TextUtils.isEmpty(str8) ? null : str8 : str7;
                final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle2);
                final TLRPC$User tLRPC$User2 = tLRPC$User;
                final String str14 = str13;
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda113
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z3, TopicsFragment topicsFragment) {
                        boolean lambda$runLinkRequest$54;
                        lambda$runLinkRequest$54 = LaunchActivity.this.lambda$runLinkRequest$54(i, tLRPC$User2, str9, str14, dialogsActivity2, dialogsActivity3, arrayList4, charSequence, z3, topicsFragment);
                        return lambda$runLinkRequest$54;
                    }
                });
                lambda$runLinkRequest$72(dialogsActivity2);
            } else {
                Bundle bundle3 = new Bundle();
                if (!tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()) {
                    bundle3.putLong("chat_id", tLRPC$TL_contacts_resolvedPeer.chats.get(0).id);
                    j = -tLRPC$TL_contacts_resolvedPeer.chats.get(0).id;
                } else {
                    bundle3.putLong("user_id", tLRPC$TL_contacts_resolvedPeer.users.get(0).id);
                    j = tLRPC$TL_contacts_resolvedPeer.users.get(0).id;
                }
                if (str10 == null || tLRPC$TL_contacts_resolvedPeer.users.size() <= 0 || !tLRPC$TL_contacts_resolvedPeer.users.get(0).bot) {
                    z = false;
                } else {
                    bundle3.putString("botUser", str10);
                    z = true;
                }
                if (this.navigateToPremiumBot) {
                    this.navigateToPremiumBot = false;
                    bundle3.putBoolean("premium_bot", true);
                }
                if (num != null) {
                    bundle3.putInt("message_id", num.intValue());
                }
                if (str2 != null) {
                    bundle3.putString("voicechat", str2);
                }
                if (str3 != null) {
                    bundle3.putString("livestream", str3);
                }
                if (i2 >= 0) {
                    bundle3.putInt("video_timestamp", i2);
                }
                if (str5 != null) {
                    bundle3.putString("attach_bot", str5);
                }
                if (str4 != null) {
                    bundle3.putString("attach_bot_start_command", str4);
                }
                if (mainFragmentsStack.isEmpty() || str2 != null) {
                    chatActivity = null;
                } else {
                    ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                    chatActivity = arrayList4.get(arrayList4.size() - 1);
                }
                if (chatActivity == null || MessagesController.getInstance(i).checkCanOpenChat(bundle3, chatActivity)) {
                    if (z && (chatActivity instanceof ChatActivity)) {
                        ChatActivity chatActivity2 = chatActivity;
                        if (chatActivity2.getDialogId() == j) {
                            chatActivity2.setBotUser(str10);
                        }
                    }
                    long j2 = -j;
                    TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                    if (chat != null && chat.forum) {
                        Integer num4 = num3 == null ? num : num3;
                        if (num4 != null && num4.intValue() != 0) {
                            openForumFromLink(j, num4.intValue(), num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.lambda$runLinkRequest$55(runnable);
                                }
                            });
                        } else {
                            Bundle bundle4 = new Bundle();
                            bundle4.putLong("chat_id", j2);
                            lambda$runLinkRequest$72(new TopicsFragment(bundle4));
                            try {
                                runnable.run();
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                        }
                    } else {
                        long j3 = j;
                        MessagesController.getInstance(i).ensureMessagesLoaded(j3, num == null ? 0 : num.intValue(), new 18(runnable, str3, chatActivity, j3, num, bundle3));
                    }
                    z2 = false;
                }
            }
        } else {
            try {
                if (!mainFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                    BaseFragment baseFragment = arrayList5.get(arrayList5.size() - 1);
                    if (baseFragment instanceof ChatActivity) {
                        ((ChatActivity) baseFragment).shakeContent();
                    }
                    if (tLRPC$TL_error != null && (str12 = tLRPC$TL_error.text) != null && str12.startsWith("FLOOD_WAIT")) {
                        BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString("FloodWait", R.string.FloodWait)).show();
                    } else if (AndroidUtilities.isNumeric(str11)) {
                        BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString("NoPhoneFound", R.string.NoPhoneFound)).show();
                    } else {
                        BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound)).show();
                    }
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
        }
        if (z2) {
            try {
                runnable.run();
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$48(final int i, final String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$47(tLObject, i, str, tLRPC$User, str2, tLRPC$TL_contacts_resolvedPeer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$47(TLObject tLObject, final int i, String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer) {
        final DialogsActivity dialogsActivity;
        String[] split;
        if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            TLRPC$TL_attachMenuBotsBot tLRPC$TL_attachMenuBotsBot = (TLRPC$TL_attachMenuBotsBot) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_attachMenuBotsBot.users, false);
            TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = tLRPC$TL_attachMenuBotsBot.bot;
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            final BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
            ArrayList arrayList2 = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                for (String str3 : str.split(" ")) {
                    if (MediaDataController.canShowAttachMenuBotForTarget(tLRPC$TL_attachMenuBot, str3)) {
                        arrayList2.add(str3);
                    }
                }
            }
            if (arrayList2.isEmpty()) {
                dialogsActivity = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("dialogsType", 14);
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("allowGroups", arrayList2.contains("groups"));
                bundle.putBoolean("allowUsers", arrayList2.contains("users"));
                bundle.putBoolean("allowChannels", arrayList2.contains("channels"));
                bundle.putBoolean("allowBots", arrayList2.contains("bots"));
                DialogsActivity dialogsActivity2 = new DialogsActivity(bundle);
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda115
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList3, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                        boolean lambda$runLinkRequest$42;
                        lambda$runLinkRequest$42 = LaunchActivity.this.lambda$runLinkRequest$42(tLRPC$User, str2, i, dialogsActivity3, arrayList3, charSequence, z, topicsFragment);
                        return lambda$runLinkRequest$42;
                    }
                });
                dialogsActivity = dialogsActivity2;
            }
            if (!tLRPC$TL_attachMenuBot.inactive) {
                if (dialogsActivity != null) {
                    lambda$runLinkRequest$72(dialogsActivity);
                    return;
                } else if (baseFragment instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment;
                    if (!MediaDataController.canShowAttachMenuBot(tLRPC$TL_attachMenuBot, chatActivity.getCurrentUser() != null ? chatActivity.getCurrentUser() : chatActivity.getCurrentChat())) {
                        BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(R.string.BotAlreadyAddedToAttachMenu)).show();
                        return;
                    } else {
                        chatActivity.openAttachBotLayout(tLRPC$User.id, str2);
                        return;
                    }
                } else {
                    BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(R.string.BotAlreadyAddedToAttachMenu)).show();
                    return;
                }
            }
            AttachBotIntroTopView attachBotIntroTopView = new AttachBotIntroTopView(this);
            attachBotIntroTopView.setColor(Theme.getColor("chat_attachContactIcon"));
            attachBotIntroTopView.setBackgroundColor(Theme.getColor("dialogTopBackground"));
            attachBotIntroTopView.setAttachBot(tLRPC$TL_attachMenuBot);
            final AtomicBoolean atomicBoolean = new AtomicBoolean();
            AlertDialog.Builder negativeButton = new AlertDialog.Builder(this).setTopView(attachBotIntroTopView).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRequestAttachPermission", R.string.BotRequestAttachPermission, UserObject.getUserName(tLRPC$User)))).setPositiveButton(LocaleController.getString(R.string.BotAddToMenu), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    LaunchActivity.this.lambda$runLinkRequest$45(i, tLRPC$TL_contacts_resolvedPeer, atomicBoolean, dialogsActivity, baseFragment, tLRPC$User, str2, dialogInterface, i2);
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            if (tLRPC$TL_attachMenuBot.request_write_access) {
                atomicBoolean.set(true);
                final CheckBoxCell checkBoxCell = new CheckBoxCell(this, 5, baseFragment.getResourceProvider());
                checkBoxCell.setBackground(Theme.getSelectorDrawable(false));
                checkBoxCell.setMultiline(true);
                checkBoxCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("OpenUrlOption2", R.string.OpenUrlOption2, UserObject.getUserName(tLRPC$User))), "", true, false);
                checkBoxCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                checkBoxCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.lambda$runLinkRequest$46(CheckBoxCell.this, atomicBoolean, view);
                    }
                });
                negativeButton.setView(checkBoxCell);
            }
            negativeButton.show();
            return;
        }
        ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
        BulletinFactory.of(arrayList3.get(arrayList3.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$42(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else if (DialogObject.isUserDialog(j)) {
            bundle.putLong("user_id", j);
        } else {
            bundle.putLong("chat_id", -j);
        }
        bundle.putString("attach_bot", UserObject.getPublicUsername(tLRPC$User));
        if (str != null) {
            bundle.putString("attach_bot_start_command", str);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$45(final int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, AtomicBoolean atomicBoolean, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = atomicBoolean.get();
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$44(i, dialogsActivity, baseFragment, tLRPC$User, str, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$44(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$43(tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$43(TLObject tLObject, int i, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
            if (dialogsActivity != null) {
                lambda$runLinkRequest$72(dialogsActivity);
            } else if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$46(CheckBoxCell checkBoxCell, AtomicBoolean atomicBoolean, View view) {
        boolean z = !checkBoxCell.isChecked();
        checkBoxCell.setChecked(z, true);
        atomicBoolean.set(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$49(String str, int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        TLRPC$TL_inputMediaGame tLRPC$TL_inputMediaGame = new TLRPC$TL_inputMediaGame();
        TLRPC$TL_inputGameShortName tLRPC$TL_inputGameShortName = new TLRPC$TL_inputGameShortName();
        tLRPC$TL_inputMediaGame.id = tLRPC$TL_inputGameShortName;
        tLRPC$TL_inputGameShortName.short_name = str;
        tLRPC$TL_inputGameShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.users.get(0));
        SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer(j), tLRPC$TL_inputMediaGame, 0L, 0L);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else if (DialogObject.isUserDialog(j)) {
            bundle.putLong("user_id", j);
        } else {
            bundle.putLong("chat_id", -j);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$54(final int i, final TLRPC$User tLRPC$User, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        final TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(false, chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$52(str, str2, i, chat, dialogsActivity, tLRPC$User, j, z2, tLRPC$TL_chatAdminRights2, str3);
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int i2 = R.string.AddBot;
            builder.setTitle(LocaleController.getString("AddBot", i2));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(tLRPC$User), chat == null ? "" : chat.title)));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("AddBot", i2), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda10
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    LaunchActivity.this.lambda$runLinkRequest$53(j, i, tLRPC$User, str2, dialogInterface, i3);
                }
            });
            builder.show();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$52(final String str, final String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, final TLRPC$User tLRPC$User, final long j, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$51(str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$51(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        if (str != null) {
            String[] split = str.split("\\+| ");
            tLRPC$TL_chatAdminRights2 = new TLRPC$TL_chatAdminRights();
            for (String str4 : split) {
                str4.hashCode();
                char c = 65535;
                switch (str4.hashCode()) {
                    case -2110462504:
                        if (str4.equals("ban_users")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -2095811475:
                        if (str4.equals("anonymous")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1654794275:
                        if (str4.equals("change_info")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1593320096:
                        if (str4.equals("delete_messages")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -939200543:
                        if (str4.equals("edit_messages")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 22162680:
                        if (str4.equals("manage_call")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 22169074:
                        if (str4.equals("manage_chat")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 106069776:
                        if (str4.equals("other")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 449085338:
                        if (str4.equals("promote_members")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 632157522:
                        if (str4.equals("invite_users")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 758599179:
                        if (str4.equals("post_messages")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 1357805750:
                        if (str4.equals("pin_messages")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 1529816162:
                        if (str4.equals("add_admins")) {
                            c = '\f';
                            break;
                        }
                        break;
                    case 1542893206:
                        if (str4.equals("restrict_members")) {
                            c = '\r';
                            break;
                        }
                        break;
                    case 1641337725:
                        if (str4.equals("manage_video_chats")) {
                            c = 14;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case '\r':
                        tLRPC$TL_chatAdminRights2.ban_users = true;
                        break;
                    case 1:
                        tLRPC$TL_chatAdminRights2.anonymous = true;
                        break;
                    case 2:
                        tLRPC$TL_chatAdminRights2.change_info = true;
                        break;
                    case 3:
                        tLRPC$TL_chatAdminRights2.delete_messages = true;
                        break;
                    case 4:
                        tLRPC$TL_chatAdminRights2.edit_messages = true;
                        break;
                    case 5:
                    case 14:
                        tLRPC$TL_chatAdminRights2.manage_call = true;
                        break;
                    case 6:
                    case 7:
                        tLRPC$TL_chatAdminRights2.other = true;
                        break;
                    case '\b':
                    case '\f':
                        tLRPC$TL_chatAdminRights2.add_admins = true;
                        break;
                    case '\t':
                        tLRPC$TL_chatAdminRights2.invite_users = true;
                        break;
                    case '\n':
                        tLRPC$TL_chatAdminRights2.post_messages = true;
                        break;
                    case 11:
                        tLRPC$TL_chatAdminRights2.pin_messages = true;
                        break;
                }
            }
        } else {
            tLRPC$TL_chatAdminRights2 = null;
        }
        if (tLRPC$TL_chatAdminRights2 == null && tLRPC$TL_chatAdminRights == null) {
            tLRPC$TL_chatAdminRights3 = null;
        } else {
            if (tLRPC$TL_chatAdminRights2 != null) {
                if (tLRPC$TL_chatAdminRights == null) {
                    tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights2;
                } else {
                    tLRPC$TL_chatAdminRights.change_info = tLRPC$TL_chatAdminRights2.change_info || tLRPC$TL_chatAdminRights.change_info;
                    tLRPC$TL_chatAdminRights.post_messages = tLRPC$TL_chatAdminRights2.post_messages || tLRPC$TL_chatAdminRights.post_messages;
                    tLRPC$TL_chatAdminRights.edit_messages = tLRPC$TL_chatAdminRights2.edit_messages || tLRPC$TL_chatAdminRights.edit_messages;
                    tLRPC$TL_chatAdminRights.add_admins = tLRPC$TL_chatAdminRights2.add_admins || tLRPC$TL_chatAdminRights.add_admins;
                    tLRPC$TL_chatAdminRights.delete_messages = tLRPC$TL_chatAdminRights2.delete_messages || tLRPC$TL_chatAdminRights.delete_messages;
                    tLRPC$TL_chatAdminRights.ban_users = tLRPC$TL_chatAdminRights2.ban_users || tLRPC$TL_chatAdminRights.ban_users;
                    tLRPC$TL_chatAdminRights.invite_users = tLRPC$TL_chatAdminRights2.invite_users || tLRPC$TL_chatAdminRights.invite_users;
                    tLRPC$TL_chatAdminRights.pin_messages = tLRPC$TL_chatAdminRights2.pin_messages || tLRPC$TL_chatAdminRights.pin_messages;
                    tLRPC$TL_chatAdminRights.manage_call = tLRPC$TL_chatAdminRights2.manage_call || tLRPC$TL_chatAdminRights.manage_call;
                    tLRPC$TL_chatAdminRights.anonymous = tLRPC$TL_chatAdminRights2.anonymous || tLRPC$TL_chatAdminRights.anonymous;
                    tLRPC$TL_chatAdminRights.other = tLRPC$TL_chatAdminRights2.other || tLRPC$TL_chatAdminRights.other;
                }
            }
            tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights;
        }
        if (z && tLRPC$TL_chatAdminRights2 == null && !TextUtils.isEmpty(str2)) {
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$50(i, tLRPC$Chat, dialogsActivity);
                }
            }, null);
            return;
        }
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(tLRPC$User.id, -j, tLRPC$TL_chatAdminRights3, null, null, str3, 2, true, !z, str2);
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate(this) { // from class: org.telegram.ui.LaunchActivity.17
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC$User tLRPC$User2) {
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights4, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str5) {
                dialogsActivity.removeSelfFromStack();
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
        });
        this.actionBarLayout.presentFragment(chatRightsEditActivity, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$50(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            presentFragment(new ChatActivity(bundle), true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$53(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        long j2 = -j;
        bundle.putLong("chat_id", j2);
        ChatActivity chatActivity = new ChatActivity(bundle);
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(j2, tLRPC$User, 0, str, chatActivity, null);
        this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$55(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 18 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ Runnable val$dismissLoading;
        final /* synthetic */ BaseFragment val$lastFragment;
        final /* synthetic */ String val$livestream;
        final /* synthetic */ Integer val$messageId;

        18(Runnable runnable, String str, BaseFragment baseFragment, long j, Integer num, Bundle bundle) {
            this.val$dismissLoading = runnable;
            this.val$livestream = str;
            this.val$lastFragment = baseFragment;
            this.val$dialog_id = j;
            this.val$messageId = num;
            this.val$args = bundle;
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onMessagesLoaded(boolean z) {
            BaseFragment chatActivity;
            try {
                this.val$dismissLoading.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (LaunchActivity.this.isFinishing()) {
                return;
            }
            if (this.val$livestream != null) {
                BaseFragment baseFragment = this.val$lastFragment;
                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == this.val$dialog_id) {
                    chatActivity = this.val$lastFragment;
                    final BaseFragment baseFragment2 = chatActivity;
                    final String str = this.val$livestream;
                    final long j = this.val$dialog_id;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.18.this.lambda$onMessagesLoaded$2(str, j, baseFragment2);
                        }
                    }, 150L);
                }
            }
            BaseFragment baseFragment3 = this.val$lastFragment;
            if ((baseFragment3 instanceof ChatActivity) && ((ChatActivity) baseFragment3).getDialogId() == this.val$dialog_id && this.val$messageId == null) {
                ChatActivity chatActivity2 = (ChatActivity) this.val$lastFragment;
                AndroidUtilities.shakeViewSpring(chatActivity2.getChatListView(), 5.0f);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                ChatActivityEnterView chatActivityEnterView = chatActivity2.getChatActivityEnterView();
                for (int i = 0; i < chatActivityEnterView.getChildCount(); i++) {
                    AndroidUtilities.shakeViewSpring(chatActivityEnterView.getChildAt(i), 5.0f);
                }
                ActionBar actionBar = chatActivity2.getActionBar();
                for (int i2 = 0; i2 < actionBar.getChildCount(); i2++) {
                    AndroidUtilities.shakeViewSpring(actionBar.getChildAt(i2), 5.0f);
                }
                chatActivity = this.val$lastFragment;
            } else {
                chatActivity = new ChatActivity(this.val$args);
                LaunchActivity.this.actionBarLayout.presentFragment(chatActivity);
            }
            final BaseFragment baseFragment22 = chatActivity;
            final String str2 = this.val$livestream;
            final long j2 = this.val$dialog_id;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.18.this.lambda$onMessagesLoaded$2(str2, j2, baseFragment22);
                }
            }, 150L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$2(String str, final long j, final BaseFragment baseFragment) {
            if (str != null) {
                final AccountInstance accountInstance = AccountInstance.getInstance(LaunchActivity.this.currentAccount);
                long j2 = -j;
                ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
                if (groupCall != null) {
                    VoIPHelper.startCall(accountInstance.getMessagesController().getChat(Long.valueOf(j2)), accountInstance.getMessagesController().getInputPeer(j), null, false, Boolean.valueOf(groupCall.call.rtmp_stream ? false : true), LaunchActivity.this, baseFragment, accountInstance);
                    return;
                }
                TLRPC$ChatFull chatFull = accountInstance.getMessagesController().getChatFull(j2);
                if (chatFull != null) {
                    if (chatFull.call == null) {
                        if (baseFragment.getParentActivity() != null) {
                            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.linkbroken, LocaleController.getString("InviteExpired", R.string.InviteExpired)).show();
                            return;
                        }
                        return;
                    }
                    accountInstance.getMessagesController().getGroupCall(j2, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.18.this.lambda$onMessagesLoaded$1(accountInstance, j, baseFragment);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$1(final AccountInstance accountInstance, final long j, final BaseFragment baseFragment) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.18.this.lambda$onMessagesLoaded$0(accountInstance, j, baseFragment);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$0(AccountInstance accountInstance, long j, BaseFragment baseFragment) {
            long j2 = -j;
            boolean z = false;
            ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
            VoIPHelper.startCall(accountInstance.getMessagesController().getChat(Long.valueOf(j2)), accountInstance.getMessagesController().getInputPeer(j), null, false, Boolean.valueOf((groupCall == null || !groupCall.call.rtmp_stream) ? true : true), LaunchActivity.this, baseFragment, accountInstance);
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
            }
            try {
                this.val$dismissLoading.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$60(final int i, final AlertDialog alertDialog, final Runnable runnable, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$59(tLRPC$TL_error, tLObject, i, alertDialog, runnable, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x002f, code lost:
        if (r10.chat.has_geo != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0077, code lost:
        if (r15.checkCanOpenChat(r7, r0.get(r0.size() - 1)) != false) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$59(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, AlertDialog alertDialog, final Runnable runnable, String str) {
        if (isFinishing()) {
            return;
        }
        boolean z = true;
        if (tLRPC$TL_error == null && this.actionBarLayout != null) {
            final TLRPC$ChatInvite tLRPC$ChatInvite = (TLRPC$ChatInvite) tLObject;
            TLRPC$Chat tLRPC$Chat = tLRPC$ChatInvite.chat;
            if (tLRPC$Chat != null) {
                if (ChatObject.isLeftFromChat(tLRPC$Chat)) {
                    TLRPC$Chat tLRPC$Chat2 = tLRPC$ChatInvite.chat;
                    if (!tLRPC$Chat2.kicked) {
                        if (!ChatObject.isPublic(tLRPC$Chat2)) {
                            if (!(tLRPC$ChatInvite instanceof TLRPC$TL_chatInvitePeek)) {
                            }
                        }
                    }
                }
                MessagesController.getInstance(i).putChat(tLRPC$ChatInvite.chat, false);
                ArrayList<TLRPC$Chat> arrayList = new ArrayList<>();
                arrayList.add(tLRPC$ChatInvite.chat);
                MessagesStorage.getInstance(i).putUsersAndChats(null, arrayList, false, true);
                final Bundle bundle = new Bundle();
                bundle.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                if (!mainFragmentsStack.isEmpty()) {
                    MessagesController messagesController = MessagesController.getInstance(i);
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                }
                final boolean[] zArr = new boolean[1];
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        LaunchActivity.lambda$runLinkRequest$58(zArr, dialogInterface);
                    }
                });
                if (tLRPC$ChatInvite.chat.forum) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                    lambda$runLinkRequest$72(new TopicsFragment(bundle2));
                } else {
                    MessagesController.getInstance(i).ensureMessagesLoaded(-tLRPC$ChatInvite.chat.id, 0, new MessagesController.MessagesLoadedCallback() { // from class: org.telegram.ui.LaunchActivity.19
                        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                        public void onMessagesLoaded(boolean z2) {
                            try {
                                runnable.run();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            if (zArr[0]) {
                                return;
                            }
                            ChatActivity chatActivity = new ChatActivity(bundle);
                            TLRPC$ChatInvite tLRPC$ChatInvite2 = tLRPC$ChatInvite;
                            if (tLRPC$ChatInvite2 instanceof TLRPC$TL_chatInvitePeek) {
                                chatActivity.setChatInvite(tLRPC$ChatInvite2);
                            }
                            LaunchActivity.this.actionBarLayout.presentFragment(chatActivity);
                        }

                        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                        public void onError() {
                            if (!LaunchActivity.this.isFinishing()) {
                                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
                            }
                            try {
                                runnable.run();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    });
                    z = false;
                }
            }
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            BaseFragment baseFragment = arrayList3.get(arrayList3.size() - 1);
            baseFragment.showDialog(new JoinGroupAlert(this, tLRPC$ChatInvite, str, baseFragment, baseFragment instanceof ChatActivity ? ((ChatActivity) baseFragment).themeDelegate : null));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
            } else if (tLRPC$TL_error.text.startsWith("INVITE_HASH_EXPIRED")) {
                builder.setTitle(LocaleController.getString("ExpiredLink", R.string.ExpiredLink));
                builder.setMessage(LocaleController.getString("InviteExpired", R.string.InviteExpired));
            } else {
                builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
            }
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        }
        if (z) {
            try {
                runnable.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$58(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$62(final int i, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$61(runnable, tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$61(Runnable runnable, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        if (isFinishing()) {
            return;
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLRPC$TL_error == null) {
            if (this.actionBarLayout != null) {
                TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
                if (tLRPC$Updates.chats.isEmpty()) {
                    return;
                }
                TLRPC$Chat tLRPC$Chat = tLRPC$Updates.chats.get(0);
                tLRPC$Chat.left = false;
                tLRPC$Chat.kicked = false;
                MessagesController.getInstance(i).putUsers(tLRPC$Updates.users, false);
                MessagesController.getInstance(i).putChats(tLRPC$Updates.chats, false);
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", tLRPC$Chat.id);
                if (!mainFragmentsStack.isEmpty()) {
                    MessagesController messagesController = MessagesController.getInstance(i);
                    ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                    if (!messagesController.checkCanOpenChat(bundle, arrayList.get(arrayList.size() - 1))) {
                        return;
                    }
                }
                ChatActivity chatActivity = new ChatActivity(bundle);
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                this.actionBarLayout.presentFragment(chatActivity, false, true, true, false);
                return;
            }
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
        } else if (tLRPC$TL_error.text.equals("USERS_TOO_MUCH")) {
            builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", R.string.JoinToGroupErrorFull));
        } else {
            builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
        }
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        showAlertDialog(builder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$63(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2, TopicsFragment topicsFragment) {
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putBoolean("hasUrl", z);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else if (DialogObject.isUserDialog(j)) {
            bundle.putLong("user_id", j);
        } else {
            bundle.putLong("chat_id", -j);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            MediaDataController.getInstance(i).saveDraft(j, 0, str, null, null, false);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$67(int[] iArr, final int i, final Runnable runnable, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$65(runnable, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3, tLObject2, tLRPC$TL_error2);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$66(runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$65(final Runnable runnable, final int i, final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$64(runnable, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$64(Runnable runnable, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$72(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$account_Password) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$66(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        try {
            runnable.run();
            if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$69(final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$68(runnable, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$68(Runnable runnable, TLObject tLObject) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_help_deepLinkInfo) {
            TLRPC$TL_help_deepLinkInfo tLRPC$TL_help_deepLinkInfo = (TLRPC$TL_help_deepLinkInfo) tLObject;
            AlertsCreator.showUpdateAppAlert(this, tLRPC$TL_help_deepLinkInfo.message, tLRPC$TL_help_deepLinkInfo.update_app);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$71(final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$70(runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$70(Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_langPackLanguage) {
            showAlertDialog(AlertsCreator.createLanguageAlert(this, (TLRPC$TL_langPackLanguage) tLObject));
        } else if (tLRPC$TL_error != null) {
            if ("LANG_CODE_NOT_SUPPORTED".equals(tLRPC$TL_error.text)) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", R.string.LanguageUnsupportedError)));
                return;
            }
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$74(final Runnable runnable, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$73(runnable, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$73(Runnable runnable, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = (TLRPC$TL_wallPaper) tLObject;
            if (tLRPC$TL_wallPaper2.pattern) {
                String str = tLRPC$TL_wallPaper2.slug;
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                int i = tLRPC$WallPaperSettings.background_color;
                int i2 = tLRPC$WallPaperSettings.second_background_color;
                int i3 = tLRPC$WallPaperSettings.third_background_color;
                int i4 = tLRPC$WallPaperSettings.fourth_background_color;
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false);
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings2 = tLRPC$TL_wallPaper.settings;
                WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(str, i, i2, i3, i4, wallpaperRotation, tLRPC$WallPaperSettings2.intensity / 100.0f, tLRPC$WallPaperSettings2.motion, null);
                colorWallpaper.pattern = tLRPC$TL_wallPaper2;
                tLRPC$TL_wallPaper2 = colorWallpaper;
            }
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(tLRPC$TL_wallPaper2, null, true, false);
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings3 = tLRPC$TL_wallPaper.settings;
            themePreviewActivity.setInitialModes(tLRPC$WallPaperSettings3.blur, tLRPC$WallPaperSettings3.motion);
            lambda$runLinkRequest$72(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$75(Browser.Progress progress) {
        this.loadingThemeFileName = null;
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeProgressDialog = null;
        this.loadingTheme = null;
        if (progress != null) {
            progress.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$77(final AlertDialog alertDialog, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$76(tLObject, alertDialog, runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$76(TLObject tLObject, AlertDialog alertDialog, Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
        char c;
        if (tLObject instanceof TLRPC$TL_theme) {
            TLRPC$TL_theme tLRPC$TL_theme = (TLRPC$TL_theme) tLObject;
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = null;
            c = 0;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme.settings.size() > 0 ? tLRPC$TL_theme.settings.get(0) : null;
            if (tLRPC$ThemeSettings != null) {
                Theme.ThemeInfo theme = Theme.getTheme(Theme.getBaseThemeKey(tLRPC$ThemeSettings));
                if (theme != null) {
                    TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
                    if (tLRPC$WallPaper instanceof TLRPC$TL_wallPaper) {
                        tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLRPC$WallPaper;
                        if (!FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true).exists()) {
                            this.loadingThemeProgressDialog = alertDialog;
                            this.loadingThemeAccent = true;
                            this.loadingThemeInfo = theme;
                            this.loadingTheme = tLRPC$TL_theme;
                            this.loadingThemeWallpaper = tLRPC$TL_wallPaper;
                            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                            FileLoader.getInstance(this.currentAccount).loadFile(tLRPC$TL_wallPaper.document, tLRPC$TL_wallPaper, 1, 1);
                            return;
                        }
                    }
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    openThemeAccentPreview(tLRPC$TL_theme, tLRPC$TL_wallPaper, theme);
                }
                c = 1;
            } else {
                TLRPC$Document tLRPC$Document = tLRPC$TL_theme.document;
                if (tLRPC$Document != null) {
                    this.loadingThemeAccent = false;
                    this.loadingTheme = tLRPC$TL_theme;
                    this.loadingThemeFileName = FileLoader.getAttachFileName(tLRPC$Document);
                    this.loadingThemeProgressDialog = alertDialog;
                    FileLoader.getInstance(this.currentAccount).loadFile(this.loadingTheme.document, tLRPC$TL_theme, 1, 1);
                }
                c = 1;
            }
        } else {
            if (tLRPC$TL_error == null || !"THEME_FORMAT_INVALID".equals(tLRPC$TL_error.text)) {
                c = 2;
            }
            c = 1;
        }
        if (c != 0) {
            try {
                runnable.run();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            if (c == 1) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", R.string.Theme), LocaleController.getString("ThemeNotSupported", R.string.ThemeNotSupported)));
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", R.string.Theme), LocaleController.getString("ThemeNotFound", R.string.ThemeNotFound)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$79(final int[] iArr, final int i, final Runnable runnable, final Integer num, final Integer num2, final Integer num3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$78(tLObject, iArr, i, runnable, num, num2, num3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$78(TLObject tLObject, int[] iArr, int i, Runnable runnable, Integer num, Integer num2, Integer num3) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                iArr[0] = runCommentRequest(i, runnable, num, num2, num3, tLRPC$TL_messages_chats.chats.get(0));
                if (z) {
                    return;
                }
                try {
                    runnable.run();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
                return;
            }
        }
        z = true;
        if (z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$80(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$83(final Bundle bundle, final Long l, int[] iArr, final Runnable runnable, final Integer num, final Integer num2, final BaseFragment baseFragment, final int i) {
        if (this.actionBarLayout.presentFragment(new ChatActivity(bundle))) {
            return;
        }
        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
        tLRPC$TL_inputChannel.channel_id = l.longValue();
        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$82(runnable, num, l, num2, baseFragment, i, bundle, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$82(final Runnable runnable, final Integer num, final Long l, final Integer num2, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$81(runnable, tLObject, num, l, num2, baseFragment, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$81(Runnable runnable, TLObject tLObject, Integer num, Long l, Integer num2, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        boolean z = true;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_chats.chats.get(0);
                if (tLRPC$Chat != null && tLRPC$Chat.forum) {
                    if (num != null) {
                        openForumFromLink(-l.longValue(), num.intValue(), num2, null);
                    } else {
                        openForumFromLink(-l.longValue(), num2.intValue(), null, null);
                    }
                }
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    this.actionBarLayout.presentFragment(new ChatActivity(bundle));
                }
                z = false;
            }
        }
        if (z) {
            showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$84(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$85(int i, int[] iArr, Runnable runnable) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void openForumFromLink(final long j, int i, final Integer num, final Runnable runnable) {
        if (num == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            lambda$runLinkRequest$72(new TopicsFragment(bundle));
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
        tLRPC$TL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(-j);
        tLRPC$TL_channels_getMessages.id.add(num);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openForumFromLink$87(num, j, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$87(final Integer num, final long j, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openForumFromLink$86(tLObject, num, j, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$86(TLObject tLObject, Integer num, long j, Runnable runnable) {
        TLRPC$Message tLRPC$Message;
        if (tLObject instanceof TLRPC$messages_Messages) {
            ArrayList<TLRPC$Message> arrayList = ((TLRPC$messages_Messages) tLObject).messages;
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) != null && arrayList.get(i).id == num.intValue()) {
                    tLRPC$Message = arrayList.get(i);
                    break;
                }
            }
        }
        tLRPC$Message = null;
        if (tLRPC$Message != null) {
            runCommentRequest(this.currentAccount, null, Integer.valueOf(tLRPC$Message.id), null, Integer.valueOf(MessageObject.getTopicId(tLRPC$Message, MessagesController.getInstance(this.currentAccount).isForum(tLRPC$Message))), MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)), runnable);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$72(new TopicsFragment(bundle));
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<TLRPC$TL_contact> findContacts(String str, String str2, boolean z) {
        String str3;
        String lowerCase;
        TLRPC$User user;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
        ArrayList arrayList = new ArrayList(contactsController.contacts);
        ArrayList arrayList2 = new ArrayList();
        String str4 = null;
        int i = 0;
        if (str2 != null) {
            String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
            TLRPC$TL_contact tLRPC$TL_contact = contactsController.contactsByPhone.get(stripExceptNumbers);
            if (tLRPC$TL_contact == null) {
                tLRPC$TL_contact = contactsController.contactsByShortPhone.get(stripExceptNumbers.substring(Math.max(0, stripExceptNumbers.length() - 7)));
            }
            if (tLRPC$TL_contact != null) {
                TLRPC$User user2 = messagesController.getUser(Long.valueOf(tLRPC$TL_contact.user_id));
                if (user2 != null && (!user2.self || z)) {
                    arrayList2.add(tLRPC$TL_contact);
                } else {
                    str3 = null;
                    if (arrayList2.isEmpty() && str3 != null) {
                        lowerCase = str3.trim().toLowerCase();
                        if (!TextUtils.isEmpty(lowerCase)) {
                            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                            translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
                            int i2 = 2;
                            char c = 1;
                            String[] strArr = {lowerCase, translitString};
                            int size = arrayList.size();
                            int i3 = 0;
                            while (i3 < size) {
                                TLRPC$TL_contact tLRPC$TL_contact2 = (TLRPC$TL_contact) arrayList.get(i3);
                                if (tLRPC$TL_contact2 != null && (user = messagesController.getUser(Long.valueOf(tLRPC$TL_contact2.user_id))) != null && (!user.self || z)) {
                                    String[] strArr2 = new String[3];
                                    strArr2[i] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                                    strArr2[c] = LocaleController.getInstance().getTranslitString(strArr2[i]);
                                    if (strArr2[i].equals(strArr2[c])) {
                                        strArr2[c] = str4;
                                    }
                                    if (UserObject.isReplyUser(user)) {
                                        strArr2[i2] = LocaleController.getString("RepliesTitle", R.string.RepliesTitle).toLowerCase();
                                    } else if (user.self) {
                                        strArr2[i2] = LocaleController.getString("SavedMessages", R.string.SavedMessages).toLowerCase();
                                    }
                                    int i4 = 0;
                                    boolean z2 = false;
                                    while (true) {
                                        if (i4 >= i2) {
                                            break;
                                        }
                                        String str5 = strArr[i4];
                                        if (str5 != null) {
                                            for (int i5 = 3; i < i5; i5 = 3) {
                                                String str6 = strArr2[i];
                                                if (str6 != null) {
                                                    if (!str6.startsWith(str5)) {
                                                        if (str6.contains(" " + str5)) {
                                                        }
                                                    }
                                                    z2 = true;
                                                    break;
                                                }
                                                i++;
                                            }
                                            String publicUsername = UserObject.getPublicUsername(user);
                                            if (!z2 && publicUsername != null && publicUsername.startsWith(str5)) {
                                                z2 = true;
                                            }
                                            if (z2) {
                                                arrayList2.add(tLRPC$TL_contact2);
                                                break;
                                            }
                                        }
                                        i4++;
                                        i = 0;
                                        i2 = 2;
                                    }
                                }
                                i3++;
                                c = 1;
                                str4 = null;
                                i = 0;
                                i2 = 2;
                            }
                        }
                    }
                    return arrayList2;
                }
            }
        }
        str3 = str;
        if (arrayList2.isEmpty()) {
            lowerCase = str3.trim().toLowerCase();
            if (!TextUtils.isEmpty(lowerCase)) {
            }
        }
        return arrayList2;
    }

    private void createUpdateUI() {
        if (this.sideMenuContainer == null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.20
            private int lastGradientWidth;
            private LinearGradient updateGradient;
            private Paint paint = new Paint();
            private Matrix matrix = new Matrix();

            @Override // android.view.View
            public void draw(Canvas canvas) {
                if (this.updateGradient != null) {
                    this.paint.setColor(-1);
                    this.paint.setShader(this.updateGradient);
                    this.updateGradient.setLocalMatrix(this.matrix);
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
                    LaunchActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                    LaunchActivity.this.updateLayoutIcon.draw(canvas);
                }
                super.draw(canvas);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
                int size = View.MeasureSpec.getSize(i);
                if (this.lastGradientWidth != size) {
                    this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    this.lastGradientWidth = size;
                }
            }
        };
        this.updateLayout = frameLayout;
        frameLayout.setWillNotDraw(false);
        this.updateLayout.setVisibility(4);
        this.updateLayout.setTranslationY(AndroidUtilities.dp(44.0f));
        if (Build.VERSION.SDK_INT >= 21) {
            this.updateLayout.setBackground(Theme.getSelectorDrawable(1090519039, false));
        }
        this.sideMenuContainer.addView(this.updateLayout, LayoutHelper.createFrame(-1, 44, 83));
        this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.lambda$createUpdateUI$88(view);
            }
        });
        RadialProgress2 radialProgress2 = new RadialProgress2(this.updateLayout);
        this.updateLayoutIcon = radialProgress2;
        radialProgress2.setColors(-1, -1, -1, -1);
        this.updateLayoutIcon.setProgressRect(AndroidUtilities.dp(22.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(44.0f), AndroidUtilities.dp(33.0f));
        this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
        this.updateLayoutIcon.setAsMini();
        SimpleTextView simpleTextView = new SimpleTextView(this);
        this.updateTextView = simpleTextView;
        simpleTextView.setTextSize(15);
        this.updateTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.updateTextView.setText(LocaleController.getString("AppUpdate", R.string.AppUpdate));
        this.updateTextView.setTextColor(-1);
        this.updateTextView.setGravity(3);
        this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 16, 74.0f, 0.0f, 0.0f, 0.0f));
        TextView textView = new TextView(this);
        this.updateSizeTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.updateSizeTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.updateSizeTextView.setGravity(5);
        this.updateSizeTextView.setTextColor(-1);
        this.updateLayout.addView(this.updateSizeTextView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 17.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createUpdateUI$88(View view) {
        if (SharedConfig.isAppUpdateAvailable()) {
            if (this.updateLayoutIcon.getIcon() == 2) {
                FileLoader.getInstance(this.currentAccount).loadFile(SharedConfig.pendingAppUpdate.document, "update", 1, 1);
                updateAppUpdateViews(true);
            } else if (this.updateLayoutIcon.getIcon() == 3) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile(SharedConfig.pendingAppUpdate.document);
                updateAppUpdateViews(true);
            } else {
                AndroidUtilities.openForView(SharedConfig.pendingAppUpdate.document, true, (Activity) this);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0130 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0131  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAppUpdateViews(boolean z) {
        boolean z2;
        if (this.sideMenuContainer == null) {
            return;
        }
        if (SharedConfig.isAppUpdateAvailable()) {
            final FrameLayout frameLayout = this.updateLayout;
            createUpdateUI();
            this.updateSizeTextView.setText(AndroidUtilities.formatFileSize(SharedConfig.pendingAppUpdate.document.size));
            String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            if (FileLoader.getInstance(this.currentAccount).getPathToAttach(SharedConfig.pendingAppUpdate.document, true).exists()) {
                this.updateLayoutIcon.setIcon(15, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow));
            } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(attachFileName)) {
                this.updateLayoutIcon.setIcon(3, true, false);
                this.updateLayoutIcon.setProgress(0.0f, false);
                Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                SimpleTextView simpleTextView = this.updateTextView;
                int i = R.string.AppUpdateDownloading;
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf((int) ((fileProgress != null ? fileProgress.floatValue() : 0.0f) * 100.0f));
                simpleTextView.setText(LocaleController.formatString("AppUpdateDownloading", i, objArr));
            } else {
                this.updateLayoutIcon.setIcon(2, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdate", R.string.AppUpdate));
                z2 = true;
                if (!z2) {
                    if (this.updateSizeTextView.getTag() != null) {
                        if (z) {
                            this.updateSizeTextView.setTag(null);
                            this.updateSizeTextView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(180L).start();
                        } else {
                            this.updateSizeTextView.setAlpha(1.0f);
                            this.updateSizeTextView.setScaleX(1.0f);
                            this.updateSizeTextView.setScaleY(1.0f);
                        }
                    }
                } else if (this.updateSizeTextView.getTag() == null) {
                    if (z) {
                        this.updateSizeTextView.setTag(1);
                        this.updateSizeTextView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(180L).start();
                    } else {
                        this.updateSizeTextView.setAlpha(0.0f);
                        this.updateSizeTextView.setScaleX(0.0f);
                        this.updateSizeTextView.setScaleY(0.0f);
                    }
                }
                if (this.updateLayout.getTag() == null) {
                    return;
                }
                this.updateLayout.setVisibility(0);
                this.updateLayout.setTag(1);
                if (z) {
                    this.updateLayout.animate().translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(null).setDuration(180L).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.lambda$updateAppUpdateViews$89(frameLayout);
                        }
                    }).start();
                } else {
                    this.updateLayout.setTranslationY(0.0f);
                    if (frameLayout != null) {
                        ((ViewGroup) frameLayout.getParent()).removeView(frameLayout);
                    }
                }
                this.sideMenu.setPadding(0, 0, 0, AndroidUtilities.dp(44.0f));
                return;
            }
            z2 = false;
            if (!z2) {
            }
            if (this.updateLayout.getTag() == null) {
            }
        } else {
            FrameLayout frameLayout2 = this.updateLayout;
            if (frameLayout2 == null || frameLayout2.getTag() == null) {
                return;
            }
            this.updateLayout.setTag(null);
            if (z) {
                this.updateLayout.animate().translationY(AndroidUtilities.dp(44.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.21
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (LaunchActivity.this.updateLayout.getTag() == null) {
                            LaunchActivity.this.updateLayout.setVisibility(4);
                        }
                    }
                }).setDuration(180L).start();
            } else {
                this.updateLayout.setTranslationY(AndroidUtilities.dp(44.0f));
                this.updateLayout.setVisibility(4);
            }
            this.sideMenu.setPadding(0, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateAppUpdateViews$89(View view) {
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void checkAppUpdate(boolean z) {
        if (z || !BuildVars.DEBUG_VERSION) {
            if (z || BuildVars.CHECK_UPDATES) {
                if (z || Math.abs(System.currentTimeMillis() - SharedConfig.lastUpdateCheckTime) >= MessagesController.getInstance(0).updateCheckDelay * 1000) {
                    TLRPC$TL_help_getAppUpdate tLRPC$TL_help_getAppUpdate = new TLRPC$TL_help_getAppUpdate();
                    try {
                        tLRPC$TL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                    } catch (Exception unused) {
                    }
                    if (tLRPC$TL_help_getAppUpdate.source == null) {
                        tLRPC$TL_help_getAppUpdate.source = "";
                    }
                    final int i = this.currentAccount;
                    ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$checkAppUpdate$91(i, tLObject, tLRPC$TL_error);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$91(final int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            final TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$90(tLRPC$TL_help_appUpdate, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$90(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i) {
        TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate2 = SharedConfig.pendingAppUpdate;
        if ((tLRPC$TL_help_appUpdate2 == null || !tLRPC$TL_help_appUpdate2.version.equals(tLRPC$TL_help_appUpdate.version)) && SharedConfig.setNewAppVersionAvailable(tLRPC$TL_help_appUpdate)) {
            if (tLRPC$TL_help_appUpdate.can_not_skip) {
                showUpdateActivity(i, tLRPC$TL_help_appUpdate, false);
            } else {
                this.drawerLayoutAdapter.notifyDataSetChanged();
                try {
                    new UpdateAppAlertDialog(this, tLRPC$TL_help_appUpdate, i).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable, new Object[0]);
        }
    }

    public AlertDialog showAlertDialog(AlertDialog.Builder builder) {
        try {
            AlertDialog alertDialog = this.visibleDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            AlertDialog show = builder.show();
            this.visibleDialog = show;
            show.setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$showAlertDialog$92(dialogInterface);
                }
            });
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlertDialog$92(DialogInterface dialogInterface) {
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                try {
                    Toast.makeText(this, getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), 1).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.localeDialog = null;
            } else if (alertDialog == this.proxyErrorDialog) {
                MessagesController.getGlobalMainSettings();
                SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.putBoolean("proxy_enabled", false);
                edit.putBoolean("proxy_enabled_calls", false);
                edit.commit();
                ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                this.proxyErrorDialog = null;
            }
        }
        this.visibleDialog = null;
    }

    public void showBulletin(Function<BulletinFactory, Bulletin> function) {
        BaseFragment baseFragment;
        if (!layerFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList = layerFragmentsStack;
            baseFragment = arrayList.get(arrayList.size() - 1);
        } else if (!rightFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        } else if (mainFragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            baseFragment = arrayList3.get(arrayList3.size() - 1);
        }
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            function.apply(BulletinFactory.of(baseFragment)).show();
        }
    }

    public void setNavigateToPremiumBot(boolean z) {
        this.navigateToPremiumBot = z;
    }

    public void setNavigateToPremiumGiftCallback(Runnable runnable) {
        this.navigateToPremiumGiftCallback = runnable;
    }

    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void onNewIntent(Intent intent, Browser.Progress progress) {
        super.onNewIntent(intent);
        lambda$handleIntent$15(intent, true, false, false, progress);
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public boolean didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<MessagesStorage.TopicKey> arrayList, final CharSequence charSequence, final boolean z, TopicsFragment topicsFragment) {
        ChatActivity chatActivity;
        boolean z2;
        ChatActivity chatActivity2;
        MessageObject messageObject;
        int i;
        long j;
        ChatActivity chatActivity3;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList2;
        TLRPC$TL_forumTopic findTopic;
        final int currentAccount = dialogsActivity != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        final Uri uri = this.exportingChatUri;
        if (uri != null) {
            final ArrayList arrayList3 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            final AlertDialog alertDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).dialogId, this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j2) {
                    LaunchActivity.this.lambda$didSelectDialogs$93(currentAccount, dialogsActivity, z, arrayList3, uri, alertDialog, j2);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            final boolean z7 = dialogsActivity == null || dialogsActivity.notify;
            if (arrayList.size() <= 1) {
                long j2 = arrayList.get(0).dialogId;
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(j2)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j2));
                } else if (DialogObject.isUserDialog(j2)) {
                    bundle.putLong("user_id", j2);
                } else {
                    bundle.putLong("chat_id", -j2);
                }
                if (!MessagesController.getInstance(currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
                    return false;
                }
                ChatActivity chatActivity4 = new ChatActivity(bundle);
                ForumUtilities.applyTopic(chatActivity4, arrayList.get(0));
                chatActivity = chatActivity4;
            } else {
                chatActivity = null;
            }
            ArrayList<TLRPC$User> arrayList4 = this.contactsToSend;
            int size = arrayList4 != null ? arrayList4.size() + 0 : 0;
            if (this.videoPath != null) {
                size++;
            }
            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList5 = this.photoPathsArray;
            if (arrayList5 != null) {
                size += arrayList5.size();
            }
            ArrayList<String> arrayList6 = this.documentsPathsArray;
            if (arrayList6 != null) {
                size += arrayList6.size();
            }
            ArrayList<Uri> arrayList7 = this.documentsUrisArray;
            if (arrayList7 != null) {
                size += arrayList7.size();
            }
            if (this.videoPath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                size++;
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, arrayList.get(i2).dialogId, size > 1)) {
                    return false;
                }
            }
            if (topicsFragment != null) {
                topicsFragment.removeSelfFromStack();
            }
            ArrayList<TLRPC$User> arrayList8 = this.contactsToSend;
            if (arrayList8 != null && arrayList8.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(arrayList9.get(arrayList9.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                final ChatActivity chatActivity5 = chatActivity;
                phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda107
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z8, int i3) {
                        LaunchActivity.this.lambda$didSelectDialogs$94(chatActivity5, arrayList, currentAccount, charSequence, z7, tLRPC$User, z8, i3);
                    }
                });
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                arrayList10.get(arrayList10.size() - 1).showDialog(phonebookShareAlert);
                chatActivity2 = chatActivity;
                z2 = true;
            } else {
                int i3 = 0;
                z2 = false;
                String str = null;
                while (i3 < arrayList.size()) {
                    long j3 = arrayList.get(i3).dialogId;
                    int i4 = arrayList.get(i3).topicId;
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (i4 == 0 || (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j3, i4)) == null || findTopic.topicStartMessage == null) {
                        messageObject = null;
                    } else {
                        messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                        messageObject.isTopicMainMessage = true;
                    }
                    if (chatActivity != null) {
                        i = i4;
                        j = j3;
                        boolean z8 = dialogsActivity == null || this.videoPath != null || ((arrayList2 = this.photoPathsArray) != null && arrayList2.size() > 0);
                        ChatActivity chatActivity6 = chatActivity;
                        this.actionBarLayout.presentFragment(chatActivity, dialogsActivity != null, z8, true, false);
                        boolean z9 = dialogsActivity != null;
                        String str2 = this.videoPath;
                        if (str2 != null) {
                            chatActivity6.openVideoEditor(str2, this.sendingText);
                            this.sendingText = null;
                            z5 = false;
                            z6 = true;
                        } else {
                            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList11 = this.photoPathsArray;
                            if (arrayList11 == null || arrayList11.size() <= 0) {
                                z5 = false;
                            } else {
                                z5 = chatActivity6.openPhotosEditor(this.photoPathsArray, (charSequence == null || charSequence.length() == 0) ? this.sendingText : charSequence);
                                if (z5) {
                                    this.sendingText = null;
                                }
                            }
                            z6 = false;
                        }
                        chatActivity3 = chatActivity6;
                        z2 = z9;
                        z3 = z5;
                        z4 = z6;
                    } else {
                        i = i4;
                        j = j3;
                        chatActivity3 = chatActivity;
                        if (this.videoPath != null) {
                            String str3 = this.sendingText;
                            if (str3 != null && str3.length() <= 1024) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                            ArrayList arrayList12 = new ArrayList();
                            arrayList12.add(this.videoPath);
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList12, arrayList12, null, str, null, j, messageObject, messageObject, null, null, z7, 0);
                        }
                        z3 = false;
                        z4 = false;
                    }
                    if (this.photoPathsArray != null && !z3) {
                        String str4 = this.sendingText;
                        if (str4 != null && str4.length() <= 1024 && this.photoPathsArray.size() == 1) {
                            this.photoPathsArray.get(0).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, messageObject, messageObject, null, false, false, null, z7, 0, false);
                    }
                    if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
                        String str5 = this.sendingText;
                        if (str5 != null && str5.length() <= 1024) {
                            ArrayList<String> arrayList13 = this.documentsPathsArray;
                            int size2 = arrayList13 != null ? arrayList13.size() : 0;
                            ArrayList<Uri> arrayList14 = this.documentsUrisArray;
                            if (size2 + (arrayList14 != null ? arrayList14.size() : 0) == 1) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                        }
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j, messageObject, messageObject, null, null, z7, 0);
                    }
                    String str6 = this.sendingText;
                    if (str6 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str6, j, i, z7, 0);
                    }
                    ArrayList<TLRPC$User> arrayList15 = this.contactsToSend;
                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                        for (int i5 = 0; i5 < this.contactsToSend.size(); i5++) {
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(this.contactsToSend.get(i5), j, messageObject, messageObject, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z7, 0);
                        }
                    }
                    if (!TextUtils.isEmpty(charSequence) && !z4 && !z3) {
                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, i, z7, 0);
                    }
                    i3++;
                    chatActivity = chatActivity3;
                }
                chatActivity2 = chatActivity;
            }
            if (dialogsActivity != null && chatActivity2 == null && !z2) {
                dialogsActivity.finishFragment();
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.sendingText = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
        this.exportingChatUri = null;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectDialogs$93(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
        if (j != 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (!AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
            if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setOpenImport();
            this.actionBarLayout.presentFragment(chatActivity, dialogsActivity != null || z, dialogsActivity == null, true, false);
        } else {
            this.documentsUrisArray = arrayList;
            if (arrayList == null) {
                this.documentsUrisArray = new ArrayList<>();
            }
            this.documentsUrisArray.add(0, uri);
            openDialogsToSend(true);
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectDialogs$94(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2) {
        TLRPC$TL_forumTopic findTopic;
        if (chatActivity != null) {
            this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long j = ((MessagesStorage.TopicKey) arrayList.get(i3)).dialogId;
            int i4 = ((MessagesStorage.TopicKey) arrayList.get(i3)).topicId;
            MessageObject messageObject = null;
            if (i4 != 0 && (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j, i4)) != null && findTopic.topicStartMessage != null) {
                messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                messageObject.isTopicMainMessage = true;
            }
            MessageObject messageObject2 = messageObject;
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$User, j, messageObject2, messageObject2, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2);
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, z, 0);
            }
        }
    }

    private void onFinish() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (this.finished) {
            return;
        }
        this.finished = true;
        int i = this.currentAccount;
        if (i != -1) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowPlayServicesAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.historyImportProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersImportComplete);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserShowLimitReachedDialog);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.appUpdateAvailable);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.requestPermissions);
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$72(BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return this.actionBarLayout.presentFragment(baseFragment, z, z2, true, false);
    }

    public INavigationLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public INavigationLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public INavigationLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        VoIPService sharedInstance;
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onActivityResult");
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (i == 105) {
            if (Build.VERSION.SDK_INT >= 23) {
                boolean canDrawOverlays = Settings.canDrawOverlays(this);
                ApplicationLoader.canDrawOverlays = canDrawOverlays;
                if (canDrawOverlays) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                    if (groupCallActivity != null) {
                        groupCallActivity.dismissInternal();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onActivityResult$95();
                        }
                    }, 200L);
                    return;
                }
                return;
            }
            return;
        }
        super.onActivityResult(i, i2, intent);
        if (i == 520) {
            if (i2 != -1 || (sharedInstance = VoIPService.getSharedInstance()) == null) {
                return;
            }
            VideoCapturerDevice.mediaProjectionPermissionResultData = intent;
            sharedInstance.createCaptureDevice(true);
        } else if (i == 140) {
            LocationController.getInstance(this.currentAccount).startFusedLocationRequest(i2 == -1);
        } else {
            ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
            if (themeEditorView != null) {
                themeEditorView.onActivityResult(i, i2, intent);
            }
            if (this.actionBarLayout.getFragmentStack().size() != 0) {
                this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1).onActivityResultFragment(i, i2, intent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.getFragmentStack().size() != 0) {
                    this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1).onActivityResultFragment(i, i2, intent);
                }
                if (this.layersActionBarLayout.getFragmentStack().size() != 0) {
                    this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1).onActivityResultFragment(i, i2, intent);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onActivityResultReceived, Integer.valueOf(i), Integer.valueOf(i2), intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$95() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (checkPermissionsResult(i, strArr, iArr)) {
            if (this.actionBarLayout.getFragmentStack().size() != 0) {
                this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.getFragmentStack().size() != 0) {
                    this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
                }
                if (this.layersActionBarLayout.getFragmentStack().size() != 0) {
                    this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
                }
            }
            VoIPFragment.onRequestPermissionsResult(i, strArr, iArr);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onRequestPermissionResultReceived, Integer.valueOf(i), strArr, iArr);
            if (this.requestedPermissions.get(i, -1) >= 0) {
                int i2 = this.requestedPermissions.get(i, -1);
                this.requestedPermissions.delete(i);
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.permissionsGranted, Integer.valueOf(i2));
            }
        }
    }

    @Override // android.app.Activity
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        INavigationLayout iNavigationLayout = this.actionBarLayout;
        if (iNavigationLayout != null) {
            iNavigationLayout.rebuildFragments(1);
        }
        INavigationLayout iNavigationLayout2 = this.rightActionBarLayout;
        if (iNavigationLayout2 != null) {
            iNavigationLayout2.rebuildFragments(1);
        }
        INavigationLayout iNavigationLayout3 = this.layersActionBarLayout;
        if (iNavigationLayout3 != null) {
            iNavigationLayout3.rebuildFragments(1);
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 4096);
        ApplicationLoader.mainInterfacePaused = true;
        final int i = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$96(i);
            }
        });
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            INavigationLayout iNavigationLayout = this.rightActionBarLayout;
            if (iNavigationLayout != null) {
                iNavigationLayout.onPause();
            }
            INavigationLayout iNavigationLayout2 = this.layersActionBarLayout;
            if (iNavigationLayout2 != null) {
                iNavigationLayout2.onPause();
            }
        }
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
        for (PasscodeView passcodeView2 : this.overlayPasscodeViews) {
            passcodeView2.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onPause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onPause$96(int i) {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
        if (VoIPService.getSharedInstance() == null) {
            MessagesController.getInstance(i).ignoreSetOnline = false;
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = false;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onResume();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = true;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onPause();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        if (PhotoViewer.getPipInstance() != null) {
            PhotoViewer.getPipInstance().destroyPhotoViewer();
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().destroyPhotoViewer();
        }
        if (SecretMediaViewer.hasInstance()) {
            SecretMediaViewer.getInstance().destroyPhotoViewer();
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().destroyArticleViewer();
        }
        if (ContentPreviewViewer.hasInstance()) {
            ContentPreviewViewer.getInstance().destroy();
        }
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.dismissInternal();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView(this.feedbackView, false);
        if (pipRoundVideoView != null) {
            pipRoundVideoView.close(false);
        }
        Theme.destroyResources();
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.destroy();
        }
        ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
        if (themeEditorView != null) {
            themeEditorView.destroy();
        }
        try {
            AlertDialog alertDialog = this.visibleDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        super.onDestroy();
        onFinish();
        FloatingDebugController.onDestroy();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        for (Runnable runnable : this.onUserLeaveHintListeners) {
            runnable.run();
        }
        INavigationLayout iNavigationLayout = this.actionBarLayout;
        if (iNavigationLayout != null) {
            iNavigationLayout.onUserLeaveHint();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        MessageObject playingMessageObject;
        super.onResume();
        isResumed = true;
        Runnable runnable = onResumeStaticCallback;
        if (runnable != null) {
            runnable.run();
            onResumeStaticCallback = null;
        }
        if (Theme.selectedAutoNightType == 3) {
            Theme.checkAutoNightThemeConditions();
        }
        checkWasMutedByAdmin(true);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 4096);
        MediaController mediaController = MediaController.getInstance();
        ViewGroup view = this.actionBarLayout.getView();
        this.feedbackView = view;
        mediaController.setFeedbackView(view, true);
        ApplicationLoader.mainInterfacePaused = false;
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda75.INSTANCE);
        checkFreeDiscSpace(0);
        MediaController.checkGallery();
        onPasscodeResume();
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView == null || passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                INavigationLayout iNavigationLayout = this.rightActionBarLayout;
                if (iNavigationLayout != null) {
                    iNavigationLayout.onResume();
                }
                INavigationLayout iNavigationLayout2 = this.layersActionBarLayout;
                if (iNavigationLayout2 != null) {
                    iNavigationLayout2.onResume();
                }
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                INavigationLayout iNavigationLayout3 = this.rightActionBarLayout;
                if (iNavigationLayout3 != null) {
                    iNavigationLayout3.dismissDialogs();
                }
                INavigationLayout iNavigationLayout4 = this.layersActionBarLayout;
                if (iNavigationLayout4 != null) {
                    iNavigationLayout4.dismissDialogs();
                }
            }
            this.passcodeView.onResume();
            for (PasscodeView passcodeView2 : this.overlayPasscodeViews) {
                passcodeView2.onResume();
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused() && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
            MediaController.getInstance().seekToProgress(playingMessageObject, playingMessageObject.audioProgress);
        }
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            int i = UserConfig.selectedAccount;
            showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
        } else {
            TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = SharedConfig.pendingAppUpdate;
            if (tLRPC$TL_help_appUpdate != null && tLRPC$TL_help_appUpdate.can_not_skip) {
                showUpdateActivity(UserConfig.selectedAccount, SharedConfig.pendingAppUpdate, true);
            }
        }
        checkAppUpdate(false);
        if (Build.VERSION.SDK_INT >= 23) {
            ApplicationLoader.canDrawOverlays = Settings.canDrawOverlays(this);
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onResume();
        }
        invalidateTabletMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$97() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    private void invalidateTabletMode() {
        Boolean wasTablet = AndroidUtilities.getWasTablet();
        if (wasTablet == null) {
            return;
        }
        AndroidUtilities.resetWasTabletFlag();
        if (wasTablet.booleanValue() != AndroidUtilities.isTablet()) {
            int i = 0;
            long j = 0;
            if (wasTablet.booleanValue()) {
                mainFragmentsStack.addAll(rightFragmentsStack);
                mainFragmentsStack.addAll(layerFragmentsStack);
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
            } else {
                ArrayList<BaseFragment> arrayList = new ArrayList(mainFragmentsStack);
                mainFragmentsStack.clear();
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
                long j2 = 0;
                for (BaseFragment baseFragment : arrayList) {
                    if (baseFragment instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) baseFragment;
                        if (dialogsActivity.isMainDialogList() && !dialogsActivity.isArchive()) {
                            mainFragmentsStack.add(baseFragment);
                        }
                    }
                    if (baseFragment instanceof ChatActivity) {
                        ChatActivity chatActivity = (ChatActivity) baseFragment;
                        if (!chatActivity.isInScheduleMode()) {
                            rightFragmentsStack.add(baseFragment);
                            if (j2 == 0) {
                                j2 = chatActivity.getDialogId();
                                i = chatActivity.getTopicId();
                            }
                        }
                    }
                    layerFragmentsStack.add(baseFragment);
                }
                j = j2;
            }
            setupActionBarLayout();
            this.actionBarLayout.rebuildFragments(1);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.rebuildFragments(1);
                this.layersActionBarLayout.rebuildFragments(1);
                Iterator<BaseFragment> it = mainFragmentsStack.iterator();
                while (it.hasNext()) {
                    BaseFragment next = it.next();
                    if (next instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity2 = (DialogsActivity) next;
                        if (dialogsActivity2.isMainDialogList()) {
                            dialogsActivity2.setOpenedDialogId(j, i);
                        }
                    }
                }
            }
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        AndroidUtilities.checkDisplaySize(this, configuration);
        super.onConfigurationChanged(configuration);
        checkLayout();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.onConfigurationChanged();
        }
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.onConfigurationChanged(configuration);
        }
        PhotoViewer pipInstance = PhotoViewer.getPipInstance();
        if (pipInstance != null) {
            pipInstance.onConfigurationChanged(configuration);
        }
        ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
        if (themeEditorView != null) {
            themeEditorView.onConfigurationChanged();
        }
        if (Theme.selectedAutoNightType == 3) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        AndroidUtilities.isInMultiwindow = z;
        checkLayout();
    }

    /* JADX WARN: Code restructure failed: missing block: B:120:0x031e, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L139;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x066b  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0673  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0684  */
    /* JADX WARN: Removed duplicated region for block: B:468:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, final int i2, Object... objArr) {
        BaseFragment baseFragment;
        int i3;
        String str;
        int i4;
        String str2;
        GroupCallActivity groupCallActivity;
        BaseFragment baseFragment2;
        boolean z;
        boolean z2;
        View childAt;
        BaseFragment baseFragment3;
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        boolean z3 = false;
        r4 = false;
        r4 = false;
        boolean z4 = false;
        r4 = false;
        boolean z5 = false;
        if (i == NotificationCenter.closeOtherAppActivities) {
            if (objArr[0] != this) {
                onFinish();
                finish();
            }
        } else if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = ConnectionsManager.getInstance(i2).getConnectionState();
            if (this.currentConnectionState != connectionState) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("switch to state " + connectionState);
                }
                this.currentConnectionState = connectionState;
                updateCurrentConnectionState(i2);
            }
        } else if (i == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.needShowAlert) {
            Integer num = (Integer) objArr[0];
            if (num.intValue() != 6) {
                if (num.intValue() != 3 || this.proxyErrorDialog == null) {
                    if (num.intValue() == 4) {
                        showTosActivity(i2, (TLRPC$TL_help_termsOfService) objArr[1]);
                        return;
                    }
                    if (mainFragmentsStack.isEmpty()) {
                        baseFragment3 = null;
                    } else {
                        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                        baseFragment3 = arrayList.get(arrayList.size() - 1);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    if (baseFragment3 != null) {
                        Map<String, Integer> hashMap = new HashMap<>();
                        hashMap.put("info1.**", Integer.valueOf(baseFragment3.getThemedColor("dialogTopBackground")));
                        hashMap.put("info2.**", Integer.valueOf(baseFragment3.getThemedColor("dialogTopBackground")));
                        builder.setTopAnimation(R.raw.not_available, 52, false, baseFragment3.getThemedColor("dialogTopBackground"), hashMap);
                        builder.setTopAnimationIsNew(true);
                    }
                    if (num.intValue() != 2 && num.intValue() != 3) {
                        builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda4
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                LaunchActivity.lambda$didReceivedNotification$98(i2, dialogInterface, i5);
                            }
                        });
                    }
                    if (num.intValue() == 5) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam3", R.string.NobodyLikesSpam3));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    } else if (num.intValue() == 0) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam1", R.string.NobodyLikesSpam1));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    } else if (num.intValue() == 1) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    } else if (num.intValue() == 2) {
                        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf((String) objArr[1]);
                        String str3 = (String) objArr[2];
                        if (str3.startsWith("PREMIUM_GIFT_SELF_REQUIRED_")) {
                            String str4 = (String) objArr[1];
                            int indexOf = str4.indexOf(42);
                            int i5 = indexOf + 1;
                            int indexOf2 = str4.indexOf(42, i5);
                            if (indexOf != -1 && indexOf2 != -1 && indexOf != indexOf2) {
                                valueOf.replace(indexOf, indexOf2 + 1, (CharSequence) str4.substring(i5, indexOf2));
                                valueOf.setSpan(new ClickableSpan() { // from class: org.telegram.ui.LaunchActivity.22
                                    @Override // android.text.style.ClickableSpan
                                    public void onClick(View view) {
                                        LaunchActivity.this.getActionBarLayout().presentFragment(new PremiumPreviewFragment("gift"));
                                    }

                                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                                    public void updateDrawState(TextPaint textPaint) {
                                        super.updateDrawState(textPaint);
                                        textPaint.setUnderlineText(false);
                                    }
                                }, indexOf, indexOf2 - 1, 33);
                            }
                        }
                        builder.setMessage(valueOf);
                        if (str3.startsWith("AUTH_KEY_DROP_")) {
                            builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i6) {
                                    LaunchActivity.this.lambda$didReceivedNotification$99(dialogInterface, i6);
                                }
                            });
                        } else if (str3.startsWith("PREMIUM_")) {
                            builder.setTitle(LocaleController.getString(R.string.TelegramPremium));
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        } else {
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        }
                    } else if (num.intValue() == 3) {
                        builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
                        builder.setMessage(LocaleController.getString("UseProxyTelegramError", R.string.UseProxyTelegramError));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        this.proxyErrorDialog = showAlertDialog(builder);
                        return;
                    }
                    if (mainFragmentsStack.isEmpty()) {
                        return;
                    }
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                    arrayList2.get(arrayList2.size() - 1).showDialog(builder.create());
                }
            }
        } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
            final HashMap hashMap2 = (HashMap) objArr[0];
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda11
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i6) {
                    LaunchActivity.this.lambda$didReceivedNotification$101(hashMap2, i2, dialogInterface, i6);
                }
            });
            builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", R.string.ShareYouLocationUnable));
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            arrayList3.get(arrayList3.size() - 1).showDialog(builder2.create());
        } else if (i == NotificationCenter.didSetNewWallpapper) {
            RecyclerListView recyclerListView = this.sideMenu;
            if (recyclerListView != null && (childAt = recyclerListView.getChildAt(0)) != null) {
                childAt.invalidate();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
            if (sizeNotifierFrameLayout != null) {
                sizeNotifierFrameLayout.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            }
        } else if (i == NotificationCenter.didSetPasscode) {
            if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                try {
                    getWindow().setFlags(8192, 8192);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (!AndroidUtilities.hasFlagSecureFragment()) {
                try {
                    getWindow().clearFlags(8192);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        } else if (i == NotificationCenter.reloadInterface) {
            if (mainFragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                if (arrayList4.get(arrayList4.size() - 1) instanceof ProfileActivity) {
                    z2 = true;
                    if (z2) {
                        ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                    }
                    z3 = z2;
                    rebuildAllFragments(z3);
                }
            }
            z2 = false;
            if (z2) {
            }
            z3 = z2;
            rebuildAllFragments(z3);
        } else if (i == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (i == NotificationCenter.openArticle) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArticleViewer articleViewer = ArticleViewer.getInstance();
            ArrayList<BaseFragment> arrayList6 = mainFragmentsStack;
            articleViewer.setParentActivity(this, arrayList6.get(arrayList6.size() - 1));
            ArticleViewer.getInstance().open((TLRPC$TL_webPage) objArr[0], (String) objArr[1]);
        } else if (i == NotificationCenter.hasNewContactsToImport) {
            INavigationLayout iNavigationLayout = this.actionBarLayout;
            if (iNavigationLayout == null || iNavigationLayout.getFragmentStack().isEmpty()) {
                return;
            }
            ((Integer) objArr[0]).intValue();
            final HashMap hashMap3 = (HashMap) objArr[1];
            final boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            final boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor("dialogTopBackground"));
            builder3.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
            builder3.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
            builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i6) {
                    LaunchActivity.lambda$didReceivedNotification$102(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i6) {
                    LaunchActivity.lambda$didReceivedNotification$103(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                }
            });
            builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i6) {
                    LaunchActivity.lambda$didReceivedNotification$104(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                }
            });
            AlertDialog create = builder3.create();
            this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1).showDialog(create);
            create.setCanceledOnTouchOutside(false);
        } else if (i == NotificationCenter.didSetNewTheme) {
            if (!((Boolean) objArr[0]).booleanValue()) {
                RecyclerListView recyclerListView2 = this.sideMenu;
                if (recyclerListView2 != null) {
                    recyclerListView2.setBackgroundColor(Theme.getColor("chats_menuBackground"));
                    this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
                    this.sideMenu.setListSelectorColor(Theme.getColor("listSelectorSDK21"));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor("actionBarDefault") | (-16777216)));
                    } catch (Exception unused) {
                    }
                }
            }
            this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
            boolean booleanValue3 = objArr.length > 1 ? ((Boolean) objArr[1]).booleanValue() : true;
            boolean z6 = objArr.length > 2 && ((Boolean) objArr[2]).booleanValue();
            if (booleanValue3 && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                z4 = true;
            }
            checkSystemBarColors(z6, true, z4);
        } else if (i == NotificationCenter.needSetDayNightTheme) {
            if (Build.VERSION.SDK_INT >= 21 && objArr[2] != null) {
                if (this.themeSwitchImageView.getVisibility() == 0) {
                    return;
                }
                try {
                    int[] iArr = (int[]) objArr[2];
                    final boolean booleanValue4 = ((Boolean) objArr[4]).booleanValue();
                    final RLottieImageView rLottieImageView = (RLottieImageView) objArr[5];
                    int measuredWidth = this.drawerLayoutContainer.getMeasuredWidth();
                    int measuredHeight = this.drawerLayoutContainer.getMeasuredHeight();
                    if (!booleanValue4) {
                        rLottieImageView.setVisibility(4);
                    }
                    this.rippleAbove = null;
                    if (objArr.length > 6) {
                        this.rippleAbove = (View) objArr[6];
                    }
                    this.isNavigationBarColorFrozen = true;
                    invalidateCachedViews(this.drawerLayoutContainer);
                    View view = this.rippleAbove;
                    if (view != null && view.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(0);
                    }
                    Bitmap snapshotView = AndroidUtilities.snapshotView(this.drawerLayoutContainer);
                    View view2 = this.rippleAbove;
                    if (view2 != null && view2.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(255);
                    }
                    this.frameLayout.removeView(this.themeSwitchImageView);
                    if (booleanValue4) {
                        this.frameLayout.addView(this.themeSwitchImageView, 0, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setVisibility(8);
                    } else {
                        this.frameLayout.addView(this.themeSwitchImageView, 1, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setTranslationX(iArr[0] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setTranslationY(iArr[1] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setVisibility(0);
                        this.themeSwitchSunView.invalidate();
                    }
                    this.themeSwitchImageView.setImageBitmap(snapshotView);
                    this.themeSwitchImageView.setVisibility(0);
                    this.themeSwitchSunDrawable = rLottieImageView.getAnimatedDrawable();
                    float max = Math.max((float) Math.max(Math.sqrt(((measuredWidth - iArr[0]) * (measuredWidth - iArr[0])) + ((measuredHeight - iArr[1]) * (measuredHeight - iArr[1]))), Math.sqrt((iArr[0] * iArr[0]) + ((measuredHeight - iArr[1]) * (measuredHeight - iArr[1])))), (float) Math.max(Math.sqrt(((measuredWidth - iArr[0]) * (measuredWidth - iArr[0])) + (iArr[1] * iArr[1])), Math.sqrt((iArr[0] * iArr[0]) + (iArr[1] * iArr[1]))));
                    View view3 = booleanValue4 ? this.drawerLayoutContainer : this.themeSwitchImageView;
                    int i6 = iArr[0];
                    int i7 = iArr[1];
                    float f = booleanValue4 ? 0.0f : max;
                    if (!booleanValue4) {
                        max = 0.0f;
                    }
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view3, i6, i7, f, max);
                    createCircularReveal.setDuration(400L);
                    createCircularReveal.setInterpolator(Easings.easeInOutQuad);
                    createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.23
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            LaunchActivity.this.rippleAbove = null;
                            LaunchActivity.this.drawerLayoutContainer.invalidate();
                            LaunchActivity.this.themeSwitchImageView.invalidate();
                            LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
                            LaunchActivity.this.themeSwitchImageView.setVisibility(8);
                            LaunchActivity.this.themeSwitchSunView.setVisibility(8);
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeAccentListUpdated, new Object[0]);
                            if (!booleanValue4) {
                                rLottieImageView.setVisibility(0);
                            }
                            DrawerProfileCell.switchingTheme = false;
                        }
                    });
                    if (this.rippleAbove != null) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                LaunchActivity.this.lambda$didReceivedNotification$105(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(createCircularReveal.getDuration());
                        ofFloat.start();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$didReceivedNotification$106();
                        }
                    }, booleanValue4 ? (measuredHeight - iArr[1]) / AndroidUtilities.dp(2.25f) : 50L);
                    createCircularReveal.start();
                    z = true;
                } catch (Throwable th) {
                    FileLog.e(th);
                    try {
                        this.themeSwitchImageView.setImageDrawable(null);
                        this.frameLayout.removeView(this.themeSwitchImageView);
                        DrawerProfileCell.switchingTheme = false;
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) objArr[0];
                boolean booleanValue5 = ((Boolean) objArr[1]).booleanValue();
                int intValue = ((Integer) objArr[3]).intValue();
                this.actionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z, objArr.length <= 7 ? (Runnable) objArr[7] : null);
                if (AndroidUtilities.isTablet()) {
                    return;
                }
                this.layersActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                this.rightActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                return;
            }
            DrawerProfileCell.switchingTheme = false;
            z = false;
            Theme.ThemeInfo themeInfo2 = (Theme.ThemeInfo) objArr[0];
            boolean booleanValue52 = ((Boolean) objArr[1]).booleanValue();
            int intValue2 = ((Integer) objArr[3]).intValue();
            this.actionBarLayout.animateThemedValues(themeInfo2, intValue2, booleanValue52, z, objArr.length <= 7 ? (Runnable) objArr[7] : null);
            if (AndroidUtilities.isTablet()) {
            }
        } else if (i == NotificationCenter.notificationsCountUpdated) {
            RecyclerListView recyclerListView3 = this.sideMenu;
            if (recyclerListView3 != null) {
                Integer num2 = (Integer) objArr[0];
                int childCount = recyclerListView3.getChildCount();
                for (int i8 = 0; i8 < childCount; i8++) {
                    View childAt2 = this.sideMenu.getChildAt(i8);
                    if ((childAt2 instanceof DrawerUserCell) && ((DrawerUserCell) childAt2).getAccountNumber() == num2.intValue()) {
                        childAt2.invalidate();
                        return;
                    }
                }
            }
        } else if (i == NotificationCenter.needShowPlayServicesAlert) {
            try {
                ((Status) objArr[0]).startResolutionForResult(this, 140);
            } catch (Throwable unused2) {
            }
        } else if (i == NotificationCenter.fileLoaded) {
            String str5 = (String) objArr[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str5)) {
                updateAppUpdateViews(true);
            }
            String str6 = this.loadingThemeFileName;
            if (str6 != null) {
                if (str6.equals(str5)) {
                    this.loadingThemeFileName = null;
                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
                    TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
                    final Theme.ThemeInfo fillThemeValues = Theme.fillThemeValues(file, tLRPC$TL_theme.title, tLRPC$TL_theme);
                    if (fillThemeValues != null) {
                        if (fillThemeValues.pathToWallpaper != null && !new File(fillThemeValues.pathToWallpaper).exists()) {
                            TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                            TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                            tLRPC$TL_inputWallPaperSlug.slug = fillThemeValues.slug;
                            tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                            ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$didReceivedNotification$108(fillThemeValues, tLObject, tLRPC$TL_error);
                                }
                            });
                            return;
                        }
                        TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                        if (applyThemeFile != null) {
                            lambda$runLinkRequest$72(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
                        }
                    }
                    onThemeLoadFinish();
                    return;
                }
                return;
            }
            String str7 = this.loadingThemeWallpaperName;
            if (str7 == null || !str7.equals(str5)) {
                return;
            }
            this.loadingThemeWallpaperName = null;
            final File file2 = (File) objArr[1];
            if (this.loadingThemeAccent) {
                openThemeAccentPreview(this.loadingTheme, this.loadingThemeWallpaper, this.loadingThemeInfo);
                onThemeLoadFinish();
                return;
            }
            final Theme.ThemeInfo themeInfo3 = this.loadingThemeInfo;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$didReceivedNotification$110(themeInfo3, file2);
                }
            });
        } else if (i == NotificationCenter.fileLoadFailed) {
            String str8 = (String) objArr[0];
            if (str8.equals(this.loadingThemeFileName) || str8.equals(this.loadingThemeWallpaperName)) {
                onThemeLoadFinish();
            }
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str8)) {
                updateAppUpdateViews(true);
            }
        } else if (i == NotificationCenter.screenStateChanged) {
            if (ApplicationLoader.mainInterfacePaused) {
                return;
            }
            if (ApplicationLoader.isScreenOn) {
                onPasscodeResume();
            } else {
                onPasscodePause();
            }
        } else if (i == NotificationCenter.needCheckSystemBarColors) {
            if (objArr.length > 0 && ((Boolean) objArr[0]).booleanValue()) {
                z5 = true;
            }
            checkSystemBarColors(z5);
        } else if (i == NotificationCenter.historyImportProgressChanged) {
            if (objArr.length <= 1 || mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
            AlertsCreator.processError(this.currentAccount, (TLRPC$TL_error) objArr[2], arrayList7.get(arrayList7.size() - 1), (TLObject) objArr[1], new Object[0]);
        } else if (i == NotificationCenter.stickersImportComplete) {
            MediaDataController mediaDataController = MediaDataController.getInstance(i2);
            TLObject tLObject = (TLObject) objArr[0];
            if (mainFragmentsStack.isEmpty()) {
                baseFragment2 = null;
            } else {
                ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                baseFragment2 = arrayList8.get(arrayList8.size() - 1);
            }
            mediaDataController.toggleStickerSet(this, tLObject, 2, baseFragment2, false, true);
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            this.sideMenu.invalidateViews();
        } else if (i == NotificationCenter.showBulletin) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            int intValue3 = ((Integer) objArr[0]).intValue();
            BottomSheet.ContainerView container = (!GroupCallActivity.groupCallUiVisible || (groupCallActivity = GroupCallActivity.groupCallInstance) == null) ? null : groupCallActivity.getContainer();
            if (container == null) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                baseFragment = arrayList9.get(arrayList9.size() - 1);
            } else {
                baseFragment = null;
            }
            int i9 = 1500;
            switch (intValue3) {
                case 0:
                    TLRPC$Document tLRPC$Document = (TLRPC$Document) objArr[1];
                    int intValue4 = ((Integer) objArr[2]).intValue();
                    StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, tLRPC$Document, null);
                    i9 = (intValue4 == 6 || intValue4 == 7) ? 3500 : 3500;
                    if (baseFragment != null) {
                        Bulletin.make(baseFragment, stickerSetBulletinLayout, i9).show();
                        return;
                    } else {
                        Bulletin.make(container, stickerSetBulletinLayout, i9).show();
                        return;
                    }
                case 1:
                    if (baseFragment != null) {
                        BulletinFactory.of(baseFragment).createErrorBulletin((String) objArr[1]).show();
                        return;
                    } else {
                        BulletinFactory.of(container, null).createErrorBulletin((String) objArr[1]).show();
                        return;
                    }
                case 2:
                    if (((Long) objArr[1]).longValue() > 0) {
                        i3 = R.string.YourBioChanged;
                        str = "YourBioChanged";
                    } else {
                        i3 = R.string.ChannelDescriptionChanged;
                        str = "CannelDescriptionChanged";
                    }
                    (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str, i3)).show();
                    return;
                case 3:
                    if (((Long) objArr[1]).longValue() > 0) {
                        i4 = R.string.YourNameChanged;
                        str2 = "YourNameChanged";
                    } else {
                        i4 = R.string.ChannelTitleChanged;
                        str2 = "CannelTitleChanged";
                    }
                    (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str2, i4)).show();
                    return;
                case 4:
                    if (baseFragment != null) {
                        BulletinFactory.of(baseFragment).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], baseFragment.getResourceProvider()).show();
                        return;
                    } else {
                        BulletinFactory.of(container, null).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], null).show();
                        return;
                    }
                case 5:
                    AppIconBulletinLayout appIconBulletinLayout = new AppIconBulletinLayout(this, (LauncherIconController.LauncherIcon) objArr[1], null);
                    if (baseFragment != null) {
                        Bulletin.make(baseFragment, appIconBulletinLayout, 1500).show();
                        return;
                    } else {
                        Bulletin.make(container, appIconBulletinLayout, 1500).show();
                        return;
                    }
                case 6:
                    if (baseFragment != null) {
                        BulletinFactory.of(baseFragment).createSuccessBulletin((String) objArr[1]).show();
                        return;
                    } else {
                        BulletinFactory.of(container, null).createSuccessBulletin((String) objArr[1]).show();
                        return;
                    }
                default:
                    return;
            }
        } else {
            String[] strArr = null;
            if (i == NotificationCenter.groupCallUpdated) {
                checkWasMutedByAdmin(false);
            } else if (i == NotificationCenter.fileLoadProgressChanged) {
                if (this.updateTextView == null || !SharedConfig.isAppUpdateAvailable()) {
                    return;
                }
                String str9 = (String) objArr[0];
                String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
                if (attachFileName == null || !attachFileName.equals(str9)) {
                    return;
                }
                float longValue = ((float) ((Long) objArr[1]).longValue()) / ((float) ((Long) objArr[2]).longValue());
                this.updateLayoutIcon.setProgress(longValue, true);
                this.updateTextView.setText(LocaleController.formatString("AppUpdateDownloading", R.string.AppUpdateDownloading, Integer.valueOf((int) (longValue * 100.0f))));
            } else if (i == NotificationCenter.appUpdateAvailable) {
                updateAppUpdateViews(mainFragmentsStack.size() == 1);
            } else if (i == NotificationCenter.currentUserShowLimitReachedDialog) {
                if (mainFragmentsStack.isEmpty()) {
                    return;
                }
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                BaseFragment baseFragment4 = arrayList10.get(arrayList10.size() - 1);
                if (baseFragment4.getParentActivity() != null) {
                    baseFragment4.showDialog(new LimitReachedBottomSheet(baseFragment4, baseFragment4.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount));
                }
            } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
                DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
                if (drawerLayoutAdapter != null) {
                    drawerLayoutAdapter.notifyDataSetChanged();
                }
                MessagesController.getMainSettings(this.currentAccount).edit().remove("transcribeButtonPressed").apply();
            } else if (i == NotificationCenter.requestPermissions) {
                int intValue5 = ((Integer) objArr[0]).intValue();
                if (intValue5 == 0 && Build.VERSION.SDK_INT >= 31) {
                    strArr = new String[]{"android.permission.BLUETOOTH_CONNECT"};
                }
                if (strArr != null) {
                    int i10 = this.requsetPermissionsPointer + 1;
                    this.requsetPermissionsPointer = i10;
                    this.requestedPermissions.put(i10, intValue5);
                    ActivityCompat.requestPermissions(this, strArr, this.requsetPermissionsPointer);
                }
            } else if (i == NotificationCenter.chatSwithcedToForum) {
                ForumUtilities.switchAllFragmentsInStackToForum(((Long) objArr[0]).longValue(), this.actionBarLayout);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$98(int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(i);
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$99(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$101(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (AndroidUtilities.isMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            LocationActivity locationActivity = new LocationActivity(0);
            locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda117
                @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
                public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i3, boolean z, int i4) {
                    LaunchActivity.lambda$didReceivedNotification$100(hashMap, i, tLRPC$MessageMedia, i3, z, i4);
                }
            });
            lambda$runLinkRequest$72(locationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$100(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$102(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$103(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$104(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$105(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$106() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$108(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$107(tLObject, themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$107(TLObject tLObject, Theme.ThemeInfo themeInfo) {
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
            this.loadingThemeInfo = themeInfo;
            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
            this.loadingThemeWallpaper = tLRPC$TL_wallPaper;
            FileLoader.getInstance(themeInfo.account).loadFile(tLRPC$TL_wallPaper.document, tLRPC$TL_wallPaper, 1, 1);
            return;
        }
        onThemeLoadFinish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$110(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$109();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$109() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$72(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
        }
        onThemeLoadFinish();
    }

    private void invalidateCachedViews(View view) {
        if (view.getLayerType() != 0) {
            view.invalidate();
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                invalidateCachedViews(viewGroup.getChildAt(i));
            }
        }
    }

    private void checkWasMutedByAdmin(boolean z) {
        ChatObject.Call call;
        long j;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z2 = false;
        if (sharedInstance != null && (call = sharedInstance.groupCall) != null) {
            boolean z3 = this.wasMutedByAdminRaisedHand;
            TLRPC$InputPeer groupCallPeer = sharedInstance.getGroupCallPeer();
            if (groupCallPeer != null) {
                j = groupCallPeer.user_id;
                if (j == 0) {
                    long j2 = groupCallPeer.chat_id;
                    if (j2 == 0) {
                        j2 = groupCallPeer.channel_id;
                    }
                    j = -j2;
                }
            } else {
                j = UserConfig.getInstance(this.currentAccount).clientUserId;
            }
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = call.participants.get(j);
            boolean z4 = (tLRPC$TL_groupCallParticipant == null || tLRPC$TL_groupCallParticipant.can_self_unmute || !tLRPC$TL_groupCallParticipant.muted) ? false : true;
            if (z4 && tLRPC$TL_groupCallParticipant.raise_hand_rating != 0) {
                z2 = true;
            }
            this.wasMutedByAdminRaisedHand = z2;
            if (z || !z3 || z2 || z4 || GroupCallActivity.groupCallInstance != null) {
                return;
            }
            showVoiceChatTooltip(38);
            return;
        }
        this.wasMutedByAdminRaisedHand = false;
    }

    private void showVoiceChatTooltip(int i) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || mainFragmentsStack.isEmpty() || sharedInstance.groupCall == null) {
            return;
        }
        TLRPC$Chat chat = sharedInstance.getChat();
        BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-chat.id)) {
                chat = null;
            }
            UndoView undoView = chatActivity.getUndoView();
            if (undoView != null) {
                undoView.showWithAction(0L, i, chat);
            }
        } else if (baseFragment instanceof DialogsActivity) {
            ((DialogsActivity) baseFragment).getUndoView().showWithAction(0L, i, chat);
        } else if (baseFragment instanceof ProfileActivity) {
            ((ProfileActivity) baseFragment).getUndoView().showWithAction(0L, i, chat);
        }
        if (i != 38 || VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService.getSharedInstance().playAllowTalkSound();
    }

    private String getStringForLanguageAlert(HashMap<String, String> hashMap, String str, int i) {
        String str2 = hashMap.get(str);
        return str2 == null ? LocaleController.getString(str, i) : str2;
    }

    private void openThemeAccentPreview(TLRPC$TL_theme tLRPC$TL_theme, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, Theme.ThemeInfo themeInfo) {
        int i = themeInfo.lastAccentId;
        Theme.ThemeAccent createNewAccent = themeInfo.createNewAccent(tLRPC$TL_theme, this.currentAccount);
        themeInfo.prevAccentId = themeInfo.currentAccentId;
        themeInfo.setCurrentAccentId(createNewAccent.id);
        createNewAccent.pattern = tLRPC$TL_wallPaper;
        lambda$runLinkRequest$72(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
    }

    private void onThemeLoadFinish() {
        AlertDialog alertDialog = this.loadingThemeProgressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } finally {
                this.loadingThemeProgressDialog = null;
            }
        }
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeFileName = null;
        this.loadingTheme = null;
    }

    private void checkFreeDiscSpace(final int i) {
        staticInstanceForAlerts = this;
        AutoDeleteMediaTask.run();
        SharedConfig.checkLogsToDelete();
        if ((Build.VERSION.SDK_INT < 26 || i != 0) && !this.checkFreeDiscSpaceShown) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$113(i);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$113(int i) {
        File directory;
        long availableBlocksLong;
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if ((i == 2 || ((i == 1 && Math.abs(this.alreadyShownFreeDiscSpaceAlertForced - System.currentTimeMillis()) > 240000) || Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) >= 259200000)) && (directory = FileLoader.getDirectory(4)) != null) {
                    StatFs statFs = new StatFs(directory.getAbsolutePath());
                    if (Build.VERSION.SDK_INT < 18) {
                        availableBlocksLong = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
                    } else {
                        availableBlocksLong = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                    }
                    if (i > 0 || availableBlocksLong < 52428800) {
                        if (i > 0) {
                            this.alreadyShownFreeDiscSpaceAlertForced = System.currentTimeMillis();
                        }
                        globalMainSettings.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkFreeDiscSpace$112();
                            }
                        });
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$112() {
        if (this.checkFreeDiscSpaceShown) {
            return;
        }
        try {
            Dialog createFreeSpaceDialog = AlertsCreator.createFreeSpaceDialog(this);
            createFreeSpaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$111(dialogInterface);
                }
            });
            this.checkFreeDiscSpaceShown = true;
            createFreeSpaceDialog.show();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$111(DialogInterface dialogInterface) {
        this.checkFreeDiscSpaceShown = false;
    }

    public static void checkFreeDiscSpaceStatic(int i) {
        LaunchActivity launchActivity = staticInstanceForAlerts;
        if (launchActivity != null) {
            launchActivity.checkFreeDiscSpace(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c A[Catch: Exception -> 0x011e, TRY_ENTER, TryCatch #0 {Exception -> 0x011e, blocks: (B:3:0x0007, B:5:0x0010, B:10:0x001e, B:14:0x0056, B:18:0x005e, B:22:0x0065, B:26:0x006c, B:30:0x0080, B:34:0x00a0, B:35:0x00be), top: B:40:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showLanguageAlertInternal(LocaleController.LocaleInfo localeInfo, LocaleController.LocaleInfo localeInfo2, String str) {
        boolean z;
        int i;
        try {
            this.loadingLocaleDialog = false;
            LocaleController.LocaleInfo localeInfo3 = localeInfo;
            if (!localeInfo3.builtIn && !LocaleController.getInstance().isCurrentLocalLocale()) {
                z = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                HashMap<String, String> hashMap = this.systemLocaleStrings;
                int i2 = R.string.ChooseYourLanguage;
                builder.setTitle(getStringForLanguageAlert(hashMap, "ChooseYourLanguage", i2));
                builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", i2));
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                final LanguageCell[] languageCellArr = new LanguageCell[2];
                final LocaleController.LocaleInfo[] localeInfoArr = new LocaleController.LocaleInfo[1];
                LocaleController.LocaleInfo[] localeInfoArr2 = new LocaleController.LocaleInfo[2];
                String stringForLanguageAlert = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
                localeInfoArr2[0] = !z ? localeInfo3 : localeInfo2;
                localeInfoArr2[1] = !z ? localeInfo2 : localeInfo3;
                if (z) {
                    localeInfo3 = localeInfo2;
                }
                localeInfoArr[0] = localeInfo3;
                i = 0;
                while (i < 2) {
                    languageCellArr[i] = new LanguageCell(this);
                    languageCellArr[i].setLanguage(localeInfoArr2[i], localeInfoArr2[i] == localeInfo2 ? stringForLanguageAlert : null, true);
                    languageCellArr[i].setTag(Integer.valueOf(i));
                    languageCellArr[i].setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                    languageCellArr[i].setLanguageSelected(i == 0, false);
                    linearLayout.addView(languageCellArr[i], LayoutHelper.createLinear(-1, 50));
                    languageCellArr[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda20
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LaunchActivity.lambda$showLanguageAlertInternal$114(localeInfoArr, languageCellArr, view);
                        }
                    });
                    i++;
                }
                LanguageCell languageCell = new LanguageCell(this);
                HashMap<String, String> hashMap2 = this.systemLocaleStrings;
                int i3 = R.string.ChooseYourLanguageOther;
                languageCell.setValue(getStringForLanguageAlert(hashMap2, "ChooseYourLanguageOther", i3), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i3));
                languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$115(view);
                    }
                });
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$116(localeInfoArr, dialogInterface, i4);
                    }
                });
                this.localeDialog = showAlertDialog(builder);
                MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
            }
            z = true;
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            HashMap<String, String> hashMap3 = this.systemLocaleStrings;
            int i22 = R.string.ChooseYourLanguage;
            builder2.setTitle(getStringForLanguageAlert(hashMap3, "ChooseYourLanguage", i22));
            builder2.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", i22));
            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(1);
            final LanguageCell[] languageCellArr2 = new LanguageCell[2];
            final LocaleController.LocaleInfo[] localeInfoArr3 = new LocaleController.LocaleInfo[1];
            LocaleController.LocaleInfo[] localeInfoArr22 = new LocaleController.LocaleInfo[2];
            String stringForLanguageAlert2 = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
            localeInfoArr22[0] = !z ? localeInfo3 : localeInfo2;
            localeInfoArr22[1] = !z ? localeInfo2 : localeInfo3;
            if (z) {
            }
            localeInfoArr3[0] = localeInfo3;
            i = 0;
            while (i < 2) {
            }
            LanguageCell languageCell2 = new LanguageCell(this);
            HashMap<String, String> hashMap22 = this.systemLocaleStrings;
            int i32 = R.string.ChooseYourLanguageOther;
            languageCell2.setValue(getStringForLanguageAlert(hashMap22, "ChooseYourLanguageOther", i32), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i32));
            languageCell2.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
            languageCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$115(view);
                }
            });
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$116(localeInfoArr3, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showLanguageAlertInternal$114(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$115(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$72(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$116(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
        LocaleController.getInstance().applyLanguage(localeInfoArr[0], true, false, this.currentAccount);
        rebuildAllFragments(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawRippleAbove(Canvas canvas, View view) {
        View view2;
        if (view == null || (view2 = this.rippleAbove) == null || view2.getBackground() == null) {
            return;
        }
        if (this.tempLocation == null) {
            this.tempLocation = new int[2];
        }
        this.rippleAbove.getLocationInWindow(this.tempLocation);
        int[] iArr = this.tempLocation;
        int i = iArr[0];
        int i2 = iArr[1];
        view.getLocationInWindow(iArr);
        int[] iArr2 = this.tempLocation;
        int i3 = i2 - iArr2[1];
        canvas.save();
        canvas.translate(i - iArr2[0], i3);
        this.rippleAbove.getBackground().draw(canvas);
        canvas.restore();
    }

    private void showLanguageAlert(boolean z) {
        String str;
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                if (!this.loadingLocaleDialog && !ApplicationLoader.mainInterfacePaused) {
                    String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                    final String str2 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                    if (!z && string.equals(str2)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("alert already showed for " + string);
                            return;
                        }
                        return;
                    }
                    final LocaleController.LocaleInfo[] localeInfoArr = new LocaleController.LocaleInfo[2];
                    String str3 = str2.contains("-") ? str2.split("-")[0] : str2;
                    if ("in".equals(str3)) {
                        str = "id";
                    } else if ("iw".equals(str3)) {
                        str = "he";
                    } else {
                        str = "jw".equals(str3) ? "jv" : null;
                    }
                    for (int i = 0; i < LocaleController.getInstance().languages.size(); i++) {
                        LocaleController.LocaleInfo localeInfo = LocaleController.getInstance().languages.get(i);
                        if (localeInfo.shortName.equals("en")) {
                            localeInfoArr[0] = localeInfo;
                        }
                        if (localeInfo.shortName.replace("_", "-").equals(str2) || localeInfo.shortName.equals(str3) || localeInfo.shortName.equals(str)) {
                            localeInfoArr[1] = localeInfo;
                        }
                        if (localeInfoArr[0] != null && localeInfoArr[1] != null) {
                            break;
                        }
                    }
                    if (localeInfoArr[0] != null && localeInfoArr[1] != null && localeInfoArr[0] != localeInfoArr[1]) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("show lang alert for " + localeInfoArr[0].getKey() + " and " + localeInfoArr[1].getKey());
                        }
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings = new TLRPC$TL_langpack_getStrings();
                        tLRPC$TL_langpack_getStrings.lang_code = localeInfoArr[1].getLangCode();
                        tLRPC$TL_langpack_getStrings.keys.add("English");
                        tLRPC$TL_langpack_getStrings.keys.add("ChooseYourLanguage");
                        tLRPC$TL_langpack_getStrings.keys.add("ChooseYourLanguageOther");
                        tLRPC$TL_langpack_getStrings.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda104
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$118(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 8);
                        TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                        tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                        tLRPC$TL_langpack_getStrings2.keys.add("English");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$120(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 8);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$118(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$117(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$117(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$120(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$119(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$119(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.englishLocaleStrings = hashMap;
        if (hashMap == null || this.systemLocaleStrings == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    private void onPasscodePause() {
        if (this.lockRunnable != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("cancel lockRunnable onPasscodePause");
            }
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity.24
                @Override // java.lang.Runnable
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("lock app");
                            }
                            LaunchActivity.this.showPasscodeActivity(true, false, -1, -1, null, null);
                            try {
                                NotificationsController.getInstance(UserConfig.selectedAccount).showNotifications();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("didn't pass lock check");
                        }
                        LaunchActivity.this.lockRunnable = null;
                    }
                }
            };
            this.lockRunnable = runnable;
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(runnable, 1000L);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("schedule app lock in 1000");
                }
            } else if (SharedConfig.autoLockIn != 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("schedule app lock in " + ((SharedConfig.autoLockIn * 1000) + 1000));
                }
                AndroidUtilities.runOnUIThread(this.lockRunnable, (SharedConfig.autoLockIn * 1000) + 1000);
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    public void addOverlayPasscodeView(PasscodeView passcodeView) {
        this.overlayPasscodeViews.add(passcodeView);
    }

    public void removeOverlayPasscodeView(PasscodeView passcodeView) {
        this.overlayPasscodeViews.remove(passcodeView);
    }

    private void onPasscodeResume() {
        if (this.lockRunnable != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("cancel lockRunnable onPasscodeResume");
            }
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onPasscodeResume");
            }
        }
    }

    private void updateCurrentConnectionState(int i) {
        String str;
        if (this.actionBarLayout == null) {
            return;
        }
        int i2 = 0;
        int connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        this.currentConnectionState = connectionState;
        Runnable runnable = null;
        if (connectionState == 2) {
            i2 = R.string.WaitingForNetwork;
            str = "WaitingForNetwork";
        } else if (connectionState == 5) {
            i2 = R.string.Updating;
            str = "Updating";
        } else if (connectionState == 4) {
            i2 = R.string.ConnectingToProxy;
            str = "ConnectingToProxy";
        } else if (connectionState == 1) {
            i2 = R.string.Connecting;
            str = "Connecting";
        } else {
            str = null;
        }
        if (connectionState == 1 || connectionState == 4) {
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$updateCurrentConnectionState$121();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentConnectionState$121() {
        BaseFragment baseFragment;
        if (AndroidUtilities.isTablet()) {
            if (!layerFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = layerFragmentsStack;
                baseFragment = arrayList.get(arrayList.size() - 1);
            }
            baseFragment = null;
        } else {
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                baseFragment = arrayList2.get(arrayList2.size() - 1);
            }
            baseFragment = null;
        }
        if ((baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity)) {
            return;
        }
        lambda$runLinkRequest$72(new ProxyListActivity());
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode == null) {
            return;
        }
        actionMode.finish();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        try {
            super.onSaveInstanceState(bundle);
            BaseFragment baseFragment = null;
            if (AndroidUtilities.isTablet()) {
                INavigationLayout iNavigationLayout = this.layersActionBarLayout;
                if (iNavigationLayout != null && !iNavigationLayout.getFragmentStack().isEmpty()) {
                    baseFragment = this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1);
                } else {
                    INavigationLayout iNavigationLayout2 = this.rightActionBarLayout;
                    if (iNavigationLayout2 != null && !iNavigationLayout2.getFragmentStack().isEmpty()) {
                        baseFragment = this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1);
                    } else if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
                        baseFragment = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
                    }
                }
            } else if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
                baseFragment = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
            }
            if (baseFragment != null) {
                Bundle arguments = baseFragment.getArguments();
                if ((baseFragment instanceof ChatActivity) && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "chat");
                } else if ((baseFragment instanceof GroupCreateFinalActivity) && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "group");
                } else if (baseFragment instanceof WallpapersListActivity) {
                    bundle.putString("fragment", "wallpapers");
                } else if (baseFragment instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment;
                    if (profileActivity.isSettings()) {
                        bundle.putString("fragment", "settings");
                    } else if (profileActivity.isChat() && arguments != null) {
                        bundle.putBundle("args", arguments);
                        bundle.putString("fragment", "chat_profile");
                    }
                } else if ((baseFragment instanceof ChannelCreateActivity) && arguments != null && arguments.getInt("step") == 0) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "channel");
                }
                baseFragment.saveSelfArgs(bundle);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (FloatingDebugController.onBackPressed()) {
            return;
        }
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null && passcodeView.getVisibility() == 0) {
            finish();
            return;
        }
        if (ContentPreviewViewer.hasInstance() && ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().closeWithMenu();
        }
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getView().getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
            } else if (this.rightActionBarLayout.getView().getVisibility() == 0 && !this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                BaseFragment baseFragment = this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1);
                if (baseFragment.onBackPressed()) {
                    baseFragment.finishFragment();
                }
            } else {
                this.actionBarLayout.onBackPressed();
            }
        } else {
            this.actionBarLayout.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        INavigationLayout iNavigationLayout = this.actionBarLayout;
        if (iNavigationLayout != null) {
            iNavigationLayout.onLowMemory();
            if (AndroidUtilities.isTablet()) {
                INavigationLayout iNavigationLayout2 = this.rightActionBarLayout;
                if (iNavigationLayout2 != null) {
                    iNavigationLayout2.onLowMemory();
                }
                INavigationLayout iNavigationLayout3 = this.layersActionBarLayout;
                if (iNavigationLayout3 != null) {
                    iNavigationLayout3.onLowMemory();
                }
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.visibleActionMode = actionMode;
        try {
            Menu menu = actionMode.getMenu();
            if (menu != null && !this.actionBarLayout.extendActionMode(menu) && AndroidUtilities.isTablet() && !this.rightActionBarLayout.extendActionMode(menu)) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (Build.VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeStarted(actionMode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeStarted(actionMode);
                this.layersActionBarLayout.onActionModeStarted(actionMode);
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        if (this.visibleActionMode == actionMode) {
            this.visibleActionMode = null;
        }
        if (Build.VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeFinished(actionMode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeFinished(actionMode);
                this.layersActionBarLayout.onActionModeFinished(actionMode);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
            return true;
        } else {
            return false;
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        keyEvent.getKeyCode();
        if (keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25)) {
            boolean z = true;
            if (VoIPService.getSharedInstance() != null) {
                if (Build.VERSION.SDK_INT >= 32) {
                    boolean isSpeakerMuted = WebRtcAudioTrack.isSpeakerMuted();
                    AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
                    z = (audioManager.getStreamVolume(0) == audioManager.getStreamMinVolume(0) && keyEvent.getKeyCode() == 25) ? false : false;
                    WebRtcAudioTrack.setSpeakerMute(z);
                    if (isSpeakerMuted != WebRtcAudioTrack.isSpeakerMuted()) {
                        showVoiceChatTooltip(z ? 42 : 43);
                    }
                }
            } else if (!mainFragmentsStack.isEmpty() && ((!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && keyEvent.getRepeatCount() == 0)) {
                ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).maybePlayVisibleVideo()) {
                    return true;
                }
                if (AndroidUtilities.isTablet() && !rightFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
                    BaseFragment baseFragment2 = arrayList2.get(arrayList2.size() - 1);
                    if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).maybePlayVisibleVideo()) {
                        return true;
                    }
                }
            }
        }
        try {
            super.dispatchKeyEvent(keyEvent);
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getView().getVisibility() == 0 && !this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                    this.layersActionBarLayout.getView().onKeyUp(i, keyEvent);
                } else if (this.rightActionBarLayout.getView().getVisibility() == 0 && !this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                    this.rightActionBarLayout.getView().onKeyUp(i, keyEvent);
                } else {
                    this.actionBarLayout.getView().onKeyUp(i, keyEvent);
                }
            } else if (this.actionBarLayout.getFragmentStack().size() == 1) {
                if (!this.drawerLayoutContainer.isDrawerOpened()) {
                    if (getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(getCurrentFocus());
                    }
                    this.drawerLayoutContainer.openDrawer(false);
                } else {
                    this.drawerLayoutContainer.closeDrawer(false);
                }
            } else {
                this.actionBarLayout.getView().onKeyUp(i, keyEvent);
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public boolean needPresentFragment(INavigationLayout iNavigationLayout, INavigationLayout.NavigationParams navigationParams) {
        INavigationLayout iNavigationLayout2;
        INavigationLayout iNavigationLayout3;
        INavigationLayout iNavigationLayout4;
        INavigationLayout iNavigationLayout5;
        INavigationLayout iNavigationLayout6;
        BaseFragment baseFragment = navigationParams.fragment;
        boolean z = navigationParams.removeLast;
        boolean z2 = navigationParams.noAnimation;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z3 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z3 || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || ((iNavigationLayout6 = this.layersActionBarLayout) != null && iNavigationLayout6.getView().getVisibility() == 0)) ? false : true, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (iNavigationLayout5 = this.actionBarLayout)) {
                iNavigationLayout5.removeAllFragments();
                this.actionBarLayout.presentFragment(navigationParams.setRemoveLast(z).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                this.layersActionBarLayout.removeAllFragments();
                this.layersActionBarLayout.getView().setVisibility(8);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
                return false;
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z4 = this.tabletFullSize;
                if ((!z4 && iNavigationLayout == this.rightActionBarLayout) || (z4 && iNavigationLayout == this.actionBarLayout)) {
                    boolean z5 = (z4 && iNavigationLayout == (iNavigationLayout4 = this.actionBarLayout) && iNavigationLayout4.getFragmentStack().size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout7 = this.layersActionBarLayout;
                            iNavigationLayout7.removeFragmentFromStack(iNavigationLayout7.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z5) {
                        this.actionBarLayout.presentFragment(navigationParams.setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    }
                    return z5;
                } else if (!z4 && iNavigationLayout != (iNavigationLayout3 = this.rightActionBarLayout)) {
                    iNavigationLayout3.getView().setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(navigationParams.setNoAnimation(true).setRemoveLast(z).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout8 = this.layersActionBarLayout;
                            iNavigationLayout8.removeFragmentFromStack(iNavigationLayout8.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else if (z4 && iNavigationLayout != (iNavigationLayout2 = this.actionBarLayout)) {
                    iNavigationLayout2.presentFragment(navigationParams.setRemoveLast(iNavigationLayout2.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout9 = this.layersActionBarLayout;
                            iNavigationLayout9.removeFragmentFromStack(iNavigationLayout9.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else {
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout10 = this.layersActionBarLayout;
                            iNavigationLayout10.removeFragmentFromStack(iNavigationLayout10.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    INavigationLayout iNavigationLayout11 = this.actionBarLayout;
                    iNavigationLayout11.presentFragment(navigationParams.setRemoveLast(iNavigationLayout11.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    return false;
                }
            } else {
                INavigationLayout iNavigationLayout12 = this.layersActionBarLayout;
                if (iNavigationLayout != iNavigationLayout12) {
                    iNavigationLayout12.getView().setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    int i = 0;
                    while (true) {
                        if (i >= 4) {
                            i = -1;
                            break;
                        } else if (UserConfig.getInstance(i).isClientActivated()) {
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z3 && i == -1) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.presentFragment(navigationParams.setRemoveLast(z).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    return false;
                }
            }
        } else {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment baseFragment, INavigationLayout iNavigationLayout) {
        INavigationLayout iNavigationLayout2;
        INavigationLayout iNavigationLayout3;
        INavigationLayout iNavigationLayout4;
        if (AndroidUtilities.isTablet()) {
            boolean z = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getView().getVisibility() == 0) ? false : true, true);
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (iNavigationLayout4 = this.actionBarLayout)) {
                    iNavigationLayout4.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(baseFragment);
                    this.layersActionBarLayout.removeAllFragments();
                    this.layersActionBarLayout.getView().setVisibility(8);
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    if (!this.tabletFullSize) {
                        this.shadowTabletSide.setVisibility(0);
                        if (this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                            this.backgroundTablet.setVisibility(0);
                        }
                    }
                    return false;
                }
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z2 = this.tabletFullSize;
                if (!z2 && iNavigationLayout != (iNavigationLayout3 = this.rightActionBarLayout)) {
                    iNavigationLayout3.getView().setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout5 = this.layersActionBarLayout;
                            iNavigationLayout5.removeFragmentFromStack(iNavigationLayout5.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (z2 && iNavigationLayout != (iNavigationLayout2 = this.actionBarLayout)) {
                    iNavigationLayout2.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout6 = this.layersActionBarLayout;
                            iNavigationLayout6.removeFragmentFromStack(iNavigationLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else {
                INavigationLayout iNavigationLayout7 = this.layersActionBarLayout;
                if (iNavigationLayout != iNavigationLayout7) {
                    iNavigationLayout7.getView().setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    int i = 0;
                    while (true) {
                        if (i >= 4) {
                            i = -1;
                            break;
                        } else if (UserConfig.getInstance(i).isClientActivated()) {
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z && i == -1) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.addFragmentToStack(baseFragment);
                    return false;
                }
            }
        } else {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public boolean needCloseLastFragment(INavigationLayout iNavigationLayout) {
        if (AndroidUtilities.isTablet()) {
            if (iNavigationLayout == this.actionBarLayout && iNavigationLayout.getFragmentStack().size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (iNavigationLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (iNavigationLayout == this.layersActionBarLayout && this.actionBarLayout.getFragmentStack().isEmpty() && this.layersActionBarLayout.getFragmentStack().size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (iNavigationLayout.getFragmentStack().size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (iNavigationLayout.getFragmentStack().size() >= 2 && !(iNavigationLayout.getFragmentStack().get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean z) {
        INavigationLayout iNavigationLayout = this.layersActionBarLayout;
        if (iNavigationLayout != null) {
            iNavigationLayout.rebuildAllFragmentViews(z, z);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public void onRebuildAllFragments(INavigationLayout iNavigationLayout, boolean z) {
        if (AndroidUtilities.isTablet() && iNavigationLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(z, z);
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
