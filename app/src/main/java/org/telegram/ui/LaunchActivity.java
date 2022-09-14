package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
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
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
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
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.FireworksOverlay;
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
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
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
public class LaunchActivity extends BasePermissionsActivity implements ActionBarLayout.ActionBarLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    public static boolean isResumed;
    public static Runnable onResumeStaticCallback;
    private ActionBarLayout actionBarLayout;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
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
    private ActionBarLayout layersActionBarLayout;
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
    private ActionBarLayout rightActionBarLayout;
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
        this.actionBarLayout = new ActionBarLayout(this) { // from class: org.telegram.ui.LaunchActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBarLayout
            public void setThemeAnimationValue(float f) {
                super.setThemeAnimationValue(f);
                if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                    ArticleViewer.getInstance().updateThemeColors(f);
                }
                LaunchActivity.this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
                if (PhotoViewer.hasInstance()) {
                    PhotoViewer.getInstance().updateColors();
                }
            }
        };
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
        FireworksOverlay fireworksOverlay = new FireworksOverlay(this);
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
        this.drawerLayoutAdapter.setOnPremiumDrawableClick(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
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
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
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
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i3) {
                boolean lambda$onCreate$3;
                lambda$onCreate$3 = LaunchActivity.this.lambda$onCreate$3(itemTouchHelper, view2, i3);
                return lambda$onCreate$3;
            }
        });
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
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
        if (this.actionBarLayout.fragmentsStack.isEmpty()) {
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
            BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(0);
            if (baseFragment instanceof DialogsActivity) {
                ((DialogsActivity) baseFragment).setSideMenu(this.sideMenu);
            }
            if (AndroidUtilities.isTablet()) {
                z = this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty();
                if (this.layersActionBarLayout.fragmentsStack.size() == 1 && ((this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.layersActionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
                    z = false;
                }
            } else {
                z = true;
            }
            if (this.actionBarLayout.fragmentsStack.size() == 1 && ((this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.actionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
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
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda21
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
        AndroidUtilities.startAppCenter(this);
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
    public class 3 extends DrawerLayoutContainer {
        private boolean wasPortrait;

        3(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
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
                    lambda$runLinkRequest$62(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.fragmentsStack.size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
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
                lambda$runLinkRequest$62(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$62(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$62(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$62(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                lambda$runLinkRequest$62(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$62(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$62(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$62(new ChatActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$62(new ActionIntroActivity(1));
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
                    lambda$runLinkRequest$62(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$62(new ActionIntroActivity(4));
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
                protected void onTransitionAnimationEnd(boolean z, boolean z2) {
                    super.onTransitionAnimationEnd(z, z2);
                    if (z || !z2) {
                        return;
                    }
                    LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public void onPreviewOpenAnimationEnd() {
                    super.onPreviewOpenAnimationEnd();
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                    LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                    LaunchActivity.this.switchToAccount(accountNumber, true);
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

    private void setupActionBarLayout() {
        DrawerLayoutContainer drawerLayoutContainer;
        View view;
        if (this.drawerLayoutContainer.indexOfChild(this.launchLayout) != -1) {
            drawerLayoutContainer = this.drawerLayoutContainer;
            view = this.launchLayout;
        } else {
            drawerLayoutContainer = this.drawerLayoutContainer;
            view = this.actionBarLayout;
        }
        int indexOfChild = drawerLayoutContainer.indexOfChild(view);
        if (indexOfChild != -1) {
            this.drawerLayoutContainer.removeViewAt(indexOfChild);
        }
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout relativeLayout = new RelativeLayout(this) { // from class: org.telegram.ui.LaunchActivity.8
                private boolean inLayout;

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
                        LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    } else {
                        LaunchActivity.this.tabletFullSize = false;
                        int i3 = (size / 100) * 35;
                        if (i3 < AndroidUtilities.dp(320.0f)) {
                            i3 = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        LaunchActivity.this.shadowTabletSide.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        LaunchActivity.this.rightActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(size - i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    }
                    LaunchActivity.this.backgroundTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    LaunchActivity.this.shadowTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    LaunchActivity.this.layersActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), size), 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), size2), 1073741824));
                    this.inLayout = false;
                }

                @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                    int i5 = i3 - i;
                    int i6 = i4 - i2;
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                    } else {
                        int i7 = (i5 / 100) * 35;
                        if (i7 < AndroidUtilities.dp(320.0f)) {
                            i7 = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.shadowTabletSide.layout(i7, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + i7, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                        LaunchActivity.this.rightActionBarLayout.layout(i7, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + i7, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
                    }
                    int measuredWidth = (i5 - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
                    int measuredHeight = (i6 - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
                    LaunchActivity.this.layersActionBarLayout.layout(measuredWidth, measuredHeight, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + measuredWidth, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + measuredHeight);
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
            int i = 0;
            sizeNotifierFrameLayout.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            this.launchLayout.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            ViewGroup viewGroup = (ViewGroup) this.actionBarLayout.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.actionBarLayout);
            }
            this.launchLayout.addView(this.actionBarLayout);
            ActionBarLayout actionBarLayout = new ActionBarLayout(this);
            this.rightActionBarLayout = actionBarLayout;
            actionBarLayout.init(rightFragmentsStack);
            this.rightActionBarLayout.setDelegate(this);
            this.launchLayout.addView(this.rightActionBarLayout);
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
                    boolean lambda$setupActionBarLayout$6;
                    lambda$setupActionBarLayout$6 = LaunchActivity.this.lambda$setupActionBarLayout$6(view2, motionEvent);
                    return lambda$setupActionBarLayout$6;
                }
            });
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda19.INSTANCE);
            ActionBarLayout actionBarLayout2 = new ActionBarLayout(this);
            this.layersActionBarLayout = actionBarLayout2;
            actionBarLayout2.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(this.shadowTablet);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(R.drawable.boxshadow);
            this.layersActionBarLayout.init(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
            ActionBarLayout actionBarLayout3 = this.layersActionBarLayout;
            if (layerFragmentsStack.isEmpty()) {
                i = 8;
            }
            actionBarLayout3.setVisibility(i);
            VerticalPositionAutoAnimator.attach(this.layersActionBarLayout);
            this.launchLayout.addView(this.layersActionBarLayout);
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) this.actionBarLayout.getParent();
        if (viewGroup2 != null) {
            viewGroup2.removeView(this.actionBarLayout);
        }
        this.actionBarLayout.init(mainFragmentsStack);
        if (indexOfChild != -1) {
            this.drawerLayoutContainer.addView(this.actionBarLayout, indexOfChild, new ViewGroup.LayoutParams(-1, -1));
        } else {
            this.drawerLayoutContainer.addView(this.actionBarLayout, new ViewGroup.LayoutParams(-1, -1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupActionBarLayout$6(View view, MotionEvent motionEvent) {
        if (!this.actionBarLayout.fragmentsStack.isEmpty() && motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] iArr = new int[2];
            this.layersActionBarLayout.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            if (!this.layersActionBarLayout.checkTransitionAnimation() && (x <= i || x >= i + this.layersActionBarLayout.getWidth() || y <= i2 || y >= i2 + this.layersActionBarLayout.getHeight())) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
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
        if (this.selectAnimatedEmojiDialog == null && (lastFragment = this.actionBarLayout.getLastFragment()) != null) {
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
        lambda$runLinkRequest$62(new ProfileActivity(bundle));
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ DialogsActivity lambda$switchToAccount$8(Void r1) {
        return new DialogsActivity(null);
    }

    public void switchToAccount(int i, boolean z) {
        switchToAccount(i, z, LaunchActivity$$ExternalSyntheticLambda67.INSTANCE);
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
                if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.backgroundTablet.setVisibility(0);
                }
                this.rightActionBarLayout.setVisibility(8);
            }
            this.layersActionBarLayout.setVisibility(8);
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
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
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
        lambda$runLinkRequest$62(new IntroActivity().setOnLogout());
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
    }

    private void checkLayout() {
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        int i = 0;
        if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2)) {
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                while (1 < this.actionBarLayout.fragmentsStack.size()) {
                    BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(1);
                    if (baseFragment instanceof ChatActivity) {
                        ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                    }
                    baseFragment.onPause();
                    this.actionBarLayout.fragmentsStack.remove(1);
                    this.rightActionBarLayout.fragmentsStack.add(baseFragment);
                }
                PasscodeView passcodeView = this.passcodeView;
                if (passcodeView == null || passcodeView.getVisibility() != 0) {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                }
            }
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            actionBarLayout.setVisibility(actionBarLayout.fragmentsStack.isEmpty() ? 8 : 0);
            this.backgroundTablet.setVisibility(this.rightActionBarLayout.fragmentsStack.isEmpty() ? 0 : 8);
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
            return;
        }
        this.tabletFullSize = true;
        if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
            while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                BaseFragment baseFragment2 = this.rightActionBarLayout.fragmentsStack.get(0);
                if (baseFragment2 instanceof ChatActivity) {
                    ((ChatActivity) baseFragment2).setIgnoreAttachOnPause(true);
                }
                baseFragment2.onPause();
                this.rightActionBarLayout.fragmentsStack.remove(0);
                this.actionBarLayout.fragmentsStack.add(baseFragment2);
            }
            PasscodeView passcodeView2 = this.passcodeView;
            if (passcodeView2 == null || passcodeView2.getVisibility() != 0) {
                this.actionBarLayout.showLastFragment();
            }
        }
        this.shadowTabletSide.setVisibility(8);
        this.rightActionBarLayout.setVisibility(8);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
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
        this.passcodeView.onShow(z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showPasscodeActivity$9(runnable);
            }
        }, runnable2);
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate(new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword() {
                LaunchActivity.this.lambda$showPasscodeActivity$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPasscodeActivity$9(Runnable runnable) {
        this.actionBarLayout.setVisibility(4);
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.setVisibility(4);
            }
            this.rightActionBarLayout.setVisibility(4);
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
        this.actionBarLayout.setVisibility(0);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
            if (this.layersActionBarLayout.getVisibility() == 4) {
                this.layersActionBarLayout.setVisibility(0);
            }
            this.rightActionBarLayout.setVisibility(0);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:1076:0x1687, code lost:
        if (r2 == 0) goto L1087;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x1ef0, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r3)) != false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0330, code lost:
        if (r84.sendingText == null) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x015b, code lost:
        if (r1.equals(r0) != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x09c0, code lost:
        if (r7 == 0) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0a52, code lost:
        if (r7 == 0) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x1e72, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r3)) != false) goto L123;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1032:0x15ef A[Catch: Exception -> 0x15fd, TRY_LEAVE, TryCatch #18 {Exception -> 0x15fd, blocks: (B:1030:0x15e3, B:1032:0x15ef), top: B:1029:0x15e3 }] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x2327  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x237e  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x2386  */
    /* JADX WARN: Removed duplicated region for block: B:1192:0x03f7  */
    /* JADX WARN: Removed duplicated region for block: B:1212:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x1ebb  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x2059  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x2079  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x208a  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x206b  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x199c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x1ac3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:558:0x1b97  */
    /* JADX WARN: Removed duplicated region for block: B:560:0x1ba7  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x19de  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x073d  */
    /* JADX WARN: Removed duplicated region for block: B:619:0x0829 A[Catch: Exception -> 0x0837, TRY_LEAVE, TryCatch #1 {Exception -> 0x0837, blocks: (B:617:0x081d, B:619:0x0829), top: B:616:0x081d }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0337  */
    /* JADX WARN: Removed duplicated region for block: B:632:0x0836  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x1e0f  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x1e3e  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x22d4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x22dc  */
    /* JADX WARN: Removed duplicated region for block: B:934:0x1224 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x2372  */
    /* JADX WARN: Type inference failed for: r0v284, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v303, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v483, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v491, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v57, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v63, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v67, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v71, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v75, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v77, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v89, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r11v7, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r1v160, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v163, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v169, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v184, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r1v191, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r1v252, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v373, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v382, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v389, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v401, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r2v130, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r2v238, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r7v57, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r8v13, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v36 */
    /* JADX WARN: Type inference failed for: r8v38 */
    /* JADX WARN: Type inference failed for: r8v45 */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3) {
        long j;
        String str;
        final LaunchActivity launchActivity;
        int i;
        final int[] iArr;
        Intent intent2;
        long j2;
        long j3;
        long j4;
        int i2;
        final boolean z4;
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
        char c;
        Intent intent7;
        boolean z16;
        boolean z17;
        boolean z18;
        boolean z19;
        boolean z20;
        boolean z21;
        ?? r8;
        GroupCallActivity groupCallActivity;
        ?? r3;
        String str6;
        char c2;
        final BaseFragment editWidgetActivity;
        final boolean z22;
        BaseFragment baseFragment;
        boolean z23;
        boolean z24;
        boolean z25;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        boolean z26;
        Pattern compile;
        long j5;
        long j6;
        int i5;
        int i6;
        int i7;
        boolean z27;
        char c3;
        String str7;
        int[] iArr2;
        long j7;
        int i8;
        char c4;
        boolean z28;
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
        boolean z29;
        String str22;
        String str23;
        String str24;
        int i9;
        char c5;
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
        String str36;
        int i10;
        String str37;
        String str38;
        String str39;
        String str40;
        final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Cursor query;
        boolean z30;
        char c6;
        long j11;
        String str41;
        String str42;
        boolean z31;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z32;
        int i11;
        long j12;
        long j13;
        int parseInt;
        boolean z33;
        long j14;
        boolean z34;
        boolean z35;
        boolean z36;
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
        String str57;
        int i12;
        String str58;
        boolean z37;
        String str59;
        String str60;
        String str61;
        String str62;
        String str63;
        String str64;
        String str65;
        String str66;
        String str67;
        String str68;
        int i13;
        int i14;
        String str69;
        String str70;
        String str71;
        String str72;
        String str73;
        String str74;
        boolean z38;
        String queryParameter3;
        String queryParameter4;
        String[] split2;
        String substring;
        long j15;
        String type2;
        String stringExtra;
        Parcelable parcelableExtra;
        boolean z39;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str75;
        String str76;
        if (AndroidUtilities.handleProxyIntent(this, intent)) {
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
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
        boolean z40 = action != null && action.equals("voip");
        if (!z3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z40) {
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
        String str77 = "message_id";
        if ((1048576 & flags) == 0 && intent.getAction() != null && !z2) {
            String str78 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j15 = intent.getExtras().getLong("dialogId", 0L);
                    if (j15 == 0) {
                        try {
                            String string = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                for (int i15 = 0; i15 < size; i15++) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i15);
                                    if (string.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j16 = extras.getLong("dialogId", 0L);
                                        try {
                                            str75 = extras.getString("hash", null);
                                            j15 = j16;
                                            break;
                                        } catch (Throwable th) {
                                            th = th;
                                            j15 = j16;
                                            FileLog.e(th);
                                            str75 = null;
                                            str76 = SharedConfig.directShareHash;
                                            if (str76 != null) {
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
                        str75 = null;
                    } else {
                        str75 = intent.getExtras().getString("hash", null);
                    }
                    str76 = SharedConfig.directShareHash;
                    if (str76 != null) {
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
                    z39 = true;
                    if (z39) {
                    }
                    str = " ";
                    launchActivity = this;
                    j4 = j15;
                    i2 = -1;
                    z4 = false;
                    i3 = -1;
                    str2 = null;
                    j2 = 0;
                    i = 0;
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
                    str4 = null;
                    str5 = null;
                    i4 = 0;
                    c = 0;
                    iArr = iArr3;
                    intent7 = intent;
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
                        boolean z41 = parcelableExtra instanceof Uri;
                        Uri uri2 = parcelableExtra;
                        if (!z41) {
                            uri2 = Uri.parse(parcelableExtra.toString());
                        }
                        Uri uri3 = (Uri) uri2;
                        boolean z42 = uri3 != null && AndroidUtilities.isInternalUri(uri3);
                        if (!z42 && uri3 != null) {
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
                                    for (String str79 : set) {
                                        try {
                                            compile2 = Pattern.compile(str79);
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
                                            path = path.replace("file://", str78);
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
                        z39 = z42;
                        if (z39) {
                            Toast.makeText(this, "Unsupported content", 0).show();
                        }
                        str = " ";
                        launchActivity = this;
                        j4 = j15;
                        i2 = -1;
                        z4 = false;
                        i3 = -1;
                        str2 = null;
                        j2 = 0;
                        i = 0;
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
                        str4 = null;
                        str5 = null;
                        i4 = 0;
                        c = 0;
                        iArr = iArr3;
                        intent7 = intent;
                    }
                }
                z39 = false;
                if (z39) {
                }
                str = " ";
                launchActivity = this;
                j4 = j15;
                i2 = -1;
                z4 = false;
                i3 = -1;
                str2 = null;
                j2 = 0;
                i = 0;
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
                str4 = null;
                str5 = null;
                i4 = 0;
                c = 0;
                iArr = iArr3;
                intent7 = intent;
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
                        int i16 = 0;
                        while (i16 < parcelableArrayListExtra.size()) {
                            Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i16);
                            boolean z43 = parcelable instanceof Uri;
                            Uri uri5 = parcelable;
                            if (!z43) {
                                uri5 = Uri.parse(parcelable.toString());
                            }
                            Uri uri6 = (Uri) uri5;
                            if (uri6 != null && AndroidUtilities.isInternalUri(uri6)) {
                                parcelableArrayListExtra.remove(i16);
                                i16--;
                            }
                            i16++;
                        }
                        if (parcelableArrayListExtra.isEmpty()) {
                            arrayList = null;
                            if (arrayList != null) {
                                if (type != null && type.startsWith("image/")) {
                                    for (int i17 = 0; i17 < arrayList.size(); i17++) {
                                        Parcelable parcelable2 = (Parcelable) arrayList.get(i17);
                                        boolean z44 = parcelable2 instanceof Uri;
                                        Uri uri7 = parcelable2;
                                        if (!z44) {
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
                                    for (int i18 = 0; i18 < arrayList.size(); i18++) {
                                        Object obj = (Parcelable) arrayList.get(i18);
                                        if (!(obj instanceof Uri)) {
                                            obj = Uri.parse(obj.toString());
                                        }
                                        Uri uri9 = (Uri) obj;
                                        String path2 = AndroidUtilities.getPath(uri9);
                                        String obj2 = obj.toString();
                                        String str80 = obj2 == null ? path2 : obj2;
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("export path = " + str80);
                                        }
                                        if (str80 != null && this.exportingChatUri == null) {
                                            String fixFileName2 = FileLoader.fixFileName(MediaController.getFileName(uri9));
                                            for (String str81 : set2) {
                                                try {
                                                    compile = Pattern.compile(str81);
                                                } catch (Exception e4) {
                                                    FileLog.e(e4);
                                                }
                                                if (compile.matcher(str80).find() || compile.matcher(fixFileName2).find()) {
                                                    this.exportingChatUri = uri9;
                                                    z26 = true;
                                                    break;
                                                }
                                            }
                                            z26 = false;
                                            if (!z26) {
                                                if (str80.startsWith("content://com.kakao.talk") && str80.endsWith("KakaoTalkChats.txt")) {
                                                    this.exportingChatUri = uri9;
                                                }
                                            }
                                        }
                                        if (path2 != null) {
                                            if (path2.startsWith("file:")) {
                                                path2 = path2.replace("file://", str78);
                                            }
                                            if (this.documentsPathsArray == null) {
                                                this.documentsPathsArray = new ArrayList<>();
                                                this.documentsOriginalPathsArray = new ArrayList<>();
                                            }
                                            this.documentsPathsArray.add(path2);
                                            this.documentsOriginalPathsArray.add(str80);
                                        } else {
                                            if (this.documentsUrisArray == null) {
                                                this.documentsUrisArray = new ArrayList<>();
                                            }
                                            this.documentsUrisArray.add(uri9);
                                            this.documentsMimeType = type;
                                        }
                                    }
                                }
                                z25 = false;
                                if (z25) {
                                    Toast.makeText(this, "Unsupported content", 0).show();
                                }
                            }
                            z25 = true;
                            if (z25) {
                            }
                        }
                    }
                    arrayList = parcelableArrayListExtra;
                    if (arrayList != null) {
                    }
                    z25 = true;
                    if (z25) {
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
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
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
                                        str78 = null;
                                        str18 = null;
                                        str19 = null;
                                        str20 = null;
                                        str21 = null;
                                        z29 = false;
                                        str22 = null;
                                        str23 = null;
                                        str24 = null;
                                        i9 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z28 = false;
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
                                        str26 = null;
                                        str25 = null;
                                        str29 = null;
                                        str34 = null;
                                        str33 = null;
                                        str32 = null;
                                        str31 = null;
                                        str30 = null;
                                        str35 = null;
                                        str36 = null;
                                        i10 = -1;
                                        str37 = null;
                                        str38 = null;
                                        str39 = null;
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
                                                str26 = hashMap;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str28 = null;
                                                str27 = null;
                                                str25 = null;
                                            } else {
                                                String queryParameter7 = parse.getQueryParameter("start");
                                                String queryParameter8 = parse.getQueryParameter("startgroup");
                                                String queryParameter9 = parse.getQueryParameter("startchannel");
                                                String queryParameter10 = parse.getQueryParameter("admin");
                                                String queryParameter11 = parse.getQueryParameter("game");
                                                String queryParameter12 = parse.getQueryParameter("voicechat");
                                                String str82 = queryParameter5;
                                                String queryParameter13 = parse.getQueryParameter("livestream");
                                                String queryParameter14 = parse.getQueryParameter("startattach");
                                                String queryParameter15 = parse.getQueryParameter("choose");
                                                String queryParameter16 = parse.getQueryParameter("attach");
                                                ?? parseInt2 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                                String str83 = parseInt2.intValue() == 0 ? null : parseInt2;
                                                ?? parseInt3 = Utilities.parseInt((CharSequence) parse.getQueryParameter("thread"));
                                                String str84 = parseInt3.intValue() == 0 ? null : parseInt3;
                                                ?? parseInt4 = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                str36 = queryParameter13;
                                                if (parseInt4.intValue() == 0) {
                                                    str27 = queryParameter11;
                                                    str35 = queryParameter12;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str17 = str82;
                                                    str37 = queryParameter14;
                                                    str39 = queryParameter15;
                                                    str38 = queryParameter16;
                                                    str22 = str83;
                                                    str24 = str84;
                                                    str12 = null;
                                                    str13 = null;
                                                    str78 = null;
                                                    z29 = false;
                                                    str23 = null;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                    str28 = null;
                                                } else {
                                                    str28 = parseInt4;
                                                    str27 = queryParameter11;
                                                    str35 = queryParameter12;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str17 = str82;
                                                    str37 = queryParameter14;
                                                    str39 = queryParameter15;
                                                    str38 = queryParameter16;
                                                    str22 = str83;
                                                    str24 = str84;
                                                    str12 = null;
                                                    str13 = null;
                                                    str78 = null;
                                                    z29 = false;
                                                    str23 = null;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z12 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    str11 = null;
                                                }
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                i10 = -1;
                                                str18 = queryParameter7;
                                                str19 = queryParameter8;
                                                str20 = queryParameter9;
                                                str21 = queryParameter10;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                break;
                                            }
                                        } else if (uri10.startsWith("tg:invoice") || uri10.startsWith("tg://invoice")) {
                                            str17 = null;
                                            j11 = 0;
                                            str31 = Uri.parse(uri10.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                            str12 = null;
                                            str13 = null;
                                            str14 = null;
                                            str15 = null;
                                            str16 = null;
                                            str78 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            str22 = null;
                                            str23 = null;
                                            str24 = null;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            str11 = null;
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str29 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str30 = null;
                                            str35 = str30;
                                            str36 = str35;
                                            str37 = str36;
                                            str38 = str37;
                                            str39 = str38;
                                            j10 = j11;
                                            j8 = j10;
                                            z29 = false;
                                            i9 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z28 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            i10 = -1;
                                            break;
                                        } else if (uri10.startsWith("tg:privatepost") || uri10.startsWith("tg://privatepost")) {
                                            str17 = null;
                                            Uri parse2 = Uri.parse(uri10.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                            ?? parseInt5 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("post"));
                                            ?? parseLong = Utilities.parseLong(parse2.getQueryParameter("channel"));
                                            if (parseInt5.intValue() != 0) {
                                                j11 = 0;
                                                int i19 = (parseLong.longValue() > 0L ? 1 : (parseLong.longValue() == 0L ? 0 : -1));
                                                str41 = parseLong;
                                                str42 = parseInt5;
                                                break;
                                            } else {
                                                j11 = 0;
                                            }
                                            str41 = null;
                                            str42 = null;
                                            ?? parseInt6 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("thread"));
                                            int intValue = parseInt6.intValue();
                                            String str85 = parseInt6;
                                            if (intValue == 0) {
                                                str85 = null;
                                            }
                                            ?? parseInt7 = Utilities.parseInt((CharSequence) parse2.getQueryParameter("comment"));
                                            if (parseInt7.intValue() == 0) {
                                                str23 = str41;
                                                str24 = str85;
                                                str22 = str42;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str28 = null;
                                                str27 = null;
                                            } else {
                                                str28 = parseInt7;
                                                str23 = str41;
                                                str24 = str85;
                                                str22 = str42;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                str11 = null;
                                                str27 = null;
                                            }
                                            str26 = str27;
                                            str25 = str26;
                                        } else if (uri10.startsWith("tg:bg") || uri10.startsWith("tg://bg")) {
                                            Uri parse3 = Uri.parse(uri10.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                            ?? tLRPC$TL_wallPaper = new TLRPC$TL_wallPaper();
                                            tLRPC$TL_wallPaper.settings = new TLRPC$TL_wallPaperSettings();
                                            String queryParameter17 = parse3.getQueryParameter("slug");
                                            tLRPC$TL_wallPaper.slug = queryParameter17;
                                            if (queryParameter17 == null) {
                                                tLRPC$TL_wallPaper.slug = parse3.getQueryParameter("color");
                                            }
                                            String str86 = tLRPC$TL_wallPaper.slug;
                                            if (str86 != null && str86.length() == 6) {
                                                tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug, 16) | (-16777216);
                                                tLRPC$TL_wallPaper.slug = null;
                                                z31 = true;
                                                str17 = null;
                                            } else {
                                                String str87 = tLRPC$TL_wallPaper.slug;
                                                if (str87 != null && str87.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(6))) {
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
                                                        z31 = true;
                                                    } catch (Exception unused2) {
                                                    }
                                                }
                                                str17 = null;
                                                z31 = false;
                                            }
                                            if (!z31) {
                                                String queryParameter19 = parse3.getQueryParameter("mode");
                                                if (queryParameter19 != null && (split = queryParameter19.toLowerCase().split(" ")) != null && split.length > 0) {
                                                    for (int i20 = 0; i20 < split.length; i20++) {
                                                        if ("blur".equals(split[i20])) {
                                                            tLRPC$TL_wallPaper.settings.blur = true;
                                                        } else if ("motion".equals(split[i20])) {
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
                                            str32 = tLRPC$TL_wallPaper;
                                            str12 = str17;
                                            str13 = str12;
                                            str14 = str13;
                                            str15 = str14;
                                            str16 = str15;
                                            str78 = str16;
                                            str18 = str78;
                                            str19 = str18;
                                            str20 = str19;
                                            str21 = str20;
                                            str22 = str21;
                                            str23 = str22;
                                            str24 = str23;
                                            str8 = str24;
                                            str9 = str8;
                                            str10 = str9;
                                            str11 = str10;
                                            str28 = str11;
                                            str27 = str28;
                                            str26 = str27;
                                            str25 = str26;
                                            str29 = str25;
                                            str34 = str29;
                                            str33 = str34;
                                            str31 = str33;
                                            str30 = str31;
                                            str35 = str30;
                                            str36 = str35;
                                            str37 = str36;
                                            str38 = str37;
                                            str39 = str38;
                                            z29 = false;
                                            i9 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z28 = false;
                                            z12 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            j10 = 0;
                                            j8 = 0;
                                            i10 = -1;
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
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:addemoji") || uri10.startsWith("tg://addemoji")) {
                                                str16 = Uri.parse(uri10.replace("tg:addemoji", "tg://telegram.org").replace("tg://addemoji", "tg://telegram.org")).getQueryParameter("set");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:msg") || uri10.startsWith("tg://msg") || uri10.startsWith("tg://share") || uri10.startsWith("tg:share")) {
                                                Uri parse4 = Uri.parse(uri10.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                                String queryParameter20 = parse4.getQueryParameter("url");
                                                if (queryParameter20 != null) {
                                                    str78 = queryParameter20;
                                                }
                                                if (parse4.getQueryParameter("text") != null) {
                                                    if (str78.length() > 0) {
                                                        str78 = str78 + "\n";
                                                        z32 = true;
                                                    } else {
                                                        z32 = false;
                                                    }
                                                    str78 = str78 + parse4.getQueryParameter("text");
                                                } else {
                                                    z32 = false;
                                                }
                                                if (str78.length() > 16384) {
                                                    i11 = 0;
                                                    str78 = str78.substring(0, 16384);
                                                } else {
                                                    i11 = 0;
                                                }
                                                while (str78.endsWith("\n")) {
                                                    str78 = str78.substring(i11, str78.length() - 1);
                                                }
                                                z29 = z32;
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
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:confirmphone") || uri10.startsWith("tg://confirmphone")) {
                                                Uri parse5 = Uri.parse(uri10.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                                String queryParameter21 = parse5.getQueryParameter("phone");
                                                str13 = parse5.getQueryParameter("hash");
                                                str12 = queryParameter21;
                                                str14 = null;
                                            } else if (uri10.startsWith("tg:login") || uri10.startsWith("tg://login")) {
                                                Uri parse6 = Uri.parse(uri10.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                                String queryParameter22 = parse6.getQueryParameter("token");
                                                int intValue2 = Utilities.parseInt((CharSequence) parse6.getQueryParameter("code")).intValue();
                                                str34 = intValue2 != 0 ? str78 + intValue2 : null;
                                                str33 = queryParameter22;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:openmessage") || uri10.startsWith("tg://openmessage")) {
                                                Uri parse7 = Uri.parse(uri10.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                                String queryParameter23 = parse7.getQueryParameter("user_id");
                                                String queryParameter24 = parse7.getQueryParameter("chat_id");
                                                String queryParameter25 = parse7.getQueryParameter(str77);
                                                if (queryParameter23 != null) {
                                                    j13 = Long.parseLong(queryParameter23);
                                                    j12 = 0;
                                                    if (queryParameter25 != null) {
                                                        try {
                                                            parseInt = Integer.parseInt(queryParameter25);
                                                        } catch (NumberFormatException unused6) {
                                                        }
                                                        i9 = parseInt;
                                                        j8 = j12;
                                                        j10 = j13;
                                                        str12 = null;
                                                        str13 = null;
                                                        str14 = null;
                                                        str15 = null;
                                                        str16 = null;
                                                        str17 = null;
                                                        str78 = null;
                                                        str18 = null;
                                                        str19 = null;
                                                        str20 = null;
                                                        str21 = null;
                                                        z29 = false;
                                                        str22 = null;
                                                        str23 = null;
                                                        str24 = null;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z28 = false;
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
                                                        str26 = null;
                                                        str25 = null;
                                                        str29 = null;
                                                        str34 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        str30 = null;
                                                        str35 = null;
                                                        str36 = null;
                                                        i10 = -1;
                                                        str37 = null;
                                                        str38 = null;
                                                        str39 = null;
                                                    }
                                                    parseInt = 0;
                                                    i9 = parseInt;
                                                    j8 = j12;
                                                    j10 = j13;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
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
                                                    str26 = null;
                                                    str25 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str36 = null;
                                                    i10 = -1;
                                                    str37 = null;
                                                    str38 = null;
                                                    str39 = null;
                                                } else {
                                                    if (queryParameter24 != null) {
                                                        j12 = Long.parseLong(queryParameter24);
                                                        j13 = 0;
                                                        if (queryParameter25 != null) {
                                                        }
                                                        parseInt = 0;
                                                        i9 = parseInt;
                                                        j8 = j12;
                                                        j10 = j13;
                                                        str12 = null;
                                                        str13 = null;
                                                        str14 = null;
                                                        str15 = null;
                                                        str16 = null;
                                                        str17 = null;
                                                        str78 = null;
                                                        str18 = null;
                                                        str19 = null;
                                                        str20 = null;
                                                        str21 = null;
                                                        z29 = false;
                                                        str22 = null;
                                                        str23 = null;
                                                        str24 = null;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z28 = false;
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
                                                        str26 = null;
                                                        str25 = null;
                                                        str29 = null;
                                                        str34 = null;
                                                        str33 = null;
                                                        str32 = null;
                                                        str31 = null;
                                                        str30 = null;
                                                        str35 = null;
                                                        str36 = null;
                                                        i10 = -1;
                                                        str37 = null;
                                                        str38 = null;
                                                        str39 = null;
                                                    }
                                                    j12 = 0;
                                                    j13 = 0;
                                                    if (queryParameter25 != null) {
                                                    }
                                                    parseInt = 0;
                                                    i9 = parseInt;
                                                    j8 = j12;
                                                    j10 = j13;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
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
                                                    str26 = null;
                                                    str25 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str36 = null;
                                                    i10 = -1;
                                                    str37 = null;
                                                    str38 = null;
                                                    str39 = null;
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
                                                str26 = hashMap2;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:setlanguage") || uri10.startsWith("tg://setlanguage")) {
                                                str25 = Uri.parse(uri10.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:addtheme") || uri10.startsWith("tg://addtheme")) {
                                                str30 = Uri.parse(uri10.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:settings") || uri10.startsWith("tg://settings")) {
                                                if (uri10.contains("themes")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 2;
                                                } else if (uri10.contains("devices")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 3;
                                                } else if (uri10.contains("folders")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 4;
                                                } else if (uri10.contains("change_number")) {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 5;
                                                } else {
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 1;
                                                }
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:search") || uri10.startsWith("tg://search")) {
                                                String queryParameter27 = Uri.parse(uri10.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                                if (queryParameter27 != null) {
                                                    str78 = queryParameter27.trim();
                                                }
                                                str11 = str78;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:calllog") || uri10.startsWith("tg://calllog")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = true;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else if (uri10.startsWith("tg:call") || uri10.startsWith("tg://call")) {
                                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                                    if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                        String queryParameter28 = data.getQueryParameter("format");
                                                        String queryParameter29 = data.getQueryParameter("name");
                                                        String queryParameter30 = data.getQueryParameter("phone");
                                                        z33 = false;
                                                        List<TLRPC$TL_contact> findContacts = findContacts(queryParameter29, queryParameter30, false);
                                                        if (!findContacts.isEmpty() || queryParameter30 == null) {
                                                            j14 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                            if (j14 != 0) {
                                                                str78 = null;
                                                            } else if (queryParameter29 != null) {
                                                                str78 = queryParameter29;
                                                            }
                                                            boolean equalsIgnoreCase = MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(queryParameter28);
                                                            z34 = !equalsIgnoreCase;
                                                            z35 = equalsIgnoreCase;
                                                            z36 = false;
                                                            z33 = true;
                                                            str43 = null;
                                                        } else {
                                                            str44 = queryParameter30;
                                                            str43 = queryParameter29;
                                                            z36 = true;
                                                            z34 = false;
                                                            z35 = false;
                                                            j14 = 0;
                                                            str78 = null;
                                                            z14 = z36;
                                                            z11 = z34;
                                                            z28 = z35;
                                                            z12 = z33;
                                                            j10 = j14;
                                                            str9 = str43;
                                                            str8 = str78;
                                                            str10 = str44;
                                                            str12 = null;
                                                            str13 = null;
                                                            str14 = null;
                                                            str15 = null;
                                                            str16 = null;
                                                            str17 = null;
                                                            str78 = null;
                                                            str18 = null;
                                                            str19 = null;
                                                            str20 = null;
                                                            str21 = null;
                                                            z29 = false;
                                                            str22 = null;
                                                            str23 = null;
                                                            str24 = null;
                                                            i9 = 0;
                                                            c5 = 0;
                                                            z10 = false;
                                                            z13 = false;
                                                            z15 = false;
                                                            j8 = 0;
                                                            str11 = null;
                                                            str28 = null;
                                                            str27 = null;
                                                            str26 = null;
                                                            str25 = null;
                                                            str29 = null;
                                                            str34 = null;
                                                            str33 = null;
                                                            str32 = null;
                                                            str31 = null;
                                                            str30 = null;
                                                            str35 = null;
                                                            str36 = null;
                                                            i10 = -1;
                                                            str37 = null;
                                                            str38 = null;
                                                            str39 = null;
                                                        }
                                                    } else {
                                                        final Intent intent8 = new Intent(intent);
                                                        intent8.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                        intent8.putExtra("extra_force_call", true);
                                                        ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
                                                            @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                            public final void onResult(boolean z45) {
                                                                LaunchActivity.this.lambda$handleIntent$12(intent8, z45);
                                                            }
                                                        }, 1000L);
                                                        z36 = false;
                                                        z34 = false;
                                                        z35 = false;
                                                        z33 = false;
                                                        j14 = 0;
                                                        str43 = null;
                                                        str78 = null;
                                                    }
                                                    str44 = null;
                                                    z14 = z36;
                                                    z11 = z34;
                                                    z28 = z35;
                                                    z12 = z33;
                                                    j10 = j14;
                                                    str9 = str43;
                                                    str8 = str78;
                                                    str10 = str44;
                                                    str12 = null;
                                                    str13 = null;
                                                    str14 = null;
                                                    str15 = null;
                                                    str16 = null;
                                                    str17 = null;
                                                    str78 = null;
                                                    str18 = null;
                                                    str19 = null;
                                                    str20 = null;
                                                    str21 = null;
                                                    z29 = false;
                                                    str22 = null;
                                                    str23 = null;
                                                    str24 = null;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z13 = false;
                                                    z15 = false;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str28 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str25 = null;
                                                    str29 = null;
                                                    str34 = null;
                                                    str33 = null;
                                                    str32 = null;
                                                    str31 = null;
                                                    str30 = null;
                                                    str35 = null;
                                                    str36 = null;
                                                    i10 = -1;
                                                    str37 = null;
                                                    str38 = null;
                                                    str39 = null;
                                                }
                                            } else if (uri10.startsWith("tg:scanqr") || uri10.startsWith("tg://scanqr")) {
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
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
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
                                                z12 = false;
                                                z13 = true;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str29 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            } else {
                                                String replace = uri10.replace("tg://", str78).replace("tg:", str78);
                                                int indexOf = replace.indexOf(63);
                                                if (indexOf >= 0) {
                                                    replace = replace.substring(0, indexOf);
                                                }
                                                str29 = replace;
                                                str12 = null;
                                                str13 = null;
                                                str14 = null;
                                                str15 = null;
                                                str16 = null;
                                                str17 = null;
                                                str78 = null;
                                                str18 = null;
                                                str19 = null;
                                                str20 = null;
                                                str21 = null;
                                                z29 = false;
                                                str22 = null;
                                                str23 = null;
                                                str24 = null;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
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
                                                str28 = null;
                                                str27 = null;
                                                str26 = null;
                                                str25 = null;
                                                str34 = null;
                                                str33 = null;
                                                str32 = null;
                                                str31 = null;
                                                str30 = null;
                                                str35 = null;
                                                str36 = null;
                                                i10 = -1;
                                                str37 = null;
                                                str38 = null;
                                                str39 = null;
                                            }
                                            str15 = null;
                                            str16 = null;
                                            str17 = null;
                                            str78 = null;
                                            str18 = null;
                                            str19 = null;
                                            str20 = null;
                                            str21 = null;
                                            z29 = false;
                                            str22 = null;
                                            str23 = null;
                                            str24 = null;
                                            i9 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z28 = false;
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
                                            str28 = null;
                                            str27 = null;
                                            str26 = null;
                                            str25 = null;
                                            str29 = null;
                                            str34 = null;
                                            str33 = null;
                                            str32 = null;
                                            str31 = null;
                                            str30 = null;
                                            str35 = null;
                                            str36 = null;
                                            i10 = -1;
                                            str37 = null;
                                            str38 = null;
                                            str39 = null;
                                        }
                                        str29 = str25;
                                        str34 = str29;
                                        str33 = str34;
                                        str32 = str33;
                                        str31 = str32;
                                        str30 = str31;
                                        str35 = str30;
                                        str36 = str35;
                                        str37 = str36;
                                        str38 = str37;
                                        str39 = str38;
                                        j10 = j11;
                                        j8 = j10;
                                        z29 = false;
                                        i9 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z28 = false;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        i10 = -1;
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
                                            sb.append(TextUtils.isEmpty(data.getPath()) ? str78 : data.getPath());
                                            sb.append(TextUtils.isEmpty(data.getQuery()) ? str78 : "?" + data.getQuery());
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
                                                String replace2 = substring2.replace("bg/", str78);
                                                tLRPC$TL_wallPaper2.slug = replace2;
                                                if (replace2 != null && replace2.length() == 6) {
                                                    tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug, 16) | (-16777216);
                                                    tLRPC$TL_wallPaper2.slug = null;
                                                } else {
                                                    String str88 = tLRPC$TL_wallPaper2.slug;
                                                    if (str88 != null && str88.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(6))) {
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
                                                    z38 = false;
                                                    if (!z38) {
                                                        String queryParameter33 = data.getQueryParameter("mode");
                                                        if (queryParameter33 != null && (split2 = queryParameter33.toLowerCase().split(" ")) != null && split2.length > 0) {
                                                            for (int i21 = 0; i21 < split2.length; i21++) {
                                                                if ("blur".equals(split2[i21])) {
                                                                    tLRPC$TL_wallPaper2.settings.blur = true;
                                                                } else if ("motion".equals(split2[i21])) {
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
                                                    str60 = tLRPC$TL_wallPaper2;
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
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
                                                    str59 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str89 = str53;
                                                    str18 = str56;
                                                    str78 = str89;
                                                    break;
                                                }
                                                z38 = true;
                                                if (!z38) {
                                                }
                                                str60 = tLRPC$TL_wallPaper2;
                                                str45 = null;
                                                z37 = false;
                                                str58 = null;
                                                i12 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str57 = null;
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
                                                str59 = null;
                                                str61 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                i10 = i12;
                                                str19 = str57;
                                                str20 = str55;
                                                str27 = str51;
                                                str35 = str50;
                                                str36 = str49;
                                                str25 = str47;
                                                str30 = str46;
                                                str34 = str59;
                                                str32 = str60;
                                                str31 = str61;
                                                str22 = str68;
                                                str24 = str66;
                                                str28 = str65;
                                                str37 = str64;
                                                str38 = str63;
                                                str39 = str62;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str26 = null;
                                                str29 = null;
                                                str33 = null;
                                                str14 = str45;
                                                z29 = z37;
                                                str17 = str58;
                                                str12 = str52;
                                                str13 = str48;
                                                str23 = str67;
                                                z12 = false;
                                                str21 = str54;
                                                String str892 = str53;
                                                str18 = str56;
                                                str78 = str892;
                                            } else if (substring2.startsWith("login/")) {
                                                int intValue3 = Utilities.parseInt((CharSequence) substring2.replace("login/", str78)).intValue();
                                                str59 = intValue3 != 0 ? str78 + intValue3 : null;
                                                str45 = null;
                                                z37 = false;
                                                str58 = null;
                                                i12 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str57 = null;
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
                                                str60 = null;
                                                str61 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                i10 = i12;
                                                str19 = str57;
                                                str20 = str55;
                                                str27 = str51;
                                                str35 = str50;
                                                str36 = str49;
                                                str25 = str47;
                                                str30 = str46;
                                                str34 = str59;
                                                str32 = str60;
                                                str31 = str61;
                                                str22 = str68;
                                                str24 = str66;
                                                str28 = str65;
                                                str37 = str64;
                                                str38 = str63;
                                                str39 = str62;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str26 = null;
                                                str29 = null;
                                                str33 = null;
                                                str14 = str45;
                                                z29 = z37;
                                                str17 = str58;
                                                str12 = str52;
                                                str13 = str48;
                                                str23 = str67;
                                                z12 = false;
                                                str21 = str54;
                                                String str8922 = str53;
                                                str18 = str56;
                                                str78 = str8922;
                                            } else {
                                                if (substring2.startsWith("joinchat/")) {
                                                    str45 = substring2.replace("joinchat/", str78);
                                                } else if (substring2.startsWith("+")) {
                                                    str45 = substring2.replace("+", str78);
                                                    if (AndroidUtilities.isNumeric(str45)) {
                                                        str58 = str45;
                                                        str45 = null;
                                                        z37 = false;
                                                        i12 = -1;
                                                        str15 = null;
                                                        str16 = null;
                                                        str57 = null;
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
                                                        str59 = null;
                                                        str60 = null;
                                                        str61 = null;
                                                        str68 = null;
                                                        str67 = null;
                                                        str66 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        str63 = null;
                                                        str62 = null;
                                                        i10 = i12;
                                                        str19 = str57;
                                                        str20 = str55;
                                                        str27 = str51;
                                                        str35 = str50;
                                                        str36 = str49;
                                                        str25 = str47;
                                                        str30 = str46;
                                                        str34 = str59;
                                                        str32 = str60;
                                                        str31 = str61;
                                                        str22 = str68;
                                                        str24 = str66;
                                                        str28 = str65;
                                                        str37 = str64;
                                                        str38 = str63;
                                                        str39 = str62;
                                                        i9 = 0;
                                                        c5 = 0;
                                                        z10 = false;
                                                        z11 = false;
                                                        z28 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        z15 = false;
                                                        str8 = null;
                                                        str9 = null;
                                                        str10 = null;
                                                        j10 = 0;
                                                        j8 = 0;
                                                        str11 = null;
                                                        str26 = null;
                                                        str29 = null;
                                                        str33 = null;
                                                        str14 = str45;
                                                        z29 = z37;
                                                        str17 = str58;
                                                        str12 = str52;
                                                        str13 = str48;
                                                        str23 = str67;
                                                        z12 = false;
                                                        str21 = str54;
                                                        String str89222 = str53;
                                                        str18 = str56;
                                                        str78 = str89222;
                                                    }
                                                } else if (substring2.startsWith("addstickers/")) {
                                                    str15 = substring2.replace("addstickers/", str78);
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str16 = null;
                                                    str57 = null;
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
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str892222 = str53;
                                                    str18 = str56;
                                                    str78 = str892222;
                                                } else if (substring2.startsWith("addemoji/")) {
                                                    str16 = substring2.replace("addemoji/", str78);
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str57 = null;
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
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str8922222 = str53;
                                                    str18 = str56;
                                                    str78 = str8922222;
                                                } else if (substring2.startsWith("msg/") || substring2.startsWith("share/")) {
                                                    String queryParameter35 = data.getQueryParameter("url");
                                                    if (queryParameter35 != null) {
                                                        str78 = queryParameter35;
                                                    }
                                                    if (data.getQueryParameter("text") != null) {
                                                        if (str78.length() > 0) {
                                                            str78 = str78 + "\n";
                                                            z37 = true;
                                                        } else {
                                                            z37 = false;
                                                        }
                                                        str78 = str78 + data.getQueryParameter("text");
                                                    } else {
                                                        z37 = false;
                                                    }
                                                    if (str78.length() > 16384) {
                                                        i13 = 0;
                                                        str78 = str78.substring(0, 16384);
                                                    } else {
                                                        i13 = 0;
                                                    }
                                                    while (str78.endsWith("\n")) {
                                                        str78 = str78.substring(i13, str78.length() - 1);
                                                    }
                                                    str53 = str78;
                                                    str45 = null;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str89222222 = str53;
                                                    str18 = str56;
                                                    str78 = str89222222;
                                                } else if (substring2.startsWith("confirmphone")) {
                                                    String queryParameter36 = data.getQueryParameter("phone");
                                                    str48 = data.getQueryParameter("hash");
                                                    str52 = queryParameter36;
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str47 = null;
                                                    str46 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str892222222 = str53;
                                                    str18 = str56;
                                                    str78 = str892222222;
                                                } else if (substring2.startsWith("setlanguage/")) {
                                                    str47 = substring2.substring(12);
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    str55 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str51 = null;
                                                    str50 = null;
                                                    str49 = null;
                                                    str48 = null;
                                                    str46 = null;
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str8922222222 = str53;
                                                    str18 = str56;
                                                    str78 = str8922222222;
                                                } else if (substring2.startsWith("addtheme/")) {
                                                    str46 = substring2.substring(9);
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
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
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str89222222222 = str53;
                                                    str18 = str56;
                                                    str78 = str89222222222;
                                                } else if (substring2.startsWith("c/")) {
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() == 3) {
                                                        ?? parseLong2 = Utilities.parseLong(pathSegments.get(1));
                                                        ?? parseInt8 = Utilities.parseInt((CharSequence) pathSegments.get(2));
                                                        if (parseInt8.intValue() != 0) {
                                                            int i22 = (parseLong2.longValue() > 0L ? 1 : (parseLong2.longValue() == 0L ? 0 : -1));
                                                            str73 = parseInt8;
                                                            str74 = parseLong2;
                                                            break;
                                                        }
                                                        str73 = null;
                                                        str74 = null;
                                                        ?? parseInt9 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        int intValue4 = parseInt9.intValue();
                                                        str70 = parseInt9;
                                                        str71 = str73;
                                                        str72 = str74;
                                                        if (intValue4 == 0) {
                                                            str70 = null;
                                                            str71 = str73;
                                                            str72 = str74;
                                                        }
                                                    } else {
                                                        str70 = null;
                                                        str71 = null;
                                                        str72 = null;
                                                    }
                                                    str66 = str70;
                                                    str68 = str71;
                                                    str67 = str72;
                                                    str45 = null;
                                                    z37 = false;
                                                    str58 = null;
                                                    i12 = -1;
                                                    str15 = null;
                                                    str16 = null;
                                                    str57 = null;
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
                                                    str59 = null;
                                                    str60 = null;
                                                    str61 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str892222222222 = str53;
                                                    str18 = str56;
                                                    str78 = str892222222222;
                                                } else if (substring2.length() >= 1) {
                                                    ArrayList arrayList3 = new ArrayList(data.getPathSegments());
                                                    if (arrayList3.size() > 0) {
                                                        i14 = 0;
                                                        if (((String) arrayList3.get(0)).equals("s")) {
                                                            arrayList3.remove(0);
                                                        }
                                                    } else {
                                                        i14 = 0;
                                                    }
                                                    if (arrayList3.size() > 0) {
                                                        str58 = (String) arrayList3.get(i14);
                                                        if (arrayList3.size() > 1) {
                                                            ?? parseInt10 = Utilities.parseInt((CharSequence) arrayList3.get(1));
                                                            int intValue5 = parseInt10.intValue();
                                                            str69 = parseInt10;
                                                            break;
                                                        }
                                                        str69 = null;
                                                    } else {
                                                        str69 = null;
                                                        str58 = null;
                                                    }
                                                    i12 = str69 != null ? getTimestampFromLink(data) : -1;
                                                    str56 = data.getQueryParameter("start");
                                                    String queryParameter37 = data.getQueryParameter("startgroup");
                                                    String queryParameter38 = data.getQueryParameter("startchannel");
                                                    String queryParameter39 = data.getQueryParameter("admin");
                                                    String str90 = str69;
                                                    String queryParameter40 = data.getQueryParameter("game");
                                                    String queryParameter41 = data.getQueryParameter("voicechat");
                                                    String queryParameter42 = data.getQueryParameter("livestream");
                                                    String queryParameter43 = data.getQueryParameter("startattach");
                                                    String queryParameter44 = data.getQueryParameter("choose");
                                                    String queryParameter45 = data.getQueryParameter("attach");
                                                    ?? parseInt11 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                    String str91 = parseInt11.intValue() == 0 ? null : parseInt11;
                                                    ?? parseInt12 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                    str68 = str90;
                                                    if (parseInt12.intValue() == 0) {
                                                        str64 = queryParameter43;
                                                        str62 = queryParameter44;
                                                        str63 = queryParameter45;
                                                        str66 = str91;
                                                        str45 = null;
                                                        z37 = false;
                                                        str52 = null;
                                                        str48 = null;
                                                        str47 = null;
                                                        str46 = null;
                                                        str59 = null;
                                                        str60 = null;
                                                        str61 = null;
                                                        str67 = null;
                                                        str65 = null;
                                                    } else {
                                                        str65 = parseInt12;
                                                        str64 = queryParameter43;
                                                        str62 = queryParameter44;
                                                        str63 = queryParameter45;
                                                        str66 = str91;
                                                        str45 = null;
                                                        z37 = false;
                                                        str52 = null;
                                                        str48 = null;
                                                        str47 = null;
                                                        str46 = null;
                                                        str59 = null;
                                                        str60 = null;
                                                        str61 = null;
                                                        str67 = null;
                                                    }
                                                    str51 = queryParameter40;
                                                    str50 = queryParameter41;
                                                    str49 = queryParameter42;
                                                    str53 = null;
                                                    str55 = queryParameter38;
                                                    str54 = queryParameter39;
                                                    str16 = null;
                                                    str57 = queryParameter37;
                                                    str15 = null;
                                                    i10 = i12;
                                                    str19 = str57;
                                                    str20 = str55;
                                                    str27 = str51;
                                                    str35 = str50;
                                                    str36 = str49;
                                                    str25 = str47;
                                                    str30 = str46;
                                                    str34 = str59;
                                                    str32 = str60;
                                                    str31 = str61;
                                                    str22 = str68;
                                                    str24 = str66;
                                                    str28 = str65;
                                                    str37 = str64;
                                                    str38 = str63;
                                                    str39 = str62;
                                                    i9 = 0;
                                                    c5 = 0;
                                                    z10 = false;
                                                    z11 = false;
                                                    z28 = false;
                                                    z13 = false;
                                                    z14 = false;
                                                    z15 = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    str10 = null;
                                                    j10 = 0;
                                                    j8 = 0;
                                                    str11 = null;
                                                    str26 = null;
                                                    str29 = null;
                                                    str33 = null;
                                                    str14 = str45;
                                                    z29 = z37;
                                                    str17 = str58;
                                                    str12 = str52;
                                                    str13 = str48;
                                                    str23 = str67;
                                                    z12 = false;
                                                    str21 = str54;
                                                    String str8922222222222 = str53;
                                                    str18 = str56;
                                                    str78 = str8922222222222;
                                                }
                                                z37 = false;
                                                str58 = null;
                                                i12 = -1;
                                                str15 = null;
                                                str16 = null;
                                                str57 = null;
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
                                                str59 = null;
                                                str60 = null;
                                                str61 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                i10 = i12;
                                                str19 = str57;
                                                str20 = str55;
                                                str27 = str51;
                                                str35 = str50;
                                                str36 = str49;
                                                str25 = str47;
                                                str30 = str46;
                                                str34 = str59;
                                                str32 = str60;
                                                str31 = str61;
                                                str22 = str68;
                                                str24 = str66;
                                                str28 = str65;
                                                str37 = str64;
                                                str38 = str63;
                                                str39 = str62;
                                                i9 = 0;
                                                c5 = 0;
                                                z10 = false;
                                                z11 = false;
                                                z28 = false;
                                                z13 = false;
                                                z14 = false;
                                                z15 = false;
                                                str8 = null;
                                                str9 = null;
                                                str10 = null;
                                                j10 = 0;
                                                j8 = 0;
                                                str11 = null;
                                                str26 = null;
                                                str29 = null;
                                                str33 = null;
                                                str14 = str45;
                                                z29 = z37;
                                                str17 = str58;
                                                str12 = str52;
                                                str13 = str48;
                                                str23 = str67;
                                                z12 = false;
                                                str21 = str54;
                                                String str89222222222222 = str53;
                                                str18 = str56;
                                                str78 = str89222222222222;
                                            }
                                            str61 = substring;
                                            str45 = null;
                                            z37 = false;
                                            str58 = null;
                                            i12 = -1;
                                            str15 = null;
                                            str16 = null;
                                            str57 = null;
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
                                            str59 = null;
                                            str60 = null;
                                            str68 = null;
                                            str67 = null;
                                            str66 = null;
                                            str65 = null;
                                            str64 = null;
                                            str63 = null;
                                            str62 = null;
                                            i10 = i12;
                                            str19 = str57;
                                            str20 = str55;
                                            str27 = str51;
                                            str35 = str50;
                                            str36 = str49;
                                            str25 = str47;
                                            str30 = str46;
                                            str34 = str59;
                                            str32 = str60;
                                            str31 = str61;
                                            str22 = str68;
                                            str24 = str66;
                                            str28 = str65;
                                            str37 = str64;
                                            str38 = str63;
                                            str39 = str62;
                                            i9 = 0;
                                            c5 = 0;
                                            z10 = false;
                                            z11 = false;
                                            z28 = false;
                                            z13 = false;
                                            z14 = false;
                                            z15 = false;
                                            str8 = null;
                                            str9 = null;
                                            str10 = null;
                                            j10 = 0;
                                            j8 = 0;
                                            str11 = null;
                                            str26 = null;
                                            str29 = null;
                                            str33 = null;
                                            str14 = str45;
                                            z29 = z37;
                                            str17 = str58;
                                            str12 = str52;
                                            str13 = str48;
                                            str23 = str67;
                                            z12 = false;
                                            str21 = str54;
                                            String str892222222222222 = str53;
                                            str18 = str56;
                                            str78 = str892222222222222;
                                        }
                                        str45 = null;
                                        z37 = false;
                                        str58 = null;
                                        i12 = -1;
                                        str15 = null;
                                        str16 = null;
                                        str57 = null;
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
                                        str59 = null;
                                        str60 = null;
                                        str61 = null;
                                        str68 = null;
                                        str67 = null;
                                        str66 = null;
                                        str65 = null;
                                        str64 = null;
                                        str63 = null;
                                        str62 = null;
                                        i10 = i12;
                                        str19 = str57;
                                        str20 = str55;
                                        str27 = str51;
                                        str35 = str50;
                                        str36 = str49;
                                        str25 = str47;
                                        str30 = str46;
                                        str34 = str59;
                                        str32 = str60;
                                        str31 = str61;
                                        str22 = str68;
                                        str24 = str66;
                                        str28 = str65;
                                        str37 = str64;
                                        str38 = str63;
                                        str39 = str62;
                                        i9 = 0;
                                        c5 = 0;
                                        z10 = false;
                                        z11 = false;
                                        z28 = false;
                                        z13 = false;
                                        z14 = false;
                                        z15 = false;
                                        str8 = null;
                                        str9 = null;
                                        str10 = null;
                                        j10 = 0;
                                        j8 = 0;
                                        str11 = null;
                                        str26 = null;
                                        str29 = null;
                                        str33 = null;
                                        str14 = str45;
                                        z29 = z37;
                                        str17 = str58;
                                        str12 = str52;
                                        str13 = str48;
                                        str23 = str67;
                                        z12 = false;
                                        str21 = str54;
                                        String str8922222222222222 = str53;
                                        str18 = str56;
                                        str78 = str8922222222222222;
                                    }
                                    break;
                            }
                            if (!intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                                str40 = str77;
                                FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(scheme) && str29 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                                intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                            } else {
                                str40 = str77;
                            }
                            if (str34 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                str7 = str40;
                            } else if (str12 == null || str13 != null) {
                                str = " ";
                                iArr2 = iArr3;
                                str7 = str40;
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
                                final String str92 = str12;
                                ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$handleIntent$14(alertDialog, str92, bundle, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                                    }
                                }, 2);
                            } else if (str17 != null || str14 != null || str15 != null || str16 != null || str78 != null || str27 != null || str35 != null || str26 != null || str29 != null || str25 != null || str34 != null || str32 != null || str31 != null || str23 != null || str30 != null || str33 != null) {
                                if (str78 != null && str78.startsWith("@")) {
                                    str78 = " " + str78;
                                }
                                str = " ";
                                str7 = str40;
                                iArr2 = iArr3;
                                runLinkRequest(iArr3[0], str17, str14, str15, str16, str18, str19, str20, str21, str78, z29, str22, str23, str24, str28, str27, str26, str25, str29, str34, str33, str32, str31, str30, str35, str36, 0, i10, str37, str38, str39);
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
                                            int intValue6 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                            int i23 = 0;
                                            while (true) {
                                                if (i23 < 4) {
                                                    if (UserConfig.getInstance(i23).getClientUserId() != intValue6) {
                                                        try {
                                                            i23++;
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
                                                                i8 = i9;
                                                                c4 = c5;
                                                                j7 = j10;
                                                                str7 = str40;
                                                                intent7 = intent;
                                                                i = i8;
                                                                c = c4;
                                                                z4 = z28;
                                                                str4 = str9;
                                                                str5 = str10;
                                                                j2 = j8;
                                                                str3 = str11;
                                                                str77 = str7;
                                                                iArr = iArr2;
                                                                i2 = -1;
                                                                z5 = false;
                                                                z6 = false;
                                                                z9 = false;
                                                                z8 = false;
                                                                z7 = false;
                                                                j4 = 0;
                                                                i4 = 0;
                                                                j3 = j7;
                                                                str2 = str8;
                                                                i3 = -1;
                                                                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                                }
                                                                z16 = false;
                                                                z17 = true;
                                                                z18 = z16;
                                                                z19 = z;
                                                                z20 = z18;
                                                                z21 = false;
                                                                r8 = z20;
                                                                if (!z21) {
                                                                }
                                                                if (z40) {
                                                                }
                                                                if (!z7) {
                                                                }
                                                                intent7.setAction(r8);
                                                                return z21;
                                                            }
                                                        }
                                                    } else {
                                                        iArr3[0] = i23;
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
                                                    z30 = true;
                                                } else {
                                                    j10 = j17;
                                                    z30 = z11;
                                                    if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                        z28 = true;
                                                    }
                                                }
                                                if (query != null) {
                                                    try {
                                                        query.close();
                                                    } catch (Exception e7) {
                                                        e = e7;
                                                        z11 = z30;
                                                        FileLog.e(e);
                                                        str = " ";
                                                        iArr2 = iArr3;
                                                        launchActivity = this;
                                                        i8 = i9;
                                                        c4 = c5;
                                                        j7 = j10;
                                                        str7 = str40;
                                                        intent7 = intent;
                                                        i = i8;
                                                        c = c4;
                                                        z4 = z28;
                                                        str4 = str9;
                                                        str5 = str10;
                                                        j2 = j8;
                                                        str3 = str11;
                                                        str77 = str7;
                                                        iArr = iArr2;
                                                        i2 = -1;
                                                        z5 = false;
                                                        z6 = false;
                                                        z9 = false;
                                                        z8 = false;
                                                        z7 = false;
                                                        j4 = 0;
                                                        i4 = 0;
                                                        j3 = j7;
                                                        str2 = str8;
                                                        i3 = -1;
                                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        }
                                                        z16 = false;
                                                        z17 = true;
                                                        z18 = z16;
                                                        z19 = z;
                                                        z20 = z18;
                                                        z21 = false;
                                                        r8 = z20;
                                                        if (!z21) {
                                                        }
                                                        if (z40) {
                                                        }
                                                        if (!z7) {
                                                        }
                                                        intent7.setAction(r8);
                                                        return z21;
                                                    }
                                                }
                                                z11 = z30;
                                                str = " ";
                                                iArr2 = iArr3;
                                                launchActivity = this;
                                                i8 = i9;
                                                c4 = c5;
                                                j7 = j10;
                                                str7 = str40;
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
                                z30 = z11;
                                if (query != null) {
                                }
                                z11 = z30;
                                str = " ";
                                iArr2 = iArr3;
                                launchActivity = this;
                                i8 = i9;
                                c4 = c5;
                                j7 = j10;
                                str7 = str40;
                            }
                            i8 = i9;
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
                        str78 = null;
                        str18 = null;
                        str19 = null;
                        str20 = null;
                        str21 = null;
                        z29 = false;
                        str22 = null;
                        str23 = null;
                        str24 = null;
                        i9 = 0;
                        c5 = 0;
                        z10 = false;
                        z11 = false;
                        z28 = false;
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
                        str26 = null;
                        str25 = null;
                        str29 = null;
                        str34 = null;
                        str33 = null;
                        str32 = null;
                        str31 = null;
                        str30 = null;
                        str35 = null;
                        str36 = null;
                        i10 = -1;
                        str37 = null;
                        str38 = null;
                        str39 = null;
                        if (!intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                        }
                        if (str34 != null) {
                        }
                        if (str12 == null) {
                        }
                        str = " ";
                        iArr2 = iArr3;
                        str7 = str40;
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
                        final String str922 = str12;
                        ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$handleIntent$14(alertDialog2, str922, bundle2, tLRPC$TL_account_sendConfirmPhoneCode, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                        i8 = i9;
                        c4 = c5;
                        j7 = j10;
                    } else {
                        str7 = str77;
                        str = " ";
                        iArr2 = iArr3;
                        launchActivity = this;
                        j7 = 0;
                        i8 = 0;
                        c4 = 0;
                        z10 = false;
                        z11 = false;
                        z28 = false;
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
                    i = i8;
                    c = c4;
                    z4 = z28;
                    str4 = str9;
                    str5 = str10;
                    j2 = j8;
                    str3 = str11;
                    str77 = str7;
                    iArr = iArr2;
                    i2 = -1;
                    z5 = false;
                    z6 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    j4 = 0;
                    i4 = 0;
                    j3 = j7;
                    str2 = str8;
                    i3 = -1;
                } else {
                    str = " ";
                    launchActivity = this;
                    i = 0;
                    int i24 = -1;
                    if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        intent7 = intent;
                        str77 = str77;
                        iArr = iArr3;
                        i2 = -1;
                        z4 = false;
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
                        c = 1;
                    } else if (intent.getAction().equals("new_dialog")) {
                        intent6 = intent;
                        str77 = str77;
                        iArr = iArr3;
                        i2 = -1;
                        z4 = false;
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
                        c = 0;
                        intent7 = intent6;
                    } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                        Intent intent9 = intent;
                        long longExtra = intent9.getLongExtra("chatId", 0L);
                        long longExtra2 = intent9.getLongExtra("userId", 0L);
                        int intExtra = intent9.getIntExtra("encId", 0);
                        int intExtra2 = intent9.getIntExtra("appWidgetId", 0);
                        if (intExtra2 != 0) {
                            i24 = intent9.getIntExtra("appWidgetType", 0);
                            j5 = 0;
                            j6 = 0;
                            i6 = intExtra2;
                            str77 = str77;
                            iArr = iArr3;
                            i7 = 0;
                            i5 = 0;
                            z27 = false;
                            c3 = 6;
                        } else {
                            str77 = str77;
                            int intExtra3 = intent9.getIntExtra(str77, 0);
                            if (longExtra != 0) {
                                iArr = iArr3;
                                NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                j6 = longExtra;
                                i5 = intExtra3;
                                i6 = -1;
                                i7 = 0;
                                z27 = false;
                                c3 = 0;
                                j5 = 0;
                            } else {
                                iArr = iArr3;
                                if (longExtra2 != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    i5 = intExtra3;
                                    j5 = longExtra2;
                                    i6 = -1;
                                    i7 = 0;
                                    z27 = false;
                                    c3 = 0;
                                    j6 = 0;
                                } else if (intExtra != 0) {
                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    j5 = 0;
                                    j6 = 0;
                                    i5 = intExtra3;
                                    z27 = false;
                                    c3 = 0;
                                    i7 = intExtra;
                                    i6 = -1;
                                } else {
                                    j5 = 0;
                                    j6 = 0;
                                    i5 = intExtra3;
                                    i6 = -1;
                                    i7 = 0;
                                    z27 = true;
                                    c3 = 0;
                                }
                            }
                        }
                        j4 = 0;
                        i4 = i7;
                        j2 = j6;
                        i = i5;
                        c = c3;
                        z4 = false;
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
                        z5 = z27;
                        str2 = null;
                        i3 = i6;
                        i2 = i24;
                        intent7 = intent9;
                    } else {
                        intent2 = intent;
                        str77 = str77;
                        iArr = iArr3;
                        j = 0;
                        if (intent.getAction().equals("com.tmessages.openplayer")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i2 = -1;
                            z4 = false;
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
                            c = 0;
                            intent7 = intent6;
                        } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                            j2 = 0;
                            j3 = 0;
                            j4 = 0;
                            i2 = -1;
                            z4 = false;
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
                            c = 0;
                            intent7 = intent6;
                        } else {
                            if (action.equals("voip_chat")) {
                                j2 = 0;
                                j3 = 0;
                                j4 = 0;
                                i2 = -1;
                                z4 = false;
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
                                c = 0;
                                intent7 = intent6;
                            }
                            j2 = j;
                            j3 = j2;
                            j4 = j3;
                            i2 = -1;
                            z4 = false;
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
                            if (i != 0) {
                                bundle3.putInt(str77, i);
                            }
                            if (!mainFragmentsStack.isEmpty()) {
                                MessagesController messagesController = MessagesController.getInstance(iArr[0]);
                                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                            }
                            if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle3), false, true, true, false)) {
                                launchActivity.drawerLayoutContainer.closeDrawer();
                                z21 = true;
                            }
                            z21 = false;
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
                        if (i != 0) {
                            bundle4.putInt(str77, i);
                        }
                        if (!mainFragmentsStack.isEmpty()) {
                            MessagesController messagesController2 = MessagesController.getInstance(iArr[0]);
                            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                        }
                        if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle4), false, true, true, false)) {
                            launchActivity.drawerLayoutContainer.closeDrawer();
                            z21 = true;
                        }
                        z21 = false;
                    } else {
                        int i25 = i4;
                        if (i25 != 0) {
                            Bundle bundle5 = new Bundle();
                            bundle5.putInt("enc_id", i25);
                            if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle5), false, true, true, false)) {
                                launchActivity.drawerLayoutContainer.closeDrawer();
                                z21 = true;
                            }
                            z21 = false;
                        } else {
                            if (z5) {
                                if (!AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.removeAllFragments();
                                } else if (!launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                    while (true) {
                                        int size2 = launchActivity.layersActionBarLayout.fragmentsStack.size();
                                        int i26 = r3 == true ? 1 : 0;
                                        int i27 = r3 == true ? 1 : 0;
                                        int i28 = r3 == true ? 1 : 0;
                                        int i29 = r3 == true ? 1 : 0;
                                        if (size2 - i26 > 0) {
                                            ActionBarLayout actionBarLayout = launchActivity.layersActionBarLayout;
                                            actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                                        } else {
                                            z19 = false;
                                            launchActivity.layersActionBarLayout.closeLastFragment(false);
                                            z24 = false;
                                        }
                                    }
                                }
                                z19 = false;
                                z24 = false;
                            } else {
                                if (!z6) {
                                    z18 = false;
                                    z23 = false;
                                    z23 = false;
                                    z16 = false;
                                    z23 = false;
                                    z18 = false;
                                    z18 = false;
                                    r8 = 0;
                                    z18 = false;
                                    r8 = 0;
                                    if (z9) {
                                        if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                            launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
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
                                            z20 = z18;
                                            z21 = false;
                                            r8 = z20;
                                            if (!z21 && !z19) {
                                                if (!AndroidUtilities.isTablet()) {
                                                    if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        if (launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                                            launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        }
                                                    } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                                        DialogsActivity dialogsActivity2 = new DialogsActivity(r8);
                                                        dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                                        if (str3 != null) {
                                                            dialogsActivity2.setInitialSearchString(str3);
                                                        }
                                                        launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2);
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(z17, false);
                                                    }
                                                } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
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
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(z17, false);
                                                    }
                                                }
                                                launchActivity.actionBarLayout.showLastFragment();
                                                if (AndroidUtilities.isTablet()) {
                                                    launchActivity.layersActionBarLayout.showLastFragment();
                                                    launchActivity.rightActionBarLayout.showLastFragment();
                                                }
                                            }
                                            if (z40) {
                                                VoIPFragment.show(launchActivity, iArr[0]);
                                            }
                                            if (!z7 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                                                groupCallActivity.dismiss();
                                            }
                                            intent7.setAction(r8);
                                            return z21;
                                        } else if (launchActivity.importingStickers != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
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
                                                        baseFragment = new ProfileActivity(bundle6);
                                                    } else if (c7 == 2) {
                                                        baseFragment = new ThemeActivity(0);
                                                    } else if (c7 == 3) {
                                                        baseFragment = new SessionsActivity(0);
                                                    } else if (c7 == 4) {
                                                        baseFragment = new FiltersSetupActivity();
                                                    } else if (c7 == 5) {
                                                        editWidgetActivity = new ActionIntroActivity(3);
                                                        c2 = 6;
                                                        z22 = true;
                                                        if (c7 == c2) {
                                                            launchActivity.actionBarLayout.presentFragment(editWidgetActivity, false, true, true, false);
                                                        } else {
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    LaunchActivity.this.lambda$handleIntent$18(editWidgetActivity, z22);
                                                                }
                                                            });
                                                        }
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.showLastFragment();
                                                            launchActivity.rightActionBarLayout.showLastFragment();
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                        }
                                                    } else {
                                                        c2 = 6;
                                                        editWidgetActivity = c7 == 6 ? new EditWidgetActivity(i2, i3) : null;
                                                        z22 = false;
                                                        if (c7 == c2) {
                                                        }
                                                        if (AndroidUtilities.isTablet()) {
                                                        }
                                                    }
                                                    c2 = 6;
                                                    editWidgetActivity = baseFragment;
                                                    z22 = false;
                                                    if (c7 == c2) {
                                                    }
                                                    if (AndroidUtilities.isTablet()) {
                                                    }
                                                } else if (z8) {
                                                    ?? bundle7 = new Bundle();
                                                    bundle7.putBoolean("destroyAfterSelect", r3);
                                                    launchActivity.actionBarLayout.presentFragment(new ContactsActivity(bundle7), false, true, true, false);
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.showLastFragment();
                                                        launchActivity.rightActionBarLayout.showLastFragment();
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
                                                    contactsActivity.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                                                        @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                                        public final void didSelectContact(TLRPC$User tLRPC$User, String str93, ContactsActivity contactsActivity2) {
                                                            LaunchActivity.this.lambda$handleIntent$19(z4, iArr, tLRPC$User, str93, contactsActivity2);
                                                        }
                                                    });
                                                    ActionBarLayout actionBarLayout2 = launchActivity.actionBarLayout;
                                                    actionBarLayout2.presentFragment(contactsActivity, actionBarLayout2.getLastFragment() instanceof ContactsActivity, true, true, false);
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.showLastFragment();
                                                        launchActivity.rightActionBarLayout.showLastFragment();
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else if (z15) {
                                                    final ActionIntroActivity actionIntroActivity = new ActionIntroActivity(5);
                                                    actionIntroActivity.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                                                        @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                                        public final void didFindQRCode(String str93) {
                                                            LaunchActivity.this.lambda$handleIntent$23(actionIntroActivity, str93);
                                                        }
                                                    });
                                                    launchActivity.actionBarLayout.presentFragment(actionIntroActivity, false, true, true, false);
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.showLastFragment();
                                                        launchActivity.rightActionBarLayout.showLastFragment();
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else if (z13) {
                                                    NewContactActivity newContactActivity = new NewContactActivity();
                                                    String str93 = str4;
                                                    if (str93 != null) {
                                                        String[] split3 = str93.split(str, 2);
                                                        String str94 = split3[0];
                                                        if (split3.length > r3) {
                                                            char c8 = r3 == true ? 1 : 0;
                                                            char c9 = r3 == true ? 1 : 0;
                                                            char c10 = r3 == true ? 1 : 0;
                                                            char c11 = r3 == true ? 1 : 0;
                                                            str6 = split3[c8];
                                                        } else {
                                                            str6 = null;
                                                        }
                                                        newContactActivity.setInitialName(str94, str6);
                                                    }
                                                    String str95 = str5;
                                                    if (str95 != null) {
                                                        newContactActivity.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str95, r3), false);
                                                    }
                                                    launchActivity.actionBarLayout.presentFragment(newContactActivity, false, true, true, false);
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.actionBarLayout.showLastFragment();
                                                        launchActivity.rightActionBarLayout.showLastFragment();
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                    } else {
                                                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r3, false);
                                                    }
                                                } else {
                                                    final String str96 = str4;
                                                    String str97 = str5;
                                                    if (z7) {
                                                        z17 = true;
                                                        GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                        if (GroupCallActivity.groupCallInstance != null) {
                                                            GroupCallActivity.groupCallUiVisible = true;
                                                        }
                                                    } else {
                                                        z17 = true;
                                                        if (z14) {
                                                            final BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                                            if (lastFragment3 == null || lastFragment3.getParentActivity() == null) {
                                                                z21 = false;
                                                            } else {
                                                                final String phoneNumber = NewContactActivity.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str97, false);
                                                                lastFragment3.showDialog(new AlertDialog.Builder(lastFragment3.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", R.string.NewContactAlertMessage, PhoneFormat.getInstance().format(phoneNumber)))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                                                                    @Override // android.content.DialogInterface.OnClickListener
                                                                    public final void onClick(DialogInterface dialogInterface, int i30) {
                                                                        LaunchActivity.lambda$handleIntent$24(phoneNumber, str96, lastFragment3, dialogInterface, i30);
                                                                    }
                                                                }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
                                                                z21 = true;
                                                            }
                                                            z19 = z;
                                                        } else if (z10) {
                                                            launchActivity.actionBarLayout.presentFragment(new CallLogActivity(), false, true, true, false);
                                                            if (AndroidUtilities.isTablet()) {
                                                                launchActivity.actionBarLayout.showLastFragment();
                                                                launchActivity.rightActionBarLayout.showLastFragment();
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                            } else {
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                            }
                                                            z19 = z;
                                                            z21 = true;
                                                        }
                                                    }
                                                    z19 = z;
                                                    z20 = z18;
                                                    z21 = false;
                                                    r8 = z20;
                                                }
                                                z19 = z;
                                                z17 = true;
                                                z21 = true;
                                            } else {
                                                z17 = true;
                                                if (!AndroidUtilities.isTablet()) {
                                                    NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                }
                                                if (j4 == 0) {
                                                    launchActivity.openDialogsToSend(false);
                                                    z19 = z;
                                                    z21 = true;
                                                } else {
                                                    ArrayList<Long> arrayList6 = new ArrayList<>();
                                                    arrayList6.add(Long.valueOf(j4));
                                                    launchActivity.didSelectDialogs(null, arrayList6, null, false);
                                                    z19 = z;
                                                    z20 = z18;
                                                    z21 = false;
                                                    r8 = z20;
                                                }
                                            }
                                            if (!z21) {
                                                if (!AndroidUtilities.isTablet()) {
                                                }
                                                launchActivity.actionBarLayout.showLastFragment();
                                                if (AndroidUtilities.isTablet()) {
                                                }
                                            }
                                            if (z40) {
                                            }
                                            if (!z7) {
                                                groupCallActivity.dismiss();
                                            }
                                            intent7.setAction(r8);
                                            return z21;
                                        }
                                    }
                                } else if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                    z23 = false;
                                    launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                                } else {
                                    z23 = false;
                                }
                                z19 = z;
                                z24 = z23;
                            }
                            z17 = true;
                            z20 = z24;
                            z21 = false;
                            r8 = z20;
                            if (!z21) {
                            }
                            if (z40) {
                            }
                            if (!z7) {
                            }
                            intent7.setAction(r8);
                            return z21;
                        }
                    }
                    z19 = z;
                    r8 = 0;
                    z17 = true;
                    if (!z21) {
                    }
                    if (z40) {
                    }
                    if (!z7) {
                    }
                    intent7.setAction(r8);
                    return z21;
                }
                r3 = 1;
                if (j3 == 0) {
                }
                z19 = z;
                r8 = 0;
                z17 = true;
                if (!z21) {
                }
                if (z40) {
                }
                if (!z7) {
                }
                intent7.setAction(r8);
                return z21;
            }
            z16 = false;
            z17 = true;
            z18 = z16;
            z19 = z;
            z20 = z18;
            z21 = false;
            r8 = z20;
            if (!z21) {
            }
            if (z40) {
            }
            if (!z7) {
            }
            intent7.setAction(r8);
            return z21;
        }
        j = 0;
        str = " ";
        launchActivity = this;
        i = 0;
        iArr = iArr3;
        intent2 = intent;
        j2 = j;
        j3 = j2;
        j4 = j3;
        i2 = -1;
        z4 = false;
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
        c = 0;
        intent7 = intent6;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z16 = false;
        z17 = true;
        z18 = z16;
        z19 = z;
        z20 = z18;
        z21 = false;
        r8 = z20;
        if (!z21) {
        }
        if (z40) {
        }
        if (!z7) {
        }
        intent7.setAction(r8);
        return z21;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$11(String str) {
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
            this.actionBarLayout.fragmentsStack.get(0).presentFragment(new PremiumPreviewFragment(Uri.parse(str).getQueryParameter("ref")));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$12(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$14(final AlertDialog alertDialog, final String str, final Bundle bundle, final TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
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
            lambda$runLinkRequest$62(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
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
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
                LaunchActivity.lambda$handleIntent$15(iArr, dialogId, tLRPC$MessageMedia, i, z, i2);
            }
        });
        lambda$runLinkRequest$62(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$15(int[] iArr, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$17() {
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
            this.actionBarLayout.fragmentsStack.get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.lambda$handleIntent$22(AlertDialog.this, actionIntroActivity, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleIntent$22(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda24
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
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

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0079, code lost:
        if ((r0.get(r0.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0097, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0095, code lost:
        if ((r0.get(r0.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openDialogsToSend(boolean z) {
        boolean z2;
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
            public boolean shouldShowNextButton(DialogsActivity dialogsActivity2, ArrayList<Long> arrayList2, CharSequence charSequence, boolean z3) {
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
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                ArrayList<BaseFragment> arrayList2 = this.layersActionBarLayout.fragmentsStack;
            }
            z2 = false;
        } else {
            if (this.actionBarLayout.fragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList3 = this.actionBarLayout.fragmentsStack;
            }
            z2 = false;
        }
        this.actionBarLayout.presentFragment(dialogsActivity, z2, !z, true, false);
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
                this.actionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
                return;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    private int runCommentRequest(final int i, final AlertDialog alertDialog, final Integer num, final Integer num2, final Integer num3, final TLRPC$Chat tLRPC$Chat) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = (num2 != null ? num : num3).intValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runCommentRequest$26(i, num, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage, num2, num3, alertDialog, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runCommentRequest$26(final int i, final Integer num, final TLRPC$Chat tLRPC$Chat, final TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, final Integer num2, final Integer num3, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$25(tLObject, i, num, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage, num2, num3, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0094 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runCommentRequest$25(TLObject tLObject, int i, Integer num, TLRPC$Chat tLRPC$Chat, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Integer num2, Integer num3, AlertDialog alertDialog) {
        boolean z = false;
        try {
            if (tLObject instanceof TLRPC$TL_messages_discussionMessage) {
                TLRPC$TL_messages_discussionMessage tLRPC$TL_messages_discussionMessage = (TLRPC$TL_messages_discussionMessage) tLObject;
                MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_discussionMessage.users, false);
                MessagesController.getInstance(i).putChats(tLRPC$TL_messages_discussionMessage.chats, false);
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                int size = tLRPC$TL_messages_discussionMessage.messages.size();
                for (int i2 = 0; i2 < size; i2++) {
                    arrayList.add(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_messages_discussionMessage.messages.get(i2), true, true));
                }
                if (!arrayList.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -arrayList.get(0).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, tLRPC$TL_messages_discussionMessage.read_inbox_max_id, tLRPC$TL_messages_discussionMessage.read_outbox_max_id);
                    if (num2 != null) {
                        chatActivity.setHighlightMessageId(num2.intValue());
                    } else if (num3 != null) {
                        chatActivity.setHighlightMessageId(num.intValue());
                    }
                    lambda$runLinkRequest$62(chatActivity);
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
                    alertDialog.dismiss();
                    return;
                }
            }
            alertDialog.dismiss();
            return;
        } catch (Exception e2) {
            FileLog.e(e2);
            return;
        }
        if (!z) {
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
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runImportRequest$28(uri, i, alertDialog, tLObject, tLRPC$TL_error);
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    LaunchActivity.lambda$runImportRequest$29(i, iArr, r3, dialogInterface);
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
    public /* synthetic */ void lambda$runImportRequest$28(final Uri uri, final int i, final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runImportRequest$27(tLObject, uri, i, alertDialog);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x011b, code lost:
        if ((r10.get(r10.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0139, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0137, code lost:
        if ((r10.get(r10.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runImportRequest$27(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
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
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                } else {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                }
                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                dialogsActivity.setDelegate(this);
                if (AndroidUtilities.isTablet()) {
                    if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                        ArrayList<BaseFragment> arrayList = this.layersActionBarLayout.fragmentsStack;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, false, true, false);
                } else {
                    if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        ArrayList<BaseFragment> arrayList2 = this.actionBarLayout.fragmentsStack;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, false, true, false);
                }
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
    public static /* synthetic */ void lambda$runImportRequest$29(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x04b5  */
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
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i5) {
                    LaunchActivity.this.lambda$runLinkRequest$30(i, str, str2, str3, str4, str5, str6, str7, str8, str9, z, num, l, num2, num3, str10, hashMap, str11, str12, str13, str14, tLRPC$TL_wallPaper, str15, str16, str17, str18, i3, str19, str20, str21, i5);
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
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LaunchActivity.this.lambda$runLinkRequest$33(i, str15, alertDialog2, tLObject, tLRPC$TL_error);
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
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$47(str10, str17, str18, i, str19, str20, str21, num, num3, num2, iArr2, alertDialog2, str6, str7, str8, str5, i3, str, tLObject, tLRPC$TL_error);
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
                                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                                    public final void didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z3) {
                                        LaunchActivity.this.lambda$runLinkRequest$53(z, i4, str9, dialogsActivity2, arrayList4, charSequence, z3);
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
                                iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$57(iArr, i, alertDialog, tLRPC$TL_account_getAuthorizationForm, str23, str24, str25, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str12 != null) {
                                TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                                tLRPC$TL_help_getDeepLinkInfo.path = str12;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$59(alertDialog, tLObject, tLRPC$TL_error);
                                    }
                                });
                            } else if (str11 != null) {
                                TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                                tLRPC$TL_langpack_getLanguage.lang_code = str11;
                                tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$61(alertDialog, tLObject, tLRPC$TL_error);
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
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$runLinkRequest$62(themePreviewActivity);
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
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                LaunchActivity.this.lambda$runLinkRequest$64(alertDialog, tLRPC$TL_wallPaper, tLObject, tLRPC$TL_error);
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
                                        LaunchActivity.this.lambda$runLinkRequest$65();
                                    }
                                };
                                TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                                tLRPC$TL_account_getTheme.format = "android";
                                TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                                tLRPC$TL_inputThemeSlug.slug = str16;
                                tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        LaunchActivity.this.lambda$runLinkRequest$67(alertDialog, tLObject, tLRPC$TL_error);
                                    }
                                });
                                if (iArr[c] != 0) {
                                    return;
                                }
                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda2
                                    @Override // android.content.DialogInterface.OnCancelListener
                                    public final void onCancel(DialogInterface dialogInterface) {
                                        LaunchActivity.lambda$runLinkRequest$73(i4, iArr, runnable2, dialogInterface);
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
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                LaunchActivity.this.lambda$runLinkRequest$69(iArr, i, alertDialog, num, num3, num2, tLObject, tLRPC$TL_error);
                                            }
                                        });
                                    }
                                } else {
                                    final Bundle bundle2 = new Bundle();
                                    bundle2.putLong("chat_id", l.longValue());
                                    bundle2.putInt("message_id", num.intValue());
                                    if (!mainFragmentsStack.isEmpty()) {
                                        ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                        baseFragment = arrayList4.get(arrayList4.size() - 1);
                                    } else {
                                        baseFragment = null;
                                    }
                                    if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle2, baseFragment)) {
                                        final BaseFragment baseFragment4 = baseFragment;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                LaunchActivity.this.lambda$runLinkRequest$72(bundle2, l, iArr, alertDialog, baseFragment4, i);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } else if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        i4 = i;
                        alertDialog = alertDialog2;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$50(i4, alertDialog, str2, tLObject, tLRPC$TL_error);
                            }
                        }, 2);
                    } else {
                        i4 = i;
                        alertDialog = alertDialog2;
                        if (i2 == 1) {
                            TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                            tLRPC$TL_messages_importChatInvite.hash = str2;
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$52(i4, alertDialog, tLObject, tLRPC$TL_error);
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
    public /* synthetic */ void lambda$runLinkRequest$30(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, boolean z, Integer num, Long l, Integer num2, Integer num3, String str10, HashMap hashMap, String str11, String str12, String str13, String str14, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str15, String str16, String str17, String str18, int i2, String str19, String str20, String str21, int i3) {
        if (i3 != i) {
            switchToAccount(i3, true);
        }
        runLinkRequest(i3, str, str2, str3, str4, str5, str6, str7, str8, str9, z, num, l, num2, num3, str10, hashMap, str11, str12, str13, str14, tLRPC$TL_wallPaper, str15, str16, str17, str18, 1, i2, str19, str20, str21);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$33(final int i, final String str, final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$32(tLRPC$TL_error, tLObject, i, str, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$32(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, String str, AlertDialog alertDialog) {
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
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$31(runnable, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$62(paymentFormActivity);
            }
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$31(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$47(final String str, final String str2, final String str3, final int i, final String str4, final String str5, final String str6, final Integer num, final Integer num2, final Integer num3, final int[] iArr, final AlertDialog alertDialog, final String str7, final String str8, final String str9, final String str10, final int i2, final String str11, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$46(tLObject, tLRPC$TL_error, str, str2, str3, i, str4, str5, str6, num, num2, num3, iArr, alertDialog, str7, str8, str9, str10, i2, str11);
            }
        }, 2L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0117, code lost:
        if (r29[0] != 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x017a, code lost:
        if ((r1.get(r1.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x017c, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0199, code lost:
        if ((r1.get(r1.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L47;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:178:0x0427 -> B:175:0x042a). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$46(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, final String str, String str2, String str3, final int i, final String str4, String str5, final String str6, Integer num, Integer num2, Integer num3, int[] iArr, AlertDialog alertDialog, String str7, String str8, final String str9, String str10, int i2, String str11) {
        String str12;
        long j;
        boolean z;
        BaseFragment baseFragment;
        boolean z2;
        if (!isFinishing()) {
            final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
            boolean z3 = true;
            boolean z4 = false;
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
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                                    LaunchActivity.this.lambda$runLinkRequest$39(i, str6, user, str4, tLRPC$TL_contacts_resolvedPeer, tLObject2, tLRPC$TL_error2);
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
                } else if (str != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putBoolean("cantSendToChannels", true);
                    bundle.putInt("dialogsType", 1);
                    bundle.putString("selectAlertString", LocaleController.getString("SendGameToText", R.string.SendGameToText));
                    bundle.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", R.string.SendGameToGroupText));
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final void didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z5) {
                            LaunchActivity.this.lambda$runLinkRequest$40(str, i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity2, arrayList3, charSequence, z5);
                        }
                    });
                    if (AndroidUtilities.isTablet()) {
                        if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                            ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                        }
                        z2 = false;
                    } else {
                        if (this.actionBarLayout.fragmentsStack.size() > 1) {
                            ArrayList<BaseFragment> arrayList4 = this.actionBarLayout.fragmentsStack;
                        }
                        z2 = false;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, true, true, false);
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
                        this.actionBarLayout.showLastFragment();
                        this.rightActionBarLayout.showLastFragment();
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
                            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                            BulletinFactory.of(arrayList5.get(arrayList5.size() - 1)).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups)).show();
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
                    if (str8 != null) {
                        z4 = true;
                    }
                    bundle2.putBoolean("allowChannels", z4);
                    String str13 = TextUtils.isEmpty(str7) ? TextUtils.isEmpty(str8) ? null : str8 : str7;
                    final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle2);
                    final TLRPC$User tLRPC$User2 = tLRPC$User;
                    final String str14 = str13;
                    dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final void didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList6, CharSequence charSequence, boolean z5) {
                            LaunchActivity.this.lambda$runLinkRequest$45(i, tLRPC$User2, str9, str14, dialogsActivity2, dialogsActivity3, arrayList6, charSequence, z5);
                        }
                    });
                    lambda$runLinkRequest$62(dialogsActivity2);
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
                        baseFragment = null;
                    } else {
                        ArrayList<BaseFragment> arrayList6 = mainFragmentsStack;
                        baseFragment = arrayList6.get(arrayList6.size() - 1);
                    }
                    if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle3, baseFragment)) {
                        if (z && (baseFragment instanceof ChatActivity)) {
                            ChatActivity chatActivity = (ChatActivity) baseFragment;
                            if (chatActivity.getDialogId() == j) {
                                chatActivity.setBotUser(str10);
                            }
                        }
                        MessagesController.getInstance(i).ensureMessagesLoaded(j, num == null ? 0 : num.intValue(), new 16(alertDialog, str3, baseFragment, j, bundle3));
                        z3 = false;
                    }
                }
            } else {
                try {
                    if (!mainFragmentsStack.isEmpty()) {
                        ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
                        BaseFragment baseFragment2 = arrayList7.get(arrayList7.size() - 1);
                        if (tLRPC$TL_error != null && (str12 = tLRPC$TL_error.text) != null && str12.startsWith("FLOOD_WAIT")) {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("FloodWait", R.string.FloodWait)).show();
                        } else if (AndroidUtilities.isNumeric(str11)) {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("NoPhoneFound", R.string.NoPhoneFound)).show();
                        } else {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound)).show();
                        }
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            if (!z3) {
                return;
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e3) {
                FileLog.e(e3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$39(final int i, final String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$38(tLObject, i, str, tLRPC$User, str2, tLRPC$TL_contacts_resolvedPeer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$38(TLObject tLObject, final int i, String str, final TLRPC$User tLRPC$User, final String str2, final TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer) {
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
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final void didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList3, CharSequence charSequence, boolean z) {
                        LaunchActivity.this.lambda$runLinkRequest$34(tLRPC$User, str2, i, dialogsActivity3, arrayList3, charSequence, z);
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
                        LaunchActivity.this.lambda$runLinkRequest$37(i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity3, baseFragment, tLRPC$User, str2, dialogInterface, i2);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
                return;
            } else if (dialogsActivity != null) {
                lambda$runLinkRequest$62(dialogsActivity);
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
    public /* synthetic */ void lambda$runLinkRequest$34(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        bundle.putString("attach_bot", tLRPC$User.username);
        if (str != null) {
            bundle.putString("attach_bot_start_command", str);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$37(final int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LaunchActivity.this.lambda$runLinkRequest$36(i, dialogsActivity, baseFragment, tLRPC$User, str, tLObject, tLRPC$TL_error);
            }
        }, 66);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$36(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC$User tLRPC$User, final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$35(tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$35(TLObject tLObject, int i, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
            if (dialogsActivity != null) {
                lambda$runLinkRequest$62(dialogsActivity);
            } else if (!(baseFragment instanceof ChatActivity)) {
            } else {
                ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$40(String str, int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        TLRPC$TL_inputMediaGame tLRPC$TL_inputMediaGame = new TLRPC$TL_inputMediaGame();
        TLRPC$TL_inputGameShortName tLRPC$TL_inputGameShortName = new TLRPC$TL_inputGameShortName();
        tLRPC$TL_inputMediaGame.id = tLRPC$TL_inputGameShortName;
        tLRPC$TL_inputGameShortName.short_name = str;
        tLRPC$TL_inputGameShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.users.get(0));
        SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer(longValue), tLRPC$TL_inputMediaGame, 0L, 0L);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$45(final int i, final TLRPC$User tLRPC$User, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        final long longValue = ((Long) arrayList.get(0)).longValue();
        final TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(chat, tLRPC$User, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z2, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$43(str, str2, i, chat, dialogsActivity, tLRPC$User, longValue, z2, tLRPC$TL_chatAdminRights2, str3);
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
                LaunchActivity.this.lambda$runLinkRequest$44(longValue, i, tLRPC$User, str2, dialogInterface, i3);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$43(final String str, final String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, final TLRPC$User tLRPC$User, final long j, final boolean z, final TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$42(str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$42(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, final int i, final TLRPC$Chat tLRPC$Chat, final DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
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
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$41(i, tLRPC$Chat, dialogsActivity);
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
    public /* synthetic */ void lambda$runLinkRequest$41(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
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
    public /* synthetic */ void lambda$runLinkRequest$44(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        long j2 = -j;
        bundle.putLong("chat_id", j2);
        ChatActivity chatActivity = new ChatActivity(bundle);
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(j2, tLRPC$User, 0, str, chatActivity, null);
        this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 16 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ BaseFragment val$lastFragment;
        final /* synthetic */ String val$livestream;
        final /* synthetic */ AlertDialog val$progressDialog;

        16(AlertDialog alertDialog, String str, BaseFragment baseFragment, long j, Bundle bundle) {
            this.val$progressDialog = alertDialog;
            this.val$livestream = str;
            this.val$lastFragment = baseFragment;
            this.val$dialog_id = j;
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
                chatActivity = new ChatActivity(this.val$args);
                LaunchActivity.this.actionBarLayout.presentFragment(chatActivity);
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
    public /* synthetic */ void lambda$runLinkRequest$50(final int i, final AlertDialog alertDialog, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$49(tLRPC$TL_error, tLObject, i, alertDialog, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0031, code lost:
        if (r10.chat.has_geo != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0079, code lost:
        if (r11.checkCanOpenChat(r7, r14.get(r14.size() - 1)) != false) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$49(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, final AlertDialog alertDialog, String str) {
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
                            if (TextUtils.isEmpty(tLRPC$Chat2.username)) {
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
                            LaunchActivity.lambda$runLinkRequest$48(zArr, dialogInterface);
                        }
                    });
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
    public static /* synthetic */ void lambda$runLinkRequest$48(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$52(final int i, final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$51(alertDialog, tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$51(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
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
    public /* synthetic */ void lambda$runLinkRequest$53(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putBoolean("hasUrl", z);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            MediaDataController.getInstance(i).saveDraft(longValue, 0, str, null, null, false);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$57(int[] iArr, final int i, final AlertDialog alertDialog, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$55(alertDialog, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3, tLObject2, tLRPC$TL_error2);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$56(alertDialog, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$55(final AlertDialog alertDialog, final int i, final TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, final TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$54(alertDialog, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$54(AlertDialog alertDialog, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$62(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$account_Password) tLObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$56(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$59(final AlertDialog alertDialog, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$58(alertDialog, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$58(AlertDialog alertDialog, TLObject tLObject) {
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
    public /* synthetic */ void lambda$runLinkRequest$61(final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$60(alertDialog, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$60(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$64(final AlertDialog alertDialog, final TLRPC$TL_wallPaper tLRPC$TL_wallPaper, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$63(alertDialog, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$63(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
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
            lambda$runLinkRequest$62(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$65() {
        this.loadingThemeFileName = null;
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeProgressDialog = null;
        this.loadingTheme = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$67(final AlertDialog alertDialog, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$66(tLObject, alertDialog, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$66(TLObject tLObject, AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
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
    public /* synthetic */ void lambda$runLinkRequest$69(final int[] iArr, final int i, final AlertDialog alertDialog, final Integer num, final Integer num2, final Integer num3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$68(tLObject, iArr, i, alertDialog, num, num2, num3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$68(TLObject tLObject, int[] iArr, int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3) {
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
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", R.string.LinkNotFound)));
                return;
            }
        }
        z = true;
        if (z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$72(final Bundle bundle, Long l, int[] iArr, final AlertDialog alertDialog, final BaseFragment baseFragment, final int i) {
        if (!this.actionBarLayout.presentFragment(new ChatActivity(bundle))) {
            TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
            TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
            tLRPC$TL_inputChannel.channel_id = l.longValue();
            tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$runLinkRequest$71(alertDialog, baseFragment, i, bundle, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$71(final AlertDialog alertDialog, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$70(alertDialog, tLObject, baseFragment, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLinkRequest$70(AlertDialog alertDialog, TLObject tLObject, BaseFragment baseFragment, int i, Bundle bundle) {
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
                tLRPC$TL_messages_chats.chats.get(0);
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    this.actionBarLayout.presentFragment(new ChatActivity(bundle));
                }
                z = false;
            }
        }
        if (z) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", R.string.LinkNotFound)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$runLinkRequest$73(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<TLRPC$TL_contact> findContacts(String str, String str2, boolean z) {
        String str3;
        String lowerCase;
        TLRPC$User user;
        String str4;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
        ArrayList arrayList = new ArrayList(contactsController.contacts);
        ArrayList arrayList2 = new ArrayList();
        String str5 = null;
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
                                        strArr2[c] = str5;
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
                                        String str6 = strArr[i4];
                                        if (str6 != null) {
                                            for (int i5 = 3; i < i5; i5 = 3) {
                                                String str7 = strArr2[i];
                                                if (str7 != null) {
                                                    if (!str7.startsWith(str6)) {
                                                        if (str7.contains(" " + str6)) {
                                                        }
                                                    }
                                                    z2 = true;
                                                    break;
                                                }
                                                i++;
                                            }
                                            if (!z2 && (str4 = user.username) != null && str4.startsWith(str6)) {
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
                                str5 = null;
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
        this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.lambda$createUpdateUI$74(view);
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
    public /* synthetic */ void lambda$createUpdateUI$74(View view) {
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
                    this.updateLayout.animate().translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(null).setDuration(180L).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda23
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.lambda$updateAppUpdateViews$75(frameLayout);
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
    public static /* synthetic */ void lambda$updateAppUpdateViews$75(View view) {
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
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LaunchActivity.this.lambda$checkAppUpdate$77(i, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$77(final int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            final TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$76(tLRPC$TL_help_appUpdate, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppUpdate$76(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i) {
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
                    LaunchActivity.this.lambda$showAlertDialog$78(dialogInterface);
                }
            });
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAlertDialog$78(DialogInterface dialogInterface) {
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
    public void didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<Long> arrayList, final CharSequence charSequence, final boolean z) {
        ChatActivity chatActivity;
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
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).longValue(), this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    LaunchActivity.this.lambda$didSelectDialogs$79(currentAccount, dialogsActivity, z, arrayList3, uri, alertDialog, j);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            final boolean z6 = dialogsActivity == null || dialogsActivity.notify;
            if (arrayList.size() <= 1) {
                long longValue = arrayList.get(0).longValue();
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(longValue)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
                } else if (DialogObject.isUserDialog(longValue)) {
                    bundle.putLong("user_id", longValue);
                } else {
                    bundle.putLong("chat_id", -longValue);
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
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, arrayList.get(i2).longValue(), size > 1)) {
                    return;
                }
            }
            ArrayList<TLRPC$User> arrayList8 = this.contactsToSend;
            if (arrayList8 != null && arrayList8.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(arrayList9.get(arrayList9.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                final ChatActivity chatActivity3 = chatActivity;
                phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z7, int i3) {
                        LaunchActivity.this.lambda$didSelectDialogs$80(chatActivity3, arrayList, currentAccount, charSequence, z6, tLRPC$User, z7, i3);
                    }
                });
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                arrayList10.get(arrayList10.size() - 1).showDialog(phonebookShareAlert);
            } else {
                String str = null;
                int i3 = 0;
                while (i3 < arrayList.size()) {
                    long longValue2 = arrayList.get(i3).longValue();
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (chatActivity != null) {
                        i = 1024;
                        chatActivity2 = chatActivity;
                        this.actionBarLayout.presentFragment(chatActivity, dialogsActivity != null, dialogsActivity == null || this.videoPath != null || ((arrayList2 = this.photoPathsArray) != null && arrayList2.size() > 0), true, false);
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
                        chatActivity2 = chatActivity;
                        i = 1024;
                        if (this.videoPath != null) {
                            String str3 = this.sendingText;
                            if (str3 != null && str3.length() <= 1024) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                            ArrayList arrayList12 = new ArrayList();
                            arrayList12.add(this.videoPath);
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList12, arrayList12, null, str, null, longValue2, null, null, null, null, z6, 0);
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
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, longValue2, null, null, null, false, false, null, z6, 0, false);
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
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, longValue2, null, null, null, null, z6, 0);
                    }
                    String str6 = this.sendingText;
                    if (str6 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str6, longValue2, true, 0);
                    }
                    ArrayList<TLRPC$User> arrayList15 = this.contactsToSend;
                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                        for (int i4 = 0; i4 < this.contactsToSend.size(); i4++) {
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(this.contactsToSend.get(i4), longValue2, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z6, 0);
                        }
                    }
                    if (!TextUtils.isEmpty(charSequence) && !z2 && !z3) {
                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), longValue2, z6, 0);
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
    public /* synthetic */ void lambda$didSelectDialogs$79(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
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
    public /* synthetic */ void lambda$didSelectDialogs$80(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2) {
        if (chatActivity != null) {
            this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long longValue = ((Long) arrayList.get(i3)).longValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$User, longValue, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2);
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), longValue, z, 0);
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
    public void lambda$runLinkRequest$62(BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return this.actionBarLayout.presentFragment(baseFragment, z, z2, true, false);
    }

    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public ActionBarLayout getRightActionBarLayout() {
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$onActivityResult$81();
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
            if (this.actionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
                arrayList.get(arrayList.size() - 1).onActivityResultFragment(i, i2, intent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                    ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                    arrayList2.get(arrayList2.size() - 1).onActivityResultFragment(i, i2, intent);
                }
                if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                    ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                    arrayList3.get(arrayList3.size() - 1).onActivityResultFragment(i, i2, intent);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onActivityResultReceived, Integer.valueOf(i), Integer.valueOf(i2), intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResult$81() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (!checkPermissionsResult(i, strArr, iArr)) {
            return;
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
            arrayList.get(arrayList.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                arrayList2.get(arrayList2.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                arrayList3.get(arrayList3.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
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
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 4096);
        ApplicationLoader.mainInterfacePaused = true;
        final int i = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$82(i);
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
    public static /* synthetic */ void lambda$onPause$82(int i) {
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
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
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
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
        ApplicationLoader.mainInterfacePaused = false;
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda65.INSTANCE);
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView == null || passcodeView.getVisibility() != 0) {
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
    public static /* synthetic */ void lambda$onResume$83() {
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
        if (wasTablet.booleanValue() == AndroidUtilities.isTablet()) {
            return;
        }
        long j2 = 0;
        if (wasTablet.booleanValue()) {
            mainFragmentsStack.addAll(rightFragmentsStack);
            mainFragmentsStack.addAll(layerFragmentsStack);
            rightFragmentsStack.clear();
            layerFragmentsStack.clear();
        } else {
            if (rightFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = new ArrayList(mainFragmentsStack);
                mainFragmentsStack.clear();
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
                j = 0;
                for (BaseFragment baseFragment : arrayList) {
                    if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList()) {
                        mainFragmentsStack.add(baseFragment);
                    } else {
                        if (baseFragment instanceof ChatActivity) {
                            ChatActivity chatActivity = (ChatActivity) baseFragment;
                            if (!chatActivity.isInScheduleMode()) {
                                rightFragmentsStack.add(baseFragment);
                                if (j == 0) {
                                    j = chatActivity.getDialogId();
                                }
                            }
                        }
                        layerFragmentsStack.add(baseFragment);
                    }
                }
            } else {
                Iterator<BaseFragment> it = rightFragmentsStack.iterator();
                j = 0;
                while (it.hasNext()) {
                    BaseFragment next = it.next();
                    if (next instanceof ChatActivity) {
                        ChatActivity chatActivity2 = (ChatActivity) next;
                        if (!chatActivity2.isInScheduleMode() && j == 0) {
                            j = chatActivity2.getDialogId();
                        }
                    }
                }
            }
            j2 = j;
        }
        setupActionBarLayout();
        this.actionBarLayout.showLastFragment();
        if (!AndroidUtilities.isTablet()) {
            return;
        }
        this.rightActionBarLayout.showLastFragment();
        this.layersActionBarLayout.showLastFragment();
        Iterator<BaseFragment> it2 = mainFragmentsStack.iterator();
        while (it2.hasNext()) {
            BaseFragment next2 = it2.next();
            if (next2 instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) next2;
                if (dialogsActivity.isMainDialogList()) {
                    dialogsActivity.setOpenedDialogId(j2);
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

    /* JADX WARN: Code restructure failed: missing block: B:121:0x0277, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L122;
     */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x05b6  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x05d2  */
    /* JADX WARN: Removed duplicated region for block: B:230:? A[RETURN, SYNTHETIC] */
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
        } else {
            String[] strArr = null;
            Runnable runnable = null;
            strArr = null;
            if (i == NotificationCenter.needShowAlert) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                if (num.intValue() != 2 && num.intValue() != 3) {
                    builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            LaunchActivity.lambda$didReceivedNotification$84(i2, dialogInterface, i5);
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
                    if (((String) objArr[2]).startsWith("AUTH_KEY_DROP_")) {
                        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                LaunchActivity.this.lambda$didReceivedNotification$85(dialogInterface, i5);
                            }
                        });
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
                ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                arrayList.get(arrayList.size() - 1).showDialog(builder.create());
            } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
                final HashMap hashMap = (HashMap) objArr[0];
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        LaunchActivity.this.lambda$didReceivedNotification$87(hashMap, i2, dialogInterface, i5);
                    }
                });
                builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", R.string.ShareYouLocationUnable));
                if (mainFragmentsStack.isEmpty()) {
                    return;
                }
                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                arrayList2.get(arrayList2.size() - 1).showDialog(builder2.create());
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
                    ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                    if (arrayList3.get(arrayList3.size() - 1) instanceof ProfileActivity) {
                        z2 = true;
                        if (z2) {
                            ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
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
                ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                articleViewer.setParentActivity(this, arrayList5.get(arrayList5.size() - 1));
                ArticleViewer.getInstance().open((TLRPC$TL_webPage) objArr[0], (String) objArr[1]);
            } else if (i == NotificationCenter.hasNewContactsToImport) {
                ActionBarLayout actionBarLayout = this.actionBarLayout;
                if (actionBarLayout == null || actionBarLayout.fragmentsStack.isEmpty()) {
                    return;
                }
                ((Integer) objArr[0]).intValue();
                final HashMap hashMap2 = (HashMap) objArr[1];
                final boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                final boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
                ArrayList<BaseFragment> arrayList6 = this.actionBarLayout.fragmentsStack;
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor("dialogTopBackground"));
                builder3.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
                builder3.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
                builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        LaunchActivity.lambda$didReceivedNotification$88(i2, hashMap2, booleanValue, booleanValue2, dialogInterface, i5);
                    }
                });
                builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        LaunchActivity.lambda$didReceivedNotification$89(i2, hashMap2, booleanValue, booleanValue2, dialogInterface, i5);
                    }
                });
                builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        LaunchActivity.lambda$didReceivedNotification$90(i2, hashMap2, booleanValue, booleanValue2, dialogInterface, i5);
                    }
                });
                AlertDialog create = builder3.create();
                arrayList6.get(arrayList6.size() - 1).showDialog(create);
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
                                    LaunchActivity.this.lambda$didReceivedNotification$91(valueAnimator);
                                }
                            });
                            ofFloat.setDuration(createCircularReveal.getDuration());
                            ofFloat.start();
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$didReceivedNotification$92();
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
                    if (objArr.length > 7) {
                        runnable = (Runnable) objArr[7];
                    }
                    this.actionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z, runnable);
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
                if (objArr.length > 7) {
                }
                this.actionBarLayout.animateThemedValues(themeInfo2, intValue2, booleanValue52, z, runnable);
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
                String str3 = (String) objArr[0];
                if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str3)) {
                    updateAppUpdateViews(true);
                }
                String str4 = this.loadingThemeFileName;
                if (str4 != null) {
                    if (!str4.equals(str3)) {
                        return;
                    }
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
                            ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    LaunchActivity.this.lambda$didReceivedNotification$94(fillThemeValues, tLObject, tLRPC$TL_error);
                                }
                            });
                            return;
                        }
                        TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                        if (applyThemeFile != null) {
                            lambda$runLinkRequest$62(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
                        }
                    }
                    onThemeLoadFinish();
                    return;
                }
                String str5 = this.loadingThemeWallpaperName;
                if (str5 == null || !str5.equals(str3)) {
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
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.lambda$didReceivedNotification$96(themeInfo3, file2);
                    }
                });
            } else if (i == NotificationCenter.fileLoadFailed) {
                String str6 = (String) objArr[0];
                if (str6.equals(this.loadingThemeFileName) || str6.equals(this.loadingThemeWallpaperName)) {
                    onThemeLoadFinish();
                }
                if (!SharedConfig.isAppUpdateAvailable() || !FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str6)) {
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
                if (intValue3 == 0) {
                    TLRPC$Document tLRPC$Document = (TLRPC$Document) objArr[1];
                    int intValue4 = ((Integer) objArr[2]).intValue();
                    StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, tLRPC$Document, null);
                    int i8 = (intValue4 == 6 || intValue4 == 7) ? 3500 : 1500;
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
            } else if (i == NotificationCenter.groupCallUpdated) {
                checkWasMutedByAdmin(false);
            } else if (i == NotificationCenter.fileLoadProgressChanged) {
                if (this.updateTextView == null || !SharedConfig.isAppUpdateAvailable()) {
                    return;
                }
                String str7 = (String) objArr[0];
                String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
                if (attachFileName == null || !attachFileName.equals(str7)) {
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
                BaseFragment baseFragment3 = arrayList10.get(arrayList10.size() - 1);
                if (baseFragment3.getParentActivity() == null) {
                    return;
                }
                baseFragment3.showDialog(new LimitReachedBottomSheet(baseFragment3, baseFragment3.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount));
            } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
                DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
                if (drawerLayoutAdapter == null) {
                    return;
                }
                drawerLayoutAdapter.notifyDataSetChanged();
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
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$84(int i, DialogInterface dialogInterface, int i2) {
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController messagesController = MessagesController.getInstance(i);
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$85(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$87(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (!AndroidUtilities.isMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            return;
        }
        LocationActivity locationActivity = new LocationActivity(0);
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda104
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i3, boolean z, int i4) {
                LaunchActivity.lambda$didReceivedNotification$86(hashMap, i, tLRPC$MessageMedia, i3, z, i4);
            }
        });
        lambda$runLinkRequest$62(locationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$86(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$88(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$89(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$didReceivedNotification$90(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$91(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$92() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$94(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$93(tLObject, themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$93(TLObject tLObject, Theme.ThemeInfo themeInfo) {
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
    public /* synthetic */ void lambda$didReceivedNotification$96(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$95();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$95() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$62(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
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
        ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
        BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
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
        lambda$runLinkRequest$62(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
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

    private void checkFreeDiscSpace() {
        SharedConfig.checkKeepMedia();
        SharedConfig.checkLogsToDelete();
        if (Build.VERSION.SDK_INT >= 26) {
            return;
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$checkFreeDiscSpace$98();
            }
        }, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$98() {
        File directory;
        long blockSizeLong;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) < 259200000 || (directory = FileLoader.getDirectory(4)) == null) {
                return;
            }
            StatFs statFs = new StatFs(directory.getAbsolutePath());
            if (Build.VERSION.SDK_INT < 18) {
                blockSizeLong = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
            } else {
                blockSizeLong = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
            }
            if (blockSizeLong >= 104857600) {
                return;
            }
            globalMainSettings.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$97();
                }
            });
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFreeDiscSpace$97() {
        try {
            AlertsCreator.createFreeSpaceDialog(this).show();
        } catch (Throwable unused) {
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
                    languageCellArr[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LaunchActivity.lambda$showLanguageAlertInternal$99(localeInfoArr, languageCellArr, view);
                        }
                    });
                    i++;
                }
                LanguageCell languageCell = new LanguageCell(this);
                HashMap<String, String> hashMap2 = this.systemLocaleStrings;
                int i3 = R.string.ChooseYourLanguageOther;
                languageCell.setValue(getStringForLanguageAlert(hashMap2, "ChooseYourLanguageOther", i3), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i3));
                languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                languageCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$100(view);
                    }
                });
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$101(localeInfoArr, dialogInterface, i4);
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
            languageCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$100(view);
                }
            });
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$101(localeInfoArr3, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showLanguageAlertInternal$99(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$100(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$62(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlertInternal$101(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
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
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$showLanguageAlert$103(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                        }
                    }, 8);
                    TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                    tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                    tLRPC$TL_langpack_getStrings2.keys.add("English");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LaunchActivity.this.lambda$showLanguageAlert$105(localeInfoArr, str2, tLObject, tLRPC$TL_error);
                        }
                    }, 8);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$103(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$102(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$102(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$105(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$104(hashMap, localeInfoArr, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLanguageAlert$104(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
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
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$updateCurrentConnectionState$106();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentConnectionState$106() {
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
        lambda$runLinkRequest$62(new ProxyListActivity());
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
                ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                if (actionBarLayout != null && !actionBarLayout.fragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList = this.layersActionBarLayout.fragmentsStack;
                    baseFragment = arrayList.get(arrayList.size() - 1);
                } else {
                    ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
                    if (actionBarLayout2 != null && !actionBarLayout2.fragmentsStack.isEmpty()) {
                        ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                        baseFragment = arrayList2.get(arrayList2.size() - 1);
                    } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                        ArrayList<BaseFragment> arrayList3 = this.actionBarLayout.fragmentsStack;
                        baseFragment = arrayList3.get(arrayList3.size() - 1);
                    }
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList4 = this.actionBarLayout.fragmentsStack;
                baseFragment = arrayList4.get(arrayList4.size() - 1);
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
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
            } else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = this.rightActionBarLayout.fragmentsStack;
                BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
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
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.onLowMemory();
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

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
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
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(i, keyEvent);
                } else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.rightActionBarLayout.onKeyUp(i, keyEvent);
                } else {
                    this.actionBarLayout.onKeyUp(i, keyEvent);
                }
            } else if (this.actionBarLayout.fragmentsStack.size() == 1) {
                if (!this.drawerLayoutContainer.isDrawerOpened()) {
                    if (getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(getCurrentFocus());
                    }
                    this.drawerLayoutContainer.openDrawer(false);
                } else {
                    this.drawerLayoutContainer.closeDrawer(false);
                }
            } else {
                this.actionBarLayout.onKeyUp(i, keyEvent);
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, ActionBarLayout actionBarLayout) {
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        ActionBarLayout actionBarLayout5;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z3 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer(!z3 && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && actionBarLayout != (actionBarLayout5 = this.actionBarLayout)) {
                actionBarLayout5.removeAllFragments();
                this.actionBarLayout.presentFragment(baseFragment, z, z2, false, false);
                this.layersActionBarLayout.removeAllFragments();
                this.layersActionBarLayout.setVisibility(8);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
                return false;
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z4 = this.tabletFullSize;
                if ((!z4 && actionBarLayout == this.rightActionBarLayout) || (z4 && actionBarLayout == this.actionBarLayout)) {
                    boolean z5 = (z4 && actionBarLayout == (actionBarLayout4 = this.actionBarLayout) && actionBarLayout4.fragmentsStack.size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z5) {
                        this.actionBarLayout.presentFragment(baseFragment, false, z2, false, false);
                    }
                    return z5;
                } else if (!z4 && actionBarLayout != (actionBarLayout3 = this.rightActionBarLayout)) {
                    actionBarLayout3.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(baseFragment, z, true, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                            actionBarLayout7.removeFragmentFromStack(actionBarLayout7.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else if (z4 && actionBarLayout != (actionBarLayout2 = this.actionBarLayout)) {
                    actionBarLayout2.presentFragment(baseFragment, actionBarLayout2.fragmentsStack.size() > 1, z2, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout8 = this.layersActionBarLayout;
                            actionBarLayout8.removeFragmentFromStack(actionBarLayout8.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout9 = this.layersActionBarLayout;
                            actionBarLayout9.removeFragmentFromStack(actionBarLayout9.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    ActionBarLayout actionBarLayout10 = this.actionBarLayout;
                    actionBarLayout10.presentFragment(baseFragment, actionBarLayout10.fragmentsStack.size() > 1, z2, false, false);
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout11 = this.layersActionBarLayout;
                if (actionBarLayout != actionBarLayout11) {
                    actionBarLayout11.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (z3) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.presentFragment(baseFragment, z, z2, false, false);
                    return false;
                }
            }
        } else {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout) {
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        if (AndroidUtilities.isTablet()) {
            boolean z = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer(!z && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity) baseFragment).isMainDialogList() && actionBarLayout != (actionBarLayout4 = this.actionBarLayout)) {
                    actionBarLayout4.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(baseFragment);
                    this.layersActionBarLayout.removeAllFragments();
                    this.layersActionBarLayout.setVisibility(8);
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    if (!this.tabletFullSize) {
                        this.shadowTabletSide.setVisibility(0);
                        if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                            this.backgroundTablet.setVisibility(0);
                        }
                    }
                    return false;
                }
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z2 = this.tabletFullSize;
                if (!z2 && actionBarLayout != (actionBarLayout3 = this.rightActionBarLayout)) {
                    actionBarLayout3.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (z2 && actionBarLayout != (actionBarLayout2 = this.actionBarLayout)) {
                    actionBarLayout2.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                if (actionBarLayout != actionBarLayout7) {
                    actionBarLayout7.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (z) {
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

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needCloseLastFragment(ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (actionBarLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (actionBarLayout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (actionBarLayout.fragmentsStack.size() >= 2 && !(actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
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

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z) {
        if (AndroidUtilities.isTablet() && actionBarLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(z, z);
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
