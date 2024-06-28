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
import java.util.Set;
import java.util.concurrent.CountDownLatch;
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
import org.telegram.messenger.OpenAttachedMenuBotReceiver;
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
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$LangPackString;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$PaymentForm;
import org.telegram.tgnet.TLRPC$PaymentReceipt;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getTheme;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_resolveBusinessChatLink;
import org.telegram.tgnet.TLRPC$TL_account_resolvedBusinessChatLinks;
import org.telegram.tgnet.TLRPC$TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots;
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
import org.telegram.tgnet.TLRPC$TL_help_noAppUpdate;
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
import org.telegram.tgnet.TLRPC$TL_payments_paymentFormStars;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
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
import org.telegram.tgnet.tl.TL_stories$TL_stories_getStoriesByID;
import org.telegram.tgnet.tl.TL_stories$TL_stories_peerStories;
import org.telegram.tgnet.tl.TL_stories$TL_stories_stories;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.BottomSheetTabsOverlay;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.ChatMessageCell;
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
import org.telegram.ui.Components.PasscodeViewDialog;
import org.telegram.ui.Components.PhonebookShareAlert;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostPagerBottomSheet;
import org.telegram.ui.Components.Premium.boosts.GiftInfoBottomSheet;
import org.telegram.ui.Components.Premium.boosts.UserSelectorBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SearchTagsList;
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
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WallpapersListActivity;
import org.telegram.ui.bots.BotWebViewAttachedSheet;
import org.telegram.ui.bots.BotWebViewSheet;
import org.webrtc.MediaStreamTrack;
import org.webrtc.voiceengine.WebRtcAudioTrack;
/* loaded from: classes4.dex */
public class LaunchActivity extends BasePermissionsActivity implements INavigationLayout.INavigationLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    public static LaunchActivity instance;
    public static boolean isActive;
    public static boolean isResumed;
    public static Runnable onResumeStaticCallback;
    private static LaunchActivity staticInstanceForAlerts;
    public static boolean systemBlurEnabled;
    private ActionBarLayout actionBarLayout;
    private long alreadyShownFreeDiscSpaceAlertForced;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private BottomSheetTabsOverlay bottomSheetTabsOverlay;
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
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private TLRPC$TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private String loadingThemeFileName;
    private Theme.ThemeInfo loadingThemeInfo;
    private AlertDialog loadingThemeProgressDialog;
    private TLRPC$TL_wallPaper loadingThemeWallpaper;
    private String loadingThemeWallpaperName;
    private Dialog localeDialog;
    private Runnable lockRunnable;
    private ValueAnimator navBarAnimator;
    private boolean navigateToPremiumBot;
    private Runnable navigateToPremiumGiftCallback;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private PasscodeViewDialog passcodeDialog;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private ArrayList<SendMessagesHelper.SendingMediaInfo> photoPathsArray;
    private Dialog proxyErrorDialog;
    private ActionBarLayout rightActionBarLayout;
    private View rippleAbove;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private FrameLayout sideMenuContainer;
    private boolean switchingAccount;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private int[] tempLocation;
    private TermsOfServiceView termsOfServiceView;
    private ImageView themeSwitchImageView;
    private RLottieDrawable themeSwitchSunDrawable;
    private View themeSwitchSunView;
    private String videoPath;
    private ActionMode visibleActionMode;
    private String voicePath;
    private boolean wasMutedByAdminRaisedHand;
    public static final Pattern PREFIX_T_ME_PATTERN = Pattern.compile("^(?:http(?:s|)://|)([A-z0-9-]+?)\\.t\\.me");
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList<>();
    public ArrayList<INavigationLayout> sheetFragmentsStack = new ArrayList<>();
    private List<PasscodeView> overlayPasscodeViews = new ArrayList();
    public final ArrayList<Dialog> visibleDialogs = new ArrayList<>();
    private boolean isNavigationBarColorFrozen = false;
    private List<Runnable> onUserLeaveHintListeners = new ArrayList();
    private SparseIntArray requestedPermissions = new SparseIntArray();
    private int requsetPermissionsPointer = 5934;
    private Consumer<Boolean> blurListener = new Consumer<Boolean>(this) { // from class: org.telegram.ui.LaunchActivity.1
        @Override // j$.util.function.Consumer
        public /* synthetic */ Consumer<Boolean> andThen(Consumer<? super Boolean> consumer) {
            return Consumer.-CC.$default$andThen(this, consumer);
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

    public Dialog getVisibleDialog() {
        for (int size = this.visibleDialogs.size() - 1; size >= 0; size--) {
            Dialog dialog = this.visibleDialogs.get(size);
            if (dialog.isShowing()) {
                return dialog;
            }
        }
        return null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        boolean z;
        ActionBarLayout actionBarLayout;
        Intent intent;
        Uri data;
        isActive = true;
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
        FlagSecureReason flagSecureReason = new FlagSecureReason(getWindow(), new FlagSecureReason.FlagSecureCondition() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda107
            @Override // org.telegram.messenger.FlagSecureReason.FlagSecureCondition
            public final boolean run() {
                boolean lambda$onCreate$0;
                lambda$onCreate$0 = LaunchActivity.lambda$onCreate$0();
                return lambda$onCreate$0;
            }
        });
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
        this.actionBarLayout = new ActionBarLayout(this, true);
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.2
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LaunchActivity.this.drawRippleAbove(canvas, this);
            }
        };
        this.frameLayout = frameLayout;
        frameLayout.setClipToPadding(false);
        this.frameLayout.setClipChildren(false);
        char c = 65535;
        setContentView(this.frameLayout, new ViewGroup.LayoutParams(-1, -1));
        if (i2 >= 21) {
            ImageView imageView = new ImageView(this);
            this.themeSwitchImageView = imageView;
            imageView.setVisibility(8);
        }
        3 r8 = new 3(this);
        this.drawerLayoutContainer = r8;
        r8.setClipChildren(false);
        this.drawerLayoutContainer.setClipToPadding(false);
        this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
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
        BottomSheetTabsOverlay bottomSheetTabsOverlay = new BottomSheetTabsOverlay(this);
        this.bottomSheetTabsOverlay = bottomSheetTabsOverlay;
        frameLayout2.addView(bottomSheetTabsOverlay);
        FrameLayout frameLayout3 = this.frameLayout;
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
        frameLayout3.addView(fireworksOverlay);
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
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda151
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
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda152
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
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
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
        LiteMode.addOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda112(this));
        if (this.actionBarLayout.getFragmentStack().isEmpty() && ((actionBarLayout = this.layersActionBarLayout) == null || actionBarLayout.getFragmentStack().isEmpty())) {
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
    /* loaded from: classes4.dex */
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

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (LaunchActivity.this.actionBarLayout.getParent() == this) {
                LaunchActivity.this.actionBarLayout.parentDraw(this, canvas);
            }
            super.dispatchDraw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        showSelectStatusDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$6(View view, int i, float f, float f2) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        if (this.drawerLayoutAdapter.click(view, i)) {
            this.drawerLayoutContainer.closeDrawer(false);
            return;
        }
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
                    lambda$runLinkRequest$88(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.getFragmentStack().size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount, null);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
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
                lambda$runLinkRequest$88(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$88(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$88(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$88(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                Bundle bundle3 = new Bundle();
                bundle3.putBoolean("needFinishFragment", false);
                lambda$runLinkRequest$88(new ContactsActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$88(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$88(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$88(new ChatActivity(bundle4));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$88(new ActionIntroActivity(1));
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
                    lambda$runLinkRequest$88(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$88(new ActionIntroActivity(4));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 13) {
                Browser.openUrl(this, LocaleController.getString("TelegramFeaturesUrl", R.string.TelegramFeaturesUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 15) {
                showSelectStatusDialog();
            } else if (id == 16) {
                this.drawerLayoutContainer.closeDrawer(true);
                Bundle bundle5 = new Bundle();
                bundle5.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                bundle5.putBoolean("my_profile", true);
                lambda$runLinkRequest$88(new ProfileActivity(bundle5, null));
            } else if (id == 17) {
                this.drawerLayoutContainer.closeDrawer(true);
                Bundle bundle6 = new Bundle();
                bundle6.putLong("dialog_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                bundle6.putInt("type", 1);
                lambda$runLinkRequest$88(new MediaActivity(bundle6, null));
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda138
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$onCreate$4(tLRPC$TL_attachMenuBot, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
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
        if (getLastFragment() == null || getLastFragment().getLastStoryViewer() == null) {
            return;
        }
        getLastFragment().getLastStoryViewer().updatePlayingMode();
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
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null) {
            return;
        }
        BotWebViewAttachedSheet createBotViewer = lastFragment.createBotViewer();
        createBotViewer.setParentActivity(this);
        int i = this.currentAccount;
        long j = tLRPC$TL_attachMenuBot.bot_id;
        createBotViewer.requestWebView(null, BotWebViewAttachedSheet.WebViewRequestProps.of(i, j, j, tLRPC$TL_attachMenuBot.short_name, null, 1, 0, false, null, false, str, null, 2));
        createBotViewer.show();
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

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    if (LaunchActivity.this.layersActionBarLayout != null) {
                        LaunchActivity.this.layersActionBarLayout.parentDraw(this, canvas);
                    }
                    super.dispatchDraw(canvas);
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
            ActionBarLayout actionBarLayout = new ActionBarLayout(this, false);
            this.rightActionBarLayout = actionBarLayout;
            actionBarLayout.setFragmentStack(rightFragmentsStack);
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
            this.shadowTablet.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda19
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LaunchActivity.lambda$setupActionBarLayout$11(view2);
                }
            });
            ActionBarLayout actionBarLayout2 = new ActionBarLayout(this, true);
            this.layersActionBarLayout = actionBarLayout2;
            actionBarLayout2.setRemoveActionBarExtraHeight(true);
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
                        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack(actionBarLayout.getFragmentStack().get(0));
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
                            boolean z2 = DialogObject.getEmojiStatusDocumentId(currentUser.emoji_status) != 0;
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
            selectAnimatedEmojiDialog.setExpireDateHint(DialogObject.getEmojiStatusUntil(user.emoji_status));
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

    public BottomSheetTabsOverlay getBottomSheetTabsOverlay() {
        return this.bottomSheetTabsOverlay;
    }

    private void openSettings(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
        if (z) {
            bundle.putBoolean("expandPhoto", true);
        }
        lambda$runLinkRequest$88(new ProfileActivity(bundle));
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
        switchToAccount(i, z, new GenericProvider() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda108
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                DialogsActivity lambda$switchToAccount$12;
                lambda$switchToAccount$12 = LaunchActivity.lambda$switchToAccount$12((Void) obj);
                return lambda$switchToAccount$12;
            }
        });
    }

    public void switchToAccount(int i, boolean z, GenericProvider<Void, DialogsActivity> genericProvider) {
        if (i == UserConfig.selectedAccount || !UserConfig.isValidAccount(i)) {
            return;
        }
        this.switchingAccount = true;
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
        this.actionBarLayout.addFragmentToStack(provide, -3);
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
        this.switchingAccount = false;
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
        lambda$runLinkRequest$88(new IntroActivity().setOnLogout());
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
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.openBoostForUsersDialog);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
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
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.openBoostForUsersDialog);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
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
                    PasscodeViewDialog passcodeViewDialog = this.passcodeDialog;
                    if (passcodeViewDialog == null || passcodeViewDialog.passcodeView.getVisibility() != 0) {
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
                PasscodeViewDialog passcodeViewDialog2 = this.passcodeDialog;
                if (passcodeViewDialog2 == null || passcodeViewDialog2.passcodeView.getVisibility() != 0) {
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
    /* loaded from: classes4.dex */
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
        if (this.passcodeDialog == null) {
            this.passcodeDialog = new PasscodeViewDialog(this);
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
        this.passcodeDialog.show();
        this.passcodeDialog.passcodeView.onShow(this.overlayPasscodeViews.isEmpty() && z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
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
        PasscodeView.PasscodeViewDelegate passcodeViewDelegate = new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda150
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword(PasscodeView passcodeView) {
                LaunchActivity.this.lambda$showPasscodeActivity$14(passcodeView);
            }
        };
        this.passcodeDialog.passcodeView.setDelegate(passcodeViewDelegate);
        for (PasscodeView passcodeView : this.overlayPasscodeViews) {
            passcodeView.setDelegate(passcodeViewDelegate);
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
            ActionBarLayout actionBarLayout = this.layersActionBarLayout;
            if (actionBarLayout != null && actionBarLayout.getView() != null && this.layersActionBarLayout.getView().getVisibility() == 0) {
                this.layersActionBarLayout.getView().setVisibility(4);
            }
            ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
            if (actionBarLayout2 != null && actionBarLayout2.getView() != null) {
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
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, null, false);
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
        PasscodeViewDialog passcodeViewDialog;
        if (!this.overlayPasscodeViews.isEmpty() || (passcodeViewDialog = this.passcodeDialog) == null) {
            List<PasscodeView> list = this.overlayPasscodeViews;
            if (list.get(list.size() - 1) != passcodeView) {
                return false;
            }
        } else if (passcodeView != passcodeViewDialog.passcodeView) {
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
    /* JADX WARN: Code restructure failed: missing block: B:1220:0x2750, code lost:
        if (r2.checkCanOpenChat(r0, r3.get(r3.size() - r11)) != false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1237:0x27d3, code lost:
        if (r2.checkCanOpenChat(r0, r3.get(r3.size() - r11)) != false) goto L152;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x034b, code lost:
        if (r99.sendingText == null) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0a88, code lost:
        if (r4.longValue() == 0) goto L807;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x015d, code lost:
        if (r3.equals(r0) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:944:0x1d23, code lost:
        if (r2.longValue() == 0) goto L1294;
     */
    /* JADX WARN: Multi-variable search skipped. Vars limit reached: 5054 (expected less than 5000) */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:1018:0x214c  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:1087:0x2269  */
    /* JADX WARN: Removed duplicated region for block: B:1088:0x226d A[Catch: all -> 0x227c, TRY_LEAVE, TryCatch #12 {all -> 0x227c, blocks: (B:1085:0x2257, B:1088:0x226d), top: B:1506:0x2257 }] */
    /* JADX WARN: Removed duplicated region for block: B:1127:0x236e  */
    /* JADX WARN: Removed duplicated region for block: B:1128:0x2380  */
    /* JADX WARN: Removed duplicated region for block: B:1190:0x26b4  */
    /* JADX WARN: Removed duplicated region for block: B:1203:0x26e1  */
    /* JADX WARN: Removed duplicated region for block: B:1205:0x26fc  */
    /* JADX WARN: Removed duplicated region for block: B:1344:0x29f0  */
    /* JADX WARN: Removed duplicated region for block: B:1345:0x2a01  */
    /* JADX WARN: Removed duplicated region for block: B:1348:0x2a0f  */
    /* JADX WARN: Removed duplicated region for block: B:1349:0x2a20  */
    /* JADX WARN: Removed duplicated region for block: B:1429:0x2cd7  */
    /* JADX WARN: Removed duplicated region for block: B:1433:0x2cdf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1436:0x2ce7  */
    /* JADX WARN: Removed duplicated region for block: B:1447:0x2d38  */
    /* JADX WARN: Removed duplicated region for block: B:1457:0x2d7e  */
    /* JADX WARN: Removed duplicated region for block: B:1461:0x2d95  */
    /* JADX WARN: Removed duplicated region for block: B:1463:0x2d9d  */
    /* JADX WARN: Removed duplicated region for block: B:1498:0x2296 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1535:0x1866 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0352  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0415  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0542  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0774  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x0ca9  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x0cb0  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x0d21  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x0d30  */
    /* JADX WARN: Removed duplicated region for block: B:537:0x0d3f  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0d42  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0d57 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0d8b  */
    /* JADX WARN: Removed duplicated region for block: B:552:0x0db8  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:900:0x1bb0  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:937:0x1c91 A[Catch: Exception -> 0x1ca0, TRY_LEAVE, TryCatch #6 {Exception -> 0x1ca0, blocks: (B:935:0x1c85, B:937:0x1c91), top: B:1493:0x1c85 }] */
    /* JADX WARN: Removed duplicated region for block: B:939:0x1c9e  */
    /* JADX WARN: Type inference failed for: r0v20, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v23, types: [org.telegram.ui.ActionBar.INavigationLayout, org.telegram.ui.ActionBar.ActionBarLayout] */
    /* JADX WARN: Type inference failed for: r0v25, types: [org.telegram.ui.ActionBar.INavigationLayout, org.telegram.ui.ActionBar.ActionBarLayout] */
    /* JADX WARN: Type inference failed for: r0v26, types: [org.telegram.ui.ActionBar.INavigationLayout, org.telegram.ui.ActionBar.ActionBarLayout] */
    /* JADX WARN: Type inference failed for: r0v34, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r100v0, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r11v11 */
    /* JADX WARN: Type inference failed for: r11v14 */
    /* JADX WARN: Type inference failed for: r11v15 */
    /* JADX WARN: Type inference failed for: r11v31 */
    /* JADX WARN: Type inference failed for: r11v32 */
    /* JADX WARN: Type inference failed for: r11v33 */
    /* JADX WARN: Type inference failed for: r11v34 */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v5, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r11v7 */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v193 */
    /* JADX WARN: Type inference failed for: r7v209 */
    /* JADX WARN: Type inference failed for: r7v210 */
    /* JADX WARN: Type inference failed for: r7v3, types: [android.os.Bundle, java.lang.String] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3, Browser.Progress progress, boolean z4) {
        int[] iArr;
        long j;
        String str;
        final LaunchActivity launchActivity;
        final int[] iArr2;
        long j2;
        long j3;
        long j4;
        long j5;
        int i;
        String str2;
        long[] jArr;
        String str3;
        int i2;
        int i3;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15;
        boolean z16;
        String str4;
        String str5;
        int i4;
        int i5;
        boolean z17;
        int i6;
        String str6;
        boolean z18;
        int i7;
        ?? r11;
        ?? r7;
        boolean z19;
        boolean z20;
        GroupCallActivity groupCallActivity;
        int i8;
        boolean z21;
        int i9;
        String str7;
        BaseFragment lastFragment;
        boolean z22;
        int i10;
        BaseFragment openSettings;
        final ActionIntroActivity actionIntroActivity;
        final boolean z23;
        BaseFragment filtersSetupActivity;
        boolean z24;
        boolean z25;
        String str8;
        boolean z26;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        boolean z27;
        Pattern compile;
        int i11;
        int i12;
        int i13;
        long[] jArr2;
        int i14;
        boolean z28;
        int i15;
        long j6;
        int[] iArr3;
        int i16;
        int i17;
        boolean z29;
        boolean z30;
        String str9;
        String str10;
        String str11;
        String str12;
        long j7;
        long j8;
        long j9;
        String str13;
        String str14;
        final String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        boolean z31;
        Long l;
        Integer num;
        Integer num2;
        Long l2;
        String str26;
        HashMap<String, String> hashMap;
        String str27;
        String str28;
        String str29;
        String str30;
        int i18;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        int i19;
        String str36;
        boolean z32;
        String str37;
        String str38;
        String str39;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        String str40;
        int i20;
        boolean z33;
        String str41;
        final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Cursor query;
        Throwable th;
        String string;
        boolean z34;
        int i21;
        char c;
        long j10;
        boolean z35;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z36;
        int i22;
        String str42;
        long j11;
        long j12;
        int parseInt;
        boolean z37;
        long j13;
        boolean z38;
        boolean z39;
        boolean z40;
        String str43;
        String str44;
        boolean z41;
        String str45;
        boolean z42;
        String str46;
        String str47;
        String str48;
        String str49;
        String str50;
        boolean z43;
        int i23;
        String str51;
        String str52;
        String str53;
        String str54;
        int i24;
        String str55;
        String str56;
        String str57;
        String str58;
        String str59;
        String str60;
        String str61;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper2;
        String str62;
        String str63;
        String str64;
        String str65;
        Integer num3;
        String str66;
        Long l3;
        Long l4;
        Integer num4;
        int i25;
        String str67;
        String str68;
        Long parseLong;
        int i26;
        String str69;
        int i27;
        String str70;
        String str71;
        Integer num5;
        String str72;
        Long parseLong2;
        boolean z44;
        Integer num6;
        Integer parseInt2;
        String substring;
        Long l5;
        Integer num7;
        Long l6;
        Long l7;
        long j14;
        String str73;
        boolean z45;
        String[] split2;
        String substring2;
        long j15;
        String type2;
        String stringExtra;
        Parcelable parcelableExtra;
        boolean z46;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str74;
        String str75;
        if (GiftInfoBottomSheet.handleIntent(intent, progress) || UserSelectorBottomSheet.handleIntent(intent, progress) || AndroidUtilities.handleProxyIntent(this, intent)) {
            return true;
        }
        if (intent == 0 || !"android.intent.action.MAIN".equals(intent.getAction())) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            }
            StoryRecorder.destroyInstance();
        }
        int flags = intent.getFlags();
        String action = intent.getAction();
        int[] iArr4 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(iArr4[0], true);
        boolean z47 = action != null && action.equals("voip");
        if (z3 || !(AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            iArr = iArr4;
        } else {
            iArr = iArr4;
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z47) {
                this.passcodeSaveIntent = intent;
                this.passcodeSaveIntentIsNew = z;
                this.passcodeSaveIntentIsRestore = z2;
                return false;
            }
        }
        boolean booleanExtra = intent.getBooleanExtra("force_not_internal_apps", false);
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
        long j16 = 0;
        if ((1048576 & flags) == 0 && intent.getAction() != null && !z2) {
            str8 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j15 = intent.getExtras().getLong("dialogId", 0L);
                    if (j15 == 0) {
                        try {
                            String string2 = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string2 != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                for (int i28 = 0; i28 < size; i28++) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i28);
                                    if (string2.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j17 = extras.getLong("dialogId", 0L);
                                        try {
                                            str74 = extras.getString("hash", null);
                                            j15 = j17;
                                            break;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            j15 = j17;
                                            FileLog.e(th);
                                            str74 = null;
                                            str75 = SharedConfig.directShareHash;
                                            if (str75 != null) {
                                            }
                                            j15 = 0;
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
                        str74 = null;
                    } else {
                        str74 = intent.getExtras().getString("hash", null);
                    }
                    str75 = SharedConfig.directShareHash;
                    if (str75 != null) {
                    }
                }
                j15 = 0;
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
                    z46 = true;
                    if (z46) {
                    }
                    j3 = 0;
                    j5 = 0;
                    str2 = null;
                    jArr = null;
                    str3 = null;
                    str4 = null;
                    str5 = null;
                    str = " ";
                    launchActivity = this;
                    j4 = j15;
                    i = -1;
                    i2 = -1;
                    z5 = false;
                    z6 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z10 = false;
                    z11 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    i4 = 0;
                    i5 = 0;
                    z17 = false;
                    i6 = 0;
                    j2 = 0;
                    iArr2 = iArr;
                    i3 = -1;
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
                        boolean z48 = parcelableExtra instanceof Uri;
                        Uri uri2 = parcelableExtra;
                        if (!z48) {
                            uri2 = Uri.parse(parcelableExtra.toString());
                        }
                        Uri uri3 = (Uri) uri2;
                        boolean z49 = uri3 != null && AndroidUtilities.isInternalUri(uri3);
                        if (!z49 && uri3 != null) {
                            if ((type2 != null && type2.startsWith("image/")) || uri3.toString().toLowerCase().endsWith(".jpg")) {
                                if (this.photoPathsArray == null) {
                                    this.photoPathsArray = new ArrayList<>();
                                }
                                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                                sendingMediaInfo.uri = uri3;
                                this.photoPathsArray.add(sendingMediaInfo);
                            } else {
                                String uri4 = uri3.toString();
                                if (j15 == 0 && uri4 != null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("export path = " + uri4);
                                    }
                                    Set<String> set = MessagesController.getInstance(iArr[0]).exportUri;
                                    String fixFileName = FileLoader.fixFileName(MediaController.getFileName(uri3));
                                    for (String str76 : set) {
                                        try {
                                            compile2 = Pattern.compile(str76);
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
                        z46 = z49;
                        if (z46) {
                            Toast.makeText(this, "Unsupported content", 0).show();
                        }
                        j3 = 0;
                        j5 = 0;
                        str2 = null;
                        jArr = null;
                        str3 = null;
                        str4 = null;
                        str5 = null;
                        str = " ";
                        launchActivity = this;
                        j4 = j15;
                        i = -1;
                        i2 = -1;
                        z5 = false;
                        z6 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z10 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        i4 = 0;
                        i5 = 0;
                        z17 = false;
                        i6 = 0;
                        j2 = 0;
                        iArr2 = iArr;
                        i3 = -1;
                    }
                }
                z46 = false;
                if (z46) {
                }
                j3 = 0;
                j5 = 0;
                str2 = null;
                jArr = null;
                str3 = null;
                str4 = null;
                str5 = null;
                str = " ";
                launchActivity = this;
                j4 = j15;
                i = -1;
                i2 = -1;
                z5 = false;
                z6 = false;
                z9 = false;
                z8 = false;
                z7 = false;
                z10 = false;
                z11 = false;
                z12 = false;
                z13 = false;
                z14 = false;
                z15 = false;
                z16 = false;
                i4 = 0;
                i5 = 0;
                z17 = false;
                i6 = 0;
                j2 = 0;
                iArr2 = iArr;
                i3 = -1;
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
                j = 0;
                str = " ";
                launchActivity = this;
            } else if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                try {
                    parcelableArrayListExtra = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                    type = intent.getType();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                if (parcelableArrayListExtra != null) {
                    int i29 = 0;
                    while (i29 < parcelableArrayListExtra.size()) {
                        Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i29);
                        boolean z50 = parcelable instanceof Uri;
                        Uri uri5 = parcelable;
                        if (!z50) {
                            uri5 = Uri.parse(parcelable.toString());
                        }
                        Uri uri6 = (Uri) uri5;
                        if (uri6 != null && AndroidUtilities.isInternalUri(uri6)) {
                            parcelableArrayListExtra.remove(i29);
                            i29--;
                        }
                        i29++;
                    }
                    if (parcelableArrayListExtra.isEmpty()) {
                        arrayList = null;
                        if (arrayList != null) {
                            if (type != null && type.startsWith("image/")) {
                                for (int i30 = 0; i30 < arrayList.size(); i30++) {
                                    Parcelable parcelable2 = (Parcelable) arrayList.get(i30);
                                    boolean z51 = parcelable2 instanceof Uri;
                                    Uri uri7 = parcelable2;
                                    if (!z51) {
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
                                Set<String> set2 = MessagesController.getInstance(iArr[0]).exportUri;
                                for (int i31 = 0; i31 < arrayList.size(); i31++) {
                                    Object obj = (Parcelable) arrayList.get(i31);
                                    if (!(obj instanceof Uri)) {
                                        obj = Uri.parse(obj.toString());
                                    }
                                    Uri uri9 = (Uri) obj;
                                    String path2 = AndroidUtilities.getPath(uri9);
                                    String obj2 = obj.toString();
                                    String str77 = obj2 == null ? path2 : obj2;
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("export path = " + str77);
                                    }
                                    if (str77 != null && this.exportingChatUri == null) {
                                        String fixFileName2 = FileLoader.fixFileName(MediaController.getFileName(uri9));
                                        for (String str78 : set2) {
                                            try {
                                                compile = Pattern.compile(str78);
                                            } catch (Exception e4) {
                                                FileLog.e(e4);
                                            }
                                            if (compile.matcher(str77).find() || compile.matcher(fixFileName2).find()) {
                                                this.exportingChatUri = uri9;
                                                z27 = true;
                                                break;
                                            }
                                        }
                                        z27 = false;
                                        if (!z27) {
                                            if (str77.startsWith("content://com.kakao.talk") && str77.endsWith("KakaoTalkChats.txt")) {
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
                                        this.documentsOriginalPathsArray.add(str77);
                                    } else {
                                        if (this.documentsUrisArray == null) {
                                            this.documentsUrisArray = new ArrayList<>();
                                        }
                                        this.documentsUrisArray.add(uri9);
                                        this.documentsMimeType = type;
                                    }
                                }
                            }
                            z26 = false;
                            if (z26) {
                                Toast.makeText(this, "Unsupported content", 0).show();
                            }
                            str = " ";
                            launchActivity = this;
                            j = 0;
                        }
                        z26 = true;
                        if (z26) {
                        }
                        str = " ";
                        launchActivity = this;
                        j = 0;
                    }
                }
                arrayList = parcelableArrayListExtra;
                if (arrayList != null) {
                }
                z26 = true;
                if (z26) {
                }
                str = " ";
                launchActivity = this;
                j = 0;
            } else if ("android.intent.action.VIEW".equals(intent.getAction())) {
                Uri data = intent.getData();
                if (data != null) {
                    String scheme = data.getScheme();
                    if (scheme != null) {
                        switch (scheme.hashCode()) {
                            case 3699:
                                if (scheme.equals("tg")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3213448:
                                if (scheme.equals("http")) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 99617003:
                                if (scheme.equals("https")) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                final String uri10 = data.toString();
                                if (uri10.startsWith("tg:premium_offer") || uri10.startsWith("tg://premium_offer")) {
                                    j8 = 0;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.lambda$handleIntent$15(uri10);
                                        }
                                    });
                                    j9 = j8;
                                    j7 = j9;
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
                                    z31 = false;
                                    l = null;
                                    num = null;
                                    num2 = null;
                                    l2 = null;
                                    str26 = null;
                                    hashMap = null;
                                    str27 = null;
                                    str28 = null;
                                    str29 = null;
                                    str30 = null;
                                    i17 = 0;
                                    i18 = 0;
                                    z12 = false;
                                    z29 = false;
                                    z30 = false;
                                    z13 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    str9 = null;
                                    str10 = null;
                                    str11 = null;
                                    str12 = null;
                                    str40 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str39 = null;
                                    str38 = null;
                                    str37 = null;
                                    z32 = false;
                                    str36 = null;
                                    i19 = -1;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    str32 = null;
                                    str31 = null;
                                    i20 = 0;
                                    z33 = false;
                                    str41 = null;
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
                                        str32 = queryParameter4;
                                        str31 = queryParameter5;
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
                                        z31 = false;
                                        l = null;
                                        num = null;
                                        num2 = null;
                                        l2 = null;
                                        str26 = null;
                                        str27 = null;
                                        str28 = null;
                                        str29 = null;
                                        str30 = null;
                                        i17 = 0;
                                        i18 = 0;
                                        z12 = false;
                                        z29 = false;
                                        z30 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        z16 = false;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str12 = null;
                                        j9 = 0;
                                        j7 = 0;
                                        str40 = null;
                                        tLRPC$TL_wallPaper = null;
                                        str39 = null;
                                        str38 = null;
                                        str37 = null;
                                        z32 = false;
                                        str36 = null;
                                        i19 = -1;
                                        str35 = null;
                                        str34 = null;
                                        str33 = null;
                                        i20 = 0;
                                        z33 = false;
                                        str41 = null;
                                    } else {
                                        String queryParameter7 = parse.getQueryParameter("start");
                                        str20 = parse.getQueryParameter("startgroup");
                                        String queryParameter8 = parse.getQueryParameter("startchannel");
                                        str21 = queryParameter3;
                                        String queryParameter9 = parse.getQueryParameter("admin");
                                        String queryParameter10 = parse.getQueryParameter("game");
                                        String queryParameter11 = parse.getQueryParameter("voicechat");
                                        boolean booleanQueryParameter = parse.getBooleanQueryParameter("videochat", false);
                                        String queryParameter12 = parse.getQueryParameter("livestream");
                                        String queryParameter13 = parse.getQueryParameter("startattach");
                                        String queryParameter14 = parse.getQueryParameter("choose");
                                        String queryParameter15 = parse.getQueryParameter("attach");
                                        Integer parseInt3 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                        int intValue = Utilities.parseInt((CharSequence) parse.getQueryParameter("story")).intValue();
                                        num2 = parseInt3.intValue() == 0 ? null : parseInt3;
                                        Long parseLong3 = Utilities.parseLong(parse.getQueryParameter("thread"));
                                        if (parseLong3.longValue() == 0) {
                                            parseLong3 = null;
                                        }
                                        if (parseLong3 == null) {
                                            parseLong3 = Utilities.parseLong(parse.getQueryParameter("topic"));
                                            if (parseLong3.longValue() == 0) {
                                                parseLong3 = null;
                                            }
                                        }
                                        String queryParameter16 = parse.getQueryParameter("text");
                                        l2 = parseLong3;
                                        Integer parseInt4 = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                        i20 = intValue;
                                        str31 = queryParameter5;
                                        j9 = 0;
                                        j7 = 0;
                                        str26 = queryParameter10;
                                        str37 = queryParameter11;
                                        str32 = queryParameter4;
                                        z32 = booleanQueryParameter;
                                        str35 = queryParameter13;
                                        str33 = queryParameter14;
                                        str34 = queryParameter15;
                                        str36 = queryParameter12;
                                        if (parseInt4.intValue() == 0) {
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str18 = null;
                                            str23 = null;
                                            z31 = false;
                                            l = null;
                                            num = null;
                                            hashMap = null;
                                            str27 = null;
                                            str28 = null;
                                            str29 = null;
                                            str30 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z30 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            str40 = null;
                                            tLRPC$TL_wallPaper = null;
                                            str39 = null;
                                            str38 = null;
                                            i19 = -1;
                                            z33 = false;
                                            str41 = null;
                                            str25 = queryParameter9;
                                            str22 = queryParameter7;
                                            str24 = queryParameter16;
                                        } else {
                                            str14 = null;
                                            str15 = null;
                                            str18 = null;
                                            str23 = null;
                                            z31 = false;
                                            l = null;
                                            hashMap = null;
                                            str27 = null;
                                            str28 = null;
                                            str29 = null;
                                            str30 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z30 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            str40 = null;
                                            tLRPC$TL_wallPaper = null;
                                            str39 = null;
                                            str38 = null;
                                            i19 = -1;
                                            z33 = false;
                                            str41 = null;
                                            str25 = queryParameter9;
                                            num = parseInt4;
                                            str22 = queryParameter7;
                                            str24 = queryParameter16;
                                            str13 = null;
                                        }
                                        str16 = null;
                                        str19 = queryParameter8;
                                        str17 = null;
                                        break;
                                    }
                                } else if (uri10.startsWith("tg:invoice") || uri10.startsWith("tg://invoice")) {
                                    str39 = Uri.parse(uri10.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
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
                                    z31 = false;
                                    l = null;
                                    num = null;
                                    num2 = null;
                                    l2 = null;
                                    str26 = null;
                                    hashMap = null;
                                    str27 = null;
                                    str28 = null;
                                    str29 = null;
                                    str30 = null;
                                    i17 = 0;
                                    i18 = 0;
                                    z12 = false;
                                    z29 = false;
                                    z30 = false;
                                    z13 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    str9 = null;
                                    str10 = null;
                                    str11 = null;
                                    str12 = null;
                                    j9 = 0;
                                    j7 = 0;
                                    str40 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str38 = null;
                                    str37 = null;
                                    z32 = false;
                                    str36 = null;
                                    i19 = -1;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    str32 = null;
                                    str31 = null;
                                    i20 = 0;
                                    z33 = false;
                                    str41 = null;
                                } else {
                                    if (uri10.startsWith("tg:contact") || uri10.startsWith("tg://contact")) {
                                        str30 = Uri.parse(uri10.replace("tg:contact", "tg://contact")).getQueryParameter("token");
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
                                        z31 = false;
                                        l = null;
                                        num = null;
                                        num2 = null;
                                        l2 = null;
                                        str26 = null;
                                        hashMap = null;
                                        str27 = null;
                                        str28 = null;
                                        str29 = null;
                                    } else {
                                        if (uri10.startsWith("tg:privatepost") || uri10.startsWith("tg://privatepost")) {
                                            Uri parse2 = Uri.parse(uri10.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                            Integer parseInt5 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("post"));
                                            Long parseLong4 = Utilities.parseLong(parse2.getQueryParameter("channel"));
                                            if (parseInt5.intValue() != 0) {
                                                j10 = 0;
                                                break;
                                            } else {
                                                j10 = 0;
                                            }
                                            parseLong4 = null;
                                            parseInt5 = null;
                                            Long parseLong5 = Utilities.parseLong(parse2.getQueryParameter("thread"));
                                            if (parseLong5.longValue() == j10) {
                                                parseLong5 = null;
                                            }
                                            if (parseLong5 == null) {
                                                parseLong5 = Utilities.parseLong(parse2.getQueryParameter("topic"));
                                                if (parseLong5.longValue() == j10) {
                                                    parseLong5 = null;
                                                }
                                            }
                                            Integer parseInt6 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("comment"));
                                            if (parseInt6.intValue() == 0) {
                                                l = parseLong4;
                                                num2 = parseInt5;
                                                l2 = parseLong5;
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
                                                z31 = false;
                                                num = null;
                                            } else {
                                                num = parseInt6;
                                                l = parseLong4;
                                                num2 = parseInt5;
                                                l2 = parseLong5;
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
                                                z31 = false;
                                            }
                                        } else if (uri10.startsWith("tg:bg") || uri10.startsWith("tg://bg")) {
                                            Uri parse3 = Uri.parse(uri10.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = new TLRPC$TL_wallPaper();
                                            tLRPC$TL_wallPaper3.settings = new TLRPC$TL_wallPaperSettings();
                                            String queryParameter17 = parse3.getQueryParameter("slug");
                                            tLRPC$TL_wallPaper3.slug = queryParameter17;
                                            if (queryParameter17 == null) {
                                                tLRPC$TL_wallPaper3.slug = parse3.getQueryParameter("color");
                                            }
                                            String str79 = tLRPC$TL_wallPaper3.slug;
                                            if (str79 != null && str79.length() == 6) {
                                                tLRPC$TL_wallPaper3.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper3.slug, 16) | (-16777216);
                                                tLRPC$TL_wallPaper3.slug = null;
                                                z35 = true;
                                            } else {
                                                String str80 = tLRPC$TL_wallPaper3.slug;
                                                if (str80 != null && str80.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper3.slug.charAt(6))) {
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
                                                            String queryParameter18 = parse3.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter18)) {
                                                                tLRPC$TL_wallPaper3.settings.rotation = Utilities.parseInt((CharSequence) queryParameter18).intValue();
                                                            }
                                                        } catch (Exception unused) {
                                                        }
                                                        try {
                                                            tLRPC$TL_wallPaper3.slug = null;
                                                            z35 = true;
                                                        } catch (Exception unused2) {
                                                        }
                                                    } catch (Exception unused3) {
                                                    }
                                                }
                                                z35 = false;
                                                if (!z35) {
                                                    String queryParameter19 = parse3.getQueryParameter("mode");
                                                    if (queryParameter19 != null && (split = queryParameter19.toLowerCase().split(" ")) != null && split.length > 0) {
                                                        for (int i32 = 0; i32 < split.length; i32++) {
                                                            if ("blur".equals(split[i32])) {
                                                                tLRPC$TL_wallPaper3.settings.blur = true;
                                                            } else if ("motion".equals(split[i32])) {
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            }
                                            if (!z35) {
                                            }
                                            tLRPC$TL_wallPaper = tLRPC$TL_wallPaper3;
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
                                            z31 = false;
                                            l = null;
                                            num = null;
                                            num2 = null;
                                            l2 = null;
                                            str26 = null;
                                            hashMap = null;
                                            str27 = null;
                                            str28 = null;
                                            str29 = null;
                                            str30 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z30 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            j9 = 0;
                                            j7 = 0;
                                            str40 = null;
                                            str39 = null;
                                            str38 = null;
                                            str37 = null;
                                            z32 = false;
                                            str36 = null;
                                            i19 = -1;
                                            str35 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            i20 = 0;
                                            z33 = false;
                                            str41 = null;
                                        } else {
                                            if (uri10.startsWith("tg:join") || uri10.startsWith("tg://join")) {
                                                str13 = Uri.parse(uri10.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                            } else if (uri10.startsWith("tg:addstickers") || uri10.startsWith("tg://addstickers")) {
                                                str18 = Uri.parse(uri10.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                            } else if (uri10.startsWith("tg:addemoji") || uri10.startsWith("tg://addemoji")) {
                                                str17 = Uri.parse(uri10.replace("tg:addemoji", "tg://telegram.org").replace("tg://addemoji", "tg://telegram.org")).getQueryParameter("set");
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                            } else if (uri10.startsWith("tg:msg") || uri10.startsWith("tg://msg") || uri10.startsWith("tg://share") || uri10.startsWith("tg:share")) {
                                                Uri parse4 = Uri.parse(uri10.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                                String queryParameter20 = parse4.getQueryParameter("url");
                                                str8 = queryParameter20 != null ? queryParameter20 : "";
                                                if (parse4.getQueryParameter("text") != null) {
                                                    if (str8.length() > 0) {
                                                        str8 = str8 + "\n";
                                                        z36 = true;
                                                    } else {
                                                        z36 = false;
                                                    }
                                                    str8 = str8 + parse4.getQueryParameter("text");
                                                } else {
                                                    z36 = false;
                                                }
                                                if (str8.length() > 16384) {
                                                    i22 = 0;
                                                    str42 = str8.substring(0, LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                                                } else {
                                                    i22 = 0;
                                                    str42 = str8;
                                                }
                                                while (str42.endsWith("\n")) {
                                                    str42 = str42.substring(i22, str42.length() - 1);
                                                }
                                                str16 = str42;
                                                z31 = z36;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str17 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                str25 = null;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                            } else if (uri10.startsWith("tg:confirmphone") || uri10.startsWith("tg://confirmphone")) {
                                                Uri parse5 = Uri.parse(uri10.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                                str15 = parse5.getQueryParameter("phone");
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                                str14 = parse5.getQueryParameter("hash");
                                                str13 = null;
                                                break;
                                            } else if (uri10.startsWith("tg:login") || uri10.startsWith("tg://login")) {
                                                Uri parse6 = Uri.parse(uri10.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                                String queryParameter21 = parse6.getQueryParameter("token");
                                                int intValue2 = Utilities.parseInt((CharSequence) parse6.getQueryParameter("code")).intValue();
                                                str29 = intValue2 != 0 ? "" + intValue2 : null;
                                                str40 = queryParameter21;
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:openmessage") || uri10.startsWith("tg://openmessage")) {
                                                Uri parse7 = Uri.parse(uri10.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                                String queryParameter22 = parse7.getQueryParameter("user_id");
                                                String queryParameter23 = parse7.getQueryParameter("chat_id");
                                                String queryParameter24 = parse7.getQueryParameter("message_id");
                                                if (queryParameter22 != null) {
                                                    j11 = Long.parseLong(queryParameter22);
                                                    j12 = 0;
                                                    if (queryParameter24 != null) {
                                                        try {
                                                            parseInt = Integer.parseInt(queryParameter24);
                                                        } catch (NumberFormatException unused7) {
                                                        }
                                                        i17 = parseInt;
                                                        j9 = j11;
                                                        j7 = j12;
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
                                                        z31 = false;
                                                        l = null;
                                                        num = null;
                                                        num2 = null;
                                                        l2 = null;
                                                        str26 = null;
                                                        hashMap = null;
                                                        str27 = null;
                                                        str28 = null;
                                                        str29 = null;
                                                        str30 = null;
                                                        i18 = 0;
                                                        z12 = false;
                                                        z29 = false;
                                                        z30 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        z16 = false;
                                                        str9 = null;
                                                        str10 = null;
                                                        str11 = null;
                                                        str12 = null;
                                                        str40 = null;
                                                        tLRPC$TL_wallPaper = null;
                                                        str39 = null;
                                                        str38 = null;
                                                        str37 = null;
                                                        z32 = false;
                                                        str36 = null;
                                                        i19 = -1;
                                                        str35 = null;
                                                        str34 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        i20 = 0;
                                                        z33 = false;
                                                        str41 = null;
                                                    }
                                                    parseInt = 0;
                                                    i17 = parseInt;
                                                    j9 = j11;
                                                    j7 = j12;
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i18 = 0;
                                                    z12 = false;
                                                    z29 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str40 = null;
                                                    tLRPC$TL_wallPaper = null;
                                                    str39 = null;
                                                    str38 = null;
                                                    str37 = null;
                                                    z32 = false;
                                                    str36 = null;
                                                    i19 = -1;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    i20 = 0;
                                                    z33 = false;
                                                    str41 = null;
                                                } else {
                                                    if (queryParameter23 != null) {
                                                        j12 = Long.parseLong(queryParameter23);
                                                        j11 = 0;
                                                        if (queryParameter24 != null) {
                                                        }
                                                        parseInt = 0;
                                                        i17 = parseInt;
                                                        j9 = j11;
                                                        j7 = j12;
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
                                                        z31 = false;
                                                        l = null;
                                                        num = null;
                                                        num2 = null;
                                                        l2 = null;
                                                        str26 = null;
                                                        hashMap = null;
                                                        str27 = null;
                                                        str28 = null;
                                                        str29 = null;
                                                        str30 = null;
                                                        i18 = 0;
                                                        z12 = false;
                                                        z29 = false;
                                                        z30 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        z16 = false;
                                                        str9 = null;
                                                        str10 = null;
                                                        str11 = null;
                                                        str12 = null;
                                                        str40 = null;
                                                        tLRPC$TL_wallPaper = null;
                                                        str39 = null;
                                                        str38 = null;
                                                        str37 = null;
                                                        z32 = false;
                                                        str36 = null;
                                                        i19 = -1;
                                                        str35 = null;
                                                        str34 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        i20 = 0;
                                                        z33 = false;
                                                        str41 = null;
                                                    }
                                                    j11 = 0;
                                                    j12 = 0;
                                                    if (queryParameter24 != null) {
                                                    }
                                                    parseInt = 0;
                                                    i17 = parseInt;
                                                    j9 = j11;
                                                    j7 = j12;
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i18 = 0;
                                                    z12 = false;
                                                    z29 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    z16 = false;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str12 = null;
                                                    str40 = null;
                                                    tLRPC$TL_wallPaper = null;
                                                    str39 = null;
                                                    str38 = null;
                                                    str37 = null;
                                                    z32 = false;
                                                    str36 = null;
                                                    i19 = -1;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    i20 = 0;
                                                    z33 = false;
                                                    str41 = null;
                                                }
                                            } else if (uri10.startsWith("tg:passport") || uri10.startsWith("tg://passport") || uri10.startsWith("tg:secureid")) {
                                                Uri parse8 = Uri.parse(uri10.replace("tg:passport", "tg://telegram.org").replace("tg://passport", "tg://telegram.org").replace("tg:secureid", "tg://telegram.org"));
                                                HashMap<String, String> hashMap3 = new HashMap<>();
                                                String queryParameter25 = parse8.getQueryParameter("scope");
                                                if (!TextUtils.isEmpty(queryParameter25) && queryParameter25.startsWith("{") && queryParameter25.endsWith("}")) {
                                                    hashMap3.put("nonce", parse8.getQueryParameter("nonce"));
                                                } else {
                                                    hashMap3.put("payload", parse8.getQueryParameter("payload"));
                                                }
                                                hashMap3.put("bot_id", parse8.getQueryParameter("bot_id"));
                                                hashMap3.put("scope", queryParameter25);
                                                hashMap3.put("public_key", parse8.getQueryParameter("public_key"));
                                                hashMap3.put("callback_url", parse8.getQueryParameter("callback_url"));
                                                hashMap = hashMap3;
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                            } else if (uri10.startsWith("tg:setlanguage") || uri10.startsWith("tg://setlanguage")) {
                                                str27 = Uri.parse(uri10.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                            } else if (uri10.startsWith("tg:addtheme") || uri10.startsWith("tg://addtheme")) {
                                                str38 = Uri.parse(uri10.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:settings") || uri10.startsWith("tg://settings")) {
                                                if (uri10.contains("themes") || uri10.contains("theme")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 2;
                                                } else if (uri10.contains("devices")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 3;
                                                } else if (uri10.contains("folders")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 4;
                                                } else if (uri10.contains("change_number")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 5;
                                                } else if (uri10.contains("language")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 10;
                                                } else if (uri10.contains("auto_delete")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 11;
                                                } else if (uri10.contains("privacy")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 12;
                                                } else if (uri10.contains("?enablelogs")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 7;
                                                } else if (uri10.contains("?sendlogs")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 8;
                                                } else if (uri10.contains("?disablelogs")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 9;
                                                } else if (uri10.contains("premium_sms")) {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 13;
                                                } else {
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 1;
                                                }
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:search") || uri10.startsWith("tg://search")) {
                                                String queryParameter26 = Uri.parse(uri10.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                                str12 = queryParameter26 != null ? queryParameter26.trim() : "";
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:calllog") || uri10.startsWith("tg://calllog")) {
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = true;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:call") || uri10.startsWith("tg://call")) {
                                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                                    if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                        String queryParameter27 = data.getQueryParameter("format");
                                                        String queryParameter28 = data.getQueryParameter("name");
                                                        String queryParameter29 = data.getQueryParameter("phone");
                                                        z37 = false;
                                                        List<TLRPC$TL_contact> findContacts = findContacts(queryParameter28, queryParameter29, false);
                                                        if (!findContacts.isEmpty() || queryParameter29 == null) {
                                                            j13 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                            if (j13 != 0) {
                                                                str8 = null;
                                                            } else if (queryParameter28 != null) {
                                                                str8 = queryParameter28;
                                                            }
                                                            boolean equalsIgnoreCase = MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(queryParameter27);
                                                            z38 = !equalsIgnoreCase;
                                                            z39 = equalsIgnoreCase;
                                                            z40 = false;
                                                            z37 = true;
                                                            str43 = null;
                                                        } else {
                                                            str44 = queryParameter29;
                                                            str43 = queryParameter28;
                                                            z40 = true;
                                                            z38 = false;
                                                            z39 = false;
                                                            str8 = null;
                                                            j13 = 0;
                                                            z15 = z40;
                                                            z29 = z38;
                                                            z30 = z39;
                                                            z13 = z37;
                                                            str9 = str8;
                                                            str10 = str43;
                                                            j9 = j13;
                                                            str11 = str44;
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
                                                            z31 = false;
                                                            l = null;
                                                            num = null;
                                                            num2 = null;
                                                            l2 = null;
                                                            str26 = null;
                                                            hashMap = null;
                                                            str27 = null;
                                                            str28 = null;
                                                            str29 = null;
                                                            str30 = null;
                                                            i17 = 0;
                                                            i18 = 0;
                                                            z12 = false;
                                                            z14 = false;
                                                            z16 = false;
                                                            str12 = null;
                                                            j7 = 0;
                                                            str40 = null;
                                                            tLRPC$TL_wallPaper = null;
                                                            str39 = null;
                                                            str38 = null;
                                                            str37 = null;
                                                            z32 = false;
                                                            str36 = null;
                                                            i19 = -1;
                                                            str35 = null;
                                                            str34 = null;
                                                            str33 = null;
                                                            str32 = null;
                                                            str31 = null;
                                                            i20 = 0;
                                                            z33 = false;
                                                            str41 = null;
                                                        }
                                                    } else {
                                                        final Intent intent2 = new Intent((Intent) intent);
                                                        intent2.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                        intent2.putExtra("extra_force_call", true);
                                                        ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
                                                            @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                            public final void onResult(boolean z52) {
                                                                LaunchActivity.this.lambda$handleIntent$16(intent2, z52);
                                                            }
                                                        }, 1000L);
                                                        z40 = false;
                                                        z38 = false;
                                                        z39 = false;
                                                        z37 = false;
                                                        str8 = null;
                                                        str43 = null;
                                                        j13 = 0;
                                                    }
                                                    str44 = null;
                                                    z15 = z40;
                                                    z29 = z38;
                                                    z30 = z39;
                                                    z13 = z37;
                                                    str9 = str8;
                                                    str10 = str43;
                                                    j9 = j13;
                                                    str11 = str44;
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
                                                    z31 = false;
                                                    l = null;
                                                    num = null;
                                                    num2 = null;
                                                    l2 = null;
                                                    str26 = null;
                                                    hashMap = null;
                                                    str27 = null;
                                                    str28 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    i17 = 0;
                                                    i18 = 0;
                                                    z12 = false;
                                                    z14 = false;
                                                    z16 = false;
                                                    str12 = null;
                                                    j7 = 0;
                                                    str40 = null;
                                                    tLRPC$TL_wallPaper = null;
                                                    str39 = null;
                                                    str38 = null;
                                                    str37 = null;
                                                    z32 = false;
                                                    str36 = null;
                                                    i19 = -1;
                                                    str35 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    i20 = 0;
                                                    z33 = false;
                                                    str41 = null;
                                                }
                                            } else if (uri10.startsWith("tg:scanqr") || uri10.startsWith("tg://scanqr")) {
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = true;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:addcontact") || uri10.startsWith("tg://addcontact")) {
                                                Uri parse9 = Uri.parse(uri10.replace("tg:addcontact", "tg://telegram.org").replace("tg://addcontact", "tg://telegram.org"));
                                                String queryParameter30 = parse9.getQueryParameter("name");
                                                List<String> queryParameters = parse9.getQueryParameters("phone");
                                                str11 = (queryParameters == null || queryParameters.size() <= 0) ? null : queryParameters.get(0);
                                                str10 = queryParameter30;
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = true;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                str41 = null;
                                            } else if (uri10.startsWith("tg:addlist") || uri10.startsWith("tg://addlist")) {
                                                str23 = Uri.parse(uri10.replace("tg:addlist", "tg://telegram.org").replace("tg://addlist", "tg://telegram.org")).getQueryParameter("slug");
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
                                                str24 = null;
                                                str25 = null;
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                            } else if (uri10.startsWith("tg:message") || uri10.startsWith("tg://message")) {
                                                str41 = Uri.parse(uri10.replace("tg:message", "tg://telegram.org").replace("tg://message", "tg://telegram.org")).getQueryParameter("slug");
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str28 = null;
                                                str29 = null;
                                                str30 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                tLRPC$TL_wallPaper = null;
                                                str39 = null;
                                                str38 = null;
                                                str37 = null;
                                                z32 = false;
                                                str36 = null;
                                                i19 = -1;
                                                str35 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                i20 = 0;
                                                z33 = false;
                                                break;
                                            } else {
                                                String replace = uri10.replace("tg://", "").replace("tg:", "");
                                                int indexOf = replace.indexOf(63);
                                                if (indexOf >= 0) {
                                                    replace = replace.substring(0, indexOf);
                                                }
                                                str28 = replace;
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
                                                z31 = false;
                                                l = null;
                                                num = null;
                                                num2 = null;
                                                l2 = null;
                                                str26 = null;
                                                hashMap = null;
                                                str27 = null;
                                                str29 = null;
                                                str30 = null;
                                            }
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str23 = null;
                                            str24 = null;
                                            str25 = null;
                                            z31 = false;
                                            l = null;
                                            num = null;
                                            num2 = null;
                                            l2 = null;
                                        }
                                        str26 = null;
                                        hashMap = null;
                                        str27 = null;
                                        str28 = null;
                                        str29 = null;
                                        str30 = null;
                                    }
                                    i17 = 0;
                                    i18 = 0;
                                    z12 = false;
                                    z29 = false;
                                    z30 = false;
                                    z13 = false;
                                    z14 = false;
                                    z15 = false;
                                    z16 = false;
                                    str9 = null;
                                    str10 = null;
                                    str11 = null;
                                    str12 = null;
                                    j9 = 0;
                                    j7 = 0;
                                    str40 = null;
                                    tLRPC$TL_wallPaper = null;
                                    str39 = null;
                                    str38 = null;
                                    str37 = null;
                                    z32 = false;
                                    str36 = null;
                                    i19 = -1;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    str32 = null;
                                    str31 = null;
                                    i20 = 0;
                                    z33 = false;
                                    str41 = null;
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
                                    if (path3 != null && path3.length() > 1) {
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
                                                String str81 = tLRPC$TL_wallPaper4.slug;
                                                if (str81 != null && str81.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(6))) {
                                                    tLRPC$TL_wallPaper4.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(0, 6), 16) | (-16777216);
                                                    tLRPC$TL_wallPaper4.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(7, 13), 16) | (-16777216);
                                                    if (tLRPC$TL_wallPaper4.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(13))) {
                                                        tLRPC$TL_wallPaper4.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(14, 20), 16) | (-16777216);
                                                    }
                                                    if (tLRPC$TL_wallPaper4.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper4.slug.charAt(20))) {
                                                        tLRPC$TL_wallPaper4.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper4.slug.substring(21), 16) | (-16777216);
                                                    }
                                                    try {
                                                        String queryParameter31 = data.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter31)) {
                                                            tLRPC$TL_wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) queryParameter31).intValue();
                                                        }
                                                    } catch (Exception unused8) {
                                                    }
                                                    tLRPC$TL_wallPaper4.slug = null;
                                                }
                                                z45 = false;
                                                if (!z45) {
                                                    String queryParameter32 = data.getQueryParameter("mode");
                                                    if (queryParameter32 != null && (split2 = queryParameter32.toLowerCase().split(" ")) != null && split2.length > 0) {
                                                        for (int i33 = 0; i33 < split2.length; i33++) {
                                                            if ("blur".equals(split2[i33])) {
                                                                tLRPC$TL_wallPaper4.settings.blur = true;
                                                            } else if ("motion".equals(split2[i33])) {
                                                                tLRPC$TL_wallPaper4.settings.motion = true;
                                                            }
                                                        }
                                                    }
                                                    String queryParameter33 = data.getQueryParameter("intensity");
                                                    if (!TextUtils.isEmpty(queryParameter33)) {
                                                        tLRPC$TL_wallPaper4.settings.intensity = Utilities.parseInt((CharSequence) queryParameter33).intValue();
                                                    } else {
                                                        tLRPC$TL_wallPaper4.settings.intensity = 50;
                                                    }
                                                    try {
                                                        String queryParameter34 = data.getQueryParameter("bg_color");
                                                        if (!TextUtils.isEmpty(queryParameter34)) {
                                                            tLRPC$TL_wallPaper4.settings.background_color = Integer.parseInt(queryParameter34.substring(0, 6), 16) | (-16777216);
                                                            if (queryParameter34.length() >= 13) {
                                                                tLRPC$TL_wallPaper4.settings.second_background_color = Integer.parseInt(queryParameter34.substring(7, 13), 16) | (-16777216);
                                                                if (queryParameter34.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter34.charAt(13))) {
                                                                    tLRPC$TL_wallPaper4.settings.third_background_color = Integer.parseInt(queryParameter34.substring(14, 20), 16) | (-16777216);
                                                                }
                                                                if (queryParameter34.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter34.charAt(20))) {
                                                                    tLRPC$TL_wallPaper4.settings.fourth_background_color = Integer.parseInt(queryParameter34.substring(21), 16) | (-16777216);
                                                                }
                                                            }
                                                        } else {
                                                            tLRPC$TL_wallPaper4.settings.background_color = -1;
                                                        }
                                                    } catch (Exception unused9) {
                                                    }
                                                    try {
                                                        String queryParameter35 = data.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter35)) {
                                                            tLRPC$TL_wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) queryParameter35).intValue();
                                                        }
                                                    } catch (Exception unused10) {
                                                    }
                                                }
                                                tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper4;
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                                str18 = null;
                                                str47 = null;
                                                str56 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                                break;
                                            }
                                            z45 = true;
                                            if (!z45) {
                                            }
                                            tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper4;
                                            z41 = false;
                                            str45 = null;
                                            z42 = false;
                                            str46 = null;
                                            str18 = null;
                                            str47 = null;
                                            str56 = null;
                                            str55 = null;
                                            i24 = -1;
                                            str22 = null;
                                            str54 = null;
                                            str53 = null;
                                            str25 = null;
                                            str52 = null;
                                            str51 = null;
                                            i23 = 0;
                                            z43 = false;
                                            str50 = null;
                                            str49 = null;
                                            str48 = null;
                                            str57 = null;
                                            str58 = null;
                                            str59 = null;
                                            str29 = null;
                                            str30 = null;
                                            str61 = null;
                                            str60 = null;
                                            str62 = null;
                                            num4 = null;
                                            l4 = null;
                                            l3 = null;
                                            str66 = null;
                                            num3 = null;
                                            str65 = null;
                                            str64 = null;
                                            str63 = null;
                                            z33 = z41;
                                            str13 = str46;
                                            str17 = str47;
                                            str16 = str56;
                                            i19 = i24;
                                            str20 = str54;
                                            str19 = str53;
                                            str37 = str51;
                                            i20 = i23;
                                            z32 = z43;
                                            str38 = str48;
                                            str36 = str57;
                                            str32 = str58;
                                            str31 = str59;
                                            str23 = str61;
                                            str41 = str60;
                                            tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                            str39 = str62;
                                            l = l4;
                                            str24 = str66;
                                            num = num3;
                                            str35 = str65;
                                            str34 = str64;
                                            str33 = str63;
                                            hashMap = null;
                                            str28 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z13 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            j9 = 0;
                                            j7 = 0;
                                            str40 = null;
                                            str21 = str45;
                                            str26 = str52;
                                            str14 = str50;
                                            str27 = str49;
                                            num2 = num4;
                                            l2 = l3;
                                            z30 = false;
                                            z14 = false;
                                            z31 = z42;
                                            str15 = str55;
                                        } else if (substring3.startsWith("login/")) {
                                            int intValue3 = Utilities.parseInt((CharSequence) substring3.replace("login/", "")).intValue();
                                            str29 = intValue3 != 0 ? "" + intValue3 : null;
                                            z41 = false;
                                            str45 = null;
                                            z42 = false;
                                            str46 = null;
                                            str18 = null;
                                            str47 = null;
                                            str56 = null;
                                            str55 = null;
                                            i24 = -1;
                                            str22 = null;
                                            str54 = null;
                                            str53 = null;
                                            str25 = null;
                                            str52 = null;
                                            str51 = null;
                                            i23 = 0;
                                            z43 = false;
                                            str50 = null;
                                            str49 = null;
                                            str48 = null;
                                            str57 = null;
                                            str58 = null;
                                            str59 = null;
                                            str30 = null;
                                            str61 = null;
                                            str60 = null;
                                            tLRPC$TL_wallPaper2 = null;
                                            str62 = null;
                                            num4 = null;
                                            l4 = null;
                                            l3 = null;
                                            str66 = null;
                                            num3 = null;
                                            str65 = null;
                                            str64 = null;
                                            str63 = null;
                                            z33 = z41;
                                            str13 = str46;
                                            str17 = str47;
                                            str16 = str56;
                                            i19 = i24;
                                            str20 = str54;
                                            str19 = str53;
                                            str37 = str51;
                                            i20 = i23;
                                            z32 = z43;
                                            str38 = str48;
                                            str36 = str57;
                                            str32 = str58;
                                            str31 = str59;
                                            str23 = str61;
                                            str41 = str60;
                                            tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                            str39 = str62;
                                            l = l4;
                                            str24 = str66;
                                            num = num3;
                                            str35 = str65;
                                            str34 = str64;
                                            str33 = str63;
                                            hashMap = null;
                                            str28 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z13 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            j9 = 0;
                                            j7 = 0;
                                            str40 = null;
                                            str21 = str45;
                                            str26 = str52;
                                            str14 = str50;
                                            str27 = str49;
                                            num2 = num4;
                                            l2 = l3;
                                            z30 = false;
                                            z14 = false;
                                            z31 = z42;
                                            str15 = str55;
                                        } else {
                                            if (substring3.startsWith("joinchat/")) {
                                                str46 = substring3.replace("joinchat/", "");
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str18 = null;
                                            } else if (substring3.startsWith("+")) {
                                                str45 = substring3.replace("+", "");
                                                if (AndroidUtilities.isNumeric(str45)) {
                                                    str73 = null;
                                                } else {
                                                    str73 = str45;
                                                    str45 = null;
                                                }
                                                str66 = data.getQueryParameter("text");
                                                str46 = str73;
                                                z41 = false;
                                                z42 = false;
                                                str18 = null;
                                                str47 = null;
                                                str56 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else if (substring3.startsWith("addstickers/")) {
                                                str18 = substring3.replace("addstickers/", "");
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                            } else if (substring3.startsWith("addemoji/")) {
                                                str47 = substring3.replace("addemoji/", "");
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                                str18 = null;
                                                str56 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else if (substring3.startsWith("msg/") || substring3.startsWith("share/")) {
                                                String queryParameter36 = data.getQueryParameter("url");
                                                str8 = queryParameter36 != null ? queryParameter36 : "";
                                                if (data.getQueryParameter("text") != null) {
                                                    if (str8.length() > 0) {
                                                        str8 = str8 + "\n";
                                                        z42 = true;
                                                    } else {
                                                        z42 = false;
                                                    }
                                                    str8 = str8 + data.getQueryParameter("text");
                                                } else {
                                                    z42 = false;
                                                }
                                                if (str8.length() > 16384) {
                                                    i25 = 0;
                                                    str67 = str8.substring(0, LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                                                } else {
                                                    i25 = 0;
                                                    str67 = str8;
                                                }
                                                while (str67.endsWith("\n")) {
                                                    str67 = str67.substring(i25, str67.length() - 1);
                                                }
                                                str56 = str67;
                                                z41 = false;
                                                str45 = null;
                                                str46 = null;
                                                str18 = null;
                                                str47 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else if (substring3.startsWith("confirmphone")) {
                                                String queryParameter37 = data.getQueryParameter("phone");
                                                str50 = data.getQueryParameter("hash");
                                                str55 = queryParameter37;
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                                str18 = null;
                                                str47 = null;
                                                str56 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str49 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else if (substring3.startsWith("setlanguage/")) {
                                                str49 = substring3.substring(12);
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                                str18 = null;
                                                str47 = null;
                                                str56 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str48 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else if (substring3.startsWith("addtheme/")) {
                                                str48 = substring3.substring(9);
                                                z41 = false;
                                                str45 = null;
                                                z42 = false;
                                                str46 = null;
                                                str18 = null;
                                                str47 = null;
                                                str56 = null;
                                                str55 = null;
                                                i24 = -1;
                                                str22 = null;
                                                str54 = null;
                                                str53 = null;
                                                str25 = null;
                                                str52 = null;
                                                str51 = null;
                                                i23 = 0;
                                                z43 = false;
                                                str50 = null;
                                                str49 = null;
                                                str57 = null;
                                                str58 = null;
                                                str59 = null;
                                                str29 = null;
                                                str30 = null;
                                                str61 = null;
                                                str60 = null;
                                                tLRPC$TL_wallPaper2 = null;
                                                str62 = null;
                                                num4 = null;
                                                l4 = null;
                                                l3 = null;
                                                str66 = null;
                                                num3 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            } else {
                                                if (substring3.equalsIgnoreCase("boost") || substring3.startsWith("boost/")) {
                                                    String queryParameter38 = data.getQueryParameter("c");
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() >= 2) {
                                                        str68 = pathSegments.get(1);
                                                    } else if (TextUtils.isEmpty(queryParameter38)) {
                                                        str68 = null;
                                                    } else {
                                                        parseLong = Utilities.parseLong(queryParameter38);
                                                        str68 = null;
                                                        l4 = parseLong;
                                                        z42 = false;
                                                        str46 = null;
                                                        str18 = null;
                                                        str47 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        i24 = -1;
                                                        str22 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str25 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        i23 = 0;
                                                        z43 = false;
                                                        str50 = null;
                                                        str49 = null;
                                                        str48 = null;
                                                        str57 = null;
                                                        str58 = null;
                                                        str59 = null;
                                                        str29 = null;
                                                        str30 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        tLRPC$TL_wallPaper2 = null;
                                                        str62 = null;
                                                        num4 = null;
                                                        l3 = null;
                                                        str66 = null;
                                                        num3 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        str63 = null;
                                                        str45 = str68;
                                                        z41 = true;
                                                    }
                                                    parseLong = null;
                                                    l4 = parseLong;
                                                    z42 = false;
                                                    str46 = null;
                                                    str18 = null;
                                                    str47 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    i24 = -1;
                                                    str22 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str25 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i23 = 0;
                                                    z43 = false;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str57 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str62 = null;
                                                    num4 = null;
                                                    l3 = null;
                                                    str66 = null;
                                                    num3 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str45 = str68;
                                                    z41 = true;
                                                } else if (substring3.startsWith("c/")) {
                                                    List<String> pathSegments2 = data.getPathSegments();
                                                    if (pathSegments2.size() >= 3) {
                                                        l5 = Utilities.parseLong(pathSegments2.get(1));
                                                        num7 = Utilities.parseInt((CharSequence) pathSegments2.get(2));
                                                        if (num7.intValue() != 0) {
                                                            j14 = 0;
                                                            break;
                                                        } else {
                                                            j14 = 0;
                                                        }
                                                        l5 = null;
                                                        num7 = null;
                                                        l6 = Utilities.parseLong(data.getQueryParameter("thread"));
                                                        if (l6.longValue() == j14) {
                                                            l6 = null;
                                                        }
                                                        if (l6 == null) {
                                                            l6 = Utilities.parseLong(data.getQueryParameter("topic"));
                                                            if (l6.longValue() == j14) {
                                                                l6 = null;
                                                            }
                                                        }
                                                        if (l6 == null && num7 != null && pathSegments2.size() >= 4) {
                                                            l6 = Long.valueOf(num7.intValue());
                                                            num7 = Utilities.parseInt((CharSequence) pathSegments2.get(3));
                                                        }
                                                    } else {
                                                        l5 = null;
                                                        num7 = null;
                                                        l6 = null;
                                                    }
                                                    if (data.getQuery() == null || pathSegments2.size() != 2) {
                                                        l7 = l5;
                                                        z41 = false;
                                                    } else {
                                                        z41 = data.getQuery().equals("boost");
                                                        l7 = Utilities.parseLong(pathSegments2.get(1));
                                                    }
                                                    l4 = l7;
                                                    num4 = num7;
                                                    l3 = l6;
                                                    str45 = null;
                                                    z42 = false;
                                                    str46 = null;
                                                    str18 = null;
                                                    str47 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    i24 = -1;
                                                    str22 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str25 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i23 = 0;
                                                    z43 = false;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str57 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str62 = null;
                                                    str66 = null;
                                                    num3 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                } else if (substring3.startsWith("contact/")) {
                                                    str30 = substring3.substring(8);
                                                    z41 = false;
                                                    str45 = null;
                                                    z42 = false;
                                                    str46 = null;
                                                    str18 = null;
                                                    str47 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    i24 = -1;
                                                    str22 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str25 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i23 = 0;
                                                    z43 = false;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str57 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str29 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str62 = null;
                                                    num4 = null;
                                                    l4 = null;
                                                    l3 = null;
                                                    str66 = null;
                                                    num3 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                } else {
                                                    if (substring3.startsWith("folder/")) {
                                                        substring = substring3.substring(7);
                                                    } else if (substring3.startsWith("addlist/")) {
                                                        substring = substring3.substring(8);
                                                    } else if (substring3.startsWith("m/")) {
                                                        str60 = substring3.substring(2);
                                                        z41 = false;
                                                        str45 = null;
                                                        z42 = false;
                                                        str46 = null;
                                                        str18 = null;
                                                        str47 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        i24 = -1;
                                                        str22 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str25 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        i23 = 0;
                                                        z43 = false;
                                                        str50 = null;
                                                        str49 = null;
                                                        str48 = null;
                                                        str57 = null;
                                                        str58 = null;
                                                        str59 = null;
                                                        str29 = null;
                                                        str30 = null;
                                                        str61 = null;
                                                        tLRPC$TL_wallPaper2 = null;
                                                        str62 = null;
                                                        num4 = null;
                                                        l4 = null;
                                                        l3 = null;
                                                        str66 = null;
                                                        num3 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        str63 = null;
                                                    } else if (substring3.length() >= 1) {
                                                        ArrayList arrayList3 = new ArrayList(data.getPathSegments());
                                                        if (arrayList3.size() > 0) {
                                                            i26 = 0;
                                                            if (((String) arrayList3.get(0)).equals("s")) {
                                                                arrayList3.remove(0);
                                                            }
                                                        } else {
                                                            i26 = 0;
                                                        }
                                                        if (arrayList3.size() > 0) {
                                                            str69 = (String) arrayList3.get(i26);
                                                            if (arrayList3.size() >= 3 && "s".equals(arrayList3.get(1))) {
                                                                try {
                                                                    i27 = Integer.parseInt((String) arrayList3.get(2));
                                                                } catch (Exception unused11) {
                                                                }
                                                                num5 = null;
                                                                str71 = null;
                                                                str70 = null;
                                                                if (num5 != null) {
                                                                }
                                                                str22 = data.getQueryParameter("start");
                                                                str54 = data.getQueryParameter("startgroup");
                                                                str53 = data.getQueryParameter("startchannel");
                                                                str25 = data.getQueryParameter("admin");
                                                                str52 = data.getQueryParameter("game");
                                                                str51 = data.getQueryParameter("voicechat");
                                                                i23 = i27;
                                                                boolean booleanQueryParameter2 = data.getBooleanQueryParameter("videochat", false);
                                                                String queryParameter39 = data.getQueryParameter("livestream");
                                                                z43 = booleanQueryParameter2;
                                                                String queryParameter40 = data.getQueryParameter("startattach");
                                                                String queryParameter41 = data.getQueryParameter("choose");
                                                                String queryParameter42 = data.getQueryParameter("attach");
                                                                parseLong2 = Utilities.parseLong(data.getQueryParameter("thread"));
                                                                String queryParameter43 = data.getQueryParameter("text");
                                                                if (data.getQuery() != null) {
                                                                }
                                                                if (parseLong2.longValue() == 0) {
                                                                }
                                                                if (parseLong2 == null) {
                                                                }
                                                                if (parseLong2 == null) {
                                                                }
                                                                str59 = str70;
                                                                num6 = num5;
                                                                parseInt2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                                if (parseInt2.intValue() != 0) {
                                                                }
                                                                str45 = str72;
                                                            } else {
                                                                if (arrayList3.size() > 1) {
                                                                    String str82 = (String) arrayList3.get(1);
                                                                    String queryParameter44 = data.getQueryParameter("startapp");
                                                                    try {
                                                                        num5 = Utilities.parseInt((CharSequence) arrayList3.get(1));
                                                                        if (num5.intValue() == 0) {
                                                                            num5 = null;
                                                                        }
                                                                        str70 = queryParameter44;
                                                                    } catch (NumberFormatException unused12) {
                                                                        str70 = queryParameter44;
                                                                        num5 = null;
                                                                    }
                                                                    str71 = str82;
                                                                    i27 = 0;
                                                                } else if (arrayList3.size() == 1) {
                                                                    str70 = data.getQueryParameter("startapp");
                                                                    i27 = 0;
                                                                    num5 = null;
                                                                    str71 = null;
                                                                }
                                                                if (num5 != null) {
                                                                    i24 = getTimestampFromLink(data);
                                                                    str72 = str69;
                                                                } else {
                                                                    str72 = str69;
                                                                    i24 = -1;
                                                                }
                                                                str22 = data.getQueryParameter("start");
                                                                str54 = data.getQueryParameter("startgroup");
                                                                str53 = data.getQueryParameter("startchannel");
                                                                str25 = data.getQueryParameter("admin");
                                                                str52 = data.getQueryParameter("game");
                                                                str51 = data.getQueryParameter("voicechat");
                                                                i23 = i27;
                                                                boolean booleanQueryParameter22 = data.getBooleanQueryParameter("videochat", false);
                                                                String queryParameter392 = data.getQueryParameter("livestream");
                                                                z43 = booleanQueryParameter22;
                                                                String queryParameter402 = data.getQueryParameter("startattach");
                                                                String queryParameter412 = data.getQueryParameter("choose");
                                                                String queryParameter422 = data.getQueryParameter("attach");
                                                                parseLong2 = Utilities.parseLong(data.getQueryParameter("thread"));
                                                                String queryParameter432 = data.getQueryParameter("text");
                                                                if (data.getQuery() != null) {
                                                                    str57 = queryParameter392;
                                                                    str58 = str71;
                                                                    z44 = data.getQuery().equals("boost");
                                                                } else {
                                                                    str57 = queryParameter392;
                                                                    str58 = str71;
                                                                    z44 = false;
                                                                }
                                                                if (parseLong2.longValue() == 0) {
                                                                    parseLong2 = null;
                                                                }
                                                                if (parseLong2 == null) {
                                                                    parseLong2 = Utilities.parseLong(data.getQueryParameter("topic"));
                                                                    if (parseLong2.longValue() == 0) {
                                                                        parseLong2 = null;
                                                                    }
                                                                }
                                                                if (parseLong2 == null || num5 == null) {
                                                                    str59 = str70;
                                                                } else {
                                                                    str59 = str70;
                                                                    if (arrayList3.size() >= 3) {
                                                                        parseLong2 = Long.valueOf(num5.intValue());
                                                                        num6 = Utilities.parseInt((CharSequence) arrayList3.get(2));
                                                                        parseInt2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                                        if (parseInt2.intValue() != 0) {
                                                                            num4 = num6;
                                                                            l3 = parseLong2;
                                                                            z41 = z44;
                                                                            str66 = queryParameter432;
                                                                            str65 = queryParameter402;
                                                                            str63 = queryParameter412;
                                                                            str64 = queryParameter422;
                                                                            z42 = false;
                                                                            str46 = null;
                                                                            str18 = null;
                                                                            str47 = null;
                                                                            str56 = null;
                                                                            str55 = null;
                                                                            str50 = null;
                                                                            str49 = null;
                                                                            str48 = null;
                                                                            str29 = null;
                                                                            str30 = null;
                                                                            str61 = null;
                                                                            str60 = null;
                                                                            tLRPC$TL_wallPaper2 = null;
                                                                            str62 = null;
                                                                            l4 = null;
                                                                            num3 = null;
                                                                        } else {
                                                                            num3 = parseInt2;
                                                                            num4 = num6;
                                                                            l3 = parseLong2;
                                                                            z41 = z44;
                                                                            str66 = queryParameter432;
                                                                            str65 = queryParameter402;
                                                                            str63 = queryParameter412;
                                                                            str64 = queryParameter422;
                                                                            z42 = false;
                                                                            str46 = null;
                                                                            str18 = null;
                                                                            str47 = null;
                                                                            str56 = null;
                                                                            str55 = null;
                                                                            str50 = null;
                                                                            str49 = null;
                                                                            str48 = null;
                                                                            str29 = null;
                                                                            str30 = null;
                                                                            str61 = null;
                                                                            str60 = null;
                                                                            tLRPC$TL_wallPaper2 = null;
                                                                            str62 = null;
                                                                            l4 = null;
                                                                        }
                                                                        str45 = str72;
                                                                    }
                                                                }
                                                                num6 = num5;
                                                                parseInt2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                                if (parseInt2.intValue() != 0) {
                                                                }
                                                                str45 = str72;
                                                            }
                                                        } else {
                                                            str69 = null;
                                                        }
                                                        i27 = 0;
                                                        num5 = null;
                                                        str71 = null;
                                                        str70 = null;
                                                        if (num5 != null) {
                                                        }
                                                        str22 = data.getQueryParameter("start");
                                                        str54 = data.getQueryParameter("startgroup");
                                                        str53 = data.getQueryParameter("startchannel");
                                                        str25 = data.getQueryParameter("admin");
                                                        str52 = data.getQueryParameter("game");
                                                        str51 = data.getQueryParameter("voicechat");
                                                        i23 = i27;
                                                        boolean booleanQueryParameter222 = data.getBooleanQueryParameter("videochat", false);
                                                        String queryParameter3922 = data.getQueryParameter("livestream");
                                                        z43 = booleanQueryParameter222;
                                                        String queryParameter4022 = data.getQueryParameter("startattach");
                                                        String queryParameter4122 = data.getQueryParameter("choose");
                                                        String queryParameter4222 = data.getQueryParameter("attach");
                                                        parseLong2 = Utilities.parseLong(data.getQueryParameter("thread"));
                                                        String queryParameter4322 = data.getQueryParameter("text");
                                                        if (data.getQuery() != null) {
                                                        }
                                                        if (parseLong2.longValue() == 0) {
                                                        }
                                                        if (parseLong2 == null) {
                                                        }
                                                        if (parseLong2 == null) {
                                                        }
                                                        str59 = str70;
                                                        num6 = num5;
                                                        parseInt2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                        if (parseInt2.intValue() != 0) {
                                                        }
                                                        str45 = str72;
                                                    }
                                                    str61 = substring;
                                                    z41 = false;
                                                    str45 = null;
                                                    z42 = false;
                                                    str46 = null;
                                                    str18 = null;
                                                    str47 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    i24 = -1;
                                                    str22 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str25 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    i23 = 0;
                                                    z43 = false;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str57 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str29 = null;
                                                    str30 = null;
                                                    str60 = null;
                                                    tLRPC$TL_wallPaper2 = null;
                                                    str62 = null;
                                                    num4 = null;
                                                    l4 = null;
                                                    l3 = null;
                                                    str66 = null;
                                                    num3 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                }
                                                z33 = z41;
                                                str13 = str46;
                                                str17 = str47;
                                                str16 = str56;
                                                i19 = i24;
                                                str20 = str54;
                                                str19 = str53;
                                                str37 = str51;
                                                i20 = i23;
                                                z32 = z43;
                                                str38 = str48;
                                                str36 = str57;
                                                str32 = str58;
                                                str31 = str59;
                                                str23 = str61;
                                                str41 = str60;
                                                tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                                str39 = str62;
                                                l = l4;
                                                str24 = str66;
                                                num = num3;
                                                str35 = str65;
                                                str34 = str64;
                                                str33 = str63;
                                                hashMap = null;
                                                str28 = null;
                                                i17 = 0;
                                                i18 = 0;
                                                z12 = false;
                                                z29 = false;
                                                z13 = false;
                                                z15 = false;
                                                z16 = false;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str12 = null;
                                                j9 = 0;
                                                j7 = 0;
                                                str40 = null;
                                                str21 = str45;
                                                str26 = str52;
                                                str14 = str50;
                                                str27 = str49;
                                                num2 = num4;
                                                l2 = l3;
                                                z30 = false;
                                                z14 = false;
                                                z31 = z42;
                                                str15 = str55;
                                            }
                                            str47 = null;
                                            str56 = null;
                                            str55 = null;
                                            i24 = -1;
                                            str22 = null;
                                            str54 = null;
                                            str53 = null;
                                            str25 = null;
                                            str52 = null;
                                            str51 = null;
                                            i23 = 0;
                                            z43 = false;
                                            str50 = null;
                                            str49 = null;
                                            str48 = null;
                                            str57 = null;
                                            str58 = null;
                                            str59 = null;
                                            str29 = null;
                                            str30 = null;
                                            str61 = null;
                                            str60 = null;
                                            tLRPC$TL_wallPaper2 = null;
                                            str62 = null;
                                            num4 = null;
                                            l4 = null;
                                            l3 = null;
                                            str66 = null;
                                            num3 = null;
                                            str65 = null;
                                            str64 = null;
                                            str63 = null;
                                            z33 = z41;
                                            str13 = str46;
                                            str17 = str47;
                                            str16 = str56;
                                            i19 = i24;
                                            str20 = str54;
                                            str19 = str53;
                                            str37 = str51;
                                            i20 = i23;
                                            z32 = z43;
                                            str38 = str48;
                                            str36 = str57;
                                            str32 = str58;
                                            str31 = str59;
                                            str23 = str61;
                                            str41 = str60;
                                            tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                            str39 = str62;
                                            l = l4;
                                            str24 = str66;
                                            num = num3;
                                            str35 = str65;
                                            str34 = str64;
                                            str33 = str63;
                                            hashMap = null;
                                            str28 = null;
                                            i17 = 0;
                                            i18 = 0;
                                            z12 = false;
                                            z29 = false;
                                            z13 = false;
                                            z15 = false;
                                            z16 = false;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str12 = null;
                                            j9 = 0;
                                            j7 = 0;
                                            str40 = null;
                                            str21 = str45;
                                            str26 = str52;
                                            str14 = str50;
                                            str27 = str49;
                                            num2 = num4;
                                            l2 = l3;
                                            z30 = false;
                                            z14 = false;
                                            z31 = z42;
                                            str15 = str55;
                                        }
                                        str62 = substring2;
                                        z41 = false;
                                        str45 = null;
                                        z42 = false;
                                        str46 = null;
                                        str18 = null;
                                        str47 = null;
                                        str56 = null;
                                        str55 = null;
                                        i24 = -1;
                                        str22 = null;
                                        str54 = null;
                                        str53 = null;
                                        str25 = null;
                                        str52 = null;
                                        str51 = null;
                                        i23 = 0;
                                        z43 = false;
                                        str50 = null;
                                        str49 = null;
                                        str48 = null;
                                        str57 = null;
                                        str58 = null;
                                        str59 = null;
                                        str29 = null;
                                        str30 = null;
                                        str61 = null;
                                        str60 = null;
                                        tLRPC$TL_wallPaper2 = null;
                                        num4 = null;
                                        l4 = null;
                                        l3 = null;
                                        str66 = null;
                                        num3 = null;
                                        str65 = null;
                                        str64 = null;
                                        str63 = null;
                                        z33 = z41;
                                        str13 = str46;
                                        str17 = str47;
                                        str16 = str56;
                                        i19 = i24;
                                        str20 = str54;
                                        str19 = str53;
                                        str37 = str51;
                                        i20 = i23;
                                        z32 = z43;
                                        str38 = str48;
                                        str36 = str57;
                                        str32 = str58;
                                        str31 = str59;
                                        str23 = str61;
                                        str41 = str60;
                                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                        str39 = str62;
                                        l = l4;
                                        str24 = str66;
                                        num = num3;
                                        str35 = str65;
                                        str34 = str64;
                                        str33 = str63;
                                        hashMap = null;
                                        str28 = null;
                                        i17 = 0;
                                        i18 = 0;
                                        z12 = false;
                                        z29 = false;
                                        z13 = false;
                                        z15 = false;
                                        z16 = false;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str12 = null;
                                        j9 = 0;
                                        j7 = 0;
                                        str40 = null;
                                        str21 = str45;
                                        str26 = str52;
                                        str14 = str50;
                                        str27 = str49;
                                        num2 = num4;
                                        l2 = l3;
                                        z30 = false;
                                        z14 = false;
                                        z31 = z42;
                                        str15 = str55;
                                    }
                                    z41 = false;
                                    str45 = null;
                                    z42 = false;
                                    str46 = null;
                                    str18 = null;
                                    str47 = null;
                                    str56 = null;
                                    str55 = null;
                                    i24 = -1;
                                    str22 = null;
                                    str54 = null;
                                    str53 = null;
                                    str25 = null;
                                    str52 = null;
                                    str51 = null;
                                    i23 = 0;
                                    z43 = false;
                                    str50 = null;
                                    str49 = null;
                                    str48 = null;
                                    str57 = null;
                                    str58 = null;
                                    str59 = null;
                                    str29 = null;
                                    str30 = null;
                                    str61 = null;
                                    str60 = null;
                                    tLRPC$TL_wallPaper2 = null;
                                    str62 = null;
                                    num4 = null;
                                    l4 = null;
                                    l3 = null;
                                    str66 = null;
                                    num3 = null;
                                    str65 = null;
                                    str64 = null;
                                    str63 = null;
                                    z33 = z41;
                                    str13 = str46;
                                    str17 = str47;
                                    str16 = str56;
                                    i19 = i24;
                                    str20 = str54;
                                    str19 = str53;
                                    str37 = str51;
                                    i20 = i23;
                                    z32 = z43;
                                    str38 = str48;
                                    str36 = str57;
                                    str32 = str58;
                                    str31 = str59;
                                    str23 = str61;
                                    str41 = str60;
                                    tLRPC$TL_wallPaper = tLRPC$TL_wallPaper2;
                                    str39 = str62;
                                    l = l4;
                                    str24 = str66;
                                    num = num3;
                                    str35 = str65;
                                    str34 = str64;
                                    str33 = str63;
                                    hashMap = null;
                                    str28 = null;
                                    i17 = 0;
                                    i18 = 0;
                                    z12 = false;
                                    z29 = false;
                                    z13 = false;
                                    z15 = false;
                                    z16 = false;
                                    str9 = null;
                                    str10 = null;
                                    str11 = null;
                                    str12 = null;
                                    j9 = 0;
                                    j7 = 0;
                                    str40 = null;
                                    str21 = str45;
                                    str26 = str52;
                                    str14 = str50;
                                    str27 = str49;
                                    num2 = num4;
                                    l2 = l3;
                                    z30 = false;
                                    z14 = false;
                                    z31 = z42;
                                    str15 = str55;
                                }
                                break;
                        }
                        if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                            FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(scheme) && str28 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                            intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                        }
                        if (str29 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                            str = " ";
                            iArr3 = iArr;
                            launchActivity = this;
                        } else if (str15 == null || str14 != null) {
                            str = " ";
                            iArr3 = iArr;
                            launchActivity = this;
                            final AlertDialog alertDialog = new AlertDialog(launchActivity, 3);
                            alertDialog.setCanCancel(false);
                            alertDialog.show();
                            tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                            tLRPC$TL_account_sendConfirmPhoneCode.hash = str14;
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
                            bundle.putString("phone", str15);
                            ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda141
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$handleIntent$18(alertDialog, str15, bundle, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                                }
                            }, 2);
                        } else if (str21 != null || str13 != null || str18 != null || str17 != null || str30 != null || str23 != null || str16 != null || str26 != null || str37 != null || z32 || hashMap != null || str28 != null || str27 != null || str29 != null || tLRPC$TL_wallPaper != null || str39 != null || l != null || str38 != null || str40 != null || str41 != null) {
                            str = " ";
                            iArr3 = iArr;
                            runLinkRequest(iArr[0], str21, str13, str18, str17, str22, str20, str19, str25, (str16 == null || !str16.startsWith("@")) ? str16 : " " + str16, str30, str23, str24, z31, num2, l, l2, num, str26, hashMap, str27, str28, str29, str40, tLRPC$TL_wallPaper, str39, str38, str37, z32, str36, 0, i19, str35, str34, str33, str32, str31, progress, booleanExtra, i20, z33, str41);
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
                                        long j18 = query.getLong(query.getColumnIndex("data4"));
                                        int intValue4 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                        try {
                                            for (int i34 = -1; i34 < 4; i34++) {
                                                if (i34 == -1) {
                                                    try {
                                                        i21 = iArr[0];
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        try {
                                                            query.close();
                                                            throw th;
                                                        }
                                                    }
                                                } else {
                                                    i21 = i34;
                                                }
                                                if ((i34 != -1 || !MessagesStorage.getInstance(i21).containsLocalDialog(j18)) && UserConfig.getInstance(i21).getClientUserId() != intValue4) {
                                                    try {
                                                    } catch (Throwable th6) {
                                                        th = th6;
                                                        th = th;
                                                        query.close();
                                                        throw th;
                                                    }
                                                } else {
                                                    iArr[0] = i21;
                                                    switchToAccount(iArr[0], true);
                                                    NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                                    string = query.getString(query.getColumnIndex("mimetype"));
                                                    if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                        j9 = j18;
                                                        z34 = z29;
                                                        if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                            z30 = true;
                                                        }
                                                    } else {
                                                        j9 = j18;
                                                        z34 = true;
                                                    }
                                                    if (query != null) {
                                                        try {
                                                            query.close();
                                                        } catch (Exception e6) {
                                                            e = e6;
                                                            z29 = z34;
                                                            FileLog.e(e);
                                                            str = " ";
                                                            iArr3 = iArr;
                                                            launchActivity = this;
                                                            i16 = i18;
                                                            j16 = j9;
                                                            i6 = i16;
                                                            i5 = i17;
                                                            z11 = z29;
                                                            z17 = z30;
                                                            str3 = str9;
                                                            str4 = str10;
                                                            str5 = str11;
                                                            str2 = str12;
                                                            j3 = j7;
                                                            iArr2 = iArr3;
                                                            i = -1;
                                                            jArr = null;
                                                            j2 = 0;
                                                            i2 = -1;
                                                            i3 = -1;
                                                            z5 = false;
                                                            z6 = false;
                                                            z9 = false;
                                                            z8 = false;
                                                            z7 = false;
                                                            z10 = false;
                                                            j4 = 0;
                                                            j5 = 0;
                                                            i4 = 0;
                                                            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                            }
                                                            z20 = z;
                                                            z21 = z18;
                                                            i8 = i7;
                                                            z19 = false;
                                                            r7 = z21;
                                                            r11 = i8;
                                                            if (!z19) {
                                                            }
                                                            if (z47) {
                                                            }
                                                            if (!z7) {
                                                            }
                                                            intent.setAction(r7);
                                                            return z19;
                                                        }
                                                    }
                                                    z29 = z34;
                                                    str = " ";
                                                    iArr3 = iArr;
                                                    launchActivity = this;
                                                    i16 = i18;
                                                    j16 = j9;
                                                }
                                            }
                                            string = query.getString(query.getColumnIndex("mimetype"));
                                            if (TextUtils.equals(string, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                            }
                                            if (query != null) {
                                            }
                                            z29 = z34;
                                            str = " ";
                                            iArr3 = iArr;
                                            launchActivity = this;
                                            i16 = i18;
                                            j16 = j9;
                                        } catch (Throwable th7) {
                                            th = th7;
                                            th = th;
                                            query.close();
                                            throw th;
                                        }
                                        NotificationCenter.getInstance(iArr[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                }
                            }
                            z34 = z29;
                            if (query != null) {
                            }
                            z29 = z34;
                            str = " ";
                            iArr3 = iArr;
                            launchActivity = this;
                            i16 = i18;
                            j16 = j9;
                        }
                        i16 = i18;
                        j16 = j9;
                    }
                    j8 = 0;
                    j9 = j8;
                    j7 = j9;
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
                    z31 = false;
                    l = null;
                    num = null;
                    num2 = null;
                    l2 = null;
                    str26 = null;
                    hashMap = null;
                    str27 = null;
                    str28 = null;
                    str29 = null;
                    str30 = null;
                    i17 = 0;
                    i18 = 0;
                    z12 = false;
                    z29 = false;
                    z30 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    str9 = null;
                    str10 = null;
                    str11 = null;
                    str12 = null;
                    str40 = null;
                    tLRPC$TL_wallPaper = null;
                    str39 = null;
                    str38 = null;
                    str37 = null;
                    z32 = false;
                    str36 = null;
                    i19 = -1;
                    str35 = null;
                    str34 = null;
                    str33 = null;
                    str32 = null;
                    str31 = null;
                    i20 = 0;
                    z33 = false;
                    str41 = null;
                    if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                    }
                    if (str29 != null) {
                    }
                    if (str15 == null) {
                    }
                    str = " ";
                    iArr3 = iArr;
                    launchActivity = this;
                    final AlertDialog alertDialog2 = new AlertDialog(launchActivity, 3);
                    alertDialog2.setCanCancel(false);
                    alertDialog2.show();
                    tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                    tLRPC$TL_account_sendConfirmPhoneCode.hash = str14;
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
                    bundle2.putString("phone", str15);
                    ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda141
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$handleIntent$18(alertDialog2, str15, bundle2, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                        }
                    }, 2);
                    i16 = i18;
                    j16 = j9;
                } else {
                    str = " ";
                    iArr3 = iArr;
                    launchActivity = this;
                    j16 = 0;
                    i16 = 0;
                    i17 = 0;
                    z12 = false;
                    z29 = false;
                    z30 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    str9 = null;
                    str10 = null;
                    str11 = null;
                    str12 = null;
                    j7 = 0;
                }
                i6 = i16;
                i5 = i17;
                z11 = z29;
                z17 = z30;
                str3 = str9;
                str4 = str10;
                str5 = str11;
                str2 = str12;
                j3 = j7;
                iArr2 = iArr3;
                i = -1;
                jArr = null;
                j2 = 0;
                i2 = -1;
                i3 = -1;
                z5 = false;
                z6 = false;
                z9 = false;
                z8 = false;
                z7 = false;
                z10 = false;
                j4 = 0;
                j5 = 0;
                i4 = 0;
            } else {
                str = " ";
                int[] iArr5 = iArr;
                launchActivity = this;
                if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                    iArr2 = iArr5;
                    i = -1;
                    str2 = null;
                    jArr = null;
                    str3 = null;
                    j16 = 0;
                    j2 = 0;
                    i2 = -1;
                    j3 = 0;
                    i3 = -1;
                    z5 = false;
                    z6 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z10 = false;
                    z11 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    j4 = 0;
                    j5 = 0;
                    str4 = null;
                    str5 = null;
                    i4 = 0;
                    i5 = 0;
                    z17 = false;
                    i6 = 1;
                } else if (intent.getAction().equals("new_dialog")) {
                    iArr2 = iArr5;
                    i = -1;
                    str2 = null;
                    jArr = null;
                    str3 = null;
                    j16 = 0;
                    j2 = 0;
                    i2 = -1;
                    j3 = 0;
                    i3 = -1;
                    z5 = false;
                    z6 = false;
                    z9 = false;
                    z8 = true;
                    z7 = false;
                    z10 = false;
                    z11 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    j4 = 0;
                    j5 = 0;
                    str4 = null;
                    str5 = null;
                    i4 = 0;
                    i5 = 0;
                    z17 = false;
                    i6 = 0;
                } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                    long longExtra = intent.getLongExtra("chatId", 0L);
                    long[] longArrayExtra = intent.getLongArrayExtra("storyDialogIds");
                    int i35 = -1;
                    int intExtra = intent.getIntExtra("storyId", -1);
                    long longExtra2 = intent.getLongExtra("userId", 0L);
                    int intExtra2 = intent.getIntExtra("encId", 0);
                    int intExtra3 = intent.getIntExtra("appWidgetId", 0);
                    long longExtra3 = intent.getLongExtra("topicId", 0L);
                    if (intExtra3 != 0) {
                        j16 = 0;
                        longExtra3 = 0;
                        i12 = intent.getIntExtra("appWidgetType", 0);
                        i13 = intExtra3;
                        iArr2 = iArr5;
                        jArr2 = null;
                        i14 = 0;
                        i11 = 0;
                        z28 = false;
                        i15 = 6;
                    } else {
                        int intExtra4 = intent.getIntExtra("message_id", 0);
                        if (intExtra != -1) {
                            iArr2 = iArr5;
                            NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                            j16 = 0;
                            longExtra3 = 0;
                            i35 = intExtra;
                            i11 = intExtra4;
                        } else {
                            iArr2 = iArr5;
                            if (longArrayExtra != null) {
                                NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                jArr2 = longArrayExtra;
                                j16 = 0;
                                longExtra3 = 0;
                                i11 = intExtra4;
                                i12 = -1;
                                i13 = -1;
                            } else if (longExtra != 0) {
                                NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                i11 = intExtra4;
                                i12 = -1;
                                i13 = -1;
                                jArr2 = null;
                                i14 = 0;
                                z28 = false;
                                i15 = 0;
                                j6 = longExtra;
                                j16 = 0;
                                j4 = 0;
                                j5 = 0;
                                jArr = jArr2;
                                i4 = i14;
                                i5 = i11;
                                i6 = i15;
                                str2 = null;
                                str3 = null;
                                z9 = false;
                                z8 = false;
                                z7 = false;
                                z10 = false;
                                z11 = false;
                                z12 = false;
                                z13 = false;
                                z14 = false;
                                z15 = false;
                                z16 = false;
                                str4 = null;
                                str5 = null;
                                z17 = false;
                                i3 = i13;
                                i2 = i12;
                                i = i35;
                                j3 = j6;
                                j2 = longExtra3;
                                z5 = z28;
                                z6 = false;
                            } else if (longExtra2 != 0) {
                                NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                longExtra3 = 0;
                                i11 = intExtra4;
                                j16 = longExtra2;
                            } else if (intExtra2 != 0) {
                                NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                j16 = 0;
                                longExtra3 = 0;
                                i11 = intExtra4;
                                i14 = intExtra2;
                                i12 = -1;
                                i13 = -1;
                                jArr2 = null;
                                z28 = false;
                                i15 = 0;
                            } else {
                                j16 = 0;
                                longExtra3 = 0;
                                i11 = intExtra4;
                                i12 = -1;
                                i13 = -1;
                                jArr2 = null;
                            }
                            i14 = 0;
                            z28 = true;
                            i15 = 0;
                        }
                        i12 = -1;
                        i13 = -1;
                        jArr2 = null;
                        i14 = 0;
                        z28 = false;
                        i15 = 0;
                    }
                    j6 = longExtra3;
                    j4 = 0;
                    j5 = 0;
                    jArr = jArr2;
                    i4 = i14;
                    i5 = i11;
                    i6 = i15;
                    str2 = null;
                    str3 = null;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z10 = false;
                    z11 = false;
                    z12 = false;
                    z13 = false;
                    z14 = false;
                    z15 = false;
                    z16 = false;
                    str4 = null;
                    str5 = null;
                    z17 = false;
                    i3 = i13;
                    i2 = i12;
                    i = i35;
                    j3 = j6;
                    j2 = longExtra3;
                    z5 = z28;
                    z6 = false;
                } else {
                    iArr2 = iArr5;
                    j = 0;
                    if (intent.getAction().startsWith(OpenAttachedMenuBotReceiver.ACTION)) {
                        long longExtra4 = intent.getLongExtra("botId", 0L);
                        j2 = 0;
                        j3 = 0;
                        j4 = 0;
                        j5 = longExtra4;
                        if (longExtra4 != 0) {
                            i = -1;
                            str2 = null;
                            i2 = -1;
                            i3 = -1;
                            z5 = false;
                            z6 = false;
                            z9 = false;
                            z8 = false;
                            z7 = false;
                            z10 = true;
                        } else {
                            i = -1;
                            str2 = null;
                            i2 = -1;
                            i3 = -1;
                            z5 = false;
                            z6 = false;
                            z9 = false;
                            z8 = false;
                            z7 = false;
                            z10 = false;
                        }
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        z17 = false;
                        i6 = 0;
                        j16 = 0;
                        jArr = null;
                        str3 = null;
                    } else if (intent.getAction().equals("com.tmessages.openplayer")) {
                        j16 = 0;
                        j2 = 0;
                        j3 = 0;
                        j4 = 0;
                        j5 = 0;
                        i = -1;
                        str2 = null;
                        jArr = null;
                        str3 = null;
                        i2 = -1;
                        i3 = -1;
                        z5 = false;
                        z6 = true;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z10 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        z17 = false;
                        i6 = 0;
                    } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                        j16 = 0;
                        j2 = 0;
                        j3 = 0;
                        j4 = 0;
                        j5 = 0;
                        i = -1;
                        str2 = null;
                        jArr = null;
                        str3 = null;
                        i2 = -1;
                        i3 = -1;
                        z5 = false;
                        z6 = false;
                        z9 = true;
                        z8 = false;
                        z7 = false;
                        z10 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        z17 = false;
                        i6 = 0;
                    } else {
                        if (action.equals("voip_chat")) {
                            j16 = 0;
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            j5 = 0;
                            i = -1;
                            str2 = null;
                            jArr = null;
                            str3 = null;
                            i2 = -1;
                            i3 = -1;
                            z5 = false;
                            z6 = false;
                            z9 = false;
                            z8 = false;
                            z7 = true;
                            z10 = false;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            z16 = false;
                            str4 = null;
                            str5 = null;
                            i4 = 0;
                            i5 = 0;
                            z17 = false;
                            i6 = 0;
                        }
                        j16 = j;
                        j2 = j16;
                        j3 = j2;
                        j4 = j3;
                        j5 = j4;
                        i = -1;
                        str2 = null;
                        jArr = null;
                        str3 = null;
                        i2 = -1;
                        i3 = -1;
                        z5 = false;
                        z6 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z10 = false;
                        z11 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        z16 = false;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        z17 = false;
                        i6 = 0;
                    }
                }
            }
            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                if (str2 != null) {
                    BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                    if (lastFragment2 instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) lastFragment2;
                        if (dialogsActivity.isMainDialogList()) {
                            if (dialogsActivity.getFragmentView() != null && z) {
                                i7 = 1;
                                dialogsActivity.search(str2, true);
                            } else {
                                i7 = 1;
                                dialogsActivity.setInitialSearchString(str2);
                            }
                        }
                    } else {
                        i7 = 1;
                        z5 = true;
                    }
                    if (i <= 0) {
                        NotificationsController.getInstance(iArr2[0]).processSeenStoryReactions(UserConfig.getInstance(iArr2[0]).getClientUserId(), i);
                        launchActivity.openMyStory(i, i7);
                    } else if (jArr != null) {
                        NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                        launchActivity.openStories(jArr, i7);
                    } else {
                        if (j16 != 0) {
                            boolean z52 = z17;
                            if (!z11 && !z52) {
                                Bundle bundle3 = new Bundle();
                                bundle3.putLong("user_id", j16);
                                int i36 = i5;
                                if (i36 != 0) {
                                    bundle3.putInt("message_id", i36);
                                }
                                if (!mainFragmentsStack.isEmpty()) {
                                    MessagesController messagesController = MessagesController.getInstance(iArr2[0]);
                                    ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                }
                                if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle3)).setNoAnimation(i7))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z22 = true;
                                }
                                z22 = false;
                            } else if (z13) {
                                BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                if (lastFragment3 != null) {
                                    AlertsCreator.createCallDialogAlert(lastFragment3, lastFragment3.getMessagesController().getUser(Long.valueOf(j16)), z52);
                                }
                            } else {
                                VoIPPendingCall.startOrSchedule(launchActivity, j16, z52, AccountInstance.getInstance(iArr2[0]));
                            }
                        } else {
                            int i37 = i5;
                            final boolean z53 = z17;
                            if (j3 != 0) {
                                Bundle bundle4 = new Bundle();
                                bundle4.putLong("chat_id", j3);
                                if (i37 != 0) {
                                    bundle4.putInt("message_id", i37);
                                }
                                if (!mainFragmentsStack.isEmpty()) {
                                    MessagesController messagesController2 = MessagesController.getInstance(iArr2[0]);
                                    ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                                }
                                final ChatActivity chatActivity = new ChatActivity(bundle4);
                                if (j2 > 0) {
                                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().findTopic(j3, j2);
                                    FileLog.d("LaunchActivity openForum " + j3 + str + j2 + " TL_forumTopic " + findTopic);
                                    if (findTopic != null) {
                                        ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(-j3, j2));
                                    } else {
                                        final long j19 = j3;
                                        final long j20 = j2;
                                        MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().loadTopic(j3, j20, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$handleIntent$19(j19, j20, chatActivity);
                                            }
                                        });
                                        return i7;
                                    }
                                }
                                if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(chatActivity).setNoAnimation(i7))) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z22 = true;
                                }
                                z22 = false;
                            } else {
                                String str83 = str;
                                int i38 = i4;
                                if (i38 != 0) {
                                    Bundle bundle5 = new Bundle();
                                    bundle5.putInt("enc_id", i38);
                                    if (getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle5)).setNoAnimation(i7))) {
                                        launchActivity.drawerLayoutContainer.closeDrawer();
                                        z22 = true;
                                    }
                                    z22 = false;
                                } else {
                                    if (z5) {
                                        if (!AndroidUtilities.isTablet()) {
                                            launchActivity.actionBarLayout.removeAllFragments();
                                        } else {
                                            ActionBarLayout actionBarLayout = launchActivity.layersActionBarLayout;
                                            if (actionBarLayout != null && !actionBarLayout.getFragmentStack().isEmpty()) {
                                                while (launchActivity.layersActionBarLayout.getFragmentStack().size() - i7 > 0) {
                                                    ActionBarLayout actionBarLayout2 = launchActivity.layersActionBarLayout;
                                                    actionBarLayout2.removeFragmentFromStack(actionBarLayout2.getFragmentStack().get(0));
                                                }
                                                launchActivity.layersActionBarLayout.closeLastFragment(false);
                                            }
                                        }
                                        str6 = str2;
                                        z20 = false;
                                        z19 = false;
                                        r7 = 0;
                                        r11 = i7;
                                    } else {
                                        if (!z6) {
                                            z18 = false;
                                            z24 = false;
                                            z24 = false;
                                            z18 = false;
                                            z24 = false;
                                            z18 = false;
                                            z18 = false;
                                            z25 = false;
                                            z25 = false;
                                            z18 = false;
                                            z18 = false;
                                            z18 = false;
                                            z18 = false;
                                            z18 = false;
                                            r7 = 0;
                                            if (z9) {
                                                if (!launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                    launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda153
                                                        @Override // org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate
                                                        public final void didSelectLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
                                                            LaunchActivity.this.lambda$handleIntent$21(iArr2, sharingLocationInfo);
                                                        }
                                                    }, null));
                                                }
                                            } else {
                                                Uri uri11 = launchActivity.exportingChatUri;
                                                if (uri11 != null) {
                                                    launchActivity.runImportRequest(uri11, launchActivity.documentsUrisArray);
                                                    str6 = str2;
                                                } else if (launchActivity.importingStickers != null) {
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            LaunchActivity.this.lambda$handleIntent$22();
                                                        }
                                                    });
                                                } else if (launchActivity.videoPath != null || launchActivity.voicePath != null || launchActivity.photoPathsArray != null || launchActivity.sendingText != null || launchActivity.documentsPathsArray != null || launchActivity.contactsToSend != null || launchActivity.documentsUrisArray != null) {
                                                    str6 = str2;
                                                    if (!AndroidUtilities.isTablet()) {
                                                        NotificationCenter.getInstance(iArr2[0]).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                                                    }
                                                    long j21 = j4;
                                                    if (j21 == 0) {
                                                        launchActivity.openDialogsToSend(false);
                                                        z20 = z;
                                                        z19 = true;
                                                        r11 = i7;
                                                    } else {
                                                        ArrayList<MessagesStorage.TopicKey> arrayList6 = new ArrayList<>();
                                                        arrayList6.add(MessagesStorage.TopicKey.of(j21, 0L));
                                                        didSelectDialogs(null, arrayList6, null, false, null);
                                                    }
                                                } else {
                                                    int i39 = i6;
                                                    if (i39 != 7) {
                                                        i9 = 8;
                                                        if (i39 == 8 || i39 == 9) {
                                                            str6 = str2;
                                                        } else {
                                                            if (i39 != 0) {
                                                                if (i39 == i7) {
                                                                    Bundle bundle6 = new Bundle();
                                                                    bundle6.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                                                    openSettings = new ProfileActivity(bundle6);
                                                                } else {
                                                                    if (i39 == 2) {
                                                                        filtersSetupActivity = new ThemeActivity(0);
                                                                    } else if (i39 == 3) {
                                                                        filtersSetupActivity = new SessionsActivity(0);
                                                                    } else if (i39 == 4) {
                                                                        filtersSetupActivity = new FiltersSetupActivity();
                                                                    } else if (i39 == 5) {
                                                                        actionIntroActivity = new ActionIntroActivity(3);
                                                                        i10 = 6;
                                                                        z23 = true;
                                                                        if (i39 == i10) {
                                                                            getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(actionIntroActivity).setNoAnimation(i7));
                                                                        } else {
                                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                                                                                @Override // java.lang.Runnable
                                                                                public final void run() {
                                                                                    LaunchActivity.this.lambda$handleIntent$23(actionIntroActivity, z23);
                                                                                }
                                                                            });
                                                                        }
                                                                        if (AndroidUtilities.isTablet()) {
                                                                            launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                            launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                        } else {
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                        }
                                                                    } else {
                                                                        i10 = 6;
                                                                        if (i39 == 6) {
                                                                            openSettings = new EditWidgetActivity(i2, i3);
                                                                        } else if (i39 == 10) {
                                                                            openSettings = new LanguageSelectActivity();
                                                                        } else if (i39 == 11) {
                                                                            openSettings = new AutoDeleteMessagesActivity();
                                                                        } else if (i39 == 12) {
                                                                            openSettings = new PrivacySettingsActivity();
                                                                        } else {
                                                                            ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
                                                                            openSettings = applicationLoader != null ? applicationLoader.openSettings(i39) : null;
                                                                        }
                                                                        z23 = false;
                                                                        actionIntroActivity = openSettings;
                                                                        if (i39 == i10) {
                                                                        }
                                                                        if (AndroidUtilities.isTablet()) {
                                                                        }
                                                                    }
                                                                    openSettings = filtersSetupActivity;
                                                                }
                                                                i10 = 6;
                                                                z23 = false;
                                                                actionIntroActivity = openSettings;
                                                                if (i39 == i10) {
                                                                }
                                                                if (AndroidUtilities.isTablet()) {
                                                                }
                                                            } else if (z8) {
                                                                Bundle bundle7 = new Bundle();
                                                                bundle7.putBoolean("destroyAfterSelect", i7);
                                                                getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new ContactsActivity(bundle7)).setNoAnimation(i7));
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                }
                                                            } else if (str3 != null) {
                                                                Bundle bundle8 = new Bundle();
                                                                bundle8.putBoolean("destroyAfterSelect", i7);
                                                                bundle8.putBoolean("returnAsResult", i7);
                                                                bundle8.putBoolean("onlyUsers", i7);
                                                                bundle8.putBoolean("allowSelf", false);
                                                                ContactsActivity contactsActivity = new ContactsActivity(bundle8);
                                                                contactsActivity.setInitialSearchString(str3);
                                                                contactsActivity.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda154
                                                                    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                                                    public final void didSelectContact(TLRPC$User tLRPC$User, String str84, ContactsActivity contactsActivity2) {
                                                                        LaunchActivity.this.lambda$handleIntent$24(z53, iArr2, tLRPC$User, str84, contactsActivity2);
                                                                    }
                                                                });
                                                                getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(contactsActivity).setRemoveLast(launchActivity.actionBarLayout.getLastFragment() instanceof ContactsActivity));
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                }
                                                            } else if (z16) {
                                                                final ActionIntroActivity actionIntroActivity2 = new ActionIntroActivity(5);
                                                                actionIntroActivity2.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda147
                                                                    @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                                                    public final void didFindQRCode(String str84) {
                                                                        LaunchActivity.this.lambda$handleIntent$28(actionIntroActivity2, str84);
                                                                    }
                                                                });
                                                                getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(actionIntroActivity2).setNoAnimation(i7));
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                }
                                                            } else if (z14) {
                                                                NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(launchActivity.actionBarLayout.getLastFragment(), launchActivity);
                                                                String str84 = str4;
                                                                if (str84 != null) {
                                                                    String[] split3 = str84.split(str83, 2);
                                                                    newContactBottomSheet.setInitialName(split3[0], split3.length > i7 ? split3[i7] : null);
                                                                }
                                                                String str85 = str5;
                                                                if (str85 != null) {
                                                                    newContactBottomSheet.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str85, i7), false);
                                                                }
                                                                newContactBottomSheet.show();
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                }
                                                            } else {
                                                                final String str86 = str4;
                                                                String str87 = str5;
                                                                if (z7) {
                                                                    str6 = str2;
                                                                    GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                                    if (GroupCallActivity.groupCallInstance != null) {
                                                                        GroupCallActivity.groupCallUiVisible = i7;
                                                                    }
                                                                } else {
                                                                    str6 = str2;
                                                                    if (z15) {
                                                                        final BaseFragment lastFragment4 = launchActivity.actionBarLayout.getLastFragment();
                                                                        if (lastFragment4 == null || lastFragment4.getParentActivity() == null) {
                                                                            z22 = false;
                                                                        } else {
                                                                            final String phoneNumber = NewContactBottomSheet.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str87, false);
                                                                            AlertDialog.Builder title = new AlertDialog.Builder(lastFragment4.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle));
                                                                            int i40 = R.string.NewContactAlertMessage;
                                                                            Object[] objArr = new Object[i7];
                                                                            objArr[0] = PhoneFormat.getInstance().format(phoneNumber);
                                                                            lastFragment4.showDialog(title.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", i40, objArr))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                                                                                @Override // android.content.DialogInterface.OnClickListener
                                                                                public final void onClick(DialogInterface dialogInterface, int i41) {
                                                                                    LaunchActivity.this.lambda$handleIntent$29(lastFragment4, phoneNumber, str86, dialogInterface, i41);
                                                                                }
                                                                            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
                                                                            z22 = true;
                                                                        }
                                                                        z19 = z22;
                                                                        z20 = z;
                                                                        r7 = z25;
                                                                        r11 = i7;
                                                                    } else if (z12) {
                                                                        getActionBarLayout().presentFragment(new INavigationLayout.NavigationParams(new CallLogActivity()).setNoAnimation(i7));
                                                                        if (AndroidUtilities.isTablet()) {
                                                                            launchActivity.actionBarLayout.rebuildFragments(i7);
                                                                            launchActivity.rightActionBarLayout.rebuildFragments(i7);
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                        } else {
                                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(i7, false);
                                                                        }
                                                                        z20 = z;
                                                                        z19 = true;
                                                                        r11 = i7;
                                                                    } else if (z10) {
                                                                        launchActivity.processAttachedMenuBotFromShortcut(j5);
                                                                    }
                                                                }
                                                            }
                                                            z20 = z;
                                                            str6 = str2;
                                                            z19 = true;
                                                            r11 = i7;
                                                        }
                                                    } else {
                                                        str6 = str2;
                                                        i9 = 8;
                                                    }
                                                    if (!BuildVars.DEBUG_PRIVATE_VERSION) {
                                                        str7 = "Locked in release.";
                                                    } else if (i39 == 7) {
                                                        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit();
                                                        BuildVars.LOGS_ENABLED = i7;
                                                        edit.putBoolean("logsEnabled", i7).commit();
                                                        str7 = "Logs enabled.";
                                                    } else {
                                                        if (i39 == i9) {
                                                            ProfileActivity.sendLogs(launchActivity, false);
                                                        } else if (i39 == 9) {
                                                            SharedPreferences.Editor edit2 = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit();
                                                            BuildVars.LOGS_ENABLED = false;
                                                            edit2.putBoolean("logsEnabled", false).commit();
                                                            str7 = "Logs disabled.";
                                                        }
                                                        str7 = null;
                                                    }
                                                    if (str7 != null && (lastFragment = launchActivity.actionBarLayout.getLastFragment()) != null) {
                                                        BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.info, str7).show();
                                                    }
                                                }
                                            }
                                        } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                            z24 = false;
                                        } else {
                                            z24 = false;
                                            launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                                        }
                                        z20 = z;
                                        str6 = str2;
                                        z21 = z24;
                                        i8 = i7;
                                        z19 = false;
                                        r7 = z21;
                                        r11 = i8;
                                    }
                                    if (!z19 && !z20) {
                                        if (!AndroidUtilities.isTablet()) {
                                            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                if (launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                                    launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment(), -2);
                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                }
                                            } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                DialogsActivity dialogsActivity2 = new DialogsActivity(r7);
                                                dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                                if (str6 != null) {
                                                    dialogsActivity2.setInitialSearchString(str6);
                                                }
                                                launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2, -2);
                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r11, false);
                                            }
                                        } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment(), -2);
                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                            } else {
                                                DialogsActivity dialogsActivity3 = new DialogsActivity(r7);
                                                dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                                                if (str6 != null) {
                                                    dialogsActivity3.setInitialSearchString(str6);
                                                }
                                                launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3, -2);
                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r11, false);
                                            }
                                        }
                                        if (z4) {
                                            launchActivity.actionBarLayout.rebuildFragments(r11);
                                            if (AndroidUtilities.isTablet()) {
                                                launchActivity.layersActionBarLayout.rebuildFragments(r11);
                                                launchActivity.rightActionBarLayout.rebuildFragments(r11);
                                            }
                                        }
                                    }
                                    if (z47) {
                                        VoIPFragment.show(launchActivity, iArr2[0]);
                                    }
                                    if (!z7 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                                        groupCallActivity.dismiss();
                                    }
                                    intent.setAction(r7);
                                    return z19;
                                }
                            }
                        }
                        str6 = str2;
                        z25 = false;
                        z19 = z22;
                        z20 = z;
                        r7 = z25;
                        r11 = i7;
                        if (!z19) {
                            if (!AndroidUtilities.isTablet()) {
                            }
                            if (z4) {
                            }
                        }
                        if (z47) {
                        }
                        if (!z7) {
                            groupCallActivity.dismiss();
                        }
                        intent.setAction(r7);
                        return z19;
                    }
                    str6 = str2;
                    z18 = false;
                }
                i7 = 1;
                if (i <= 0) {
                }
                str6 = str2;
                z18 = false;
            } else {
                str6 = str2;
                z18 = false;
                i7 = 1;
            }
            z20 = z;
            z21 = z18;
            i8 = i7;
            z19 = false;
            r7 = z21;
            r11 = i8;
            if (!z19) {
            }
            if (z47) {
            }
            if (!z7) {
            }
            intent.setAction(r7);
            return z19;
        }
        j = 0;
        str = " ";
        launchActivity = this;
        iArr2 = iArr;
        j16 = j;
        j2 = j16;
        j3 = j2;
        j4 = j3;
        j5 = j4;
        i = -1;
        str2 = null;
        jArr = null;
        str3 = null;
        i2 = -1;
        i3 = -1;
        z5 = false;
        z6 = false;
        z9 = false;
        z8 = false;
        z7 = false;
        z10 = false;
        z11 = false;
        z12 = false;
        z13 = false;
        z14 = false;
        z15 = false;
        z16 = false;
        str4 = null;
        str5 = null;
        i4 = 0;
        i5 = 0;
        z17 = false;
        i6 = 0;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z20 = z;
        z21 = z18;
        i8 = i7;
        z19 = false;
        r7 = z21;
        r11 = i8;
        if (!z19) {
        }
        if (z47) {
        }
        if (!z7) {
        }
        intent.setAction(r7);
        return z19;
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
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
            lambda$runLinkRequest$88(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, getActionBarLayout().getLastFragment(), tLRPC$TL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$19(long j, long j2, ChatActivity chatActivity) {
        TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(j, j2);
        FileLog.d("LaunchActivity openForum after load " + j + " " + j2 + " TL_forumTopic " + findTopic);
        if (this.actionBarLayout != null) {
            ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(-j, j2));
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
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda160
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
                LaunchActivity.lambda$handleIntent$20(iArr, dialogId, tLRPC$MessageMedia, i, z, i2);
            }
        });
        lambda$runLinkRequest$88(locationActivity);
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda117
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$handleIntent$27(AlertDialog.this, actionIntroActivity, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$27(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
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

    private int runCommentRequest(int i, Runnable runnable, Integer num, Integer num2, Long l, TLRPC$Chat tLRPC$Chat) {
        return runCommentRequest(i, runnable, num, num2, l, tLRPC$Chat, null, null, 0, -1);
    }

    private int runCommentRequest(final int i, final Runnable runnable, final Integer num, final Integer num2, final Long l, final TLRPC$Chat tLRPC$Chat, final Runnable runnable2, final String str, final int i2, final int i3) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = num2 != null ? num.intValue() : (int) l.longValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda126
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runCommentRequest$31(i, tLRPC$Chat, l, num2, num, runnable2, str, i2, i3, tLRPC$TL_messages_getDiscussionMessage, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$31(final int i, final TLRPC$Chat tLRPC$Chat, final Long l, final Integer num, final Integer num2, final Runnable runnable, final String str, final int i2, final int i3, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Runnable runnable2, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$30(tLObject, i, tLRPC$Chat, l, num, num2, runnable, str, i2, i3, tLRPC$TL_messages_getDiscussionMessage, runnable2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0123 A[Catch: Exception -> 0x011f, TRY_LEAVE, TryCatch #0 {Exception -> 0x011f, blocks: (B:40:0x011b, B:44:0x0123), top: B:48:0x011b }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runCommentRequest$30(TLObject tLObject, int i, TLRPC$Chat tLRPC$Chat, Long l, Integer num, Integer num2, Runnable runnable, String str, int i2, int i3, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Runnable runnable2) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_discussionMessage) {
            TLRPC$TL_messages_discussionMessage tLRPC$TL_messages_discussionMessage = (TLRPC$TL_messages_discussionMessage) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_discussionMessage.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_messages_discussionMessage.chats, false);
            ArrayList<MessageObject> arrayList = new ArrayList<>();
            int size = tLRPC$TL_messages_discussionMessage.messages.size();
            for (int i4 = 0; i4 < size; i4++) {
                arrayList.add(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_messages_discussionMessage.messages.get(i4), true, true));
            }
            if (!arrayList.isEmpty() || (tLRPC$Chat.forum && l != null && l.longValue() == 1)) {
                if (tLRPC$Chat.forum) {
                    openTopicRequest(i, (int) l.longValue(), tLRPC$Chat, (num != null ? num : num2).intValue(), null, runnable, str, i2, arrayList, i3);
                    z = true;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -arrayList.get(0).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num2.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, tLRPC$TL_messages_discussionMessage.read_inbox_max_id, tLRPC$TL_messages_discussionMessage.read_outbox_max_id, null);
                    if (num != null) {
                        if (str != null) {
                            chatActivity.setHighlightQuote(num.intValue(), str, i3);
                        } else {
                            chatActivity.setHighlightMessageId(num.intValue());
                        }
                    } else if (l != null) {
                        if (str != null) {
                            chatActivity.setHighlightQuote(num2.intValue(), str, i3);
                        } else {
                            chatActivity.setHighlightMessageId(num2.intValue());
                        }
                    }
                    lambda$runLinkRequest$88(chatActivity);
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

    /* JADX WARN: Removed duplicated region for block: B:39:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openTopicRequest(final int i, final int i2, final TLRPC$Chat tLRPC$Chat, final int i3, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, final Runnable runnable, final String str, final int i4, final ArrayList<MessageObject> arrayList, final int i5) {
        BaseFragment baseFragment;
        TLRPC$TL_forumTopic findTopic = tLRPC$TL_forumTopic == null ? MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, i2) : tLRPC$TL_forumTopic;
        if (findTopic == null) {
            TLRPC$TL_channels_getForumTopicsByID tLRPC$TL_channels_getForumTopicsByID = new TLRPC$TL_channels_getForumTopicsByID();
            tLRPC$TL_channels_getForumTopicsByID.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(tLRPC$Chat.id);
            tLRPC$TL_channels_getForumTopicsByID.topics.add(Integer.valueOf(i2));
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda125
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$openTopicRequest$33(i, tLRPC$Chat, i2, i3, runnable, str, i4, arrayList, i5, tLObject, tLRPC$TL_error);
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
                    chatActivity.setHighlightQuote(i3, str, i5);
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
                chatActivity2.setHighlightQuote(i3, str, i5);
            } else {
                chatActivity2.setHighlightMessageId(i3);
            }
            chatActivity2.scrollToMessageId(i3, i4, true, 0, true, 0, null);
        }
        lambda$runLinkRequest$88(chatActivity2);
        if (runnable == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTopicRequest$33(final int i, final TLRPC$Chat tLRPC$Chat, final int i2, final int i3, final Runnable runnable, final String str, final int i4, final ArrayList arrayList, final int i5, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openTopicRequest$32(tLRPC$TL_error, tLObject, i, tLRPC$Chat, i2, i3, runnable, str, i4, arrayList, i5);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTopicRequest$32(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, TLRPC$Chat tLRPC$Chat, int i2, int i3, Runnable runnable, String str, int i4, ArrayList arrayList, int i5) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics = (TLRPC$TL_messages_forumTopics) tLObject;
            SparseArray<TLRPC$Message> sparseArray = new SparseArray<>();
            for (int i6 = 0; i6 < tLRPC$TL_messages_forumTopics.messages.size(); i6++) {
                sparseArray.put(tLRPC$TL_messages_forumTopics.messages.get(i6).id, tLRPC$TL_messages_forumTopics.messages.get(i6));
            }
            MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_forumTopics.users, false);
            MessagesController.getInstance(i).putChats(tLRPC$TL_messages_forumTopics.chats, false);
            MessagesController.getInstance(i).getTopicsController().processTopics(tLRPC$Chat.id, tLRPC$TL_messages_forumTopics.topics, sparseArray, false, 2, -1);
            openTopicRequest(i, i2, tLRPC$Chat, i3, MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, i2), runnable, str, i4, arrayList, i5);
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
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda129
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
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

    public void openMessage(final long j, final int i, final String str, final Browser.Progress progress, int i2, final int i3) {
        BaseFragment baseFragment;
        TLRPC$Chat chat;
        if (j < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j))) != null && ChatObject.isForum(chat)) {
            if (progress != null) {
                progress.init();
            }
            openForumFromLink(j, Integer.valueOf(i), str, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$openMessage$37(Browser.Progress.this);
                }
            }, i2, i3);
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
                openForumFromLink(j, Integer.valueOf(i), str, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$openMessage$38(Browser.Progress.this);
                    }
                }, i2, i3);
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openMessage$42(bundle, i, str, i3, j, progress, baseFragment2);
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
    public /* synthetic */ void lambda$openMessage$42(final Bundle bundle, final int i, final String str, final int i2, final long j, final Browser.Progress progress, final BaseFragment baseFragment) {
        final ChatActivity chatActivity = new ChatActivity(bundle);
        chatActivity.setHighlightQuote(i, str, i2);
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
        final int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda137
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openMessage$40(progress, j, i, baseFragment, bundle, chatActivity, str, i2, tLObject, tLRPC$TL_error);
            }
        });
        if (progress != null) {
            progress.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openMessage$41(sendRequest);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$40(final Browser.Progress progress, final long j, final int i, final BaseFragment baseFragment, final Bundle bundle, final ChatActivity chatActivity, final String str, final int i2, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openMessage$39(progress, tLObject, j, i, baseFragment, bundle, chatActivity, str, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMessage$39(Browser.Progress progress, TLObject tLObject, long j, int i, BaseFragment baseFragment, Bundle bundle, ChatActivity chatActivity, String str, int i2) {
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
                    chatActivity.setHighlightQuote(i, str, i2);
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

    /* JADX WARN: Removed duplicated region for block: B:100:0x0461  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x05cd  */
    /* JADX WARN: Removed duplicated region for block: B:157:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap<String, String> hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final String str25, final String str26, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str27) {
        final int i5;
        final int[] iArr;
        char c;
        Runnable runnable;
        final AlertDialog alertDialog;
        final Browser.Progress progress2;
        BaseFragment baseFragment;
        final Runnable runnable2;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        EmojiPacksAlert emojiPacksAlert;
        StickersAlert stickersAlert;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda148
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i6) {
                    LaunchActivity.this.lambda$runLinkRequest$45(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, i6);
                }
            }).show();
            return;
        }
        if (str16 != null) {
            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
            int i6 = NotificationCenter.didReceiveSmsCode;
            if (globalInstance.hasObservers(i6)) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(i6, str16);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", R.string.OtherLoginCode, str16)));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        } else if (str17 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder2);
        } else {
            final AlertDialog alertDialog2 = new AlertDialog(this, 3);
            final Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$runLinkRequest$46(Browser.Progress.this, alertDialog2);
                }
            };
            final int[] iArr2 = {0};
            if (str10 != null) {
                TLRPC$TL_contacts_importContactToken tLRPC$TL_contacts_importContactToken = new TLRPC$TL_contacts_importContactToken();
                tLRPC$TL_contacts_importContactToken.token = str10;
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_importContactToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda121
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$48(i, str10, runnable3, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str11 != null) {
                TL_chatlists$TL_chatlists_checkChatlistInvite tL_chatlists$TL_chatlists_checkChatlistInvite = new TL_chatlists$TL_chatlists_checkChatlistInvite();
                tL_chatlists$TL_chatlists_checkChatlistInvite.slug = str11;
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tL_chatlists$TL_chatlists_checkChatlistInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda115
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.lambda$runLinkRequest$50(i, str11, runnable3, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str18 != null) {
                TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                final TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = str18;
                tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda139
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$55(tLRPC$TL_inputInvoiceSlug, runnable3, i, str18, tLObject, tLRPC$TL_error);
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
                            LaunchActivity.this.lambda$runLinkRequest$73(i4, runnable3, str13, str20, z2, str21, str25, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, i2, i3, str22, str23, str24, progress, z3, z4, str27, str26, iArr2, (Long) obj);
                        }
                    });
                    i5 = i;
                    iArr = iArr2;
                    alertDialog = alertDialog2;
                    progress2 = progress;
                    c = 0;
                } else if (str2 != null) {
                    if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        iArr = iArr2;
                        c = 0;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda127
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$76(i, alertDialog2, runnable3, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                    } else {
                        iArr = iArr2;
                        c = 0;
                        if (i2 == 1) {
                            TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                            tLRPC$TL_messages_importChatInvite.hash = str2;
                            i5 = i;
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda120
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$78(i5, runnable3, tLObject, tLRPC$TL_error);
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
                    String str28 = str3;
                    iArr = iArr2;
                    c = 0;
                    if (str28 != null) {
                        if (mainFragmentsStack.isEmpty()) {
                            return;
                        }
                        TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                        tLRPC$TL_inputStickerSetShortName.short_name = str28;
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
                        if (str28 == null) {
                            str28 = str4;
                        }
                        tLRPC$TL_inputStickerSetShortName2.short_name = str28;
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
                            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda158
                                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z5, TopicsFragment topicsFragment) {
                                    boolean lambda$runLinkRequest$79;
                                    lambda$runLinkRequest$79 = LaunchActivity.this.lambda$runLinkRequest$79(z, i5, str9, dialogsActivity2, arrayList4, charSequence, z5, topicsFragment);
                                    return lambda$runLinkRequest$79;
                                }
                            });
                            presentFragment(dialogsActivity, false, true);
                        } else if (hashMap != null) {
                            int intValue = Utilities.parseInt((CharSequence) hashMap.get("bot_id")).intValue();
                            if (intValue == 0) {
                                return;
                            }
                            final String str29 = hashMap.get("payload");
                            final String str30 = hashMap.get("nonce");
                            final String str31 = hashMap.get("callback_url");
                            final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
                            tLRPC$TL_account_getAuthorizationForm.bot_id = intValue;
                            tLRPC$TL_account_getAuthorizationForm.scope = hashMap.get("scope");
                            tLRPC$TL_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda144
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$83(iArr, i, runnable3, tLRPC$TL_account_getAuthorizationForm, str29, str30, str31, tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (str15 != null) {
                            TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                            tLRPC$TL_help_getDeepLinkInfo.path = str15;
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda131
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$85(runnable3, tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (str14 != null) {
                            TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                            tLRPC$TL_langpack_getLanguage.lang_code = str14;
                            tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda132
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$87(runnable3, tLObject, tLRPC$TL_error);
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
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.lambda$runLinkRequest$88(themePreviewActivity);
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
                                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda134
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                            LaunchActivity.this.lambda$runLinkRequest$90(runnable3, tLRPC$TL_wallPaper, tLObject, tLRPC$TL_error);
                                        }
                                    });
                                }
                            }
                            r3 = false;
                            if (!r3) {
                            }
                        } else if (str19 != null) {
                            progress2 = progress;
                            runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.this.lambda$runLinkRequest$91(progress2);
                                }
                            };
                            TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                            tLRPC$TL_account_getTheme.format = "android";
                            TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                            tLRPC$TL_inputThemeSlug.slug = str19;
                            tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                            alertDialog = alertDialog2;
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda140
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$93(alertDialog, runnable3, tLObject, tLRPC$TL_error);
                                }
                            });
                            if (iArr[c] != 0) {
                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda3
                                    @Override // android.content.DialogInterface.OnCancelListener
                                    public final void onCancel(DialogInterface dialogInterface) {
                                        LaunchActivity.lambda$runLinkRequest$102(i5, iArr, runnable2, dialogInterface);
                                    }
                                });
                                if (progress2 != null) {
                                    progress2.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.lambda$runLinkRequest$103(i5, iArr, runnable2);
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
                        } else {
                            alertDialog = alertDialog2;
                            progress2 = progress;
                            if (l == null || (num == null && !z4)) {
                                if (str27 != null) {
                                    TLRPC$TL_account_resolveBusinessChatLink tLRPC$TL_account_resolveBusinessChatLink = new TLRPC$TL_account_resolveBusinessChatLink();
                                    tLRPC$TL_account_resolveBusinessChatLink.slug = str27;
                                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_resolveBusinessChatLink, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda118
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                            LaunchActivity.this.lambda$runLinkRequest$101(tLObject, tLRPC$TL_error);
                                        }
                                    });
                                }
                            } else if (l2 != null) {
                                TLRPC$Chat chat = MessagesController.getInstance(i).getChat(l);
                                if (chat != null) {
                                    iArr[0] = runCommentRequest(i, runnable3, num, num2, l2, chat);
                                } else {
                                    TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                                    TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
                                    tLRPC$TL_inputChannel.channel_id = l.longValue();
                                    tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
                                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda143
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                            LaunchActivity.this.lambda$runLinkRequest$95(iArr, i, runnable3, num, num2, l2, tLObject, tLRPC$TL_error);
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
                                if (chat2 != null && ChatObject.isBoostSupported(chat2) && z4) {
                                    processBoostDialog(Long.valueOf(-l.longValue()), runnable3, progress2);
                                } else if (chat2 != null && chat2.forum) {
                                    openForumFromLink(-l.longValue(), num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.lambda$runLinkRequest$96(runnable3);
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
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$runLinkRequest$99(bundle2, l, iArr, runnable3, z4, progress, l2, num, baseFragment4, i);
                                            }
                                        });
                                    }
                                }
                            }
                            runnable2 = runnable;
                            if (iArr[c] != 0) {
                            }
                        }
                        alertDialog = alertDialog2;
                        progress2 = progress;
                        runnable2 = runnable;
                        if (iArr[c] != 0) {
                        }
                    }
                }
                runnable = null;
                runnable2 = runnable;
                if (iArr[c] != 0) {
                }
            }
            iArr = iArr2;
            progress2 = progress;
            runnable = null;
            c = 0;
            alertDialog = alertDialog2;
            i5 = i;
            runnable2 = runnable;
            if (iArr[c] != 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$45(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i3, boolean z4, String str27, int i4) {
        if (i4 != i) {
            switchToAccount(i4, true);
        }
        runLinkRequest(i4, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, 1, i2, str22, str23, str24, str25, str26, progress, z3, i3, z4, str27);
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
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
            lambda$runLinkRequest$88(new ChatActivity(bundle));
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
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
    public /* synthetic */ void lambda$runLinkRequest$55(final TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug, final Runnable runnable, final int i, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$54(tLRPC$TL_error, tLObject, tLRPC$TL_inputInvoiceSlug, runnable, i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$54(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug, final Runnable runnable, int i, String str) {
        PaymentFormActivity paymentFormActivity;
        if (tLRPC$TL_error != null) {
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.PaymentInvoiceLinkInvalid)).show();
        } else if (!isFinishing()) {
            if (tLObject instanceof TLRPC$TL_payments_paymentFormStars) {
                final Runnable runnable2 = this.navigateToPremiumGiftCallback;
                this.navigateToPremiumGiftCallback = null;
                StarsController.getInstance(this.currentAccount).openPaymentForm(null, tLRPC$TL_inputInvoiceSlug, (TLRPC$TL_payments_paymentFormStars) tLObject, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$runLinkRequest$51(runnable);
                    }
                }, new Utilities.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda111
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        LaunchActivity.lambda$runLinkRequest$52(runnable2, (String) obj);
                    }
                });
                return;
            }
            if (tLObject instanceof TLRPC$PaymentForm) {
                TLRPC$PaymentForm tLRPC$PaymentForm = (TLRPC$PaymentForm) tLObject;
                MessagesController.getInstance(i).putUsers(tLRPC$PaymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(tLRPC$PaymentForm, str, getActionBarLayout().getLastFragment());
            } else {
                paymentFormActivity = tLObject instanceof TLRPC$PaymentReceipt ? new PaymentFormActivity((TLRPC$PaymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                final Runnable runnable3 = this.navigateToPremiumGiftCallback;
                if (runnable3 != null) {
                    this.navigateToPremiumGiftCallback = null;
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda161
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$53(runnable3, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$88(paymentFormActivity);
            }
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$51(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$52(Runnable runnable, String str) {
        if (runnable == null || !"paid".equals(str)) {
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$53(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0268, code lost:
        if (r93[0] != 0) goto L7;
     */
    /* JADX WARN: Removed duplicated region for block: B:247:0x05f4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:254:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$73(final int i, final Runnable runnable, final String str, final String str2, final boolean z, final String str3, final String str4, final int i2, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final String str13, final String str14, final String str15, final String str16, final boolean z2, final Integer num, final Long l, final Long l2, final Integer num2, final HashMap hashMap, final String str17, final String str18, final String str19, final String str20, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str21, final String str22, final int i3, final int i4, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z3, final boolean z4, final String str26, final String str27, int[] iArr, final Long l3) {
        String str28;
        Integer num3;
        long longValue;
        String str29;
        boolean z5;
        BaseFragment baseFragment;
        boolean z6;
        boolean z7;
        final TLRPC$User user;
        if (isFinishing()) {
            return;
        }
        boolean z8 = true;
        if (i != 0 && l3 != null) {
            MessagesController.getInstance(this.currentAccount).getStoriesController().resolveStoryLink(l3.longValue(), i, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$runLinkRequest$56(runnable, l3, (TL_stories$StoryItem) obj);
                }
            });
        } else {
            if (l3 != null && this.actionBarLayout != null && ((str == null && str2 == null) || ((str != null && l3.longValue() > 0) || ((str2 != null && l3.longValue() > 0) || ((z && l3.longValue() < 0) || (str3 != null && l3.longValue() < 0)))))) {
                if (!TextUtils.isEmpty(str4) && (user = MessagesController.getInstance(i2).getUser(l3)) != null && user.bot) {
                    if (user.bot_attach_menu && !MediaDataController.getInstance(i2).botInAttachMenu(user.id)) {
                        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
                        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i2).getInputUser(l3.longValue());
                        ConnectionsManager.getInstance(i2).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda122
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$65(i2, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, z2, num, l, l2, num2, str, hashMap, str17, str18, str19, str20, tLRPC$TL_wallPaper, str21, str22, str2, z, str3, i3, i4, str23, str24, str25, progress, z3, i, z4, str26, l3, str4, str27, user, runnable, tLObject, tLRPC$TL_error);
                            }
                        });
                        return;
                    }
                    processWebAppBot(i2, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, z2, num, l, l2, num2, str, hashMap, str17, str18, str19, str20, tLRPC$TL_wallPaper, str21, str22, str2, z, str3, i3, i4, str23, str24, str25, str4, str27, progress, z3, i, z4, str26, user, runnable, false, false);
                    return;
                } else if (z4 && ChatObject.isBoostSupported(MessagesController.getInstance(i2).getChat(Long.valueOf(-l3.longValue())))) {
                    processBoostDialog(l3, runnable, progress);
                    return;
                } else {
                    if (str23 == null && str27 == null) {
                        num3 = num;
                        str28 = str24;
                    } else {
                        str28 = str24;
                        if (str28 == null) {
                            TLRPC$User user2 = MessagesController.getInstance(i2).getUser(l3);
                            if (user2 != null && user2.bot) {
                                if (user2.bot_attach_menu) {
                                    processAttachMenuBot(i2, l3.longValue(), str25, user2, str23, str27);
                                } else {
                                    ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                                    BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
                                }
                            } else {
                                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotSetAttachLinkNotBot)).show();
                            }
                        } else {
                            num3 = num;
                        }
                    }
                    if (num3 != null && ((num2 != null || l2 != null) && l3.longValue() < 0)) {
                        iArr[0] = runCommentRequest(i2, runnable, num, num2, l2, MessagesController.getInstance(i2).getChat(Long.valueOf(-l3.longValue())));
                    } else if (str != null) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("onlySelect", true);
                        bundle.putBoolean("cantSendToChannels", true);
                        bundle.putInt("dialogsType", 1);
                        bundle.putString("selectAlertString", LocaleController.getString("SendGameToText", R.string.SendGameToText));
                        bundle.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", R.string.SendGameToGroupText));
                        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                        final TLRPC$User user3 = MessagesController.getInstance(i2).getUser(l3);
                        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda156
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z9, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$66;
                                lambda$runLinkRequest$66 = LaunchActivity.this.lambda$runLinkRequest$66(str, i2, user3, dialogsActivity2, arrayList3, charSequence, z9, topicsFragment);
                                return lambda$runLinkRequest$66;
                            }
                        });
                        getActionBarLayout().presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), true, true, false);
                        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                            z7 = false;
                            SecretMediaViewer.getInstance().closePhoto(false, false);
                        } else {
                            z7 = false;
                            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                                PhotoViewer.getInstance().closePhoto(false, true);
                            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                                ArticleViewer.getInstance().close(false, true);
                            }
                        }
                        StoryRecorder.destroyInstance();
                        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                        if (groupCallActivity != null) {
                            groupCallActivity.dismiss();
                        }
                        this.drawerLayoutContainer.setAllowOpenDrawer(z7, z7);
                        if (AndroidUtilities.isTablet()) {
                            this.actionBarLayout.rebuildFragments(1);
                            this.rightActionBarLayout.rebuildFragments(1);
                        } else {
                            this.drawerLayoutContainer.setAllowOpenDrawer(true, z7);
                        }
                    } else if (str10 != null || str11 != null) {
                        final TLRPC$User user4 = MessagesController.getInstance(i2).getUser(l3);
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
                        String str30 = TextUtils.isEmpty(str10) ? TextUtils.isEmpty(str11) ? null : str11 : str10;
                        final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle2);
                        final String str31 = str30;
                        dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda155
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z9, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$71;
                                lambda$runLinkRequest$71 = LaunchActivity.this.lambda$runLinkRequest$71(i2, user4, str12, str31, dialogsActivity2, dialogsActivity3, arrayList4, charSequence, z9, topicsFragment);
                                return lambda$runLinkRequest$71;
                            }
                        });
                        lambda$runLinkRequest$88(dialogsActivity2);
                    } else {
                        Bundle bundle3 = new Bundle();
                        TLRPC$User user5 = MessagesController.getInstance(i2).getUser(l3);
                        if (l3.longValue() < 0) {
                            bundle3.putLong("chat_id", -l3.longValue());
                            longValue = l3.longValue();
                        } else {
                            bundle3.putLong("user_id", l3.longValue());
                            longValue = l3.longValue();
                            if (str16 != null) {
                                if (str16.startsWith("@")) {
                                    str29 = " " + str16;
                                } else {
                                    str29 = str16;
                                }
                                bundle3.putString("start_text", str29);
                            }
                        }
                        if (str9 == null || user5 == null || !user5.bot) {
                            z5 = false;
                        } else {
                            bundle3.putString("botUser", str9);
                            z5 = true;
                        }
                        if (this.navigateToPremiumBot) {
                            this.navigateToPremiumBot = false;
                            bundle3.putBoolean("premium_bot", true);
                        }
                        if (num3 != null) {
                            bundle3.putInt("message_id", num.intValue());
                        }
                        if (str2 != null) {
                            bundle3.putString("voicechat", str2);
                        }
                        if (z) {
                            bundle3.putBoolean("videochat", true);
                        }
                        if (str3 != null) {
                            bundle3.putString("livestream", str3);
                        }
                        if (i4 >= 0) {
                            bundle3.putInt("video_timestamp", i4);
                        }
                        if (str28 != null) {
                            bundle3.putString("attach_bot", str28);
                        }
                        if (str23 != null) {
                            bundle3.putString("attach_bot_start_command", str23);
                        }
                        if (mainFragmentsStack.isEmpty() || str2 != null) {
                            baseFragment = null;
                        } else {
                            ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                            baseFragment = arrayList4.get(arrayList4.size() - 1);
                        }
                        if (baseFragment == null || MessagesController.getInstance(i2).checkCanOpenChat(bundle3, baseFragment)) {
                            boolean z9 = (baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == longValue;
                            if (z5 && z9) {
                                ((ChatActivity) baseFragment).setBotUser(str9);
                            } else if (str28 != null && z9) {
                                ((ChatActivity) baseFragment).openAttachBotLayout(str28);
                            } else {
                                long j = -longValue;
                                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                if (chat != null && chat.forum) {
                                    Long valueOf = (l2 != null || num3 == null) ? l2 : Long.valueOf(num.intValue());
                                    if (valueOf != null && valueOf.longValue() != 0) {
                                        openForumFromLink(longValue, num3, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.lambda$runLinkRequest$72(runnable);
                                            }
                                        });
                                    } else {
                                        Bundle bundle4 = new Bundle();
                                        bundle4.putLong("chat_id", j);
                                        lambda$runLinkRequest$88(TopicsFragment.getTopicsOrChat(this, bundle4));
                                        try {
                                            runnable.run();
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                } else {
                                    MessagesController.getInstance(i2).ensureMessagesLoaded(longValue, num3 == null ? 0 : num.intValue(), new 18(runnable, str3, baseFragment, longValue, num, bundle3));
                                }
                                z6 = false;
                                z8 = z6;
                            }
                        }
                        z6 = true;
                        z8 = z6;
                    }
                    if (z8) {
                        try {
                            runnable.run();
                            return;
                        } catch (Exception e3) {
                            FileLog.e(e3);
                            return;
                        }
                    }
                    return;
                }
            }
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
            } catch (Exception e4) {
                FileLog.e(e4);
            }
            z8 = true;
            if (z8) {
            }
        }
        z8 = false;
        if (z8) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$56(Runnable runnable, Long l, TL_stories$StoryItem tL_stories$StoryItem) {
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
            StoryViewer createOverlayStoryViewer = lastFragment.createOverlayStoryViewer();
            createOverlayStoryViewer.instantClose();
            createOverlayStoryViewer.open(this, tL_stories$StoryItem, (StoryViewer.PlaceProvider) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$65(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str25, final Long l3, final String str26, final String str27, final TLRPC$User tLRPC$User, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$64(tLRPC$TL_error, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25, tLObject, l3, str26, str27, tLRPC$User, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$57(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, Browser.Progress progress, boolean z3, int i4, boolean z4, String str25) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, null, null, progress, z3, i4, z4, str25);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$64(TLRPC$TL_error tLRPC$TL_error, final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str25, TLObject tLObject, final Long l3, final String str26, final String str27, final TLRPC$User tLRPC$User, final Runnable runnable) {
        if (tLRPC$TL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$57(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25);
                }
            });
        } else if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = ((TLRPC$TL_attachMenuBotsBot) tLObject).bot;
            final boolean z5 = tLRPC$TL_attachMenuBot != null && (tLRPC$TL_attachMenuBot.show_in_side_menu || tLRPC$TL_attachMenuBot.show_in_attach_menu);
            if ((tLRPC$TL_attachMenuBot.inactive || tLRPC$TL_attachMenuBot.side_menu_disclaimer_needed) && z5) {
                WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        LaunchActivity.this.lambda$runLinkRequest$60(tLRPC$TL_attachMenuBot, i, l3, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, tLRPC$User, runnable, z5, (Boolean) obj);
                    }
                }, null);
            } else if (tLRPC$TL_attachMenuBot.request_write_access || z3) {
                final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                AlertsCreator.createBotLaunchAlert(getLastFragment(), atomicBoolean, tLRPC$User, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.lambda$runLinkRequest$63(tLRPC$TL_attachMenuBot, atomicBoolean, i, l3, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, tLRPC$User, runnable);
                    }
                });
            } else {
                processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, tLRPC$User, runnable, false, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$60(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, final int i, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l2, Long l3, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i4, boolean z4, String str27, TLRPC$User tLRPC$User, Runnable runnable, boolean z5, Boolean bool) {
        tLRPC$TL_attachMenuBot.inactive = false;
        tLRPC$TL_attachMenuBot.request_write_access = false;
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda113
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$runLinkRequest$59(i, tLObject, tLRPC$TL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l2, l3, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, tLRPC$User, runnable, z5, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$59(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$58(TLObject.this, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$58(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$63(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, AtomicBoolean atomicBoolean, final int i, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l2, Long l3, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i4, boolean z4, String str27, TLRPC$User tLRPC$User, Runnable runnable) {
        tLRPC$TL_attachMenuBot.inactive = false;
        tLRPC$TL_attachMenuBot.request_write_access = !atomicBoolean.get();
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = atomicBoolean.get();
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda114
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$runLinkRequest$62(i, tLObject, tLRPC$TL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l2, l3, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, tLRPC$User, runnable, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$62(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$61(TLObject.this, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$61(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runLinkRequest$66(String str, int i, TLRPC$User tLRPC$User, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
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
    public /* synthetic */ boolean lambda$runLinkRequest$71(final int i, final TLRPC$User tLRPC$User, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        final TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(false, chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda109
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$69(str, str2, i, chat, dialogsActivity, tLRPC$User, j, z2, tLRPC$TL_chatAdminRights2, str3);
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
                    LaunchActivity.this.lambda$runLinkRequest$70(j, i, tLRPC$User, str2, dialogInterface, i3);
                }
            });
            builder.show();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$69(final String str, final String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, final TLRPC$User tLRPC$User, final long j, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$68(str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$68(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
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
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$67(i, tLRPC$Chat, dialogsActivity);
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
    public /* synthetic */ void lambda$runLinkRequest$67(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            presentFragment(new ChatActivity(bundle), true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$70(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
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
    public static /* synthetic */ void lambda$runLinkRequest$72(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
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
    public /* synthetic */ void lambda$runLinkRequest$76(final int i, final AlertDialog alertDialog, final Runnable runnable, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$75(tLRPC$TL_error, tLObject, i, alertDialog, runnable, str);
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
    public /* synthetic */ void lambda$runLinkRequest$75(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, AlertDialog alertDialog, final Runnable runnable, String str) {
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
                        LaunchActivity.lambda$runLinkRequest$74(zArr, dialogInterface);
                    }
                });
                if (tLRPC$ChatInvite.chat.forum) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                    lambda$runLinkRequest$88(TopicsFragment.getTopicsOrChat(this, bundle2));
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
    public static /* synthetic */ void lambda$runLinkRequest$74(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$78(final int i, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$77(runnable, tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$77(Runnable runnable, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
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
    public /* synthetic */ boolean lambda$runLinkRequest$79(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2, TopicsFragment topicsFragment) {
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
            MediaDataController.getInstance(i).saveDraft(j, 0, str, null, null, false, 0L);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$83(int[] iArr, final int i, final Runnable runnable, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda133
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$81(runnable, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3, tLObject2, tLRPC$TL_error2);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$82(runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$81(final Runnable runnable, final int i, final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$80(runnable, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$80(Runnable runnable, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$88(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$account_Password) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$82(Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$85(final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$84(runnable, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$84(Runnable runnable, TLObject tLObject) {
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
    public /* synthetic */ void lambda$runLinkRequest$87(final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$86(runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$86(Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$90(final Runnable runnable, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$89(runnable, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$89(Runnable runnable, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
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
            lambda$runLinkRequest$88(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$91(Browser.Progress progress) {
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
    public /* synthetic */ void lambda$runLinkRequest$93(final AlertDialog alertDialog, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$92(tLObject, alertDialog, runnable, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$92(TLObject tLObject, AlertDialog alertDialog, Runnable runnable, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$95(final int[] iArr, final int i, final Runnable runnable, final Integer num, final Integer num2, final Long l, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$94(tLObject, iArr, i, runnable, num, num2, l);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$94(TLObject tLObject, int[] iArr, int i, Runnable runnable, Integer num, Integer num2, Long l) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                iArr[0] = runCommentRequest(i, runnable, num, num2, l, tLRPC$TL_messages_chats.chats.get(0));
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
    public static /* synthetic */ void lambda$runLinkRequest$96(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$99(final Bundle bundle, final Long l, int[] iArr, final Runnable runnable, final boolean z, final Browser.Progress progress, final Long l2, final Integer num, final BaseFragment baseFragment, final int i) {
        if (getActionBarLayout().presentFragment(new ChatActivity(bundle))) {
            return;
        }
        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
        tLRPC$TL_inputChannel.channel_id = l.longValue();
        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda135
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$98(runnable, z, l, progress, l2, num, baseFragment, i, bundle, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$98(final Runnable runnable, final boolean z, final Long l, final Browser.Progress progress, final Long l2, final Integer num, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$97(runnable, tLObject, z, l, progress, l2, num, baseFragment, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$97(Runnable runnable, TLObject tLObject, boolean z, Long l, Browser.Progress progress, Long l2, Integer num, BaseFragment baseFragment, int i, Bundle bundle) {
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
                if (tLRPC$Chat != null && z && ChatObject.isBoostSupported(tLRPC$Chat)) {
                    processBoostDialog(Long.valueOf(-l.longValue()), null, progress);
                } else if (tLRPC$Chat != null && tLRPC$Chat.forum) {
                    if (l2 != null) {
                        openForumFromLink(-l.longValue(), num, null);
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
    public /* synthetic */ void lambda$runLinkRequest$101(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$100(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$100(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_account_resolvedBusinessChatLinks) {
            TLRPC$TL_account_resolvedBusinessChatLinks tLRPC$TL_account_resolvedBusinessChatLinks = (TLRPC$TL_account_resolvedBusinessChatLinks) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_account_resolvedBusinessChatLinks.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_account_resolvedBusinessChatLinks.chats, false);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$TL_account_resolvedBusinessChatLinks.users, tLRPC$TL_account_resolvedBusinessChatLinks.chats, true, true);
            Bundle bundle = new Bundle();
            TLRPC$Peer tLRPC$Peer = tLRPC$TL_account_resolvedBusinessChatLinks.peer;
            if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                bundle.putLong("user_id", tLRPC$Peer.user_id);
            } else if ((tLRPC$Peer instanceof TLRPC$TL_peerChat) || (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
                bundle.putLong("chat_id", tLRPC$Peer.channel_id);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setResolvedChatLink(tLRPC$TL_account_resolvedBusinessChatLinks);
            presentFragment(chatActivity, false, true);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString(R.string.BusinessLink), LocaleController.getString(R.string.BusinessLinkInvalid)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$102(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$103(int i, int[] iArr, Runnable runnable) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void processWebAppBot(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap<String, String> hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, String str25, final String str26, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str27, final TLRPC$User tLRPC$User, final Runnable runnable, final boolean z5, final boolean z6) {
        TLRPC$TL_messages_getBotApp tLRPC$TL_messages_getBotApp = new TLRPC$TL_messages_getBotApp();
        TLRPC$TL_inputBotAppShortName tLRPC$TL_inputBotAppShortName = new TLRPC$TL_inputBotAppShortName();
        tLRPC$TL_inputBotAppShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$User);
        tLRPC$TL_inputBotAppShortName.short_name = str25;
        tLRPC$TL_messages_getBotApp.app = tLRPC$TL_inputBotAppShortName;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getBotApp, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda136
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processWebAppBot$108(progress, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, z3, i4, z4, str27, runnable, tLRPC$User, str26, z6, z5, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$108(final Browser.Progress progress, final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final boolean z3, final int i4, final boolean z4, final String str25, final Runnable runnable, final TLRPC$User tLRPC$User, final String str26, final boolean z5, final boolean z6, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (progress != null) {
            progress.end();
        }
        if (tLRPC$TL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processWebAppBot$104(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25);
                }
            });
            return;
        }
        final TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp = (TLRPC$TL_messages_botApp) tLObject;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processWebAppBot$107(runnable, i, tLRPC$User, tLRPC$TL_messages_botApp, str26, z3, z5, z6);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$104(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, Browser.Progress progress, boolean z3, int i4, boolean z4, String str25) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tLRPC$TL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, null, null, progress, z3, i4, z4, str25);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processWebAppBot$107(Runnable runnable, final int i, final TLRPC$User tLRPC$User, final TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp, final String str, final boolean z, boolean z2, boolean z3) {
        BaseFragment baseFragment;
        ArrayList<BaseFragment> arrayList;
        runnable.run();
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            baseFragment = null;
        } else {
            baseFragment = mainFragmentsStack.get(arrayList.size() - 1);
        }
        final BaseFragment baseFragment2 = baseFragment;
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processWebAppBot$105(baseFragment2, i, tLRPC$User, tLRPC$TL_messages_botApp, atomicBoolean, str, z);
            }
        };
        if (z2) {
            runnable2.run();
        } else if (tLRPC$TL_messages_botApp.inactive && z3) {
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
    public /* synthetic */ void lambda$processWebAppBot$105(BaseFragment baseFragment, int i, TLRPC$User tLRPC$User, TLRPC$TL_messages_botApp tLRPC$TL_messages_botApp, AtomicBoolean atomicBoolean, String str, boolean z) {
        if (baseFragment == null || !isActive || isFinishing() || isDestroyed()) {
            return;
        }
        BotWebViewAttachedSheet createBotViewer = baseFragment.createBotViewer();
        createBotViewer.setParentActivity(this);
        long j = tLRPC$User.id;
        createBotViewer.requestWebView(i, j, j, null, null, 3, 0, false, baseFragment, tLRPC$TL_messages_botApp.app, atomicBoolean.get(), str, tLRPC$User);
        createBotViewer.show();
        if (tLRPC$TL_messages_botApp.inactive || z) {
            createBotViewer.showJustAddedBulletin();
        }
    }

    private void processAttachedMenuBotFromShortcut(long j) {
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            if (this.visibleDialogs.get(i) instanceof BotWebViewSheet) {
                BotWebViewSheet botWebViewSheet = (BotWebViewSheet) this.visibleDialogs.get(i);
                if (botWebViewSheet.isShowing() && botWebViewSheet.getBotId() == j) {
                    return;
                }
            }
        }
        final AtomicBoolean atomicBoolean = new AtomicBoolean(MediaDataController.getInstance(this.currentAccount).isMenuBotsUpdatedLocal());
        if (!atomicBoolean.get()) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processAttachedMenuBotFromShortcut$109(atomicBoolean, countDownLatch);
                }
            });
            try {
                countDownLatch.await();
            } catch (Exception e) {
                FileLog.e(e);
                return;
            }
        }
        if (atomicBoolean.get()) {
            TLRPC$TL_attachMenuBots attachMenuBots = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots();
            if (attachMenuBots.bots.isEmpty()) {
                MediaDataController.getInstance(this.currentAccount).uninstallShortcut(j, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
                return;
            }
            for (int i2 = 0; i2 < attachMenuBots.bots.size(); i2++) {
                if (attachMenuBots.bots.get(i2).bot_id == j) {
                    if (getLastFragment() != null) {
                        showAttachMenuBot(attachMenuBots.bots.get(i2), null);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachedMenuBotFromShortcut$109(AtomicBoolean atomicBoolean, CountDownLatch countDownLatch) {
        atomicBoolean.set(MediaDataController.getInstance(this.currentAccount).isMenuBotsUpdatedLocal());
        countDownLatch.countDown();
    }

    private void processBoostDialog(Long l, Runnable runnable, Browser.Progress progress) {
        processBoostDialog(l, runnable, progress, null);
    }

    private void processBoostDialog(final Long l, final Runnable runnable, final Browser.Progress progress, final ChatMessageCell chatMessageCell) {
        final ChannelBoostsController boostsController = MessagesController.getInstance(this.currentAccount).getBoostsController();
        if (progress != null) {
            progress.init();
        }
        boostsController.getBoostsStats(l.longValue(), new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LaunchActivity.this.lambda$processBoostDialog$111(progress, runnable, boostsController, l, chatMessageCell, (TL_stories$TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processBoostDialog$111(final Browser.Progress progress, final Runnable runnable, ChannelBoostsController channelBoostsController, final Long l, final ChatMessageCell chatMessageCell, final TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        if (tL_stories$TL_premium_boostsStatus != null) {
            channelBoostsController.userCanBoostChannel(l.longValue(), tL_stories$TL_premium_boostsStatus, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$processBoostDialog$110(progress, l, tL_stories$TL_premium_boostsStatus, chatMessageCell, runnable, (ChannelBoostsController.CanApplyBoost) obj);
                }
            });
            return;
        }
        if (progress != null) {
            progress.end();
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004b, code lost:
        if (((org.telegram.ui.ChatActivity) r8).getDialogId() == r9.longValue()) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0066, code lost:
        if (r13.getCurrentFragmetDialogId() == r9.longValue()) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processBoostDialog$110(Browser.Progress progress, Long l, TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, ChatMessageCell chatMessageCell, Runnable runnable, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        if (progress != null) {
            progress.end();
        }
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null) {
            return;
        }
        Theme.ResourcesProvider resourceProvider = lastFragment.getResourceProvider();
        if (lastFragment.getLastStoryViewer() != null && lastFragment.getLastStoryViewer().isFullyVisible()) {
            resourceProvider = lastFragment.getLastStoryViewer().getResourceProvider();
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(lastFragment, this, 19, this.currentAccount, resourceProvider);
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        boolean z = true;
        boolean z2 = false;
        if (!(lastFragment instanceof ChatActivity)) {
            if (lastFragment instanceof DialogsActivity) {
                RightSlidingDialogContainer rightSlidingDialogContainer = ((DialogsActivity) lastFragment).rightSlidingDialogContainer;
                if (rightSlidingDialogContainer != null) {
                }
                z = false;
                z2 = z;
            }
        }
        limitReachedBottomSheet.setBoostsStats(tL_stories$TL_premium_boostsStatus, z2);
        limitReachedBottomSheet.setDialogId(l.longValue());
        limitReachedBottomSheet.setChatMessageCell(chatMessageCell);
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
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda123
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$118(i, str3, str, tLRPC$User, str2, j, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$118(final int i, final String str, final String str2, final TLRPC$User tLRPC$User, final String str3, final long j, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$117(tLObject, i, str, str2, tLRPC$User, str3, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$117(TLObject tLObject, final int i, String str, String str2, final TLRPC$User tLRPC$User, final String str3, final long j) {
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
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda157
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                        boolean lambda$processAttachMenuBot$112;
                        lambda$processAttachMenuBot$112 = LaunchActivity.this.lambda$processAttachMenuBot$112(tLRPC$User2, str3, i, dialogsActivity3, arrayList4, charSequence, z, topicsFragment);
                        return lambda$processAttachMenuBot$112;
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
                        LaunchActivity.this.lambda$processAttachMenuBot$116(i, j, dialogsActivity3, baseFragment2, tLRPC$User, str3, (Boolean) obj);
                    }
                };
                if (!tLRPC$TL_attachMenuBot.request_write_access) {
                    tLRPC$User2 = null;
                }
                WebAppDisclaimerAlert.show(this, consumer, tLRPC$User2);
                return;
            } else if (dialogsActivity != null) {
                if (baseFragment2 != null) {
                    baseFragment2.dismissCurrentDialog();
                }
                for (int i2 = 0; i2 < this.visibleDialogs.size(); i2++) {
                    if (this.visibleDialogs.get(i2).isShowing()) {
                        this.visibleDialogs.get(i2).dismiss();
                    }
                }
                this.visibleDialogs.clear();
                lambda$runLinkRequest$88(dialogsActivity);
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
    public /* synthetic */ boolean lambda$processAttachMenuBot$112(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
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
    public /* synthetic */ void lambda$processAttachMenuBot$116(final int i, long j, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, Boolean bool) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(j);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        tLRPC$TL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda128
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$115(i, dialogsActivity, baseFragment, tLRPC$User, str, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$115(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$114(tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$114(TLObject tLObject, int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processAttachMenuBot$113(dialogsActivity, baseFragment, tLRPC$User, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAttachMenuBot$113(DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (dialogsActivity != null) {
            if (baseFragment != null) {
                baseFragment.dismissCurrentDialog();
            }
            for (int i = 0; i < this.visibleDialogs.size(); i++) {
                if (this.visibleDialogs.get(i).isShowing()) {
                    this.visibleDialogs.get(i).dismiss();
                }
            }
            this.visibleDialogs.clear();
            lambda$runLinkRequest$88(dialogsActivity);
        } else if (baseFragment instanceof ChatActivity) {
            ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str, true);
        }
    }

    private void openForumFromLink(long j, Integer num, Runnable runnable) {
        openForumFromLink(j, num, null, runnable, 0, -1);
    }

    private void openForumFromLink(final long j, final Integer num, final String str, final Runnable runnable, final int i, final int i2) {
        if (num == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            lambda$runLinkRequest$88(TopicsFragment.getTopicsOrChat(this, bundle));
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
        tLRPC$TL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(-j);
        tLRPC$TL_channels_getMessages.id.add(num);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda130
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openForumFromLink$120(num, j, runnable, str, i, i2, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$120(final Integer num, final long j, final Runnable runnable, final String str, final int i, final int i2, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openForumFromLink$119(tLObject, num, j, runnable, str, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$119(TLObject tLObject, Integer num, long j, Runnable runnable, String str, int i, int i2) {
        TLRPC$Message tLRPC$Message;
        if (tLObject instanceof TLRPC$messages_Messages) {
            ArrayList<TLRPC$Message> arrayList = ((TLRPC$messages_Messages) tLObject).messages;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (arrayList.get(i3) != null && arrayList.get(i3).id == num.intValue()) {
                    tLRPC$Message = arrayList.get(i3);
                    break;
                }
            }
        }
        tLRPC$Message = null;
        if (tLRPC$Message != null) {
            int i4 = this.currentAccount;
            Integer valueOf = Integer.valueOf(tLRPC$Message.id);
            int i5 = this.currentAccount;
            runCommentRequest(i4, null, valueOf, null, Long.valueOf(MessageObject.getTopicId(i5, tLRPC$Message, MessagesController.getInstance(i5).isForum(tLRPC$Message))), MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)), runnable, str, i, i2);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$88(TopicsFragment.getTopicsOrChat(this, bundle));
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

    public void checkAppUpdate(boolean z, final Browser.Progress progress) {
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
                    final int sendRequest = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda124
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$checkAppUpdate$124(i, progress, tLObject, tLRPC$TL_error);
                        }
                    });
                    if (progress != null) {
                        progress.init();
                        progress.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkAppUpdate$125(sendRequest);
                            }
                        });
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$124(final int i, final Browser.Progress progress, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            final TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$121(tLRPC$TL_help_appUpdate, i, progress);
                }
            });
        } else if (tLObject instanceof TLRPC$TL_help_noAppUpdate) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$checkAppUpdate$122(Browser.Progress.this);
                }
            });
        } else if (tLRPC$TL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$checkAppUpdate$123(Browser.Progress.this, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$121(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i, Browser.Progress progress) {
        BaseFragment lastFragment;
        TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate2 = SharedConfig.pendingAppUpdate;
        if (tLRPC$TL_help_appUpdate2 == null || !tLRPC$TL_help_appUpdate2.version.equals(tLRPC$TL_help_appUpdate.version)) {
            boolean newAppVersionAvailable = SharedConfig.setNewAppVersionAvailable(tLRPC$TL_help_appUpdate);
            if (newAppVersionAvailable) {
                if (tLRPC$TL_help_appUpdate.can_not_skip) {
                    showUpdateActivity(i, tLRPC$TL_help_appUpdate, false);
                } else if (ApplicationLoader.isStandaloneBuild() || BuildVars.DEBUG_VERSION) {
                    this.drawerLayoutAdapter.notifyDataSetChanged();
                    ApplicationLoader.applicationLoaderInstance.showUpdateAppPopup(this, tLRPC$TL_help_appUpdate, i);
                }
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
            }
            if (progress != null) {
                progress.end();
                if (newAppVersionAvailable || (lastFragment = getLastFragment()) == null) {
                    return;
                }
                BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(R.string.YourVersionIsLatest)).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkAppUpdate$122(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
            BaseFragment lastFragment = getLastFragment();
            if (lastFragment != null) {
                BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(R.string.YourVersionIsLatest)).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkAppUpdate$123(Browser.Progress progress, TLRPC$TL_error tLRPC$TL_error) {
        if (progress != null) {
            progress.end();
            BaseFragment lastFragment = getLastFragment();
            if (lastFragment != null) {
                BulletinFactory.of(lastFragment).showForError(tLRPC$TL_error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$125(int i) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    public Dialog showAlertDialog(AlertDialog.Builder builder) {
        try {
            final AlertDialog show = builder.show();
            show.setCanceledOnTouchOutside(true);
            show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$showAlertDialog$126(show, dialogInterface);
                }
            });
            this.visibleDialogs.add(show);
            return show;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlertDialog$126(AlertDialog alertDialog, DialogInterface dialogInterface) {
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                ActionBarLayout actionBarLayout = this.actionBarLayout;
                BaseFragment lastFragment = actionBarLayout == null ? null : actionBarLayout.getLastFragment();
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
        this.visibleDialogs.remove(alertDialog);
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

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void onNewIntent(Intent intent, Browser.Progress progress) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false, progress);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0363  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0422 A[LOOP:2: B:205:0x041a->B:207:0x0422, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x044c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0463 A[ADDED_TO_REGION, SYNTHETIC] */
    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<MessagesStorage.TopicKey> arrayList, final CharSequence charSequence, final boolean z, TopicsFragment topicsFragment) {
        ChatActivity chatActivity;
        boolean z2;
        ChatActivity chatActivity2;
        int i;
        boolean z3;
        MessageObject messageObject;
        long j;
        long j2;
        ChatActivity chatActivity3;
        String str;
        boolean z4;
        boolean z5;
        boolean z6;
        String str2;
        int size;
        ArrayList<Uri> arrayList2;
        String str3;
        ArrayList<TLRPC$User> arrayList3;
        int i2;
        String str4;
        boolean z7;
        boolean z8;
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList4;
        final int currentAccount = dialogsActivity != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        final Uri uri = this.exportingChatUri;
        boolean z9 = true;
        if (uri != null) {
            ArrayList arrayList5 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            final AlertDialog alertDialog = new AlertDialog(this, 3);
            final ArrayList arrayList6 = arrayList5;
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).dialogId, this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda110
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j3) {
                    LaunchActivity.this.lambda$didSelectDialogs$127(currentAccount, dialogsActivity, z, arrayList6, uri, alertDialog, j3);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            final boolean z10 = dialogsActivity == null || dialogsActivity.notify;
            if (arrayList.size() <= 1) {
                long j3 = arrayList.get(0).dialogId;
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(j3)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j3));
                } else if (DialogObject.isUserDialog(j3)) {
                    bundle.putLong("user_id", j3);
                } else {
                    bundle.putLong("chat_id", -j3);
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
            ArrayList<TLRPC$User> arrayList7 = this.contactsToSend;
            int size2 = arrayList7 != null ? arrayList7.size() + 0 : 0;
            if (this.videoPath != null) {
                size2++;
            }
            if (this.voicePath != null) {
                size2++;
            }
            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList8 = this.photoPathsArray;
            if (arrayList8 != null) {
                size2 += arrayList8.size();
            }
            ArrayList<String> arrayList9 = this.documentsPathsArray;
            if (arrayList9 != null) {
                size2 += arrayList9.size();
            }
            ArrayList<Uri> arrayList10 = this.documentsUrisArray;
            if (arrayList10 != null) {
                size2 += arrayList10.size();
            }
            if (this.videoPath == null && this.voicePath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                size2++;
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, arrayList.get(i3).dialogId, size2 > 1)) {
                    return false;
                }
            }
            if (topicsFragment != null) {
                topicsFragment.removeSelfFromStack();
            }
            ArrayList<TLRPC$User> arrayList11 = this.contactsToSend;
            if (arrayList11 != null && arrayList11.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList12 = mainFragmentsStack;
                PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(arrayList12.get(arrayList12.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                final ChatActivity chatActivity5 = chatActivity;
                phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda149
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z11, int i4, long j4, boolean z12) {
                        LaunchActivity.this.lambda$didSelectDialogs$128(chatActivity5, arrayList, charSequence, currentAccount, z10, tLRPC$User, z11, i4, j4, z12);
                    }

                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public /* synthetic */ void didSelectContacts(ArrayList arrayList13, String str5, boolean z11, int i4, long j4, boolean z12) {
                        ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate.-CC.$default$didSelectContacts(this, arrayList13, str5, z11, i4, j4, z12);
                    }
                });
                ArrayList<BaseFragment> arrayList13 = mainFragmentsStack;
                arrayList13.get(arrayList13.size() - 1).showDialog(phonebookShareAlert);
                chatActivity2 = chatActivity;
                z2 = true;
            } else {
                int i4 = 0;
                z2 = false;
                String str5 = null;
                while (i4 < arrayList.size()) {
                    long j4 = arrayList.get(i4).dialogId;
                    long j5 = arrayList.get(i4).topicId;
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (j5 != 0) {
                        i = i4;
                        TLRPC$TL_forumTopic findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j4, j5);
                        if (findTopic != null && findTopic.topicStartMessage != null) {
                            z3 = z2;
                            messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                            messageObject.isTopicMainMessage = z9;
                            if (chatActivity == null) {
                                j = j5;
                                j2 = j4;
                                chatActivity3 = chatActivity;
                                getActionBarLayout().presentFragment(chatActivity, dialogsActivity != null, dialogsActivity == null || this.videoPath != null || ((arrayList4 = this.photoPathsArray) != null && arrayList4.size() > 0), true, false);
                                boolean z11 = dialogsActivity != null;
                                String str6 = this.videoPath;
                                if (str6 != null && j == 0) {
                                    chatActivity3.openVideoEditor(str6, this.sendingText);
                                    this.sendingText = null;
                                    z7 = false;
                                    z8 = true;
                                } else {
                                    ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList14 = this.photoPathsArray;
                                    if (arrayList14 == null || arrayList14.size() <= 0 || j != 0) {
                                        z7 = false;
                                    } else {
                                        z7 = chatActivity3.openPhotosEditor(this.photoPathsArray, (charSequence == null || charSequence.length() == 0) ? this.sendingText : charSequence);
                                        if (z7) {
                                            this.sendingText = null;
                                        }
                                    }
                                    z8 = false;
                                }
                                str = str5;
                                z5 = z8;
                                z6 = z11;
                                z4 = z7;
                            } else {
                                j = j5;
                                j2 = j4;
                                chatActivity3 = chatActivity;
                                if (this.videoPath != null) {
                                    String str7 = this.sendingText;
                                    if (str7 != null && str7.length() <= 1024) {
                                        str5 = this.sendingText;
                                        this.sendingText = null;
                                    }
                                    ArrayList arrayList15 = new ArrayList();
                                    arrayList15.add(this.videoPath);
                                    SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList15, arrayList15, null, str5, null, j2, messageObject, messageObject, null, null, null, z10, 0, null, null, 0, 0L, false);
                                }
                                str = str5;
                                z4 = false;
                                z5 = false;
                                z6 = z3;
                            }
                            if (this.photoPathsArray != null && !z4) {
                                str4 = this.sendingText;
                                if (str4 != null && str4.length() <= 1024 && this.photoPathsArray.size() == z9) {
                                    this.photoPathsArray.get(0).caption = this.sendingText;
                                    this.sendingText = null;
                                }
                                SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j2, messageObject, messageObject, null, null, false, false, null, z10, 0, 0, false, null, null, 0, 0L, false);
                            }
                            if (this.documentsPathsArray == null || this.documentsUrisArray != null) {
                                str2 = this.sendingText;
                                if (str2 != null && str2.length() <= 1024) {
                                    ArrayList<String> arrayList16 = this.documentsPathsArray;
                                    size = arrayList16 == null ? arrayList16.size() : 0;
                                    arrayList2 = this.documentsUrisArray;
                                    if (size + (arrayList2 == null ? arrayList2.size() : 0) == z9) {
                                        String str8 = this.sendingText;
                                        this.sendingText = null;
                                        str = str8;
                                    }
                                }
                                SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j2, messageObject, messageObject, null, null, null, z10, 0, null, null, 0, 0L, false);
                            }
                            if (this.voicePath != null) {
                                File file = new File(this.voicePath);
                                if (file.exists()) {
                                    TLRPC$TL_document tLRPC$TL_document = new TLRPC$TL_document();
                                    tLRPC$TL_document.file_reference = new byte[0];
                                    tLRPC$TL_document.dc_id = Integer.MIN_VALUE;
                                    tLRPC$TL_document.id = SharedConfig.getLastLocalId();
                                    tLRPC$TL_document.user_id = accountInstance.getUserConfig().getClientUserId();
                                    tLRPC$TL_document.mime_type = "audio/ogg";
                                    tLRPC$TL_document.date = accountInstance.getConnectionsManager().getCurrentTime();
                                    tLRPC$TL_document.size = (int) file.length();
                                    TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                                    tLRPC$TL_documentAttributeAudio.voice = z9;
                                    byte[] waveform = MediaController.getWaveform(file.getAbsolutePath());
                                    tLRPC$TL_documentAttributeAudio.waveform = waveform;
                                    if (waveform != null) {
                                        tLRPC$TL_documentAttributeAudio.flags |= 4;
                                    }
                                    tLRPC$TL_document.attributes.add(tLRPC$TL_documentAttributeAudio);
                                    accountInstance.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$TL_document, null, file.getAbsolutePath(), j2, messageObject, messageObject, this.sendingText, null, null, null, z10, 0, 0, null, null, false));
                                    if (this.sendingText != null) {
                                        this.sendingText = null;
                                    }
                                    str3 = this.sendingText;
                                    if (str3 != null) {
                                        SendMessagesHelper.prepareSendingText(accountInstance, str3, j2, j, z10, 0, 0L);
                                    }
                                    arrayList3 = this.contactsToSend;
                                    if (arrayList3 != null && !arrayList3.isEmpty()) {
                                        for (i2 = 0; i2 < this.contactsToSend.size(); i2++) {
                                            SendMessagesHelper.getInstance(currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(this.contactsToSend.get(i2), j2, messageObject, messageObject, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z10, 0));
                                        }
                                    }
                                    if (TextUtils.isEmpty(charSequence) && !z5 && !z4) {
                                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j2, j, z10, 0, 0L);
                                    }
                                    i4 = i + 1;
                                    z2 = z6;
                                    chatActivity = chatActivity3;
                                    str5 = str;
                                    z9 = true;
                                }
                            }
                            str3 = this.sendingText;
                            if (str3 != null) {
                            }
                            arrayList3 = this.contactsToSend;
                            if (arrayList3 != null) {
                                while (i2 < this.contactsToSend.size()) {
                                }
                            }
                            if (TextUtils.isEmpty(charSequence)) {
                                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j2, j, z10, 0, 0L);
                            }
                            i4 = i + 1;
                            z2 = z6;
                            chatActivity = chatActivity3;
                            str5 = str;
                            z9 = true;
                        }
                    } else {
                        i = i4;
                    }
                    z3 = z2;
                    messageObject = null;
                    if (chatActivity == null) {
                    }
                    if (this.photoPathsArray != null) {
                        str4 = this.sendingText;
                        if (str4 != null) {
                            this.photoPathsArray.get(0).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j2, messageObject, messageObject, null, null, false, false, null, z10, 0, 0, false, null, null, 0, 0L, false);
                    }
                    if (this.documentsPathsArray == null) {
                    }
                    str2 = this.sendingText;
                    if (str2 != null) {
                        ArrayList<String> arrayList162 = this.documentsPathsArray;
                        if (arrayList162 == null) {
                        }
                        arrayList2 = this.documentsUrisArray;
                        if (size + (arrayList2 == null ? arrayList2.size() : 0) == z9) {
                        }
                    }
                    SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j2, messageObject, messageObject, null, null, null, z10, 0, null, null, 0, 0L, false);
                    if (this.voicePath != null) {
                    }
                    str3 = this.sendingText;
                    if (str3 != null) {
                    }
                    arrayList3 = this.contactsToSend;
                    if (arrayList3 != null) {
                    }
                    if (TextUtils.isEmpty(charSequence)) {
                    }
                    i4 = i + 1;
                    z2 = z6;
                    chatActivity = chatActivity3;
                    str5 = str;
                    z9 = true;
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
    public /* synthetic */ void lambda$didSelectDialogs$127(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
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
    public /* synthetic */ void lambda$didSelectDialogs$128(ChatActivity chatActivity, ArrayList arrayList, CharSequence charSequence, int i, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2, long j, boolean z3) {
        MessageObject messageObject;
        TLRPC$TL_forumTopic findTopic;
        if (chatActivity != null) {
            getActionBarLayout().presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long j2 = ((MessagesStorage.TopicKey) arrayList.get(i3)).dialogId;
            long j3 = ((MessagesStorage.TopicKey) arrayList.get(i3)).topicId;
            if (j3 == 0 || (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j2, j3)) == null || findTopic.topicStartMessage == null) {
                messageObject = null;
            } else {
                MessageObject messageObject2 = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                messageObject2.isTopicMainMessage = true;
                messageObject = messageObject2;
            }
            SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(tLRPC$User, j2, messageObject, messageObject, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2);
            if (TextUtils.isEmpty(charSequence)) {
                of.effect_id = j;
            }
            of.invert_media = z3;
            SendMessagesHelper.getInstance(i).sendMessage(of);
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j2, z, 0, j);
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
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openBoostForUsersDialog);
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
        LiteMode.removeOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda112(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPowerSaver(boolean z) {
        BaseFragment lastFragment;
        if (Build.VERSION.SDK_INT < 21 || this.actionBarLayout == null || !z || LiteMode.getPowerSaverLevel() >= 100 || (lastFragment = this.actionBarLayout.getLastFragment()) == null || (lastFragment instanceof LiteModeSettingsActivity)) {
            return;
        }
        int batteryLevel = LiteMode.getBatteryLevel();
        BulletinFactory.of(lastFragment).createSimpleBulletin(new BatteryDrawable(batteryLevel / 100.0f, -1, lastFragment.getThemedColor(Theme.key_dialogSwipeRemove), 1.3f), LocaleController.getString("LowPowerEnabledTitle", R.string.LowPowerEnabledTitle), LocaleController.formatString("LowPowerEnabledSubtitle", R.string.LowPowerEnabledSubtitle, String.format("%d%%", Integer.valueOf(batteryLevel))), LocaleController.getString("Disable", R.string.Disable), new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onPowerSaver$129();
            }
        }).setDuration(5000).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPowerSaver$129() {
        lambda$runLinkRequest$88(new LiteModeSettingsActivity());
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$88(BaseFragment baseFragment) {
        getActionBarLayout().presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return getActionBarLayout().presentFragment(baseFragment, z, z2, true, false);
    }

    public INavigationLayout getActionBarLayout() {
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (this.sheetFragmentsStack.isEmpty()) {
            return actionBarLayout;
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

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
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
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onActivityResult$130();
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
                if (baseFragment.getLastStoryViewer() != null) {
                    baseFragment.getLastStoryViewer().onActivityResult(i, i2, intent);
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
    public /* synthetic */ void lambda$onActivityResult$130() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (checkPermissionsResult(i, strArr, iArr)) {
            ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
            if (applicationLoader == null || !applicationLoader.checkRequestPermissionResult(i, strArr, iArr)) {
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
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM));
        ApplicationLoader.mainInterfacePaused = true;
        final int i = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$131(i);
            }
        });
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            if (actionBarLayout != null) {
                actionBarLayout.onPause();
            }
            ActionBarLayout actionBarLayout2 = this.layersActionBarLayout;
            if (actionBarLayout2 != null) {
                actionBarLayout2.onPause();
            }
        }
        PasscodeViewDialog passcodeViewDialog = this.passcodeDialog;
        if (passcodeViewDialog != null) {
            passcodeViewDialog.passcodeView.onPause();
        }
        for (PasscodeView passcodeView : this.overlayPasscodeViews) {
            passcodeView.onPause();
        }
        ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(!(applicationLoader != null ? applicationLoader.onPause() : false), false);
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
    public static /* synthetic */ void lambda$onPause$131(int i) {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
        if (VoIPService.getSharedInstance() == null) {
            MessagesController.getInstance(i).ignoreSetOnline = false;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
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

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
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

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        isActive = false;
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
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            try {
                if (this.visibleDialogs.get(i).isShowing()) {
                    this.visibleDialogs.get(i).dismiss();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        this.visibleDialogs.clear();
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
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.onUserLeaveHint();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
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
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onResume$132();
            }
        });
        checkFreeDiscSpace(0);
        MediaController.checkGallery();
        onPasscodeResume();
        PasscodeViewDialog passcodeViewDialog = this.passcodeDialog;
        if (passcodeViewDialog == null || passcodeViewDialog.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                ActionBarLayout actionBarLayout = this.rightActionBarLayout;
                if (actionBarLayout != null) {
                    actionBarLayout.onResume();
                }
                ActionBarLayout actionBarLayout2 = this.layersActionBarLayout;
                if (actionBarLayout2 != null) {
                    actionBarLayout2.onResume();
                }
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                ActionBarLayout actionBarLayout3 = this.rightActionBarLayout;
                if (actionBarLayout3 != null) {
                    actionBarLayout3.dismissDialogs();
                }
                ActionBarLayout actionBarLayout4 = this.layersActionBarLayout;
                if (actionBarLayout4 != null) {
                    actionBarLayout4.dismissDialogs();
                }
            }
            this.passcodeDialog.passcodeView.onResume();
            for (PasscodeView passcodeView : this.overlayPasscodeViews) {
                passcodeView.onResume();
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
        checkAppUpdate(false, null);
        if (Build.VERSION.SDK_INT >= 23) {
            ApplicationLoader.canDrawOverlays = Settings.canDrawOverlays(this);
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onResume();
        }
        invalidateTabletMode();
        SpoilerEffect2.pause(false);
        ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
        if (applicationLoader != null) {
            applicationLoader.onResume();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$132() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    private void invalidateTabletMode() {
        long j;
        Boolean wasTablet = AndroidUtilities.getWasTablet();
        if (wasTablet == null) {
            return;
        }
        AndroidUtilities.resetWasTabletFlag();
        if (wasTablet.booleanValue() != AndroidUtilities.isTablet()) {
            long j2 = 0;
            if (wasTablet.booleanValue()) {
                mainFragmentsStack.addAll(rightFragmentsStack);
                mainFragmentsStack.addAll(layerFragmentsStack);
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
                j = 0;
            } else {
                ArrayList<BaseFragment> arrayList = new ArrayList(mainFragmentsStack);
                mainFragmentsStack.clear();
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
                long j3 = 0;
                j = 0;
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
                            if (j3 == 0) {
                                j3 = chatActivity.getDialogId();
                                j = chatActivity.getTopicId();
                            }
                        }
                    }
                    layerFragmentsStack.add(baseFragment);
                }
                j2 = j3;
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
                            dialogsActivity2.setOpenedDialogId(j2, j);
                        }
                    }
                }
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
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

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        AndroidUtilities.isInMultiwindow = z;
        checkLayout();
        super.onMultiWindowModeChanged(z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x031d, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L132;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x030c  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0699  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x069f  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x06a4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x06a5  */
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
        ActionBarLayout actionBarLayout;
        boolean z2;
        View childAt;
        BaseFragment baseFragment3;
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        boolean z3 = false;
        boolean z4 = false;
        r5 = false;
        r5 = false;
        boolean z5 = false;
        z3 = false;
        if (i == NotificationCenter.openBoostForUsersDialog) {
            processBoostDialog(Long.valueOf(((Long) objArr[0]).longValue()), null, null, objArr.length > 1 ? (ChatMessageCell) objArr[1] : null);
        } else if (i == NotificationCenter.closeOtherAppActivities) {
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
                                LaunchActivity.lambda$didReceivedNotification$133(i2, dialogInterface, i6);
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
                                    LaunchActivity.this.lambda$didReceivedNotification$134(dialogInterface, i7);
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
                    LaunchActivity.this.lambda$didReceivedNotification$136(hashMap2, i2, dialogInterface, i7);
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
            ActionBarLayout actionBarLayout2 = this.actionBarLayout;
            if (actionBarLayout2 == null || actionBarLayout2.getFragmentStack().isEmpty()) {
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
            builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$137(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$138(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
                }
            });
            builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    LaunchActivity.lambda$didReceivedNotification$139(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i7);
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
                    RecyclerListView recyclerListView3 = this.sideMenu;
                    if (recyclerListView3 != null && recyclerListView3.getChildCount() > 0) {
                        View childAt2 = this.sideMenu.getChildAt(0);
                        if (childAt2 instanceof DrawerProfileCell) {
                            ((DrawerProfileCell) childAt2).updateSunDrawable(booleanValue4);
                        }
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
                                LaunchActivity.this.lambda$didReceivedNotification$140(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(createCircularReveal.getDuration());
                        ofFloat.start();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$didReceivedNotification$141();
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
                actionBarLayout = this.actionBarLayout;
                if (actionBarLayout != null) {
                    return;
                }
                actionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z, runnable);
                if (AndroidUtilities.isTablet()) {
                    ActionBarLayout actionBarLayout3 = this.layersActionBarLayout;
                    if (actionBarLayout3 != null) {
                        actionBarLayout3.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                    }
                    ActionBarLayout actionBarLayout4 = this.rightActionBarLayout;
                    if (actionBarLayout4 != null) {
                        actionBarLayout4.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                        return;
                    }
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
            actionBarLayout = this.actionBarLayout;
            if (actionBarLayout != null) {
            }
        } else if (i == NotificationCenter.notificationsCountUpdated) {
            RecyclerListView recyclerListView4 = this.sideMenu;
            if (recyclerListView4 != null) {
                Integer num2 = (Integer) objArr[0];
                int childCount = recyclerListView4.getChildCount();
                for (int i10 = 0; i10 < childCount; i10++) {
                    View childAt3 = this.sideMenu.getChildAt(i10);
                    if ((childAt3 instanceof DrawerUserCell) && ((DrawerUserCell) childAt3).getAccountNumber() == num2.intValue()) {
                        childAt3.invalidate();
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
                    File file = new File(ApplicationLoader.getFilesDirFixed(), "remote" + this.loadingTheme.id + ".attheme");
                    TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
                    final Theme.ThemeInfo fillThemeValues = Theme.fillThemeValues(file, tLRPC$TL_theme.title, tLRPC$TL_theme);
                    if (fillThemeValues != null) {
                        if (fillThemeValues.pathToWallpaper != null && !new File(fillThemeValues.pathToWallpaper).exists()) {
                            TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                            TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                            tLRPC$TL_inputWallPaperSlug.slug = fillThemeValues.slug;
                            tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                            ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda142
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$didReceivedNotification$143(fillThemeValues, tLObject, tLRPC$TL_error);
                                }
                            });
                            return;
                        }
                        TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                        if (applyThemeFile != null) {
                            lambda$runLinkRequest$88(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$didReceivedNotification$145(themeInfo3, file2);
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
    public static /* synthetic */ void lambda$didReceivedNotification$133(int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(i);
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$134(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$136(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (AndroidUtilities.isMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            LocationActivity locationActivity = new LocationActivity(0);
            locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda159
                @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
                public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i3, boolean z, int i4) {
                    LaunchActivity.lambda$didReceivedNotification$135(hashMap, i, tLRPC$MessageMedia, i3, z, i4);
                }
            });
            lambda$runLinkRequest$88(locationActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$135(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$137(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$138(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$139(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$140(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$141() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$143(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$142(tLObject, themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$142(TLObject tLObject, Theme.ThemeInfo themeInfo) {
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
    public /* synthetic */ void lambda$didReceivedNotification$145(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$144();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$144() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$88(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
        lambda$runLinkRequest$88(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$148(i);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$148(int i) {
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkFreeDiscSpace$147();
                            }
                        });
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$147() {
        if (this.checkFreeDiscSpaceShown) {
            return;
        }
        try {
            Dialog createFreeSpaceDialog = AlertsCreator.createFreeSpaceDialog(this);
            createFreeSpaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$146(dialogInterface);
                }
            });
            this.checkFreeDiscSpaceShown = true;
            createFreeSpaceDialog.show();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$146(DialogInterface dialogInterface) {
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
                            LaunchActivity.lambda$showLanguageAlertInternal$149(localeInfoArr, languageCellArr, view);
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
                        LaunchActivity.this.lambda$showLanguageAlertInternal$150(view);
                    }
                });
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$151(localeInfoArr, dialogInterface, i4);
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
                    LaunchActivity.this.lambda$showLanguageAlertInternal$150(view);
                }
            });
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$151(localeInfoArr3, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showLanguageAlertInternal$149(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$150(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$88(new LanguageSelectActivity());
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            if (this.visibleDialogs.get(i).isShowing()) {
                this.visibleDialogs.get(i).dismiss();
            }
        }
        this.visibleDialogs.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$151(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
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
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda145
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$153(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 8);
                        TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                        tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                        tLRPC$TL_langpack_getStrings2.keys.add("English");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                        tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda146
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$155(localeInfoArr, str2, tLObject, tLRPC$TL_error);
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
    public /* synthetic */ void lambda$showLanguageAlert$153(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$152(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$152(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$155(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$154(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$154(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
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
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$updateCurrentConnectionState$156();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentConnectionState$156() {
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
        lambda$runLinkRequest$88(new ProxyListActivity());
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode == null) {
            return;
        }
        actionMode.finish();
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        try {
            super.onSaveInstanceState(bundle);
            BaseFragment baseFragment = null;
            if (AndroidUtilities.isTablet()) {
                ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                if (actionBarLayout != null && !actionBarLayout.getFragmentStack().isEmpty()) {
                    baseFragment = this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1);
                } else {
                    ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
                    if (actionBarLayout2 != null && !actionBarLayout2.getFragmentStack().isEmpty()) {
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

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (FloatingDebugController.onBackPressed()) {
            return;
        }
        PasscodeViewDialog passcodeViewDialog = this.passcodeDialog;
        if (passcodeViewDialog != null && passcodeViewDialog.passcodeView.getVisibility() == 0) {
            finish();
            return;
        }
        BottomSheetTabsOverlay bottomSheetTabsOverlay = this.bottomSheetTabsOverlay;
        if ((bottomSheetTabsOverlay == null || !bottomSheetTabsOverlay.onBackPressed()) && !SearchTagsList.onBackPressedRenameTagAlert()) {
            if (ContentPreviewViewer.hasInstance() && ContentPreviewViewer.getInstance().isVisible()) {
                ContentPreviewViewer.getInstance().closeWithMenu();
            } else if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
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
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.onLowMemory();
            if (AndroidUtilities.isTablet()) {
                ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
                if (actionBarLayout2 != null) {
                    actionBarLayout2.onLowMemory();
                }
                ActionBarLayout actionBarLayout3 = this.layersActionBarLayout;
                if (actionBarLayout3 != null) {
                    actionBarLayout3.onLowMemory();
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

    @Override // androidx.core.app.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        BaseFragment lastFragment;
        keyEvent.getKeyCode();
        boolean z = true;
        if ((keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) && (lastFragment = getLastFragment()) != null && lastFragment.getLastStoryViewer() != null) {
            lastFragment.getLastStoryViewer().dispatchKeyEvent(keyEvent);
            return true;
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
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        BaseFragment baseFragment = navigationParams.fragment;
        boolean z = navigationParams.removeLast;
        boolean z2 = navigationParams.noAnimation;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z3 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z3 || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || ((actionBarLayout4 = this.layersActionBarLayout) != null && actionBarLayout4.getView().getVisibility() == 0)) ? false : true, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (actionBarLayout3 = this.actionBarLayout)) {
                actionBarLayout3.removeAllFragments();
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
                    boolean z5 = (z4 && iNavigationLayout == (actionBarLayout2 = this.actionBarLayout) && actionBarLayout2.getFragmentStack().size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z5) {
                        getActionBarLayout().presentFragment(navigationParams.setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    }
                    return z5;
                } else if (!z4 && iNavigationLayout != (actionBarLayout = this.rightActionBarLayout) && actionBarLayout != null) {
                    if (actionBarLayout.getView() != null) {
                        this.rightActionBarLayout.getView().setVisibility(0);
                    }
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(navigationParams.setNoAnimation(true).setRemoveLast(z).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else if (z4 && iNavigationLayout != this.actionBarLayout) {
                    getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                            actionBarLayout7.removeFragmentFromStack(actionBarLayout7.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else {
                    ActionBarLayout actionBarLayout8 = this.layersActionBarLayout;
                    if (actionBarLayout8 != null && actionBarLayout8.getFragmentStack() != null && !this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout9 = this.layersActionBarLayout;
                            actionBarLayout9.removeFragmentFromStack(actionBarLayout9.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout10 = this.layersActionBarLayout;
                if (iNavigationLayout != actionBarLayout10) {
                    actionBarLayout10.getView().setVisibility(0);
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
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        if (AndroidUtilities.isTablet()) {
            boolean z = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity) || this.layersActionBarLayout.getView().getVisibility() == 0) ? false : true, true);
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (actionBarLayout3 = this.actionBarLayout)) {
                    actionBarLayout3.removeAllFragments();
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
                if (!z2 && iNavigationLayout != (actionBarLayout2 = this.rightActionBarLayout)) {
                    actionBarLayout2.getView().setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout4 = this.layersActionBarLayout;
                            actionBarLayout4.removeFragmentFromStack(actionBarLayout4.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (z2 && iNavigationLayout != (actionBarLayout = this.actionBarLayout)) {
                    actionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                if (iNavigationLayout != actionBarLayout6) {
                    actionBarLayout6.getView().setVisibility(0);
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
            if (iNavigationLayout == this.actionBarLayout && iNavigationLayout.getFragmentStack().size() <= 1 && !this.switchingAccount) {
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
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.rebuildAllFragmentViews(z, z);
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

    public static BaseFragment getSafeLastFragment() {
        LaunchActivity launchActivity = instance;
        if (launchActivity != null && !launchActivity.sheetFragmentsStack.isEmpty()) {
            ArrayList<INavigationLayout> arrayList = instance.sheetFragmentsStack;
            return arrayList.get(arrayList.size() - 1).getSafeLastFragment();
        }
        LaunchActivity launchActivity2 = instance;
        if (launchActivity2 == null || launchActivity2.getActionBarLayout() == null) {
            return null;
        }
        return instance.getActionBarLayout().getSafeLastFragment();
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
        BottomSheetTabs bottomSheetTabs = getBottomSheetTabs();
        if (bottomSheetTabs != null) {
            bottomSheetTabs.setNavigationBarColor(i);
        }
    }

    public BottomSheetTabs getBottomSheetTabs() {
        ActionBarLayout actionBarLayout = this.rightActionBarLayout;
        if (actionBarLayout != null && actionBarLayout.getBottomSheetTabs() != null) {
            return this.rightActionBarLayout.getBottomSheetTabs();
        }
        ActionBarLayout actionBarLayout2 = this.actionBarLayout;
        if (actionBarLayout2 == null || actionBarLayout2.getBottomSheetTabs() == null) {
            return null;
        }
        return this.actionBarLayout.getBottomSheetTabs();
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
                LaunchActivity.this.lambda$animateNavigationBarColor$157(valueAnimator2);
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
    public /* synthetic */ void lambda$animateNavigationBarColor$157(ValueAnimator valueAnimator) {
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

    /* JADX WARN: Removed duplicated region for block: B:23:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openMyStory(final int i, final boolean z) {
        TL_stories$StoryItem tL_stories$StoryItem;
        StoriesController.StoriesList storiesList;
        StoriesListPlaceProvider of;
        StoriesController.StoriesList storiesList2;
        MessageObject findMessageObject;
        MessageObject findMessageObject2;
        StoriesListPlaceProvider of2;
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        StoriesController storiesController = MessagesController.getInstance(this.currentAccount).getStoriesController();
        TL_stories$PeerStories stories = storiesController.getStories(clientUserId);
        if (stories != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= stories.stories.size()) {
                    tL_stories$StoryItem = null;
                    break;
                } else if (stories.stories.get(i2).id == i) {
                    tL_stories$StoryItem = stories.stories.get(i2);
                    break;
                } else {
                    i2++;
                }
            }
            if (tL_stories$StoryItem != null) {
                BaseFragment lastFragment = getLastFragment();
                if (lastFragment == null) {
                    return;
                }
                if (lastFragment instanceof DialogsActivity) {
                    try {
                        of2 = StoriesListPlaceProvider.of(((DialogsActivity) lastFragment).dialogStoriesCell.recyclerListView);
                    } catch (Exception unused) {
                    }
                    lastFragment.getOrCreateStoryViewer().instantClose();
                    ArrayList<Long> arrayList = new ArrayList<>();
                    arrayList.add(Long.valueOf(tL_stories$StoryItem.dialogId));
                    if (z) {
                        lastFragment.getOrCreateStoryViewer().showViewsAfterOpening();
                    }
                    lastFragment.getOrCreateStoryViewer().open(this, tL_stories$StoryItem, arrayList, 0, null, stories, of2, false);
                    return;
                }
                of2 = null;
                lastFragment.getOrCreateStoryViewer().instantClose();
                ArrayList<Long> arrayList2 = new ArrayList<>();
                arrayList2.add(Long.valueOf(tL_stories$StoryItem.dialogId));
                if (z) {
                }
                lastFragment.getOrCreateStoryViewer().open(this, tL_stories$StoryItem, arrayList2, 0, null, stories, of2, false);
                return;
            }
        } else {
            tL_stories$StoryItem = null;
        }
        if (tL_stories$StoryItem == null) {
            StoriesController.StoriesList storiesList3 = storiesController.getStoriesList(clientUserId, 0);
            if (storiesList3 == null || (findMessageObject2 = storiesList3.findMessageObject(i)) == null) {
                storiesList3 = null;
            } else {
                tL_stories$StoryItem = findMessageObject2.storyItem;
            }
            if (tL_stories$StoryItem != null || (storiesList2 = storiesController.getStoriesList(clientUserId, 1)) == null || (findMessageObject = storiesList2.findMessageObject(i)) == null) {
                storiesList = storiesList3;
            } else {
                TL_stories$StoryItem tL_stories$StoryItem2 = findMessageObject.storyItem;
                storiesList = storiesList2;
                tL_stories$StoryItem = tL_stories$StoryItem2;
            }
            if (tL_stories$StoryItem != null && storiesList != null) {
                BaseFragment lastFragment2 = getLastFragment();
                if (lastFragment2 == null) {
                    return;
                }
                if (lastFragment2 instanceof DialogsActivity) {
                    try {
                        of = StoriesListPlaceProvider.of(((DialogsActivity) lastFragment2).dialogStoriesCell.recyclerListView);
                    } catch (Exception unused2) {
                    }
                    lastFragment2.getOrCreateStoryViewer().instantClose();
                    ArrayList<Long> arrayList3 = new ArrayList<>();
                    arrayList3.add(Long.valueOf(tL_stories$StoryItem.dialogId));
                    if (z) {
                        lastFragment2.getOrCreateStoryViewer().showViewsAfterOpening();
                    }
                    lastFragment2.getOrCreateStoryViewer().open(this, tL_stories$StoryItem, arrayList3, 0, storiesList, null, of, false);
                    return;
                }
                of = null;
                lastFragment2.getOrCreateStoryViewer().instantClose();
                ArrayList<Long> arrayList32 = new ArrayList<>();
                arrayList32.add(Long.valueOf(tL_stories$StoryItem.dialogId));
                if (z) {
                }
                lastFragment2.getOrCreateStoryViewer().open(this, tL_stories$StoryItem, arrayList32, 0, storiesList, null, of, false);
                return;
            }
        }
        TL_stories$TL_stories_getStoriesByID tL_stories$TL_stories_getStoriesByID = new TL_stories$TL_stories_getStoriesByID();
        tL_stories$TL_stories_getStoriesByID.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(clientUserId);
        tL_stories$TL_stories_getStoriesByID.id.add(Integer.valueOf(i));
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda119
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openMyStory$159(i, clientUserId, z, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMyStory$159(final int i, final long j, final boolean z, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openMyStory$158(tLObject, i, j, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMyStory$158(TLObject tLObject, int i, long j, boolean z) {
        StoriesListPlaceProvider storiesListPlaceProvider;
        TL_stories$StoryItem tL_stories$StoryItem;
        if (tLObject instanceof TL_stories$TL_stories_stories) {
            TL_stories$TL_stories_stories tL_stories$TL_stories_stories = (TL_stories$TL_stories_stories) tLObject;
            int i2 = 0;
            while (true) {
                storiesListPlaceProvider = null;
                if (i2 >= tL_stories$TL_stories_stories.stories.size()) {
                    tL_stories$StoryItem = null;
                    break;
                } else if (tL_stories$TL_stories_stories.stories.get(i2).id == i) {
                    tL_stories$StoryItem = tL_stories$TL_stories_stories.stories.get(i2);
                    break;
                } else {
                    i2++;
                }
            }
            if (tL_stories$StoryItem != null) {
                tL_stories$StoryItem.dialogId = j;
                BaseFragment lastFragment = getLastFragment();
                if (lastFragment == null) {
                    return;
                }
                if (lastFragment instanceof DialogsActivity) {
                    try {
                        storiesListPlaceProvider = StoriesListPlaceProvider.of(((DialogsActivity) lastFragment).dialogStoriesCell.recyclerListView);
                    } catch (Exception unused) {
                    }
                }
                StoriesListPlaceProvider storiesListPlaceProvider2 = storiesListPlaceProvider;
                lastFragment.getOrCreateStoryViewer().instantClose();
                ArrayList<Long> arrayList = new ArrayList<>();
                arrayList.add(Long.valueOf(j));
                if (z) {
                    lastFragment.getOrCreateStoryViewer().showViewsAfterOpening();
                }
                lastFragment.getOrCreateStoryViewer().open(this, tL_stories$StoryItem, arrayList, 0, null, null, storiesListPlaceProvider2, false);
                return;
            }
        }
        BulletinFactory.global().createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryNotFound)).show(false);
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
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda104
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openStories$160(iArr, jArr2);
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
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_getPeerStories, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda116
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.lambda$openStories$162(MessagesController.this, longValue, runnable, tLObject, tLRPC$TL_error);
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
    public /* synthetic */ void lambda$openStories$160(int[] iArr, long[] jArr) {
        iArr[0] = iArr[0] - 1;
        if (iArr[0] == 0) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.storiesUpdated, new Object[0]);
            openStories(jArr, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStories$162(final MessagesController messagesController, final long j, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$openStories$161(TLObject.this, messagesController, j, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openStories$161(TLObject tLObject, MessagesController messagesController, long j, Runnable runnable) {
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
