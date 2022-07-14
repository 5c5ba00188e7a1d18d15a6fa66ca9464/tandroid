package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.graphics.Shader;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
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
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.AssistActionBuilder;
import com.google.firebase.messaging.Constants;
import com.microsoft.appcenter.ingestion.models.CommonProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPPendingCall;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionIntroActivity;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
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
import org.telegram.ui.WallpapersListActivity;
import org.webrtc.voiceengine.WebRtcAudioTrack;
/* loaded from: classes4.dex */
public class LaunchActivity extends BasePermissionsActivity implements ActionBarLayout.ActionBarLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    private static final String EXTRA_ACTION_TOKEN = "actions.fulfillment.extra.ACTION_TOKEN";
    private static final int PLAY_SERVICES_REQUEST_CHECK_SETTINGS = 140;
    public static final int SCREEN_CAPTURE_REQUEST_CODE = 520;
    public static boolean isResumed;
    public static Runnable onResumeStaticCallback;
    private ActionBarLayout actionBarLayout;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private ArrayList<TLRPC.User> contactsToSend;
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
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private TLRPC.TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private String loadingThemeFileName;
    private Theme.ThemeInfo loadingThemeInfo;
    private AlertDialog loadingThemeProgressDialog;
    private TLRPC.TL_wallPaper loadingThemeWallpaper;
    private String loadingThemeWallpaperName;
    private AlertDialog localeDialog;
    private Runnable lockRunnable;
    private boolean navigateToPremiumBot;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendMessagesHelper.SendingMediaInfo> photoPathsArray;
    private AlertDialog proxyErrorDialog;
    private ActionBarLayout rightActionBarLayout;
    private View rippleAbove;
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
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList<>();
    private boolean isNavigationBarColorFrozen = false;
    private List<Runnable> onUserLeaveHintListeners = new ArrayList();

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        String os1;
        String os2;
        Intent intent;
        Uri uri;
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());
        this.currentAccount = UserConfig.selectedAccount;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated() && (intent = getIntent()) != null && intent.getAction() != null) {
            if ("android.intent.action.SEND".equals(intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                super.onCreate(savedInstanceState);
                finish();
                return;
            } else if ("android.intent.action.VIEW".equals(intent.getAction()) && (uri = intent.getData()) != null) {
                String url = uri.toString().toLowerCase();
                if (url.startsWith("tg:proxy") || url.startsWith("tg://proxy") || url.startsWith("tg:socks") || url.startsWith("tg://socks")) {
                }
            }
        }
        requestWindowFeature(1);
        setTheme(R.style.Theme_TMessages);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor(Theme.key_actionBarDefault) | (-16777216)));
            } catch (Throwable th) {
            }
            try {
                getWindow().setNavigationBarColor(-16777216);
            } catch (Throwable th2) {
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
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 24) {
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
            public void setThemeAnimationValue(float value) {
                super.setThemeAnimationValue(value);
                if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                    ArticleViewer.getInstance().updateThemeColors(value);
                }
                LaunchActivity.this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
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
        int i = 8;
        if (Build.VERSION.SDK_INT >= 21) {
            ImageView imageView = new ImageView(this);
            this.themeSwitchImageView = imageView;
            imageView.setVisibility(8);
        }
        DrawerLayoutContainer drawerLayoutContainer = new DrawerLayoutContainer(this) { // from class: org.telegram.ui.LaunchActivity.3
            @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            public void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                setDrawerPosition(getDrawerPosition());
            }
        };
        this.drawerLayoutContainer = drawerLayoutContainer;
        drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.frameLayout.addView(this.drawerLayoutContainer, LayoutHelper.createFrame(-1, -1.0f));
        if (Build.VERSION.SDK_INT >= 21) {
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
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout launchLayout = new RelativeLayout(this) { // from class: org.telegram.ui.LaunchActivity.5
                private boolean inLayout;

                @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
                public void requestLayout() {
                    if (this.inLayout) {
                        return;
                    }
                    super.requestLayout();
                }

                @Override // android.widget.RelativeLayout, android.view.View
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    this.inLayout = true;
                    int width = View.MeasureSpec.getSize(widthMeasureSpec);
                    int height = View.MeasureSpec.getSize(heightMeasureSpec);
                    setMeasuredDimension(width, height);
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.tabletFullSize = true;
                        LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(width, C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                    } else {
                        LaunchActivity.this.tabletFullSize = false;
                        int leftWidth = (width / 100) * 35;
                        if (leftWidth < AndroidUtilities.dp(320.0f)) {
                            leftWidth = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(leftWidth, C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                        LaunchActivity.this.shadowTabletSide.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                        LaunchActivity.this.rightActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(width - leftWidth, C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                    }
                    LaunchActivity.this.backgroundTablet.measure(View.MeasureSpec.makeMeasureSpec(width, C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                    LaunchActivity.this.shadowTablet.measure(View.MeasureSpec.makeMeasureSpec(width, C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(height, C.BUFFER_FLAG_ENCRYPTED));
                    LaunchActivity.this.layersActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), width), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), height), C.BUFFER_FLAG_ENCRYPTED));
                    this.inLayout = false;
                }

                @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean changed, int l, int t, int r, int b) {
                    int width = r - l;
                    int height = b - t;
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                    } else {
                        int leftWidth = (width / 100) * 35;
                        if (leftWidth < AndroidUtilities.dp(320.0f)) {
                            leftWidth = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.shadowTabletSide.layout(leftWidth, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + leftWidth, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                        LaunchActivity.this.rightActionBarLayout.layout(leftWidth, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + leftWidth, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
                    }
                    int x = (width - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
                    int y = (height - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
                    LaunchActivity.this.layersActionBarLayout.layout(x, y, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + x, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + y);
                    LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
                    LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
                }
            };
            this.drawerLayoutContainer.addView(launchLayout, LayoutHelper.createFrame(-1, -1.0f));
            SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.6
                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
                protected boolean isActionBarVisible() {
                    return false;
                }
            };
            this.backgroundTablet = sizeNotifierFrameLayout;
            sizeNotifierFrameLayout.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            launchLayout.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            launchLayout.addView(this.actionBarLayout);
            ActionBarLayout actionBarLayout = new ActionBarLayout(this);
            this.rightActionBarLayout = actionBarLayout;
            actionBarLayout.init(rightFragmentsStack);
            this.rightActionBarLayout.setDelegate(this);
            launchLayout.addView(this.rightActionBarLayout);
            FrameLayout frameLayout3 = new FrameLayout(this);
            this.shadowTabletSide = frameLayout3;
            frameLayout3.setBackgroundColor(1076449908);
            launchLayout.addView(this.shadowTabletSide);
            FrameLayout frameLayout4 = new FrameLayout(this);
            this.shadowTablet = frameLayout4;
            frameLayout4.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            launchLayout.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    return LaunchActivity.this.m3614lambda$onCreate$0$orgtelegramuiLaunchActivity(view2, motionEvent);
                }
            });
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda13.INSTANCE);
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
            if (!layerFragmentsStack.isEmpty()) {
                i = 0;
            }
            actionBarLayout3.setVisibility(i);
            VerticalPositionAutoAnimator.attach(this.layersActionBarLayout);
            launchLayout.addView(this.layersActionBarLayout);
        } else {
            this.drawerLayoutContainer.addView(this.actionBarLayout, new ViewGroup.LayoutParams(-1, -1));
        }
        this.sideMenuContainer = new FrameLayout(this);
        this.sideMenu = new RecyclerListView(this) { // from class: org.telegram.ui.LaunchActivity.7
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View child, long drawingTime) {
                int restore = -1;
                if (LaunchActivity.this.itemAnimator != null && LaunchActivity.this.itemAnimator.isRunning() && LaunchActivity.this.itemAnimator.isAnimatingChild(child)) {
                    restore = canvas.save();
                    canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
                }
                boolean result = super.drawChild(canvas, child, drawingTime);
                if (restore >= 0) {
                    canvas.restoreToCount(restore);
                    invalidate();
                    invalidateViews();
                }
                return result;
            }
        };
        SideMenultItemAnimator sideMenultItemAnimator = new SideMenultItemAnimator(this.sideMenu);
        this.itemAnimator = sideMenultItemAnimator;
        this.sideMenu.setItemAnimator(sideMenultItemAnimator);
        this.sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.sideMenu.setAllowItemsInteractionDuringAnimation(false);
        RecyclerListView recyclerListView = this.sideMenu;
        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, this.itemAnimator, this.drawerLayoutContainer);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView.setAdapter(drawerLayoutAdapter);
        this.sideMenuContainer.addView(this.sideMenu, LayoutHelper.createFrame(-1, -1.0f));
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenuContainer);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenuContainer.getLayoutParams();
        Point screenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(screenSize.x, screenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenuContainer.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view2, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view2, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view2, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view2, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view2, int i2, float f, float f2) {
                LaunchActivity.this.m3616lambda$onCreate$3$orgtelegramuiLaunchActivity(view2, i2, f, f2);
            }
        });
        final ItemTouchHelper sideMenuTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(3, 0) { // from class: org.telegram.ui.LaunchActivity.8
            private RecyclerView.ViewHolder selectedViewHolder;

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder.getItemViewType() == target.getItemViewType()) {
                    LaunchActivity.this.drawerLayoutAdapter.swapElements(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                clearSelectedViewHolder();
                if (actionState != 0) {
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
                        ObjectAnimator animator = ObjectAnimator.ofFloat(view2, "elevation", 0.0f);
                        animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.8.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animation) {
                                view2.setBackground(null);
                            }
                        });
                        animator.setDuration(150L).start();
                    }
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onChildDraw(Canvas c2, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View view2 = viewHolder.itemView;
                if (LaunchActivity.this.drawerLayoutAdapter.isAccountsShown()) {
                    RecyclerView.ViewHolder topViewHolder = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getFirstAccountPosition() - 1);
                    RecyclerView.ViewHolder bottomViewHolder = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getLastAccountPosition() + 1);
                    if (topViewHolder != null && topViewHolder.itemView != null && topViewHolder.itemView.getBottom() == view2.getTop() && dY < 0.0f) {
                        dY = 0.0f;
                    } else if (bottomViewHolder != null && bottomViewHolder.itemView != null && bottomViewHolder.itemView.getTop() == view2.getBottom() && dY > 0.0f) {
                        dY = 0.0f;
                    }
                }
                view2.setTranslationX(dX);
                view2.setTranslationY(dY);
            }
        });
        sideMenuTouchHelper.attachToRecyclerView(this.sideMenu);
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view2, int i2) {
                return LaunchActivity.this.m3617lambda$onCreate$4$orgtelegramuiLaunchActivity(sideMenuTouchHelper, view2, i2);
            }
        });
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3618lambda$onCreate$5$orgtelegramuiLaunchActivity();
            }
        });
        this.actionBarLayout.setDelegate(this);
        Theme.loadWallpaper();
        checkCurrentAccount();
        updateCurrentConnectionState(this.currentAccount);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
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
            if (savedInstanceState != null) {
                try {
                    String fragmentName = savedInstanceState.getString("fragment");
                    if (fragmentName != null) {
                        Bundle args = savedInstanceState.getBundle("args");
                        switch (fragmentName.hashCode()) {
                            case -1529105743:
                                if (fragmentName.equals("wallpapers")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case -1349522494:
                                if (fragmentName.equals("chat_profile")) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 3052376:
                                if (fragmentName.equals("chat")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 98629247:
                                if (fragmentName.equals("group")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 738950403:
                                if (fragmentName.equals("channel")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1434631203:
                                if (fragmentName.equals("settings")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                if (args != null) {
                                    ChatActivity chat = new ChatActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(chat)) {
                                        chat.restoreSelfArgs(savedInstanceState);
                                        break;
                                    }
                                }
                                break;
                            case 1:
                                args.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
                                ProfileActivity settings = new ProfileActivity(args);
                                this.actionBarLayout.addFragmentToStack(settings);
                                settings.restoreSelfArgs(savedInstanceState);
                                break;
                            case 2:
                                if (args != null) {
                                    GroupCreateFinalActivity group = new GroupCreateFinalActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(group)) {
                                        group.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 3:
                                if (args != null) {
                                    ChannelCreateActivity channel = new ChannelCreateActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(channel)) {
                                        channel.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 4:
                                if (args != null) {
                                    ProfileActivity profile = new ProfileActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(profile)) {
                                        profile.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 5:
                                WallpapersListActivity settings2 = new WallpapersListActivity(0);
                                this.actionBarLayout.addFragmentToStack(settings2);
                                settings2.restoreSelfArgs(savedInstanceState);
                                break;
                        }
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        } else {
            BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(0);
            if (fragment instanceof DialogsActivity) {
                ((DialogsActivity) fragment).setSideMenu(this.sideMenu);
            }
            boolean allowOpen = true;
            if (AndroidUtilities.isTablet()) {
                allowOpen = this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty();
                if (this.layersActionBarLayout.fragmentsStack.size() == 1 && ((this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.layersActionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
                    allowOpen = false;
                }
            }
            if (this.actionBarLayout.fragmentsStack.size() == 1 && ((this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.actionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
                allowOpen = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(allowOpen, false);
        }
        checkLayout();
        checkSystemBarColors();
        handleIntent(getIntent(), false, savedInstanceState != null, false);
        try {
            String os12 = Build.DISPLAY;
            String os22 = Build.USER;
            if (os12 == null) {
                os1 = "";
            } else {
                os1 = os12.toLowerCase();
            }
            if (os22 == null) {
                os2 = "";
            } else {
                os2 = os1.toLowerCase();
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("OS name " + os1 + " " + os2);
            }
            if ((os1.contains("flyme") || os2.contains("flyme")) && Build.VERSION.SDK_INT <= 24) {
                AndroidUtilities.incorrectDisplaySizeFix = true;
                final View view2 = getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public final void onGlobalLayout() {
                        LaunchActivity.lambda$onCreate$6(view2);
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
        if (Build.VERSION.SDK_INT >= 23) {
            FingerprintController.checkKeyReady();
        }
        if (Build.VERSION.SDK_INT >= 28) {
            ActivityManager am = (ActivityManager) getSystemService("activity");
            if (am.isBackgroundRestricted() && System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() >= 86400000) {
                AlertsCreator.createBackgroundActivityDialog(this).show();
                SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
            }
        }
    }

    /* renamed from: lambda$onCreate$0$org-telegram-ui-LaunchActivity */
    public /* synthetic */ boolean m3614lambda$onCreate$0$orgtelegramuiLaunchActivity(View v, MotionEvent event) {
        if (this.actionBarLayout.fragmentsStack.isEmpty() || event.getAction() != 1) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        int[] location = new int[2];
        this.layersActionBarLayout.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        if (this.layersActionBarLayout.checkTransitionAnimation() || (x > viewX && x < this.layersActionBarLayout.getWidth() + viewX && y > viewY && y < this.layersActionBarLayout.getHeight() + viewY)) {
            return false;
        }
        if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
            for (int a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
            }
            this.layersActionBarLayout.closeLastFragment(true);
        }
        return true;
    }

    public static /* synthetic */ void lambda$onCreate$1(View v) {
    }

    /* renamed from: lambda$onCreate$3$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3616lambda$onCreate$3$orgtelegramuiLaunchActivity(View view, int position, float x, float y) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        boolean z = true;
        if (position == 0) {
            DrawerProfileCell profileCell = (DrawerProfileCell) view;
            if (profileCell.isInAvatar(x, y)) {
                openSettings(profileCell.hasAvatar());
                return;
            }
            this.drawerLayoutAdapter.setAccountsShown(!drawerLayoutAdapter.isAccountsShown(), true);
        } else if (view instanceof DrawerUserCell) {
            switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (view instanceof DrawerAddCell) {
            int freeAccounts = 0;
            Integer availableAccount = null;
            for (int a = 3; a >= 0; a--) {
                if (!UserConfig.getInstance(a).isClientActivated()) {
                    freeAccounts++;
                    if (availableAccount == null) {
                        availableAccount = Integer.valueOf(a);
                    }
                }
            }
            if (!UserConfig.hasPremiumOnAccounts()) {
                freeAccounts--;
            }
            if (freeAccounts > 0 && availableAccount != null) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new LoginActivity(availableAccount.intValue()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (!UserConfig.hasPremiumOnAccounts() && this.actionBarLayout.fragmentsStack.size() > 0) {
                BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(0);
                LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(fragment, this, 7, this.currentAccount);
                fragment.showDialog(limitReachedBottomSheet);
                limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda28
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.m3615lambda$onCreate$2$orgtelegramuiLaunchActivity();
                    }
                };
            }
        } else {
            int id = this.drawerLayoutAdapter.getId(position);
            if (id == 2) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle args = new Bundle();
                args.putBoolean("onlyUsers", true);
                args.putBoolean("destroyAfterSelect", true);
                args.putBoolean("createSecretChat", true);
                args.putBoolean("allowBots", false);
                args.putBoolean("allowSelf", false);
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ContactsActivity(args));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && preferences.getBoolean("channel_intro", false)) {
                    Bundle args2 = new Bundle();
                    args2.putInt("step", 0);
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ChannelCreateActivity(args2));
                } else {
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ActionIntroActivity(0));
                    preferences.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle args3 = new Bundle();
                args3.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ChatActivity(args3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ActionIntroActivity(1));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                }
                boolean enabled = true;
                if (Build.VERSION.SDK_INT >= 28) {
                    LocationManager lm = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
                    enabled = lm.isLocationEnabled();
                } else if (Build.VERSION.SDK_INT >= 19) {
                    try {
                        int mode = Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0);
                        if (mode == 0) {
                            z = false;
                        }
                        enabled = z;
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                if (enabled) {
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new PeopleNearbyActivity());
                } else {
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ActionIntroActivity(4));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 13) {
                Browser.openUrl(this, LocaleController.getString("TelegramFeaturesUrl", R.string.TelegramFeaturesUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            }
        }
    }

    /* renamed from: lambda$onCreate$2$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3615lambda$onCreate$2$orgtelegramuiLaunchActivity() {
        this.drawerLayoutContainer.closeDrawer(false);
    }

    /* renamed from: lambda$onCreate$4$org-telegram-ui-LaunchActivity */
    public /* synthetic */ boolean m3617lambda$onCreate$4$orgtelegramuiLaunchActivity(ItemTouchHelper sideMenuTouchHelper, View view, int position) {
        if (view instanceof DrawerUserCell) {
            final int accountNumber = ((DrawerUserCell) view).getAccountNumber();
            if (accountNumber == this.currentAccount || AndroidUtilities.isTablet()) {
                sideMenuTouchHelper.startDrag(this.sideMenu.getChildViewHolder(view));
                return false;
            }
            BaseFragment fragment = new DialogsActivity(null) { // from class: org.telegram.ui.LaunchActivity.9
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.DialogsActivity, org.telegram.ui.ActionBar.BaseFragment
                public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
                    super.onTransitionAnimationEnd(isOpen, backward);
                    if (!isOpen && backward) {
                        LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                    }
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public void onPreviewOpenAnimationEnd() {
                    super.onPreviewOpenAnimationEnd();
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                    LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                    LaunchActivity.this.switchToAccount(accountNumber, true);
                }
            };
            fragment.setCurrentAccount(accountNumber);
            this.actionBarLayout.presentFragmentAsPreview(fragment);
            this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(true);
            return true;
        }
        return false;
    }

    /* renamed from: lambda$onCreate$5$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3618lambda$onCreate$5$orgtelegramuiLaunchActivity() {
        checkSystemBarColors(true, false);
    }

    public static /* synthetic */ void lambda$onCreate$6(View view) {
        int height = view.getMeasuredHeight();
        FileLog.d("height = " + height + " displayHeight = " + AndroidUtilities.displaySize.y);
        if (Build.VERSION.SDK_INT >= 21) {
            height -= AndroidUtilities.statusBarHeight;
        }
        if (height > AndroidUtilities.dp(100.0f) && height < AndroidUtilities.displaySize.y && AndroidUtilities.dp(100.0f) + height > AndroidUtilities.displaySize.y) {
            AndroidUtilities.displaySize.y = height;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("fix display size y to " + AndroidUtilities.displaySize.y);
            }
        }
    }

    public void addOnUserLeaveHintListener(Runnable callback) {
        this.onUserLeaveHintListeners.add(callback);
    }

    public void removeOnUserLeaveHintListener(Runnable callback) {
        this.onUserLeaveHintListeners.remove(callback);
    }

    private BaseFragment getClientNotActivatedFragment() {
        if (LoginActivity.loadCurrentState(false).getInt("currentViewNum", 0) != 0) {
            return new LoginActivity();
        }
        return new IntroActivity();
    }

    public FireworksOverlay getFireworksOverlay() {
        return this.fireworksOverlay;
    }

    private void openSettings(boolean expanded) {
        Bundle args = new Bundle();
        args.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
        if (expanded) {
            args.putBoolean("expandPhoto", true);
        }
        ProfileActivity fragment = new ProfileActivity(args);
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(fragment);
        this.drawerLayoutContainer.closeDrawer(false);
    }

    private void checkSystemBarColors() {
        checkSystemBarColors(false, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean useCurrentFragment) {
        checkSystemBarColors(useCurrentFragment, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean checkStatusBar, boolean checkNavigationBar) {
        checkSystemBarColors(false, checkStatusBar, checkNavigationBar);
    }

    private void checkSystemBarColors(boolean useCurrentFragment, boolean checkStatusBar, boolean checkNavigationBar) {
        BaseFragment currentFragment;
        boolean enable;
        BaseFragment baseFragment;
        ArrayList<BaseFragment> arrayList;
        boolean z = true;
        if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            currentFragment = arrayList2.get(arrayList2.size() - 1);
        } else {
            currentFragment = null;
        }
        if (currentFragment != null && (currentFragment.isRemovingFromStack() || currentFragment.isInPreviewMode())) {
            if (mainFragmentsStack.size() > 1) {
                baseFragment = mainFragmentsStack.get(arrayList.size() - 2);
            } else {
                baseFragment = null;
            }
            currentFragment = baseFragment;
        }
        boolean forceLightStatusBar = currentFragment != null && currentFragment.hasForceLightStatusBar();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkStatusBar) {
                if (currentFragment != null) {
                    enable = currentFragment.isLightStatusBar();
                } else {
                    enable = ColorUtils.calculateLuminance(Theme.getColor(Theme.key_actionBarDefault, null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), enable, forceLightStatusBar);
            }
            if (Build.VERSION.SDK_INT >= 26 && checkNavigationBar && (!useCurrentFragment || currentFragment == null || !currentFragment.isInPreviewMode())) {
                Window window = getWindow();
                int color = (currentFragment == null || !useCurrentFragment) ? Theme.getColor(Theme.key_windowBackgroundGray, null, true) : currentFragment.getNavigationBarColor();
                if (window.getNavigationBarColor() != color) {
                    window.setNavigationBarColor(color);
                    float brightness = AndroidUtilities.computePerceivedBrightness(color);
                    Window window2 = getWindow();
                    if (brightness < 0.721f) {
                        z = false;
                    }
                    AndroidUtilities.setLightNavigationBar(window2, z);
                }
            }
        }
        if ((SharedConfig.noStatusBar || forceLightStatusBar) && Build.VERSION.SDK_INT >= 21 && checkStatusBar) {
            getWindow().setStatusBarColor(0);
        }
    }

    public static /* synthetic */ DialogsActivity lambda$switchToAccount$7(Void obj) {
        return new DialogsActivity(null);
    }

    public void switchToAccount(int account, boolean removeAll) {
        switchToAccount(account, removeAll, LaunchActivity$$ExternalSyntheticLambda65.INSTANCE);
    }

    public void switchToAccount(int account, boolean removeAll, GenericProvider<Void, DialogsActivity> dialogsActivityProvider) {
        if (account == UserConfig.selectedAccount || !UserConfig.isValidAccount(account)) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        UserConfig.selectedAccount = account;
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
        if (removeAll) {
            this.actionBarLayout.removeAllFragments();
        } else {
            this.actionBarLayout.removeFragmentFromStack(0);
        }
        DialogsActivity dialogsActivity = dialogsActivityProvider.provide(null);
        dialogsActivity.setSideMenu(this.sideMenu);
        this.actionBarLayout.addFragmentToStack(dialogsActivity, 0);
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
        }
        if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        }
        if (UserConfig.getInstance(account).unacceptedTermsOfService != null) {
            showTosActivity(account, UserConfig.getInstance(account).unacceptedTermsOfService);
        }
        updateCurrentConnectionState(this.currentAccount);
    }

    private void switchToAvailableAccountOrLogout() {
        int account = -1;
        int a = 0;
        while (true) {
            if (a >= 4) {
                break;
            } else if (!UserConfig.getInstance(a).isClientActivated()) {
                a++;
            } else {
                account = a;
                break;
            }
        }
        TermsOfServiceView termsOfServiceView = this.termsOfServiceView;
        if (termsOfServiceView != null) {
            termsOfServiceView.setVisibility(8);
        }
        if (account != -1) {
            switchToAccount(account, true);
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
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new IntroActivity().setOnLogout());
    }

    public static void clearFragments() {
        Iterator<BaseFragment> it = mainFragmentsStack.iterator();
        while (it.hasNext()) {
            BaseFragment fragment = it.next();
            fragment.onFragmentDestroy();
        }
        mainFragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            Iterator<BaseFragment> it2 = layerFragmentsStack.iterator();
            while (it2.hasNext()) {
                BaseFragment fragment2 = it2.next();
                fragment2.onFragmentDestroy();
            }
            layerFragmentsStack.clear();
            Iterator<BaseFragment> it3 = rightFragmentsStack.iterator();
            while (it3.hasNext()) {
                BaseFragment fragment3 = it3.next();
                fragment3.onFragmentDestroy();
            }
            rightFragmentsStack.clear();
        }
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        if (this.currentAccount != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
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
        }
        this.currentAccount = UserConfig.selectedAccount;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
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
    }

    private void checkLayout() {
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        int i = 0;
        if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2)) {
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                for (int a = 1; a < this.actionBarLayout.fragmentsStack.size(); a = (a - 1) + 1) {
                    BaseFragment chatFragment = this.actionBarLayout.fragmentsStack.get(a);
                    if (chatFragment instanceof ChatActivity) {
                        ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                    }
                    chatFragment.onPause();
                    this.actionBarLayout.fragmentsStack.remove(a);
                    this.rightActionBarLayout.fragmentsStack.add(chatFragment);
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
            for (int a2 = 0; a2 < this.rightActionBarLayout.fragmentsStack.size(); a2 = (a2 - 1) + 1) {
                BaseFragment chatFragment2 = this.rightActionBarLayout.fragmentsStack.get(a2);
                if (chatFragment2 instanceof ChatActivity) {
                    ((ChatActivity) chatFragment2).setIgnoreAttachOnPause(true);
                }
                chatFragment2.onPause();
                this.rightActionBarLayout.fragmentsStack.remove(a2);
                this.actionBarLayout.fragmentsStack.add(chatFragment2);
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

    private void showUpdateActivity(int account, TLRPC.TL_help_appUpdate update, boolean check) {
        if (this.blockingUpdateView == null) {
            BlockingUpdateView blockingUpdateView = new BlockingUpdateView(this) { // from class: org.telegram.ui.LaunchActivity.10
                @Override // org.telegram.ui.Components.BlockingUpdateView, android.view.View
                public void setVisibility(int visibility) {
                    super.setVisibility(visibility);
                    if (visibility == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.blockingUpdateView = blockingUpdateView;
            this.drawerLayoutContainer.addView(blockingUpdateView, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(account, update, check);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showTosActivity(int account, TLRPC.TL_help_termsOfService tos) {
        if (this.termsOfServiceView == null) {
            TermsOfServiceView termsOfServiceView = new TermsOfServiceView(this);
            this.termsOfServiceView = termsOfServiceView;
            termsOfServiceView.setAlpha(0.0f);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new AnonymousClass11());
        }
        TLRPC.TL_help_termsOfService currentTos = UserConfig.getInstance(account).unacceptedTermsOfService;
        if (currentTos != tos && (currentTos == null || !currentTos.id.data.equals(tos.id.data))) {
            UserConfig.getInstance(account).unacceptedTermsOfService = tos;
            UserConfig.getInstance(account).saveConfig(false);
        }
        this.termsOfServiceView.show(account, tos);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.termsOfServiceView.animate().alpha(1.0f).setDuration(150L).setInterpolator(AndroidUtilities.decelerateInterpolator).setListener(null).start();
    }

    /* renamed from: org.telegram.ui.LaunchActivity$11 */
    /* loaded from: classes4.dex */
    public class AnonymousClass11 implements TermsOfServiceView.TermsOfServiceViewDelegate {
        AnonymousClass11() {
            LaunchActivity.this = this$0;
        }

        @Override // org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate
        public void onAcceptTerms(int account) {
            UserConfig.getInstance(account).unacceptedTermsOfService = null;
            UserConfig.getInstance(account).saveConfig(false);
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            if (LaunchActivity.mainFragmentsStack.size() > 0) {
                ((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1)).onResume();
            }
            LaunchActivity.this.termsOfServiceView.animate().alpha(0.0f).setDuration(150L).setInterpolator(AndroidUtilities.accelerateInterpolator).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$11$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.AnonymousClass11.this.m3674lambda$onAcceptTerms$0$orgtelegramuiLaunchActivity$11();
                }
            }).start();
        }

        /* renamed from: lambda$onAcceptTerms$0$org-telegram-ui-LaunchActivity$11 */
        public /* synthetic */ void m3674lambda$onAcceptTerms$0$orgtelegramuiLaunchActivity$11() {
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }

        @Override // org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate
        public void onDeclineTerms(int account) {
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }
    }

    public void showPasscodeActivity(boolean fingerprint, boolean animated, int x, int y, final Runnable onShow, Runnable onStart) {
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
        MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
        if (messageObject != null && messageObject.isRoundVideo()) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
        this.passcodeView.onShow(fingerprint, animated, x, y, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3671lambda$showPasscodeActivity$8$orgtelegramuiLaunchActivity(onShow);
            }
        }, onStart);
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate(new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword() {
                LaunchActivity.this.m3672lambda$showPasscodeActivity$9$orgtelegramuiLaunchActivity();
            }
        });
    }

    /* renamed from: lambda$showPasscodeActivity$8$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3671lambda$showPasscodeActivity$8$orgtelegramuiLaunchActivity(Runnable onShow) {
        this.actionBarLayout.setVisibility(4);
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.setVisibility(4);
            }
            this.rightActionBarLayout.setVisibility(4);
        }
        if (onShow != null) {
            onShow.run();
        }
    }

    /* renamed from: lambda$showPasscodeActivity$9$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3672lambda$showPasscodeActivity$9$orgtelegramuiLaunchActivity() {
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
    /* JADX WARN: Code restructure failed: missing block: B:1016:0x2319, code lost:
        if (org.telegram.messenger.MessagesController.getInstance(r12[0]).checkCanOpenChat(r2, org.telegram.ui.LaunchActivity.mainFragmentsStack.get(r4.size() - 1)) != false) goto L1017;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x016d, code lost:
        r3 = r53.getIntent().getExtras();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0179, code lost:
        r57 = false;
        r56 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x017f, code lost:
        r39 = r3.getLong("dialogId", 0);
        r0 = r3.getString("hash", null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x018a, code lost:
        r3 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x018c, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01bc, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r3 = r52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:995:0x2262, code lost:
        if (org.telegram.messenger.MessagesController.getInstance(r12[0]).checkCanOpenChat(r3, org.telegram.ui.LaunchActivity.mainFragmentsStack.get(r10.size() - 1)) != false) goto L997;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:1166:0x2847  */
    /* JADX WARN: Removed duplicated region for block: B:1168:0x2859 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1171:0x2861  */
    /* JADX WARN: Removed duplicated region for block: B:1182:0x28ae  */
    /* JADX WARN: Removed duplicated region for block: B:1193:0x28fb  */
    /* JADX WARN: Removed duplicated region for block: B:1195:0x2907  */
    /* JADX WARN: Removed duplicated region for block: B:1197:0x290f  */
    /* JADX WARN: Removed duplicated region for block: B:1204:0x292b  */
    /* JADX WARN: Removed duplicated region for block: B:1252:0x1e50 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x03d4  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0602  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0611  */
    /* JADX WARN: Removed duplicated region for block: B:784:0x1b23  */
    /* JADX WARN: Removed duplicated region for block: B:838:0x1d15  */
    /* JADX WARN: Removed duplicated region for block: B:848:0x1d41  */
    /* JADX WARN: Removed duplicated region for block: B:849:0x1d44  */
    /* JADX WARN: Removed duplicated region for block: B:851:0x1d59  */
    /* JADX WARN: Removed duplicated region for block: B:857:0x1d76  */
    /* JADX WARN: Removed duplicated region for block: B:925:0x1f2c  */
    /* JADX WARN: Removed duplicated region for block: B:928:0x1f70  */
    /* JADX WARN: Removed duplicated region for block: B:929:0x1f80  */
    /* JADX WARN: Removed duplicated region for block: B:972:0x21ea  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0252  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean isNew, boolean restore, boolean fromPassword) {
        String newContactPhone;
        long j;
        boolean showCallLog;
        boolean videoCallUser;
        long push_user_id;
        final int[] intentAccount;
        int i;
        String action;
        int open_widget_edit;
        LaunchActivity launchActivity;
        int open_widget_edit_type;
        long push_chat_id;
        String searchQuery;
        String callSearchQuery;
        String newContactName;
        int push_enc_id;
        int push_msg_id;
        int open_widget_edit_type2;
        boolean isNew2;
        String searchQuery2;
        Intent intent2;
        String callSearchQuery2;
        boolean z;
        int open_settings;
        final BaseFragment fragment;
        int i2;
        int push_enc_id2;
        String searchQuery3;
        String searchQuery4;
        boolean z2;
        int push_msg_id2;
        long push_chat_id2;
        long push_user_id2;
        String action2;
        boolean z3;
        int push_msg_id3;
        int push_msg_id4;
        String str;
        int[] intentAccount2;
        boolean message;
        boolean z4;
        int push_msg_id5;
        long push_chat_id3;
        String attachMenuBotChoose;
        String attachMenuBotToOpen;
        String setAsAttachBot;
        String scheme;
        int[] intentAccount3;
        String action3;
        boolean hasUrl;
        int videoTimestamp;
        Integer commentId;
        Integer threadId;
        String sticker;
        Long channelId;
        String group;
        Integer messageId;
        String attachMenuBotToOpen2;
        TLRPC.TL_wallPaper wallPaper;
        String code;
        String theme;
        String lang;
        String livestream;
        String voicechat;
        String game;
        String inputInvoiceSlug;
        String botChannel;
        String code2;
        String theme2;
        String lang2;
        String phoneHash;
        String livestream2;
        String voicechat2;
        String game2;
        String phone;
        String message2;
        String botChatAdminParams;
        String botChannel2;
        boolean videoCallUser2;
        boolean audioCallUser;
        boolean unsupportedUrl;
        String phone2;
        String phoneHash2;
        String message3;
        String scheme2;
        String phoneHash3;
        String phone3;
        final TLRPC.TL_account_sendConfirmPhoneCode req;
        String str2;
        Exception e;
        Throwable th;
        Throwable th2;
        String str3;
        boolean success;
        char c;
        String group2;
        int i3;
        int i4;
        String username;
        HashMap<String, String> auth;
        Long channelId2;
        Integer messageId2;
        Integer threadId2;
        int i5;
        int push_msg_id6;
        long push_user_id3;
        String unsupportedUrl2;
        String url;
        boolean error;
        Exception e2;
        ArrayList<Parcelable> uris;
        String type;
        ArrayList<Parcelable> uris2;
        boolean error2;
        Parcelable parcelable;
        boolean error3;
        String originalPath;
        ArrayList<Parcelable> uris3;
        boolean ok;
        boolean ok2;
        String fileName;
        Exception e3;
        Pattern pattern;
        boolean z5;
        int push_msg_id7;
        long push_chat_id4;
        String text;
        Parcelable parcelable2;
        Pattern pattern2;
        CharSequence textSequence;
        String hash;
        Throwable e4;
        String hash2;
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
        String action4 = intent.getAction();
        int[] intentAccount4 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(intentAccount4[0], true);
        boolean isVoipIntent = action4 != null && action4.equals("voip");
        if (!fromPassword && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!isVoipIntent) {
                this.passcodeSaveIntent = intent;
                this.passcodeSaveIntentIsNew = isNew;
                this.passcodeSaveIntentIsRestore = restore;
                return false;
            }
        }
        boolean pushOpened = false;
        int push_enc_id3 = 0;
        int open_widget_edit2 = -1;
        int open_widget_edit_type3 = -1;
        int open_new_dialog = 0;
        long dialogId = 0;
        boolean showDialogsList = false;
        boolean showPlayer = false;
        boolean showLocations = false;
        boolean showGroupVoip = false;
        boolean audioCallUser2 = false;
        boolean videoCallUser3 = false;
        String needCallAlert = null;
        String newContact = null;
        String newContactAlert = null;
        String scanQr = null;
        String searchQuery5 = null;
        String callSearchQuery3 = null;
        String newContactName2 = null;
        String newContactPhone2 = null;
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
        int i6 = flags & 1048576;
        String str4 = Constants.MessagePayloadKeys.MSGID_SERVER;
        if (i6 != 0) {
            push_user_id2 = 0;
            push_chat_id2 = 0;
            push_msg_id2 = 0;
            z2 = false;
            showCallLog = false;
            j = 0;
            launchActivity = this;
            intentAccount = intentAccount4;
            action2 = action4;
            action = str4;
        } else if (intent != null && intent.getAction() != null && !restore) {
            push_user_id2 = 0;
            String str5 = "";
            if (!"android.intent.action.SEND".equals(intent.getAction())) {
                push_chat_id2 = 0;
                push_msg_id2 = 0;
                z2 = false;
                showCallLog = false;
                if ("org.telegram.messenger.CREATE_STICKER_PACK".equals(intent.getAction())) {
                    try {
                        this.importingStickers = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                        this.importingStickersEmoji = intent.getStringArrayListExtra("STICKER_EMOJIS");
                        this.importingStickersSoftware = intent.getStringExtra("IMPORTER");
                    } catch (Throwable e5) {
                        FileLog.e(e5);
                        this.importingStickers = null;
                        this.importingStickersEmoji = null;
                        this.importingStickersSoftware = null;
                    }
                    intentAccount = intentAccount4;
                    action2 = action4;
                    action = str4;
                    launchActivity = this;
                    j = 0;
                } else if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                    boolean error4 = false;
                    try {
                        uris = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                        type = intent.getType();
                    } catch (Exception e6) {
                        e2 = e6;
                    }
                    if (uris != null) {
                        int a = 0;
                        while (a < uris.size()) {
                            try {
                                Parcelable parcelable3 = uris.get(a);
                                if (!(parcelable3 instanceof Uri)) {
                                    parcelable3 = Uri.parse(parcelable3.toString());
                                }
                                Uri uri = (Uri) parcelable3;
                                if (uri != null && AndroidUtilities.isInternalUri(uri)) {
                                    uris.remove(a);
                                    a--;
                                }
                                a++;
                            } catch (Exception e7) {
                                e2 = e7;
                                FileLog.e(e2);
                                error = true;
                                if (error) {
                                }
                                intentAccount = intentAccount4;
                                action2 = action4;
                                action = str4;
                                launchActivity = this;
                                j = 0;
                                videoCallUser = false;
                                searchQuery = null;
                                callSearchQuery = null;
                                newContactName = null;
                                newContactPhone = null;
                                push_enc_id = 0;
                                open_widget_edit = -1;
                                open_widget_edit_type = -1;
                                push_user_id = push_user_id2;
                                push_chat_id = push_chat_id2;
                                push_msg_id = push_msg_id2;
                                i = z2;
                                final String newContactName3 = newContactName;
                                if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                }
                                if (!pushOpened) {
                                }
                                if (isVoipIntent) {
                                }
                                if (showGroupVoip) {
                                }
                                intent2.setAction(null);
                                return pushOpened;
                            }
                        }
                        if (uris.isEmpty()) {
                            uris2 = null;
                            if (uris2 == null) {
                                if (type != null && type.startsWith("image/")) {
                                    for (int a2 = 0; a2 < uris2.size(); a2++) {
                                        Parcelable parcelable4 = uris2.get(a2);
                                        if (!(parcelable4 instanceof Uri)) {
                                            parcelable4 = Uri.parse(parcelable4.toString());
                                        }
                                        Uri uri2 = (Uri) parcelable4;
                                        if (this.photoPathsArray == null) {
                                            this.photoPathsArray = new ArrayList<>();
                                        }
                                        SendMessagesHelper.SendingMediaInfo info = new SendMessagesHelper.SendingMediaInfo();
                                        info.uri = uri2;
                                        this.photoPathsArray.add(info);
                                    }
                                    error2 = false;
                                } else {
                                    Set<String> exportUris = MessagesController.getInstance(intentAccount4[0]).exportUri;
                                    int a3 = 0;
                                    while (a3 < uris2.size()) {
                                        Parcelable parcelable5 = uris2.get(a3);
                                        if (parcelable5 instanceof Uri) {
                                            parcelable = parcelable5;
                                        } else {
                                            parcelable = Uri.parse(parcelable5.toString());
                                        }
                                        Uri uri3 = (Uri) parcelable;
                                        String path = AndroidUtilities.getPath(uri3);
                                        String originalPath2 = parcelable.toString();
                                        if (originalPath2 != null) {
                                            error3 = error4;
                                            originalPath = originalPath2;
                                        } else {
                                            error3 = error4;
                                            originalPath = path;
                                        }
                                        try {
                                            if (!BuildVars.LOGS_ENABLED) {
                                                uris3 = uris2;
                                            } else {
                                                StringBuilder sb = new StringBuilder();
                                                uris3 = uris2;
                                                sb.append("export path = ");
                                                sb.append(originalPath);
                                                FileLog.d(sb.toString());
                                            }
                                            if (0 == 0 && originalPath != null && this.exportingChatUri == null) {
                                                boolean ok3 = false;
                                                String fileName2 = FileLoader.fixFileName(MediaController.getFileName(uri3));
                                                for (String u : exportUris) {
                                                    try {
                                                        pattern = Pattern.compile(u);
                                                    } catch (Exception e8) {
                                                        e3 = e8;
                                                        ok2 = ok3;
                                                        fileName = fileName2;
                                                    }
                                                    if (!pattern.matcher(originalPath).find()) {
                                                        ok2 = ok3;
                                                        fileName = fileName2;
                                                        try {
                                                        } catch (Exception e9) {
                                                            e3 = e9;
                                                            FileLog.e(e3);
                                                            fileName2 = fileName;
                                                            ok3 = ok2;
                                                        }
                                                        if (!pattern.matcher(fileName).find()) {
                                                            fileName2 = fileName;
                                                            ok3 = ok2;
                                                        }
                                                    }
                                                    this.exportingChatUri = uri3;
                                                    ok = true;
                                                }
                                                ok = ok3;
                                                if (!ok) {
                                                    if (originalPath.startsWith("content://com.kakao.talk") && originalPath.endsWith("KakaoTalkChats.txt")) {
                                                        this.exportingChatUri = uri3;
                                                    }
                                                }
                                                a3++;
                                                error4 = error3;
                                                uris2 = uris3;
                                            }
                                            if (path != null) {
                                                if (path.startsWith("file:")) {
                                                    path = path.replace("file://", str5);
                                                }
                                                if (this.documentsPathsArray == null) {
                                                    this.documentsPathsArray = new ArrayList<>();
                                                    this.documentsOriginalPathsArray = new ArrayList<>();
                                                }
                                                this.documentsPathsArray.add(path);
                                                this.documentsOriginalPathsArray.add(originalPath);
                                            } else {
                                                if (this.documentsUrisArray == null) {
                                                    this.documentsUrisArray = new ArrayList<>();
                                                }
                                                this.documentsUrisArray.add(uri3);
                                                this.documentsMimeType = type;
                                            }
                                            a3++;
                                            error4 = error3;
                                            uris2 = uris3;
                                        } catch (Exception e10) {
                                            e2 = e10;
                                            FileLog.e(e2);
                                            error = true;
                                            if (error) {
                                            }
                                            intentAccount = intentAccount4;
                                            action2 = action4;
                                            action = str4;
                                            launchActivity = this;
                                            j = 0;
                                            videoCallUser = false;
                                            searchQuery = null;
                                            callSearchQuery = null;
                                            newContactName = null;
                                            newContactPhone = null;
                                            push_enc_id = 0;
                                            open_widget_edit = -1;
                                            open_widget_edit_type = -1;
                                            push_user_id = push_user_id2;
                                            push_chat_id = push_chat_id2;
                                            push_msg_id = push_msg_id2;
                                            i = z2;
                                            final String newContactName32 = newContactName;
                                            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                            }
                                            if (!pushOpened) {
                                            }
                                            if (isVoipIntent) {
                                            }
                                            if (showGroupVoip) {
                                            }
                                            intent2.setAction(null);
                                            return pushOpened;
                                        }
                                    }
                                    error2 = error4;
                                }
                                error = error2;
                            } else {
                                error = true;
                            }
                            if (error) {
                                Toast.makeText(this, "Unsupported content", 0).show();
                            }
                            intentAccount = intentAccount4;
                            action2 = action4;
                            action = str4;
                            launchActivity = this;
                            j = 0;
                        }
                    }
                    uris2 = uris;
                    if (uris2 == null) {
                    }
                    if (error) {
                    }
                    intentAccount = intentAccount4;
                    action2 = action4;
                    action = str4;
                    launchActivity = this;
                    j = 0;
                } else if (!"android.intent.action.VIEW".equals(intent.getAction())) {
                    launchActivity = this;
                    j = 0;
                    if (intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        i = true;
                        videoCallUser = false;
                        searchQuery = null;
                        callSearchQuery = null;
                        newContactName = null;
                        newContactPhone = null;
                        push_enc_id = 0;
                        open_widget_edit = -1;
                        open_widget_edit_type = -1;
                        push_user_id = 0;
                        push_chat_id = 0;
                        push_msg_id = 0;
                        intentAccount = intentAccount4;
                        action = str4;
                    } else if (intent.getAction().equals("new_dialog")) {
                        open_new_dialog = 1;
                        videoCallUser = false;
                        searchQuery = null;
                        callSearchQuery = null;
                        newContactName = null;
                        newContactPhone = null;
                        push_enc_id = 0;
                        open_widget_edit = -1;
                        open_widget_edit_type = -1;
                        push_user_id = 0;
                        push_chat_id = 0;
                        push_msg_id = 0;
                        i = false;
                        intentAccount = intentAccount4;
                        action = str4;
                    } else if (intent.getAction().startsWith("com.tmessages.openchat")) {
                        long chatId = intent.getLongExtra("chatId", intent.getIntExtra("chatId", 0));
                        long userId = intent.getLongExtra("userId", intent.getIntExtra("userId", 0));
                        int encId = intent.getIntExtra("encId", 0);
                        int widgetId = intent.getIntExtra("appWidgetId", 0);
                        if (widgetId != 0) {
                            open_widget_edit2 = widgetId;
                            open_widget_edit_type3 = intent.getIntExtra("appWidgetType", 0);
                            z3 = true;
                            chatId = 0;
                            push_msg_id3 = 0;
                            intentAccount = intentAccount4;
                            action = str4;
                        } else {
                            if (0 != 0) {
                                action = str4;
                                push_msg_id4 = 0;
                            } else {
                                action = str4;
                                push_msg_id4 = intent.getIntExtra(action, 0);
                            }
                            if (chatId != 0) {
                                intentAccount = intentAccount4;
                                NotificationCenter.getInstance(intentAccount[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                push_msg_id3 = push_msg_id4;
                                z3 = false;
                            } else {
                                intentAccount = intentAccount4;
                                if (userId != 0) {
                                    NotificationCenter.getInstance(intentAccount[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    push_msg_id3 = push_msg_id4;
                                    push_user_id2 = userId;
                                    chatId = 0;
                                    z3 = false;
                                } else if (encId != 0) {
                                    NotificationCenter.getInstance(intentAccount[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    push_enc_id3 = encId;
                                    push_msg_id3 = push_msg_id4;
                                    chatId = 0;
                                    z3 = false;
                                } else {
                                    showDialogsList = true;
                                    push_msg_id3 = push_msg_id4;
                                    chatId = 0;
                                    z3 = false;
                                }
                            }
                        }
                        push_msg_id = push_msg_id3;
                        i = z3;
                        videoCallUser = false;
                        newContactName = null;
                        newContactPhone = null;
                        push_enc_id = push_enc_id3;
                        open_widget_edit = open_widget_edit2;
                        open_widget_edit_type = open_widget_edit_type3;
                        push_user_id = push_user_id2;
                        push_chat_id = chatId;
                        searchQuery = null;
                        callSearchQuery = null;
                    } else {
                        intentAccount = intentAccount4;
                        action = str4;
                        if (intent.getAction().equals("com.tmessages.openplayer")) {
                            showPlayer = true;
                            videoCallUser = false;
                            searchQuery = null;
                            callSearchQuery = null;
                            newContactName = null;
                            newContactPhone = null;
                            push_enc_id = 0;
                            open_widget_edit = -1;
                            open_widget_edit_type = -1;
                            push_user_id = 0;
                            push_chat_id = 0;
                            push_msg_id = 0;
                            i = false;
                        } else if (intent.getAction().equals("org.tmessages.openlocations")) {
                            showLocations = true;
                            videoCallUser = false;
                            searchQuery = null;
                            callSearchQuery = null;
                            newContactName = null;
                            newContactPhone = null;
                            push_enc_id = 0;
                            open_widget_edit = -1;
                            open_widget_edit_type = -1;
                            push_user_id = 0;
                            push_chat_id = 0;
                            push_msg_id = 0;
                            i = false;
                        } else {
                            action2 = action4;
                            if (action2.equals("voip_chat")) {
                                showGroupVoip = true;
                                videoCallUser = false;
                                searchQuery = null;
                                callSearchQuery = null;
                                newContactName = null;
                                newContactPhone = null;
                                push_enc_id = 0;
                                open_widget_edit = -1;
                                open_widget_edit_type = -1;
                                push_user_id = 0;
                                push_chat_id = 0;
                                push_msg_id = 0;
                                i = false;
                            }
                        }
                    }
                } else {
                    Uri data = intent.getData();
                    if (data == null) {
                        intentAccount2 = intentAccount4;
                        str = str4;
                        launchActivity = this;
                        j = 0;
                        message = false;
                        push_chat_id3 = 0;
                        push_msg_id5 = 0;
                        z4 = false;
                    } else {
                        String botUser = null;
                        String botChat = null;
                        String botChannel3 = null;
                        String botChatAdminParams2 = null;
                        String message4 = null;
                        String phone4 = null;
                        String game3 = null;
                        String voicechat3 = null;
                        String livestream3 = null;
                        String phoneHash4 = null;
                        String lang3 = null;
                        String theme3 = null;
                        String code3 = null;
                        TLRPC.TL_wallPaper wallPaper2 = null;
                        String inputInvoiceSlug2 = null;
                        Integer messageId3 = null;
                        Long channelId3 = null;
                        Integer threadId3 = null;
                        Integer commentId2 = null;
                        int videoTimestamp2 = -1;
                        boolean hasUrl2 = false;
                        String setAsAttachBot2 = null;
                        String attachMenuBotToOpen3 = null;
                        String attachMenuBotChoose2 = null;
                        HashMap<String, String> auth2 = null;
                        String scheme3 = data.getScheme();
                        String username2 = null;
                        String login = null;
                        if (scheme3 != null) {
                            group = null;
                            switch (scheme3.hashCode()) {
                                case 3699:
                                    if (scheme3.equals("tg")) {
                                        c = 2;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 3213448:
                                    if (scheme3.equals("http")) {
                                        c = 0;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 99617003:
                                    if (scheme3.equals("https")) {
                                        c = 1;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                default:
                                    c = 65535;
                                    break;
                            }
                            sticker = null;
                            switch (c) {
                                case 0:
                                case 1:
                                    intentAccount3 = intentAccount4;
                                    action3 = action4;
                                    scheme = scheme3;
                                    String host = data.getHost().toLowerCase();
                                    if (!host.equals("telegram.me") && !host.equals("t.me") && !host.equals("telegram.dog")) {
                                        j = 0;
                                        break;
                                    } else {
                                        String path2 = data.getPath();
                                        if (path2 != null && path2.length() > 1) {
                                            String path3 = path2.substring(1);
                                            if (path3.startsWith("$")) {
                                                inputInvoiceSlug2 = path3.substring(1);
                                                group2 = null;
                                                j = 0;
                                            } else if (path3.startsWith("invoice/")) {
                                                inputInvoiceSlug2 = path3.substring(path3.indexOf(47) + 1);
                                                group2 = null;
                                                j = 0;
                                            } else if (path3.startsWith("bg/")) {
                                                TLRPC.TL_wallPaper wallPaper3 = new TLRPC.TL_wallPaper();
                                                wallPaper3.settings = new TLRPC.TL_wallPaperSettings();
                                                wallPaper3.slug = path3.replace("bg/", str5);
                                                boolean ok4 = false;
                                                if (wallPaper3.slug != null && wallPaper3.slug.length() == 6) {
                                                    try {
                                                        wallPaper3.settings.background_color = Integer.parseInt(wallPaper3.slug, 16) | (-16777216);
                                                        wallPaper3.slug = null;
                                                        ok4 = true;
                                                    } catch (Exception e11) {
                                                    }
                                                } else if (wallPaper3.slug != null && wallPaper3.slug.length() >= 13 && AndroidUtilities.isValidWallChar(wallPaper3.slug.charAt(6))) {
                                                    try {
                                                        wallPaper3.settings.background_color = Integer.parseInt(wallPaper3.slug.substring(0, 6), 16) | (-16777216);
                                                        wallPaper3.settings.second_background_color = Integer.parseInt(wallPaper3.slug.substring(7, 13), 16) | (-16777216);
                                                        if (wallPaper3.slug.length() >= 20 && AndroidUtilities.isValidWallChar(wallPaper3.slug.charAt(13))) {
                                                            wallPaper3.settings.third_background_color = Integer.parseInt(wallPaper3.slug.substring(14, 20), 16) | (-16777216);
                                                        }
                                                        if (wallPaper3.slug.length() == 27 && AndroidUtilities.isValidWallChar(wallPaper3.slug.charAt(20))) {
                                                            wallPaper3.settings.fourth_background_color = Integer.parseInt(wallPaper3.slug.substring(21), 16) | (-16777216);
                                                        }
                                                        try {
                                                            String rotation = data.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(rotation)) {
                                                                wallPaper3.settings.rotation = Utilities.parseInt((CharSequence) rotation).intValue();
                                                            }
                                                        } catch (Exception e12) {
                                                        }
                                                        try {
                                                            wallPaper3.slug = null;
                                                            ok4 = true;
                                                        } catch (Exception e13) {
                                                        }
                                                    } catch (Exception e14) {
                                                    }
                                                }
                                                if (!ok4) {
                                                    String mode = data.getQueryParameter("mode");
                                                    if (mode != null) {
                                                        String[] modes = mode.toLowerCase().split(" ");
                                                        if (modes != null && modes.length > 0) {
                                                            for (int a4 = 0; a4 < modes.length; a4++) {
                                                                if ("blur".equals(modes[a4])) {
                                                                    wallPaper3.settings.blur = true;
                                                                } else if ("motion".equals(modes[a4])) {
                                                                    wallPaper3.settings.motion = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    String intensity = data.getQueryParameter("intensity");
                                                    if (!TextUtils.isEmpty(intensity)) {
                                                        wallPaper3.settings.intensity = Utilities.parseInt((CharSequence) intensity).intValue();
                                                    } else {
                                                        wallPaper3.settings.intensity = 50;
                                                    }
                                                    try {
                                                        String bgColor = data.getQueryParameter("bg_color");
                                                        try {
                                                            if (!TextUtils.isEmpty(bgColor)) {
                                                                wallPaper3.settings.background_color = Integer.parseInt(bgColor.substring(0, 6), 16) | (-16777216);
                                                                if (bgColor.length() >= 13) {
                                                                    wallPaper3.settings.second_background_color = Integer.parseInt(bgColor.substring(7, 13), 16) | (-16777216);
                                                                    if (bgColor.length() >= 20 && AndroidUtilities.isValidWallChar(bgColor.charAt(13))) {
                                                                        wallPaper3.settings.third_background_color = Integer.parseInt(bgColor.substring(14, 20), 16) | (-16777216);
                                                                    }
                                                                    if (bgColor.length() == 27 && AndroidUtilities.isValidWallChar(bgColor.charAt(20))) {
                                                                        wallPaper3.settings.fourth_background_color = Integer.parseInt(bgColor.substring(21), 16) | (-16777216);
                                                                    }
                                                                }
                                                            } else {
                                                                wallPaper3.settings.background_color = -1;
                                                            }
                                                        } catch (Exception e15) {
                                                        }
                                                    } catch (Exception e16) {
                                                    }
                                                    try {
                                                        String rotation2 = data.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(rotation2)) {
                                                            wallPaper3.settings.rotation = Utilities.parseInt((CharSequence) rotation2).intValue();
                                                        }
                                                    } catch (Exception e17) {
                                                    }
                                                }
                                                wallPaper2 = wallPaper3;
                                                group2 = null;
                                                j = 0;
                                            } else if (path3.startsWith("login/")) {
                                                int intCode = Utilities.parseInt((CharSequence) path3.replace("login/", str5)).intValue();
                                                if (intCode != 0) {
                                                    code3 = str5 + intCode;
                                                }
                                                group2 = null;
                                                j = 0;
                                            } else if (path3.startsWith("joinchat/")) {
                                                group2 = path3.replace("joinchat/", str5);
                                                j = 0;
                                            } else if (path3.startsWith("+")) {
                                                group2 = path3.replace("+", str5);
                                                if (AndroidUtilities.isNumeric(group2)) {
                                                    group2 = null;
                                                    username2 = group2;
                                                    j = 0;
                                                } else {
                                                    j = 0;
                                                }
                                            } else if (path3.startsWith("addstickers/")) {
                                                sticker = path3.replace("addstickers/", str5);
                                                group2 = null;
                                                j = 0;
                                            } else {
                                                if (path3.startsWith("msg/")) {
                                                    j = 0;
                                                } else if (path3.startsWith("share/")) {
                                                    j = 0;
                                                } else if (path3.startsWith("confirmphone")) {
                                                    phone4 = data.getQueryParameter("phone");
                                                    phoneHash4 = data.getQueryParameter("hash");
                                                    group2 = null;
                                                    j = 0;
                                                } else if (path3.startsWith("setlanguage/")) {
                                                    lang3 = path3.substring(12);
                                                    group2 = null;
                                                    j = 0;
                                                } else if (path3.startsWith("addtheme/")) {
                                                    theme3 = path3.substring(9);
                                                    group2 = null;
                                                    j = 0;
                                                } else if (path3.startsWith("c/")) {
                                                    List<String> segments = data.getPathSegments();
                                                    if (segments.size() == 3) {
                                                        Long channelId4 = Utilities.parseLong(segments.get(1));
                                                        Integer messageId4 = Utilities.parseInt((CharSequence) segments.get(2));
                                                        if (messageId4.intValue() != 0) {
                                                            j = 0;
                                                            if (channelId4.longValue() != 0) {
                                                                channelId3 = channelId4;
                                                                messageId3 = messageId4;
                                                                threadId3 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                                if (threadId3.intValue() == 0) {
                                                                    threadId3 = null;
                                                                }
                                                            }
                                                        } else {
                                                            j = 0;
                                                        }
                                                        channelId3 = null;
                                                        messageId3 = null;
                                                        threadId3 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (threadId3.intValue() == 0) {
                                                        }
                                                    } else {
                                                        j = 0;
                                                    }
                                                    group2 = null;
                                                } else {
                                                    j = 0;
                                                    if (path3.length() >= 1) {
                                                        ArrayList<String> segments2 = new ArrayList<>(data.getPathSegments());
                                                        if (segments2.size() > 0) {
                                                            i4 = 0;
                                                            if (segments2.get(0).equals("s")) {
                                                                segments2.remove(0);
                                                            }
                                                        } else {
                                                            i4 = 0;
                                                        }
                                                        if (segments2.size() <= 0) {
                                                            username = null;
                                                        } else {
                                                            username = segments2.get(i4);
                                                            if (segments2.size() > 1) {
                                                                messageId3 = Utilities.parseInt((CharSequence) segments2.get(1));
                                                                if (messageId3.intValue() == 0) {
                                                                    messageId3 = null;
                                                                }
                                                            }
                                                        }
                                                        if (messageId3 != null) {
                                                            videoTimestamp2 = getTimestampFromLink(data);
                                                        }
                                                        botUser = data.getQueryParameter(TtmlNode.START);
                                                        botChat = data.getQueryParameter("startgroup");
                                                        botChannel3 = data.getQueryParameter("startchannel");
                                                        botChatAdminParams2 = data.getQueryParameter("admin");
                                                        game3 = data.getQueryParameter("game");
                                                        voicechat3 = data.getQueryParameter("voicechat");
                                                        livestream3 = data.getQueryParameter("livestream");
                                                        setAsAttachBot2 = data.getQueryParameter("startattach");
                                                        attachMenuBotChoose2 = data.getQueryParameter("choose");
                                                        attachMenuBotToOpen3 = data.getQueryParameter("attach");
                                                        Integer threadId4 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        if (threadId4.intValue() != 0) {
                                                            threadId3 = threadId4;
                                                        } else {
                                                            threadId3 = null;
                                                        }
                                                        commentId2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                        if (commentId2.intValue() != 0) {
                                                            username2 = username;
                                                            group2 = null;
                                                        } else {
                                                            commentId2 = null;
                                                            username2 = username;
                                                            group2 = null;
                                                        }
                                                    }
                                                }
                                                String message5 = data.getQueryParameter(ImagesContract.URL);
                                                if (message5 == null) {
                                                    message5 = "";
                                                }
                                                if (data.getQueryParameter("text") != null) {
                                                    if (message5.length() > 0) {
                                                        hasUrl2 = true;
                                                        message5 = message5 + "\n";
                                                    }
                                                    message5 = message5 + data.getQueryParameter("text");
                                                }
                                                if (message5.length() <= 16384) {
                                                    i3 = 0;
                                                } else {
                                                    i3 = 0;
                                                    message5 = message5.substring(0, 16384);
                                                }
                                                while (message5.endsWith("\n")) {
                                                    message5 = message5.substring(i3, message5.length() - 1);
                                                }
                                                message4 = message5;
                                                group2 = null;
                                            }
                                            group = group2;
                                            message3 = message4;
                                            phone2 = phone4;
                                            phoneHash2 = phoneHash4;
                                            channelId = channelId3;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = videoTimestamp2;
                                            hasUrl = hasUrl2;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = lang3;
                                            theme = theme3;
                                            code = code3;
                                            wallPaper = wallPaper2;
                                            attachMenuBotToOpen2 = inputInvoiceSlug2;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else {
                                            j = 0;
                                        }
                                        group2 = null;
                                        group = group2;
                                        message3 = message4;
                                        phone2 = phone4;
                                        phoneHash2 = phoneHash4;
                                        channelId = channelId3;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = videoTimestamp2;
                                        hasUrl = hasUrl2;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = lang3;
                                        theme = theme3;
                                        code = code3;
                                        wallPaper = wallPaper2;
                                        attachMenuBotToOpen2 = inputInvoiceSlug2;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                    }
                                    break;
                                case 2:
                                    String url2 = data.toString();
                                    action3 = action4;
                                    if (!url2.startsWith("tg:resolve") && !url2.startsWith("tg://resolve")) {
                                        intentAccount3 = intentAccount4;
                                        scheme = scheme3;
                                        auth = null;
                                    } else {
                                        String url3 = url2.replace("tg:resolve", "tg://telegram.org").replace("tg://resolve", "tg://telegram.org");
                                        data = Uri.parse(url3);
                                        String username3 = data.getQueryParameter("domain");
                                        if (username3 != null) {
                                            url = url3;
                                            intentAccount3 = intentAccount4;
                                        } else {
                                            username3 = data.getQueryParameter("phone");
                                            if (username3 != null) {
                                                url = url3;
                                                if (!username3.startsWith("+")) {
                                                    intentAccount3 = intentAccount4;
                                                } else {
                                                    intentAccount3 = intentAccount4;
                                                    username3 = username3.substring(1);
                                                }
                                            } else {
                                                url = url3;
                                                intentAccount3 = intentAccount4;
                                            }
                                        }
                                        if ("telegrampassport".equals(username3)) {
                                            HashMap<String, String> auth3 = new HashMap<>();
                                            String scope = data.getQueryParameter("scope");
                                            if (!TextUtils.isEmpty(scope)) {
                                                username2 = null;
                                                if (!scope.startsWith("{") || !scope.endsWith("}")) {
                                                    scheme = scheme3;
                                                } else {
                                                    scheme = scheme3;
                                                    auth3.put("nonce", data.getQueryParameter("nonce"));
                                                    auth3.put("bot_id", data.getQueryParameter("bot_id"));
                                                    auth3.put("scope", scope);
                                                    auth3.put("public_key", data.getQueryParameter("public_key"));
                                                    auth3.put("callback_url", data.getQueryParameter("callback_url"));
                                                    auth = auth3;
                                                    url2 = url;
                                                }
                                            } else {
                                                username2 = null;
                                                scheme = scheme3;
                                            }
                                            auth3.put("payload", data.getQueryParameter("payload"));
                                            auth3.put("bot_id", data.getQueryParameter("bot_id"));
                                            auth3.put("scope", scope);
                                            auth3.put("public_key", data.getQueryParameter("public_key"));
                                            auth3.put("callback_url", data.getQueryParameter("callback_url"));
                                            auth = auth3;
                                            url2 = url;
                                        } else {
                                            scheme = scheme3;
                                            botUser = data.getQueryParameter(TtmlNode.START);
                                            botChat = data.getQueryParameter("startgroup");
                                            botChannel3 = data.getQueryParameter("startchannel");
                                            botChatAdminParams2 = data.getQueryParameter("admin");
                                            game3 = data.getQueryParameter("game");
                                            voicechat3 = data.getQueryParameter("voicechat");
                                            livestream3 = data.getQueryParameter("livestream");
                                            setAsAttachBot2 = data.getQueryParameter("startattach");
                                            attachMenuBotChoose2 = data.getQueryParameter("choose");
                                            attachMenuBotToOpen3 = data.getQueryParameter("attach");
                                            Integer messageId5 = Utilities.parseInt((CharSequence) data.getQueryParameter("post"));
                                            if (messageId5.intValue() != 0) {
                                                messageId3 = messageId5;
                                            } else {
                                                messageId3 = null;
                                            }
                                            Integer threadId5 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                            if (threadId5.intValue() != 0) {
                                                threadId3 = threadId5;
                                            } else {
                                                threadId3 = null;
                                            }
                                            commentId2 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                            if (commentId2.intValue() != 0) {
                                                username2 = username3;
                                                auth = null;
                                                url2 = url;
                                            } else {
                                                commentId2 = null;
                                                username2 = username3;
                                                auth = null;
                                                url2 = url;
                                            }
                                        }
                                    }
                                    if (url2.startsWith("tg:invoice") || url2.startsWith("tg://invoice")) {
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = Uri.parse(url2.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:privatepost") || url2.startsWith("tg://privatepost")) {
                                        Uri data2 = Uri.parse(url2.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                        Integer messageId6 = Utilities.parseInt((CharSequence) data2.getQueryParameter("post"));
                                        Long channelId5 = Utilities.parseLong(data2.getQueryParameter("channel"));
                                        if (messageId6.intValue() == 0 || channelId5.longValue() == 0) {
                                            messageId2 = null;
                                            channelId2 = null;
                                        } else {
                                            messageId2 = messageId6;
                                            channelId2 = channelId5;
                                        }
                                        Integer threadId6 = Utilities.parseInt((CharSequence) data2.getQueryParameter("thread"));
                                        if (threadId6.intValue() != 0) {
                                            threadId2 = threadId6;
                                        } else {
                                            threadId2 = null;
                                        }
                                        Integer commentId3 = Utilities.parseInt((CharSequence) data2.getQueryParameter("comment"));
                                        if (commentId3.intValue() == 0) {
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = channelId2;
                                            threadId = threadId2;
                                            commentId = null;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId2;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else {
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = channelId2;
                                            threadId = threadId2;
                                            commentId = commentId3;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId2;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        }
                                    } else if (url2.startsWith("tg:bg") || url2.startsWith("tg://bg")) {
                                        Uri data3 = Uri.parse(url2.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                        TLRPC.TL_wallPaper wallPaper4 = new TLRPC.TL_wallPaper();
                                        wallPaper4.settings = new TLRPC.TL_wallPaperSettings();
                                        wallPaper4.slug = data3.getQueryParameter("slug");
                                        if (wallPaper4.slug == null) {
                                            wallPaper4.slug = data3.getQueryParameter(TtmlNode.ATTR_TTS_COLOR);
                                        }
                                        boolean ok5 = false;
                                        if (wallPaper4.slug != null && wallPaper4.slug.length() == 6) {
                                            try {
                                                wallPaper4.settings.background_color = Integer.parseInt(wallPaper4.slug, 16) | (-16777216);
                                                wallPaper4.slug = null;
                                                ok5 = true;
                                            } catch (Exception e18) {
                                            }
                                        } else if (wallPaper4.slug != null && wallPaper4.slug.length() >= 13 && AndroidUtilities.isValidWallChar(wallPaper4.slug.charAt(6))) {
                                            try {
                                                wallPaper4.settings.background_color = Integer.parseInt(wallPaper4.slug.substring(0, 6), 16) | (-16777216);
                                                wallPaper4.settings.second_background_color = Integer.parseInt(wallPaper4.slug.substring(7, 13), 16) | (-16777216);
                                                if (wallPaper4.slug.length() >= 20 && AndroidUtilities.isValidWallChar(wallPaper4.slug.charAt(13))) {
                                                    wallPaper4.settings.third_background_color = Integer.parseInt(wallPaper4.slug.substring(14, 20), 16) | (-16777216);
                                                }
                                                if (wallPaper4.slug.length() == 27 && AndroidUtilities.isValidWallChar(wallPaper4.slug.charAt(20))) {
                                                    wallPaper4.settings.fourth_background_color = Integer.parseInt(wallPaper4.slug.substring(21), 16) | (-16777216);
                                                }
                                                try {
                                                    String rotation3 = data3.getQueryParameter("rotation");
                                                    if (!TextUtils.isEmpty(rotation3)) {
                                                        wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) rotation3).intValue();
                                                    }
                                                } catch (Exception e19) {
                                                }
                                                wallPaper4.slug = null;
                                                ok5 = true;
                                            } catch (Exception e20) {
                                            }
                                        }
                                        if (!ok5) {
                                            String mode2 = data3.getQueryParameter("mode");
                                            if (mode2 != null) {
                                                String[] modes2 = mode2.toLowerCase().split(" ");
                                                if (modes2 != null && modes2.length > 0) {
                                                    for (int a5 = 0; a5 < modes2.length; a5++) {
                                                        if ("blur".equals(modes2[a5])) {
                                                            wallPaper4.settings.blur = true;
                                                        } else if ("motion".equals(modes2[a5])) {
                                                            wallPaper4.settings.motion = true;
                                                        }
                                                    }
                                                }
                                            }
                                            wallPaper4.settings.intensity = Utilities.parseInt((CharSequence) data3.getQueryParameter("intensity")).intValue();
                                            try {
                                                String bgColor2 = data3.getQueryParameter("bg_color");
                                                if (!TextUtils.isEmpty(bgColor2)) {
                                                    wallPaper4.settings.background_color = Integer.parseInt(bgColor2.substring(0, 6), 16) | (-16777216);
                                                    if (bgColor2.length() >= 13) {
                                                        wallPaper4.settings.second_background_color = Integer.parseInt(bgColor2.substring(8, 13), 16) | (-16777216);
                                                        if (bgColor2.length() >= 20 && AndroidUtilities.isValidWallChar(bgColor2.charAt(13))) {
                                                            wallPaper4.settings.third_background_color = Integer.parseInt(bgColor2.substring(14, 20), 16) | (-16777216);
                                                        }
                                                        if (bgColor2.length() == 27 && AndroidUtilities.isValidWallChar(bgColor2.charAt(20))) {
                                                            wallPaper4.settings.fourth_background_color = Integer.parseInt(bgColor2.substring(21), 16) | (-16777216);
                                                        }
                                                    }
                                                }
                                            } catch (Exception e21) {
                                            }
                                            try {
                                                String rotation4 = data3.getQueryParameter("rotation");
                                                if (!TextUtils.isEmpty(rotation4)) {
                                                    wallPaper4.settings.rotation = Utilities.parseInt((CharSequence) rotation4).intValue();
                                                }
                                            } catch (Exception e22) {
                                            }
                                        }
                                        auth2 = auth;
                                        botChannel = botChannel3;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        wallPaper = wallPaper4;
                                        botChannel2 = null;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:join") || url2.startsWith("tg://join")) {
                                        group = Uri.parse(url2.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:addstickers") || url2.startsWith("tg://addstickers")) {
                                        sticker = Uri.parse(url2.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:msg") || url2.startsWith("tg://msg") || url2.startsWith("tg://share") || url2.startsWith("tg:share")) {
                                        Uri data4 = Uri.parse(url2.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                        String message6 = data4.getQueryParameter(ImagesContract.URL);
                                        if (message6 == null) {
                                            message6 = "";
                                        }
                                        if (data4.getQueryParameter("text") != null) {
                                            if (message6.length() > 0) {
                                                hasUrl2 = true;
                                                message6 = message6 + "\n";
                                            }
                                            message6 = message6 + data4.getQueryParameter("text");
                                        }
                                        if (message6.length() <= 16384) {
                                            i5 = 0;
                                        } else {
                                            i5 = 0;
                                            message6 = message6.substring(0, 16384);
                                        }
                                        while (message6.endsWith("\n")) {
                                            message6 = message6.substring(i5, message6.length() - 1);
                                        }
                                        message3 = message6;
                                        auth2 = auth;
                                        message2 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = hasUrl2;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:confirmphone") || url2.startsWith("tg://confirmphone")) {
                                        Uri data5 = Uri.parse(url2.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = data5.getQueryParameter("phone");
                                        phoneHash2 = data5.getQueryParameter("hash");
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:login") || url2.startsWith("tg://login")) {
                                        Uri data6 = Uri.parse(url2.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                        String login2 = data6.getQueryParameter("token");
                                        int intCode2 = Utilities.parseInt((CharSequence) data6.getQueryParameter("code")).intValue();
                                        if (intCode2 != 0) {
                                            code3 = str5 + intCode2;
                                        }
                                        login = login2;
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = code3;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:openmessage") || url2.startsWith("tg://openmessage")) {
                                        Uri data7 = Uri.parse(url2.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                        String userID = data7.getQueryParameter("user_id");
                                        String chatID = data7.getQueryParameter(ChatReactionsEditActivity.KEY_CHAT_ID);
                                        String msgID = data7.getQueryParameter(str4);
                                        if (userID != null) {
                                            try {
                                                push_user_id2 = Long.parseLong(userID);
                                            } catch (NumberFormatException e23) {
                                            }
                                        } else if (chatID != null) {
                                            try {
                                                push_chat_id2 = Long.parseLong(chatID);
                                            } catch (NumberFormatException e24) {
                                            }
                                        }
                                        if (msgID != null) {
                                            try {
                                                push_msg_id6 = Integer.parseInt(msgID);
                                            } catch (NumberFormatException e25) {
                                            }
                                            push_msg_id2 = push_msg_id6;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        }
                                        push_msg_id6 = 0;
                                        push_msg_id2 = push_msg_id6;
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                    } else if (url2.startsWith("tg:passport") || url2.startsWith("tg://passport") || url2.startsWith("tg:secureid")) {
                                        Uri data8 = Uri.parse(url2.replace("tg:passport", "tg://telegram.org").replace("tg://passport", "tg://telegram.org").replace("tg:secureid", "tg://telegram.org"));
                                        HashMap<String, String> auth4 = new HashMap<>();
                                        String scope2 = data8.getQueryParameter("scope");
                                        if (!TextUtils.isEmpty(scope2) && scope2.startsWith("{") && scope2.endsWith("}")) {
                                            auth4.put("nonce", data8.getQueryParameter("nonce"));
                                        } else {
                                            auth4.put("payload", data8.getQueryParameter("payload"));
                                        }
                                        auth4.put("bot_id", data8.getQueryParameter("bot_id"));
                                        auth4.put("scope", scope2);
                                        auth4.put("public_key", data8.getQueryParameter("public_key"));
                                        auth4.put("callback_url", data8.getQueryParameter("callback_url"));
                                        auth2 = auth4;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:setlanguage") || url2.startsWith("tg://setlanguage")) {
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = Uri.parse(url2.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:addtheme") || url2.startsWith("tg://addtheme")) {
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = Uri.parse(url2.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:settings") || url2.startsWith("tg://settings")) {
                                        if (url2.contains("themes")) {
                                            z2 = true;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else if (url2.contains("devices")) {
                                            z2 = true;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else if (url2.contains("folders")) {
                                            z2 = true;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else if (url2.contains("change_number")) {
                                            z2 = true;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else {
                                            z2 = true;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        }
                                    } else if (url2.startsWith("tg:search") || url2.startsWith("tg://search")) {
                                        String searchQuery6 = Uri.parse(url2.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter(SearchIntents.EXTRA_QUERY);
                                        if (searchQuery6 != null) {
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = searchQuery6.trim();
                                            unsupportedUrl = false;
                                            break;
                                        } else {
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = "";
                                            unsupportedUrl = false;
                                            break;
                                        }
                                    } else if (url2.startsWith("tg:calllog") || url2.startsWith("tg://calllog")) {
                                        auth2 = auth;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        unsupportedUrl = true;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        message3 = null;
                                        message2 = null;
                                        break;
                                    } else if (url2.startsWith("tg:call") || url2.startsWith("tg://call")) {
                                        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = null;
                                            phone = null;
                                            phoneHash = null;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = null;
                                            livestream2 = null;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = false;
                                            videoCallUser2 = false;
                                            botChannel2 = null;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        } else {
                                            if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                String callFormat = data.getQueryParameter("format");
                                                String callUserName = data.getQueryParameter(CommonProperties.NAME);
                                                String callPhone = data.getQueryParameter("phone");
                                                List<TLRPC.TL_contact> contacts = findContacts(callUserName, callPhone, false);
                                                if (contacts.isEmpty() && callPhone != null) {
                                                    newContactName2 = callUserName;
                                                    newContactPhone2 = callPhone;
                                                    newContactAlert = 1;
                                                } else {
                                                    if (contacts.size() == 1) {
                                                        push_user_id2 = contacts.get(0).user_id;
                                                    }
                                                    if (push_user_id2 == 0) {
                                                        if (callUserName != null) {
                                                            str5 = callUserName;
                                                        }
                                                        callSearchQuery3 = str5;
                                                    }
                                                    if ("video".equalsIgnoreCase(callFormat)) {
                                                        videoCallUser3 = true;
                                                    } else {
                                                        audioCallUser2 = true;
                                                    }
                                                    needCallAlert = 1;
                                                }
                                                push_user_id3 = push_user_id2;
                                            } else {
                                                final Intent copyIntent = new Intent(intent);
                                                copyIntent.removeExtra(EXTRA_ACTION_TOKEN);
                                                copyIntent.putExtra("extra_force_call", true);
                                                ContactsLoadingObserver.observe(new ContactsLoadingObserver.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
                                                    @Override // org.telegram.messenger.ContactsLoadingObserver.Callback
                                                    public final void onResult(boolean z6) {
                                                        LaunchActivity.this.m3605lambda$handleIntent$10$orgtelegramuiLaunchActivity(copyIntent, z6);
                                                    }
                                                }, 1000L);
                                                push_user_id3 = 0;
                                            }
                                            push_user_id2 = push_user_id3;
                                            auth2 = auth;
                                            message3 = null;
                                            phone2 = null;
                                            phoneHash2 = null;
                                            channelId = null;
                                            threadId = threadId3;
                                            commentId = commentId2;
                                            videoTimestamp = -1;
                                            hasUrl = false;
                                            setAsAttachBot = setAsAttachBot2;
                                            attachMenuBotToOpen = attachMenuBotToOpen3;
                                            attachMenuBotChoose = attachMenuBotChoose2;
                                            j = 0;
                                            message2 = newContactAlert;
                                            phone = null;
                                            phoneHash = newContactPhone2;
                                            voicechat = voicechat3;
                                            livestream = livestream3;
                                            lang = null;
                                            theme = null;
                                            code = null;
                                            wallPaper = null;
                                            attachMenuBotToOpen2 = null;
                                            messageId = messageId3;
                                            voicechat2 = callSearchQuery3;
                                            livestream2 = newContactName2;
                                            lang2 = null;
                                            theme2 = botUser;
                                            code2 = botChat;
                                            botChannel = botChannel3;
                                            inputInvoiceSlug = botChatAdminParams2;
                                            game = game3;
                                            audioCallUser = audioCallUser2;
                                            videoCallUser2 = videoCallUser3;
                                            botChannel2 = needCallAlert;
                                            botChatAdminParams = null;
                                            game2 = null;
                                            unsupportedUrl = false;
                                            break;
                                        }
                                    } else if (url2.startsWith("tg:scanqr") || url2.startsWith("tg://scanqr")) {
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = 1;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else if (url2.startsWith("tg:addcontact") || url2.startsWith("tg://addcontact")) {
                                        Uri data9 = Uri.parse(url2.replace("tg:addcontact", "tg://telegram.org").replace("tg://addcontact", "tg://telegram.org"));
                                        String newContactName4 = data9.getQueryParameter(CommonProperties.NAME);
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = data9.getQueryParameter("phone");
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = newContactName4;
                                        lang2 = null;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = 1;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    } else {
                                        String unsupportedUrl3 = url2.replace("tg://", str5).replace("tg:", str5);
                                        int index = unsupportedUrl3.indexOf(63);
                                        if (index < 0) {
                                            unsupportedUrl2 = unsupportedUrl3;
                                        } else {
                                            unsupportedUrl2 = unsupportedUrl3.substring(0, index);
                                        }
                                        auth2 = auth;
                                        message3 = null;
                                        phone2 = null;
                                        phoneHash2 = null;
                                        channelId = null;
                                        threadId = threadId3;
                                        commentId = commentId2;
                                        videoTimestamp = -1;
                                        hasUrl = false;
                                        setAsAttachBot = setAsAttachBot2;
                                        attachMenuBotToOpen = attachMenuBotToOpen3;
                                        attachMenuBotChoose = attachMenuBotChoose2;
                                        j = 0;
                                        message2 = null;
                                        phone = null;
                                        phoneHash = null;
                                        voicechat = voicechat3;
                                        livestream = livestream3;
                                        lang = null;
                                        theme = null;
                                        code = null;
                                        wallPaper = null;
                                        attachMenuBotToOpen2 = null;
                                        messageId = messageId3;
                                        voicechat2 = null;
                                        livestream2 = null;
                                        lang2 = unsupportedUrl2;
                                        theme2 = botUser;
                                        code2 = botChat;
                                        botChannel = botChannel3;
                                        inputInvoiceSlug = botChatAdminParams2;
                                        game = game3;
                                        audioCallUser = false;
                                        videoCallUser2 = false;
                                        botChannel2 = null;
                                        botChatAdminParams = null;
                                        game2 = null;
                                        unsupportedUrl = false;
                                        break;
                                    }
                                    break;
                                default:
                                    intentAccount3 = intentAccount4;
                                    action3 = action4;
                                    scheme = scheme3;
                                    j = 0;
                                    break;
                            }
                            if (intent.hasExtra(EXTRA_ACTION_TOKEN)) {
                                scheme2 = scheme;
                            } else {
                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                    scheme2 = scheme;
                                    if ("tg".equals(scheme2) && lang2 == null) {
                                        success = true;
                                        Action assistAction = new AssistActionBuilder().setActionToken(intent.getStringExtra(EXTRA_ACTION_TOKEN)).setActionStatus(!success ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build();
                                        FirebaseUserActions.getInstance(this).end(assistAction);
                                        intent.removeExtra(EXTRA_ACTION_TOKEN);
                                    }
                                } else {
                                    scheme2 = scheme;
                                }
                                success = false;
                                Action assistAction2 = new AssistActionBuilder().setActionToken(intent.getStringExtra(EXTRA_ACTION_TOKEN)).setActionStatus(!success ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build();
                                FirebaseUserActions.getInstance(this).end(assistAction2);
                                intent.removeExtra(EXTRA_ACTION_TOKEN);
                            }
                            if (code != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                str = str4;
                                launchActivity = this;
                                intentAccount2 = intentAccount3;
                            } else {
                                if (phone2 == null) {
                                    phoneHash3 = phoneHash2;
                                    phone3 = phone2;
                                    str = str4;
                                    intentAccount2 = intentAccount3;
                                } else if (phoneHash2 != null) {
                                    phoneHash3 = phoneHash2;
                                    phone3 = phone2;
                                    str = str4;
                                    intentAccount2 = intentAccount3;
                                } else {
                                    if (username2 != null || group != null || sticker != null || message3 != null || game != null || voicechat != null || auth2 != null || lang2 != null || lang != null || code != null || wallPaper != null || attachMenuBotToOpen2 != null || channelId != null || theme != null) {
                                        str2 = str4;
                                    } else if (login != null) {
                                        str2 = str4;
                                    } else {
                                        try {
                                            Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
                                            try {
                                                if (cursor == null) {
                                                    str3 = str4;
                                                } else {
                                                    try {
                                                        if (!cursor.moveToFirst()) {
                                                            str3 = str4;
                                                        } else {
                                                            int accountId = Utilities.parseInt((CharSequence) cursor.getString(cursor.getColumnIndex("account_name"))).intValue();
                                                            int a6 = 0;
                                                            while (true) {
                                                                if (a6 < 4) {
                                                                    str3 = str4;
                                                                    if (UserConfig.getInstance(a6).getClientUserId() == accountId) {
                                                                        intentAccount3[0] = a6;
                                                                        switchToAccount(intentAccount3[0], true);
                                                                    } else {
                                                                        try {
                                                                            a6++;
                                                                            str4 = str3;
                                                                        } catch (Throwable th3) {
                                                                            th2 = th3;
                                                                            th = th2;
                                                                            if (cursor != null) {
                                                                            }
                                                                            throw th;
                                                                        }
                                                                    }
                                                                } else {
                                                                    str3 = str4;
                                                                }
                                                            }
                                                            long userId2 = cursor.getLong(cursor.getColumnIndex("data4"));
                                                            NotificationCenter.getInstance(intentAccount3[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                            try {
                                                                String mimeType = cursor.getString(cursor.getColumnIndex("mimetype"));
                                                                if (TextUtils.equals(mimeType, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                                    push_user_id2 = userId2;
                                                                    audioCallUser = true;
                                                                } else if (TextUtils.equals(mimeType, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                                    push_user_id2 = userId2;
                                                                    videoCallUser2 = true;
                                                                } else {
                                                                    push_user_id2 = userId2;
                                                                }
                                                            } catch (Throwable th4) {
                                                                th = th4;
                                                                if (cursor != null) {
                                                                    try {
                                                                        cursor.close();
                                                                    } catch (Throwable th5) {
                                                                    }
                                                                }
                                                                throw th;
                                                            }
                                                        }
                                                    } catch (Throwable th6) {
                                                        th2 = th6;
                                                    }
                                                }
                                                if (cursor != null) {
                                                    cursor.close();
                                                }
                                                launchActivity = this;
                                                str = str3;
                                                push_chat_id3 = push_chat_id2;
                                                message = unsupportedUrl;
                                                audioCallUser2 = audioCallUser;
                                                videoCallUser3 = videoCallUser2;
                                                needCallAlert = botChannel2;
                                                push_msg_id5 = push_msg_id2;
                                                z4 = z2;
                                                newContact = botChatAdminParams;
                                                newContactAlert = message2;
                                                scanQr = phone;
                                                searchQuery5 = game2;
                                                callSearchQuery3 = voicechat2;
                                                newContactName2 = livestream2;
                                                newContactPhone2 = phoneHash;
                                                intentAccount2 = intentAccount3;
                                            } catch (Exception e26) {
                                                e = e26;
                                                FileLog.e(e);
                                                launchActivity = this;
                                                str = str4;
                                                push_chat_id3 = push_chat_id2;
                                                message = unsupportedUrl;
                                                audioCallUser2 = audioCallUser;
                                                videoCallUser3 = videoCallUser2;
                                                needCallAlert = botChannel2;
                                                push_msg_id5 = push_msg_id2;
                                                z4 = z2;
                                                newContact = botChatAdminParams;
                                                newContactAlert = message2;
                                                scanQr = phone;
                                                searchQuery5 = game2;
                                                callSearchQuery3 = voicechat2;
                                                newContactName2 = livestream2;
                                                newContactPhone2 = phoneHash;
                                                intentAccount2 = intentAccount3;
                                                push_msg_id = push_msg_id5;
                                                i = z4;
                                                showCallLog = message;
                                                videoCallUser = videoCallUser3;
                                                newContactName = newContactName2;
                                                newContactPhone = newContactPhone2;
                                                push_enc_id = 0;
                                                open_widget_edit = -1;
                                                open_widget_edit_type = -1;
                                                push_user_id = push_user_id2;
                                                intentAccount = intentAccount2;
                                                action = str;
                                                push_chat_id = push_chat_id3;
                                                searchQuery = searchQuery5;
                                                callSearchQuery = callSearchQuery3;
                                                final String newContactName322 = newContactName;
                                                if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                }
                                                if (!pushOpened) {
                                                }
                                                if (isVoipIntent) {
                                                }
                                                if (showGroupVoip) {
                                                }
                                                intent2.setAction(null);
                                                return pushOpened;
                                            }
                                        } catch (Exception e27) {
                                            e = e27;
                                        }
                                    }
                                    intentAccount2 = intentAccount3;
                                    str = str2;
                                    runLinkRequest(intentAccount3[0], username2, group, sticker, theme2, code2, botChannel, inputInvoiceSlug, (message3 != null && message3.startsWith("@")) ? " " + message3 : message3, hasUrl, messageId, channelId, threadId, commentId, game, auth2, lang, lang2, code, login, wallPaper, attachMenuBotToOpen2, theme, voicechat, livestream, 0, videoTimestamp, setAsAttachBot, attachMenuBotToOpen, attachMenuBotChoose);
                                    launchActivity = this;
                                }
                                launchActivity = this;
                                final AlertDialog cancelDeleteProgressDialog = new AlertDialog(launchActivity, 3);
                                cancelDeleteProgressDialog.setCanCancel(false);
                                cancelDeleteProgressDialog.show();
                                req = new TLRPC.TL_account_sendConfirmPhoneCode();
                                req.hash = phoneHash3;
                                req.settings = new TLRPC.TL_codeSettings();
                                req.settings.allow_flashcall = false;
                                req.settings.allow_app_hash = ApplicationLoader.hasPlayServices;
                                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                                if (!req.settings.allow_app_hash) {
                                    preferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                                } else {
                                    preferences.edit().remove("sms_hash").apply();
                                }
                                final Bundle params = new Bundle();
                                final String phone5 = phone3;
                                params.putString("phone", phone5);
                                ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        LaunchActivity.this.m3607lambda$handleIntent$12$orgtelegramuiLaunchActivity(cancelDeleteProgressDialog, phone5, params, req, tLObject, tL_error);
                                    }
                                }, 2);
                            }
                            push_chat_id3 = push_chat_id2;
                            message = unsupportedUrl;
                            audioCallUser2 = audioCallUser;
                            videoCallUser3 = videoCallUser2;
                            needCallAlert = botChannel2;
                            push_msg_id5 = push_msg_id2;
                            z4 = z2;
                            newContact = botChatAdminParams;
                            newContactAlert = message2;
                            scanQr = phone;
                            searchQuery5 = game2;
                            callSearchQuery3 = voicechat2;
                            newContactName2 = livestream2;
                            newContactPhone2 = phoneHash;
                        } else {
                            group = null;
                            sticker = null;
                            intentAccount3 = intentAccount4;
                            action3 = action4;
                            scheme = scheme3;
                            j = 0;
                        }
                        message3 = null;
                        phone2 = null;
                        phoneHash2 = null;
                        channelId = null;
                        threadId = null;
                        commentId = null;
                        videoTimestamp = -1;
                        hasUrl = false;
                        setAsAttachBot = null;
                        attachMenuBotToOpen = null;
                        attachMenuBotChoose = null;
                        message2 = null;
                        phone = null;
                        phoneHash = null;
                        voicechat = null;
                        livestream = null;
                        lang = null;
                        theme = null;
                        code = null;
                        wallPaper = null;
                        attachMenuBotToOpen2 = null;
                        messageId = null;
                        voicechat2 = null;
                        livestream2 = null;
                        lang2 = null;
                        theme2 = null;
                        code2 = null;
                        botChannel = null;
                        inputInvoiceSlug = null;
                        game = null;
                        audioCallUser = false;
                        videoCallUser2 = false;
                        botChannel2 = null;
                        botChatAdminParams = null;
                        game2 = null;
                        unsupportedUrl = false;
                        if (intent.hasExtra(EXTRA_ACTION_TOKEN)) {
                        }
                        if (code != null) {
                        }
                        if (phone2 == null) {
                        }
                        launchActivity = this;
                        final AlertDialog cancelDeleteProgressDialog2 = new AlertDialog(launchActivity, 3);
                        cancelDeleteProgressDialog2.setCanCancel(false);
                        cancelDeleteProgressDialog2.show();
                        req = new TLRPC.TL_account_sendConfirmPhoneCode();
                        req.hash = phoneHash3;
                        req.settings = new TLRPC.TL_codeSettings();
                        req.settings.allow_flashcall = false;
                        req.settings.allow_app_hash = ApplicationLoader.hasPlayServices;
                        SharedPreferences preferences2 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        if (!req.settings.allow_app_hash) {
                        }
                        final Bundle params2 = new Bundle();
                        final String phone52 = phone3;
                        params2.putString("phone", phone52);
                        ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.m3607lambda$handleIntent$12$orgtelegramuiLaunchActivity(cancelDeleteProgressDialog2, phone52, params2, req, tLObject, tL_error);
                            }
                        }, 2);
                        push_chat_id3 = push_chat_id2;
                        message = unsupportedUrl;
                        audioCallUser2 = audioCallUser;
                        videoCallUser3 = videoCallUser2;
                        needCallAlert = botChannel2;
                        push_msg_id5 = push_msg_id2;
                        z4 = z2;
                        newContact = botChatAdminParams;
                        newContactAlert = message2;
                        scanQr = phone;
                        searchQuery5 = game2;
                        callSearchQuery3 = voicechat2;
                        newContactName2 = livestream2;
                        newContactPhone2 = phoneHash;
                    }
                    push_msg_id = push_msg_id5;
                    i = z4;
                    showCallLog = message;
                    videoCallUser = videoCallUser3;
                    newContactName = newContactName2;
                    newContactPhone = newContactPhone2;
                    push_enc_id = 0;
                    open_widget_edit = -1;
                    open_widget_edit_type = -1;
                    push_user_id = push_user_id2;
                    intentAccount = intentAccount2;
                    action = str;
                    push_chat_id = push_chat_id3;
                    searchQuery = searchQuery5;
                    callSearchQuery = callSearchQuery3;
                }
            } else {
                if (SharedConfig.directShare && intent != null && intent.getExtras() != null) {
                    push_chat_id4 = 0;
                    showCallLog = false;
                    dialogId = intent.getExtras().getLong("dialogId", 0L);
                    String hash3 = null;
                    if (dialogId != 0) {
                        z5 = false;
                        push_msg_id7 = 0;
                        hash2 = intent.getExtras().getString("hash", null);
                    } else {
                        try {
                            String id = intent.getExtras().getString(ShortcutManagerCompat.EXTRA_SHORTCUT_ID);
                            if (id == null) {
                                hash = null;
                                z5 = false;
                                push_msg_id7 = 0;
                            } else {
                                List<ShortcutInfoCompat> list = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int a7 = 0;
                                int N = list.size();
                                while (true) {
                                    hash = hash3;
                                    int N2 = N;
                                    if (a7 >= N2) {
                                        z5 = false;
                                        push_msg_id7 = 0;
                                        break;
                                    }
                                    try {
                                        ShortcutInfoCompat info2 = list.get(a7);
                                        if (id.equals(info2.getId())) {
                                            break;
                                        }
                                        a7++;
                                        hash3 = hash;
                                        N = N2;
                                    } catch (Throwable th7) {
                                        e4 = th7;
                                        z5 = false;
                                        push_msg_id7 = 0;
                                    }
                                }
                            }
                            hash2 = hash;
                        } catch (Throwable th8) {
                            e4 = th8;
                            hash = null;
                            z5 = false;
                            push_msg_id7 = 0;
                        }
                    }
                    if (SharedConfig.directShareHash != null || !SharedConfig.directShareHash.equals(hash2)) {
                        dialogId = 0;
                    }
                } else {
                    push_chat_id4 = 0;
                    push_msg_id7 = 0;
                    z5 = false;
                    showCallLog = false;
                }
                boolean error5 = false;
                String type2 = intent.getType();
                if (type2 == null && type2.equals("text/x-vcard")) {
                    try {
                        Uri uri4 = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
                        if (uri4 != null) {
                            ArrayList<TLRPC.User> loadVCardFromStream = AndroidUtilities.loadVCardFromStream(uri4, this.currentAccount, false, null, null);
                            this.contactsToSend = loadVCardFromStream;
                            if (loadVCardFromStream.size() > 5) {
                                this.contactsToSend = null;
                                ArrayList<Uri> arrayList = new ArrayList<>();
                                this.documentsUrisArray = arrayList;
                                arrayList.add(uri4);
                                this.documentsMimeType = type2;
                            } else {
                                this.contactsToSendUri = uri4;
                            }
                        } else {
                            error5 = true;
                        }
                    } catch (Exception e28) {
                        FileLog.e(e28);
                        error5 = true;
                    }
                } else {
                    String text2 = intent.getStringExtra("android.intent.extra.TEXT");
                    if (text2 == null && (textSequence = intent.getCharSequenceExtra("android.intent.extra.TEXT")) != null) {
                        text2 = textSequence.toString();
                    }
                    String subject = intent.getStringExtra("android.intent.extra.SUBJECT");
                    if (TextUtils.isEmpty(text2)) {
                        if ((text2.startsWith("http://") || text2.startsWith("https://")) && !TextUtils.isEmpty(subject)) {
                            text2 = subject + "\n" + text2;
                        }
                        this.sendingText = text2;
                        text = text2;
                    } else {
                        if (!TextUtils.isEmpty(subject)) {
                            this.sendingText = subject;
                        }
                        text = text2;
                    }
                    Parcelable parcelable6 = intent.getParcelableExtra("android.intent.extra.STREAM");
                    if (parcelable6 != null) {
                        if (this.sendingText == null) {
                            error5 = true;
                        }
                    } else {
                        if (parcelable6 instanceof Uri) {
                            parcelable2 = parcelable6;
                        } else {
                            parcelable2 = Uri.parse(parcelable6.toString());
                        }
                        Uri uri5 = (Uri) parcelable2;
                        if (uri5 != null && AndroidUtilities.isInternalUri(uri5)) {
                            error5 = true;
                        }
                        if (!error5 && uri5 != null) {
                            if ((type2 != null && type2.startsWith("image/")) || uri5.toString().toLowerCase().endsWith(".jpg")) {
                                if (this.photoPathsArray == null) {
                                    this.photoPathsArray = new ArrayList<>();
                                }
                                SendMessagesHelper.SendingMediaInfo info3 = new SendMessagesHelper.SendingMediaInfo();
                                info3.uri = uri5;
                                this.photoPathsArray.add(info3);
                            } else {
                                String originalPath3 = uri5.toString();
                                if (dialogId == 0 && originalPath3 != null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("export path = " + originalPath3);
                                    }
                                    Set<String> exportUris2 = MessagesController.getInstance(intentAccount4[0]).exportUri;
                                    String fileName3 = FileLoader.fixFileName(MediaController.getFileName(uri5));
                                    for (String u2 : exportUris2) {
                                        try {
                                            pattern2 = Pattern.compile(u2);
                                        } catch (Exception e29) {
                                            FileLog.e(e29);
                                        }
                                        if (!pattern2.matcher(originalPath3).find() && !pattern2.matcher(fileName3).find()) {
                                        }
                                        this.exportingChatUri = uri5;
                                    }
                                    if (this.exportingChatUri == null && originalPath3.startsWith("content://com.kakao.talk") && originalPath3.endsWith("KakaoTalkChats.txt")) {
                                        this.exportingChatUri = uri5;
                                    }
                                }
                                if (this.exportingChatUri == null) {
                                    String path4 = AndroidUtilities.getPath(uri5);
                                    if (!BuildVars.NO_SCOPED_STORAGE) {
                                        path4 = MediaController.copyFileToCache(uri5, "file");
                                    }
                                    if (path4 != null) {
                                        if (path4.startsWith("file:")) {
                                            path4 = path4.replace("file://", str5);
                                        }
                                        if (type2 != null && type2.startsWith("video/")) {
                                            this.videoPath = path4;
                                        } else {
                                            if (this.documentsPathsArray == null) {
                                                this.documentsPathsArray = new ArrayList<>();
                                                this.documentsOriginalPathsArray = new ArrayList<>();
                                            }
                                            this.documentsPathsArray.add(path4);
                                            this.documentsOriginalPathsArray.add(uri5.toString());
                                        }
                                    } else {
                                        if (this.documentsUrisArray == null) {
                                            this.documentsUrisArray = new ArrayList<>();
                                        }
                                        this.documentsUrisArray.add(uri5);
                                        this.documentsMimeType = type2;
                                    }
                                }
                            }
                        }
                    }
                }
                if (error5) {
                    Toast.makeText(this, "Unsupported content", 0).show();
                }
                intentAccount = intentAccount4;
                action = str4;
                launchActivity = this;
                videoCallUser = false;
                searchQuery = null;
                callSearchQuery = null;
                newContactName = null;
                newContactPhone = null;
                push_enc_id = 0;
                open_widget_edit = -1;
                open_widget_edit_type = -1;
                push_user_id = 0;
                push_chat_id = push_chat_id4;
                push_msg_id = push_msg_id7;
                i = z5;
                j = 0;
            }
            final String newContactName3222 = newContactName;
            if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                if (searchQuery == null) {
                    callSearchQuery2 = callSearchQuery;
                } else {
                    BaseFragment lastFragment = launchActivity.actionBarLayout.getLastFragment();
                    callSearchQuery2 = callSearchQuery;
                    if (lastFragment instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) lastFragment;
                        if (dialogsActivity.isMainDialogList()) {
                            if (dialogsActivity.getFragmentView() != null) {
                                dialogsActivity.search(searchQuery, true);
                            } else {
                                dialogsActivity.setInitialSearchString(searchQuery);
                            }
                        }
                    } else {
                        showDialogsList = true;
                    }
                }
                if (push_user_id != j) {
                    if (audioCallUser2) {
                        searchQuery3 = searchQuery;
                    } else if (videoCallUser) {
                        searchQuery3 = searchQuery;
                    } else {
                        Bundle args = new Bundle();
                        args.putLong("user_id", push_user_id);
                        if (push_msg_id != 0) {
                            args.putInt(action, push_msg_id);
                        }
                        if (!mainFragmentsStack.isEmpty()) {
                            searchQuery4 = searchQuery;
                        } else {
                            searchQuery4 = searchQuery;
                        }
                        if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(args), false, true, true, false)) {
                            pushOpened = true;
                            launchActivity.drawerLayoutContainer.closeDrawer();
                        }
                        isNew2 = isNew;
                        searchQuery2 = searchQuery4;
                        open_widget_edit_type2 = open_widget_edit_type;
                    }
                    if (needCallAlert != null) {
                        BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                        if (lastFragment2 != null) {
                            AlertsCreator.createCallDialogAlert(lastFragment2, lastFragment2.getMessagesController().getUser(Long.valueOf(push_user_id)), videoCallUser);
                        }
                        searchQuery2 = searchQuery3;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else {
                        VoIPPendingCall.startOrSchedule(launchActivity, push_user_id, videoCallUser, AccountInstance.getInstance(intentAccount[0]));
                        searchQuery2 = searchQuery3;
                        open_widget_edit_type2 = open_widget_edit_type;
                    }
                    isNew2 = isNew;
                } else {
                    String searchQuery7 = searchQuery;
                    if (push_chat_id != j) {
                        Bundle args2 = new Bundle();
                        args2.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, push_chat_id);
                        if (push_msg_id != 0) {
                            args2.putInt(action, push_msg_id);
                        }
                        if (!mainFragmentsStack.isEmpty()) {
                        }
                        if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(args2), false, true, true, false)) {
                            pushOpened = true;
                            launchActivity.drawerLayoutContainer.closeDrawer();
                        }
                        isNew2 = isNew;
                        searchQuery2 = searchQuery7;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else if (push_enc_id != 0) {
                        Bundle args3 = new Bundle();
                        args3.putInt("enc_id", push_enc_id);
                        if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(args3), false, true, true, false)) {
                            pushOpened = true;
                            launchActivity.drawerLayoutContainer.closeDrawer();
                        }
                        isNew2 = isNew;
                        searchQuery2 = searchQuery7;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else if (showDialogsList) {
                        if (!AndroidUtilities.isTablet()) {
                            launchActivity.actionBarLayout.removeAllFragments();
                        } else if (!launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            for (int a8 = 0; a8 < launchActivity.layersActionBarLayout.fragmentsStack.size() - 1; a8 = (a8 - 1) + 1) {
                                ActionBarLayout actionBarLayout = launchActivity.layersActionBarLayout;
                                actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                            }
                            launchActivity.layersActionBarLayout.closeLastFragment(false);
                        }
                        pushOpened = false;
                        isNew2 = false;
                        searchQuery2 = searchQuery7;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else if (showPlayer) {
                        if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                            launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                        }
                        pushOpened = false;
                        isNew2 = isNew;
                        searchQuery2 = searchQuery7;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else if (showLocations) {
                        if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                            launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new SharingLocationsAlert(launchActivity, new SharingLocationsAlert.SharingLocationsAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                                @Override // org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate
                                public final void didSelectLocation(LocationController.SharingLocationInfo sharingLocationInfo) {
                                    LaunchActivity.this.m3608lambda$handleIntent$14$orgtelegramuiLaunchActivity(intentAccount, sharingLocationInfo);
                                }
                            }, null));
                        }
                        pushOpened = false;
                        isNew2 = isNew;
                        searchQuery2 = searchQuery7;
                        open_widget_edit_type2 = open_widget_edit_type;
                    } else {
                        Uri uri6 = launchActivity.exportingChatUri;
                        if (uri6 != null) {
                            launchActivity.runImportRequest(uri6, launchActivity.documentsUrisArray);
                            searchQuery2 = searchQuery7;
                            open_widget_edit_type2 = open_widget_edit_type;
                        } else if (launchActivity.importingStickers != null) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda25
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.this.m3609lambda$handleIntent$15$orgtelegramuiLaunchActivity();
                                }
                            });
                            pushOpened = false;
                            isNew2 = isNew;
                            searchQuery2 = searchQuery7;
                            open_widget_edit_type2 = open_widget_edit_type;
                        } else {
                            if (launchActivity.videoPath != null || launchActivity.photoPathsArray != null || launchActivity.sendingText != null || launchActivity.documentsPathsArray != null || launchActivity.contactsToSend != null) {
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (launchActivity.documentsUrisArray != null) {
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (i != 0) {
                                boolean closePrevious = false;
                                if (i == 1) {
                                    Bundle args4 = new Bundle();
                                    int open_settings2 = i;
                                    args4.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                    fragment = new ProfileActivity(args4);
                                    open_settings = open_settings2;
                                    i2 = 6;
                                } else {
                                    int open_settings3 = i;
                                    open_settings = open_settings3;
                                    if (open_settings == 2) {
                                        fragment = new ThemeActivity(0);
                                        i2 = 6;
                                    } else if (open_settings == 3) {
                                        fragment = new SessionsActivity(0);
                                        i2 = 6;
                                    } else if (open_settings == 4) {
                                        fragment = new FiltersSetupActivity();
                                        i2 = 6;
                                    } else if (open_settings == 5) {
                                        closePrevious = true;
                                        fragment = new ActionIntroActivity(3);
                                        i2 = 6;
                                    } else {
                                        i2 = 6;
                                        if (open_settings == 6) {
                                            fragment = new EditWidgetActivity(open_widget_edit_type, open_widget_edit);
                                        } else {
                                            fragment = null;
                                        }
                                    }
                                }
                                final boolean closePreviousFinal = closePrevious;
                                if (open_settings == i2) {
                                    launchActivity.actionBarLayout.presentFragment(fragment, false, true, true, false);
                                } else {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.m3610lambda$handleIntent$16$orgtelegramuiLaunchActivity(fragment, closePreviousFinal);
                                        }
                                    });
                                }
                                if (AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.showLastFragment();
                                    launchActivity.rightActionBarLayout.showLastFragment();
                                    push_enc_id2 = push_enc_id;
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                } else {
                                    push_enc_id2 = push_enc_id;
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                }
                                pushOpened = true;
                                isNew2 = isNew;
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (open_new_dialog != 0) {
                                Bundle args5 = new Bundle();
                                args5.putBoolean("destroyAfterSelect", true);
                                launchActivity.actionBarLayout.presentFragment(new ContactsActivity(args5), false, true, true, false);
                                if (AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.showLastFragment();
                                    launchActivity.rightActionBarLayout.showLastFragment();
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                } else {
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                }
                                pushOpened = true;
                                isNew2 = isNew;
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (callSearchQuery2 != null) {
                                Bundle args6 = new Bundle();
                                args6.putBoolean("destroyAfterSelect", true);
                                args6.putBoolean("returnAsResult", true);
                                args6.putBoolean("onlyUsers", true);
                                args6.putBoolean("allowSelf", false);
                                ContactsActivity contactsFragment = new ContactsActivity(args6);
                                contactsFragment.setInitialSearchString(callSearchQuery2);
                                final boolean videoCall = videoCallUser;
                                contactsFragment.setDelegate(new ContactsActivity.ContactsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                                    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
                                    public final void didSelectContact(TLRPC.User user, String str6, ContactsActivity contactsActivity) {
                                        LaunchActivity.this.m3611lambda$handleIntent$17$orgtelegramuiLaunchActivity(videoCall, intentAccount, user, str6, contactsActivity);
                                    }
                                });
                                ActionBarLayout actionBarLayout2 = launchActivity.actionBarLayout;
                                actionBarLayout2.presentFragment(contactsFragment, actionBarLayout2.getLastFragment() instanceof ContactsActivity, true, true, false);
                                if (AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.showLastFragment();
                                    launchActivity.rightActionBarLayout.showLastFragment();
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                } else {
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                }
                                pushOpened = true;
                                isNew2 = isNew;
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (scanQr != null) {
                                final ActionIntroActivity fragment2 = new ActionIntroActivity(5);
                                fragment2.setQrLoginDelegate(new ActionIntroActivity.ActionIntroQRLoginDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
                                    @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
                                    public final void didFindQRCode(String str6) {
                                        LaunchActivity.this.m3612lambda$handleIntent$21$orgtelegramuiLaunchActivity(fragment2, str6);
                                    }
                                });
                                launchActivity.actionBarLayout.presentFragment(fragment2, false, true, true, false);
                                if (AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.showLastFragment();
                                    launchActivity.rightActionBarLayout.showLastFragment();
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                } else {
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                }
                                pushOpened = true;
                                isNew2 = isNew;
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else if (newContact != null) {
                                NewContactActivity fragment3 = new NewContactActivity();
                                if (newContactName3222 != null) {
                                    String[] names = newContactName3222.split(" ", 2);
                                    fragment3.setInitialName(names[0], names.length > 1 ? names[1] : null);
                                }
                                String newContactPhone3 = newContactPhone;
                                if (newContactPhone3 != null) {
                                    fragment3.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(newContactPhone3, true), false);
                                }
                                launchActivity.actionBarLayout.presentFragment(fragment3, false, true, true, false);
                                if (AndroidUtilities.isTablet()) {
                                    launchActivity.actionBarLayout.showLastFragment();
                                    launchActivity.rightActionBarLayout.showLastFragment();
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                } else {
                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                }
                                pushOpened = true;
                                isNew2 = isNew;
                                searchQuery2 = searchQuery7;
                                open_widget_edit_type2 = open_widget_edit_type;
                            } else {
                                String newContactPhone4 = newContactPhone;
                                if (showGroupVoip) {
                                    searchQuery2 = searchQuery7;
                                    GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                    if (GroupCallActivity.groupCallInstance != null) {
                                        GroupCallActivity.groupCallUiVisible = true;
                                        open_widget_edit_type2 = open_widget_edit_type;
                                    } else {
                                        open_widget_edit_type2 = open_widget_edit_type;
                                    }
                                } else {
                                    searchQuery2 = searchQuery7;
                                    if (newContactAlert != null) {
                                        final BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                        if (lastFragment3 == null || lastFragment3.getParentActivity() == null) {
                                            open_widget_edit_type2 = open_widget_edit_type;
                                        } else {
                                            final String finalNewContactPhone = NewContactActivity.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), newContactPhone4, false);
                                            open_widget_edit_type2 = open_widget_edit_type;
                                            AlertDialog newContactAlertDialog = new AlertDialog.Builder(lastFragment3.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", R.string.NewContactAlertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", R.string.NewContactAlertMessage, PhoneFormat.getInstance().format(finalNewContactPhone)))).setPositiveButton(LocaleController.getString("NewContactAlertButton", R.string.NewContactAlertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                                                @Override // android.content.DialogInterface.OnClickListener
                                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                                    LaunchActivity.lambda$handleIntent$22(finalNewContactPhone, newContactName3222, lastFragment3, dialogInterface, i7);
                                                }
                                            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                                            lastFragment3.showDialog(newContactAlertDialog);
                                            pushOpened = true;
                                        }
                                        isNew2 = isNew;
                                    } else {
                                        open_widget_edit_type2 = open_widget_edit_type;
                                        if (showCallLog) {
                                            launchActivity.actionBarLayout.presentFragment(new CallLogActivity(), false, true, true, false);
                                            if (AndroidUtilities.isTablet()) {
                                                launchActivity.actionBarLayout.showLastFragment();
                                                launchActivity.rightActionBarLayout.showLastFragment();
                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                            } else {
                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                            }
                                            pushOpened = true;
                                            isNew2 = isNew;
                                        }
                                    }
                                }
                            }
                            if (AndroidUtilities.isTablet()) {
                                z = false;
                            } else {
                                z = false;
                                NotificationCenter.getInstance(intentAccount[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                            }
                            if (dialogId == j) {
                                launchActivity.openDialogsToSend(z);
                                pushOpened = true;
                                isNew2 = isNew;
                            } else {
                                ArrayList<Long> dids = new ArrayList<>();
                                dids.add(Long.valueOf(dialogId));
                                launchActivity.didSelectDialogs(null, dids, null, false);
                            }
                        }
                        isNew2 = isNew;
                    }
                }
            } else {
                searchQuery2 = searchQuery;
                open_widget_edit_type2 = open_widget_edit_type;
                isNew2 = isNew;
            }
            if (!pushOpened && !isNew2) {
                if (!AndroidUtilities.isTablet()) {
                    if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                        if (launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                        }
                    } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                        DialogsActivity dialogsActivity2 = new DialogsActivity(null);
                        dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                        if (searchQuery2 != null) {
                            dialogsActivity2.setInitialSearchString(searchQuery2);
                        }
                        launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2);
                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                    if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                        launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                    } else {
                        DialogsActivity dialogsActivity3 = new DialogsActivity(null);
                        dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                        if (searchQuery2 != null) {
                            dialogsActivity3.setInitialSearchString(searchQuery2);
                        }
                        launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3);
                        launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
                launchActivity.actionBarLayout.showLastFragment();
                if (AndroidUtilities.isTablet()) {
                    launchActivity.layersActionBarLayout.showLastFragment();
                    launchActivity.rightActionBarLayout.showLastFragment();
                }
            }
            if (isVoipIntent) {
                VoIPFragment.show(launchActivity, intentAccount[0]);
            }
            if (showGroupVoip) {
                intent2 = intent;
                if ((intent2 == null || !"android.intent.action.MAIN".equals(intent.getAction())) && GroupCallActivity.groupCallInstance != null) {
                    GroupCallActivity.groupCallInstance.dismiss();
                }
            } else {
                intent2 = intent;
            }
            intent2.setAction(null);
            return pushOpened;
        } else {
            push_user_id2 = 0;
            push_chat_id2 = 0;
            push_msg_id2 = 0;
            z2 = false;
            showCallLog = false;
            j = 0;
            launchActivity = this;
            intentAccount = intentAccount4;
            action2 = action4;
            action = str4;
        }
        videoCallUser = false;
        searchQuery = null;
        callSearchQuery = null;
        newContactName = null;
        newContactPhone = null;
        push_enc_id = 0;
        open_widget_edit = -1;
        open_widget_edit_type = -1;
        push_user_id = push_user_id2;
        push_chat_id = push_chat_id2;
        push_msg_id = push_msg_id2;
        i = z2;
        final String newContactName32222 = newContactName;
        if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        if (!pushOpened) {
            if (!AndroidUtilities.isTablet()) {
            }
            launchActivity.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
            }
        }
        if (isVoipIntent) {
        }
        if (showGroupVoip) {
        }
        intent2.setAction(null);
        return pushOpened;
    }

    /* renamed from: lambda$handleIntent$10$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3605lambda$handleIntent$10$orgtelegramuiLaunchActivity(Intent copyIntent, boolean contactsLoaded) {
        handleIntent(copyIntent, true, false, false);
    }

    /* renamed from: lambda$handleIntent$12$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3607lambda$handleIntent$12$orgtelegramuiLaunchActivity(final AlertDialog cancelDeleteProgressDialog, final String finalPhone, final Bundle params, final TLRPC.TL_account_sendConfirmPhoneCode req, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3606lambda$handleIntent$11$orgtelegramuiLaunchActivity(cancelDeleteProgressDialog, error, finalPhone, params, response, req);
            }
        });
    }

    /* renamed from: lambda$handleIntent$11$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3606lambda$handleIntent$11$orgtelegramuiLaunchActivity(AlertDialog cancelDeleteProgressDialog, TLRPC.TL_error error, String finalPhone, Bundle params, TLObject response, TLRPC.TL_account_sendConfirmPhoneCode req) {
        cancelDeleteProgressDialog.dismiss();
        if (error == null) {
            m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new LoginActivity().cancelAccountDeletion(finalPhone, params, (TLRPC.TL_auth_sentCode) response));
        } else {
            AlertsCreator.processError(this.currentAccount, error, getActionBarLayout().getLastFragment(), req, new Object[0]);
        }
    }

    /* renamed from: lambda$handleIntent$14$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3608lambda$handleIntent$14$orgtelegramuiLaunchActivity(final int[] intentAccount, LocationController.SharingLocationInfo info) {
        intentAccount[0] = info.messageObject.currentAccount;
        switchToAccount(intentAccount[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(info.messageObject);
        final long dialog_id = info.messageObject.getDialogId();
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
                SendMessagesHelper.getInstance(intentAccount[0]).sendMessage(messageMedia, dialog_id, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
            }
        });
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(locationActivity);
    }

    /* renamed from: lambda$handleIntent$15$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3609lambda$handleIntent$15$orgtelegramuiLaunchActivity() {
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
            BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(0);
            fragment.showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
        }
    }

    /* renamed from: lambda$handleIntent$16$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3610lambda$handleIntent$16$orgtelegramuiLaunchActivity(BaseFragment fragment, boolean closePreviousFinal) {
        presentFragment(fragment, closePreviousFinal, false);
    }

    /* renamed from: lambda$handleIntent$17$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3611lambda$handleIntent$17$orgtelegramuiLaunchActivity(boolean videoCall, int[] intentAccount, TLRPC.User user, String param, ContactsActivity activity) {
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
        VoIPHelper.startCall(user, videoCall, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(intentAccount[0]));
    }

    /* renamed from: lambda$handleIntent$21$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3612lambda$handleIntent$21$orgtelegramuiLaunchActivity(final ActionIntroActivity fragment, String code) {
        final AlertDialog progressDialog = new AlertDialog(this, 3);
        progressDialog.setCanCancel(false);
        progressDialog.show();
        byte[] token = Base64.decode(code.substring("tg://login?token=".length()), 8);
        TLRPC.TL_auth_acceptLoginToken req = new TLRPC.TL_auth_acceptLoginToken();
        req.token = token;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda19
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$handleIntent$19(AlertDialog.this, tLObject, r3, tL_error);
                    }
                });
            }
        });
    }

    public static /* synthetic */ void lambda$handleIntent$19(AlertDialog progressDialog, TLObject response, final ActionIntroActivity fragment, final TLRPC.TL_error error) {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
        }
        if (!(response instanceof TLRPC.TL_authorization)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$handleIntent$18(ActionIntroActivity.this, error);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$handleIntent$18(ActionIntroActivity fragment, TLRPC.TL_error error) {
        String string = LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient);
        AlertsCreator.showSimpleAlert(fragment, string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error.text);
    }

    public static /* synthetic */ void lambda$handleIntent$22(String finalNewContactPhone, String finalNewContactName, BaseFragment lastFragment, DialogInterface d, int i) {
        NewContactActivity fragment = new NewContactActivity();
        fragment.setInitialPhoneNumber(finalNewContactPhone, false);
        if (finalNewContactName != null) {
            String[] names = finalNewContactName.split(" ", 2);
            fragment.setInitialName(names[0], names.length > 1 ? names[1] : null);
        }
        lastFragment.presentFragment(fragment);
    }

    public static int getTimestampFromLink(Uri data) {
        List<String> segments = data.getPathSegments();
        String timestampStr = null;
        if (segments.contains("video")) {
            timestampStr = data.getQuery();
        } else if (data.getQueryParameter(Theme.THEME_BACKGROUND_SLUG) != null) {
            timestampStr = data.getQueryParameter(Theme.THEME_BACKGROUND_SLUG);
        }
        int videoTimestamp = -1;
        if (timestampStr == null) {
            return -1;
        }
        try {
            videoTimestamp = Integer.parseInt(timestampStr);
        } catch (Throwable th) {
        }
        if (videoTimestamp == -1) {
            DateFormat dateFormat = new SimpleDateFormat("mm:ss");
            try {
                Date reference = dateFormat.parse("00:00");
                Date date = dateFormat.parse(timestampStr);
                int videoTimestamp2 = (int) ((date.getTime() - reference.getTime()) / 1000);
                return videoTimestamp2;
            } catch (ParseException e) {
                e.printStackTrace();
                return videoTimestamp;
            }
        }
        return videoTimestamp;
    }

    private void openDialogsToSend(boolean animated) {
        boolean removeLast;
        Bundle args = new Bundle();
        args.putBoolean("onlySelect", true);
        args.putInt("dialogsType", 3);
        args.putBoolean("allowSwitchAccount", true);
        ArrayList<TLRPC.User> arrayList = this.contactsToSend;
        if (arrayList != null) {
            if (arrayList.size() != 1) {
                args.putString("selectAlertString", LocaleController.getString("SendContactToText", R.string.SendMessagesToText));
                args.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroupText", R.string.SendContactToGroupText));
            }
        } else {
            args.putString("selectAlertString", LocaleController.getString("SendMessagesToText", R.string.SendMessagesToText));
            args.putString("selectAlertStringGroup", LocaleController.getString("SendMessagesToGroupText", R.string.SendMessagesToGroupText));
        }
        DialogsActivity fragment = new DialogsActivity(args) { // from class: org.telegram.ui.LaunchActivity.12
            @Override // org.telegram.ui.DialogsActivity
            public boolean shouldShowNextButton(DialogsActivity dialogsFragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                if (LaunchActivity.this.exportingChatUri != null) {
                    return false;
                }
                if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                    return true;
                }
                if (dids.size() <= 1) {
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
        fragment.setDelegate(this);
        if (AndroidUtilities.isTablet()) {
            removeLast = this.layersActionBarLayout.fragmentsStack.size() > 0 && (this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
        } else {
            removeLast = this.actionBarLayout.fragmentsStack.size() > 1 && (this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
        }
        this.actionBarLayout.presentFragment(fragment, removeLast, !animated, true, false);
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (GroupCallActivity.groupCallInstance != null) {
            GroupCallActivity.groupCallInstance.dismiss();
        }
        if (!animated) {
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            if (AndroidUtilities.isTablet()) {
                this.actionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
                return;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    private int runCommentRequest(final int intentAccount, final AlertDialog progressDialog, final Integer messageId, final Integer commentId, final Integer threadId, final TLRPC.Chat chat) {
        if (chat == null) {
            return 0;
        }
        final TLRPC.TL_messages_getDiscussionMessage req = new TLRPC.TL_messages_getDiscussionMessage();
        req.peer = MessagesController.getInputPeer(chat);
        req.msg_id = (commentId != null ? messageId : threadId).intValue();
        return ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.m3620lambda$runCommentRequest$24$orgtelegramuiLaunchActivity(intentAccount, messageId, chat, req, commentId, threadId, progressDialog, tLObject, tL_error);
            }
        });
    }

    /* renamed from: lambda$runCommentRequest$24$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3620lambda$runCommentRequest$24$orgtelegramuiLaunchActivity(final int intentAccount, final Integer messageId, final TLRPC.Chat chat, final TLRPC.TL_messages_getDiscussionMessage req, final Integer commentId, final Integer threadId, final AlertDialog progressDialog, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3619lambda$runCommentRequest$23$orgtelegramuiLaunchActivity(response, intentAccount, messageId, chat, req, commentId, threadId, progressDialog);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* renamed from: lambda$runCommentRequest$23$org-telegram-ui-LaunchActivity */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m3619lambda$runCommentRequest$23$orgtelegramuiLaunchActivity(TLObject response, int intentAccount, Integer messageId, TLRPC.Chat chat, TLRPC.TL_messages_getDiscussionMessage req, Integer commentId, Integer threadId, AlertDialog progressDialog) {
        TLRPC.TL_messages_discussionMessage res;
        try {
            if (response instanceof TLRPC.TL_messages_discussionMessage) {
                TLRPC.TL_messages_discussionMessage res2 = (TLRPC.TL_messages_discussionMessage) response;
                MessagesController.getInstance(intentAccount).putUsers(res2.users, false);
                MessagesController.getInstance(intentAccount).putChats(res2.chats, false);
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                int N = res2.messages.size();
                for (int a = 0; a < N; a++) {
                    arrayList.add(new MessageObject(UserConfig.selectedAccount, res2.messages.get(a), true, true));
                }
                if (!arrayList.isEmpty()) {
                    Bundle args = new Bundle();
                    args.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -arrayList.get(0).getDialogId());
                    args.putInt(Constants.MessagePayloadKeys.MSGID_SERVER, Math.max(1, messageId.intValue()));
                    ChatActivity chatActivity = new ChatActivity(args);
                    chatActivity.setThreadMessages(arrayList, chat, req.msg_id, res2.read_inbox_max_id, res2.read_outbox_max_id);
                    if (commentId != null) {
                        chatActivity.setHighlightMessageId(commentId.intValue());
                    } else if (threadId != null) {
                        chatActivity.setHighlightMessageId(messageId.intValue());
                    }
                    m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(chatActivity);
                    res = 1;
                    if (res == null) {
                        try {
                            if (!mainFragmentsStack.isEmpty()) {
                                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString("ChannelPostDeleted", R.string.ChannelPostDeleted)).show();
                            }
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    progressDialog.dismiss();
                    return;
                }
            }
            progressDialog.dismiss();
            return;
        } catch (Exception e2) {
            FileLog.e(e2);
            return;
        }
        res = null;
        if (res == null) {
        }
    }

    private void runImportRequest(final Uri importUri, ArrayList<Uri> documents) {
        final int intentAccount = UserConfig.selectedAccount;
        final AlertDialog progressDialog = new AlertDialog(this, 3);
        final int[] requestId = {0};
        InputStream inputStream = null;
        int linesCount = 0;
        try {
            try {
                inputStream = getContentResolver().openInputStream(importUri);
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                while (true) {
                    String line = r.readLine();
                    if (line == null || linesCount >= 100) {
                        break;
                    }
                    total.append(line);
                    total.append('\n');
                    linesCount++;
                }
                String content = total.toString();
                TLRPC.TL_messages_checkHistoryImport req = new TLRPC.TL_messages_checkHistoryImport();
                req.import_head = content;
                requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        LaunchActivity.this.m3622lambda$runImportRequest$26$orgtelegramuiLaunchActivity(importUri, intentAccount, progressDialog, tLObject, tL_error);
                    }
                });
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        LaunchActivity.lambda$runImportRequest$27(intentAccount, requestId, r3, dialogInterface);
                    }
                });
                try {
                    progressDialog.showDelayed(300L);
                } catch (Exception e) {
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
            }
        } catch (Exception e3) {
            FileLog.e(e3);
            if (inputStream == null) {
                return;
            }
            try {
                inputStream.close();
            } catch (Exception e22) {
                FileLog.e(e22);
            }
        }
    }

    /* renamed from: lambda$runImportRequest$26$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3622lambda$runImportRequest$26$orgtelegramuiLaunchActivity(final Uri importUri, final int intentAccount, final AlertDialog progressDialog, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3621lambda$runImportRequest$25$orgtelegramuiLaunchActivity(response, importUri, intentAccount, progressDialog);
            }
        }, 2L);
    }

    /* renamed from: lambda$runImportRequest$25$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3621lambda$runImportRequest$25$orgtelegramuiLaunchActivity(TLObject response, Uri importUri, int intentAccount, AlertDialog progressDialog) {
        if (!isFinishing()) {
            boolean removeLast = false;
            if (response != null && this.actionBarLayout != null) {
                TLRPC.TL_messages_historyImportParsed res = (TLRPC.TL_messages_historyImportParsed) response;
                Bundle args = new Bundle();
                args.putBoolean("onlySelect", true);
                args.putString("importTitle", res.title);
                args.putBoolean("allowSwitchAccount", true);
                if (res.pm) {
                    args.putInt("dialogsType", 12);
                } else if (res.group) {
                    args.putInt("dialogsType", 11);
                } else {
                    String uri = importUri.toString();
                    Set<String> uris = MessagesController.getInstance(intentAccount).exportPrivateUri;
                    boolean ok = false;
                    Iterator<String> it = uris.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String u = it.next();
                        if (uri.contains(u)) {
                            args.putInt("dialogsType", 12);
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Set<String> uris2 = MessagesController.getInstance(intentAccount).exportGroupUri;
                        Iterator<String> it2 = uris2.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            String u2 = it2.next();
                            if (uri.contains(u2)) {
                                args.putInt("dialogsType", 11);
                                ok = true;
                                break;
                            }
                        }
                        if (!ok) {
                            args.putInt("dialogsType", 13);
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
                if (GroupCallActivity.groupCallInstance != null) {
                    GroupCallActivity.groupCallInstance.dismiss();
                }
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                if (!AndroidUtilities.isTablet()) {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                } else {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                }
                DialogsActivity fragment = new DialogsActivity(args);
                fragment.setDelegate(this);
                if (AndroidUtilities.isTablet()) {
                    if (this.layersActionBarLayout.fragmentsStack.size() > 0 && (this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity)) {
                        removeLast = true;
                    }
                } else if (this.actionBarLayout.fragmentsStack.size() > 1 && (this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity)) {
                    removeLast = true;
                }
                this.actionBarLayout.presentFragment(fragment, removeLast, false, true, false);
            } else {
                if (this.documentsUrisArray == null) {
                    this.documentsUrisArray = new ArrayList<>();
                }
                this.documentsUrisArray.add(0, this.exportingChatUri);
                this.exportingChatUri = null;
                openDialogsToSend(true);
            }
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static /* synthetic */ void lambda$runImportRequest$27(int intentAccount, int[] requestId, Runnable cancelRunnableFinal, DialogInterface dialog) {
        ConnectionsManager.getInstance(intentAccount).cancelRequest(requestId[0], true);
        if (cancelRunnableFinal != null) {
            cancelRunnableFinal.run();
        }
    }

    private void openGroupCall(AccountInstance accountInstance, TLRPC.Chat chat, String hash) {
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        VoIPHelper.startCall(chat, null, hash, false, this, arrayList.get(arrayList.size() - 1), accountInstance);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void runLinkRequest(final int intentAccount, final String username, final String group, final String sticker, final String botUser, final String botChat, final String botChannel, final String botChatAdminParams, final String message, final boolean hasUrl, final Integer messageId, final Long channelId, final Integer threadId, final Integer commentId, final String game, final HashMap<String, String> auth, final String lang, final String unsupportedUrl, final String code, final String loginToken, final TLRPC.TL_wallPaper wallPaper, final String inputInvoiceSlug, final String theme, final String voicechat, final String livestream, int state, final int videoTimestamp, final String setAsAttachBot, final String attachMenuBotToOpen, final String attachMenuBotChoose) {
        final int i;
        final AlertDialog progressDialog;
        final int[] requestId;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        StickersAlert alert;
        final AlertDialog progressDialog2;
        TLRPC.TL_contacts_resolveUsername resolveUsername;
        if (state == 0 && UserConfig.getActivatedAccountsCount() >= 2 && auth != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i2) {
                    LaunchActivity.this.m3623lambda$runLinkRequest$28$orgtelegramuiLaunchActivity(intentAccount, username, group, sticker, botUser, botChat, botChannel, botChatAdminParams, message, hasUrl, messageId, channelId, threadId, commentId, game, auth, lang, unsupportedUrl, code, loginToken, wallPaper, inputInvoiceSlug, theme, voicechat, livestream, videoTimestamp, setAsAttachBot, attachMenuBotToOpen, attachMenuBotChoose, i2);
                }
            }).show();
            return;
        }
        BaseFragment baseFragment = null;
        if (code != null) {
            if (!NotificationCenter.getGlobalInstance().hasObservers(NotificationCenter.didReceiveSmsCode)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", R.string.OtherLoginCode, code)));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                showAlertDialog(builder);
                return;
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveSmsCode, code);
        } else if (loginToken != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder2);
        } else {
            final AlertDialog progressDialog3 = new AlertDialog(this, 3);
            final int[] requestId2 = {0};
            Runnable cancelRunnable = null;
            if (inputInvoiceSlug != null) {
                TLRPC.TL_payments_getPaymentForm req = new TLRPC.TL_payments_getPaymentForm();
                TLRPC.TL_inputInvoiceSlug invoiceSlug = new TLRPC.TL_inputInvoiceSlug();
                invoiceSlug.slug = inputInvoiceSlug;
                req.invoice = invoiceSlug;
                requestId2[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        LaunchActivity.this.m3625lambda$runLinkRequest$30$orgtelegramuiLaunchActivity(intentAccount, inputInvoiceSlug, progressDialog3, tLObject, tL_error);
                    }
                });
                i = intentAccount;
                requestId = requestId2;
                progressDialog = progressDialog3;
            } else if (username != null) {
                if (AndroidUtilities.isNumeric(username)) {
                    TLRPC.TL_contacts_resolvePhone resolvePhone = new TLRPC.TL_contacts_resolvePhone();
                    resolvePhone.phone = username;
                    resolveUsername = resolvePhone;
                } else {
                    TLRPC.TL_contacts_resolveUsername resolveUsername2 = new TLRPC.TL_contacts_resolveUsername();
                    resolveUsername2.username = username;
                    resolveUsername = resolveUsername2;
                }
                requestId = requestId2;
                requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        LaunchActivity.this.m3639lambda$runLinkRequest$44$orgtelegramuiLaunchActivity(game, voicechat, livestream, intentAccount, setAsAttachBot, attachMenuBotToOpen, attachMenuBotChoose, messageId, commentId, threadId, requestId2, progressDialog3, botChat, botChannel, botChatAdminParams, botUser, videoTimestamp, username, tLObject, tL_error);
                    }
                });
                i = intentAccount;
                progressDialog = progressDialog3;
            } else {
                requestId = requestId2;
                if (group != null) {
                    if (state != 0) {
                        i = intentAccount;
                        progressDialog2 = progressDialog3;
                        if (state == 1) {
                            TLRPC.TL_messages_importChatInvite req2 = new TLRPC.TL_messages_importChatInvite();
                            req2.hash = group;
                            ConnectionsManager.getInstance(intentAccount).sendRequest(req2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.m3643lambda$runLinkRequest$49$orgtelegramuiLaunchActivity(i, progressDialog2, tLObject, tL_error);
                                }
                            }, 2);
                            progressDialog = progressDialog2;
                        }
                    } else {
                        TLRPC.TL_messages_checkChatInvite req3 = new TLRPC.TL_messages_checkChatInvite();
                        req3.hash = group;
                        i = intentAccount;
                        progressDialog2 = progressDialog3;
                        requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req3, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.m3641lambda$runLinkRequest$47$orgtelegramuiLaunchActivity(i, progressDialog2, group, tLObject, tL_error);
                            }
                        }, 2);
                    }
                    progressDialog = progressDialog2;
                } else {
                    i = intentAccount;
                    if (sticker != null) {
                        if (!mainFragmentsStack.isEmpty()) {
                            TLRPC.TL_inputStickerSetShortName stickerset = new TLRPC.TL_inputStickerSetShortName();
                            stickerset.short_name = sticker;
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BaseFragment fragment = arrayList.get(arrayList.size() - 1);
                            if (fragment instanceof ChatActivity) {
                                ChatActivity chatActivity = (ChatActivity) fragment;
                                alert = new StickersAlert(this, fragment, stickerset, null, chatActivity.getChatActivityEnterViewForStickers(), chatActivity.getResourceProvider());
                                alert.setCalcMandatoryInsets(chatActivity.isKeyboardVisible());
                            } else {
                                alert = new StickersAlert(this, fragment, stickerset, (TLRPC.TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null);
                            }
                            fragment.showDialog(alert);
                            return;
                        }
                        return;
                    } else if (message != null) {
                        Bundle args = new Bundle();
                        args.putBoolean("onlySelect", true);
                        args.putInt("dialogsType", 3);
                        DialogsActivity fragment2 = new DialogsActivity(args);
                        fragment2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda2
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList2, CharSequence charSequence, boolean z) {
                                LaunchActivity.this.m3644lambda$runLinkRequest$50$orgtelegramuiLaunchActivity(hasUrl, i, message, dialogsActivity, arrayList2, charSequence, z);
                            }
                        });
                        presentFragment(fragment2, false, true);
                        progressDialog = progressDialog3;
                    } else if (auth != null) {
                        int bot_id = Utilities.parseInt((CharSequence) auth.get("bot_id")).intValue();
                        if (bot_id == 0) {
                            return;
                        }
                        final String payload = auth.get("payload");
                        final String nonce = auth.get("nonce");
                        final String callbackUrl = auth.get("callback_url");
                        final TLRPC.TL_account_getAuthorizationForm req4 = new TLRPC.TL_account_getAuthorizationForm();
                        req4.bot_id = bot_id;
                        req4.scope = auth.get("scope");
                        req4.public_key = auth.get("public_key");
                        progressDialog = progressDialog3;
                        requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req4, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.m3648lambda$runLinkRequest$54$orgtelegramuiLaunchActivity(requestId, intentAccount, progressDialog3, req4, payload, nonce, callbackUrl, tLObject, tL_error);
                            }
                        });
                    } else {
                        progressDialog = progressDialog3;
                        if (unsupportedUrl != null) {
                            TLRPC.TL_help_getDeepLinkInfo req5 = new TLRPC.TL_help_getDeepLinkInfo();
                            req5.path = unsupportedUrl;
                            requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req5, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.m3650lambda$runLinkRequest$56$orgtelegramuiLaunchActivity(progressDialog, tLObject, tL_error);
                                }
                            });
                        } else if (lang != null) {
                            TLRPC.TL_langpack_getLanguage req6 = new TLRPC.TL_langpack_getLanguage();
                            req6.lang_code = lang;
                            req6.lang_pack = "android";
                            requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req6, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.m3652lambda$runLinkRequest$58$orgtelegramuiLaunchActivity(progressDialog, tLObject, tL_error);
                                }
                            });
                        } else if (wallPaper != null) {
                            boolean ok = false;
                            if (TextUtils.isEmpty(wallPaper.slug)) {
                                try {
                                    if (wallPaper.settings.third_background_color != 0) {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper(Theme.COLOR_BACKGROUND_SLUG, wallPaper.settings.background_color, wallPaper.settings.second_background_color, wallPaper.settings.third_background_color, wallPaper.settings.fourth_background_color);
                                    } else {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper(Theme.COLOR_BACKGROUND_SLUG, wallPaper.settings.background_color, wallPaper.settings.second_background_color, AndroidUtilities.getWallpaperRotation(wallPaper.settings.rotation, false));
                                    }
                                    final ThemePreviewActivity wallpaperActivity = new ThemePreviewActivity(colorWallpaper, null, true, false);
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(wallpaperActivity);
                                        }
                                    });
                                    ok = true;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            if (!ok) {
                                TLRPC.TL_account_getWallPaper req7 = new TLRPC.TL_account_getWallPaper();
                                TLRPC.TL_inputWallPaperSlug inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                                inputWallPaperSlug.slug = wallPaper.slug;
                                req7.wallpaper = inputWallPaperSlug;
                                requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req7, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        LaunchActivity.this.m3655lambda$runLinkRequest$61$orgtelegramuiLaunchActivity(progressDialog, wallPaper, tLObject, tL_error);
                                    }
                                });
                            }
                        } else if (theme != null) {
                            cancelRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda30
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.this.m3656lambda$runLinkRequest$62$orgtelegramuiLaunchActivity();
                                }
                            };
                            TLRPC.TL_account_getTheme req8 = new TLRPC.TL_account_getTheme();
                            req8.format = "android";
                            TLRPC.TL_inputThemeSlug inputThemeSlug = new TLRPC.TL_inputThemeSlug();
                            inputThemeSlug.slug = theme;
                            req8.theme = inputThemeSlug;
                            requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req8, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.m3658lambda$runLinkRequest$64$orgtelegramuiLaunchActivity(progressDialog, tLObject, tL_error);
                                }
                            });
                        } else if (channelId != null && messageId != null) {
                            if (threadId != null) {
                                TLRPC.Chat chat = MessagesController.getInstance(intentAccount).getChat(channelId);
                                if (chat != null) {
                                    requestId[0] = runCommentRequest(intentAccount, progressDialog, messageId, commentId, threadId, chat);
                                } else {
                                    TLRPC.TL_channels_getChannels req9 = new TLRPC.TL_channels_getChannels();
                                    TLRPC.TL_inputChannel inputChannel = new TLRPC.TL_inputChannel();
                                    inputChannel.channel_id = channelId.longValue();
                                    req9.id.add(inputChannel);
                                    requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req9, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            LaunchActivity.this.m3660lambda$runLinkRequest$66$orgtelegramuiLaunchActivity(requestId, intentAccount, progressDialog, messageId, commentId, threadId, tLObject, tL_error);
                                        }
                                    });
                                }
                            } else {
                                final Bundle args2 = new Bundle();
                                args2.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, channelId.longValue());
                                args2.putInt(Constants.MessagePayloadKeys.MSGID_SERVER, messageId.intValue());
                                if (!mainFragmentsStack.isEmpty()) {
                                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                    baseFragment = arrayList2.get(arrayList2.size() - 1);
                                }
                                final BaseFragment lastFragment = baseFragment;
                                if (lastFragment == null || MessagesController.getInstance(intentAccount).checkCanOpenChat(args2, lastFragment)) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda33
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.this.m3663lambda$runLinkRequest$69$orgtelegramuiLaunchActivity(args2, channelId, requestId, progressDialog, lastFragment, intentAccount);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
            if (requestId[0] != 0) {
                final Runnable cancelRunnableFinal = cancelRunnable;
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda26
                    @Override // android.content.DialogInterface.OnCancelListener
                    public final void onCancel(DialogInterface dialogInterface) {
                        LaunchActivity.lambda$runLinkRequest$70(i, requestId, cancelRunnableFinal, dialogInterface);
                    }
                });
                try {
                    progressDialog.showDelayed(300L);
                } catch (Exception e2) {
                }
            }
        }
    }

    /* renamed from: lambda$runLinkRequest$28$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3623lambda$runLinkRequest$28$orgtelegramuiLaunchActivity(int intentAccount, String username, String group, String sticker, String botUser, String botChat, String botChannel, String botChatAdminParams, String message, boolean hasUrl, Integer messageId, Long channelId, Integer threadId, Integer commentId, String game, HashMap auth, String lang, String unsupportedUrl, String code, String loginToken, TLRPC.TL_wallPaper wallPaper, String inputInvoiceSlug, String theme, String voicechat, String livestream, int videoTimestamp, String setAsAttachBot, String attachMenuBotToOpen, String attachMenuBotChoose, int account) {
        if (account != intentAccount) {
            switchToAccount(account, true);
        }
        runLinkRequest(account, username, group, sticker, botUser, botChat, botChannel, botChatAdminParams, message, hasUrl, messageId, channelId, threadId, commentId, game, auth, lang, unsupportedUrl, code, loginToken, wallPaper, inputInvoiceSlug, theme, voicechat, livestream, 1, videoTimestamp, setAsAttachBot, attachMenuBotToOpen, attachMenuBotChoose);
    }

    /* renamed from: lambda$runLinkRequest$30$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3625lambda$runLinkRequest$30$orgtelegramuiLaunchActivity(final int intentAccount, final String inputInvoiceSlug, final AlertDialog progressDialog, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3624lambda$runLinkRequest$29$orgtelegramuiLaunchActivity(error, response, intentAccount, inputInvoiceSlug, progressDialog);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$29$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3624lambda$runLinkRequest$29$orgtelegramuiLaunchActivity(TLRPC.TL_error error, TLObject response, int intentAccount, String inputInvoiceSlug, AlertDialog progressDialog) {
        if (error != null) {
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString((int) R.string.PaymentInvoiceLinkInvalid)).show();
        } else if (!isFinishing()) {
            if (response instanceof TLRPC.TL_payments_paymentForm) {
                TLRPC.TL_payments_paymentForm form = (TLRPC.TL_payments_paymentForm) response;
                MessagesController.getInstance(intentAccount).putUsers(form.users, false);
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new PaymentFormActivity(form, inputInvoiceSlug, getActionBarLayout().getLastFragment()));
            } else if (response instanceof TLRPC.TL_payments_paymentReceipt) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new PaymentFormActivity((TLRPC.TL_payments_paymentReceipt) response));
            }
        }
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: lambda$runLinkRequest$44$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3639lambda$runLinkRequest$44$orgtelegramuiLaunchActivity(final String game, final String voicechat, final String livestream, final int intentAccount, final String setAsAttachBot, final String attachMenuBotToOpen, final String attachMenuBotChoose, final Integer messageId, final Integer commentId, final Integer threadId, final int[] requestId, final AlertDialog progressDialog, final String botChat, final String botChannel, final String botChatAdminParams, final String botUser, final int videoTimestamp, final String username, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3638lambda$runLinkRequest$43$orgtelegramuiLaunchActivity(response, error, game, voicechat, livestream, intentAccount, setAsAttachBot, attachMenuBotToOpen, attachMenuBotChoose, messageId, commentId, threadId, requestId, progressDialog, botChat, botChannel, botChatAdminParams, botUser, videoTimestamp, username);
            }
        }, 2L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:195:0x049c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:206:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r14v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v5 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* renamed from: lambda$runLinkRequest$43$org-telegram-ui-LaunchActivity */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m3638lambda$runLinkRequest$43$orgtelegramuiLaunchActivity(TLObject response, TLRPC.TL_error error, final String game, String voicechat, String livestream, final int intentAccount, final String setAsAttachBot, String attachMenuBotToOpen, final String attachMenuBotChoose, Integer messageId, Integer commentId, Integer threadId, int[] requestId, AlertDialog progressDialog, String botChat, String botChannel, final String botChatAdminParams, String botUser, int videoTimestamp, String username) {
        Exception e;
        ?? r14;
        final TLRPC.TL_contacts_resolvedPeer res;
        TLRPC.User user;
        long dialog_id;
        boolean isBot;
        boolean removeLast;
        final TLRPC.TL_contacts_resolvedPeer res2;
        if (!isFinishing()) {
            boolean hideProgressDialog = true;
            TLRPC.TL_contacts_resolvedPeer res3 = (TLRPC.TL_contacts_resolvedPeer) response;
            boolean z = true;
            if (error == null && this.actionBarLayout != null && ((game == null && voicechat == null) || ((game != null && !res3.users.isEmpty()) || ((voicechat != null && !res3.chats.isEmpty()) || (livestream != null && !res3.chats.isEmpty()))))) {
                MessagesController.getInstance(intentAccount).putUsers(res3.users, false);
                MessagesController.getInstance(intentAccount).putChats(res3.chats, false);
                MessagesStorage.getInstance(intentAccount).putUsersAndChats(res3.users, res3.chats, false, true);
                if (setAsAttachBot != null && attachMenuBotToOpen == null) {
                    final TLRPC.User user2 = MessagesController.getInstance(intentAccount).getUser(Long.valueOf(res3.peer.user_id));
                    if (user2 != null && user2.bot) {
                        if (!user2.bot_attach_menu) {
                            res2 = res3;
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString((int) R.string.BotCantAddToAttachMenu)).show();
                        } else {
                            TLRPC.TL_messages_getAttachMenuBot getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
                            getAttachMenuBot.bot = MessagesController.getInstance(intentAccount).getInputUser(res3.peer.user_id);
                            res2 = res3;
                            ConnectionsManager.getInstance(intentAccount).sendRequest(getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.m3631lambda$runLinkRequest$36$orgtelegramuiLaunchActivity(intentAccount, attachMenuBotChoose, user2, setAsAttachBot, res2, tLObject, tL_error);
                                }
                            });
                        }
                    } else {
                        res2 = res3;
                        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                        BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString((int) R.string.BotSetAttachLinkNotBot)).show();
                    }
                } else {
                    if (messageId == null) {
                        res = res3;
                        r14 = 0;
                    } else if (commentId != null || threadId != null) {
                        res = res3;
                        if (res.chats.isEmpty()) {
                            r14 = 0;
                        } else {
                            requestId[0] = runCommentRequest(intentAccount, progressDialog, messageId, commentId, threadId, res.chats.get(0));
                            if (requestId[0] != 0) {
                                hideProgressDialog = false;
                            }
                        }
                    } else {
                        res = res3;
                        r14 = 0;
                    }
                    if (game != null) {
                        Bundle args = new Bundle();
                        args.putBoolean("onlySelect", true);
                        args.putBoolean("cantSendToChannels", true);
                        args.putInt("dialogsType", 1);
                        args.putString("selectAlertString", LocaleController.getString("SendGameToText", R.string.SendGameToText));
                        args.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", R.string.SendGameToGroupText));
                        DialogsActivity fragment = new DialogsActivity(args);
                        fragment.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList3, CharSequence charSequence, boolean z2) {
                                LaunchActivity.this.m3632lambda$runLinkRequest$37$orgtelegramuiLaunchActivity(game, intentAccount, res, dialogsActivity, arrayList3, charSequence, z2);
                            }
                        });
                        if (!AndroidUtilities.isTablet()) {
                            removeLast = this.actionBarLayout.fragmentsStack.size() > 1 && (this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                        } else {
                            removeLast = this.layersActionBarLayout.fragmentsStack.size() > 0 && (this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                        }
                        this.actionBarLayout.presentFragment(fragment, removeLast, true, true, false);
                        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                            SecretMediaViewer.getInstance().closePhoto(r14, r14);
                        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                            PhotoViewer.getInstance().closePhoto(r14, true);
                        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                            ArticleViewer.getInstance().close(r14, true);
                        }
                        if (GroupCallActivity.groupCallInstance != null) {
                            GroupCallActivity.groupCallInstance.dismiss();
                        }
                        this.drawerLayoutContainer.setAllowOpenDrawer(r14, r14);
                        if (!AndroidUtilities.isTablet()) {
                            this.drawerLayoutContainer.setAllowOpenDrawer(true, r14);
                        } else {
                            this.actionBarLayout.showLastFragment();
                            this.rightActionBarLayout.showLastFragment();
                        }
                    } else {
                        BaseFragment baseFragment = null;
                        if (botChat == null && botChannel == null) {
                            Bundle args2 = new Bundle();
                            if (!res.chats.isEmpty()) {
                                args2.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, res.chats.get(r14).id);
                                dialog_id = -res.chats.get(r14).id;
                            } else {
                                args2.putLong("user_id", res.users.get(r14).id);
                                dialog_id = res.users.get(r14).id;
                            }
                            if (botUser != null && res.users.size() > 0 && res.users.get(r14).bot) {
                                args2.putString("botUser", botUser);
                                isBot = true;
                            } else {
                                isBot = false;
                            }
                            boolean isBot2 = this.navigateToPremiumBot;
                            if (isBot2) {
                                this.navigateToPremiumBot = r14;
                                args2.putBoolean("premium_bot", true);
                            }
                            if (messageId != null) {
                                args2.putInt(Constants.MessagePayloadKeys.MSGID_SERVER, messageId.intValue());
                            }
                            if (voicechat != null) {
                                args2.putString("voicechat", voicechat);
                            }
                            if (livestream != null) {
                                args2.putString("livestream", livestream);
                            }
                            if (videoTimestamp >= 0) {
                                args2.putInt("video_timestamp", videoTimestamp);
                            }
                            if (attachMenuBotToOpen != null) {
                                args2.putString("attach_bot", attachMenuBotToOpen);
                            }
                            if (setAsAttachBot != null) {
                                args2.putString("attach_bot_start_command", setAsAttachBot);
                            }
                            if (!mainFragmentsStack.isEmpty() && voicechat == null) {
                                ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                                baseFragment = arrayList3.get(arrayList3.size() - 1);
                            }
                            BaseFragment lastFragment = baseFragment;
                            if (lastFragment == null || MessagesController.getInstance(intentAccount).checkCanOpenChat(args2, lastFragment)) {
                                if (isBot && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                    ((ChatActivity) lastFragment).setBotUser(botUser);
                                } else {
                                    MessagesController messagesController = MessagesController.getInstance(intentAccount);
                                    int i = r14;
                                    if (messageId != null) {
                                        i = messageId.intValue();
                                    }
                                    long dialog_id2 = dialog_id;
                                    messagesController.ensureMessagesLoaded(dialog_id2, i == 1 ? 1 : 0, new AnonymousClass14(progressDialog, livestream, lastFragment, dialog_id2, args2));
                                    hideProgressDialog = false;
                                }
                            }
                        }
                        if (!res.users.isEmpty()) {
                            ArrayList<TLRPC.User> arrayList4 = res.users;
                            int i2 = r14 == true ? 1 : 0;
                            int i3 = r14 == true ? 1 : 0;
                            int i4 = r14 == true ? 1 : 0;
                            int i5 = r14 == true ? 1 : 0;
                            user = arrayList4.get(i2);
                        } else {
                            user = null;
                        }
                        final TLRPC.User user3 = user;
                        if (user3 == null || (user3.bot && user3.bot_nochats)) {
                            try {
                                if (!mainFragmentsStack.isEmpty()) {
                                    ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                                    BulletinFactory.of(arrayList5.get(arrayList5.size() - 1)).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups)).show();
                                    return;
                                }
                                return;
                            } catch (Exception e2) {
                                FileLog.e(e2);
                                return;
                            }
                        }
                        Bundle args3 = new Bundle();
                        args3.putBoolean("onlySelect", true);
                        args3.putInt("dialogsType", 2);
                        args3.putBoolean("resetDelegate", r14);
                        args3.putBoolean("closeFragment", r14);
                        args3.putBoolean("allowGroups", botChat != null);
                        if (botChannel == null) {
                            z = false;
                        }
                        args3.putBoolean("allowChannels", z);
                        final String botHash = TextUtils.isEmpty(botChat) ? TextUtils.isEmpty(botChannel) ? null : botChannel : botChat;
                        final DialogsActivity fragment2 = new DialogsActivity(args3);
                        fragment2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList6, CharSequence charSequence, boolean z2) {
                                LaunchActivity.this.m3637lambda$runLinkRequest$42$orgtelegramuiLaunchActivity(intentAccount, user3, botChatAdminParams, botHash, fragment2, dialogsActivity, arrayList6, charSequence, z2);
                            }
                        });
                        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(fragment2);
                    }
                }
            } else {
                try {
                    if (!mainFragmentsStack.isEmpty()) {
                        ArrayList<BaseFragment> arrayList6 = mainFragmentsStack;
                        BaseFragment fragment3 = arrayList6.get(arrayList6.size() - 1);
                        if (error != null) {
                            try {
                                if (error.text != null && error.text.startsWith("FLOOD_WAIT")) {
                                    BulletinFactory.of(fragment3).createErrorBulletin(LocaleController.getString("FloodWait", R.string.FloodWait)).show();
                                }
                            } catch (Exception e3) {
                                e = e3;
                                FileLog.e(e);
                                if (!hideProgressDialog) {
                                }
                            }
                        }
                        if (AndroidUtilities.isNumeric(username)) {
                            BulletinFactory.of(fragment3).createErrorBulletin(LocaleController.getString("NoPhoneFound", R.string.NoPhoneFound)).show();
                        } else {
                            BulletinFactory.of(fragment3).createErrorBulletin(LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound)).show();
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                }
            }
            if (!hideProgressDialog) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
        }
    }

    /* renamed from: lambda$runLinkRequest$36$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3631lambda$runLinkRequest$36$orgtelegramuiLaunchActivity(final int intentAccount, final String attachMenuBotChoose, final TLRPC.User user, final String setAsAttachBot, final TLRPC.TL_contacts_resolvedPeer res, final TLObject response1, TLRPC.TL_error error1) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3630lambda$runLinkRequest$35$orgtelegramuiLaunchActivity(response1, intentAccount, attachMenuBotChoose, user, setAsAttachBot, res);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$35$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3630lambda$runLinkRequest$35$orgtelegramuiLaunchActivity(TLObject response1, final int intentAccount, String attachMenuBotChoose, final TLRPC.User user, final String setAsAttachBot, final TLRPC.TL_contacts_resolvedPeer res) {
        DialogsActivity dialogsActivity;
        String[] split;
        if (response1 instanceof TLRPC.TL_attachMenuBotsBot) {
            TLRPC.TL_attachMenuBotsBot attachMenuBotsBot = (TLRPC.TL_attachMenuBotsBot) response1;
            MessagesController.getInstance(intentAccount).putUsers(attachMenuBotsBot.users, false);
            TLRPC.TL_attachMenuBot attachMenuBot = attachMenuBotsBot.bot;
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            final BaseFragment lastFragment = arrayList.get(arrayList.size() - 1);
            List<String> chooserTargets = new ArrayList<>();
            if (!TextUtils.isEmpty(attachMenuBotChoose)) {
                for (String target : attachMenuBotChoose.split(" ")) {
                    if (MediaDataController.canShowAttachMenuBotForTarget(attachMenuBot, target)) {
                        chooserTargets.add(target);
                    }
                }
            }
            if (!chooserTargets.isEmpty()) {
                Bundle args = new Bundle();
                args.putInt("dialogsType", 14);
                args.putBoolean("onlySelect", true);
                args.putBoolean("allowGroups", chooserTargets.contains("groups"));
                args.putBoolean("allowUsers", chooserTargets.contains("users"));
                args.putBoolean("allowChannels", chooserTargets.contains("channels"));
                args.putBoolean("allowBots", chooserTargets.contains("bots"));
                DialogsActivity dialogsActivity2 = new DialogsActivity(args);
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final void didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList2, CharSequence charSequence, boolean z) {
                        LaunchActivity.this.m3626lambda$runLinkRequest$31$orgtelegramuiLaunchActivity(user, setAsAttachBot, intentAccount, dialogsActivity3, arrayList2, charSequence, z);
                    }
                });
                dialogsActivity = dialogsActivity2;
            } else {
                dialogsActivity = null;
            }
            if (attachMenuBot.inactive) {
                AttachBotIntroTopView introTopView = new AttachBotIntroTopView(this);
                introTopView.setColor(Theme.getColor(Theme.key_chat_attachContactIcon));
                introTopView.setBackgroundColor(Theme.getColor(Theme.key_dialogTopBackground));
                introTopView.setAttachBot(attachMenuBot);
                final DialogsActivity dialogsActivity3 = dialogsActivity;
                new AlertDialog.Builder(this).setTopView(introTopView).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRequestAttachPermission", R.string.BotRequestAttachPermission, UserObject.getUserName(user)))).setPositiveButton(LocaleController.getString((int) R.string.BotAddToMenu), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        LaunchActivity.this.m3629lambda$runLinkRequest$34$orgtelegramuiLaunchActivity(intentAccount, res, dialogsActivity3, lastFragment, user, setAsAttachBot, dialogInterface, i);
                    }
                }).setNegativeButton(LocaleController.getString((int) R.string.Cancel), null).show();
                return;
            } else if (dialogsActivity != null) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(dialogsActivity);
                return;
            } else if (lastFragment instanceof ChatActivity) {
                ((ChatActivity) lastFragment).openAttachBotLayout(user.id, setAsAttachBot);
                return;
            } else {
                BulletinFactory.of(lastFragment).createErrorBulletin(LocaleController.getString((int) R.string.BotAlreadyAddedToAttachMenu)).show();
                return;
            }
        }
        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
        BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString((int) R.string.BotCantAddToAttachMenu)).show();
    }

    /* renamed from: lambda$runLinkRequest$31$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3626lambda$runLinkRequest$31$orgtelegramuiLaunchActivity(TLRPC.User user, String setAsAttachBot, int intentAccount, DialogsActivity fragment, ArrayList dids, CharSequence message1, boolean param) {
        long did = ((Long) dids.get(0)).longValue();
        Bundle args1 = new Bundle();
        args1.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(did)) {
            args1.putInt("enc_id", DialogObject.getEncryptedChatId(did));
        } else if (DialogObject.isUserDialog(did)) {
            args1.putLong("user_id", did);
        } else {
            args1.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -did);
        }
        args1.putString("attach_bot", user.username);
        if (setAsAttachBot != null) {
            args1.putString("attach_bot_start_command", setAsAttachBot);
        }
        if (MessagesController.getInstance(intentAccount).checkCanOpenChat(args1, fragment)) {
            NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(args1), true, false, true, false);
        }
    }

    /* renamed from: lambda$runLinkRequest$34$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3629lambda$runLinkRequest$34$orgtelegramuiLaunchActivity(final int intentAccount, TLRPC.TL_contacts_resolvedPeer res, final DialogsActivity dialogsActivity, final BaseFragment lastFragment, final TLRPC.User user, final String setAsAttachBot, DialogInterface dialog, int which) {
        TLRPC.TL_messages_toggleBotInAttachMenu botRequest = new TLRPC.TL_messages_toggleBotInAttachMenu();
        botRequest.bot = MessagesController.getInstance(intentAccount).getInputUser(res.peer.user_id);
        botRequest.enabled = true;
        ConnectionsManager.getInstance(intentAccount).sendRequest(botRequest, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.m3628lambda$runLinkRequest$33$orgtelegramuiLaunchActivity(intentAccount, dialogsActivity, lastFragment, user, setAsAttachBot, tLObject, tL_error);
            }
        }, 66);
    }

    /* renamed from: lambda$runLinkRequest$33$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3628lambda$runLinkRequest$33$orgtelegramuiLaunchActivity(final int intentAccount, final DialogsActivity dialogsActivity, final BaseFragment lastFragment, final TLRPC.User user, final String setAsAttachBot, final TLObject response2, TLRPC.TL_error error2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3627lambda$runLinkRequest$32$orgtelegramuiLaunchActivity(response2, intentAccount, dialogsActivity, lastFragment, user, setAsAttachBot);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$32$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3627lambda$runLinkRequest$32$orgtelegramuiLaunchActivity(TLObject response2, int intentAccount, DialogsActivity dialogsActivity, BaseFragment lastFragment, TLRPC.User user, String setAsAttachBot) {
        if (response2 instanceof TLRPC.TL_boolTrue) {
            MediaDataController.getInstance(intentAccount).loadAttachMenuBots(false, true);
            if (dialogsActivity != null) {
                m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(dialogsActivity);
            } else if (lastFragment instanceof ChatActivity) {
                ((ChatActivity) lastFragment).openAttachBotLayout(user.id, setAsAttachBot);
            }
        }
    }

    /* renamed from: lambda$runLinkRequest$37$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3632lambda$runLinkRequest$37$orgtelegramuiLaunchActivity(String game, int intentAccount, TLRPC.TL_contacts_resolvedPeer res, DialogsActivity fragment1, ArrayList dids, CharSequence message1, boolean param) {
        long did = ((Long) dids.get(0)).longValue();
        TLRPC.TL_inputMediaGame inputMediaGame = new TLRPC.TL_inputMediaGame();
        inputMediaGame.id = new TLRPC.TL_inputGameShortName();
        inputMediaGame.id.short_name = game;
        inputMediaGame.id.bot_id = MessagesController.getInstance(intentAccount).getInputUser(res.users.get(0));
        SendMessagesHelper.getInstance(intentAccount).sendGame(MessagesController.getInstance(intentAccount).getInputPeer(did), inputMediaGame, 0L, 0L);
        Bundle args1 = new Bundle();
        args1.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(did)) {
            args1.putInt("enc_id", DialogObject.getEncryptedChatId(did));
        } else if (DialogObject.isUserDialog(did)) {
            args1.putLong("user_id", did);
        } else {
            args1.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -did);
        }
        if (MessagesController.getInstance(intentAccount).checkCanOpenChat(args1, fragment1)) {
            NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(args1), true, false, true, false);
        }
    }

    /* renamed from: lambda$runLinkRequest$42$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3637lambda$runLinkRequest$42$orgtelegramuiLaunchActivity(final int intentAccount, final TLRPC.User user, final String botChatAdminParams, final String botHash, final DialogsActivity fragment, DialogsActivity fragment12, ArrayList dids, CharSequence message1, boolean param) {
        final long did = ((Long) dids.get(0)).longValue();
        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-did));
        if (chat != null) {
            if (chat.creator || (chat.admin_rights != null && chat.admin_rights.add_admins)) {
                MessagesController.getInstance(intentAccount).checkIsInChat(chat, user, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
                    @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                    public final void run(boolean z, TLRPC.TL_chatAdminRights tL_chatAdminRights, String str) {
                        LaunchActivity.this.m3635lambda$runLinkRequest$40$orgtelegramuiLaunchActivity(botChatAdminParams, botHash, intentAccount, chat, fragment, user, did, z, tL_chatAdminRights, str);
                    }
                });
                return;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(LocaleController.getString("AddBot", R.string.AddBot));
        String chatName = chat == null ? "" : chat.title;
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(user), chatName)));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("AddBot", R.string.AddBot), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LaunchActivity.this.m3636lambda$runLinkRequest$41$orgtelegramuiLaunchActivity(did, intentAccount, user, botHash, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* renamed from: lambda$runLinkRequest$40$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3635lambda$runLinkRequest$40$orgtelegramuiLaunchActivity(final String botChatAdminParams, final String botHash, final int intentAccount, final TLRPC.Chat chat, final DialogsActivity fragment, final TLRPC.User user, final long did, final boolean isInChatAlready, final TLRPC.TL_chatAdminRights currentRights, final String currentRank) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3634lambda$runLinkRequest$39$orgtelegramuiLaunchActivity(botChatAdminParams, currentRights, isInChatAlready, botHash, intentAccount, chat, fragment, user, did, currentRank);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$39$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3634lambda$runLinkRequest$39$orgtelegramuiLaunchActivity(String botChatAdminParams, TLRPC.TL_chatAdminRights currentRights, boolean isInChatAlready, String botHash, final int intentAccount, final TLRPC.Chat chat, final DialogsActivity fragment, TLRPC.User user, long did, String currentRank) {
        TLRPC.TL_chatAdminRights requestingRights;
        TLRPC.TL_chatAdminRights editRights;
        boolean z = true;
        if (botChatAdminParams == null) {
            requestingRights = null;
        } else {
            String[] adminParams = botChatAdminParams.split("\\+| ");
            TLRPC.TL_chatAdminRights requestingRights2 = new TLRPC.TL_chatAdminRights();
            for (String adminParam : adminParams) {
                char c = 65535;
                switch (adminParam.hashCode()) {
                    case -2110462504:
                        if (adminParam.equals("ban_users")) {
                            c = 6;
                            break;
                        }
                        break;
                    case -2095811475:
                        if (adminParam.equals("anonymous")) {
                            c = 14;
                            break;
                        }
                        break;
                    case -1654794275:
                        if (adminParam.equals("change_info")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1593320096:
                        if (adminParam.equals("delete_messages")) {
                            c = 5;
                            break;
                        }
                        break;
                    case -939200543:
                        if (adminParam.equals("edit_messages")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 22162680:
                        if (adminParam.equals("manage_call")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 22169074:
                        if (adminParam.equals("manage_chat")) {
                            c = '\f';
                            break;
                        }
                        break;
                    case 106069776:
                        if (adminParam.equals("other")) {
                            c = '\r';
                            break;
                        }
                        break;
                    case 449085338:
                        if (adminParam.equals("promote_members")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 632157522:
                        if (adminParam.equals("invite_users")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 758599179:
                        if (adminParam.equals("post_messages")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1357805750:
                        if (adminParam.equals("pin_messages")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 1529816162:
                        if (adminParam.equals("add_admins")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1542893206:
                        if (adminParam.equals("restrict_members")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 1641337725:
                        if (adminParam.equals("manage_video_chats")) {
                            c = '\n';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        requestingRights2.change_info = true;
                        break;
                    case 1:
                        requestingRights2.post_messages = true;
                        break;
                    case 2:
                        requestingRights2.edit_messages = true;
                        break;
                    case 3:
                    case 4:
                        requestingRights2.add_admins = true;
                        break;
                    case 5:
                        requestingRights2.delete_messages = true;
                        break;
                    case 6:
                    case 7:
                        requestingRights2.ban_users = true;
                        break;
                    case '\b':
                        requestingRights2.invite_users = true;
                        break;
                    case '\t':
                        requestingRights2.pin_messages = true;
                        break;
                    case '\n':
                    case 11:
                        requestingRights2.manage_call = true;
                        break;
                    case '\f':
                    case '\r':
                        requestingRights2.other = true;
                        break;
                    case 14:
                        requestingRights2.anonymous = true;
                        break;
                }
            }
            requestingRights = requestingRights2;
        }
        if (requestingRights == null && currentRights == null) {
            editRights = null;
        } else if (requestingRights == null) {
            editRights = currentRights;
        } else if (currentRights == null) {
            TLRPC.TL_chatAdminRights editRights2 = requestingRights;
            editRights = editRights2;
        } else {
            currentRights.change_info = requestingRights.change_info || currentRights.change_info;
            currentRights.post_messages = requestingRights.post_messages || currentRights.post_messages;
            currentRights.edit_messages = requestingRights.edit_messages || currentRights.edit_messages;
            currentRights.add_admins = requestingRights.add_admins || currentRights.add_admins;
            currentRights.delete_messages = requestingRights.delete_messages || currentRights.delete_messages;
            currentRights.ban_users = requestingRights.ban_users || currentRights.ban_users;
            currentRights.invite_users = requestingRights.invite_users || currentRights.invite_users;
            currentRights.pin_messages = requestingRights.pin_messages || currentRights.pin_messages;
            currentRights.manage_call = requestingRights.manage_call || currentRights.manage_call;
            currentRights.anonymous = requestingRights.anonymous || currentRights.anonymous;
            if (!requestingRights.other && !currentRights.other) {
                z = false;
            }
            currentRights.other = z;
            editRights = currentRights;
        }
        if (isInChatAlready && requestingRights == null && !TextUtils.isEmpty(botHash)) {
            Runnable onFinish = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.m3633lambda$runLinkRequest$38$orgtelegramuiLaunchActivity(intentAccount, chat, fragment);
                }
            };
            MessagesController.getInstance(this.currentAccount).addUserToChat(chat.id, user, 0, botHash, fragment, true, onFinish, null);
            return;
        }
        ChatRightsEditActivity editRightsActivity = new ChatRightsEditActivity(user.id, -did, editRights, null, null, currentRank, 2, true, !isInChatAlready, botHash);
        editRightsActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.LaunchActivity.13
            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int rights, TLRPC.TL_chatAdminRights rightsAdmin, TLRPC.TL_chatBannedRights rightsBanned, String rank) {
                fragment.removeSelfFromStack();
                NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC.User user2) {
            }
        });
        this.actionBarLayout.presentFragment(editRightsActivity, false);
    }

    /* renamed from: lambda$runLinkRequest$38$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3633lambda$runLinkRequest$38$orgtelegramuiLaunchActivity(int intentAccount, TLRPC.Chat chat, DialogsActivity fragment) {
        NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        Bundle args1 = new Bundle();
        args1.putBoolean("scrollToTopOnResume", true);
        args1.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, chat.id);
        if (!MessagesController.getInstance(this.currentAccount).checkCanOpenChat(args1, fragment)) {
            return;
        }
        ChatActivity chatActivity = new ChatActivity(args1);
        presentFragment(chatActivity, true, false);
    }

    /* renamed from: lambda$runLinkRequest$41$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3636lambda$runLinkRequest$41$orgtelegramuiLaunchActivity(long did, int intentAccount, TLRPC.User user, String botHash, DialogInterface di, int i) {
        Bundle args12 = new Bundle();
        args12.putBoolean("scrollToTopOnResume", true);
        args12.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -did);
        ChatActivity chatActivity = new ChatActivity(args12);
        NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(intentAccount).addUserToChat(-did, user, 0, botHash, chatActivity, null);
        this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$14 */
    /* loaded from: classes4.dex */
    public class AnonymousClass14 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ BaseFragment val$lastFragment;
        final /* synthetic */ String val$livestream;
        final /* synthetic */ AlertDialog val$progressDialog;

        AnonymousClass14(AlertDialog alertDialog, String str, BaseFragment baseFragment, long j, Bundle bundle) {
            LaunchActivity.this = this$0;
            this.val$progressDialog = alertDialog;
            this.val$livestream = str;
            this.val$lastFragment = baseFragment;
            this.val$dialog_id = j;
            this.val$args = bundle;
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onMessagesLoaded(boolean fromCache) {
            BaseFragment voipLastFragment;
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!LaunchActivity.this.isFinishing()) {
                if (this.val$livestream != null) {
                    BaseFragment baseFragment = this.val$lastFragment;
                    if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == this.val$dialog_id) {
                        voipLastFragment = this.val$lastFragment;
                        final String str = this.val$livestream;
                        final long j = this.val$dialog_id;
                        final BaseFragment baseFragment2 = voipLastFragment;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$14$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.AnonymousClass14.this.m3677lambda$onMessagesLoaded$2$orgtelegramuiLaunchActivity$14(str, j, baseFragment2);
                            }
                        }, 150L);
                    }
                }
                voipLastFragment = new ChatActivity(this.val$args);
                LaunchActivity.this.actionBarLayout.presentFragment(voipLastFragment);
                final String str2 = this.val$livestream;
                final long j2 = this.val$dialog_id;
                final BaseFragment baseFragment22 = voipLastFragment;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$14$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.AnonymousClass14.this.m3677lambda$onMessagesLoaded$2$orgtelegramuiLaunchActivity$14(str2, j2, baseFragment22);
                    }
                }, 150L);
            }
        }

        /* renamed from: lambda$onMessagesLoaded$2$org-telegram-ui-LaunchActivity$14 */
        public /* synthetic */ void m3677lambda$onMessagesLoaded$2$orgtelegramuiLaunchActivity$14(String livestream, final long dialog_id, final BaseFragment voipLastFragment) {
            if (livestream != null) {
                final AccountInstance accountInstance = AccountInstance.getInstance(LaunchActivity.this.currentAccount);
                boolean z = false;
                ChatObject.Call cachedCall = accountInstance.getMessagesController().getGroupCall(-dialog_id, false);
                if (cachedCall != null) {
                    TLRPC.Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(-dialog_id));
                    TLRPC.InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(dialog_id);
                    if (cachedCall == null || !cachedCall.call.rtmp_stream) {
                        z = true;
                    }
                    VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, voipLastFragment, accountInstance);
                    return;
                }
                TLRPC.ChatFull chatFull = accountInstance.getMessagesController().getChatFull(-dialog_id);
                if (chatFull != null) {
                    if (chatFull.call != null) {
                        accountInstance.getMessagesController().getGroupCall(-dialog_id, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$14$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.AnonymousClass14.this.m3676lambda$onMessagesLoaded$1$orgtelegramuiLaunchActivity$14(accountInstance, dialog_id, voipLastFragment);
                            }
                        });
                    } else if (voipLastFragment.getParentActivity() != null) {
                        BulletinFactory.of(voipLastFragment).createSimpleBulletin(R.raw.linkbroken, LocaleController.getString("InviteExpired", R.string.InviteExpired)).show();
                    }
                }
            }
        }

        /* renamed from: lambda$onMessagesLoaded$1$org-telegram-ui-LaunchActivity$14 */
        public /* synthetic */ void m3676lambda$onMessagesLoaded$1$orgtelegramuiLaunchActivity$14(final AccountInstance accountInstance, final long dialog_id, final BaseFragment voipLastFragment) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$14$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.AnonymousClass14.this.m3675lambda$onMessagesLoaded$0$orgtelegramuiLaunchActivity$14(accountInstance, dialog_id, voipLastFragment);
                }
            });
        }

        /* renamed from: lambda$onMessagesLoaded$0$org-telegram-ui-LaunchActivity$14 */
        public /* synthetic */ void m3675lambda$onMessagesLoaded$0$orgtelegramuiLaunchActivity$14(AccountInstance accountInstance, long dialog_id, BaseFragment voipLastFragment) {
            boolean z = false;
            ChatObject.Call call = accountInstance.getMessagesController().getGroupCall(-dialog_id, false);
            TLRPC.Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(-dialog_id));
            TLRPC.InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(dialog_id);
            if (call == null || !call.call.rtmp_stream) {
                z = true;
            }
            VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, voipLastFragment, accountInstance);
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                BaseFragment fragment = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                AlertsCreator.showSimpleAlert(fragment, LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
            }
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* renamed from: lambda$runLinkRequest$47$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3641lambda$runLinkRequest$47$orgtelegramuiLaunchActivity(final int intentAccount, final AlertDialog progressDialog, final String group, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3640lambda$runLinkRequest$46$orgtelegramuiLaunchActivity(error, response, intentAccount, progressDialog, group);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0085, code lost:
        if (r1.checkCanOpenChat(r1, r2.get(r2.size() - 1)) != false) goto L24;
     */
    /* renamed from: lambda$runLinkRequest$46$org-telegram-ui-LaunchActivity */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m3640lambda$runLinkRequest$46$orgtelegramuiLaunchActivity(TLRPC.TL_error error, TLObject response, int intentAccount, final AlertDialog progressDialog, String group) {
        boolean hideProgressDialog;
        if (!isFinishing()) {
            boolean hideProgressDialog2 = true;
            ChatActivity.ThemeDelegate themeDelegate = null;
            if (error != null || this.actionBarLayout == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                if (error.text.startsWith("FLOOD_WAIT")) {
                    builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
                } else if (error.text.startsWith("INVITE_HASH_EXPIRED")) {
                    builder.setTitle(LocaleController.getString("ExpiredLink", R.string.ExpiredLink));
                    builder.setMessage(LocaleController.getString("InviteExpired", R.string.InviteExpired));
                } else {
                    builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
                }
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                showAlertDialog(builder);
                hideProgressDialog = true;
            } else {
                final TLRPC.ChatInvite invite = (TLRPC.ChatInvite) response;
                if (invite.chat != null && (!ChatObject.isLeftFromChat(invite.chat) || (!invite.chat.kicked && (!TextUtils.isEmpty(invite.chat.username) || (invite instanceof TLRPC.TL_chatInvitePeek) || invite.chat.has_geo)))) {
                    MessagesController.getInstance(intentAccount).putChat(invite.chat, false);
                    ArrayList<TLRPC.Chat> chats = new ArrayList<>();
                    chats.add(invite.chat);
                    MessagesStorage.getInstance(intentAccount).putUsersAndChats(null, chats, false, true);
                    final Bundle args = new Bundle();
                    args.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, invite.chat.id);
                    if (!mainFragmentsStack.isEmpty()) {
                        MessagesController messagesController = MessagesController.getInstance(intentAccount);
                        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                    }
                    final boolean[] canceled = new boolean[1];
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
                        @Override // android.content.DialogInterface.OnCancelListener
                        public final void onCancel(DialogInterface dialogInterface) {
                            LaunchActivity.lambda$runLinkRequest$45(canceled, dialogInterface);
                        }
                    });
                    MessagesController.getInstance(intentAccount).ensureMessagesLoaded(-invite.chat.id, 0, new MessagesController.MessagesLoadedCallback() { // from class: org.telegram.ui.LaunchActivity.15
                        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                        public void onMessagesLoaded(boolean fromCache) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            if (canceled[0]) {
                                return;
                            }
                            ChatActivity fragment = new ChatActivity(args);
                            TLRPC.ChatInvite chatInvite = invite;
                            if (chatInvite instanceof TLRPC.TL_chatInvitePeek) {
                                fragment.setChatInvite(chatInvite);
                            }
                            LaunchActivity.this.actionBarLayout.presentFragment(fragment);
                        }

                        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                        public void onError() {
                            if (!LaunchActivity.this.isFinishing()) {
                                BaseFragment fragment = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                AlertsCreator.showSimpleAlert(fragment, LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
                            }
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    });
                    hideProgressDialog2 = false;
                } else {
                    ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                    BaseFragment fragment = arrayList2.get(arrayList2.size() - 1);
                    if (fragment instanceof ChatActivity) {
                        themeDelegate = ((ChatActivity) fragment).themeDelegate;
                    }
                    fragment.showDialog(new JoinGroupAlert(this, invite, group, fragment, themeDelegate));
                }
                hideProgressDialog = hideProgressDialog2;
            }
            if (hideProgressDialog) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$45(boolean[] canceled, DialogInterface dialog) {
        canceled[0] = true;
    }

    /* renamed from: lambda$runLinkRequest$49$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3643lambda$runLinkRequest$49$orgtelegramuiLaunchActivity(final int intentAccount, final AlertDialog progressDialog, final TLObject response, final TLRPC.TL_error error) {
        if (error == null) {
            TLRPC.Updates updates = (TLRPC.Updates) response;
            MessagesController.getInstance(intentAccount).processUpdates(updates, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3642lambda$runLinkRequest$48$orgtelegramuiLaunchActivity(progressDialog, error, response, intentAccount);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$48$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3642lambda$runLinkRequest$48$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLRPC.TL_error error, TLObject response, int intentAccount) {
        if (!isFinishing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (error == null) {
                if (this.actionBarLayout != null) {
                    TLRPC.Updates updates = (TLRPC.Updates) response;
                    if (!updates.chats.isEmpty()) {
                        TLRPC.Chat chat = updates.chats.get(0);
                        chat.left = false;
                        chat.kicked = false;
                        MessagesController.getInstance(intentAccount).putUsers(updates.users, false);
                        MessagesController.getInstance(intentAccount).putChats(updates.chats, false);
                        Bundle args = new Bundle();
                        args.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, chat.id);
                        if (!mainFragmentsStack.isEmpty()) {
                            MessagesController messagesController = MessagesController.getInstance(intentAccount);
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            if (!messagesController.checkCanOpenChat(args, arrayList.get(arrayList.size() - 1))) {
                                return;
                            }
                        }
                        ChatActivity fragment = new ChatActivity(args);
                        NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                        this.actionBarLayout.presentFragment(fragment, false, true, true, false);
                        return;
                    }
                    return;
                }
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (error.text.startsWith("FLOOD_WAIT")) {
                builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
            } else if (error.text.equals("USERS_TOO_MUCH")) {
                builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", R.string.JoinToGroupErrorFull));
            } else {
                builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
            }
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showAlertDialog(builder);
        }
    }

    /* renamed from: lambda$runLinkRequest$50$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3644lambda$runLinkRequest$50$orgtelegramuiLaunchActivity(boolean hasUrl, int intentAccount, String message, DialogsActivity fragment13, ArrayList dids, CharSequence m, boolean param) {
        long did = ((Long) dids.get(0)).longValue();
        Bundle args13 = new Bundle();
        args13.putBoolean("scrollToTopOnResume", true);
        args13.putBoolean("hasUrl", hasUrl);
        if (DialogObject.isEncryptedDialog(did)) {
            args13.putInt("enc_id", DialogObject.getEncryptedChatId(did));
        } else if (DialogObject.isUserDialog(did)) {
            args13.putLong("user_id", did);
        } else {
            args13.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -did);
        }
        if (MessagesController.getInstance(intentAccount).checkCanOpenChat(args13, fragment13)) {
            NotificationCenter.getInstance(intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            MediaDataController.getInstance(intentAccount).saveDraft(did, 0, message, null, null, false);
            this.actionBarLayout.presentFragment(new ChatActivity(args13), true, false, true, false);
        }
    }

    /* renamed from: lambda$runLinkRequest$54$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3648lambda$runLinkRequest$54$orgtelegramuiLaunchActivity(int[] requestId, final int intentAccount, final AlertDialog progressDialog, final TLRPC.TL_account_getAuthorizationForm req, final String payload, final String nonce, final String callbackUrl, TLObject response, final TLRPC.TL_error error) {
        final TLRPC.TL_account_authorizationForm authorizationForm = (TLRPC.TL_account_authorizationForm) response;
        if (authorizationForm != null) {
            TLRPC.TL_account_getPassword req2 = new TLRPC.TL_account_getPassword();
            requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.m3646lambda$runLinkRequest$52$orgtelegramuiLaunchActivity(progressDialog, intentAccount, authorizationForm, req, payload, nonce, callbackUrl, tLObject, tL_error);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3647lambda$runLinkRequest$53$orgtelegramuiLaunchActivity(progressDialog, error);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$52$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3646lambda$runLinkRequest$52$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final int intentAccount, final TLRPC.TL_account_authorizationForm authorizationForm, final TLRPC.TL_account_getAuthorizationForm req, final String payload, final String nonce, final String callbackUrl, final TLObject response1, TLRPC.TL_error error1) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3645lambda$runLinkRequest$51$orgtelegramuiLaunchActivity(progressDialog, response1, intentAccount, authorizationForm, req, payload, nonce, callbackUrl);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$51$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3645lambda$runLinkRequest$51$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLObject response1, int intentAccount, TLRPC.TL_account_authorizationForm authorizationForm, TLRPC.TL_account_getAuthorizationForm req, String payload, String nonce, String callbackUrl) {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (response1 != null) {
            TLRPC.TL_account_password accountPassword = (TLRPC.TL_account_password) response1;
            MessagesController.getInstance(intentAccount).putUsers(authorizationForm.users, false);
            m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new PassportActivity(5, req.bot_id, req.scope, req.public_key, payload, nonce, callbackUrl, authorizationForm, accountPassword));
        }
    }

    /* renamed from: lambda$runLinkRequest$53$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3647lambda$runLinkRequest$53$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLRPC.TL_error error) {
        try {
            progressDialog.dismiss();
            if ("APP_VERSION_OUTDATED".equals(error.text)) {
                AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error.text));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: lambda$runLinkRequest$56$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3650lambda$runLinkRequest$56$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3649lambda$runLinkRequest$55$orgtelegramuiLaunchActivity(progressDialog, response);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$55$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3649lambda$runLinkRequest$55$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLObject response) {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (response instanceof TLRPC.TL_help_deepLinkInfo) {
            TLRPC.TL_help_deepLinkInfo res = (TLRPC.TL_help_deepLinkInfo) response;
            AlertsCreator.showUpdateAppAlert(this, res.message, res.update_app);
        }
    }

    /* renamed from: lambda$runLinkRequest$58$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3652lambda$runLinkRequest$58$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3651lambda$runLinkRequest$57$orgtelegramuiLaunchActivity(progressDialog, response, error);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$57$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3651lambda$runLinkRequest$57$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLObject response, TLRPC.TL_error error) {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (response instanceof TLRPC.TL_langPackLanguage) {
            TLRPC.TL_langPackLanguage res = (TLRPC.TL_langPackLanguage) response;
            showAlertDialog(AlertsCreator.createLanguageAlert(this, res));
        } else if (error != null) {
            if ("LANG_CODE_NOT_SUPPORTED".equals(error.text)) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", R.string.LanguageUnsupportedError)));
                return;
            }
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error.text));
        }
    }

    /* renamed from: lambda$runLinkRequest$61$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3655lambda$runLinkRequest$61$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final TLRPC.TL_wallPaper wallPaper, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3654lambda$runLinkRequest$60$orgtelegramuiLaunchActivity(progressDialog, response, wallPaper, error);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: lambda$runLinkRequest$60$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3654lambda$runLinkRequest$60$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLObject response, TLRPC.TL_wallPaper wallPaper, TLRPC.TL_error error) {
        Object object;
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (response instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper res = (TLRPC.TL_wallPaper) response;
            if (res.pattern) {
                WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(res.slug, wallPaper.settings.background_color, wallPaper.settings.second_background_color, wallPaper.settings.third_background_color, wallPaper.settings.fourth_background_color, AndroidUtilities.getWallpaperRotation(wallPaper.settings.rotation, false), wallPaper.settings.intensity / 100.0f, wallPaper.settings.motion, null);
                colorWallpaper.pattern = res;
                object = colorWallpaper;
            } else {
                object = res;
            }
            ThemePreviewActivity wallpaperActivity = new ThemePreviewActivity(object, null, true, false);
            wallpaperActivity.setInitialModes(wallPaper.settings.blur, wallPaper.settings.motion);
            m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(wallpaperActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error.text));
    }

    /* renamed from: lambda$runLinkRequest$62$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3656lambda$runLinkRequest$62$orgtelegramuiLaunchActivity() {
        this.loadingThemeFileName = null;
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeProgressDialog = null;
        this.loadingTheme = null;
    }

    /* renamed from: lambda$runLinkRequest$64$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3658lambda$runLinkRequest$64$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3657lambda$runLinkRequest$63$orgtelegramuiLaunchActivity(response, progressDialog, error);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$63$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3657lambda$runLinkRequest$63$orgtelegramuiLaunchActivity(TLObject response, AlertDialog progressDialog, TLRPC.TL_error error) {
        TLRPC.TL_wallPaper object;
        int notFound = 2;
        if (response instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme t = (TLRPC.TL_theme) response;
            TLRPC.ThemeSettings settings = null;
            if (t.settings.size() > 0) {
                TLRPC.ThemeSettings settings2 = t.settings.get(0);
                settings = settings2;
            }
            if (settings != null) {
                String key = Theme.getBaseThemeKey(settings);
                Theme.ThemeInfo info = Theme.getTheme(key);
                if (info != null) {
                    if (settings.wallpaper instanceof TLRPC.TL_wallPaper) {
                        object = (TLRPC.TL_wallPaper) settings.wallpaper;
                        File path = FileLoader.getInstance(this.currentAccount).getPathToAttach(object.document, true);
                        if (!path.exists()) {
                            this.loadingThemeProgressDialog = progressDialog;
                            this.loadingThemeAccent = true;
                            this.loadingThemeInfo = info;
                            this.loadingTheme = t;
                            this.loadingThemeWallpaper = object;
                            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(object.document);
                            FileLoader.getInstance(this.currentAccount).loadFile(object.document, object, 1, 1);
                            return;
                        }
                    } else {
                        object = null;
                    }
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    notFound = 0;
                    openThemeAccentPreview(t, object, info);
                } else {
                    notFound = 1;
                }
            } else if (t.document != null) {
                this.loadingThemeAccent = false;
                this.loadingTheme = t;
                this.loadingThemeFileName = FileLoader.getAttachFileName(t.document);
                this.loadingThemeProgressDialog = progressDialog;
                FileLoader.getInstance(this.currentAccount).loadFile(this.loadingTheme.document, t, 1, 1);
                notFound = 0;
            } else {
                notFound = 1;
            }
        } else if (error != null && "THEME_FORMAT_INVALID".equals(error.text)) {
            notFound = 1;
        }
        if (notFound != 0) {
            try {
                progressDialog.dismiss();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            if (notFound == 1) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", R.string.Theme), LocaleController.getString("ThemeNotSupported", R.string.ThemeNotSupported)));
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", R.string.Theme), LocaleController.getString("ThemeNotFound", R.string.ThemeNotFound)));
            }
        }
    }

    /* renamed from: lambda$runLinkRequest$66$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3660lambda$runLinkRequest$66$orgtelegramuiLaunchActivity(final int[] requestId, final int intentAccount, final AlertDialog progressDialog, final Integer messageId, final Integer commentId, final Integer threadId, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3659lambda$runLinkRequest$65$orgtelegramuiLaunchActivity(response, requestId, intentAccount, progressDialog, messageId, commentId, threadId);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* renamed from: lambda$runLinkRequest$65$org-telegram-ui-LaunchActivity */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m3659lambda$runLinkRequest$65$orgtelegramuiLaunchActivity(TLObject response, int[] requestId, int intentAccount, AlertDialog progressDialog, Integer messageId, Integer commentId, Integer threadId) {
        boolean notFound;
        if (response instanceof TLRPC.TL_messages_chats) {
            TLRPC.TL_messages_chats res = (TLRPC.TL_messages_chats) response;
            if (!res.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(res.chats, false);
                requestId[0] = runCommentRequest(intentAccount, progressDialog, messageId, commentId, threadId, res.chats.get(0));
                notFound = false;
                if (!notFound) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", R.string.LinkNotFound)));
                    return;
                }
                return;
            }
        }
        notFound = true;
        if (!notFound) {
        }
    }

    /* renamed from: lambda$runLinkRequest$69$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3663lambda$runLinkRequest$69$orgtelegramuiLaunchActivity(final Bundle args, Long channelId, int[] requestId, final AlertDialog progressDialog, final BaseFragment lastFragment, final int intentAccount) {
        if (!this.actionBarLayout.presentFragment(new ChatActivity(args))) {
            TLRPC.TL_channels_getChannels req = new TLRPC.TL_channels_getChannels();
            TLRPC.TL_inputChannel inputChannel = new TLRPC.TL_inputChannel();
            inputChannel.channel_id = channelId.longValue();
            req.id.add(inputChannel);
            requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.m3662lambda$runLinkRequest$68$orgtelegramuiLaunchActivity(progressDialog, lastFragment, intentAccount, args, tLObject, tL_error);
                }
            });
        }
    }

    /* renamed from: lambda$runLinkRequest$68$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3662lambda$runLinkRequest$68$orgtelegramuiLaunchActivity(final AlertDialog progressDialog, final BaseFragment lastFragment, final int intentAccount, final Bundle args, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3661lambda$runLinkRequest$67$orgtelegramuiLaunchActivity(progressDialog, response, lastFragment, intentAccount, args);
            }
        });
    }

    /* renamed from: lambda$runLinkRequest$67$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3661lambda$runLinkRequest$67$orgtelegramuiLaunchActivity(AlertDialog progressDialog, TLObject response, BaseFragment lastFragment, int intentAccount, Bundle args) {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        boolean notFound = true;
        if (response instanceof TLRPC.TL_messages_chats) {
            TLRPC.TL_messages_chats res = (TLRPC.TL_messages_chats) response;
            if (!res.chats.isEmpty()) {
                notFound = false;
                MessagesController.getInstance(this.currentAccount).putChats(res.chats, false);
                res.chats.get(0);
                if (lastFragment == null || MessagesController.getInstance(intentAccount).checkCanOpenChat(args, lastFragment)) {
                    this.actionBarLayout.presentFragment(new ChatActivity(args));
                }
            }
        }
        if (notFound) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", R.string.LinkNotFound)));
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$70(int intentAccount, int[] requestId, Runnable cancelRunnableFinal, DialogInterface dialog) {
        ConnectionsManager.getInstance(intentAccount).cancelRequest(requestId[0], true);
        if (cancelRunnableFinal != null) {
            cancelRunnableFinal.run();
        }
    }

    private List<TLRPC.TL_contact> findContacts(String userName, String userPhone, boolean allowSelf) {
        String userName2;
        String userPhone2;
        String userPhone3;
        String[] queries;
        ContactsController contactsController;
        List<TLRPC.TL_contact> contacts;
        MessagesController messagesController;
        boolean found;
        MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
        ContactsController contactsController2 = ContactsController.getInstance(this.currentAccount);
        List<TLRPC.TL_contact> contacts2 = new ArrayList<>(contactsController2.contacts);
        List<TLRPC.TL_contact> foundContacts = new ArrayList<>();
        if (userPhone == null) {
            userName2 = userName;
            userPhone2 = userPhone;
        } else {
            userPhone2 = PhoneFormat.stripExceptNumbers(userPhone);
            TLRPC.TL_contact contact = contactsController2.contactsByPhone.get(userPhone2);
            if (contact == null) {
                String shortUserPhone = userPhone2.substring(Math.max(0, userPhone2.length() - 7));
                contact = contactsController2.contactsByShortPhone.get(shortUserPhone);
            }
            if (contact != null) {
                TLRPC.User user = messagesController2.getUser(Long.valueOf(contact.user_id));
                if (user != null && (!user.self || allowSelf)) {
                    foundContacts.add(contact);
                } else {
                    userName2 = null;
                }
            }
            userName2 = userName;
        }
        if (foundContacts.isEmpty() && userName2 != null) {
            String query1 = userName2.trim().toLowerCase();
            if (!TextUtils.isEmpty(query1)) {
                String query2 = LocaleController.getInstance().getTranslitString(query1);
                if (query1.equals(query2) || query2.length() == 0) {
                    query2 = null;
                }
                String[] queries2 = {query1, query2};
                int i = 0;
                int size = contacts2.size();
                while (i < size) {
                    TLRPC.TL_contact contact2 = contacts2.get(i);
                    if (contact2 == null) {
                        messagesController = messagesController2;
                        contactsController = contactsController2;
                        contacts = contacts2;
                        userPhone3 = userPhone2;
                        queries = queries2;
                    } else {
                        String[] queries3 = queries2;
                        TLRPC.User user2 = messagesController2.getUser(Long.valueOf(contact2.user_id));
                        if (user2 == null) {
                            queries = queries3;
                            messagesController = messagesController2;
                            contactsController = contactsController2;
                            contacts = contacts2;
                            userPhone3 = userPhone2;
                        } else if (user2.self && !allowSelf) {
                            queries = queries3;
                            messagesController = messagesController2;
                            contactsController = contactsController2;
                            contacts = contacts2;
                            userPhone3 = userPhone2;
                        } else {
                            String[] names = new String[3];
                            names[0] = ContactsController.formatName(user2.first_name, user2.last_name).toLowerCase();
                            names[1] = LocaleController.getInstance().getTranslitString(names[0]);
                            if (names[0].equals(names[1])) {
                                names[1] = null;
                            }
                            if (UserObject.isReplyUser(user2)) {
                                names[2] = LocaleController.getString("RepliesTitle", R.string.RepliesTitle).toLowerCase();
                            } else if (user2.self) {
                                names[2] = LocaleController.getString("SavedMessages", R.string.SavedMessages).toLowerCase();
                            }
                            String[] queries4 = queries3;
                            int length = queries4.length;
                            boolean found2 = false;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= length) {
                                    messagesController = messagesController2;
                                    contactsController = contactsController2;
                                    contacts = contacts2;
                                    queries = queries4;
                                    userPhone3 = userPhone2;
                                    break;
                                }
                                messagesController = messagesController2;
                                String q = queries4[i2];
                                if (q == null) {
                                    contactsController = contactsController2;
                                    contacts = contacts2;
                                    queries = queries4;
                                    userPhone3 = userPhone2;
                                    found = found2;
                                } else {
                                    contactsController = contactsController2;
                                    int j = 0;
                                    while (true) {
                                        contacts = contacts2;
                                        if (j >= names.length) {
                                            queries = queries4;
                                            userPhone3 = userPhone2;
                                            found = found2;
                                            break;
                                        }
                                        String name = names[j];
                                        if (name == null) {
                                            queries = queries4;
                                            userPhone3 = userPhone2;
                                        } else if (name.startsWith(q)) {
                                            queries = queries4;
                                            userPhone3 = userPhone2;
                                            break;
                                        } else {
                                            queries = queries4;
                                            StringBuilder sb = new StringBuilder();
                                            userPhone3 = userPhone2;
                                            sb.append(" ");
                                            sb.append(q);
                                            if (name.contains(sb.toString())) {
                                                break;
                                            }
                                        }
                                        j++;
                                        contacts2 = contacts;
                                        queries4 = queries;
                                        userPhone2 = userPhone3;
                                    }
                                    found = true;
                                    if (!found && user2.username != null && user2.username.startsWith(q)) {
                                        found = true;
                                    }
                                    if (found) {
                                        foundContacts.add(contact2);
                                        break;
                                    }
                                }
                                i2++;
                                found2 = found;
                                messagesController2 = messagesController;
                                contacts2 = contacts;
                                contactsController2 = contactsController;
                                queries4 = queries;
                                userPhone2 = userPhone3;
                            }
                        }
                    }
                    i++;
                    messagesController2 = messagesController;
                    contacts2 = contacts;
                    contactsController2 = contactsController;
                    queries2 = queries;
                    userPhone2 = userPhone3;
                }
            }
        }
        return foundContacts;
    }

    private void createUpdateUI() {
        if (this.sideMenuContainer == null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.16
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
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                int width = View.MeasureSpec.getSize(widthMeasureSpec);
                if (this.lastGradientWidth != width) {
                    this.updateGradient = new LinearGradient(0.0f, 0.0f, width, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    this.lastGradientWidth = width;
                }
            }
        };
        this.updateLayout = frameLayout;
        frameLayout.setWillNotDraw(false);
        this.updateLayout.setVisibility(4);
        this.updateLayout.setTranslationY(AndroidUtilities.dp(44.0f));
        if (Build.VERSION.SDK_INT >= 21) {
            this.updateLayout.setBackground(Theme.getSelectorDrawable((int) Theme.ACTION_BAR_WHITE_SELECTOR_COLOR, false));
        }
        this.sideMenuContainer.addView(this.updateLayout, LayoutHelper.createFrame(-1, 44, 83));
        this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.m3594lambda$createUpdateUI$71$orgtelegramuiLaunchActivity(view);
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

    /* renamed from: lambda$createUpdateUI$71$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3594lambda$createUpdateUI$71$orgtelegramuiLaunchActivity(View v) {
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

    private void updateAppUpdateViews(boolean animated) {
        boolean showSize;
        if (this.sideMenuContainer == null) {
            return;
        }
        if (SharedConfig.isAppUpdateAvailable()) {
            final View prevUpdateLayout = this.updateLayout;
            createUpdateUI();
            this.updateSizeTextView.setText(AndroidUtilities.formatFileSize(SharedConfig.pendingAppUpdate.document.size));
            String fileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            File path = FileLoader.getInstance(this.currentAccount).getPathToAttach(SharedConfig.pendingAppUpdate.document, true);
            if (path.exists()) {
                this.updateLayoutIcon.setIcon(15, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow));
                showSize = false;
            } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                this.updateLayoutIcon.setIcon(3, true, false);
                this.updateLayoutIcon.setProgress(0.0f, false);
                Float p = ImageLoader.getInstance().getFileProgress(fileName);
                SimpleTextView simpleTextView = this.updateTextView;
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf((int) ((p != null ? p.floatValue() : 0.0f) * 100.0f));
                simpleTextView.setText(LocaleController.formatString("AppUpdateDownloading", R.string.AppUpdateDownloading, objArr));
                showSize = false;
            } else {
                this.updateLayoutIcon.setIcon(2, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdate", R.string.AppUpdate));
                showSize = true;
            }
            if (showSize) {
                if (this.updateSizeTextView.getTag() != null) {
                    if (!animated) {
                        this.updateSizeTextView.setAlpha(1.0f);
                        this.updateSizeTextView.setScaleX(1.0f);
                        this.updateSizeTextView.setScaleY(1.0f);
                    } else {
                        this.updateSizeTextView.setTag(null);
                        this.updateSizeTextView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(180L).start();
                    }
                }
            } else if (this.updateSizeTextView.getTag() == null) {
                if (animated) {
                    this.updateSizeTextView.setTag(1);
                    this.updateSizeTextView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(180L).start();
                } else {
                    this.updateSizeTextView.setAlpha(0.0f);
                    this.updateSizeTextView.setScaleX(0.0f);
                    this.updateSizeTextView.setScaleY(0.0f);
                }
            }
            if (this.updateLayout.getTag() != null) {
                return;
            }
            this.updateLayout.setVisibility(0);
            this.updateLayout.setTag(1);
            if (animated) {
                this.updateLayout.animate().translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(null).setDuration(180L).withEndAction(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$updateAppUpdateViews$72(prevUpdateLayout);
                    }
                }).start();
            } else {
                this.updateLayout.setTranslationY(0.0f);
                if (prevUpdateLayout != null) {
                    ViewGroup parent = (ViewGroup) prevUpdateLayout.getParent();
                    parent.removeView(prevUpdateLayout);
                }
            }
            this.sideMenu.setPadding(0, 0, 0, AndroidUtilities.dp(44.0f));
            return;
        }
        FrameLayout frameLayout = this.updateLayout;
        if (frameLayout == null || frameLayout.getTag() == null) {
            return;
        }
        this.updateLayout.setTag(null);
        if (animated) {
            this.updateLayout.animate().translationY(AndroidUtilities.dp(44.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.17
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
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

    public static /* synthetic */ void lambda$updateAppUpdateViews$72(View prevUpdateLayout) {
        if (prevUpdateLayout != null) {
            ViewGroup parent = (ViewGroup) prevUpdateLayout.getParent();
            parent.removeView(prevUpdateLayout);
        }
    }

    public void checkAppUpdate(boolean force) {
        if (force || !BuildVars.DEBUG_VERSION) {
            if (!force && !BuildVars.CHECK_UPDATES) {
                return;
            }
            if (!force && Math.abs(System.currentTimeMillis() - SharedConfig.lastUpdateCheckTime) < MessagesController.getInstance(0).updateCheckDelay * 1000) {
                return;
            }
            TLRPC.TL_help_getAppUpdate req = new TLRPC.TL_help_getAppUpdate();
            try {
                req.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception e) {
            }
            if (req.source == null) {
                req.source = "";
            }
            final int accountNum = this.currentAccount;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.m3591lambda$checkAppUpdate$74$orgtelegramuiLaunchActivity(accountNum, tLObject, tL_error);
                }
            });
        }
    }

    /* renamed from: lambda$checkAppUpdate$74$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3591lambda$checkAppUpdate$74$orgtelegramuiLaunchActivity(final int accountNum, TLObject response, TLRPC.TL_error error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (response instanceof TLRPC.TL_help_appUpdate) {
            final TLRPC.TL_help_appUpdate res = (TLRPC.TL_help_appUpdate) response;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.m3590lambda$checkAppUpdate$73$orgtelegramuiLaunchActivity(res, accountNum);
                }
            });
        }
    }

    /* renamed from: lambda$checkAppUpdate$73$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3590lambda$checkAppUpdate$73$orgtelegramuiLaunchActivity(TLRPC.TL_help_appUpdate res, int accountNum) {
        if ((SharedConfig.pendingAppUpdate == null || !SharedConfig.pendingAppUpdate.version.equals(res.version)) && SharedConfig.setNewAppVersionAvailable(res)) {
            if (res.can_not_skip) {
                showUpdateActivity(accountNum, res, false);
            } else {
                this.drawerLayoutAdapter.notifyDataSetChanged();
                try {
                    new UpdateAppAlertDialog(this, res, accountNum).show();
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
            this.visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.m3664lambda$showAlertDialog$75$orgtelegramuiLaunchActivity(dialogInterface);
                }
            });
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    /* renamed from: lambda$showAlertDialog$75$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3664lambda$showAlertDialog$75$orgtelegramuiLaunchActivity(DialogInterface dialog) {
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                try {
                    String shorname = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
                    Toast.makeText(this, getStringForLanguageAlert(shorname.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), 1).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.localeDialog = null;
            } else if (alertDialog == this.proxyErrorDialog) {
                MessagesController.getGlobalMainSettings();
                SharedPreferences.Editor editor = MessagesController.getGlobalMainSettings().edit();
                editor.putBoolean("proxy_enabled", false);
                editor.putBoolean("proxy_enabled_calls", false);
                editor.commit();
                ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                this.proxyErrorDialog = null;
            }
        }
        this.visibleDialog = null;
    }

    public void showBulletin(Function<BulletinFactory, Bulletin> createBulletin) {
        BaseFragment topFragment = null;
        if (!layerFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList = layerFragmentsStack;
            BaseFragment topFragment2 = arrayList.get(arrayList.size() - 1);
            topFragment = topFragment2;
        } else if (!rightFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
            BaseFragment topFragment3 = arrayList2.get(arrayList2.size() - 1);
            topFragment = topFragment3;
        } else if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            BaseFragment topFragment4 = arrayList3.get(arrayList3.size() - 1);
            topFragment = topFragment4;
        }
        if (BulletinFactory.canShowBulletin(topFragment)) {
            createBulletin.apply(BulletinFactory.of(topFragment)).show();
        }
    }

    public void setNavigateToPremiumBot(boolean val) {
        this.navigateToPremiumBot = val;
    }

    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public void didSelectDialogs(final DialogsActivity dialogsFragment, final ArrayList<Long> dids, final CharSequence message, final boolean param) {
        ChatActivity fragment;
        int attachesCount;
        int i;
        int i2;
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList;
        final int account = dialogsFragment != null ? dialogsFragment.getCurrentAccount() : this.currentAccount;
        if (this.exportingChatUri != null) {
            final Uri uri = this.exportingChatUri;
            final ArrayList<Uri> documentsUris = this.documentsUrisArray != null ? new ArrayList<>(this.documentsUrisArray) : null;
            final AlertDialog progressDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(account).prepareImportHistory(dids.get(0).longValue(), this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j) {
                    LaunchActivity.this.m3603lambda$didSelectDialogs$76$orgtelegramuiLaunchActivity(account, dialogsFragment, param, documentsUris, uri, progressDialog, j);
                }
            });
            try {
                progressDialog.showDelayed(300L);
            } catch (Exception e) {
            }
        } else {
            final boolean notify = dialogsFragment == null || dialogsFragment.notify;
            if (dids.size() <= 1) {
                long did = dids.get(0).longValue();
                Bundle args = new Bundle();
                args.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(did)) {
                    args.putInt("enc_id", DialogObject.getEncryptedChatId(did));
                } else if (DialogObject.isUserDialog(did)) {
                    args.putLong("user_id", did);
                } else {
                    args.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -did);
                }
                if (!MessagesController.getInstance(account).checkCanOpenChat(args, dialogsFragment)) {
                    return;
                }
                fragment = new ChatActivity(args);
            } else {
                fragment = null;
            }
            int attachesCount2 = 0;
            ArrayList<TLRPC.User> arrayList2 = this.contactsToSend;
            if (arrayList2 != null) {
                attachesCount2 = 0 + arrayList2.size();
            }
            if (this.videoPath != null) {
                attachesCount2++;
            }
            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList3 = this.photoPathsArray;
            if (arrayList3 != null) {
                attachesCount2 += arrayList3.size();
            }
            ArrayList<String> arrayList4 = this.documentsPathsArray;
            if (arrayList4 != null) {
                attachesCount2 += arrayList4.size();
            }
            ArrayList<Uri> arrayList5 = this.documentsUrisArray;
            if (arrayList5 != null) {
                attachesCount2 += arrayList5.size();
            }
            if (this.videoPath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                attachesCount = attachesCount2 + 1;
            } else {
                attachesCount = attachesCount2;
            }
            for (int i3 = 0; i3 < dids.size(); i3++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, dids.get(i3).longValue(), attachesCount > 1)) {
                    return;
                }
            }
            ArrayList<TLRPC.User> arrayList6 = this.contactsToSend;
            if (arrayList6 != null && arrayList6.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
                PhonebookShareAlert alert = new PhonebookShareAlert(arrayList7.get(arrayList7.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                final ChatActivity chatActivity = fragment;
                alert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
                    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                    public final void didSelectContact(TLRPC.User user, boolean z, int i4) {
                        LaunchActivity.this.m3604lambda$didSelectDialogs$77$orgtelegramuiLaunchActivity(chatActivity, dids, account, message, notify, user, z, i4);
                    }
                });
                ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                arrayList8.get(arrayList8.size() - 1).showDialog(alert);
            } else {
                String captionToSend = null;
                for (int i4 = 0; i4 < dids.size(); i4 = i + 1) {
                    long did2 = dids.get(i4).longValue();
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    boolean photosEditorOpened = false;
                    boolean videoEditorOpened = false;
                    if (fragment != null) {
                        boolean withoutAnimation = dialogsFragment == null || this.videoPath != null || ((arrayList = this.photoPathsArray) != null && arrayList.size() > 0);
                        i2 = 1024;
                        i = i4;
                        this.actionBarLayout.presentFragment(fragment, dialogsFragment != null, withoutAnimation, true, false);
                        String str = this.videoPath;
                        if (str != null) {
                            fragment.openVideoEditor(str, this.sendingText);
                            this.sendingText = null;
                            videoEditorOpened = true;
                        } else {
                            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList9 = this.photoPathsArray;
                            if (arrayList9 != null && arrayList9.size() > 0) {
                                boolean photosEditorOpened2 = fragment.openPhotosEditor(this.photoPathsArray, (message == null || message.length() == 0) ? this.sendingText : message);
                                if (photosEditorOpened2) {
                                    this.sendingText = null;
                                }
                                photosEditorOpened = photosEditorOpened2;
                            }
                        }
                    } else {
                        i = i4;
                        i2 = 1024;
                        if (this.videoPath != null) {
                            String str2 = this.sendingText;
                            if (str2 != null && str2.length() <= 1024) {
                                captionToSend = this.sendingText;
                                this.sendingText = null;
                            }
                            ArrayList<String> arrayList10 = new ArrayList<>();
                            arrayList10.add(this.videoPath);
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList10, arrayList10, null, captionToSend, null, did2, null, null, null, null, notify, 0);
                        }
                    }
                    if (this.photoPathsArray != null && !photosEditorOpened) {
                        String str3 = this.sendingText;
                        if (str3 != null && str3.length() <= i2 && this.photoPathsArray.size() == 1) {
                            this.photoPathsArray.get(0).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, did2, null, null, null, false, false, null, notify, 0);
                    }
                    if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
                        String str4 = this.sendingText;
                        if (str4 != null && str4.length() <= i2) {
                            ArrayList<String> arrayList11 = this.documentsPathsArray;
                            int size = arrayList11 != null ? arrayList11.size() : 0;
                            ArrayList<Uri> arrayList12 = this.documentsUrisArray;
                            if (size + (arrayList12 != null ? arrayList12.size() : 0) == 1) {
                                captionToSend = this.sendingText;
                                this.sendingText = null;
                            }
                        }
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, captionToSend, this.documentsMimeType, did2, null, null, null, null, notify, 0);
                    }
                    String str5 = this.sendingText;
                    if (str5 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str5, did2, true, 0);
                    }
                    ArrayList<TLRPC.User> arrayList13 = this.contactsToSend;
                    if (arrayList13 != null && !arrayList13.isEmpty()) {
                        for (int a = 0; a < this.contactsToSend.size(); a++) {
                            TLRPC.User user = this.contactsToSend.get(a);
                            SendMessagesHelper.getInstance(account).sendMessage(user, did2, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, notify, 0);
                        }
                    }
                    if (!TextUtils.isEmpty(message) && !videoEditorOpened && !photosEditorOpened) {
                        SendMessagesHelper.prepareSendingText(accountInstance, message.toString(), did2, notify, 0);
                    }
                }
            }
            if (dialogsFragment != null && fragment == null) {
                dialogsFragment.finishFragment();
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

    /* renamed from: lambda$didSelectDialogs$76$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3603lambda$didSelectDialogs$76$orgtelegramuiLaunchActivity(int account, DialogsActivity dialogsFragment, boolean param, ArrayList documentsUris, Uri uri, AlertDialog progressDialog, long result) {
        if (result != 0) {
            Bundle args = new Bundle();
            args.putBoolean("scrollToTopOnResume", true);
            if (!AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
            if (DialogObject.isUserDialog(result)) {
                args.putLong("user_id", result);
            } else {
                args.putLong(ChatReactionsEditActivity.KEY_CHAT_ID, -result);
            }
            ChatActivity fragment = new ChatActivity(args);
            fragment.setOpenImport();
            this.actionBarLayout.presentFragment(fragment, dialogsFragment != null || param, dialogsFragment == null, true, false);
        } else {
            this.documentsUrisArray = documentsUris;
            if (documentsUris == null) {
                this.documentsUrisArray = new ArrayList<>();
            }
            this.documentsUrisArray.add(0, uri);
            openDialogsToSend(true);
        }
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: lambda$didSelectDialogs$77$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3604lambda$didSelectDialogs$77$orgtelegramuiLaunchActivity(ChatActivity fragment, ArrayList dids, int account, CharSequence message, boolean notify, TLRPC.User user, boolean notify2, int scheduleDate) {
        if (fragment != null) {
            this.actionBarLayout.presentFragment(fragment, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i = 0; i < dids.size(); i++) {
            long did = ((Long) dids.get(i)).longValue();
            SendMessagesHelper.getInstance(account).sendMessage(user, did, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, notify2, scheduleDate);
            if (!TextUtils.isEmpty(message)) {
                SendMessagesHelper.prepareSendingText(accountInstance, message.toString(), did, notify, 0);
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
        if (this.currentAccount != -1) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
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
    }

    /* renamed from: presentFragment */
    public void m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(BaseFragment fragment) {
        this.actionBarLayout.presentFragment(fragment);
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation) {
        return this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VoIPService service;
        boolean z = false;
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onActivityResult");
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (requestCode == 105) {
            if (Build.VERSION.SDK_INT >= 23) {
                boolean canDrawOverlays = Settings.canDrawOverlays(this);
                ApplicationLoader.canDrawOverlays = canDrawOverlays;
                if (canDrawOverlays) {
                    if (GroupCallActivity.groupCallInstance != null) {
                        GroupCallActivity.groupCallInstance.dismissInternal();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda27
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.m3613lambda$onActivityResult$78$orgtelegramuiLaunchActivity();
                        }
                    }, 200L);
                    return;
                }
                return;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 520) {
            if (resultCode == -1 && (service = VoIPService.getSharedInstance()) != null) {
                VideoCapturerDevice.mediaProjectionPermissionResultData = data;
                service.createCaptureDevice(true);
            }
        } else if (requestCode == PLAY_SERVICES_REQUEST_CHECK_SETTINGS) {
            LocationController locationController = LocationController.getInstance(this.currentAccount);
            if (resultCode == -1) {
                z = true;
            }
            locationController.startFusedLocationRequest(z);
        } else {
            ThemeEditorView editorView = ThemeEditorView.getInstance();
            if (editorView != null) {
                editorView.onActivityResult(requestCode, resultCode, data);
            }
            if (this.actionBarLayout.fragmentsStack.size() != 0) {
                BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                fragment.onActivityResultFragment(requestCode, resultCode, data);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                    BaseFragment fragment2 = this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                    fragment2.onActivityResultFragment(requestCode, resultCode, data);
                }
                if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                    BaseFragment fragment3 = this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                    fragment3.onActivityResultFragment(requestCode, resultCode, data);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onActivityResultReceived, Integer.valueOf(requestCode), Integer.valueOf(resultCode), data);
        }
    }

    /* renamed from: lambda$onActivityResult$78$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3613lambda$onActivityResult$78$orgtelegramuiLaunchActivity() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!checkPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            fragment.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                BaseFragment fragment2 = this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                fragment2.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                BaseFragment fragment3 = this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                fragment3.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
            }
        }
        VoIPFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onRequestPermissionResultReceived, Integer.valueOf(requestCode), permissions, grantResults);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 4096);
        ApplicationLoader.mainInterfacePaused = true;
        final int account = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$79(account);
            }
        });
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
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

    public static /* synthetic */ void lambda$onPause$79(int account) {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
        if (VoIPService.getSharedInstance() == null) {
            MessagesController.getInstance(account).ignoreSetOnline = false;
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = false;
        GroupCallPip.updateVisibility(this);
        if (GroupCallActivity.groupCallInstance != null) {
            GroupCallActivity.groupCallInstance.onResume();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = true;
        GroupCallPip.updateVisibility(this);
        if (GroupCallActivity.groupCallInstance != null) {
            GroupCallActivity.groupCallInstance.onPause();
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
        if (GroupCallActivity.groupCallInstance != null) {
            GroupCallActivity.groupCallInstance.dismissInternal();
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
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.destroy();
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
                View view = getWindow().getDecorView().getRootView();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        super.onDestroy();
        onFinish();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        for (Runnable callback : this.onUserLeaveHintListeners) {
            callback.run();
        }
        this.actionBarLayout.onUserLeaveHint();
    }

    @Override // android.app.Activity
    protected void onResume() {
        MessageObject messageObject;
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
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda63.INSTANCE);
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView == null || passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.dismissDialogs();
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null && MediaController.getInstance().isMessagePaused() && (messageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
            MediaController.getInstance().seekToProgress(messageObject, messageObject.audioProgress);
        }
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            showTosActivity(UserConfig.selectedAccount, UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService);
        } else if (SharedConfig.pendingAppUpdate != null && SharedConfig.pendingAppUpdate.can_not_skip) {
            showUpdateActivity(UserConfig.selectedAccount, SharedConfig.pendingAppUpdate, true);
        }
        checkAppUpdate(false);
        if (Build.VERSION.SDK_INT >= 23) {
            ApplicationLoader.canDrawOverlays = Settings.canDrawOverlays(this);
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onResume();
        }
    }

    public static /* synthetic */ void lambda$onResume$80() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        AndroidUtilities.checkDisplaySize(this, newConfig);
        super.onConfigurationChanged(newConfig);
        checkLayout();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.onConfigurationChanged();
        }
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.onConfigurationChanged(newConfig);
        }
        PhotoViewer photoViewer = PhotoViewer.getPipInstance();
        if (photoViewer != null) {
            photoViewer.onConfigurationChanged(newConfig);
        }
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onConfigurationChanged();
        }
        if (Theme.selectedAutoNightType == 3) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        checkLayout();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, final int account, Object... args) {
        FrameLayout container;
        BaseFragment fragment;
        char c;
        String str;
        int i;
        String str2;
        int i2;
        BaseFragment baseFragment;
        char c2;
        View child;
        if (id == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        boolean z = false;
        if (id == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != this) {
                onFinish();
                finish();
            }
        } else if (id == NotificationCenter.didUpdateConnectionState) {
            int state = ConnectionsManager.getInstance(account).getConnectionState();
            if (this.currentConnectionState != state) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("switch to state " + state);
                }
                this.currentConnectionState = state;
                updateCurrentConnectionState(account);
            }
        } else if (id == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (id == NotificationCenter.needShowAlert) {
            Integer reason = (Integer) args[0];
            if (reason.intValue() != 6) {
                if (reason.intValue() == 3 && this.proxyErrorDialog != null) {
                    return;
                }
                if (reason.intValue() == 4) {
                    showTosActivity(account, (TLRPC.TL_help_termsOfService) args[1]);
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                if (reason.intValue() != 2 && reason.intValue() != 3) {
                    builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i3) {
                            LaunchActivity.lambda$didReceivedNotification$81(account, dialogInterface, i3);
                        }
                    });
                }
                if (reason.intValue() == 5) {
                    builder.setMessage(LocaleController.getString("NobodyLikesSpam3", R.string.NobodyLikesSpam3));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                } else if (reason.intValue() == 0) {
                    builder.setMessage(LocaleController.getString("NobodyLikesSpam1", R.string.NobodyLikesSpam1));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                } else if (reason.intValue() == 1) {
                    builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                } else if (reason.intValue() == 2) {
                    builder.setMessage((String) args[1]);
                    String type = (String) args[2];
                    if (type.startsWith("AUTH_KEY_DROP_")) {
                        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                LaunchActivity.this.m3595lambda$didReceivedNotification$82$orgtelegramuiLaunchActivity(dialogInterface, i3);
                            }
                        });
                    } else {
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    }
                } else if (reason.intValue() == 3) {
                    builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
                    builder.setMessage(LocaleController.getString("UseProxyTelegramError", R.string.UseProxyTelegramError));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    this.proxyErrorDialog = showAlertDialog(builder);
                    return;
                }
                if (!mainFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                    arrayList.get(arrayList.size() - 1).showDialog(builder.create());
                }
            }
        } else if (id == NotificationCenter.wasUnableToFindCurrentLocation) {
            final HashMap<String, MessageObject> waitingForLocation = (HashMap) args[0];
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    LaunchActivity.this.m3596lambda$didReceivedNotification$84$orgtelegramuiLaunchActivity(waitingForLocation, account, dialogInterface, i3);
                }
            });
            builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", R.string.ShareYouLocationUnable));
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                arrayList2.get(arrayList2.size() - 1).showDialog(builder2.create());
            }
        } else if (id == NotificationCenter.didSetNewWallpapper) {
            RecyclerListView recyclerListView = this.sideMenu;
            if (recyclerListView != null && (child = recyclerListView.getChildAt(0)) != null) {
                child.invalidate();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
            if (sizeNotifierFrameLayout != null) {
                sizeNotifierFrameLayout.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            }
        } else if (id == NotificationCenter.didSetPasscode) {
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
        } else if (id == NotificationCenter.reloadInterface) {
            if (mainFragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                if (arrayList3.get(arrayList3.size() - 1) instanceof ProfileActivity) {
                    z = true;
                }
            }
            boolean last = z;
            if (last) {
                ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                ProfileActivity profileActivity = (ProfileActivity) arrayList4.get(arrayList4.size() - 1);
                if (!profileActivity.isSettings()) {
                    last = false;
                }
            }
            rebuildAllFragments(last);
        } else if (id == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (id == NotificationCenter.openArticle) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArticleViewer articleViewer = ArticleViewer.getInstance();
            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
            articleViewer.setParentActivity(this, arrayList5.get(arrayList5.size() - 1));
            ArticleViewer.getInstance().open((TLRPC.TL_webPage) args[0], (String) args[1]);
        } else if (id == NotificationCenter.hasNewContactsToImport) {
            ActionBarLayout actionBarLayout = this.actionBarLayout;
            if (actionBarLayout == null || actionBarLayout.fragmentsStack.isEmpty()) {
                return;
            }
            ((Integer) args[0]).intValue();
            final HashMap<String, ContactsController.Contact> contactHashMap = (HashMap) args[1];
            final boolean first = ((Boolean) args[2]).booleanValue();
            final boolean schedule = ((Boolean) args[3]).booleanValue();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
            builder3.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
            builder3.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
            builder3.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    ContactsController.getInstance(account).syncPhoneBookByAlert(contactHashMap, first, schedule, false);
                }
            });
            builder3.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    ContactsController.getInstance(account).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                }
            });
            builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    ContactsController.getInstance(account).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                }
            });
            AlertDialog dialog = builder3.create();
            this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1).showDialog(dialog);
            dialog.setCanceledOnTouchOutside(false);
        } else if (id == NotificationCenter.didSetNewTheme) {
            if (!((Boolean) args[0]).booleanValue()) {
                RecyclerListView recyclerListView2 = this.sideMenu;
                if (recyclerListView2 != null) {
                    recyclerListView2.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.setListSelectorColor(Theme.getColor(Theme.key_listSelector));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor(Theme.key_actionBarDefault) | (-16777216)));
                    } catch (Exception e3) {
                    }
                }
            }
            this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            boolean checkNavigationBarColor = true;
            if (args.length > 1) {
                checkNavigationBarColor = ((Boolean) args[1]).booleanValue();
            }
            boolean z2 = args.length > 2 && ((Boolean) args[2]).booleanValue();
            if (checkNavigationBarColor && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                z = true;
            }
            checkSystemBarColors(z2, true, z);
        } else if (id == NotificationCenter.needSetDayNightTheme) {
            boolean instant = false;
            if (Build.VERSION.SDK_INT >= 21 && args[2] != null) {
                if (this.themeSwitchImageView.getVisibility() == 0) {
                    return;
                }
                try {
                    int[] pos = (int[]) args[2];
                    final boolean toDark = ((Boolean) args[4]).booleanValue();
                    final RLottieImageView darkThemeView = (RLottieImageView) args[5];
                    int w = this.drawerLayoutContainer.getMeasuredWidth();
                    int h = this.drawerLayoutContainer.getMeasuredHeight();
                    if (!toDark) {
                        darkThemeView.setVisibility(4);
                    }
                    this.rippleAbove = null;
                    if (args.length > 6) {
                        this.rippleAbove = (View) args[6];
                    }
                    this.isNavigationBarColorFrozen = true;
                    invalidateCachedViews(this.drawerLayoutContainer);
                    View view = this.rippleAbove;
                    if (view != null && view.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(0);
                    }
                    Bitmap bitmap = AndroidUtilities.snapshotView(this.drawerLayoutContainer);
                    View view2 = this.rippleAbove;
                    if (view2 != null && view2.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(255);
                    }
                    this.frameLayout.removeView(this.themeSwitchImageView);
                    if (toDark) {
                        this.frameLayout.addView(this.themeSwitchImageView, 0, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setVisibility(8);
                    } else {
                        this.frameLayout.addView(this.themeSwitchImageView, 1, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setTranslationX(pos[0] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setTranslationY(pos[1] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setVisibility(0);
                        this.themeSwitchSunView.invalidate();
                    }
                    this.themeSwitchImageView.setImageBitmap(bitmap);
                    this.themeSwitchImageView.setVisibility(0);
                    this.themeSwitchSunDrawable = darkThemeView.getAnimatedDrawable();
                    float finalRadius2 = (float) Math.max(Math.sqrt(((w - pos[0]) * (w - pos[0])) + (pos[1] * pos[1])), Math.sqrt((pos[0] * pos[0]) + (pos[1] * pos[1])));
                    float finalRadius = Math.max((float) Math.max(Math.sqrt(((w - pos[0]) * (w - pos[0])) + ((h - pos[1]) * (h - pos[1]))), Math.sqrt((pos[0] * pos[0]) + ((h - pos[1]) * (h - pos[1])))), finalRadius2);
                    View view3 = toDark ? this.drawerLayoutContainer : this.themeSwitchImageView;
                    int i3 = pos[0];
                    int i4 = pos[1];
                    float f = toDark ? 0.0f : finalRadius;
                    if (!toDark) {
                        finalRadius = 0.0f;
                    }
                    Animator anim = ViewAnimationUtils.createCircularReveal(view3, i3, i4, f, finalRadius);
                    anim.setDuration(400L);
                    anim.setInterpolator(Easings.easeInOutQuad);
                    anim.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.18
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animation) {
                            LaunchActivity.this.rippleAbove = null;
                            LaunchActivity.this.drawerLayoutContainer.invalidate();
                            LaunchActivity.this.themeSwitchImageView.invalidate();
                            LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
                            LaunchActivity.this.themeSwitchImageView.setVisibility(8);
                            LaunchActivity.this.themeSwitchSunView.setVisibility(8);
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeAccentListUpdated, new Object[0]);
                            if (!toDark) {
                                darkThemeView.setVisibility(0);
                            }
                            DrawerProfileCell.switchingTheme = false;
                        }
                    });
                    if (this.rippleAbove != null) {
                        ValueAnimator invalidateAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                        invalidateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                LaunchActivity.this.m3597lambda$didReceivedNotification$88$orgtelegramuiLaunchActivity(valueAnimator);
                            }
                        });
                        invalidateAnimator.setDuration(anim.getDuration());
                        invalidateAnimator.start();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda23
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.m3598lambda$didReceivedNotification$89$orgtelegramuiLaunchActivity();
                        }
                    }, toDark ? (h - pos[1]) / AndroidUtilities.dp(2.25f) : 50L);
                    anim.start();
                    instant = true;
                } catch (Throwable e4) {
                    FileLog.e(e4);
                    try {
                        this.themeSwitchImageView.setImageDrawable(null);
                        this.frameLayout.removeView(this.themeSwitchImageView);
                        DrawerProfileCell.switchingTheme = false;
                    } catch (Exception e22) {
                        FileLog.e(e22);
                    }
                }
                c2 = 0;
            } else {
                c2 = 0;
                DrawerProfileCell.switchingTheme = false;
            }
            Theme.ThemeInfo theme = (Theme.ThemeInfo) args[c2];
            boolean nightTheme = ((Boolean) args[1]).booleanValue();
            int accentId = ((Integer) args[3]).intValue();
            this.actionBarLayout.animateThemedValues(theme, accentId, nightTheme, instant);
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.animateThemedValues(theme, accentId, nightTheme, instant);
                this.rightActionBarLayout.animateThemedValues(theme, accentId, nightTheme, instant);
            }
        } else if (id == NotificationCenter.notificationsCountUpdated) {
            RecyclerListView recyclerListView3 = this.sideMenu;
            if (recyclerListView3 != null) {
                Integer accountNum = (Integer) args[0];
                int count = recyclerListView3.getChildCount();
                for (int a = 0; a < count; a++) {
                    View child2 = this.sideMenu.getChildAt(a);
                    if ((child2 instanceof DrawerUserCell) && ((DrawerUserCell) child2).getAccountNumber() == accountNum.intValue()) {
                        child2.invalidate();
                        return;
                    }
                }
            }
        } else if (id == NotificationCenter.needShowPlayServicesAlert) {
            try {
                Status status = (Status) args[0];
                status.startResolutionForResult(this, PLAY_SERVICES_REQUEST_CHECK_SETTINGS);
            } catch (Throwable th) {
            }
        } else if (id == NotificationCenter.fileLoaded) {
            String path = (String) args[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(path)) {
                updateAppUpdateViews(true);
            }
            String name = this.loadingThemeFileName;
            if (name != null) {
                if (name.equals(path)) {
                    this.loadingThemeFileName = null;
                    File locFile = new File(ApplicationLoader.getFilesDirFixed(), "remote" + this.loadingTheme.id + ".attheme");
                    final Theme.ThemeInfo themeInfo = Theme.fillThemeValues(locFile, this.loadingTheme.title, this.loadingTheme);
                    if (themeInfo != null) {
                        if (themeInfo.pathToWallpaper != null) {
                            File file = new File(themeInfo.pathToWallpaper);
                            if (!file.exists()) {
                                TLRPC.TL_account_getWallPaper req = new TLRPC.TL_account_getWallPaper();
                                TLRPC.TL_inputWallPaperSlug inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                                inputWallPaperSlug.slug = themeInfo.slug;
                                req.wallpaper = inputWallPaperSlug;
                                ConnectionsManager.getInstance(themeInfo.account).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        LaunchActivity.this.m3600lambda$didReceivedNotification$91$orgtelegramuiLaunchActivity(themeInfo, tLObject, tL_error);
                                    }
                                });
                                return;
                            }
                        }
                        Theme.ThemeInfo finalThemeInfo = Theme.applyThemeFile(locFile, this.loadingTheme.title, this.loadingTheme, true);
                        if (finalThemeInfo != null) {
                            m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ThemePreviewActivity(finalThemeInfo, true, 0, false, false));
                        }
                    }
                    onThemeLoadFinish();
                    return;
                }
                return;
            }
            String str3 = this.loadingThemeWallpaperName;
            if (str3 != null && str3.equals(path)) {
                this.loadingThemeWallpaperName = null;
                final File file2 = (File) args[1];
                if (this.loadingThemeAccent) {
                    openThemeAccentPreview(this.loadingTheme, this.loadingThemeWallpaper, this.loadingThemeInfo);
                    onThemeLoadFinish();
                    return;
                }
                final Theme.ThemeInfo info = this.loadingThemeInfo;
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.m3602lambda$didReceivedNotification$93$orgtelegramuiLaunchActivity(info, file2);
                    }
                });
            }
        } else if (id == NotificationCenter.fileLoadFailed) {
            String path2 = (String) args[0];
            if (path2.equals(this.loadingThemeFileName) || path2.equals(this.loadingThemeWallpaperName)) {
                onThemeLoadFinish();
            }
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(path2)) {
                updateAppUpdateViews(true);
            }
        } else if (id == NotificationCenter.screenStateChanged) {
            if (ApplicationLoader.mainInterfacePaused) {
                return;
            }
            if (ApplicationLoader.isScreenOn) {
                onPasscodeResume();
            } else {
                onPasscodePause();
            }
        } else if (id == NotificationCenter.needCheckSystemBarColors) {
            boolean useCurrentFragment = args.length > 0 && ((Boolean) args[0]).booleanValue();
            checkSystemBarColors(useCurrentFragment);
        } else if (id == NotificationCenter.historyImportProgressChanged) {
            if (args.length > 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList6 = mainFragmentsStack;
                AlertsCreator.processError(this.currentAccount, (TLRPC.TL_error) args[2], arrayList6.get(arrayList6.size() - 1), (TLObject) args[1], new Object[0]);
            }
        } else if (id == NotificationCenter.stickersImportComplete) {
            MediaDataController mediaDataController = MediaDataController.getInstance(account);
            TLObject tLObject = (TLObject) args[0];
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
                baseFragment = arrayList7.get(arrayList7.size() - 1);
            } else {
                baseFragment = null;
            }
            mediaDataController.toggleStickerSet(this, tLObject, 2, baseFragment, false, true);
        } else if (id == NotificationCenter.newSuggestionsAvailable) {
            this.sideMenu.invalidateViews();
        } else if (id == NotificationCenter.showBulletin) {
            if (!mainFragmentsStack.isEmpty()) {
                int type2 = ((Integer) args[0]).intValue();
                if (GroupCallActivity.groupCallUiVisible && GroupCallActivity.groupCallInstance != null) {
                    container = GroupCallActivity.groupCallInstance.getContainer();
                } else {
                    container = null;
                }
                if (container != null) {
                    c = 1;
                    fragment = null;
                } else {
                    ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                    c = 1;
                    fragment = arrayList8.get(arrayList8.size() - 1);
                }
                switch (type2) {
                    case 0:
                        TLRPC.Document sticker = (TLRPC.Document) args[1];
                        int bulletinType = ((Integer) args[2]).intValue();
                        StickerSetBulletinLayout layout = new StickerSetBulletinLayout(this, null, bulletinType, sticker, null);
                        int duration = 1500;
                        if (bulletinType == 6 || bulletinType == 7) {
                            duration = 3500;
                        }
                        if (fragment != null) {
                            Bulletin.make(fragment, layout, duration).show();
                            return;
                        } else {
                            Bulletin.make(container, layout, duration).show();
                            return;
                        }
                    case 1:
                        if (fragment != null) {
                            BulletinFactory.of(fragment).createErrorBulletin((String) args[1]).show();
                            return;
                        } else {
                            BulletinFactory.of(container, null).createErrorBulletin((String) args[1]).show();
                            return;
                        }
                    case 2:
                        long peerId = ((Long) args[1]).longValue();
                        if (peerId > 0) {
                            i = R.string.YourBioChanged;
                            str = "YourBioChanged";
                        } else {
                            i = R.string.ChannelDescriptionChanged;
                            str = "CannelDescriptionChanged";
                        }
                        String text = LocaleController.getString(str, i);
                        (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(fragment)).createErrorBulletin(text).show();
                        return;
                    case 3:
                        long peerId2 = ((Long) args[1]).longValue();
                        if (peerId2 > 0) {
                            i2 = R.string.YourNameChanged;
                            str2 = "YourNameChanged";
                        } else {
                            i2 = R.string.ChannelTitleChanged;
                            str2 = "CannelTitleChanged";
                        }
                        String text2 = LocaleController.getString(str2, i2);
                        (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(fragment)).createErrorBulletin(text2).show();
                        return;
                    case 4:
                        if (fragment != null) {
                            BulletinFactory.of(fragment).createErrorBulletinSubtitle((String) args[1], (String) args[2], fragment.getResourceProvider()).show();
                            return;
                        } else {
                            BulletinFactory.of(container, null).createErrorBulletinSubtitle((String) args[1], (String) args[2], null).show();
                            return;
                        }
                    case 5:
                        LauncherIconController.LauncherIcon icon = (LauncherIconController.LauncherIcon) args[c];
                        AppIconBulletinLayout layout2 = new AppIconBulletinLayout(this, icon, null);
                        if (fragment != null) {
                            Bulletin.make(fragment, layout2, 1500).show();
                            return;
                        } else {
                            Bulletin.make(container, layout2, 1500).show();
                            return;
                        }
                    default:
                        return;
                }
            }
        } else if (id == NotificationCenter.groupCallUpdated) {
            checkWasMutedByAdmin(false);
        } else if (id == NotificationCenter.fileLoadProgressChanged) {
            if (this.updateTextView != null && SharedConfig.isAppUpdateAvailable()) {
                String location = (String) args[0];
                String fileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
                if (fileName != null && fileName.equals(location)) {
                    Long loadedSize = (Long) args[1];
                    Long totalSize = (Long) args[2];
                    float loadProgress = ((float) loadedSize.longValue()) / ((float) totalSize.longValue());
                    this.updateLayoutIcon.setProgress(loadProgress, true);
                    this.updateTextView.setText(LocaleController.formatString("AppUpdateDownloading", R.string.AppUpdateDownloading, Integer.valueOf((int) (100.0f * loadProgress))));
                }
            }
        } else if (id == NotificationCenter.appUpdateAvailable) {
            updateAppUpdateViews(mainFragmentsStack.size() == 1);
        } else if (id == NotificationCenter.currentUserShowLimitReachedDialog && !mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
            BaseFragment fragment2 = arrayList9.get(arrayList9.size() - 1);
            if (fragment2.getParentActivity() != null) {
                fragment2.showDialog(new LimitReachedBottomSheet(fragment2, fragment2.getParentActivity(), ((Integer) args[0]).intValue(), this.currentAccount));
            }
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$81(int account, DialogInterface dialogInterface, int i) {
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController messagesController = MessagesController.getInstance(account);
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
        }
    }

    /* renamed from: lambda$didReceivedNotification$82$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3595lambda$didReceivedNotification$82$orgtelegramuiLaunchActivity(DialogInterface dialog, int which) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    /* renamed from: lambda$didReceivedNotification$84$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3596lambda$didReceivedNotification$84$orgtelegramuiLaunchActivity(final HashMap waitingForLocation, final int account, DialogInterface dialogInterface, int i) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        BaseFragment lastFragment = arrayList.get(arrayList.size() - 1);
        if (!AndroidUtilities.isGoogleMapsInstalled(lastFragment)) {
            return;
        }
        LocationActivity fragment = new LocationActivity(0);
        fragment.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i2, boolean z, int i3) {
                LaunchActivity.lambda$didReceivedNotification$83(waitingForLocation, account, messageMedia, i2, z, i3);
            }
        });
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(fragment);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$83(HashMap waitingForLocation, int account, TLRPC.MessageMedia location, int live, boolean notify, int scheduleDate) {
        for (Map.Entry<String, MessageObject> entry : waitingForLocation.entrySet()) {
            MessageObject messageObject = entry.getValue();
            SendMessagesHelper.getInstance(account).sendMessage(location, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, notify, scheduleDate);
        }
    }

    /* renamed from: lambda$didReceivedNotification$88$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3597lambda$didReceivedNotification$88$orgtelegramuiLaunchActivity(ValueAnimator a) {
        this.frameLayout.invalidate();
    }

    /* renamed from: lambda$didReceivedNotification$89$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3598lambda$didReceivedNotification$89$orgtelegramuiLaunchActivity() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    /* renamed from: lambda$didReceivedNotification$91$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3600lambda$didReceivedNotification$91$orgtelegramuiLaunchActivity(final Theme.ThemeInfo themeInfo, final TLObject response, TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3599lambda$didReceivedNotification$90$orgtelegramuiLaunchActivity(response, themeInfo);
            }
        });
    }

    /* renamed from: lambda$didReceivedNotification$90$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3599lambda$didReceivedNotification$90$orgtelegramuiLaunchActivity(TLObject response, Theme.ThemeInfo themeInfo) {
        if (response instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper wallPaper = (TLRPC.TL_wallPaper) response;
            this.loadingThemeInfo = themeInfo;
            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(wallPaper.document);
            this.loadingThemeWallpaper = wallPaper;
            FileLoader.getInstance(themeInfo.account).loadFile(wallPaper.document, wallPaper, 1, 1);
            return;
        }
        onThemeLoadFinish();
    }

    /* renamed from: lambda$didReceivedNotification$93$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3602lambda$didReceivedNotification$93$orgtelegramuiLaunchActivity(Theme.ThemeInfo info, File file) {
        info.createBackground(file, info.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3601lambda$didReceivedNotification$92$orgtelegramuiLaunchActivity();
            }
        });
    }

    /* renamed from: lambda$didReceivedNotification$92$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3601lambda$didReceivedNotification$92$orgtelegramuiLaunchActivity() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File locFile = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        Theme.ThemeInfo finalThemeInfo = Theme.applyThemeFile(locFile, this.loadingTheme.title, this.loadingTheme, true);
        if (finalThemeInfo != null) {
            m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ThemePreviewActivity(finalThemeInfo, true, 0, false, false));
        }
        onThemeLoadFinish();
    }

    private void invalidateCachedViews(View parent) {
        int layerType = parent.getLayerType();
        if (layerType != 0) {
            parent.invalidate();
        }
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                invalidateCachedViews(viewGroup.getChildAt(i));
            }
        }
    }

    private void checkWasMutedByAdmin(boolean checkOnly) {
        long did;
        VoIPService voIPService = VoIPService.getSharedInstance();
        boolean z = false;
        if (voIPService != null && voIPService.groupCall != null) {
            boolean wasMuted = this.wasMutedByAdminRaisedHand;
            ChatObject.Call call = voIPService.groupCall;
            TLRPC.InputPeer peer = voIPService.getGroupCallPeer();
            if (peer != null) {
                if (peer.user_id != 0) {
                    did = peer.user_id;
                } else {
                    long did2 = peer.chat_id;
                    if (did2 != 0) {
                        did = -peer.chat_id;
                    } else {
                        long did3 = peer.channel_id;
                        did = -did3;
                    }
                }
            } else {
                did = UserConfig.getInstance(this.currentAccount).clientUserId;
            }
            TLRPC.TL_groupCallParticipant participant = call.participants.get(did);
            boolean mutedByAdmin = participant != null && !participant.can_self_unmute && participant.muted;
            if (mutedByAdmin && participant.raise_hand_rating != 0) {
                z = true;
            }
            this.wasMutedByAdminRaisedHand = z;
            if (!checkOnly && wasMuted && !z && !mutedByAdmin && GroupCallActivity.groupCallInstance == null) {
                showVoiceChatTooltip(38);
                return;
            }
            return;
        }
        this.wasMutedByAdminRaisedHand = false;
    }

    private void showVoiceChatTooltip(int action) {
        VoIPService voIPService = VoIPService.getSharedInstance();
        if (voIPService != null && !mainFragmentsStack.isEmpty() && voIPService.groupCall != null && !mainFragmentsStack.isEmpty()) {
            TLRPC.Chat chat = voIPService.getChat();
            BaseFragment fragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            if (fragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) fragment;
                if (chatActivity.getDialogId() == (-chat.id)) {
                    chat = null;
                }
                chatActivity.getUndoView().showWithAction(0L, action, chat);
            } else if (fragment instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) fragment;
                dialogsActivity.getUndoView().showWithAction(0L, action, chat);
            } else if (fragment instanceof ProfileActivity) {
                ProfileActivity profileActivity = (ProfileActivity) fragment;
                profileActivity.getUndoView().showWithAction(0L, action, chat);
            }
            if (action == 38 && VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().playAllowTalkSound();
            }
        }
    }

    private String getStringForLanguageAlert(HashMap<String, String> map, String key, int intKey) {
        String value = map.get(key);
        if (value == null) {
            return LocaleController.getString(key, intKey);
        }
        return value;
    }

    private void openThemeAccentPreview(TLRPC.TL_theme t, TLRPC.TL_wallPaper wallPaper, Theme.ThemeInfo info) {
        int lastId = info.lastAccentId;
        Theme.ThemeAccent accent = info.createNewAccent(t, this.currentAccount);
        info.prevAccentId = info.currentAccentId;
        info.setCurrentAccentId(accent.id);
        accent.pattern = wallPaper;
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ThemePreviewActivity(info, lastId != info.lastAccentId, 0, false, false));
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
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3593lambda$checkFreeDiscSpace$95$orgtelegramuiLaunchActivity();
            }
        }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
    }

    /* renamed from: lambda$checkFreeDiscSpace$95$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3593lambda$checkFreeDiscSpace$95$orgtelegramuiLaunchActivity() {
        File path;
        long freeSpace;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
            if (Math.abs(preferences.getLong("last_space_check", 0L) - System.currentTimeMillis()) < 259200000 || (path = FileLoader.getDirectory(4)) == null) {
                return;
            }
            StatFs statFs = new StatFs(path.getAbsolutePath());
            if (Build.VERSION.SDK_INT < 18) {
                freeSpace = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
            } else {
                long freeSpace2 = statFs.getAvailableBlocksLong();
                freeSpace = freeSpace2 * statFs.getBlockSizeLong();
            }
            if (freeSpace < 104857600) {
                preferences.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda21
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.m3592lambda$checkFreeDiscSpace$94$orgtelegramuiLaunchActivity();
                    }
                });
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: lambda$checkFreeDiscSpace$94$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3592lambda$checkFreeDiscSpace$94$orgtelegramuiLaunchActivity() {
        try {
            AlertsCreator.createFreeSpaceDialog(this).show();
        } catch (Throwable th) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f A[Catch: Exception -> 0x012e, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x012e, blocks: (B:5:0x000b, B:7:0x0010, B:12:0x001e, B:16:0x0058, B:20:0x0060, B:24:0x0068, B:28:0x006f), top: B:52:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showLanguageAlertInternal(LocaleController.LocaleInfo systemInfo, LocaleController.LocaleInfo englishInfo, String systemLang) {
        Exception e;
        boolean z;
        boolean firstSystem;
        int i;
        int a;
        try {
            this.loadingLocaleDialog = false;
        } catch (Exception e2) {
            e = e2;
        }
        try {
            z = true;
        } catch (Exception e3) {
            e = e3;
            FileLog.e(e);
            return;
        }
        try {
            if (!systemInfo.builtIn && !LocaleController.getInstance().isCurrentLocalLocale()) {
                firstSystem = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                final LanguageCell[] cells = new LanguageCell[2];
                final LocaleController.LocaleInfo[] selectedLanguage = new LocaleController.LocaleInfo[1];
                LocaleController.LocaleInfo[] locales = new LocaleController.LocaleInfo[2];
                String englishName = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
                locales[0] = !firstSystem ? systemInfo : englishInfo;
                locales[1] = !firstSystem ? englishInfo : systemInfo;
                selectedLanguage[0] = !firstSystem ? systemInfo : englishInfo;
                a = 0;
                for (i = 2; a < i; i = 2) {
                    cells[a] = new LanguageCell(this);
                    try {
                        cells[a].setLanguage(locales[a], locales[a] == englishInfo ? englishName : null, z);
                        cells[a].setTag(Integer.valueOf(a));
                        cells[a].setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                        cells[a].setLanguageSelected(a == 0, false);
                        linearLayout.addView(cells[a], LayoutHelper.createLinear(-1, 50));
                        cells[a].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                LaunchActivity.lambda$showLanguageAlertInternal$96(selectedLanguage, cells, view);
                            }
                        });
                        a++;
                        z = true;
                    } catch (Exception e4) {
                        e = e4;
                        FileLog.e(e);
                        return;
                    }
                }
                LanguageCell cell = new LanguageCell(this);
                cell.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther));
                cell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                cell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda11
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.this.m3669x1067c221(view);
                    }
                });
                linearLayout.addView(cell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        LaunchActivity.this.m3670x98980200(selectedLanguage, dialogInterface, i2);
                    }
                });
                this.localeDialog = showAlertDialog(builder);
                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                preferences.edit().putString("language_showed2", systemLang).commit();
                return;
            }
            preferences.edit().putString("language_showed2", systemLang).commit();
            return;
        } catch (Exception e5) {
            e = e5;
            FileLog.e(e);
            return;
        }
        firstSystem = true;
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
        builder2.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        final LanguageCell[] cells2 = new LanguageCell[2];
        final LocaleController.LocaleInfo[] selectedLanguage2 = new LocaleController.LocaleInfo[1];
        LocaleController.LocaleInfo[] locales2 = new LocaleController.LocaleInfo[2];
        String englishName2 = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
        locales2[0] = !firstSystem ? systemInfo : englishInfo;
        locales2[1] = !firstSystem ? englishInfo : systemInfo;
        selectedLanguage2[0] = !firstSystem ? systemInfo : englishInfo;
        a = 0;
        while (a < i) {
        }
        LanguageCell cell2 = new LanguageCell(this);
        cell2.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther));
        cell2.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
        cell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.m3669x1067c221(view);
            }
        });
        linearLayout2.addView(cell2, LayoutHelper.createLinear(-1, 50));
        builder2.setView(linearLayout2);
        builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                LaunchActivity.this.m3670x98980200(selectedLanguage2, dialogInterface, i2);
            }
        });
        this.localeDialog = showAlertDialog(builder2);
        SharedPreferences preferences2 = MessagesController.getGlobalMainSettings();
    }

    public static /* synthetic */ void lambda$showLanguageAlertInternal$96(LocaleController.LocaleInfo[] selectedLanguage, LanguageCell[] cells, View v) {
        Integer tag = (Integer) v.getTag();
        selectedLanguage[0] = ((LanguageCell) v).getCurrentLocale();
        int a1 = 0;
        while (a1 < cells.length) {
            cells[a1].setLanguageSelected(a1 == tag.intValue(), true);
            a1++;
        }
    }

    /* renamed from: lambda$showLanguageAlertInternal$97$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3669x1067c221(View v) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    /* renamed from: lambda$showLanguageAlertInternal$98$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3670x98980200(LocaleController.LocaleInfo[] selectedLanguage, DialogInterface dialog, int which) {
        LocaleController.getInstance().applyLanguage(selectedLanguage[0], true, false, this.currentAccount);
        rebuildAllFragments(true);
    }

    public void drawRippleAbove(Canvas canvas, View parent) {
        View view;
        if (parent == null || (view = this.rippleAbove) == null || view.getBackground() == null) {
            return;
        }
        if (this.tempLocation == null) {
            this.tempLocation = new int[2];
        }
        this.rippleAbove.getLocationInWindow(this.tempLocation);
        int[] iArr = this.tempLocation;
        int x = iArr[0];
        int y = iArr[1];
        parent.getLocationInWindow(iArr);
        int[] iArr2 = this.tempLocation;
        int y2 = y - iArr2[1];
        canvas.save();
        canvas.translate(x - iArr2[0], y2);
        this.rippleAbove.getBackground().draw(canvas);
        canvas.restore();
    }

    private void showLanguageAlert(boolean force) {
        String alias;
        char c;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            if (!this.loadingLocaleDialog && !ApplicationLoader.mainInterfacePaused) {
                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                String showedLang = preferences.getString("language_showed2", "");
                final String systemLang = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                if (!force && showedLang.equals(systemLang)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("alert already showed for " + showedLang);
                        return;
                    }
                    return;
                }
                final LocaleController.LocaleInfo[] infos = new LocaleController.LocaleInfo[2];
                String arg = systemLang.contains("-") ? systemLang.split("-")[0] : systemLang;
                if ("in".equals(arg)) {
                    alias = "id";
                } else if ("iw".equals(arg)) {
                    alias = "he";
                } else if ("jw".equals(arg)) {
                    alias = "jv";
                } else {
                    alias = null;
                }
                for (int a = 0; a < LocaleController.getInstance().languages.size(); a++) {
                    LocaleController.LocaleInfo info = LocaleController.getInstance().languages.get(a);
                    if (info.shortName.equals("en")) {
                        infos[0] = info;
                    }
                    if (!info.shortName.replace("_", "-").equals(systemLang) && !info.shortName.equals(arg) && !info.shortName.equals(alias)) {
                        c = 1;
                        if (infos[0] == null && infos[c] != null) {
                            break;
                        }
                    }
                    c = 1;
                    infos[1] = info;
                    if (infos[0] == null) {
                    }
                }
                if (infos[0] != null && infos[1] != null && infos[0] != infos[1]) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show lang alert for " + infos[0].getKey() + " and " + infos[1].getKey());
                    }
                    this.systemLocaleStrings = null;
                    this.englishLocaleStrings = null;
                    this.loadingLocaleDialog = true;
                    TLRPC.TL_langpack_getStrings req = new TLRPC.TL_langpack_getStrings();
                    req.lang_code = infos[1].getLangCode();
                    req.keys.add("English");
                    req.keys.add("ChooseYourLanguage");
                    req.keys.add("ChooseYourLanguageOther");
                    req.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.m3665lambda$showLanguageAlert$100$orgtelegramuiLaunchActivity(infos, systemLang, tLObject, tL_error);
                        }
                    }, 8);
                    TLRPC.TL_langpack_getStrings req2 = new TLRPC.TL_langpack_getStrings();
                    req2.lang_code = infos[0].getLangCode();
                    req2.keys.add("English");
                    req2.keys.add("ChooseYourLanguage");
                    req2.keys.add("ChooseYourLanguageOther");
                    req2.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.m3667lambda$showLanguageAlert$102$orgtelegramuiLaunchActivity(infos, systemLang, tLObject, tL_error);
                        }
                    }, 8);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: lambda$showLanguageAlert$100$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3665lambda$showLanguageAlert$100$orgtelegramuiLaunchActivity(final LocaleController.LocaleInfo[] infos, final String systemLang, TLObject response, TLRPC.TL_error error) {
        final HashMap<String, String> keys = new HashMap<>();
        if (response != null) {
            TLRPC.Vector vector = (TLRPC.Vector) response;
            for (int a = 0; a < vector.objects.size(); a++) {
                TLRPC.LangPackString string = (TLRPC.LangPackString) vector.objects.get(a);
                keys.put(string.key, string.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3668lambda$showLanguageAlert$99$orgtelegramuiLaunchActivity(keys, infos, systemLang);
            }
        });
    }

    /* renamed from: lambda$showLanguageAlert$99$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3668lambda$showLanguageAlert$99$orgtelegramuiLaunchActivity(HashMap keys, LocaleController.LocaleInfo[] infos, String systemLang) {
        this.systemLocaleStrings = keys;
        if (this.englishLocaleStrings != null && keys != null) {
            showLanguageAlertInternal(infos[1], infos[0], systemLang);
        }
    }

    /* renamed from: lambda$showLanguageAlert$102$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3667lambda$showLanguageAlert$102$orgtelegramuiLaunchActivity(final LocaleController.LocaleInfo[] infos, final String systemLang, TLObject response, TLRPC.TL_error error) {
        final HashMap<String, String> keys = new HashMap<>();
        if (response != null) {
            TLRPC.Vector vector = (TLRPC.Vector) response;
            for (int a = 0; a < vector.objects.size(); a++) {
                TLRPC.LangPackString string = (TLRPC.LangPackString) vector.objects.get(a);
                keys.put(string.key, string.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.m3666lambda$showLanguageAlert$101$orgtelegramuiLaunchActivity(keys, infos, systemLang);
            }
        });
    }

    /* renamed from: lambda$showLanguageAlert$101$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3666lambda$showLanguageAlert$101$orgtelegramuiLaunchActivity(HashMap keys, LocaleController.LocaleInfo[] infos, String systemLang) {
        this.englishLocaleStrings = keys;
        if (keys != null && this.systemLocaleStrings != null) {
            showLanguageAlertInternal(infos[1], infos[0], systemLang);
        }
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
            this.lockRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity.19
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
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000L);
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
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onPasscodeResume");
            }
        }
    }

    private void updateCurrentConnectionState(int account) {
        if (this.actionBarLayout == null) {
            return;
        }
        String title = null;
        int titleId = 0;
        Runnable action = null;
        int connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        this.currentConnectionState = connectionState;
        if (connectionState == 2) {
            title = "WaitingForNetwork";
            titleId = R.string.WaitingForNetwork;
        } else if (connectionState == 5) {
            title = "Updating";
            titleId = R.string.Updating;
        } else if (connectionState == 4) {
            title = "ConnectingToProxy";
            titleId = R.string.ConnectingToProxy;
        } else if (connectionState == 1) {
            title = "Connecting";
            titleId = R.string.Connecting;
        }
        if (connectionState == 1 || connectionState == 4) {
            action = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.m3673x2dcba16c();
                }
            };
        }
        this.actionBarLayout.setTitleOverlayText(title, titleId, action);
    }

    /* renamed from: lambda$updateCurrentConnectionState$103$org-telegram-ui-LaunchActivity */
    public /* synthetic */ void m3673x2dcba16c() {
        BaseFragment lastFragment = null;
        if (AndroidUtilities.isTablet()) {
            if (!layerFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = layerFragmentsStack;
                lastFragment = arrayList.get(arrayList.size() - 1);
            }
        } else if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            lastFragment = arrayList2.get(arrayList2.size() - 1);
        }
        if ((lastFragment instanceof ProxyListActivity) || (lastFragment instanceof ProxySettingsActivity)) {
            return;
        }
        m3653lambda$runLinkRequest$59$orgtelegramuiLaunchActivity(new ProxyListActivity());
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode == null) {
            return;
        }
        actionMode.finish();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            BaseFragment lastFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                lastFragment = this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            }
            if (lastFragment != null) {
                Bundle args = lastFragment.getArguments();
                if ((lastFragment instanceof ChatActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "chat");
                } else if ((lastFragment instanceof GroupCreateFinalActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "group");
                } else if (lastFragment instanceof WallpapersListActivity) {
                    outState.putString("fragment", "wallpapers");
                } else if (lastFragment instanceof ProfileActivity) {
                    ProfileActivity profileActivity = (ProfileActivity) lastFragment;
                    if (profileActivity.isSettings()) {
                        outState.putString("fragment", "settings");
                    } else if (profileActivity.isChat() && args != null) {
                        outState.putBundle("args", args);
                        outState.putString("fragment", "chat_profile");
                    }
                } else if ((lastFragment instanceof ChannelCreateActivity) && args != null && args.getInt("step") == 0) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "channel");
                }
                lastFragment.saveSelfArgs(outState);
            }
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
                return;
            }
            boolean cancel = false;
            if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                BaseFragment lastFragment = this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                cancel = true ^ lastFragment.onBackPressed();
            }
            if (!cancel) {
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
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onLowMemory();
                this.layersActionBarLayout.onLowMemory();
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        this.visibleActionMode = mode;
        try {
            Menu menu = mode.getMenu();
            if (menu != null) {
                boolean extended = this.actionBarLayout.extendActionMode(menu);
                if (!extended && AndroidUtilities.isTablet()) {
                    boolean extended2 = this.rightActionBarLayout.extendActionMode(menu);
                    if (!extended2) {
                        this.layersActionBarLayout.extendActionMode(menu);
                    }
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (Build.VERSION.SDK_INT >= 23 && mode.getType() == 1) {
            return;
        }
        this.actionBarLayout.onActionModeStarted(mode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeStarted(mode);
            this.layersActionBarLayout.onActionModeStarted(mode);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        if (this.visibleActionMode == mode) {
            this.visibleActionMode = null;
        }
        if (Build.VERSION.SDK_INT >= 23 && mode.getType() == 1) {
            return;
        }
        this.actionBarLayout.onActionModeFinished(mode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeFinished(mode);
            this.layersActionBarLayout.onActionModeFinished(mode);
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        event.getKeyCode();
        if (event.getAction() == 0 && (event.getKeyCode() == 24 || event.getKeyCode() == 25)) {
            boolean mute = true;
            if (VoIPService.getSharedInstance() != null) {
                if (Build.VERSION.SDK_INT >= 32) {
                    boolean oldValue = WebRtcAudioTrack.isSpeakerMuted();
                    AudioManager am = (AudioManager) getSystemService("audio");
                    int minVolume = am.getStreamMinVolume(0);
                    if (am.getStreamVolume(0) != minVolume || event.getKeyCode() != 25) {
                        mute = false;
                    }
                    WebRtcAudioTrack.setSpeakerMute(mute);
                    if (oldValue != WebRtcAudioTrack.isSpeakerMuted()) {
                        showVoiceChatTooltip(mute ? 42 : 43);
                    }
                }
            } else if (!mainFragmentsStack.isEmpty() && ((!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && event.getRepeatCount() == 0)) {
                ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                BaseFragment fragment = arrayList.get(arrayList.size() - 1);
                if ((fragment instanceof ChatActivity) && ((ChatActivity) fragment).maybePlayVisibleVideo()) {
                    return true;
                }
                if (AndroidUtilities.isTablet() && !rightFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
                    BaseFragment fragment2 = arrayList2.get(arrayList2.size() - 1);
                    if ((fragment2 instanceof ChatActivity) && ((ChatActivity) fragment2).maybePlayVisibleVideo()) {
                        return true;
                    }
                }
            }
        }
        try {
            super.dispatchKeyEvent(event);
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(keyCode, event);
                } else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.rightActionBarLayout.onKeyUp(keyCode, event);
                } else {
                    this.actionBarLayout.onKeyUp(keyCode, event);
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
                this.actionBarLayout.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout) {
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            this.drawerLayoutContainer.setAllowOpenDrawer(!(fragment instanceof LoginActivity) && !(fragment instanceof IntroActivity) && !(fragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if (fragment instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) fragment;
                if (dialogsActivity.isMainDialogList() && layout != (actionBarLayout4 = this.actionBarLayout)) {
                    actionBarLayout4.removeAllFragments();
                    this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false, false);
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
            }
            if ((fragment instanceof ChatActivity) && !((ChatActivity) fragment).isInScheduleMode()) {
                boolean result = this.tabletFullSize;
                if ((!result && layout == this.rightActionBarLayout) || (result && layout == this.actionBarLayout)) {
                    boolean result2 = (result && layout == (actionBarLayout = this.actionBarLayout) && actionBarLayout.fragmentsStack.size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    if (!result2) {
                        this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false, false);
                    }
                    return result2;
                } else if (!result && layout != (actionBarLayout3 = this.rightActionBarLayout)) {
                    actionBarLayout3.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(fragment, removeLast, true, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a2 = 0; a2 < this.layersActionBarLayout.fragmentsStack.size() - 1; a2 = (a2 - 1) + 1) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    return false;
                } else if (result && layout != (actionBarLayout2 = this.actionBarLayout)) {
                    actionBarLayout2.presentFragment(fragment, actionBarLayout2.fragmentsStack.size() > 1, forceWithoutAnimation, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a3 = 0; a3 < this.layersActionBarLayout.fragmentsStack.size() - 1; a3 = (a3 - 1) + 1) {
                            ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                            actionBarLayout7.removeFragmentFromStack(actionBarLayout7.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    return false;
                } else {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a4 = 0; a4 < this.layersActionBarLayout.fragmentsStack.size() - 1; a4 = (a4 - 1) + 1) {
                            ActionBarLayout actionBarLayout8 = this.layersActionBarLayout;
                            actionBarLayout8.removeFragmentFromStack(actionBarLayout8.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    ActionBarLayout actionBarLayout9 = this.actionBarLayout;
                    actionBarLayout9.presentFragment(fragment, actionBarLayout9.fragmentsStack.size() > 1, forceWithoutAnimation, false, false);
                    return false;
                }
            }
            ActionBarLayout actionBarLayout10 = this.layersActionBarLayout;
            if (layout != actionBarLayout10) {
                actionBarLayout10.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (fragment instanceof LoginActivity) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                }
                this.layersActionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false, false);
                return false;
            }
        } else {
            boolean allow = true;
            if ((fragment instanceof LoginActivity) || (fragment instanceof IntroActivity)) {
                if (mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) {
                    allow = false;
                }
            } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
                allow = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(allow, false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout) {
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        if (AndroidUtilities.isTablet()) {
            this.drawerLayoutContainer.setAllowOpenDrawer(!(fragment instanceof LoginActivity) && !(fragment instanceof IntroActivity) && !(fragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if (fragment instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) fragment;
                if (dialogsActivity.isMainDialogList() && layout != (actionBarLayout3 = this.actionBarLayout)) {
                    actionBarLayout3.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(fragment);
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
            } else if ((fragment instanceof ChatActivity) && !((ChatActivity) fragment).isInScheduleMode()) {
                boolean z = this.tabletFullSize;
                if (!z && layout != (actionBarLayout2 = this.rightActionBarLayout)) {
                    actionBarLayout2.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(fragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            ActionBarLayout actionBarLayout4 = this.layersActionBarLayout;
                            actionBarLayout4.removeFragmentFromStack(actionBarLayout4.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (z && layout != (actionBarLayout = this.actionBarLayout)) {
                    actionBarLayout.addFragmentToStack(fragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (int a2 = 0; a2 < this.layersActionBarLayout.fragmentsStack.size() - 1; a2 = (a2 - 1) + 1) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                if (layout != actionBarLayout6) {
                    actionBarLayout6.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (fragment instanceof LoginActivity) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                    }
                    this.layersActionBarLayout.addFragmentToStack(fragment);
                    return false;
                }
            }
        } else {
            boolean allow = true;
            if ((fragment instanceof LoginActivity) || (fragment instanceof IntroActivity)) {
                if (mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) {
                    allow = false;
                }
            } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
                allow = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(allow, false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needCloseLastFragment(ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            if (layout == this.actionBarLayout && layout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (layout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (layout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (layout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (layout.fragmentsStack.size() >= 2 && !(layout.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean last) {
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.rebuildAllFragmentViews(last, last);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public void onRebuildAllFragments(ActionBarLayout layout, boolean last) {
        if (AndroidUtilities.isTablet() && layout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(last, last);
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
