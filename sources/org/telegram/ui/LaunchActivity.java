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
import android.text.TextUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
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
import org.telegram.tgnet.TLRPC$TL_account_updateEmojiStatus;
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
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.ChatActivity;
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
    private boolean isNavigationBarColorFrozen = false;
    private List<Runnable> onUserLeaveHintListeners = new ArrayList();
    private SparseIntArray requestedPermissions = new SparseIntArray();
    private int requsetPermissionsPointer = 5934;

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
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.1
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
        2 r7 = new 2(this);
        this.drawerLayoutContainer = r7;
        r7.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
        this.frameLayout.addView(this.drawerLayoutContainer, LayoutHelper.createFrame(-1, -1.0f));
        if (i2 >= 21) {
            View view = new View(this) { // from class: org.telegram.ui.LaunchActivity.3
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
        FireworksOverlay fireworksOverlay = new FireworksOverlay(this, this) { // from class: org.telegram.ui.LaunchActivity.4
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
        RecyclerListView recyclerListView = new RecyclerListView(this) { // from class: org.telegram.ui.LaunchActivity.5
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view2, long j) {
                int i3;
                if (LaunchActivity.this.itemAnimator == null || !LaunchActivity.this.itemAnimator.isRunning() || !LaunchActivity.this.itemAnimator.isAnimatingChild(view2)) {
                    i3 = -1;
                } else {
                    i3 = canvas.save();
                    canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
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
        this.drawerLayoutAdapter.setOnPremiumDrawableClick(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
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
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
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
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(3, 0) { // from class: org.telegram.ui.LaunchActivity.6
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
                    if (Build.VERSION.SDK_INT < 21) {
                        return;
                    }
                    ObjectAnimator.ofFloat(view2, "elevation", AndroidUtilities.dp(1.0f)).setDuration(150L).start();
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
                    if (Build.VERSION.SDK_INT < 21) {
                        return;
                    }
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, "elevation", 0.0f);
                    ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.LaunchActivity.6.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            view2.setBackground(null);
                        }
                    });
                    ofFloat.setDuration(150L).start();
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
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda104
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
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
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
            String str3 = "";
            String lowerCase2 = str != null ? str.toLowerCase() : str3;
            if (str2 != null) {
                str3 = lowerCase2.toLowerCase();
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("OS name " + lowerCase2 + " " + str3);
            }
            if ((lowerCase2.contains("flyme") || str3.contains("flyme")) && Build.VERSION.SDK_INT <= 24) {
                AndroidUtilities.incorrectDisplaySizeFix = true;
                final View rootView = getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
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
        if (i4 < 28 || !((ActivityManager) getSystemService("activity")).isBackgroundRestricted() || System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() < 86400000) {
            return;
        }
        AlertsCreator.createBackgroundActivityDialog(this).show();
        SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 extends DrawerLayoutContainer {
        private boolean wasPortrait;

        2(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            setDrawerPosition(getDrawerPosition());
            boolean z2 = i4 - i2 > i3 - i;
            if (z2 != this.wasPortrait) {
                post(new Runnable() { // from class: org.telegram.ui.LaunchActivity$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.2.this.lambda$onLayout$0();
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
                    lambda$runLinkRequest$65(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.getFragmentStack().size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
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
                lambda$runLinkRequest$65(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$65(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$65(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$65(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                lambda$runLinkRequest$65(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$65(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$65(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$65(new ChatActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$65(new ActionIntroActivity(1));
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
                    lambda$runLinkRequest$65(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$65(new ActionIntroActivity(4));
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
            DialogsActivity dialogsActivity = new DialogsActivity(null) { // from class: org.telegram.ui.LaunchActivity.7
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
        if (dp <= point.y) {
            return;
        }
        point.y = measuredHeight;
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        FileLog.d("fix display size y to " + AndroidUtilities.displaySize.y);
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
            RelativeLayout relativeLayout = new RelativeLayout(this) { // from class: org.telegram.ui.LaunchActivity.8
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
            SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(this, this) { // from class: org.telegram.ui.LaunchActivity.9
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
            int i = 8;
            frameLayout2.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(2130706432);
            this.launchLayout.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda21
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$setupActionBarLayout$6;
                    lambda$setupActionBarLayout$6 = LaunchActivity.this.lambda$setupActionBarLayout$6(view2, motionEvent);
                    return lambda$setupActionBarLayout$6;
                }
            });
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda20.INSTANCE);
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
            if (!layerFragmentsStack.isEmpty()) {
                i = 0;
            }
            view2.setVisibility(i);
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
        if (LoginActivity.loadCurrentState(false).getInt("currentViewNum", 0) != 0) {
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
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
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
        10 r7 = new 10(lastFragment, this, true, Integer.valueOf(i), 0, null, selectAnimatedEmojiDialogWindowArr);
        if (user != null) {
            TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
            if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                r7.setExpireDateHint(((TLRPC$TL_emojiStatusUntil) user.emoji_status).until);
            }
        }
        r7.setSelected((swapAnimatedEmojiDrawable == null || !(swapAnimatedEmojiDrawable.getDrawable() instanceof AnimatedEmojiDrawable)) ? null : Long.valueOf(((AnimatedEmojiDrawable) swapAnimatedEmojiDrawable.getDrawable()).getDocumentId()));
        r7.setSaveState(2);
        r7.setScrimDrawable(swapAnimatedEmojiDrawable, view2);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(r7, -2, -2) { // from class: org.telegram.ui.LaunchActivity.11
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 10 extends SelectAnimatedEmojiDialog {
        final /* synthetic */ SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] val$popup;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        10(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, Theme.ResourcesProvider resourcesProvider, SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr) {
            super(baseFragment, context, z, num, i, resourcesProvider);
            this.val$popup = selectAnimatedEmojiDialogWindowArr;
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        public void onSettings() {
            DrawerLayoutContainer drawerLayoutContainer = LaunchActivity.this.drawerLayoutContainer;
            if (drawerLayoutContainer != null) {
                drawerLayoutContainer.closeDrawer();
            }
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
            String string;
            int i;
            TLRPC$TL_account_updateEmojiStatus tLRPC$TL_account_updateEmojiStatus = new TLRPC$TL_account_updateEmojiStatus();
            if (l == null) {
                tLRPC$TL_account_updateEmojiStatus.emoji_status = new TLRPC$TL_emojiStatusEmpty();
            } else if (num != null) {
                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil = new TLRPC$TL_emojiStatusUntil();
                tLRPC$TL_account_updateEmojiStatus.emoji_status = tLRPC$TL_emojiStatusUntil;
                tLRPC$TL_emojiStatusUntil.document_id = l.longValue();
                ((TLRPC$TL_emojiStatusUntil) tLRPC$TL_account_updateEmojiStatus.emoji_status).until = num.intValue();
            } else {
                TLRPC$TL_emojiStatus tLRPC$TL_emojiStatus = new TLRPC$TL_emojiStatus();
                tLRPC$TL_account_updateEmojiStatus.emoji_status = tLRPC$TL_emojiStatus;
                tLRPC$TL_emojiStatus.document_id = l.longValue();
            }
            TLRPC$User user = MessagesController.getInstance(LaunchActivity.this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(LaunchActivity.this.currentAccount).getClientUserId()));
            if (user != null) {
                user.emoji_status = tLRPC$TL_account_updateEmojiStatus.emoji_status;
                NotificationCenter.getInstance(LaunchActivity.this.currentAccount).postNotificationName(NotificationCenter.userEmojiStatusUpdated, user);
                MessagesController.getInstance(LaunchActivity.this.currentAccount).updateEmojiStatusUntilUpdate(user.id, user.emoji_status);
                for (int i2 = 0; i2 < LaunchActivity.this.sideMenu.getChildCount(); i2++) {
                    View childAt = LaunchActivity.this.sideMenu.getChildAt(i2);
                    if (childAt instanceof DrawerUserCell) {
                        DrawerUserCell drawerUserCell = (DrawerUserCell) childAt;
                        drawerUserCell.setAccount(drawerUserCell.getAccountNumber());
                    } else if (childAt instanceof DrawerProfileCell) {
                        if (l != null) {
                            ((DrawerProfileCell) childAt).animateStateChange(l.longValue());
                        }
                        ((DrawerProfileCell) childAt).setUser(user, LaunchActivity.this.drawerLayoutAdapter.isAccountsShown());
                    } else if ((childAt instanceof DrawerActionCell) && LaunchActivity.this.drawerLayoutAdapter.getId(LaunchActivity.this.sideMenu.getChildAdapterPosition(childAt)) == 15) {
                        TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
                        boolean z = (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatus) || ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000)));
                        DrawerActionCell drawerActionCell = (DrawerActionCell) childAt;
                        if (z) {
                            string = LocaleController.getString("ChangeEmojiStatus", R.string.ChangeEmojiStatus);
                        } else {
                            string = LocaleController.getString("SetEmojiStatus", R.string.SetEmojiStatus);
                        }
                        drawerActionCell.updateText(string);
                        if (z) {
                            i = R.raw.emoji_status_change_to_set;
                        } else {
                            i = R.raw.emoji_status_set_to_change;
                        }
                        drawerActionCell.updateIcon(i);
                    }
                }
            }
            ConnectionsManager.getInstance(LaunchActivity.this.currentAccount).sendRequest(tLRPC$TL_account_updateEmojiStatus, LaunchActivity$10$$ExternalSyntheticLambda0.INSTANCE);
            if (this.val$popup[0] != null) {
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
                this.val$popup[0].dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onEmojiSelected$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            boolean z = tLObject instanceof TLRPC$TL_boolTrue;
        }
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
        lambda$runLinkRequest$65(new ProfileActivity(bundle));
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
        boolean z5 = true;
        if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        } else {
            baseFragment = null;
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
                    z4 = baseFragment.isLightStatusBar();
                } else {
                    z4 = ColorUtils.calculateLuminance(Theme.getColor("actionBarDefault", null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), z4, z6);
            }
            if (i >= 26 && z3 && (!z || baseFragment == null || !baseFragment.isInPreviewMode())) {
                Window window = getWindow();
                int color = (baseFragment == null || !z) ? Theme.getColor("windowBackgroundGray", null, true) : baseFragment.getNavigationBarColor();
                if (window.getNavigationBarColor() != color) {
                    window.setNavigationBarColor(color);
                    float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(color);
                    Window window2 = getWindow();
                    if (computePerceivedBrightness < 0.721f) {
                        z5 = false;
                    }
                    AndroidUtilities.setLightNavigationBar(window2, z5);
                }
            }
        }
        if ((SharedConfig.noStatusBar || z6) && i >= 21 && z2) {
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
        switchToAccount(i, z, LaunchActivity$$ExternalSyntheticLambda72.INSTANCE);
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
        clearFragments();
        this.actionBarLayout.rebuildLogout();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildLogout();
            this.rightActionBarLayout.rebuildLogout();
        }
        lambda$runLinkRequest$65(new IntroActivity().setOnLogout());
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
        if (AndroidUtilities.getWasTablet() != null && AndroidUtilities.getWasTablet().booleanValue() != AndroidUtilities.isTabletForce()) {
            return;
        }
        int i = 0;
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
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.getFragmentStack().isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
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
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
        if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
            i = 8;
        }
        sizeNotifierFrameLayout.setVisibility(i);
    }

    private void showUpdateActivity(int i, TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, boolean z) {
        if (this.blockingUpdateView == null) {
            BlockingUpdateView blockingUpdateView = new BlockingUpdateView(this) { // from class: org.telegram.ui.LaunchActivity.12
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
            this.termsOfServiceView.setDelegate(new 13());
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
    public class 13 implements TermsOfServiceView.TermsOfServiceViewDelegate {
        13() {
        }

        @Override // org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate
        public void onAcceptTerms(int i) {
            UserConfig.getInstance(i).unacceptedTermsOfService = null;
            UserConfig.getInstance(i).saveConfig(false);
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            if (LaunchActivity.mainFragmentsStack.size() > 0) {
                ((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1)).onResume();
            }
            LaunchActivity.this.termsOfServiceView.animate().alpha(0.0f).setDuration(150L).setInterpolator(AndroidUtilities.accelerateInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$13$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.13.this.lambda$onAcceptTerms$0();
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
        this.passcodeView.onShow(z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showPasscodeActivity$9(runnable);
            }
        }, runnable2);
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate(new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword() {
                LaunchActivity.this.lambda$showPasscodeActivity$10();
            }
        });
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
    public /* synthetic */ void lambda$showPasscodeActivity$10() {
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
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:1090:0x16ae, code lost:
        if (r2 == 0) goto L1105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x1f4c, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r3)) != false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0330, code lost:
        if (r85.sendingText == null) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x015b, code lost:
        if (r1.equals(r0) != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x09bf, code lost:
        if (r7 == 0) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0a66, code lost:
        if (r7 == 0) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x1ed1, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r3)) != false) goto L123;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1046:0x1616 A[Catch: Exception -> 0x1624, TRY_LEAVE, TryCatch #13 {Exception -> 0x1624, blocks: (B:1044:0x160a, B:1046:0x1616), top: B:1043:0x160a }] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x23d2  */
    /* JADX WARN: Removed duplicated region for block: B:1138:0x18f4  */
    /* JADX WARN: Removed duplicated region for block: B:1140:0x192c  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x242b  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x2433  */
    /* JADX WARN: Removed duplicated region for block: B:1214:0x03f6  */
    /* JADX WARN: Removed duplicated region for block: B:1234:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x1f17  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x2106  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x2123  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x2134  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x2115  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:464:0x19e9  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:505:0x1b10 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:563:0x1be4  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x1bf4  */
    /* JADX WARN: Removed duplicated region for block: B:568:0x1a2b  */
    /* JADX WARN: Removed duplicated region for block: B:582:0x073c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0337  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x0828 A[Catch: Exception -> 0x0836, TRY_LEAVE, TryCatch #5 {Exception -> 0x0836, blocks: (B:622:0x081c, B:624:0x0828), top: B:621:0x081c }] */
    /* JADX WARN: Removed duplicated region for block: B:637:0x0835  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x1e6e  */
    /* JADX WARN: Removed duplicated region for block: B:750:0x0af8  */
    /* JADX WARN: Removed duplicated region for block: B:752:0x0b15  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x1e9d  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x237b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x2383  */
    /* JADX WARN: Removed duplicated region for block: B:948:0x124b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x241f  */
    /* JADX WARN: Type inference failed for: r0v19, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v24, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v25, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v292, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v311, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v33, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v491, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v58, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v60, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v65, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v66, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v67, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v69, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v73, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v74, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v75, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v77, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v80, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v81, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v82, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v92, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v93, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r0v94, types: [org.telegram.ui.ActionBar.INavigationLayout] */
    /* JADX WARN: Type inference failed for: r11v7, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r1v180, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v195, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v210, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r1v217, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r1v278, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v414, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v421, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v433, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r2v244, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r2v25, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v31, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v34, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v40, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v50, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v52, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r2v62, types: [org.telegram.ui.ActionBar.INavigationLayout$NavigationParams] */
    /* JADX WARN: Type inference failed for: r30v56 */
    /* JADX WARN: Type inference failed for: r30v57 */
    /* JADX WARN: Type inference failed for: r30v60 */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r40v38 */
    /* JADX WARN: Type inference failed for: r40v39 */
    /* JADX WARN: Type inference failed for: r40v43 */
    /* JADX WARN: Type inference failed for: r7v57, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r8v13, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v21 */
    /* JADX WARN: Type inference failed for: r8v37 */
    /* JADX WARN: Type inference failed for: r8v39 */
    /* JADX WARN: Type inference failed for: r8v46 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v211 */
    /* JADX WARN: Type inference failed for: r9v219 */
    /* JADX WARN: Type inference failed for: r9v220 */
    /* JADX WARN: Type inference failed for: r9v4, types: [int, boolean] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3) {
        long j;
        String str;
        final LaunchActivity launchActivity;
        final boolean z4;
        final int[] iArr;
        Intent intent2;
        long j2;
        long j3;
        long j4;
        int i;
        int i2;
        int i3;
        String str2;
        String str3;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        Intent intent3;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15;
        Intent intent4;
        Intent intent5;
        Intent intent6;
        String str4;
        String str5;
        int i4;
        int i5;
        char c;
        Intent intent7;
        boolean z16;
        boolean z17;
        boolean z18;
        boolean z19;
        boolean z20;
        boolean z21;
        boolean z22;
        ?? r9;
        ?? r8;
        GroupCallActivity groupCallActivity;
        ?? r3;
        boolean z23;
        boolean z24;
        String str6;
        char c2;
        BaseFragment editWidgetActivity;
        final boolean z25;
        final BaseFragment baseFragment;
        BaseFragment baseFragment2;
        boolean z26;
        boolean z27;
        TLRPC$TL_forumTopic findTopic;
        boolean z28;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        boolean z29;
        Pattern compile;
        long j5;
        long j6;
        int i6;
        int i7;
        int i8;
        char c3;
        String str7;
        int[] iArr2;
        long j7;
        int i9;
        char c4;
        boolean z30;
        String str8;
        String str9;
        String str10;
        long j8;
        String str11;
        long j9;
        long j10;
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
        boolean z31;
        String str22;
        String str23;
        Integer num;
        int i10;
        char c5;
        String str24;
        String str25;
        String str26;
        String str27;
        String str28;
        String str29;
        String str30;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        int i11;
        String str36;
        String str37;
        String str38;
        String str39;
        final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Cursor query;
        boolean z32;
        char c6;
        long j11;
        Integer num2;
        ?? parseInt;
        Integer num3;
        String str40;
        String str41;
        Integer num4;
        boolean z33;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z34;
        int i12;
        long j12;
        long j13;
        int parseInt2;
        boolean z35;
        long j14;
        boolean z36;
        boolean z37;
        boolean z38;
        String str42;
        String str43;
        String str44;
        String str45;
        String str46;
        String str47;
        String str48;
        String str49;
        String str50;
        String str51;
        String str52;
        String str53;
        String str54;
        String str55;
        String str56;
        int i13;
        String str57;
        boolean z39;
        String str58;
        String str59;
        String str60;
        String str61;
        String str62;
        String str63;
        String str64;
        Integer num5;
        String str65;
        String str66;
        ?? r40;
        int i14;
        int i15;
        String str67;
        ?? r30;
        ?? parseInt3;
        Integer num6;
        Integer num7;
        String str68;
        String str69;
        String str70;
        String str71;
        boolean z40;
        String queryParameter3;
        String queryParameter4;
        String[] split2;
        String substring;
        long j15;
        String type2;
        String stringExtra;
        Parcelable parcelableExtra;
        boolean z41;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str72;
        String str73;
        if (AndroidUtilities.handleProxyIntent(this, intent)) {
            this.actionBarLayout.rebuildFragments(1);
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.rebuildFragments(1);
                this.rightActionBarLayout.rebuildFragments(1);
            }
            return true;
        }
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible() && (intent == null || !"android.intent.action.MAIN".equals(intent.getAction()))) {
            PhotoViewer.getInstance().closePhoto(false, true);
        }
        int flags = intent.getFlags();
        String action = intent.getAction();
        int[] iArr3 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(iArr3[0], true);
        boolean z42 = action != null && action.equals("voip");
        if (!z3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z42) {
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
        String str74 = "message_id";
        if ((1048576 & flags) == 0 && intent.getAction() != null && !z2) {
            String str75 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j15 = intent.getExtras().getLong("dialogId", 0L);
                    if (j15 == 0) {
                        try {
                            String string = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                for (int i16 = 0; i16 < size; i16++) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i16);
                                    if (string.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j16 = extras.getLong("dialogId", 0L);
                                        try {
                                            str72 = extras.getString("hash", null);
                                            j15 = j16;
                                            break;
                                        } catch (Throwable th) {
                                            th = th;
                                            j15 = j16;
                                            FileLog.e(th);
                                            str72 = null;
                                            str73 = SharedConfig.directShareHash;
                                            if (str73 != null) {
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
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        str72 = null;
                    } else {
                        str72 = intent.getExtras().getString("hash", null);
                    }
                    str73 = SharedConfig.directShareHash;
                    if (str73 != null) {
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
                    z41 = true;
                    if (z41) {
                    }
                    str = " ";
                    launchActivity = this;
                    j4 = j15;
                    i = -1;
                    i2 = 0;
                    i3 = -1;
                    str2 = null;
                    j2 = 0;
                    z4 = false;
                    j3 = 0;
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
                    str4 = null;
                    str5 = null;
                    i4 = 0;
                    i5 = 0;
                    c = 0;
                    iArr = iArr3;
                    intent7 = intent;
                    str3 = null;
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
                        boolean z43 = parcelableExtra instanceof Uri;
                        Uri uri2 = parcelableExtra;
                        if (!z43) {
                            uri2 = Uri.parse(parcelableExtra.toString());
                        }
                        Uri uri3 = (Uri) uri2;
                        boolean z44 = uri3 != null && AndroidUtilities.isInternalUri(uri3);
                        if (!z44 && uri3 != null) {
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
                                    Set<String> set = MessagesController.getInstance(iArr3[0]).exportUri;
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
                                            path = path.replace("file://", str75);
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
                        z41 = z44;
                        if (z41) {
                            Toast.makeText(this, "Unsupported content", 0).show();
                        }
                        str = " ";
                        launchActivity = this;
                        j4 = j15;
                        i = -1;
                        i2 = 0;
                        i3 = -1;
                        str2 = null;
                        j2 = 0;
                        z4 = false;
                        j3 = 0;
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
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        c = 0;
                        iArr = iArr3;
                        intent7 = intent;
                        str3 = null;
                    }
                }
                z41 = false;
                if (z41) {
                }
                str = " ";
                launchActivity = this;
                j4 = j15;
                i = -1;
                i2 = 0;
                i3 = -1;
                str2 = null;
                j2 = 0;
                z4 = false;
                j3 = 0;
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
                str4 = null;
                str5 = null;
                i4 = 0;
                i5 = 0;
                c = 0;
                iArr = iArr3;
                intent7 = intent;
                str3 = null;
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
                        int i17 = 0;
                        while (i17 < parcelableArrayListExtra.size()) {
                            Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i17);
                            boolean z45 = parcelable instanceof Uri;
                            Uri uri5 = parcelable;
                            if (!z45) {
                                uri5 = Uri.parse(parcelable.toString());
                            }
                            Uri uri6 = (Uri) uri5;
                            if (uri6 != null && AndroidUtilities.isInternalUri(uri6)) {
                                parcelableArrayListExtra.remove(i17);
                                i17--;
                            }
                            i17++;
                        }
                        if (parcelableArrayListExtra.isEmpty()) {
                            arrayList = null;
                            if (arrayList != null) {
                                if (type != null && type.startsWith("image/")) {
                                    for (int i18 = 0; i18 < arrayList.size(); i18++) {
                                        Parcelable parcelable2 = (Parcelable) arrayList.get(i18);
                                        boolean z46 = parcelable2 instanceof Uri;
                                        Uri uri7 = parcelable2;
                                        if (!z46) {
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
                                    for (int i19 = 0; i19 < arrayList.size(); i19++) {
                                        Object obj = (Parcelable) arrayList.get(i19);
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
                                                    z29 = true;
                                                    break;
                                                }
                                            }
                                            z29 = false;
                                            if (!z29) {
                                                if (str77.startsWith("content://com.kakao.talk") && str77.endsWith("KakaoTalkChats.txt")) {
                                                    this.exportingChatUri = uri9;
                                                }
                                            }
                                        }
                                        if (path2 != null) {
                                            if (path2.startsWith("file:")) {
                                                path2 = path2.replace("file://", str75);
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
                                z28 = false;
                                if (z28) {
                                    Toast.makeText(this, "Unsupported content", 0).show();
                                }
                            }
                            z28 = true;
                            if (z28) {
                            }
                        }
                    }
                    arrayList = parcelableArrayListExtra;
                    if (arrayList != null) {
                    }
                    z28 = true;
                    if (z28) {
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
                                        j9 = 0;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$handleIntent$11(uri10);
                                            }
                                        });
                                        j10 = j9;
                                        j8 = j10;
                                        str12 = null;
                                        str13 = null;
                                        str14 = null;
                                        str15 = null;
                                        str16 = null;
                                        str17 = null;
                                        str75 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        z31 = false;
                                        str22 = null;
                                        str23 = null;
                                        num = null;
                                        i10 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z30 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        str11 = null;
                                        str27 = null;
                                        str26 = null;
                                        str25 = null;
                                        str24 = null;
                                        str28 = null;
                                        str33 = null;
                                        str32 = null;
                                        str31 = null;
                                        str30 = null;
                                        str29 = null;
                                        str34 = null;
                                        str35 = null;
                                        i11 = -1;
                                        str36 = null;
                                        str37 = null;
                                        str38 = null;
                                        break;
                                    } else {
                                        if (uri10.startsWith("tg:resolve") || uri10.startsWith("tg://resolve")) {
                                            str17 = null;
                                            j11 = 0;
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
                                                str25 = hashMap;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                str23 = null;
                                                num3 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str24 = null;
                                            } else {
                                                String queryParameter7 = parse.getQueryParameter("start");
                                                String queryParameter8 = parse.getQueryParameter("startgroup");
                                                String queryParameter9 = parse.getQueryParameter("startchannel");
                                                String queryParameter10 = parse.getQueryParameter("admin");
                                                String queryParameter11 = parse.getQueryParameter("game");
                                                String queryParameter12 = parse.getQueryParameter("voicechat");
                                                String str79 = queryParameter5;
                                                String queryParameter13 = parse.getQueryParameter("livestream");
                                                String queryParameter14 = parse.getQueryParameter("startattach");
                                                String queryParameter15 = parse.getQueryParameter("choose");
                                                String queryParameter16 = parse.getQueryParameter("attach");
                                                ?? parseInt4 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                                String str80 = parseInt4.intValue() == 0 ? null : parseInt4;
                                                Integer parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("thread"));
                                                if (parseInt5.intValue() == 0) {
                                                    parseInt5 = null;
                                                }
                                                if (parseInt5 == null) {
                                                    parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("topic"));
                                                    if (parseInt5.intValue() == 0) {
                                                        num2 = null;
                                                        parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                        str35 = queryParameter13;
                                                        if (parseInt.intValue() != 0) {
                                                            str26 = queryParameter11;
                                                            str34 = queryParameter12;
                                                            j10 = 0;
                                                            j8 = 0;
                                                            str17 = str79;
                                                            str36 = queryParameter14;
                                                            str38 = queryParameter15;
                                                            str37 = queryParameter16;
                                                            str22 = str80;
                                                            num = num2;
                                                            str12 = null;
                                                            str13 = null;
                                                            str75 = null;
                                                            z31 = false;
                                                            str23 = null;
                                                            i10 = 0;
                                                            c5 = 0;
                                                            z10 = false;
                                                            z11 = false;
                                                            z30 = false;
                                                            z12 = false;
                                                            z13 = false;
                                                            z14 = false;
                                                            z15 = false;
                                                            str8 = null;
                                                            str9 = null;
                                                            str10 = null;
                                                            str11 = null;
                                                            str27 = null;
                                                        } else {
                                                            str27 = parseInt;
                                                            str26 = queryParameter11;
                                                            str34 = queryParameter12;
                                                            j10 = 0;
                                                            j8 = 0;
                                                            str17 = str79;
                                                            str36 = queryParameter14;
                                                            str38 = queryParameter15;
                                                            str37 = queryParameter16;
                                                            str22 = str80;
                                                            num = num2;
                                                            str12 = null;
                                                            str13 = null;
                                                            str75 = null;
                                                            z31 = false;
                                                            str23 = null;
                                                            i10 = 0;
                                                            c5 = 0;
                                                            z10 = false;
                                                            z11 = false;
                                                            z30 = false;
                                                            z12 = false;
                                                            z13 = false;
                                                            z14 = false;
                                                            z15 = false;
                                                            str8 = null;
                                                            str9 = null;
                                                            str10 = null;
                                                            str11 = null;
                                                        }
                                                        str25 = null;
                                                        str24 = null;
                                                        str28 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        str30 = null;
                                                        str29 = null;
                                                        i11 = -1;
                                                        str18 = queryParameter7;
                                                        str19 = queryParameter8;
                                                        str20 = queryParameter9;
                                                        str21 = queryParameter10;
                                                        str14 = null;
                                                        str15 = null;
                                                        str16 = null;
                                                        break;
                                                    }
                                                }
                                                num2 = parseInt5;
                                                parseInt = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                str35 = queryParameter13;
                                                if (parseInt.intValue() != 0) {
                                                }
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                i11 = -1;
                                                str18 = queryParameter7;
                                                str19 = queryParameter8;
                                                str20 = queryParameter9;
                                                str21 = queryParameter10;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                            }
                                        } else if (uri10.startsWith("tg:invoice") || uri10.startsWith("tg://invoice")) {
                                            str17 = null;
                                            j11 = 0;
                                            str30 = Uri.parse(uri10.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str75 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str23 = null;
                                            num3 = null;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str28 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str29 = null;
                                            str34 = str29;
                                            str35 = str34;
                                            str36 = str35;
                                            str37 = str36;
                                            str38 = str37;
                                            j10 = j11;
                                            j8 = j10;
                                            z31 = false;
                                            i10 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z30 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            num = num3;
                                            i11 = -1;
                                            break;
                                        } else if (uri10.startsWith("tg:privatepost") || uri10.startsWith("tg://privatepost")) {
                                            str17 = null;
                                            Uri parse2 = Uri.parse(uri10.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                            ?? parseInt6 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("post"));
                                            ?? parseLong = Utilities.parseLong(parse2.getQueryParameter("channel"));
                                            if (parseInt6.intValue() != 0) {
                                                j11 = 0;
                                                int i20 = (parseLong.longValue() > 0L ? 1 : (parseLong.longValue() == 0L ? 0 : -1));
                                                str40 = parseLong;
                                                str41 = parseInt6;
                                                break;
                                            } else {
                                                j11 = 0;
                                            }
                                            str40 = null;
                                            str41 = null;
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
                                                str23 = str40;
                                                num4 = parseInt7;
                                                str22 = str41;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                            } else {
                                                str27 = parseInt8;
                                                str23 = str40;
                                                num4 = parseInt7;
                                                str22 = str41;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str26 = null;
                                            }
                                            str25 = str26;
                                            str24 = str25;
                                            num3 = num4;
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
                                                z33 = true;
                                                str17 = null;
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
                                                    str17 = null;
                                                    try {
                                                        tLRPC$TL_wallPaper.slug = null;
                                                        z33 = true;
                                                    } catch (Exception unused2) {
                                                    }
                                                }
                                                str17 = null;
                                                z33 = false;
                                            }
                                            if (!z33) {
                                                String queryParameter19 = parse3.getQueryParameter("mode");
                                                if (queryParameter19 != null && (split = queryParameter19.toLowerCase().split(" ")) != null && split.length > 0) {
                                                    for (int i21 = 0; i21 < split.length; i21++) {
                                                        if ("blur".equals(split[i21])) {
                                                            tLRPC$TL_wallPaper.settings.blur = true;
                                                        } else if ("motion".equals(split[i21])) {
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
                                            str31 = tLRPC$TL_wallPaper;
                                            str12 = str17;
                                            str13 = str12;
                                            str14 = str13;
                                            str15 = str14;
                                            str16 = str15;
                                            str75 = str16;
                                            str18 = str75;
                                            str19 = str18;
                                            str20 = str19;
                                            str21 = str20;
                                            str22 = str21;
                                            str23 = str22;
                                            String str83 = str23;
                                            str8 = str83;
                                            str9 = str8;
                                            str10 = str9;
                                            str11 = str10;
                                            str27 = str11;
                                            str26 = str27;
                                            str25 = str26;
                                            str24 = str25;
                                            str28 = str24;
                                            str33 = str28;
                                            str32 = str33;
                                            str30 = str32;
                                            str29 = str30;
                                            str34 = str29;
                                            str35 = str34;
                                            str36 = str35;
                                            str37 = str36;
                                            str38 = str37;
                                            z31 = false;
                                            i10 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z30 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j10 = 0;
                                            j8 = 0;
                                            num = str83;
                                            i11 = -1;
                                        } else {
                                            if (uri10.startsWith("tg:join") || uri10.startsWith("tg://join")) {
                                                str14 = Uri.parse(uri10.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                                str12 = null;
                                                str13 = null;
                                            } else if (uri10.startsWith("tg:addstickers") || uri10.startsWith("tg://addstickers")) {
                                                str15 = Uri.parse(uri10.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:addemoji") || uri10.startsWith("tg://addemoji")) {
                                                str16 = Uri.parse(uri10.replace("tg:addemoji", "tg://telegram.org").replace("tg://addemoji", "tg://telegram.org")).getQueryParameter("set");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:msg") || uri10.startsWith("tg://msg") || uri10.startsWith("tg://share") || uri10.startsWith("tg:share")) {
                                                Uri parse4 = Uri.parse(uri10.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                                String queryParameter20 = parse4.getQueryParameter("url");
                                                if (queryParameter20 != null) {
                                                    str75 = queryParameter20;
                                                }
                                                if (parse4.getQueryParameter("text") != null) {
                                                    if (str75.length() > 0) {
                                                        str75 = str75 + "\n";
                                                        z34 = true;
                                                    } else {
                                                        z34 = false;
                                                    }
                                                    str75 = str75 + parse4.getQueryParameter("text");
                                                } else {
                                                    z34 = false;
                                                }
                                                if (str75.length() > 16384) {
                                                    i12 = 0;
                                                    str75 = str75.substring(0, 16384);
                                                } else {
                                                    i12 = 0;
                                                }
                                                while (str75.endsWith("\n")) {
                                                    str75 = str75.substring(i12, str75.length() - 1);
                                                }
                                                z31 = z34;
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
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:confirmphone") || uri10.startsWith("tg://confirmphone")) {
                                                Uri parse5 = Uri.parse(uri10.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                                String queryParameter21 = parse5.getQueryParameter("phone");
                                                str13 = parse5.getQueryParameter("hash");
                                                str12 = queryParameter21;
                                                str14 = null;
                                            } else if (uri10.startsWith("tg:login") || uri10.startsWith("tg://login")) {
                                                Uri parse6 = Uri.parse(uri10.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                                String queryParameter22 = parse6.getQueryParameter("token");
                                                int intValue = Utilities.parseInt((CharSequence) parse6.getQueryParameter("code")).intValue();
                                                str33 = intValue != 0 ? str75 + intValue : null;
                                                str32 = queryParameter22;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:openmessage") || uri10.startsWith("tg://openmessage")) {
                                                Uri parse7 = Uri.parse(uri10.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                                String queryParameter23 = parse7.getQueryParameter("user_id");
                                                String queryParameter24 = parse7.getQueryParameter("chat_id");
                                                String queryParameter25 = parse7.getQueryParameter(str74);
                                                if (queryParameter23 != null) {
                                                    j13 = Long.parseLong(queryParameter23);
                                                    j12 = 0;
                                                    if (queryParameter25 != null) {
                                                        try {
                                                            parseInt2 = Integer.parseInt(queryParameter25);
                                                        } catch (NumberFormatException unused6) {
                                                        }
                                                        i10 = parseInt2;
                                                        j8 = j12;
                                                        j10 = j13;
                                                        str12 = null;
                                                        str13 = null;
                                                        str14 = null;
                                                        str15 = null;
                                                        str16 = null;
                                                        str17 = null;
                                                        str75 = null;
                                                        str18 = null;
                                                        str19 = null;
                                                        str20 = null;
                                                        str21 = null;
                                                        z31 = false;
                                                        str22 = null;
                                                        str23 = null;
                                                        num = null;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z30 = false;
                                                        z12 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        str8 = null;
                                                        str9 = null;
                                                        str10 = null;
                                                        str11 = null;
                                                        str27 = null;
                                                        str26 = null;
                                                        str25 = null;
                                                        str24 = null;
                                                        str28 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        str30 = null;
                                                        str29 = null;
                                                        str34 = null;
                                                        str35 = null;
                                                        i11 = -1;
                                                        str36 = null;
                                                        str37 = null;
                                                        str38 = null;
                                                    }
                                                    parseInt2 = 0;
                                                    i10 = parseInt2;
                                                    j8 = j12;
                                                    j10 = j13;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str25 = null;
                                                    str24 = null;
                                                    str28 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str35 = null;
                                                    i11 = -1;
                                                    str36 = null;
                                                    str37 = null;
                                                    str38 = null;
                                                } else {
                                                    if (queryParameter24 != null) {
                                                        j12 = Long.parseLong(queryParameter24);
                                                        j13 = 0;
                                                        if (queryParameter25 != null) {
                                                        }
                                                        parseInt2 = 0;
                                                        i10 = parseInt2;
                                                        j8 = j12;
                                                        j10 = j13;
                                                        str12 = null;
                                                        str13 = null;
                                                        str14 = null;
                                                        str15 = null;
                                                        str16 = null;
                                                        str17 = null;
                                                        str75 = null;
                                                        str18 = null;
                                                        str19 = null;
                                                        str20 = null;
                                                        str21 = null;
                                                        z31 = false;
                                                        str22 = null;
                                                        str23 = null;
                                                        num = null;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z30 = false;
                                                        z12 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        str8 = null;
                                                        str9 = null;
                                                        str10 = null;
                                                        str11 = null;
                                                        str27 = null;
                                                        str26 = null;
                                                        str25 = null;
                                                        str24 = null;
                                                        str28 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        str30 = null;
                                                        str29 = null;
                                                        str34 = null;
                                                        str35 = null;
                                                        i11 = -1;
                                                        str36 = null;
                                                        str37 = null;
                                                        str38 = null;
                                                    }
                                                    j12 = 0;
                                                    j13 = 0;
                                                    if (queryParameter25 != null) {
                                                    }
                                                    parseInt2 = 0;
                                                    i10 = parseInt2;
                                                    j8 = j12;
                                                    j10 = j13;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str25 = null;
                                                    str24 = null;
                                                    str28 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str35 = null;
                                                    i11 = -1;
                                                    str36 = null;
                                                    str37 = null;
                                                    str38 = null;
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
                                                str25 = hashMap2;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:setlanguage") || uri10.startsWith("tg://setlanguage")) {
                                                str24 = Uri.parse(uri10.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:addtheme") || uri10.startsWith("tg://addtheme")) {
                                                str29 = Uri.parse(uri10.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:settings") || uri10.startsWith("tg://settings")) {
                                                if (uri10.contains("themes")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 2;
                                                } else if (uri10.contains("devices")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 3;
                                                } else if (uri10.contains("folders")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 4;
                                                } else if (uri10.contains("change_number")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 5;
                                                } else {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 1;
                                                }
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:search") || uri10.startsWith("tg://search")) {
                                                String queryParameter27 = Uri.parse(uri10.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                                if (queryParameter27 != null) {
                                                    str75 = queryParameter27.trim();
                                                }
                                                str11 = str75;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:calllog") || uri10.startsWith("tg://calllog")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = true;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else if (uri10.startsWith("tg:call") || uri10.startsWith("tg://call")) {
                                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                                    if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                        String queryParameter28 = data.getQueryParameter("format");
                                                        String queryParameter29 = data.getQueryParameter("name");
                                                        String queryParameter30 = data.getQueryParameter("phone");
                                                        z35 = false;
                                                        List<TLRPC$TL_contact> findContacts = findContacts(queryParameter29, queryParameter30, false);
                                                        if (!findContacts.isEmpty() || queryParameter30 == null) {
                                                            j14 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                            if (j14 != 0) {
                                                                str75 = null;
                                                            } else if (queryParameter29 != null) {
                                                                str75 = queryParameter29;
                                                            }
                                                            boolean equalsIgnoreCase = MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(queryParameter28);
                                                            z36 = !equalsIgnoreCase;
                                                            z37 = equalsIgnoreCase;
                                                            z38 = false;
                                                            z35 = true;
                                                            str42 = null;
                                                        } else {
                                                            str43 = queryParameter30;
                                                            str42 = queryParameter29;
                                                            z38 = true;
                                                            z36 = false;
                                                            z37 = false;
                                                            j14 = 0;
                                                            str75 = null;
                                                            z14 = z38;
                                                            z11 = z36;
                                                            z30 = z37;
                                                            z12 = z35;
                                                            j10 = j14;
                                                            str9 = str42;
                                                            str8 = str75;
                                                            str10 = str43;
                                                            str12 = null;
                                                            str13 = null;
                                                            str14 = null;
                                                            str15 = null;
                                                            str16 = null;
                                                            str17 = null;
                                                            str75 = null;
                                                            str18 = null;
                                                            str19 = null;
                                                            str20 = null;
                                                            str21 = null;
                                                            z31 = false;
                                                            str22 = null;
                                                            str23 = null;
                                                            num = null;
                                                            i10 = 0;
                                                            c5 = 0;
                                                            z10 = false;
                                                            z13 = false;
                                                            z15 = false;
                                                            j8 = 0;
                                                            str11 = null;
                                                            str27 = null;
                                                            str26 = null;
                                                            str25 = null;
                                                            str24 = null;
                                                            str28 = null;
                                                            str33 = null;
                                                            str32 = null;
                                                            str31 = null;
                                                            str30 = null;
                                                            str29 = null;
                                                            str34 = null;
                                                            str35 = null;
                                                            i11 = -1;
                                                            str36 = null;
                                                            str37 = null;
                                                            str38 = null;
                                                        }
                                                    } else {
                                                        final Intent intent8 = new Intent(intent);
                                                        intent8.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                        intent8.putExtra("extra_force_call", true);
                                                        ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
                                                            @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                            public final void onResult(boolean z47) {
                                                                LaunchActivity.this.lambda$handleIntent$12(intent8, z47);
                                                            }
                                                        }, 1000L);
                                                        z38 = false;
                                                        z36 = false;
                                                        z37 = false;
                                                        z35 = false;
                                                        j14 = 0;
                                                        str42 = null;
                                                        str75 = null;
                                                    }
                                                    str43 = null;
                                                    z14 = z38;
                                                    z11 = z36;
                                                    z30 = z37;
                                                    z12 = z35;
                                                    j10 = j14;
                                                    str9 = str42;
                                                    str8 = str75;
                                                    str10 = str43;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str75 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z31 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    num = null;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z13 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str25 = null;
                                                    str24 = null;
                                                    str28 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str35 = null;
                                                    i11 = -1;
                                                    str36 = null;
                                                    str37 = null;
                                                    str38 = null;
                                                }
                                            } else if (uri10.startsWith("tg:scanqr") || uri10.startsWith("tg://scanqr")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = true;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
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
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = true;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str28 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            } else {
                                                String replace = uri10.replace("tg://", str75).replace("tg:", str75);
                                                int indexOf = replace.indexOf(63);
                                                if (indexOf >= 0) {
                                                    replace = replace.substring(0, indexOf);
                                                }
                                                str28 = replace;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str75 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z31 = false;
                                                str22 = null;
                                                str23 = null;
                                                num = null;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str24 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str29 = null;
                                                str34 = null;
                                                str35 = null;
                                                i11 = -1;
                                                str36 = null;
                                                str37 = null;
                                                str38 = null;
                                            }
                                            str15 = null;
                                            str16 = null;
                                            str17 = null;
                                            str75 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            z31 = false;
                                            str22 = null;
                                            str23 = null;
                                            num = null;
                                            i10 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z30 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            j10 = 0;
                                            j8 = 0;
                                            str11 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str24 = null;
                                            str28 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str30 = null;
                                            str29 = null;
                                            str34 = null;
                                            str35 = null;
                                            i11 = -1;
                                            str36 = null;
                                            str37 = null;
                                            str38 = null;
                                        }
                                        str28 = str24;
                                        str33 = str28;
                                        str32 = str33;
                                        str31 = str32;
                                        str30 = str31;
                                        str29 = str30;
                                        str34 = str29;
                                        str35 = str34;
                                        str36 = str35;
                                        str37 = str36;
                                        str38 = str37;
                                        j10 = j11;
                                        j8 = j10;
                                        z31 = false;
                                        i10 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z30 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        num = num3;
                                        i11 = -1;
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
                                            sb.append(TextUtils.isEmpty(data.getPath()) ? str75 : data.getPath());
                                            sb.append(TextUtils.isEmpty(data.getQuery()) ? str75 : "?" + data.getQuery());
                                            data = Uri.parse(sb.toString());
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
                                                String replace2 = substring2.replace("bg/", str75);
                                                tLRPC$TL_wallPaper2.slug = replace2;
                                                if (replace2 != null && replace2.length() == 6) {
                                                    tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug, 16) | (-16777216);
                                                    tLRPC$TL_wallPaper2.slug = null;
                                                } else {
                                                    String str84 = tLRPC$TL_wallPaper2.slug;
                                                    if (str84 != null && str84.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(6))) {
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
                                                    z40 = false;
                                                    if (!z40) {
                                                        String queryParameter33 = data.getQueryParameter("mode");
                                                        if (queryParameter33 != null && (split2 = queryParameter33.toLowerCase().split(" ")) != null && split2.length > 0) {
                                                            for (int i22 = 0; i22 < split2.length; i22++) {
                                                                if ("blur".equals(split2[i22])) {
                                                                    tLRPC$TL_wallPaper2.settings.blur = true;
                                                                } else if ("motion".equals(split2[i22])) {
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
                                                    str59 = tLRPC$TL_wallPaper2;
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str85 = str52;
                                                    str18 = str55;
                                                    str75 = str85;
                                                    break;
                                                }
                                                z40 = true;
                                                if (!z40) {
                                                }
                                                str59 = tLRPC$TL_wallPaper2;
                                                str44 = null;
                                                z39 = false;
                                                str57 = null;
                                                i13 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str47 = null;
                                                str46 = null;
                                                str45 = null;
                                                str58 = null;
                                                str60 = null;
                                                str66 = null;
                                                str65 = null;
                                                r40 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                num5 = r40;
                                                i11 = i13;
                                                str19 = str56;
                                                str20 = str54;
                                                str26 = str50;
                                                str34 = str49;
                                                str35 = str48;
                                                str24 = str46;
                                                str29 = str45;
                                                str33 = str58;
                                                str31 = str59;
                                                str30 = str60;
                                                str22 = str66;
                                                num = num5;
                                                str27 = str64;
                                                str36 = str63;
                                                str37 = str62;
                                                str38 = str61;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str25 = null;
                                                str28 = null;
                                                str32 = null;
                                                str14 = str44;
                                                z31 = z39;
                                                str17 = str57;
                                                str12 = str51;
                                                str13 = str47;
                                                str23 = str65;
                                                z12 = false;
                                                str21 = str53;
                                                String str852 = str52;
                                                str18 = str55;
                                                str75 = str852;
                                            } else if (substring2.startsWith("login/")) {
                                                int intValue2 = Utilities.parseInt((CharSequence) substring2.replace("login/", str75)).intValue();
                                                str58 = intValue2 != 0 ? str75 + intValue2 : null;
                                                str44 = null;
                                                z39 = false;
                                                str57 = null;
                                                i13 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str47 = null;
                                                str46 = null;
                                                str45 = null;
                                                str59 = null;
                                                str60 = null;
                                                str66 = null;
                                                str65 = null;
                                                r40 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                num5 = r40;
                                                i11 = i13;
                                                str19 = str56;
                                                str20 = str54;
                                                str26 = str50;
                                                str34 = str49;
                                                str35 = str48;
                                                str24 = str46;
                                                str29 = str45;
                                                str33 = str58;
                                                str31 = str59;
                                                str30 = str60;
                                                str22 = str66;
                                                num = num5;
                                                str27 = str64;
                                                str36 = str63;
                                                str37 = str62;
                                                str38 = str61;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str25 = null;
                                                str28 = null;
                                                str32 = null;
                                                str14 = str44;
                                                z31 = z39;
                                                str17 = str57;
                                                str12 = str51;
                                                str13 = str47;
                                                str23 = str65;
                                                z12 = false;
                                                str21 = str53;
                                                String str8522 = str52;
                                                str18 = str55;
                                                str75 = str8522;
                                            } else {
                                                if (substring2.startsWith("joinchat/")) {
                                                    str44 = substring2.replace("joinchat/", str75);
                                                } else if (substring2.startsWith("+")) {
                                                    str44 = substring2.replace("+", str75);
                                                    if (AndroidUtilities.isNumeric(str44)) {
                                                        str57 = str44;
                                                        str44 = null;
                                                        z39 = false;
                                                        i13 = -1;
                                                        str15 = null;
                                                        str16 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str52 = null;
                                                        str51 = null;
                                                        str50 = null;
                                                        str49 = null;
                                                        str48 = null;
                                                        str47 = null;
                                                        str46 = null;
                                                        str45 = null;
                                                        str58 = null;
                                                        str59 = null;
                                                        str60 = null;
                                                        str66 = null;
                                                        str65 = null;
                                                        r40 = null;
                                                        str64 = null;
                                                        str63 = null;
                                                        str62 = null;
                                                        str61 = null;
                                                        num5 = r40;
                                                        i11 = i13;
                                                        str19 = str56;
                                                        str20 = str54;
                                                        str26 = str50;
                                                        str34 = str49;
                                                        str35 = str48;
                                                        str24 = str46;
                                                        str29 = str45;
                                                        str33 = str58;
                                                        str31 = str59;
                                                        str30 = str60;
                                                        str22 = str66;
                                                        num = num5;
                                                        str27 = str64;
                                                        str36 = str63;
                                                        str37 = str62;
                                                        str38 = str61;
                                                        i10 = 0;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z30 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        str8 = null;
                                                        str9 = null;
                                                        str10 = null;
                                                        j10 = 0;
                                                        j8 = 0;
                                                        str11 = null;
                                                        str25 = null;
                                                        str28 = null;
                                                        str32 = null;
                                                        str14 = str44;
                                                        z31 = z39;
                                                        str17 = str57;
                                                        str12 = str51;
                                                        str13 = str47;
                                                        str23 = str65;
                                                        z12 = false;
                                                        str21 = str53;
                                                        String str85222 = str52;
                                                        str18 = str55;
                                                        str75 = str85222;
                                                    }
                                                } else if (substring2.startsWith("addstickers/")) {
                                                    str15 = substring2.replace("addstickers/", str75);
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str852222 = str52;
                                                    str18 = str55;
                                                    str75 = str852222;
                                                } else if (substring2.startsWith("addemoji/")) {
                                                    str16 = substring2.replace("addemoji/", str75);
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str8522222 = str52;
                                                    str18 = str55;
                                                    str75 = str8522222;
                                                } else if (substring2.startsWith("msg/") || substring2.startsWith("share/")) {
                                                    String queryParameter35 = data.getQueryParameter("url");
                                                    if (queryParameter35 != null) {
                                                        str75 = queryParameter35;
                                                    }
                                                    if (data.getQueryParameter("text") != null) {
                                                        if (str75.length() > 0) {
                                                            str75 = str75 + "\n";
                                                            z39 = true;
                                                        } else {
                                                            z39 = false;
                                                        }
                                                        str75 = str75 + data.getQueryParameter("text");
                                                    } else {
                                                        z39 = false;
                                                    }
                                                    if (str75.length() > 16384) {
                                                        i14 = 0;
                                                        str75 = str75.substring(0, 16384);
                                                    } else {
                                                        i14 = 0;
                                                    }
                                                    while (str75.endsWith("\n")) {
                                                        str75 = str75.substring(i14, str75.length() - 1);
                                                    }
                                                    str52 = str75;
                                                    str44 = null;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str85222222 = str52;
                                                    str18 = str55;
                                                    str75 = str85222222;
                                                } else if (substring2.startsWith("confirmphone")) {
                                                    String queryParameter36 = data.getQueryParameter("phone");
                                                    str47 = data.getQueryParameter("hash");
                                                    str51 = queryParameter36;
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str852222222 = str52;
                                                    str18 = str55;
                                                    str75 = str852222222;
                                                } else if (substring2.startsWith("setlanguage/")) {
                                                    str46 = substring2.substring(12);
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str8522222222 = str52;
                                                    str18 = str55;
                                                    str75 = str8522222222;
                                                } else if (substring2.startsWith("addtheme/")) {
                                                    str45 = substring2.substring(9);
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    r40 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str85222222222 = str52;
                                                    str18 = str55;
                                                    str75 = str85222222222;
                                                } else if (substring2.startsWith("c/")) {
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() == 3) {
                                                        ?? parseLong2 = Utilities.parseLong(pathSegments.get(1));
                                                        ?? parseInt9 = Utilities.parseInt((CharSequence) pathSegments.get(2));
                                                        if (parseInt9.intValue() != 0) {
                                                            int i23 = (parseLong2.longValue() > 0L ? 1 : (parseLong2.longValue() == 0L ? 0 : -1));
                                                            str70 = parseInt9;
                                                            str71 = parseLong2;
                                                            break;
                                                        }
                                                        str70 = null;
                                                        str71 = null;
                                                        Integer parseInt10 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (parseInt10.intValue() == 0) {
                                                            parseInt10 = null;
                                                        }
                                                        if (parseInt10 == null) {
                                                            num7 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                            str68 = str70;
                                                            str69 = str71;
                                                            if (num7.intValue() == 0) {
                                                                num7 = null;
                                                                str68 = str70;
                                                                str69 = str71;
                                                            }
                                                        } else {
                                                            num7 = parseInt10;
                                                            str68 = str70;
                                                            str69 = str71;
                                                        }
                                                    } else {
                                                        num7 = null;
                                                        str68 = null;
                                                        str69 = null;
                                                    }
                                                    r40 = num7;
                                                    str66 = str68;
                                                    str65 = str69;
                                                    str44 = null;
                                                    z39 = false;
                                                    str57 = null;
                                                    i13 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str45 = null;
                                                    str58 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    num5 = r40;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str852222222222 = str52;
                                                    str18 = str55;
                                                    str75 = str852222222222;
                                                } else if (substring2.length() >= 1) {
                                                    ArrayList arrayList3 = new ArrayList(data.getPathSegments());
                                                    if (arrayList3.size() > 0) {
                                                        i15 = 0;
                                                        if (((String) arrayList3.get(0)).equals("s")) {
                                                            arrayList3.remove(0);
                                                        }
                                                    } else {
                                                        i15 = 0;
                                                    }
                                                    if (arrayList3.size() > 0) {
                                                        str57 = (String) arrayList3.get(i15);
                                                        if (arrayList3.size() > 1) {
                                                            ?? parseInt11 = Utilities.parseInt((CharSequence) arrayList3.get(1));
                                                            int intValue3 = parseInt11.intValue();
                                                            str67 = parseInt11;
                                                            break;
                                                        }
                                                        str67 = null;
                                                    } else {
                                                        str67 = null;
                                                        str57 = null;
                                                    }
                                                    i13 = str67 != null ? getTimestampFromLink(data) : -1;
                                                    str55 = data.getQueryParameter("start");
                                                    String queryParameter37 = data.getQueryParameter("startgroup");
                                                    String queryParameter38 = data.getQueryParameter("startchannel");
                                                    String queryParameter39 = data.getQueryParameter("admin");
                                                    String str86 = str67;
                                                    String queryParameter40 = data.getQueryParameter("game");
                                                    String queryParameter41 = data.getQueryParameter("voicechat");
                                                    String queryParameter42 = data.getQueryParameter("livestream");
                                                    String queryParameter43 = data.getQueryParameter("startattach");
                                                    String queryParameter44 = data.getQueryParameter("choose");
                                                    String queryParameter45 = data.getQueryParameter("attach");
                                                    Integer parseInt12 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                    if (parseInt12.intValue() == 0) {
                                                        parseInt12 = null;
                                                    }
                                                    if (parseInt12 == null) {
                                                        parseInt12 = Utilities.parseInt((CharSequence) data.getQueryParameter("topic"));
                                                        if (parseInt12.intValue() == 0) {
                                                            r30 = null;
                                                            parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                            str66 = str86;
                                                            if (parseInt3.intValue() != 0) {
                                                                str63 = queryParameter43;
                                                                str61 = queryParameter44;
                                                                str62 = queryParameter45;
                                                                num6 = r30;
                                                                str44 = null;
                                                                z39 = false;
                                                                str51 = null;
                                                                str47 = null;
                                                                str46 = null;
                                                                str45 = null;
                                                                str58 = null;
                                                                str59 = null;
                                                                str60 = null;
                                                                str65 = null;
                                                                str64 = null;
                                                            } else {
                                                                str64 = parseInt3;
                                                                str63 = queryParameter43;
                                                                str61 = queryParameter44;
                                                                str62 = queryParameter45;
                                                                num6 = r30;
                                                                str44 = null;
                                                                z39 = false;
                                                                str51 = null;
                                                                str47 = null;
                                                                str46 = null;
                                                                str45 = null;
                                                                str58 = null;
                                                                str59 = null;
                                                                str60 = null;
                                                                str65 = null;
                                                            }
                                                            str50 = queryParameter40;
                                                            str49 = queryParameter41;
                                                            str48 = queryParameter42;
                                                            str52 = null;
                                                            str54 = queryParameter38;
                                                            str53 = queryParameter39;
                                                            str16 = null;
                                                            str56 = queryParameter37;
                                                            str15 = null;
                                                            num5 = num6;
                                                            i11 = i13;
                                                            str19 = str56;
                                                            str20 = str54;
                                                            str26 = str50;
                                                            str34 = str49;
                                                            str35 = str48;
                                                            str24 = str46;
                                                            str29 = str45;
                                                            str33 = str58;
                                                            str31 = str59;
                                                            str30 = str60;
                                                            str22 = str66;
                                                            num = num5;
                                                            str27 = str64;
                                                            str36 = str63;
                                                            str37 = str62;
                                                            str38 = str61;
                                                            i10 = 0;
                                                            c5 = 0;
                                                            z10 = false;
                                                            z11 = false;
                                                            z30 = false;
                                                            z13 = false;
                                                            z14 = false;
                                                            z15 = false;
                                                            str8 = null;
                                                            str9 = null;
                                                            str10 = null;
                                                            j10 = 0;
                                                            j8 = 0;
                                                            str11 = null;
                                                            str25 = null;
                                                            str28 = null;
                                                            str32 = null;
                                                            str14 = str44;
                                                            z31 = z39;
                                                            str17 = str57;
                                                            str12 = str51;
                                                            str13 = str47;
                                                            str23 = str65;
                                                            z12 = false;
                                                            str21 = str53;
                                                            String str8522222222222 = str52;
                                                            str18 = str55;
                                                            str75 = str8522222222222;
                                                        }
                                                    }
                                                    r30 = parseInt12;
                                                    parseInt3 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                    str66 = str86;
                                                    if (parseInt3.intValue() != 0) {
                                                    }
                                                    str50 = queryParameter40;
                                                    str49 = queryParameter41;
                                                    str48 = queryParameter42;
                                                    str52 = null;
                                                    str54 = queryParameter38;
                                                    str53 = queryParameter39;
                                                    str16 = null;
                                                    str56 = queryParameter37;
                                                    str15 = null;
                                                    num5 = num6;
                                                    i11 = i13;
                                                    str19 = str56;
                                                    str20 = str54;
                                                    str26 = str50;
                                                    str34 = str49;
                                                    str35 = str48;
                                                    str24 = str46;
                                                    str29 = str45;
                                                    str33 = str58;
                                                    str31 = str59;
                                                    str30 = str60;
                                                    str22 = str66;
                                                    num = num5;
                                                    str27 = str64;
                                                    str36 = str63;
                                                    str37 = str62;
                                                    str38 = str61;
                                                    i10 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z30 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str25 = null;
                                                    str28 = null;
                                                    str32 = null;
                                                    str14 = str44;
                                                    z31 = z39;
                                                    str17 = str57;
                                                    str12 = str51;
                                                    str13 = str47;
                                                    str23 = str65;
                                                    z12 = false;
                                                    str21 = str53;
                                                    String str85222222222222 = str52;
                                                    str18 = str55;
                                                    str75 = str85222222222222;
                                                }
                                                z39 = false;
                                                str57 = null;
                                                i13 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str56 = null;
                                                str55 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str51 = null;
                                                str50 = null;
                                                str49 = null;
                                                str48 = null;
                                                str47 = null;
                                                str46 = null;
                                                str45 = null;
                                                str58 = null;
                                                str59 = null;
                                                str60 = null;
                                                str66 = null;
                                                str65 = null;
                                                r40 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                num5 = r40;
                                                i11 = i13;
                                                str19 = str56;
                                                str20 = str54;
                                                str26 = str50;
                                                str34 = str49;
                                                str35 = str48;
                                                str24 = str46;
                                                str29 = str45;
                                                str33 = str58;
                                                str31 = str59;
                                                str30 = str60;
                                                str22 = str66;
                                                num = num5;
                                                str27 = str64;
                                                str36 = str63;
                                                str37 = str62;
                                                str38 = str61;
                                                i10 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z30 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str25 = null;
                                                str28 = null;
                                                str32 = null;
                                                str14 = str44;
                                                z31 = z39;
                                                str17 = str57;
                                                str12 = str51;
                                                str13 = str47;
                                                str23 = str65;
                                                z12 = false;
                                                str21 = str53;
                                                String str852222222222222 = str52;
                                                str18 = str55;
                                                str75 = str852222222222222;
                                            }
                                            str60 = substring;
                                            str44 = null;
                                            z39 = false;
                                            str57 = null;
                                            i13 = -1;
                                            str15 = null;
                                            str16 = null;
                                            str56 = null;
                                            str55 = null;
                                            str54 = null;
                                            str53 = null;
                                            str52 = null;
                                            str51 = null;
                                            str50 = null;
                                            str49 = null;
                                            str48 = null;
                                            str47 = null;
                                            str46 = null;
                                            str45 = null;
                                            str58 = null;
                                            str59 = null;
                                            str66 = null;
                                            str65 = null;
                                            r40 = null;
                                            str64 = null;
                                            str63 = null;
                                            str62 = null;
                                            str61 = null;
                                            num5 = r40;
                                            i11 = i13;
                                            str19 = str56;
                                            str20 = str54;
                                            str26 = str50;
                                            str34 = str49;
                                            str35 = str48;
                                            str24 = str46;
                                            str29 = str45;
                                            str33 = str58;
                                            str31 = str59;
                                            str30 = str60;
                                            str22 = str66;
                                            num = num5;
                                            str27 = str64;
                                            str36 = str63;
                                            str37 = str62;
                                            str38 = str61;
                                            i10 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z30 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            j10 = 0;
                                            j8 = 0;
                                            str11 = null;
                                            str25 = null;
                                            str28 = null;
                                            str32 = null;
                                            str14 = str44;
                                            z31 = z39;
                                            str17 = str57;
                                            str12 = str51;
                                            str13 = str47;
                                            str23 = str65;
                                            z12 = false;
                                            str21 = str53;
                                            String str8522222222222222 = str52;
                                            str18 = str55;
                                            str75 = str8522222222222222;
                                        }
                                        str44 = null;
                                        z39 = false;
                                        str57 = null;
                                        i13 = -1;
                                        str15 = null;
                                        str16 = null;
                                        str56 = null;
                                        str55 = null;
                                        str54 = null;
                                        str53 = null;
                                        str52 = null;
                                        str51 = null;
                                        str50 = null;
                                        str49 = null;
                                        str48 = null;
                                        str47 = null;
                                        str46 = null;
                                        str45 = null;
                                        str58 = null;
                                        str59 = null;
                                        str60 = null;
                                        str66 = null;
                                        str65 = null;
                                        r40 = null;
                                        str64 = null;
                                        str63 = null;
                                        str62 = null;
                                        str61 = null;
                                        num5 = r40;
                                        i11 = i13;
                                        str19 = str56;
                                        str20 = str54;
                                        str26 = str50;
                                        str34 = str49;
                                        str35 = str48;
                                        str24 = str46;
                                        str29 = str45;
                                        str33 = str58;
                                        str31 = str59;
                                        str30 = str60;
                                        str22 = str66;
                                        num = num5;
                                        str27 = str64;
                                        str36 = str63;
                                        str37 = str62;
                                        str38 = str61;
                                        i10 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z30 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        j10 = 0;
                                        j8 = 0;
                                        str11 = null;
                                        str25 = null;
                                        str28 = null;
                                        str32 = null;
                                        str14 = str44;
                                        z31 = z39;
                                        str17 = str57;
                                        str12 = str51;
                                        str13 = str47;
                                        str23 = str65;
                                        z12 = false;
                                        str21 = str53;
                                        String str85222222222222222 = str52;
                                        str18 = str55;
                                        str75 = str85222222222222222;
                                    }
                                    break;
                            }
                            if (!intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                                str39 = str74;
                                FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(scheme) && str28 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                                intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                            } else {
                                str39 = str74;
                            }
                            if (str33 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                str7 = str39;
                            } else if (str12 == null || str13 != null) {
                                str = " ";
                                iArr2 = iArr3;
                                str7 = str39;
                                launchActivity = this;
                                final AlertDialog alertDialog = new AlertDialog(launchActivity, 3);
                                alertDialog.setCanCancel(false);
                                alertDialog.show();
                                tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                                tLRPC$TL_account_sendConfirmPhoneCode.hash = str13;
                                TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                                tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings;
                                tLRPC$TL_codeSettings.allow_flashcall = false;
                                tLRPC$TL_codeSettings.allow_app_hash = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                                if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                                    sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                                } else {
                                    sharedPreferences.edit().remove("sms_hash").apply();
                                }
                                final Bundle bundle = new Bundle();
                                bundle.putString("phone", str12);
                                final String str87 = str12;
                                ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$handleIntent$14(alertDialog, str87, bundle, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                                    }
                                }, 2);
                            } else if (str17 != null || str14 != null || str15 != null || str16 != null || str75 != null || str26 != null || str34 != null || str25 != null || str28 != null || str24 != null || str33 != null || str31 != null || str30 != null || str23 != null || str29 != null || str32 != null) {
                                if (str75 != null && str75.startsWith("@")) {
                                    str75 = " " + str75;
                                }
                                str = " ";
                                str7 = str39;
                                iArr2 = iArr3;
                                runLinkRequest(iArr3[0], str17, str14, str15, str16, str18, str19, str20, str21, str75, z31, str22, str23, num, str27, str26, str25, str24, str28, str33, str32, str31, str30, str29, str34, str35, 0, i11, str36, str37, str38);
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
                                            int intValue4 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                            int i24 = 0;
                                            while (true) {
                                                if (i24 < 4) {
                                                    if (UserConfig.getInstance(i24).getClientUserId() != intValue4) {
                                                        try {
                                                            i24++;
                                                        } catch (Throwable th4) {
                                                            th = th4;
                                                            try {
                                                                query.close();
                                                            } catch (Throwable unused11) {
                                                            }
                                                            try {
                                                                throw th;
                                                            } catch (Exception e6) {
                                                                e = e6;
                                                                FileLog.e(e);
                                                                str = " ";
                                                                iArr2 = iArr3;
                                                                launchActivity = this;
                                                                i9 = i10;
                                                                c4 = c5;
                                                                j7 = j10;
                                                                str7 = str39;
                                                                intent7 = intent;
                                                                i2 = i9;
                                                                c = c4;
                                                                z4 = z30;
                                                                str4 = str9;
                                                                str5 = str10;
                                                                j2 = j8;
                                                                str3 = str11;
                                                                str74 = str7;
                                                                iArr = iArr2;
                                                                i = -1;
                                                                z5 = false;
                                                                z6 = false;
                                                                z9 = false;
                                                                z8 = false;
                                                                z7 = false;
                                                                j4 = 0;
                                                                i4 = 0;
                                                                i5 = 0;
                                                                j3 = j7;
                                                                str2 = str8;
                                                                i3 = -1;
                                                                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                                }
                                                                z16 = false;
                                                                z17 = true;
                                                                z18 = z16;
                                                                z19 = z;
                                                                z21 = z18;
                                                                z20 = z17;
                                                                z22 = false;
                                                                r8 = z21;
                                                                r9 = z20;
                                                                if (!z22) {
                                                                }
                                                                if (z42) {
                                                                }
                                                                if (!z7) {
                                                                }
                                                                intent7.setAction(r8);
                                                                return z22;
                                                            }
                                                        }
                                                    } else {
                                                        iArr3[0] = i24;
                                                        switchToAccount(iArr3[0], true);
                                                    }
                                                }
                                            }
                                            long j17 = query.getLong(query.getColumnIndex("data4"));
                                            NotificationCenter.getInstance(iArr3[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            try {
                                                String string2 = query.getString(query.getColumnIndex("mimetype"));
                                                if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                    j10 = j17;
                                                    z32 = true;
                                                } else {
                                                    j10 = j17;
                                                    z32 = z11;
                                                    if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                        z30 = true;
                                                    }
                                                }
                                                if (query != null) {
                                                    try {
                                                        query.close();
                                                    } catch (Exception e7) {
                                                        e = e7;
                                                        z11 = z32;
                                                        FileLog.e(e);
                                                        str = " ";
                                                        iArr2 = iArr3;
                                                        launchActivity = this;
                                                        i9 = i10;
                                                        c4 = c5;
                                                        j7 = j10;
                                                        str7 = str39;
                                                        intent7 = intent;
                                                        i2 = i9;
                                                        c = c4;
                                                        z4 = z30;
                                                        str4 = str9;
                                                        str5 = str10;
                                                        j2 = j8;
                                                        str3 = str11;
                                                        str74 = str7;
                                                        iArr = iArr2;
                                                        i = -1;
                                                        z5 = false;
                                                        z6 = false;
                                                        z9 = false;
                                                        z8 = false;
                                                        z7 = false;
                                                        j4 = 0;
                                                        i4 = 0;
                                                        i5 = 0;
                                                        j3 = j7;
                                                        str2 = str8;
                                                        i3 = -1;
                                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        }
                                                        z16 = false;
                                                        z17 = true;
                                                        z18 = z16;
                                                        z19 = z;
                                                        z21 = z18;
                                                        z20 = z17;
                                                        z22 = false;
                                                        r8 = z21;
                                                        r9 = z20;
                                                        if (!z22) {
                                                        }
                                                        if (z42) {
                                                        }
                                                        if (!z7) {
                                                        }
                                                        intent7.setAction(r8);
                                                        return z22;
                                                    }
                                                }
                                                z11 = z32;
                                                str = " ";
                                                iArr2 = iArr3;
                                                launchActivity = this;
                                                i9 = i10;
                                                c4 = c5;
                                                j7 = j10;
                                                str7 = str39;
                                            } catch (Throwable th5) {
                                                th = th5;
                                                j10 = j17;
                                                query.close();
                                                throw th;
                                            }
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                }
                                z32 = z11;
                                if (query != null) {
                                }
                                z11 = z32;
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                i9 = i10;
                                c4 = c5;
                                j7 = j10;
                                str7 = str39;
                            }
                            i9 = i10;
                            c4 = c5;
                            j7 = j10;
                        }
                        j9 = 0;
                        j10 = j9;
                        j8 = j10;
                        str12 = null;
                        str13 = null;
                        str14 = null;
                        str15 = null;
                        str16 = null;
                        str17 = null;
                        str75 = null;
                        str18 = null;
                        str19 = null;
                        str20 = null;
                        str21 = null;
                        z31 = false;
                        str22 = null;
                        str23 = null;
                        num = null;
                        i10 = 0;
                        c5 = 0;
                        z10 = false;
                        z11 = false;
                        z30 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        str8 = null;
                        str9 = null;
                        str10 = null;
                        str11 = null;
                        str27 = null;
                        str26 = null;
                        str25 = null;
                        str24 = null;
                        str28 = null;
                        str33 = null;
                        str32 = null;
                        str31 = null;
                        str30 = null;
                        str29 = null;
                        str34 = null;
                        str35 = null;
                        i11 = -1;
                        str36 = null;
                        str37 = null;
                        str38 = null;
                        if (!intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                        }
                        if (str33 != null) {
                        }
                        if (str12 == null) {
                        }
                        str = " ";
                        iArr2 = iArr3;
                        str7 = str39;
                        launchActivity = this;
                        final AlertDialog alertDialog2 = new AlertDialog(launchActivity, 3);
                        alertDialog2.setCanCancel(false);
                        alertDialog2.show();
                        tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                        tLRPC$TL_account_sendConfirmPhoneCode.hash = str13;
                        TLRPC$TL_codeSettings tLRPC$TL_codeSettings2 = new TLRPC$TL_codeSettings();
                        tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings2;
                        tLRPC$TL_codeSettings2.allow_flashcall = false;
                        tLRPC$TL_codeSettings2.allow_app_hash = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                        SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                        }
                        final Bundle bundle2 = new Bundle();
                        bundle2.putString("phone", str12);
                        final String str872 = str12;
                        ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$handleIntent$14(alertDialog2, str872, bundle2, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                        i9 = i10;
                        c4 = c5;
                        j7 = j10;
                    } else {
                        str7 = str74;
                        str = " ";
                        iArr2 = iArr3;
                        launchActivity = this;
                        j7 = 0;
                        i9 = 0;
                        c4 = 0;
                        z10 = false;
                        z11 = false;
                        z30 = false;
                        z12 = false;
                        z13 = false;
                        z14 = false;
                        z15 = false;
                        str8 = null;
                        str9 = null;
                        str10 = null;
                        j8 = 0;
                        str11 = null;
                    }
                    intent7 = intent;
                    i2 = i9;
                    c = c4;
                    z4 = z30;
                    str4 = str9;
                    str5 = str10;
                    j2 = j8;
                    str3 = str11;
                    str74 = str7;
                    iArr = iArr2;
                    i = -1;
                    z5 = false;
                    z6 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    j4 = 0;
                    i4 = 0;
                    i5 = 0;
                    j3 = j7;
                    str2 = str8;
                    i3 = -1;
                } else {
                    str = " ";
                    launchActivity = this;
                    z4 = false;
                    int i25 = -1;
                    if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        intent7 = intent;
                        str74 = str74;
                        iArr = iArr3;
                        i = -1;
                        i2 = 0;
                        i3 = -1;
                        str2 = null;
                        j2 = 0;
                        j3 = 0;
                        str3 = null;
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
                        j4 = 0;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        c = 1;
                    } else if (intent.getAction().equals("new_dialog")) {
                        intent6 = intent;
                        str74 = str74;
                        iArr = iArr3;
                        i = -1;
                        i2 = 0;
                        i3 = -1;
                        str2 = null;
                        j2 = 0;
                        j3 = 0;
                        str3 = null;
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
                        j4 = 0;
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        i5 = 0;
                        c = 0;
                        intent7 = intent6;
                    } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                        Intent intent9 = intent;
                        long longExtra = intent9.getLongExtra("chatId", 0L);
                        long longExtra2 = intent9.getLongExtra("userId", 0L);
                        int intExtra = intent9.getIntExtra("encId", 0);
                        int intExtra2 = intent9.getIntExtra("appWidgetId", 0);
                        int intExtra3 = intent9.getIntExtra("topicId", 0);
                        if (intExtra2 != 0) {
                            i25 = intent9.getIntExtra("appWidgetType", 0);
                            j5 = 0;
                            j6 = 0;
                            i7 = intExtra2;
                            str74 = str74;
                            iArr = iArr3;
                            intExtra3 = 0;
                            i8 = 0;
                            i6 = 0;
                            z5 = false;
                            c3 = 6;
                        } else {
                            str74 = str74;
                            int intExtra4 = intent9.getIntExtra(str74, 0);
                            if (longExtra != 0) {
                                iArr = iArr3;
                                NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                j6 = longExtra;
                                i6 = intExtra4;
                                i7 = -1;
                                i8 = 0;
                                z5 = false;
                                c3 = 0;
                                j5 = 0;
                            } else {
                                iArr = iArr3;
                                if (longExtra2 != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    j5 = longExtra2;
                                    i6 = intExtra4;
                                    i7 = -1;
                                    intExtra3 = 0;
                                    i8 = 0;
                                    z5 = false;
                                    c3 = 0;
                                    j6 = 0;
                                } else if (intExtra != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    j5 = 0;
                                    j6 = 0;
                                    i6 = intExtra4;
                                    intExtra3 = 0;
                                    z5 = false;
                                    c3 = 0;
                                    i8 = intExtra;
                                    i7 = -1;
                                } else {
                                    j5 = 0;
                                    j6 = 0;
                                    i6 = intExtra4;
                                    i7 = -1;
                                    intExtra3 = 0;
                                    i8 = 0;
                                    z5 = true;
                                    c3 = 0;
                                }
                            }
                        }
                        j4 = 0;
                        i5 = intExtra3;
                        j2 = j6;
                        i4 = i8;
                        i2 = i6;
                        c = c3;
                        str3 = null;
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
                        str4 = null;
                        str5 = null;
                        j3 = j5;
                        str2 = null;
                        i3 = i7;
                        i = i25;
                        intent7 = intent9;
                    } else {
                        intent2 = intent;
                        str74 = str74;
                        iArr = iArr3;
                        j = 0;
                        if (intent.getAction().equals("com.tmessages.openplayer")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i = -1;
                            i2 = 0;
                            i3 = -1;
                            str2 = null;
                            str3 = null;
                            z5 = false;
                            z6 = true;
                            intent5 = intent2;
                            z9 = false;
                            intent4 = intent5;
                            z8 = false;
                            z7 = false;
                            intent3 = intent4;
                            z10 = false;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent6 = intent3;
                            str4 = null;
                            str5 = null;
                            i4 = 0;
                            i5 = 0;
                            c = 0;
                            intent7 = intent6;
                        } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i = -1;
                            i2 = 0;
                            i3 = -1;
                            str2 = null;
                            str3 = null;
                            z5 = false;
                            z6 = false;
                            z9 = true;
                            intent4 = intent2;
                            z8 = false;
                            z7 = false;
                            intent3 = intent4;
                            z10 = false;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent6 = intent3;
                            str4 = null;
                            str5 = null;
                            i4 = 0;
                            i5 = 0;
                            c = 0;
                            intent7 = intent6;
                        } else {
                            if (action.equals("voip_chat")) {
                                j2 = 0;
                                j3 = 0;
                                j4 = 0;
                                i = -1;
                                i2 = 0;
                                i3 = -1;
                                str2 = null;
                                str3 = null;
                                z5 = false;
                                z6 = false;
                                z9 = false;
                                z8 = false;
                                z7 = true;
                                intent3 = intent2;
                                z10 = false;
                                z11 = false;
                                z12 = false;
                                z13 = false;
                                z14 = false;
                                z15 = false;
                                intent6 = intent3;
                                str4 = null;
                                str5 = null;
                                i4 = 0;
                                i5 = 0;
                                c = 0;
                                intent7 = intent6;
                            }
                            j2 = j;
                            j3 = j2;
                            j4 = j3;
                            i = -1;
                            i2 = 0;
                            i3 = -1;
                            str2 = null;
                            str3 = null;
                            z5 = false;
                            z6 = false;
                            intent5 = intent2;
                            z9 = false;
                            intent4 = intent5;
                            z8 = false;
                            z7 = false;
                            intent3 = intent4;
                            z10 = false;
                            z11 = false;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            z15 = false;
                            intent6 = intent3;
                            str4 = null;
                            str5 = null;
                            i4 = 0;
                            i5 = 0;
                            c = 0;
                            intent7 = intent6;
                        }
                    }
                }
                str = " ";
                launchActivity = this;
                j = 0;
            }
            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                if (str3 != null) {
                    BaseFragment lastFragment = launchActivity.actionBarLayout.getLastFragment();
                    if (lastFragment instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) lastFragment;
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
                        z5 = true;
                    }
                    if (j3 == 0) {
                        if (!z11 && !z4) {
                            Bundle bundle3 = new Bundle();
                            bundle3.putLong("user_id", j3);
                            if (i2 != 0) {
                                bundle3.putInt(str74, i2);
                            }
                            if (!mainFragmentsStack.isEmpty()) {
                                MessagesController messagesController = MessagesController.getInstance(iArr[0]);
                                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                            }
                            if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle3)).setNoAnimation(r3))) {
                                launchActivity.drawerLayoutContainer.closeDrawer();
                                z22 = true;
                            }
                            z22 = false;
                        } else if (z12) {
                            BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                            if (lastFragment2 != null) {
                                AlertsCreator.createCallDialogAlert(lastFragment2, lastFragment2.getMessagesController().getUser(Long.valueOf(j3)), z4);
                            }
                        } else {
                            VoIPPendingCall.startOrSchedule(launchActivity, j3, z4, AccountInstance.getInstance(iArr[0]));
                        }
                    } else if (j2 != 0) {
                        Bundle bundle4 = new Bundle();
                        bundle4.putLong("chat_id", j2);
                        if (i2 != 0) {
                            bundle4.putInt(str74, i2);
                        }
                        if (!mainFragmentsStack.isEmpty()) {
                            MessagesController messagesController2 = MessagesController.getInstance(iArr[0]);
                            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                        }
                        ChatActivity chatActivity = new ChatActivity(bundle4);
                        int i26 = i5;
                        if (i26 > 0 && (findTopic = MessagesController.getInstance(launchActivity.currentAccount).getTopicsController().findTopic(j2, i26)) != null) {
                            TLRPC$Message tLRPC$Message = findTopic.topicStartMessage;
                            ArrayList<MessageObject> arrayList6 = new ArrayList<>();
                            TLRPC$Chat chat = MessagesController.getInstance(launchActivity.currentAccount).getChat(Long.valueOf(j2));
                            arrayList6.add(new MessageObject(launchActivity.currentAccount, tLRPC$Message, false, false));
                            chatActivity.setThreadMessages(arrayList6, chat, findTopic.id, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
                        }
                        if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(chatActivity).setNoAnimation(r3))) {
                            launchActivity.drawerLayoutContainer.closeDrawer();
                            z22 = true;
                        }
                        z22 = false;
                    } else {
                        int i27 = i4;
                        if (i27 != 0) {
                            Bundle bundle5 = new Bundle();
                            bundle5.putInt("enc_id", i27);
                            if (launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle5)).setNoAnimation(r3))) {
                                launchActivity.drawerLayoutContainer.closeDrawer();
                                z22 = true;
                            }
                            z22 = false;
                        } else {
                            if (z5) {
                                if (!AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.removeAllFragments();
                                } else if (!launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                    while (launchActivity.layersActionBarLayout.getFragmentStack().size() - r3 > 0) {
                                        INavigationLayout iNavigationLayout = launchActivity.layersActionBarLayout;
                                        iNavigationLayout.removeFragmentFromStack(iNavigationLayout.getFragmentStack().get(0));
                                    }
                                    z19 = false;
                                    launchActivity.layersActionBarLayout.closeLastFragment(false);
                                    z27 = false;
                                }
                                z19 = false;
                                z27 = false;
                            } else {
                                if (!z6) {
                                    z18 = false;
                                    z26 = false;
                                    z26 = false;
                                    z16 = false;
                                    z26 = false;
                                    z18 = false;
                                    z18 = false;
                                    r8 = 0;
                                    z18 = false;
                                    r8 = 0;
                                    if (z9) {
                                        if (!launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                            launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
                                                @Override // org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate
                                                public final void didSelectLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
                                                    LaunchActivity.this.lambda$handleIntent$16(iArr, sharingLocationInfo);
                                                }
                                            }, null));
                                        }
                                    } else {
                                        Uri uri11 = launchActivity.exportingChatUri;
                                        if (uri11 != null) {
                                            launchActivity.runImportRequest(uri11, launchActivity.documentsUrisArray);
                                            z17 = true;
                                            z18 = z16;
                                            z19 = z;
                                            z21 = z18;
                                            z20 = z17;
                                            z22 = false;
                                            r8 = z21;
                                            r9 = z20;
                                            if (!z22 && !z19) {
                                                if (!AndroidUtilities.isTablet()) {
                                                    if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        if (launchActivity.layersActionBarLayout.getFragmentStack().isEmpty()) {
                                                            launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        }
                                                    } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                        DialogsActivity dialogsActivity2 = new DialogsActivity(r8);
                                                        dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                                        if (str3 != null) {
                                                            dialogsActivity2.setInitialSearchString(str3);
                                                        }
                                                        launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r9, false);
                                                    }
                                                } else if (launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                                    if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        DialogsActivity dialogsActivity3 = new DialogsActivity(r8);
                                                        dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                                                        if (str3 != null) {
                                                            dialogsActivity3.setInitialSearchString(str3);
                                                        }
                                                        launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r9, false);
                                                    }
                                                }
                                                INavigationLayout iNavigationLayout2 = launchActivity.actionBarLayout;
                                                int i28 = r9 == true ? 1 : 0;
                                                int i29 = r9 == true ? 1 : 0;
                                                int i30 = r9 == true ? 1 : 0;
                                                int i31 = r9 == true ? 1 : 0;
                                                int i32 = r9 == true ? 1 : 0;
                                                int i33 = r9 == true ? 1 : 0;
                                                iNavigationLayout2.rebuildFragments(i28);
                                                if (AndroidUtilities.isTablet()) {
                                                    launchActivity.layersActionBarLayout.rebuildFragments(r9);
                                                    launchActivity.rightActionBarLayout.rebuildFragments(r9);
                                                }
                                            }
                                            if (z42) {
                                                VoIPFragment.show(launchActivity, iArr[0]);
                                            }
                                            if (!z7 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                                                groupCallActivity.dismiss();
                                            }
                                            intent7.setAction(r8);
                                            return z22;
                                        } else if (launchActivity.importingStickers != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    LaunchActivity.this.lambda$handleIntent$17();
                                                }
                                            });
                                        } else {
                                            if (launchActivity.videoPath == null && launchActivity.photoPathsArray == null && launchActivity.sendingText == null && launchActivity.documentsPathsArray == null && launchActivity.contactsToSend == null && launchActivity.documentsUrisArray == null) {
                                                char c7 = c;
                                                if (c7 != 0) {
                                                    if (c7 == r3) {
                                                        Bundle bundle6 = new Bundle();
                                                        bundle6.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                                        baseFragment2 = new ProfileActivity(bundle6);
                                                    } else if (c7 == 2) {
                                                        baseFragment2 = new ThemeActivity(0);
                                                    } else if (c7 == 3) {
                                                        baseFragment2 = new SessionsActivity(0);
                                                    } else if (c7 == 4) {
                                                        baseFragment2 = new FiltersSetupActivity();
                                                    } else if (c7 == 5) {
                                                        c2 = 6;
                                                        z25 = true;
                                                        baseFragment = new ActionIntroActivity(3);
                                                        if (c7 == c2) {
                                                            launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(baseFragment).setNoAnimation(r3));
                                                        } else {
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    LaunchActivity.this.lambda$handleIntent$18(baseFragment, z25);
                                                                }
                                                            });
                                                        }
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.rebuildFragments(r3);
                                                            launchActivity.rightActionBarLayout.rebuildFragments(r3);
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                        }
                                                    } else {
                                                        c2 = 6;
                                                        editWidgetActivity = c7 == 6 ? new EditWidgetActivity(i, i3) : null;
                                                        z25 = false;
                                                        baseFragment = editWidgetActivity;
                                                        if (c7 == c2) {
                                                        }
                                                        if (AndroidUtilities.isTablet()) {
                                                        }
                                                    }
                                                    c2 = 6;
                                                    editWidgetActivity = baseFragment2;
                                                    z25 = false;
                                                    baseFragment = editWidgetActivity;
                                                    if (c7 == c2) {
                                                    }
                                                    if (AndroidUtilities.isTablet()) {
                                                    }
                                                } else if (z8) {
                                                    ?? bundle7 = new Bundle();
                                                    bundle7.putBoolean("destroyAfterSelect", r3);
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new ContactsActivity(bundle7)).setNoAnimation(r3));
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(r3);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(r3);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else if (str2 != null) {
                                                    ?? bundle8 = new Bundle();
                                                    bundle8.putBoolean("destroyAfterSelect", r3);
                                                    bundle8.putBoolean("returnAsResult", r3);
                                                    bundle8.putBoolean("onlyUsers", r3);
                                                    bundle8.putBoolean("allowSelf", false);
                                                    ContactsActivity contactsActivity = new ContactsActivity(bundle8);
                                                    contactsActivity.setInitialSearchString(str2);
                                                    contactsActivity.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
                                                        @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                                        public final void didSelectContact(TLRPC$User tLRPC$User, String str88, ContactsActivity contactsActivity2) {
                                                            LaunchActivity.this.lambda$handleIntent$19(z4, iArr, tLRPC$User, str88, contactsActivity2);
                                                        }
                                                    });
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(contactsActivity).setRemoveLast(launchActivity.actionBarLayout.getLastFragment() instanceof ContactsActivity));
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(r3);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(r3);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else if (z15) {
                                                    final ActionIntroActivity actionIntroActivity = new ActionIntroActivity(5);
                                                    actionIntroActivity.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                                                        @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                                        public final void didFindQRCode(String str88) {
                                                            LaunchActivity.this.lambda$handleIntent$23(actionIntroActivity, str88);
                                                        }
                                                    });
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(actionIntroActivity).setNoAnimation(r3));
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.rebuildFragments(r3);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(r3);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else if (z13) {
                                                    NewContactActivity newContactActivity = new NewContactActivity();
                                                    String str88 = str4;
                                                    if (str88 != null) {
                                                        String[] split3 = str88.split(str, 2);
                                                        String str89 = split3[0];
                                                        if (split3.length > r3) {
                                                            char c8 = r3 == true ? 1 : 0;
                                                            char c9 = r3 == true ? 1 : 0;
                                                            char c10 = r3 == true ? 1 : 0;
                                                            char c11 = r3 == true ? 1 : 0;
                                                            str6 = split3[c8];
                                                        } else {
                                                            str6 = null;
                                                        }
                                                        newContactActivity.setInitialName(str89, str6);
                                                    }
                                                    String str90 = str5;
                                                    if (str90 != null) {
                                                        newContactActivity.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str90, r3), false);
                                                    }
                                                    launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(newContactActivity).setNoAnimation(r3));
                                                    if (AndroidUtilities.isTablet()) {
                                                        INavigationLayout iNavigationLayout3 = launchActivity.actionBarLayout;
                                                        int i34 = r3 == true ? 1 : 0;
                                                        int i35 = r3 == true ? 1 : 0;
                                                        int i36 = r3 == true ? 1 : 0;
                                                        int i37 = r3 == true ? 1 : 0;
                                                        iNavigationLayout3.rebuildFragments(i34);
                                                        launchActivity.rightActionBarLayout.rebuildFragments(r3);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else {
                                                    final String str91 = str4;
                                                    String str92 = str5;
                                                    if (z7) {
                                                        z17 = true;
                                                        z17 = true;
                                                        GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                        if (GroupCallActivity.groupCallInstance != null) {
                                                            GroupCallActivity.groupCallUiVisible = true;
                                                        }
                                                    } else {
                                                        z17 = true;
                                                        r9 = 1;
                                                        z23 = true;
                                                        z23 = true;
                                                        if (z14) {
                                                            final BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                                            if (lastFragment3 == null || lastFragment3.getParentActivity() == null) {
                                                                z22 = false;
                                                            } else {
                                                                final String phoneNumber = NewContactActivity.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str92, false);
                                                                lastFragment3.showDialog(new AlertDialog.Builder(lastFragment3.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", R.string.NewContactAlertMessage, PhoneFormat.getInstance().format(phoneNumber)))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                                                                    @Override // android.content.DialogInterface.OnClickListener
                                                                    public final void onClick(DialogInterface dialogInterface, int i38) {
                                                                        LaunchActivity.lambda$handleIntent$24(phoneNumber, str91, lastFragment3, dialogInterface, i38);
                                                                    }
                                                                }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
                                                                z22 = true;
                                                            }
                                                            z19 = z;
                                                        } else if (z10) {
                                                            launchActivity.actionBarLayout.presentFragment(new INavigationLayout.NavigationParams(new CallLogActivity()).setNoAnimation(true));
                                                            if (AndroidUtilities.isTablet()) {
                                                                launchActivity.actionBarLayout.rebuildFragments(1);
                                                                launchActivity.rightActionBarLayout.rebuildFragments(1);
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                            } else {
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                            }
                                                            z19 = z;
                                                            z24 = z23;
                                                            z22 = true;
                                                            r9 = z24;
                                                        }
                                                    }
                                                    z19 = z;
                                                    z21 = z18;
                                                    z20 = z17;
                                                    z22 = false;
                                                    r8 = z21;
                                                    r9 = z20;
                                                }
                                                z19 = z;
                                                z24 = true;
                                                z22 = true;
                                                r9 = z24;
                                            } else {
                                                z17 = true;
                                                z23 = true;
                                                if (!AndroidUtilities.isTablet()) {
                                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                }
                                                long j18 = j4;
                                                if (j18 == 0) {
                                                    launchActivity.openDialogsToSend(false);
                                                    z19 = z;
                                                    z24 = z23;
                                                    z22 = true;
                                                    r9 = z24;
                                                } else {
                                                    ArrayList<MessagesStorage.TopicKey> arrayList7 = new ArrayList<>();
                                                    arrayList7.add(MessagesStorage.TopicKey.of(j18, 0));
                                                    launchActivity.didSelectDialogs(null, arrayList7, null, false);
                                                    z19 = z;
                                                    z21 = z18;
                                                    z20 = z17;
                                                    z22 = false;
                                                    r8 = z21;
                                                    r9 = z20;
                                                }
                                            }
                                            if (!z22) {
                                                if (!AndroidUtilities.isTablet()) {
                                                }
                                                INavigationLayout iNavigationLayout22 = launchActivity.actionBarLayout;
                                                int i282 = r9 == true ? 1 : 0;
                                                int i292 = r9 == true ? 1 : 0;
                                                int i302 = r9 == true ? 1 : 0;
                                                int i312 = r9 == true ? 1 : 0;
                                                int i322 = r9 == true ? 1 : 0;
                                                int i332 = r9 == true ? 1 : 0;
                                                iNavigationLayout22.rebuildFragments(i282);
                                                if (AndroidUtilities.isTablet()) {
                                                }
                                            }
                                            if (z42) {
                                            }
                                            if (!z7) {
                                                groupCallActivity.dismiss();
                                            }
                                            intent7.setAction(r8);
                                            return z22;
                                        }
                                    }
                                } else if (!launchActivity.actionBarLayout.getFragmentStack().isEmpty()) {
                                    z26 = false;
                                    launchActivity.actionBarLayout.getFragmentStack().get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                                } else {
                                    z26 = false;
                                }
                                z19 = z;
                                z27 = z26;
                            }
                            z20 = true;
                            z21 = z27;
                            z22 = false;
                            r8 = z21;
                            r9 = z20;
                            if (!z22) {
                            }
                            if (z42) {
                            }
                            if (!z7) {
                            }
                            intent7.setAction(r8);
                            return z22;
                        }
                    }
                    z19 = z;
                    r8 = 0;
                    r9 = 1;
                    if (!z22) {
                    }
                    if (z42) {
                    }
                    if (!z7) {
                    }
                    intent7.setAction(r8);
                    return z22;
                }
                r3 = 1;
                if (j3 == 0) {
                }
                z19 = z;
                r8 = 0;
                r9 = 1;
                if (!z22) {
                }
                if (z42) {
                }
                if (!z7) {
                }
                intent7.setAction(r8);
                return z22;
            }
            z16 = false;
            z17 = true;
            z18 = z16;
            z19 = z;
            z21 = z18;
            z20 = z17;
            z22 = false;
            r8 = z21;
            r9 = z20;
            if (!z22) {
            }
            if (z42) {
            }
            if (!z7) {
            }
            intent7.setAction(r8);
            return z22;
        }
        j = 0;
        str = " ";
        launchActivity = this;
        z4 = false;
        iArr = iArr3;
        intent2 = intent;
        j2 = j;
        j3 = j2;
        j4 = j3;
        i = -1;
        i2 = 0;
        i3 = -1;
        str2 = null;
        str3 = null;
        z5 = false;
        z6 = false;
        intent5 = intent2;
        z9 = false;
        intent4 = intent5;
        z8 = false;
        z7 = false;
        intent3 = intent4;
        z10 = false;
        z11 = false;
        z12 = false;
        z13 = false;
        z14 = false;
        z15 = false;
        intent6 = intent3;
        str4 = null;
        str5 = null;
        i4 = 0;
        i5 = 0;
        c = 0;
        intent7 = intent6;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z16 = false;
        z17 = true;
        z18 = z16;
        z19 = z;
        z21 = z18;
        z20 = z17;
        z22 = false;
        r8 = z21;
        r9 = z20;
        if (!z22) {
        }
        if (z42) {
        }
        if (!z7) {
        }
        intent7.setAction(r8);
        return z22;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$11(String str) {
        if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
            this.actionBarLayout.getFragmentStack().get(0).presentFragment(new PremiumPreviewFragment(Uri.parse(str).getQueryParameter("ref")));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$12(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$14(final AlertDialog alertDialog, final String str, final Bundle bundle, final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda65
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
            lambda$runLinkRequest$65(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, getActionBarLayout().getLastFragment(), tLRPC$TL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$16(final int[] iArr, LocationController.SharingLocationInfo sharingLocationInfo) {
        iArr[0] = sharingLocationInfo.messageObject.currentAccount;
        switchToAccount(iArr[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        final long dialogId = sharingLocationInfo.messageObject.getDialogId();
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda112
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
                LaunchActivity.lambda$handleIntent$15(iArr, dialogId, tLRPC$MessageMedia, i, z, i2);
            }
        });
        lambda$runLinkRequest$65(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$15(int[] iArr, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$17() {
        if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
            this.actionBarLayout.getFragmentStack().get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$18(BaseFragment baseFragment, boolean z) {
        presentFragment(baseFragment, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$19(boolean z, int[] iArr, TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
        VoIPHelper.startCall(tLRPC$User, z, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(iArr[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$23(final ActionIntroActivity actionIntroActivity, String str) {
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        byte[] decode = Base64.decode(str.substring(17), 8);
        TLRPC$TL_auth_acceptLoginToken tLRPC$TL_auth_acceptLoginToken = new TLRPC$TL_auth_acceptLoginToken();
        tLRPC$TL_auth_acceptLoginToken.token = decode;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$handleIntent$22(AlertDialog.this, actionIntroActivity, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$22(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$21(AlertDialog.this, tLObject, actionIntroActivity, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$21(AlertDialog alertDialog, TLObject tLObject, final ActionIntroActivity actionIntroActivity, final TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (!(tLObject instanceof TLRPC$TL_authorization)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$handleIntent$20(ActionIntroActivity.this, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$20(ActionIntroActivity actionIntroActivity, TLRPC$TL_error tLRPC$TL_error) {
        String string = LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient);
        AlertsCreator.showSimpleAlert(actionIntroActivity, string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$24(String str, String str2, BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        NewContactActivity newContactActivity = new NewContactActivity();
        newContactActivity.setInitialPhoneNumber(str, false);
        if (str2 != null) {
            String[] split = str2.split(" ", 2);
            newContactActivity.setInitialName(split[0], split.length > 1 ? split[1] : null);
        }
        baseFragment.presentFragment(newContactActivity);
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
        DialogsActivity dialogsActivity = new DialogsActivity(bundle) { // from class: org.telegram.ui.LaunchActivity.14
            @Override // org.telegram.ui.DialogsActivity
            public boolean shouldShowNextButton(DialogsActivity dialogsActivity2, ArrayList<Long> arrayList2, CharSequence charSequence, boolean z2) {
                if (LaunchActivity.this.exportingChatUri != null) {
                    return false;
                }
                if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                    return true;
                }
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
        };
        dialogsActivity.setDelegate(this);
        this.actionBarLayout.presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? !(this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)) : !(this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)), !z, true, false);
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
        if (!z) {
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            if (AndroidUtilities.isTablet()) {
                this.actionBarLayout.rebuildFragments(1);
                this.rightActionBarLayout.rebuildFragments(1);
                return;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    private int runCommentRequest(int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3, TLRPC$Chat tLRPC$Chat) {
        return runCommentRequest(i, alertDialog, num, num2, num3, tLRPC$Chat, null);
    }

    private int runCommentRequest(final int i, final AlertDialog alertDialog, final Integer num, final Integer num2, final Integer num3, final TLRPC$Chat tLRPC$Chat, final Runnable runnable) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = (num2 != null ? num : num3).intValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runCommentRequest$28(i, tLRPC$Chat, num3, num, tLRPC$TL_messages_getDiscussionMessage, num2, runnable, alertDialog, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$28(final int i, final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$27(tLObject, i, tLRPC$Chat, num, num2, tLRPC$TL_messages_getDiscussionMessage, num3, runnable, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$27(TLObject tLObject, int i, final TLRPC$Chat tLRPC$Chat, final Integer num, final Integer num2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, AlertDialog alertDialog) {
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
            if (!arrayList.isEmpty()) {
                if (tLRPC$Chat.forum) {
                    TLRPC$TL_channels_getForumTopicsByID tLRPC$TL_channels_getForumTopicsByID = new TLRPC$TL_channels_getForumTopicsByID();
                    tLRPC$TL_channels_getForumTopicsByID.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(tLRPC$Chat.id);
                    tLRPC$TL_channels_getForumTopicsByID.topics.add(num);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runCommentRequest$26(tLRPC$Chat, num, arrayList, num2, tLRPC$TL_messages_getDiscussionMessage, num3, runnable, tLObject2, tLRPC$TL_error);
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
                    lambda$runLinkRequest$65(chatActivity);
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
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
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
    public /* synthetic */ void lambda$runCommentRequest$26(final TLRPC$Chat tLRPC$Chat, final Integer num, final ArrayList arrayList, final Integer num2, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num3, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$25(tLRPC$TL_error, tLObject, tLRPC$Chat, num, arrayList, num2, tLRPC$TL_messages_getDiscussionMessage, num3, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$25(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$Chat tLRPC$Chat, Integer num, ArrayList arrayList, Integer num2, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Integer num3, Runnable runnable) {
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
            bundle.putLong("chat_id", -((MessageObject) arrayList.get(0)).getDialogId());
            if (num2.intValue() != findTopic.id) {
                bundle.putInt("message_id", Math.max(1, num2.intValue()));
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
            if (num3 != null) {
                chatActivity.setHighlightMessageId(num3.intValue());
            } else if (num2.intValue() != findTopic.id) {
                chatActivity.setHighlightMessageId(num2.intValue());
            }
            lambda$runLinkRequest$65(chatActivity);
            if (runnable == null) {
                return;
            }
            runnable.run();
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
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runImportRequest$30(uri, i, alertDialog, tLObject, tLRPC$TL_error);
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    LaunchActivity.lambda$runImportRequest$31(i, iArr, r3, dialogInterface);
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
            if (inputStream == null) {
                return;
            }
            try {
                inputStream.close();
            } catch (Exception e4) {
                FileLog.e(e4);
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
    public /* synthetic */ void lambda$runImportRequest$30(final Uri uri, final int i, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runImportRequest$29(tLObject, uri, i, alertDialog);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runImportRequest$29(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
        boolean z;
        if (!isFinishing()) {
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
                            if (it2.hasNext()) {
                                if (uri2.contains(it2.next())) {
                                    bundle.putInt("dialogsType", 11);
                                    z = true;
                                    break;
                                }
                            } else {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runImportRequest$31(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x04e3  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x03b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final boolean z, final Integer num, final Long l, final Integer num2, final Integer num3, final String str10, final HashMap<String, String> hashMap, final String str11, final String str12, final String str13, final String str14, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final String str15, final String str16, final String str17, final String str18, int i2, final int i3, final String str19, final String str20, final String str21) {
        final int[] iArr;
        char c;
        final int i4;
        final AlertDialog alertDialog;
        Runnable runnable;
        BaseFragment baseFragment;
        final Runnable runnable2;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        EmojiPacksAlert emojiPacksAlert;
        StickersAlert stickersAlert;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername;
        String str22 = str3;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i5) {
                    LaunchActivity.this.lambda$runLinkRequest$32(i, str, str2, str3, str4, str5, str6, str7, str8, str9, z, num, l, num2, num3, str10, hashMap, str11, str12, str13, str14, tLRPC$TL_wallPaper, str15, str16, str17, str18, i3, str19, str20, str21, i5);
                }
            }).show();
            return;
        }
        boolean z2 = true;
        if (str13 != null) {
            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
            int i5 = NotificationCenter.didReceiveSmsCode;
            if (globalInstance.hasObservers(i5)) {
                NotificationCenter.getGlobalInstance().postNotificationName(i5, str13);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", R.string.OtherLoginCode, str13)));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        } else if (str14 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder2);
        } else {
            final AlertDialog alertDialog2 = new AlertDialog(this, 3);
            final int[] iArr2 = {0};
            if (str15 != null) {
                TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = str15;
                tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$35(i, str15, alertDialog2, tLObject, tLRPC$TL_error);
                    }
                });
                iArr = iArr2;
                alertDialog = alertDialog2;
                runnable = null;
                i4 = i;
                c = 0;
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
                    iArr = iArr2;
                    c = 0;
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$50(str10, str17, str18, i, str19, str20, str21, num, num3, num2, iArr2, alertDialog2, str6, str7, str8, str5, i3, str, tLObject, tLRPC$TL_error);
                        }
                    });
                    i4 = i;
                    alertDialog = alertDialog2;
                } else {
                    iArr = iArr2;
                    c = 0;
                    if (str2 == null) {
                        i4 = i;
                        alertDialog = alertDialog2;
                        if (str22 != null) {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                            tLRPC$TL_inputStickerSetShortName.short_name = str22;
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BaseFragment baseFragment2 = arrayList.get(arrayList.size() - 1);
                            if (baseFragment2 instanceof ChatActivity) {
                                ChatActivity chatActivity = (ChatActivity) baseFragment2;
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, null, chatActivity.getChatActivityEnterViewForStickers(), chatActivity.getResourceProvider());
                                stickersAlert.setCalcMandatoryInsets(chatActivity.isKeyboardVisible());
                            } else {
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null);
                            }
                            if (str4 == null) {
                                z2 = false;
                            }
                            stickersAlert.probablyEmojis = z2;
                            baseFragment2.showDialog(stickersAlert);
                            return;
                        } else if (str4 != null) {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName2 = new TLRPC$TL_inputStickerSetShortName();
                            if (str22 == null) {
                                str22 = str4;
                            }
                            tLRPC$TL_inputStickerSetShortName2.short_name = str22;
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
                                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda110
                                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                                    public final void didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z3) {
                                        LaunchActivity.this.lambda$runLinkRequest$56(z, i4, str9, dialogsActivity2, arrayList4, charSequence, z3);
                                    }
                                });
                                presentFragment(dialogsActivity, false, true);
                            } else if (hashMap != null) {
                                int intValue = Utilities.parseInt((CharSequence) hashMap.get("bot_id")).intValue();
                                if (intValue == 0) {
                                    return;
                                }
                                final String str23 = hashMap.get("payload");
                                final String str24 = hashMap.get("nonce");
                                final String str25 = hashMap.get("callback_url");
                                final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
                                tLRPC$TL_account_getAuthorizationForm.bot_id = intValue;
                                tLRPC$TL_account_getAuthorizationForm.scope = hashMap.get("scope");
                                tLRPC$TL_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$60(iArr, i, alertDialog, tLRPC$TL_account_getAuthorizationForm, str23, str24, str25, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str12 != null) {
                                TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                                tLRPC$TL_help_getDeepLinkInfo.path = str12;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$62(alertDialog, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str11 != null) {
                                TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                                tLRPC$TL_langpack_getLanguage.lang_code = str11;
                                tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$64(alertDialog, tLObject, tLRPC$TL_error);
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
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$runLinkRequest$65(themePreviewActivity);
                                            }
                                        });
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                    if (!z2) {
                                        TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                                        TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                                        tLRPC$TL_inputWallPaperSlug.slug = tLRPC$TL_wallPaper.slug;
                                        tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                LaunchActivity.this.lambda$runLinkRequest$67(alertDialog, tLRPC$TL_wallPaper, tLObject, tLRPC$TL_error);
                                            }
                                        });
                                    }
                                }
                                z2 = false;
                                if (!z2) {
                                }
                            } else if (str16 != null) {
                                runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.this.lambda$runLinkRequest$68();
                                    }
                                };
                                TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                                tLRPC$TL_account_getTheme.format = "android";
                                TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                                tLRPC$TL_inputThemeSlug.slug = str16;
                                tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$70(alertDialog, tLObject, tLRPC$TL_error);
                                    }
                                });
                                if (iArr[c] != 0) {
                                    return;
                                }
                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda2
                                    @Override // android.content.DialogInterface.OnCancelListener
                                    public final void onCancel(DialogInterface dialogInterface) {
                                        LaunchActivity.lambda$runLinkRequest$77(i4, iArr, runnable2, dialogInterface);
                                    }
                                });
                                try {
                                    alertDialog.showDelayed(300L);
                                    return;
                                } catch (Exception unused) {
                                    return;
                                }
                            } else if (l != null && num != null) {
                                if (num2 != null) {
                                    TLRPC$Chat chat = MessagesController.getInstance(i).getChat(l);
                                    if (chat != null) {
                                        iArr[0] = runCommentRequest(i, alertDialog, num, num3, num2, chat);
                                    } else {
                                        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                                        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
                                        tLRPC$TL_inputChannel.channel_id = l.longValue();
                                        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                LaunchActivity.this.lambda$runLinkRequest$72(iArr, i, alertDialog, num, num3, num2, tLObject, tLRPC$TL_error);
                                            }
                                        });
                                    }
                                } else {
                                    final Bundle bundle2 = new Bundle();
                                    bundle2.putLong("chat_id", l.longValue());
                                    bundle2.putInt("message_id", num.intValue());
                                    TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(l);
                                    if (chat2 != null && chat2.forum) {
                                        openForumFromLink(-l.longValue(), 0, num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.lambda$runLinkRequest$73(AlertDialog.this);
                                            }
                                        });
                                    } else {
                                        if (!mainFragmentsStack.isEmpty()) {
                                            ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                            baseFragment = arrayList4.get(arrayList4.size() - 1);
                                        } else {
                                            baseFragment = null;
                                        }
                                        if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle2, baseFragment)) {
                                            final BaseFragment baseFragment4 = baseFragment;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    LaunchActivity.this.lambda$runLinkRequest$76(bundle2, l, iArr, alertDialog, num2, num, baseFragment4, i);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    } else if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        i4 = i;
                        alertDialog = alertDialog2;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$53(i4, alertDialog, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                    } else {
                        i4 = i;
                        alertDialog = alertDialog2;
                        if (i2 == 1) {
                            TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                            tLRPC$TL_messages_importChatInvite.hash = str2;
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$55(i4, alertDialog, tLObject, tLRPC$TL_error);
                                }
                            }, 2);
                        }
                    }
                }
                runnable = null;
            }
            runnable2 = runnable;
            if (iArr[c] != 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$32(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, boolean z, Integer num, Long l, Integer num2, Integer num3, String str10, HashMap hashMap, String str11, String str12, String str13, String str14, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str15, String str16, String str17, String str18, int i2, String str19, String str20, String str21, int i3) {
        if (i3 != i) {
            switchToAccount(i3, true);
        }
        runLinkRequest(i3, str, str2, str3, str4, str5, str6, str7, str8, str9, z, num, l, num2, num3, str10, hashMap, str11, str12, str13, str14, tLRPC$TL_wallPaper, str15, str16, str17, str18, 1, i2, str19, str20, str21);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$35(final int i, final String str, final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$34(tLRPC$TL_error, tLObject, i, str, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$34(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, String str, AlertDialog alertDialog) {
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
                final Runnable runnable = this.navigateToPremiumGiftCallback;
                if (runnable != null) {
                    this.navigateToPremiumGiftCallback = null;
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda113
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$33(runnable, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$65(paymentFormActivity);
            }
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$33(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$50(final String str, final String str2, final String str3, final int i, final String str4, final String str5, final String str6, final Integer num, final Integer num2, final Integer num3, final int[] iArr, final AlertDialog alertDialog, final String str7, final String str8, final String str9, final String str10, final int i2, final String str11, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$49(tLObject, tLRPC$TL_error, str, str2, str3, i, str4, str5, str6, num, num2, num3, iArr, alertDialog, str7, str8, str9, str10, i2, str11);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$49(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, final String str, String str2, String str3, final int i, final String str4, String str5, final String str6, Integer num, Integer num2, Integer num3, int[] iArr, final AlertDialog alertDialog, String str7, String str8, final String str9, String str10, int i2, String str11) {
        String str12;
        long j;
        boolean z;
        ChatActivity chatActivity;
        if (!isFinishing()) {
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
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                                    LaunchActivity.this.lambda$runLinkRequest$41(i, str6, user, str4, tLRPC$TL_contacts_resolvedPeer, tLObject2, tLRPC$TL_error2);
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
                    iArr[0] = runCommentRequest(i, alertDialog, num, num2, num3, tLRPC$TL_contacts_resolvedPeer.chats.get(0));
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
                    dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda108
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final void didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z3) {
                            LaunchActivity.this.lambda$runLinkRequest$42(str, i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity2, arrayList3, charSequence, z3);
                        }
                    });
                    this.actionBarLayout.presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? !(this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)) : !(this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)), true, true, false);
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
                    dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda107
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final void didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z3) {
                            LaunchActivity.this.lambda$runLinkRequest$47(i, tLRPC$User2, str9, str14, dialogsActivity2, dialogsActivity3, arrayList4, charSequence, z3);
                        }
                    });
                    lambda$runLinkRequest$65(dialogsActivity2);
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
                                openForumFromLink(j, num4.intValue(), num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.lambda$runLinkRequest$48(AlertDialog.this);
                                    }
                                });
                            } else {
                                Bundle bundle4 = new Bundle();
                                bundle4.putLong("chat_id", j2);
                                lambda$runLinkRequest$65(new TopicsFragment(bundle4));
                                try {
                                    alertDialog.dismiss();
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                            }
                        } else {
                            long j3 = j;
                            MessagesController.getInstance(i).ensureMessagesLoaded(j3, num == null ? 0 : num.intValue(), new 16(alertDialog, str3, chatActivity, j3, num, bundle3));
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
            if (!z2) {
                return;
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$41(final int i, final String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$40(tLObject, i, str, tLRPC$User, str2, tLRPC$TL_contacts_resolvedPeer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$40(TLObject tLObject, final int i, String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer) {
        DialogsActivity dialogsActivity;
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
            if (!arrayList2.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putInt("dialogsType", 14);
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("allowGroups", arrayList2.contains("groups"));
                bundle.putBoolean("allowUsers", arrayList2.contains("users"));
                bundle.putBoolean("allowChannels", arrayList2.contains("channels"));
                bundle.putBoolean("allowBots", arrayList2.contains("bots"));
                DialogsActivity dialogsActivity2 = new DialogsActivity(bundle);
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda109
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final void didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList3, CharSequence charSequence, boolean z) {
                        LaunchActivity.this.lambda$runLinkRequest$36(tLRPC$User, str2, i, dialogsActivity3, arrayList3, charSequence, z);
                    }
                });
                dialogsActivity = dialogsActivity2;
            } else {
                dialogsActivity = null;
            }
            if (tLRPC$TL_attachMenuBot.inactive) {
                AttachBotIntroTopView attachBotIntroTopView = new AttachBotIntroTopView(this);
                attachBotIntroTopView.setColor(Theme.getColor("chat_attachContactIcon"));
                attachBotIntroTopView.setBackgroundColor(Theme.getColor("dialogTopBackground"));
                attachBotIntroTopView.setAttachBot(tLRPC$TL_attachMenuBot);
                final DialogsActivity dialogsActivity3 = dialogsActivity;
                new AlertDialog.Builder(this).setTopView(attachBotIntroTopView).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRequestAttachPermission", R.string.BotRequestAttachPermission, UserObject.getUserName(tLRPC$User)))).setPositiveButton(LocaleController.getString(R.string.BotAddToMenu), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda10
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        LaunchActivity.this.lambda$runLinkRequest$39(i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity3, baseFragment, tLRPC$User, str2, dialogInterface, i2);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
                return;
            } else if (dialogsActivity != null) {
                lambda$runLinkRequest$65(dialogsActivity);
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
        ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
        BulletinFactory.of(arrayList3.get(arrayList3.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantAddToAttachMenu)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$36(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$39(final int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$38(i, dialogsActivity, baseFragment, tLRPC$User, str, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$38(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$37(tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$37(TLObject tLObject, int i, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
            if (dialogsActivity != null) {
                lambda$runLinkRequest$65(dialogsActivity);
            } else if (!(baseFragment instanceof ChatActivity)) {
            } else {
                ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$42(String str, int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$47(final int i, final TLRPC$User tLRPC$User, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        final TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$45(str, str2, i, chat, dialogsActivity, tLRPC$User, j, z2, tLRPC$TL_chatAdminRights2, str3);
                }
            });
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int i2 = R.string.AddBot;
        builder.setTitle(LocaleController.getString("AddBot", i2));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(tLRPC$User), chat == null ? "" : chat.title)));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("AddBot", i2), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                LaunchActivity.this.lambda$runLinkRequest$46(j, i, tLRPC$User, str2, dialogInterface, i3);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$45(final String str, final String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, final TLRPC$User tLRPC$User, final long j, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$44(str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$44(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
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
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$43(i, tLRPC$Chat, dialogsActivity);
                }
            }, null);
            return;
        }
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(tLRPC$User.id, -j, tLRPC$TL_chatAdminRights3, null, null, str3, 2, true, !z, str2);
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate(this) { // from class: org.telegram.ui.LaunchActivity.15
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
    public /* synthetic */ void lambda$runLinkRequest$43(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (!MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            return;
        }
        presentFragment(new ChatActivity(bundle), true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$46(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
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
    public static /* synthetic */ void lambda$runLinkRequest$48(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 16 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ BaseFragment val$lastFragment;
        final /* synthetic */ String val$livestream;
        final /* synthetic */ Integer val$messageId;
        final /* synthetic */ AlertDialog val$progressDialog;

        16(AlertDialog alertDialog, String str, BaseFragment baseFragment, long j, Integer num, Bundle bundle) {
            this.val$progressDialog = alertDialog;
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
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!LaunchActivity.this.isFinishing()) {
                if (this.val$livestream != null) {
                    BaseFragment baseFragment = this.val$lastFragment;
                    if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == this.val$dialog_id) {
                        chatActivity = this.val$lastFragment;
                        final BaseFragment baseFragment2 = chatActivity;
                        final String str = this.val$livestream;
                        final long j = this.val$dialog_id;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$16$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.16.this.lambda$onMessagesLoaded$2(str, j, baseFragment2);
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$16$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.16.this.lambda$onMessagesLoaded$2(str2, j2, baseFragment22);
                    }
                }, 150L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$2(String str, final long j, final BaseFragment baseFragment) {
            if (str != null) {
                final AccountInstance accountInstance = AccountInstance.getInstance(LaunchActivity.this.currentAccount);
                long j2 = -j;
                boolean z = false;
                ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
                if (groupCall != null) {
                    TLRPC$Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(j2));
                    TLRPC$InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(j);
                    if (!groupCall.call.rtmp_stream) {
                        z = true;
                    }
                    VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, baseFragment, accountInstance);
                    return;
                }
                TLRPC$ChatFull chatFull = accountInstance.getMessagesController().getChatFull(j2);
                if (chatFull == null) {
                    return;
                }
                if (chatFull.call == null) {
                    if (baseFragment.getParentActivity() == null) {
                        return;
                    }
                    BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.linkbroken, LocaleController.getString("InviteExpired", R.string.InviteExpired)).show();
                    return;
                }
                accountInstance.getMessagesController().getGroupCall(j2, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$16$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.16.this.lambda$onMessagesLoaded$1(accountInstance, j, baseFragment);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$1(final AccountInstance accountInstance, final long j, final BaseFragment baseFragment) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$16$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.16.this.lambda$onMessagesLoaded$0(accountInstance, j, baseFragment);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessagesLoaded$0(AccountInstance accountInstance, long j, BaseFragment baseFragment) {
            long j2 = -j;
            boolean z = false;
            ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
            TLRPC$Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(j2));
            TLRPC$InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(j);
            if (groupCall == null || !groupCall.call.rtmp_stream) {
                z = true;
            }
            VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, baseFragment, accountInstance);
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
            }
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$53(final int i, final AlertDialog alertDialog, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$52(tLRPC$TL_error, tLObject, i, alertDialog, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x002f, code lost:
        if (r10.chat.has_geo != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0077, code lost:
        if (r14.checkCanOpenChat(r7, r0.get(r0.size() - 1)) != false) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$52(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, final AlertDialog alertDialog, String str) {
        if (!isFinishing()) {
            ChatActivity.ThemeDelegate themeDelegate = null;
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
                            LaunchActivity.lambda$runLinkRequest$51(zArr, dialogInterface);
                        }
                    });
                    if (tLRPC$ChatInvite.chat.forum) {
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                        lambda$runLinkRequest$65(new TopicsFragment(bundle2));
                    } else {
                        MessagesController.getInstance(i).ensureMessagesLoaded(-tLRPC$ChatInvite.chat.id, 0, new MessagesController.MessagesLoadedCallback() { // from class: org.telegram.ui.LaunchActivity.17
                            @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                            public void onMessagesLoaded(boolean z2) {
                                try {
                                    alertDialog.dismiss();
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
                                    alertDialog.dismiss();
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
                if (baseFragment instanceof ChatActivity) {
                    themeDelegate = ((ChatActivity) baseFragment).themeDelegate;
                }
                baseFragment.showDialog(new JoinGroupAlert(this, tLRPC$ChatInvite, str, baseFragment, themeDelegate));
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
            if (!z) {
                return;
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$51(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$55(final int i, final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$54(alertDialog, tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$54(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        if (!isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (tLRPC$TL_error == null) {
                if (this.actionBarLayout == null) {
                    return;
                }
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$56(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2) {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$60(int[] iArr, final int i, final AlertDialog alertDialog, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$58(alertDialog, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3, tLObject2, tLRPC$TL_error2);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$59(alertDialog, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$58(final AlertDialog alertDialog, final int i, final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$57(alertDialog, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$57(AlertDialog alertDialog, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$65(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$account_Password) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$59(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
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
    public /* synthetic */ void lambda$runLinkRequest$62(final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$61(alertDialog, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$61(AlertDialog alertDialog, TLObject tLObject) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_help_deepLinkInfo) {
            TLRPC$TL_help_deepLinkInfo tLRPC$TL_help_deepLinkInfo = (TLRPC$TL_help_deepLinkInfo) tLObject;
            AlertsCreator.showUpdateAppAlert(this, tLRPC$TL_help_deepLinkInfo.message, tLRPC$TL_help_deepLinkInfo.update_app);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$64(final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$63(alertDialog, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$63(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_langPackLanguage) {
            showAlertDialog(AlertsCreator.createLanguageAlert(this, (TLRPC$TL_langPackLanguage) tLObject));
        } else if (tLRPC$TL_error == null) {
        } else {
            if ("LANG_CODE_NOT_SUPPORTED".equals(tLRPC$TL_error.text)) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", R.string.LanguageUnsupportedError)));
                return;
            }
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$67(final AlertDialog alertDialog, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$66(alertDialog, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$66(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
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
            lambda$runLinkRequest$65(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$68() {
        this.loadingThemeFileName = null;
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeProgressDialog = null;
        this.loadingTheme = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$70(final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$69(tLObject, alertDialog, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$69(TLObject tLObject, AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
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
                        alertDialog.dismiss();
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
                alertDialog.dismiss();
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
    public /* synthetic */ void lambda$runLinkRequest$72(final int[] iArr, final int i, final AlertDialog alertDialog, final Integer num, final Integer num2, final Integer num3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$71(tLObject, iArr, i, alertDialog, num, num2, num3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$71(TLObject tLObject, int[] iArr, int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                iArr[0] = runCommentRequest(i, alertDialog, num, num2, num3, tLRPC$TL_messages_chats.chats.get(0));
                if (z) {
                    return;
                }
                try {
                    alertDialog.dismiss();
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
    public static /* synthetic */ void lambda$runLinkRequest$73(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$76(final Bundle bundle, final Long l, int[] iArr, final AlertDialog alertDialog, final Integer num, final Integer num2, final BaseFragment baseFragment, final int i) {
        if (!this.actionBarLayout.presentFragment(new ChatActivity(bundle))) {
            TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
            TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
            tLRPC$TL_inputChannel.channel_id = l.longValue();
            tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runLinkRequest$75(alertDialog, num, l, num2, baseFragment, i, bundle, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$75(final AlertDialog alertDialog, final Integer num, final Long l, final Integer num2, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$74(alertDialog, tLObject, num, l, num2, baseFragment, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$74(AlertDialog alertDialog, TLObject tLObject, Integer num, Long l, Integer num2, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            alertDialog.dismiss();
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
    public static /* synthetic */ void lambda$runLinkRequest$77(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void openForumFromLink(final long j, int i, final Integer num, final Runnable runnable) {
        if (num == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", -j);
            lambda$runLinkRequest$65(new TopicsFragment(bundle));
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
        tLRPC$TL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(-j);
        tLRPC$TL_channels_getMessages.id.add(num);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$openForumFromLink$79(num, j, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$79(final Integer num, final long j, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openForumFromLink$78(tLObject, num, j, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openForumFromLink$78(TLObject tLObject, Integer num, long j, Runnable runnable) {
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
            runCommentRequest(this.currentAccount, null, Integer.valueOf(tLRPC$Message.id), null, Integer.valueOf(MessageObject.getTopicId(tLRPC$Message)), MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)), runnable);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$65(new TopicsFragment(bundle));
        if (runnable == null) {
            return;
        }
        runnable.run();
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0075  */
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
                            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                                translitString = null;
                            }
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
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.18
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
        this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.lambda$createUpdateUI$80(view);
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
    public /* synthetic */ void lambda$createUpdateUI$80(View view) {
        if (!SharedConfig.isAppUpdateAvailable()) {
            return;
        }
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

    /* JADX WARN: Removed duplicated region for block: B:12:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0130 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ec  */
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
                    this.updateLayout.animate().translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(null).setDuration(180L).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda24
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.lambda$updateAppUpdateViews$81(frameLayout);
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
                this.updateLayout.animate().translationY(AndroidUtilities.dp(44.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.19
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
    public static /* synthetic */ void lambda$updateAppUpdateViews$81(View view) {
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void checkAppUpdate(boolean z) {
        if (z || !BuildVars.DEBUG_VERSION) {
            if (!z && !BuildVars.CHECK_UPDATES) {
                return;
            }
            if (!z && Math.abs(System.currentTimeMillis() - SharedConfig.lastUpdateCheckTime) < MessagesController.getInstance(0).updateCheckDelay * 1000) {
                return;
            }
            TLRPC$TL_help_getAppUpdate tLRPC$TL_help_getAppUpdate = new TLRPC$TL_help_getAppUpdate();
            try {
                tLRPC$TL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception unused) {
            }
            if (tLRPC$TL_help_getAppUpdate.source == null) {
                tLRPC$TL_help_getAppUpdate.source = "";
            }
            final int i = this.currentAccount;
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$checkAppUpdate$83(i, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$83(final int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            final TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$82(tLRPC$TL_help_appUpdate, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$82(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i) {
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
            this.visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$showAlertDialog$84(dialogInterface);
                }
            });
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlertDialog$84(DialogInterface dialogInterface) {
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
        } else if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            baseFragment = arrayList3.get(arrayList3.size() - 1);
        } else {
            baseFragment = null;
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

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public void didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<MessagesStorage.TopicKey> arrayList, final CharSequence charSequence, final boolean z) {
        ChatActivity chatActivity;
        long j;
        ChatActivity chatActivity2;
        int i;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList2;
        final int currentAccount = dialogsActivity != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        final Uri uri = this.exportingChatUri;
        if (uri != null) {
            final ArrayList arrayList3 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            final AlertDialog alertDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).dialogId, this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j2) {
                    LaunchActivity.this.lambda$didSelectDialogs$85(currentAccount, dialogsActivity, z, arrayList3, uri, alertDialog, j2);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            final boolean z6 = dialogsActivity == null || dialogsActivity.notify;
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
                    return;
                }
                chatActivity = new ChatActivity(bundle);
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
                    return;
                }
            }
            ArrayList<TLRPC$User> arrayList8 = this.contactsToSend;
            if (arrayList8 != null && arrayList8.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(arrayList9.get(arrayList9.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                final ChatActivity chatActivity3 = chatActivity;
                phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z7, int i3) {
                        LaunchActivity.this.lambda$didSelectDialogs$86(chatActivity3, arrayList, currentAccount, charSequence, z6, tLRPC$User, z7, i3);
                    }
                });
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                arrayList10.get(arrayList10.size() - 1).showDialog(phonebookShareAlert);
            } else {
                String str = null;
                int i3 = 0;
                while (i3 < arrayList.size()) {
                    long j3 = arrayList.get(i3).dialogId;
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (chatActivity != null) {
                        boolean z7 = dialogsActivity == null || this.videoPath != null || ((arrayList2 = this.photoPathsArray) != null && arrayList2.size() > 0);
                        INavigationLayout iNavigationLayout = this.actionBarLayout;
                        boolean z8 = dialogsActivity != null;
                        i = ConnectionsManager.RequestFlagDoNotWaitFloodWait;
                        j = j3;
                        chatActivity2 = chatActivity;
                        iNavigationLayout.presentFragment(chatActivity, z8, z7, true, false);
                        String str2 = this.videoPath;
                        if (str2 != null) {
                            chatActivity2.openVideoEditor(str2, this.sendingText);
                            this.sendingText = null;
                            z4 = true;
                        } else {
                            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList11 = this.photoPathsArray;
                            if (arrayList11 == null || arrayList11.size() <= 0) {
                                z4 = false;
                            } else {
                                boolean openPhotosEditor = chatActivity2.openPhotosEditor(this.photoPathsArray, (charSequence == null || charSequence.length() == 0) ? this.sendingText : charSequence);
                                if (openPhotosEditor) {
                                    this.sendingText = null;
                                }
                                z5 = openPhotosEditor;
                                z4 = false;
                                z2 = z4;
                                z3 = z5;
                            }
                        }
                        z5 = false;
                        z2 = z4;
                        z3 = z5;
                    } else {
                        j = j3;
                        chatActivity2 = chatActivity;
                        i = ConnectionsManager.RequestFlagDoNotWaitFloodWait;
                        if (this.videoPath != null) {
                            String str3 = this.sendingText;
                            if (str3 != null && str3.length() <= 1024) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                            ArrayList arrayList12 = new ArrayList();
                            arrayList12.add(this.videoPath);
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList12, arrayList12, null, str, null, j, null, null, null, null, z6, 0);
                        }
                        z2 = false;
                        z3 = false;
                    }
                    if (this.photoPathsArray != null && !z3) {
                        String str4 = this.sendingText;
                        if (str4 != null && str4.length() <= i && this.photoPathsArray.size() == 1) {
                            this.photoPathsArray.get(0).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, null, null, null, false, false, null, z6, 0, false);
                    }
                    if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
                        String str5 = this.sendingText;
                        if (str5 != null && str5.length() <= i) {
                            ArrayList<String> arrayList13 = this.documentsPathsArray;
                            int size2 = arrayList13 != null ? arrayList13.size() : 0;
                            ArrayList<Uri> arrayList14 = this.documentsUrisArray;
                            if (size2 + (arrayList14 != null ? arrayList14.size() : 0) == 1) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                        }
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j, null, null, null, null, z6, 0);
                    }
                    String str6 = this.sendingText;
                    if (str6 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str6, j, true, 0);
                    }
                    ArrayList<TLRPC$User> arrayList15 = this.contactsToSend;
                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                        for (int i4 = 0; i4 < this.contactsToSend.size(); i4++) {
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(this.contactsToSend.get(i4), j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z6, 0);
                        }
                    }
                    if (!TextUtils.isEmpty(charSequence) && !z2 && !z3) {
                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, z6, 0);
                    }
                    i3++;
                    chatActivity = chatActivity2;
                }
            }
            ChatActivity chatActivity4 = chatActivity;
            if (dialogsActivity != null && chatActivity4 == null) {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectDialogs$85(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
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
    public /* synthetic */ void lambda$didSelectDialogs$86(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2) {
        if (chatActivity != null) {
            this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long j = ((MessagesStorage.TopicKey) arrayList.get(i3)).dialogId;
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$User, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2);
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
    public void lambda$runLinkRequest$65(BaseFragment baseFragment) {
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
        boolean z = false;
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onActivityResult");
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (i == 105) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            boolean canDrawOverlays = Settings.canDrawOverlays(this);
            ApplicationLoader.canDrawOverlays = canDrawOverlays;
            if (!canDrawOverlays) {
                return;
            }
            GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
            if (groupCallActivity != null) {
                groupCallActivity.dismissInternal();
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$onActivityResult$87();
                }
            }, 200L);
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
            LocationController locationController = LocationController.getInstance(this.currentAccount);
            if (i2 == -1) {
                z = true;
            }
            locationController.startFusedLocationRequest(z);
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
    public /* synthetic */ void lambda$onActivityResult$87() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (!checkPermissionsResult(i, strArr, iArr)) {
            return;
        }
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
        if (this.requestedPermissions.get(i, -1) < 0) {
            return;
        }
        int i2 = this.requestedPermissions.get(i, -1);
        this.requestedPermissions.delete(i);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.permissionsGranted, Integer.valueOf(i2));
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
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$88(i);
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
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onPause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onPause$88(int i) {
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
        MediaController.getInstance().setFeedbackView(this.actionBarLayout.getView(), false);
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
        this.actionBarLayout.onUserLeaveHint();
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
        MediaController.getInstance().setFeedbackView(this.actionBarLayout.getView(), true);
        ApplicationLoader.mainInterfacePaused = false;
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda70.INSTANCE);
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
    public static /* synthetic */ void lambda$onResume$89() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    private void invalidateTabletMode() {
        Boolean wasTablet = AndroidUtilities.getWasTablet();
        if (wasTablet == null) {
            return;
        }
        AndroidUtilities.resetWasTabletFlag();
        if (wasTablet.booleanValue() == AndroidUtilities.isTablet()) {
            return;
        }
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
                        }
                    }
                }
                layerFragmentsStack.add(baseFragment);
            }
            j = j2;
        }
        setupActionBarLayout();
        this.actionBarLayout.rebuildFragments(1);
        if (!AndroidUtilities.isTablet()) {
            return;
        }
        this.rightActionBarLayout.rebuildFragments(1);
        this.layersActionBarLayout.rebuildFragments(1);
        Iterator<BaseFragment> it = mainFragmentsStack.iterator();
        while (it.hasNext()) {
            BaseFragment next = it.next();
            if (next instanceof DialogsActivity) {
                DialogsActivity dialogsActivity2 = (DialogsActivity) next;
                if (dialogsActivity2.isMainDialogList()) {
                    dialogsActivity2.setOpenedDialogId(j);
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

    /* JADX WARN: Code restructure failed: missing block: B:131:0x02e7, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L132;
     */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0631  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0648  */
    /* JADX WARN: Removed duplicated region for block: B:240:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0638  */
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
        boolean z4 = false;
        r4 = false;
        r4 = false;
        boolean z5 = false;
        r4 = false;
        boolean z6 = false;
        if (i == NotificationCenter.closeOtherAppActivities) {
            if (objArr[0] == this) {
                return;
            }
            onFinish();
            finish();
        } else if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = ConnectionsManager.getInstance(i2).getConnectionState();
            if (this.currentConnectionState == connectionState) {
                return;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to state " + connectionState);
            }
            this.currentConnectionState = connectionState;
            updateCurrentConnectionState(i2);
        } else if (i == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.needShowAlert) {
            Integer num = (Integer) objArr[0];
            if (num.intValue() == 6) {
                return;
            }
            if (num.intValue() == 3 && this.proxyErrorDialog != null) {
                return;
            }
            if (num.intValue() == 4) {
                showTosActivity(i2, (TLRPC$TL_help_termsOfService) objArr[1]);
                return;
            }
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                baseFragment3 = arrayList.get(arrayList.size() - 1);
            } else {
                baseFragment3 = null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (baseFragment3 != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("info1.**", Integer.valueOf(baseFragment3.getThemedColor("dialogTopBackground")));
                hashMap.put("info2.**", Integer.valueOf(baseFragment3.getThemedColor("dialogTopBackground")));
                builder.setTopAnimation(R.raw.not_available, 52, false, baseFragment3.getThemedColor("dialogTopBackground"), hashMap);
                builder.setTopAnimationIsNew(true);
            }
            if (num.intValue() != 2 && num.intValue() != 3) {
                builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        LaunchActivity.lambda$didReceivedNotification$90(i2, dialogInterface, i5);
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
                builder.setMessage((String) objArr[1]);
                String str3 = (String) objArr[2];
                if (str3.startsWith("AUTH_KEY_DROP_")) {
                    builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            LaunchActivity.this.lambda$didReceivedNotification$91(dialogInterface, i5);
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
        } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
            final HashMap hashMap2 = (HashMap) objArr[0];
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    LaunchActivity.this.lambda$didReceivedNotification$93(hashMap2, i2, dialogInterface, i5);
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
            if (sizeNotifierFrameLayout == null) {
                return;
            }
            sizeNotifierFrameLayout.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
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
            builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    LaunchActivity.lambda$didReceivedNotification$94(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i5);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    LaunchActivity.lambda$didReceivedNotification$95(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i5);
                }
            });
            builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    LaunchActivity.lambda$didReceivedNotification$96(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i5);
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
            boolean z7 = objArr.length > 2 && ((Boolean) objArr[2]).booleanValue();
            if (booleanValue3 && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                z5 = true;
            }
            checkSystemBarColors(z7, true, z5);
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
                    int i5 = iArr[0];
                    int i6 = iArr[1];
                    float f = booleanValue4 ? 0.0f : max;
                    if (!booleanValue4) {
                        max = 0.0f;
                    }
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view3, i5, i6, f, max);
                    createCircularReveal.setDuration(400L);
                    createCircularReveal.setInterpolator(Easings.easeInOutQuad);
                    createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.20
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
                                LaunchActivity.this.lambda$didReceivedNotification$97(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(createCircularReveal.getDuration());
                        ofFloat.start();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$didReceivedNotification$98();
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
            if (recyclerListView3 == null) {
                return;
            }
            Integer num2 = (Integer) objArr[0];
            int childCount = recyclerListView3.getChildCount();
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt2 = this.sideMenu.getChildAt(i7);
                if ((childAt2 instanceof DrawerUserCell) && ((DrawerUserCell) childAt2).getAccountNumber() == num2.intValue()) {
                    childAt2.invalidate();
                    return;
                }
            }
        } else if (i == NotificationCenter.needShowPlayServicesAlert) {
            try {
                ((Status) objArr[0]).startResolutionForResult(this, 140);
            } catch (Throwable unused2) {
            }
        } else if (i == NotificationCenter.fileLoaded) {
            String str4 = (String) objArr[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str4)) {
                updateAppUpdateViews(true);
            }
            String str5 = this.loadingThemeFileName;
            if (str5 != null) {
                if (!str5.equals(str4)) {
                    return;
                }
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
                        ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$didReceivedNotification$100(fillThemeValues, tLObject, tLRPC$TL_error);
                            }
                        });
                        return;
                    }
                    TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                    Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                    if (applyThemeFile != null) {
                        lambda$runLinkRequest$65(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
                    }
                }
                onThemeLoadFinish();
                return;
            }
            String str6 = this.loadingThemeWallpaperName;
            if (str6 == null || !str6.equals(str4)) {
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
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$didReceivedNotification$102(themeInfo3, file2);
                }
            });
        } else if (i == NotificationCenter.fileLoadFailed) {
            String str7 = (String) objArr[0];
            if (str7.equals(this.loadingThemeFileName) || str7.equals(this.loadingThemeWallpaperName)) {
                onThemeLoadFinish();
            }
            if (!SharedConfig.isAppUpdateAvailable() || !FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str7)) {
                return;
            }
            updateAppUpdateViews(true);
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
                z6 = true;
            }
            checkSystemBarColors(z6);
        } else if (i == NotificationCenter.historyImportProgressChanged) {
            if (objArr.length <= 1 || mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
            AlertsCreator.processError(this.currentAccount, (TLRPC$TL_error) objArr[2], arrayList7.get(arrayList7.size() - 1), (TLObject) objArr[1], new Object[0]);
        } else if (i == NotificationCenter.stickersImportComplete) {
            MediaDataController mediaDataController = MediaDataController.getInstance(i2);
            TLObject tLObject = (TLObject) objArr[0];
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                baseFragment2 = arrayList8.get(arrayList8.size() - 1);
            } else {
                baseFragment2 = null;
            }
            mediaDataController.toggleStickerSet(this, tLObject, 2, baseFragment2, false, true);
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            this.sideMenu.invalidateViews();
        } else if (i == NotificationCenter.showBulletin) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            int intValue3 = ((Integer) objArr[0]).intValue();
            FrameLayout container = (!GroupCallActivity.groupCallUiVisible || (groupCallActivity = GroupCallActivity.groupCallInstance) == null) ? null : groupCallActivity.getContainer();
            if (container == null) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                baseFragment = arrayList9.get(arrayList9.size() - 1);
            } else {
                baseFragment = null;
            }
            int i8 = 1500;
            if (intValue3 == 0) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) objArr[1];
                int intValue4 = ((Integer) objArr[2]).intValue();
                StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, tLRPC$Document, null);
                if (intValue4 == 6 || intValue4 == 7) {
                    i8 = 3500;
                }
                if (baseFragment != null) {
                    Bulletin.make(baseFragment, stickerSetBulletinLayout, i8).show();
                } else {
                    Bulletin.make(container, stickerSetBulletinLayout, i8).show();
                }
            } else if (intValue3 == 1) {
                if (baseFragment != null) {
                    BulletinFactory.of(baseFragment).createErrorBulletin((String) objArr[1]).show();
                } else {
                    BulletinFactory.of(container, null).createErrorBulletin((String) objArr[1]).show();
                }
            } else if (intValue3 == 2) {
                if (((Long) objArr[1]).longValue() > 0) {
                    i3 = R.string.YourBioChanged;
                    str = "YourBioChanged";
                } else {
                    i3 = R.string.ChannelDescriptionChanged;
                    str = "CannelDescriptionChanged";
                }
                (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str, i3)).show();
            } else if (intValue3 == 3) {
                if (((Long) objArr[1]).longValue() > 0) {
                    i4 = R.string.YourNameChanged;
                    str2 = "YourNameChanged";
                } else {
                    i4 = R.string.ChannelTitleChanged;
                    str2 = "CannelTitleChanged";
                }
                (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str2, i4)).show();
            } else if (intValue3 == 4) {
                if (baseFragment != null) {
                    BulletinFactory.of(baseFragment).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], baseFragment.getResourceProvider()).show();
                } else {
                    BulletinFactory.of(container, null).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], null).show();
                }
            } else if (intValue3 == 5) {
                AppIconBulletinLayout appIconBulletinLayout = new AppIconBulletinLayout(this, (LauncherIconController.LauncherIcon) objArr[1], null);
                if (baseFragment != null) {
                    Bulletin.make(baseFragment, appIconBulletinLayout, 1500).show();
                } else {
                    Bulletin.make(container, appIconBulletinLayout, 1500).show();
                }
            }
        } else {
            String[] strArr = null;
            if (i == NotificationCenter.groupCallUpdated) {
                checkWasMutedByAdmin(false);
            } else if (i == NotificationCenter.fileLoadProgressChanged) {
                if (this.updateTextView == null || !SharedConfig.isAppUpdateAvailable()) {
                    return;
                }
                String str8 = (String) objArr[0];
                String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
                if (attachFileName == null || !attachFileName.equals(str8)) {
                    return;
                }
                float longValue = ((float) ((Long) objArr[1]).longValue()) / ((float) ((Long) objArr[2]).longValue());
                this.updateLayoutIcon.setProgress(longValue, true);
                this.updateTextView.setText(LocaleController.formatString("AppUpdateDownloading", R.string.AppUpdateDownloading, Integer.valueOf((int) (longValue * 100.0f))));
            } else if (i == NotificationCenter.appUpdateAvailable) {
                if (mainFragmentsStack.size() == 1) {
                    z3 = true;
                }
                updateAppUpdateViews(z3);
            } else if (i == NotificationCenter.currentUserShowLimitReachedDialog) {
                if (mainFragmentsStack.isEmpty()) {
                    return;
                }
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                BaseFragment baseFragment4 = arrayList10.get(arrayList10.size() - 1);
                if (baseFragment4.getParentActivity() == null) {
                    return;
                }
                baseFragment4.showDialog(new LimitReachedBottomSheet(baseFragment4, baseFragment4.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount));
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
                if (strArr == null) {
                    return;
                }
                int i9 = this.requsetPermissionsPointer + 1;
                this.requsetPermissionsPointer = i9;
                this.requestedPermissions.put(i9, intValue5);
                ActivityCompat.requestPermissions(this, strArr, this.requsetPermissionsPointer);
            } else if (i == NotificationCenter.chatSwithcedToForum) {
                ForumUtilities.switchAllFragmentsInStackToForum(((Long) objArr[0]).longValue(), this.actionBarLayout);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$90(int i, DialogInterface dialogInterface, int i2) {
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController messagesController = MessagesController.getInstance(i);
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$91(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$93(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (!AndroidUtilities.isMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            return;
        }
        LocationActivity locationActivity = new LocationActivity(0);
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda111
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i3, boolean z, int i4) {
                LaunchActivity.lambda$didReceivedNotification$92(hashMap, i, tLRPC$MessageMedia, i3, z, i4);
            }
        });
        lambda$runLinkRequest$65(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$92(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$94(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$95(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$96(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$97(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$98() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$100(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$99(tLObject, themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$99(TLObject tLObject, Theme.ThemeInfo themeInfo) {
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
    public /* synthetic */ void lambda$didReceivedNotification$102(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$101();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$101() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$65(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
            boolean z4 = tLRPC$TL_groupCallParticipant != null && !tLRPC$TL_groupCallParticipant.can_self_unmute && tLRPC$TL_groupCallParticipant.muted;
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
        if (sharedInstance == null || mainFragmentsStack.isEmpty() || sharedInstance.groupCall == null || mainFragmentsStack.isEmpty()) {
            return;
        }
        TLRPC$Chat chat = sharedInstance.getChat();
        BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-chat.id)) {
                chat = null;
            }
            chatActivity.getUndoView().showWithAction(0L, i, chat);
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
        lambda$runLinkRequest$65(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
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
        SharedConfig.checkKeepMedia();
        SharedConfig.checkLogsToDelete();
        if ((Build.VERSION.SDK_INT < 26 || i != 0) && !this.checkFreeDiscSpaceShown) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$105(i);
                }
            }, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$105(int i) {
        File directory;
        long availableBlocksLong;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if ((i != 2 && ((i != 1 || Math.abs(this.alreadyShownFreeDiscSpaceAlertForced - System.currentTimeMillis()) <= 240000) && Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) < 259200000)) || (directory = FileLoader.getDirectory(4)) == null) {
                return;
            }
            StatFs statFs = new StatFs(directory.getAbsolutePath());
            if (Build.VERSION.SDK_INT < 18) {
                availableBlocksLong = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
            } else {
                availableBlocksLong = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
            }
            if (i <= 0 && availableBlocksLong >= 52428800) {
                return;
            }
            if (i > 0) {
                this.alreadyShownFreeDiscSpaceAlertForced = System.currentTimeMillis();
            }
            globalMainSettings.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$104();
                }
            });
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$104() {
        if (this.checkFreeDiscSpaceShown) {
            return;
        }
        try {
            Dialog createFreeSpaceDialog = AlertsCreator.createFreeSpaceDialog(this);
            createFreeSpaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$103(dialogInterface);
                }
            });
            this.checkFreeDiscSpaceShown = true;
            createFreeSpaceDialog.show();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$103(DialogInterface dialogInterface) {
        this.checkFreeDiscSpaceShown = false;
    }

    public static void checkFreeDiscSpaceStatic(int i) {
        LaunchActivity launchActivity = staticInstanceForAlerts;
        if (launchActivity != null) {
            launchActivity.checkFreeDiscSpace(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006c A[Catch: Exception -> 0x011e, TRY_ENTER, TryCatch #0 {Exception -> 0x011e, blocks: (B:3:0x0007, B:5:0x0010, B:9:0x001e, B:12:0x0056, B:15:0x005e, B:18:0x0065, B:22:0x006c, B:25:0x0080, B:29:0x00a0, B:34:0x00be), top: B:2:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0054  */
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
                    languageCellArr[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda19
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LaunchActivity.lambda$showLanguageAlertInternal$106(localeInfoArr, languageCellArr, view);
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
                        LaunchActivity.this.lambda$showLanguageAlertInternal$107(view);
                    }
                });
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$108(localeInfoArr, dialogInterface, i4);
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
                    LaunchActivity.this.lambda$showLanguageAlertInternal$107(view);
                }
            });
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$108(localeInfoArr3, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showLanguageAlertInternal$106(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$107(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$65(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$108(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
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
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            if (!this.loadingLocaleDialog && !ApplicationLoader.mainInterfacePaused) {
                String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                final String str2 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                if (!z && string.equals(str2)) {
                    if (!BuildVars.LOGS_ENABLED) {
                        return;
                    }
                    FileLog.d("alert already showed for " + string);
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
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$showLanguageAlert$110(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                        }
                    }, 8);
                    TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                    tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                    tLRPC$TL_langpack_getStrings2.keys.add("English");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$showLanguageAlert$112(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                        }
                    }, 8);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$110(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$109(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$109(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$112(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$111(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$111(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
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
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity.21
                @Override // java.lang.Runnable
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("lock app");
                            }
                            LaunchActivity.this.showPasscodeActivity(true, false, -1, -1, null, null);
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
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("reset lastPauseTime onPasscodeResume");
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
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$updateCurrentConnectionState$113();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentConnectionState$113() {
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
        lambda$runLinkRequest$65(new ProxyListActivity());
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
            if (baseFragment == null) {
                return;
            }
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
                if (!baseFragment.onBackPressed()) {
                    return;
                }
                baseFragment.finishFragment();
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
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
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
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onActionModeStarted(actionMode);
            this.layersActionBarLayout.onActionModeStarted(actionMode);
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
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onActionModeFinished(actionMode);
            this.layersActionBarLayout.onActionModeFinished(actionMode);
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
        } else if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
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
                    if (audioManager.getStreamVolume(0) != audioManager.getStreamMinVolume(0) || keyEvent.getKeyCode() != 25) {
                        z = false;
                    }
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
        BaseFragment baseFragment = navigationParams.fragment;
        boolean z = navigationParams.removeLast;
        boolean z2 = navigationParams.noAnimation;
        boolean z3 = true;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z4 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer(!z4 && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getView().getVisibility() != 0, true);
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
                boolean z5 = this.tabletFullSize;
                if ((!z5 && iNavigationLayout == this.rightActionBarLayout) || (z5 && iNavigationLayout == this.actionBarLayout)) {
                    boolean z6 = (z5 && iNavigationLayout == (iNavigationLayout4 = this.actionBarLayout) && iNavigationLayout4.getFragmentStack().size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            INavigationLayout iNavigationLayout6 = this.layersActionBarLayout;
                            iNavigationLayout6.removeFragmentFromStack(iNavigationLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z6) {
                        this.actionBarLayout.presentFragment(navigationParams.setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    }
                    return z6;
                } else if (!z5 && iNavigationLayout != (iNavigationLayout3 = this.rightActionBarLayout)) {
                    iNavigationLayout3.getView().setVisibility(0);
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
                } else if (z5 && iNavigationLayout != (iNavigationLayout2 = this.actionBarLayout)) {
                    iNavigationLayout2.presentFragment(navigationParams.setRemoveLast(iNavigationLayout2.getFragmentStack().size() > 1).setNoAnimation(z2).setCheckPresentFromDelegate(false));
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
                    INavigationLayout iNavigationLayout10 = this.actionBarLayout;
                    if (iNavigationLayout10.getFragmentStack().size() <= 1) {
                        z3 = false;
                    }
                    iNavigationLayout10.presentFragment(navigationParams.setRemoveLast(z3).setNoAnimation(z2).setCheckPresentFromDelegate(false));
                    return false;
                }
            } else {
                INavigationLayout iNavigationLayout11 = this.layersActionBarLayout;
                if (iNavigationLayout != iNavigationLayout11) {
                    iNavigationLayout11.getView().setVisibility(0);
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
                    if (z4 && i == -1) {
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
            this.drawerLayoutContainer.setAllowOpenDrawer(!z && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getView().getVisibility() != 0, true);
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
