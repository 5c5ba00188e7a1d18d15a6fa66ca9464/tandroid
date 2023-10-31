package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
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
import com.google.common.primitives.Longs;
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
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsLoadingObserver;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FingerprintController;
import org.telegram.messenger.FlagSecureReason;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LiteMode;
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
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
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
import org.telegram.tgnet.TLRPC$TL_inputBotAppShortName;
import org.telegram.tgnet.TLRPC$TL_inputChannel;
import org.telegram.tgnet.TLRPC$TL_inputGameShortName;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_inputMediaGame;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_inputThemeSlug;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_langPackLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messages_botApp;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_checkChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImport;
import org.telegram.tgnet.TLRPC$TL_messages_discussionMessage;
import org.telegram.tgnet.TLRPC$TL_messages_forumTopics;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_messages_getBotApp;
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
import org.telegram.tgnet.tl.TL_chatlists$TL_chatlists_chatlistInvite;
import org.telegram.tgnet.tl.TL_chatlists$TL_chatlists_chatlistInviteAlready;
import org.telegram.tgnet.tl.TL_chatlists$TL_chatlists_checkChatlistInvite;
import org.telegram.tgnet.tl.TL_chatlists$chatlist_ChatlistInvite;
import org.telegram.tgnet.tl.TL_stories$PeerStories;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.tgnet.tl.TL_stories$TL_stories_getPeerStories;
import org.telegram.tgnet.tl.TL_stories$TL_stories_peerStories;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
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
import org.telegram.ui.Components.BatteryDrawable;
import org.telegram.ui.Components.BlockingUpdateView;
import org.telegram.ui.Components.BotWebViewSheet;
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
import org.telegram.ui.Components.FolderBottomSheet;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MediaActivity;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PhonebookShareAlert;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostPagerBottomSheet;
import org.telegram.ui.Components.Premium.boosts.GiftInfoBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SharingLocationsAlert;
import org.telegram.ui.Components.SideMenultItemAnimator;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.ContactsActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LauncherIconController;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WallpapersListActivity;
import org.webrtc.MediaStreamTrack;
import org.webrtc.voiceengine.WebRtcAudioTrack;
/* loaded from: classes3.dex */
public class LaunchActivity extends BasePermissionsActivity implements INavigationLayout.INavigationLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    public static LaunchActivity instance;
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
    private View customNavigationBar;
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
    private FlagSecureReason flagSecureReason;
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
    private ValueAnimator navBarAnimator;
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
    private String videoPath;
    private ActionMode visibleActionMode;
    private AlertDialog visibleDialog;
    private String voicePath;
    private boolean wasMutedByAdminRaisedHand;
    public static final Pattern PREFIX_T_ME_PATTERN = Pattern.compile("^(?:http(?:s|)://|)([A-z0-9-]+?)\\.t\\.me");
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList<>();
    public ArrayList<INavigationLayout> sheetFragmentsStack = new ArrayList<>();
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
    public static /* synthetic */ void lambda$setupActionBarLayout$11(View view) {
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
        INavigationLayout iNavigationLayout;
        Intent intent;
        Uri data;
        if (BuildVars.DEBUG_VERSION) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy()).detectLeakedClosableObjects().penaltyLog().build());
        }
        instance = this;
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
                setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor(Theme.key_actionBarDefault) | (-16777216)));
            } catch (Throwable unused) {
            }
            try {
                getWindow().setNavigationBarColor(-16777216);
            } catch (Throwable unused2) {
            }
        }
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        FlagSecureReason flagSecureReason = new FlagSecureReason(getWindow(), LaunchActivity$$ExternalSyntheticLambda100.INSTANCE);
        this.flagSecureReason = flagSecureReason;
        flagSecureReason.attach();
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
        AndroidUtilities.fillStatusBarHeight(this, false);
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
        3 r8 = new 3(this);
        this.drawerLayoutContainer = r8;
        r8.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
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
        RecyclerListView recyclerListView2 = this.sideMenu;
        int i3 = Theme.key_chats_menuBackground;
        recyclerListView2.setBackgroundColor(Theme.getColor(i3));
        this.sideMenuContainer.setBackgroundColor(Theme.getColor(i3));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.sideMenu.setAllowItemsInteractionDuringAnimation(false);
        RecyclerListView recyclerListView3 = this.sideMenu;
        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, this.itemAnimator, this.drawerLayoutContainer);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView3.setAdapter(drawerLayoutAdapter);
        this.drawerLayoutAdapter.setOnPremiumDrawableClick(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                LaunchActivity.this.lambda$onCreate$1(view2);
            }
        });
        this.sideMenuContainer.addView(this.sideMenu, LayoutHelper.createFrame(-1, -1.0f));
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenuContainer, this.sideMenu);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenuContainer.getLayoutParams();
        Point realScreenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(realScreenSize.x, realScreenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenuContainer.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda141
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view2, int i4) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view2, i4);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view2, int i4, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view2, i4, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view2, int i4, float f, float f2) {
                LaunchActivity.this.lambda$onCreate$6(view2, i4, f, f2);
            }
        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(3, 0) { // from class: org.telegram.ui.LaunchActivity.7
            private RecyclerView.ViewHolder selectedViewHolder;

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i4) {
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
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i4) {
                clearSelectedViewHolder();
                if (i4 != 0) {
                    this.selectedViewHolder = viewHolder;
                    View view2 = viewHolder.itemView;
                    LaunchActivity.this.sideMenu.cancelClickRunnables(false);
                    view2.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
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
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i4, boolean z2) {
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
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda142
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i4) {
                boolean lambda$onCreate$7;
                lambda$onCreate$7 = LaunchActivity.this.lambda$onCreate$7(itemTouchHelper, view2, i4);
                return lambda$onCreate$7;
            }
        });
        ApplicationLoader.applicationLoaderInstance.takeUpdateLayout(this, this.sideMenu, this.sideMenuContainer);
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.setFragmentStack(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onCreate$8();
            }
        });
        this.actionBarLayout.setDelegate(this);
        Theme.loadWallpaper(true);
        checkCurrentAccount();
        updateCurrentConnectionState(this.currentAccount);
        NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
        int i4 = NotificationCenter.closeOtherAppActivities;
        globalInstance.lambda$postNotificationNameOnUIThread$1(i4, this);
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().addObserver(this, i4);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.requestPermissions);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.billingConfirmPurchaseError);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        LiteMode.addOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda104(this));
        if (this.actionBarLayout.getFragmentStack().isEmpty() && ((iNavigationLayout = this.layersActionBarLayout) == null || iNavigationLayout.getFragmentStack().isEmpty())) {
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
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } else {
            BaseFragment baseFragment = (this.actionBarLayout.getFragmentStack().size() > 0 ? this.actionBarLayout : this.layersActionBarLayout).getFragmentStack().get(0);
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
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda21
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public final void onGlobalLayout() {
                        LaunchActivity.lambda$onCreate$9(rootView);
                    }
                };
                this.onGlobalLayoutListener = onGlobalLayoutListener;
                viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        MediaController.getInstance().setBaseActivity(this, true);
        ApplicationLoader.startAppCenter(this);
        int i5 = Build.VERSION.SDK_INT;
        if (i5 >= 23) {
            FingerprintController.checkKeyReady();
        }
        if (i5 >= 28 && ((ActivityManager) getSystemService("activity")).isBackgroundRestricted() && System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() >= 86400000 && SharedConfig.BackgroundActivityPrefs.getDismissedCount() < 3) {
            AlertsCreator.createBackgroundActivityDialog(this).show();
            SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
        }
        if (i5 >= 31) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onCreate$0() {
        return SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture;
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
    public /* synthetic */ void lambda$onCreate$1(View view) {
        showSelectStatusDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$6(View view, int i, float f, float f2) {
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
                    lambda$runLinkRequest$86(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.getFragmentStack().size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount, null);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onCreate$2();
                        }
                    };
                    return;
                }
            }
            int id = this.drawerLayoutAdapter.getId(i);
            final TLRPC$TL_attachMenuBot attachMenuBot = this.drawerLayoutAdapter.getAttachMenuBot(i);
            if (attachMenuBot != null) {
                if (attachMenuBot.inactive || attachMenuBot.side_menu_disclaimer_needed) {
                    WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda28
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            LaunchActivity.this.lambda$onCreate$5(attachMenuBot, (Boolean) obj);
                        }
                    }, null);
                } else {
                    showAttachMenuBot(attachMenuBot, null);
                }
            } else if (id == 2) {
                lambda$runLinkRequest$86(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$86(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$86(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$86(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                Bundle bundle3 = new Bundle();
                bundle3.putBoolean("needFinishFragment", false);
                lambda$runLinkRequest$86(new ContactsActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$86(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$86(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$86(new ChatActivity(bundle4));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$86(new ActionIntroActivity(1));
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
                    lambda$runLinkRequest$86(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$86(new ActionIntroActivity(4));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 13) {
                Browser.openUrl(this, LocaleController.getString("TelegramFeaturesUrl", R.string.TelegramFeaturesUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 15) {
                showSelectStatusDialog();
            } else if (id == 16) {
                Bundle bundle5 = new Bundle();
                bundle5.putLong("dialog_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                bundle5.putInt("type", 1);
                this.drawerLayoutContainer.closeDrawer(true);
                lambda$runLinkRequest$86(new MediaActivity(bundle5, null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2() {
        this.drawerLayoutContainer.closeDrawer(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$5(final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, Boolean bool) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tLRPC$TL_attachMenuBot.bot_id);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda129
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$onCreate$4(tLRPC$TL_attachMenuBot, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onCreate$3(tLRPC$TL_attachMenuBot);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot) {
        tLRPC$TL_attachMenuBot.side_menu_disclaimer_needed = false;
        tLRPC$TL_attachMenuBot.inactive = false;
        showAttachMenuBot(tLRPC$TL_attachMenuBot, null);
        MediaDataController.getInstance(this.currentAccount).updateAttachMenuBotsInCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$7(ItemTouchHelper itemTouchHelper, View view, int i) {
        if (view instanceof DrawerUserCell) {
            final int accountNumber = ((DrawerUserCell) view).getAccountNumber();
            if (accountNumber == this.currentAccount || AndroidUtilities.isTablet()) {
                itemTouchHelper.startDrag(this.sideMenu.getChildViewHolder(view));
            } else {
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
        }
        if (view instanceof DrawerActionCell) {
            this.drawerLayoutAdapter.getId(i);
            TLRPC$TL_attachMenuBot attachMenuBot = this.drawerLayoutAdapter.getAttachMenuBot(i);
            if (attachMenuBot != null) {
                BotWebViewSheet.deleteBot(this.currentAccount, attachMenuBot.bot_id, null);
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$8() {
        checkSystemBarColors(true, false);
        if (getLastFragment() == null || getLastFragment().storyViewer == null || !getLastFragment().storyViewer.isShown()) {
            return;
        }
        getLastFragment().storyViewer.updatePlayingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreate$9(View view) {
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

    private void showAttachMenuBot(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, String str) {
        this.drawerLayoutContainer.closeDrawer();
        BotWebViewSheet botWebViewSheet = new BotWebViewSheet(this, getLastFragment().getResourceProvider());
        botWebViewSheet.setParentActivity(this);
        int i = this.currentAccount;
        long j = tLRPC$TL_attachMenuBot.bot_id;
        botWebViewSheet.requestWebView(i, j, j, tLRPC$TL_attachMenuBot.short_name, null, 1, 0, false, null, null, false, str, null, 2);
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment != null) {
            lastFragment.showDialog(botWebViewSheet);
        } else {
            botWebViewSheet.show();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public void onThemeProgress(float f) {
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().updateThemeColors(f);
        }
        this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
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
            this.shadowTablet.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda20
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$setupActionBarLayout$10;
                    lambda$setupActionBarLayout$10 = LaunchActivity.this.lambda$setupActionBarLayout$10(view2, motionEvent);
                    return lambda$setupActionBarLayout$10;
                }
            });
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda19.INSTANCE);
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
    public /* synthetic */ boolean lambda$setupActionBarLayout$10(View view, MotionEvent motionEvent) {
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
                            if (z2) {
                                i3 = R.drawable.msg_status_edit;
                            } else {
                                i3 = R.drawable.msg_status_set;
                            }
                            drawerActionCell.updateTextAndIcon(string, i3);
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
        lambda$runLinkRequest$86(new ProfileActivity(bundle));
        this.drawerLayoutContainer.closeDrawer(false);
    }

    private void checkSystemBarColors() {
        checkSystemBarColors(false, true, !this.isNavigationBarColorFrozen, true);
    }

    public void checkSystemBarColors(boolean z) {
        checkSystemBarColors(z, true, !this.isNavigationBarColorFrozen, true);
    }

    private void checkSystemBarColors(boolean z, boolean z2) {
        checkSystemBarColors(false, z, z2, true);
    }

    public void checkSystemBarColors(boolean z, boolean z2, boolean z3, boolean z4) {
        BaseFragment baseFragment;
        boolean z5;
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
        boolean z6 = baseFragment != null && baseFragment.hasForceLightStatusBar();
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            if (z2) {
                if (baseFragment != null) {
                    z5 = baseFragment.isLightStatusBar();
                } else {
                    z5 = ColorUtils.calculateLuminance(Theme.getColor(Theme.key_actionBarDefault, null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), z5, z6);
            }
            if (i >= 26 && z3 && (!z || baseFragment == null || !baseFragment.isInPreviewMode())) {
                setNavigationBarColor((baseFragment == null || !z) ? Theme.getColor(Theme.key_windowBackgroundGray, null, true) : baseFragment.getNavigationBarColor(), z4);
            }
        }
        if (i < 21 || !z2) {
            return;
        }
        getWindow().setStatusBarColor(0);
    }

    public FrameLayout getMainContainerFrameLayout() {
        return this.frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ DialogsActivity lambda$switchToAccount$12(Void r1) {
        return new DialogsActivity(null);
    }

    public void switchToAccount(int i, boolean z) {
        switchToAccount(i, z, LaunchActivity$$ExternalSyntheticLambda101.INSTANCE);
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
        lambda$runLinkRequest$86(new IntroActivity().setOnLogout());
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
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.attachMenuBotsDidLoad);
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
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesEnabledUpdate);
        }
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.attachMenuBotsDidLoad);
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
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesEnabledUpdate);
    }

    private void checkLayout() {
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        if (AndroidUtilities.getWasTablet() == null || AndroidUtilities.getWasTablet().booleanValue() == AndroidUtilities.isTabletForce()) {
            if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2)) {
                this.tabletFullSize = false;
                List<BaseFragment> fragmentStack = this.actionBarLayout.getFragmentStack();
                if (fragmentStack.size() >= 2) {
                    while (1 < fragmentStack.size()) {
                        BaseFragment baseFragment = fragmentStack.get(1);
                        if (baseFragment instanceof ChatActivity) {
                            ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                        }
                        baseFragment.onPause();
                        baseFragment.onFragmentDestroy();
                        baseFragment.setParentLayout(null);
                        fragmentStack.remove(baseFragment);
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
            List<BaseFragment> fragmentStack2 = this.rightActionBarLayout.getFragmentStack();
            if (!fragmentStack2.isEmpty()) {
                while (fragmentStack2.size() > 0) {
                    BaseFragment baseFragment2 = fragmentStack2.get(0);
                    if (baseFragment2 instanceof ChatActivity) {
                        ((ChatActivity) baseFragment2).setIgnoreAttachOnPause(true);
                    }
                    baseFragment2.onPause();
                    baseFragment2.onFragmentDestroy();
                    baseFragment2.setParentLayout(null);
                    fragmentStack2.remove(baseFragment2);
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
        StoryRecorder.destroyInstance();
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null && playingMessageObject.isRoundVideo()) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
        this.passcodeView.onShow(this.overlayPasscodeViews.isEmpty() && z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showPasscodeActivity$13(runnable);
            }
        }, runnable2);
        int i3 = 0;
        while (i3 < this.overlayPasscodeViews.size()) {
            this.overlayPasscodeViews.get(i3).onShow(z && i3 == this.overlayPasscodeViews.size() - 1, z2, i, i2, null, null);
            i3++;
        }
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        PasscodeView.PasscodeViewDelegate passcodeViewDelegate = new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda140
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword(PasscodeView passcodeView2) {
                LaunchActivity.this.lambda$showPasscodeActivity$14(passcodeView2);
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
    public /* synthetic */ void lambda$showPasscodeActivity$13(Runnable runnable) {
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
    public /* synthetic */ void lambda$showPasscodeActivity$14(PasscodeView passcodeView) {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true);
            this.passcodeSaveIntent = null;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.getView().setVisibility(0);
        this.actionBarLayout.rebuildFragments(1);
        this.actionBarLayout.updateTitleOverlay();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildFragments(1);
            this.rightActionBarLayout.rebuildFragments(1);
            if (this.layersActionBarLayout.getView().getVisibility() == 4) {
                this.layersActionBarLayout.getView().setVisibility(0);
            }
            this.rightActionBarLayout.getView().setVisibility(0);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.passcodeDismissed, passcodeView);
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
        return handleIntent(intent, z, z2, z3, null);
    }

    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3, Browser.Progress progress) {
        return handleIntent(intent, z, z2, z3, progress, true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:1165:0x2402, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - (r3 == true ? 1 : 0))) != false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1182:0x2481, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - (r3 == true ? 1 : 0))) != false) goto L142;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0345, code lost:
        if (r95.sendingText == null) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0157, code lost:
        if (r9.equals(r0) != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:916:0x1ae5, code lost:
        if (r5.longValue() == 0) goto L1237;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x1fbe  */
    /* JADX WARN: Removed duplicated region for block: B:1052:0x1fc2 A[Catch: all -> 0x1fd1, TRY_LEAVE, TryCatch #20 {all -> 0x1fd1, blocks: (B:1049:0x1fac, B:1052:0x1fc2), top: B:1445:0x1fac }] */
    /* JADX WARN: Removed duplicated region for block: B:1090:0x20d3  */
    /* JADX WARN: Removed duplicated region for block: B:1091:0x20e5  */
    /* JADX WARN: Removed duplicated region for block: B:1140:0x2388  */
    /* JADX WARN: Removed duplicated region for block: B:1152:0x23b3  */
    /* JADX WARN: Removed duplicated region for block: B:1153:0x23c5  */
    /* JADX WARN: Removed duplicated region for block: B:1275:0x265e  */
    /* JADX WARN: Removed duplicated region for block: B:1276:0x266f  */
    /* JADX WARN: Removed duplicated region for block: B:1279:0x267d  */
    /* JADX WARN: Removed duplicated region for block: B:1280:0x268d  */
    /* JADX WARN: Removed duplicated region for block: B:1358:0x2932 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1361:0x293b  */
    /* JADX WARN: Removed duplicated region for block: B:1372:0x2988  */
    /* JADX WARN: Removed duplicated region for block: B:1382:0x29ca  */
    /* JADX WARN: Removed duplicated region for block: B:1386:0x29e1  */
    /* JADX WARN: Removed duplicated region for block: B:1388:0x29e8  */
    /* JADX WARN: Removed duplicated region for block: B:1414:0x15f4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1462:0x1feb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0534  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x075e  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0866  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x0c08  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x0c0f  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x0c73  */
    /* JADX WARN: Removed duplicated region for block: B:526:0x0c80  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x0c89  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x0c8c  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0c9f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:542:0x0ccb  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0cf4  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x196e  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:909:0x1a53 A[Catch: Exception -> 0x1a62, TRY_LEAVE, TryCatch #15 {Exception -> 0x1a62, blocks: (B:907:0x1a47, B:909:0x1a53), top: B:1436:0x1a47 }] */
    /* JADX WARN: Removed duplicated region for block: B:911:0x1a60  */
    /* JADX WARN: Removed duplicated region for block: B:965:0x1da1  */
    /* JADX WARN: Removed duplicated region for block: B:966:0x1dca  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:984:0x1ea0  */
    /* JADX WARN: Type inference failed for: r10v17 */
    /* JADX WARN: Type inference failed for: r10v21 */
    /* JADX WARN: Type inference failed for: r10v23 */
    /* JADX WARN: Type inference failed for: r10v24 */
    /* JADX WARN: Type inference failed for: r10v25 */
    /* JADX WARN: Type inference failed for: r12v16 */
    /* JADX WARN: Type inference failed for: r12v175 */
    /* JADX WARN: Type inference failed for: r12v180 */
    /* JADX WARN: Type inference failed for: r12v188 */
    /* JADX WARN: Type inference failed for: r12v189 */
    /* JADX WARN: Type inference failed for: r12v9, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r3v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v17 */
    /* JADX WARN: Type inference failed for: r9v284 */
    /* JADX WARN: Type inference failed for: r9v286 */
    /* JADX WARN: Type inference failed for: r9v287 */
    /* JADX WARN: Type inference failed for: r9v9, types: [boolean, int] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3, Browser.Progress progress, boolean z4) {
        long j;
        Intent intent2;
        final LaunchActivity launchActivity;
        String str;
        String str2;
        final int[] iArr;
        boolean z5;
        Intent intent3;
        long j2;
        long j3;
        int i;
        int i2;
        int i3;
        String str3;
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
        boolean z16;
        boolean z17;
        Intent intent5;
        boolean z18;
        Intent intent6;
        boolean z19;
        Intent intent7;
        boolean z20;
        String str4;
        String str5;
        String str6;
        int i4;
        boolean z21;
        int i5;
        char c;
        Intent intent8;
        int i6;
        boolean z22;
        boolean z23;
        boolean z24;
        boolean z25;
        ?? r12;
        ?? r9;
        GroupCallActivity groupCallActivity;
        ?? r3;
        boolean z26;
        String str7;
        BaseFragment lastFragment;
        boolean z27;
        char c2;
        final BaseFragment editWidgetActivity;
        final boolean z28;
        boolean z29;
        boolean z30;
        boolean z31;
        boolean z32;
        boolean z33;
        String str8;
        boolean z34;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        boolean z35;
        Pattern compile;
        int i7;
        int i8;
        char c3;
        String str9;
        int[] iArr2;
        boolean z36;
        String str10;
        boolean z37;
        char c4;
        int i9;
        boolean z38;
        String str11;
        String str12;
        String str13;
        long j4;
        String str14;
        String str15;
        long j5;
        String str16;
        HashMap<String, String> hashMap;
        String str17;
        Integer num;
        Integer num2;
        Long l;
        Integer num3;
        String str18;
        boolean z39;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        String str26;
        String str27;
        String str28;
        String str29;
        String str30;
        char c5;
        boolean z40;
        String str31;
        String str32;
        long j6;
        String str33;
        String str34;
        String str35;
        String str36;
        String str37;
        int i10;
        String str38;
        String str39;
        String str40;
        String str41;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        String str42;
        String str43;
        int i11;
        boolean z41;
        boolean z42;
        final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Cursor query;
        Throwable th;
        String string;
        boolean z43;
        int i12;
        char c6;
        Integer num4;
        Integer parseInt;
        long j7;
        boolean z44;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z45;
        int i13;
        long j8;
        long j9;
        int parseInt2;
        long j10;
        boolean z46;
        boolean z47;
        String str44;
        boolean z48;
        boolean z49;
        String str45;
        String str46;
        String str47;
        String str48;
        String str49;
        String str50;
        boolean z50;
        int i14;
        String str51;
        String str52;
        String str53;
        String str54;
        String str55;
        String str56;
        String str57;
        String str58;
        String str59;
        int i15;
        String str60;
        String str61;
        String str62;
        boolean z51;
        String str63;
        String str64;
        String str65;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper2;
        String str66;
        String str67;
        String str68;
        String str69;
        Integer num5;
        Integer num6;
        Long l2;
        Integer num7;
        int i16;
        String str70;
        Long parseLong;
        int i17;
        String str71;
        int i18;
        String str72;
        String str73;
        Integer num8;
        String str74;
        Integer parseInt3;
        boolean z52;
        Integer parseInt4;
        String substring;
        Long l3;
        Integer num9;
        Integer num10;
        Long l4;
        boolean z53;
        boolean z54;
        String substring2;
        long j11;
        String type2;
        String stringExtra;
        Parcelable parcelableExtra;
        boolean z55;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str75;
        String str76;
        if (GiftInfoBottomSheet.handleIntent(intent, progress) || AndroidUtilities.handleProxyIntent(this, intent)) {
            return true;
        }
        if (intent == null || !"android.intent.action.MAIN".equals(intent.getAction())) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            }
            StoryRecorder.destroyInstance();
        }
        int flags = intent.getFlags();
        String action = intent.getAction();
        int[] iArr3 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(iArr3[0], true);
        boolean z56 = action != null && action.equals("voip");
        if (!z3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z56) {
                this.passcodeSaveIntent = intent;
                this.passcodeSaveIntentIsNew = z;
                this.passcodeSaveIntentIsRestore = z2;
                return false;
            }
        }
        boolean booleanExtra = intent.getBooleanExtra("force_not_internal_apps", false);
        long[] jArr = null;
        this.photoPathsArray = null;
        this.videoPath = null;
        this.voicePath = null;
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
        long j12 = 0;
        if ((1048576 & flags) == 0 && intent.getAction() != null && !z2) {
            str8 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j11 = intent.getExtras().getLong("dialogId", 0L);
                    if (j11 == 0) {
                        try {
                            String string2 = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string2 != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                for (int i19 = 0; i19 < size; i19++) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i19);
                                    if (string2.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j13 = extras.getLong("dialogId", 0L);
                                        try {
                                            str75 = extras.getString("hash", null);
                                            j11 = j13;
                                            break;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            j11 = j13;
                                            FileLog.e(th);
                                            str75 = null;
                                            str76 = SharedConfig.directShareHash;
                                            if (str76 != null) {
                                            }
                                            j11 = 0;
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
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                        str75 = null;
                    } else {
                        str75 = intent.getExtras().getString("hash", null);
                    }
                    str76 = SharedConfig.directShareHash;
                    if (str76 != null) {
                    }
                }
                j11 = 0;
                type2 = intent.getType();
                if (type2 == null && type2.equals("text/x-vcard")) {
                    try {
                        Uri uri = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
                        if (uri != null) {
                            ArrayList<TLRPC$User> loadVCardFromStream = AndroidUtilities.loadVCardFromStream(uri, this.currentAccount, false, null, null);
                            this.contactsToSend = loadVCardFromStream;
                            if (loadVCardFromStream.size() > 5) {
                                this.contactsToSend = null;
                                ArrayList<Uri> arrayList2 = new ArrayList<>();
                                this.documentsUrisArray = arrayList2;
                                arrayList2.add(uri);
                                this.documentsMimeType = type2;
                            } else {
                                this.contactsToSendUri = uri;
                            }
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    z55 = true;
                    if (z55) {
                    }
                    j2 = 0;
                    str4 = null;
                    str5 = null;
                    str6 = null;
                    intent8 = intent;
                    launchActivity = this;
                    str = " ";
                    j3 = j11;
                    i = -1;
                    i2 = 0;
                    i3 = -1;
                    z6 = false;
                    z7 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    i4 = 0;
                    z21 = false;
                    i5 = 0;
                    c = 0;
                    str3 = null;
                    str2 = "message_id";
                    iArr = iArr3;
                    i6 = 0;
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
                        boolean z57 = parcelableExtra instanceof Uri;
                        Uri uri2 = parcelableExtra;
                        if (!z57) {
                            uri2 = Uri.parse(parcelableExtra.toString());
                        }
                        Uri uri3 = (Uri) uri2;
                        boolean z58 = uri3 != null && AndroidUtilities.isInternalUri(uri3);
                        if (!z58 && uri3 != null) {
                            if ((type2 != null && type2.startsWith("image/")) || uri3.toString().toLowerCase().endsWith(".jpg")) {
                                if (this.photoPathsArray == null) {
                                    this.photoPathsArray = new ArrayList<>();
                                }
                                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                                sendingMediaInfo.uri = uri3;
                                this.photoPathsArray.add(sendingMediaInfo);
                            } else {
                                String uri4 = uri3.toString();
                                if (j11 == 0 && uri4 != null) {
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
                                        } else if (type2 != null && type2.startsWith("audio/ogg") && type2.contains("codecs=opus") && MediaController.isOpusFile(path) == 1) {
                                            this.voicePath = path;
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
                        z55 = z58;
                        if (z55) {
                            Toast.makeText(this, "Unsupported content", 0).show();
                        }
                        j2 = 0;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        intent8 = intent;
                        launchActivity = this;
                        str = " ";
                        j3 = j11;
                        i = -1;
                        i2 = 0;
                        i3 = -1;
                        z6 = false;
                        z7 = false;
                        z10 = false;
                        z9 = false;
                        z8 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        z17 = false;
                        i4 = 0;
                        z21 = false;
                        i5 = 0;
                        c = 0;
                        str3 = null;
                        str2 = "message_id";
                        iArr = iArr3;
                        i6 = 0;
                    }
                }
                z55 = false;
                if (z55) {
                }
                j2 = 0;
                str4 = null;
                str5 = null;
                str6 = null;
                intent8 = intent;
                launchActivity = this;
                str = " ";
                j3 = j11;
                i = -1;
                i2 = 0;
                i3 = -1;
                z6 = false;
                z7 = false;
                z10 = false;
                z9 = false;
                z8 = false;
                z12 = false;
                z13 = false;
                z14 = false;
                z15 = false;
                z16 = false;
                z17 = false;
                i4 = 0;
                z21 = false;
                i5 = 0;
                c = 0;
                str3 = null;
                str2 = "message_id";
                iArr = iArr3;
                i6 = 0;
            } else if ("org.telegram.messenger.CREATE_STICKER_PACK".equals(intent.getAction())) {
                try {
                    this.importingStickers = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                    this.importingStickersEmoji = intent.getStringArrayListExtra("STICKER_EMOJIS");
                    this.importingStickersSoftware = intent.getStringExtra("IMPORTER");
                } catch (Throwable th4) {
                    FileLog.e(th4);
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
                    int i20 = 0;
                    while (i20 < parcelableArrayListExtra.size()) {
                        Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i20);
                        boolean z59 = parcelable instanceof Uri;
                        Uri uri5 = parcelable;
                        if (!z59) {
                            uri5 = Uri.parse(parcelable.toString());
                        }
                        Uri uri6 = (Uri) uri5;
                        if (uri6 != null && AndroidUtilities.isInternalUri(uri6)) {
                            parcelableArrayListExtra.remove(i20);
                            i20--;
                        }
                        i20++;
                    }
                    if (parcelableArrayListExtra.isEmpty()) {
                        arrayList = null;
                        if (arrayList != null) {
                            if (type != null && type.startsWith("image/")) {
                                for (int i21 = 0; i21 < arrayList.size(); i21++) {
                                    Parcelable parcelable2 = (Parcelable) arrayList.get(i21);
                                    boolean z60 = parcelable2 instanceof Uri;
                                    Uri uri7 = parcelable2;
                                    if (!z60) {
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
                                for (int i22 = 0; i22 < arrayList.size(); i22++) {
                                    Object obj = (Parcelable) arrayList.get(i22);
                                    if (!(obj instanceof Uri)) {
                                        obj = Uri.parse(obj.toString());
                                    }
                                    Uri uri9 = (Uri) obj;
                                    String path2 = AndroidUtilities.getPath(uri9);
                                    String obj2 = obj.toString();
                                    String str78 = obj2 == null ? path2 : obj2;
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("export path = " + str78);
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
                                                z35 = true;
                                                break;
                                            }
                                        }
                                        z35 = false;
                                        if (!z35) {
                                            if (str78.startsWith("content://com.kakao.talk") && str78.endsWith("KakaoTalkChats.txt")) {
                                                this.exportingChatUri = uri9;
                                            }
                                        }
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
                                }
                            }
                            z34 = false;
                            if (z34) {
                                Toast.makeText(this, "Unsupported content", 0).show();
                            }
                            intent2 = intent;
                            launchActivity = this;
                            str = " ";
                            j = 0;
                            str2 = "message_id";
                            iArr = iArr3;
                            z5 = false;
                            intent3 = intent2;
                            j12 = j;
                            j2 = j12;
                            j3 = j2;
                            i = -1;
                            i2 = 0;
                            jArr = null;
                            i3 = -1;
                            str3 = null;
                            z6 = false;
                            z7 = false;
                            z19 = z5;
                            intent6 = intent3;
                            z10 = false;
                            z18 = z19;
                            intent5 = intent6;
                            z9 = false;
                            z8 = false;
                            z11 = z18;
                            intent4 = intent5;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            z16 = false;
                            z17 = false;
                            z20 = z11;
                            intent7 = intent4;
                            str4 = null;
                            str5 = null;
                            str6 = null;
                            i4 = 0;
                            z21 = false;
                            i5 = 0;
                            c = 0;
                            i6 = z20;
                            intent8 = intent7;
                        }
                        z34 = true;
                        if (z34) {
                        }
                        intent2 = intent;
                        launchActivity = this;
                        str = " ";
                        j = 0;
                        str2 = "message_id";
                        iArr = iArr3;
                        z5 = false;
                        intent3 = intent2;
                        j12 = j;
                        j2 = j12;
                        j3 = j2;
                        i = -1;
                        i2 = 0;
                        jArr = null;
                        i3 = -1;
                        str3 = null;
                        z6 = false;
                        z7 = false;
                        z19 = z5;
                        intent6 = intent3;
                        z10 = false;
                        z18 = z19;
                        intent5 = intent6;
                        z9 = false;
                        z8 = false;
                        z11 = z18;
                        intent4 = intent5;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        z17 = false;
                        z20 = z11;
                        intent7 = intent4;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        i4 = 0;
                        z21 = false;
                        i5 = 0;
                        c = 0;
                        i6 = z20;
                        intent8 = intent7;
                    }
                }
                arrayList = parcelableArrayListExtra;
                if (arrayList != null) {
                }
                z34 = true;
                if (z34) {
                }
                intent2 = intent;
                launchActivity = this;
                str = " ";
                j = 0;
                str2 = "message_id";
                iArr = iArr3;
                z5 = false;
                intent3 = intent2;
                j12 = j;
                j2 = j12;
                j3 = j2;
                i = -1;
                i2 = 0;
                jArr = null;
                i3 = -1;
                str3 = null;
                z6 = false;
                z7 = false;
                z19 = z5;
                intent6 = intent3;
                z10 = false;
                z18 = z19;
                intent5 = intent6;
                z9 = false;
                z8 = false;
                z11 = z18;
                intent4 = intent5;
                z12 = false;
                z13 = false;
                z14 = false;
                z15 = false;
                z16 = false;
                z17 = false;
                z20 = z11;
                intent7 = intent4;
                str4 = null;
                str5 = null;
                str6 = null;
                i4 = 0;
                z21 = false;
                i5 = 0;
                c = 0;
                i6 = z20;
                intent8 = intent7;
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
                                str14 = " ";
                                final String uri10 = data.toString();
                                if (uri10.startsWith("tg:premium_offer") || uri10.startsWith("tg://premium_offer")) {
                                    j6 = 0;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.lambda$handleIntent$15(uri10);
                                        }
                                    });
                                    str15 = "message_id";
                                    j5 = j6;
                                    j4 = j5;
                                    str30 = null;
                                    str29 = null;
                                    str28 = null;
                                    str27 = null;
                                    str26 = null;
                                    str25 = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str19 = null;
                                    z39 = false;
                                    str18 = null;
                                    num3 = null;
                                    l = null;
                                    num2 = null;
                                    num = null;
                                    str17 = null;
                                    hashMap = null;
                                    str16 = null;
                                    i9 = 0;
                                    c5 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    z17 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    str43 = null;
                                    str42 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str41 = null;
                                    str40 = null;
                                    str39 = null;
                                    str38 = null;
                                    i10 = -1;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    i11 = 0;
                                    z41 = false;
                                    break;
                                } else if (uri10.startsWith("tg:resolve") || uri10.startsWith("tg://resolve")) {
                                    Uri parse = Uri.parse(uri10.replace("tg:resolve", "tg://telegram.org").replace("tg://resolve", "tg://telegram.org"));
                                    String queryParameter3 = parse.getQueryParameter("domain");
                                    if (queryParameter3 == null && (queryParameter3 = parse.getQueryParameter("phone")) != null && queryParameter3.startsWith("+")) {
                                        queryParameter3 = queryParameter3.substring(1);
                                    }
                                    String queryParameter4 = parse.getQueryParameter("appname");
                                    String queryParameter5 = parse.getQueryParameter("startapp");
                                    if ("telegrampassport".equals(queryParameter3)) {
                                        HashMap<String, String> hashMap2 = new HashMap<>();
                                        String queryParameter6 = parse.getQueryParameter("scope");
                                        if (!TextUtils.isEmpty(queryParameter6) && queryParameter6.startsWith("{") && queryParameter6.endsWith("}")) {
                                            hashMap2.put("nonce", parse.getQueryParameter("nonce"));
                                        } else {
                                            hashMap2.put("payload", parse.getQueryParameter("payload"));
                                        }
                                        hashMap2.put("bot_id", parse.getQueryParameter("bot_id"));
                                        hashMap2.put("scope", queryParameter6);
                                        hashMap2.put("public_key", parse.getQueryParameter("public_key"));
                                        hashMap2.put("callback_url", parse.getQueryParameter("callback_url"));
                                        hashMap = hashMap2;
                                        str34 = queryParameter4;
                                        str33 = queryParameter5;
                                        str15 = "message_id";
                                        j5 = 0;
                                        j4 = 0;
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else {
                                        String queryParameter7 = parse.getQueryParameter("start");
                                        String queryParameter8 = parse.getQueryParameter("startgroup");
                                        str23 = parse.getQueryParameter("startchannel");
                                        String queryParameter9 = parse.getQueryParameter("admin");
                                        String str80 = queryParameter3;
                                        String queryParameter10 = parse.getQueryParameter("game");
                                        String queryParameter11 = parse.getQueryParameter("voicechat");
                                        String queryParameter12 = parse.getQueryParameter("livestream");
                                        String queryParameter13 = parse.getQueryParameter("startattach");
                                        String queryParameter14 = parse.getQueryParameter("choose");
                                        String queryParameter15 = parse.getQueryParameter("attach");
                                        Integer parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                        str18 = queryParameter7;
                                        int intValue = Utilities.parseInt((CharSequence) parse.getQueryParameter("story")).intValue();
                                        num3 = parseInt5.intValue() == 0 ? null : parseInt5;
                                        Integer parseInt6 = Utilities.parseInt((CharSequence) parse.getQueryParameter("thread"));
                                        if (parseInt6.intValue() == 0) {
                                            parseInt6 = null;
                                        }
                                        if (parseInt6 == null) {
                                            parseInt6 = Utilities.parseInt((CharSequence) parse.getQueryParameter("topic"));
                                            if (parseInt6.intValue() == 0) {
                                                num4 = null;
                                                parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                str17 = queryParameter10;
                                                if (parseInt.intValue() != 0) {
                                                    i11 = intValue;
                                                    str34 = queryParameter4;
                                                    str33 = queryParameter5;
                                                    str15 = "message_id";
                                                    str28 = str80;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str39 = queryParameter11;
                                                    str38 = queryParameter12;
                                                    str37 = queryParameter13;
                                                    str35 = queryParameter14;
                                                    str36 = queryParameter15;
                                                    num2 = num4;
                                                    str30 = null;
                                                    str29 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str21 = null;
                                                    str19 = null;
                                                    z39 = false;
                                                    l = null;
                                                    num = null;
                                                } else {
                                                    num = parseInt;
                                                    i11 = intValue;
                                                    str34 = queryParameter4;
                                                    str33 = queryParameter5;
                                                    str15 = "message_id";
                                                    str28 = str80;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str39 = queryParameter11;
                                                    str38 = queryParameter12;
                                                    str37 = queryParameter13;
                                                    str35 = queryParameter14;
                                                    str36 = queryParameter15;
                                                    num2 = num4;
                                                    str30 = null;
                                                    str29 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str21 = null;
                                                    str19 = null;
                                                    z39 = false;
                                                    l = null;
                                                }
                                                hashMap = null;
                                                str16 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                z17 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                str43 = null;
                                                str42 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str41 = null;
                                                str40 = null;
                                                i10 = -1;
                                                z41 = false;
                                                str20 = queryParameter8;
                                                str22 = queryParameter9;
                                                str25 = null;
                                                str24 = null;
                                                break;
                                            }
                                        }
                                        num4 = parseInt6;
                                        parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                        str17 = queryParameter10;
                                        if (parseInt.intValue() != 0) {
                                        }
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        i10 = -1;
                                        z41 = false;
                                        str20 = queryParameter8;
                                        str22 = queryParameter9;
                                        str25 = null;
                                        str24 = null;
                                    }
                                } else if (uri10.startsWith("tg:invoice") || uri10.startsWith("tg://invoice")) {
                                    str41 = Uri.parse(uri10.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                    str15 = "message_id";
                                    j5 = 0;
                                    j4 = 0;
                                    str30 = null;
                                    str29 = null;
                                    str28 = null;
                                    str27 = null;
                                    str26 = null;
                                    str25 = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str19 = null;
                                    z39 = false;
                                    str18 = null;
                                    num3 = null;
                                    l = null;
                                    num2 = null;
                                    num = null;
                                    str17 = null;
                                    hashMap = null;
                                    str16 = null;
                                    i9 = 0;
                                    c5 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    z17 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    str43 = null;
                                    str42 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str40 = null;
                                    str39 = null;
                                    str38 = null;
                                    i10 = -1;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    i11 = 0;
                                    z41 = false;
                                } else if (uri10.startsWith("tg:contact") || uri10.startsWith("tg://contact")) {
                                    str21 = Uri.parse(uri10.replace("tg:contact", "tg://contact")).getQueryParameter("token");
                                    str15 = "message_id";
                                    j5 = 0;
                                    j4 = 0;
                                    str30 = null;
                                    str29 = null;
                                    str28 = null;
                                    str27 = null;
                                    str26 = null;
                                    str25 = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str20 = null;
                                    str19 = null;
                                    z39 = false;
                                    str18 = null;
                                    num3 = null;
                                    l = null;
                                    num2 = null;
                                    num = null;
                                    str17 = null;
                                    hashMap = null;
                                    str16 = null;
                                    i9 = 0;
                                    c5 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    z17 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    str43 = null;
                                    str42 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str41 = null;
                                    str40 = null;
                                    str39 = null;
                                    str38 = null;
                                    i10 = -1;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    i11 = 0;
                                    z41 = false;
                                } else if (uri10.startsWith("tg:privatepost") || uri10.startsWith("tg://privatepost")) {
                                    Uri parse2 = Uri.parse(uri10.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                    Integer parseInt7 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("post"));
                                    Long parseLong2 = Utilities.parseLong(parse2.getQueryParameter("channel"));
                                    if (parseInt7.intValue() != 0) {
                                        j7 = 0;
                                        break;
                                    } else {
                                        j7 = 0;
                                    }
                                    parseInt7 = null;
                                    parseLong2 = null;
                                    Integer parseInt8 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("thread"));
                                    if (parseInt8.intValue() == 0) {
                                        parseInt8 = null;
                                    }
                                    if (parseInt8 == null) {
                                        parseInt8 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("topic"));
                                        if (parseInt8.intValue() == 0) {
                                            parseInt8 = null;
                                        }
                                    }
                                    Integer parseInt9 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("comment"));
                                    if (parseInt9.intValue() == 0) {
                                        num3 = parseInt7;
                                        l = parseLong2;
                                        num2 = parseInt8;
                                        str15 = "message_id";
                                        j5 = j7;
                                        j4 = j5;
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else {
                                        num = parseInt9;
                                        num3 = parseInt7;
                                        l = parseLong2;
                                        num2 = parseInt8;
                                        str15 = "message_id";
                                        j5 = j7;
                                        j4 = j5;
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    }
                                } else if (uri10.startsWith("tg:bg") || uri10.startsWith("tg://bg")) {
                                    Uri parse3 = Uri.parse(uri10.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = new TLRPC$TL_wallPaper();
                                    tLRPC$TL_wallPaper3.settings = new TLRPC$TL_wallPaperSettings();
                                    String queryParameter16 = parse3.getQueryParameter("slug");
                                    tLRPC$TL_wallPaper3.slug = queryParameter16;
                                    if (queryParameter16 == null) {
                                        tLRPC$TL_wallPaper3.slug = parse3.getQueryParameter("color");
                                    }
                                    String str81 = tLRPC$TL_wallPaper3.slug;
                                    if (str81 != null && str81.length() == 6) {
                                        tLRPC$TL_wallPaper3.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug, 16) | (-16777216);
                                        tLRPC$TL_wallPaper3.slug = null;
                                        z44 = true;
                                    } else {
                                        String str82 = tLRPC$TL_wallPaper3.slug;
                                        if (str82 != null && str82.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper3.slug.charAt(6))) {
                                            tLRPC$TL_wallPaper3.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug.substring(0, 6), 16) | (-16777216);
                                            try {
                                                tLRPC$TL_wallPaper3.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug.substring(7, 13), 16) | (-16777216);
                                                if (tLRPC$TL_wallPaper3.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper3.slug.charAt(13))) {
                                                    tLRPC$TL_wallPaper3.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug.substring(14, 20), 16) | (-16777216);
                                                }
                                                if (tLRPC$TL_wallPaper3.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper3.slug.charAt(20))) {
                                                    tLRPC$TL_wallPaper3.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug.substring(21), 16) | (-16777216);
                                                }
                                                try {
                                                    String queryParameter17 = parse3.getQueryParameter("rotation");
                                                    if (!TextUtils.isEmpty(queryParameter17)) {
                                                        tLRPC$TL_wallPaper3.settings.rotation = Utilities.parseInt((CharSequence) queryParameter17).intValue();
                                                    }
                                                } catch (Exception unused) {
                                                }
                                                try {
                                                    tLRPC$TL_wallPaper3.slug = null;
                                                    z44 = true;
                                                } catch (Exception unused2) {
                                                }
                                            } catch (Exception unused3) {
                                            }
                                        }
                                        z44 = false;
                                        if (!z44) {
                                            String queryParameter18 = parse3.getQueryParameter("mode");
                                            if (queryParameter18 != null && (split = queryParameter18.toLowerCase().split(str14)) != null && split.length > 0) {
                                                for (int i23 = 0; i23 < split.length; i23++) {
                                                    if ("blur".equals(split[i23])) {
                                                        tLRPC$TL_wallPaper3.settings.blur = true;
                                                    } else if ("motion".equals(split[i23])) {
                                                        tLRPC$TL_wallPaper3.settings.motion = true;
                                                    }
                                                }
                                            }
                                            tLRPC$TL_wallPaper3.settings.intensity = Utilities.parseInt((CharSequence) parse3.getQueryParameter("intensity")).intValue();
                                            try {
                                                queryParameter2 = parse3.getQueryParameter("bg_color");
                                            } catch (Exception unused4) {
                                            }
                                            try {
                                                if (!TextUtils.isEmpty(queryParameter2)) {
                                                    tLRPC$TL_wallPaper3.settings.background_color = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                                    if (queryParameter2.length() >= 13) {
                                                        try {
                                                            tLRPC$TL_wallPaper3.settings.second_background_color = Integer.parseInt(queryParameter2.substring(8, 13), 16) | (-16777216);
                                                            if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                                tLRPC$TL_wallPaper3.settings.third_background_color = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                                            }
                                                            if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                                tLRPC$TL_wallPaper3.settings.fourth_background_color = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                                            }
                                                        } catch (Exception unused5) {
                                                        }
                                                        queryParameter = parse3.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter)) {
                                                            tLRPC$TL_wallPaper3.settings.rotation = Utilities.parseInt((CharSequence) queryParameter).intValue();
                                                        }
                                                    }
                                                }
                                                queryParameter = parse3.getQueryParameter("rotation");
                                                if (!TextUtils.isEmpty(queryParameter)) {
                                                }
                                            } catch (Exception unused6) {
                                            }
                                        }
                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper3;
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    }
                                    if (!z44) {
                                    }
                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper3;
                                    str15 = "message_id";
                                    str30 = null;
                                    str29 = null;
                                    str28 = null;
                                    str27 = null;
                                    str26 = null;
                                    str25 = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str19 = null;
                                    z39 = false;
                                    str18 = null;
                                    num3 = null;
                                    l = null;
                                    num2 = null;
                                    num = null;
                                    str17 = null;
                                    hashMap = null;
                                    str16 = null;
                                    i9 = 0;
                                    c5 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    z17 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    j5 = 0;
                                    j4 = 0;
                                    str43 = null;
                                    str42 = null;
                                    str41 = null;
                                    str40 = null;
                                    str39 = null;
                                    str38 = null;
                                    i10 = -1;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    i11 = 0;
                                    z41 = false;
                                } else {
                                    if (uri10.startsWith("tg:join") || uri10.startsWith("tg://join")) {
                                        str26 = Uri.parse(uri10.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str25 = null;
                                    } else if (uri10.startsWith("tg:addstickers") || uri10.startsWith("tg://addstickers")) {
                                        str25 = Uri.parse(uri10.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                    } else if (uri10.startsWith("tg:addemoji") || uri10.startsWith("tg://addemoji")) {
                                        str24 = Uri.parse(uri10.replace("tg:addemoji", "tg://telegram.org").replace("tg://addemoji", "tg://telegram.org")).getQueryParameter("set");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:msg") || uri10.startsWith("tg://msg") || uri10.startsWith("tg://share") || uri10.startsWith("tg:share")) {
                                        Uri parse4 = Uri.parse(uri10.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                        String queryParameter19 = parse4.getQueryParameter("url");
                                        str8 = queryParameter19 != null ? queryParameter19 : "";
                                        if (parse4.getQueryParameter("text") != null) {
                                            if (str8.length() > 0) {
                                                str8 = str8 + "\n";
                                                z45 = true;
                                            } else {
                                                z45 = false;
                                            }
                                            str8 = str8 + parse4.getQueryParameter("text");
                                        } else {
                                            z45 = false;
                                        }
                                        if (str8.length() > 16384) {
                                            i13 = 0;
                                            str30 = str8.substring(0, LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                                        } else {
                                            i13 = 0;
                                            str30 = str8;
                                        }
                                        while (str30.endsWith("\n")) {
                                            str30 = str30.substring(i13, str30.length() - 1);
                                        }
                                        z39 = z45;
                                        str15 = "message_id";
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:confirmphone") || uri10.startsWith("tg://confirmphone")) {
                                        Uri parse5 = Uri.parse(uri10.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                        str27 = parse5.getQueryParameter("phone");
                                        str15 = "message_id";
                                        str28 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                        str29 = parse5.getQueryParameter("hash");
                                        str30 = null;
                                        break;
                                    } else if (uri10.startsWith("tg:login") || uri10.startsWith("tg://login")) {
                                        Uri parse6 = Uri.parse(uri10.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                        String queryParameter20 = parse6.getQueryParameter("token");
                                        int intValue2 = Utilities.parseInt((CharSequence) parse6.getQueryParameter("code")).intValue();
                                        str43 = intValue2 != 0 ? "" + intValue2 : null;
                                        str42 = queryParameter20;
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:openmessage") || uri10.startsWith("tg://openmessage")) {
                                        Uri parse7 = Uri.parse(uri10.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                        String queryParameter21 = parse7.getQueryParameter("user_id");
                                        String queryParameter22 = parse7.getQueryParameter("chat_id");
                                        String queryParameter23 = parse7.getQueryParameter("message_id");
                                        if (queryParameter21 != null) {
                                            j8 = Long.parseLong(queryParameter21);
                                            j9 = 0;
                                            if (queryParameter23 != null) {
                                                try {
                                                    parseInt2 = Integer.parseInt(queryParameter23);
                                                } catch (NumberFormatException unused7) {
                                                }
                                                i9 = parseInt2;
                                                j5 = j8;
                                                str15 = "message_id";
                                                j4 = j9;
                                                str30 = null;
                                                str29 = null;
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str23 = null;
                                                str22 = null;
                                                str21 = null;
                                                str20 = null;
                                                str19 = null;
                                                z39 = false;
                                                str18 = null;
                                                num3 = null;
                                                l = null;
                                                num2 = null;
                                                num = null;
                                                str17 = null;
                                                hashMap = null;
                                                str16 = null;
                                                c5 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                z17 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                str43 = null;
                                                str42 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str41 = null;
                                                str40 = null;
                                                str39 = null;
                                                str38 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str36 = null;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                i11 = 0;
                                                z41 = false;
                                            }
                                            parseInt2 = 0;
                                            i9 = parseInt2;
                                            j5 = j8;
                                            str15 = "message_id";
                                            j4 = j9;
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            c5 = 0;
                                            z13 = false;
                                            z40 = false;
                                            z38 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            z17 = false;
                                            str31 = null;
                                            str11 = null;
                                            str12 = null;
                                            str13 = null;
                                            str32 = null;
                                            str43 = null;
                                            str42 = null;
                                            tLRPC$TL_wallPaper = null;
                                            str41 = null;
                                            str40 = null;
                                            str39 = null;
                                            str38 = null;
                                            i10 = -1;
                                            str37 = null;
                                            str36 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            i11 = 0;
                                            z41 = false;
                                        } else {
                                            if (queryParameter22 != null) {
                                                j9 = Long.parseLong(queryParameter22);
                                                j8 = 0;
                                                if (queryParameter23 != null) {
                                                }
                                                parseInt2 = 0;
                                                i9 = parseInt2;
                                                j5 = j8;
                                                str15 = "message_id";
                                                j4 = j9;
                                                str30 = null;
                                                str29 = null;
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str23 = null;
                                                str22 = null;
                                                str21 = null;
                                                str20 = null;
                                                str19 = null;
                                                z39 = false;
                                                str18 = null;
                                                num3 = null;
                                                l = null;
                                                num2 = null;
                                                num = null;
                                                str17 = null;
                                                hashMap = null;
                                                str16 = null;
                                                c5 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                z17 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                str43 = null;
                                                str42 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str41 = null;
                                                str40 = null;
                                                str39 = null;
                                                str38 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str36 = null;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                i11 = 0;
                                                z41 = false;
                                            }
                                            j8 = 0;
                                            j9 = 0;
                                            if (queryParameter23 != null) {
                                            }
                                            parseInt2 = 0;
                                            i9 = parseInt2;
                                            j5 = j8;
                                            str15 = "message_id";
                                            j4 = j9;
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            c5 = 0;
                                            z13 = false;
                                            z40 = false;
                                            z38 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            z17 = false;
                                            str31 = null;
                                            str11 = null;
                                            str12 = null;
                                            str13 = null;
                                            str32 = null;
                                            str43 = null;
                                            str42 = null;
                                            tLRPC$TL_wallPaper = null;
                                            str41 = null;
                                            str40 = null;
                                            str39 = null;
                                            str38 = null;
                                            i10 = -1;
                                            str37 = null;
                                            str36 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            i11 = 0;
                                            z41 = false;
                                        }
                                    } else if (uri10.startsWith("tg:passport") || uri10.startsWith("tg://passport") || uri10.startsWith("tg:secureid")) {
                                        Uri parse8 = Uri.parse(uri10.replace("tg:passport", "tg://telegram.org").replace("tg://passport", "tg://telegram.org").replace("tg:secureid", "tg://telegram.org"));
                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                        String queryParameter24 = parse8.getQueryParameter("scope");
                                        if (!TextUtils.isEmpty(queryParameter24) && queryParameter24.startsWith("{") && queryParameter24.endsWith("}")) {
                                            hashMap3.put("nonce", parse8.getQueryParameter("nonce"));
                                        } else {
                                            hashMap3.put("payload", parse8.getQueryParameter("payload"));
                                        }
                                        hashMap3.put("bot_id", parse8.getQueryParameter("bot_id"));
                                        hashMap3.put("scope", queryParameter24);
                                        hashMap3.put("public_key", parse8.getQueryParameter("public_key"));
                                        hashMap3.put("callback_url", parse8.getQueryParameter("callback_url"));
                                        hashMap = hashMap3;
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:setlanguage") || uri10.startsWith("tg://setlanguage")) {
                                        str16 = Uri.parse(uri10.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:addtheme") || uri10.startsWith("tg://addtheme")) {
                                        str40 = Uri.parse(uri10.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:settings") || uri10.startsWith("tg://settings")) {
                                        if (uri10.contains("themes")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 2;
                                        } else if (uri10.contains("devices")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 3;
                                        } else if (uri10.contains("folders")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 4;
                                        } else if (uri10.contains("change_number")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 5;
                                        } else if (uri10.contains("?enablelogs")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 7;
                                        } else if (uri10.contains("?sendlogs")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = '\b';
                                        } else if (uri10.contains("?disablelogs")) {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = '\t';
                                        } else {
                                            str15 = "message_id";
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 1;
                                        }
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:search") || uri10.startsWith("tg://search")) {
                                        String queryParameter25 = Uri.parse(uri10.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                        str31 = queryParameter25 != null ? queryParameter25.trim() : "";
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:calllog") || uri10.startsWith("tg://calllog")) {
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = true;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:call") || uri10.startsWith("tg://call")) {
                                        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                            if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                String queryParameter26 = data.getQueryParameter("format");
                                                String queryParameter27 = data.getQueryParameter("name");
                                                String queryParameter28 = data.getQueryParameter("phone");
                                                List<TLRPC$TL_contact> findContacts = findContacts(queryParameter27, queryParameter28, false);
                                                if (!findContacts.isEmpty() || queryParameter28 == null) {
                                                    j10 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                    if (j10 != 0) {
                                                        str8 = null;
                                                    } else if (queryParameter27 != null) {
                                                        str8 = queryParameter27;
                                                    }
                                                    boolean equalsIgnoreCase = MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(queryParameter26);
                                                    z46 = !equalsIgnoreCase;
                                                    z47 = equalsIgnoreCase;
                                                    str44 = str8;
                                                    z48 = true;
                                                    z49 = false;
                                                } else {
                                                    str45 = queryParameter28;
                                                    str46 = queryParameter27;
                                                    z48 = false;
                                                    z49 = true;
                                                    z46 = false;
                                                    z47 = false;
                                                    str44 = null;
                                                    j10 = 0;
                                                    z14 = z48;
                                                    z16 = z49;
                                                    z40 = z46;
                                                    z38 = z47;
                                                    str15 = "message_id";
                                                    str11 = str44;
                                                    j5 = j10;
                                                    str12 = str46;
                                                    str13 = str45;
                                                    str30 = null;
                                                    str29 = null;
                                                    str28 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str25 = null;
                                                    str24 = null;
                                                    str23 = null;
                                                    str22 = null;
                                                    str21 = null;
                                                    str20 = null;
                                                    str19 = null;
                                                    z39 = false;
                                                    str18 = null;
                                                    num3 = null;
                                                    l = null;
                                                    num2 = null;
                                                    num = null;
                                                    str17 = null;
                                                    hashMap = null;
                                                    str16 = null;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z13 = false;
                                                    z15 = false;
                                                    z17 = false;
                                                    str31 = null;
                                                    str32 = null;
                                                    j4 = 0;
                                                    str43 = null;
                                                    str42 = null;
                                                    tLRPC$TL_wallPaper = null;
                                                    str41 = null;
                                                    str40 = null;
                                                    str39 = null;
                                                    str38 = null;
                                                    i10 = -1;
                                                    str37 = null;
                                                    str36 = null;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    i11 = 0;
                                                    z41 = false;
                                                }
                                            } else {
                                                final Intent intent9 = new Intent(intent);
                                                intent9.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                intent9.putExtra("extra_force_call", true);
                                                ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                                                    @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                    public final void onResult(boolean z61) {
                                                        LaunchActivity.this.lambda$handleIntent$16(intent9, z61);
                                                    }
                                                }, 1000L);
                                                z48 = false;
                                                z49 = false;
                                                z46 = false;
                                                z47 = false;
                                                str44 = null;
                                                j10 = 0;
                                            }
                                            str46 = null;
                                            str45 = null;
                                            z14 = z48;
                                            z16 = z49;
                                            z40 = z46;
                                            z38 = z47;
                                            str15 = "message_id";
                                            str11 = str44;
                                            j5 = j10;
                                            str12 = str46;
                                            str13 = str45;
                                            str30 = null;
                                            str29 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str23 = null;
                                            str22 = null;
                                            str21 = null;
                                            str20 = null;
                                            str19 = null;
                                            z39 = false;
                                            str18 = null;
                                            num3 = null;
                                            l = null;
                                            num2 = null;
                                            num = null;
                                            str17 = null;
                                            hashMap = null;
                                            str16 = null;
                                            i9 = 0;
                                            c5 = 0;
                                            z13 = false;
                                            z15 = false;
                                            z17 = false;
                                            str31 = null;
                                            str32 = null;
                                            j4 = 0;
                                            str43 = null;
                                            str42 = null;
                                            tLRPC$TL_wallPaper = null;
                                            str41 = null;
                                            str40 = null;
                                            str39 = null;
                                            str38 = null;
                                            i10 = -1;
                                            str37 = null;
                                            str36 = null;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            i11 = 0;
                                            z41 = false;
                                        }
                                        j6 = 0;
                                        str15 = "message_id";
                                        j5 = j6;
                                        j4 = j5;
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:scanqr") || uri10.startsWith("tg://scanqr")) {
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = true;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:addcontact") || uri10.startsWith("tg://addcontact")) {
                                        Uri parse9 = Uri.parse(uri10.replace("tg:addcontact", "tg://telegram.org").replace("tg://addcontact", "tg://telegram.org"));
                                        String queryParameter29 = parse9.getQueryParameter("name");
                                        str13 = parse9.getQueryParameter("phone");
                                        str12 = queryParameter29;
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = true;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else if (uri10.startsWith("tg:addlist") || uri10.startsWith("tg://addlist")) {
                                        str19 = Uri.parse(uri10.replace("tg:addlist", "tg://telegram.org").replace("tg://addlist", "tg://telegram.org")).getQueryParameter("slug");
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    } else {
                                        String replace = uri10.replace("tg://", "").replace("tg:", "");
                                        int indexOf = replace.indexOf(63);
                                        if (indexOf >= 0) {
                                            replace = replace.substring(0, indexOf);
                                        }
                                        str32 = replace;
                                        str15 = "message_id";
                                        str30 = null;
                                        str29 = null;
                                        str28 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str23 = null;
                                        str22 = null;
                                        str21 = null;
                                        str20 = null;
                                        str19 = null;
                                        z39 = false;
                                        str18 = null;
                                        num3 = null;
                                        l = null;
                                        num2 = null;
                                        num = null;
                                        str17 = null;
                                        hashMap = null;
                                        str16 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        z17 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str43 = null;
                                        str42 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        str38 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str36 = null;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i11 = 0;
                                        z41 = false;
                                    }
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str19 = null;
                                    z39 = false;
                                    str18 = null;
                                    num3 = null;
                                    l = null;
                                    num2 = null;
                                    num = null;
                                    str17 = null;
                                    hashMap = null;
                                    str16 = null;
                                    i9 = 0;
                                    c5 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    z17 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    j5 = 0;
                                    j4 = 0;
                                    str43 = null;
                                    str42 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str41 = null;
                                    str40 = null;
                                    str39 = null;
                                    str38 = null;
                                    i10 = -1;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    i11 = 0;
                                    z41 = false;
                                }
                                break;
                            case 1:
                            case 2:
                                String lowerCase = data.getHost().toLowerCase();
                                Matcher matcher = PREFIX_T_ME_PATTERN.matcher(lowerCase);
                                boolean find = matcher.find();
                                if (lowerCase.equals("telegram.me") || lowerCase.equals("t.me") || lowerCase.equals("telegram.dog") || find) {
                                    if (find) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("https://t.me/");
                                        sb.append(matcher.group(1));
                                        sb.append(TextUtils.isEmpty(data.getPath()) ? "" : data.getPath());
                                        sb.append(TextUtils.isEmpty(data.getQuery()) ? "" : "?" + data.getQuery());
                                        data = Uri.parse(sb.toString());
                                    }
                                    String path3 = data.getPath();
                                    if (path3 == null || path3.length() <= 1) {
                                        str14 = " ";
                                    } else {
                                        String substring3 = path3.substring(1);
                                        if (substring3.startsWith("$")) {
                                            substring2 = substring3.substring(1);
                                        } else if (substring3.startsWith("invoice/")) {
                                            substring2 = substring3.substring(substring3.indexOf(47) + 1);
                                        } else if (substring3.startsWith("bg/")) {
                                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper4 = new TLRPC$TL_wallPaper();
                                            tLRPC$TL_wallPaper4.settings = new TLRPC$TL_wallPaperSettings();
                                            String replace2 = substring3.replace("bg/", "");
                                            tLRPC$TL_wallPaper4.slug = replace2;
                                            if (replace2 != null && replace2.length() == 6) {
                                                tLRPC$TL_wallPaper4.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug, 16) | (-16777216);
                                                tLRPC$TL_wallPaper4.slug = null;
                                            } else {
                                                String str83 = tLRPC$TL_wallPaper4.slug;
                                                if (str83 != null && str83.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(6))) {
                                                    tLRPC$TL_wallPaper4.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(0, 6), 16) | (-16777216);
                                                    tLRPC$TL_wallPaper4.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(7, 13), 16) | (-16777216);
                                                    if (tLRPC$TL_wallPaper4.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(13))) {
                                                        tLRPC$TL_wallPaper4.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(14, 20), 16) | (-16777216);
                                                    }
                                                    if (tLRPC$TL_wallPaper4.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(20))) {
                                                        tLRPC$TL_wallPaper4.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(21), 16) | (-16777216);
                                                    }
                                                    try {
                                                        String queryParameter30 = data.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter30)) {
                                                            tLRPC$TL_wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) queryParameter30).intValue();
                                                        }
                                                    } catch (Exception unused8) {
                                                    }
                                                    tLRPC$TL_wallPaper4.slug = null;
                                                }
                                                z54 = false;
                                                if (z54) {
                                                    String queryParameter31 = data.getQueryParameter("mode");
                                                    if (queryParameter31 != null) {
                                                        str14 = " ";
                                                        String[] split2 = queryParameter31.toLowerCase().split(str14);
                                                        if (split2 != null && split2.length > 0) {
                                                            for (int i24 = 0; i24 < split2.length; i24++) {
                                                                if ("blur".equals(split2[i24])) {
                                                                    tLRPC$TL_wallPaper4.settings.blur = true;
                                                                } else if ("motion".equals(split2[i24])) {
                                                                    tLRPC$TL_wallPaper4.settings.motion = true;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        str14 = " ";
                                                    }
                                                    String queryParameter32 = data.getQueryParameter("intensity");
                                                    if (!TextUtils.isEmpty(queryParameter32)) {
                                                        tLRPC$TL_wallPaper4.settings.intensity = Utilities.parseInt((CharSequence) queryParameter32).intValue();
                                                    } else {
                                                        tLRPC$TL_wallPaper4.settings.intensity = 50;
                                                    }
                                                    try {
                                                        String queryParameter33 = data.getQueryParameter("bg_color");
                                                        if (!TextUtils.isEmpty(queryParameter33)) {
                                                            tLRPC$TL_wallPaper4.settings.background_color = Integer.parseInt(queryParameter33.substring(0, 6), 16) | (-16777216);
                                                            if (queryParameter33.length() >= 13) {
                                                                tLRPC$TL_wallPaper4.settings.second_background_color = Integer.parseInt(queryParameter33.substring(7, 13), 16) | (-16777216);
                                                                if (queryParameter33.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter33.charAt(13))) {
                                                                    tLRPC$TL_wallPaper4.settings.third_background_color = Integer.parseInt(queryParameter33.substring(14, 20), 16) | (-16777216);
                                                                }
                                                                if (queryParameter33.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter33.charAt(20))) {
                                                                    tLRPC$TL_wallPaper4.settings.fourth_background_color = Integer.parseInt(queryParameter33.substring(21), 16) | (-16777216);
                                                                }
                                                            }
                                                        } else {
                                                            tLRPC$TL_wallPaper4.settings.background_color = -1;
                                                        }
                                                    } catch (Exception unused9) {
                                                    }
                                                    try {
                                                        String queryParameter34 = data.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter34)) {
                                                            tLRPC$TL_wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) queryParameter34).intValue();
                                                        }
                                                    } catch (Exception unused10) {
                                                    }
                                                } else {
                                                    str14 = " ";
                                                }
                                                tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper4;
                                                str47 = null;
                                                str48 = null;
                                                str49 = null;
                                                z51 = false;
                                                str24 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                i15 = -1;
                                                str59 = null;
                                                str20 = null;
                                                str58 = null;
                                                str57 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                i14 = 0;
                                                z50 = false;
                                                str50 = null;
                                                str63 = null;
                                                str65 = null;
                                                str64 = null;
                                                str66 = null;
                                                num7 = null;
                                                l2 = null;
                                                num6 = null;
                                                num5 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str26 = str48;
                                                str15 = "message_id";
                                                str34 = str62;
                                                str33 = str61;
                                                i10 = i15;
                                                str23 = str58;
                                                str22 = str57;
                                                str27 = str56;
                                                str39 = str54;
                                                str38 = str53;
                                                str29 = str52;
                                                i11 = i14;
                                                z41 = z50;
                                                str40 = str50;
                                                str43 = str63;
                                                str19 = str64;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str41 = str66;
                                                num3 = num7;
                                                l = l2;
                                                num2 = num6;
                                                str37 = str69;
                                                str36 = str68;
                                                str35 = str67;
                                                hashMap = null;
                                                i9 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                j5 = 0;
                                                j4 = 0;
                                                str42 = null;
                                                z39 = z51;
                                                str17 = str55;
                                                str16 = str51;
                                                num = num5;
                                                z17 = false;
                                                str25 = str49;
                                                str18 = str59;
                                                str21 = str65;
                                                c5 = 0;
                                                str28 = str47;
                                                str30 = str60;
                                                break;
                                            }
                                            z54 = true;
                                            if (z54) {
                                            }
                                            tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper4;
                                            str47 = null;
                                            str48 = null;
                                            str49 = null;
                                            z51 = false;
                                            str24 = null;
                                            str62 = null;
                                            str61 = null;
                                            str60 = null;
                                            i15 = -1;
                                            str59 = null;
                                            str20 = null;
                                            str58 = null;
                                            str57 = null;
                                            str56 = null;
                                            str55 = null;
                                            str54 = null;
                                            str53 = null;
                                            str52 = null;
                                            str51 = null;
                                            i14 = 0;
                                            z50 = false;
                                            str50 = null;
                                            str63 = null;
                                            str65 = null;
                                            str64 = null;
                                            str66 = null;
                                            num7 = null;
                                            l2 = null;
                                            num6 = null;
                                            num5 = null;
                                            str69 = null;
                                            str68 = null;
                                            str67 = null;
                                            str26 = str48;
                                            str15 = "message_id";
                                            str34 = str62;
                                            str33 = str61;
                                            i10 = i15;
                                            str23 = str58;
                                            str22 = str57;
                                            str27 = str56;
                                            str39 = str54;
                                            str38 = str53;
                                            str29 = str52;
                                            i11 = i14;
                                            z41 = z50;
                                            str40 = str50;
                                            str43 = str63;
                                            str19 = str64;
                                            tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                            str41 = str66;
                                            num3 = num7;
                                            l = l2;
                                            num2 = num6;
                                            str37 = str69;
                                            str36 = str68;
                                            str35 = str67;
                                            hashMap = null;
                                            i9 = 0;
                                            z13 = false;
                                            z40 = false;
                                            z38 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            str31 = null;
                                            str11 = null;
                                            str12 = null;
                                            str13 = null;
                                            str32 = null;
                                            j5 = 0;
                                            j4 = 0;
                                            str42 = null;
                                            z39 = z51;
                                            str17 = str55;
                                            str16 = str51;
                                            num = num5;
                                            z17 = false;
                                            str25 = str49;
                                            str18 = str59;
                                            str21 = str65;
                                            c5 = 0;
                                            str28 = str47;
                                            str30 = str60;
                                        } else {
                                            str14 = " ";
                                            if (substring3.startsWith("login/")) {
                                                int intValue3 = Utilities.parseInt((CharSequence) substring3.replace("login/", "")).intValue();
                                                str63 = intValue3 != 0 ? "" + intValue3 : null;
                                                str47 = null;
                                                str48 = null;
                                                str49 = null;
                                                z51 = false;
                                                str24 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                i15 = -1;
                                                str59 = null;
                                                str20 = null;
                                                str58 = null;
                                                str57 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                i14 = 0;
                                                z50 = false;
                                                str50 = null;
                                                str65 = null;
                                                str64 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str66 = null;
                                                num7 = null;
                                                l2 = null;
                                                num6 = null;
                                                num5 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str26 = str48;
                                                str15 = "message_id";
                                                str34 = str62;
                                                str33 = str61;
                                                i10 = i15;
                                                str23 = str58;
                                                str22 = str57;
                                                str27 = str56;
                                                str39 = str54;
                                                str38 = str53;
                                                str29 = str52;
                                                i11 = i14;
                                                z41 = z50;
                                                str40 = str50;
                                                str43 = str63;
                                                str19 = str64;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str41 = str66;
                                                num3 = num7;
                                                l = l2;
                                                num2 = num6;
                                                str37 = str69;
                                                str36 = str68;
                                                str35 = str67;
                                                hashMap = null;
                                                i9 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                j5 = 0;
                                                j4 = 0;
                                                str42 = null;
                                                z39 = z51;
                                                str17 = str55;
                                                str16 = str51;
                                                num = num5;
                                                z17 = false;
                                                str25 = str49;
                                                str18 = str59;
                                                str21 = str65;
                                                c5 = 0;
                                                str28 = str47;
                                                str30 = str60;
                                            } else {
                                                if (substring3.startsWith("joinchat/")) {
                                                    str47 = substring3.replace("joinchat/", "");
                                                } else if (substring3.startsWith("+")) {
                                                    str47 = substring3.replace("+", "");
                                                    if (AndroidUtilities.isNumeric(str47)) {
                                                        str48 = null;
                                                        str49 = null;
                                                        z51 = false;
                                                        str24 = null;
                                                        str62 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        i15 = -1;
                                                        str59 = null;
                                                        str20 = null;
                                                        str58 = null;
                                                        str57 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        i14 = 0;
                                                        z50 = false;
                                                        str50 = null;
                                                        str63 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        tLRPC$TL_wallPaper2 = null;
                                                        str66 = null;
                                                        num7 = null;
                                                        l2 = null;
                                                        num6 = null;
                                                        num5 = null;
                                                        str69 = null;
                                                        str68 = null;
                                                        str67 = null;
                                                        str26 = str48;
                                                        str15 = "message_id";
                                                        str34 = str62;
                                                        str33 = str61;
                                                        i10 = i15;
                                                        str23 = str58;
                                                        str22 = str57;
                                                        str27 = str56;
                                                        str39 = str54;
                                                        str38 = str53;
                                                        str29 = str52;
                                                        i11 = i14;
                                                        z41 = z50;
                                                        str40 = str50;
                                                        str43 = str63;
                                                        str19 = str64;
                                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                        str41 = str66;
                                                        num3 = num7;
                                                        l = l2;
                                                        num2 = num6;
                                                        str37 = str69;
                                                        str36 = str68;
                                                        str35 = str67;
                                                        hashMap = null;
                                                        i9 = 0;
                                                        z13 = false;
                                                        z40 = false;
                                                        z38 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        z16 = false;
                                                        str31 = null;
                                                        str11 = null;
                                                        str12 = null;
                                                        str13 = null;
                                                        str32 = null;
                                                        j5 = 0;
                                                        j4 = 0;
                                                        str42 = null;
                                                        z39 = z51;
                                                        str17 = str55;
                                                        str16 = str51;
                                                        num = num5;
                                                        z17 = false;
                                                        str25 = str49;
                                                        str18 = str59;
                                                        str21 = str65;
                                                        c5 = 0;
                                                        str28 = str47;
                                                        str30 = str60;
                                                    }
                                                } else if (substring3.startsWith("addstickers/")) {
                                                    str49 = substring3.replace("addstickers/", "");
                                                    str47 = null;
                                                    str48 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("addemoji/")) {
                                                    str24 = substring3.replace("addemoji/", "");
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("msg/") || substring3.startsWith("share/")) {
                                                    String queryParameter35 = data.getQueryParameter("url");
                                                    str8 = queryParameter35 != null ? queryParameter35 : "";
                                                    if (data.getQueryParameter("text") != null) {
                                                        if (str8.length() > 0) {
                                                            str8 = str8 + "\n";
                                                            z51 = true;
                                                        } else {
                                                            z51 = false;
                                                        }
                                                        str8 = str8 + data.getQueryParameter("text");
                                                    } else {
                                                        z51 = false;
                                                    }
                                                    if (str8.length() > 16384) {
                                                        i16 = 0;
                                                        str70 = str8.substring(0, LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                                                    } else {
                                                        i16 = 0;
                                                        str70 = str8;
                                                    }
                                                    while (str70.endsWith("\n")) {
                                                        str70 = str70.substring(i16, str70.length() - 1);
                                                    }
                                                    str60 = str70;
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("confirmphone")) {
                                                    String queryParameter36 = data.getQueryParameter("phone");
                                                    str52 = data.getQueryParameter("hash");
                                                    str56 = queryParameter36;
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("setlanguage/")) {
                                                    str51 = substring3.substring(12);
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("addtheme/")) {
                                                    str50 = substring3.substring(9);
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.equalsIgnoreCase("boost") || substring3.startsWith("boost/")) {
                                                    String queryParameter37 = data.getQueryParameter("c");
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() >= 2) {
                                                        str47 = pathSegments.get(1);
                                                    } else if (TextUtils.isEmpty(queryParameter37)) {
                                                        str47 = null;
                                                    } else {
                                                        parseLong = Utilities.parseLong(queryParameter37);
                                                        str47 = null;
                                                        l2 = parseLong;
                                                        str48 = null;
                                                        str49 = null;
                                                        z51 = false;
                                                        str24 = null;
                                                        str62 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        i15 = -1;
                                                        str59 = null;
                                                        str20 = null;
                                                        str58 = null;
                                                        str57 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        i14 = 0;
                                                        z50 = true;
                                                        str50 = null;
                                                        str63 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        tLRPC$TL_wallPaper2 = null;
                                                        str66 = null;
                                                        num7 = null;
                                                        num6 = null;
                                                        num5 = null;
                                                        str69 = null;
                                                        str68 = null;
                                                        str67 = null;
                                                        str26 = str48;
                                                        str15 = "message_id";
                                                        str34 = str62;
                                                        str33 = str61;
                                                        i10 = i15;
                                                        str23 = str58;
                                                        str22 = str57;
                                                        str27 = str56;
                                                        str39 = str54;
                                                        str38 = str53;
                                                        str29 = str52;
                                                        i11 = i14;
                                                        z41 = z50;
                                                        str40 = str50;
                                                        str43 = str63;
                                                        str19 = str64;
                                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                        str41 = str66;
                                                        num3 = num7;
                                                        l = l2;
                                                        num2 = num6;
                                                        str37 = str69;
                                                        str36 = str68;
                                                        str35 = str67;
                                                        hashMap = null;
                                                        i9 = 0;
                                                        z13 = false;
                                                        z40 = false;
                                                        z38 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        z16 = false;
                                                        str31 = null;
                                                        str11 = null;
                                                        str12 = null;
                                                        str13 = null;
                                                        str32 = null;
                                                        j5 = 0;
                                                        j4 = 0;
                                                        str42 = null;
                                                        z39 = z51;
                                                        str17 = str55;
                                                        str16 = str51;
                                                        num = num5;
                                                        z17 = false;
                                                        str25 = str49;
                                                        str18 = str59;
                                                        str21 = str65;
                                                        c5 = 0;
                                                        str28 = str47;
                                                        str30 = str60;
                                                    }
                                                    parseLong = null;
                                                    l2 = parseLong;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = true;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("c/")) {
                                                    List<String> pathSegments2 = data.getPathSegments();
                                                    if (pathSegments2.size() >= 3) {
                                                        l3 = Utilities.parseLong(pathSegments2.get(1));
                                                        num9 = Utilities.parseInt((CharSequence) pathSegments2.get(2));
                                                        if (num9.intValue() == 0 || l3.longValue() == 0) {
                                                            l3 = null;
                                                            num9 = null;
                                                        }
                                                        Integer parseInt10 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (parseInt10.intValue() == 0) {
                                                            parseInt10 = null;
                                                        }
                                                        if (parseInt10 == null) {
                                                            parseInt10 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                            if (parseInt10.intValue() == 0) {
                                                                parseInt10 = null;
                                                            }
                                                        }
                                                        if (parseInt10 != null || num9 == null || pathSegments2.size() < 4) {
                                                            Integer num11 = parseInt10;
                                                            num10 = num9;
                                                            num9 = num11;
                                                        } else {
                                                            num10 = Utilities.parseInt((CharSequence) pathSegments2.get(3));
                                                        }
                                                    } else {
                                                        l3 = null;
                                                        num9 = null;
                                                        num10 = null;
                                                    }
                                                    if (data.getQuery() == null || pathSegments2.size() != 2) {
                                                        l4 = l3;
                                                        z53 = false;
                                                    } else {
                                                        z53 = data.getQuery().equals("boost");
                                                        l4 = Utilities.parseLong(pathSegments2.get(1));
                                                    }
                                                    z50 = z53;
                                                    l2 = l4;
                                                    num6 = num9;
                                                    num7 = num10;
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else if (substring3.startsWith("contact/")) {
                                                    str65 = substring3.substring(8);
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str64 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                } else {
                                                    if (substring3.startsWith("folder/")) {
                                                        substring = substring3.substring(7);
                                                    } else if (substring3.startsWith("addlist/")) {
                                                        substring = substring3.substring(8);
                                                    } else if (substring3.length() >= 1) {
                                                        ArrayList arrayList3 = new ArrayList(data.getPathSegments());
                                                        if (arrayList3.size() > 0) {
                                                            i17 = 0;
                                                            if (((String) arrayList3.get(0)).equals("s")) {
                                                                arrayList3.remove(0);
                                                            }
                                                        } else {
                                                            i17 = 0;
                                                        }
                                                        if (arrayList3.size() > 0) {
                                                            str71 = (String) arrayList3.get(i17);
                                                            if (arrayList3.size() >= 3 && "s".equals(arrayList3.get(1))) {
                                                                try {
                                                                    i18 = Integer.parseInt((String) arrayList3.get(2));
                                                                } catch (Exception unused11) {
                                                                }
                                                                num8 = null;
                                                                str73 = null;
                                                                str72 = null;
                                                                if (num8 != null) {
                                                                }
                                                                str59 = data.getQueryParameter("start");
                                                                str20 = data.getQueryParameter("startgroup");
                                                                str58 = data.getQueryParameter("startchannel");
                                                                str57 = data.getQueryParameter("admin");
                                                                String queryParameter38 = data.getQueryParameter("game");
                                                                String queryParameter39 = data.getQueryParameter("voicechat");
                                                                String queryParameter40 = data.getQueryParameter("livestream");
                                                                String queryParameter41 = data.getQueryParameter("startattach");
                                                                String queryParameter42 = data.getQueryParameter("choose");
                                                                String queryParameter43 = data.getQueryParameter("attach");
                                                                parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                                if (data.getQuery() != null) {
                                                                }
                                                                if (parseInt3.intValue() == 0) {
                                                                }
                                                                if (parseInt3 == null) {
                                                                }
                                                                if (parseInt3 == null) {
                                                                }
                                                                z50 = z52;
                                                                parseInt4 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                                if (parseInt4.intValue() == 0) {
                                                                }
                                                                str62 = str73;
                                                                str53 = queryParameter40;
                                                                str24 = null;
                                                                str54 = queryParameter39;
                                                                str55 = queryParameter38;
                                                                str56 = null;
                                                                str26 = str48;
                                                                str15 = "message_id";
                                                                str34 = str62;
                                                                str33 = str61;
                                                                i10 = i15;
                                                                str23 = str58;
                                                                str22 = str57;
                                                                str27 = str56;
                                                                str39 = str54;
                                                                str38 = str53;
                                                                str29 = str52;
                                                                i11 = i14;
                                                                z41 = z50;
                                                                str40 = str50;
                                                                str43 = str63;
                                                                str19 = str64;
                                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                                str41 = str66;
                                                                num3 = num7;
                                                                l = l2;
                                                                num2 = num6;
                                                                str37 = str69;
                                                                str36 = str68;
                                                                str35 = str67;
                                                                hashMap = null;
                                                                i9 = 0;
                                                                z13 = false;
                                                                z40 = false;
                                                                z38 = false;
                                                                z14 = false;
                                                                z15 = false;
                                                                z16 = false;
                                                                str31 = null;
                                                                str11 = null;
                                                                str12 = null;
                                                                str13 = null;
                                                                str32 = null;
                                                                j5 = 0;
                                                                j4 = 0;
                                                                str42 = null;
                                                                z39 = z51;
                                                                str17 = str55;
                                                                str16 = str51;
                                                                num = num5;
                                                                z17 = false;
                                                                str25 = str49;
                                                                str18 = str59;
                                                                str21 = str65;
                                                                c5 = 0;
                                                                str28 = str47;
                                                                str30 = str60;
                                                            } else {
                                                                if (arrayList3.size() > 1) {
                                                                    String str84 = (String) arrayList3.get(1);
                                                                    String queryParameter44 = data.getQueryParameter("startapp");
                                                                    try {
                                                                        num8 = Utilities.parseInt((CharSequence) arrayList3.get(1));
                                                                        if (num8.intValue() == 0) {
                                                                            num8 = null;
                                                                        }
                                                                        str72 = queryParameter44;
                                                                    } catch (NumberFormatException unused12) {
                                                                        str72 = queryParameter44;
                                                                        num8 = null;
                                                                    }
                                                                    str73 = str84;
                                                                    i18 = 0;
                                                                } else if (arrayList3.size() == 1) {
                                                                    str72 = data.getQueryParameter("startapp");
                                                                    i18 = 0;
                                                                    num8 = null;
                                                                    str73 = null;
                                                                }
                                                                if (num8 != null) {
                                                                    i15 = getTimestampFromLink(data);
                                                                    str74 = str71;
                                                                } else {
                                                                    str74 = str71;
                                                                    i15 = -1;
                                                                }
                                                                str59 = data.getQueryParameter("start");
                                                                str20 = data.getQueryParameter("startgroup");
                                                                str58 = data.getQueryParameter("startchannel");
                                                                str57 = data.getQueryParameter("admin");
                                                                String queryParameter382 = data.getQueryParameter("game");
                                                                String queryParameter392 = data.getQueryParameter("voicechat");
                                                                String queryParameter402 = data.getQueryParameter("livestream");
                                                                String queryParameter412 = data.getQueryParameter("startattach");
                                                                String queryParameter422 = data.getQueryParameter("choose");
                                                                String queryParameter432 = data.getQueryParameter("attach");
                                                                parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                                if (data.getQuery() != null) {
                                                                    i14 = i18;
                                                                    z52 = data.getQuery().equals("boost");
                                                                } else {
                                                                    i14 = i18;
                                                                    z52 = false;
                                                                }
                                                                if (parseInt3.intValue() == 0) {
                                                                    parseInt3 = null;
                                                                }
                                                                if (parseInt3 == null) {
                                                                    parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                                    if (parseInt3.intValue() == 0) {
                                                                        parseInt3 = null;
                                                                    }
                                                                }
                                                                if (parseInt3 == null || num8 == null) {
                                                                    z50 = z52;
                                                                } else {
                                                                    z50 = z52;
                                                                    if (arrayList3.size() >= 3) {
                                                                        parseInt3 = num8;
                                                                        num8 = Utilities.parseInt((CharSequence) arrayList3.get(2));
                                                                    }
                                                                }
                                                                parseInt4 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                                if (parseInt4.intValue() == 0) {
                                                                    str47 = str74;
                                                                    num6 = parseInt3;
                                                                    num7 = num8;
                                                                    str61 = str72;
                                                                    str69 = queryParameter412;
                                                                    str67 = queryParameter422;
                                                                    str68 = queryParameter432;
                                                                    str48 = null;
                                                                    str49 = null;
                                                                    z51 = false;
                                                                    str60 = null;
                                                                    str52 = null;
                                                                    str51 = null;
                                                                    str50 = null;
                                                                    str63 = null;
                                                                    str65 = null;
                                                                    str64 = null;
                                                                    tLRPC$TL_wallPaper2 = null;
                                                                    str66 = null;
                                                                    l2 = null;
                                                                    num5 = null;
                                                                } else {
                                                                    num5 = parseInt4;
                                                                    num6 = parseInt3;
                                                                    num7 = num8;
                                                                    str61 = str72;
                                                                    str69 = queryParameter412;
                                                                    str67 = queryParameter422;
                                                                    str68 = queryParameter432;
                                                                    str48 = null;
                                                                    str49 = null;
                                                                    z51 = false;
                                                                    str60 = null;
                                                                    str52 = null;
                                                                    str51 = null;
                                                                    str50 = null;
                                                                    str63 = null;
                                                                    str65 = null;
                                                                    str64 = null;
                                                                    tLRPC$TL_wallPaper2 = null;
                                                                    str66 = null;
                                                                    l2 = null;
                                                                    str47 = str74;
                                                                }
                                                                str62 = str73;
                                                                str53 = queryParameter402;
                                                                str24 = null;
                                                                str54 = queryParameter392;
                                                                str55 = queryParameter382;
                                                                str56 = null;
                                                                str26 = str48;
                                                                str15 = "message_id";
                                                                str34 = str62;
                                                                str33 = str61;
                                                                i10 = i15;
                                                                str23 = str58;
                                                                str22 = str57;
                                                                str27 = str56;
                                                                str39 = str54;
                                                                str38 = str53;
                                                                str29 = str52;
                                                                i11 = i14;
                                                                z41 = z50;
                                                                str40 = str50;
                                                                str43 = str63;
                                                                str19 = str64;
                                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                                str41 = str66;
                                                                num3 = num7;
                                                                l = l2;
                                                                num2 = num6;
                                                                str37 = str69;
                                                                str36 = str68;
                                                                str35 = str67;
                                                                hashMap = null;
                                                                i9 = 0;
                                                                z13 = false;
                                                                z40 = false;
                                                                z38 = false;
                                                                z14 = false;
                                                                z15 = false;
                                                                z16 = false;
                                                                str31 = null;
                                                                str11 = null;
                                                                str12 = null;
                                                                str13 = null;
                                                                str32 = null;
                                                                j5 = 0;
                                                                j4 = 0;
                                                                str42 = null;
                                                                z39 = z51;
                                                                str17 = str55;
                                                                str16 = str51;
                                                                num = num5;
                                                                z17 = false;
                                                                str25 = str49;
                                                                str18 = str59;
                                                                str21 = str65;
                                                                c5 = 0;
                                                                str28 = str47;
                                                                str30 = str60;
                                                            }
                                                        } else {
                                                            str71 = null;
                                                        }
                                                        i18 = 0;
                                                        num8 = null;
                                                        str73 = null;
                                                        str72 = null;
                                                        if (num8 != null) {
                                                        }
                                                        str59 = data.getQueryParameter("start");
                                                        str20 = data.getQueryParameter("startgroup");
                                                        str58 = data.getQueryParameter("startchannel");
                                                        str57 = data.getQueryParameter("admin");
                                                        String queryParameter3822 = data.getQueryParameter("game");
                                                        String queryParameter3922 = data.getQueryParameter("voicechat");
                                                        String queryParameter4022 = data.getQueryParameter("livestream");
                                                        String queryParameter4122 = data.getQueryParameter("startattach");
                                                        String queryParameter4222 = data.getQueryParameter("choose");
                                                        String queryParameter4322 = data.getQueryParameter("attach");
                                                        parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (data.getQuery() != null) {
                                                        }
                                                        if (parseInt3.intValue() == 0) {
                                                        }
                                                        if (parseInt3 == null) {
                                                        }
                                                        if (parseInt3 == null) {
                                                        }
                                                        z50 = z52;
                                                        parseInt4 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                        if (parseInt4.intValue() == 0) {
                                                        }
                                                        str62 = str73;
                                                        str53 = queryParameter4022;
                                                        str24 = null;
                                                        str54 = queryParameter3922;
                                                        str55 = queryParameter3822;
                                                        str56 = null;
                                                        str26 = str48;
                                                        str15 = "message_id";
                                                        str34 = str62;
                                                        str33 = str61;
                                                        i10 = i15;
                                                        str23 = str58;
                                                        str22 = str57;
                                                        str27 = str56;
                                                        str39 = str54;
                                                        str38 = str53;
                                                        str29 = str52;
                                                        i11 = i14;
                                                        z41 = z50;
                                                        str40 = str50;
                                                        str43 = str63;
                                                        str19 = str64;
                                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                        str41 = str66;
                                                        num3 = num7;
                                                        l = l2;
                                                        num2 = num6;
                                                        str37 = str69;
                                                        str36 = str68;
                                                        str35 = str67;
                                                        hashMap = null;
                                                        i9 = 0;
                                                        z13 = false;
                                                        z40 = false;
                                                        z38 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        z16 = false;
                                                        str31 = null;
                                                        str11 = null;
                                                        str12 = null;
                                                        str13 = null;
                                                        str32 = null;
                                                        j5 = 0;
                                                        j4 = 0;
                                                        str42 = null;
                                                        z39 = z51;
                                                        str17 = str55;
                                                        str16 = str51;
                                                        num = num5;
                                                        z17 = false;
                                                        str25 = str49;
                                                        str18 = str59;
                                                        str21 = str65;
                                                        c5 = 0;
                                                        str28 = str47;
                                                        str30 = str60;
                                                    }
                                                    str64 = substring;
                                                    str47 = null;
                                                    str48 = null;
                                                    str49 = null;
                                                    z51 = false;
                                                    str24 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    i15 = -1;
                                                    str59 = null;
                                                    str20 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i14 = 0;
                                                    z50 = false;
                                                    str50 = null;
                                                    str63 = null;
                                                    str65 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str66 = null;
                                                    num7 = null;
                                                    l2 = null;
                                                    num6 = null;
                                                    num5 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str26 = str48;
                                                    str15 = "message_id";
                                                    str34 = str62;
                                                    str33 = str61;
                                                    i10 = i15;
                                                    str23 = str58;
                                                    str22 = str57;
                                                    str27 = str56;
                                                    str39 = str54;
                                                    str38 = str53;
                                                    str29 = str52;
                                                    i11 = i14;
                                                    z41 = z50;
                                                    str40 = str50;
                                                    str43 = str63;
                                                    str19 = str64;
                                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                    str41 = str66;
                                                    num3 = num7;
                                                    l = l2;
                                                    num2 = num6;
                                                    str37 = str69;
                                                    str36 = str68;
                                                    str35 = str67;
                                                    hashMap = null;
                                                    i9 = 0;
                                                    z13 = false;
                                                    z40 = false;
                                                    z38 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str31 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str13 = null;
                                                    str32 = null;
                                                    j5 = 0;
                                                    j4 = 0;
                                                    str42 = null;
                                                    z39 = z51;
                                                    str17 = str55;
                                                    str16 = str51;
                                                    num = num5;
                                                    z17 = false;
                                                    str25 = str49;
                                                    str18 = str59;
                                                    str21 = str65;
                                                    c5 = 0;
                                                    str28 = str47;
                                                    str30 = str60;
                                                }
                                                str48 = str47;
                                                str47 = null;
                                                str49 = null;
                                                z51 = false;
                                                str24 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                i15 = -1;
                                                str59 = null;
                                                str20 = null;
                                                str58 = null;
                                                str57 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                i14 = 0;
                                                z50 = false;
                                                str50 = null;
                                                str63 = null;
                                                str65 = null;
                                                str64 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str66 = null;
                                                num7 = null;
                                                l2 = null;
                                                num6 = null;
                                                num5 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str26 = str48;
                                                str15 = "message_id";
                                                str34 = str62;
                                                str33 = str61;
                                                i10 = i15;
                                                str23 = str58;
                                                str22 = str57;
                                                str27 = str56;
                                                str39 = str54;
                                                str38 = str53;
                                                str29 = str52;
                                                i11 = i14;
                                                z41 = z50;
                                                str40 = str50;
                                                str43 = str63;
                                                str19 = str64;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str41 = str66;
                                                num3 = num7;
                                                l = l2;
                                                num2 = num6;
                                                str37 = str69;
                                                str36 = str68;
                                                str35 = str67;
                                                hashMap = null;
                                                i9 = 0;
                                                z13 = false;
                                                z40 = false;
                                                z38 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str31 = null;
                                                str11 = null;
                                                str12 = null;
                                                str13 = null;
                                                str32 = null;
                                                j5 = 0;
                                                j4 = 0;
                                                str42 = null;
                                                z39 = z51;
                                                str17 = str55;
                                                str16 = str51;
                                                num = num5;
                                                z17 = false;
                                                str25 = str49;
                                                str18 = str59;
                                                str21 = str65;
                                                c5 = 0;
                                                str28 = str47;
                                                str30 = str60;
                                            }
                                        }
                                        str66 = substring2;
                                        str14 = " ";
                                        str47 = null;
                                        str48 = null;
                                        str49 = null;
                                        z51 = false;
                                        str24 = null;
                                        str62 = null;
                                        str61 = null;
                                        str60 = null;
                                        i15 = -1;
                                        str59 = null;
                                        str20 = null;
                                        str58 = null;
                                        str57 = null;
                                        str56 = null;
                                        str55 = null;
                                        str54 = null;
                                        str53 = null;
                                        str52 = null;
                                        str51 = null;
                                        i14 = 0;
                                        z50 = false;
                                        str50 = null;
                                        str63 = null;
                                        str65 = null;
                                        str64 = null;
                                        tLRPC$TL_wallPaper2 = null;
                                        num7 = null;
                                        l2 = null;
                                        num6 = null;
                                        num5 = null;
                                        str69 = null;
                                        str68 = null;
                                        str67 = null;
                                        str26 = str48;
                                        str15 = "message_id";
                                        str34 = str62;
                                        str33 = str61;
                                        i10 = i15;
                                        str23 = str58;
                                        str22 = str57;
                                        str27 = str56;
                                        str39 = str54;
                                        str38 = str53;
                                        str29 = str52;
                                        i11 = i14;
                                        z41 = z50;
                                        str40 = str50;
                                        str43 = str63;
                                        str19 = str64;
                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                        str41 = str66;
                                        num3 = num7;
                                        l = l2;
                                        num2 = num6;
                                        str37 = str69;
                                        str36 = str68;
                                        str35 = str67;
                                        hashMap = null;
                                        i9 = 0;
                                        z13 = false;
                                        z40 = false;
                                        z38 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        str31 = null;
                                        str11 = null;
                                        str12 = null;
                                        str13 = null;
                                        str32 = null;
                                        j5 = 0;
                                        j4 = 0;
                                        str42 = null;
                                        z39 = z51;
                                        str17 = str55;
                                        str16 = str51;
                                        num = num5;
                                        z17 = false;
                                        str25 = str49;
                                        str18 = str59;
                                        str21 = str65;
                                        c5 = 0;
                                        str28 = str47;
                                        str30 = str60;
                                    }
                                    str47 = null;
                                    str48 = null;
                                    str49 = null;
                                    z51 = false;
                                    str24 = null;
                                    str62 = null;
                                    str61 = null;
                                    str60 = null;
                                    i15 = -1;
                                    str59 = null;
                                    str20 = null;
                                    str58 = null;
                                    str57 = null;
                                    str56 = null;
                                    str55 = null;
                                    str54 = null;
                                    str53 = null;
                                    str52 = null;
                                    str51 = null;
                                    i14 = 0;
                                    z50 = false;
                                    str50 = null;
                                    str63 = null;
                                    str65 = null;
                                    str64 = null;
                                    tLRPC$TL_wallPaper2 = null;
                                    str66 = null;
                                    num7 = null;
                                    l2 = null;
                                    num6 = null;
                                    num5 = null;
                                    str69 = null;
                                    str68 = null;
                                    str67 = null;
                                    str26 = str48;
                                    str15 = "message_id";
                                    str34 = str62;
                                    str33 = str61;
                                    i10 = i15;
                                    str23 = str58;
                                    str22 = str57;
                                    str27 = str56;
                                    str39 = str54;
                                    str38 = str53;
                                    str29 = str52;
                                    i11 = i14;
                                    z41 = z50;
                                    str40 = str50;
                                    str43 = str63;
                                    str19 = str64;
                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                    str41 = str66;
                                    num3 = num7;
                                    l = l2;
                                    num2 = num6;
                                    str37 = str69;
                                    str36 = str68;
                                    str35 = str67;
                                    hashMap = null;
                                    i9 = 0;
                                    z13 = false;
                                    z40 = false;
                                    z38 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    str31 = null;
                                    str11 = null;
                                    str12 = null;
                                    str13 = null;
                                    str32 = null;
                                    j5 = 0;
                                    j4 = 0;
                                    str42 = null;
                                    z39 = z51;
                                    str17 = str55;
                                    str16 = str51;
                                    num = num5;
                                    z17 = false;
                                    str25 = str49;
                                    str18 = str59;
                                    str21 = str65;
                                    c5 = 0;
                                    str28 = str47;
                                    str30 = str60;
                                }
                                break;
                            default:
                                str14 = " ";
                                j6 = 0;
                                str15 = "message_id";
                                j5 = j6;
                                j4 = j5;
                                str30 = null;
                                str29 = null;
                                str28 = null;
                                str27 = null;
                                str26 = null;
                                str25 = null;
                                str24 = null;
                                str23 = null;
                                str22 = null;
                                str21 = null;
                                str20 = null;
                                str19 = null;
                                z39 = false;
                                str18 = null;
                                num3 = null;
                                l = null;
                                num2 = null;
                                num = null;
                                str17 = null;
                                hashMap = null;
                                str16 = null;
                                i9 = 0;
                                c5 = 0;
                                z13 = false;
                                z40 = false;
                                z38 = false;
                                z14 = false;
                                z15 = false;
                                z16 = false;
                                z17 = false;
                                str31 = null;
                                str11 = null;
                                str12 = null;
                                str13 = null;
                                str32 = null;
                                str43 = null;
                                str42 = null;
                                tLRPC$TL_wallPaper = null;
                                str41 = null;
                                str40 = null;
                                str39 = null;
                                str38 = null;
                                i10 = -1;
                                str37 = null;
                                str36 = null;
                                str35 = null;
                                str34 = null;
                                str33 = null;
                                i11 = 0;
                                z41 = false;
                                break;
                        }
                        if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                            FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(scheme) && str32 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                            intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                        }
                        if (str43 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                            str = str14;
                            iArr2 = iArr3;
                            launchActivity = this;
                            str9 = str15;
                            z42 = false;
                        } else if (str27 == null || str29 != null) {
                            str = str14;
                            iArr2 = iArr3;
                            str9 = str15;
                            launchActivity = this;
                            final AlertDialog alertDialog = new AlertDialog(launchActivity, 3);
                            z42 = false;
                            alertDialog.setCanCancel(false);
                            alertDialog.show();
                            tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                            tLRPC$TL_account_sendConfirmPhoneCode.hash = str29;
                            TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                            tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings;
                            tLRPC$TL_codeSettings.allow_flashcall = false;
                            boolean hasServices = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                            tLRPC$TL_codeSettings.allow_firebase = hasServices;
                            tLRPC$TL_codeSettings.allow_app_hash = hasServices;
                            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                            if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                                sharedPreferences.edit().putString("sms_hash", BuildVars.getSmsHash()).apply();
                            } else {
                                sharedPreferences.edit().remove("sms_hash").apply();
                            }
                            final Bundle bundle = new Bundle();
                            bundle.putString("phone", str27);
                            final String str85 = str27;
                            ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda131
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$handleIntent$18(alertDialog, str85, bundle, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                                }
                            }, 2);
                        } else if (str28 != null || str26 != null || str25 != null || str24 != null || str21 != null || str19 != null || str30 != null || str17 != null || str39 != null || hashMap != null || str32 != null || str16 != null || str43 != null || tLRPC$TL_wallPaper != null || str41 != null || l != null || str40 != null || str42 != null) {
                            if (str30 != null && str30.startsWith("@")) {
                                str30 = str14 + str30;
                            }
                            str9 = str15;
                            str = str14;
                            iArr2 = iArr3;
                            runLinkRequest(iArr3[0], str28, str26, str25, str24, str18, str20, str23, str22, str30, str21, str19, z39, num3, l, num2, num, str17, hashMap, str16, str32, str43, str42, tLRPC$TL_wallPaper, str41, str40, str39, str38, 0, i10, str37, str36, str35, str34, str33, progress, booleanExtra, i11, z41);
                            z42 = false;
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
                                        long j14 = query.getLong(query.getColumnIndex("data4"));
                                        int intValue4 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                        int i25 = -1;
                                        try {
                                            while (i25 < 4) {
                                                if (i25 == -1) {
                                                    try {
                                                        i12 = iArr3[0];
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        try {
                                                            query.close();
                                                            throw th;
                                                        }
                                                    }
                                                } else {
                                                    i12 = i25;
                                                }
                                                if (i25 != -1 || !MessagesStorage.getInstance(i12).containsLocalDialog(j14)) {
                                                    int i26 = i25;
                                                    if (UserConfig.getInstance(i12).getClientUserId() != intValue4) {
                                                        try {
                                                            i25 = i26 + 1;
                                                        } catch (Throwable th6) {
                                                            th = th6;
                                                            th = th;
                                                            query.close();
                                                            throw th;
                                                        }
                                                    }
                                                }
                                                iArr3[0] = i12;
                                                switchToAccount(iArr3[0], true);
                                                NotificationCenter.getInstance(iArr3[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                                string = query.getString(query.getColumnIndex("mimetype"));
                                                if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                    j5 = j14;
                                                    z43 = z40;
                                                    if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                        z38 = true;
                                                    }
                                                } else {
                                                    j5 = j14;
                                                    z43 = true;
                                                }
                                                if (query != null) {
                                                    try {
                                                        query.close();
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        z40 = z43;
                                                        FileLog.e(e);
                                                        str = str14;
                                                        iArr2 = iArr3;
                                                        launchActivity = this;
                                                        c4 = c5;
                                                        z37 = z40;
                                                        str10 = str31;
                                                        j12 = j5;
                                                        str9 = str15;
                                                        z36 = false;
                                                        str3 = str10;
                                                        z12 = z37;
                                                        c = c4;
                                                        i4 = i9;
                                                        z21 = z38;
                                                        str4 = str11;
                                                        str5 = str12;
                                                        str6 = str13;
                                                        j2 = j4;
                                                        str2 = str9;
                                                        iArr = iArr2;
                                                        i = -1;
                                                        i2 = 0;
                                                        jArr = null;
                                                        i3 = -1;
                                                        z6 = false;
                                                        z7 = false;
                                                        z10 = false;
                                                        z9 = false;
                                                        z8 = false;
                                                        j3 = 0;
                                                        i5 = 0;
                                                        intent8 = intent;
                                                        i6 = z36;
                                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        }
                                                        z22 = true;
                                                        z23 = false;
                                                        z24 = z;
                                                        z25 = false;
                                                        r9 = z22;
                                                        r12 = z23;
                                                        if (!z25) {
                                                        }
                                                        if (z56) {
                                                        }
                                                        if (!z8) {
                                                        }
                                                        intent8.setAction(r12);
                                                        return z25;
                                                    }
                                                }
                                                str = str14;
                                                z37 = z43;
                                                iArr2 = iArr3;
                                                launchActivity = this;
                                                c4 = c5;
                                                str10 = str31;
                                                j12 = j5;
                                                str9 = str15;
                                                z36 = false;
                                            }
                                            string = query.getString(query.getColumnIndex("mimetype"));
                                            if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                            }
                                            if (query != null) {
                                            }
                                            str = str14;
                                            z37 = z43;
                                            iArr2 = iArr3;
                                            launchActivity = this;
                                            c4 = c5;
                                            str10 = str31;
                                            j12 = j5;
                                            str9 = str15;
                                            z36 = false;
                                        } catch (Throwable th7) {
                                            th = th7;
                                            th = th;
                                            query.close();
                                            throw th;
                                        }
                                        NotificationCenter.getInstance(iArr3[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                }
                            }
                            z43 = z40;
                            if (query != null) {
                            }
                            str = str14;
                            z37 = z43;
                            iArr2 = iArr3;
                            launchActivity = this;
                            c4 = c5;
                            str10 = str31;
                            j12 = j5;
                            str9 = str15;
                            z36 = false;
                        }
                        c4 = c5;
                        z37 = z40;
                        str10 = str31;
                        j12 = j5;
                        z36 = z42;
                    }
                    str14 = " ";
                    j6 = 0;
                    str15 = "message_id";
                    j5 = j6;
                    j4 = j5;
                    str30 = null;
                    str29 = null;
                    str28 = null;
                    str27 = null;
                    str26 = null;
                    str25 = null;
                    str24 = null;
                    str23 = null;
                    str22 = null;
                    str21 = null;
                    str20 = null;
                    str19 = null;
                    z39 = false;
                    str18 = null;
                    num3 = null;
                    l = null;
                    num2 = null;
                    num = null;
                    str17 = null;
                    hashMap = null;
                    str16 = null;
                    i9 = 0;
                    c5 = 0;
                    z13 = false;
                    z40 = false;
                    z38 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    str31 = null;
                    str11 = null;
                    str12 = null;
                    str13 = null;
                    str32 = null;
                    str43 = null;
                    str42 = null;
                    tLRPC$TL_wallPaper = null;
                    str41 = null;
                    str40 = null;
                    str39 = null;
                    str38 = null;
                    i10 = -1;
                    str37 = null;
                    str36 = null;
                    str35 = null;
                    str34 = null;
                    str33 = null;
                    i11 = 0;
                    z41 = false;
                    if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                    }
                    if (str43 != null) {
                    }
                    if (str27 == null) {
                    }
                    str = str14;
                    iArr2 = iArr3;
                    str9 = str15;
                    launchActivity = this;
                    final AlertDialog alertDialog2 = new AlertDialog(launchActivity, 3);
                    z42 = false;
                    alertDialog2.setCanCancel(false);
                    alertDialog2.show();
                    tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                    tLRPC$TL_account_sendConfirmPhoneCode.hash = str29;
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
                    bundle2.putString("phone", str27);
                    final String str852 = str27;
                    ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda131
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$handleIntent$18(alertDialog2, str852, bundle2, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                        }
                    }, 2);
                    c4 = c5;
                    z37 = z40;
                    str10 = str31;
                    j12 = j5;
                    z36 = z42;
                } else {
                    str9 = "message_id";
                    iArr2 = iArr3;
                    launchActivity = this;
                    str = " ";
                    z36 = false;
                    j12 = 0;
                    str10 = null;
                    z37 = false;
                    c4 = 0;
                    i9 = 0;
                    z13 = false;
                    z38 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    str11 = null;
                    str12 = null;
                    str13 = null;
                    j4 = 0;
                }
                str3 = str10;
                z12 = z37;
                c = c4;
                i4 = i9;
                z21 = z38;
                str4 = str11;
                str5 = str12;
                str6 = str13;
                j2 = j4;
                str2 = str9;
                iArr = iArr2;
                i = -1;
                i2 = 0;
                jArr = null;
                i3 = -1;
                z6 = false;
                z7 = false;
                z10 = false;
                z9 = false;
                z8 = false;
                j3 = 0;
                i5 = 0;
                intent8 = intent;
                i6 = z36;
            } else {
                launchActivity = this;
                str = " ";
                z5 = false;
                i6 = 0;
                z20 = false;
                i6 = 0;
                z19 = false;
                z18 = false;
                z11 = false;
                int i27 = -1;
                if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                    intent8 = intent;
                    str2 = "message_id";
                    iArr = iArr3;
                    i = -1;
                    i2 = 0;
                    j12 = 0;
                    jArr = null;
                    i3 = -1;
                    j2 = 0;
                    str3 = null;
                    z6 = false;
                    z7 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    j3 = 0;
                    str4 = null;
                    str5 = null;
                    str6 = null;
                    i4 = 0;
                    z21 = false;
                    i5 = 0;
                    c = 1;
                } else if (intent.getAction().equals("new_dialog")) {
                    intent7 = intent;
                    str2 = "message_id";
                    iArr = iArr3;
                    i = -1;
                    i2 = 0;
                    j12 = 0;
                    jArr = null;
                    i3 = -1;
                    j2 = 0;
                    str3 = null;
                    z6 = false;
                    z7 = false;
                    z10 = false;
                    z9 = true;
                    z8 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    j3 = 0;
                    str4 = null;
                    str5 = null;
                    str6 = null;
                    i4 = 0;
                    z21 = false;
                    i5 = 0;
                    c = 0;
                    i6 = z20;
                    intent8 = intent7;
                } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                    Intent intent10 = intent;
                    long longExtra = intent10.getLongExtra("chatId", 0L);
                    long[] longArrayExtra = intent10.getLongArrayExtra("storyDialogIds");
                    long longExtra2 = intent10.getLongExtra("userId", 0L);
                    int intExtra = intent10.getIntExtra("encId", 0);
                    i3 = intent10.getIntExtra("appWidgetId", 0);
                    int intExtra2 = intent10.getIntExtra("topicId", 0);
                    if (i3 != 0) {
                        j2 = 0;
                        i27 = intent10.getIntExtra("appWidgetType", 0);
                        str2 = "message_id";
                        iArr = iArr3;
                        i8 = 0;
                        jArr = null;
                        intExtra2 = 0;
                        i7 = 0;
                        z6 = false;
                        c3 = 6;
                        j12 = 0;
                    } else {
                        str2 = "message_id";
                        int intExtra3 = intent10.getIntExtra(str2, 0);
                        if (longArrayExtra != null) {
                            iArr = iArr3;
                            NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                            jArr = longArrayExtra;
                            j12 = 0;
                            j2 = 0;
                            i7 = intExtra3;
                            i8 = 0;
                        } else {
                            iArr = iArr3;
                            if (longExtra != 0) {
                                NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                j2 = longExtra;
                                i7 = intExtra3;
                                i8 = 0;
                                jArr = null;
                                i3 = -1;
                                z6 = false;
                                c3 = 0;
                                j12 = 0;
                            } else if (longExtra2 != 0) {
                                NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                i7 = intExtra3;
                                j12 = longExtra2;
                                i8 = 0;
                                jArr = null;
                                i3 = -1;
                                intExtra2 = 0;
                                z6 = false;
                                c3 = 0;
                                j2 = 0;
                            } else if (intExtra != 0) {
                                NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                j12 = 0;
                                j2 = 0;
                                i8 = intExtra;
                                i7 = intExtra3;
                                jArr = null;
                                i3 = -1;
                                intExtra2 = 0;
                                z6 = false;
                                c3 = 0;
                            } else {
                                j12 = 0;
                                j2 = 0;
                                i7 = intExtra3;
                                i8 = 0;
                                jArr = null;
                            }
                        }
                        i3 = -1;
                        intExtra2 = 0;
                        z6 = true;
                        c3 = 0;
                    }
                    i2 = i8;
                    j3 = 0;
                    i5 = intExtra2;
                    i4 = i7;
                    c = c3;
                    i = i27;
                    str3 = null;
                    z7 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    z17 = false;
                    str4 = null;
                    str5 = null;
                    str6 = null;
                    z21 = false;
                    intent8 = intent10;
                } else {
                    Intent intent11 = intent;
                    str2 = "message_id";
                    iArr = iArr3;
                    j = 0;
                    if (intent.getAction().equals("com.tmessages.openplayer")) {
                        j12 = 0;
                        j2 = 0;
                        j3 = 0;
                        i = -1;
                        i2 = 0;
                        jArr = null;
                        i3 = -1;
                        str3 = null;
                        z6 = false;
                        z7 = true;
                        intent6 = intent11;
                        z10 = false;
                        z18 = z19;
                        intent5 = intent6;
                        z9 = false;
                        z8 = false;
                        z11 = z18;
                        intent4 = intent5;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        z17 = false;
                        z20 = z11;
                        intent7 = intent4;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        i4 = 0;
                        z21 = false;
                        i5 = 0;
                        c = 0;
                        i6 = z20;
                        intent8 = intent7;
                    } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                        j12 = 0;
                        j2 = 0;
                        j3 = 0;
                        i = -1;
                        i2 = 0;
                        jArr = null;
                        i3 = -1;
                        str3 = null;
                        z6 = false;
                        z7 = false;
                        z10 = true;
                        intent5 = intent11;
                        z9 = false;
                        z8 = false;
                        z11 = z18;
                        intent4 = intent5;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        z17 = false;
                        z20 = z11;
                        intent7 = intent4;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        i4 = 0;
                        z21 = false;
                        i5 = 0;
                        c = 0;
                        i6 = z20;
                        intent8 = intent7;
                    } else {
                        intent3 = intent11;
                        if (action.equals("voip_chat")) {
                            j12 = 0;
                            j2 = 0;
                            j3 = 0;
                            i = -1;
                            i2 = 0;
                            jArr = null;
                            i3 = -1;
                            str3 = null;
                            z6 = false;
                            z7 = false;
                            z10 = false;
                            z9 = false;
                            z8 = true;
                            intent4 = intent11;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            z16 = false;
                            z17 = false;
                            z20 = z11;
                            intent7 = intent4;
                            str4 = null;
                            str5 = null;
                            str6 = null;
                            i4 = 0;
                            z21 = false;
                            i5 = 0;
                            c = 0;
                            i6 = z20;
                            intent8 = intent7;
                        }
                        j12 = j;
                        j2 = j12;
                        j3 = j2;
                        i = -1;
                        i2 = 0;
                        jArr = null;
                        i3 = -1;
                        str3 = null;
                        z6 = false;
                        z7 = false;
                        z19 = z5;
                        intent6 = intent3;
                        z10 = false;
                        z18 = z19;
                        intent5 = intent6;
                        z9 = false;
                        z8 = false;
                        z11 = z18;
                        intent4 = intent5;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        z17 = false;
                        z20 = z11;
                        intent7 = intent4;
                        str4 = null;
                        str5 = null;
                        str6 = null;
                        i4 = 0;
                        z21 = false;
                        i5 = 0;
                        c = 0;
                        i6 = z20;
                        intent8 = intent7;
                    }
                }
            }
            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                if (str3 != null) {
                    BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                    if (lastFragment2 instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) lastFragment2;
                        if (dialogsActivity.isMainDialogList()) {
                            if (dialogsActivity.getFragmentView() != null) {
                                r3 = 1;
                                dialogsActivity.search(str3, true);
                            } else {
                                r3 = 1;
                                dialogsActivity.setInitialSearchString(str3);
                            }
                        }
                    } else {
                        r3 = 1;
                        z6 = true;
                    }
                    if (jArr == null) {
                        NotificationCenter.getInstance(iArr[i6]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[i6]);
                        launchActivity.openStories(jArr, r3);
                    } else {
                        if (j12 != 0) {
                            boolean z61 = z21;
                            if (!z12 && !z61) {
                                Bundle bundle3 = new Bundle();
                                bundle3.putLong("user_id", j12);
                                int i28 = i4;
                                if (i28 != 0) {
                                    bundle3.putInt(str2, i28);
                                }
                                if (!mainFragmentsStack.isEmpty()) {
                                    MessagesController messagesController = MessagesController.getInstance(iArr[i6]);
                                    ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                }
                                if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle3)).setNoAnimation(r3))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z33 = true;
                                }
                                z33 = false;
                            } else if (z14) {
                                BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                if (lastFragment3 != null) {
                                    AlertsCreator.createCallDialogAlert(lastFragment3, lastFragment3.getMessagesController().getUser(Long.valueOf(j12)), z61);
                                }
                            } else {
                                VoIPPendingCall.startOrSchedule(launchActivity, j12, z61, AccountInstance.getInstance(iArr[i6]));
                            }
                        } else {
                            int i29 = i4;
                            final boolean z62 = z21;
                            if (j2 != 0) {
                                Bundle bundle4 = new Bundle();
                                bundle4.putLong("chat_id", j2);
                                if (i29 != 0) {
                                    bundle4.putInt(str2, i29);
                                }
                                if (!mainFragmentsStack.isEmpty()) {
                                    MessagesController messagesController2 = MessagesController.getInstance(iArr[i6]);
                                    ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                                }
                                final ChatActivity chatActivity = new ChatActivity(bundle4);
                                final int i30 = i5;
                                if (i30 > 0) {
                                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().findTopic(j2, i30);
                                    FileLog.d("LaunchActivity openForum " + j2 + str + i30 + " TL_forumTopic " + findTopic);
                                    if (findTopic != null) {
                                        ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(-j2, i30));
                                    } else {
                                        final long j15 = j2;
                                        MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().loadTopic(j2, i30, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$handleIntent$19(j15, i30, chatActivity);
                                            }
                                        });
                                        return r3;
                                    }
                                }
                                if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(chatActivity).setNoAnimation(r3))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z33 = true;
                                }
                                z33 = false;
                            } else {
                                String str86 = str;
                                if (i2 == 0) {
                                    if (z6) {
                                        if (!AndroidUtilities.isTablet()) {
                                            launchActivity.actionBarLayout.removeAllFragments();
                                        } else if (!launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                            while (launchActivity.layersActionBarLayout.getFragmentStack().size() - (r3 == true ? 1 : 0) > 0) {
                                                INavigationLayout iNavigationLayout = launchActivity.layersActionBarLayout;
                                                iNavigationLayout.removeFragmentFromStack(iNavigationLayout.getFragmentStack().get(i6));
                                            }
                                            launchActivity.layersActionBarLayout.closeLastFragment(i6);
                                        }
                                        z24 = false;
                                        z32 = false;
                                        r9 = 1;
                                        r12 = 0;
                                        z25 = z32;
                                        if (!z25 && !z24) {
                                            if (!AndroidUtilities.isTablet()) {
                                                if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                    if (launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                                        launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment(), -2);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                    }
                                                } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                    DialogsActivity dialogsActivity2 = new DialogsActivity(r12);
                                                    dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                                    if (str3 != null) {
                                                        dialogsActivity2.setInitialSearchString(str3);
                                                    }
                                                    launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2, -2);
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r9, i6);
                                                }
                                            } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                    launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment(), -2);
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                } else {
                                                    DialogsActivity dialogsActivity3 = new DialogsActivity(r12);
                                                    dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                                                    if (str3 != null) {
                                                        dialogsActivity3.setInitialSearchString(str3);
                                                    }
                                                    launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3, -2);
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r9, i6);
                                                }
                                            }
                                            if (z4) {
                                                launchActivity.actionBarLayout.rebuildFragments(r9);
                                                if (AndroidUtilities.isTablet()) {
                                                    launchActivity.layersActionBarLayout.rebuildFragments(r9);
                                                    launchActivity.rightActionBarLayout.rebuildFragments(r9);
                                                }
                                            }
                                        }
                                        if (z56) {
                                            VoIPFragment.show(launchActivity, iArr[i6]);
                                        }
                                        if (!z8 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                                            groupCallActivity.dismiss();
                                        }
                                        intent8.setAction(r12);
                                        return z25;
                                    }
                                    if (!z7) {
                                        z23 = false;
                                        z31 = false;
                                        z31 = false;
                                        z23 = false;
                                        z31 = false;
                                        z29 = false;
                                        z23 = false;
                                        z23 = false;
                                        r12 = 0;
                                        z23 = false;
                                        z23 = false;
                                        z23 = false;
                                        z23 = false;
                                        r12 = 0;
                                        if (z10) {
                                            if (!launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                launchActivity.actionBarLayout.getFragmentStack().get(i6).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda143
                                                    @Override // org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate
                                                    public final void didSelectLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
                                                        LaunchActivity.this.lambda$handleIntent$21(iArr, sharingLocationInfo);
                                                    }
                                                }, null));
                                            }
                                        } else {
                                            Uri uri11 = launchActivity.exportingChatUri;
                                            if (uri11 != null) {
                                                launchActivity.runImportRequest(uri11, launchActivity.documentsUrisArray);
                                                z22 = true;
                                            } else if (launchActivity.importingStickers != null) {
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        LaunchActivity.this.lambda$handleIntent$22();
                                                    }
                                                });
                                            } else {
                                                if (launchActivity.videoPath == null && launchActivity.voicePath == null && launchActivity.photoPathsArray == null && launchActivity.sendingText == null && launchActivity.documentsPathsArray == null && launchActivity.contactsToSend == null && launchActivity.documentsUrisArray == null) {
                                                    char c7 = c;
                                                    if (c7 == 7 || c7 == '\b' || c7 == '\t') {
                                                        z22 = true;
                                                        z22 = true;
                                                        z22 = true;
                                                        if (!BuildVars.DEBUG_PRIVATE_VERSION) {
                                                            str7 = "Locked in release.";
                                                        } else if (c7 == 7) {
                                                            SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", i6).edit();
                                                            BuildVars.LOGS_ENABLED = true;
                                                            edit.putBoolean("logsEnabled", true).commit();
                                                            str7 = "Logs enabled.";
                                                        } else {
                                                            if (c7 == '\b') {
                                                                ProfileActivity.sendLogs(launchActivity, i6);
                                                            } else if (c7 == '\t') {
                                                                SharedPreferences.Editor edit2 = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", i6).edit();
                                                                BuildVars.LOGS_ENABLED = i6;
                                                                edit2.putBoolean("logsEnabled", i6).commit();
                                                                str7 = "Logs disabled.";
                                                            }
                                                            str7 = null;
                                                        }
                                                        if (str7 != null && (lastFragment = launchActivity.actionBarLayout.getLastFragment()) != null) {
                                                            BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.info, str7).show();
                                                        }
                                                    } else {
                                                        if (c7 != 0) {
                                                            if (c7 == r3) {
                                                                Bundle bundle5 = new Bundle();
                                                                bundle5.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                                                editWidgetActivity = new ProfileActivity(bundle5);
                                                            } else if (c7 == 2) {
                                                                editWidgetActivity = new ThemeActivity(i6);
                                                            } else if (c7 == 3) {
                                                                editWidgetActivity = new SessionsActivity(i6);
                                                            } else if (c7 == 4) {
                                                                editWidgetActivity = new FiltersSetupActivity();
                                                            } else if (c7 == 5) {
                                                                editWidgetActivity = new ActionIntroActivity(3);
                                                                z28 = true;
                                                                c2 = 6;
                                                                if (c7 == c2) {
                                                                    getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(editWidgetActivity).setNoAnimation(r3));
                                                                } else {
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            LaunchActivity.this.lambda$handleIntent$23(editWidgetActivity, z28);
                                                                        }
                                                                    });
                                                                }
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, i6);
                                                                }
                                                            } else {
                                                                c2 = 6;
                                                                editWidgetActivity = c7 == 6 ? new EditWidgetActivity(i, i3) : null;
                                                                z28 = false;
                                                                if (c7 == c2) {
                                                                }
                                                                if (AndroidUtilities.isTablet()) {
                                                                }
                                                            }
                                                            z28 = false;
                                                            c2 = 6;
                                                            if (c7 == c2) {
                                                            }
                                                            if (AndroidUtilities.isTablet()) {
                                                            }
                                                        } else if (z9) {
                                                            Bundle bundle6 = new Bundle();
                                                            bundle6.putBoolean("destroyAfterSelect", r3);
                                                            getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ContactsActivity(bundle6)).setNoAnimation(r3));
                                                            if (AndroidUtilities.isTablet()) {
                                                                launchActivity.actionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                launchActivity.rightActionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                            } else {
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, i6);
                                                            }
                                                        } else {
                                                            String str87 = str4;
                                                            if (str87 != null) {
                                                                Bundle bundle7 = new Bundle();
                                                                bundle7.putBoolean("destroyAfterSelect", r3);
                                                                bundle7.putBoolean("returnAsResult", r3);
                                                                bundle7.putBoolean("onlyUsers", r3);
                                                                bundle7.putBoolean("allowSelf", i6);
                                                                ContactsActivity contactsActivity = new ContactsActivity(bundle7);
                                                                contactsActivity.setInitialSearchString(str87);
                                                                contactsActivity.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda144
                                                                    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                                                    public final void didSelectContact(TLRPC$User tLRPC$User, String str88, ContactsActivity contactsActivity2) {
                                                                        LaunchActivity.this.lambda$handleIntent$24(z62, iArr, tLRPC$User, str88, contactsActivity2);
                                                                    }
                                                                });
                                                                getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(contactsActivity).setRemoveLast(launchActivity.actionBarLayout.getLastFragment() instanceof ContactsActivity));
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, i6);
                                                                }
                                                            } else if (z17) {
                                                                final ActionIntroActivity actionIntroActivity = new ActionIntroActivity(5);
                                                                actionIntroActivity.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda137
                                                                    @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                                                    public final void didFindQRCode(String str88) {
                                                                        LaunchActivity.this.lambda$handleIntent$28(actionIntroActivity, str88);
                                                                    }
                                                                });
                                                                getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(actionIntroActivity).setNoAnimation(r3));
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, i6);
                                                                }
                                                            } else if (z15) {
                                                                NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(launchActivity.actionBarLayout.getLastFragment(), launchActivity);
                                                                String str88 = str5;
                                                                if (str88 != null) {
                                                                    String[] split3 = str88.split(str86, 2);
                                                                    newContactBottomSheet.setInitialName(split3[i6], split3.length > r3 ? split3[r3 == true ? 1 : 0] : null);
                                                                }
                                                                String str89 = str6;
                                                                if (str89 != null) {
                                                                    newContactBottomSheet.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str89, r3), i6);
                                                                }
                                                                newContactBottomSheet.show();
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(r3 == true ? 1 : 0);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, i6);
                                                                }
                                                            } else {
                                                                final String str90 = str5;
                                                                String str91 = str6;
                                                                if (z8) {
                                                                    z22 = true;
                                                                    z22 = true;
                                                                    GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                                    if (GroupCallActivity.groupCallInstance != null) {
                                                                        GroupCallActivity.groupCallUiVisible = true;
                                                                    }
                                                                } else {
                                                                    z22 = true;
                                                                    r9 = 1;
                                                                    z26 = true;
                                                                    z26 = true;
                                                                    if (z16) {
                                                                        final BaseFragment lastFragment4 = launchActivity.actionBarLayout.getLastFragment();
                                                                        if (lastFragment4 == null || lastFragment4.getParentActivity() == null) {
                                                                            z27 = false;
                                                                        } else {
                                                                            final String phoneNumber = NewContactBottomSheet.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str91, i6);
                                                                            AlertDialog.Builder title = new AlertDialog.Builder(lastFragment4.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle));
                                                                            int i31 = R.string.NewContactAlertMessage;
                                                                            Object[] objArr = new Object[1];
                                                                            objArr[i6] = PhoneFormat.getInstance().format(phoneNumber);
                                                                            lastFragment4.showDialog(title.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", i31, objArr))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                                                                                @Override // android.content.DialogInterface.OnClickListener
                                                                                public final void onClick(DialogInterface dialogInterface, int i32) {
                                                                                    LaunchActivity.this.lambda$handleIntent$29(lastFragment4, phoneNumber, str90, dialogInterface, i32);
                                                                                }
                                                                            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
                                                                            z27 = true;
                                                                        }
                                                                        z25 = z27;
                                                                        z24 = z;
                                                                    } else if (z13) {
                                                                        getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new CallLogActivity()).setNoAnimation(true));
                                                                        if (AndroidUtilities.isTablet()) {
                                                                            launchActivity.actionBarLayout.rebuildFragments(1);
                                                                            launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i6, i6);
                                                                        } else {
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, i6);
                                                                        }
                                                                        z24 = z;
                                                                        z25 = true;
                                                                        r9 = z26;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        z24 = z;
                                                        z30 = r3;
                                                        r9 = 1;
                                                        z25 = z30;
                                                        r12 = z29;
                                                    }
                                                } else {
                                                    z22 = true;
                                                    z26 = true;
                                                    if (!AndroidUtilities.isTablet()) {
                                                        NotificationCenter.getInstance(iArr[i6]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[i6]);
                                                    }
                                                    long j16 = j3;
                                                    if (j16 == 0) {
                                                        launchActivity.openDialogsToSend(i6);
                                                        z24 = z;
                                                        z25 = true;
                                                        r9 = z26;
                                                    } else {
                                                        ArrayList<MessagesStorage.TopicKey> arrayList6 = new ArrayList<>();
                                                        arrayList6.add(MessagesStorage.TopicKey.of(j16, i6));
                                                        didSelectDialogs(null, arrayList6, null, false, null);
                                                    }
                                                }
                                                if (!z25) {
                                                    if (!AndroidUtilities.isTablet()) {
                                                    }
                                                    if (z4) {
                                                    }
                                                }
                                                if (z56) {
                                                }
                                                if (!z8) {
                                                    groupCallActivity.dismiss();
                                                }
                                                intent8.setAction(r12);
                                                return z25;
                                            }
                                            z24 = z;
                                            z25 = false;
                                            r9 = z22;
                                            r12 = z23;
                                            if (!z25) {
                                            }
                                            if (z56) {
                                            }
                                            if (!z8) {
                                            }
                                            intent8.setAction(r12);
                                            return z25;
                                        }
                                    } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                        z31 = false;
                                    } else {
                                        z31 = false;
                                        launchActivity.actionBarLayout.getFragmentStack().get(i6).showDialog(new AudioPlayerAlert(launchActivity, null));
                                    }
                                    z24 = z;
                                    z30 = false;
                                    z29 = z31;
                                    r9 = 1;
                                    z25 = z30;
                                    r12 = z29;
                                    if (!z25) {
                                    }
                                    if (z56) {
                                    }
                                    if (!z8) {
                                    }
                                    intent8.setAction(r12);
                                    return z25;
                                }
                                Bundle bundle8 = new Bundle();
                                bundle8.putInt("enc_id", i2);
                                if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle8)).setNoAnimation(r3))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z33 = true;
                                }
                                z33 = false;
                            }
                        }
                        z24 = z;
                        z32 = z33;
                        r9 = 1;
                        r12 = 0;
                        z25 = z32;
                        if (!z25) {
                        }
                        if (z56) {
                        }
                        if (!z8) {
                        }
                        intent8.setAction(r12);
                        return z25;
                    }
                }
                r3 = 1;
                if (jArr == null) {
                }
            }
            z22 = true;
            z23 = false;
            z24 = z;
            z25 = false;
            r9 = z22;
            r12 = z23;
            if (!z25) {
            }
            if (z56) {
            }
            if (!z8) {
            }
            intent8.setAction(r12);
            return z25;
        }
        j = 0;
        intent2 = intent;
        launchActivity = this;
        str = " ";
        str2 = "message_id";
        iArr = iArr3;
        z5 = false;
        intent3 = intent2;
        j12 = j;
        j2 = j12;
        j3 = j2;
        i = -1;
        i2 = 0;
        jArr = null;
        i3 = -1;
        str3 = null;
        z6 = false;
        z7 = false;
        z19 = z5;
        intent6 = intent3;
        z10 = false;
        z18 = z19;
        intent5 = intent6;
        z9 = false;
        z8 = false;
        z11 = z18;
        intent4 = intent5;
        z12 = false;
        z13 = false;
        z14 = false;
        z15 = false;
        z16 = false;
        z17 = false;
        z20 = z11;
        intent7 = intent4;
        str4 = null;
        str5 = null;
        str6 = null;
        i4 = 0;
        z21 = false;
        i5 = 0;
        c = 0;
        i6 = z20;
        intent8 = intent7;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z22 = true;
        z23 = false;
        z24 = z;
        z25 = false;
        r9 = z22;
        r12 = z23;
        if (!z25) {
        }
        if (z56) {
        }
        if (!z8) {
        }
        intent8.setAction(r12);
        return z25;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$15(String str) {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).presentFragment(new PremiumPreviewFragment(Uri.parse(str).getQueryParameter("ref")));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$16(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$18(final AlertDialog alertDialog, final String str, final Bundle bundle, final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$handleIntent$17(alertDialog, tLRPC$TL_error, str, bundle, tLObject, tLRPC$TL_account_sendConfirmPhoneCode);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$17(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, String str, Bundle bundle, TLObject tLObject, TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode) {
        alertDialog.dismiss();
        if (tLRPC$TL_error == null) {
            lambda$runLinkRequest$86(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, getActionBarLayout().getLastFragment(), tLRPC$TL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$19(long j, int i, ChatActivity chatActivity) {
        TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(j, i);
        FileLog.d("LaunchActivity openForum after load " + j + " " + i + " TL_forumTopic " + findTopic);
        if (this.actionBarLayout != null) {
            ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(-j, i));
            getActionBarLayout().presentFragment(chatActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$21(final int[] iArr, LocationController.SharingLocationInfo sharingLocationInfo) {
        iArr[0] = sharingLocationInfo.messageObject.currentAccount;
        switchToAccount(iArr[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        final long dialogId = sharingLocationInfo.messageObject.getDialogId();
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda150
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
                LaunchActivity.lambda$handleIntent$20(iArr, dialogId, tLRPC$MessageMedia, i, z, i2);
            }
        });
        lambda$runLinkRequest$86(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$20(int[] iArr, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$22() {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$23(BaseFragment baseFragment, boolean z) {
        presentFragment(baseFragment, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$24(boolean z, int[] iArr, TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
        VoIPHelper.startCall(tLRPC$User, z, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(iArr[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$28(final ActionIntroActivity actionIntroActivity, String str) {
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        byte[] decode = Base64.decode(str.substring(17), 8);
        TLRPC$TL_auth_acceptLoginToken tLRPC$TL_auth_acceptLoginToken = new TLRPC$TL_auth_acceptLoginToken();
        tLRPC$TL_auth_acceptLoginToken.token = decode;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda109
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$handleIntent$27(AlertDialog.this, actionIntroActivity, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$27(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$26(AlertDialog.this, tLObject, actionIntroActivity, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$26(AlertDialog alertDialog, TLObject tLObject, final ActionIntroActivity actionIntroActivity, final TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (tLObject instanceof TLRPC$TL_authorization) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$25(ActionIntroActivity.this, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$25(ActionIntroActivity actionIntroActivity, TLRPC$TL_error tLRPC$TL_error) {
        String string = LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient);
        AlertsCreator.showSimpleAlert(actionIntroActivity, string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$29(BaseFragment baseFragment, String str, String str2, DialogInterface dialogInterface, int i) {
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
                        return LaunchActivity.this.videoPath != null || (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0);
                    }
                    return false;
                }
                return true;
            }
        };
        dialogsActivity.setDelegate(this);
        getActionBarLayout().presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), !z, true, false);
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        StoryRecorder.destroyInstance();
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
        return runCommentRequest(i, runnable, num, num2, num3, tLRPC$Chat, null, null, 0);
    }

    private int runCommentRequest(final int i, final Runnable runnable, final Integer num, final Integer num2, final Integer num3, final TLRPC$Chat tLRPC$Chat, final Runnable runnable2, final String str, final int i2) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = (num2 != null ? num : num3).intValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda117
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runCommentRequest$31(i, tLRPC$Chat, num3, num2, num, runnable2, str, i2, tLRPC$TL_messages_getDiscussionMessage, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$31(final int i, final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final Integer num3, final Runnable runnable, final String str, final int i2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Runnable runnable2, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$30(tLObject, i, tLRPC$Chat, num, num2, num3, runnable, str, i2, tLRPC$TL_messages_getDiscussionMessage, runnable2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0116 A[Catch: Exception -> 0x0112, TRY_LEAVE, TryCatch #1 {Exception -> 0x0112, blocks: (B:40:0x010e, B:44:0x0116), top: B:50:0x010e }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00df A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runCommentRequest$30(TLObject tLObject, int i, TLRPC$Chat tLRPC$Chat, Integer num, Integer num2, Integer num3, Runnable runnable, String str, int i2, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Runnable runnable2) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_discussionMessage) {
            TLRPC$TL_messages_discussionMessage tLRPC$TL_messages_discussionMessage = (TLRPC$TL_messages_discussionMessage) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_discussionMessage.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_messages_discussionMessage.chats, false);
            ArrayList<MessageObject> arrayList = new ArrayList<>();
            int size = tLRPC$TL_messages_discussionMessage.messages.size();
            for (int i3 = 0; i3 < size; i3++) {
                arrayList.add(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_messages_discussionMessage.messages.get(i3), true, true));
            }
            if (!arrayList.isEmpty() || (tLRPC$Chat.forum && num != null && num.intValue() == 1)) {
                if (tLRPC$Chat.forum) {
                    openTopicRequest(i, num.intValue(), tLRPC$Chat, (num2 != null ? num2 : num3).intValue(), null, runnable, str, i2, arrayList);
                    z = true;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -arrayList.get(0).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num3.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, tLRPC$TL_messages_discussionMessage.read_inbox_max_id, tLRPC$TL_messages_discussionMessage.read_outbox_max_id, null);
                    if (num2 != null) {
                        if (str != null) {
                            chatActivity.setHighlightQuote(num2.intValue(), str);
                        } else {
                            chatActivity.setHighlightMessageId(num2.intValue());
                        }
                    } else if (num != null) {
                        if (str != null) {
                            chatActivity.setHighlightQuote(num3.intValue(), str);
                        } else {
                            chatActivity.setHighlightMessageId(num3.intValue());
                        }
                    }
                    lambda$runLinkRequest$86(chatActivity);
                    z = true;
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
                    if (runnable == null) {
                        runnable.run();
                        return;
                    }
                    return;
                }
            }
        }
        if (!z) {
        }
        if (runnable2 != null) {
        }
        if (runnable == null) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openTopicRequest(final int i, final int i2, final TLRPC$Chat tLRPC$Chat, final int i3, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, final Runnable runnable, final String str, final int i4, final ArrayList<MessageObject> arrayList) {
        BaseFragment baseFragment;
        TLRPC$TL_forumTopic findTopic = tLRPC$TL_forumTopic == null ? MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, i2) : tLRPC$TL_forumTopic;
        if (findTopic == null) {
            TLRPC$TL_channels_getForumTopicsByID tLRPC$TL_channels_getForumTopicsByID = new TLRPC$TL_channels_getForumTopicsByID();
            tLRPC$TL_channels_getForumTopicsByID.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(tLRPC$Chat.id);
            tLRPC$TL_channels_getForumTopicsByID.topics.add(Integer.valueOf(i2));
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda116
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$openTopicRequest$33(i, tLRPC$Chat, i2, i3, runnable, str, i4, arrayList, tLObject, tLRPC$TL_error);
                }
            });
            return;
        }
        if (mainFragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        }
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-tLRPC$Chat.id) && chatActivity.isTopic && chatActivity.getTopicId() == findTopic.id) {
                if (str != null) {
                    chatActivity.setHighlightQuote(i3, str);
                }
                chatActivity.scrollToMessageId(i3, i4, true, 0, true, 0, null);
                if (runnable == null) {
                    runnable.run();
                    return;
                }
                return;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (i3 != findTopic.id) {
            bundle.putInt("message_id", Math.max(1, i3));
        }
        ChatActivity chatActivity2 = new ChatActivity(bundle);
        if (arrayList.isEmpty()) {
            TLRPC$Message tLRPC$Message = new TLRPC$Message();
            tLRPC$Message.id = 1;
            tLRPC$Message.action = new TLRPC$TL_messageActionChannelMigrateFrom();
            arrayList.add(new MessageObject(i, tLRPC$Message, false, false));
        }
        chatActivity2.setThreadMessages(arrayList, tLRPC$Chat, i3, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
        if (i3 != findTopic.id) {
            if (str != null) {
                chatActivity2.setHighlightQuote(i3, str);
            } else {
                chatActivity2.setHighlightMessageId(i3);
            }
            chatActivity2.scrollToMessageId(i3, i4, true, 0, true, 0, null);
        }
        lambda$runLinkRequest$86(chatActivity2);
        if (runnable == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTopicRequest$33(final int i, final TLRPC$Chat tLRPC$Chat, final int i2, final int i3, final Runnable runnable, final String str, final int i4, final ArrayList arrayList, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openTopicRequest$32(tLRPC$TL_error, tLObject, i, tLRPC$Chat, i2, i3, runnable, str, i4, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTopicRequest$32(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, TLRPC$Chat tLRPC$Chat, int i2, int i3, Runnable runnable, String str, int i4, ArrayList arrayList) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics = (TLRPC$TL_messages_forumTopics) tLObject;
            SparseArray<TLRPC$Message> sparseArray = new SparseArray<>();
            for (int i5 = 0; i5 < tLRPC$TL_messages_forumTopics.messages.size(); i5++) {
                sparseArray.put(tLRPC$TL_messages_forumTopics.messages.get(i5).id, tLRPC$TL_messages_forumTopics.messages.get(i5));
            }
            MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_forumTopics.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_messages_forumTopics.chats, false);
            MessagesController.getInstance(i).getTopicsController().processTopics(tLRPC$Chat.id, tLRPC$TL_messages_forumTopics.topics, sparseArray, false, 2, -1);
            openTopicRequest(i, i2, tLRPC$Chat, i3, MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, i2), runnable, str, i4, arrayList);
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
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda120
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runImportRequest$35(uri, i, alertDialog, tLObject, tLRPC$TL_error);
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    LaunchActivity.lambda$runImportRequest$36(i, iArr, r3, dialogInterface);
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
    public /* synthetic */ void lambda$runImportRequest$35(final Uri uri, final int i, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runImportRequest$34(tLObject, uri, i, alertDialog);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runImportRequest$34(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
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
            StoryRecorder.destroyInstance();
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
            getActionBarLayout().presentFragment(dialogsActivity, z2, false, true, false);
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
    public static /* synthetic */ void lambda$runImportRequest$36(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void openMessage(final long j, final int i, final String str, final Browser.Progress progress, int i2) {
        BaseFragment baseFragment;
        TLRPC$Chat chat;
        if (j < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j))) != null && ChatObject.isForum(chat)) {
            if (progress != null) {
                progress.init();
            }
            openForumFromLink(j, Integer.valueOf(i), str, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$openMessage$37(Browser.Progress.this);
                }
            }, i2);
            return;
        }
        if (progress != null) {
            progress.init();
        }
        final Bundle bundle = new Bundle();
        if (j >= 0) {
            bundle.putLong("user_id", j);
        } else {
            long j2 = -j;
            TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
            if (chat2 != null && chat2.forum) {
                openForumFromLink(j, Integer.valueOf(i), str, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$openMessage$38(Browser.Progress.this);
                    }
                }, i2);
                return;
            }
            bundle.putLong("chat_id", j2);
        }
        bundle.putInt("message_id", i);
        if (mainFragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            baseFragment = arrayList.get(arrayList.size() - 1);
        }
        final BaseFragment baseFragment2 = baseFragment;
        if (baseFragment2 == null || MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, baseFragment2)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openMessage$42(bundle, i, str, j, progress, baseFragment2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openMessage$37(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openMessage$38(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$42(final Bundle bundle, final int i, final String str, final long j, final Browser.Progress progress, final BaseFragment baseFragment) {
        final ChatActivity chatActivity = new ChatActivity(bundle);
        chatActivity.setHighlightQuote(i, str);
        if ((AndroidUtilities.isTablet() ? this.rightActionBarLayout : getActionBarLayout()).presentFragment(chatActivity) || j >= 0) {
            if (progress != null) {
                progress.end();
                return;
            }
            return;
        }
        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
        tLRPC$TL_inputChannel.channel_id = -j;
        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
        final int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda128
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openMessage$40(progress, j, i, baseFragment, bundle, chatActivity, str, tLObject, tLRPC$TL_error);
            }
        });
        if (progress != null) {
            progress.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openMessage$41(sendRequest);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$40(final Browser.Progress progress, final long j, final int i, final BaseFragment baseFragment, final Bundle bundle, final ChatActivity chatActivity, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openMessage$39(progress, tLObject, j, i, baseFragment, bundle, chatActivity, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$39(Browser.Progress progress, TLObject tLObject, long j, int i, BaseFragment baseFragment, Bundle bundle, ChatActivity chatActivity, String str) {
        if (progress != null) {
            progress.end();
        }
        boolean z = true;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_chats.chats.get(0);
                if (tLRPC$Chat != null && tLRPC$Chat.forum) {
                    openForumFromLink(-j, Integer.valueOf(i), null);
                }
                if (baseFragment == null || MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, baseFragment)) {
                    ChatActivity chatActivity2 = new ChatActivity(bundle);
                    chatActivity.setHighlightQuote(i, str);
                    getActionBarLayout().presentFragment(chatActivity2);
                }
                z = false;
            }
        }
        if (z) {
            showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$41(int i) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:153:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final String str12, final HashMap<String, String> hashMap, final String str13, final String str14, final String str15, final String str16, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str17, final String str18, final String str19, final String str20, final int i2, final int i3, final String str21, final String str22, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z2, final int i4, final boolean z3) {
        final int i5;
        char c;
        final AlertDialog alertDialog;
        final Browser.Progress progress2;
        BaseFragment baseFragment;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        EmojiPacksAlert emojiPacksAlert;
        StickersAlert stickersAlert;
        String str26 = str3;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda138
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i6) {
                    LaunchActivity.this.lambda$runLinkRequest$45(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, i6);
                }
            }).show();
            return;
        }
        final Runnable runnable = null;
        if (str15 != null) {
            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
            int i6 = NotificationCenter.didReceiveSmsCode;
            if (globalInstance.hasObservers(i6)) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(i6, str15);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", R.string.OtherLoginCode, str15)));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        } else if (str16 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder2);
        } else {
            final AlertDialog alertDialog2 = new AlertDialog(this, 3);
            final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$runLinkRequest$46(Browser.Progress.this, alertDialog2);
                }
            };
            final int[] iArr = {0};
            if (str10 != null) {
                TLRPC$TL_contacts_importContactToken tLRPC$TL_contacts_importContactToken = new TLRPC$TL_contacts_importContactToken();
                tLRPC$TL_contacts_importContactToken.token = str10;
                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_importContactToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda114
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$48(i, str10, runnable2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str11 != null) {
                TL_chatlists$TL_chatlists_checkChatlistInvite tL_chatlists$TL_chatlists_checkChatlistInvite = new TL_chatlists$TL_chatlists_checkChatlistInvite();
                tL_chatlists$TL_chatlists_checkChatlistInvite.slug = str11;
                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tL_chatlists$TL_chatlists_checkChatlistInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda107
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.lambda$runLinkRequest$50(i, str11, runnable2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str17 != null) {
                TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = str17;
                tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda113
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$53(i, str17, runnable2, tLObject, tLRPC$TL_error);
                    }
                });
            } else {
                if (str != null) {
                    if (progress != null) {
                        progress.init();
                    }
                    MessagesController.getInstance(i).getUserNameResolver().resolve(str, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda24
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            LaunchActivity.this.lambda$runLinkRequest$71(i4, runnable2, str12, str19, str20, str24, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, i2, i3, str21, str22, str23, str25, progress, z2, z3, iArr, (Long) obj);
                        }
                    });
                    i5 = i;
                    alertDialog = alertDialog2;
                    progress2 = progress;
                    iArr = iArr;
                    c = 0;
                } else if (str2 != null) {
                    if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        iArr = iArr;
                        c = 0;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda118
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$74(i, alertDialog2, runnable2, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                    } else {
                        iArr = iArr;
                        c = 0;
                        if (i2 == 1) {
                            TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                            tLRPC$TL_messages_importChatInvite.hash = str2;
                            i5 = i;
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda112
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$76(i5, runnable2, tLObject, tLRPC$TL_error);
                                }
                            }, 2);
                            alertDialog = alertDialog2;
                            progress2 = progress;
                        }
                    }
                    i5 = i;
                    alertDialog = alertDialog2;
                    progress2 = progress;
                } else {
                    i5 = i;
                    iArr = iArr;
                    c = 0;
                    if (str26 != null) {
                        if (mainFragmentsStack.isEmpty()) {
                            return;
                        }
                        TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                        tLRPC$TL_inputStickerSetShortName.short_name = str26;
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
                        if (str26 == null) {
                            str26 = str4;
                        }
                        tLRPC$TL_inputStickerSetShortName2.short_name = str26;
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
                            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda148
                                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z4, TopicsFragment topicsFragment) {
                                    boolean lambda$runLinkRequest$77;
                                    lambda$runLinkRequest$77 = LaunchActivity.this.lambda$runLinkRequest$77(z, i5, str9, dialogsActivity2, arrayList4, charSequence, z4, topicsFragment);
                                    return lambda$runLinkRequest$77;
                                }
                            });
                            presentFragment(dialogsActivity, false, true);
                        } else if (hashMap != null) {
                            int intValue = Utilities.parseInt((CharSequence) hashMap.get("bot_id")).intValue();
                            if (intValue == 0) {
                                return;
                            }
                            final String str27 = hashMap.get("payload");
                            final String str28 = hashMap.get("nonce");
                            final String str29 = hashMap.get("callback_url");
                            final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
                            tLRPC$TL_account_getAuthorizationForm.bot_id = intValue;
                            tLRPC$TL_account_getAuthorizationForm.scope = hashMap.get("scope");
                            tLRPC$TL_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda134
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$81(iArr, i, runnable2, tLRPC$TL_account_getAuthorizationForm, str27, str28, str29, tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (str14 != null) {
                            TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                            tLRPC$TL_help_getDeepLinkInfo.path = str14;
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda123
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$83(runnable2, tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (str13 != null) {
                            TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                            tLRPC$TL_langpack_getLanguage.lang_code = str13;
                            tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda122
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$85(runnable2, tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (tLRPC$TL_wallPaper != null) {
                            if (TextUtils.isEmpty(tLRPC$TL_wallPaper.slug)) {
                                try {
                                    TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                                    int i7 = tLRPC$WallPaperSettings.third_background_color;
                                    if (i7 != 0) {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, i7, tLRPC$WallPaperSettings.fourth_background_color);
                                    } else {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false));
                                    }
                                    final ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(colorWallpaper, null, true, false);
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.lambda$runLinkRequest$86(themePreviewActivity);
                                        }
                                    });
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (!r2) {
                                    TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                                    TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                                    tLRPC$TL_inputWallPaperSlug.slug = tLRPC$TL_wallPaper.slug;
                                    tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda125
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                            LaunchActivity.this.lambda$runLinkRequest$88(runnable2, tLRPC$TL_wallPaper, tLObject, tLRPC$TL_error);
                                        }
                                    });
                                }
                            }
                            r2 = false;
                            if (!r2) {
                            }
                        } else {
                            if (str18 != null) {
                                progress2 = progress;
                                runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.this.lambda$runLinkRequest$89(progress2);
                                    }
                                };
                                TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                                tLRPC$TL_account_getTheme.format = "android";
                                TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                                tLRPC$TL_inputThemeSlug.slug = str18;
                                tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                                alertDialog = alertDialog2;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda130
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$91(alertDialog, runnable2, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else {
                                alertDialog = alertDialog2;
                                progress2 = progress;
                                if (l != null && (num != null || z3)) {
                                    if (num2 != null) {
                                        TLRPC$Chat chat = MessagesController.getInstance(i).getChat(l);
                                        if (chat != null) {
                                            iArr[0] = runCommentRequest(i, runnable2, num, num3, num2, chat);
                                        } else {
                                            TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                                            TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
                                            tLRPC$TL_inputChannel.channel_id = l.longValue();
                                            tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
                                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda133
                                                @Override // org.telegram.tgnet.RequestDelegate
                                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                    LaunchActivity.this.lambda$runLinkRequest$93(iArr, i, runnable2, num, num3, num2, tLObject, tLRPC$TL_error);
                                                }
                                            });
                                        }
                                    } else {
                                        final Bundle bundle2 = new Bundle();
                                        bundle2.putLong("chat_id", l.longValue());
                                        if (num != null) {
                                            bundle2.putInt("message_id", num.intValue());
                                        }
                                        TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(l);
                                        if (chat2 != null && ChatObject.isChannelAndNotMegaGroup(chat2) && z3) {
                                            processBoostDialog(Long.valueOf(-l.longValue()), runnable2, progress2);
                                        } else if (chat2 != null && chat2.forum) {
                                            openForumFromLink(-l.longValue(), num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    LaunchActivity.lambda$runLinkRequest$94(runnable2);
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
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        LaunchActivity.this.lambda$runLinkRequest$97(bundle2, l, iArr, runnable2, z3, progress, num2, num, baseFragment4, i);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                            if (iArr[c] == 0) {
                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda3
                                    @Override // android.content.DialogInterface.OnCancelListener
                                    public final void onCancel(DialogInterface dialogInterface) {
                                        LaunchActivity.lambda$runLinkRequest$98(i5, iArr, runnable, dialogInterface);
                                    }
                                });
                                if (progress2 != null) {
                                    progress2.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.lambda$runLinkRequest$99(i5, iArr, runnable);
                                        }
                                    });
                                }
                                try {
                                    if (progress2 != null) {
                                        progress.init();
                                    } else {
                                        alertDialog.showDelayed(300L);
                                    }
                                    return;
                                } catch (Exception unused) {
                                    return;
                                }
                            }
                            return;
                        }
                        alertDialog = alertDialog2;
                        progress2 = progress;
                        if (iArr[c] == 0) {
                        }
                    }
                }
                runnable = null;
                if (iArr[c] == 0) {
                }
            }
            progress2 = progress;
            i5 = i;
            c = 0;
            alertDialog = alertDialog2;
            if (iArr[c] == 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$45(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, boolean z, Integer num, Long l, Integer num2, Integer num3, String str12, HashMap hashMap, String str13, String str14, String str15, String str16, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str17, String str18, String str19, String str20, int i2, String str21, String str22, String str23, String str24, String str25, Browser.Progress progress, boolean z2, int i3, boolean z3, int i4) {
        if (i4 != i) {
            switchToAccount(i4, true);
        }
        runLinkRequest(i4, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, 1, i2, str21, str22, str23, str24, str25, progress, z2, i3, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$46(Browser.Progress progress, AlertDialog alertDialog) {
        if (progress != null) {
            progress.end();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$48(final int i, final String str, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$47(tLObject, i, str, tLRPC$TL_error, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$47(TLObject tLObject, int i, String str, TLRPC$TL_error tLRPC$TL_error, Runnable runnable) {
        if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
            MessagesController.getInstance(i).putUser(tLRPC$User, false);
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", tLRPC$User.id);
            lambda$runLinkRequest$86(new ChatActivity(bundle));
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
    public static /* synthetic */ void lambda$runLinkRequest$50(final int i, final String str, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$49(TLObject.this, i, str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$runLinkRequest$49(TLObject tLObject, int i, String str, Runnable runnable) {
        ArrayList<BaseFragment> arrayList;
        ArrayList<TLRPC$User> arrayList2;
        ArrayList<TLRPC$Chat> arrayList3;
        ArrayList<TLRPC$User> arrayList4;
        BaseFragment baseFragment = mainFragmentsStack.get(arrayList.size() - 1);
        if (tLObject instanceof TL_chatlists$chatlist_ChatlistInvite) {
            TL_chatlists$chatlist_ChatlistInvite tL_chatlists$chatlist_ChatlistInvite = (TL_chatlists$chatlist_ChatlistInvite) tLObject;
            boolean z = tL_chatlists$chatlist_ChatlistInvite instanceof TL_chatlists$TL_chatlists_chatlistInvite;
            ArrayList<TLRPC$Chat> arrayList5 = null;
            if (z) {
                TL_chatlists$TL_chatlists_chatlistInvite tL_chatlists$TL_chatlists_chatlistInvite = (TL_chatlists$TL_chatlists_chatlistInvite) tL_chatlists$chatlist_ChatlistInvite;
                arrayList3 = tL_chatlists$TL_chatlists_chatlistInvite.chats;
                arrayList4 = tL_chatlists$TL_chatlists_chatlistInvite.users;
            } else if (tL_chatlists$chatlist_ChatlistInvite instanceof TL_chatlists$TL_chatlists_chatlistInviteAlready) {
                TL_chatlists$TL_chatlists_chatlistInviteAlready tL_chatlists$TL_chatlists_chatlistInviteAlready = (TL_chatlists$TL_chatlists_chatlistInviteAlready) tL_chatlists$chatlist_ChatlistInvite;
                arrayList3 = tL_chatlists$TL_chatlists_chatlistInviteAlready.chats;
                arrayList4 = tL_chatlists$TL_chatlists_chatlistInviteAlready.users;
            } else {
                arrayList2 = null;
                MessagesController.getInstance(i).putChats(arrayList5, false);
                MessagesController.getInstance(i).putUsers(arrayList2, false);
                if (z || !((TL_chatlists$TL_chatlists_chatlistInvite) tL_chatlists$chatlist_ChatlistInvite).peers.isEmpty()) {
                    FolderBottomSheet folderBottomSheet = new FolderBottomSheet(baseFragment, str, tL_chatlists$chatlist_ChatlistInvite);
                    if (baseFragment == null) {
                        baseFragment.showDialog(folderBottomSheet);
                    } else {
                        folderBottomSheet.show();
                    }
                } else {
                    BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(R.string.NoFolderFound)).show();
                }
            }
            ArrayList<TLRPC$Chat> arrayList6 = arrayList3;
            arrayList2 = arrayList4;
            arrayList5 = arrayList6;
            MessagesController.getInstance(i).putChats(arrayList5, false);
            MessagesController.getInstance(i).putUsers(arrayList2, false);
            if (z) {
            }
            FolderBottomSheet folderBottomSheet2 = new FolderBottomSheet(baseFragment, str, tL_chatlists$chatlist_ChatlistInvite);
            if (baseFragment == null) {
            }
        } else {
            BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(R.string.NoFolderFound)).show();
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$53(final int i, final String str, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$52(tLRPC$TL_error, tLObject, i, str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$52(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, String str, Runnable runnable) {
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
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda151
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$51(runnable2, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$86(paymentFormActivity);
            }
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$51(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01d8, code lost:
        if (r86[0] != 0) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$71(final int i, final Runnable runnable, final String str, final String str2, final String str3, final String str4, final int i2, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final String str13, final String str14, final String str15, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final HashMap hashMap, final String str16, final String str17, final String str18, final String str19, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str20, final String str21, final int i3, final int i4, final String str22, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z2, final boolean z3, int[] iArr, final Long l2) {
        String str26;
        Integer num4;
        long longValue;
        boolean z4;
        BaseFragment baseFragment;
        final TLRPC$User user;
        if (isFinishing()) {
            return;
        }
        boolean z5 = false;
        if (i != 0 && l2 != null) {
            MessagesController.getInstance(this.currentAccount).getStoriesController().resolveStoryLink(l2.longValue(), i, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$runLinkRequest$55(runnable, l2, (TL_stories$StoryItem) obj);
                }
            });
        } else {
            if (l2 != null && this.actionBarLayout != null && ((str == null && str2 == null) || ((str != null && l2.longValue() > 0) || ((str2 != null && l2.longValue() > 0) || (str3 != null && l2.longValue() < 0))))) {
                if (!TextUtils.isEmpty(str4) && (user = MessagesController.getInstance(i2).getUser(l2)) != null && user.bot) {
                    TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
                    tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i2).getInputUser(l2.longValue());
                    ConnectionsManager.getInstance(i2).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda111
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$63(i2, l2, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, z, num, l, num2, num3, str, hashMap, str16, str17, str18, str19, tLRPC$TL_wallPaper, str20, str21, str2, str3, i3, i4, str22, str23, str24, str4, str25, progress, z2, i, z3, user, runnable, tLObject, tLRPC$TL_error);
                        }
                    });
                    return;
                } else if (z3 && ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(i2).getChat(Long.valueOf(-l2.longValue())))) {
                    processBoostDialog(l2, runnable, progress);
                    return;
                } else {
                    if (str22 == null && str25 == null) {
                        num4 = num;
                        str26 = str23;
                    } else {
                        str26 = str23;
                        if (str26 == null) {
                            TLRPC$User user2 = MessagesController.getInstance(i2).getUser(l2);
                            if (user2 != null && user2.bot) {
                                if (user2.bot_attach_menu) {
                                    processAttachMenuBot(i2, l2.longValue(), str24, user2, str22, str25);
                                } else {
                                    ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                                    BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
                                }
                            } else {
                                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotSetAttachLinkNotBot)).show();
                            }
                        } else {
                            num4 = num;
                        }
                    }
                    if (num4 != null && ((num3 != null || num2 != null) && l2.longValue() < 0)) {
                        iArr[0] = runCommentRequest(i2, runnable, num, num3, num2, MessagesController.getInstance(i2).getChat(Long.valueOf(-l2.longValue())));
                    } else if (str != null) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("onlySelect", true);
                        bundle.putBoolean("cantSendToChannels", true);
                        bundle.putInt("dialogsType", 1);
                        bundle.putString("selectAlertString", LocaleController.getString("SendGameToText", R.string.SendGameToText));
                        bundle.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", R.string.SendGameToGroupText));
                        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                        final TLRPC$User user3 = MessagesController.getInstance(i2).getUser(l2);
                        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda146
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z6, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$64;
                                lambda$runLinkRequest$64 = LaunchActivity.this.lambda$runLinkRequest$64(str, i2, user3, dialogsActivity2, arrayList3, charSequence, z6, topicsFragment);
                                return lambda$runLinkRequest$64;
                            }
                        });
                        getActionBarLayout().presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), true, true, false);
                        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                            SecretMediaViewer.getInstance().closePhoto(false, false);
                        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                            PhotoViewer.getInstance().closePhoto(false, true);
                        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                            ArticleViewer.getInstance().close(false, true);
                        }
                        StoryRecorder.destroyInstance();
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
                    } else if (str10 != null || str11 != null) {
                        final TLRPC$User user4 = MessagesController.getInstance(i2).getUser(l2);
                        if (user4 == null || (user4.bot && user4.bot_nochats)) {
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
                        bundle2.putBoolean("allowGroups", str10 != null);
                        bundle2.putBoolean("allowChannels", str11 != null);
                        String str27 = TextUtils.isEmpty(str10) ? TextUtils.isEmpty(str11) ? null : str11 : str10;
                        final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle2);
                        final String str28 = str27;
                        dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda145
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z6, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$69;
                                lambda$runLinkRequest$69 = LaunchActivity.this.lambda$runLinkRequest$69(i2, user4, str12, str28, dialogsActivity2, dialogsActivity3, arrayList4, charSequence, z6, topicsFragment);
                                return lambda$runLinkRequest$69;
                            }
                        });
                        lambda$runLinkRequest$86(dialogsActivity2);
                    } else {
                        Bundle bundle3 = new Bundle();
                        TLRPC$User user5 = MessagesController.getInstance(i2).getUser(l2);
                        if (l2.longValue() < 0) {
                            bundle3.putLong("chat_id", -l2.longValue());
                            longValue = l2.longValue();
                        } else {
                            bundle3.putLong("user_id", l2.longValue());
                            longValue = l2.longValue();
                        }
                        if (str9 == null || user5 == null || !user5.bot) {
                            z4 = false;
                        } else {
                            bundle3.putString("botUser", str9);
                            z4 = true;
                        }
                        if (this.navigateToPremiumBot) {
                            this.navigateToPremiumBot = false;
                            bundle3.putBoolean("premium_bot", true);
                        }
                        if (num4 != null) {
                            bundle3.putInt("message_id", num.intValue());
                        }
                        if (str2 != null) {
                            bundle3.putString("voicechat", str2);
                        }
                        if (str3 != null) {
                            bundle3.putString("livestream", str3);
                        }
                        if (i4 >= 0) {
                            bundle3.putInt("video_timestamp", i4);
                        }
                        if (str26 != null) {
                            bundle3.putString("attach_bot", str26);
                        }
                        if (str22 != null) {
                            bundle3.putString("attach_bot_start_command", str22);
                        }
                        if (mainFragmentsStack.isEmpty() || str2 != null) {
                            baseFragment = null;
                        } else {
                            ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                            baseFragment = arrayList4.get(arrayList4.size() - 1);
                        }
                        if (baseFragment == null || MessagesController.getInstance(i2).checkCanOpenChat(bundle3, baseFragment)) {
                            boolean z6 = (baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == longValue;
                            if (z4 && z6) {
                                ((ChatActivity) baseFragment).setBotUser(str9);
                            } else if (str26 != null && z6) {
                                ((ChatActivity) baseFragment).openAttachBotLayout(str26);
                            } else {
                                long j = -longValue;
                                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                if (chat != null && chat.forum) {
                                    Integer num5 = num2 == null ? num4 : num2;
                                    if (num5 != null && num5.intValue() != 0) {
                                        openForumFromLink(longValue, num4, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.lambda$runLinkRequest$70(runnable);
                                            }
                                        });
                                    } else {
                                        Bundle bundle4 = new Bundle();
                                        bundle4.putLong("chat_id", j);
                                        lambda$runLinkRequest$86(new TopicsFragment(bundle4));
                                        try {
                                            runnable.run();
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                } else {
                                    MessagesController.getInstance(i2).ensureMessagesLoaded(longValue, num4 == null ? 0 : num.intValue(), new 18(runnable, str3, baseFragment, longValue, num, bundle3));
                                }
                                z5 = false;
                            }
                        }
                    }
                }
            } else {
                try {
                    BaseFragment lastFragment = getLastFragment();
                    if (lastFragment != null) {
                        if (lastFragment instanceof ChatActivity) {
                            ((ChatActivity) lastFragment).shakeContent();
                        }
                        if (AndroidUtilities.isNumeric(str5)) {
                            BulletinFactory.of(lastFragment).createErrorBulletin(LocaleController.getString("NoPhoneFound", R.string.NoPhoneFound)).show();
                        } else {
                            BulletinFactory.of(lastFragment).createErrorBulletin(LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound)).show();
                        }
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            z5 = true;
        }
        if (z5) {
            try {
                runnable.run();
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$55(Runnable runnable, Long l, TL_stories$StoryItem tL_stories$StoryItem) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        BaseFragment lastFragment = getLastFragment();
        if (tL_stories$StoryItem == null) {
            BulletinFactory global = BulletinFactory.global();
            if (global != null) {
                global.createSimpleBulletin(R.raw.story_bomb2, LocaleController.getString("StoryNotFound", R.string.StoryNotFound)).show();
            }
        } else if (tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted) {
            BulletinFactory global2 = BulletinFactory.global();
            if (global2 != null) {
                global2.createSimpleBulletin(R.raw.story_bomb1, LocaleController.getString("StoryNotFound", R.string.StoryNotFound)).show();
            }
        } else if (lastFragment != null) {
            tL_stories$StoryItem.dialogId = l.longValue();
            final StoryViewer orCreateStoryViewer = lastFragment.getOrCreateStoryViewer();
            if (orCreateStoryViewer.isShown() && orCreateStoryViewer.attachedToParent()) {
                StoryViewer orCreateOverlayStoryViewer = lastFragment.getOrCreateOverlayStoryViewer();
                orCreateOverlayStoryViewer.setOnCloseListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryViewer.this.setOverlayVisible(false);
                    }
                });
                orCreateStoryViewer.setOverlayVisible(true);
                orCreateStoryViewer = orCreateOverlayStoryViewer;
            }
            orCreateStoryViewer.instantClose();
            orCreateStoryViewer.open(this, tL_stories$StoryItem, (StoryViewer.PlaceProvider) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$63(final int i, final Long l, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final boolean z, final Integer num, final Long l2, final Integer num2, final Integer num3, final String str12, final HashMap hashMap, final String str13, final String str14, final String str15, final String str16, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str17, final String str18, final String str19, final String str20, final int i2, final int i3, final String str21, final String str22, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z2, final int i4, final boolean z3, final TLRPC$User tLRPC$User, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$62(tLObject, i, l, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$62(TLObject tLObject, final int i, final Long l, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final boolean z, final Integer num, final Long l2, final Integer num2, final Integer num3, final String str12, final HashMap hashMap, final String str13, final String str14, final String str15, final String str16, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str17, final String str18, final String str19, final String str20, final int i2, final int i3, final String str21, final String str22, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z2, final int i4, final boolean z3, final TLRPC$User tLRPC$User, final Runnable runnable) {
        if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = ((TLRPC$TL_attachMenuBotsBot) tLObject).bot;
            final boolean z4 = tLRPC$TL_attachMenuBot != null && (tLRPC$TL_attachMenuBot.show_in_side_menu || tLRPC$TL_attachMenuBot.show_in_attach_menu);
            if (tLRPC$TL_attachMenuBot.inactive && z4) {
                WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        LaunchActivity.this.lambda$runLinkRequest$58(tLRPC$TL_attachMenuBot, i, l, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable, z4, (Boolean) obj);
                    }
                }, null);
            } else if (tLRPC$TL_attachMenuBot.request_write_access || z2) {
                final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                AlertsCreator.createBotLaunchAlert(getLastFragment(), atomicBoolean, tLRPC$User, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.lambda$runLinkRequest$61(tLRPC$TL_attachMenuBot, atomicBoolean, i, l, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable);
                    }
                });
            } else {
                processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$58(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, final int i, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, boolean z, Integer num, Long l2, Integer num2, Integer num3, String str12, HashMap hashMap, String str13, String str14, String str15, String str16, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str17, String str18, String str19, String str20, int i2, int i3, String str21, String str22, String str23, String str24, String str25, Browser.Progress progress, boolean z2, int i4, boolean z3, TLRPC$User tLRPC$User, Runnable runnable, boolean z4, Boolean bool) {
        tLRPC$TL_attachMenuBot.inactive = false;
        tLRPC$TL_attachMenuBot.request_write_access = false;
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$runLinkRequest$57(i, tLObject, tLRPC$TL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable, z4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$57(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$56(TLObject.this, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$56(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$61(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, AtomicBoolean atomicBoolean, final int i, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, boolean z, Integer num, Long l2, Integer num2, Integer num3, String str12, HashMap hashMap, String str13, String str14, String str15, String str16, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str17, String str18, String str19, String str20, int i2, int i3, String str21, String str22, String str23, String str24, String str25, Browser.Progress progress, boolean z2, int i4, boolean z3, TLRPC$User tLRPC$User, Runnable runnable) {
        tLRPC$TL_attachMenuBot.inactive = false;
        tLRPC$TL_attachMenuBot.request_write_access = !atomicBoolean.get();
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = atomicBoolean.get();
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$runLinkRequest$60(i, tLObject, tLRPC$TL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l2, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, str24, str25, progress, z2, i4, z3, tLRPC$User, runnable, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$60(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$59(TLObject.this, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$59(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$64(String str, int i, TLRPC$User tLRPC$User, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        TLRPC$TL_inputMediaGame tLRPC$TL_inputMediaGame = new TLRPC$TL_inputMediaGame();
        TLRPC$TL_inputGameShortName tLRPC$TL_inputGameShortName = new TLRPC$TL_inputGameShortName();
        tLRPC$TL_inputMediaGame.id = tLRPC$TL_inputGameShortName;
        tLRPC$TL_inputGameShortName.short_name = str;
        tLRPC$TL_inputGameShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$User);
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
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$69(final int i, final TLRPC$User tLRPC$User, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        final TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(false, chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$67(str, str2, i, chat, dialogsActivity, tLRPC$User, j, z2, tLRPC$TL_chatAdminRights2, str3);
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
                    LaunchActivity.this.lambda$runLinkRequest$68(j, i, tLRPC$User, str2, dialogInterface, i3);
                }
            });
            builder.show();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$67(final String str, final String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, final TLRPC$User tLRPC$User, final long j, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$66(str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$66(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
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
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$65(i, tLRPC$Chat, dialogsActivity);
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
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            }
        });
        getActionBarLayout().presentFragment(chatRightsEditActivity, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$65(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            presentFragment(new ChatActivity(bundle), true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$68(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        long j2 = -j;
        bundle.putLong("chat_id", j2);
        ChatActivity chatActivity = new ChatActivity(bundle);
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(j2, tLRPC$User, 0, str, chatActivity, null);
        getActionBarLayout().presentFragment(chatActivity, true, false, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$70(Runnable runnable) {
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
                LaunchActivity.this.getActionBarLayout().presentFragment(chatActivity);
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
    public /* synthetic */ void lambda$runLinkRequest$74(final int i, final AlertDialog alertDialog, final Runnable runnable, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$73(tLRPC$TL_error, tLObject, i, alertDialog, runnable, str);
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
    public /* synthetic */ void lambda$runLinkRequest$73(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, AlertDialog alertDialog, final Runnable runnable, String str) {
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
                ArrayList arrayList = new ArrayList();
                arrayList.add(tLRPC$ChatInvite.chat);
                MessagesStorage.getInstance(i).putUsersAndChats(null, arrayList, false, true);
                final Bundle bundle = new Bundle();
                bundle.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                if (!mainFragmentsStack.isEmpty()) {
                    MessagesController messagesController = MessagesController.getInstance(i);
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                }
                final boolean[] zArr = new boolean[1];
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        LaunchActivity.lambda$runLinkRequest$72(zArr, dialogInterface);
                    }
                });
                if (tLRPC$ChatInvite.chat.forum) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                    lambda$runLinkRequest$86(new TopicsFragment(bundle2));
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
                            LaunchActivity.this.getActionBarLayout().presentFragment(chatActivity);
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
    public static /* synthetic */ void lambda$runLinkRequest$72(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$76(final int i, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$75(runnable, tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$75(Runnable runnable, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
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
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                getActionBarLayout().presentFragment(chatActivity, false, true, true, false);
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
    public /* synthetic */ boolean lambda$runLinkRequest$77(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2, TopicsFragment topicsFragment) {
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
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            MediaDataController.getInstance(i).saveDraft(j, 0, str, null, null, false);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$81(int[] iArr, final int i, final Runnable runnable, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda124
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$79(runnable, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3, tLObject2, tLRPC$TL_error2);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$80(runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$79(final Runnable runnable, final int i, final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$78(runnable, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$78(Runnable runnable, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$86(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$account_Password) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$80(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$83(final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$82(runnable, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$82(Runnable runnable, TLObject tLObject) {
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
    public /* synthetic */ void lambda$runLinkRequest$85(final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$84(runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$84(Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$88(final Runnable runnable, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$87(runnable, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$87(Runnable runnable, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
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
            themePreviewActivity.setInitialModes(tLRPC$WallPaperSettings3.blur, tLRPC$WallPaperSettings3.motion, tLRPC$WallPaperSettings3.intensity);
            lambda$runLinkRequest$86(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$89(Browser.Progress progress) {
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
    public /* synthetic */ void lambda$runLinkRequest$91(final AlertDialog alertDialog, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$90(tLObject, alertDialog, runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$90(TLObject tLObject, AlertDialog alertDialog, Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$93(final int[] iArr, final int i, final Runnable runnable, final Integer num, final Integer num2, final Integer num3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$92(tLObject, iArr, i, runnable, num, num2, num3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$92(TLObject tLObject, int[] iArr, int i, Runnable runnable, Integer num, Integer num2, Integer num3) {
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
    public static /* synthetic */ void lambda$runLinkRequest$94(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$97(final Bundle bundle, final Long l, int[] iArr, final Runnable runnable, final boolean z, final Browser.Progress progress, final Integer num, final Integer num2, final BaseFragment baseFragment, final int i) {
        if (getActionBarLayout().presentFragment(new ChatActivity(bundle))) {
            return;
        }
        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
        tLRPC$TL_inputChannel.channel_id = l.longValue();
        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda126
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$96(runnable, z, l, progress, num, num2, baseFragment, i, bundle, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$96(final Runnable runnable, final boolean z, final Long l, final Browser.Progress progress, final Integer num, final Integer num2, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$95(runnable, tLObject, z, l, progress, num, num2, baseFragment, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$95(Runnable runnable, TLObject tLObject, boolean z, Long l, Browser.Progress progress, Integer num, Integer num2, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        boolean z2 = true;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_chats.chats.get(0);
                if (tLRPC$Chat != null && z && ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                    processBoostDialog(Long.valueOf(-l.longValue()), null, progress);
                } else if (tLRPC$Chat != null && tLRPC$Chat.forum) {
                    if (num != null) {
                        openForumFromLink(-l.longValue(), num2, null);
                    } else {
                        openForumFromLink(-l.longValue(), null, null);
                    }
                }
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    getActionBarLayout().presentFragment(new ChatActivity(bundle));
                }
                z2 = false;
            }
        }
        if (z2) {
            showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$98(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$99(int i, int[] iArr, Runnable runnable) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void processWebAppBot(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final String str12, final HashMap<String, String> hashMap, final String str13, final String str14, final String str15, final String str16, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str17, final String str18, final String str19, final String str20, final int i2, final int i3, final String str21, final String str22, final String str23, String str24, final String str25, final Browser.Progress progress, final boolean z2, final int i4, final boolean z3, final TLRPC$User tLRPC$User, final Runnable runnable, final boolean z4) {
        TLRPC$TL_messages_getBotApp tLRPC$TL_messages_getBotApp = new TLRPC$TL_messages_getBotApp();
        TLRPC$TL_inputBotAppShortName tLRPC$TL_inputBotAppShortName = new TLRPC$TL_inputBotAppShortName();
        tLRPC$TL_inputBotAppShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$User);
        tLRPC$TL_inputBotAppShortName.short_name = str24;
        tLRPC$TL_messages_getBotApp.app = tLRPC$TL_inputBotAppShortName;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getBotApp, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda127
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processWebAppBot$104(progress, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, z2, i4, z3, runnable, tLRPC$User, str25, z4, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$104(final Browser.Progress progress, final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final String str12, final HashMap hashMap, final String str13, final String str14, final String str15, final String str16, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str17, final String str18, final String str19, final String str20, final int i2, final int i3, final String str21, final String str22, final String str23, final boolean z2, final int i4, final boolean z3, final Runnable runnable, final TLRPC$User tLRPC$User, final String str24, final boolean z4, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (progress != null) {
            progress.end();
        }
        if (tLRPC$TL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processWebAppBot$100(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, progress, z2, i4, z3);
                }
            });
            return;
        }
        final TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp = (TLRPC$TL_messages_botApp) tLObject;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processWebAppBot$103(runnable, i, tLRPC$User, tLRPC$TL_messages_botApp, str24, z2, z4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$100(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, boolean z, Integer num, Long l, Integer num2, Integer num3, String str12, HashMap hashMap, String str13, String str14, String str15, String str16, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str17, String str18, String str19, String str20, int i2, int i3, String str21, String str22, String str23, Browser.Progress progress, boolean z2, int i4, boolean z3) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, z, num, l, num2, num3, str12, hashMap, str13, str14, str15, str16, tLRPC$TL_wallPaper, str17, str18, str19, str20, i2, i3, str21, str22, str23, null, null, progress, z2, i4, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$103(Runnable runnable, final int i, final TLRPC$User tLRPC$User, final TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp, final String str, final boolean z, boolean z2) {
        runnable.run();
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        final BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processWebAppBot$101(baseFragment, i, tLRPC$User, tLRPC$TL_messages_botApp, atomicBoolean, str, z);
            }
        };
        if (tLRPC$TL_messages_botApp.inactive && z2) {
            WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    Boolean bool = (Boolean) obj;
                    runnable2.run();
                }
            }, null);
        } else if (tLRPC$TL_messages_botApp.request_write_access || z) {
            AlertsCreator.createBotLaunchAlert(baseFragment, atomicBoolean, tLRPC$User, runnable2);
        } else {
            runnable2.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$101(BaseFragment baseFragment, int i, TLRPC$User tLRPC$User, TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp, AtomicBoolean atomicBoolean, String str, boolean z) {
        BotWebViewSheet botWebViewSheet;
        BotWebViewSheet botWebViewSheet2 = new BotWebViewSheet(this, baseFragment.getResourceProvider());
        botWebViewSheet2.setParentActivity(this);
        long j = tLRPC$User.id;
        botWebViewSheet2.requestWebView(i, j, j, null, null, 3, 0, false, baseFragment, tLRPC$TL_messages_botApp.app, atomicBoolean.get(), str, tLRPC$User);
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment != null) {
            botWebViewSheet = botWebViewSheet2;
            lastFragment.showDialog(botWebViewSheet);
        } else {
            botWebViewSheet = botWebViewSheet2;
            botWebViewSheet.show();
        }
        if (tLRPC$TL_messages_botApp.inactive || z) {
            botWebViewSheet.showJustAddedBulletin();
        }
    }

    private void processBoostDialog(final Long l, final Runnable runnable, final Browser.Progress progress) {
        final ChannelBoostsController boostsController = MessagesController.getInstance(this.currentAccount).getBoostsController();
        if (progress != null) {
            progress.init();
        }
        boostsController.getBoostsStats(l.longValue(), new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LaunchActivity.this.lambda$processBoostDialog$106(progress, runnable, boostsController, l, (TL_stories$TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processBoostDialog$106(final Browser.Progress progress, final Runnable runnable, ChannelBoostsController channelBoostsController, final Long l, final TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        if (tL_stories$TL_premium_boostsStatus == null) {
            if (progress != null) {
                progress.end();
            }
            runnable.run();
            return;
        }
        channelBoostsController.userCanBoostChannel(l.longValue(), tL_stories$TL_premium_boostsStatus, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LaunchActivity.this.lambda$processBoostDialog$105(progress, l, tL_stories$TL_premium_boostsStatus, runnable, (ChannelBoostsController.CanApplyBoost) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processBoostDialog$105(Browser.Progress progress, Long l, TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, Runnable runnable, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        if (progress != null) {
            progress.end();
        }
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null) {
            return;
        }
        Theme.ResourcesProvider resourceProvider = lastFragment.getResourceProvider();
        StoryViewer storyViewer = lastFragment.storyViewer;
        if (storyViewer != null && storyViewer.isFullyVisible()) {
            resourceProvider = lastFragment.storyViewer.getResourceProvider();
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(lastFragment, this, 19, this.currentAccount, resourceProvider);
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        boolean z = false;
        if ((lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == l.longValue()) {
            z = true;
        }
        limitReachedBottomSheet.setBoostsStats(tL_stories$TL_premium_boostsStatus, z);
        limitReachedBottomSheet.setDialogId(l.longValue());
        lastFragment.showDialog(limitReachedBottomSheet);
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void processAttachMenuBot(final int i, final long j, final String str, final TLRPC$User tLRPC$User, final String str2, final String str3) {
        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda115
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$113(i, str3, str, tLRPC$User, str2, j, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$113(final int i, final String str, final String str2, final TLRPC$User tLRPC$User, final String str3, final long j, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$112(tLObject, i, str, str2, tLRPC$User, str3, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$112(TLObject tLObject, final int i, String str, String str2, final TLRPC$User tLRPC$User, final String str3, final long j) {
        DialogsActivity dialogsActivity;
        String[] split;
        final TLRPC$User tLRPC$User2 = tLRPC$User;
        if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            TLRPC$TL_attachMenuBotsBot tLRPC$TL_attachMenuBotsBot = (TLRPC$TL_attachMenuBotsBot) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_attachMenuBotsBot.users, false);
            TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = tLRPC$TL_attachMenuBotsBot.bot;
            if (str != null) {
                showAttachMenuBot(tLRPC$TL_attachMenuBot, str);
                return;
            }
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
            if (AndroidUtilities.isTablet() && !(baseFragment instanceof ChatActivity) && !rightFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
                baseFragment = arrayList2.get(arrayList2.size() - 1);
            }
            final BaseFragment baseFragment2 = baseFragment;
            ArrayList arrayList3 = new ArrayList();
            if (!TextUtils.isEmpty(str2)) {
                for (String str4 : str2.split(" ")) {
                    if (MediaDataController.canShowAttachMenuBotForTarget(tLRPC$TL_attachMenuBot, str4)) {
                        arrayList3.add(str4);
                    }
                }
            }
            if (arrayList3.isEmpty()) {
                dialogsActivity = null;
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("dialogsType", 14);
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("allowGroups", arrayList3.contains("groups"));
                bundle.putBoolean("allowMegagroups", arrayList3.contains("groups"));
                bundle.putBoolean("allowLegacyGroups", arrayList3.contains("groups"));
                bundle.putBoolean("allowUsers", arrayList3.contains("users"));
                bundle.putBoolean("allowChannels", arrayList3.contains("channels"));
                bundle.putBoolean("allowBots", arrayList3.contains("bots"));
                DialogsActivity dialogsActivity2 = new DialogsActivity(bundle);
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda147
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                        boolean lambda$processAttachMenuBot$107;
                        lambda$processAttachMenuBot$107 = LaunchActivity.this.lambda$processAttachMenuBot$107(tLRPC$User2, str3, i, dialogsActivity3, arrayList4, charSequence, z, topicsFragment);
                        return lambda$processAttachMenuBot$107;
                    }
                });
                dialogsActivity = dialogsActivity2;
            }
            if (tLRPC$TL_attachMenuBot.inactive) {
                AttachBotIntroTopView attachBotIntroTopView = new AttachBotIntroTopView(this);
                attachBotIntroTopView.setColor(Theme.getColor(Theme.key_chat_attachIcon));
                attachBotIntroTopView.setBackgroundColor(Theme.getColor(Theme.key_dialogTopBackground));
                attachBotIntroTopView.setAttachBot(tLRPC$TL_attachMenuBot);
                final DialogsActivity dialogsActivity3 = dialogsActivity;
                com.google.android.exoplayer2.util.Consumer consumer = new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda23
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        LaunchActivity.this.lambda$processAttachMenuBot$111(i, j, dialogsActivity3, baseFragment2, tLRPC$User, str3, (Boolean) obj);
                    }
                };
                if (!tLRPC$TL_attachMenuBot.request_write_access) {
                    tLRPC$User2 = null;
                }
                WebAppDisclaimerAlert.show(this, consumer, tLRPC$User2);
                return;
            } else if (dialogsActivity != null) {
                lambda$runLinkRequest$86(dialogsActivity);
                return;
            } else if (baseFragment2 instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment2;
                if (!MediaDataController.canShowAttachMenuBot(tLRPC$TL_attachMenuBot, chatActivity.getCurrentUser() != null ? chatActivity.getCurrentUser() : chatActivity.getCurrentChat())) {
                    BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString(R.string.BotAlreadyAddedToAttachMenu)).show();
                    return;
                } else {
                    chatActivity.openAttachBotLayout(tLRPC$User2.id, str3, false);
                    return;
                }
            } else {
                BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString(R.string.BotAlreadyAddedToAttachMenu)).show();
                return;
            }
        }
        ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
        BulletinFactory.of(arrayList4.get(arrayList4.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$processAttachMenuBot$107(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
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
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$111(final int i, long j, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, Boolean bool) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(j);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda119
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$110(i, dialogsActivity, baseFragment, tLRPC$User, str, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$110(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$109(tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$109(TLObject tLObject, int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processAttachMenuBot$108(dialogsActivity, baseFragment, tLRPC$User, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$108(DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (dialogsActivity != null) {
            lambda$runLinkRequest$86(dialogsActivity);
        } else if (baseFragment instanceof ChatActivity) {
            ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str, true);
        }
    }

    private void openForumFromLink(long j, Integer num, Runnable runnable) {
        openForumFromLink(j, num, null, runnable, 0);
    }

    private void openForumFromLink(final long j, final Integer num, final String str, final Runnable runnable, final int i) {
        if (num == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            lambda$runLinkRequest$86(new TopicsFragment(bundle));
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
        tLRPC$TL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(-j);
        tLRPC$TL_channels_getMessages.id.add(num);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda121
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openForumFromLink$115(num, j, runnable, str, i, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$115(final Integer num, final long j, final Runnable runnable, final String str, final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openForumFromLink$114(tLObject, num, j, runnable, str, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$114(TLObject tLObject, Integer num, long j, Runnable runnable, String str, int i) {
        TLRPC$Message tLRPC$Message;
        if (tLObject instanceof TLRPC$messages_Messages) {
            ArrayList<TLRPC$Message> arrayList = ((TLRPC$messages_Messages) tLObject).messages;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (arrayList.get(i2) != null && arrayList.get(i2).id == num.intValue()) {
                    tLRPC$Message = arrayList.get(i2);
                    break;
                }
            }
        }
        tLRPC$Message = null;
        if (tLRPC$Message != null) {
            runCommentRequest(this.currentAccount, null, Integer.valueOf(tLRPC$Message.id), null, Integer.valueOf(MessageObject.getTopicId(tLRPC$Message, MessagesController.getInstance(this.currentAccount).isForum(tLRPC$Message))), MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)), runnable, str, i);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$86(new TopicsFragment(bundle));
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
                    ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda110
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$checkAppUpdate$117(i, tLObject, tLRPC$TL_error);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$117(final int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            final TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$116(tLRPC$TL_help_appUpdate, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$116(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i) {
        TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate2 = SharedConfig.pendingAppUpdate;
        if ((tLRPC$TL_help_appUpdate2 == null || !tLRPC$TL_help_appUpdate2.version.equals(tLRPC$TL_help_appUpdate.version)) && SharedConfig.setNewAppVersionAvailable(tLRPC$TL_help_appUpdate)) {
            if (tLRPC$TL_help_appUpdate.can_not_skip) {
                showUpdateActivity(i, tLRPC$TL_help_appUpdate, false);
            } else if (ApplicationLoader.isStandaloneBuild() || BuildVars.DEBUG_VERSION) {
                this.drawerLayoutAdapter.notifyDataSetChanged();
                ApplicationLoader.applicationLoaderInstance.showUpdateAppPopup(this, tLRPC$TL_help_appUpdate, i);
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
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
            this.visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$showAlertDialog$118(dialogInterface);
                }
            });
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlertDialog$118(DialogInterface dialogInterface) {
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                INavigationLayout iNavigationLayout = this.actionBarLayout;
                BaseFragment lastFragment = iNavigationLayout == null ? null : iNavigationLayout.getLastFragment();
                try {
                    String str = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
                    if (lastFragment != null) {
                        BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.msg_translate, getStringForLanguageAlert(str.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater)).setDuration(5000).show();
                    } else {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(this), null).createSimpleBulletin(R.raw.msg_translate, getStringForLanguageAlert(str.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater)).setDuration(5000).show();
                    }
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
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.proxySettingsChanged, new Object[0]);
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
        handleIntent(intent, true, false, false, progress);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v5 */
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
        int i2 = 0;
        if (uri != null) {
            final ArrayList arrayList3 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            final AlertDialog alertDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).dialogId, this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j2) {
                    LaunchActivity.this.lambda$didSelectDialogs$119(currentAccount, dialogsActivity, z, arrayList3, uri, alertDialog, j2);
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
                    NotificationCenter.getInstance(currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
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
            if (this.voicePath != null) {
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
            if (this.videoPath == null && this.voicePath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                size++;
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, arrayList.get(i3).dialogId, size > 1)) {
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
                phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda139
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z8, int i4) {
                        LaunchActivity.this.lambda$didSelectDialogs$120(chatActivity5, arrayList, currentAccount, charSequence, z7, tLRPC$User, z8, i4);
                    }
                });
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                arrayList10.get(arrayList10.size() - 1).showDialog(phonebookShareAlert);
                chatActivity2 = chatActivity;
                z2 = true;
            } else {
                int i4 = 0;
                z2 = false;
                String str = null;
                while (i4 < arrayList.size()) {
                    long j3 = arrayList.get(i4).dialogId;
                    int i5 = arrayList.get(i4).topicId;
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (i5 == 0 || (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j3, i5)) == null || findTopic.topicStartMessage == null) {
                        messageObject = null;
                    } else {
                        messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, i2, i2);
                        messageObject.isTopicMainMessage = true;
                    }
                    if (chatActivity != null) {
                        i = i5;
                        j = j3;
                        boolean z8 = dialogsActivity == null || this.videoPath != null || ((arrayList2 = this.photoPathsArray) != null && arrayList2.size() > 0);
                        ChatActivity chatActivity6 = chatActivity;
                        getActionBarLayout().presentFragment(chatActivity, dialogsActivity != null, z8, true, false);
                        boolean z9 = dialogsActivity != null;
                        String str2 = this.videoPath;
                        if (str2 != null && i == 0) {
                            chatActivity6.openVideoEditor(str2, this.sendingText);
                            this.sendingText = null;
                            z5 = false;
                            z6 = true;
                        } else {
                            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList11 = this.photoPathsArray;
                            if (arrayList11 == null || arrayList11.size() <= 0 || i != 0) {
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
                        i = i5;
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
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList12, arrayList12, null, str, null, j, messageObject, messageObject, null, null, null, z7, 0, null);
                        }
                        z3 = false;
                        z4 = false;
                    }
                    if (this.photoPathsArray != null && !z3) {
                        String str4 = this.sendingText;
                        if (str4 != null && str4.length() <= 1024 && this.photoPathsArray.size() == 1) {
                            this.photoPathsArray.get(i2).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, messageObject, messageObject, null, null, false, false, null, z7, 0, false, null);
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
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j, messageObject, messageObject, null, null, null, z7, 0, null);
                    }
                    if (this.voicePath != null) {
                        File file = new File(this.voicePath);
                        if (file.exists()) {
                            TLRPC$TL_document tLRPC$TL_document = new TLRPC$TL_document();
                            tLRPC$TL_document.file_reference = new byte[i2];
                            tLRPC$TL_document.dc_id = Integer.MIN_VALUE;
                            tLRPC$TL_document.id = SharedConfig.getLastLocalId();
                            tLRPC$TL_document.user_id = accountInstance.getUserConfig().getClientUserId();
                            tLRPC$TL_document.mime_type = "audio/ogg";
                            tLRPC$TL_document.date = accountInstance.getConnectionsManager().getCurrentTime();
                            tLRPC$TL_document.size = (int) file.length();
                            TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                            tLRPC$TL_documentAttributeAudio.voice = true;
                            byte[] waveform = MediaController.getWaveform(file.getAbsolutePath());
                            tLRPC$TL_documentAttributeAudio.waveform = waveform;
                            if (waveform != null) {
                                tLRPC$TL_documentAttributeAudio.flags |= 4;
                            }
                            tLRPC$TL_document.attributes.add(tLRPC$TL_documentAttributeAudio);
                            accountInstance.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$TL_document, null, file.getAbsolutePath(), j, messageObject, messageObject, this.sendingText, null, null, null, z7, 0, 0, null, null, false));
                            if (this.sendingText != null) {
                                this.sendingText = null;
                            }
                        }
                    }
                    String str6 = this.sendingText;
                    if (str6 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str6, j, i, z7, 0);
                    }
                    ArrayList<TLRPC$User> arrayList15 = this.contactsToSend;
                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                        for (int i6 = 0; i6 < this.contactsToSend.size(); i6++) {
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(this.contactsToSend.get(i6), j, messageObject, messageObject, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z7, 0));
                        }
                    }
                    if (!TextUtils.isEmpty(charSequence) && !z4 && !z3) {
                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, i, z7, 0);
                    }
                    i4++;
                    chatActivity = chatActivity3;
                    i2 = 0;
                }
                chatActivity2 = chatActivity;
            }
            if (dialogsActivity != null && chatActivity2 == null && !z2) {
                dialogsActivity.finishFragment();
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.voicePath = null;
        this.sendingText = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
        this.exportingChatUri = null;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectDialogs$119(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
        if (j != 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (!AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            }
            if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setOpenImport();
            getActionBarLayout().presentFragment(chatActivity, dialogsActivity != null || z, dialogsActivity == null, true, false);
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
    public /* synthetic */ void lambda$didSelectDialogs$120(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2) {
        TLRPC$TL_forumTopic findTopic;
        if (chatActivity != null) {
            getActionBarLayout().presentFragment(chatActivity, true, false, true, false);
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
            SendMessagesHelper.getInstance(i).sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$User, j, messageObject2, messageObject2, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2));
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
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.attachMenuBotsDidLoad);
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
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.billingConfirmPurchaseError);
        LiteMode.removeOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda104(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPowerSaver(boolean z) {
        BaseFragment lastFragment;
        if (Build.VERSION.SDK_INT < 21 || this.actionBarLayout == null || !z || LiteMode.getPowerSaverLevel() >= 100 || (lastFragment = this.actionBarLayout.getLastFragment()) == null || (lastFragment instanceof LiteModeSettingsActivity)) {
            return;
        }
        int batteryLevel = LiteMode.getBatteryLevel();
        BulletinFactory.of(lastFragment).createSimpleBulletin(new BatteryDrawable(batteryLevel / 100.0f, -1, lastFragment.getThemedColor(Theme.key_dialogSwipeRemove), 1.3f), LocaleController.getString("LowPowerEnabledTitle", R.string.LowPowerEnabledTitle), LocaleController.formatString("LowPowerEnabledSubtitle", R.string.LowPowerEnabledSubtitle, String.format("%d%%", Integer.valueOf(batteryLevel))), LocaleController.getString("Disable", R.string.Disable), new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onPowerSaver$121();
            }
        }).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPowerSaver$121() {
        lambda$runLinkRequest$86(new LiteModeSettingsActivity());
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$86(BaseFragment baseFragment) {
        getActionBarLayout().presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return getActionBarLayout().presentFragment(baseFragment, z, z2, true, false);
    }

    public INavigationLayout getActionBarLayout() {
        INavigationLayout iNavigationLayout = this.actionBarLayout;
        if (this.sheetFragmentsStack.isEmpty()) {
            return iNavigationLayout;
        }
        ArrayList<INavigationLayout> arrayList = this.sheetFragmentsStack;
        return arrayList.get(arrayList.size() - 1);
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
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onActivityResult$122();
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
                BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
                baseFragment.onActivityResultFragment(i, i2, intent);
                StoryViewer storyViewer = baseFragment.storyViewer;
                if (storyViewer != null && storyViewer.isShown()) {
                    baseFragment.storyViewer.onActivityResult(i, i2, intent);
                }
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.getFragmentStack().size() != 0) {
                    this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1).onActivityResultFragment(i, i2, intent);
                }
                if (this.layersActionBarLayout.getFragmentStack().size() != 0) {
                    this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1).onActivityResultFragment(i, i2, intent);
                }
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onActivityResultReceived, Integer.valueOf(i), Integer.valueOf(i2), intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$122() {
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
            StoryRecorder.onRequestPermissionsResult(i, strArr, iArr);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onRequestPermissionResultReceived, Integer.valueOf(i), strArr, iArr);
            if (this.requestedPermissions.get(i, -1) >= 0) {
                int i2 = this.requestedPermissions.get(i, -1);
                this.requestedPermissions.delete(i);
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.permissionsGranted, Integer.valueOf(i2));
            }
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM));
        ApplicationLoader.mainInterfacePaused = true;
        final int i = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$123(i);
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
        StoryRecorder.onPause();
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onPause();
        }
        SpoilerEffect2.pause(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onPause$123(int i) {
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
        FlagSecureReason flagSecureReason = this.flagSecureReason;
        if (flagSecureReason != null) {
            flagSecureReason.detach();
        }
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
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM));
        MediaController mediaController = MediaController.getInstance();
        ViewGroup view = this.actionBarLayout.getView();
        this.feedbackView = view;
        mediaController.setFeedbackView(view, true);
        ApplicationLoader.mainInterfacePaused = false;
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda98.INSTANCE);
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
        StoryRecorder.onResume();
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
        SpoilerEffect2.pause(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$124() {
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
        BoostPagerBottomSheet boostPagerBottomSheet = BoostPagerBottomSheet.getInstance();
        if (boostPagerBottomSheet != null) {
            boostPagerBottomSheet.onConfigurationChanged(configuration);
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

    /* JADX WARN: Code restructure failed: missing block: B:110:0x02fe, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L124;
     */
    /* JADX WARN: Removed duplicated region for block: B:109:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0662  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x066a  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0670 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0671  */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, final int i2, Object... objArr) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        BaseFragment baseFragment;
        int i3;
        String str;
        int i4;
        String str2;
        GroupCallActivity groupCallActivity;
        BaseFragment baseFragment2;
        boolean z;
        INavigationLayout iNavigationLayout;
        boolean z2;
        View childAt;
        BaseFragment baseFragment3;
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        boolean z3 = false;
        boolean z4 = false;
        r4 = false;
        r4 = false;
        boolean z5 = false;
        z3 = false;
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
        } else if (i == NotificationCenter.attachMenuBotsDidLoad) {
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
                        int i5 = Theme.key_dialogTopBackground;
                        hashMap.put("info1.**", Integer.valueOf(baseFragment3.getThemedColor(i5)));
                        hashMap.put("info2.**", Integer.valueOf(baseFragment3.getThemedColor(i5)));
                        builder.setTopAnimation(R.raw.not_available, 52, false, baseFragment3.getThemedColor(i5), hashMap);
                        builder.setTopAnimationIsNew(true);
                    }
                    if (num.intValue() != 2 && num.intValue() != 3) {
                        builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i6) {
                                LaunchActivity.lambda$didReceivedNotification$125(i2, dialogInterface, i6);
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
                            int i6 = indexOf + 1;
                            int indexOf2 = str4.indexOf(42, i6);
                            if (indexOf != -1 && indexOf2 != -1 && indexOf != indexOf2) {
                                valueOf.replace(indexOf, indexOf2 + 1, (CharSequence) str4.substring(i6, indexOf2));
                                valueOf.setSpan(new ClickableSpan() { // from class: org.telegram.ui.LaunchActivity.20
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
                            builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                    LaunchActivity.this.lambda$didReceivedNotification$126(dialogInterface, i7);
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
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.this.lambda$didReceivedNotification$128(hashMap2, i2, dialogInterface, i7);
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
            this.flagSecureReason.invalidate();
        } else if (i == NotificationCenter.reloadInterface) {
            if (mainFragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                if (arrayList4.get(arrayList4.size() - 1) instanceof ProfileActivity) {
                    z2 = true;
                    if (z2) {
                        ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                    }
                    z4 = z2;
                    rebuildAllFragments(z4);
                }
            }
            z2 = false;
            if (z2) {
            }
            z4 = z2;
            rebuildAllFragments(z4);
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
            INavigationLayout iNavigationLayout2 = this.actionBarLayout;
            if (iNavigationLayout2 == null || iNavigationLayout2.getFragmentStack().isEmpty()) {
                return;
            }
            ((Integer) objArr[0]).intValue();
            final HashMap hashMap3 = (HashMap) objArr[1];
            final boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            final boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
            builder3.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
            builder3.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
            builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$129(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$130(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
                }
            });
            builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$131(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
                }
            });
            AlertDialog create = builder3.create();
            this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1).showDialog(create);
            create.setCanceledOnTouchOutside(false);
        } else if (i == NotificationCenter.didSetNewTheme) {
            if (!((Boolean) objArr[0]).booleanValue()) {
                if (this.sideMenu != null) {
                    FrameLayout frameLayout = this.sideMenuContainer;
                    if (frameLayout != null) {
                        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                    }
                    RecyclerListView recyclerListView2 = this.sideMenu;
                    int i7 = Theme.key_chats_menuBackground;
                    recyclerListView2.setBackgroundColor(Theme.getColor(i7));
                    this.sideMenu.setGlowColor(Theme.getColor(i7));
                    this.sideMenu.setListSelectorColor(Integer.valueOf(Theme.getColor(Theme.key_listSelector)));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor(Theme.key_actionBarDefault) | (-16777216)));
                    } catch (Exception unused) {
                    }
                }
            }
            this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            boolean booleanValue3 = objArr.length > 1 ? ((Boolean) objArr[1]).booleanValue() : true;
            boolean z6 = objArr.length > 2 && ((Boolean) objArr[2]).booleanValue();
            if (booleanValue3 && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                z5 = true;
            }
            checkSystemBarColors(z6, true, z5, true);
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
                    ImageView imageView = new ImageView(this);
                    this.themeSwitchImageView = imageView;
                    if (booleanValue4) {
                        this.frameLayout.addView(imageView, 0, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setVisibility(8);
                    } else {
                        this.frameLayout.addView(imageView, 1, LayoutHelper.createFrame(-1, -1.0f));
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
                    int i8 = iArr[0];
                    int i9 = iArr[1];
                    float f = booleanValue4 ? 0.0f : max;
                    if (!booleanValue4) {
                        max = 0.0f;
                    }
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view3, i8, i9, f, max);
                    createCircularReveal.setDuration(400L);
                    createCircularReveal.setInterpolator(Easings.easeInOutQuad);
                    createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.21
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            LaunchActivity.this.rippleAbove = null;
                            LaunchActivity.this.drawerLayoutContainer.invalidate();
                            LaunchActivity.this.themeSwitchImageView.invalidate();
                            LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
                            LaunchActivity.this.themeSwitchImageView.setVisibility(8);
                            LaunchActivity.this.themeSwitchSunView.setVisibility(8);
                            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeAccentListUpdated, new Object[0]);
                            if (!booleanValue4) {
                                rLottieImageView.setVisibility(0);
                            }
                            DrawerProfileCell.switchingTheme = false;
                        }
                    });
                    if (this.rippleAbove != null) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                LaunchActivity.this.lambda$didReceivedNotification$132(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(createCircularReveal.getDuration());
                        ofFloat.start();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$didReceivedNotification$133();
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
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) objArr[0];
                boolean booleanValue5 = ((Boolean) objArr[1]).booleanValue();
                int intValue = ((Integer) objArr[3]).intValue();
                Runnable runnable = objArr.length <= 7 ? (Runnable) objArr[7] : null;
                iNavigationLayout = this.actionBarLayout;
                if (iNavigationLayout != null) {
                    return;
                }
                iNavigationLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z, runnable);
                if (AndroidUtilities.isTablet()) {
                    this.layersActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                    this.rightActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                    return;
                }
                return;
            }
            DrawerProfileCell.switchingTheme = false;
            z = false;
            Theme.ThemeInfo themeInfo2 = (Theme.ThemeInfo) objArr[0];
            boolean booleanValue52 = ((Boolean) objArr[1]).booleanValue();
            int intValue2 = ((Integer) objArr[3]).intValue();
            if (objArr.length <= 7) {
            }
            iNavigationLayout = this.actionBarLayout;
            if (iNavigationLayout != null) {
            }
        } else if (i == NotificationCenter.notificationsCountUpdated) {
            RecyclerListView recyclerListView3 = this.sideMenu;
            if (recyclerListView3 != null) {
                Integer num2 = (Integer) objArr[0];
                int childCount = recyclerListView3.getChildCount();
                for (int i10 = 0; i10 < childCount; i10++) {
                    View childAt2 = this.sideMenu.getChildAt(i10);
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
            if (SharedConfig.isAppUpdateAvailable()) {
                FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str5);
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
                            ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda132
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$didReceivedNotification$135(fillThemeValues, tLObject, tLRPC$TL_error);
                                }
                            });
                            return;
                        }
                        TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                        if (applyThemeFile != null) {
                            lambda$runLinkRequest$86(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$didReceivedNotification$137(themeInfo3, file2);
                }
            });
        } else if (i == NotificationCenter.fileLoadFailed) {
            String str8 = (String) objArr[0];
            if (str8.equals(this.loadingThemeFileName) || str8.equals(this.loadingThemeWallpaperName)) {
                onThemeLoadFinish();
            }
            if (SharedConfig.isAppUpdateAvailable()) {
                FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str8);
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
                z3 = true;
            }
            checkSystemBarColors(z3);
        } else if (i == NotificationCenter.historyImportProgressChanged) {
            if (objArr.length <= 1 || mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
            AlertsCreator.processError(this.currentAccount, (TLRPC$TL_error) objArr[2], arrayList7.get(arrayList7.size() - 1), (TLObject) objArr[1], new Object[0]);
        } else if (i == NotificationCenter.billingConfirmPurchaseError) {
            ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
            AlertsCreator.processError(this.currentAccount, (TLRPC$TL_error) objArr[1], arrayList8.get(arrayList8.size() - 1), (TLObject) objArr[0], new Object[0]);
        } else if (i == NotificationCenter.stickersImportComplete) {
            MediaDataController mediaDataController = MediaDataController.getInstance(i2);
            TLObject tLObject = (TLObject) objArr[0];
            if (mainFragmentsStack.isEmpty()) {
                baseFragment2 = null;
            } else {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                baseFragment2 = arrayList9.get(arrayList9.size() - 1);
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
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                baseFragment = arrayList10.get(arrayList10.size() - 1);
            } else {
                baseFragment = null;
            }
            int i11 = 1500;
            switch (intValue3) {
                case 0:
                    TLRPC$Document tLRPC$Document = (TLRPC$Document) objArr[1];
                    int intValue4 = ((Integer) objArr[2]).intValue();
                    StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, tLRPC$Document, null);
                    i11 = (intValue4 == 6 || intValue4 == 7) ? 3500 : 3500;
                    if (baseFragment != null) {
                        Bulletin.make(baseFragment, stickerSetBulletinLayout, i11).show();
                        return;
                    } else {
                        Bulletin.make(container, stickerSetBulletinLayout, i11).show();
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
                        str = "ChannelDescriptionChanged";
                    }
                    (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str, i3)).show();
                    return;
                case 3:
                    if (((Long) objArr[1]).longValue() > 0) {
                        i4 = R.string.YourNameChanged;
                        str2 = "YourNameChanged";
                    } else {
                        i4 = R.string.ChannelTitleChanged;
                        str2 = "ChannelTitleChanged";
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
        } else if (i == NotificationCenter.groupCallUpdated) {
            checkWasMutedByAdmin(false);
        } else if (i != NotificationCenter.fileLoadProgressChanged && i != NotificationCenter.appUpdateAvailable) {
            if (i == NotificationCenter.currentUserShowLimitReachedDialog) {
                if (mainFragmentsStack.isEmpty()) {
                    return;
                }
                ArrayList<BaseFragment> arrayList11 = mainFragmentsStack;
                BaseFragment baseFragment4 = arrayList11.get(arrayList11.size() - 1);
                if (baseFragment4.getParentActivity() != null) {
                    baseFragment4.showDialog(new LimitReachedBottomSheet(baseFragment4, baseFragment4.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount, null));
                }
            } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
                DrawerLayoutAdapter drawerLayoutAdapter2 = this.drawerLayoutAdapter;
                if (drawerLayoutAdapter2 != null) {
                    drawerLayoutAdapter2.notifyDataSetChanged();
                }
                MessagesController.getMainSettings(this.currentAccount).edit().remove("transcribeButtonPressed").apply();
            } else if (i == NotificationCenter.requestPermissions) {
                int intValue5 = ((Integer) objArr[0]).intValue();
                String[] strArr = (intValue5 != 0 || Build.VERSION.SDK_INT < 31) ? null : new String[]{"android.permission.BLUETOOTH_CONNECT"};
                if (strArr != null) {
                    int i12 = this.requsetPermissionsPointer + 1;
                    this.requsetPermissionsPointer = i12;
                    this.requestedPermissions.put(i12, intValue5);
                    ActivityCompat.requestPermissions(this, strArr, this.requsetPermissionsPointer);
                }
            } else if (i == NotificationCenter.chatSwithcedToForum) {
                ForumUtilities.switchAllFragmentsInStackToForum(((Long) objArr[0]).longValue(), this.actionBarLayout);
            } else if (i == NotificationCenter.storiesEnabledUpdate && (drawerLayoutAdapter = this.drawerLayoutAdapter) != null) {
                drawerLayoutAdapter.notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$125(int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(i);
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$126(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$128(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (AndroidUtilities.isMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            LocationActivity locationActivity = new LocationActivity(0);
            locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda149
                @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
                public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i3, boolean z, int i4) {
                    LaunchActivity.lambda$didReceivedNotification$127(hashMap, i, tLRPC$MessageMedia, i3, z, i4);
                }
            });
            lambda$runLinkRequest$86(locationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$127(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$129(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$130(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$131(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$132(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$133() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$135(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$134(tLObject, themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$134(TLObject tLObject, Theme.ThemeInfo themeInfo) {
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
    public /* synthetic */ void lambda$didReceivedNotification$137(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$136();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$136() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$86(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
        UndoView undoView = null;
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-chat.id)) {
                chat = null;
            }
            undoView = chatActivity.getUndoView();
        } else if (baseFragment instanceof DialogsActivity) {
            undoView = ((DialogsActivity) baseFragment).getUndoView();
        } else if (baseFragment instanceof ProfileActivity) {
            undoView = ((ProfileActivity) baseFragment).getUndoView();
        }
        if (undoView != null) {
            undoView.showWithAction(0L, i, chat);
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
        lambda$runLinkRequest$86(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$140(i);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$140(int i) {
        File directory;
        long availableBlocksLong;
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if ((((i == 2 || i == 1) && Math.abs(this.alreadyShownFreeDiscSpaceAlertForced - System.currentTimeMillis()) > 240000) || Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) >= 259200000) && (directory = FileLoader.getDirectory(4)) != null) {
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkFreeDiscSpace$139();
                            }
                        });
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$139() {
        if (this.checkFreeDiscSpaceShown) {
            return;
        }
        try {
            Dialog createFreeSpaceDialog = AlertsCreator.createFreeSpaceDialog(this);
            createFreeSpaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$138(dialogInterface);
                }
            });
            this.checkFreeDiscSpaceShown = true;
            createFreeSpaceDialog.show();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$138(DialogInterface dialogInterface) {
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
    /* JADX WARN: Removed duplicated region for block: B:24:0x006a A[Catch: Exception -> 0x011e, TryCatch #0 {Exception -> 0x011e, blocks: (B:3:0x0007, B:5:0x0010, B:10:0x001e, B:14:0x0056, B:18:0x005e, B:22:0x0065, B:24:0x006a, B:28:0x007e, B:32:0x00a0, B:33:0x00bc), top: B:38:0x0007 }] */
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
                    languageCellArr[i].setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                    languageCellArr[i].setLanguageSelected(i == 0, false);
                    linearLayout.addView(languageCellArr[i], LayoutHelper.createLinear(-1, 50));
                    languageCellArr[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LaunchActivity.lambda$showLanguageAlertInternal$141(localeInfoArr, languageCellArr, view);
                        }
                    });
                    i++;
                }
                LanguageCell languageCell = new LanguageCell(this);
                HashMap<String, String> hashMap2 = this.systemLocaleStrings;
                int i3 = R.string.ChooseYourLanguageOther;
                languageCell.setValue(getStringForLanguageAlert(hashMap2, "ChooseYourLanguageOther", i3), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i3));
                languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$142(view);
                    }
                });
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$143(localeInfoArr, dialogInterface, i4);
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
            languageCell2.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
            languageCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$142(view);
                }
            });
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$143(localeInfoArr3, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showLanguageAlertInternal$141(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$142(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$86(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$143(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
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
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda135
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$145(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 8);
                        TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                        tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                        tLRPC$TL_langpack_getStrings2.keys.add("English");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda136
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$147(localeInfoArr, str2, tLObject, tLRPC$TL_error);
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
    public /* synthetic */ void lambda$showLanguageAlert$145(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$144(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$144(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$147(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$146(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$146(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
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
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity.22
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
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$updateCurrentConnectionState$148();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentConnectionState$148() {
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
        lambda$runLinkRequest$86(new ProxyListActivity());
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
        StoryViewer storyViewer;
        StoryViewer storyViewer2;
        keyEvent.getKeyCode();
        boolean z = true;
        if (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) {
            BaseFragment lastFragment = getLastFragment();
            if (lastFragment != null && (storyViewer2 = lastFragment.overlayStoryViewer) != null && storyViewer2.isShown()) {
                lastFragment.overlayStoryViewer.dispatchKeyEvent(keyEvent);
                return true;
            } else if (lastFragment != null && (storyViewer = lastFragment.storyViewer) != null && storyViewer.isShown()) {
                lastFragment.storyViewer.dispatchKeyEvent(keyEvent);
                return true;
            }
        }
        if (keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25)) {
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
            return super.dispatchKeyEvent(keyEvent);
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
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
        BaseFragment baseFragment = navigationParams.fragment;
        boolean z = navigationParams.removeLast;
        boolean z2 = navigationParams.noAnimation;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z3 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z3 || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || ((iNavigationLayout5 = this.layersActionBarLayout) != null && iNavigationLayout5.getView().getVisibility() == 0)) ? false : true, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (iNavigationLayout4 = this.actionBarLayout)) {
                iNavigationLayout4.removeAllFragments();
                getActionBarLayout().presentFragment(navigationParams.setRemoveLast(z).setNoAnimation(z2).setCheckPresentFromDelegate(false));
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
                    boolean z5 = (z4 && iNavigationLayout == (iNavigationLayout3 = this.actionBarLayout) && iNavigationLayout3.getFragmentStack().size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout6 = this.layersActionBarLayout;
                            iNavigationLayout6.removeFragmentFromStack(iNavigationLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z5) {
                        getActionBarLayout().presentFragment(navigationParams.setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    }
                    return z5;
                } else if (!z4 && iNavigationLayout != (iNavigationLayout2 = this.rightActionBarLayout)) {
                    iNavigationLayout2.getView().setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(navigationParams.setNoAnimation(true).setRemoveLast(z).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout7 = this.layersActionBarLayout;
                            iNavigationLayout7.removeFragmentFromStack(iNavigationLayout7.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else if (z4 && iNavigationLayout != this.actionBarLayout) {
                    getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout8 = this.layersActionBarLayout;
                            iNavigationLayout8.removeFragmentFromStack(iNavigationLayout8.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else {
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout9 = this.layersActionBarLayout;
                            iNavigationLayout9.removeFragmentFromStack(iNavigationLayout9.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    return false;
                }
            } else {
                INavigationLayout iNavigationLayout10 = this.layersActionBarLayout;
                if (iNavigationLayout != iNavigationLayout10) {
                    iNavigationLayout10.getView().setVisibility(0);
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
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity) || (mainFragmentsStack.get(0) instanceof LoginActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
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
            this.drawerLayoutContainer.setAllowOpenDrawer((z || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity) || this.layersActionBarLayout.getView().getVisibility() == 0) ? false : true, true);
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
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
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

    public static BaseFragment getLastFragment() {
        LaunchActivity launchActivity = instance;
        if (launchActivity != null && !launchActivity.sheetFragmentsStack.isEmpty()) {
            ArrayList<INavigationLayout> arrayList = instance.sheetFragmentsStack;
            return arrayList.get(arrayList.size() - 1).getLastFragment();
        }
        LaunchActivity launchActivity2 = instance;
        if (launchActivity2 == null || launchActivity2.getActionBarLayout() == null) {
            return null;
        }
        return instance.getActionBarLayout().getLastFragment();
    }

    public void requestCustomNavigationBar() {
        if (this.customNavigationBar == null && Build.VERSION.SDK_INT >= 26) {
            this.customNavigationBar = this.drawerLayoutContainer.createNavigationBar();
            ((FrameLayout) getWindow().getDecorView()).addView(this.customNavigationBar);
        }
        View view = this.customNavigationBar;
        if (view != null) {
            if (view.getLayoutParams().height == AndroidUtilities.navigationBarHeight && ((FrameLayout.LayoutParams) this.customNavigationBar.getLayoutParams()).topMargin == this.customNavigationBar.getHeight()) {
                return;
            }
            this.customNavigationBar.getLayoutParams().height = AndroidUtilities.navigationBarHeight;
            ((FrameLayout.LayoutParams) this.customNavigationBar.getLayoutParams()).topMargin = this.drawerLayoutContainer.getMeasuredHeight();
            this.customNavigationBar.requestLayout();
        }
    }

    public int getNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= 26) {
            Window window = getWindow();
            if (this.customNavigationBar != null) {
                return this.drawerLayoutContainer.getNavigationBarColor();
            }
            return window.getNavigationBarColor();
        }
        return 0;
    }

    public void setNavigationBarColor(int i, boolean z) {
        if (Build.VERSION.SDK_INT >= 26) {
            Window window = getWindow();
            if (this.customNavigationBar != null) {
                if (this.drawerLayoutContainer.getNavigationBarColor() != i) {
                    this.drawerLayoutContainer.setNavigationBarColor(i);
                    if (z) {
                        AndroidUtilities.setLightNavigationBar(window, AndroidUtilities.computePerceivedBrightness(i) >= 0.721f);
                    }
                }
            } else if (window.getNavigationBarColor() != i) {
                window.setNavigationBarColor(i);
                if (z) {
                    AndroidUtilities.setLightNavigationBar(window, AndroidUtilities.computePerceivedBrightness(i) >= 0.721f);
                }
            }
        }
    }

    public void animateNavigationBarColor(final int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        ValueAnimator valueAnimator = this.navBarAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.navBarAnimator = null;
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(getNavigationBarColor(), i);
        this.navBarAnimator = ofArgb;
        ofArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                LaunchActivity.this.lambda$animateNavigationBarColor$149(valueAnimator2);
            }
        });
        this.navBarAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.23
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LaunchActivity.this.setNavigationBarColor(i, false);
            }
        });
        this.navBarAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.navBarAnimator.setDuration(320L);
        this.navBarAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateNavigationBarColor$149(ValueAnimator valueAnimator) {
        setNavigationBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue(), false);
    }

    public void setLightNavigationBar(boolean z) {
        if (Build.VERSION.SDK_INT >= 26) {
            AndroidUtilities.setLightNavigationBar(getWindow(), z);
        }
    }

    public boolean isLightNavigationBar() {
        return AndroidUtilities.getLightNavigationBar(getWindow());
    }

    private void openStories(long[] jArr, boolean z) {
        boolean z2;
        final long[] jArr2 = jArr;
        int i = 0;
        while (true) {
            if (i >= jArr2.length) {
                z2 = true;
                break;
            }
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(jArr2[i]));
            if (user != null && !user.stories_hidden) {
                z2 = false;
                break;
            }
            i++;
        }
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null) {
            return;
        }
        StoriesController storiesController = MessagesController.getInstance(this.currentAccount).getStoriesController();
        ArrayList arrayList = new ArrayList(z2 ? storiesController.getHiddenList() : storiesController.getDialogListStories());
        ArrayList<Long> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        if (!z2) {
            ArrayList arrayList4 = new ArrayList();
            for (int i2 = 0; i2 < jArr2.length; i2++) {
                TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(jArr2[i2]));
                if (user2 == null || !user2.stories_hidden) {
                    arrayList4.add(Long.valueOf(jArr2[i2]));
                }
            }
            jArr2 = Longs.toArray(arrayList4);
        }
        if (z) {
            for (long j : jArr2) {
                arrayList3.add(Long.valueOf(j));
            }
        } else {
            for (long j2 : jArr2) {
                arrayList2.add(Long.valueOf(j2));
            }
        }
        if (!arrayList3.isEmpty() && z) {
            final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            final int[] iArr = {arrayList3.size()};
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openStories$150(iArr, jArr2);
                }
            };
            for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                final long longValue = ((Long) arrayList3.get(i3)).longValue();
                TL_stories$TL_stories_getPeerStories tL_stories$TL_stories_getPeerStories = new TL_stories$TL_stories_getPeerStories();
                TLRPC$InputPeer inputPeer = messagesController.getInputPeer(longValue);
                tL_stories$TL_stories_getPeerStories.peer = inputPeer;
                if (inputPeer instanceof TLRPC$TL_inputPeerEmpty) {
                    iArr[0] = iArr[0] - 1;
                } else if (inputPeer == null) {
                    iArr[0] = iArr[0] - 1;
                } else {
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_getPeerStories, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda108
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.lambda$openStories$152(MessagesController.this, longValue, runnable, tLObject, tLRPC$TL_error);
                        }
                    });
                }
            }
            return;
        }
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            long peerDialogId = DialogObject.getPeerDialogId(((TL_stories$PeerStories) arrayList.get(i4)).peer);
            if (peerDialogId != clientUserId && !arrayList2.contains(Long.valueOf(peerDialogId)) && storiesController.hasUnreadStories(peerDialogId)) {
                arrayList2.add(Long.valueOf(peerDialogId));
            }
        }
        if (arrayList2.isEmpty()) {
            return;
        }
        StoriesListPlaceProvider storiesListPlaceProvider = null;
        if (lastFragment instanceof DialogsActivity) {
            try {
                storiesListPlaceProvider = StoriesListPlaceProvider.of(((DialogsActivity) lastFragment).dialogStoriesCell.recyclerListView);
            } catch (Exception unused) {
            }
        }
        lastFragment.getOrCreateStoryViewer().instantClose();
        lastFragment.getOrCreateStoryViewer().open(this, null, arrayList2, 0, null, null, storiesListPlaceProvider, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openStories$150(int[] iArr, long[] jArr) {
        iArr[0] = iArr[0] - 1;
        if (iArr[0] == 0) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.storiesUpdated, new Object[0]);
            openStories(jArr, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStories$152(final MessagesController messagesController, final long j, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$openStories$151(TLObject.this, messagesController, j, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStories$151(TLObject tLObject, MessagesController messagesController, long j, Runnable runnable) {
        if (tLObject instanceof TL_stories$TL_stories_peerStories) {
            TL_stories$TL_stories_peerStories tL_stories$TL_stories_peerStories = (TL_stories$TL_stories_peerStories) tLObject;
            messagesController.putUsers(tL_stories$TL_stories_peerStories.users, false);
            messagesController.getStoriesController().putStories(j, tL_stories$TL_stories_peerStories.stories);
            runnable.run();
            return;
        }
        runnable.run();
    }
}
